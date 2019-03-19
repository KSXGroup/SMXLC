package kstarxin.ir;

import kstarxin.ir.instruction.*;
import kstarxin.ir.operand.*;
import kstarxin.parser.MxStarParser;
import kstarxin.utilities.OperatorTranslator;

import java.io.*;
import java.util.*;

public class IRPrinter {
    public final static String           labelHeader             = "%";
    public final static String           staticAreaHeader        = "<STAT>";
    public final static String           codeAreaHeader          = "<CODE>";
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
        printMethod(init);
        map.values().forEach(this::printMethod);
    }

    void printMethod(Method m){
        m.bfs();
        if(!m.isBuiltin) {
            irPrintStream.println("\n" + methodHeader + m.hintName);
            m.parameters.forEach(this::printParameter);
            m.localVariables.values().forEach(this::printLocalVariable);
            List<BasicBlock> blockList = m.basicBlockInBFSOrder;
            blockList.forEach(bb -> {
                irPrintStream.print("\n");
                if (bb.blockLabel != null) irPrintStream.println(labelHeader + bb.blockLabel);
                for (Instruction inst = bb.getBeginInst(); inst != null; inst = inst.next) printInst(inst);
            });
        }
    }

    private void printInst(Instruction inst){
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
        irPrintStream.println(OperatorTranslator.operatorToSymbolicName(inst.op) + "\t\t" + inst.target.getDisplayName()  + "\t" +  inst.lhs.getDisplayName() + "\t"+ inst.rhs.getDisplayName());
    }

    private void printUnaryInstruction(UnaryInstruction inst){
        irPrintStream.println(OperatorTranslator.operatorToSymbolicName(inst.op) + "\t\t" + inst.dest.getDisplayName() + "\t" + inst.src.getDisplayName());
    }

    private void printLoadInstruction(LoadInstruction inst){
        irPrintStream.println("LOA\t\t" +  inst.dest.getDisplayName() + "\t" + inst.src.getDisplayName() + "\t" );
    }

    private void printStoreInstruction(StoreInstruction inst){
        irPrintStream.println("STO\t\t" + inst.dest.getDisplayName() + "\t" + inst.src.getDisplayName() + "\t" );
    }

    private void printReturnInstruction(ReturnInstruction ret){
        if(ret.returnValue != null) {
            irPrintStream.println("RET\t\t" + ret.returnValue.getDisplayName());
        } else irPrintStream.println("RET");
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
        irPrintStream.println(instName + "\t\t%" + inst.trueTarget.blockLabel + "\t%" + inst.falseTarget.blockLabel);
    }

    private void printCompareInstruction(CompareInstruction inst){
        irPrintStream.println("CMP\t\t" + inst.lhs.getDisplayName() + "\t" + inst.rhs.getDisplayName());
    }

    private void printMoveInstruciton(MoveInstruction inst){
        irPrintStream.println("MOV\t\t" + inst.dest.getDisplayName() + "\t" + inst.src.getDisplayName());
    }

    private void printDirectJump(DirectJumpInstruction inst){
        irPrintStream.println("JMP\t\t%" + inst.target.blockLabel);
    }

    private void printMethodCall(CallInstruction inst){
        String retReg = inst.returnValue != null ? inst.returnValue.getDisplayName() : "null" ;
        String classPtr = inst.isClassMemberCall? inst.classThisPointer.getDisplayName() : "null";
        String toPrint ="CAL\t\t" + inst.callee.getDisplayName() + "\t" + retReg + "\t" + classPtr + "\t";
        for (Operand parameter : inst.parameters) {
            toPrint += parameter.getDisplayName() + "\t";
        }
        irPrintStream.println(toPrint);
    }

    private void printLocalVariable(VirtualRegister local){
        irPrintStream.println(localVariableHeader + local.getDisplayName());
    }

    private void printParameter(VirtualRegister para){
        irPrintStream.println(parameterHeader + para.getDisplayName());
    }
}
