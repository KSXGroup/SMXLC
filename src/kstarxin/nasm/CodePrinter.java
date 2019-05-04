package kstarxin.nasm;

import kstarxin.compiler.Configure;
import kstarxin.ir.IRBaseVisitor;
import kstarxin.ir.IRProgram;
import kstarxin.ir.Method;
import kstarxin.ir.asmir.ASMLevelIRInstruction.*;
import kstarxin.ir.asmir.ASMLevelIRMethod;
import kstarxin.ir.asmir.ASMLevelIRProgram;
import kstarxin.ir.asmir.ASMLevelIRVisitor;
import kstarxin.ir.instruction.*;
import kstarxin.ir.operand.*;
import kstarxin.ir.operand.physical.*;
import kstarxin.parser.MxStarParser;
import kstarxin.utilities.NameMangler;
import kstarxin.utilities.OperatorTranslator;
import kstarxin.utilities.OperatorTranslator.*;
import kstarxin.utilities.StringParser;

import java.io.*;

public class CodePrinter implements ASMLevelIRVisitor<String> {

    public static final String              DEFALUT_REL         = "\ndefault rel\n";
    public static final String              SECTION_TEXT        = "\nSECTION\t.text\n";
    public static final String              SECTION_DATA        = "\nSECTION\t.data\n";
    public static final String              SECTION_BSS         = "\nSECTION\t.bss\n";
    public static final String              SECTION_RODATA      = "\nSECTION\t.rodata\n";
  //  public static final String              SECTION_INIT_ARRAY  = "\nSECTION\t.init_array\n";
    public static final String              GLOBAL              = "global";
    public static final String              QWORD               = "QWORD ";
    public static final String              DWORD               = "DWORD ";
    public static final String              WORD                = "WORD ";
    public static final String              BYTE                = "BYTE ";
    public static final String              EXTERN              = "extern printf, scanf, malloc, strcmp, sscanf, puts, memcpy, __isoc99_scanf\n"; //extern some c functions
    public static final String              INT_PRINT_FORMAT    = "\'%d\\\n'";
    public static final String              BUILTIN_FUNCTION    = "/home/kstarxin/code/compiler/lib/lib.asm";

    private             ASMLevelIRProgram   lir;
    private             String              nasm;
    private             PrintStream         asmPrintStream;
    private             BufferedReader      reader;
    private             boolean             ifAllocated;
    private             boolean             inLEA;
    private             String              currentMemorySizeFlag;

    private boolean isMemory(Operand op){
        if(op instanceof StackSpace || op instanceof PhysicalRegister) throw new RuntimeException();
        if(op instanceof Memory || op instanceof StaticString || op instanceof StaticPointer) return true;
        else if(op instanceof VirtualRegister && ((VirtualRegister) op).spaceAllocatedTo instanceof StackSpace) return true;
        else return false;
    }


    public CodePrinter(ASMLevelIRProgram _ir, PrintStream _asmPrintStream){
        nasm            = "";
        lir             = _ir;
        ifAllocated     = _ir.ifAllocated;
        //ifAllocated = false;
        asmPrintStream  = _asmPrintStream;
        inLEA           = false;
        currentMemorySizeFlag = QWORD;
    }

    //callee should save RBX, RBP, R12, R13, R14, R15 according to SYSTEM V AMD64 ABI (registers which callee should save to stack if callee want to use) (callee saved register)
    //caller saved: RAX, RCX, RDX, RDI, RSI, RSP, R8, R9, R10, R11
    //I think RSP RBP should not be allocated
    //I decide to use EAX and ECX as tmp register now
    //naviely, registers can be used now are EBX, EDX, EDI, ESI, R8D, R9D, R10D, R11D, R12D, R13D, R14D, R15D

    public void printCode() throws Exception{
        printStartInfo();
        printTextPart();
        printNonTextPart();
        nasm += "\n\n";
        if(ifAllocated) {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(BUILTIN_FUNCTION)));
            String line = null;
            while ((line = reader.readLine()) != null) {
                nasm += line + "\n";
            }
        }
        asmPrintStream.print(nasm);
    }

    public void printStartInfo(){
        lir.getMethodMap().values().forEach(method -> {
            if(!method.name.equals(NameMangler.mallocTrue)) {
                nasm += GLOBAL + "\t" + method.name + "\n";
            }
        });
        nasm += "\n";
        nasm += EXTERN + "\n";
    }

    public void printTextPart(){
        lir.getMethodMap().values().forEach(method -> {
            if(!method.isBuiltIn) {
                method.basicBlocks.forEach(asmBasicBlock -> {
                    if (asmBasicBlock.nasmLabel != null)
                        nasm += "\n" + asmBasicBlock.nasmLabel + ":\n";
                    asmBasicBlock.insts.forEach(this::visit);
                    if(Configure.PRINT_REG_ALLOC_LIVEOUT){
                        nasm += "\n\nlive out ot bb " + asmBasicBlock.nasmLabel + " is:\n";
                        int cnt = 0;
                        for(VirtualRegister vreg : asmBasicBlock.liveOut){
                            nasm += vreg.getDisplayName() + "\t";
                            cnt++;
                            if(cnt % 5 == 0) nasm += "\n";
                        }
                        nasm += "\n\n";

                    }
                });
            }
        });
    }

    public void printNonTextPart(){
        nasm += SECTION_BSS;
        lir.getBSSSection().forEach(this::visit);
        nasm += SECTION_DATA;
        lir.getDataSection().forEach(this::visit);
        nasm += SECTION_RODATA;
        lir.getRomDataSection().forEach(this::visit);
    }

    @Override
    public String visit(ASMInstruction inst) {
        return inst.accept(this);
    }

    @Override
    public String visit(ASMBinaryInstruction inst) {
        if(inst.operator.equals(NASMInstructionOperator.SHL) || inst.operator.equals(NASMInstructionOperator.SAR)){
            if(inst.src instanceof VirtualRegister && ((VirtualRegister) inst.src).spaceAllocatedTo instanceof PhysicalRegister){
                nasm += inst.operator.toString() + "\t\t" + visit(inst.dst) + ", " + OperatorTranslator.to8bitLowRegister((PhysicalRegister) ((VirtualRegister) inst.src).spaceAllocatedTo).getNASMName() + "\n";
            }else nasm += inst.operator.toString() + "\t\t" + visit(inst.dst) + ", " + visit(inst.src) + "\n";
        } else nasm += inst.operator.toString() + "\t\t" + visit(inst.dst) + ", " + visit(inst.src) + "\n";
        return null;
    }

    @Override
    public String visit(ASMCallInstruction inst) {
        nasm += "CALL\t\t"  + inst.callee.name + "\n";
        return null;
    }

    @Override
    public String visit(ASMCDQInstruction inst) {
        nasm += "CQO\n";
        return null;
    }

    @Override
    public String visit(ASMCompareInstruction inst) {
        nasm += "CMP\t\t" + visit(inst.lhs) + ", " + visit(inst.rhs) + "\n";
        return null;
    }

    @Override
    public String visit(ASMConditionalJumpInstruction inst) {
        nasm += inst.op.toString() + "\t\t" + inst.falseTarget.nasmLabel + "\n";
        return null;
    }

    @Override
    public String visit(ASMDInstruction inst) {
        nasm += inst.nasmName + ":\n\tDQ\t\t" + inst.value + "\n";
        return null;
    }

    @Override
    public String visit(ASMDirectJumpInstruction inst) {
        nasm += "JMP\t\t" + inst.target.nasmLabel + "\n";
        return null;
    }


    @Override
    public String visit(ASMLEAInstruction inst) {
        inLEA = true;
        nasm += "LEA\t\t" + visit(inst.dst) + ", " + visit(inst.src) + "\n";
        inLEA = false;
        return null;
    }

    @Override
    public String visit(ASMLeaveInstruction inst) {
        nasm += "LEAVE\n";
        return null;
    }

    @Override
    public String visit(ASMMoveInstruction inst) {
        if(inst.op.equals(NASMInstructionOperator.MOV)) {
            nasm += inst.op.toString() + "\t\t" + visit(inst.dst) + ", " + visit(inst.src)+ "\n";
        } else if(inst.op.equals(NASMInstructionOperator.MOVZX)){
            String ssrc = "";
            if(!(inst.src instanceof VirtualRegister)){}
            else ssrc += OperatorTranslator.to8bitLowRegister((PhysicalRegister) ((VirtualRegister) inst.src).spaceAllocatedTo).getNASMName();
            nasm += inst.op.toString() + "\t\t" + visit(inst.dst) + ", " + ssrc + "\n";
        }
        return null;
    }

    @Override
    public String visit(ASMPopInstruction inst) {
        nasm += "POP\t\t" + visit(inst.op) + "\n";
        return null;
    }

    @Override
    public String visit(ASMPushInstruction inst) {
        nasm += "PUSH\t\t" + visit(inst.src) + "\n";
        return null;
    }

    @Override
    public String visit(ASMRESInstruction inst) {
        nasm += inst.nasmName + ":\n\tRESB\t\t" + inst.size + "\n";
        return null;
    }

    @Override
    public String visit(ASMReturnInstruction inst) {
        nasm += "RET\n";
        return null;
    }

    @Override
    public String visit(ASMRomDataInstruction inst) {
        nasm += inst.nasmName + ":\n" + StringParser.stringToHex(inst.content) + "\n";
        return null;
    }

    @Override
    public String visit(ASMSetInstruction inst) {
        nasm += inst.operator.toString() + "\t\t" + (OperatorTranslator.to8bitLowRegister((PhysicalRegister) inst.dst.spaceAllocatedTo)).getNASMName() + "\n";
        return null;
    }

    @Override
    public String visit(ASMUnaryInstruction inst) {
        nasm += inst.operator.toString() + "\t\t" + visit(inst.src) + "\n";
        return null;
    }

    @Override
    public String visit(Operand op) {
        return op.accept(this);
    }

    @Override
    public String visit(Address op) {
        return op.accept(this);
    }

    @Override
    public String visit(Constant op) {
        return op.accept(this);
    }

    @Override
    public String visit(Immediate op) {
        return op.value + "";
    }

    @Override
    public String visit(Label op) {
        return op.accept(this);
    }

    @Override
    public String visit(Memory op) {
        if(!ifAllocated) return op.getDisplayName();
        else{
            if(inLEA) return op.getNASMName();
            else return QWORD + op.getNASMName();
        }
    }

    @Override
    public String visit(Register op) {
        return op.accept(this);
    }

    @Override
    public String visit(StaticPointer op) {
        if(!ifAllocated) return op.getDisplayName();
        else return QWORD + op.getNASMName();
    }

    @Override
    public String visit(StaticString op) {
        if(!ifAllocated) return op.getDisplayName();
        else{
            if(inLEA) return op.getNASMName();
            else return QWORD + op.getNASMName();
        }
    }

    @Override
    public String visit(StringLiteral op) {
        if(inLEA) return op.getNASMName();
        else return op.nasmName;
    }

    @Override
    public String visit(VirtualRegister op) {
        if(!ifAllocated) return op.getDisplayName();
        else{
            if(op.spaceAllocatedTo instanceof StackSpace) return QWORD + op.spaceAllocatedTo.getNASMName();
            else return op.spaceAllocatedTo.getNASMName();
        }
    }
}
