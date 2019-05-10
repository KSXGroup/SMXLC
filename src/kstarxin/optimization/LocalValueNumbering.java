package kstarxin.optimization;

import kstarxin.ir.IRBaseVisitor;
import kstarxin.ir.IRProgram;
import kstarxin.ir.Method;
import kstarxin.ir.instruction.*;
import kstarxin.ir.operand.*;
import kstarxin.ir.operand.physical.PhysicalRegister;
import kstarxin.ir.operand.physical.StackSpace;
import kstarxin.parser.MxStarParser;

import java.util.*;

public class LocalValueNumbering implements IRBaseVisitor<Void> {
    private IRProgram ir;
    private HashMap<VirtualRegister, Integer>               valueNumberingMap;              // vreg -> vreg value numbering
    //private HashMap<Integer, HashSet<VirtualRegister>>      numberingVirtualRegsiterMap;    // numbering -> {vregs}, because move lead to the same numbering of different reg
    private HashMap<Integer, VirtualRegister>               quadNumberingMap;               // quad hash -> vregs that save this result currently
    private HashMap<Integer, Integer>                       immediateNumberingMap;          // imm -> imm numbering
    private HashMap<Integer, Integer>                       numberingImmediateMap;          // imm numbering -> imm
    private int                                             valueCounter;

    public LocalValueNumbering(IRProgram _ir){
        ir                          = _ir;
        valueNumberingMap           = new HashMap<VirtualRegister, Integer>();
        //numberingVirtualRegsiterMap = new HashMap<Integer, HashSet<VirtualRegister>>();
        quadNumberingMap            = new HashMap<Integer, VirtualRegister>();
        immediateNumberingMap       = new HashMap<Integer, Integer>();
        numberingImmediateMap       = new HashMap<Integer, Integer>();
        valueCounter                = 0;
    }


    public void run(){
        ir.getMethodMap().values().forEach(m->{
            if(!m.isBuiltin) {
                m.dfs();
                m.basicBlockInDFSOrder.forEach(bb -> {
                    clearAll();
                    for (Instruction i = bb.getBeginInst(); i != null; i = i.next)
                        visit(i);
                });
            }
        });
    }

    private void clearAll(){
        valueNumberingMap.clear();
        quadNumberingMap.clear();
        immediateNumberingMap.clear();
        numberingImmediateMap.clear();
    }

    private Integer getOperandNumbering(Operand op){
        if(op instanceof VirtualRegister){
            return getValueNumbering((VirtualRegister) op);
        }
        else if(op instanceof Immediate){
            return getValueNumbering((Immediate) op);
        }
        else
            throw new RuntimeException();
    }

    private boolean exchangable(int operator){
        switch (operator){
            case MxStarParser.ADD:
            case MxStarParser.MUL:
            case MxStarParser.BITNOT:
            case MxStarParser.BITAND:
            case MxStarParser.BITXOR:
            case MxStarParser.EQ:
            case MxStarParser.NEQ:
            case MxStarParser.AND:
            case MxStarParser.OR:
                return true;
            default:
                return false;
        }
    }

    private int evaluate(int op, int oprand1, int oprand2, boolean uflag){
        int ret = -1;
        switch (op){
            case MxStarParser.ADD:
                ret = oprand1 + oprand2;
                break;
            case MxStarParser.ASSIGN:
                throw new RuntimeException();
            case MxStarParser.BITAND:
                ret = oprand1 & oprand2;
                break;
            case MxStarParser.AND:
                ret = oprand1 & oprand2;
                break;
            case MxStarParser.BITNOT:
                if(uflag){
                    ret = ~oprand2;
                }
                else throw new RuntimeException();
                break;
            case MxStarParser.BITOR:
                ret = (oprand1 | oprand2);
                break;
            case MxStarParser.BITXOR:
                ret = (oprand1 ^ oprand2);
                break;
            case MxStarParser.DEC:
                if(uflag){
                    ret = oprand2 - 1;
                }
                break;
            case MxStarParser.DIV:
                ret = oprand1 / oprand2;
                break;
            case MxStarParser.EQ:
                if(oprand1 == oprand2)
                    ret = 1;
                else
                    ret = 0;
                break;
            case MxStarParser.GE:
                if(oprand1 >= oprand2)
                    ret = 1;
                else ret = 0;
                break;
            case MxStarParser.GT:
                if(oprand1 > oprand2)
                    ret = 1;
                else ret = 0;
                break;
            case MxStarParser.INC:
                if(uflag){
                    ret = oprand2 + 1;
                }else throw new RuntimeException();
                break;
            case MxStarParser.LE:
                if(oprand1 <= oprand2)
                    ret = 1;
                else ret = 0;
                break;
            case MxStarParser.LT:
                if(oprand1 < oprand2)
                    ret = 1;
                else ret = 0;
                break;
            case MxStarParser.MOD:
                ret = oprand1 % oprand2;
                break;
            case MxStarParser.MUL:
                ret = oprand1 * oprand2;
                break;
            case MxStarParser.NEQ:
                if(oprand1 != oprand2)
                    ret =  1;
                else ret = 0;
                break;
            case MxStarParser.OR:
                if(oprand1 > 0 || oprand2 > 0) ret = 1;
                else ret = 0;
                break;
            case MxStarParser.SFTL:
                ret = oprand1 << oprand2;
                break;
            case MxStarParser.SFTR:
                ret = oprand1 >> oprand2;
                break;
            case MxStarParser.NOT:
                if(uflag){
                    if(oprand2 > 0) ret = 1;
                    else ret = 0;
                }
                else throw new RuntimeException();
                break;
            case MxStarParser.SUB:
                if(!uflag){
                    ret = oprand1 - oprand2;
                }else
                    ret =-oprand2;
                break;
            default:
                throw new RuntimeException();
        }
        return ret;
    }

    private Integer getValueNumbering(VirtualRegister vreg){
        int ret = -1;
        if(valueNumberingMap.containsKey(vreg)) ret = valueNumberingMap.get(vreg);
        else{
            valueNumberingMap.put(vreg, valueCounter);
            ret = valueCounter;
            valueCounter++;
        }
        return ret;
    }

    private Integer getValueNumbering(Immediate imm){
        int ret = -1;
        if(immediateNumberingMap.containsKey(imm.value)) ret = immediateNumberingMap.get(imm.value);
        else{
            immediateNumberingMap.put((int)imm.value, valueCounter);
            numberingImmediateMap.put(valueCounter, (int)imm.value);
            ret = valueCounter;
            valueCounter++;
        }
        return ret;
    }

    private int hash(int operator, int oprand1, int oprand2){
        int op1 = -1, op2 = -1, ret = -1;
        if(exchangable(operator)){
            if(oprand1 > oprand2){
                op1 = oprand2;
                op2 = oprand1;
            }else{
                op1 = oprand1;
                op2 = oprand2;
            }
        }else{
            op1 = oprand1;
            op2 = oprand2;
        }
        ret = operator * (int)(1e6) + op1 * (int)(1e3) + op2;
        //System.err.println(ret);
        return ret;
    }

    private void defVirtualRegister(VirtualRegister vreg, int newDefQuadHashValue){
        // if newDefQuadHashValue is -1, that means this def is performed by quad that cannot be hashed(like load, call, bin/unary arith we memory oprand, and so on)
        if(newDefQuadHashValue == -1) {
            valueNumberingMap.put(vreg, valueCounter);
            valueCounter++;
        }else{
            if(quadNumberingMap.containsKey(newDefQuadHashValue)) {
                valueNumberingMap.put(vreg, valueNumberingMap.get(quadNumberingMap.get(newDefQuadHashValue)));
            }
            else {
                valueNumberingMap.put(vreg, valueCounter);
                quadNumberingMap.put(newDefQuadHashValue, vreg);
                valueCounter++;
            }
        }

    }

    private void copy(VirtualRegister vdst, Operand src){
        valueNumberingMap.put(vdst, getOperandNumbering(src));
    }

    private boolean canValueNumbering(Operand op){
        if(op instanceof VirtualRegister || op instanceof Immediate) return true;
        else return false;
    }

    @Override
    public Void visit(Method method) {
        throw new RuntimeException();
    }

    @Override
    public Void visit(BinaryArithmeticInstruction inst) {
        Integer op = inst.op;
        int instHashValue = -1;
        if(canValueNumbering(inst.lhs) && canValueNumbering(inst.rhs)){
            if(numberingImmediateMap.containsKey(getOperandNumbering(inst.lhs)) && numberingImmediateMap.containsKey(getOperandNumbering(inst.rhs))){
                int v = evaluate(inst.op, numberingImmediateMap.get(getOperandNumbering(inst.lhs)), numberingImmediateMap.get(getOperandNumbering(inst.rhs)), false);
                System.err.println("replace!");
                inst.replaceThisWith(new MoveInstruction((VirtualRegister)inst.target, new Immediate(v)));
                valueNumberingMap.put((VirtualRegister) inst.target, valueCounter);
                immediateNumberingMap.put(v, valueCounter);
                numberingImmediateMap.put(valueCounter, v);
                valueCounter++;
            }else {
                int lnum = getOperandNumbering(inst.lhs);
                int rnum = getOperandNumbering(inst.rhs);
                instHashValue = hash(inst.op, lnum, rnum);
                if (quadNumberingMap.containsKey(instHashValue)) {
                    //System.err.println("replace!");
                    inst.replaceThisWith(new MoveInstruction((VirtualRegister) inst.target, quadNumberingMap.get(instHashValue)));
                }
                defVirtualRegister((VirtualRegister) inst.target, instHashValue);
            }
        } else if(inst.target instanceof VirtualRegister)
            defVirtualRegister((VirtualRegister) inst.target, -1);
        return null;
    }

    @Override
    public Void visit(CallInstruction inst) {
        if(inst.returnValue != null)
            defVirtualRegister(inst.returnValue, -1);
        return null;
    }

    @Override
    public Void visit(Instruction inst) { return inst.accept(this); }

    @Override
    public Void visit(LoadInstruction inst) {
        defVirtualRegister((VirtualRegister)inst.dest, -1);
        return null;
    }

    @Override
    public Void visit(MoveInstruction inst) {
        copy((VirtualRegister) inst.dest, inst.src);
        return null;
    }

    @Override
    public Void visit(UnaryInstruction inst) {
        if(canValueNumbering(inst.src) && canValueNumbering(inst.dest)){
            int instHashValue = hash(inst.op, getOperandNumbering(inst.src), getOperandNumbering(inst.dest));
            if(quadNumberingMap.containsKey(instHashValue))
                inst.replaceThisWith(new MoveInstruction((VirtualRegister)inst.dest, quadNumberingMap.get(instHashValue)));
            defVirtualRegister((VirtualRegister)inst.dest, instHashValue);
        }
        else if(inst.src instanceof VirtualRegister)
           defVirtualRegister((VirtualRegister) inst.dest, -1);
        return null;
    }

    @Override
    public Void visit(JumpInstruction inst) { return null; }
    @Override
    public Void visit(CompareInstruction inst) { return null; }
    @Override
    public Void visit(ConditionJumpInstruction inst) { return null; }
    @Override
    public Void visit(DirectJumpInstruction inst) { return null; }
    @Override
    public Void visit(ReturnInstruction inst) { return null; }
    @Override
    public Void visit(StoreInstruction inst) { return null; }
    @Override
    public Operand visit(Operand operand) { return null; }
    @Override
    public Operand visit(PhysicalRegister operand) { return null; }
    @Override
    public Operand visit(StackSpace operand) { return null; }
    @Override
    public Operand visit(Address operand) { return null; }
    @Override
    public Operand visit(Constant operand) { return null; }
    @Override
    public Operand visit(Immediate operand) { return null; }
    @Override
    public Operand visit(Label operand) { return null; }
    @Override
    public Operand visit(Memory operand) { return null; }
    @Override
    public Operand visit(Register operand) { return null; }
    @Override
    public Operand visit(StaticString operand) { return null; }
    @Override
    public Operand visit(StaticPointer operand) { return null; }
    @Override
    public Operand visit(VirtualRegister operand) { return null; }
}
