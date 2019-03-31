package kstarxin.ir.instruction;

import kstarxin.ir.operand.*;

import java.util.HashMap;

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

    @Override
    public MoveInstruction copy() {
        if(src instanceof Immediate) return new MoveInstruction((Register)dest, (Immediate)src);
        else return new MoveInstruction((Register)dest, (Register)src);
    }

    @Override
    public void replaceOperandForInline(HashMap<Operand, Operand> map) {
       Operand nsrc = null, ndest = null;
       if(src instanceof Immediate) nsrc = src;
       else nsrc = map.get(src);
       ndest = map.get(dest);
       if(ndest == null || nsrc == null)
           throw new RuntimeException();
       src = nsrc;
       dest = ndest;
    }
}
