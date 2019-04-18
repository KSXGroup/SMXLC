package kstarxin.ir.operand;

import kstarxin.ir.IRBaseVisitor;

public class StaticPointer extends Label {
    public int spaceSize;
    public int value;
    public boolean initialized;
    public StaticPointer(String _hintName, int _spaceSize){
        super(_hintName);
        spaceSize = _spaceSize;
        initialized = false;
        value = 0;
    }

    public void setInitialized(int _value){
        initialized = true;
        value = _value;
    }

    public String getInitName(){
        if(initialized) return spaceSize + "[" + hintName + "]\t" + value;
        else  return spaceSize + "[" + hintName + "]";
    }

    @Override
    public String getDisplayName(){
       return spaceSize + "[" + hintName + "]";
    }

    @Override
    public Operand accept(IRBaseVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getNASMName() {
        return null;
    }
}
