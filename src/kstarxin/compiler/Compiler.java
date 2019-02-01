package kstarxin.compiler;

import java.io.*;
import kstarxin.parser.*;
import kstarxin.ast.*;
import org.antlr.v4.runtime.*;

public class Compiler{
    private MxStarParser parser;

    public Compiler(String f){
        try {
            FileInputStream in = new FileInputStream(f);
            CharStream ipt = CharStreams.fromStream(in);
            MxStarLexer lexer = new MxStarLexer(ipt);
            CommonTokenStream token = new CommonTokenStream(lexer);
            parser = new MxStarParser(token);
        } catch (IOException e){
            System.out.println(e);
        }
    }

    public void compileStart(){
        ASTBuilderVisitor builder = new ASTBuilderVisitor(parser.program());
        ProgramNode prog = builder.build();
        ASTPrinterVisitor printer = new ASTPrinterVisitor(prog, System.out);
        printer.display();
        prog.getCurrentSymbolTable().dumpSymbolTable("",System.out);
    }
 }
