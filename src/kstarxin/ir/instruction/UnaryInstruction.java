package kstarxin.ir.instruction;

import kstarxin.ir.operand.Operand;
import kstarxin.utilities.OperatorTranslator;

public class UnaryInstruction extends Instruction{
    public int      op;
    public Operand  dest;
    public Operand  src;
    public UnaryInstruction(int _op, Operand _dest, Operand _src){
        super(OperatorTranslator.operatorToSymbolicName(_op));
        dest    = _dest;
        op      = _op;
        src     = _src;
    }
}
