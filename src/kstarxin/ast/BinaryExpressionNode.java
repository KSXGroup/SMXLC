package kstarxin.ast;

import kstarxin.utilities.Location;
import kstarxin.utilities.SymbolTable;

public class BinaryExpressionNode extends ExpressionNode{
    ExpressionNode left;
    ExpressionNode right;
    int op;
    public BinaryExpressionNode(ExpressionNode _left, int _op, ExpressionNode _right, SymbolTable stb, Location loc){
        super(false, stb, loc);
        left = _left;
        right = _right;
        op = _op;
    }

    public ExpressionNode getLeft(){
        return left;
    }

    public ExpressionNode getRight(){
        return right;
    }

    public int getOp(){
        return op;
    }

    @Override
    public <T> T accept(ASTBaseVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
