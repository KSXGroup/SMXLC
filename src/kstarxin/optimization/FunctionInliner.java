package kstarxin.optimization;

import kstarxin.compiler.Configure;
import kstarxin.ir.*;
import kstarxin.ir.instruction.*;
import kstarxin.ir.operand.*;
import kstarxin.ir.superblock.*;
import kstarxin.utilities.*;
import java.util.*;

public class FunctionInliner {

    class copiedMethodInfo{
        BasicBlock startBB;
        BasicBlock endBB;
        LinkedList<LoopSuperBlock> loops;
        public copiedMethodInfo(BasicBlock _startBB, BasicBlock _endBB, LinkedList<LoopSuperBlock> _loops){
            startBB = _startBB;
            endBB   = _endBB;
            loops   = _loops;
        }
    }


    private int level;
    private int inlineCounter = 0;
    private IRProgram ir;


    //function return the start bb and end bb pair
    private copiedMethodInfo copyAndReplace(Method m, Method into, CallInstruction callInst){
        m.basicBlockInBFSOrder.clear();
        m.bfs();
        ReturnInstruction ret = null;
        //direct copy old one and map old one to new one
        HashMap<BasicBlock, BasicBlock> basicBlockReplaceMap = new HashMap<BasicBlock, BasicBlock>();
        m.basicBlockInBFSOrder.forEach(bb->{
            BasicBlock copiedBB = bb.copy();
            if(copiedBB.blockLabel != null) copiedBB.blockLabel += (NameMangler.inlineSuffix + inlineCounter);
            basicBlockReplaceMap.put(bb, copiedBB);
        });

        //the function copied may contain loop, record these info will do good to loop-related optimization if i have time to do that
        HashMap<LoopSuperBlock, LoopSuperBlock> superBlockMap = new HashMap<LoopSuperBlock, LoopSuperBlock>();
        LinkedList<LoopSuperBlock> superBlocks = new LinkedList<LoopSuperBlock>();
        m.loops.forEach(loopSuperBlock -> {
            LoopSuperBlock newLoop = new LoopSuperBlock(loopSuperBlock);
            if(newLoop.condBB != null) newLoop.condBB = basicBlockReplaceMap.get(newLoop.condBB);
            if(newLoop.stepBB != null) newLoop.stepBB = basicBlockReplaceMap.get(newLoop.stepBB);
            if(newLoop.bodyBBStart != null) newLoop.bodyBBStart = basicBlockReplaceMap.get(newLoop.bodyBBStart);
            superBlockMap.put(loopSuperBlock,newLoop);
            superBlocks.add(newLoop);
        });

        //map superbb and bb
        for (BasicBlock bb : basicBlockReplaceMap.values()) {
            if(bb.superBlockBelongTo != null) {
                LoopSuperBlock lsb = superBlockMap.get(bb.superBlockBelongTo);
                if(lsb == null) throw new RuntimeException("impossible things happened on lsb!!!");
            }

            Instruction endInst = bb.getEndInst();
            if(endInst instanceof DirectJumpInstruction) ((DirectJumpInstruction) endInst).target = basicBlockReplaceMap.get(((DirectJumpInstruction) endInst).target);
            else if(endInst instanceof ConditionJumpInstruction){
                ConditionJumpInstruction cdj = (ConditionJumpInstruction) endInst;
                cdj.replaceTrueTargetWith(basicBlockReplaceMap.get(cdj.trueTarget));
                cdj.replaceFalseTargetWith(basicBlockReplaceMap.get(cdj.falseTarget));
            } else if(endInst instanceof ReturnInstruction) ret = (ReturnInstruction) endInst;
            else throw new RuntimeException("bb should end with jmp or ret");
        }

        //replace operands:
        //for parameters, replace it with corresponding call parameter
        //for local variables, reallocate vreg and add name suffix "#inline[count]" and add to "into" method's local variable
        //for tmp vreg, reallocate new tmp vreg
        //for thisPointer, replace it with the second para of the call inst
        //for return inst, replace it with a move and map the return reg to a tmp reg
        //add all global variables into replaceMap for sake of convenience

        HashMap<Operand, Operand> operandMapper = new HashMap<Operand, Operand>();
        ir.getGlobalVariableMap().values().forEach(gv -> operandMapper.put(gv, gv)); //self map
        //map class this pointer if exist
        if(callInst.classThisPointer != null)
            if(m.classThisPointer == null) throw new RuntimeException("this pointer not match!!!");
            else operandMapper.put(m.classThisPointer, callInst.classThisPointer);
        //map return reg
        operandMapper.put(m.returnRegister, into.allocateNewTmpRegister());
        //map function parameter(s)
        for(int i = 0; i < m.parameters.size(); ++i) operandMapper.put(m.parameters.get(i), callInst.parameters.get(i));

        //map local variables
        m.localVariables.values().forEach(local->{
            if(!operandMapper.containsKey(local)) operandMapper.put(local, new VirtualRegister(local.hintName, local.mangledName + NameMangler.inlineSuffix + inlineCounter));
        });
        //allocate new tmp vregs
        m.tmpLocalRegisters.values().forEach(tmpreg ->{operandMapper.put(tmpreg, into.allocateNewTmpRegister());});
        //now all variables are mapped

        //traverse and replace
        basicBlockReplaceMap.values().forEach(bb->{
            for(Instruction i = bb.getBeginInst(); i != null; i = i.next) i.replaceOperandForInline(operandMapper);
        });

        //replace the return inst with move inst
        BasicBlock newEndBB = basicBlockReplaceMap.get(m.endBlock);
        ReturnInstruction retInst = (ReturnInstruction) newEndBB.getEndInst();
        Operand regVal = null;
        if(retInst.returnValue instanceof Address) retInst.insertBeforeThis(new LoadInstruction(into.allocateNewTmpRegister(), (Address) retInst.returnValue));
        else if(retInst.returnValue instanceof Immediate || retInst.returnValue instanceof VirtualRegister){
            regVal = retInst.returnValue;
        }
        if(regVal instanceof VirtualRegister) retInst.replaceThisWith(new MoveInstruction(callInst.returnValue, (VirtualRegister) regVal));
        else  retInst.replaceThisWith(new MoveInstruction(callInst.returnValue, (Immediate) regVal));

        return new copiedMethodInfo(basicBlockReplaceMap.get(m.startBlock), basicBlockReplaceMap.get(m.endBlock),superBlocks);
    }

    private void collectCallInfo(){
        ir.getMethodMap().values().forEach(method -> {
            if(!method.isBuiltin){
                method.basicBlockInBFSOrder.clear();
                method.bfs();
                int instCnt = 0;
                for (BasicBlock bb : method.basicBlockInBFSOrder) {
                    for(Instruction inst = bb.getBeginInst(); inst != null; inst = inst.next) {
                        instCnt++;
                        if (inst instanceof CallInstruction){
                            if(((CallInstruction) inst).callee == method) method.recursiveMethodCall.add((CallInstruction) inst);
                            else method.nonRecursiveMethodCall.add((CallInstruction) inst);
                        }
                    }
                }
                if(instCnt < Configure.INLINE_THRESHOULD && !method.hintName.equals(NameMangler.mainMethodName)) method.canBeInlined = true;
                else method.canBeInlined = false;
            }
        });
    }


    private void inlineFunction(CallInstruction call, Method into){
        inlineCounter++;
        copiedMethodInfo copy = copyAndReplace(call.callee, into, call);
        BasicBlock bb = call.basicBlockBelongTo;
        BasicBlock newBB = new BasicBlock(into, bb.superBlockBelongTo, null, null);

        //move inst after call to new BB
        Instruction nextToCall = call.next;
        Instruction originBBEndInst = bb.getEndInst();
        call.next = null;
        nextToCall.prev = null;
        for(Instruction i = nextToCall; i != null; i = i.next) i.basicBlockBelongTo = newBB;
        newBB.setEndInst(originBBEndInst);

        //merge start bb and end bb of copy into them
        Instruction copyStartBegin = copy.startBB.getBeginInst();
        Instruction copyStartEnd = copy.startBB.getEndInst();
        Instruction copyEndStart = copy.endBB.getBeginInst();
        Instruction copyEndEnd = copy.endBB.getEndInst();

        //clear copy bb
        copy.startBB.setEndInst(null);
        copy.startBB.setBeginInst(null);
        copy.startBB.setSize(0);
        //connect with old bb
        copyStartBegin.prev = call;
        call.next = copyStartBegin;
        int instCnt = 0;
        for(Instruction i = bb.getBeginInst(); i != null; i = i.next){
            i.basicBlockBelongTo = bb;
            instCnt++;
        }
        bb.setSize(instCnt);
        LinkedHashSet<BasicBlock> oldBBsucc = bb.succ;
        bb.succ = copy.startBB.succ;
        bb.setEndInst(copyStartEnd);
        call.removeThisInst();//remove call

        copyEndEnd.next = nextToCall;
        nextToCall.prev = copyEndEnd;
        newBB.succ = oldBBsucc;
        newBB.pred = copy.endBB.pred;
        newBB.setBeginInst(copyEndStart);
        instCnt = 0;
        for(Instruction i = newBB.getBeginInst(); i != null; i = i.next){
            i.basicBlockBelongTo = newBB;
            instCnt++;
        }
        newBB.setSize(instCnt);

        into.canBeInlined = false;
        into.basicBlockInBFSOrder.clear();
    }

    public FunctionInliner(IRProgram _ir, int _level){
        ir = _ir;
        level = _level;
    }

    public void run(){
        collectCallInfo();
        ir.getMethodMap().values().forEach(method -> {
            //inline non-recursive call first
            //just do one level inline first
            method.nonRecursiveMethodCall.forEach(call->{
                if(!method.isBuiltin && !method.canBeInlined && call.callee.canBeInlined) inlineFunction(call, method);
            });
        });
    }
}
