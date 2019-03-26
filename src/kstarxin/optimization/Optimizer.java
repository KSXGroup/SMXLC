package kstarxin.optimization;

import kstarxin.ir.*;

public class Optimizer {
    IRProgram ir;
    CFGSimplifier simplifier;

    public Optimizer(IRProgram _ir){
        ir = _ir;
        simplifier = new CFGSimplifier(_ir);
    }

    public void run(){
        simplifier.run();
    }
}
