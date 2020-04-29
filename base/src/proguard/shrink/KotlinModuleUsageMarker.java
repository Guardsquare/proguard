/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
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
