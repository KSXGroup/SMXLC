package kstarxin.compiler;

public class Semantic {
    public static void main(String[] args) throws Exception{
        Compiler compiler = new Compiler("program.txt"); //88
        compiler.compileStart(false, false,false);
    }
}
