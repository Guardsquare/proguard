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
 * This class applies the given member visitor to the referenced method of a Kotlin function.
 */
public class KotlinFunctionToMethodVisitor
implements   KotlinFunctionVisitor
{
    private final MemberVisitor memberVisitor;

    public KotlinFunctionToMethodVisitor(MemberVisitor memberVisitor) {
        this.memberVisitor = memberVisitor;
    }

    @Override
    public void visitAnyFunction(Clazz                  clazz,
                                 KotlinMetadata         kotlinMetadata,
                                 KotlinFunctionMetadata kotlinFunctionMetadata)
    {
        kotlinFunctionMetadata.referencedMethod.accept(kotlinFunctionMetadata.referencedMethodClass,
                                                       memberVisitor);
    }
}
