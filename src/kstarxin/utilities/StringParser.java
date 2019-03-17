package kstarxin.utilities;

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
}
