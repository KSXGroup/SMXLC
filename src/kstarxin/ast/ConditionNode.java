package kstarxin.ast;

public abstract class ConditionNode extends Node{
    public  ConditionNode(){
        super(null, null);
    }
    @Override
    public <T> T accept(ASTBaseVisitor<T> visitor) {
        return null;
    }
}
