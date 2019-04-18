package kstarxin.ir.asmir;

import kstarxin.ir.operand.VirtualRegister;

import java.util.LinkedList;

public class ASMLevelIRMethod {
    public static String asmTmpRegPrefix  =  "_asm_tmp";

    public String                       name;
    public LinkedList<ASMBasicBlock>    basicBlocks;
    public int                          ASMTmpRegisterCounter;
    public ASMLevelIRMethod(String _name, int _ASMTmpRegisterCounter) {
        name                    = _name;
        ASMTmpRegisterCounter   = _ASMTmpRegisterCounter;
        basicBlocks             = new LinkedList<ASMBasicBlock>();
    }

    public VirtualRegister asmAllocateVirtualRegister(){
        Integer id = ASMTmpRegisterCounter;
        VirtualRegister ret = new VirtualRegister(id.toString(), asmTmpRegPrefix + ASMTmpRegisterCounter);
        ret.nasmName = ret.mangledName;
        return ret;
    }

    public void addBasicBlock(ASMBasicBlock asmbb){
        basicBlocks.addLast(asmbb);
    }
}
