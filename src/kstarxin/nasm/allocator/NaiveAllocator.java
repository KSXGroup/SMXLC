package kstarxin.nasm.allocator;

import kstarxin.compiler.Configure;
import kstarxin.ir.*;
import kstarxin.ir.asmir.ASMBasicBlock;
import kstarxin.ir.asmir.ASMLevelIRInstruction.*;
import kstarxin.ir.asmir.ASMLevelIRMethod;
import kstarxin.ir.asmir.ASMLevelIRProgram;
import kstarxin.ir.asmir.ASMLevelIRVisitor;
import kstarxin.ir.instruction.Instruction;
import kstarxin.ir.operand.*;
import kstarxin.ir.operand.physical.*;
import kstarxin.nasm.PhysicalRegisterSet;
import kstarxin.utilities.*;
import kstarxin.utilities.NameMangler.PhysicalRegisterName;

import java.util.*;

public class NaiveAllocator implements ASMLevelIRVisitor<Void> {

    private static final int totalPhyscalRegister = 12;


    private ASMLevelIRProgram           lir;
    private int                         offsetInStack;
    private LinkedList<ASMInstruction>  allocatedInstList;
    private ASMLevelIRMethod            currentMethod;
    private ASMBasicBlock               currentBasicBlock;
    private static PhysicalRegister             tmpRegA = PhysicalRegisterSet.RAX;
    private static PhysicalRegister             tmpRegB = PhysicalRegisterSet.RDX;
    private static PhysicalRegister             tmpBase =PhysicalRegisterSet.RBX;
    private static PhysicalRegister             tmpIndex = PhysicalRegisterSet.R10;

    public NaiveAllocator(ASMLevelIRProgram _lir){
        lir = _lir;
        allocatedInstList = new LinkedList<ASMInstruction>();
        offsetInStack = 0;
    }

    //callee should save RBX, RBP, R12, R13, R14, R15 according to SYSTEM V AMD64 ABI (registers which callee should save to stack) (callee saved register)
    //caller saved: RAX, RCX, RDX, RDI, RSI, RSP, R8, R9, R10, R11
    //I think RSP RBP should not be allocated
    //I decide to use EAX and ECX as tmp register now
    //naviely, registers can be used now are EBX, EDX, EDI, ESI, R8D, R9D, R10D, R11D, R12D, R13D, R14D, R15D
    //paramter passed via RDI, RSI, RDX, RCX, R8, R9

    /*private boolean ifPreAllocated(Operand op){
        if(op instanceof Memory)
    }*/

    private boolean isMemory(Operand op){
        if(op instanceof StackSpace || op instanceof PhysicalRegister) throw new RuntimeException();
        if(op instanceof Memory || op instanceof StaticString || op instanceof StaticPointer) return true;
        else if(op instanceof VirtualRegister && ((VirtualRegister) op).spaceAllocatedTo instanceof StackSpace) return true;
        else return false;
    }

    private StackSpace allocateStackSpace(){
        StackSpace ret = new StackSpace(PhysicalRegisterSet.RBP, offsetInStack - 8);
        offsetInStack -= 8;
        return ret;
    }

    public void run(){
        //allocate all registers to stack!
        for(ASMLevelIRMethod m : lir.getMethodMap().values()) {
            if (!m.isBuiltIn) {
                currentMethod = m;
                if(m.thisPointer != null) m.thisPointer.spaceAllocatedTo = allocateStackSpace();
                for (ASMBasicBlock ab : m.basicBlocks) {
                    allocatedInstList.clear();
                    currentBasicBlock = ab;
                    ab.insts.forEach(this::visit);
                    ab.insts.clear();
                    ab.insts.addAll(allocatedInstList);
                }
                if((-offsetInStack) % 16 != 0) offsetInStack -= 8;
                ASMBasicBlock firstBB = m.basicBlocks.getFirst();
                firstBB.insts.addFirst(new ASMBinaryInstruction(OperatorTranslator.NASMInstructionOperator.SUB, currentBasicBlock, currentMethod.asmAllocateVirtualRegister(PhysicalRegisterSet.RSP), new Immediate(-offsetInStack + Configure.CALL_STACK_ALIGN)));
                firstBB.insts.addFirst(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, currentMethod.asmAllocateVirtualRegister(PhysicalRegisterSet.RBP), currentMethod.asmAllocateVirtualRegister(PhysicalRegisterSet.RSP)));
                firstBB.insts.addFirst(new ASMPushInstruction(currentBasicBlock, currentMethod.asmAllocateVirtualRegister(PhysicalRegisterSet.RBP)));
                offsetInStack = 0;
            }
        }
        lir.ifAllocated = true;
    }

    @Override
    public Void visit(ASMInstruction inst) {
        return inst.accept(this);
    }

    @Override
    public Void visit(ASMBinaryInstruction inst) {
        visit(inst.src);
        visit(inst.dst);
        if(isMemory(inst.src) && isMemory(inst.dst)){
            VirtualRegister tmp = currentMethod.asmAllocateVirtualRegister(tmpRegA);
            if(inst.operator.equals(OperatorTranslator.NASMInstructionOperator.IMUL)){
                VirtualRegister tmpB= currentMethod.asmAllocateVirtualRegister(tmpRegB);
                allocatedInstList.add(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, tmp, inst.src));
                allocatedInstList.add(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, tmpB, inst.dst));
                allocatedInstList.add(new ASMBinaryInstruction(OperatorTranslator.NASMInstructionOperator.IMUL, currentBasicBlock, tmpB, tmp));
                allocatedInstList.add(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, inst.dst, tmpB));
            }
            else{
                allocatedInstList.add(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, tmp, inst.src));
                inst.src = tmp;
                allocatedInstList.add(inst);
            }
        }else if(inst.operator.equals(OperatorTranslator.NASMInstructionOperator.IMUL)){
            VirtualRegister tmp = currentMethod.asmAllocateVirtualRegister(tmpRegA);
            if(isMemory(inst.src)){
                allocatedInstList.add(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, tmp, inst.src));
                inst.src = tmp;
                allocatedInstList.add(inst);
            }
            if(isMemory(inst.dst)){
                allocatedInstList.add(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, tmp, inst.dst));
                Operand dstt= inst.dst;
                inst.dst = tmp;
                allocatedInstList.add(inst);
                allocatedInstList.add(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, dstt, tmp));
            }
        } else allocatedInstList.add(inst);
        return null;
    }

    @Override
    public Void visit(ASMCallInstruction inst) {
        allocatedInstList.add(inst);
        return null;
    }

    @Override
    public Void visit(ASMCDQInstruction inst) {
        allocatedInstList.add(inst);
        return null;
    }

    @Override
    public Void visit(ASMCompareInstruction inst) {
        visit(inst.lhs);
        VirtualRegister tcmpa = currentMethod.asmAllocateVirtualRegister(tmpRegA);
        allocatedInstList.add(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV,currentBasicBlock, tcmpa, inst.lhs));
        visit(inst.rhs);
        VirtualRegister tcmpb = currentMethod.asmAllocateVirtualRegister(tmpRegB);
        allocatedInstList.add(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, tcmpb, inst.rhs));
        inst.lhs = tcmpa;
        inst.rhs = tcmpb;
        allocatedInstList.add(inst);
        return null;
    }

    @Override
    public Void visit(ASMConditionalJumpInstruction inst) {
        allocatedInstList.add(inst);
        return null;
    }

    @Override
    public Void visit(ASMDInstruction inst) {
        throw new RuntimeException();
    }

    @Override
    public Void visit(ASMDirectJumpInstruction inst) {
        allocatedInstList.add(inst);
        return null;
    }

    @Override
    public Void visit(ASMLEAInstruction inst) {
        visit(inst.src);
        visit(inst.dst);
        if((inst.dst instanceof VirtualRegister && ((VirtualRegister) inst.dst).spaceAllocatedTo instanceof StackSpace) || inst.dst instanceof Memory) {
            Operand tmp = inst.dst;
            VirtualRegister tdst = currentMethod.asmAllocateVirtualRegister(tmpRegA);
            inst.dst = tdst;
            allocatedInstList.add(inst);
            allocatedInstList.add(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, tmp, tdst));
        }else allocatedInstList.add(inst);
        return null;
    }

    @Override
    public Void visit(ASMLeaveInstruction inst) {
        allocatedInstList.add(inst);
        return null;
    }

    @Override
    public Void visit(ASMMoveInstruction inst) {
        visit(inst.src);
        visit(inst.dst);
        if(isMemory(inst.src) && isMemory(inst.dst)){
            VirtualRegister pvreg = currentMethod.asmAllocateVirtualRegister(tmpRegA);
            allocatedInstList.add(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, pvreg, inst.src));
            inst.src = pvreg;
            allocatedInstList.add(inst);
        }else if(inst.op.equals(OperatorTranslator.NASMInstructionOperator.MOVZX) && isMemory(inst.dst)){
            if(!(inst.src instanceof VirtualRegister && ((VirtualRegister) inst.src).spaceAllocatedTo == PhysicalRegisterSet.AL)) throw new RuntimeException();
            VirtualRegister vrax = currentMethod.asmAllocateVirtualRegister(PhysicalRegisterSet.RAX);
            Operand ddst = inst.dst;
            inst.dst = vrax;
            allocatedInstList.add(inst);
            allocatedInstList.add(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, ddst, vrax));
        } else allocatedInstList.add(inst);
        return null;
    }

    @Override
    public Void visit(ASMPopInstruction inst) {
        visit(inst.op);
        allocatedInstList.add(inst);
        return null;
    }

    @Override
    public Void visit(ASMPushInstruction inst) {
        visit(inst.src);
        allocatedInstList.add(inst);
        return null;
    }

    @Override
    public Void visit(ASMRESInstruction inst) {
        throw new RuntimeException();
    }

    @Override
    public Void visit(ASMReturnInstruction inst) {
        allocatedInstList.add(inst);
        return null;
    }

    @Override
    public Void visit(ASMRomDataInstruction inst) {
        throw new RuntimeException();
    }

    @Override
    public Void visit(ASMSetInstruction inst) {
        visit(inst.dst);
        if(inst.dst instanceof VirtualRegister){
            if(!(inst.dst.spaceAllocatedTo instanceof PhysicalRegister)) throw new RuntimeException();
            allocatedInstList.add(inst);
        }
        else throw new RuntimeException();
        return null;
    }

    @Override
    public Void visit(ASMUnaryInstruction inst) {
        visit(inst.src);
        allocatedInstList.add(inst);
        return null;
    }

    @Override
    public Void visit(Operand op) {
        return op.accept(this);
    }

    @Override
    public Void visit(Address op) {
        return op.accept(this);
    }

    @Override
    public Void visit(Constant op) {
        return op.accept(this);
    }

    @Override
    public Void visit(Immediate op) {
        return null;
    }

    @Override
    public Void visit(Label op) {
        return op.accept(this);
    }

    @Override
    public Void visit(Memory op) {
        if(((VirtualRegister)(op.address)).spaceAllocatedTo == null)visit(op.address);
        VirtualRegister tbase = currentMethod.asmAllocateVirtualRegister(tmpBase);
        allocatedInstList.add(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, tbase, op.address));
        op.address = tbase;
        if(op.index != null && op.index instanceof VirtualRegister){
            if(((VirtualRegister) op.index).spaceAllocatedTo == null) visit(op.index);
            VirtualRegister tindex = currentMethod.asmAllocateVirtualRegister(tmpIndex);
            allocatedInstList.add(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, tindex, op.index));
            op.index = tindex;
        }
        return null;
    }

    @Override
    public Void visit(Register op) {
        return op.accept(this);
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
        if(op.spaceAllocatedTo == null) op.spaceAllocatedTo = allocateStackSpace();
        return null;
    }
}
