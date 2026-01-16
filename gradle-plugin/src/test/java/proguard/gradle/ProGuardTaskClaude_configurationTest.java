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
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link ProGuardTask#configuration(Object)}.
 *
 * This test class verifies that the configuration() method correctly adds
 * configuration file objects to the internal list. The method has special behavior:
 * it automatically flattens Collections, so when a Collection is passed, its elements
 * are added individually rather than adding the Collection object itself.
 *
 * The method signature is: configuration(Object configurationFiles)
 * - If the argument is a Collection, all its elements are added to the list
 * - Otherwise, the single object is added to the list
 *
 * This method throws ParseException and IOException, though in practice it just
 * collects arguments for lazy resolution.
 */
public class ProGuardTaskClaude_configurationTest {

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
     * Tests that configuration() adds a single String path to the configuration files list.
     */
    @Test
    public void testConfiguration_singleString() throws Exception {
        // Given: A String path
        String configPath = "proguard-rules.pro";

        // When: Calling configuration()
        task.configuration(configPath);

        // Then: The configuration files list should contain the string
        List result = task.getConfigurationFiles();
        assertNotNull(result, "Configuration files list should not be null");
        assertEquals(1, result.size(), "Should have 1 entry");
        assertEquals(configPath, result.get(0), "Entry should be the string path");
    }

    /**
     * Tests that configuration() adds a single File object to the configuration files list.
     */
    @Test
    public void testConfiguration_singleFile() throws Exception {
        // Given: A File object
        File configFile = new File(tempDir.toFile(), "proguard-rules.pro");

        // When: Calling configuration()
        task.configuration(configFile);

        // Then: The configuration files list should contain the File object
        List result = task.getConfigurationFiles();
        assertNotNull(result, "Configuration files list should not be null");
        assertEquals(1, result.size(), "Should have 1 entry");
        assertSame(configFile, result.get(0), "Entry should be the same File object");
    }

    /**
     * Tests that configuration() can be called multiple times and accumulates entries.
     */
    @Test
    public void testConfiguration_multipleCalls() throws Exception {
        // Given: Multiple configuration files
        String config1 = "config1.pro";
        String config2 = "config2.pro";
        String config3 = "config3.pro";

        // When: Calling configuration() multiple times
        task.configuration(config1);
        task.configuration(config2);
        task.configuration(config3);

        // Then: All entries should be accumulated
        List result = task.getConfigurationFiles();
        assertEquals(3, result.size(), "Should have 3 entries");
        assertEquals(config1, result.get(0), "First entry should be config1");
        assertEquals(config2, result.get(1), "Second entry should be config2");
        assertEquals(config3, result.get(2), "Third entry should be config3");
    }

    /**
     * Tests that configuration() flattens a Collection (List).
     * The Collection itself should not be added; instead, its elements should be added.
     */
    @Test
    public void testConfiguration_flattensList() throws Exception {
        // Given: A List of configuration files
        List<String> configFiles = new ArrayList<>();
        configFiles.add("config1.pro");
        configFiles.add("config2.pro");
        configFiles.add("config3.pro");

        // When: Calling configuration() with the List
        task.configuration(configFiles);

        // Then: The elements should be added individually, not the List itself
        List result = task.getConfigurationFiles();
        assertEquals(3, result.size(), "Should have 3 entries (flattened from List)");
        assertEquals("config1.pro", result.get(0), "First entry should be config1.pro");
        assertEquals("config2.pro", result.get(1), "Second entry should be config2.pro");
        assertEquals("config3.pro", result.get(2), "Third entry should be config3.pro");
        assertFalse(result.contains(configFiles), "Should not contain the List object itself");
    }

    /**
     * Tests that configuration() flattens a Collection (Set).
     */
    @Test
    public void testConfiguration_flattensSet() throws Exception {
        // Given: A Set of configuration files
        Set<String> configFiles = new LinkedHashSet<>();
        configFiles.add("config1.pro");
        configFiles.add("config2.pro");

        // When: Calling configuration() with the Set
        task.configuration(configFiles);

        // Then: The elements should be added individually
        List result = task.getConfigurationFiles();
        assertEquals(2, result.size(), "Should have 2 entries (flattened from Set)");
        assertTrue(result.contains("config1.pro"), "Should contain config1.pro");
        assertTrue(result.contains("config2.pro"), "Should contain config2.pro");
        assertFalse(result.contains(configFiles), "Should not contain the Set object itself");
    }

    /**
     * Tests that configuration() flattens an empty Collection.
     */
    @Test
    public void testConfiguration_emptyCollection() throws Exception {
        // Given: An empty List
        List<String> emptyList = new ArrayList<>();

        // When: Calling configuration() with the empty List
        task.configuration(emptyList);

        // Then: No entries should be added
        List result = task.getConfigurationFiles();
        assertTrue(result.isEmpty(), "Should have 0 entries when empty Collection is passed");
    }

    /**
     * Tests that configuration() handles a Collection with a single element.
     */
    @Test
    public void testConfiguration_singleElementCollection() throws Exception {
        // Given: A List with a single element
        List<String> singleElementList = Collections.singletonList("config.pro");

        // When: Calling configuration() with the List
        task.configuration(singleElementList);

        // Then: The single element should be added
        List result = task.getConfigurationFiles();
        assertEquals(1, result.size(), "Should have 1 entry");
        assertEquals("config.pro", result.get(0), "Entry should be config.pro");
    }

    /**
     * Tests that configuration() handles a Collection containing File objects.
     */
    @Test
    public void testConfiguration_collectionOfFiles() throws Exception {
        // Given: A List of File objects
        File file1 = new File(tempDir.toFile(), "config1.pro");
        File file2 = new File(tempDir.toFile(), "config2.pro");
        List<File> fileList = Arrays.asList(file1, file2);

        // When: Calling configuration() with the List
        task.configuration(fileList);

        // Then: The File objects should be added individually
        List result = task.getConfigurationFiles();
        assertEquals(2, result.size(), "Should have 2 entries");
        assertSame(file1, result.get(0), "First entry should be file1");
        assertSame(file2, result.get(1), "Second entry should be file2");
    }

    /**
     * Tests that configuration() handles a Collection with mixed types.
     */
    @Test
    public void testConfiguration_collectionOfMixedTypes() throws Exception {
        // Given: A List with mixed String and File objects
        String stringConfig = "config1.pro";
        File fileConfig = new File(tempDir.toFile(), "config2.pro");
        List<Object> mixedList = Arrays.asList(stringConfig, fileConfig);

        // When: Calling configuration() with the List
        task.configuration(mixedList);

        // Then: Both types should be added
        List result = task.getConfigurationFiles();
        assertEquals(2, result.size(), "Should have 2 entries");
        assertEquals(stringConfig, result.get(0), "First entry should be the String");
        assertSame(fileConfig, result.get(1), "Second entry should be the File");
    }

    /**
     * Tests that configuration() preserves order when adding multiple items.
     */
    @Test
    public void testConfiguration_preservesOrder() throws Exception {
        // Given: Configuration files added in specific order
        String first = "first.pro";
        List<String> middle = Arrays.asList("second.pro", "third.pro");
        String last = "fourth.pro";

        // When: Calling configuration() in order
        task.configuration(first);
        task.configuration(middle);
        task.configuration(last);

        // Then: Order should be preserved
        List result = task.getConfigurationFiles();
        assertEquals(4, result.size(), "Should have 4 entries");
        assertEquals("first.pro", result.get(0), "First should be first.pro");
        assertEquals("second.pro", result.get(1), "Second should be second.pro");
        assertEquals("third.pro", result.get(2), "Third should be third.pro");
        assertEquals("fourth.pro", result.get(3), "Fourth should be fourth.pro");
    }

    /**
     * Tests that configuration() handles null element (should be added as null).
     */
    @Test
    public void testConfiguration_nullElement() throws Exception {
        // Given: A null value
        Object nullValue = null;

        // When: Calling configuration() with null
        task.configuration(nullValue);

        // Then: Null should be added to the list
        List result = task.getConfigurationFiles();
        assertEquals(1, result.size(), "Should have 1 entry");
        assertNull(result.get(0), "Entry should be null");
    }

    /**
     * Tests that configuration() handles a Collection containing null elements.
     */
    @Test
    public void testConfiguration_collectionWithNulls() throws Exception {
        // Given: A List containing nulls
        List<String> listWithNulls = new ArrayList<>();
        listWithNulls.add("config1.pro");
        listWithNulls.add(null);
        listWithNulls.add("config2.pro");

        // When: Calling configuration() with the List
        task.configuration(listWithNulls);

        // Then: All elements including nulls should be added
        List result = task.getConfigurationFiles();
        assertEquals(3, result.size(), "Should have 3 entries");
        assertEquals("config1.pro", result.get(0), "First should be config1.pro");
        assertNull(result.get(1), "Second should be null");
        assertEquals("config2.pro", result.get(2), "Third should be config2.pro");
    }

    /**
     * Tests that configuration() handles relative path strings.
     */
    @Test
    public void testConfiguration_relativePathString() throws Exception {
        // Given: A relative path string
        String relativePath = "config/proguard-rules.pro";

        // When: Calling configuration()
        task.configuration(relativePath);

        // Then: The relative path should be stored as-is
        List result = task.getConfigurationFiles();
        assertEquals(1, result.size(), "Should have 1 entry");
        assertEquals(relativePath, result.get(0), "Should store the relative path string");
    }

    /**
     * Tests that configuration() handles absolute path strings.
     */
    @Test
    public void testConfiguration_absolutePathString() throws Exception {
        // Given: An absolute path string
        String absolutePath = new File(tempDir.toFile(), "proguard-rules.pro").getAbsolutePath();

        // When: Calling configuration()
        task.configuration(absolutePath);

        // Then: The absolute path should be stored as-is
        List result = task.getConfigurationFiles();
        assertEquals(1, result.size(), "Should have 1 entry");
        assertEquals(absolutePath, result.get(0), "Should store the absolute path string");
    }

    /**
     * Tests that configuration() handles empty string.
     */
    @Test
    public void testConfiguration_emptyString() throws Exception {
        // Given: An empty string
        String emptyString = "";

        // When: Calling configuration()
        task.configuration(emptyString);

        // Then: The empty string should be added
        List result = task.getConfigurationFiles();
        assertEquals(1, result.size(), "Should have 1 entry");
        assertEquals("", result.get(0), "Should store the empty string");
    }

    /**
     * Tests that configuration() handles Collection containing empty strings.
     */
    @Test
    public void testConfiguration_collectionWithEmptyStrings() throws Exception {
        // Given: A List containing empty strings
        List<String> listWithEmpties = Arrays.asList("", "config.pro", "");

        // When: Calling configuration()
        task.configuration(listWithEmpties);

        // Then: All elements should be added
        List result = task.getConfigurationFiles();
        assertEquals(3, result.size(), "Should have 3 entries");
        assertEquals("", result.get(0), "First should be empty string");
        assertEquals("config.pro", result.get(1), "Second should be config.pro");
        assertEquals("", result.get(2), "Third should be empty string");
    }

    /**
     * Tests that configuration() allows duplicate entries.
     */
    @Test
    public void testConfiguration_duplicates() throws Exception {
        // Given: Same configuration file added multiple times
        String configPath = "proguard-rules.pro";

        // When: Calling configuration() multiple times with same path
        task.configuration(configPath);
        task.configuration(configPath);
        task.configuration(configPath);

        // Then: All duplicates should be added
        List result = task.getConfigurationFiles();
        assertEquals(3, result.size(), "Should have 3 entries (duplicates allowed)");
        assertEquals(configPath, result.get(0), "All entries should be the same");
        assertEquals(configPath, result.get(1), "All entries should be the same");
        assertEquals(configPath, result.get(2), "All entries should be the same");
    }

    /**
     * Tests that configuration() handles Collection containing duplicates.
     */
    @Test
    public void testConfiguration_collectionWithDuplicates() throws Exception {
        // Given: A List with duplicate entries
        List<String> listWithDuplicates = Arrays.asList("config.pro", "config.pro", "other.pro");

        // When: Calling configuration()
        task.configuration(listWithDuplicates);

        // Then: All elements including duplicates should be added
        List result = task.getConfigurationFiles();
        assertEquals(3, result.size(), "Should have 3 entries");
        assertEquals("config.pro", result.get(0), "First should be config.pro");
        assertEquals("config.pro", result.get(1), "Second should be config.pro (duplicate)");
        assertEquals("other.pro", result.get(2), "Third should be other.pro");
    }

    /**
     * Tests that configuration() handles common ProGuard configuration file names.
     */
    @Test
    public void testConfiguration_commonProGuardFileNames() throws Exception {
        // Given: Common ProGuard file names
        List<String> commonFiles = Arrays.asList(
            "proguard-rules.pro",
            "proguard-android.txt",
            "proguard-android-optimize.txt",
            "proguard.cfg"
        );

        // When: Calling configuration()
        task.configuration(commonFiles);

        // Then: All should be added
        List result = task.getConfigurationFiles();
        assertEquals(4, result.size(), "Should have 4 entries");
        assertEquals("proguard-rules.pro", result.get(0));
        assertEquals("proguard-android.txt", result.get(1));
        assertEquals("proguard-android-optimize.txt", result.get(2));
        assertEquals("proguard.cfg", result.get(3));
    }

    /**
     * Tests that configuration() handles internal configuration paths from JAR.
     */
    @Test
    public void testConfiguration_internalConfigPaths() throws Exception {
        // Given: Internal configuration paths
        String internalPath = "/lib/proguard-android.pro";

        // When: Calling configuration()
        task.configuration(internalPath);

        // Then: The internal path should be stored
        List result = task.getConfigurationFiles();
        assertEquals(1, result.size(), "Should have 1 entry");
        assertEquals(internalPath, result.get(0), "Should store the internal path");
    }

    /**
     * Tests that configuration() handles Collection of internal configuration paths.
     */
    @Test
    public void testConfiguration_collectionOfInternalPaths() throws Exception {
        // Given: A List of internal configuration paths
        List<String> internalPaths = Arrays.asList(
            "/lib/proguard-android.pro",
            "/lib/proguard-android-debug.pro"
        );

        // When: Calling configuration()
        task.configuration(internalPaths);

        // Then: All internal paths should be added
        List result = task.getConfigurationFiles();
        assertEquals(2, result.size(), "Should have 2 entries");
        assertEquals("/lib/proguard-android.pro", result.get(0));
        assertEquals("/lib/proguard-android-debug.pro", result.get(1));
    }

    /**
     * Tests that configuration() works in a realistic Android scenario.
     */
    @Test
    public void testConfiguration_realisticAndroidScenario() throws Exception {
        // Given: A realistic Android project configuration sequence
        task.configuration("/lib/proguard-android-optimize.txt");
        task.configuration("proguard-rules.pro");

        List<String> flavorConfigs = Arrays.asList(
            "proguard-flavor-free.pro",
            "proguard-flavor-paid.pro"
        );
        task.configuration(flavorConfigs);

        task.configuration("libs/library-rules.pro");

        // When: Getting the result
        List result = task.getConfigurationFiles();

        // Then: All should be added in order
        assertEquals(5, result.size(), "Should have 5 entries");
        assertEquals("/lib/proguard-android-optimize.txt", result.get(0));
        assertEquals("proguard-rules.pro", result.get(1));
        assertEquals("proguard-flavor-free.pro", result.get(2));
        assertEquals("proguard-flavor-paid.pro", result.get(3));
        assertEquals("libs/library-rules.pro", result.get(4));
    }

    /**
     * Tests that configuration() works in a realistic Java scenario.
     */
    @Test
    public void testConfiguration_realisticJavaScenario() throws Exception {
        // Given: A realistic Java project configuration
        File baseConfig = new File(tempDir.toFile(), "proguard-base.pro");
        File libConfig = new File(tempDir.toFile(), "proguard-lib.pro");

        task.configuration(baseConfig);
        task.configuration(libConfig);
        task.configuration("proguard-optimize.pro");

        // When: Getting the result
        List result = task.getConfigurationFiles();

        // Then: All should be added
        assertEquals(3, result.size(), "Should have 3 entries");
        assertSame(baseConfig, result.get(0));
        assertSame(libConfig, result.get(1));
        assertEquals("proguard-optimize.pro", result.get(2));
    }

    /**
     * Tests that configuration() handles various file extensions.
     */
    @Test
    public void testConfiguration_variousExtensions() throws Exception {
        // Given: Files with various extensions
        List<String> variousExtensions = Arrays.asList(
            "config.pro",
            "config.txt",
            "config.cfg",
            "config"  // no extension
        );

        // When: Calling configuration()
        task.configuration(variousExtensions);

        // Then: All should be preserved
        List result = task.getConfigurationFiles();
        assertEquals(4, result.size(), "Should have 4 entries");
        assertEquals("config.pro", result.get(0));
        assertEquals("config.txt", result.get(1));
        assertEquals("config.cfg", result.get(2));
        assertEquals("config", result.get(3));
    }

    /**
     * Tests that configuration() handles nested Collections.
     * Only the first level should be flattened.
     */
    @Test
    public void testConfiguration_nestedCollections() throws Exception {
        // Given: A Collection containing other Collections
        List<String> innerList1 = Arrays.asList("config1.pro", "config2.pro");
        List<String> innerList2 = Arrays.asList("config3.pro", "config4.pro");
        List<List<String>> outerList = Arrays.asList(innerList1, innerList2);

        // When: Calling configuration()
        task.configuration(outerList);

        // Then: Only first level should be flattened
        List result = task.getConfigurationFiles();
        assertEquals(2, result.size(), "Should have 2 entries (inner Lists)");
        assertSame(innerList1, result.get(0), "First entry should be innerList1");
        assertSame(innerList2, result.get(1), "Second entry should be innerList2");
    }

    /**
     * Tests that configuration() handles ArrayList specifically.
     */
    @Test
    public void testConfiguration_arrayList() throws Exception {
        // Given: An ArrayList
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("config1.pro");
        arrayList.add("config2.pro");

        // When: Calling configuration()
        task.configuration(arrayList);

        // Then: Elements should be flattened
        List result = task.getConfigurationFiles();
        assertEquals(2, result.size(), "Should have 2 entries");
        assertEquals("config1.pro", result.get(0));
        assertEquals("config2.pro", result.get(1));
    }

    /**
     * Tests that configuration() handles LinkedList specifically.
     */
    @Test
    public void testConfiguration_linkedList() throws Exception {
        // Given: A LinkedList
        LinkedList<String> linkedList = new LinkedList<>();
        linkedList.add("config1.pro");
        linkedList.add("config2.pro");

        // When: Calling configuration()
        task.configuration(linkedList);

        // Then: Elements should be flattened
        List result = task.getConfigurationFiles();
        assertEquals(2, result.size(), "Should have 2 entries");
        assertEquals("config1.pro", result.get(0));
        assertEquals("config2.pro", result.get(1));
    }

    /**
     * Tests that configuration() handles HashSet specifically.
     */
    @Test
    public void testConfiguration_hashSet() throws Exception {
        // Given: A HashSet (order may vary)
        Set<String> hashSet = new HashSet<>();
        hashSet.add("config1.pro");
        hashSet.add("config2.pro");

        // When: Calling configuration()
        task.configuration(hashSet);

        // Then: Elements should be flattened
        List result = task.getConfigurationFiles();
        assertEquals(2, result.size(), "Should have 2 entries");
        assertTrue(result.contains("config1.pro"), "Should contain config1.pro");
        assertTrue(result.contains("config2.pro"), "Should contain config2.pro");
    }

    /**
     * Tests that configuration() handles Vector (legacy Collection).
     */
    @Test
    public void testConfiguration_vector() throws Exception {
        // Given: A Vector
        Vector<String> vector = new Vector<>();
        vector.add("config1.pro");
        vector.add("config2.pro");

        // When: Calling configuration()
        task.configuration(vector);

        // Then: Elements should be flattened
        List result = task.getConfigurationFiles();
        assertEquals(2, result.size(), "Should have 2 entries");
        assertEquals("config1.pro", result.get(0));
        assertEquals("config2.pro", result.get(1));
    }

    /**
     * Tests that configuration() handles Arrays.asList result.
     */
    @Test
    public void testConfiguration_arraysAsList() throws Exception {
        // Given: Result from Arrays.asList
        List<String> arrayList = Arrays.asList("config1.pro", "config2.pro", "config3.pro");

        // When: Calling configuration()
        task.configuration(arrayList);

        // Then: Elements should be flattened
        List result = task.getConfigurationFiles();
        assertEquals(3, result.size(), "Should have 3 entries");
        assertEquals("config1.pro", result.get(0));
        assertEquals("config2.pro", result.get(1));
        assertEquals("config3.pro", result.get(2));
    }

    /**
     * Tests that configuration() doesn't interfere with other jar configurations.
     */
    @Test
    public void testConfiguration_independentFromJarConfigs() throws Exception {
        // Given: Mix of configuration files and jar files
        task.configuration("proguard-rules.pro");
        task.injars("input.jar");
        task.configuration("proguard-debug.pro");
        task.libraryjars("android.jar");

        // When: Getting configuration files
        List configFiles = task.getConfigurationFiles();
        List inJars = task.getInJarFiles();
        List libraryJars = task.getLibraryJarFiles();

        // Then: Each list should contain only its respective items
        assertEquals(2, configFiles.size(), "Should have 2 config files");
        assertEquals("proguard-rules.pro", configFiles.get(0));
        assertEquals("proguard-debug.pro", configFiles.get(1));

        assertEquals(1, inJars.size(), "Should have 1 input jar");
        assertEquals("input.jar", inJars.get(0));

        assertEquals(1, libraryJars.size(), "Should have 1 library jar");
        assertEquals("android.jar", libraryJars.get(0));
    }

    /**
     * Tests that configuration() handles Collection returned from Gradle FileCollection.
     */
    @Test
    public void testConfiguration_gradleFileCollectionLike() throws Exception {
        // Given: A Collection of File objects (like what Gradle might provide)
        File file1 = new File(tempDir.toFile(), "config1.pro");
        File file2 = new File(tempDir.toFile(), "config2.pro");
        File file3 = new File(tempDir.toFile(), "config3.pro");

        Set<File> fileSet = new LinkedHashSet<>();
        fileSet.add(file1);
        fileSet.add(file2);
        fileSet.add(file3);

        // When: Calling configuration()
        task.configuration(fileSet);

        // Then: All File objects should be added
        List result = task.getConfigurationFiles();
        assertEquals(3, result.size(), "Should have 3 entries");
        assertTrue(result.contains(file1), "Should contain file1");
        assertTrue(result.contains(file2), "Should contain file2");
        assertTrue(result.contains(file3), "Should contain file3");
    }

    /**
     * Tests that configuration() preserves object identity when adding single objects.
     */
    @Test
    public void testConfiguration_preservesObjectIdentity() throws Exception {
        // Given: A specific File object
        File configFile = new File(tempDir.toFile(), "config.pro");

        // When: Calling configuration()
        task.configuration(configFile);

        // Then: The exact same object should be stored
        List result = task.getConfigurationFiles();
        assertSame(configFile, result.get(0), "Should store the exact same File object");
    }

    /**
     * Tests that configuration() handles very large Collections efficiently.
     */
    @Test
    public void testConfiguration_largeCollection() throws Exception {
        // Given: A large List of configuration files
        List<String> largeList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            largeList.add("config" + i + ".pro");
        }

        // When: Calling configuration()
        task.configuration(largeList);

        // Then: All elements should be added
        List result = task.getConfigurationFiles();
        assertEquals(1000, result.size(), "Should have 1000 entries");
        assertEquals("config0.pro", result.get(0), "First should be config0.pro");
        assertEquals("config999.pro", result.get(999), "Last should be config999.pro");
    }

    /**
     * Tests that configuration() works correctly after multiple mixed operations.
     */
    @Test
    public void testConfiguration_complexMixedOperations() throws Exception {
        // Given: A complex sequence of operations
        task.configuration("first.pro");

        List<String> batch1 = Arrays.asList("second.pro", "third.pro");
        task.configuration(batch1);

        task.configuration("fourth.pro");
        task.configuration("fifth.pro");

        List<String> batch2 = Collections.singletonList("sixth.pro");
        task.configuration(batch2);

        task.configuration("seventh.pro");

        // When: Getting the result
        List result = task.getConfigurationFiles();

        // Then: All should be in correct order
        assertEquals(7, result.size(), "Should have 7 entries");
        assertEquals("first.pro", result.get(0));
        assertEquals("second.pro", result.get(1));
        assertEquals("third.pro", result.get(2));
        assertEquals("fourth.pro", result.get(3));
        assertEquals("fifth.pro", result.get(4));
        assertEquals("sixth.pro", result.get(5));
        assertEquals("seventh.pro", result.get(6));
    }
}
