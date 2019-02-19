package kstarxin.utilities;

import java.util.ArrayList;
import java.util.LinkedList;

public class MxType {
    private String name;
    private TypeEnum type;
    private int dimension;
    private boolean isPrimitiveType;
    private ArrayList<MxType> paraTypeList;
    public static enum TypeEnum {
        BOOL, INT, STRING,VOID, NULL, CLASS, NOT_DECIDED
    }
    public static String enumToString(TypeEnum _typeEnum){
        switch(_typeEnum){
            case STRING:
                return "string";
            case BOOL:
                return "bool";
            case INT:
                return "int";
            case VOID:
                return "void";
            case NULL:
                return "null";
            case CLASS:
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
            case "void":
                return TypeEnum.VOID;
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
        paraTypeList = null;
        if(_type == TypeEnum.CLASS || _type == TypeEnum.NOT_DECIDED) isPrimitiveType = false;
        else isPrimitiveType = true;
    }

    public MxType(TypeEnum _type, int _dim){
        name = enumToString(_type);
        type = _type;
        dimension = _dim;
        paraTypeList = null;
        if(_type == TypeEnum.CLASS || _type == TypeEnum.NOT_DECIDED) isPrimitiveType = false;
        else isPrimitiveType = true;
    }

    public MxType(String _name){
        name = _name;
        type = stringToEnum(_name);
        dimension = 0;
        paraTypeList = null;
        if (type == TypeEnum.CLASS || type == TypeEnum.NOT_DECIDED) isPrimitiveType = false;
        else isPrimitiveType = true;
    }

    public MxType(String _name, ArrayList<MxType> _paraTypeList){
        name = _name;
        type = stringToEnum(_name);
        dimension = 0;
        paraTypeList = _paraTypeList;
        if (type == TypeEnum.CLASS || type == TypeEnum.NOT_DECIDED) isPrimitiveType = false;
        else isPrimitiveType = true;
    }

    public MxType(String _name, int _dim){
        name = _name;
        type = stringToEnum(_name);
        dimension = _dim;
        paraTypeList = null;
        if (type == TypeEnum.CLASS || type == TypeEnum.VOID || type == TypeEnum.NOT_DECIDED) isPrimitiveType = false;
        else isPrimitiveType = true;
    }

    public MxType(String _name, int _dim, ArrayList<MxType> _paraTypeList){
        name = _name;
        type = stringToEnum(_name);
        dimension = _dim;
        paraTypeList = _paraTypeList;
        if (type == TypeEnum.CLASS || type == TypeEnum.VOID || type == TypeEnum.NOT_DECIDED) isPrimitiveType = false;
        else isPrimitiveType = true;
    }

    public MxType(TypeEnum _type, String _name){
        name = _name;
        type = _type;
        dimension = 0;
        paraTypeList = null;
        if (type == TypeEnum.CLASS || type == TypeEnum.VOID || type == TypeEnum.NOT_DECIDED)  isPrimitiveType = false;
        else isPrimitiveType = true;
    }

    public MxType(TypeEnum _type, String _name, ArrayList<MxType> _paraTypeList){
        name = _name;
        type = _type;
        dimension = 0;
        paraTypeList = _paraTypeList;
        if (type == TypeEnum.CLASS || type == TypeEnum.VOID || type == TypeEnum.NOT_DECIDED)  isPrimitiveType = false;
        else isPrimitiveType = true;
    }

    public MxType(TypeEnum _type, String _name, int _dim){
        name = _name;
        type = _type;
        dimension = _dim;
        paraTypeList = null;
        if (type == TypeEnum.CLASS || type == TypeEnum.VOID || type == TypeEnum.NOT_DECIDED)  isPrimitiveType = false;
        else isPrimitiveType = true;
    }

    public MxType(TypeEnum _type, String _name, int _dim, ArrayList<MxType> _paraTypeList){
        name = _name;
        type = _type;
        dimension = _dim;
        paraTypeList = _paraTypeList;
        if (type == TypeEnum.CLASS || type == TypeEnum.VOID || type == TypeEnum.NOT_DECIDED)  isPrimitiveType = false;
        else isPrimitiveType = true;
    }

    public void setType(MxType.TypeEnum newType){
        type = newType;
    }

    public void setParaTypeList(ArrayList _paraTypeList){
        paraTypeList = _paraTypeList;
    }

    public boolean isPrimitiveType() {
        return isPrimitiveType;
    }

    public TypeEnum getEnumType(){
        return type;
    }

    public String getEnumString(){
        return type.toString();
    }

    public int getDimension(){
        return dimension;
    }

    public ArrayList<MxType> getParameterTypeList(){
        return paraTypeList;
    }

    public String toString(){
        return name;
    }

    public boolean equals(MxType other){
        if(other.name.equals(name) && other.dimension == dimension && other.type.equals(type)) return true;
        if(dimension > 0 && other.getEnumType().equals(TypeEnum.NULL)) return true;
        if(!(type.equals(TypeEnum.BOOL) || type.equals(TypeEnum.INT) || type.equals(TypeEnum.STRING)) && other.getEnumType().equals(TypeEnum.NULL)) return true;
        return false;
    }

    public MxType cloneWithoutParameter(){
        return new MxType(type, name ,dimension);
    }

}
