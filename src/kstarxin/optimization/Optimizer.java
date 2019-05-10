package kstarxin.optimization;

import kstarxin.compiler.Configure;
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
    LocalValueNumbering     localValueNumbering;
    Memorization            memorization;
    NaiveStrengthReduction  naiveStrengthReduction;
    public Optimizer(IRProgram _ir){
        ir                      = _ir;
        simplifier              = new CFGSimplifier(_ir);
        memorization            = new Memorization(_ir);
        inliner                 = new FunctionInliner(_ir, Configure.INLINE_LEVEL, Configure.RECURSIVE_INLINE_LEVEL);
        globalVariableOptimizer = new GlobalVariableOptimizer(_ir);
        localValueNumbering     = new LocalValueNumbering(_ir);
        naiveStrengthReduction  = new NaiveStrengthReduction(_ir);
        livenessAnalyzer        = new LivenessAnalyzer(_ir);
    }

    public void run(){
        simplifier.cleanUp();
        memorization.run();
        inliner.run();
        simplifier.run();
        globalVariableOptimizer.run();
        localValueNumbering.run();
        //naiveStrengthReduction.run();
        //livenessAnalyzer.run();
    }
}
