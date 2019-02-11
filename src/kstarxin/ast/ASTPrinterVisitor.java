package kstarxin.ast;

import kstarxin.parser.MxStarParser;
import kstarxin.utilities.MxType;

import java.io.*;
import java.util.*;

public class ASTPrinterVisitor implements ASTBaseVisitor<Void> {
    ProgramNode ast;
    PrintStream out;
    String      idt;

    public ASTPrinterVisitor(ProgramNode _ast, PrintStream _out){
        ast = _ast;
        out = _out;
        idt = "";
    }

    public void addIdent(){
        idt += "\t";
    }

    public void subIndent(){
        idt = idt.substring(1);
    }

    public void printLine(String s){
        out.println(idt + s);
    }

    public void printNoIdt(String s){
        out.print(s);
    }
    public void print(String s){
        out.print(idt + s);
    }

    public void display(){
        visit(ast);
    }

    @Override
    public Void visit(Node node) {
        return node.accept(this);
    }

    @Override
    public Void visit(ProgramNode node) {
        out.println("[Start Print Program]");
        node.getVariableDeclarations().forEach(this::visit);
        node.getMethodDeclarations().forEach(this::visit);
        node.getClassDeclarations().forEach(this::visit);
        return null;
    }

    @Override
    public Void visit(DeclarationNode node){
        return node.accept(this);
    }

    @Override
    public Void visit(StatementNode node) {
        return node.accept(this);
    }

    @Override
    public Void visit(VariableDeclarationNode node){
        String t = node.getTypeNode().getType().toString();
        String enumt = node.getTypeNode().getType().getEnumString();
        Integer dim = node.getTypeNode().getType().getDimension();
        print("[VarDecl: " + t + ":" + enumt + " dim = " + dim.toString() + " type]\n\t");
        node.getDeclaratorList().forEach(this::visit);
        return null;
    }

    @Override
    public Void visit(MethodDeclarationNode node){
        printLine("[Method "+node.getIdentifier() + " with returnType:" +node.getReturnType().toString() + ":" + node.getReturnType().getEnumString() + "]");
        ArrayList<MxType> typeList = node.getReturnType().getParameterTypeList();
        String paraTypeString = "";
        if(typeList.size() > 0) {
            paraTypeString = typeList.get(0).toString() + " : "  + typeList.get(0).getEnumString();
            int i = 0;
            for (i = 1; i < typeList.size(); ++i) {
                paraTypeString += ", " + typeList.get(i).toString() + " : " + typeList.get(i).getEnumString();
            }
        }
        printLine("[Method Para:"+paraTypeString+"]");
        visit(node.getBlock());
        return null;
    }

    @Override
    public Void visit(ClassDeclarationNode node){
        printLine("[Class:" + node.getName() + "]");
        addIdent();
        printLine("[Constructor(s)]");
        node.getConstructorList().forEach(this::visit);
        printLine("[MemberVariables]");
        node.getMemberVariableList().forEach(this::visit);
        printLine("[MemberMethods]");
        node.getMemberMethodList().forEach(this::visit);
        subIndent();
        return null;
    }

    @Override
    public Void visit(VariableDeclaratorNode node) {
        print(node.getIdentifier());
        if(node.getInitializer() != null) visit(node.getInitializer());
        else printNoIdt("[NOT INIT]");
        printNoIdt("\n");
        return null;
    }

    @Override
    public Void visit(BlockNode node) {
        addIdent();
        printLine("\n" + idt + "[Block]<");
        node.getStatementList().forEach(this::visit);
        printLine("\n" + idt + "[Block]>");
        subIndent();
        return null;
    }

    @Override
    public Void visit(ParameterDeclarationNode node){
        return null;
    }

    @Override
    public Void visit(LoopNode node) {
        addIdent();
        printLine("[Loop]<:");
        print("[Init]");
        ExpressionNode tmp = node.getInitializer();
        if(tmp != null) visit(tmp);
        else printNoIdt("NULL");
        tmp = node.getCondition();
        printNoIdt("[Cond]");
        if(tmp != null) visit(tmp);
        else printNoIdt("NULL");
        printNoIdt("[Step]");
        tmp = node.getStep();
        if(tmp != null) visit(tmp);
        else printNoIdt("NULL") ;
        printNoIdt("\n");
        Node tmp1 = node.getBody();
        if(tmp1 != null)visit(tmp1);
        printLine("[Loop]>:");
        subIndent();
        return null;
    }

    @Override
    public Void visit(JumpNode node) {
        return node.accept(this);
    }

    @Override
    public Void visit(ConditionNode node) {
        printLine("[Condition]<:");
        print("[Cond]");
        ExpressionNode cond = node.getCond();
        if(cond != null) visit(node.getCond());
        else printNoIdt("NULL");
        printNoIdt("\n");
        printLine("[Body]\n" + idt);
        if(node.getBody() != null)visit(node.getBody());
        printLine("[Else]");
        ConditionNode elseNode = node.getElse();
        if(elseNode != null)visit(node.getElse());
        else printLine("NULL");
        printLine("[Condition]>:");
        return null;
    }

    @Override
    public Void visit(ExpressionNode node) {
        return node.accept(this);
    }

    @Override
    public Void visit(IntegerConstantNode node) {
        Integer tmp = node.getValue();
        printNoIdt("[Value: " + tmp.toString() + "]");
        return null;
    }

    @Override
    public Void visit(BooleanConstantNode node) {
        Boolean tmp = node.getValue();
        printNoIdt("[Value: " + tmp.toString() + "]");
        return null;
    }

    @Override
    public Void visit(NullConstantNode node) {
        printNoIdt("[ValueNull]");
        return null;
    }

    @Override
    public Void visit(StringConstantNode node) {
        printNoIdt("[Value: " + node.getValue() + "]");
        return null;
    }

    @Override
    public Void visit(BreakNode node) {
        printLine("[Break]");
        return null;
    }

    @Override
    public Void visit(ContinueNode node) {
        printLine("[Continue]");
        return null;
    }

    @Override
    public Void visit(ReturnNode node) {
        print("[Return]" + "\n\t");
        if(node.getReturnValue() != null) visit(node.getReturnValue());
        return null;
    }

    @Override
    public Void visit(BinaryExpressionNode node){
        int op = node.getOp();
        String sop = null;
        switch (op) {
            case MxStarParser.MUL:
                sop = "MUL";
                break;
            case MxStarParser.DIV:
                sop = "DIV";
                break;
            case MxStarParser.MOD:
                sop = "MOD";
                break;
            case MxStarParser.ADD:
                sop = "ADD";
                break;
            case MxStarParser.SUB:
                sop = "SUB";
                break;
            case MxStarParser.SFTL:
                sop = "SHIFT LEFT";
                break;
            case MxStarParser.SFTR:
                sop = "SHIFT RIGTH";
                break;
            case MxStarParser.GT:
                sop = "GREATOR THAN";
                break;
            case MxStarParser.LT:
                sop = "LESS THAN";
                break;
            case MxStarParser.GE:
                sop = "GREATOR EQUAL";
                break;
            case MxStarParser.EQ:
                sop = "EQUAL";
                break;
            case MxStarParser.NEQ:
                sop = "NOT EQUAL";
                break;
            case MxStarParser.BITAND:
                sop = "BITAND";
                break;
            case MxStarParser.BITXOR:
                sop = "BITXOR";
                break;
            case MxStarParser.BITOR:
                sop = "BITOR";
                break;
            case MxStarParser.AND:
                sop = "AND";
                break;
            case MxStarParser.OR:
                sop = "OR";
                break;
            case MxStarParser.ASSIGN:
                sop = "ASSIGN";
                break;
        }
        if(sop == "ASSIGN") printNoIdt(idt);
        visit(node.getLeft());
        printNoIdt("<"+sop+">");
        visit(node.getRight());
        return null;
    }

    @Override
    public Void visit(UnaryExpressionNode node) {
        int op = node.getOp();
        String sop = null;
        switch (op){
            case MxStarParser.INC:
                sop = "SELF-INCREASE";
                break;
            case MxStarParser.DEC:
                sop = "SELF-DECREASE";
                break;
            case MxStarParser.ADD:
                sop = "POSITIVE";
                break;
            case MxStarParser.SUB:
                sop = "NEGATIVE";
                break;
            case MxStarParser.NOT:
                sop = "NOT";
                break;
            case MxStarParser.BITNOT:
                sop = "BITNOT";
                break;
        }
        printNoIdt("<"+sop+">");
        visit(node.getRight());
        return null;
    }

    @Override
    public Void visit(DotMemberNode node){
        visit(node.getExpression());
        printNoIdt("[DOT]");
        printNoIdt(node.getMember());
        return null;
    }

    @Override
    public Void visit(IndexAccessNode node){
        printNoIdt("[ArrayName:]");
        visit(node.getExpression());
        printNoIdt("[Index:]");
        visit(node.getIndex());
        return null;
    }

    @Override
    public Void visit(IdentifierNode node){
        printNoIdt("[id:"+node.getIdentifier()+"]");
        return null;
    }

    @Override
    public Void visit(NewCreatorNode node){
        printNoIdt("[NEW " + node.type.toString()  + "]" );
        if(node.isArrayCreator()){
            ArrayList<ExpressionNode> lst = node.getSizeExpressionList();
            for(ExpressionNode n : lst){
                printNoIdt("[");
                visit(n);
                printNoIdt("]");
            }
        }
        else{
            if(node.getParameterList().size() != 0){
                printNoIdt("(");
                for(ExpressionNode n : node.getParameterList()){
                    visit(n);
                    printNoIdt(",");
                }
                printNoIdt(")");
            }
        }
        return null;
    }

    @Override
    public Void visit(SuffixIncreaseDecreaseNode node){
        int op = node.getOp();
        String sop = null;
        switch (op){
            case MxStarParser.INC:
                sop = "SELF-INCREASE";
                break;
            case MxStarParser.DEC:
                sop = "SELF-DECREASE";
                break;
        }
        visit(node.getExpression());
        printNoIdt("["+sop + "]");
        return null;
    }

    @Override
    public Void visit(ThisNode node){
        printNoIdt("[This]");
        return null;
    }

    @Override
    public Void visit(MethodCallNode node) {
        printNoIdt("[Call]");
        printNoIdt(node.getMethodName());
        printNoIdt("(");
        node.getParameterExpressionList().forEach(this::visit);
        printNoIdt(")\n");
        return null;
    }

    @Override
    public Void visit(DotMemberMethodCallNode node) {
        printNoIdt("[Dot][Call]");
        printNoIdt(node.getMemberMethodName());
        printNoIdt("(");
        node.getParameterExpressionList().forEach(this::visit);
        printNoIdt(")");
        return null;
    }
}
