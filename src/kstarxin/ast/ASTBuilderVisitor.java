package kstarxin.ast;
import kstarxin.parser.*;
import kstarxin.utilities.*;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.*;


//TODO:let the compiler capable of identifying methods
public class ASTBuilderVisitor extends MxStarBaseVisitor<Node>{
    private SymbolTable currentSymbolTable;
    private MxStarParser.ProgramContext concreteSyntaxTree;
    public ASTBuilderVisitor(MxStarParser.ProgramContext cst){
        currentSymbolTable = new SymbolTable("Global");
        concreteSyntaxTree = cst;
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
                    //TODO
                } else if(child == null) break;
                else throw new CompileException("Unknown Program Section");
            }
        }
        prog.getCurrentSymbolTable().pushDown();
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
        }
        return null;
    }


    @Override
    public Node visitVariableDeclaration(MxStarParser.VariableDeclarationContext ctx) {
        List<MxStarParser.VariableDeclaratorContext> vardList;
        TypeNode type = (TypeNode) visit(ctx.type());
        vardList = ctx.variableDeclarationList().variableDeclarator();

        VariableDeclarationNode vardecl = new VariableDeclarationNode(type, currentSymbolTable, new Location(ctx));

        for (MxStarParser.VariableDeclaratorContext declarator : vardList) {
            String varId = declarator.Identifier().getText();
            ExpressionNode init;
            if (declarator.ASSIGN() != null) init = (ExpressionNode) visit(declarator.expression());
            else init = null;
            currentSymbolTable.addVariable(type.getType(), varId);
            vardecl.addDeclarator(new VariableDeclaratorNode(varId, init, currentSymbolTable, new Location(declarator)));
        }

        return vardecl;
    }

    @Override
    public Node visitMethodDeclaration(MxStarParser.MethodDeclarationContext ctx) throws CompileException{
        String methName = null;
        SymbolTable funcSymbolTable = null, parentTable = null;
        MethodDeclarationNode methnode = null;
        TypeNode retTypeNode = null;
        TypeNode paraTypeNode = null;
        LinkedList<Node> statements = new LinkedList<>();
        List<MxStarParser.ParameterDeclarationContext> paraListctx = ctx.parameterField().parameterDeclaration();
        ArrayList<ParameterDeclarationNode> paraList = new ArrayList<>();
        List<MxStarParser.MethodBodyContext> bodyListctx = ctx.methodBody();
        retTypeNode = (TypeNode) visit(ctx.typeWithVoid());

        methName = ctx.Identifier().getText();
        funcSymbolTable = new SymbolTable(methName);

        currentSymbolTable.addMethod(retTypeNode.getType(), methName);

        for(MxStarParser.ParameterDeclarationContext ictx : paraListctx) {
            paraTypeNode = (TypeNode) visit(ictx.type());
            paraList.add(new ParameterDeclarationNode(paraTypeNode, ictx.Identifier().getText(), null, new Location(ictx)));
            funcSymbolTable.addVariable(paraTypeNode.getType(), ictx.Identifier().getText());
        }
        parentTable = currentSymbolTable;
        currentSymbolTable = funcSymbolTable;
        for(MxStarParser.MethodBodyContext ictx : bodyListctx){
            if(ictx.statement().block() != null) throw new CompileException("Block not supported in method");
            else statements.add(visit(ictx.statement()));
        }
        methnode = new MethodDeclarationNode(retTypeNode, methName, paraList, statements, currentSymbolTable, new Location(ctx));
        parentTable.addChildSymbolTable(currentSymbolTable);
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

}