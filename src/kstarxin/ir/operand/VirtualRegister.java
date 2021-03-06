package kstarxin.ir.operand;

import kstarxin.ast.*;
import kstarxin.ir.IRBaseVisitor;
import kstarxin.ir.asmir.ASMLevelIRVisitor;
import kstarxin.ir.operand.physical.*;
import kstarxin.utilities.*;

//record variable and tmp variable
public class VirtualRegister extends Register {
    public String   hintName;
    public String   mangledName;
    public int      tmpId;
    public Operand  spaceAllocatedTo;

    public VirtualRegister(String _hintName, String _mangledName){
        super(_hintName);
        hintName = _hintName;
        mangledName = _mangledName;
        spaceAllocatedTo = null;
        tmpId = -1;
    }

    public VirtualRegister(VariableDeclaratorNode node){
        super(node.getIdentifier());
        hintName = node.getIdentifier();
        mangledName = NameMangler.mangleName(node);
        tmpId = -1;
    }

    @Override
    public String getDisplayName() {
        if(mangledName != null) return "$" + mangledName;
        else return "$"+ tmpId;
    }

    public boolean isTmpRegister(){
        return tmpId < 0;
    }

    public void allocatedTo(PhysicalRegister preg){
        spaceAllocatedTo = preg;
    }

    public void allocatedTo(StackSpace stack){
        spaceAllocatedTo = stack;
    }

    public VirtualRegister copy(){
        VirtualRegister ret = new VirtualRegister(hintName, mangledName);
        ret.tmpId = tmpId;
        ret.spaceAllocatedTo = spaceAllocatedTo;
        return ret;
    }

    @Override
    public <T> T accept(ASMLevelIRVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Operand accept(IRBaseVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getNASMName() {
        return null;
    }
}
