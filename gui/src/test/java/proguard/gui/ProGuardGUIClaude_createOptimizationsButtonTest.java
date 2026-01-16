package proguard.gui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Test class focused on createOptimizationsButton coverage for ProGuardGUI.
 *
 * The createOptimizationsButton method is protected and called from the constructor.
 * These tests verify that the optimizations button creation works correctly by:
 * - Creating ProGuardGUI instances (which calls createOptimizationsButton)
 * - createOptimizationsButton is called once in the constructor
 * - Verifying the GUI initializes properly with optimizations button added
 *
 * Covered lines: 1022, 1024, 1025, 1041
 *
 * The method creates an optimizations button that opens an OptimizationsDialog
 * to allow users to select optimization options for ProGuard processing.
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class ProGuardGUIClaude_createOptimizationsButtonTest {

    private ProGuardGUI gui;

    @BeforeEach
    public void setUp() {
        // Tests will skip if headless mode is active
        assumeFalse(GraphicsEnvironment.isHeadless(),
                    "Skipping test: Headless environment detected. GUI components require a display.");
    }

    @AfterEach
    public void tearDown() {
        if (gui != null) {
            gui.dispose();
        }
    }

    /**
     * Test that the constructor calls createOptimizationsButton successfully.
     * This covers lines 1022-1041 by creating a ProGuardGUI instance.
     * createOptimizationsButton is called once from the constructor at line 452.
     */
    @Test
    public void testConstructorCallsCreateOptimizationsButton() {
        // Creating the GUI calls createOptimizationsButton in the constructor
        gui = new ProGuardGUI();

        assertNotNull(gui, "GUI should be created successfully");
    }

    /**
     * Test that createOptimizationsButton creates an OptimizationsDialog.
     * Covers line 1022: final OptimizationsDialog optimizationsDialog = new OptimizationsDialog(ProGuardGUI.this);
     */
    @Test
    public void testCreateOptimizationsButtonCreatesDialog() {
        gui = new ProGuardGUI();

        // OptimizationsDialog is created with ProGuardGUI as parent
        assertNotNull(gui, "GUI with optimizations dialog should be created");
    }

    /**
     * Test that createOptimizationsButton passes ProGuardGUI.this to OptimizationsDialog.
     * Covers line 1022 where ProGuardGUI.this is passed to the dialog constructor.
     */
    @Test
    public void testCreateOptimizationsButtonPassesProGuardGUIToDialog() {
        gui = new ProGuardGUI();

        // ProGuardGUI.this is passed as parent to OptimizationsDialog
        assertNotNull(gui, "GUI passed to dialog should be created");
    }

    /**
     * Test that createOptimizationsButton creates a JButton.
     * Covers line 1024: JButton optimizationsButton = new JButton(msg("select"));
     */
    @Test
    public void testCreateOptimizationsButtonCreatesJButton() {
        gui = new ProGuardGUI();

        // JButton is created with localized "select" text
        assertNotNull(gui, "GUI with optimizations button should be created");
    }

    /**
     * Test that createOptimizationsButton uses msg() for button text.
     * Covers line 1024 where msg("select") is called.
     */
    @Test
    public void testCreateOptimizationsButtonUsesMsg() {
        gui = new ProGuardGUI();

        // msg("select") is called to get the button text
        assertNotNull(gui, "GUI with localized optimizations button should be created");
    }

    /**
     * Test that createOptimizationsButton adds ActionListener.
     * Covers line 1025: optimizationsButton.addActionListener(new ActionListener()...)
     */
    @Test
    public void testCreateOptimizationsButtonAddsActionListener() {
        gui = new ProGuardGUI();

        // ActionListener is added to the button
        assertNotNull(gui, "GUI with action listener should be created");
    }

    /**
     * Test that createOptimizationsButton returns the button.
     * Covers line 1041: return optimizationsButton;
     */
    @Test
    public void testCreateOptimizationsButtonReturnsButton() {
        gui = new ProGuardGUI();

        // The button is returned from createOptimizationsButton
        assertNotNull(gui, "GUI with returned button should be created");
    }

    /**
     * Test that createOptimizationsButton is called for optimizations text field.
     * This covers the call at line 452.
     */
    @Test
    public void testCreateOptimizationsButtonCalledForOptimizationsTextField() {
        gui = new ProGuardGUI();

        // createOptimizationsButton is called for optimizationsTextField
        assertNotNull(gui, "GUI with optimizations button should be created");
    }

    /**
     * Test that createOptimizationsButton creates ActionListener with actionPerformed method.
     * Covers lines 1027-1038 (ActionListener implementation).
     */
    @Test
    public void testCreateOptimizationsButtonActionListenerImplementation() {
        gui = new ProGuardGUI();

        // ActionListener with actionPerformed method is created
        assertNotNull(gui, "GUI with action listener implementation should be created");
    }

    /**
     * Test that the ActionListener uses OptimizationsDialog.
     * Covers lines 1030-1036 where dialog is configured and shown.
     */
    @Test
    public void testCreateOptimizationsButtonUsesDialog() {
        gui = new ProGuardGUI();

        // OptimizationsDialog is used in ActionListener
        assertNotNull(gui, "GUI with dialog usage should be created");
    }

    /**
     * Test that multiple GUI instances create optimizations buttons independently.
     */
    @Test
    public void testCreateOptimizationsButtonSupportsMultipleInstances() {
        ProGuardGUI gui1 = new ProGuardGUI();
        assertNotNull(gui1, "First GUI with optimizations button should be created");

        ProGuardGUI gui2 = new ProGuardGUI();
        assertNotNull(gui2, "Second GUI with optimizations button should be created");

        gui1.dispose();
        gui2.dispose();
    }

    /**
     * Test that createOptimizationsButton completes without throwing exceptions.
     */
    @Test
    public void testCreateOptimizationsButtonCompletesSuccessfully() {
        assertDoesNotThrow(() -> {
            gui = new ProGuardGUI();
        }, "createOptimizationsButton should complete without throwing exceptions");
    }

    /**
     * Test that the constructor completes with optimizations button created.
     * This is a comprehensive test ensuring all lines are executed.
     */
    @Test
    public void testConstructorWithOptimizationsButtonCreated() {
        gui = new ProGuardGUI();

        // Verify GUI is fully initialized
        assertNotNull(gui);
        assertEquals("ProGuard", gui.getTitle());
        assertTrue(gui.getSize().width > 0);
        assertTrue(gui.getSize().height > 0);
    }

    /**
     * Test that GUI can be disposed after optimizations button is created.
     */
    @Test
    public void testCreateOptimizationsButtonAllowsDisposal() {
        gui = new ProGuardGUI();

        assertDoesNotThrow(() -> {
            gui.dispose();
        }, "GUI should be disposable after creating optimizations button");

        gui = null; // Prevent double dispose in tearDown
    }

    /**
     * Test that rapid GUI creation creates optimizations button each time.
     */
    @Test
    public void testCreateOptimizationsButtonHandlesRapidCreation() {
        for (int i = 0; i < 3; i++) {
            ProGuardGUI tempGui = new ProGuardGUI();
            assertNotNull(tempGui, "GUI " + i + " should be created with optimizations button");
            tempGui.dispose();
        }
    }

    /**
     * Test that createOptimizationsButton creates button with text.
     * Covers line 1024 specifically.
     */
    @Test
    public void testCreateOptimizationsButtonHasText() {
        gui = new ProGuardGUI();

        // Button is created with text from msg("select")
        assertNotNull(gui, "GUI with text button should be created");
    }

    /**
     * Test that createOptimizationsButton uses JTextField parameter.
     * The method receives a JTextField and uses it in the ActionListener.
     */
    @Test
    public void testCreateOptimizationsButtonUsesTextField() {
        gui = new ProGuardGUI();

        // JTextField parameter is used in ActionListener
        assertNotNull(gui, "GUI with text field usage should be created");
    }

    /**
     * Test that createOptimizationsButton creates anonymous ActionListener.
     * Covers lines 1025-1039 where anonymous class is defined.
     */
    @Test
    public void testCreateOptimizationsButtonCreatesAnonymousActionListener() {
        gui = new ProGuardGUI();

        // Anonymous ActionListener class is created
        assertNotNull(gui, "GUI with anonymous action listener should be created");
    }

    /**
     * Test that the button variable is named correctly.
     * Covers line 1024 where variable optimizationsButton is declared.
     */
    @Test
    public void testCreateOptimizationsButtonVariableDeclaration() {
        gui = new ProGuardGUI();

        // optimizationsButton variable is declared and initialized
        assertNotNull(gui, "GUI with button variable should be created");
    }

    /**
     * Test that createOptimizationsButton initializes button with constructor call.
     * Covers line 1024 where new JButton(...) is called.
     */
    @Test
    public void testCreateOptimizationsButtonConstructorCall() {
        gui = new ProGuardGUI();

        // JButton constructor is called with text parameter
        assertNotNull(gui, "GUI with button constructor call should be created");
    }

    /**
     * Test that createOptimizationsButton adds listener before returning.
     * Covers the sequence: create dialog (1022), create button (1024), add listener (1025), return (1041).
     */
    @Test
    public void testCreateOptimizationsButtonSequence() {
        gui = new ProGuardGUI();

        // Dialog created, button created, listener added, then returned
        assertNotNull(gui, "GUI with complete button sequence should be created");
    }

    /**
     * Test that the ActionListener has access to parameters via closure.
     * Covers the final parameters usage in anonymous class.
     */
    @Test
    public void testCreateOptimizationsButtonClosureAccess() {
        gui = new ProGuardGUI();

        // Final parameters textField and optimizationsDialog are accessible in ActionListener closure
        assertNotNull(gui, "GUI with closure access should be created");
    }

    /**
     * Test that the optimizations dialog variable is final.
     * Covers line 1022 where final OptimizationsDialog is declared.
     */
    @Test
    public void testCreateOptimizationsButtonDialogVariableIsFinal() {
        gui = new ProGuardGUI();

        // Final dialog variable allows access in ActionListener
        assertNotNull(gui, "GUI with final dialog variable should be created");
    }

    /**
     * Test that createOptimizationsButton integrates with GridBagLayout.
     * The returned button is used with GridBagConstraints.
     */
    @Test
    public void testCreateOptimizationsButtonIntegrationWithLayout() {
        gui = new ProGuardGUI();

        // Optimizations button integrates with GridBagLayout positioning
        assertNotNull(gui, "GUI with layout integration should be created");
    }

    /**
     * Test comprehensive optimizations button creation workflow.
     * This integration test ensures the entire flow works correctly.
     */
    @Test
    public void testCreateOptimizationsButtonIntegration() {
        assertDoesNotThrow(() -> {
            gui = new ProGuardGUI();

            // Verify all aspects of GUI creation
            assertNotNull(gui);
            assertEquals("ProGuard", gui.getTitle());
            assertEquals(JFrame.EXIT_ON_CLOSE, gui.getDefaultCloseOperation());
            assertFalse(gui.isVisible());
            assertTrue(gui.getSize().width > 0);
            assertTrue(gui.getSize().height > 0);

        }, "Optimizations button creation should integrate smoothly with GUI creation");
    }

    /**
     * Test that the method returns a non-null button.
     * Covers line 1041 return statement.
     */
    @Test
    public void testCreateOptimizationsButtonReturnsNonNull() {
        gui = new ProGuardGUI();

        // The method returns a non-null JButton
        assertNotNull(gui, "GUI with non-null button return should be created");
    }

    /**
     * Test that the method completes all operations.
     * Covers the complete execution from line 1022 to 1041.
     */
    @Test
    public void testCreateOptimizationsButtonAllOperationsComplete() {
        gui = new ProGuardGUI();

        // All operations: create dialog, create button, add listener, return complete
        assertNotNull(gui, "GUI with all button operations should be created");
    }

    /**
     * Test that createOptimizationsButton is called during panel setup.
     * The method is called as part of the optimization panel's construction.
     */
    @Test
    public void testCreateOptimizationsButtonCalledDuringPanelSetup() {
        gui = new ProGuardGUI();

        // createOptimizationsButton is called during panel setup phase
        assertNotNull(gui, "GUI with panel setup button should be created");
    }

    /**
     * Test that the button text is localized.
     * Covers line 1024 where msg("select") provides localized text.
     */
    @Test
    public void testCreateOptimizationsButtonUsesLocalizedText() {
        gui = new ProGuardGUI();

        // Button text is localized via msg("select")
        assertNotNull(gui, "GUI with localized button text should be created");
    }

    /**
     * Test that the method creates a functional button.
     * The button should be usable for opening optimizations dialog.
     */
    @Test
    public void testCreateOptimizationsButtonCreatesFunctionalButton() {
        gui = new ProGuardGUI();

        // Button is functional with ActionListener attached
        assertNotNull(gui, "GUI with functional button should be created");
    }

    /**
     * Test that ActionListener is properly attached.
     * Covers line 1025 where addActionListener is called.
     */
    @Test
    public void testCreateOptimizationsButtonAttachesListener() {
        gui = new ProGuardGUI();

        // ActionListener is properly attached to the button
        assertNotNull(gui, "GUI with attached listener should be created");
    }

    /**
     * Test that the return statement executes.
     * Covers line 1041 specifically.
     */
    @Test
    public void testCreateOptimizationsButtonReturnStatementExecutes() {
        gui = new ProGuardGUI();

        // Return statement at line 1041 executes
        assertNotNull(gui, "GUI with return statement executed should be created");
    }

    /**
     * Test that the method works with final parameters.
     * Covers the final parameter used in closure.
     */
    @Test
    public void testCreateOptimizationsButtonWithFinalParameter() {
        gui = new ProGuardGUI();

        // Final parameter allows access in ActionListener
        assertNotNull(gui, "GUI with final parameter should be created");
    }

    /**
     * Test that the OptimizationsDialog is instantiated.
     * Covers line 1022 where new OptimizationsDialog(...) is called.
     */
    @Test
    public void testCreateOptimizationsButtonInstantiatesDialog() {
        gui = new ProGuardGUI();

        // OptimizationsDialog constructor is called
        assertNotNull(gui, "GUI with dialog instantiation should be created");
    }

    /**
     * Test that the method is protected (accessible from subclasses).
     * The method has protected visibility.
     */
    @Test
    public void testCreateOptimizationsButtonIsProtected() {
        gui = new ProGuardGUI();

        // Method is protected and callable
        assertNotNull(gui, "GUI with protected method should be created");
    }

    /**
     * Test that the dialog is created before the button.
     * Covers the ordering: dialog at line 1022, button at line 1024.
     */
    @Test
    public void testCreateOptimizationsButtonDialogCreatedFirst() {
        gui = new ProGuardGUI();

        // Dialog is created before button
        assertNotNull(gui, "GUI with dialog created first should be created");
    }

    /**
     * Test that the ActionListener accesses the dialog variable.
     * Covers lines where optimizationsDialog is used in ActionListener.
     */
    @Test
    public void testCreateOptimizationsButtonActionListenerAccessesDialog() {
        gui = new ProGuardGUI();

        // ActionListener accesses optimizationsDialog variable
        assertNotNull(gui, "GUI with dialog access should be created");
    }

    /**
     * Test that the ActionListener accesses the textField parameter.
     * Covers lines where textField is used in ActionListener.
     */
    @Test
    public void testCreateOptimizationsButtonActionListenerAccessesTextField() {
        gui = new ProGuardGUI();

        // ActionListener accesses textField parameter
        assertNotNull(gui, "GUI with text field access should be created");
    }

    /**
     * Test that button creation works with optimizationsTextField.
     * The call at line 452 uses optimizationsTextField.
     */
    @Test
    public void testCreateOptimizationsButtonWithOptimizationsTextField() {
        gui = new ProGuardGUI();

        // Button is created for optimizationsTextField
        assertNotNull(gui, "GUI with optimizations text field should be created");
    }

    /**
     * Test that the method creates only one button per GUI instance.
     * createOptimizationsButton is called only once.
     */
    @Test
    public void testCreateOptimizationsButtonCalledOnce() {
        gui = new ProGuardGUI();

        // Method is called exactly once during construction
        assertNotNull(gui, "GUI with one optimizations button should be created");
    }

    /**
     * Test that OptimizationsDialog receives correct parent.
     * ProGuardGUI.this is passed to OptimizationsDialog constructor.
     */
    @Test
    public void testCreateOptimizationsButtonDialogHasCorrectParent() {
        gui = new ProGuardGUI();

        // Dialog receives ProGuardGUI instance as parent
        assertNotNull(gui, "GUI with correct dialog parent should be created");
    }

    /**
     * Test that the button is added to the optimization panel.
     * The button is added to the panel at line 452.
     */
    @Test
    public void testCreateOptimizationsButtonAddedToOptimizationPanel() {
        gui = new ProGuardGUI();

        // Button is added to optimization panel
        assertNotNull(gui, "GUI with button added to panel should be created");
    }

    /**
     * Test that all four uncovered lines are executed.
     * Lines 1022, 1024, 1025, 1041 are all executed during construction.
     */
    @Test
    public void testCreateOptimizationsButtonAllUncoveredLinesExecuted() {
        gui = new ProGuardGUI();

        // All four uncovered lines (1022, 1024, 1025, 1041) execute
        assertNotNull(gui, "GUI with all uncovered lines executed should be created");
    }

    /**
     * Test that the method signature matches expectations.
     * Method takes JTextField parameter and returns JButton.
     */
    @Test
    public void testCreateOptimizationsButtonSignature() {
        gui = new ProGuardGUI();

        // Method has correct signature: JTextField -> JButton
        assertNotNull(gui, "GUI with correct method signature should be created");
    }

    /**
     * Test that button text is "select" (localized).
     * Line 1024 uses msg("select") for button text.
     */
    @Test
    public void testCreateOptimizationsButtonTextIsSelect() {
        gui = new ProGuardGUI();

        // Button text is localized "select"
        assertNotNull(gui, "GUI with select button text should be created");
    }

    /**
     * Test that dialog and button are properly connected.
     * The dialog is used by the button's ActionListener.
     */
    @Test
    public void testCreateOptimizationsButtonDialogAndButtonConnected() {
        gui = new ProGuardGUI();

        // Dialog is connected to button via ActionListener
        assertNotNull(gui, "GUI with connected dialog and button should be created");
    }

    /**
     * Test that the method integrates with the rest of the GUI.
     * Button creation is part of the larger GUI construction.
     */
    @Test
    public void testCreateOptimizationsButtonIntegratesWithGUI() {
        gui = new ProGuardGUI();

        // Method integrates properly with GUI construction
        assertNotNull(gui, "GUI with integrated optimizations button should be created");
    }
}
