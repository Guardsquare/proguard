package proguard.gui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.ClassSpecification;
import proguard.KeepClassSpecification;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Test class for KeepSpecificationsPanel constructor.
 *
 * The constructor KeepSpecificationsPanel(JFrame owner, boolean markClasses, boolean markConditionally,
 * boolean markDescriptorClasses, boolean allowShrinking, boolean allowOptimization, boolean allowObfuscation)
 * performs the following operations (lines that need coverage):
 * - Line 51: Calls super(owner, true, true) to initialize ClassSpecificationsPanel parent
 * - Line 53: Assigns this.markClasses = markClasses
 * - Line 54: Assigns this.markConditionally = markConditionally
 * - Line 55: Assigns this.markDescriptorClasses = markDescriptorClasses
 * - Line 56: Assigns this.allowShrinking = allowShrinking
 * - Line 57: Assigns this.allowOptimization = allowOptimization
 * - Line 58: Assigns this.allowObfuscation = allowObfuscation
 * - Line 59: End of constructor
 *
 * These tests verify:
 * - The panel is properly initialized with different boolean parameter combinations
 * - All six boolean flags are correctly assigned to instance fields
 * - The parent constructor is called with correct parameters (true, true)
 * - The panel inherits all functionality from ClassSpecificationsPanel
 * - Created specifications reflect the constructor parameters
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class KeepSpecificationsPanelClaude_constructorTest {

    private JFrame testFrame;
    private KeepSpecificationsPanel panel;

    @BeforeEach
    public void setUp() {
        // Tests will skip if headless mode is active
        assumeFalse(GraphicsEnvironment.isHeadless(),
                    "Skipping test: Headless environment detected. GUI components require a display.");
    }

    @AfterEach
    public void tearDown() {
        if (panel != null) {
            panel = null;
        }
        if (testFrame != null) {
            testFrame.dispose();
            testFrame = null;
        }
    }

    /**
     * Test constructor with all flags set to false.
     * This exercises all lines in the constructor (51, 53-58, 59):
     * - super() call with (owner, true, true)
     * - All six field assignments with false values
     */
    @Test
    public void testConstructorAllFalse() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, false, false, false, false, false, false);

        // Verify panel was created successfully (line 51 executed)
        assertNotNull(panel, "Panel should be created");

        // Verify the parent class was initialized (line 51)
        assertNotNull(panel.classSpecificationDialog,
                      "ClassSpecificationDialog should be initialized by parent");

        // Verify all flags are set to false by checking created specification (lines 53-58)
        ClassSpecification spec = panel.createClassSpecification();
        assertTrue(spec instanceof KeepClassSpecification,
                   "Should create KeepClassSpecification");

        KeepClassSpecification keepSpec = (KeepClassSpecification) spec;
        assertFalse(keepSpec.markClasses, "markClasses should be false");
        assertFalse(keepSpec.markConditionally, "markConditionally should be false");
        assertFalse(keepSpec.markDescriptorClasses, "markDescriptorClasses should be false");
        assertFalse(keepSpec.allowShrinking, "allowShrinking should be false");
        assertFalse(keepSpec.allowOptimization, "allowOptimization should be false");
        assertFalse(keepSpec.allowObfuscation, "allowObfuscation should be false");
    }

    /**
     * Test constructor with all flags set to true.
     * This exercises all lines with true values.
     */
    @Test
    public void testConstructorAllTrue() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, true, true, true, true, true, true);

        assertNotNull(panel, "Panel should be created");
        assertNotNull(panel.classSpecificationDialog,
                      "ClassSpecificationDialog should be initialized");

        // Verify all flags are set to true (lines 53-58)
        ClassSpecification spec = panel.createClassSpecification();
        KeepClassSpecification keepSpec = (KeepClassSpecification) spec;
        assertTrue(keepSpec.markClasses, "markClasses should be true");
        assertTrue(keepSpec.markConditionally, "markConditionally should be true");
        assertTrue(keepSpec.markDescriptorClasses, "markDescriptorClasses should be true");
        assertTrue(keepSpec.allowShrinking, "allowShrinking should be true");
        assertTrue(keepSpec.allowOptimization, "allowOptimization should be true");
        assertTrue(keepSpec.allowObfuscation, "allowObfuscation should be true");
    }

    /**
     * Test constructor with only markClasses set to true.
     * This verifies line 53 specifically.
     */
    @Test
    public void testConstructorMarkClassesTrue() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, true, false, false, false, false, false);

        assertNotNull(panel, "Panel should be created");

        ClassSpecification spec = panel.createClassSpecification();
        KeepClassSpecification keepSpec = (KeepClassSpecification) spec;
        assertTrue(keepSpec.markClasses, "markClasses should be true (line 53)");
        assertFalse(keepSpec.markConditionally, "markConditionally should be false");
        assertFalse(keepSpec.markDescriptorClasses, "markDescriptorClasses should be false");
        assertFalse(keepSpec.allowShrinking, "allowShrinking should be false");
        assertFalse(keepSpec.allowOptimization, "allowOptimization should be false");
        assertFalse(keepSpec.allowObfuscation, "allowObfuscation should be false");
    }

    /**
     * Test constructor with only markConditionally set to true.
     * This verifies line 54 specifically.
     */
    @Test
    public void testConstructorMarkConditionallyTrue() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, false, true, false, false, false, false);

        assertNotNull(panel, "Panel should be created");

        ClassSpecification spec = panel.createClassSpecification();
        KeepClassSpecification keepSpec = (KeepClassSpecification) spec;
        assertFalse(keepSpec.markClasses, "markClasses should be false");
        assertTrue(keepSpec.markConditionally, "markConditionally should be true (line 54)");
        assertFalse(keepSpec.markDescriptorClasses, "markDescriptorClasses should be false");
        assertFalse(keepSpec.allowShrinking, "allowShrinking should be false");
        assertFalse(keepSpec.allowOptimization, "allowOptimization should be false");
        assertFalse(keepSpec.allowObfuscation, "allowObfuscation should be false");
    }

    /**
     * Test constructor with only markDescriptorClasses set to true.
     * This verifies line 55 specifically.
     */
    @Test
    public void testConstructorMarkDescriptorClassesTrue() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, false, false, true, false, false, false);

        assertNotNull(panel, "Panel should be created");

        ClassSpecification spec = panel.createClassSpecification();
        KeepClassSpecification keepSpec = (KeepClassSpecification) spec;
        assertFalse(keepSpec.markClasses, "markClasses should be false");
        assertFalse(keepSpec.markConditionally, "markConditionally should be false");
        assertTrue(keepSpec.markDescriptorClasses, "markDescriptorClasses should be true (line 55)");
        assertFalse(keepSpec.allowShrinking, "allowShrinking should be false");
        assertFalse(keepSpec.allowOptimization, "allowOptimization should be false");
        assertFalse(keepSpec.allowObfuscation, "allowObfuscation should be false");
    }

    /**
     * Test constructor with only allowShrinking set to true.
     * This verifies line 56 specifically.
     */
    @Test
    public void testConstructorAllowShrinkingTrue() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, false, false, false, true, false, false);

        assertNotNull(panel, "Panel should be created");

        ClassSpecification spec = panel.createClassSpecification();
        KeepClassSpecification keepSpec = (KeepClassSpecification) spec;
        assertFalse(keepSpec.markClasses, "markClasses should be false");
        assertFalse(keepSpec.markConditionally, "markConditionally should be false");
        assertFalse(keepSpec.markDescriptorClasses, "markDescriptorClasses should be false");
        assertTrue(keepSpec.allowShrinking, "allowShrinking should be true (line 56)");
        assertFalse(keepSpec.allowOptimization, "allowOptimization should be false");
        assertFalse(keepSpec.allowObfuscation, "allowObfuscation should be false");
    }

    /**
     * Test constructor with only allowOptimization set to true.
     * This verifies line 57 specifically.
     */
    @Test
    public void testConstructorAllowOptimizationTrue() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, false, false, false, false, true, false);

        assertNotNull(panel, "Panel should be created");

        ClassSpecification spec = panel.createClassSpecification();
        KeepClassSpecification keepSpec = (KeepClassSpecification) spec;
        assertFalse(keepSpec.markClasses, "markClasses should be false");
        assertFalse(keepSpec.markConditionally, "markConditionally should be false");
        assertFalse(keepSpec.markDescriptorClasses, "markDescriptorClasses should be false");
        assertFalse(keepSpec.allowShrinking, "allowShrinking should be false");
        assertTrue(keepSpec.allowOptimization, "allowOptimization should be true (line 57)");
        assertFalse(keepSpec.allowObfuscation, "allowObfuscation should be false");
    }

    /**
     * Test constructor with only allowObfuscation set to true.
     * This verifies line 58 specifically.
     */
    @Test
    public void testConstructorAllowObfuscationTrue() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, false, false, false, false, false, true);

        assertNotNull(panel, "Panel should be created");

        ClassSpecification spec = panel.createClassSpecification();
        KeepClassSpecification keepSpec = (KeepClassSpecification) spec;
        assertFalse(keepSpec.markClasses, "markClasses should be false");
        assertFalse(keepSpec.markConditionally, "markConditionally should be false");
        assertFalse(keepSpec.markDescriptorClasses, "markDescriptorClasses should be false");
        assertFalse(keepSpec.allowShrinking, "allowShrinking should be false");
        assertFalse(keepSpec.allowOptimization, "allowOptimization should be false");
        assertTrue(keepSpec.allowObfuscation, "allowObfuscation should be true (line 58)");
    }

    /**
     * Test constructor with multiple mark flags set.
     * This exercises lines 53-55 with true values.
     */
    @Test
    public void testConstructorMultipleMarkFlags() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, true, true, true, false, false, false);

        assertNotNull(panel, "Panel should be created");

        ClassSpecification spec = panel.createClassSpecification();
        KeepClassSpecification keepSpec = (KeepClassSpecification) spec;
        assertTrue(keepSpec.markClasses, "markClasses should be true (line 53)");
        assertTrue(keepSpec.markConditionally, "markConditionally should be true (line 54)");
        assertTrue(keepSpec.markDescriptorClasses, "markDescriptorClasses should be true (line 55)");
        assertFalse(keepSpec.allowShrinking, "allowShrinking should be false");
        assertFalse(keepSpec.allowOptimization, "allowOptimization should be false");
        assertFalse(keepSpec.allowObfuscation, "allowObfuscation should be false");
    }

    /**
     * Test constructor with multiple allow flags set.
     * This exercises lines 56-58 with true values.
     */
    @Test
    public void testConstructorMultipleAllowFlags() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, false, false, false, true, true, true);

        assertNotNull(panel, "Panel should be created");

        ClassSpecification spec = panel.createClassSpecification();
        KeepClassSpecification keepSpec = (KeepClassSpecification) spec;
        assertFalse(keepSpec.markClasses, "markClasses should be false");
        assertFalse(keepSpec.markConditionally, "markConditionally should be false");
        assertFalse(keepSpec.markDescriptorClasses, "markDescriptorClasses should be false");
        assertTrue(keepSpec.allowShrinking, "allowShrinking should be true (line 56)");
        assertTrue(keepSpec.allowOptimization, "allowOptimization should be true (line 57)");
        assertTrue(keepSpec.allowObfuscation, "allowObfuscation should be true (line 58)");
    }

    /**
     * Test constructor with alternating flags pattern 1.
     * This exercises all assignment lines with mixed values.
     */
    @Test
    public void testConstructorAlternatingFlags1() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, true, false, true, false, true, false);

        assertNotNull(panel, "Panel should be created");

        ClassSpecification spec = panel.createClassSpecification();
        KeepClassSpecification keepSpec = (KeepClassSpecification) spec;
        assertTrue(keepSpec.markClasses, "markClasses should be true (line 53)");
        assertFalse(keepSpec.markConditionally, "markConditionally should be false (line 54)");
        assertTrue(keepSpec.markDescriptorClasses, "markDescriptorClasses should be true (line 55)");
        assertFalse(keepSpec.allowShrinking, "allowShrinking should be false (line 56)");
        assertTrue(keepSpec.allowOptimization, "allowOptimization should be true (line 57)");
        assertFalse(keepSpec.allowObfuscation, "allowObfuscation should be false (line 58)");
    }

    /**
     * Test constructor with alternating flags pattern 2.
     * This exercises all assignment lines with opposite mixed values.
     */
    @Test
    public void testConstructorAlternatingFlags2() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, false, true, false, true, false, true);

        assertNotNull(panel, "Panel should be created");

        ClassSpecification spec = panel.createClassSpecification();
        KeepClassSpecification keepSpec = (KeepClassSpecification) spec;
        assertFalse(keepSpec.markClasses, "markClasses should be false (line 53)");
        assertTrue(keepSpec.markConditionally, "markConditionally should be true (line 54)");
        assertFalse(keepSpec.markDescriptorClasses, "markDescriptorClasses should be false (line 55)");
        assertTrue(keepSpec.allowShrinking, "allowShrinking should be true (line 56)");
        assertFalse(keepSpec.allowOptimization, "allowOptimization should be false (line 57)");
        assertTrue(keepSpec.allowObfuscation, "allowObfuscation should be true (line 58)");
    }

    /**
     * Test constructor with null owner.
     * This verifies line 51 (super call) works with null owner.
     */
    @Test
    public void testConstructorWithNullOwner() {
        panel = new KeepSpecificationsPanel(null, true, false, true, false, true, false);

        assertNotNull(panel, "Panel should be created with null owner");
        assertNotNull(panel.classSpecificationDialog,
                      "Dialog should be initialized even with null owner (line 51)");

        // Verify flags are still properly assigned (lines 53-58)
        ClassSpecification spec = panel.createClassSpecification();
        KeepClassSpecification keepSpec = (KeepClassSpecification) spec;
        assertTrue(keepSpec.markClasses, "markClasses should be true");
        assertFalse(keepSpec.markConditionally, "markConditionally should be false");
        assertTrue(keepSpec.markDescriptorClasses, "markDescriptorClasses should be true");
        assertFalse(keepSpec.allowShrinking, "allowShrinking should be false");
        assertTrue(keepSpec.allowOptimization, "allowOptimization should be true");
        assertFalse(keepSpec.allowObfuscation, "allowObfuscation should be false");
    }

    /**
     * Test that constructor calls parent with correct parameters (true, true).
     * This verifies line 51 specifically.
     */
    @Test
    public void testConstructorCallsParentCorrectly() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, false, false, false, false, false, false);

        // The parent constructor is called with (owner, true, true)
        // This should initialize the dialog with includeKeepSettings=true, includeFieldButton=true
        assertNotNull(panel.classSpecificationDialog,
                      "Dialog should be initialized (parent constructor with true, true)");

        // Parent should also initialize buttons
        java.util.List<Component> buttons = panel.getButtons();
        assertNotNull(buttons, "Buttons should be initialized by parent");
        assertTrue(buttons.size() >= 5,
                   "Should have at least 5 buttons from parent initialization");
    }

    /**
     * Test that constructor properly initializes panel as a JPanel.
     * This verifies the super call (line 51) properly chains to ListPanel.
     */
    @Test
    public void testConstructorInitializesAsJPanel() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, true, true, true, true, true, true);

        assertTrue(panel instanceof JPanel,
                   "KeepSpecificationsPanel should be a JPanel (via parent hierarchy)");
        assertTrue(panel.getComponentCount() > 0,
                   "Panel should have components after initialization (line 51)");
    }

    /**
     * Test that multiple panels can be constructed independently.
     * This ensures all lines execute correctly for multiple instances.
     */
    @Test
    public void testMultiplePanelConstruction() {
        testFrame = new JFrame("Test Frame");

        KeepSpecificationsPanel panel1 = new KeepSpecificationsPanel(
            testFrame, true, false, false, false, false, false
        );
        KeepSpecificationsPanel panel2 = new KeepSpecificationsPanel(
            testFrame, false, true, false, false, false, false
        );
        KeepSpecificationsPanel panel3 = new KeepSpecificationsPanel(
            testFrame, false, false, true, true, true, true
        );

        assertNotNull(panel1, "First panel should be created");
        assertNotNull(panel2, "Second panel should be created");
        assertNotNull(panel3, "Third panel should be created");

        // Each panel should have its own field values (lines 53-58)
        ClassSpecification spec1 = panel1.createClassSpecification();
        ClassSpecification spec2 = panel2.createClassSpecification();
        ClassSpecification spec3 = panel3.createClassSpecification();

        KeepClassSpecification keepSpec1 = (KeepClassSpecification) spec1;
        KeepClassSpecification keepSpec2 = (KeepClassSpecification) spec2;
        KeepClassSpecification keepSpec3 = (KeepClassSpecification) spec3;

        // Verify each has different field values
        assertTrue(keepSpec1.markClasses);
        assertFalse(keepSpec2.markClasses);
        assertFalse(keepSpec3.markClasses);

        assertFalse(keepSpec1.markConditionally);
        assertTrue(keepSpec2.markConditionally);
        assertFalse(keepSpec3.markConditionally);

        assertFalse(keepSpec1.markDescriptorClasses);
        assertFalse(keepSpec2.markDescriptorClasses);
        assertTrue(keepSpec3.markDescriptorClasses);
    }

    /**
     * Test that the panel can be added to a frame after construction.
     * This verifies all constructor lines result in a valid panel state.
     */
    @Test
    public void testConstructorCreatesValidPanel() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, true, true, false, false, true, true);

        // Should be able to add panel to frame without exception
        testFrame.add(panel);
        testFrame.pack();

        assertNotNull(panel.getParent(),
                      "Panel should have a parent after being added to frame");
        assertTrue(panel.getSize().width > 0,
                   "Panel should have positive width");
        assertTrue(panel.getSize().height > 0,
                   "Panel should have positive height");
    }

    /**
     * Test that constructed panel has proper layout.
     * This verifies the parent initialization (line 51) sets up layout.
     */
    @Test
    public void testConstructorInitializesLayout() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, false, true, false, true, false, true);

        assertNotNull(panel.getLayout(),
                      "Panel should have a layout manager after construction");
        assertTrue(panel.getLayout() instanceof GridBagLayout,
                   "Panel should use GridBagLayout (from parent ListPanel)");
    }

    /**
     * Test constructor with all possible single-flag combinations.
     * This ensures comprehensive coverage of lines 53-58.
     */
    @Test
    public void testConstructorSingleFlagCoverage() {
        testFrame = new JFrame("Test Frame");

        // Test each flag individually to ensure each assignment line is executed
        boolean[][] testCases = {
            {true, false, false, false, false, false},  // markClasses only
            {false, true, false, false, false, false},  // markConditionally only
            {false, false, true, false, false, false},  // markDescriptorClasses only
            {false, false, false, true, false, false},  // allowShrinking only
            {false, false, false, false, true, false},  // allowOptimization only
            {false, false, false, false, false, true}   // allowObfuscation only
        };

        for (boolean[] testCase : testCases) {
            KeepSpecificationsPanel testPanel = new KeepSpecificationsPanel(
                testFrame, testCase[0], testCase[1], testCase[2],
                testCase[3], testCase[4], testCase[5]
            );

            assertNotNull(testPanel, "Panel should be created for test case");

            ClassSpecification spec = testPanel.createClassSpecification();
            KeepClassSpecification keepSpec = (KeepClassSpecification) spec;

            // Verify the flags match the test case
            assertEquals(testCase[0], keepSpec.markClasses,
                        "markClasses should match input (line 53)");
            assertEquals(testCase[1], keepSpec.markConditionally,
                        "markConditionally should match input (line 54)");
            assertEquals(testCase[2], keepSpec.markDescriptorClasses,
                        "markDescriptorClasses should match input (line 55)");
            assertEquals(testCase[3], keepSpec.allowShrinking,
                        "allowShrinking should match input (line 56)");
            assertEquals(testCase[4], keepSpec.allowOptimization,
                        "allowOptimization should match input (line 57)");
            assertEquals(testCase[5], keepSpec.allowObfuscation,
                        "allowObfuscation should match input (line 58)");
        }
    }

    /**
     * Test that flags remain consistent across multiple createClassSpecification calls.
     * This verifies that the field assignments (lines 53-58) are permanent and stable.
     */
    @Test
    public void testConstructorFieldsStability() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, true, false, true, false, true, false);

        // Call createClassSpecification multiple times
        ClassSpecification spec1 = panel.createClassSpecification();
        ClassSpecification spec2 = panel.createClassSpecification();
        ClassSpecification spec3 = panel.createClassSpecification();

        KeepClassSpecification keepSpec1 = (KeepClassSpecification) spec1;
        KeepClassSpecification keepSpec2 = (KeepClassSpecification) spec2;
        KeepClassSpecification keepSpec3 = (KeepClassSpecification) spec3;

        // All should have the same flag values (verifying fields weren't changed)
        assertEquals(keepSpec1.markClasses, keepSpec2.markClasses);
        assertEquals(keepSpec2.markClasses, keepSpec3.markClasses);

        assertEquals(keepSpec1.markConditionally, keepSpec2.markConditionally);
        assertEquals(keepSpec2.markConditionally, keepSpec3.markConditionally);

        assertEquals(keepSpec1.markDescriptorClasses, keepSpec2.markDescriptorClasses);
        assertEquals(keepSpec2.markDescriptorClasses, keepSpec3.markDescriptorClasses);

        assertEquals(keepSpec1.allowShrinking, keepSpec2.allowShrinking);
        assertEquals(keepSpec2.allowShrinking, keepSpec3.allowShrinking);

        assertEquals(keepSpec1.allowOptimization, keepSpec2.allowOptimization);
        assertEquals(keepSpec2.allowOptimization, keepSpec3.allowOptimization);

        assertEquals(keepSpec1.allowObfuscation, keepSpec2.allowObfuscation);
        assertEquals(keepSpec2.allowObfuscation, keepSpec3.allowObfuscation);
    }

    /**
     * Test constructor with visible owner frame.
     * This ensures line 51 (super call) works even when owner is visible.
     */
    @Test
    public void testConstructorWithVisibleFrame() {
        testFrame = new JFrame("Test Frame");
        testFrame.setSize(400, 300);
        testFrame.setVisible(true);

        panel = new KeepSpecificationsPanel(testFrame, true, true, true, false, false, false);

        assertNotNull(panel, "Panel should be created with visible owner");
        assertNotNull(panel.classSpecificationDialog,
                      "Dialog should be initialized with visible owner");

        // Hide the frame
        testFrame.setVisible(false);
    }

    /**
     * Test that dialog owner matches the provided frame.
     * This verifies that line 51 correctly passes the owner to the parent.
     */
    @Test
    public void testConstructorDialogOwner() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, false, false, false, false, false, false);

        assertNotNull(panel.classSpecificationDialog);
        assertEquals(testFrame, panel.classSpecificationDialog.getOwner(),
                     "Dialog owner should match the provided frame (line 51)");
    }

    /**
     * Test that buttons are properly initialized by parent constructor.
     * This verifies that line 51 properly executes the parent initialization.
     */
    @Test
    public void testConstructorInitializesButtons() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, false, false, false, false, false, false);

        java.util.List<Component> buttons = panel.getButtons();
        assertNotNull(buttons, "Buttons should be initialized");
        assertEquals(5, buttons.size(), "Should have 5 buttons from parent");

        // All buttons should be properly initialized
        for (Component button : buttons) {
            assertTrue(button instanceof JButton, "Each button should be JButton");
            JButton jButton = (JButton) button;
            assertNotNull(jButton.getText(), "Button should have text");
            assertNotNull(jButton.getToolTipText(), "Button should have tooltip");
        }
    }
}
