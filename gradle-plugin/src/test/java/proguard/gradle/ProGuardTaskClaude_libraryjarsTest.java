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
 * Tests for {@link ProGuardTask#libraryjars(Object)} and {@link ProGuardTask#libraryjars(Map, Object)}.
 *
 * This test class verifies that both libraryjars methods correctly add library jar file objects
 * to the internal list.
 *
 * Method signature: libraryjars(Object libraryJarFiles)
 * - Adds the library jar file object to the libraryJarFiles list
 * - Adds a null entry to the libraryJarFilters list (since no filters are specified)
 * - This is a convenience method that delegates to libraryjars(Map, Object) with null filters
 *
 * Method signature: libraryjars(Map filterArgs, Object libraryJarFiles)
 * - Adds the library jar file object to the libraryJarFiles list
 * - Adds the filterArgs Map to the libraryJarFilters list
 * - Supports filter keys: filter, jarfilter, aarfilter, apkfilter, warfilter, earfilter, jmodfilter, zipfilter
 * - Stores the Map by reference (not a copy)
 *
 * Unlike configuration(), this method does NOT flatten Collections - it adds the
 * object as-is to the list.
 *
 * Key behavior: Library jars are runtime dependencies that are needed for processing
 * but should not be included in the output. Common examples include android.jar,
 * rt.jar, and other platform libraries.
 */
public class ProGuardTaskClaude_libraryjarsTest {

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
     * Tests that libraryjars() adds a single String path to the library jar files list.
     */
    @Test
    public void testLibraryjars_singleString() throws Exception {
        // Given: A String path
        String jarPath = "android.jar";

        // When: Calling libraryjars()
        task.libraryjars(jarPath);

        // Then: The library jar files list should contain the string
        List jarFiles = task.getLibraryJarFiles();
        assertNotNull(jarFiles, "Library jar files list should not be null");
        assertEquals(1, jarFiles.size(), "Should have 1 entry");
        assertEquals(jarPath, jarFiles.get(0), "Entry should be the string path");
    }

    /**
     * Tests that libraryjars() adds a null filter entry when called without filters.
     */
    @Test
    public void testLibraryjars_addsNullFilter() throws Exception {
        // Given: A String path
        String jarPath = "android.jar";

        // When: Calling libraryjars() without filters
        task.libraryjars(jarPath);

        // Then: The filters list should have a null entry
        List filters = task.getLibraryJarFilters();
        assertNotNull(filters, "Filters list should not be null");
        assertEquals(1, filters.size(), "Should have 1 filter entry");
        assertNull(filters.get(0), "Filter entry should be null when no filters specified");
    }

    /**
     * Tests that libraryjars() adds a single File object to the library jar files list.
     */
    @Test
    public void testLibraryjars_singleFile() throws Exception {
        // Given: A File object
        File jarFile = new File(tempDir.toFile(), "android.jar");

        // When: Calling libraryjars()
        task.libraryjars(jarFile);

        // Then: The library jar files list should contain the File object
        List jarFiles = task.getLibraryJarFiles();
        assertNotNull(jarFiles, "Library jar files list should not be null");
        assertEquals(1, jarFiles.size(), "Should have 1 entry");
        assertSame(jarFile, jarFiles.get(0), "Entry should be the same File object");
    }

    /**
     * Tests that libraryjars() can be called multiple times and accumulates entries.
     */
    @Test
    public void testLibraryjars_multipleCalls() throws Exception {
        // Given: Multiple library jars
        String jar1 = "android.jar";
        String jar2 = "rt.jar";
        String jar3 = "jsse.jar";

        // When: Calling libraryjars() multiple times
        task.libraryjars(jar1);
        task.libraryjars(jar2);
        task.libraryjars(jar3);

        // Then: All entries should be accumulated
        List jarFiles = task.getLibraryJarFiles();
        assertEquals(3, jarFiles.size(), "Should have 3 entries");
        assertEquals(jar1, jarFiles.get(0), "First entry should be jar1");
        assertEquals(jar2, jarFiles.get(1), "Second entry should be jar2");
        assertEquals(jar3, jarFiles.get(2), "Third entry should be jar3");
    }

    /**
     * Tests that libraryjars() maintains 1-to-1 correspondence with filters.
     */
    @Test
    public void testLibraryjars_maintainsFilterCorrespondence() throws Exception {
        // Given: Multiple library jars
        task.libraryjars("jar1.jar");
        task.libraryjars("jar2.jar");
        task.libraryjars("jar3.jar");

        // When: Getting both lists
        List jarFiles = task.getLibraryJarFiles();
        List filters = task.getLibraryJarFilters();

        // Then: Both lists should have same size with null filters
        assertEquals(jarFiles.size(), filters.size(), "Lists should have same size");
        assertEquals(3, filters.size(), "Should have 3 filter entries");
        assertNull(filters.get(0), "All filters should be null");
        assertNull(filters.get(1), "All filters should be null");
        assertNull(filters.get(2), "All filters should be null");
    }

    /**
     * Tests that libraryjars() preserves the order of library jars as they were added.
     */
    @Test
    public void testLibraryjars_preservesOrder() throws Exception {
        // Given: Library jars added in specific order
        String first = "first.jar";
        String second = "second.jar";
        String third = "third.jar";

        // When: Calling libraryjars() in order
        task.libraryjars(first);
        task.libraryjars(second);
        task.libraryjars(third);

        // Then: Order should be preserved
        List jarFiles = task.getLibraryJarFiles();
        assertEquals(3, jarFiles.size(), "Should have 3 entries");
        assertEquals(first, jarFiles.get(0), "First should be first.jar");
        assertEquals(second, jarFiles.get(1), "Second should be second.jar");
        assertEquals(third, jarFiles.get(2), "Third should be third.jar");
    }

    /**
     * Tests that libraryjars() handles relative path strings.
     */
    @Test
    public void testLibraryjars_relativePathString() throws Exception {
        // Given: A relative path string
        String relativePath = "libs/android.jar";

        // When: Calling libraryjars()
        task.libraryjars(relativePath);

        // Then: The relative path should be stored as-is
        List jarFiles = task.getLibraryJarFiles();
        assertEquals(1, jarFiles.size(), "Should have 1 entry");
        assertEquals(relativePath, jarFiles.get(0), "Should store the relative path string");
    }

    /**
     * Tests that libraryjars() handles absolute path strings.
     */
    @Test
    public void testLibraryjars_absolutePathString() throws Exception {
        // Given: An absolute path string
        String absolutePath = new File(tempDir.toFile(), "android.jar").getAbsolutePath();

        // When: Calling libraryjars()
        task.libraryjars(absolutePath);

        // Then: The absolute path should be stored as-is
        List jarFiles = task.getLibraryJarFiles();
        assertEquals(1, jarFiles.size(), "Should have 1 entry");
        assertEquals(absolutePath, jarFiles.get(0), "Should store the absolute path string");
    }

    /**
     * Tests that libraryjars() handles null value.
     */
    @Test
    public void testLibraryjars_nullValue() throws Exception {
        // Given: A null value
        Object nullValue = null;

        // When: Calling libraryjars()
        task.libraryjars(nullValue);

        // Then: Null should be added to the list
        List jarFiles = task.getLibraryJarFiles();
        assertEquals(1, jarFiles.size(), "Should have 1 entry");
        assertNull(jarFiles.get(0), "Entry should be null");
    }

    /**
     * Tests that libraryjars() handles empty string.
     */
    @Test
    public void testLibraryjars_emptyString() throws Exception {
        // Given: An empty string
        String emptyString = "";

        // When: Calling libraryjars()
        task.libraryjars(emptyString);

        // Then: The empty string should be added
        List jarFiles = task.getLibraryJarFiles();
        assertEquals(1, jarFiles.size(), "Should have 1 entry");
        assertEquals("", jarFiles.get(0), "Should store the empty string");
    }

    /**
     * Tests that libraryjars() allows duplicate entries.
     */
    @Test
    public void testLibraryjars_duplicates() throws Exception {
        // Given: Same jar added multiple times
        String jarPath = "android.jar";

        // When: Calling libraryjars() multiple times with same path
        task.libraryjars(jarPath);
        task.libraryjars(jarPath);
        task.libraryjars(jarPath);

        // Then: All duplicates should be added
        List jarFiles = task.getLibraryJarFiles();
        assertEquals(3, jarFiles.size(), "Should have 3 entries (duplicates allowed)");
        assertEquals(jarPath, jarFiles.get(0), "All entries should be the same");
        assertEquals(jarPath, jarFiles.get(1), "All entries should be the same");
        assertEquals(jarPath, jarFiles.get(2), "All entries should be the same");
    }

    /**
     * Tests that libraryjars() handles directory paths.
     */
    @Test
    public void testLibraryjars_directoryPath() throws Exception {
        // Given: A directory path
        File directory = new File(tempDir.toFile(), "libs");

        // When: Calling libraryjars()
        task.libraryjars(directory);

        // Then: The directory should be stored
        List jarFiles = task.getLibraryJarFiles();
        assertEquals(1, jarFiles.size(), "Should have 1 entry");
        assertSame(directory, jarFiles.get(0), "Should store the directory File object");
    }

    /**
     * Tests that libraryjars() handles various archive file types.
     */
    @Test
    public void testLibraryjars_variousFileTypes() throws Exception {
        // Given: Various file types
        String jarFile = "rt.jar";
        String aarFile = "library.aar";
        String zipFile = "archive.zip";
        String warFile = "webapp.war";

        // When: Calling libraryjars() with various types
        task.libraryjars(jarFile);
        task.libraryjars(aarFile);
        task.libraryjars(zipFile);
        task.libraryjars(warFile);

        // Then: All types should be stored
        List jarFiles = task.getLibraryJarFiles();
        assertEquals(4, jarFiles.size(), "Should have 4 entries");
        assertEquals(jarFile, jarFiles.get(0), "First should be jar");
        assertEquals(aarFile, jarFiles.get(1), "Second should be aar");
        assertEquals(zipFile, jarFiles.get(2), "Third should be zip");
        assertEquals(warFile, jarFiles.get(3), "Fourth should be war");
    }

    /**
     * Tests that libraryjars() does NOT flatten Collections (unlike configuration()).
     * Collections should be added as-is.
     */
    @Test
    public void testLibraryjars_doesNotFlattenCollections() throws Exception {
        // Given: A List of jar files
        java.util.List<String> jarList = java.util.Arrays.asList("jar1.jar", "jar2.jar");

        // When: Calling libraryjars() with the List
        task.libraryjars(jarList);

        // Then: The List itself should be added (not flattened)
        List jarFiles = task.getLibraryJarFiles();
        assertEquals(1, jarFiles.size(), "Should have 1 entry (the List itself)");
        assertSame(jarList, jarFiles.get(0), "Entry should be the List object itself, not its elements");
    }

    /**
     * Tests that libraryjars() preserves object identity.
     */
    @Test
    public void testLibraryjars_preservesObjectIdentity() throws Exception {
        // Given: A specific File object
        File jarFile = new File(tempDir.toFile(), "android.jar");

        // When: Calling libraryjars()
        task.libraryjars(jarFile);

        // Then: The exact same object should be stored
        List jarFiles = task.getLibraryJarFiles();
        assertSame(jarFile, jarFiles.get(0), "Should store the exact same File object");
    }

    /**
     * Tests that libraryjars() is independent from injars.
     */
    @Test
    public void testLibraryjars_independentFromInjars() throws Exception {
        // Given: Input and library jars
        String inputJar = "input.jar";
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
     * Tests that libraryjars() is independent from outjars.
     */
    @Test
    public void testLibraryjars_independentFromOutjars() throws Exception {
        // Given: Library and output jars
        String libraryJar = "android.jar";
        String outputJar = "output.jar";

        // When: Calling both libraryjars() and outjars()
        task.libraryjars(libraryJar);
        task.outjars(outputJar);

        // Then: Each list should contain only its respective jar
        List libraryJars = task.getLibraryJarFiles();
        List outJars = task.getOutJarFiles();

        assertEquals(1, libraryJars.size(), "Should have 1 library jar");
        assertEquals(libraryJar, libraryJars.get(0), "Library jar should be correct");

        assertEquals(1, outJars.size(), "Should have 1 output jar");
        assertEquals(outputJar, outJars.get(0), "Output jar should be correct");

        assertFalse(libraryJars.contains(outputJar), "Library jars should not contain output jar");
        assertFalse(outJars.contains(libraryJar), "Output jars should not contain library jar");
    }

    /**
     * Tests that libraryjars() works in a realistic Android scenario.
     */
    @Test
    public void testLibraryjars_realisticAndroidScenario() throws Exception {
        // Given: A realistic Android project with input, library, and output jars
        task.injars("build/intermediates/classes.jar");
        task.libraryjars("${ANDROID_HOME}/platforms/android-28/android.jar");
        task.libraryjars("libs/support-library.jar");
        task.outjars("build/outputs/app-release.jar");

        // When: Getting the results
        List inJars = task.getInJarFiles();
        List libraryJars = task.getLibraryJarFiles();
        List outJars = task.getOutJarFiles();

        // Then: All should be tracked correctly
        assertEquals(1, inJars.size(), "Should have 1 input jar");
        assertEquals(2, libraryJars.size(), "Should have 2 library jars");
        assertEquals(1, outJars.size(), "Should have 1 output jar");

        assertEquals("${ANDROID_HOME}/platforms/android-28/android.jar", libraryJars.get(0));
        assertEquals("libs/support-library.jar", libraryJars.get(1));
    }

    /**
     * Tests that libraryjars() works in a realistic Java scenario.
     */
    @Test
    public void testLibraryjars_realisticJavaScenario() throws Exception {
        // Given: A realistic Java project with runtime library
        File inputJar = new File(tempDir.toFile(), "build/libs/app-1.0.jar");
        File rtJar = new File(System.getProperty("java.home"), "lib/rt.jar");
        File outputJar = new File(tempDir.toFile(), "build/libs/app-1.0-obfuscated.jar");

        task.injars(inputJar);
        task.libraryjars(rtJar);
        task.outjars(outputJar);

        // When: Getting the results
        List libraryJars = task.getLibraryJarFiles();

        // Then: Library jar should be tracked
        assertEquals(1, libraryJars.size(), "Should have 1 library jar");
        assertSame(rtJar, libraryJars.get(0));
    }

    /**
     * Tests that libraryjars() handles multiple platform libraries.
     */
    @Test
    public void testLibraryjars_multiplePlatformLibraries() throws Exception {
        // Given: Multiple platform libraries
        task.libraryjars("${JAVA_HOME}/lib/rt.jar");
        task.libraryjars("${JAVA_HOME}/lib/jce.jar");
        task.libraryjars("${JAVA_HOME}/lib/jsse.jar");

        // When: Getting the results
        List libraryJars = task.getLibraryJarFiles();

        // Then: All should be tracked
        assertEquals(3, libraryJars.size(), "Should have 3 library jars");
        assertEquals("${JAVA_HOME}/lib/rt.jar", libraryJars.get(0));
        assertEquals("${JAVA_HOME}/lib/jce.jar", libraryJars.get(1));
        assertEquals("${JAVA_HOME}/lib/jsse.jar", libraryJars.get(2));
    }

    /**
     * Tests that libraryjars() handles Gradle project dependencies.
     */
    @Test
    public void testLibraryjars_gradleProjectDependencies() throws Exception {
        // Given: Gradle-style dependency paths
        String libraryJar = "build/libs/dependency-1.0.jar";

        // When: Calling libraryjars()
        task.libraryjars(libraryJar);

        // Then: Library should be stored
        List jarFiles = task.getLibraryJarFiles();
        assertEquals(1, jarFiles.size(), "Should have 1 entry");
        assertEquals(libraryJar, jarFiles.get(0));
    }

    /**
     * Tests that libraryjars() handles mixed String and File objects.
     */
    @Test
    public void testLibraryjars_mixedStringAndFile() throws Exception {
        // Given: Mix of String and File objects
        String stringPath = "android.jar";
        File fileObject = new File(tempDir.toFile(), "support.jar");
        String anotherString = "rt.jar";

        // When: Calling libraryjars()
        task.libraryjars(stringPath);
        task.libraryjars(fileObject);
        task.libraryjars(anotherString);

        // Then: Both types should be preserved
        List jarFiles = task.getLibraryJarFiles();
        assertEquals(3, jarFiles.size(), "Should have 3 entries");
        assertTrue(jarFiles.get(0) instanceof String, "First should be String");
        assertTrue(jarFiles.get(1) instanceof File, "Second should be File");
        assertTrue(jarFiles.get(2) instanceof String, "Third should be String");
    }

    /**
     * Tests that libraryjars() handles paths with spaces.
     */
    @Test
    public void testLibraryjars_pathsWithSpaces() throws Exception {
        // Given: A path with spaces
        String pathWithSpaces = "Program Files/Java/lib/rt.jar";

        // When: Calling libraryjars()
        task.libraryjars(pathWithSpaces);

        // Then: The path should be stored as-is
        List jarFiles = task.getLibraryJarFiles();
        assertEquals(1, jarFiles.size(), "Should have 1 entry");
        assertEquals(pathWithSpaces, jarFiles.get(0), "Should preserve path with spaces");
    }

    /**
     * Tests that libraryjars() handles paths with special characters.
     */
    @Test
    public void testLibraryjars_pathsWithSpecialCharacters() throws Exception {
        // Given: Paths with special characters
        String path1 = "android-28.jar";
        String path2 = "support_v7.jar";
        String path3 = "lib@platform.jar";

        // When: Calling libraryjars()
        task.libraryjars(path1);
        task.libraryjars(path2);
        task.libraryjars(path3);

        // Then: All paths should be stored
        List jarFiles = task.getLibraryJarFiles();
        assertEquals(3, jarFiles.size(), "Should have 3 entries");
        assertEquals(path1, jarFiles.get(0));
        assertEquals(path2, jarFiles.get(1));
        assertEquals(path3, jarFiles.get(2));
    }

    /**
     * Tests that libraryjars() handles Windows-style paths.
     */
    @Test
    public void testLibraryjars_windowsPaths() throws Exception {
        // Given: Windows-style paths
        String windowsPath = "C:\\Program Files\\Java\\jdk1.8.0\\lib\\rt.jar";

        // When: Calling libraryjars()
        task.libraryjars(windowsPath);

        // Then: The Windows path should be stored
        List jarFiles = task.getLibraryJarFiles();
        assertEquals(1, jarFiles.size(), "Should have 1 entry");
        assertEquals(windowsPath, jarFiles.get(0), "Should store Windows path as-is");
    }

    /**
     * Tests that libraryjars() handles Unix-style paths.
     */
    @Test
    public void testLibraryjars_unixPaths() throws Exception {
        // Given: Unix-style paths
        String unixPath = "/usr/lib/jvm/java-8-openjdk/jre/lib/rt.jar";

        // When: Calling libraryjars()
        task.libraryjars(unixPath);

        // Then: The Unix path should be stored
        List jarFiles = task.getLibraryJarFiles();
        assertEquals(1, jarFiles.size(), "Should have 1 entry");
        assertEquals(unixPath, jarFiles.get(0), "Should store Unix path as-is");
    }

    /**
     * Tests that libraryjars() interoperates correctly with configuration().
     */
    @Test
    public void testLibraryjars_interoperatesWithConfiguration() throws Exception {
        // Given: Mix of configuration files and library jars
        task.configuration("proguard-rules.pro");
        task.libraryjars("android.jar");
        task.configuration("proguard-debug.pro");

        // When: Getting both lists
        List configFiles = task.getConfigurationFiles();
        List libraryJars = task.getLibraryJarFiles();

        // Then: Each list should contain only its respective items
        assertEquals(2, configFiles.size(), "Should have 2 config files");
        assertEquals("proguard-rules.pro", configFiles.get(0));
        assertEquals("proguard-debug.pro", configFiles.get(1));

        assertEquals(1, libraryJars.size(), "Should have 1 library jar");
        assertEquals("android.jar", libraryJars.get(0));
    }

    /**
     * Tests that libraryjars() handles Android SDK path.
     */
    @Test
    public void testLibraryjars_androidSdkPath() throws Exception {
        // Given: Android SDK path
        String androidSdkPath = "${ANDROID_HOME}/platforms/android-28/android.jar";

        // When: Calling libraryjars()
        task.libraryjars(androidSdkPath);

        // Then: Path should be stored
        List jarFiles = task.getLibraryJarFiles();
        assertEquals(1, jarFiles.size(), "Should have 1 entry");
        assertEquals(androidSdkPath, jarFiles.get(0));
    }

    /**
     * Tests that libraryjars() stores objects without any validation.
     */
    @Test
    public void testLibraryjars_noValidation() throws Exception {
        // Given: Various types of objects
        String string = "android.jar";
        Integer integer = 42;
        Object customObject = new Object();

        // When: Calling libraryjars() with any object type
        task.libraryjars(string);
        task.libraryjars(integer);
        task.libraryjars(customObject);

        // Then: All objects should be stored without validation
        List jarFiles = task.getLibraryJarFiles();
        assertEquals(3, jarFiles.size(), "Should have 3 entries");
        assertSame(string, jarFiles.get(0), "First should be the string");
        assertSame(integer, jarFiles.get(1), "Second should be the integer");
        assertSame(customObject, jarFiles.get(2), "Third should be the custom object");
    }

    /**
     * Tests that libraryjars() handles complex project workflow.
     */
    @Test
    public void testLibraryjars_complexWorkflow() throws Exception {
        // Given: Complex project setup
        task.injars("app.jar");
        task.libraryjars("android.jar");
        task.libraryjars("support-v4.jar");
        task.libraryjars("support-v7.jar");
        task.outjars("app-obfuscated.jar");

        // When: Getting all lists
        List inJars = task.getInJarFiles();
        List libraryJars = task.getLibraryJarFiles();
        List outJars = task.getOutJarFiles();

        // Then: All should be tracked correctly
        assertEquals(1, inJars.size(), "Should have 1 input jar");
        assertEquals(3, libraryJars.size(), "Should have 3 library jars");
        assertEquals(1, outJars.size(), "Should have 1 output jar");

        assertEquals("android.jar", libraryJars.get(0));
        assertEquals("support-v4.jar", libraryJars.get(1));
        assertEquals("support-v7.jar", libraryJars.get(2));
    }

    /**
     * Tests that libraryjars() handles variable substitution syntax.
     */
    @Test
    public void testLibraryjars_variableSubstitution() throws Exception {
        // Given: Path with variable substitution
        String pathWithVar = "${JAVA_HOME}/jre/lib/rt.jar";

        // When: Calling libraryjars()
        task.libraryjars(pathWithVar);

        // Then: Path with variable should be stored as-is
        List jarFiles = task.getLibraryJarFiles();
        assertEquals(1, jarFiles.size(), "Should have 1 entry");
        assertEquals(pathWithVar, jarFiles.get(0), "Should store path with variable as-is");
    }

    /**
     * Tests that libraryjars() handles nested Collections.
     */
    @Test
    public void testLibraryjars_nestedCollections() throws Exception {
        // Given: A Collection containing another Collection
        java.util.List<String> innerList = java.util.Arrays.asList("jar1.jar", "jar2.jar");
        java.util.List<Object> outerList = java.util.Arrays.asList(innerList, "jar3.jar");

        // When: Calling libraryjars() with nested Collection
        task.libraryjars(outerList);

        // Then: The Collection should be stored as-is (not flattened)
        List jarFiles = task.getLibraryJarFiles();
        assertEquals(1, jarFiles.size(), "Should have 1 entry (the Collection itself)");
        assertSame(outerList, jarFiles.get(0), "Should store Collection as-is");
    }

    /**
     * Tests that libraryjars() handles large number of libraries.
     */
    @Test
    public void testLibraryjars_largeNumberOfLibraries() throws Exception {
        // Given: Many library jars
        for (int i = 0; i < 100; i++) {
            task.libraryjars("library-" + i + ".jar");
        }

        // When: Getting the results
        List jarFiles = task.getLibraryJarFiles();

        // Then: All should be stored
        assertEquals(100, jarFiles.size(), "Should have 100 entries");
        assertEquals("library-0.jar", jarFiles.get(0));
        assertEquals("library-99.jar", jarFiles.get(99));
    }

    /**
     * Tests that libraryjars() handles AAR files (Android Archive).
     */
    @Test
    public void testLibraryjars_aarFiles() throws Exception {
        // Given: AAR library files
        String aarFile = "support-library.aar";

        // When: Calling libraryjars()
        task.libraryjars(aarFile);

        // Then: AAR should be stored
        List jarFiles = task.getLibraryJarFiles();
        assertEquals(1, jarFiles.size(), "Should have 1 entry");
        assertEquals(aarFile, jarFiles.get(0));
    }

    /**
     * Tests that libraryjars() handles module paths (Java 9+).
     */
    @Test
    public void testLibraryjars_modulePaths() throws Exception {
        // Given: Java module paths
        String modulePath = "java.base.jmod";

        // When: Calling libraryjars()
        task.libraryjars(modulePath);

        // Then: Module path should be stored
        List jarFiles = task.getLibraryJarFiles();
        assertEquals(1, jarFiles.size(), "Should have 1 entry");
        assertEquals(modulePath, jarFiles.get(0));
    }

    // ==================== Tests for libraryjars(Map, Object) ====================

    /**
     * Tests that libraryjars(Map, Object) adds a single jar with filter Map.
     */
    @Test
    public void testLibraryjarsWithMap_singleJarWithFilter() throws Exception {
        // Given: A jar path and filter Map
        String jarPath = "android.jar";
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("filter", "!**.class");

        // When: Calling libraryjars(Map, Object)
        task.libraryjars(filterArgs, jarPath);

        // Then: Both jar and filter should be stored
        List jarFiles = task.getLibraryJarFiles();
        List filters = task.getLibraryJarFilters();

        assertEquals(1, jarFiles.size(), "Should have 1 jar entry");
        assertEquals(jarPath, jarFiles.get(0), "Jar path should be stored");
        assertSame(filterArgs, filters.get(0), "Filter Map should be stored");
    }

    /**
     * Tests that libraryjars(Map, Object) stores the filter Map by reference.
     */
    @Test
    public void testLibraryjarsWithMap_storesMapByReference() throws Exception {
        // Given: A filter Map
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("filter", "**.png");

        // When: Calling libraryjars(Map, Object)
        task.libraryjars(filterArgs, "android.jar");

        // Then: The exact same Map object should be stored
        List filters = task.getLibraryJarFilters();
        assertSame(filterArgs, filters.get(0), "Should store the exact same Map object");
    }

    /**
     * Tests that libraryjars(Map, Object) accepts null filter Map.
     */
    @Test
    public void testLibraryjarsWithMap_nullFilterMap() throws Exception {
        // Given: Null filter Map
        Map<String, String> nullFilter = null;

        // When: Calling libraryjars(Map, Object)
        task.libraryjars(nullFilter, "android.jar");

        // Then: Null should be stored
        List filters = task.getLibraryJarFilters();
        assertEquals(1, filters.size(), "Should have 1 filter entry");
        assertNull(filters.get(0), "Filter should be null");
    }

    /**
     * Tests that libraryjars(Map, Object) accepts empty filter Map.
     */
    @Test
    public void testLibraryjarsWithMap_emptyFilterMap() throws Exception {
        // Given: Empty filter Map
        Map<String, String> emptyFilter = new HashMap<>();

        // When: Calling libraryjars(Map, Object)
        task.libraryjars(emptyFilter, "android.jar");

        // Then: Empty Map should be stored
        List filters = task.getLibraryJarFilters();
        assertSame(emptyFilter, filters.get(0), "Empty Map should be stored");
        assertEquals(0, ((Map) filters.get(0)).size(), "Map should be empty");
    }

    /**
     * Tests that libraryjars(Map, Object) handles 'filter' key.
     */
    @Test
    public void testLibraryjarsWithMap_filterKey() throws Exception {
        // Given: Filter Map with 'filter' key
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("filter", "!com/example/**");

        // When: Calling libraryjars(Map, Object)
        task.libraryjars(filterArgs, "android.jar");

        // Then: Filter should be stored
        List filters = task.getLibraryJarFilters();
        Map storedFilter = (Map) filters.get(0);
        assertEquals("!com/example/**", storedFilter.get("filter"));
    }

    /**
     * Tests that libraryjars(Map, Object) handles 'jarfilter' key.
     */
    @Test
    public void testLibraryjarsWithMap_jarfilterKey() throws Exception {
        // Given: Filter Map with 'jarfilter' key
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("jarfilter", "!**.txt");

        // When: Calling libraryjars(Map, Object)
        task.libraryjars(filterArgs, "rt.jar");

        // Then: Filter should be stored
        List filters = task.getLibraryJarFilters();
        Map storedFilter = (Map) filters.get(0);
        assertEquals("!**.txt", storedFilter.get("jarfilter"));
    }

    /**
     * Tests that libraryjars(Map, Object) handles 'aarfilter' key.
     */
    @Test
    public void testLibraryjarsWithMap_aarfilterKey() throws Exception {
        // Given: Filter Map with 'aarfilter' key
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("aarfilter", "!**/LICENSE");

        // When: Calling libraryjars(Map, Object)
        task.libraryjars(filterArgs, "support-library.aar");

        // Then: Filter should be stored
        List filters = task.getLibraryJarFilters();
        Map storedFilter = (Map) filters.get(0);
        assertEquals("!**/LICENSE", storedFilter.get("aarfilter"));
    }

    /**
     * Tests that libraryjars(Map, Object) handles 'apkfilter' key.
     */
    @Test
    public void testLibraryjarsWithMap_apkfilterKey() throws Exception {
        // Given: Filter Map with 'apkfilter' key
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("apkfilter", "!resources.arsc");

        // When: Calling libraryjars(Map, Object)
        task.libraryjars(filterArgs, "library.apk");

        // Then: Filter should be stored
        List filters = task.getLibraryJarFilters();
        Map storedFilter = (Map) filters.get(0);
        assertEquals("!resources.arsc", storedFilter.get("apkfilter"));
    }

    /**
     * Tests that libraryjars(Map, Object) handles 'warfilter' key.
     */
    @Test
    public void testLibraryjarsWithMap_warfilterKey() throws Exception {
        // Given: Filter Map with 'warfilter' key
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("warfilter", "!WEB-INF/**");

        // When: Calling libraryjars(Map, Object)
        task.libraryjars(filterArgs, "servlet-api.war");

        // Then: Filter should be stored
        List filters = task.getLibraryJarFilters();
        Map storedFilter = (Map) filters.get(0);
        assertEquals("!WEB-INF/**", storedFilter.get("warfilter"));
    }

    /**
     * Tests that libraryjars(Map, Object) handles 'earfilter' key.
     */
    @Test
    public void testLibraryjarsWithMap_earfilterKey() throws Exception {
        // Given: Filter Map with 'earfilter' key
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("earfilter", "!META-INF/**");

        // When: Calling libraryjars(Map, Object)
        task.libraryjars(filterArgs, "enterprise.ear");

        // Then: Filter should be stored
        List filters = task.getLibraryJarFilters();
        Map storedFilter = (Map) filters.get(0);
        assertEquals("!META-INF/**", storedFilter.get("earfilter"));
    }

    /**
     * Tests that libraryjars(Map, Object) handles 'jmodfilter' key.
     */
    @Test
    public void testLibraryjarsWithMap_jmodfilterKey() throws Exception {
        // Given: Filter Map with 'jmodfilter' key
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("jmodfilter", "!classes/**");

        // When: Calling libraryjars(Map, Object)
        task.libraryjars(filterArgs, "java.base.jmod");

        // Then: Filter should be stored
        List filters = task.getLibraryJarFilters();
        Map storedFilter = (Map) filters.get(0);
        assertEquals("!classes/**", storedFilter.get("jmodfilter"));
    }

    /**
     * Tests that libraryjars(Map, Object) handles 'zipfilter' key.
     */
    @Test
    public void testLibraryjarsWithMap_zipfilterKey() throws Exception {
        // Given: Filter Map with 'zipfilter' key
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("zipfilter", "!**.txt");

        // When: Calling libraryjars(Map, Object)
        task.libraryjars(filterArgs, "library.zip");

        // Then: Filter should be stored
        List filters = task.getLibraryJarFilters();
        Map storedFilter = (Map) filters.get(0);
        assertEquals("!**.txt", storedFilter.get("zipfilter"));
    }

    /**
     * Tests that libraryjars(Map, Object) handles multiple filter keys in one Map.
     */
    @Test
    public void testLibraryjarsWithMap_multipleFilterKeys() throws Exception {
        // Given: Filter Map with multiple keys
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("filter", "!**.png");
        filterArgs.put("jarfilter", "!META-INF/**");

        // When: Calling libraryjars(Map, Object)
        task.libraryjars(filterArgs, "android.jar");

        // Then: All filters should be stored
        List filters = task.getLibraryJarFilters();
        Map storedFilter = (Map) filters.get(0);
        assertEquals(2, storedFilter.size(), "Should have 2 filter keys");
        assertEquals("!**.png", storedFilter.get("filter"));
        assertEquals("!META-INF/**", storedFilter.get("jarfilter"));
    }

    /**
     * Tests that libraryjars(Map, Object) can be called multiple times.
     */
    @Test
    public void testLibraryjarsWithMap_multipleCalls() throws Exception {
        // Given: Multiple filter Maps
        Map<String, String> filter1 = new HashMap<>();
        filter1.put("filter", "!**.png");

        Map<String, String> filter2 = new HashMap<>();
        filter2.put("filter", "!**.txt");

        Map<String, String> filter3 = new HashMap<>();
        filter3.put("filter", "!**.xml");

        // When: Calling libraryjars(Map, Object) multiple times
        task.libraryjars(filter1, "android.jar");
        task.libraryjars(filter2, "rt.jar");
        task.libraryjars(filter3, "jsse.jar");

        // Then: All should be accumulated
        List jarFiles = task.getLibraryJarFiles();
        List filters = task.getLibraryJarFilters();

        assertEquals(3, jarFiles.size(), "Should have 3 jar entries");
        assertEquals(3, filters.size(), "Should have 3 filter entries");

        assertSame(filter1, filters.get(0));
        assertSame(filter2, filters.get(1));
        assertSame(filter3, filters.get(2));
    }

    /**
     * Tests that libraryjars(Map, Object) maintains 1-to-1 correspondence with jars.
     */
    @Test
    public void testLibraryjarsWithMap_maintainsCorrespondence() throws Exception {
        // Given: Mix of filtered and unfiltered library jars
        Map<String, String> filter = new HashMap<>();
        filter.put("filter", "!**.png");

        task.libraryjars("jar1.jar");  // No filter
        task.libraryjars(filter, "jar2.jar");  // With filter
        task.libraryjars(null, "jar3.jar");  // Null filter

        // When: Getting both lists
        List jarFiles = task.getLibraryJarFiles();
        List filters = task.getLibraryJarFilters();

        // Then: Lists should have same size with correct correspondence
        assertEquals(3, jarFiles.size(), "Should have 3 jar entries");
        assertEquals(3, filters.size(), "Should have 3 filter entries");

        assertNull(filters.get(0), "First filter should be null");
        assertSame(filter, filters.get(1), "Second filter should be the Map");
        assertNull(filters.get(2), "Third filter should be null");
    }

    /**
     * Tests that libraryjars(Map, Object) handles File objects with filters.
     */
    @Test
    public void testLibraryjarsWithMap_fileObjectWithFilter() throws Exception {
        // Given: A File object and filter Map
        File jarFile = new File(tempDir.toFile(), "android.jar");
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("filter", "!**.class");

        // When: Calling libraryjars(Map, Object)
        task.libraryjars(filterArgs, jarFile);

        // Then: Both should be stored
        List jarFiles = task.getLibraryJarFiles();
        List filters = task.getLibraryJarFilters();

        assertSame(jarFile, jarFiles.get(0), "File object should be stored");
        assertSame(filterArgs, filters.get(0), "Filter Map should be stored");
    }

    /**
     * Tests that libraryjars(Map, Object) works with Collections (not flattened).
     */
    @Test
    public void testLibraryjarsWithMap_collectionNotFlattened() throws Exception {
        // Given: A Collection and filter Map
        java.util.List<String> jarList = java.util.Arrays.asList("jar1.jar", "jar2.jar");
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("filter", "!**.class");

        // When: Calling libraryjars(Map, Object)
        task.libraryjars(filterArgs, jarList);

        // Then: Collection should be stored as-is (not flattened)
        List jarFiles = task.getLibraryJarFiles();
        List filters = task.getLibraryJarFilters();

        assertEquals(1, jarFiles.size(), "Should have 1 entry (the Collection itself)");
        assertSame(jarList, jarFiles.get(0), "Should store Collection as-is");
        assertSame(filterArgs, filters.get(0), "Filter should be stored");
    }

    /**
     * Tests that libraryjars(Map, Object) handles modification of filter Map after call.
     */
    @Test
    public void testLibraryjarsWithMap_filterMapModificationAfterCall() throws Exception {
        // Given: A filter Map
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("filter", "!**.png");

        // When: Calling libraryjars(Map, Object) and then modifying the Map
        task.libraryjars(filterArgs, "android.jar");
        filterArgs.put("jarfilter", "!**.txt");

        // Then: Stored Map should reflect modifications (stored by reference)
        List filters = task.getLibraryJarFilters();
        Map storedFilter = (Map) filters.get(0);
        assertEquals(2, storedFilter.size(), "Map should have 2 entries after modification");
        assertEquals("!**.png", storedFilter.get("filter"));
        assertEquals("!**.txt", storedFilter.get("jarfilter"));
    }

    /**
     * Tests that libraryjars(Map, Object) handles complex workflow with filters.
     */
    @Test
    public void testLibraryjarsWithMap_complexWorkflowWithFilters() throws Exception {
        // Given: Complex workflow with input/library/output and filters
        task.injars("app.jar");

        Map<String, String> filter1 = new HashMap<>();
        filter1.put("filter", "!**.png");
        task.libraryjars(filter1, "android.jar");

        Map<String, String> filter2 = new HashMap<>();
        filter2.put("filter", "!**.txt");
        filter2.put("jarfilter", "!META-INF/**");
        task.libraryjars(filter2, "support-v4.jar");

        task.outjars("app-obfuscated.jar");

        // When: Getting all lists
        List inJars = task.getInJarFiles();
        List libraryJars = task.getLibraryJarFiles();
        List libraryFilters = task.getLibraryJarFilters();
        List outJars = task.getOutJarFiles();

        // Then: All should be tracked correctly
        assertEquals(1, inJars.size(), "Should have 1 input jar");
        assertEquals(2, libraryJars.size(), "Should have 2 library jars");
        assertEquals(2, libraryFilters.size(), "Should have 2 filters");
        assertEquals(1, outJars.size(), "Should have 1 output jar");

        assertSame(filter1, libraryFilters.get(0));
        assertSame(filter2, libraryFilters.get(1));
    }

    /**
     * Tests that libraryjars(Map, Object) handles duplicate filter Maps.
     */
    @Test
    public void testLibraryjarsWithMap_duplicateFilterMaps() throws Exception {
        // Given: Same filter Map used for multiple jars
        Map<String, String> sharedFilter = new HashMap<>();
        sharedFilter.put("filter", "!**.class");

        // When: Using same Map for multiple jars
        task.libraryjars(sharedFilter, "android.jar");
        task.libraryjars(sharedFilter, "rt.jar");
        task.libraryjars(sharedFilter, "jsse.jar");

        // Then: Same Map should be stored for all
        List filters = task.getLibraryJarFilters();
        assertEquals(3, filters.size(), "Should have 3 filter entries");
        assertSame(sharedFilter, filters.get(0));
        assertSame(sharedFilter, filters.get(1));
        assertSame(sharedFilter, filters.get(2));
    }

    /**
     * Tests that libraryjars(Map, Object) handles realistic Android scenario with filters.
     */
    @Test
    public void testLibraryjarsWithMap_realisticAndroidScenarioWithFilters() throws Exception {
        // Given: Android project with filtered library jars
        task.injars("build/intermediates/classes.jar");

        Map<String, String> androidFilter = new HashMap<>();
        androidFilter.put("filter", "!**.png,!**.xml");
        androidFilter.put("jarfilter", "!android/support/**");
        task.libraryjars(androidFilter, "${ANDROID_HOME}/platforms/android-28/android.jar");

        Map<String, String> supportFilter = new HashMap<>();
        supportFilter.put("aarfilter", "!**/LICENSE");
        task.libraryjars(supportFilter, "libs/support-library.aar");

        task.outjars("build/outputs/app-release.jar");

        // When: Getting the results
        List libraryJars = task.getLibraryJarFiles();
        List filters = task.getLibraryJarFilters();

        // Then: All should be tracked correctly
        assertEquals(2, libraryJars.size(), "Should have 2 library jars");
        assertEquals(2, filters.size(), "Should have 2 filters");

        Map storedFilter1 = (Map) filters.get(0);
        assertEquals("!**.png,!**.xml", storedFilter1.get("filter"));
        assertEquals("!android/support/**", storedFilter1.get("jarfilter"));

        Map storedFilter2 = (Map) filters.get(1);
        assertEquals("!**/LICENSE", storedFilter2.get("aarfilter"));
    }

    /**
     * Tests that libraryjars(Map, Object) handles mixed calls with and without filters.
     */
    @Test
    public void testLibraryjarsWithMap_mixedFilteredAndUnfiltered() throws Exception {
        // Given: Mix of filtered and unfiltered calls
        task.libraryjars("library1.jar");  // No filter (delegates to Map version with null)

        Map<String, String> filter = new HashMap<>();
        filter.put("filter", "!**.png");
        task.libraryjars(filter, "library2.jar");  // With filter

        task.libraryjars("library3.jar");  // No filter again

        // When: Getting the results
        List libraryJars = task.getLibraryJarFiles();
        List filters = task.getLibraryJarFilters();

        // Then: Filters should match expectations
        assertEquals(3, libraryJars.size(), "Should have 3 library jars");
        assertEquals(3, filters.size(), "Should have 3 filter entries");

        assertNull(filters.get(0), "First filter should be null");
        assertSame(filter, filters.get(1), "Second filter should be the Map");
        assertNull(filters.get(2), "Third filter should be null");
    }

    /**
     * Tests that libraryjars(Map, Object) handles paths with special characters.
     */
    @Test
    public void testLibraryjarsWithMap_pathsWithSpecialCharacters() throws Exception {
        // Given: Path with special characters and filter
        String specialPath = "android-28_v1.0@platform.jar";
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("filter", "!**.class");

        // When: Calling libraryjars(Map, Object)
        task.libraryjars(filterArgs, specialPath);

        // Then: Path should be stored correctly
        List jarFiles = task.getLibraryJarFiles();
        assertEquals(specialPath, jarFiles.get(0), "Special path should be stored correctly");
    }

    /**
     * Tests that libraryjars(Map, Object) handles null jar path.
     */
    @Test
    public void testLibraryjarsWithMap_nullJarPath() throws Exception {
        // Given: Null jar path and filter Map
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("filter", "!**.class");

        // When: Calling libraryjars(Map, Object) with null path
        task.libraryjars(filterArgs, null);

        // Then: Null should be stored
        List jarFiles = task.getLibraryJarFiles();
        List filters = task.getLibraryJarFilters();

        assertEquals(1, jarFiles.size(), "Should have 1 jar entry");
        assertNull(jarFiles.get(0), "Jar path should be null");
        assertSame(filterArgs, filters.get(0), "Filter should be stored");
    }

    /**
     * Tests that libraryjars(Map, Object) handles platform libraries with filters.
     */
    @Test
    public void testLibraryjarsWithMap_platformLibrariesWithFilters() throws Exception {
        // Given: Multiple platform libraries with different filters
        Map<String, String> rtFilter = new HashMap<>();
        rtFilter.put("filter", "!sun/**");
        task.libraryjars(rtFilter, "${JAVA_HOME}/lib/rt.jar");

        Map<String, String> jceFilter = new HashMap<>();
        jceFilter.put("filter", "!com/sun/**");
        task.libraryjars(jceFilter, "${JAVA_HOME}/lib/jce.jar");

        // When: Getting the results
        List libraryJars = task.getLibraryJarFiles();
        List filters = task.getLibraryJarFilters();

        // Then: All should be tracked correctly
        assertEquals(2, libraryJars.size(), "Should have 2 library jars");
        assertEquals(2, filters.size(), "Should have 2 filters");

        Map storedFilter1 = (Map) filters.get(0);
        assertEquals("!sun/**", storedFilter1.get("filter"));

        Map storedFilter2 = (Map) filters.get(1);
        assertEquals("!com/sun/**", storedFilter2.get("filter"));
    }

    /**
     * Tests that libraryjars(Map, Object) handles interleaved calls with both versions.
     */
    @Test
    public void testLibraryjarsWithMap_interleavedWithSimpleVersion() throws Exception {
        // Given: Interleaved calls to both libraryjars versions
        Map<String, String> filter1 = new HashMap<>();
        filter1.put("filter", "!**.png");

        task.libraryjars(filter1, "library1.jar");  // Map version
        task.libraryjars("library2.jar");  // Simple version

        Map<String, String> filter2 = new HashMap<>();
        filter2.put("filter", "!**.txt");
        task.libraryjars(filter2, "library3.jar");  // Map version

        // When: Getting all lists
        List libraryJars = task.getLibraryJarFiles();
        List filters = task.getLibraryJarFilters();

        // Then: All should be tracked correctly
        assertEquals(3, libraryJars.size(), "Should have 3 library jars");
        assertEquals(3, filters.size(), "Should have 3 filter entries");

        assertSame(filter1, filters.get(0), "First filter should be filter1");
        assertNull(filters.get(1), "Second filter should be null");
        assertSame(filter2, filters.get(2), "Third filter should be filter2");
    }

    /**
     * Tests that libraryjars(Map, Object) handles AAR files with filters.
     */
    @Test
    public void testLibraryjarsWithMap_aarFilesWithFilters() throws Exception {
        // Given: AAR file with filter
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("aarfilter", "!**/LICENSE,!**/NOTICE");

        // When: Calling libraryjars(Map, Object)
        task.libraryjars(filterArgs, "support-library.aar");

        // Then: Filter should be stored
        List filters = task.getLibraryJarFilters();
        Map storedFilter = (Map) filters.get(0);
        assertEquals("!**/LICENSE,!**/NOTICE", storedFilter.get("aarfilter"));
    }

    /**
     * Tests that libraryjars(Map, Object) handles module paths with filters.
     */
    @Test
    public void testLibraryjarsWithMap_modulePathsWithFilters() throws Exception {
        // Given: Java module path with filter
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("jmodfilter", "!classes/**");

        // When: Calling libraryjars(Map, Object)
        task.libraryjars(filterArgs, "java.base.jmod");

        // Then: Filter should be stored
        List filters = task.getLibraryJarFilters();
        Map storedFilter = (Map) filters.get(0);
        assertEquals("!classes/**", storedFilter.get("jmodfilter"));
    }

    /**
     * Tests that libraryjars(Map, Object) handles variable substitution with filters.
     */
    @Test
    public void testLibraryjarsWithMap_variableSubstitutionWithFilters() throws Exception {
        // Given: Path with variable substitution and filter
        String pathWithVar = "${JAVA_HOME}/jre/lib/rt.jar";
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("filter", "!java/awt/**");

        // When: Calling libraryjars(Map, Object)
        task.libraryjars(filterArgs, pathWithVar);

        // Then: Path and filter should be stored
        List jarFiles = task.getLibraryJarFiles();
        List filters = task.getLibraryJarFilters();

        assertEquals(pathWithVar, jarFiles.get(0), "Should store path with variable as-is");
        Map storedFilter = (Map) filters.get(0);
        assertEquals("!java/awt/**", storedFilter.get("filter"));
    }
}
