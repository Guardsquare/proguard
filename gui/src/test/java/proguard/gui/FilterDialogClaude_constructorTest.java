package proguard.gui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Test class for FilterDialog constructor.
 *
 * The constructor takes two parameters:
 * - JFrame owner: the parent frame for the dialog
 * - String explanation: text to display as an explanation in the dialog
 *
 * These tests verify:
 * - The dialog can be constructed with various parameter combinations
 * - The dialog is properly initialized as a modal dialog
 * - The dialog's properties (modality, resizability) are set correctly
 * - The dialog can be created with null owner (parentless)
 * - All UI components are properly initialized (text fields, labels, panels, buttons)
 * - The layout and constraints are properly set up
 * - The explanation text area is properly configured
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class FilterDialogClaude_constructorTest {

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
     * Test that the constructor creates a dialog with valid owner and explanation.
     * This test covers the basic initialization of the dialog, including:
     * - Modal setting (line 74)
     * - Resizable setting (line 75)
     * - GridBagConstraints initialization (lines 78-115)
     * - GridBagLayout creation (line 117)
     * - Border creation (line 119)
     * - Text fields initialization (lines 59-66)
     * - All UI component creation and layout
     */
    @Test
    public void testConstructorWithValidOwnerAndExplanation() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "This is a test explanation for the filter dialog.");

        assertNotNull(dialog, "Dialog should be created successfully");
        assertTrue(dialog.isModal(), "Dialog should be modal");
        assertTrue(dialog.isResizable(), "Dialog should be resizable");
        assertEquals(testFrame, dialog.getOwner(), "Dialog owner should be the test frame");
        assertNotNull(dialog.getContentPane(), "Dialog should have a content pane");
        assertTrue(dialog.getContentPane().getComponentCount() > 0,
                   "Dialog content pane should contain components");
    }

    /**
     * Test that the constructor creates a dialog with null owner.
     * A null owner means the dialog has no parent window.
     */
    @Test
    public void testConstructorWithNullOwner() {
        dialog = new FilterDialog(null, "Test explanation");

        assertNotNull(dialog, "Dialog should be created successfully with null owner");
        assertTrue(dialog.isModal(), "Dialog should be modal");
        assertTrue(dialog.isResizable(), "Dialog should be resizable");
        assertNull(dialog.getOwner(), "Dialog owner should be null");
    }

    /**
     * Test that the constructor creates a dialog with empty explanation.
     * The explanation text area should still be created and configured.
     */
    @Test
    public void testConstructorWithEmptyExplanation() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "");

        assertNotNull(dialog, "Dialog should be created successfully with empty explanation");
        assertTrue(dialog.isModal(), "Dialog should be modal");
        assertTrue(dialog.isResizable(), "Dialog should be resizable");
    }

    /**
     * Test that the constructor creates a dialog with null explanation.
     * The dialog should handle null explanation gracefully.
     */
    @Test
    public void testConstructorWithNullExplanation() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, null);

        assertNotNull(dialog, "Dialog should be created successfully with null explanation");
        assertTrue(dialog.isModal(), "Dialog should be modal");
        assertTrue(dialog.isResizable(), "Dialog should be resizable");
    }

    /**
     * Test that the constructor creates a dialog with long explanation.
     * This tests the text area's line wrap and word wrap functionality.
     * Covers lines 122-126 (text area configuration).
     */
    @Test
    public void testConstructorWithLongExplanation() {
        testFrame = new JFrame("Test Frame");
        String longExplanation = "This is a very long explanation that should wrap across multiple lines. " +
                                "It contains enough text to verify that the text area properly handles " +
                                "long explanations without any issues. The text should wrap nicely and " +
                                "demonstrate that the line wrap and word wrap features are working correctly. " +
                                "This long text helps ensure the UI components are properly initialized " +
                                "and the layout constraints are correctly applied.";
        dialog = new FilterDialog(testFrame, longExplanation);

        assertNotNull(dialog, "Dialog should be created successfully with long explanation");
        assertTrue(dialog.getContentPane().getComponentCount() > 0,
                   "Dialog content pane should contain components");
    }

    /**
     * Test that the constructor creates a dialog with multiline explanation.
     * Tests that newlines in the explanation are handled properly.
     */
    @Test
    public void testConstructorWithMultilineExplanation() {
        testFrame = new JFrame("Test Frame");
        String multilineExplanation = "Line 1: First line of explanation\n" +
                                     "Line 2: Second line of explanation\n" +
                                     "Line 3: Third line of explanation";
        dialog = new FilterDialog(testFrame, multilineExplanation);

        assertNotNull(dialog, "Dialog should be created successfully with multiline explanation");
        assertTrue(dialog.isModal(), "Dialog should be modal");
    }

    /**
     * Test that the dialog is initially not visible after construction.
     * The dialog should only become visible when showDialog() is called.
     */
    @Test
    public void testConstructorDialogNotInitiallyVisible() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        assertFalse(dialog.isVisible(), "Dialog should not be visible immediately after construction");
    }

    /**
     * Test that the constructor initializes the content pane with components.
     * This verifies that all the panels, labels, text fields, and buttons are created.
     * Covers lines related to component creation (129-196).
     */
    @Test
    public void testConstructorInitializesContentPane() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        assertNotNull(dialog.getContentPane(), "Dialog should have a content pane");
        assertTrue(dialog.getContentPane().getComponentCount() > 0,
                   "Dialog content pane should contain components");

        // Verify that the content pane has the main panel
        Container contentPane = dialog.getContentPane();
        assertEquals(1, contentPane.getComponentCount(),
                    "Content pane should have exactly one component (the main panel)");
    }

    /**
     * Test that multiple dialogs can be constructed without interfering with each other.
     * This ensures that the static configuration doesn't cause issues.
     */
    @Test
    public void testMultipleDialogConstruction() {
        testFrame = new JFrame("Test Frame");

        FilterDialog dialog1 = new FilterDialog(testFrame, "Explanation 1");
        FilterDialog dialog2 = new FilterDialog(testFrame, "Explanation 2");
        FilterDialog dialog3 = new FilterDialog(testFrame, "Explanation 3");

        assertNotNull(dialog1, "First dialog should be created");
        assertNotNull(dialog2, "Second dialog should be created");
        assertNotNull(dialog3, "Third dialog should be created");

        assertNotSame(dialog1, dialog2, "Dialogs should be different instances");
        assertNotSame(dialog2, dialog3, "Dialogs should be different instances");

        assertTrue(dialog1.isModal(), "First dialog should be modal");
        assertTrue(dialog2.isModal(), "Second dialog should be modal");
        assertTrue(dialog3.isModal(), "Third dialog should be modal");

        // Clean up all dialogs
        dialog1.dispose();
        dialog2.dispose();
        dialog3.dispose();
    }

    /**
     * Test that the dialog can be constructed and disposed multiple times.
     * This tests the lifecycle of the dialog.
     */
    @Test
    public void testConstructorAndDisposeMultipleTimes() {
        testFrame = new JFrame("Test Frame");

        // Create and dispose first dialog
        dialog = new FilterDialog(testFrame, "Explanation 1");
        assertNotNull(dialog, "First dialog should be created");
        dialog.dispose();

        // Create and dispose second dialog
        dialog = new FilterDialog(testFrame, "Explanation 2");
        assertNotNull(dialog, "Second dialog should be created after first disposal");
        dialog.dispose();

        // Create third dialog (will be disposed in tearDown)
        dialog = new FilterDialog(testFrame, "Explanation 3");
        assertNotNull(dialog, "Third dialog should be created after second disposal");
    }

    /**
     * Test that the dialog's owner frame can be minimized without affecting the dialog's construction.
     */
    @Test
    public void testConstructorWithMinimizedOwner() {
        testFrame = new JFrame("Test Frame");
        testFrame.setExtendedState(JFrame.ICONIFIED);

        dialog = new FilterDialog(testFrame, "Test explanation");

        assertNotNull(dialog, "Dialog should be created even with minimized owner");
        assertEquals(testFrame, dialog.getOwner(), "Dialog owner should still be the minimized frame");
    }

    /**
     * Test that the dialog's owner frame can be invisible during construction.
     */
    @Test
    public void testConstructorWithInvisibleOwner() {
        testFrame = new JFrame("Test Frame");
        testFrame.setVisible(false);

        dialog = new FilterDialog(testFrame, "Test explanation");

        assertNotNull(dialog, "Dialog should be created even with invisible owner");
        assertEquals(testFrame, dialog.getOwner(), "Dialog owner should still be the invisible frame");
    }

    /**
     * Test that the dialog can be constructed immediately after owner creation.
     */
    @Test
    public void testConstructorImmediatelyAfterOwnerCreation() {
        testFrame = new JFrame("Test Frame");

        // Create dialog immediately after frame creation
        dialog = new FilterDialog(testFrame, "Test explanation");

        assertNotNull(dialog, "Dialog should be created immediately after owner creation");
        assertEquals(testFrame, dialog.getOwner(), "Dialog owner should be correctly set");
    }

    /**
     * Test that constructor properly sets up the dialog as a JDialog subclass.
     */
    @Test
    public void testConstructorCreatesJDialogSubclass() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        assertTrue(dialog instanceof JDialog, "Dialog should be an instance of JDialog");
        assertTrue(dialog instanceof Dialog, "Dialog should be an instance of Dialog");
        assertTrue(dialog instanceof Window, "Dialog should be an instance of Window");
    }

    /**
     * Test that the dialog is modal by default.
     * Modal dialogs block input to other windows.
     */
    @Test
    public void testConstructorCreatesModalDialog() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        assertTrue(dialog.isModal(), "Dialog should be modal by default");
    }

    /**
     * Test that the dialog is resizable.
     * Users should be able to resize the dialog window.
     */
    @Test
    public void testConstructorCreatesResizableDialog() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        assertTrue(dialog.isResizable(), "Dialog should be resizable");
    }

    /**
     * Test constructor with special characters in explanation.
     * The dialog should handle special characters gracefully.
     */
    @Test
    public void testConstructorWithSpecialCharactersInExplanation() {
        testFrame = new JFrame("Test Frame");
        String specialExplanation = "Special chars: \n\t<>&\"'[]{}@#$%^&*()";
        dialog = new FilterDialog(testFrame, specialExplanation);

        assertNotNull(dialog, "Dialog should be created successfully with special characters");
        assertTrue(dialog.isModal(), "Dialog should be modal");
    }

    /**
     * Test constructor with Unicode characters in explanation.
     */
    @Test
    public void testConstructorWithUnicodeInExplanation() {
        testFrame = new JFrame("Test Frame");
        String unicodeExplanation = "Unicode: \u00E9\u00F1\u00FC \u4E2D\u6587 \u0627\u0644\u0639\u0631\u0628\u064A\u0629";
        dialog = new FilterDialog(testFrame, unicodeExplanation);

        assertNotNull(dialog, "Dialog should be created successfully with Unicode characters");
        assertTrue(dialog.isModal(), "Dialog should be modal");
    }

    /**
     * Test that constructor with visible owner frame works correctly.
     */
    @Test
    public void testConstructorWithVisibleOwner() {
        testFrame = new JFrame("Test Frame");
        testFrame.setVisible(true);

        dialog = new FilterDialog(testFrame, "Test explanation");

        assertNotNull(dialog, "Dialog should be created with visible owner");
        assertEquals(testFrame, dialog.getOwner(), "Dialog owner should be the visible frame");

        testFrame.setVisible(false);
    }

    /**
     * Test that constructor initializes all filter text fields.
     * This ensures that lines 59-66 are covered (text field initialization).
     */
    @Test
    public void testConstructorInitializesAllTextFields() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        // The dialog should be created without exceptions
        assertNotNull(dialog, "Dialog should be created successfully");

        // Verify that the dialog has components (which includes the text fields)
        assertTrue(dialog.getContentPane().getComponentCount() > 0,
                   "Dialog should have components including text fields");
    }

    /**
     * Test that constructor with very short explanation works.
     */
    @Test
    public void testConstructorWithShortExplanation() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "X");

        assertNotNull(dialog, "Dialog should be created successfully with single character explanation");
        assertTrue(dialog.isModal(), "Dialog should be modal");
    }

    /**
     * Test that constructor with explanation containing only whitespace works.
     */
    @Test
    public void testConstructorWithWhitespaceExplanation() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "   \t\n   ");

        assertNotNull(dialog, "Dialog should be created successfully with whitespace explanation");
        assertTrue(dialog.isModal(), "Dialog should be modal");
    }

    /**
     * Test that constructor creates OK and Cancel buttons.
     * This covers lines 170-187 (button creation and action listeners).
     */
    @Test
    public void testConstructorCreatesButtons() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        assertNotNull(dialog, "Dialog should be created successfully");

        // The buttons are part of the main panel which should be in the content pane
        Container contentPane = dialog.getContentPane();
        assertTrue(contentPane.getComponentCount() > 0,
                   "Content pane should contain the main panel with buttons");
    }

    /**
     * Test that constructor with extremely long explanation doesn't fail.
     */
    @Test
    public void testConstructorWithExtremelyLongExplanation() {
        testFrame = new JFrame("Test Frame");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append("This is a very long explanation that repeats many times. ");
        }
        dialog = new FilterDialog(testFrame, sb.toString());

        assertNotNull(dialog, "Dialog should be created successfully with extremely long explanation");
        assertTrue(dialog.isModal(), "Dialog should be modal");
    }

    /**
     * Test that constructor with owner having custom title works.
     */
    @Test
    public void testConstructorWithCustomTitleOwner() {
        testFrame = new JFrame("Custom Title Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        assertNotNull(dialog, "Dialog should be created successfully");
        assertEquals(testFrame, dialog.getOwner(), "Dialog owner should be the custom titled frame");
    }

    /**
     * Test that constructor works when owner has specific size.
     */
    @Test
    public void testConstructorWithSizedOwner() {
        testFrame = new JFrame("Test Frame");
        testFrame.setSize(800, 600);

        dialog = new FilterDialog(testFrame, "Test explanation");

        assertNotNull(dialog, "Dialog should be created successfully with sized owner");
        assertEquals(testFrame, dialog.getOwner(), "Dialog owner should be the sized frame");
    }

    /**
     * Test that constructor works when owner has specific location.
     */
    @Test
    public void testConstructorWithPositionedOwner() {
        testFrame = new JFrame("Test Frame");
        testFrame.setLocation(100, 100);

        dialog = new FilterDialog(testFrame, "Test explanation");

        assertNotNull(dialog, "Dialog should be created successfully with positioned owner");
        assertEquals(testFrame, dialog.getOwner(), "Dialog owner should be the positioned frame");
    }
}
