package kstarxin.ir.operand;

public class StaticPointer extends Label {
    public int spaceSize;
    public int value;
    public StaticPointer(String _hintName, int _spaceSize){
        super(_hintName);
        spaceSize = _spaceSize;
    }

    public void setValue(int _value){
        value = _value;
    }

    @Override
    public String getDisplayName(){
        return "[" + hintName +"]" + spaceSize;
    }
}
