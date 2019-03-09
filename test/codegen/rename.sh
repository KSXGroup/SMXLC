#!/bin/bash
c=0
for((i=0;i<300;++i))
do
if [ -f "codegentestcase_$i.txt" ];
then
	let "c=c+1"
	mv codegentestcase_$i.txt testcase_$c.txt
fi
done
