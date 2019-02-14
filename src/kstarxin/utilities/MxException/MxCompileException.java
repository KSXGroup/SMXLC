package kstarxin.utilities.MxException;

import kstarxin.utilities.Location;

public class MxCompileException extends RuntimeException{
    private boolean hasLocation;
    private Location loc;
    public MxCompileException(String err){
        super(err);
        hasLocation = false;
    }
    public MxCompileException(String err, Location _loc){
        super(err);
        loc  = _loc;
        hasLocation = true;
    }

    public Location getLocation() {
        return loc;
    }
}
