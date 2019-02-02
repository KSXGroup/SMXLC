package kstarxin.ast;

import kstarxin.utilities.Location;
import kstarxin.utilities.MxType;
import kstarxin.utilities.Symbol;
import kstarxin.utilities.SymbolTable;

public class TypeNode extends Node{
    private MxType type;

    public TypeNode(MxType _type, SymbolTable stb, Location loc){
        super(stb, loc);
        type = _type;
    }

    public MxType getType(){
        return  type;
    }

    public void setType(MxType _type){
        type = _type;
    }

    @Override
    public <T> T accept(ASTBaseVisitor<T> visitor) {
        return null;
    }
}
