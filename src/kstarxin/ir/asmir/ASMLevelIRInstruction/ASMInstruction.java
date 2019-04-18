package kstarxin.ir.asmir.ASMLevelIRInstruction;

import kstarxin.ir.IRBaseVisitor;
import kstarxin.ir.asmir.ASMBasicBlock;
import kstarxin.ir.asmir.ASMLevelIRMethod;
import kstarxin.ir.asmir.ASMLevelIRVisitor;
import kstarxin.ir.instruction.*;
import kstarxin.ir.operand.Address;
import kstarxin.ir.operand.Operand;
import kstarxin.ir.operand.VirtualRegister;

import java.util.HashMap;

public abstract class ASMInstruction extends Instruction {
    public ASMBasicBlock basicBlockBelongTo;
    public ASMInstruction(String name, ASMBasicBlock _bbBelongTo){
        super(name);
        basicBlockBelongTo = _bbBelongTo;
        prev = null;
        next = null;
    }
    //we use linked list in this level of ir to maintain the list
    //this is really cxk-like

    @Override
    public Instruction copy() {
        throw new RuntimeException();
    }

    @Override
    public void replaceOperandForInline(HashMap<Operand, Operand> map) {
        throw new RuntimeException();
    }

    @Override
    public void collectDefUseInfo() {
        //TODO
    }

    @Override
    public Address replaceOperandForGlobalVariableOptimization(HashMap<Address, VirtualRegister> map) {
        throw new RuntimeException();
    }

    @Override
    public final <T> T accept(IRBaseVisitor<T> visitor) {
        throw new RuntimeException();
    }

    abstract public <T> T accept(ASMLevelIRVisitor<T> asmVisitor);
}
