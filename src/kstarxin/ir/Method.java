package kstarxin.ir;

import kstarxin.ir.superblock.ConditionSuperBlock;
import kstarxin.ir.superblock.LoopSuperBlock;

import java.util.*;

public class Method {
    public int tmpRegisterCounter;
    private BasicBlock startBlock;
    private BasicBlock endBlock;
    private List<LoopSuperBlock> loops;
    private List<ConditionSuperBlock> conditions;
    private List<BasicBlock> basicBlocks;
    public String hintName;
    public Method(String _hintName){
        hintName = _hintName;
        startBlock = null;
        endBlock = null;
        loops = new LinkedList<LoopSuperBlock>();
        conditions= new LinkedList<ConditionSuperBlock>();
        basicBlocks = new LinkedList<BasicBlock>();
    }


    public void setStartBlock(BasicBlock _startBlock){
        startBlock = _startBlock;
    }

    public void setEndBlock(BasicBlock _endBlock){
        endBlock = _endBlock;
    }

    public void addBasicBlock(BasicBlock bb){
        basicBlocks.add(bb);
    }

}
