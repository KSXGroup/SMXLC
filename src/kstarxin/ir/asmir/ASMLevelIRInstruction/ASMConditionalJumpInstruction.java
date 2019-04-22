package kstarxin.ir.asmir.ASMLevelIRInstruction;

import kstarxin.ir.asmir.*;
import kstarxin.utilities.OperatorTranslator.NASMInstructionOperator;

public class ASMConditionalJumpInstruction extends ASMInstruction {
    public NASMInstructionOperator  op;
    public ASMBasicBlock            trueTarget;
    public ASMBasicBlock            falseTarget;
    public ASMConditionalJumpInstruction(NASMInstructionOperator _op, ASMBasicBlock bb, ASMBasicBlock _t, ASMBasicBlock _f){
        super(_op.toString(), bb);
        op = _op;
        trueTarget = _t;
        falseTarget = _f;
    }

    @Override
    public <T> T accept(ASMLevelIRVisitor<T> asmVisitor) {
        return asmVisitor.visit(this);
    }
}
