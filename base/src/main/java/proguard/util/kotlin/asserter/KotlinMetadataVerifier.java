/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2022 Guardsquare NV
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


package proguard.util.kotlin.asserter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.AppView;
import proguard.Configuration;
import proguard.classfile.util.WarningLogger;
import proguard.pass.Pass;
import proguard.resources.file.ResourceFilePool;

/**
 * This pass performs a series of checks to see whether the kotlin metadata is intact.
 */
public class KotlinMetadataVerifier implements Pass
{
    private static final Logger logger = LogManager.getLogger(KotlinMetadataVerifier.class);

    private final Configuration configuration;

    public KotlinMetadataVerifier(Configuration configuration)
    {
        this.configuration = configuration;
    }


    @Override
    public void execute(AppView appView)
    {
        WarningLogger warningLogger = new WarningLogger(logger, configuration.warn);

        KotlinMetadataAsserter kotlinMetadataAsserter = new KotlinMetadataAsserter();

        kotlinMetadataAsserter.execute(
            warningLogger,
            appView.programClassPool,
            appView.libraryClassPool,
            appView.resourceFilePool
        );

        int warningCount = warningLogger.getWarningCount();
        if (warningCount > 0)
        {
            logger.warn("Warning: there were {} errors during Kotlin metadata verification.", warningCount);
        }
    }
}
