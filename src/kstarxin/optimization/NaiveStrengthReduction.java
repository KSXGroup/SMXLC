package kstarxin.optimization;

import kstarxin.ir.IRBaseVisitor;
import kstarxin.ir.IRProgram;
import kstarxin.ir.Method;
import kstarxin.ir.instruction.*;
import kstarxin.ir.operand.*;
import kstarxin.parser.*;
import kstarxin.utilities.FastDivision;

public class NaiveStrengthReduction {
    IRProgram ir;

    public NaiveStrengthReduction(IRProgram _ir){
        ir = _ir;
    }

    public void run(){
        ir.getMethodMap().values().forEach(m->{
            m.dfs();
            m.basicBlockInDFSOrder.forEach(bb->{
                for(Instruction inst = bb.getBeginInst(); inst != null; inst = inst.next){
                    if(inst instanceof BinaryArithmeticInstruction) processBinArith((BinaryArithmeticInstruction) inst);
                }
            });
        });
    }

    private void processBinArith(BinaryArithmeticInstruction inst){
        if(inst.rhs instanceof Immediate){
            long imm = ((Immediate) inst.rhs).value;
            switch (inst.op){
                case MxStarParser.MUL:
                    if(((Immediate) inst.rhs).value == 1) inst.removeThisInst();
                    else if(((Immediate) inst.rhs).value == 0){
                        if(inst.target instanceof VirtualRegister)
                            inst.replaceThisWith(new MoveInstruction((VirtualRegister)inst.target, new Immediate(0)));
                        else if(inst.target instanceof Address)
                            inst.replaceThisWith(new StoreInstruction((Address)inst.target, new Immediate(0)));
                    }else {
                        int shft = isPowerOfTwo(((Immediate) inst.rhs).value);
                        if (shft > 0) inst.replaceThisWith(new BinaryArithmeticInstruction(MxStarParser.SFTL, inst.target,inst.lhs, new Immediate(shft)));
                    }
                    break;
                case MxStarParser.DIV:
                    if(((Immediate) inst.rhs).value == 1) inst.removeThisInst();
                    else {
                        int shft = isPowerOfTwo(((Immediate) inst.rhs).value);
                        if (shft > 0) inst.replaceThisWith(new BinaryArithmeticInstruction(MxStarParser.SFTR, inst.target,inst.lhs, new Immediate(shft)));
                    }
                    break;
                case MxStarParser.MOD:
                    if(((Immediate) inst.rhs).value == 0) {
                        if(inst.target instanceof VirtualRegister)
                            inst.replaceThisWith(new MoveInstruction((VirtualRegister)inst.target, new Immediate(0)));
                        else if(inst.target instanceof Address)
                            inst.replaceThisWith(new StoreInstruction((Address)inst.target, new Immediate(0)));
                    }
                    else{
                        int shft = isPowerOfTwo(((Immediate) inst.rhs).value);
                        if(shft > 0) inst.replaceThisWith(new BinaryArithmeticInstruction(MxStarParser.AND, inst.target, inst.lhs, new Immediate(((Immediate) inst.rhs).value - 1)));
                    }
                    break;
                default:
                    return;
            }
        }
    }

    private int isPowerOfTwo(long imm){
        int st = 1;
        int cnt = 0;
        while(st < imm){
            st <<= 1;
            cnt++;
            if(st == imm) return cnt;
        }
        return -1;
    }


}
