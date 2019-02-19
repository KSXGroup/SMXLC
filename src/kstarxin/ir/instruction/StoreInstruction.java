package kstarxin.ir.instruction;

import kstarxin.ir.operand.Operand;

public class StoreInstruction extends Instruction {
    Operand dest;
    Operand src;
    Operand offset;
    public StoreInstruction(Operand _dest, Operand _src, Operand _offset) {
        super("store");
        dest    = _dest;
        src     = _src;
        offset  = _offset;
    }
}
