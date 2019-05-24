package kstarxin.nasm;

import kstarxin.ir.operand.VirtualRegister;
import kstarxin.ir.operand.physical.*;
import kstarxin.utilities.NameMangler.PhysicalRegisterName;

import java.util.*;

public class PhysicalRegisterSet {
    public final static PhysicalRegister RAX = new PhysicalRegister(PhysicalRegisterName.RAX);
    public final static PhysicalRegister RBX = new PhysicalRegister(PhysicalRegisterName.RBX);
    public final static PhysicalRegister RCX = new PhysicalRegister(PhysicalRegisterName.RCX);
    public final static PhysicalRegister RDX = new PhysicalRegister(PhysicalRegisterName.RDX);
    public final static PhysicalRegister RBP = new PhysicalRegister(PhysicalRegisterName.RBP);
    public final static PhysicalRegister RSP = new PhysicalRegister(PhysicalRegisterName.RSP);
    public final static PhysicalRegister RDI = new PhysicalRegister(PhysicalRegisterName.RDI);
    public final static PhysicalRegister RSI = new PhysicalRegister(PhysicalRegisterName.RSI);
    public final static PhysicalRegister R8 = new PhysicalRegister(PhysicalRegisterName.R8);
    public final static PhysicalRegister R9 = new PhysicalRegister(PhysicalRegisterName.R9);
    public final static PhysicalRegister R10 = new PhysicalRegister(PhysicalRegisterName.R10);
    public final static PhysicalRegister R11 = new PhysicalRegister(PhysicalRegisterName.R11);
    public final static PhysicalRegister R12 = new PhysicalRegister(PhysicalRegisterName.R12);
    public final static PhysicalRegister R13 = new PhysicalRegister(PhysicalRegisterName.R13);
    public final static PhysicalRegister R14 = new PhysicalRegister(PhysicalRegisterName.R14);
    public final static PhysicalRegister R15 = new PhysicalRegister(PhysicalRegisterName.R15);

    public final static PhysicalRegister EAX = new PhysicalRegister(PhysicalRegisterName.EAX);
    public final static PhysicalRegister EBX = new PhysicalRegister(PhysicalRegisterName.EBX);
    public final static PhysicalRegister ECX = new PhysicalRegister(PhysicalRegisterName.ECX);
    public final static PhysicalRegister EDX = new PhysicalRegister(PhysicalRegisterName.EDX);
    public final static PhysicalRegister EBP = new PhysicalRegister(PhysicalRegisterName.EBP);
    public final static PhysicalRegister ESP = new PhysicalRegister(PhysicalRegisterName.ESP);
    public final static PhysicalRegister EDI = new PhysicalRegister(PhysicalRegisterName.EDI);
    public final static PhysicalRegister ESI = new PhysicalRegister(PhysicalRegisterName.ESI);
    public final static PhysicalRegister R8D = new PhysicalRegister(PhysicalRegisterName.R8D);
    public final static PhysicalRegister R9D = new PhysicalRegister(PhysicalRegisterName.R9D);
    public final static PhysicalRegister R10D = new PhysicalRegister(PhysicalRegisterName.R10D);
    public final static PhysicalRegister R11D = new PhysicalRegister(PhysicalRegisterName.R11D);
    public final static PhysicalRegister R12D = new PhysicalRegister(PhysicalRegisterName.R12D);
    public final static PhysicalRegister R13D = new PhysicalRegister(PhysicalRegisterName.R13D);
    public final static PhysicalRegister R14D = new PhysicalRegister(PhysicalRegisterName.R14D);
    public final static PhysicalRegister R15D = new PhysicalRegister(PhysicalRegisterName.R15D);

    public final static PhysicalRegister AX = new PhysicalRegister(PhysicalRegisterName.AX);
    public final static PhysicalRegister BX = new PhysicalRegister(PhysicalRegisterName.BX);
    public final static PhysicalRegister CX = new PhysicalRegister(PhysicalRegisterName.CX);
    public final static PhysicalRegister DX = new PhysicalRegister(PhysicalRegisterName.DX);

    public final static PhysicalRegister AL = new PhysicalRegister(PhysicalRegisterName.AL);
    public final static PhysicalRegister BL = new PhysicalRegister(PhysicalRegisterName.BL);
    public final static PhysicalRegister CL = new PhysicalRegister(PhysicalRegisterName.CL);
    public final static PhysicalRegister DL = new PhysicalRegister(PhysicalRegisterName.DL);

    public final static PhysicalRegister AH = new PhysicalRegister(PhysicalRegisterName.AH);
    public final static PhysicalRegister BH = new PhysicalRegister(PhysicalRegisterName.BH);
    public final static PhysicalRegister CH = new PhysicalRegister(PhysicalRegisterName.CH);
    public final static PhysicalRegister DH = new PhysicalRegister(PhysicalRegisterName.DH);


    //callee should save RBX, RBP, R12, R13, R14, R15 according to SYSTEM V AMD64 ABI (registers which callee should save to stack) (callee saved register)
    //caller saved: RAX, RCX, RDX, RDI, RSI, RSP, R8, R9, R10, R11
    public final static LinkedHashSet<PhysicalRegister> PhysicalRegisterForAllocation = new LinkedHashSet<PhysicalRegister>(){{
        add(RAX);
        add(RCX);
        add(RDX);
        add(RDI);
        add(RSI);
        add(R8);
        add(R9);
        add(R10);
        add(R11);


        add(RBX);
        add(R12);
        add(R13);
        add(R14);
        add(R15);
    }
    };

    public final static HashSet<PhysicalRegister> CallerSavedRegister = new HashSet<PhysicalRegister>(){{
        add(RAX);
        add(RCX);
        add(RDX);
        add(RDI);
        add(RSI);
        add(R8);
        add(R9);
        add(R10);
        add(R11);
    }};

    public final static HashSet<PhysicalRegister> CalleeSavedRegister = new HashSet<PhysicalRegister>(){{
        add(RBX);
        add(R12);
        add(R13);
        add(R14);
        add(R15);
    }};

    //paramter passed via RDI, RSI, RDX, RCX, R8, R9

    public final static ArrayList<PhysicalRegister> ParameterPassingPhysicalRegister = new ArrayList<PhysicalRegister>(){{
        add(RDI);
        add(RSI);
        add(RDX);
        add(RCX);
        add(R8);
        add(R9);
    }};

    public final static HashSet<PhysicalRegister> dwordRegisterSet = new HashSet<PhysicalRegister>(){{
        add(EAX);
        add(EBX);
        add(ECX);
        add(EDX);
        add(EBP);
        add(ESP);
        add(EDI);
        add(ESI);
        add(R8D);
        add(R9D);
        add(R10D);
        add(R11D);
        add(R12D);
        add(R13D);
        add(R14D);
        add(R15D);
    }};

}
