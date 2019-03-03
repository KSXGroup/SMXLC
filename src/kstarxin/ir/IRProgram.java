package kstarxin.ir;

import java.util.*;
import kstarxin.ir.operand.*;
import kstarxin.ir.instruction.*;
import kstarxin.ir.superblock.*;
import kstarxin.utilities.*;

public class IRProgram {
    private static String stringConstantPrefix      = "_tmpString_";
    //private static String compareResultRegisterName = "_compareResult";

    private Integer                     stringConstantCounter;
    private Method                      initMethod;
    private Method                      entranceMethod;
    private HashMap<String, Method>     methodMap;
    private HashMap<String, MxType>     typeMap;
    private HashMap<String, Label>      globalVariableMap;
    private HashMap<String, Integer>    offsetInClass;
    private HashMap<String, Integer>    classSize;
    private List<Method>                builtinMethodList;
    private List<LoopSuperBlock>        loopList;
    private List<ConditionSuperBlock>   conditionList;

    public Method                       strcmp;
    public Method                       strcat;
    public Method                       malloc;

    public IRProgram(){
        stringConstantCounter   = 0;
        entranceMethod          = null;
        methodMap               = new HashMap<String, Method>();
        typeMap                 = new HashMap<String, MxType>();
        globalVariableMap       = new HashMap<String, Label>();
        offsetInClass           = new HashMap<String, Integer>();
        builtinMethodList       = new LinkedList<Method>();
        loopList                = new LinkedList<LoopSuperBlock>();
        conditionList           = new LinkedList<ConditionSuperBlock>();
    }

    public void setInitMethod(Method _initMethod){
        initMethod = _initMethod;
    }

    public void setEntranceMethod(Method _entranceMethod){
        entranceMethod = _entranceMethod;
    }

    public void addGlobalVariable(String mangledName, Label addr, MxType type){
        globalVariableMap.put(mangledName, addr);
        typeMap.put(mangledName, type);
    }

    public void addLocalVariable(String mangledName, MxType type){
        typeMap.put(mangledName, type);
    }

    public void addClassVariable(String mangledName, MxType type){
        typeMap.put(mangledName, type);
    }

    public void addMethod(String mangledName, Method method, MxType retType){
        methodMap.put(mangledName, method);
        typeMap.put(mangledName, retType);
    }

    public void addBuiltinMethod(String mangledName, Method method, MxType retType){
        method.setBuiltin();
        methodMap.put(mangledName, method);
        typeMap.put(mangledName, retType);
    }

    public void setOffsetInClass(String mangledName, Integer offset){
        offsetInClass.put(mangledName, offset);
    }

    public StaticString addStringConstant(String s){
        String tmpHintName = stringConstantPrefix + stringConstantCounter.toString();
        StaticString tmp = new StaticString(tmpHintName,s);
        globalVariableMap.put(tmpHintName, tmp);
        return tmp;
    }

    public Method getMethod(String mangledName){
        return methodMap.get(mangledName);
    }

    public int getOffsetInClass(String mangledName){
        return offsetInClass.get(mangledName);
    }

    public Label getGlobalVariableVirtualRegister(String mn){
        return globalVariableMap.get(mn);
    }

    public void addClassSize(String name, int size){
        classSize.put(name, size);
    }
    public int getClassSize(String name){
        return classSize.get(name);
    }

    public Method getInitMethod(){
        return initMethod;
    }

    public HashMap<String, Method> getMethodMap(){
        return methodMap;
    }

    public HashMap<String, Label> getGlobalVariableMap(){
        return globalVariableMap;
    }
}
