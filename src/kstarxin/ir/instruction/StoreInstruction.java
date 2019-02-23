package kstarxin.ir.instruction;

import kstarxin.ir.operand.*;

public class StoreInstruction extends Instruction{
    public Operand src;
    public Operand dest;
    public Immediate offset;
    public StoreInstruction(Operand _dest, Operand _src, Immediate _offset){
        super("store");
        dest    = _dest;
        src     = _src;
        offset  = _offset;
    }
}
