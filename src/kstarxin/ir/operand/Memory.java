package kstarxin.ir.operand;

public class Memory extends Address {
    public Register address;
    public Operand  index;
    public int      sizePerData;
    public int      offsetInClass;
    public int      scale;
    public Memory(VirtualRegister _address, Immediate _index, int _sizePerData, int _scale){
        //for array access
        super("mem");
        address     = _address;
        index       = _index;
        sizePerData = _sizePerData;
        scale       = _scale;
    }

    public Memory(VirtualRegister _address, VirtualRegister _index, int _sizePerData, int _scale){
        //for array access
        super("mem");
        address     = _address;
        index       = _index;
        sizePerData = _sizePerData;
        scale       = _scale;
    }

    public Memory(VirtualRegister _address, int _scale){
        super("mem");
        address         = _address;
        index           = null;
        sizePerData     = -1;
        offsetInClass   = 0;
        scale           = _scale;
    }

    public Memory(VirtualRegister _address, int _offsetInClass, int _scale){
        super("mem");
        address         = _address;
        index           = null;
        sizePerData     = -1;
        offsetInClass   = _offsetInClass;
        scale           = _scale;
    }

    @Override
    public String getDisplayName() {
        String addrString = address.getDisplayName();
        String ret = scale + "[" + addrString;
        if(sizePerData == -1){
            if(offsetInClass == 0) ret += "]";
            else ret = ret + " + " + offsetInClass + "]";
        }
        else{
            if(index instanceof Immediate) {
                int sz = ((Immediate) index).value * sizePerData;
                if(sz == 0) ret += "]";
                else ret += " + " + sz + "]";
            } else {
                ret = ret + " + " + sizePerData + " * " + index.getDisplayName() + "]";
            }
        }
        return ret;
    }
}
