package kstarxin.ast;

import kstarxin.utilities.Location;
import kstarxin.utilities.Symbol;
import kstarxin.utilities.SymbolTable;

public class VariableDeclaratorNode extends Node {
    private String id;
    private ExpressionNode initializer;

    public VariableDeclaratorNode(String _id, ExpressionNode _initializer, SymbolTable stb, Location loc){
        super(stb, loc);
        id = _id;
        initializer = _initializer;
    }

    public String getIdentifier(){
        return id;
    }

    @Override
    public <T> T accept(ASTBaseVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
