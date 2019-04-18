package kstarxin.nasm;

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

import java.io.PrintStream;

public class CodePrinter implements ASMLevelIRVisitor<Void> {

    public static final String              DEFALUT_REL         = "default rel\n";
    public static final String              SECTION_TEXT        = "SECTION\t.text\n";
    public static final String              SECTION_DATA        = "SECTION\t.data\n";
    public static final String              SECTION_BSS         = "SECTION\t.bss\n";
    public static final String              SECTION_RODATA      = "SECTION\t.rodata\n";
    public static final String              SECTION_INIT_ARRAY  = "SECTION\t.init_array\n";
    public static final String              GLOBAL              = "global";
    public static final String              DWORD               = "DWORD ";
    public static final String              EXTERN              = "extern printf, scanf, malloc, strcmp, sscanf, puts\n"; //extern some c functions
    public static final String              INT_PRINT_FORMAT    = "\'%d\\\n'";

    private             ASMLevelIRProgram   lir;
    private             String              nasm;
    private             PrintStream         asmPrintStream;
    private             boolean             ifAllocated;

    public CodePrinter(ASMLevelIRProgram _ir, boolean _ifAllocated, PrintStream _asmPrintStream){
        nasm            = "";
        lir             = _ir;
        ifAllocated     = _ifAllocated;
        asmPrintStream  = _asmPrintStream;
    }

    //callee should save RBX, RBP, R12, R13, R14, R15 according to SYSTEM V AMD64 ABI (registers which callee should save to stack if callee want to use) (callee saved register)
    //caller saved: RAX, RCX, RDX, RDI, RSI, RSP, R8, R9, R10, R11
    //I think RSP RBP should not be allocated
    //I decide to use EAX and ECX as tmp register now
    //naviely, registers can be used now are EBX, EDX, EDI, ESI, R8D, R9D, R10D, R11D, R12D, R13D, R14D, R15D


    public void printCode(){
        nasm += DEFALUT_REL;
        printTextPart();
        printNonTextPart();
        asmPrintStream.print(nasm);
    }

    public void printTextPart(){
        return;
    }

    public void printNonTextPart(){
        nasm += SECTION_BSS;
        lir.getBSSSection().forEach(this::visit);
        nasm += SECTION_DATA;
        lir.getDataSection().forEach(this::visit);
        nasm += SECTION_RODATA;
        lir.getRomDataSection().forEach(this::visit);
        nasm += SECTION_INIT_ARRAY + "dq\t" + lir.getGlobalInit().name;
    }

    @Override
    public Void visit(ASMInstruction inst) {
        return inst.accept(this);
    }

    @Override
    public Void visit(ASMBinaryInstruction inst) {
        return null;
    }

    @Override
    public Void visit(ASMCallInstruction inst) {
        return null;
    }

    @Override
    public Void visit(ASMCDQInstruction inst) {
        nasm += "CDQ\n";
        return null;
    }

    @Override
    public Void visit(ASMCompareInstruction inst) {
        return null;
    }

    @Override
    public Void visit(ASMConditionalJumpInstruction inst) {
        return null;
    }

    @Override
    public Void visit(ASMDInstruction inst) {
        nasm += inst.nasmName + ":\n\tdd\t" + inst.value + "\n";
        return null;
    }

    @Override
    public Void visit(ASMDirectJumpInstruction inst) {
        return null;
    }

    @Override
    public Void visit(ASMLabelInstruction inst) {
        nasm += inst.nasmName + "\n";
        return null;
    }

    @Override
    public Void visit(ASMLEAInstruction inst) {
        return null;
    }

    @Override
    public Void visit(ASMLeaveInstruction inst) {
        nasm += "LEAVE\n";
        return null;
    }

    @Override
    public Void visit(ASMMoveInstruction inst) {
        return null;
    }

    @Override
    public Void visit(ASMPopInstruction inst) {
        return null;
    }

    @Override
    public Void visit(ASMPushInstruction inst) {
        return null;
    }

    @Override
    public Void visit(ASMRESInstruction inst) {
        nasm += inst.nasmName + ":\n\tRESB\t" + inst.size + "\n";
        return null;
    }

    @Override
    public Void visit(ASMReturnInstruction inst) {
        nasm += "RET\n";
        return null;
    }

    @Override
    public Void visit(ASMRomDataInstruction inst) {
        nasm += inst.nasmName + ":\n" + StringParser.stringToHex(inst.content) + "\n";
        return null;
    }

    @Override
    public Void visit(ASMSetInstruction inst) {
        return null;
    }

    @Override
    public Void visit(ASMUnaryInstruction inst) {
        return null;
    }
}
