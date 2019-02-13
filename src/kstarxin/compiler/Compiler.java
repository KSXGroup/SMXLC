package kstarxin.compiler;

import java.io.*;
import kstarxin.parser.*;
import kstarxin.ast.*;
import kstarxin.utilities.MxException.*;
import org.antlr.v4.runtime.*;

public class Compiler {
    private MxStarParser parser;
    private String fileName;
    public Compiler(String f) {
        fileName = f;
        try {
            FileInputStream in = new FileInputStream(f);
            CharStream ipt = CharStreams.fromStream(in);
            MxStarLexer lexer = new MxStarLexer(ipt);
            CommonTokenStream token = new CommonTokenStream(lexer);
            parser = new MxStarParser(token);
            parser.removeErrorListeners();
            parser.addErrorListener(ParserErrorListener.INSTANCE);
        } catch (FileNotFoundException e) {
            throw new FileNotFound(f);
        } catch (IOException e) {
            throw new MxIOException();
        }
    }

    public void compileStart() {
        ASTBuilderVisitor builder = null;
        try {
            builder = new ASTBuilderVisitor(parser.program());
        } catch (Exception e) {
            throw new CompileException("Parser error" + e.toString());
        }
        ProgramNode prog = builder.build();
        ASTTypeCheckerVisitor typeChecker = new ASTTypeCheckerVisitor(prog);
        typeChecker.checkType();
        ASTPrinterVisitor printer = new ASTPrinterVisitor(prog, System.out);
        printer.display();
        prog.getCurrentSymbolTable().dumpSymbolTable("", System.out);
    }
}
