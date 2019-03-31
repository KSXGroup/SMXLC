package kstarxin.compiler;

public class Main{
    public static void main(String [] arg) throws Exception{
       /* for(Integer i = 1; i <=  83; ++i) {
            try {
                Compiler compiler = new Compiler("/home/kstarxin/code/compiler/test/semantic/failure/testcase_" + i.toString() + ".txt"); //88
                compiler.compileStart(false, false, false);
            }catch (Exception e){
                System.out.print("testcase_" + i.toString() + " failed\n");
                continue;
            }
            System.out.print("testcase_" + i.toString() + " passed\n");
            break;
        }*/
        /*for(Integer i = 1; i <=  72; ++i) {
            try {
                Compiler compiler = new Compiler("/home/kstarxin/code/compiler/test/codegen/testcase_" + i.toString() + ".txt"); //88
                compiler.compileStart(false);
            }catch (Exception e){
                System.out.println(e + "\n\n\n\nfailed on test" + i.toString());
                System.exit(0);
            }catch (StackOverflowError e){
                System.out.println(e + "\n\n\n\nfailed on test" + i.toString());
                System.exit(0);
            }
        }*/

        Compiler compiler = new Compiler("/home/kstarxin/code/compiler/test/codegen/my.txt"); //88
        compiler.compileStart();
    }
}
