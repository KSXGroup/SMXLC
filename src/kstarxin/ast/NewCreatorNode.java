package kstarxin.ast;

import kstarxin.ir.operand.VirtualRegister;
import kstarxin.utilities.Location;
import kstarxin.utilities.MxType;
import kstarxin.utilities.SymbolTable;

import java.util.ArrayList;

public class NewCreatorNode extends ExpressionNode{
    private ArrayList<ExpressionNode>   para;
    private ArrayList<ExpressionNode>   sizeExprList;
    private MxType                      type;
    private int                         dim;

    public NewCreatorNode(MxType _type, SymbolTable stb, Location loc){
        super(false, stb ,loc);
        para                        = null;
        dim                         = 0;
        type                        = _type;
    }

    public NewCreatorNode(MxType _type, ArrayList _para, SymbolTable stb, Location loc){
        super(false,stb,loc);
        para                        = _para;
        dim                         = 0;
        type                        = _type;
    }

    public NewCreatorNode(MxType _type, int _dim,ArrayList _sizeExprList, SymbolTable stb, Location loc){
        super(false, stb, loc);
        sizeExprList                = _sizeExprList;
        para                        = null;
        dim                         = _dim;
        type                        = _type;
    }

    public boolean isArrayCreator(){
        return dim > 0 ? true : false;
    }

    public ArrayList<ExpressionNode> getParameterList(){
        return para;
    }

    public ArrayList<ExpressionNode> getSizeExpressionList(){
        return sizeExprList;
    }

    public int getDimension(){
        return dim;
    }

    public MxType getCreatorType(){
        return type;
    }

    @Override
    public <T> T accept(ASTBaseVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
