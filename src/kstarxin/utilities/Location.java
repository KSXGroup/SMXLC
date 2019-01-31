package kstarxin.utilities;

import org.antlr.v4.runtime.ParserRuleContext;

public class Location {
    private final int LineNumber;
    private final int ColumnNumber;
    public Location(ParserRuleContext ctx){
        LineNumber = ctx.start.getLine();
        ColumnNumber = ctx.start.getCharPositionInLine();
    }
}
