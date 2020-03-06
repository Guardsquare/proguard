/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 GuardSquare NV
 */
package proguard.obfuscate.kotlin;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.*;
import proguard.classfile.kotlin.visitor.KotlinFunctionVisitor;

import static proguard.obfuscate.MemberObfuscator.*;

public class KotlinDefaultImplsMethodNameEqualizer
implements   KotlinFunctionVisitor
{

    @Override
    public void visitAnyFunction(Clazz                  clazz,
                                 KotlinMetadata         kotlinMetadata,
                                 KotlinFunctionMetadata kotlinFunctionMetadata)
    {
        // Ensure that the names of default implementation methods match.
        if (kotlinFunctionMetadata.referencedDefaultImplementationMethod != null)
        {
            setNewMemberName(
                kotlinFunctionMetadata.referencedDefaultImplementationMethod,
                newMemberName(kotlinFunctionMetadata.referencedMethod)
            );
        }
    }
}
