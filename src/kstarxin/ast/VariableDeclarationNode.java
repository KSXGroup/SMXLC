package kstarxin.ast;

import kstarxin.utilities.Location;
import kstarxin.utilities.MxType;
import kstarxin.utilities.SymbolTable;

import java.util.LinkedList;

public class VariableDeclarationNode extends DeclarationNode {
    private TypeNode type;
    private LinkedList<VariableDeclaratorNode> decls;
    public VariableDeclarationNode(TypeNode _type, SymbolTable stb, Location loc){
        super(stb, loc);
        type = _type;
        decls = new LinkedList<>();
    }

    public void addDeclarator(VariableDeclaratorNode decltor){
        decls.add(decltor);
    }

    public LinkedList<VariableDeclaratorNode> getDeclaratorList(){
        return decls;
    }

    public MxType getType(){
        return type.getType();
    }

    @Override
    public <T> T accept(ASTBaseVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
