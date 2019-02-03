package kstarxin.ast;

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
        printLine("[VarDecl: " + t + ":" + enumt + " dim = " + dim.toString() + " type]");
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
        printLine(node.getIdentifier());
        return null;
    }

    @Override
    public Void visit(BlockNode node) {
        addIdent();
        printLine("[Block]");
        node.getStatementList().forEach(this::visit);
        subIndent();
        return null;
    }

    @Override
    public Void visit(ParameterDeclarationNode node){
        return null;
    }

    @Override
    public Void visit(LoopNode node) {
        return null;
    }

    @Override
    public Void visit(JumpNode node) {
        return null;
    }


    @Override
    public Void visit(ConditionNode node) {
        printLine("[Condition:]");
        print("[Cond]");
        ExpressionNode cond = node.getCond();
        if(cond != null) visit(node.getCond());
        else printNoIdt("NULL");
        printNoIdt("\n");
        printLine("[Body]");
        visit(node.getBody());
        printLine("[Else]");
        ConditionNode elseNode = node.getElse();
        if(elseNode != null)visit(node.getElse());
        else printLine("NULL");
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
        return null;
    }

    @Override
    public Void visit(StringConstantNode node) {
        printNoIdt("[Value: " + node.getValue() + "]");
        return null;
    }
}
