package kstarxin.ast;

import kstarxin.utilities.Location;
import kstarxin.utilities.SymbolTable;

public abstract class StatementNode extends Node {
    public StatementNode(SymbolTable stb, Location loc){
        super(stb, loc);
    }
    @Override
    abstract public <T> T accept(ASTBaseVisitor<T> visitor);
}
