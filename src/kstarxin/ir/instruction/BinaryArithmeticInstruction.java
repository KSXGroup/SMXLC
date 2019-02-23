package kstarxin.ir.instruction;

import kstarxin.ir.operand.Operand;

public class BinaryArithmeticInstruction extends Instruction {
    public int op;
    public Operand lhs;
    public Operand rhs;
    public BinaryArithmeticInstruction(int _op, Operand _lhs, Operand _rhs){
        super("binArith");
        op = _op;
        lhs = _lhs;
        rhs = _rhs;
    }
}
