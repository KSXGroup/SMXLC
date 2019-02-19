package kstarxin.ir.operand;

public class StaticString extends Label {
    public String value;
    public StaticString(String _hintName, String _value){
        super(_hintName);
        value = _value;
    }
}
