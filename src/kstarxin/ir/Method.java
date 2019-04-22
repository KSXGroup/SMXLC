package kstarxin.ir;

import kstarxin.ast.ParameterDeclarationNode;
import kstarxin.ir.instruction.*;
import kstarxin.ir.operand.*;
import kstarxin.ir.operand.physical.PhysicalRegister;
import kstarxin.ir.superblock.*;
import kstarxin.utilities.NameMangler;

import java.util.*;

public class Method {
    public static String retVal             = "_returnValue";
    public static String retTmp             = "_returnTmpValue";
    public static String thisPtr            = "_this";
    public static String tmpRegPrefix       =  "_tmp";
    public boolean                          isBuiltin;
    public boolean                          canBeInlined;
    public Integer                          tmpRegisterCounter;
    public ArrayList<VirtualRegister>       parameters;
    public HashMap<String, VirtualRegister> localVariables;
    public HashMap<String, VirtualRegister> tmpLocalRegisters;
    public HashSet<Address>                 globalVariableUsed;
    public LinkedList<CallInstruction>      nonRecursiveMethodCall;
    public LinkedList<CallInstruction>      recursiveMethodCall;
    public LinkedList<PhysicalRegister>     usedCalleeSavedRegister;
    public List<ReturnInstruction>          returnInsts;
    public List<LoopSuperBlock>             loops;
    public LinkedHashSet<BasicBlock>        basicBlockInDFSOrder;
    public LinkedHashSet<BasicBlock>        basicBlockInBFSOrder;
    public BasicBlock                       startBlock;
    public BasicBlock                       endBlock;
    public String                           hintName;
    public VirtualRegister                  returnRegister;
    public VirtualRegister                  classThisPointer; // as parameter;
    public int                              stackAligned;


    private Integer                         tmpLabelCounter;
    private HashSet<BasicBlock>             visited;

    public Method(String _hintName, boolean inClass){
        isBuiltin               = false;
        canBeInlined            = false;
        basicBlockInDFSOrder    = new LinkedHashSet<BasicBlock>();
        basicBlockInBFSOrder    = new LinkedHashSet<BasicBlock>();
        returnRegister          = new VirtualRegister(retVal, _hintName + retVal);
        tmpRegisterCounter      = 0;
        tmpLabelCounter         = 0;
        hintName                = _hintName;
        startBlock              = null;
        loops                   = new LinkedList<LoopSuperBlock>();
        parameters              = new ArrayList<VirtualRegister>();
        localVariables          = new HashMap<String, VirtualRegister>();
        tmpLocalRegisters       = new HashMap<String, VirtualRegister>();
        returnInsts             = new LinkedList<ReturnInstruction>();
        usedCalleeSavedRegister = new LinkedList<PhysicalRegister>();
        endBlock                = new BasicBlock(this,null, null, _hintName + IRBuilderVisitor.ret); //block for merge return insts
        visited                 = new HashSet<BasicBlock>();
        nonRecursiveMethodCall  = new LinkedList<CallInstruction>();
        recursiveMethodCall     = new LinkedList<CallInstruction>();
        globalVariableUsed      = new HashSet<Address>();

        if(inClass){
            classThisPointer    = new VirtualRegister(thisPtr, _hintName + thisPtr);
            localVariables.put(_hintName + thisPtr , classThisPointer);
        }
        else classThisPointer           = null;
        
        //endBlock.insertEnd(new MoveInstruction(returnRegister, returnTmpRegister));
        endBlock.insertEnd(new ReturnInstruction(returnRegister));
    }


    public void setStartBlock(BasicBlock _startBlock){
        startBlock = _startBlock;
    }

    public void setEndBlock(BasicBlock _endBlock){
        endBlock = _endBlock;
    }

    public void addBasicBlock(BasicBlock bb){
        //basicBlocks.add(bb);
        //abandoned
    }

    public void addParameter(ParameterDeclarationNode n){
        String nm = NameMangler.mangleName(n);
        VirtualRegister para = new VirtualRegister(n.getIdentifier(), nm);
        parameters.add(para);
        localVariables.put(nm, para);
    }

    public void addLocalVariable(String mangledName, VirtualRegister vreg){
        localVariables.put(mangledName, vreg);
    }

    public VirtualRegister allocateNewTmpRegister(){
        String          id      = tmpRegisterCounter.toString();
        String          regName = hintName + tmpRegPrefix+id;
        VirtualRegister vreg    = new VirtualRegister(id, regName);
        vreg.tmpId              = tmpRegisterCounter;

        tmpLocalRegisters.put(regName, vreg);
        tmpRegisterCounter++;
        return vreg;
    }

    public String generateTmpLabel(){
        String ret = hintName + tmpRegPrefix + tmpLabelCounter.toString();
        tmpLabelCounter++;
        return ret;
    }

    public void setBuiltin() {
        isBuiltin = true;
    }

    public String getDisplayName(){
        return "%" + hintName;
    }

    private void dodfs(BasicBlock bb, int order){
        bb.dfsOrder = order;
        basicBlockInDFSOrder.add(bb);
        if(bb.succ.size() == 0) return;
        bb.succ.forEach(succ -> {
            if(!basicBlockInDFSOrder.contains(succ)) dodfs(succ, order + 1);
        });
    }

    public void dfs(){
        if(isBuiltin) return;
        else{
            basicBlockInDFSOrder.clear();
            dodfs(startBlock, 0);
        }
    }

    public void bfs(){
        if(isBuiltin) return;
        Queue<BasicBlock> Q = new LinkedList<BasicBlock>();
        visited.clear();
        Q.add(startBlock);
        while(!Q.isEmpty()){
            BasicBlock cur = Q.poll();
            basicBlockInBFSOrder.add(cur);
            cur.succ.forEach(basicBlock -> {
                if(!visited.contains(basicBlock)){
                    Q.add(basicBlock);
                    visited.add(basicBlock);
                }
            });
        }
    }

    private void fixCFG(){
        //holy shit, never try to maintain the cfg when build it
        if(isBuiltin) return;
        basicBlockInBFSOrder.clear();
        bfs();
        LinkedList<BasicBlock> predToRemove = new LinkedList<BasicBlock>();
        for (BasicBlock bb : basicBlockInBFSOrder) {
            predToRemove.clear();
            Instruction endInst = bb.getEndInst();
            bb.succ.forEach(dsucc->{dsucc.addPred(bb);});
            bb.pred.forEach(dpred->{
                Instruction end = dpred.getEndInst();
                if(end instanceof DirectJumpInstruction){
                    if(((DirectJumpInstruction) end).target != bb) predToRemove.add(dpred);
                }
                else if(end instanceof ConditionJumpInstruction){
                    if(((ConditionJumpInstruction) end).trueTarget != bb && ((ConditionJumpInstruction) end).falseTarget != bb) predToRemove.add(dpred);
                }
                else if(!(end instanceof ReturnInstruction))
                    throw new RuntimeException();
            });
            predToRemove.forEach(rm -> bb.removePred(rm));
        }
        basicBlockInBFSOrder.clear();
    }

    private int removeRedundantdBB(){
        Queue<BasicBlock> Q = new LinkedList<BasicBlock>();
        visited.clear();
        int count = 0;
        Q.add(endBlock);
        while(!Q.isEmpty()){
            BasicBlock cur = Q.poll();
            if(cur.pred.size() == 0 && cur != startBlock){
                cur.succ.forEach(bb->{
                    bb.pred.remove(cur);
                    if(bb.pred.size() == 0) Q.add(bb);
                });
                count++;
                cur.setRemoved();
                visited.add(cur);
            }else{
                cur.pred.forEach(bb->{
                    if(!visited.contains(bb)) {
                        Q.add(bb);
                        visited.add(bb);
                    }
                });
            }
        }
        return count;
    }

    public int cleanUp(){
        //PAY FOR NOT ELEGENT DESIGN, SHIT!!
        int ret = removeRedundantdBB();
        fixCFG();
        ret += removeRedundantdBB();
        return ret;
    }

    public void collectDefUseInfo(){
        basicBlockInBFSOrder.clear();
        bfs();
        basicBlockInBFSOrder.forEach(BasicBlock::collectDefUseInfo);
    }



}
