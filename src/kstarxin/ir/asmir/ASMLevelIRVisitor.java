package kstarxin.ir.asmir;

import kstarxin.ir.asmir.ASMLevelIRInstruction.*;

public interface ASMLevelIRVisitor<T> {
    public T visit(ASMInstruction inst);
    public T visit(ASMBinaryInstruction inst);
    public T visit(ASMCallInstruction inst);
    public T visit(ASMCDQInstruction inst);
    public T visit(ASMCompareInstruction inst);
    public T visit(ASMConditionalJumpInstruction inst);
    public T visit(ASMDInstruction inst);
    public T visit(ASMDirectJumpInstruction inst);
    public T visit(ASMLabelInstruction inst);
    public T visit(ASMLEAInstruction inst);
    public T visit(ASMLeaveInstruction inst);
    public T visit(ASMMoveInstruction inst);
    public T visit(ASMPopInstruction inst);
    public T visit(ASMPushInstruction inst);
    public T visit(ASMRESInstruction inst);
    public T visit(ASMReturnInstruction inst);
    public T visit(ASMRomDataInstruction inst);
    public T visit(ASMSetInstruction inst);
    public T visit(ASMUnaryInstruction inst);
}
