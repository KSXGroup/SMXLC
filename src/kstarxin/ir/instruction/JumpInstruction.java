package kstarxin.ir.instruction;

import kstarxin.ir.IRBaseVisitor;

public abstract class JumpInstruction extends Instruction {
    public JumpInstruction(String _hintName){
        super(_hintName);
    }

    @Override
    abstract public <T> T accept(IRBaseVisitor<T> visitor);
}
