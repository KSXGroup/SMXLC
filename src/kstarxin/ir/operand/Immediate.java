package kstarxin.ir.operand;

import kstarxin.ir.IRBaseVisitor;
import kstarxin.ir.asmir.ASMLevelIRVisitor;

public class Immediate extends Constant {
    public int value;
    public Immediate(int _value){
        super(_value +"");
        value = _value;
    }

    @Override
    public Operand accept(IRBaseVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public <T> T accept(ASMLevelIRVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getNASMName(){
        return "" + value;
    }
}
