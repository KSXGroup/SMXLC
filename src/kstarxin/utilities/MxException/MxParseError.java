package kstarxin.utilities.MxException;

import kstarxin.utilities.Location;

public class MxParseError extends MxCompileException {
    public MxParseError(String err, Integer line, Integer column){
        super(err, new Location(line, column));
    }
}
