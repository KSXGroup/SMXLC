package kstarxin.ast;

import kstarxin.ir.BasicBlock;
import kstarxin.utilities.Location;
import kstarxin.utilities.MxType;
import kstarxin.utilities.SymbolTable;

abstract  public class ExpressionNode extends Node {
    private MxType      type;
    private boolean     isLeftValue;

    //For shortcut evaluation
    //This is not elegant but useful
    //Thank to abcdabcd987
    public  BasicBlock  trueJumpTarget;
    public  BasicBlock  falseJumpTarget;

    public ExpressionNode(boolean _isLeftValue, SymbolTable stb, Location loc) {
        super(stb, loc);
        isLeftValue     = _isLeftValue;
        trueJumpTarget  = null;
        falseJumpTarget = null;
    }

    public MxType getType() {
        return type;
    }

    public void setType(MxType _type) {
        type = _type;
    }

    public void setLeftValue() {
        isLeftValue = true;
    }

    public void setRightValue(){
        isLeftValue = false;
    }

    public boolean isLeftValue(){
        return isLeftValue;
    }

    @Override
    abstract public <T> T accept(ASTBaseVisitor<T> visitor);
}
