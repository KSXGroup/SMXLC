package kstarxin.ir.operand.physical;

import kstarxin.ir.IRBaseVisitor;
import kstarxin.ir.operand.*;

public class StackSpace extends Address {
    int offset;
    public StackSpace(int _offset){
        super("stack");
        offset = _offset;
    }

    @Override
    public String getNASMName(){
        return "[RBP" + "-" + offset + "]";
    }

    @Override
    public Operand accept(IRBaseVisitor visitor) {
        return null;
    }
}
