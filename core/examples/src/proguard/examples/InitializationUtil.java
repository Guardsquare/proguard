package proguard.examples;

import proguard.classfile.*;
import proguard.classfile.attribute.visitor.AllAttributeVisitor;
import proguard.classfile.util.*;
import proguard.classfile.visitor.*;
import proguard.io.*;
import proguard.preverify.CodePreverifier;

import java.io.*;

/**
 * This utility class provides a method to initialize the cached
 * cross-references classes. They are necessary to traverse the class
 * hierarchy efficiently, for example when preverifying code or
 * performing more general partial evaluation.
 */
public class InitializationUtil
{
    /**
     * Initializes the cached cross-references of the classes in the given
     * class pools.
     * @param programClassPool the program class pool, typically with processed
     *                         classes.
     * @param libraryClassPool the library class pool, typically with run-time
     *                         classes.
     */
    public static void initialize(ClassPool programClassPool,
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
}
