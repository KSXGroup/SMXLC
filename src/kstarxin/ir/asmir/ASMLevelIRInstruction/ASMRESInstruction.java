package kstarxin.ir.asmir.ASMLevelIRInstruction;

import kstarxin.ir.asmir.ASMLevelIRVisitor;
import kstarxin.utilities.NameMangler;

public class ASMRESInstruction extends ASMInstruction {
    public int size;
    public String nasmName;
    public ASMRESInstruction(String _name, int _size){
        super(_name, null);
        size = _size;
        nasmName = NameMangler.convertToASMName(_name);
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
