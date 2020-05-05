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

package proguard.shrink;

import proguard.classfile.kotlin.*;
import proguard.resources.kotlinmodule.*;
import proguard.resources.kotlinmodule.visitor.KotlinModulePackageVisitor;
import proguard.resources.file.ResourceFile;
import proguard.resources.file.visitor.*;

import java.util.*;

/**
 * Shrink the contents of the Kotlin module based on if the referenced
 * classes are used or not.
 *
 * @author James Hamilton
 */
public class KotlinModuleShrinker
implements   ResourceFileVisitor,
             KotlinModulePackageVisitor
{
    private final SimpleUsageMarker usageMarker;

    KotlinModuleShrinker(SimpleUsageMarker usageMarker)
    {
        this.usageMarker = usageMarker;
    }


    // Implementations for ResourceFileVisitor.

    @Override
    public void visitKotlinModule(KotlinModule kotlinModule)
    {
        kotlinModule.modulePackagesAccept(this);
    }


    // Implementations for KotlinModulePackageVisitor.

    @Override
    public void visitKotlinModulePackage(KotlinModule kotlinModule, KotlinModulePackage kotlinModulePart)
    {
         // Shrink the referenced facades.
        for (int k = kotlinModulePart.referencedFileFacades.size() - 1; k >= 0; k--)
        {
            KotlinFileFacadeKindMetadata kotlinFileFacadeKindMetadata = kotlinModulePart.referencedFileFacades.get(k);

            if (!usageMarker.isUsed(kotlinFileFacadeKindMetadata))
            {
                kotlinModulePart.fileFacadeNames      .remove(k);
                kotlinModulePart.referencedFileFacades.remove(k);
            }
        }

         // Shrink the multi-file-part -> facade map.
         List<String> partsToRemove   = new ArrayList<>();
         Set<String>  facadesToRemove = new HashSet<>();

         kotlinModulePart.multiFileClassParts.forEach((partName, facadeName) -> {
             KotlinMultiFilePartKindMetadata referencedMultiFilePart =
                 kotlinModulePart.referencedMultiFileParts.get(partName);

             if (!usageMarker.isUsed(referencedMultiFilePart.ownerReferencedClass))
             {
                 partsToRemove.add(partName);
             }

             if (!usageMarker.isUsed(referencedMultiFilePart.referencedFacadeClass))
             {
                 facadesToRemove.add(facadeName);
             }
         });

         for (String partName : partsToRemove)
         {
             kotlinModulePart.multiFileClassParts     .remove(partName);
             kotlinModulePart.referencedMultiFileParts.remove(partName);
         }

         for (String facadeName : facadesToRemove)
         {
             kotlinModulePart.multiFileClassParts.values().removeIf(f -> f.equals(facadeName));
         }
    }
}