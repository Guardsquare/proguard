/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 GuardSquare NV
 */
package proguard.obfuscate.kotlin;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.*;
import proguard.classfile.kotlin.visitor.KotlinMetadataVisitor;
import proguard.classfile.util.ClassUtil;
import proguard.obfuscate.MemberObfuscator;

public class KotlinCompanionEqualizer
implements   KotlinMetadataVisitor
{

    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata) {}

    @Override
    public void visitKotlinClassMetadata(Clazz clazz, KotlinClassKindMetadata kotlinClassKindMetadata)
    {
        if (kotlinClassKindMetadata.companionObjectName != null)
        {
            String newCompanionClassName = (String)kotlinClassKindMetadata.referencedCompanionClass.getProcessingInfo();

            // The name should be an inner class, but if for some reason it isn't
            // then don't try to rename it as it could lead to problems.
            // The Kotlin asserter will check the field name, so will throw the metadata away if
            // it wasn't named correctly.

            if (newCompanionClassName.contains("$"))
            {
                MemberObfuscator.setNewMemberName(kotlinClassKindMetadata.referencedCompanionField,
                                                  ClassUtil.internalSimpleClassName(newCompanionClassName));
            }
        }
    }
}