package kstarxin.ast;

import kstarxin.utilities.Location;
import kstarxin.utilities.SymbolTable;

import java.util.ArrayList;

public class DotMemberMethodCallNode extends ExpressionNode{
    private String memberMethodName;
    private ExpressionNode expr;
    private ArrayList<ExpressionNode> paraList;
    public DotMemberMethodCallNode(ExpressionNode _expr, String _memberMethodName, ArrayList<ExpressionNode> _paraList, SymbolTable stb, Location loc){
        super(false, stb, loc);
        expr = _expr;
        memberMethodName = _memberMethodName;
        paraList = _paraList;
    }

    public String getMemberMethodName() {
        return memberMethodName;
    }

    public ArrayList<ExpressionNode> getParameterExpressionList(){
        return paraList;
    }

    @Override
    public <T> T accept(ASTBaseVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
