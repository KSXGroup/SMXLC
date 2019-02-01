package kstarxin.ast;

import kstarxin.utilities.Location;
import kstarxin.utilities.MxType;
import kstarxin.utilities.SymbolTable;

public class ParameterDeclarationNode extends Node {
    private TypeNode type;
    private String id;

    ParameterDeclarationNode(TypeNode _type, String _id, SymbolTable stb, Location loc){
        super(stb, loc);
        type = _type;
        id = _id;
    }

    @Override
    public <T> T accept(ASTBaseVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
