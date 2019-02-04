package kstarxin.utilities;

public class Symbol {
    private String id;
    private MxType type;
    private Location defLoc;
    private String defScope;
    public Symbol(String _id, MxType _type, String _defScope, Location _defLoc){
        id = _id;
        type = _type;
        defScope = _defScope;
        defLoc = _defLoc;
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

    public Location getLocation(){
        return defLoc;
    }
}
