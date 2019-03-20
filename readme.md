## Make clear everything before start to code! ##
2019.1.31: can identify some variable

2019.2.12: passed all semantic test, but found bugs, design new test case and reconstruct semantic part

2019.2.13 bug fixed, senmantic passed, start elaborate error report

2019.2.14 error report elaborate finished

2019.2.28 roughly finish IRBuilder

2019.3.3 can print some simple ir

2019.3.9 can print all ir of codegn test
ir rules:

	1.return memory and virtual register when build ir

	2.memory operand is allowed in arithmetic instruction

	3.method call parameters is vreg or immediate

	4.only dot member and index access return memory operand

start working on ir interpreter

2019.3.20 rougly finish ir interpreter and can run some testcase
