package kstarxin.ir.asmir;

import kstarxin.ir.asmir.ASMLevelIRInstruction.*;

import java.util.LinkedList;

public class ASMLevelIRProgram {
    private LinkedList<ASMRESInstruction>       BSSSection;
    private LinkedList<ASMDInstruction>         DataSection;
    private LinkedList<ASMRomDataInstruction>   romDataSection;
    private LinkedList<ASMLevelIRMethod>        method;
    private ASMLevelIRMethod                    globalInit;

    public ASMLevelIRProgram(){
        BSSSection      = new LinkedList<ASMRESInstruction>();
        DataSection     = new LinkedList<ASMDInstruction>();
        romDataSection  = new LinkedList<ASMRomDataInstruction>();
        method          = new LinkedList<ASMLevelIRMethod>();
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

    public LinkedList<ASMLevelIRMethod> getMethod() {
        return method;
    }

    public ASMLevelIRMethod getGlobalInit() {
        return globalInit;
    }

    public void setGlobalInit(ASMLevelIRMethod _globalInit) {
        globalInit = _globalInit;
    }
}
