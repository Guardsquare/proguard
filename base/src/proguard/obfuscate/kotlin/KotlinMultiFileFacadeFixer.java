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
import proguard.util.*;

import static proguard.classfile.util.ClassUtil.internalPackagePrefix;
import static proguard.classfile.util.ClassUtil.internalSimpleClassName;
import static proguard.obfuscate.ClassObfuscator.*;

/**
 * Ensure that multi-file class parts and multi-file facades are kept in the same package.
 *
 * @author James Hamilton
 */
public class KotlinMultiFileFacadeFixer
implements   KotlinMetadataVisitor
{
    // Implementations for KotlinMetadataVisitor.

    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata) {}

    @Override
    public void visitKotlinMultiFileFacadeMetadata(Clazz                             clazz,
                                                   KotlinMultiFileFacadeKindMetadata kotlinMultiFileFacadeKindMetadata)
    {
        String packagePrefix = internalPackagePrefix(hasOriginalClassName(clazz) ? clazz.getName() : newClassName(clazz));

        for (Clazz referencedPartClass : kotlinMultiFileFacadeKindMetadata.referencedPartClasses)
        {
            if (dontObfuscate(referencedPartClass))
            {
                packagePrefix = internalPackagePrefix(referencedPartClass.getName());
                break;
            }
        }

        for (Clazz ref : kotlinMultiFileFacadeKindMetadata.referencedPartClasses)
        {
            setNewClassName(ref, packagePrefix +
                                 (internalSimpleClassName(hasOriginalClassName(ref) ? ref.getName() : newClassName(ref))));
        }

        String className = newClassName(clazz);
        if (className == null)
        {
            className = clazz.getName();
        }

        setNewClassName(clazz, packagePrefix + internalSimpleClassName(className));
    }

    // Small helper methods.

    private static boolean dontObfuscate(Processable processable)
    {
        return (processable.getProcessingFlags() & ProcessingFlags.DONT_OBFUSCATE) != 0;
    }
}
