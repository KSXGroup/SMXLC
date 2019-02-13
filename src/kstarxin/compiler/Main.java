package kstarxin.compiler;

import kstarxin.parser.MxStarParser;
import kstarxin.utilities.CompileException;

public class Main{
    public static void main(String [] arg){
        Compiler compiler =  new Compiler("/home/kstarxin/code/compiler/test/semantic/failure/testcase_11.txt"); //88
        compiler.compileStart();
    }
}
