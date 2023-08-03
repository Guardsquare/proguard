package proguard.optimize.inline;

import proguard.classfile.*;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.constant.FieldrefConstant;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.visitor.AllMethodVisitor;
import proguard.classfile.visitor.MemberVisitor;

public class LambdaImplementationVisitor implements ConstantVisitor, MemberVisitor {
    private final InvokeMethodVisitor invokeMethodVisitor;
    private Clazz lambdaImplementationClass;
    public LambdaImplementationVisitor(InvokeMethodVisitor invokeMethodHandler) {
        this.invokeMethodVisitor = invokeMethodHandler;
    }

    @Override
    public void visitFieldrefConstant(Clazz clazz, FieldrefConstant fieldrefConstant) {
        lambdaImplementationClass = fieldrefConstant.referencedClass;
        fieldrefConstant.referencedClass.interfaceConstantsAccept(this);
    }

    @Override
    public void visitClassConstant(Clazz clazz, ClassConstant referencedClassConstant) {
        referencedClassConstant.referencedClass.methodsAccept(this);
    }

    @Override
    public void visitProgramMethod(ProgramClass interfaceClass, ProgramMethod programMethod)
    {
        getLambdaImplementation(interfaceClass, programMethod);
    }

    @Override
    public void visitLibraryMethod(LibraryClass interfaceClass, LibraryMethod libraryMethod) {
        getLambdaImplementation(interfaceClass, libraryMethod);
    }

    private void getLambdaImplementation(Clazz interfaceClazz, Method method) {
        String descriptor = method.getDescriptor(interfaceClazz);
        lambdaImplementationClass.methodAccept("invoke", descriptor, new MemberVisitor() {
            @Override
            public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
                System.out.println("Descriptor " + programMethod.getDescriptor(programClass));
                String descriptor = programMethod.getDescriptor(programClass);
                lambdaImplementationClass.accept(new AllMethodVisitor(new MemberVisitor() {
                    @Override
                    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
                        if (programMethod.getName(programClass).equals("invoke") && programMethod.u2accessFlags == (AccessConstants.PUBLIC | AccessConstants.FINAL)) {
                            lambdaImplementationClass.methodAccept("invoke", programMethod.getDescriptor(programClass), new MemberVisitor() {
                                @Override
                                public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
                                    invokeMethodVisitor.visitInvokeMethod(programClass, programMethod, interfaceClazz, descriptor);
                                }
                            });
                        }
                    }
                }));
            }
        });
    }

    public interface InvokeMethodVisitor {
        void visitInvokeMethod(ProgramClass programClass, ProgramMethod programMethod, Clazz interfaceClass, String bridgeDescriptor);
    }
}
