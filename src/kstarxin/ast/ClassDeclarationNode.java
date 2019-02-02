package kstarxin.ast;

import kstarxin.utilities.Location;
import kstarxin.utilities.SymbolTable;

import java.util.LinkedList;

public class ClassDeclarationNode extends DeclarationNode{
    String name = null;
    LinkedList<MethodDeclarationNode> classConstructorDeclaration;
    LinkedList<VariableDeclarationNode> classMemberVariableDeclaration;
    LinkedList<MethodDeclarationNode> classMemberMethodDeclaration;
    LinkedList<ClassDeclarationNode> classSubclassDeclaration = null;

    public ClassDeclarationNode(String _name, SymbolTable stb, Location loc){
        super(stb, loc);
        name = _name;
        classConstructorDeclaration = new LinkedList<>();
        classMemberMethodDeclaration = new LinkedList<>();
        classMemberVariableDeclaration = new LinkedList<>();
    }

    public void addVariable(VariableDeclarationNode n){
        classMemberVariableDeclaration.add(n);
    }

    public void addMethod(MethodDeclarationNode n){
        classMemberMethodDeclaration.add(n);
    }


    public void addConstructor(MethodDeclarationNode n){
        classConstructorDeclaration.add(n);
    }

    public LinkedList<MethodDeclarationNode> getConstructorList(){
        return classConstructorDeclaration;
    }

    public LinkedList<VariableDeclarationNode> getMemberVariableList(){
        return classMemberVariableDeclaration;
    }

    public LinkedList<MethodDeclarationNode> getMemberMethodList(){
        return classMemberMethodDeclaration;
    }

    public String getName(){
        return name;
    }

    @Override
    public <T> T accept(ASTBaseVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
