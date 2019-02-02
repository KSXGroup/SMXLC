package kstarxin.ast;

import kstarxin.utilities.Location;
import kstarxin.utilities.Symbol;
import kstarxin.utilities.SymbolTable;

import java.util.LinkedList;

public class BlockNode extends Node {
    LinkedList<Node> statements;
    BlockNode(SymbolTable stb, Location loc){
        super(stb ,loc);
        statements = new LinkedList<>();
    }

    public void addStatement(Node n){
        statements.add(n);
    }

    public LinkedList<Node> getStatementList(){
        return statements;
    }
    @Override
    public <T> T accept(ASTBaseVisitor<T> visitor) {
        return null;
    }
}
