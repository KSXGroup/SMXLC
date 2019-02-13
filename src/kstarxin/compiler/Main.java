package kstarxin.compiler;

public class Main{
    public static void main(String [] arg){
       Compiler compiler =  new Compiler("/home/kstarxin/code/compiler/test/semantic/success/my.txt"); //88
       // Compiler compiler =  new Compiler("/home/kstarxin/code/compiler/test/semantic/failure/testcase_207.txt"); //TODO:check 207
       //Compiler compiler =  new Compiler("/home/kstarxin/code/compiler/mytestcase/mainAndClass.mx");
       compiler.compileStart();
    }
}
