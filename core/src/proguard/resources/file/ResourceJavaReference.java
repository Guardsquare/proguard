/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2018 GuardSquare NV
 */
package proguard.resources.file;

import proguard.classfile.Clazz;

/**
 * Represents a reference to a Java class from a resource file.
 *
 * @author Lars Vandenbergh
 */
public class ResourceJavaReference
{
    public String externalClassName;
    public Clazz  referencedClass;


    /**
     * Creates an uninitialized ResourceJavaReference.
     */
    public ResourceJavaReference()
    {
    }


    /**
     * Creates an initialized ResourceJavaReference.
     */
    public ResourceJavaReference(String externalClassName,
                                 Clazz  referencedClass)
    {
        this.externalClassName = externalClassName;
        this.referencedClass   = referencedClass;
    }
}
