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
import proguard.classfile.kotlin.visitor.KotlinMetadataVisitor;
import proguard.classfile.visitor.MemberVisitor;

import static proguard.classfile.kotlin.KotlinConstants.KOTLIN_OBJECT_INSTANCE_FIELD_NAME;
import static proguard.obfuscate.MemberObfuscator.setNewMemberName;

/**
 * This fixer ensures that the INSTANCE field in an object class remains named INSTANCE.
 *
 * @author James Hamilton
 */
public class KotlinObjectFixer
implements KotlinMetadataVisitor
{
    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata) {}


    @Override
    public void visitKotlinClassMetadata(Clazz clazz, KotlinClassKindMetadata kotlinClassKindMetadata)
    {
        if (kotlinClassKindMetadata.flags.isObject)
        {
            clazz.fieldAccept(KOTLIN_OBJECT_INSTANCE_FIELD_NAME, null,
                new MemberVisitor()
                {
                    @Override
                    public void visitProgramField(ProgramClass programClass, ProgramField programField)
                    {
                        setNewMemberName(programField, KOTLIN_OBJECT_INSTANCE_FIELD_NAME);
                    }

                    @Override
                    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {}
                    @Override
                    public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField) {}
                    @Override
                    public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod) {}
                }
            );
        }
    }
}
