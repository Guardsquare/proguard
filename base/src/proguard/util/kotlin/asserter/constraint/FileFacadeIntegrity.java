/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 */

package proguard.util.kotlin.asserter.constraint;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.KotlinFileFacadeKindMetadata;

/**
 * @author James Hamilton
 */
public class FileFacadeIntegrity
extends AbstractKotlinMetadataConstraint
{


    @Override
    public void visitKotlinFileFacadeMetadata(Clazz clazz, KotlinFileFacadeKindMetadata kotlinFileFacadeKindMetadata)
    {
/*        new AssertUtil("File facade " + clazz.getName(), reporter).
            reportIfNullReference("referenced module", kotlinFileFacadeKindMetadata.referencedModule);*/
    }
}
