package kstarxin.ir.asmir.ASMLevelIRInstruction;

import kstarxin.ir.asmir.ASMBasicBlock;
import kstarxin.ir.asmir.ASMLevelIRMethod;
import kstarxin.ir.asmir.ASMLevelIRVisitor;
import kstarxin.ir.operand.Operand;
import kstarxin.utilities.OperatorTranslator.NASMInstructionOperator;

public class ASMUnaryInstruction extends ASMInstruction {
    public NASMInstructionOperator operator;
    public Operand src;
    public ASMUnaryInstruction(NASMInstructionOperator op, ASMBasicBlock bb, Operand _src){
        super(op.toString(), bb);
        src = _src;
        operator = op;
    }

    @Override
    public <T> T accept(ASMLevelIRVisitor<T> asmVisitor) {
        return asmVisitor.visit(this);
    }
}
