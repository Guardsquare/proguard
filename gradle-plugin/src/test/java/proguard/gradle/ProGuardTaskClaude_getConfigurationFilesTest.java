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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link ProGuardTask#getConfigurationFiles()}.
 *
 * This test class verifies that the getConfigurationFiles method correctly
 * returns a List of configuration file objects configured via the configuration() method.
 * Unlike getConfigurationFileCollection(), this method returns the raw list of Objects
 * (String, File, etc.) before Gradle resolves them.
 *
 * The configuration() method has special behavior: it flattens Collections automatically,
 * so when a Collection is passed, its elements are added individually rather than adding
 * the Collection itself.
 */
public class ProGuardTaskClaude_getConfigurationFilesTest {

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
     * Tests that getConfigurationFiles returns an empty list when no configuration files have been configured.
     */
    @Test
    public void testGetConfigurationFiles_noConfigurationFiles() {
        // When: No configuration files are configured
        List result = task.getConfigurationFiles();

        // Then: The list should be empty
        assertNotNull(result, "List should not be null");
        assertTrue(result.isEmpty(), "List should be empty when no configuration files are configured");
    }

    /**
     * Tests that getConfigurationFiles returns a list containing a single entry when one configuration file
     * is configured with a String path.
     */
    @Test
    public void testGetConfigurationFiles_singleConfigurationFile_StringPath() throws Exception {
        // Given: A single configuration file configured with a String path
        String configPath = "proguard-rules.pro";
        task.configuration(configPath);

        // When: Getting the configuration files list
        List result = task.getConfigurationFiles();

        // Then: The list should contain the configured string
        assertNotNull(result, "List should not be null");
        assertEquals(1, result.size(), "List should contain 1 entry");
        assertEquals(configPath, result.get(0), "List entry should match the configured string path");
    }

    /**
     * Tests that getConfigurationFiles returns a list containing a single entry when one configuration file
     * is configured with a File object.
     */
    @Test
    public void testGetConfigurationFiles_singleConfigurationFile_FileObject() throws Exception {
        // Given: A single configuration file configured with a File object
        File configFile = new File(tempDir.toFile(), "proguard-rules.pro");
        task.configuration(configFile);

        // When: Getting the configuration files list
        List result = task.getConfigurationFiles();

        // Then: The list should contain the configured File object
        assertNotNull(result, "List should not be null");
        assertEquals(1, result.size(), "List should contain 1 entry");
        assertSame(configFile, result.get(0), "List entry should be the same File object");
    }

    /**
     * Tests that getConfigurationFiles returns a list containing multiple entries when multiple
     * configuration files are configured.
     */
    @Test
    public void testGetConfigurationFiles_multipleConfigurationFiles() throws Exception {
        // Given: Multiple configuration files configured
        File config1 = new File(tempDir.toFile(), "proguard-rules.pro");
        String config2 = "proguard-debug.pro";
        File config3 = new File(tempDir.toFile(), "proguard-release.pro");

        task.configuration(config1);
        task.configuration(config2);
        task.configuration(config3);

        // When: Getting the configuration files list
        List result = task.getConfigurationFiles();

        // Then: The list should contain all configured entries in order
        assertNotNull(result, "List should not be null");
        assertEquals(3, result.size(), "List should contain 3 entries");
        assertSame(config1, result.get(0), "First entry should be config1");
        assertEquals(config2, result.get(1), "Second entry should be config2");
        assertSame(config3, result.get(2), "Third entry should be config3");
    }

    /**
     * Tests that getConfigurationFiles preserves the order of configuration files as they were added.
     */
    @Test
    public void testGetConfigurationFiles_preservesOrder() throws Exception {
        // Given: Configuration files added in a specific order
        String config1 = "first.pro";
        String config2 = "second.pro";
        String config3 = "third.pro";

        task.configuration(config1);
        task.configuration(config2);
        task.configuration(config3);

        // When: Getting the configuration files list
        List result = task.getConfigurationFiles();

        // Then: The list should maintain the same order
        assertNotNull(result, "List should not be null");
        assertEquals(3, result.size(), "List should contain 3 entries");
        assertEquals(config1, result.get(0), "First entry should be first.pro");
        assertEquals(config2, result.get(1), "Second entry should be second.pro");
        assertEquals(config3, result.get(2), "Third entry should be third.pro");
    }

    /**
     * Tests that getConfigurationFiles returns the raw, unresolved objects (not File objects).
     */
    @Test
    public void testGetConfigurationFiles_returnsRawObjects() throws Exception {
        // Given: Configuration files configured with various object types
        String stringPath = "path/to/proguard.pro";
        File fileObject = new File("another.pro");

        task.configuration(stringPath);
        task.configuration(fileObject);

        // When: Getting the configuration files list
        List result = task.getConfigurationFiles();

        // Then: The list should contain the raw objects, not resolved File objects
        assertNotNull(result, "List should not be null");
        assertEquals(2, result.size(), "List should contain 2 entries");
        assertTrue(result.get(0) instanceof String, "First entry should be a String");
        assertTrue(result.get(1) instanceof File, "Second entry should be a File");
        assertEquals(stringPath, result.get(0), "First entry should be the original string");
        assertSame(fileObject, result.get(1), "Second entry should be the original File object");
    }

    /**
     * Tests that getConfigurationFiles can be called multiple times and returns the same list instance.
     */
    @Test
    public void testGetConfigurationFiles_returnsSameListInstance() throws Exception {
        // Given: A configuration file configured
        task.configuration("proguard-rules.pro");

        // When: Getting the configuration files list multiple times
        List result1 = task.getConfigurationFiles();
        List result2 = task.getConfigurationFiles();

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
    public void testGetConfigurationFiles_returnsModifiableList() throws Exception {
        // Given: A configuration file configured
        task.configuration("proguard-rules.pro");
        List result1 = task.getConfigurationFiles();
        int initialSize = result1.size();

        // When: Adding another configuration file via configuration()
        task.configuration("another.pro");

        // Then: The previously obtained list should reflect the change
        List result2 = task.getConfigurationFiles();
        assertEquals(initialSize + 1, result1.size(), "Original list reference should reflect additions");
        assertEquals(result2.size(), result1.size(), "Both references should have the same size");
    }

    /**
     * Tests that getConfigurationFiles handles relative path strings.
     */
    @Test
    public void testGetConfigurationFiles_relativePathString() throws Exception {
        // Given: A configuration file configured with a relative path string
        String relativePath = "config/proguard-rules.pro";
        task.configuration(relativePath);

        // When: Getting the configuration files list
        List result = task.getConfigurationFiles();

        // Then: The list should contain the unresolved relative path string
        assertNotNull(result, "List should not be null");
        assertEquals(1, result.size(), "List should contain 1 entry");
        assertEquals(relativePath, result.get(0), "List should contain the original relative path string");
    }

    /**
     * Tests that getConfigurationFiles handles absolute path strings.
     */
    @Test
    public void testGetConfigurationFiles_absolutePathString() throws Exception {
        // Given: A configuration file configured with an absolute path string
        String absolutePath = new File(tempDir.toFile(), "proguard-rules.pro").getAbsolutePath();
        task.configuration(absolutePath);

        // When: Getting the configuration files list
        List result = task.getConfigurationFiles();

        // Then: The list should contain the absolute path string
        assertNotNull(result, "List should not be null");
        assertEquals(1, result.size(), "List should contain 1 entry");
        assertEquals(absolutePath, result.get(0), "List should contain the absolute path string");
    }

    /**
     * Tests that configuration files are kept in a separate list from jar files.
     */
    @Test
    public void testGetConfigurationFiles_separateFromJarFiles() throws Exception {
        // Given: Input jars and configuration files configured
        String inputJar = "input.jar";
        String configFile = "proguard-rules.pro";

        task.injars(inputJar);
        task.configuration(configFile);
        task.libraryjars("android.jar");

        // When: Getting the configuration files list
        List configResult = task.getConfigurationFiles();
        List inputResult = task.getInJarFiles();
        List libraryResult = task.getLibraryJarFiles();

        // Then: Configuration files should only contain the config file
        assertNotNull(configResult, "Config list should not be null");
        assertEquals(1, configResult.size(), "Config list should contain only 1 entry");
        assertEquals(configFile, configResult.get(0), "Config list should contain only the config file");

        assertFalse(configResult.contains(inputJar), "Config list should not contain input jar");
        assertFalse(configResult.contains("android.jar"), "Config list should not contain library jar");
    }

    /**
     * Tests that getConfigurationFiles handles Collection flattening.
     * When a Collection is passed to configuration(), its elements should be added individually.
     */
    @Test
    public void testGetConfigurationFiles_flattensList() throws Exception {
        // Given: A List of configuration files
        List<String> configFiles = new ArrayList<>();
        configFiles.add("config1.pro");
        configFiles.add("config2.pro");
        configFiles.add("config3.pro");

        task.configuration(configFiles);

        // When: Getting the configuration files list
        List result = task.getConfigurationFiles();

        // Then: The list should contain the individual elements, not the List itself
        assertNotNull(result, "List should not be null");
        assertEquals(3, result.size(), "List should contain 3 entries (flattened from Collection)");
        assertEquals("config1.pro", result.get(0), "First entry should be config1.pro");
        assertEquals("config2.pro", result.get(1), "Second entry should be config2.pro");
        assertEquals("config3.pro", result.get(2), "Third entry should be config3.pro");
        assertFalse(result.contains(configFiles), "List should not contain the original Collection");
    }

    /**
     * Tests that getConfigurationFiles handles multiple Collection additions.
     */
    @Test
    public void testGetConfigurationFiles_multipleCollections() throws Exception {
        // Given: Multiple Collections of configuration files
        List<String> collection1 = Arrays.asList("config1.pro", "config2.pro");
        List<String> collection2 = Arrays.asList("config3.pro", "config4.pro");

        task.configuration(collection1);
        task.configuration(collection2);

        // When: Getting the configuration files list
        List result = task.getConfigurationFiles();

        // Then: All elements from both collections should be present
        assertNotNull(result, "List should not be null");
        assertEquals(4, result.size(), "List should contain 4 entries");
        assertEquals("config1.pro", result.get(0), "First entry should be config1.pro");
        assertEquals("config2.pro", result.get(1), "Second entry should be config2.pro");
        assertEquals("config3.pro", result.get(2), "Third entry should be config3.pro");
        assertEquals("config4.pro", result.get(3), "Fourth entry should be config4.pro");
    }

    /**
     * Tests that getConfigurationFiles handles mixed individual files and Collections.
     */
    @Test
    public void testGetConfigurationFiles_mixedIndividualAndCollections() throws Exception {
        // Given: Mix of individual files and Collections
        String config1 = "config1.pro";
        List<String> collection = Arrays.asList("config2.pro", "config3.pro");
        String config4 = "config4.pro";

        task.configuration(config1);
        task.configuration(collection);
        task.configuration(config4);

        // When: Getting the configuration files list
        List result = task.getConfigurationFiles();

        // Then: All should be flattened in order
        assertNotNull(result, "List should not be null");
        assertEquals(4, result.size(), "List should contain 4 entries");
        assertEquals(config1, result.get(0), "First entry should be config1.pro");
        assertEquals("config2.pro", result.get(1), "Second entry should be config2.pro");
        assertEquals("config3.pro", result.get(2), "Third entry should be config3.pro");
        assertEquals(config4, result.get(3), "Fourth entry should be config4.pro");
    }

    /**
     * Tests that getConfigurationFiles handles empty Collections.
     */
    @Test
    public void testGetConfigurationFiles_emptyCollection() throws Exception {
        // Given: An empty Collection
        List<String> emptyCollection = new ArrayList<>();
        task.configuration(emptyCollection);

        // When: Getting the configuration files list
        List result = task.getConfigurationFiles();

        // Then: The list should still be empty
        assertNotNull(result, "List should not be null");
        assertTrue(result.isEmpty(), "List should be empty when empty Collection is added");
    }

    /**
     * Tests that getConfigurationFiles handles Collections containing File objects.
     */
    @Test
    public void testGetConfigurationFiles_collectionOfFileObjects() throws Exception {
        // Given: A Collection of File objects
        File config1 = new File(tempDir.toFile(), "config1.pro");
        File config2 = new File(tempDir.toFile(), "config2.pro");
        List<File> configFiles = Arrays.asList(config1, config2);

        task.configuration(configFiles);

        // When: Getting the configuration files list
        List result = task.getConfigurationFiles();

        // Then: The list should contain the individual File objects
        assertNotNull(result, "List should not be null");
        assertEquals(2, result.size(), "List should contain 2 entries");
        assertSame(config1, result.get(0), "First entry should be config1 File object");
        assertSame(config2, result.get(1), "Second entry should be config2 File object");
    }

    /**
     * Tests that getConfigurationFiles handles Collections with mixed types.
     */
    @Test
    public void testGetConfigurationFiles_collectionOfMixedTypes() throws Exception {
        // Given: A Collection with mixed String and File objects
        File fileConfig = new File(tempDir.toFile(), "config1.pro");
        String stringConfig = "config2.pro";
        List<Object> mixedCollection = Arrays.asList(fileConfig, stringConfig);

        task.configuration(mixedCollection);

        // When: Getting the configuration files list
        List result = task.getConfigurationFiles();

        // Then: Both types should be preserved
        assertNotNull(result, "List should not be null");
        assertEquals(2, result.size(), "List should contain 2 entries");
        assertSame(fileConfig, result.get(0), "First entry should be the File object");
        assertEquals(stringConfig, result.get(1), "Second entry should be the String");
    }

    /**
     * Tests that getConfigurationFiles handles common ProGuard configuration file names.
     */
    @Test
    public void testGetConfigurationFiles_commonProGuardFileNames() throws Exception {
        // Given: Common ProGuard configuration file names
        String mainConfig = "proguard-rules.pro";
        String androidConfig = "proguard-android.txt";
        String androidOptimizeConfig = "proguard-android-optimize.txt";
        String customConfig = "proguard.cfg";

        task.configuration(mainConfig);
        task.configuration(androidConfig);
        task.configuration(androidOptimizeConfig);
        task.configuration(customConfig);

        // When: Getting the configuration files list
        List result = task.getConfigurationFiles();

        // Then: All common file names should be preserved
        assertNotNull(result, "List should not be null");
        assertEquals(4, result.size(), "List should contain 4 entries");
        assertEquals(mainConfig, result.get(0), "First entry should be proguard-rules.pro");
        assertEquals(androidConfig, result.get(1), "Second entry should be proguard-android.txt");
        assertEquals(androidOptimizeConfig, result.get(2), "Third entry should be proguard-android-optimize.txt");
        assertEquals(customConfig, result.get(3), "Fourth entry should be proguard.cfg");
    }

    /**
     * Tests that getConfigurationFiles handles internal configuration file paths (from JAR).
     */
    @Test
    public void testGetConfigurationFiles_internalConfigPaths() throws Exception {
        // Given: Internal configuration file paths (packaged in JAR)
        String internalConfig1 = "/lib/proguard-android.pro";
        String internalConfig2 = "/lib/proguard-android-debug.pro";

        task.configuration(internalConfig1);
        task.configuration(internalConfig2);

        // When: Getting the configuration files list
        List result = task.getConfigurationFiles();

        // Then: Internal paths should be preserved
        assertNotNull(result, "List should not be null");
        assertEquals(2, result.size(), "List should contain 2 entries");
        assertEquals(internalConfig1, result.get(0), "First entry should be /lib/proguard-android.pro");
        assertEquals(internalConfig2, result.get(1), "Second entry should be /lib/proguard-android-debug.pro");
    }

    /**
     * Tests a realistic Android scenario with multiple configuration files.
     */
    @Test
    public void testGetConfigurationFiles_realisticAndroidScenario() throws Exception {
        // Given: A realistic Android project setup
        String androidOptimize = "/lib/proguard-android-optimize.txt";
        String projectRules = "proguard-rules.pro";
        String flavorRules = "proguard-flavor-specific.pro";
        String libraryRules = "libs/library-rules.pro";

        task.configuration(androidOptimize);
        task.configuration(projectRules);
        task.configuration(flavorRules);
        task.configuration(libraryRules);

        // When: Getting the configuration files list
        List result = task.getConfigurationFiles();

        // Then: All configuration files should be present in order
        assertNotNull(result, "List should not be null");
        assertEquals(4, result.size(), "List should contain 4 entries");
        assertEquals(androidOptimize, result.get(0), "First should be Android optimize config");
        assertEquals(projectRules, result.get(1), "Second should be project rules");
        assertEquals(flavorRules, result.get(2), "Third should be flavor rules");
        assertEquals(libraryRules, result.get(3), "Fourth should be library rules");
    }

    /**
     * Tests a realistic Java scenario with multiple configuration files.
     */
    @Test
    public void testGetConfigurationFiles_realisticJavaScenario() throws Exception {
        // Given: A realistic Java project setup
        File baseConfig = new File(tempDir.toFile(), "proguard-base.pro");
        File libraryConfig = new File(tempDir.toFile(), "proguard-library.pro");
        String optimizationConfig = "proguard-optimize.pro";

        task.configuration(baseConfig);
        task.configuration(libraryConfig);
        task.configuration(optimizationConfig);

        // When: Getting the configuration files list
        List result = task.getConfigurationFiles();

        // Then: All configuration files should be present
        assertNotNull(result, "List should not be null");
        assertEquals(3, result.size(), "List should contain 3 entries");
        assertSame(baseConfig, result.get(0), "First should be base config");
        assertSame(libraryConfig, result.get(1), "Second should be library config");
        assertEquals(optimizationConfig, result.get(2), "Third should be optimization config");
    }

    /**
     * Tests that getConfigurationFiles handles various file extensions.
     */
    @Test
    public void testGetConfigurationFiles_variousExtensions() throws Exception {
        // Given: Configuration files with various extensions
        String proFile = "config.pro";
        String txtFile = "config.txt";
        String cfgFile = "config.cfg";
        String noExtension = "config";

        task.configuration(proFile);
        task.configuration(txtFile);
        task.configuration(cfgFile);
        task.configuration(noExtension);

        // When: Getting the configuration files list
        List result = task.getConfigurationFiles();

        // Then: All file extensions should be preserved
        assertNotNull(result, "List should not be null");
        assertEquals(4, result.size(), "List should contain 4 entries");
        assertEquals(proFile, result.get(0), "First should be .pro file");
        assertEquals(txtFile, result.get(1), "Second should be .txt file");
        assertEquals(cfgFile, result.get(2), "Third should be .cfg file");
        assertEquals(noExtension, result.get(3), "Fourth should be file without extension");
    }

    /**
     * Tests that getConfigurationFiles handles nested Collections (Collection containing Collections).
     */
    @Test
    public void testGetConfigurationFiles_nestedCollections() throws Exception {
        // Given: A Collection containing other Collections
        List<String> innerList1 = Arrays.asList("config1.pro", "config2.pro");
        List<String> innerList2 = Arrays.asList("config3.pro", "config4.pro");
        List<List<String>> nestedCollection = Arrays.asList(innerList1, innerList2);

        task.configuration(nestedCollection);

        // When: Getting the configuration files list
        List result = task.getConfigurationFiles();

        // Then: Only the first level should be flattened (Collections themselves are added)
        assertNotNull(result, "List should not be null");
        assertEquals(2, result.size(), "List should contain 2 entries (inner lists)");
        assertSame(innerList1, result.get(0), "First entry should be innerList1");
        assertSame(innerList2, result.get(1), "Second entry should be innerList2");
    }

    /**
     * Tests that getConfigurationFiles handles configuration files from Gradle build variants.
     */
    @Test
    public void testGetConfigurationFiles_buildVariants() throws Exception {
        // Given: Configuration files from different build variants
        List<String> debugConfigs = Arrays.asList("proguard-debug.pro", "proguard-common.pro");
        List<String> releaseConfigs = Arrays.asList("proguard-release.pro", "proguard-common.pro");

        task.configuration(debugConfigs);

        // When: Getting the configuration files list
        List result = task.getConfigurationFiles();

        // Then: Debug configs should be flattened
        assertNotNull(result, "List should not be null");
        assertEquals(2, result.size(), "List should contain 2 entries");
        assertEquals("proguard-debug.pro", result.get(0), "First should be debug config");
        assertEquals("proguard-common.pro", result.get(1), "Second should be common config");
    }

    /**
     * Tests that getConfigurationFiles handles empty string.
     */
    @Test
    public void testGetConfigurationFiles_emptyString() throws Exception {
        // Given: An empty string configured as configuration file
        String emptyPath = "";
        task.configuration(emptyPath);

        // When: Getting the configuration files list
        List result = task.getConfigurationFiles();

        // Then: The list should contain the empty string
        assertNotNull(result, "List should not be null");
        assertEquals(1, result.size(), "List should contain 1 entry");
        assertEquals(emptyPath, result.get(0), "List should contain the empty string");
    }

    /**
     * Tests that getConfigurationFiles handles duplicate configuration file paths.
     */
    @Test
    public void testGetConfigurationFiles_duplicatePaths() throws Exception {
        // Given: The same configuration file added multiple times
        String configPath = "proguard-rules.pro";
        task.configuration(configPath);
        task.configuration(configPath);
        task.configuration(configPath);

        // When: Getting the configuration files list
        List result = task.getConfigurationFiles();

        // Then: All entries should be present (duplicates allowed)
        assertNotNull(result, "List should not be null");
        assertEquals(3, result.size(), "List should contain 3 entries (duplicates allowed)");
        assertEquals(configPath, result.get(0), "First entry should be the config path");
        assertEquals(configPath, result.get(1), "Second entry should be the same config path");
        assertEquals(configPath, result.get(2), "Third entry should be the same config path");
    }
}
