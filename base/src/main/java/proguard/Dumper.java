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
    private final Configuration configuration;

    public Dumper(Configuration configuration)
    {
        this.configuration = configuration;
    }


    @Override
    public void execute(AppView appView) throws Exception
    {
        if (configuration.verbose)
        {
            System.out.println("Printing classes to [" + PrintWriterUtil.fileName(configuration.dump) + "]...");
        }

        PrintWriter pw = PrintWriterUtil.createPrintWriterOut(configuration.dump);
        try
        {
            appView.programClassPool.classesAccept(new ClassPrinter(pw));
        }
        finally
        {
            PrintWriterUtil.closePrintWriter(configuration.dump, pw);
        }
    }
}
