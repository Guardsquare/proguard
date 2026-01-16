package proguard.gui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.ClassSpecification;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Test class for ClassSpecificationsPanel.getClassSpecifications() method.
 *
 * The getClassSpecifications() method returns the list of ClassSpecification objects
 * currently displayed in the panel.
 *
 * Lines that need coverage:
 * - Line 157: int size = listModel.size() - Get the size of the list model
 * - Line 158: if (size == 0) - Check if list is empty
 * - Line 160: return null - Return null for empty list
 * - Line 163: List classSpecifications = new ArrayList(size) - Create new list with size
 * - Line 164: for (int index = 0; index < size; index++) - Loop through all elements
 * - Line 166: classSpecifications.add(listModel.get(index)) - Add each element to result
 * - Line 169: return classSpecifications - Return the populated list
 *
 * These tests verify:
 * - Empty list returns null (lines 157, 158, 160)
 * - Single specification is returned correctly (lines 157, 158, 163, 164, 166, 169)
 * - Multiple specifications are returned correctly (all lines)
 * - Order is preserved
 * - Result is a copy (defensive programming)
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class ClassSpecificationsPanelClaude_getClassSpecificationsTest {

    private JFrame testFrame;
    private ClassSpecificationsPanel panel;

    @BeforeEach
    public void setUp() {
        // Tests will skip if headless mode is active
        assumeFalse(GraphicsEnvironment.isHeadless(),
                    "Skipping test: Headless environment detected. GUI components require a display.");
    }

    @AfterEach
    public void tearDown() {
        if (panel != null) {
            panel = null;
        }
        if (testFrame != null) {
            testFrame.dispose();
            testFrame = null;
        }
    }

    /**
     * Test getClassSpecifications when list is empty.
     * This covers lines 157 (size = 0), 158 (true branch), 160 (return null).
     */
    @Test
    public void testGetClassSpecificationsWhenEmpty() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        // Initially empty - covers lines 157, 158 (size == 0), 160
        List result = panel.getClassSpecifications();

        assertNull(result, "Empty list should return null");
    }

    /**
     * Test getClassSpecifications with single specification.
     * This covers lines 157, 158 (false), 163, 164 (loop once), 166, 169.
     */
    @Test
    public void testGetClassSpecificationsWithSingleSpec() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        ClassSpecification spec = new ClassSpecification(
            "Test", 0, 0, null, "com.example.TestClass", null, null
        );
        List<ClassSpecification> list = new ArrayList<>();
        list.add(spec);
        panel.setClassSpecifications(list);

        // Covers lines 157, 158 (false), 163, 164 (index 0), 166, 169
        List result = panel.getClassSpecifications();

        assertNotNull(result, "Should return non-null list");
        assertEquals(1, result.size(), "Should have 1 specification");
        assertEquals("Test", ((ClassSpecification)result.get(0)).comments);
    }

    /**
     * Test getClassSpecifications with multiple specifications.
     * This covers lines 157, 158 (false), 163, 164 (loop multiple times), 166, 169.
     */
    @Test
    public void testGetClassSpecificationsWithMultipleSpecs() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        List<ClassSpecification> list = new ArrayList<>();
        list.add(new ClassSpecification("Test1", 0, 0, null, "Class1", null, null));
        list.add(new ClassSpecification("Test2", 0, 0, null, "Class2", null, null));
        list.add(new ClassSpecification("Test3", 0, 0, null, "Class3", null, null));
        panel.setClassSpecifications(list);

        // Covers lines 157, 158 (false), 163, 164 (loop 3 times), 166 (3 times), 169
        List result = panel.getClassSpecifications();

        assertNotNull(result, "Should return non-null list");
        assertEquals(3, result.size(), "Should have 3 specifications");
        assertEquals("Test1", ((ClassSpecification)result.get(0)).comments);
        assertEquals("Test2", ((ClassSpecification)result.get(1)).comments);
        assertEquals("Test3", ((ClassSpecification)result.get(2)).comments);
    }

    /**
     * Test getClassSpecifications after clearing.
     * Verifies transition from non-empty to empty (line 158 true branch).
     */
    @Test
    public void testGetClassSpecificationsAfterClearing() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        // Add some specifications
        List<ClassSpecification> list = new ArrayList<>();
        list.add(new ClassSpecification("Test", 0, 0, null, "TestClass", null, null));
        panel.setClassSpecifications(list);
        assertNotNull(panel.getClassSpecifications());

        // Clear by setting null
        panel.setClassSpecifications(null);

        // Should return null - covers lines 157, 158 (true), 160
        List result = panel.getClassSpecifications();
        assertNull(result, "Should return null after clearing");
    }

    /**
     * Test getClassSpecifications returns a copy (defensive programming).
     * Modifying the returned list should not affect the panel.
     */
    @Test
    public void testGetClassSpecificationsReturnsCopy() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        List<ClassSpecification> list = new ArrayList<>();
        list.add(new ClassSpecification("Test1", 0, 0, null, "Class1", null, null));
        panel.setClassSpecifications(list);

        // Get the list
        List result1 = panel.getClassSpecifications();
        assertNotNull(result1);
        assertEquals(1, result1.size());

        // Modify the returned list
        result1.add(new ClassSpecification("Test2", 0, 0, null, "Class2", null, null));

        // Get again - should still be 1 (not affected by modification)
        List result2 = panel.getClassSpecifications();
        assertEquals(1, result2.size(),
                     "Modifying returned list should not affect panel's internal list");
    }

    /**
     * Test getClassSpecifications maintains order.
     * Verifies the loop (line 164, 166) preserves order.
     */
    @Test
    public void testGetClassSpecificationsMaintainsOrder() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        List<ClassSpecification> list = new ArrayList<>();
        list.add(new ClassSpecification("A", 0, 0, null, "ClassA", null, null));
        list.add(new ClassSpecification("B", 0, 0, null, "ClassB", null, null));
        list.add(new ClassSpecification("C", 0, 0, null, "ClassC", null, null));
        list.add(new ClassSpecification("D", 0, 0, null, "ClassD", null, null));
        panel.setClassSpecifications(list);

        // Covers lines 157, 158 (false), 163, 164 (loop 4 times), 166, 169
        List result = panel.getClassSpecifications();

        assertEquals("A", ((ClassSpecification)result.get(0)).comments);
        assertEquals("B", ((ClassSpecification)result.get(1)).comments);
        assertEquals("C", ((ClassSpecification)result.get(2)).comments);
        assertEquals("D", ((ClassSpecification)result.get(3)).comments);
    }

    /**
     * Test getClassSpecifications with large list.
     * Verifies the loop (line 164, 166) handles many iterations.
     */
    @Test
    public void testGetClassSpecificationsWithLargeList() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        List<ClassSpecification> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(new ClassSpecification("Test" + i, 0, 0, null, "Class" + i, null, null));
        }
        panel.setClassSpecifications(list);

        // Covers line 164 looping 100 times, line 166 adding 100 times
        List result = panel.getClassSpecifications();

        assertNotNull(result);
        assertEquals(100, result.size(), "Should have 100 specifications");
        assertEquals("Test0", ((ClassSpecification)result.get(0)).comments);
        assertEquals("Test99", ((ClassSpecification)result.get(99)).comments);
    }

    /**
     * Test getClassSpecifications called multiple times returns consistent results.
     */
    @Test
    public void testGetClassSpecificationsConsistency() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        List<ClassSpecification> list = new ArrayList<>();
        list.add(new ClassSpecification("Test", 0, 0, null, "TestClass", null, null));
        panel.setClassSpecifications(list);

        // Call multiple times
        List result1 = panel.getClassSpecifications();
        List result2 = panel.getClassSpecifications();
        List result3 = panel.getClassSpecifications();

        // All should have same size and content
        assertEquals(result1.size(), result2.size());
        assertEquals(result2.size(), result3.size());
        assertEquals(
            ((ClassSpecification)result1.get(0)).comments,
            ((ClassSpecification)result2.get(0)).comments
        );
    }

    /**
     * Test getClassSpecifications with boundary size of 1.
     * Tests line 158 boundary condition (size == 0 vs size > 0).
     */
    @Test
    public void testGetClassSpecificationsBoundarySizeOne() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        // Exactly 1 element (boundary between empty and non-empty)
        List<ClassSpecification> list = new ArrayList<>();
        list.add(new ClassSpecification("Single", 0, 0, null, "SingleClass", null, null));
        panel.setClassSpecifications(list);

        // Line 157: size = 1, line 158: false, proceed to line 163
        List result = panel.getClassSpecifications();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    /**
     * Test getClassSpecifications after setting empty list.
     * Verifies empty list handling (line 158 true branch).
     */
    @Test
    public void testGetClassSpecificationsAfterSettingEmptyList() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        // Set empty list
        panel.setClassSpecifications(new ArrayList<>());

        // Should return null - covers lines 157 (size = 0), 158 (true), 160
        List result = panel.getClassSpecifications();

        assertNull(result, "Empty list should return null");
    }

    /**
     * Test getClassSpecifications with specifications having various properties.
     * Verifies line 166 correctly copies different specification types.
     */
    @Test
    public void testGetClassSpecificationsWithVariousProperties() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        List<ClassSpecification> list = new ArrayList<>();
        list.add(new ClassSpecification("Comment1", 1, 2, "Ann1", "Class1", "ExtAnn1", "ExtClass1"));
        list.add(new ClassSpecification("Comment2", 3, 4, "Ann2", "Class2", "ExtAnn2", "ExtClass2"));
        list.add(new ClassSpecification(null, 0, 0, null, null, null, null));
        panel.setClassSpecifications(list);

        // Covers lines 157, 158 (false), 163, 164 (loop 3 times), 166, 169
        List result = panel.getClassSpecifications();

        assertEquals(3, result.size());

        ClassSpecification spec1 = (ClassSpecification) result.get(0);
        assertEquals("Comment1", spec1.comments);
        assertEquals("Class1", spec1.className);

        ClassSpecification spec2 = (ClassSpecification) result.get(1);
        assertEquals("Comment2", spec2.comments);

        ClassSpecification spec3 = (ClassSpecification) result.get(2);
        assertNull(spec3.comments);
    }

    /**
     * Test getClassSpecifications doesn't return internal list reference.
     * Each call should return a new list instance.
     */
    @Test
    public void testGetClassSpecificationsReturnsNewInstance() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        List<ClassSpecification> list = new ArrayList<>();
        list.add(new ClassSpecification("Test", 0, 0, null, "TestClass", null, null));
        panel.setClassSpecifications(list);

        List result1 = panel.getClassSpecifications();
        List result2 = panel.getClassSpecifications();

        // Should be different list instances (line 163 creates new ArrayList each time)
        assertNotSame(result1, result2,
                      "Each call should return a new list instance");
    }

    /**
     * Test round-trip: set then get should return same data.
     */
    @Test
    public void testSetAndGetClassSpecificationsRoundTrip() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        List<ClassSpecification> original = new ArrayList<>();
        original.add(new ClassSpecification("Test1", 1, 2, "Ann1", "Class1", "ExtAnn1", "ExtClass1"));
        original.add(new ClassSpecification("Test2", 3, 4, "Ann2", "Class2", "ExtAnn2", "ExtClass2"));

        panel.setClassSpecifications(original);
        List result = panel.getClassSpecifications();

        assertEquals(original.size(), result.size());
        for (int i = 0; i < original.size(); i++) {
            ClassSpecification orig = original.get(i);
            ClassSpecification res = (ClassSpecification) result.get(i);
            assertEquals(orig.comments, res.comments);
            assertEquals(orig.className, res.className);
        }
    }

    /**
     * Test getClassSpecifications with panel in different configurations.
     */
    @Test
    public void testGetClassSpecificationsWithDifferentPanelConfigurations() {
        testFrame = new JFrame("Test Frame");

        // Test with different constructor parameters
        ClassSpecificationsPanel panel1 = new ClassSpecificationsPanel(testFrame, false, false);
        ClassSpecificationsPanel panel2 = new ClassSpecificationsPanel(testFrame, true, true);

        List<ClassSpecification> list = new ArrayList<>();
        list.add(new ClassSpecification("Test", 0, 0, null, "TestClass", null, null));

        panel1.setClassSpecifications(list);
        panel2.setClassSpecifications(list);

        assertEquals(1, panel1.getClassSpecifications().size());
        assertEquals(1, panel2.getClassSpecifications().size());
    }

    /**
     * Test getClassSpecifications executes without exception.
     * Smoke test for all lines 157, 158, 160, 163, 164, 166, 169.
     */
    @Test
    public void testGetClassSpecificationsExecutesWithoutException() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        // Empty case
        assertDoesNotThrow(() -> panel.getClassSpecifications());

        // Non-empty case
        List<ClassSpecification> list = new ArrayList<>();
        list.add(new ClassSpecification("Test", 0, 0, null, "TestClass", null, null));
        panel.setClassSpecifications(list);

        assertDoesNotThrow(() -> panel.getClassSpecifications());
    }

    /**
     * Test getClassSpecifications with null owner panel.
     */
    @Test
    public void testGetClassSpecificationsWithNullOwner() {
        panel = new ClassSpecificationsPanel(null, false, true);

        // Initially empty
        assertNull(panel.getClassSpecifications());

        // Add data
        List<ClassSpecification> list = new ArrayList<>();
        list.add(new ClassSpecification("Test", 0, 0, null, "TestClass", null, null));
        panel.setClassSpecifications(list);

        List result = panel.getClassSpecifications();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    /**
     * Test getClassSpecifications size calculation (line 157).
     * Verify correct size is used for ArrayList initialization.
     */
    @Test
    public void testGetClassSpecificationsSizeCalculation() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        // Add 5 specifications
        List<ClassSpecification> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(new ClassSpecification("Test" + i, 0, 0, null, "Class" + i, null, null));
        }
        panel.setClassSpecifications(list);

        // Line 157: size should be 5
        // Line 163: ArrayList initialized with size 5
        List result = panel.getClassSpecifications();

        assertEquals(5, result.size());
    }

    /**
     * Test getClassSpecifications loop iteration count (line 164).
     * Verify loop executes correct number of times.
     */
    @Test
    public void testGetClassSpecificationsLoopIterations() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        // Test with various sizes
        for (int expectedSize : new int[]{1, 2, 5, 10}) {
            List<ClassSpecification> list = new ArrayList<>();
            for (int i = 0; i < expectedSize; i++) {
                list.add(new ClassSpecification("Test" + i, 0, 0, null, "Class" + i, null, null));
            }
            panel.setClassSpecifications(list);

            // Line 164 should loop expectedSize times
            List result = panel.getClassSpecifications();
            assertEquals(expectedSize, result.size(),
                        "Loop should execute " + expectedSize + " times");
        }
    }

    /**
     * Test getClassSpecifications with null elements in the list.
     * Verifies line 166 handles null elements.
     */
    @Test
    public void testGetClassSpecificationsWithNullElements() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        List<ClassSpecification> list = new ArrayList<>();
        list.add(new ClassSpecification("Test1", 0, 0, null, "Class1", null, null));
        list.add(null); // null element
        list.add(new ClassSpecification("Test2", 0, 0, null, "Class2", null, null));
        panel.setClassSpecifications(list);

        // Line 166 should add null element as well
        List result = panel.getClassSpecifications();

        assertEquals(3, result.size());
        assertNotNull(result.get(0));
        assertNull(result.get(1)); // null element preserved
        assertNotNull(result.get(2));
    }

    /**
     * Test getClassSpecifications returns correct type.
     * Verifies line 163, 169 return proper List type.
     */
    @Test
    public void testGetClassSpecificationsReturnsCorrectType() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        List<ClassSpecification> list = new ArrayList<>();
        list.add(new ClassSpecification("Test", 0, 0, null, "TestClass", null, null));
        panel.setClassSpecifications(list);

        List result = panel.getClassSpecifications();

        assertNotNull(result);
        assertTrue(result instanceof List, "Should return a List instance");
        assertTrue(result instanceof ArrayList, "Should be ArrayList implementation");
    }
}
