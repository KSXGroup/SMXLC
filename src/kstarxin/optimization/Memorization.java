package kstarxin.optimization;

import kstarxin.compiler.Configure;
import kstarxin.ir.*;
import kstarxin.ir.instruction.*;
import kstarxin.ir.operand.Immediate;
import kstarxin.ir.operand.Memory;
import kstarxin.ir.operand.StaticPointer;
import kstarxin.ir.operand.VirtualRegister;
import kstarxin.parser.MxStarParser;
import kstarxin.utilities.MxType;
import kstarxin.utilities.NameMangler;

import java.util.*;
//this is a pure data oriented optimization!
public class Memorization {
    private static String memArrayName = "_mem_array_";
    private static String memValidName = "_mem_valid_";
    private static String memArrayFalse = "_mem_false_";
    private static String memLoopCond = "_mem_loop_cond_";
    private static String memLoopBody = "_mem_loop_body_";
    private static String memLoopStep = "_mem_loop_step_";
    private static String old          = "_old";
    private static int  memArraySize = 256;
    private int memArrayCnt;
    private IRProgram ir;
    private HashSet<Method> methodCanBeMemorized;
    private Method mainMethod;
    public Memorization(IRProgram _ir){
        ir = _ir;
        methodCanBeMemorized = new HashSet<Method>();
        memArrayCnt = 0;
    }

    private void getCanMemorize(){
        int callCount = 0;
        for(Method m : ir.getMethodMap().values()){
            if(m.hintName.equals(NameMangler.mainMethodName)){
                mainMethod = m;
                continue;
            }
            if(m.parameters.size() != 1 || m.classThisPointer != null) continue;
            boolean canMemorize = true;
            m.dfs();
            for(BasicBlock bb : m.basicBlockInDFSOrder){
                for(Instruction inst = bb.getBeginInst(); inst != null; inst = inst.next){
                    if(inst instanceof LoadInstruction || inst instanceof StoreInstruction){
                        canMemorize = false;
                        break;
                    }
                    if(inst instanceof CallInstruction){
                        if(((CallInstruction) inst).callee != m){
                            canMemorize = false;
                            break;
                        }
                        callCount++;
                    }
                }
                if(!canMemorize) break;
            }
            if(!canMemorize || callCount == 0) continue;
            else methodCanBeMemorized.add(m);
        }
    }


    private VirtualRegister addHeapAlloc(VirtualRegister ret, Immediate size, Method m, BasicBlock bb) {
        CallInstruction call = new CallInstruction(ir.malloc, ret);
        VirtualRegister tmpReg = m.allocateNewTmpRegister();
        bb.insertEnd(new MoveInstruction(tmpReg, (Immediate) size));
        call.addParameter(tmpReg);
        bb.insertEnd(call);
        return ret;
    }


    private void rewrite(Method m){
        m.isMemorized = true;
        StaticPointer arrayForMem = new StaticPointer(memArrayName + memArrayCnt, Configure.PTR_SIZE);
        StaticPointer validForMem = new StaticPointer(memValidName + memArrayCnt, Configure.PTR_SIZE);
        ir.addGlobalVariable(arrayForMem.hintName, arrayForMem, new MxType(MxType.TypeEnum.INT));
        ir.addGlobalVariable(validForMem.hintName, validForMem, new MxType(MxType.TypeEnum.INT));
        m.globalVariableUsed.add(arrayForMem);
        m.globalVariableUsed.add(validForMem);
        memArrayCnt++;
        BasicBlock startBB = m.startBlock;
        BasicBlock endBB = m.endBlock;
        BasicBlock newStart = new BasicBlock(m, null, null, startBB.blockLabel);
        BasicBlock newStartFalse = new BasicBlock(m, null, newStart, startBB.blockLabel + memArrayFalse);
        startBB.blockLabel += old;
        VirtualRegister loadedValidAddr = m.allocateNewTmpRegister();
        VirtualRegister  loadedDataAddr = m.allocateNewTmpRegister();
        newStart.insertEnd(new LoadInstruction(loadedDataAddr, arrayForMem));
        newStart.insertEnd(new LoadInstruction(loadedValidAddr, validForMem));
        Memory validBitAddr = new Memory(loadedValidAddr, m.parameters.get(0),Configure.PTR_SIZE, Configure.PTR_SIZE);
        VirtualRegister validBit = m.allocateNewTmpRegister();
        newStart.insertEnd(new LoadInstruction(validBit, validBitAddr));
        newStart.insertEnd(new CompareInstruction(validBit, new Immediate(1)));
        newStart.insertEnd(new ConditionJumpInstruction(MxStarParser.NEQ, startBB, newStartFalse));
        newStart.addSucc(startBB);
        newStart.addSucc(newStartFalse);
        newStartFalse.insertEnd(new LoadInstruction(loadedDataAddr, arrayForMem));
        Memory dataBitAddr = new Memory(loadedDataAddr, m.parameters.get(0), Configure.PTR_SIZE, Configure.PTR_SIZE);
        VirtualRegister loadedData = m.allocateNewTmpRegister();
        newStartFalse.insertEnd(new LoadInstruction(loadedData, dataBitAddr));
        newStartFalse.insertEnd(new MoveInstruction(m.returnRegister, loadedData));
        newStartFalse.insertEnd(new DirectJumpInstruction(endBB));
        newStartFalse.addSucc(endBB);
        ReturnInstruction ret = (ReturnInstruction)endBB.getEndInst();
        ret.insertBeforeThis(new StoreInstruction(validBitAddr, new Immediate(1)));
        ret.insertBeforeThis(new StoreInstruction(dataBitAddr, m.returnRegister));
        m.startBlock = newStart;



        //--------code above rewrite function self-----------

        Immediate allocSize = new Immediate(memArraySize * Configure.PTR_SIZE);
        Method initMethod = ir.getInitMethod();
        initMethod.globalVariableUsed.add(validForMem);
        initMethod.globalVariableUsed.add(arrayForMem);
        BasicBlock initStart = initMethod.startBlock;
        BasicBlock newInitStart = new BasicBlock(initMethod, null, null, initStart.blockLabel);
        initStart.blockLabel += old;
        initMethod.startBlock = newInitStart;
        VirtualRegister validAddr = initMethod.allocateNewTmpRegister();
        VirtualRegister dataAddr = initMethod.allocateNewTmpRegister();
        VirtualRegister offset = initMethod.allocateNewTmpRegister();
        Memory toReset = new Memory(validAddr, offset, Configure.PTR_SIZE, Configure.PTR_SIZE);
        addHeapAlloc(validAddr, allocSize, initMethod, newInitStart);
        addHeapAlloc(dataAddr, allocSize, initMethod, newInitStart);
        newInitStart.insertEnd(new StoreInstruction(arrayForMem, dataAddr));
        newInitStart.insertEnd(new StoreInstruction(validForMem, validAddr));
        BasicBlock loopCond =  new BasicBlock(initMethod, null, newInitStart, memLoopCond + memArrayCnt);
        BasicBlock loopBody =  new BasicBlock(initMethod, null, loopCond, memLoopBody + memArrayCnt);
        newInitStart.insertEnd(new MoveInstruction(offset, new Immediate(0)));
        newInitStart.insertEnd(new DirectJumpInstruction(loopCond));
        loopCond.insertEnd(new CompareInstruction(offset, new Immediate(memArraySize)));
        loopCond.insertEnd(new ConditionJumpInstruction(MxStarParser.LT, loopBody, initStart));
        loopCond.addSucc(initStart);
        loopCond.addSucc(loopBody);
        loopBody.insertEnd(new StoreInstruction(toReset, new Immediate(0)));
        loopBody.insertEnd(new UnaryInstruction(MxStarParser.INC, offset, offset));
        loopBody.insertEnd(new DirectJumpInstruction(loopCond));
        loopBody.addSucc(loopCond);
    }

    public void run(){
        getCanMemorize();
        methodCanBeMemorized.forEach(method -> {
            System.err.println("mem" + method.hintName);
            rewrite(method);
        });
    }
}
