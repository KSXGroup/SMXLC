package kstarxin.ast;

import kstarxin.utilities.Location;
import kstarxin.utilities.SymbolTable;

public class IndexAccessNode extends ExpressionNode{
    ExpressionNode expr;
    ExpressionNode index;
    boolean isAssgined;
    public IndexAccessNode(ExpressionNode _expr, ExpressionNode _index, SymbolTable stb, Location loc){
        super(true, stb, loc);
        expr = _expr;
        index = _index;
    }

    public boolean isAssgined(){
        return isAssgined;
    }

    public void setAssigned(){
        isAssgined = true;
    }

    public ExpressionNode getExpression() {
        return expr;
    }

    public ExpressionNode getIndex() {
        return index;
    }

    @Override
    public <T> T accept(ASTBaseVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
