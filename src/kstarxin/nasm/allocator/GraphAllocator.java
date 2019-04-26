package kstarxin.nasm.allocator;

import kstarxin.ir.asmir.ASMBasicBlock;
import kstarxin.ir.asmir.ASMLevelIRInstruction.ASMInstruction;
import kstarxin.ir.asmir.ASMLevelIRInstruction.ASMMoveInstruction;
import kstarxin.ir.asmir.ASMLevelIRMethod;
import kstarxin.ir.asmir.ASMLevelIRProgram;
import kstarxin.ir.operand.Operand;
import kstarxin.ir.operand.VirtualRegister;
import kstarxin.ir.operand.physical.PhysicalRegister;
import kstarxin.ir.operand.physical.StackSpace;
import kstarxin.utilities.OperatorTranslator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;

public class GraphAllocator {

    private int K = 14;
    private int inf = 65536;
    private ASMLevelIRMethod currentMethod;

    private ASMLevelIRProgram               ir;
    private LivenessAnalyzerForAllocator    analyzer;
    private HashSet<VirtualRegister>        precolored          = new HashSet<VirtualRegister>();
    private HashSet<VirtualRegister>        initial             = new HashSet<VirtualRegister>();
    private HashSet<VirtualRegister>        simplifyWorkList    = new HashSet<VirtualRegister>();
    private HashSet<VirtualRegister>        freezeWorkList      = new HashSet<VirtualRegister>();
    private HashSet<VirtualRegister>        spilled             = new HashSet<VirtualRegister>();
    private HashSet<VirtualRegister>        colored             = new HashSet<VirtualRegister>();
    private Stack<VirtualRegister>          selectedStack       = new Stack<VirtualRegister>();

    private HashSet<ASMMoveInstruction>     coalescedMoves      = new HashSet<ASMMoveInstruction>();
    private HashSet<ASMMoveInstruction>     constrainedMoves    = new HashSet<ASMMoveInstruction>();
    private HashSet<ASMMoveInstruction>     frozenMoves         = new HashSet<ASMMoveInstruction>();
    private HashSet<ASMMoveInstruction>     workListMoves       = new HashSet<ASMMoveInstruction>();
    private HashSet<ASMMoveInstruction>     activeMoves         = new HashSet<ASMMoveInstruction>();

    private HashMap<VirtualRegister, HashSet<VirtualRegister>>              interferenceGraph   = new HashMap<VirtualRegister, HashSet<VirtualRegister>>();
    private HashMap<VirtualRegister, HashSet<VirtualRegister>>              adjacentGraph       = new HashMap<VirtualRegister, HashSet<VirtualRegister>>();
    private HashMap<VirtualRegister, HashSet<ASMMoveInstruction>>           moveList            = new HashMap<VirtualRegister, HashSet<ASMMoveInstruction>>();
    private HashMap<VirtualRegister, VirtualRegister>                       alias               = new HashMap<VirtualRegister, VirtualRegister>();

    private int degree(VirtualRegister vreg){
        if(precolored.contains(vreg)){
            if(vreg.spaceAllocatedTo != null) return inf;
            else throw new RuntimeException();
        }else return interferenceGraph.get(vreg).size();
    }

    private void decreaseDegree(VirtualRegister vreg){
    }

    public GraphAllocator(ASMLevelIRProgram _ir){
        ir = _ir;
        analyzer = new LivenessAnalyzerForAllocator(_ir);
    }

    public void run(){
        ir.ifAllocated = false;
        ir.getMethodMap().values().forEach(method -> {
            currentMethod = method;
            doAllocate();
        });
    }

    private void doAllocate(){
        analyzer.analyze(currentMethod);
        buildInterferenceGraph();
    }

    private void clearAll(){
        precolored.clear();
        initial.clear();
        simplifyWorkList.clear();
        freezeWorkList.clear();
        spilled.clear();
        colored.clear();
        selectedStack.clear();
        coalescedMoves.clear();
        constrainedMoves.clear();
        frozenMoves.clear();
        workListMoves.clear();
        activeMoves.clear();
        adjacentGraph.clear();
        interferenceGraph.clear();
        moveList.clear();
    }

    private void initialize(){
        currentMethod.basicBlocks.forEach(asmBasicBlock -> {
            asmBasicBlock.insts.forEach(inst->{
                inst.def.forEach(vreg->{
                    if(vreg.spaceAllocatedTo == null) initial.add(vreg);
                    else if(!(vreg.spaceAllocatedTo instanceof StackSpace)) precolored.add(vreg);
                });
                inst.use.forEach(vreg->{
                    if(vreg.spaceAllocatedTo == null) initial.add(vreg);
                    else if(!(vreg.spaceAllocatedTo instanceof StackSpace)) precolored.add(vreg);
                });
            });
        });
    }

    private void addEdge(VirtualRegister u, VirtualRegister v){
        if(adjacentGraph.containsKey(u)) adjacentGraph.get(u).add(v);
        else{
            HashSet<VirtualRegister> tmp = new HashSet<VirtualRegister>();
            tmp.add(v);
            adjacentGraph.put(u, tmp);
        }
        if(adjacentGraph.containsKey(v)) adjacentGraph.get(v).add(u);
        else{
            HashSet<VirtualRegister> tmp = new HashSet<VirtualRegister>();
            tmp.add(u);
            adjacentGraph.put(v, tmp);
        }

        if(!precolored.contains(u)){
            if(interferenceGraph.containsKey(u)) interferenceGraph.get(u).add(v);
            else{
                HashSet<VirtualRegister> tmp = new HashSet<VirtualRegister>();
                tmp.add(v);
                interferenceGraph.put(u, tmp);
            }
        }

        if(!precolored.contains(v)){
            if(interferenceGraph.containsKey(v)) interferenceGraph.get(v).add(u);
            else{
                HashSet<VirtualRegister> tmp = new HashSet<VirtualRegister>();
                tmp.add(u);
                interferenceGraph.put(v, tmp);
            }
        }
    }

    private boolean isMoveInstruction(ASMInstruction inst){
        if(inst instanceof ASMMoveInstruction && ((ASMMoveInstruction) inst).op.equals(OperatorTranslator.NASMInstructionOperator.MOV)
                && ((ASMMoveInstruction) inst).src instanceof VirtualRegister && ((ASMMoveInstruction) inst).dst instanceof VirtualRegister) return true;
        else return false;
    }

    private boolean ifAdjacent(VirtualRegister u, VirtualRegister v){
        if(!adjacentGraph.containsKey(u)) throw new RuntimeException();
        else return adjacentGraph.get(u).contains(v);
    }

    private void buildInterferenceGraph(){
        HashSet<VirtualRegister> live = new HashSet<>();
        for(ASMBasicBlock bb : currentMethod.basicBlocks){
            live.addAll(bb.liveOut);
            Iterator<ASMInstruction> iter = bb.insts.descendingIterator();
            while(iter.hasNext()){
                ASMInstruction curInst = (ASMInstruction)(iter.next());
                if(isMoveInstruction(curInst)){
                    live.removeAll(curInst.use);
                    curInst.def.forEach(vreg->{
                        if(moveList.containsKey(vreg)) moveList.get(vreg).add((ASMMoveInstruction) curInst);
                        else {
                            HashSet<ASMMoveInstruction> tmp = new HashSet<ASMMoveInstruction>();
                            tmp.add((ASMMoveInstruction) curInst);
                            moveList.put(vreg, tmp);
                        }
                    });
                    curInst.use.forEach(vreg->{
                        if(moveList.containsKey(vreg)) moveList.get(vreg).add((ASMMoveInstruction) curInst);
                        else {
                            HashSet<ASMMoveInstruction> tmp = new HashSet<ASMMoveInstruction>();
                            tmp.add((ASMMoveInstruction) curInst);
                            moveList.put(vreg, tmp);
                        }
                    });
                    workListMoves.add((ASMMoveInstruction) curInst);
                }
                live.addAll(curInst.def);
                curInst.def.forEach(vreg->{
                    live.forEach(lvreg->{
                        addEdge(lvreg, vreg);
                    });
                });
                curInst.use.forEach(vreg->{
                    live.forEach(lvreg->{
                        addEdge(lvreg, vreg);
                    });
                });
                live.removeAll(curInst.def);
                live.addAll(curInst.use);
            }
        }
    }

    private boolean moveRelated(VirtualRegister vreg){
        return !nodeMoves(vreg).isEmpty();
    }

    private HashSet<ASMMoveInstruction> nodeMoves(VirtualRegister vreg){
        HashSet<ASMMoveInstruction> ret = new HashSet<ASMMoveInstruction>();
        moveList.get(vreg).forEach(moveInst ->{
            if(activeMoves.contains(moveInst) || workListMoves.contains(moveInst))
                ret.add(moveInst);
        });
        return ret;
    }

    private HashSet<VirtualRegister> adjacent(VirtualRegister vreg){
        return new HashSet<>(interferenceGraph.get(vreg));
    }

    private void makeWorkList(){
        initial.forEach(vreg ->{
            if(degree(vreg) >= K) spilled.add(vreg);
            else if(moveRelated(vreg)) freezeWorkList.add(vreg);
            else simplifyWorkList.add(vreg);
        });
    }

    private void simplify(){
        Iterator<VirtualRegister> iter = simplifyWorkList.iterator();
        VirtualRegister cur = null;
        if(iter.hasNext()){
            cur = iter.next();
            iter.remove();
        }
        if(cur != null) selectedStack.push(cur);
        for(VirtualRegister vreg : interferenceGraph.get(cur)){
            HashSet<VirtualRegister> neighbour = interferenceGraph.get(vreg);
            int d = neighbour.size();
            neighbour.remove(cur);
            if(d == K){

            }
        }
    }


}
