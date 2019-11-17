/*
 * ProGuard Core -- library to process Java bytecode.
 *
 * Copyright (c) 2002-2019 Guardsquare NV
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package proguard.classfile.kotlin.asserter;

import proguard.classfile.*;
import proguard.classfile.kotlin.KotlinMetadata;
import proguard.classfile.kotlin.asserter.constraint.*;
import proguard.classfile.kotlin.visitors.*;
import proguard.classfile.util.WarningPrinter;

import java.util.*;

/**
 * This class performs a series of checks to see whether the kotlin metadata is intact
 */
public class KotlinMetadataAsserter
implements KotlinMetadataVisitor
{
    // This is the list of constraints that will be checked using this asserter.
    public static final List<KotlinMetadataConstraint> DEFAULT_CONSTRAINTS = Arrays.asList(
        FunctionIntegrity      .constraint(),
        ConstructorIntegrity   .constraint(),
        PropertyIntegrity      .constraint(),
        ClassIntegrity         .constraint(),
        TypeIntegrity          .constraint(),
        KmAnnotationIntegrity  .constraint(),
        ValueParameterIntegrity.constraint(),
        SyntheticClassIntegrity.constraint(),
        DeclarationContainerIntegrity.constraint()
    );

    private final Iterable<? extends KotlinMetadataConstraint> constraints;
    private final Reporter                                     reporter;

    private final ClassPool programClassPool;
    private final ClassPool libraryClassPool;
    private final WarningPrinter warningPrinter;

    public KotlinMetadataAsserter(ClassPool      programClassPool,
                                  ClassPool      libraryClassPool,
                                  WarningPrinter warningPrinter)
    {
        this(DEFAULT_CONSTRAINTS,
             new Reporter()
             {
                 private int count = 0;

                 public void report(KotlinMetadataError error)
                 {
                     count++;
                     warningPrinter.print(error.clazz.getName(), "Warning: " + error.toString());
                 }


                 public void resetCounter()
                 {
                     count = 0;
                 }


                 public int getCount()
                 {
                     return count;
                 }
             },
             programClassPool,
             libraryClassPool,
             warningPrinter);
    }


    public KotlinMetadataAsserter(Iterable<? extends KotlinMetadataConstraint> constraints,
                                  Reporter                                     reporter,
                                  ClassPool                                    programClassPool,
                                  ClassPool                                    libraryClassPool,
                                  WarningPrinter                               warningPrinter)
    {
        this.constraints      = constraints;
        this.reporter         = reporter;
        this.programClassPool = programClassPool;
        this.libraryClassPool = libraryClassPool;
        this.warningPrinter   = warningPrinter;
    }


    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata)
    {
        reporter.resetCounter();

        for (KotlinMetadataConstraint kotlinMetadataConstraint : constraints)
        {
            kotlinMetadataConstraint.check(reporter,
                                           clazz,
                                           kotlinMetadata,
                                           programClassPool,
                                           libraryClassPool);
        }

        if (reporter.getCount() > 0)
        {
            warningPrinter.print(clazz.getName(), "  Not processing the metadata for class " + clazz.getName());
            clazz.accept(new KotlinMetadataRemover());
        }
    }
}
