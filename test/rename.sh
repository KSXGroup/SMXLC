#!/bin/bash
c=0
for((i=500;i<999;++i))
do
if [ -f "testcase_$i.txt" ];
then
	let "c=c+1"
	mv testcase_$i.txt testcase_$c.txt
fi
done
