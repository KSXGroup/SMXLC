package kstarxin.optimization;

import kstarxin.ir.*;
import kstarxin.ir.instruction.ConditionJumpInstruction;
import kstarxin.ir.instruction.DirectJumpInstruction;
import kstarxin.ir.instruction.Instruction;
import kstarxin.ir.instruction.ReturnInstruction;

public class Optimizer {
    IRProgram ir;
    CFGSimplifier   simplifier;
    FunctionInliner inliner;

    public Optimizer(IRProgram _ir){
        ir          = _ir;
        simplifier  = new CFGSimplifier(_ir);
        inliner     = new FunctionInliner(_ir, 3);
    }

    public void run(){
        simplifier.cleanUp();
        inliner.run();
        simplifier.run();

    }
}
