package kstarxin.utilities;

import kstarxin.ir.operand.StaticString;

public class StringParser {
    public static String parseString(String originString){
        int len = originString.length();
        String ret = "";
        for(int i = 0; i < len; ++i){
            if(originString.charAt(i) == '\\') {
                switch (originString.charAt(i + 1)) {
                    case 'b':
                        ret += '\b';
                        break;
                    case 'f':
                        ret += '\f';
                        break;
                    case 'n':
                        ret += '\n';
                        break;
                    case 'r':
                        ret += '\r';
                        break;
                    case 't':
                        ret += '\t';
                        break;
                    case '\\':
                        ret += '\\';
                        break;
                    case '\'':
                        ret += '\'';
                        break;
                    case '\"':
                        ret += '\"';
                        break;
                }
                i += 1;
            }else if(originString.charAt(i) != '\"') ret += originString.charAt(i);
        }
        return ret;
    }

    public static String stringToHex(String s){
        //this function return string in hex format with its length at begining
        int len = s.length();
        String ret = "";
        String tmp = null;
        ret += "\tDQ\t" + s.length() + "\n\tDB\t";
        for(byte i :s.getBytes()){
            tmp = Integer.toHexString((int)(i & 0xFF)).toUpperCase();
            if(tmp.length() == 1) tmp = "0" + tmp;
            ret += tmp + "H, ";
        }
        ret += "00H";
        return ret;
    }

}
