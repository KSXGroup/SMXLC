package kstarxin.utilities;

import kstarxin.ast.*;

public class NameMangler {
        public final static String globalPrefix   = "@";
        public final static String methodPrefix   = "_Z";
        public final static String initMethod     = "@_INIT_";
        public final static String strcmp         = "@_Zstrcmpss";
        public final static String strcat         = "@_Zstrcatss";
        public final static String malloc         = "@_Zmalloci";
        public final static String substring      = "@_Zstring4substringii";
        public final static String ord            = "@_Zstring4ordi";
        public final static String parseInt       = "@_Zstring4parseInt";
        public final static String mainMethodName = "@main";
        public final static String inlineSuffix   = "#inline";

        public static boolean isGlobal(String scopeName){
            if(scopeName.equals(ASTBuilderVisitor.globalScopeName)) return true;
            else return false;
        }

        public static String mangleName(VariableDeclaratorNode n){
            String scopeName = n.getCurrentSymbolTable().getName();
            String idName = n.getIdentifier();
            if(isGlobal(scopeName)) return globalPrefix + idName;
            else return scopeName + "_" + idName;
        }

        public static String mangleName(MethodDeclarationNode n){
            if(n.getIdentifier().equals("main")) return globalPrefix + "main"; // ignore all parameters of main method
            String mangledName =globalPrefix + methodPrefix + n.getIdentifier();
            for(MxType t : n.getReturnType().getParameterTypeList()) {
                if(t.isPrimitiveType()) mangledName += t.toString().charAt(0);
                else mangledName += t.toString();
            }
            return mangledName;
        }

        public static String mangleName(String className ,MethodDeclarationNode n){
            String ret = mangleName(n);
            return ret + globalPrefix + className;
        }

        public static String mangleName(MethodCallNode n){
            return mangleName(n.getCurrentSymbolTable().get(n.getMethodName(), n.getLocation()));
        }

        public static String mangleName(String className, MethodCallNode n){
            String ret = mangleName(n);
            return ret + globalPrefix + className;
        }

        public static String mangleClassConstructor(String className){
            return globalPrefix + methodPrefix + className + globalPrefix + className;
        }

        public static String mangleName(LoopNode n){
            return n.getLoopName();
        }

        public static String mangleName(Symbol s){
            Symbol.SymbolType st = s.getSymbolType();
            String ret = "";
            String scopeName = "";
            String symbolId = s.getIdentifier();
            switch (st){
                case METHOD:
                    if(symbolId.equals("main")) return globalPrefix + symbolId;
                    else{
                        ret = globalPrefix  + methodPrefix + symbolId;
                        for(MxType t : s.getType().getParameterTypeList()){
                            if(t.isPrimitiveType()) ret += t.toString().charAt(0);
                            else ret += t.toString();
                        }
                    }
                    return ret;

                case VARIABLE:
                    if(isGlobal(s.getScopeName())) return globalPrefix + s.getIdentifier();
                    else return s.getScopeName() + "_" + s.getIdentifier();

                case CLASS:
                    return s.getIdentifier();
            }
            return null;
        }

        public static String mangleName(String className, Symbol s){
            String ret = mangleName(s);
             return ret + globalPrefix + className;
        }

        public static String mangleName(ParameterDeclarationNode n) {
            String scopeName = n.getCurrentSymbolTable().getName();
            return scopeName + "_" + n.getIdentifier();
        }

        public static String mangleName(ConditionNode n){
            return n.getName();
        }
}
