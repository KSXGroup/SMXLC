package kstarxin.utilities;

import org.antlr.v4.runtime.Token;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class SymbolTable {
    private String name = "";
    private Map<String, Symbol> currentScopeVariableTable;
    private Map<String, Symbol> currentScopeMethodTable;
    private Map<String, Symbol> currentScopeClassTable;
    private LinkedList<SymbolTable> childScopeSymbolTable;


    public SymbolTable(String _name){
        currentScopeVariableTable   = new HashMap<String, Symbol>();
        currentScopeMethodTable     = new HashMap<String, Symbol>();
        currentScopeClassTable      = new HashMap<String, Symbol>();
        childScopeSymbolTable       = new LinkedList<>();
        name = _name;
    }

    public SymbolTable(SymbolTable other){
        currentScopeVariableTable   = new HashMap<String, Symbol>(other.currentScopeVariableTable);
        currentScopeMethodTable     = new HashMap<String, Symbol>(other.currentScopeMethodTable);
        currentScopeClassTable      = new HashMap<String, Symbol>(other.currentScopeClassTable);
        childScopeSymbolTable = new LinkedList<>();
    }

    public void addVariable(Symbol s){
        String id = s.getIdentifier();
        if(!currentScopeVariableTable.containsKey(id)) currentScopeVariableTable.put(id, s);
        else if(currentScopeVariableTable.get(id).getScopeName() != name) currentScopeVariableTable.replace(id, s);
        else throw new CompileException("redeclaration of " + id);
    }

    public void addVariable(MxType type, String id, Location defLoc) throws CompileException{
        if(!currentScopeVariableTable.containsKey(id)) currentScopeVariableTable.put(id, new Symbol(id, type, name, defLoc));
        else if(currentScopeVariableTable.get(id).getScopeName() != name) currentScopeVariableTable.replace(id, new Symbol(id, type, name, defLoc));
        else throw new CompileException("redeclaration of " + id);
    }

    public void addMethod(MxType retType, String id,Location defLoc) throws CompileException{
        if(!currentScopeMethodTable.containsKey(id)) currentScopeMethodTable.put(id, new Symbol(id, retType, name, defLoc));
        else throw new CompileException("redeclaration of method " + id);
    }

    public void addBuiltInMethod(MxType retType, String id,Location defLoc){
        Symbol sym = new Symbol(id, retType, name, defLoc);
        sym.setBuiltIn();
        currentScopeMethodTable.put(id, sym);
    }

    public void addClass(String id, SymbolTable memTable, Location defLoc) throws CompileException{
        if(!currentScopeClassTable.containsKey(id)) currentScopeClassTable.put(id, new Symbol(id,new MxType(MxType.TypeEnum.CLASS), name, memTable, defLoc));
        else throw new CompileException("reclaration of class " + id);
    }

    public void addChildSymbolTable(SymbolTable stb){
        childScopeSymbolTable.add(stb);
    }

    public void dumpSymbolTable(String indent, PrintStream out){
        out.print("\n");
        out.println("[Scope: "+name+"]");
        out.println("[Variable(s)]");
        String name = null;
        MxType type = null;
        Location loc = null;
        for(String key : currentScopeVariableTable.keySet()){
            name = currentScopeVariableTable.get(key).getIdentifier();
            type = currentScopeVariableTable.get(key).getType();
            loc = currentScopeVariableTable.get(key).getLocation();
            out.println(indent + name  + ":" + "[" +type.toString()+":"+ type.getEnumString() +" dim = " +type.getDimension()+" Define at: " + loc.getLineNumber() +":"+ loc.getColumnNumber()+"]");
        }
        out.println("[Method(s)]");
        for(String key : currentScopeMethodTable.keySet()){
            name = currentScopeMethodTable.get(key).getIdentifier();
            type = currentScopeMethodTable.get(key).getType();
            loc = currentScopeMethodTable.get(key).getLocation();
            out.println(indent + name + ":[retType: " + type.toString()+":"+ type.getEnumString()+" dim = " +type.getDimension()+ " Define at: " + loc.getLineNumber() +":"+ loc.getColumnNumber() + "]");
        }
        out.println("[Class(es)]");
        for(String key : currentScopeClassTable.keySet()){
            Symbol s =  currentScopeClassTable.get(key);
            name = s.getIdentifier();
            type = s.getType();
            loc = s.getLocation();
            out.println(indent + "[class:" + name+"]"+ " Define at: " + loc.getLineNumber() +":"+ loc.getColumnNumber() + "]");
        }
        for(SymbolTable stb : childScopeSymbolTable) stb.dumpSymbolTable(indent + "\t", out);
    }

    public boolean containsVariable(String id){
        return currentScopeVariableTable.containsKey(id);
    }

    public Symbol getVariable(String id){
        return currentScopeVariableTable.get(id);
    }

    public Symbol getClass(String id){
        return currentScopeClassTable.get(id);
    }

    public void setName(String _name){
        name = _name;
    }

    private Map getVariableTable(){
        return currentScopeVariableTable;
    }
    private Map getMethodTable(){
        return currentScopeMethodTable;
    }
    private Map getClassTable(){
        return currentScopeClassTable;
    }
}
