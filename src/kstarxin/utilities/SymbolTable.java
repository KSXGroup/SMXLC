package kstarxin.utilities;

import org.antlr.v4.runtime.Token;
import kstarxin.ast.ASTBuilderVisitor;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class SymbolTable {
    private String name = "";
    private Map<String, Symbol> currentScopeTable;
    private LinkedList<SymbolTable> childScopeSymbolTable;


    public SymbolTable(String _name){
        currentScopeTable       = new HashMap<String, Symbol>();
        childScopeSymbolTable   = new LinkedList<>();
        name = _name;
    }

    public SymbolTable(SymbolTable other){
        currentScopeTable   = new HashMap<String, Symbol>(other.currentScopeTable);
        childScopeSymbolTable = new LinkedList<>();
    }

    public void addVariable(Symbol s){
        String id = s.getIdentifier();
        if(!currentScopeTable.containsKey(id)) currentScopeTable.put(id, s);
        else if(!currentScopeTable.get(id).getScopeName().equals(name)) currentScopeTable.replace(id, s);
        else throw new CompileException("redeclaration of " + id);
    }

    public void addVariable(MxType type, String id, Location defLoc) {
        if(!currentScopeTable.containsKey(id)) currentScopeTable.put(id, new Symbol(Symbol.SymbolType.VARIABLE, id, type, name, defLoc));
        else if(!currentScopeTable.get(id).getScopeName().equals(name)) currentScopeTable.replace(id, new Symbol(Symbol.SymbolType.VARIABLE, id, type, name, defLoc));
        else throw new CompileException("redeclaration of " + id);
    }

    public void addMethod(MxType retType, String id,Location defLoc) {
        if(!currentScopeTable.containsKey(id)) currentScopeTable.put(id, new Symbol(Symbol.SymbolType.METHOD, id, retType, name, defLoc));
        else if(!currentScopeTable.get(id).getScopeName().equals(name)) currentScopeTable.replace(id, new Symbol(Symbol.SymbolType.METHOD, id, retType, name, defLoc));
        else throw new CompileException("redeclaration of method " + id);
    }

    public void addBuiltInMethod(MxType retType, String id,Location defLoc){
        Symbol sym = new Symbol(Symbol.SymbolType.METHOD, id, retType, name, defLoc);
        sym.setBuiltIn();
        currentScopeTable.put(id, sym);
    }

    public void addClass(String id, SymbolTable memTable, Location defLoc){
        if(!currentScopeTable.containsKey(id)) currentScopeTable.put(id, new Symbol(Symbol.SymbolType.CLASS, id ,new MxType(MxType.TypeEnum.CLASS), name, memTable, defLoc));
        else if(!currentScopeTable.get(id).getScopeName().equals(name)) currentScopeTable.replace(id, new Symbol(Symbol.SymbolType.CLASS, id ,new MxType(MxType.TypeEnum.CLASS), name, memTable, defLoc));
        else throw new CompileException("reclaration of class " + id);
    }

    public void addChildSymbolTable(SymbolTable stb){
        childScopeSymbolTable.add(stb);
    }

    public void pushDown(){
        // Check type
        for(Symbol s : currentScopeTable.values()){
            if(s.getSymbolType().equals(Symbol.SymbolType.VARIABLE) || s.getSymbolType().equals(Symbol.SymbolType.METHOD)) {
                MxType t = s.getType();
                String id = t.toString();
                if (t.getEnumType().equals(MxType.TypeEnum.NOT_DECIDED)) {
                    Integer line = s.getLocation().getLineNumber(), column = s.getLocation().getColumnNumber();
                    if (!currentScopeTable.containsKey(id))
                        throw new CompileException("type " + id + " is not declared in this scope");
                    else if (!currentScopeTable.get(id).isClass()) throw new CompileException(id + " can not be used as type " + line.toString() + ":" +column.toString());
                    else t.setType(MxType.TypeEnum.CLASS);
                }
            }
        }
        if(childScopeSymbolTable.size() == 0) return;
        for(SymbolTable cstb : childScopeSymbolTable){
            for(String key : currentScopeTable.keySet()) {
                if (!cstb.currentScopeTable.containsKey(key))
                    cstb.currentScopeTable.put(key, currentScopeTable.get(key));
            }
            cstb.pushDown();
        }
    }

    public void dumpSymbolTable(String indent, PrintStream out){
        out.print("\n");
        out.println("[Scope: "+name+"]");
        String name = null;
        MxType type = null;
        Location loc = null;
        Symbol.SymbolType stype = null;
        for(String key : currentScopeTable.keySet()){
            stype = currentScopeTable.get(key).getSymbolType();
            name = currentScopeTable.get(key).getIdentifier();
            type = currentScopeTable.get(key).getType();
            loc = currentScopeTable.get(key).getLocation();
            switch (stype) {
                case VARIABLE:
                    out.println("[Variable]");
                    out.println(indent + name + ":" + "[" + type.toString() + ":" + type.getEnumString() + " dim = " + type.getDimension() + " Define at: " + loc.getLineNumber() + ":" + loc.getColumnNumber() + "]");
                    break;
                case METHOD:
                    out.println("[Method]");
                    out.println(indent + name + ":[retType: " + type.toString()+":"+ type.getEnumString()+" dim = " +type.getDimension()+ " Define at: " + loc.getLineNumber() +":"+ loc.getColumnNumber() + "]");
                    break;
                case CLASS:
                    out.println("[Class]");
                   // currentScopeTable.get(key).getMemberTable().dumpSymbolTable("", System.err);
                    out.println(indent + "[class:" + name+"]"+ " Define at: " + loc.getLineNumber() +":"+ loc.getColumnNumber() + "]");
                    break;
            }
        }
        for(SymbolTable stb : childScopeSymbolTable) stb.dumpSymbolTable(indent + "\t", out);
    }


    public void setName(String _name){
        name = _name;
    }

    public String getName(){
        return  name;
    }

    public boolean contains(String id){
        return currentScopeTable.containsKey(id);
    }

    public Symbol get(String id){
        return currentScopeTable.get(id);
    }

}
