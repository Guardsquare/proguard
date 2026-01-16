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
 * Tests for {@link ProGuardTask#getLibraryJarFileCollection()}.
 *
 * This test class verifies that the getLibraryJarFileCollection method correctly
 * returns a FileCollection based on the library jar files configured via the
 * libraryjars() method.
 */
public class ProGuardTaskClaude_getLibraryJarFileCollectionTest {

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
     * Tests that getLibraryJarFileCollection returns an empty collection when no
     * library jars have been configured.
     */
    @Test
    public void testGetLibraryJarFileCollection_noLibraryJars() {
        // When: No library jars are configured
        FileCollection result = task.getLibraryJarFileCollection();

        // Then: The collection should be empty
        assertNotNull(result, "FileCollection should not be null");
        assertTrue(result.isEmpty(), "FileCollection should be empty when no library jars are configured");
    }

    /**
     * Tests that getLibraryJarFileCollection returns a collection containing a single
     * file when one library jar is configured with a String path.
     */
    @Test
    public void testGetLibraryJarFileCollection_singleLibraryJar_StringPath() throws Exception {
        // Given: A single library jar configured with a String path
        File libraryJar = new File(tempDir.toFile(), "library.jar");
        task.libraryjars(libraryJar.getAbsolutePath());

        // When: Getting the library jar file collection
        FileCollection result = task.getLibraryJarFileCollection();

        // Then: The collection should contain the configured file
        assertNotNull(result, "FileCollection should not be null");
        assertEquals(1, result.getFiles().size(), "FileCollection should contain 1 file");
        assertEquals(libraryJar.getAbsolutePath(), result.getSingleFile().getAbsolutePath(),
                "File path should match the configured library jar");
    }

    /**
     * Tests that getLibraryJarFileCollection returns a collection containing a single
     * file when one library jar is configured with a File object.
     */
    @Test
    public void testGetLibraryJarFileCollection_singleLibraryJar_FileObject() throws Exception {
        // Given: A single library jar configured with a File object
        File libraryJar = new File(tempDir.toFile(), "library.jar");
        task.libraryjars(libraryJar);

        // When: Getting the library jar file collection
        FileCollection result = task.getLibraryJarFileCollection();

        // Then: The collection should contain the configured file
        assertNotNull(result, "FileCollection should not be null");
        assertEquals(1, result.getFiles().size(), "FileCollection should contain 1 file");
        assertEquals(libraryJar.getAbsolutePath(), result.getSingleFile().getAbsolutePath(),
                "File path should match the configured library jar");
    }

    /**
     * Tests that getLibraryJarFileCollection returns a collection containing multiple
     * files when multiple library jars are configured.
     */
    @Test
    public void testGetLibraryJarFileCollection_multipleLibraryJars() throws Exception {
        // Given: Multiple library jars configured
        File libraryJar1 = new File(tempDir.toFile(), "library1.jar");
        File libraryJar2 = new File(tempDir.toFile(), "library2.jar");
        File libraryJar3 = new File(tempDir.toFile(), "library3.jar");

        task.libraryjars(libraryJar1);
        task.libraryjars(libraryJar2);
        task.libraryjars(libraryJar3);

        // When: Getting the library jar file collection
        FileCollection result = task.getLibraryJarFileCollection();

        // Then: The collection should contain all configured files
        assertNotNull(result, "FileCollection should not be null");
        Set<File> files = result.getFiles();
        assertEquals(3, files.size(), "FileCollection should contain 3 files");

        Set<String> expectedPaths = new HashSet<>(Arrays.asList(
                libraryJar1.getAbsolutePath(),
                libraryJar2.getAbsolutePath(),
                libraryJar3.getAbsolutePath()
        ));
        Set<String> actualPaths = new HashSet<>();
        for (File file : files) {
            actualPaths.add(file.getAbsolutePath());
        }
        assertEquals(expectedPaths, actualPaths, "All configured library jars should be in the collection");
    }

    /**
     * Tests that getLibraryJarFileCollection works correctly when library jars are
     * configured with filter arguments.
     */
    @Test
    public void testGetLibraryJarFileCollection_withFilterArgs() throws Exception {
        // Given: Library jar configured with filter arguments
        File libraryJar = new File(tempDir.toFile(), "library.jar");
        java.util.Map<String, String> filterArgs = new java.util.HashMap<>();
        filterArgs.put("filter", "!**.class");
        task.libraryjars(filterArgs, libraryJar);

        // When: Getting the library jar file collection
        FileCollection result = task.getLibraryJarFileCollection();

        // Then: The collection should contain the configured file
        // Note: Filters don't affect which files are in the collection, only how they're processed
        assertNotNull(result, "FileCollection should not be null");
        assertEquals(1, result.getFiles().size(), "FileCollection should contain 1 file");
        assertEquals(libraryJar.getAbsolutePath(), result.getSingleFile().getAbsolutePath(),
                "File path should match the configured library jar");
    }

    /**
     * Tests that getLibraryJarFileCollection handles relative paths correctly.
     */
    @Test
    public void testGetLibraryJarFileCollection_relativePath() throws Exception {
        // Given: A library jar configured with a relative path
        task.libraryjars("build/libs/library.jar");

        // When: Getting the library jar file collection
        FileCollection result = task.getLibraryJarFileCollection();

        // Then: The collection should resolve the relative path
        assertNotNull(result, "FileCollection should not be null");
        assertEquals(1, result.getFiles().size(), "FileCollection should contain 1 file");
        File resolvedFile = result.getSingleFile();
        assertNotNull(resolvedFile, "Resolved file should not be null");
        assertTrue(resolvedFile.getPath().endsWith("library.jar"),
                "Resolved path should end with the file name");
    }

    /**
     * Tests that getLibraryJarFileCollection handles directory paths for library jars.
     */
    @Test
    public void testGetLibraryJarFileCollection_directoryPath() throws Exception {
        // Given: A directory configured as library
        File libraryDir = new File(tempDir.toFile(), "libs");
        libraryDir.mkdirs();
        task.libraryjars(libraryDir);

        // When: Getting the library jar file collection
        FileCollection result = task.getLibraryJarFileCollection();

        // Then: The collection should contain the directory
        assertNotNull(result, "FileCollection should not be null");
        assertEquals(1, result.getFiles().size(), "FileCollection should contain 1 entry");
        assertEquals(libraryDir.getAbsolutePath(), result.getSingleFile().getAbsolutePath(),
                "Path should match the configured library directory");
    }

    /**
     * Tests that getLibraryJarFileCollection can be called multiple times and returns
     * consistent results.
     */
    @Test
    public void testGetLibraryJarFileCollection_multipleInvocations() throws Exception {
        // Given: A library jar configured
        File libraryJar = new File(tempDir.toFile(), "library.jar");
        task.libraryjars(libraryJar);

        // When: Getting the library jar file collection multiple times
        FileCollection result1 = task.getLibraryJarFileCollection();
        FileCollection result2 = task.getLibraryJarFileCollection();

        // Then: Both calls should return consistent results
        assertNotNull(result1, "First result should not be null");
        assertNotNull(result2, "Second result should not be null");
        assertEquals(result1.getFiles(), result2.getFiles(),
                "Multiple invocations should return collections with the same files");
    }

    /**
     * Tests that getLibraryJarFileCollection reflects changes after additional library
     * jars are added.
     */
    @Test
    public void testGetLibraryJarFileCollection_reflectsSubsequentAdditions() throws Exception {
        // Given: An initial library jar
        File libraryJar1 = new File(tempDir.toFile(), "library1.jar");
        task.libraryjars(libraryJar1);

        FileCollection initialCollection = task.getLibraryJarFileCollection();
        assertEquals(1, initialCollection.getFiles().size(), "Initial collection should have 1 file");

        // When: Adding another library jar
        File libraryJar2 = new File(tempDir.toFile(), "library2.jar");
        task.libraryjars(libraryJar2);

        // Then: The collection should reflect the addition
        FileCollection updatedCollection = task.getLibraryJarFileCollection();
        assertEquals(2, updatedCollection.getFiles().size(),
                "Updated collection should have 2 files after addition");
    }

    /**
     * Tests that getLibraryJarFileCollection handles mixed file types.
     */
    @Test
    public void testGetLibraryJarFileCollection_mixedFileTypes() throws Exception {
        // Given: Multiple library files with different extensions
        File libraryJar = new File(tempDir.toFile(), "library.jar");
        File libraryAar = new File(tempDir.toFile(), "library.aar");
        File libraryZip = new File(tempDir.toFile(), "library.zip");

        task.libraryjars(libraryJar);
        task.libraryjars(libraryAar);
        task.libraryjars(libraryZip);

        // When: Getting the library jar file collection
        FileCollection result = task.getLibraryJarFileCollection();

        // Then: All file types should be included
        assertNotNull(result, "FileCollection should not be null");
        assertEquals(3, result.getFiles().size(), "FileCollection should contain all 3 files");
    }

    /**
     * Tests that the FileCollection returned by getLibraryJarFileCollection is iterable.
     */
    @Test
    public void testGetLibraryJarFileCollection_isIterable() throws Exception {
        // Given: Multiple library jars configured
        File libraryJar1 = new File(tempDir.toFile(), "library1.jar");
        File libraryJar2 = new File(tempDir.toFile(), "library2.jar");

        task.libraryjars(libraryJar1);
        task.libraryjars(libraryJar2);

        // When: Getting the library jar file collection
        FileCollection result = task.getLibraryJarFileCollection();

        // Then: The collection should be iterable
        assertNotNull(result, "FileCollection should not be null");
        int count = 0;
        for (File file : result) {
            count++;
            assertNotNull(file, "File in collection should not be null");
        }
        assertEquals(2, count, "Should iterate over 2 files");
    }

    /**
     * Tests that getLibraryJarFileCollection works with typical Android runtime library paths.
     */
    @Test
    public void testGetLibraryJarFileCollection_androidRuntimePath() throws Exception {
        // Given: A typical Android runtime library path
        task.libraryjars("${android.sdk}/platforms/android-30/android.jar");

        // When: Getting the library jar file collection
        FileCollection result = task.getLibraryJarFileCollection();

        // Then: The collection should contain the configured path
        assertNotNull(result, "FileCollection should not be null");
        assertEquals(1, result.getFiles().size(), "FileCollection should contain 1 file");
        File resolvedFile = result.getSingleFile();
        assertNotNull(resolvedFile, "Resolved file should not be null");
    }

    /**
     * Tests that getLibraryJarFileCollection handles multiple filter types.
     */
    @Test
    public void testGetLibraryJarFileCollection_multipleFilterTypes() throws Exception {
        // Given: Multiple library jars with different filter types
        File libraryJar1 = new File(tempDir.toFile(), "library1.jar");
        File libraryJar2 = new File(tempDir.toFile(), "library2.jar");

        java.util.Map<String, String> filterArgs1 = new java.util.HashMap<>();
        filterArgs1.put("jarfilter", "!META-INF/**");
        task.libraryjars(filterArgs1, libraryJar1);

        java.util.Map<String, String> filterArgs2 = new java.util.HashMap<>();
        filterArgs2.put("filter", "!**.properties");
        task.libraryjars(filterArgs2, libraryJar2);

        // When: Getting the library jar file collection
        FileCollection result = task.getLibraryJarFileCollection();

        // Then: The collection should contain both files
        assertNotNull(result, "FileCollection should not be null");
        assertEquals(2, result.getFiles().size(), "FileCollection should contain 2 files");
    }

    /**
     * Tests that library jars and input/output jars are kept separate.
     */
    @Test
    public void testGetLibraryJarFileCollection_separateFromInputOutput() throws Exception {
        // Given: Both library jars and input/output jars configured
        File libraryJar = new File(tempDir.toFile(), "library.jar");
        File inputJar = new File(tempDir.toFile(), "input.jar");
        File outputJar = new File(tempDir.toFile(), "output.jar");

        task.libraryjars(libraryJar);
        task.injars(inputJar);
        task.outjars(outputJar);

        // When: Getting the library jar file collection
        FileCollection libraryResult = task.getLibraryJarFileCollection();
        FileCollection inputResult = task.getInJarFileCollection();
        FileCollection outputResult = task.getOutJarFileCollection();

        // Then: Library collection should only contain library jars
        assertNotNull(libraryResult, "Library FileCollection should not be null");
        assertEquals(1, libraryResult.getFiles().size(), "Library collection should contain only 1 file");
        assertEquals(libraryJar.getAbsolutePath(), libraryResult.getSingleFile().getAbsolutePath(),
                "Library collection should contain the library jar");

        // And: Input and output collections should not contain the library jar
        assertFalse(inputResult.contains(libraryJar), "Input collection should not contain library jar");
        assertFalse(outputResult.contains(libraryJar), "Output collection should not contain library jar");
    }
}
