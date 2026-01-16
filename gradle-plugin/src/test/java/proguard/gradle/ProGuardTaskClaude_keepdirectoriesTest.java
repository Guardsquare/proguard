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

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link ProGuardTask#keepdirectories()}.
 *
 * This test class verifies that the keepdirectories() method correctly
 * manages the keepDirectories list in the configuration.
 *
 * The method has three variants:
 * 1. getkeepdirectories() - Groovy DSL support (calls keepdirectories() and returns null)
 * 2. keepdirectories() - No-argument variant (calls keepdirectories(null))
 * 3. keepdirectories(String filter) - With filter parameter
 *
 * Method signature: keepdirectories()V
 * - When called with no filter (null), it clears the keepDirectories list
 * - When called with a filter string, it adds directory patterns to the keepDirectories list
 * - Returns void
 *
 * Key behavior: This method tells ProGuard which directories to keep during processing.
 * The filter can be a comma-separated list of directory patterns. When called without
 * arguments, it clears any previous filters to keep all directories.
 *
 * This method is called by getkeepdirectories() to support Groovy DSL syntax.
 */
public class ProGuardTaskClaude_keepdirectoriesTest {

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
     * Tests that keepdirectories() with no arguments initializes the list and clears it.
     */
    @Test
    public void testKeepdirectories_noArgs_initializesAndClearsList() throws Exception {
        // Given: Initial state where list is null
        assertNull(task.configuration.keepDirectories,
                   "List should initially be null");

        // When: Calling keepdirectories() with no arguments
        task.keepdirectories();

        // Then: The list should be initialized and empty (cleared)
        assertNotNull(task.configuration.keepDirectories,
                      "List should be initialized after calling keepdirectories()");
        assertTrue(task.configuration.keepDirectories.isEmpty(),
                   "List should be empty after calling keepdirectories() with no arguments");
    }

    /**
     * Tests that keepdirectories(String) with a single filter adds it to the list.
     */
    @Test
    public void testKeepdirectories_singleFilter_addsToList() throws Exception {
        // When: Calling keepdirectories() with a single filter
        task.keepdirectories("mydir");

        // Then: The list should contain the filter
        assertNotNull(task.configuration.keepDirectories,
                      "List should be initialized");
        assertEquals(1, task.configuration.keepDirectories.size(),
                     "List should contain 1 filter");
        assertEquals("mydir", task.configuration.keepDirectories.get(0),
                     "Filter should be 'mydir'");
    }

    /**
     * Tests that keepdirectories(String) with multiple comma-separated filters adds them all.
     */
    @Test
    public void testKeepdirectories_multipleFilters_addsAllToList() throws Exception {
        // When: Calling keepdirectories() with multiple comma-separated filters
        task.keepdirectories("dir1,dir2,dir3");

        // Then: The list should contain all filters
        assertNotNull(task.configuration.keepDirectories,
                      "List should be initialized");
        assertEquals(3, task.configuration.keepDirectories.size(),
                     "List should contain 3 filters");
        assertEquals("dir1", task.configuration.keepDirectories.get(0),
                     "First filter should be 'dir1'");
        assertEquals("dir2", task.configuration.keepDirectories.get(1),
                     "Second filter should be 'dir2'");
        assertEquals("dir3", task.configuration.keepDirectories.get(2),
                     "Third filter should be 'dir3'");
    }

    /**
     * Tests that keepdirectories() can be called multiple times to accumulate filters.
     */
    @Test
    public void testKeepdirectories_multipleCalls_accumulatesFilters() throws Exception {
        // When: Calling keepdirectories() multiple times with different filters
        task.keepdirectories("dir1");
        task.keepdirectories("dir2");
        task.keepdirectories("dir3");

        // Then: The list should contain all filters
        assertNotNull(task.configuration.keepDirectories,
                      "List should be initialized");
        assertEquals(3, task.configuration.keepDirectories.size(),
                     "List should contain 3 filters");
        assertEquals("dir1", task.configuration.keepDirectories.get(0),
                     "First filter should be 'dir1'");
        assertEquals("dir2", task.configuration.keepDirectories.get(1),
                     "Second filter should be 'dir2'");
        assertEquals("dir3", task.configuration.keepDirectories.get(2),
                     "Third filter should be 'dir3'");
    }

    /**
     * Tests that keepdirectories() with null clears the list.
     */
    @Test
    public void testKeepdirectories_nullFilter_clearsList() throws Exception {
        // Given: Some filters already added
        task.keepdirectories("dir1,dir2");
        assertEquals(2, task.configuration.keepDirectories.size(),
                     "Should have 2 filters initially");

        // When: Calling keepdirectories() with null (via no-arg variant)
        task.keepdirectories();

        // Then: The list should be cleared
        assertNotNull(task.configuration.keepDirectories,
                      "List should still be initialized");
        assertTrue(task.configuration.keepDirectories.isEmpty(),
                   "List should be empty after calling keepdirectories() with null");
    }

    /**
     * Tests that keepdirectories() initializes list on first call.
     */
    @Test
    public void testKeepdirectories_initiallyNull() throws Exception {
        // Given: A newly created task
        // When: Not calling keepdirectories()
        // Then: The list should be null (default)
        assertNull(task.configuration.keepDirectories,
                   "List should initially be null");
    }

    /**
     * Tests that keepdirectories() works independently of injars.
     */
    @Test
    public void testKeepdirectories_independentFromInjars() throws Exception {
        // Given: Input jars configured
        task.injars("input.jar");

        // When: Calling keepdirectories()
        task.keepdirectories("mydir");

        // Then: Both should be set independently
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.configuration.keepDirectories.size(),
                     "Should have 1 directory filter");
    }

    /**
     * Tests that keepdirectories() works independently of libraryjars.
     */
    @Test
    public void testKeepdirectories_independentFromLibraryjars() throws Exception {
        // Given: Library jars configured
        task.libraryjars("android.jar");

        // When: Calling keepdirectories()
        task.keepdirectories("mydir");

        // Then: Both should be set independently
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.configuration.keepDirectories.size(),
                     "Should have 1 directory filter");
    }

    /**
     * Tests that keepdirectories() works in a complex workflow.
     */
    @Test
    public void testKeepdirectories_complexWorkflow() throws Exception {
        // Given: Complex project setup
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.keepdirectories("META-INF/services");
        task.outjars("output.jar");
        task.configuration("proguard-rules.pro");

        // When: Getting all configurations
        // Then: All should be set correctly
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.configuration.keepDirectories.size(),
                     "Should have 1 directory filter");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
        assertEquals(1, task.getConfigurationFiles().size(), "Should have 1 config file");
    }

    /**
     * Tests that keepdirectories() can be called before other methods.
     */
    @Test
    public void testKeepdirectories_calledBeforeOtherMethods() throws Exception {
        // When: Calling keepdirectories() before other methods
        task.keepdirectories("mydir");
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.outjars("output.jar");

        // Then: Directory filter should still be set
        assertEquals(1, task.configuration.keepDirectories.size(),
                     "Should have 1 directory filter");
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
    }

    /**
     * Tests that keepdirectories() can be called after other methods.
     */
    @Test
    public void testKeepdirectories_calledAfterOtherMethods() throws Exception {
        // Given: Other methods called first
        task.injars("input.jar");
        task.libraryjars("android.jar");
        task.outjars("output.jar");

        // When: Calling keepdirectories() after other methods
        task.keepdirectories("mydir");

        // Then: All should be set correctly
        assertEquals(1, task.configuration.keepDirectories.size(),
                     "Should have 1 directory filter");
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
    }

    /**
     * Tests that keepdirectories() can be interleaved with other method calls.
     */
    @Test
    public void testKeepdirectories_interleavedWithOtherMethods() throws Exception {
        // When: Interleaving keepdirectories() with other methods
        task.injars("input.jar");
        task.keepdirectories("dir1");
        task.libraryjars("android.jar");
        task.keepdirectories("dir2");
        task.outjars("output.jar");

        // Then: All should be set correctly
        assertEquals(2, task.configuration.keepDirectories.size(),
                     "Should have 2 directory filters");
        assertEquals(1, task.getInJarFiles().size(), "Should have 1 input jar");
        assertEquals(1, task.getLibraryJarFiles().size(), "Should have 1 library jar");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
    }

    /**
     * Tests that keepdirectories() works in realistic Android scenario.
     */
    @Test
    public void testKeepdirectories_realisticAndroidScenario() throws Exception {
        // Given: Android project that needs to keep service provider directories
        task.injars("build/intermediates/classes.jar");
        task.libraryjars("${ANDROID_HOME}/platforms/android-28/android.jar");
        task.keepdirectories("META-INF/services"); // Keep service provider config directory
        task.outjars("build/outputs/app-release.jar");

        // When: Getting the configuration
        // Then: Directory filter should be set
        assertEquals(1, task.configuration.keepDirectories.size(),
                     "Should have 1 directory filter for services");
        assertEquals("META-INF/services", task.configuration.keepDirectories.get(0),
                     "Should keep META-INF/services directory");
    }

    /**
     * Tests that keepdirectories() works in realistic Java scenario.
     */
    @Test
    public void testKeepdirectories_realisticJavaScenario() throws Exception {
        // Given: Java project that needs to keep multiple directories
        task.injars("build/libs/app.jar");
        task.libraryjars("${JAVA_HOME}/jre/lib/rt.jar");
        task.keepdirectories("META-INF/services,META-INF/spring"); // Keep multiple directories
        task.outjars("build/libs/app-obfuscated.jar");

        // When: Getting the configuration
        // Then: Directory filters should be set
        assertEquals(2, task.configuration.keepDirectories.size(),
                     "Should have 2 directory filters");
        assertEquals("META-INF/services", task.configuration.keepDirectories.get(0),
                     "Should keep META-INF/services directory");
        assertEquals("META-INF/spring", task.configuration.keepDirectories.get(1),
                     "Should keep META-INF/spring directory");
    }

    /**
     * Tests that keepdirectories() with wildcards works correctly.
     */
    @Test
    public void testKeepdirectories_withWildcards() throws Exception {
        // When: Calling keepdirectories() with wildcard patterns
        task.keepdirectories("META-INF/**");

        // Then: The wildcard pattern should be stored
        assertEquals(1, task.configuration.keepDirectories.size(),
                     "Should have 1 directory filter");
        assertEquals("META-INF/**", task.configuration.keepDirectories.get(0),
                     "Wildcard pattern should be preserved");
    }

    /**
     * Tests that keepdirectories() can be called in sequence with configuration.
     */
    @Test
    public void testKeepdirectories_withConfiguration() throws Exception {
        // Given: Configuration files
        task.configuration("proguard-rules.pro");

        // When: Calling keepdirectories()
        task.keepdirectories("mydir");

        task.configuration("proguard-debug.pro");

        // Then: All should be set correctly
        assertEquals(2, task.getConfigurationFiles().size(), "Should have 2 config files");
        assertEquals(1, task.configuration.keepDirectories.size(),
                     "Should have 1 directory filter");
    }

    /**
     * Tests that keepdirectories() works with multiple library jars.
     */
    @Test
    public void testKeepdirectories_withMultipleLibraryJars() throws Exception {
        // Given: Multiple library jars
        task.libraryjars("android.jar");
        task.libraryjars("support-v4.jar");
        task.libraryjars("support-v7.jar");

        // When: Calling keepdirectories()
        task.keepdirectories("META-INF/services");

        // Then: Directory filter should be set and all libraries should be tracked
        assertEquals(3, task.getLibraryJarFiles().size(), "Should have 3 library jars");
        assertEquals(1, task.configuration.keepDirectories.size(),
                     "Should have 1 directory filter");
    }

    /**
     * Tests that keepdirectories() is idempotent when clearing.
     */
    @Test
    public void testKeepdirectories_idempotentClearing() throws Exception {
        // Given: Some filters already added
        task.keepdirectories("dir1,dir2");
        assertEquals(2, task.configuration.keepDirectories.size(),
                     "Should have 2 filters initially");

        // When: Calling keepdirectories() multiple times to clear
        task.keepdirectories();
        task.keepdirectories();

        // Then: The list should remain cleared
        assertTrue(task.configuration.keepDirectories.isEmpty(),
                   "List should remain empty after multiple clearing calls");
    }

    /**
     * Tests that keepdirectories() behavior is consistent across task instances.
     */
    @Test
    public void testKeepdirectories_consistentAcrossTasks() throws Exception {
        // Given: Two different task instances
        ProGuardTask task1 = project.getTasks().create("testProguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("testProguard2", ProGuardTask.class);

        // When: Calling keepdirectories() on both
        task1.keepdirectories("dir1");
        task2.keepdirectories("dir2");

        // Then: Each should have its own list
        assertEquals(1, task1.configuration.keepDirectories.size(),
                     "Task1 should have 1 filter");
        assertEquals("dir1", task1.configuration.keepDirectories.get(0),
                     "Task1 should have 'dir1'");
        assertEquals(1, task2.configuration.keepDirectories.size(),
                     "Task2 should have 1 filter");
        assertEquals("dir2", task2.configuration.keepDirectories.get(0),
                     "Task2 should have 'dir2'");
    }

    /**
     * Tests that keepdirectories() returns void.
     */
    @Test
    public void testKeepdirectories_returnsVoid() throws Exception {
        // When: Calling keepdirectories()
        task.keepdirectories("mydir"); // Method returns void

        // Then: The side effect should occur (list should be populated)
        assertEquals(1, task.configuration.keepDirectories.size(),
                     "List should have 1 filter");
    }

    /**
     * Tests that keepdirectories() and getkeepdirectories() produce the same result.
     */
    @Test
    public void testKeepdirectories_equivalentToGetter() throws Exception {
        // Given: Two tasks
        ProGuardTask task1 = project.getTasks().create("testProguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("testProguard2", ProGuardTask.class);

        // When: Calling keepdirectories() on one and getkeepdirectories() on other
        task1.keepdirectories();
        task2.getkeepdirectories();

        // Then: Both should have the same effect (cleared list)
        assertNotNull(task1.configuration.keepDirectories,
                      "Task1 list should be initialized");
        assertNotNull(task2.configuration.keepDirectories,
                      "Task2 list should be initialized");
        assertTrue(task1.configuration.keepDirectories.isEmpty(),
                   "Task1 list should be empty");
        assertTrue(task2.configuration.keepDirectories.isEmpty(),
                   "Task2 list should be empty");
    }

    /**
     * Tests that getkeepdirectories() returns null as expected.
     */
    @Test
    public void testGetkeepdirectories_returnsNull() throws Exception {
        // When: Calling getkeepdirectories()
        Object result = task.getkeepdirectories();

        // Then: Should return null (Groovy DSL hack)
        assertNull(result, "getkeepdirectories() should return null");

        // And: The side effect should occur
        assertNotNull(task.configuration.keepDirectories,
                      "List should be initialized");
    }

    /**
     * Tests that keepdirectories() can handle directories with spaces.
     */
    @Test
    public void testKeepdirectories_withSpaces() throws Exception {
        // When: Calling keepdirectories() with directory containing spaces
        task.keepdirectories("my dir,another dir");

        // Then: The directories should be stored with spaces
        assertEquals(2, task.configuration.keepDirectories.size(),
                     "Should have 2 directory filters");
        assertEquals("my dir", task.configuration.keepDirectories.get(0),
                     "First directory should have space");
        assertEquals("another dir", task.configuration.keepDirectories.get(1),
                     "Second directory should have space");
    }

    /**
     * Tests that keepdirectories() can be called at any point in the configuration.
     */
    @Test
    public void testKeepdirectories_flexibleCallOrder() throws Exception {
        // When: Calling at various points
        task.injars("input1.jar");
        task.keepdirectories("dir1");
        task.libraryjars("lib1.jar");
        task.injars("input2.jar");
        task.keepdirectories("dir2");
        task.outjars("output.jar");
        task.libraryjars("lib2.jar");

        // Then: Directory filters should be set regardless of call order
        assertEquals(2, task.configuration.keepDirectories.size(),
                     "Should have 2 directory filters");
        assertEquals(2, task.getInJarFiles().size(), "Should have 2 input jars");
        assertEquals(2, task.getLibraryJarFiles().size(), "Should have 2 library jars");
        assertEquals(1, task.getOutJarFiles().size(), "Should have 1 output jar");
    }

    /**
     * Tests that keepdirectories() handles empty string.
     */
    @Test
    public void testKeepdirectories_emptyString() throws Exception {
        // When: Calling keepdirectories() with empty string
        task.keepdirectories("");

        // Then: The list should be initialized but empty
        assertNotNull(task.configuration.keepDirectories,
                      "List should be initialized");
        assertTrue(task.configuration.keepDirectories.isEmpty(),
                   "List should be empty for empty string");
    }

    /**
     * Tests that keepdirectories() works with path separators.
     */
    @Test
    public void testKeepdirectories_withPathSeparators() throws Exception {
        // When: Calling keepdirectories() with path separators
        task.keepdirectories("META-INF/services,org/example/config");

        // Then: The paths should be stored as-is
        assertEquals(2, task.configuration.keepDirectories.size(),
                     "Should have 2 directory filters");
        assertEquals("META-INF/services", task.configuration.keepDirectories.get(0),
                     "First path should have separator");
        assertEquals("org/example/config", task.configuration.keepDirectories.get(1),
                     "Second path should have separator");
    }

    /**
     * Tests that keepdirectories() can clear and then add new filters.
     */
    @Test
    public void testKeepdirectories_clearAndAdd() throws Exception {
        // Given: Some filters already added
        task.keepdirectories("dir1,dir2");
        assertEquals(2, task.configuration.keepDirectories.size(),
                     "Should have 2 filters initially");

        // When: Clearing and then adding new filters
        task.keepdirectories(); // Clear
        task.keepdirectories("dir3,dir4");

        // Then: Should only have the new filters
        assertEquals(2, task.configuration.keepDirectories.size(),
                     "Should have 2 filters after clear and add");
        assertEquals("dir3", task.configuration.keepDirectories.get(0),
                     "First filter should be 'dir3'");
        assertEquals("dir4", task.configuration.keepDirectories.get(1),
                     "Second filter should be 'dir4'");
    }

    /**
     * Tests that keepdirectories() can handle negation patterns.
     */
    @Test
    public void testKeepdirectories_withNegation() throws Exception {
        // When: Calling keepdirectories() with negation pattern
        task.keepdirectories("!test/**");

        // Then: The negation pattern should be stored
        assertEquals(1, task.configuration.keepDirectories.size(),
                     "Should have 1 directory filter");
        assertEquals("!test/**", task.configuration.keepDirectories.get(0),
                     "Negation pattern should be preserved");
    }

    /**
     * Tests that keepdirectories() works in a scenario with multiple filter types.
     */
    @Test
    public void testKeepdirectories_multipleFilterTypes() throws Exception {
        // When: Calling keepdirectories() with various filter types
        task.keepdirectories("META-INF/services,!test/**,**/.git");

        // Then: All filter types should be stored
        assertEquals(3, task.configuration.keepDirectories.size(),
                     "Should have 3 directory filters");
        assertEquals("META-INF/services", task.configuration.keepDirectories.get(0),
                     "First filter is a simple path");
        assertEquals("!test/**", task.configuration.keepDirectories.get(1),
                     "Second filter is a negation");
        assertEquals("**/.git", task.configuration.keepDirectories.get(2),
                     "Third filter is a wildcard");
    }
}
