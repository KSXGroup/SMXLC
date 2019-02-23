package kstarxin.ir.instruction;

import kstarxin.ir.operand.Operand;

public class CompareInstruction extends Instruction {
    public Operand lhs;
    public Operand rhs;
    public CompareInstruction(Operand _lhs, Operand _rhs){
        super("cmp");
        lhs = _lhs;
        rhs = _rhs;
    }
}
