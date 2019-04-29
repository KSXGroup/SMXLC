package kstarxin.ir.asmir.ASMLevelIRInstruction;

import kstarxin.ir.asmir.ASMLevelIRVisitor;
import kstarxin.ir.operand.VirtualRegister;
import kstarxin.utilities.NameMangler;

import java.util.HashMap;

public class ASMDInstruction extends ASMInstruction {
    public int value;
    public String nasmName;
    public ASMDInstruction(String _name, int _value){
        super(_name,null);
        nasmName = NameMangler.convertToASMName(_name);
        value = _value;
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
