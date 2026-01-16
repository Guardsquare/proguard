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
 * Tests for {@link ProGuardTask#getInJarFilters()}.
 *
 * This test class verifies that the getInJarFilters method correctly
 * returns a List of filter Maps corresponding to the input jar files configured
 * via the injars() method. The list has a 1-to-1 correspondence with the input
 * jar files list, where each entry is either a Map of filter arguments or null.
 */
public class ProGuardTaskClaude_getInJarFiltersTest {

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
     * Tests that getInJarFilters returns an empty list when no input jars have been configured.
     */
    @Test
    public void testGetInJarFilters_noInputJars() {
        // When: No input jars are configured
        List result = task.getInJarFilters();

        // Then: The list should be empty
        assertNotNull(result, "List should not be null");
        assertTrue(result.isEmpty(), "List should be empty when no input jars are configured");
    }

    /**
     * Tests that getInJarFilters returns a list with a null entry when one input jar
     * is configured without filters.
     */
    @Test
    public void testGetInJarFilters_singleInputJar_noFilters() throws Exception {
        // Given: A single input jar configured without filters
        task.injars("input.jar");

        // When: Getting the input jar filters list
        List result = task.getInJarFilters();

        // Then: The list should contain one null entry
        assertNotNull(result, "List should not be null");
        assertEquals(1, result.size(), "List should contain 1 entry");
        assertNull(result.get(0), "Entry should be null when no filters are specified");
    }

    /**
     * Tests that getInJarFilters returns a list with a Map entry when one input jar
     * is configured with filters.
     */
    @Test
    public void testGetInJarFilters_singleInputJar_withFilters() throws Exception {
        // Given: A single input jar configured with filters
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("filter", "!**.class");
        task.injars(filterArgs, "input.jar");

        // When: Getting the input jar filters list
        List result = task.getInJarFilters();

        // Then: The list should contain the filter Map
        assertNotNull(result, "List should not be null");
        assertEquals(1, result.size(), "List should contain 1 entry");
        assertNotNull(result.get(0), "Entry should not be null when filters are specified");
        assertSame(filterArgs, result.get(0), "Entry should be the same Map object");
    }

    /**
     * Tests that getInJarFilters maintains 1-to-1 correspondence with input jars.
     */
    @Test
    public void testGetInJarFilters_correspondenceWithInputJars() throws Exception {
        // Given: Multiple input jars, some with filters and some without
        Map<String, String> filter1 = new HashMap<>();
        filter1.put("filter", "!**.class");

        task.injars(filter1, "input1.jar");
        task.injars("input2.jar");  // No filters

        Map<String, String> filter2 = new HashMap<>();
        filter2.put("jarfilter", "!META-INF/**");
        task.injars(filter2, "input3.jar");

        // When: Getting both lists
        List jarFiles = task.getInJarFiles();
        List jarFilters = task.getInJarFilters();

        // Then: Both lists should have the same size with proper correspondence
        assertEquals(jarFiles.size(), jarFilters.size(),
                "Filter list should have same size as jar files list");
        assertEquals(3, jarFilters.size(), "Filter list should contain 3 entries");
        assertSame(filter1, jarFilters.get(0), "First filter should match first jar");
        assertNull(jarFilters.get(1), "Second filter should be null (no filters)");
        assertSame(filter2, jarFilters.get(2), "Third filter should match third jar");
    }

    /**
     * Tests that getInJarFilters preserves the order of filters as they were added.
     */
    @Test
    public void testGetInJarFilters_preservesOrder() throws Exception {
        // Given: Multiple input jars with filters added in a specific order
        Map<String, String> filter1 = new HashMap<>();
        filter1.put("filter", "first");

        Map<String, String> filter2 = new HashMap<>();
        filter2.put("filter", "second");

        Map<String, String> filter3 = new HashMap<>();
        filter3.put("filter", "third");

        task.injars(filter1, "jar1.jar");
        task.injars(filter2, "jar2.jar");
        task.injars(filter3, "jar3.jar");

        // When: Getting the input jar filters list
        List result = task.getInJarFilters();

        // Then: The list should maintain the same order
        assertEquals(3, result.size(), "List should contain 3 entries");
        assertSame(filter1, result.get(0), "First entry should be filter1");
        assertSame(filter2, result.get(1), "Second entry should be filter2");
        assertSame(filter3, result.get(2), "Third entry should be filter3");
    }

    /**
     * Tests that getInJarFilters can be called multiple times and returns the same list instance.
     */
    @Test
    public void testGetInJarFilters_returnsSameListInstance() throws Exception {
        // Given: An input jar with filter configured
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("filter", "!**.class");
        task.injars(filterArgs, "input.jar");

        // When: Getting the input jar filters list multiple times
        List result1 = task.getInJarFilters();
        List result2 = task.getInJarFilters();

        // Then: Both calls should return the same list instance
        assertSame(result1, result2, "Multiple invocations should return the same List instance");
    }

    /**
     * Tests that modifications are reflected in subsequent calls (returns actual internal list).
     */
    @Test
    public void testGetInJarFilters_returnsModifiableList() throws Exception {
        // Given: An input jar with filter configured
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("filter", "!**.class");
        task.injars(filterArgs, "input.jar");

        List result1 = task.getInJarFilters();
        int initialSize = result1.size();

        // When: Adding another input jar
        task.injars("another.jar");

        // Then: The previously obtained list should reflect the change
        assertEquals(initialSize + 1, result1.size(),
                "Original list reference should reflect additions");
    }

    /**
     * Tests that getInJarFilters handles various filter types.
     */
    @Test
    public void testGetInJarFilters_variousFilterTypes() throws Exception {
        // Given: Input jars with different filter types
        Map<String, String> filter1 = new HashMap<>();
        filter1.put("filter", "!**.class");

        Map<String, String> filter2 = new HashMap<>();
        filter2.put("jarfilter", "!META-INF/**");

        Map<String, String> filter3 = new HashMap<>();
        filter3.put("aarfilter", "!res/**");

        task.injars(filter1, "input1.jar");
        task.injars(filter2, "input2.jar");
        task.injars(filter3, "input3.aar");

        // When: Getting the input jar filters list
        List result = task.getInJarFilters();

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
     * Tests that getInJarFilters handles filters with multiple keys.
     */
    @Test
    public void testGetInJarFilters_multipleFilterKeys() throws Exception {
        // Given: An input jar with multiple filter keys
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("filter", "!**.class");
        filterArgs.put("jarfilter", "!META-INF/**");
        filterArgs.put("feature", "myFeature");
        task.injars(filterArgs, "input.jar");

        // When: Getting the input jar filters list
        List result = task.getInJarFilters();

        // Then: The Map should contain all keys
        assertEquals(1, result.size(), "List should contain 1 entry");
        Map entry = (Map) result.get(0);
        assertNotNull(entry, "Entry should not be null");
        assertEquals(3, entry.size(), "Filter Map should contain 3 keys");
        assertEquals("!**.class", entry.get("filter"), "Filter key should be present");
        assertEquals("!META-INF/**", entry.get("jarfilter"), "Jarfilter key should be present");
        assertEquals("myFeature", entry.get("feature"), "Feature key should be present");
    }

    /**
     * Tests that getInJarFilters handles mixed scenario with some jars having filters and others not.
     */
    @Test
    public void testGetInJarFilters_mixedFiltersAndNulls() throws Exception {
        // Given: Multiple input jars, alternating between filters and no filters
        Map<String, String> filter1 = new HashMap<>();
        filter1.put("filter", "!**.class");

        task.injars(filter1, "input1.jar");
        task.injars("input2.jar");  // No filters
        task.injars("input3.jar");  // No filters

        Map<String, String> filter4 = new HashMap<>();
        filter4.put("filter", "!**.properties");
        task.injars(filter4, "input4.jar");

        task.injars("input5.jar");  // No filters

        // When: Getting the input jar filters list
        List result = task.getInJarFilters();

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
    public void testGetInJarFilters_storesMapsByReference() throws Exception {
        // Given: An input jar with a filter Map
        Map<String, String> filterArgs = new HashMap<>();
        filterArgs.put("filter", "!**.class");
        task.injars(filterArgs, "input.jar");

        // When: Getting the input jar filters list
        List result = task.getInJarFilters();
        Map retrievedFilter = (Map) result.get(0);

        // Then: The retrieved Map should be the same instance
        assertSame(filterArgs, retrievedFilter, "Filter Map should be stored by reference");

        // And: Modifications to the original Map should affect the stored Map
        filterArgs.put("newKey", "newValue");
        assertEquals("newValue", retrievedFilter.get("newKey"),
                "Changes to original Map should be reflected in stored Map");
    }

    /**
     * Tests that input jar filters are separate from other jar type filters.
     */
    @Test
    public void testGetInJarFilters_separateFromOtherFilterLists() throws Exception {
        // Given: Filters for input, output, and library jars
        Map<String, String> inputFilter = new HashMap<>();
        inputFilter.put("filter", "input-filter");

        Map<String, String> outputFilter = new HashMap<>();
        outputFilter.put("filter", "output-filter");

        Map<String, String> libraryFilter = new HashMap<>();
        libraryFilter.put("filter", "library-filter");

        task.injars(inputFilter, "input.jar");
        task.outjars(outputFilter, "output.jar");
        task.libraryjars(libraryFilter, "library.jar");

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
    public void testGetInJarFilters_emptyFilterMap() throws Exception {
        // Given: An input jar with an empty filter Map
        Map<String, String> emptyFilterArgs = new HashMap<>();
        task.injars(emptyFilterArgs, "input.jar");

        // When: Getting the input jar filters list
        List result = task.getInJarFilters();

        // Then: The list should contain the empty Map (not null)
        assertEquals(1, result.size(), "List should contain 1 entry");
        assertNotNull(result.get(0), "Entry should not be null for empty Map");
        assertSame(emptyFilterArgs, result.get(0), "Entry should be the empty Map");
        assertTrue(((Map) result.get(0)).isEmpty(), "Map should be empty");
    }

    /**
     * Tests the relationship between getInJarFiles() and getInJarFilters() sizes.
     */
    @Test
    public void testGetInJarFilters_sizeMatchesInJarFiles() throws Exception {
        // Given: Various input jars with and without filters
        task.injars("jar1.jar");

        Map<String, String> filter = new HashMap<>();
        filter.put("filter", "!**.class");
        task.injars(filter, "jar2.jar");

        task.injars("jar3.jar");
        task.injars("jar4.jar");

        // When: Getting both lists
        List jarFiles = task.getInJarFiles();
        List jarFilters = task.getInJarFilters();

        // Then: Sizes should always match
        assertEquals(jarFiles.size(), jarFilters.size(),
                "Filter list size should always match jar files list size");
        assertEquals(4, jarFilters.size(), "Both lists should have 4 entries");
    }

    /**
     * Tests that getInJarFilters handles filters for non-jar file types.
     */
    @Test
    public void testGetInJarFilters_nonJarFileTypes() throws Exception {
        // Given: Filters for various file types
        Map<String, String> aarFilter = new HashMap<>();
        aarFilter.put("aarfilter", "!res/**");

        Map<String, String> apkFilter = new HashMap<>();
        apkFilter.put("apkfilter", "!assets/**");

        Map<String, String> warFilter = new HashMap<>();
        warFilter.put("warfilter", "!WEB-INF/**");

        task.injars(aarFilter, "library.aar");
        task.injars(apkFilter, "app.apk");
        task.injars(warFilter, "webapp.war");

        // When: Getting the input jar filters list
        List result = task.getInJarFilters();

        // Then: All filters should be preserved
        assertEquals(3, result.size(), "List should contain 3 entries");
        assertSame(aarFilter, result.get(0), "First entry should be aar filter");
        assertSame(apkFilter, result.get(1), "Second entry should be apk filter");
        assertSame(warFilter, result.get(2), "Third entry should be war filter");
    }
}
