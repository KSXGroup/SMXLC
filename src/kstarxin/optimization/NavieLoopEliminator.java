package kstarxin.optimization;

import kstarxin.ast.*;
import kstarxin.utilities.SymbolTable;

import java.util.HashSet;
import java.util.LinkedList;

//this is also a pure data oriented optimization
//everything shit, i hate this life!

public class NavieLoopEliminator implements ASTBaseVisitor<Void> {

    private ProgramNode ast;
    private boolean inLoop;
    private boolean inCond;
    private boolean isRhs;
    private SymbolTable globalSymbolTable;
    private SymbolTable localSymbolTable;
    private boolean canBeEliminated;

    public NavieLoopEliminator(ProgramNode _ast){
        ast = _ast;
        inLoop = false;
        globalSymbolTable = ast.getCurrentSymbolTable();
        canBeEliminated = false;
    }

    public void eliminateIrrelevantLoop(){
        visit(ast);
    }

    @Override
    public Void visit(Node node) {
        return node.accept(this);
    }

    @Override
    public Void visit(ProgramNode node) {
        if(node.getClassDeclarations().size() > 0) return null;
        else node.getMethodDeclarations().forEach(this::visit);
        return null;
    }

    @Override
    public Void visit(DeclarationNode node) {
        return node.accept(this);
    }

    @Override
    public Void visit(BlockNode node) {
        HashSet<LoopNode> toRemove = new HashSet<LoopNode>();

        localSymbolTable = node.getCurrentSymbolTable();
       LinkedList<Node> stmLst = node.getStatementList();
       for(Node n : stmLst){
           if(inLoop && n instanceof LoopNode){
               canBeEliminated = false;
               return null;
           }else if(!inLoop && n instanceof LoopNode) {
               visit(n);
               if(canBeEliminated && ((LoopNode) n).getBody() instanceof ExpressionNode)
                   toRemove.add((LoopNode) n);
           }
       }
       LinkedList<Node> newStm = new LinkedList<Node>();
       for(Node n : stmLst){
           if(!toRemove.contains(n))
               newStm.add(n);
           else System.err.println("eliminate loop!");
       }
       node.setStatementList(newStm);
       return null;
    }

    @Override
    public Void visit(StatementNode node) {
        return node.accept(this);
    }

    @Override
    public Void visit(VariableDeclarationNode node) {
         return null;
    }

    @Override
    public Void visit(VariableDeclaratorNode node) {
        return null;
    }

    @Override
    public Void visit(ClassDeclarationNode node) {
        return null;
    }

    @Override
    public Void visit(MethodDeclarationNode node) {
        return visit(node.getBlock());
    }

    @Override
    public Void visit(ParameterDeclarationNode node) {
        return null;
    }

    @Override
    public Void visit(ConditionNode node) {
        return null;
    }

    @Override
    public Void visit(LoopNode node) {
        canBeEliminated = true;
        inLoop = true;
        if(node.getCondition() == null || (node.getCondition() instanceof BooleanConstantNode)) return null;
        else{
            if(node.getInitializer() != null)
                visit(node.getInitializer());
            else{
                canBeEliminated = false;
                inLoop = false;
                return null;
            }
            if(canBeEliminated == false) {
                inLoop = false;
                return null;
            }
            inCond = true;
            visit(node.getCondition());
            inCond = false;
            if(canBeEliminated == false) {
                inLoop = false;
                return null;
            }
            if(node.getBody() == null){
                inLoop = false;
                canBeEliminated = false;
                return null;
            }else
                visit(node.getBody());
            if(node.getStep() == null) {
                inLoop = false;
                return null;
            }
            else{
                visit(node.getStep());
                inLoop = false;
                return null;
            }
        }
    }

    @Override
    public Void visit(JumpNode node) {
        return node.accept(this);
    }

    @Override
    public Void visit(BreakNode node) {
        canBeEliminated = false;
        return null;
    }

    @Override
    public Void visit(ContinueNode node) {
        canBeEliminated = false;
        return null;
    }

    @Override
    public Void visit(ReturnNode node) {
        canBeEliminated = false;
        return null;
    }

    @Override
    public Void visit(ExpressionNode node) {
        return node.accept(this);
    }

    @Override
    public Void visit(IntegerConstantNode node) {
        return null;
    }

    @Override
    public Void visit(BooleanConstantNode node) {
        return null;
    }

    @Override
    public Void visit(StringConstantNode node) {
        return null;
    }

    @Override
    public Void visit(NullConstantNode node) {
        return null;
    }

    @Override
    public Void visit(BinaryExpressionNode node) {
        isRhs = true;
        visit(node.getRight());
        isRhs = false;
        if(canBeEliminated == false)
            return null;
        visit(node.getLeft());
        return null;
    }

    @Override
    public Void visit(DotMemberMethodCallNode node) {
        //this may refer to mem access
        canBeEliminated =false;
        return null;
    }

    @Override
    public Void visit(MethodCallNode node) {
        canBeEliminated = false;
        return null;
    }

    @Override
    public Void visit(DotMemberNode node) {
        canBeEliminated = false;
        return null;
    }

    @Override
    public Void visit(IndexAccessNode node) {
        canBeEliminated = false;
        return null;
    }

    @Override
    public Void visit(IdentifierNode node) {
        if(globalSymbolTable.contains(node.getIdentifier()))
            canBeEliminated = false;
        if(inCond && isRhs && localSymbolTable.contains(node.getIdentifier()))
            canBeEliminated = false;
        return null;
    }

    @Override
    public Void visit(NewCreatorNode node) {
        canBeEliminated = false;
        return null;
    }

    @Override
    public Void visit(SuffixIncreaseDecreaseNode node) {
        return visit(node.getExpression());
    }

    @Override
    public Void visit(ThisNode node) {
        canBeEliminated = false;
        return null;
    }

    @Override
    public Void visit(UnaryExpressionNode node) {
        return visit(node.getRight());
    }
}
