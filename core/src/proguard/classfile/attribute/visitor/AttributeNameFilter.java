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
import proguard.util.*;

import java.util.List;

/**
 * This {@link AttributeVisitor} delegates its visits another {@link AttributeVisitor}, but
 * only when the visited attribute has a name that that matches a given regular
 * expression.
 *
 * @author Eric Lafortune
 */
public class AttributeNameFilter
implements   AttributeVisitor
{
    private final StringMatcher    regularExpressionMatcher;
    private final AttributeVisitor attributeVisitor;


    /**
     * Creates a new AttributeNameFilter.
     * @param regularExpression the regular expression against which attribute
     *                          names will be matched.
     * @param attributeVisitor  the <code>AttributeVisitor</code> to which
     *                          visits will be delegated.
     */
    public AttributeNameFilter(String           regularExpression,
                               AttributeVisitor attributeVisitor)
    {
        this(new ListParser(new NameParser()).parse(regularExpression),
             attributeVisitor);
    }


    /**
     * Creates a new AttributeNameFilter.
     * @param regularExpression the regular expression against which attribute
     *                          names will be matched.
     * @param attributeVisitor  the <code>AttributeVisitor</code> to which
     *                          visits will be delegated.
     */
    public AttributeNameFilter(List             regularExpression,
                               AttributeVisitor attributeVisitor)
    {
        this(new ListParser(new NameParser()).parse(regularExpression),
             attributeVisitor);
    }


    /**
     * Creates a new AttributeNameFilter.
     * @param regularExpressionMatcher the string matcher against which
     *                                 attribute names will be matched.
     * @param attributeVisitor         the <code>AttributeVisitor</code> to
     *                                 which visits will be delegated.
     */
    public AttributeNameFilter(StringMatcher    regularExpressionMatcher,
                               AttributeVisitor attributeVisitor)
    {
        this.regularExpressionMatcher = regularExpressionMatcher;
        this.attributeVisitor         = attributeVisitor;
    }


    // Implementations for AttributeVisitor.

    public void visitUnknownAttribute(Clazz clazz, UnknownAttribute unknownAttribute)
    {
        if (accepted(clazz, unknownAttribute))
        {
            attributeVisitor.visitUnknownAttribute(clazz, unknownAttribute);
        }
    }


    public void visitBootstrapMethodsAttribute(Clazz clazz, BootstrapMethodsAttribute bootstrapMethodsAttribute)
    {
        if (accepted(clazz, bootstrapMethodsAttribute))
        {
            attributeVisitor.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);
        }
    }


    public void visitSourceFileAttribute(Clazz clazz, SourceFileAttribute sourceFileAttribute)
    {
        if (accepted(clazz, sourceFileAttribute))
        {
            attributeVisitor.visitSourceFileAttribute(clazz, sourceFileAttribute);
        }
    }


    public void visitSourceDirAttribute(Clazz clazz, SourceDirAttribute sourceDirAttribute)
    {
        if (accepted(clazz, sourceDirAttribute))
        {
            attributeVisitor.visitSourceDirAttribute(clazz, sourceDirAttribute);
        }
    }


    public void visitSourceDebugExtensionAttribute(Clazz                         clazz,
                                                   SourceDebugExtensionAttribute sourceDebugExtensionAttribute)
    {
        if (accepted(clazz, sourceDebugExtensionAttribute))
        {
            attributeVisitor.visitSourceDebugExtensionAttribute(clazz, sourceDebugExtensionAttribute);
        }
    }


    public void visitInnerClassesAttribute(Clazz clazz, InnerClassesAttribute innerClassesAttribute)
    {
        if (accepted(clazz, innerClassesAttribute))
        {
            attributeVisitor.visitInnerClassesAttribute(clazz, innerClassesAttribute);
        }
    }


    public void visitEnclosingMethodAttribute(Clazz clazz, EnclosingMethodAttribute enclosingMethodAttribute)
    {
        if (accepted(clazz, enclosingMethodAttribute))
        {
            attributeVisitor.visitEnclosingMethodAttribute(clazz, enclosingMethodAttribute);
        }
    }


    public void visitNestHostAttribute(Clazz clazz, NestHostAttribute nestHostAttribute)
    {
        if (accepted(clazz, nestHostAttribute))
        {
            attributeVisitor.visitNestHostAttribute(clazz, nestHostAttribute);
        }
    }


    public void visitNestMembersAttribute(Clazz clazz, NestMembersAttribute nestMembersAttribute)
    {
        if (accepted(clazz, nestMembersAttribute))
        {
            attributeVisitor.visitNestMembersAttribute(clazz, nestMembersAttribute);
        }
    }


    public void visitModuleAttribute(Clazz clazz, ModuleAttribute moduleAttribute)
    {
        if (accepted(clazz, moduleAttribute))
        {
            attributeVisitor.visitModuleAttribute(clazz, moduleAttribute);
        }
    }


    public void visitModuleMainClassAttribute(Clazz clazz, ModuleMainClassAttribute moduleMainClassAttribute)
    {
        if (accepted(clazz, moduleMainClassAttribute))
        {
            attributeVisitor.visitModuleMainClassAttribute(clazz, moduleMainClassAttribute);
        }
    }


    public void visitModulePackagesAttribute(Clazz clazz, ModulePackagesAttribute modulePackagesAttribute)
    {
        if (accepted(clazz, modulePackagesAttribute))
        {
            attributeVisitor.visitModulePackagesAttribute(clazz, modulePackagesAttribute);
        }
    }


    public void visitDeprecatedAttribute(Clazz clazz, DeprecatedAttribute deprecatedAttribute)
    {
        if (accepted(clazz, deprecatedAttribute))
        {
            attributeVisitor.visitDeprecatedAttribute(clazz, deprecatedAttribute);
        }
    }


    public void visitDeprecatedAttribute(Clazz clazz, Field field, DeprecatedAttribute deprecatedAttribute)
    {
        if (accepted(clazz, deprecatedAttribute))
        {
            attributeVisitor.visitDeprecatedAttribute(clazz, field, deprecatedAttribute);
        }
    }


    public void visitDeprecatedAttribute(Clazz clazz, Method method, DeprecatedAttribute deprecatedAttribute)
    {
        if (accepted(clazz, deprecatedAttribute))
        {
            attributeVisitor.visitDeprecatedAttribute(clazz, method, deprecatedAttribute);
        }
    }


    public void visitSyntheticAttribute(Clazz clazz, SyntheticAttribute syntheticAttribute)
    {
        if (accepted(clazz, syntheticAttribute))
        {
            attributeVisitor.visitSyntheticAttribute(clazz, syntheticAttribute);
        }
    }


    public void visitSyntheticAttribute(Clazz clazz, Field field, SyntheticAttribute syntheticAttribute)
    {
        if (accepted(clazz, syntheticAttribute))
        {
            attributeVisitor.visitSyntheticAttribute(clazz, field, syntheticAttribute);
        }
    }


    public void visitSyntheticAttribute(Clazz clazz, Method method, SyntheticAttribute syntheticAttribute)
    {
        if (accepted(clazz, syntheticAttribute))
        {
            attributeVisitor.visitSyntheticAttribute(clazz, method, syntheticAttribute);
        }
    }


    public void visitSignatureAttribute(Clazz clazz, SignatureAttribute signatureAttribute)
    {
        if (accepted(clazz, signatureAttribute))
        {
            attributeVisitor.visitSignatureAttribute(clazz, signatureAttribute);
        }
    }


    public void visitSignatureAttribute(Clazz clazz, Field field, SignatureAttribute signatureAttribute)
    {
        if (accepted(clazz, signatureAttribute))
        {
            attributeVisitor.visitSignatureAttribute(clazz, field, signatureAttribute);
        }
    }


    public void visitSignatureAttribute(Clazz clazz, Method method, SignatureAttribute signatureAttribute)
    {
        if (accepted(clazz, signatureAttribute))
        {
            attributeVisitor.visitSignatureAttribute(clazz, method, signatureAttribute);
        }
    }


    public void visitConstantValueAttribute(Clazz clazz, Field field, ConstantValueAttribute constantValueAttribute)
    {
        if (accepted(clazz, constantValueAttribute))
        {
            attributeVisitor.visitConstantValueAttribute(clazz, field, constantValueAttribute);
        }
    }


    public void visitMethodParametersAttribute(Clazz clazz, Method method, MethodParametersAttribute exceptionsAttribute)
    {
        if (accepted(clazz, exceptionsAttribute))
        {
            attributeVisitor.visitMethodParametersAttribute(clazz, method, exceptionsAttribute);
        }
    }


    public void visitExceptionsAttribute(Clazz clazz, Method method, ExceptionsAttribute exceptionsAttribute)
    {
        if (accepted(clazz, exceptionsAttribute))
        {
            attributeVisitor.visitExceptionsAttribute(clazz, method, exceptionsAttribute);
        }
    }


    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        if (accepted(clazz, codeAttribute))
        {
            attributeVisitor.visitCodeAttribute(clazz, method, codeAttribute);
        }
    }


    public void visitStackMapAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, StackMapAttribute stackMapAttribute)
    {
        if (accepted(clazz, stackMapAttribute))
        {
            attributeVisitor.visitStackMapAttribute(clazz, method, codeAttribute, stackMapAttribute);
        }
    }


    public void visitStackMapTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, StackMapTableAttribute stackMapTableAttribute)
    {
        if (accepted(clazz, stackMapTableAttribute))
        {
            attributeVisitor.visitStackMapTableAttribute(clazz, method, codeAttribute, stackMapTableAttribute);
        }
    }


    public void visitLineNumberTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LineNumberTableAttribute lineNumberTableAttribute)
    {
        if (accepted(clazz, lineNumberTableAttribute))
        {
            attributeVisitor.visitLineNumberTableAttribute(clazz, method, codeAttribute, lineNumberTableAttribute);
        }
    }


    public void visitLocalVariableTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTableAttribute localVariableTableAttribute)
    {
        if (accepted(clazz, localVariableTableAttribute))
        {
            attributeVisitor.visitLocalVariableTableAttribute(clazz, method, codeAttribute, localVariableTableAttribute);
        }
    }


    public void visitLocalVariableTypeTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTypeTableAttribute localVariableTypeTableAttribute)
    {
        if (accepted(clazz, localVariableTypeTableAttribute))
        {
            attributeVisitor.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, localVariableTypeTableAttribute);
        }
    }


    public void visitRuntimeVisibleAnnotationsAttribute(Clazz clazz, RuntimeVisibleAnnotationsAttribute runtimeVisibleAnnotationsAttribute)
    {
        if (accepted(clazz, runtimeVisibleAnnotationsAttribute))
        {
            attributeVisitor.visitRuntimeVisibleAnnotationsAttribute(clazz, runtimeVisibleAnnotationsAttribute);
        }
    }


    public void visitRuntimeVisibleAnnotationsAttribute(Clazz clazz, Field field, RuntimeVisibleAnnotationsAttribute runtimeVisibleAnnotationsAttribute)
    {
        if (accepted(clazz, runtimeVisibleAnnotationsAttribute))
        {
            attributeVisitor.visitRuntimeVisibleAnnotationsAttribute(clazz, field, runtimeVisibleAnnotationsAttribute);
        }
    }


    public void visitRuntimeVisibleAnnotationsAttribute(Clazz clazz, Method method, RuntimeVisibleAnnotationsAttribute runtimeVisibleAnnotationsAttribute)
    {
        if (accepted(clazz, runtimeVisibleAnnotationsAttribute))
        {
            attributeVisitor.visitRuntimeVisibleAnnotationsAttribute(clazz, method, runtimeVisibleAnnotationsAttribute);
        }
    }


    public void visitRuntimeInvisibleAnnotationsAttribute(Clazz clazz, RuntimeInvisibleAnnotationsAttribute runtimeInvisibleAnnotationsAttribute)
    {
        if (accepted(clazz, runtimeInvisibleAnnotationsAttribute))
        {
            attributeVisitor.visitRuntimeInvisibleAnnotationsAttribute(clazz, runtimeInvisibleAnnotationsAttribute);
        }
    }


    public void visitRuntimeInvisibleAnnotationsAttribute(Clazz clazz, Field field, RuntimeInvisibleAnnotationsAttribute runtimeInvisibleAnnotationsAttribute)
    {
        if (accepted(clazz, runtimeInvisibleAnnotationsAttribute))
        {
            attributeVisitor.visitRuntimeInvisibleAnnotationsAttribute(clazz, field, runtimeInvisibleAnnotationsAttribute);
        }
    }


    public void visitRuntimeInvisibleAnnotationsAttribute(Clazz clazz, Method method, RuntimeInvisibleAnnotationsAttribute runtimeInvisibleAnnotationsAttribute)
    {
        if (accepted(clazz, runtimeInvisibleAnnotationsAttribute))
        {
            attributeVisitor.visitRuntimeInvisibleAnnotationsAttribute(clazz, method, runtimeInvisibleAnnotationsAttribute);
        }
    }


    public void visitRuntimeVisibleParameterAnnotationsAttribute(Clazz clazz, Method method, RuntimeVisibleParameterAnnotationsAttribute runtimeVisibleParameterAnnotationsAttribute)
    {
        if (accepted(clazz, runtimeVisibleParameterAnnotationsAttribute))
        {
            attributeVisitor.visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, runtimeVisibleParameterAnnotationsAttribute);
        }
    }


    public void visitRuntimeInvisibleParameterAnnotationsAttribute(Clazz clazz, Method method, RuntimeInvisibleParameterAnnotationsAttribute runtimeInvisibleParameterAnnotationsAttribute)
    {
        if (accepted(clazz, runtimeInvisibleParameterAnnotationsAttribute))
        {
            attributeVisitor.visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, runtimeInvisibleParameterAnnotationsAttribute);
        }
    }


    public void visitRuntimeVisibleTypeAnnotationsAttribute(Clazz clazz, RuntimeVisibleTypeAnnotationsAttribute runtimeVisibleTypeAnnotationsAttribute)
    {
        if (accepted(clazz, runtimeVisibleTypeAnnotationsAttribute))
        {
            attributeVisitor.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, runtimeVisibleTypeAnnotationsAttribute);
        }
    }


    public void visitRuntimeVisibleTypeAnnotationsAttribute(Clazz clazz, Field field, RuntimeVisibleTypeAnnotationsAttribute runtimeVisibleTypeAnnotationsAttribute)
    {
        if (accepted(clazz, runtimeVisibleTypeAnnotationsAttribute))
        {
            attributeVisitor.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, field, runtimeVisibleTypeAnnotationsAttribute);
        }
    }


    public void visitRuntimeVisibleTypeAnnotationsAttribute(Clazz clazz, Method method, RuntimeVisibleTypeAnnotationsAttribute runtimeVisibleTypeAnnotationsAttribute)
    {
        if (accepted(clazz, runtimeVisibleTypeAnnotationsAttribute))
        {
            attributeVisitor.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, runtimeVisibleTypeAnnotationsAttribute);
        }
    }


    public void visitRuntimeVisibleTypeAnnotationsAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, RuntimeVisibleTypeAnnotationsAttribute runtimeVisibleTypeAnnotationsAttribute)
    {
        if (accepted(clazz, runtimeVisibleTypeAnnotationsAttribute))
        {
            attributeVisitor.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, runtimeVisibleTypeAnnotationsAttribute);
        }
    }


    public void visitRuntimeInvisibleTypeAnnotationsAttribute(Clazz clazz, RuntimeInvisibleTypeAnnotationsAttribute runtimeInvisibleTypeAnnotationsAttribute)
    {
        if (accepted(clazz, runtimeInvisibleTypeAnnotationsAttribute))
        {
            attributeVisitor.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, runtimeInvisibleTypeAnnotationsAttribute);
        }
    }


    public void visitRuntimeInvisibleTypeAnnotationsAttribute(Clazz clazz, Field field, RuntimeInvisibleTypeAnnotationsAttribute runtimeInvisibleTypeAnnotationsAttribute)
    {
        if (accepted(clazz, runtimeInvisibleTypeAnnotationsAttribute))
        {
            attributeVisitor.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, field, runtimeInvisibleTypeAnnotationsAttribute);
        }
    }


    public void visitRuntimeInvisibleTypeAnnotationsAttribute(Clazz clazz, Method method, RuntimeInvisibleTypeAnnotationsAttribute runtimeInvisibleTypeAnnotationsAttribute)
    {
        if (accepted(clazz, runtimeInvisibleTypeAnnotationsAttribute))
        {
            attributeVisitor.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, runtimeInvisibleTypeAnnotationsAttribute);
        }
    }


    public void visitRuntimeInvisibleTypeAnnotationsAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, RuntimeInvisibleTypeAnnotationsAttribute runtimeInvisibleTypeAnnotationsAttribute)
    {
        if (accepted(clazz, runtimeInvisibleTypeAnnotationsAttribute))
        {
            attributeVisitor.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, runtimeInvisibleTypeAnnotationsAttribute);
        }
    }


    public void visitAnnotationDefaultAttribute(Clazz clazz, Method method, AnnotationDefaultAttribute annotationDefaultAttribute)
    {
        if (accepted(clazz, annotationDefaultAttribute))
        {
            attributeVisitor.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);
        }
    }


    // Small utility methods.

    private boolean accepted(Clazz clazz, Attribute attribute)
    {
        return regularExpressionMatcher.matches(attribute.getAttributeName(clazz));
    }
}
