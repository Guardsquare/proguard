/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 */

package proguard.util.kotlin.asserter;

import proguard.classfile.util.WarningPrinter;

/**
 * @author James Hamilton
 */
class DefaultReporter implements Reporter
{
    private final WarningPrinter warningPrinter;
    private       int            count;
    private       String         errorMessage = "";
    private       String         contextName  = "";


    DefaultReporter(WarningPrinter warningPrinter)
    {
        this.warningPrinter = warningPrinter;
        count = 0;
    }


    @Override
    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }


    @Override
    public void report(String error)
    {
        if (count == 0)
        {
            warningPrinter.print(this.contextName, String.format(this.errorMessage, this.contextName));
        }
        count++;
        warningPrinter.print(this.contextName, "  " + error);
    }


    @Override
    public void resetCounter(String contextName)
    {
        this.contextName = contextName;
        this.count       = 0;
    }


    @Override
    public int getCount()
    {
        return count;
    }


    @Override
    public void print(String className, String s)
    {
        this.warningPrinter.print(className, s);
    }
}
