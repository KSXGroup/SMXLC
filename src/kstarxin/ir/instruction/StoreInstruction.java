package kstarxin.ir.instruction;

import kstarxin.ir.operand.*;

public class StoreInstruction extends Instruction{
    public Operand  src;
    public Operand  dest;
    public StoreInstruction(Address _dest, Register _src){
        super("load");
        src     = _src;
        dest    = _dest;
    }

    public StoreInstruction(Address _dest, Immediate _src){
        super("load");
        src     = _src;
        dest    = _dest;
    }
}
