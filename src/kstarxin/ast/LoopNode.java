package kstarxin.ast;

import kstarxin.utilities.Location;
import kstarxin.utilities.SymbolTable;

public class LoopNode extends StatementNode{
    private ExpressionNode init =null;
    private ExpressionNode cond = null;
    private ExpressionNode step = null;
    private Node body = null;
    private boolean isForLoop = false;
    private String loopName = null;

    LoopNode(boolean _isForLoop, ExpressionNode _init, ExpressionNode _cond, ExpressionNode _step,Node _body ,SymbolTable stb, Location loc){
        super(stb,loc);
        isForLoop = _isForLoop;
        init = _init;
        cond = _cond;
        step = _step;
        body = _body;
    }

    public ExpressionNode getInitializer(){
        return init;
    }

    public ExpressionNode getCondition(){
        return cond;
    }

    public ExpressionNode getStep(){
        return step;
    }

    public Node getBody(){
        return body;
    }

    public boolean isForLoop(){
        return isForLoop;
    }

    public void setLoopName(String _loopName) {
        this.loopName = _loopName;
    }

    public String getLoopName(){
        return loopName;
    }

    @Override
    public <T> T accept(ASTBaseVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
