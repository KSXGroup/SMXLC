package kstarxin.utilities;

public class Symbol {
    private String id;
    private MxType type;
    public Symbol(String _id, MxType _type){
        id = _id;
        type = _type;
    }
    public String getIdentifier(){
        return id;
    }

    public MxType getType(){
        return type;
    }
}
