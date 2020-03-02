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
package proguard.shrink;

import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.annotation.*;
import proguard.classfile.attribute.module.*;
import proguard.classfile.attribute.module.visitor.*;
import proguard.classfile.attribute.preverification.*;
import proguard.classfile.attribute.preverification.visitor.*;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.*;
import proguard.classfile.instruction.*;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.kotlin.*;
import proguard.classfile.kotlin.visitor.*;
import proguard.classfile.util.*;
import proguard.classfile.visitor.*;
import proguard.fixer.kotlin.KotlinAnnotationCounter;
import proguard.util.*;

import java.util.List;

/**
 * This ClassVisitor, MemberVisitor and KotlinMetadataVisitor recursively marks all classes and class
 * elements that are being used.
 *
 * @see InterfaceUsageMarker
 * @see ClassShrinker
 *
 * @author Eric Lafortune
 */
public class ClassUsageMarker
implements   ClassVisitor,
             MemberVisitor,
             KotlinMetadataVisitor,
             ConstantVisitor,
             AttributeVisitor,
             InnerClassesInfoVisitor,
             ExceptionInfoVisitor,
             StackMapFrameVisitor,
             VerificationTypeVisitor,
             ParameterInfoVisitor,
//             LocalVariableInfoVisitor,
//             LocalVariableTypeInfoVisitor,
//             AnnotationVisitor,
//             ElementValueVisitor,
             RequiresInfoVisitor,
             ExportsInfoVisitor,
             OpensInfoVisitor,
             ProvidesInfoVisitor,
             InstructionVisitor
{
    // A processing info flag to indicate the ProgramMember object is being used,
    // if its Clazz can be determined as being used as well.
    private final Object POSSIBLY_USED = new Object();

    private final SimpleUsageMarker usageMarker;
    private final KotlinUsageMarker kotlinUsageMarker = new KotlinUsageMarker();

    private final ClassVisitor                    interfaceUsageMarker;//           = new MyInterfaceUsageMarker();
    private final MyDefaultMethodUsageMarker      defaultMethodUsageMarker       = new MyDefaultMethodUsageMarker();
    private final MyPossiblyUsedMemberUsageMarker possiblyUsedMemberUsageMarker  = new MyPossiblyUsedMemberUsageMarker();
    private final MemberVisitor                   nonEmptyMethodUsageMarker      = new AllAttributeVisitor(
                                                                                   new MyNonEmptyMethodUsageMarker());
    private final ConstantVisitor                 parameterlessConstructorMarker = new ConstantTagFilter(new int[] { Constant.STRING, Constant.CLASS },
                                                                                   new ReferencedClassVisitor(
                                                                                   new NamedMethodVisitor(ClassConstants.METHOD_NAME_INIT,
                                                                                                          ClassConstants.METHOD_TYPE_INIT,
                                                                                                          this)));
    private ConstantVisitor extraConstantVisitor;
    private MemberVisitor   extraMethodVisitor;


    /**
     * Creates a new UsageMarker. It only marks interfaces if they are
     * really used in the code.
     */
    public ClassUsageMarker()
    {
        this(new SimpleUsageMarker(), false);
    }

    /**
     * Creates a new UsageMarker. It only marks interfaces if they are
     * really used in the code.
     */
    public ClassUsageMarker(SimpleUsageMarker usageMarker)
    {
        this(usageMarker, false);
    }


    /**
     * Creates a new UsageMarker.
     * @param keepAllInterfaces a flag that specifies whether all interfaces
     *                          of kept classes should automatically be kept
     *                          as well.
     */
    public ClassUsageMarker(SimpleUsageMarker usageMarker,
                            boolean           keepAllInterfaces)
    {
        this.usageMarker          = usageMarker;
        this.interfaceUsageMarker = keepAllInterfaces ? this :
            new MyInterfaceUsageMarker();
    }


    /**
     * Returns the SimpleUsageMarker used by this class to mark the individual classes, class members, ...
     */
    public SimpleUsageMarker getUsageMarker()
    {
        return usageMarker;
    }


    /**
     * Sets an optional ConstantVisitor that is invoked for all IntegerConstant
     * and StringConstant instances that are marked.
     */
    public void setExtraConstantVisitor(ConstantVisitor extraConstantVisitor)
    {
        this.extraConstantVisitor = extraConstantVisitor;
    }


    /**
     * Return the optional ConstantVisitor that is invoked for all IntegerConstant
     * and StringConstant instances that are marked.
     */
    public ConstantVisitor getExtraConstantVisitor()
    {
        return extraConstantVisitor;
    }


    /**
     * Sets an optional MemberVisitor that is invoked for all methods
     * that are marked.
     */
    public void setExtraMethodVisitor(MemberVisitor extraMethodVisitor)
    {
        this.extraMethodVisitor = extraMethodVisitor;
    }


    // Implementations for ClassVisitor.

    @Override
    public void visitAnyClass(Clazz clazz)
    {
        throw new UnsupportedOperationException(this.getClass().getName() + " does not support " + clazz.getClass().getName());
    }


    @Override
    public void visitProgramClass(ProgramClass programClass)
    {
        if (shouldBeMarkedAsUsed(programClass))
        {
            // Mark this class.
            markAsUsed(programClass);

            markProgramClassBody(programClass);

            // Mark the Kotlin metadata.
            programClass.accept(new ReferencedKotlinMetadataVisitor(kotlinUsageMarker));
        }
    }


    protected void markProgramClassBody(ProgramClass programClass)
    {
        // Mark this class's name.
        markConstant(programClass, programClass.u2thisClass);

        // Mark the superclass.
        markOptionalConstant(programClass, programClass.u2superClass);

        // Give the interfaces preliminary marks.
        programClass.hierarchyAccept(false, false, true, false,
                                     interfaceUsageMarker);

        // Explicitly mark the <clinit> method, if it's not empty.
        programClass.methodAccept(ClassConstants.METHOD_NAME_CLINIT,
                                  ClassConstants.METHOD_TYPE_CLINIT,
                                  nonEmptyMethodUsageMarker);

        // Process all class members that have already been marked as possibly used.
        programClass.fieldsAccept(possiblyUsedMemberUsageMarker);
        programClass.methodsAccept(possiblyUsedMemberUsageMarker);

        // Mark the attributes.
        programClass.attributesAccept(this);
    }


    @Override
    public void visitLibraryClass(LibraryClass libraryClass)
    {
        if (shouldBeMarkedAsUsed(libraryClass))
        {
            markAsUsed(libraryClass);

            // We're not going to analyze all library code. We're assuming that
            // if this class is being used, all of its methods will be used as
            // well. We'll mark them as such (here and in all subclasses).

            // Mark the superclass.
            Clazz superClass = libraryClass.superClass;
            if (superClass != null)
            {
                superClass.accept(this);
            }

            // Mark the interfaces.
            Clazz[] interfaceClasses = libraryClass.interfaceClasses;
            if (interfaceClasses != null)
            {
                for (int index = 0; index < interfaceClasses.length; index++)
                {
                    if (interfaceClasses[index] != null)
                    {
                        interfaceClasses[index].accept(this);
                    }
                }
            }

            // Mark all methods.
            libraryClass.methodsAccept(this);
        }
    }


    // Implementations for KotlinMetadataVisitor.

    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata)
    {
        // Only grow the Kotlin metadata usage tree for used classes.
        if (isUsed(clazz))
        {
            kotlinMetadata.accept(clazz, kotlinUsageMarker);

//            // Check if any annotations should be kept.
//            kotlinMetadata.accept(clazz,
//                new AllKotlinAnnotationVisitor(kotlinUsageMarker));
//
//            // Check if any types should be kept.
//            kotlinMetadata.accept(clazz,
//                new AllTypeVisitor(kotlinUsageMarker));
        }
    }

    @Override
    public void visitKotlinDeclarationContainerMetadata(Clazz                              clazz,
                                                        KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata)
    {
        // Check whether type aliases have a core type that's kept.
        // Don't go into the specific subtypes of declaration containers,
        // because we're only interested in type aliases and other members
        // won't be marked anyway.
        kotlinUsageMarker.visitKotlinDeclarationContainerMetadata(clazz, kotlinDeclarationContainerMetadata);

        visitAnyKotlinMetadata(clazz, kotlinDeclarationContainerMetadata);
    }


    /**
     * This ClassVisitor marks ProgramClass objects as possibly used,
     * and it visits LibraryClass objects with its outer UsageMarker.
     */
    private class MyInterfaceUsageMarker
    implements    ClassVisitor
    {

        @Override
        public void visitAnyClass(Clazz clazz)
        {
            throw new UnsupportedOperationException(this.getClass().getName() + " does not support " + clazz.getClass().getName());
        }


        @Override
        public void visitProgramClass(ProgramClass programClass)
        {
            if (shouldBeMarkedAsPossiblyUsed(programClass))
            {
                // We can't process the interface yet, because it might not
                // be required. Give it a preliminary mark.
                markAsPossiblyUsed(programClass);
            }
        }


        @Override
        public void visitLibraryClass(LibraryClass libraryClass)
        {
            // Make sure all library interface methods are marked.
            ClassUsageMarker.this.visitLibraryClass(libraryClass);
        }
    }


    /**
     * This MemberVisitor marks ProgramMethod objects of default
     * implementations that may be present in interface classes.
     */
    private class MyDefaultMethodUsageMarker
    implements    MemberVisitor
    {
        // Implementations for MemberVisitor.

        public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
        {
            if (shouldBeMarkedAsUsed(programMethod))
            {
                markAsUsed(programMethod);

                // Mark the method body.
                markProgramMethodBody(programClass, programMethod);

                // Note that, if the method has been marked as possibly used,
                // the method hierarchy has already been marked (cfr. below).
            }
        }
    }


    /**
     * This MemberVisitor marks ProgramField and ProgramMethod objects that
     * have already been marked as possibly used.
     */
    private class MyPossiblyUsedMemberUsageMarker
    implements    MemberVisitor
    {
        // Implementations for MemberVisitor.

        public void visitProgramField(ProgramClass programClass, ProgramField programField)
        {
            // Has the method already been referenced?
            if (isPossiblyUsed(programField))
            {
                markAsUsed(programField);

                // Mark the name and descriptor.
                markConstant(programClass, programField.u2nameIndex);
                markConstant(programClass, programField.u2descriptorIndex);

                // Mark the attributes.
                programField.attributesAccept(programClass, ClassUsageMarker.this);

                // Mark the classes referenced in the descriptor string.
                programField.referencedClassesAccept(ClassUsageMarker.this);
            }
        }


        public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
        {
            // Has the method already been referenced?
            if (isPossiblyUsed(programMethod))
            {
                markAsUsed(programMethod);

                // Mark the method body.
                markProgramMethodBody(programClass, programMethod);

                // Note that, if the method has been marked as possibly used,
                // the method hierarchy has already been marked (cfr. below).
            }
        }
    }


    /**
     * This AttributeVisitor marks ProgramMethod objects of non-empty methods.
     */
    private class MyNonEmptyMethodUsageMarker
    implements    AttributeVisitor
    {
        // Implementations for AttributeVisitor.

        public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


        public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
        {
            if (codeAttribute.u4codeLength > 1)
            {
                method.accept(clazz, ClassUsageMarker.this);
            }
        }
    }


    // Implementations for MemberVisitor.

    public void visitProgramField(ProgramClass programClass, ProgramField programField)
    {
        if (shouldBeMarkedAsUsed(programField))
        {
            // Is the field's class used?
            if (isUsed(programClass))
            {
                markAsUsed(programField);

                // Mark the field body.
                markProgramFieldBody(programClass, programField);
            }

            // Hasn't the field been marked as possibly being used yet?
            else if (shouldBeMarkedAsPossiblyUsed(programClass, programField))
            {
                // We can't process the field yet, because the class isn't
                // marked as being used (yet). Give it a preliminary mark.
                markAsPossiblyUsed(programField);
            }
        }
    }


    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        if (shouldBeMarkedAsUsed(programMethod))
        {
            // Is the method's class used?
            if (isUsed(programClass))
            {
                markAsUsed(programMethod);

                // Mark the method body.
                markProgramMethodBody(programClass, programMethod);

                // Mark the method hierarchy.
                markMethodHierarchy(programClass, programMethod);
            }

            // Hasn't the method been marked as possibly being used yet?
            else if (shouldBeMarkedAsPossiblyUsed(programClass, programMethod))
            {
                // We can't process the method yet, because the class isn't
                // marked as being used (yet). Give it a preliminary mark.
                markAsPossiblyUsed(programMethod);

                // Mark the method hierarchy.
                markMethodHierarchy(programClass, programMethod);
            }
        }
    }


    public void visitLibraryField(LibraryClass programClass, LibraryField programField) {}


    public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod)
    {
        if (shouldBeMarkedAsUsed(libraryMethod))
        {
            markAsUsed(libraryMethod);

            // Mark the method hierarchy.
            markMethodHierarchy(libraryClass, libraryMethod);

            if (extraMethodVisitor != null)
            {
                libraryMethod.accept(libraryClass, extraMethodVisitor);
            }

            markMethodKotlinMetadata(libraryClass, libraryMethod);
        }
    }


    protected void markProgramFieldBody(ProgramClass programClass, ProgramField programField)
    {
        // Mark the name and descriptor.
        markConstant(programClass, programField.u2nameIndex);
        markConstant(programClass, programField.u2descriptorIndex);

        // Mark the attributes.
        programField.attributesAccept(programClass, this);

        // Mark the classes referenced in the descriptor string.
        programField.referencedClassesAccept(this);

        programField.accept(programClass, new MemberToKotlinPropertyVisitor(kotlinUsageMarker));
    }


    protected void markProgramMethodBody(ProgramClass programClass, ProgramMethod programMethod)
    {
        // Mark the name and descriptor.
        markConstant(programClass, programMethod.u2nameIndex);
        markConstant(programClass, programMethod.u2descriptorIndex);

        // Mark the attributes.
        programMethod.attributesAccept(programClass, this);

        // Mark the classes referenced in the descriptor string.
        programMethod.referencedClassesAccept(this);

        if (extraMethodVisitor != null)
        {
            programMethod.accept(programClass, extraMethodVisitor);
        }

        markMethodKotlinMetadata(programClass, programMethod);
    }


    private void markMethodKotlinMetadata(Clazz clazz, Method method)
    {
        if (method.getName(clazz).equals(ClassConstants.METHOD_NAME_INIT))
        {
            method.accept(clazz, new MethodToKotlinConstructorVisitor(kotlinUsageMarker));
        }
        else
        {
            method.accept(clazz, new MultiMemberVisitor(
                                 new MethodToKotlinFunctionVisitor(kotlinUsageMarker),
                                 new MemberToKotlinPropertyVisitor(kotlinUsageMarker)));
        }
    }


    /**
     * Marks the hierarchy of implementing or overriding methods corresponding
     * to the given method, if any.
     */
    protected void markMethodHierarchy(Clazz clazz, Method method)
    {
        // Only visit the hierarchy if the method is not private, static, or
        // an initializer.
        int accessFlags = method.getAccessFlags();
        if ((accessFlags &
             (AccessConstants.PRIVATE |
              AccessConstants.STATIC)) == 0 &&
            !ClassUtil.isInitializer(method.getName(clazz)))
        {
            // We can skip private and static methods in the hierarchy, and
            // also abstract methods, unless they might widen a current
            // non-public access.
            int requiredUnsetAccessFlags =
                AccessConstants.PRIVATE |
                AccessConstants.STATIC  |
                ((accessFlags & AccessConstants.PUBLIC) == 0 ? 0 :
                     AccessConstants.ABSTRACT);

            // Mark default implementations in interfaces down the hierarchy,
            // if this is an interface itself.
            // TODO: This may be premature if there aren't any concrete implementing classes.
            clazz.accept(new ClassAccessFilter(AccessConstants.INTERFACE, 0,
                         new ClassHierarchyTraveler(false, false, false, true,
                         new ProgramClassFilter(
                         new ClassAccessFilter(AccessConstants.INTERFACE, 0,
                         new NamedMethodVisitor(method.getName(clazz),
                                                method.getDescriptor(clazz),
                         new MemberAccessFilter(0, requiredUnsetAccessFlags,
                         defaultMethodUsageMarker)))))));

            // Mark other implementations.
            clazz.accept(new ConcreteClassDownTraveler(
                         new ClassHierarchyTraveler(true, true, false, true,
                         new NamedMethodVisitor(method.getName(clazz),
                                                method.getDescriptor(clazz),
                         new MemberAccessFilter(0, requiredUnsetAccessFlags,
                         this)))));
        }
    }


    // Implementations for ConstantVisitor.

    public void visitIntegerConstant(Clazz clazz, IntegerConstant integerConstant)
    {
        if (shouldBeMarkedAsUsed(integerConstant))
        {
            markAsUsed(integerConstant);

            // Also apply the optional extra constant visitor.
            if (extraConstantVisitor != null)
            {
                extraConstantVisitor.visitIntegerConstant(clazz, integerConstant);
            }
        }
    }


    public void visitLongConstant(Clazz clazz, LongConstant longConstant)
    {
        if (shouldBeMarkedAsUsed(longConstant))
        {
            markAsUsed(longConstant);
        }
    }


    public void visitFloatConstant(Clazz clazz, FloatConstant floatConstant)
    {
        if (shouldBeMarkedAsUsed(floatConstant))
        {
            markAsUsed(floatConstant);
        }
    }


    public void visitDoubleConstant(Clazz clazz, DoubleConstant doubleConstant)
    {
        if (shouldBeMarkedAsUsed(doubleConstant))
        {
            markAsUsed(doubleConstant);
        }
    }


    public void visitPrimitiveArrayConstant(Clazz clazz, PrimitiveArrayConstant primitiveArrayConstant)
    {
        if (shouldBeMarkedAsUsed(primitiveArrayConstant))
        {
            markAsUsed(primitiveArrayConstant);
        }

        // Also apply the optional extra constant visitor.
        if (extraConstantVisitor != null)
        {
            extraConstantVisitor.visitPrimitiveArrayConstant(clazz, primitiveArrayConstant);
        }
    }


    public void visitStringConstant(Clazz clazz, StringConstant stringConstant)
    {
        if (shouldBeMarkedAsUsed(stringConstant))
        {
            markAsUsed(stringConstant);

            markConstant(clazz, stringConstant.u2stringIndex);

            // Mark the referenced class and class member, if any.
            stringConstant.referencedClassAccept(this);
            stringConstant.referencedMemberAccept(this);

            // Also apply the optional extra constant visitor.
            if (extraConstantVisitor != null)
            {
                extraConstantVisitor.visitStringConstant(clazz, stringConstant);
            }
        }
    }


    public void visitUtf8Constant(Clazz clazz, Utf8Constant utf8Constant)
    {
        if (shouldBeMarkedAsUsed(utf8Constant))
        {
            markAsUsed(utf8Constant);
        }
    }


    public void visitDynamicConstant(Clazz clazz, DynamicConstant dynamicConstant)
    {
        if (shouldBeMarkedAsUsed(dynamicConstant))
        {
            markAsUsed(dynamicConstant);

            markConstant(clazz, dynamicConstant.u2nameAndTypeIndex);

            // Mark the referenced descriptor classes.
            dynamicConstant.referencedClassesAccept(this);

            // Mark the bootstrap methods attribute.
            clazz.attributesAccept(new MyBootStrapMethodUsageMarker(dynamicConstant.u2bootstrapMethodAttributeIndex));
        }
    }


    public void visitInvokeDynamicConstant(Clazz clazz, InvokeDynamicConstant invokeDynamicConstant)
    {
        if (shouldBeMarkedAsUsed(invokeDynamicConstant))
        {
            markAsUsed(invokeDynamicConstant);

            markConstant(clazz, invokeDynamicConstant.u2nameAndTypeIndex);

            // Mark the referenced descriptor classes.
            invokeDynamicConstant.referencedClassesAccept(this);

            // Mark the bootstrap methods attribute.
            clazz.attributesAccept(new MyBootStrapMethodUsageMarker(invokeDynamicConstant.u2bootstrapMethodAttributeIndex));
        }
    }


    public void visitMethodHandleConstant(Clazz clazz, MethodHandleConstant methodHandleConstant)
    {
        if (shouldBeMarkedAsUsed(methodHandleConstant))
        {
            markAsUsed(methodHandleConstant);

            markConstant(clazz, methodHandleConstant.u2referenceIndex);
        }
    }


    public void visitAnyRefConstant(Clazz clazz, RefConstant refConstant)
    {
        if (shouldBeMarkedAsUsed(refConstant))
        {
            markAsUsed(refConstant);

            markConstant(clazz, refConstant.u2classIndex);
            markConstant(clazz, refConstant.u2nameAndTypeIndex);

            // When compiled with "-target 1.2" or higher, the class or
            // interface actually containing the referenced class member may
            // be higher up the hierarchy. Make sure it's marked, in case it
            // isn't used elsewhere.
            refConstant.referencedClassAccept(this);

            // Mark the referenced class member itself.
            refConstant.referencedMemberAccept(this);
        }
    }


    public void visitClassConstant(Clazz clazz, ClassConstant classConstant)
    {
        if (shouldBeMarkedAsUsed(classConstant))
        {
            markAsUsed(classConstant);

            markConstant(clazz, classConstant.u2nameIndex);

            // Mark the referenced class itself.
            classConstant.referencedClassAccept(this);
        }
    }


    public void visitMethodTypeConstant(Clazz clazz, MethodTypeConstant methodTypeConstant)
    {
        if (shouldBeMarkedAsUsed(methodTypeConstant))
        {
            markAsUsed(methodTypeConstant);

            markConstant(clazz, methodTypeConstant.u2descriptorIndex);

            // Mark the referenced descriptor classes.
            methodTypeConstant.referencedClassesAccept(this);
        }
    }


    public void visitNameAndTypeConstant(Clazz clazz, NameAndTypeConstant nameAndTypeConstant)
    {
        if (shouldBeMarkedAsUsed(nameAndTypeConstant))
        {
            markAsUsed(nameAndTypeConstant);

            markConstant(clazz, nameAndTypeConstant.u2nameIndex);
            markConstant(clazz, nameAndTypeConstant.u2descriptorIndex);
        }
    }


    public void visitModuleConstant(Clazz clazz, ModuleConstant moduleConstant)
    {
        if (shouldBeMarkedAsUsed(moduleConstant))
        {
            markAsUsed(moduleConstant);

            markConstant(clazz, moduleConstant.u2nameIndex);
        }
    }


    public void visitPackageConstant(Clazz clazz, PackageConstant packageConstant)
    {
        if (shouldBeMarkedAsUsed(packageConstant))
        {
            markAsUsed(packageConstant);

            markConstant(clazz, packageConstant.u2nameIndex);
        }
    }


    /**
     * This AttributeVisitor marks the bootstrap methods attributes, their
     * method entries, their method handles, and their arguments.
     */
    private class MyBootStrapMethodUsageMarker
    implements    AttributeVisitor,
                  BootstrapMethodInfoVisitor
    {
        private int bootstrapMethodIndex;


        private MyBootStrapMethodUsageMarker(int bootstrapMethodIndex)
        {
            this.bootstrapMethodIndex = bootstrapMethodIndex;
        }


        // Implementations for AttributeVisitor.

        public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


        public void visitBootstrapMethodsAttribute(Clazz clazz, BootstrapMethodsAttribute bootstrapMethodsAttribute)
        {
            if (shouldBeMarkedAsUsed(bootstrapMethodsAttribute))
            {
                markAsUsed(bootstrapMethodsAttribute);

                markConstant(clazz, bootstrapMethodsAttribute.u2attributeNameIndex);
            }

            bootstrapMethodsAttribute.bootstrapMethodEntryAccept(clazz,
                                                                 bootstrapMethodIndex,
                                                                 this);
        }


        // Implementations for BootstrapMethodInfoVisitor.

        public void visitBootstrapMethodInfo(Clazz clazz, BootstrapMethodInfo bootstrapMethodInfo)
        {
            markAsUsed(bootstrapMethodInfo);

            markConstant(clazz, bootstrapMethodInfo.u2methodHandleIndex);

            // Mark the constant pool entries referenced by the arguments.
            bootstrapMethodInfo.methodArgumentsAccept(clazz, ClassUsageMarker.this);
        }
    }


    // Implementations for AttributeVisitor.
    // Note that attributes are typically only referenced once, so we don't
    // test if they have been marked already.

    public void visitUnknownAttribute(Clazz clazz, UnknownAttribute unknownAttribute)
    {
        // This is the best we can do for unknown attributes.
        markAsUsed(unknownAttribute);

        markConstant(clazz, unknownAttribute.u2attributeNameIndex);
    }


    public void visitSourceDebugExtensionAttribute(Clazz clazz,
                                                   SourceDebugExtensionAttribute sourceDebugExtensionAttribute)
    {
        markAsUsed(sourceDebugExtensionAttribute);

        markConstant(clazz, sourceDebugExtensionAttribute.u2attributeNameIndex);
    }


    public void visitBootstrapMethodsAttribute(Clazz clazz, BootstrapMethodsAttribute bootstrapMethodsAttribute)
    {
        // Don't mark the attribute and its name here. We may mark it in
        // MyBootStrapMethodsAttributeUsageMarker.
    }


    public void visitSourceFileAttribute(Clazz clazz, SourceFileAttribute sourceFileAttribute)
    {
        markAsUsed(sourceFileAttribute);

        markConstant(clazz, sourceFileAttribute.u2attributeNameIndex);
        markConstant(clazz, sourceFileAttribute.u2sourceFileIndex);
    }


    public void visitSourceDirAttribute(Clazz clazz, SourceDirAttribute sourceDirAttribute)
    {
        markAsUsed(sourceDirAttribute);

        markConstant(clazz, sourceDirAttribute.u2attributeNameIndex);
        markConstant(clazz, sourceDirAttribute.u2sourceDirIndex);
    }


    public void visitInnerClassesAttribute(Clazz clazz, InnerClassesAttribute innerClassesAttribute)
    {
        // Don't mark the attribute and its name yet. We may mark it later, in
        // InnerUsageMarker.
        //markAsUsed(innerClassesAttribute);

        //markConstant(clazz, innerClassesAttribute.u2attrNameIndex);

        // Do mark the outer class entries.
        innerClassesAttribute.innerClassEntriesAccept(clazz, this);
    }


    public void visitEnclosingMethodAttribute(Clazz clazz, EnclosingMethodAttribute enclosingMethodAttribute)
    {
        markAsUsed(enclosingMethodAttribute);

        markConstant(        clazz, enclosingMethodAttribute.u2attributeNameIndex);
        markConstant(        clazz, enclosingMethodAttribute.u2classIndex);
        markOptionalConstant(clazz, enclosingMethodAttribute.u2nameAndTypeIndex);
    }


    public void visitNestHostAttribute(Clazz clazz, NestHostAttribute nestHostAttribute)
    {
        // Don't mark the attribute and its contents yet. We may mark it later,
        // in NestUsageMarker.
        //markAsUsed(nestHostAttribute);

        //markConstant(clazz, nestHostAttribute.u2attributeNameIndex);
        //markConstant(clazz, nestHostAttribute.u2hostClassIndex);
    }


    public void visitNestMembersAttribute(Clazz clazz, NestMembersAttribute nestMembersAttribute)
    {
        // Don't mark the attribute and its contents yet. We may mark it later,
        // in NestUsageMarker.
        //markAsUsed(nestMembersAttribute);

        //markConstant(clazz, nestMembersAttribute.u2attributeNameIndex);

        // Mark the nest member entries.
        //nestMembersAttribute.memberClassConstantsAccept(clazz, this);
    }


    public void visitModuleAttribute(Clazz clazz, ModuleAttribute moduleAttribute)
    {
        markAsUsed(moduleAttribute);

        markConstant(        clazz, moduleAttribute.u2attributeNameIndex);
        markConstant(        clazz, moduleAttribute.u2moduleNameIndex);
        markOptionalConstant(clazz, moduleAttribute.u2moduleVersionIndex);

        // Mark the constant pool entries referenced by the contained info.
        moduleAttribute.requiresAccept(clazz, this);
        moduleAttribute.exportsAccept(clazz, this);
        moduleAttribute.opensAccept(clazz, this);

        markConstants(clazz, moduleAttribute.u2uses, moduleAttribute.u2usesCount);

        // Mark the constant pool entries referenced by the provides info.
        moduleAttribute.providesAccept(clazz, this);
    }


    public void visitModuleMainClassAttribute(Clazz clazz, ModuleMainClassAttribute moduleMainClassAttribute)
    {
        markAsUsed(moduleMainClassAttribute);

        markConstant(clazz, moduleMainClassAttribute.u2attributeNameIndex);
        markConstant(clazz, moduleMainClassAttribute.u2mainClass);
    }


    public void visitModulePackagesAttribute(Clazz clazz, ModulePackagesAttribute modulePackagesAttribute)
    {
        markAsUsed(modulePackagesAttribute);

        markConstant(clazz, modulePackagesAttribute.u2attributeNameIndex);

        // Mark the constant pool entries referenced by the packages info.
        modulePackagesAttribute.packagesAccept(clazz, this);
    }


    public void visitDeprecatedAttribute(Clazz clazz, DeprecatedAttribute deprecatedAttribute)
    {
        markAsUsed(deprecatedAttribute);

        markConstant(clazz, deprecatedAttribute.u2attributeNameIndex);
    }


    public void visitSyntheticAttribute(Clazz clazz, SyntheticAttribute syntheticAttribute)
    {
        markAsUsed(syntheticAttribute);

        markConstant(clazz, syntheticAttribute.u2attributeNameIndex);
    }


    public void visitSignatureAttribute(Clazz clazz, SignatureAttribute signatureAttribute)
    {
        markAsUsed(signatureAttribute);

        markConstant(clazz, signatureAttribute.u2attributeNameIndex);
        markConstant(clazz, signatureAttribute.u2signatureIndex);

        // Don't mark the referenced classes. We'll clean them up in
        // ClassShrinker, if they appear unused.
        //// Mark the classes referenced in the descriptor string.
        //signatureAttribute.referencedClassesAccept(this);
    }


    public void visitConstantValueAttribute(Clazz clazz, Field field, ConstantValueAttribute constantValueAttribute)
    {
        markAsUsed(constantValueAttribute);

        markConstant(clazz, constantValueAttribute.u2attributeNameIndex);
        markConstant(clazz, constantValueAttribute.u2constantValueIndex);
    }


    public void visitMethodParametersAttribute(Clazz clazz, Method method, MethodParametersAttribute methodParametersAttribute)
    {
        markAsUsed(methodParametersAttribute);

        markConstant(clazz, methodParametersAttribute.u2attributeNameIndex);

        // Mark the constant pool entries referenced by the parameter information.
        methodParametersAttribute.parametersAccept(clazz, method, this);
    }


    public void visitExceptionsAttribute(Clazz clazz, Method method, ExceptionsAttribute exceptionsAttribute)
    {
        markAsUsed(exceptionsAttribute);

        markConstant(clazz, exceptionsAttribute.u2attributeNameIndex);

        // Mark the constant pool entries referenced by the exceptions.
        exceptionsAttribute.exceptionEntriesAccept(clazz, this);
    }


    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        markAsUsed(codeAttribute);

        markConstant(clazz, codeAttribute.u2attributeNameIndex);

        // Mark the constant pool entries referenced by the instructions,
        // by the exceptions, and by the attributes.
        codeAttribute.instructionsAccept(clazz, method, this);
        codeAttribute.exceptionsAccept(clazz, method, this);
        codeAttribute.attributesAccept(clazz, method, this);
    }


    public void visitStackMapAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, StackMapAttribute stackMapAttribute)
    {
        markAsUsed(stackMapAttribute);

        markConstant(clazz, stackMapAttribute.u2attributeNameIndex);

        // Mark the constant pool entries referenced by the stack map frames.
        stackMapAttribute.stackMapFramesAccept(clazz, method, codeAttribute, this);
    }


    public void visitStackMapTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, StackMapTableAttribute stackMapTableAttribute)
    {
        markAsUsed(stackMapTableAttribute);

        markConstant(clazz, stackMapTableAttribute.u2attributeNameIndex);

        // Mark the constant pool entries referenced by the stack map frames.
        stackMapTableAttribute.stackMapFramesAccept(clazz, method, codeAttribute, this);
    }


    public void visitLineNumberTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LineNumberTableAttribute lineNumberTableAttribute)
    {
        markAsUsed(lineNumberTableAttribute);

        markConstant(clazz, lineNumberTableAttribute.u2attributeNameIndex);
    }


    public void visitLocalVariableTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTableAttribute localVariableTableAttribute)
    {
        // Don't mark the attribute and its contents yet. We may mark them later,
        // in LocalVariableTypeUsageMarker.
        //markAsUsed(localVariableTableAttribute);
        //
        //markConstant(clazz, localVariableTableAttribute.u2attributeNameIndex);
        //
        //// Mark the constant pool entries referenced by the local variables.
        //localVariableTableAttribute.localVariablesAccept(clazz, method, codeAttribute, this);
    }


    public void visitLocalVariableTypeTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTypeTableAttribute localVariableTypeTableAttribute)
    {
        // Don't mark the attribute and its contents yet. We may mark them later,
        // in LocalVariableTypeUsageMarker.
        //markAsUsed(localVariableTypeTableAttribute);
        //
        //markConstant(clazz, localVariableTypeTableAttribute.u2attributeNameIndex);
        //
        //// Mark the constant pool entries referenced by the local variable types.
        //localVariableTypeTableAttribute.localVariablesAccept(clazz, method, codeAttribute, this);
    }


    public void visitAnyAnnotationsAttribute(Clazz clazz, AnnotationsAttribute annotationsAttribute)
    {
        // Don't mark the attribute and its contents yet. We may mark them later,
        // in AnnotationUsageMarker.
        //markAsUsed(annotationsAttribute);
        //
        //markConstant(clazz, annotationsAttribute.u2attributeNameIndex);
        //
        //// Mark the constant pool entries referenced by the annotations.
        //annotationsAttribute.annotationsAccept(clazz, this);
    }


    public void visitAnyParameterAnnotationsAttribute(Clazz clazz, Method method, ParameterAnnotationsAttribute parameterAnnotationsAttribute)
    {
        // Don't mark the attribute and its contents yet. We may mark them later,
        // in AnnotationUsageMarker.
        //markAsUsed(parameterAnnotationsAttribute);
        //
        //markConstant(clazz, parameterAnnotationsAttribute.u2attributeNameIndex);
        //
        //// Mark the constant pool entries referenced by the annotations.
        //parameterAnnotationsAttribute.annotationsAccept(clazz, method, this);
    }


    public void visitAnnotationDefaultAttribute(Clazz clazz, Method method, AnnotationDefaultAttribute annotationDefaultAttribute)
    {
        // Don't mark the attribute and its contents yet. We may mark them later,
        // in AnnotationUsageMarker.
        //markAsUsed(annotationDefaultAttribute);
        //
        //markConstant(clazz, annotationDefaultAttribute.u2attributeNameIndex);
        //
        //// Mark the constant pool entries referenced by the element value.
        //annotationDefaultAttribute.defaultValueAccept(clazz, this);
    }


    // Implementations for ExceptionInfoVisitor.

    public void visitExceptionInfo(Clazz clazz, Method method, CodeAttribute codeAttribute, ExceptionInfo exceptionInfo)
    {
        markAsUsed(exceptionInfo);

        markOptionalConstant(clazz, exceptionInfo.u2catchType);
    }


    // Implementations for InnerClassesInfoVisitor.

    public void visitInnerClassesInfo(Clazz clazz, InnerClassesInfo innerClassesInfo)
    {
        // At this point, we only mark outer classes of this class.
        // Inner class can be marked later, by InnerUsageMarker.
        if (innerClassesInfo.u2innerClassIndex != 0 &&
            clazz.getName().equals(clazz.getClassName(innerClassesInfo.u2innerClassIndex)))
        {
            markAsUsed(innerClassesInfo);

            // Mark the constant pool entries referenced by the contained info.
            innerClassesInfo.innerClassConstantAccept(clazz, this);
            innerClassesInfo.outerClassConstantAccept(clazz, this);
            innerClassesInfo.innerNameConstantAccept(clazz, this);
        }
    }


    // Implementations for StackMapFrameVisitor.

    public void visitAnyStackMapFrame(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, StackMapFrame stackMapFrame) {}


    public void visitSameOneFrame(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, SameOneFrame sameOneFrame)
    {
        // Mark the constant pool entries referenced by the verification types.
        sameOneFrame.stackItemAccept(clazz, method, codeAttribute, offset, this);
    }


    public void visitMoreZeroFrame(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, MoreZeroFrame moreZeroFrame)
    {
        // Mark the constant pool entries referenced by the verification types.
        moreZeroFrame.additionalVariablesAccept(clazz, method, codeAttribute, offset, this);
    }


    public void visitFullFrame(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, FullFrame fullFrame)
    {
        // Mark the constant pool entries referenced by the verification types.
        fullFrame.variablesAccept(clazz, method, codeAttribute, offset, this);
        fullFrame.stackAccept(clazz, method, codeAttribute, offset, this);
    }


    // Implementations for VerificationTypeVisitor.

    public void visitAnyVerificationType(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, VerificationType verificationType) {}


    public void visitObjectType(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ObjectType objectType)
    {
        markConstant(clazz, objectType.u2classIndex);
    }


    // Implementations for ParameterInfoVisitor.

    public void visitParameterInfo(Clazz clazz, Method method, int parameterIndex, ParameterInfo parameterInfo)
    {
        parameterInfo.nameConstantAccept(clazz, this);
    }


    // Implementations for RequiresInfoVisitor.

    public void visitRequiresInfo(Clazz clazz, RequiresInfo requiresInfo)
    {
        markConstant(        clazz, requiresInfo.u2requiresIndex);
        markOptionalConstant(clazz, requiresInfo.u2requiresVersionIndex);
    }


    // Implementations for ExportsInfoVisitor.

    public void visitExportsInfo(Clazz clazz, ExportsInfo exportsInfo)
    {
        markConstant( clazz,  exportsInfo.u2exportsIndex);
        markConstants(clazz, exportsInfo.u2exportsToIndex, exportsInfo.u2exportsToCount);
    }


    // Implementations for OpensInfoVisitor.

    public void visitOpensInfo(Clazz clazz, OpensInfo opensInfo)
    {
        markConstant( clazz, opensInfo.u2opensIndex);
        markConstants(clazz, opensInfo.u2opensToIndex, opensInfo.u2opensToCount);
    }


    // Implementations for ProvidesInfoVisitor.

    public void visitProvidesInfo(Clazz clazz, ProvidesInfo providesInfo)
    {
        markConstant( clazz, providesInfo.u2providesIndex);
        markConstants(clazz, providesInfo.u2providesWithIndex, providesInfo.u2providesWithCount);
    }


//    // Implementations for LocalVariableInfoVisitor.
//
//    public void visitLocalVariableInfo(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableInfo localVariableInfo)
//    {
//        markConstant(clazz, localVariableInfo.u2nameIndex);
//        markConstant(clazz, localVariableInfo.u2descriptorIndex);
//    }
//
//
//    // Implementations for LocalVariableTypeInfoVisitor.
//
//    public void visitLocalVariableTypeInfo(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTypeInfo localVariableTypeInfo)
//    {
//        markConstant(clazz, localVariableTypeInfo.u2nameIndex);
//        markConstant(clazz, localVariableTypeInfo.u2signatureIndex);
//    }
//
//
//    // Implementations for AnnotationVisitor.
//
//    public void visitAnnotation(Clazz clazz, Annotation annotation)
//    {
//        markConstant(clazz, annotation.u2typeIndex);
//
//        // Mark the constant pool entries referenced by the element values.
//        annotation.elementValuesAccept(clazz, this);
//    }
//
//
//    // Implementations for ElementValueVisitor.
//
//    public void visitConstantElementValue(Clazz clazz, Annotation annotation, ConstantElementValue constantElementValue)
//    {
//        markOptionalConstant(clazz, constantElementValue.u2elementNameIndex);
//        markConstant(        clazz, constantElementValue.u2constantValueIndex);
//    }
//
//
//    public void visitEnumConstantElementValue(Clazz clazz, Annotation annotation, EnumConstantElementValue enumConstantElementValue)
//    {
//        markOptionalConstant(clazz, enumConstantElementValue.u2elementNameIndex);
//        markConstant(        clazz, enumConstantElementValue.u2typeNameIndex);
//        markConstant(        clazz, enumConstantElementValue.u2constantNameIndex);
//    }
//
//
//    public void visitClassElementValue(Clazz clazz, Annotation annotation, ClassElementValue classElementValue)
//    {
//        markOptionalConstant(clazz, classElementValue.u2elementNameIndex);
//
//        // Mark the referenced class constant pool entry.
//        markConstant(clazz, classElementValue.u2classInfoIndex);
//    }
//
//
//    public void visitAnnotationElementValue(Clazz clazz, Annotation annotation, AnnotationElementValue annotationElementValue)
//    {
//        markOptionalConstant(clazz, annotationElementValue.u2elementNameIndex);
//
//        // Mark the constant pool entries referenced by the annotation.
//        annotationElementValue.annotationAccept(clazz, this);
//    }
//
//
//    public void visitArrayElementValue(Clazz clazz, Annotation annotation, ArrayElementValue arrayElementValue)
//    {
//        markOptionalConstant(clazz, arrayElementValue.u2elementNameIndex);
//
//        // Mark the constant pool entries referenced by the element values.
//        arrayElementValue.elementValuesAccept(clazz, annotation, this);
//    }


    // Implementations for InstructionVisitor.

    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction) {}


    public void visitConstantInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction)
    {
        markConstant(clazz, constantInstruction.constantIndex);

        // Also mark the parameterless constructor of the class, in case the
        // string constant or class constant is being used in a Class.forName
        // or a .class construct.
        clazz.constantPoolEntryAccept(constantInstruction.constantIndex,
                                      parameterlessConstructorMarker);
    }


    // Small utility methods.

    /**
     * Marks the given processable as being used.
     */
    public void markAsUsed(Processable processable)
    {
        usageMarker.markAsUsed(processable);
    }


    /**
     * Returns whether the given program class should still be marked as
     * being used.
     */
    public boolean shouldBeMarkedAsUsed(ProgramClass programClass)
    {
        return shouldBeMarkedAsUsed((Processable)programClass);
    }


    /**
     * Returns whether the given program member should still be marked as
     * being used.
     */
    public boolean shouldBeMarkedAsUsed(ProgramClass  programClass,
                                        ProgramMember programMember)
    {
        return shouldBeMarkedAsUsed(programMember);
    }


    /**
     * Returns whether the given processable should still be marked as
     * being used.
     */
    public boolean shouldBeMarkedAsUsed(Processable processable)
    {
        return !isUsed(processable);
    }


    /**
     * Returns whether the given processable has been marked as being used.
     */
    public boolean isUsed(Processable processable)
    {
        return usageMarker.isUsed(processable);
    }


    /**
     * Marks the given processable as possibly being used.
     */
    public void markAsPossiblyUsed(Processable processable)
    {
        usageMarker.markAsPossiblyUsed(processable);
    }


    /**
     * Returns whether the given program member should still be marked as
     * being used.
     */
    public boolean shouldBeMarkedAsPossiblyUsed(ProgramClass  programClass,
                                                ProgramMember programMember)
    {
        return shouldBeMarkedAsPossiblyUsed(programMember);
    }


    /**
     * Returns whether the given processable should still be marked as
     * possibly being used.
     */
    public boolean shouldBeMarkedAsPossiblyUsed(Processable processable)
    {
        return !isUsed(processable) &&
               !isPossiblyUsed(processable);
    }


    /**
     * Returns whether the given processable has been marked as possibly
     * being used.
     */
    public boolean isPossiblyUsed(Processable processable)
    {
        return usageMarker.isPossiblyUsed(processable);
    }


    /**
     * Clears any usage marks from the given processable.
     */
    public void markAsUnused(Processable processable)
    {
        usageMarker.markAsUnused(processable);
    }


    /**
     * Marks the specified constant pool entries of the given class.
     * This includes visiting any referenced objects.
     */
    private void markConstants(Clazz clazz,
                               int[] constantIndices,
                               int   constantIndicesCount)
    {
        for (int index = 0; index < constantIndicesCount; index++)
        {
            markConstant(clazz, constantIndices[index]);
        }
    }


    /**
     * Marks the specified constant pool entry of the given class, if the index
     * is not 0. This includes visiting any referenced objects.
     */
    private void markOptionalConstant(Clazz clazz, int constantIndex)
    {
        if (constantIndex != 0)
        {
            markConstant(clazz, constantIndex);
        }
    }


    /**
     * Marks the specified constant pool entry of the given class.
     * This includes visiting any referenced objects.
     */
    private void markConstant(Clazz clazz, int constantIndex)
    {
        clazz.constantPoolEntryAccept(constantIndex, this);
    }


    public class KotlinUsageMarker
    implements   KotlinMetadataVisitor,

                 // Implementation interfaces.
                 KotlinPropertyVisitor,
                 KotlinFunctionVisitor,
                 KotlinTypeAliasVisitor,
                 KotlinTypeVisitor,
                 KotlinConstructorVisitor,
                 KotlinTypeParameterVisitor,
                 KotlinValueParameterVisitor,
                 KotlinVersionRequirementVisitor,
                 KotlinContractVisitor,
                 KotlinEffectVisitor,
                 KotlinEffectExprVisitor,
                 KotlinAnnotationVisitor
    {


        // Implementations for KotlinMetadataVisitor.
        @Override
        public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata) {}

        @Override
        public void visitKotlinDeclarationContainerMetadata(Clazz clazz, KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata)
        {
            if (!isUsed(kotlinDeclarationContainerMetadata))
            {
                if (isJavaClassUsed(kotlinDeclarationContainerMetadata.ownerReferencedClass))
                {
                    markAsUsed(kotlinDeclarationContainerMetadata);
                }

                kotlinDeclarationContainerMetadata.typeAliasesAccept(clazz, this);
            }

            if (isUsed(kotlinDeclarationContainerMetadata))
            {
                kotlinDeclarationContainerMetadata.propertiesAccept(         clazz, this);
                kotlinDeclarationContainerMetadata.functionsAccept(          clazz, this);
                kotlinDeclarationContainerMetadata.typeAliasesAccept(        clazz, this);
                kotlinDeclarationContainerMetadata.delegatedPropertiesAccept(clazz, this);
            }
        }

        @Override
        public void visitKotlinClassMetadata(Clazz clazz, KotlinClassKindMetadata kotlinClassKindMetadata)
        {
            visitKotlinDeclarationContainerMetadata(clazz, kotlinClassKindMetadata);

            if (isUsed(kotlinClassKindMetadata))
            {
                kotlinClassKindMetadata.referencedClass.accept(ClassUsageMarker.this);

                markAsUsed(kotlinClassKindMetadata.superTypes);
                markAsUsed(kotlinClassKindMetadata.typeParameters);

                if (kotlinClassKindMetadata.flags.isAnnotationClass)
                {
                    // Annotation classes have constructors in the metadata but
                    // no corresponding Java constructors.
                    markAsUsed(kotlinClassKindMetadata.constructors);
                }
                else
                {
                    kotlinClassKindMetadata.constructorsAccept(clazz, this);
                }

                // Mark the INSTANCE field in object classes.
                if (kotlinClassKindMetadata.flags.isObject)
                {
                    clazz.fieldAccept(KotlinConstants.KOTLIN_OBJECT_INSTANCE_FIELD_NAME, null, ClassUsageMarker.this);
                }

                kotlinClassKindMetadata.superTypesAccept(        clazz, this);
                kotlinClassKindMetadata.typeParametersAccept(    clazz, this);
                kotlinClassKindMetadata.versionRequirementAccept(clazz, this);
            }
        }

        @Override
        public void visitKotlinFileFacadeMetadata(Clazz clazz, KotlinFileFacadeKindMetadata kotlinFileFacadeKindMetadata)
        {
            visitKotlinDeclarationContainerMetadata(clazz, kotlinFileFacadeKindMetadata);
        }


        @Override
        public void visitKotlinSyntheticClassMetadata(Clazz                            clazz,
                                                      KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata)
        {
            markAsUsed(kotlinSyntheticClassKindMetadata);
            kotlinSyntheticClassKindMetadata.functionsAccept(clazz, this);
        }

        @Override
        public void visitKotlinMultiFileFacadeMetadata(Clazz                             clazz,
                                                       KotlinMultiFileFacadeKindMetadata kotlinMultiFileFacadeKindMetadata)
        {
        }

        @Override
        public void visitKotlinMultiFilePartMetadata(Clazz                           clazz,
                                                     KotlinMultiFilePartKindMetadata kotlinMultiFilePartKindMetadata)
        {
            visitKotlinDeclarationContainerMetadata(clazz, kotlinMultiFilePartKindMetadata);

            if (isUsed(kotlinMultiFilePartKindMetadata))
            {
                kotlinMultiFilePartKindMetadata.referencedFacadeClass.accept(ClassUsageMarker.this);
            }
        }

        // Implementations for KotlinPropertyVisitor.

        @Override
        public void visitAnyProperty(Clazz                              clazz,
                                     KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                     KotlinPropertyMetadata             kotlinPropertyMetadata)
        {
            if (!isUsed(kotlinPropertyMetadata))
            {
                boolean backingFieldUsed =
                    kotlinPropertyMetadata.referencedBackingField != null &&
                    isUsed(kotlinPropertyMetadata.referencedBackingField);
                boolean getterUsed =
                    kotlinPropertyMetadata.referencedGetterMethod != null &&
                    isUsed(kotlinPropertyMetadata.referencedGetterMethod);
                boolean setterUsed =
                    kotlinPropertyMetadata.referencedSetterMethod != null &&
                    isUsed(kotlinPropertyMetadata.referencedSetterMethod);

                if (backingFieldUsed || getterUsed || setterUsed)
                {
                    markAsUsed(kotlinPropertyMetadata);
                }
            }

            if (isUsed(kotlinPropertyMetadata))
            {
                markAsUsed(kotlinPropertyMetadata.receiverType);
                markAsUsed(kotlinPropertyMetadata.typeParameters);
                markAsUsed(kotlinPropertyMetadata.setterParameters);
                markAsUsed(kotlinPropertyMetadata.type);

                if (kotlinPropertyMetadata.flags.common.hasAnnotations &&
                    kotlinPropertyMetadata.syntheticMethodForAnnotations != null)
                {
                    // Annotations are placed on a synthetic method (e.g. myProperty$annotations())
                    // so we must ensure that the synthetic method is marked as used, if there
                    // are any used annotations there.

                    KotlinAnnotationCounter annotationCounter = new KotlinAnnotationCounter(ClassUsageMarker.this.usageMarker);
                    kotlinPropertyMetadata.referencedSyntheticMethodForAnnotations.accept(
                        kotlinPropertyMetadata.referencedSyntheticMethodClass,
                        annotationCounter
                    );

                    if (annotationCounter.getCount() != 0)
                    {
                        kotlinPropertyMetadata.referencedSyntheticMethodForAnnotations.accept(
                            kotlinPropertyMetadata.referencedSyntheticMethodClass,
                            ClassUsageMarker.this
                        );
                    }
                }

                kotlinPropertyMetadata.receiverTypeAccept(      clazz, kotlinDeclarationContainerMetadata, this);
                kotlinPropertyMetadata.typeParametersAccept(    clazz, kotlinDeclarationContainerMetadata, this);
                kotlinPropertyMetadata.setterParametersAccept(  clazz, kotlinDeclarationContainerMetadata, this);
                kotlinPropertyMetadata.typeAccept(              clazz, kotlinDeclarationContainerMetadata, this);
                kotlinPropertyMetadata.versionRequirementAccept(clazz, kotlinDeclarationContainerMetadata, this);
            }
        }

        // Implementations for KotlinFunctionVisitor.

        @Override
        public void visitAnyFunction(Clazz                  clazz,
                                     KotlinMetadata         kotlinMetadata,
                                     KotlinFunctionMetadata kotlinFunctionMetadata)
        {
            if (!isUsed(kotlinFunctionMetadata))
            {
                if (isUsed(kotlinFunctionMetadata.referencedMethod))
                {
                    markAsUsed(kotlinFunctionMetadata);
                }
            }

            if (isUsed(kotlinFunctionMetadata))
            {
                // Mark the required elements.
                markAsUsed(kotlinFunctionMetadata.receiverType);
                markAsUsed(kotlinFunctionMetadata.typeParameters);
                markAsUsed(kotlinFunctionMetadata.valueParameters);
                markAsUsed(kotlinFunctionMetadata.returnType);
                markAsUsed(kotlinFunctionMetadata.contracts);

                // If there is a corresponding default method and the user specifically kept this method, keep it as well.
                if (kotlinFunctionMetadata.referencedDefaultMethod != null &&
                    (kotlinFunctionMetadata.referencedMethod.getProcessingFlags() & ProcessingFlags.DONT_SHRINK) != 0)
                {
                    kotlinFunctionMetadata.referencedDefaultMethodClass
                        .accept(ClassUsageMarker.this);
                    kotlinFunctionMetadata.referencedDefaultMethod
                        .accept(kotlinFunctionMetadata.referencedDefaultMethodClass,
                                ClassUsageMarker.this);
                }

                kotlinFunctionMetadata.receiverTypeAccept(      clazz, kotlinMetadata, this);
                kotlinFunctionMetadata.typeParametersAccept(    clazz, kotlinMetadata, this);
                kotlinFunctionMetadata.valueParametersAccept(   clazz, kotlinMetadata, this);
                kotlinFunctionMetadata.returnTypeAccept(        clazz, kotlinMetadata, this);
                kotlinFunctionMetadata.contractsAccept(         clazz, kotlinMetadata, this);
                kotlinFunctionMetadata.versionRequirementAccept(clazz, kotlinMetadata, this);
            }
        }

        @Override
        public void visitFunction(Clazz clazz,
                                  KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                  KotlinFunctionMetadata kotlinFunctionMetadata)
        {
            visitAnyFunction(clazz, kotlinDeclarationContainerMetadata, kotlinFunctionMetadata);

            // Non-abstract functions in interfaces should have default implementations, so keep it if the
            // user kept the original function.
            if (isUsed(kotlinFunctionMetadata))
            {
                if (kotlinDeclarationContainerMetadata.k == KotlinConstants.METADATA_KIND_CLASS &&
                    ((KotlinClassKindMetadata)kotlinDeclarationContainerMetadata).flags.isInterface &&
                    !kotlinFunctionMetadata.flags.modality.isAbstract &&
                    (kotlinFunctionMetadata.referencedMethod.getProcessingFlags() & ProcessingFlags.DONT_SHRINK) != 0)
                {
                    kotlinFunctionMetadata.referencedDefaultImplementationMethodClass
                        .accept(ClassUsageMarker.this);
                    kotlinFunctionMetadata.referencedDefaultImplementationMethod
                        .accept(kotlinFunctionMetadata.referencedDefaultImplementationMethodClass,
                                ClassUsageMarker.this);
                }
            }
        }

        // Implementations for KotlinTypeAliasVisitor.

        @Override
        public void visitTypeAlias(Clazz                              clazz,
                                   KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                   KotlinTypeAliasMetadata            kotlinTypeAliasMetadata)
        {
            if (!isUsed(kotlinTypeAliasMetadata))
            {
                // Mark a type alias if its expandedType is used.
                kotlinTypeAliasMetadata.expandedTypeAccept(clazz, kotlinDeclarationContainerMetadata, this);

                if (isUsed(kotlinTypeAliasMetadata.expandedType))
                {
                    markAsUsed(kotlinTypeAliasMetadata);
                }
            }

            if (isUsed(kotlinTypeAliasMetadata))
            {
                clazz.accept(ClassUsageMarker.this);

                markAsUsed(kotlinTypeAliasMetadata.typeParameters);
                markAsUsed(kotlinTypeAliasMetadata.underlyingType);
                markAsUsed(kotlinTypeAliasMetadata.expandedType);

                kotlinTypeAliasMetadata.typeParametersAccept(    clazz, kotlinDeclarationContainerMetadata, this);
                kotlinTypeAliasMetadata.underlyingTypeAccept(    clazz, kotlinDeclarationContainerMetadata, this);
                kotlinTypeAliasMetadata.expandedTypeAccept(      clazz, kotlinDeclarationContainerMetadata, this);
                kotlinTypeAliasMetadata.versionRequirementAccept(clazz, kotlinDeclarationContainerMetadata, this);
            }
        }

        // Implementations for KotlinTypeVisitor.

        @Override
        public void visitAnyType(Clazz clazz, KotlinTypeMetadata kotlinTypeMetadata)
        {
            if (!isUsed(kotlinTypeMetadata))
            {
                if (kotlinTypeMetadata.className != null)
                {
                    if (isJavaClassUsed(kotlinTypeMetadata.referencedClass))
                    {
                        markAsUsed(kotlinTypeMetadata);
                    }
                }
                else if (kotlinTypeMetadata.aliasName != null)
                {
                    kotlinTypeMetadata.referencedTypeAlias.accept(kotlinTypeMetadata.referencedTypeAlias.referencedDeclarationContainer.ownerReferencedClass,
                                                                  kotlinTypeMetadata.referencedTypeAlias.referencedDeclarationContainer,
                                                                  this);

                    if (isUsed(kotlinTypeMetadata.referencedTypeAlias))
                    {
                        markAsUsed(kotlinTypeMetadata);
                    }
                }
                else
                {
                    markAsUsed(kotlinTypeMetadata);
                }
            }

            if (isUsed(kotlinTypeMetadata))
            {
                if (kotlinTypeMetadata.className != null)
                {
                    kotlinTypeMetadata.referencedClass.accept(ClassUsageMarker.this);
                }
                else if (kotlinTypeMetadata.aliasName != null && !isUsed(kotlinTypeMetadata.referencedTypeAlias))
                {
                    markAsUsed(kotlinTypeMetadata.referencedTypeAlias);
                    kotlinTypeMetadata.referencedTypeAlias.accept(null, null, this);
                }

                markAsUsed(kotlinTypeMetadata.typeArguments);
                markAsUsed(kotlinTypeMetadata.upperBounds);
                markAsUsed(kotlinTypeMetadata.outerClassType);

                kotlinTypeMetadata.typeArgumentsAccept(clazz, this);
                kotlinTypeMetadata.upperBoundsAccept(  clazz, this);
                kotlinTypeMetadata.abbreviationAccept( clazz, this);
                kotlinTypeMetadata.annotationsAccept(  clazz, this);
            }
        }

        //Implementations for KotlinConstructorVisitor.

        @Override
        public void visitConstructor(Clazz                     clazz,
                                     KotlinClassKindMetadata   kotlinClassKindMetadata,
                                     KotlinConstructorMetadata kotlinConstructorMetadata)
        {
            if (!isUsed(kotlinConstructorMetadata))
            {
                if (isUsed(kotlinConstructorMetadata.referencedMethod))
                {
                    markAsUsed(kotlinConstructorMetadata);
                }
            }

            if (isUsed(kotlinConstructorMetadata))
            {
                markAsUsed(kotlinConstructorMetadata.valueParameters);

                kotlinConstructorMetadata.valueParametersAccept(  clazz,  kotlinClassKindMetadata, this);
                kotlinConstructorMetadata.versionRequirementAccept(clazz, kotlinClassKindMetadata, this);
            }
        }

        //Implementations for KotlinTypeParameterVisitor.

        @Override
        public void visitAnyTypeParameter(Clazz clazz, KotlinTypeParameterMetadata kotlinTypeParameterMetadata)
        {
            if (isUsed(kotlinTypeParameterMetadata))
            {
                markAsUsed(kotlinTypeParameterMetadata.upperBounds);
                kotlinTypeParameterMetadata.upperBoundsAccept(clazz, this);
                kotlinTypeParameterMetadata.annotationsAccept(clazz, this);
            }
        }

        // Implementations for KotlinValueParameterVisitor.

        @Override
        public void visitAnyValueParameter(Clazz clazz,
                                           KotlinValueParameterMetadata kotlinValueParameterMetadata) {}

        @Override
        public void visitFunctionValParameter(Clazz                        clazz,
                                              KotlinMetadata               kotlinMetadata,
                                              KotlinFunctionMetadata       kotlinFunctionMetadata,
                                              KotlinValueParameterMetadata kotlinValueParameterMetadata)
        {
            if (isUsed(kotlinValueParameterMetadata))
            {
                kotlinValueParameterMetadata.typeAccept(clazz,
                                                        kotlinMetadata,
                                                        kotlinFunctionMetadata,
                                                        this);
            }
        }

        @Override
        public void visitConstructorValParameter(Clazz                        clazz,
                                                 KotlinClassKindMetadata      kotlinClassKindMetadata,
                                                 KotlinConstructorMetadata    kotlinConstructorMetadata,
                                                 KotlinValueParameterMetadata kotlinValueParameterMetadata)
        {
            if (isUsed(kotlinValueParameterMetadata))
            {
                kotlinValueParameterMetadata.typeAccept(clazz,
                                                        kotlinClassKindMetadata,
                                                        kotlinConstructorMetadata,
                                                        this);
            }
        }

        @Override
        public void visitPropertyValParameter(Clazz                              clazz,
                                              KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                              KotlinPropertyMetadata             kotlinPropertyMetadata,
                                              KotlinValueParameterMetadata       kotlinValueParameterMetadata)
        {
            if (isUsed(kotlinValueParameterMetadata))
            {
                kotlinValueParameterMetadata.typeAccept(clazz,
                                                        kotlinDeclarationContainerMetadata,
                                                        kotlinPropertyMetadata,
                                                        this);
            }
        }

        // Implementations for KotlinVersionRequirementVisitor

        @Override
        public void visitAnyVersionRequirement(Clazz                            clazz,
                                               KotlinVersionRequirementMetadata kotlinVersionRequirementMetadata)
        {
            markAsUsed(kotlinVersionRequirementMetadata);
        }

        // Implementations for KotlinAnnotationVisitor.

        @Override
        public void visitAnyAnnotation(Clazz                    clazz,
                                       KotlinMetadataAnnotation annotation)
        {
            if (!isUsed(annotation))
            {
                if (isJavaClassUsed(annotation.referencedAnnotationClass))
                {
                    markAsUsed(annotation);
                }
            }
        }

        // Implementations for KotlinContractVisitor.

        @Override
        public void visitContract(Clazz                  clazz,
                                  KotlinMetadata         kotlinMetadata,
                                  KotlinFunctionMetadata kotlinFunctionMetadata,
                                  KotlinContractMetadata kotlinContractMetadata)
        {
            if (isUsed(kotlinContractMetadata))
            {
                markAsUsed(kotlinContractMetadata.effects);

                kotlinContractMetadata.effectsAccept(clazz, kotlinMetadata, kotlinFunctionMetadata, this);
            }
        }

        // Implementations for KotlinEffectVisitor.

        @Override
        public void visitEffect(Clazz                  clazz,
                                KotlinMetadata         kotlinMetadata,
                                KotlinFunctionMetadata kotlinFunctionMetadata,
                                KotlinContractMetadata kotlinContractMetadata,
                                KotlinEffectMetadata   kotlinEffectMetadata)
        {
            if (isUsed(kotlinEffectMetadata))
            {
                markAsUsed(kotlinEffectMetadata.constructorArguments);
                markAsUsed(kotlinEffectMetadata.conclusionOfConditionalEffect);

                kotlinEffectMetadata.constructorArgumentAccept(clazz, this);
                kotlinEffectMetadata.conclusionOfConditionalEffectAccept(clazz, this);
            }
        }

        // Implementations for KotlinEffectExpressionVisitor.

        @Override
        public void visitAnyEffectExpression(Clazz                          clazz,
                                             KotlinEffectMetadata           kotlinEffectMetadata,
                                             KotlinEffectExpressionMetadata kotlinEffectExpressionMetadata)
        {
            if (!isUsed(kotlinEffectExpressionMetadata))
            {
                markAsUsed(kotlinEffectExpressionMetadata.typeOfIs);
                markAsUsed(kotlinEffectExpressionMetadata.orRightHandSides);
                markAsUsed(kotlinEffectExpressionMetadata.andRightHandSides);

                kotlinEffectExpressionMetadata.typeOfIsAccept(clazz, this);
                kotlinEffectExpressionMetadata.orRightHandSideAccept( clazz, kotlinEffectMetadata, this);
                kotlinEffectExpressionMetadata.andRightHandSideAccept(clazz, kotlinEffectMetadata, this);
            }
        }

        // Small helper methods.

        private void markAsUsed(Processable metadataElement)
        {
            if (metadataElement != null)
            {
                ClassUsageMarker.this.markAsUsed(metadataElement);
            }
        }

        private boolean isJavaClassUsed(Clazz clazz)
        {
            // Because Kotlin dummy classes (see ClassReferenceInitializer) won't be marked as used
            // we must also check the DONT_SHRINK flag.
            return ClassUsageMarker.this.isUsed(clazz) ||
                   ((clazz.getProcessingFlags() & ProcessingFlags.DONT_SHRINK) != 0);
        }

        private void markAsUsed(List<? extends Processable> metadataElements)
        {
            metadataElements.forEach(this::markAsUsed);
        }
    }
}
