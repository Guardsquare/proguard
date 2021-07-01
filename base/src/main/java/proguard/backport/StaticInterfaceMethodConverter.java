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
package proguard.backport;

import proguard.classfile.*;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.*;
import proguard.classfile.editor.*;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.visitor.*;
import proguard.classfile.util.*;
import proguard.classfile.visitor.*;
import proguard.io.ExtraDataEntryNameMap;

import java.util.*;

/**
 * This ClassVisitor moves all static interface methods in the visited
 * interfaces to a separate util class and updates all invocations in
 * the program class pool.
 *
 * @author Thomas Neidhart
 */
public class StaticInterfaceMethodConverter
implements   ClassVisitor
{
    private final ClassPool             programClassPool;
    private final ClassPool             libraryClassPool;
    private final ExtraDataEntryNameMap extraDataEntryNameMap;
    private final ClassVisitor          modifiedClassVisitor;
    private final MemberVisitor         extraMemberVisitor;


    public StaticInterfaceMethodConverter(ClassPool             programClassPool,
                                          ClassPool             libraryClassPool,
                                          ExtraDataEntryNameMap extraDataEntryNameMap,
                                          ClassVisitor          modifiedClassVisitor,
                                          MemberVisitor         extraMemberVisitor)
    {
        this.programClassPool      = programClassPool;
        this.libraryClassPool      = libraryClassPool;
        this.extraDataEntryNameMap = extraDataEntryNameMap;
        this.modifiedClassVisitor  = modifiedClassVisitor;
        this.extraMemberVisitor    = extraMemberVisitor;
    }


    // Implementations for ClassVisitor.

    @Override
    public void visitAnyClass(Clazz clazz) { }


    @Override
    public void visitProgramClass(ProgramClass programClass)
    {
        // Collect all static methods of the interface class.
        Set<String> staticMethods = new HashSet<>();
        programClass.accept(
            new AllMethodVisitor(
            new MemberAccessFilter(AccessConstants.STATIC, 0,
            new InitializerMethodFilter(null,
            new MemberCollector(false, true, true, staticMethods)))));

        if (!staticMethods.isEmpty())
        {
            // Create a new utility class.
            ProgramClass utilityClass = new ClassBuilder(
                VersionConstants.CLASS_VERSION_1_2,
                AccessConstants.PUBLIC |
                AccessConstants.SYNTHETIC,
                programClass.getName() + "$$Util",
                ClassConstants.NAME_JAVA_LANG_OBJECT)

                // Add a private constructor.
                .addMethod(
                    AccessConstants.PRIVATE,
                    ClassConstants.METHOD_NAME_INIT,
                    ClassConstants.METHOD_TYPE_INIT,
                    10,
                    code -> code
                        .aload_0()
                        .invokespecial(ClassConstants.NAME_JAVA_LANG_OBJECT,
                                       ClassConstants.METHOD_NAME_INIT,
                                       ClassConstants.METHOD_TYPE_INIT)
                        .return_())

                .getProgramClass();

            // Copy all static interface methods to the utility class.
            MemberVisitor memberAdder = new MemberAdder(utilityClass);
            if (extraMemberVisitor != null)
            {
                memberAdder =
                    new MultiMemberVisitor(
                        memberAdder,
                        extraMemberVisitor
                    );
            }

            MemberRemover memberRemover = new MemberRemover();

            programClass.accept(
                new AllMethodVisitor(
                new MemberAccessFilter(AccessConstants.STATIC, 0,
                new InitializerMethodFilter(null,
                new MultiMemberVisitor(
                    // Add the method to the utility class.
                    memberAdder,

                    // Mark the method for removal from the
                    // interface class.
                    memberRemover
                )
                ))));

            // Add the utility class to the program class pool
            // and the injected class name map.
            programClassPool.addClass(utilityClass);
            extraDataEntryNameMap.addExtraClassToClass(programClass, utilityClass);

            // Change all invokestatic invocations of the static interface
            // methods to use the utility class instead.
            replaceInstructions(programClass, utilityClass, staticMethods);

            // Initialize the hierarchy and references of the utility class.
            utilityClass.accept(
                new MultiClassVisitor(
                    new ClassSuperHierarchyInitializer(programClassPool, libraryClassPool),
                    new ClassReferenceInitializer(programClassPool, libraryClassPool)
                ));

            // Remove the static methods from the interface class and
            // shrink the constant pool of unused constants.
            programClass.accept(
                new MultiClassVisitor(
                    memberRemover,

                    new ConstantPoolShrinker()
                ));
        }
    }


    // Small utility methods.

    /**
     * Replaces all static invocations of the given methods in the given
     * interface class by invocations of copies of these methods in the
     * given utility class.
     */
    private void replaceInstructions(ProgramClass interfaceClass,
                                     ProgramClass utilityClass,
                                     Set<String>  staticMethods)
    {
        InstructionSequenceBuilder ____ =
            new InstructionSequenceBuilder(programClassPool,
                                           libraryClassPool);

        Instruction[][][] instructions =
            new Instruction[staticMethods.size()][][];

        int index = 0;
        for (String staticMethod : staticMethods)
        {
            String[] splitArray = staticMethod.split("\\.");
            String methodName = splitArray[0];
            String methodDesc = splitArray[1];

            Instruction[][] replacement = new Instruction[][]
            {
                ____.invokestatic_interface(interfaceClass.getName(),
                                            methodName,
                                            methodDesc).__(),

                ____.invokestatic(utilityClass.getName(),
                                  methodName,
                                  methodDesc).__(),
            };

            instructions[index++] = replacement;
        }

        CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();

        InstructionVisitor updatedClassVisitor =
            new InstructionToAttributeVisitor(
            new AttributeToClassVisitor(
            modifiedClassVisitor));

        programClassPool.classesAccept(
            new MyReferencedClassFilter(interfaceClass,
            new AllMethodVisitor(
            new AllAttributeVisitor(
            new PeepholeEditor(codeAttributeEditor,
                               new InstructionSequencesReplacer(____.constants(),
                                             instructions,
                                             null,
                                             codeAttributeEditor,
                                             updatedClassVisitor))))));
    }


    /**
     * This ClassVisitor delegates its visits to classes that
     * reference a given class via any RefConstant.
     */
    private static class MyReferencedClassFilter
    implements ClassVisitor,
               ConstantVisitor
    {
        private final Clazz        referencedClass;
        private final ClassVisitor classVisitor;

        private boolean referenceClassFound;

        public MyReferencedClassFilter(Clazz        referencedClass,
                                       ClassVisitor classVisitor)
        {
            this.referencedClass = referencedClass;
            this.classVisitor    = classVisitor;
        }


        // Implementations for ClassVisitor.

        @Override
        public void visitAnyClass(Clazz clazz) { }


        @Override
        public void visitProgramClass(ProgramClass programClass)
        {
            referenceClassFound = false;
            programClass.constantPoolEntriesAccept(this);

            if (referenceClassFound)
            {
                programClass.accept(classVisitor);
            }
        }


        // Implementations for ConstantVisitor.

        public void visitAnyConstant(Clazz clazz, Constant constant) {}


        public void visitAnyRefConstant(Clazz clazz, RefConstant refConstant)
        {
            if (refConstant.referencedClass == referencedClass)
            {
                referenceClassFound = true;
            }
        }
    }
}
