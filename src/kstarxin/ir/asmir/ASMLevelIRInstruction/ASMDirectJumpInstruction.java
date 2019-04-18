package kstarxin.ir.asmir.ASMLevelIRInstruction;

import kstarxin.ir.asmir.ASMBasicBlock;
import kstarxin.ir.asmir.ASMLevelIRMethod;
import kstarxin.ir.asmir.ASMLevelIRVisitor;

public class ASMDirectJumpInstruction extends ASMInstruction{
    public ASMBasicBlock target;
    public ASMDirectJumpInstruction(ASMBasicBlock bb, ASMBasicBlock _target){
        super("JMP", bb);
        target = _target;
    }

    @Override
    public <T> T accept(ASMLevelIRVisitor<T> asmVisitor) {
        return asmVisitor.visit(this);
    }
}
