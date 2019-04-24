package kstarxin.utilities;

import kstarxin.parser.*;

public class OperatorTranslator {

    public enum NASMInstructionOperator{MOV, MOVZX, CMP, PUSH, POP, ADD, SUB, IDIV, IMUL,NEG, AND, JMP, JE ,JNE,
                                        JNZ, JGE, JG, JLE, JL, LEA, NOT, OR, XOR, DEC, INC, SAR, SAL, SHL, LEAVE,
                                        RET, SETL, SETLE, SETG, SETGE, SETE, SETNE, RESQ, RESD, DQ, DD};

    public static NASMInstructionOperator toNASMOperator(int op){
        NASMInstructionOperator ret = null;
        switch (op){
            case MxStarParser.ADD:
                ret = NASMInstructionOperator.ADD;
                break;
            case MxStarParser.BITAND:
                ret = NASMInstructionOperator.AND;
                break;
            case MxStarParser.AND:
                ret = NASMInstructionOperator.AND;
                break;
            case MxStarParser.BITNOT:
            case MxStarParser.NOT:
                ret = NASMInstructionOperator.NOT;
                break;
            case MxStarParser.OR:
            case MxStarParser.BITOR:
                ret = NASMInstructionOperator.OR;
                break;
            case MxStarParser.BITXOR:
                ret = NASMInstructionOperator.XOR;
                break;
            case MxStarParser.EQ:
            case MxStarParser.NEQ:
            case MxStarParser.GE:
            case MxStarParser.GT:
            case MxStarParser.LE:
            case MxStarParser.LT:
                throw new RuntimeException("never try to translate compare operand");
            case MxStarParser.DEC:
                ret = NASMInstructionOperator.DEC;
                break;
            case MxStarParser.DIV:
                throw new RuntimeException("never try to translate div operand");
            case MxStarParser.INC:
                ret = NASMInstructionOperator.INC;
                break;
            case MxStarParser.MOD:
                throw new RuntimeException("never try to translate mod operand");
            case MxStarParser.MUL:
                ret = NASMInstructionOperator.IMUL;
                break;
            case MxStarParser.SFTL:
                ret = NASMInstructionOperator.SAL;
                break;
            case MxStarParser.SFTR:
                ret = NASMInstructionOperator.SAR;
                break;
            case MxStarParser.SUB:
                ret = NASMInstructionOperator.SUB;
                break;
            default:
                throw new RuntimeException("unknown operand!!");
        }
        return ret;
    }

    public static String operatorToLiteralName(int op){
        String ret = "";
        switch (op){
            case MxStarParser.ADD:
                ret = "+";
                break;
            case MxStarParser.ASSIGN:
                ret = "=";
                break;
            case MxStarParser.BITAND:
                ret = "&";
                break;
            case MxStarParser.AND:
                ret = "&&";
                break;
            case MxStarParser.BITNOT:
                ret = "~";
                break;
            case MxStarParser.BITOR:
                ret = "|";
                break;
            case MxStarParser.BITXOR:
                ret = "^";
                break;
            case MxStarParser.DEC:
                ret = "--";
                break;
            case MxStarParser.DIV:
                ret = "/";
                break;
            case MxStarParser.EQ:
                ret = "==";
                break;
            case MxStarParser.GE:
                ret = ">=";
                break;
            case MxStarParser.GT:
                ret = ">";
                break;
            case MxStarParser.INC:
                ret = "++";
                break;
            case MxStarParser.LE:
                ret = "<=";
                break;
            case MxStarParser.LT:
                ret = "<";
                break;
            case MxStarParser.MOD:
                ret = "%";
                break;
            case MxStarParser.MUL:
                ret = "*";
                break;
            case MxStarParser.NEQ:
                ret = "!=";
                break;
            case MxStarParser.OR:
                ret = "||";
                break;
            case MxStarParser.SFTL:
                ret = "<<";
                break;
            case MxStarParser.SFTR:
                ret = ">>";
                break;
            case MxStarParser.NOT:
                ret = "!";
                break;
            case MxStarParser.SUB:
                ret = "-";
        }
        return ret;
    }

    public static String operatorToSymbolicName(int op){
        String ret = "";
        switch (op){
            case MxStarParser.ADD:
                ret = "ADD";
                break;
            case MxStarParser.ASSIGN:
                ret = "ASS";
                break;
            case MxStarParser.BITAND:
                ret = "BAN";
                break;
            case MxStarParser.AND:
                ret = "AND";
                break;
            case MxStarParser.BITNOT:
                ret = "BNO";
                break;
            case MxStarParser.BITOR:
                ret = "BOR";
                break;
            case MxStarParser.BITXOR:
                ret = "XOR";
                break;
            case MxStarParser.DEC:
                ret = "DEC";
                break;
            case MxStarParser.DIV:
                ret = "DIV";
                break;
            case MxStarParser.EQ:
                ret = "EQU";
                break;
            case MxStarParser.GE:
                ret = "GEQ";
                break;
            case MxStarParser.GT:
                ret = "GTH";
                break;
            case MxStarParser.INC:
                ret = "INC";
                break;
            case MxStarParser.LE:
                ret = "LEQ";
                break;
            case MxStarParser.LT:
                ret = "LTH";
                break;
            case MxStarParser.MOD:
                ret = "MOD";
                break;
            case MxStarParser.MUL:
                ret = "MUL";
                break;
            case MxStarParser.NEQ:
                ret = "NEQ";
                break;
            case MxStarParser.OR:
                ret = "ORI";
                break;
            case MxStarParser.SFTL:
                ret = "SHL";
                break;
            case MxStarParser.SFTR:
                ret = "SAR";
                break;
            case MxStarParser.NOT:
                ret = "NOT";
                break;
            case MxStarParser.SUB:
                ret = "SUB";
        }
        return ret;
    }

    public static boolean isCompareOperator(int op){
        switch (op){
            case MxStarParser.NEQ:
            case MxStarParser.EQ:
            case MxStarParser.GE:
            case MxStarParser.GT:
            case MxStarParser.LE:
            case MxStarParser.LT:
                return true;
            default:
                return false;
        }
    }

}
