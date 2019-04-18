package kstarxin.ir.instruction;

import kstarxin.ir.IRBaseVisitor;
import kstarxin.ir.operand.*;
import kstarxin.utilities.*;

import java.util.HashMap;

public class BinaryArithmeticInstruction extends Instruction {
    public int op;
    public Operand lhs;
    public Operand rhs;
    public Operand target;

    private String opSymbolic;
    private String opLiteral;

    public BinaryArithmeticInstruction(int _op, Operand _target, Operand _lhs, Operand _rhs){
        super("binArith");
        op          = _op;
        opSymbolic  = OperatorTranslator.operatorToSymbolicName(_op);
        opLiteral   = OperatorTranslator.operatorToLiteralName(_op);
        lhs         = _lhs;
        rhs         = _rhs;
        target      = _target;
    }

    @Override
    public BinaryArithmeticInstruction copy() {
        Operand nlhs, nrhs, ntarget;
        if(lhs instanceof Memory) nlhs = new Memory((Memory) lhs);
        else nlhs = lhs;
        if(rhs instanceof Memory) nrhs = new Memory((Memory) rhs);
        else nrhs = rhs;
        if(target instanceof Memory) ntarget = new Memory((Memory) target);
        else ntarget = target;
        return new BinaryArithmeticInstruction(op, ntarget, nlhs, nrhs);
    }

    @Override
    public void replaceOperandForInline(HashMap<Operand, Operand> map) {
        Operand a = null;
        if(lhs instanceof Memory) ((Memory) lhs).replaceOperandForInline(map);
        else if(!(lhs instanceof Immediate)){
            a = map.get(lhs);
            if(a == null) throw new RuntimeException();
            else lhs = a;
        }

        if(rhs instanceof Memory) ((Memory) rhs).replaceOperandForInline(map);
        else if(!(rhs instanceof Immediate)){
            a = map.get(rhs);
            if(a == null)
                throw new RuntimeException();
            else rhs = a;
        }

        if(target instanceof Memory) ((Memory) target).replaceOperandForInline(map);
        else {
            a = map.get(target);
            if(a == null)
                throw new RuntimeException();
            else target = a;
        }
    }

    @Override
    public void collectDefUseInfo() {
        def.clear();
        use.clear();
        if(lhs instanceof VirtualRegister) use.add((VirtualRegister) lhs);
        else if(lhs instanceof Memory) use.addAll(((Memory) lhs).collectUseInfo());
        if(rhs instanceof VirtualRegister) use.add((VirtualRegister) rhs);
        else if(rhs instanceof Memory) use.addAll(((Memory) rhs).collectUseInfo());
        if(target instanceof VirtualRegister) def.add((VirtualRegister) target);
        else if(target instanceof Memory) use.addAll(((Memory) target).collectUseInfo());
    }

    @Override
    public Address replaceOperandForGlobalVariableOptimization(HashMap<Address, VirtualRegister> map) {
        if(rhs instanceof StaticString || rhs instanceof StaticPointer){
            rhs = map.get(rhs);
            if(rhs == null) throw new RuntimeException();
        }

        if(lhs instanceof StaticString || lhs instanceof StaticPointer){
            lhs = map.get(lhs);
            if(lhs == null) throw new RuntimeException();
        }

        if(target instanceof StaticString || target instanceof StaticPointer){
            target = map.get(target);
            if(target == null) throw new RuntimeException();
            return (Address) target;
        }
        return null;
    }

    @Override
    public <T> T accept(IRBaseVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
