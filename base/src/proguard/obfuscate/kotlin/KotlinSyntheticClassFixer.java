/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 GuardSquare NV
 */
package proguard.obfuscate.kotlin;

import proguard.classfile.*;
import proguard.classfile.kotlin.*;
import proguard.classfile.kotlin.visitor.*;
import proguard.classfile.visitor.*;

import static proguard.classfile.kotlin.KotlinConstants.*;
import static proguard.obfuscate.ClassObfuscator.*;
import static proguard.obfuscate.MemberObfuscator.*;

/**
 * Synthetic classes are created for lambdas, $DefaultImpls and $WhenMappings.
 *
 * This class ensures $DefaultImpls and $WhenMappings classes are correctly named
 * and lambda classes have their $field prefixed with a $.
 */
public class KotlinSyntheticClassFixer
implements   KotlinMetadataVisitor
{

    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata) {}

    @Override
    public void visitKotlinClassMetadata(Clazz clazz, KotlinClassKindMetadata kotlinClassKindMetadata)
    {
        // If there is a default implementations class, name it as this one but with a $DefaultImpls suffix.
        if (kotlinClassKindMetadata.flags.isInterface && kotlinClassKindMetadata.referencedDefaultImplsClass != null)
        {
            String className = newClassName(kotlinClassKindMetadata.referencedClass);

            final String defaultImplsClassName = className.endsWith(DEFAULT_IMPLEMENTATIONS_SUFFIX) ?
                                                 className :
                                                 className + DEFAULT_IMPLEMENTATIONS_SUFFIX;

            kotlinClassKindMetadata.accept(clazz,
                new KotlinInterfaceToDefaultImplsClassVisitor(
                new ClassVisitor()
                {
                    @Override
                    public void visitProgramClass(ProgramClass programClass)
                    {
                        setNewClassName(programClass, defaultImplsClassName);
                    }

                    @Override
                    public void visitLibraryClass(LibraryClass libraryClass) {}
                }));
        }
    }

    @Override
    public void visitKotlinSyntheticClassMetadata(Clazz                            clazz,
                                                  KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata)
    {
        // If this is a $WhenMappings class ensure that it has the suffix.
        if (kotlinSyntheticClassKindMetadata.flavor == KotlinSyntheticClassKindMetadata.Flavor.WHEN_MAPPINGS)
        {
            String originalName = newClassName(clazz);

            if (!originalName.endsWith(WHEN_MAPPINGS_SUFFIX))
            {
                setNewClassName(clazz, originalName + WHEN_MAPPINGS_SUFFIX);
            }
        }
        else if (kotlinSyntheticClassKindMetadata.flavor == KotlinSyntheticClassKindMetadata.Flavor.LAMBDA)
        {
            clazz.accept(
                // Obfuscate the lambda fields in synthetic lambda classes, but ensuring $ prefix is kept.
                new AllFieldVisitor(
                    new MemberNameFilter("$*",
                    new MemberVisitor()
                    {
                        @Override
                        public void visitProgramField(ProgramClass programClass, ProgramField programField)
                        {
                            setNewMemberName(programField, "$" + newMemberName(programField));
                        }

                        public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {}
                        public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField) {}
                        public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod) {}
                    }
                ))
            );
        }
    }
}