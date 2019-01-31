package kstarxin.ast;

import kstarxin.utilities.Location;
import kstarxin.utilities.SymbolTable;

public abstract class DeclarationNode extends Node {
    public DeclarationNode(SymbolTable stb, Location loc){
        super(stb, loc);
    }

    @Override
    public <T> T accept(ASTBaseVisitor<T> visitor) {
        return null;
    }
}
