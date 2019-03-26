package kstarxin.ir;

import kstarxin.compiler.Configure;
import kstarxin.ir.*;
import kstarxin.utilities.*;

import java.io.*;
import java.lang.reflect.Array;
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

//function call not supported for there is no run-time stack

class Pair<K,V>{
    K first;
    V second;
    public Pair(K _first, V _second){
        first = _first;
        second = _second;
    }

    public Pair(){
        first = null;
        second = null;
    }
}

public class IRInterpreter {

    abstract class irInst {
        String opCode;

        public irInst(String _opCode) {
            opCode = _opCode;
        }

        public abstract void run();
    }

    class oneOpInst extends irInst {
        private String op;

        public oneOpInst(String _opCode, String _op) {
            super(_opCode);
            op = _op;
        }

        @Override
        public void run() {
            throw new RuntimeException("There is no one op inst now !!!");
        }
    }

    class twoOpInst extends irInst {
        private String op1, op2;

        public twoOpInst(String opCode, String opr1, String opr2) {
            super(opCode);
            op1 = opr1;
            op2 = opr2;
        }

        @Override
        public void run() {
            switch (opCode) {
                case "MOV":
                case "STO":
                case "LOA":
                    write(op1, read(op2));
                    break;
                case "CMP":
                    irCompare(op1, op2);
                    break;
                case "INC":
                    write(op1, read(op2) + 1);
                    break;
                case "DEC":
                    write(op1, read(op2) - 1);
                    break;
                case "BNO":
                    write(op1, ~read(op2));
                    break;
                case "NOT":
                    if(read(op2) <= 0) write(op1, 1);
                    else write(op1, 0);
                    break;
                case "SUB":
                    write(op1, -read(op2));
                    break;
                default:
                    throw new RuntimeException("unknown inst opcode");
            }
        }
    }

    class threeOpInst extends irInst {
        private String opdest, op1, op2;

        public threeOpInst(String opCode, String oprdest, String opr1, String opr2) {
            super(opCode);
            opdest = oprdest;
            op1 = opr1;
            op2 = opr2;
        }

        @Override
        public void run() {
            int a = read(op1);
            int b = read(op2);
            boolean ba = false;
            boolean bb = false;
            if (a > 0) ba = true;
            if (b > 0) bb = true;
            int res = 0;
            switch (opCode) {
                case "ADD":
                    res = a + b;
                    break;
                case "BAN":
                    res = (a & b);
                    break;
                case "AND":
                    if (ba && bb) res = 1;
                    else res = 0;
                    break;
                case "BOR":
                    res = (a | b);
                    break;
                case "XOR":
                    res = (a ^ b);
                    break;
                case "DIV":
                    res = (a / b);
                    break;
                case "EQU":
                   // if(programCounter != 193) System.out.println("EQU " + a +" "+b + "at line " + programCounter);
                    if (a == b) res = 1;
                    else res = 0;
                    break;
                case "GEQ":
                    if (a >= b) res = 1;
                    else res = 0;
                    break;
                case "GTH":
                    if (a > b) res = 1;
                    else res = 0;
                    break;
                case "LEQ":
                    if (a <= b) res = 1;
                    else res = 0;
                    break;
                case "LTH":
                    if (a < b) res = 1;
                    else res = 0;
                    break;
                case "MOD":
                    res = a % b;
                    break;
                case "MUL":
                    res = a * b;
                    break;
                case "NEQ":
                    if (a == b) res = 0;
                    else res = 1;
                    break;
                case "ORI":
                    res = (ba || bb) ? 1 : 0;
                    break;
                case "SHL":
                    res = (a << b);
                    break;
                case "SAR":
                    res = (a >> b);
                    break;
                case "SUB":
                    res = (a - b);
                    break;
                default:
                    throw new RuntimeException("unknown inst" + opCode);
            }
            write(opdest, res);
            return;
        }
    }

    class djumpInst extends irInst {
        private String target;

        public djumpInst(String _target) {
            super("JMP");
            target = _target;
        }

        @Override
        public void run() {
            jumpTo(target);
        }
    }

    class cjumpInst extends irInst {
        private String ltrue;
        private String lfalse;

        public cjumpInst(String opCode, String _t1, String _t2) {
            super(opCode);
            ltrue = _t1;
            lfalse = _t2;
        }

        @Override
        public void run() {
            switch (opCode) {
                case "JEQ":
                    if (compareResult == 0) jumpTo(ltrue);
                    else jumpTo(lfalse);
                    break;
                case "JNE":
                    if (compareResult == 0) jumpTo(lfalse);
                    else jumpTo(ltrue);
                    break;
                case "JGT":
                    if (compareResult > 0) jumpTo(ltrue);
                    else jumpTo(lfalse);
                    break;
                case "JLT":
                    if (compareResult < 0) jumpTo(ltrue);
                    else jumpTo(lfalse);
                    break;
                case "JLE":
                    if (compareResult > 0) jumpTo(lfalse);
                    else jumpTo(ltrue);
                    break;
                case "JGE":
                    if (compareResult < 0) jumpTo(lfalse);
                    else jumpTo(ltrue);
                    break;
                default:
                    throw new RuntimeException("unknown ir jump inst:" + opCode);
            }
        }
    }

    class callInst extends irInst {
        private String calleeName;
        private String returnReg;
        private String classThisPointer;
        private ArrayList<String> parameters;

        public callInst(String[] arr) {
            super("CAL");
            calleeName = arr[1];
            returnReg = arr[2];
            parameters = new ArrayList<String>();
            if (arr[3].equals("null")) classThisPointer = null;
            else classThisPointer = arr[3];
            for (int i = 4; i < arr.length; ++i)
                parameters.add(arr[i]);
        }

        @Override
        public void run() {
            callMethod(calleeName, classThisPointer, parameters, returnReg);
        }
    }

    class returnInst extends irInst {
        public String retReg = null;
        public returnInst(String[] rets) {
            super("RET");
            if(rets.length > 1) retReg = rets[1];
        }

        @Override
        public void run() {
            methodReturn();
            if(retReg != null){
                if(retReg.charAt(0) == '$' && !registerHeap.containsKey(retReg)) write(globalReturnReg, 0);
                else{
                   // System.out.println("ret " + retReg + read(retReg));
                    write(globalReturnReg, read(retReg));
                }
            }
        }
    }

    class labelInst extends irInst {
        public labelInst(String _label) {
            super(_label);
        }

        @Override
        public void run() {
            return;
        }
    }

    class irMethod {
        private ArrayList<String> paras;
        private ArrayList<String> localVars;
        private HashMap<Integer, irInst> insts;
        private String name;
        private String thisPointer;
        private boolean retFlag;

        public irMethod(String _name) {
            name            = _name;
            paras           = new ArrayList<String>();
            localVars       = new ArrayList<String>();
            insts           = new HashMap<Integer, irInst>();
            retFlag         = false;
            thisPointer     = null;
        }

        public void run() {
            Integer startNum = labelToLineNumber.get("%" + name);
            if (startNum == null) throw new RuntimeException("not found the enterance line of method " + name);
            programCounter = startNum + 1;
            while (!retFlag) {
                //comment for search progccccc
                //System.out.println(programCounter);
                /*if (programCounter == 38) {
                    int a = 1;
                }*/
                irInst i = insts.get(programCounter);
                if (i == null) break;
                else {
                    // System.out.println("Running " + programCounter);
                    i.run();
                }
                programCounter++;
            }
        }

    }

    class methodInfo {
        public irMethod meth;
        public int programPointer;
        public LinkedList<Pair<String, Integer>> varibales;
        public methodInfo(irMethod _meth, int _programPointer){
            meth = _meth;
            programPointer =  _programPointer;
            varibales = new LinkedList<Pair<String, Integer>>();
        }
    }

    private enum STATUS {
        STATIC, TEXT
    }

    private final static int memorySize = (1 << 30);  //256 MB memory
    private final static int nonStaticStart = (1 << 18); //16 MB static area fixed memory start
    private final static String globalReturnReg = "$__global_return_register";
    private STATUS current_state;
    private String fileName;
    private HashMap<String, Integer> registerHeap;
    private HashMap<String, Integer> staticAddressMapper;
    private HashMap<String, irMethod> methodMap;
    private HashMap<String, Integer> labelToLineNumber;
    private LinkedList<methodInfo> callStack;
    private byte[] memory;
    private BufferedReader reader;
    private int currentLineNumber;
    private int staticTop;
    private int memoryBottom;
    private int compareResult;
    private int programCounter;
    private String output;
    private irMethod currentMethodParsing;
    private irMethod currentMethodRunning;
    private Scanner input;

    public IRInterpreter(String _fileName) throws IOException {
        fileName = _fileName;
        current_state = STATUS.STATIC;
        registerHeap = new HashMap<String, Integer>();
        staticAddressMapper = new HashMap<String, Integer>();
        methodMap = new HashMap<String, irMethod>();
        labelToLineNumber = new HashMap<String, Integer>();
        callStack = new LinkedList<methodInfo>();
        memory = new byte[memorySize];
        reader = new BufferedReader(new FileReader(fileName));
        staticTop = 0;
        output = "";
        input = new Scanner(System.in);
        memoryBottom = nonStaticStart;
        registerHeap.put(globalReturnReg, 0);
        for (int i = 0; i < memorySize; ++i) memory[i] = 0;
    }

    private void alignToFour() {
        while (staticTop % 4 != 0) ++staticTop;
        while (memoryBottom % 4 != 0) ++memoryBottom;
    }

    private void writeMemory(int addr, int data) {
        //System.out.println("write" + addr + ":" + data + " at line " + programCounter);
        //low addr for low byte
        memory[addr] = (byte) (data & 0xff);
        memory[addr + 1] = (byte) ((data >> 8) & 0xff);
        memory[addr + 2] = (byte) ((data >> 16) & 0xff);
        memory[addr + 3] = (byte) ((data >> 24) & 0xff);
    }

    private int readMemory(int addr) {
        int ret = 0;
        ret |= memory[addr] & 0xff;
        ret |= (memory[addr + 1] & 0xff) << 8;
        ret |= (memory[addr + 2] & 0xff) << 16;
        ret |= (memory[addr + 3] & 0xff) << 24;
        //System.out.println("get" + addr + ":" + ret  + " at line " + programCounter);
        return ret;
    }

    private void irCompare(String op1, String op2) {
        int a = read(op1);
        int b = read(op2);
        if (a > b) compareResult = 1;
        else if (a == b) compareResult = 0;
        else compareResult = -1;
    }

    private void parseStaticData(String line) {
        int f = 0;
        int size = 0;
        boolean stringFlag = false;
        String tmpSize = "";
        String staticName = "";
        String value = null;
        char c;
        for (int i = 0; i < line.length(); ++i) {
            switch (f) {
                case 0:
                    c = line.charAt(i);
                    if (c == '[') {
                        f = 1;
                        staticName += c;
                    } else tmpSize += c;
                    break;
                case 1:
                    c = line.charAt(i);
                    staticName += c;
                    if (c == ']') f = 2;
                    break;
                case 2:
                    c = line.charAt(i);
                    if (c == '\"') {
                        stringFlag = true;
                        value = "";
                        f = 3;
                    } else if (c <= '9' && c >= '0') {
                        value = "" + c;
                        f = 4;
                    }
                    break;
                case 3:
                    c = line.charAt(i);
                    if (c == '\"' && i == line.length() - 1)
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
        if (value != null) {
            if (stringFlag) {
                writeMemory(addr, staticTop);
                writeMemory(staticTop, size - Configure.PTR_SIZE);
                staticTop += Configure.PTR_SIZE;
                value = StringParser.parseString(value);
                for (int i = 0; i < value.length(); ++i) {
                    memory[staticTop] = (byte) value.charAt(i);
                    staticTop++;
                }
                memory[staticTop] = '\0';
                staticTop++;
                //alignToFour();
            } else {
                writeMemory(addr, Integer.valueOf(value));
                //staticTop += Configure.PTR_SIZE;
            }
        }
    }

    private void parseInstruction(String line) {
        String[] arr = line.trim().split("\\s+");
        switch (arr[0]) {
            case "<METHO>":
                irMethod mth = new irMethod(arr[1]);
                currentMethodParsing = mth;
                methodMap.put(arr[1], mth);
                break;
            case "<PARA>":
                currentMethodParsing.paras.add(arr[1]);
                break;
            case "<LOCAL>":
                registerHeap.put(arr[1], 0);
                currentMethodParsing.localVars.add(arr[1]);
                break;
            case "<THIS>":
                currentMethodParsing.thisPointer = arr[1];
                break;
            default:
                currentMethodParsing.insts.put(currentLineNumber, buildInst(arr));
                break;
        }
    }

    private String removeBrac(String s) {
        String ret = "";
        boolean st = false;
        char c;
        for (int i = 0; i < s.length(); ++i) {
            c = s.charAt(i);
            if (st) ret += c;
            if (c == '[') st = true;
            else if (c == ']') break;
        }
        return ret;
    }

    private Pair<Integer, String> removeNumberPrefix(String s) {
        Pair<Integer, String> ret = new Pair<Integer, String>();
        String num = "";
        String op = "";
        char c;
        boolean f = false;
        for (int i = 0; i < s.length(); ++i) {
            c = s.charAt(i);
            if (c == '[') f = true;
            if (!f) num += c;
            else op += c;
        }
        if (!num.equals("")) ret.first = Integer.valueOf(num);
        else ret.first = null;
        ret.second = op;
        return ret;
    }

    private int resolveMemoryAddres(String memOp) {
        int f = 0;
        String regName = "";
        String num = null;
        String pos = null;
        char c;
        for (int i = 0; i < memOp.length(); ++i) {
            switch (f) {
                case 0:
                    if (memOp.charAt(i) == '[') f = 1;
                    break;
                case 1:
                    c = memOp.charAt(i);
                    if (c == '+') f = 2;
                    else if (c == ']') break;
                    else regName += c;
                    break;
                case 2:
                    c = memOp.charAt(i);
                    if (c >= '0' && c <= '9') {
                        if (num == null) num = "" + c;
                        else num += c;
                    }
                    if (c == '*') f = 3;
                    else if (c == ']') break;
                    break;
                case 3:
                    c = memOp.charAt(i);
                    if (c == ']') break;
                    if (pos == null) pos = "";
                    pos += c;
            }
        }
        int baseAddr = 0;
        int ret = 0;
        if (regName.charAt(0) != '$') baseAddr = staticAddressMapper.get('[' + regName + ']');
        else baseAddr = read(regName);
        if (num == null && pos == null) ret = baseAddr;
        else if (num != null && pos == null) ret = baseAddr + Integer.valueOf(num);
        else if (num != null && pos != null) ret = baseAddr + Integer.valueOf(num) * read(pos);
        else
            throw new RuntimeException("unknwon memory parttern");
        if (ret < 0)
            throw new RuntimeException("Why there is minus addr???");
        else return ret;
    }

    private int read(String op) {
        char c = op.charAt(0);
        if (c == '$') {
            Integer ret = registerHeap.get(op);
            if (ret == null) throw new RuntimeException("use of not defined reg " + op + " at " + programCounter);
            else return ret;
        } else {
            Pair<Integer, String> processed = removeNumberPrefix(op);
            Integer num = processed.first;
            String pop = processed.second;
            if (num != null && pop.equals("")) return num;
            else if (num != null && pop.charAt(0) == '['){
                int addr = resolveMemoryAddres(pop);
                int ret = readMemory(addr);
                //if(programCounter == 435)System.out.println("read " + pop  + " : " + addr + " and get " + ret + " at " + programCounter);
                return ret;
            }
            else
                throw new RuntimeException("unknown oprand " + op);
        }
    }

    private void write(String op, int value) {
        char c = op.charAt(0);
        if (c == '$') {
            /*if(op.equals("$_GLOBAL_METHOD_gcd_x"))
                System.out.println("write " + value + " to " + op + " at line " + programCounter);*/
            //if(op.equals("$@main_tmp106"))
                //System.out.println("write " + value + " to " + op + " at line " + programCounter);
            Integer ret = registerHeap.get(op);
            if (ret == null) registerHeap.put(op, value);
            else registerHeap.replace(op, value);
        } else {
            Pair<Integer, String> processed = removeNumberPrefix(op);
            Integer num = processed.first;
            String pop = processed.second;
            if (num != null && pop.equals(""))
                throw new RuntimeException("write constant????");
            else if (num != null && pop.charAt(0) == '['){
                int addr  = resolveMemoryAddres(pop);
                //if(programCounter == 469 || programCounter == 455) System.out.println("write " + pop + ":" + addr + " with " + value + " at line " + programCounter);
                writeMemory(addr , value);
            }
            else throw new RuntimeException("unknown oprand " + op);
        }
    }

    private int malloc(int sizeAsByte) {
        //System.out.println("malloc " + sizeAsByte + " at line " + programCounter);
        int ret = memoryBottom;
        memoryBottom += sizeAsByte;
        return ret;
    }

    private methodInfo callBackUp() {
        methodInfo m = new methodInfo(currentMethodRunning, programCounter);
        currentMethodRunning.localVars.forEach(s -> {
            int val = read(s);
            //System.out.println("back up " + s + " with val " + val);
            m.varibales.add(new Pair<String, Integer>(s, val));
        });
        return m;
    }

    private void retRestore(methodInfo info){
        currentMethodRunning = info.meth;
        programCounter = info.programPointer;
        info.varibales.forEach(p->{
            //System.out.println("restore " + p.first + " with val " + p.second);
            write(p.first, p.second);
        });
    }

    private String processPrint(String pointer){
        Integer startAddr = read(pointer);
        String ret = "";
        if(startAddr == null) throw new RuntimeException("try to read undefined register");
        startAddr += Configure.PTR_SIZE;
        for(int i = startAddr; memory[i] != '\0'; ++i) ret += (char)memory[i];
        return ret;
    }

    private void callMethod(String name,String classThisPointer, ArrayList<String> para, String returnReg){
        switch (name){
            case "%@_Zprintlns":
                if(classThisPointer != null) throw new RuntimeException("why there is class pointer for print ???" + programCounter);
                else if(para.size() > 1) throw new RuntimeException("why there is more than one parameter");
                else{
                    String s = processPrint(para.get(0));
                    //System.out.println("[DEBUG]"+s);
                    output += s + "\n";
                }
                break;
            case "%@_Zprints":
                if(classThisPointer != null) throw new RuntimeException("why there is class pointer for print ???");
                else if(para.size() > 1) throw new RuntimeException("why there is more than one parameter");
                else output += processPrint(para.get(0));
                break;
            case "%@_ZtoStringi":
                int a = read(para.get(0));
                String s = a + "";
                int addr = malloc(s.length() + 1 + Configure.PTR_SIZE);
                writeMemory(addr, s.length());
                int st = addr + Configure.PTR_SIZE;
                for(int i = 0; i < s.length(); ++i) {
                   // System.out.println("toString write " + (st + i));
                    memory[st + i] = (byte) s.charAt(i);
                }
                memory[st + s.length()] = '\0';
                write(returnReg, addr);
                break;
            case "%@_Zmalloci":
                write(returnReg, malloc(read(para.get(0))));
                break;
            case "%@_ZgetInt":
                int ipt = Integer.parseInt(input.nextLine());
                write(returnReg, ipt);
                break;
            case "%@_Zstrcatss":
                String sa = "";
                String sb = "";
                int addra = read(para.get(0)) + Configure.PTR_SIZE;
                int addrb = read(para.get(1)) + Configure.PTR_SIZE;
                //System.err.println("[start] " + addra + " : " + addrb +" : "+programCounter);
                for(int i = addra; memory[i] !='\0'; ++i) sa += (char) memory[i];
                for(int i = addrb; memory[i] !='\0'; ++i) sb += (char) memory[i];
                sa += sb;
                //System.err.println("[cat]" + sa);
                int maddr = malloc(Configure.PTR_SIZE + sa.length() + 1);
                writeMemory(maddr, sa.length());
                int addrst = maddr + Configure.PTR_SIZE;
                for(int i = 0; i < sa.length(); ++i) memory[addrst + i] = (byte)sa.charAt(i);
                memory[addrst + sa.length()] = '\0';
                //System.out.println(addrst + sa.length());
                write(returnReg, maddr);
                break;
            case "%@_ZgetString":
                String sipt = input.nextLine();
                int saddr = malloc(sipt.length() + Configure.PTR_SIZE + 1);
                writeMemory(saddr, sipt.length());
                int staddr = saddr + Configure.PTR_SIZE;
                for(int i = 0; i < sipt.length(); ++i) memory[staddr + i] = (byte)sipt.charAt(i);
                memory[staddr + sipt.length()] = '\0';
                write(returnReg, saddr);
                break;
            case "%@_Zstring4ordi":
                int sptr = read(classThisPointer);
                int pos = read(para.get(0));
                write(returnReg, memory[sptr + Configure.PTR_SIZE + pos]);
                //System.out.println("ordi " + (char)memory[sptr + Configure.PTR_SIZE + pos]);
                break;
            case "%@_Zstring4substringii":
                sptr = read(classThisPointer);
                int pst = read(para.get(0));
                int ped = read(para.get(1));
                addr = malloc(ped - pst + 2 + Configure.PTR_SIZE);
                sptr += Configure.PTR_SIZE;
                writeMemory(addr, ped - pst  + 1);
                addr += Configure.PTR_SIZE;
                //System.out.print("[sbs]");
                for(int i = pst; i <= ped;  ++i){
                    //System.out.print((int)memory[i + sptr]);
                    memory[addr + i - pst] =  memory[i + sptr];
                }
                //System.out.println("[end]");
                memory[addr + ped - pst + 1] = '\0';
                write(returnReg, addr - Configure.PTR_SIZE);
                break;
            case "%@_Zstrcmpss":
                sa = "";
                sb = "";
                addra = read(para.get(0)) + Configure.PTR_SIZE;
                addrb = read(para.get(1)) + Configure.PTR_SIZE;
                for(int i = addra; memory[i] !='\0'; ++i) sa += (char) memory[i];
                for(int i = addrb; memory[i] !='\0'; ++i) sb += (char) memory[i];
                int cmpres = sa.compareTo(sb);
                if(cmpres > 0) write(returnReg, 1);
                else if(cmpres == 0) write(returnReg, 0);
                else write(returnReg, -1);
                break;
            case "%@_Zstring4parseInt":
                addr = read(classThisPointer);
                sa = "";
                for(int i = addr + Configure.PTR_SIZE; memory[i] != '\0'; ++i) sa += (char) memory[i];
                write(returnReg, builtinParseInt(sa));
                break;
            default:
                name = name.substring(1, name.length());
                //throw new RuntimeException("in construction");
                irMethod newMethodToRun = methodMap.get(name);
                if(newMethodToRun == null) throw new RuntimeException("method " + name  + " not found!!!");
                else{
                    methodInfo originInfo = callBackUp();
                    if(classThisPointer != null){
                        if(newMethodToRun.thisPointer == null) throw new RuntimeException("try to call none class memeber function with this pointer");
                        else write(newMethodToRun.thisPointer, read(classThisPointer));
                    }
                    if(para.size() != newMethodToRun.paras.size()) throw new RuntimeException("parameter number not match!!");
                    else{
                        int[] vars = new int[para.size()];
                        //gather variables
                        //System.out.print(name +  " ");
                        for(int i = 0; i < para.size(); ++i){
                            vars[i] = read(para.get(i));
                          //  System.out.print(vars[i] + " ");
                        }
                      //  System.out.print("\n");
                        //put variables
                        for(int i = 0; i < para.size(); ++i) write(newMethodToRun.paras.get(i), vars[i]);
                    }
                    //System.out.println("\n");
                    currentMethodRunning = newMethodToRun;
                    //System.out.println("call " + currentMethodRunning.name);
                    currentMethodRunning.run();
                    //System.err.println("call " + currentMethodRunning.name + " return with value" + read(globalReturnReg));
                    currentMethodRunning.retFlag = false;
                    retRestore(originInfo);
                    write(returnReg, read(globalReturnReg));
                }
                break;
        }
        return;
    }

    private void methodReturn(){
        currentMethodRunning.retFlag = true;
    }

    private int builtinParseInt(String s){
        String numPrex = "";
        for(int i = 0; i < s.length(); ++i)
            if(s.charAt(i) >= '0' && s.charAt(i) <= '9')
                numPrex += s.charAt(i);
        if(numPrex.equals("")) return 0;
        else return Integer.parseInt(numPrex);
    }

    private void jumpTo(String label){
        Integer ret = labelToLineNumber.get(label);
        if(ret == null) throw new RuntimeException("try to jump to label not exsit!");
        else{
           // System.out.println("[DEBUG] jump to " + label);
            programCounter = ret;
        }
    }

    private irInst buildInst(String[] arr){
        if(arr[0].charAt(0) == '%') {
            labelToLineNumber.put(arr[0], currentLineNumber);
            return new labelInst(arr[0]);
        }
        else if(arr[0].equals("RET"))   return new returnInst(arr);
        else if(arr[0].equals("CAL"))   return new callInst(arr);
        else if(arr[0].equals("JMP"))   return new djumpInst(arr[1]);
        else if(arr[0].charAt(0) == 'J')return new cjumpInst(arr[0], arr[1], arr[2]);
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
                if (current_state.equals(STATUS.STATIC))
                    parseStaticData(line);
                else
                    parseInstruction(line);
            }
        } while(true);
    }

    public void runIR() throws IOException{
        parseFile();
        irMethod initm = methodMap.get(NameMangler.initMethod);
        if(initm != null){
            currentMethodRunning = initm;
            initm.run();
        }
        irMethod mainm = methodMap.get(NameMangler.mainMethodName);
        if(mainm == null) throw new RuntimeException("No main method in ir!!!!!");
        currentMethodRunning = mainm;
        currentMethodRunning.run();
        System.out.print("[RESULT]:\n" + output + "[END]\n");
        System.out.println("[EXITCODE] " + read(globalReturnReg));
        System.out.print("[STATICS]:\n");
        System.out.println("Static top: " + staticTop);
        System.out.println("Heap memory used: " + (memoryBottom - nonStaticStart));
        return;
    }
}
