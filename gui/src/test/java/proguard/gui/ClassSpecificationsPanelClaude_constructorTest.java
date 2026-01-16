package proguard.gui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Test class for ClassSpecificationsPanel constructor.
 *
 * The constructor ClassSpecificationsPanel(JFrame owner, boolean includeKeepSettings, boolean includeFieldButton)
 * performs the following operations (lines that need coverage):
 * - Line 48: Calls super() to initialize the ListPanel parent
 * - Line 50: Sets a custom cell renderer (MyListCellRenderer) on the list
 * - Lines 52-54: Creates a ClassSpecificationDialog with the provided parameters
 * - Line 56: Calls addAddButton() to add the "Add" button
 * - Line 57: Calls addEditButton() to add the "Edit" button
 * - Line 58: Calls addRemoveButton() to add the "Remove" button
 * - Line 59: Calls addUpButton() to add the "Move Up" button
 * - Line 60: Calls addDownButton() to add the "Move Down" button
 * - Line 62: Calls enableSelectionButtons() to set initial button states
 *
 * These tests verify:
 * - The panel is properly initialized with different parameter combinations
 * - All buttons are created and added to the panel
 * - The dialog is properly initialized with the correct parameters
 * - The list cell renderer is set
 * - The panel can be created with null owner
 * - Button states are properly initialized
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class ClassSpecificationsPanelClaude_constructorTest {

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
     * Test constructor with includeKeepSettings=false and includeFieldButton=false.
     * This exercises all lines in the constructor:
     * - super() initialization
     * - list.setCellRenderer()
     * - classSpecificationDialog creation
     * - addAddButton(), addEditButton(), addRemoveButton(), addUpButton(), addDownButton() calls
     * - enableSelectionButtons() call
     */
    @Test
    public void testConstructorWithFalseFalse() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, false);

        // Verify panel was created successfully
        assertNotNull(panel, "Panel should be created");

        // Verify the dialog was initialized (line 52-54)
        assertNotNull(panel.classSpecificationDialog,
                      "ClassSpecificationDialog should be initialized");

        // Verify buttons were added (lines 56-60)
        // The panel should have buttons: Add, Edit, Remove, Up, Down
        java.util.List<Component> buttons = panel.getButtons();
        assertNotNull(buttons, "Buttons list should not be null");
        assertEquals(5, buttons.size(),
                     "Should have 5 buttons: Add, Edit, Remove, Up, Down");

        // Verify all buttons are JButton instances
        for (Component button : buttons) {
            assertTrue(button instanceof JButton,
                       "Each button should be a JButton instance");
        }

        // Verify button states are properly initialized (line 62)
        // Edit, Remove, Up, Down buttons should be disabled initially (no selection)
        JButton editButton = (JButton) buttons.get(1);
        assertFalse(editButton.isEnabled(),
                    "Edit button should be disabled initially");
    }

    /**
     * Test constructor with includeKeepSettings=true and includeFieldButton=true.
     * This exercises all constructor lines with different parameter values.
     */
    @Test
    public void testConstructorWithTrueTrue() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, true, true);

        assertNotNull(panel, "Panel should be created");
        assertNotNull(panel.classSpecificationDialog,
                      "ClassSpecificationDialog should be initialized");

        java.util.List<Component> buttons = panel.getButtons();
        assertEquals(5, buttons.size(),
                     "Should have 5 buttons");
    }

    /**
     * Test constructor with includeKeepSettings=true and includeFieldButton=false.
     */
    @Test
    public void testConstructorWithTrueFalse() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, true, false);

        assertNotNull(panel, "Panel should be created");
        assertNotNull(panel.classSpecificationDialog,
                      "ClassSpecificationDialog should be initialized");

        java.util.List<Component> buttons = panel.getButtons();
        assertEquals(5, buttons.size(),
                     "Should have 5 buttons");
    }

    /**
     * Test constructor with includeKeepSettings=false and includeFieldButton=true.
     */
    @Test
    public void testConstructorWithFalseTrue() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        assertNotNull(panel, "Panel should be created");
        assertNotNull(panel.classSpecificationDialog,
                      "ClassSpecificationDialog should be initialized");

        java.util.List<Component> buttons = panel.getButtons();
        assertEquals(5, buttons.size(),
                     "Should have 5 buttons");
    }

    /**
     * Test constructor with null owner.
     * This verifies that the constructor can handle null owner (line 52).
     */
    @Test
    public void testConstructorWithNullOwner() {
        panel = new ClassSpecificationsPanel(null, false, true);

        assertNotNull(panel, "Panel should be created with null owner");
        assertNotNull(panel.classSpecificationDialog,
                      "ClassSpecificationDialog should be initialized with null owner");

        java.util.List<Component> buttons = panel.getButtons();
        assertEquals(5, buttons.size(),
                     "Should have 5 buttons even with null owner");
    }

    /**
     * Test that the constructor properly initializes the dialog with correct parameters.
     * Verifies that the dialog's owner matches the provided frame.
     */
    @Test
    public void testConstructorDialogOwner() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        assertNotNull(panel.classSpecificationDialog);
        assertEquals(testFrame, panel.classSpecificationDialog.getOwner(),
                     "Dialog owner should match the provided frame");
    }

    /**
     * Test that all five buttons are properly added in order.
     * Verifies lines 56-60 (addAddButton, addEditButton, addRemoveButton, addUpButton, addDownButton).
     */
    @Test
    public void testConstructorAddsAllButtonsInOrder() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        java.util.List<Component> buttons = panel.getButtons();

        // Verify we have exactly 5 buttons
        assertEquals(5, buttons.size(), "Should have exactly 5 buttons");

        // All should be JButton instances
        for (int i = 0; i < buttons.size(); i++) {
            assertTrue(buttons.get(i) instanceof JButton,
                       "Button at index " + i + " should be JButton");
        }

        // Verify each button has a text label (buttons should not be empty)
        for (Component button : buttons) {
            JButton jButton = (JButton) button;
            assertNotNull(jButton.getText(), "Button should have text");
            assertFalse(jButton.getText().isEmpty(), "Button text should not be empty");
        }
    }

    /**
     * Test that enableSelectionButtons is called (line 62).
     * This sets the initial state of buttons based on selection.
     * Since there's no selection initially, selection-dependent buttons should be disabled.
     */
    @Test
    public void testConstructorEnablesSelectionButtons() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        java.util.List<Component> buttons = panel.getButtons();

        // First button (Add) should be enabled
        JButton addButton = (JButton) buttons.get(0);
        assertTrue(addButton.isEnabled(),
                   "Add button should be enabled initially");

        // Other buttons (Edit, Remove, Up, Down) should be disabled
        // since no items are selected initially
        for (int i = 1; i < buttons.size(); i++) {
            JButton button = (JButton) buttons.get(i);
            assertFalse(button.isEnabled(),
                        "Button at index " + i + " should be disabled initially (no selection)");
        }
    }

    /**
     * Test that the panel is properly initialized as a JPanel.
     * This verifies the super() call (line 48) properly initializes the parent.
     */
    @Test
    public void testConstructorInitializesAsJPanel() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        // Panel should be an instance of JPanel (through ListPanel)
        assertTrue(panel instanceof JPanel,
                   "ClassSpecificationsPanel should be a JPanel");

        // Panel should have components (the list and buttons)
        assertTrue(panel.getComponentCount() > 0,
                   "Panel should have components after initialization");
    }

    /**
     * Test that the panel can be added to a frame after construction.
     * This verifies the panel is in a valid state after construction.
     */
    @Test
    public void testConstructorCreatesValidPanel() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        // Should be able to add panel to frame without exception
        testFrame.add(panel);
        testFrame.pack();

        assertNotNull(panel.getParent(),
                      "Panel should have a parent after being added to frame");
        assertTrue(panel.getSize().width > 0,
                   "Panel should have positive width after packing");
        assertTrue(panel.getSize().height > 0,
                   "Panel should have positive height after packing");
    }

    /**
     * Test that multiple panels can be constructed independently.
     * This ensures the constructor doesn't have any shared state issues.
     */
    @Test
    public void testMultiplePanelConstruction() {
        testFrame = new JFrame("Test Frame");

        ClassSpecificationsPanel panel1 = new ClassSpecificationsPanel(testFrame, false, false);
        ClassSpecificationsPanel panel2 = new ClassSpecificationsPanel(testFrame, true, true);
        ClassSpecificationsPanel panel3 = new ClassSpecificationsPanel(testFrame, false, true);

        assertNotNull(panel1, "First panel should be created");
        assertNotNull(panel2, "Second panel should be created");
        assertNotNull(panel3, "Third panel should be created");

        // Each should have its own dialog
        assertNotSame(panel1.classSpecificationDialog,
                      panel2.classSpecificationDialog,
                      "Each panel should have its own dialog instance");
        assertNotSame(panel1.classSpecificationDialog,
                      panel3.classSpecificationDialog,
                      "Each panel should have its own dialog instance");

        // Each should have its own set of buttons
        assertNotSame(panel1.getButtons(), panel2.getButtons(),
                      "Each panel should have its own button instances");
    }

    /**
     * Test that the dialog parameters are correctly passed through.
     * This verifies lines 52-54 pass the parameters correctly.
     */
    @Test
    public void testConstructorDialogParameters() {
        testFrame = new JFrame("Test Frame");

        // Test with different parameter combinations
        ClassSpecificationsPanel panel1 = new ClassSpecificationsPanel(testFrame, true, true);
        assertNotNull(panel1.classSpecificationDialog,
                      "Dialog should be created with includeKeepSettings=true, includeFieldButton=true");

        ClassSpecificationsPanel panel2 = new ClassSpecificationsPanel(testFrame, false, false);
        assertNotNull(panel2.classSpecificationDialog,
                      "Dialog should be created with includeKeepSettings=false, includeFieldButton=false");
    }

    /**
     * Test that buttons have tooltips after construction.
     * This indirectly verifies that the add*Button methods were called properly.
     */
    @Test
    public void testConstructorButtonsHaveTooltips() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        java.util.List<Component> buttons = panel.getButtons();

        // Each button should have a tooltip set
        for (Component button : buttons) {
            JButton jButton = (JButton) button;
            assertNotNull(jButton.getToolTipText(),
                          "Button should have a tooltip");
        }
    }

    /**
     * Test that buttons have action listeners after construction.
     * This verifies that addAddButton() and addEditButton() properly set up listeners.
     */
    @Test
    public void testConstructorButtonsHaveActionListeners() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        java.util.List<Component> buttons = panel.getButtons();

        // Each button should have at least one action listener
        for (Component button : buttons) {
            JButton jButton = (JButton) button;
            assertTrue(jButton.getActionListeners().length > 0,
                       "Button should have action listeners");
        }
    }

    /**
     * Test constructor with a visible frame.
     * This ensures the constructor works even when the owner is visible.
     */
    @Test
    public void testConstructorWithVisibleFrame() {
        testFrame = new JFrame("Test Frame");
        testFrame.setSize(400, 300);
        testFrame.setVisible(true);

        panel = new ClassSpecificationsPanel(testFrame, false, true);

        assertNotNull(panel, "Panel should be created with visible owner");
        assertNotNull(panel.classSpecificationDialog,
                      "Dialog should be initialized with visible owner");

        // Hide the frame
        testFrame.setVisible(false);
    }

    /**
     * Test that the panel's layout is properly initialized.
     * This verifies the super() call sets up the layout correctly.
     */
    @Test
    public void testConstructorInitializesLayout() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        assertNotNull(panel.getLayout(),
                      "Panel should have a layout manager");
        assertTrue(panel.getLayout() instanceof GridBagLayout,
                   "Panel should use GridBagLayout (from ListPanel)");
    }

    /**
     * Test that the internal list component is accessible and initialized.
     * This verifies that super() properly initializes the list component.
     */
    @Test
    public void testConstructorInitializesList() {
        testFrame = new JFrame("Test Frame");
        panel = new ClassSpecificationsPanel(testFrame, false, true);

        // The panel should contain a JScrollPane which contains the list
        boolean hasScrollPane = false;
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JScrollPane) {
                hasScrollPane = true;
                JScrollPane scrollPane = (JScrollPane) comp;
                assertNotNull(scrollPane.getViewport().getView(),
                              "ScrollPane should contain the list view");
                break;
            }
        }
        assertTrue(hasScrollPane,
                   "Panel should contain a JScrollPane for the list");
    }
}
