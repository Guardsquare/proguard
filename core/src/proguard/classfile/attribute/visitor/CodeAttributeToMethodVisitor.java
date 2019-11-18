/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2018 GuardSquare NV
 */
package proguard.classfile.attribute.visitor;

import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.util.SimplifiedVisitor;
import proguard.classfile.visitor.MemberVisitor;

/**
 * This AttributeVisitor lets a given MemberVisitor visit all methods whose code attribute is visited.
 *
 * @author Johan Leys
 */
public class CodeAttributeToMethodVisitor
extends      SimplifiedVisitor
implements   AttributeVisitor
{
    private final MemberVisitor memberVisitor;


    /**
     * Creates a new CodeAttributeToMethodVisitor.
     */
    public CodeAttributeToMethodVisitor(MemberVisitor memberVisitor)
    {
        this.memberVisitor = memberVisitor;
    }


    // Implementations for AttributeToMemberVisitor.

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    @Override
    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        method.accept(clazz, memberVisitor);
    }
}