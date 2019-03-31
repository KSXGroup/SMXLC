package kstarxin.ir.instruction;

import kstarxin.ir.operand.*;
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
        if(dest == src){
          if(dest instanceof Memory){
                Memory copy = new Memory((Memory) dest);
                return new UnaryInstruction(op, copy, copy);
          }else if(!(dest instanceof Immediate)) return new UnaryInstruction(op, dest, src);
        } else{
            if(dest instanceof Memory){
                if(src instanceof Memory) return new UnaryInstruction(op, new Memory((Memory) dest), new Memory((Memory) src));
                else if(!(src instanceof Immediate)) return new UnaryInstruction(op, new Memory((Memory) dest), src);
                else throw new RuntimeException();
            }else if(!(dest instanceof Immediate)){
                if(src instanceof Memory) return new UnaryInstruction(op, dest, new Memory((Memory)src));
                else if(!(src instanceof Immediate)) return new UnaryInstruction(op, dest, src);
                else throw new RuntimeException();
            }else throw new RuntimeException();
        }
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
