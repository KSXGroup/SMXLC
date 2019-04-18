package kstarxin.ir.instruction;

import kstarxin.ir.IRBaseVisitor;
import kstarxin.ir.operand.*;

import java.util.HashMap;

public class CompareInstruction extends Instruction {
    public Operand lhs;
    public Operand rhs;
    public CompareInstruction(Operand _lhs, Operand _rhs){
        super("cmp");
        lhs = _lhs;
        rhs = _rhs;
    }

    public void replaceLhs(Operand _lhs){
        lhs = _lhs;
    }

    public void replaceRhs(Operand _rhs){
        rhs = _rhs;
    }

    @Override
    public CompareInstruction copy() {
        Operand nlhs, nrhs;
        if(lhs instanceof Memory) nlhs = new Memory((Memory) lhs);
        else nlhs = lhs;
        if(rhs instanceof Memory) nrhs = new Memory((Memory) rhs);
        else nrhs = rhs;
        return new CompareInstruction(nlhs, nrhs);
    }

    @Override
    public void replaceOperandForInline(HashMap<Operand, Operand> map) {
        Operand a = null;
        if(lhs instanceof Memory) ((Memory) lhs).replaceOperandForInline(map);
        else if(!(lhs instanceof Immediate)){
             a = map.get(lhs);
             if(a == null) throw new RuntimeException();
             else lhs = a;
        }

        if(rhs instanceof Memory) ((Memory) rhs).replaceOperandForInline(map);
        else if(!(rhs instanceof Immediate)){
            a = map.get(rhs);
            if(a == null) throw new RuntimeException();
            else rhs = a;
        }
    }

    @Override
    public void collectDefUseInfo() {
        use.clear();
        def.clear();
        if(lhs instanceof VirtualRegister) use.add((VirtualRegister) lhs);
        else if(lhs instanceof Memory) use.addAll(((Memory) lhs).collectUseInfo());

        if(rhs instanceof VirtualRegister) use.add((VirtualRegister) rhs);
        else if(rhs instanceof Memory) use.addAll(((Memory) rhs).collectUseInfo());
    }

    @Override
    public Address replaceOperandForGlobalVariableOptimization(HashMap<Address, VirtualRegister> map) {
        if(lhs instanceof StaticString || lhs instanceof StaticPointer){
            lhs = map.get(lhs);
            if(lhs == null) throw new RuntimeException();
        }

        if(rhs instanceof StaticString || rhs instanceof StaticPointer){
            rhs = map.get(rhs);
            if(rhs == null) throw new RuntimeException();
        }
        return null;
    }

    @Override
    public <T> T accept(IRBaseVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
