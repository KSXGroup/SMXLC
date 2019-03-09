package kstarxin.ir.instruction;

import kstarxin.ir.operand.*;

public class MoveInstruction extends Instruction {
    public Operand src;
    public Operand dest;
    public MoveInstruction(Register _dest, Register _src){
        super("mov");
        src     = _src;
        dest    = _dest;
    }

    public MoveInstruction(Register _dest, Immediate _src){
        super("mov");
        src     = _src;
        dest    = _dest;
    }
}
