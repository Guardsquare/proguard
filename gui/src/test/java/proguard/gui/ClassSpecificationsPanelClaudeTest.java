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
 * Test class for ClassSpecificationsPanel.
 *
 * This test class verifies the functionality of ClassSpecificationsPanel, which provides
 * a GUI panel for managing a list of ClassSpecification objects. The panel enables users
 * to add, edit, move, and remove ClassSpecification entries.
 *
 * Tests cover:
 * - Constructor: initialization with different configurations
 * - addAddButton: functionality of the add button
 * - addEditButton: functionality of the edit button
 * - createClassSpecification: factory method for creating new specifications
 * - setClassSpecification: delegating specification to the dialog
 * - getClassSpecification: retrieving specification from the dialog
 * - setClassSpecifications: setting the list of specifications
 * - getClassSpecifications: retrieving the list of specifications
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class ClassSpecificationsPanelClaudeTest {

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

    // ========== Constructor Tests ==========

    /**
     * Test constructor with includeKeepSettings=false and includeFieldButton=false.
     * Verifies that the panel is properly initialized with basic configuration.
     */
    @Test
    public void testConstructorBasicConfiguration() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, false);

        assertNotNull(panel, "Panel should be created");
        assertNotNull(panel.classSpecificationDialog, "Dialog should be initialized");
        assertTrue(panel.getButtons().size() >= 5,
                   "Panel should have at least 5 buttons (add, edit, remove, up, down)");
    }

    /**
     * Test constructor with includeKeepSettings=true and includeFieldButton=true.
     * Verifies that the panel is properly initialized with full configuration.
     */
    @Test
    public void testConstructorFullConfiguration() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, true, true);

        assertNotNull(panel, "Panel should be created");
        assertNotNull(panel.classSpecificationDialog, "Dialog should be initialized");
        assertTrue(panel.getButtons().size() >= 5,
                   "Panel should have at least 5 buttons (add, edit, remove, up, down)");
    }

    /**
     * Test constructor with includeKeepSettings=true and includeFieldButton=false.
     * Verifies that the panel can be created with mixed configuration.
     */
    @Test
    public void testConstructorMixedConfiguration1() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, true, false);

        assertNotNull(panel, "Panel should be created");
        assertNotNull(panel.classSpecificationDialog, "Dialog should be initialized");
    }

    /**
     * Test constructor with includeKeepSettings=false and includeFieldButton=true.
     * Verifies that the panel can be created with mixed configuration.
     */
    @Test
    public void testConstructorMixedConfiguration2() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        assertNotNull(panel, "Panel should be created");
        assertNotNull(panel.classSpecificationDialog, "Dialog should be initialized");
    }

    /**
     * Test constructor with null owner.
     * Verifies that the panel can be created with null owner frame.
     */
    @Test
    public void testConstructorWithNullOwner() {
        panel = new ClassSpecificationsPanel(null, false, true);

        assertNotNull(panel, "Panel should be created with null owner");
        assertNotNull(panel.classSpecificationDialog, "Dialog should be initialized");
    }

    /**
     * Test that constructor properly initializes the list model.
     */
    @Test
    public void testConstructorInitializesListModel() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        // List should be empty initially
        assertNull(panel.getClassSpecifications(),
                   "Initial list should be null (empty)");
    }

    /**
     * Test that constructor properly sets up buttons.
     */
    @Test
    public void testConstructorSetsUpButtons() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        List<Component> buttons = panel.getButtons();
        assertNotNull(buttons, "Buttons list should not be null");
        assertEquals(5, buttons.size(),
                     "Should have exactly 5 buttons (add, edit, remove, up, down)");

        // All buttons should be JButton instances
        for (Component button : buttons) {
            assertTrue(button instanceof JButton,
                       "Each button should be a JButton");
        }
    }

    // ========== addAddButton Tests ==========

    /**
     * Test that the add button is created and added to the panel.
     * The addAddButton method is called by the constructor.
     */
    @Test
    public void testAddAddButtonCreatesButton() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        List<Component> buttons = panel.getButtons();
        assertTrue(buttons.size() > 0, "Add button should be created");

        // First button should be the add button
        Component firstButton = buttons.get(0);
        assertTrue(firstButton instanceof JButton, "First button should be JButton");
    }

    /**
     * Test that the add button has action listeners.
     */
    @Test
    public void testAddAddButtonHasActionListener() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        List<Component> buttons = panel.getButtons();
        JButton addButton = (JButton) buttons.get(0);

        assertTrue(addButton.getActionListeners().length > 0,
                   "Add button should have action listeners");
    }

    /**
     * Test that the add button has a tooltip.
     */
    @Test
    public void testAddAddButtonHasTooltip() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        List<Component> buttons = panel.getButtons();
        JButton addButton = (JButton) buttons.get(0);

        assertNotNull(addButton.getToolTipText(),
                      "Add button should have a tooltip");
    }

    // ========== addEditButton Tests ==========

    /**
     * Test that the edit button is created and added to the panel.
     * The addEditButton method is called by the constructor.
     */
    @Test
    public void testAddEditButtonCreatesButton() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        List<Component> buttons = panel.getButtons();
        assertTrue(buttons.size() > 1, "Edit button should be created");

        // Second button should be the edit button
        Component secondButton = buttons.get(1);
        assertTrue(secondButton instanceof JButton, "Second button should be JButton");
    }

    /**
     * Test that the edit button has action listeners.
     */
    @Test
    public void testAddEditButtonHasActionListener() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        List<Component> buttons = panel.getButtons();
        JButton editButton = (JButton) buttons.get(1);

        assertTrue(editButton.getActionListeners().length > 0,
                   "Edit button should have action listeners");
    }

    /**
     * Test that the edit button has a tooltip.
     */
    @Test
    public void testAddEditButtonHasTooltip() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        List<Component> buttons = panel.getButtons();
        JButton editButton = (JButton) buttons.get(1);

        assertNotNull(editButton.getToolTipText(),
                      "Edit button should have a tooltip");
    }

    /**
     * Test that the edit button is initially disabled (no selection).
     */
    @Test
    public void testEditButtonInitiallyDisabled() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        List<Component> buttons = panel.getButtons();
        JButton editButton = (JButton) buttons.get(1);

        assertFalse(editButton.isEnabled(),
                    "Edit button should be initially disabled when no items are selected");
    }

    // ========== createClassSpecification Tests ==========

    /**
     * Test that createClassSpecification returns a new ClassSpecification instance.
     */
    @Test
    public void testCreateClassSpecificationReturnsNewInstance() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        ClassSpecification spec = panel.createClassSpecification();

        assertNotNull(spec, "createClassSpecification should return non-null instance");
    }

    /**
     * Test that createClassSpecification returns a new instance each time.
     */
    @Test
    public void testCreateClassSpecificationReturnsNewInstanceEachTime() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        ClassSpecification spec1 = panel.createClassSpecification();
        ClassSpecification spec2 = panel.createClassSpecification();

        assertNotSame(spec1, spec2,
                      "createClassSpecification should return different instances");
    }

    /**
     * Test that createClassSpecification returns an empty ClassSpecification.
     */
    @Test
    public void testCreateClassSpecificationReturnsEmptySpec() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        ClassSpecification spec = panel.createClassSpecification();

        // Default ClassSpecification should have null or default values
        assertNull(spec.comments, "New specification should have null comments");
        assertNull(spec.className, "New specification should have null className");
    }

    // ========== setClassSpecification Tests ==========

    /**
     * Test setClassSpecification with a valid ClassSpecification.
     * This method delegates to the dialog.
     */
    @Test
    public void testSetClassSpecificationWithValidSpec() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        ClassSpecification spec = new ClassSpecification(
            "Test", 0, 0, null, "com.example.TestClass", null, null
        );

        // Should not throw exception
        panel.setClassSpecification(spec);

        // Verify it was set in the dialog
        ClassSpecification retrieved = panel.getClassSpecification();
        assertEquals("Test", retrieved.comments,
                     "Comments should be set in dialog");
        assertEquals("com.example.TestClass", retrieved.className,
                     "ClassName should be set in dialog");
    }

    /**
     * Test setClassSpecification with null specification.
     */
    @Test
    public void testSetClassSpecificationWithNull() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        // Should not throw exception
        panel.setClassSpecification(null);
    }

    /**
     * Test setClassSpecification multiple times.
     */
    @Test
    public void testSetClassSpecificationMultipleTimes() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        ClassSpecification spec1 = new ClassSpecification(
            "Test1", 0, 0, null, "com.example.Class1", null, null
        );
        ClassSpecification spec2 = new ClassSpecification(
            "Test2", 0, 0, null, "com.example.Class2", null, null
        );

        panel.setClassSpecification(spec1);
        ClassSpecification retrieved1 = panel.getClassSpecification();
        assertEquals("Test1", retrieved1.comments);

        panel.setClassSpecification(spec2);
        ClassSpecification retrieved2 = panel.getClassSpecification();
        assertEquals("Test2", retrieved2.comments);
    }

    // ========== getClassSpecification Tests ==========

    /**
     * Test getClassSpecification returns value from dialog.
     */
    @Test
    public void testGetClassSpecificationFromDialog() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        ClassSpecification spec = new ClassSpecification(
            "Test", 0, 0, null, "com.example.TestClass", null, null
        );

        panel.setClassSpecification(spec);
        ClassSpecification retrieved = panel.getClassSpecification();

        assertNotNull(retrieved, "getClassSpecification should return non-null");
        assertEquals("Test", retrieved.comments);
        assertEquals("com.example.TestClass", retrieved.className);
    }

    /**
     * Test getClassSpecification consistency.
     */
    @Test
    public void testGetClassSpecificationConsistency() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        ClassSpecification spec = new ClassSpecification(
            "Test", 0, 0, null, "com.example.TestClass", null, null
        );

        panel.setClassSpecification(spec);
        ClassSpecification retrieved1 = panel.getClassSpecification();
        ClassSpecification retrieved2 = panel.getClassSpecification();

        // Should return the same values
        assertEquals(retrieved1.comments, retrieved2.comments);
        assertEquals(retrieved1.className, retrieved2.className);
    }

    // ========== setClassSpecifications Tests ==========

    /**
     * Test setClassSpecifications with null list.
     * Should clear the panel.
     */
    @Test
    public void testSetClassSpecificationsWithNull() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        panel.setClassSpecifications(null);

        List result = panel.getClassSpecifications();
        assertNull(result, "Setting null should result in empty list");
    }

    /**
     * Test setClassSpecifications with empty list.
     */
    @Test
    public void testSetClassSpecificationsWithEmptyList() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        List<ClassSpecification> emptyList = new ArrayList<>();
        panel.setClassSpecifications(emptyList);

        List result = panel.getClassSpecifications();
        assertNull(result, "Setting empty list should result in null");
    }

    /**
     * Test setClassSpecifications with single specification.
     */
    @Test
    public void testSetClassSpecificationsWithSingleSpec() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        ClassSpecification spec = new ClassSpecification(
            "Test", 0, 0, null, "com.example.TestClass", null, null
        );
        List<ClassSpecification> list = new ArrayList<>();
        list.add(spec);

        panel.setClassSpecifications(list);

        List result = panel.getClassSpecifications();
        assertNotNull(result, "Should return non-null list");
        assertEquals(1, result.size(), "Should have 1 specification");
        assertEquals("Test", ((ClassSpecification)result.get(0)).comments);
    }

    /**
     * Test setClassSpecifications with multiple specifications.
     */
    @Test
    public void testSetClassSpecificationsWithMultipleSpecs() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        ClassSpecification spec1 = new ClassSpecification(
            "Test1", 0, 0, null, "com.example.Class1", null, null
        );
        ClassSpecification spec2 = new ClassSpecification(
            "Test2", 0, 0, null, "com.example.Class2", null, null
        );
        ClassSpecification spec3 = new ClassSpecification(
            "Test3", 0, 0, null, "com.example.Class3", null, null
        );

        List<ClassSpecification> list = new ArrayList<>();
        list.add(spec1);
        list.add(spec2);
        list.add(spec3);

        panel.setClassSpecifications(list);

        List result = panel.getClassSpecifications();
        assertNotNull(result, "Should return non-null list");
        assertEquals(3, result.size(), "Should have 3 specifications");
        assertEquals("Test1", ((ClassSpecification)result.get(0)).comments);
        assertEquals("Test2", ((ClassSpecification)result.get(1)).comments);
        assertEquals("Test3", ((ClassSpecification)result.get(2)).comments);
    }

    /**
     * Test setClassSpecifications replaces existing content.
     */
    @Test
    public void testSetClassSpecificationsReplacesExisting() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        // Set initial list
        ClassSpecification spec1 = new ClassSpecification(
            "Test1", 0, 0, null, "com.example.Class1", null, null
        );
        List<ClassSpecification> list1 = new ArrayList<>();
        list1.add(spec1);
        panel.setClassSpecifications(list1);

        // Set new list
        ClassSpecification spec2 = new ClassSpecification(
            "Test2", 0, 0, null, "com.example.Class2", null, null
        );
        List<ClassSpecification> list2 = new ArrayList<>();
        list2.add(spec2);
        panel.setClassSpecifications(list2);

        List result = panel.getClassSpecifications();
        assertEquals(1, result.size(), "Should have 1 specification after replacement");
        assertEquals("Test2", ((ClassSpecification)result.get(0)).comments,
                     "Should have the new specification, not the old one");
    }

    /**
     * Test setClassSpecifications clears previous content before adding new.
     */
    @Test
    public void testSetClassSpecificationsClearsPrevious() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        // Set initial list with 3 items
        List<ClassSpecification> list1 = new ArrayList<>();
        list1.add(new ClassSpecification("Test1", 0, 0, null, "Class1", null, null));
        list1.add(new ClassSpecification("Test2", 0, 0, null, "Class2", null, null));
        list1.add(new ClassSpecification("Test3", 0, 0, null, "Class3", null, null));
        panel.setClassSpecifications(list1);
        assertEquals(3, panel.getClassSpecifications().size());

        // Set new list with 1 item
        List<ClassSpecification> list2 = new ArrayList<>();
        list2.add(new ClassSpecification("NewTest", 0, 0, null, "NewClass", null, null));
        panel.setClassSpecifications(list2);

        List result = panel.getClassSpecifications();
        assertEquals(1, result.size(), "Should only have 1 item after replacement");
        assertEquals("NewTest", ((ClassSpecification)result.get(0)).comments);
    }

    // ========== getClassSpecifications Tests ==========

    /**
     * Test getClassSpecifications when empty.
     */
    @Test
    public void testGetClassSpecificationsWhenEmpty() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        List result = panel.getClassSpecifications();
        assertNull(result, "Empty list should return null");
    }

    /**
     * Test getClassSpecifications returns copy of list.
     * Modifying the returned list should not affect the panel.
     */
    @Test
    public void testGetClassSpecificationsReturnsCopy() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        ClassSpecification spec1 = new ClassSpecification(
            "Test1", 0, 0, null, "com.example.Class1", null, null
        );
        List<ClassSpecification> list = new ArrayList<>();
        list.add(spec1);
        panel.setClassSpecifications(list);

        List result1 = panel.getClassSpecifications();
        result1.add(new ClassSpecification("Test2", 0, 0, null, "Class2", null, null));

        List result2 = panel.getClassSpecifications();
        assertEquals(1, result2.size(),
                     "Modifying returned list should not affect panel's internal list");
    }

    /**
     * Test getClassSpecifications maintains order.
     */
    @Test
    public void testGetClassSpecificationsMaintainsOrder() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        List<ClassSpecification> list = new ArrayList<>();
        list.add(new ClassSpecification("A", 0, 0, null, "ClassA", null, null));
        list.add(new ClassSpecification("B", 0, 0, null, "ClassB", null, null));
        list.add(new ClassSpecification("C", 0, 0, null, "ClassC", null, null));
        panel.setClassSpecifications(list);

        List result = panel.getClassSpecifications();
        assertEquals("A", ((ClassSpecification)result.get(0)).comments);
        assertEquals("B", ((ClassSpecification)result.get(1)).comments);
        assertEquals("C", ((ClassSpecification)result.get(2)).comments);
    }

    /**
     * Test round-trip: set then get should return same data.
     */
    @Test
    public void testRoundTripSetGetClassSpecifications() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        List<ClassSpecification> original = new ArrayList<>();
        original.add(new ClassSpecification("Test1", 1, 2, null, "Class1", "extends", null));
        original.add(new ClassSpecification("Test2", 3, 4, null, "Class2", "implements", null));

        panel.setClassSpecifications(original);
        List result = panel.getClassSpecifications();

        assertEquals(original.size(), result.size());

        for (int i = 0; i < original.size(); i++) {
            ClassSpecification orig = original.get(i);
            ClassSpecification res = (ClassSpecification) result.get(i);
            assertEquals(orig.comments, res.comments);
            assertEquals(orig.className, res.className);
            assertEquals(orig.extendsClassName, res.extendsClassName);
        }
    }

    /**
     * Test getClassSpecifications after clearing.
     */
    @Test
    public void testGetClassSpecificationsAfterClear() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        // Set some specifications
        List<ClassSpecification> list = new ArrayList<>();
        list.add(new ClassSpecification("Test", 0, 0, null, "TestClass", null, null));
        panel.setClassSpecifications(list);
        assertEquals(1, panel.getClassSpecifications().size());

        // Clear by setting null
        panel.setClassSpecifications(null);

        List result = panel.getClassSpecifications();
        assertNull(result, "Should return null after clearing");
    }

    /**
     * Test getClassSpecifications with large list.
     */
    @Test
    public void testGetClassSpecificationsWithLargeList() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        List<ClassSpecification> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(new ClassSpecification(
                "Test" + i, 0, 0, null, "Class" + i, null, null
            ));
        }

        panel.setClassSpecifications(list);
        List result = panel.getClassSpecifications();

        assertEquals(100, result.size(), "Should handle large lists");
        assertEquals("Test0", ((ClassSpecification)result.get(0)).comments);
        assertEquals("Test99", ((ClassSpecification)result.get(99)).comments);
    }

    // ========== Integration Tests ==========

    /**
     * Test that panel can be added to a frame and displayed.
     */
    @Test
    public void testPanelCanBeAddedToFrame() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        testFrame.add(panel);
        testFrame.pack();

        assertNotNull(panel.getParent(), "Panel should have a parent after being added");
        assertTrue(panel.getSize().width > 0, "Panel should have positive width");
        assertTrue(panel.getSize().height > 0, "Panel should have positive height");
    }

    /**
     * Test multiple panels can coexist.
     */
    @Test
    public void testMultiplePanelsCanCoexist() {
        testFrame = new JFrame("Test Frame");
        ClassSpecificationsPanel panel1 = new ClassSpecificationsPanel(testFrame, false, true);
        ClassSpecificationsPanel panel2 = new ClassSpecificationsPanel(testFrame, true, false);

        // Set different data in each panel
        List<ClassSpecification> list1 = new ArrayList<>();
        list1.add(new ClassSpecification("Panel1", 0, 0, null, "Class1", null, null));
        panel1.setClassSpecifications(list1);

        List<ClassSpecification> list2 = new ArrayList<>();
        list2.add(new ClassSpecification("Panel2", 0, 0, null, "Class2", null, null));
        panel2.setClassSpecifications(list2);

        // Verify they maintain independent state
        assertEquals("Panel1",
                     ((ClassSpecification)panel1.getClassSpecifications().get(0)).comments);
        assertEquals("Panel2",
                     ((ClassSpecification)panel2.getClassSpecifications().get(0)).comments);
    }

    /**
     * Test that panel properly initializes its components hierarchy.
     */
    @Test
    public void testPanelComponentHierarchy() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        assertTrue(panel.getComponentCount() > 0, "Panel should have components");

        // Panel should contain a JScrollPane with the list
        boolean hasScrollPane = false;
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JScrollPane) {
                hasScrollPane = true;
                break;
            }
        }
        assertTrue(hasScrollPane, "Panel should contain a JScrollPane for the list");
    }

    /**
     * Test panel state after multiple operations.
     */
    @Test
    public void testPanelStateAfterMultipleOperations() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        // Set initial list
        List<ClassSpecification> list1 = new ArrayList<>();
        list1.add(new ClassSpecification("Test1", 0, 0, null, "Class1", null, null));
        panel.setClassSpecifications(list1);
        assertEquals(1, panel.getClassSpecifications().size());

        // Set different list
        List<ClassSpecification> list2 = new ArrayList<>();
        list2.add(new ClassSpecification("Test2", 0, 0, null, "Class2", null, null));
        list2.add(new ClassSpecification("Test3", 0, 0, null, "Class3", null, null));
        panel.setClassSpecifications(list2);
        assertEquals(2, panel.getClassSpecifications().size());

        // Clear
        panel.setClassSpecifications(null);
        assertNull(panel.getClassSpecifications());

        // Set again
        List<ClassSpecification> list3 = new ArrayList<>();
        list3.add(new ClassSpecification("Test4", 0, 0, null, "Class4", null, null));
        panel.setClassSpecifications(list3);
        assertEquals(1, panel.getClassSpecifications().size());
        assertEquals("Test4",
                     ((ClassSpecification)panel.getClassSpecifications().get(0)).comments);
    }
}
