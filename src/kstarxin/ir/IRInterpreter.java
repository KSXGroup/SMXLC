package kstarxin.ir;

import kstarxin.compiler.Configure;
import kstarxin.ir.*;
import kstarxin.utilities.StringParser;

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

        }
    }

    class twoOpInst extends irInst{
        public twoOpInst(String opCode, String opr1, String opr2){
            super(opCode);
        }
        @Override
        public void run(){
        }
    }

    class threeOpInst extends irInst{
        private String op1, op2;
        public threeOpInst(String opCode, String oprdest, String opr1, String opr2){
            super(opCode);
            op1 = opr1;
            op2 = opr2;
        }
        @Override
        public void run(){
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

        }
    }

    class cjumpInst extends irInst{
        private String t1;
        private String t2;
        public cjumpInst(String opCode, String _t1, String _t2){
            super(opCode);
            t1 = _t1;
            t2 = _t2;
        }

        @Override
        public void run() {
        }
    }

    class callInst extends irInst{
        public callInst(String[] paras){
            super("CAL");
        }
        @Override
        public void run() {
        }
    }

    class irMethod{
        private ArrayList<String> paras;
        private HashMap<Integer, irInst> insts;
        public irMethod(String name){}

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
                irMethod mth = new irMethod("shit");
                currentMethodParsing = mth;
                //methodMap.put()
                break;
        }
    }

    private void callMethod(String name, String[] para){
        return;
    }

    private irInst buildInst(String[] arr){
        return null;
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
    }
}
