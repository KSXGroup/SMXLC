package kstarxin.nasm.allocator;

import kstarxin.compiler.Configure;
import kstarxin.ir.asmir.ASMBasicBlock;
import kstarxin.ir.asmir.ASMLevelIRInstruction.*;
import kstarxin.ir.asmir.ASMLevelIRMethod;
import kstarxin.ir.asmir.ASMLevelIRProgram;
import kstarxin.ir.operand.Immediate;
import kstarxin.ir.operand.physical.PhysicalRegister;
import kstarxin.nasm.PhysicalRegisterSet;
import kstarxin.utilities.NameMangler;
import kstarxin.utilities.OperatorTranslator;

import java.util.ArrayList;

public class StackBuilder {
    private ASMLevelIRProgram ir;
    private ArrayList<PhysicalRegister> usedCallee;
    private ArrayList<PhysicalRegister> usedCaller;

    public StackBuilder(ASMLevelIRProgram _ir){
        ir = _ir;
        usedCallee = new ArrayList<PhysicalRegister>();
        usedCaller = new ArrayList<PhysicalRegister>();
    }

    public void buildStackFrame(){
        for(ASMLevelIRMethod method : ir.getMethodMap().values()){
            if(method.isBuiltIn) continue;
            boolean isMain = method.name.equals(NameMangler.mainTrue);
            int trueStackAlign = method.stackAligned;
            if((-trueStackAlign) % 16 != 0) trueStackAlign -= Configure.PTR_SIZE;
            trueStackAlign -= Configure.CALL_STACK_ALIGN;
            usedCallee.clear();
            usedCallee.addAll(method.usedCalleeSavedRegister);
            ASMBasicBlock start = method.basicBlocks.getFirst();
            ASMBasicBlock end = method.basicBlocks.getLast();
            ASMReturnInstruction ret = (ASMReturnInstruction) end.insts.getLast();
            end.insts.removeLast();
            ASMLeaveInstruction leave = (ASMLeaveInstruction) end.insts.getLast();
            end.insts.removeLast();
            start.insts.addFirst(new ASMPushInstruction(start, method.asmAllocateVirtualRegister(PhysicalRegisterSet.RBP)));
            int sz = usedCallee.size();
            if(!isMain) for(int i = sz - 1; i >= 0; --i) start.insts.addFirst(new ASMPushInstruction(start, method.asmAllocateVirtualRegister(usedCallee.get(i))));
            start.insts.addFirst(new ASMBinaryInstruction(OperatorTranslator.NASMInstructionOperator.SUB, start, method.asmAllocateVirtualRegister(PhysicalRegisterSet.RSP), new Immediate(-trueStackAlign)));
            if(!isMain) for(int i = 0; i < sz; ++i) end.insts.addLast(new ASMPopInstruction(end, method.asmAllocateVirtualRegister(usedCallee.get(i))));
            end.insts.addLast(leave);
            end.insts.addLast(ret);
        }
    }
}
