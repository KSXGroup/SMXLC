package kstarxin.ir.instruction;

import kstarxin.ir.BasicBlock;

public class DirectJumpInstruction extends Instruction {
    public BasicBlock target;
    public DirectJumpInstruction(BasicBlock _target) {
        super("jmp");
        target = _target;
    }

    public void replaceTargetWith(BasicBlock _target){
        target = _target;
    }
}
