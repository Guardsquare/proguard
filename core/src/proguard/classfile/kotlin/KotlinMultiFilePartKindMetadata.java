/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.visitors.KotlinMetadataVisitor;

import static proguard.classfile.kotlin.KotlinConstants.METADATA_KIND_MULTI_FILE_CLASS_PART;

public class KotlinMultiFilePartKindMetadata
extends KotlinDeclarationContainerMetadata
{
    public Clazz referencedFacade;


    public KotlinMultiFilePartKindMetadata(int[]  mv,
                                           int[]  bv,
                                           int    xi,
                                           String xs,
                                           String pn)
    {
        super(METADATA_KIND_MULTI_FILE_CLASS_PART, mv, bv, xi, xs, pn);
    }


    @Override
    public void accept(Clazz clazz, KotlinMetadataVisitor kotlinMetadataVisitor)
    {
        kotlinMetadataVisitor.visitKotlinMultiFilePartMetadata(clazz, this);
    }


    // Implementations for Object.
    @Override
    public String toString()
    {
        return "Kotlin file part";
    }
}
