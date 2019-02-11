package kstarxin.ast;

import kstarxin.parser.MxStarParser;
import kstarxin.utilities.CompileException;
import kstarxin.utilities.MxType;
import kstarxin.utilities.SymbolTable;
import kstarxin.utilities.Symbol;

import java.util.*;


public class ASTTypeCheckerVisitor implements ASTBaseVisitor<Void> {
    ProgramNode ast;
    private int     inLoop;
    private boolean inMethod;
    private boolean inClass;
    private boolean inClassConstructor;
    private boolean methodReturned;
    private String currentClassName;
    private String currentMethodName;
    private MxType currentMethodReturnType;


    public ASTTypeCheckerVisitor(ProgramNode _ast) {
        inLoop = 0;
        inClass = false;
        inMethod = false;
        methodReturned = false;
        inClassConstructor = false;
        currentClassName = "";
        currentMethodName = "";
        currentMethodReturnType = null;
        ast = _ast;
    }

    private boolean isBooleanType(MxType t){
        if(t.getEnumType() == MxType.TypeEnum.BOOL && t.getDimension() == 0) return true;
        else return false;
    }

    private boolean isIntegerType(MxType t){
        if(t.getEnumType().equals(MxType.TypeEnum.INT) && t.getDimension() == 0) return true;
        else return false;
    }

    private boolean isStringType(MxType t){
        if(t.getEnumType().equals(MxType.TypeEnum.STRING) && t.getDimension() == 0) return true;
        else return true;
    }

    private boolean canHaveLogicOperation(MxType a, MxType b){
        boolean fa, fb;
        if((a.getEnumType().equals(MxType.TypeEnum.INT) || a.getEnumType().equals(MxType.TypeEnum.BOOL)) && a.getDimension() == 0) fa = true;
        else fa = false;
        if((b.getEnumType().equals(MxType.TypeEnum.INT) || b.getEnumType().equals(MxType.TypeEnum.BOOL)) && b.getDimension() == 0) fb = true;
        else fb = false;
        if(fa && fb) return true;
        else return false;
    }

    private boolean canBeAdded(MxType a, MxType b){
        if(isIntegerType(a) && isIntegerType(b)) return true;
        else if(isStringType(a) && isStringType(b)) return true;
        else return false;
    }

    private boolean canAssignedNull(MxType t){
        if(t.getDimension() > 0) return true;
        if(t.getEnumType().equals(MxType.TypeEnum.STRING) || t.getEnumType().equals(MxType.TypeEnum.CLASS)) return true;
        return false;
    }

    public void checkType(){
        visit(ast);
    }

    @Override
    public Void visit(Node node) {
        return node.accept(this);
    }


    @Override
    public Void visit(DeclarationNode node) {
        return node.accept(this);
    }

    @Override
    public Void visit(ProgramNode node) {
        node.getVariableDeclarations().forEach(this::visit);
        node.getMethodDeclarations().forEach(this::visit);
        node.getClassDeclarations().forEach(this::visit);
        return null;
    }


    @Override
    public Void visit(BlockNode node) {
        node.getStatementList().forEach(this::visit);
        return null;
    }


    @Override
    public Void visit(StatementNode node) {
        return node.accept(this);
    }

    @Override
    public Void visit(VariableDeclarationNode node) {
        MxType varT = node.getTypeNode().getType();
        ExpressionNode expr = null;
        LinkedList<VariableDeclaratorNode> lst = node.getDeclaratorList();
        for(VariableDeclaratorNode n: lst){
            expr = n.getInitializer();
            if(expr != null){
                visit(expr);
                if(!varT.equals(expr.getType())) throw new CompileException("Type not equal " + node.getLocation().getLineNumberString() + ":" + node.getLocation().getColumnNumberString());
            }
        }
        return null;
    }

    @Override
    public Void visit(ClassDeclarationNode node) {
        inClass = true;
        currentClassName = node.getName();
        node.getConstructorList().forEach(this::visit);
        node.getMemberMethodList().forEach(this::visit);
        inClass = false;
        return null;
    }

    @Override
    public Void visit(MethodDeclarationNode node) {
        inMethod = true;
        methodReturned = false;
        currentMethodName = node.getIdentifier();
        currentMethodReturnType = node.getReturnType();
        if(node.getCurrentSymbolTable().getName().indexOf(ASTBuilderVisitor.constructorPrefix) != -1 && inClass){
            inClassConstructor = true;
            if(!currentMethodName.equals(currentClassName)) {
                throw new CompileException("Member method " + currentMethodName + " of class " + currentClassName + " should have return value");
            }
            if(!node.getReturnType().toString().equals(currentClassName) || node.getReturnType().getDimension() != 0){
                throw new CompileException("Class constructor should not have return value");
            }
        }
        visit(node.getBlock());
        if(!methodReturned && !node.getReturnType().getEnumType().equals(MxType.TypeEnum.VOID)) throw new CompileException("Method not returned");
        inMethod = false;
        methodReturned = false;
        currentMethodName = "";
        inClassConstructor = false;
        currentMethodReturnType = null;
        return null;
    }

    @Override
    public Void visit(ParameterDeclarationNode node) {
        return null;
    }

    @Override
    public Void visit(ConditionNode node) {
        ExpressionNode cond = node.getCond();
        Node body = node.getBody();
        ConditionNode celse = node.getElse();
        visit(cond);
        if(!isBooleanType(cond.getType())) throw new CompileException("cond should be bool!");
        if(body != null) visit(body);
        if(celse != null) visit(celse);
        return null;
    }

    @Override
    public Void visit(LoopNode node) {
        inLoop++;
        ExpressionNode init = node.getInitializer(), cond = node.getCondition(), step = node.getStep();
        Node body = node.getBody();
        if(init != null) visit(init);
        if(step != null) visit(step);
        if(cond != null) visit(cond);
        else if(!node.isForLoop()) throw new CompileException("while loop should have cond!");
        if(!isBooleanType(cond.getType())) throw new CompileException("cond should be boolean!");
        if(body != null) visit(body);
        inLoop--;
        return null;
    }

    @Override
    public Void visit(VariableDeclaratorNode node) {
        return null;
    }

    @Override
    public Void visit(JumpNode node) {
        return node.accept(this);
    }

    @Override
    public Void visit(ContinueNode node) {
        if(inLoop == 0) throw new CompileException("Continue not in loop");
        return null;
    }

    @Override
    public Void visit(BreakNode node) {
        if(inLoop == 0) throw new CompileException("Break no in loop");
        return null;
    }

    @Override
    public Void visit(ReturnNode node) {
        if(!(inMethod || inClassConstructor)) throw new CompileException("Invalid return!");
        else if(inMethod) if(!currentMethodReturnType.equals(node.getReturnValue().getType())) throw new CompileException("Return type not match");
        else methodReturned = true;
        return null;
    }

    @Override
    public Void visit(ExpressionNode node) {
        return node.accept(this);
    }

    @Override
    public Void visit(SuffixIncreaseDecreaseNode node) {
        ExpressionNode expr = node.getExpression();
        visit(expr);
        if(!isIntegerType(expr.getType())) throw new CompileException("Invalid use of suffix inc/dec");
        expr.setType(new MxType(MxType.TypeEnum.INT));
        node.setRightValue();
        return null;
    }

    @Override
    public Void visit(IndexAccessNode node) {
        MxType exprt = node.getExpression().getType(), idxt = node.getIndex().getType(), nt = node.getType();
        visit(node.getIndex());
        if(!isIntegerType(idxt)) throw new CompileException("Invalid index!");
        if(exprt.getDimension() == 0) throw new CompileException("No index access to non-array type");
        node.setType(new MxType(exprt.getEnumType(), exprt.toString(), exprt.getDimension() - 1));
        node.setLeftValue();
        return null;
    }

    @Override
    public Void visit(DotMemberMethodCallNode node) {
        String memId = node.getMemberMethodName();
        ExpressionNode expr = node.getExpression();
        visit(expr);
        MxType t = expr.getType();
        Symbol classSymbol = null;
        SymbolTable memberTable = null;
        if(t.getEnumType().equals(MxType.TypeEnum.STRING)){
            classSymbol = node.getCurrentSymbolTable().get(ASTBuilderVisitor.builtinStringClassName);
        }
        else if(t.getDimension() > 0 && memId.equals("size")){
            classSymbol = node.getCurrentSymbolTable().get(ASTBuilderVisitor.builtinArrayClassName);
        }
        else {
            classSymbol = node.getCurrentSymbolTable().get(t.toString());
        }
        memberTable = classSymbol.getMemberTable();
        Symbol classMemberSymbol = memberTable.get(memId);
        if(classMemberSymbol == null) throw new CompileException("Type " + t.toString() + " has no member " + memId);
        else if(!classMemberSymbol.isMethod()) throw new CompileException("Member " + memId + " can not be used as method");
        else if(classMemberSymbol.getType().getParameterTypeList().size() != node.getParameterExpressionList().size()) throw new CompileException("Parameter number not match when call member method " + memId );
        else if(node.getParameterExpressionList().size() > 0){
            int sz = node.getParameterExpressionList().size();
            ArrayList<ExpressionNode> callPara = node.getParameterExpressionList();
            ArrayList<MxType> declPara = classMemberSymbol.getType().getParameterTypeList();
            for(int i = 0; i < sz; ++i){
                visit(callPara.get(i));
                if(!callPara.get(i).getType().equals(declPara.get(i)))throw new CompileException("Parameter type of method call " + memId + " not match");
            }
        }
        node.setType(new MxType(classMemberSymbol.getType().getEnumType(), classMemberSymbol.getType().toString(), classMemberSymbol.getType().getDimension()));
        node.setRightValue();
        return null;
    }

    @Override
    public Void visit(MethodCallNode node) {
        MxType ret = null;
        String id = node.getMethodName();
        Symbol s = node.getCurrentSymbolTable().get(id);
        ArrayList<ExpressionNode> paraList = node.getParameterExpressionList();
        ExpressionNode para = null;
        if(s == null) throw new CompileException("Method " + id + " is not declaraed");
        if(!s.isMethod()) throw new CompileException(id + " can not be used as method");
        ret = s.getType();
        if(ret.getParameterTypeList().size() != paraList.size()) throw new CompileException("Method call" + id + " parameter number not match!");
        else{
            for(int i = 0; i < paraList.size(); ++i){
                para = paraList.get(i);
                visit(para);
                if(!para.getType().equals(ret.getParameterTypeList().get(i))) throw new CompileException("Parameter type of method call " + id + " not match");
            }
        }
        node.setType(new MxType(ret.getEnumType(), ret.toString(), ret.getDimension()));
        node.setRightValue();
        return null;
    }

    @Override
    public Void visit(IdentifierNode node) {
        String id = node.getIdentifier();
        Symbol s = node.getCurrentSymbolTable().get(id);
        if(!s.isVariable()) throw new CompileException(id + " can not be used as a variable");
        node.setType(node.getCurrentSymbolTable().get(id).getType());
        node.setLeftValue();
        return null;
    }



    @Override
    public Void visit(DotMemberNode node) {
        String memId = node.getMember();
        String classId = node.getMember();
        ExpressionNode expr = node.getExpression();
        MxType t = expr.getType();
        visit(expr);
        Symbol s = node.getCurrentSymbolTable().get(t.toString());
        if(s == null) throw new CompileException(t.toString() + " has no member " + memId);
        else node.setType(new MxType(s.getType().getEnumType(), s.getType().toString(), s.getType().getDimension()));
        node.setLeftValue();
        return null;
    }

    @Override
    public Void visit(BinaryExpressionNode node) {
        ExpressionNode lhs = node.getLeft(), rhs = node.getRight();
        visit(lhs);
        visit(rhs);
        Integer op = node.getOp();
        switch (op){
            case MxStarParser.MUL:
            case MxStarParser.DIV:
            case MxStarParser.MOD:
            case MxStarParser.SUB:
            case MxStarParser.SFTL:
            case MxStarParser.SFTR:
            case MxStarParser.BITAND:
            case MxStarParser.BITOR:
            case MxStarParser.BITXOR:
                if(!isIntegerType(lhs.getType()) || !isIntegerType(rhs.getType())) throw new CompileException("Type can not " + op.toString());
                node.setType(new MxType(MxType.TypeEnum.INT));
                break;
            case MxStarParser.ADD:
            case MxStarParser.GT:
            case MxStarParser.LT:
            case MxStarParser.GE:
            case MxStarParser.LE:
                if(!canBeAdded(lhs.getType(), rhs.getType())) throw new CompileException("Type can not " + op.toString());
                if(isStringType(lhs.getType())) node.setType(new MxType(MxType.TypeEnum.STRING));
                else if(isIntegerType(lhs.getType())) node.setType(new MxType(MxType.TypeEnum.INT));
                break;
            case MxStarParser.AND:
            case MxStarParser.OR:
                if(!isBooleanType(lhs.getType()) || !isBooleanType(rhs.getType())) throw new CompileException("Type can not " + op.toString());
                node.setType(new MxType(MxType.TypeEnum.BOOL));
                break;
            case MxStarParser.ASSIGN:
                if(!lhs.isLeftValue()) throw new CompileException("Can not assign to non-left value");
                else if(rhs.getType().getEnumType().equals(MxType.TypeEnum.NULL)) {
                    if(!canAssignedNull(lhs.getType())) throw new CompileException("Can not assign null to type " + lhs.getType().toString());
                }else if (!lhs.getType().equals(rhs.getType())) throw new CompileException("Type not equal when assign");
                else node.setType(new MxType(lhs.getType().getEnumType(), lhs.toString(), lhs.getType().getDimension()));
                break;
            case MxStarParser.EQ:
            case MxStarParser.NEQ:
                if(rhs.getType().getEnumType().equals(MxType.TypeEnum.NULL)){
                    if(isIntegerType(lhs.getType()) || isBooleanType(lhs.getType())) throw new CompileException("EQ/NEQ can not compare null with bool/int");
                }else{
                    if(!rhs.getType().equals(lhs.getType())) throw new CompileException("EQ/NEQ can not compare different type");
                }
                node.setType(new MxType(MxType.TypeEnum.BOOL));
                break;
        }
        node.setRightValue();
        return null;
    }

    @Override
    public Void visit(UnaryExpressionNode node) {
        int op = node.getOp();
        ExpressionNode expr = node.getRight();
        MxType t = expr.getType();
        visit(expr);
        switch (op){
            case MxStarParser.INC:
            case MxStarParser.DEC:
                if(!isIntegerType(t)) throw new CompileException("Self-inc/dec can not be applied to non-integer type");
                node.setType(new MxType(MxType.TypeEnum.INT));
                node.setLeftValue();
                break;
            case MxStarParser.ADD:
            case MxStarParser.SUB:
            case MxStarParser.BITNOT:
                if(!isIntegerType(t)) throw new CompileException("Neg/plus/bitnot can not be applied to non-integer type");
                node.setType(new MxType(MxType.TypeEnum.INT));
                node.setRightValue();
                break;
            case MxStarParser.NOT:
                if(!isBooleanType(t)) throw new CompileException("Not can not be applied to non-boolean type");
                node.setType(new MxType(MxType.TypeEnum.BOOL));
                node.setRightValue();
                break;
        }
        return null;
    }

    @Override
    public Void visit(ThisNode node) {
        if(!inClass) throw new CompileException("Usage of this outside the class");
        else node.setType(new MxType(MxType.TypeEnum.CLASS, currentClassName));
        return null;
    }

    @Override
    public Void visit(NewCreatorNode node) {
        MxType t = node.getCreatorType();
        if(node.isArrayCreator()){
            for(ExpressionNode n : node.getSizeExpressionList()){
                visit(n);
                if(!isIntegerType(n.getType())) throw new CompileException("Array creator size should be integer");
            }
            if(!t.isPrimitiveType() && !node.getCurrentSymbolTable().contains(t.toString())) throw new CompileException("type " + t.toString() + " is not declared");
            if(!t.isPrimitiveType()) t.setType(MxType.TypeEnum.CLASS);
            node.setType(new MxType(t.getEnumType(),t.toString(), node.getDimension()));
        }else{
            if(t.isPrimitiveType()){
                if(node.getParameterList().size() != 0) throw new CompileException("Primitive Type have no costructor parameter");
                node.setType(new MxType(t.getEnumType()));
            }
            else{
                Symbol s = node.getCurrentSymbolTable().get(t.toString());
                if(s == null) throw new CompileException("type " + t.toString() + " is not declared");
                else{
                    SymbolTable memberTable = s.getMemberTable();
                    Symbol constructor = memberTable.get(ASTBuilderVisitor.constructorPrefix + t.toString());
                    if(constructor == null) throw new CompileException("Class " + t.toString() + " has no constructor");
                    else{
                        t.setType(MxType.TypeEnum.CLASS);
                        ArrayList<MxType> paraTypeLst = constructor.getType().getParameterTypeList();
                        ArrayList<ExpressionNode> paraExpLst = node.getParameterList();
                        if(paraExpLst.size() != paraTypeLst.size()) throw new CompileException("parameter number not match when call constrctor " + t.toString());
                        for(int i = 0; i < paraExpLst.size(); ++i){
                            visit(paraExpLst.get(i));
                            if(!paraExpLst.get(i).getType().equals(paraTypeLst.get(i))) throw new CompileException("parameter type not match when call constructor " + t.toString());
                        }
                    }
                    node.setType(new MxType(t.getEnumType(), t.toString()));
                }
            }
        }
        node.setRightValue();
        return null;
    }

    @Override
    public Void visit(BooleanConstantNode node) {
        return null;
    }

    @Override
    public Void visit(IntegerConstantNode node) {
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

}
