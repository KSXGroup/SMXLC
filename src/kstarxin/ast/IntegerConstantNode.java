package kstarxin.ast;

import kstarxin.utilities.Location;
import kstarxin.utilities.SymbolTable;

public class IntegerConstantNode extends ExpressionNode{
    private int value;
    public IntegerConstantNode(String _value, SymbolTable stb, Location loc){
        super(false, stb, loc);
        value = Integer.parseInt(_value);
    }

    public int getValue() {
        return value;
    }

    @Override
    public <T> T accept(ASTBaseVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
