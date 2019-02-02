package kstarxin.ast;

import java.io.PrintStream;
import java.util.LinkedList;

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

    public void display(){
        visit(ast);
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
        return null;
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
        for(Node n : node.getStatementList()){
            if(n instanceof VariableDeclarationNode) visit((VariableDeclarationNode) n);
            else if(n instanceof BlockNode) visit((BlockNode)n);
            else if(n instanceof LoopNode) visit((LoopNode) n);
            else if(n instanceof ConditionNode) visit((ConditionNode) n);
            else if(n instanceof ExpressionNode) visit((ExpressionNode)n);
        }
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
    public Void visit(ConditionNode node) {
        return null;
    }

    @Override
    public Void visit(ExpressionNode node) {
        return null;
    }

    @Override
    public Void visit(JumpNode node) {
        return null;
    }
}
