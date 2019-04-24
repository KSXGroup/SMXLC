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
import kstarxin.nasm.PhysicalRegisterSet;
import kstarxin.parser.MxStarParser;
import kstarxin.utilities.NameMangler;
import kstarxin.utilities.OperatorTranslator;
import java.util.*;

// This ir builder perform a naive instruction selection

//paramter passed via RDI, RSI, RDX, RCX, R8, R9

public class ASMLevelIRBuilder implements IRBaseVisitor<Void> {

    IRProgram                               mir;
    ASMBasicBlock                           currentBasicBlock;
    ASMLevelIRMethod                        currentMethod;
    ASMLevelIRProgram                       lir;
    ArrayList<PhysicalRegister>             parameterPassingPhysicalRegister;
    HashMap<BasicBlock, ASMBasicBlock>      visited;

    public ASMLevelIRBuilder(IRProgram _ir){
        mir                                 = _ir;
        lir                                 = new ASMLevelIRProgram();
        visited                             = new HashMap<BasicBlock, ASMBasicBlock>();
        currentMethod                       = null;
        parameterPassingPhysicalRegister    = new ArrayList<PhysicalRegister>();
        parameterPassingPhysicalRegister.add(PhysicalRegisterSet.RDI);
        parameterPassingPhysicalRegister.add(PhysicalRegisterSet.RSI);
        parameterPassingPhysicalRegister.add(PhysicalRegisterSet.RDX);
        parameterPassingPhysicalRegister.add(PhysicalRegisterSet.RCX);
        parameterPassingPhysicalRegister.add(PhysicalRegisterSet.R8);
        parameterPassingPhysicalRegister.add(PhysicalRegisterSet.R9);
    }


    public ASMLevelIRProgram build(){
        processStaticData();
        mir.getMethodMap().values().forEach(method -> {
            ASMLevelIRMethod asmm = new ASMLevelIRMethod(method.hintName, method.tmpRegisterCounter);
            if(method.isBuiltin) asmm.isBuiltIn = true;
            if(method.classThisPointer != null) asmm.thisPointer = method.classThisPointer;
            lir.addASMMethod(method, asmm);
        });
        mir.getMethodMap().values().forEach(this::visit);
        return lir;
    }

    private ASMBasicBlock processBasicBlock(BasicBlock bb){
        if(visited.containsKey(bb)) return visited.get(bb);
        ASMBasicBlock newBB = new ASMBasicBlock(currentMethod, bb.blockLabel);
        visited.put(bb, newBB);
        currentBasicBlock = newBB;
        currentMethod.addBasicBlock(currentBasicBlock);
        Instruction irInst  = null;
        for(irInst = bb.getBeginInst(); irInst.next != null; irInst = irInst.next) visit(irInst);
        if(irInst instanceof ConditionJumpInstruction){
            visit(irInst);
            ASMConditionalJumpInstruction cj = (ASMConditionalJumpInstruction)currentBasicBlock.insts.getLast();
            if(visited.containsKey(((ConditionJumpInstruction) irInst).trueTarget))
                currentBasicBlock.insertEnd(new ASMDirectJumpInstruction(currentBasicBlock, visited.get(((ConditionJumpInstruction) irInst).trueTarget)));
            else cj.trueTarget = processBasicBlock(((ConditionJumpInstruction) irInst).trueTarget);
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
                if(((StaticString) v).value != null){
                    lir.addRomData(new ASMRomDataInstruction(NameMangler.STRING_LITERAL_PRE + k, ((StaticString) v).parsedValue));
                    if(!((StaticString) v).isConstantAllocatedByCompiler)
                        lir.addBSSData(new ASMRESInstruction(k, Configure.PTR_SIZE));
                }
                else lir.addBSSData(new ASMRESInstruction(k, Configure.PTR_SIZE));
            } else if(v instanceof StaticPointer){
                if(((StaticPointer) v).value != 0) lir.addDData(new ASMDInstruction(k, ((StaticPointer) v).value));
                else lir.addBSSData(new ASMRESInstruction(k, Configure.PTR_SIZE));
            }
        });
    }

    @Override
    public Void visit(Method method) {
        if(method.isBuiltin) return null;
        currentMethod = lir.getMethodMap().get(method);
        visited.clear();
        processBasicBlock(method.startBlock);
        ASMBasicBlock firstBB = currentMethod.basicBlocks.getFirst();
        if(method == mir.getInitMethod()){
            mir.getGlobalVariableMap().values().forEach(gv->{
                if(gv instanceof StaticString && !((StaticString) gv).isConstantAllocatedByCompiler && ((StaticString) gv).value != null){
                    firstBB.insts.addFirst(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, firstBB, gv, new StringLiteral(NameMangler.STRING_LITERAL_PRE + gv.hintName)));
                }
            });
            lir.setGlobalInit(currentMethod);
        }else if(method != mir.getMethod(NameMangler.mainMethodName) && method.parameters.size() > 0){
            int startPos = 0;
            VirtualRegister virtualPhysicalReg = null;
            if(method.classThisPointer != null){
                virtualPhysicalReg = currentMethod.asmAllocateVirtualRegister();
                virtualPhysicalReg.spaceAllocatedTo = parameterPassingPhysicalRegister.get(0);
                startPos++;
                firstBB.insts.addFirst(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, firstBB, method.classThisPointer, virtualPhysicalReg));
            }
            int i = startPos;
            int j = 0;
            for(; i < parameterPassingPhysicalRegister.size() && j < method.parameters.size(); ++i, ++j){
                virtualPhysicalReg = currentMethod.asmAllocateVirtualRegister();
                virtualPhysicalReg.spaceAllocatedTo = parameterPassingPhysicalRegister.get(i);
                firstBB.insts.addFirst(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, firstBB, method.parameters.get(j), virtualPhysicalReg));
            }
            for(;j < method.parameters.size(); ++j){
                virtualPhysicalReg = currentMethod.asmAllocateVirtualRegister();
                virtualPhysicalReg.spaceAllocatedTo = new StackSpace(PhysicalRegisterSet.RBP, Configure.PTR_SIZE + (j - 5) * Configure.PTR_SIZE);
                firstBB.insts.addFirst(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, firstBB, method.parameters.get(j), virtualPhysicalReg));
            }
        }else if(method == mir.getMethod(NameMangler.mainMethodName)) firstBB.insts.addFirst(new ASMCallInstruction(firstBB, lir.getMethodMap().get(mir.getInitMethod())));
        return null;
    }

    @Override
    public Void visit(BinaryArithmeticInstruction inst) {
        switch (inst.op){
            case MxStarParser.EQ:
            case MxStarParser.NEQ:
            case MxStarParser.GE:
            case MxStarParser.GT:
            case MxStarParser.LE:
            case MxStarParser.LT:
                if(inst.lhs instanceof Address && inst.rhs instanceof  Address) {
                    VirtualRegister tmp = currentMethod.asmAllocateVirtualRegister();
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
                    default:
                        throw new RuntimeException();
                }
                currentBasicBlock.insertEnd(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOVZX, currentBasicBlock, visit(inst.target), virtualAL));
                break;
            case MxStarParser.MOD:
            case MxStarParser.DIV:
                VirtualRegister virtualEAX = currentMethod.asmAllocateVirtualRegister();
                virtualEAX.spaceAllocatedTo = PhysicalRegisterSet.RAX;
                currentBasicBlock.insertEnd(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, virtualEAX, visit(inst.lhs)));
                currentBasicBlock.insertEnd(new ASMCDQInstruction(currentBasicBlock));
                if(inst.rhs instanceof Immediate) {
                    VirtualRegister tmp = currentMethod.asmAllocateVirtualRegister();
                    currentBasicBlock.insertEnd(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, tmp, visit(inst.rhs)));
                    currentBasicBlock.insertEnd(new ASMUnaryInstruction(OperatorTranslator.NASMInstructionOperator.IDIV, currentBasicBlock,tmp));
                }else currentBasicBlock.insertEnd(new ASMUnaryInstruction(OperatorTranslator.NASMInstructionOperator.IDIV, currentBasicBlock, visit(inst.rhs)));
                if(inst.op == MxStarParser.DIV) currentBasicBlock.insertEnd(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, visit(inst.target), virtualEAX));
                else{
                    VirtualRegister virtualEDX = currentMethod.asmAllocateVirtualRegister();
                    virtualEDX.spaceAllocatedTo = PhysicalRegisterSet.RDX;
                    currentBasicBlock.insertEnd(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, visit(inst.target), virtualEDX));
                }
                break;
            case MxStarParser.SFTL:
            case MxStarParser.SFTR:
                OperatorTranslator.NASMInstructionOperator op = OperatorTranslator.NASMInstructionOperator.SHL;
                if (inst.op == MxStarParser.SFTR) op = OperatorTranslator.NASMInstructionOperator.SAR;
                if(!(inst.rhs instanceof Immediate)) {
                    VirtualRegister virtualCL = currentMethod.asmAllocateVirtualRegister(PhysicalRegisterSet.CL);
                    Operand target = visit(inst.target);
                    currentBasicBlock.insertEnd(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, virtualCL, visit(inst.rhs)));
                    currentBasicBlock.insertEnd(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, target, visit(inst.lhs)));
                    currentBasicBlock.insertEnd(new ASMBinaryInstruction(op, currentBasicBlock, target, virtualCL));
                }else{
                    Operand target = visit(inst.target);
                    currentBasicBlock.insertEnd(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, target, visit(inst.lhs)));
                    currentBasicBlock.insertEnd(new ASMBinaryInstruction(OperatorTranslator.toNASMOperator(inst.op), currentBasicBlock, target, visit(inst.rhs)));
                }
                break;
            default:
                currentBasicBlock.insertEnd(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, visit(inst.target), visit(inst.lhs)));
                currentBasicBlock.insertEnd(new ASMBinaryInstruction(OperatorTranslator.toNASMOperator(inst.op), currentBasicBlock, visit(inst.target), visit(inst.rhs)));
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
        for(int i = paraSize; i >= 0; --i){
            VirtualRegister tmp = currentMethod.asmAllocateVirtualRegister();
            tmp.spaceAllocatedTo = parameterPassingPhysicalRegister.get(i + isClassMember);
            currentBasicBlock.insertEnd(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, tmp, visit(inst.parameters.get(i))));
        }
        if(isClassMember == 1){
            VirtualRegister tmpClassPtr = currentMethod.asmAllocateVirtualRegister(parameterPassingPhysicalRegister.get(0));
            currentBasicBlock.insertEnd(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, tmpClassPtr, visit(inst.classThisPointer)));
        }
        currentBasicBlock.insertEnd(new ASMCallInstruction(currentBasicBlock, lir.getMethodMap().get(inst.callee)));
        if(inst.returnValue != null) {
            VirtualRegister veax = currentMethod.asmAllocateVirtualRegister();
            veax.spaceAllocatedTo = PhysicalRegisterSet.RAX;
            currentBasicBlock.insertEnd(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, inst.returnValue, veax));
        }
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
                asmOp = OperatorTranslator.NASMInstructionOperator.JNE;
                break;
            case MxStarParser.NEQ:
                asmOp = OperatorTranslator.NASMInstructionOperator.JE;
                break;
            case MxStarParser.GT:
                asmOp = OperatorTranslator.NASMInstructionOperator.JLE;
                break;
            case MxStarParser.LT:
                asmOp = OperatorTranslator.NASMInstructionOperator.JGE;
                break;
            case MxStarParser.LE:
                asmOp = OperatorTranslator.NASMInstructionOperator.JG;
                break;
            case MxStarParser.GE:
                asmOp = OperatorTranslator.NASMInstructionOperator.JL;
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
        if(inst.src instanceof StaticString && (((StaticString) inst.src).isConstantAllocatedByCompiler)){
            StringLiteral sl = new StringLiteral(NameMangler.STRING_LITERAL_PRE + inst.src.hintName);
            currentBasicBlock.insertEnd(new ASMLEAInstruction(currentBasicBlock, visit(inst.dest), sl));
        }
        else currentBasicBlock.insertEnd(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, visit(inst.dest), visit(inst.src)));
        return null;
    }

    @Override
    public Void visit(MoveInstruction inst) {
        currentBasicBlock.insertEnd(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, visit(inst.dest), visit(inst.src)));
        return null;
    }

    @Override
    public Void visit(ReturnInstruction inst) {
        //function return on eax
        VirtualRegister retVeg = currentMethod.asmAllocateVirtualRegister();
        retVeg.spaceAllocatedTo = PhysicalRegisterSet.RAX;
        if(inst.returnValue instanceof StaticString && ((StaticString) inst.returnValue).isConstantAllocatedByCompiler)
            currentBasicBlock.insertEnd(new ASMLEAInstruction(currentBasicBlock, retVeg, inst.returnValue));
        else currentBasicBlock.insertEnd(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, retVeg, inst.returnValue));
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
        if(inst.src == inst.dest) {
            switch (inst.op){
                case MxStarParser.SUB:
                    currentBasicBlock.insertEnd(new ASMUnaryInstruction(OperatorTranslator.NASMInstructionOperator.NEG, currentBasicBlock, visit(inst.src)));
                    break;
                case MxStarParser.BITNOT:
                case MxStarParser.NOT:
                    currentBasicBlock.insertEnd(new ASMUnaryInstruction(OperatorTranslator.NASMInstructionOperator.NOT, currentBasicBlock, visit(inst.src)));
                    break;
                case MxStarParser.INC:
                    currentBasicBlock.insertEnd(new ASMUnaryInstruction(OperatorTranslator.NASMInstructionOperator.INC, currentBasicBlock, visit(inst.src)));
                    break;
                case MxStarParser.DEC:
                    currentBasicBlock.insertEnd(new ASMUnaryInstruction(OperatorTranslator.NASMInstructionOperator.DEC, currentBasicBlock, visit(inst.src)));
                    break;
                default:
                    throw new RuntimeException();
            }
        }else{
            switch (inst.op){
                case MxStarParser.SUB:
                    currentBasicBlock.insertEnd(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, visit(inst.dest), visit(inst.src)));
                    currentBasicBlock.insertEnd(new ASMUnaryInstruction(OperatorTranslator.NASMInstructionOperator.NEG, currentBasicBlock, visit(inst.dest)));
                    break;
                case MxStarParser.BITNOT:
                case MxStarParser.NOT:
                    currentBasicBlock.insertEnd(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, visit(inst.dest), visit(inst.src)));
                    currentBasicBlock.insertEnd(new ASMUnaryInstruction(OperatorTranslator.NASMInstructionOperator.NOT, currentBasicBlock, visit(inst.dest)));
                    if(inst.op == MxStarParser.NOT) currentBasicBlock.insertEnd(new ASMBinaryInstruction(OperatorTranslator.NASMInstructionOperator.AND,currentBasicBlock, visit(inst.dest), new Immediate(1)));
                    break;
                case MxStarParser.INC:
                    currentBasicBlock.insertEnd(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, visit(inst.dest), visit(inst.src)));
                    currentBasicBlock.insertEnd(new ASMUnaryInstruction(OperatorTranslator.NASMInstructionOperator.INC, currentBasicBlock, visit(inst.dest)));
                    break;
                case MxStarParser.DEC:
                    currentBasicBlock.insertEnd(new ASMMoveInstruction(OperatorTranslator.NASMInstructionOperator.MOV, currentBasicBlock, visit(inst.dest), visit(inst.src)));
                    currentBasicBlock.insertEnd(new ASMUnaryInstruction(OperatorTranslator.NASMInstructionOperator.DEC, currentBasicBlock, visit(inst.dest)));
                    break;
                default:
                    throw new RuntimeException();
            }
        }
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
