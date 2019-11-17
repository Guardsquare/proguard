/*
 * ProGuard Core -- library to process Java bytecode.
 *
 * Copyright (c) 2002-2019 Guardsquare NV
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package proguard.classfile.attribute.visitor;

import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.annotation.*;
import proguard.classfile.attribute.module.*;
import proguard.classfile.attribute.preverification.*;

/**
 * This AttributeVisitor delegates its visits another AttributeVisitor, but
 * only when the visited attribute is not empty. For instance, a local variable
 * table without variables is empty.
 *
 * @author Eric Lafortune
 */
public class NonEmptyAttributeFilter
implements   AttributeVisitor
{
    private final AttributeVisitor attributeVisitor;


    /**
     * Creates a new NonEmptyAttributeFilter.
     * @param attributeVisitor the <code>AttributeVisitor</code> to which
     *                         visits will be delegated.
     */
    public NonEmptyAttributeFilter(AttributeVisitor attributeVisitor)
    {
        this.attributeVisitor = attributeVisitor;
    }


    // Implementations for AttributeVisitor.

    public void visitUnknownAttribute(Clazz clazz, UnknownAttribute unknownAttribute)
    {
        attributeVisitor.visitUnknownAttribute(clazz, unknownAttribute);
    }


    public void visitBootstrapMethodsAttribute(Clazz clazz, BootstrapMethodsAttribute bootstrapMethodsAttribute)
    {
        if (bootstrapMethodsAttribute.u2bootstrapMethodsCount > 0)
        {
            attributeVisitor.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);
        }
    }


    public void visitSourceFileAttribute(Clazz clazz, SourceFileAttribute sourceFileAttribute)
    {
        attributeVisitor.visitSourceFileAttribute(clazz, sourceFileAttribute);
    }


    public void visitSourceDirAttribute(Clazz clazz, SourceDirAttribute sourceDirAttribute)
    {
        attributeVisitor.visitSourceDirAttribute(clazz, sourceDirAttribute);
    }


    public void visitSourceDebugExtensionAttribute(Clazz                         clazz,
                                                   SourceDebugExtensionAttribute sourceDebugExtensionAttribute)
    {
        attributeVisitor.visitSourceDebugExtensionAttribute(clazz, sourceDebugExtensionAttribute);
    }


    public void visitInnerClassesAttribute(Clazz clazz, InnerClassesAttribute innerClassesAttribute)
    {
        if (innerClassesAttribute.u2classesCount > 0)
        {
            attributeVisitor.visitInnerClassesAttribute(clazz, innerClassesAttribute);
        }
    }


    public void visitEnclosingMethodAttribute(Clazz clazz, EnclosingMethodAttribute enclosingMethodAttribute)
    {
        attributeVisitor.visitEnclosingMethodAttribute(clazz, enclosingMethodAttribute);
    }


    public void visitNestHostAttribute(Clazz clazz, NestHostAttribute nestHostAttribute)
    {
        attributeVisitor.visitNestHostAttribute(clazz, nestHostAttribute);
    }


    public void visitNestMembersAttribute(Clazz clazz, NestMembersAttribute nestMembersAttribute)
    {
        if (nestMembersAttribute.u2classesCount > 0)
        {
            attributeVisitor.visitNestMembersAttribute(clazz, nestMembersAttribute);
        }
    }


    public void visitModuleAttribute(Clazz clazz, ModuleAttribute moduleAttribute)
    {
        if (moduleAttribute.u2requiresCount > 0 ||
            moduleAttribute.u2exportsCount  > 0 ||
            moduleAttribute.u2opensCount    > 0 ||
            moduleAttribute.u2usesCount     > 0 ||
            moduleAttribute.u2providesCount > 0)
        {
            attributeVisitor.visitModuleAttribute(clazz, moduleAttribute);
        }
    }


    public void visitModuleMainClassAttribute(Clazz clazz, ModuleMainClassAttribute moduleMainClassAttribute)
    {
        attributeVisitor.visitModuleMainClassAttribute(clazz, moduleMainClassAttribute);
    }


    public void visitModulePackagesAttribute(Clazz clazz, ModulePackagesAttribute modulePackagesAttribute)
    {
        if (modulePackagesAttribute.u2packagesCount > 0)
        {
            attributeVisitor.visitModulePackagesAttribute(clazz, modulePackagesAttribute);
        }
    }


    public void visitDeprecatedAttribute(Clazz clazz, DeprecatedAttribute deprecatedAttribute)
    {
        attributeVisitor.visitDeprecatedAttribute(clazz, deprecatedAttribute);
    }


    public void visitDeprecatedAttribute(Clazz clazz, Field field, DeprecatedAttribute deprecatedAttribute)
    {
        attributeVisitor.visitDeprecatedAttribute(clazz, field, deprecatedAttribute);
    }


    public void visitDeprecatedAttribute(Clazz clazz, Method method, DeprecatedAttribute deprecatedAttribute)
    {
        attributeVisitor.visitDeprecatedAttribute(clazz, method, deprecatedAttribute);
    }


    public void visitSyntheticAttribute(Clazz clazz, SyntheticAttribute syntheticAttribute)
    {
        attributeVisitor.visitSyntheticAttribute(clazz, syntheticAttribute);
    }


    public void visitSyntheticAttribute(Clazz clazz, Field field, SyntheticAttribute syntheticAttribute)
    {
        attributeVisitor.visitSyntheticAttribute(clazz, field, syntheticAttribute);
    }


    public void visitSyntheticAttribute(Clazz clazz, Method method, SyntheticAttribute syntheticAttribute)
    {
        attributeVisitor.visitSyntheticAttribute(clazz, method, syntheticAttribute);
    }


    public void visitSignatureAttribute(Clazz clazz, SignatureAttribute signatureAttribute)
    {
        attributeVisitor.visitSignatureAttribute(clazz, signatureAttribute);
    }


    public void visitSignatureAttribute(Clazz clazz, Field field, SignatureAttribute signatureAttribute)
    {
        attributeVisitor.visitSignatureAttribute(clazz, field, signatureAttribute);
    }


    public void visitSignatureAttribute(Clazz clazz, Method method, SignatureAttribute signatureAttribute)
    {
        attributeVisitor.visitSignatureAttribute(clazz, method, signatureAttribute);
    }


    public void visitConstantValueAttribute(Clazz clazz, Field field, ConstantValueAttribute constantValueAttribute)
    {
        attributeVisitor.visitConstantValueAttribute(clazz, field, constantValueAttribute);
    }


    public void visitMethodParametersAttribute(Clazz clazz, Method method, MethodParametersAttribute exceptionsAttribute)
    {
        if (exceptionsAttribute.u1parametersCount > 0)
        {
            attributeVisitor.visitMethodParametersAttribute(clazz, method, exceptionsAttribute);
        }
    }


    public void visitExceptionsAttribute(Clazz clazz, Method method, ExceptionsAttribute exceptionsAttribute)
    {
        if (exceptionsAttribute.u2exceptionIndexTableLength > 0)
        {
            attributeVisitor.visitExceptionsAttribute(clazz, method, exceptionsAttribute);
        }
    }


    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        attributeVisitor.visitCodeAttribute(clazz, method, codeAttribute);
    }


    public void visitStackMapAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, StackMapAttribute stackMapAttribute)
    {
        if (stackMapAttribute.u2stackMapFramesCount > 0)
        {
            attributeVisitor.visitStackMapAttribute(clazz, method, codeAttribute, stackMapAttribute);
        }
    }


    public void visitStackMapTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, StackMapTableAttribute stackMapTableAttribute)
    {
        if (stackMapTableAttribute.u2stackMapFramesCount > 0)
        {
            attributeVisitor.visitStackMapTableAttribute(clazz, method, codeAttribute, stackMapTableAttribute);
        }
    }


    public void visitLineNumberTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LineNumberTableAttribute lineNumberTableAttribute)
    {
        if (lineNumberTableAttribute.u2lineNumberTableLength > 0)
        {
            attributeVisitor.visitLineNumberTableAttribute(clazz, method, codeAttribute, lineNumberTableAttribute);
        }
    }


    public void visitLocalVariableTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTableAttribute localVariableTableAttribute)
    {
        if (localVariableTableAttribute.u2localVariableTableLength > 0)
        {
            attributeVisitor.visitLocalVariableTableAttribute(clazz, method, codeAttribute, localVariableTableAttribute);
        }
    }


    public void visitLocalVariableTypeTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTypeTableAttribute localVariableTypeTableAttribute)
    {
        if (localVariableTypeTableAttribute.u2localVariableTypeTableLength > 0)
        {
            attributeVisitor.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, localVariableTypeTableAttribute);
        }
    }


    public void visitRuntimeVisibleAnnotationsAttribute(Clazz clazz, RuntimeVisibleAnnotationsAttribute runtimeVisibleAnnotationsAttribute)
    {
        if (runtimeVisibleAnnotationsAttribute.u2annotationsCount > 0)
        {
            attributeVisitor.visitRuntimeVisibleAnnotationsAttribute(clazz, runtimeVisibleAnnotationsAttribute);
        }
    }


    public void visitRuntimeVisibleAnnotationsAttribute(Clazz clazz, Field field, RuntimeVisibleAnnotationsAttribute runtimeVisibleAnnotationsAttribute)
    {
        if (runtimeVisibleAnnotationsAttribute.u2annotationsCount > 0)
        {
            attributeVisitor.visitRuntimeVisibleAnnotationsAttribute(clazz, field, runtimeVisibleAnnotationsAttribute);
        }
    }


    public void visitRuntimeVisibleAnnotationsAttribute(Clazz clazz, Method method, RuntimeVisibleAnnotationsAttribute runtimeVisibleAnnotationsAttribute)
    {
        if (runtimeVisibleAnnotationsAttribute.u2annotationsCount > 0)
        {
            attributeVisitor.visitRuntimeVisibleAnnotationsAttribute(clazz, method, runtimeVisibleAnnotationsAttribute);
        }
    }


    public void visitRuntimeInvisibleAnnotationsAttribute(Clazz clazz, RuntimeInvisibleAnnotationsAttribute runtimeInvisibleAnnotationsAttribute)
    {
        if (runtimeInvisibleAnnotationsAttribute.u2annotationsCount > 0)
        {
            attributeVisitor.visitRuntimeInvisibleAnnotationsAttribute(clazz, runtimeInvisibleAnnotationsAttribute);
        }
    }


    public void visitRuntimeInvisibleAnnotationsAttribute(Clazz clazz, Field field, RuntimeInvisibleAnnotationsAttribute runtimeInvisibleAnnotationsAttribute)
    {
        if (runtimeInvisibleAnnotationsAttribute.u2annotationsCount > 0)
        {
            attributeVisitor.visitRuntimeInvisibleAnnotationsAttribute(clazz, field, runtimeInvisibleAnnotationsAttribute);
        }
    }


    public void visitRuntimeInvisibleAnnotationsAttribute(Clazz clazz, Method method, RuntimeInvisibleAnnotationsAttribute runtimeInvisibleAnnotationsAttribute)
    {
        if (runtimeInvisibleAnnotationsAttribute.u2annotationsCount > 0)
        {
            attributeVisitor.visitRuntimeInvisibleAnnotationsAttribute(clazz, method, runtimeInvisibleAnnotationsAttribute);
        }
    }


    public void visitRuntimeVisibleParameterAnnotationsAttribute(Clazz clazz, Method method, RuntimeVisibleParameterAnnotationsAttribute runtimeVisibleParameterAnnotationsAttribute)
    {
        if (runtimeVisibleParameterAnnotationsAttribute.u1parametersCount > 0)
        {
            attributeVisitor.visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, runtimeVisibleParameterAnnotationsAttribute);
        }
    }


    public void visitRuntimeInvisibleParameterAnnotationsAttribute(Clazz clazz, Method method, RuntimeInvisibleParameterAnnotationsAttribute runtimeInvisibleParameterAnnotationsAttribute)
    {
        if (runtimeInvisibleParameterAnnotationsAttribute.u1parametersCount > 0)
        {
            attributeVisitor.visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, runtimeInvisibleParameterAnnotationsAttribute);
        }
    }


    public void visitRuntimeVisibleTypeAnnotationsAttribute(Clazz clazz, RuntimeVisibleTypeAnnotationsAttribute runtimeVisibleTypeAnnotationsAttribute)
    {
        if (runtimeVisibleTypeAnnotationsAttribute.u2annotationsCount > 0)
        {
            attributeVisitor.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, runtimeVisibleTypeAnnotationsAttribute);
        }
    }


    public void visitRuntimeVisibleTypeAnnotationsAttribute(Clazz clazz, Field field, RuntimeVisibleTypeAnnotationsAttribute runtimeVisibleTypeAnnotationsAttribute)
    {
        if (runtimeVisibleTypeAnnotationsAttribute.u2annotationsCount > 0)
        {
            attributeVisitor.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, field, runtimeVisibleTypeAnnotationsAttribute);
        }
    }


    public void visitRuntimeVisibleTypeAnnotationsAttribute(Clazz clazz, Method method, RuntimeVisibleTypeAnnotationsAttribute runtimeVisibleTypeAnnotationsAttribute)
    {
        if (runtimeVisibleTypeAnnotationsAttribute.u2annotationsCount > 0)
        {
            attributeVisitor.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, runtimeVisibleTypeAnnotationsAttribute);
        }
    }


    public void visitRuntimeVisibleTypeAnnotationsAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, RuntimeVisibleTypeAnnotationsAttribute runtimeVisibleTypeAnnotationsAttribute)
    {
        if (runtimeVisibleTypeAnnotationsAttribute.u2annotationsCount > 0)
        {
            attributeVisitor.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, runtimeVisibleTypeAnnotationsAttribute);
        }
    }


    public void visitRuntimeInvisibleTypeAnnotationsAttribute(Clazz clazz, RuntimeInvisibleTypeAnnotationsAttribute runtimeInvisibleTypeAnnotationsAttribute)
    {
        if (runtimeInvisibleTypeAnnotationsAttribute.u2annotationsCount > 0)
        {
            attributeVisitor.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, runtimeInvisibleTypeAnnotationsAttribute);
        }
    }


    public void visitRuntimeInvisibleTypeAnnotationsAttribute(Clazz clazz, Field field, RuntimeInvisibleTypeAnnotationsAttribute runtimeInvisibleTypeAnnotationsAttribute)
    {
        if (runtimeInvisibleTypeAnnotationsAttribute.u2annotationsCount > 0)
        {
            attributeVisitor.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, field, runtimeInvisibleTypeAnnotationsAttribute);
        }
    }


    public void visitRuntimeInvisibleTypeAnnotationsAttribute(Clazz clazz, Method method, RuntimeInvisibleTypeAnnotationsAttribute runtimeInvisibleTypeAnnotationsAttribute)
    {
        if (runtimeInvisibleTypeAnnotationsAttribute.u2annotationsCount > 0)
        {
            attributeVisitor.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, runtimeInvisibleTypeAnnotationsAttribute);
        }
    }


    public void visitRuntimeInvisibleTypeAnnotationsAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, RuntimeInvisibleTypeAnnotationsAttribute runtimeInvisibleTypeAnnotationsAttribute)
    {
        if (runtimeInvisibleTypeAnnotationsAttribute.u2annotationsCount > 0)
        {
            attributeVisitor.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, runtimeInvisibleTypeAnnotationsAttribute);
        }
    }


    public void visitAnnotationDefaultAttribute(Clazz clazz, Method method, AnnotationDefaultAttribute annotationDefaultAttribute)
    {
        attributeVisitor.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);
    }
}
