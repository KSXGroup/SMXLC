package kstarxin.ast;

import kstarxin.utilities.Location;
import kstarxin.utilities.Symbol;
import kstarxin.utilities.SymbolTable;

public class MethodDeclarationNode extends DeclarationNode {

    public MethodDeclarationNode(TypeNode _type, String id, SymbolTable stb, Location loc){
        super(stb, loc);
    }
    @Override
    public <T> T accept(ASTBaseVisitor<T> visitor) {
        return null;
    }
}
