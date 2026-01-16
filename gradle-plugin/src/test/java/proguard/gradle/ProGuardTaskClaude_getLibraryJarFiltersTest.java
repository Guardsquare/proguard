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
 * Tests for {@link ProGuardTask#getLibraryJarFilters()}.
 *
 * This test class verifies that the getLibraryJarFilters method correctly
 * returns a List of filter Maps corresponding to the library jar files configured
 * via the libraryjars() method. The list has a 1-to-1 correspondence with the library
 * jar files list, where each entry is either a Map of filter arguments or null.
 */
public class ProGuardTaskClaude_getLibraryJarFiltersTest {

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
     * Tests that getLibraryJarFilters returns an empty list when no library jars have been configured.
     */
    @Test
    public void testGetLibraryJarFilters_noLibraryJars() {
        // When: No library jars are configured
        List result = task.getLibraryJarFilters();

        // Then: The list should be empty
        assertNotNull(result, "List should not be null");
        assertTrue(result.isEmpty(), "List should be empty when no library jars are configured");
    }

    /**
     * Tests that getLibraryJarFilters returns a list with a null entry when one library jar
     * is configured without filters.
     */
    @Test
    public void testGetLibraryJarFilters_singleLibraryJar_noFilters() throws Exception {
        // Given: A single library jar configured without filters
        task.libraryjars("android.jar");

        // When: Getting the library jar filters list
        List result = task.getLibraryJarFilters();

        // Then: The list should contain one null entry
        assertNotNull(result, "List should not be null");
        assertEquals(1, result.size(), "List should contain 1 entry");
        assertNull(result.get(0), "Entry should be null when no filters are specified");
    }

    /**
     * Tests that getLibraryJarFilters returns a list with a Map entry when one library jar
     * is configured with filters.
     */
    @Test
    public void testGetLibraryJarFilters_singleLibraryJar_withFilters() throws Exception {
        // Given: A single library jar configured with filters
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("jarfilter", "!**.class");
        task.libraryjars(filterArgs, "android.jar");

        // When: Getting the library jar filters list
        List result = task.getLibraryJarFilters();

        // Then: The list should contain the filter Map
        assertNotNull(result, "List should not be null");
        assertEquals(1, result.size(), "List should contain 1 entry");
        assertNotNull(result.get(0), "Entry should not be null when filters are specified");
        assertSame(filterArgs, result.get(0), "Entry should be the same Map object");
    }

    /**
     * Tests that getLibraryJarFilters maintains 1-to-1 correspondence with library jars.
     */
    @Test
    public void testGetLibraryJarFilters_correspondenceWithLibraryJars() throws Exception {
        // Given: Multiple library jars, some with filters and some without
        Map<String, String> filter1 = new HashMap<>();
        filter1.put("jarfilter", "!**.class");

        task.libraryjars(filter1, "android.jar");
        task.libraryjars("rt.jar");  // No filters

        Map<String, String> filter2 = new HashMap<>();
        filter2.put("jarfilter", "!META-INF/**");
        task.libraryjars(filter2, "jce.jar");

        // When: Getting both lists
        List jarFiles = task.getLibraryJarFiles();
        List jarFilters = task.getLibraryJarFilters();

        // Then: Both lists should have the same size with proper correspondence
        assertEquals(jarFiles.size(), jarFilters.size(),
                "Filter list should have same size as jar files list");
        assertEquals(3, jarFilters.size(), "Filter list should contain 3 entries");
        assertSame(filter1, jarFilters.get(0), "First filter should match first jar");
        assertNull(jarFilters.get(1), "Second filter should be null (no filters)");
        assertSame(filter2, jarFilters.get(2), "Third filter should match third jar");
    }

    /**
     * Tests that getLibraryJarFilters preserves the order of filters as they were added.
     */
    @Test
    public void testGetLibraryJarFilters_preservesOrder() throws Exception {
        // Given: Multiple library jars with filters added in a specific order
        Map<String, String> filter1 = new HashMap<>();
        filter1.put("jarfilter", "first");

        Map<String, String> filter2 = new HashMap<>();
        filter2.put("jarfilter", "second");

        Map<String, String> filter3 = new HashMap<>();
        filter3.put("jarfilter", "third");

        task.libraryjars(filter1, "jar1.jar");
        task.libraryjars(filter2, "jar2.jar");
        task.libraryjars(filter3, "jar3.jar");

        // When: Getting the library jar filters list
        List result = task.getLibraryJarFilters();

        // Then: The list should maintain the same order
        assertEquals(3, result.size(), "List should contain 3 entries");
        assertSame(filter1, result.get(0), "First entry should be filter1");
        assertSame(filter2, result.get(1), "Second entry should be filter2");
        assertSame(filter3, result.get(2), "Third entry should be filter3");
    }

    /**
     * Tests that getLibraryJarFilters can be called multiple times and returns the same list instance.
     */
    @Test
    public void testGetLibraryJarFilters_returnsSameListInstance() throws Exception {
        // Given: A library jar with filter configured
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("jarfilter", "!**.class");
        task.libraryjars(filterArgs, "android.jar");

        // When: Getting the library jar filters list multiple times
        List result1 = task.getLibraryJarFilters();
        List result2 = task.getLibraryJarFilters();

        // Then: Both calls should return the same list instance
        assertSame(result1, result2, "Multiple invocations should return the same List instance");
    }

    /**
     * Tests that modifications are reflected in subsequent calls (returns actual internal list).
     */
    @Test
    public void testGetLibraryJarFilters_returnsModifiableList() throws Exception {
        // Given: A library jar with filter configured
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("jarfilter", "!**.class");
        task.libraryjars(filterArgs, "android.jar");

        List result1 = task.getLibraryJarFilters();
        int initialSize = result1.size();

        // When: Adding another library jar
        task.libraryjars("rt.jar");

        // Then: The previously obtained list should reflect the change
        assertEquals(initialSize + 1, result1.size(),
                "Original list reference should reflect additions");
    }

    /**
     * Tests that getLibraryJarFilters handles various filter types.
     */
    @Test
    public void testGetLibraryJarFilters_variousFilterTypes() throws Exception {
        // Given: Library jars with different filter types
        Map<String, String> filter1 = new HashMap<>();
        filter1.put("filter", "!**.class");

        Map<String, String> filter2 = new HashMap<>();
        filter2.put("jarfilter", "!META-INF/**");

        Map<String, String> filter3 = new HashMap<>();
        filter3.put("aarfilter", "!res/**");

        task.libraryjars(filter1, "android.jar");
        task.libraryjars(filter2, "rt.jar");
        task.libraryjars(filter3, "support-v4.aar");

        // When: Getting the library jar filters list
        List result = task.getLibraryJarFilters();

        // Then: All filter types should be preserved
        assertEquals(3, result.size(), "List should contain 3 entries");

        Map entry1 = (Map) result.get(0);
        assertEquals("!**.class", entry1.get("filter"), "First filter should have 'filter' key");

        Map entry2 = (Map) result.get(1);
        assertEquals("!META-INF/**", entry2.get("jarfilter"), "Second filter should have 'jarfilter' key");

        Map entry3 = (Map) result.get(2);
        assertEquals("!res/**", entry3.get("aarfilter"), "Third filter should have 'aarfilter' key");
    }

    /**
     * Tests that getLibraryJarFilters handles filters with multiple keys.
     */
    @Test
    public void testGetLibraryJarFilters_multipleFilterKeys() throws Exception {
        // Given: A library jar with multiple filter keys
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("filter", "!**.class");
        filterArgs.put("jarfilter", "!META-INF/**");
        filterArgs.put("feature", "baseFeature");
        task.libraryjars(filterArgs, "android.jar");

        // When: Getting the library jar filters list
        List result = task.getLibraryJarFilters();

        // Then: The Map should contain all keys
        assertEquals(1, result.size(), "List should contain 1 entry");
        Map entry = (Map) result.get(0);
        assertNotNull(entry, "Entry should not be null");
        assertEquals(3, entry.size(), "Filter Map should contain 3 keys");
        assertEquals("!**.class", entry.get("filter"), "Filter key should be present");
        assertEquals("!META-INF/**", entry.get("jarfilter"), "Jarfilter key should be present");
        assertEquals("baseFeature", entry.get("feature"), "Feature key should be present");
    }

    /**
     * Tests that getLibraryJarFilters handles mixed scenario with some jars having filters and others not.
     */
    @Test
    public void testGetLibraryJarFilters_mixedFiltersAndNulls() throws Exception {
        // Given: Multiple library jars, alternating between filters and no filters
        Map<String, String> filter1 = new HashMap<>();
        filter1.put("jarfilter", "!**.class");

        task.libraryjars(filter1, "android.jar");
        task.libraryjars("rt.jar");  // No filters
        task.libraryjars("jce.jar");  // No filters

        Map<String, String> filter4 = new HashMap<>();
        filter4.put("jarfilter", "!**.properties");
        task.libraryjars(filter4, "jsse.jar");

        task.libraryjars("charsets.jar");  // No filters

        // When: Getting the library jar filters list
        List result = task.getLibraryJarFilters();

        // Then: The list should have correct mix of Maps and nulls
        assertEquals(5, result.size(), "List should contain 5 entries");
        assertNotNull(result.get(0), "First entry should have filter");
        assertNull(result.get(1), "Second entry should be null");
        assertNull(result.get(2), "Third entry should be null");
        assertNotNull(result.get(3), "Fourth entry should have filter");
        assertNull(result.get(4), "Fifth entry should be null");
    }

    /**
     * Tests that filter Maps are stored by reference, not copied.
     */
    @Test
    public void testGetLibraryJarFilters_storesMapsByReference() throws Exception {
        // Given: A library jar with a filter Map
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("jarfilter", "!**.class");
        task.libraryjars(filterArgs, "android.jar");

        // When: Getting the library jar filters list
        List result = task.getLibraryJarFilters();
        Map retrievedFilter = (Map) result.get(0);

        // Then: The retrieved Map should be the same instance
        assertSame(filterArgs, retrievedFilter, "Filter Map should be stored by reference");

        // And: Modifications to the original Map should affect the stored Map
        filterArgs.put("newKey", "newValue");
        assertEquals("newValue", retrievedFilter.get("newKey"),
                "Changes to original Map should be reflected in stored Map");
    }

    /**
     * Tests that library jar filters are separate from other jar type filters.
     */
    @Test
    public void testGetLibraryJarFilters_separateFromOtherFilterLists() throws Exception {
        // Given: Filters for input, output, and library jars
        Map<String, String> inputFilter = new HashMap<>();
        inputFilter.put("filter", "input-filter");

        Map<String, String> outputFilter = new HashMap<>();
        outputFilter.put("filter", "output-filter");

        Map<String, String> libraryFilter = new HashMap<>();
        libraryFilter.put("jarfilter", "library-filter");

        task.injars(inputFilter, "input.jar");
        task.outjars(outputFilter, "output.jar");
        task.libraryjars(libraryFilter, "android.jar");

        // When: Getting the different filter lists
        List inputFilters = task.getInJarFilters();
        List outputFilters = task.getOutJarFilters();
        List libraryFilters = task.getLibraryJarFilters();

        // Then: Each list should only contain its respective filters
        assertEquals(1, inputFilters.size(), "Input filters should have 1 entry");
        assertEquals(1, outputFilters.size(), "Output filters should have 1 entry");
        assertEquals(1, libraryFilters.size(), "Library filters should have 1 entry");

        assertSame(inputFilter, inputFilters.get(0), "Input filter should be in input filters list");
        assertSame(outputFilter, outputFilters.get(0), "Output filter should be in output filters list");
        assertSame(libraryFilter, libraryFilters.get(0), "Library filter should be in library filters list");
    }

    /**
     * Tests that empty filter Map is stored (not treated as null).
     */
    @Test
    public void testGetLibraryJarFilters_emptyFilterMap() throws Exception {
        // Given: A library jar with an empty filter Map
        Map<String, String> emptyFilterArgs = new HashMap<>();
        task.libraryjars(emptyFilterArgs, "android.jar");

        // When: Getting the library jar filters list
        List result = task.getLibraryJarFilters();

        // Then: The list should contain the empty Map (not null)
        assertEquals(1, result.size(), "List should contain 1 entry");
        assertNotNull(result.get(0), "Entry should not be null for empty Map");
        assertSame(emptyFilterArgs, result.get(0), "Entry should be the empty Map");
        assertTrue(((Map) result.get(0)).isEmpty(), "Map should be empty");
    }

    /**
     * Tests the relationship between getLibraryJarFiles() and getLibraryJarFilters() sizes.
     */
    @Test
    public void testGetLibraryJarFilters_sizeMatchesLibraryJarFiles() throws Exception {
        // Given: Various library jars with and without filters
        task.libraryjars("android.jar");

        Map<String, String> filter = new HashMap<>();
        filter.put("jarfilter", "!**.class");
        task.libraryjars(filter, "rt.jar");

        task.libraryjars("jce.jar");
        task.libraryjars("jsse.jar");

        // When: Getting both lists
        List jarFiles = task.getLibraryJarFiles();
        List jarFilters = task.getLibraryJarFilters();

        // Then: Sizes should always match
        assertEquals(jarFiles.size(), jarFilters.size(),
                "Filter list size should always match jar files list size");
        assertEquals(4, jarFilters.size(), "Both lists should have 4 entries");
    }

    /**
     * Tests that getLibraryJarFilters handles filters for non-jar file types.
     */
    @Test
    public void testGetLibraryJarFilters_nonJarFileTypes() throws Exception {
        // Given: Filters for various file types
        Map<String, String> aarFilter = new HashMap<>();
        aarFilter.put("aarfilter", "!res/**");

        Map<String, String> apkFilter = new HashMap<>();
        apkFilter.put("apkfilter", "!assets/**");

        Map<String, String> jmodFilter = new HashMap<>();
        jmodFilter.put("jmodfilter", "!classes/**");

        task.libraryjars(aarFilter, "support-v4.aar");
        task.libraryjars(apkFilter, "framework.apk");
        task.libraryjars(jmodFilter, "java.base.jmod");

        // When: Getting the library jar filters list
        List result = task.getLibraryJarFilters();

        // Then: All filters should be preserved
        assertEquals(3, result.size(), "List should contain 3 entries");
        assertSame(aarFilter, result.get(0), "First entry should be aar filter");
        assertSame(apkFilter, result.get(1), "Second entry should be apk filter");
        assertSame(jmodFilter, result.get(2), "Third entry should be jmod filter");
    }

    /**
     * Tests that getLibraryJarFilters handles common Android SDK library jar filters.
     */
    @Test
    public void testGetLibraryJarFilters_androidSdkFilters() throws Exception {
        // Given: Android SDK library jars with typical filters
        Map<String, String> androidFilter = new HashMap<>();
        androidFilter.put("jarfilter", "!META-INF/**.RSA,!META-INF/**.SF,!META-INF/**.MF");

        Map<String, String> supportFilter = new HashMap<>();
        supportFilter.put("aarfilter", "!res/**,!assets/**");

        task.libraryjars(androidFilter, "android.jar");
        task.libraryjars(supportFilter, "support-v4.aar");

        // When: Getting the library jar filters list
        List result = task.getLibraryJarFilters();

        // Then: Filters should be preserved with complex patterns
        assertEquals(2, result.size(), "List should contain 2 entries");
        Map filter1 = (Map) result.get(0);
        assertEquals("!META-INF/**.RSA,!META-INF/**.SF,!META-INF/**.MF",
                filter1.get("jarfilter"), "Android jar filter should be preserved");
        Map filter2 = (Map) result.get(1);
        assertEquals("!res/**,!assets/**", filter2.get("aarfilter"),
                "Support AAR filter should be preserved");
    }

    /**
     * Tests that getLibraryJarFilters handles filters with 'feature' key for multi-feature apps.
     */
    @Test
    public void testGetLibraryJarFilters_featureKey() throws Exception {
        // Given: Library jars with feature specifications
        Map<String, String> filter1 = new HashMap<>();
        filter1.put("feature", "baseFeature");
        filter1.put("jarfilter", "!**.class");

        Map<String, String> filter2 = new HashMap<>();
        filter2.put("feature", "dynamicFeature");

        task.libraryjars(filter1, "android.jar");
        task.libraryjars(filter2, "support-v4.aar");

        // When: Getting the library jar filters list
        List result = task.getLibraryJarFilters();

        // Then: Feature keys should be preserved
        assertEquals(2, result.size(), "List should contain 2 entries");
        Map entry1 = (Map) result.get(0);
        assertEquals("baseFeature", entry1.get("feature"), "First feature should be baseFeature");
        Map entry2 = (Map) result.get(1);
        assertEquals("dynamicFeature", entry2.get("feature"), "Second feature should be dynamicFeature");
    }

    /**
     * Tests a realistic Android scenario with multiple library jars and various filters.
     */
    @Test
    public void testGetLibraryJarFilters_realisticAndroidScenario() throws Exception {
        // Given: A realistic Android project setup
        Map<String, String> androidFilter = new HashMap<>();
        androidFilter.put("jarfilter", "!META-INF/**");

        task.libraryjars(androidFilter, "android.jar");
        task.libraryjars("support-v4.aar");  // No filter

        Map<String, String> appcompatFilter = new HashMap<>();
        appcompatFilter.put("aarfilter", "!res/**");
        task.libraryjars(appcompatFilter, "appcompat.aar");

        task.libraryjars("kotlin-stdlib.jar");  // No filter

        // When: Getting the library jar filters list
        List result = task.getLibraryJarFilters();

        // Then: Filters should be in correct positions
        assertEquals(4, result.size(), "List should contain 4 entries");
        assertNotNull(result.get(0), "First entry should have filter");
        assertNull(result.get(1), "Second entry should be null");
        assertNotNull(result.get(2), "Third entry should have filter");
        assertNull(result.get(3), "Fourth entry should be null");
    }

    /**
     * Tests a realistic Java scenario with JRE library jars.
     */
    @Test
    public void testGetLibraryJarFilters_realisticJavaScenario() throws Exception {
        // Given: A realistic Java project setup with JRE libraries
        Map<String, String> rtFilter = new HashMap<>();
        rtFilter.put("jarfilter", "!sun/**,!com/sun/**");

        task.libraryjars(rtFilter, "rt.jar");
        task.libraryjars("jce.jar");  // No filter
        task.libraryjars("jsse.jar");  // No filter

        // When: Getting the library jar filters list
        List result = task.getLibraryJarFilters();

        // Then: Filters should be in correct positions
        assertEquals(3, result.size(), "List should contain 3 entries");
        assertNotNull(result.get(0), "First entry should have filter");
        assertNull(result.get(1), "Second entry should be null");
        assertNull(result.get(2), "Third entry should be null");
    }

    /**
     * Tests that getLibraryJarFilters handles all supported filter key types.
     */
    @Test
    public void testGetLibraryJarFilters_allFilterKeyTypes() throws Exception {
        // Given: Library jars with all filter key types
        Map<String, String> filter1 = new HashMap<>();
        filter1.put("filter", "!**.txt");
        filter1.put("jarfilter", "!**.class");
        filter1.put("aarfilter", "!res/**");
        filter1.put("apkfilter", "!assets/**");
        filter1.put("warfilter", "!WEB-INF/**");
        filter1.put("earfilter", "!META-INF/**");
        filter1.put("jmodfilter", "!classes/**");
        filter1.put("zipfilter", "!temp/**");

        task.libraryjars(filter1, "library.jar");

        // When: Getting the library jar filters list
        List result = task.getLibraryJarFilters();

        // Then: All filter keys should be preserved
        assertEquals(1, result.size(), "List should contain 1 entry");
        Map entry = (Map) result.get(0);
        assertEquals(8, entry.size(), "Filter Map should contain 8 keys");
        assertEquals("!**.txt", entry.get("filter"), "filter key should be present");
        assertEquals("!**.class", entry.get("jarfilter"), "jarfilter key should be present");
        assertEquals("!res/**", entry.get("aarfilter"), "aarfilter key should be present");
        assertEquals("!assets/**", entry.get("apkfilter"), "apkfilter key should be present");
        assertEquals("!WEB-INF/**", entry.get("warfilter"), "warfilter key should be present");
        assertEquals("!META-INF/**", entry.get("earfilter"), "earfilter key should be present");
        assertEquals("!classes/**", entry.get("jmodfilter"), "jmodfilter key should be present");
        assertEquals("!temp/**", entry.get("zipfilter"), "zipfilter key should be present");
    }

    /**
     * Tests that getLibraryJarFilters works correctly when library jars are mixed with input jars.
     */
    @Test
    public void testGetLibraryJarFilters_mixedWithInputJars() throws Exception {
        // Given: A mix of input and library jars with filters
        Map<String, String> inputFilter = new HashMap<>();
        inputFilter.put("filter", "input");

        Map<String, String> libraryFilter1 = new HashMap<>();
        libraryFilter1.put("jarfilter", "library1");

        Map<String, String> libraryFilter2 = new HashMap<>();
        libraryFilter2.put("jarfilter", "library2");

        task.injars(inputFilter, "app.jar");
        task.libraryjars(libraryFilter1, "android.jar");
        task.injars("module.jar");
        task.libraryjars(libraryFilter2, "support-v4.aar");

        // When: Getting the library jar filters list
        List libraryFilters = task.getLibraryJarFilters();
        List inputFilters = task.getInJarFilters();

        // Then: Library filters should only contain library jar filters
        assertEquals(2, libraryFilters.size(), "Library filters should contain 2 entries");
        assertSame(libraryFilter1, libraryFilters.get(0), "First library filter should match");
        assertSame(libraryFilter2, libraryFilters.get(1), "Second library filter should match");

        // And: Input filters should be separate
        assertEquals(2, inputFilters.size(), "Input filters should contain 2 entries");
        assertSame(inputFilter, inputFilters.get(0), "Input filter should be separate");
    }
}
