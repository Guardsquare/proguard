/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin.visitors;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.*;
import proguard.classfile.visitor.MemberVisitor;

/**
 * This class applies the given member visitor to the referenced default method of a Kotlin function.
 */
public class KotlinFunctionToDefaultMethodVisitor
implements   KotlinFunctionVisitor
{
    private final MemberVisitor memberVisitor;

    public KotlinFunctionToDefaultMethodVisitor(MemberVisitor memberVisitor) {
        this.memberVisitor = memberVisitor;
    }

    @Override
    public void visitAnyFunction(Clazz                  clazz,
                                 KotlinMetadata         kotlinMetadata,
                                 KotlinFunctionMetadata kotlinFunctionMetadata)
    {
        if (kotlinFunctionMetadata.referencedDefaultMethod != null)
        {
            kotlinFunctionMetadata.referencedDefaultMethod.accept(
                kotlinFunctionMetadata.referencedDefaultMethodClass, memberVisitor);
        }
    }
}
