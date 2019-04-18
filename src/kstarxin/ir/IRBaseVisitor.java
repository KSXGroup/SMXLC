package kstarxin.ir;

import kstarxin.ir.instruction.*;
import kstarxin.ir.operand.*;
import kstarxin.ir.operand.physical.*;

public interface IRBaseVisitor<T>{
    public T visit(Method method);
    public T visit(BinaryArithmeticInstruction inst);
    public T visit(CallInstruction inst);
    public T visit(CompareInstruction inst);
    public T visit(ConditionJumpInstruction inst);
    public T visit(DirectJumpInstruction inst);
    public T visit(Instruction inst);
    public T visit(JumpInstruction inst);
    public T visit(LoadInstruction inst);
    public T visit(MoveInstruction inst);
    public T visit(ReturnInstruction inst);
    public T visit(StoreInstruction inst);
    public T visit(UnaryInstruction inst);
    public Operand visit(Operand operand);
    public Operand visit(PhysicalRegister operand);
    public Operand visit(StackSpace operand);
    public Operand visit(Address operand);
    public Operand visit(Constant operand);
    public Operand visit(Immediate operand);
    public Operand visit(Label operand);
    public Operand visit(Memory operand);
    public Operand visit(Register operand);
    public Operand visit(StaticString operand);
    public Operand visit(StaticPointer operand);
    public Operand visit(VirtualRegister operand);
}

