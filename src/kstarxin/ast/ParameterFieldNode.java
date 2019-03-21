package kstarxin.ast;

import kstarxin.utilities.Location;
import kstarxin.utilities.MxType;
import kstarxin.utilities.SymbolTable;

import java.util.ArrayList;
import java.util.LinkedList;

public class ParameterFieldNode extends Node {
    private ArrayList<ParameterDeclarationNode> paraDeclList;
    private ArrayList<MxType> paraTypeList;

    public ParameterFieldNode(ArrayList<ParameterDeclarationNode> _paraDeclList, ArrayList<MxType> _paraTypeList, SymbolTable stb, Location loc){
        super(stb, loc);
        paraDeclList = _paraDeclList;
        paraTypeList = _paraTypeList;
    }

    public ArrayList<ParameterDeclarationNode> getParameterList(){
        return paraDeclList;
    }

    public ArrayList<MxType> getParameterTypeList(){
        return paraTypeList;
    }

    @Override
    public <T> T accept(ASTBaseVisitor<T> visitor) {
        return null;
    }
}
