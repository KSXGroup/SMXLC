package kstarxin.ir.asmir.ASMLevelIRInstruction;

import kstarxin.ir.asmir.ASMLevelIRVisitor;
import kstarxin.ir.operand.VirtualRegister;
import kstarxin.utilities.NameMangler;

import java.util.HashMap;

public class ASMRomDataInstruction extends ASMInstruction {
    public String content;
    public String nasmName;
    public ASMRomDataInstruction(String _name, String _content){
        super(_name, null);
        nasmName = NameMangler.convertToASMName(_name);
        content = _content;
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

    @Override
    public void replaceOperandForSpill(HashMap<VirtualRegister, VirtualRegister> map) {
        return;
    }
}
