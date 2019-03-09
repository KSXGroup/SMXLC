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
        if(value != null) return "["+ hintName + "]" ;
        else return "["+ hintName + "]";
    }

    public String getInitName(){
        if(value != null) return value.length() * Configure.PTR_SIZE + " ["+ hintName + "] " + value ;
        else return "0 ["+ hintName + "]";
    }
}
