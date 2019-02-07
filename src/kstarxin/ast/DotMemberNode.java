package kstarxin.ast;

import kstarxin.utilities.Location;
import kstarxin.utilities.SymbolTable;

public class DotMemberNode extends ExpressionNode{
    ExpressionNode expr;
    String member;
    public DotMemberNode(ExpressionNode _expr, String _member, SymbolTable stb, Location loc){
        super(true, stb, loc);
        expr = _expr;
        member = _member;
    }

    public ExpressionNode getExpression() {
        return expr;
    }

    public String getMember() {
        return member;
    }

    @Override
    public <T> T accept(ASTBaseVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
