package kstarxin.optimization;

import kstarxin.ir.*;
import kstarxin.ir.instruction.ConditionJumpInstruction;
import kstarxin.ir.instruction.DirectJumpInstruction;
import kstarxin.ir.instruction.Instruction;
import kstarxin.ir.instruction.ReturnInstruction;

public class Optimizer {
    IRProgram               ir;
    CFGSimplifier           simplifier;
    FunctionInliner         inliner;
    GlobalVariableOptimizer globalVariableOptimizer;
    LivenessAnalyzer        livenessAnalyzer;
    public Optimizer(IRProgram _ir){
        ir                      = _ir;
        simplifier              = new CFGSimplifier(_ir);
        inliner                 = new FunctionInliner(_ir, 4);
        globalVariableOptimizer = new GlobalVariableOptimizer(_ir);
        livenessAnalyzer        = new LivenessAnalyzer(_ir);
    }

    public void run(){
        simplifier.cleanUp();
        inliner.run();
        simplifier.run();
        globalVariableOptimizer.run();
       // livenessAnalyzer.run();
    }
}
