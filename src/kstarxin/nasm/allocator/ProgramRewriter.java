package kstarxin.nasm.allocator;

import kstarxin.compiler.Configure;
import kstarxin.ir.asmir.ASMBasicBlock;
import kstarxin.ir.asmir.ASMLevelIRInstruction.*;
import kstarxin.ir.asmir.ASMLevelIRMethod;
import kstarxin.ir.asmir.ASMLevelIRProgram;
import kstarxin.ir.asmir.ASMLevelIRVisitor;
import kstarxin.ir.operand.*;
import kstarxin.ir.operand.physical.PhysicalRegister;
import kstarxin.nasm.PhysicalRegisterSet;
import kstarxin.utilities.OperatorTranslator;

import java.util.*;

public class ProgramRewriter implements ASMLevelIRVisitor<Void> {
    public ASMLevelIRMethod                             currentMethod;
    public ASMBasicBlock                                currentBasicBlock;
    public LinkedList<ASMInstruction>                   rewritedInsts;
    public VirtualRegister                              virtualRBP;
    public int                                          offsetInStack;
    public HashSet<VirtualRegister>                     spilledRegisters;
    public HashMap<VirtualRegister, Memory>             spilledVirtualRegisterToStackSpace;
    public HashMap<VirtualRegister, VirtualRegister>    spillReplaceMap;
    boolean needLoad = false;
    boolean needStore = false;
    HashSet<VirtualRegister> toLoad;
    HashSet<VirtualRegister> toStore;

    public ProgramRewriter(){
        rewritedInsts                           = new LinkedList<ASMInstruction>();
        spilledVirtualRegisterToStackSpace      = new HashMap<VirtualRegister, Memory>();
        spillReplaceMap                         = new HashMap<VirtualRegister, VirtualRegister>();
        toStore                                 = new HashSet<VirtualRegister>();
        toLoad                                  = new HashSet<VirtualRegister>();
    }


    //1. allocate stack space for spilled reg
    //2. create tmp reg for spilled reg
    //3. add load before use and store after def

    public void     rewrite(ASMLevelIRMethod method, HashSet<VirtualRegister> _spilledRegisters){
        spilledVirtualRegisterToStackSpace.clear();
        offsetInStack       = 0;
        currentMethod       = method;
        virtualRBP          = currentMethod.asmAllocateVirtualRegister(PhysicalRegisterSet.RBP);
        spilledRegisters    = _spilledRegisters;
        spilledRegisters.forEach(spilled->{
            spilledVirtualRegisterToStackSpace.put(spilled, allocateStackSpace());
        });
        for(ASMBasicBlock bb: currentMethod.basicBlocks){
            rewritedInsts.clear();
            currentBasicBlock = bb;
            bb.insts.forEach(this::visit);
            bb.insts.clear();
            bb.insts.addAll(rewritedInsts);
        }
    }

    private Memory allocateStackSpace(){
        Memory ret =  new Memory(virtualRBP, offsetInStack - Configure.PTR_SIZE, Configure.PTR_SIZE);
        offsetInStack -= 8;
        return ret;
    }

    private void processInst(ASMInstruction inst){
        inst.collectInfo();
        if(inst.def.size() == 0 && inst.use.size() == 0){
            rewritedInsts.add(inst);
            return;
        }
        needLoad = false;
        needStore = false;
        toLoad.clear();
        toStore.clear();
        for(VirtualRegister vreg : inst.use){
            if(spilledRegisters.contains(vreg)){
                needLoad = true;
                toLoad.add(vreg);
            }
        }
        for(VirtualRegister vreg : inst.def){
            if(spilledRegisters.contains(vreg)){
                needStore = true;
                toStore.add(vreg);
            }
        }

        spillReplaceMap.clear();
        if(needLoad){
            toLoad.forEach(vreg->{
                VirtualRegister tmp = currentMethod.asmAllocateVirtualRegister();
                spillReplaceMap.put(vreg, tmp);
                rewritedInsts.add(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock,
                        tmp, spilledVirtualRegisterToStackSpace.get(vreg)));
            });
        }
        rewritedInsts.add(inst);
        if(needStore){
            toStore.forEach(vreg->{
                VirtualRegister tmp = null;
                if(spillReplaceMap.containsKey(vreg)) tmp = spillReplaceMap.get(vreg);
                else tmp = currentMethod.asmAllocateVirtualRegister();
                spillReplaceMap.put(vreg, tmp);
                rewritedInsts.add(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock,
                        spilledVirtualRegisterToStackSpace.get(vreg), tmp));
            });
        }
        if(needStore || needLoad) inst.replaceOperandForSpill(spillReplaceMap);
    }

    @Override
    public Void visit(ASMInstruction inst) {
        return inst.accept(this);
    }

    @Override
    public Void visit(ASMBinaryInstruction inst) {
        processInst(inst);
        return null;
    }

    @Override
    public Void visit(ASMCallInstruction inst) {
        processInst(inst);
        return null;
    }

    @Override
    public Void visit(ASMCDQInstruction inst) {
        processInst(inst);
        return null;
    }

    @Override
    public Void visit(ASMCompareInstruction inst) {
        processInst(inst);
        return null;
    }

    @Override
    public Void visit(ASMConditionalJumpInstruction inst) {
        processInst(inst);
        return null;
    }

    @Override
    public Void visit(ASMDInstruction inst) {
        throw new RuntimeException();
    }

    @Override
    public Void visit(ASMDirectJumpInstruction inst) {
        processInst(inst);
        return null;
    }

    @Override
    public Void visit(ASMLEAInstruction inst) {
        processInst(inst);
        return null;
    }

    @Override
    public Void visit(ASMLeaveInstruction inst) {
        processInst(inst);
        return null;
    }

    @Override
    public Void visit(ASMMoveInstruction inst) {
        processInst(inst);
        return null;
    }

    @Override
    public Void visit(ASMPopInstruction inst) {
        processInst(inst);
        return null;
    }

    @Override
    public Void visit(ASMPushInstruction inst) {
        processInst(inst);
        return null;
    }

    @Override
    public Void visit(ASMRESInstruction inst) {
        throw new RuntimeException();
    }

    @Override
    public Void visit(ASMReturnInstruction inst) {
        processInst(inst);
        return null;
    }

    @Override
    public Void visit(ASMRomDataInstruction inst) {
        throw new RuntimeException();
    }

    @Override
    public Void visit(ASMSetInstruction inst) {
        processInst(inst);
        return null;
    }

    @Override
    public Void visit(ASMUnaryInstruction inst) {
        processInst(inst);
        return null;
    }

    @Override
    public Void visit(Operand op) {
        return null;
    }

    @Override
    public Void visit(Address op) {
        return null;
    }

    @Override
    public Void visit(Constant op) {
        return null;
    }

    @Override
    public Void visit(Immediate op) {
        return null;
    }

    @Override
    public Void visit(Label op) {
        return null;
    }

    @Override
    public Void visit(Memory op) {
        return null;
    }

    @Override
    public Void visit(Register op) {
        return null;
    }

    @Override
    public Void visit(StaticPointer op) {
        return null;
    }

    @Override
    public Void visit(StaticString op) {
        return null;
    }

    @Override
    public Void visit(StringLiteral op) {
        return null;
    }

    @Override
    public Void visit(VirtualRegister op) {
        return null;
    }
}
