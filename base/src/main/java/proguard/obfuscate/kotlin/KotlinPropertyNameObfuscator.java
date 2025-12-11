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

public class KotlinPropertyNameObfuscator
implements   KotlinMetadataVisitor,

             // Implementation interfaces.
             KotlinPropertyVisitor
{
    private final NameFactory nameFactory;

    public KotlinPropertyNameObfuscator(NameFactory nameFactory)
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
        kotlinDeclarationContainerMetadata.propertiesAccept(clazz, this);
    }

    // Implementations for KotlinPropertyVisitor.
    @Override
    public void visitAnyProperty(Clazz                              clazz,
                                 KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                 KotlinPropertyMetadata             kotlinPropertyMetadata)
    {
        // Don't rename a property if its backing field or
        // any accessor are explicitly kept.
        if ((kotlinPropertyMetadata.referencedBackingField != null &&
             (kotlinPropertyMetadata.referencedBackingField.getProcessingFlags() & ProcessingFlags.DONT_OBFUSCATE) != 0) ||

            (kotlinPropertyMetadata.getterMetadata != null &&
                    kotlinPropertyMetadata.getterMetadata.referencedMethod != null &&
                    (kotlinPropertyMetadata.getterMetadata.referencedMethod.getProcessingFlags() & ProcessingFlags.DONT_OBFUSCATE) != 0) ||

            (kotlinPropertyMetadata.setterMetadata != null &&
                    kotlinPropertyMetadata.setterMetadata.referencedMethod != null &&
                    (kotlinPropertyMetadata.setterMetadata.referencedMethod.getProcessingFlags() & ProcessingFlags.DONT_OBFUSCATE) != 0))
        {
            return;
        }

        kotlinPropertyMetadata.setProcessingInfo(nameFactory.nextName());
    }
}
