package kstarxin.ir.instruction;

import kstarxin.ir.BasicBlock;

public class Instruction {
    public String hintName;
    public Instruction next;
    public Instruction prev;
    public BasicBlock basicBlockBelongTo;
    public Instruction(String _hintName){
        hintName = _hintName;
    }
}
