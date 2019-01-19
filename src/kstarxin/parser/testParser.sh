#export LASSPATH=".:/usr/local/lib/antlr-4.7.2-complete.jar:$CLASSPATH"
antlr4='java -jar /usr/local/lib/antlr-4.7.2-complete.jar'
grun='java org.antlr.v4.gui.TestRig'
for((i=0;i<=274; i = i+1))do
	echo "run on test $i \n"
       $grun MxStar program  < /home/kstarxin/code/compiler/test/testcase_$i.txt	
done
