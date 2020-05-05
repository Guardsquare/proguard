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
import proguard.classfile.kotlin.visitor.*;
import proguard.obfuscate.NameFactory;
import proguard.util.ProcessingFlags;

public class KotlinAliasNameObfuscator
implements   KotlinMetadataVisitor,

             // Implementation interfaces.
             KotlinTypeAliasVisitor,
             KotlinTypeVisitor
{
    private final NameFactory nameFactory;

    public KotlinAliasNameObfuscator(NameFactory nameFactory)
    {
        this.nameFactory = nameFactory;
    }


    // Implementations for KotlinMetadataVisitor.
    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata) {}


    @Override
    public void visitKotlinDeclarationContainerMetadata(Clazz                              clazz,
                                                        KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata)
    {
        this.nameFactory.reset();
        kotlinDeclarationContainerMetadata.typeAliasesAccept(clazz, this);
    }

    @Override
    public void visitAnyType(Clazz clazz, KotlinTypeMetadata kotlinTypeMetadata) {}

    @Override
    public void visitTypeAlias(Clazz                              clazz,
                               KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                               KotlinTypeAliasMetadata            kotlinTypeAliasMetadata)
    {
        kotlinTypeAliasMetadata.expandedTypeAccept(clazz, kotlinDeclarationContainerMetadata, this);
    }

    @Override
    public void visitAliasExpandedType(Clazz clazz,
                                       KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                       KotlinTypeAliasMetadata kotlinTypeAliasMetadata,
                                       KotlinTypeMetadata kotlinTypeMetadata)
    {
        // If the expanded type class is kept, assume it's a public API so keep the alias also.
        if ((kotlinTypeMetadata.referencedClass.getProcessingFlags() & ProcessingFlags.DONT_OBFUSCATE) == 0)
        {
            kotlinTypeAliasMetadata.name = nameFactory.nextName();
        }
    }
}
