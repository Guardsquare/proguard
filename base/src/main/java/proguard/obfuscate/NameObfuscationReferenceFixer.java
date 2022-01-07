/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2022 Guardsquare NV
 */

package proguard.obfuscate;

import proguard.AppView;
import proguard.obfuscate.kotlin.KotlinModuleFixer;
import proguard.pass.Pass;
import proguard.resources.file.visitor.ResourceFileProcessingFlagFilter;
import proguard.util.ProcessingFlags;

/**
 * This pass fixes references between Java code and resource files.
 *
 * @see Obfuscator
 *
 * @author Tim Van Den Broecke
 */
public class NameObfuscationReferenceFixer
implements   Pass
{
    @Override
    public void execute(AppView appView)
    {
        if (appView.configuration.keepKotlinMetadata)
        {
            // Fix the Kotlin modules so the filename matches and the class names match.
            appView.resourceFilePool.resourceFilesAccept(
                new ResourceFileProcessingFlagFilter(0, ProcessingFlags.DONT_PROCESS_KOTLIN_MODULE,
                new KotlinModuleFixer()));
        }
    }
}
