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
    private HashMap<VirtualRegister, Integer>   valueNumberingMap;
    private HashMap<Integer, VirtualRegister>   quadNumberingMap;
    private HashMap<Integer, Integer>           immediateNumberingMap;
    private int                                 valueCounter;

    public LocalValueNumbering(IRProgram _ir){
        valueNumberingMap       = new HashMap<VirtualRegister, Integer>();
        quadNumberingMap        = new HashMap<Integer, VirtualRegister>();
        immediateNumberingMap   = new HashMap<Integer, Integer>();
        valueCounter            = 0;
    }

    public void run(){
        ir.getMethodMap().values().forEach(m->{
            m.dfs();
            m.basicBlockInDFSOrder.forEach(bb->{
                for(Instruction i = bb.getBeginInst(); i != null; i = i.next) visit(i);
            });
        });
    }

    private Integer getOperandNumbering(Operand op){
        if(op instanceof VirtualRegister){
            return getValueNumbering((VirtualRegister) op);
        }
        else if(op instanceof Immediate){
            return getValueNumbering((Immediate) op);
        }
        else throw new RuntimeException();
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

    private Integer getValueNumbering(VirtualRegister vreg){
        return null;
    }

    private Integer getValueNumbering(Immediate imm){
        return null;
    }

    private int hash(int operator, int oprand1, int oprand2){
        return -1;
    }

    private void updateVirtualRegisterNumbering(VirtualRegister vreg){}

    private boolean canValueNumbering(Operand op){
        if(op instanceof VirtualRegister || op instanceof Immediate) return true;
        else return false;
    }

    private void mapNewRegisterTo(VirtualRegister vnew, Operand old){
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
            instHashValue = hash(inst.op, getOperandNumbering(inst.lhs), getOperandNumbering(inst.rhs));
        } else if(inst.target instanceof VirtualRegister)
            updateVirtualRegisterNumbering((VirtualRegister) inst.target);
        if(instHashValue != -1){
            //get register from map and replace this inst with a move
        }else{
            if(inst.target instanceof VirtualRegister){
                //map the hash value to vreg
            }
        }
        return null;
    }

    @Override
    public Void visit(CallInstruction inst) {
        if(inst.returnValue != null)
            updateVirtualRegisterNumbering(inst.returnValue);
        return null;
    }

    @Override
    public Void visit(CompareInstruction inst) {
        return null;
    }

    @Override
    public Void visit(ConditionJumpInstruction inst) {
        return null;
    }

    @Override
    public Void visit(DirectJumpInstruction inst) {
        return null;
    }

    @Override
    public Void visit(Instruction inst) {
        return inst.accept(this);
    }

    @Override
    public Void visit(JumpInstruction inst) {
        return null;
    }

    @Override
    public Void visit(LoadInstruction inst) {
        updateVirtualRegisterNumbering((VirtualRegister)inst.dest);
        return null;
    }

    @Override
    public Void visit(MoveInstruction inst) {
        mapNewRegisterTo((VirtualRegister) inst.dest, inst.src);
        return null;
    }

    @Override
    public Void visit(ReturnInstruction inst) {
        return null;
    }

    @Override
    public Void visit(StoreInstruction inst) {
        return null;
    }

    @Override
    public Void visit(UnaryInstruction inst) {
        if(canValueNumbering(inst.src) && canValueNumbering(inst.dest)){}
        else if(inst.src instanceof VirtualRegister)
            updateVirtualRegisterNumbering((VirtualRegister) inst.dest);
        return null;
    }

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
