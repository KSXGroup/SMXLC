package kstarxin.ir.instruction;

import kstarxin.ir.Method;
import kstarxin.ir.operand.*;

import java.util.LinkedList;

public class CallInstruction extends Instruction{
    private VirtualRegister     returnValue;
    private Method              callee;
    private LinkedList<Operand> parameters;
    private boolean             isClassMemberCall;
    private VirtualRegister     classThisPointer;
    public CallInstruction(Method _callee, VirtualRegister _returnValue) {
        super("call");
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
