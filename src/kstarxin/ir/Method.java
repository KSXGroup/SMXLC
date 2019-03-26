package kstarxin.ir;

import kstarxin.ast.ParameterDeclarationNode;
import kstarxin.ir.instruction.*;
import kstarxin.ir.operand.*;
import kstarxin.ir.superblock.*;
import kstarxin.utilities.NameMangler;

import java.util.*;

public class Method {
    public static String retVal             = "_returnValue";
    public static String retTmp             = "_returnTmpValue";
    public static String thisPtr            = "_this";
    public static String tmpRegPrefix       =  "_tmp";
    public boolean                          isBuiltin;
    public Integer                          tmpRegisterCounter;
    public ArrayList<VirtualRegister>       parameters;
    public HashMap<String, VirtualRegister> localVariables;
    public HashMap<String, VirtualRegister> tmpLocalRegisters;
    public HashMap<String, Integer>         localVariableOffset;
    public List<ReturnInstruction>          returnInsts;
    public List<LoopSuperBlock>             loops;
    public List<BasicBlock>                 basicBlockInDFSOrder;
    public LinkedHashSet<BasicBlock>        basicBlockInBFSOrder;
    public BasicBlock                       startBlock;
    public BasicBlock                       endBlock;
    public List<ConditionSuperBlock>        conditions;
    public String                           hintName;
    public VirtualRegister                  returnRegister;
    public VirtualRegister                  classThisPointer; // as parameter;



    private Integer                         tmpLabelCounter;
    private HashSet<BasicBlock>             visited;

    public Method(String _hintName, boolean inClass){
        isBuiltin           = false;
        basicBlockInDFSOrder= new LinkedList<BasicBlock>();
        basicBlockInBFSOrder= new LinkedHashSet<BasicBlock>();
        returnRegister      = new VirtualRegister(retVal, _hintName + retVal);
        tmpRegisterCounter  = 0;
        tmpLabelCounter     = 0;
        hintName            = _hintName;
        startBlock          = null;
        loops               = new LinkedList<LoopSuperBlock>();
        conditions          = new LinkedList<ConditionSuperBlock>();
        parameters          = new ArrayList<VirtualRegister>();
        localVariables      = new HashMap<String, VirtualRegister>();
        tmpLocalRegisters   = new HashMap<String, VirtualRegister>();
        localVariableOffset = new HashMap<String, Integer>();
        returnInsts         = new LinkedList<ReturnInstruction>();
        endBlock            = new BasicBlock(this,null, null, _hintName + IRBuilderVisitor.ret); //block for merge return insts
        visited             = new HashSet<BasicBlock>();

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

    public void dfs(BasicBlock bb, int order){
        if(isBuiltin) return;
        else if(visited.contains(bb)) return;
        else{
            visited.clear();
            BasicBlock cur = null;
            if(bb == null) cur = startBlock;
            else cur = bb;
            cur.dfsOrder = order;
            if(cur.succ.size() == 0) return;
            cur.succ.forEach(succ -> {
                basicBlockInDFSOrder.add(succ);
                visited.add(succ);
                dfs(succ, order + 1);
            });
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

    public int cleanUp(){
        if(isBuiltin) return 0;
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
       // basicBlockInBFSOrder.clear();
        return count;
    }
}
