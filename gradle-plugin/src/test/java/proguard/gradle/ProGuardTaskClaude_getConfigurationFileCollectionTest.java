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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link ProGuardTask#getConfigurationFileCollection()}.
 *
 * This test class verifies that the getConfigurationFileCollection method correctly
 * returns a FileCollection based on the configuration files configured via the
 * configuration() method.
 */
public class ProGuardTaskClaude_getConfigurationFileCollectionTest {

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
     * Tests that getConfigurationFileCollection returns an empty collection when no
     * configuration files have been configured.
     */
    @Test
    public void testGetConfigurationFileCollection_noConfigurationFiles() {
        // When: No configuration files are configured
        FileCollection result = task.getConfigurationFileCollection();

        // Then: The collection should be empty
        assertNotNull(result, "FileCollection should not be null");
        assertTrue(result.isEmpty(), "FileCollection should be empty when no configuration files are configured");
    }

    /**
     * Tests that getConfigurationFileCollection returns a collection containing a single
     * file when one configuration file is configured with a String path.
     */
    @Test
    public void testGetConfigurationFileCollection_singleConfigFile_StringPath() throws Exception {
        // Given: A single configuration file configured with a String path
        File configFile = new File(tempDir.toFile(), "proguard-rules.pro");
        Files.createFile(configFile.toPath());
        task.configuration(configFile.getAbsolutePath());

        // When: Getting the configuration file collection
        FileCollection result = task.getConfigurationFileCollection();

        // Then: The collection should contain the configured file
        assertNotNull(result, "FileCollection should not be null");
        assertEquals(1, result.getFiles().size(), "FileCollection should contain 1 file");
        assertEquals(configFile.getAbsolutePath(), result.getSingleFile().getAbsolutePath(),
                "File path should match the configured configuration file");
    }

    /**
     * Tests that getConfigurationFileCollection returns a collection containing a single
     * file when one configuration file is configured with a File object.
     */
    @Test
    public void testGetConfigurationFileCollection_singleConfigFile_FileObject() throws Exception {
        // Given: A single configuration file configured with a File object
        File configFile = new File(tempDir.toFile(), "proguard-rules.pro");
        Files.createFile(configFile.toPath());
        task.configuration(configFile);

        // When: Getting the configuration file collection
        FileCollection result = task.getConfigurationFileCollection();

        // Then: The collection should contain the configured file
        assertNotNull(result, "FileCollection should not be null");
        assertEquals(1, result.getFiles().size(), "FileCollection should contain 1 file");
        assertEquals(configFile.getAbsolutePath(), result.getSingleFile().getAbsolutePath(),
                "File path should match the configured configuration file");
    }

    /**
     * Tests that getConfigurationFileCollection returns a collection containing multiple
     * files when multiple configuration files are configured separately.
     */
    @Test
    public void testGetConfigurationFileCollection_multipleConfigFiles() throws Exception {
        // Given: Multiple configuration files configured
        File configFile1 = new File(tempDir.toFile(), "proguard-rules-1.pro");
        File configFile2 = new File(tempDir.toFile(), "proguard-rules-2.pro");
        File configFile3 = new File(tempDir.toFile(), "proguard-rules-3.pro");
        Files.createFile(configFile1.toPath());
        Files.createFile(configFile2.toPath());
        Files.createFile(configFile3.toPath());

        task.configuration(configFile1);
        task.configuration(configFile2);
        task.configuration(configFile3);

        // When: Getting the configuration file collection
        FileCollection result = task.getConfigurationFileCollection();

        // Then: The collection should contain all configured files
        assertNotNull(result, "FileCollection should not be null");
        Set<File> files = result.getFiles();
        assertEquals(3, files.size(), "FileCollection should contain 3 files");

        Set<String> expectedPaths = new HashSet<>(Arrays.asList(
                configFile1.getAbsolutePath(),
                configFile2.getAbsolutePath(),
                configFile3.getAbsolutePath()
        ));
        Set<String> actualPaths = new HashSet<>();
        for (File file : files) {
            actualPaths.add(file.getAbsolutePath());
        }
        assertEquals(expectedPaths, actualPaths, "All configured configuration files should be in the collection");
    }

    /**
     * Tests that getConfigurationFileCollection works correctly when configuration files
     * are configured as a Collection.
     */
    @Test
    public void testGetConfigurationFileCollection_withCollection() throws Exception {
        // Given: Configuration files configured as a Collection
        File configFile1 = new File(tempDir.toFile(), "proguard-rules-1.pro");
        File configFile2 = new File(tempDir.toFile(), "proguard-rules-2.pro");
        Files.createFile(configFile1.toPath());
        Files.createFile(configFile2.toPath());

        List<File> configFiles = new ArrayList<>();
        configFiles.add(configFile1);
        configFiles.add(configFile2);
        task.configuration(configFiles);

        // When: Getting the configuration file collection
        FileCollection result = task.getConfigurationFileCollection();

        // Then: The collection should contain all files from the Collection
        assertNotNull(result, "FileCollection should not be null");
        assertEquals(2, result.getFiles().size(), "FileCollection should contain 2 files");

        Set<String> expectedPaths = new HashSet<>(Arrays.asList(
                configFile1.getAbsolutePath(),
                configFile2.getAbsolutePath()
        ));
        Set<String> actualPaths = new HashSet<>();
        for (File file : result.getFiles()) {
            actualPaths.add(file.getAbsolutePath());
        }
        assertEquals(expectedPaths, actualPaths, "Collection should contain all files from the provided Collection");
    }

    /**
     * Tests that getConfigurationFileCollection handles relative paths correctly.
     */
    @Test
    public void testGetConfigurationFileCollection_relativePath() throws Exception {
        // Given: A configuration file configured with a relative path
        task.configuration("proguard-rules.pro");

        // When: Getting the configuration file collection
        FileCollection result = task.getConfigurationFileCollection();

        // Then: The collection should resolve the relative path
        assertNotNull(result, "FileCollection should not be null");
        assertEquals(1, result.getFiles().size(), "FileCollection should contain 1 file");
        File resolvedFile = result.getSingleFile();
        assertNotNull(resolvedFile, "Resolved file should not be null");
        assertTrue(resolvedFile.getPath().endsWith("proguard-rules.pro"),
                "Resolved path should end with the file name");
    }

    /**
     * Tests that getConfigurationFileCollection can be called multiple times and returns
     * consistent results.
     */
    @Test
    public void testGetConfigurationFileCollection_multipleInvocations() throws Exception {
        // Given: A configuration file configured
        File configFile = new File(tempDir.toFile(), "proguard-rules.pro");
        Files.createFile(configFile.toPath());
        task.configuration(configFile);

        // When: Getting the configuration file collection multiple times
        FileCollection result1 = task.getConfigurationFileCollection();
        FileCollection result2 = task.getConfigurationFileCollection();

        // Then: Both calls should return consistent results
        assertNotNull(result1, "First result should not be null");
        assertNotNull(result2, "Second result should not be null");
        assertEquals(result1.getFiles(), result2.getFiles(),
                "Multiple invocations should return collections with the same files");
    }

    /**
     * Tests that getConfigurationFileCollection reflects changes after additional configuration
     * files are added.
     */
    @Test
    public void testGetConfigurationFileCollection_reflectsSubsequentAdditions() throws Exception {
        // Given: An initial configuration file
        File configFile1 = new File(tempDir.toFile(), "proguard-rules-1.pro");
        Files.createFile(configFile1.toPath());
        task.configuration(configFile1);

        FileCollection initialCollection = task.getConfigurationFileCollection();
        assertEquals(1, initialCollection.getFiles().size(), "Initial collection should have 1 file");

        // When: Adding another configuration file
        File configFile2 = new File(tempDir.toFile(), "proguard-rules-2.pro");
        Files.createFile(configFile2.toPath());
        task.configuration(configFile2);

        // Then: The collection should reflect the addition
        FileCollection updatedCollection = task.getConfigurationFileCollection();
        assertEquals(2, updatedCollection.getFiles().size(),
                "Updated collection should have 2 files after addition");
    }

    /**
     * Tests that the FileCollection returned by getConfigurationFileCollection is iterable.
     */
    @Test
    public void testGetConfigurationFileCollection_isIterable() throws Exception {
        // Given: Multiple configuration files configured
        File configFile1 = new File(tempDir.toFile(), "proguard-rules-1.pro");
        File configFile2 = new File(tempDir.toFile(), "proguard-rules-2.pro");
        Files.createFile(configFile1.toPath());
        Files.createFile(configFile2.toPath());

        task.configuration(configFile1);
        task.configuration(configFile2);

        // When: Getting the configuration file collection
        FileCollection result = task.getConfigurationFileCollection();

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
     * Tests that getConfigurationFileCollection handles typical ProGuard configuration file names.
     */
    @Test
    public void testGetConfigurationFileCollection_typicalConfigFileNames() throws Exception {
        // Given: Typical ProGuard configuration files
        File proguardRules = new File(tempDir.toFile(), "proguard-rules.pro");
        File proguardProject = new File(tempDir.toFile(), "proguard-project.txt");
        File proguardCfg = new File(tempDir.toFile(), "proguard.cfg");
        Files.createFile(proguardRules.toPath());
        Files.createFile(proguardProject.toPath());
        Files.createFile(proguardCfg.toPath());

        task.configuration(proguardRules);
        task.configuration(proguardProject);
        task.configuration(proguardCfg);

        // When: Getting the configuration file collection
        FileCollection result = task.getConfigurationFileCollection();

        // Then: All configuration files should be included
        assertNotNull(result, "FileCollection should not be null");
        assertEquals(3, result.getFiles().size(), "FileCollection should contain all 3 configuration files");
    }

    /**
     * Tests that getConfigurationFileCollection handles mixing single files and Collections.
     */
    @Test
    public void testGetConfigurationFileCollection_mixedSingleAndCollection() throws Exception {
        // Given: Mix of single file and Collection
        File configFile1 = new File(tempDir.toFile(), "proguard-rules-1.pro");
        File configFile2 = new File(tempDir.toFile(), "proguard-rules-2.pro");
        File configFile3 = new File(tempDir.toFile(), "proguard-rules-3.pro");
        Files.createFile(configFile1.toPath());
        Files.createFile(configFile2.toPath());
        Files.createFile(configFile3.toPath());

        task.configuration(configFile1);

        List<File> configFiles = new ArrayList<>();
        configFiles.add(configFile2);
        configFiles.add(configFile3);
        task.configuration(configFiles);

        // When: Getting the configuration file collection
        FileCollection result = task.getConfigurationFileCollection();

        // Then: All configuration files should be included
        assertNotNull(result, "FileCollection should not be null");
        assertEquals(3, result.getFiles().size(), "FileCollection should contain all 3 files");
    }

    /**
     * Tests that configuration files are kept separate from other file types.
     */
    @Test
    public void testGetConfigurationFileCollection_separateFromOtherFiles() throws Exception {
        // Given: Configuration files, library jars, and input/output jars configured
        File configFile = new File(tempDir.toFile(), "proguard-rules.pro");
        File libraryJar = new File(tempDir.toFile(), "library.jar");
        File inputJar = new File(tempDir.toFile(), "input.jar");
        File outputJar = new File(tempDir.toFile(), "output.jar");
        Files.createFile(configFile.toPath());

        task.configuration(configFile);
        task.libraryjars(libraryJar);
        task.injars(inputJar);
        task.outjars(outputJar);

        // When: Getting the configuration file collection
        FileCollection configResult = task.getConfigurationFileCollection();
        FileCollection libraryResult = task.getLibraryJarFileCollection();
        FileCollection inputResult = task.getInJarFileCollection();
        FileCollection outputResult = task.getOutJarFileCollection();

        // Then: Configuration collection should only contain configuration files
        assertNotNull(configResult, "Configuration FileCollection should not be null");
        assertEquals(1, configResult.getFiles().size(), "Configuration collection should contain only 1 file");
        assertEquals(configFile.getAbsolutePath(), configResult.getSingleFile().getAbsolutePath(),
                "Configuration collection should contain the configuration file");

        // And: Other collections should not contain the configuration file
        assertFalse(libraryResult.contains(configFile), "Library collection should not contain configuration file");
        assertFalse(inputResult.contains(configFile), "Input collection should not contain configuration file");
        assertFalse(outputResult.contains(configFile), "Output collection should not contain configuration file");
    }

    /**
     * Tests that getConfigurationFileCollection works with empty Collection.
     */
    @Test
    public void testGetConfigurationFileCollection_emptyCollection() throws Exception {
        // Given: An empty Collection configured
        List<File> emptyList = new ArrayList<>();
        task.configuration(emptyList);

        // When: Getting the configuration file collection
        FileCollection result = task.getConfigurationFileCollection();

        // Then: The collection should be empty
        assertNotNull(result, "FileCollection should not be null");
        assertTrue(result.isEmpty(), "FileCollection should be empty when an empty Collection is provided");
    }

    /**
     * Tests that getConfigurationFileCollection handles non-existent files.
     * Configuration files don't need to exist at configuration time as they might be generated.
     */
    @Test
    public void testGetConfigurationFileCollection_nonExistentFile() throws Exception {
        // Given: A configuration file that doesn't exist yet
        File nonExistentFile = new File(tempDir.toFile(), "generated-rules.pro");
        task.configuration(nonExistentFile);

        // When: Getting the configuration file collection
        FileCollection result = task.getConfigurationFileCollection();

        // Then: The collection should still contain the path
        assertNotNull(result, "FileCollection should not be null");
        assertEquals(1, result.getFiles().size(), "FileCollection should contain 1 file");
        assertEquals(nonExistentFile.getAbsolutePath(), result.getSingleFile().getAbsolutePath(),
                "File path should match even if file doesn't exist yet");
    }
}
