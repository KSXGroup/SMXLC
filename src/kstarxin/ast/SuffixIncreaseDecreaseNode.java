package kstarxin.ast;

import kstarxin.utilities.Location;
import kstarxin.utilities.SymbolTable;

public class SuffixIncreaseDecreaseNode extends ExpressionNode{
    private int op;
    private ExpressionNode expr;
    public SuffixIncreaseDecreaseNode(int _op, ExpressionNode _expr, SymbolTable stb, Location loc){
        super(false, stb, loc);
        op = _op;
        expr = _expr;
    }

    public int getOp(){
        return op;
    }

    public ExpressionNode getExpression(){
        return expr;
    }

    @Override
    public <T> T accept(ASTBaseVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
