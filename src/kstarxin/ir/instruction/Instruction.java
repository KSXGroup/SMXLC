package kstarxin.ir.instruction;

import kstarxin.ir.BasicBlock;
import kstarxin.ir.operand.Address;
import kstarxin.ir.operand.Operand;
import kstarxin.ir.operand.VirtualRegister;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public abstract class Instruction {
    public String hintName;
    public Instruction next;
    public Instruction prev;
    public BasicBlock basicBlockBelongTo;
    public HashSet<Operand> def;
    public HashSet<Operand> use;
    public HashSet<Operand> liveIn;
    public HashSet<Operand> liveOut;
    public Instruction(String _hintName){
        hintName    = _hintName;
        def         = new HashSet<Operand>();
        use         = new HashSet<Operand>();
        liveIn      = new HashSet<Operand>();
        liveOut     = new HashSet<Operand>();
    }

    public void insertBeforeThis(Instruction inst){
        Instruction tprev = prev;
        prev = inst;
        inst.next = this;
        inst.prev = tprev;
        if(tprev != null) tprev.next = inst;
        else basicBlockBelongTo.setBeginInst(inst);
        inst.basicBlockBelongTo = basicBlockBelongTo;
        basicBlockBelongTo.increaseSize();
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

    public abstract Instruction copy();

    public abstract void replaceOperandForInline(HashMap<Operand, Operand> map);

    public abstract void collectDefUseInfo();

    public abstract Address replaceOperandForGlobalVariableOptimization(HashMap<Address, VirtualRegister> map);
}
