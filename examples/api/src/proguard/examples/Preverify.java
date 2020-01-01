package proguard.examples;

import proguard.classfile.*;
import proguard.classfile.attribute.visitor.AllAttributeVisitor;
import proguard.classfile.util.*;
import proguard.classfile.visitor.*;
import proguard.io.*;
import proguard.preverify.CodePreverifier;

import java.io.*;

/**
 * This sample application illustrates how to preverify classes with the
 * ProGuard API.
 *
 * Usage:
 *     java proguard.examples.Preverify input.jar output.jar
 */
public class Preverify
{
    public static void main(String[] args)
    {
        String inputJarFileName  = args[0];
        String outputJarFileName = args[1];

        try
        {
            // Read the program classes and library classes.
            // The latter are necessary to reconstruct the class hierarchy,
            // which is necessary to properly preverify the code.
            // We're only reading the base jmod here. General code may need
            // additional jmod files.
            String runtimeFileName = System.getProperty("java.home") + "/jmods/java.base.jmod";

            ClassPool libraryClassPool = readJar(runtimeFileName, true);
            ClassPool programClassPool = readJar(inputJarFileName, false);

            // Initialize all cross-references.
            initialize(programClassPool, libraryClassPool);

            // Preverify the program classes.
            preverify(programClassPool);

            // Write the preverified classes to the jar file.
            writeJar(programClassPool, outputJarFileName);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    /**
     * Reads the classes from the specified jar file and returns them as a class
     * pool.
     *
     * @param jarFileName the name of the jar file or jmod file.
     * @param isLibrary   specifies whether classes should be represented as
     *                    ProgramClass instances (for processing) or
     *                    LibraryClass instances (more compact).
     * @return a new class pool with the read classes.
     */
    private static ClassPool readJar(String  jarFileName,
                                     boolean isLibrary)
    throws IOException
    {
        ClassPool classPool = new ClassPool();

        // Parse all classes from the input jar and
        // collect them in the class pool.
        DirectoryPump directoryPump =
            new DirectoryPump(
            new File(jarFileName));

        directoryPump.pumpDataEntries(
            new JarReader(isLibrary,
            new ClassFilter(
            new ClassReader(isLibrary, false, false, false, null,
            new ClassPoolFiller(classPool)))));

        return classPool;
    }


    /**
     * Initializes the cached cross-references of the classes in the given
     * class pools.
     * @param programClassPool the program class pool, typically with processed
     *                         classes.
     * @param libraryClassPool the library class pool, typically with run-time
     *                         classes.
     */
    private static void initialize(ClassPool programClassPool,
                                   ClassPool libraryClassPool)
    {
        // We may get some warnings about missing dependencies.
        // They're a pain, but for proper results, we really need to have
        // all dependencies.
        PrintWriter    printWriter    = new PrintWriter(System.err);
        WarningPrinter warningPrinter = new WarningPrinter(printWriter);

        // Initialize the class hierarchies.
        libraryClassPool.classesAccept(
            new ClassSuperHierarchyInitializer(programClassPool,
                                               libraryClassPool,
                                               null,
                                               null));

        programClassPool.classesAccept(
            new ClassSuperHierarchyInitializer(programClassPool,
                                               libraryClassPool,
                                               warningPrinter,
                                               warningPrinter));

        // Initialize the other references from the program classes.
        programClassPool.classesAccept(
            new ClassReferenceInitializer(programClassPool,
                                          libraryClassPool,
                                          warningPrinter,
                                          warningPrinter,
                                          warningPrinter,
                                          null));

        // Flush the warnings.
        printWriter.flush();
    }


    /**
     * Preverifies the code of the classes in the given class pool,
     * adding StackMapTable attributes to code that requires it.
     * @param programClassPool the classes to be preverified.
     */
    private static void preverify(ClassPool programClassPool)
    {
        programClassPool.classesAccept(
            new ClassVersionFilter(VersionConstants.CLASS_VERSION_1_6,
            new AllMethodVisitor(
            new AllAttributeVisitor(
            new CodePreverifier(false)))));
    }


    /**
     * Writes the classes from the given class pool to a specified jar.
     * @param programClassPool  the classes to write.
     * @param outputJarFileName the name of the output jar file.
     */
    private static void writeJar(ClassPool programClassPool,
                                 String    outputJarFileName)
    throws IOException
    {
        JarWriter jarWriter =
            new JarWriter(
            new ZipWriter(
            new FixedFileWriter(
            new File(outputJarFileName))));

        programClassPool.classesAccept(
            new DataEntryClassWriter(jarWriter));

        jarWriter.close();
    }
}
