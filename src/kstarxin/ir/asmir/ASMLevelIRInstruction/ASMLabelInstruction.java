package kstarxin.ir.asmir.ASMLevelIRInstruction;

import kstarxin.ir.asmir.ASMBasicBlock;
import kstarxin.ir.asmir.ASMLevelIRMethod;
import kstarxin.ir.asmir.ASMLevelIRVisitor;
import kstarxin.utilities.NameMangler;

public class ASMLabelInstruction extends ASMInstruction {
    public String nasmName;
    public ASMLabelInstruction(String _labelName, ASMBasicBlock bb){
        super(_labelName, bb);
        nasmName = NameMangler.convertToASMName(_labelName);
    }

    @Override
    public <T> T accept(ASMLevelIRVisitor<T> asmVisitor) {
        return asmVisitor.visit(this);
    }
}
