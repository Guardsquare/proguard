/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
 */

package proguard;

import proguard.classfile.visitor.*;
import proguard.pass.Pass;
import proguard.util.PrintWriterUtil;

import java.io.PrintWriter;

/**
 * This pass prints the contents of the program class pool.
 *
 * @author Tim Van Den Broecke
 */
public class Dumper implements Pass
{
    @Override
    public void execute(AppView appView) throws Exception
    {
        if (appView.configuration.verbose)
        {
            System.out.println("Printing classes to [" + PrintWriterUtil.fileName(appView.configuration.dump) + "]...");
        }

        PrintWriter pw = PrintWriterUtil.createPrintWriterOut(appView.configuration.dump);
        try
        {
            appView.programClassPool.classesAccept(new ClassPrinter(pw));
        }
        finally
        {
            PrintWriterUtil.closePrintWriter(appView.configuration.dump, pw);
        }
    }
}
