package kstarxin.ast;
import  kstarxin.ast.*;

abstract public interface ASTBaseVisitor<T> {
    public T visit(ProgramNode node);
    public T visit(DeclarationNode node);
    public T visit(VariableDeclarationNode node);
    public T visit(VariableDeclaratorNode node);
    public T visit(ClassDeclarationNode node);
    public T visit(MethodDeclarationNode node);
    public T visit(ParameterDeclarationNode node);
    public T visit(ConditionNode node);
    public T visit(LoopNode node);
    public T visit(ExpressionNode node);
}
