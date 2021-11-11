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

public class ConstructorIntegrity
extends      AbstractKotlinMetadataConstraint
    implements KotlinConstructorVisitor
{

    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata)
    {
        kotlinMetadata.accept(clazz, new AllConstructorsVisitor(this));
    }

    @Override
    public void visitConstructor(Clazz clazz,
                                 KotlinClassKindMetadata kotlinClassKindMetadata,
                                 KotlinConstructorMetadata kotlinConstructorMetadata)
    {
        if (!kotlinClassKindMetadata.flags.isAnnotationClass)
        {
            AssertUtil util = new AssertUtil("Constructor", reporter);
            util.reportIfNullReference("constructor referencedMethod", kotlinConstructorMetadata.referencedMethod);
            util.reportIfMethodDangling("constructor referencedMethod",
                                        clazz, kotlinConstructorMetadata.referencedMethod);
        }
    }
}
