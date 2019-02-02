// Generated from MxStar.g4 by ANTLR 4.7.2
package kstarxin.parser;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MxStarParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		Boolean=1, Integer=2, String=3, Void=4, If=5, Else=6, For=7, While=8, 
		Break=9, Continue=10, Return=11, New=12, Class=13, This=14, CommentLine=15, 
		CommentBlock=16, WhiteSpace=17, NewLine=18, Identifier=19, BoolConst=20, 
		NullConst=21, StringConst=22, IntegerConst=23, ADD=24, SUB=25, MUL=26, 
		DIV=27, MOD=28, GT=29, GE=30, LT=31, LE=32, EQ=33, NEQ=34, NOT=35, SFTL=36, 
		SFTR=37, BITNOT=38, BITOR=39, BITXOR=40, BITAND=41, AND=42, OR=43, ASSIGN=44, 
		INC=45, DEC=46, DOT=47, SEMI=48, COMMA=49, LBRAC=50, RBRAC=51, LPAREN=52, 
		RPAREN=53, LBRACE=54, RBRACE=55;
	public static final int
		RULE_program = 0, RULE_declaration = 1, RULE_variableDeclaration = 2, 
		RULE_variableDeclarationList = 3, RULE_variableDeclarator = 4, RULE_type = 5, 
		RULE_arrayType = 6, RULE_nonArrayType = 7, RULE_userType = 8, RULE_primitiveType = 9, 
		RULE_methodDeclaration = 10, RULE_typeWithVoid = 11, RULE_parameterField = 12, 
		RULE_parameterDeclaration = 13, RULE_statement = 14, RULE_block = 15, 
		RULE_conditionStatement = 16, RULE_elseIfStatement = 17, RULE_elseStatement = 18, 
		RULE_loopStatement = 19, RULE_forStatement = 20, RULE_whileStatement = 21, 
		RULE_normalForStatement = 22, RULE_jumpStatement = 23, RULE_jumpType = 24, 
		RULE_classDeclaration = 25, RULE_classBodyMember = 26, RULE_classMemberFunctionDeclaration = 27, 
		RULE_classConstructorDeclaration = 28, RULE_expressionList = 29, RULE_expression = 30, 
		RULE_constant = 31, RULE_creator = 32, RULE_nonArrayCreator = 33, RULE_arrayCreator = 34, 
		RULE_arrayCreatorUnit = 35;
	private static String[] makeRuleNames() {
		return new String[] {
			"program", "declaration", "variableDeclaration", "variableDeclarationList", 
			"variableDeclarator", "type", "arrayType", "nonArrayType", "userType", 
			"primitiveType", "methodDeclaration", "typeWithVoid", "parameterField", 
			"parameterDeclaration", "statement", "block", "conditionStatement", "elseIfStatement", 
			"elseStatement", "loopStatement", "forStatement", "whileStatement", "normalForStatement", 
			"jumpStatement", "jumpType", "classDeclaration", "classBodyMember", "classMemberFunctionDeclaration", 
			"classConstructorDeclaration", "expressionList", "expression", "constant", 
			"creator", "nonArrayCreator", "arrayCreator", "arrayCreatorUnit"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'bool'", "'int'", "'string'", "'void'", "'if'", "'else'", "'for'", 
			"'while'", "'break'", "'continue'", "'return'", "'new'", "'class'", "'this'", 
			null, null, null, null, null, null, "'null'", null, null, "'+'", "'-'", 
			"'*'", "'/'", "'%'", "'>'", "'>='", "'<'", "'<='", "'=='", "'!='", "'!'", 
			"'<<'", "'>>'", "'~'", "'|'", "'^'", "'&'", "'&&'", "'||'", "'='", "'++'", 
			"'--'", "'.'", "';'", "','", "'['", "']'", "'('", "')'", "'{'", "'}'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "Boolean", "Integer", "String", "Void", "If", "Else", "For", "While", 
			"Break", "Continue", "Return", "New", "Class", "This", "CommentLine", 
			"CommentBlock", "WhiteSpace", "NewLine", "Identifier", "BoolConst", "NullConst", 
			"StringConst", "IntegerConst", "ADD", "SUB", "MUL", "DIV", "MOD", "GT", 
			"GE", "LT", "LE", "EQ", "NEQ", "NOT", "SFTL", "SFTR", "BITNOT", "BITOR", 
			"BITXOR", "BITAND", "AND", "OR", "ASSIGN", "INC", "DEC", "DOT", "SEMI", 
			"COMMA", "LBRAC", "RBRAC", "LPAREN", "RPAREN", "LBRACE", "RBRACE"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "MxStar.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public MxStarParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class ProgramContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(MxStarParser.EOF, 0); }
		public List<DeclarationContext> declaration() {
			return getRuleContexts(DeclarationContext.class);
		}
		public DeclarationContext declaration(int i) {
			return getRuleContext(DeclarationContext.class,i);
		}
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitProgram(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_program);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(75);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Boolean) | (1L << Integer) | (1L << String) | (1L << Void) | (1L << Class) | (1L << Identifier) | (1L << SEMI))) != 0)) {
				{
				{
				setState(72);
				declaration();
				}
				}
				setState(77);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(78);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DeclarationContext extends ParserRuleContext {
		public MethodDeclarationContext methodDeclaration() {
			return getRuleContext(MethodDeclarationContext.class,0);
		}
		public ClassDeclarationContext classDeclaration() {
			return getRuleContext(ClassDeclarationContext.class,0);
		}
		public VariableDeclarationContext variableDeclaration() {
			return getRuleContext(VariableDeclarationContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(MxStarParser.SEMI, 0); }
		public DeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeclarationContext declaration() throws RecognitionException {
		DeclarationContext _localctx = new DeclarationContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_declaration);
		try {
			setState(84);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(80);
				methodDeclaration();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(81);
				classDeclaration();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(82);
				variableDeclaration();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(83);
				match(SEMI);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VariableDeclarationContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public VariableDeclarationListContext variableDeclarationList() {
			return getRuleContext(VariableDeclarationListContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(MxStarParser.SEMI, 0); }
		public VariableDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableDeclaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitVariableDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableDeclarationContext variableDeclaration() throws RecognitionException {
		VariableDeclarationContext _localctx = new VariableDeclarationContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_variableDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(86);
			type();
			setState(87);
			variableDeclarationList();
			setState(88);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VariableDeclarationListContext extends ParserRuleContext {
		public List<VariableDeclaratorContext> variableDeclarator() {
			return getRuleContexts(VariableDeclaratorContext.class);
		}
		public VariableDeclaratorContext variableDeclarator(int i) {
			return getRuleContext(VariableDeclaratorContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(MxStarParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(MxStarParser.COMMA, i);
		}
		public VariableDeclarationListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableDeclarationList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitVariableDeclarationList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableDeclarationListContext variableDeclarationList() throws RecognitionException {
		VariableDeclarationListContext _localctx = new VariableDeclarationListContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_variableDeclarationList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(90);
			variableDeclarator();
			setState(97);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(91);
				match(COMMA);
				setState(93);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Identifier) {
					{
					setState(92);
					variableDeclarator();
					}
				}

				}
				}
				setState(99);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VariableDeclaratorContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(MxStarParser.Identifier, 0); }
		public TerminalNode ASSIGN() { return getToken(MxStarParser.ASSIGN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public VariableDeclaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableDeclarator; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitVariableDeclarator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableDeclaratorContext variableDeclarator() throws RecognitionException {
		VariableDeclaratorContext _localctx = new VariableDeclaratorContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_variableDeclarator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(100);
			match(Identifier);
			setState(103);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASSIGN) {
				{
				setState(101);
				match(ASSIGN);
				setState(102);
				expression(0);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeContext extends ParserRuleContext {
		public NonArrayTypeContext nonArrayType() {
			return getRuleContext(NonArrayTypeContext.class,0);
		}
		public ArrayTypeContext arrayType() {
			return getRuleContext(ArrayTypeContext.class,0);
		}
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeContext type() throws RecognitionException {
		TypeContext _localctx = new TypeContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_type);
		try {
			setState(107);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(105);
				nonArrayType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(106);
				arrayType();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArrayTypeContext extends ParserRuleContext {
		public NonArrayTypeContext nonArrayType() {
			return getRuleContext(NonArrayTypeContext.class,0);
		}
		public List<TerminalNode> LBRAC() { return getTokens(MxStarParser.LBRAC); }
		public TerminalNode LBRAC(int i) {
			return getToken(MxStarParser.LBRAC, i);
		}
		public List<TerminalNode> RBRAC() { return getTokens(MxStarParser.RBRAC); }
		public TerminalNode RBRAC(int i) {
			return getToken(MxStarParser.RBRAC, i);
		}
		public ArrayTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitArrayType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayTypeContext arrayType() throws RecognitionException {
		ArrayTypeContext _localctx = new ArrayTypeContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_arrayType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(109);
			nonArrayType();
			setState(112); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(110);
				match(LBRAC);
				setState(111);
				match(RBRAC);
				}
				}
				setState(114); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==LBRAC );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NonArrayTypeContext extends ParserRuleContext {
		public UserTypeContext userType() {
			return getRuleContext(UserTypeContext.class,0);
		}
		public PrimitiveTypeContext primitiveType() {
			return getRuleContext(PrimitiveTypeContext.class,0);
		}
		public NonArrayTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonArrayType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitNonArrayType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NonArrayTypeContext nonArrayType() throws RecognitionException {
		NonArrayTypeContext _localctx = new NonArrayTypeContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_nonArrayType);
		try {
			setState(118);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Identifier:
				enterOuterAlt(_localctx, 1);
				{
				setState(116);
				userType();
				}
				break;
			case Boolean:
			case Integer:
			case String:
				enterOuterAlt(_localctx, 2);
				{
				setState(117);
				primitiveType();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UserTypeContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(MxStarParser.Identifier, 0); }
		public UserTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_userType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitUserType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UserTypeContext userType() throws RecognitionException {
		UserTypeContext _localctx = new UserTypeContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_userType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(120);
			match(Identifier);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PrimitiveTypeContext extends ParserRuleContext {
		public TerminalNode Boolean() { return getToken(MxStarParser.Boolean, 0); }
		public TerminalNode Integer() { return getToken(MxStarParser.Integer, 0); }
		public TerminalNode String() { return getToken(MxStarParser.String, 0); }
		public PrimitiveTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primitiveType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitPrimitiveType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimitiveTypeContext primitiveType() throws RecognitionException {
		PrimitiveTypeContext _localctx = new PrimitiveTypeContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_primitiveType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(122);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Boolean) | (1L << Integer) | (1L << String))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MethodDeclarationContext extends ParserRuleContext {
		public TypeWithVoidContext typeWithVoid() {
			return getRuleContext(TypeWithVoidContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(MxStarParser.Identifier, 0); }
		public ParameterFieldContext parameterField() {
			return getRuleContext(ParameterFieldContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public MethodDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_methodDeclaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitMethodDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MethodDeclarationContext methodDeclaration() throws RecognitionException {
		MethodDeclarationContext _localctx = new MethodDeclarationContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_methodDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(124);
			typeWithVoid();
			setState(125);
			match(Identifier);
			setState(126);
			parameterField();
			setState(127);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeWithVoidContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode Void() { return getToken(MxStarParser.Void, 0); }
		public TypeWithVoidContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeWithVoid; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitTypeWithVoid(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeWithVoidContext typeWithVoid() throws RecognitionException {
		TypeWithVoidContext _localctx = new TypeWithVoidContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_typeWithVoid);
		try {
			setState(131);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Boolean:
			case Integer:
			case String:
			case Identifier:
				enterOuterAlt(_localctx, 1);
				{
				setState(129);
				type();
				}
				break;
			case Void:
				enterOuterAlt(_localctx, 2);
				{
				setState(130);
				match(Void);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParameterFieldContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(MxStarParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(MxStarParser.RPAREN, 0); }
		public List<ParameterDeclarationContext> parameterDeclaration() {
			return getRuleContexts(ParameterDeclarationContext.class);
		}
		public ParameterDeclarationContext parameterDeclaration(int i) {
			return getRuleContext(ParameterDeclarationContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(MxStarParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(MxStarParser.COMMA, i);
		}
		public ParameterFieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameterField; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitParameterField(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParameterFieldContext parameterField() throws RecognitionException {
		ParameterFieldContext _localctx = new ParameterFieldContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_parameterField);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(133);
			match(LPAREN);
			setState(142);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Boolean) | (1L << Integer) | (1L << String) | (1L << Identifier))) != 0)) {
				{
				setState(134);
				parameterDeclaration();
				setState(139);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(135);
					match(COMMA);
					setState(136);
					parameterDeclaration();
					}
					}
					setState(141);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(144);
			match(RPAREN);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParameterDeclarationContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(MxStarParser.Identifier, 0); }
		public ParameterDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameterDeclaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitParameterDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParameterDeclarationContext parameterDeclaration() throws RecognitionException {
		ParameterDeclarationContext _localctx = new ParameterDeclarationContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_parameterDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(146);
			type();
			setState(147);
			match(Identifier);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StatementContext extends ParserRuleContext {
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public ConditionStatementContext conditionStatement() {
			return getRuleContext(ConditionStatementContext.class,0);
		}
		public LoopStatementContext loopStatement() {
			return getRuleContext(LoopStatementContext.class,0);
		}
		public JumpStatementContext jumpStatement() {
			return getRuleContext(JumpStatementContext.class,0);
		}
		public VariableDeclarationContext variableDeclaration() {
			return getRuleContext(VariableDeclarationContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(MxStarParser.SEMI, 0); }
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_statement);
		try {
			setState(158);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(149);
				block();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(150);
				conditionStatement();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(151);
				loopStatement();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(152);
				jumpStatement();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(153);
				variableDeclaration();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(154);
				expression(0);
				setState(155);
				match(SEMI);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(157);
				match(SEMI);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BlockContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(MxStarParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(MxStarParser.RBRACE, 0); }
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public BlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockContext block() throws RecognitionException {
		BlockContext _localctx = new BlockContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(160);
			match(LBRACE);
			setState(164);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Boolean) | (1L << Integer) | (1L << String) | (1L << If) | (1L << For) | (1L << While) | (1L << Break) | (1L << Continue) | (1L << Return) | (1L << New) | (1L << This) | (1L << Identifier) | (1L << BoolConst) | (1L << NullConst) | (1L << StringConst) | (1L << IntegerConst) | (1L << ADD) | (1L << SUB) | (1L << NOT) | (1L << BITNOT) | (1L << INC) | (1L << DEC) | (1L << SEMI) | (1L << LPAREN) | (1L << LBRACE))) != 0)) {
				{
				{
				setState(161);
				statement();
				}
				}
				setState(166);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(167);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConditionStatementContext extends ParserRuleContext {
		public TerminalNode If() { return getToken(MxStarParser.If, 0); }
		public TerminalNode LPAREN() { return getToken(MxStarParser.LPAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(MxStarParser.RPAREN, 0); }
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public List<ElseIfStatementContext> elseIfStatement() {
			return getRuleContexts(ElseIfStatementContext.class);
		}
		public ElseIfStatementContext elseIfStatement(int i) {
			return getRuleContext(ElseIfStatementContext.class,i);
		}
		public ElseStatementContext elseStatement() {
			return getRuleContext(ElseStatementContext.class,0);
		}
		public ConditionStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conditionStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitConditionStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConditionStatementContext conditionStatement() throws RecognitionException {
		ConditionStatementContext _localctx = new ConditionStatementContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_conditionStatement);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(169);
			match(If);
			setState(170);
			match(LPAREN);
			setState(171);
			expression(0);
			setState(172);
			match(RPAREN);
			setState(173);
			statement();
			setState(177);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(174);
					elseIfStatement();
					}
					} 
				}
				setState(179);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
			}
			setState(181);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				{
				setState(180);
				elseStatement();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ElseIfStatementContext extends ParserRuleContext {
		public ExpressionContext cond;
		public TerminalNode Else() { return getToken(MxStarParser.Else, 0); }
		public TerminalNode If() { return getToken(MxStarParser.If, 0); }
		public TerminalNode LPAREN() { return getToken(MxStarParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(MxStarParser.RPAREN, 0); }
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ElseIfStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elseIfStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitElseIfStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElseIfStatementContext elseIfStatement() throws RecognitionException {
		ElseIfStatementContext _localctx = new ElseIfStatementContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_elseIfStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(183);
			match(Else);
			setState(184);
			match(If);
			setState(185);
			match(LPAREN);
			setState(186);
			((ElseIfStatementContext)_localctx).cond = expression(0);
			setState(187);
			match(RPAREN);
			setState(188);
			statement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ElseStatementContext extends ParserRuleContext {
		public TerminalNode Else() { return getToken(MxStarParser.Else, 0); }
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public ElseStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elseStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitElseStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElseStatementContext elseStatement() throws RecognitionException {
		ElseStatementContext _localctx = new ElseStatementContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_elseStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(190);
			match(Else);
			setState(191);
			statement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LoopStatementContext extends ParserRuleContext {
		public WhileStatementContext whileStatement() {
			return getRuleContext(WhileStatementContext.class,0);
		}
		public ForStatementContext forStatement() {
			return getRuleContext(ForStatementContext.class,0);
		}
		public LoopStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_loopStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitLoopStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LoopStatementContext loopStatement() throws RecognitionException {
		LoopStatementContext _localctx = new LoopStatementContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_loopStatement);
		try {
			setState(195);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case While:
				enterOuterAlt(_localctx, 1);
				{
				setState(193);
				whileStatement();
				}
				break;
			case For:
				enterOuterAlt(_localctx, 2);
				{
				setState(194);
				forStatement();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ForStatementContext extends ParserRuleContext {
		public NormalForStatementContext normalForStatement() {
			return getRuleContext(NormalForStatementContext.class,0);
		}
		public ForStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitForStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForStatementContext forStatement() throws RecognitionException {
		ForStatementContext _localctx = new ForStatementContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_forStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(197);
			normalForStatement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class WhileStatementContext extends ParserRuleContext {
		public TerminalNode While() { return getToken(MxStarParser.While, 0); }
		public TerminalNode LPAREN() { return getToken(MxStarParser.LPAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(MxStarParser.RPAREN, 0); }
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public WhileStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whileStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitWhileStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhileStatementContext whileStatement() throws RecognitionException {
		WhileStatementContext _localctx = new WhileStatementContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_whileStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(199);
			match(While);
			setState(200);
			match(LPAREN);
			setState(201);
			expression(0);
			setState(202);
			match(RPAREN);
			setState(203);
			statement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NormalForStatementContext extends ParserRuleContext {
		public ExpressionContext init;
		public ExpressionContext cond;
		public ExpressionContext step;
		public TerminalNode For() { return getToken(MxStarParser.For, 0); }
		public TerminalNode LPAREN() { return getToken(MxStarParser.LPAREN, 0); }
		public List<TerminalNode> SEMI() { return getTokens(MxStarParser.SEMI); }
		public TerminalNode SEMI(int i) {
			return getToken(MxStarParser.SEMI, i);
		}
		public TerminalNode RPAREN() { return getToken(MxStarParser.RPAREN, 0); }
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public NormalForStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_normalForStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitNormalForStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NormalForStatementContext normalForStatement() throws RecognitionException {
		NormalForStatementContext _localctx = new NormalForStatementContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_normalForStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(205);
			match(For);
			setState(206);
			match(LPAREN);
			setState(208);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << New) | (1L << This) | (1L << Identifier) | (1L << BoolConst) | (1L << NullConst) | (1L << StringConst) | (1L << IntegerConst) | (1L << ADD) | (1L << SUB) | (1L << NOT) | (1L << BITNOT) | (1L << INC) | (1L << DEC) | (1L << LPAREN))) != 0)) {
				{
				setState(207);
				((NormalForStatementContext)_localctx).init = expression(0);
				}
			}

			setState(210);
			match(SEMI);
			setState(212);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << New) | (1L << This) | (1L << Identifier) | (1L << BoolConst) | (1L << NullConst) | (1L << StringConst) | (1L << IntegerConst) | (1L << ADD) | (1L << SUB) | (1L << NOT) | (1L << BITNOT) | (1L << INC) | (1L << DEC) | (1L << LPAREN))) != 0)) {
				{
				setState(211);
				((NormalForStatementContext)_localctx).cond = expression(0);
				}
			}

			setState(214);
			match(SEMI);
			setState(216);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << New) | (1L << This) | (1L << Identifier) | (1L << BoolConst) | (1L << NullConst) | (1L << StringConst) | (1L << IntegerConst) | (1L << ADD) | (1L << SUB) | (1L << NOT) | (1L << BITNOT) | (1L << INC) | (1L << DEC) | (1L << LPAREN))) != 0)) {
				{
				setState(215);
				((NormalForStatementContext)_localctx).step = expression(0);
				}
			}

			setState(218);
			match(RPAREN);
			setState(219);
			statement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class JumpStatementContext extends ParserRuleContext {
		public JumpTypeContext jumpType() {
			return getRuleContext(JumpTypeContext.class,0);
		}
		public JumpStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jumpStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitJumpStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JumpStatementContext jumpStatement() throws RecognitionException {
		JumpStatementContext _localctx = new JumpStatementContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_jumpStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(221);
			jumpType();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class JumpTypeContext extends ParserRuleContext {
		public JumpTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jumpType; }
	 
		public JumpTypeContext() { }
		public void copyFrom(JumpTypeContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class BreakJumpContext extends JumpTypeContext {
		public TerminalNode Break() { return getToken(MxStarParser.Break, 0); }
		public TerminalNode SEMI() { return getToken(MxStarParser.SEMI, 0); }
		public BreakJumpContext(JumpTypeContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitBreakJump(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ContinueJumpContext extends JumpTypeContext {
		public TerminalNode Continue() { return getToken(MxStarParser.Continue, 0); }
		public TerminalNode SEMI() { return getToken(MxStarParser.SEMI, 0); }
		public ContinueJumpContext(JumpTypeContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitContinueJump(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ReturnJumpContext extends JumpTypeContext {
		public TerminalNode Return() { return getToken(MxStarParser.Return, 0); }
		public TerminalNode SEMI() { return getToken(MxStarParser.SEMI, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ReturnJumpContext(JumpTypeContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitReturnJump(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JumpTypeContext jumpType() throws RecognitionException {
		JumpTypeContext _localctx = new JumpTypeContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_jumpType);
		int _la;
		try {
			setState(232);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Break:
				_localctx = new BreakJumpContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(223);
				match(Break);
				setState(224);
				match(SEMI);
				}
				break;
			case Continue:
				_localctx = new ContinueJumpContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(225);
				match(Continue);
				setState(226);
				match(SEMI);
				}
				break;
			case Return:
				_localctx = new ReturnJumpContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(227);
				match(Return);
				setState(229);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << New) | (1L << This) | (1L << Identifier) | (1L << BoolConst) | (1L << NullConst) | (1L << StringConst) | (1L << IntegerConst) | (1L << ADD) | (1L << SUB) | (1L << NOT) | (1L << BITNOT) | (1L << INC) | (1L << DEC) | (1L << LPAREN))) != 0)) {
					{
					setState(228);
					expression(0);
					}
				}

				setState(231);
				match(SEMI);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassDeclarationContext extends ParserRuleContext {
		public TerminalNode Class() { return getToken(MxStarParser.Class, 0); }
		public TerminalNode Identifier() { return getToken(MxStarParser.Identifier, 0); }
		public TerminalNode LBRACE() { return getToken(MxStarParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(MxStarParser.RBRACE, 0); }
		public List<ClassBodyMemberContext> classBodyMember() {
			return getRuleContexts(ClassBodyMemberContext.class);
		}
		public ClassBodyMemberContext classBodyMember(int i) {
			return getRuleContext(ClassBodyMemberContext.class,i);
		}
		public ClassDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classDeclaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitClassDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassDeclarationContext classDeclaration() throws RecognitionException {
		ClassDeclarationContext _localctx = new ClassDeclarationContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_classDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(234);
			match(Class);
			setState(235);
			match(Identifier);
			setState(236);
			match(LBRACE);
			setState(240);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Boolean) | (1L << Integer) | (1L << String) | (1L << Void) | (1L << Identifier))) != 0)) {
				{
				{
				setState(237);
				classBodyMember();
				}
				}
				setState(242);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(243);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassBodyMemberContext extends ParserRuleContext {
		public VariableDeclarationContext variableDeclaration() {
			return getRuleContext(VariableDeclarationContext.class,0);
		}
		public ClassConstructorDeclarationContext classConstructorDeclaration() {
			return getRuleContext(ClassConstructorDeclarationContext.class,0);
		}
		public ClassMemberFunctionDeclarationContext classMemberFunctionDeclaration() {
			return getRuleContext(ClassMemberFunctionDeclarationContext.class,0);
		}
		public ClassBodyMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classBodyMember; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitClassBodyMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassBodyMemberContext classBodyMember() throws RecognitionException {
		ClassBodyMemberContext _localctx = new ClassBodyMemberContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_classBodyMember);
		try {
			setState(248);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(245);
				variableDeclaration();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(246);
				classConstructorDeclaration();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(247);
				classMemberFunctionDeclaration();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassMemberFunctionDeclarationContext extends ParserRuleContext {
		public MethodDeclarationContext methodDeclaration() {
			return getRuleContext(MethodDeclarationContext.class,0);
		}
		public ClassMemberFunctionDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classMemberFunctionDeclaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitClassMemberFunctionDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassMemberFunctionDeclarationContext classMemberFunctionDeclaration() throws RecognitionException {
		ClassMemberFunctionDeclarationContext _localctx = new ClassMemberFunctionDeclarationContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_classMemberFunctionDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(250);
			methodDeclaration();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassConstructorDeclarationContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(MxStarParser.Identifier, 0); }
		public ParameterFieldContext parameterField() {
			return getRuleContext(ParameterFieldContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public ClassConstructorDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classConstructorDeclaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitClassConstructorDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassConstructorDeclarationContext classConstructorDeclaration() throws RecognitionException {
		ClassConstructorDeclarationContext _localctx = new ClassConstructorDeclarationContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_classConstructorDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(252);
			match(Identifier);
			setState(253);
			parameterField();
			setState(254);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressionListContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(MxStarParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(MxStarParser.COMMA, i);
		}
		public ExpressionListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expressionList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitExpressionList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionListContext expressionList() throws RecognitionException {
		ExpressionListContext _localctx = new ExpressionListContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_expressionList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(256);
			expression(0);
			setState(261);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(257);
				match(COMMA);
				setState(258);
				expression(0);
				}
				}
				setState(263);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressionContext extends ParserRuleContext {
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
	 
		public ExpressionContext() { }
		public void copyFrom(ExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ThisExpressionContext extends ExpressionContext {
		public TerminalNode This() { return getToken(MxStarParser.This, 0); }
		public ThisExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitThisExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BinaryExpressionContext extends ExpressionContext {
		public ExpressionContext lhs;
		public Token op;
		public ExpressionContext rhs;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode MUL() { return getToken(MxStarParser.MUL, 0); }
		public TerminalNode DIV() { return getToken(MxStarParser.DIV, 0); }
		public TerminalNode MOD() { return getToken(MxStarParser.MOD, 0); }
		public TerminalNode ADD() { return getToken(MxStarParser.ADD, 0); }
		public TerminalNode SUB() { return getToken(MxStarParser.SUB, 0); }
		public TerminalNode SFTL() { return getToken(MxStarParser.SFTL, 0); }
		public TerminalNode SFTR() { return getToken(MxStarParser.SFTR, 0); }
		public TerminalNode GT() { return getToken(MxStarParser.GT, 0); }
		public TerminalNode LT() { return getToken(MxStarParser.LT, 0); }
		public TerminalNode GE() { return getToken(MxStarParser.GE, 0); }
		public TerminalNode LE() { return getToken(MxStarParser.LE, 0); }
		public TerminalNode EQ() { return getToken(MxStarParser.EQ, 0); }
		public TerminalNode NEQ() { return getToken(MxStarParser.NEQ, 0); }
		public TerminalNode BITAND() { return getToken(MxStarParser.BITAND, 0); }
		public TerminalNode BITXOR() { return getToken(MxStarParser.BITXOR, 0); }
		public TerminalNode BITOR() { return getToken(MxStarParser.BITOR, 0); }
		public TerminalNode AND() { return getToken(MxStarParser.AND, 0); }
		public TerminalNode OR() { return getToken(MxStarParser.OR, 0); }
		public TerminalNode ASSIGN() { return getToken(MxStarParser.ASSIGN, 0); }
		public BinaryExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitBinaryExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ParenthesisExpressionContext extends ExpressionContext {
		public TerminalNode LPAREN() { return getToken(MxStarParser.LPAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(MxStarParser.RPAREN, 0); }
		public ParenthesisExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitParenthesisExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IndexAccessContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode LBRAC() { return getToken(MxStarParser.LBRAC, 0); }
		public TerminalNode RBRAC() { return getToken(MxStarParser.RBRAC, 0); }
		public IndexAccessContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitIndexAccess(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ConstantExpressionContext extends ExpressionContext {
		public ConstantContext constant() {
			return getRuleContext(ConstantContext.class,0);
		}
		public ConstantExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitConstantExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DotMemberContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode DOT() { return getToken(MxStarParser.DOT, 0); }
		public TerminalNode Identifier() { return getToken(MxStarParser.Identifier, 0); }
		public DotMemberContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitDotMember(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class UnaryExpressionContext extends ExpressionContext {
		public Token op;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode INC() { return getToken(MxStarParser.INC, 0); }
		public TerminalNode DEC() { return getToken(MxStarParser.DEC, 0); }
		public TerminalNode ADD() { return getToken(MxStarParser.ADD, 0); }
		public TerminalNode SUB() { return getToken(MxStarParser.SUB, 0); }
		public TerminalNode NOT() { return getToken(MxStarParser.NOT, 0); }
		public TerminalNode BITNOT() { return getToken(MxStarParser.BITNOT, 0); }
		public UnaryExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitUnaryExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MethodCallContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(MxStarParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(MxStarParser.RPAREN, 0); }
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public MethodCallContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitMethodCall(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NewCreatorContext extends ExpressionContext {
		public TerminalNode New() { return getToken(MxStarParser.New, 0); }
		public CreatorContext creator() {
			return getRuleContext(CreatorContext.class,0);
		}
		public NewCreatorContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitNewCreator(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IdentifierExpressionContext extends ExpressionContext {
		public TerminalNode Identifier() { return getToken(MxStarParser.Identifier, 0); }
		public IdentifierExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitIdentifierExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SuffixIncDecContext extends ExpressionContext {
		public Token op;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode INC() { return getToken(MxStarParser.INC, 0); }
		public TerminalNode DEC() { return getToken(MxStarParser.DEC, 0); }
		public SuffixIncDecContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitSuffixIncDec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 60;
		enterRecursionRule(_localctx, 60, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(282);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INC:
			case DEC:
				{
				_localctx = new UnaryExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(265);
				((UnaryExpressionContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==INC || _la==DEC) ) {
					((UnaryExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(266);
				expression(21);
				}
				break;
			case ADD:
			case SUB:
				{
				_localctx = new UnaryExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(267);
				((UnaryExpressionContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==ADD || _la==SUB) ) {
					((UnaryExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(268);
				expression(20);
				}
				break;
			case NOT:
				{
				_localctx = new UnaryExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(269);
				((UnaryExpressionContext)_localctx).op = match(NOT);
				setState(270);
				expression(19);
				}
				break;
			case BITNOT:
				{
				_localctx = new UnaryExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(271);
				((UnaryExpressionContext)_localctx).op = match(BITNOT);
				setState(272);
				expression(18);
				}
				break;
			case New:
				{
				_localctx = new NewCreatorContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(273);
				match(New);
				setState(274);
				creator();
				}
				break;
			case Identifier:
				{
				_localctx = new IdentifierExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(275);
				match(Identifier);
				}
				break;
			case BoolConst:
			case NullConst:
			case StringConst:
			case IntegerConst:
				{
				_localctx = new ConstantExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(276);
				constant();
				}
				break;
			case This:
				{
				_localctx = new ThisExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(277);
				match(This);
				}
				break;
			case LPAREN:
				{
				_localctx = new ParenthesisExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(278);
				match(LPAREN);
				setState(279);
				expression(0);
				setState(280);
				match(RPAREN);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(338);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,27,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(336);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
					case 1:
						{
						_localctx = new BinaryExpressionContext(new ExpressionContext(_parentctx, _parentState));
						((BinaryExpressionContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(284);
						if (!(precpred(_ctx, 16))) throw new FailedPredicateException(this, "precpred(_ctx, 16)");
						setState(285);
						((BinaryExpressionContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << MUL) | (1L << DIV) | (1L << MOD))) != 0)) ) {
							((BinaryExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(286);
						((BinaryExpressionContext)_localctx).rhs = expression(17);
						}
						break;
					case 2:
						{
						_localctx = new BinaryExpressionContext(new ExpressionContext(_parentctx, _parentState));
						((BinaryExpressionContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(287);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(288);
						((BinaryExpressionContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==ADD || _la==SUB) ) {
							((BinaryExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(289);
						((BinaryExpressionContext)_localctx).rhs = expression(16);
						}
						break;
					case 3:
						{
						_localctx = new BinaryExpressionContext(new ExpressionContext(_parentctx, _parentState));
						((BinaryExpressionContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(290);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(291);
						((BinaryExpressionContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==SFTL || _la==SFTR) ) {
							((BinaryExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(292);
						((BinaryExpressionContext)_localctx).rhs = expression(15);
						}
						break;
					case 4:
						{
						_localctx = new BinaryExpressionContext(new ExpressionContext(_parentctx, _parentState));
						((BinaryExpressionContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(293);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(294);
						((BinaryExpressionContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==GT || _la==LT) ) {
							((BinaryExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(295);
						((BinaryExpressionContext)_localctx).rhs = expression(14);
						}
						break;
					case 5:
						{
						_localctx = new BinaryExpressionContext(new ExpressionContext(_parentctx, _parentState));
						((BinaryExpressionContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(296);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(297);
						((BinaryExpressionContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==GE || _la==LE) ) {
							((BinaryExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(298);
						((BinaryExpressionContext)_localctx).rhs = expression(13);
						}
						break;
					case 6:
						{
						_localctx = new BinaryExpressionContext(new ExpressionContext(_parentctx, _parentState));
						((BinaryExpressionContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(299);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(300);
						((BinaryExpressionContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==EQ || _la==NEQ) ) {
							((BinaryExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(301);
						((BinaryExpressionContext)_localctx).rhs = expression(12);
						}
						break;
					case 7:
						{
						_localctx = new BinaryExpressionContext(new ExpressionContext(_parentctx, _parentState));
						((BinaryExpressionContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(302);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(303);
						((BinaryExpressionContext)_localctx).op = match(BITAND);
						setState(304);
						((BinaryExpressionContext)_localctx).rhs = expression(11);
						}
						break;
					case 8:
						{
						_localctx = new BinaryExpressionContext(new ExpressionContext(_parentctx, _parentState));
						((BinaryExpressionContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(305);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(306);
						((BinaryExpressionContext)_localctx).op = match(BITXOR);
						setState(307);
						((BinaryExpressionContext)_localctx).rhs = expression(10);
						}
						break;
					case 9:
						{
						_localctx = new BinaryExpressionContext(new ExpressionContext(_parentctx, _parentState));
						((BinaryExpressionContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(308);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(309);
						((BinaryExpressionContext)_localctx).op = match(BITOR);
						setState(310);
						((BinaryExpressionContext)_localctx).rhs = expression(9);
						}
						break;
					case 10:
						{
						_localctx = new BinaryExpressionContext(new ExpressionContext(_parentctx, _parentState));
						((BinaryExpressionContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(311);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(312);
						((BinaryExpressionContext)_localctx).op = match(AND);
						setState(313);
						((BinaryExpressionContext)_localctx).rhs = expression(8);
						}
						break;
					case 11:
						{
						_localctx = new BinaryExpressionContext(new ExpressionContext(_parentctx, _parentState));
						((BinaryExpressionContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(314);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(315);
						((BinaryExpressionContext)_localctx).op = match(OR);
						setState(316);
						((BinaryExpressionContext)_localctx).rhs = expression(7);
						}
						break;
					case 12:
						{
						_localctx = new BinaryExpressionContext(new ExpressionContext(_parentctx, _parentState));
						((BinaryExpressionContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(317);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(318);
						match(ASSIGN);
						setState(319);
						((BinaryExpressionContext)_localctx).rhs = expression(5);
						}
						break;
					case 13:
						{
						_localctx = new SuffixIncDecContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(320);
						if (!(precpred(_ctx, 25))) throw new FailedPredicateException(this, "precpred(_ctx, 25)");
						setState(321);
						((SuffixIncDecContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==INC || _la==DEC) ) {
							((SuffixIncDecContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						}
						break;
					case 14:
						{
						_localctx = new DotMemberContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(322);
						if (!(precpred(_ctx, 24))) throw new FailedPredicateException(this, "precpred(_ctx, 24)");
						setState(323);
						match(DOT);
						setState(324);
						match(Identifier);
						}
						break;
					case 15:
						{
						_localctx = new MethodCallContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(325);
						if (!(precpred(_ctx, 23))) throw new FailedPredicateException(this, "precpred(_ctx, 23)");
						setState(326);
						match(LPAREN);
						setState(328);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << New) | (1L << This) | (1L << Identifier) | (1L << BoolConst) | (1L << NullConst) | (1L << StringConst) | (1L << IntegerConst) | (1L << ADD) | (1L << SUB) | (1L << NOT) | (1L << BITNOT) | (1L << INC) | (1L << DEC) | (1L << LPAREN))) != 0)) {
							{
							setState(327);
							expressionList();
							}
						}

						setState(330);
						match(RPAREN);
						}
						break;
					case 16:
						{
						_localctx = new IndexAccessContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(331);
						if (!(precpred(_ctx, 22))) throw new FailedPredicateException(this, "precpred(_ctx, 22)");
						setState(332);
						match(LBRAC);
						setState(333);
						expression(0);
						setState(334);
						match(RBRAC);
						}
						break;
					}
					} 
				}
				setState(340);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,27,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class ConstantContext extends ParserRuleContext {
		public TerminalNode BoolConst() { return getToken(MxStarParser.BoolConst, 0); }
		public TerminalNode NullConst() { return getToken(MxStarParser.NullConst, 0); }
		public TerminalNode IntegerConst() { return getToken(MxStarParser.IntegerConst, 0); }
		public TerminalNode StringConst() { return getToken(MxStarParser.StringConst, 0); }
		public ConstantContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constant; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitConstant(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstantContext constant() throws RecognitionException {
		ConstantContext _localctx = new ConstantContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_constant);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(341);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BoolConst) | (1L << NullConst) | (1L << StringConst) | (1L << IntegerConst))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CreatorContext extends ParserRuleContext {
		public NonArrayCreatorContext nonArrayCreator() {
			return getRuleContext(NonArrayCreatorContext.class,0);
		}
		public ArrayCreatorContext arrayCreator() {
			return getRuleContext(ArrayCreatorContext.class,0);
		}
		public CreatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_creator; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitCreator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreatorContext creator() throws RecognitionException {
		CreatorContext _localctx = new CreatorContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_creator);
		try {
			setState(345);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,28,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(343);
				nonArrayCreator();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(344);
				arrayCreator();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NonArrayCreatorContext extends ParserRuleContext {
		public NonArrayTypeContext nonArrayType() {
			return getRuleContext(NonArrayTypeContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(MxStarParser.LPAREN, 0); }
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(MxStarParser.RPAREN, 0); }
		public NonArrayCreatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonArrayCreator; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitNonArrayCreator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NonArrayCreatorContext nonArrayCreator() throws RecognitionException {
		NonArrayCreatorContext _localctx = new NonArrayCreatorContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_nonArrayCreator);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(347);
			nonArrayType();
			setState(352);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
			case 1:
				{
				setState(348);
				match(LPAREN);
				setState(349);
				expressionList();
				setState(350);
				match(RPAREN);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArrayCreatorContext extends ParserRuleContext {
		public NonArrayTypeContext nonArrayType() {
			return getRuleContext(NonArrayTypeContext.class,0);
		}
		public List<ArrayCreatorUnitContext> arrayCreatorUnit() {
			return getRuleContexts(ArrayCreatorUnitContext.class);
		}
		public ArrayCreatorUnitContext arrayCreatorUnit(int i) {
			return getRuleContext(ArrayCreatorUnitContext.class,i);
		}
		public ArrayCreatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayCreator; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitArrayCreator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayCreatorContext arrayCreator() throws RecognitionException {
		ArrayCreatorContext _localctx = new ArrayCreatorContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_arrayCreator);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(354);
			nonArrayType();
			setState(356); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(355);
					arrayCreatorUnit();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(358); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,30,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArrayCreatorUnitContext extends ParserRuleContext {
		public TerminalNode LBRAC() { return getToken(MxStarParser.LBRAC, 0); }
		public TerminalNode RBRAC() { return getToken(MxStarParser.RBRAC, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ArrayCreatorUnitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayCreatorUnit; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxStarVisitor ) return ((MxStarVisitor<? extends T>)visitor).visitArrayCreatorUnit(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayCreatorUnitContext arrayCreatorUnit() throws RecognitionException {
		ArrayCreatorUnitContext _localctx = new ArrayCreatorUnitContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_arrayCreatorUnit);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(360);
			match(LBRAC);
			setState(362);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << New) | (1L << This) | (1L << Identifier) | (1L << BoolConst) | (1L << NullConst) | (1L << StringConst) | (1L << IntegerConst) | (1L << ADD) | (1L << SUB) | (1L << NOT) | (1L << BITNOT) | (1L << INC) | (1L << DEC) | (1L << LPAREN))) != 0)) {
				{
				setState(361);
				expression(0);
				}
			}

			setState(364);
			match(RBRAC);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 30:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 16);
		case 1:
			return precpred(_ctx, 15);
		case 2:
			return precpred(_ctx, 14);
		case 3:
			return precpred(_ctx, 13);
		case 4:
			return precpred(_ctx, 12);
		case 5:
			return precpred(_ctx, 11);
		case 6:
			return precpred(_ctx, 10);
		case 7:
			return precpred(_ctx, 9);
		case 8:
			return precpred(_ctx, 8);
		case 9:
			return precpred(_ctx, 7);
		case 10:
			return precpred(_ctx, 6);
		case 11:
			return precpred(_ctx, 5);
		case 12:
			return precpred(_ctx, 25);
		case 13:
			return precpred(_ctx, 24);
		case 14:
			return precpred(_ctx, 23);
		case 15:
			return precpred(_ctx, 22);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\39\u0171\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\3\2\7\2L\n\2\f\2\16\2O\13\2\3\2\3\2\3\3"+
		"\3\3\3\3\3\3\5\3W\n\3\3\4\3\4\3\4\3\4\3\5\3\5\3\5\5\5`\n\5\7\5b\n\5\f"+
		"\5\16\5e\13\5\3\6\3\6\3\6\5\6j\n\6\3\7\3\7\5\7n\n\7\3\b\3\b\3\b\6\bs\n"+
		"\b\r\b\16\bt\3\t\3\t\5\ty\n\t\3\n\3\n\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3"+
		"\r\3\r\5\r\u0086\n\r\3\16\3\16\3\16\3\16\7\16\u008c\n\16\f\16\16\16\u008f"+
		"\13\16\5\16\u0091\n\16\3\16\3\16\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3"+
		"\20\3\20\3\20\3\20\3\20\5\20\u00a1\n\20\3\21\3\21\7\21\u00a5\n\21\f\21"+
		"\16\21\u00a8\13\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\7\22\u00b2"+
		"\n\22\f\22\16\22\u00b5\13\22\3\22\5\22\u00b8\n\22\3\23\3\23\3\23\3\23"+
		"\3\23\3\23\3\23\3\24\3\24\3\24\3\25\3\25\5\25\u00c6\n\25\3\26\3\26\3\27"+
		"\3\27\3\27\3\27\3\27\3\27\3\30\3\30\3\30\5\30\u00d3\n\30\3\30\3\30\5\30"+
		"\u00d7\n\30\3\30\3\30\5\30\u00db\n\30\3\30\3\30\3\30\3\31\3\31\3\32\3"+
		"\32\3\32\3\32\3\32\3\32\5\32\u00e8\n\32\3\32\5\32\u00eb\n\32\3\33\3\33"+
		"\3\33\3\33\7\33\u00f1\n\33\f\33\16\33\u00f4\13\33\3\33\3\33\3\34\3\34"+
		"\3\34\5\34\u00fb\n\34\3\35\3\35\3\36\3\36\3\36\3\36\3\37\3\37\3\37\7\37"+
		"\u0106\n\37\f\37\16\37\u0109\13\37\3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3"+
		" \3 \3 \3 \3 \3 \3 \5 \u011d\n \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3"+
		" \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3"+
		" \3 \3 \3 \3 \3 \3 \3 \3 \5 \u014b\n \3 \3 \3 \3 \3 \3 \7 \u0153\n \f"+
		" \16 \u0156\13 \3!\3!\3\"\3\"\5\"\u015c\n\"\3#\3#\3#\3#\3#\5#\u0163\n"+
		"#\3$\3$\6$\u0167\n$\r$\16$\u0168\3%\3%\5%\u016d\n%\3%\3%\3%\2\3>&\2\4"+
		"\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFH\2\13"+
		"\3\2\3\5\3\2/\60\3\2\32\33\3\2\34\36\3\2&\'\4\2\37\37!!\4\2  \"\"\3\2"+
		"#$\3\2\26\31\2\u018a\2M\3\2\2\2\4V\3\2\2\2\6X\3\2\2\2\b\\\3\2\2\2\nf\3"+
		"\2\2\2\fm\3\2\2\2\16o\3\2\2\2\20x\3\2\2\2\22z\3\2\2\2\24|\3\2\2\2\26~"+
		"\3\2\2\2\30\u0085\3\2\2\2\32\u0087\3\2\2\2\34\u0094\3\2\2\2\36\u00a0\3"+
		"\2\2\2 \u00a2\3\2\2\2\"\u00ab\3\2\2\2$\u00b9\3\2\2\2&\u00c0\3\2\2\2(\u00c5"+
		"\3\2\2\2*\u00c7\3\2\2\2,\u00c9\3\2\2\2.\u00cf\3\2\2\2\60\u00df\3\2\2\2"+
		"\62\u00ea\3\2\2\2\64\u00ec\3\2\2\2\66\u00fa\3\2\2\28\u00fc\3\2\2\2:\u00fe"+
		"\3\2\2\2<\u0102\3\2\2\2>\u011c\3\2\2\2@\u0157\3\2\2\2B\u015b\3\2\2\2D"+
		"\u015d\3\2\2\2F\u0164\3\2\2\2H\u016a\3\2\2\2JL\5\4\3\2KJ\3\2\2\2LO\3\2"+
		"\2\2MK\3\2\2\2MN\3\2\2\2NP\3\2\2\2OM\3\2\2\2PQ\7\2\2\3Q\3\3\2\2\2RW\5"+
		"\26\f\2SW\5\64\33\2TW\5\6\4\2UW\7\62\2\2VR\3\2\2\2VS\3\2\2\2VT\3\2\2\2"+
		"VU\3\2\2\2W\5\3\2\2\2XY\5\f\7\2YZ\5\b\5\2Z[\7\62\2\2[\7\3\2\2\2\\c\5\n"+
		"\6\2]_\7\63\2\2^`\5\n\6\2_^\3\2\2\2_`\3\2\2\2`b\3\2\2\2a]\3\2\2\2be\3"+
		"\2\2\2ca\3\2\2\2cd\3\2\2\2d\t\3\2\2\2ec\3\2\2\2fi\7\25\2\2gh\7.\2\2hj"+
		"\5> \2ig\3\2\2\2ij\3\2\2\2j\13\3\2\2\2kn\5\20\t\2ln\5\16\b\2mk\3\2\2\2"+
		"ml\3\2\2\2n\r\3\2\2\2or\5\20\t\2pq\7\64\2\2qs\7\65\2\2rp\3\2\2\2st\3\2"+
		"\2\2tr\3\2\2\2tu\3\2\2\2u\17\3\2\2\2vy\5\22\n\2wy\5\24\13\2xv\3\2\2\2"+
		"xw\3\2\2\2y\21\3\2\2\2z{\7\25\2\2{\23\3\2\2\2|}\t\2\2\2}\25\3\2\2\2~\177"+
		"\5\30\r\2\177\u0080\7\25\2\2\u0080\u0081\5\32\16\2\u0081\u0082\5 \21\2"+
		"\u0082\27\3\2\2\2\u0083\u0086\5\f\7\2\u0084\u0086\7\6\2\2\u0085\u0083"+
		"\3\2\2\2\u0085\u0084\3\2\2\2\u0086\31\3\2\2\2\u0087\u0090\7\66\2\2\u0088"+
		"\u008d\5\34\17\2\u0089\u008a\7\63\2\2\u008a\u008c\5\34\17\2\u008b\u0089"+
		"\3\2\2\2\u008c\u008f\3\2\2\2\u008d\u008b\3\2\2\2\u008d\u008e\3\2\2\2\u008e"+
		"\u0091\3\2\2\2\u008f\u008d\3\2\2\2\u0090\u0088\3\2\2\2\u0090\u0091\3\2"+
		"\2\2\u0091\u0092\3\2\2\2\u0092\u0093\7\67\2\2\u0093\33\3\2\2\2\u0094\u0095"+
		"\5\f\7\2\u0095\u0096\7\25\2\2\u0096\35\3\2\2\2\u0097\u00a1\5 \21\2\u0098"+
		"\u00a1\5\"\22\2\u0099\u00a1\5(\25\2\u009a\u00a1\5\60\31\2\u009b\u00a1"+
		"\5\6\4\2\u009c\u009d\5> \2\u009d\u009e\7\62\2\2\u009e\u00a1\3\2\2\2\u009f"+
		"\u00a1\7\62\2\2\u00a0\u0097\3\2\2\2\u00a0\u0098\3\2\2\2\u00a0\u0099\3"+
		"\2\2\2\u00a0\u009a\3\2\2\2\u00a0\u009b\3\2\2\2\u00a0\u009c\3\2\2\2\u00a0"+
		"\u009f\3\2\2\2\u00a1\37\3\2\2\2\u00a2\u00a6\78\2\2\u00a3\u00a5\5\36\20"+
		"\2\u00a4\u00a3\3\2\2\2\u00a5\u00a8\3\2\2\2\u00a6\u00a4\3\2\2\2\u00a6\u00a7"+
		"\3\2\2\2\u00a7\u00a9\3\2\2\2\u00a8\u00a6\3\2\2\2\u00a9\u00aa\79\2\2\u00aa"+
		"!\3\2\2\2\u00ab\u00ac\7\7\2\2\u00ac\u00ad\7\66\2\2\u00ad\u00ae\5> \2\u00ae"+
		"\u00af\7\67\2\2\u00af\u00b3\5\36\20\2\u00b0\u00b2\5$\23\2\u00b1\u00b0"+
		"\3\2\2\2\u00b2\u00b5\3\2\2\2\u00b3\u00b1\3\2\2\2\u00b3\u00b4\3\2\2\2\u00b4"+
		"\u00b7\3\2\2\2\u00b5\u00b3\3\2\2\2\u00b6\u00b8\5&\24\2\u00b7\u00b6\3\2"+
		"\2\2\u00b7\u00b8\3\2\2\2\u00b8#\3\2\2\2\u00b9\u00ba\7\b\2\2\u00ba\u00bb"+
		"\7\7\2\2\u00bb\u00bc\7\66\2\2\u00bc\u00bd\5> \2\u00bd\u00be\7\67\2\2\u00be"+
		"\u00bf\5\36\20\2\u00bf%\3\2\2\2\u00c0\u00c1\7\b\2\2\u00c1\u00c2\5\36\20"+
		"\2\u00c2\'\3\2\2\2\u00c3\u00c6\5,\27\2\u00c4\u00c6\5*\26\2\u00c5\u00c3"+
		"\3\2\2\2\u00c5\u00c4\3\2\2\2\u00c6)\3\2\2\2\u00c7\u00c8\5.\30\2\u00c8"+
		"+\3\2\2\2\u00c9\u00ca\7\n\2\2\u00ca\u00cb\7\66\2\2\u00cb\u00cc\5> \2\u00cc"+
		"\u00cd\7\67\2\2\u00cd\u00ce\5\36\20\2\u00ce-\3\2\2\2\u00cf\u00d0\7\t\2"+
		"\2\u00d0\u00d2\7\66\2\2\u00d1\u00d3\5> \2\u00d2\u00d1\3\2\2\2\u00d2\u00d3"+
		"\3\2\2\2\u00d3\u00d4\3\2\2\2\u00d4\u00d6\7\62\2\2\u00d5\u00d7\5> \2\u00d6"+
		"\u00d5\3\2\2\2\u00d6\u00d7\3\2\2\2\u00d7\u00d8\3\2\2\2\u00d8\u00da\7\62"+
		"\2\2\u00d9\u00db\5> \2\u00da\u00d9\3\2\2\2\u00da\u00db\3\2\2\2\u00db\u00dc"+
		"\3\2\2\2\u00dc\u00dd\7\67\2\2\u00dd\u00de\5\36\20\2\u00de/\3\2\2\2\u00df"+
		"\u00e0\5\62\32\2\u00e0\61\3\2\2\2\u00e1\u00e2\7\13\2\2\u00e2\u00eb\7\62"+
		"\2\2\u00e3\u00e4\7\f\2\2\u00e4\u00eb\7\62\2\2\u00e5\u00e7\7\r\2\2\u00e6"+
		"\u00e8\5> \2\u00e7\u00e6\3\2\2\2\u00e7\u00e8\3\2\2\2\u00e8\u00e9\3\2\2"+
		"\2\u00e9\u00eb\7\62\2\2\u00ea\u00e1\3\2\2\2\u00ea\u00e3\3\2\2\2\u00ea"+
		"\u00e5\3\2\2\2\u00eb\63\3\2\2\2\u00ec\u00ed\7\17\2\2\u00ed\u00ee\7\25"+
		"\2\2\u00ee\u00f2\78\2\2\u00ef\u00f1\5\66\34\2\u00f0\u00ef\3\2\2\2\u00f1"+
		"\u00f4\3\2\2\2\u00f2\u00f0\3\2\2\2\u00f2\u00f3\3\2\2\2\u00f3\u00f5\3\2"+
		"\2\2\u00f4\u00f2\3\2\2\2\u00f5\u00f6\79\2\2\u00f6\65\3\2\2\2\u00f7\u00fb"+
		"\5\6\4\2\u00f8\u00fb\5:\36\2\u00f9\u00fb\58\35\2\u00fa\u00f7\3\2\2\2\u00fa"+
		"\u00f8\3\2\2\2\u00fa\u00f9\3\2\2\2\u00fb\67\3\2\2\2\u00fc\u00fd\5\26\f"+
		"\2\u00fd9\3\2\2\2\u00fe\u00ff\7\25\2\2\u00ff\u0100\5\32\16\2\u0100\u0101"+
		"\5 \21\2\u0101;\3\2\2\2\u0102\u0107\5> \2\u0103\u0104\7\63\2\2\u0104\u0106"+
		"\5> \2\u0105\u0103\3\2\2\2\u0106\u0109\3\2\2\2\u0107\u0105\3\2\2\2\u0107"+
		"\u0108\3\2\2\2\u0108=\3\2\2\2\u0109\u0107\3\2\2\2\u010a\u010b\b \1\2\u010b"+
		"\u010c\t\3\2\2\u010c\u011d\5> \27\u010d\u010e\t\4\2\2\u010e\u011d\5> "+
		"\26\u010f\u0110\7%\2\2\u0110\u011d\5> \25\u0111\u0112\7(\2\2\u0112\u011d"+
		"\5> \24\u0113\u0114\7\16\2\2\u0114\u011d\5B\"\2\u0115\u011d\7\25\2\2\u0116"+
		"\u011d\5@!\2\u0117\u011d\7\20\2\2\u0118\u0119\7\66\2\2\u0119\u011a\5>"+
		" \2\u011a\u011b\7\67\2\2\u011b\u011d\3\2\2\2\u011c\u010a\3\2\2\2\u011c"+
		"\u010d\3\2\2\2\u011c\u010f\3\2\2\2\u011c\u0111\3\2\2\2\u011c\u0113\3\2"+
		"\2\2\u011c\u0115\3\2\2\2\u011c\u0116\3\2\2\2\u011c\u0117\3\2\2\2\u011c"+
		"\u0118\3\2\2\2\u011d\u0154\3\2\2\2\u011e\u011f\f\22\2\2\u011f\u0120\t"+
		"\5\2\2\u0120\u0153\5> \23\u0121\u0122\f\21\2\2\u0122\u0123\t\4\2\2\u0123"+
		"\u0153\5> \22\u0124\u0125\f\20\2\2\u0125\u0126\t\6\2\2\u0126\u0153\5>"+
		" \21\u0127\u0128\f\17\2\2\u0128\u0129\t\7\2\2\u0129\u0153\5> \20\u012a"+
		"\u012b\f\16\2\2\u012b\u012c\t\b\2\2\u012c\u0153\5> \17\u012d\u012e\f\r"+
		"\2\2\u012e\u012f\t\t\2\2\u012f\u0153\5> \16\u0130\u0131\f\f\2\2\u0131"+
		"\u0132\7+\2\2\u0132\u0153\5> \r\u0133\u0134\f\13\2\2\u0134\u0135\7*\2"+
		"\2\u0135\u0153\5> \f\u0136\u0137\f\n\2\2\u0137\u0138\7)\2\2\u0138\u0153"+
		"\5> \13\u0139\u013a\f\t\2\2\u013a\u013b\7,\2\2\u013b\u0153\5> \n\u013c"+
		"\u013d\f\b\2\2\u013d\u013e\7-\2\2\u013e\u0153\5> \t\u013f\u0140\f\7\2"+
		"\2\u0140\u0141\7.\2\2\u0141\u0153\5> \7\u0142\u0143\f\33\2\2\u0143\u0153"+
		"\t\3\2\2\u0144\u0145\f\32\2\2\u0145\u0146\7\61\2\2\u0146\u0153\7\25\2"+
		"\2\u0147\u0148\f\31\2\2\u0148\u014a\7\66\2\2\u0149\u014b\5<\37\2\u014a"+
		"\u0149\3\2\2\2\u014a\u014b\3\2\2\2\u014b\u014c\3\2\2\2\u014c\u0153\7\67"+
		"\2\2\u014d\u014e\f\30\2\2\u014e\u014f\7\64\2\2\u014f\u0150\5> \2\u0150"+
		"\u0151\7\65\2\2\u0151\u0153\3\2\2\2\u0152\u011e\3\2\2\2\u0152\u0121\3"+
		"\2\2\2\u0152\u0124\3\2\2\2\u0152\u0127\3\2\2\2\u0152\u012a\3\2\2\2\u0152"+
		"\u012d\3\2\2\2\u0152\u0130\3\2\2\2\u0152\u0133\3\2\2\2\u0152\u0136\3\2"+
		"\2\2\u0152\u0139\3\2\2\2\u0152\u013c\3\2\2\2\u0152\u013f\3\2\2\2\u0152"+
		"\u0142\3\2\2\2\u0152\u0144\3\2\2\2\u0152\u0147\3\2\2\2\u0152\u014d\3\2"+
		"\2\2\u0153\u0156\3\2\2\2\u0154\u0152\3\2\2\2\u0154\u0155\3\2\2\2\u0155"+
		"?\3\2\2\2\u0156\u0154\3\2\2\2\u0157\u0158\t\n\2\2\u0158A\3\2\2\2\u0159"+
		"\u015c\5D#\2\u015a\u015c\5F$\2\u015b\u0159\3\2\2\2\u015b\u015a\3\2\2\2"+
		"\u015cC\3\2\2\2\u015d\u0162\5\20\t\2\u015e\u015f\7\66\2\2\u015f\u0160"+
		"\5<\37\2\u0160\u0161\7\67\2\2\u0161\u0163\3\2\2\2\u0162\u015e\3\2\2\2"+
		"\u0162\u0163\3\2\2\2\u0163E\3\2\2\2\u0164\u0166\5\20\t\2\u0165\u0167\5"+
		"H%\2\u0166\u0165\3\2\2\2\u0167\u0168\3\2\2\2\u0168\u0166\3\2\2\2\u0168"+
		"\u0169\3\2\2\2\u0169G\3\2\2\2\u016a\u016c\7\64\2\2\u016b\u016d\5> \2\u016c"+
		"\u016b\3\2\2\2\u016c\u016d\3\2\2\2\u016d\u016e\3\2\2\2\u016e\u016f\7\65"+
		"\2\2\u016fI\3\2\2\2\"MV_cimtx\u0085\u008d\u0090\u00a0\u00a6\u00b3\u00b7"+
		"\u00c5\u00d2\u00d6\u00da\u00e7\u00ea\u00f2\u00fa\u0107\u011c\u014a\u0152"+
		"\u0154\u015b\u0162\u0168\u016c";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}