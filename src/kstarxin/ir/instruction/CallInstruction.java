package kstarxin.ir.instruction;

import kstarxin.ir.Method;
import kstarxin.ir.operand.*;

import java.util.LinkedList;

public class CallInstruction extends Instruction{
    private Method callee;
    private LinkedList<Operand> parameters;
    public CallInstruction(Method _callee) {
        super("call");
        callee = _callee;
    }

    public void addParameter(Operand para){
        parameters.add(para);
    }
}
