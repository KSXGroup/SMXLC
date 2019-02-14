package kstarxin.compiler;

import java.io.*;
import kstarxin.parser.*;
import kstarxin.ast.*;
import kstarxin.utilities.MxException.*;
import org.antlr.v4.runtime.*;

public class Compiler {
    private MxStarParser parser;
    private String fileName;
    private MxErrorProcessor errorProcessor;
    public Compiler(String f) {
        fileName = f;
        try {
            FileInputStream in = new FileInputStream(f);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            ParserErrorListener pel = ParserErrorListener.INSTANCE;
            CharStream ipt = CharStreams.fromStream(in);
            MxStarLexer lexer = new MxStarLexer(ipt);
            CommonTokenStream token = new CommonTokenStream(lexer);
            parser = new MxStarParser(token);
            parser.removeErrorListeners();
            parser.addErrorListener(pel);
            errorProcessor = new MxErrorProcessor(fileName, System.out,pel);
        } catch (FileNotFoundException e) {
            errorProcessor.add(new FileNotFound(f));
        } catch (IOException e) {
            errorProcessor.add(new MxIOException());
        }
    }

    public void compileStart() {
        if(errorProcessor.size() > 0) {
            errorProcessor.printError();
            throw new MxCompileException("compilation terminated");
        }

        ASTBuilderVisitor builder = null;

        builder = new ASTBuilderVisitor(parser.program(), errorProcessor);
        if (errorProcessor.size() > 0) {
            errorProcessor.printErrorWithReference();
            throw new MxCompileException("compilation terminated");
        }

        ProgramNode prog = null;

        try {
            prog = builder.build();
        }catch (Exception e){}
        if (errorProcessor.size() > 0) {
            errorProcessor.printErrorWithReference();
            throw new MxCompileException("compilation terminated");
        }

        ASTTypeCheckerVisitor typeChecker = null;

        try {
            typeChecker = new ASTTypeCheckerVisitor(prog, errorProcessor);
            typeChecker.checkType();
        }catch (Exception e){}
        if(errorProcessor.size() > 0){
            errorProcessor.printErrorWithReference();
            throw new MxCompileException("compilation terminated");
        }


        //ASTPrinterVisitor printer = new ASTPrinterVisitor(prog, System.out);
        //printer.display();
        //prog.getCurrentSymbolTable().dumpSymbolTable("", System.out);
    }
}
