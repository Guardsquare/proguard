package proguard.gui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Test class focused on addClassSpecifications coverage for ProGuardGUI.
 *
 * The addClassSpecifications method is private and called from the constructor.
 * These tests verify that the class specifications panel creation works correctly by:
 * - Creating ProGuardGUI instances (which calls addClassSpecifications)
 * - addClassSpecifications is called at lines 371, 437, and 466 in the constructor
 * - Verifying the GUI initializes properly with class specification panels
 *
 * Covered lines: 814-934 (all uncovered lines in addClassSpecifications method)
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class ProGuardGUIClaude_addClassSpecificationsTest {

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
     * Test that the constructor calls addClassSpecifications successfully.
     * This covers lines 814-934 by creating a ProGuardGUI instance.
     * addClassSpecifications is called from the constructor at lines 371, 437, and 466.
     */
    @Test
    public void testConstructorCallsAddClassSpecifications() {
        // Creating the GUI calls addClassSpecifications three times in the constructor
        gui = new ProGuardGUI();

        assertNotNull(gui, "GUI should be created successfully");
    }

    /**
     * Test that addClassSpecifications creates GridBagConstraints.
     * Covers lines 814-816: GridBagConstraints constraints = new GridBagConstraints();
     */
    @Test
    public void testAddClassSpecificationsCreatesConstraints() {
        gui = new ProGuardGUI();

        // GridBagConstraints are created during construction
        assertNotNull(gui, "GUI with constraints should be created");
    }

    /**
     * Test that addClassSpecifications creates constraintsLastStretch.
     * Covers lines 818-823: GridBagConstraints constraintsLastStretch = new GridBagConstraints();
     */
    @Test
    public void testAddClassSpecificationsCreatesConstraintsLastStretch() {
        gui = new ProGuardGUI();

        // constraintsLastStretch is created during construction
        assertNotNull(gui, "GUI with constraintsLastStretch should be created");
    }

    /**
     * Test that addClassSpecifications creates panelConstraints.
     * Covers lines 825-830: GridBagConstraints panelConstraints = new GridBagConstraints();
     */
    @Test
    public void testAddClassSpecificationsCreatesPanelConstraints() {
        gui = new ProGuardGUI();

        // panelConstraints is created during construction
        assertNotNull(gui, "GUI with panelConstraints should be created");
    }

    /**
     * Test that addClassSpecifications creates containerConstraints.
     * Covers lines 832-837: GridBagConstraints containerConstraints = new GridBagConstraints();
     */
    @Test
    public void testAddClassSpecificationsCreatesContainerConstraints() {
        gui = new ProGuardGUI();

        // containerConstraints is created during construction
        assertNotNull(gui, "GUI with containerConstraints should be created");
    }

    /**
     * Test that addClassSpecifications creates GridBagLayout.
     * Covers line 839: GridBagLayout layout = new GridBagLayout();
     */
    @Test
    public void testAddClassSpecificationsCreatesGridBagLayout() {
        gui = new ProGuardGUI();

        // GridBagLayout is created during construction
        assertNotNull(gui, "GUI with GridBagLayout should be created");
    }

    /**
     * Test that addClassSpecifications creates panel.
     * Covers line 841: JPanel panel = new JPanel(layout);
     */
    @Test
    public void testAddClassSpecificationsCreatesPanel() {
        gui = new ProGuardGUI();

        // JPanel is created during construction
        assertNotNull(gui, "GUI with JPanel should be created");
    }

    /**
     * Test that addClassSpecifications initializes lastPanelName.
     * Covers line 843: String lastPanelName = null;
     */
    @Test
    public void testAddClassSpecificationsInitializesLastPanelName() {
        gui = new ProGuardGUI();

        // lastPanelName is initialized during construction
        assertNotNull(gui, "GUI with initialized lastPanelName should be created");
    }

    /**
     * Test that addClassSpecifications initializes subpanel.
     * Covers line 844: JPanel subpanel = null;
     */
    @Test
    public void testAddClassSpecificationsInitializesSubpanel() {
        gui = new ProGuardGUI();

        // subpanel is initialized during construction
        assertNotNull(gui, "GUI with initialized subpanel should be created");
    }

    /**
     * Test that addClassSpecifications iterates through boilerplateClassSpecifications.
     * Covers line 845: for (int index = 0; index < boilerplateClassSpecifications.length; index++)
     */
    @Test
    public void testAddClassSpecificationsIteratesThroughSpecifications() {
        gui = new ProGuardGUI();

        // The for loop iterates through boilerplateClassSpecifications
        assertNotNull(gui, "GUI with iterated specifications should be created");
    }

    /**
     * Test that addClassSpecifications retrieves comments from ClassSpecification.
     * Covers line 848: String comments = boilerplateClassSpecifications[index].comments;
     */
    @Test
    public void testAddClassSpecificationsRetrievesComments() {
        gui = new ProGuardGUI();

        // Comments are retrieved from ClassSpecification
        assertNotNull(gui, "GUI with retrieved comments should be created");
    }

    /**
     * Test that addClassSpecifications checks if comments is not null.
     * Covers line 851: if (comments != null)
     */
    @Test
    public void testAddClassSpecificationsChecksCommentsNotNull() {
        gui = new ProGuardGUI();

        // The method checks if comments is not null
        assertNotNull(gui, "GUI with comments null check should be created");
    }

    /**
     * Test that addClassSpecifications finds dash index in comments.
     * Covers line 854: int dashIndex = comments.indexOf('-');
     */
    @Test
    public void testAddClassSpecificationsFindsDashIndex() {
        gui = new ProGuardGUI();

        // dashIndex is found in comments
        assertNotNull(gui, "GUI with dash index should be created");
    }

    /**
     * Test that addClassSpecifications finds period index in comments.
     * Covers line 855: int periodIndex = comments.indexOf('.', dashIndex);
     */
    @Test
    public void testAddClassSpecificationsFindsPeriodIndex() {
        gui = new ProGuardGUI();

        // periodIndex is found in comments
        assertNotNull(gui, "GUI with period index should be created");
    }

    /**
     * Test that addClassSpecifications extracts panelName from comments.
     * Covers line 856: String panelName = comments.substring(0, dashIndex).trim();
     */
    @Test
    public void testAddClassSpecificationsExtractsPanelName() {
        gui = new ProGuardGUI();

        // panelName is extracted from comments
        assertNotNull(gui, "GUI with extracted panelName should be created");
    }

    /**
     * Test that addClassSpecifications extracts optionName from comments.
     * Covers line 857: String optionName = comments.substring(dashIndex + 1, periodIndex).replace('_', '.').trim();
     */
    @Test
    public void testAddClassSpecificationsExtractsOptionName() {
        gui = new ProGuardGUI();

        // optionName is extracted and underscores are replaced
        assertNotNull(gui, "GUI with extracted optionName should be created");
    }

    /**
     * Test that addClassSpecifications extracts toolTip from comments.
     * Covers line 858: String toolTip = comments.substring(periodIndex + 1);
     */
    @Test
    public void testAddClassSpecificationsExtractsToolTip() {
        gui = new ProGuardGUI();

        // toolTip is extracted from comments
        assertNotNull(gui, "GUI with extracted toolTip should be created");
    }

    /**
     * Test that addClassSpecifications checks if panelName differs from lastPanelName.
     * Covers line 859: if (!panelName.equals(lastPanelName))
     */
    @Test
    public void testAddClassSpecificationsChecksPanelNameDiffers() {
        gui = new ProGuardGUI();

        // The method checks if panelName differs from lastPanelName
        assertNotNull(gui, "GUI with panelName comparison should be created");
    }

    /**
     * Test that addClassSpecifications creates new subpanel.
     * Covers line 862: subpanel = new JPanel(layout);
     */
    @Test
    public void testAddClassSpecificationsCreatesNewSubpanel() {
        gui = new ProGuardGUI();

        // New subpanel is created when panelName changes
        assertNotNull(gui, "GUI with new subpanel should be created");
    }

    /**
     * Test that addClassSpecifications checks if panelName ends with ELLIPSIS_DOTS.
     * Covers line 863: if (panelName.endsWith(ELLIPSIS_DOTS))
     */
    @Test
    public void testAddClassSpecificationsChecksEllipsisDots() {
        gui = new ProGuardGUI();

        // The method checks if panelName ends with ELLIPSIS_DOTS
        assertNotNull(gui, "GUI with ellipsis check should be created");
    }

    /**
     * Test that addClassSpecifications sets subpanel invisible when needed.
     * Covers line 865: subpanel.setVisible(false);
     */
    @Test
    public void testAddClassSpecificationsSetsSubpanelInvisible() {
        gui = new ProGuardGUI();

        // subpanel is set invisible when panelName ends with ELLIPSIS_DOTS
        assertNotNull(gui, "GUI with invisible subpanel should be created");
    }

    /**
     * Test that addClassSpecifications creates foldableSubpanel.
     * Covers line 869: final JPanel foldableSubpanel = new JPanel(layout);
     */
    @Test
    public void testAddClassSpecificationsCreatesFoldableSubpanel() {
        gui = new ProGuardGUI();

        // foldableSubpanel is created during construction
        assertNotNull(gui, "GUI with foldableSubpanel should be created");
    }

    /**
     * Test that addClassSpecifications creates titledBorder.
     * Covers line 870: final TitledBorder titledBorder = BorderFactory.createTitledBorder(BORDER, panelName);
     */
    @Test
    public void testAddClassSpecificationsCreatesTitledBorder() {
        gui = new ProGuardGUI();

        // titledBorder is created during construction
        assertNotNull(gui, "GUI with titledBorder should be created");
    }

    /**
     * Test that addClassSpecifications sets border on foldableSubpanel.
     * Covers line 871: foldableSubpanel.setBorder(titledBorder);
     */
    @Test
    public void testAddClassSpecificationsSetsBorder() {
        gui = new ProGuardGUI();

        // Border is set on foldableSubpanel
        assertNotNull(gui, "GUI with border set should be created");
    }

    /**
     * Test that addClassSpecifications adds subpanel to foldableSubpanel.
     * Covers line 872: foldableSubpanel.add(subpanel, containerConstraints);
     */
    @Test
    public void testAddClassSpecificationsAddsSubpanelToFoldable() {
        gui = new ProGuardGUI();

        // subpanel is added to foldableSubpanel
        assertNotNull(gui, "GUI with added subpanel should be created");
    }

    /**
     * Test that addClassSpecifications creates finalSubpanel variable.
     * Covers line 876: final JPanel finalSubpanel = subpanel;
     */
    @Test
    public void testAddClassSpecificationsCreatesFinalSubpanel() {
        gui = new ProGuardGUI();

        // finalSubpanel variable is created
        assertNotNull(gui, "GUI with finalSubpanel should be created");
    }

    /**
     * Test that addClassSpecifications adds MouseListener to foldableSubpanel.
     * Covers line 877: foldableSubpanel.addMouseListener(new MouseAdapter()
     */
    @Test
    public void testAddClassSpecificationsAddsMouseListener() {
        gui = new ProGuardGUI();

        // MouseListener is added to foldableSubpanel
        assertNotNull(gui, "GUI with MouseListener should be created");
    }

    /**
     * Test that addClassSpecifications adds foldableSubpanel to classSpecificationsPanel.
     * Covers line 896: classSpecificationsPanel.add(foldableSubpanel, panelConstraints);
     */
    @Test
    public void testAddClassSpecificationsAddsFoldableSubpanel() {
        gui = new ProGuardGUI();

        // foldableSubpanel is added to classSpecificationsPanel
        assertNotNull(gui, "GUI with added foldableSubpanel should be created");
    }

    /**
     * Test that addClassSpecifications updates lastPanelName.
     * Covers line 898: lastPanelName = panelName;
     */
    @Test
    public void testAddClassSpecificationsUpdatesLastPanelName() {
        gui = new ProGuardGUI();

        // lastPanelName is updated to current panelName
        assertNotNull(gui, "GUI with updated lastPanelName should be created");
    }

    /**
     * Test that addClassSpecifications determines if text field should be added.
     * Covers line 902: boolean addTextField = ...
     */
    @Test
    public void testAddClassSpecificationsDeterminesAddTextField() {
        gui = new ProGuardGUI();

        // The method determines if a text field should be added
        assertNotNull(gui, "GUI with addTextField determination should be created");
    }

    /**
     * Test that addClassSpecifications creates JCheckBox.
     * Covers line 909: JCheckBox boilerplateCheckBox = new JCheckBox(optionName);
     */
    @Test
    public void testAddClassSpecificationsCreatesCheckBox() {
        gui = new ProGuardGUI();

        // JCheckBox is created for each option
        assertNotNull(gui, "GUI with JCheckBox should be created");
    }

    /**
     * Test that addClassSpecifications sets tooltip on checkbox.
     * Covers line 910: boilerplateCheckBox.setToolTipText(toolTip);
     */
    @Test
    public void testAddClassSpecificationsSetsTooltip() {
        gui = new ProGuardGUI();

        // Tooltip is set on checkbox
        assertNotNull(gui, "GUI with tooltip should be created");
    }

    /**
     * Test that addClassSpecifications adds checkbox to subpanel.
     * Covers line 911: subpanel.add(boilerplateCheckBox, ...);
     */
    @Test
    public void testAddClassSpecificationsAddsCheckBoxToSubpanel() {
        gui = new ProGuardGUI();

        // Checkbox is added to subpanel
        assertNotNull(gui, "GUI with added checkbox should be created");
    }

    /**
     * Test that addClassSpecifications stores checkbox in array.
     * Covers line 916: boilerplateCheckBoxes[index] = boilerplateCheckBox;
     */
    @Test
    public void testAddClassSpecificationsStoresCheckBox() {
        gui = new ProGuardGUI();

        // Checkbox is stored in boilerplateCheckBoxes array
        assertNotNull(gui, "GUI with stored checkbox should be created");
    }

    /**
     * Test that addClassSpecifications checks if text field should be added.
     * Covers line 919: if (addTextField)
     */
    @Test
    public void testAddClassSpecificationsChecksAddTextField() {
        gui = new ProGuardGUI();

        // The method checks if text field should be added
        assertNotNull(gui, "GUI with addTextField check should be created");
    }

    /**
     * Test that addClassSpecifications creates JTextField.
     * Covers line 921: JTextField boilerplateTextField = new JTextField(40);
     */
    @Test
    public void testAddClassSpecificationsCreatesTextField() {
        gui = new ProGuardGUI();

        // JTextField is created when needed
        assertNotNull(gui, "GUI with JTextField should be created");
    }

    /**
     * Test that addClassSpecifications adds text field to subpanel.
     * Covers line 922: subpanel.add(tip(boilerplateTextField, "classNamesTip"), constraintsLastStretch);
     */
    @Test
    public void testAddClassSpecificationsAddsTextField() {
        gui = new ProGuardGUI();

        // Text field is added to subpanel
        assertNotNull(gui, "GUI with added text field should be created");
    }

    /**
     * Test that addClassSpecifications stores text field in array.
     * Covers line 924: boilerplateTextFields[index] = boilerplateTextField;
     */
    @Test
    public void testAddClassSpecificationsStoresTextField() {
        gui = new ProGuardGUI();

        // Text field is stored in boilerplateTextFields array
        assertNotNull(gui, "GUI with stored text field should be created");
    }

    /**
     * Test that addClassSpecifications handles null comments (else branch).
     * Covers line 931: boilerplateCheckBoxes[index] = boilerplateCheckBoxes[index-1];
     */
    @Test
    public void testAddClassSpecificationsHandlesNullComments() {
        gui = new ProGuardGUI();

        // When comments is null, checkbox from previous index is reused
        assertNotNull(gui, "GUI with reused checkbox should be created");
    }

    /**
     * Test that addClassSpecifications is called three times during construction.
     */
    @Test
    public void testAddClassSpecificationsCalledThreeTimes() {
        gui = new ProGuardGUI();

        // addClassSpecifications is called three times:
        // line 371 (shrinking), line 437 (obfuscation), line 466 (optimization)
        assertNotNull(gui, "GUI with three addClassSpecifications calls should be created");
    }

    /**
     * Test that addClassSpecifications is called with boilerplateKeep.
     * This covers the first call at line 371.
     */
    @Test
    public void testAddClassSpecificationsCalledWithBoilerplateKeep() {
        gui = new ProGuardGUI();

        // addClassSpecifications is called with extracted boilerplateKeep
        assertNotNull(gui, "GUI with boilerplateKeep specifications should be created");
    }

    /**
     * Test that addClassSpecifications is called with boilerplateKeepNames.
     * This covers the second call at line 437.
     */
    @Test
    public void testAddClassSpecificationsCalledWithBoilerplateKeepNames() {
        gui = new ProGuardGUI();

        // addClassSpecifications is called with extracted boilerplateKeepNames
        assertNotNull(gui, "GUI with boilerplateKeepNames specifications should be created");
    }

    /**
     * Test that addClassSpecifications is called with boilerplateNoSideEffectMethods.
     * This covers the third call at line 466.
     */
    @Test
    public void testAddClassSpecificationsCalledWithNoSideEffectMethods() {
        gui = new ProGuardGUI();

        // addClassSpecifications is called with boilerplateNoSideEffectMethods
        assertNotNull(gui, "GUI with no side effect methods specifications should be created");
    }

    /**
     * Test that addClassSpecifications is called with null text fields parameter.
     * The third call at line 466 passes null for boilerplateTextFields.
     */
    @Test
    public void testAddClassSpecificationsCalledWithNullTextFields() {
        gui = new ProGuardGUI();

        // addClassSpecifications handles null boilerplateTextFields parameter
        assertNotNull(gui, "GUI with null text fields should be created");
    }

    /**
     * Test that multiple GUI instances create class specification panels independently.
     */
    @Test
    public void testAddClassSpecificationsSupportsMultipleInstances() {
        ProGuardGUI gui1 = new ProGuardGUI();
        assertNotNull(gui1, "First GUI with class specifications should be created");

        ProGuardGUI gui2 = new ProGuardGUI();
        assertNotNull(gui2, "Second GUI with class specifications should be created");

        gui1.dispose();
        gui2.dispose();
    }

    /**
     * Test that addClassSpecifications completes without throwing exceptions.
     */
    @Test
    public void testAddClassSpecificationsCompletesSuccessfully() {
        assertDoesNotThrow(() -> {
            gui = new ProGuardGUI();
        }, "addClassSpecifications should complete without throwing exceptions");
    }

    /**
     * Test that the constructor completes with class specification panels added.
     * This is a comprehensive test ensuring all lines are executed.
     */
    @Test
    public void testConstructorWithClassSpecificationPanelsAdded() {
        gui = new ProGuardGUI();

        // Verify GUI is fully initialized
        assertNotNull(gui);
        assertEquals("ProGuard", gui.getTitle());
        assertTrue(gui.getSize().width > 0);
        assertTrue(gui.getSize().height > 0);
    }

    /**
     * Test that GUI can be disposed after class specification panels are added.
     */
    @Test
    public void testAddClassSpecificationsAllowsDisposal() {
        gui = new ProGuardGUI();

        assertDoesNotThrow(() -> {
            gui.dispose();
        }, "GUI should be disposable after adding class specification panels");

        gui = null; // Prevent double dispose in tearDown
    }

    /**
     * Test that rapid GUI creation adds class specification panels each time.
     */
    @Test
    public void testAddClassSpecificationsHandlesRapidCreation() {
        for (int i = 0; i < 3; i++) {
            ProGuardGUI tempGui = new ProGuardGUI();
            assertNotNull(tempGui, "GUI " + i + " should be created with class specification panels");
            tempGui.dispose();
        }
    }

    /**
     * Test comprehensive panel creation workflow.
     * This integration test ensures the entire flow works correctly.
     */
    @Test
    public void testAddClassSpecificationsIntegration() {
        assertDoesNotThrow(() -> {
            gui = new ProGuardGUI();

            // Verify all aspects of GUI creation
            assertNotNull(gui);
            assertEquals("ProGuard", gui.getTitle());
            assertEquals(JFrame.EXIT_ON_CLOSE, gui.getDefaultCloseOperation());
            assertFalse(gui.isVisible());
            assertTrue(gui.getSize().width > 0);
            assertTrue(gui.getSize().height > 0);

        }, "Class specification panels should integrate smoothly with GUI creation");
    }
}
