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
 * Tests for {@link ProGuardTask#getInJarCounts()}.
 *
 * This test class verifies that the getInJarCounts method correctly
 * returns a List of Integers representing the number of input jars that
 * correspond to each output jar.
 *
 * For instance, [2, 3] means that:
 *   - the contents of the first 2 input files go to the first output file and
 *   - the contents of the next 3 input files go to the second output file.
 *
 * The inJarCounts list is populated by the outjars() method, which records
 * the current size of the inJarFiles list each time an output jar is added.
 */
public class ProGuardTaskClaude_getInJarCountsTest {

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
     * Tests that getInJarCounts returns an empty list when no output jars have been configured.
     */
    @Test
    public void testGetInJarCounts_noOutputJars() {
        // When: No output jars are configured
        List result = task.getInJarCounts();

        // Then: The list should be empty
        assertNotNull(result, "List should not be null");
        assertTrue(result.isEmpty(), "List should be empty when no output jars are configured");
    }

    /**
     * Tests that getInJarCounts records a count when a single output jar is added.
     * The count should represent the number of input jars at the time the output jar was added.
     */
    @Test
    public void testGetInJarCounts_singleOutputJar_noInputJars() throws Exception {
        // Given: No input jars configured
        // When: A single output jar is configured
        task.outjars("output.jar");

        // Then: The list should contain one entry with value 0
        List result = task.getInJarCounts();
        assertNotNull(result, "List should not be null");
        assertEquals(1, result.size(), "List should contain 1 entry");
        assertEquals(0, result.get(0), "Count should be 0 when no input jars were configured");
    }

    /**
     * Tests that getInJarCounts correctly records the count of input jars
     * when output jar is added after configuring input jars.
     */
    @Test
    public void testGetInJarCounts_singleOutputJar_withInputJars() throws Exception {
        // Given: Three input jars configured
        task.injars("input1.jar");
        task.injars("input2.jar");
        task.injars("input3.jar");

        // When: A single output jar is configured
        task.outjars("output.jar");

        // Then: The list should contain one entry with value 3
        List result = task.getInJarCounts();
        assertNotNull(result, "List should not be null");
        assertEquals(1, result.size(), "List should contain 1 entry");
        assertEquals(3, result.get(0), "Count should be 3 (number of input jars)");
    }

    /**
     * Tests that getInJarCounts correctly records cumulative counts for multiple output jars.
     */
    @Test
    public void testGetInJarCounts_multipleOutputJars() throws Exception {
        // Given: Two input jars, then one output jar, then three more input jars, then another output jar
        task.injars("input1.jar");
        task.injars("input2.jar");
        task.outjars("output1.jar");
        task.injars("input3.jar");
        task.injars("input4.jar");
        task.injars("input5.jar");
        task.outjars("output2.jar");

        // When: Getting the inJarCounts
        List result = task.getInJarCounts();

        // Then: The list should contain [2, 5] representing cumulative counts
        assertNotNull(result, "List should not be null");
        assertEquals(2, result.size(), "List should contain 2 entries");
        assertEquals(2, result.get(0), "First count should be 2 (input jars before first output)");
        assertEquals(5, result.get(1), "Second count should be 5 (total input jars before second output)");
    }

    /**
     * Tests that getInJarCounts preserves the order of counts as output jars are added.
     */
    @Test
    public void testGetInJarCounts_preservesOrder() throws Exception {
        // Given: Multiple output jars added in sequence with varying input jar counts
        task.injars("input1.jar");
        task.outjars("output1.jar");  // Count: 1

        task.injars("input2.jar");
        task.injars("input3.jar");
        task.outjars("output2.jar");  // Count: 3

        task.injars("input4.jar");
        task.injars("input5.jar");
        task.injars("input6.jar");
        task.injars("input7.jar");
        task.outjars("output3.jar");  // Count: 7

        // When: Getting the inJarCounts
        List result = task.getInJarCounts();

        // Then: The list should contain [1, 3, 7] in order
        assertNotNull(result, "List should not be null");
        assertEquals(3, result.size(), "List should contain 3 entries");
        assertEquals(1, result.get(0), "First count should be 1");
        assertEquals(3, result.get(1), "Second count should be 3");
        assertEquals(7, result.get(2), "Third count should be 7");
    }

    /**
     * Tests that getInJarCounts works correctly when output jars are configured with filter arguments.
     * Note: Filters don't affect the count logic.
     */
    @Test
    public void testGetInJarCounts_withFilterArgs() throws Exception {
        // Given: Input jars and output jar with filter arguments
        task.injars("input1.jar");
        task.injars("input2.jar");

        java.util.Map<String, String> filterArgs = new java.util.HashMap<>();
        filterArgs.put("filter", "**.class");
        task.outjars(filterArgs, "output.jar");

        // When: Getting the inJarCounts
        List result = task.getInJarCounts();

        // Then: The list should still contain the count correctly
        assertNotNull(result, "List should not be null");
        assertEquals(1, result.size(), "List should contain 1 entry");
        assertEquals(2, result.get(0), "Count should be 2 (filter args don't affect count)");
    }

    /**
     * Tests that getInJarCounts returns the same list instance on multiple invocations.
     */
    @Test
    public void testGetInJarCounts_returnsSameListInstance() throws Exception {
        // Given: An output jar configured
        task.injars("input.jar");
        task.outjars("output.jar");

        // When: Getting the inJarCounts multiple times
        List result1 = task.getInJarCounts();
        List result2 = task.getInJarCounts();

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
    public void testGetInJarCounts_returnsModifiableList() throws Exception {
        // Given: An output jar configured
        task.injars("input.jar");
        task.outjars("output.jar");
        List result1 = task.getInJarCounts();
        int initialSize = result1.size();

        // When: Adding another output jar via outjars()
        task.injars("input2.jar");
        task.outjars("output2.jar");

        // Then: The previously obtained list should reflect the change
        List result2 = task.getInJarCounts();
        assertEquals(initialSize + 1, result1.size(), "Original list reference should reflect additions");
        assertEquals(result2.size(), result1.size(), "Both references should have the same size");
    }

    /**
     * Tests that getInJarCounts handles the case where multiple output jars are added
     * without any input jars in between.
     */
    @Test
    public void testGetInJarCounts_multipleOutputJars_noInputJarsBetween() throws Exception {
        // Given: Input jars added, then multiple output jars without more inputs
        task.injars("input1.jar");
        task.injars("input2.jar");
        task.outjars("output1.jar");
        task.outjars("output2.jar");
        task.outjars("output3.jar");

        // When: Getting the inJarCounts
        List result = task.getInJarCounts();

        // Then: All counts should be the same (2)
        assertNotNull(result, "List should not be null");
        assertEquals(3, result.size(), "List should contain 3 entries");
        assertEquals(2, result.get(0), "First count should be 2");
        assertEquals(2, result.get(1), "Second count should be 2");
        assertEquals(2, result.get(2), "Third count should be 2");
    }

    /**
     * Tests that getInJarCounts correctly handles a complex scenario with interleaved
     * input and output jar configurations.
     */
    @Test
    public void testGetInJarCounts_complexInterleavedScenario() throws Exception {
        // Given: A complex scenario with multiple inputs and outputs
        task.injars("input1.jar");
        task.injars("input2.jar");
        task.injars("input3.jar");
        task.outjars("output1.jar");  // Count: 3

        task.injars("input4.jar");
        task.outjars("output2.jar");  // Count: 4

        task.injars("input5.jar");
        task.injars("input6.jar");
        task.injars("input7.jar");
        task.injars("input8.jar");
        task.injars("input9.jar");
        task.outjars("output3.jar");  // Count: 9

        // When: Getting the inJarCounts
        List result = task.getInJarCounts();

        // Then: The list should contain [3, 4, 9]
        assertNotNull(result, "List should not be null");
        assertEquals(3, result.size(), "List should contain 3 entries");
        assertEquals(3, result.get(0), "First count should be 3");
        assertEquals(4, result.get(1), "Second count should be 4");
        assertEquals(9, result.get(2), "Third count should be 9");
    }

    /**
     * Tests that getInJarCounts returns Integer objects, not primitives.
     */
    @Test
    public void testGetInJarCounts_returnsIntegerObjects() throws Exception {
        // Given: Some input and output jars
        task.injars("input1.jar");
        task.injars("input2.jar");
        task.outjars("output.jar");

        // When: Getting the inJarCounts
        List result = task.getInJarCounts();

        // Then: The list should contain Integer objects
        assertNotNull(result, "List should not be null");
        assertEquals(1, result.size(), "List should contain 1 entry");
        assertTrue(result.get(0) instanceof Integer, "Entry should be an Integer object");
        assertEquals(Integer.valueOf(2), result.get(0), "Value should equal Integer.valueOf(2)");
    }

    /**
     * Tests that getInJarCounts is separate from other jar-related lists.
     */
    @Test
    public void testGetInJarCounts_separateFromOtherLists() throws Exception {
        // Given: Input, output, and library jars configured
        task.injars("input1.jar");
        task.injars("input2.jar");
        task.outjars("output.jar");
        task.libraryjars("library.jar");

        // When: Getting various jar-related lists
        List inJarFiles = task.getInJarFiles();
        List outJarFiles = task.getOutJarFiles();
        List libraryJarFiles = task.getLibraryJarFiles();
        List inJarCounts = task.getInJarCounts();

        // Then: inJarCounts should be distinct and contain count information
        assertNotNull(inJarCounts, "inJarCounts should not be null");
        assertEquals(1, inJarCounts.size(), "inJarCounts should contain 1 entry");
        assertEquals(2, inJarCounts.get(0), "Count should be 2");

        // And: Other lists should have their expected sizes
        assertEquals(2, inJarFiles.size(), "inJarFiles should contain 2 entries");
        assertEquals(1, outJarFiles.size(), "outJarFiles should contain 1 entry");
        assertEquals(1, libraryJarFiles.size(), "libraryJarFiles should contain 1 entry");
    }

    /**
     * Tests that getInJarCounts handles zero input jars followed by zero input jars.
     */
    @Test
    public void testGetInJarCounts_zeroInputJarsForMultipleOutputs() throws Exception {
        // Given: Multiple output jars with no input jars
        task.outjars("output1.jar");
        task.outjars("output2.jar");
        task.outjars("output3.jar");

        // When: Getting the inJarCounts
        List result = task.getInJarCounts();

        // Then: All counts should be 0
        assertNotNull(result, "List should not be null");
        assertEquals(3, result.size(), "List should contain 3 entries");
        assertEquals(0, result.get(0), "First count should be 0");
        assertEquals(0, result.get(1), "Second count should be 0");
        assertEquals(0, result.get(2), "Third count should be 0");
    }

    /**
     * Tests that getInJarCounts correctly records counts when input jars are added
     * using different object types (String, File, etc.).
     */
    @Test
    public void testGetInJarCounts_withDifferentInputJarTypes() throws Exception {
        // Given: Input jars of different types
        task.injars("input1.jar");  // String
        task.injars(new File(tempDir.toFile(), "input2.jar"));  // File
        task.injars("input3.jar");  // String
        task.outjars("output.jar");

        // When: Getting the inJarCounts
        List result = task.getInJarCounts();

        // Then: The count should be 3 regardless of input type
        assertNotNull(result, "List should not be null");
        assertEquals(1, result.size(), "List should contain 1 entry");
        assertEquals(3, result.get(0), "Count should be 3 (all input types counted equally)");
    }

    /**
     * Tests that getInJarCounts correctly records counts when output jars are added
     * using different object types (String, File, etc.).
     */
    @Test
    public void testGetInJarCounts_withDifferentOutputJarTypes() throws Exception {
        // Given: Input jars and output jars of different types
        task.injars("input1.jar");
        task.injars("input2.jar");
        task.outjars("output1.jar");  // String
        task.injars("input3.jar");
        task.outjars(new File(tempDir.toFile(), "output2.jar"));  // File

        // When: Getting the inJarCounts
        List result = task.getInJarCounts();

        // Then: Both counts should be recorded correctly
        assertNotNull(result, "List should not be null");
        assertEquals(2, result.size(), "List should contain 2 entries");
        assertEquals(2, result.get(0), "First count should be 2");
        assertEquals(3, result.get(1), "Second count should be 3");
    }

    /**
     * Tests that library jars do not affect the inJarCounts.
     */
    @Test
    public void testGetInJarCounts_libraryJarsDoNotAffectCounts() throws Exception {
        // Given: Input jars, library jars, and output jars
        task.injars("input1.jar");
        task.injars("input2.jar");
        task.libraryjars("library1.jar");
        task.libraryjars("library2.jar");
        task.libraryjars("library3.jar");
        task.outjars("output.jar");

        // When: Getting the inJarCounts
        List result = task.getInJarCounts();

        // Then: Count should only reflect input jars, not library jars
        assertNotNull(result, "List should not be null");
        assertEquals(1, result.size(), "List should contain 1 entry");
        assertEquals(2, result.get(0), "Count should be 2 (library jars don't count)");
    }

    /**
     * Tests a realistic scenario where multiple input jars are mapped to multiple output jars.
     */
    @Test
    public void testGetInJarCounts_realisticScenario() throws Exception {
        // Given: A realistic scenario with multiple inputs mapped to multiple outputs
        // First output: combines first 3 input jars
        task.injars("app-classes.jar");
        task.injars("app-resources.jar");
        task.injars("app-assets.jar");
        task.outjars("app-combined.jar");

        // Second output: combines next 2 input jars
        task.injars("lib1.jar");
        task.injars("lib2.jar");
        task.outjars("libs-combined.jar");

        // Third output: single input jar
        task.injars("standalone.jar");
        task.outjars("standalone-output.jar");

        // When: Getting the inJarCounts
        List result = task.getInJarCounts();

        // Then: The counts should be [3, 5, 6]
        assertNotNull(result, "List should not be null");
        assertEquals(3, result.size(), "List should contain 3 entries");
        assertEquals(3, result.get(0), "First output should combine 3 input jars");
        assertEquals(5, result.get(1), "Second output should be at total count of 5");
        assertEquals(6, result.get(2), "Third output should be at total count of 6");
    }
}
