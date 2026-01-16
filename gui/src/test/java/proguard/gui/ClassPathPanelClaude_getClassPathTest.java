package proguard.gui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.ClassPath;
import proguard.ClassPathEntry;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Test class for ClassPathPanel.getClassPath() method.
 *
 * The getClassPath method returns a ClassPath object representing the current
 * state of the panel's list model. These tests verify:
 * - Returns null when the panel is empty
 * - Returns a ClassPath with correct entries when the panel has content
 * - Returns entries in the correct order
 * - Returns a new ClassPath instance (not sharing references)
 * - Correctly handles round-trip operations with setClassPath
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class ClassPathPanelClaude_getClassPathTest {

    private JFrame testFrame;
    private ClassPathPanel classPathPanel;

    @BeforeEach
    public void setUp() {
        // Tests will skip if headless mode is active
        assumeFalse(GraphicsEnvironment.isHeadless(),
                    "Skipping test: Headless environment detected. GUI components require a display.");
    }

    @AfterEach
    public void tearDown() {
        if (testFrame != null) {
            testFrame.dispose();
        }
    }

    /**
     * Test that getClassPath returns null when the panel is empty.
     * A newly created panel has no entries, so getClassPath should return null.
     */
    @Test
    public void testGetClassPathReturnsNullForEmptyPanel() {
        testFrame = new JFrame("Test Frame");
        classPathPanel = new ClassPathPanel(testFrame, false);

        // A new panel should have no entries
        ClassPath result = classPathPanel.getClassPath();

        assertNull(result, "getClassPath should return null for empty panel");
    }

    /**
     * Test that getClassPath returns null after setting an empty ClassPath.
     */
    @Test
    public void testGetClassPathReturnsNullAfterSettingEmptyClassPath() {
        testFrame = new JFrame("Test Frame");
        classPathPanel = new ClassPathPanel(testFrame, false);

        // Set an empty ClassPath
        ClassPath emptyClassPath = new ClassPath();
        classPathPanel.setClassPath(emptyClassPath);

        // getClassPath should return null for empty ClassPath
        ClassPath result = classPathPanel.getClassPath();

        assertNull(result, "getClassPath should return null after setting empty ClassPath");
    }

    /**
     * Test that getClassPath returns null after setting null.
     */
    @Test
    public void testGetClassPathReturnsNullAfterSettingNull() {
        testFrame = new JFrame("Test Frame");
        classPathPanel = new ClassPathPanel(testFrame, false);

        // First add some entries
        ClassPath initialClassPath = new ClassPath();
        initialClassPath.add(new ClassPathEntry(new File("/test/test.jar"), false));
        classPathPanel.setClassPath(initialClassPath);

        // Now set to null
        classPathPanel.setClassPath(null);

        // getClassPath should return null
        ClassPath result = classPathPanel.getClassPath();

        assertNull(result, "getClassPath should return null after setting to null");
    }

    /**
     * Test that getClassPath returns a ClassPath with a single entry.
     */
    @Test
    public void testGetClassPathWithSingleEntry() {
        testFrame = new JFrame("Test Frame");
        classPathPanel = new ClassPathPanel(testFrame, false);

        // Set a ClassPath with one entry
        ClassPath classPath = new ClassPath();
        File testFile = new File("/test/library.jar");
        ClassPathEntry entry = new ClassPathEntry(testFile, false);
        classPath.add(entry);
        classPathPanel.setClassPath(classPath);

        // Get the ClassPath back
        ClassPath result = classPathPanel.getClassPath();

        assertNotNull(result, "getClassPath should not return null");
        assertEquals(1, result.size(), "ClassPath should have 1 entry");
        assertEquals(testFile.getPath(), result.get(0).getFile().getPath(),
                     "Entry file path should match");
        assertFalse(result.get(0).isOutput(), "Entry should not be output");
    }

    /**
     * Test that getClassPath returns a ClassPath with multiple entries in correct order.
     */
    @Test
    public void testGetClassPathWithMultipleEntriesPreservesOrder() {
        testFrame = new JFrame("Test Frame");
        classPathPanel = new ClassPathPanel(testFrame, false);

        // Set a ClassPath with multiple entries
        ClassPath classPath = new ClassPath();
        File file1 = new File("/test/first.jar");
        File file2 = new File("/test/second.jar");
        File file3 = new File("/test/third.jar");

        classPath.add(new ClassPathEntry(file1, false));
        classPath.add(new ClassPathEntry(file2, false));
        classPath.add(new ClassPathEntry(file3, false));
        classPathPanel.setClassPath(classPath);

        // Get the ClassPath back
        ClassPath result = classPathPanel.getClassPath();

        assertNotNull(result, "getClassPath should not return null");
        assertEquals(3, result.size(), "ClassPath should have 3 entries");

        // Verify order is preserved
        assertEquals(file1.getPath(), result.get(0).getFile().getPath(),
                     "First entry should be at index 0");
        assertEquals(file2.getPath(), result.get(1).getFile().getPath(),
                     "Second entry should be at index 1");
        assertEquals(file3.getPath(), result.get(2).getFile().getPath(),
                     "Third entry should be at index 2");
    }

    /**
     * Test that getClassPath correctly handles both input and output entries.
     */
    @Test
    public void testGetClassPathWithInputAndOutputEntries() {
        testFrame = new JFrame("Test Frame");
        classPathPanel = new ClassPathPanel(testFrame, true); // inputAndOutput mode

        // Set a ClassPath with both input and output entries
        ClassPath classPath = new ClassPath();
        File inputFile1 = new File("/test/input1.jar");
        File inputFile2 = new File("/test/input2.jar");
        File outputFile = new File("/test/output.jar");

        classPath.add(new ClassPathEntry(inputFile1, false)); // input
        classPath.add(new ClassPathEntry(outputFile, true));  // output
        classPath.add(new ClassPathEntry(inputFile2, false)); // input
        classPathPanel.setClassPath(classPath);

        // Get the ClassPath back
        ClassPath result = classPathPanel.getClassPath();

        assertNotNull(result, "getClassPath should not return null");
        assertEquals(3, result.size(), "ClassPath should have 3 entries");

        // Verify input/output flags are preserved
        assertFalse(result.get(0).isOutput(), "First entry should be input");
        assertTrue(result.get(1).isOutput(), "Second entry should be output");
        assertFalse(result.get(2).isOutput(), "Third entry should be input");
    }

    /**
     * Test that getClassPath returns a new ClassPath instance (not the original).
     * The returned ClassPath should be independent from the one that was set.
     */
    @Test
    public void testGetClassPathReturnsNewInstance() {
        testFrame = new JFrame("Test Frame");
        classPathPanel = new ClassPathPanel(testFrame, false);

        // Set a ClassPath
        ClassPath originalClassPath = new ClassPath();
        originalClassPath.add(new ClassPathEntry(new File("/test/test.jar"), false));
        classPathPanel.setClassPath(originalClassPath);

        // Get the ClassPath back
        ClassPath result = classPathPanel.getClassPath();

        assertNotNull(result, "getClassPath should not return null");
        assertNotSame(originalClassPath, result,
                      "getClassPath should return a new ClassPath instance, not the original");
    }

    /**
     * Test that modifying the returned ClassPath doesn't affect the panel's internal state.
     */
    @Test
    public void testModifyingReturnedClassPathDoesNotAffectPanel() {
        testFrame = new JFrame("Test Frame");
        classPathPanel = new ClassPathPanel(testFrame, false);

        // Set a ClassPath with one entry
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new File("/test/original.jar"), false));
        classPathPanel.setClassPath(classPath);

        // Get the ClassPath
        ClassPath result1 = classPathPanel.getClassPath();
        assertNotNull(result1, "First call should return non-null");
        assertEquals(1, result1.size(), "Should have 1 entry");

        // Modify the returned ClassPath by adding an entry
        result1.add(new ClassPathEntry(new File("/test/added.jar"), false));
        assertEquals(2, result1.size(), "Modified ClassPath should have 2 entries");

        // Get the ClassPath again - it should still have only 1 entry
        ClassPath result2 = classPathPanel.getClassPath();
        assertNotNull(result2, "Second call should return non-null");
        assertEquals(1, result2.size(),
                     "Panel should still have 1 entry - modification of returned ClassPath should not affect panel");
    }

    /**
     * Test round-trip consistency: setClassPath followed by getClassPath
     * should return equivalent data.
     */
    @Test
    public void testRoundTripConsistency() {
        testFrame = new JFrame("Test Frame");
        classPathPanel = new ClassPathPanel(testFrame, false);

        // Create a ClassPath with various entries
        ClassPath originalClassPath = new ClassPath();
        File file1 = new File("/test/lib1.jar");
        File file2 = new File("/test/lib2.jar");
        File file3 = new File("/test/classes");

        originalClassPath.add(new ClassPathEntry(file1, false));
        originalClassPath.add(new ClassPathEntry(file2, false));
        originalClassPath.add(new ClassPathEntry(file3, false));

        // Set the ClassPath
        classPathPanel.setClassPath(originalClassPath);

        // Get it back
        ClassPath retrievedClassPath = classPathPanel.getClassPath();

        // Verify they contain the same data
        assertNotNull(retrievedClassPath, "Retrieved ClassPath should not be null");
        assertEquals(originalClassPath.size(), retrievedClassPath.size(),
                     "Retrieved ClassPath should have same size as original");

        for (int i = 0; i < originalClassPath.size(); i++) {
            ClassPathEntry original = originalClassPath.get(i);
            ClassPathEntry retrieved = retrievedClassPath.get(i);

            assertEquals(original.getFile().getPath(), retrieved.getFile().getPath(),
                         "Entry " + i + " file path should match");
            assertEquals(original.isOutput(), retrieved.isOutput(),
                         "Entry " + i + " output flag should match");
        }
    }

    /**
     * Test getClassPath after multiple setClassPath operations.
     */
    @Test
    public void testGetClassPathAfterMultipleSets() {
        testFrame = new JFrame("Test Frame");
        classPathPanel = new ClassPathPanel(testFrame, false);

        // First set
        ClassPath classPath1 = new ClassPath();
        classPath1.add(new ClassPathEntry(new File("/test/first.jar"), false));
        classPathPanel.setClassPath(classPath1);

        ClassPath result1 = classPathPanel.getClassPath();
        assertNotNull(result1, "First get should return non-null");
        assertEquals(1, result1.size(), "First get should have 1 entry");

        // Second set with different entries
        ClassPath classPath2 = new ClassPath();
        classPath2.add(new ClassPathEntry(new File("/test/second.jar"), false));
        classPath2.add(new ClassPathEntry(new File("/test/third.jar"), false));
        classPathPanel.setClassPath(classPath2);

        ClassPath result2 = classPathPanel.getClassPath();
        assertNotNull(result2, "Second get should return non-null");
        assertEquals(2, result2.size(), "Second get should have 2 entries");
        assertEquals("/test/second.jar", result2.get(0).getFile().getPath(),
                     "Should have new entries, not old ones");

        // Third set to null
        classPathPanel.setClassPath(null);

        ClassPath result3 = classPathPanel.getClassPath();
        assertNull(result3, "Third get should return null after setting null");
    }

    /**
     * Test that getClassPath works correctly when called multiple times
     * without modifying the panel in between.
     */
    @Test
    public void testGetClassPathMultipleCallsReturnEquivalentData() {
        testFrame = new JFrame("Test Frame");
        classPathPanel = new ClassPathPanel(testFrame, false);

        // Set a ClassPath
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(new File("/test/test.jar"), false));
        classPathPanel.setClassPath(classPath);

        // Call getClassPath multiple times
        ClassPath result1 = classPathPanel.getClassPath();
        ClassPath result2 = classPathPanel.getClassPath();
        ClassPath result3 = classPathPanel.getClassPath();

        // All should be non-null and contain the same data
        assertNotNull(result1, "First call should return non-null");
        assertNotNull(result2, "Second call should return non-null");
        assertNotNull(result3, "Third call should return non-null");

        // They should be different instances
        assertNotSame(result1, result2, "Each call should return a new instance");
        assertNotSame(result2, result3, "Each call should return a new instance");

        // But contain equivalent data
        assertEquals(result1.size(), result2.size(), "All results should have same size");
        assertEquals(result2.size(), result3.size(), "All results should have same size");
        assertEquals(result1.get(0).getFile().getPath(), result2.get(0).getFile().getPath(),
                     "All results should have same entry");
        assertEquals(result2.get(0).getFile().getPath(), result3.get(0).getFile().getPath(),
                     "All results should have same entry");
    }
}
