package kstarxin.utilities.MxException;

import kstarxin.utilities.Location;

abstract public class MxSemanticCheckError extends CompileException {
    public MxSemanticCheckError(String err, Location loc){
        super(err, loc);
    }
}
