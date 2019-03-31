package kstarxin.ir.instruction;

import kstarxin.ir.operand.Memory;
import kstarxin.ir.operand.Operand;
import kstarxin.utilities.OperatorTranslator;

import java.util.HashMap;

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

    @Override
    public UnaryInstruction copy() {
        return new UnaryInstruction(op, dest, src);
    }

    @Override
    public void replaceOperandForInline(HashMap<Operand, Operand> map) {
        Operand a = null;
        if(src == dest){
            if(src instanceof Memory) ((Memory) src).replaceOperandForInline(map);
            else{
                a = map.get(src);
                if(a == null) throw new RuntimeException();
                src = a;
                dest = a;
            }
        }
        else{
            if(src instanceof Memory) ((Memory) src).replaceOperandForInline(map);
            else{
                a = map.get(src);
                if(a == null) throw new RuntimeException();
                src = a;
            }

            if(dest instanceof Memory) ((Memory) dest).replaceOperandForInline(map);
            else{
                a = map.get(dest);
                if(a == null) throw new RuntimeException();
                dest = a;
            }
        }
    }
}
