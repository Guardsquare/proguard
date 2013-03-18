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
import proguard.util.*;

import java.util.List;

/**
 * This AttributeVisitor delegates its visits another AttributeVisitor, but
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
            unknownAttribute.accept(clazz, attributeVisitor);
        }
    }


    public void visitBootstrapMethodsAttribute(Clazz clazz, BootstrapMethodsAttribute bootstrapMethodsAttribute)
    {
        if (accepted(clazz, bootstrapMethodsAttribute))
        {
            bootstrapMethodsAttribute.accept(clazz, attributeVisitor);
        }
    }


    public void visitSourceFileAttribute(Clazz clazz, SourceFileAttribute sourceFileAttribute)
    {
        if (accepted(clazz, sourceFileAttribute))
        {
            sourceFileAttribute.accept(clazz, attributeVisitor);
        }
    }


    public void visitSourceDirAttribute(Clazz clazz, SourceDirAttribute sourceDirAttribute)
    {
        if (accepted(clazz, sourceDirAttribute))
        {
            sourceDirAttribute.accept(clazz, attributeVisitor);
        }
    }


    public void visitInnerClassesAttribute(Clazz clazz, InnerClassesAttribute innerClassesAttribute)
    {
        if (accepted(clazz, innerClassesAttribute))
        {
            innerClassesAttribute.accept(clazz, attributeVisitor);
        }
    }


    public void visitEnclosingMethodAttribute(Clazz clazz, EnclosingMethodAttribute enclosingMethodAttribute)
    {
        if (accepted(clazz, enclosingMethodAttribute))
        {
            enclosingMethodAttribute.accept(clazz, attributeVisitor);
        }
    }


    public void visitDeprecatedAttribute(Clazz clazz, DeprecatedAttribute deprecatedAttribute)
    {
        if (accepted(clazz, deprecatedAttribute))
        {
            deprecatedAttribute.accept(clazz, attributeVisitor);
        }
    }


    public void visitDeprecatedAttribute(Clazz clazz, Field field, DeprecatedAttribute deprecatedAttribute)
    {
        if (accepted(clazz, deprecatedAttribute))
        {
            deprecatedAttribute.accept(clazz, field, attributeVisitor);
        }
    }


    public void visitDeprecatedAttribute(Clazz clazz, Method method, DeprecatedAttribute deprecatedAttribute)
    {
        if (accepted(clazz, deprecatedAttribute))
        {
            deprecatedAttribute.accept(clazz, method, attributeVisitor);
        }
    }


    public void visitSyntheticAttribute(Clazz clazz, SyntheticAttribute syntheticAttribute)
    {
        if (accepted(clazz, syntheticAttribute))
        {
            syntheticAttribute.accept(clazz, attributeVisitor);
        }
    }


    public void visitSyntheticAttribute(Clazz clazz, Field field, SyntheticAttribute syntheticAttribute)
    {
        if (accepted(clazz, syntheticAttribute))
        {
            syntheticAttribute.accept(clazz, field, attributeVisitor);
        }
    }


    public void visitSyntheticAttribute(Clazz clazz, Method method, SyntheticAttribute syntheticAttribute)
    {
        if (accepted(clazz, syntheticAttribute))
        {
            syntheticAttribute.accept(clazz, method, attributeVisitor);
        }
    }


    public void visitSignatureAttribute(Clazz clazz, SignatureAttribute signatureAttribute)
    {
        if (accepted(clazz, signatureAttribute))
        {
            signatureAttribute.accept(clazz, attributeVisitor);
        }
    }


    public void visitSignatureAttribute(Clazz clazz, Field field, SignatureAttribute signatureAttribute)
    {
        if (accepted(clazz, signatureAttribute))
        {
            signatureAttribute.accept(clazz, field, attributeVisitor);
        }
    }


    public void visitSignatureAttribute(Clazz clazz, Method method, SignatureAttribute signatureAttribute)
    {
        if (accepted(clazz, signatureAttribute))
        {
            signatureAttribute.accept(clazz, method, attributeVisitor);
        }
    }


    public void visitConstantValueAttribute(Clazz clazz, Field field, ConstantValueAttribute constantValueAttribute)
    {
        if (accepted(clazz, constantValueAttribute))
        {
            constantValueAttribute.accept(clazz, field, attributeVisitor);
        }
    }


    public void visitExceptionsAttribute(Clazz clazz, Method method, ExceptionsAttribute exceptionsAttribute)
    {
        if (accepted(clazz, exceptionsAttribute))
        {
            exceptionsAttribute.accept(clazz, method, attributeVisitor);
        }
    }


    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        if (accepted(clazz, codeAttribute))
        {
            codeAttribute.accept(clazz, method, attributeVisitor);
        }
    }


    public void visitStackMapAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, StackMapAttribute stackMapAttribute)
    {
        if (accepted(clazz, stackMapAttribute))
        {
            stackMapAttribute.accept(clazz, method, codeAttribute, attributeVisitor);
        }
    }


    public void visitStackMapTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, StackMapTableAttribute stackMapTableAttribute)
    {
        if (accepted(clazz, stackMapTableAttribute))
        {
            stackMapTableAttribute.accept(clazz, method, codeAttribute, attributeVisitor);
        }
    }


    public void visitLineNumberTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LineNumberTableAttribute lineNumberTableAttribute)
    {
        if (accepted(clazz, lineNumberTableAttribute))
        {
            lineNumberTableAttribute.accept(clazz, method, codeAttribute, attributeVisitor);
        }
    }


    public void visitLocalVariableTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTableAttribute localVariableTableAttribute)
    {
        if (accepted(clazz, localVariableTableAttribute))
        {
            localVariableTableAttribute.accept(clazz, method, codeAttribute, attributeVisitor);
        }
    }


    public void visitLocalVariableTypeTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTypeTableAttribute localVariableTypeTableAttribute)
    {
        if (accepted(clazz, localVariableTypeTableAttribute))
        {
            localVariableTypeTableAttribute.accept(clazz, method, codeAttribute, attributeVisitor);
        }
    }


    public void visitRuntimeVisibleAnnotationsAttribute(Clazz clazz, RuntimeVisibleAnnotationsAttribute runtimeVisibleAnnotationsAttribute)
    {
        if (accepted(clazz, runtimeVisibleAnnotationsAttribute))
        {
            runtimeVisibleAnnotationsAttribute.accept(clazz, attributeVisitor);
        }
    }


    public void visitRuntimeVisibleAnnotationsAttribute(Clazz clazz, Field field, RuntimeVisibleAnnotationsAttribute runtimeVisibleAnnotationsAttribute)
    {
        if (accepted(clazz, runtimeVisibleAnnotationsAttribute))
        {
            runtimeVisibleAnnotationsAttribute.accept(clazz, field, attributeVisitor);
        }
    }


    public void visitRuntimeVisibleAnnotationsAttribute(Clazz clazz, Method method, RuntimeVisibleAnnotationsAttribute runtimeVisibleAnnotationsAttribute)
    {
        if (accepted(clazz, runtimeVisibleAnnotationsAttribute))
        {
            runtimeVisibleAnnotationsAttribute.accept(clazz, method, attributeVisitor);
        }
    }


    public void visitRuntimeInvisibleAnnotationsAttribute(Clazz clazz, RuntimeInvisibleAnnotationsAttribute runtimeInvisibleAnnotationsAttribute)
    {
        if (accepted(clazz, runtimeInvisibleAnnotationsAttribute))
        {
            runtimeInvisibleAnnotationsAttribute.accept(clazz, attributeVisitor);
        }
    }


    public void visitRuntimeInvisibleAnnotationsAttribute(Clazz clazz, Field field, RuntimeInvisibleAnnotationsAttribute runtimeInvisibleAnnotationsAttribute)
    {
        if (accepted(clazz, runtimeInvisibleAnnotationsAttribute))
        {
            runtimeInvisibleAnnotationsAttribute.accept(clazz, field, attributeVisitor);
        }
    }


    public void visitRuntimeInvisibleAnnotationsAttribute(Clazz clazz, Method method, RuntimeInvisibleAnnotationsAttribute runtimeInvisibleAnnotationsAttribute)
    {
        if (accepted(clazz, runtimeInvisibleAnnotationsAttribute))
        {
            runtimeInvisibleAnnotationsAttribute.accept(clazz, method, attributeVisitor);
        }
    }


    public void visitRuntimeVisibleParameterAnnotationsAttribute(Clazz clazz, Method method, RuntimeVisibleParameterAnnotationsAttribute runtimeVisibleParameterAnnotationsAttribute)
    {
        if (accepted(clazz, runtimeVisibleParameterAnnotationsAttribute))
        {
            runtimeVisibleParameterAnnotationsAttribute.accept(clazz, method, attributeVisitor);
        }
    }


    public void visitRuntimeInvisibleParameterAnnotationsAttribute(Clazz clazz, Method method, RuntimeInvisibleParameterAnnotationsAttribute runtimeInvisibleParameterAnnotationsAttribute)
    {
        if (accepted(clazz, runtimeInvisibleParameterAnnotationsAttribute))
        {
            runtimeInvisibleParameterAnnotationsAttribute.accept(clazz, method, attributeVisitor);
        }
    }


    public void visitAnnotationDefaultAttribute(Clazz clazz, Method method, AnnotationDefaultAttribute annotationDefaultAttribute)
    {
        if (accepted(clazz, annotationDefaultAttribute))
        {
            annotationDefaultAttribute.accept(clazz, method, attributeVisitor);
        }
    }


    // Small utility methods.

    private boolean accepted(Clazz clazz, Attribute attribute)
    {
        return regularExpressionMatcher.matches(attribute.getAttributeName(clazz));
    }
}
