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

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.*;
import proguard.classfile.kotlin.visitor.KotlinMetadataVisitor;
import proguard.util.*;

import static proguard.classfile.util.ClassUtil.internalPackagePrefix;
import static proguard.classfile.util.ClassUtil.internalSimpleClassName;
import static proguard.obfuscate.ClassObfuscator.*;

/**
 * Ensure that multi-file class parts and multi-file facades are kept in the same package.
 *
 * @author James Hamilton
 */
public class KotlinMultiFileFacadeFixer
implements   KotlinMetadataVisitor
{
    // Implementations for KotlinMetadataVisitor.

    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata) {}

    @Override
    public void visitKotlinMultiFileFacadeMetadata(Clazz                             clazz,
                                                   KotlinMultiFileFacadeKindMetadata kotlinMultiFileFacadeKindMetadata)
    {
        String packagePrefix = internalPackagePrefix(hasOriginalClassName(clazz) ? clazz.getName() : newClassName(clazz));

        for (Clazz referencedPartClass : kotlinMultiFileFacadeKindMetadata.referencedPartClasses)
        {
            if (dontObfuscate(referencedPartClass))
            {
                packagePrefix = internalPackagePrefix(referencedPartClass.getName());
                break;
            }
        }

        for (Clazz ref : kotlinMultiFileFacadeKindMetadata.referencedPartClasses)
        {
            setNewClassName(ref, packagePrefix +
                                 (internalSimpleClassName(hasOriginalClassName(ref) ? ref.getName() : newClassName(ref))));
        }

        String className = newClassName(clazz);
        if (className == null)
        {
            className = clazz.getName();
        }

        setNewClassName(clazz, packagePrefix + internalSimpleClassName(className));
    }

    // Small helper methods.

    private static boolean dontObfuscate(Processable processable)
    {
        return (processable.getProcessingFlags() & ProcessingFlags.DONT_OBFUSCATE) != 0;
    }
}
