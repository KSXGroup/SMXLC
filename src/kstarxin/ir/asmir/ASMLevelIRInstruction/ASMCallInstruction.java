package kstarxin.ir.asmir.ASMLevelIRInstruction;

import kstarxin.ir.asmir.*;

public class ASMCallInstruction extends ASMInstruction {
    public ASMCallInstruction(ASMBasicBlock bb){
        super("CALL", bb);
    }

    @Override
    public <T> T accept(ASMLevelIRVisitor<T> asmVisitor) {
        return asmVisitor.visit(this);
    }
}
