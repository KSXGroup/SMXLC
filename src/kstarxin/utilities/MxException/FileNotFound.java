package kstarxin.utilities.MxException;

public class FileNotFound extends CompileException {
    public FileNotFound(String fileName){
        super(fileName + ": No such file or directory");
    }
}
