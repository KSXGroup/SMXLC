package kstarxin.ir.asmir;

import kstarxin.compiler.Configure;
import kstarxin.ir.BasicBlock;
import kstarxin.ir.IRBaseVisitor;
import kstarxin.ir.IRProgram;
import kstarxin.ir.Method;
import kstarxin.ir.asmir.ASMLevelIRInstruction.*;
import kstarxin.ir.instruction.*;
import kstarxin.ir.operand.*;
import kstarxin.ir.operand.physical.*;
import kstarxin.nasm.Allocator;
import kstarxin.nasm.PhysicalRegisterSet;
import kstarxin.parser.MxStarParser;
import kstarxin.utilities.OperatorTranslator;
import java.util.*;

// This ir builder perform a naive instruction selection

//paramter passed via RDI, RSI, RDX, RCX, R8, R9

public class ASMLevelIRBuilder implements IRBaseVisitor<Void> {
    IRProgram                               mir;
    ASMLevelIRProgram                       lir;
    HashMap<BasicBlock, ASMBasicBlock>      visited;
    HashMap<String, ASMBasicBlock>          jumpMap;
    HashSet<ASMInstruction>                 jumpInsts;
    ASMLevelIRMethod                        currentMethod;
    ASMBasicBlock                           currentBasicBlock;
    ArrayList<PhysicalRegister>             parameterPassingPhysicalRegister;

    public ASMLevelIRBuilder(IRProgram _ir){
        mir                                 = _ir;
        lir                                 = new ASMLevelIRProgram();
        visited                             = new HashMap<BasicBlock, ASMBasicBlock>();
        jumpMap                             = new HashMap<String, ASMBasicBlock>();
        jumpInsts                           = new HashSet<ASMInstruction>();
        currentMethod                       = null;
        parameterPassingPhysicalRegister    = new ArrayList<PhysicalRegister>();
        parameterPassingPhysicalRegister.add(PhysicalRegisterSet.EDI);
        parameterPassingPhysicalRegister.add(PhysicalRegisterSet.ESI);
        parameterPassingPhysicalRegister.add(PhysicalRegisterSet.EDX);
        parameterPassingPhysicalRegister.add(PhysicalRegisterSet.ECX);
        parameterPassingPhysicalRegister.add(PhysicalRegisterSet.R8D);
        parameterPassingPhysicalRegister.add(PhysicalRegisterSet.R9D);
    }


    public ASMLevelIRProgram build(){
        processStaticData();
        mir.getMethodMap().values().forEach(this::visit);
        return lir;
    }

    private ASMBasicBlock processBasicBlock(BasicBlock bb){
        if(visited.containsKey(bb)) return visited.get(bb);
        ASMBasicBlock newBB = new ASMBasicBlock(currentMethod);
        visited.put(bb, newBB);
        currentBasicBlock = newBB;
        currentMethod.addBasicBlock(currentBasicBlock);
        Instruction irInst  = null;
        if(bb.blockLabel != null){
            ASMLabelInstruction label = new ASMLabelInstruction(bb.blockLabel, currentBasicBlock);
            currentBasicBlock.insertEnd(label);
            jumpMap.put(label.nasmName, currentBasicBlock);
        }
        for(irInst = bb.getBeginInst(); irInst.next != null; irInst = irInst.next) visit(irInst);
        if(irInst instanceof ConditionJumpInstruction){
            visit(irInst);
            ASMConditionalJumpInstruction cj = (ASMConditionalJumpInstruction)currentBasicBlock.insts.getLast();
            cj.trueTarget = processBasicBlock(((ConditionJumpInstruction) irInst).trueTarget);
            cj.falseTarget = processBasicBlock(((ConditionJumpInstruction) irInst).falseTarget);
        }
        else if(irInst instanceof DirectJumpInstruction){
            visit(irInst);
            ASMDirectJumpInstruction dj = (ASMDirectJumpInstruction) currentBasicBlock.insts.getLast();
            dj.target = processBasicBlock(((DirectJumpInstruction) irInst).target);
        }
        else if(irInst instanceof ReturnInstruction) visit(irInst);
        else throw new RuntimeException();
        return newBB;
    }

    private void processStaticData(){
        mir.getGlobalVariableMap().forEach((k,v)->{
            if(v instanceof StaticString){
                if(((StaticString) v).value != null) lir.addRomData(new ASMRomDataInstruction(k, ((StaticString) v).value));
                else lir.addBSSData(new ASMRESInstruction(k, Configure.PTR_SIZE));
            } else if(v instanceof StaticPointer){
                if(((StaticPointer) v).value != 0) lir.addDData(new ASMDInstruction(k, ((StaticPointer) v).value));
                else lir.addBSSData(new ASMRESInstruction(k, Configure.PTR_SIZE));
            }
        });
    }

    @Override
    public Void visit(Method method) {
        currentMethod = new ASMLevelIRMethod(method.hintName, method.tmpRegisterCounter);
        processBasicBlock(method.startBlock);
        if(method == mir.getInitMethod()) lir.setGlobalInit(currentMethod);
        return null;
    }

    @Override
    public Void visit(BinaryArithmeticInstruction inst) {
        VirtualRegister tmp = currentMethod.asmAllocateVirtualRegister();
        switch (inst.op){
            case MxStarParser.EQ:
            case MxStarParser.NEQ:
            case MxStarParser.GE:
            case MxStarParser.GT:
            case MxStarParser.LE:
            case MxStarParser.LT:
                if(inst.lhs instanceof Address && inst.rhs instanceof  Address) {
                    currentBasicBlock.insertEnd(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, tmp, visit(inst.lhs)));
                    currentBasicBlock.insertEnd(new ASMCompareInstruction(currentBasicBlock, tmp, visit(inst.rhs)));
                }else currentBasicBlock.insertEnd(new ASMCompareInstruction(currentBasicBlock, visit(inst.lhs), visit(inst.rhs)));
                VirtualRegister virtualAL = currentMethod.asmAllocateVirtualRegister();
                virtualAL.spaceAllocatedTo = PhysicalRegisterSet.AL;
                switch (inst.op){
                    case MxStarParser.EQ:
                        currentBasicBlock.insertEnd(new ASMSetInstruction(OperatorTranslator.NASMInstructionOperator.SETE, currentBasicBlock, virtualAL));
                        break;
                    case MxStarParser.NEQ:
                        currentBasicBlock.insertEnd(new ASMSetInstruction(OperatorTranslator.NASMInstructionOperator.SETNE, currentBasicBlock, virtualAL));
                        break;
                    case MxStarParser.GE:
                        currentBasicBlock.insertEnd(new ASMSetInstruction(OperatorTranslator.NASMInstructionOperator.SETGE, currentBasicBlock, virtualAL));
                        break;
                    case MxStarParser.GT:
                        currentBasicBlock.insertEnd(new ASMSetInstruction(OperatorTranslator.NASMInstructionOperator.SETG, currentBasicBlock, virtualAL));
                        break;
                    case MxStarParser.LE:
                        currentBasicBlock.insertEnd(new ASMSetInstruction(OperatorTranslator.NASMInstructionOperator.SETLE, currentBasicBlock, virtualAL));
                        break;
                    case MxStarParser.LT:
                        currentBasicBlock.insertEnd(new ASMSetInstruction(OperatorTranslator.NASMInstructionOperator.SETL, currentBasicBlock, virtualAL));
                        break;
                }
                currentBasicBlock.insertEnd(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOVZX, currentBasicBlock, visit(inst.target), virtualAL));
                break;
            case MxStarParser.MOD:
            case MxStarParser.DIV:
                VirtualRegister virtualEAX = currentMethod.asmAllocateVirtualRegister();
                virtualEAX.spaceAllocatedTo = PhysicalRegisterSet.EAX;
                currentBasicBlock.insertEnd(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, virtualEAX, visit(inst.lhs)));
                currentBasicBlock.insertEnd(new ASMCDQInstruction(currentBasicBlock));
                currentBasicBlock.insertEnd(new ASMUnaryInstruction(OperatorTranslator.NASMInstructionOperator.IDIV, currentBasicBlock, visit(inst.rhs)));
                if(inst.op == MxStarParser.DIV) currentBasicBlock.insertEnd(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, visit(inst.target), virtualEAX));
                else{
                    VirtualRegister virtualEDX = currentMethod.asmAllocateVirtualRegister();
                    virtualEDX.spaceAllocatedTo = PhysicalRegisterSet.EDX;
                    currentBasicBlock.insertEnd(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, visit(inst.target), virtualEDX));
                }
                break;
            default:
                currentBasicBlock.insertEnd(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, tmp, visit(inst.lhs)));
                currentBasicBlock.insertEnd(new ASMBinaryInstruction(OperatorTranslator.toNASMOperator(inst.op), currentBasicBlock, tmp, visit(inst.rhs)));
                currentBasicBlock.insertEnd(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, visit(inst.target), tmp));
                break;
        }
        return null;
    }

    @Override
    public Void visit(CallInstruction inst) {
        int paraSize = inst.parameters.size();
        paraSize--;
        int isClassMember = inst.isClassMemberCall ? 1 : 0;
        for(;paraSize > (5 - isClassMember); --paraSize) currentBasicBlock.insertEnd(new ASMPushInstruction(currentBasicBlock, visit(inst.parameters.get(paraSize))));
        for(int i = 5;paraSize >= 0 && i >= 0; --i,--paraSize){
            VirtualRegister tmp = currentMethod.asmAllocateVirtualRegister();
            tmp.spaceAllocatedTo = parameterPassingPhysicalRegister.get(i);
            currentBasicBlock.insertEnd(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, tmp, visit(inst.parameters.get(paraSize))));
        }
        if(isClassMember == 1) currentBasicBlock.insertEnd(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, parameterPassingPhysicalRegister.get(0), visit(inst.classThisPointer)));
        return null;
    }

    @Override
    public Void visit(CompareInstruction inst) {
        currentBasicBlock.insertEnd(new ASMCompareInstruction(currentBasicBlock, visit(inst.lhs), visit(inst.rhs)));
        return null;
    }

    @Override
    public Void visit(ConditionJumpInstruction inst) {
        OperatorTranslator.NASMInstructionOperator asmOp = null;
        switch (inst.op){
            case MxStarParser.EQ:
                asmOp = OperatorTranslator.NASMInstructionOperator.JE;
                break;
            case MxStarParser.NEQ:
                asmOp = OperatorTranslator.NASMInstructionOperator.JNE;
                break;
            case MxStarParser.GT:
                asmOp = OperatorTranslator.NASMInstructionOperator.JG;
                break;
            case MxStarParser.LT:
                asmOp = OperatorTranslator.NASMInstructionOperator.JL;
                break;
            case MxStarParser.LE:
                asmOp = OperatorTranslator.NASMInstructionOperator.JLE;
                break;
            case MxStarParser.GE:
                asmOp = OperatorTranslator.NASMInstructionOperator.JGE;
                break;
            default:
                throw new RuntimeException("what shit opeator do you in conditional jump ??????");
        }
        currentBasicBlock.insertEnd(new ASMConditionalJumpInstruction(asmOp , currentBasicBlock,  null, null));
        return null;
    }

    @Override
    public Void visit(DirectJumpInstruction inst) {
        currentBasicBlock.insertEnd(new ASMDirectJumpInstruction(currentBasicBlock, null));
        return null;
    }

    @Override
    public Void visit(Instruction inst) {
        return inst.accept(this);
    }

    @Override
    public Void visit(JumpInstruction inst) {
        return inst.accept(this);
    }

    @Override
    public Void visit(LoadInstruction inst) {
        currentBasicBlock.insertEnd(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, visit(inst.dest), visit(inst.src)));
        return null;
    }

    @Override
    public Void visit(MoveInstruction inst) {
        currentBasicBlock.insertEnd(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, visit(inst.dest), visit(inst.src)));
        return null;
    }

    @Override
    public Void visit(ReturnInstruction inst) {
        currentBasicBlock.insertEnd(new ASMLeaveInstruction(currentBasicBlock));
        currentBasicBlock.insertEnd(new ASMReturnInstruction(currentBasicBlock));
        return null;
    }

    @Override
    public Void visit(StoreInstruction inst) {
        currentBasicBlock.insertEnd(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, visit(inst.dest), visit(inst.src)));
        return null;
    }

    @Override
    public Void visit(UnaryInstruction inst) {
        return null;
    }

    @Override
    public Operand visit(Operand operand) {
        return operand.accept(this);
    }

    @Override
    public Operand visit(PhysicalRegister operand) {
        throw new RuntimeException("no direct use of physical!");
    }

    @Override
    public Operand visit(StackSpace operand) {
        throw new RuntimeException("no direct use of physical!");
    }

    @Override
    public Operand visit(Address operand) {
        return operand.accept(this);
    }

    @Override
    public Operand visit(Constant operand) {
        return operand.accept(this);
    }

    @Override
    public Operand visit(Immediate operand) {
        return operand;
    }

    @Override
    public Operand visit(Label operand) {
        return operand.accept(this);
    }

    @Override
    public Operand visit(Memory operand) {
        return operand;
    }

    @Override
    public Operand visit(Register operand) {
        return operand.accept(this);
    }

    @Override
    public Operand visit(StaticString operand) {
        return operand;
    }

    @Override
    public Operand visit(StaticPointer operand) {
        return operand;
    }

    @Override
    public Operand visit(VirtualRegister operand) {
        return operand;
    }
}
