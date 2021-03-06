package kstarxin.ast;

import kstarxin.utilities.Location;
import kstarxin.utilities.SymbolTable;

public class BreakNode extends JumpNode{
    public BreakNode(SymbolTable stb, Location loc){
        super(stb, loc);
    }

    @Override
    public <T> T accept(ASTBaseVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
