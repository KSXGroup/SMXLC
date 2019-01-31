grammar MxStar;
import MxStarLexerRule;
@header{package kstarxin.parser;}

program             :   declaration* EOF
                    ;

declaration         :   methodDeclaration
                    |   classDeclaration
                    |   variableDeclaration
                    |   SEMI
                    ;

variableDeclaration :   type variableDeclarationList SEMI
                    ;

variableDeclarationList
                    :
                        variableDeclarator (COMMA variableDeclarator?)*
                    ;

variableDeclarator  :   Identifier (ASSIGN expression)?
                    ;

type                :   nonArrayType
                    |   arrayType
                    ;

arrayType           :   nonArrayType (LBRAC RBRAC)+
                    ;

nonArrayType        :   userType
                    |   primitiveType
                    ;

userType            :   Identifier
                    ;

primitiveType       :   Boolean |   Integer |   String;

methodDeclaration   :   typeWithVoid Identifier parameterField LBRACE methodBody* RBRACE;

typeWithVoid        :   type |  Void;

parameterField      :   (LPAREN (parameterDeclaration (COMMA parameterDeclaration)*)? RPAREN);

parameterDeclaration:   type Identifier;

methodBody          :   statement;

statement           :   block
                    |   conditionStatement
                    |   loopStatement
                    |   jumpStatement
                    |   variableDeclaration
                    |   expression SEMI
                    |   SEMI
                    ;

block               :   LBRACE statement* RBRACE
                    ;

conditionStatement  :   If LPAREN expression RPAREN statement elseIfStatement* elseStatement?
                    ;

elseIfStatement     :   Else If LPAREN cond = expression RPAREN statement
                    ;

elseStatement       :   Else statement
                    ;

loopStatement       :   whileStatement
                    |   forStatement
                    ;

forStatement        :   normalForStatement
                    ;

whileStatement      :   While LPAREN expression RPAREN  statement
                    ;

normalForStatement  :   For LPAREN (init=expression)? SEMI (cond=expression)? SEMI (step=expression)? RPAREN statement
                    ;

jumpStatement       :   jumpType
                    ;

jumpType            :   Break SEMI                  #BreakJump
                    |   Continue SEMI               #ContinueJump
                    |   Return expression? SEMI     #ReturnJump
                    ;

classDeclaration    :   Class Identifier LBRACE classBodyMember* RBRACE
                    ;

classBodyMember     :   variableDeclaration
                    |   classConstructorDeclaration
                    |   classMemberFunctionDeclaration
                    ;

classMemberFunctionDeclaration
                    :   methodDeclaration
                    ;

classConstructorDeclaration
                    :   Identifier parameterField LBRACE constructorBody RBRACE
                    ;

constructorBody     :   statement*
                    ;

expressionList      :   expression (COMMA expression)*
                    ;

expression          :   expression op   =   (INC | DEC)                                     #SuffixIncDec
                    |   expression DOT Identifier                                           #DotMember
                    |   expression LPAREN expressionList? RPAREN                            #MethodCall
                    |   expression LBRAC expression RBRAC                                   #IndexAccess

                    |   <assoc=right>   op  =   (INC | DEC)     expression                  #UnaryExpression
                    |   <assoc=right>   op  =   (ADD | SUB)     expression                  #UnaryExpression
                    |   <assoc=right>   op  =   NOT             expression                  #UnaryExpression
                    |   <assoc=right>   op  =   BITNOT          expression                  #UnaryExpression
                    |   <assoc=right>   New     creator                                     #NewCreator

                    |   lhs = expression    op  =   (MUL | DIV | MOD) rhs = expression      #BinaryExpression
                    |   lhs = expression    op  =   (ADD | SUB )      rhs = expression      #BinaryExpression
                    |   lhs = expression    op  =   (SFTL| SFTR)      rhs = expression      #BinaryExpression
                    |   lhs = expression    op  =   (GT  | LT)        rhs = expression      #BinaryExpression
                    |   lhs = expression    op  =   (GE  | LE)        rhs = expression      #BinaryExpression
                    |   lhs = expression    op  =   (EQ  | NEQ)       rhs = expression      #BinaryExpression
                    |   lhs = expression    op  =   BITAND            rhs = expression      #BinaryExpression
                    |   lhs = expression    op  =   BITXOR            rhs = expression      #BinaryExpression
                    |   lhs = expression    op  =   BITOR             rhs = expression      #BinaryExpression
                    |   lhs = expression    op  =   AND               rhs = expression      #BinaryExpression
                    |   lhs = expression    op  =   OR                rhs = expression      #BinaryExpression

                    |   <assoc=right>       lhs =  expression ASSIGN  rhs = expression      #BinaryExpression

                    |   Identifier                                                          #IdentifierExpression
                    |   constant                                                            #ConstantExpression
                    |   This                                                                #ThisExpression
                    |   LPAREN  expression  RPAREN                                          #ParenthesisExpression
                    ;

constant            :   BoolConst
                    |   NullConst
                    |   IntegerConst
                    |   StringConst
                    ;

creator             :   nonArrayCreator
                    |   arrayCreator
                    ;

nonArrayCreator     :   nonArrayType (LPAREN expressionList RPAREN)?
                    ;

arrayCreator        :   nonArrayType arrayCreatorUnit+
                    ;

arrayCreatorUnit    :   LBRAC   expression?  RBRAC
                    ;
