/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin.visitors;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.*;

public interface KotlinEffectExprVisitor
{
    void visitEffectExpression(Clazz                          clazz,
                               KotlinEffectMetadata           kotlinEffectMetadata,
                               KotlinEffectExpressionMetadata kotlinEffectExpressionMetadata);
}
