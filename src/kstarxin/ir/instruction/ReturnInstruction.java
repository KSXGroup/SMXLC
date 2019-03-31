package kstarxin.ir.instruction;

import kstarxin.ir.operand.*;

import java.util.HashMap;

public class ReturnInstruction extends JumpInstruction {
    public Operand returnValue;
    public  ReturnInstruction(Operand _returnValue){
        super("ret");
        returnValue = _returnValue;
    }

    @Override
    public ReturnInstruction copy() {
        if(returnValue instanceof Memory) return new ReturnInstruction(new Memory((Memory) returnValue));
        else return new ReturnInstruction(returnValue);
    }

    @Override
    public void replaceOperandForInline(HashMap<Operand, Operand> map) {
        Operand a = null;
        if(returnValue instanceof Memory) ((Memory) returnValue).replaceOperandForInline(map);
        else{
            a = map.get(returnValue);
            if(a == null) throw new RuntimeException();
            returnValue = a;
        }
    }
}
