package kstarxin.nasm.allocator;

import kstarxin.ir.asmir.ASMBasicBlock;
import kstarxin.ir.asmir.ASMLevelIRInstruction.ASMConditionalJumpInstruction;
import kstarxin.ir.asmir.ASMLevelIRInstruction.ASMDirectJumpInstruction;
import kstarxin.ir.asmir.ASMLevelIRInstruction.ASMInstruction;
import kstarxin.ir.asmir.ASMLevelIRMethod;
import kstarxin.ir.asmir.ASMLevelIRProgram;
import kstarxin.ir.operand.VirtualRegister;

import java.util.HashSet;

public class LivenessAnalyzerForAllocator {
    private ASMLevelIRProgram ir;

    public LivenessAnalyzerForAllocator(ASMLevelIRProgram _ir){
        ir = _ir;
    }

    public void analyze(ASMLevelIRMethod method){
        method.collectInfo();
        method.basicBlocks.forEach(asmBasicBlock -> asmBasicBlock.liveOut.clear());
        boolean changed = true;
        HashSet<ASMBasicBlock> succSet = new HashSet<ASMBasicBlock>();
        HashSet<VirtualRegister> originLiveOut = new HashSet<VirtualRegister>();
        while(changed){
            changed = false;
            for(ASMBasicBlock bb : method.basicBlocks){
                succSet.clear();
                ASMInstruction endInst = bb.insts.getLast();
                if(endInst instanceof ASMDirectJumpInstruction)
                    succSet.add(((ASMDirectJumpInstruction) endInst).target);
                else if(endInst instanceof ASMConditionalJumpInstruction) {
                    succSet.add(((ASMConditionalJumpInstruction) endInst).falseTarget);
                    succSet.add(((ASMConditionalJumpInstruction) endInst).trueTarget);
                }
                endInst = bb.insts.get(bb.insts.size() - 2);
                if(endInst instanceof ASMDirectJumpInstruction)
                    succSet.add(((ASMDirectJumpInstruction) endInst).target);
                else if(endInst instanceof ASMConditionalJumpInstruction) {
                    succSet.add(((ASMConditionalJumpInstruction) endInst).falseTarget);
                    succSet.add(((ASMConditionalJumpInstruction) endInst).trueTarget);
                }

                originLiveOut.clear();
                originLiveOut.addAll(bb.liveOut);
                bb.liveOut.clear();
                succSet.forEach(succ->{
                    if(succ == null)
                        throw new RuntimeException();
                    bb.liveOut.addAll(succ.UEVAR);
                    succ.liveOut.forEach(vreg->{
                        if(!succ.VARKILL.contains(vreg)) bb.liveOut.add(vreg);
                    });
                });
                if(!bb.liveOut.equals(originLiveOut)) changed = true;
            }
        }
    }
}
