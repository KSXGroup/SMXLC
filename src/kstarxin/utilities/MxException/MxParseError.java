package kstarxin.utilities.MxException;

import kstarxin.utilities.Location;

public class MxParseError extends CompileException {
    public MxParseError(String err, Integer line, Integer column){
        super(err, new Location(line, column));
    }
}
