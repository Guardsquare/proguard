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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link ProGuardTask#injars(Object)} and {@link ProGuardTask#injars(Map, Object)}.
 *
 * This test class verifies that both injars methods correctly add
 * input jar file objects to the internal list and maintain proper correspondence with filters.
 *
 * Method signatures:
 * 1. injars(Object inJarFiles)
 *    - Adds the input jar file object to the inJarFiles list
 *    - Adds a null entry to the inJarFilters list (since no filters are specified)
 *    - This is a convenience method that delegates to injars(Map, Object) with null filters
 *
 * 2. injars(Map filterArgs, Object inJarFiles)
 *    - Adds the input jar file object to the inJarFiles list
 *    - Adds the filterArgs Map to the inJarFilters list
 *    - Supports various filter types: filter, jarfilter, aarfilter, apkfilter, etc.
 *
 * Unlike configuration(), these methods do NOT flatten Collections - they add the
 * object as-is to the list.
 */
public class ProGuardTaskClaude_injarsTest {

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
     * Tests that injars() adds a single String path to the input jar files list.
     */
    @Test
    public void testInjars_singleString() throws Exception {
        // Given: A String path
        String jarPath = "input.jar";

        // When: Calling injars()
        task.injars(jarPath);

        // Then: The input jar files list should contain the string
        List jarFiles = task.getInJarFiles();
        assertNotNull(jarFiles, "Input jar files list should not be null");
        assertEquals(1, jarFiles.size(), "Should have 1 entry");
        assertEquals(jarPath, jarFiles.get(0), "Entry should be the string path");
    }

    /**
     * Tests that injars() adds a null filter entry when called without filters.
     */
    @Test
    public void testInjars_addsNullFilter() throws Exception {
        // Given: A String path
        String jarPath = "input.jar";

        // When: Calling injars() without filters
        task.injars(jarPath);

        // Then: The filters list should have a null entry
        List filters = task.getInJarFilters();
        assertNotNull(filters, "Filters list should not be null");
        assertEquals(1, filters.size(), "Should have 1 filter entry");
        assertNull(filters.get(0), "Filter entry should be null when no filters specified");
    }

    /**
     * Tests that injars() adds a single File object to the input jar files list.
     */
    @Test
    public void testInjars_singleFile() throws Exception {
        // Given: A File object
        File jarFile = new File(tempDir.toFile(), "input.jar");

        // When: Calling injars()
        task.injars(jarFile);

        // Then: The input jar files list should contain the File object
        List jarFiles = task.getInJarFiles();
        assertNotNull(jarFiles, "Input jar files list should not be null");
        assertEquals(1, jarFiles.size(), "Should have 1 entry");
        assertSame(jarFile, jarFiles.get(0), "Entry should be the same File object");
    }

    /**
     * Tests that injars() can be called multiple times and accumulates entries.
     */
    @Test
    public void testInjars_multipleCalls() throws Exception {
        // Given: Multiple input jars
        String jar1 = "input1.jar";
        String jar2 = "input2.jar";
        String jar3 = "input3.jar";

        // When: Calling injars() multiple times
        task.injars(jar1);
        task.injars(jar2);
        task.injars(jar3);

        // Then: All entries should be accumulated
        List jarFiles = task.getInJarFiles();
        assertEquals(3, jarFiles.size(), "Should have 3 entries");
        assertEquals(jar1, jarFiles.get(0), "First entry should be jar1");
        assertEquals(jar2, jarFiles.get(1), "Second entry should be jar2");
        assertEquals(jar3, jarFiles.get(2), "Third entry should be jar3");
    }

    /**
     * Tests that injars() maintains 1-to-1 correspondence with filters.
     */
    @Test
    public void testInjars_maintainsFilterCorrespondence() throws Exception {
        // Given: Multiple input jars
        task.injars("jar1.jar");
        task.injars("jar2.jar");
        task.injars("jar3.jar");

        // When: Getting both lists
        List jarFiles = task.getInJarFiles();
        List filters = task.getInJarFilters();

        // Then: Both lists should have same size with null filters
        assertEquals(jarFiles.size(), filters.size(), "Lists should have same size");
        assertEquals(3, filters.size(), "Should have 3 filter entries");
        assertNull(filters.get(0), "All filters should be null");
        assertNull(filters.get(1), "All filters should be null");
        assertNull(filters.get(2), "All filters should be null");
    }

    /**
     * Tests that injars() preserves the order of input jars as they were added.
     */
    @Test
    public void testInjars_preservesOrder() throws Exception {
        // Given: Input jars added in specific order
        String first = "first.jar";
        String second = "second.jar";
        String third = "third.jar";

        // When: Calling injars() in order
        task.injars(first);
        task.injars(second);
        task.injars(third);

        // Then: Order should be preserved
        List jarFiles = task.getInJarFiles();
        assertEquals(3, jarFiles.size(), "Should have 3 entries");
        assertEquals(first, jarFiles.get(0), "First should be first.jar");
        assertEquals(second, jarFiles.get(1), "Second should be second.jar");
        assertEquals(third, jarFiles.get(2), "Third should be third.jar");
    }

    /**
     * Tests that injars() handles relative path strings.
     */
    @Test
    public void testInjars_relativePathString() throws Exception {
        // Given: A relative path string
        String relativePath = "build/libs/app.jar";

        // When: Calling injars()
        task.injars(relativePath);

        // Then: The relative path should be stored as-is
        List jarFiles = task.getInJarFiles();
        assertEquals(1, jarFiles.size(), "Should have 1 entry");
        assertEquals(relativePath, jarFiles.get(0), "Should store the relative path string");
    }

    /**
     * Tests that injars() handles absolute path strings.
     */
    @Test
    public void testInjars_absolutePathString() throws Exception {
        // Given: An absolute path string
        String absolutePath = new File(tempDir.toFile(), "app.jar").getAbsolutePath();

        // When: Calling injars()
        task.injars(absolutePath);

        // Then: The absolute path should be stored as-is
        List jarFiles = task.getInJarFiles();
        assertEquals(1, jarFiles.size(), "Should have 1 entry");
        assertEquals(absolutePath, jarFiles.get(0), "Should store the absolute path string");
    }

    /**
     * Tests that injars() handles null value.
     */
    @Test
    public void testInjars_nullValue() throws Exception {
        // Given: A null value
        Object nullValue = null;

        // When: Calling injars()
        task.injars(nullValue);

        // Then: Null should be added to the list
        List jarFiles = task.getInJarFiles();
        assertEquals(1, jarFiles.size(), "Should have 1 entry");
        assertNull(jarFiles.get(0), "Entry should be null");
    }

    /**
     * Tests that injars() handles empty string.
     */
    @Test
    public void testInjars_emptyString() throws Exception {
        // Given: An empty string
        String emptyString = "";

        // When: Calling injars()
        task.injars(emptyString);

        // Then: The empty string should be added
        List jarFiles = task.getInJarFiles();
        assertEquals(1, jarFiles.size(), "Should have 1 entry");
        assertEquals("", jarFiles.get(0), "Should store the empty string");
    }

    /**
     * Tests that injars() allows duplicate entries.
     */
    @Test
    public void testInjars_duplicates() throws Exception {
        // Given: Same jar added multiple times
        String jarPath = "input.jar";

        // When: Calling injars() multiple times with same path
        task.injars(jarPath);
        task.injars(jarPath);
        task.injars(jarPath);

        // Then: All duplicates should be added
        List jarFiles = task.getInJarFiles();
        assertEquals(3, jarFiles.size(), "Should have 3 entries (duplicates allowed)");
        assertEquals(jarPath, jarFiles.get(0), "All entries should be the same");
        assertEquals(jarPath, jarFiles.get(1), "All entries should be the same");
        assertEquals(jarPath, jarFiles.get(2), "All entries should be the same");
    }

    /**
     * Tests that injars() handles directory paths.
     */
    @Test
    public void testInjars_directoryPath() throws Exception {
        // Given: A directory path
        File directory = new File(tempDir.toFile(), "classes");

        // When: Calling injars()
        task.injars(directory);

        // Then: The directory should be stored
        List jarFiles = task.getInJarFiles();
        assertEquals(1, jarFiles.size(), "Should have 1 entry");
        assertSame(directory, jarFiles.get(0), "Should store the directory File object");
    }

    /**
     * Tests that injars() handles various archive file types.
     */
    @Test
    public void testInjars_variousFileTypes() throws Exception {
        // Given: Various file types
        String jarFile = "app.jar";
        String aarFile = "library.aar";
        String apkFile = "app.apk";
        String warFile = "webapp.war";
        String zipFile = "archive.zip";

        // When: Calling injars() with various types
        task.injars(jarFile);
        task.injars(aarFile);
        task.injars(apkFile);
        task.injars(warFile);
        task.injars(zipFile);

        // Then: All types should be stored
        List jarFiles = task.getInJarFiles();
        assertEquals(5, jarFiles.size(), "Should have 5 entries");
        assertEquals(jarFile, jarFiles.get(0), "First should be jar");
        assertEquals(aarFile, jarFiles.get(1), "Second should be aar");
        assertEquals(apkFile, jarFiles.get(2), "Third should be apk");
        assertEquals(warFile, jarFiles.get(3), "Fourth should be war");
        assertEquals(zipFile, jarFiles.get(4), "Fifth should be zip");
    }

    /**
     * Tests that injars() does NOT flatten Collections (unlike configuration()).
     * Collections should be added as-is.
     */
    @Test
    public void testInjars_doesNotFlattenCollections() throws Exception {
        // Given: A List of jar files
        java.util.List<String> jarList = java.util.Arrays.asList("jar1.jar", "jar2.jar");

        // When: Calling injars() with the List
        task.injars(jarList);

        // Then: The List itself should be added (not flattened)
        List jarFiles = task.getInJarFiles();
        assertEquals(1, jarFiles.size(), "Should have 1 entry (the List itself)");
        assertSame(jarList, jarFiles.get(0), "Entry should be the List object itself, not its elements");
    }

    /**
     * Tests that injars() preserves object identity.
     */
    @Test
    public void testInjars_preservesObjectIdentity() throws Exception {
        // Given: A specific File object
        File jarFile = new File(tempDir.toFile(), "app.jar");

        // When: Calling injars()
        task.injars(jarFile);

        // Then: The exact same object should be stored
        List jarFiles = task.getInJarFiles();
        assertSame(jarFile, jarFiles.get(0), "Should store the exact same File object");
    }

    /**
     * Tests that injars() is independent from outjars.
     */
    @Test
    public void testInjars_independentFromOutjars() throws Exception {
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
     * Tests that injars() is independent from libraryjars.
     */
    @Test
    public void testInjars_independentFromLibraryjars() throws Exception {
        // Given: Input and library jars
        String inputJar = "app.jar";
        String libraryJar = "android.jar";

        // When: Calling both injars() and libraryjars()
        task.injars(inputJar);
        task.libraryjars(libraryJar);

        // Then: Each list should contain only its respective jar
        List inJars = task.getInJarFiles();
        List libraryJars = task.getLibraryJarFiles();

        assertEquals(1, inJars.size(), "Should have 1 input jar");
        assertEquals(inputJar, inJars.get(0), "Input jar should be correct");

        assertEquals(1, libraryJars.size(), "Should have 1 library jar");
        assertEquals(libraryJar, libraryJars.get(0), "Library jar should be correct");

        assertFalse(inJars.contains(libraryJar), "Input jars should not contain library jar");
        assertFalse(libraryJars.contains(inputJar), "Library jars should not contain input jar");
    }

    /**
     * Tests that injars() updates inJarCounts when outjars is called.
     */
    @Test
    public void testInjars_affectsInJarCounts() throws Exception {
        // Given: Input jars followed by an output jar
        task.injars("jar1.jar");
        task.injars("jar2.jar");
        task.injars("jar3.jar");
        task.outjars("output.jar");

        // When: Getting inJarCounts
        List inJarCounts = task.getInJarCounts();

        // Then: The count should reflect the number of input jars
        assertEquals(1, inJarCounts.size(), "Should have 1 count entry");
        assertEquals(3, inJarCounts.get(0), "Count should be 3");
    }

    /**
     * Tests that injars() works in a realistic Android scenario.
     */
    @Test
    public void testInjars_realisticAndroidScenario() throws Exception {
        // Given: A realistic Android project with multiple input jars
        task.injars("build/intermediates/classes.jar");
        task.injars("build/intermediates/library-classes.jar");
        task.injars("libs/third-party-lib.jar");

        // When: Getting the result
        List jarFiles = task.getInJarFiles();

        // Then: All should be added in order
        assertEquals(3, jarFiles.size(), "Should have 3 entries");
        assertEquals("build/intermediates/classes.jar", jarFiles.get(0));
        assertEquals("build/intermediates/library-classes.jar", jarFiles.get(1));
        assertEquals("libs/third-party-lib.jar", jarFiles.get(2));
    }

    /**
     * Tests that injars() works in a realistic Java scenario.
     */
    @Test
    public void testInjars_realisticJavaScenario() throws Exception {
        // Given: A realistic Java project with input jars
        File appJar = new File(tempDir.toFile(), "build/libs/app.jar");
        File moduleJar = new File(tempDir.toFile(), "module/build/libs/module.jar");

        task.injars(appJar);
        task.injars(moduleJar);

        // When: Getting the result
        List jarFiles = task.getInJarFiles();

        // Then: All should be added
        assertEquals(2, jarFiles.size(), "Should have 2 entries");
        assertSame(appJar, jarFiles.get(0));
        assertSame(moduleJar, jarFiles.get(1));
    }

    /**
     * Tests that injars() handles Gradle project outputs.
     */
    @Test
    public void testInjars_gradleProjectOutputs() throws Exception {
        // Given: Gradle-style output paths
        String classesDir = "build/classes/java/main";
        String resourcesDir = "build/resources/main";
        String jarFile = "build/libs/project-1.0.jar";

        // When: Calling injars()
        task.injars(classesDir);
        task.injars(resourcesDir);
        task.injars(jarFile);

        // Then: All should be stored
        List jarFiles = task.getInJarFiles();
        assertEquals(3, jarFiles.size(), "Should have 3 entries");
        assertEquals(classesDir, jarFiles.get(0));
        assertEquals(resourcesDir, jarFiles.get(1));
        assertEquals(jarFile, jarFiles.get(2));
    }

    /**
     * Tests that injars() handles mixed String and File objects.
     */
    @Test
    public void testInjars_mixedStringAndFile() throws Exception {
        // Given: Mix of String and File objects
        String stringPath = "app.jar";
        File fileObject = new File(tempDir.toFile(), "lib.jar");
        String anotherString = "module.jar";

        // When: Calling injars()
        task.injars(stringPath);
        task.injars(fileObject);
        task.injars(anotherString);

        // Then: Both types should be preserved
        List jarFiles = task.getInJarFiles();
        assertEquals(3, jarFiles.size(), "Should have 3 entries");
        assertTrue(jarFiles.get(0) instanceof String, "First should be String");
        assertTrue(jarFiles.get(1) instanceof File, "Second should be File");
        assertTrue(jarFiles.get(2) instanceof String, "Third should be String");
    }

    /**
     * Tests that injars() can handle a large number of input jars.
     */
    @Test
    public void testInjars_manyJars() throws Exception {
        // Given: Many input jars
        for (int i = 0; i < 100; i++) {
            task.injars("jar" + i + ".jar");
        }

        // When: Getting the result
        List jarFiles = task.getInJarFiles();

        // Then: All should be stored
        assertEquals(100, jarFiles.size(), "Should have 100 entries");
        assertEquals("jar0.jar", jarFiles.get(0), "First should be jar0.jar");
        assertEquals("jar99.jar", jarFiles.get(99), "Last should be jar99.jar");
    }

    /**
     * Tests that injars() works correctly in a complex workflow.
     */
    @Test
    public void testInjars_complexWorkflow() throws Exception {
        // Given: A complex workflow with multiple operations
        task.injars("app-classes.jar");
        task.injars("app-resources.jar");
        task.outjars("app-combined.jar");

        task.injars("lib1.jar");
        task.injars("lib2.jar");
        task.outjars("libs-combined.jar");

        // When: Getting the results
        List inJars = task.getInJarFiles();
        List outJars = task.getOutJarFiles();
        List inJarCounts = task.getInJarCounts();

        // Then: All should be tracked correctly
        assertEquals(4, inJars.size(), "Should have 4 input jars");
        assertEquals(2, outJars.size(), "Should have 2 output jars");
        assertEquals(2, inJarCounts.size(), "Should have 2 count entries");
        assertEquals(2, inJarCounts.get(0), "First count should be 2");
        assertEquals(4, inJarCounts.get(1), "Second count should be 4");
    }

    /**
     * Tests that injars() handles paths with spaces.
     */
    @Test
    public void testInjars_pathsWithSpaces() throws Exception {
        // Given: A path with spaces
        String pathWithSpaces = "my folder/app.jar";

        // When: Calling injars()
        task.injars(pathWithSpaces);

        // Then: The path should be stored as-is
        List jarFiles = task.getInJarFiles();
        assertEquals(1, jarFiles.size(), "Should have 1 entry");
        assertEquals(pathWithSpaces, jarFiles.get(0), "Should preserve path with spaces");
    }

    /**
     * Tests that injars() handles paths with special characters.
     */
    @Test
    public void testInjars_pathsWithSpecialCharacters() throws Exception {
        // Given: Paths with special characters
        String path1 = "app-v1.0.jar";
        String path2 = "lib_core.jar";
        String path3 = "module@2.0.jar";

        // When: Calling injars()
        task.injars(path1);
        task.injars(path2);
        task.injars(path3);

        // Then: All paths should be stored
        List jarFiles = task.getInJarFiles();
        assertEquals(3, jarFiles.size(), "Should have 3 entries");
        assertEquals(path1, jarFiles.get(0));
        assertEquals(path2, jarFiles.get(1));
        assertEquals(path3, jarFiles.get(2));
    }

    /**
     * Tests that injars() handles Windows-style paths.
     */
    @Test
    public void testInjars_windowsPaths() throws Exception {
        // Given: Windows-style paths
        String windowsPath = "C:\\Users\\Dev\\project\\app.jar";

        // When: Calling injars()
        task.injars(windowsPath);

        // Then: The Windows path should be stored
        List jarFiles = task.getInJarFiles();
        assertEquals(1, jarFiles.size(), "Should have 1 entry");
        assertEquals(windowsPath, jarFiles.get(0), "Should store Windows path as-is");
    }

    /**
     * Tests that injars() handles Unix-style paths.
     */
    @Test
    public void testInjars_unixPaths() throws Exception {
        // Given: Unix-style paths
        String unixPath = "/home/dev/project/app.jar";

        // When: Calling injars()
        task.injars(unixPath);

        // Then: The Unix path should be stored
        List jarFiles = task.getInJarFiles();
        assertEquals(1, jarFiles.size(), "Should have 1 entry");
        assertEquals(unixPath, jarFiles.get(0), "Should store Unix path as-is");
    }

    /**
     * Tests that injars() handles paths with parent directory references.
     */
    @Test
    public void testInjars_pathsWithParentReferences() throws Exception {
        // Given: Paths with parent directory references
        String pathWithParent = "../lib/dependency.jar";

        // When: Calling injars()
        task.injars(pathWithParent);

        // Then: The path should be stored as-is
        List jarFiles = task.getInJarFiles();
        assertEquals(1, jarFiles.size(), "Should have 1 entry");
        assertEquals(pathWithParent, jarFiles.get(0), "Should store path with parent reference");
    }

    /**
     * Tests that injars() handles module dependencies.
     */
    @Test
    public void testInjars_moduleDependencies() throws Exception {
        // Given: Multiple module dependencies
        task.injars("core-module/build/libs/core.jar");
        task.injars("api-module/build/libs/api.jar");
        task.injars("utils-module/build/libs/utils.jar");

        // When: Getting the result
        List jarFiles = task.getInJarFiles();

        // Then: All modules should be added
        assertEquals(3, jarFiles.size(), "Should have 3 entries");
        assertEquals("core-module/build/libs/core.jar", jarFiles.get(0));
        assertEquals("api-module/build/libs/api.jar", jarFiles.get(1));
        assertEquals("utils-module/build/libs/utils.jar", jarFiles.get(2));
    }

    /**
     * Tests that injars() interoperates correctly with configuration().
     */
    @Test
    public void testInjars_interoperatesWithConfiguration() throws Exception {
        // Given: Mix of configuration files and input jars
        task.configuration("proguard-rules.pro");
        task.injars("app.jar");
        task.configuration("proguard-debug.pro");
        task.injars("lib.jar");

        // When: Getting both lists
        List configFiles = task.getConfigurationFiles();
        List inJars = task.getInJarFiles();

        // Then: Each list should contain only its respective items
        assertEquals(2, configFiles.size(), "Should have 2 config files");
        assertEquals("proguard-rules.pro", configFiles.get(0));
        assertEquals("proguard-debug.pro", configFiles.get(1));

        assertEquals(2, inJars.size(), "Should have 2 input jars");
        assertEquals("app.jar", inJars.get(0));
        assertEquals("lib.jar", inJars.get(1));
    }

    /**
     * Tests that injars() handles JAR files from Maven/Gradle cache.
     */
    @Test
    public void testInjars_mavenGradleCache() throws Exception {
        // Given: Paths from Maven/Gradle cache
        String gradleCachePath = ".gradle/caches/modules-2/files-2.1/group/artifact/1.0/hash/artifact-1.0.jar";
        String mavenCachePath = ".m2/repository/group/artifact/1.0/artifact-1.0.jar";

        // When: Calling injars()
        task.injars(gradleCachePath);
        task.injars(mavenCachePath);

        // Then: Cache paths should be stored
        List jarFiles = task.getInJarFiles();
        assertEquals(2, jarFiles.size(), "Should have 2 entries");
        assertEquals(gradleCachePath, jarFiles.get(0));
        assertEquals(mavenCachePath, jarFiles.get(1));
    }

    /**
     * Tests that injars() can be called after outjars() is called.
     */
    @Test
    public void testInjars_afterOutjars() throws Exception {
        // Given: Output jar called first, then input jars
        task.outjars("output.jar");
        task.injars("input1.jar");
        task.injars("input2.jar");

        // When: Getting the results
        List inJars = task.getInJarFiles();
        List inJarCounts = task.getInJarCounts();

        // Then: Input jars should still be tracked
        assertEquals(2, inJars.size(), "Should have 2 input jars");
        assertEquals(1, inJarCounts.size(), "Should have 1 count entry");
        assertEquals(0, inJarCounts.get(0), "Count should be 0 (no input jars before output)");
    }

    /**
     * Tests that injars() stores objects without any validation.
     */
    @Test
    public void testInjars_noValidation() throws Exception {
        // Given: Various types of objects
        String string = "some.jar";
        Integer integer = 42;
        Object customObject = new Object();

        // When: Calling injars() with any object type
        task.injars(string);
        task.injars(integer);
        task.injars(customObject);

        // Then: All objects should be stored without validation
        List jarFiles = task.getInJarFiles();
        assertEquals(3, jarFiles.size(), "Should have 3 entries");
        assertSame(string, jarFiles.get(0), "First should be the string");
        assertSame(integer, jarFiles.get(1), "Second should be the integer");
        assertSame(customObject, jarFiles.get(2), "Third should be the custom object");
    }

    // ==================== Tests for injars(Map, Object) ====================

    /**
     * Tests that injars(Map, Object) adds a jar with a filter Map.
     */
    @Test
    public void testInjarsWithMap_singleJarWithFilter() throws Exception {
        // Given: A jar path and a filter Map
        String jarPath = "input.jar";
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("filter", "!**.class");

        // When: Calling injars() with filters
        task.injars(filterArgs, jarPath);

        // Then: Both jar and filter should be stored
        List jarFiles = task.getInJarFiles();
        List filters = task.getInJarFilters();

        assertEquals(1, jarFiles.size(), "Should have 1 jar entry");
        assertEquals(jarPath, jarFiles.get(0), "Jar path should be stored");

        assertEquals(1, filters.size(), "Should have 1 filter entry");
        assertSame(filterArgs, filters.get(0), "Filter Map should be stored");
    }

    /**
     * Tests that injars(Map, Object) handles null filter Map (same as injars(Object)).
     */
    @Test
    public void testInjarsWithMap_nullFilterMap() throws Exception {
        // Given: A jar path and null filters
        String jarPath = "input.jar";
        Map<String, String> nullFilters = null;

        // When: Calling injars() with null filters
        task.injars(nullFilters, jarPath);

        // Then: Jar should be stored with null filter
        List jarFiles = task.getInJarFiles();
        List filters = task.getInJarFilters();

        assertEquals(1, jarFiles.size(), "Should have 1 jar entry");
        assertEquals(jarPath, jarFiles.get(0), "Jar path should be stored");

        assertEquals(1, filters.size(), "Should have 1 filter entry");
        assertNull(filters.get(0), "Filter should be null");
    }

    /**
     * Tests that injars(Map, Object) handles empty filter Map.
     */
    @Test
    public void testInjarsWithMap_emptyFilterMap() throws Exception {
        // Given: A jar path and empty filter Map
        String jarPath = "input.jar";
        Map<String, String> emptyFilters = new HashMap<>();

        // When: Calling injars() with empty filters
        task.injars(emptyFilters, jarPath);

        // Then: Jar and empty Map should be stored
        List jarFiles = task.getInJarFiles();
        List filters = task.getInJarFilters();

        assertEquals(1, jarFiles.size(), "Should have 1 jar entry");
        assertEquals(1, filters.size(), "Should have 1 filter entry");
        assertSame(emptyFilters, filters.get(0), "Empty filter Map should be stored");
        assertTrue(((Map) filters.get(0)).isEmpty(), "Filter Map should be empty");
    }

    /**
     * Tests that injars(Map, Object) handles the 'filter' key.
     */
    @Test
    public void testInjarsWithMap_filterKey() throws Exception {
        // Given: A jar with 'filter' key
        String jarPath = "input.jar";
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("filter", "!**.class,!**.xml");

        // When: Calling injars()
        task.injars(filterArgs, jarPath);

        // Then: Filter should be preserved
        List filters = task.getInJarFilters();
        Map storedFilter = (Map) filters.get(0);
        assertEquals("!**.class,!**.xml", storedFilter.get("filter"),
            "Filter value should be preserved");
    }

    /**
     * Tests that injars(Map, Object) handles the 'jarfilter' key.
     */
    @Test
    public void testInjarsWithMap_jarfilterKey() throws Exception {
        // Given: A jar with 'jarfilter' key
        String jarPath = "input.jar";
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("jarfilter", "!META-INF/**");

        // When: Calling injars()
        task.injars(filterArgs, jarPath);

        // Then: Jarfilter should be preserved
        List filters = task.getInJarFilters();
        Map storedFilter = (Map) filters.get(0);
        assertEquals("!META-INF/**", storedFilter.get("jarfilter"),
            "Jarfilter value should be preserved");
    }

    /**
     * Tests that injars(Map, Object) handles the 'aarfilter' key.
     */
    @Test
    public void testInjarsWithMap_aarfilterKey() throws Exception {
        // Given: An AAR with 'aarfilter' key
        String aarPath = "library.aar";
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("aarfilter", "!res/**,!assets/**");

        // When: Calling injars()
        task.injars(filterArgs, aarPath);

        // Then: Aarfilter should be preserved
        List filters = task.getInJarFilters();
        Map storedFilter = (Map) filters.get(0);
        assertEquals("!res/**,!assets/**", storedFilter.get("aarfilter"),
            "Aarfilter value should be preserved");
    }

    /**
     * Tests that injars(Map, Object) handles the 'apkfilter' key.
     */
    @Test
    public void testInjarsWithMap_apkfilterKey() throws Exception {
        // Given: An APK with 'apkfilter' key
        String apkPath = "app.apk";
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("apkfilter", "!assets/**");

        // When: Calling injars()
        task.injars(filterArgs, apkPath);

        // Then: Apkfilter should be preserved
        List filters = task.getInJarFilters();
        Map storedFilter = (Map) filters.get(0);
        assertEquals("!assets/**", storedFilter.get("apkfilter"),
            "Apkfilter value should be preserved");
    }

    /**
     * Tests that injars(Map, Object) handles multiple filter keys in one Map.
     */
    @Test
    public void testInjarsWithMap_multipleFilterKeys() throws Exception {
        // Given: A jar with multiple filter keys
        String jarPath = "input.jar";
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("filter", "!**.class");
        filterArgs.put("jarfilter", "!META-INF/**");
        filterArgs.put("feature", "myFeature");

        // When: Calling injars()
        task.injars(filterArgs, jarPath);

        // Then: All filter keys should be preserved
        List filters = task.getInJarFilters();
        Map storedFilter = (Map) filters.get(0);
        assertEquals(3, storedFilter.size(), "Should have 3 keys");
        assertEquals("!**.class", storedFilter.get("filter"));
        assertEquals("!META-INF/**", storedFilter.get("jarfilter"));
        assertEquals("myFeature", storedFilter.get("feature"));
    }

    /**
     * Tests that injars(Map, Object) handles the 'feature' key.
     */
    @Test
    public void testInjarsWithMap_featureKey() throws Exception {
        // Given: A jar with 'feature' key
        String jarPath = "input.jar";
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("feature", "dynamicFeature");

        // When: Calling injars()
        task.injars(filterArgs, jarPath);

        // Then: Feature should be preserved
        List filters = task.getInJarFilters();
        Map storedFilter = (Map) filters.get(0);
        assertEquals("dynamicFeature", storedFilter.get("feature"),
            "Feature value should be preserved");
    }

    /**
     * Tests that injars(Map, Object) stores the Map by reference, not by copy.
     */
    @Test
    public void testInjarsWithMap_storesMapByReference() throws Exception {
        // Given: A filter Map
        String jarPath = "input.jar";
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("filter", "!**.class");

        // When: Calling injars()
        task.injars(filterArgs, jarPath);

        // Then: The stored Map should be the same instance
        List filters = task.getInJarFilters();
        assertSame(filterArgs, filters.get(0), "Filter Map should be stored by reference");

        // And: Modifications to original Map should affect stored Map
        filterArgs.put("newKey", "newValue");
        Map storedFilter = (Map) filters.get(0);
        assertEquals("newValue", storedFilter.get("newKey"),
            "Changes to original Map should be reflected");
    }

    /**
     * Tests that injars(Map, Object) can be called multiple times with different filters.
     */
    @Test
    public void testInjarsWithMap_multipleCallsWithDifferentFilters() throws Exception {
        // Given: Multiple jars with different filters
        Map<String, String> filter1 = new HashMap<>();
        filter1.put("filter", "!**.class");

        Map<String, String> filter2 = new HashMap<>();
        filter2.put("jarfilter", "!META-INF/**");

        // When: Calling injars() with different filters
        task.injars(filter1, "jar1.jar");
        task.injars(filter2, "jar2.jar");
        task.injars("jar3.jar"); // No filter

        // Then: All should be stored correctly
        List jarFiles = task.getInJarFiles();
        List filters = task.getInJarFilters();

        assertEquals(3, jarFiles.size(), "Should have 3 jar entries");
        assertEquals(3, filters.size(), "Should have 3 filter entries");

        assertSame(filter1, filters.get(0), "First filter should be filter1");
        assertSame(filter2, filters.get(1), "Second filter should be filter2");
        assertNull(filters.get(2), "Third filter should be null");
    }

    /**
     * Tests that injars(Map, Object) maintains correct correspondence with jar files.
     */
    @Test
    public void testInjarsWithMap_maintainsCorrespondence() throws Exception {
        // Given: Multiple jars with and without filters
        Map<String, String> filter1 = new HashMap<>();
        filter1.put("filter", "first");

        Map<String, String> filter2 = new HashMap<>();
        filter2.put("filter", "second");

        task.injars(filter1, "jar1.jar");
        task.injars("jar2.jar"); // No filter
        task.injars(filter2, "jar3.jar");
        task.injars("jar4.jar"); // No filter

        // When: Getting both lists
        List jarFiles = task.getInJarFiles();
        List filters = task.getInJarFilters();

        // Then: Correspondence should be maintained
        assertEquals(4, jarFiles.size(), "Should have 4 jar entries");
        assertEquals(4, filters.size(), "Should have 4 filter entries");

        assertEquals("jar1.jar", jarFiles.get(0));
        assertSame(filter1, filters.get(0));

        assertEquals("jar2.jar", jarFiles.get(1));
        assertNull(filters.get(1));

        assertEquals("jar3.jar", jarFiles.get(2));
        assertSame(filter2, filters.get(2));

        assertEquals("jar4.jar", jarFiles.get(3));
        assertNull(filters.get(3));
    }

    /**
     * Tests that injars(Map, Object) works with File objects.
     */
    @Test
    public void testInjarsWithMap_withFileObject() throws Exception {
        // Given: A File object with filters
        File jarFile = new File(tempDir.toFile(), "input.jar");
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("jarfilter", "!**.class");

        // When: Calling injars()
        task.injars(filterArgs, jarFile);

        // Then: File and filter should be stored
        List jarFiles = task.getInJarFiles();
        List filters = task.getInJarFilters();

        assertEquals(1, jarFiles.size(), "Should have 1 jar entry");
        assertSame(jarFile, jarFiles.get(0), "File object should be stored");
        assertSame(filterArgs, filters.get(0), "Filter should be stored");
    }

    /**
     * Tests that injars(Map, Object) handles all supported filter key types.
     */
    @Test
    public void testInjarsWithMap_allFilterKeyTypes() throws Exception {
        // Given: A jar with all filter key types
        String jarPath = "input.jar";
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("filter", "!**.txt");
        filterArgs.put("jarfilter", "!**.class");
        filterArgs.put("aarfilter", "!res/**");
        filterArgs.put("apkfilter", "!assets/**");
        filterArgs.put("warfilter", "!WEB-INF/**");
        filterArgs.put("earfilter", "!META-INF/**");
        filterArgs.put("jmodfilter", "!classes/**");
        filterArgs.put("zipfilter", "!temp/**");

        // When: Calling injars()
        task.injars(filterArgs, jarPath);

        // Then: All filter keys should be preserved
        List filters = task.getInJarFilters();
        Map storedFilter = (Map) filters.get(0);
        assertEquals(8, storedFilter.size(), "Should have 8 filter keys");
        assertEquals("!**.txt", storedFilter.get("filter"));
        assertEquals("!**.class", storedFilter.get("jarfilter"));
        assertEquals("!res/**", storedFilter.get("aarfilter"));
        assertEquals("!assets/**", storedFilter.get("apkfilter"));
        assertEquals("!WEB-INF/**", storedFilter.get("warfilter"));
        assertEquals("!META-INF/**", storedFilter.get("earfilter"));
        assertEquals("!classes/**", storedFilter.get("jmodfilter"));
        assertEquals("!temp/**", storedFilter.get("zipfilter"));
    }

    /**
     * Tests that injars(Map, Object) works in a realistic Android scenario with filters.
     */
    @Test
    public void testInjarsWithMap_realisticAndroidScenario() throws Exception {
        // Given: Android project with filtered inputs
        Map<String, String> classesFilter = new HashMap<>();
        classesFilter.put("filter", "!**.class");

        Map<String, String> aarFilter = new HashMap<>();
        aarFilter.put("aarfilter", "!res/**,!assets/**");

        // When: Adding filtered inputs
        task.injars(classesFilter, "build/intermediates/classes.jar");
        task.injars(aarFilter, "libs/support-v4.aar");
        task.injars("libs/unfiltered.jar"); // No filter

        // Then: All should be stored with correct filters
        List jarFiles = task.getInJarFiles();
        List filters = task.getInJarFilters();

        assertEquals(3, jarFiles.size(), "Should have 3 jars");
        assertSame(classesFilter, filters.get(0), "First should have classes filter");
        assertSame(aarFilter, filters.get(1), "Second should have AAR filter");
        assertNull(filters.get(2), "Third should have no filter");
    }

    /**
     * Tests that injars(Map, Object) handles complex filter patterns.
     */
    @Test
    public void testInjarsWithMap_complexFilterPatterns() throws Exception {
        // Given: Complex filter patterns
        String jarPath = "input.jar";
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("filter", "!**.class,!**.xml,**.properties");
        filterArgs.put("jarfilter", "!META-INF/**.RSA,!META-INF/**.SF,!META-INF/**.MF");

        // When: Calling injars()
        task.injars(filterArgs, jarPath);

        // Then: Complex patterns should be preserved
        List filters = task.getInJarFilters();
        Map storedFilter = (Map) filters.get(0);
        assertEquals("!**.class,!**.xml,**.properties", storedFilter.get("filter"));
        assertEquals("!META-INF/**.RSA,!META-INF/**.SF,!META-INF/**.MF",
            storedFilter.get("jarfilter"));
    }

    /**
     * Tests that injars(Map, Object) preserves filter values with special characters.
     */
    @Test
    public void testInjarsWithMap_filterValuesWithSpecialCharacters() throws Exception {
        // Given: Filter values with special characters
        String jarPath = "input.jar";
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("filter", "!com/example/**/*.class");
        filterArgs.put("jarfilter", "!**/$*.class");

        // When: Calling injars()
        task.injars(filterArgs, jarPath);

        // Then: Special characters should be preserved
        List filters = task.getInJarFilters();
        Map storedFilter = (Map) filters.get(0);
        assertEquals("!com/example/**/*.class", storedFilter.get("filter"));
        assertEquals("!**/$*.class", storedFilter.get("jarfilter"));
    }

    /**
     * Tests that injars(Map, Object) works correctly in a complex workflow.
     */
    @Test
    public void testInjarsWithMap_complexWorkflow() throws Exception {
        // Given: A complex workflow with filtered and unfiltered inputs
        Map<String, String> filter1 = new HashMap<>();
        filter1.put("filter", "!**.class");

        task.injars(filter1, "app-classes.jar");
        task.injars("app-resources.jar"); // No filter
        task.outjars("app-combined.jar");

        Map<String, String> filter2 = new HashMap<>();
        filter2.put("jarfilter", "!META-INF/**");

        task.injars(filter2, "lib1.jar");
        task.injars("lib2.jar"); // No filter
        task.outjars("libs-combined.jar");

        // When: Getting the results
        List inJars = task.getInJarFiles();
        List filters = task.getInJarFilters();
        List inJarCounts = task.getInJarCounts();

        // Then: All should be tracked correctly with proper filters
        assertEquals(4, inJars.size(), "Should have 4 input jars");
        assertEquals(4, filters.size(), "Should have 4 filter entries");

        assertSame(filter1, filters.get(0), "First should have filter");
        assertNull(filters.get(1), "Second should have no filter");
        assertSame(filter2, filters.get(2), "Third should have filter");
        assertNull(filters.get(3), "Fourth should have no filter");

        assertEquals(2, inJarCounts.get(0), "First count should be 2");
        assertEquals(4, inJarCounts.get(1), "Second count should be 4");
    }

    /**
     * Tests that injars(Map, Object) handles duplicate jars with different filters.
     */
    @Test
    public void testInjarsWithMap_duplicateJarsWithDifferentFilters() throws Exception {
        // Given: Same jar added multiple times with different filters
        String jarPath = "input.jar";

        Map<String, String> filter1 = new HashMap<>();
        filter1.put("filter", "!**.class");

        Map<String, String> filter2 = new HashMap<>();
        filter2.put("filter", "!**.xml");

        // When: Adding same jar with different filters
        task.injars(filter1, jarPath);
        task.injars(filter2, jarPath);
        task.injars(jarPath); // No filter

        // Then: All entries should be stored with respective filters
        List jarFiles = task.getInJarFiles();
        List filters = task.getInJarFilters();

        assertEquals(3, jarFiles.size(), "Should have 3 entries");
        assertEquals(jarPath, jarFiles.get(0));
        assertEquals(jarPath, jarFiles.get(1));
        assertEquals(jarPath, jarFiles.get(2));

        assertSame(filter1, filters.get(0));
        assertSame(filter2, filters.get(1));
        assertNull(filters.get(2));
    }

    /**
     * Tests that injars(Map, Object) handles filters with empty string values.
     */
    @Test
    public void testInjarsWithMap_emptyStringFilterValues() throws Exception {
        // Given: Filter with empty string values
        String jarPath = "input.jar";
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("filter", "");
        filterArgs.put("jarfilter", "");

        // When: Calling injars()
        task.injars(filterArgs, jarPath);

        // Then: Empty string values should be preserved
        List filters = task.getInJarFilters();
        Map storedFilter = (Map) filters.get(0);
        assertEquals("", storedFilter.get("filter"), "Empty filter should be preserved");
        assertEquals("", storedFilter.get("jarfilter"), "Empty jarfilter should be preserved");
    }

    /**
     * Tests that both injars() methods can be used interchangeably.
     */
    @Test
    public void testInjarsWithMap_interchangeableWithNoArgVersion() throws Exception {
        // Given: Mix of filtered and unfiltered calls
        Map<String, String> filter = new HashMap<>();
        filter.put("filter", "!**.class");

        // When: Using both versions
        task.injars("jar1.jar");
        task.injars(filter, "jar2.jar");
        task.injars("jar3.jar");
        task.injars(null, "jar4.jar");

        // Then: All should work correctly
        List jarFiles = task.getInJarFiles();
        List filters = task.getInJarFilters();

        assertEquals(4, jarFiles.size(), "Should have 4 jar entries");
        assertEquals(4, filters.size(), "Should have 4 filter entries");

        assertNull(filters.get(0), "First should have no filter");
        assertSame(filter, filters.get(1), "Second should have filter");
        assertNull(filters.get(2), "Third should have no filter");
        assertNull(filters.get(3), "Fourth should have no filter");
    }
}
