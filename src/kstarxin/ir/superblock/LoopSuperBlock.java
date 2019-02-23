package kstarxin.ir.superblock;

import kstarxin.ir.BasicBlock;
import kstarxin.ir.Method;

import java.util.LinkedList;

public class LoopSuperBlock extends SuperBlock {
    public BasicBlock condBB;
    public BasicBlock stepBB;
    public BasicBlock bodyBBStart;
    public LoopSuperBlock(Method _methodBelongTo){
        _methodBelongTo.loops.add(this);
    }
}
