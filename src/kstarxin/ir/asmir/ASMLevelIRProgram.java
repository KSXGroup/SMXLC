package kstarxin.ir.asmir;

import kstarxin.ir.Method;
import kstarxin.ir.asmir.ASMLevelIRInstruction.*;
import java.util.*;

import java.util.LinkedList;

public class ASMLevelIRProgram {
    private LinkedList<ASMRESInstruction>       BSSSection;
    private LinkedList<ASMDInstruction>         DataSection;
    private LinkedList<ASMRomDataInstruction>   romDataSection;
    private HashMap<Method, ASMLevelIRMethod>   methodMap;
    private ASMLevelIRMethod                    globalInit;
    public  boolean                             ifAllocated;

    public ASMLevelIRProgram(){
        BSSSection      = new LinkedList<ASMRESInstruction>();
        DataSection     = new LinkedList<ASMDInstruction>();
        romDataSection  = new LinkedList<ASMRomDataInstruction>();
        methodMap       = new HashMap<Method, ASMLevelIRMethod>();
    }

    public void addASMMethod(Method mirMethod,ASMLevelIRMethod _method){
        methodMap.put(mirMethod, _method);
    }

    public void addBSSData(ASMRESInstruction inst){
        BSSSection.add(inst);
    }

    public void addDData(ASMDInstruction inst){
        DataSection.add(inst);
    }

    public void addRomData(ASMRomDataInstruction inst){
        romDataSection.add(inst);
    }

    public LinkedList<ASMDInstruction> getDataSection() {
        return DataSection;
    }

    public LinkedList<ASMRomDataInstruction> getRomDataSection() {
        return romDataSection;
    }

    public LinkedList<ASMRESInstruction> getBSSSection() {
        return BSSSection;
    }

    public HashMap<Method, ASMLevelIRMethod> getMethodMap() {
        return methodMap;
    }

    public ASMLevelIRMethod getGlobalInit() {
        return globalInit;
    }

    public void setGlobalInit(ASMLevelIRMethod _globalInit) {
        globalInit = _globalInit;
    }
}
