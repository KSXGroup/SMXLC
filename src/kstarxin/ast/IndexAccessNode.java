package kstarxin.ast;

import kstarxin.utilities.Location;
import kstarxin.utilities.SymbolTable;

public class IndexAccessNode extends ExpressionNode{
    ExpressionNode expr;
    ExpressionNode index;
    public IndexAccessNode(ExpressionNode _expr, ExpressionNode _index, SymbolTable stb, Location loc){
        super(true, stb, loc);
        expr = _expr;
        index = _index;
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