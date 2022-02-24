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
package proguard.util.kotlin.asserter.constraint;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.KotlinDeclarationContainerMetadata;
import proguard.classfile.kotlin.visitor.KotlinMetadataVisitor;
import proguard.util.kotlin.asserter.AssertUtil;

/**
 */
public class DeclarationContainerIntegrity
extends      AbstractKotlinMetadataConstraint
    implements KotlinMetadataVisitor
{
    // Implementations for KotlinMetadataVisitor.

    @Override
    public void visitKotlinDeclarationContainerMetadata(Clazz clazz,
                                                        KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata)
    {
        AssertUtil assertUtil = new AssertUtil("Declaration container " +
                                               kotlinDeclarationContainerMetadata.ownerClassName,
                                               reporter,
                                               programClassPool,
                                               libraryClassPool);
        assertUtil
                .reportIfNullReference("referenced owner class", kotlinDeclarationContainerMetadata.ownerReferencedClass);
        assertUtil
                .reportIfClassDangling("referenced owner class", kotlinDeclarationContainerMetadata.ownerReferencedClass);
    }
}