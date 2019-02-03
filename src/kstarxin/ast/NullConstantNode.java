package kstarxin.ast;

import kstarxin.utilities.Location;
import kstarxin.utilities.SymbolTable;

public class NullConstantNode extends ExpressionNode{
    public NullConstantNode(SymbolTable stb, Location loc){
        super(false, stb, loc);
    }
    @Override
    public <T> T accept(ASTBaseVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
