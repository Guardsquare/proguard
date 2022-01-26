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
package proguard.backport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.*;
import proguard.classfile.*;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.constant.Constant;
import proguard.classfile.editor.*;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.visitor.InstructionCounter;
import proguard.classfile.util.*;
import proguard.classfile.visitor.*;
import proguard.pass.Pass;

import java.io.IOException;

/**
 * This pass backports classes to the specified targetClassVersion.
 *
 * @author Thomas Neidhart
 */
public class Backporter implements Pass
{
    private static final Logger logger = LogManager.getLogger(Backporter.class);
    private final Configuration configuration;

    public Backporter(Configuration configuration)
    {
        this.configuration = configuration;
    }


    @Override
    public void execute(AppView appView) throws IOException
    {
        int targetClassVersion = configuration.targetClassVersion;

        logger.info("Backporting class files...");

        // Clean up any previous processing info.
        appView.programClassPool.classesAccept(new ClassCleaner());
        appView.libraryClassPool.classesAccept(new ClassCleaner());

        final InstructionCounter replacedStringConcatCounter      = new InstructionCounter();
        final ClassCounter       lambdaExpressionCounter          = new ClassCounter();
        final MemberCounter      staticInterfaceMethodCounter     = new MemberCounter();
        final MemberCounter      defaultInterfaceMethodCounter    = new MemberCounter();
        final InstructionCounter replacedMethodCallCounter        = new InstructionCounter();
        final InstructionCounter replacedStreamsMethodCallCounter = new InstructionCounter();
        final InstructionCounter replacedTimeMethodCallCounter    = new InstructionCounter();

        if (targetClassVersion < VersionConstants.CLASS_VERSION_1_9)
        {
            // Convert indy string concatenations to StringBuilder chains
            CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor(true, true);
            appView.programClassPool.classesAccept(
                new ClassVersionFilter(VersionConstants.CLASS_VERSION_1_9,
                new AllAttributeVisitor(
                new AttributeNameFilter(Attribute.BOOTSTRAP_METHODS,
                new AttributeToClassVisitor(
                new MultiClassVisitor(
                    new AllMethodVisitor(
                        new AllAttributeVisitor(
                        new PeepholeEditor(codeAttributeEditor,
                                           // Replace the indy instructions related to String concatenation.
                                           new StringConcatenationConverter(replacedStringConcatCounter,
                                                         codeAttributeEditor)))
                    ),

                    // Clean up unused bootstrap methods and their dangling constants.
                    new BootstrapMethodsAttributeShrinker(),

                    // Initialize new references to StringBuilder.
                    new ClassReferenceInitializer(appView.programClassPool, appView.libraryClassPool)
                ))))));
        }

        if (targetClassVersion < VersionConstants.CLASS_VERSION_1_8)
        {
            // Collect all classes with BootstrapMethod attributes,
            // and convert lambda expressions and method references.
            ClassPool filteredClasses = new ClassPool();
            appView.programClassPool.classesAccept(
                new ClassVersionFilter(VersionConstants.CLASS_VERSION_1_8,
                new AllAttributeVisitor(
                new AttributeNameFilter(Attribute.BOOTSTRAP_METHODS,
                new AttributeToClassVisitor(
                new ClassPoolFiller(filteredClasses))))));

            // Note: we visit the filtered classes in a separate step
            // because we modify the programClassPool while converting
            filteredClasses.classesAccept(
                new MultiClassVisitor(
                    // Replace the indy instructions related to lambda expressions.
                    new LambdaExpressionConverter(appView.programClassPool,
                                                  appView.libraryClassPool,
                                                  appView.extraDataEntryNameMap,
                                                  lambdaExpressionCounter),

                    // Clean up unused bootstrap methods and their dangling constants.
                    new BootstrapMethodsAttributeShrinker(),

                    // Re-initialize references.
                    new ClassReferenceInitializer(appView.programClassPool, appView.libraryClassPool)
                ));

            // Remove static and default methods from interfaces if the
            // target version is < 1.8. The dalvik format 037 has native
            // support for default methods. The dalvik format specification
            // does not explicitly mention static interface methods, although
            // they seem to work correctly.
            ClassPool interfaceClasses = new ClassPool();
            appView.programClassPool.classesAccept(
                new ClassVersionFilter(VersionConstants.CLASS_VERSION_1_8,
                new ClassAccessFilter(AccessConstants.INTERFACE, 0,
                new ClassPoolFiller(interfaceClasses))));

            ClassPool modifiedClasses = new ClassPool();
            ClassVisitor modifiedClassCollector =
                new ClassPoolFiller(modifiedClasses);

            interfaceClasses.classesAccept(
                new MultiClassVisitor(
                    new StaticInterfaceMethodConverter(appView.programClassPool,
                                                       appView.libraryClassPool,
                                                       appView.extraDataEntryNameMap,
                                                       modifiedClassCollector,
                                                       staticInterfaceMethodCounter),

                    new DefaultInterfaceMethodConverter(modifiedClassCollector,
                                                        defaultInterfaceMethodCounter)
                ));

            // Re-Initialize references in modified classes.
            modifiedClasses.classesAccept(
                new ClassReferenceInitializer(appView.programClassPool,
                                              appView.libraryClassPool));
        }

        if (targetClassVersion < VersionConstants.CLASS_VERSION_1_7)
        {
            // Replace / remove method calls only available in Java 7+.
            InstructionSequenceBuilder ____ =
                new InstructionSequenceBuilder(appView.programClassPool,
                                               appView.libraryClassPool);

            Instruction[][][] instructions = new Instruction[][][]
            {
                // Replace Objects.requireNonNull(...) with Object.getClass().

                // Starting in JDK 9, javac uses {@code requireNonNull} for
                // synthetic null-checks
                // (see <a href="http://bugs.openjdk.java.net/browse/JDK-8074306">
                // JDK-8074306</a>).
                {
                    ____.invokestatic("java/util/Objects",
                                      "requireNonNull",
                                      "(Ljava/lang/Object;)Ljava/lang/Object;").__(),

                    ____.dup()
                        .invokevirtual(ClassConstants.NAME_JAVA_LANG_OBJECT,
                                       ClassConstants.METHOD_NAME_OBJECT_GET_CLASS,
                                       ClassConstants.METHOD_TYPE_OBJECT_GET_CLASS)
                        .pop().__()
                },

                // Remove Throwable.addSuppressed(...).
                {
                    ____.invokevirtual("java/util/Throwable",
                                       "addSuppressed",
                                       "(Ljava/lang/Throwable;)V").__(),

                    ____.pop()      // the suppressed exception
                        .pop().__() // the original exception
                }
            };

            Constant[] constants = ____.constants();

            CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();

            appView.programClassPool.classesAccept(
                new AllMethodVisitor(
                new AllAttributeVisitor(
                new PeepholeEditor(null, codeAttributeEditor,
                                   new InstructionSequencesReplacer(constants,
                                                 instructions,
                                                 null,
                                                 codeAttributeEditor,
                                                 replacedMethodCallCounter)))));
        }

        if (targetClassVersion < VersionConstants.CLASS_VERSION_1_8)
        {
            // Sanity check: if the streamsupport library is not found in the
            // classpools do not try to backport.
            ClassCounter streamSupportClasses = new ClassCounter();

            ClassVisitor streamSupportVisitor =
                new ClassNameFilter("java8/**",
                streamSupportClasses);

            appView.programClassPool.classesAccept(streamSupportVisitor);
            appView.libraryClassPool.classesAccept(streamSupportVisitor);

            if (streamSupportClasses.getCount() > 0)
            {
                WarningPrinter streamSupportWarningPrinter =
                    new WarningLogger(logger, configuration.warn);

                ClassPool modifiedClasses = new ClassPool();
                ClassVisitor modifiedClassCollector =
                    new ClassPoolFiller(modifiedClasses);

                appView.programClassPool.classesAccept(
                    // Do not process classes of the stream support library itself.
                    new ClassNameFilter("!java8/**",
                    new StreamSupportConverter(appView.programClassPool,
                                               appView.libraryClassPool,
                                               streamSupportWarningPrinter,
                                               modifiedClassCollector,
                                               replacedStreamsMethodCallCounter)));

                // Re-Initialize references in modified classes.
                modifiedClasses.classesAccept(
                    new ClassReferenceInitializer(appView.programClassPool,
                                                  appView.libraryClassPool));

                int conversionWarningCount = streamSupportWarningPrinter.getWarningCount();
                if (conversionWarningCount > 0)
                {
                    logger.warn("Warning: there were {} Java 8 stream API method calls that could not be backported.",
                                 conversionWarningCount);
                    logger.warn("      You should check if a your project setup is correct (compileSdkVersion, streamsupport dependency).");
                    logger.warn("      For more information, consult the section \'Integration->Gradle Plugin->Java 8 stream API support\' in our manual");
                }
            }
        }

        if (targetClassVersion < VersionConstants.CLASS_VERSION_1_8)
        {
            // Sanity check: if the threetenbp library is not found in the
            // classpools do not try to backport.
            ClassCounter threetenClasses = new ClassCounter();

            ClassVisitor threetenClassVisitor =
                new ClassNameFilter("org/threeten/bp/**",
                threetenClasses);

            appView.programClassPool.classesAccept(threetenClassVisitor);
            appView.libraryClassPool.classesAccept(threetenClassVisitor);

            if (threetenClasses.getCount() > 0)
            {
                WarningPrinter threetenWarningPrinter =
                    new WarningLogger(logger, configuration.warn);

                ClassPool modifiedClasses = new ClassPool();
                ClassVisitor modifiedClassCollector =
                    new ClassPoolFiller(modifiedClasses);

                appView.programClassPool.classesAccept(
                    // Do not process classes of the threeten library itself.
                    new ClassNameFilter("!org/threeten/bp/**",
                                        new JSR310Converter(appView.programClassPool,
                                                            appView.libraryClassPool,
                                                            threetenWarningPrinter,
                                                            modifiedClassCollector,
                                                            replacedTimeMethodCallCounter)));

                // Re-Initialize references in modified classes.
                modifiedClasses.classesAccept(
                    new ClassReferenceInitializer(appView.programClassPool,
                                                  appView.libraryClassPool));

                int conversionWarningCount = threetenWarningPrinter.getWarningCount();
                if (conversionWarningCount > 0)
                {
                    logger.warn("Warning: there were {} Java 8 time API method calls that could not be backported.",
                                 conversionWarningCount);
                    logger.warn("      You should check if a your project setup is correct (compileSdkVersion, threetenbp dependency).");
                    logger.warn("      For more information, consult the section \'Integration->Gradle Plugin->Java 8 time API support\' in our manual");
                }
            }
        }

        if (targetClassVersion != 0)
        {
            // Set the class version of all classes in the program ClassPool
            // to the specified target version. This is needed to perform
            // optimization on the backported + generated classes.
            appView.programClassPool.classesAccept(new ClassVersionSetter(targetClassVersion));
        }

        logger.info("  Number of converted string concatenations:     {}", replacedStringConcatCounter.getCount());
        logger.info("  Number of converted lambda expressions:        {}", lambdaExpressionCounter.getCount());
        logger.info("  Number of converted static interface methods:  {}", staticInterfaceMethodCounter.getCount());
        logger.info("  Number of converted default interface methods: {}", defaultInterfaceMethodCounter.getCount());
        logger.info("  Number of replaced Java 7+ method calls:       {}", replacedMethodCallCounter.getCount());
        logger.info("  Number of replaced Java 8 stream method calls: {}", replacedStreamsMethodCallCounter.getCount());
        logger.info("  Number of replaced Java 8 time method calls:   {}", replacedTimeMethodCallCounter.getCount());
    }
}
