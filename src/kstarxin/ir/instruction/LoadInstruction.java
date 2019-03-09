package kstarxin.ir.instruction;

import kstarxin.ir.operand.*;

public class LoadInstruction extends Instruction {
    public Operand  src;
    public Operand  dest;

    public LoadInstruction(Register _dest, Address _src){
        super("load");
        src     = _src;
        dest    = _dest;
    }
}
