package kstarxin.ir.instruction;

import kstarxin.ir.operand.Operand;
import kstarxin.utilities.OperatorTranslator;

public class UnaryInstruction extends Instruction{
    public int      op;
    public Operand  dest;
    public UnaryInstruction(int _op, Operand _dest){
        super(OperatorTranslator.operatorToSymbolicName(_op));
        dest    = _dest;
        op      = _op;
    }
}
