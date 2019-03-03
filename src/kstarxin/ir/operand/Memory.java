package kstarxin.ir.operand;

public class Memory extends Address {
    public VirtualRegister address;
    public Immediate       offset;
    public Memory(VirtualRegister _address, Immediate _offset){
        super("mem");
        address = _address;
        offset  = _offset;
    }

    @Override
    public String getDisplayName() {
        return "[" + address.getDisplayName()+" " + offset + "]";
    }
}
