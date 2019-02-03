package kstarxin.ast;
import  kstarxin.ast.*;

abstract public interface ASTBaseVisitor<T> {
    public T visit(Node node);
    public T visit(ProgramNode node);
    public T visit(DeclarationNode node);
    public T visit(BlockNode node);
    public T visit(StatementNode node);
    public T visit(VariableDeclarationNode node);
    public T visit(VariableDeclaratorNode node);
    public T visit(ClassDeclarationNode node);
    public T visit(MethodDeclarationNode node);
    public T visit(ParameterDeclarationNode node);
    public T visit(ConditionNode node);
    public T visit(LoopNode node);
    public T visit(JumpNode node);
    public T visit(ExpressionNode node);
    public T visit(IntegerConstantNode node);
    public T visit(BooleanConstantNode node);
    public T visit(StringConstantNode node);
    public T visit(NullConstantNode node);
}
