package kstarxin.ir.asmir;

import kstarxin.ir.operand.VirtualRegister;
import kstarxin.ir.operand.physical.PhysicalRegister;
import kstarxin.utilities.NameMangler;

import java.util.LinkedList;

public class ASMLevelIRMethod {
    public static String asmTmpRegPrefix  =  "_asm_tmp";

    public String                       name;
    public LinkedList<ASMBasicBlock>    basicBlocks;
    public VirtualRegister              thisPointer;
    public int                          ASMTmpRegisterCounter;
    public boolean                      isBuiltIn;
    public ASMLevelIRMethod(String _name, int _ASMTmpRegisterCounter) {
        name                    = NameMangler.convertToASMName(_name);
        ASMTmpRegisterCounter   = _ASMTmpRegisterCounter;
        basicBlocks             = new LinkedList<ASMBasicBlock>();
        isBuiltIn               = false;
        thisPointer             = null;
    }

    public VirtualRegister asmAllocateVirtualRegister(){
        Integer id = ASMTmpRegisterCounter;
        VirtualRegister ret = new VirtualRegister(id.toString(), asmTmpRegPrefix + ASMTmpRegisterCounter);
        ret.nasmName = ret.mangledName;
        ASMTmpRegisterCounter += 1;
        return ret;
    }

    public VirtualRegister asmAllocateVirtualRegister(PhysicalRegister preg){
        Integer id = ASMTmpRegisterCounter;
        VirtualRegister ret = new VirtualRegister(id.toString(), asmTmpRegPrefix + ASMTmpRegisterCounter);
        ret.nasmName = ret.mangledName;
        ASMTmpRegisterCounter += 1;
        ret.spaceAllocatedTo = preg;
        return ret;
    }

    public void addBasicBlock(ASMBasicBlock asmbb){
        basicBlocks.addLast(asmbb);
    }
}
