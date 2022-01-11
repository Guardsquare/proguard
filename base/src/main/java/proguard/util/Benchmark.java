/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2022 Guardsquare NV
 */

package proguard.util;

public class Benchmark
{
    private long startTime;
    private int  elapsedTimeMs;

    public void start()
    {
        startTime = System.currentTimeMillis();
    }

    public void stop()
    {
        elapsedTimeMs = (int) (System.currentTimeMillis() - startTime);
    }

    /**
     * Return the elapsed time in milliseconds.
     */
    public int getElapsedTimeMs()
    {
        return elapsedTimeMs;
    }
}
