package kstarxin.ir.operand;

import kstarxin.ir.IRBaseVisitor;
import kstarxin.ir.asmir.ASMLevelIRVisitor;

abstract public class Operand {
    public String hintName;
    public String nasmName;
    public Operand(String _hintName){
        hintName = _hintName;
        nasmName = null;
    }

    public String getDisplayName(){
        return hintName;
    }

    public abstract String getNASMName();

    public abstract Operand accept(IRBaseVisitor visitor);

    public abstract <T> T accept(ASMLevelIRVisitor<T> visitor);
}
