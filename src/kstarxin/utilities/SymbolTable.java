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

    public void addVariable(MxType type, String id, Location defLoc) throws CompileException{
        if(!currentScopeVariableTable.containsKey(id)) currentScopeVariableTable.put(id, new Symbol(id, type, name, defLoc));
        else if(currentScopeVariableTable.get(id).getScopeName() != name) currentScopeVariableTable.replace(id, new Symbol(id, type, name, defLoc));
        else throw new CompileException("redeclaration of " + id);
    }

    public void addMethod(MxType retType, String id,Location defLoc) throws CompileException{
        if(!currentScopeMethodTable.containsKey(id)) currentScopeMethodTable.put(id, new Symbol(id, retType, name, defLoc));
        else throw new CompileException("redeclaration of method " + id);
    }

    public void addClass(String id, Location defLoc) throws CompileException{
        if(!currentScopeClassTable.containsKey(id)) currentScopeClassTable.put(id, new Symbol(id,new MxType(MxType.TypeEnum.CLASS), name, defLoc));
        else throw new CompileException("reclaration of class " + id);
    }

    public void addChildSymbolTable(SymbolTable stb){
        childScopeSymbolTable.add(stb);
    }

    public void dumpSymbolTable(String indent, PrintStream out){
        out.print("\n");
        out.println("[Scope: "+name+"]");
        out.println("[Variables]");
        String name = null;
        MxType type = null;
        Location loc = null;
        for(String key : currentScopeVariableTable.keySet()){
            name = currentScopeVariableTable.get(key).getIdentifier();
            type = currentScopeVariableTable.get(key).getType();
            loc = currentScopeVariableTable.get(key).getLocation();
            out.println(indent + name  + ":" + "[" +type.toString()+":"+ type.getEnumString() +" dim = " +type.getDimension()+" Define at: " + loc.getLineNumber() +":"+ loc.getColumnNumber()+"]");
        }
        out.println("[Methods]");
        for(String key : currentScopeMethodTable.keySet()){
            name = currentScopeMethodTable.get(key).getIdentifier();
            type = currentScopeMethodTable.get(key).getType();
            loc = currentScopeMethodTable.get(key).getLocation();
            out.println(indent + name + ":[retType: " + type.toString()+":"+ type.getEnumString()+" dim = " +type.getDimension()+ " Define at: " + loc.getLineNumber() +":"+ loc.getColumnNumber() + "]");
        }
        for(SymbolTable stb : childScopeSymbolTable) stb.dumpSymbolTable(indent + "\t", out);
    }

    public Symbol search(Token id){
        return null;
    }
    public Symbol search(String id){
        return null;
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
