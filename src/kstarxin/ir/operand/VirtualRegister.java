package kstarxin.ir.operand;

import kstarxin.ast.*;
import kstarxin.utilities.*;

//record variable and tmp variable
public class VirtualRegister extends Register {
    public String hintName;
    public String mangledName;
    public int tmpId;

    public VirtualRegister(String _hintName, String _mangledName){
        super(_hintName);
        hintName = _hintName;
        mangledName = _mangledName;
        tmpId = -1;
    }

    public VirtualRegister(int _tmpId){
        super(""+_tmpId);
        tmpId = _tmpId;
        mangledName = null;
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
}
