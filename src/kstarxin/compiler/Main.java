package kstarxin.compiler;

public class Main{
    public static void main(String [] arg){
       Compiler compiler =  new Compiler("/home/kstarxin/code/compiler/classes/production/compiler/kstarxin/compiler/program.txt");
       compiler.compileStart();
    }
}
