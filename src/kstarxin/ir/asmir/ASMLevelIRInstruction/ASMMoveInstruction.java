package kstarxin.ir.asmir.ASMLevelIRInstruction;

import kstarxin.ir.asmir.ASMBasicBlock;
import kstarxin.ir.asmir.ASMLevelIRMethod;
import kstarxin.ir.asmir.ASMLevelIRVisitor;
import kstarxin.ir.operand.*;
import kstarxin.utilities.OperatorTranslator.NASMInstructionOperator;

public class ASMMoveInstruction extends ASMInstruction {
    public Operand src;
    public Operand dst;
    public ASMMoveInstruction(NASMInstructionOperator op, ASMBasicBlock bb, Operand _dst, Operand _src){
        super(op.toString(), bb);
        src = _src;
        dst = _dst;
    }

    @Override
    public <T> T accept(ASMLevelIRVisitor<T> asmVisitor) {
        return asmVisitor.visit(this);
    }
}
