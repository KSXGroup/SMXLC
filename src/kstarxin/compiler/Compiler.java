package kstarxin.compiler;

import java.io.*;
import java.util.Scanner;

import kstarxin.ir.IRBuilderVisitor;
import kstarxin.ir.IRInterpreter;
import kstarxin.ir.IRPrinter;
import kstarxin.ir.IRProgram;
import kstarxin.parser.*;
import kstarxin.ast.*;
import kstarxin.utilities.MxException.*;
import org.antlr.v4.runtime.*;

public class Compiler {
    private static final    String              compileTerminateInfo = "compliation terminated";
    private static final    String              irPrintPath = "/home/kstarxin/code/compiler/ir.txt";
    private                 MxStarParser        parser;
    private                 String              fileName;
    private                 MxErrorProcessor    errorProcessor;

    public Compiler(String f) {
        fileName = f;
        ParserErrorListener pel = ParserErrorListener.INSTANCE;
        errorProcessor = new MxErrorProcessor(fileName, System.err ,pel);
        try {
            FileInputStream     in      = new FileInputStream(f);
            BufferedReader      reader  = new BufferedReader(new InputStreamReader(in));
            CharStream          ipt     = CharStreams.fromStream(in);
            MxStarLexer         lexer   = new MxStarLexer(ipt);
            CommonTokenStream   token   = new CommonTokenStream(lexer);
            parser                      = new MxStarParser(token);

            parser.removeErrorListeners();
            parser.addErrorListener(pel);

        } catch (FileNotFoundException e) {
            errorProcessor.add(new FileNotFound(f));
        } catch (IOException e) {
            errorProcessor.add(new MxIOException());
        }
    }

    public void compileStart(boolean buildIR, boolean ifPrintIRtoFile, boolean runIR) throws Exception {
        ASTBuilderVisitor       builder         = null;
        ProgramNode             prog            = null;
        ASTTypeCheckerVisitor   typeChecker     = null;
        IRBuilderVisitor        irBuilder       = null;
        IRProgram               ir              = null;
        IRPrinter               irPrinter       = null;
        PrintStream             irOutputStream  = null;
        IRInterpreter           irIntererter    = null;

        if(ifPrintIRtoFile){
            irOutputStream = new PrintStream(new File(irPrintPath));
        } else irOutputStream = System.out;
        if(errorProcessor.size() > 0) {
            errorProcessor.printError();
            throw new MxCompileException(compileTerminateInfo);
        }

        builder = new ASTBuilderVisitor(parser.program(), errorProcessor);
        if (errorProcessor.size() > 0) {
            errorProcessor.printErrorWithReference();
            throw new MxCompileException(compileTerminateInfo);
        }


        try {
            prog = builder.build();
        }catch (Exception e){
            throw new MxCompileException(e.getMessage() + "\n" + compileTerminateInfo);
        }

        if (errorProcessor.size() > 0) {
            errorProcessor.printErrorWithReference();
            throw new MxCompileException(compileTerminateInfo);
        }

        try {
            typeChecker = new ASTTypeCheckerVisitor(prog, errorProcessor);
            typeChecker.checkType();
        }catch (Exception e){
            throw e;
        }

        if(errorProcessor.size() > 0){
            errorProcessor.printErrorWithReference();
            throw new MxCompileException(compileTerminateInfo);
        }

        if(Configure.PRINT_AST_SYMBOLTABLE) {
            ASTPrinterVisitor printer = new ASTPrinterVisitor(prog, System.out);
            printer.display();
            prog.getCurrentSymbolTable().dumpSymbolTable("", System.out);
        }

        if(buildIR) {
            irBuilder = new IRBuilderVisitor(prog);
            ir = irBuilder.buildIR();
            irPrinter = new IRPrinter(ir, irOutputStream);
            irPrinter.printIR();
        }
        if(runIR) {
            irIntererter = new IRInterpreter(irPrintPath);
            irIntererter.runIR();
        }
        return;
    }
}
