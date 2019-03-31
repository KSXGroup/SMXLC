package kstarxin.optimization;

import kstarxin.ir.*;

public class Optimizer {
    IRProgram ir;
    CFGSimplifier   simplifier;
    FunctionInliner inliner;

    public Optimizer(IRProgram _ir){
        ir          = _ir;
        simplifier  = new CFGSimplifier(_ir);
        inliner     = new FunctionInliner(_ir, 1);
    }

    public void run(){
        simplifier.run();
        inliner.run();
    }
}
