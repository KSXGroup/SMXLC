package kstarxin.optimization;

import kstarxin.ir.*;
import kstarxin.ir.instruction.*;
import kstarxin.ir.operand.*;
import java.util.*;

public class LivenessAnalyzer {
    private IRProgram ir;
    public LivenessAnalyzer(IRProgram _ir){
        ir = _ir;
    }

    public void collectDefUseInfo(){
        for (Method m : ir.getMethodMap().values()) {
            if(m.isBuiltin) continue;
            m.dfs();
            m.basicBlockInDFSOrder.forEach(bb->{
                for(Instruction i = bb.getBeginInst(); i != null; i = i.next) i.collectDefUseInfo();
            });
        }
    }

    private void anlysis(Method m){
        collectDefUseInfo();
        boolean changed = true;
        int cnt = 0;
        while (changed){
            //System.err.println(m.hintName + " liveness analysis iterated " + cnt + " times");
            changed = false;
            HashSet<VirtualRegister> oriLiveIn = new HashSet<VirtualRegister>();
            HashSet<VirtualRegister> oriLiveOut = new HashSet<VirtualRegister>();
            for(BasicBlock bb : m.basicBlockInDFSOrder){
                for (Instruction i = bb.getEndInst(); i != null; i = i.prev) {
                    oriLiveIn.clear();
                    oriLiveOut.clear();
                    oriLiveIn.addAll(i.liveIn);
                    oriLiveOut.addAll(i.liveOut);
                    i.liveIn.clear();
                    i.liveIn.addAll(i.use);
                    i.liveOut.removeAll(i.def);
                    i.liveIn.addAll(i.liveOut);
                    i.liveOut.clear();
                    if (i instanceof ConditionJumpInstruction) {
                        Instruction n1 = ((ConditionJumpInstruction) i).trueTarget.getBeginInst();
                        Instruction n2 = ((ConditionJumpInstruction) i).falseTarget.getEndInst();
                        i.liveOut.addAll(n1.liveIn);
                        i.liveOut.addAll(n2.liveIn);
                    } else if (i instanceof DirectJumpInstruction) {
                        Instruction n = ((DirectJumpInstruction) i).target.getBeginInst();
                        i.liveOut.addAll(n.liveIn);
                    } else {
                        if(i.next != null) i.liveOut.addAll(i.next.liveIn);
                    }
                    if(!(oriLiveIn.equals(i.liveIn) && oriLiveOut.equals(i.liveOut))) changed = true;
                }
            }
            cnt++;
        }
    }

    private void deadCodeEliminate(){
        Instruction toRemove = null;
        boolean changed = true;
        int cnt = 0;
        for (Method method : ir.getMethodMap().values()) {
            if(method.isBuiltin) continue;
            changed = true;
            while(changed) {
                changed = false;
                anlysis(method);
                for (BasicBlock bb : method.basicBlockInDFSOrder) {
                    for (Instruction i = bb.getBeginInst(); i != null; i = i.next) {
                        if (i instanceof BinaryArithmeticInstruction) {
                            if (!i.liveOut.contains(((BinaryArithmeticInstruction) i).target)) {
                                toRemove = i;
                                i = i.next;
                                toRemove.removeThisInst();
                                cnt++;
                                changed = true;
                                continue;
                            }
                        } else if (i instanceof UnaryInstruction) {
                            if (!i.liveOut.contains(((UnaryInstruction) i).dest)) {
                                toRemove = i;
                                i = i.next;
                                toRemove.removeThisInst();
                                cnt++;
                                changed = true;
                                continue;
                            }
                        } else if (i instanceof MoveInstruction) {
                            if (!i.liveOut.contains(((MoveInstruction) i).dest)) {
                                toRemove = i;
                                i = i.next;
                                toRemove.removeThisInst();
                                cnt++;
                                changed = true;
                                continue;
                            }
                        } else if (i instanceof LoadInstruction) {
                            if (!i.liveOut.contains(((LoadInstruction) i).dest)) {
                                toRemove = i;
                                i = i.next;
                                toRemove.removeThisInst();
                                cnt++;
                                changed = true;
                                continue;
                            }
                        } /*else if (i instanceof CallInstruction) {
                            if (!((CallInstruction) i).callee.isBuiltin && !i.liveOut.contains(((CallInstruction) i).returnValue)) {
                                toRemove = i;
                                i = i.next;
                                toRemove.removeThisInst();
                                cnt++;
                                changed = true;
                                continue;
                            }
                        }*/
                    }
                }
            }
        }
    }

    public void removeRedundantMove(){
        //TODO
    }

    public void run(){
        //something wrong with these opt
        //deadCodeEliminate();
    }
}
