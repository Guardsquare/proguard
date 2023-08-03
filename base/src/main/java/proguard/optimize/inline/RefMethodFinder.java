package proguard.optimize.inline;

import proguard.classfile.Clazz;
import proguard.classfile.Member;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.instruction.ConstantInstruction;
import proguard.classfile.visitor.MemberVisitor;
import proguard.classfile.visitor.ReferencedMemberVisitor;

public class RefMethodFinder {
    private final Clazz clazz;
    private Method foundMethod;
    public RefMethodFinder(Clazz clazz) {
        this.clazz = clazz;
        this.foundMethod = null;
    }

    public Method findReferencedMethod(ConstantInstruction constantInstruction) {
        clazz.constantPoolEntryAccept(constantInstruction.constantIndex, new ReferencedMemberVisitor(new MemberVisitor() {
            @Override
            public void visitAnyMember(Clazz clazz, Member member) {}

            @Override
            public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
                foundMethod = programMethod;
            }
        }));
        return foundMethod;
    }
}
