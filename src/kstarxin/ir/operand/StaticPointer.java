package kstarxin.ir.operand;

public class StaticPointer extends Label {
    public int spaceSize;
    public int value;
    public boolean initialized;
    public StaticPointer(String _hintName, int _spaceSize){
        super(_hintName);
        spaceSize = _spaceSize;
        initialized = false;
        value = 0;
    }

    public void setInitialized(int _value){
        initialized = true;
        value = _value;
    }

    @Override
    public String getDisplayName(){
        return spaceSize + "[" + hintName +"]";
    }
}
