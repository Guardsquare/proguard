package proguard.optimize.inline;

import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.visitor.AllAttributeVisitor;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.instruction.ConstantInstruction;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.visitor.AllInstructionVisitor;
import proguard.classfile.instruction.visitor.InstructionOpCodeFilter;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MemberVisitor;

/**
 * A class that searches the entire class pool for calls  to a specific method.
 */
public class MethodUsageFinder implements ClassVisitor, MemberVisitor, InstructionVisitor, ConstantVisitor {
    private final Method targetMethod;
    private final UsingMethodHandler usageHandler;

    public MethodUsageFinder(Method targetMethod, UsingMethodHandler usageHandler) {
        this.targetMethod = targetMethod;
        this.usageHandler = usageHandler;
    }

    @Override
    public void visitAnyClass(Clazz clazz) {}

    @Override
    public void visitProgramClass(ProgramClass programClass) {
        programClass.methodsAccept(this);
    }

    @Override
    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
        programMethod.accept(programClass,
            new AllAttributeVisitor(
            new AllInstructionVisitor(
            new InstructionOpCodeFilter(
                new int[] {
                    Instruction.OP_INVOKESTATIC,
                    Instruction.OP_INVOKEVIRTUAL,
                    Instruction.OP_INVOKESPECIAL
                },
                this
            )))
        );
    }

    @Override
    public void visitConstantInstruction(Clazz consumingCallClazz, Method possibleMethodUser, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction) {
        Method referencedMethod = new RefMethodFinder(consumingCallClazz).findReferencedMethod(constantInstruction);
        if (referencedMethod != null && referencedMethod.equals(targetMethod)) {
            System.out.println("Found a user, method " + possibleMethodUser.getName(consumingCallClazz) + " in class " + consumingCallClazz.getName());
            usageHandler.handle(consumingCallClazz, possibleMethodUser, constantInstruction, offset);
        }
    }

    public interface UsingMethodHandler {
        void handle(Clazz clazz, Method method, ConstantInstruction constantInstruction, int offset);
    }
}
