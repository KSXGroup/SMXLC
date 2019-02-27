package kstarxin.ir;

import com.sun.org.apache.bcel.internal.generic.BALOAD;
import com.sun.org.apache.bcel.internal.generic.IMPDEP1;
import kstarxin.ast.*;
import kstarxin.compiler.*;
import kstarxin.ir.instruction.*;
import kstarxin.ir.operand.*;
import kstarxin.ir.superblock.LoopSuperBlock;
import kstarxin.ir.superblock.SuperBlock;
import kstarxin.parser.MxStarParser;
import kstarxin.utilities.*;

import java.util.*;


public class IRBuilderVisitor implements ASTBaseVisitor<Operand> {
    public static String cond       = "_cond";
    public static String step       = "_step";
    public static String body       = "_body";
    public static String after      = "_after";
    public static String ret        = "_return";
    public static String ifTrue     = "_true";
    public static String ifFalse    = "_false";

    private IRProgram   ir;
    private ProgramNode ast;
    private SymbolTable currentClassSymbolTable;
    private Method      currentMethod;
    private SuperBlock  currentSuperBlock;
    private BasicBlock  currentBasicBlock;
    private BasicBlock  BasicBlockAfterCurrentLoop;
    private BasicBlock  CondBasicBlockOfCurrentLoop;
    private BasicBlock  StepBasicBlockOfCurrentLoop;
    private BasicBlock  BodyStartBasicBlockOfCurrentLoop;
    private boolean     inMethod;
    private boolean     inClass;
    private int         currentVariableOffset;

    public IRBuilderVisitor(ProgramNode _ast){
        ast = _ast;
        ir = new IRProgram();
        inMethod = false;
        inClass = false;
        currentVariableOffset = 0;
    }


    class LoopBackUper{
        private SuperBlock _CurrentSuperBlock;
        private BasicBlock _BasicBlockAfterCurrentLoop;
        private BasicBlock _CondBasicBlockOfCurrentLoop;
        private BasicBlock _StepBasicBlockOfCurrentLoop;
        private BasicBlock _BodyStartBasicBlockOfCurrentLoop;
        public void backup(){
            _CurrentSuperBlock                  = currentSuperBlock;
            _BasicBlockAfterCurrentLoop         = BasicBlockAfterCurrentLoop;
            _CondBasicBlockOfCurrentLoop        = CondBasicBlockOfCurrentLoop;
            _StepBasicBlockOfCurrentLoop        = StepBasicBlockOfCurrentLoop;
            _BodyStartBasicBlockOfCurrentLoop   = BodyStartBasicBlockOfCurrentLoop;
        }
        public void restore(){
            currentSuperBlock                  = _CurrentSuperBlock;
            BasicBlockAfterCurrentLoop         = _BasicBlockAfterCurrentLoop;
            CondBasicBlockOfCurrentLoop        = _CondBasicBlockOfCurrentLoop;
            StepBasicBlockOfCurrentLoop        = _StepBasicBlockOfCurrentLoop;
            BodyStartBasicBlockOfCurrentLoop   = _BodyStartBasicBlockOfCurrentLoop;
        }
    }

    private void init(){
        ast.getMethodDeclarations().forEach(
                decl ->  {
                    String mn = NameMangler.mangleName(decl);
                    ir.addMethod(mn , new Method(mn, false), decl.getReturnType());
            }
        );
        ast.getClassDeclarations().forEach(
                decl -> {
                    decl.getMemberMethodList().forEach(
                            mdecl -> {
                                String mn = NameMangler.mangleName(mdecl);
                                ir.addMethod(mn, new Method(mn, true), mdecl.getReturnType());
                            }
                    );
                    decl.getConstructorList().forEach(
                            cdecl -> {
                                String mn = NameMangler.mangleName(cdecl);
                                ir.addMethod(mn, new Method(mn, true), cdecl.getReturnType());
                            }
                    );
                }
        );
    }

    private void initBuiltin(){
        ast.getCurrentSymbolTable().getOriginTable().values().forEach(
                symbol -> {
                    String mn = NameMangler.mangleName(symbol);
                    if(symbol.isBuiltIn()) ir.addBuiltinMethod(mn , new Method(mn, false), symbol.getType());
                }
        );
        //add strcat //add strcmp
        ArrayList<MxType> paraStrcat = new ArrayList<MxType>();
        paraStrcat.add(new MxType(MxType.TypeEnum.STRING));
        paraStrcat.add(new MxType(MxType.TypeEnum.STRING));
        MxType retOfstrcat = new MxType(MxType.TypeEnum.STRING, "string", paraStrcat);
        MxType retOfstrcmp = new MxType(MxType.TypeEnum.INT, "int", paraStrcat);
        ir.addBuiltinMethod(NameMangler.strcat, new Method(NameMangler.strcat, false), retOfstrcat);
        ir.addBuiltinMethod(NameMangler.strcmp, new Method(NameMangler.strcmp, false), retOfstrcmp);
        //add malloc
        ArrayList<MxType> paraMalloc = new ArrayList<MxType>();
        paraMalloc.add(new MxType(MxType.TypeEnum.INT));
        MxType retOfMalloc = new MxType(MxType.TypeEnum.INT, "int", paraMalloc);
        ir.addBuiltinMethod(NameMangler.malloc, new Method(NameMangler.malloc, false), retOfMalloc);
    }

    //resolve global variable and make program entrance
    private void makeEntranceMethod(){
        for(VariableDeclarationNode  n : ast.getVariableDeclarations()){
            //process global variable
            MxType t = n.getTypeNode().getType();
            if(!t.isPrimitiveType()){
                n.getDeclaratorList().forEach(decl -> {
                    String mn = NameMangler.mangleName(decl);
                    StaticPointer sp = new StaticPointer(mn , Configure.PTR_SIZE);
                    ir.addGlobalVariable(mn, sp, t);
                });
            }
            else if (t.isPrimitiveType() && !t.getEnumType().equals(MxType.TypeEnum.STRING)){
                for (VariableDeclaratorNode decl : n.getDeclaratorList()) {
                    StaticPointer sp = new StaticPointer(decl.getIdentifier(), Configure.PTR_SIZE);
                    ExpressionNode init = decl.getInitializer();
                    ir.addGlobalVariable(NameMangler.mangleName(decl), sp, t);
                    if (init != null) {
                        if(init instanceof IntegerConstantNode) sp.setValue(((IntegerConstantNode) init).getValue());
                        else if(init instanceof BooleanConstantNode){
                            if(((BooleanConstantNode) init).getValue() == true) sp.setValue(1);
                            else sp.setValue(0);
                        }
                        else {
                            Operand v = visit(init);
                            if(v instanceof Immediate) sp.setValue(((Immediate) v).value);
                            else currentBasicBlock.insertEnd(new StoreInstruction(sp, v, new Immediate(0)));
                        }
                    }
                }
            }
            else
                n.getDeclaratorList().forEach(decl -> ir.addGlobalVariable(NameMangler.mangleName(decl), new StaticString(decl.getIdentifier(), ((StringConstantNode)decl.getInitializer()).getValue()), t));
        }
        currentBasicBlock.insertEnd(new ReturnInstruction(null));
    }

    public IRProgram buildIR(){
        init();
        initBuiltin();
        currentMethod       = new Method(NameMangler.initMethod ,false);
        BasicBlock startBB  = new BasicBlock(currentMethod,null,null, null);
        currentMethod.setStartBlock(startBB);
        currentMethod.setEndBlock(startBB);
        currentBasicBlock = startBB;
        makeEntranceMethod();
        if(startBB.size() == 0) ir.setInitMethod(null);
        else ir.setInitMethod(currentMethod);
        visit(ast);
        return ir;
    }

    @Override
    public Operand visit(Node node) {
        return node.accept(this);
    }

    @Override
    public Operand visit(ProgramNode node) {
       node.getClassDeclarations().forEach(this::visit);
       node.getMethodDeclarations().forEach(this::visit);
       // global variable is processed before
        return null;
    }

    @Override
    public Operand visit(DeclarationNode node) {
        return node.accept(this);
    }


    @Override
    public Operand visit(MethodDeclarationNode node) {
        inMethod                    = true;
        String mangledName          = NameMangler.mangleName(node);
        currentMethod               = ir.getMethod(mangledName);
        currentBasicBlock           = new BasicBlock(currentMethod, null, null, mangledName);
        currentMethod.startBlock    = currentBasicBlock;
        currentMethod.endBlock      = new BasicBlock(currentMethod, null, null, mangledName + ret);
        node.getParameterList().forEach(para ->{
            ir.addLocalVariable(NameMangler.mangleName(para), para.getTypeNode().getType());
            currentMethod.addParameter(para);
        });
        visit(node.getBlock());
        currentBasicBlock.addSucc(currentMethod.endBlock);
        currentBasicBlock   = null;
        currentMethod       = null;
        currentSuperBlock   = null;
        inMethod            = false;
        return null;
    }

    @Override
    public Operand visit(ClassDeclarationNode node) {
        inClass                 = true;
        currentVariableOffset   = 0;
        currentClassSymbolTable = node.getCurrentSymbolTable();
        node.getMemberVariableList().forEach(this::visit);
        node.getMemberMethodList().forEach(this::visit);
        node.getConstructorList().forEach(this::visit);
        inClass                 = false;
        currentClassSymbolTable = null;
        currentVariableOffset   = 0;
        return null;
    }

    @Override
    public Operand visit(VariableDeclarationNode node) {
        MxType t = node.getTypeNode().getType();
        if(inMethod && !inClass) {
            for (VariableDeclaratorNode n : node.getDeclaratorList()) {
                ExpressionNode init     = n.getInitializer();
                Operand initir          = null;
                String mn               = NameMangler.mangleName(n);
                if (n != null) initir   = visit(init);
                VirtualRegister vreg    = new VirtualRegister(n);
                currentMethod.addLocalVariable(mn, vreg);
                currentBasicBlock.insertEnd(new StoreInstruction(vreg, initir, new Immediate(0)));
                ir.addLocalVariable(mn, node.getTypeNode().getType());
            }
        } else if(inClass && !inMethod){
            for(VariableDeclaratorNode decl : node.getDeclaratorList()){
                String mn = NameMangler.mangleName(decl);
                ir.addClassVariable(mn, node.getTypeNode().getType());
                ir.setOffsetInClass(mn, currentVariableOffset);
                currentVariableOffset += Configure.PTR_SIZE;
            }
        }
        return null;
    }

    @Override
    public Operand visit(VariableDeclaratorNode node) {
        return null;
    }

    @Override
    public Operand visit(BlockNode node) {
        node.getStatementList().forEach(this::visit);
        return null;
    }

    @Override
    public Operand visit(StatementNode node) {
        return node.accept(this);
    }

    @Override
    public Operand visit(ConditionNode node) {
        String          mn          = NameMangler.mangleName(node);
        ExpressionNode  astCond     = node.getCond();
        Operand         cond        = null;
        if(astCond != null) cond    = visit(astCond);
        else{
            visit(node.getBody());
            return null;
        }
        if(cond instanceof Immediate){
            if(((Immediate) cond).value == 0){
                ConditionNode astElse = node.getElse();
                if(astElse == null) return null;
                else{
                    visit(astElse);
                    return null;
                }
            } else if(((Immediate) cond).value == 1){
                visit(node.getBody());
                return null;
            }
            else throw new RuntimeException("No such immediate in if!");
        }
        BasicBlock ifTrueBB     = new BasicBlock(currentMethod, currentSuperBlock, currentBasicBlock, mn + ifTrue);
        BasicBlock ifFalseBB    = new BasicBlock(currentMethod, currentSuperBlock, currentBasicBlock, mn + ifFalse);
        BasicBlock ifExitBB     = new BasicBlock(currentMethod, currentSuperBlock, ifFalseBB, mn + after);
        ifExitBB.addPred(ifFalseBB);

        currentBasicBlock.insertEnd(new CompareInstruction(cond, new Immediate(1)));
        currentBasicBlock.insertEnd(new ConditionJumpInstruction(MxStarParser.EQ, ifTrueBB, ifFalseBB));

        currentBasicBlock = ifTrueBB;
        visit(node.getBody());
        currentBasicBlock.insertEnd(new DirectJumpInstruction(ifExitBB));
        currentBasicBlock = ifFalseBB;
        visit(node.getElse());
        currentBasicBlock.insertEnd(new DirectJumpInstruction(ifExitBB));
        currentBasicBlock = ifExitBB;
        return null;
    }

    @Override
    public Operand visit(LoopNode node) {
        LoopBackUper bkp = new LoopBackUper();
        bkp.backup();
        visit(node.getInitializer());
        String mangledLoopName  = NameMangler.mangleName(node);
        LoopSuperBlock loop     = new LoopSuperBlock(currentMethod);
        currentSuperBlock       = loop;
        String nm               = NameMangler.mangleName(node);
        ExpressionNode astCond  = node.getCondition();
        ExpressionNode astStep  = node.getStep();
        Node astBody            = node.getBody();
        BasicBlock beforeLoop   = currentBasicBlock;
        BasicBlock loopCond     = null;
        BasicBlock loopStep     = null;
        BasicBlock loopBodyStart= new BasicBlock(currentMethod, loop,  CondBasicBlockOfCurrentLoop,mangledLoopName + body);
        BasicBlock afterLoopBB  = new BasicBlock(currentMethod, null, currentBasicBlock, mangledLoopName + after);
        Operand    condResult   = null;

        BasicBlockAfterCurrentLoop = afterLoopBB;
        BodyStartBasicBlockOfCurrentLoop = loopBodyStart;
        loop.bodyBBStart = loopBodyStart;

        if(astCond != null){
            loopCond = new BasicBlock(currentMethod, loop, beforeLoop,mangledLoopName + cond);
            CondBasicBlockOfCurrentLoop = loopCond;
            loop.condBB                 = loopCond;
            currentBasicBlock           = loopCond;
            condResult                  = visit(astCond);
        } else{
            loopCond                    = null;
            loop.condBB                 = null;
            CondBasicBlockOfCurrentLoop = loopBodyStart;
            condResult                  = null;
        }

        if(condResult instanceof VirtualRegister){
            currentBasicBlock.insertEnd(new CompareInstruction(condResult, new Immediate(1)));
            currentBasicBlock.insertEnd(new ConditionJumpInstruction(MxStarParser.EQ, BodyStartBasicBlockOfCurrentLoop, BasicBlockAfterCurrentLoop));
            currentBasicBlock.addSucc(BodyStartBasicBlockOfCurrentLoop);
            currentBasicBlock.addSucc(BasicBlockAfterCurrentLoop);
        } else if(condResult instanceof Immediate){
            if(((Immediate) condResult).value == 1){
                beforeLoop.removeSucc(loopCond);
                beforeLoop.addSucc(loopBodyStart);
                loopCond = null;
            }
            else{
                // eliminate always false loop;
                beforeLoop.removeSucc(loopCond);
                beforeLoop.addSucc(afterLoopBB);
                loopCond = null;
                bkp.restore();
                afterLoopBB.superBlockBelongTo = currentSuperBlock;
                return null;
            }
        } else if(astCond == null) loopBodyStart.addPred(beforeLoop);

        currentBasicBlock = loopBodyStart;

        if(astBody != null) visit(astBody);
        DirectJumpInstruction jmp = new DirectJumpInstruction(null);
        currentBasicBlock.insertEnd(jmp);

        if(astStep != null) {
            loopStep = new BasicBlock(currentMethod, loop, currentBasicBlock,mangledLoopName + step);
            loop.stepBB = loopStep;
            StepBasicBlockOfCurrentLoop = loopStep;
            currentBasicBlock.insertEnd(new DirectJumpInstruction(StepBasicBlockOfCurrentLoop));
            currentBasicBlock.addSucc(StepBasicBlockOfCurrentLoop);
            currentBasicBlock = loopStep;
            visit(astStep);
            currentBasicBlock.addSucc(CondBasicBlockOfCurrentLoop);
        } else{
            loopStep = null;
            loop.stepBB = null;
            StepBasicBlockOfCurrentLoop = CondBasicBlockOfCurrentLoop;
            currentBasicBlock.insertEnd(new DirectJumpInstruction(StepBasicBlockOfCurrentLoop));
            currentBasicBlock.addSucc(StepBasicBlockOfCurrentLoop);
        }

        jmp.target = StepBasicBlockOfCurrentLoop;
        jmp.basicBlockBelongTo.addSucc(StepBasicBlockOfCurrentLoop);

        currentMethod.loops.add(loop);
        bkp.restore();
        afterLoopBB.superBlockBelongTo = currentSuperBlock;

        return null;
    }

    @Override
    public Operand visit(JumpNode node) {
        return node.accept(this);
    }

    @Override
    public Operand visit(ContinueNode node) {
        currentBasicBlock.insertEnd(new DirectJumpInstruction(StepBasicBlockOfCurrentLoop));
        currentBasicBlock.addSucc(StepBasicBlockOfCurrentLoop);
        currentBasicBlock = new BasicBlock(currentMethod, currentSuperBlock, null, null);
        return null;
    }

    @Override
    public Operand visit(BreakNode node) {
        currentBasicBlock.insertEnd(new DirectJumpInstruction(BasicBlockAfterCurrentLoop));
        currentBasicBlock.addSucc(BasicBlockAfterCurrentLoop);
        currentBasicBlock = new BasicBlock(currentMethod, currentSuperBlock, null, null);
        return null;
    }

    @Override
    public Operand visit(ReturnNode node) {
        Operand ret = visit(node.getReturnValue());
        ReturnInstruction retInst = new ReturnInstruction(ret);
        currentBasicBlock.insertEnd(retInst);
        currentMethod.returnInsts.add(retInst);
        currentBasicBlock = new BasicBlock(currentMethod, currentSuperBlock, null, null);
        return null;
    }

    @Override
    public Operand visit(DotMemberNode node) {
        ExpressionNode  astExpr     = node.getExpression();
        Operand         expr        = visit(astExpr);
        int             offset      = ir.getOffsetInClass(NameMangler.mangleName(node.getCurrentSymbolTable().get(node.getExpression().getType().toString(), node.getLocation()).getMemberTable().getMember(node.getMember())));
        VirtualRegister loadReg     = currentMethod.allocateNewTmpRegister();
        currentBasicBlock.insertEnd(new LoadInstruction(loadReg, expr, new Immediate(offset)));
        return loadReg;
    }

    @Override
    public Operand visit(MethodCallNode node) {
        String          mmn     = NameMangler.mangleName(node);
        Method          callee  = ir.getMethod(mmn);
        VirtualRegister retReg  = currentMethod.allocateNewTmpRegister();
        CallInstruction call    = new CallInstruction(callee, retReg);
        ArrayList<VirtualRegister> paras = new ArrayList<VirtualRegister>();
        node.getParameterExpressionList().forEach(para -> call.addParameter(visit(para)));
        currentBasicBlock.insertEnd(call);
        return retReg;
    }

    @Override
    public Operand visit(DotMemberMethodCallNode node) {
        ExpressionNode  astExpr         = node.getExpression();
        Operand         classPointer    = visit(astExpr);
        String          mn              = NameMangler.mangleName(node.getCurrentSymbolTable().get(astExpr.getType().toString(), astExpr.getLocation()).getMemberTable().getMember(node.getMemberMethodName()));
        VirtualRegister returnReg       = currentMethod.allocateNewTmpRegister();
        if(!(classPointer instanceof VirtualRegister)) throw new RuntimeException("the dotMemberCall shit!");
        CallInstruction call            = new CallInstruction(ir.getMethod(mn), returnReg);
        node.getParameterExpressionList().forEach(para -> call.addParameter(visit(para)));
        call.setClassMemberCall((VirtualRegister) classPointer);
        currentBasicBlock.insertEnd(call);
        return returnReg;
    }

    @Override
    public Operand visit(ExpressionNode node) {
        return node.accept(this);
    }

    @Override
    public Operand visit(UnaryExpressionNode node) {
        Operand rhsValue = visit(node.getRight());
        if(rhsValue instanceof Immediate){
            int irhs = ((Immediate) rhsValue).value;
            switch (node.getOp()){
                case MxStarParser.INC:
                    return new Immediate(irhs + 1);
                case MxStarParser.DEC:
                    return new Immediate(irhs - 1);
                case MxStarParser.ADD:
                    return new Immediate(irhs);
                case MxStarParser.SUB:
                    return new Immediate(-irhs);
                case MxStarParser.NOT:
                    return new Immediate(irhs == 1 ? 0 : 1);
                case MxStarParser.BITNOT:
                    return new Immediate(~irhs);
                default:
                    throw new RuntimeException("shit in unary expression generation");
            }
        } else if(rhsValue instanceof VirtualRegister){
            currentBasicBlock.insertEnd(new UnaryInstruction(node.getOp(), rhsValue));
            return rhsValue;
        } else throw new RuntimeException("what shit type operand? in unary operand process");
    }

    @Override
    public Operand visit(BinaryExpressionNode node) {
        int      op = node.getOp();
        if(op == MxStarParser.AND || op == MxStarParser.OR) return processLogic(node);
        Operand lhs = visit(node.getLeft());
        Operand rhs = visit(node.getRight());
        if(lhs instanceof Immediate && rhs instanceof Immediate){
            int lval = ((Immediate) rhs).value;
            int rval = ((Immediate) lhs).value;
            switch (op){
                case MxStarParser.MUL:
                    return new Immediate(lval * rval);
                case MxStarParser.DIV:
                    return new Immediate(lval / rval);
                case MxStarParser.MOD:
                    return new Immediate(lval % rval);
                case MxStarParser.ADD:
                    return new Immediate(lval + rval);
                case MxStarParser.SUB:
                    return new Immediate(lval - rval);
                case MxStarParser.SFTL:
                    return new Immediate(lval << rval);
                case MxStarParser.SFTR:
                    return new Immediate(lval >> rval);
                case MxStarParser.GT:
                    return new Immediate(lval > rval ? 1 : 0);
                case MxStarParser.LT:
                    return new Immediate(lval < rval ? 1 : 0);
                case MxStarParser.GE:
                    return new Immediate(lval >= rval ? 1 : 0);
                case MxStarParser.LE:
                    return new Immediate(lval <= rval ? 1 : 0);
                case MxStarParser.EQ:
                    return new Immediate(lval == rval ? 1 : 0);
                case MxStarParser.NEQ:
                    return new Immediate(lval != rval ? 1 : 0);
                case MxStarParser.BITAND:
                    return new Immediate(lval & rval);
                case MxStarParser.BITXOR:
                    return new Immediate(lval ^ rval);
                case MxStarParser.BITOR:
                    return new Immediate(lval | rval);
                default:
                    throw new RuntimeException("what shit operand is this?????!");
            }
        }else if((lhs instanceof Memory || lhs instanceof Address)){
            // TODO String comparasion, should add built in function first!;
            return null;
        } else{
            VirtualRegister tmpReg = currentMethod.allocateNewTmpRegister();
            switch (op){
                case MxStarParser.ASSIGN:
                    currentBasicBlock.insertEnd(new MoveInstruction(lhs, rhs));
                    return lhs;
                default:
                    currentBasicBlock.insertEnd(new BinaryArithmeticInstruction(op, tmpReg, lhs, rhs));
                    return tmpReg;
            }
        }
    }

    @Override
    public Operand visit(NewCreatorNode node) {
        return null;
    }

    @Override
    public Operand visit(ThisNode node) {
        return currentMethod.classThisPointer;
    }

    @Override
    public Operand visit(IndexAccessNode node) {
        Operand         expr        = visit(node.getExpression());
        VirtualRegister loadTarget  = currentMethod.allocateNewTmpRegister();
        currentBasicBlock.insertEnd(new LoadInstruction(loadTarget, expr, new Immediate(0)));
        return loadTarget;
    }

    @Override
    public Operand visit(SuffixIncreaseDecreaseNode node) {
        Operand expr = visit(node.getExpression());
        if(expr instanceof Immediate){
            switch (node.getOp()){
                case MxStarParser.INC:
                    ((Immediate) expr).value++;
                    return expr;
                case MxStarParser.DEC:
                    ((Immediate) expr).value--;
                    return expr;
                default:
                    throw new RuntimeException("What shit suffix INC/DEC instruction is this??????");
            }
        } else {
            switch (node.getOp()) {
                case MxStarParser.INC:
                    currentBasicBlock.insertEnd(new UnaryInstruction(MxStarParser.INC, expr));
                    break;
                case MxStarParser.DEC:
                    currentBasicBlock.insertEnd(new UnaryInstruction(MxStarParser.DEC, expr));
                    break;
                default:
                    throw new RuntimeException("What shit suffix INC/DEC instruction is this??????");
            }
            return expr;
        }
    }

    @Override
    public Operand visit(IdentifierNode node) {
        String nm = NameMangler.mangleName(node.getCurrentSymbolTable().get(node.getIdentifier(), node.getLocation()));
        if(inClass && isClassMemberVariable(node)){
            VirtualRegister classPtr = currentMethod.classThisPointer;
            return new Memory(classPtr, new Immediate(ir.getOffsetInClass(nm)));
        } else{
            Operand ret = currentMethod.localVariables.get(nm);
            if(ret == null) ret = ir.getGlobalVariableVirtualRegister(nm);
            if(ret == null) throw new RuntimeException("there is no shit variable called " + node.getIdentifier() + " there must be something run in the symbol system!");
            return ret;
        }
    }

    @Override
    public Operand visit(ParameterDeclarationNode node) {
        return null;
    }

    @Override
    public Operand visit(BooleanConstantNode node) {
        int val = node.getValue() == true ? 1 : 0;
        return new Immediate(val);
    }

    @Override
    public Operand visit(NullConstantNode node) {
        return new Immediate(0);
    }

    @Override
    public Operand visit(StringConstantNode node) {
        return ir.addStringConstant(node.getValue());
    }

    @Override
    public Operand visit(IntegerConstantNode node) {
        return new Immediate(node.getValue());
    }


    private boolean isClassMemberVariable(IdentifierNode node){
        if(!inClass) return false;
        else{
            Symbol is = node.getCurrentSymbolTable().get(node.getIdentifier(), node.getLocation());
            Symbol cs = currentClassSymbolTable.getMember(node.getIdentifier());
            return is == cs;
        }
    }

    //for short-cut value evaluaiton

    private boolean isLogicOperator(int op){
        if(op == MxStarParser.AND || op == MxStarParser.OR) return true;
        else return false;
    }

    private Operand processLogic(ExpressionNode node){
        if(node instanceof BinaryExpressionNode && isLogicOperator(((BinaryExpressionNode) node).getOp())) {
            ExpressionNode astLeft = ((BinaryExpressionNode) node).getLeft();
            ExpressionNode astRight = ((BinaryExpressionNode) node).getRight();
            int op = ((BinaryExpressionNode) node).getOp();
            if(node.trueJumpTarget == null && node.falseJumpTarget == null) {
                VirtualRegister resultReg = currentMethod.allocateNewTmpRegister();
                BasicBlock trueBB = new BasicBlock(currentMethod, currentSuperBlock, null, currentMethod.generateTmpLabel());
                BasicBlock falseBB = new BasicBlock(currentMethod, currentSuperBlock, null, currentMethod.generateTmpLabel());
                BasicBlock exitBB = new BasicBlock(currentMethod, currentSuperBlock, trueBB, currentMethod.generateTmpLabel());
                exitBB.addPred(falseBB);
                node.trueJumpTarget = trueBB;
                node.falseJumpTarget = falseBB;
                trueBB.insertEnd(new MoveInstruction(resultReg, new Immediate(1)));
                trueBB.insertEnd(new DirectJumpInstruction(exitBB));
                falseBB.insertEnd(new MoveInstruction(resultReg, new Immediate(0)));
                falseBB.insertEnd(new DirectJumpInstruction(exitBB));
                processLogic(node);
                return resultReg;
            }else{
                if(op == MxStarParser.AND){
                    astLeft.falseJumpTarget = node.falseJumpTarget;
                    astLeft.trueJumpTarget = new BasicBlock(currentMethod, currentSuperBlock, null, currentMethod.generateTmpLabel());
                    Operand ret = processLogic(astLeft);
                    if(ret != null){
                        currentBasicBlock.insertEnd(new CompareInstruction(ret, new Immediate(1)));
                        currentBasicBlock.insertEnd(new ConditionJumpInstruction(MxStarParser.EQ, astLeft.trueJumpTarget, astLeft.falseJumpTarget));
                        currentBasicBlock.addSucc(astLeft.falseJumpTarget);
                        currentBasicBlock.addSucc(astLeft.trueJumpTarget);
                    }
                    currentBasicBlock = astLeft.trueJumpTarget;
                }
                else{
                    astLeft.trueJumpTarget = node.trueJumpTarget;
                    astLeft.falseJumpTarget = new BasicBlock(currentMethod, currentSuperBlock, null, currentMethod.generateTmpLabel());
                    Operand ret = processLogic(astLeft);
                    if(ret != null){
                        currentBasicBlock.insertEnd(new CompareInstruction(ret, new Immediate(1)));
                        currentBasicBlock.insertEnd(new ConditionJumpInstruction(MxStarParser.EQ, astLeft.trueJumpTarget, astLeft.falseJumpTarget));
                        currentBasicBlock.addSucc(astLeft.trueJumpTarget);
                        currentBasicBlock.addSucc(astLeft.falseJumpTarget);
                    }
                    currentBasicBlock = astLeft.falseJumpTarget;
                }
                astRight.trueJumpTarget = node.trueJumpTarget;
                astRight.falseJumpTarget = node.falseJumpTarget;
                Operand ret = processLogic(astRight);
                if(ret != null) {
                    currentBasicBlock.insertEnd(new CompareInstruction(ret, new Immediate(1)));
                    currentBasicBlock.insertEnd(new ConditionJumpInstruction(MxStarParser.EQ, astRight.trueJumpTarget, astRight.falseJumpTarget));
                    currentBasicBlock.addSucc(astRight.falseJumpTarget);
                    currentBasicBlock.addSucc(astRight.trueJumpTarget);
                }
                return null;
            }
        } else return visit(node);
    }

}
