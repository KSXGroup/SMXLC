package kstarxin.utilities.MxException;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class MxErrorProcessor {
    private String fileName;
    private BufferedReader inputFile;
    private PrintStream errorPrintStream;
    private ParserErrorListener errorListener;
    private LinkedList<MxCompileException> errorList;
    private MxErrorPrinter errorPrinter;
    private ArrayList<String> lines;

    private String getReference(MxCompileException err){
        String  s = lines.get(err.getLocation().getLineNumber()-1);
        return s;
    }

    public MxErrorProcessor(String _fileName, PrintStream _errorPrintStream, ParserErrorListener _listener){
        errorList = new LinkedList<MxCompileException>();
        lines = new ArrayList<String>();
        fileName = _fileName;
        errorListener = _listener;
        errorPrintStream = _errorPrintStream;
        errorPrinter = new MxErrorPrinter(errorPrintStream);

        String tmpl;
        try {
            inputFile = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
        } catch (FileNotFoundException e){
            return;
        }//DO NOTHING

        while(true) {
            try {
                tmpl = inputFile.readLine();
                if(tmpl == null) break;
                lines.add(tmpl);
            } catch (IOException e) {
                break;
            }
        }

    }

    public void printError(){
        errorList.forEach(err -> errorPrinter.print(fileName, err));
    }

    public void printErrorWithReference(){
        for(MxCompileException e: errorListener.getParseError()){
            String rf = getReference(e);
            errorPrinter.printWithReference(fileName, e, rf);
        }
       for(MxCompileException e: errorList){
           String rf = getReference(e);
           errorPrinter.printWithReference(fileName, e, rf);
       }
    }

    public void add(MxCompileException e){
        errorList.add(e);
    }

    public int size(){
        return errorList.size() + errorListener.getParseError().size();
    }

}
