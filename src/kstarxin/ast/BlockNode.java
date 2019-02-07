package kstarxin.ast;

import kstarxin.utilities.Location;
import kstarxin.utilities.Symbol;
import kstarxin.utilities.SymbolTable;

import java.util.LinkedList;

public class BlockNode extends StatementNode {
    LinkedList<Node> statements;
    BlockNode(SymbolTable stb, Location loc){
        super(stb ,loc);
        statements = new LinkedList<>();
        int p = 0;
    }

    public void addStatement(Node n){
        statements.add(n);
    }

    public LinkedList<Node> getStatementList(){
        return statements;
    }
    @Override
    public <T> T accept(ASTBaseVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
