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
    private int recursiveInlineLevel;
    private int inlineCounter = 0;
    private IRProgram ir;
    private IRPrinter printer;

    public FunctionInliner(IRProgram _ir, int _level, int _recursiveInlineLevel){
        ir = _ir;
        level = _level;
        printer = new IRPrinter(_ir, System.out);
        recursiveInlineLevel = _recursiveInlineLevel;
    }

    //function return the start bb and end bb pair
    private copiedMethodInfo copyAndReplace(Method m, Method into, CallInstruction callInst){
        m.bfs();
        ReturnInstruction ret = null;
        //direct copy old one and map old one to new one
        HashMap<BasicBlock, BasicBlock> basicBlockReplaceMap = new HashMap<BasicBlock, BasicBlock>();
        m.basicBlockInBFSOrder.forEach(bb->{
            BasicBlock copiedBB = bb.copy();
            //System.err.println("copy" + copiedBB.blockLabel);
            if(copiedBB.blockLabel != null) copiedBB.blockLabel += (NameMangler.inlineSuffix + inlineCounter);
            basicBlockReplaceMap.put(bb, copiedBB);
        });

        //for every bb, update the pred and succ set use mapping
        basicBlockReplaceMap.values().forEach(bb -> {
            LinkedHashSet<BasicBlock> newPred = new LinkedHashSet<BasicBlock>();
            LinkedHashSet<BasicBlock> newSucc = new LinkedHashSet<BasicBlock>();
            bb.succ.forEach(dsucc->{
                BasicBlock tmp = basicBlockReplaceMap.get(dsucc);
                if(tmp == null)
                    throw new RuntimeException();
                newSucc.add(tmp);
            });

            bb.pred.forEach(dpred ->{
                BasicBlock tmp = basicBlockReplaceMap.get(dpred);
                if(tmp == null)
                    throw new RuntimeException();
                newPred.add(tmp);
            });

            bb.succ = newSucc;
            bb.pred = newPred;
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
        if(m != into) {
            m.tmpLocalRegisters.values().forEach(tmpreg -> {
                operandMapper.put(tmpreg, into.allocateNewTmpRegister());
            });
        }else{
            HashMap<String, VirtualRegister> tmpMap = new HashMap<String, VirtualRegister>();
            m.tmpLocalRegisters.values().forEach(tmpreg->{
                VirtualRegister corresReg = into.allocateNewTmpRegisterWithoutPuttingItIntoMap();
                operandMapper.put(tmpreg, corresReg);
                tmpMap.put(tmpreg.hintName, corresReg);
            });
            tmpMap.forEach((k,v)->{
                m.tmpLocalRegisters.put(k, v);
            });
        }
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

        if (regVal instanceof VirtualRegister)
            retInst.replaceThisWith(new MoveInstruction(callInst.returnValue, (VirtualRegister) regVal));
        else retInst.replaceThisWith(new MoveInstruction(callInst.returnValue, (Immediate) regVal));

        return new copiedMethodInfo(basicBlockReplaceMap.get(m.startBlock), basicBlockReplaceMap.get(m.endBlock),superBlocks);
    }

    public void collectCallInfo(){
        ir.getMethodMap().values().forEach(method -> {
            method.recursiveMethodCall.clear();
            method.nonRecursiveMethodCall.clear();
            if(!method.isBuiltin){
                method.basicBlockInBFSOrder.clear();
                method.bfs();
                int instCnt = 0;
                for (BasicBlock bb : method.basicBlockInBFSOrder) {
                    for(Instruction inst = bb.getBeginInst(); inst != null; inst = inst.next) {
                        instCnt++;
                        if(inst instanceof CallInstruction && ((CallInstruction) inst).callee == null)
                            throw new RuntimeException();
                        if (inst instanceof CallInstruction && !((CallInstruction) inst).callee.isBuiltin){
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
        into.globalVariableUsed.addAll(call.callee.globalVariableUsed);
        copy.loops.forEach(loop->into.loops.add(loop));
        BasicBlock bb = call.basicBlockBelongTo;
        BasicBlock newBB = new BasicBlock(into, bb.superBlockBelongTo, null, call.basicBlockBelongTo.blockLabel + NameMangler.splitSuffix + inlineCounter);

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
        copy.startBB.succ.forEach(stbsucc->{
            stbsucc.pred.remove(copy.startBB);
            stbsucc.pred.add(bb);
        });

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

        if(call.returnValue == null) copyEndEnd.removeThisInst(); // if no return value, remove the replaced inst

        if(bb == into.endBlock) into.endBlock = newBB;
        copy.endBB.pred.forEach(edbbpred->{
            Instruction ed = edbbpred.getEndInst();
            if(ed instanceof DirectJumpInstruction){
                if(((DirectJumpInstruction) ed).target == copy.endBB) ((DirectJumpInstruction) ed).target = newBB;
                else throw new RuntimeException();
            }
            else if(ed instanceof ConditionJumpInstruction){
                if(((ConditionJumpInstruction) ed).trueTarget == copy.endBB) ((ConditionJumpInstruction) ed).trueTarget = newBB;
                else if(((ConditionJumpInstruction) ed).falseTarget == copy.endBB) ((ConditionJumpInstruction) ed).falseTarget = newBB;
                else
                    throw new RuntimeException();
            }
            else throw new RuntimeException();
            edbbpred.succ.remove(copy.endBB);
            edbbpred.succ.add(newBB);
        });

        copy.endBB.setEndInst(null);
        copy.endBB.setBeginInst(null);
        copy.endBB.setSize(0);

        into.basicBlockInBFSOrder.clear();
    }

    private void removeUselessMethodFromIR(){
        HashSet<Method> called = new HashSet<Method>();

        collectCallInfo();

        Queue<Method> callQ = new LinkedList<Method>();
        Method main = ir.getMethodMap().get(NameMangler.mainMethodName);
        Method init = ir.getInitMethod();
        ((LinkedList<Method>) callQ).add(main);
        ((LinkedList<Method>) callQ).add(init);
        called.add(main);
        called.add(init);

        while(!callQ.isEmpty()){
            Method m = ((LinkedList<Method>) callQ).pop();
            m.nonRecursiveMethodCall.forEach(nrmc ->{
                if(!called.contains(nrmc.callee)) ((LinkedList<Method>) callQ).add(nrmc.callee);
                called.add(nrmc.callee);
            });
        }

        LinkedList<String> toRemove = new LinkedList<String>();
        ir.getMethodMap().forEach((k, v)->{
            if(!v.isBuiltin && !called.contains(v)) toRemove.add(k);
        });
        toRemove.forEach(item -> ir.getMethodMap().remove(item));
    }


    public void run(){
        //give up this
        while(recursiveInlineLevel > 0){
            collectCallInfo();
            ir.getMethodMap().values().forEach(method -> {
                if(method.hintName.equals("@_Zcdisssi")) {
                    method.recursiveMethodCall.forEach(call -> {
                        if (!method.isBuiltin && call.callee.canBeInlined) {
                            inlineFunction(call, method);
                        }
                    });
                }
            });
            recursiveInlineLevel--;
        }
        if(ir.getMethodMap().size() < 2) return;
        while(level > 0) {
            collectCallInfo();
            ir.getMethodMap().values().forEach(method -> {
                //inline non-recursive call first
                method.nonRecursiveMethodCall.forEach(call -> {
                    if (!method.isBuiltin && !method.canBeInlined && !call.callee.isMemorized && call.callee.canBeInlined) {
                        inlineFunction(call, method);
                        //printer.printMethod(method);
                    }
                });

            });
            level--;
        }
        removeUselessMethodFromIR();
    }
}
