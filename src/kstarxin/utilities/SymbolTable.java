package kstarxin.utilities;

import org.antlr.v4.runtime.Token;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class SymbolTable {
    private Map<String, Symbol> currentScopeVariableTable;
    private Map<String, Symbol> currentScopeMethodTable;
    private Map<String, Symbol> currentScopeClassTable;
    public SymbolTable(){
        currentScopeVariableTable   = new HashMap<String, Symbol>();
        currentScopeMethodTable     = new HashMap<String, Symbol>();
        currentScopeClassTable      = new HashMap<String, Symbol>();
    }

    public SymbolTable(SymbolTable other){
        currentScopeVariableTable   = new HashMap<String, Symbol>(other.currentScopeVariableTable);
        currentScopeMethodTable     = new HashMap<String, Symbol>(other.currentScopeMethodTable);
        currentScopeClassTable      = new HashMap<String, Symbol>(other.currentScopeClassTable);
    }

    public void addVariable(String type, String id) throws CompileException{
        if(!currentScopeVariableTable.containsKey(id)) currentScopeVariableTable.put(id, new Symbol(id, new MxType(type)));
        else throw new CompileException("redeclaration of " + id);
    }

    public void addVariable(String type, String id, int dim) throws CompileException{
        if(!currentScopeVariableTable.containsKey(id)) currentScopeVariableTable.put(id, new Symbol(id, new MxType(type, dim)));
        else throw new CompileException("redeclaration of " + id);
    }

    public void addMethod(String retType, String id) throws CompileException{

    }

    public void addMethod(String retType, String id, int dim) throws CompileException{

    }

    public void addClass(String id) throws CompileException{

    }

    public Symbol search(Token id){
        return null;
    }
    public Symbol search(String id){
        return null;
    }
}
