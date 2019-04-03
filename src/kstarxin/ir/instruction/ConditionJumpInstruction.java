package kstarxin.ir.instruction;

import kstarxin.ir.BasicBlock;
import kstarxin.ir.operand.Address;
import kstarxin.ir.operand.Operand;
import kstarxin.ir.operand.VirtualRegister;

import java.util.HashMap;

public class ConditionJumpInstruction extends Instruction {
    public int op;
    public BasicBlock trueTarget;
    public BasicBlock falseTarget;
    public ConditionJumpInstruction(int _op, BasicBlock _trueTarget, BasicBlock _falseTarget){
        super("cjump");
        op = _op;
        trueTarget = _trueTarget;
        falseTarget = _falseTarget;
    }

    public void replaceOperatorWith(int _operator){
        op = _operator;
    }

    public void replaceTrueTargetWith(BasicBlock bb){
        trueTarget = bb;
    }

    public void replaceFalseTargetWith(BasicBlock bb){
        falseTarget = bb;
    }

    @Override
    public ConditionJumpInstruction copy() {
        return new ConditionJumpInstruction(op, trueTarget, falseTarget);
    }

    @Override
    public void replaceOperandForInline(HashMap<Operand, Operand> map) {
        //DO NOTHING
    }

    @Override
    public void collectDefUseInfo() {
        //do nothing
    }

    @Override
    public Address replaceOperandForGlobalVariableOptimization(HashMap<Address, VirtualRegister> map) {
        return null;
    }
}
