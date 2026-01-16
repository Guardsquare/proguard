package proguard.gui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Test class for ClassPathPanel.addFilterButton() method.
 *
 * Note: These tests verify that the addFilterButton() method correctly adds
 * a filter button to the ClassPathPanel during construction. The tests check:
 * - That the button is added to the panel's button list
 * - That the correct number of buttons are added for different configurations
 * - That buttons have appropriate properties (tooltips)
 * - That the filter button is properly enabled/disabled based on selection state
 *
 * The addFilterButton() method is a GUI setup method that must instantiate
 * Swing components (JFrame, JButton, etc.). These tests will automatically
 * skip if running in a headless environment (typical for CI/CD pipelines)
 * since GUI components cannot be instantiated without a display.
 */
public class ClassPathPanelClaude_addFilterButtonTest {

    private JFrame testFrame;
    private ClassPathPanel classPathPanel;

    @BeforeEach
    public void setUp() {
        // Tests will skip if headless mode is active
        assumeFalse(GraphicsEnvironment.isHeadless(),
                    "Skipping test: Headless environment detected. GUI components require a display.");
    }

    @AfterEach
    public void tearDown() {
        if (testFrame != null) {
            testFrame.dispose();
        }
    }

    /**
     * Test that addFilterButton is called during panel construction.
     * This test verifies that the filter button is added to the panel by checking
     * the total button count includes it.
     */
    @Test
    public void testAddFilterButtonAddsButtonToPanel() {
        // Create a frame and panel
        testFrame = new JFrame("Test Frame");
        classPathPanel = new ClassPathPanel(testFrame, false);

        // Get all buttons from the panel
        List buttons = classPathPanel.getButtons();

        // Verify that buttons were added (should include filter button)
        assertNotNull(buttons, "Buttons list should not be null");
        assertTrue(buttons.size() > 0, "Panel should have at least one button");

        // Verify all buttons are JButton instances
        boolean allAreButtons = true;
        for (Object button : buttons) {
            if (!(button instanceof JButton)) {
                allAreButtons = false;
                break;
            }
        }
        assertTrue(allAreButtons, "All components should be JButton instances");
    }

    /**
     * Test that buttons have appropriate properties.
     * Since addFilterButton() calls tip() to set a tooltip, we verify
     * that buttons have tooltips set.
     */
    @Test
    public void testFilterButtonHasToolTip() {
        testFrame = new JFrame("Test Frame");
        classPathPanel = new ClassPathPanel(testFrame, false);

        // Get all buttons
        List buttons = classPathPanel.getButtons();

        // Check that at least one button has a tooltip
        // All buttons in ClassPathPanel should have tooltips
        int buttonsWithTooltips = 0;
        for (Object button : buttons) {
            if (button instanceof JButton) {
                JButton jButton = (JButton) button;
                if (jButton.getToolTipText() != null) {
                    buttonsWithTooltips++;
                }
            }
        }

        // All buttons should have tooltips since all use the tip() method
        assertEquals(buttons.size(), buttonsWithTooltips,
                    "All buttons should have tooltips set via the tip() method");
    }

    /**
     * Test verifying the count of buttons matches expected configuration.
     * For inputAndOutput=false: add, edit, filter, remove, up, down = 6 buttons
     *
     * This test indirectly verifies that addFilterButton() was called since
     * we expect 6 buttons total (including the filter button).
     */
    @Test
    public void testButtonCountForNonInputOutputMode() {
        testFrame = new JFrame("Test Frame");
        classPathPanel = new ClassPathPanel(testFrame, false);

        List buttons = classPathPanel.getButtons();

        // In non-input-output mode: 1 add + 1 edit + 1 filter + 1 remove + 1 up + 1 down = 6
        assertEquals(6, buttons.size(), "Panel should have exactly 6 buttons in non-input-output mode");
    }

    /**
     * Test verifying the count of buttons for inputAndOutput mode.
     * For inputAndOutput=true: addInput, addOutput, edit, filter, remove, up, down = 7 buttons
     *
     * This test indirectly verifies that addFilterButton() was called since
     * we expect 7 buttons total (including the filter button).
     */
    @Test
    public void testButtonCountForInputOutputMode() {
        testFrame = new JFrame("Test Frame");
        classPathPanel = new ClassPathPanel(testFrame, true);

        List buttons = classPathPanel.getButtons();

        // In input-output mode: 2 add (input/output) + 1 edit + 1 filter + 1 remove + 1 up + 1 down = 7
        assertEquals(7, buttons.size(), "Panel should have exactly 7 buttons in input-output mode");
    }

    /**
     * Test that buttons are enabled/disabled based on selection state.
     * This indirectly tests that the filter button responds to selection changes
     * since addFilterButton() adds the button which should be controlled by
     * enableSelectionButtons() based on the firstSelectionButton index.
     *
     * For non-input-output mode, firstSelectionButton=2, meaning buttons at
     * index 2 and above (filter, remove, up, down) should be disabled when
     * there's no selection.
     */
    @Test
    public void testFilterButtonDisabledWhenNoSelection() {
        testFrame = new JFrame("Test Frame");
        classPathPanel = new ClassPathPanel(testFrame, false);

        List buttons = classPathPanel.getButtons();

        // Filter button should be at index 2 (0=add, 1=edit, 2=filter, 3=remove, 4=up, 5=down)
        // With no selection, selection-dependent buttons (index >= 2) should be disabled
        // firstSelectionButton for non-input-output mode is 2

        // Check first two buttons (add, edit) - behavior depends on implementation
        // Check filter button and beyond (indices 2-5) - should be disabled
        for (int i = 2; i < buttons.size(); i++) {
            Component button = (Component) buttons.get(i);
            assertFalse(button.isEnabled(),
                "Selection-dependent button at index " + i + " should be disabled when no selection");
        }
    }

    /**
     * Test that all buttons have localized text.
     * The addFilterButton() method uses msg("filter") to get localized text,
     * so we verify that buttons have non-null, non-empty text.
     */
    @Test
    public void testButtonsHaveLocalizedText() {
        testFrame = new JFrame("Test Frame");
        classPathPanel = new ClassPathPanel(testFrame, false);

        List buttons = classPathPanel.getButtons();

        // All buttons should have text from the resource bundle
        for (Object button : buttons) {
            if (button instanceof JButton) {
                JButton jButton = (JButton) button;
                assertNotNull(jButton.getText(), "Button text should not be null");
                assertFalse(jButton.getText().isEmpty(), "Button text should not be empty");
            }
        }
    }
}
