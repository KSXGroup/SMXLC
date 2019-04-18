package kstarxin.ir.instruction;

import kstarxin.ir.IRBaseVisitor;
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

    @Override
    public void collectDefUseInfo() {
        use.clear();
        def.clear();
        if(src instanceof VirtualRegister) use.add((VirtualRegister) src);
        def.add((VirtualRegister) dest);
    }

    @Override
    public Address replaceOperandForGlobalVariableOptimization(HashMap<Address, VirtualRegister> map) {
        return null;
    }

    @Override
    public <T> T accept(IRBaseVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
