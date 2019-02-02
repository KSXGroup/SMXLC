package kstarxin.ast;

import kstarxin.parser.*;
import kstarxin.utilities.Location;
import kstarxin.utilities.SymbolTable;

public abstract class Node {
    private SymbolTable currentSymbolTable;
    private Location    currentLocation;

    public Node( SymbolTable stb, Location loc){
        currentLocation = loc;
        currentSymbolTable = stb;
    }

    public SymbolTable getCurrentSymbolTable(){
        return currentSymbolTable;
    }

    public void setCurrentSymbolTable(SymbolTable stb){
        currentSymbolTable = stb;
    }

    public Location getLocation(){
        return currentLocation;
    }

    public abstract <T> T accept(ASTBaseVisitor<T> visitor);
}
