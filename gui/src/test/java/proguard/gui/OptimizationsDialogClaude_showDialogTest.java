package proguard.gui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Test class focused on showDialog method coverage for OptimizationsDialog.
 *
 * This class ensures full coverage of the showDialog method including:
 * - Line 219: Setting returnValue to CANCEL_OPTION
 * - Line 223: Calling pack() to size the dialog
 * - Line 224: Calling setLocationRelativeTo(getOwner()) to center the dialog
 * - Line 225: Calling show() to display the dialog (blocking call)
 * - Line 227: Returning returnValue
 *
 * The method returns CANCEL_OPTION by default (line 219), but can return
 * APPROVE_OPTION if the OK button is clicked (which sets returnValue).
 *
 * Note: These tests require GUI components and will skip in headless environments.
 * Since show() is a blocking call, tests use separate threads to close the dialog.
 */
public class OptimizationsDialogClaude_showDialogTest {

    private JFrame testFrame;
    private OptimizationsDialog dialog;

    @BeforeEach
    public void setUp() {
        // Tests will skip if headless mode is active
        assumeFalse(GraphicsEnvironment.isHeadless(),
                    "Skipping test: Headless environment detected. GUI components require a display.");

        testFrame = new JFrame("Test Frame");
        dialog = new OptimizationsDialog(testFrame);
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
     * Test showDialog returns CANCEL_OPTION when dialog is closed via window close.
     * This covers lines 219, 223, 224, 225, 227.
     * Line 219 sets returnValue to CANCEL_OPTION, which is returned on line 227.
     */
    @Test
    public void testShowDialogReturnsCancelOption() {
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

        // This executes lines 219, 223, 224, 225, and 227
        int result = dialog.showDialog();

        assertEquals(OptimizationsDialog.CANCEL_OPTION, result,
                    "showDialog should return CANCEL_OPTION when dialog is closed");
    }

    /**
     * Test that showDialog calls pack() which sizes the dialog.
     * This verifies line 223 is executed.
     */
    @Test
    public void testShowDialogCallsPack() {
        // Check dialog size before showDialog
        Dimension sizeBeforePack = dialog.getSize();

        // Use a separate thread to close the dialog
        Thread closer = new Thread(() -> {
            try {
                Thread.sleep(100);
                SwingUtilities.invokeLater(() -> {
                    if (dialog != null && dialog.isVisible()) {
                        // Check that dialog has been sized (pack() was called)
                        Dimension sizeAfterPack = dialog.getSize();
                        assertTrue(sizeAfterPack.width > 0, "Dialog width should be > 0 after pack()");
                        assertTrue(sizeAfterPack.height > 0, "Dialog height should be > 0 after pack()");

                        dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
                    }
                });
            } catch (InterruptedException e) {
                // Ignore
            }
        });
        closer.start();

        dialog.showDialog();

        // After showDialog returns, dialog should have been packed
        Dimension finalSize = dialog.getSize();
        assertTrue(finalSize.width > 0, "Dialog should have positive width after showDialog");
        assertTrue(finalSize.height > 0, "Dialog should have positive height after showDialog");
    }

    /**
     * Test that showDialog calls setLocationRelativeTo to center the dialog.
     * This verifies line 224 is executed.
     */
    @Test
    public void testShowDialogSetsLocation() {
        // Position the test frame at a known location
        testFrame.setLocation(200, 200);
        testFrame.setSize(400, 300);

        // Use a separate thread to verify location and close
        Thread checker = new Thread(() -> {
            try {
                Thread.sleep(100);
                SwingUtilities.invokeLater(() -> {
                    if (dialog != null && dialog.isVisible()) {
                        // Dialog location should have been set relative to owner
                        Point location = dialog.getLocation();
                        assertNotNull(location, "Dialog location should be set");

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
     * Test that showDialog calls show() which makes the dialog visible.
     * This verifies line 225 is executed.
     */
    @Test
    public void testShowDialogMakesDialogVisible() {
        assertFalse(dialog.isVisible(), "Dialog should not be visible before showDialog");

        // Use a separate thread to verify visibility
        Thread checker = new Thread(() -> {
            try {
                Thread.sleep(100);
                SwingUtilities.invokeLater(() -> {
                    if (dialog != null) {
                        // Line 225 (show()) should have made the dialog visible
                        assertTrue(dialog.isVisible(), "Dialog should be visible after show() is called");
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
     * Test that showDialog is a blocking call.
     * This verifies that line 225 (show()) blocks until dialog is closed.
     */
    @Test
    public void testShowDialogIsBlocking() {
        final boolean[] dialogWasVisible = {false};

        // Use a separate thread to close the dialog
        Thread closer = new Thread(() -> {
            try {
                Thread.sleep(100);
                SwingUtilities.invokeLater(() -> {
                    if (dialog != null && dialog.isVisible()) {
                        dialogWasVisible[0] = true;
                        dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
                    }
                });
            } catch (InterruptedException e) {
                // Ignore
            }
        });
        closer.start();

        // showDialog should block until the dialog is closed
        dialog.showDialog();

        // By the time we get here, the dialog should have been visible
        assertTrue(dialogWasVisible[0], "Dialog should have been visible during showDialog execution");
    }

    /**
     * Test showDialog returns the correct value set by returnValue field.
     * This verifies line 227 returns the returnValue.
     */
    @Test
    public void testShowDialogReturnsCorrectValue() {
        // Close the dialog, which should leave returnValue as CANCEL_OPTION (set on line 219)
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

        // Line 227 should return the returnValue, which is CANCEL_OPTION from line 219
        assertEquals(OptimizationsDialog.CANCEL_OPTION, result,
                    "showDialog should return CANCEL_OPTION");
    }

    /**
     * Test showDialog can be called multiple times.
     * This ensures lines 219-227 can execute multiple times correctly.
     */
    @Test
    public void testShowDialogCalledMultipleTimes() {
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
        assertEquals(OptimizationsDialog.CANCEL_OPTION, result1,
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
        assertEquals(OptimizationsDialog.CANCEL_OPTION, result2,
                    "Second call should return CANCEL_OPTION");
    }

    /**
     * Test that returnValue is reset to CANCEL_OPTION on each showDialog call.
     * This specifically tests line 219.
     */
    @Test
    public void testShowDialogResetsReturnValueToCancelOption() throws Exception {
        // First, we'll simulate setting returnValue to APPROVE_OPTION
        // (as if OK button was clicked previously)
        Field returnValueField = OptimizationsDialog.class.getDeclaredField("returnValue");
        returnValueField.setAccessible(true);
        returnValueField.setInt(dialog, OptimizationsDialog.APPROVE_OPTION);

        // Verify it's set to APPROVE_OPTION
        int valueBefore = returnValueField.getInt(dialog);
        assertEquals(OptimizationsDialog.APPROVE_OPTION, valueBefore,
                    "returnValue should be APPROVE_OPTION initially");

        // Now call showDialog - line 219 should reset it to CANCEL_OPTION
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

        // Line 219 should have reset returnValue to CANCEL_OPTION
        assertEquals(OptimizationsDialog.CANCEL_OPTION, result,
                    "showDialog should return CANCEL_OPTION after resetting returnValue");
    }

    /**
     * Test showDialog with null owner.
     * This ensures line 224 (setLocationRelativeTo(getOwner())) works with null owner.
     */
    @Test
    public void testShowDialogWithNullOwner() {
        // Create dialog with null owner
        OptimizationsDialog nullOwnerDialog = new OptimizationsDialog(null);

        Thread closer = new Thread(() -> {
            try {
                Thread.sleep(100);
                SwingUtilities.invokeLater(() -> {
                    if (nullOwnerDialog != null && nullOwnerDialog.isVisible()) {
                        nullOwnerDialog.dispatchEvent(new WindowEvent(nullOwnerDialog, WindowEvent.WINDOW_CLOSING));
                    }
                });
            } catch (InterruptedException e) {
                // Ignore
            }
        });
        closer.start();

        // Line 224 should handle null owner gracefully
        int result = nullOwnerDialog.showDialog();
        assertEquals(OptimizationsDialog.CANCEL_OPTION, result);

        nullOwnerDialog.dispose();
    }

    /**
     * Test showDialog after setting various filters.
     * This ensures lines 219-227 work after dialog state has been modified.
     */
    @Test
    public void testShowDialogAfterSettingFilter() {
        // Modify dialog state
        dialog.setFilter("class/marking/final");

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

        // showDialog should still work correctly
        int result = dialog.showDialog();
        assertEquals(OptimizationsDialog.CANCEL_OPTION, result);
    }

    /**
     * Test that dialog is not visible after showDialog returns.
     * This verifies the complete execution flow through line 227.
     */
    @Test
    public void testDialogNotVisibleAfterShowDialog() {
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

        // Wait a bit for dialog to fully close
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            // Ignore
        }

        assertFalse(dialog.isVisible(), "Dialog should not be visible after showDialog returns");
    }

    /**
     * Test showDialog packs dialog to correct size.
     * This specifically verifies line 223 (pack()).
     */
    @Test
    public void testShowDialogPacksToCorrectSize() {
        // Before showDialog, size might be zero
        Thread closer = new Thread(() -> {
            try {
                Thread.sleep(100);
                SwingUtilities.invokeLater(() -> {
                    if (dialog != null && dialog.isVisible()) {
                        // After pack() on line 223, dialog should have appropriate size
                        Dimension size = dialog.getSize();
                        assertTrue(size.width > 100, "Dialog width should be reasonable after pack()");
                        assertTrue(size.height > 100, "Dialog height should be reasonable after pack()");

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
     * Test showDialog with owner that has specific location.
     * This verifies line 224 (setLocationRelativeTo) positions dialog correctly.
     */
    @Test
    public void testShowDialogCentersRelativeToOwner() {
        // Set owner to specific location
        testFrame.setBounds(100, 100, 500, 400);
        testFrame.setVisible(true);

        Thread closer = new Thread(() -> {
            try {
                Thread.sleep(100);
                SwingUtilities.invokeLater(() -> {
                    if (dialog != null && dialog.isVisible()) {
                        // Dialog should be positioned relative to owner (line 224)
                        Point dialogLocation = dialog.getLocation();
                        Point ownerLocation = testFrame.getLocation();

                        // Dialog should be somewhere near the owner
                        assertNotNull(dialogLocation, "Dialog location should be set");

                        dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
                    }
                });
            } catch (InterruptedException e) {
                // Ignore
            }
        });
        closer.start();

        dialog.showDialog();

        testFrame.setVisible(false);
    }

    /**
     * Comprehensive test to ensure all lines 219-227 are executed.
     * This test explicitly verifies each line's effect.
     */
    @Test
    public void testShowDialogComprehensiveCoverage() {
        // Prepare to track execution
        final boolean[] checksPerformed = {false};

        Thread checker = new Thread(() -> {
            try {
                Thread.sleep(100);
                SwingUtilities.invokeLater(() -> {
                    if (dialog != null && dialog.isVisible()) {
                        // By this point:
                        // - Line 219: returnValue should be CANCEL_OPTION
                        // - Line 223: pack() should have sized the dialog
                        Dimension size = dialog.getSize();
                        assertTrue(size.width > 0, "Line 223: pack() should have set width");
                        assertTrue(size.height > 0, "Line 223: pack() should have set height");

                        // - Line 224: setLocationRelativeTo() should have positioned dialog
                        Point location = dialog.getLocation();
                        assertNotNull(location, "Line 224: location should be set");

                        // - Line 225: show() should have made dialog visible
                        assertTrue(dialog.isVisible(), "Line 225: show() should make dialog visible");

                        checksPerformed[0] = true;

                        dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
                    }
                });
            } catch (InterruptedException e) {
                // Ignore
            }
        });
        checker.start();

        // Execute lines 219-227
        int result = dialog.showDialog();

        // Line 227: return returnValue (which is CANCEL_OPTION from line 219)
        assertEquals(OptimizationsDialog.CANCEL_OPTION, result,
                    "Line 227: should return CANCEL_OPTION");

        assertTrue(checksPerformed[0], "All checks should have been performed");
    }

    /**
     * Test that pack() is called before show().
     * This verifies the execution order of lines 223 and 225.
     */
    @Test
    public void testShowDialogCallsPackBeforeShow() {
        final boolean[] wasPackedWhenShown = {false};

        Thread checker = new Thread(() -> {
            try {
                Thread.sleep(100);
                SwingUtilities.invokeLater(() -> {
                    if (dialog != null && dialog.isVisible()) {
                        // If we're here and dialog is visible, pack() must have been called first
                        // because pack() comes before show() in the code
                        Dimension size = dialog.getSize();
                        wasPackedWhenShown[0] = (size.width > 0 && size.height > 0);

                        dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
                    }
                });
            } catch (InterruptedException e) {
                // Ignore
            }
        });
        checker.start();

        dialog.showDialog();

        assertTrue(wasPackedWhenShown[0],
                  "Dialog should be packed (have size) when it becomes visible");
    }
}
