package kstarxin.ir.asmir;

import kstarxin.ir.asmir.ASMLevelIRInstruction.ASMInstruction;
import kstarxin.ir.asmir.ASMLevelIRMethod;
import kstarxin.utilities.NameMangler;

import java.util.*;

public class ASMBasicBlock {
    public ASMLevelIRMethod methodBelongTo;
    public String nasmLabel;
    public LinkedList<ASMInstruction> insts;
    public ASMBasicBlock(ASMLevelIRMethod _m, String _label){
        methodBelongTo  = _m;
        insts           = new LinkedList<ASMInstruction>();
        if(_label != null) nasmLabel = NameMangler.convertToASMName(_label);
        else nasmLabel = null;
    }

    public void insertEnd(ASMInstruction _inst){
        insts.addLast(_inst);
    }
}
