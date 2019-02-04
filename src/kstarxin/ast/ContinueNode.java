package kstarxin.ast;

import kstarxin.utilities.Location;
import kstarxin.utilities.SymbolTable;

public class ContinueNode extends JumpNode{
    public ContinueNode(SymbolTable stb, Location loc){
        super(stb, loc);
    }

    @Override
    public <T> T accept(ASTBaseVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
