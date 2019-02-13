package kstarxin.utilities;

public class CompileException extends RuntimeException{
    public CompileException(String err){
        super(err);
    }
    public CompileException(String err, Location loc){
        super(err + " at "+loc.getLineNumberString()+ ":"+loc.getColumnNumberString());
    }
}
