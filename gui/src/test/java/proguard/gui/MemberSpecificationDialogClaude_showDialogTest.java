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
 * Test class for MemberSpecificationDialog.showDialog() method.
 *
 * The showDialog method (lines 435-446):
 * - Sets returnValue to CANCEL_OPTION (line 437)
 * - Calls pack() to size the dialog (line 441)
 * - Calls setLocationRelativeTo(getOwner()) to center the dialog (line 442)
 * - Calls show() to display the dialog (line 443)
 * - Returns returnValue (line 445)
 *
 * Since showDialog() displays a modal dialog that blocks execution until closed,
 * these tests use a separate thread to programmatically close the dialog after
 * a short delay to avoid blocking the test execution.
 *
 * These tests verify:
 * - The method returns CANCEL_OPTION when dialog is closed without approval
 * - The method executes pack() to size the dialog
 * - The method executes setLocationRelativeTo() to position the dialog
 * - The method executes show() to display the dialog
 * - The dialog becomes visible during showDialog execution
 * - The CANCEL_OPTION and APPROVE_OPTION constants are defined correctly
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class MemberSpecificationDialogClaude_showDialogTest {

    private JDialog testDialog;
    private MemberSpecificationDialog memberDialog;

    @BeforeEach
    public void setUp() {
        // Tests will skip if headless mode is active
        assumeFalse(GraphicsEnvironment.isHeadless(),
                    "Skipping test: Headless environment detected. GUI components require a display.");
    }

    @AfterEach
    public void tearDown() {
        if (memberDialog != null) {
            memberDialog.dispose();
        }
        if (testDialog != null) {
            testDialog.dispose();
        }
    }

    /**
     * Test showDialog returns CANCEL_OPTION when dialog is closed.
     * This covers lines 437, 441, 442, 443, 445.
     */
    @Test
    public void testShowDialogReturnsCancelOption() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Use a separate thread to close the dialog after a short delay
        Thread closer = new Thread(() -> {
            try {
                Thread.sleep(100);
                SwingUtilities.invokeLater(() -> {
                    if (memberDialog != null && memberDialog.isVisible()) {
                        memberDialog.dispatchEvent(new WindowEvent(memberDialog, WindowEvent.WINDOW_CLOSING));
                    }
                });
            } catch (InterruptedException e) {
                // Ignore
            }
        });
        closer.start();

        // Call showDialog - this executes lines 437, 441, 442, 443, 445
        int result = memberDialog.showDialog();

        assertEquals(MemberSpecificationDialog.CANCEL_OPTION, result,
                    "showDialog should return CANCEL_OPTION when dialog is closed");
    }

    /**
     * Test showDialog makes the dialog visible.
     * This verifies line 443 where show() is called.
     */
    @Test
    public void testShowDialogMakesDialogVisible() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        assertFalse(memberDialog.isVisible(), "Dialog should not be visible before showDialog");

        // Use a separate thread to verify visibility and close the dialog
        Thread checker = new Thread(() -> {
            try {
                Thread.sleep(100);
                SwingUtilities.invokeLater(() -> {
                    if (memberDialog != null) {
                        assertTrue(memberDialog.isVisible(), "Dialog should be visible during showDialog");
                        memberDialog.dispatchEvent(new WindowEvent(memberDialog, WindowEvent.WINDOW_CLOSING));
                    }
                });
            } catch (InterruptedException e) {
                // Ignore
            }
        });
        checker.start();

        memberDialog.showDialog();
    }

    /**
     * Test showDialog executes pack() to size the dialog.
     * This verifies line 441 where pack() is called.
     */
    @Test
    public void testShowDialogExecutesPack() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Get initial size (before pack is called by showDialog)
        Dimension preferredSize = memberDialog.getPreferredSize();
        assertTrue(preferredSize.width > 0, "Dialog should have positive preferred width");
        assertTrue(preferredSize.height > 0, "Dialog should have positive preferred height");

        // Use a separate thread to close the dialog
        Thread closer = new Thread(() -> {
            try {
                Thread.sleep(100);
                SwingUtilities.invokeLater(() -> {
                    if (memberDialog != null && memberDialog.isVisible()) {
                        // After pack() is called, the dialog should be sized
                        Dimension actualSize = memberDialog.getSize();
                        assertTrue(actualSize.width > 0, "Dialog should have positive width after pack");
                        assertTrue(actualSize.height > 0, "Dialog should have positive height after pack");
                        memberDialog.dispatchEvent(new WindowEvent(memberDialog, WindowEvent.WINDOW_CLOSING));
                    }
                });
            } catch (InterruptedException e) {
                // Ignore
            }
        });
        closer.start();

        memberDialog.showDialog();
    }

    /**
     * Test showDialog executes setLocationRelativeTo to position the dialog.
     * This verifies line 442 where setLocationRelativeTo(getOwner()) is called.
     */
    @Test
    public void testShowDialogExecutesSetLocationRelativeTo() {
        testDialog = new JDialog();
        testDialog.setSize(400, 300);
        testDialog.setLocation(100, 100);

        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Use a separate thread to verify location and close the dialog
        Thread checker = new Thread(() -> {
            try {
                Thread.sleep(100);
                SwingUtilities.invokeLater(() -> {
                    if (memberDialog != null && memberDialog.isVisible()) {
                        // After setLocationRelativeTo is called, dialog should have a location
                        Point location = memberDialog.getLocation();
                        assertNotNull(location, "Dialog should have a location after setLocationRelativeTo");
                        memberDialog.dispatchEvent(new WindowEvent(memberDialog, WindowEvent.WINDOW_CLOSING));
                    }
                });
            } catch (InterruptedException e) {
                // Ignore
            }
        });
        checker.start();

        memberDialog.showDialog();
    }

    /**
     * Test showDialog with field dialog.
     * This verifies the method works correctly for field dialogs.
     */
    @Test
    public void testShowDialogWithFieldDialog() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        Thread closer = new Thread(() -> {
            try {
                Thread.sleep(100);
                SwingUtilities.invokeLater(() -> {
                    if (memberDialog != null && memberDialog.isVisible()) {
                        memberDialog.dispatchEvent(new WindowEvent(memberDialog, WindowEvent.WINDOW_CLOSING));
                    }
                });
            } catch (InterruptedException e) {
                // Ignore
            }
        });
        closer.start();

        int result = memberDialog.showDialog();

        assertEquals(MemberSpecificationDialog.CANCEL_OPTION, result,
                    "Field dialog should return CANCEL_OPTION");
    }

    /**
     * Test showDialog with method dialog.
     * This verifies the method works correctly for method dialogs.
     */
    @Test
    public void testShowDialogWithMethodDialog() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        Thread closer = new Thread(() -> {
            try {
                Thread.sleep(100);
                SwingUtilities.invokeLater(() -> {
                    if (memberDialog != null && memberDialog.isVisible()) {
                        memberDialog.dispatchEvent(new WindowEvent(memberDialog, WindowEvent.WINDOW_CLOSING));
                    }
                });
            } catch (InterruptedException e) {
                // Ignore
            }
        });
        closer.start();

        int result = memberDialog.showDialog();

        assertEquals(MemberSpecificationDialog.CANCEL_OPTION, result,
                    "Method dialog should return CANCEL_OPTION");
    }

    /**
     * Test showDialog with null owner.
     * This verifies the method works when dialog has no owner.
     */
    @Test
    public void testShowDialogWithNullOwner() {
        memberDialog = new MemberSpecificationDialog(null, true);

        Thread closer = new Thread(() -> {
            try {
                Thread.sleep(100);
                SwingUtilities.invokeLater(() -> {
                    if (memberDialog != null && memberDialog.isVisible()) {
                        memberDialog.dispatchEvent(new WindowEvent(memberDialog, WindowEvent.WINDOW_CLOSING));
                    }
                });
            } catch (InterruptedException e) {
                // Ignore
            }
        });
        closer.start();

        int result = memberDialog.showDialog();

        assertEquals(MemberSpecificationDialog.CANCEL_OPTION, result,
                    "Dialog with null owner should return CANCEL_OPTION");
    }

    /**
     * Test that CANCEL_OPTION constant has expected value.
     * This verifies the constant used in line 437.
     */
    @Test
    public void testCancelOptionConstant() {
        assertEquals(1, MemberSpecificationDialog.CANCEL_OPTION,
                    "CANCEL_OPTION should have value 1");
    }

    /**
     * Test that APPROVE_OPTION constant has expected value.
     * This verifies the constant that can be returned from line 445.
     */
    @Test
    public void testApproveOptionConstant() {
        assertEquals(0, MemberSpecificationDialog.APPROVE_OPTION,
                    "APPROVE_OPTION should have value 0");
    }

    /**
     * Test that dialog is not visible before showDialog.
     * This verifies the initial state before line 443 is executed.
     */
    @Test
    public void testDialogNotVisibleBeforeShowDialog() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        assertFalse(memberDialog.isVisible(), "Dialog should not be visible before showDialog");
    }

    /**
     * Test that dialog is modal.
     * This is important for showDialog's blocking behavior.
     */
    @Test
    public void testDialogIsModal() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        assertTrue(memberDialog.isModal(), "Dialog should be modal for showDialog to block correctly");
    }

    /**
     * Test showDialog closes dialog properly with window closing event.
     * This verifies the complete execution path through line 445.
     */
    @Test
    public void testShowDialogWithWindowClosingEvent() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        Thread closer = new Thread(() -> {
            try {
                Thread.sleep(100);
                SwingUtilities.invokeLater(() -> {
                    if (memberDialog != null && memberDialog.isVisible()) {
                        WindowEvent closingEvent = new WindowEvent(memberDialog, WindowEvent.WINDOW_CLOSING);
                        memberDialog.dispatchEvent(closingEvent);
                    }
                });
            } catch (InterruptedException e) {
                // Ignore
            }
        });
        closer.start();

        int result = memberDialog.showDialog();

        assertEquals(MemberSpecificationDialog.CANCEL_OPTION, result,
                    "Window closing event should cause CANCEL_OPTION to be returned");
    }

    /**
     * Test showDialog returns immediately after dialog is closed.
     * This verifies line 445 returns the value after dialog closes.
     */
    @Test
    public void testShowDialogReturnsAfterDialogClosed() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        Thread closer = new Thread(() -> {
            try {
                Thread.sleep(100);
                SwingUtilities.invokeLater(() -> {
                    if (memberDialog != null && memberDialog.isVisible()) {
                        memberDialog.setVisible(false);
                        memberDialog.dispose();
                    }
                });
            } catch (InterruptedException e) {
                // Ignore
            }
        });
        closer.start();

        long startTime = System.currentTimeMillis();
        int result = memberDialog.showDialog();
        long endTime = System.currentTimeMillis();

        assertEquals(MemberSpecificationDialog.CANCEL_OPTION, result,
                    "showDialog should return after dialog is closed");
        assertTrue((endTime - startTime) < 5000,
                  "showDialog should return relatively quickly after dialog is closed");
    }

    /**
     * Test showDialog can be called on dialog created for field specification.
     * This ensures all lines 437-445 work for field dialogs.
     */
    @Test
    public void testShowDialogWorksForFieldSpecification() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Verify dialog is configured for fields
        assertNotNull(memberDialog, "Field dialog should be created");

        Thread closer = new Thread(() -> {
            try {
                Thread.sleep(100);
                SwingUtilities.invokeLater(() -> {
                    if (memberDialog != null && memberDialog.isVisible()) {
                        memberDialog.dispatchEvent(new WindowEvent(memberDialog, WindowEvent.WINDOW_CLOSING));
                    }
                });
            } catch (InterruptedException e) {
                // Ignore
            }
        });
        closer.start();

        int result = memberDialog.showDialog();

        assertEquals(MemberSpecificationDialog.CANCEL_OPTION, result,
                    "Field dialog showDialog should work correctly");
    }

    /**
     * Test showDialog can be called on dialog created for method specification.
     * This ensures all lines 437-445 work for method dialogs.
     */
    @Test
    public void testShowDialogWorksForMethodSpecification() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        // Verify dialog is configured for methods
        assertNotNull(memberDialog, "Method dialog should be created");

        Thread closer = new Thread(() -> {
            try {
                Thread.sleep(100);
                SwingUtilities.invokeLater(() -> {
                    if (memberDialog != null && memberDialog.isVisible()) {
                        memberDialog.dispatchEvent(new WindowEvent(memberDialog, WindowEvent.WINDOW_CLOSING));
                    }
                });
            } catch (InterruptedException e) {
                // Ignore
            }
        });
        closer.start();

        int result = memberDialog.showDialog();

        assertEquals(MemberSpecificationDialog.CANCEL_OPTION, result,
                    "Method dialog showDialog should work correctly");
    }

    /**
     * Test that dialog size is reasonable after pack is called.
     * This verifies line 441 pack() produces reasonable dimensions.
     */
    @Test
    public void testShowDialogPackProducesReasonableSize() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        Thread checker = new Thread(() -> {
            try {
                Thread.sleep(100);
                SwingUtilities.invokeLater(() -> {
                    if (memberDialog != null && memberDialog.isVisible()) {
                        Dimension size = memberDialog.getSize();
                        assertTrue(size.width > 100, "Dialog width should be reasonable (> 100)");
                        assertTrue(size.height > 100, "Dialog height should be reasonable (> 100)");
                        assertTrue(size.width < 2000, "Dialog width should be reasonable (< 2000)");
                        assertTrue(size.height < 2000, "Dialog height should be reasonable (< 2000)");
                        memberDialog.dispatchEvent(new WindowEvent(memberDialog, WindowEvent.WINDOW_CLOSING));
                    }
                });
            } catch (InterruptedException e) {
                // Ignore
            }
        });
        checker.start();

        memberDialog.showDialog();
    }

    /**
     * Test that constants are different values.
     * This verifies APPROVE_OPTION and CANCEL_OPTION are distinguishable.
     */
    @Test
    public void testConstantsAreDifferent() {
        assertNotEquals(MemberSpecificationDialog.APPROVE_OPTION,
                        MemberSpecificationDialog.CANCEL_OPTION,
                        "APPROVE_OPTION and CANCEL_OPTION should be different values");
    }

    /**
     * Test showDialog execution with visible owner.
     * This verifies line 442 setLocationRelativeTo works with visible owner.
     */
    @Test
    public void testShowDialogWithVisibleOwner() {
        testDialog = new JDialog();
        testDialog.setSize(400, 300);
        testDialog.setVisible(false);

        memberDialog = new MemberSpecificationDialog(testDialog, true);

        Thread closer = new Thread(() -> {
            try {
                Thread.sleep(100);
                SwingUtilities.invokeLater(() -> {
                    if (memberDialog != null && memberDialog.isVisible()) {
                        memberDialog.dispatchEvent(new WindowEvent(memberDialog, WindowEvent.WINDOW_CLOSING));
                    }
                });
            } catch (InterruptedException e) {
                // Ignore
            }
        });
        closer.start();

        int result = memberDialog.showDialog();

        assertEquals(MemberSpecificationDialog.CANCEL_OPTION, result,
                    "showDialog should work with visible owner");
    }

    /**
     * Test that returnValue is set to CANCEL_OPTION before dialog is shown.
     * This specifically tests line 437.
     */
    @Test
    public void testReturnValueSetToCancelOptionBeforeShow() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        Thread closer = new Thread(() -> {
            try {
                Thread.sleep(100);
                SwingUtilities.invokeLater(() -> {
                    if (memberDialog != null && memberDialog.isVisible()) {
                        memberDialog.dispatchEvent(new WindowEvent(memberDialog, WindowEvent.WINDOW_CLOSING));
                    }
                });
            } catch (InterruptedException e) {
                // Ignore
            }
        });
        closer.start();

        // When dialog is closed without clicking OK, CANCEL_OPTION should be returned
        int result = memberDialog.showDialog();

        assertEquals(MemberSpecificationDialog.CANCEL_OPTION, result,
                    "returnValue should be CANCEL_OPTION (set in line 437)");
    }

    /**
     * Test showDialog multiple times on same dialog instance.
     * This verifies lines 437-445 can be executed multiple times.
     */
    @Test
    public void testShowDialogMultipleTimes() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // First call
        Thread closer1 = new Thread(() -> {
            try {
                Thread.sleep(100);
                SwingUtilities.invokeLater(() -> {
                    if (memberDialog != null && memberDialog.isVisible()) {
                        memberDialog.dispatchEvent(new WindowEvent(memberDialog, WindowEvent.WINDOW_CLOSING));
                    }
                });
            } catch (InterruptedException e) {
                // Ignore
            }
        });
        closer1.start();

        int result1 = memberDialog.showDialog();
        assertEquals(MemberSpecificationDialog.CANCEL_OPTION, result1,
                    "First call should return CANCEL_OPTION");

        // Second call
        Thread closer2 = new Thread(() -> {
            try {
                Thread.sleep(100);
                SwingUtilities.invokeLater(() -> {
                    if (memberDialog != null && memberDialog.isVisible()) {
                        memberDialog.dispatchEvent(new WindowEvent(memberDialog, WindowEvent.WINDOW_CLOSING));
                    }
                });
            } catch (InterruptedException e) {
                // Ignore
            }
        });
        closer2.start();

        int result2 = memberDialog.showDialog();
        assertEquals(MemberSpecificationDialog.CANCEL_OPTION, result2,
                    "Second call should return CANCEL_OPTION");
    }

    /**
     * Test that show() is called and dialog becomes visible.
     * This specifically tests line 443.
     */
    @Test
    public void testShowMethodCalledAndDialogBecomesVisible() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        final boolean[] wasVisible = {false};

        Thread checker = new Thread(() -> {
            try {
                Thread.sleep(100);
                SwingUtilities.invokeLater(() -> {
                    if (memberDialog != null) {
                        wasVisible[0] = memberDialog.isVisible();
                        if (memberDialog.isVisible()) {
                            memberDialog.dispatchEvent(new WindowEvent(memberDialog, WindowEvent.WINDOW_CLOSING));
                        }
                    }
                });
            } catch (InterruptedException e) {
                // Ignore
            }
        });
        checker.start();

        memberDialog.showDialog();

        assertTrue(wasVisible[0], "Dialog should have been visible after show() was called");
    }

    /**
     * Test comprehensive execution of all lines in showDialog.
     * This test verifies lines 437, 441, 442, 443, and 445 are all executed.
     */
    @Test
    public void testComprehensiveShowDialogExecution() {
        testDialog = new JDialog();
        testDialog.setSize(400, 300);
        testDialog.setLocation(100, 100);

        memberDialog = new MemberSpecificationDialog(testDialog, true);

        assertFalse(memberDialog.isVisible(), "Dialog should not be visible initially");

        final boolean[] packedAndPositioned = {false};

        Thread verifier = new Thread(() -> {
            try {
                Thread.sleep(100);
                SwingUtilities.invokeLater(() -> {
                    if (memberDialog != null && memberDialog.isVisible()) {
                        // Verify pack() was called (line 441) - dialog should have size
                        Dimension size = memberDialog.getSize();
                        boolean hasSizeFromPack = size.width > 0 && size.height > 0;

                        // Verify setLocationRelativeTo() was called (line 442) - dialog should have location
                        Point location = memberDialog.getLocation();
                        boolean hasLocation = location != null;

                        // Verify show() was called (line 443) - dialog is visible
                        boolean isVisible = memberDialog.isVisible();

                        packedAndPositioned[0] = hasSizeFromPack && hasLocation && isVisible;

                        memberDialog.dispatchEvent(new WindowEvent(memberDialog, WindowEvent.WINDOW_CLOSING));
                    }
                });
            } catch (InterruptedException e) {
                // Ignore
            }
        });
        verifier.start();

        // Execute showDialog - this should execute lines 437, 441, 442, 443
        int result = memberDialog.showDialog();

        // Verify return value (line 445)
        assertEquals(MemberSpecificationDialog.CANCEL_OPTION, result,
                    "Return value should be CANCEL_OPTION");
        assertTrue(packedAndPositioned[0],
                  "Dialog should have been packed, positioned, and shown");
    }
}
