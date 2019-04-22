package kstarxin.ir.operand;

import kstarxin.compiler.Configure;
import kstarxin.ir.IRBaseVisitor;
import kstarxin.ir.asmir.ASMLevelIRVisitor;
import kstarxin.utilities.*;

public class StaticString extends Label {
    public String   value;
    public String   parsedValue;
    public boolean  isConstantAllocatedByCompiler;
    public StaticString(String _hintName, String _value){
        super(_hintName);
        nasmName = NameMangler.convertToASMName(_hintName);
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


    @Override
    public Operand accept(IRBaseVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public <T> T accept(ASMLevelIRVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getNASMName() {
        return "[" + nasmName + "]";
    }
}
