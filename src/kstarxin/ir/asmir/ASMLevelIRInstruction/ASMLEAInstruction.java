package kstarxin.ir.asmir.ASMLevelIRInstruction;

import kstarxin.ir.asmir.ASMBasicBlock;
import kstarxin.ir.asmir.ASMLevelIRMethod;
import kstarxin.ir.asmir.ASMLevelIRVisitor;
import kstarxin.ir.operand.Memory;
import kstarxin.ir.operand.Operand;
import kstarxin.ir.operand.VirtualRegister;

import java.util.HashMap;

public class ASMLEAInstruction extends ASMInstruction {
    public Operand dst;
    public Operand src;
    public ASMLEAInstruction(ASMBasicBlock bb,Operand _dst, Operand _src){
        super("LEA", bb);
        dst = _dst;
        src = _src;
    }

    @Override
    public <T> T accept(ASMLevelIRVisitor<T> asmVisitor) {
        return asmVisitor.visit(this);
    }

    @Override
    public void collectInfo() {
        def.clear();
        use.clear();
        if(dst instanceof VirtualRegister) def.add((VirtualRegister)dst);
        else if(dst instanceof Memory) use.addAll(((Memory) dst).collectUseInfo());
        if(src instanceof VirtualRegister) use.add((VirtualRegister) src);
        else if(src instanceof Memory) use.addAll(((Memory) src).collectUseInfo());
    }

    @Override
    public void replaceOperandForSpill(HashMap<VirtualRegister, VirtualRegister> map) {
        if(dst instanceof VirtualRegister && map.containsKey(dst))  dst = map.get(dst);
        if(src instanceof Memory) ((Memory) src).replaceForSpill(map);
    }
}
