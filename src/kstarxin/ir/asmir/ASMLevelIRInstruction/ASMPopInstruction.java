package kstarxin.ir.asmir.ASMLevelIRInstruction;

import kstarxin.ir.asmir.ASMBasicBlock;
import kstarxin.ir.asmir.ASMLevelIRMethod;
import kstarxin.ir.asmir.ASMLevelIRVisitor;
import kstarxin.ir.operand.Memory;
import kstarxin.ir.operand.Operand;
import kstarxin.ir.operand.VirtualRegister;

import java.util.HashMap;

public class ASMPopInstruction extends ASMInstruction{
    public Operand op;
    public ASMPopInstruction(ASMBasicBlock bb, Operand _op){
        super("POP", bb);
        op = _op;
    }

    @Override
    public <T> T accept(ASMLevelIRVisitor<T> asmVisitor) {
        return asmVisitor.visit(this);
    }

    @Override
    public void collectInfo() {
        def.clear();
        use.clear();
        if(op instanceof VirtualRegister) def.add((VirtualRegister)op);
        else if(op instanceof Memory) use.addAll(((Memory) op).collectUseInfo());
    }

    @Override
    public void replaceOperandForSpill(HashMap<VirtualRegister, VirtualRegister> map) {
        if(op instanceof VirtualRegister && map.containsKey(op)) op = map.get(op);
        if(op instanceof Memory) ((Memory) op).replaceForSpill(map);
    }
}
