package kstarxin.ir.operand;

public class Memory extends Operand {
    public VirtualRegister address;
    public Immediate       offset;
    public Memory(VirtualRegister _address, Immediate _offset){
        address = _address;
        offset  = _offset;
    }
}
