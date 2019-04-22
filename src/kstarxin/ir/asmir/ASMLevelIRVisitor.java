package kstarxin.ir.asmir;

import kstarxin.ir.asmir.ASMLevelIRInstruction.*;
import kstarxin.ir.operand.*;

public interface ASMLevelIRVisitor<T> {
    public T visit(ASMInstruction inst);
    public T visit(ASMBinaryInstruction inst);
    public T visit(ASMCallInstruction inst);
    public T visit(ASMCDQInstruction inst);
    public T visit(ASMCompareInstruction inst);
    public T visit(ASMConditionalJumpInstruction inst);
    public T visit(ASMDInstruction inst);
    public T visit(ASMDirectJumpInstruction inst);
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
    public T visit(Operand op);
    public T visit(Address op);
    public T visit(Constant op);
    public T visit(Immediate op);
    public T visit(Label op);
    public T visit(Memory op);
    public T visit(Register op);
    public T visit(StaticPointer op);
    public T visit(StaticString op);
    public T visit(StringLiteral op);
    public T visit(VirtualRegister op);
}
