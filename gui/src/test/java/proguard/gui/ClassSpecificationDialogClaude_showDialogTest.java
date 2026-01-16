package proguard.gui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Test class for ClassSpecificationDialog.showDialog() method.
 *
 * The showDialog method:
 * - Shows a modal dialog that blocks execution until the user closes it
 * - Returns APPROVE_OPTION (0) if the user clicks OK
 * - Returns CANCEL_OPTION (1) if the user clicks Cancel or closes the dialog
 * - Packs the dialog before showing
 * - Centers the dialog relative to its owner
 *
 * These tests verify:
 * - The APPROVE_OPTION and CANCEL_OPTION constants are correctly defined
 * - The dialog can be created and prepared for showing
 * - The dialog's state before showing
 * - The dialog can be programmatically closed to simulate user interaction
 *
 * Note: These tests require GUI components and will skip in headless environments.
 * Note: Testing showDialog() fully requires handling modal blocking behavior, which
 *       these tests accomplish by programmatically closing the dialog.
 */
public class ClassSpecificationDialogClaude_showDialogTest {

    private JFrame testFrame;
    private ClassSpecificationDialog dialog;

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
     * Test that CANCEL_OPTION constant is defined correctly.
     */
    @Test
    public void testCancelOptionConstant() {
        assertEquals(1, ClassSpecificationDialog.CANCEL_OPTION,
                     "CANCEL_OPTION should be 1");
    }

    /**
     * Test that APPROVE_OPTION constant is defined correctly.
     */
    @Test
    public void testApproveOptionConstant() {
        assertEquals(0, ClassSpecificationDialog.APPROVE_OPTION,
                     "APPROVE_OPTION should be 0");
    }

    /**
     * Test that the constants are different values.
     */
    @Test
    public void testConstantsAreDifferent() {
        assertNotEquals(ClassSpecificationDialog.APPROVE_OPTION,
                        ClassSpecificationDialog.CANCEL_OPTION,
                        "APPROVE_OPTION and CANCEL_OPTION should be different");
    }

    /**
     * Test that dialog is not visible before showDialog is called.
     */
    @Test
    public void testDialogNotVisibleBeforeShow() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        assertFalse(dialog.isVisible(), "Dialog should not be visible before showDialog");
    }

    /**
     * Test that dialog is modal.
     */
    @Test
    public void testDialogIsModal() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        assertTrue(dialog.isModal(), "Dialog should be modal for showDialog to work correctly");
    }

    /**
     * Test showDialog with programmatic cancel (close window).
     * This test programmatically closes the dialog to simulate user canceling.
     */
    @Test
    public void testShowDialogWithProgrammaticCancel() throws Exception {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        // Create a thread to close the dialog after a short delay
        Thread closerThread = new Thread(() -> {
            try {
                // Wait a bit for showDialog to be called
                Thread.sleep(100);
                // Close the dialog on the EDT
                SwingUtilities.invokeLater(() -> {
                    if (dialog != null && dialog.isVisible()) {
                        dialog.setVisible(false);
                        dialog.dispose();
                    }
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        closerThread.start();

        // Call showDialog - it should return CANCEL_OPTION when closed
        int result = dialog.showDialog();

        closerThread.join(2000); // Wait for thread to finish

        assertEquals(ClassSpecificationDialog.CANCEL_OPTION, result,
                     "showDialog should return CANCEL_OPTION when dialog is closed");
    }

    /**
     * Test that dialog can be created and is ready to show.
     */
    @Test
    public void testDialogReadyToShow() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        assertNotNull(dialog, "Dialog should be created");
        assertFalse(dialog.isVisible(), "Dialog should not be visible yet");
        assertTrue(dialog.isModal(), "Dialog should be modal");
        assertEquals(testFrame, dialog.getOwner(), "Dialog should have correct owner");
    }

    /**
     * Test that dialog has correct title before showing.
     */
    @Test
    public void testDialogTitleBeforeShow() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        assertEquals("Specify classes and class members...", dialog.getTitle(),
                     "Dialog should have correct title");
    }

    /**
     * Test that dialog is resizable before showing.
     */
    @Test
    public void testDialogResizableBeforeShow() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        assertTrue(dialog.isResizable(), "Dialog should be resizable");
    }

    /**
     * Test that dialog has content before showing.
     */
    @Test
    public void testDialogHasContentBeforeShow() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        assertNotNull(dialog.getContentPane(), "Dialog should have content pane");
        assertTrue(dialog.getContentPane().getComponentCount() > 0,
                   "Dialog should have components");
    }

    /**
     * Test showDialog with different dialog configurations.
     */
    @Test
    public void testShowDialogWithDifferentConfigurations() throws Exception {
        testFrame = new JFrame("Test Frame");

        // Test with includeKeepSettings=true, includeFieldButton=true
        dialog = new ClassSpecificationDialog(testFrame, true, true);
        assertNotNull(dialog, "Dialog with (true, true) should be created");
        assertTrue(dialog.isModal(), "Dialog should be modal");

        // Create a new dialog with different configuration
        dialog.dispose();
        dialog = new ClassSpecificationDialog(testFrame, false, false);
        assertNotNull(dialog, "Dialog with (false, false) should be created");
        assertTrue(dialog.isModal(), "Dialog should be modal");
    }

    /**
     * Test that dialog can be disposed without showing.
     */
    @Test
    public void testDialogCanBeDisposedWithoutShowing() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        // Should not throw exception
        dialog.dispose();

        // Create a new dialog to verify disposal didn't break anything
        dialog = new ClassSpecificationDialog(testFrame, true, true);
        assertNotNull(dialog, "New dialog should be created after disposing previous one");
    }

    /**
     * Test that multiple dialogs can be created sequentially.
     */
    @Test
    public void testMultipleDialogsSequentially() {
        testFrame = new JFrame("Test Frame");

        ClassSpecificationDialog dialog1 = new ClassSpecificationDialog(testFrame, true, true);
        assertNotNull(dialog1, "First dialog should be created");
        dialog1.dispose();

        ClassSpecificationDialog dialog2 = new ClassSpecificationDialog(testFrame, false, true);
        assertNotNull(dialog2, "Second dialog should be created");
        dialog2.dispose();

        ClassSpecificationDialog dialog3 = new ClassSpecificationDialog(testFrame, true, false);
        assertNotNull(dialog3, "Third dialog should be created");
        dialog3.dispose();
    }

    /**
     * Test dialog with null owner can be prepared for showing.
     */
    @Test
    public void testShowDialogWithNullOwner() {
        dialog = new ClassSpecificationDialog(null, true, true);

        assertNotNull(dialog, "Dialog with null owner should be created");
        assertTrue(dialog.isModal(), "Dialog should be modal");
        assertNull(dialog.getOwner(), "Dialog owner should be null");
    }

    /**
     * Test that dialog state can be set before showing.
     */
    @Test
    public void testDialogStateBeforeShowing() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        // Set some state before showing
        proguard.ClassSpecification classSpec = new proguard.ClassSpecification(
                "Test", 0, 0, null, "TestClass", null, null
        );
        dialog.setClassSpecification(classSpec);

        // Verify state is set
        proguard.ClassSpecification result = dialog.getClassSpecification();
        assertEquals("Test", result.comments, "State should be set before showing");
        assertEquals("TestClass", result.className, "State should be set before showing");
    }

    /**
     * Test that dialog closes properly with window closing event.
     */
    @Test
    public void testDialogWindowClosing() throws Exception {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        // Create a thread to dispatch window closing event
        Thread closerThread = new Thread(() -> {
            try {
                Thread.sleep(100);
                SwingUtilities.invokeLater(() -> {
                    if (dialog != null && dialog.isVisible()) {
                        WindowEvent closingEvent = new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING);
                        dialog.dispatchEvent(closingEvent);
                    }
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        closerThread.start();

        // Call showDialog
        int result = dialog.showDialog();

        closerThread.join(2000);

        // Should return CANCEL_OPTION
        assertEquals(ClassSpecificationDialog.CANCEL_OPTION, result,
                     "Window closing should return CANCEL_OPTION");
    }

    /**
     * Test that dialog size is reasonable after creation.
     */
    @Test
    public void testDialogSizeBeforeShowing() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        // Pack to get preferred size
        dialog.pack();

        Dimension size = dialog.getSize();
        assertTrue(size.width > 0, "Dialog width should be positive");
        assertTrue(size.height > 0, "Dialog height should be positive");
    }

    /**
     * Test that dialog can be created with different owner states.
     */
    @Test
    public void testDialogWithDifferentOwnerStates() {
        testFrame = new JFrame("Test Frame");

        // Test with visible owner
        testFrame.setVisible(false);
        dialog = new ClassSpecificationDialog(testFrame, true, true);
        assertNotNull(dialog, "Dialog should be created with invisible owner");
        dialog.dispose();

        // Test with minimized owner
        testFrame.setExtendedState(JFrame.ICONIFIED);
        dialog = new ClassSpecificationDialog(testFrame, true, true);
        assertNotNull(dialog, "Dialog should be created with minimized owner");
    }
}
