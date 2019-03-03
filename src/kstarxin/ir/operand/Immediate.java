package kstarxin.ir.operand;

public class Immediate extends Constant {
    public int value;
    public Immediate(int _value){
        super(_value +"");
        value = _value;
    }
}
