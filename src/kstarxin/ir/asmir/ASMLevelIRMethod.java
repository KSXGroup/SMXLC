package kstarxin.ir.asmir;

import kstarxin.ir.operand.VirtualRegister;
import kstarxin.ir.operand.physical.PhysicalRegister;
import kstarxin.nasm.PhysicalRegisterSet;
import kstarxin.utilities.NameMangler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class ASMLevelIRMethod {
    public static String asmTmpRegPrefix  =  "_asm_tmp";

    public String                                       name;
    public LinkedList<ASMBasicBlock>                    basicBlocks;
    public VirtualRegister                              thisPointer;
    public int                                          parameterCount;
    public int                                          ASMTmpRegisterCounter;
    public boolean                                      isBuiltIn;
    public HashSet<PhysicalRegister>                    usedCalleeSavedRegister;
    public HashSet<PhysicalRegister>                    usedCallerSavedRegister;
    public HashSet<VirtualRegister>                     virtualCalleeSavedRegister;
    public HashSet<VirtualRegister>                     virtualCallerSavedRegister;
    public ArrayList<VirtualRegister>                   virtualParameterPassingRegister;
    public int                                          stackAligned;

    private HashMap<PhysicalRegister, VirtualRegister>  pvMap;

    public ASMLevelIRMethod(String _name, int _ASMTmpRegisterCounter, int parameterNum) {
        parameterCount                  = parameterNum;
        name                            = NameMangler.convertToASMName(_name);
        ASMTmpRegisterCounter           = _ASMTmpRegisterCounter;
        basicBlocks                     = new LinkedList<ASMBasicBlock>();
        isBuiltIn                       = false;
        thisPointer                     = null;
        pvMap                           = new HashMap<PhysicalRegister, VirtualRegister>();
        usedCalleeSavedRegister         = new HashSet<PhysicalRegister>();
        usedCallerSavedRegister         = new HashSet<PhysicalRegister>();
        virtualCalleeSavedRegister      = new HashSet<VirtualRegister>();
        virtualCallerSavedRegister      = new HashSet<VirtualRegister>();
        virtualParameterPassingRegister = new ArrayList<VirtualRegister>();

        for(PhysicalRegister preg : PhysicalRegisterSet.CallerSavedRegister){
            virtualCallerSavedRegister.add(asmAllocateVirtualRegister(preg));
        }

        for(PhysicalRegister preg : PhysicalRegisterSet.CalleeSavedRegister){
            virtualCalleeSavedRegister.add(asmAllocateVirtualRegister(preg));
        }

        for(PhysicalRegister preg : PhysicalRegisterSet.ParameterPassingPhysicalRegister){
            virtualParameterPassingRegister.add(asmAllocateVirtualRegister(preg));
        }

    }

    public VirtualRegister asmAllocateVirtualRegister(){
        Integer id = ASMTmpRegisterCounter;
        VirtualRegister ret = new VirtualRegister(id.toString(), asmTmpRegPrefix + ASMTmpRegisterCounter);
        ret.nasmName = ret.mangledName;
        ASMTmpRegisterCounter += 1;
        return ret;
    }

    public VirtualRegister asmAllocateVirtualRegister(PhysicalRegister preg){
        if(pvMap.containsKey(preg)) return pvMap.get(preg);
        else {
            Integer id = ASMTmpRegisterCounter;
            VirtualRegister ret = new VirtualRegister(id.toString(), asmTmpRegPrefix + ASMTmpRegisterCounter + "_" + preg.getNASMName());
            ret.nasmName = ret.mangledName;
            ASMTmpRegisterCounter += 1;
            ret.spaceAllocatedTo = preg;
            pvMap.put(preg, ret);
            return ret;
        }
    }

    public void addBasicBlock(ASMBasicBlock asmbb){
        basicBlocks.addLast(asmbb);
    }

    public void collectInfo(){
        basicBlocks.forEach(ASMBasicBlock::collectInfo);
    }

    public void addColorUsed(PhysicalRegister preg){
        if(PhysicalRegisterSet.CalleeSavedRegister.contains(preg)) usedCalleeSavedRegister.add(preg);
        else if(PhysicalRegisterSet.CallerSavedRegister.contains(preg)) usedCallerSavedRegister.add(preg);
    }
}
