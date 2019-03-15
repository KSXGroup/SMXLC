package kstarxin.ir.operand;

import kstarxin.compiler.Configure;
import kstarxin.utilities.*;

public class StaticString extends Label {
    public String value;
    public String parsedValue;
    public StaticString(String _hintName, String _value){
        super(_hintName);
        value       = _value;
        parsedValue = StringParser.parseString(_value);
    }

    @Override
    public String getDisplayName() {
        if(value != null) return "["+ hintName + "]" ;
        else return "["+ hintName + "]";
    }

    public String getInitName(){
        if(value != null) return (parsedValue.length() + Configure.PTR_SIZE) + "["+ hintName + "] " + value ;
        else return "4["+ hintName + "]";
    }

}
