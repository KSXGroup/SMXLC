package kstarxin.ir.asmir.ASMLevelIRInstruction;

import kstarxin.ir.asmir.ASMBasicBlock;
import kstarxin.ir.asmir.ASMLevelIRMethod;
import kstarxin.ir.asmir.ASMLevelIRVisitor;
import kstarxin.ir.operand.Memory;
import kstarxin.ir.operand.Operand;
import kstarxin.ir.operand.VirtualRegister;

public class ASMPushInstruction extends ASMInstruction {
    public Operand src;
    public ASMPushInstruction(ASMBasicBlock bb, Operand _src) {
        super("PUSH", bb);
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
        if(src instanceof VirtualRegister) use.add((VirtualRegister)src);
        else if(src instanceof Memory) use.addAll(((Memory) src).collectUseInfo());
    }
}
