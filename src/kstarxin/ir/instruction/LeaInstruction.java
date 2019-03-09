package kstarxin.ir.instruction;

import kstarxin.ir.operand.*;

public class LeaInstruction extends Instruction {
    public VirtualRegister dest;
    public Address addr;
    public LeaInstruction(VirtualRegister _dest, Address _addr){
        super("lea");
        addr = _addr;
        dest = _dest;
    }
}
