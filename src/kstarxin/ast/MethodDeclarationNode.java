package kstarxin.ast;

import kstarxin.utilities.Location;
import kstarxin.utilities.MxType;
import kstarxin.utilities.Symbol;
import kstarxin.utilities.SymbolTable;

import java.util.ArrayList;
import java.util.LinkedList;

public class MethodDeclarationNode extends DeclarationNode {
    private ArrayList<ParameterDeclarationNode> paraList;
    private BlockNode block;
    private TypeNode retType;
    private String id;

    public MethodDeclarationNode(TypeNode _type, String _id, ArrayList<ParameterDeclarationNode> _paraList, BlockNode _block, SymbolTable stb, Location loc){
        super(stb, loc);
        retType = _type;
        id = _id;
        paraList = _paraList;
        block = _block;
    }

    public String getIdentifier(){
        return id;
    }

    public TypeNode geTypeNode(){
        return retType;
    }

    public MxType getReturnType(){
        return retType.getType();
    }

    public ArrayList<ParameterDeclarationNode> getParameterList(){
        return paraList;
    }

    public BlockNode getBlock(){
        return block;
    }

    @Override
    public <T> T accept(ASTBaseVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
