package kstarxin.ir.asmir.ASMLevelIRInstruction;

import kstarxin.ir.asmir.ASMBasicBlock;
import kstarxin.ir.asmir.ASMLevelIRMethod;
import kstarxin.ir.asmir.ASMLevelIRVisitor;
import kstarxin.ir.operand.VirtualRegister;
import kstarxin.utilities.OperatorTranslator.NASMInstructionOperator;

public class ASMSetInstruction extends ASMInstruction{
    public VirtualRegister dst;
    public NASMInstructionOperator operator;
    public ASMSetInstruction(NASMInstructionOperator op, ASMBasicBlock bb, VirtualRegister _dst){
        super(op.toString(), bb);
        dst = _dst;
        operator = op;
    }

    @Override
    public <T> T accept(ASMLevelIRVisitor<T> asmVisitor) {
        return asmVisitor.visit(this);
    }
}
