package kstarxin.utilities;

import kstarxin.parser.*;

public class OperatorTranslator {
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
                ret = "ASSGIN";
                break;
            case MxStarParser.BITAND:
                ret = "BITAND";
                break;
            case MxStarParser.AND:
                ret = "AND";
                break;
            case MxStarParser.BITNOT:
                ret = "BITNOT";
                break;
            case MxStarParser.BITOR:
                ret = "OR";
                break;
            case MxStarParser.BITXOR:
                ret = "XOR";
                break;
            case MxStarParser.DEC:
                ret = "DEC";
                break;
            case MxStarParser.DIV:
                ret = "IDIV";
                break;
            case MxStarParser.EQ:
                ret = "EQ";
                break;
            case MxStarParser.GE:
                ret = "GE";
                break;
            case MxStarParser.GT:
                ret = "GT";
                break;
            case MxStarParser.INC:
                ret = "INC";
                break;
            case MxStarParser.LE:
                ret = "LE";
                break;
            case MxStarParser.LT:
                ret = "LT";
                break;
            case MxStarParser.MOD:
                ret = "MOD";
                break;
            case MxStarParser.MUL:
                ret = "IMUL";
                break;
            case MxStarParser.NEQ:
                ret = "NEQ";
                break;
            case MxStarParser.OR:
                ret = "OR";
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
}
