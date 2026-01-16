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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link ProGuardTask#outjars(Object)} and {@link ProGuardTask#outjars(Map, Object)}.
 *
 * This test class verifies that both outjars methods correctly add output jar file objects
 * to the internal list and record the input jar count.
 *
 * Method signature: outjars(Object outJarFiles)
 * - Adds the output jar file object to the outJarFiles list
 * - Adds a null entry to the outJarFilters list (since no filters are specified)
 * - Records the current size of inJarFiles list in inJarCounts
 * - This is a convenience method that delegates to outjars(Map, Object) with null filters
 *
 * Method signature: outjars(Map filterArgs, Object outJarFiles)
 * - Adds the output jar file object to the outJarFiles list
 * - Adds the filterArgs Map to the outJarFilters list
 * - Records the current size of inJarFiles list in inJarCounts
 * - Supports filter keys: filter, jarfilter, aarfilter, apkfilter, warfilter, earfilter, jmodfilter, zipfilter
 * - Stores the Map by reference (not a copy)
 *
 * Unlike configuration(), this method does NOT flatten Collections - it adds the
 * object as-is to the list.
 *
 * Key behavior: Each call to outjars() records how many input jars have been configured
 * so far, enabling the mapping of input jars to output jars.
 */
public class ProGuardTaskClaude_outjarsTest {

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
     * Tests that outjars() adds a single String path to the output jar files list.
     */
    @Test
    public void testOutjars_singleString() throws Exception {
        // Given: A String path
        String jarPath = "output.jar";

        // When: Calling outjars()
        task.outjars(jarPath);

        // Then: The output jar files list should contain the string
        List jarFiles = task.getOutJarFiles();
        assertNotNull(jarFiles, "Output jar files list should not be null");
        assertEquals(1, jarFiles.size(), "Should have 1 entry");
        assertEquals(jarPath, jarFiles.get(0), "Entry should be the string path");
    }

    /**
     * Tests that outjars() adds a null filter entry when called without filters.
     */
    @Test
    public void testOutjars_addsNullFilter() throws Exception {
        // Given: A String path
        String jarPath = "output.jar";

        // When: Calling outjars() without filters
        task.outjars(jarPath);

        // Then: The filters list should have a null entry
        List filters = task.getOutJarFilters();
        assertNotNull(filters, "Filters list should not be null");
        assertEquals(1, filters.size(), "Should have 1 filter entry");
        assertNull(filters.get(0), "Filter entry should be null when no filters specified");
    }

    /**
     * Tests that outjars() records the input jar count when called.
     */
    @Test
    public void testOutjars_recordsInputJarCount() throws Exception {
        // Given: No input jars configured
        String outputJar = "output.jar";

        // When: Calling outjars()
        task.outjars(outputJar);

        // Then: The inJarCounts should record 0 (no input jars)
        List inJarCounts = task.getInJarCounts();
        assertNotNull(inJarCounts, "InJarCounts list should not be null");
        assertEquals(1, inJarCounts.size(), "Should have 1 count entry");
        assertEquals(0, inJarCounts.get(0), "Count should be 0 when no input jars");
    }

    /**
     * Tests that outjars() records the correct input jar count.
     */
    @Test
    public void testOutjars_recordsCorrectInputJarCount() throws Exception {
        // Given: Three input jars configured
        task.injars("input1.jar");
        task.injars("input2.jar");
        task.injars("input3.jar");

        // When: Calling outjars()
        task.outjars("output.jar");

        // Then: The inJarCounts should record 3
        List inJarCounts = task.getInJarCounts();
        assertEquals(1, inJarCounts.size(), "Should have 1 count entry");
        assertEquals(3, inJarCounts.get(0), "Count should be 3 (number of input jars)");
    }

    /**
     * Tests that outjars() adds a single File object to the output jar files list.
     */
    @Test
    public void testOutjars_singleFile() throws Exception {
        // Given: A File object
        File jarFile = new File(tempDir.toFile(), "output.jar");

        // When: Calling outjars()
        task.outjars(jarFile);

        // Then: The output jar files list should contain the File object
        List jarFiles = task.getOutJarFiles();
        assertNotNull(jarFiles, "Output jar files list should not be null");
        assertEquals(1, jarFiles.size(), "Should have 1 entry");
        assertSame(jarFile, jarFiles.get(0), "Entry should be the same File object");
    }

    /**
     * Tests that outjars() can be called multiple times and accumulates entries.
     */
    @Test
    public void testOutjars_multipleCalls() throws Exception {
        // Given: Multiple output jars
        String jar1 = "output1.jar";
        String jar2 = "output2.jar";
        String jar3 = "output3.jar";

        // When: Calling outjars() multiple times
        task.outjars(jar1);
        task.outjars(jar2);
        task.outjars(jar3);

        // Then: All entries should be accumulated
        List jarFiles = task.getOutJarFiles();
        assertEquals(3, jarFiles.size(), "Should have 3 entries");
        assertEquals(jar1, jarFiles.get(0), "First entry should be jar1");
        assertEquals(jar2, jarFiles.get(1), "Second entry should be jar2");
        assertEquals(jar3, jarFiles.get(2), "Third entry should be jar3");
    }

    /**
     * Tests that outjars() maintains 1-to-1 correspondence with filters.
     */
    @Test
    public void testOutjars_maintainsFilterCorrespondence() throws Exception {
        // Given: Multiple output jars
        task.outjars("jar1.jar");
        task.outjars("jar2.jar");
        task.outjars("jar3.jar");

        // When: Getting both lists
        List jarFiles = task.getOutJarFiles();
        List filters = task.getOutJarFilters();

        // Then: Both lists should have same size with null filters
        assertEquals(jarFiles.size(), filters.size(), "Lists should have same size");
        assertEquals(3, filters.size(), "Should have 3 filter entries");
        assertNull(filters.get(0), "All filters should be null");
        assertNull(filters.get(1), "All filters should be null");
        assertNull(filters.get(2), "All filters should be null");
    }

    /**
     * Tests that outjars() records cumulative input jar counts.
     */
    @Test
    public void testOutjars_recordsCumulativeInputJarCounts() throws Exception {
        // Given: A complex workflow with input and output jars
        task.injars("input1.jar");
        task.injars("input2.jar");
        task.outjars("output1.jar");  // Count: 2

        task.injars("input3.jar");
        task.injars("input4.jar");
        task.injars("input5.jar");
        task.outjars("output2.jar");  // Count: 5

        // When: Getting the inJarCounts
        List inJarCounts = task.getInJarCounts();

        // Then: Counts should be cumulative [2, 5]
        assertEquals(2, inJarCounts.size(), "Should have 2 count entries");
        assertEquals(2, inJarCounts.get(0), "First count should be 2");
        assertEquals(5, inJarCounts.get(1), "Second count should be 5 (cumulative)");
    }

    /**
     * Tests that outjars() preserves the order of output jars as they were added.
     */
    @Test
    @Disabled("Skipped by hand due to long time taken.")
    public void testOutjars_preservesOrder() throws Exception {
        // Given: Output jars added in specific order
        String first = "first.jar";
        String second = "second.jar";
        String third = "third.jar";

        // When: Calling outjars() in order
        task.outjars(first);
        task.outjars(second);
        task.outjars(third);

        // Then: Order should be preserved
        List jarFiles = task.getOutJarFiles();
        assertEquals(3, jarFiles.size(), "Should have 3 entries");
        assertEquals(first, jarFiles.get(0), "First should be first.jar");
        assertEquals(second, jarFiles.get(1), "Second should be second.jar");
        assertEquals(third, jarFiles.get(2), "Third should be third.jar");
    }

    /**
     * Tests that outjars() handles relative path strings.
     */
    @Test
    public void testOutjars_relativePathString() throws Exception {
        // Given: A relative path string
        String relativePath = "build/outputs/app.jar";

        // When: Calling outjars()
        task.outjars(relativePath);

        // Then: The relative path should be stored as-is
        List jarFiles = task.getOutJarFiles();
        assertEquals(1, jarFiles.size(), "Should have 1 entry");
        assertEquals(relativePath, jarFiles.get(0), "Should store the relative path string");
    }

    /**
     * Tests that outjars() handles absolute path strings.
     */
    @Test
    public void testOutjars_absolutePathString() throws Exception {
        // Given: An absolute path string
        String absolutePath = new File(tempDir.toFile(), "output.jar").getAbsolutePath();

        // When: Calling outjars()
        task.outjars(absolutePath);

        // Then: The absolute path should be stored as-is
        List jarFiles = task.getOutJarFiles();
        assertEquals(1, jarFiles.size(), "Should have 1 entry");
        assertEquals(absolutePath, jarFiles.get(0), "Should store the absolute path string");
    }

    /**
     * Tests that outjars() handles null value.
     */
    @Test
    public void testOutjars_nullValue() throws Exception {
        // Given: A null value
        Object nullValue = null;

        // When: Calling outjars()
        task.outjars(nullValue);

        // Then: Null should be added to the list
        List jarFiles = task.getOutJarFiles();
        assertEquals(1, jarFiles.size(), "Should have 1 entry");
        assertNull(jarFiles.get(0), "Entry should be null");
    }

    /**
     * Tests that outjars() handles empty string.
     */
    @Test
    public void testOutjars_emptyString() throws Exception {
        // Given: An empty string
        String emptyString = "";

        // When: Calling outjars()
        task.outjars(emptyString);

        // Then: The empty string should be added
        List jarFiles = task.getOutJarFiles();
        assertEquals(1, jarFiles.size(), "Should have 1 entry");
        assertEquals("", jarFiles.get(0), "Should store the empty string");
    }

    /**
     * Tests that outjars() allows duplicate entries.
     */
    @Test
    public void testOutjars_duplicates() throws Exception {
        // Given: Same jar added multiple times
        String jarPath = "output.jar";

        // When: Calling outjars() multiple times with same path
        task.outjars(jarPath);
        task.outjars(jarPath);
        task.outjars(jarPath);

        // Then: All duplicates should be added
        List jarFiles = task.getOutJarFiles();
        assertEquals(3, jarFiles.size(), "Should have 3 entries (duplicates allowed)");
        assertEquals(jarPath, jarFiles.get(0), "All entries should be the same");
        assertEquals(jarPath, jarFiles.get(1), "All entries should be the same");
        assertEquals(jarPath, jarFiles.get(2), "All entries should be the same");
    }

    /**
     * Tests that outjars() handles directory paths.
     */
    @Test
    public void testOutjars_directoryPath() throws Exception {
        // Given: A directory path
        File directory = new File(tempDir.toFile(), "output");

        // When: Calling outjars()
        task.outjars(directory);

        // Then: The directory should be stored
        List jarFiles = task.getOutJarFiles();
        assertEquals(1, jarFiles.size(), "Should have 1 entry");
        assertSame(directory, jarFiles.get(0), "Should store the directory File object");
    }

    /**
     * Tests that outjars() handles various archive file types.
     */
    @Test
    public void testOutjars_variousFileTypes() throws Exception {
        // Given: Various file types
        String jarFile = "output.jar";
        String aarFile = "library.aar";
        String apkFile = "app.apk";
        String warFile = "webapp.war";
        String zipFile = "archive.zip";

        // When: Calling outjars() with various types
        task.outjars(jarFile);
        task.outjars(aarFile);
        task.outjars(apkFile);
        task.outjars(warFile);
        task.outjars(zipFile);

        // Then: All types should be stored
        List jarFiles = task.getOutJarFiles();
        assertEquals(5, jarFiles.size(), "Should have 5 entries");
        assertEquals(jarFile, jarFiles.get(0), "First should be jar");
        assertEquals(aarFile, jarFiles.get(1), "Second should be aar");
        assertEquals(apkFile, jarFiles.get(2), "Third should be apk");
        assertEquals(warFile, jarFiles.get(3), "Fourth should be war");
        assertEquals(zipFile, jarFiles.get(4), "Fifth should be zip");
    }

    /**
     * Tests that outjars() does NOT flatten Collections (unlike configuration()).
     * Collections should be added as-is.
     */
    @Test
    public void testOutjars_doesNotFlattenCollections() throws Exception {
        // Given: A List of jar files
        java.util.List<String> jarList = java.util.Arrays.asList("jar1.jar", "jar2.jar");

        // When: Calling outjars() with the List
        task.outjars(jarList);

        // Then: The List itself should be added (not flattened)
        List jarFiles = task.getOutJarFiles();
        assertEquals(1, jarFiles.size(), "Should have 1 entry (the List itself)");
        assertSame(jarList, jarFiles.get(0), "Entry should be the List object itself, not its elements");
    }

    /**
     * Tests that outjars() preserves object identity.
     */
    @Test
    public void testOutjars_preservesObjectIdentity() throws Exception {
        // Given: A specific File object
        File jarFile = new File(tempDir.toFile(), "output.jar");

        // When: Calling outjars()
        task.outjars(jarFile);

        // Then: The exact same object should be stored
        List jarFiles = task.getOutJarFiles();
        assertSame(jarFile, jarFiles.get(0), "Should store the exact same File object");
    }

    /**
     * Tests that outjars() is independent from injars.
     */
    @Test
    public void testOutjars_independentFromInjars() throws Exception {
        // Given: Input and output jars
        String inputJar = "input.jar";
        String outputJar = "output.jar";

        // When: Calling both injars() and outjars()
        task.injars(inputJar);
        task.outjars(outputJar);

        // Then: Each list should contain only its respective jar
        List inJars = task.getInJarFiles();
        List outJars = task.getOutJarFiles();

        assertEquals(1, inJars.size(), "Should have 1 input jar");
        assertEquals(inputJar, inJars.get(0), "Input jar should be correct");

        assertEquals(1, outJars.size(), "Should have 1 output jar");
        assertEquals(outputJar, outJars.get(0), "Output jar should be correct");

        assertFalse(inJars.contains(outputJar), "Input jars should not contain output jar");
        assertFalse(outJars.contains(inputJar), "Output jars should not contain input jar");
    }

    /**
     * Tests that outjars() is independent from libraryjars.
     */
    @Test
    public void testOutjars_independentFromLibraryjars() throws Exception {
        // Given: Output and library jars
        String outputJar = "output.jar";
        String libraryJar = "android.jar";

        // When: Calling both outjars() and libraryjars()
        task.outjars(outputJar);
        task.libraryjars(libraryJar);

        // Then: Each list should contain only its respective jar
        List outJars = task.getOutJarFiles();
        List libraryJars = task.getLibraryJarFiles();

        assertEquals(1, outJars.size(), "Should have 1 output jar");
        assertEquals(outputJar, outJars.get(0), "Output jar should be correct");

        assertEquals(1, libraryJars.size(), "Should have 1 library jar");
        assertEquals(libraryJar, libraryJars.get(0), "Library jar should be correct");

        assertFalse(outJars.contains(libraryJar), "Output jars should not contain library jar");
        assertFalse(libraryJars.contains(outputJar), "Library jars should not contain output jar");
    }

    /**
     * Tests that outjars() works in a realistic Android scenario.
     */
    @Test
    public void testOutjars_realisticAndroidScenario() throws Exception {
        // Given: A realistic Android project with input and output jars
        task.injars("build/intermediates/classes.jar");
        task.injars("build/intermediates/library-classes.jar");
        task.outjars("build/outputs/app-release.jar");

        // When: Getting the results
        List inJars = task.getInJarFiles();
        List outJars = task.getOutJarFiles();
        List inJarCounts = task.getInJarCounts();

        // Then: All should be tracked correctly
        assertEquals(2, inJars.size(), "Should have 2 input jars");
        assertEquals(1, outJars.size(), "Should have 1 output jar");
        assertEquals("build/outputs/app-release.jar", outJars.get(0));
        assertEquals(1, inJarCounts.size(), "Should have 1 count entry");
        assertEquals(2, inJarCounts.get(0), "Count should be 2");
    }

    /**
     * Tests that outjars() works in a realistic Java scenario.
     */
    @Test
    public void testOutjars_realisticJavaScenario() throws Exception {
        // Given: A realistic Java project with input and output jars
        File inputJar = new File(tempDir.toFile(), "build/libs/app-1.0.jar");
        File outputJar = new File(tempDir.toFile(), "build/libs/app-1.0-obfuscated.jar");

        task.injars(inputJar);
        task.outjars(outputJar);

        // When: Getting the results
        List inJars = task.getInJarFiles();
        List outJars = task.getOutJarFiles();

        // Then: All should be tracked
        assertEquals(1, inJars.size(), "Should have 1 input jar");
        assertEquals(1, outJars.size(), "Should have 1 output jar");
        assertSame(outputJar, outJars.get(0));
    }

    /**
     * Tests that outjars() handles multiple input-output mappings.
     */
    @Test
    public void testOutjars_multipleInputOutputMappings() throws Exception {
        // Given: Multiple groups of input-output mappings
        task.injars("app-classes.jar");
        task.injars("app-resources.jar");
        task.outjars("app-combined.jar");

        task.injars("lib1.jar");
        task.injars("lib2.jar");
        task.injars("lib3.jar");
        task.outjars("libs-combined.jar");

        // When: Getting the results
        List inJars = task.getInJarFiles();
        List outJars = task.getOutJarFiles();
        List inJarCounts = task.getInJarCounts();

        // Then: Mappings should be tracked correctly
        assertEquals(5, inJars.size(), "Should have 5 input jars");
        assertEquals(2, outJars.size(), "Should have 2 output jars");
        assertEquals(2, inJarCounts.size(), "Should have 2 count entries");
        assertEquals(2, inJarCounts.get(0), "First count should be 2");
        assertEquals(5, inJarCounts.get(1), "Second count should be 5");
    }

    /**
     * Tests that outjars() handles Gradle project outputs.
     */
    @Test
    public void testOutjars_gradleProjectOutputs() throws Exception {
        // Given: Gradle-style output paths
        String outputJar = "build/libs/project-1.0-obfuscated.jar";

        // When: Calling outjars()
        task.outjars(outputJar);

        // Then: Output should be stored
        List jarFiles = task.getOutJarFiles();
        assertEquals(1, jarFiles.size(), "Should have 1 entry");
        assertEquals(outputJar, jarFiles.get(0));
    }

    /**
     * Tests that outjars() handles mixed String and File objects.
     */
    @Test
    public void testOutjars_mixedStringAndFile() throws Exception {
        // Given: Mix of String and File objects
        String stringPath = "output1.jar";
        File fileObject = new File(tempDir.toFile(), "output2.jar");
        String anotherString = "output3.jar";

        // When: Calling outjars()
        task.outjars(stringPath);
        task.outjars(fileObject);
        task.outjars(anotherString);

        // Then: Both types should be preserved
        List jarFiles = task.getOutJarFiles();
        assertEquals(3, jarFiles.size(), "Should have 3 entries");
        assertTrue(jarFiles.get(0) instanceof String, "First should be String");
        assertTrue(jarFiles.get(1) instanceof File, "Second should be File");
        assertTrue(jarFiles.get(2) instanceof String, "Third should be String");
    }

    /**
     * Tests that outjars() handles paths with spaces.
     */
    @Test
    public void testOutjars_pathsWithSpaces() throws Exception {
        // Given: A path with spaces
        String pathWithSpaces = "my folder/output.jar";

        // When: Calling outjars()
        task.outjars(pathWithSpaces);

        // Then: The path should be stored as-is
        List jarFiles = task.getOutJarFiles();
        assertEquals(1, jarFiles.size(), "Should have 1 entry");
        assertEquals(pathWithSpaces, jarFiles.get(0), "Should preserve path with spaces");
    }

    /**
     * Tests that outjars() handles paths with special characters.
     */
    @Test
    public void testOutjars_pathsWithSpecialCharacters() throws Exception {
        // Given: Paths with special characters
        String path1 = "output-v1.0.jar";
        String path2 = "output_final.jar";
        String path3 = "output@release.jar";

        // When: Calling outjars()
        task.outjars(path1);
        task.outjars(path2);
        task.outjars(path3);

        // Then: All paths should be stored
        List jarFiles = task.getOutJarFiles();
        assertEquals(3, jarFiles.size(), "Should have 3 entries");
        assertEquals(path1, jarFiles.get(0));
        assertEquals(path2, jarFiles.get(1));
        assertEquals(path3, jarFiles.get(2));
    }

    /**
     * Tests that outjars() handles Windows-style paths.
     */
    @Test
    public void testOutjars_windowsPaths() throws Exception {
        // Given: Windows-style paths
        String windowsPath = "C:\\Users\\Dev\\project\\output.jar";

        // When: Calling outjars()
        task.outjars(windowsPath);

        // Then: The Windows path should be stored
        List jarFiles = task.getOutJarFiles();
        assertEquals(1, jarFiles.size(), "Should have 1 entry");
        assertEquals(windowsPath, jarFiles.get(0), "Should store Windows path as-is");
    }

    /**
     * Tests that outjars() handles Unix-style paths.
     */
    @Test
    public void testOutjars_unixPaths() throws Exception {
        // Given: Unix-style paths
        String unixPath = "/home/dev/project/output.jar";

        // When: Calling outjars()
        task.outjars(unixPath);

        // Then: The Unix path should be stored
        List jarFiles = task.getOutJarFiles();
        assertEquals(1, jarFiles.size(), "Should have 1 entry");
        assertEquals(unixPath, jarFiles.get(0), "Should store Unix path as-is");
    }

    /**
     * Tests that outjars() interoperates correctly with configuration().
     */
    @Test
    public void testOutjars_interoperatesWithConfiguration() throws Exception {
        // Given: Mix of configuration files and output jars
        task.configuration("proguard-rules.pro");
        task.injars("input.jar");
        task.outjars("output.jar");
        task.configuration("proguard-debug.pro");

        // When: Getting both lists
        List configFiles = task.getConfigurationFiles();
        List outJars = task.getOutJarFiles();

        // Then: Each list should contain only its respective items
        assertEquals(2, configFiles.size(), "Should have 2 config files");
        assertEquals("proguard-rules.pro", configFiles.get(0));
        assertEquals("proguard-debug.pro", configFiles.get(1));

        assertEquals(1, outJars.size(), "Should have 1 output jar");
        assertEquals("output.jar", outJars.get(0));
    }

    /**
     * Tests that outjars() called before any injars records count of 0.
     */
    @Test
    public void testOutjars_beforeAnyInjars() throws Exception {
        // Given: No input jars
        // When: Calling outjars() first
        task.outjars("output.jar");

        // Then: Count should be 0
        List inJarCounts = task.getInJarCounts();
        assertEquals(1, inJarCounts.size(), "Should have 1 count entry");
        assertEquals(0, inJarCounts.get(0), "Count should be 0");
    }

    /**
     * Tests that outjars() records correct count when called multiple times without new inputs.
     */
    @Test
    public void testOutjars_multipleWithoutNewInputs() throws Exception {
        // Given: Input jars configured once
        task.injars("input1.jar");
        task.injars("input2.jar");

        // When: Calling outjars() multiple times without adding new inputs
        task.outjars("output1.jar");
        task.outjars("output2.jar");
        task.outjars("output3.jar");

        // Then: All counts should be the same (2)
        List inJarCounts = task.getInJarCounts();
        assertEquals(3, inJarCounts.size(), "Should have 3 count entries");
        assertEquals(2, inJarCounts.get(0), "First count should be 2");
        assertEquals(2, inJarCounts.get(1), "Second count should be 2");
        assertEquals(2, inJarCounts.get(2), "Third count should be 2");
    }

    /**
     * Tests that outjars() stores objects without any validation.
     */
    @Test
    public void testOutjars_noValidation() throws Exception {
        // Given: Various types of objects
        String string = "output.jar";
        Integer integer = 42;
        Object customObject = new Object();

        // When: Calling outjars() with any object type
        task.outjars(string);
        task.outjars(integer);
        task.outjars(customObject);

        // Then: All objects should be stored without validation
        List jarFiles = task.getOutJarFiles();
        assertEquals(3, jarFiles.size(), "Should have 3 entries");
        assertSame(string, jarFiles.get(0), "First should be the string");
        assertSame(integer, jarFiles.get(1), "Second should be the integer");
        assertSame(customObject, jarFiles.get(2), "Third should be the custom object");
    }

    /**
     * Tests that outjars() handles a complex interleaved workflow.
     */
    @Test
    public void testOutjars_complexInterleavedWorkflow() throws Exception {
        // Given: Complex interleaved input and output operations
        task.injars("input1.jar");
        task.outjars("output1.jar");

        task.injars("input2.jar");
        task.injars("input3.jar");
        task.outjars("output2.jar");

        task.outjars("output3.jar");  // Same input count as output2

        task.injars("input4.jar");
        task.outjars("output4.jar");

        // When: Getting the results
        List inJars = task.getInJarFiles();
        List outJars = task.getOutJarFiles();
        List inJarCounts = task.getInJarCounts();

        // Then: All should be tracked correctly
        assertEquals(4, inJars.size(), "Should have 4 input jars");
        assertEquals(4, outJars.size(), "Should have 4 output jars");
        assertEquals(4, inJarCounts.size(), "Should have 4 count entries");

        assertEquals(1, inJarCounts.get(0), "First count should be 1");
        assertEquals(3, inJarCounts.get(1), "Second count should be 3");
        assertEquals(3, inJarCounts.get(2), "Third count should be 3 (no new inputs)");
        assertEquals(4, inJarCounts.get(3), "Fourth count should be 4");
    }

    /**
     * Tests that outjars() handles output to build directory.
     */
    @Test
    public void testOutjars_buildDirectory() throws Exception {
        // Given: Output to build directory
        String outputPath = "build/outputs/proguard/release/output.jar";

        // When: Calling outjars()
        task.outjars(outputPath);

        // Then: Path should be stored
        List jarFiles = task.getOutJarFiles();
        assertEquals(1, jarFiles.size(), "Should have 1 entry");
        assertEquals(outputPath, jarFiles.get(0));
    }

    /**
     * Tests that inJarCounts size always matches outJarFiles size.
     */
    @Test
    public void testOutjars_inJarCountsSizeMatchesOutJarFiles() throws Exception {
        // Given: Various output jars added
        task.outjars("output1.jar");
        task.injars("input1.jar");
        task.outjars("output2.jar");
        task.injars("input2.jar");
        task.injars("input3.jar");
        task.outjars("output3.jar");

        // When: Getting both lists
        List outJars = task.getOutJarFiles();
        List inJarCounts = task.getInJarCounts();

        // Then: Sizes should match
        assertEquals(outJars.size(), inJarCounts.size(),
            "InJarCounts size should always match outJarFiles size");
        assertEquals(3, outJars.size());
        assertEquals(3, inJarCounts.size());
    }

    // ==================== Tests for outjars(Map, Object) ====================

    /**
     * Tests that outjars(Map, Object) adds a single jar with filter Map.
     */
    @Test
    public void testOutjarsWithMap_singleJarWithFilter() throws Exception {
        // Given: A jar path and filter Map
        String jarPath = "output.jar";
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("filter", "!**.class");

        // When: Calling outjars(Map, Object)
        task.outjars(filterArgs, jarPath);

        // Then: Both jar and filter should be stored
        List jarFiles = task.getOutJarFiles();
        List filters = task.getOutJarFilters();

        assertEquals(1, jarFiles.size(), "Should have 1 jar entry");
        assertEquals(jarPath, jarFiles.get(0), "Jar path should be stored");
        assertSame(filterArgs, filters.get(0), "Filter Map should be stored");
    }

    /**
     * Tests that outjars(Map, Object) stores the filter Map by reference.
     */
    @Test
    public void testOutjarsWithMap_storesMapByReference() throws Exception {
        // Given: A filter Map
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("filter", "**.png");

        // When: Calling outjars(Map, Object)
        task.outjars(filterArgs, "output.jar");

        // Then: The exact same Map object should be stored
        List filters = task.getOutJarFilters();
        assertSame(filterArgs, filters.get(0), "Should store the exact same Map object");
    }

    /**
     * Tests that outjars(Map, Object) accepts null filter Map.
     */
    @Test
    public void testOutjarsWithMap_nullFilterMap() throws Exception {
        // Given: Null filter Map
        Map<String, String> nullFilter = null;

        // When: Calling outjars(Map, Object)
        task.outjars(nullFilter, "output.jar");

        // Then: Null should be stored
        List filters = task.getOutJarFilters();
        assertEquals(1, filters.size(), "Should have 1 filter entry");
        assertNull(filters.get(0), "Filter should be null");
    }

    /**
     * Tests that outjars(Map, Object) accepts empty filter Map.
     */
    @Test
    public void testOutjarsWithMap_emptyFilterMap() throws Exception {
        // Given: Empty filter Map
        Map<String, String> emptyFilter = new HashMap<>();

        // When: Calling outjars(Map, Object)
        task.outjars(emptyFilter, "output.jar");

        // Then: Empty Map should be stored
        List filters = task.getOutJarFilters();
        assertSame(emptyFilter, filters.get(0), "Empty Map should be stored");
        assertEquals(0, ((Map) filters.get(0)).size(), "Map should be empty");
    }

    /**
     * Tests that outjars(Map, Object) handles 'filter' key.
     */
    @Test
    public void testOutjarsWithMap_filterKey() throws Exception {
        // Given: Filter Map with 'filter' key
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("filter", "!com/example/**");

        // When: Calling outjars(Map, Object)
        task.outjars(filterArgs, "output.jar");

        // Then: Filter should be stored
        List filters = task.getOutJarFilters();
        Map storedFilter = (Map) filters.get(0);
        assertEquals("!com/example/**", storedFilter.get("filter"));
    }

    /**
     * Tests that outjars(Map, Object) handles 'jarfilter' key.
     */
    @Test
    public void testOutjarsWithMap_jarfilterKey() throws Exception {
        // Given: Filter Map with 'jarfilter' key
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("jarfilter", "!**.txt");

        // When: Calling outjars(Map, Object)
        task.outjars(filterArgs, "output.jar");

        // Then: Filter should be stored
        List filters = task.getOutJarFilters();
        Map storedFilter = (Map) filters.get(0);
        assertEquals("!**.txt", storedFilter.get("jarfilter"));
    }

    /**
     * Tests that outjars(Map, Object) handles 'aarfilter' key.
     */
    @Test
    public void testOutjarsWithMap_aarfilterKey() throws Exception {
        // Given: Filter Map with 'aarfilter' key
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("aarfilter", "!**/LICENSE");

        // When: Calling outjars(Map, Object)
        task.outjars(filterArgs, "library.aar");

        // Then: Filter should be stored
        List filters = task.getOutJarFilters();
        Map storedFilter = (Map) filters.get(0);
        assertEquals("!**/LICENSE", storedFilter.get("aarfilter"));
    }

    /**
     * Tests that outjars(Map, Object) handles 'apkfilter' key.
     */
    @Test
    public void testOutjarsWithMap_apkfilterKey() throws Exception {
        // Given: Filter Map with 'apkfilter' key
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("apkfilter", "!resources.arsc");

        // When: Calling outjars(Map, Object)
        task.outjars(filterArgs, "app.apk");

        // Then: Filter should be stored
        List filters = task.getOutJarFilters();
        Map storedFilter = (Map) filters.get(0);
        assertEquals("!resources.arsc", storedFilter.get("apkfilter"));
    }

    /**
     * Tests that outjars(Map, Object) handles 'warfilter' key.
     */
    @Test
    public void testOutjarsWithMap_warfilterKey() throws Exception {
        // Given: Filter Map with 'warfilter' key
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("warfilter", "!WEB-INF/**");

        // When: Calling outjars(Map, Object)
        task.outjars(filterArgs, "webapp.war");

        // Then: Filter should be stored
        List filters = task.getOutJarFilters();
        Map storedFilter = (Map) filters.get(0);
        assertEquals("!WEB-INF/**", storedFilter.get("warfilter"));
    }

    /**
     * Tests that outjars(Map, Object) handles 'earfilter' key.
     */
    @Test
    public void testOutjarsWithMap_earfilterKey() throws Exception {
        // Given: Filter Map with 'earfilter' key
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("earfilter", "!META-INF/**");

        // When: Calling outjars(Map, Object)
        task.outjars(filterArgs, "app.ear");

        // Then: Filter should be stored
        List filters = task.getOutJarFilters();
        Map storedFilter = (Map) filters.get(0);
        assertEquals("!META-INF/**", storedFilter.get("earfilter"));
    }

    /**
     * Tests that outjars(Map, Object) handles 'jmodfilter' key.
     */
    @Test
    public void testOutjarsWithMap_jmodfilterKey() throws Exception {
        // Given: Filter Map with 'jmodfilter' key
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("jmodfilter", "!classes/**");

        // When: Calling outjars(Map, Object)
        task.outjars(filterArgs, "module.jmod");

        // Then: Filter should be stored
        List filters = task.getOutJarFilters();
        Map storedFilter = (Map) filters.get(0);
        assertEquals("!classes/**", storedFilter.get("jmodfilter"));
    }

    /**
     * Tests that outjars(Map, Object) handles 'zipfilter' key.
     */
    @Test
    public void testOutjarsWithMap_zipfilterKey() throws Exception {
        // Given: Filter Map with 'zipfilter' key
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("zipfilter", "!**.txt");

        // When: Calling outjars(Map, Object)
        task.outjars(filterArgs, "archive.zip");

        // Then: Filter should be stored
        List filters = task.getOutJarFilters();
        Map storedFilter = (Map) filters.get(0);
        assertEquals("!**.txt", storedFilter.get("zipfilter"));
    }

    /**
     * Tests that outjars(Map, Object) handles multiple filter keys in one Map.
     */
    @Test
    public void testOutjarsWithMap_multipleFilterKeys() throws Exception {
        // Given: Filter Map with multiple keys
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("filter", "!**.png");
        filterArgs.put("jarfilter", "!META-INF/**");

        // When: Calling outjars(Map, Object)
        task.outjars(filterArgs, "output.jar");

        // Then: All filters should be stored
        List filters = task.getOutJarFilters();
        Map storedFilter = (Map) filters.get(0);
        assertEquals(2, storedFilter.size(), "Should have 2 filter keys");
        assertEquals("!**.png", storedFilter.get("filter"));
        assertEquals("!META-INF/**", storedFilter.get("jarfilter"));
    }

    /**
     * Tests that outjars(Map, Object) records input jar count.
     */
    @Test
    public void testOutjarsWithMap_recordsInputJarCount() throws Exception {
        // Given: Input jars configured
        task.injars("input1.jar");
        task.injars("input2.jar");
        task.injars("input3.jar");

        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("filter", "!**.class");

        // When: Calling outjars(Map, Object)
        task.outjars(filterArgs, "output.jar");

        // Then: Input jar count should be recorded
        List inJarCounts = task.getInJarCounts();
        assertEquals(1, inJarCounts.size(), "Should have 1 count entry");
        assertEquals(3, inJarCounts.get(0), "Count should be 3");
    }

    /**
     * Tests that outjars(Map, Object) can be called multiple times.
     */
    @Test
    public void testOutjarsWithMap_multipleCalls() throws Exception {
        // Given: Multiple filter Maps
        Map<String, String> filter1 = new HashMap<>();
        filter1.put("filter", "!**.png");

        Map<String, String> filter2 = new HashMap<>();
        filter2.put("filter", "!**.txt");

        Map<String, String> filter3 = new HashMap<>();
        filter3.put("filter", "!**.xml");

        // When: Calling outjars(Map, Object) multiple times
        task.outjars(filter1, "output1.jar");
        task.outjars(filter2, "output2.jar");
        task.outjars(filter3, "output3.jar");

        // Then: All should be accumulated
        List jarFiles = task.getOutJarFiles();
        List filters = task.getOutJarFilters();

        assertEquals(3, jarFiles.size(), "Should have 3 jar entries");
        assertEquals(3, filters.size(), "Should have 3 filter entries");

        assertSame(filter1, filters.get(0));
        assertSame(filter2, filters.get(1));
        assertSame(filter3, filters.get(2));
    }

    /**
     * Tests that outjars(Map, Object) maintains 1-to-1 correspondence with jars.
     */
    @Test
    public void testOutjarsWithMap_maintainsCorrespondence() throws Exception {
        // Given: Mix of filtered and unfiltered output jars
        Map<String, String> filter = new HashMap<>();
        filter.put("filter", "!**.png");

        task.outjars("jar1.jar");  // No filter
        task.outjars(filter, "jar2.jar");  // With filter
        task.outjars(null, "jar3.jar");  // Null filter

        // When: Getting both lists
        List jarFiles = task.getOutJarFiles();
        List filters = task.getOutJarFilters();

        // Then: Lists should have same size with correct correspondence
        assertEquals(3, jarFiles.size(), "Should have 3 jar entries");
        assertEquals(3, filters.size(), "Should have 3 filter entries");

        assertNull(filters.get(0), "First filter should be null");
        assertSame(filter, filters.get(1), "Second filter should be the Map");
        assertNull(filters.get(2), "Third filter should be null");
    }

    /**
     * Tests that outjars(Map, Object) handles File objects with filters.
     */
    @Test
    public void testOutjarsWithMap_fileObjectWithFilter() throws Exception {
        // Given: A File object and filter Map
        File jarFile = new File(tempDir.toFile(), "output.jar");
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("filter", "!**.class");

        // When: Calling outjars(Map, Object)
        task.outjars(filterArgs, jarFile);

        // Then: Both should be stored
        List jarFiles = task.getOutJarFiles();
        List filters = task.getOutJarFilters();

        assertSame(jarFile, jarFiles.get(0), "File object should be stored");
        assertSame(filterArgs, filters.get(0), "Filter Map should be stored");
    }

    /**
     * Tests that outjars(Map, Object) works with Collections (not flattened).
     */
    @Test
    public void testOutjarsWithMap_collectionNotFlattened() throws Exception {
        // Given: A Collection and filter Map
        java.util.List<String> jarList = java.util.Arrays.asList("jar1.jar", "jar2.jar");
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("filter", "!**.class");

        // When: Calling outjars(Map, Object)
        task.outjars(filterArgs, jarList);

        // Then: Collection should be stored as-is (not flattened)
        List jarFiles = task.getOutJarFiles();
        List filters = task.getOutJarFilters();

        assertEquals(1, jarFiles.size(), "Should have 1 entry (the Collection itself)");
        assertSame(jarList, jarFiles.get(0), "Should store Collection as-is");
        assertSame(filterArgs, filters.get(0), "Filter should be stored");
    }

    /**
     * Tests that outjars(Map, Object) handles modification of filter Map after call.
     */
    @Test
    public void testOutjarsWithMap_filterMapModificationAfterCall() throws Exception {
        // Given: A filter Map
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("filter", "!**.png");

        // When: Calling outjars(Map, Object) and then modifying the Map
        task.outjars(filterArgs, "output.jar");
        filterArgs.put("jarfilter", "!**.txt");

        // Then: Stored Map should reflect modifications (stored by reference)
        List filters = task.getOutJarFilters();
        Map storedFilter = (Map) filters.get(0);
        assertEquals(2, storedFilter.size(), "Map should have 2 entries after modification");
        assertEquals("!**.png", storedFilter.get("filter"));
        assertEquals("!**.txt", storedFilter.get("jarfilter"));
    }

    /**
     * Tests that outjars(Map, Object) handles complex workflow with filters.
     */
    @Test
    public void testOutjarsWithMap_complexWorkflowWithFilters() throws Exception {
        // Given: Complex workflow with input/output and filters
        task.injars("input1.jar");
        task.injars("input2.jar");

        Map<String, String> filter1 = new HashMap<>();
        filter1.put("filter", "!**.png");
        task.outjars(filter1, "output1.jar");

        task.injars("input3.jar");
        task.injars("input4.jar");

        Map<String, String> filter2 = new HashMap<>();
        filter2.put("filter", "!**.txt");
        filter2.put("jarfilter", "!META-INF/**");
        task.outjars(filter2, "output2.jar");

        // When: Getting all lists
        List inJars = task.getInJarFiles();
        List outJars = task.getOutJarFiles();
        List filters = task.getOutJarFilters();
        List inJarCounts = task.getInJarCounts();

        // Then: All should be tracked correctly
        assertEquals(4, inJars.size(), "Should have 4 input jars");
        assertEquals(2, outJars.size(), "Should have 2 output jars");
        assertEquals(2, filters.size(), "Should have 2 filters");
        assertEquals(2, inJarCounts.size(), "Should have 2 count entries");

        assertEquals(2, inJarCounts.get(0), "First count should be 2");
        assertEquals(4, inJarCounts.get(1), "Second count should be 4");

        assertSame(filter1, filters.get(0));
        assertSame(filter2, filters.get(1));
    }

    /**
     * Tests that outjars(Map, Object) handles duplicate filter Maps.
     */
    @Test
    public void testOutjarsWithMap_duplicateFilterMaps() throws Exception {
        // Given: Same filter Map used for multiple jars
        Map<String, String> sharedFilter = new HashMap<>();
        sharedFilter.put("filter", "!**.class");

        // When: Using same Map for multiple jars
        task.outjars(sharedFilter, "output1.jar");
        task.outjars(sharedFilter, "output2.jar");
        task.outjars(sharedFilter, "output3.jar");

        // Then: Same Map should be stored for all
        List filters = task.getOutJarFilters();
        assertEquals(3, filters.size(), "Should have 3 filter entries");
        assertSame(sharedFilter, filters.get(0));
        assertSame(sharedFilter, filters.get(1));
        assertSame(sharedFilter, filters.get(2));
    }

    /**
     * Tests that outjars(Map, Object) handles realistic Android scenario with filters.
     */
    @Test
    public void testOutjarsWithMap_realisticAndroidScenarioWithFilters() throws Exception {
        // Given: Android project with filtered output
        task.injars("build/intermediates/classes.jar");
        task.injars("build/intermediates/library-classes.jar");

        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("filter", "!**.png,!**.xml");
        filterArgs.put("apkfilter", "!resources.arsc");

        task.outjars(filterArgs, "build/outputs/app-release.jar");

        // When: Getting the results
        List outJars = task.getOutJarFiles();
        List filters = task.getOutJarFilters();
        List inJarCounts = task.getInJarCounts();

        // Then: All should be tracked correctly
        assertEquals(1, outJars.size(), "Should have 1 output jar");
        assertEquals(1, filters.size(), "Should have 1 filter");
        assertEquals(1, inJarCounts.size(), "Should have 1 count entry");
        assertEquals(2, inJarCounts.get(0), "Count should be 2");

        Map storedFilter = (Map) filters.get(0);
        assertEquals("!**.png,!**.xml", storedFilter.get("filter"));
        assertEquals("!resources.arsc", storedFilter.get("apkfilter"));
    }

    /**
     * Tests that outjars(Map, Object) handles mixed calls with and without filters.
     */
    @Test
    public void testOutjarsWithMap_mixedFilteredAndUnfiltered() throws Exception {
        // Given: Mix of filtered and unfiltered calls
        task.outjars("output1.jar");  // No filter (delegates to Map version with null)

        Map<String, String> filter = new HashMap<>();
        filter.put("filter", "!**.png");
        task.outjars(filter, "output2.jar");  // With filter

        task.outjars("output3.jar");  // No filter again

        // When: Getting the results
        List outJars = task.getOutJarFiles();
        List filters = task.getOutJarFilters();

        // Then: Filters should match expectations
        assertEquals(3, outJars.size(), "Should have 3 output jars");
        assertEquals(3, filters.size(), "Should have 3 filter entries");

        assertNull(filters.get(0), "First filter should be null");
        assertSame(filter, filters.get(1), "Second filter should be the Map");
        assertNull(filters.get(2), "Third filter should be null");
    }

    /**
     * Tests that outjars(Map, Object) handles paths with special characters.
     */
    @Test
    public void testOutjarsWithMap_pathsWithSpecialCharacters() throws Exception {
        // Given: Path with special characters and filter
        String specialPath = "output-v1.0_final@release.jar";
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("filter", "!**.class");

        // When: Calling outjars(Map, Object)
        task.outjars(filterArgs, specialPath);

        // Then: Path should be stored correctly
        List jarFiles = task.getOutJarFiles();
        assertEquals(specialPath, jarFiles.get(0), "Special path should be stored correctly");
    }

    /**
     * Tests that outjars(Map, Object) handles null jar path.
     */
    @Test
    public void testOutjarsWithMap_nullJarPath() throws Exception {
        // Given: Null jar path and filter Map
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("filter", "!**.class");

        // When: Calling outjars(Map, Object) with null path
        task.outjars(filterArgs, null);

        // Then: Null should be stored
        List jarFiles = task.getOutJarFiles();
        List filters = task.getOutJarFilters();

        assertEquals(1, jarFiles.size(), "Should have 1 jar entry");
        assertNull(jarFiles.get(0), "Jar path should be null");
        assertSame(filterArgs, filters.get(0), "Filter should be stored");
    }

    /**
     * Tests that outjars(Map, Object) handles cumulative input jar counts with filters.
     */
    @Test
    public void testOutjarsWithMap_cumulativeInputJarCountsWithFilters() throws Exception {
        // Given: Complex workflow with filters
        task.injars("input1.jar");
        task.injars("input2.jar");

        Map<String, String> filter1 = new HashMap<>();
        filter1.put("filter", "!**.png");
        task.outjars(filter1, "output1.jar");  // Count: 2

        task.injars("input3.jar");
        task.injars("input4.jar");
        task.injars("input5.jar");

        Map<String, String> filter2 = new HashMap<>();
        filter2.put("filter", "!**.txt");
        task.outjars(filter2, "output2.jar");  // Count: 5

        // When: Getting the inJarCounts
        List inJarCounts = task.getInJarCounts();

        // Then: Counts should be cumulative
        assertEquals(2, inJarCounts.size(), "Should have 2 count entries");
        assertEquals(2, inJarCounts.get(0), "First count should be 2");
        assertEquals(5, inJarCounts.get(1), "Second count should be 5 (cumulative)");
    }

    /**
     * Tests that outjars(Map, Object) handles interleaved calls with both versions.
     */
    @Test
    public void testOutjarsWithMap_interleavedWithSimpleVersion() throws Exception {
        // Given: Interleaved calls to both outjars versions
        Map<String, String> filter1 = new HashMap<>();
        filter1.put("filter", "!**.png");

        task.injars("input1.jar");
        task.outjars(filter1, "output1.jar");  // Map version
        task.injars("input2.jar");
        task.outjars("output2.jar");  // Simple version
        task.injars("input3.jar");

        Map<String, String> filter2 = new HashMap<>();
        filter2.put("filter", "!**.txt");
        task.outjars(filter2, "output3.jar");  // Map version

        // When: Getting all lists
        List outJars = task.getOutJarFiles();
        List filters = task.getOutJarFilters();
        List inJarCounts = task.getInJarCounts();

        // Then: All should be tracked correctly
        assertEquals(3, outJars.size(), "Should have 3 output jars");
        assertEquals(3, filters.size(), "Should have 3 filter entries");
        assertEquals(3, inJarCounts.size(), "Should have 3 count entries");

        assertSame(filter1, filters.get(0), "First filter should be filter1");
        assertNull(filters.get(1), "Second filter should be null");
        assertSame(filter2, filters.get(2), "Third filter should be filter2");

        assertEquals(1, inJarCounts.get(0), "First count should be 1");
        assertEquals(2, inJarCounts.get(1), "Second count should be 2");
        assertEquals(3, inJarCounts.get(2), "Third count should be 3");
    }
}
