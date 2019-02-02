package kstarxin.ast;
import kstarxin.parser.*;
import kstarxin.utilities.*;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.*;

//TODO:Add if loop jump
public class ASTBuilderVisitor extends MxStarBaseVisitor<Node>{
    private SymbolTable currentSymbolTable;
    private MxStarParser.ProgramContext concreteSyntaxTree;
    private String scopePrefix;
    private String scopeName;
    private int inloop;
    private boolean inmeth;
    private int loopCount;

    public ASTBuilderVisitor(MxStarParser.ProgramContext cst){
        concreteSyntaxTree = cst;
        inmeth = false;
        loopCount = 0;
        inloop = 0;
        scopePrefix = "";
        scopeName = "global";
        currentSymbolTable = new SymbolTable(scopePrefix + scopeName);
    }

    private void enterScope(){
        scopePrefix += "_";
    }

    private void exitScope(){
        scopePrefix = scopePrefix.substring(1);
    }

    public ProgramNode build(){
        return (ProgramNode) visitProgram(concreteSyntaxTree);
    }

    @Override
    public Node visitProgram(MxStarParser.ProgramContext ctx) {
        ProgramNode prog = new ProgramNode(currentSymbolTable, new Location(ctx));
        Node child;
        for(MxStarParser.DeclarationContext c : ctx.declaration()){
            if(c != null) {
                child = visit(c);
                if (child instanceof VariableDeclarationNode) {
                    prog.addVariableDeclaration((VariableDeclarationNode) child);
                } else if (child instanceof MethodDeclarationNode) {
                    prog.addMethodDeclaration((MethodDeclarationNode) child);
                } else if (child instanceof ClassDeclarationNode){
                    prog.addClassDeclarationNode((ClassDeclarationNode) child);
                } else if(child == null) break;
                else throw new CompileException("Unknown Program Section");
            }
        }
        prog.getCurrentSymbolTable().pushDown(); //Do Some type update
        return prog;
    }

    //Process variable declaration directly
    @Override
    public Node visitDeclaration(MxStarParser.DeclarationContext ctx){
        MxStarParser.VariableDeclarationContext vard    = ctx.variableDeclaration();
        MxStarParser.MethodDeclarationContext   methd   = ctx.methodDeclaration();
        MxStarParser.ClassDeclarationContext    classd  = ctx.classDeclaration();
        if(vard != null){
            return visit(vard);
        } else if(methd != null){
            return visit(methd);
        } else if(classd!= null){
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
    public Node visitMethodDeclaration(MxStarParser.MethodDeclarationContext ctx) throws CompileException{
        String methName = ctx.Identifier().getText();
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
        return methnode;
    }

    @Override
    public Node visitTypeWithVoid(MxStarParser.TypeWithVoidContext ctx) {
        MxStarParser.TypeContext tctx = ctx.type();
        if(ctx.type() != null) return visit(tctx);
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

        if(nonArrTctx != null){
            ptype = nonArrTctx.primitiveType();
            utype = nonArrTctx.userType();
        }
        else{
            dim = arrTctx.LBRAC().size();
            ptype = arrTctx.nonArrayType().primitiveType();
            utype = arrTctx.nonArrayType().userType();
        }
        if(ptype != null) varType = ptype.getText();
        else varType = utype.getText();
        return new TypeNode(new MxType(varType, dim), currentSymbolTable,typeLoc);
    }

    @Override
    public Node visitClassDeclaration(MxStarParser.ClassDeclarationContext ctx) {
        enterScope();
        String id = ctx.Identifier().getText();
        String parentScopeName = scopeName;
        Location loc = new Location(ctx);
        scopeName = scopeName + "_CLASS_" + id;
        SymbolTable classSymbolTable = new SymbolTable(scopePrefix + scopeName), parentTable = currentSymbolTable;
        currentSymbolTable.addClass(id, loc);
        currentSymbolTable = classSymbolTable;
        ClassDeclarationNode classNode = new ClassDeclarationNode(id, classSymbolTable, loc);
        for(MxStarParser.ClassBodyMemberContext m : ctx.classBodyMember()){
            MxStarParser.VariableDeclarationContext vard = m.variableDeclaration();
            MxStarParser.ClassConstructorDeclarationContext consd = m.classConstructorDeclaration();
            MxStarParser.ClassMemberFunctionDeclarationContext methd = m.classMemberFunctionDeclaration();

            if(vard != null) classNode.addVariable((VariableDeclarationNode) visit(vard));
            else if(consd != null) classNode.addConstructor((MethodDeclarationNode) visit(consd));
            else if(methd != null) classNode.addMethod((MethodDeclarationNode) visit(methd));
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
        for(MxStarParser.ParameterDeclarationContext ictx : paraDeclctx){
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
        return cons;
    }

    @Override
    public Node visitBlock(MxStarParser.BlockContext ctx) {
        enterScope();
        SymbolTable newScopeTable = new SymbolTable(scopePrefix + scopeName);
        SymbolTable parentTable = currentSymbolTable;
        Location loc = new Location(ctx);
        BlockNode block = new BlockNode(newScopeTable, loc);
        currentSymbolTable = newScopeTable;
        for(MxStarParser.StatementContext stm :  ctx.statement()) block.addStatement(visit(stm));
        parentTable.addChildSymbolTable(currentSymbolTable);
        currentSymbolTable = parentTable;
        exitScope();
        return block;
    }

    @Override
    public Node visitConditionStatement(MxStarParser.ConditionStatementContext ctx) {
        return null;
    }

    @Override
    public Node visitConstant(MxStarParser.ConstantContext ctx) {
        return super.visitConstant(ctx);
    }
}