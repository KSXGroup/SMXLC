package kstarxin.ir.operand;

import kstarxin.compiler.Configure;
import kstarxin.utilities.*;

public class StaticString extends Label {
    public String   value;
    public String   parsedValue;
    public boolean  isConstantAllocatedByCompiler;
    public StaticString(String _hintName, String _value){
        super(_hintName);
        value       = _value;
        isConstantAllocatedByCompiler = false;
        if(_value != null)
            parsedValue = StringParser.parseString(_value);
        else parsedValue = null;
    }

    @Override
    public String getDisplayName() {
        return Configure.PTR_SIZE+"["+ hintName + "]" ;
    }

    public String getInitName(){
        if(value != null) return (parsedValue.length() + Configure.PTR_SIZE) + "["+ hintName + "] " + value ;
        else return Configure.PTR_SIZE+"["+ hintName + "]";
    }

}
