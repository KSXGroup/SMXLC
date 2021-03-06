package kstarxin.ir.asmir.ASMLevelIRInstruction;

import kstarxin.ir.asmir.ASMBasicBlock;
import kstarxin.ir.asmir.ASMLevelIRMethod;
import kstarxin.ir.asmir.ASMLevelIRVisitor;
import kstarxin.ir.operand.VirtualRegister;
import kstarxin.utilities.OperatorTranslator.NASMInstructionOperator;

import java.util.HashMap;

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

    @Override
    public void collectInfo() {
        def.clear();
        use.clear();
        def.add(dst);
    }

    @Override
    public void replaceOperandForSpill(HashMap<VirtualRegister, VirtualRegister> map) {
        if(dst instanceof VirtualRegister && map.containsKey(dst)) dst = map.get(dst);
    }
}
