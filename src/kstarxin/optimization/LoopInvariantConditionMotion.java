package kstarxin.optimization;

import kstarxin.ast.*;
import kstarxin.ir.instruction.BinaryArithmeticInstruction;
import kstarxin.parser.MxStarParser;
import kstarxin.utilities.Symbol;
import kstarxin.utilities.SymbolTable;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;

//To some extent, this is a data oriented optimization
public class LoopInvariantConditionMotion implements ASTBaseVisitor<Void> {
    boolean canMoveCondition;
    HashSet<Symbol> symbolInCond;
    ProgramNode ast;

    public LoopInvariantConditionMotion(ProgramNode _ast){
        ast = _ast;
        symbolInCond = new HashSet<Symbol>();
    }

    public void moveLoopInvariantCondition(){
        visit(ast);
    }

    private boolean isTrival(Node n){
        if(n instanceof IdentifierNode || n instanceof NullConstantNode || n instanceof StringConstantNode || n instanceof IntegerConstantNode || n instanceof BooleanConstantNode)
            return true;
        else return false;
    }

    private boolean checkLoop(LoopNode node){
        Node loopBody = node.getBody();
        ConditionNode cn = null;
        if(loopBody instanceof BlockNode || loopBody instanceof ConditionNode){
            if(loopBody instanceof BlockNode) {
                if (((BlockNode) loopBody).getStatementList().size() != 1) return false;
                else if (!(((BlockNode) loopBody).getStatementList().get(0) instanceof ConditionNode)) return false;
                cn = (ConditionNode) ((BlockNode) loopBody).getStatementList().get(0);
            }else cn = (ConditionNode) loopBody;
            if(cn.getElse() != null) return false;
            if(!(cn.getCond() instanceof BinaryExpressionNode
                    && isTrival(((BinaryExpressionNode) cn.getCond()).getLeft())
                    && isTrival(((BinaryExpressionNode) cn.getCond()).getRight()))) return false;
            symbolInCond.clear();
            String id1 = null;
            String id2 = null;
            if(((BinaryExpressionNode) cn.getCond()).getLeft() instanceof IdentifierNode)
                id1 = ((IdentifierNode)(((BinaryExpressionNode) cn.getCond()).getLeft())).getIdentifier();
            if(((BinaryExpressionNode) cn.getCond()).getRight() instanceof IdentifierNode)
                id2 = ((IdentifierNode)(((BinaryExpressionNode) cn.getCond()).getRight())).getIdentifier();
            if(id1 != null) {
                Symbol ids1 = cn.getCurrentSymbolTable().get(id1, ((BinaryExpressionNode) cn.getCond()).getLeft().getLocation());
                symbolInCond.add(ids1);
            }
            if(id2 != null) {
                Symbol ids2 = cn.getCurrentSymbolTable().get(id2, ((BinaryExpressionNode) cn.getCond()).getRight().getLocation());
                symbolInCond.add(ids2);
            }
            canMoveCondition = true;
            if(node.getCondition() != null)
                visit(node.getCondition());
            if(!canMoveCondition) return false;
            if(node.getStep() != null)
                visit(node.getStep());
            if(!canMoveCondition) return false;
            visit(cn.getBody());
            if(canMoveCondition) return true;
        }
        return false;
    }

    @Override
    public Void visit(Node node) {
        return node.accept(this);
    }

    @Override
    public Void visit(ProgramNode node) {
        node.getMethodDeclarations().forEach(this::visit);
        return null;
    }

    @Override
    public Void visit(DeclarationNode node) {
        return node.accept(this);
    }

    @Override
    public Void visit(BlockNode node) {
        LinkedList<Node> newStm = new LinkedList<Node>();
        for(Node stm : node.getStatementList()){
            if(stm instanceof LoopNode){
                if(checkLoop((LoopNode) stm)){
                    System.err.println("move condtion!!!");
                    ConditionNode loopBodyWhichIsAConditionNode = null;
                    if(((LoopNode) stm).getBody() instanceof BlockNode)
                        loopBodyWhichIsAConditionNode = (ConditionNode) ((BlockNode) ((LoopNode) stm).getBody()).getStatementList().get(0);
                    else loopBodyWhichIsAConditionNode = (ConditionNode) ((LoopNode) stm).getBody();
                    Node conditonBody = loopBodyWhichIsAConditionNode.getBody();
                    ((LoopNode) stm).setBody(conditonBody);
                    loopBodyWhichIsAConditionNode.setThen(stm);
                    newStm.add(loopBodyWhichIsAConditionNode);
                }else{
                    visit(stm);
                    newStm.add(stm);
                }
            }else{
                visit(stm);
                newStm.add(stm);
            }
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
        node.getMemberMethodList().forEach(this::visit);
        node.getConstructorList().forEach(this::visit);
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
        if(node.getInitializer() != null)
            visit(node.getInitializer());
        if(node.getBody() != null)
            visit(node.getBody());
        if(node.getStep() != null)
            visit(node.getStep());
        return null;
    }

    @Override
    public Void visit(JumpNode node) {
        return node.accept(this);
    }

    @Override
    public Void visit(BreakNode node) {
        canMoveCondition = false;
        return null;
    }

    @Override
    public Void visit(ContinueNode node) {
        canMoveCondition = false;
        return null;
    }

    @Override
    public Void visit(ReturnNode node) {
        canMoveCondition = false;
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
        if(node.getOp() == MxStarParser.ASSIGN){
            if(node.getLeft() instanceof IdentifierNode){
                Symbol s = node.getCurrentSymbolTable().get(((IdentifierNode) node.getLeft()).getIdentifier(), node.getLocation());
                if(symbolInCond.contains(s))
                    canMoveCondition = false;
            }
        }
        return null;
    }

    @Override
    public Void visit(DotMemberMethodCallNode node) {
        node.getParameterExpressionList().forEach(this::visit);
        visit(node.getExpression());
        return null;
    }

    @Override
    public Void visit(MethodCallNode node) {
        node.getParameterExpressionList().forEach(this::visit);
        return null;
    }

    @Override
    public Void visit(DotMemberNode node) {
        visit(node.getExpression());
        return null;
    }

    @Override
    public Void visit(IndexAccessNode node) {
        visit(node.getExpression());
        visit(node.getIndex());
        return null;
    }

    @Override
    public Void visit(IdentifierNode node) {
        return null;
    }

    @Override
    public Void visit(NewCreatorNode node) {
        node.getSizeExpressionList().forEach(this::visit);
        node.getParameterList().forEach(this::visit);
        return null;
    }

    @Override
    public Void visit(SuffixIncreaseDecreaseNode node) {
        if(node.getExpression() instanceof IdentifierNode)
            if(symbolInCond.contains(node.getCurrentSymbolTable().get(((IdentifierNode) node.getExpression()).getIdentifier(), node.getLocation())))
                canMoveCondition = false;
        else visit(node.getExpression());
        return null;
    }

    @Override
    public Void visit(ThisNode node) {
        return null;
    }

    @Override
    public Void visit(UnaryExpressionNode node) {
        if(node.getRight() instanceof IdentifierNode)
            if(symbolInCond.contains(node.getCurrentSymbolTable().get(((IdentifierNode) node.getRight()).getIdentifier(), node.getLocation())))
                canMoveCondition = false;
        else visit(node.getRight());
        return null;
    }
}
