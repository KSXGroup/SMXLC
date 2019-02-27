package kstarxin.ir.instruction;

import kstarxin.ir.operand.Operand;

public class MoveInstruction extends Instruction {
    public Operand src;
    public Operand dest;
    public MoveInstruction(Operand _dest, Operand _src){
        super("mov");
        src     = _src;
        dest    = _dest;
    }
}
