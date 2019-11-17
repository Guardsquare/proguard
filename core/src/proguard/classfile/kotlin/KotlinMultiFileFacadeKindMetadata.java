/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.visitors.*;

import java.util.*;

import static proguard.classfile.kotlin.KotlinConstants.METADATA_KIND_MULTI_FILE_CLASS_FACADE;

public class KotlinMultiFileFacadeKindMetadata
extends KotlinMetadata
{
    public List<String> partClassNames;
    public List<Clazz>  referencedPartClasses;

    public KotlinMultiFileFacadeKindMetadata(int[]    mv,
                                             int[]    bv,
                                             String[] d1,
                                             int      xi,
                                             String   xs,
                                             String   pn)
    {
        super(METADATA_KIND_MULTI_FILE_CLASS_FACADE, mv, bv, xi, xs, pn);

        this.partClassNames = new ArrayList<>(Arrays.asList(d1));
    }


    @Override
    public void accept(Clazz clazz, KotlinMetadataVisitor kotlinMetadataVisitor)
    {
        kotlinMetadataVisitor.visitKotlinMultiFileFacadeMetadata(clazz, this);
    }


    // Implementations for Object.
    @Override
    public String toString()
    {
        return "Kotlin multi-file facade";
    }
}
