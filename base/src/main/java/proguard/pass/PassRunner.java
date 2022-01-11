/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2022 Guardsquare NV
 */

package proguard.pass;

import proguard.*;
import proguard.util.Benchmark;
import proguard.util.TimeUtil;

public class PassRunner
{
    private static final boolean   DEBUG     = System.getProperty("proguard.debug.timing") != null;
    private        final Benchmark benchmark = new Benchmark();

    public void run(Pass pass, AppView appView) throws Exception
    {
        benchmark.start();
        pass.execute(appView);
        benchmark.stop();

        if (DEBUG)
        {
            int timeMs          = benchmark.getElapsedTimeMs();
            String readableTime = TimeUtil.millisecondsToMinSecReadable(timeMs);
            System.out.println("Pass " + pass.getName() + " completed in " + readableTime);
        }
    }
}
