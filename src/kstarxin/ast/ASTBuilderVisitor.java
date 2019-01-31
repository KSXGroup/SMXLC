package kstarxin.ast;
import kstarxin.parser.*;
import kstarxin.utilities.*;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.*;


//TODO:make the compiler can identify simple global var
// that need symbol table
public class ASTBuilderVisitor extends MxStarBaseVisitor<Node>{
    private SymbolTable currentSymbolTable;
    private MxStarParser.ProgramContext concreteSyntaxTree;
    public ASTBuilderVisitor(MxStarParser.ProgramContext cst){
        currentSymbolTable = new SymbolTable();
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
                    //TODO handle function
                } else if (child instanceof ClassDeclarationNode){
                    //TODO
                } else if(child == null) break;
                else throw new CompileException("Unknown Program Section");
            }
        }
        return prog;
    }

    @Override
    public Node visitDeclaration(MxStarParser.DeclarationContext ctx){
        MxStarParser.VariableDeclarationContext vard    = ctx.variableDeclaration();
        MxStarParser.MethodDeclarationContext   methd   = ctx.methodDeclaration();
        MxStarParser.ClassDeclarationContext    classd  = ctx.classDeclaration();
        if(vard != null){
            String varType = null;
            MxStarParser.PrimitiveTypeContext ptype =  vard.type().nonArrayType().primitiveType();
            MxStarParser.UserTypeContext utype = vard.type().nonArrayType().userType();
            List<MxStarParser.VariableDeclaratorContext> vardList = vard.variableDeclarationList().variableDeclarator();

            Location typeLoc = new Location(vard.type());
            if(ptype != null) varType = ptype.getText();
            else varType = utype.getText();

            VariableDeclarationNode vardecl = new VariableDeclarationNode(new TypeNode(new MxType(varType), currentSymbolTable, typeLoc), currentSymbolTable, new Location(vard));

            for (MxStarParser.VariableDeclaratorContext declarator : vardList){
                String varId = declarator.Identifier().getText();
                ExpressionNode init;
                if(declarator.ASSIGN() != null){
                    init = (ExpressionNode) visit(declarator.expression());
                } else init = null;
                currentSymbolTable.addVariable(varType, varId);
                vardecl.addDeclarator(new VariableDeclaratorNode(varId,init, currentSymbolTable, new Location(declarator)));
            }

            return vardecl;

        } else if(methd != null){
        } else if(classd!= null){
        }
        return null;
    }

}