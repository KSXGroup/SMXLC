package kstarxin.ir.asmir.ASMLevelIRInstruction;

import kstarxin.ir.asmir.ASMBasicBlock;
import kstarxin.ir.asmir.ASMLevelIRMethod;
import kstarxin.ir.asmir.ASMLevelIRVisitor;
import kstarxin.ir.operand.Operand;

public class ASMLEAInstruction extends ASMInstruction {
    public Operand dst;
    public Operand src;
    public ASMLEAInstruction(ASMBasicBlock bb,Operand _dst, Operand _src){
        super("LEA", bb);
        dst = _dst;
        src = _src;
    }

    @Override
    public <T> T accept(ASMLevelIRVisitor<T> asmVisitor) {
        return asmVisitor.visit(this);
    }
}
