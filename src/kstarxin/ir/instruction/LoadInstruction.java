package kstarxin.ir.instruction;

import kstarxin.ir.operand.Operand;

public class LoadInstruction extends Instruction {
    private Operand dest;
    private Operand src;
    private Operand offset;
    public LoadInstruction(Operand _dest, Operand _src, Operand _offset){
        super("load");
        dest = _dest;
        src = _src;
        offset = _offset;
    }

}
