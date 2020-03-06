/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 */
package proguard.util.kotlin.asserter;

/**
 * Interface for reporting errors.
 */
public interface Reporter
{
    void setErrorMessage(String message);

    void report(String error);

    void resetCounter(String contextName);

    int getCount();

    void print(String className, String s);
}
