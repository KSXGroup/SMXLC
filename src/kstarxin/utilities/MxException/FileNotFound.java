package kstarxin.utilities.MxException;

public class FileNotFound extends MxCompileException {
    public FileNotFound(String fileName){
        super(fileName + ": No such file or directory");
    }
}
