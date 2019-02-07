package kstarxin.utilities;

public class Symbol {
    private String id;
    private MxType type;
    private Location defLoc;
    private String defScope;
    private SymbolTable memberTable;
    private boolean isBuiltIn;
    public Symbol(String _id, MxType _type, String _defScope, Location _defLoc){
        id = _id;
        type = _type;
        defScope = _defScope;
        defLoc = _defLoc;
        memberTable = null;
        isBuiltIn = false;
    }

    public Symbol(String _id, MxType _type, String _defScope,SymbolTable _memberTable, Location _defLoc){
        id = _id;
        type = _type;
        defScope = _defScope;
        defLoc = _defLoc;
        memberTable = _memberTable;
        isBuiltIn = false;
    }


    public String getIdentifier(){
        return id;
    }

    public MxType getType(){
        return type;
    }

    public String getScopeName(){
        return defScope;
    }

    public SymbolTable getMemberTable(){
        return memberTable;
    }

    public boolean isBuiltIn(){
        return isBuiltIn;
    }

    public void setBuiltIn(){
        isBuiltIn = true;
    }

    public Location getLocation(){
        return defLoc;
    }
}
