package kstarxin.ir.instruction;

import kstarxin.ir.operand.Operand;
import kstarxin.utilities.OperatorTranslator;

public class BinaryArithmeticInstruction extends Instruction {
    public int op;
    public Operand lhs;
    public Operand rhs;
    public Operand target;

    private String opSymbolic;
    private String opLiteral;

    public BinaryArithmeticInstruction(int _op, Operand _target, Operand _lhs, Operand _rhs){
        super("binArith");
        op          = _op;
        opSymbolic  = OperatorTranslator.operatorToSymbolicName(_op);
        opLiteral   = OperatorTranslator.operatorToLiteralName(_op);
        lhs         = _lhs;
        rhs         = _rhs;
        target      = _target;
    }
}
