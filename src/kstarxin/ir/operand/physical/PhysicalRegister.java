package kstarxin.ir.operand.physical;

import kstarxin.ir.IRBaseVisitor;
import kstarxin.ir.asmir.ASMLevelIRVisitor;
import kstarxin.ir.operand.*;
import kstarxin.utilities.NameMangler;

public class PhysicalRegister extends Register {
    private NameMangler.PhysicalRegisterName enumName;
    private NameMangler.PhysicalRegisterName trueName;
    public PhysicalRegister(NameMangler.PhysicalRegisterName _enumName){
        super(_enumName.toString());
        enumName = _enumName;
        switch (_enumName){
            case EAX:
            case AX:
            case AL:
            case AH:
                trueName = NameMangler.PhysicalRegisterName.RAX;
                break;
            case EBX:
            case BX:
            case BH:
            case BL:
                trueName = NameMangler.PhysicalRegisterName.RBX;
                break;
            case ECX:
            case CX:
            case CH:
            case CL:
                trueName = NameMangler.PhysicalRegisterName.RCX;
                break;
            case EDX:
            case DX:
            case DL:
            case DH:
                trueName = NameMangler.PhysicalRegisterName.RDX;
                break;
            case EBP:
                trueName = NameMangler.PhysicalRegisterName.RBP;
                break;
            case ESP:
                trueName = NameMangler.PhysicalRegisterName.RSP;
                break;
            case EDI:
                trueName = NameMangler.PhysicalRegisterName.RDI;
                break;
            case ESI:
                trueName = NameMangler.PhysicalRegisterName.RSI;
                break;
            case R9D:
                trueName = NameMangler.PhysicalRegisterName.R9;
                break;
            case R10D:
                trueName = NameMangler.PhysicalRegisterName.R10;
                break;
            case R11D:
                trueName = NameMangler.PhysicalRegisterName.R11;
                break;
            case R12D:
                trueName = NameMangler.PhysicalRegisterName.R12;
                break;
            case R13D:
                trueName = NameMangler.PhysicalRegisterName.R13;
                break;
            case R14D:
                trueName = NameMangler.PhysicalRegisterName.R14;
                break;
            case R15D:
                trueName = NameMangler.PhysicalRegisterName.R15;
                break;
            default:
                trueName = _enumName;

        }
    }

    @Override
    public String getNASMName(){
        return enumName.toString();
    }

    @Override
    public <T> T accept(ASMLevelIRVisitor<T> visitor) {
        throw new RuntimeException();
    }

    @Override
    public Operand accept(IRBaseVisitor visitor) {
        return null;
    }

    public boolean physicalEqual(PhysicalRegister other){
        return trueName.equals(other.trueName);
    }
}
