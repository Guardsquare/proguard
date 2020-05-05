/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
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
                new ProgramClassFilter(_clazz -> setNewClassName(_clazz, defaultImplsClassName))));
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