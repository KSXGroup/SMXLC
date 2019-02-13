package kstarxin.utilities.MxException;

import kstarxin.utilities.Location;

public class CompileException extends RuntimeException{
    private boolean hasLocation;
    public CompileException(String err){
        super(err);
        hasLocation = false;
    }
    public CompileException(String err, Location loc){
        super(err + " at "+loc.getLineNumberString()+ ":"+loc.getColumnNumberString());
        hasLocation = true;
    }
}
