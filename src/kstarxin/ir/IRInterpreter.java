package kstarxin.ir;

import kstarxin.compiler.Configure;
import kstarxin.ir.*;
import kstarxin.utilities.*;

import java.io.*;
import java.util.*;

/*
steps:
    1.read ir lines
    2.allocate static memory for global variable
    3.execute lines
compenents:
    1.heap memory
    2.static data memory
    3.registers
    4.stack
    for different stage after ir
*/

class Pair<K,V>{
    K first;
    V second;
}

public class IRInterpreter {

    abstract class irInst{
        String opCode;
        public irInst(String _opCode){
            opCode = _opCode;
        }
        public abstract void run();
    }

    class oneOpInst extends irInst{
        private String op;
        public oneOpInst(String _opCode, String _op){
            super(_opCode);
            op = _op;
        }

        @Override
        public void run() {
            switch (opCode){
                case "INC":
                    write(op, read(op) + 1);
                    break;
                case "DEC":
                    write(op, read(op) - 1);
                    break;
                case "BNO":
                    write(op, ~read(op));
                    break;
                default:
                    throw new RuntimeException("");
            }

        }
    }

    class twoOpInst extends irInst{
        private String op1, op2;
        public twoOpInst(String opCode, String opr1, String opr2){
            super(opCode);
            op1 = opr1;
            op2 = opr2;
        }
        @Override
        public void run(){
            switch (opCode){
                case "MOV":
                case "STO":
                case "LOA":
                    write(op1, read(op2));
                    break;
                default:
                    throw new RuntimeException("");
            }
        }
    }

    class threeOpInst extends irInst{
        private String opdest, op1, op2;
        public threeOpInst(String opCode, String oprdest, String opr1, String opr2){
            super(opCode);
            opdest  = oprdest;
            op1     = opr1;
            op2     = opr2;
        }
        @Override
        public void run(){
            int a   = read(op1);
            int b   = read(op2);
            boolean ba = false;
            boolean bb = false;
            if(a > 0) ba = true;
            if(b > 0) bb = true;
            int res = 0;
            switch (opCode){
                case "ADD":
                    res = a + b;
                    break;
                case "BAN":
                    res = a & b;
                    break;
                case "AND":
                    if(ba && bb) res = 1;
                    else res = 0;
                    break;
                case "BOR":
                    res = a & b;
                    break;
                case "XOR":
                    res = a ^ b;
                    break;
                case "DIV":
                    res = (int)a / b;
                    break;
                case "EQU":
                    if(a == b) res = 1;
                    else res = 0;
                    break;
                case "GEQ":
                    if(a >= b) res = 1;
                    else res = 0;
                    break;
                case "GTH":
                    if(a > b) res = 1;
                    else res = 0;
                    break;
                case "LEQ":
                    if(a <= b) res = 1;
                    else res = 0;
                    break;
                case "LTH":
                    if(a < b) res = 1;
                    else res = 0;
                    break;
                case "MOD":
                    res = a % b;
                    break;
                case "MUL":
                    res = a * b;
                    break;
                case "NEQ":
                    res = a == b ? 0 : 1;
                    break;
                case "ORI":
                    res = (ba || bb) ? 1 : 0;
                    break;
                case "SHL":
                    res = a << b;
                    break;
                case "SAR":
                    res = a >> b;
                    break;
                case "SUB":
                    res = a - b;
                default:
                    throw new RuntimeException("unknown inst" + opCode);
            }
            write(opdest, res);
            return;
        }
    }

    class djumpInst extends irInst{
        private String target;
        public djumpInst(String _target){
            super("JMP");
            target = _target;
        }

        @Override
        public void run(){
            jumpTo(target);
        }
    }

    class cjumpInst extends irInst{
        private String ltrue;
        private String lfalse;
        public cjumpInst(String opCode, String _t1, String _t2){
            super(opCode);
            ltrue   = _t1;
            lfalse  = _t2;
        }

        @Override
        public void run() {
            switch(opCode){
                case "JEQ":
                    if(compareResult == 0) jumpTo(ltrue);
                    else jumpTo(lfalse);
                    break;
                case "JNE":
                    if(compareResult == 0) jumpTo(lfalse);
                    else jumpTo(ltrue);
                    break;
                case "JGT":
                    if(compareResult > 0) jumpTo(ltrue);
                    else jumpTo(lfalse);
                    break;
                case "JLT":
                    if(compareResult < 0) jumpTo(ltrue);
                    else jumpTo(lfalse);
                    break;
                case "JLE":
                    if(compareResult > 0) jumpTo(lfalse);
                    else jumpTo(ltrue);
                    break;
                case "JGE":
                    if(compareResult < 0) jumpTo(lfalse);
                    else jumpTo(ltrue);
                    break;
                default:
                    throw new RuntimeException("unknown ir jump inst:" + opCode );
            }
        }
    }

    class callInst extends irInst{
        private String calleeName;
        private String returnReg;
        private String classThisPointer;
        private ArrayList<String> parameters;
        public callInst(String[] arr){
            super("CAL");
            calleeName = arr[0];
            returnReg = arr[1];
            parameters = new ArrayList<String>();
            if(classThisPointer.equals("null")) classThisPointer = null;
            else classThisPointer = arr[2];
            for(int i = 3; i < arr.length; ++i)
                parameters.add(arr[i]);
        }
        @Override
        public void run() {
            callMethod(calleeName, classThisPointer, parameters, returnReg);
        }
    }

    class labelInst extends irInst{
        public labelInst(String _label){
            super(_label);
        }

        @Override
        public void run() {
            return;
        }
    }

    class irMethod{
        private ArrayList<String> paras;
        private HashMap<Integer, irInst> insts;
        private String name;
        private boolean retFlag = false;
        public irMethod(String _name){
            paras   = new ArrayList<String>();
            insts   = new HashMap<Integer, irInst>();
            name    = _name;
        }

        public void run(){
            Integer startNum = labelToLineNumber.get("%" + name);
            if(startNum  == null) throw new RuntimeException("not found the enterance line of method " + name);
            programCounter = startNum + 1;
            while(!retFlag){
                insts.get(programCounter).run();
                programCounter++;
            }
        }

    }

    private enum STATUS{
        STATIC, TEXT
    }

    private final static int memorySize     = (1 << 26);  //64 MB memory
    private final static int nonStaticStart = (1 << 20); //1 MB static area fixed memory start
    private STATUS                          current_state;
    private String                          fileName;
    private HashMap<String, Integer>        registerHeap;
    private HashMap<String, Integer>        staticAddressMapper;
    private HashMap<String, irMethod>       methodMap;
    private HashMap<String, Integer>        labelToLineNumber;
    private LinkedList<LinkedList<Pair<String, Integer>>>
                                            callStack;
    private byte[]                          memory;
    private BufferedReader                  reader;
    private int                             currentLineNumber;
    private int                             staticTop;
    private int                             memoryBottom;
    private int                             compareResult;
    private int                             programCounter;
    private String                          output;
    private irMethod                        currentMethodParsing;
    private irMethod                        currentMethodRunning;

    public IRInterpreter(String _fileName) throws IOException{
        fileName            = _fileName;
        current_state       = STATUS.STATIC;
        registerHeap        = new HashMap<String, Integer>();
        staticAddressMapper = new HashMap<String, Integer>();
        methodMap           = new HashMap<String, irMethod>();
        labelToLineNumber   = new HashMap<String, Integer>();
        callStack           = new LinkedList<LinkedList<Pair<String, Integer>>>();
        memory              = new byte[memorySize];
        reader              = new BufferedReader(new FileReader(fileName));
        staticTop           = 0;
        output              = "";
        for(int i = 0; i < memorySize; ++i) memory[i] = 0;
    }

    private void alignToFour(){
        while(staticTop % 4 != 0)       ++staticTop;
        while(memoryBottom % 4 != 0)    ++memoryBottom;
    }

    private void writeMemory(int addr, int data){
        //low addr for low byte
        memory[addr]        = (byte)( data          & 0xff);
        memory[addr + 1]    = (byte)((data >> 8 )   & 0xff);
        memory[addr + 2]    = (byte)((data >> 16)   & 0xff);
        memory[addr + 3]    = (byte)((data >> 24)   & 0xff);
    }

    private int readMemory(int addr){
        int ret =          (int)(memory[addr + 3]);
        ret = (ret << 8) | (int)(memory[addr + 2]);
        ret = (ret << 8) | (int)(memory[addr + 1]);
        ret = (ret << 8) | (int)(memory[addr    ]);
        return ret;
    }

    private void parseStaticData(String line){
        int f = 0;
        int size = 0;
        boolean stringFlag = false;
        String tmpSize = "";
        String staticName = "";
        String value = null;
        char c;
        for(int i = 0; i < line.length(); ++i){
            switch (f){
                case 0:
                    c = line.charAt(i);
                    if(c  == '['){
                        f= 1;
                        staticName += c;
                    }
                    else tmpSize += c;
                    break;
                case 1:
                    c = line.charAt(i);
                    staticName += c;
                    if(c == ']') f = 2;
                    break;
                case 2:
                    c = line.charAt(i);
                    if(c == '\"') {
                        stringFlag = true;
                        value = "";
                        f = 3;
                    } else if(c <= '9' && c >= '0') {
                        value = "" + c;
                        f = 4;
                    }
                    break;
                case 3:
                    c = line.charAt(i);
                    if(c == '\"' && i == line.length() - 1)
                        f = 5;
                    else value += c;
                    break;
                case 4:
                    value += line.charAt(i);
                    break;
                case 5:
                    throw new RuntimeException("why there is something after \" ?????");
            }
        }
        size = Integer.parseInt(tmpSize);
        staticAddressMapper.put(staticName, staticTop);
        int addr = staticTop;
        staticTop += Configure.PTR_SIZE;
        if(value != null ) {
            if(stringFlag) {
                writeMemory(addr, staticTop);
                writeMemory(staticTop, size - Configure.PTR_SIZE);
                staticTop += Configure.PTR_SIZE;
                value = StringParser.parseString(value);
                for (int i = 0; i < value.length(); ++i)
                    memory[staticTop++] = (byte) value.charAt(i);
                memory[staticTop] = '\0';
                alignToFour();
            }else{
                writeMemory(staticTop, Integer.valueOf(value));
                staticTop += Configure.PTR_SIZE;
            }
        }
    }

    private void parseInstruction(String line){
        String[] arr = line.trim().split("\\s+");
        switch(arr[0]){
            case IRPrinter.methodHeader:
                irMethod mth = new irMethod(arr[1]);
                currentMethodParsing = mth;
                methodMap.put(arr[1], mth);
                break;
            case IRPrinter.parameterHeader:
                currentMethodParsing.paras.add(arr[1]);
                break;
            case IRPrinter.localVariableHeader:
                registerHeap.put(arr[1], 0);
                break;
            default:
                currentMethodParsing.insts.put(currentLineNumber, buildInst(arr));
                break;
        }
    }

    private String removeBrac(String s){
        String ret = "";
        boolean st = false;
        char c;
        for(int i = 0; i < s.length(); ++i){
            c = s.charAt(i);
            if(c == '[') st = true;
            else if(c  == ']') break;
            else ret += c;
        }
        return ret;
    }

    private int resolveMemoryAddres(String memOp){
        int f = 0;
        String regName = "";
        String num = null;
        String pos = null;
        char c;
        for(int i = 0; i < memOp.length(); ++i){
            switch (f){
                case 0:
                    if(memOp.charAt(i) == '[') f = 1;
                    break;
                case 1:
                    c = memOp.charAt(i);
                    if(c == '+') f = 2;
                    else if(c == ']') break;
                    else regName += c;
                    break;
                case 2:
                    c = memOp.charAt(i);
                    if(c > '0' && c < '9'){
                        if(num == null) num = "" + c;
                        else num += c;
                    }
                    if(c == '*') f = 3;
                    else if(c == ']') break;
                    break;
                case 3:
                    c = memOp.charAt(i);
                    if(c == ']') break;
                    if(pos == null) pos = "";
                    pos += c;
            }
        }
        int baseAddr = read(regName);
        if(num == null && pos == null) return baseAddr;
        if(num != null && pos == null) return baseAddr + Integer.valueOf(num);
        else if(num != null && pos != null) return baseAddr + Integer.valueOf(num) * read(pos);
        else throw new RuntimeException("unknwon memory parttern");
    }

    private int read(String op){
        char c = op.charAt(0);
        if(c == '$'){
            Integer ret = registerHeap.get(op);
            if(ret == null) throw new RuntimeException("use of not defined reg " + op);
            else return ret;
        }
        else if(c == '['){
            String inner = removeBrac(op);
            if(inner.charAt(0) != '$') return readMemory(staticAddressMapper.get(inner));
            else return readMemory(resolveMemoryAddres(op));
        }
        else if(c >= '0' &&  c <= '9') return Integer.valueOf(op);
        else throw new RuntimeException("unknown opread " + op);
    }

    private void write(String op, int value){
        char c = op.charAt(0);
        if(c == '$'){
            Integer ret = registerHeap.get(op);
            if(ret == null) throw new RuntimeException("use of not defined reg " + op);
            else registerHeap.replace(op, value);
        }
        else if(c == '[') writeMemory(resolveMemoryAddres(op), value);
        else if(c >= '0' &&  c <= '9') throw new RuntimeException("write constant????");
        else throw new RuntimeException("unknown opread " + op);
    }

    private String processPrint(String pointer){
        Integer startAddr = read(pointer);
        String ret = "";
        if(startAddr == null) throw new RuntimeException("try to read undefined register");
        startAddr += 4;
        for(int i = startAddr; memory[i] != '\0'; ++i) ret += (char)memory[i];
        return ret;
    }

    private void callMethod(String name,String classThisPointer, ArrayList<String> para, String returnReg){
        switch (name){
            case "%@_Zprintlns":
                if(classThisPointer != null) throw new RuntimeException("why there is class pointer for print ???");
                else if(para.size() > 1) throw new RuntimeException("why there is more than one parameter");
                else output += processPrint(para.get(0)) + "\n";
                break;
            case "%@_Zprints":
                if(classThisPointer != null) throw new RuntimeException("why there is class pointer for print ???");
                else if(para.size() > 1) throw new RuntimeException("why there is more than one parameter");
                else output += processPrint(para.get(0));
                break;
        }
        return;
    }

    private void methodReturn(){
        currentMethodRunning.retFlag = true;
    }

    private void bstrcat(String ptr1, String ptr2){}

    private void bstrcmp(String ptr1, String ptr2){}

    private void jumpTo(String label){
        Integer ret = labelToLineNumber.get(label);
        if(ret == null) throw new RuntimeException("try to jump to label not exsit!");
        else programCounter = ret;
    }

    private irInst buildInst(String[] arr){
        if(arr[0].charAt(0) == '%') {
            labelToLineNumber.put(arr[0], currentLineNumber);
            return new labelInst(arr[0]);
        }
        else if(arr[0].equals("CAL"))   return new callInst(arr);
        else if(arr.length == 2)        return new oneOpInst(arr[0], arr[1]);
        else if(arr.length == 3)        return new twoOpInst(arr[0], arr[1], arr[2]);
        else if(arr.length == 4)        return new threeOpInst(arr[0], arr[1], arr[2], arr[3]);
        else throw new RuntimeException("unknown inst when iterpret ir");
    }

    public void parseFile() throws IOException {
        String line = null;
        do {
            line = reader.readLine();
            if(line == null) break;
            else if(line.equals("")){
                currentLineNumber++;
                continue;
            }
            else {
                //System.out.println(line);
                if(line.equals(IRPrinter.codeAreaHeader)) {
                    current_state = STATUS.TEXT;
                    currentLineNumber++;
                    continue;
                }
                else if(line.equals(IRPrinter.staticAreaHeader)) {
                    current_state = STATUS.STATIC;
                    currentLineNumber++;
                    continue;
                }
                //System.out.println(line);
                currentLineNumber++;
                if (current_state == STATUS.STATIC)
                    parseStaticData(line);
                else
                    parseInstruction(line);
            }
        } while(true);
    }

    public void runIR() throws IOException{
        parseFile();
        irMethod initm = methodMap.get(NameMangler.initMethod);
        if(initm != null) initm.run();
        irMethod mainm = methodMap.get(NameMangler.mainMethodName);
        if(mainm == null) throw new RuntimeException("No main method in ir!!!!!");
        currentMethodRunning = mainm;
        mainm.run();
        System.out.println("[RESULT]:\n" + output);
        return;
    }
}
