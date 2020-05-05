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
public class PropertyIntegrity
extends      AbstractKotlinMetadataConstraint
    implements KotlinPropertyVisitor
{

    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata)
    {
        kotlinMetadata.accept(clazz, new AllKotlinPropertiesVisitor(this));
    }

    // Implementations for KotlinPropertyVisitor.

    @Override
    public void visitAnyProperty(Clazz clazz,
                                 KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                 KotlinPropertyMetadata kotlinPropertyMetadata)
    {
        AssertUtil util = new AssertUtil("Property " + kotlinPropertyMetadata.name, reporter);

        util.reportIfNull("backingFieldSignature, getterSignature or setterSignature",
                          kotlinPropertyMetadata.backingFieldSignature,
                          kotlinPropertyMetadata.getterSignature,
                          kotlinPropertyMetadata.setterSignature);

        if (kotlinPropertyMetadata.backingFieldSignature  != null)
        {
            util.reportIfNullReference("backing field class",
                                       kotlinPropertyMetadata.referencedBackingFieldClass);
            util.reportIfNullReference("backing field",
                                       kotlinPropertyMetadata.referencedBackingField);
            util.reportIfFieldDangling("backing field",
                                       kotlinPropertyMetadata.referencedBackingFieldClass,
                                       kotlinPropertyMetadata.referencedBackingField);
        }

        if (kotlinPropertyMetadata.getterSignature != null)
        {
            util.reportIfNullReference("getter", kotlinPropertyMetadata.referencedGetterMethod);
        }

        if (kotlinPropertyMetadata.setterSignature != null)
        {
            util.reportIfNullReference("setter", kotlinPropertyMetadata.referencedSetterMethod);
        }

        if (kotlinPropertyMetadata.syntheticMethodForAnnotations != null)
        {
            util.reportIfNullReference("synthetic annotations method class",
                                       kotlinPropertyMetadata.referencedSyntheticMethodClass);
            util.reportIfNullReference("synthetic annotations method",
                                       kotlinPropertyMetadata.referencedSyntheticMethodForAnnotations);
            util.reportIfMethodDangling("synthetic annotations method",
                                        kotlinPropertyMetadata.referencedSyntheticMethodClass,
                                        kotlinPropertyMetadata.referencedSyntheticMethodForAnnotations);
        }
    }
}
