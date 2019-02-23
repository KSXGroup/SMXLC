package kstarxin.ir;

import kstarxin.ir.instruction.*;
import kstarxin.ir.operand.*;
import kstarxin.ir.superblock.*;

import java.util.*;

public class Method {
    public static String retVal             = "_returnValue";
    public static String retTmp             = "_returnTmpValue";
    public int                              tmpRegisterCounter;
    public ArrayList<VirtualRegister>       parameters;
    public HashMap<String, VirtualRegister> localVariables;
    public HashMap<String, Integer>         localVariableOffset;
    public List<ReturnInstruction>          returnInsts;
    public List<LoopSuperBlock>             loops;
    public List<BasicBlock>                 basicBlocks;
    public BasicBlock                       startBlock;
    public BasicBlock                       endBlock;
    public List<ConditionSuperBlock>        conditions;
    public String                           hintName;
    public VirtualRegister                  returnTmpRegister;

    private VirtualRegister                 returnRegister;

    public Method(String _hintName){
        returnRegister      = new VirtualRegister(retVal, _hintName + retVal);
        returnTmpRegister   = new VirtualRegister(retTmp, _hintName + retTmp);
        hintName            = _hintName;
        startBlock          = null;
        endBlock            = new BasicBlock(this,null, null, _hintName + IRBuilderVisitor.ret);
        loops               = new LinkedList<LoopSuperBlock>();
        conditions          = new LinkedList<ConditionSuperBlock>();
        parameters          = new ArrayList<VirtualRegister>();
        localVariables      = new HashMap<String, VirtualRegister>();
        localVariableOffset = new HashMap<String, Integer>();
        returnInsts         = new LinkedList<ReturnInstruction>();
        basicBlocks         = new LinkedList<BasicBlock>();
        
        endBlock.insertEnd(new LoadInstruction(returnRegister, returnTmpRegister, new Immediate(0)));
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

    public void addReturnInstruction(ReturnInstruction ret){
        //TODO
    }

    public void addLocalVariable(String mangledName, VirtualRegister vreg){
        localVariables.put(mangledName, vreg);
    }

}
