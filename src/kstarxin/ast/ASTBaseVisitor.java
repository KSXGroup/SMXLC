package kstarxin.ast;
import  kstarxin.ast.*;

abstract public interface ASTBaseVisitor<T> {
    T visit(ProgramNode node);
    T visit(DeclarationNode node);
    T visit(VariableDeclarationNode node);
    T visit(VariableDeclaratorNode node);
    T visit(ClassDeclarationNode node);
}
