package kstarxin.optimization;

import kstarxin.ir.*;
import kstarxin.ir.instruction.*;
import kstarxin.ir.operand.*;
import kstarxin.ir.superblock.LoopSuperBlock;
import kstarxin.utilities.OperatorTranslator;
//eliminate single jump block and perform naive simplification
//TODO: eliminate direct jump only basic block and merge blocks
//TODO: eliminate CMP & JEQ pair

public class CFGSimplifier {
    IRProgram ir;
    private int blockMergedCount = 0;

    public CFGSimplifier(IRProgram _ir){
        ir = _ir;
    }

    public void mergeInstructions(){
        ir.getMethodMap().values().forEach(method -> {
            if(!method.isBuiltin) {
                method.bfs();
                for (BasicBlock bb : method.basicBlockInBFSOrder) {
                    Instruction end = bb.getEndInst();
                    Instruction p = end.prev;
                    Instruction pp = null;
                    if(p == null) continue;
                    else pp = p.prev;
                    if(end instanceof ConditionJumpInstruction && p instanceof CompareInstruction && pp instanceof BinaryArithmeticInstruction && OperatorTranslator.isCompareOperator(((BinaryArithmeticInstruction) pp).op)){
                        //System.err.println(end.toString());
                        int operator = ((BinaryArithmeticInstruction)pp).op;
                        Operand lhs = ((BinaryArithmeticInstruction) pp).lhs;
                        Operand rhs = ((BinaryArithmeticInstruction) pp).rhs;
                        pp.removeThisInst();
                        pp = null;
                        ((CompareInstruction) p).replaceLhs(lhs);
                        ((CompareInstruction) p).replaceRhs(rhs);
                        ((ConditionJumpInstruction) end).replaceOperatorWith(operator);
                    }
                }
                method.basicBlockInBFSOrder.clear();
            }
        });
    }

    // merge b to a
    private void doMergeBlock(BasicBlock a, BasicBlock b){
        if(!(a.getEndInst() instanceof DirectJumpInstruction && ((DirectJumpInstruction) a.getEndInst()).target == b)) throw new RuntimeException("merge shit bb!!!");
        int finalSize = a.size() + b.size() - 1; //update size;
        Instruction bbegin = b.getBeginInst();
        Instruction aend = a.getEndInst();
        aend.next = bbegin;
        bbegin.prev = aend;
        a.setEndInst(b.getEndInst()); //update endInst
        aend.removeThisInst();
        a.setSize(finalSize);
        for(Instruction inst = bbegin; inst != null; inst = inst.next) inst.basicBlockBelongTo = a;

        //update loopsueprblock
        if(b.superBlockBelongTo != null && b.superBlockBelongTo instanceof LoopSuperBlock){
            if(((LoopSuperBlock) b.superBlockBelongTo).stepBB == b) ((LoopSuperBlock) b.superBlockBelongTo).stepBB = null; //stepBB can always be merged
            else if(((LoopSuperBlock) b.superBlockBelongTo).condBB == b) throw new RuntimeException("why cond bb only have one prev!"); //cond BB always have two or more pred, can not be merged
            else if(((LoopSuperBlock) b.superBlockBelongTo).bodyBBStart == b){
                if(a != ((LoopSuperBlock) b.superBlockBelongTo).condBB) throw new RuntimeException("merge non cond block to body ????"); //can only merge condBB to body
                ((LoopSuperBlock) b.superBlockBelongTo).bodyBBStart = a;
                ((LoopSuperBlock) b.superBlockBelongTo).condBB = null;
            }
        }

        b.setRemoved();
    }

    public void mergeBlocks(){
        blockMergedCount = 0;
        ir.getMethodMap().values().forEach(method -> {
            if(!method.isBuiltin){
                method.bfs();
                for (BasicBlock bb : method.basicBlockInBFSOrder) {
                    if(bb.isRemoved()) continue;
                    boolean sflag = false;
                    BasicBlock dsucc = null;
                    if(bb.succ.size() == 1){
                        for (BasicBlock bbsucc : bb.succ) {
                            sflag = bbsucc.pred.size() == 1;
                            dsucc = bbsucc;
                        }

                        //merge block if they are directly connected

                        if(dsucc != bb && sflag){
                            doMergeBlock(bb, dsucc);
                            bb.succ = dsucc.succ; //set the merger's succ as mergee's succ
                            for (BasicBlock succbb : dsucc.succ) {
                                //for every mergee's succ, update it's pred
                                succbb.pred.remove(dsucc);
                                succbb.pred.add(bb);
                            }
                            //remove from bfs seqence
                            blockMergedCount++;
                        }
                    }
                }
                method.basicBlockInBFSOrder.clear();
            }
        });
        return ;
    }

    public void run(){
        mergeInstructions();
        do mergeBlocks();
        while(blockMergedCount > 0);
    }
}
