package kstarxin.optimization;

import kstarxin.ir.*;
import kstarxin.ir.instruction.*;
import kstarxin.ir.operand.*;
import java.util.*;

public class LivenessAnalyzer {
    private IRProgram ir;
    IRPrinter ptr;
    public LivenessAnalyzer(IRProgram _ir){
        ptr = new IRPrinter(_ir, System.err);
        ir = _ir;
    }

    public void collectDefUseInfo(){
        for (Method m : ir.getMethodMap().values()) {
            if(m.isBuiltin) continue;
            m.dfs();
            m.basicBlockInDFSOrder.forEach(BasicBlock::collectDefUseInfo);
        }
    }


    public void analysisByBasicBlock(Method m){
        collectDefUseInfo();
        ArrayList<BasicBlock> bbs = new ArrayList<>(m.basicBlockInDFSOrder);
        BasicBlock bb = null;
        boolean changed = true;
        HashSet<VirtualRegister> originLiveOut = new HashSet<VirtualRegister>();
        while(changed) {
            changed = false;
            int i = bbs.size() - 1;
            for (; i >= 0; --i) {
                bb = bbs.get(i);
                originLiveOut.clear();
                originLiveOut.addAll(bb.liveOut);
                bb.liveOut.clear();
                for(BasicBlock succ : bb.succ){
                    if(succ == null)
                        throw new RuntimeException();
                    bb.liveOut.addAll(succ.UEVAR);
                    for(VirtualRegister vreg : succ.liveOut){
                        if(!succ.VARKILL.contains(vreg)) bb.liveOut.add(vreg);
                    }
                }
                if(!bb.liveOut.equals(originLiveOut)) changed = true;
            }
        }

        HashSet<VirtualRegister> liveNow = new HashSet<VirtualRegister>();

        for(BasicBlock basicBlock : m.basicBlockInDFSOrder){
            liveNow.clear();
            liveNow.addAll(basicBlock.liveOut);
            for(Instruction inst = basicBlock.getEndInst(); inst != null; inst =inst.prev){
                inst.liveOut.clear();
                inst.liveIn.clear();
                inst.liveOut.addAll(liveNow);
                liveNow.removeAll(inst.def);
                liveNow.addAll(inst.use);
                inst.liveIn.addAll(liveNow);
            }
        }
    }

    public void commonSubExpressionElimination(){
        //TODO
    }

    public void copyPropagation(){
        //TODO
    }

    public void constantPropagation(){
        //TODO
    }

    public void deadCodeElimination(){
        int cnt = 0;
        for(Method m : ir.getMethodMap().values()){
            boolean changed = true;
            while(changed){
                changed = false;
                analysisByBasicBlock(m);
                for(BasicBlock bb : m.basicBlockInDFSOrder){
                    for(Instruction i = bb.getBeginInst(); i != null; i = i.next){
                        if(i instanceof BinaryArithmeticInstruction  && !(((BinaryArithmeticInstruction) i).target instanceof Address) && !i.liveOut.contains(((BinaryArithmeticInstruction) i).target)){
                            Instruction toRmove = i;
                            i = i.next;
                            toRmove.removeThisInst();
                            cnt++;
                            changed = true;
                            continue;
                        }else if(i instanceof MoveInstruction && !i.liveOut.contains(((MoveInstruction) i).dest)){
                            Instruction toRemove = i;
                            i = i.next;
                            toRemove.removeThisInst();;
                            cnt++;
                            changed = true;
                            continue;
                        }else if(i instanceof UnaryInstruction && !(((UnaryInstruction) i).dest instanceof Address) &&!i.liveOut.contains(((UnaryInstruction) i).dest)){
                            Instruction toRemove = i;
                            i = i.next;
                            toRemove.removeThisInst();;
                            cnt++;
                            changed = true;
                            continue;
                        }else if(i instanceof LoadInstruction && !i.liveOut.contains(((LoadInstruction) i).dest)){
                            Instruction toRemove = i;
                            i = i.next;
                            toRemove.removeThisInst();;
                            cnt++;
                            changed = true;
                            continue;
                        }
                    }
                }
            }
        }
        System.err.println(cnt + " line(s) is eliminated.");
    }

    public void run(){
        deadCodeElimination();
    }
}
