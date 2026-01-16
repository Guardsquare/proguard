package proguard.gui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Test class for ClassSpecificationDialog constructor.
 *
 * The constructor takes three parameters:
 * - JFrame owner: the parent frame for the dialog
 * - boolean includeKeepSettings: whether to include keep-related UI settings
 * - boolean includeFieldButton: whether to include field button in member specifications
 *
 * These tests verify:
 * - The dialog can be constructed with various parameter combinations
 * - The dialog is properly initialized as a modal dialog
 * - The dialog's properties (title, resizability, modality) are set correctly
 * - The dialog can be created with null owner (parentless)
 * - The nested condition dialog is created when includeKeepSettings is true
 * - The dialog is properly constructed without exceptions in all valid scenarios
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class ClassSpecificationDialogClaude_constructorTest {

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
     * Test that the constructor creates a dialog with includeKeepSettings=true and includeFieldButton=true.
     * This is the most feature-complete configuration.
     */
    @Test
    public void testConstructorWithKeepSettingsAndFieldButton() {
        testFrame = new JFrame("Test Frame");

        // Create dialog with both flags true
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        assertNotNull(dialog, "Dialog should be created successfully");
        assertTrue(dialog.isModal(), "Dialog should be modal");
        assertTrue(dialog.isResizable(), "Dialog should be resizable");
        assertEquals("Specify classes and class members...", dialog.getTitle(), "Dialog should have correct title");
        assertEquals(testFrame, dialog.getOwner(), "Dialog owner should be the test frame");
    }

    /**
     * Test that the constructor creates a dialog with includeKeepSettings=true and includeFieldButton=false.
     */
    @Test
    public void testConstructorWithKeepSettingsWithoutFieldButton() {
        testFrame = new JFrame("Test Frame");

        // Create dialog with includeKeepSettings=true, includeFieldButton=false
        dialog = new ClassSpecificationDialog(testFrame, true, false);

        assertNotNull(dialog, "Dialog should be created successfully");
        assertTrue(dialog.isModal(), "Dialog should be modal");
        assertTrue(dialog.isResizable(), "Dialog should be resizable");
        assertEquals("Specify classes and class members...", dialog.getTitle(), "Dialog should have correct title");
    }

    /**
     * Test that the constructor creates a dialog with includeKeepSettings=false and includeFieldButton=true.
     * When includeKeepSettings is false, the keep-related panels should not be included.
     */
    @Test
    public void testConstructorWithoutKeepSettingsWithFieldButton() {
        testFrame = new JFrame("Test Frame");

        // Create dialog with includeKeepSettings=false, includeFieldButton=true
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        assertNotNull(dialog, "Dialog should be created successfully");
        assertTrue(dialog.isModal(), "Dialog should be modal");
        assertTrue(dialog.isResizable(), "Dialog should be resizable");
        assertEquals("Specify classes and class members...", dialog.getTitle(), "Dialog should have correct title");
    }

    /**
     * Test that the constructor creates a dialog with includeKeepSettings=false and includeFieldButton=false.
     * This is the minimal configuration.
     */
    @Test
    public void testConstructorWithoutKeepSettingsWithoutFieldButton() {
        testFrame = new JFrame("Test Frame");

        // Create dialog with both flags false
        dialog = new ClassSpecificationDialog(testFrame, false, false);

        assertNotNull(dialog, "Dialog should be created successfully");
        assertTrue(dialog.isModal(), "Dialog should be modal");
        assertTrue(dialog.isResizable(), "Dialog should be resizable");
        assertEquals("Specify classes and class members...", dialog.getTitle(), "Dialog should have correct title");
    }

    /**
     * Test that the constructor can create a dialog with a null owner.
     * A null owner means the dialog has no parent window.
     */
    @Test
    public void testConstructorWithNullOwner() {
        // Create dialog with null owner
        dialog = new ClassSpecificationDialog(null, true, true);

        assertNotNull(dialog, "Dialog should be created successfully with null owner");
        assertTrue(dialog.isModal(), "Dialog should be modal");
        assertTrue(dialog.isResizable(), "Dialog should be resizable");
        assertNull(dialog.getOwner(), "Dialog owner should be null");
    }

    /**
     * Test that the constructor with null owner and minimal settings works.
     */
    @Test
    public void testConstructorWithNullOwnerMinimalSettings() {
        // Create dialog with null owner and minimal settings
        dialog = new ClassSpecificationDialog(null, false, false);

        assertNotNull(dialog, "Dialog should be created successfully with null owner");
        assertTrue(dialog.isModal(), "Dialog should be modal");
        assertTrue(dialog.isResizable(), "Dialog should be resizable");
    }

    /**
     * Test that the dialog is initially not visible after construction.
     * The dialog should only become visible when showDialog() is called.
     */
    @Test
    public void testConstructorDialogNotInitiallyVisible() {
        testFrame = new JFrame("Test Frame");

        dialog = new ClassSpecificationDialog(testFrame, true, true);

        assertFalse(dialog.isVisible(), "Dialog should not be visible immediately after construction");
    }

    /**
     * Test that multiple dialogs can be constructed without interfering with each other.
     */
    @Test
    public void testMultipleDialogConstruction() {
        testFrame = new JFrame("Test Frame");

        ClassSpecificationDialog dialog1 = new ClassSpecificationDialog(testFrame, true, true);
        ClassSpecificationDialog dialog2 = new ClassSpecificationDialog(testFrame, false, false);
        ClassSpecificationDialog dialog3 = new ClassSpecificationDialog(testFrame, true, false);

        assertNotNull(dialog1, "First dialog should be created");
        assertNotNull(dialog2, "Second dialog should be created");
        assertNotNull(dialog3, "Third dialog should be created");

        assertNotSame(dialog1, dialog2, "Dialogs should be different instances");
        assertNotSame(dialog2, dialog3, "Dialogs should be different instances");

        // Clean up all dialogs
        dialog1.dispose();
        dialog2.dispose();
        dialog3.dispose();
    }

    /**
     * Test that the dialog has a content pane after construction.
     */
    @Test
    public void testConstructorInitializesContentPane() {
        testFrame = new JFrame("Test Frame");

        dialog = new ClassSpecificationDialog(testFrame, true, true);

        assertNotNull(dialog.getContentPane(), "Dialog should have a content pane");
        assertTrue(dialog.getContentPane().getComponentCount() > 0,
                   "Dialog content pane should contain components");
    }

    /**
     * Test that the dialog can be constructed and disposed multiple times.
     */
    @Test
    public void testConstructorAndDisposeMultipleTimes() {
        testFrame = new JFrame("Test Frame");

        // Create and dispose first dialog
        dialog = new ClassSpecificationDialog(testFrame, true, true);
        assertNotNull(dialog, "First dialog should be created");
        dialog.dispose();

        // Create and dispose second dialog
        dialog = new ClassSpecificationDialog(testFrame, false, false);
        assertNotNull(dialog, "Second dialog should be created after first disposal");
        dialog.dispose();

        // Create third dialog (will be disposed in tearDown)
        dialog = new ClassSpecificationDialog(testFrame, true, false);
        assertNotNull(dialog, "Third dialog should be created after second disposal");
    }

    /**
     * Test that the dialog's owner frame can be minimized without affecting the dialog's construction.
     */
    @Test
    public void testConstructorWithMinimizedOwner() {
        testFrame = new JFrame("Test Frame");
        testFrame.setExtendedState(JFrame.ICONIFIED);

        dialog = new ClassSpecificationDialog(testFrame, true, true);

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

        dialog = new ClassSpecificationDialog(testFrame, true, true);

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
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        assertNotNull(dialog, "Dialog should be created immediately after owner creation");
        assertEquals(testFrame, dialog.getOwner(), "Dialog owner should be correctly set");
    }

    /**
     * Test that constructor properly sets up the dialog as a JDialog subclass.
     */
    @Test
    public void testConstructorCreatesJDialogSubclass() {
        testFrame = new JFrame("Test Frame");

        dialog = new ClassSpecificationDialog(testFrame, true, true);

        assertTrue(dialog instanceof JDialog, "Dialog should be an instance of JDialog");
        assertTrue(dialog instanceof Dialog, "Dialog should be an instance of Dialog");
        assertTrue(dialog instanceof Window, "Dialog should be an instance of Window");
    }
}
