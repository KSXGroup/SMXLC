// Generated from /home/kstarxin/code/compiler/src/kstarxin/parser/MxStar.g4 by ANTLR 4.7.2
package kstarxin.parser;
package kstarxin.parser;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MxStarLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		Boolean=1, Integer=2, String=3, Void=4, If=5, Else=6, For=7, While=8, 
		Break=9, Continue=10, Return=11, New=12, Class=13, This=14, NullConst=15, 
		BoolConst=16, CommentLine=17, CommentBlock=18, WhiteSpace=19, NewLine=20, 
		Identifier=21, StringConst=22, IntegerConst=23, ADD=24, SUB=25, MUL=26, 
		DIV=27, MOD=28, GT=29, GE=30, LT=31, LE=32, EQ=33, NEQ=34, NOT=35, SFTL=36, 
		SFTR=37, BITNOT=38, BITOR=39, BITXOR=40, BITAND=41, AND=42, OR=43, ASSIGN=44, 
		INC=45, DEC=46, DOT=47, SEMI=48, COMMA=49, LBRAC=50, RBRAC=51, LPAREN=52, 
		RPAREN=53, LBRACE=54, RBRACE=55;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"Boolean", "Integer", "String", "Void", "If", "Else", "For", "While", 
			"Break", "Continue", "Return", "New", "Class", "This", "NullConst", "BoolConst", 
			"CommentLine", "CommentBlock", "WhiteSpace", "NewLine", "Identifier", 
			"EscapeCharacter", "StringCharacter", "StringConst", "Digit", "NonZeroDigit", 
			"DecimalInteger", "IntegerConst", "ADD", "SUB", "MUL", "DIV", "MOD", 
			"GT", "GE", "LT", "LE", "EQ", "NEQ", "NOT", "SFTL", "SFTR", "BITNOT", 
			"BITOR", "BITXOR", "BITAND", "AND", "OR", "ASSIGN", "INC", "DEC", "DOT", 
			"SEMI", "COMMA", "LBRAC", "RBRAC", "LPAREN", "RPAREN", "LBRACE", "RBRACE"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'bool'", "'int'", "'string'", "'void'", "'if'", "'else'", "'for'", 
			"'while'", "'break'", "'continue'", "'return'", "'new'", "'class'", "'this'", 
			"'null'", null, null, null, null, null, null, null, null, "'+'", "'-'", 
			"'*'", "'/'", "'%'", "'>'", "'>='", "'<'", "'<='", "'=='", "'!='", "'!'", 
			"'<<'", "'>>'", "'~'", "'|'", "'^'", "'&'", "'&&'", "'||'", "'='", "'++'", 
			"'--'", "'.'", "';'", "','", "'['", "']'", "'('", "')'", "'{'", "'}'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "Boolean", "Integer", "String", "Void", "If", "Else", "For", "While", 
			"Break", "Continue", "Return", "New", "Class", "This", "NullConst", "BoolConst", 
			"CommentLine", "CommentBlock", "WhiteSpace", "NewLine", "Identifier", 
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


	public MxStarLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "MxStar.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\29\u0171\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3"+
		"\5\3\5\3\5\3\5\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\t\3\t"+
		"\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13"+
		"\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\16\3\16"+
		"\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20"+
		"\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\5\21\u00d6\n\21\3\22\3\22"+
		"\3\22\3\22\7\22\u00dc\n\22\f\22\16\22\u00df\13\22\3\22\3\22\3\22\3\22"+
		"\3\23\3\23\3\23\3\23\7\23\u00e9\n\23\f\23\16\23\u00ec\13\23\3\23\3\23"+
		"\3\23\3\23\3\23\3\24\6\24\u00f4\n\24\r\24\16\24\u00f5\3\24\3\24\3\25\5"+
		"\25\u00fb\n\25\3\25\3\25\3\25\3\25\3\26\3\26\7\26\u0103\n\26\f\26\16\26"+
		"\u0106\13\26\3\27\3\27\3\27\3\30\3\30\5\30\u010d\n\30\3\31\3\31\7\31\u0111"+
		"\n\31\f\31\16\31\u0114\13\31\3\31\3\31\3\32\3\32\3\33\3\33\3\34\3\34\7"+
		"\34\u011e\n\34\f\34\16\34\u0121\13\34\3\34\5\34\u0124\n\34\3\35\3\35\3"+
		"\36\3\36\3\37\3\37\3 \3 \3!\3!\3\"\3\"\3#\3#\3$\3$\3$\3%\3%\3&\3&\3&\3"+
		"\'\3\'\3\'\3(\3(\3(\3)\3)\3*\3*\3*\3+\3+\3+\3,\3,\3-\3-\3.\3.\3/\3/\3"+
		"\60\3\60\3\60\3\61\3\61\3\61\3\62\3\62\3\63\3\63\3\63\3\64\3\64\3\64\3"+
		"\65\3\65\3\66\3\66\3\67\3\67\38\38\39\39\3:\3:\3;\3;\3<\3<\3=\3=\4\u00dd"+
		"\u00ea\2>\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33"+
		"\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\2/\2\61\30\63\2\65\2\67\29\31"+
		";\32=\33?\34A\35C\36E\37G I!K\"M#O$Q%S&U\'W(Y)[*]+_,a-c.e/g\60i\61k\62"+
		"m\63o\64q\65s\66u\67w8y9\3\2\n\5\2\13\f\17\17\"\"\4\2C\\c|\6\2\62;C\\"+
		"aac|\f\2$$))\62\62^^cdhhppttvvxx\4\2$$^^\3\2\62;\3\2\63;\3\2\62\62\2\u0175"+
		"\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2"+
		"\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2"+
		"\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2"+
		"\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2\61\3\2\2\2\29\3\2"+
		"\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2"+
		"\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S"+
		"\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2"+
		"\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3\2\2\2\2i\3\2\2\2\2k\3\2\2\2"+
		"\2m\3\2\2\2\2o\3\2\2\2\2q\3\2\2\2\2s\3\2\2\2\2u\3\2\2\2\2w\3\2\2\2\2y"+
		"\3\2\2\2\3{\3\2\2\2\5\u0080\3\2\2\2\7\u0084\3\2\2\2\t\u008b\3\2\2\2\13"+
		"\u0090\3\2\2\2\r\u0093\3\2\2\2\17\u0098\3\2\2\2\21\u009c\3\2\2\2\23\u00a2"+
		"\3\2\2\2\25\u00a8\3\2\2\2\27\u00b1\3\2\2\2\31\u00b8\3\2\2\2\33\u00bc\3"+
		"\2\2\2\35\u00c2\3\2\2\2\37\u00c7\3\2\2\2!\u00d5\3\2\2\2#\u00d7\3\2\2\2"+
		"%\u00e4\3\2\2\2\'\u00f3\3\2\2\2)\u00fa\3\2\2\2+\u0100\3\2\2\2-\u0107\3"+
		"\2\2\2/\u010c\3\2\2\2\61\u010e\3\2\2\2\63\u0117\3\2\2\2\65\u0119\3\2\2"+
		"\2\67\u0123\3\2\2\29\u0125\3\2\2\2;\u0127\3\2\2\2=\u0129\3\2\2\2?\u012b"+
		"\3\2\2\2A\u012d\3\2\2\2C\u012f\3\2\2\2E\u0131\3\2\2\2G\u0133\3\2\2\2I"+
		"\u0136\3\2\2\2K\u0138\3\2\2\2M\u013b\3\2\2\2O\u013e\3\2\2\2Q\u0141\3\2"+
		"\2\2S\u0143\3\2\2\2U\u0146\3\2\2\2W\u0149\3\2\2\2Y\u014b\3\2\2\2[\u014d"+
		"\3\2\2\2]\u014f\3\2\2\2_\u0151\3\2\2\2a\u0154\3\2\2\2c\u0157\3\2\2\2e"+
		"\u0159\3\2\2\2g\u015c\3\2\2\2i\u015f\3\2\2\2k\u0161\3\2\2\2m\u0163\3\2"+
		"\2\2o\u0165\3\2\2\2q\u0167\3\2\2\2s\u0169\3\2\2\2u\u016b\3\2\2\2w\u016d"+
		"\3\2\2\2y\u016f\3\2\2\2{|\7d\2\2|}\7q\2\2}~\7q\2\2~\177\7n\2\2\177\4\3"+
		"\2\2\2\u0080\u0081\7k\2\2\u0081\u0082\7p\2\2\u0082\u0083\7v\2\2\u0083"+
		"\6\3\2\2\2\u0084\u0085\7u\2\2\u0085\u0086\7v\2\2\u0086\u0087\7t\2\2\u0087"+
		"\u0088\7k\2\2\u0088\u0089\7p\2\2\u0089\u008a\7i\2\2\u008a\b\3\2\2\2\u008b"+
		"\u008c\7x\2\2\u008c\u008d\7q\2\2\u008d\u008e\7k\2\2\u008e\u008f\7f\2\2"+
		"\u008f\n\3\2\2\2\u0090\u0091\7k\2\2\u0091\u0092\7h\2\2\u0092\f\3\2\2\2"+
		"\u0093\u0094\7g\2\2\u0094\u0095\7n\2\2\u0095\u0096\7u\2\2\u0096\u0097"+
		"\7g\2\2\u0097\16\3\2\2\2\u0098\u0099\7h\2\2\u0099\u009a\7q\2\2\u009a\u009b"+
		"\7t\2\2\u009b\20\3\2\2\2\u009c\u009d\7y\2\2\u009d\u009e\7j\2\2\u009e\u009f"+
		"\7k\2\2\u009f\u00a0\7n\2\2\u00a0\u00a1\7g\2\2\u00a1\22\3\2\2\2\u00a2\u00a3"+
		"\7d\2\2\u00a3\u00a4\7t\2\2\u00a4\u00a5\7g\2\2\u00a5\u00a6\7c\2\2\u00a6"+
		"\u00a7\7m\2\2\u00a7\24\3\2\2\2\u00a8\u00a9\7e\2\2\u00a9\u00aa\7q\2\2\u00aa"+
		"\u00ab\7p\2\2\u00ab\u00ac\7v\2\2\u00ac\u00ad\7k\2\2\u00ad\u00ae\7p\2\2"+
		"\u00ae\u00af\7w\2\2\u00af\u00b0\7g\2\2\u00b0\26\3\2\2\2\u00b1\u00b2\7"+
		"t\2\2\u00b2\u00b3\7g\2\2\u00b3\u00b4\7v\2\2\u00b4\u00b5\7w\2\2\u00b5\u00b6"+
		"\7t\2\2\u00b6\u00b7\7p\2\2\u00b7\30\3\2\2\2\u00b8\u00b9\7p\2\2\u00b9\u00ba"+
		"\7g\2\2\u00ba\u00bb\7y\2\2\u00bb\32\3\2\2\2\u00bc\u00bd\7e\2\2\u00bd\u00be"+
		"\7n\2\2\u00be\u00bf\7c\2\2\u00bf\u00c0\7u\2\2\u00c0\u00c1\7u\2\2\u00c1"+
		"\34\3\2\2\2\u00c2\u00c3\7v\2\2\u00c3\u00c4\7j\2\2\u00c4\u00c5\7k\2\2\u00c5"+
		"\u00c6\7u\2\2\u00c6\36\3\2\2\2\u00c7\u00c8\7p\2\2\u00c8\u00c9\7w\2\2\u00c9"+
		"\u00ca\7n\2\2\u00ca\u00cb\7n\2\2\u00cb \3\2\2\2\u00cc\u00cd\7v\2\2\u00cd"+
		"\u00ce\7t\2\2\u00ce\u00cf\7w\2\2\u00cf\u00d6\7g\2\2\u00d0\u00d1\7h\2\2"+
		"\u00d1\u00d2\7c\2\2\u00d2\u00d3\7n\2\2\u00d3\u00d4\7u\2\2\u00d4\u00d6"+
		"\7g\2\2\u00d5\u00cc\3\2\2\2\u00d5\u00d0\3\2\2\2\u00d6\"\3\2\2\2\u00d7"+
		"\u00d8\7\61\2\2\u00d8\u00d9\7\61\2\2\u00d9\u00dd\3\2\2\2\u00da\u00dc\13"+
		"\2\2\2\u00db\u00da\3\2\2\2\u00dc\u00df\3\2\2\2\u00dd\u00de\3\2\2\2\u00dd"+
		"\u00db\3\2\2\2\u00de\u00e0\3\2\2\2\u00df\u00dd\3\2\2\2\u00e0\u00e1\7\f"+
		"\2\2\u00e1\u00e2\3\2\2\2\u00e2\u00e3\b\22\2\2\u00e3$\3\2\2\2\u00e4\u00e5"+
		"\7\61\2\2\u00e5\u00e6\7,\2\2\u00e6\u00ea\3\2\2\2\u00e7\u00e9\13\2\2\2"+
		"\u00e8\u00e7\3\2\2\2\u00e9\u00ec\3\2\2\2\u00ea\u00eb\3\2\2\2\u00ea\u00e8"+
		"\3\2\2\2\u00eb\u00ed\3\2\2\2\u00ec\u00ea\3\2\2\2\u00ed\u00ee\7,\2\2\u00ee"+
		"\u00ef\7\61\2\2\u00ef\u00f0\3\2\2\2\u00f0\u00f1\b\23\2\2\u00f1&\3\2\2"+
		"\2\u00f2\u00f4\t\2\2\2\u00f3\u00f2\3\2\2\2\u00f4\u00f5\3\2\2\2\u00f5\u00f3"+
		"\3\2\2\2\u00f5\u00f6\3\2\2\2\u00f6\u00f7\3\2\2\2\u00f7\u00f8\b\24\2\2"+
		"\u00f8(\3\2\2\2\u00f9\u00fb\7\17\2\2\u00fa\u00f9\3\2\2\2\u00fa\u00fb\3"+
		"\2\2\2\u00fb\u00fc\3\2\2\2\u00fc\u00fd\7\f\2\2\u00fd\u00fe\3\2\2\2\u00fe"+
		"\u00ff\b\25\2\2\u00ff*\3\2\2\2\u0100\u0104\t\3\2\2\u0101\u0103\t\4\2\2"+
		"\u0102\u0101\3\2\2\2\u0103\u0106\3\2\2\2\u0104\u0102\3\2\2\2\u0104\u0105"+
		"\3\2\2\2\u0105,\3\2\2\2\u0106\u0104\3\2\2\2\u0107\u0108\7^\2\2\u0108\u0109"+
		"\t\5\2\2\u0109.\3\2\2\2\u010a\u010d\n\6\2\2\u010b\u010d\5-\27\2\u010c"+
		"\u010a\3\2\2\2\u010c\u010b\3\2\2\2\u010d\60\3\2\2\2\u010e\u0112\7$\2\2"+
		"\u010f\u0111\5/\30\2\u0110\u010f\3\2\2\2\u0111\u0114\3\2\2\2\u0112\u0110"+
		"\3\2\2\2\u0112\u0113\3\2\2\2\u0113\u0115\3\2\2\2\u0114\u0112\3\2\2\2\u0115"+
		"\u0116\7$\2\2\u0116\62\3\2\2\2\u0117\u0118\t\7\2\2\u0118\64\3\2\2\2\u0119"+
		"\u011a\t\b\2\2\u011a\66\3\2\2\2\u011b\u011f\t\b\2\2\u011c\u011e\5\63\32"+
		"\2\u011d\u011c\3\2\2\2\u011e\u0121\3\2\2\2\u011f\u011d\3\2\2\2\u011f\u0120"+
		"\3\2\2\2\u0120\u0124\3\2\2\2\u0121\u011f\3\2\2\2\u0122\u0124\t\t\2\2\u0123"+
		"\u011b\3\2\2\2\u0123\u0122\3\2\2\2\u01248\3\2\2\2\u0125\u0126\5\67\34"+
		"\2\u0126:\3\2\2\2\u0127\u0128\7-\2\2\u0128<\3\2\2\2\u0129\u012a\7/\2\2"+
		"\u012a>\3\2\2\2\u012b\u012c\7,\2\2\u012c@\3\2\2\2\u012d\u012e\7\61\2\2"+
		"\u012eB\3\2\2\2\u012f\u0130\7\'\2\2\u0130D\3\2\2\2\u0131\u0132\7@\2\2"+
		"\u0132F\3\2\2\2\u0133\u0134\7@\2\2\u0134\u0135\7?\2\2\u0135H\3\2\2\2\u0136"+
		"\u0137\7>\2\2\u0137J\3\2\2\2\u0138\u0139\7>\2\2\u0139\u013a\7?\2\2\u013a"+
		"L\3\2\2\2\u013b\u013c\7?\2\2\u013c\u013d\7?\2\2\u013dN\3\2\2\2\u013e\u013f"+
		"\7#\2\2\u013f\u0140\7?\2\2\u0140P\3\2\2\2\u0141\u0142\7#\2\2\u0142R\3"+
		"\2\2\2\u0143\u0144\7>\2\2\u0144\u0145\7>\2\2\u0145T\3\2\2\2\u0146\u0147"+
		"\7@\2\2\u0147\u0148\7@\2\2\u0148V\3\2\2\2\u0149\u014a\7\u0080\2\2\u014a"+
		"X\3\2\2\2\u014b\u014c\7~\2\2\u014cZ\3\2\2\2\u014d\u014e\7`\2\2\u014e\\"+
		"\3\2\2\2\u014f\u0150\7(\2\2\u0150^\3\2\2\2\u0151\u0152\7(\2\2\u0152\u0153"+
		"\7(\2\2\u0153`\3\2\2\2\u0154\u0155\7~\2\2\u0155\u0156\7~\2\2\u0156b\3"+
		"\2\2\2\u0157\u0158\7?\2\2\u0158d\3\2\2\2\u0159\u015a\7-\2\2\u015a\u015b"+
		"\7-\2\2\u015bf\3\2\2\2\u015c\u015d\7/\2\2\u015d\u015e\7/\2\2\u015eh\3"+
		"\2\2\2\u015f\u0160\7\60\2\2\u0160j\3\2\2\2\u0161\u0162\7=\2\2\u0162l\3"+
		"\2\2\2\u0163\u0164\7.\2\2\u0164n\3\2\2\2\u0165\u0166\7]\2\2\u0166p\3\2"+
		"\2\2\u0167\u0168\7_\2\2\u0168r\3\2\2\2\u0169\u016a\7*\2\2\u016at\3\2\2"+
		"\2\u016b\u016c\7+\2\2\u016cv\3\2\2\2\u016d\u016e\7}\2\2\u016ex\3\2\2\2"+
		"\u016f\u0170\7\177\2\2\u0170z\3\2\2\2\r\2\u00d5\u00dd\u00ea\u00f5\u00fa"+
		"\u0104\u010c\u0112\u011f\u0123\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}