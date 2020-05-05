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
import proguard.classfile.kotlin.visitor.KotlinFunctionVisitor;

import static proguard.classfile.kotlin.KotlinConstants.*;
import static proguard.obfuscate.MemberObfuscator.*;

public class KotlinDefaultMethodNameEqualizer
implements   KotlinFunctionVisitor
{

    @Override
    public void visitAnyFunction(Clazz                  clazz,
                                 KotlinMetadata         kotlinMetadata,
                                 KotlinFunctionMetadata kotlinFunctionMetadata)
    {
        // Default parameter methods should still have the $default suffix.
        if (kotlinFunctionMetadata.referencedDefaultMethod != null)
        {
            setNewMemberName(
                kotlinFunctionMetadata.referencedDefaultMethod,
                newMemberName(kotlinFunctionMetadata.referencedMethod) + DEFAULT_METHOD_SUFFIX
            );
        }
    }
}
