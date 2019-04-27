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
import kstarxin.nasm.PhysicalRegisterSet;
import kstarxin.utilities.OperatorTranslator;

import java.util.*;

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
    private HashSet<VirtualRegister>        spillWorkList       = new HashSet<VirtualRegister>();
    private HashSet<VirtualRegister>        coalesced           = new HashSet<VirtualRegister>();
    private HashSet<VirtualRegister>        spilled             = new HashSet<VirtualRegister>();
    private HashSet<VirtualRegister>        colored             = new HashSet<VirtualRegister>();
    private Stack<VirtualRegister>          selectedStack       = new Stack<VirtualRegister>();

    private HashSet<ASMMoveInstruction>     coalescedMoves      = new HashSet<ASMMoveInstruction>();
    private HashSet<ASMMoveInstruction>     constrainedMoves    = new HashSet<ASMMoveInstruction>();
    private HashSet<ASMMoveInstruction>     frozenMoves         = new HashSet<ASMMoveInstruction>();
    private HashSet<ASMMoveInstruction>     workListMoves       = new HashSet<ASMMoveInstruction>();
    private HashSet<ASMMoveInstruction>     activeMoves         = new HashSet<ASMMoveInstruction>();

    private HashMap<VirtualRegister, PhysicalRegister>                      colorMap            = new HashMap<VirtualRegister, PhysicalRegister>();
    private HashMap<VirtualRegister, HashSet<VirtualRegister>>              interferenceGraph   = new HashMap<VirtualRegister, HashSet<VirtualRegister>>();
    private HashMap<VirtualRegister, HashSet<VirtualRegister>>              adjacentGraph       = new HashMap<VirtualRegister, HashSet<VirtualRegister>>();
    private HashMap<VirtualRegister, HashSet<ASMMoveInstruction>>           moveList            = new HashMap<VirtualRegister, HashSet<ASMMoveInstruction>>();
    private HashMap<VirtualRegister, VirtualRegister>                       alias               = new HashMap<VirtualRegister, VirtualRegister>();

    class Pair<K, V>{
        public K first;
        public V second;
        public Pair(K a, V b){
            first = a;
            second = b;
        }
    }

    private int degree(VirtualRegister vreg){
        if(precolored.contains(vreg)){
            if(vreg.spaceAllocatedTo != null) return inf;
            else throw new RuntimeException();
        }else return interferenceGraph.get(vreg).size();
    }

    public GraphAllocator(ASMLevelIRProgram _ir){
        ir = _ir;
        analyzer = new LivenessAnalyzerForAllocator(_ir);
    }

    public void run(){
        ir.ifAllocated = false;
        ir.getMethodMap().values().forEach(method -> {
            if(!method.isBuiltIn) {
                currentMethod = method;
                doAllocate();
            }
        });
    }

    private void doAllocate(){
        clearAll();
        analyzer.analyze(currentMethod);
        initialize();
        buildInterferenceGraph();
        printInterferenceGraph();
        makeWorkList();
        while(!(simplifyWorkList.isEmpty() && workListMoves.isEmpty() && freezeWorkList.isEmpty() && spillWorkList.isEmpty())) {
            if (!simplifyWorkList.isEmpty()) simplify();
            else if (!workListMoves.isEmpty()) coalesce();
            else if (!freezeWorkList.isEmpty()) freeze();
            else if (!spillWorkList.isEmpty()) selectSpill();
        }
        assignColor();
        printResult();
    }

    private void clearAll(){
        precolored.clear();
        initial.clear();
        simplifyWorkList.clear();
        freezeWorkList.clear();
        spillWorkList.clear();
        spilled.clear();
        colored.clear();
        colorMap.clear();
        coalesced.clear();
        selectedStack.clear();
        coalescedMoves.clear();
        constrainedMoves.clear();
        frozenMoves.clear();
        workListMoves.clear();
        activeMoves.clear();
        adjacentGraph.clear();
        interferenceGraph.clear();
        moveList.clear();
        alias.clear();
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
        HashSet<ASMMoveInstruction> relatedMov = moveList.get(vreg);
        if(relatedMov != null) {
            relatedMov.forEach(moveInst -> {
                if (activeMoves.contains(moveInst) || workListMoves.contains(moveInst))
                    ret.add(moveInst);
            });
        }
        return ret;
    }

    private HashSet<VirtualRegister> adjacent(VirtualRegister vreg){
        return new HashSet<>(interferenceGraph.get(vreg));
    }

    private void makeWorkList(){
        initial.forEach(vreg ->{
            if(degree(vreg) >= K) spillWorkList.add(vreg);
            else if(moveRelated(vreg)) freezeWorkList.add(vreg);
            else simplifyWorkList.add(vreg);
        });
        initial.clear();
    }

    private void enableMoves(HashSet<VirtualRegister> n){
        for(VirtualRegister vreg : n){
            for(ASMMoveInstruction mov : nodeMoves(vreg)){
                if(activeMoves.contains(mov)){
                    activeMoves.remove(mov);
                    workListMoves.add(mov);
                }
            }
        }
    }

    private void enableMoves(VirtualRegister v){
        for(ASMMoveInstruction mov : nodeMoves(v)){
            if(activeMoves.contains(mov)){
                activeMoves.remove(mov);
                workListMoves.add(mov);
            }
        }
    }

    private void simplify(){
        Iterator<VirtualRegister> iter = simplifyWorkList.iterator();
        VirtualRegister cur = null;
        if(iter.hasNext()){
            cur = iter.next();
            iter.remove();
        }else return;
        LinkedList<Pair<HashSet<VirtualRegister>, VirtualRegister>> toRemove = new LinkedList<Pair<HashSet<VirtualRegister>, VirtualRegister>>();
        if(cur != null) selectedStack.push(cur);
        for(VirtualRegister vreg : interferenceGraph.get(cur)){
            HashSet<VirtualRegister> neighbour = interferenceGraph.get(vreg);
            /*if(neighbour == null || !neighbour.contains(cur)) {
                if (precolored.contains(vreg) || precolored.contains(cur)) continue;
                else
                    throw new RuntimeException();
            }*/
            int d = degree(vreg);
            if(neighbour != null) {
                toRemove.add(new Pair<HashSet<VirtualRegister>, VirtualRegister>(neighbour, cur));
            }
            if(d == K){
                HashSet<VirtualRegister> tmp = new HashSet<>(interferenceGraph.get(vreg));
                tmp.add(vreg);
                enableMoves(tmp);
                spillWorkList.remove(vreg);
                if(moveRelated(vreg)) freezeWorkList.add(vreg);
                else simplifyWorkList.add(vreg);
            }
        }
        toRemove.forEach(pair->{
            pair.first.remove(pair.second);
        });
    }

    private void addWorkList(VirtualRegister u){
        if(!precolored.contains(u) && !moveRelated(u) && degree(u) < K){
            freezeWorkList.remove(u);
            simplifyWorkList.add(u);
        }
    }

    private boolean ok(VirtualRegister t, VirtualRegister r){
        if(degree(t) < K || precolored.contains(t) || ifAdjacent(t, r)) return true;
        else return false;
    }

    private boolean conservative(HashSet<VirtualRegister> nodes){
        int k = 0;
        for(VirtualRegister vreg : nodes) if(degree(vreg) > K) ++k;
        return (k < K);
    }

    private VirtualRegister getAlias(VirtualRegister n){
        if(coalesced.contains(n)) {
            VirtualRegister ret = getAlias(alias.get(n));
            alias.replace(n, ret);
            return ret;
        }else return n;
    }


    private boolean ifPreColoredNodeCanBeCombined(VirtualRegister u, VirtualRegister v){
        for(VirtualRegister t : adjacent(v)){
            if(!ok(t, u)) return false;
        }
        return true;
    }

    private boolean ifNodePreColoredNodeCanBeCombined(VirtualRegister u, VirtualRegister v){
        HashSet<VirtualRegister> totalSet = new HashSet<VirtualRegister>();
        totalSet.addAll(adjacent(u));
        totalSet.addAll(adjacent(v));
        return conservative(totalSet);
    }

    private void coalesce(){
        ASMMoveInstruction mov = workListMoves.iterator().next();
        VirtualRegister y = (VirtualRegister) mov.src;
        VirtualRegister x = (VirtualRegister) mov.dst;
        VirtualRegister u = null, v = null;
        x = getAlias(x);
        y = getAlias(y);
        if(precolored.contains(y)){
            u = y;
            v = x;
        }else{
            u = x;
            v = y;
        }
        workListMoves.remove(mov);
        if(u == v){
            constrainedMoves.add(mov);
            addWorkList(u);
        }else if(precolored.contains(v) || ifAdjacent(u, v)){
            constrainedMoves.add(mov);
            addWorkList(u);
            addWorkList(v);
        }else if((precolored.contains(u) && ifPreColoredNodeCanBeCombined(u, v)) ||
                (!precolored.contains(u) && ifNodePreColoredNodeCanBeCombined(u, v))){
            constrainedMoves.add(mov);
            combine(u, v);
            addWorkList(u);
        }else activeMoves.add(mov);
    }

    private void combine(VirtualRegister u, VirtualRegister v){
        if(freezeWorkList.contains(v)) freezeWorkList.remove(v);
        else spillWorkList.remove(v);
        coalesced.add(v);
        alias.put(v ,u);
        HashSet<ASMMoveInstruction> combinedNodeMoveList = new HashSet<ASMMoveInstruction>();
        combinedNodeMoveList.addAll(moveList.get(u));
        combinedNodeMoveList.addAll(moveList.get(v));
        moveList.put(u, combinedNodeMoveList);
        enableMoves(v);
        for(VirtualRegister t : adjacent(v)){
            addEdge(t, u);
            HashSet<VirtualRegister> neighbours = interferenceGraph.get(t);
            int d = neighbours.size();
            neighbours.remove(v);
            if(d == K){
                HashSet<VirtualRegister> tmp = new HashSet<>(adjacent(t));
                tmp.add(t);
                enableMoves(tmp);
                spillWorkList.remove(t);
                if(moveRelated(t)) freezeWorkList.add(t);
                else simplifyWorkList.add(t);
            }
        }
        if(degree(u) >= K && freezeWorkList.contains(u)){
            freezeWorkList.remove(u);
            spillWorkList.add(u);
        }
    }

    private void freezeMoves(VirtualRegister u){
        VirtualRegister x = null, y = null, v = null;
        for(ASMMoveInstruction mov : nodeMoves(u)){
             x = (VirtualRegister) mov.dst;
             y = (VirtualRegister) mov.src;
             if(getAlias(y) == getAlias(u)) v = getAlias(x);
             else v = getAlias(y);
             activeMoves.remove(mov);
             frozenMoves.add(mov);
             if(freezeWorkList.contains(v) && nodeMoves(v).isEmpty()){
                 freezeWorkList.remove(v);
                 simplifyWorkList.add(v);
             }
        }
    }

    private void freeze(){
        VirtualRegister u = freezeWorkList.iterator().next();
        freezeWorkList.remove(u);
        simplifyWorkList.add(u);
        freezeMoves(u);
    }

    private VirtualRegister getFromSpillWorkList(){
        //maybe use better strategy
        return spillWorkList.iterator().next();
    }

    private void selectSpill(){
        VirtualRegister m = getFromSpillWorkList();
        spillWorkList.remove(m);
        simplifyWorkList.add(m);
        freezeMoves(m);
    }

    private void assignColor(){
        LinkedHashSet<PhysicalRegister> okColor = new LinkedHashSet<PhysicalRegister>();
        while(!selectedStack.empty()){
            VirtualRegister n = selectedStack.pop();
            okColor.clear();
            okColor.addAll(PhysicalRegisterSet.PhysicalRegisterForAllocation);
            for(VirtualRegister w : interferenceGraph.get(n)){
                VirtualRegister vreg = getAlias(w);
                if(colored.contains(vreg)) okColor.remove(colorMap.get(vreg));
                else if(precolored.contains(vreg)) okColor.remove(vreg.spaceAllocatedTo);
            }
            if(okColor.isEmpty()) spilled.add(n);
            else {
                colored.add(n);
                PhysicalRegister c = okColor.iterator().next();
                colorMap.put(n, c);
            }
        }
        for(VirtualRegister n : coalesced) colorMap.put(n, colorMap.get(getAlias(n)));
    }

    private void printResult(){
        System.err.println("\n\n===================Register Allocation Result after one iteration of method " + currentMethod.name + "=========================");
        System.err.println("[Color Map]:");
        colorMap.forEach((k,v)->{
            System.err.println(k.getDisplayName() + " ----> " + v.getNASMName());
        });

        System.err.println("\n\n[Spilled]:");
        spilled.forEach(vreg->{
            System.err.print(vreg.getDisplayName() + "\t");
        });
        System.err.print("\n\n");

        System.err.println("\n\n[Coalesced]:");
        coalesced.forEach(vreg->{
            System.err.print(vreg.getDisplayName() + "\t");
        });
        System.err.print("\n\n");

    }

    private void printInterferenceGraph(){
        System.err.println("\n\n===================Ineterference Graph of " + currentMethod.name + "=========================");
        interferenceGraph.forEach((k, v)->{
            System.err.println("\n\n" + k.getDisplayName() + ":");
            int cnt = 0;
            for(VirtualRegister n : v){
                System.err.print( n.getDisplayName() + "\t");
                cnt++;
                if(cnt % 5 == 0) System.err.println();
            }
        });
        System.err.print("\n\n");
    }
}
