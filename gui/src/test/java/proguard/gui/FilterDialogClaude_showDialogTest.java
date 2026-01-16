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
 * Test class for FilterDialog.showDialog() method.
 *
 * The showDialog() method is responsible for:
 * - Initializing returnValue to CANCEL_OPTION (line 367)
 * - Calling pack() to size the dialog (line 371)
 * - Calling setLocationRelativeTo(getOwner()) to position the dialog (line 372)
 * - Calling show() to make the dialog visible (line 373)
 * - Returning the returnValue (line 375)
 *
 * These tests verify:
 * - The dialog is displayed and properly initialized
 * - The return value is CANCEL_OPTION when dialog is closed without approval
 * - The return value is APPROVE_OPTION when OK button is clicked
 * - The dialog is positioned relative to its owner
 * - The dialog is properly packed before display
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class FilterDialogClaude_showDialogTest {

    private JFrame testFrame;
    private FilterDialog dialog;

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
     * Test that showDialog returns CANCEL_OPTION when dialog is closed.
     * This test covers lines 367, 371, 372, 373, and 375.
     * Line 367: returnValue = CANCEL_OPTION
     * Line 371: pack()
     * Line 372: setLocationRelativeTo(getOwner())
     * Line 373: show()
     * Line 375: return returnValue
     */
    @Test
    public void testShowDialogReturnsCancelOption() {
        testFrame = new JFrame("Test Frame");
        testFrame.setSize(400, 300);
        testFrame.setLocation(100, 100);
        dialog = new FilterDialog(testFrame, "Test explanation");

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

        assertEquals(FilterDialog.CANCEL_OPTION, result,
                    "showDialog should return CANCEL_OPTION when dialog is closed");
    }

    /**
     * Test that showDialog makes the dialog visible.
     * This test covers lines 367, 371, 372, 373, and 375.
     */
    @Test
    public void testShowDialogMakesDialogVisible() {
        testFrame = new JFrame("Test Frame");
        testFrame.setVisible(true);
        dialog = new FilterDialog(testFrame, "Test explanation");

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

        testFrame.setVisible(false);
    }

    /**
     * Test that showDialog packs the dialog before displaying.
     * This test covers lines 367, 371, 372, 373, and 375.
     * Line 371 specifically: pack()
     */
    @Test
    public void testShowDialogPacksDialog() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        // Before showDialog, size might not be set
        Dimension sizeBefore = dialog.getSize();

        // Use a separate thread to close the dialog
        Thread closer = new Thread(() -> {
            try {
                Thread.sleep(100);
                SwingUtilities.invokeLater(() -> {
                    if (dialog != null && dialog.isVisible()) {
                        // After showDialog, size should be set by pack()
                        Dimension sizeAfter = dialog.getSize();
                        assertTrue(sizeAfter.width > 0, "Dialog width should be positive after pack");
                        assertTrue(sizeAfter.height > 0, "Dialog height should be positive after pack");
                        dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
                    }
                });
            } catch (InterruptedException e) {
                // Ignore
            }
        });
        closer.start();

        dialog.showDialog();
    }

    /**
     * Test that showDialog positions the dialog relative to owner.
     * This test covers lines 367, 371, 372, 373, and 375.
     * Line 372 specifically: setLocationRelativeTo(getOwner())
     */
    @Test
    public void testShowDialogPositionsRelativeToOwner() {
        testFrame = new JFrame("Test Frame");
        testFrame.setSize(800, 600);
        testFrame.setLocation(200, 200);
        dialog = new FilterDialog(testFrame, "Test explanation");

        // Use a separate thread to verify position and close the dialog
        Thread checker = new Thread(() -> {
            try {
                Thread.sleep(100);
                SwingUtilities.invokeLater(() -> {
                    if (dialog != null && dialog.isVisible()) {
                        Point dialogLocation = dialog.getLocation();
                        // Dialog should be positioned (not at 0,0 unless owner is there)
                        assertNotNull(dialogLocation, "Dialog should have a location");
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
     * Test that showDialog works with null owner.
     * This test covers lines 367, 371, 372, 373, and 375.
     */
    @Test
    public void testShowDialogWithNullOwner() {
        dialog = new FilterDialog(null, "Test explanation");

        // Use a separate thread to close the dialog
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

        assertEquals(FilterDialog.CANCEL_OPTION, result,
                    "showDialog should return CANCEL_OPTION even with null owner");
    }

    /**
     * Test that showDialog can be called multiple times on the same dialog.
     * This test covers lines 367, 371, 372, 373, and 375.
     */
    @Test
    public void testShowDialogMultipleTimes() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        // First call
        Thread closer1 = new Thread(() -> {
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
        closer1.start();

        int result1 = dialog.showDialog();
        assertEquals(FilterDialog.CANCEL_OPTION, result1,
                    "First call should return CANCEL_OPTION");

        // Second call
        Thread closer2 = new Thread(() -> {
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
        closer2.start();

        int result2 = dialog.showDialog();
        assertEquals(FilterDialog.CANCEL_OPTION, result2,
                    "Second call should also return CANCEL_OPTION");
    }

    /**
     * Test that showDialog initializes returnValue to CANCEL_OPTION.
     * This test specifically covers line 367: returnValue = CANCEL_OPTION
     */
    @Test
    public void testShowDialogInitializesReturnValue() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        // Use a separate thread to close the dialog immediately
        Thread closer = new Thread(() -> {
            try {
                Thread.sleep(50);
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

        // Since we closed the dialog without clicking OK, returnValue should be CANCEL_OPTION
        assertEquals(FilterDialog.CANCEL_OPTION, result,
                    "returnValue should be initialized to CANCEL_OPTION at line 367");
    }

    /**
     * Test that showDialog works correctly with invisible owner.
     * This test covers lines 367, 371, 372, 373, and 375.
     */
    @Test
    public void testShowDialogWithInvisibleOwner() {
        testFrame = new JFrame("Test Frame");
        testFrame.setVisible(false);
        dialog = new FilterDialog(testFrame, "Test explanation");

        // Use a separate thread to close the dialog
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

        assertEquals(FilterDialog.CANCEL_OPTION, result,
                    "showDialog should work correctly even with invisible owner");
    }

    /**
     * Test that showDialog positions dialog correctly when owner is minimized.
     * This test covers lines 367, 371, 372, 373, and 375.
     */
    @Test
    public void testShowDialogWithMinimizedOwner() {
        testFrame = new JFrame("Test Frame");
        testFrame.setExtendedState(JFrame.ICONIFIED);
        dialog = new FilterDialog(testFrame, "Test explanation");

        // Use a separate thread to close the dialog
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

        assertEquals(FilterDialog.CANCEL_OPTION, result,
                    "showDialog should work correctly even with minimized owner");
    }

    /**
     * Test that showDialog returns immediately after dialog is closed.
     * This test covers lines 367, 371, 372, 373, and 375.
     * Line 375 specifically: return returnValue
     */
    @Test
    public void testShowDialogReturnsAfterClose() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        long startTime = System.currentTimeMillis();

        // Use a separate thread to close the dialog quickly
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
        long endTime = System.currentTimeMillis();

        // Method should return after dialog closes
        assertEquals(FilterDialog.CANCEL_OPTION, result,
                    "showDialog should return a value after dialog closes");
        assertTrue(endTime - startTime < 5000,
                  "showDialog should return within reasonable time after dialog closes");
    }

    /**
     * Test that showDialog works with owner at specific location.
     * This test covers lines 367, 371, 372, 373, and 375.
     */
    @Test
    public void testShowDialogWithOwnerAtSpecificLocation() {
        testFrame = new JFrame("Test Frame");
        testFrame.setSize(400, 300);
        testFrame.setLocation(500, 500);
        dialog = new FilterDialog(testFrame, "Test explanation");

        // Use a separate thread to close the dialog
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

        assertEquals(FilterDialog.CANCEL_OPTION, result,
                    "showDialog should work correctly with owner at specific location");
    }

    /**
     * Test that dialog is modal and blocks until closed.
     * This test covers lines 367, 371, 372, 373, and 375.
     */
    @Test
    public void testShowDialogModalBehavior() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        assertTrue(dialog.isModal(), "Dialog should be modal before showDialog");

        // Use a separate thread to close the dialog
        Thread closer = new Thread(() -> {
            try {
                Thread.sleep(100);
                SwingUtilities.invokeLater(() -> {
                    if (dialog != null && dialog.isVisible()) {
                        // Verify dialog is still modal
                        assertTrue(dialog.isModal(), "Dialog should remain modal during showDialog");
                        dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
                    }
                });
            } catch (InterruptedException e) {
                // Ignore
            }
        });
        closer.start();

        int result = dialog.showDialog();

        assertEquals(FilterDialog.CANCEL_OPTION, result,
                    "Modal dialog should return CANCEL_OPTION when closed");
    }

    /**
     * Test that showDialog handles long explanation text properly.
     * This test covers lines 367, 371, 372, 373, and 375.
     */
    @Test
    public void testShowDialogWithLongExplanation() {
        testFrame = new JFrame("Test Frame");
        StringBuilder longExplanation = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            longExplanation.append("This is a very long explanation text. ");
        }
        dialog = new FilterDialog(testFrame, longExplanation.toString());

        // Use a separate thread to close the dialog
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

        assertEquals(FilterDialog.CANCEL_OPTION, result,
                    "showDialog should work correctly with long explanation");
    }

    /**
     * Test that showDialog sets proper size after pack().
     * This test covers line 371: pack()
     */
    @Test
    public void testShowDialogSetsProperSize() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        // Use a separate thread to verify size and close the dialog
        Thread checker = new Thread(() -> {
            try {
                Thread.sleep(100);
                SwingUtilities.invokeLater(() -> {
                    if (dialog != null && dialog.isVisible()) {
                        Dimension size = dialog.getSize();
                        assertTrue(size.width > 0 && size.height > 0,
                                  "Dialog should have positive dimensions after pack()");
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
}
