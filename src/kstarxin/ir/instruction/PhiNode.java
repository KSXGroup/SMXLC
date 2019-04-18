package kstarxin.ir.instruction;

import kstarxin.ir.IRBaseVisitor;
import kstarxin.ir.operand.Address;
import kstarxin.ir.operand.Operand;
import kstarxin.ir.operand.VirtualRegister;

import java.util.HashMap;

//abandoned for not use SSA
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

    @Override
    public void collectDefUseInfo() {
    }

    @Override
    public Address replaceOperandForGlobalVariableOptimization(HashMap<Address, VirtualRegister> map) {
        return null;
    }

    @Override
    public <T> T accept(IRBaseVisitor<T> visitor) {
        return null;
    }
}
