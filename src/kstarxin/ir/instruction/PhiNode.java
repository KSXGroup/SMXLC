package kstarxin.ir.instruction;

import kstarxin.ir.operand.Operand;

import java.util.HashMap;

//maybe abandoned for not use SSA
public class PhiNode extends Instruction{
    public PhiNode(){
        super("phi");
    }

    @Override
    public PhiNode copy() {
        return null;
    }

    @Override
    public void replaceOperandForInline(HashMap<Operand, Operand> map) {

    }
}
