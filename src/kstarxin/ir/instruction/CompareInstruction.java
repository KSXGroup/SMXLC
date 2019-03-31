package kstarxin.ir.instruction;

import kstarxin.ir.operand.Memory;
import kstarxin.ir.operand.Operand;

import java.util.HashMap;

public class CompareInstruction extends Instruction {
    public Operand lhs;
    public Operand rhs;
    public CompareInstruction(Operand _lhs, Operand _rhs){
        super("cmp");
        lhs = _lhs;
        rhs = _rhs;
    }

    public void replaceLhs(Operand _lhs){
        lhs = _lhs;
    }

    public void replaceRhs(Operand _rhs){
        rhs = _rhs;
    }

    @Override
    public CompareInstruction copy() {
        Operand nlhs, nrhs;
        if(lhs instanceof Memory) nlhs = new Memory((Memory) lhs);
        else nlhs = lhs;
        if(rhs instanceof Memory) nrhs = new Memory((Memory) rhs);
        else nrhs = rhs;
        return new CompareInstruction(nlhs, nrhs);
    }

    @Override
    public void replaceOperandForInline(HashMap<Operand, Operand> map) {
        Operand a = null;
        if(lhs instanceof Memory) ((Memory) lhs).replaceOperandForInline(map);
        else{
             a = map.get(lhs);
             if(a == null) throw new RuntimeException();
             else lhs = a;
        }

        if(rhs instanceof Memory) ((Memory) rhs).replaceOperandForInline(map);
        else{
            a = map.get(rhs);
            if(a == null) throw new RuntimeException();
            else rhs = a;
        }
    }
}
