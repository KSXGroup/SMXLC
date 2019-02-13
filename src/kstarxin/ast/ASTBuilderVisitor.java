package kstarxin.ast;
import java.util.*;
import kstarxin.parser.*;
import kstarxin.utilities.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

//TODO:MAKER ERROR REPORTER USER-FRIENDLY
//TODO:TYPE CHECK
public class ASTBuilderVisitor extends MxStarBaseVisitor<Node> {
    public static final String globalScopeName = "GLOBAL";
    public static final String constructorPrefix = "_CONSTRUCTOR_";
    public static final String builtinStringClassName = "_STRING_";
    public static final String builtinArrayClassName = "_ARRAY_";

    private SymbolTable currentSymbolTable;
    private SymbolTable classMemberTable;
    private MxStarParser.ProgramContext concreteSyntaxTree;
    private LinkedList<Symbol> parameterBuffer;
    private String scopePrefix;
    private String scopeName;
    private boolean inMethodDecl; //for return
    private boolean inConstructor;
    private boolean inClass;
    private int inLoop;
    private int loopCount;
    private int conditionCount;

    private void enterScope() {
        scopePrefix += "_";
    }

    private void exitScope() {
        scopePrefix = scopePrefix.substring(1);
    }

    private void initializeBuiltinMethods(){
        MxType t = new MxType("void");
        ArrayList<MxType> para = new ArrayList<>();
        para.add(new MxType("string"));
        t.setParaTypeList(para);
        currentSymbolTable.addBuiltInMethod(t,"print", new Location(concreteSyntaxTree));

        t= new MxType("void");
        para = new ArrayList<>();
        para.add(new MxType("string"));
        t.setParaTypeList(para);
        currentSymbolTable.addBuiltInMethod(t,"println", new Location(concreteSyntaxTree));

        t = new MxType("string");
        para = new ArrayList<>();
        t.setParaTypeList(para);
        currentSymbolTable.addBuiltInMethod(t,"getString", new Location(concreteSyntaxTree));

        t = new MxType("int");
        para = new ArrayList<>();
        t.setParaTypeList(para);
        currentSymbolTable.addBuiltInMethod(t,"getInt", new Location(concreteSyntaxTree));

        t= new MxType("string");
        para = new ArrayList<>();
        para.add(new MxType("int"));
        t.setParaTypeList(para);
        currentSymbolTable.addBuiltInMethod(t,"toString", new Location(concreteSyntaxTree));
    }

    private void initializeBuiltInMemberMethod(){
        SymbolTable stringStb = new SymbolTable(builtinStringClassName);

        MxType t= new MxType("int");
        ArrayList<MxType> para = new ArrayList<>();
        t.setParaTypeList(para);
        stringStb.addBuiltInMethod(t, "length",new Location(concreteSyntaxTree));

        t= new MxType("string");
        para = new ArrayList<>();
        para.add(new MxType("int"));
        para.add(new MxType("int"));
        t.setParaTypeList(para);
        stringStb.addBuiltInMethod(t, "substring",new Location(concreteSyntaxTree));

        t= new MxType("int");
        para = new ArrayList<>();
        t.setParaTypeList(para);
        stringStb.addBuiltInMethod(t, "parseInt",new Location(concreteSyntaxTree));

        t= new MxType("int");
        para = new ArrayList<>();
        para.add(new MxType("int"));
        t.setParaTypeList(para);
        stringStb.addBuiltInMethod(t, "ord",new Location(concreteSyntaxTree));

        currentSymbolTable.addClass(builtinStringClassName, stringStb, new Location(concreteSyntaxTree));

        SymbolTable arrayStb = new SymbolTable(builtinArrayClassName);
        t= new MxType("int");
        para = new ArrayList<>();
        t.setParaTypeList(para);
        arrayStb.addBuiltInMethod(t, "size",new Location(concreteSyntaxTree));

        currentSymbolTable.addClass(builtinArrayClassName, arrayStb, new Location(concreteSyntaxTree));
    }

    public ASTBuilderVisitor(MxStarParser.ProgramContext cst) {
        parameterBuffer = new LinkedList<Symbol>();
        concreteSyntaxTree = cst;
        scopePrefix = "";
        scopeName = globalScopeName;
        inMethodDecl = false;
        inConstructor = false;
        inLoop = 0;
        loopCount = 0;
        conditionCount = 0;
        currentSymbolTable = new SymbolTable(scopePrefix + scopeName);
        initializeBuiltinMethods();
        initializeBuiltInMemberMethod();
    }

    public ProgramNode build() {
        ProgramNode ret =  (ProgramNode) visitProgram(concreteSyntaxTree);
        ret.getCurrentSymbolTable().pushDown();
        return ret;
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
            if(inClass & !inMethodDecl & !inConstructor) classMemberTable.addVariable(type.getType(), varId, loc);
        }
        return vardecl;
    }

    @Override
    public Node visitMethodDeclaration(MxStarParser.MethodDeclarationContext ctx) {
        String methName = ctx.Identifier().getText();
        SymbolTable methodTable = null;
        inMethodDecl = true;
        String parentScopeName = scopeName;
        scopeName = scopeName + "_METHOD_" + methName;
        MethodDeclarationNode methnode = null;
        TypeNode retTypeNode = null;
        Location loc = new Location(ctx);
        BlockNode block = null;
        ParameterFieldNode pf = (ParameterFieldNode) visit(ctx.parameterField());
        for(ParameterDeclarationNode n : pf.getParameterList()){
            parameterBuffer.add(new Symbol(Symbol.SymbolType.VARIABLE,n.getIdentifier(), n.getTypeNode().getType(),scopeName, n.getLocation()));
        }
        retTypeNode = (TypeNode) visit(ctx.typeWithVoid());
        MxType funcDefType = new MxType(retTypeNode.getType().toString(), retTypeNode.getType().getDimension(), pf.getParameterTypeList());
        retTypeNode.setType(funcDefType);
        currentSymbolTable.addMethod(funcDefType, methName, loc);
        if(inClass) classMemberTable.addMethod(funcDefType, methName, loc);
        block = (BlockNode) visit(ctx.block());
        methodTable = block.getCurrentSymbolTable();
        for(ParameterDeclarationNode n : pf.getParameterList()){
            n.setCurrentSymbolTable(methodTable);
        }
        methnode = new MethodDeclarationNode(retTypeNode, methName, pf.getParameterList(), block, methodTable, loc);
        scopeName = parentScopeName;
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
        inClass = true;
        String id = ctx.Identifier().getText();
        classMemberTable = new SymbolTable(id);
        String parentScopeName = scopeName;
        LinkedList<MxStarParser.ClassConstructorDeclarationContext> consl = new LinkedList<MxStarParser.ClassConstructorDeclarationContext>();
        LinkedList<MxStarParser.ClassMemberFunctionDeclarationContext> methl = new LinkedList<MxStarParser.ClassMemberFunctionDeclarationContext>();
        Location loc = new Location(ctx);
        scopeName = scopeName + "_CLASS_" + id;
        currentSymbolTable.addClass(id,classMemberTable, loc);
        SymbolTable classSymbolTable = new SymbolTable(currentSymbolTable), parentTable = currentSymbolTable;
        classSymbolTable.setName(scopePrefix + scopeName);
        currentSymbolTable = classSymbolTable;
        ClassDeclarationNode classNode = new ClassDeclarationNode(id, currentSymbolTable, loc);
        for (MxStarParser.ClassBodyMemberContext m : ctx.classBodyMember()) {
            MxStarParser.VariableDeclarationContext vard = m.variableDeclaration();
            MxStarParser.ClassConstructorDeclarationContext consd = m.classConstructorDeclaration();
            MxStarParser.ClassMemberFunctionDeclarationContext methd = m.classMemberFunctionDeclaration();

            if (vard != null){
                classNode.addVariable((VariableDeclarationNode) visit(vard));
            }
            else if (consd != null){
                consl.add(consd);
            }
            else if (methd != null){
                methl.add(methd);
            }
        }


        for(MxStarParser.ClassMemberFunctionDeclarationContext ictx : methl){
            classNode.addMethod((MethodDeclarationNode) visit(ictx));
        }


        for(MxStarParser.ClassConstructorDeclarationContext ictx : consl){
            classNode.addConstructor((MethodDeclarationNode) visit(ictx));
        }

        parentTable.addChildSymbolTable(currentSymbolTable);
        currentSymbolTable = parentTable;
        scopeName = parentScopeName;
        inClass = false;
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
            paraDeclList.add(n);
            paraTypeList.add(n.getTypeNode().getType());
        }
        return new ParameterFieldNode(paraDeclList, paraTypeList, null, null);
    }

    @Override
    public Node visitParameterDeclaration(MxStarParser.ParameterDeclarationContext ctx) {
        TypeNode type = (TypeNode) visit(ctx.type());
        Location loc = new Location(ctx);
        String id = ctx.Identifier().getText();
        return new ParameterDeclarationNode(type, id, null, loc);
    }

    @Override
    public Node visitClassConstructorDeclaration(MxStarParser.ClassConstructorDeclarationContext ctx) {
        inConstructor = true;
        MethodDeclarationNode cons = null;
        Location loc = new Location(ctx);
        String name = ctx.Identifier().getText();
        String parentScopeName = scopeName;
        scopeName = scopeName + constructorPrefix + name;
        SymbolTable classTable = currentSymbolTable;
        ParameterFieldNode para = (ParameterFieldNode) visit(ctx.parameterField());
        for(ParameterDeclarationNode n : para.getParameterList()){
            parameterBuffer.add(new Symbol(Symbol.SymbolType.VARIABLE, n.getIdentifier(), n.getTypeNode().getType(),scopeName, n.getLocation()));
        }
        MxType consRetType = new MxType(name);
        consRetType.setParaTypeList(para.getParameterTypeList());
        BlockNode block = (BlockNode) visit(ctx.block());
        currentSymbolTable = block.getCurrentSymbolTable();
        for(ParameterDeclarationNode n : para.getParameterList()){
            n.setCurrentSymbolTable(currentSymbolTable);
        }
        TypeNode consTypeNode = new TypeNode(consRetType, classTable, loc);
        classTable.addMethod(consRetType, constructorPrefix + name, loc);
        classMemberTable.addMethod(consRetType,constructorPrefix + name,loc);
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
        parameterBuffer.forEach(n -> currentSymbolTable.addVariable(n));
        parameterBuffer.clear();
        List stml = ctx.statement();
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
        SymbolTable nstb = null,parentTable = null;
        boolean newStbFlag = false;
        Node then = null;
        scopeName += "_IF_" + cnt.toString();
        ExpressionNode cond = (ExpressionNode) visit(ctx.expression());
        if(ctx.statement().block() != null) then = visit(ctx.statement());
        else{
            newStbFlag = true;
            nstb = new SymbolTable(currentSymbolTable);
            nstb.setName(scopePrefix+ scopeName);
            parentTable = currentSymbolTable;
            currentSymbolTable = nstb;
            then = visit(ctx.statement());
            parentTable.addChildSymbolTable(currentSymbolTable);
            currentSymbolTable = parentTable;
        }
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
        SymbolTable nstb = null, parentTable = null;
        Node then = null;
        scopeName += "_ELSEIF_" + cnt.toString();
        ExpressionNode cond = (ExpressionNode) visit(ctx.expression());
        if(ctx.statement().block() != null) then = visit(ctx.statement());
        else{
            nstb = new SymbolTable(currentSymbolTable);
            nstb.setName(scopePrefix + scopeName);
            parentTable = currentSymbolTable;
            currentSymbolTable = nstb;
            then = visit(ctx.statement());
            parentTable.addChildSymbolTable(currentSymbolTable);
            currentSymbolTable = parentTable;
        }
        scopeName = parentScopeName;
        return new ConditionNode(cond, then, null, currentSymbolTable, new Location(ctx));
    }

    @Override
    public Node visitElseStatement(MxStarParser.ElseStatementContext ctx) {
        conditionCount++;
        Integer cnt = conditionCount;
        String parentScopeName = scopeName;
        scopeName += "_ELSE_" + cnt.toString();
        Node then = null;
        SymbolTable nstb = null, parentTable = null;
        if(ctx.statement().block() != null) then = visit(ctx.statement());
        else{
            nstb = new SymbolTable(currentSymbolTable);
            nstb.setName(scopePrefix + scopeName);
            parentTable = currentSymbolTable;
            currentSymbolTable = nstb;
            then = visit(ctx.statement());
            parentTable.addChildSymbolTable(currentSymbolTable);
            currentSymbolTable = parentTable;
        }
        scopeName = parentScopeName;
        return new ConditionNode(null, then, null, currentSymbolTable, new Location(ctx));
    }

    @Override
    public Node visitLoopStatement(MxStarParser.LoopStatementContext ctx) {
        inLoop++;
        loopCount += 1;
        Integer tmp  = loopCount;
        String parentScopeName = scopeName;
        scopeName += "_LOOP_" + tmp.toString();
        MxStarParser.WhileStatementContext whileStm = ctx.whileStatement();
        MxStarParser.ForStatementContext forStm = ctx.forStatement();
        SymbolTable parentTable = null, nstb = null;
        Node ret = null;
        ExpressionNode tinit = null;
        ExpressionNode tcond = null;
        ExpressionNode tstep = null;
        Node tbody = null;

        if(whileStm != null){
            tcond = (ExpressionNode) visit(whileStm.expression());
            if(whileStm.statement().block() != null) tbody = visit(whileStm.statement());
            else{
                nstb = new SymbolTable(currentSymbolTable);
                nstb.setName(scopePrefix + scopeName);
                parentTable = currentSymbolTable;
                currentSymbolTable = nstb;
                tbody = visit(whileStm.statement());
                parentTable.addChildSymbolTable(currentSymbolTable);
                currentSymbolTable = parentTable;
            }
            ret =  new LoopNode(false,null, tcond, null, tbody, currentSymbolTable, new Location(whileStm));
        }
        else if(forStm != null){
            if(forStm.normalForStatement().init != null)tinit = (ExpressionNode) visit(forStm.normalForStatement().init);
            if(forStm.normalForStatement().cond != null)tcond = (ExpressionNode) visit(forStm.normalForStatement().cond);
            if(forStm.normalForStatement().step != null)tstep = (ExpressionNode) visit(forStm.normalForStatement().step);
            if(forStm.normalForStatement().statement().block() != null) tbody = visit(forStm.normalForStatement().statement());
            else{
                nstb = new SymbolTable(currentSymbolTable);
                nstb.setName(scopePrefix + scopeName);
                parentTable = currentSymbolTable;
                currentSymbolTable = nstb;
                tbody = visit(forStm.normalForStatement().statement());
                parentTable.addChildSymbolTable(currentSymbolTable);
                currentSymbolTable = parentTable;
            }
            ret = new LoopNode(true, tinit,tcond ,tstep ,tbody ,currentSymbolTable, new Location(ctx));
        }
        else throw new CompileException("Unknown loop");
        scopeName = parentScopeName;
        inLoop--;
        return  ret;
    }

    @Override
    public Node visitBreakJump(MxStarParser.BreakJumpContext ctx) {
        if(inLoop == 0) throw new CompileException("break not in loop");
        else return new BreakNode(currentSymbolTable, new Location(ctx));
    }

    @Override
    public Node visitContinueJump(MxStarParser.ContinueJumpContext ctx) {
        if(inLoop == 0) throw new CompileException("continue not in loop");
        else return new ContinueNode(currentSymbolTable, new Location(ctx));
    }

    @Override
    public Node visitReturnJump(MxStarParser.ReturnJumpContext ctx) {
        if(!inConstructor && !inMethodDecl) throw new CompileException("invalid return");
        else if(inConstructor &&  ctx.expression() != null) throw new CompileException("nothing shold be returned in constructor!");
        else{
            ExpressionNode ret = null;
            if(ctx.expression() != null) ret = (ExpressionNode) visit(ctx.expression());
            return new ReturnNode(ret, currentSymbolTable, new Location(ctx));
        }
    }

    @Override
    public Node visitConstantExpression(MxStarParser.ConstantExpressionContext ctx) {
        return visit(ctx.constant());
    }

    @Override
    public Node visitExpressionStatement(MxStarParser.ExpressionStatementContext ctx) {
        return visit(ctx.expression());
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
    public Node visitIdentifierExpression(MxStarParser.IdentifierExpressionContext ctx) {
        String id = ctx.getText();
        ExpressionNode ret = null;
        Symbol varSymbol = currentSymbolTable.get(id);
        ret = new IdentifierNode(id, currentSymbolTable, new Location(ctx));
        if(varSymbol == null) throw new CompileException(id + " is not declared in this scope" + ctx.getStart().getLine());
        else{
            ret = new IdentifierNode(id, currentSymbolTable, new Location(ctx));
            ret.setType(varSymbol.getType());
        }
        return ret;
    }

    @Override
    public Node visitSuffixIncDec(MxStarParser.SuffixIncDecContext ctx) {
        return new SuffixIncreaseDecreaseNode(ctx.op.getType(), (ExpressionNode) visit(ctx.expression()), currentSymbolTable, new Location(ctx));
    }

    @Override
    public Node visitIndexAccess(MxStarParser.IndexAccessContext ctx) {
        return new IndexAccessNode((ExpressionNode) visit(ctx.expression(0)), (ExpressionNode) visit(ctx.expression(1)),currentSymbolTable, new Location(ctx));
    }

    @Override
    public Node visitUnaryExpression(MxStarParser.UnaryExpressionContext ctx) {
        return new UnaryExpressionNode(ctx.op.getType(),(ExpressionNode) visit(ctx.expression()), currentSymbolTable, new Location(ctx));
    }

    @Override
    public Node visitBinaryExpression(MxStarParser.BinaryExpressionContext ctx) {
        return new BinaryExpressionNode((ExpressionNode) visit(ctx.lhs), ctx.op.getType(),(ExpressionNode) visit(ctx.rhs), currentSymbolTable, new Location(ctx));
    }

    @Override
    public Node visitDotMember(MxStarParser.DotMemberContext ctx) {
        return new DotMemberNode((ExpressionNode) visit(ctx.expression()), ctx.Identifier().getText(), currentSymbolTable, new Location(ctx));
    }

    @Override
    public Node visitMethodCall(MxStarParser.MethodCallContext ctx) {
        ArrayList<ExpressionNode> para = new ArrayList<>();
        String methodName = ctx.Identifier().getText();
        MxStarParser.ExpressionListContext lst = ctx.expressionList();
        if(lst != null) {
            for (MxStarParser.ExpressionContext ectx : lst.expression()){
                para.add((ExpressionNode) visit(ectx));
            }
        }
        return new MethodCallNode(methodName, para, currentSymbolTable, new Location(ctx));
    }

    @Override
    public Node visitDotMemberMethodCall(MxStarParser.DotMemberMethodCallContext ctx) {
        ArrayList<ExpressionNode> para = new ArrayList<>();
        String memberMethodName = ctx.Identifier().getText();
        MxStarParser.ExpressionListContext lst = ctx.expressionList();
        if(lst != null) {
            for (MxStarParser.ExpressionContext ectx : lst.expression()) para.add((ExpressionNode) visit(ectx));
        }
        return new DotMemberMethodCallNode((ExpressionNode) visit(ctx.expression()), memberMethodName, para, currentSymbolTable, new Location(ctx));
    }

    @Override
    public Node visitNewCreator(MxStarParser.NewCreatorContext ctx) {
        MxStarParser.ArrayCreatorContext  arrc = ctx.creator().arrayCreator();
        MxStarParser.NonArrayCreatorContext narrc = ctx.creator().nonArrayCreator();
        MxStarParser.UserTypeContext uType = null;
        MxStarParser.PrimitiveTypeContext pType = null;
        if(arrc != null){
            int dim = arrc.arrayCreatorUnit().size();
            ArrayList<ExpressionNode> sizeList = new ArrayList<>();
            boolean flag = false;
            for(MxStarParser.ArrayCreatorUnitContext ictx : arrc.arrayCreatorUnit()){
                if(ictx.expression() != null){
                    if(!flag) sizeList.add((ExpressionNode) visit(ictx.expression()));
                    else throw new CompileException("Juggled array declaration error.");
                }
                else flag = true;
            }
            return new NewCreatorNode(new MxType(arrc.nonArrayType().getText()), dim, sizeList, currentSymbolTable, new Location(ctx));
        }
        else if(narrc != null){
            String types = narrc.nonArrayType().getText();
            if((types.equals("int") || types.equals("bool")) && narrc.LPAREN() != null) throw new CompileException("Int/bool have no constructor");
            ArrayList<ExpressionNode> para = new ArrayList<>();
            if(narrc.expressionList() != null) {
                for (MxStarParser.ExpressionContext ictx : narrc.expressionList().expression())
                    para.add((ExpressionNode) visit(ictx));
            }
            return new NewCreatorNode(new MxType(narrc.nonArrayType().getText()), para,currentSymbolTable, new Location(ctx));
        }else throw new CompileException("Invalid new creator");
    }

    @Override
    public Node visitParenthesisExpression(MxStarParser.ParenthesisExpressionContext ctx) {
        return visit(ctx.expression());
    }

    @Override
    public Node visitThisExpression(MxStarParser.ThisExpressionContext ctx) {
        return new ThisNode(currentSymbolTable, new Location(ctx));
    }
}