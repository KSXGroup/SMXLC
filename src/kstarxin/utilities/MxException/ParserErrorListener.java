package kstarxin.utilities.MxException;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import java.util.LinkedList;

public class ParserErrorListener extends BaseErrorListener {
    public static final ParserErrorListener INSTANCE = new ParserErrorListener();
    private LinkedList<MxParseError> parseError = new LinkedList<MxParseError>();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e){
        parseError.add(new MxParseError(msg, line, charPositionInLine));
    }

    public LinkedList<MxParseError> getParseError() {
        return parseError;
    }
}
