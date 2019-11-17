/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2018 GuardSquare NV
 */
package proguard.resources.file.visitor;

import proguard.resources.file.ResourceFile;

/**
 * Abstract class providing default implementations for ResourceFileVisitor.
 *
 * @author Johan Leys
 */
public abstract class SimplifiedResourceFileVisitor
{
    // Simplifications for ResourceFileVisitor.

    public void visitAnyResourceFile(ResourceFile resourceFile)
    {
        throw new UnsupportedOperationException("Method must be overridden in ["+this.getClass().getName()+"] if ever called");
    }


    // ...


    public void visitResourceFile(ResourceFile resourceFile)
    {
        visitAnyResourceFile(resourceFile);
    }
}
