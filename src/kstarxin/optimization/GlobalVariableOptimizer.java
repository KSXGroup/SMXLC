package kstarxin.optimization;

import kstarxin.ir.*;
import kstarxin.ir.instruction.*;
import kstarxin.ir.operand.*;
import kstarxin.utilities.NameMangler;

import java.util.*;

//load the global variable to vreg and store load and store before and after the call
public class GlobalVariableOptimizer {
    IRProgram                           ir;
    HashMap<Address, VirtualRegister>   globalVariableReplacer;
    public GlobalVariableOptimizer(IRProgram _ir){
        ir                      = _ir;
        globalVariableReplacer  = new HashMap<Address, VirtualRegister>();
    }

    public void run(){
        HashMap<Address, VirtualRegister> tmpMap = new HashMap<Address, VirtualRegister>();
        ir.getMethodMap().values().forEach(method -> {
            if(!method.isBuiltin) {
                globalVariableReplacer.clear();
                method.globalVariableUsed.forEach(gv -> {
                    if(!(gv instanceof StaticString && ((StaticString) gv).isConstantAllocatedByCompiler)) {
                        VirtualRegister vreg = method.allocateNewTmpRegister();
                        globalVariableReplacer.put(gv, vreg);
                    }
                });
                ReturnInstruction retInst = null;
                method.basicBlockInBFSOrder.clear();
                method.bfs();

                Address k = null;
                VirtualRegister v = null;

                for (BasicBlock bb : method.basicBlockInBFSOrder) {
                    for (Instruction i = bb.getBeginInst(); i != null; i = i.next) {
                        if (!(i instanceof CallInstruction)){
                            Address ret = i.replaceOperandForGlobalVariableOptimization(globalVariableReplacer);
                            if ( i instanceof ReturnInstruction) {
                                if(retInst != null)throw new RuntimeException();
                                else retInst = (ReturnInstruction) i;
                            }
                        }
                    }
                    for(Instruction i = bb.getBeginInst(); i != null; i = i.next) {
                        if (i instanceof CallInstruction && !((CallInstruction) i).callee.isBuiltin) {
                            for (Map.Entry<Address, VirtualRegister> entry : globalVariableReplacer.entrySet()) {
                                k = entry.getKey();
                                v = entry.getValue();
                                if ((k instanceof StaticString && ((StaticString) k).isConstantAllocatedByCompiler))
                                    continue;
                                i.insertBeforeThis(new StoreInstruction(k, v));
                                i.insertInstAfterThis(new LoadInstruction(v, k));
                            }
                        }
                    }
                }

                Instruction beginInst = method.startBlock.getBeginInst();

                for (Map.Entry<Address, VirtualRegister> entry : globalVariableReplacer.entrySet()) {
                    beginInst.insertBeforeThis(new LoadInstruction(entry.getValue(), entry.getKey()));
                }

                if(!method.hintName.equals(NameMangler.mainMethodName)) {
                    for (Map.Entry<Address, VirtualRegister> entry : globalVariableReplacer.entrySet()) {
                        k = entry.getKey();
                        v = entry.getValue();
                        if (entry.getKey() instanceof StaticString && ((StaticString) k).isConstantAllocatedByCompiler)
                            continue;
                        retInst.insertBeforeThis(new StoreInstruction(k, v));
                    }
                }
            }
        });
    }
}
