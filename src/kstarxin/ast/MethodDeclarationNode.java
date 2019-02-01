package kstarxin.ast;

import kstarxin.utilities.Location;
import kstarxin.utilities.MxType;
import kstarxin.utilities.Symbol;
import kstarxin.utilities.SymbolTable;

import java.util.ArrayList;
import java.util.LinkedList;

public class MethodDeclarationNode extends DeclarationNode {
    private ArrayList<ParameterDeclarationNode> paraList;
    private LinkedList<Node> statementList;
    private TypeNode retType;
    private String id;

    public MethodDeclarationNode(TypeNode _type, String _id, ArrayList _paraList, LinkedList _statementList ,SymbolTable stb, Location loc){
        super(stb, loc);
        retType = _type;
        id = _id;
        paraList = _paraList;
        statementList = _statementList;
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

    public ArrayList getParameterList(){
        return paraList;
    }

    public LinkedList getStatementList(){
        return statementList;
    }

    @Override
    public <T> T accept(ASTBaseVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
