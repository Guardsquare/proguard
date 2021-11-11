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
import proguard.classfile.kotlin.*;
import proguard.classfile.kotlin.visitor.*;
import proguard.util.kotlin.asserter.AssertUtil;

/**
 * This class checks the assumption: All properties need a JVM signature for their getter
 */
public class KmAnnotationIntegrity
extends      AbstractKotlinMetadataConstraint
    implements KotlinTypeVisitor,
               KotlinTypeAliasVisitor,
               KotlinTypeParameterVisitor,
               KotlinAnnotationVisitor
{

    private AssertUtil util;


    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata)
    {
        kotlinMetadata.accept(clazz, new MultiKotlinMetadataVisitor(
            new AllTypeVisitor(this),
            new AllTypeAliasVisitor(this),
            new AllTypeParameterVisitor(this)
        ));
    }

    // Implementations for KotlinTypeVisitor.
    @Override
    public void visitAnyType(Clazz clazz, KotlinTypeMetadata type)
    {
        util = new AssertUtil("Type", reporter);
        type.annotationsAccept(clazz, this);

    }

    // Implementations for KotlinTypeAliasVisitor.
    @Override
    public void visitTypeAlias(Clazz clazz,
                               KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                               KotlinTypeAliasMetadata kotlinTypeAliasMetadata)
    {
        util = new AssertUtil("Type alias", reporter);
        kotlinTypeAliasMetadata.annotationsAccept(clazz, this);
    }

    // Implementations for KotlinTypeParameterVisitor.
    @Override
    public void visitAnyTypeParameter(Clazz clazz,
                                      KotlinTypeParameterMetadata kotlinTypeParameterMetadata)
    {
        util = new AssertUtil("Type parameter", reporter);
        kotlinTypeParameterMetadata.annotationsAccept(clazz, this);
    }

    @Override
    public void visitAnyAnnotation(Clazz clazz, KotlinMetadataAnnotation antn)
    {
        util.reportIfNullReference("annotation class", antn.referencedAnnotationClass);
        antn.referencedArgumentMethods
            .values()
            .forEach(annotation -> util.reportIfNullReference("annotation method", annotation));
    }
}
