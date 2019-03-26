package kstarxin.ir.instruction;

import kstarxin.ir.BasicBlock;

public class Instruction {
    public String hintName;
    public Instruction next;
    public Instruction prev;
    public BasicBlock basicBlockBelongTo;
    public Instruction(String _hintName){
        hintName = _hintName;
    }

    public void insertInstAfterThis(Instruction inst){
        Instruction tnext = next;
        next        = inst;
        inst.prev   = this;
        inst.next   = tnext;
        if(tnext != null) tnext.prev = inst;
        else basicBlockBelongTo.setEndInst(inst);
        inst.basicBlockBelongTo = basicBlockBelongTo;
        basicBlockBelongTo.increaseSize();
    }

    public void replaceThisWith(Instruction inst){
        inst.prev = prev;
        inst.next = next;
        if(prev != null) prev.next = inst;
        else basicBlockBelongTo.setBeginInst(inst);
        if(next != null) next.prev = inst;
        else basicBlockBelongTo.setEndInst(inst);
        inst.basicBlockBelongTo = basicBlockBelongTo;
    }

    public void removeThisInst(){
        if(prev != null) prev.next = next;
        else basicBlockBelongTo.setBeginInst(next);
        if(next != null) next.prev = prev;
        else basicBlockBelongTo.setEndInst(prev);
        prev = null;
        next = null;
        basicBlockBelongTo.decreaseSize();
        basicBlockBelongTo = null;
    }
}
