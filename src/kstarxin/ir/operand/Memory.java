package kstarxin.ir.operand;

import java.util.HashMap;

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

    public Memory(VirtualRegister _address, Register _index, int _sizePerData, int _scale){
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

    public Memory(Memory other){
        super("mem");
        address         = other.address;
        index           = other.index;
        sizePerData     = other.sizePerData;
        offsetInClass   = other.offsetInClass;
        scale           = other.scale;
    }

    public void replaceOperandForInline(HashMap<Operand, Operand> map) {
        Operand a = map.get(address);
        if(a == null || !(a instanceof Register)) throw new RuntimeException();
        else address = (Register) a;
        if(!(index instanceof Immediate)) {
            a = map.get(index);
            if(a == null || !(a instanceof Register)) throw new RuntimeException();
            index = (Register) a;
        }
    }

    @Override
    public String getDisplayName() {
        String addrString = address.getDisplayName();
        String ret = scale + "[" + addrString;
        if(sizePerData == -1){
            if(offsetInClass == 0) ret += "]";
            else ret = ret + "+" + offsetInClass + "]";
        }
        else{
            if(index instanceof Immediate) {
                int sz = ((Immediate) index).value * sizePerData;
                if(sz == 0) ret += "]";
                else ret += "+" + sz + "]";
            } else {
                ret = ret + "+" + sizePerData + "*" + index.getDisplayName() + "]";
            }
        }
        return ret;
    }
}
