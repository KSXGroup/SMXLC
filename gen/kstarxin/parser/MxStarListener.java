// Generated from /home/kstarxin/code/compiler/src/kstarxin/parser/MxStar.g4 by ANTLR 4.7.2
package kstarxin.parser;
package kstarxin.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MxStarParser}.
 */
public interface MxStarListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MxStarParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(MxStarParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(MxStarParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#declaration}.
	 * @param ctx the parse tree
	 */
	void enterDeclaration(MxStarParser.DeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#declaration}.
	 * @param ctx the parse tree
	 */
	void exitDeclaration(MxStarParser.DeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclaration(MxStarParser.VariableDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclaration(MxStarParser.VariableDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#variableDeclarationList}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclarationList(MxStarParser.VariableDeclarationListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#variableDeclarationList}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclarationList(MxStarParser.VariableDeclarationListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#variableDeclarator}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclarator(MxStarParser.VariableDeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#variableDeclarator}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclarator(MxStarParser.VariableDeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(MxStarParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(MxStarParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#arrayType}.
	 * @param ctx the parse tree
	 */
	void enterArrayType(MxStarParser.ArrayTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#arrayType}.
	 * @param ctx the parse tree
	 */
	void exitArrayType(MxStarParser.ArrayTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#nonArrayType}.
	 * @param ctx the parse tree
	 */
	void enterNonArrayType(MxStarParser.NonArrayTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#nonArrayType}.
	 * @param ctx the parse tree
	 */
	void exitNonArrayType(MxStarParser.NonArrayTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#userType}.
	 * @param ctx the parse tree
	 */
	void enterUserType(MxStarParser.UserTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#userType}.
	 * @param ctx the parse tree
	 */
	void exitUserType(MxStarParser.UserTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#primitiveType}.
	 * @param ctx the parse tree
	 */
	void enterPrimitiveType(MxStarParser.PrimitiveTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#primitiveType}.
	 * @param ctx the parse tree
	 */
	void exitPrimitiveType(MxStarParser.PrimitiveTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#methodDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterMethodDeclaration(MxStarParser.MethodDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#methodDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitMethodDeclaration(MxStarParser.MethodDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#typeWithVoid}.
	 * @param ctx the parse tree
	 */
	void enterTypeWithVoid(MxStarParser.TypeWithVoidContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#typeWithVoid}.
	 * @param ctx the parse tree
	 */
	void exitTypeWithVoid(MxStarParser.TypeWithVoidContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#parameterField}.
	 * @param ctx the parse tree
	 */
	void enterParameterField(MxStarParser.ParameterFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#parameterField}.
	 * @param ctx the parse tree
	 */
	void exitParameterField(MxStarParser.ParameterFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#parameterDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterParameterDeclaration(MxStarParser.ParameterDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#parameterDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitParameterDeclaration(MxStarParser.ParameterDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(MxStarParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(MxStarParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#expressionStatement}.
	 * @param ctx the parse tree
	 */
	void enterExpressionStatement(MxStarParser.ExpressionStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#expressionStatement}.
	 * @param ctx the parse tree
	 */
	void exitExpressionStatement(MxStarParser.ExpressionStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(MxStarParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(MxStarParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#conditionStatement}.
	 * @param ctx the parse tree
	 */
	void enterConditionStatement(MxStarParser.ConditionStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#conditionStatement}.
	 * @param ctx the parse tree
	 */
	void exitConditionStatement(MxStarParser.ConditionStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#elseIfStatement}.
	 * @param ctx the parse tree
	 */
	void enterElseIfStatement(MxStarParser.ElseIfStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#elseIfStatement}.
	 * @param ctx the parse tree
	 */
	void exitElseIfStatement(MxStarParser.ElseIfStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#elseStatement}.
	 * @param ctx the parse tree
	 */
	void enterElseStatement(MxStarParser.ElseStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#elseStatement}.
	 * @param ctx the parse tree
	 */
	void exitElseStatement(MxStarParser.ElseStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#loopStatement}.
	 * @param ctx the parse tree
	 */
	void enterLoopStatement(MxStarParser.LoopStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#loopStatement}.
	 * @param ctx the parse tree
	 */
	void exitLoopStatement(MxStarParser.LoopStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#forStatement}.
	 * @param ctx the parse tree
	 */
	void enterForStatement(MxStarParser.ForStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#forStatement}.
	 * @param ctx the parse tree
	 */
	void exitForStatement(MxStarParser.ForStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void enterWhileStatement(MxStarParser.WhileStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void exitWhileStatement(MxStarParser.WhileStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#normalForStatement}.
	 * @param ctx the parse tree
	 */
	void enterNormalForStatement(MxStarParser.NormalForStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#normalForStatement}.
	 * @param ctx the parse tree
	 */
	void exitNormalForStatement(MxStarParser.NormalForStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#jumpStatement}.
	 * @param ctx the parse tree
	 */
	void enterJumpStatement(MxStarParser.JumpStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#jumpStatement}.
	 * @param ctx the parse tree
	 */
	void exitJumpStatement(MxStarParser.JumpStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BreakJump}
	 * labeled alternative in {@link MxStarParser#jumpType}.
	 * @param ctx the parse tree
	 */
	void enterBreakJump(MxStarParser.BreakJumpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BreakJump}
	 * labeled alternative in {@link MxStarParser#jumpType}.
	 * @param ctx the parse tree
	 */
	void exitBreakJump(MxStarParser.BreakJumpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ContinueJump}
	 * labeled alternative in {@link MxStarParser#jumpType}.
	 * @param ctx the parse tree
	 */
	void enterContinueJump(MxStarParser.ContinueJumpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ContinueJump}
	 * labeled alternative in {@link MxStarParser#jumpType}.
	 * @param ctx the parse tree
	 */
	void exitContinueJump(MxStarParser.ContinueJumpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ReturnJump}
	 * labeled alternative in {@link MxStarParser#jumpType}.
	 * @param ctx the parse tree
	 */
	void enterReturnJump(MxStarParser.ReturnJumpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ReturnJump}
	 * labeled alternative in {@link MxStarParser#jumpType}.
	 * @param ctx the parse tree
	 */
	void exitReturnJump(MxStarParser.ReturnJumpContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#classDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterClassDeclaration(MxStarParser.ClassDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#classDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitClassDeclaration(MxStarParser.ClassDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#classBodyMember}.
	 * @param ctx the parse tree
	 */
	void enterClassBodyMember(MxStarParser.ClassBodyMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#classBodyMember}.
	 * @param ctx the parse tree
	 */
	void exitClassBodyMember(MxStarParser.ClassBodyMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#classMemberFunctionDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterClassMemberFunctionDeclaration(MxStarParser.ClassMemberFunctionDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#classMemberFunctionDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitClassMemberFunctionDeclaration(MxStarParser.ClassMemberFunctionDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#classConstructorDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterClassConstructorDeclaration(MxStarParser.ClassConstructorDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#classConstructorDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitClassConstructorDeclaration(MxStarParser.ClassConstructorDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void enterExpressionList(MxStarParser.ExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void exitExpressionList(MxStarParser.ExpressionListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ThisExpression}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterThisExpression(MxStarParser.ThisExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ThisExpression}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitThisExpression(MxStarParser.ThisExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DotMemberMethodCall}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterDotMemberMethodCall(MxStarParser.DotMemberMethodCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DotMemberMethodCall}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitDotMemberMethodCall(MxStarParser.DotMemberMethodCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BinaryExpression}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterBinaryExpression(MxStarParser.BinaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BinaryExpression}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitBinaryExpression(MxStarParser.BinaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenthesisExpression}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterParenthesisExpression(MxStarParser.ParenthesisExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenthesisExpression}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitParenthesisExpression(MxStarParser.ParenthesisExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IndexAccess}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterIndexAccess(MxStarParser.IndexAccessContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IndexAccess}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitIndexAccess(MxStarParser.IndexAccessContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ConstantExpression}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterConstantExpression(MxStarParser.ConstantExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ConstantExpression}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitConstantExpression(MxStarParser.ConstantExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DotMember}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterDotMember(MxStarParser.DotMemberContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DotMember}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitDotMember(MxStarParser.DotMemberContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UnaryExpression}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpression(MxStarParser.UnaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UnaryExpression}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpression(MxStarParser.UnaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MethodCall}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMethodCall(MxStarParser.MethodCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MethodCall}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMethodCall(MxStarParser.MethodCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NewCreator}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNewCreator(MxStarParser.NewCreatorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NewCreator}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNewCreator(MxStarParser.NewCreatorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IdentifierExpression}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterIdentifierExpression(MxStarParser.IdentifierExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IdentifierExpression}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitIdentifierExpression(MxStarParser.IdentifierExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SuffixIncDec}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterSuffixIncDec(MxStarParser.SuffixIncDecContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SuffixIncDec}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitSuffixIncDec(MxStarParser.SuffixIncDecContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterConstant(MxStarParser.ConstantContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitConstant(MxStarParser.ConstantContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#creator}.
	 * @param ctx the parse tree
	 */
	void enterCreator(MxStarParser.CreatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#creator}.
	 * @param ctx the parse tree
	 */
	void exitCreator(MxStarParser.CreatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#nonArrayCreator}.
	 * @param ctx the parse tree
	 */
	void enterNonArrayCreator(MxStarParser.NonArrayCreatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#nonArrayCreator}.
	 * @param ctx the parse tree
	 */
	void exitNonArrayCreator(MxStarParser.NonArrayCreatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#arrayCreator}.
	 * @param ctx the parse tree
	 */
	void enterArrayCreator(MxStarParser.ArrayCreatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#arrayCreator}.
	 * @param ctx the parse tree
	 */
	void exitArrayCreator(MxStarParser.ArrayCreatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxStarParser#arrayCreatorUnit}.
	 * @param ctx the parse tree
	 */
	void enterArrayCreatorUnit(MxStarParser.ArrayCreatorUnitContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxStarParser#arrayCreatorUnit}.
	 * @param ctx the parse tree
	 */
	void exitArrayCreatorUnit(MxStarParser.ArrayCreatorUnitContext ctx);
}