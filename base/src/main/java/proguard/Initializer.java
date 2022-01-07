/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2022 Guardsquare NV
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
import proguard.pass.Pass;
import proguard.resources.file.visitor.ResourceJavaReferenceClassInitializer;
import proguard.resources.kotlinmodule.util.KotlinModuleReferenceInitializer;
import proguard.util.*;

import java.io.*;
import java.util.*;

/**
 * This pass initializes class pools and resource information.
 *
 * @author Eric Lafortune
 */
public class Initializer implements Pass
{
    /**
     * Initializes the classes in the given program class pool and library class
     * pool, performs some basic checks, and shrinks the library class pool.
     */
    @Override
    public void execute(AppView appView) throws IOException
    {
        if (appView.configuration.verbose)
        {
            System.out.println("Initializing...");
        }

        boolean checkConfiguration = appView.configuration.shrink   ||
                                     appView.configuration.optimize ||
                                     appView.configuration.obfuscate;

        // We're using the system's default character encoding for writing to
        // the standard output and error output.
        PrintWriter out = new PrintWriter(System.out, true);
        PrintWriter err = new PrintWriter(System.err, true);

        int originalLibraryClassPoolSize = appView.libraryClassPool.size();

        // Perform basic checks on the configuration.
        WarningPrinter fullyQualifiedClassNameNotePrinter = new WarningPrinter(out, appView.configuration.note);

        if (checkConfiguration)
        {
            FullyQualifiedClassNameChecker fullyQualifiedClassNameChecker =
                new FullyQualifiedClassNameChecker(appView.programClassPool,
                                                   appView.libraryClassPool,
                                                   fullyQualifiedClassNameNotePrinter);

            fullyQualifiedClassNameChecker.checkClassSpecifications(appView.configuration.keep);
            fullyQualifiedClassNameChecker.checkClassSpecifications(appView.configuration.assumeNoSideEffects);
            fullyQualifiedClassNameChecker.checkClassSpecifications(appView.configuration.assumeNoExternalSideEffects);
            fullyQualifiedClassNameChecker.checkClassSpecifications(appView.configuration.assumeNoEscapingParameters);
            fullyQualifiedClassNameChecker.checkClassSpecifications(appView.configuration.assumeNoExternalReturnValues);
        }

        StringMatcher keepAttributesMatcher = appView.configuration.keepAttributes != null ?
            new ListParser(new NameParser()).parse(appView.configuration.keepAttributes) :
            new EmptyStringMatcher();

        WarningPrinter getAnnotationNotePrinter = new WarningPrinter(out, appView.configuration.note);

        if (!keepAttributesMatcher.matches(Attribute.RUNTIME_VISIBLE_ANNOTATIONS))
        {
            appView.programClassPool.classesAccept(
                new AllConstantVisitor(
                new GetAnnotationChecker(getAnnotationNotePrinter)));
        }

        WarningPrinter getSignatureNotePrinter = new WarningPrinter(out, appView.configuration.note);

        if (!keepAttributesMatcher.matches(Attribute.SIGNATURE))
        {
            appView.programClassPool.classesAccept(
                new AllConstantVisitor(
                new GetSignatureChecker(getSignatureNotePrinter)));
        }

        WarningPrinter getEnclosingClassNotePrinter = new WarningPrinter(out, appView.configuration.note);

        if (!keepAttributesMatcher.matches(Attribute.INNER_CLASSES))
        {
            appView.programClassPool.classesAccept(
                new AllConstantVisitor(
                new GetEnclosingClassChecker(getEnclosingClassNotePrinter)));
        }

        WarningPrinter getEnclosingMethodNotePrinter = new WarningPrinter(out, appView.configuration.note);

        if (!keepAttributesMatcher.matches(Attribute.ENCLOSING_METHOD))
        {
            appView.programClassPool.classesAccept(
                new AllConstantVisitor(
                new GetEnclosingMethodChecker(getEnclosingMethodNotePrinter)));
        }

        // Construct a reduced library class pool with only those library
        // classes whose hierarchies are referenced by the program classes.
        // We can't do this if we later have to come up with the obfuscated
        // class member names that are globally unique.
        ClassPool reducedLibraryClassPool = appView.configuration.useUniqueClassMemberNames ?
            null : new ClassPool();

        WarningPrinter classReferenceWarningPrinter = new WarningPrinter(err, appView.configuration.warn);
        WarningPrinter dependencyWarningPrinter     = new WarningPrinter(err, appView.configuration.warn);

        // Initialize the superclass hierarchies for program classes.
        appView.programClassPool.classesAccept(
            new ClassSuperHierarchyInitializer(appView.programClassPool,
                                               appView.libraryClassPool,
                                               classReferenceWarningPrinter,
                                               null));

        // Initialize the superclass hierarchy of all library classes, without
        // warnings.
        appView.libraryClassPool.classesAccept(
            new ClassSuperHierarchyInitializer(appView.programClassPool,
                                               appView.libraryClassPool,
                                               null,
                                               dependencyWarningPrinter));

        WarningPrinter kotlinInitializationWarningPrinter = new WarningPrinter(err, appView.configuration.warn);

        // Initialize the Kotlin Metadata for Kotlin classes.
        if (appView.configuration.keepKotlinMetadata)
        {
            ClassVisitor kotlinMetadataInitializer =
                new AllAttributeVisitor(
                new AttributeNameFilter(Attribute.RUNTIME_VISIBLE_ANNOTATIONS,
                new AllAnnotationVisitor(
                new AnnotationTypeFilter(KotlinConstants.TYPE_KOTLIN_METADATA,
                new KotlinMetadataInitializer(kotlinInitializationWarningPrinter)))));

            appView.programClassPool.classesAccept(kotlinMetadataInitializer);
            appView.libraryClassPool.classesAccept(kotlinMetadataInitializer);
        }

        // Initialize the class references of program class members and
        // attributes. Note that all superclass hierarchies have to be
        // initialized for this purpose.
        WarningPrinter programMemberReferenceWarningPrinter = new WarningPrinter(err, appView.configuration.warn);
        WarningPrinter libraryMemberReferenceWarningPrinter = new WarningPrinter(err, appView.configuration.warn);

        appView.programClassPool.classesAccept(
            new ClassReferenceInitializer(appView.programClassPool,
                                          appView.libraryClassPool,
                                          classReferenceWarningPrinter,
                                          programMemberReferenceWarningPrinter,
                                          libraryMemberReferenceWarningPrinter,
                                          null));

        if (reducedLibraryClassPool != null)
        {
            // Collect the library classes that are directly referenced by
            // program classes, without reflection.
            appView.programClassPool.classesAccept(
                new ReferencedClassVisitor(true,
                new LibraryClassFilter(
                new ClassPoolFiller(reducedLibraryClassPool))));

            // Reinitialize the superclass hierarchies of referenced library
            // classes, this time with warnings.
            reducedLibraryClassPool.classesAccept(
                new ClassSuperHierarchyInitializer(appView.programClassPool,
                                                   appView.libraryClassPool,
                                                   classReferenceWarningPrinter,
                                                   null));
        }

        // Initialize the enum annotation references.
        appView.programClassPool.classesAccept(
            new AllAttributeVisitor(true,
            new AllElementValueVisitor(true,
            new EnumFieldReferenceInitializer())));

        // Initialize the Class.forName references.
        WarningPrinter dynamicClassReferenceNotePrinter = new WarningPrinter(out, appView.configuration.note);
        WarningPrinter classForNameNotePrinter          = new WarningPrinter(out, appView.configuration.note);

        appView.programClassPool.classesAccept(
            new AllMethodVisitor(
            new AllAttributeVisitor(
            new AllInstructionVisitor(
            new DynamicClassReferenceInitializer(appView.programClassPool,
                                                 appView.libraryClassPool,
                                                 dynamicClassReferenceNotePrinter,
                                                 null,
                                                 classForNameNotePrinter,
                                                 createClassNoteExceptionMatcher(appView.configuration.keep, true))))));

        // Initialize the Class.get[Declared]{Field,Method} references.
        WarningPrinter getMemberNotePrinter = new WarningPrinter(out, appView.configuration.note);

        appView.programClassPool.classesAccept(
            new AllMethodVisitor(
            new AllAttributeVisitor(
            new DynamicMemberReferenceInitializer(appView.programClassPool,
                                                  appView.libraryClassPool,
                                                  getMemberNotePrinter,
                                                  createClassMemberNoteExceptionMatcher(appView.configuration.keep, true),
                                                  createClassMemberNoteExceptionMatcher(appView.configuration.keep, false)))));

        // Initialize other string constant references, if requested.
        if (appView.configuration.adaptClassStrings != null)
        {
            appView.programClassPool.classesAccept(
                new ClassNameFilter(appView.configuration.adaptClassStrings,
                new AllConstantVisitor(
                new StringReferenceInitializer(appView.programClassPool,
                                               appView.libraryClassPool))));
        }

        // Initialize the class references of library class members.
        if (reducedLibraryClassPool != null)
        {
            // Collect the library classes that are referenced by program
            // classes, directly or indirectly, with or without reflection.
            appView.programClassPool.classesAccept(
                new ReferencedClassVisitor(
                new LibraryClassFilter(
                new ClassHierarchyTraveler(true, true, true, false,
                new LibraryClassFilter(
                new ClassPoolFiller(reducedLibraryClassPool))))));

            // Initialize the class references of referenced library
            // classes, without warnings.
            reducedLibraryClassPool.classesAccept(
                new ClassReferenceInitializer(appView.programClassPool,
                                              appView.libraryClassPool,
                                              null,
                                              null,
                                              null,
                                              dependencyWarningPrinter));

            // Reset the library class pool.
            appView.libraryClassPool.clear();

            // Copy the library classes that are referenced directly by program
            // classes and the library classes that are referenced by referenced
            // library classes.
            reducedLibraryClassPool.classesAccept(
                new MultiClassVisitor(
                    new ClassHierarchyTraveler(true, true, true, false,
                    new LibraryClassFilter(
                    new ClassPoolFiller(appView.libraryClassPool))),

                    new ReferencedClassVisitor(
                    new LibraryClassFilter(
                    new ClassHierarchyTraveler(true, true, true, false,
                    new LibraryClassFilter(
                    new ClassPoolFiller(appView.libraryClassPool)))))
                ));
        }
        else
        {
            // Initialize the class references of all library class members.
            appView.libraryClassPool.classesAccept(
                new ClassReferenceInitializer(appView.programClassPool,
                                              appView.libraryClassPool,
                                              null,
                                              null,
                                              null,
                                              dependencyWarningPrinter));
        }

        // Initialize the subclass hierarchies (in the right order,
        // with a single instance).
        ClassSubHierarchyInitializer classSubHierarchyInitializer =
            new ClassSubHierarchyInitializer();

        appView.programClassPool.accept(classSubHierarchyInitializer);
        appView.libraryClassPool.accept(classSubHierarchyInitializer);

        if (appView.configuration.keepKotlinMetadata)
        {
            appView.resourceFilePool.resourceFilesAccept(new KotlinModuleReferenceInitializer(appView.programClassPool, appView.libraryClassPool));
        }

        // Share strings between the classes, to reduce heap memory usage.
        appView.programClassPool.classesAccept(new StringSharer());
        appView.libraryClassPool.classesAccept(new StringSharer());

        // Check for any unmatched class members.
        WarningPrinter classMemberNotePrinter = new WarningPrinter(out, appView.configuration.note);

        if (checkConfiguration)
        {
            ClassMemberChecker classMemberChecker =
                new ClassMemberChecker(appView.programClassPool,
                                       classMemberNotePrinter);

            classMemberChecker.checkClassSpecifications(appView.configuration.keep);
            classMemberChecker.checkClassSpecifications(appView.configuration.assumeNoSideEffects);
            classMemberChecker.checkClassSpecifications(appView.configuration.assumeNoExternalSideEffects);
            classMemberChecker.checkClassSpecifications(appView.configuration.assumeNoEscapingParameters);
            classMemberChecker.checkClassSpecifications(appView.configuration.assumeNoExternalReturnValues);
        }

        // Check for unkept descriptor classes of kept class members.
        WarningPrinter descriptorKeepNotePrinter = new WarningPrinter(out, appView.configuration.note);

        if (checkConfiguration)
        {
            new DescriptorKeepChecker(appView.programClassPool,
                                      appView.libraryClassPool,
                                      descriptorKeepNotePrinter).checkClassSpecifications(appView.configuration.keep);
        }

        // Check for keep options that only match library classes.
        WarningPrinter libraryKeepNotePrinter = new WarningPrinter(out, appView.configuration.note);

        if (checkConfiguration)
        {
            new LibraryKeepChecker(appView.programClassPool,
                                   appView.libraryClassPool,
                                   libraryKeepNotePrinter).checkClassSpecifications(appView.configuration.keep);
        }

        // Initialize the references to Java classes in resource files.
        appView.resourceFilePool.resourceFilesAccept(
            new ResourceJavaReferenceClassInitializer(appView.programClassPool));

        // Print out a summary of the notes, if necessary.
        int fullyQualifiedNoteCount = fullyQualifiedClassNameNotePrinter.getWarningCount();
        if (fullyQualifiedNoteCount > 0)
        {
            out.println("Note: there were " + fullyQualifiedNoteCount +
                        " references to unknown classes.");
            out.println("      You should check your configuration for typos.");
            out.println("      (https://www.guardsquare.com/proguard/manual/troubleshooting#unknownclass)");
        }

        int classMemberNoteCount = classMemberNotePrinter.getWarningCount();
        if (classMemberNoteCount > 0)
        {
            out.println("Note: there were " + classMemberNoteCount +
                        " references to unknown class members.");
            out.println("      You should check your configuration for typos.");
        }

        int getAnnotationNoteCount = getAnnotationNotePrinter.getWarningCount();
        if (getAnnotationNoteCount > 0)
        {
            out.println("Note: there were " + getAnnotationNoteCount +
                        " classes trying to access annotations using reflection.");
            out.println("      You should consider keeping the annotation attributes");
            out.println("      (using '-keepattributes *Annotation*').");
            out.println("      (https://www.guardsquare.com/proguard/manual/troubleshooting#attributes)");
        }

        int getSignatureNoteCount = getSignatureNotePrinter.getWarningCount();
        if (getSignatureNoteCount > 0)
        {
            out.println("Note: there were " + getSignatureNoteCount +
                        " classes trying to access generic signatures using reflection.");
            out.println("      You should consider keeping the signature attributes");
            out.println("      (using '-keepattributes Signature').");
            out.println("      (https://www.guardsquare.com/proguard/manual/troubleshooting#attributes)");
        }

        int getEnclosingClassNoteCount = getEnclosingClassNotePrinter.getWarningCount();
        if (getEnclosingClassNoteCount > 0)
        {
            out.println("Note: there were " + getEnclosingClassNoteCount +
                        " classes trying to access enclosing classes using reflection.");
            out.println("      You should consider keeping the inner classes attributes");
            out.println("      (using '-keepattributes InnerClasses').");
            out.println("      (https://www.guardsquare.com/proguard/manual/troubleshooting#attributes)");
        }

        int getEnclosingMethodNoteCount = getEnclosingMethodNotePrinter.getWarningCount();
        if (getEnclosingMethodNoteCount > 0)
        {
            out.println("Note: there were " + getEnclosingMethodNoteCount +
                        " classes trying to access enclosing methods using reflection.");
            out.println("      You should consider keeping the enclosing method attributes");
            out.println("      (using '-keepattributes InnerClasses,EnclosingMethod').");
            out.println("      (https://www.guardsquare.com/proguard/manual/troubleshooting#attributes)");
        }

        int descriptorNoteCount = descriptorKeepNotePrinter.getWarningCount();
        if (descriptorNoteCount > 0)
        {
            out.println("Note: there were " + descriptorNoteCount +
                        " unkept descriptor classes in kept class members.");
            out.println("      You should consider explicitly keeping the mentioned classes");
            out.println("      (using '-keep').");
            out.println("      (https://www.guardsquare.com/proguard/manual/troubleshooting#descriptorclass)");
        }

        int libraryNoteCount = libraryKeepNotePrinter.getWarningCount();
        if (libraryNoteCount > 0)
        {
            out.println("Note: there were " + libraryNoteCount +
                               " library classes explicitly being kept.");
            out.println("      You don't need to keep library classes; they are already left unchanged.");
            out.println("      (https://www.guardsquare.com/proguard/manual/troubleshooting#libraryclass)");
        }

        int dynamicClassReferenceNoteCount = dynamicClassReferenceNotePrinter.getWarningCount();
        if (dynamicClassReferenceNoteCount > 0)
        {
            out.println("Note: there were " + dynamicClassReferenceNoteCount +
                        " unresolved dynamic references to classes or interfaces.");
            out.println("      You should check if you need to specify additional program jars.");
            out.println("      (https://www.guardsquare.com/proguard/manual/troubleshooting#dynamicalclass)");
        }

        int classForNameNoteCount = classForNameNotePrinter.getWarningCount();
        if (classForNameNoteCount > 0)
        {
            out.println("Note: there were " + classForNameNoteCount +
                        " class casts of dynamically created class instances.");
            out.println("      You might consider explicitly keeping the mentioned classes and/or");
            out.println("      their implementations (using '-keep').");
            out.println("      (https://www.guardsquare.com/proguard/manual/troubleshooting#dynamicalclasscast)");
        }

        int getmemberNoteCount = getMemberNotePrinter.getWarningCount();
        if (getmemberNoteCount > 0)
        {
            out.println("Note: there were " + getmemberNoteCount +
                        " accesses to class members by means of reflection.");
            out.println("      You should consider explicitly keeping the mentioned class members");
            out.println("      (using '-keep' or '-keepclassmembers').");
            out.println("      (https://www.guardsquare.com/proguard/manual/troubleshooting#dynamicalclassmember)");
        }

        // Print out a summary of the warnings, if necessary.
        int classReferenceWarningCount = classReferenceWarningPrinter.getWarningCount();
        if (classReferenceWarningCount > 0)
        {
            err.println("Warning: there were " + classReferenceWarningCount +
                        " unresolved references to classes or interfaces.");
            err.println("         You may need to add missing library jars or update their versions.");
            err.println("         If your code works fine without the missing classes, you can suppress");
            err.println("         the warnings with '-dontwarn' options.");

            if (appView.configuration.skipNonPublicLibraryClasses)
            {
                err.println("         You may also have to remove the option '-skipnonpubliclibraryclasses'.");
            }

            err.println("         (https://www.guardsquare.com/proguard/manual/troubleshooting#unresolvedclass)");
        }

        int dependencyWarningCount = dependencyWarningPrinter.getWarningCount();
        if (dependencyWarningCount > 0)
        {
            err.println("Warning: there were " + dependencyWarningCount +
                        " instances of library classes depending on program classes.");
            err.println("         You must avoid such dependencies, since the program classes will");
            err.println("         be processed, while the library classes will remain unchanged.");
            err.println("         (https://www.guardsquare.com/proguard/manual/troubleshooting#dependency)");
        }

        int programMemberReferenceWarningCount = programMemberReferenceWarningPrinter.getWarningCount();
        if (programMemberReferenceWarningCount > 0)
        {
            err.println("Warning: there were " + programMemberReferenceWarningCount +
                        " unresolved references to program class members.");
            err.println("         Your input classes appear to be inconsistent.");
            err.println("         You may need to recompile the code.");
            err.println("         (https://www.guardsquare.com/proguard/manual/troubleshooting#unresolvedprogramclassmember)");
        }

        int libraryMemberReferenceWarningCount = libraryMemberReferenceWarningPrinter.getWarningCount();
        if (libraryMemberReferenceWarningCount > 0)
        {
            err.println("Warning: there were " + libraryMemberReferenceWarningCount +
                        " unresolved references to library class members.");
            err.println("         You probably need to update the library versions.");

            if (!appView.configuration.skipNonPublicLibraryClassMembers)
            {
                err.println("         Alternatively, you may have to specify the option ");
                err.println("         '-dontskipnonpubliclibraryclassmembers'.");
            }

            if (appView.configuration.skipNonPublicLibraryClasses)
            {
                err.println("         You may also have to remove the option '-skipnonpubliclibraryclasses'.");
            }

            err.println("         (https://www.guardsquare.com/proguard/manual/troubleshooting#unresolvedlibraryclassmember)");
        }

        if (appView.configuration.keepKotlinMetadata)
        {
            int kotlinInitializationWarningCount = kotlinInitializationWarningPrinter.getWarningCount();

            if (kotlinInitializationWarningCount > 0)
            {
                err.println("Warning: there were " + kotlinInitializationWarningCount +
                            " errors during Kotlin metadata initialisation.");
            }
        }

        boolean incompatibleOptimization = appView.configuration.optimize && !appView.configuration.shrink;

        if (incompatibleOptimization)
        {
            err.println("Warning: optimization is enabled while shrinking is disabled.");
            err.println("         You need to remove the option -dontshrink or optimization might result in classes that fail verification at runtime.");
        }

        if ((classReferenceWarningCount         > 0 ||
             dependencyWarningCount             > 0 ||
             programMemberReferenceWarningCount > 0 ||
             libraryMemberReferenceWarningCount > 0) &&
            !appView.configuration.ignoreWarnings)
        {
            throw new IOException("Please correct the above warnings first.");
        }

        if ((appView.configuration.note == null ||
             !appView.configuration.note.isEmpty()) &&
            (appView.configuration.warn != null &&
             appView.configuration.warn.isEmpty() ||
             appView.configuration.ignoreWarnings))
        {
            out.println("Note: you're ignoring all warnings!");
        }

        // Discard unused library classes.
        if (appView.configuration.verbose)
        {
            out.println("Ignoring unused library classes...");
            out.println("  Original number of library classes: " + originalLibraryClassPoolSize);
            out.println("  Final number of library classes:    " + appView.libraryClassPool.size());
        }
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
