package kstarxin.ir.operand;

import kstarxin.compiler.Configure;

public class StaticString extends Label {
    public String value;
    public StaticString(String _hintName, String _value){
        super(_hintName);
        value = _value;
    }

    @Override
    public String getDisplayName() {
        return "["+ hintName + "]" + value + value.length() * Configure.PTR_SIZE;
    }
}
