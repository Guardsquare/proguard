/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin.visitors;

import proguard.classfile.*;
import proguard.classfile.visitor.ClassVisitor;

public class KotlinMetadataRemover
implements   ClassVisitor
{
    @Override
    public void visitProgramClass(ProgramClass programClass)
    {
        programClass.kotlinMetadata = null;
    }

    @Override
    public void visitLibraryClass(LibraryClass libraryClass)
    {
        libraryClass.kotlinMetadata = null;
    }
}