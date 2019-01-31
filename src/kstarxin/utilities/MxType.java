package kstarxin.utilities;

public class MxType {
    private String name;
    private TypeEnum type;
    private boolean isPrimitiveType;
    private int dimension;
    public static enum TypeEnum {
        BOOL, INT, STRING, NULL, METHOD, CLASS, NOT_DECIDED
    }
    public static String enumToString(TypeEnum _typeEnum){
        switch(_typeEnum){
            case STRING:
                return "string";
            case BOOL:
                return "bool";
            case INT:
                return "int";
            case NULL:
                return "null";
            case CLASS:
            case METHOD:
                return null;
            default:
                return null;
        }
    }
    public static TypeEnum stringToEnum(String _name){
        switch (_name){
            case "bool":
                return TypeEnum.BOOL;
            case "int":
                return TypeEnum.INT;
            case "string":
                return TypeEnum.STRING;
            case "null":
                return TypeEnum.NULL;
            default:
                return TypeEnum.NOT_DECIDED;
        }
    }
    public MxType(TypeEnum _type){
        name = enumToString(_type);
        type = _type;
        dimension = 0;
        if(_type == TypeEnum.METHOD || _type == TypeEnum.CLASS || _type == TypeEnum.NOT_DECIDED) isPrimitiveType = false;
        else isPrimitiveType = true;
    }

    public MxType(TypeEnum _type, int _dim){
        name = enumToString(_type);
        type = _type;
        dimension = _dim;
        if(_type == TypeEnum.METHOD || _type == TypeEnum.CLASS || _type == TypeEnum.NOT_DECIDED) isPrimitiveType = false;
        else isPrimitiveType = true;
    }

    public MxType(String _name){
        name = _name;
        type = stringToEnum(_name);
        dimension = 0;
        if (type == TypeEnum.METHOD || type == TypeEnum.CLASS || type == TypeEnum.NOT_DECIDED) isPrimitiveType = false;
        else isPrimitiveType = true;
    }

    public MxType(String _name, int _dim){
        name = _name;
        type = stringToEnum(_name);
        dimension = _dim;
        if (type == TypeEnum.METHOD || type == TypeEnum.CLASS || type == TypeEnum.NOT_DECIDED) isPrimitiveType = false;
        else isPrimitiveType = true;
    }


    public MxType(TypeEnum _type, String _name){
        name = _name;
        type = _type;
        dimension = 0;
        if(type == TypeEnum.METHOD || type == TypeEnum.CLASS || type == TypeEnum.NOT_DECIDED) isPrimitiveType = false;
        else isPrimitiveType = true;
    }

    public MxType(TypeEnum _type, String _name, int _dim){
        name = _name;
        type = _type;
        dimension = _dim;
        if(type == TypeEnum.METHOD || type == TypeEnum.CLASS || type == TypeEnum.NOT_DECIDED) isPrimitiveType = false;
        else isPrimitiveType = true;
    }

    public void setType(MxType.TypeEnum newType){
        type = newType;
    }

    public boolean isPrimitiveType() {
        return isPrimitiveType;
    }

    public String toString(){
        return name;
    }
}
