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
 * Tests for {@link ProGuardTask#getLibraryJarFiles()}.
 *
 * This test class verifies that the getLibraryJarFiles method correctly
 * returns a List of library jar file objects configured via the libraryjars() method.
 * Unlike getLibraryJarFileCollection(), this method returns the raw list of Objects
 * (String, File, etc.) before Gradle resolves them.
 *
 * Library jars are used to provide class definitions that are referenced by the program
 * jars but should not be included in the output.
 */
public class ProGuardTaskClaude_getLibraryJarFilesTest {

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
     * Tests that getLibraryJarFiles returns an empty list when no library jars have been configured.
     */
    @Test
    public void testGetLibraryJarFiles_noLibraryJars() {
        // When: No library jars are configured
        List result = task.getLibraryJarFiles();

        // Then: The list should be empty
        assertNotNull(result, "List should not be null");
        assertTrue(result.isEmpty(), "List should be empty when no library jars are configured");
    }

    /**
     * Tests that getLibraryJarFiles returns a list containing a single entry when one library jar
     * is configured with a String path.
     */
    @Test
    public void testGetLibraryJarFiles_singleLibraryJar_StringPath() throws Exception {
        // Given: A single library jar configured with a String path
        String libraryJarPath = "android.jar";
        task.libraryjars(libraryJarPath);

        // When: Getting the library jar files list
        List result = task.getLibraryJarFiles();

        // Then: The list should contain the configured string
        assertNotNull(result, "List should not be null");
        assertEquals(1, result.size(), "List should contain 1 entry");
        assertEquals(libraryJarPath, result.get(0), "List entry should match the configured string path");
    }

    /**
     * Tests that getLibraryJarFiles returns a list containing a single entry when one library jar
     * is configured with a File object.
     */
    @Test
    public void testGetLibraryJarFiles_singleLibraryJar_FileObject() throws Exception {
        // Given: A single library jar configured with a File object
        File libraryJar = new File(tempDir.toFile(), "rt.jar");
        task.libraryjars(libraryJar);

        // When: Getting the library jar files list
        List result = task.getLibraryJarFiles();

        // Then: The list should contain the configured File object
        assertNotNull(result, "List should not be null");
        assertEquals(1, result.size(), "List should contain 1 entry");
        assertSame(libraryJar, result.get(0), "List entry should be the same File object");
    }

    /**
     * Tests that getLibraryJarFiles returns a list containing multiple entries when multiple
     * library jars are configured.
     */
    @Test
    public void testGetLibraryJarFiles_multipleLibraryJars() throws Exception {
        // Given: Multiple library jars configured
        File libraryJar1 = new File(tempDir.toFile(), "android.jar");
        String libraryJar2 = "rt.jar";
        File libraryJar3 = new File(tempDir.toFile(), "jce.jar");

        task.libraryjars(libraryJar1);
        task.libraryjars(libraryJar2);
        task.libraryjars(libraryJar3);

        // When: Getting the library jar files list
        List result = task.getLibraryJarFiles();

        // Then: The list should contain all configured entries in order
        assertNotNull(result, "List should not be null");
        assertEquals(3, result.size(), "List should contain 3 entries");
        assertSame(libraryJar1, result.get(0), "First entry should be libraryJar1");
        assertEquals(libraryJar2, result.get(1), "Second entry should be libraryJar2");
        assertSame(libraryJar3, result.get(2), "Third entry should be libraryJar3");
    }

    /**
     * Tests that getLibraryJarFiles preserves the order of library jars as they were added.
     */
    @Test
    public void testGetLibraryJarFiles_preservesOrder() throws Exception {
        // Given: Library jars added in a specific order
        String jar1 = "first.jar";
        String jar2 = "second.jar";
        String jar3 = "third.jar";

        task.libraryjars(jar1);
        task.libraryjars(jar2);
        task.libraryjars(jar3);

        // When: Getting the library jar files list
        List result = task.getLibraryJarFiles();

        // Then: The list should maintain the same order
        assertNotNull(result, "List should not be null");
        assertEquals(3, result.size(), "List should contain 3 entries");
        assertEquals(jar1, result.get(0), "First entry should be first.jar");
        assertEquals(jar2, result.get(1), "Second entry should be second.jar");
        assertEquals(jar3, result.get(2), "Third entry should be third.jar");
    }

    /**
     * Tests that getLibraryJarFiles works correctly when library jars are configured with filter arguments.
     * Note: Filters are stored separately in getLibraryJarFilters(), not in getLibraryJarFiles().
     */
    @Test
    public void testGetLibraryJarFiles_withFilterArgs() throws Exception {
        // Given: Library jar configured with filter arguments
        File libraryJar = new File(tempDir.toFile(), "android.jar");
        java.util.Map<String, String> filterArgs = new java.util.HashMap<>();
        filterArgs.put("jarfilter", "!**.class");
        task.libraryjars(filterArgs, libraryJar);

        // When: Getting the library jar files list
        List result = task.getLibraryJarFiles();

        // Then: The list should contain the file (filters are stored separately)
        assertNotNull(result, "List should not be null");
        assertEquals(1, result.size(), "List should contain 1 entry");
        assertSame(libraryJar, result.get(0), "List entry should be the File object");
    }

    /**
     * Tests that getLibraryJarFiles returns the raw, unresolved objects (not File objects).
     */
    @Test
    public void testGetLibraryJarFiles_returnsRawObjects() throws Exception {
        // Given: Library jars configured with various object types
        String stringPath = "path/to/android.jar";
        File fileObject = new File("rt.jar");

        task.libraryjars(stringPath);
        task.libraryjars(fileObject);

        // When: Getting the library jar files list
        List result = task.getLibraryJarFiles();

        // Then: The list should contain the raw objects, not resolved File objects
        assertNotNull(result, "List should not be null");
        assertEquals(2, result.size(), "List should contain 2 entries");
        assertTrue(result.get(0) instanceof String, "First entry should be a String");
        assertTrue(result.get(1) instanceof File, "Second entry should be a File");
        assertEquals(stringPath, result.get(0), "First entry should be the original string");
        assertSame(fileObject, result.get(1), "Second entry should be the original File object");
    }

    /**
     * Tests that getLibraryJarFiles can be called multiple times and returns the same list instance.
     */
    @Test
    public void testGetLibraryJarFiles_returnsSameListInstance() throws Exception {
        // Given: A library jar configured
        task.libraryjars("android.jar");

        // When: Getting the library jar files list multiple times
        List result1 = task.getLibraryJarFiles();
        List result2 = task.getLibraryJarFiles();

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
    public void testGetLibraryJarFiles_returnsModifiableList() throws Exception {
        // Given: A library jar configured
        task.libraryjars("android.jar");
        List result1 = task.getLibraryJarFiles();
        int initialSize = result1.size();

        // When: Adding another library jar via libraryjars()
        task.libraryjars("rt.jar");

        // Then: The previously obtained list should reflect the change
        List result2 = task.getLibraryJarFiles();
        assertEquals(initialSize + 1, result1.size(), "Original list reference should reflect additions");
        assertEquals(result2.size(), result1.size(), "Both references should have the same size");
    }

    /**
     * Tests that getLibraryJarFiles handles relative path strings.
     */
    @Test
    public void testGetLibraryJarFiles_relativePathString() throws Exception {
        // Given: A library jar configured with a relative path string
        String relativePath = "libs/android.jar";
        task.libraryjars(relativePath);

        // When: Getting the library jar files list
        List result = task.getLibraryJarFiles();

        // Then: The list should contain the unresolved relative path string
        assertNotNull(result, "List should not be null");
        assertEquals(1, result.size(), "List should contain 1 entry");
        assertEquals(relativePath, result.get(0), "List should contain the original relative path string");
    }

    /**
     * Tests that getLibraryJarFiles handles absolute path strings.
     */
    @Test
    public void testGetLibraryJarFiles_absolutePathString() throws Exception {
        // Given: A library jar configured with an absolute path string
        String absolutePath = new File(tempDir.toFile(), "android.jar").getAbsolutePath();
        task.libraryjars(absolutePath);

        // When: Getting the library jar files list
        List result = task.getLibraryJarFiles();

        // Then: The list should contain the absolute path string
        assertNotNull(result, "List should not be null");
        assertEquals(1, result.size(), "List should contain 1 entry");
        assertEquals(absolutePath, result.get(0), "List should contain the absolute path string");
    }

    /**
     * Tests that library jars and other jar types are kept in separate lists.
     */
    @Test
    public void testGetLibraryJarFiles_separateFromOtherJarTypes() throws Exception {
        // Given: Input, output, and library jars configured
        String inputJar = "input.jar";
        String outputJar = "output.jar";
        String libraryJar = "android.jar";

        task.injars(inputJar);
        task.outjars(outputJar);
        task.libraryjars(libraryJar);

        // When: Getting the library jar files list
        List inputResult = task.getInJarFiles();
        List outputResult = task.getOutJarFiles();
        List libraryResult = task.getLibraryJarFiles();

        // Then: Each list should only contain its respective jar type
        assertNotNull(libraryResult, "Library list should not be null");
        assertEquals(1, libraryResult.size(), "Library list should contain only 1 entry");
        assertEquals(libraryJar, libraryResult.get(0), "Library list should contain only the library jar");

        assertFalse(libraryResult.contains(inputJar), "Library list should not contain input jar");
        assertFalse(libraryResult.contains(outputJar), "Library list should not contain output jar");
    }

    /**
     * Tests that getLibraryJarFiles handles directory paths.
     */
    @Test
    public void testGetLibraryJarFiles_directoryPath() throws Exception {
        // Given: A directory configured as library
        File libraryDir = new File(tempDir.toFile(), "android-sdk");
        libraryDir.mkdirs();
        task.libraryjars(libraryDir);

        // When: Getting the library jar files list
        List result = task.getLibraryJarFiles();

        // Then: The list should contain the directory File object
        assertNotNull(result, "List should not be null");
        assertEquals(1, result.size(), "List should contain 1 entry");
        assertSame(libraryDir, result.get(0), "List should contain the directory File object");
    }

    /**
     * Tests that getLibraryJarFiles can handle various file types beyond just .jar files.
     */
    @Test
    public void testGetLibraryJarFiles_variousFileTypes() throws Exception {
        // Given: Various file types configured as library
        String jarFile = "android.jar";
        String aarFile = "support-v4.aar";
        String apkFile = "framework.apk";
        String jmodFile = "java.base.jmod";

        task.libraryjars(jarFile);
        task.libraryjars(aarFile);
        task.libraryjars(apkFile);
        task.libraryjars(jmodFile);

        // When: Getting the library jar files list
        List result = task.getLibraryJarFiles();

        // Then: The list should contain all file types
        assertNotNull(result, "List should not be null");
        assertEquals(4, result.size(), "List should contain 4 entries");
        assertEquals(jarFile, result.get(0), "First entry should be jar file");
        assertEquals(aarFile, result.get(1), "Second entry should be aar file");
        assertEquals(apkFile, result.get(2), "Third entry should be apk file");
        assertEquals(jmodFile, result.get(3), "Fourth entry should be jmod file");
    }

    /**
     * Tests that getLibraryJarFiles handles common Android SDK library jar paths.
     */
    @Test
    public void testGetLibraryJarFiles_androidSdkPath() throws Exception {
        // Given: Android SDK library jar path
        String androidJar = "${ANDROID_HOME}/platforms/android-28/android.jar";
        task.libraryjars(androidJar);

        // When: Getting the library jar files list
        List result = task.getLibraryJarFiles();

        // Then: The list should contain the path with environment variable
        assertNotNull(result, "List should not be null");
        assertEquals(1, result.size(), "List should contain 1 entry");
        assertEquals(androidJar, result.get(0), "List should contain the Android SDK path");
    }

    /**
     * Tests that getLibraryJarFiles handles Java runtime library jar paths.
     */
    @Test
    public void testGetLibraryJarFiles_javaRuntimePath() throws Exception {
        // Given: Java runtime library jars
        String rtJar = "${JAVA_HOME}/lib/rt.jar";
        String jceJar = "${JAVA_HOME}/lib/jce.jar";
        task.libraryjars(rtJar);
        task.libraryjars(jceJar);

        // When: Getting the library jar files list
        List result = task.getLibraryJarFiles();

        // Then: The list should contain both runtime jars
        assertNotNull(result, "List should not be null");
        assertEquals(2, result.size(), "List should contain 2 entries");
        assertEquals(rtJar, result.get(0), "First entry should be rt.jar");
        assertEquals(jceJar, result.get(1), "Second entry should be jce.jar");
    }

    /**
     * Tests that getLibraryJarFiles works correctly with multiple filter configurations.
     */
    @Test
    public void testGetLibraryJarFiles_multipleWithDifferentFilters() throws Exception {
        // Given: Multiple library jars with different filters
        File jar1 = new File(tempDir.toFile(), "lib1.jar");
        File jar2 = new File(tempDir.toFile(), "lib2.jar");

        java.util.Map<String, String> filter1 = new java.util.HashMap<>();
        filter1.put("jarfilter", "!**.class");
        task.libraryjars(filter1, jar1);

        java.util.Map<String, String> filter2 = new java.util.HashMap<>();
        filter2.put("jarfilter", "**.xml");
        task.libraryjars(filter2, jar2);

        // When: Getting the library jar files list
        List result = task.getLibraryJarFiles();

        // Then: The list should contain both jars (filters stored separately)
        assertNotNull(result, "List should not be null");
        assertEquals(2, result.size(), "List should contain 2 entries");
        assertSame(jar1, result.get(0), "First entry should be jar1");
        assertSame(jar2, result.get(1), "Second entry should be jar2");
    }

    /**
     * Tests a realistic scenario with multiple library jars representing a typical Android project.
     */
    @Test
    public void testGetLibraryJarFiles_realisticAndroidScenario() throws Exception {
        // Given: A realistic Android project setup
        String androidJar = "platforms/android-30/android.jar";
        String supportV4 = "support-v4-28.0.0.aar";
        String appcompat = "appcompat-1.2.0.aar";
        String kotlinStdlib = "kotlin-stdlib-1.4.32.jar";

        task.libraryjars(androidJar);
        task.libraryjars(supportV4);
        task.libraryjars(appcompat);
        task.libraryjars(kotlinStdlib);

        // When: Getting the library jar files list
        List result = task.getLibraryJarFiles();

        // Then: All library dependencies should be present
        assertNotNull(result, "List should not be null");
        assertEquals(4, result.size(), "List should contain 4 entries");
        assertEquals(androidJar, result.get(0), "First entry should be android.jar");
        assertEquals(supportV4, result.get(1), "Second entry should be support-v4");
        assertEquals(appcompat, result.get(2), "Third entry should be appcompat");
        assertEquals(kotlinStdlib, result.get(3), "Fourth entry should be kotlin-stdlib");
    }

    /**
     * Tests a realistic scenario with multiple library jars representing a typical Java project.
     */
    @Test
    public void testGetLibraryJarFiles_realisticJavaScenario() throws Exception {
        // Given: A realistic Java project setup
        String rtJar = "jre/lib/rt.jar";
        String jceJar = "jre/lib/jce.jar";
        String jsseJar = "jre/lib/jsse.jar";

        task.libraryjars(rtJar);
        task.libraryjars(jceJar);
        task.libraryjars(jsseJar);

        // When: Getting the library jar files list
        List result = task.getLibraryJarFiles();

        // Then: All runtime libraries should be present
        assertNotNull(result, "List should not be null");
        assertEquals(3, result.size(), "List should contain 3 entries");
        assertEquals(rtJar, result.get(0), "First entry should be rt.jar");
        assertEquals(jceJar, result.get(1), "Second entry should be jce.jar");
        assertEquals(jsseJar, result.get(2), "Third entry should be jsse.jar");
    }

    /**
     * Tests that empty string can be added as library jar path.
     */
    @Test
    public void testGetLibraryJarFiles_emptyString() throws Exception {
        // Given: An empty string configured as library jar
        String emptyPath = "";
        task.libraryjars(emptyPath);

        // When: Getting the library jar files list
        List result = task.getLibraryJarFiles();

        // Then: The list should contain the empty string
        assertNotNull(result, "List should not be null");
        assertEquals(1, result.size(), "List should contain 1 entry");
        assertEquals(emptyPath, result.get(0), "List should contain the empty string");
    }

    /**
     * Tests that getLibraryJarFiles works with wildcard paths.
     */
    @Test
    public void testGetLibraryJarFiles_wildcardPath() throws Exception {
        // Given: Library jar path with wildcards
        String wildcardPath = "libs/*.jar";
        task.libraryjars(wildcardPath);

        // When: Getting the library jar files list
        List result = task.getLibraryJarFiles();

        // Then: The list should contain the wildcard path as-is
        assertNotNull(result, "List should not be null");
        assertEquals(1, result.size(), "List should contain 1 entry");
        assertEquals(wildcardPath, result.get(0), "List should contain the wildcard path");
    }

    /**
     * Tests that getLibraryJarFiles handles a mix of input and library jars correctly.
     */
    @Test
    public void testGetLibraryJarFiles_mixedWithInputJars() throws Exception {
        // Given: A mix of input and library jars
        task.injars("app.jar");
        task.libraryjars("android.jar");
        task.injars("module1.jar");
        task.libraryjars("support-v4.aar");
        task.injars("module2.jar");
        task.libraryjars("rt.jar");

        // When: Getting the library jar files list
        List libraryResult = task.getLibraryJarFiles();
        List inputResult = task.getInJarFiles();

        // Then: Library list should only contain library jars
        assertNotNull(libraryResult, "Library list should not be null");
        assertEquals(3, libraryResult.size(), "Library list should contain 3 entries");
        assertEquals("android.jar", libraryResult.get(0), "First library should be android.jar");
        assertEquals("support-v4.aar", libraryResult.get(1), "Second library should be support-v4.aar");
        assertEquals("rt.jar", libraryResult.get(2), "Third library should be rt.jar");

        // And: Input list should only contain input jars
        assertEquals(3, inputResult.size(), "Input list should contain 3 entries");
    }
}
