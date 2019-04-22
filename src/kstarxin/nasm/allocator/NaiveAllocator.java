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
    private static PhysicalRegister             tmpReg = PhysicalRegisterSet.RAX;
    private static PhysicalRegister             tmpBase =PhysicalRegisterSet.RBX;
    private static PhysicalRegister             tmpIndex = PhysicalRegisterSet.RCX;

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
                for (ASMBasicBlock ab : m.basicBlocks) {
                    allocatedInstList.clear();
                    currentBasicBlock = ab;
                    ab.insts.forEach(this::visit);
                    ab.insts.clear();
                    ab.insts.addAll(allocatedInstList);
                }
                ASMBasicBlock firstBB = m.basicBlocks.getFirst();
                firstBB.insts.addFirst(new ASMBinaryInstruction(OperatorTranslator.NASMInstructionOperator.SUB, currentBasicBlock, currentMethod.asmAllocateVirtualRegister(PhysicalRegisterSet.RSP), new Immediate(-offsetInStack + 8)));
                firstBB.insts.addFirst(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, currentMethod.asmAllocateVirtualRegister(PhysicalRegisterSet.RBP), currentMethod.asmAllocateVirtualRegister(PhysicalRegisterSet.RSP)));
                firstBB.insts.addFirst(new ASMPushInstruction(currentBasicBlock, currentMethod.asmAllocateVirtualRegister(PhysicalRegisterSet.RBP)));
                lir.ifAllocated = true;
            }
        }
    }

    @Override
    public Void visit(ASMInstruction inst) {
        return inst.accept(this);
    }

    @Override
    public Void visit(ASMBinaryInstruction inst) {
        visit(inst.src);
        visit(inst.dst);
        if(inst.src instanceof VirtualRegister){
            if(((VirtualRegister) inst.src).spaceAllocatedTo instanceof StackSpace){
                VirtualRegister vpreg = currentMethod.asmAllocateVirtualRegister(tmpReg);
                allocatedInstList.add(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV,currentBasicBlock, vpreg, inst.src));
                inst.src = vpreg;
                allocatedInstList.add(inst);
                return null;
            }else{
                allocatedInstList.add(inst);
                return null;
            }
        }else if(inst.src instanceof Memory){
            if(inst.dst instanceof Memory){
                VirtualRegister vpreg = currentMethod.asmAllocateVirtualRegister(tmpReg);
                allocatedInstList.add(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, vpreg, inst.src));
                inst.src = vpreg;
                allocatedInstList.add(inst);
                return null;
            }
            else if(inst.dst instanceof VirtualRegister){
                if(!(((VirtualRegister) inst.dst).spaceAllocatedTo instanceof PhysicalRegister)){
                    VirtualRegister vpreg = currentMethod.asmAllocateVirtualRegister(tmpReg);
                    allocatedInstList.add(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, vpreg, inst.src));
                    inst.src = vpreg;
                    allocatedInstList.add(inst);
                    return null;
                }
            }else{
                allocatedInstList.add(inst);
                return null;
            }
        }
        else if(inst.src instanceof Immediate) allocatedInstList.add(inst);
        else throw new RuntimeException();
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
        visit(inst.rhs);
        if(inst.rhs instanceof VirtualRegister && !(((VirtualRegister) inst.rhs).spaceAllocatedTo instanceof PhysicalRegister)){
            if((inst.lhs instanceof Memory || (inst.lhs instanceof VirtualRegister && ((VirtualRegister) inst.lhs).spaceAllocatedTo instanceof StackSpace)) || inst.lhs instanceof Memory){
                VirtualRegister pvreg = currentMethod.asmAllocateVirtualRegister(tmpReg);
                allocatedInstList.add(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, tmpReg, inst.rhs));
                inst.rhs = pvreg;
                allocatedInstList.add(inst);
            }else allocatedInstList.add(inst);
        }else allocatedInstList.add(inst);
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
            VirtualRegister tdst = currentMethod.asmAllocateVirtualRegister(tmpReg);
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
            VirtualRegister pvreg = currentMethod.asmAllocateVirtualRegister(tmpReg);
            allocatedInstList.add(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, pvreg, inst.src));
            inst.src = pvreg;
            allocatedInstList.add(inst);
        }else allocatedInstList.add(inst);
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
        ((VirtualRegister)(op.address)).spaceAllocatedTo = allocateStackSpace();
        VirtualRegister tbase = currentMethod.asmAllocateVirtualRegister(tmpBase);
        allocatedInstList.add(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, tbase, op.address));
        op.address = tbase;
        if(op.index != null && op.index instanceof VirtualRegister){
            ((VirtualRegister) op.index).spaceAllocatedTo = allocateStackSpace();
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
