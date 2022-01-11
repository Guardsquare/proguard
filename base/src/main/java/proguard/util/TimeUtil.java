/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2022 Guardsquare NV
 */

package proguard.util;

public class TimeUtil
{
    public static String millisecondsToMinSecReadable(int timeMs)
    {
        int ms = timeMs % 1000;
        int s  = ((timeMs - ms) / 1000) % 60;
        int m  = timeMs / 60000;
        return String.format("%d:%d.%d (m:s.ms)", m, s, ms);
    }
}
