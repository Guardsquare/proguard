/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin;

import proguard.classfile.*;
import proguard.classfile.kotlin.visitors.*;

import static proguard.classfile.kotlin.KotlinConstants.METADATA_KIND_FILE_FACADE;

public class KotlinFileFacadeKindMetadata
extends KotlinDeclarationContainerMetadata
{

    public KotlinFileFacadeKindMetadata(int[]  mv,
                                        int[]  bv,
                                        int    xi,
                                        String xs,
                                        String pn)
    {
        super(METADATA_KIND_FILE_FACADE, mv, bv, xi, xs, pn);
    }


    @Override
    public void accept(Clazz clazz, KotlinMetadataVisitor kotlinMetadataVisitor)
    {
        kotlinMetadataVisitor.visitKotlinFileFacadeMetadata(clazz, this);
    }


    // Implementations for Object.
    @Override
    public String toString()
    {
        return "Kotlin file facade(" + ownerClassName + ")";
    }
}
