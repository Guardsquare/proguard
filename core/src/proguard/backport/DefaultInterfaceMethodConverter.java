/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2018 GuardSquare NV
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
package proguard.backport;

import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.*;
import proguard.classfile.editor.*;
import proguard.classfile.instruction.*;
import proguard.classfile.instruction.visitor.*;
import proguard.classfile.util.*;
import proguard.classfile.visitor.*;
import proguard.optimize.peephole.*;
import proguard.util.StringTransformer;

import java.util.*;

/**
 * This ClassVisitor moves all default interface methods in the visited
 * interfaces to concrete implementations.
 *
 * @author Thomas Neidhart
 */
public class DefaultInterfaceMethodConverter
extends      SimplifiedVisitor
implements   ClassVisitor,

             // Implementation interfaces.
             AttributeVisitor
{
    private final ClassVisitor  modifiedClassVisitor;
    private final MemberVisitor extraMemberVisitor;

    // Fields acting as parameters and return values for the visitor methods.

    private final Set<Clazz> implClasses       = new LinkedHashSet<Clazz>();
    private boolean          hasDefaultMethods;


    public DefaultInterfaceMethodConverter(ClassVisitor  modifiedClassVisitor,
                                           MemberVisitor extraMemberVisitor)
    {
        this.modifiedClassVisitor = modifiedClassVisitor;
        this.extraMemberVisitor   = extraMemberVisitor;
    }


    // Implementations for ClassVisitor.

    public void visitLibraryClass(LibraryClass libraryClass) {}


    public void visitProgramClass(ProgramClass programClass)
    {
        hasDefaultMethods = false;
        implClasses.clear();

        // Collect all implementations of the interface.
        programClass.hierarchyAccept(false, false, false, true,
            new ProgramClassFilter(
            new ClassCollector(implClasses)));

        programClass.accept(
            new AllMethodVisitor(
            new MemberAccessFilter(0, ClassConstants.ACC_STATIC,
            new AllAttributeVisitor(this))));

        if (hasDefaultMethods)
        {
            // Shrink the constant pool of unused constants.
            programClass.accept(new ConstantPoolShrinker());
        }
    }


    // Implementations for AttributeVisitor.

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        hasDefaultMethods = true;

        ProgramClass interfaceClass = (ProgramClass) clazz;
        ProgramMethod defaultMethod = (ProgramMethod) method;

        for (Clazz implClass : implClasses)
        {
            ProgramClass targetClass = (ProgramClass) implClass;

            // Add the default method to the implementing class
            // if necessary.
            if (!hasInheritedMethod(targetClass,
                                    defaultMethod.getName(interfaceClass),
                                    defaultMethod.getDescriptor(interfaceClass)))
            {
                defaultMethod.accept(interfaceClass,
                    new MemberAdder(targetClass));

                targetClass.accept(modifiedClassVisitor);
            }

            // Add the default method as a different method and adapt
            // super invocations to it, if necessary.
            if (callsDefaultMethodUsingSuper(targetClass,
                                             interfaceClass,
                                             defaultMethod))
            {
                replaceDefaultMethodInvocation(targetClass,
                                               interfaceClass,
                                               defaultMethod);

                targetClass.accept(modifiedClassVisitor);
            }
        }

        // Remove the code attribute from the method and
        // add make it abstract.
        defaultMethod.accept(interfaceClass,
            new MultiMemberVisitor(
                new NamedAttributeDeleter(ClassConstants.ATTR_Code),

                new MemberAccessFlagSetter(ClassConstants.ACC_ABSTRACT)
            ));

        // Call extra visitor for each visited default method.
        if (extraMemberVisitor != null)
        {
            defaultMethod.accept(interfaceClass, extraMemberVisitor);
        }
    }


    // Small utility methods.

    private boolean hasInheritedMethod(Clazz  clazz,
                                       String methodName,
                                       String methodDescriptor)
    {
        MemberCounter counter = new MemberCounter();

        clazz.hierarchyAccept(true, true, false, false,
            new NamedMethodVisitor(methodName, methodDescriptor,
            counter));

        return counter.getCount() > 0;
    }


    /**
     * Returns true if any method of the given class
     * calls Interface.super.defaultMethod(...).
     */
    private boolean callsDefaultMethodUsingSuper(Clazz  clazz,
                                                 Clazz  interfaceClass,
                                                 Method defaultMethod)
    {
        ConstantCounter counter = new ConstantCounter();

        clazz.accept(
            new AllMethodVisitor(
            new AllAttributeVisitor(
            new AllInstructionVisitor(
            new InvocationInstructionMatcher(interfaceClass,
                                             defaultMethod,
                                             counter)))));

        return counter.getCount() > 0;
    }


    /**
     * Replaces any super calls to the given default interface method
     * in the target class. The default method is copied to the target
     * class and the invoke is updated accordingly.
     */
    private void replaceDefaultMethodInvocation(ProgramClass  targetClass,
                                                ProgramClass  interfaceClass,
                                                ProgramMethod interfaceMethod)
    {
        // Copy the interface method to the target class, with an updated name.
        StringTransformer memberRenamer = new StringTransformer()
        {
            public String transform(String string)
            {
                return "default$" + string;
            }
        };

        interfaceMethod.accept(interfaceClass,
            new MemberAdder(targetClass, memberRenamer, null));

        String targetMethodName =
            memberRenamer.transform(interfaceMethod.getName(interfaceClass));

        // Update invocations of the method inside the target class.
        String descriptor   = interfaceMethod.getDescriptor(interfaceClass);
        Method targetMethod = targetClass.findMethod(targetMethodName, descriptor);

        InstructionSequenceBuilder ____ =
            new InstructionSequenceBuilder();

        Instruction[] patternInstructions =
            ____.invokespecial_interface(interfaceClass,
                                         interfaceMethod).__();

        Instruction[] replacementInstructions =
            ____.invokevirtual(targetClass,
                               targetMethod).__();

        CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();

        Constant[] constants = ____.constants();

        targetClass.accept(
            new AllMethodVisitor(
            new AllAttributeVisitor(
            new PeepholeOptimizer(null, codeAttributeEditor,
            new InstructionSequenceReplacer(constants,
                                            patternInstructions,
                                            constants,
                                            replacementInstructions,
                                            null,
                                            codeAttributeEditor)))));
    }


    /**
     * This InstructionVisitor will call the specified ConstantVisitor
     * for any encountered INVOKESPECIAL instruction whose associated
     * constant is an InterfaceMethodRefConstant and matches the given
     * referenced class and method.
     */
    private static class InvocationInstructionMatcher
    extends    SimplifiedVisitor
    implements InstructionVisitor,
               ConstantVisitor
    {
        private final Clazz           referencedClass;
        private final Method          referencedMethod;
        private final ConstantVisitor constantVisitor;

        public InvocationInstructionMatcher(Clazz           referencedClass,
                                            Method          referencedMethod,
                                            ConstantVisitor constantVisitor)
        {
            this.referencedClass  = referencedClass;
            this.referencedMethod = referencedMethod;
            this.constantVisitor  = constantVisitor;
        }


        // Implementations for InstructionVisitor.

        public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction) {}


        public void visitConstantInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction)
        {
            switch (constantInstruction.opcode)
            {
                case InstructionConstants.OP_INVOKESPECIAL:
                    clazz.constantPoolEntryAccept(constantInstruction.constantIndex, this);
                    break;
            }
        }


        // Implementations for ConstantVisitor.

        public void visitAnyConstant(Clazz clazz, Constant constant) {}


        public void visitInterfaceMethodrefConstant(Clazz clazz, InterfaceMethodrefConstant interfaceMethodrefConstant)
        {
            if (interfaceMethodrefConstant.referencedClass  == referencedClass &&
                interfaceMethodrefConstant.referencedMember == referencedMethod)
            {
                constantVisitor.visitInterfaceMethodrefConstant(clazz, interfaceMethodrefConstant);
            }
        }
    }
}
