package kstarxin.ir.instruction;

import kstarxin.ir.operand.*;

import java.util.HashMap;

public class LoadInstruction extends Instruction {
    public Operand  src;
    public Operand  dest;

    public LoadInstruction(Register _dest, Address _src){
        super("load");
        src     = _src;
        dest    = _dest;
    }

    @Override
    public LoadInstruction copy() {
        if(src instanceof Memory) return new LoadInstruction((Register)dest, new Memory((Memory)src));
        else return new LoadInstruction((Register) dest, (Address) src);
    }

    @Override
    public void replaceOperandForInline(HashMap<Operand, Operand> map) {
        Operand a = null;
        if(src instanceof Memory) ((Memory) src).replaceOperandForInline(map);
        else{
            a = map.get(src);
            if(a == null) throw new RuntimeException();
            else src = a;
        }
        a = map.get(dest);
        if(a == null) throw new RuntimeException();
        else dest = a;
    }
}
