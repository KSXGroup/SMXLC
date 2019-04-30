package kstarxin.ir.asmir.ASMLevelIRInstruction;

import kstarxin.ir.asmir.*;
import kstarxin.ir.operand.VirtualRegister;

import java.util.HashMap;

public class ASMCallInstruction extends ASMInstruction {
    public ASMLevelIRMethod callee;
    public VirtualRegister ret;
    public ASMCallInstruction(ASMBasicBlock bb, ASMLevelIRMethod _callee, VirtualRegister _ret){
        super("CALL", bb);
        callee = _callee;
        ret = _ret;
    }

    @Override
    public <T> T accept(ASMLevelIRVisitor<T> asmVisitor) {
        return asmVisitor.visit(this);
    }

    @Override
    public void collectInfo() {
        def.clear();
        use.clear();
        def.addAll(basicBlockBelongTo.methodBelongTo.virtualCallerSavedRegister);
        use.addAll(basicBlockBelongTo.methodBelongTo.virtualParameterPassingRegister);
    }

    @Override
    public void replaceOperandForSpill(HashMap<VirtualRegister, VirtualRegister> map) {
        return;
    }
}
