package kstarxin.ir.instruction;

import kstarxin.ir.IRBaseVisitor;
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

    @Override
    public void collectDefUseInfo() {
        def.clear();
        use.clear();
        def.add((VirtualRegister) dest);
        if(src instanceof Memory) use.addAll(((Memory) src).collectUseInfo());
    }

    @Override
    public Address replaceOperandForGlobalVariableOptimization(HashMap<Address, VirtualRegister> map) {
        if(src instanceof StaticPointer || src instanceof StaticString){
            VirtualRegister vreg = map.get(src);
            if(vreg == null) throw new RuntimeException();
            replaceThisWith(new MoveInstruction((Register) dest, vreg));
        }
        return null;
    }

    @Override
    public <T> T accept(IRBaseVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
