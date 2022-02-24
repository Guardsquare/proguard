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

    // TODO(T14074): Fix the Kotlin metadata version until Kotlin 1.7 is released. Normally, ProGuardCORE
    //               will write out the kotlinx.metadata "compatible" version, which for 0.3 is 1.5.1.
    //               ProGuardCORE 8.0.7 uses kotlinx.metadata 0.4.1 so fix the written version until 1.7 is released.
    public static final KotlinMetadataVersion KOTLIN_METADATA_VERSION = new KotlinMetadataVersion(1, 5, 1);


    @Override
    public void execute(AppView appView)
    {
        logger.info("Adapting Kotlin metadata...");

        ClassCounter counter = new ClassCounter();
        appView.programClassPool.classesAccept(
            new ReferencedKotlinMetadataVisitor(
            new KotlinMetadataWriter(
                (clazz, message) -> logger.warn(clazz.getName(), message),
                KOTLIN_METADATA_VERSION,
                counter
            )));

        logger.info("  Number of Kotlin classes adapted:              " + counter.getCount());
    }
}
