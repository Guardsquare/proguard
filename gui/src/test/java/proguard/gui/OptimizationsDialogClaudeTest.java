package proguard.gui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Test class for OptimizationsDialog.
 *
 * This class tests all public methods of OptimizationsDialog including:
 * - Constructor
 * - setFilter method
 * - getFilter method
 * - showDialog method
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class OptimizationsDialogClaudeTest {

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

    // Constructor tests

    /**
     * Test that the constructor creates a dialog with valid parameters.
     */
    @Test
    public void testConstructorWithValidParameters() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        assertNotNull(dialog, "Dialog should be created successfully");
        assertTrue(dialog.isModal(), "Dialog should be modal");
        assertTrue(dialog.isResizable(), "Dialog should be resizable");
        assertEquals(testFrame, dialog.getOwner(), "Dialog owner should be the test frame");
    }

    /**
     * Test that the constructor creates a dialog with null owner.
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
     * Test that the dialog is initially not visible after construction.
     */
    @Test
    public void testConstructorDialogNotInitiallyVisible() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        assertFalse(dialog.isVisible(), "Dialog should not be visible immediately after construction");
    }

    /**
     * Test that the dialog has a content pane after construction.
     */
    @Test
    public void testConstructorInitializesContentPane() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        assertNotNull(dialog.getContentPane(), "Dialog should have a content pane");
        assertTrue(dialog.getContentPane().getComponentCount() > 0,
                   "Dialog content pane should contain components");
    }

    /**
     * Test that the dialog has a scroll pane in its content.
     */
    @Test
    public void testConstructorCreatesScrollPane() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        Component component = dialog.getContentPane().getComponent(0);
        assertTrue(component instanceof JScrollPane,
                   "Dialog content pane should contain a JScrollPane");
    }

    // setFilter tests

    /**
     * Test setFilter with null value.
     */
    @Test
    public void testSetFilterWithNull() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        dialog.setFilter(null);
        // No exception should be thrown
        String filter = dialog.getFilter();
        assertNotNull(filter, "getFilter should return a non-null value after setFilter(null)");
    }

    /**
     * Test setFilter with empty string.
     */
    @Test
    public void testSetFilterWithEmptyString() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        dialog.setFilter("");
        // No exception should be thrown
        String filter = dialog.getFilter();
        assertNotNull(filter, "getFilter should return a non-null value after setFilter(\"\")");
    }

    /**
     * Test setFilter with a single optimization name.
     */
    @Test
    public void testSetFilterWithSingleOptimization() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        // Use a specific optimization pattern
        dialog.setFilter("class/marking/final");
        String filter = dialog.getFilter();
        assertNotNull(filter, "getFilter should return a non-null value");
    }

    /**
     * Test setFilter with multiple optimization names.
     */
    @Test
    public void testSetFilterWithMultipleOptimizations() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        // Use multiple optimization patterns
        dialog.setFilter("class/marking/final,class/merging/vertical");
        String filter = dialog.getFilter();
        assertNotNull(filter, "getFilter should return a non-null value");
    }

    /**
     * Test setFilter with wildcard pattern.
     */
    @Test
    public void testSetFilterWithWildcard() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        // Use wildcard pattern
        dialog.setFilter("class/*");
        String filter = dialog.getFilter();
        assertNotNull(filter, "getFilter should return a non-null value");
    }

    /**
     * Test setFilter with negation pattern.
     */
    @Test
    public void testSetFilterWithNegation() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        // Use negation pattern
        dialog.setFilter("!class/marking/final");
        String filter = dialog.getFilter();
        assertNotNull(filter, "getFilter should return a non-null value");
    }

    /**
     * Test setFilter with all optimizations enabled.
     */
    @Test
    public void testSetFilterWithAllOptimizations() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        // Use wildcard to match all
        dialog.setFilter("*");
        String filter = dialog.getFilter();
        assertNotNull(filter, "getFilter should return a non-null value");
    }

    /**
     * Test setFilter followed by another setFilter call.
     */
    @Test
    public void testSetFilterMultipleTimes() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        dialog.setFilter("class/marking/final");
        dialog.setFilter("class/merging/vertical");
        String filter = dialog.getFilter();
        assertNotNull(filter, "getFilter should return a non-null value after multiple setFilter calls");
    }

    /**
     * Test setFilter with complex pattern.
     */
    @Test
    public void testSetFilterWithComplexPattern() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        // Use a complex pattern with multiple components
        dialog.setFilter("class/*,!class/marking/final,field/propagation/value");
        String filter = dialog.getFilter();
        assertNotNull(filter, "getFilter should return a non-null value");
    }

    // getFilter tests

    /**
     * Test getFilter returns non-null value by default.
     */
    @Test
    public void testGetFilterReturnsNonNullByDefault() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        String filter = dialog.getFilter();
        assertNotNull(filter, "getFilter should return a non-null value by default");
    }

    /**
     * Test getFilter after setFilter with null.
     */
    @Test
    public void testGetFilterAfterSetFilterNull() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        dialog.setFilter(null);
        String filter = dialog.getFilter();
        assertNotNull(filter, "getFilter should return a non-null value after setFilter(null)");
    }

    /**
     * Test getFilter after setFilter with empty string.
     */
    @Test
    public void testGetFilterAfterSetFilterEmpty() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        dialog.setFilter("");
        String filter = dialog.getFilter();
        assertNotNull(filter, "getFilter should return a non-null value after setFilter(\"\")");
    }

    /**
     * Test getFilter returns a String.
     */
    @Test
    public void testGetFilterReturnsString() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        Object filter = dialog.getFilter();
        assertTrue(filter instanceof String, "getFilter should return a String instance");
    }

    /**
     * Test getFilter can be called multiple times.
     */
    @Test
    public void testGetFilterMultipleCalls() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        dialog.setFilter("class/marking/final");
        String filter1 = dialog.getFilter();
        String filter2 = dialog.getFilter();

        assertNotNull(filter1, "First getFilter call should return non-null");
        assertNotNull(filter2, "Second getFilter call should return non-null");
    }

    // showDialog tests

    /**
     * Test showDialog returns CANCEL_OPTION by default when dialog is closed.
     * Note: This test programmatically closes the dialog to avoid blocking.
     */
    @Test
    public void testShowDialogReturnsCancelOption() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        // Use a separate thread to close the dialog after a short delay
        Thread closer = new Thread(() -> {
            try {
                Thread.sleep(100);
                SwingUtilities.invokeLater(() -> {
                    if (dialog != null && dialog.isVisible()) {
                        dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
                    }
                });
            } catch (InterruptedException e) {
                // Ignore
            }
        });
        closer.start();

        int result = dialog.showDialog();

        assertEquals(OptimizationsDialog.CANCEL_OPTION, result,
                    "showDialog should return CANCEL_OPTION when dialog is closed");
    }

    /**
     * Test that showDialog makes the dialog visible.
     * Note: This test programmatically closes the dialog to avoid blocking.
     */
    @Test
    public void testShowDialogMakesDialogVisible() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        assertFalse(dialog.isVisible(), "Dialog should not be visible before showDialog");

        // Use a separate thread to verify visibility and close the dialog
        Thread checker = new Thread(() -> {
            try {
                Thread.sleep(100);
                SwingUtilities.invokeLater(() -> {
                    if (dialog != null) {
                        assertTrue(dialog.isVisible(), "Dialog should be visible during showDialog");
                        dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
                    }
                });
            } catch (InterruptedException e) {
                // Ignore
            }
        });
        checker.start();

        dialog.showDialog();
    }

    /**
     * Test that APPROVE_OPTION constant has expected value.
     */
    @Test
    public void testApproveOptionConstant() {
        assertEquals(0, OptimizationsDialog.APPROVE_OPTION,
                    "APPROVE_OPTION should have value 0");
    }

    /**
     * Test that CANCEL_OPTION constant has expected value.
     */
    @Test
    public void testCancelOptionConstant() {
        assertEquals(1, OptimizationsDialog.CANCEL_OPTION,
                    "CANCEL_OPTION should have value 1");
    }

    /**
     * Test showDialog can be called after setting filter.
     */
    @Test
    public void testShowDialogAfterSetFilter() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        dialog.setFilter("class/marking/final");

        // Use a separate thread to close the dialog after a short delay
        Thread closer = new Thread(() -> {
            try {
                Thread.sleep(100);
                SwingUtilities.invokeLater(() -> {
                    if (dialog != null && dialog.isVisible()) {
                        dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
                    }
                });
            } catch (InterruptedException e) {
                // Ignore
            }
        });
        closer.start();

        int result = dialog.showDialog();

        assertEquals(OptimizationsDialog.CANCEL_OPTION, result,
                    "showDialog should return CANCEL_OPTION when dialog is closed");
    }

    /**
     * Test dialog not visible after showDialog returns.
     */
    @Test
    public void testDialogNotVisibleAfterShowDialog() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        // Use a separate thread to close the dialog after a short delay
        Thread closer = new Thread(() -> {
            try {
                Thread.sleep(100);
                SwingUtilities.invokeLater(() -> {
                    if (dialog != null && dialog.isVisible()) {
                        dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
                    }
                });
            } catch (InterruptedException e) {
                // Ignore
            }
        });
        closer.start();

        dialog.showDialog();

        // Wait a bit for the dialog to be fully closed
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            // Ignore
        }

        assertFalse(dialog.isVisible(), "Dialog should not be visible after showDialog returns");
    }

    // Integration tests

    /**
     * Test complete workflow: create, set filter, get filter, show dialog.
     */
    @Test
    public void testCompleteWorkflow() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        // Set a filter
        dialog.setFilter("class/marking/final,field/propagation/value");

        // Get the filter
        String filter = dialog.getFilter();
        assertNotNull(filter, "getFilter should return non-null value");

        // Show the dialog (close it programmatically)
        Thread closer = new Thread(() -> {
            try {
                Thread.sleep(100);
                SwingUtilities.invokeLater(() -> {
                    if (dialog != null && dialog.isVisible()) {
                        dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
                    }
                });
            } catch (InterruptedException e) {
                // Ignore
            }
        });
        closer.start();

        int result = dialog.showDialog();
        assertEquals(OptimizationsDialog.CANCEL_OPTION, result,
                    "showDialog should return CANCEL_OPTION when dialog is closed");
    }

    /**
     * Test multiple set/get filter calls.
     */
    @Test
    public void testMultipleSetGetCalls() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        // Set filter multiple times and verify getFilter works each time
        dialog.setFilter("class/*");
        String filter1 = dialog.getFilter();
        assertNotNull(filter1, "getFilter should return non-null after first setFilter");

        dialog.setFilter("field/*");
        String filter2 = dialog.getFilter();
        assertNotNull(filter2, "getFilter should return non-null after second setFilter");

        dialog.setFilter("method/*");
        String filter3 = dialog.getFilter();
        assertNotNull(filter3, "getFilter should return non-null after third setFilter");
    }

    /**
     * Test that dialog can be disposed and recreated.
     */
    @Test
    public void testDialogDisposeAndRecreate() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        dialog.setFilter("class/marking/final");
        dialog.dispose();

        // Create a new dialog
        dialog = new OptimizationsDialog(testFrame);
        assertNotNull(dialog, "New dialog should be created after disposing previous one");

        // Set filter on new dialog should work
        dialog.setFilter("field/propagation/value");
        String filter = dialog.getFilter();
        assertNotNull(filter, "getFilter should work on new dialog instance");
    }

    /**
     * Test setFilter with patterns that don't match any optimizations.
     */
    @Test
    public void testSetFilterWithNonMatchingPattern() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        dialog.setFilter("nonexistent/optimization/name");
        String filter = dialog.getFilter();
        assertNotNull(filter, "getFilter should return non-null even with non-matching pattern");
    }

    /**
     * Test getFilter without any prior setFilter call.
     */
    @Test
    public void testGetFilterWithoutSetFilter() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        // Don't call setFilter, just get the filter
        String filter = dialog.getFilter();
        assertNotNull(filter, "getFilter should return non-null even without prior setFilter");
    }

    /**
     * Test that dialog owner is correctly set.
     */
    @Test
    public void testDialogOwnerCorrectlySet() {
        testFrame = new JFrame("Test Owner Frame");
        dialog = new OptimizationsDialog(testFrame);

        assertEquals(testFrame, dialog.getOwner(),
                    "Dialog owner should match the frame passed to constructor");
    }

    /**
     * Test setFilter with only negations.
     */
    @Test
    public void testSetFilterWithOnlyNegations() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        dialog.setFilter("!class/marking/final,!field/propagation/value");
        String filter = dialog.getFilter();
        assertNotNull(filter, "getFilter should return non-null with negation-only pattern");
    }

    /**
     * Test setFilter with mixed patterns.
     */
    @Test
    public void testSetFilterWithMixedPatterns() {
        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);

        dialog.setFilter("class/*,!class/marking/final,field/propagation/value");
        String filter = dialog.getFilter();
        assertNotNull(filter, "getFilter should return non-null with mixed pattern");
    }
}
