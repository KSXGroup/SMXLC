package kstarxin.ir.operand;

import kstarxin.ir.IRBaseVisitor;

public class Immediate extends Constant {
    public int value;
    public Immediate(int _value){
        super(_value +"");
        value = _value;
    }

    @Override
    public Operand accept(IRBaseVisitor visitor) {
        return null;
    }

    @Override
    public String getNASMName(){
        return "" + value;
    }
}
