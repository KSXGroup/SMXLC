package kstarxin.ir.instruction;

import kstarxin.ir.BasicBlock;
import kstarxin.ir.operand.Operand;

import java.util.HashMap;

public class DirectJumpInstruction extends Instruction {
    public BasicBlock target;
    public DirectJumpInstruction(BasicBlock _target) {
        super("jmp");
        target = _target;
    }

    public void replaceTargetWith(BasicBlock _target){
        target = _target;
    }

    @Override
    public DirectJumpInstruction copy() {
        return new DirectJumpInstruction(target);
    }

    @Override
    public void replaceOperandForInline(HashMap<Operand, Operand> map) {
        //DO NOTHING
    }
}
