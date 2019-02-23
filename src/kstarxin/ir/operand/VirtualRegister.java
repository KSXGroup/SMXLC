package kstarxin.ir.operand;

import kstarxin.ast.*;
import kstarxin.utilities.*;

//record variable and tmp variable
public class VirtualRegister extends Register {
    public String hintName;
    public String mangledName;
    public int tmpId;

    public VirtualRegister(String _hintName, String _mangledName){
        hintName = _hintName;
        mangledName = _mangledName;
        tmpId = -1;
    }

    public VirtualRegister(int _tmpId){
        tmpId = _tmpId;
        mangledName = "";
    }

    public VirtualRegister(VariableDeclaratorNode node){
        hintName = node.getIdentifier();
        mangledName = NameMangler.mangleName(node);
        tmpId = -1;
    }
}
