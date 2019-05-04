package kstarxin.ir.asmir.ASMLevelIRInstruction;

import kstarxin.ir.asmir.ASMBasicBlock;
import kstarxin.ir.asmir.ASMLevelIRMethod;
import kstarxin.ir.asmir.ASMLevelIRVisitor;
import kstarxin.ir.operand.*;

import java.util.HashMap;

public class ASMCompareInstruction extends ASMInstruction{
    public Operand lhs, rhs;
    public ASMCompareInstruction(ASMBasicBlock bb, Operand _lhs, Operand _rhs){
        super("CMP", bb);
        lhs = _lhs;
        rhs = _rhs;
    }

    @Override
    public <T> T accept(ASMLevelIRVisitor<T> asmVisitor) {
        return asmVisitor.visit(this);
    }

    @Override
    public void collectInfo() {
        use.clear();
        def.clear();
        if(lhs instanceof VirtualRegister) use.add((VirtualRegister) lhs);
        else if(lhs instanceof Memory) use.addAll(((Memory) lhs).collectUseInfo());
        if(rhs instanceof VirtualRegister) use.add((VirtualRegister) rhs);
        else if(rhs instanceof Memory) use.addAll(((Memory) rhs).collectUseInfo());
    }

    @Override
    public void replaceOperandForSpill(HashMap<VirtualRegister, VirtualRegister> map) {
        if(lhs instanceof VirtualRegister && map.containsKey(lhs)) lhs = map.get(lhs);
        if(rhs instanceof VirtualRegister && map.containsKey(rhs)) rhs = map.get(rhs);
        if(lhs instanceof Memory){
            lhs = new Memory((Memory)lhs);
            ((Memory) lhs).replaceForSpill(map);
        }
        if(rhs instanceof Memory){
            rhs = new Memory((Memory)rhs);
            ((Memory) rhs).replaceForSpill(map);
        }
    }
}
