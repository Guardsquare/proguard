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
import proguard.obfuscate.AttributeShrinker;

/**
 * This AttributeVisitor delegates its visits to one of two other
 * AttributeVisitor instances, depending on whether the visited attribute
 * is strictly required or not.
 *
 * Stack map attributes and stack map table attributes are treated as optional.
 *
 * @see AttributeShrinker
 *
 * @author Eric Lafortune
 */
public class RequiredAttributeFilter
implements   AttributeVisitor
{
    private final AttributeVisitor requiredAttributeVisitor;
    private final AttributeVisitor optionalAttributeVisitor;


    /**
     * Creates a new RequiredAttributeFilter for visiting required attributes.
     * @param requiredAttributeVisitor   the visitor that will visit required
     *                                   attributes.
     */
    public RequiredAttributeFilter(AttributeVisitor requiredAttributeVisitor)
    {
        this(requiredAttributeVisitor, null);
    }


    /**
     * Creates a new RequiredAttributeFilter for visiting required and
     * optional attributes.
     * @param requiredAttributeVisitor the visitor that will visit required
     *                                 attributes.
     * @param optionalAttributeVisitor the visitor that will visit optional
     *                                 attributes.
     */
    public RequiredAttributeFilter(AttributeVisitor requiredAttributeVisitor,
                                   AttributeVisitor optionalAttributeVisitor)
    {
        this.requiredAttributeVisitor = requiredAttributeVisitor;
        this.optionalAttributeVisitor = optionalAttributeVisitor;
    }


    // Implementations for AttributeVisitor.

    public void visitUnknownAttribute(Clazz clazz, UnknownAttribute unknownAttribute)
    {
        if (optionalAttributeVisitor != null)
        {
            unknownAttribute.accept(clazz, optionalAttributeVisitor);
        }
    }


    public void visitBootstrapMethodsAttribute(Clazz clazz, BootstrapMethodsAttribute bootstrapMethodsAttribute)
    {
        if (requiredAttributeVisitor != null)
        {
            bootstrapMethodsAttribute.accept(clazz, requiredAttributeVisitor);
        }
    }


    public void visitSourceFileAttribute(Clazz clazz, SourceFileAttribute sourceFileAttribute)
    {
        if (optionalAttributeVisitor != null)
        {
            sourceFileAttribute.accept(clazz, optionalAttributeVisitor);
        }
    }


    public void visitSourceDirAttribute(Clazz clazz, SourceDirAttribute sourceDirAttribute)
    {
        if (optionalAttributeVisitor != null)
        {
            sourceDirAttribute.accept(clazz, optionalAttributeVisitor);
        }
    }


    public void visitInnerClassesAttribute(Clazz clazz, InnerClassesAttribute innerClassesAttribute)
    {
        if (optionalAttributeVisitor != null)
        {
            innerClassesAttribute.accept(clazz, optionalAttributeVisitor);
        }
    }


    public void visitEnclosingMethodAttribute(Clazz clazz, EnclosingMethodAttribute enclosingMethodAttribute)
    {
        if (optionalAttributeVisitor != null)
        {
            enclosingMethodAttribute.accept(clazz, optionalAttributeVisitor);
        }
    }


    public void visitDeprecatedAttribute(Clazz clazz, DeprecatedAttribute deprecatedAttribute)
    {
        if (optionalAttributeVisitor != null)
        {
            deprecatedAttribute.accept(clazz, optionalAttributeVisitor);
        }
    }


    public void visitDeprecatedAttribute(Clazz clazz, Field field, DeprecatedAttribute deprecatedAttribute)
    {
        if (optionalAttributeVisitor != null)
        {
            deprecatedAttribute.accept(clazz, field, optionalAttributeVisitor);
        }
    }


    public void visitDeprecatedAttribute(Clazz clazz, Method method, DeprecatedAttribute deprecatedAttribute)
    {
        if (optionalAttributeVisitor != null)
        {
            deprecatedAttribute.accept(clazz, method, optionalAttributeVisitor);
        }
    }


    public void visitSyntheticAttribute(Clazz clazz, SyntheticAttribute syntheticAttribute)
    {
        if (optionalAttributeVisitor != null)
        {
            syntheticAttribute.accept(clazz, optionalAttributeVisitor);
        }
    }


    public void visitSyntheticAttribute(Clazz clazz, Field field, SyntheticAttribute syntheticAttribute)
    {
        if (optionalAttributeVisitor != null)
        {
            syntheticAttribute.accept(clazz, field, optionalAttributeVisitor);
        }
    }


    public void visitSyntheticAttribute(Clazz clazz, Method method, SyntheticAttribute syntheticAttribute)
    {
        if (optionalAttributeVisitor != null)
        {
            syntheticAttribute.accept(clazz, method, optionalAttributeVisitor);
        }
    }


    public void visitSignatureAttribute(Clazz clazz, SignatureAttribute signatureAttribute)
    {
        if (optionalAttributeVisitor != null)
        {
            signatureAttribute.accept(clazz, optionalAttributeVisitor);
        }
    }


    public void visitSignatureAttribute(Clazz clazz, Field field, SignatureAttribute signatureAttribute)
    {
        if (optionalAttributeVisitor != null)
        {
            signatureAttribute.accept(clazz, field, optionalAttributeVisitor);
        }
    }


    public void visitSignatureAttribute(Clazz clazz, Method method, SignatureAttribute signatureAttribute)
    {
        if (optionalAttributeVisitor != null)
        {
            signatureAttribute.accept(clazz, method, optionalAttributeVisitor);
        }
    }


    public void visitConstantValueAttribute(Clazz clazz, Field field, ConstantValueAttribute constantValueAttribute)
    {
        if (requiredAttributeVisitor != null)
        {
            constantValueAttribute.accept(clazz, field, requiredAttributeVisitor);
        }
    }


    public void visitExceptionsAttribute(Clazz clazz, Method method, ExceptionsAttribute exceptionsAttribute)
    {
        if (optionalAttributeVisitor != null)
        {
            exceptionsAttribute.accept(clazz, method, optionalAttributeVisitor);
        }
    }


    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        if (requiredAttributeVisitor != null)
        {
            codeAttribute.accept(clazz, method, requiredAttributeVisitor);
        }
    }


    public void visitStackMapAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, StackMapAttribute stackMapAttribute)
    {
        if (optionalAttributeVisitor != null)
        {
            stackMapAttribute.accept(clazz, method, codeAttribute, optionalAttributeVisitor);
        }
    }


    public void visitStackMapTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, StackMapTableAttribute stackMapTableAttribute)
    {
        if (optionalAttributeVisitor != null)
        {
            stackMapTableAttribute.accept(clazz, method, codeAttribute, optionalAttributeVisitor);
        }
    }


    public void visitLineNumberTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LineNumberTableAttribute lineNumberTableAttribute)
    {
        if (optionalAttributeVisitor != null)
        {
            lineNumberTableAttribute.accept(clazz, method, codeAttribute, optionalAttributeVisitor);
        }
    }


    public void visitLocalVariableTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTableAttribute localVariableTableAttribute)
    {
        if (optionalAttributeVisitor != null)
        {
            localVariableTableAttribute.accept(clazz, method, codeAttribute, optionalAttributeVisitor);
        }
    }


    public void visitLocalVariableTypeTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTypeTableAttribute localVariableTypeTableAttribute)
    {
        if (optionalAttributeVisitor != null)
        {
            localVariableTypeTableAttribute.accept(clazz, method, codeAttribute, optionalAttributeVisitor);
        }
    }


    public void visitRuntimeVisibleAnnotationsAttribute(Clazz clazz, RuntimeVisibleAnnotationsAttribute runtimeVisibleAnnotationsAttribute)
    {
        if (optionalAttributeVisitor != null)
        {
            runtimeVisibleAnnotationsAttribute.accept(clazz, optionalAttributeVisitor);
        }
    }


    public void visitRuntimeVisibleAnnotationsAttribute(Clazz clazz, Field field, RuntimeVisibleAnnotationsAttribute runtimeVisibleAnnotationsAttribute)
    {
        if (optionalAttributeVisitor != null)
        {
            runtimeVisibleAnnotationsAttribute.accept(clazz, field, optionalAttributeVisitor);
        }
    }


    public void visitRuntimeVisibleAnnotationsAttribute(Clazz clazz, Method method, RuntimeVisibleAnnotationsAttribute runtimeVisibleAnnotationsAttribute)
    {
        if (optionalAttributeVisitor != null)
        {
            runtimeVisibleAnnotationsAttribute.accept(clazz, method, optionalAttributeVisitor);
        }
    }


    public void visitRuntimeInvisibleAnnotationsAttribute(Clazz clazz, RuntimeInvisibleAnnotationsAttribute runtimeInvisibleAnnotationsAttribute)
    {
        if (optionalAttributeVisitor != null)
        {
            runtimeInvisibleAnnotationsAttribute.accept(clazz, optionalAttributeVisitor);
        }
    }


    public void visitRuntimeInvisibleAnnotationsAttribute(Clazz clazz, Field field, RuntimeInvisibleAnnotationsAttribute runtimeInvisibleAnnotationsAttribute)
    {
        if (optionalAttributeVisitor != null)
        {
            runtimeInvisibleAnnotationsAttribute.accept(clazz, field, optionalAttributeVisitor);
        }
    }


    public void visitRuntimeInvisibleAnnotationsAttribute(Clazz clazz, Method method, RuntimeInvisibleAnnotationsAttribute runtimeInvisibleAnnotationsAttribute)
    {
        if (optionalAttributeVisitor != null)
        {
            runtimeInvisibleAnnotationsAttribute.accept(clazz, method, optionalAttributeVisitor);
        }
    }


    public void visitRuntimeVisibleParameterAnnotationsAttribute(Clazz clazz, Method method, RuntimeVisibleParameterAnnotationsAttribute runtimeVisibleParameterAnnotationsAttribute)
    {
        if (optionalAttributeVisitor != null)
        {
            runtimeVisibleParameterAnnotationsAttribute.accept(clazz, method, optionalAttributeVisitor);
        }
    }


    public void visitRuntimeInvisibleParameterAnnotationsAttribute(Clazz clazz, Method method, RuntimeInvisibleParameterAnnotationsAttribute runtimeInvisibleParameterAnnotationsAttribute)
    {
        if (optionalAttributeVisitor != null)
        {
            runtimeInvisibleParameterAnnotationsAttribute.accept(clazz, method, optionalAttributeVisitor);
        }
    }


    public void visitAnnotationDefaultAttribute(Clazz clazz, Method method, AnnotationDefaultAttribute annotationDefaultAttribute)
    {
        if (optionalAttributeVisitor != null)
        {
            annotationDefaultAttribute.accept(clazz, method, optionalAttributeVisitor);
        }
    }
}
