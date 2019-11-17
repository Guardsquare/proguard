/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin;

import proguard.classfile.*;
import proguard.classfile.kotlin.visitors.*;

import java.util.List;

import static proguard.classfile.kotlin.KotlinConstants.METADATA_KIND_SYNTHETIC_CLASS;

public class KotlinSyntheticClassKindMetadata
extends KotlinMetadata
{
    public List<KotlinFunctionMetadata> functions;

    public String className;
    public Clazz  referencedClass;

    public final Kind kind;

    public enum Kind {
        LAMBDA,
        DEFAULT_IMPLS,
        WHEN_MAPPINGS,
        UNKNOWN
    }

    public KotlinSyntheticClassKindMetadata(int[]  mv,
                                            int[]  bv,
                                            int    xi,
                                            String xs,
                                            String pn,
                                            Kind kind)
    {
        super(METADATA_KIND_SYNTHETIC_CLASS, mv, bv, xi, xs, pn);
        this.kind = kind;
    }


    @Override
    public void accept(Clazz clazz, KotlinMetadataVisitor kotlinMetadataVisitor)
    {
        kotlinMetadataVisitor.visitKotlinSyntheticClassMetadata(clazz, this);
    }


    public void functionsAccept(Clazz clazz, KotlinFunctionVisitor kotlinFunctionVisitor)
    {
        for (KotlinFunctionMetadata function : functions)
        {
            function.accept(clazz, this, kotlinFunctionVisitor);
        }
    }


    // Implementations for Object.
    @Override
    public String toString()
    {
        String functionName = functions.size() > 0 ? functions.get(0).name : "//";
        return "Kotlin synthetic class(" + functionName + ")";
    }
}
