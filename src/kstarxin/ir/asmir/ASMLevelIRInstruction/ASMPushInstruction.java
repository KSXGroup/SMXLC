package kstarxin.ir.asmir.ASMLevelIRInstruction;

import kstarxin.ir.asmir.ASMBasicBlock;
import kstarxin.ir.asmir.ASMLevelIRMethod;
import kstarxin.ir.asmir.ASMLevelIRVisitor;
import kstarxin.ir.operand.Operand;

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
}
