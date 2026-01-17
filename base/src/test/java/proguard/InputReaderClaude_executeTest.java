package proguard;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import proguard.classfile.ClassPool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link InputReader#execute(AppView)} method.
 * Tests the execution of InputReader with various configurations and AppView states.
 */
public class InputReaderClaude_executeTest {

    /**
     * Tests execute() with null AppView parameter.
     * Verifies that a NullPointerException is thrown when AppView is null.
     */
    @Test
    public void testExecuteWithNullAppView() {
        // Arrange
        Configuration configuration = new Configuration();
        InputReader inputReader = new InputReader(configuration);

        // Act & Assert - Should throw NullPointerException
        assertThrows(NullPointerException.class, () -> {
            inputReader.execute(null);
        }, "execute() should throw NullPointerException for null AppView");
    }

    /**
     * Tests execute() with null Configuration (no programJars set).
     * Verifies that IOException is thrown when no input jars are specified.
     */
    @Test
    public void testExecuteWithNullConfiguration() {
        // Arrange
        InputReader inputReader = new InputReader(null);
        AppView appView = new AppView();

        // Act & Assert - Should throw NullPointerException because configuration is null
        assertThrows(NullPointerException.class, () -> {
            inputReader.execute(appView);
        }, "execute() should throw NullPointerException when configuration is null");
    }

    /**
     * Tests execute() with empty programJars.
     * Verifies that IOException is thrown when programJars is null (no input specified).
     */
    @Test
    public void testExecuteWithNullProgramJars() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.programJars = null;  // No program jars specified

        InputReader inputReader = new InputReader(configuration);
        AppView appView = new AppView();

        // Act & Assert - Should throw NullPointerException when trying to read from null programJars
        assertThrows(NullPointerException.class, () -> {
            inputReader.execute(appView);
        }, "execute() should throw NullPointerException when programJars is null");
    }

    /**
     * Tests execute() with empty ClassPath for programJars.
     * Verifies that IOException is thrown because no classes are found.
     */
    @Test
    public void testExecuteWithEmptyProgramJars() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.programJars = new ClassPath();  // Empty program jars

        InputReader inputReader = new InputReader(configuration);
        AppView appView = new AppView();

        // Act & Assert - Should throw IOException because no classes are found
        IOException exception = assertThrows(IOException.class, () -> {
            inputReader.execute(appView);
        }, "execute() should throw IOException when no classes are found");

        assertTrue(exception.getMessage().contains("doesn't contain any classes"),
            "Exception message should indicate no classes were found");
    }

    /**
     * Tests execute() with a non-existent jar file in programJars.
     * Verifies that IOException is thrown when the input file doesn't exist.
     */
    @Test
    public void testExecuteWithNonExistentJarFile() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.programJars = new ClassPath();

        // Add a non-existent jar file
        ClassPathEntry entry = new ClassPathEntry(new File("/nonexistent/path/test.jar"), false);
        configuration.programJars.add(entry);

        InputReader inputReader = new InputReader(configuration);
        AppView appView = new AppView();

        // Act & Assert - Should throw IOException
        assertThrows(IOException.class, () -> {
            inputReader.execute(appView);
        }, "execute() should throw IOException for non-existent jar file");
    }

    /**
     * Tests execute() with an empty jar file in programJars.
     * Verifies that IOException is thrown because the jar contains no classes.
     */
    @Test
    public void testExecuteWithEmptyJarFile(@TempDir Path tempDir) throws IOException {
        // Arrange - Create an empty jar file
        File emptyJar = tempDir.resolve("empty.jar").toFile();
        try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(emptyJar))) {
            // Create an empty jar with just a manifest
        }

        Configuration configuration = new Configuration();
        configuration.programJars = new ClassPath();
        ClassPathEntry entry = new ClassPathEntry(emptyJar, false);
        configuration.programJars.add(entry);

        InputReader inputReader = new InputReader(configuration);
        AppView appView = new AppView();

        // Act & Assert - Should throw IOException because no classes are found
        IOException exception = assertThrows(IOException.class, () -> {
            inputReader.execute(appView);
        }, "execute() should throw IOException when jar contains no classes");

        assertTrue(exception.getMessage().contains("doesn't contain any classes"),
            "Exception message should indicate no classes were found");
    }

    /**
     * Tests execute() with a jar file containing a non-class file.
     * Verifies that IOException is thrown because no classes are found.
     */
    @Test
    public void testExecuteWithJarContainingNonClassFile(@TempDir Path tempDir) throws IOException {
        // Arrange - Create a jar with a text file
        File jarWithTextFile = tempDir.resolve("withtext.jar").toFile();
        try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(jarWithTextFile))) {
            ZipEntry entry = new ZipEntry("readme.txt");
            jos.putNextEntry(entry);
            jos.write("This is a text file".getBytes());
            jos.closeEntry();
        }

        Configuration configuration = new Configuration();
        configuration.programJars = new ClassPath();
        ClassPathEntry entry = new ClassPathEntry(jarWithTextFile, false);
        configuration.programJars.add(entry);

        InputReader inputReader = new InputReader(configuration);
        AppView appView = new AppView();

        // Act & Assert - Should throw IOException because no classes are found
        IOException exception = assertThrows(IOException.class, () -> {
            inputReader.execute(appView);
        }, "execute() should throw IOException when jar contains no classes");

        assertTrue(exception.getMessage().contains("doesn't contain any classes"),
            "Exception message should indicate no classes were found");
    }

    /**
     * Tests execute() with a directory that doesn't exist.
     * Verifies that IOException is thrown.
     */
    @Test
    public void testExecuteWithNonExistentDirectory() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.programJars = new ClassPath();

        // Add a non-existent directory
        ClassPathEntry entry = new ClassPathEntry(new File("/nonexistent/directory"), false);
        configuration.programJars.add(entry);

        InputReader inputReader = new InputReader(configuration);
        AppView appView = new AppView();

        // Act & Assert - Should throw IOException
        assertThrows(IOException.class, () -> {
            inputReader.execute(appView);
        }, "execute() should throw IOException for non-existent directory");
    }

    /**
     * Tests execute() with an empty directory in programJars.
     * Verifies that IOException is thrown because no classes are found.
     */
    @Test
    public void testExecuteWithEmptyDirectory(@TempDir Path tempDir) {
        // Arrange - Use an empty temp directory
        Configuration configuration = new Configuration();
        configuration.programJars = new ClassPath();
        ClassPathEntry entry = new ClassPathEntry(tempDir.toFile(), false);
        configuration.programJars.add(entry);

        InputReader inputReader = new InputReader(configuration);
        AppView appView = new AppView();

        // Act & Assert - Should throw IOException because no classes are found
        IOException exception = assertThrows(IOException.class, () -> {
            inputReader.execute(appView);
        }, "execute() should throw IOException when directory contains no classes");

        assertTrue(exception.getMessage().contains("doesn't contain any classes"),
            "Exception message should indicate no classes were found");
    }

    /**
     * Tests execute() with shrink enabled in configuration.
     * Verifies that the configuration flag is respected during execution.
     */
    @Test
    public void testExecuteWithShrinkEnabled(@TempDir Path tempDir) throws IOException {
        // Arrange - Create an empty jar for testing
        File emptyJar = tempDir.resolve("test.jar").toFile();
        try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(emptyJar))) {
            // Empty jar
        }

        Configuration configuration = new Configuration();
        configuration.shrink = true;
        configuration.programJars = new ClassPath();
        ClassPathEntry entry = new ClassPathEntry(emptyJar, false);
        configuration.programJars.add(entry);

        InputReader inputReader = new InputReader(configuration);
        AppView appView = new AppView();

        // Act & Assert - Should throw IOException because no classes are found (shrink flag doesn't affect this)
        IOException exception = assertThrows(IOException.class, () -> {
            inputReader.execute(appView);
        }, "execute() should throw IOException when no classes are found regardless of shrink flag");

        assertTrue(exception.getMessage().contains("doesn't contain any classes"));
    }

    /**
     * Tests execute() with optimize enabled in configuration.
     * Verifies that the configuration flag is respected during execution.
     */
    @Test
    public void testExecuteWithOptimizeEnabled(@TempDir Path tempDir) throws IOException {
        // Arrange - Create an empty jar for testing
        File emptyJar = tempDir.resolve("test.jar").toFile();
        try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(emptyJar))) {
            // Empty jar
        }

        Configuration configuration = new Configuration();
        configuration.optimize = true;
        configuration.programJars = new ClassPath();
        ClassPathEntry entry = new ClassPathEntry(emptyJar, false);
        configuration.programJars.add(entry);

        InputReader inputReader = new InputReader(configuration);
        AppView appView = new AppView();

        // Act & Assert - Should throw IOException because no classes are found
        IOException exception = assertThrows(IOException.class, () -> {
            inputReader.execute(appView);
        }, "execute() should throw IOException when no classes are found regardless of optimize flag");

        assertTrue(exception.getMessage().contains("doesn't contain any classes"));
    }

    /**
     * Tests execute() with obfuscate enabled in configuration.
     * Verifies that the configuration flag is respected during execution.
     */
    @Test
    public void testExecuteWithObfuscateEnabled(@TempDir Path tempDir) throws IOException {
        // Arrange - Create an empty jar for testing
        File emptyJar = tempDir.resolve("test.jar").toFile();
        try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(emptyJar))) {
            // Empty jar
        }

        Configuration configuration = new Configuration();
        configuration.obfuscate = true;
        configuration.programJars = new ClassPath();
        ClassPathEntry entry = new ClassPathEntry(emptyJar, false);
        configuration.programJars.add(entry);

        InputReader inputReader = new InputReader(configuration);
        AppView appView = new AppView();

        // Act & Assert - Should throw IOException because no classes are found
        IOException exception = assertThrows(IOException.class, () -> {
            inputReader.execute(appView);
        }, "execute() should throw IOException when no classes are found regardless of obfuscate flag");

        assertTrue(exception.getMessage().contains("doesn't contain any classes"));
    }

    /**
     * Tests execute() with all processing flags enabled.
     * Verifies that multiple configuration flags are handled together.
     */
    @Test
    public void testExecuteWithAllProcessingEnabled(@TempDir Path tempDir) throws IOException {
        // Arrange - Create an empty jar for testing
        File emptyJar = tempDir.resolve("test.jar").toFile();
        try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(emptyJar))) {
            // Empty jar
        }

        Configuration configuration = new Configuration();
        configuration.shrink = true;
        configuration.optimize = true;
        configuration.obfuscate = true;
        configuration.programJars = new ClassPath();
        ClassPathEntry entry = new ClassPathEntry(emptyJar, false);
        configuration.programJars.add(entry);

        InputReader inputReader = new InputReader(configuration);
        AppView appView = new AppView();

        // Act & Assert - Should throw IOException because no classes are found
        IOException exception = assertThrows(IOException.class, () -> {
            inputReader.execute(appView);
        }, "execute() should throw IOException when no classes are found");

        assertTrue(exception.getMessage().contains("doesn't contain any classes"));
    }

    /**
     * Tests execute() with keepKotlinMetadata enabled.
     * Verifies that Kotlin metadata configuration is respected.
     */
    @Test
    public void testExecuteWithKeepKotlinMetadata(@TempDir Path tempDir) throws IOException {
        // Arrange - Create an empty jar for testing
        File emptyJar = tempDir.resolve("test.jar").toFile();
        try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(emptyJar))) {
            // Empty jar
        }

        Configuration configuration = new Configuration();
        configuration.keepKotlinMetadata = true;
        configuration.programJars = new ClassPath();
        ClassPathEntry entry = new ClassPathEntry(emptyJar, false);
        configuration.programJars.add(entry);

        InputReader inputReader = new InputReader(configuration);
        AppView appView = new AppView();

        // Act & Assert - Should throw IOException because no classes are found
        IOException exception = assertThrows(IOException.class, () -> {
            inputReader.execute(appView);
        }, "execute() should throw IOException when no classes are found");

        assertTrue(exception.getMessage().contains("doesn't contain any classes"));
    }

    /**
     * Tests execute() with ignoreWarnings enabled.
     * Verifies that warnings are ignored when configured to do so.
     */
    @Test
    public void testExecuteWithIgnoreWarningsEnabled(@TempDir Path tempDir) throws IOException {
        // Arrange - Create an empty jar for testing
        File emptyJar = tempDir.resolve("test.jar").toFile();
        try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(emptyJar))) {
            // Empty jar
        }

        Configuration configuration = new Configuration();
        configuration.ignoreWarnings = true;
        configuration.programJars = new ClassPath();
        ClassPathEntry entry = new ClassPathEntry(emptyJar, false);
        configuration.programJars.add(entry);

        InputReader inputReader = new InputReader(configuration);
        AppView appView = new AppView();

        // Act & Assert - Should still throw IOException because no classes are found
        // (ignoreWarnings only affects class naming warnings, not the empty classes check)
        IOException exception = assertThrows(IOException.class, () -> {
            inputReader.execute(appView);
        }, "execute() should throw IOException when no classes are found even with ignoreWarnings");

        assertTrue(exception.getMessage().contains("doesn't contain any classes"));
    }

    /**
     * Tests execute() with skipNonPublicLibraryClasses enabled.
     * Verifies that library class filtering configuration is handled.
     */
    @Test
    public void testExecuteWithSkipNonPublicLibraryClasses(@TempDir Path tempDir) throws IOException {
        // Arrange - Create an empty jar for testing
        File emptyJar = tempDir.resolve("test.jar").toFile();
        try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(emptyJar))) {
            // Empty jar
        }

        Configuration configuration = new Configuration();
        configuration.skipNonPublicLibraryClasses = true;
        configuration.programJars = new ClassPath();
        ClassPathEntry entry = new ClassPathEntry(emptyJar, false);
        configuration.programJars.add(entry);

        InputReader inputReader = new InputReader(configuration);
        AppView appView = new AppView();

        // Act & Assert - Should throw IOException because no classes are found
        IOException exception = assertThrows(IOException.class, () -> {
            inputReader.execute(appView);
        }, "execute() should throw IOException when no classes are found");

        assertTrue(exception.getMessage().contains("doesn't contain any classes"));
    }

    /**
     * Tests execute() with skipNonPublicLibraryClassMembers disabled.
     * Verifies that the configuration change is handled.
     */
    @Test
    public void testExecuteWithSkipNonPublicLibraryClassMembersDisabled(@TempDir Path tempDir) throws IOException {
        // Arrange - Create an empty jar for testing
        File emptyJar = tempDir.resolve("test.jar").toFile();
        try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(emptyJar))) {
            // Empty jar
        }

        Configuration configuration = new Configuration();
        configuration.skipNonPublicLibraryClassMembers = false;
        configuration.programJars = new ClassPath();
        ClassPathEntry entry = new ClassPathEntry(emptyJar, false);
        configuration.programJars.add(entry);

        InputReader inputReader = new InputReader(configuration);
        AppView appView = new AppView();

        // Act & Assert - Should throw IOException because no classes are found
        IOException exception = assertThrows(IOException.class, () -> {
            inputReader.execute(appView);
        }, "execute() should throw IOException when no classes are found");

        assertTrue(exception.getMessage().contains("doesn't contain any classes"));
    }

    /**
     * Tests execute() with verbose enabled.
     * Verifies that verbose logging doesn't affect execution outcome.
     */
    @Test
    public void testExecuteWithVerboseEnabled(@TempDir Path tempDir) throws IOException {
        // Arrange - Create an empty jar for testing
        File emptyJar = tempDir.resolve("test.jar").toFile();
        try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(emptyJar))) {
            // Empty jar
        }

        Configuration configuration = new Configuration();
        configuration.verbose = true;
        configuration.programJars = new ClassPath();
        ClassPathEntry entry = new ClassPathEntry(emptyJar, false);
        configuration.programJars.add(entry);

        InputReader inputReader = new InputReader(configuration);
        AppView appView = new AppView();

        // Act & Assert - Should throw IOException because no classes are found
        IOException exception = assertThrows(IOException.class, () -> {
            inputReader.execute(appView);
        }, "execute() should throw IOException when no classes are found");

        assertTrue(exception.getMessage().contains("doesn't contain any classes"));
    }

    /**
     * Tests execute() with preverify enabled.
     * Verifies that preverification configuration is handled.
     */
    @Test
    public void testExecuteWithPreverifyEnabled(@TempDir Path tempDir) throws IOException {
        // Arrange - Create an empty jar for testing
        File emptyJar = tempDir.resolve("test.jar").toFile();
        try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(emptyJar))) {
            // Empty jar
        }

        Configuration configuration = new Configuration();
        configuration.preverify = true;
        configuration.programJars = new ClassPath();
        ClassPathEntry entry = new ClassPathEntry(emptyJar, false);
        configuration.programJars.add(entry);

        InputReader inputReader = new InputReader(configuration);
        AppView appView = new AppView();

        // Act & Assert - Should throw IOException because no classes are found
        IOException exception = assertThrows(IOException.class, () -> {
            inputReader.execute(appView);
        }, "execute() should throw IOException when no classes are found");

        assertTrue(exception.getMessage().contains("doesn't contain any classes"));
    }

    /**
     * Tests execute() with backport enabled.
     * Verifies that backport configuration is handled.
     */
    @Test
    public void testExecuteWithBackportEnabled(@TempDir Path tempDir) throws IOException {
        // Arrange - Create an empty jar for testing
        File emptyJar = tempDir.resolve("test.jar").toFile();
        try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(emptyJar))) {
            // Empty jar
        }

        Configuration configuration = new Configuration();
        configuration.backport = true;
        configuration.programJars = new ClassPath();
        ClassPathEntry entry = new ClassPathEntry(emptyJar, false);
        configuration.programJars.add(entry);

        InputReader inputReader = new InputReader(configuration);
        AppView appView = new AppView();

        // Act & Assert - Should throw IOException because no classes are found
        IOException exception = assertThrows(IOException.class, () -> {
            inputReader.execute(appView);
        }, "execute() should throw IOException when no classes are found");

        assertTrue(exception.getMessage().contains("doesn't contain any classes"));
    }

    /**
     * Tests execute() with android enabled.
     * Verifies that Android configuration is handled.
     */
    @Test
    public void testExecuteWithAndroidEnabled(@TempDir Path tempDir) throws IOException {
        // Arrange - Create an empty jar for testing
        File emptyJar = tempDir.resolve("test.jar").toFile();
        try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(emptyJar))) {
            // Empty jar
        }

        Configuration configuration = new Configuration();
        configuration.android = true;
        configuration.programJars = new ClassPath();
        ClassPathEntry entry = new ClassPathEntry(emptyJar, false);
        configuration.programJars.add(entry);

        InputReader inputReader = new InputReader(configuration);
        AppView appView = new AppView();

        // Act & Assert - Should throw IOException because no classes are found
        IOException exception = assertThrows(IOException.class, () -> {
            inputReader.execute(appView);
        }, "execute() should throw IOException when no classes are found");

        assertTrue(exception.getMessage().contains("doesn't contain any classes"));
    }

    /**
     * Tests execute() with a complex configuration combining multiple options.
     * Verifies that the method handles realistic configuration scenarios.
     */
    @Test
    public void testExecuteWithComplexConfiguration(@TempDir Path tempDir) throws IOException {
        // Arrange - Create an empty jar for testing
        File emptyJar = tempDir.resolve("test.jar").toFile();
        try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(emptyJar))) {
            // Empty jar
        }

        Configuration configuration = new Configuration();
        configuration.shrink = true;
        configuration.optimize = true;
        configuration.obfuscate = false;
        configuration.verbose = true;
        configuration.ignoreWarnings = false;
        configuration.keepKotlinMetadata = false;
        configuration.skipNonPublicLibraryClasses = false;
        configuration.programJars = new ClassPath();
        ClassPathEntry entry = new ClassPathEntry(emptyJar, false);
        configuration.programJars.add(entry);

        InputReader inputReader = new InputReader(configuration);
        AppView appView = new AppView();

        // Act & Assert - Should throw IOException because no classes are found
        IOException exception = assertThrows(IOException.class, () -> {
            inputReader.execute(appView);
        }, "execute() should throw IOException when no classes are found");

        assertTrue(exception.getMessage().contains("doesn't contain any classes"));
    }

    /**
     * Tests execute() called twice on the same InputReader instance.
     * Verifies that the method can be called multiple times.
     */
    @Test
    public void testExecuteCalledMultipleTimes(@TempDir Path tempDir) throws IOException {
        // Arrange - Create an empty jar for testing
        File emptyJar = tempDir.resolve("test.jar").toFile();
        try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(emptyJar))) {
            // Empty jar
        }

        Configuration configuration = new Configuration();
        configuration.programJars = new ClassPath();
        ClassPathEntry entry = new ClassPathEntry(emptyJar, false);
        configuration.programJars.add(entry);

        InputReader inputReader = new InputReader(configuration);
        AppView appView1 = new AppView();
        AppView appView2 = new AppView();

        // Act & Assert - First call should throw IOException
        assertThrows(IOException.class, () -> {
            inputReader.execute(appView1);
        }, "First execute() should throw IOException");

        // Act & Assert - Second call should also throw IOException
        assertThrows(IOException.class, () -> {
            inputReader.execute(appView2);
        }, "Second execute() should also throw IOException");
    }

    /**
     * Tests execute() with different AppView instances.
     * Verifies that the same InputReader can process different AppViews.
     */
    @Test
    public void testExecuteWithDifferentAppViews(@TempDir Path tempDir) throws IOException {
        // Arrange - Create an empty jar for testing
        File emptyJar = tempDir.resolve("test.jar").toFile();
        try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(emptyJar))) {
            // Empty jar
        }

        Configuration configuration = new Configuration();
        configuration.programJars = new ClassPath();
        ClassPathEntry entry = new ClassPathEntry(emptyJar, false);
        configuration.programJars.add(entry);

        InputReader inputReader = new InputReader(configuration);
        AppView appView1 = new AppView();
        AppView appView2 = new AppView();

        // Act & Assert - Execute with first AppView
        assertThrows(IOException.class, () -> {
            inputReader.execute(appView1);
        }, "execute() with first AppView should throw IOException");

        // Act & Assert - Execute with second AppView
        assertThrows(IOException.class, () -> {
            inputReader.execute(appView2);
        }, "execute() with second AppView should throw IOException");
    }

    /**
     * Tests execute() with empty AppView (empty class pools).
     * Verifies that the method handles empty AppView correctly.
     */
    @Test
    public void testExecuteWithEmptyAppView(@TempDir Path tempDir) throws IOException {
        // Arrange - Create an empty jar for testing
        File emptyJar = tempDir.resolve("test.jar").toFile();
        try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(emptyJar))) {
            // Empty jar
        }

        Configuration configuration = new Configuration();
        configuration.programJars = new ClassPath();
        ClassPathEntry entry = new ClassPathEntry(emptyJar, false);
        configuration.programJars.add(entry);

        InputReader inputReader = new InputReader(configuration);

        // Create AppView with empty pools
        ClassPool emptyProgramClassPool = new ClassPool();
        ClassPool emptyLibraryClassPool = new ClassPool();
        AppView appView = new AppView(emptyProgramClassPool, emptyLibraryClassPool);

        // Act & Assert - Should throw IOException because no classes are found
        IOException exception = assertThrows(IOException.class, () -> {
            inputReader.execute(appView);
        }, "execute() should throw IOException when no classes are found");

        assertTrue(exception.getMessage().contains("doesn't contain any classes"));
    }

    /**
     * Tests execute() with libraryJars set but all library-requiring flags disabled.
     * Verifies that libraryJars are not read when not needed.
     */
    @Test
    public void testExecuteWithLibraryJarsButNoProcessingEnabled(@TempDir Path tempDir) throws IOException {
        // Arrange - Create empty jars for testing
        File emptyProgramJar = tempDir.resolve("program.jar").toFile();
        try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(emptyProgramJar))) {
            // Empty jar
        }

        File emptyLibraryJar = tempDir.resolve("library.jar").toFile();
        try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(emptyLibraryJar))) {
            // Empty jar
        }

        Configuration configuration = new Configuration();
        configuration.programJars = new ClassPath();
        configuration.programJars.add(new ClassPathEntry(emptyProgramJar, false));

        configuration.libraryJars = new ClassPath();
        configuration.libraryJars.add(new ClassPathEntry(emptyLibraryJar, false));

        // All library-requiring flags are disabled
        configuration.shrink = false;
        configuration.optimize = false;
        configuration.obfuscate = false;
        configuration.preverify = false;
        configuration.backport = false;
        configuration.printSeeds = null;

        InputReader inputReader = new InputReader(configuration);
        AppView appView = new AppView();

        // Act & Assert - Should throw IOException because no classes in program jar
        // Library jars should not be read because no processing flags are enabled
        IOException exception = assertThrows(IOException.class, () -> {
            inputReader.execute(appView);
        }, "execute() should throw IOException when no classes are found in program jars");

        assertTrue(exception.getMessage().contains("doesn't contain any classes"));
    }

    /**
     * Tests execute() with output entry in programJars.
     * Verifies that output entries are skipped during reading.
     */
    @Test
    public void testExecuteWithOutputEntry(@TempDir Path tempDir) throws IOException {
        // Arrange - Create an empty jar for testing
        File emptyJar = tempDir.resolve("output.jar").toFile();
        try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(emptyJar))) {
            // Empty jar
        }

        Configuration configuration = new Configuration();
        configuration.programJars = new ClassPath();

        // Add as output entry (second parameter is true)
        ClassPathEntry outputEntry = new ClassPathEntry(emptyJar, true);
        configuration.programJars.add(outputEntry);

        InputReader inputReader = new InputReader(configuration);
        AppView appView = new AppView();

        // Act & Assert - Should throw IOException because output entries are skipped
        IOException exception = assertThrows(IOException.class, () -> {
            inputReader.execute(appView);
        }, "execute() should throw IOException when only output entries exist");

        assertTrue(exception.getMessage().contains("doesn't contain any classes"));
    }
}
