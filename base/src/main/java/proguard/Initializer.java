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
package proguard;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.classfile.*;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.annotation.visitor.*;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.constant.visitor.AllConstantVisitor;
import proguard.classfile.instruction.visitor.AllInstructionVisitor;
import proguard.classfile.kotlin.*;
import proguard.classfile.util.*;
import proguard.classfile.util.kotlin.KotlinMetadataInitializer;
import proguard.classfile.visitor.*;
import proguard.resources.file.ResourceFilePool;
import proguard.resources.file.visitor.ResourceJavaReferenceClassInitializer;
import proguard.resources.kotlinmodule.util.KotlinModuleReferenceInitializer;
import proguard.util.*;
import proguard.util.kotlin.asserter.KotlinMetadataAsserter;

import java.io.*;
import java.util.*;

/**
 * This class initializes class pools and resource information.
 *
 * @author Eric Lafortune
 */
public class Initializer
{
    private static final Logger logger = LogManager.getLogger(Initializer.class);
    private final Configuration configuration;
    private final boolean       checkConfiguration;


    /**
     * Creates a new Initializer to initialize classes according to the given
     * configuration.
     */
    public Initializer(Configuration configuration)
    {
        this.configuration = configuration;

        // Only check the configuration if either shrinking, optimization or obfuscation is enabled,
        // in other cases the configuration does not have any effect. This will speed up pure debug builds.
        this.checkConfiguration = configuration.shrink   ||
                                  configuration.optimize ||
                                  configuration.obfuscate;
    }


    /**
     * Initializes the classes in the given program class pool and library class
     * pool, performs some basic checks, and shrinks the library class pool.
     */
    public void execute(ClassPool        programClassPool,
                        ClassPool        libraryClassPool,
                        ResourceFilePool resourceFilePool)
    throws IOException
    {
        int originalLibraryClassPoolSize = libraryClassPool.size();

        // Perform basic checks on the configuration.
        WarningPrinter fullyQualifiedClassNameNotePrinter = new WarningLogger(logger, configuration.note);

        if (checkConfiguration)
        {
            FullyQualifiedClassNameChecker fullyQualifiedClassNameChecker =
                new FullyQualifiedClassNameChecker(programClassPool,
                                                   libraryClassPool,
                                                   fullyQualifiedClassNameNotePrinter);

            fullyQualifiedClassNameChecker.checkClassSpecifications(configuration.keep);
            fullyQualifiedClassNameChecker.checkClassSpecifications(configuration.assumeNoSideEffects);
            fullyQualifiedClassNameChecker.checkClassSpecifications(configuration.assumeNoExternalSideEffects);
            fullyQualifiedClassNameChecker.checkClassSpecifications(configuration.assumeNoEscapingParameters);
            fullyQualifiedClassNameChecker.checkClassSpecifications(configuration.assumeNoExternalReturnValues);
        }

        StringMatcher keepAttributesMatcher = configuration.keepAttributes != null ?
            new ListParser(new NameParser()).parse(configuration.keepAttributes) :
            new EmptyStringMatcher();

        WarningPrinter getAnnotationNotePrinter = new WarningLogger(logger, configuration.note);

        if (!keepAttributesMatcher.matches(Attribute.RUNTIME_VISIBLE_ANNOTATIONS))
        {
            programClassPool.classesAccept(
                new AllConstantVisitor(
                new GetAnnotationChecker(getAnnotationNotePrinter)));
        }

        WarningPrinter getSignatureNotePrinter = new WarningLogger(logger, configuration.note);

        if (!keepAttributesMatcher.matches(Attribute.SIGNATURE))
        {
            programClassPool.classesAccept(
                new AllConstantVisitor(
                new GetSignatureChecker(getSignatureNotePrinter)));
        }

        WarningPrinter getEnclosingClassNotePrinter = new WarningLogger(logger, configuration.note);

        if (!keepAttributesMatcher.matches(Attribute.INNER_CLASSES))
        {
            programClassPool.classesAccept(
                new AllConstantVisitor(
                new GetEnclosingClassChecker(getEnclosingClassNotePrinter)));
        }

        WarningPrinter getEnclosingMethodNotePrinter = new WarningLogger(logger, configuration.note);

        if (!keepAttributesMatcher.matches(Attribute.ENCLOSING_METHOD))
        {
            programClassPool.classesAccept(
                new AllConstantVisitor(
                new GetEnclosingMethodChecker(getEnclosingMethodNotePrinter)));
        }

        // Construct a reduced library class pool with only those library
        // classes whose hierarchies are referenced by the program classes.
        // We can't do this if we later have to come up with the obfuscated
        // class member names that are globally unique.
        ClassPool reducedLibraryClassPool = configuration.useUniqueClassMemberNames ?
            null : new ClassPool();

        WarningPrinter classReferenceWarningPrinter = new WarningLogger(logger, configuration.warn);
        WarningPrinter dependencyWarningPrinter     = new WarningLogger(logger, configuration.warn);

        // Initialize the superclass hierarchies for program classes.
        programClassPool.classesAccept(
            new ClassSuperHierarchyInitializer(programClassPool,
                                               libraryClassPool,
                                               classReferenceWarningPrinter,
                                               null));

        // Initialize the superclass hierarchy of all library classes, without
        // warnings.
        libraryClassPool.classesAccept(
            new ClassSuperHierarchyInitializer(programClassPool,
                                               libraryClassPool,
                                               null,
                                               dependencyWarningPrinter));

        WarningPrinter kotlinInitializationWarningPrinter = new WarningLogger(logger, configuration.warn);

        // Initialize the Kotlin Metadata for Kotlin classes.
        if (configuration.keepKotlinMetadata)
        {
            ClassVisitor kotlinMetadataInitializer =
                new AllAttributeVisitor(
                new AttributeNameFilter(Attribute.RUNTIME_VISIBLE_ANNOTATIONS,
                new AllAnnotationVisitor(
                new AnnotationTypeFilter(KotlinConstants.TYPE_KOTLIN_METADATA,
                new KotlinMetadataInitializer(kotlinInitializationWarningPrinter)))));

            programClassPool.classesAccept(kotlinMetadataInitializer);
            libraryClassPool.classesAccept(kotlinMetadataInitializer);
        }

        // Initialize the class references of program class members and
        // attributes. Note that all superclass hierarchies have to be
        // initialized for this purpose.
        WarningPrinter programMemberReferenceWarningPrinter = new WarningLogger(logger, configuration.warn);
        WarningPrinter libraryMemberReferenceWarningPrinter = new WarningLogger(logger, configuration.warn);

        programClassPool.classesAccept(
            new ClassReferenceInitializer(programClassPool,
                                          libraryClassPool,
                                          classReferenceWarningPrinter,
                                          programMemberReferenceWarningPrinter,
                                          libraryMemberReferenceWarningPrinter,
                                          null));

        if (reducedLibraryClassPool != null)
        {
            // Collect the library classes that are directly referenced by
            // program classes, without reflection.
            programClassPool.classesAccept(
                new ReferencedClassVisitor(true,
                new LibraryClassFilter(
                new ClassPoolFiller(reducedLibraryClassPool))));

            // Reinitialize the superclass hierarchies of referenced library
            // classes, this time with warnings.
            reducedLibraryClassPool.classesAccept(
                new ClassSuperHierarchyInitializer(programClassPool,
                                                   libraryClassPool,
                                                   classReferenceWarningPrinter,
                                                   null));
        }

        // Initialize the enum annotation references.
        programClassPool.classesAccept(
            new AllAttributeVisitor(true,
            new AllElementValueVisitor(true,
            new EnumFieldReferenceInitializer())));

        // Initialize the Class.forName references.
        WarningPrinter dynamicClassReferenceNotePrinter = new WarningLogger(logger, configuration.note);
        WarningPrinter classForNameNotePrinter          = new WarningLogger(logger, configuration.note);

        programClassPool.classesAccept(
            new AllMethodVisitor(
            new AllAttributeVisitor(
            new AllInstructionVisitor(
            new DynamicClassReferenceInitializer(programClassPool,
                                                 libraryClassPool,
                                                 dynamicClassReferenceNotePrinter,
                                                 null,
                                                 classForNameNotePrinter,
                                                 createClassNoteExceptionMatcher(configuration.keep, true))))));

        // Initialize the Class.get[Declared]{Field,Method} references.
        WarningPrinter getMemberNotePrinter = new WarningLogger(logger, configuration.note);

        programClassPool.classesAccept(
            new AllMethodVisitor(
            new AllAttributeVisitor(
            new DynamicMemberReferenceInitializer(programClassPool,
                                                  libraryClassPool,
                                                  getMemberNotePrinter,
                                                  createClassMemberNoteExceptionMatcher(configuration.keep, true),
                                                  createClassMemberNoteExceptionMatcher(configuration.keep, false)))));

        // Initialize other string constant references, if requested.
        if (configuration.adaptClassStrings != null)
        {
            programClassPool.classesAccept(
                new ClassNameFilter(configuration.adaptClassStrings,
                new AllConstantVisitor(
                new StringReferenceInitializer(programClassPool,
                                               libraryClassPool))));
        }

        // Initialize the class references of library class members.
        if (reducedLibraryClassPool != null)
        {
            // Collect the library classes that are referenced by program
            // classes, directly or indirectly, with or without reflection.
            programClassPool.classesAccept(
                new ReferencedClassVisitor(
                new LibraryClassFilter(
                new ClassHierarchyTraveler(true, true, true, false,
                new LibraryClassFilter(
                new ClassPoolFiller(reducedLibraryClassPool))))));

            // Initialize the class references of referenced library
            // classes, without warnings.
            reducedLibraryClassPool.classesAccept(
                new ClassReferenceInitializer(programClassPool,
                                              libraryClassPool,
                                              null,
                                              null,
                                              null,
                                              dependencyWarningPrinter));

            // Reset the library class pool.
            libraryClassPool.clear();

            // Copy the library classes that are referenced directly by program
            // classes and the library classes that are referenced by referenced
            // library classes.
            reducedLibraryClassPool.classesAccept(
                new MultiClassVisitor(
                    new ClassHierarchyTraveler(true, true, true, false,
                    new LibraryClassFilter(
                    new ClassPoolFiller(libraryClassPool))),

                    new ReferencedClassVisitor(
                    new LibraryClassFilter(
                    new ClassHierarchyTraveler(true, true, true, false,
                    new LibraryClassFilter(
                    new ClassPoolFiller(libraryClassPool)))))
                ));
        }
        else
        {
            // Initialize the class references of all library class members.
            libraryClassPool.classesAccept(
                new ClassReferenceInitializer(programClassPool,
                                              libraryClassPool,
                                              null,
                                              null,
                                              null,
                                              dependencyWarningPrinter));
        }

        // Initialize the subclass hierarchies (in the right order,
        // with a single instance).
        ClassSubHierarchyInitializer classSubHierarchyInitializer =
            new ClassSubHierarchyInitializer();

        programClassPool.accept(classSubHierarchyInitializer);
        libraryClassPool.accept(classSubHierarchyInitializer);

        if (configuration.keepKotlinMetadata)
        {
            resourceFilePool.resourceFilesAccept(new KotlinModuleReferenceInitializer(programClassPool, libraryClassPool));

            if (configuration.enableKotlinAsserter)
            {
                new KotlinMetadataAsserter()
                    .execute(programClassPool,
                             libraryClassPool,
                             resourceFilePool,
                             kotlinInitializationWarningPrinter);
            }
        }

        // Share strings between the classes, to reduce heap memory usage.
        programClassPool.classesAccept(new StringSharer());
        libraryClassPool.classesAccept(new StringSharer());

        // Check for any unmatched class members.
        WarningPrinter classMemberNotePrinter = new WarningLogger(logger, configuration.note);

        if (checkConfiguration)
        {
            ClassMemberChecker classMemberChecker =
                new ClassMemberChecker(programClassPool,
                                       classMemberNotePrinter);

            classMemberChecker.checkClassSpecifications(configuration.keep);
            classMemberChecker.checkClassSpecifications(configuration.assumeNoSideEffects);
            classMemberChecker.checkClassSpecifications(configuration.assumeNoExternalSideEffects);
            classMemberChecker.checkClassSpecifications(configuration.assumeNoEscapingParameters);
            classMemberChecker.checkClassSpecifications(configuration.assumeNoExternalReturnValues);
        }

        // Check for unkept descriptor classes of kept class members.
        WarningPrinter descriptorKeepNotePrinter = new WarningLogger(logger, configuration.note);

        if (checkConfiguration)
        {
            new DescriptorKeepChecker(programClassPool,
                                      libraryClassPool,
                                      descriptorKeepNotePrinter).checkClassSpecifications(configuration.keep);
        }

        // Check for keep options that only match library classes.
        WarningPrinter libraryKeepNotePrinter = new WarningLogger(logger, configuration.note);

        if (checkConfiguration)
        {
            new LibraryKeepChecker(programClassPool,
                                   libraryClassPool,
                                   libraryKeepNotePrinter).checkClassSpecifications(configuration.keep);
        }

        // Initialize the references to Java classes in resource files.
        resourceFilePool.resourceFilesAccept(
            new ResourceJavaReferenceClassInitializer(programClassPool));

        // Print out a summary of the notes, if necessary.
        int fullyQualifiedNoteCount = fullyQualifiedClassNameNotePrinter.getWarningCount();
        if (fullyQualifiedNoteCount > 0)
        {
            logger.info("Note: there were {} references to unknown classes.", fullyQualifiedNoteCount);
            logger.info("      You should check your configuration for typos.");
            logger.info("      (https://www.guardsquare.com/proguard/manual/troubleshooting#unknownclass)");
        }

        int classMemberNoteCount = classMemberNotePrinter.getWarningCount();
        if (classMemberNoteCount > 0)
        {
            logger.info("Note: there were {} references to unknown class members.", classMemberNoteCount);
            logger.info("      You should check your configuration for typos.");
        }

        int getAnnotationNoteCount = getAnnotationNotePrinter.getWarningCount();
        if (getAnnotationNoteCount > 0)
        {
            logger.info("Note: there were {} classes trying to access annotations using reflection.",
                               getAnnotationNoteCount);
            logger.info("      You should consider keeping the annotation attributes");
            logger.info("      (using '-keepattributes *Annotation*').");
            logger.info("      (https://www.guardsquare.com/proguard/manual/troubleshooting#attributes)");
        }

        int getSignatureNoteCount = getSignatureNotePrinter.getWarningCount();
        if (getSignatureNoteCount > 0)
        {
            logger.info("Note: there were {} classes trying to access generic signatures using reflection.",
                         getSignatureNoteCount);
            logger.info("      You should consider keeping the signature attributes");
            logger.info("      (using '-keepattributes Signature').");
            logger.info("      (https://www.guardsquare.com/proguard/manual/troubleshooting#attributes)");
        }

        int getEnclosingClassNoteCount = getEnclosingClassNotePrinter.getWarningCount();
        if (getEnclosingClassNoteCount > 0)
        {
            logger.info("Note: there were {} classes trying to access enclosing classes using reflection.",
                         getEnclosingClassNoteCount);
            logger.info("      You should consider keeping the inner classes attributes");
            logger.info("      (using '-keepattributes InnerClasses').");
            logger.info("      (https://www.guardsquare.com/proguard/manual/troubleshooting#attributes)");
        }

        int getEnclosingMethodNoteCount = getEnclosingMethodNotePrinter.getWarningCount();
        if (getEnclosingMethodNoteCount > 0)
        {
            logger.info("Note: there were {} classes trying to access enclosing methods using reflection.",
                         getEnclosingMethodNoteCount);
            logger.info("      You should consider keeping the enclosing method attributes");
            logger.info("      (using '-keepattributes InnerClasses,EnclosingMethod').");
            logger.info("      (https://www.guardsquare.com/proguard/manual/troubleshooting#attributes)");
        }

        int descriptorNoteCount = descriptorKeepNotePrinter.getWarningCount();
        if (descriptorNoteCount > 0)
        {
            logger.info("Note: there were {} unkept descriptor classes in kept class members.",
                        descriptorNoteCount);
            logger.info("      You should consider explicitly keeping the mentioned classes");
            logger.info("      (using '-keep').");
            logger.info("      (https://www.guardsquare.com/proguard/manual/troubleshooting#descriptorclass)");
        }

        int libraryNoteCount = libraryKeepNotePrinter.getWarningCount();
        if (libraryNoteCount > 0)
        {
            logger.info("Note: there were {} library classes explicitly being kept.", libraryNoteCount);
            logger.info("      You don't need to keep library classes; they are already left unchanged.");
            logger.info("      (https://www.guardsquare.com/proguard/manual/troubleshooting#libraryclass)");
        }

        int dynamicClassReferenceNoteCount = dynamicClassReferenceNotePrinter.getWarningCount();
        if (dynamicClassReferenceNoteCount > 0)
        {
            logger.info("Note: there were {} unresolved dynamic references to classes or interfaces.",
                        dynamicClassReferenceNoteCount);
            logger.info("      You should check if you need to specify additional program jars.");
            logger.info("      (https://www.guardsquare.com/proguard/manual/troubleshooting#dynamicalclass)");
        }

        int classForNameNoteCount = classForNameNotePrinter.getWarningCount();
        if (classForNameNoteCount > 0)
        {
            logger.info("Note: there were {} class casts of dynamically created class instances.",
                        classForNameNoteCount);
            logger.info("      You might consider explicitly keeping the mentioned classes and/or");
            logger.info("      their implementations (using '-keep').");
            logger.info("      (https://www.guardsquare.com/proguard/manual/troubleshooting#dynamicalclasscast)");
        }

        int getmemberNoteCount = getMemberNotePrinter.getWarningCount();
        if (getmemberNoteCount > 0)
        {
            logger.info("Note: there were {} accesses to class members by means of reflection.",
                        getmemberNoteCount);
            logger.info("      You should consider explicitly keeping the mentioned class members");
            logger.info("      (using '-keep' or '-keepclassmembers').");
            logger.info("      (https://www.guardsquare.com/proguard/manual/troubleshooting#dynamicalclassmember)");
        }

        // Print out a summary of the warnings, if necessary.
        int classReferenceWarningCount = classReferenceWarningPrinter.getWarningCount();
        if (classReferenceWarningCount > 0)
        {
            logger.warn("Warning: there were {} unresolved references to classes or interfaces.",
                         classReferenceWarningCount);
            logger.warn("         You may need to add missing library jars or update their versions.");
            logger.warn("         If your code works fine without the missing classes, you can suppress");
            logger.warn("         the warnings with '-dontwarn' options.");

            if (configuration.skipNonPublicLibraryClasses)
            {
                logger.warn("         You may also have to remove the option '-skipnonpubliclibraryclasses'.");
            }

            logger.warn("         (https://www.guardsquare.com/proguard/manual/troubleshooting#unresolvedclass)");
        }

        int dependencyWarningCount = dependencyWarningPrinter.getWarningCount();
        if (dependencyWarningCount > 0)
        {
            logger.warn("Warning: there were {} instances of library classes depending on program classes.",
                         dependencyWarningCount);
            logger.warn("         You must avoid such dependencies, since the program classes will");
            logger.warn("         be processed, while the library classes will remain unchanged.");
            logger.warn("         (https://www.guardsquare.com/proguard/manual/troubleshooting#dependency)");
        }

        int programMemberReferenceWarningCount = programMemberReferenceWarningPrinter.getWarningCount();
        if (programMemberReferenceWarningCount > 0)
        {
            logger.warn("Warning: there were {} unresolved references to program class members.",
                         programMemberReferenceWarningCount);
            logger.warn("         Your input classes appear to be inconsistent.");
            logger.warn("         You may need to recompile the code.");
            logger.warn("         (https://www.guardsquare.com/proguard/manual/troubleshooting#unresolvedprogramclassmember)");
        }

        int libraryMemberReferenceWarningCount = libraryMemberReferenceWarningPrinter.getWarningCount();
        if (libraryMemberReferenceWarningCount > 0)
        {
            logger.warn("Warning: there were {} unresolved references to library class members.",
                         libraryMemberReferenceWarningCount);
            logger.warn("         You probably need to update the library versions.");

            if (!configuration.skipNonPublicLibraryClassMembers)
            {
                logger.warn("         Alternatively, you may have to specify the option ");
                logger.warn("         '-dontskipnonpubliclibraryclassmembers'.");
            }

            if (configuration.skipNonPublicLibraryClasses)
            {
                logger.warn("         You may also have to remove the option '-skipnonpubliclibraryclasses'.");
            }

            logger.warn("         (https://www.guardsquare.com/proguard/manual/troubleshooting#unresolvedlibraryclassmember)");
        }

        if (configuration.keepKotlinMetadata)
        {
            int kotlinInitializationWarningCount = kotlinInitializationWarningPrinter.getWarningCount();

            if (kotlinInitializationWarningCount > 0)
            {
                logger.warn("Warning: there were {} errors during Kotlin metadata initialisation.",
                             kotlinInitializationWarningCount);
            }
        }

        boolean incompatibleOptimization = configuration.optimize && !configuration.shrink;

        if (incompatibleOptimization)
        {
            logger.warn("Warning: optimization is enabled while shrinking is disabled.");
            logger.warn("         You need to remove the option -dontshrink or optimization might result in classes that fail verification at runtime.");
        }

        if ((classReferenceWarningCount         > 0 ||
             dependencyWarningCount             > 0 ||
             programMemberReferenceWarningCount > 0 ||
             libraryMemberReferenceWarningCount > 0) &&
            !configuration.ignoreWarnings)
        {
            throw new IOException("Please correct the above warnings first.");
        }

        if ((configuration.note == null ||
             !configuration.note.isEmpty()) &&
            (configuration.warn != null &&
             configuration.warn.isEmpty() ||
             configuration.ignoreWarnings))
        {
            logger.info("Note: you're ignoring all warnings!");
        }

        logger.info("Ignoring unused library classes...");
        logger.info("  Original number of library classes: {}", originalLibraryClassPoolSize);
        logger.info("  Final number of library classes:    {}", libraryClassPool.size());
    }


    /**
     * Extracts a list of exceptions of classes for which not to print notes,
     * from the keep configuration.
     */
    private StringMatcher createClassNoteExceptionMatcher(List    noteExceptions,
                                                          boolean markClasses)
    {
        if (noteExceptions != null)
        {
            List noteExceptionNames = new ArrayList(noteExceptions.size());
            for (int index = 0; index < noteExceptions.size(); index++)
            {
                KeepClassSpecification keepClassSpecification = (KeepClassSpecification)noteExceptions.get(index);
                if (keepClassSpecification.markClasses || !markClasses)
                {
                    // If the class itself is being kept, it's ok.
                    String className = keepClassSpecification.className;
                    if (className != null &&
                        !containsWildCardReferences(className))
                    {
                        noteExceptionNames.add(className);
                    }

                    // If all of its extensions are being kept, it's ok too.
                    String extendsClassName = keepClassSpecification.extendsClassName;
                    if (extendsClassName != null &&
                        !containsWildCardReferences(extendsClassName))
                    {
                        noteExceptionNames.add(extendsClassName);
                    }
                }
            }

            if (noteExceptionNames.size() > 0)
            {
                return new ListParser(new ClassNameParser()).parse(noteExceptionNames);
            }
        }

        return null;
    }


    /**
     * Extracts a list of exceptions of field or method names for which not to
     * print notes, from the keep configuration.
     */
    private StringMatcher createClassMemberNoteExceptionMatcher(List    noteExceptions,
                                                                boolean isField)
    {
        if (noteExceptions != null)
        {
            List noteExceptionNames = new ArrayList();
            for (int index = 0; index < noteExceptions.size(); index++)
            {
                KeepClassSpecification keepClassSpecification = (KeepClassSpecification)noteExceptions.get(index);
                List memberSpecifications = isField ?
                    keepClassSpecification.fieldSpecifications :
                    keepClassSpecification.methodSpecifications;

                if (memberSpecifications != null)
                {
                    for (int index2 = 0; index2 < memberSpecifications.size(); index2++)
                    {
                        MemberSpecification memberSpecification =
                            (MemberSpecification)memberSpecifications.get(index2);

                        String memberName = memberSpecification.name;
                        if (memberName != null &&
                            !containsWildCardReferences(memberName))
                        {
                            noteExceptionNames.add(memberName);
                        }
                    }
                }
            }

            if (noteExceptionNames.size() > 0)
            {
                return new ListParser(new NameParser()).parse(noteExceptionNames);
            }
        }

        return null;
    }


    /**
     * Returns whether the given string contains a numeric reference to a
     * wild card ("<n>").
     */
    private static boolean containsWildCardReferences(String string)
    {
        int openIndex = string.indexOf('<');
        if (openIndex < 0)
        {
            return false;
        }

        int closeIndex = string.indexOf('>', openIndex + 1);
        if (closeIndex < 0)
        {
            return false;
        }

        try
        {
            Integer.parseInt(string.substring(openIndex + 1, closeIndex));
        }
        catch (NumberFormatException e)
        {
            return false;
        }

        return true;
    }
}
