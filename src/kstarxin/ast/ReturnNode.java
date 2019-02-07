package kstarxin.ast;

import kstarxin.utilities.Location;
import kstarxin.utilities.SymbolTable;

public class ReturnNode extends JumpNode {
    private ExpressionNode ret;
    public ReturnNode(ExpressionNode _ret, SymbolTable stb, Location loc){
        super(stb, loc);
        ret = _ret;
    }

    public ExpressionNode getReturnValue(){
        return ret;
    }

    @Override
    public <T> T accept(ASTBaseVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
