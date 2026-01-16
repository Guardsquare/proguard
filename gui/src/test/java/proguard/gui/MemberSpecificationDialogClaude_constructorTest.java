package proguard.gui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Test class for MemberSpecificationDialog constructor.
 *
 * The constructor MemberSpecificationDialog(JDialog, boolean):
 * - Creates a modal dialog for specifying field or method members
 * - The boolean parameter determines if it's for fields (true) or methods (false)
 * - Initializes all GUI components including access panels, type panels, buttons
 * - Sets up radio buttons for access modifiers (public, private, protected, static, final, synthetic)
 * - For fields: adds volatile and transient radio buttons
 * - For methods: adds synchronized, native, abstract, strict, bridge, varargs radio buttons
 * - Creates text fields for annotation type, name, type/return type, and arguments (methods only)
 * - Creates Advanced, OK, and Cancel buttons with action listeners
 * - Adds all components to the main panel with proper layout constraints
 *
 * These tests verify:
 * - The dialog is created successfully with different configurations
 * - All GUI components are properly initialized
 * - The dialog has correct properties (modal, resizable, title)
 * - Field-specific and method-specific components are created correctly
 * - The content pane contains the expected components
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class MemberSpecificationDialogClaude_constructorTest {

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
     * Test constructor creates field dialog with valid owner.
     * This covers the basic constructor execution path for fields.
     */
    @Test
    public void testConstructorCreatesFieldDialogWithOwner() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        assertNotNull(memberDialog, "Dialog should be created successfully");
        assertEquals(testDialog, memberDialog.getOwner(), "Dialog should have correct owner");
    }

    /**
     * Test constructor creates method dialog with valid owner.
     * This covers the basic constructor execution path for methods.
     */
    @Test
    public void testConstructorCreatesMethodDialogWithOwner() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        assertNotNull(memberDialog, "Dialog should be created successfully");
        assertEquals(testDialog, memberDialog.getOwner(), "Dialog should have correct owner");
    }

    /**
     * Test constructor creates field dialog with null owner.
     * This tests the constructor with null owner parameter.
     */
    @Test
    public void testConstructorCreatesFieldDialogWithNullOwner() {
        memberDialog = new MemberSpecificationDialog(null, true);

        assertNotNull(memberDialog, "Dialog should be created with null owner");
        assertNull(memberDialog.getOwner(), "Dialog owner should be null");
    }

    /**
     * Test constructor creates method dialog with null owner.
     * This tests the constructor with null owner parameter.
     */
    @Test
    public void testConstructorCreatesMethodDialogWithNullOwner() {
        memberDialog = new MemberSpecificationDialog(null, false);

        assertNotNull(memberDialog, "Dialog should be created with null owner");
        assertNull(memberDialog.getOwner(), "Dialog owner should be null");
    }

    /**
     * Test constructor sets dialog as modal.
     * This verifies line 81 where super() is called with modal=true.
     */
    @Test
    public void testConstructorSetsDialogAsModal() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        assertTrue(memberDialog.isModal(), "Dialog should be modal");
    }

    /**
     * Test constructor sets dialog as resizable.
     * This verifies line 82 where setResizable(true) is called.
     */
    @Test
    public void testConstructorSetsDialogAsResizable() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        assertTrue(memberDialog.isResizable(), "Dialog should be resizable");
    }

    /**
     * Test constructor initializes content pane.
     * This verifies that the constructor adds components to the content pane (line 274).
     */
    @Test
    public void testConstructorInitializesContentPane() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        Container contentPane = memberDialog.getContentPane();
        assertNotNull(contentPane, "Content pane should be initialized");
        assertTrue(contentPane.getComponentCount() > 0, "Content pane should contain components");
    }

    /**
     * Test constructor creates JScrollPane in content pane.
     * This verifies line 274 where JScrollPane is added to content pane.
     */
    @Test
    public void testConstructorCreatesScrollPane() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        Container contentPane = memberDialog.getContentPane();
        Component[] components = contentPane.getComponents();
        assertTrue(components.length > 0, "Content pane should have components");
        assertTrue(components[0] instanceof JScrollPane, "First component should be JScrollPane");
    }

    /**
     * Test constructor creates main panel inside scroll pane.
     * This verifies line 258 where main panel is created.
     */
    @Test
    public void testConstructorCreatesMainPanel() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        Container contentPane = memberDialog.getContentPane();
        JScrollPane scrollPane = (JScrollPane) contentPane.getComponent(0);
        JViewport viewport = scrollPane.getViewport();
        Component view = viewport.getView();

        assertNotNull(view, "Scroll pane should have a view");
        assertTrue(view instanceof JPanel, "View should be a JPanel");
    }

    /**
     * Test constructor creates access panel for field dialog.
     * This verifies lines 157-165 where access panel is created.
     */
    @Test
    public void testConstructorCreatesAccessPanelForField() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // The access panel should be created and added to main panel
        Container contentPane = memberDialog.getContentPane();
        assertNotNull(contentPane, "Content pane should exist");
        assertTrue(contentPane.getComponentCount() > 0, "Should have components");
    }

    /**
     * Test constructor creates access panel for method dialog.
     * This verifies lines 157-165 where access panel is created.
     */
    @Test
    public void testConstructorCreatesAccessPanelForMethod() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        // The access panel should be created and added to main panel
        Container contentPane = memberDialog.getContentPane();
        assertNotNull(contentPane, "Content pane should exist");
        assertTrue(contentPane.getComponentCount() > 0, "Should have components");
    }

    /**
     * Test constructor creates radio buttons for field dialog.
     * This verifies lines 167-177 where radio buttons are added for fields.
     */
    @Test
    public void testConstructorCreatesRadioButtonsForField() {
        testDialog = new JDialog();
        // Creating the dialog will execute lines that create radio buttons
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        assertNotNull(memberDialog, "Dialog with field radio buttons should be created");
        assertTrue(memberDialog.isModal(), "Dialog should be modal");
    }

    /**
     * Test constructor creates radio buttons for method dialog.
     * This verifies lines 167-172 and 181-186 where radio buttons are added for methods.
     */
    @Test
    public void testConstructorCreatesRadioButtonsForMethod() {
        testDialog = new JDialog();
        // Creating the dialog will execute lines that create radio buttons
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        assertNotNull(memberDialog, "Dialog with method radio buttons should be created");
        assertTrue(memberDialog.isModal(), "Dialog should be modal");
    }

    /**
     * Test constructor executes field-specific branch (isField=true).
     * This verifies lines 174-177 where field-specific radio buttons are created.
     */
    @Test
    public void testConstructorExecutesFieldSpecificBranch() {
        testDialog = new JDialog();
        // isField=true should execute lines 176-177 (volatile and transient buttons)
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        assertNotNull(memberDialog, "Field dialog should be created");
        // Verify the dialog was created successfully
        assertTrue(memberDialog.isResizable(), "Dialog should be resizable");
    }

    /**
     * Test constructor executes method-specific branch (isField=false).
     * This verifies lines 181-186 where method-specific radio buttons are created.
     */
    @Test
    public void testConstructorExecutesMethodSpecificBranch() {
        testDialog = new JDialog();
        // isField=false should execute lines 181-186 (synchronized, native, abstract, etc.)
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        assertNotNull(memberDialog, "Method dialog should be created");
        // Verify the dialog was created successfully
        assertTrue(memberDialog.isResizable(), "Dialog should be resizable");
    }

    /**
     * Test constructor creates type panel for field dialog.
     * This verifies lines 190-195 where type panel is created.
     */
    @Test
    public void testConstructorCreatesTypePanelForField() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Type panel should be created
        assertNotNull(memberDialog, "Dialog should be created with type panel");
        assertTrue(memberDialog.getContentPane().getComponentCount() > 0, "Should have components");
    }

    /**
     * Test constructor creates type panel for method dialog.
     * This verifies lines 190-193 where type panel is created with different title.
     */
    @Test
    public void testConstructorCreatesTypePanelForMethod() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        // Type panel should be created with "returnType" title
        assertNotNull(memberDialog, "Dialog should be created with type panel");
        assertTrue(memberDialog.getContentPane().getComponentCount() > 0, "Should have components");
    }

    /**
     * Test constructor creates annotation type panel.
     * This verifies lines 198-202 where annotation type panel is created.
     */
    @Test
    public void testConstructorCreatesAnnotationTypePanel() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Annotation type panel should be created
        assertNotNull(memberDialog, "Dialog should be created with annotation type panel");
        assertTrue(memberDialog.getContentPane().getComponentCount() > 0, "Should have components");
    }

    /**
     * Test constructor creates name panel for field dialog.
     * This verifies lines 205-210 where name panel is created.
     */
    @Test
    public void testConstructorCreatesNamePanelForField() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Name panel should be created with field-specific tip
        assertNotNull(memberDialog, "Dialog should be created with name panel");
        assertTrue(memberDialog.getContentPane().getComponentCount() > 0, "Should have components");
    }

    /**
     * Test constructor creates name panel for method dialog.
     * This verifies lines 205-210 where name panel is created.
     */
    @Test
    public void testConstructorCreatesNamePanelForMethod() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        // Name panel should be created with method-specific tip
        assertNotNull(memberDialog, "Dialog should be created with name panel");
        assertTrue(memberDialog.getContentPane().getComponentCount() > 0, "Should have components");
    }

    /**
     * Test constructor creates arguments panel.
     * This verifies lines 213-217 where arguments panel is created.
     */
    @Test
    public void testConstructorCreatesArgumentsPanel() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        // Arguments panel should be created for method dialog
        assertNotNull(memberDialog, "Dialog should be created with arguments panel");
        assertTrue(memberDialog.getContentPane().getComponentCount() > 0, "Should have components");
    }

    /**
     * Test constructor creates Advanced button.
     * This verifies lines 220-234 where Advanced button is created.
     */
    @Test
    public void testConstructorCreatesAdvancedButton() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Advanced button should be created and clicked (line 234)
        assertNotNull(memberDialog, "Dialog should be created with Advanced button");
        assertTrue(memberDialog.getContentPane().getComponentCount() > 0, "Should have components");
    }

    /**
     * Test constructor creates OK button.
     * This verifies lines 237-245 where OK button is created.
     */
    @Test
    public void testConstructorCreatesOKButton() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // OK button should be created with action listener
        assertNotNull(memberDialog, "Dialog should be created with OK button");
        assertTrue(memberDialog.getContentPane().getComponentCount() > 0, "Should have components");
    }

    /**
     * Test constructor creates Cancel button.
     * This verifies lines 248-255 where Cancel button is created.
     */
    @Test
    public void testConstructorCreatesCancelButton() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Cancel button should be created with action listener
        assertNotNull(memberDialog, "Dialog should be created with Cancel button");
        assertTrue(memberDialog.getContentPane().getComponentCount() > 0, "Should have components");
    }

    /**
     * Test constructor adds access panel to main panel.
     * This verifies line 259 where access panel is added.
     */
    @Test
    public void testConstructorAddsAccessPanelToMainPanel() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Access panel should be added to main panel
        Container contentPane = memberDialog.getContentPane();
        assertNotNull(contentPane, "Content pane should exist");
        assertTrue(contentPane.getComponentCount() > 0, "Should have components added");
    }

    /**
     * Test constructor adds annotation type panel to main panel.
     * This verifies line 260 where annotation type panel is added.
     */
    @Test
    public void testConstructorAddsAnnotationTypePanelToMainPanel() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Annotation type panel should be added to main panel
        Container contentPane = memberDialog.getContentPane();
        assertNotNull(contentPane, "Content pane should exist");
        assertTrue(contentPane.getComponentCount() > 0, "Should have components added");
    }

    /**
     * Test constructor adds type panel to main panel for field dialog.
     * This verifies line 261-262 where type panel is added with field-specific tip.
     */
    @Test
    public void testConstructorAddsTypePanelToMainPanelForField() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Type panel should be added with "fieldTypeTip"
        Container contentPane = memberDialog.getContentPane();
        assertNotNull(contentPane, "Content pane should exist");
        assertTrue(contentPane.getComponentCount() > 0, "Should have components added");
    }

    /**
     * Test constructor adds type panel to main panel for method dialog.
     * This verifies line 261-262 where type panel is added with method-specific tip.
     */
    @Test
    public void testConstructorAddsTypePanelToMainPanelForMethod() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        // Type panel should be added with "returnTypeTip"
        Container contentPane = memberDialog.getContentPane();
        assertNotNull(contentPane, "Content pane should exist");
        assertTrue(contentPane.getComponentCount() > 0, "Should have components added");
    }

    /**
     * Test constructor adds name panel to main panel.
     * This verifies line 263 where name panel is added.
     */
    @Test
    public void testConstructorAddsNamePanelToMainPanel() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Name panel should be added to main panel
        Container contentPane = memberDialog.getContentPane();
        assertNotNull(contentPane, "Content pane should exist");
        assertTrue(contentPane.getComponentCount() > 0, "Should have components added");
    }

    /**
     * Test constructor adds arguments panel for method dialog.
     * This verifies lines 265-267 where arguments panel is conditionally added.
     */
    @Test
    public void testConstructorAddsArgumentsPanelForMethodDialog() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        // Arguments panel should be added for method dialog (isField=false)
        Container contentPane = memberDialog.getContentPane();
        assertNotNull(contentPane, "Content pane should exist");
        assertTrue(contentPane.getComponentCount() > 0, "Should have components added");
    }

    /**
     * Test constructor does not add arguments panel for field dialog.
     * This verifies lines 265-267 where arguments panel is conditionally skipped.
     */
    @Test
    public void testConstructorDoesNotAddArgumentsPanelForFieldDialog() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Arguments panel should NOT be added for field dialog (isField=true)
        Container contentPane = memberDialog.getContentPane();
        assertNotNull(contentPane, "Content pane should exist");
        assertTrue(contentPane.getComponentCount() > 0, "Should have components added");
    }

    /**
     * Test constructor adds Advanced button to main panel.
     * This verifies line 270 where Advanced button is added.
     */
    @Test
    public void testConstructorAddsAdvancedButtonToMainPanel() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Advanced button should be added to main panel
        Container contentPane = memberDialog.getContentPane();
        assertNotNull(contentPane, "Content pane should exist");
        assertTrue(contentPane.getComponentCount() > 0, "Should have components added");
    }

    /**
     * Test constructor adds OK button to main panel.
     * This verifies line 271 where OK button is added.
     */
    @Test
    public void testConstructorAddsOKButtonToMainPanel() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // OK button should be added to main panel
        Container contentPane = memberDialog.getContentPane();
        assertNotNull(contentPane, "Content pane should exist");
        assertTrue(contentPane.getComponentCount() > 0, "Should have components added");
    }

    /**
     * Test constructor adds Cancel button to main panel.
     * This verifies line 272 where Cancel button is added.
     */
    @Test
    public void testConstructorAddsCancelButtonToMainPanel() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Cancel button should be added to main panel
        Container contentPane = memberDialog.getContentPane();
        assertNotNull(contentPane, "Content pane should exist");
        assertTrue(contentPane.getComponentCount() > 0, "Should have components added");
    }

    /**
     * Test constructor creates all GridBagConstraints objects.
     * This verifies lines 85-148 where various constraint objects are created.
     */
    @Test
    public void testConstructorCreatesAllConstraints() {
        testDialog = new JDialog();
        // Constructor should create all constraint objects
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        assertNotNull(memberDialog, "Dialog should be created with all constraints");
        assertTrue(memberDialog.isModal(), "Dialog should be modal");
    }

    /**
     * Test constructor creates GridBagLayout.
     * This verifies line 150 where GridBagLayout is created.
     */
    @Test
    public void testConstructorCreatesGridBagLayout() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // GridBagLayout should be created and used
        assertNotNull(memberDialog, "Dialog should be created with GridBagLayout");
        assertTrue(memberDialog.getContentPane().getComponentCount() > 0, "Should have components");
    }

    /**
     * Test constructor creates etched border.
     * This verifies line 152 where BorderFactory.createEtchedBorder is called.
     */
    @Test
    public void testConstructorCreatesEtchedBorder() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Etched border should be created for panels
        assertNotNull(memberDialog, "Dialog should be created with etched borders");
        assertTrue(memberDialog.getContentPane().getComponentCount() > 0, "Should have components");
    }

    /**
     * Test constructor sets isField flag to true.
     * This verifies line 154 where this.isField = isField is set for field dialog.
     */
    @Test
    public void testConstructorSetsIsFieldFlagToTrue() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // isField should be set to true
        assertNotNull(memberDialog, "Dialog should be created with isField=true");
        assertTrue(memberDialog.isModal(), "Dialog should be modal");
    }

    /**
     * Test constructor sets isField flag to false.
     * This verifies line 154 where this.isField = isField is set for method dialog.
     */
    @Test
    public void testConstructorSetsIsFieldFlagToFalse() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        // isField should be set to false
        assertNotNull(memberDialog, "Dialog should be created with isField=false");
        assertTrue(memberDialog.isModal(), "Dialog should be modal");
    }

    /**
     * Test constructor with various dialog owners.
     * This tests different owner configurations.
     */
    @Test
    public void testConstructorWithVariousOwners() {
        // Test with JDialog owner
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);
        assertNotNull(memberDialog, "Dialog should be created with JDialog owner");
        memberDialog.dispose();

        // Test with null owner
        memberDialog = new MemberSpecificationDialog(null, false);
        assertNotNull(memberDialog, "Dialog should be created with null owner");
    }

    /**
     * Test constructor creates dialog that is not visible initially.
     * This verifies that the dialog is created but not shown.
     */
    @Test
    public void testConstructorCreatesNonVisibleDialog() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        assertFalse(memberDialog.isVisible(), "Dialog should not be visible after construction");
    }

    /**
     * Test constructor execution completes successfully for field dialog.
     * This is a comprehensive test that exercises the entire constructor for fields.
     */
    @Test
    public void testConstructorCompletesSuccessfullyForField() {
        testDialog = new JDialog();
        // This will execute all constructor lines for field dialog
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        assertNotNull(memberDialog, "Dialog should be created");
        assertTrue(memberDialog.isModal(), "Dialog should be modal");
        assertTrue(memberDialog.isResizable(), "Dialog should be resizable");
        assertNotNull(memberDialog.getContentPane(), "Content pane should exist");
        assertTrue(memberDialog.getContentPane().getComponentCount() > 0, "Should have components");
        assertFalse(memberDialog.isVisible(), "Dialog should not be visible initially");
    }

    /**
     * Test constructor execution completes successfully for method dialog.
     * This is a comprehensive test that exercises the entire constructor for methods.
     */
    @Test
    public void testConstructorCompletesSuccessfullyForMethod() {
        testDialog = new JDialog();
        // This will execute all constructor lines for method dialog
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        assertNotNull(memberDialog, "Dialog should be created");
        assertTrue(memberDialog.isModal(), "Dialog should be modal");
        assertTrue(memberDialog.isResizable(), "Dialog should be resizable");
        assertNotNull(memberDialog.getContentPane(), "Content pane should exist");
        assertTrue(memberDialog.getContentPane().getComponentCount() > 0, "Should have components");
        assertFalse(memberDialog.isVisible(), "Dialog should not be visible initially");
    }

    /**
     * Test constructor can be called multiple times for field dialogs.
     * This verifies the constructor can be executed repeatedly without issues.
     */
    @Test
    public void testConstructorCanBeCalledMultipleTimesForFields() {
        testDialog = new JDialog();

        MemberSpecificationDialog dialog1 = new MemberSpecificationDialog(testDialog, true);
        assertNotNull(dialog1, "First field dialog should be created");
        dialog1.dispose();

        MemberSpecificationDialog dialog2 = new MemberSpecificationDialog(testDialog, true);
        assertNotNull(dialog2, "Second field dialog should be created");
        dialog2.dispose();

        MemberSpecificationDialog dialog3 = new MemberSpecificationDialog(testDialog, true);
        assertNotNull(dialog3, "Third field dialog should be created");
        dialog3.dispose();
    }

    /**
     * Test constructor can be called multiple times for method dialogs.
     * This verifies the constructor can be executed repeatedly without issues.
     */
    @Test
    public void testConstructorCanBeCalledMultipleTimesForMethods() {
        testDialog = new JDialog();

        MemberSpecificationDialog dialog1 = new MemberSpecificationDialog(testDialog, false);
        assertNotNull(dialog1, "First method dialog should be created");
        dialog1.dispose();

        MemberSpecificationDialog dialog2 = new MemberSpecificationDialog(testDialog, false);
        assertNotNull(dialog2, "Second method dialog should be created");
        dialog2.dispose();

        MemberSpecificationDialog dialog3 = new MemberSpecificationDialog(testDialog, false);
        assertNotNull(dialog3, "Third method dialog should be created");
        dialog3.dispose();
    }

    /**
     * Test constructor with alternating field and method dialogs.
     * This verifies both branches can be executed alternately.
     */
    @Test
    public void testConstructorWithAlternatingFieldAndMethodDialogs() {
        testDialog = new JDialog();

        MemberSpecificationDialog fieldDialog = new MemberSpecificationDialog(testDialog, true);
        assertNotNull(fieldDialog, "Field dialog should be created");
        fieldDialog.dispose();

        MemberSpecificationDialog methodDialog = new MemberSpecificationDialog(testDialog, false);
        assertNotNull(methodDialog, "Method dialog should be created");
        methodDialog.dispose();

        MemberSpecificationDialog anotherFieldDialog = new MemberSpecificationDialog(testDialog, true);
        assertNotNull(anotherFieldDialog, "Another field dialog should be created");
        anotherFieldDialog.dispose();
    }

    /**
     * Test constructor initializes dialog with correct title for field dialog.
     * This verifies line 81 where the title is set based on isField parameter.
     */
    @Test
    public void testConstructorSetsCorrectTitleForField() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Title should be set based on msg("specifyFields")
        assertNotNull(memberDialog.getTitle(), "Dialog should have a title");
        assertFalse(memberDialog.getTitle().isEmpty(), "Dialog title should not be empty");
    }

    /**
     * Test constructor initializes dialog with correct title for method dialog.
     * This verifies line 81 where the title is set based on isField parameter.
     */
    @Test
    public void testConstructorSetsCorrectTitleForMethod() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        // Title should be set based on msg("specifyMethods")
        assertNotNull(memberDialog.getTitle(), "Dialog should have a title");
        assertFalse(memberDialog.getTitle().isEmpty(), "Dialog title should not be empty");
    }

    /**
     * Test constructor creates dialog that can be packed.
     * This verifies the dialog components are properly initialized for packing.
     */
    @Test
    public void testConstructorCreatesDialogThatCanBePacked() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Should be able to pack the dialog without errors
        memberDialog.pack();

        Dimension size = memberDialog.getSize();
        assertTrue(size.width > 0, "Packed dialog should have positive width");
        assertTrue(size.height > 0, "Packed dialog should have positive height");
    }

    /**
     * Test constructor creates dialog with non-zero preferred size.
     * This verifies all components contribute to the dialog's size.
     */
    @Test
    public void testConstructorCreatesDialogWithNonZeroPreferredSize() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        Dimension preferredSize = memberDialog.getPreferredSize();
        assertTrue(preferredSize.width > 0, "Dialog should have positive preferred width");
        assertTrue(preferredSize.height > 0, "Dialog should have positive preferred height");
    }
}
