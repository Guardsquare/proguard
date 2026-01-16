/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 */
package proguard.gradle;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import proguard.Configuration;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link ProGuardTask#getPrintSeedsFile()}.
 *
 * This test class verifies that the getPrintSeedsFile() method correctly
 * returns the print seeds file for Gradle's output tracking.
 *
 * Method signature: getPrintSeedsFile()Ljava/io/File;
 * - Returns the configured print seeds file
 * - Returns null if printSeeds is null
 * - Returns null if printSeeds is Configuration.STD_OUT (stdout)
 * - Annotated with @Optional and @OutputFile for Gradle task dependencies
 *
 * Key behavior: This method is used by Gradle to track output files for
 * incremental builds and up-to-date checks. It filters out stdout (STD_OUT)
 * and returns null for it, since stdout is not a real file that Gradle can track.
 *
 * The method uses optionalFile() internally which returns null for both null
 * input and Configuration.STD_OUT, otherwise returns the file.
 */
public class ProGuardTaskClaude_getPrintSeedsFileTest {

    @TempDir
    Path tempDir;

    private Project project;
    private ProGuardTask task;

    @BeforeEach
    public void setUp() {
        project = ProjectBuilder.builder()
                .withProjectDir(tempDir.toFile())
                .build();
        task = project.getTasks().create("testProguard", ProGuardTask.class);
    }

    @AfterEach
    public void tearDown() {
        project = null;
        task = null;
    }

    /**
     * Tests that getPrintSeedsFile() returns null when printSeeds is not set.
     */
    @Test
    public void testGetPrintSeedsFile_returnsNullWhenNotSet() throws Exception {
        // Given: printSeeds is not set (null)
        assertNull(task.configuration.printSeeds,
                   "printSeeds should initially be null");

        // When: Calling getPrintSeedsFile()
        File result = task.getPrintSeedsFile();

        // Then: Should return null
        assertNull(result,
                   "getPrintSeedsFile() should return null when printSeeds is not set");
    }

    /**
     * Tests that getPrintSeedsFile() returns null when printSeeds is set to stdout.
     */
    @Test
    public void testGetPrintSeedsFile_returnsNullForStdout() throws Exception {
        // Given: printSeeds is set to stdout
        task.printseeds();
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be stdout");

        // When: Calling getPrintSeedsFile()
        File result = task.getPrintSeedsFile();

        // Then: Should return null (stdout is not a trackable file)
        assertNull(result,
                   "getPrintSeedsFile() should return null for stdout");
    }

    /**
     * Tests that getPrintSeedsFile() returns the file when printSeeds is set to a file.
     */
    @Test
    public void testGetPrintSeedsFile_returnsFileWhenSet() throws Exception {
        // Given: printSeeds is set to a file
        task.printseeds("seeds.txt");
        assertNotNull(task.configuration.printSeeds,
                   "printSeeds should be set");

        // When: Calling getPrintSeedsFile()
        File result = task.getPrintSeedsFile();

        // Then: Should return the file
        assertNotNull(result,
                   "getPrintSeedsFile() should return the file");
        assertEquals("seeds.txt", result.getName(),
                   "File name should match");
    }

    /**
     * Tests that getPrintSeedsFile() returns the same file as configuration.printSeeds.
     */
    @Test
    public void testGetPrintSeedsFile_returnsSameFileAsConfiguration() throws Exception {
        // Given: printSeeds is set to a file
        task.printseeds("output-seeds.txt");

        // When: Calling getPrintSeedsFile()
        File result = task.getPrintSeedsFile();

        // Then: Should return the same file object
        assertSame(task.configuration.printSeeds, result,
                   "Should return the same file object as configuration.printSeeds");
    }

    /**
     * Tests that getPrintSeedsFile() tracks changes to printSeeds.
     */
    @Test
    public void testGetPrintSeedsFile_tracksChanges() throws Exception {
        // Given: Initial file
        task.printseeds("seeds1.txt");
        File firstFile = task.getPrintSeedsFile();
        assertNotNull(firstFile, "First file should not be null");
        assertEquals("seeds1.txt", firstFile.getName(),
                   "First file name should match");

        // When: Changing to a different file
        task.printseeds("seeds2.txt");
        File secondFile = task.getPrintSeedsFile();

        // Then: Should return the new file
        assertNotNull(secondFile, "Second file should not be null");
        assertEquals("seeds2.txt", secondFile.getName(),
                   "Second file name should match");
        assertNotSame(firstFile, secondFile,
                   "Should return a different file object");
    }

    /**
     * Tests that getPrintSeedsFile() returns null after switching from file to stdout.
     */
    @Test
    public void testGetPrintSeedsFile_returnsNullAfterSwitchingToStdout() throws Exception {
        // Given: printSeeds was set to a file
        task.printseeds("seeds.txt");
        assertNotNull(task.getPrintSeedsFile(),
                   "Should initially return a file");

        // When: Switching to stdout
        task.printseeds();
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                   "printSeeds should be stdout");

        // Then: Should return null
        File result = task.getPrintSeedsFile();
        assertNull(result,
                   "Should return null after switching to stdout");
    }

    /**
     * Tests that getPrintSeedsFile() returns file after switching from stdout to file.
     */
    @Test
    public void testGetPrintSeedsFile_returnsFileAfterSwitchingFromStdout() throws Exception {
        // Given: printSeeds was set to stdout
        task.printseeds();
        assertNull(task.getPrintSeedsFile(),
                   "Should initially return null for stdout");

        // When: Switching to a file
        task.printseeds("new-seeds.txt");

        // Then: Should return the file
        File result = task.getPrintSeedsFile();
        assertNotNull(result,
                   "Should return file after switching from stdout");
        assertEquals("new-seeds.txt", result.getName(),
                   "File name should match");
    }

    /**
     * Tests that getPrintSeedsFile() works with absolute paths.
     */
    @Test
    public void testGetPrintSeedsFile_absolutePath() throws Exception {
        // Given: printSeeds set to absolute path
        File absoluteFile = new File(tempDir.toFile(), "absolute-seeds.txt");
        task.printseeds(absoluteFile.getAbsolutePath());

        // When: Calling getPrintSeedsFile()
        File result = task.getPrintSeedsFile();

        // Then: Should return the file
        assertNotNull(result,
                   "Should return file for absolute path");
        assertEquals("absolute-seeds.txt", result.getName(),
                   "File name should match");
    }

    /**
     * Tests that getPrintSeedsFile() works with relative paths.
     */
    @Test
    public void testGetPrintSeedsFile_relativePath() throws Exception {
        // Given: printSeeds set to relative path
        task.printseeds("build/outputs/seeds.txt");

        // When: Calling getPrintSeedsFile()
        File result = task.getPrintSeedsFile();

        // Then: Should return the file
        assertNotNull(result,
                   "Should return file for relative path");
        assertEquals("seeds.txt", result.getName(),
                   "File name should match");
    }

    /**
     * Tests that getPrintSeedsFile() works with nested directories.
     */
    @Test
    public void testGetPrintSeedsFile_nestedDirectories() throws Exception {
        // Given: printSeeds set to nested path
        task.printseeds("build/reports/proguard/seeds.txt");

        // When: Calling getPrintSeedsFile()
        File result = task.getPrintSeedsFile();

        // Then: Should return the file
        assertNotNull(result,
                   "Should return file for nested path");
        assertEquals("seeds.txt", result.getName(),
                   "File name should match");
    }

    /**
     * Tests that getPrintSeedsFile() can be called multiple times.
     */
    @Test
    public void testGetPrintSeedsFile_multipleCalls() throws Exception {
        // Given: printSeeds is set
        task.printseeds("seeds.txt");

        // When: Calling getPrintSeedsFile() multiple times
        File result1 = task.getPrintSeedsFile();
        File result2 = task.getPrintSeedsFile();
        File result3 = task.getPrintSeedsFile();

        // Then: All calls should return the same file
        assertNotNull(result1, "First call should return file");
        assertNotNull(result2, "Second call should return file");
        assertNotNull(result3, "Third call should return file");
        assertSame(result1, result2, "Should return same object");
        assertSame(result2, result3, "Should return same object");
    }

    /**
     * Tests that getPrintSeedsFile() is independent of injars configuration.
     */
    @Test
    public void testGetPrintSeedsFile_independentFromInjars() throws Exception {
        // Given: injars configured
        task.injars("input.jar");
        task.printseeds("seeds.txt");

        // When: Calling getPrintSeedsFile()
        File result = task.getPrintSeedsFile();

        // Then: Should return the file regardless of injars
        assertNotNull(result,
                   "Should return file independent of injars");
        assertEquals("seeds.txt", result.getName(),
                   "File name should match");
        assertEquals(1, task.getInJarFiles().size(),
                   "Should have 1 input jar");
    }

    /**
     * Tests that getPrintSeedsFile() is independent of other configuration.
     */
    @Test
    public void testGetPrintSeedsFile_independentFromOtherConfiguration() throws Exception {
        // Given: Various configuration options set
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars("lib.jar");
        task.printseeds("seeds.txt");

        // When: Calling getPrintSeedsFile()
        File result = task.getPrintSeedsFile();

        // Then: Should return the file
        assertNotNull(result,
                   "Should return file independent of other configuration");
        assertEquals("seeds.txt", result.getName(),
                   "File name should match");
    }

    /**
     * Tests that getPrintSeedsFile() works in realistic Android scenario.
     */
    @Test
    public void testGetPrintSeedsFile_realisticAndroidScenario() throws Exception {
        // Given: Android project configuration
        task.injars("build/intermediates/classes.jar");
        task.libraryjars("${ANDROID_HOME}/platforms/android-28/android.jar");
        task.printseeds("build/outputs/proguard/seeds.txt");
        task.outjars("build/outputs/app-release.jar");

        // When: Calling getPrintSeedsFile()
        File result = task.getPrintSeedsFile();

        // Then: Should return the seeds file for Gradle tracking
        assertNotNull(result,
                   "Should return file for Android scenario");
        assertEquals("seeds.txt", result.getName(),
                   "File name should match");
    }

    /**
     * Tests that getPrintSeedsFile() works in realistic Java scenario.
     */
    @Test
    public void testGetPrintSeedsFile_realisticJavaScenario() throws Exception {
        // Given: Java project configuration
        task.injars("build/libs/app.jar");
        task.libraryjars("${JAVA_HOME}/jre/lib/rt.jar");
        task.printseeds("docs/proguard-seeds.txt");
        task.outjars("build/libs/app-obfuscated.jar");

        // When: Calling getPrintSeedsFile()
        File result = task.getPrintSeedsFile();

        // Then: Should return the seeds file
        assertNotNull(result,
                   "Should return file for Java scenario");
        assertEquals("proguard-seeds.txt", result.getName(),
                   "File name should match");
    }

    /**
     * Tests that getPrintSeedsFile() enables Gradle's up-to-date checking.
     */
    @Test
    public void testGetPrintSeedsFile_enablesUpToDateChecking() throws Exception {
        // Given: File-based seed output
        task.printseeds("build/outputs/seeds.txt");

        // When: Calling getPrintSeedsFile()
        File result = task.getPrintSeedsFile();

        // Then: Should return a file for Gradle to track
        assertNotNull(result,
                   "Should return file for up-to-date checking");
        assertTrue(result.getPath().contains("seeds.txt"),
                  "Path should contain the filename");
    }

    /**
     * Tests that getPrintSeedsFile() returns null for stdout in CI scenario.
     */
    @Test
    public void testGetPrintSeedsFile_stdoutInCiScenario() throws Exception {
        // Given: CI build using stdout for seeds
        task.injars("build/libs/app.jar");
        task.printseeds(); // stdout
        task.outjars("build/libs/app-release.jar");

        // When: Calling getPrintSeedsFile()
        File result = task.getPrintSeedsFile();

        // Then: Should return null (stdout not trackable)
        assertNull(result,
                   "Should return null for stdout in CI scenario");
    }

    /**
     * Tests that getPrintSeedsFile() works with different file extensions.
     */
    @Test
    public void testGetPrintSeedsFile_differentFileExtensions() throws Exception {
        // Test .txt extension
        task.printseeds("seeds.txt");
        File txtFile = task.getPrintSeedsFile();
        assertNotNull(txtFile, "Should work with .txt extension");
        assertEquals("seeds.txt", txtFile.getName());

        // Test .log extension
        task.printseeds("seeds.log");
        File logFile = task.getPrintSeedsFile();
        assertNotNull(logFile, "Should work with .log extension");
        assertEquals("seeds.log", logFile.getName());

        // Test no extension
        task.printseeds("seeds");
        File noExtFile = task.getPrintSeedsFile();
        assertNotNull(noExtFile, "Should work without extension");
        assertEquals("seeds", noExtFile.getName());
    }

    /**
     * Tests that getPrintSeedsFile() behavior is consistent across task instances.
     */
    @Test
    public void testGetPrintSeedsFile_consistentAcrossTasks() throws Exception {
        // Given: Two different task instances
        ProGuardTask task1 = project.getTasks().create("testProguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("testProguard2", ProGuardTask.class);

        // When: Setting different configurations
        task1.printseeds("seeds1.txt");
        task2.printseeds(); // stdout

        // Then: Each should return its own configuration
        File result1 = task1.getPrintSeedsFile();
        File result2 = task2.getPrintSeedsFile();

        assertNotNull(result1, "Task1 should return file");
        assertEquals("seeds1.txt", result1.getName());
        assertNull(result2, "Task2 should return null for stdout");
    }

    /**
     * Tests that getPrintSeedsFile() returns file for File object input.
     */
    @Test
    public void testGetPrintSeedsFile_fileObjectInput() throws Exception {
        // Given: printSeeds set with File object
        File seedFile = new File(tempDir.toFile(), "test-seeds.txt");
        task.printseeds(seedFile);

        // When: Calling getPrintSeedsFile()
        File result = task.getPrintSeedsFile();

        // Then: Should return the file
        assertNotNull(result,
                   "Should return file for File object input");
        assertEquals("test-seeds.txt", result.getName(),
                   "File name should match");
    }

    /**
     * Tests that getPrintSeedsFile() works with build directory structure.
     */
    @Test
    public void testGetPrintSeedsFile_buildDirectoryStructure() throws Exception {
        // Given: Standard build directory structure
        task.printseeds("build/reports/proguard/seeds.txt");

        // When: Calling getPrintSeedsFile()
        File result = task.getPrintSeedsFile();

        // Then: Should return the file in build directory
        assertNotNull(result,
                   "Should return file in build directory");
        assertEquals("seeds.txt", result.getName(),
                   "File name should match");
        assertTrue(result.getPath().contains("proguard"),
                  "Path should contain proguard directory");
    }

    /**
     * Tests that getPrintSeedsFile() handles null configuration correctly.
     */
    @Test
    public void testGetPrintSeedsFile_nullConfiguration() throws Exception {
        // Given: printSeeds is explicitly set to null
        task.configuration.printSeeds = null;

        // When: Calling getPrintSeedsFile()
        File result = task.getPrintSeedsFile();

        // Then: Should return null
        assertNull(result,
                   "Should return null for null configuration");
    }

    /**
     * Tests that getPrintSeedsFile() handles Configuration.STD_OUT correctly.
     */
    @Test
    public void testGetPrintSeedsFile_configurationStdOut() throws Exception {
        // Given: printSeeds is explicitly set to STD_OUT
        task.configuration.printSeeds = Configuration.STD_OUT;

        // When: Calling getPrintSeedsFile()
        File result = task.getPrintSeedsFile();

        // Then: Should return null
        assertNull(result,
                   "Should return null for Configuration.STD_OUT");
    }

    /**
     * Tests that getPrintSeedsFile() works with minimal configuration.
     */
    @Test
    public void testGetPrintSeedsFile_minimalConfiguration() throws Exception {
        // Given: Only printseeds configured
        task.printseeds("minimal-seeds.txt");

        // When: Calling getPrintSeedsFile()
        File result = task.getPrintSeedsFile();

        // Then: Should return the file
        assertNotNull(result,
                   "Should work with minimal configuration");
        assertEquals("minimal-seeds.txt", result.getName(),
                   "File name should match");
    }

    /**
     * Tests that getPrintSeedsFile() supports versioned filenames.
     */
    @Test
    public void testGetPrintSeedsFile_versionedFilenames() throws Exception {
        // Given: Versioned filename
        task.printseeds("seeds-v1.0.0.txt");

        // When: Calling getPrintSeedsFile()
        File result = task.getPrintSeedsFile();

        // Then: Should return the versioned file
        assertNotNull(result,
                   "Should support versioned filenames");
        assertEquals("seeds-v1.0.0.txt", result.getName(),
                   "Versioned filename should match");
    }

    /**
     * Tests that getPrintSeedsFile() supports timestamped filenames.
     */
    @Test
    public void testGetPrintSeedsFile_timestampedFilenames() throws Exception {
        // Given: Timestamped filename
        String timestamp = "20240115-120000";
        task.printseeds("seeds-" + timestamp + ".txt");

        // When: Calling getPrintSeedsFile()
        File result = task.getPrintSeedsFile();

        // Then: Should return the timestamped file
        assertNotNull(result,
                   "Should support timestamped filenames");
        assertTrue(result.getName().contains(timestamp),
                  "Filename should contain timestamp");
    }

    /**
     * Tests that getPrintSeedsFile() is useful for Gradle output tracking.
     */
    @Test
    public void testGetPrintSeedsFile_gradleOutputTracking() throws Exception {
        // Given: Configuration with file output
        task.printseeds("build/outputs/seeds.txt");

        // When: Gradle queries output files
        File result = task.getPrintSeedsFile();

        // Then: Should provide file for tracking
        assertNotNull(result,
                   "Should provide file for Gradle output tracking");
        assertTrue(result.getPath().contains("outputs"),
                  "Path should be in outputs directory");
    }

    /**
     * Tests that getPrintSeedsFile() filters out stdout for incremental builds.
     */
    @Test
    public void testGetPrintSeedsFile_filtersStdoutForIncrementalBuilds() throws Exception {
        // Given: Stdout configuration (not a real file)
        task.printseeds();

        // When: Gradle checks for output files
        File result = task.getPrintSeedsFile();

        // Then: Should return null (stdout is not trackable for incremental builds)
        assertNull(result,
                   "Should filter out stdout for incremental builds");
    }

    /**
     * Tests that getPrintSeedsFile() works after configuration changes.
     */
    @Test
    public void testGetPrintSeedsFile_afterConfigurationChanges() throws Exception {
        // Given: Initial configuration with stdout
        task.printseeds();
        assertNull(task.getPrintSeedsFile(),
                   "Should initially return null for stdout");

        // When: Changing to file and then to different file
        task.printseeds("seeds1.txt");
        File file1 = task.getPrintSeedsFile();
        assertNotNull(file1, "Should return file after first change");

        task.printseeds("seeds2.txt");
        File file2 = task.getPrintSeedsFile();
        assertNotNull(file2, "Should return file after second change");

        // Then: Should track the latest configuration
        assertEquals("seeds2.txt", file2.getName(),
                   "Should return latest configured file");
        assertNotSame(file1, file2,
                   "Should be different file objects");
    }

    /**
     * Tests that getPrintSeedsFile() returns appropriate file for archiving.
     */
    @Test
    public void testGetPrintSeedsFile_archivingScenario() throws Exception {
        // Given: Archiving configuration
        task.printseeds("archive/seeds-release-1.0.txt");

        // When: Calling getPrintSeedsFile()
        File result = task.getPrintSeedsFile();

        // Then: Should return archive file
        assertNotNull(result,
                   "Should return file for archiving");
        assertEquals("seeds-release-1.0.txt", result.getName(),
                   "Archive filename should match");
    }

    /**
     * Tests that getPrintSeedsFile() is annotated correctly for Gradle.
     */
    @Test
    public void testGetPrintSeedsFile_gradleAnnotations() throws Exception {
        // Given: Method should be annotated with @Optional and @OutputFile
        // (This is verified by compilation and Gradle's task validation)

        // When: File is configured
        task.printseeds("seeds.txt");
        File result = task.getPrintSeedsFile();

        // Then: Should return file that Gradle can track
        assertNotNull(result,
                   "Should return trackable file for Gradle");
    }

    /**
     * Tests that getPrintSeedsFile() works with complex project structure.
     */
    @Test
    public void testGetPrintSeedsFile_complexProjectStructure() throws Exception {
        // Given: Complex multi-module project
        task.injars("module1/build/classes");
        task.injars("module2/build/classes");
        task.outjars("build/libs/merged.jar");
        task.printseeds("build/reports/proguard/merged-seeds.txt");

        // When: Calling getPrintSeedsFile()
        File result = task.getPrintSeedsFile();

        // Then: Should return the seeds file
        assertNotNull(result,
                   "Should work with complex project structure");
        assertEquals("merged-seeds.txt", result.getName(),
                   "File name should match");
    }
}
