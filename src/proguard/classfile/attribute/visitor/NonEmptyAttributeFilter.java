/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2013 Eric Lafortune (eric@graphics.cornell.edu)
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
package proguard.classfile.attribute.visitor;

import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.annotation.*;
import proguard.classfile.attribute.preverification.*;
import proguard.util.StringMatcher;

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
        unknownAttribute.accept(clazz, attributeVisitor);
    }


    public void visitBootstrapMethodsAttribute(Clazz clazz, BootstrapMethodsAttribute bootstrapMethodsAttribute)
    {
        if (bootstrapMethodsAttribute.u2bootstrapMethodsCount > 0)
        {
            bootstrapMethodsAttribute.accept(clazz, attributeVisitor);
        }
    }


    public void visitSourceFileAttribute(Clazz clazz, SourceFileAttribute sourceFileAttribute)
    {
        sourceFileAttribute.accept(clazz, attributeVisitor);
    }


    public void visitSourceDirAttribute(Clazz clazz, SourceDirAttribute sourceDirAttribute)
    {
        sourceDirAttribute.accept(clazz, attributeVisitor);
    }


    public void visitInnerClassesAttribute(Clazz clazz, InnerClassesAttribute innerClassesAttribute)
    {
        if (innerClassesAttribute.u2classesCount > 0)
        {
            innerClassesAttribute.accept(clazz, attributeVisitor);
        }
    }


    public void visitEnclosingMethodAttribute(Clazz clazz, EnclosingMethodAttribute enclosingMethodAttribute)
    {
        enclosingMethodAttribute.accept(clazz, attributeVisitor);
    }


    public void visitDeprecatedAttribute(Clazz clazz, DeprecatedAttribute deprecatedAttribute)
    {
        deprecatedAttribute.accept(clazz, attributeVisitor);
    }


    public void visitDeprecatedAttribute(Clazz clazz, Field field, DeprecatedAttribute deprecatedAttribute)
    {
        deprecatedAttribute.accept(clazz, field, attributeVisitor);
    }


    public void visitDeprecatedAttribute(Clazz clazz, Method method, DeprecatedAttribute deprecatedAttribute)
    {
        deprecatedAttribute.accept(clazz, method, attributeVisitor);
    }


    public void visitSyntheticAttribute(Clazz clazz, SyntheticAttribute syntheticAttribute)
    {
        syntheticAttribute.accept(clazz, attributeVisitor);
    }


    public void visitSyntheticAttribute(Clazz clazz, Field field, SyntheticAttribute syntheticAttribute)
    {
        syntheticAttribute.accept(clazz, field, attributeVisitor);
    }


    public void visitSyntheticAttribute(Clazz clazz, Method method, SyntheticAttribute syntheticAttribute)
    {
        syntheticAttribute.accept(clazz, method, attributeVisitor);
    }


    public void visitSignatureAttribute(Clazz clazz, SignatureAttribute signatureAttribute)
    {
        signatureAttribute.accept(clazz, attributeVisitor);
    }


    public void visitSignatureAttribute(Clazz clazz, Field field, SignatureAttribute signatureAttribute)
    {
        signatureAttribute.accept(clazz, field, attributeVisitor);
    }


    public void visitSignatureAttribute(Clazz clazz, Method method, SignatureAttribute signatureAttribute)
    {
        signatureAttribute.accept(clazz, method, attributeVisitor);
    }


    public void visitConstantValueAttribute(Clazz clazz, Field field, ConstantValueAttribute constantValueAttribute)
    {
        constantValueAttribute.accept(clazz, field, attributeVisitor);
    }


    public void visitExceptionsAttribute(Clazz clazz, Method method, ExceptionsAttribute exceptionsAttribute)
    {
        if (exceptionsAttribute.u2exceptionIndexTableLength > 0)
        {
            exceptionsAttribute.accept(clazz, method, attributeVisitor);
        }
    }


    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        codeAttribute.accept(clazz, method, attributeVisitor);
    }


    public void visitStackMapAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, StackMapAttribute stackMapAttribute)
    {
        if (stackMapAttribute.u2stackMapFramesCount > 0)
        {
            stackMapAttribute.accept(clazz, method, codeAttribute, attributeVisitor);
        }
    }


    public void visitStackMapTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, StackMapTableAttribute stackMapTableAttribute)
    {
        if (stackMapTableAttribute.u2stackMapFramesCount > 0)
        {
            stackMapTableAttribute.accept(clazz, method, codeAttribute, attributeVisitor);
        }
    }


    public void visitLineNumberTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LineNumberTableAttribute lineNumberTableAttribute)
    {
        if (lineNumberTableAttribute.u2lineNumberTableLength > 0)
        {
            lineNumberTableAttribute.accept(clazz, method, codeAttribute, attributeVisitor);
        }
    }


    public void visitLocalVariableTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTableAttribute localVariableTableAttribute)
    {
        if (localVariableTableAttribute.u2localVariableTableLength > 0)
        {
            localVariableTableAttribute.accept(clazz, method, codeAttribute, attributeVisitor);
        }
    }


    public void visitLocalVariableTypeTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTypeTableAttribute localVariableTypeTableAttribute)
    {
        if (localVariableTypeTableAttribute.u2localVariableTypeTableLength > 0)
        {
            localVariableTypeTableAttribute.accept(clazz, method, codeAttribute, attributeVisitor);
        }
    }


    public void visitRuntimeVisibleAnnotationsAttribute(Clazz clazz, RuntimeVisibleAnnotationsAttribute runtimeVisibleAnnotationsAttribute)
    {
        if (runtimeVisibleAnnotationsAttribute.u2annotationsCount > 0)
        {
            runtimeVisibleAnnotationsAttribute.accept(clazz, attributeVisitor);
        }
    }


    public void visitRuntimeVisibleAnnotationsAttribute(Clazz clazz, Field field, RuntimeVisibleAnnotationsAttribute runtimeVisibleAnnotationsAttribute)
    {
        if (runtimeVisibleAnnotationsAttribute.u2annotationsCount > 0)
        {
            runtimeVisibleAnnotationsAttribute.accept(clazz, field, attributeVisitor);
        }
    }


    public void visitRuntimeVisibleAnnotationsAttribute(Clazz clazz, Method method, RuntimeVisibleAnnotationsAttribute runtimeVisibleAnnotationsAttribute)
    {
        if (runtimeVisibleAnnotationsAttribute.u2annotationsCount > 0)
        {
            runtimeVisibleAnnotationsAttribute.accept(clazz, method, attributeVisitor);
        }
    }


    public void visitRuntimeInvisibleAnnotationsAttribute(Clazz clazz, RuntimeInvisibleAnnotationsAttribute runtimeInvisibleAnnotationsAttribute)
    {
        if (runtimeInvisibleAnnotationsAttribute.u2annotationsCount > 0)
        {
            runtimeInvisibleAnnotationsAttribute.accept(clazz, attributeVisitor);
        }
    }


    public void visitRuntimeInvisibleAnnotationsAttribute(Clazz clazz, Field field, RuntimeInvisibleAnnotationsAttribute runtimeInvisibleAnnotationsAttribute)
    {
        if (runtimeInvisibleAnnotationsAttribute.u2annotationsCount > 0)
        {
            runtimeInvisibleAnnotationsAttribute.accept(clazz, field, attributeVisitor);
        }
    }


    public void visitRuntimeInvisibleAnnotationsAttribute(Clazz clazz, Method method, RuntimeInvisibleAnnotationsAttribute runtimeInvisibleAnnotationsAttribute)
    {
        if (runtimeInvisibleAnnotationsAttribute.u2annotationsCount > 0)
        {
            runtimeInvisibleAnnotationsAttribute.accept(clazz, method, attributeVisitor);
        }
    }


    public void visitRuntimeVisibleParameterAnnotationsAttribute(Clazz clazz, Method method, RuntimeVisibleParameterAnnotationsAttribute runtimeVisibleParameterAnnotationsAttribute)
    {
        if (runtimeVisibleParameterAnnotationsAttribute.u2parametersCount > 0)
        {
            runtimeVisibleParameterAnnotationsAttribute.accept(clazz, method, attributeVisitor);
        }
    }


    public void visitRuntimeInvisibleParameterAnnotationsAttribute(Clazz clazz, Method method, RuntimeInvisibleParameterAnnotationsAttribute runtimeInvisibleParameterAnnotationsAttribute)
    {
        if (runtimeInvisibleParameterAnnotationsAttribute.u2parametersCount > 0)
        {
            runtimeInvisibleParameterAnnotationsAttribute.accept(clazz, method, attributeVisitor);
        }
    }


    public void visitAnnotationDefaultAttribute(Clazz clazz, Method method, AnnotationDefaultAttribute annotationDefaultAttribute)
    {
        annotationDefaultAttribute.accept(clazz, method, attributeVisitor);
    }
}
