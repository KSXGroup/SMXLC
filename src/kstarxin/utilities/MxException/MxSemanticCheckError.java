package kstarxin.utilities.MxException;

import kstarxin.utilities.Location;

public class MxSemanticCheckError extends MxCompileException {
    public MxSemanticCheckError(String err, Location loc){
        super(err, loc);
    }
}
