package kstarxin.ast;

import kstarxin.utilities.Location;
import kstarxin.utilities.SymbolTable;

import java.util.ArrayList;

public class MethodCallNode extends ExpressionNode{
    private String methodName;
    private ArrayList<ExpressionNode> para;
    MethodCallNode(String _methodName,ArrayList<ExpressionNode> _para, SymbolTable stb, Location loc){
        super(false,stb, loc);
        methodName = _methodName;
        para = _para;
    }

    public String getMethodName(){
        return methodName;
    }

    public ArrayList<ExpressionNode> getParameterExpressionList(){
        return para;
    }

    @Override
    public <T> T accept(ASTBaseVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
