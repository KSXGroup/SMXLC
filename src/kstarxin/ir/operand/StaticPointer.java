package kstarxin.ir.operand;

import kstarxin.ir.IRBaseVisitor;
import kstarxin.ir.asmir.ASMLevelIRVisitor;
import kstarxin.utilities.NameMangler;

public class StaticPointer extends Label {
    public int spaceSize;
    public int value;
    public boolean initialized;
    public StaticPointer(String _hintName, int _spaceSize){
        super(_hintName);
        spaceSize = _spaceSize;
        initialized = false;
        nasmName = NameMangler.convertToASMName(_hintName);
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
    public <T> T accept(ASMLevelIRVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getNASMName() {
        return "[" + nasmName + "]";
    }
}
