/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin.visitors.filter;

import proguard.classfile.*;
import proguard.classfile.attribute.annotation.visitor.*;
import proguard.classfile.attribute.visitor.AllAttributeVisitor;
import proguard.classfile.kotlin.KotlinConstants;
import proguard.classfile.visitor.ClassVisitor;

public class KotlinClassFilter
implements ClassVisitor
{
    private final ClassVisitor filteredDelegate;

    public KotlinClassFilter(ClassVisitor delegate)
    {
        this.filteredDelegate =
            new AllAttributeVisitor(
            new AllAnnotationVisitor(
            new AnnotationTypeFilter(KotlinConstants.TYPE_KOTLIN_METADATA,
                                     new AnnotationToAnnotatedClassVisitor(delegate))));
    }

    // Implementations for ClassVisitor.

    @Override
    public void visitProgramClass(ProgramClass programClass)
    {
        programClass.accept(filteredDelegate);
    }

    @Override
    public void visitLibraryClass(LibraryClass libraryClass)
    {
        libraryClass.accept(filteredDelegate);
    }
}
