package kstarxin.compiler;

public class Main{
    public static void main(String [] arg){
        /*for(Integer i = 1; i <=  51; ++i) {
            try {
                Compiler compiler = new Compiler("/home/kstarxin/code/compiler/test/semantic/success/testcase_" + i.toString() + ".txt"); //88
                compiler.compileStart();
            }catch (Exception e){
                System.out.print("testcase_" + i.toString() + " failed\n");
                break;
            }
            System.out.print("testcase_" + i.toString() + " passed\n");
        }*/
        Compiler compiler = new Compiler("/home/kstarxin/code/compiler/test/semantic/success/my.txt"); //88
        compiler.compileStart();
    }
}
