package kstarxin.ast;

import kstarxin.utilities.Location;
import kstarxin.utilities.MxType;
import kstarxin.utilities.SymbolTable;

public class IdentifierNode extends ExpressionNode{
    private String id;
    IdentifierNode(String _id, SymbolTable stb, Location loc){
        super(true,stb, loc);
        id = _id;
    }

    public String getIdentifier(){
        return id;
    }

    @Override
    public <T> T accept(ASTBaseVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
