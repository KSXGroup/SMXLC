package kstarxin.ir;

import kstarxin.ir.instruction.*;
import kstarxin.ir.operand.*;
import kstarxin.parser.MxStarParser;
import kstarxin.utilities.OperatorTranslator;
import kstarxin.compiler.Configure;

import java.io.*;
import java.util.*;

import static kstarxin.compiler.Configure.PRINT_LIVEINOUT;

public class IRPrinter {
    public final static String           labelHeader             = "%";
    public final static String           staticAreaHeader        = "<STAT>";
    public final static String           codeAreaHeader          = "<CODE>";
    public final static String           classThisPointerHeader  = "<THIS>\t";
    public final static String           localVariableHeader     = "<LOCAL>\t";
    public final static String           parameterHeader         = "<PARA>\t";
    public final static String           methodHeader            = "<METHO>\t";

    private IRProgram               ir;
    private Method                  init;
    private HashMap<String, Method> map;
    private PrintStream             irPrintStream;

    public IRPrinter(IRProgram _ir, PrintStream _irPrintStream){
        ir              = _ir;
        irPrintStream   = _irPrintStream;
        init            = ir.getInitMethod();
        map             = ir.getMethodMap();
    }
    public void printIR(){
        //print global variables
        irPrintStream.println(staticAreaHeader);
        ir.getGlobalVariableMap().values().forEach(var -> {
            if(var instanceof StaticPointer) irPrintStream.println(((StaticPointer)var).getInitName());
            else if(var instanceof StaticString) irPrintStream.println(((StaticString) var).getInitName());
            else throw new RuntimeException("no such global var type");
        });
        irPrintStream.println();
        //print init part first
        irPrintStream.println(codeAreaHeader);
        map.values().forEach(this::printMethod);
    }

    public void printMethod(Method m){
        if(!m.isBuiltin) {
            m.dfs();
            irPrintStream.println("\n" + methodHeader + m.hintName);
            m.parameters.forEach(this::printParameter);
            if(m.classThisPointer != null) irPrintStream.println(classThisPointerHeader + m.classThisPointer.getDisplayName());
            m.localVariables.values().forEach(this::printLocalVariable);
            LinkedHashSet<BasicBlock> blockList = m.basicBlockInDFSOrder;
            blockList.forEach(bb -> {
                irPrintStream.print("\n");
                if (bb.blockLabel != null) irPrintStream.println(labelHeader + bb.blockLabel);
                for (Instruction inst = bb.getBeginInst(); inst != null; inst = inst.next) printInst(inst);
            });
        }
    }

    public void printInst(Instruction inst){
        if     (inst instanceof BinaryArithmeticInstruction )    printBinaryInstruction ((BinaryArithmeticInstruction   ) inst);
        else if(inst instanceof UnaryInstruction            )    printUnaryInstruction  ((UnaryInstruction              ) inst);
        else if(inst instanceof LoadInstruction             )    printLoadInstruction   ((LoadInstruction               ) inst);
        else if(inst instanceof StoreInstruction            )    printStoreInstruction  ((StoreInstruction              ) inst);
        else if(inst instanceof ReturnInstruction           )    printReturnInstruction ((ReturnInstruction             ) inst);
        else if(inst instanceof ConditionJumpInstruction    )    printConditionJump     ((ConditionJumpInstruction      ) inst);
        else if(inst instanceof DirectJumpInstruction       )    printDirectJump        ((DirectJumpInstruction         ) inst);
        else if(inst instanceof CompareInstruction          )    printCompareInstruction((CompareInstruction            ) inst);
        else if(inst instanceof MoveInstruction             )    printMoveInstruciton   ((MoveInstruction               ) inst);
        else if(inst instanceof CallInstruction             )    printMethodCall        ((CallInstruction               ) inst);
    }

    private void printBinaryInstruction(BinaryArithmeticInstruction inst){
        String s = OperatorTranslator.operatorToSymbolicName(inst.op) + "\t\t" + inst.target.getDisplayName()  + "\t" +  inst.lhs.getDisplayName() + "\t"+ inst.rhs.getDisplayName();
        if(PRINT_LIVEINOUT) s += livenessInfo(inst);
        irPrintStream.println(s);
    }

    private void printUnaryInstruction(UnaryInstruction inst){
        String s = OperatorTranslator.operatorToSymbolicName(inst.op) + "\t\t" + inst.dest.getDisplayName() + "\t" + inst.src.getDisplayName();
        if(PRINT_LIVEINOUT) s += livenessInfo(inst);
        irPrintStream.println(s);
    }

    private void printLoadInstruction(LoadInstruction inst){
        String s = "LOA\t\t" +  inst.dest.getDisplayName() + "\t" + inst.src.getDisplayName() + "\t";
        if(PRINT_LIVEINOUT) s += livenessInfo(inst);
        irPrintStream.println(s);
    }

    private void printStoreInstruction(StoreInstruction inst){
        String s = "STO\t\t" + inst.dest.getDisplayName() + "\t" + inst.src.getDisplayName() + "\t";
        if(PRINT_LIVEINOUT) s += livenessInfo(inst);
        irPrintStream.println(s);
    }

    private void printReturnInstruction(ReturnInstruction ret){
        String s = "";
        if(ret.returnValue != null) {
            s += "RET\t\t" + ret.returnValue.getDisplayName();
        } else s += "RET";
        if(PRINT_LIVEINOUT) s += livenessInfo(ret);
        irPrintStream.println(s);
    }

    private void printConditionJump(ConditionJumpInstruction inst){
        String instName = null;
        switch (inst.op){
            case MxStarParser.EQ:
                instName = "JEQ";
                break;
            case MxStarParser.NEQ:
                instName = "JNE";
                break;
            case MxStarParser.GT:
                instName = "JGT";
                break;
            case MxStarParser.LT:
                instName = "JLT";
                break;
            case MxStarParser.LE:
                instName = "JLE";
                break;
            case MxStarParser.GE:
                instName = "JGE";
                break;
            default:
                throw new RuntimeException("what shit opeator do you in conditional jump ??????");
        }
        String s = instName + "\t\t%" + inst.trueTarget.blockLabel + "\t%" + inst.falseTarget.blockLabel;
        if(PRINT_LIVEINOUT) s += livenessInfo(inst);
        irPrintStream.println(s);
    }

    private void printCompareInstruction(CompareInstruction inst){
        String s = "CMP\t\t" + inst.lhs.getDisplayName() + "\t" + inst.rhs.getDisplayName();
        if(PRINT_LIVEINOUT) s += livenessInfo(inst);
        irPrintStream.println(s);
    }

    private void printMoveInstruciton(MoveInstruction inst){
        String s = "MOV\t\t" + inst.dest.getDisplayName() + "\t" + inst.src.getDisplayName();
        if(PRINT_LIVEINOUT) s += livenessInfo(inst);
        irPrintStream.println(s);
    }

    private void printDirectJump(DirectJumpInstruction inst){
        String s = "JMP\t\t%" + inst.target.blockLabel;
        if(PRINT_LIVEINOUT) s += livenessInfo(inst);
        irPrintStream.println(s);
    }

    private void printMethodCall(CallInstruction inst){
        String retReg = inst.returnValue != null ? inst.returnValue.getDisplayName() : "null" ;
        String classPtr = inst.isClassMemberCall? inst.classThisPointer.getDisplayName() : "null";
        if(inst.callee == null)
            throw new RuntimeException("shit!");
        String toPrint ="CAL\t\t" + inst.callee.getDisplayName() + "\t" + retReg + "\t" + classPtr + "\t";
        for (Operand parameter : inst.parameters) {
            toPrint += parameter.getDisplayName() + "\t";
        }
        if(PRINT_LIVEINOUT) toPrint += livenessInfo(inst);
        irPrintStream.println(toPrint);
    }

    private void printLocalVariable(VirtualRegister local){
        irPrintStream.println(localVariableHeader + local.getDisplayName());
    }

    private void printParameter(VirtualRegister para){
        irPrintStream.println(parameterHeader + para.getDisplayName());
    }

    private String livenessInfo(Instruction i){
        String s = "\t\tLIVEIN:";
        for(VirtualRegister vreg : i.liveIn) s += vreg.getDisplayName() + "\t";
        s += "\t\tLIVEOUT:";
        for(VirtualRegister vreg : i.liveOut)s += vreg.getDisplayName() + "\t";
        return s;
    }
}
