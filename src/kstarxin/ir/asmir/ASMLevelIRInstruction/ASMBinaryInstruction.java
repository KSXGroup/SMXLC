package kstarxin.ir.asmir.ASMLevelIRInstruction;

import kstarxin.ir.asmir.ASMBasicBlock;
import kstarxin.ir.asmir.ASMLevelIRMethod;
import kstarxin.ir.asmir.ASMLevelIRVisitor;
import kstarxin.ir.operand.Memory;
import kstarxin.ir.operand.Operand;
import kstarxin.ir.operand.VirtualRegister;
import kstarxin.utilities.OperatorTranslator.NASMInstructionOperator;

import java.util.HashMap;

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

    @Override
    public void collectInfo() {
        def.clear();
        use.clear();
        if(src instanceof VirtualRegister) use.add((VirtualRegister) src);
        else if(src instanceof Memory) use.addAll(((Memory) src).collectUseInfo());
        if(dst instanceof VirtualRegister){
            use.add((VirtualRegister) dst);
            def.add((VirtualRegister) dst);
        }else if(dst instanceof Memory) use.addAll(((Memory) dst).collectUseInfo());
    }

    @Override
    public void replaceOperandForSpill(HashMap<VirtualRegister, VirtualRegister> map) {
        if(src instanceof VirtualRegister && map.containsKey(src)) src = map.get(src);
        if(dst instanceof VirtualRegister && map.containsKey(dst)) dst = map.get(dst);
        if(src instanceof Memory) ((Memory) src).replaceForSpill(map);
        if(dst instanceof Memory) ((Memory) dst).replaceForSpill(map);
    }
}
