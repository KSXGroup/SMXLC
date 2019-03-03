package kstarxin.ir.operand;

public class Label extends Address {
    public Label(String _hintName){
        super(_hintName);
    }

    @Override
    public String getDisplayName() {
        return "["+hintName+"]";
    }
}
