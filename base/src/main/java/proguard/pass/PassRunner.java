/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2022 Guardsquare NV
 */

package proguard.pass;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.*;
import proguard.util.Benchmark;
import proguard.util.TimeUtil;

public class PassRunner
{
    private static final Logger    logger    = LogManager.getLogger(PassRunner.class);
    private        final Benchmark benchmark = new Benchmark();

    public void run(Pass pass, AppView appView) throws Exception
    {
        benchmark.start();
        pass.execute(appView);
        benchmark.stop();

        logger.debug("Pass {} completed in {}", pass::getName, () -> TimeUtil.millisecondsToMinSecReadable(benchmark.getElapsedTimeMs()));
    }
}
