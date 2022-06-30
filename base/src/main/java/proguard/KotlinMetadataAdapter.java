/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
 */

package proguard;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.classfile.io.kotlin.KotlinMetadataWriter;
import proguard.classfile.kotlin.KotlinMetadataVersion;
import proguard.classfile.kotlin.visitor.ReferencedKotlinMetadataVisitor;
import proguard.classfile.visitor.ClassCounter;
import proguard.pass.Pass;


public class KotlinMetadataAdapter
implements Pass
{
    private static final Logger logger = LogManager.getLogger(KotlinMetadataAdapter.class);


    @Override
    public void execute(AppView appView)
    {
        logger.info("Adapting Kotlin metadata...");

        ClassCounter counter = new ClassCounter();
        appView.programClassPool.classesAccept(
            new ReferencedKotlinMetadataVisitor(
            new KotlinMetadataWriter(
                (clazz, message) -> logger.warn(clazz.getName(), message),
                counter
            )));

        logger.info("  Number of Kotlin classes adapted:              " + counter.getCount());
    }
}
