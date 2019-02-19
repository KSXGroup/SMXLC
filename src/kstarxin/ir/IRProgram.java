package kstarxin.ir;

import java.util.*;
import kstarxin.ir.operand.*;
import kstarxin.ir.instruction.*;
import kstarxin.ir.superblock.*;
import kstarxin.utilities.*;

public class IRProgram {
    private Method entranceMethod;
    private HashMap<String, Method> methodMap;
    private HashMap<String, MxType> typeMap;
    private HashMap<String, Label> globalVariableMap;
    private List<LoopSuperBlock> loopList;
    private List<ConditionSuperBlock> conditionList;

    public IRProgram(){
        entranceMethod      = null;
        methodMap           = new HashMap<String, Method>();
        typeMap             = new HashMap<String, MxType>();
        globalVariableMap   = new HashMap<String, Label>();
        loopList            = new LinkedList<LoopSuperBlock>();
        conditionList       = new LinkedList<ConditionSuperBlock>();
    }

    public void setEntranceMethod(Method _entranceMethod){
        entranceMethod = _entranceMethod;
    }

    public void addGlobalVariable(String mangledName, Label addr, MxType type){
        globalVariableMap.put(mangledName, addr);
        typeMap.put(mangledName, type);
    }

    public void addMethod(String mangledName, Method method, MxType retType){
        methodMap.put(mangledName, method);
        typeMap.put(mangledName, retType);
    }

}
