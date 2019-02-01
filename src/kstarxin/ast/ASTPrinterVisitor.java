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
        printLine("[Method"+node.getIdentifier() + "with return type:" +node.getReturnType().toString() + ":" + node.getReturnType().getEnumString() + "]");
        addIdent();
        LinkedList<Node> lst = node.getStatementList();
        for(Node n : lst){
            visit((VariableDeclarationNode) n);
        }
        subIndent();
        return null;
    }

    @Override
    public Void visit(ClassDeclarationNode node){
        printLine("[Class:]");
        addIdent();
        //TODO
        subIndent();
        return null;
    }

    @Override
    public Void visit(VariableDeclaratorNode node) {
        printLine(node.getIdentifier());
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
}
