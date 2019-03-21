package kstarxin.ast;

import kstarxin.parser.MxStarParser;
import kstarxin.utilities.Location;
import kstarxin.utilities.MxException.*;
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
    private MxErrorProcessor errorProcessor;


    public ASTTypeCheckerVisitor(ProgramNode _ast, MxErrorProcessor _errorProcessor) {
        inLoop = 0;
        inClass = false;
        inMethod = false;
        methodReturned = false;
        inClassConstructor = false;
        currentClassName = "";
        currentMethodName = "";
        currentMethodReturnType = null;
        errorProcessor = _errorProcessor;
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
        else return false;
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

    private boolean checkMain(){
        Symbol mainSymbol = ast.getCurrentSymbolTable().getMember("main");
        if(mainSymbol == null || !mainSymbol.isMethod()){
            errorProcessor.add(new MxSemanticCheckError("main method not found!", new Location(1,0)));
            return false;
        }
        if(!isIntegerType(mainSymbol.getType())){
            errorProcessor.add(new MxSemanticCheckError("main method should return int", mainSymbol.getLocation()));
            return false;
        }
        return true;
    }

    public void checkType(){
        if(!checkMain()) return;
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
                if(!varT.equals(expr.getType())){
                    errorProcessor.add(new MxSemanticCheckError("type not match when initialize variable, expected " + varT.toString() + " ,but get " + expr.getType().toString() , node.getLocation()));
                    return null;
                }
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
                errorProcessor.add(new MxSemanticCheckError("member method " + currentMethodName + " of class " + currentClassName + " should have return value", node.getLocation()));
                return null;
            }
            if(!node.getReturnType().toString().equals(currentClassName) || node.getReturnType().getDimension() != 0){
                errorProcessor.add(new MxSemanticCheckError("class constructor should not have return value", node.getLocation()));
                return null;
            }
        } else if(currentMethodName.equals(currentClassName)) {
            errorProcessor.add(new MxSemanticCheckError("constructor should not have return value", node.getLocation()));
            return null;
        }
        visit(node.getBlock());
        //TODO: make not return a warning
        /*if (!methodReturned && !node.getReturnType().getEnumType().equals(MxType.TypeEnum.VOID) && !inClassConstructor)
            throw new MxSemanticCheckError("Method not returned", node.getLocation());*/
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
        if(cond != null) {
            visit(cond);
            if (!isBooleanType(cond.getType())) {
                errorProcessor.add(new MxSemanticCheckError("cond should be bool!", node.getLocation()));
                return null;
            }
        }
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
        else if(!node.isForLoop()){
            errorProcessor.add(new MxSemanticCheckError("while loop should have cond!", node.getLocation()));
            return null;
        }
        if(cond != null && !isBooleanType(cond.getType())) {
            errorProcessor.add(new MxSemanticCheckError("cond should be boolean!", node.getLocation()));
            return null;
        }
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
        if(inLoop == 0){
            errorProcessor.add(new MxSemanticCheckError("continue not in loop", node.getLocation()));
            return null;
        }
        return null;
    }

    @Override
    public Void visit(BreakNode node) {
        if(inLoop == 0) {
            errorProcessor.add(new MxSemanticCheckError("break no in loop", node.getLocation()));
            return null;
        }
        return null;
    }

    @Override
    public Void visit(ReturnNode node) {
        if(!(inMethod || inClassConstructor)){
            errorProcessor.add(new MxSemanticCheckError("invalid return!", node.getLocation()));
            return null;
        }
        else if(inMethod && !inClassConstructor){
            if(!currentMethodReturnType.getEnumType().equals(MxType.TypeEnum.VOID)) {
                visit(node.getReturnValue());
                if (!currentMethodReturnType.equals(node.getReturnValue().getType())) {
                    errorProcessor.add(new MxSemanticCheckError("return type not match", node.getLocation()));
                    return null;
                }
            }else if(node.getReturnValue() != null){
                errorProcessor.add(new MxSemanticCheckError("nothing should be returned in void method", node.getLocation()));
                return null;
            }
            methodReturned = true;
        }else if(inClassConstructor){
            if(node.getReturnValue() != null){
                errorProcessor.add(new MxSemanticCheckError("nothing should be returned in class constructor", node.getLocation()));
                return null;
            }
        }
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
        if(!expr.isLeftValue()) {
            errorProcessor.add(new MxSemanticCheckError("left value necessary when suffix inc/dec", node.getLocation()));
            return null;
        }
        if(!isIntegerType(expr.getType())){
            errorProcessor.add(new MxSemanticCheckError("invalid use of suffix inc/dec", node.getLocation()));
            return null;
        }
        node.setType(new MxType(MxType.TypeEnum.INT));
        node.setRightValue();
        return null;
    }

    @Override
    public Void visit(IndexAccessNode node) {
        MxType exprt = null, idxt = null;
        visit(node.getIndex());
        idxt =  node.getIndex().getType();
        if(!isIntegerType(idxt)) {
            errorProcessor.add(new MxSemanticCheckError("invalid index!", node.getLocation()));
            return null;
        }
        visit(node.getExpression());
        exprt = node.getExpression().getType();
        if(exprt.getDimension() == 0){
            errorProcessor.add(new MxSemanticCheckError("no index access to non-array type", node.getLocation()));
            return null;
        }
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
            classSymbol = node.getCurrentSymbolTable().get(ASTBuilderVisitor.builtinStringClassName, node.getLocation());
        }
        else if(t.getDimension() > 0 && memId.equals("size")){
            classSymbol = node.getCurrentSymbolTable().get(ASTBuilderVisitor.builtinArrayClassName, node.getLocation());
        }
        else {
            classSymbol = node.getCurrentSymbolTable().getClassType(t.toString(), node.getLocation());
        }
        memberTable = classSymbol.getMemberTable();
        Symbol classMemberSymbol = memberTable.getMember(memId);
        if(classMemberSymbol == null){
            errorProcessor.add(new MxSemanticCheckError("type " + t.toString() + " has no member " + memId, node.getLocation()));
            return null;
        }
        else if(!classMemberSymbol.isMethod()){
            errorProcessor.add(new MxSemanticCheckError("member " + memId + " can not be used as method", node.getLocation()));
            return null;
        }
        else if(classMemberSymbol.getType().getParameterTypeList().size() != node.getParameterExpressionList().size()){
            errorProcessor.add(new MxSemanticCheckError("parameter number not match when call member method " + memId ,node.getLocation()));
            return null;
        }
        else if(node.getParameterExpressionList().size() > 0){
            int sz = node.getParameterExpressionList().size();
            ArrayList<ExpressionNode> callPara = node.getParameterExpressionList();
            ArrayList<MxType> declPara = classMemberSymbol.getType().getParameterTypeList();
            for(int i = 0; i < sz; ++i){
                visit(callPara.get(i));
                if(!callPara.get(i).getType().equals(declPara.get(i))){
                    errorProcessor.add(new MxSemanticCheckError("parameter type of method call " + memId + " not match", node.getLocation()));
                    return null;
                }
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
        Symbol s = node.getCurrentSymbolTable().get(id, node.getLocation());
        ArrayList<ExpressionNode> paraList = node.getParameterExpressionList();
        ExpressionNode para = null;
        if(s == null) {
            errorProcessor.add(new MxSemanticCheckError("method " + id + " is not declaraed" ,node.getLocation()));
            return null;
        }
        if(!s.isMethod()){
            errorProcessor.add(new MxSemanticCheckError(id + " can not be used as method", node.getLocation()));
            return null;
        }
        ret = s.getType();
        if(ret.getParameterTypeList().size() != paraList.size()){
            errorProcessor.add(new MxSemanticCheckError("method call" + id + " parameter number not match!", node.getLocation()));
            return null;
        }
        else{
            for(int i = 0; i < paraList.size(); ++i){
                para = paraList.get(i);
                visit(para);
                if(!ret.getParameterTypeList().get(i).equals(para.getType())) {
                    errorProcessor.add(new MxSemanticCheckError("parameter type of method call " + id + " not match", node.getLocation()));
                }
            }
        }
        node.setType(new MxType(ret.getEnumType(), ret.toString(), ret.getDimension()));
        node.setRightValue();
        return null;
    }

    @Override
    public Void visit(IdentifierNode node) {
       /* String id = node.getIdentifier();
        Symbol s = node.getCurrentSymbolTable().get(id);
        if(!s.isVariable()) throw new MxSemanticCheckError(id + " can not be used as a variable");
        node.setType(node.getCurrentSymbolTable().get(id).getType());*/
        node.setLeftValue();
        return null;
    }



    @Override
    public Void visit(DotMemberNode node) {
        String memId = node.getMember();
        String classId = node.getMember();
        ExpressionNode expr = node.getExpression();
        SymbolTable classMemberTable = null;
        MxType t = null;
        visit(expr);
        t = expr.getType();
        Symbol classSymbol = node.getCurrentSymbolTable().getClassType(t.toString(), node.getLocation());
        classMemberTable = classSymbol.getMemberTable();
        Symbol memberSymbol = classMemberTable.getMember(memId);
        if(memberSymbol == null) {
            errorProcessor.add(new MxSemanticCheckError(t.toString() + " has no member " + memId, node.getLocation()));
            return null;
        }
        else node.setType(new MxType(memberSymbol.getType().getEnumType(), memberSymbol.getType().toString(), memberSymbol.getType().getDimension()));
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
                if(!isIntegerType(lhs.getType()) || !isIntegerType(rhs.getType())) {
                    errorProcessor.add(new MxSemanticCheckError("type can not " + op.toString(), node.getLocation()));
                    return null;
                }
                node.setType(new MxType(MxType.TypeEnum.INT));
                break;
            case MxStarParser.ADD:
            case MxStarParser.GT:
            case MxStarParser.LT:
            case MxStarParser.GE:
            case MxStarParser.LE:
                if(!canBeAdded(lhs.getType(), rhs.getType())){
                    errorProcessor.add(new MxSemanticCheckError("type can not " + op.toString(), node.getLocation()));
                    return null;
                }
                if(op == MxStarParser.ADD) {
                    if (isStringType(lhs.getType())) node.setType(new MxType(MxType.TypeEnum.STRING));
                    else if (isIntegerType(lhs.getType())) node.setType(new MxType(MxType.TypeEnum.INT));
                }else{
                    node.setType(new MxType(MxType.TypeEnum.BOOL));
                }
                break;
            case MxStarParser.AND:
            case MxStarParser.OR:
                if(!isBooleanType(lhs.getType()) || !isBooleanType(rhs.getType())) {
                    errorProcessor.add(new MxSemanticCheckError("type can not " + op.toString(), node.getLocation()));
                    return null;
                }
                node.setType(new MxType(MxType.TypeEnum.BOOL));
                break;
            case MxStarParser.ASSIGN:
                if(!lhs.isLeftValue()){
                    errorProcessor.add(new MxSemanticCheckError("can not assign to non-left value", node.getLocation()));
                    return null;
                }
                else if(rhs.getType().getEnumType().equals(MxType.TypeEnum.NULL)) {
                    if(!canAssignedNull(lhs.getType())){
                        errorProcessor.add(new MxSemanticCheckError("can not assign null to type " + lhs.getType().toString(), node.getLocation()));
                        return null;
                    }
                }else if (!lhs.getType().equals(rhs.getType())){
                    errorProcessor.add(new MxSemanticCheckError("type not equal when assign", node.getLocation()));
                    return null;
                }
                else{
                    if(lhs instanceof IndexAccessNode) ((IndexAccessNode) lhs).setAssigned();
                    node.setType(new MxType(lhs.getType().getEnumType(), lhs.toString(), lhs.getType().getDimension()));
                }
                break;
            case MxStarParser.EQ:
            case MxStarParser.NEQ:
                if(rhs.getType().getEnumType().equals(MxType.TypeEnum.NULL)){
                    if(isIntegerType(lhs.getType()) || isBooleanType(lhs.getType())){
                        errorProcessor.add(new MxSemanticCheckError("EQ/NEQ can not compare null with bool/int" , node.getLocation()));
                        return null;
                    }
                }else{
                    if(!rhs.getType().equals(lhs.getType())){
                        errorProcessor.add(new MxSemanticCheckError("EQ/NEQ can not compare different type", node.getLocation()));
                        return null;
                    }
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
        MxType t = null;
        visit(expr);
        t = expr.getType();
        switch (op){
            case MxStarParser.INC:
            case MxStarParser.DEC:
                if(!isIntegerType(t)) {
                    errorProcessor.add(new MxSemanticCheckError("self-inc/dec can not be applied to non-integer type", node.getLocation()));
                    return null;
                }
                node.setType(new MxType(MxType.TypeEnum.INT));
                node.setLeftValue();
                break;
            case MxStarParser.ADD:
            case MxStarParser.SUB:
            case MxStarParser.BITNOT:
                if(!isIntegerType(t)){
                    errorProcessor.add(new MxSemanticCheckError("neg/plus/bitnot can not be applied to non-integer type", node.getLocation()));
                    return null;
                }
                node.setType(new MxType(MxType.TypeEnum.INT));
                node.setRightValue();
                break;
            case MxStarParser.NOT:
                if(!isBooleanType(t)){
                    new MxSemanticCheckError("not can not be applied to non-boolean type", node.getLocation());
                    return null;
                }
                node.setType(new MxType(MxType.TypeEnum.BOOL));
                node.setRightValue();
                break;
        }
        return null;
    }

    @Override
    public Void visit(ThisNode node) {
        if(!inClass) {
            errorProcessor.add(new MxSemanticCheckError("usage of \"this\" outside the class",node.getLocation()));
            return null;
        }
        else node.setType(new MxType(MxType.TypeEnum.CLASS, currentClassName));
        return null;
    }

    @Override
    public Void visit(NewCreatorNode node) {
        MxType t = node.getCreatorType();
        if(!t.isPrimitiveType()){
            Symbol ts = node.getCurrentSymbolTable().get(t.toString(), node.getLocation());
            if(ts != null && ts.isClass()) t.setType(MxType.TypeEnum.CLASS);
            else {
                errorProcessor.add(new MxSemanticCheckError("can not create type " + t.toString(), node.getLocation()));
                return null;
            }
        }
        if(node.isArrayCreator()){
            for(ExpressionNode n : node.getSizeExpressionList()){
                visit(n);
                if(!isIntegerType(n.getType())){
                    errorProcessor.add(new MxSemanticCheckError("array creator size should be integer", node.getLocation()));
                    return null;
                }
            }
            if(!t.isPrimitiveType() && node.getCurrentSymbolTable().get(t.toString(), node.getLocation()) == null) {
                errorProcessor.add(new MxSemanticCheckError("type " + t.toString() + " is not declared", node.getLocation()));
                return null;
            }
            if(!t.isPrimitiveType()) t.setType(MxType.TypeEnum.CLASS);
            node.setType(new MxType(t.getEnumType(),t.toString(), node.getDimension()));
        }else{
            if(t.isPrimitiveType()){
                if(node.getParameterList().size() != 0) {
                    errorProcessor.add(new MxSemanticCheckError("primitive Type have no costructor parameter", node.getLocation()));
                    return null;
                }
                node.setType(new MxType(t.getEnumType()));
            }
            else{
                Symbol s = node.getCurrentSymbolTable().get(t.toString(), node.getLocation());
                if(s == null){
                    errorProcessor.add(new MxSemanticCheckError("type " + t.toString() + " is not declared", node.getLocation()));
                    return null;
                }
                else{
                    SymbolTable memberTable = s.getMemberTable();
                    Symbol constructor = memberTable.getMember(ASTBuilderVisitor.constructorPrefix + t.toString());
                    if(constructor == null && node.getParameterList().size() > 0) {
                        errorProcessor.add(new MxSemanticCheckError("class " + t.toString() + " has no constructor", node.getLocation()));
                        return null;
                    }
                    else if(constructor != null){
                        t.setType(MxType.TypeEnum.CLASS);
                        ArrayList<MxType> paraTypeLst = constructor.getType().getParameterTypeList();
                        ArrayList<ExpressionNode> paraExpLst = node.getParameterList();
                        if(paraExpLst.size() != paraTypeLst.size()){
                            errorProcessor.add(new MxSemanticCheckError("parameter number not match when call constrctor " + t.toString(), node.getLocation()));
                            return null;
                        }
                        for(int i = 0; i < paraExpLst.size(); ++i){
                            visit(paraExpLst.get(i));
                            if(!paraExpLst.get(i).getType().equals(paraTypeLst.get(i))){
                                errorProcessor.add(new MxSemanticCheckError("parameter type not match when call constructor " + t.toString(), node.getLocation()));
                                return null;
                            }
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
        node.setType(new MxType(MxType.TypeEnum.BOOL));
        return null;
    }

    @Override
    public Void visit(IntegerConstantNode node) {
        node.setType(new MxType(MxType.TypeEnum.INT));
        return null;
    }

    @Override
    public Void visit(StringConstantNode node) {
        node.setType(new MxType(MxType.TypeEnum.STRING));
        return null;
    }

    @Override
    public Void visit(NullConstantNode node) {
        node.setType(new MxType(MxType.TypeEnum.NULL));
        return null;
    }

}
