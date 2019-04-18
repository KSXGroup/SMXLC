package kstarxin.ir.operand.physical;

import kstarxin.ir.IRBaseVisitor;
import kstarxin.ir.operand.*;
//this 'Physical Memory' is not contary to 'Virtual Memory'
public class PhysicalMemory extends Operand {
    private PhysicalRegister base;
    private Operand offset;
    private int     sizePerData;
    public PhysicalMemory(PhysicalRegister _base, PhysicalRegister _offset, int _sizePerData){
        super("PMEM");
        base = _base;
        offset = _offset;
        sizePerData = _sizePerData;
    }

    public PhysicalMemory(PhysicalRegister _base){
        super("PMEM");
        base = _base;
        offset = null;
        sizePerData = 0;
    }

    public PhysicalMemory(PhysicalRegister _base, Immediate _offset){
        super("PMEM");
        base = _base;
        offset = _offset;
    }

    @Override
    public String getNASMName(){
        String ret = "[";
        if(offset != null){
            if(sizePerData == 0) {
                if(offset instanceof Immediate) ret += base.getNASMName() + "+" + ((Immediate) offset).getNASMName() + "]";
                else if(offset instanceof PhysicalRegister) ret += base.getNASMName() + "+" + ((PhysicalRegister)offset).getNASMName() + "]";
                else throw new RuntimeException();
            }else if(sizePerData > 0){
                if(offset instanceof Immediate){
                    int calc = ((Immediate) offset).value * sizePerData;
                    ret += base.getNASMName() + "+" + calc +"]";
                }
                else if(offset instanceof PhysicalRegister){
                    ret += base.getNASMName() + "+" + sizePerData + "*" + ((PhysicalRegister)offset).getNASMName();
                }
                else throw new RuntimeException();
            }else throw new RuntimeException();
        } else ret += base.getNASMName() + "]";
        return ret;
    }

    @Override
    public Operand accept(IRBaseVisitor visitor) {
        return null;
    }

}
