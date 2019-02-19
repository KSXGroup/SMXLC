package kstarxin.ir;

import kstarxin.ir.instruction.*;
import kstarxin.ir.superblock.*;

public class BasicBlock {
    public SuperBlock superBlockBelongTo;
    private Instruction beginInst;
    private Instruction endInst;

    public BasicBlock(SuperBlock _superBlockBelongTo){
        superBlockBelongTo = _superBlockBelongTo;
    }

    public void insertEnd(Instruction inst){
        endInst.next = inst;
        endInst = inst;
    }
}
