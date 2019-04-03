package kstarxin.ir.instruction;

import kstarxin.ir.operand.*;

import java.util.HashMap;

public class StoreInstruction extends Instruction{
    public Operand  src;
    public Operand  dest;
    public StoreInstruction(Address _dest, Register _src){
        super("load");
        src     = _src;
        dest    = _dest;
    }

    public StoreInstruction(Address _dest, Immediate _src){
        super("load");
        src     = _src;
        dest    = _dest;
    }

    @Override
    public StoreInstruction copy() {
        if(src instanceof Register){
            if(dest instanceof Memory) return new StoreInstruction(new Memory((Memory)dest), (Register) src);
            else return new StoreInstruction((Address) dest, (Register) src);
        }
        else{
            if(dest instanceof Memory) return new StoreInstruction(new Memory((Memory)dest), (Immediate)src);
            else return new StoreInstruction((Address) dest, (Immediate) src);
        }
    }

    @Override
    public void replaceOperandForInline(HashMap<Operand, Operand> map) {
        Operand a = null;
        if(dest instanceof  Memory) ((Memory) dest).replaceOperandForInline(map);
        else {
            a = map.get(dest);
            if(a == null) throw new RuntimeException();
            dest = a;
        }

        if(!(src instanceof Immediate)) a = map.get(src);
        else a = src;
        if(a == null) throw new RuntimeException();
        src = a;
    }

    @Override
    public void collectDefUseInfo() {
        use.clear();
        def.clear();
        if(src instanceof Register) use.add(src);
        if(dest instanceof StaticString || dest instanceof StaticPointer) def.add(dest);
        else if(dest instanceof Memory) use.addAll(((Memory) dest).collectUseInfo());
    }

    @Override
    public Address replaceOperandForGlobalVariableOptimization(HashMap<Address, VirtualRegister> map) {
        if(dest instanceof StaticPointer || dest instanceof StaticString) {
            VirtualRegister vreg = map.get(dest);
            if(vreg == null)
                throw new RuntimeException();
            if(src instanceof Immediate) replaceThisWith(new MoveInstruction(vreg, (Immediate) src));
            else if(src instanceof VirtualRegister) replaceThisWith(new MoveInstruction(vreg, (VirtualRegister)src));
            else throw new RuntimeException();
            return (Address) dest;
        }
        return null;
    }
}
