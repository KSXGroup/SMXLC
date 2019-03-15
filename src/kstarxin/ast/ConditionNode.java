package kstarxin.ast;

import kstarxin.utilities.Location;
import kstarxin.utilities.SymbolTable;

public class ConditionNode extends StatementNode{
    private ExpressionNode cond;
    private Node ifthen;
    private ConditionNode ifelse;
    private String condtionName;

    public ConditionNode(SymbolTable stb, Location loc){
        super(stb,loc);
    }

    public ConditionNode(ExpressionNode _cond, Node _then, ConditionNode _else, SymbolTable stb, Location loc){
        super(stb, loc);
        cond = _cond;
        ifthen = _then;
        ifelse = _else;
    }

    public void setCond(ExpressionNode e){
        cond =e;
    }

    public void setThen(Node b){
        ifthen = b;
    }

    public void setElse(ConditionNode e){
        ifelse = e;
    }

    public ExpressionNode getCond(){
        return cond;
    }

    public ConditionNode getElse(){
        return ifelse;
    }

    public Node getBody(){
        return ifthen;
    }

    public void setName(String _name){
        condtionName = _name;
    }

    public String getName(){
        return condtionName;
    }

    @Override
    public <T> T accept(ASTBaseVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
