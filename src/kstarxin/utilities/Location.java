package kstarxin.utilities;

import org.antlr.v4.runtime.ParserRuleContext;

public class Location {
    private final int LineNumber;
    private final int ColumnNumber;

    public Location(int x, int y){
        LineNumber = x;
        ColumnNumber = y;
    }

    public Location(ParserRuleContext ctx){
        LineNumber = ctx.start.getLine();
        ColumnNumber = ctx.start.getCharPositionInLine();
    }

    public int getLineNumber(){
        return LineNumber;
    }

    public int getColumnNumber(){
        return ColumnNumber;
    }

    public String getLineNumberString(){
        Integer l = LineNumber;
        return l.toString();
    }

    public String getColumnNumberString(){
        Integer c = ColumnNumber;
        return c.toString();
    }

    public boolean before(Location other){
        if(LineNumber > other.LineNumber) return false;
        else if(LineNumber == other.LineNumber && ColumnNumber > other.ColumnNumber) return false;
        return true;
    }
}
