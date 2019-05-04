package kstarxin.ir.asmir.ASMLevelIRInstruction;

import kstarxin.ir.asmir.ASMBasicBlock;
import kstarxin.ir.asmir.ASMLevelIRMethod;
import kstarxin.ir.asmir.ASMLevelIRVisitor;
import kstarxin.ir.operand.Memory;
import kstarxin.ir.operand.Operand;
import kstarxin.ir.operand.VirtualRegister;
import kstarxin.utilities.OperatorTranslator.NASMInstructionOperator;

import java.util.HashMap;

public class ASMUnaryInstruction extends ASMInstruction {
    public NASMInstructionOperator operator;
    public Operand src;

    //for div
    private VirtualRegister vrax;
    private VirtualRegister vrdx;

    public ASMUnaryInstruction(NASMInstructionOperator op, ASMBasicBlock bb, Operand _src){
        super(op.toString(), bb);
        src = _src;
        operator = op;
        vrax = null;
        vrdx = null;
    }

    //for div
    public ASMUnaryInstruction(NASMInstructionOperator op, ASMBasicBlock bb, Operand _src, VirtualRegister _vrax, VirtualRegister _vrdx){
        super(op.toString(), bb);
        if(!op.equals(NASMInstructionOperator.IDIV)) throw new RuntimeException();
        src = _src;
        operator = op;
        vrax = _vrax;
        vrdx = _vrdx;
    }

    @Override
    public <T> T accept(ASMLevelIRVisitor<T> asmVisitor) {
        return asmVisitor.visit(this);
    }

    @Override
    public void collectInfo() {
        def.clear();
        use.clear();
        if(operator.equals(NASMInstructionOperator.IDIV)){
            def.add(vrax);
            def.add(vrdx);
            use.add(vrdx);
            use.add(vrax);
            if(src instanceof VirtualRegister) use.add((VirtualRegister) src);
            else if(src instanceof Memory) use.addAll(((Memory) src).collectUseInfo());
            else throw new RuntimeException();
        } else {
            if (src instanceof VirtualRegister) {
                def.add((VirtualRegister) src);
                use.add((VirtualRegister) src);
            } else if (src instanceof Memory) use.addAll(((Memory) src).collectUseInfo());
        }
    }

    @Override
    public void replaceOperandForSpill(HashMap<VirtualRegister, VirtualRegister> map) {
        if(src instanceof VirtualRegister && map.containsKey(src)) src = map.get(src);
        if(src instanceof Memory){
            src = new Memory((Memory)src);
            ((Memory) src).replaceForSpill(map);
        }
    }
}
