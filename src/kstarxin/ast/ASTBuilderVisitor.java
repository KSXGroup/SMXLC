package kstarxin.ast;
import kstarxin.parser.*;
import kstarxin.utilities.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.sql.Statement;
import java.util.*;

//TODO:Add if loop jump
public class ASTBuilderVisitor extends MxStarBaseVisitor<Node> {
    private SymbolTable currentSymbolTable;
    private MxStarParser.ProgramContext concreteSyntaxTree;
    private String scopePrefix;
    private String scopeName;
    private boolean inMethodDecl; //for return
    private boolean inConstructor;
    private boolean inLoop;
    private int loopCount;
    private int conditionCount;

    private void enterScope() {
        scopePrefix += "_";
    }

    private void exitScope() {
        scopePrefix = scopePrefix.substring(1);
    }

    public ASTBuilderVisitor(MxStarParser.ProgramContext cst) {
        concreteSyntaxTree = cst;
        scopePrefix = "";
        scopeName = "GLOBAL";
        inMethodDecl = false;
        inConstructor = false;
        inLoop = false;
        loopCount = 0;
        conditionCount = 0;
        currentSymbolTable = new SymbolTable(scopePrefix + scopeName);
    }

    public ProgramNode build() {
        return (ProgramNode) visitProgram(concreteSyntaxTree);
    }

    @Override
    public Node visitProgram(MxStarParser.ProgramContext ctx) {
        ProgramNode prog = new ProgramNode(currentSymbolTable, new Location(ctx));
        Node child;
        for (MxStarParser.DeclarationContext c : ctx.declaration()) {
            if (c != null) {
                child = visit(c);
                if (child instanceof VariableDeclarationNode) {
                    prog.addVariableDeclaration((VariableDeclarationNode) child);
                } else if (child instanceof MethodDeclarationNode) {
                    prog.addMethodDeclaration((MethodDeclarationNode) child);
                } else if (child instanceof ClassDeclarationNode) {
                    prog.addClassDeclarationNode((ClassDeclarationNode) child);
                } else if (child == null) break;
                else throw new CompileException("Unknown Program Section");
            }
        }
        return prog;
    }

    //Process variable declaration directly
    @Override
    public Node visitDeclaration(MxStarParser.DeclarationContext ctx) {
        MxStarParser.VariableDeclarationContext vard = ctx.variableDeclaration();
        MxStarParser.MethodDeclarationContext methd = ctx.methodDeclaration();
        MxStarParser.ClassDeclarationContext classd = ctx.classDeclaration();
        if (vard != null) {
            return visit(vard);
        } else if (methd != null) {
            return visit(methd);
        } else if (classd != null) {
            return visit(classd);
        }
        return null;
    }


    @Override
    public Node visitVariableDeclaration(MxStarParser.VariableDeclarationContext ctx) {
        List<MxStarParser.VariableDeclaratorContext> vardList;
        TypeNode type = (TypeNode) visit(ctx.type());
        vardList = ctx.variableDeclarationList().variableDeclarator();
        Location loc = null;

        VariableDeclarationNode vardecl = new VariableDeclarationNode(type, currentSymbolTable, new Location(ctx));

        for (MxStarParser.VariableDeclaratorContext declarator : vardList) {
            String varId = declarator.Identifier().getText();
            ExpressionNode init;
            if (declarator.ASSIGN() != null) init = (ExpressionNode) visit(declarator.expression());
            else init = null;
            loc = new Location(declarator);
            vardecl.addDeclarator(new VariableDeclaratorNode(varId, init, currentSymbolTable, loc));
            currentSymbolTable.addVariable(type.getType(), varId, loc);
        }

        return vardecl;
    }

    @Override
    public Node visitMethodDeclaration(MxStarParser.MethodDeclarationContext ctx) throws CompileException {
        String methName = ctx.Identifier().getText();
        inMethodDecl = true;
        String parentScopeName = scopeName;
        scopeName = scopeName + "_METHOD_" + methName;
        MethodDeclarationNode methnode = null;
        SymbolTable methTable = null, parentTable = null;
        TypeNode retTypeNode = null;
        Location loc = null;
        BlockNode block = null;
        retTypeNode = (TypeNode) visit(ctx.typeWithVoid());
        block = (BlockNode) visit(ctx.block());
        methTable = block.getCurrentSymbolTable();
        parentTable = currentSymbolTable;
        currentSymbolTable = methTable;
        ParameterFieldNode pf = (ParameterFieldNode) visit(ctx.parameterField());
        MxType funcDefType = new MxType(retTypeNode.getType().toString(), retTypeNode.getType().getDimension(), pf.getParameterTypeList());
        funcDefType.setMethod();
        retTypeNode.setType(funcDefType);
        loc = new Location(ctx);
        parentTable.addMethod(funcDefType, methName, loc);
        methnode = new MethodDeclarationNode(retTypeNode, methName, pf.getParameterList(), block, currentSymbolTable, loc);
        scopeName = parentScopeName;
        currentSymbolTable = parentTable;
        inMethodDecl = false;
        return methnode;
    }

    @Override
    public Node visitTypeWithVoid(MxStarParser.TypeWithVoidContext ctx) {
        MxStarParser.TypeContext tctx = ctx.type();
        if (ctx.type() != null) return visit(tctx);
        else return new TypeNode(new MxType("void"), currentSymbolTable, new Location(ctx));
    }

    @Override
    public Node visitType(MxStarParser.TypeContext ctx) {
        MxStarParser.NonArrayTypeContext nonArrTctx = ctx.nonArrayType();
        MxStarParser.ArrayTypeContext arrTctx = ctx.arrayType();
        int dim = 0;
        MxStarParser.PrimitiveTypeContext ptype = null;
        MxStarParser.UserTypeContext utype = null;
        String varType = null;
        Location typeLoc = new Location(ctx);

        if (nonArrTctx != null) {
            ptype = nonArrTctx.primitiveType();
            utype = nonArrTctx.userType();
        } else {
            dim = arrTctx.LBRAC().size();
            ptype = arrTctx.nonArrayType().primitiveType();
            utype = arrTctx.nonArrayType().userType();
        }
        if (ptype != null) varType = ptype.getText();
        else varType = utype.getText();
        return new TypeNode(new MxType(varType, dim), currentSymbolTable, typeLoc);
    }

    @Override
    public Node visitClassDeclaration(MxStarParser.ClassDeclarationContext ctx) {
        enterScope();
        String id = ctx.Identifier().getText();
        String parentScopeName = scopeName;
        Location loc = new Location(ctx);
        scopeName = scopeName + "_CLASS_" + id;
        SymbolTable classSymbolTable = new SymbolTable(currentSymbolTable), parentTable = currentSymbolTable;
        classSymbolTable.setName(scopePrefix + scopeName);
        currentSymbolTable.addClass(id, loc);
        currentSymbolTable = classSymbolTable;
        ClassDeclarationNode classNode = new ClassDeclarationNode(id, classSymbolTable, loc);
        for (MxStarParser.ClassBodyMemberContext m : ctx.classBodyMember()) {
            MxStarParser.VariableDeclarationContext vard = m.variableDeclaration();
            MxStarParser.ClassConstructorDeclarationContext consd = m.classConstructorDeclaration();
            MxStarParser.ClassMemberFunctionDeclarationContext methd = m.classMemberFunctionDeclaration();

            if (vard != null) classNode.addVariable((VariableDeclarationNode) visit(vard));
            else if (consd != null) classNode.addConstructor((MethodDeclarationNode) visit(consd));
            else if (methd != null) classNode.addMethod((MethodDeclarationNode) visit(methd));
        }

        parentTable.addChildSymbolTable(currentSymbolTable);
        currentSymbolTable = parentTable;
        scopeName = parentScopeName;
        exitScope();
        return classNode;
    }

    @Override
    public Node visitParameterField(MxStarParser.ParameterFieldContext ctx) {
        List<MxStarParser.ParameterDeclarationContext> paraDeclctx = ctx.parameterDeclaration();
        ArrayList<ParameterDeclarationNode> paraDeclList = new ArrayList<>();
        ArrayList<MxType> paraTypeList = new ArrayList<>();
        ParameterDeclarationNode n = null;
        for (MxStarParser.ParameterDeclarationContext ictx : paraDeclctx) {
            n = (ParameterDeclarationNode) visit(ictx);
            paraTypeList.add(n.getTypeNode().getType());
        }
        return new ParameterFieldNode(paraDeclList, paraTypeList, null, null);
    }

    @Override
    public Node visitParameterDeclaration(MxStarParser.ParameterDeclarationContext ctx) {
        TypeNode type = (TypeNode) visit(ctx.type());
        Location loc = new Location(ctx);
        String id = ctx.Identifier().getText();
        currentSymbolTable.addVariable(type.getType(), id, loc);
        return new ParameterDeclarationNode(type, id, currentSymbolTable, loc);
    }

    @Override
    public Node visitClassConstructorDeclaration(MxStarParser.ClassConstructorDeclarationContext ctx) {
        inConstructor = true;
        MethodDeclarationNode cons = null;
        String name = ctx.Identifier().getText();
        String parentScopeName = scopeName;
        scopeName = scopeName + "_CLASSCONS_" + name;
        SymbolTable classTable = currentSymbolTable;
        MxType consRetType = new MxType(name);
        Location loc = new Location(ctx);
        BlockNode block = (BlockNode) visit(ctx.block());
        currentSymbolTable = block.getCurrentSymbolTable();
        TypeNode consTypeNode = new TypeNode(consRetType, currentSymbolTable, loc);
        ParameterFieldNode para = (ParameterFieldNode) visit(ctx.parameterField());
        consRetType.setMethod();
        consRetType.setParaTypeList(para.getParameterTypeList());
        classTable.addMethod(consRetType, name, loc);
        cons = new MethodDeclarationNode(consTypeNode, name, para.getParameterList(), block, currentSymbolTable, loc);
        currentSymbolTable = classTable;
        scopeName = parentScopeName;
        inConstructor = false;
        return cons;
    }

    @Override
    public Node visitBlock(MxStarParser.BlockContext ctx) {
        enterScope();
        SymbolTable newScopeTable = new SymbolTable(currentSymbolTable);
        newScopeTable.setName(scopePrefix + scopeName);
        SymbolTable parentTable = currentSymbolTable;
        Location loc = new Location(ctx);
        BlockNode block = new BlockNode(newScopeTable, loc);
        currentSymbolTable = newScopeTable;
        for (MxStarParser.StatementContext stm : ctx.statement()) {
            Node tmp = visit(stm);
            if (tmp != null) block.addStatement(tmp);
        }
        parentTable.addChildSymbolTable(currentSymbolTable);
        currentSymbolTable = parentTable;
        exitScope();
        return block;
    }

    @Override
    public Node visitConditionStatement(MxStarParser.ConditionStatementContext ctx) {
        conditionCount++;
        Integer cnt = conditionCount;
        String parentScopeName = scopeName;
        scopeName += "_IF_" + cnt.toString();
        ExpressionNode cond = (ExpressionNode) visit(ctx.expression());
        Node then = visit(ctx.statement());
        ConditionNode curr = null, ret = null, tmp = null;
        curr = new ConditionNode(cond, then , null, currentSymbolTable, new Location(ctx));
        ret = curr;
        for (MxStarParser.ElseIfStatementContext stm : ctx.elseIfStatement()) {
            tmp = (ConditionNode) visit(stm);
            curr.setElse(tmp);
            curr = tmp;
        }
        if(ctx.elseStatement() != null) curr.setElse((ConditionNode) visit(ctx.elseStatement()));
        else curr.setElse(null);
        scopeName = parentScopeName;
        return ret;
    }

    @Override
    public Node visitElseIfStatement(MxStarParser.ElseIfStatementContext ctx) {
        conditionCount++;
        Integer cnt = conditionCount;
        String parentScopeName = scopeName;
        scopeName += "_ELSEIF_" + cnt.toString();
        ExpressionNode cond = (ExpressionNode) visit(ctx.expression());
        Node then = visit(ctx.statement());
        scopeName = parentScopeName;
        return new ConditionNode(cond, then, null, currentSymbolTable, new Location(ctx));
    }

    @Override
    public Node visitElseStatement(MxStarParser.ElseStatementContext ctx) {
        conditionCount++;
        Integer cnt = conditionCount;
        String parentScopeName = scopeName;
        scopeName += "_ELSE_" + cnt.toString();
        Node then = visit(ctx.statement());
        scopeName = parentScopeName;
        return new ConditionNode(null, then, null, currentSymbolTable, new Location(ctx));
    }

    @Override
    public Node visitConstantExpression(MxStarParser.ConstantExpressionContext ctx) {
        return visit(ctx.constant());
    }

    @Override
    public Node visitConstant(MxStarParser.ConstantContext ctx) {
        Location loc = new Location(ctx);
        TerminalNode boolconst = ctx.BoolConst();
        TerminalNode intconst = ctx.IntegerConst();
        TerminalNode stringconst = ctx.StringConst();
        TerminalNode nullconst = ctx.NullConst();
        if (boolconst != null) return new BooleanConstantNode(boolconst.getText(), currentSymbolTable, loc);
        else if (intconst != null) return new IntegerConstantNode(intconst.getText(), currentSymbolTable, loc);
        else if (stringconst != null) return new StringConstantNode(stringconst.getText(), currentSymbolTable, loc);
        else if (nullconst != null) return new NullConstantNode(currentSymbolTable, loc);
        else throw new CompileException("Unknown Const Type");
    }

    @Override
    public Node visitLoopStatement(MxStarParser.LoopStatementContext ctx) {
        inLoop = true;
        loopCount += 1;
        Integer tmp  = loopCount;
        String parentScopeName = scopeName;
        scopeName += "_LOOP_" + tmp.toString();
        MxStarParser.WhileStatementContext whileStm = ctx.whileStatement();
        MxStarParser.ForStatementContext forStm = ctx.forStatement();
        Node ret = null;
        ExpressionNode tinit = null;
        ExpressionNode tcond = null;
        ExpressionNode tstep = null;
        Node tbody = null;

        if(whileStm != null){
            tcond = (ExpressionNode) visit(whileStm.expression());
            tbody = visit(whileStm.statement());
            ret =  new LoopNode(null, tcond, null, tbody, currentSymbolTable, new Location(whileStm));
        }
        else if(forStm != null){
            if(forStm.normalForStatement().init != null)tinit = (ExpressionNode) visit(forStm.normalForStatement().init);
            if(forStm.normalForStatement().cond != null)tcond = (ExpressionNode) visit(forStm.normalForStatement().cond);
            if(forStm.normalForStatement().step != null)tstep = (ExpressionNode) visit(forStm.normalForStatement().step);
            tbody = visit(forStm.normalForStatement().statement());
            ret = new LoopNode(tinit,tcond ,tstep ,tbody ,currentSymbolTable, new Location(ctx));
        }
        else throw new CompileException("Unknown loop");
        scopeName = parentScopeName;
        inLoop = false;
        return  ret;
    }

    @Override
    public Node visitBreakJump(MxStarParser.BreakJumpContext ctx) {
        if(!inLoop) throw new CompileException("break not in loop");
        else return new BreakNode(currentSymbolTable, new Location(ctx));
    }

    @Override
    public Node visitContinueJump(MxStarParser.ContinueJumpContext ctx) {
        if(!inLoop) throw new CompileException("continue not in loop");
        else return new ContinueNode(currentSymbolTable, new Location(ctx));
    }

    @Override
    public Node visitReturnJump(MxStarParser.ReturnJumpContext ctx) {
        if(!inConstructor && !inMethodDecl) throw new CompileException("invalid return");
        else if(inConstructor &&  ctx.expression() != null) throw new CompileException("nothing shold be returned in constructor!");
        else{}
        return null;
    }

}