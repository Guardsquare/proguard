/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
 */

package proguard;

import proguard.classfile.io.kotlin.KotlinMetadataWriter;
import proguard.classfile.kotlin.visitor.ReferencedKotlinMetadataVisitor;
import proguard.classfile.util.WarningPrinter;
import proguard.classfile.visitor.ClassCounter;
import proguard.pass.Pass;

import java.io.PrintWriter;

public class KotlinMetadataAdapter
implements Pass
{
    @Override
    public void execute(AppView appView)
    {
        if (appView.configuration.verbose)
        {
            System.out.println("Adapting Kotlin metadata...");
        }

        WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(System.out, true));

        ClassCounter counter = new ClassCounter();
        appView.programClassPool.classesAccept(
            new ReferencedKotlinMetadataVisitor(
                new KotlinMetadataWriter(warningPrinter, counter)));

        if (appView.configuration.verbose)
        {
            System.out.println("  Number of Kotlin classes adapted:              " + counter.getCount());
        }
    }
}
