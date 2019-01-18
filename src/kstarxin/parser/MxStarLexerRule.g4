lexer grammar MxStarLexerRule;

Boolean 	:	'bool';
Integer 	:	'int';
String 		:	'string';
Void		:	'void';
If 		    :	'if';
Else        :   'else';
For		    :	'for';
While		:	'while';
Break		:	'break';
Continue	:	'continue';
Return		:	'return';
New		    :	'new';
Class		:	'class';
This		:	'this';


CommentLine	:	'//'.*?'\n' 	->skip;

CommentBlock:	'/*'.*?'*/'	->skip;

Identifier	:	[a-zA-Z][0-9a-zA-Z_]*;

WhiteSpace	:	[ \t\n\r]+	->skip;

NewLine		:	'\r'?'\n'	->skip;

BoolConst	:	'true' | 'false';

NullConst	:	'null';

fragment	EscapeCharacter	:	'\\'["abfnrtv0\\'];
fragment 	StringCharacter	:	~["\\] | EscapeCharacter;

StringConst	:	'"'StringCharacter*'"';

fragment	Digit		    :	[0-9];
fragment    NonZeroDigit    :   [1-9];
fragment 	DecimalInteger	:	'-'?[1-9]Digit* | [0];

IntegerConst:	DecimalInteger;

ADD		    :	'+';
SUB		    :	'-';
MUL		    :	'*';
DIV         :   '/';
MOD		    :	'%';
GT		    :	'>';
GE          :   '>=';
LT		    :	'<';
LE          :   '<=';

EQ		    :	'==';
NEQ		    :	'!=';
NOT		    :	'!';

SFTL		:	'<<';
SFTR		:	'>>';
BITNOT		:	'~';
BITOR		:	'|';
BITXOR		:	'^';
BITAND		:	'&';
AND         :   '&&';
OR          :   '||';

ASSIGN		:	'=';

INC		    :	'++';
DEC		    :	'--';

DOT		    :	'.';

SEMI		:	';';
COMMA		:	',';

LBRAC		:	'[';
RBRAC		:	']';

LPAREN		:	'(';
RPAREN		:	')';

LBRACE		:	'{';
RBRACE		:	'}';





