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
    private SymbolTable enclosingTable;
    private boolean isClassTable;
    private boolean isGlobal;

    private LinkedList<SymbolTable> childScopeTable;

    public SymbolTable(String _name){
        currentScopeTable  = new HashMap<String, Symbol>();
        childScopeTable = new LinkedList<SymbolTable>();
        enclosingTable     = null;
        isClassTable = false;
        isGlobal = false;
        name = _name;
    }

    public SymbolTable(SymbolTable other){
        currentScopeTable   = new HashMap<String, Symbol>();
        childScopeTable = new LinkedList<SymbolTable>();
        enclosingTable = other;
        isClassTable = false;
        isGlobal = false;

        other.addChildSymbolTable(this);
    }

    public void addVariable(Symbol s){
        String id = s.getIdentifier();
        if(!currentScopeTable.containsKey(id)) currentScopeTable.put(id, s);
        else throw new CompileException("redeclaration of " + id);
    }

    public void addVariable(MxType type, String id, Location defLoc) {
        if(!currentScopeTable.containsKey(id)) currentScopeTable.put(id, new Symbol(Symbol.SymbolType.VARIABLE, id, type, name, defLoc));
        else throw new CompileException("redeclaration of " + id);
    }

    public void addMethod(MxType retType, String id,Location defLoc) {
        if(!currentScopeTable.containsKey(id)) currentScopeTable.put(id, new Symbol(Symbol.SymbolType.METHOD, id, retType, name, defLoc));
        else throw new CompileException("redeclaration of method " + id);
    }

    public void addBuiltInMethod(MxType retType, String id,Location defLoc){
        Symbol sym = new Symbol(Symbol.SymbolType.METHOD, id, retType, name, defLoc);
        sym.setBuiltIn();
        currentScopeTable.put(id, sym);
    }

    public void addClass(String id, SymbolTable memTable, Location defLoc){
        if(!currentScopeTable.containsKey(id)) currentScopeTable.put(id, new Symbol(Symbol.SymbolType.CLASS, id ,new MxType(MxType.TypeEnum.CLASS), name, memTable, defLoc));
        else throw new CompileException("reclaration of class " + id);
    }

    public void addChildSymbolTable(SymbolTable stb){
        childScopeTable.add(stb);
    }

   public void pushDown(){
        // Check type
        for(Symbol s : currentScopeTable.values()){
            if(s.getSymbolType().equals(Symbol.SymbolType.VARIABLE) || s.getSymbolType().equals(Symbol.SymbolType.METHOD)) {
                MxType t = s.getType();
                String id = t.toString();
                if (t.getEnumType().equals(MxType.TypeEnum.NOT_DECIDED)) {
                    Symbol defs = get(t.toString(), s.getLocation());
                    if(defs == null) throw new CompileException("Type " + t.toString() + " is not defined");
                    else if(!defs.isClass()) throw new CompileException(t.toString() + " can not be be used as type");
                    else t.setType(MxType.TypeEnum.CLASS);
                }
            }
        }
        if(childScopeTable.size() == 0) return;
        childScopeTable.forEach(s -> s.pushDown());
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
        for(SymbolTable stb : childScopeTable) stb.dumpSymbolTable(indent + "\t", out);
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

    public Symbol get(String id, Location loc){
        Symbol s = currentScopeTable.get(id);
        if(s != null){
            if(isGlobal){
                if(s.isVariable()) {
                    if(s.getLocation().before(loc)) return s;
                    else return null;
                }
                else return s;
            }
            if(isClassTable) return s;
            if(s.getLocation().before(loc)) return s;
            else if(enclosingTable != null) return enclosingTable.get(id, loc);
            else return null;
        }
        else if(enclosingTable != null) return enclosingTable.get(id, loc);
        else return null;
    }

    public Symbol getMember(String id){
       return currentScopeTable.get(id);
    }

    public void setClassTable(){
       isClassTable = true;
    }

    public void setGlobal(){
       isGlobal = true;
    }

}
