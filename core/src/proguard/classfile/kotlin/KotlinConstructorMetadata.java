/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin;

import kotlinx.metadata.jvm.JvmMethodSignature;
import proguard.classfile.*;
import proguard.classfile.kotlin.flags.KotlinConstructorFlags;
import proguard.classfile.kotlin.visitors.*;
import proguard.classfile.visitor.MemberVisitor;
import proguard.util.*;

import java.util.*;

public class KotlinConstructorMetadata
extends    SimpleVisitorAccepter
implements VisitorAccepter
{
    public List<KotlinValueParameterMetadata> valueParameters;

    public KotlinVersionRequirementMetadata versionRequirement;

    public KotlinConstructorFlags flags;

    // Extensions.
    public JvmMethodSignature jvmSignature;
    public Method             referencedMethod;


    public KotlinConstructorMetadata(int flags)
    {
        this.flags = new KotlinConstructorFlags(flags);
    }


    public void accept(Clazz                    clazz,
                       KotlinClassKindMetadata  kotlinClassKindMetadata,
                       KotlinConstructorVisitor kotlinConstructorVisitor)
    {
        kotlinConstructorVisitor.visitConstructor(clazz, kotlinClassKindMetadata, this);
    }


    public void valueParametersAccept(Clazz                       clazz,
                                      KotlinClassKindMetadata     kotlinClassKindMetadata,
                                      KotlinValueParameterVisitor kotlinValueParameterVisitor)
    {
        for (KotlinValueParameterMetadata valueParameter : valueParameters)
        {
            valueParameter.accept(clazz, kotlinClassKindMetadata, this, kotlinValueParameterVisitor);
        }
    }


    public void versionRequirementAccept(Clazz                           clazz,
                                         KotlinMetadata                  kotlinMetadata,
                                         KotlinVersionRequirementVisitor kotlinVersionRequirementVisitor)
    {
        if (versionRequirement != null)
        {
            versionRequirement.accept(clazz, kotlinMetadata, this, kotlinVersionRequirementVisitor);
        }
    }


    public void referencedMethodAccept(Clazz clazz, MemberVisitor methodVisitor)
    {
        if (referencedMethod != null)
        {
            referencedMethod.accept(clazz, methodVisitor);
        }
    }


    // Implementations for Object.
    @Override
    public String toString()
    {
        return "Kotlin " +
               (flags.isPrimary ? "primary " : "") +
               "constructor";
    }
}
