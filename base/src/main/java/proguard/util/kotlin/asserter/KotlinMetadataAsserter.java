/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
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

import proguard.AppView;
import proguard.classfile.*;
import proguard.classfile.kotlin.KotlinMetadata;
import proguard.classfile.kotlin.visitor.*;
import proguard.classfile.util.WarningPrinter;
import proguard.pass.Pass;
import proguard.resources.file.*;
import proguard.resources.file.visitor.*;
import proguard.resources.kotlinmodule.KotlinModule;
import proguard.resources.kotlinmodule.visitor.KotlinModuleVisitor;
import proguard.util.*;
import proguard.util.kotlin.asserter.constraint.*;

import java.io.PrintWriter;
import java.util.*;

/**
 * This pass performs a series of checks to see whether the kotlin metadata is intact.
 */
public class KotlinMetadataAsserter implements Pass
{
    // This is the list of constraints that will be checked using this asserter.
    private static final List<KotlinAsserterConstraint> DEFAULT_CONSTRAINTS = Arrays.asList(
        new FunctionIntegrity(),
        new ConstructorIntegrity(),
        new PropertyIntegrity(),
        new ClassIntegrity(),
        new TypeIntegrity(),
        new KmAnnotationIntegrity(),
        new ValueParameterIntegrity(),
        new SyntheticClassIntegrity(),
        new FileFacadeIntegrity(),
        new MultiFilePartIntegrity(),
        new DeclarationContainerIntegrity(),
        new KotlinModuleIntegrity()
    );

    @Override
    public void execute(AppView appView)
    {
        PrintWriter    err            = new PrintWriter(System.err, true);
        WarningPrinter warningPrinter = new WarningPrinter(err, appView.configuration.warn);

        Reporter reporter = new DefaultReporter(warningPrinter);
        MyKotlinMetadataAsserter kotlinMetadataAsserter = new MyKotlinMetadataAsserter(reporter, DEFAULT_CONSTRAINTS);

        reporter.setErrorMessage("Warning: Kotlin metadata errors encountered in %s. Not processing the metadata for this class.");
        appView.programClassPool.classesAccept(new ReferencedKotlinMetadataVisitor(kotlinMetadataAsserter));
        appView.libraryClassPool.classesAccept(new ReferencedKotlinMetadataVisitor(kotlinMetadataAsserter));

        reporter.setErrorMessage("Warning: Kotlin module errors encountered in module %s. Not processing the metadata for this module.");
        appView.resourceFilePool.resourceFilesAccept(
            new ResourceFileProcessingFlagFilter(0, ProcessingFlags.DONT_PROCESS_KOTLIN_MODULE, kotlinMetadataAsserter));
    }

    /**
     * This class performs a series of checks to see whether the kotlin metadata is intact
     */
    public static class MyKotlinMetadataAsserter
        implements KotlinMetadataVisitor,
                   ResourceFileVisitor,
                   KotlinModuleVisitor
    {
        private final List<? extends KotlinAsserterConstraint> constraints;
        private final Reporter                                 reporter;

        MyKotlinMetadataAsserter(Reporter reporter, List<KotlinAsserterConstraint> constraints)
        {
            this.constraints = constraints;
            this.reporter    = reporter;
        }

        @Override
        public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata)
        {
            reporter.resetCounter(clazz.getName());

            constraints.forEach(constraint -> constraint.check(reporter, clazz, kotlinMetadata));

            if (reporter.getCount() > 0)
            {
                clazz.accept(new KotlinMetadataRemover());
            }
        }

        @Override
        public void visitKotlinModule(KotlinModule kotlinModule)
        {
            reporter.resetCounter(kotlinModule.name);

            constraints.forEach(constraint -> constraint.check(reporter, kotlinModule));

            if (reporter.getCount() > 0)
            {
                kotlinModule.accept(new ProcessingFlagSetter(ProcessingFlags.DONT_PROCESS_KOTLIN_MODULE));
            }
        }

        @Override
        public void visitResourceFile(ResourceFile resourceFile) {}
    }
}
