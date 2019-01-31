package kstarxin.ast;

import kstarxin.utilities.Location;
import kstarxin.utilities.SymbolTable;

import java.util.LinkedList;

public class ProgramNode extends Node {
    private LinkedList<VariableDeclarationNode> variableDeclarations;
    private LinkedList<ClassDeclarationNode>    classDeclarations;
    private LinkedList<MethodDeclarationNode>   methodDeclarations;

    ProgramNode(SymbolTable stb, Location loc){
        super(stb, loc);
        variableDeclarations = new LinkedList<>();
        classDeclarations = new LinkedList<>();
        methodDeclarations = new LinkedList<>();
    }

    public LinkedList<VariableDeclarationNode> getVariableDeclarations() {
        return variableDeclarations;
    }

    public LinkedList<ClassDeclarationNode> getClassDeclarations() {
        return classDeclarations;
    }

    public LinkedList<MethodDeclarationNode> getMethodDeclarations() {
        return methodDeclarations;
    }

    public void addVariableDeclaration(VariableDeclarationNode node){
        variableDeclarations.add(node);
    }

    public void addClassDeclarationNode(ClassDeclarationNode node){
        classDeclarations.add(node);
    }

    public void addMethodDeclaration(MethodDeclarationNode node){
        methodDeclarations.add(node);
    }

    @Override
    public <T> T accept(ASTBaseVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
