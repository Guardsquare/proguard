/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin;

import proguard.classfile.*;
import proguard.classfile.kotlin.visitors.KotlinMetadataVisitor;
import proguard.classfile.visitor.ClassVisitor;

/**
 * Initializes the kotlin metadata for each Kotlin class. After initialization, all
 * info from the annotation is represented in the Clazz's `kotlinMetadata` field. All
 * arrays in kotlinMetadata are initialized, even if empty.
 */
public class ReferencedKotlinMetadataVisitor
implements   ClassVisitor
{
    private final KotlinMetadataVisitor kotlinMetadataVisitor;

    public ReferencedKotlinMetadataVisitor(KotlinMetadataVisitor kotlinMetadataVisitor)
    {
        this.kotlinMetadataVisitor = kotlinMetadataVisitor;
    }

    // Implementations for ClassVisitor.

    @Override
    public void visitProgramClass(ProgramClass programClass)
    {
        programClass.kotlinMetadataAccept(kotlinMetadataVisitor);
    }

    @Override
    public void visitLibraryClass(LibraryClass libraryClass)
    {
        libraryClass.kotlinMetadataAccept(kotlinMetadataVisitor);
    }
}
