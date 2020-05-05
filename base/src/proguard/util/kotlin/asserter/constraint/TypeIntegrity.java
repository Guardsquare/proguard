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
public class TypeIntegrity
extends    AbstractKotlinMetadataConstraint
    implements KotlinTypeVisitor
{

    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata)
    {
        kotlinMetadata.accept(clazz, new AllTypeVisitor(this));
    }


    // Implementations for KotlinPropertyVisitor.
    @Override
    public void visitAnyType(Clazz clazz,
                             KotlinTypeMetadata type)
    {
        AssertUtil util = new AssertUtil("Type", reporter);

        if (type.className != null)
        {
            util.reportIfNullReference("class \"" + type.className + "\"", type.referencedClass);

            if (type.aliasName != null)
            {
                reporter.report("Type cannot have both className (" + type.className + ") and aliasName (" + type.aliasName + ")");
            }

            if (type.typeParamID >= 0)
            {
                reporter.report("Type cannot have both className (" + type.className + ") and typeParamID (" + type.typeParamID + ")");
            }
        }

        if (type.aliasName != null)
        {
            util.reportIfNullReference("type alias \"" + type.aliasName + "\"", type.referencedTypeAlias);

            if (type.className != null)
            {
                reporter.report("Type cannot have both className (" + type.className + ") and aliasName (" + type.aliasName + ")");
            }

            if (type.typeParamID >= 0)
            {
                reporter.report("Type cannot have both aliasName (" + type.aliasName + ") and typeParamID (" + type.typeParamID + ")");
            }
        }
    }
}
