package kstarxin.utilities;

public class Symbol {
    private String id;
    private MxType type;
    private Location defLoc;
    public Symbol(String _id, MxType _type, Location _defLoc){
        id = _id;
        type = _type;
        defLoc = _defLoc;
    }
    public String getIdentifier(){
        return id;
    }

    public MxType getType(){
        return type;
    }

    public Location getLocation(){
        return defLoc;
    }
}
