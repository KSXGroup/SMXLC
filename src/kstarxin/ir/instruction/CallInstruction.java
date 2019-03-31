package kstarxin.ir.instruction;

import kstarxin.ir.Method;
import kstarxin.ir.operand.*;

import java.util.*;

public class CallInstruction extends Instruction{
    public VirtualRegister              returnValue;
    public Method                       callee;
    public ArrayList<Operand>           parameters;
    public boolean                      isClassMemberCall;
    public VirtualRegister              classThisPointer;
    public CallInstruction(Method _callee, VirtualRegister _returnValue) {
        super("call");
        parameters          = new ArrayList<Operand>();
        callee              = _callee;
        returnValue         = _returnValue;
        isClassMemberCall   = false;
        classThisPointer    = null;
    }

    public void addParameter(Operand para){
        parameters.add(para);
    }

    public void setClassMemberCall(VirtualRegister _classThisPointer){
        isClassMemberCall   = true;
        classThisPointer    = _classThisPointer;
    }

    @Override
    public CallInstruction copy() {
        CallInstruction ret =  new CallInstruction(callee, returnValue);
        if(isClassMemberCall) ret.setClassMemberCall(classThisPointer);
        parameters.forEach(p -> ret.addParameter(p));
        return ret;
    }

    @Override
    public void replaceOperandForInline(HashMap<Operand, Operand> map) {
        Operand a = null;
        if(returnValue != null) {
            a = map.get(returnValue);
            if (a == null || !(a instanceof VirtualRegister))
                throw new RuntimeException();
            else returnValue = (VirtualRegister) a;
        }

        if(classThisPointer != null){
            a = map.get(classThisPointer);
            if(a == null || !(a instanceof VirtualRegister)) throw new RuntimeException();
            else classThisPointer = (VirtualRegister) a;
        }

        ArrayList<Operand> newPara = new ArrayList<Operand>();
        for (Operand para : parameters) {
            if(!(para instanceof Immediate)) a = map.get(para);
            else a = para;
            if(a == null) throw new RuntimeException();
            else newPara.add(a);
        }
        parameters = newPara;
    }
}
