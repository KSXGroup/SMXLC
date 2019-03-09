package kstarxin.ir.instruction;

import kstarxin.ir.Method;
import kstarxin.ir.operand.*;

import java.util.LinkedList;

public class CallInstruction extends Instruction{
    public VirtualRegister     returnValue;
    public Method              callee;
    public LinkedList<Operand> parameters;
    public boolean             isClassMemberCall;
    public VirtualRegister     classThisPointer;
    public CallInstruction(Method _callee, VirtualRegister _returnValue) {
        super("call");
        parameters          = new LinkedList<Operand>();
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
}
