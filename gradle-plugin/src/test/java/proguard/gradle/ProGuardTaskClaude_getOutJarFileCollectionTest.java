/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 */
package proguard.gradle;

import org.gradle.api.Project;
import org.gradle.api.file.FileCollection;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link ProGuardTask#getOutJarFileCollection()}.
 *
 * This test class verifies that the getOutJarFileCollection method correctly
 * returns a FileCollection based on the output jar files configured via the
 * outjars() method.
 */
public class ProGuardTaskClaude_getOutJarFileCollectionTest {

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
     * Tests that getOutJarFileCollection returns an empty collection when no
     * output jars have been configured.
     */
    @Test
    public void testGetOutJarFileCollection_noOutputJars() {
        // When: No output jars are configured
        FileCollection result = task.getOutJarFileCollection();

        // Then: The collection should be empty
        assertNotNull(result, "FileCollection should not be null");
        assertTrue(result.isEmpty(), "FileCollection should be empty when no output jars are configured");
    }

    /**
     * Tests that getOutJarFileCollection returns a collection containing a single
     * file when one output jar is configured with a String path.
     */
    @Test
    public void testGetOutJarFileCollection_singleOutputJar_StringPath() throws Exception {
        // Given: A single output jar configured with a String path
        File outputJar = new File(tempDir.toFile(), "output.jar");
        task.outjars(outputJar.getAbsolutePath());

        // When: Getting the output jar file collection
        FileCollection result = task.getOutJarFileCollection();

        // Then: The collection should contain the configured file
        assertNotNull(result, "FileCollection should not be null");
        assertEquals(1, result.getFiles().size(), "FileCollection should contain 1 file");
        assertEquals(outputJar.getAbsolutePath(), result.getSingleFile().getAbsolutePath(),
                "File path should match the configured output jar");
    }

    /**
     * Tests that getOutJarFileCollection returns a collection containing a single
     * file when one output jar is configured with a File object.
     */
    @Test
    public void testGetOutJarFileCollection_singleOutputJar_FileObject() throws Exception {
        // Given: A single output jar configured with a File object
        File outputJar = new File(tempDir.toFile(), "output.jar");
        task.outjars(outputJar);

        // When: Getting the output jar file collection
        FileCollection result = task.getOutJarFileCollection();

        // Then: The collection should contain the configured file
        assertNotNull(result, "FileCollection should not be null");
        assertEquals(1, result.getFiles().size(), "FileCollection should contain 1 file");
        assertEquals(outputJar.getAbsolutePath(), result.getSingleFile().getAbsolutePath(),
                "File path should match the configured output jar");
    }

    /**
     * Tests that getOutJarFileCollection returns a collection containing multiple
     * files when multiple output jars are configured.
     */
    @Test
    public void testGetOutJarFileCollection_multipleOutputJars() throws Exception {
        // Given: Multiple output jars configured
        File outputJar1 = new File(tempDir.toFile(), "output1.jar");
        File outputJar2 = new File(tempDir.toFile(), "output2.jar");
        File outputJar3 = new File(tempDir.toFile(), "output3.jar");

        task.outjars(outputJar1);
        task.outjars(outputJar2);
        task.outjars(outputJar3);

        // When: Getting the output jar file collection
        FileCollection result = task.getOutJarFileCollection();

        // Then: The collection should contain all configured files
        assertNotNull(result, "FileCollection should not be null");
        Set<File> files = result.getFiles();
        assertEquals(3, files.size(), "FileCollection should contain 3 files");

        Set<String> expectedPaths = new HashSet<>(Arrays.asList(
                outputJar1.getAbsolutePath(),
                outputJar2.getAbsolutePath(),
                outputJar3.getAbsolutePath()
        ));
        Set<String> actualPaths = new HashSet<>();
        for (File file : files) {
            actualPaths.add(file.getAbsolutePath());
        }
        assertEquals(expectedPaths, actualPaths, "All configured output jars should be in the collection");
    }

    /**
     * Tests that getOutJarFileCollection works correctly when output jars are
     * configured with filter arguments.
     */
    @Test
    public void testGetOutJarFileCollection_withFilterArgs() throws Exception {
        // Given: Output jar configured with filter arguments
        File outputJar = new File(tempDir.toFile(), "output.jar");
        java.util.Map<String, String> filterArgs = new java.util.HashMap<>();
        filterArgs.put("filter", "**.class");
        task.outjars(filterArgs, outputJar);

        // When: Getting the output jar file collection
        FileCollection result = task.getOutJarFileCollection();

        // Then: The collection should contain the configured file
        // Note: Filters don't affect which files are in the collection, only how they're processed
        assertNotNull(result, "FileCollection should not be null");
        assertEquals(1, result.getFiles().size(), "FileCollection should contain 1 file");
        assertEquals(outputJar.getAbsolutePath(), result.getSingleFile().getAbsolutePath(),
                "File path should match the configured output jar");
    }

    /**
     * Tests that getOutJarFileCollection handles relative paths correctly.
     */
    @Test
    public void testGetOutJarFileCollection_relativePath() throws Exception {
        // Given: An output jar configured with a relative path
        task.outjars("build/outputs/output.jar");

        // When: Getting the output jar file collection
        FileCollection result = task.getOutJarFileCollection();

        // Then: The collection should resolve the relative path
        assertNotNull(result, "FileCollection should not be null");
        assertEquals(1, result.getFiles().size(), "FileCollection should contain 1 file");
        File resolvedFile = result.getSingleFile();
        assertNotNull(resolvedFile, "Resolved file should not be null");
        assertTrue(resolvedFile.getPath().endsWith("output.jar"),
                "Resolved path should end with the file name");
    }

    /**
     * Tests that getOutJarFileCollection handles directory paths.
     */
    @Test
    public void testGetOutJarFileCollection_directoryPath() throws Exception {
        // Given: A directory configured as output
        File outputDir = new File(tempDir.toFile(), "output");
        outputDir.mkdirs();
        task.outjars(outputDir);

        // When: Getting the output jar file collection
        FileCollection result = task.getOutJarFileCollection();

        // Then: The collection should contain the directory
        assertNotNull(result, "FileCollection should not be null");
        assertEquals(1, result.getFiles().size(), "FileCollection should contain 1 entry");
        assertEquals(outputDir.getAbsolutePath(), result.getSingleFile().getAbsolutePath(),
                "Path should match the configured output directory");
    }

    /**
     * Tests that getOutJarFileCollection can be called multiple times and returns
     * consistent results.
     */
    @Test
    public void testGetOutJarFileCollection_multipleInvocations() throws Exception {
        // Given: An output jar configured
        File outputJar = new File(tempDir.toFile(), "output.jar");
        task.outjars(outputJar);

        // When: Getting the output jar file collection multiple times
        FileCollection result1 = task.getOutJarFileCollection();
        FileCollection result2 = task.getOutJarFileCollection();

        // Then: Both calls should return consistent results
        assertNotNull(result1, "First result should not be null");
        assertNotNull(result2, "Second result should not be null");
        assertEquals(result1.getFiles(), result2.getFiles(),
                "Multiple invocations should return collections with the same files");
    }

    /**
     * Tests that getOutJarFileCollection reflects changes after additional output
     * jars are added.
     */
    @Test
    public void testGetOutJarFileCollection_reflectsSubsequentAdditions() throws Exception {
        // Given: An initial output jar
        File outputJar1 = new File(tempDir.toFile(), "output1.jar");
        task.outjars(outputJar1);

        FileCollection initialCollection = task.getOutJarFileCollection();
        assertEquals(1, initialCollection.getFiles().size(), "Initial collection should have 1 file");

        // When: Adding another output jar
        File outputJar2 = new File(tempDir.toFile(), "output2.jar");
        task.outjars(outputJar2);

        // Then: The collection should reflect the addition
        FileCollection updatedCollection = task.getOutJarFileCollection();
        assertEquals(2, updatedCollection.getFiles().size(),
                "Updated collection should have 2 files after addition");
    }

    /**
     * Tests that getOutJarFileCollection handles mixed file types (jars, aars, etc.).
     */
    @Test
    public void testGetOutJarFileCollection_mixedFileTypes() throws Exception {
        // Given: Multiple output files with different extensions
        File outputJar = new File(tempDir.toFile(), "output.jar");
        File outputAar = new File(tempDir.toFile(), "output.aar");
        File outputApk = new File(tempDir.toFile(), "output.apk");

        task.outjars(outputJar);
        task.outjars(outputAar);
        task.outjars(outputApk);

        // When: Getting the output jar file collection
        FileCollection result = task.getOutJarFileCollection();

        // Then: All file types should be included
        assertNotNull(result, "FileCollection should not be null");
        assertEquals(3, result.getFiles().size(), "FileCollection should contain all 3 files");
    }

    /**
     * Tests that the FileCollection returned by getOutJarFileCollection is iterable.
     */
    @Test
    public void testGetOutJarFileCollection_isIterable() throws Exception {
        // Given: Multiple output jars configured
        File outputJar1 = new File(tempDir.toFile(), "output1.jar");
        File outputJar2 = new File(tempDir.toFile(), "output2.jar");

        task.outjars(outputJar1);
        task.outjars(outputJar2);

        // When: Getting the output jar file collection
        FileCollection result = task.getOutJarFileCollection();

        // Then: The collection should be iterable
        assertNotNull(result, "FileCollection should not be null");
        int count = 0;
        for (File file : result) {
            count++;
            assertNotNull(file, "File in collection should not be null");
        }
        assertEquals(2, count, "Should iterate over 2 files");
    }
}
