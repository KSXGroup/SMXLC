package kstarxin.ast;

import kstarxin.utilities.Location;
import kstarxin.utilities.SymbolTable;

public class UnaryExpressionNode extends ExpressionNode{
    int op;
    ExpressionNode expr;
    public UnaryExpressionNode(int _op, ExpressionNode _expr, SymbolTable stb, Location loc){
        super(false, stb, loc);
        op = _op;
        expr = _expr;
    }

    public ExpressionNode getRight(){
        return expr;
    }

    public int getOp(){
        return op;
    }

    @Override
    public <T> T accept(ASTBaseVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
