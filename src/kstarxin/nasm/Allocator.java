package kstarxin.nasm;

import kstarxin.compiler.Configure;
import kstarxin.ir.*;
import kstarxin.ir.asmir.ASMLevelIRProgram;
import kstarxin.ir.instruction.Instruction;
import kstarxin.ir.operand.VirtualRegister;
import kstarxin.ir.operand.physical.PhysicalRegister;
import kstarxin.ir.operand.physical.StackSpace;
import kstarxin.utilities.*;
import kstarxin.utilities.NameMangler.PhysicalRegisterName;

import java.util.*;

public class Allocator {
    private ASMLevelIRProgram ir;
    public Allocator(ASMLevelIRProgram _ir){
        ir = _ir;
    }

   /* private static final int totalPhyscalRegister = 12;

    private ArrayList<NameMangler.PhysicalRegisterName> availablePhysicalRegisters;
    private IRProgram   ir;
    private int         availablePhysicalRegisterPos;
    private int         offsetInStack;

    public Allocator(IRProgram _ir){
        ir = _ir;
        availablePhysicalRegisterPos    = 11;
        availablePhysicalRegisters = new ArrayList<NameMangler.PhysicalRegisterName>();
        availablePhysicalRegisters.add(PhysicalRegisterName.EBX);
        availablePhysicalRegisters.add(PhysicalRegisterName.EDX);
        availablePhysicalRegisters.add(PhysicalRegisterName.EDI);
        availablePhysicalRegisters.add(PhysicalRegisterName.ESI);
        availablePhysicalRegisters.add(PhysicalRegisterName.R8D);
        availablePhysicalRegisters.add(PhysicalRegisterName.R9D);
        availablePhysicalRegisters.add(PhysicalRegisterName.R10D);
        availablePhysicalRegisters.add(PhysicalRegisterName.R11D);
        availablePhysicalRegisters.add(PhysicalRegisterName.R12D);
        availablePhysicalRegisters.add(PhysicalRegisterName.R13D);
        availablePhysicalRegisters.add(PhysicalRegisterName.R14D);
        availablePhysicalRegisters.add(PhysicalRegisterName.R15D);
    }

    //callee should save RBX, RBP, R12, R13, R14, R15 according to SYSTEM V AMD64 ABI (registers which callee should save to stack) (callee saved register)
    //caller saved: RAX, RCX, RDX, RDI, RSI, RSP, R8, R9, R10, R11
    //I think RSP RBP should not be allocated
    //I decide to use EAX and ECX as tmp register now
    //naviely, registers can be used now are EBX, EDX, EDI, ESI, R8D, R9D, R10D, R11D, R12D, R13D, R14D, R15D
    //paramter passed via RDI, RSI, RDX, RCX, R8, R9

    class FrequencyComparator implements Comparator<Map.Entry<VirtualRegister, Integer>>{
        @Override
        public int compare(Map.Entry<VirtualRegister, Integer> a, Map.Entry<VirtualRegister, Integer> b) {
            int va = a.getValue(), vb = b.getValue();
            if(va < vb) return 1;
            else if(va > vb) return -1;
            else return 0;
        }
    }

    protected void lowerIRToNASMLevel(){

    }

    protected void frequencyAllocator(){
        HashMap<VirtualRegister, Integer> registerCountUse = new HashMap<VirtualRegister, Integer>();
        ir.getMethodMap().values().forEach(method -> {
            offsetInStack = 0;
            method.collectDefUseInfo();
            method.basicBlockInDFSOrder.clear();
            method.dfs();
            method.basicBlockInDFSOrder.forEach(bb->{
                for(Instruction i = bb.getBeginInst(); i != null; i = i.next){
                    i.def.forEach(vreg ->{
                        if(registerCountUse.containsKey(vreg)) registerCountUse.replace(vreg, registerCountUse.get(vreg) + 1);
                        else registerCountUse.replace(vreg, 1);
                    });
                    i.use.forEach(vreg ->{
                        if(registerCountUse.containsKey(vreg)) registerCountUse.replace(vreg, registerCountUse.get(vreg) + 1);
                        else throw new RuntimeException();
                    });
                }
            });

            ArrayList<Map.Entry<VirtualRegister, Integer>> entryList = new ArrayList<Map.Entry<VirtualRegister, Integer>>(registerCountUse.entrySet());
            Collections.sort(entryList, new FrequencyComparator());
            int tmpPos = 0;
            for(;availablePhysicalRegisterPos >=0 ; --availablePhysicalRegisterPos){
                entryList.get(tmpPos).getKey().allocatedTo(new PhysicalRegister(availablePhysicalRegisters.get(totalPhyscalRegister - 1 -availablePhysicalRegisterPos)));
                tmpPos++;
            }
            //allocate stack space for local variables, parameters are considered as local variables
            method.localVariables.values().forEach(local->{
                offsetInStack += Configure.PTR_SIZE;
                local.allocatedTo(new StackSpace(offsetInStack));
            });
            //then allocate space for tmp registers
            for(int i = tmpPos; i <= registerCountUse.size(); ++i){
                VirtualRegister vreg = entryList.get(i).getKey();
                if(vreg.spaceAllocatedTo == null) {
                    offsetInStack += Configure.PTR_SIZE;
                    vreg.allocatedTo(new StackSpace(offsetInStack));
                }
            }
            method.setStackAligned(offsetInStack);
        });

        lowerIRToNASMLevel();
    }

    protected void graphAllocator(){}*/
   //everything go shit!!!!

}
