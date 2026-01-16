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
 * Test class for ClassPathPanel.setClassPath(ClassPath) method.
 *
 * The setClassPath method populates the panel's list model with entries from
 * a ClassPath object. These tests verify:
 * - Setting a null ClassPath clears the panel
 * - Setting an empty ClassPath results in an empty panel
 * - Setting a ClassPath with entries populates the panel correctly
 * - Setting a new ClassPath replaces any existing entries
 * - The ClassPath can be retrieved via getClassPath() and matches what was set
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class ClassPathPanelClaude_setClassPathTest {

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
     * Test setting a null ClassPath.
     * According to the method implementation, setting null should clear the list
     * and result in getClassPath() returning null (since an empty list returns null).
     */
    @Test
    public void testSetClassPathWithNull() {
        testFrame = new JFrame("Test Frame");
        classPathPanel = new ClassPathPanel(testFrame, false);

        // First add some entries
        ClassPath initialClassPath = new ClassPath();
        initialClassPath.add(new ClassPathEntry(new File("/test/path1.jar"), false));
        classPathPanel.setClassPath(initialClassPath);

        // Verify it was set
        assertNotNull(classPathPanel.getClassPath(), "ClassPath should be set");
        assertEquals(1, classPathPanel.getClassPath().size(), "Should have 1 entry");

        // Now set to null
        classPathPanel.setClassPath(null);

        // Verify the list is cleared
        assertNull(classPathPanel.getClassPath(), "ClassPath should be null after setting to null");
    }

    /**
     * Test setting an empty ClassPath.
     * An empty ClassPath should result in getClassPath() returning null
     * (per the getClassPath implementation which returns null for empty lists).
     */
    @Test
    public void testSetClassPathWithEmptyClassPath() {
        testFrame = new JFrame("Test Frame");
        classPathPanel = new ClassPathPanel(testFrame, false);

        // Create an empty ClassPath
        ClassPath emptyClassPath = new ClassPath();

        // Set the empty ClassPath
        classPathPanel.setClassPath(emptyClassPath);

        // Verify the result
        assertNull(classPathPanel.getClassPath(),
                   "getClassPath should return null for empty ClassPath");
    }

    /**
     * Test setting a ClassPath with a single entry.
     */
    @Test
    public void testSetClassPathWithSingleEntry() {
        testFrame = new JFrame("Test Frame");
        classPathPanel = new ClassPathPanel(testFrame, false);

        // Create a ClassPath with one entry
        ClassPath classPath = new ClassPath();
        File testFile = new File("/test/library.jar");
        ClassPathEntry entry = new ClassPathEntry(testFile, false);
        classPath.add(entry);

        // Set the ClassPath
        classPathPanel.setClassPath(classPath);

        // Verify the result
        ClassPath result = classPathPanel.getClassPath();
        assertNotNull(result, "ClassPath should not be null");
        assertEquals(1, result.size(), "ClassPath should have 1 entry");
        assertEquals(testFile.getPath(), result.get(0).getFile().getPath(),
                     "Entry file path should match");
        assertFalse(result.get(0).isOutput(), "Entry should not be output");
    }

    /**
     * Test setting a ClassPath with multiple entries.
     */
    @Test
    public void testSetClassPathWithMultipleEntries() {
        testFrame = new JFrame("Test Frame");
        classPathPanel = new ClassPathPanel(testFrame, false);

        // Create a ClassPath with multiple entries
        ClassPath classPath = new ClassPath();
        File file1 = new File("/test/library1.jar");
        File file2 = new File("/test/library2.jar");
        File file3 = new File("/test/classes");

        classPath.add(new ClassPathEntry(file1, false));
        classPath.add(new ClassPathEntry(file2, false));
        classPath.add(new ClassPathEntry(file3, false));

        // Set the ClassPath
        classPathPanel.setClassPath(classPath);

        // Verify the result
        ClassPath result = classPathPanel.getClassPath();
        assertNotNull(result, "ClassPath should not be null");
        assertEquals(3, result.size(), "ClassPath should have 3 entries");

        // Verify each entry
        assertEquals(file1.getPath(), result.get(0).getFile().getPath(),
                     "First entry should match");
        assertEquals(file2.getPath(), result.get(1).getFile().getPath(),
                     "Second entry should match");
        assertEquals(file3.getPath(), result.get(2).getFile().getPath(),
                     "Third entry should match");
    }

    /**
     * Test setting a ClassPath with both input and output entries.
     */
    @Test
    public void testSetClassPathWithInputAndOutputEntries() {
        testFrame = new JFrame("Test Frame");
        classPathPanel = new ClassPathPanel(testFrame, true); // inputAndOutput mode

        // Create a ClassPath with both input and output entries
        ClassPath classPath = new ClassPath();
        File inputFile = new File("/test/input.jar");
        File outputFile = new File("/test/output.jar");

        classPath.add(new ClassPathEntry(inputFile, false)); // input
        classPath.add(new ClassPathEntry(outputFile, true));  // output

        // Set the ClassPath
        classPathPanel.setClassPath(classPath);

        // Verify the result
        ClassPath result = classPathPanel.getClassPath();
        assertNotNull(result, "ClassPath should not be null");
        assertEquals(2, result.size(), "ClassPath should have 2 entries");

        // Verify input entry
        assertFalse(result.get(0).isOutput(), "First entry should be input");
        assertEquals(inputFile.getPath(), result.get(0).getFile().getPath(),
                     "Input file path should match");

        // Verify output entry
        assertTrue(result.get(1).isOutput(), "Second entry should be output");
        assertEquals(outputFile.getPath(), result.get(1).getFile().getPath(),
                     "Output file path should match");
    }

    /**
     * Test that setting a new ClassPath replaces the old one.
     */
    @Test
    public void testSetClassPathReplacesExistingEntries() {
        testFrame = new JFrame("Test Frame");
        classPathPanel = new ClassPathPanel(testFrame, false);

        // Set initial ClassPath
        ClassPath initialClassPath = new ClassPath();
        initialClassPath.add(new ClassPathEntry(new File("/test/old1.jar"), false));
        initialClassPath.add(new ClassPathEntry(new File("/test/old2.jar"), false));
        classPathPanel.setClassPath(initialClassPath);

        // Verify initial state
        assertEquals(2, classPathPanel.getClassPath().size(),
                     "Should have 2 entries initially");

        // Set new ClassPath
        ClassPath newClassPath = new ClassPath();
        newClassPath.add(new ClassPathEntry(new File("/test/new.jar"), false));
        classPathPanel.setClassPath(newClassPath);

        // Verify the old entries are replaced
        ClassPath result = classPathPanel.getClassPath();
        assertNotNull(result, "ClassPath should not be null");
        assertEquals(1, result.size(), "ClassPath should have 1 entry (old entries replaced)");
        assertEquals("/test/new.jar", result.get(0).getFile().getPath(),
                     "Should contain the new entry, not old ones");
    }

    /**
     * Test setting a ClassPath multiple times in succession.
     */
    @Test
    public void testSetClassPathMultipleTimes() {
        testFrame = new JFrame("Test Frame");
        classPathPanel = new ClassPathPanel(testFrame, false);

        // First set
        ClassPath classPath1 = new ClassPath();
        classPath1.add(new ClassPathEntry(new File("/test/first.jar"), false));
        classPathPanel.setClassPath(classPath1);
        assertEquals(1, classPathPanel.getClassPath().size(), "First set should have 1 entry");

        // Second set
        ClassPath classPath2 = new ClassPath();
        classPath2.add(new ClassPathEntry(new File("/test/second.jar"), false));
        classPath2.add(new ClassPathEntry(new File("/test/third.jar"), false));
        classPathPanel.setClassPath(classPath2);
        assertEquals(2, classPathPanel.getClassPath().size(), "Second set should have 2 entries");

        // Third set to null
        classPathPanel.setClassPath(null);
        assertNull(classPathPanel.getClassPath(), "Third set (null) should clear the list");

        // Fourth set
        ClassPath classPath4 = new ClassPath();
        classPath4.add(new ClassPathEntry(new File("/test/fourth.jar"), false));
        classPathPanel.setClassPath(classPath4);
        assertEquals(1, classPathPanel.getClassPath().size(), "Fourth set should have 1 entry");
    }

    /**
     * Test that order is preserved when setting a ClassPath.
     */
    @Test
    public void testSetClassPathPreservesOrder() {
        testFrame = new JFrame("Test Frame");
        classPathPanel = new ClassPathPanel(testFrame, false);

        // Create a ClassPath with entries in specific order
        ClassPath classPath = new ClassPath();
        File file1 = new File("/test/a.jar");
        File file2 = new File("/test/b.jar");
        File file3 = new File("/test/c.jar");

        classPath.add(new ClassPathEntry(file1, false));
        classPath.add(new ClassPathEntry(file2, false));
        classPath.add(new ClassPathEntry(file3, false));

        // Set the ClassPath
        classPathPanel.setClassPath(classPath);

        // Verify order is preserved
        ClassPath result = classPathPanel.getClassPath();
        assertEquals(file1.getPath(), result.get(0).getFile().getPath(),
                     "First entry should be in first position");
        assertEquals(file2.getPath(), result.get(1).getFile().getPath(),
                     "Second entry should be in second position");
        assertEquals(file3.getPath(), result.get(2).getFile().getPath(),
                     "Third entry should be in third position");
    }
}
