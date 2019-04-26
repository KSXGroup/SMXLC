package kstarxin.ir.asmir.ASMLevelIRInstruction;

import kstarxin.ir.asmir.ASMBasicBlock;
import kstarxin.ir.asmir.ASMLevelIRMethod;
import kstarxin.ir.asmir.ASMLevelIRVisitor;
import kstarxin.ir.operand.*;
import kstarxin.ir.operand.physical.PhysicalRegister;
import kstarxin.ir.operand.physical.StackSpace;
import kstarxin.utilities.OperatorTranslator.NASMInstructionOperator;

public class ASMMoveInstruction extends ASMInstruction {
    public NASMInstructionOperator op;
    public Operand src;
    public Operand dst;
    public ASMMoveInstruction(NASMInstructionOperator _op, ASMBasicBlock bb, Operand _dst, Operand _src){
        super(_op.toString(), bb);
        src = _src;
        dst = _dst;
        op  = _op;
    }

    @Override
    public <T> T accept(ASMLevelIRVisitor<T> asmVisitor) {
        return asmVisitor.visit(this);
    }

    @Override
    public void collectInfo() {
        if(src instanceof VirtualRegister && (((VirtualRegister) src).spaceAllocatedTo == null || ((VirtualRegister) src).spaceAllocatedTo instanceof PhysicalRegister)) use.add((VirtualRegister)src);
        else if(src instanceof Memory) use.addAll(((Memory) src).collectUseInfo());
        if(dst instanceof VirtualRegister) def.add((VirtualRegister)dst);
        else if(dst instanceof Memory) use.addAll(((Memory) dst).collectUseInfo());
    }
}
