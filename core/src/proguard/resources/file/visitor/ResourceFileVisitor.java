/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2018 GuardSquare NV
 */
package proguard.resources.file.visitor;

import proguard.resources.file.ResourceFile;

/**
 * This interface specifies the methods for a visitor of ResourceFile instances.
 *
 * @author Johan Leys
 */
public interface ResourceFileVisitor
//extends        ...Visitor
{
    public void visitResourceFile(ResourceFile resourceFile);
}