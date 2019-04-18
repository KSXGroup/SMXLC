package kstarxin.ir.asmir;

import kstarxin.ir.asmir.ASMLevelIRInstruction.ASMInstruction;
import kstarxin.ir.asmir.ASMLevelIRMethod;

import java.util.*;

public class ASMBasicBlock {
    public ASMLevelIRMethod methodBelongTo;
    public LinkedList<ASMInstruction> insts;
    public ASMBasicBlock(ASMLevelIRMethod _m){
        methodBelongTo  = _m;
        insts           = new LinkedList<ASMInstruction>();
    }

    public void insertEnd(ASMInstruction _inst){
        insts.addLast(_inst);
    }
}
