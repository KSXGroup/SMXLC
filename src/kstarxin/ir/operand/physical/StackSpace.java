package kstarxin.ir.operand.physical;

import kstarxin.ir.IRBaseVisitor;
import kstarxin.ir.asmir.ASMLevelIRVisitor;
import kstarxin.ir.operand.*;

public class StackSpace extends Address {
    //this is shit design
    public PhysicalRegister base;
    public int offset;
    public StackSpace(PhysicalRegister _base, int _offset){
        super("stack");
        base = _base;
        offset = _offset;
    }

    @Override
    public <T> T accept(ASMLevelIRVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getNASMName(){
        if(offset > 0) return "[" + base.getNASMName() + "+" + offset + "]";
        return "[" + base.getNASMName() + "-" + (-offset) + "]";
    }

    @Override
    public Operand accept(IRBaseVisitor visitor) {
        return null;
    }
}
