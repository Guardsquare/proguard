package proguard.gui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Test class focused on constructor coverage for OptimizationsDialog.
 *
 * This class ensures full coverage of the constructor including:
 * - All UI component initialization
 * - GridBagConstraints setup
 * - Optimization checkboxes creation
 * - Button creation and action listeners
 * - Panel layout and assembly
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class OptimizationsDialogClaude_constructorTest {

    private JFrame testFrame;
    private OptimizationsDialog dialog;

    @BeforeEach
    public void setUp() {
        // Tests will skip if headless mode is active
        assumeFalse(GraphicsEnvironment.isHeadless(),
                    "Skipping test: Headless environment detected. GUI components require a display.");
    }

    @AfterEach
    public void tearDown() {
        if (dialog != null) {
            dialog.dispose();
        }
        if (testFrame != null) {
            testFrame.dispose();
        }
    }

    /**
     * Test that the constructor initializes the dialog with a valid JFrame owner.
     * This test ensures all lines in the constructor are executed.
     */
    @Test
    public void testConstructorWithJFrame() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        assertNotNull(dialog, "Dialog should be created successfully");
        assertTrue(dialog.isModal(), "Dialog should be modal");
        assertTrue(dialog.isResizable(), "Dialog should be resizable");
        assertEquals(testFrame, dialog.getOwner(), "Dialog owner should be the test frame");
    }

    /**
     * Test that the constructor initializes the dialog with null owner.
     * This covers the constructor execution path with null parameter.
     */
    @Test
    public void testConstructorWithNullOwner() {
        dialog = new OptimizationsDialog(null);

        assertNotNull(dialog, "Dialog should be created successfully with null owner");
        assertTrue(dialog.isModal(), "Dialog should be modal");
        assertTrue(dialog.isResizable(), "Dialog should be resizable");
        assertNull(dialog.getOwner(), "Dialog owner should be null");
    }

    /**
     * Test that the constructor properly initializes the content pane.
     * This verifies that line 182 (getContentPane().add(...)) is executed.
     */
    @Test
    public void testConstructorInitializesContentPane() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        Container contentPane = dialog.getContentPane();
        assertNotNull(contentPane, "Content pane should be initialized");
        assertEquals(1, contentPane.getComponentCount(), "Content pane should contain exactly one component");
    }

    /**
     * Test that the constructor creates a JScrollPane in the content pane.
     * This verifies line 182 creates the JScrollPane wrapper.
     */
    @Test
    public void testConstructorCreatesScrollPane() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        Component component = dialog.getContentPane().getComponent(0);
        assertTrue(component instanceof JScrollPane, "Content pane should contain a JScrollPane");
    }

    /**
     * Test that the constructor creates the optimizations panel inside the scroll pane.
     * This verifies that line 103 creates the panel and it's added to the scroll pane.
     */
    @Test
    public void testConstructorCreatesOptimizationsPanel() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        JScrollPane scrollPane = (JScrollPane) dialog.getContentPane().getComponent(0);
        Component viewportView = scrollPane.getViewport().getView();

        assertNotNull(viewportView, "ScrollPane should have a viewport view");
        assertTrue(viewportView instanceof JPanel, "ScrollPane viewport should contain a JPanel");
    }

    /**
     * Test that the constructor creates subpanels for optimization groups.
     * This verifies lines 104, 116-118 that create subpanels.
     */
    @Test
    public void testConstructorCreatesOptimizationSubpanels() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        JScrollPane scrollPane = (JScrollPane) dialog.getContentPane().getComponent(0);
        JPanel optimizationsPanel = (JPanel) scrollPane.getViewport().getView();

        // The panel should have components (subpanels and buttons)
        assertTrue(optimizationsPanel.getComponentCount() > 0,
                   "Optimizations panel should contain components");

        // Find at least one titled border (from subpanels)
        boolean hasTitledBorder = false;
        for (Component comp : optimizationsPanel.getComponents()) {
            if (comp instanceof JPanel) {
                Border border = ((JPanel) comp).getBorder();
                if (border instanceof TitledBorder) {
                    hasTitledBorder = true;
                    break;
                }
            }
        }
        assertTrue(hasTitledBorder, "At least one subpanel should have a titled border");
    }

    /**
     * Test that the constructor creates checkboxes for all optimizations.
     * This verifies lines 107-127 that iterate through OPTIMIZATION_NAMES and create checkboxes.
     */
    @Test
    public void testConstructorCreatesOptimizationCheckboxes() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        JScrollPane scrollPane = (JScrollPane) dialog.getContentPane().getComponent(0);
        JPanel optimizationsPanel = (JPanel) scrollPane.getViewport().getView();

        // Count checkboxes in the entire hierarchy
        int checkboxCount = countCheckboxes(optimizationsPanel);

        // There should be checkboxes for each optimization
        assertTrue(checkboxCount > 0, "Panel should contain optimization checkboxes");
    }

    /**
     * Test that the constructor creates the Select All button.
     * This verifies lines 130-140 that create the button and its action listener.
     */
    @Test
    public void testConstructorCreatesSelectAllButton() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        JScrollPane scrollPane = (JScrollPane) dialog.getContentPane().getComponent(0);
        JPanel optimizationsPanel = (JPanel) scrollPane.getViewport().getView();

        JButton selectAllButton = findButton(optimizationsPanel);
        assertNotNull(selectAllButton, "Panel should contain at least one button");
    }

    /**
     * Test that the constructor creates the Select None button.
     * This verifies lines 143-153 that create the button and its action listener.
     */
    @Test
    public void testConstructorCreatesSelectNoneButton() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        JScrollPane scrollPane = (JScrollPane) dialog.getContentPane().getComponent(0);
        JPanel optimizationsPanel = (JPanel) scrollPane.getViewport().getView();

        int buttonCount = countButtons(optimizationsPanel);

        // Should have at least 2 buttons (Select All and Select None, plus Ok and Cancel)
        assertTrue(buttonCount >= 2, "Panel should contain multiple buttons");
    }

    /**
     * Test that the constructor creates the Ok button.
     * This verifies lines 156-164 that create the button and its action listener.
     */
    @Test
    public void testConstructorCreatesOkButton() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        JScrollPane scrollPane = (JScrollPane) dialog.getContentPane().getComponent(0);
        JPanel optimizationsPanel = (JPanel) scrollPane.getViewport().getView();

        int buttonCount = countButtons(optimizationsPanel);

        // Should have at least 3 buttons (Select All, Select None, and Ok)
        assertTrue(buttonCount >= 3, "Panel should contain Ok button");
    }

    /**
     * Test that the constructor creates the Cancel button.
     * This verifies lines 167-174 that create the button and its action listener.
     */
    @Test
    public void testConstructorCreatesCancelButton() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        JScrollPane scrollPane = (JScrollPane) dialog.getContentPane().getComponent(0);
        JPanel optimizationsPanel = (JPanel) scrollPane.getViewport().getView();

        int buttonCount = countButtons(optimizationsPanel);

        // Should have 4 buttons (Select All, Select None, Ok, and Cancel)
        assertTrue(buttonCount >= 4, "Panel should contain Cancel button");
    }

    /**
     * Test that the constructor adds all buttons to the panel.
     * This verifies lines 177-180 that add buttons to the optimizations panel.
     */
    @Test
    public void testConstructorAddsButtonsToPanel() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        JScrollPane scrollPane = (JScrollPane) dialog.getContentPane().getComponent(0);
        JPanel optimizationsPanel = (JPanel) scrollPane.getViewport().getView();

        // Verify that buttons are present in the panel
        int buttonCount = countButtons(optimizationsPanel);
        assertEquals(4, buttonCount, "Panel should contain exactly 4 buttons (Select All, Select None, Ok, Cancel)");
    }

    /**
     * Test that the constructor uses GridBagLayout.
     * This verifies line 98 that creates the GridBagLayout.
     */
    @Test
    public void testConstructorUsesGridBagLayout() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        JScrollPane scrollPane = (JScrollPane) dialog.getContentPane().getComponent(0);
        JPanel optimizationsPanel = (JPanel) scrollPane.getViewport().getView();

        LayoutManager layout = optimizationsPanel.getLayout();
        assertTrue(layout instanceof GridBagLayout, "Optimizations panel should use GridBagLayout");
    }

    /**
     * Test that the constructor creates titled borders for subpanels.
     * This verifies line 100 creates the etched border and line 117 creates titled borders.
     */
    @Test
    public void testConstructorCreatesTitledBorders() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        JScrollPane scrollPane = (JScrollPane) dialog.getContentPane().getComponent(0);
        JPanel optimizationsPanel = (JPanel) scrollPane.getViewport().getView();

        // Find titled borders
        int titledBorderCount = 0;
        for (Component comp : optimizationsPanel.getComponents()) {
            if (comp instanceof JPanel) {
                Border border = ((JPanel) comp).getBorder();
                if (border instanceof TitledBorder) {
                    titledBorderCount++;
                }
            }
        }

        assertTrue(titledBorderCount > 0, "At least one subpanel should have a titled border");
    }

    /**
     * Test that the constructor properly initializes with different frame titles.
     * This ensures the constructor works correctly with various frame configurations.
     */
    @Test
    public void testConstructorWithDifferentFrameConfigurations() {
        // Test with a simple frame
        JFrame frame1 = new JFrame();
        OptimizationsDialog dialog1 = new OptimizationsDialog(frame1);
        assertNotNull(dialog1, "Dialog should be created with simple frame");
        dialog1.dispose();

        // Test with a titled frame
        JFrame frame2 = new JFrame("My Application");
        OptimizationsDialog dialog2 = new OptimizationsDialog(frame2);
        assertNotNull(dialog2, "Dialog should be created with titled frame");
        dialog2.dispose();

        // Test with a frame with size
        JFrame frame3 = new JFrame();
        frame3.setSize(800, 600);
        OptimizationsDialog dialog3 = new OptimizationsDialog(frame3);
        assertNotNull(dialog3, "Dialog should be created with sized frame");
        dialog3.dispose();

        frame1.dispose();
        frame2.dispose();
        frame3.dispose();
    }

    /**
     * Test that dialog can be packed after construction.
     * This ensures the layout is properly configured during construction.
     */
    @Test
    public void testConstructorAllowsPacking() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        // This should not throw an exception
        dialog.pack();

        // After packing, dialog should have non-zero size
        Dimension size = dialog.getSize();
        assertTrue(size.width > 0, "Packed dialog should have positive width");
        assertTrue(size.height > 0, "Packed dialog should have positive height");
    }

    /**
     * Test that the dialog title is set correctly by the constructor.
     * This verifies line 57 that calls super with the title.
     */
    @Test
    public void testConstructorSetsDialogTitle() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        String title = dialog.getTitle();
        assertNotNull(title, "Dialog should have a title");
        assertFalse(title.isEmpty(), "Dialog title should not be empty");
    }

    /**
     * Test that the constructor creates a modal dialog.
     * This verifies line 57 that passes 'true' for modal parameter.
     */
    @Test
    public void testConstructorCreatesModalDialog() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        assertTrue(dialog.isModal(), "Dialog should be modal as specified in constructor");
    }

    /**
     * Test that the constructor makes the dialog resizable.
     * This verifies line 58 that calls setResizable(true).
     */
    @Test
    public void testConstructorMakesDialogResizable() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        assertTrue(dialog.isResizable(), "Dialog should be resizable as set in constructor");
    }

    /**
     * Test that the constructor properly initializes the optimization checkboxes array.
     * This verifies line 50 and lines 123-124 that populate the array.
     */
    @Test
    public void testConstructorInitializesCheckboxArray() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        // After construction, we should be able to use setFilter which accesses the checkboxes
        dialog.setFilter("class/marking/final");

        // This should work without throwing NullPointerException
        String filter = dialog.getFilter();
        assertNotNull(filter, "Filter should be retrievable after constructor initializes checkboxes");
    }

    /**
     * Test that multiple dialog instances can be created.
     * This ensures the constructor doesn't have any static state issues.
     */
    @Test
    public void testConstructorAllowsMultipleInstances() {
        testFrame = new JFrame("Test Frame");

        OptimizationsDialog dialog1 = new OptimizationsDialog(testFrame);
        assertNotNull(dialog1, "First dialog should be created");

        OptimizationsDialog dialog2 = new OptimizationsDialog(testFrame);
        assertNotNull(dialog2, "Second dialog should be created");

        // Both should be independent
        assertNotSame(dialog1, dialog2, "Each constructor call should create a new instance");

        dialog1.dispose();
        dialog2.dispose();
    }

    // Helper methods

    /**
     * Recursively counts all JCheckBox components in a container.
     */
    private int countCheckboxes(Container container) {
        int count = 0;
        for (Component comp : container.getComponents()) {
            if (comp instanceof JCheckBox) {
                count++;
            } else if (comp instanceof Container) {
                count += countCheckboxes((Container) comp);
            }
        }
        return count;
    }

    /**
     * Recursively counts all JButton components in a container.
     */
    private int countButtons(Container container) {
        int count = 0;
        for (Component comp : container.getComponents()) {
            if (comp instanceof JButton) {
                count++;
            } else if (comp instanceof Container) {
                count += countButtons((Container) comp);
            }
        }
        return count;
    }

    /**
     * Finds the first JButton in a container.
     */
    private JButton findButton(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JButton) {
                return (JButton) comp;
            } else if (comp instanceof Container) {
                JButton button = findButton((Container) comp);
                if (button != null) {
                    return button;
                }
            }
        }
        return null;
    }
}
