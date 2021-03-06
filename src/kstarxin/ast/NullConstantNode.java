package kstarxin.ast;

import kstarxin.utilities.Location;
import kstarxin.utilities.MxType;
import kstarxin.utilities.SymbolTable;

public class NullConstantNode extends ExpressionNode{
    public NullConstantNode(SymbolTable stb, Location loc){
        super(false, stb, loc);
        super.setType(new MxType("null"));
    }
    @Override
    public <T> T accept(ASTBaseVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
