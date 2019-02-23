package kstarxin.ir;

import kstarxin.ir.instruction.*;
import kstarxin.ir.superblock.*;

import java.util.HashSet;

public class BasicBlock {
    public SuperBlock superBlockBelongTo;
    public HashSet<BasicBlock> pred;
    public HashSet<BasicBlock> succ;
    public String blockLabel;
    private Instruction beginInst;
    private Instruction endInst;
    private int size;

    public BasicBlock(Method _methodBelongto, SuperBlock _superBlockBelongTo, BasicBlock _pred, String _blockLabel){
        _methodBelongto.addBasicBlock(this);
        blockLabel = _blockLabel;
        superBlockBelongTo = _superBlockBelongTo;
        beginInst = null;
        endInst = null;
        pred = new HashSet<BasicBlock>();
        succ = new HashSet<BasicBlock>();
        if(_pred != null) {
            pred.add(_pred);
            _pred.pred.add(this);
        }
        size = 0;
    }

    public void insertEnd(Instruction inst){
        if(beginInst == null){
            beginInst = inst;
            endInst = inst;
        }else {
            endInst.next = inst;
            endInst = inst;
        }
        inst.basicBlockBelongTo = this;
    }

    public void addPred(BasicBlock _pred){
        if(_pred != null) {
            pred.add(_pred);
            _pred.succ.add(this);
        }
    }

    public void addSucc(BasicBlock _succ){
        if(_succ != null) {
            succ.add(_succ);
            _succ.pred.add(this);
        }
    }

    public void removeSucc(BasicBlock _succ){
        if(_succ != null) {
            succ.remove(_succ);
            _succ.removePred(this);
        }
    }

    public void removePred(BasicBlock _pred){
        if(_pred != null) {
            pred.remove(_pred);
            _pred.removeSucc(this);
        }
    }

    public int size(){
        return size;
    }

}
