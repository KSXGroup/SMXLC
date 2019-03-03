package kstarxin.ir.operand;

abstract public class Operand {
    public String hintName;
    public Operand(String _hintName){
        hintName = _hintName;
    }

    public String getDisplayName(){
        return hintName;
    }
}
