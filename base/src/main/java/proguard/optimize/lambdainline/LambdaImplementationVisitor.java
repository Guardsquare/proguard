/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package proguard.optimize.lambdainline;

import proguard.classfile.AccessConstants;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryMethod;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.constant.FieldrefConstant;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.visitor.AllMethodVisitor;
import proguard.classfile.visitor.MemberVisitor;

/**
 * A visitor that can given a field reference used in <code>getstatic</code> for example to obtain a lambda instance
 * find the referenced class and invoke method that contains the lambda implementation.
 */
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
        lambdaImplementationClass.accept(new AllMethodVisitor(new NonBridgeMethodFinder(interfaceClass, programMethod.getDescriptor(interfaceClass))));
    }

    @Override
    public void visitLibraryMethod(LibraryClass interfaceClass, LibraryMethod libraryMethod)
    {
        lambdaImplementationClass.accept(new AllMethodVisitor(new NonBridgeMethodFinder(interfaceClass, libraryMethod.getDescriptor(interfaceClass))));
    }

    public interface InvokeMethodVisitor {
        void visitInvokeMethod(ProgramClass programClass, ProgramMethod programMethod, Clazz interfaceClass, String bridgeDescriptor);
    }

    private class NonBridgeMethodFinder implements MemberVisitor{
        private final Clazz interfaceClazz;
        private final String descriptor;
        public NonBridgeMethodFinder(Clazz interfaceClazz, String descriptor) {
            this.interfaceClazz = interfaceClazz;
            this.descriptor = descriptor;
        }

        @Override
        public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
            if (programMethod.getName(programClass).equals("invoke") && programMethod.u2accessFlags == (AccessConstants.PUBLIC | AccessConstants.FINAL)) {
                invokeMethodVisitor.visitInvokeMethod(programClass, programMethod, interfaceClazz, descriptor);
            }
        }
    }
}
