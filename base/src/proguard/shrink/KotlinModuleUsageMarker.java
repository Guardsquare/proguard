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

import proguard.resources.kotlinmodule.*;
import proguard.resources.kotlinmodule.visitor.KotlinModulePackageVisitor;
import proguard.resources.file.ResourceFile;
import proguard.resources.file.visitor.*;

/**
 * Mark Kotlin modules with the given {@link SimpleUsageMarker} if required.
 *
 * @author James Hamilton
 */
public class KotlinModuleUsageMarker
implements   ResourceFileVisitor,
             KotlinModulePackageVisitor
{
    private final SimpleUsageMarker usageMarker;

    public KotlinModuleUsageMarker(SimpleUsageMarker usageMarker)
    {
        this.usageMarker = usageMarker;
    }

    private boolean isUsed = false;

    // Implementations for ResourceFileVisitor.

    @Override
    public void visitKotlinModule(KotlinModule kotlinModule)
    {
        isUsed = false;

        kotlinModule.modulePackagesAccept(this);

        if (isUsed)
        {
            usageMarker.markAsUsed(kotlinModule);
        }
    }


    // Implementations for KotlinModulePackageVisitor.

    @Override
    public void visitKotlinModulePackage(KotlinModule kotlinModule, KotlinModulePackage kotlinModulePart)
    {
        // Mark the module as used if there are any file facades or multi-file class parts that are used.
        isUsed |=
            kotlinModulePart.referencedFileFacades.stream()
                .anyMatch(usageMarker::isUsed) ||
            kotlinModulePart.referencedMultiFileParts.values().stream()
                 .anyMatch(mfp -> usageMarker.isUsed(mfp.referencedFacadeClass));
    }
}
