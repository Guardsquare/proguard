/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 */

package proguard.util.kotlin.asserter.constraint;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.KotlinMultiFilePartKindMetadata;

/**
 * @author James Hamilton
 */
public class MultiFilePartIntegrity
extends      AbstractKotlinMetadataConstraint
{
    @Override
    public void visitKotlinMultiFilePartMetadata(Clazz clazz,
                                                 KotlinMultiFilePartKindMetadata kotlinMultiFilePartKindMetadata)
    {
/*        new AssertUtil("Multi-file part " + clazz.getName(), reporter).
            reportIfNullReference("referenced module", kotlinMultiFilePartKindMetadata.referencedModule);*/
    }
}
