package kstarxin.ast;

import kstarxin.utilities.Location;
import kstarxin.utilities.SymbolTable;

public class BooleanConstantNode extends ExpressionNode{
    boolean value;
    public BooleanConstantNode(SymbolTable stb, Location loc){
        super(false, stb, loc);
        value = false;
    }

    public BooleanConstantNode(String _value, SymbolTable stb, Location loc){
        super(false, stb, loc);
        if(_value.contentEquals( "true")) value = true;
        else if(_value.contentEquals( "false"))value = false;
        else value = false;
    }

    public boolean getValue(){
        return value;
    }

    public void setValue(boolean _value){
        value = _value;
    }

    @Override
    public <T> T accept(ASTBaseVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
