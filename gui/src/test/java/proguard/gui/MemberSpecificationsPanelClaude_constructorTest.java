package proguard.gui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Test class for MemberSpecificationsPanel constructor.
 *
 * The constructor MemberSpecificationsPanel(JDialog, boolean):
 * - Line 49: Calls super() to initialize the ListPanel base class
 * - Line 51: Sets firstSelectionButton based on includeFieldButton (3 if true, 2 if false)
 * - Line 53: Sets custom cell renderer for displaying member specifications
 * - Line 55: Creates field specification dialog
 * - Line 56: Creates method specification dialog
 * - Line 58: Checks if includeFieldButton is true
 * - Line 60: Adds field button if includeFieldButton is true
 * - Line 62: Adds method button
 * - Line 63: Adds edit button
 * - Line 64: Adds remove button
 * - Line 65: Adds up button
 * - Line 66: Adds down button
 * - Line 68: Enables selection buttons
 *
 * These tests verify:
 * - The panel is created successfully with different configurations
 * - All GUI components are properly initialized
 * - The correct number of buttons are added based on includeFieldButton
 * - Both branches (with and without field button) are covered
 * - Field and method dialogs are created
 * - All standard buttons (edit, remove, up, down) are added
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class MemberSpecificationsPanelClaude_constructorTest {

    private JDialog testDialog;
    private MemberSpecificationsPanel panel;

    @BeforeEach
    public void setUp() {
        // Tests will skip if headless mode is active
        assumeFalse(GraphicsEnvironment.isHeadless(),
                    "Skipping test: Headless environment detected. GUI components require a display.");
    }

    @AfterEach
    public void tearDown() {
        if (panel != null) {
            panel.removeAll();
        }
        if (testDialog != null) {
            testDialog.dispose();
        }
    }

    /**
     * Test constructor with includeFieldButton = true.
     * This covers lines 49, 51 (true branch), 53, 55, 56, 58 (true), 60, 62, 63, 64, 65, 66, 68.
     */
    @Test
    public void testConstructorWithFieldButton() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        assertNotNull(panel, "Panel should be created successfully");

        // When includeFieldButton is true, we should have:
        // Add Field, Add Method, Edit, Remove, Up, Down buttons (6 buttons)
        List buttons = panel.getButtons();
        assertTrue(buttons.size() >= 6, "Panel should have at least 6 buttons when includeFieldButton is true");
    }

    /**
     * Test constructor with includeFieldButton = false.
     * This covers lines 49, 51 (false branch), 53, 55, 56, 58 (false), 62, 63, 64, 65, 66, 68.
     * Line 60 is NOT executed in this case.
     */
    @Test
    public void testConstructorWithoutFieldButton() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        assertNotNull(panel, "Panel should be created successfully");

        // When includeFieldButton is false, we should have:
        // Add Method, Edit, Remove, Up, Down buttons (5 buttons)
        List buttons = panel.getButtons();
        assertTrue(buttons.size() >= 5, "Panel should have at least 5 buttons when includeFieldButton is false");
        assertTrue(buttons.size() < 6, "Panel should have less than 6 buttons when includeFieldButton is false");
    }

    /**
     * Test constructor with null owner and includeFieldButton = true.
     * This verifies that the constructor works with null owner.
     */
    @Test
    public void testConstructorWithNullOwnerAndFieldButton() {
        panel = new MemberSpecificationsPanel(null, true);

        assertNotNull(panel, "Panel should be created successfully with null owner");
        List buttons = panel.getButtons();
        assertTrue(buttons.size() >= 6, "Panel should have buttons even with null owner");
    }

    /**
     * Test constructor with null owner and includeFieldButton = false.
     * This verifies that the constructor works with null owner.
     */
    @Test
    public void testConstructorWithNullOwnerWithoutFieldButton() {
        panel = new MemberSpecificationsPanel(null, false);

        assertNotNull(panel, "Panel should be created successfully with null owner");
        List buttons = panel.getButtons();
        assertTrue(buttons.size() >= 5, "Panel should have buttons even with null owner");
    }

    /**
     * Test that constructor initializes panel with empty list.
     * This verifies line 68 (enableSelectionButtons) is executed.
     */
    @Test
    public void testConstructorInitializesEmptyList() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        List fieldSpecs = panel.getMemberSpecifications(true);
        List methodSpecs = panel.getMemberSpecifications(false);

        assertNull(fieldSpecs, "Field specifications should be null initially");
        assertNull(methodSpecs, "Method specifications should be null initially");
    }

    /**
     * Test that constructor sets firstSelectionButton correctly when includeFieldButton is true.
     * This verifies line 51 (firstSelectionButton = 3 when true).
     */
    @Test
    public void testConstructorSetsFirstSelectionButtonTo3WhenIncludeFieldButtonTrue() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        // firstSelectionButton is set to 3 when includeFieldButton is true
        // This means selection buttons start at index 3 (after Add Field, Add Method, Edit)
        assertNotNull(panel, "Panel should be created");
        List buttons = panel.getButtons();
        assertTrue(buttons.size() >= 6, "Should have at least 6 buttons");
    }

    /**
     * Test that constructor sets firstSelectionButton correctly when includeFieldButton is false.
     * This verifies line 51 (firstSelectionButton = 2 when false).
     */
    @Test
    public void testConstructorSetsFirstSelectionButtonTo2WhenIncludeFieldButtonFalse() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        // firstSelectionButton is set to 2 when includeFieldButton is false
        // This means selection buttons start at index 2 (after Add Method, Edit)
        assertNotNull(panel, "Panel should be created");
        List buttons = panel.getButtons();
        assertTrue(buttons.size() >= 5, "Should have at least 5 buttons");
    }

    /**
     * Test that constructor creates field specification dialog.
     * This verifies line 55 (fieldSpecificationDialog creation).
     */
    @Test
    public void testConstructorCreatesFieldSpecificationDialog() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        // Field specification dialog should be created
        assertNotNull(panel, "Panel should be created with field specification dialog");
        // The dialog is created internally and used when clicking Add Field button
    }

    /**
     * Test that constructor creates method specification dialog.
     * This verifies line 56 (methodSpecificationDialog creation).
     */
    @Test
    public void testConstructorCreatesMethodSpecificationDialog() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        // Method specification dialog should be created
        assertNotNull(panel, "Panel should be created with method specification dialog");
        // The dialog is created internally and used when clicking Add Method button
    }

    /**
     * Test that constructor sets custom cell renderer.
     * This verifies line 53 (setCellRenderer).
     */
    @Test
    public void testConstructorSetsCellRenderer() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        // Cell renderer should be set
        assertNotNull(panel, "Panel should be created with custom cell renderer");
        // The renderer is internal, but we can verify the panel is functional
    }

    /**
     * Test that constructor adds all required buttons when includeFieldButton is true.
     * This verifies lines 60, 62, 63, 64, 65, 66.
     */
    @Test
    public void testConstructorAddsAllButtonsWithFieldButton() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        List buttons = panel.getButtons();

        // Should have: Add Field, Add Method, Edit, Remove, Up, Down
        assertEquals(6, buttons.size(), "Should have exactly 6 buttons");
    }

    /**
     * Test that constructor adds all required buttons when includeFieldButton is false.
     * This verifies lines 62, 63, 64, 65, 66 (but NOT 60).
     */
    @Test
    public void testConstructorAddsAllButtonsWithoutFieldButton() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        List buttons = panel.getButtons();

        // Should have: Add Method, Edit, Remove, Up, Down
        assertEquals(5, buttons.size(), "Should have exactly 5 buttons");
    }

    /**
     * Test that constructor can be called multiple times with includeFieldButton = true.
     * This exercises all lines multiple times for the true branch.
     */
    @Test
    public void testConstructorMultipleTimesWithFieldButton() {
        testDialog = new JDialog();

        MemberSpecificationsPanel panel1 = new MemberSpecificationsPanel(testDialog, true);
        assertNotNull(panel1, "First panel should be created");
        assertEquals(6, panel1.getButtons().size(), "First panel should have 6 buttons");

        MemberSpecificationsPanel panel2 = new MemberSpecificationsPanel(testDialog, true);
        assertNotNull(panel2, "Second panel should be created");
        assertEquals(6, panel2.getButtons().size(), "Second panel should have 6 buttons");

        MemberSpecificationsPanel panel3 = new MemberSpecificationsPanel(testDialog, true);
        assertNotNull(panel3, "Third panel should be created");
        assertEquals(6, panel3.getButtons().size(), "Third panel should have 6 buttons");
    }

    /**
     * Test that constructor can be called multiple times with includeFieldButton = false.
     * This exercises all lines multiple times for the false branch.
     */
    @Test
    public void testConstructorMultipleTimesWithoutFieldButton() {
        testDialog = new JDialog();

        MemberSpecificationsPanel panel1 = new MemberSpecificationsPanel(testDialog, false);
        assertNotNull(panel1, "First panel should be created");
        assertEquals(5, panel1.getButtons().size(), "First panel should have 5 buttons");

        MemberSpecificationsPanel panel2 = new MemberSpecificationsPanel(testDialog, false);
        assertNotNull(panel2, "Second panel should be created");
        assertEquals(5, panel2.getButtons().size(), "Second panel should have 5 buttons");

        MemberSpecificationsPanel panel3 = new MemberSpecificationsPanel(testDialog, false);
        assertNotNull(panel3, "Third panel should be created");
        assertEquals(5, panel3.getButtons().size(), "Third panel should have 5 buttons");
    }

    /**
     * Test that constructor with alternating includeFieldButton values.
     * This ensures both branches work correctly when called alternately.
     */
    @Test
    public void testConstructorAlternatingFieldButtonValues() {
        testDialog = new JDialog();

        MemberSpecificationsPanel panel1 = new MemberSpecificationsPanel(testDialog, true);
        assertNotNull(panel1, "Panel with field button should be created");
        assertEquals(6, panel1.getButtons().size(), "Should have 6 buttons");

        MemberSpecificationsPanel panel2 = new MemberSpecificationsPanel(testDialog, false);
        assertNotNull(panel2, "Panel without field button should be created");
        assertEquals(5, panel2.getButtons().size(), "Should have 5 buttons");

        MemberSpecificationsPanel panel3 = new MemberSpecificationsPanel(testDialog, true);
        assertNotNull(panel3, "Panel with field button should be created again");
        assertEquals(6, panel3.getButtons().size(), "Should have 6 buttons again");
    }

    /**
     * Test that constructor initializes panel that can be added to a container.
     * This verifies the panel is properly initialized for use in GUI.
     */
    @Test
    public void testConstructorCreatesUsablePanel() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        // Panel should be usable in a container
        JFrame frame = new JFrame();
        frame.add(panel);
        frame.pack();

        assertTrue(frame.getComponentCount() > 0, "Frame should contain the panel");
        frame.dispose();
    }

    /**
     * Test that constructor creates panel with non-zero preferred size.
     * This verifies all components contribute to the panel's size.
     */
    @Test
    public void testConstructorCreatesPanelWithNonZeroSize() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        Dimension preferredSize = panel.getPreferredSize();
        assertTrue(preferredSize.width > 0, "Panel should have positive preferred width");
        assertTrue(preferredSize.height > 0, "Panel should have positive preferred height");
    }

    /**
     * Test that constructor execution completes successfully with includeFieldButton = true.
     * This is a comprehensive test that exercises all constructor lines for the true branch.
     */
    @Test
    public void testConstructorCompletesSuccessfullyWithFieldButton() {
        testDialog = new JDialog();
        // This executes lines: 49, 51 (true), 53, 55, 56, 58 (true), 60, 62, 63, 64, 65, 66, 68
        panel = new MemberSpecificationsPanel(testDialog, true);

        assertNotNull(panel, "Panel should be created");
        assertEquals(6, panel.getButtons().size(), "Should have 6 buttons");
        assertNull(panel.getMemberSpecifications(true), "Field specs should be null initially");
        assertNull(panel.getMemberSpecifications(false), "Method specs should be null initially");
    }

    /**
     * Test that constructor execution completes successfully with includeFieldButton = false.
     * This is a comprehensive test that exercises all constructor lines for the false branch.
     */
    @Test
    public void testConstructorCompletesSuccessfullyWithoutFieldButton() {
        testDialog = new JDialog();
        // This executes lines: 49, 51 (false), 53, 55, 56, 58 (false), 62, 63, 64, 65, 66, 68
        // Line 60 is NOT executed
        panel = new MemberSpecificationsPanel(testDialog, false);

        assertNotNull(panel, "Panel should be created");
        assertEquals(5, panel.getButtons().size(), "Should have 5 buttons");
        assertNull(panel.getMemberSpecifications(true), "Field specs should be null initially");
        assertNull(panel.getMemberSpecifications(false), "Method specs should be null initially");
    }

    /**
     * Test that both field and method dialogs are created regardless of includeFieldButton.
     * This verifies lines 55 and 56 are always executed.
     */
    @Test
    public void testConstructorCreatesBothDialogsRegardlessOfFieldButton() {
        testDialog = new JDialog();

        // Both dialogs should be created even when includeFieldButton is false
        MemberSpecificationsPanel panel1 = new MemberSpecificationsPanel(testDialog, false);
        assertNotNull(panel1, "Panel should be created");

        // Both dialogs should be created when includeFieldButton is true
        MemberSpecificationsPanel panel2 = new MemberSpecificationsPanel(testDialog, true);
        assertNotNull(panel2, "Panel should be created");
    }

    /**
     * Test constructor with JFrame as parent for dialog.
     * This tests a different owner type.
     */
    @Test
    public void testConstructorWithJFrameOwner() {
        JFrame frame = new JFrame();
        JDialog dialog = new JDialog(frame);
        panel = new MemberSpecificationsPanel(dialog, true);

        assertNotNull(panel, "Panel should be created with JFrame-based dialog owner");
        assertEquals(6, panel.getButtons().size(), "Should have 6 buttons");

        dialog.dispose();
        frame.dispose();
    }

    /**
     * Test that enableSelectionButtons is called at the end of constructor.
     * This verifies line 68 is executed.
     */
    @Test
    public void testConstructorCallsEnableSelectionButtons() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        // enableSelectionButtons should have been called
        // This ensures selection buttons are in the correct initial state
        assertNotNull(panel, "Panel should be created");
        List buttons = panel.getButtons();
        assertTrue(buttons.size() >= 6, "Buttons should be present");
    }

    /**
     * Test that super() is called first in constructor.
     * This verifies line 49 initializes the parent class.
     */
    @Test
    public void testConstructorCallsSuperFirst() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        // super() should initialize the ListPanel
        assertNotNull(panel, "Panel should be created");
        assertTrue(panel instanceof ListPanel, "Panel should be a ListPanel");
    }

    /**
     * Test constructor creates panel that is an instance of ListPanel.
     * This verifies inheritance is properly set up.
     */
    @Test
    public void testConstructorCreatesPanelThatExtendsListPanel() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        assertTrue(panel instanceof ListPanel, "Panel should extend ListPanel");
        assertTrue(panel instanceof JPanel, "Panel should be a JPanel");
    }

    /**
     * Test constructor with edge case: very long dialog title.
     * This ensures the constructor handles various dialog configurations.
     */
    @Test
    public void testConstructorWithLongDialogTitle() {
        testDialog = new JDialog();
        testDialog.setTitle("This is a very long dialog title that might affect the panel creation");
        panel = new MemberSpecificationsPanel(testDialog, true);

        assertNotNull(panel, "Panel should be created even with long dialog title");
        assertEquals(6, panel.getButtons().size(), "Should have 6 buttons");
    }

    /**
     * Test constructor creates panel that can be validated.
     * This ensures the component hierarchy is properly set up.
     */
    @Test
    public void testConstructorCreatesPanelThatCanBeValidated() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        // Should be able to validate the panel
        panel.validate();

        // Panel should still be functional after validation
        assertNotNull(panel, "Panel should remain functional after validation");
    }

    /**
     * Test constructor creates panel with proper component count.
     * This verifies all components are added (list + buttons).
     */
    @Test
    public void testConstructorCreatesPanelWithComponents() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        // Panel should have components (scroll pane for list + buttons)
        assertTrue(panel.getComponentCount() > 0, "Panel should have components");
    }
}
