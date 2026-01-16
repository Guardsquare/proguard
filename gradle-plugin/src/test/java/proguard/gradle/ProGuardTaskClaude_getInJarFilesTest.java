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
 * Tests for {@link ProGuardTask#getInJarFiles()}.
 *
 * This test class verifies that the getInJarFiles method correctly
 * returns a List of input jar file objects configured via the injars() method.
 * Unlike getInJarFileCollection(), this method returns the raw list of Objects
 * (String, File, etc.) before Gradle resolves them.
 */
public class ProGuardTaskClaude_getInJarFilesTest {

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
     * Tests that getInJarFiles returns an empty list when no input jars have been configured.
     */
    @Test
    public void testGetInJarFiles_noInputJars() {
        // When: No input jars are configured
        List result = task.getInJarFiles();

        // Then: The list should be empty
        assertNotNull(result, "List should not be null");
        assertTrue(result.isEmpty(), "List should be empty when no input jars are configured");
    }

    /**
     * Tests that getInJarFiles returns a list containing a single entry when one input jar
     * is configured with a String path.
     */
    @Test
    public void testGetInJarFiles_singleInputJar_StringPath() throws Exception {
        // Given: A single input jar configured with a String path
        String inputJarPath = "input.jar";
        task.injars(inputJarPath);

        // When: Getting the input jar files list
        List result = task.getInJarFiles();

        // Then: The list should contain the configured string
        assertNotNull(result, "List should not be null");
        assertEquals(1, result.size(), "List should contain 1 entry");
        assertEquals(inputJarPath, result.get(0), "List entry should match the configured string path");
    }

    /**
     * Tests that getInJarFiles returns a list containing a single entry when one input jar
     * is configured with a File object.
     */
    @Test
    public void testGetInJarFiles_singleInputJar_FileObject() throws Exception {
        // Given: A single input jar configured with a File object
        File inputJar = new File(tempDir.toFile(), "input.jar");
        task.injars(inputJar);

        // When: Getting the input jar files list
        List result = task.getInJarFiles();

        // Then: The list should contain the configured File object
        assertNotNull(result, "List should not be null");
        assertEquals(1, result.size(), "List should contain 1 entry");
        assertSame(inputJar, result.get(0), "List entry should be the same File object");
    }

    /**
     * Tests that getInJarFiles returns a list containing multiple entries when multiple
     * input jars are configured.
     */
    @Test
    public void testGetInJarFiles_multipleInputJars() throws Exception {
        // Given: Multiple input jars configured
        File inputJar1 = new File(tempDir.toFile(), "input1.jar");
        String inputJar2 = "input2.jar";
        File inputJar3 = new File(tempDir.toFile(), "input3.jar");

        task.injars(inputJar1);
        task.injars(inputJar2);
        task.injars(inputJar3);

        // When: Getting the input jar files list
        List result = task.getInJarFiles();

        // Then: The list should contain all configured entries in order
        assertNotNull(result, "List should not be null");
        assertEquals(3, result.size(), "List should contain 3 entries");
        assertSame(inputJar1, result.get(0), "First entry should be inputJar1");
        assertEquals(inputJar2, result.get(1), "Second entry should be inputJar2");
        assertSame(inputJar3, result.get(2), "Third entry should be inputJar3");
    }

    /**
     * Tests that getInJarFiles preserves the order of input jars as they were added.
     */
    @Test
    public void testGetInJarFiles_preservesOrder() throws Exception {
        // Given: Input jars added in a specific order
        String jar1 = "first.jar";
        String jar2 = "second.jar";
        String jar3 = "third.jar";

        task.injars(jar1);
        task.injars(jar2);
        task.injars(jar3);

        // When: Getting the input jar files list
        List result = task.getInJarFiles();

        // Then: The list should maintain the same order
        assertNotNull(result, "List should not be null");
        assertEquals(3, result.size(), "List should contain 3 entries");
        assertEquals(jar1, result.get(0), "First entry should be first.jar");
        assertEquals(jar2, result.get(1), "Second entry should be second.jar");
        assertEquals(jar3, result.get(2), "Third entry should be third.jar");
    }

    /**
     * Tests that getInJarFiles works correctly when input jars are configured with filter arguments.
     * Note: Filters are stored separately in getInJarFilters(), not in getInJarFiles().
     */
    @Test
    public void testGetInJarFiles_withFilterArgs() throws Exception {
        // Given: Input jar configured with filter arguments
        File inputJar = new File(tempDir.toFile(), "input.jar");
        java.util.Map<String, String> filterArgs = new java.util.HashMap<>();
        filterArgs.put("filter", "!**.class");
        task.injars(filterArgs, inputJar);

        // When: Getting the input jar files list
        List result = task.getInJarFiles();

        // Then: The list should contain the file (filters are stored separately)
        assertNotNull(result, "List should not be null");
        assertEquals(1, result.size(), "List should contain 1 entry");
        assertSame(inputJar, result.get(0), "List entry should be the File object");
    }

    /**
     * Tests that getInJarFiles returns the raw, unresolved objects (not File objects).
     */
    @Test
    public void testGetInJarFiles_returnsRawObjects() throws Exception {
        // Given: Input jars configured with various object types
        String stringPath = "path/to/input.jar";
        File fileObject = new File("another.jar");

        task.injars(stringPath);
        task.injars(fileObject);

        // When: Getting the input jar files list
        List result = task.getInJarFiles();

        // Then: The list should contain the raw objects, not resolved File objects
        assertNotNull(result, "List should not be null");
        assertEquals(2, result.size(), "List should contain 2 entries");
        assertTrue(result.get(0) instanceof String, "First entry should be a String");
        assertTrue(result.get(1) instanceof File, "Second entry should be a File");
        assertEquals(stringPath, result.get(0), "First entry should be the original string");
        assertSame(fileObject, result.get(1), "Second entry should be the original File object");
    }

    /**
     * Tests that getInJarFiles can be called multiple times and returns the same list instance.
     */
    @Test
    public void testGetInJarFiles_returnsSameListInstance() throws Exception {
        // Given: An input jar configured
        task.injars("input.jar");

        // When: Getting the input jar files list multiple times
        List result1 = task.getInJarFiles();
        List result2 = task.getInJarFiles();

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
    public void testGetInJarFiles_returnsModifiableList() throws Exception {
        // Given: An input jar configured
        task.injars("input.jar");
        List result1 = task.getInJarFiles();
        int initialSize = result1.size();

        // When: Adding another input jar via injars()
        task.injars("another.jar");

        // Then: The previously obtained list should reflect the change
        List result2 = task.getInJarFiles();
        assertEquals(initialSize + 1, result1.size(), "Original list reference should reflect additions");
        assertEquals(result2.size(), result1.size(), "Both references should have the same size");
    }

    /**
     * Tests that getInJarFiles handles relative path strings.
     */
    @Test
    public void testGetInJarFiles_relativePathString() throws Exception {
        // Given: An input jar configured with a relative path string
        String relativePath = "build/libs/input.jar";
        task.injars(relativePath);

        // When: Getting the input jar files list
        List result = task.getInJarFiles();

        // Then: The list should contain the unresolved relative path string
        assertNotNull(result, "List should not be null");
        assertEquals(1, result.size(), "List should contain 1 entry");
        assertEquals(relativePath, result.get(0), "List should contain the original relative path string");
    }

    /**
     * Tests that getInJarFiles handles absolute path strings.
     */
    @Test
    public void testGetInJarFiles_absolutePathString() throws Exception {
        // Given: An input jar configured with an absolute path string
        String absolutePath = new File(tempDir.toFile(), "input.jar").getAbsolutePath();
        task.injars(absolutePath);

        // When: Getting the input jar files list
        List result = task.getInJarFiles();

        // Then: The list should contain the absolute path string
        assertNotNull(result, "List should not be null");
        assertEquals(1, result.size(), "List should contain 1 entry");
        assertEquals(absolutePath, result.get(0), "List should contain the absolute path string");
    }

    /**
     * Tests that input jars and other jar types are kept in separate lists.
     */
    @Test
    public void testGetInJarFiles_separateFromOtherJarTypes() throws Exception {
        // Given: Input, output, and library jars configured
        String inputJar = "input.jar";
        String outputJar = "output.jar";
        String libraryJar = "library.jar";

        task.injars(inputJar);
        task.outjars(outputJar);
        task.libraryjars(libraryJar);

        // When: Getting the input jar files list
        List inputResult = task.getInJarFiles();
        List outputResult = task.getOutJarFiles();
        List libraryResult = task.getLibraryJarFiles();

        // Then: Each list should only contain its respective jar type
        assertNotNull(inputResult, "Input list should not be null");
        assertEquals(1, inputResult.size(), "Input list should contain only 1 entry");
        assertEquals(inputJar, inputResult.get(0), "Input list should contain only the input jar");

        assertFalse(inputResult.contains(outputJar), "Input list should not contain output jar");
        assertFalse(inputResult.contains(libraryJar), "Input list should not contain library jar");
    }

    /**
     * Tests that getInJarFiles handles directory paths.
     */
    @Test
    public void testGetInJarFiles_directoryPath() throws Exception {
        // Given: A directory configured as input
        File inputDir = new File(tempDir.toFile(), "classes");
        inputDir.mkdirs();
        task.injars(inputDir);

        // When: Getting the input jar files list
        List result = task.getInJarFiles();

        // Then: The list should contain the directory File object
        assertNotNull(result, "List should not be null");
        assertEquals(1, result.size(), "List should contain 1 entry");
        assertSame(inputDir, result.get(0), "List should contain the directory File object");
    }

    /**
     * Tests that getInJarFiles can handle various file types beyond just .jar files.
     */
    @Test
    public void testGetInJarFiles_variousFileTypes() throws Exception {
        // Given: Various file types configured as input
        String jarFile = "input.jar";
        String aarFile = "library.aar";
        String apkFile = "app.apk";
        String warFile = "webapp.war";

        task.injars(jarFile);
        task.injars(aarFile);
        task.injars(apkFile);
        task.injars(warFile);

        // When: Getting the input jar files list
        List result = task.getInJarFiles();

        // Then: The list should contain all file types
        assertNotNull(result, "List should not be null");
        assertEquals(4, result.size(), "List should contain 4 entries");
        assertEquals(jarFile, result.get(0), "First entry should be jar file");
        assertEquals(aarFile, result.get(1), "Second entry should be aar file");
        assertEquals(apkFile, result.get(2), "Third entry should be apk file");
        assertEquals(warFile, result.get(3), "Fourth entry should be war file");
    }
}
