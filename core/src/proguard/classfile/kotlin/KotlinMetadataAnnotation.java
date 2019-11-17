/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin;

import kotlinx.metadata.*;
import proguard.classfile.*;
import proguard.util.*;

import java.util.Map;

public class KotlinMetadataAnnotation
extends      SimpleVisitorAccepter
implements   VisitorAccepter
{
    public KmAnnotation kmAnnotation;
    public Clazz        referencedAnnotationClass;

    // Keys correspond to methods in Java class files.
    public Map<String, Method> referencedArgumentMethods;

    public KotlinMetadataAnnotation(KmAnnotation kmAnnotation)
    {
        this.kmAnnotation = kmAnnotation;
    }


    // Implementations for Object.
    @Override
    public String toString()
    {
        return kmAnnotation.getClassName() + "(" + kmAnnotation.getArguments() + ")";
    }
}
