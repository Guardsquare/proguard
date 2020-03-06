/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 GuardSquare NV
 */
package proguard.obfuscate.kotlin;

import proguard.classfile.*;
import proguard.classfile.kotlin.*;
import proguard.classfile.kotlin.visitor.KotlinFunctionVisitor;

import static proguard.classfile.kotlin.KotlinConstants.*;
import static proguard.obfuscate.MemberObfuscator.*;

public class KotlinDefaultMethodNameEqualizer
implements   KotlinFunctionVisitor
{

    @Override
    public void visitAnyFunction(Clazz                  clazz,
                                 KotlinMetadata         kotlinMetadata,
                                 KotlinFunctionMetadata kotlinFunctionMetadata)
    {
        // Default parameter methods should still have the $default suffix.
        if (kotlinFunctionMetadata.referencedDefaultMethod != null)
        {
            setNewMemberName(
                kotlinFunctionMetadata.referencedDefaultMethod,
                newMemberName(kotlinFunctionMetadata.referencedMethod) + DEFAULT_METHOD_SUFFIX
            );
        }
    }
}
