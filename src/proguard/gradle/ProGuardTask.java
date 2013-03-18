/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2013 Eric Lafortune (eric@graphics.cornell.edu)
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
package proguard.gradle;

import groovy.lang.Closure;
import org.gradle.api.*;
import org.gradle.api.file.*;
import org.gradle.api.tasks.TaskAction;
import proguard.*;
import proguard.classfile.*;
import proguard.classfile.util.ClassUtil;
import proguard.util.ListUtil;

import java.io.*;
import java.util.*;

/**
 * This Task allows to configure and run ProGuard from Gradle.
 *
 * @author Eric Lafortune
 */
public class ProGuardTask extends DefaultTask
{
    private final Configuration configuration = new Configuration();

    // Field acting as a parameter for the class member specification methods.
    private ClassSpecification classSpecification;


    // Gradle task settings.

    public void configuration(Object configurationFile)
    throws ParseException, IOException
    {
        ConfigurationParser parser =
            new ConfigurationParser(resolvedFile(configurationFile),
                                    System.getProperties());
        try
        {
            parser.parse(configuration);
        }
        finally
        {
            parser.close();
        }
    }

    public void injars(Object programJars)
    throws ParseException
    {
        injars(null, programJars);
    }

    public void injars(Map filterArgs, Object programJars)
    throws ParseException
    {
        configuration.programJars =
            extendClassPath(configuration.programJars,
                            resolvedFiles(programJars),
                            filterArgs,
                            false);
    }

    public void outjars(Object programJars)
    throws ParseException
    {
        outjars(null, programJars);
    }

    public void outjars(Map filterArgs, Object programJars)
    throws ParseException
    {
        configuration.programJars =
            extendClassPath(configuration.programJars,
                            resolvedFiles(programJars),
                            filterArgs,
                            true);
    }

    public void libraryjars(Object libraryJars)
    throws ParseException
    {
        libraryjars(null, libraryJars);
    }

    public void libraryjars(Map filterArgs, Object libraryJars)
    throws ParseException
    {
        configuration.libraryJars =
            extendClassPath(configuration.libraryJars,
                            resolvedFiles(libraryJars),
                            filterArgs,
                            false);
    }

    // Hack: support the keyword without parentheses in Groovy.
    public Object getskipnonpubliclibraryclasses()
    {
        skipnonpubliclibraryclasses();
        return null;
    }

    public void skipnonpubliclibraryclasses()
    {
        configuration.skipNonPublicLibraryClasses = true;
    }

    // Hack: support the keyword without parentheses in Groovy.
    public Object getdontskipnonpubliclibraryclassmembers()
    {
        dontskipnonpubliclibraryclassmembers();
        return null;
    }

    public void dontskipnonpubliclibraryclassmembers()
    {
        configuration.skipNonPublicLibraryClassMembers = false;
    }

    // Hack: support the keyword without parentheses in Groovy.
    public Object getkeepdirectories()
    {
        keepdirectories();
        return null;
    }

    public void keepdirectories()
    {
        keepdirectories(null);
    }

    public void keepdirectories(String filter)
    {
        configuration.keepDirectories =
            extendFilter(configuration.keepDirectories, filter);
    }

    public void target(String targetClassVersion)
    {
        configuration.targetClassVersion =
            ClassUtil.internalClassVersion(targetClassVersion);
    }

    // Hack: support the keyword without parentheses in Groovy.
    public Object getforceprocessing()
    {
        forceprocessing();
        return null;
    }

    public void forceprocessing()
    {
        configuration.lastModified = Long.MAX_VALUE;
    }

    public void keep(String classSpecificationString)
    throws ParseException
    {
        keep(null, classSpecificationString);
    }

    public void keep(Map    keepArgs,
                     String classSpecificationString)
    throws ParseException
    {
        configuration.keep =
            extendClassSpecifications(configuration.keep,
            createKeepClassSpecification(false,
                                         true,
                                         false,
                                         keepArgs,
                                         classSpecificationString));
    }

    public void keep(Map keepClassSpecificationArgs)
    throws ParseException
    {
        keep(keepClassSpecificationArgs, (Closure)null);
    }

    public void keep(Map     keepClassSpecificationArgs,
                     Closure classMembersClosure)
    throws ParseException
    {
        configuration.keep =
            extendClassSpecifications(configuration.keep,
            createKeepClassSpecification(false,
                                         true,
                                         false,
                                         keepClassSpecificationArgs,
                                         classMembersClosure));
    }

    public void keepclassmembers(String classSpecificationString)
    throws ParseException
    {
        keepclassmembers(null, classSpecificationString);
    }

    public void keepclassmembers(Map    keepArgs,
                                 String classSpecificationString)
    throws ParseException
    {
        configuration.keep =
            extendClassSpecifications(configuration.keep,
            createKeepClassSpecification(false,
                                         false,
                                         false,
                                         keepArgs,
                                         classSpecificationString));
    }

    public void keepclassmembers(Map keepClassSpecificationArgs)
    throws ParseException
    {
        keepclassmembers(keepClassSpecificationArgs, (Closure)null);
    }

    public void keepclassmembers(Map     keepClassSpecificationArgs,
                                 Closure classMembersClosure)
    throws ParseException
    {
        configuration.keep =
            extendClassSpecifications(configuration.keep,
            createKeepClassSpecification(false,
                                         false,
                                         false,
                                         keepClassSpecificationArgs,
                                         classMembersClosure));
    }

    public void keepclasseswithmembers(String classSpecificationString)
    throws ParseException
    {
        keepclasseswithmembers(null, classSpecificationString);
    }

    public void keepclasseswithmembers(Map    keepArgs,
                                       String classSpecificationString)
    throws ParseException
    {
        configuration.keep =
            extendClassSpecifications(configuration.keep,
            createKeepClassSpecification(false,
                                         false,
                                         true,
                                         keepArgs,
                                         classSpecificationString));
    }

    public void keepclasseswithmembers(Map keepClassSpecificationArgs)
    throws ParseException
    {
        keepclasseswithmembers(keepClassSpecificationArgs, (Closure)null);
    }

    public void keepclasseswithmembers(Map     keepClassSpecificationArgs,
                                       Closure classMembersClosure)
    throws ParseException
    {
        configuration.keep =
            extendClassSpecifications(configuration.keep,
            createKeepClassSpecification(false,
                                         false,
                                         true,
                                         keepClassSpecificationArgs,
                                         classMembersClosure));
    }

    public void keepnames(String classSpecificationString)
    throws ParseException
    {
        keepnames(null, classSpecificationString);
    }

    public void keepnames(Map    keepArgs,
                          String classSpecificationString)
    throws ParseException
    {
        configuration.keep =
            extendClassSpecifications(configuration.keep,
            createKeepClassSpecification(true,
                                         true,
                                         false,
                                         keepArgs,
                                         classSpecificationString));
    }

    public void keepnames(Map keepClassSpecificationArgs)
    throws ParseException
    {
        keepnames(keepClassSpecificationArgs, (Closure)null);
    }

    public void keepnames(Map     keepClassSpecificationArgs,
                          Closure classMembersClosure)
    throws ParseException
    {
        configuration.keep =
            extendClassSpecifications(configuration.keep,
            createKeepClassSpecification(true,
                                         true,
                                         false,
                                         keepClassSpecificationArgs,
                                         classMembersClosure));
    }

    public void keepclassmembernames(String classSpecificationString)
    throws ParseException
    {
        keepclassmembernames(null, classSpecificationString);
    }

    public void keepclassmembernames(Map    keepArgs,
                                     String classSpecificationString)
    throws ParseException
    {
        configuration.keep =
            extendClassSpecifications(configuration.keep,
            createKeepClassSpecification(true,
                                         false,
                                         false,
                                         keepArgs,
                                         classSpecificationString));
    }

    public void keepclassmembernames(Map keepClassSpecificationArgs)
    throws ParseException
    {
        keepclassmembernames(keepClassSpecificationArgs, (Closure)null);
    }

    public void keepclassmembernames(Map     keepClassSpecificationArgs,
                                     Closure classMembersClosure)
    throws ParseException
    {
        configuration.keep =
            extendClassSpecifications(configuration.keep,
            createKeepClassSpecification(true,
                                         false,
                                         false,
                                         keepClassSpecificationArgs,
                                         classMembersClosure));
    }

    public void keepclasseswithmembernames(String classSpecificationString)
    throws ParseException
    {
        keepclasseswithmembernames(null, classSpecificationString);
    }

    public void keepclasseswithmembernames(Map    keepArgs,
                                           String classSpecificationString)
    throws ParseException
    {
        configuration.keep =
            extendClassSpecifications(configuration.keep,
            createKeepClassSpecification(true,
                                         false,
                                         true,
                                         keepArgs,
                                         classSpecificationString));
    }

    public void keepclasseswithmembernames(Map keepClassSpecificationArgs)
    throws ParseException
    {
        keepclasseswithmembernames(keepClassSpecificationArgs, (Closure)null);
    }

    public void keepclasseswithmembernames(Map     keepClassSpecificationArgs,
                                           Closure classMembersClosure)
    throws ParseException
    {
        configuration.keep =
            extendClassSpecifications(configuration.keep,
            createKeepClassSpecification(true,
                                         false,
                                         true,
                                         keepClassSpecificationArgs,
                                         classMembersClosure));
    }

    // Hack: support the keyword without parentheses in Groovy.
    public Object getprintseeds()
    {
        printseeds();
        return null;
    }

    public void printseeds()
    {
        configuration.printSeeds = Configuration.STD_OUT;
    }

    public void printseeds(Object printSeeds)
    throws ParseException
    {
        configuration.printSeeds = resolvedFile(printSeeds);
    }

    // Hack: support the keyword without parentheses in Groovy.
    public Object getdontshrink()
    {
        dontshrink();
        return null;
    }

    public void dontshrink()
    {
        configuration.shrink = false;
    }

    // Hack: support the keyword without parentheses in Groovy.
    public Object getprintusage()
    {
        printusage();
        return null;
    }

    public void printusage()
    {
        configuration.printUsage = Configuration.STD_OUT;
    }

    public void printusage(Object printUsage)
    throws ParseException
    {
        configuration.printUsage = resolvedFile(printUsage);
    }

    public void whyareyoukeeping(String classSpecificationString)
    throws ParseException
    {
        configuration.whyAreYouKeeping =
            extendClassSpecifications(configuration.whyAreYouKeeping,
                                      createClassSpecification(classSpecificationString));
    }

    public void whyareyoukeeping(Map classSpecificationArgs)
    throws ParseException
    {
        whyareyoukeeping(classSpecificationArgs, null);
    }

    public void whyareyoukeeping(Map     classSpecificationArgs,
                                 Closure classMembersClosure)
    throws ParseException
    {
        configuration.whyAreYouKeeping =
            extendClassSpecifications(configuration.whyAreYouKeeping,
            createClassSpecification(classSpecificationArgs,
                                     classMembersClosure));
    }

    // Hack: support the keyword without parentheses in Groovy.
    public Object getdontoptimize()
    {
        dontoptimize();
        return null;
    }

    public void dontoptimize()
    {
        configuration.optimize = false;
    }

    public void optimizations(String filter)
    {
        configuration.optimizations =
            extendFilter(configuration.optimizations, filter);
    }


    public void optimizationpasses(int optimizationPasses)
    {
        configuration.optimizationPasses = optimizationPasses;
    }

    public void assumenosideeffects(String classSpecificationString)
    throws ParseException
    {
        configuration.assumeNoSideEffects =
            extendClassSpecifications(configuration.assumeNoSideEffects,
            createClassSpecification(classSpecificationString));
    }

    public void assumenosideeffects(Map     classSpecificationArgs,
                                    Closure classMembersClosure)
    throws ParseException
    {
        configuration.assumeNoSideEffects =
            extendClassSpecifications(configuration.assumeNoSideEffects,
            createClassSpecification(classSpecificationArgs,
                                     classMembersClosure));
    }

    // Hack: support the keyword without parentheses in Groovy.
    public Object getallowaccessmodification()
    {
        allowaccessmodification();
        return null;
    }

    public void allowaccessmodification()
    {
        configuration.allowAccessModification = true;
    }

    // Hack: support the keyword without parentheses in Groovy.
    public Object getmergeinterfacesaggressively()
    {
        mergeinterfacesaggressively();
        return null;
    }

    public void mergeinterfacesaggressively()
    {
        configuration.mergeInterfacesAggressively = true;
    }

    // Hack: support the keyword without parentheses in Groovy.
    public Object getdontobfuscate()
    {
        dontobfuscate();
        return null;
    }

    public void dontobfuscate()
    {
        configuration.obfuscate = false;
    }

    // Hack: support the keyword without parentheses in Groovy.
    public Object getprintmapping()
    {
        printmapping();
        return null;
    }

    public void printmapping()
    {
        configuration.printMapping = Configuration.STD_OUT;
    }

    public void printmapping(Object printMapping)
    throws ParseException
    {
        configuration.printMapping = resolvedFile(printMapping);
    }

    public void applymapping(Object applyMapping)
    throws ParseException
    {
        configuration.applyMapping = resolvedFile(applyMapping);
    }

    public void obfuscationdictionary(Object obfuscationDictionary)
    throws ParseException
    {
        configuration.obfuscationDictionary =
            resolvedFile(obfuscationDictionary);
    }

    public void classobfuscationdictionary(Object classObfuscationDictionary)
    throws ParseException
    {
        configuration.classObfuscationDictionary =
            resolvedFile(classObfuscationDictionary);
    }

    public void packageobfuscationdictionary(Object packageObfuscationDictionary)
    throws ParseException
    {
        configuration.packageObfuscationDictionary =
            resolvedFile(packageObfuscationDictionary);
    }

    // Hack: support the keyword without parentheses in Groovy.
    public Object getoverloadaggressively()
    {
        overloadaggressively();
        return null;
    }

    public void overloadaggressively()
    {
        configuration.overloadAggressively = true;
    }

    // Hack: support the keyword without parentheses in Groovy.
    public Object getuseuniqueclassmembernames()
    {
        useuniqueclassmembernames();
        return null;
    }

    public void useuniqueclassmembernames()
    {
        configuration.useUniqueClassMemberNames = true;
    }

    // Hack: support the keyword without parentheses in Groovy.
    public Object getdontusemixedcaseclassnames()
    {
        dontusemixedcaseclassnames();
        return null;
    }

    public void dontusemixedcaseclassnames()
    {
        configuration.useMixedCaseClassNames = false;
    }

    // Hack: support the keyword without parentheses in Groovy.
    public Object getkeeppackagenames()
    {
        keeppackagenames();
        return null;
    }

    public void keeppackagenames()
    {
        keeppackagenames(null);
    }

    public void keeppackagenames(String filter)
    {
        configuration.keepPackageNames =
            extendFilter(configuration.keepPackageNames, filter, true);
    }

    // Hack: support the keyword without parentheses in Groovy.
    public Object getflattenpackagehierarchy()
    {
        flattenpackagehierarchy();
        return null;
    }

    public void flattenpackagehierarchy()
    {
        flattenpackagehierarchy("");
    }

    public void flattenpackagehierarchy(String flattenPackageHierarchy)
    {
        configuration.flattenPackageHierarchy =
            ClassUtil.internalClassName(flattenPackageHierarchy);
    }

    // Hack: support the keyword without parentheses in Groovy.
    public Object getrepackageclasses()
    {
        repackageclasses();
        return null;
    }

    public void repackageclasses()
    {
        repackageclasses("");
    }

    public void repackageclasses(String repackageClasses)
    {
        configuration.repackageClasses =
            ClassUtil.internalClassName(repackageClasses);
    }

    // Hack: support the keyword without parentheses in Groovy.
    public Object getkeepattributes()
    {
        keepattributes();
        return null;
    }

    public void keepattributes()
    {
        keepattributes(null);
    }

    public void keepattributes(String filter)
    {
        configuration.keepAttributes =
            extendFilter(configuration.keepAttributes, filter);
    }

    // Hack: support the keyword without parentheses in Groovy.
    public Object getkeepparameternames()
    {
        keepparameternames();
        return null;
    }

    public void keepparameternames()
    {
        configuration.keepParameterNames = true;
    }

    // Hack: support the keyword without parentheses in Groovy.
    public Object getrenamesourcefileattribute()
    {
        renamesourcefileattribute();
        return null;
    }

    public void renamesourcefileattribute()
    {
        renamesourcefileattribute("");
    }

    public void renamesourcefileattribute(String newSourceFileAttribute)
    {
        configuration.newSourceFileAttribute = newSourceFileAttribute;
    }

    // Hack: support the keyword without parentheses in Groovy.
    public Object getadaptclassstrings()
    {
        adaptclassstrings();
        return null;
    }

    public void adaptclassstrings()
    {
        adaptclassstrings(null);
    }

    public void adaptclassstrings(String filter)
    {
        configuration.adaptClassStrings =
            extendFilter(configuration.adaptClassStrings, filter, true);
    }

    // Hack: support the keyword without parentheses in Groovy.
    public Object getadaptresourcefilenames()
    {
        adaptresourcefilenames();
        return null;
    }

    public void adaptresourcefilenames()
    {
        adaptresourcefilenames(null);
    }

    public void adaptresourcefilenames(String filter)
    {
        configuration.adaptResourceFileNames =
            extendFilter(configuration.adaptResourceFileNames, filter);
    }

    // Hack: support the keyword without parentheses in Groovy.
    public Object getadaptresourcefilecontents()
    {
        adaptresourcefilecontents();
        return null;
    }

    public void adaptresourcefilecontents()
    {
        adaptresourcefilecontents(null);
    }

    public void adaptresourcefilecontents(String filter)
    {
        configuration.adaptResourceFileContents =
            extendFilter(configuration.adaptResourceFileContents, filter);
    }

    // Hack: support the keyword without parentheses in Groovy.
    public Object getdontpreverify()
    {
        dontpreverify();
        return null;
    }

    public void dontpreverify()
    {
        configuration.preverify = false;
    }

    // Hack: support the keyword without parentheses in Groovy.
    public Object getmicroedition()
    {
        microedition();
        return null;
    }

    public void microedition()
    {
        configuration.microEdition = true;
    }

    // Hack: support the keyword without parentheses in Groovy.
    public Object getverbose()
    {
        verbose();
        return null;
    }

    public void verbose()
    {
        configuration.verbose = true;
    }

    // Hack: support the keyword without parentheses in Groovy.
    public Object getdontnote()
    {
        dontnote();
        return null;
    }

    public void dontnote()
    {
        dontnote(null);
    }

    public void dontnote(String filter)
    {
        configuration.note = extendFilter(configuration.note, filter, true);
    }


    // Hack: support the keyword without parentheses in Groovy.
    public Object getdontwarn()
    {
        dontwarn();
        return null;
    }

    public void dontwarn()
    {
        dontwarn(null);
    }

    public void dontwarn(String filter)
    {
        configuration.warn = extendFilter(configuration.warn, filter, true);
    }


    // Hack: support the keyword without parentheses in Groovy.
    public Object getignorewarnings()
    {
        ignorewarnings();
        return null;
    }

    public void ignorewarnings()
    {
        configuration.ignoreWarnings = true;
    }

    // Hack: support the keyword without parentheses in Groovy.
    public Object getprintconfiguration()
    {
        printconfiguration();
        return null;
    }

    public void printconfiguration()
    {
        configuration.printConfiguration = Configuration.STD_OUT;
    }

    public void printconfiguration(Object printConfiguration)
    throws ParseException
    {
        configuration.printConfiguration = resolvedFile(printConfiguration);
    }

    // Hack: support the keyword without parentheses in Groovy.
    public Object getdump()
    {
        dump();
        return null;
    }

    public void dump()
    {
        configuration.dump = Configuration.STD_OUT;
    }

    public void dump(Object dump)
    throws ParseException
    {
        configuration.dump = resolvedFile(dump);
    }


    // Class member methods.

    public void field(Map memberSpecificationArgs)
    throws ParseException
    {
        if (classSpecification == null)
        {
            throw new IllegalArgumentException("The 'field' method can only be used nested inside a class specification.");
        }

        classSpecification.addField(createMemberSpecification(false,
                                                              false,
                                                              memberSpecificationArgs));
    }


    public void constructor(Map memberSpecificationArgs)
    throws ParseException
    {
        if (classSpecification == null)
        {
            throw new IllegalArgumentException("The 'constructor' method can only be used nested inside a class specification.");
        }

        classSpecification.addMethod(createMemberSpecification(true,
                                                               true,
                                                               memberSpecificationArgs));
    }


    public void method(Map memberSpecificationArgs)
    throws ParseException
    {
        if (classSpecification == null)
        {
            throw new IllegalArgumentException("The 'method' method can only be used nested inside a class specification.");
        }

        classSpecification.addMethod(createMemberSpecification(true,
                                                               false,
                                                               memberSpecificationArgs));
    }


    // Gradle task execution.

    @TaskAction
    public void proguard() throws IOException
    {
        new ProGuard(configuration).execute();
    }


    // Small utility methods.

    /**
     * Returns a file that is properly resolved with respect to the project
     * directory.
     */
    private File resolvedFile(Object file)
    throws ParseException
    {
        return getProject().file(resolvedString(file));
    }


    /**
     * Returns a file collection that is properly resolved with respect to the
     * project directory.
     */
    private ConfigurableFileCollection resolvedFiles(Object files)
    throws ParseException
    {
        return getProject().files(new Object[] { resolvedString(files) });
    }


    /**
     * Returns the given Object, with resolved properties if it is a String.
     */
    private Object resolvedString(Object object)
    throws ParseException
    {
        if (object instanceof String)
        {
            try
            {
                String fileName = (String)object;
                return
                    new ConfigurationParser(fileName,
                                            "Gradle setting",
                                            null,
                                            System.getProperties())
                        .replaceSystemProperties(fileName);
            }
            catch (IOException e)
            {
                throw new ParseException(e.getMessage());
            }
        }

        return object;
    }


    private ClassPath extendClassPath(ClassPath                  classPath,
                                      ConfigurableFileCollection fileCollection,
                                      Map                        filterArgs,
                                      boolean                    output)
    {
        if (classPath == null)
        {
            classPath = new ClassPath();
        }

        Iterator files = fileCollection.iterator();
        while (files.hasNext())
        {
            File file = (File)files.next();

            // Create the class path entry.
            ClassPathEntry classPathEntry = new ClassPathEntry(file, output);

            // Add any filters to the class path entry.
            if (filterArgs != null)
            {
                classPathEntry.setFilter(ListUtil.commaSeparatedList((String)filterArgs.get("filter")));
                classPathEntry.setJarFilter(ListUtil.commaSeparatedList((String)filterArgs.get("jarfilter")));
                classPathEntry.setWarFilter(ListUtil.commaSeparatedList((String)filterArgs.get("warfilter")));
                classPathEntry.setEarFilter(ListUtil.commaSeparatedList((String)filterArgs.get("earfilter")));
                classPathEntry.setZipFilter(ListUtil.commaSeparatedList((String)filterArgs.get("zipfilter")));
            }

            classPath.add(classPathEntry);
        }

        return classPath;
    }


    private KeepClassSpecification createKeepClassSpecification(boolean allowShrinking,
                                                                boolean markClasses,
                                                                boolean markConditionally,
                                                                Map     keepArgs,
                                                                String  classSpecificationString)
    throws ParseException
    {
        ClassSpecification classSpecification =
            createClassSpecification(classSpecificationString);

        return
            createKeepClassSpecification(allowShrinking,
                                         markClasses,
                                         markConditionally,
                                         keepArgs,
                                         classSpecification);
    }


    private KeepClassSpecification createKeepClassSpecification(boolean allowShrinking,
                                                                boolean markClasses,
                                                                boolean markConditionally,
                                                                Map     classSpecificationArgs,
                                                                Closure classMembersClosure)
    throws ParseException
    {
        ClassSpecification classSpecification =
            createClassSpecification(classSpecificationArgs,
                                     classMembersClosure);
        return
            createKeepClassSpecification(allowShrinking,
                                         markClasses,
                                         markConditionally,
                                         classSpecificationArgs,
                                         classSpecification);
    }


    private KeepClassSpecification createKeepClassSpecification(boolean            allowShrinking,
                                                                boolean            markClasses,
                                                                boolean            markConditionally,
                                                                Map                keepArgs,
                                                                ClassSpecification classSpecification)
    {
        return
            new KeepClassSpecification(markClasses,
                                       markConditionally,
                                       retrieveBoolean(keepArgs, "allowshrinking",    allowShrinking),
                                       retrieveBoolean(keepArgs, "allowoptimization", false),
                                       retrieveBoolean(keepArgs, "allowobfuscation",  false),
                                       classSpecification);
    }


    private ClassSpecification createClassSpecification(String classSpecificationString)
    throws ParseException
    {
        try
        {
            ConfigurationParser parser =
                new ConfigurationParser(new String[] { classSpecificationString }, null);

            try
            {
                return parser.parseClassSpecificationArguments();
            }
            finally
            {
                parser.close();
            }
        }
        catch (IOException e)
        {
            throw new ParseException(e.getMessage());
        }
    }


    private ClassSpecification createClassSpecification(Map     classSpecificationArgs,
                                                        Closure classMembersClosure)
    throws ParseException
    {
        // Extract the arguments.
        String access            = (String)classSpecificationArgs.get("access");
        String annotation        = (String)classSpecificationArgs.get("annotation");
        String type              = (String)classSpecificationArgs.get("type");
        String name              = (String)classSpecificationArgs.get("name");
        String extendsAnnotation = (String)classSpecificationArgs.get("extendsannotation");
        String extends_          = (String)classSpecificationArgs.get("extends");
        if (extends_ == null)
        {
            extends_             = (String)classSpecificationArgs.get("implements");
        }

        // Create the class specification.
        ClassSpecification classSpecification =
            new ClassSpecification(null,
                                   requiredClassAccessFlags(true, access, type),
                                   requiredClassAccessFlags(false, access, type),
                                   annotation        != null ? ClassUtil.internalType(annotation)        : null,
                                   name              != null ? ClassUtil.internalClassName(name)         : null,
                                   extendsAnnotation != null ? ClassUtil.internalType(extendsAnnotation) : null,
                                   extends_          != null ? ClassUtil.internalClassName(extends_)     : null);

        // Initialize the class specification with its closure.
        if (classMembersClosure != null)
        {
            // Temporarily remember the class specification, so we can add
            // class member specifications.
            this.classSpecification = classSpecification;
            classMembersClosure.call(classSpecification);
            this.classSpecification = null;
        }

        return classSpecification;
    }


    private int requiredClassAccessFlags(boolean set,
                                         String  access,
                                         String  type)
    throws ParseException
    {
        int accessFlags = 0;

        if (access != null)
        {
            StringTokenizer tokenizer = new StringTokenizer(access, " ,");
            while (tokenizer.hasMoreTokens())
            {
                String token = tokenizer.nextToken();

                if (token.startsWith("!") ^ set)
                {
                    String strippedToken = token.startsWith("!") ?
                        token.substring(1) :
                        token;

                    int accessFlag =
                        strippedToken.equals(ClassConstants.EXTERNAL_ACC_PUBLIC)     ? ClassConstants.INTERNAL_ACC_PUBLIC      :
                        strippedToken.equals(ClassConstants.EXTERNAL_ACC_FINAL)      ? ClassConstants.INTERNAL_ACC_FINAL       :
                        strippedToken.equals(ClassConstants.EXTERNAL_ACC_ABSTRACT)   ? ClassConstants.INTERNAL_ACC_ABSTRACT    :
                        strippedToken.equals(ClassConstants.EXTERNAL_ACC_SYNTHETIC)  ? ClassConstants.INTERNAL_ACC_SYNTHETIC   :
                        strippedToken.equals(ClassConstants.EXTERNAL_ACC_ANNOTATION) ? ClassConstants.INTERNAL_ACC_ANNOTATTION :
                                                                             0;

                    if (accessFlag == 0)
                    {
                        throw new ParseException("Incorrect class access modifier ["+strippedToken+"]");
                    }

                    accessFlags |= accessFlag;
                }
            }
        }

        if (type != null && (type.startsWith("!") ^ set))
        {
            int accessFlag =
                type.equals("class")                           ? 0                            :
                type.equals(      ClassConstants.EXTERNAL_ACC_INTERFACE) ||
                type.equals("!" + ClassConstants.EXTERNAL_ACC_INTERFACE) ? ClassConstants.INTERNAL_ACC_INTERFACE :
                type.equals(      ClassConstants.EXTERNAL_ACC_ENUM)      ||
                type.equals("!" + ClassConstants.EXTERNAL_ACC_ENUM)      ? ClassConstants.INTERNAL_ACC_ENUM      :
                                                                 -1;
            if (accessFlag == -1)
            {
                throw new ParseException("Incorrect class type ["+type+"]");
            }

            accessFlags |= accessFlag;
        }

        return accessFlags;
    }


    private MemberSpecification createMemberSpecification(boolean isMethod,
                                                          boolean isConstructor,
                                                          Map     classSpecificationArgs)
    throws ParseException
    {
        // Extract the arguments.
        String access            = (String)classSpecificationArgs.get("access");
        String type              = (String)classSpecificationArgs.get("type");
        String annotation        = (String)classSpecificationArgs.get("annotation");
        String name              = (String)classSpecificationArgs.get("name");
        String parameters        = (String)classSpecificationArgs.get("parameters");

        // Perform some basic conversions and checks on the attributes.
        if (annotation != null)
        {
            annotation = ClassUtil.internalType(annotation);
        }

        if (isMethod)
        {
            if (isConstructor)
            {
                if (type != null)
                {
                    throw new ParseException("Type attribute not allowed in constructor specification ["+type+"]");
                }

                if (parameters != null)
                {
                    type = ClassConstants.EXTERNAL_TYPE_VOID;
                }

                name = ClassConstants.INTERNAL_METHOD_NAME_INIT;
            }
            else if ((type != null) ^ (parameters != null))
            {
                throw new ParseException("Type and parameters attributes must always be present in combination in method specification");
            }
        }
        else
        {
            if (parameters != null)
            {
                throw new ParseException("Parameters attribute not allowed in field specification ["+parameters+"]");
            }
        }

        List parameterList = ListUtil.commaSeparatedList(parameters);

        String descriptor =
            parameters != null ? ClassUtil.internalMethodDescriptor(type, parameterList) :
            type       != null ? ClassUtil.internalType(type)                            :
                                 null;

        return new MemberSpecification(requiredMemberAccessFlags(true,  access),
                                       requiredMemberAccessFlags(false, access),
                                       annotation,
                                       name,
                                       descriptor);
    }


    private int requiredMemberAccessFlags(boolean set,
                                          String  access)
    throws ParseException
    {
        int accessFlags = 0;

        if (access != null)
        {
            StringTokenizer tokenizer = new StringTokenizer(access, " ,");
            while (tokenizer.hasMoreTokens())
            {
                String token = tokenizer.nextToken();

                if (token.startsWith("!") ^ set)
                {
                    String strippedToken = token.startsWith("!") ?
                        token.substring(1) :
                        token;

                    int accessFlag =
                        strippedToken.equals(ClassConstants.EXTERNAL_ACC_PUBLIC)       ? ClassConstants.INTERNAL_ACC_PUBLIC       :
                        strippedToken.equals(ClassConstants.EXTERNAL_ACC_PRIVATE)      ? ClassConstants.INTERNAL_ACC_PRIVATE      :
                        strippedToken.equals(ClassConstants.EXTERNAL_ACC_PROTECTED)    ? ClassConstants.INTERNAL_ACC_PROTECTED    :
                        strippedToken.equals(ClassConstants.EXTERNAL_ACC_STATIC)       ? ClassConstants.INTERNAL_ACC_STATIC       :
                        strippedToken.equals(ClassConstants.EXTERNAL_ACC_FINAL)        ? ClassConstants.INTERNAL_ACC_FINAL        :
                        strippedToken.equals(ClassConstants.EXTERNAL_ACC_SYNCHRONIZED) ? ClassConstants.INTERNAL_ACC_SYNCHRONIZED :
                        strippedToken.equals(ClassConstants.EXTERNAL_ACC_VOLATILE)     ? ClassConstants.INTERNAL_ACC_VOLATILE     :
                        strippedToken.equals(ClassConstants.EXTERNAL_ACC_TRANSIENT)    ? ClassConstants.INTERNAL_ACC_TRANSIENT    :
                        strippedToken.equals(ClassConstants.EXTERNAL_ACC_BRIDGE)       ? ClassConstants.INTERNAL_ACC_BRIDGE       :
                        strippedToken.equals(ClassConstants.EXTERNAL_ACC_VARARGS)      ? ClassConstants.INTERNAL_ACC_VARARGS      :
                        strippedToken.equals(ClassConstants.EXTERNAL_ACC_NATIVE)       ? ClassConstants.INTERNAL_ACC_NATIVE       :
                        strippedToken.equals(ClassConstants.EXTERNAL_ACC_ABSTRACT)     ? ClassConstants.INTERNAL_ACC_ABSTRACT     :
                        strippedToken.equals(ClassConstants.EXTERNAL_ACC_STRICT)       ? ClassConstants.INTERNAL_ACC_STRICT       :
                        strippedToken.equals(ClassConstants.EXTERNAL_ACC_SYNTHETIC)    ? ClassConstants.INTERNAL_ACC_SYNTHETIC    :
                                                                               0;

                    if (accessFlag == 0)
                    {
                        throw new ParseException("Incorrect class member access modifier ["+strippedToken+"]");
                    }

                    accessFlags |= accessFlag;
                }
            }
        }

        return accessFlags;
    }


    private boolean retrieveBoolean(Map args, String name, boolean defaultValue)
    {
        if (args == null)
        {
            return defaultValue;
        }

        Object arg = args.get(name);

        return arg == null ? defaultValue : ((Boolean)arg).booleanValue();
    }


    private List extendClassSpecifications(List               classSpecifications,
                                           ClassSpecification classSpecification)
    {
        if (classSpecifications == null)
        {
            classSpecifications = new ArrayList();
        }

        classSpecifications.add(classSpecification);

        return classSpecifications;
    }


    private List extendClassSpecifications(List classSpecifications,
                                           List additionalClassSpecifications)
    {
        if (additionalClassSpecifications != null)
        {
            if (classSpecifications == null)
            {
                classSpecifications = new ArrayList();
            }

            classSpecifications.addAll(additionalClassSpecifications);
        }

        return classSpecifications;
    }


    private List extendFilter(List   filter,
                              String filterString)
    {
        return extendFilter(filter, filterString, false);
    }


    private List extendFilter(List    filter,
                              String  filterString,
                              boolean internal)
    {
        if (filter == null)
        {
            filter = new ArrayList();
        }

        if (filterString == null)
        {
            // Clear the filter to keep all names.
            filter.clear();
        }
        else
        {
            if (internal)
            {
                filterString = ClassUtil.internalClassName(filterString);
            }

            // Append the filter.
            filter.addAll(ListUtil.commaSeparatedList(filterString));
        }

        return filter;
    }
}
