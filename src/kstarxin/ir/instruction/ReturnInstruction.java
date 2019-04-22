package kstarxin.ir.instruction;

import kstarxin.ir.IRBaseVisitor;
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
        else if(!(returnValue instanceof Immediate)){
            a = map.get(returnValue);
            if(a == null) throw new RuntimeException();
            returnValue = a;
        }
    }

    @Override
    public void collectDefUseInfo() {
        def.clear();
        use.clear();
        if (returnValue instanceof VirtualRegister)
            use.add((VirtualRegister) returnValue);
        else if (returnValue instanceof Memory) use.addAll(((Memory) returnValue).collectUseInfo());
    }

    @Override
    public Address replaceOperandForGlobalVariableOptimization(HashMap<Address, VirtualRegister> map) {
        if((returnValue instanceof StaticString && !((StaticString) returnValue).isConstantAllocatedByCompiler) || returnValue instanceof StaticPointer){
            returnValue = map.get(returnValue);
            if(returnValue == null) throw new RuntimeException();
        }
        return null;
    }

    @Override
    public <T> T accept(IRBaseVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
