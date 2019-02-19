package kstarxin.ir;

import kstarxin.ast.*;
import kstarxin.compiler.*;
import kstarxin.ir.instruction.*;
import kstarxin.ir.operand.*;
import kstarxin.utilities.*;
import org.graalvm.compiler.nodes.ConstantNode;

import java.util.*;

class NameMangler{
    public static String globalPrefix = "@";
    public static String methodPrefix = "_Z";
    public static String initMethod = "@_INIT_";

    public static boolean isGlobal(String scopeName){
        if(scopeName.equals(ASTBuilderVisitor.globalScopeName)) return true;
        else return false;
    }

    public static String mangleName(VariableDeclaratorNode n){
        String scopeName = n.getCurrentSymbolTable().getName();
        String idName = n.getIdentifier();
        if(isGlobal(scopeName)) return globalPrefix + idName;
        else return scopeName + "_" + idName;
    }

    public static String mangleName(MethodDeclarationNode n){
        if(n.getIdentifier().equals("main")) return globalPrefix + "main"; // ignore all parameters of main method
        String mangledName =globalPrefix + methodPrefix + n.getIdentifier();
        for(MxType t : n.getReturnType().getParameterTypeList()) {
            if(t.isPrimitiveType()) mangledName += t.toString().charAt(0);
            else mangledName += t.toString();
        }
        return mangledName;
    }

    public static String mangleName(Symbol s){
        Symbol.SymbolType st = s.getSymbolType();
        String ret = "";
        String scopeName = "";
        String symbolId = s.getIdentifier();
        switch (st){
            case METHOD:
                if(symbolId.equals("main")) return globalPrefix + symbolId;
                else{
                    ret = methodPrefix + globalPrefix + symbolId;
                    for(MxType t : s.getType().getParameterTypeList()){
                        if(t.isPrimitiveType()) ret += t.toString().charAt(0);
                        else ret += t.toString();
                    }
                }
                return ret;

            case VARIABLE:
                if(isGlobal(s.getScopeName())) return globalPrefix + s.getIdentifier();
                else return s.getScopeName() + "_" + s.getIdentifier();

            case CLASS:
                return s.getIdentifier();
        }
        return null;
    }

}


public class IRBuilderVisitor implements ASTBaseVisitor<Operand> {

    private IRProgram ir;
    private ProgramNode ast;
    private Method currentMethod;
    private BasicBlock currentBasicBlock;

    public IRBuilderVisitor(ProgramNode _ast){
        ast = _ast;
        ir = new IRProgram();
    }


    //resolve global variable and make program entrance
    private void makeEntranceMethod(){
        for(VariableDeclarationNode  n : ast.getVariableDeclarations()){
            //process global variable
            MxType t = n.getTypeNode().getType();
            if(!t.isPrimitiveType()){
                for(VariableDeclaratorNode decl: n.getDeclaratorList()){
                    StaticPointer sp = new StaticPointer(decl.getIdentifier(), Configure.PTR_SIZE);
                    ExpressionNode init = decl.getInitializer();
                    if(init == null){
                        String className = t.toString();
                        SymbolTable classMemberTable = ast.getCurrentSymbolTable().getMember(className).getMemberTable();
                        Symbol cons = classMemberTable.getMember(ASTBuilderVisitor.constructorPrefix + t.toString());
                        if(cons != null){
                            Label callee = new Label(NameMangler.mangleName(cons));
                            CallInstruction call = new CallInstruction(callee);
                            call.addParameter(sp);
                            currentBasicBlock.insertEnd(call); //initialize the class type variable
                        }
                    }
                    else{
                        //TODO(MAY NOT, BECAUSE THE MANUAL NOT SUPPORT THIS)
                    }
                }
            }
            else if (t.isPrimitiveType() && !t.getEnumType().equals(MxType.TypeEnum.STRING)){
                for (VariableDeclaratorNode decl : n.getDeclaratorList()) {
                    StaticPointer sp = new StaticPointer(decl.getIdentifier(), Configure.PTR_SIZE);
                    ExpressionNode init = decl.getInitializer();
                    ir.addGlobalVariable(NameMangler.mangleName(decl), sp, t);
                    if (init != null) {
                        if(init instanceof IntegerConstantNode) sp.setValue(((IntegerConstantNode) init).getValue());
                        else if(init instanceof BooleanConstantNode){
                            if(((BooleanConstantNode) init).getValue() == true) sp.setValue(1);
                            else sp.setValue(0);
                        }
                        else {
                            Operand v = visit(init);
                            if(v instanceof Immediate) sp.setValue(((Immediate) v).value);
                            else currentBasicBlock.insertEnd(new StoreInstruction(sp, v, new Immediate(0)));
                        }
                    }
                }
            }
            else
                n.getDeclaratorList().forEach(decl -> ir.addGlobalVariable(NameMangler.mangleName(decl), new StaticString(decl.getIdentifier(), ((StringConstantNode)decl.getInitializer()).getValue()), t));
        }

    }

    public IRProgram buildIR(){
        currentMethod = new Method(NameMangler.initMethod);
        BasicBlock startBB = new BasicBlock(null);
        currentMethod.addBasicBlock(startBB);
        currentMethod.setStartBlock(startBB);
        currentMethod.setEndBlock(startBB);
        currentBasicBlock = startBB;
        makeEntranceMethod();
        ir.setEntranceMethod(currentMethod);
        visit(ast);
        return ir;
    }

    @Override
    public Operand visit(Node node) {
        return node.accept(this);
    }

    @Override
    public Operand visit(ProgramNode node) {
        return null;
    }

    @Override
    public Operand visit(DeclarationNode node) {
        return node.accept(this);
    }


    @Override
    public Operand visit(MethodDeclarationNode node) {
        return null;
    }

    @Override
    public Operand visit(ClassDeclarationNode node) {
        return null;
    }

    @Override
    public Operand visit(VariableDeclarationNode node) {
        return null;
    }

    @Override
    public Operand visit(VariableDeclaratorNode node) {
        return null;
    }

    @Override
    public Operand visit(BlockNode node) {
        return null;
    }

    @Override
    public Operand visit(StatementNode node) {
        return node.accept(this);
    }

    @Override
    public Operand visit(ConditionNode node) {
        return null;
    }

    @Override
    public Operand visit(LoopNode node) {
        return null;
    }

    @Override
    public Operand visit(JumpNode node) {
        return node.accept(this);
    }

    @Override
    public Operand visit(ContinueNode node) {
        return null;
    }

    @Override
    public Operand visit(BreakNode node) {
        return null;
    }

    @Override
    public Operand visit(ReturnNode node) {
        return null;
    }

    @Override
    public Operand visit(DotMemberNode node) {
        return null;
    }

    @Override
    public Operand visit(MethodCallNode node) {
        return null;
    }

    @Override
    public Operand visit(DotMemberMethodCallNode node) {
        return null;
    }

    @Override
    public Operand visit(ExpressionNode node) {
        return null;
    }

    @Override
    public Operand visit(UnaryExpressionNode node) {
        return null;
    }

    @Override
    public Operand visit(BinaryExpressionNode node) {
        return null;
    }

    @Override
    public Operand visit(NewCreatorNode node) {
        return null;
    }

    @Override
    public Operand visit(ThisNode node) {
        return null;
    }

    @Override
    public Operand visit(IndexAccessNode node) {
        return null;
    }

    @Override
    public Operand visit(SuffixIncreaseDecreaseNode node) {
        return null;
    }

    @Override
    public Operand visit(IdentifierNode node) {
        return null;
    }

    @Override
    public Operand visit(ParameterDeclarationNode node) {
        return null;
    }

    @Override
    public Operand visit(BooleanConstantNode node) {
        return null;
    }

    @Override
    public Operand visit(NullConstantNode node) {
        return null;
    }

    @Override
    public Operand visit(StringConstantNode node) {
        return null;
    }

    @Override
    public Operand visit(IntegerConstantNode node) {
        return null;
    }
}
