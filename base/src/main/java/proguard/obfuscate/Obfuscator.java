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
package proguard.obfuscate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.*;
import proguard.classfile.*;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.visitor.*;
import proguard.classfile.editor.*;
import proguard.classfile.kotlin.visitor.*;
import proguard.classfile.kotlin.visitor.filter.*;
import proguard.classfile.util.*;
import proguard.classfile.visitor.*;
import proguard.fixer.kotlin.KotlinAnnotationFlagFixer;
import proguard.obfuscate.kotlin.*;
import proguard.obfuscate.util.InstructionSequenceObfuscator;
import proguard.pass.Pass;
import proguard.resources.file.visitor.ResourceFileProcessingFlagFilter;
import proguard.util.*;

import java.io.*;
import java.util.*;

/**
 * This pass can perform obfuscation of class pools according to a given
 * specification.
 *
 * @author Eric Lafortune
 */
public class Obfuscator implements Pass
{
    private static final Logger logger = LogManager.getLogger(Obfuscator.class);
    private final Configuration configuration;

    public Obfuscator(Configuration configuration)
    {
        this.configuration = configuration;
    }


    /**
     * Performs obfuscation of the given program class pool.
     */
    @Override
    public void execute(AppView appView) throws IOException
    {
        logger.info("Obfuscating...");

        // We're using the system's default character encoding for writing to
        // the standard output and error output.
        PrintWriter out = new PrintWriter(System.out, true);

        // Link all non-private, non-static methods in all class hierarchies.
        ClassVisitor memberInfoLinker =
            new BottomClassFilter(new MethodLinker());

        appView.programClassPool.classesAccept(memberInfoLinker);
        appView.libraryClassPool.classesAccept(memberInfoLinker);

        // If the class member names have to correspond globally,
        // additionally link all class members in all program classes.
        if (configuration.useUniqueClassMemberNames)
        {
            appView.programClassPool.classesAccept(new AllMemberVisitor(
                                                   new MethodLinker()));
        }

        // Create a visitor for marking the seeds.
        NameMarker nameMarker = new NameMarker();

        // All library classes and library class members keep their names.
        appView.libraryClassPool.classesAccept(nameMarker);
        appView.libraryClassPool.classesAccept(new AllMemberVisitor(nameMarker));

        // Mark classes that have the DONT_OBFUSCATE flag set.
        appView.programClassPool.classesAccept(
            new MultiClassVisitor(
                new ClassProcessingFlagFilter(ProcessingFlags.DONT_OBFUSCATE, 0,
                nameMarker),
                new AllMemberVisitor(
                new MemberProcessingFlagFilter(ProcessingFlags.DONT_OBFUSCATE, 0,
                nameMarker))));

        // We also keep the names of the abstract methods of functional
        // interfaces referenced from bootstrap method arguments (additional
        // interfaces with LambdaMetafactory.altMetafactory).
        // The functional method names have to match the names in the
        // dynamic method invocations with LambdaMetafactory.
        appView.programClassPool.classesAccept(
            new ClassVersionFilter(VersionConstants.CLASS_VERSION_1_7,
            new AllAttributeVisitor(
            new AttributeNameFilter(Attribute.BOOTSTRAP_METHODS,
            new AllBootstrapMethodInfoVisitor(
            new AllBootstrapMethodArgumentVisitor(
            new ConstantTagFilter(Constant.CLASS,
            new ReferencedClassVisitor(
            new FunctionalInterfaceFilter(
            new ClassHierarchyTraveler(true, false, true, false,
            new AllMethodVisitor(
            new MemberAccessFilter(AccessConstants.ABSTRACT, 0,
            nameMarker))))))))))));

        // We also keep the names of the abstract methods of functional
        // interfaces that are returned by dynamic method invocations.
        // The functional method names have to match the names in the
        // dynamic method invocations with LambdaMetafactory.
        appView.programClassPool.classesAccept(
            new ClassVersionFilter(VersionConstants.CLASS_VERSION_1_7,
            new AllConstantVisitor(
            new DynamicReturnedClassVisitor(
            new FunctionalInterfaceFilter(
            new ClassHierarchyTraveler(true, false, true, false,
            new AllMethodVisitor(
            new MemberAccessFilter(AccessConstants.ABSTRACT, 0,
            nameMarker))))))));

        if (configuration.keepKotlinMetadata)
        {
            appView.programClassPool.classesAccept(
                // Keep Kotlin default implementations class where the user had already kept the interface.
                new ClassProcessingFlagFilter(ProcessingFlags.DONT_OBFUSCATE, 0,
                new ReferencedKotlinMetadataVisitor(new KotlinClassToDefaultImplsClassVisitor(nameMarker))));
        }

        // Mark attributes that have to be kept.
        AttributeVisitor attributeUsageMarker =
            new NonEmptyAttributeFilter(
            new AttributeUsageMarker());

        AttributeVisitor optionalAttributeUsageMarker =
            configuration.keepAttributes == null ? null :
                new AttributeNameFilter(configuration.keepAttributes,
                                        attributeUsageMarker);

        appView.programClassPool.classesAccept(
            new AllAttributeVisitor(true,
            new RequiredAttributeFilter(attributeUsageMarker,
                                        optionalAttributeUsageMarker)));

        // Keep parameter names and types if specified.
        if (configuration.keepParameterNames)
        {
            appView.programClassPool.classesAccept(
                new AllMethodVisitor(
                new NewMemberNameFilter(
                new AllAttributeVisitor(true,
                new ParameterNameMarker(attributeUsageMarker)))));

            if (configuration.keepKotlinMetadata)
            {
                appView.programClassPool.classesAccept(
                    new ReferencedKotlinMetadataVisitor(
                    new KotlinValueParameterUsageMarker()));
            }
        }

        if (configuration.keepKotlinMetadata)
        {
            appView.programClassPool.classesAccept(
                new ReferencedKotlinMetadataVisitor(
                new KotlinValueParameterNameShrinker()));

            // Keep SourceDebugExtension annotations on Kotlin synthetic classes but obfuscate them.
            appView.programClassPool.classesAccept(
                new ReferencedKotlinMetadataVisitor(
                new KotlinSyntheticClassKindFilter(
                    KotlinSyntheticClassKindFilter::isLambda,
                new KotlinMetadataToClazzVisitor(
                new AllAttributeVisitor(
                new AttributeNameFilter(Attribute.SOURCE_DEBUG_EXTENSION,
                                        new MultiAttributeVisitor(attributeUsageMarker,
                                        new KotlinSourceDebugExtensionAttributeObfuscator())))))));
        }

        // Remove the attributes that can be discarded. Note that the attributes
        // may only be discarded after the seeds have been marked, since the
        // configuration may rely on annotations.
        appView.programClassPool.classesAccept(new AttributeShrinker());

        if (configuration.keepKotlinMetadata)
        {
            appView.programClassPool.classesAccept(
                new ReferencedKotlinMetadataVisitor(
                new KotlinAnnotationFlagFixer()));
        }

        // Apply the mapping, if one has been specified. The mapping can
        // override the names of library classes and of library class members.
        if (configuration.applyMapping != null)
        {
            logger.info("Applying mapping from [{}]...", PrintWriterUtil.fileName(configuration.applyMapping));

            WarningPrinter warningPrinter = new WarningLogger(logger, configuration.warn);

            MappingReader reader = new MappingReader(configuration.applyMapping);

            MappingProcessor keeper =
                new MultiMappingProcessor(new MappingProcessor[]
                {
                    new MappingKeeper(appView.programClassPool, warningPrinter),
                    new MappingKeeper(appView.libraryClassPool, null),
                });

            reader.pump(keeper);

            // Print out a summary of the warnings if necessary.
            int warningCount = warningPrinter.getWarningCount();
            if (warningCount > 0)
            {
                logger.warn("Warning: there were {} kept classes and class members that were remapped anyway.",
                             warningCount);
                logger.warn("         You should adapt your configuration or edit the mapping file.");

                if (!configuration.ignoreWarnings)
                {
                    logger.warn("         If you are sure this remapping won't hurt,");
                    logger.warn("         you could try your luck using the '-ignorewarnings' option.");
                }

                logger.warn("         (https://www.guardsquare.com/proguard/manual/troubleshooting#mappingconflict1)");

                if (!configuration.ignoreWarnings)
                {
                    throw new IOException("Please correct the above warnings first.");
                }
            }
        }

        // Come up with new names for all classes.
        DictionaryNameFactory classNameFactory = configuration.classObfuscationDictionary != null ?
            new DictionaryNameFactory(configuration.classObfuscationDictionary, null) :
            null;

        DictionaryNameFactory packageNameFactory = configuration.packageObfuscationDictionary != null ?
            new DictionaryNameFactory(configuration.packageObfuscationDictionary, null) :
            null;

        appView.programClassPool.classesAccept(
            new ClassObfuscator(appView.programClassPool,
                                appView.libraryClassPool,
                                classNameFactory,
                                packageNameFactory,
                                configuration.useMixedCaseClassNames,
                                configuration.keepPackageNames,
                                configuration.flattenPackageHierarchy,
                                configuration.repackageClasses,
                                configuration.allowAccessModification,
                                configuration.keepKotlinMetadata));

        // Come up with new names for all class members.
        NameFactory nameFactory = new SimpleNameFactory();
        if (configuration.obfuscationDictionary != null)
        {
            nameFactory =
                new DictionaryNameFactory(configuration.obfuscationDictionary,
                                          nameFactory);
        }

        WarningPrinter warningPrinter = new WarningLogger(logger, configuration.warn);

        // Maintain a map of names to avoid [descriptor - new name - old name].
        Map descriptorMap = new HashMap();

        // Do the class member names have to be globally unique?
        if (configuration.useUniqueClassMemberNames)
        {
            // Collect all member names in all classes.
            appView.programClassPool.classesAccept(
                new AllMemberVisitor(
                new MemberNameCollector(configuration.overloadAggressively,
                                        descriptorMap)));

            // Assign new names to all members in all classes.
            appView.programClassPool.classesAccept(
                new AllMemberVisitor(
                new MemberObfuscator(configuration.overloadAggressively,
                                     nameFactory,
                                     descriptorMap)));
        }
        else
        {
            // Come up with new names for all non-private class members.
            appView.programClassPool.classesAccept(
                new MultiClassVisitor(
                    // Collect all private member names in this class and down
                    // the hierarchy.
                    new ClassHierarchyTraveler(true, false, false, true,
                    new AllMemberVisitor(
                    new MemberAccessFilter(AccessConstants.PRIVATE, 0,
                    new MemberNameCollector(configuration.overloadAggressively,
                                            descriptorMap)))),

                    // Collect all non-private member names anywhere in the
                    // hierarchy.
                    new ClassHierarchyTraveler(true, true, true, true,
                    new AllMemberVisitor(
                    new MemberAccessFilter(0, AccessConstants.PRIVATE,
                    new MemberNameCollector(configuration.overloadAggressively,
                                            descriptorMap)))),

                    // Assign new names to all non-private members in this class.
                    new AllMemberVisitor(
                    new MemberAccessFilter(0, AccessConstants.PRIVATE,
                    new MemberObfuscator(configuration.overloadAggressively,
                                         nameFactory,
                                         descriptorMap))),

                    // Clear the collected names.
                    new MapCleaner(descriptorMap)
                ));

            // Come up with new names for all private class members.
            appView.programClassPool.classesAccept(
                new MultiClassVisitor(
                    // Collect all member names in this class.
                    new AllMemberVisitor(
                    new MemberNameCollector(configuration.overloadAggressively,
                                            descriptorMap)),

                    // Collect all non-private member names higher up the hierarchy.
                    new ClassHierarchyTraveler(false, true, true, false,
                    new AllMemberVisitor(
                    new MemberAccessFilter(0, AccessConstants.PRIVATE,
                    new MemberNameCollector(configuration.overloadAggressively,
                                            descriptorMap)))),

                    // Collect all member names from interfaces of abstract
                    // classes down the hierarchy.
                    // Due to an error in the JLS/JVMS, virtual invocations
                    // may end up at a private method otherwise (Sun/Oracle
                    // bugs #6691741 and #6684387, ProGuard bug #3471941,
                    // and ProGuard test #1180).
                    new ClassHierarchyTraveler(false, false, false, true,
                    new ClassAccessFilter(AccessConstants.ABSTRACT, 0,
                    new ClassHierarchyTraveler(false, false, true, false,
                    new AllMemberVisitor(
                    new MemberNameCollector(configuration.overloadAggressively,
                                            descriptorMap))))),

                    // Collect all default method names from interfaces of
                    // any classes down the hierarchy.
                    // This is an extended version of the above problem
                    // (Sun/Oracle bug #802464, ProGuard bug #662, and
                    // ProGuard test #2060).
                    new ClassHierarchyTraveler(false, false, false, true,
                    new ClassHierarchyTraveler(false, false, true, false,
                    new AllMethodVisitor(
                    new MemberAccessFilter(0, AccessConstants.ABSTRACT | AccessConstants.STATIC,
                    new MemberNameCollector(configuration.overloadAggressively,
                                            descriptorMap))))),

                    // Assign new names to all private members in this class.
                    new AllMemberVisitor(
                    new MemberAccessFilter(AccessConstants.PRIVATE, 0,
                    new MemberObfuscator(configuration.overloadAggressively,
                                         nameFactory,
                                         descriptorMap))),

                    // Clear the collected names.
                    new MapCleaner(descriptorMap)
                ));
        }

        // Some class members may have ended up with conflicting names.
        // Come up with new, globally unique names for them.
        NameFactory specialNameFactory =
            new SpecialNameFactory(new SimpleNameFactory());

        // Collect a map of special names to avoid
        // [descriptor - new name - old name].
        Map specialDescriptorMap = new HashMap();

        appView.programClassPool.classesAccept(
            new AllMemberVisitor(
            new MemberSpecialNameFilter(
            new MemberNameCollector(configuration.overloadAggressively,
                                    specialDescriptorMap))));

        appView.libraryClassPool.classesAccept(
            new AllMemberVisitor(
            new MemberSpecialNameFilter(
            new MemberNameCollector(configuration.overloadAggressively,
                                    specialDescriptorMap))));

        // Replace conflicting non-private member names with special names.
        appView.programClassPool.classesAccept(
            new MultiClassVisitor(
                // Collect all private member names in this class and down
                // the hierarchy.
                new ClassHierarchyTraveler(true, false, false, true,
                new AllMemberVisitor(
                new MemberAccessFilter(AccessConstants.PRIVATE, 0,
                new MemberNameCollector(configuration.overloadAggressively,
                                        descriptorMap)))),

                // Collect all non-private member names in this class and
                // higher up the hierarchy.
                new ClassHierarchyTraveler(true, true, true, false,
                new AllMemberVisitor(
                new MemberAccessFilter(0, AccessConstants.PRIVATE,
                new MemberNameCollector(configuration.overloadAggressively,
                                        descriptorMap)))),

                // Assign new names to all conflicting non-private members
                // in this class and higher up the hierarchy.
                new ClassHierarchyTraveler(true, true, true, false,
                new AllMemberVisitor(
                new MemberAccessFilter(0, AccessConstants.PRIVATE,
                new MemberNameConflictFixer(configuration.overloadAggressively,
                                            descriptorMap,
                                            warningPrinter,
                new MemberObfuscator(configuration.overloadAggressively,
                                     specialNameFactory,
                                     specialDescriptorMap))))),

                // Clear the collected names.
                new MapCleaner(descriptorMap)
            ));

        // Replace conflicting private member names with special names.
        // This is only possible if those names were kept or mapped.
        appView.programClassPool.classesAccept(
            new MultiClassVisitor(
                // Collect all member names in this class.
                new AllMemberVisitor(
                new MemberNameCollector(configuration.overloadAggressively,
                                        descriptorMap)),

                // Collect all non-private member names higher up the hierarchy.
                new ClassHierarchyTraveler(false, true, true, false,
                new AllMemberVisitor(
                new MemberAccessFilter(0, AccessConstants.PRIVATE,
                new MemberNameCollector(configuration.overloadAggressively,
                                        descriptorMap)))),

                // Assign new names to all conflicting private members in this
                // class.
                new AllMemberVisitor(
                new MemberAccessFilter(AccessConstants.PRIVATE, 0,
                new MemberNameConflictFixer(configuration.overloadAggressively,
                                            descriptorMap,
                                            warningPrinter,
                new MemberObfuscator(configuration.overloadAggressively,
                                     specialNameFactory,
                                     specialDescriptorMap)))),

                // Clear the collected names.
                new MapCleaner(descriptorMap)
            ));

        // Print out any warnings about member name conflicts.
        int warningCount = warningPrinter.getWarningCount();
        if (warningCount > 0)
        {
            logger.warn("Warning: there were {} conflicting class member name mappings.", warningCount);
            logger.warn("         Your configuration may be inconsistent.");

            if (!configuration.ignoreWarnings)
            {
                logger.warn("         If you are sure the conflicts are harmless,");
                logger.warn("         you could try your luck using the '-ignorewarnings' option.");
            }

            logger.warn("         (https://www.guardsquare.com/proguard/manual/troubleshooting#mappingconflict2)");

            if (!configuration.ignoreWarnings)
            {
                throw new IOException("Please correct the above warnings first.");
            }
        }

        // Obfuscate the Intrinsics.check* method calls.
        appView.programClassPool.classesAccept(
            new InstructionSequenceObfuscator(
                new KotlinIntrinsicsReplacementSequences(appView.programClassPool, appView.libraryClassPool))
        );

        if (configuration.keepKotlinMetadata)
        {
            appView.programClassPool.classesAccept(
                new MultiClassVisitor(
                new ReferencedKotlinMetadataVisitor(
                    new MultiKotlinMetadataVisitor(
                    // Come up with new names for Kotlin Properties.
                    new KotlinPropertyNameObfuscator(nameFactory),
                    // Obfuscate alias names.
                    new KotlinAliasNameObfuscator(nameFactory),

                    // Ensure companion classes have $CompanionName suffix.
                    new KotlinCompanionEqualizer(),
                    // Equalise/fix $DefaultImpls and $WhenMappings classes.
                    new KotlinSyntheticClassFixer(),
                    // Ensure object classes have the INSTANCE field.
                    new KotlinObjectFixer(),

                    new AllFunctionVisitor(
                        // Ensure that all default interface implementations of methods have the same names.
                        new KotlinDefaultImplsMethodNameEqualizer(),
                        // Ensure all $default methods match their counterpart but with a $default suffix.
                        new KotlinDefaultMethodNameEqualizer(),
                        // Obfuscate the throw new UnsupportedOperationExceptions in $default methods
                        // because they contain the original function name in the string.
                        new KotlinFunctionToDefaultMethodVisitor(
                        new InstructionSequenceObfuscator(
                            new KotlinUnsupportedExceptionReplacementSequences(appView.programClassPool, appView.libraryClassPool)))
                    ),

                    // Obfuscate toString & toString-impl methods in data classes and inline/value classes.
                    new KotlinClassKindFilter(
                        kc -> (kc.flags.isValue || kc.flags.isData),
                        new KotlinSyntheticToStringObfuscator()))
                    )
                ));

            appView.resourceFilePool.resourceFilesAccept(
                new ResourceFileProcessingFlagFilter(0, ProcessingFlags.DONT_OBFUSCATE,
                                                     new KotlinModuleNameObfuscator(nameFactory)));
        }

        // Print out the mapping, if requested.
        if (configuration.printMapping != null)
        {
            logger.info("Printing mapping to [{}]...", PrintWriterUtil.fileName(configuration.printMapping));

            PrintWriter mappingWriter =
                PrintWriterUtil.createPrintWriter(configuration.printMapping, out);

            try
            {
                // Print out items that will be renamed.
                appView.programClassPool.classesAcceptAlphabetically(
                    new MappingPrinter(mappingWriter));
            }
            finally
            {
                PrintWriterUtil.closePrintWriter(configuration.printMapping,
                                                 mappingWriter);
            }
        }

        if (configuration.addConfigurationDebugging)
        {
            appView.programClassPool.classesAccept(new RenamedFlagSetter());
        }

        // Collect some statistics about the number of obfuscated
        // classes and members.
        ClassCounter  obfuscatedClassCounter  = new ClassCounter();
        MemberCounter obfuscatedFieldCounter  = new MemberCounter();
        MemberCounter obfuscatedMethodCounter = new MemberCounter();

        ClassVisitor classRenamer =
            new proguard.obfuscate.ClassRenamer(
                new ProgramClassFilter(
                obfuscatedClassCounter),

                new ProgramMemberFilter(
                new MethodFilter(
                obfuscatedMethodCounter,
                obfuscatedFieldCounter))
            );

        if (configuration.keepKotlinMetadata)
        {
            // Ensure multi-file parts and facades are in the same package.
            appView.programClassPool.classesAccept(
                new ReferencedKotlinMetadataVisitor(
                new KotlinMultiFileFacadeFixer()));
        }

        // Actually apply the new names.
        appView.programClassPool.classesAccept(classRenamer);
        appView.libraryClassPool.classesAccept(classRenamer);

        if (configuration.keepKotlinMetadata)
        {
            // Apply new names to Kotlin properties.
            appView.programClassPool.classesAccept(
                new ReferencedKotlinMetadataVisitor(
                new AllPropertyVisitor(
                new KotlinPropertyRenamer())));
        }

        // Update all references to these new names.
        appView.programClassPool.classesAccept(new ClassReferenceFixer(false));
        appView.libraryClassPool.classesAccept(new ClassReferenceFixer(false));
        appView.programClassPool.classesAccept(new MemberReferenceFixer(configuration.android));

        if (configuration.keepKotlinMetadata)
        {
            appView.programClassPool.classesAccept(
                new ReferencedKotlinMetadataVisitor(
                new MultiKotlinMetadataVisitor(
                new AllTypeVisitor(
                    // Fix all the alias references.
                    new KotlinAliasReferenceFixer()),

                // Fix all the CallableReference interface methods to match the new names.
                new KotlinCallableReferenceFixer(appView.programClassPool, appView.libraryClassPool))));
        }

        // Make package visible elements public or protected, if obfuscated
        // classes are being repackaged aggressively.
        if (configuration.repackageClasses != null &&
            configuration.allowAccessModification)
        {
            appView.programClassPool.classesAccept(
                new AccessFixer());

            // Fix the access flags of the inner classes information.
            // Don't change the access flags of inner classes that
            // have not been renamed (Guice). [DGD-63]
            appView.programClassPool.classesAccept(
                new OriginalClassNameFilter(null,
                new AllAttributeVisitor(
                new AllInnerClassesInfoVisitor(
                new InnerClassesAccessFixer()))));
        }

        // Fix the bridge method flags.
        appView.programClassPool.classesAccept(
            new AllMethodVisitor(
            new BridgeMethodFixer()));

        // Rename the source file attributes, if requested.
        if (configuration.newSourceFileAttribute != null)
        {
            appView.programClassPool.classesAccept(new SourceFileRenamer(configuration.newSourceFileAttribute));
        }

        // Remove unused constants.
        appView.programClassPool.classesAccept(
            new ConstantPoolShrinker());

        logger.info("  Number of obfuscated classes:                  {}", obfuscatedClassCounter.getCount());
        logger.info("  Number of obfuscated fields:                   {}", obfuscatedFieldCounter.getCount());
        logger.info("  Number of obfuscated methods:                  {}", obfuscatedMethodCounter.getCount());
    }
}
