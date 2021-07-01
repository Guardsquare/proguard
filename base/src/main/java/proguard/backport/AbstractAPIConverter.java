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
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.annotation.*;
import proguard.classfile.attribute.annotation.visitor.*;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.*;
import proguard.classfile.editor.*;
import proguard.classfile.instruction.*;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.*;
import proguard.classfile.visitor.*;
import proguard.util.*;

/**
 * Abstract base class for API converter implementations.
 * <p>
 * By default, this class acts as ClassVisitor and will replace any
 * occurrence of the specified methods / types as configured by the
 * actual implementation.
 *
 * @see StreamSupportConverter
 * @see JSR310Converter
 *
 * @author Thomas Neidhart
 */
class      AbstractAPIConverter
implements ClassVisitor,

           // Implementation interfaces.
           MemberVisitor,
           AttributeVisitor,
           InstructionVisitor,
           ConstantVisitor,
           LocalVariableInfoVisitor,
           LocalVariableTypeInfoVisitor,
           AnnotationVisitor,
           ElementValueVisitor
{
    private static final boolean DEBUG = false;

    private final ClassPool          programClassPool;
    private final ClassPool          libraryClassPool;
    private final WarningPrinter     warningPrinter;
    private final ClassVisitor       modifiedClassVisitor;
    private final InstructionVisitor extraInstructionVisitor;

    private       TypeReplacement[]   typeReplacements;
    private       MethodReplacement[] methodReplacements;

    private final CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor(true, true);
    private       ConstantPoolEditor  constantPoolEditor;

    private int     referencingOffset;
    private Method  referencingMethod;
    private boolean classModified;
    private boolean instructionReplaced;


    /**
     * Create a new AbstractAPIConverter instance.
     */
    AbstractAPIConverter(ClassPool          programClassPool,
                         ClassPool          libraryClassPool,
                         WarningPrinter     warningPrinter,
                         ClassVisitor       modifiedClassVisitor,
                         InstructionVisitor extraInstructionVisitor)
    {
        this.programClassPool        = programClassPool;
        this.libraryClassPool        = libraryClassPool;
        this.warningPrinter          = warningPrinter;
        this.modifiedClassVisitor    = modifiedClassVisitor;
        this.extraInstructionVisitor = extraInstructionVisitor;
    }


    protected MethodReplacement replace(String className,
                                        String methodName,
                                        String methodDesc,
                                        String replacementClassName,
                                        String replacementMethodName,
                                        String replacementMethodDesc)
    {
        MethodReplacement methodReplacement =
            new MethodReplacement(className,            methodName,            methodDesc,
                                  replacementClassName, replacementMethodName, replacementMethodDesc);

        return methodReplacement.isValid() ?
            methodReplacement :
            missing(className, methodName, methodDesc);
    }


    protected TypeReplacement replace(String className, String replacementClassName)
    {
        TypeReplacement typeReplacement =
            new TypeReplacement(className, replacementClassName);

        return typeReplacement.isValid() ?
            typeReplacement :
            missing(className);
    }


    protected MethodReplacement missing(String className, String methodName, String methodDesc)
    {
        return new MissingMethodReplacement(className, methodName, methodDesc);
    }


    protected TypeReplacement missing(String className)
    {
        return new MissingTypeReplacement(className);
    }


    protected void setTypeReplacements(TypeReplacement[] replacements)
    {
        this.typeReplacements = replacements;
    }


    protected void setMethodReplacements(MethodReplacement[] replacements)
    {
        this.methodReplacements = replacements;
    }


    // Implementations for ClassVisitor.

    @Override
    public void visitAnyClass(Clazz clazz) {}


    @Override
    public void visitProgramClass(ProgramClass programClass)
    {
        constantPoolEditor = new ConstantPoolEditor(programClass);

        classModified = false;

        // We need to update the code attributes first.
        programClass.methodsAccept(
            new AllAttributeVisitor(
            new AttributeNameFilter(Attribute.CODE,
            this)));

        // Update the class constants directly.
        programClass.constantPoolEntriesAccept(
            new ConstantTagFilter(Constant.CLASS,
            this));

        // Update descriptors and attributes of fields and methods.
        programClass.fieldsAccept(this);
        programClass.methodsAccept(this);

        // Update the class attributes.
        programClass.attributesAccept(this);

        if (classModified)
        {
            // Remove replaced and now unused constant pool entries.
            programClass.accept(new ConstantPoolShrinker());

            if (modifiedClassVisitor != null)
            {
                // Mark this class as being modified.
                modifiedClassVisitor.visitProgramClass(programClass);
            }
        }
    }


    // Implementations for MemberVisitor.

    @Override
    public void visitAnyMember(Clazz clazz, Member member) {}


    @Override
    public void visitProgramField(ProgramClass programClass, ProgramField programField)
    {
        programField.u2descriptorIndex = updateDescriptor(programClass, programField.u2descriptorIndex);

        programField.attributesAccept(programClass, this);
    }


    @Override
    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        programMethod.u2descriptorIndex = updateDescriptor(programClass, programMethod.u2descriptorIndex);

        // Update the remaining attributes, except for the code attribute,
        // which has already been updated.
        programMethod.attributesAccept(programClass,
            new AttributeNameFilter("!" + Attribute.CODE,
            this));
    }


    // Implementations for AttributeVisitor.

    @Override
    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    @Override
    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        codeAttributeEditor.reset(codeAttribute.u4codeLength);

        codeAttribute.instructionsAccept(clazz, method, this);

        if (codeAttributeEditor.isModified())
        {
            codeAttributeEditor.visitCodeAttribute(clazz, method, codeAttribute);
        }

        // Update the nested attributes.
        codeAttribute.attributesAccept(clazz, method, this);
    }


    @Override
    public void visitLocalVariableTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTableAttribute localVariableTableAttribute)
    {
        localVariableTableAttribute.localVariablesAccept(clazz, method, codeAttribute, this);
    }


    @Override
    public void visitLocalVariableTypeTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTypeTableAttribute localVariableTypeTableAttribute)
    {
        localVariableTypeTableAttribute.localVariablesAccept(clazz, method, codeAttribute, this);
    }


    @Override
    public void visitSignatureAttribute(Clazz clazz, SignatureAttribute signatureAttribute)
    {
        signatureAttribute.u2signatureIndex = updateDescriptor(clazz, signatureAttribute.u2signatureIndex);
    }


    @Override
    public void visitAnyAnnotationsAttribute(Clazz clazz, AnnotationsAttribute annotationsAttribute)
    {
        annotationsAttribute.annotationsAccept(clazz, this);
    }


    @Override
    public void visitAnyParameterAnnotationsAttribute(Clazz clazz, Method method, ParameterAnnotationsAttribute parameterAnnotationsAttribute)
    {
        parameterAnnotationsAttribute.annotationsAccept(clazz, method, this);
    }


    @Override
    public void visitAnnotationDefaultAttribute(Clazz clazz, Method method, AnnotationDefaultAttribute annotationDefaultAttribute)
    {
        annotationDefaultAttribute.defaultValueAccept(clazz, this);
    }


    // Implementations for LocalVariableInfoVisitor.

    @Override
    public void visitLocalVariableInfo(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableInfo localVariableInfo)
    {
        localVariableInfo.u2descriptorIndex = updateDescriptor(clazz, localVariableInfo.u2descriptorIndex);
    }


    // Implementations for LocalVariableTypeInfoVisitor.

    @Override
    public void visitLocalVariableTypeInfo(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTypeInfo localVariableTypeInfo)
    {
        localVariableTypeInfo.u2signatureIndex = updateDescriptor(clazz, localVariableTypeInfo.u2signatureIndex);
    }


    // Implementations for AnnotationVisitor.

    @Override
    public void visitAnnotation(Clazz clazz, Annotation annotation)
    {
        annotation.u2typeIndex = updateDescriptor(clazz, annotation.u2typeIndex);

        annotation.elementValuesAccept(clazz, this);
    }


    // Implementations for ElementValueVisitor.

    @Override
    public void visitAnyElementValue(Clazz clazz, Annotation annotation, ElementValue elementValue) {}


    @Override
    public void visitEnumConstantElementValue(Clazz clazz, Annotation annotation, EnumConstantElementValue enumConstantElementValue)
    {
        enumConstantElementValue.u2typeNameIndex = updateDescriptor(clazz, enumConstantElementValue.u2typeNameIndex);
    }


    @Override
    public void visitClassElementValue(Clazz clazz, Annotation annotation, ClassElementValue classElementValue)
    {
        String className    = classElementValue.getClassName(clazz);
        String newClassName = replaceClassName(clazz, className);
        if (!newClassName.equals(className))
        {
            classModified = true;
            classElementValue.u2classInfoIndex = constantPoolEditor.addUtf8Constant(newClassName);
        }
    }


    @Override
    public void visitAnnotationElementValue(Clazz clazz, Annotation annotation, AnnotationElementValue annotationElementValue)
    {
        annotationElementValue.annotationAccept(clazz, this);
    }


    @Override
    public void visitArrayElementValue(Clazz clazz, Annotation annotation, ArrayElementValue arrayElementValue)
    {
        arrayElementValue.elementValuesAccept(clazz, annotation, this);
    }


    // Implementations for InstructionVisitor.

    @Override
    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction) {}


    @Override
    public void visitConstantInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction)
    {
        switch (constantInstruction.opcode)
        {
            case Instruction.OP_INVOKEVIRTUAL:
            case Instruction.OP_INVOKESPECIAL:
            case Instruction.OP_INVOKEINTERFACE:
            case Instruction.OP_INVOKESTATIC:
                this.referencingOffset   = offset;
                this.referencingMethod   = method;
                this.instructionReplaced = false;
                clazz.constantPoolEntryAccept(constantInstruction.constantIndex, this);

                if (instructionReplaced &&
                    extraInstructionVisitor != null)
                {
                    extraInstructionVisitor.visitConstantInstruction(clazz, method, codeAttribute, offset, constantInstruction);
                }
                break;

            case Instruction.OP_PUTFIELD:
            case Instruction.OP_GETFIELD:
            case Instruction.OP_PUTSTATIC:
            case Instruction.OP_GETSTATIC:
                this.referencingOffset   = offset;
                this.referencingMethod   = method;
                this.instructionReplaced = false;
                clazz.constantPoolEntryAccept(constantInstruction.constantIndex, this);

                if (instructionReplaced &&
                    extraInstructionVisitor != null)
                {
                    extraInstructionVisitor.visitConstantInstruction(clazz, method, codeAttribute, offset, constantInstruction);
                }
                break;
        }
    }


    // Implementations for ConstantVisitor.

    @Override
    public void visitAnyConstant(Clazz clazz, Constant constant) {}


    @Override
    public void visitClassConstant(Clazz clazz, ClassConstant classConstant)
    {
        String className    = classConstant.getName(clazz);
        String newClassName = replaceClassName(clazz, className);
        if (!newClassName.equals(className))
        {
            classConstant.u2nameIndex = constantPoolEditor.addUtf8Constant(newClassName);
            classModified = true;
        }
    }


    @Override
    public void visitFieldrefConstant(Clazz clazz, FieldrefConstant fieldrefConstant)
    {
        String name = fieldrefConstant.getName(clazz);
        String desc = fieldrefConstant.getType(clazz);

        String newDesc = replaceDescriptor(clazz, desc);
        if (!newDesc.equals(desc))
        {
            fieldrefConstant.u2nameAndTypeIndex =
                constantPoolEditor.addNameAndTypeConstant(name, newDesc);
            classModified = true;
        }
    }


    @Override
    public void visitAnyMethodrefConstant(Clazz clazz, AnyMethodrefConstant anyMethodrefConstant)
    {
        if (!replaceMethodInvocation(referencingOffset, clazz, referencingMethod, anyMethodrefConstant))
        {
            // If the method invocation was not replaced, we still
            // have to replace the descriptor if necessary.

            String name = anyMethodrefConstant.getName(clazz);
            String desc = anyMethodrefConstant.getType(clazz);

            String newDesc = replaceDescriptor(clazz, desc);
            if (!newDesc.equals(desc))
            {
                anyMethodrefConstant.u2nameAndTypeIndex =
                    constantPoolEditor.addNameAndTypeConstant(name, newDesc);
                classModified = true;
            }
        }
    }


    // Private utility methods.

    /**
     * Checks all the configured type replacements and replaces the given
     * class name accordingly.
     */
    private String replaceClassName(Clazz clazz, String className)
    {
        for (TypeReplacement typeReplacement : typeReplacements)
        {
            String newClassName =
                typeReplacement.matchesClassName(className) ?
                    typeReplacement.replaceClassName(clazz, className) :
                    null;

            if (newClassName != null)
            {
                return newClassName;
            }
        }

        return className;
    }


    /**
     * Replaces all class types that appear in the given descriptor.
     */
    private String replaceDescriptor(Clazz clazz, String descriptor)
    {
        DescriptorClassEnumeration descriptorClassEnumeration =
            new DescriptorClassEnumeration(descriptor);

        StringBuilder newDescriptorBuilder = new StringBuilder(descriptor.length());
        newDescriptorBuilder.append(descriptorClassEnumeration.nextFluff());

        while (descriptorClassEnumeration.hasMoreClassNames())
        {
            String  className        = descriptorClassEnumeration.nextClassName();
            boolean isInnerClassName = descriptorClassEnumeration.isInnerClassName();
            String  fluff            = descriptorClassEnumeration.nextFluff();

            // Strip the outer class name, if it's an inner class.
            if (isInnerClassName)
            {
                className =
                    className.substring(className.lastIndexOf(TypeConstants.INNER_CLASS_SEPARATOR)+1);
            }

            newDescriptorBuilder.append(replaceClassName(clazz, className));
            newDescriptorBuilder.append(fluff);
        }

        return newDescriptorBuilder.toString();
    }


    /**
     * Returns an updated descriptor index if the descriptor
     * has changed.
     */
    private int updateDescriptor(Clazz clazz, int descriptorIndex)
    {
        String descriptor    = clazz.getString(descriptorIndex);
        String newDescriptor = replaceDescriptor(clazz, descriptor);
        if (!newDescriptor.equals(descriptor))
        {
            classModified = true;
            return constantPoolEditor.addUtf8Constant(newDescriptor);
        }
        else
        {
            return descriptorIndex;
        }
    }


    /**
     * Checks if the instruction at the given offset has to be replaced and
     * modifies the code attribute accordingly.
     */
    private boolean replaceMethodInvocation(int offset, Clazz clazz, Method method, AnyMethodrefConstant anyMethodrefConstant)
    {
        for (MethodReplacement methodReplacement : methodReplacements)
        {
            if (methodReplacement.matches(clazz, anyMethodrefConstant))
            {
                methodReplacement.replaceInstruction(offset, clazz, method, anyMethodrefConstant);
                classModified       = true;
                instructionReplaced = true;
                return true;
            }
        }
        return false;
    }


    // Private helper classes.

    /**
     * Abstract base class for type and method replacement helper classes.
     * Contains useful methods to avoid duplication.
     */
    private abstract class AbstractReplacement
    {
        boolean isStatic(Method method)
        {
            return (method.getAccessFlags() & AccessConstants.STATIC) != 0;
        }

        boolean isDefaultMethod(Clazz clazz, Method method)
        {
            return
                isInterface(clazz) &&
                (method.getAccessFlags() & AccessConstants.ABSTRACT) == 0;
        }

        boolean isInterface(Clazz clazz)
        {
            return (clazz.getAccessFlags() & AccessConstants.INTERFACE) != 0;
        }

        Clazz findReferencedClass(String className)
        {
            Clazz clazz = programClassPool.getClass(className);
            return clazz != null ? clazz : libraryClassPool.getClass(className);
        }

        Method findReferencedMethod(Clazz clazz, String methodName, String methodDescriptor)
        {
            return clazz.findMethod(methodName, methodDescriptor);
        }

        String getReplacement(String original, String actual, String replacement)
        {
            if (replacement.contains("<1>"))
            {
                if (original.equals("<static>") ||
                    original.equals("<default>"))
                {
                    return actual;
                }

                int wildcardIndex = original.indexOf("*");
                if (wildcardIndex != -1)
                {
                    String match = actual.substring(wildcardIndex);
                    int replacementIndex = replacement.indexOf("<1>");
                    return replacement.substring(0, replacementIndex) + match;
                }
                else
                {
                    return original;
                }
            }
            else
            {
                return replacement;
            }
        }
    }


    /**
     * A helper class to define a needed method invocation replacement in an efficient way.
     */
    protected class MethodReplacement extends AbstractReplacement
    {
        final String matchingClassName;
        final String matchingMethodName;
        final String matchingMethodDesc;

        final String replacementClassName;
        final String replacementMethodName;
        final String replacementMethodDesc;

        final StringMatcher classNameMatcher;
        final StringMatcher methodNameMatcher;
        final StringMatcher descMatcher;


        MethodReplacement(String className,            String methodName,            String methodDesc,
                          String replacementClassName, String replacementMethodName, String replacementMethodDesc)
        {
            this.matchingClassName  = className;
            this.matchingMethodName = methodName;
            this.matchingMethodDesc = methodDesc;

            this.replacementClassName  = replacementClassName;
            this.replacementMethodName = replacementMethodName;
            this.replacementMethodDesc = replacementMethodDesc;

            classNameMatcher  = new ClassNameParser(null).parse(matchingClassName);
            methodNameMatcher = new NameParser().parse(matchingMethodName);
            descMatcher       = matchingMethodDesc.equals("**") ?
                                    new ConstantMatcher(true) :
                                    new ClassNameParser(null).parse(matchingMethodDesc);
        }


        private boolean isValid()
        {
            return replacementClassName.contains("*")   ||
                   replacementClassName.contains("<1>") ||
                   findReferencedClass(replacementClassName) != null;
        }


        private String getDescReplacement(String original, String actual, String replacement)
        {
            if (matchingMethodName.equals("<default>"))
            {
                // Extend the replacement descriptor.
                String replacedDesc = getReplacement(original, actual, replacement);
                return "(" + ClassUtil.internalTypeFromClassName(matchingClassName) + replacedDesc.substring(1);
            }
            else
            {
                return getReplacement(original, actual, replacement);
            }
        }


        boolean matches(Clazz clazz, AnyMethodrefConstant anyMethodrefConstant)
        {
            String className  = anyMethodrefConstant.getClassName(clazz);
            String methodName = anyMethodrefConstant.getName(clazz);
            String methodDesc = anyMethodrefConstant.getType(clazz);

            // Get the referenced class for the matching className.
            // Might be null for wildcard classNames.
            Clazz referencedMatchingClass = findReferencedClass(matchingClassName);

            Clazz referencedClass = anyMethodrefConstant.referencedClass;

            if (referencedClass == null)
            {
                // Might happen if the project is not setup correctly.
                // The class to be replaced is not present.
                return false;
            }

            Method referencedMethod = anyMethodrefConstant.referencedMethod;

            if (referencedMethod == null)
            {
                // Might happen if the project is not setup correctly.
                // The method to be replaced is not present.
                return false;
            }

            return classPatternMatches(className, referencedClass, referencedMatchingClass) &&
                   methodPatternMatches(methodName, referencedClass, referencedMethod)      &&
                   descPatternMatches(methodDesc);
        }


        private boolean classPatternMatches(String className, Clazz referencedClazz, Clazz referencedMatchingClass)
        {
            return classNameMatcher.matches(className) ||
                   (referencedClazz != null && referencedClazz.extendsOrImplements(referencedMatchingClass));
        }


        private boolean methodPatternMatches(String methodName, Clazz referencedClass, Method referencedMethod)
        {
            return methodNameMatcher.matches(methodName)                                                          ||
                   //  or the method is a default method and the pattern matches all default methods
                   (matchingMethodName.equals("<default>") && isDefaultMethod(referencedClass, referencedMethod)) ||
                   //  or the method is static and the pattern matches all static methods
                   (matchingMethodName.equals("<static>") && isStatic(referencedMethod));
        }


        private boolean descPatternMatches(String methodDesc)
        {
            return descMatcher.matches(methodDesc);
        }


        void replaceInstruction(int offset, Clazz clazz, Method method, AnyMethodrefConstant anyMethodrefConstant)
        {
            String className  =
                getReplacement(matchingClassName,      anyMethodrefConstant.getClassName(clazz), replacementClassName);
            String methodName =
                getReplacement(matchingMethodName,     anyMethodrefConstant.getName(clazz),      replacementMethodName);
            String methodDesc =
                getDescReplacement(matchingMethodDesc, anyMethodrefConstant.getType(clazz),      replacementMethodDesc);

            methodDesc = replaceDescriptor(clazz, methodDesc);

            Clazz referencedClass = findReferencedClass(className);
            if (referencedClass == null)
            {
                // Might happen if the project is not setup correctly.
                // The class to be replaced is not present.
                return;
            }

            Method referencedMethod = findReferencedMethod(referencedClass,
                                                           methodName,
                                                           methodDesc);
            if (referencedMethod == null)
            {
                warningPrinter.print(clazz.getName(),
                                     className,
                                     String.format("Warning: could not find replacement method '%s.%s(%s)',\n" +
                                                   "         not converting method instruction at offset %d " +
                                                   "in method '%s.%s(%s)'.",
                                                   ClassUtil.externalClassName(className),
                                                   methodName,
                                                   ClassUtil.externalMethodArguments(methodDesc),
                                                   offset,
                                                   ClassUtil.externalClassName(clazz.getName()),
                                                   method.getName(clazz),
                                                   ClassUtil.externalMethodArguments(method.getDescriptor(clazz))));
                return;
            }

            boolean isInterfaceMethod         = isInterface(referencedClass);
            byte replacementInstructionOpcode = isStatic(referencedMethod) ?
                Instruction.OP_INVOKESTATIC :
                isInterfaceMethod ?
                    Instruction.OP_INVOKEINTERFACE :
                    Instruction.OP_INVOKEVIRTUAL;

            int methodConstant =
                isInterfaceMethod ?
                    constantPoolEditor.addInterfaceMethodrefConstant(className,
                                                                     methodName,
                                                                     methodDesc,
                                                                     referencedClass,
                                                                     referencedMethod) :
                    constantPoolEditor.addMethodrefConstant(className,
                                                            methodName,
                                                            methodDesc,
                                                            referencedClass,
                                                            referencedMethod);

            codeAttributeEditor.replaceInstruction(offset,
                                                   new ConstantInstruction(replacementInstructionOpcode,
                                                                           methodConstant));

            if (DEBUG)
            {
                System.out.println(String.format("Replacing instruction at offset %d: %s.%s%s -> %s.%s%s",
                                                 offset,
                                                 anyMethodrefConstant.getClassName(clazz),
                                                 anyMethodrefConstant.getName(clazz),
                                                 anyMethodrefConstant.getType(clazz),
                                                 className,
                                                 methodName,
                                                 methodDesc));
            }
        }
    }


    private class MissingMethodReplacement extends MethodReplacement
    {
        MissingMethodReplacement(String className, String methodName, String methodDesc)
        {
            super(className, methodName, methodDesc, null, null, null);
        }


        boolean isValid()
        {
            return false;
        }


        void replaceInstruction(int offset, Clazz clazz, Method method, RefConstant refConstant)
        {
            String className  = refConstant.getClassName(clazz);
            String methodName = refConstant.getName(clazz);
            String methodDesc = refConstant.getType(clazz);

            warningPrinter.print(clazz.getName(),
                                 String.format("Warning: no replacement available for '%s.%s(%s)'\n" +
                                               "         found at offset %d in method '%s.%s(%s)'.",
                                               ClassUtil.externalClassName(className),
                                               methodName,
                                               ClassUtil.externalMethodArguments(methodDesc),
                                               offset,
                                               ClassUtil.externalClassName(clazz.getName()),
                                               method.getName(clazz),
                                               ClassUtil.externalMethodArguments(method.getDescriptor(clazz))));
        }
    }


    /**
     * A helper class to define a needed type replacement in an efficient way.
     */
    protected class TypeReplacement extends AbstractReplacement
    {
        final String        matchingClassName;
        final String        replacementClassName;
        final StringMatcher classNameMatcher;


        TypeReplacement(String matchingClassName, String replacementClassName)
        {
            this.matchingClassName    = matchingClassName;
            this.replacementClassName = replacementClassName;
            this.classNameMatcher     = new ClassNameParser(null).parse(matchingClassName);
        }


        boolean isValid()
        {
            return replacementClassName.contains("*")   ||
                   replacementClassName.contains("<1>") ||
                   findReferencedClass(replacementClassName) != null;
        }


        boolean matchesClassName(String className)
        {
            return classNameMatcher.matches(className);
        }


        String replaceClassName(Clazz clazz, String className)
        {
            return getReplacement(matchingClassName, className, replacementClassName);
        }
    }


    private class MissingTypeReplacement extends TypeReplacement
    {
        MissingTypeReplacement(String className)
        {
            super(className, null);
        }


        boolean isValid()
        {
            return false;
        }


        String replaceClassName(Clazz clazz, String className)
        {
            warningPrinter.print(clazz.getName(),
                                 String.format("Warning: no replacement available for class '%s'\n" +
                                               "         found in class '%s'.",
                                               ClassUtil.externalClassName(className),
                                               ClassUtil.externalClassName(clazz.getName())));

            return className;
        }
    }
}
