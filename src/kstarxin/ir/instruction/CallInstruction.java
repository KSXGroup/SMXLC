package kstarxin.ir.instruction;

import kstarxin.ir.operand.*;

import java.util.LinkedList;

public class CallInstruction extends Instruction{
    private Label callee;
    private LinkedList<Operand> parameters;
    public CallInstruction(Label callee) {
        super("call");
    }

    public void addParameter(Operand para){
        parameters.add(para);
    }
}
