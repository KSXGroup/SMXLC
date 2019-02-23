package kstarxin.ir.instruction;

import kstarxin.ir.operand.*;

public class LoadInstruction extends Instruction {
    public Operand src;
    public Operand dest;
    public Immediate offset;
    public LoadInstruction(Operand _dest, Operand _src, Immediate _offset){
        super("load");
        src     = _src;
        dest    = _dest;
        offset  = _offset;
    }
}
