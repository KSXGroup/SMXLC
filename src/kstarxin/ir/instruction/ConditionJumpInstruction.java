package kstarxin.ir.instruction;

import kstarxin.ir.BasicBlock;

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
}
