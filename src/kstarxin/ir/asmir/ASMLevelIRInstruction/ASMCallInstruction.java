package kstarxin.ir.asmir.ASMLevelIRInstruction;

import kstarxin.ir.asmir.*;

public class ASMCallInstruction extends ASMInstruction {
    public ASMLevelIRMethod callee;
    public ASMCallInstruction(ASMBasicBlock bb, ASMLevelIRMethod _callee){
        super("CALL", bb);
        callee = _callee;
    }

    @Override
    public <T> T accept(ASMLevelIRVisitor<T> asmVisitor) {
        return asmVisitor.visit(this);
    }

    @Override
    public void collectInfo() {
        def.clear();
        use.clear();
    }
}
