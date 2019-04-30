package kstarxin.compiler;

import java.io.*;

import kstarxin.ir.IRBuilderVisitor;
import kstarxin.ir.IRInterpreter;
import kstarxin.ir.IRPrinter;
import kstarxin.ir.IRProgram;
import kstarxin.ir.asmir.ASMLevelIRBuilder;
import kstarxin.ir.asmir.ASMLevelIRProgram;
import kstarxin.nasm.CodePrinter;
import kstarxin.nasm.allocator.GraphAllocator;
import kstarxin.nasm.allocator.NaiveAllocator;
import kstarxin.optimization.*;
import kstarxin.parser.*;
import kstarxin.ast.*;
import kstarxin.utilities.MxException.*;
import org.antlr.v4.runtime.*;

public class Compiler {
    private static final    String              compileTerminateInfo = "compliation terminated";
    private static final    String              irPrintPath = "/home/kstarxin/code/compiler/ir.txt";
    private static final    String              nasmPrintPath = "/home/kstarxin/Desktop/nasm.asm";
    private static final    String              nasmPrintPathAfter = "/home/kstarxin/nasmafter.asm";
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

    public void compileStart() throws Exception {
        ASTBuilderVisitor       builder         = null;
        ProgramNode             prog            = null;
        ASTTypeCheckerVisitor   typeChecker     = null;
        IRBuilderVisitor        irBuilder       = null;
        IRProgram               ir              = null;
        IRPrinter               irPrinter       = null;
        PrintStream             irOutputStream  = null;
        PrintStream             nasmOutputStream= null;
        PrintStream             nasmOutputStreamAfter= null;
        IRInterpreter           irIntererter    = null;
        ASMLevelIRBuilder       asmIrBuilder    = null;
        ASMLevelIRProgram       asmIr           = null;
        NaiveAllocator          naiveAllocator  = null;
        GraphAllocator          graphAllocator  = null;
        CodePrinter             codePrinter     = null;

        irOutputStream = new PrintStream(new File(irPrintPath));
        nasmOutputStream = new PrintStream(new File(nasmPrintPath));
        nasmOutputStreamAfter = new PrintStream(new File(nasmPrintPathAfter));

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


        irBuilder = new IRBuilderVisitor(prog);
        ir = irBuilder.buildIR();

        Optimizer optimizer = new Optimizer(ir);
        optimizer.run();

        /*irPrinter = new IRPrinter(ir, irOutputStream);
        irPrinter.printIR();*/

        /*irIntererter = new IRInterpreter(irPrintPath);
        irIntererter.runIR();*/

        asmIrBuilder = new ASMLevelIRBuilder(ir);
        asmIr = asmIrBuilder.build();

        /*naiveAllocator = new NaiveAllocator(asmIr);
        naiveAllocator.run();*/

        /*codePrinter = new CodePrinter(asmIr,nasmOutputStream);
        codePrinter.printCode();*/

        graphAllocator = new GraphAllocator(asmIr);
        graphAllocator.run();

        codePrinter = new CodePrinter(asmIr,nasmOutputStreamAfter);
        codePrinter.printCode();

        return;
    }
}
