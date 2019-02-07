// Generated from MxStar.g4 by ANTLR 4.7.2
package kstarxin.parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link MxStarParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface MxStarVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link MxStarParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(MxStarParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaration(MxStarParser.DeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#variableDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclaration(MxStarParser.VariableDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#variableDeclarationList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclarationList(MxStarParser.VariableDeclarationListContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#variableDeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclarator(MxStarParser.VariableDeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(MxStarParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#arrayType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayType(MxStarParser.ArrayTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#nonArrayType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonArrayType(MxStarParser.NonArrayTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#userType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUserType(MxStarParser.UserTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#primitiveType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveType(MxStarParser.PrimitiveTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#methodDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodDeclaration(MxStarParser.MethodDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#typeWithVoid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeWithVoid(MxStarParser.TypeWithVoidContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#parameterField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterField(MxStarParser.ParameterFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#parameterDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterDeclaration(MxStarParser.ParameterDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(MxStarParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#expressionStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionStatement(MxStarParser.ExpressionStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(MxStarParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#conditionStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionStatement(MxStarParser.ConditionStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#elseIfStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseIfStatement(MxStarParser.ElseIfStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#elseStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseStatement(MxStarParser.ElseStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#loopStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoopStatement(MxStarParser.LoopStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#forStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForStatement(MxStarParser.ForStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#whileStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStatement(MxStarParser.WhileStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#normalForStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNormalForStatement(MxStarParser.NormalForStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#jumpStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJumpStatement(MxStarParser.JumpStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BreakJump}
	 * labeled alternative in {@link MxStarParser#jumpType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreakJump(MxStarParser.BreakJumpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ContinueJump}
	 * labeled alternative in {@link MxStarParser#jumpType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinueJump(MxStarParser.ContinueJumpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ReturnJump}
	 * labeled alternative in {@link MxStarParser#jumpType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnJump(MxStarParser.ReturnJumpContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#classDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassDeclaration(MxStarParser.ClassDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#classBodyMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassBodyMember(MxStarParser.ClassBodyMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#classMemberFunctionDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassMemberFunctionDeclaration(MxStarParser.ClassMemberFunctionDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#classConstructorDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassConstructorDeclaration(MxStarParser.ClassConstructorDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#expressionList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionList(MxStarParser.ExpressionListContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ThisExpression}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitThisExpression(MxStarParser.ThisExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DotMemberMethodCall}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDotMemberMethodCall(MxStarParser.DotMemberMethodCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BinaryExpression}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryExpression(MxStarParser.BinaryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenthesisExpression}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenthesisExpression(MxStarParser.ParenthesisExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IndexAccess}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexAccess(MxStarParser.IndexAccessContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ConstantExpression}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstantExpression(MxStarParser.ConstantExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DotMember}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDotMember(MxStarParser.DotMemberContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UnaryExpression}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryExpression(MxStarParser.UnaryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MethodCall}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodCall(MxStarParser.MethodCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NewCreator}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNewCreator(MxStarParser.NewCreatorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IdentifierExpression}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierExpression(MxStarParser.IdentifierExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SuffixIncDec}
	 * labeled alternative in {@link MxStarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSuffixIncDec(MxStarParser.SuffixIncDecContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstant(MxStarParser.ConstantContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#creator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreator(MxStarParser.CreatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#nonArrayCreator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonArrayCreator(MxStarParser.NonArrayCreatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#arrayCreator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayCreator(MxStarParser.ArrayCreatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxStarParser#arrayCreatorUnit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayCreatorUnit(MxStarParser.ArrayCreatorUnitContext ctx);
}