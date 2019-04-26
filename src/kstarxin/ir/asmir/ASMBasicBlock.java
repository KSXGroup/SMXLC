package kstarxin.ir.asmir;

import kstarxin.ir.asmir.ASMLevelIRInstruction.ASMInstruction;
import kstarxin.ir.asmir.ASMLevelIRMethod;
import kstarxin.ir.operand.VirtualRegister;
import kstarxin.utilities.NameMangler;

import java.util.*;

public class ASMBasicBlock {
    public ASMLevelIRMethod methodBelongTo;
    public String nasmLabel;
    public LinkedList<ASMInstruction> insts;
    public HashSet<VirtualRegister> UEVAR;
    public HashSet<VirtualRegister> VARKILL;
    public HashSet<VirtualRegister> liveOut;

    public ASMBasicBlock(ASMLevelIRMethod _m, String _label){
        methodBelongTo  = _m;
        insts           = new LinkedList<ASMInstruction>();
        if(_label != null) nasmLabel = NameMangler.convertToASMName(_label);
        else nasmLabel  = null;
        UEVAR           = new HashSet<VirtualRegister>();
        VARKILL         = new HashSet<VirtualRegister>();
        liveOut         = new HashSet<VirtualRegister>();
    }

    public void insertEnd(ASMInstruction _inst){
        insts.addLast(_inst);
    }

    public void collectInfo(){
        for(ASMInstruction inst : insts){
            inst.collectInfo();
            UEVAR.addAll(inst.use);
            VARKILL.addAll(inst.def);
        }
    }
}
