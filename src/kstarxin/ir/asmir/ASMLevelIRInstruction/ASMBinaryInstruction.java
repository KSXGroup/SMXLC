package kstarxin.ir.asmir.ASMLevelIRInstruction;

import kstarxin.ir.asmir.ASMBasicBlock;
import kstarxin.ir.asmir.ASMLevelIRMethod;
import kstarxin.ir.asmir.ASMLevelIRVisitor;
import kstarxin.ir.operand.Operand;
import kstarxin.utilities.OperatorTranslator.NASMInstructionOperator;

public class ASMBinaryInstruction extends ASMInstruction {
    public NASMInstructionOperator operator;
    public Operand src;
    public Operand dst;
    public ASMBinaryInstruction(NASMInstructionOperator op, ASMBasicBlock bb, Operand _dst, Operand _src){
        super(op.toString(), bb);
        operator = op;
        src = _src;
        dst = _dst;
    }

    @Override
    public <T> T accept(ASMLevelIRVisitor<T> asmVisitor) {
        return asmVisitor.visit(this);
    }
}
