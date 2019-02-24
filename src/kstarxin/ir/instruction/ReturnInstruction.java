package kstarxin.ir.instruction;

import kstarxin.ir.operand.Operand;

public class ReturnInstruction extends JumpInstruction {
    private Operand returnValue;
    public  ReturnInstruction(Operand _returnValue){
        super("ret");
        returnValue = _returnValue;
    }
}
