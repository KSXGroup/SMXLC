package kstarxin.ast;

import kstarxin.utilities.Location;
import kstarxin.utilities.MxType;
import kstarxin.utilities.SymbolTable;

public abstract class ExpressionNode extends Node{
    private MxType type;
    public ExpressionNode(SymbolTable stb, Location loc){
        super(stb, loc);
    }

    public MxType getType(){
        return type;
    }

    public void setType(MxType _type){
        type = _type;
    }
}
