package kstarxin.ir.operand;

import kstarxin.ir.IRBaseVisitor;
import kstarxin.ir.asmir.ASMLevelIRVisitor;
import kstarxin.utilities.NameMangler;

public class StringLiteral extends Operand{
    public StringLiteral(String _name){
        super(_name);
        nasmName = NameMangler.convertToASMName(_name);
    }

    @Override
    public String getNASMName() {
        return "[REL " + nasmName + "]";
    }

    @Override
    public String getDisplayName() {
        return nasmName;
    }

    @Override
    public <T> T accept(ASMLevelIRVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Operand accept(IRBaseVisitor visitor) {
        return null;
    }
}
