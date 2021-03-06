package kstarxin.ast;

import kstarxin.utilities.Location;
import kstarxin.utilities.MxType;
import kstarxin.utilities.SymbolTable;

public class StringConstantNode extends ExpressionNode{
    private String value;
    StringConstantNode(String _value, SymbolTable stb, Location loc){
        super(false, stb, loc);
        super.setType(new MxType("string"));
        value = _value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public <T> T accept(ASTBaseVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
