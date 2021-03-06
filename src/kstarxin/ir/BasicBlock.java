package kstarxin.ir;

import kstarxin.ir.instruction.*;
import kstarxin.ir.operand.VirtualRegister;
import kstarxin.ir.superblock.*;

import java.util.*;

public class BasicBlock {
    public  SuperBlock                  superBlockBelongTo;
    public  LinkedHashSet<BasicBlock>   pred;
    public  LinkedHashSet<BasicBlock>   succ;
    public  HashSet<VirtualRegister>    UEVAR;
    public  HashSet<VirtualRegister>    VARKILL;
    public  HashSet<VirtualRegister>    liveOut;
    public  String                      blockLabel;
    public  int                         dfsOrder;

    private boolean                     isRemoved;
    private Instruction                 beginInst;
    private Instruction                 endInst;
    private int                         size;

    public BasicBlock(Method _methodBelongto, SuperBlock _superBlockBelongTo, BasicBlock _pred, String _blockLabel){
       // _methodBelongto.addBasicBlock(this);
        blockLabel          = _blockLabel;
        superBlockBelongTo  = _superBlockBelongTo;
        beginInst           = null;
        endInst             = null;
        pred                = new LinkedHashSet<BasicBlock>();
        succ                = new LinkedHashSet<BasicBlock>();
        UEVAR               = new HashSet<VirtualRegister>();
        VARKILL             = new HashSet<VirtualRegister>();
        liveOut             = new HashSet<VirtualRegister>();
        isRemoved           = false;
        dfsOrder            = -1;

        if(_pred != null) {
            pred.add(_pred);
            _pred.succ.add(this);
        }
        size = 0;
    }

    public void insertEnd(Instruction inst){
        if(beginInst == null){
            beginInst = inst;
            endInst = inst;
            inst.prev = null;
            inst.next = null;
        }else {
            inst.prev = endInst;
            endInst.next = inst;
            endInst = inst;
        }
        inst.basicBlockBelongTo = this;
        ++size;
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
            _succ.pred.remove(this);
        }
    }

    public void removePred(BasicBlock _pred){
        if(_pred != null) {
            pred.remove(_pred);
            _pred.succ.remove(this);
        }
    }

    public int size(){
        return size;
    }

    public int increaseSize(){
        return ++size;
    }

    public int decreaseSize(){
        return --size;
    }

    public Instruction getEndInst(){
        return endInst;
    }

    public Instruction getBeginInst(){
        return beginInst;
    }

    public void setBeginInst(Instruction inst){
        beginInst = inst;
    }

    public void setEndInst(Instruction inst){
        endInst = inst;
    }

    public void setSize(int _size){
        size = _size;
    }

    public void setRemoved(){
        isRemoved = true;
    }

    public boolean isRemoved(){
        return  isRemoved;
    }

    public BasicBlock copy(){
        BasicBlock ret = new BasicBlock(null, superBlockBelongTo, null, blockLabel);
        for(Instruction i = beginInst; i != null; i = i.next){
            ret.insertEnd(i.copy());
        }
        pred.forEach(p->ret.pred.add(p));
        succ.forEach(s-> ret.succ.add(s));
        ret.superBlockBelongTo = superBlockBelongTo;
        return ret;
    }

    public void collectDefUseInfo(){
        UEVAR.clear();
        VARKILL.clear();
        for(Instruction inst = beginInst; inst != null; inst = inst.next){
            inst.collectDefUseInfo();
            inst.use.forEach(op->{
                if(!VARKILL.contains(op)) UEVAR.add(op);
            });
            VARKILL.addAll(inst.def);
        }
    }
}
