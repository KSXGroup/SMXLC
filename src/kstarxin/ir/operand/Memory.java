package kstarxin.ir.operand;

import kstarxin.ir.IRBaseVisitor;
import kstarxin.ir.asmir.ASMLevelIRVisitor;
import kstarxin.ir.operand.physical.PhysicalRegister;
import kstarxin.ir.operand.physical.StackSpace;
import kstarxin.nasm.PhysicalRegisterSet;

import java.util.HashMap;
import java.util.HashSet;

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
        if(a == null || !(a instanceof Register))
            throw new RuntimeException();
        else address = (Register) a;
        if(index != null && !(index instanceof Immediate)) {
            a = map.get(index);
            if(a == null) throw new RuntimeException();
            if(a instanceof Register)
                index = (Register) a;
            else if(a instanceof Immediate)
                index = (Immediate)a;
        }
    }

    public void replaceForSpill(HashMap<VirtualRegister, VirtualRegister> map){
        if(address instanceof VirtualRegister && map.containsKey(address)) address = map.get(address);
        if(index instanceof VirtualRegister && map.containsKey(index)) index = map.get(index);
    }

    @Override
    public String getDisplayName() {
        String addrString = address.getDisplayName();
        String ret = scale + "[" + addrString;
        if(sizePerData == -1){
            if(offsetInClass == 0) ret += "]";
            else if(offsetInClass > 0) ret = ret + "+" + offsetInClass + "]";
            else ret = ret + "-" + (-offsetInClass) + "]";
        }
        else{
            if(index instanceof Immediate) {
                int sz = (int)((Immediate) index).value * sizePerData;
                if(sz == 0) ret += "]";
                else ret += "+" + sz + "]";
            } else {
                ret = ret + "+" + sizePerData + "*" + index.getDisplayName() + "]";
            }
        }
        return ret;
    }

    public HashSet<VirtualRegister> collectUseInfo(){
        HashSet<VirtualRegister> ret = new HashSet<VirtualRegister>();
        if(address instanceof VirtualRegister && !(((VirtualRegister) address).spaceAllocatedTo == PhysicalRegisterSet.RBP || ((VirtualRegister) address).spaceAllocatedTo == PhysicalRegisterSet.RSP ) )ret.add((VirtualRegister) address);
        if(index instanceof VirtualRegister) ret.add((VirtualRegister) index);
        return ret;
    }

    @Override
    public Operand accept(IRBaseVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public <T> T accept(ASMLevelIRVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getNASMName() {
        String addrString = ((VirtualRegister)address).spaceAllocatedTo.getNASMName();
        String ret = "[" + addrString;
        if(sizePerData == -1){
            if(offsetInClass == 0) ret += "]";
            else if(offsetInClass > 0) ret = ret + "+" + offsetInClass + "]";
            else ret = ret + "-" + (-offsetInClass) + "]";
        }
        else{
            if(index instanceof Immediate) {
                int sz = (int)((Immediate) index).value * sizePerData;
                if(sz == 0) ret += "]";
                else ret += "+" + sz + "]";
            } else  ret = ret + "+" + sizePerData + "*" +((VirtualRegister)index).spaceAllocatedTo.getNASMName()+ "]";
        }
        return ret;
    }

}
