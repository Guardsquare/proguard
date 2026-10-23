/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2026 Guardsquare NV
 */

package proguard

import io.kotest.core.spec.style.FreeSpec
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import proguard.io.DataEntry
import proguard.io.DataEntryReader
import java.nio.file.Files
import java.nio.file.Path

class InputReaderTest : FreeSpec({
    // Simply use the JAVA_HOME of the currently running JDK; test setup below should therefore only
    // reference resources which are present in all JDK versions used to run this test
    val javaHome = Path.of(System.getProperty("java.home"))
    val javaHomeJmods = javaHome.resolve("jmods")
    val hasJdkJmods = Files.isDirectory(javaHomeJmods)

    class ExpectingDataEntryReader(
        private val expectJrt: Boolean,
        expectedEntries: Set<String>,
        private val unexpectedEntries: Set<String>
    ) : DataEntryReader {
        private val expectedEntries: MutableSet<String> = LinkedHashSet(expectedEntries)

        override fun read(dataEntry: DataEntry) {
            if (expectJrt) {
                dataEntry.toString() shouldContain("jrt:/")
            } else {
                dataEntry.toString() shouldNotContain("jrt:/")
            }

            val entry = dataEntry.name
            expectedEntries.remove(entry)
            unexpectedEntries shouldNotContain entry
        }

        fun assertFoundAll() {
            expectedEntries.shouldBeEmpty()
        }
    }

    fun inputReader() = InputReader(Configuration())

    // Only run if JDK actually has jmod files
    "Given a JDK with jmods".config(enabled = hasJdkJmods) - {
        /** Entry for `java.lang.String` in the `java.base` module  */
        val javaBaseEntryString = "java/lang/String.class"

        /** Entry for `java.lang.Number` in the `java.base` module  */
        val javaBaseEntryNumber = "java/lang/Number.class"

        /** Entry in the `java.compiler` module  */
        val javaCompilerEntry = "javax/lang/model/SourceVersion.class"

        "It should read all jmod files" {
            val classPathEntry = ClassPathEntry(
                javaHomeJmods.toFile(), false)
            val entryReader = ExpectingDataEntryReader(false,
                setOf(javaBaseEntryString, javaBaseEntryNumber, javaCompilerEntry),
                emptySet())
            inputReader().readInput("message", classPathEntry, entryReader)
            entryReader.assertFoundAll()
        }

        "It should read a specific jmod file" {
            val classPathEntry = ClassPathEntry(
                javaHomeJmods.resolve("java.base.jmod").toFile(), false)
            val entryReader = ExpectingDataEntryReader(false,
                setOf(javaBaseEntryString, javaBaseEntryNumber),
                setOf(javaCompilerEntry))
            inputReader().readInput("message", classPathEntry, entryReader)
            entryReader.assertFoundAll()
        }

        "It should read a specific jmod file with filter" {
            val classPathEntry = ClassPathEntry(
                javaHomeJmods.resolve("java.base.jmod").toFile(), false)
            classPathEntry.filter = listOf("java/lang/String.class")
            val entryReader = ExpectingDataEntryReader(false,
                setOf(javaBaseEntryString),
                setOf(javaCompilerEntry, javaBaseEntryNumber))
            inputReader().readInput("message", classPathEntry, entryReader)
            entryReader.assertFoundAll()
        }
    }

    "Given a JDK without jmods" - {
        fun createHardLink(sourceDir: Path, destDir: Path, pathPieces: List<String>) {
            val source = pathPieces.fold(sourceDir, Path::resolve)
            val dest = pathPieces.fold(destDir, Path::resolve)

            // Only support linking files, not directories; otherwise could accidentally corrupt JDK when
            // following links during cleanup
            require(Files.isRegularFile(source))

            Files.createDirectories(dest.parent)
            // Some of the copied files are > 100 MB; for better performance just create a hardlink instead
            // of copying file contents
            Files.createLink(dest, source)
        }

        // Create a dummy JAVA_HOME without jmods
        // Note: Most likely this tempdir will not be fully cleaned up (at least on Windows) because internally
        // the JDK creates an additional URLClassLoader in JrtFileSystemProvider#newJrtFsLoader and does not close it
        // when the FileSystem is closed; at least the tempdir only contains hardlinks and only takes up little space
        val customJavaHome = tempdir().toPath()
        // Copy only the files needed to make it look like a JDK without jmod files
        createHardLink(javaHome, customJavaHome, listOf("lib", "jrt-fs.jar"))
        createHardLink(javaHome, customJavaHome, listOf("lib", "modules"))

        /** Entry for `java.lang.String` in the `java.base` module  */
        val javaBaseEntryString = "java/lang/String.class"

        /** Entry for `java.lang.Number` in the `java.base` module  */
        val javaBaseEntryNumber = "java/lang/Number.class"

        /** Entry in the `java.compiler` module  */
        val javaCompilerEntry = "javax/lang/model/SourceVersion.class"

        "It should read all modules" {
            val classPathEntry = ClassPathEntry(
                customJavaHome.resolve("jmods").toFile(), false)
            val entryReader = ExpectingDataEntryReader(true,
                setOf(javaBaseEntryString, javaBaseEntryNumber, javaCompilerEntry),
                emptySet())
            inputReader().readInput("message", classPathEntry, entryReader)
            entryReader.assertFoundAll()
        }

        "It should read a specific module" {
            val classPathEntry = ClassPathEntry(
                customJavaHome.resolve("jmods").resolve("java.base.jmod").toFile(), false)
            val entryReader = ExpectingDataEntryReader(true,
                setOf(javaBaseEntryString, javaBaseEntryNumber),
                setOf(javaCompilerEntry))
            inputReader().readInput("message", classPathEntry, entryReader)
            entryReader.assertFoundAll()
        }

        "It should read a specific module with filter" {
            val classPathEntry = ClassPathEntry(
                customJavaHome.resolve("jmods").resolve("java.base.jmod").toFile(), false)
            classPathEntry.filter = listOf("java/lang/String.class")
            val entryReader = ExpectingDataEntryReader(true,
                setOf(javaBaseEntryString),
                setOf(javaCompilerEntry, javaBaseEntryNumber))
            inputReader().readInput("message", classPathEntry, entryReader)
            entryReader.assertFoundAll()
        }
    }
})
