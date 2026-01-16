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

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link ProGuardTask#getOutJarFiles()}.
 *
 * This test class verifies that the getOutJarFiles method correctly
 * returns a List of output jar file objects configured via the outjars() method.
 * Unlike getOutJarFileCollection(), this method returns the raw list of Objects
 * (String, File, etc.) before Gradle resolves them.
 */
public class ProGuardTaskClaude_getOutJarFilesTest {

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
     * Tests that getOutJarFiles returns an empty list when no output jars have been configured.
     */
    @Test
    public void testGetOutJarFiles_noOutputJars() {
        // When: No output jars are configured
        List result = task.getOutJarFiles();

        // Then: The list should be empty
        assertNotNull(result, "List should not be null");
        assertTrue(result.isEmpty(), "List should be empty when no output jars are configured");
    }

    /**
     * Tests that getOutJarFiles returns a list containing a single entry when one output jar
     * is configured with a String path.
     */
    @Test
    public void testGetOutJarFiles_singleOutputJar_StringPath() throws Exception {
        // Given: A single output jar configured with a String path
        String outputJarPath = "output.jar";
        task.outjars(outputJarPath);

        // When: Getting the output jar files list
        List result = task.getOutJarFiles();

        // Then: The list should contain the configured string
        assertNotNull(result, "List should not be null");
        assertEquals(1, result.size(), "List should contain 1 entry");
        assertEquals(outputJarPath, result.get(0), "List entry should match the configured string path");
    }

    /**
     * Tests that getOutJarFiles returns a list containing a single entry when one output jar
     * is configured with a File object.
     */
    @Test
    public void testGetOutJarFiles_singleOutputJar_FileObject() throws Exception {
        // Given: A single output jar configured with a File object
        File outputJar = new File(tempDir.toFile(), "output.jar");
        task.outjars(outputJar);

        // When: Getting the output jar files list
        List result = task.getOutJarFiles();

        // Then: The list should contain the configured File object
        assertNotNull(result, "List should not be null");
        assertEquals(1, result.size(), "List should contain 1 entry");
        assertSame(outputJar, result.get(0), "List entry should be the same File object");
    }

    /**
     * Tests that getOutJarFiles returns a list containing multiple entries when multiple
     * output jars are configured.
     */
    @Test
    public void testGetOutJarFiles_multipleOutputJars() throws Exception {
        // Given: Multiple output jars configured
        File outputJar1 = new File(tempDir.toFile(), "output1.jar");
        String outputJar2 = "output2.jar";
        File outputJar3 = new File(tempDir.toFile(), "output3.jar");

        task.outjars(outputJar1);
        task.outjars(outputJar2);
        task.outjars(outputJar3);

        // When: Getting the output jar files list
        List result = task.getOutJarFiles();

        // Then: The list should contain all configured entries in order
        assertNotNull(result, "List should not be null");
        assertEquals(3, result.size(), "List should contain 3 entries");
        assertSame(outputJar1, result.get(0), "First entry should be outputJar1");
        assertEquals(outputJar2, result.get(1), "Second entry should be outputJar2");
        assertSame(outputJar3, result.get(2), "Third entry should be outputJar3");
    }

    /**
     * Tests that getOutJarFiles preserves the order of output jars as they were added.
     */
    @Test
    public void testGetOutJarFiles_preservesOrder() throws Exception {
        // Given: Output jars added in a specific order
        String jar1 = "first.jar";
        String jar2 = "second.jar";
        String jar3 = "third.jar";

        task.outjars(jar1);
        task.outjars(jar2);
        task.outjars(jar3);

        // When: Getting the output jar files list
        List result = task.getOutJarFiles();

        // Then: The list should maintain the same order
        assertNotNull(result, "List should not be null");
        assertEquals(3, result.size(), "List should contain 3 entries");
        assertEquals(jar1, result.get(0), "First entry should be first.jar");
        assertEquals(jar2, result.get(1), "Second entry should be second.jar");
        assertEquals(jar3, result.get(2), "Third entry should be third.jar");
    }

    /**
     * Tests that getOutJarFiles works correctly when output jars are configured with filter arguments.
     * Note: Filters are stored separately in getOutJarFilters(), not in getOutJarFiles().
     */
    @Test
    public void testGetOutJarFiles_withFilterArgs() throws Exception {
        // Given: Output jar configured with filter arguments
        File outputJar = new File(tempDir.toFile(), "output.jar");
        java.util.Map<String, String> filterArgs = new java.util.HashMap<>();
        filterArgs.put("filter", "**.class");
        task.outjars(filterArgs, outputJar);

        // When: Getting the output jar files list
        List result = task.getOutJarFiles();

        // Then: The list should contain the file (filters are stored separately)
        assertNotNull(result, "List should not be null");
        assertEquals(1, result.size(), "List should contain 1 entry");
        assertSame(outputJar, result.get(0), "List entry should be the File object");
    }

    /**
     * Tests that getOutJarFiles returns the raw, unresolved objects (not File objects).
     */
    @Test
    public void testGetOutJarFiles_returnsRawObjects() throws Exception {
        // Given: Output jars configured with various object types
        String stringPath = "path/to/output.jar";
        File fileObject = new File("another.jar");

        task.outjars(stringPath);
        task.outjars(fileObject);

        // When: Getting the output jar files list
        List result = task.getOutJarFiles();

        // Then: The list should contain the raw objects, not resolved File objects
        assertNotNull(result, "List should not be null");
        assertEquals(2, result.size(), "List should contain 2 entries");
        assertTrue(result.get(0) instanceof String, "First entry should be a String");
        assertTrue(result.get(1) instanceof File, "Second entry should be a File");
        assertEquals(stringPath, result.get(0), "First entry should be the original string");
        assertSame(fileObject, result.get(1), "Second entry should be the original File object");
    }

    /**
     * Tests that getOutJarFiles can be called multiple times and returns the same list instance.
     */
    @Test
    public void testGetOutJarFiles_returnsSameListInstance() throws Exception {
        // Given: An output jar configured
        task.outjars("output.jar");

        // When: Getting the output jar files list multiple times
        List result1 = task.getOutJarFiles();
        List result2 = task.getOutJarFiles();

        // Then: Both calls should return the same list instance
        assertNotNull(result1, "First result should not be null");
        assertNotNull(result2, "Second result should not be null");
        assertSame(result1, result2, "Multiple invocations should return the same List instance");
    }

    /**
     * Tests that modifications to the returned list affect subsequent calls.
     * This verifies that the method returns the actual internal list, not a copy.
     */
    @Test
    public void testGetOutJarFiles_returnsModifiableList() throws Exception {
        // Given: An output jar configured
        task.outjars("output.jar");
        List result1 = task.getOutJarFiles();
        int initialSize = result1.size();

        // When: Adding another output jar via outjars()
        task.outjars("another.jar");

        // Then: The previously obtained list should reflect the change
        List result2 = task.getOutJarFiles();
        assertEquals(initialSize + 1, result1.size(), "Original list reference should reflect additions");
        assertEquals(result2.size(), result1.size(), "Both references should have the same size");
    }

    /**
     * Tests that getOutJarFiles handles relative path strings.
     */
    @Test
    public void testGetOutJarFiles_relativePathString() throws Exception {
        // Given: An output jar configured with a relative path string
        String relativePath = "build/outputs/output.jar";
        task.outjars(relativePath);

        // When: Getting the output jar files list
        List result = task.getOutJarFiles();

        // Then: The list should contain the unresolved relative path string
        assertNotNull(result, "List should not be null");
        assertEquals(1, result.size(), "List should contain 1 entry");
        assertEquals(relativePath, result.get(0), "List should contain the original relative path string");
    }

    /**
     * Tests that getOutJarFiles handles absolute path strings.
     */
    @Test
    public void testGetOutJarFiles_absolutePathString() throws Exception {
        // Given: An output jar configured with an absolute path string
        String absolutePath = new File(tempDir.toFile(), "output.jar").getAbsolutePath();
        task.outjars(absolutePath);

        // When: Getting the output jar files list
        List result = task.getOutJarFiles();

        // Then: The list should contain the absolute path string
        assertNotNull(result, "List should not be null");
        assertEquals(1, result.size(), "List should contain 1 entry");
        assertEquals(absolutePath, result.get(0), "List should contain the absolute path string");
    }

    /**
     * Tests that output jars and other jar types are kept in separate lists.
     */
    @Test
    public void testGetOutJarFiles_separateFromOtherJarTypes() throws Exception {
        // Given: Input, output, and library jars configured
        String inputJar = "input.jar";
        String outputJar = "output.jar";
        String libraryJar = "library.jar";

        task.injars(inputJar);
        task.outjars(outputJar);
        task.libraryjars(libraryJar);

        // When: Getting the output jar files list
        List inputResult = task.getInJarFiles();
        List outputResult = task.getOutJarFiles();
        List libraryResult = task.getLibraryJarFiles();

        // Then: Each list should only contain its respective jar type
        assertNotNull(outputResult, "Output list should not be null");
        assertEquals(1, outputResult.size(), "Output list should contain only 1 entry");
        assertEquals(outputJar, outputResult.get(0), "Output list should contain only the output jar");

        assertFalse(outputResult.contains(inputJar), "Output list should not contain input jar");
        assertFalse(outputResult.contains(libraryJar), "Output list should not contain library jar");
    }

    /**
     * Tests that getOutJarFiles handles directory paths.
     */
    @Test
    public void testGetOutJarFiles_directoryPath() throws Exception {
        // Given: A directory configured as output
        File outputDir = new File(tempDir.toFile(), "output");
        outputDir.mkdirs();
        task.outjars(outputDir);

        // When: Getting the output jar files list
        List result = task.getOutJarFiles();

        // Then: The list should contain the directory File object
        assertNotNull(result, "List should not be null");
        assertEquals(1, result.size(), "List should contain 1 entry");
        assertSame(outputDir, result.get(0), "List should contain the directory File object");
    }

    /**
     * Tests that getOutJarFiles can handle various file types beyond just .jar files.
     */
    @Test
    public void testGetOutJarFiles_variousFileTypes() throws Exception {
        // Given: Various file types configured as output
        String jarFile = "output.jar";
        String aarFile = "library.aar";
        String apkFile = "app.apk";
        String warFile = "webapp.war";

        task.outjars(jarFile);
        task.outjars(aarFile);
        task.outjars(apkFile);
        task.outjars(warFile);

        // When: Getting the output jar files list
        List result = task.getOutJarFiles();

        // Then: The list should contain all file types
        assertNotNull(result, "List should not be null");
        assertEquals(4, result.size(), "List should contain 4 entries");
        assertEquals(jarFile, result.get(0), "First entry should be jar file");
        assertEquals(aarFile, result.get(1), "Second entry should be aar file");
        assertEquals(apkFile, result.get(2), "Third entry should be apk file");
        assertEquals(warFile, result.get(3), "Fourth entry should be war file");
    }

    /**
     * Tests that adding an output jar also updates the inJarCounts list.
     * This is ProGuard-specific behavior where output jars track how many input jars they correspond to.
     */
    @Test
    public void testGetOutJarFiles_interactionWithInJarCounts() throws Exception {
        // Given: Input jars configured before output jars
        task.injars("input1.jar");
        task.injars("input2.jar");
        task.outjars("output1.jar");

        task.injars("input3.jar");
        task.outjars("output2.jar");

        // When: Getting the output jar files and counts
        List outJarFiles = task.getOutJarFiles();
        List inJarCounts = task.getInJarCounts();

        // Then: Each output jar should have a corresponding count
        assertEquals(2, outJarFiles.size(), "Should have 2 output jars");
        assertEquals(2, inJarCounts.size(), "Should have 2 input jar counts");
        assertEquals(Integer.valueOf(2), inJarCounts.get(0), "First output should correspond to 2 input jars");
        assertEquals(Integer.valueOf(3), inJarCounts.get(1), "Second output should correspond to 3 total input jars");
    }

    /**
     * Tests that getOutJarFiles works correctly when no input jars are configured.
     * This is an edge case that might occur in some configurations.
     */
    @Test
    public void testGetOutJarFiles_noInputJars() throws Exception {
        // Given: Output jar configured without any input jars
        task.outjars("output.jar");

        // When: Getting the output jar files list
        List result = task.getOutJarFiles();

        // Then: The list should contain the output jar
        assertNotNull(result, "List should not be null");
        assertEquals(1, result.size(), "List should contain 1 entry");
        assertEquals("output.jar", result.get(0), "List should contain the output jar");
    }

    /**
     * Tests that getOutJarFiles maintains correspondence with getOutJarFilters.
     */
    @Test
    public void testGetOutJarFiles_correspondenceWithFilters() throws Exception {
        // Given: Multiple output jars with and without filters
        java.util.Map<String, String> filter1 = new java.util.HashMap<>();
        filter1.put("filter", "**.class");

        task.outjars(filter1, "output1.jar");
        task.outjars("output2.jar");  // No filters

        // When: Getting both lists
        List outJarFiles = task.getOutJarFiles();
        List outJarFilters = task.getOutJarFilters();

        // Then: Both lists should have the same size
        assertEquals(outJarFiles.size(), outJarFilters.size(),
                "Output files and filters lists should have the same size");
        assertEquals(2, outJarFiles.size(), "Should have 2 output jars");
    }
}
