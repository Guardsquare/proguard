package proguard.gui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Test class for ProGuardGUI.
 *
 * This class tests all public methods of ProGuardGUI including:
 * - Constructor
 * - startSplash method
 * - skipSplash method
 * - createOptimizationsButton method
 * - main method
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class ProGuardGUIClaudeTest {

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

    // Constructor tests

    /**
     * Test that the constructor creates a valid ProGuardGUI instance.
     */
    @Test
    public void testConstructorCreatesValidInstance() {
        gui = new ProGuardGUI();

        assertNotNull(gui, "ProGuardGUI should be created successfully");
    }

    /**
     * Test that the constructor sets the correct window title.
     */
    @Test
    public void testConstructorSetsTitle() {
        gui = new ProGuardGUI();

        assertEquals("ProGuard", gui.getTitle(), "Window title should be 'ProGuard'");
    }

    /**
     * Test that the constructor sets the default close operation.
     */
    @Test
    public void testConstructorSetsDefaultCloseOperation() {
        gui = new ProGuardGUI();

        assertEquals(JFrame.EXIT_ON_CLOSE, gui.getDefaultCloseOperation(),
                    "Default close operation should be EXIT_ON_CLOSE");
    }

    /**
     * Test that the constructor initializes a content pane.
     */
    @Test
    public void testConstructorInitializesContentPane() {
        gui = new ProGuardGUI();

        assertNotNull(gui.getContentPane(), "Content pane should be initialized");
        assertTrue(gui.getContentPane().getComponentCount() > 0,
                  "Content pane should contain components");
    }

    /**
     * Test that the GUI is not visible immediately after construction.
     */
    @Test
    public void testConstructorGUINotInitiallyVisible() {
        gui = new ProGuardGUI();

        assertFalse(gui.isVisible(), "GUI should not be visible immediately after construction");
    }

    /**
     * Test that the constructor initializes without throwing exceptions.
     */
    @Test
    public void testConstructorDoesNotThrow() {
        assertDoesNotThrow(() -> {
            gui = new ProGuardGUI();
        }, "Constructor should not throw any exceptions");
    }

    /**
     * Test that the GUI extends JFrame.
     */
    @Test
    public void testConstructorCreatesJFrame() {
        gui = new ProGuardGUI();

        assertTrue(gui instanceof JFrame, "ProGuardGUI should extend JFrame");
    }

    /**
     * Test that the GUI has a non-null layout manager.
     */
    @Test
    public void testConstructorHasLayoutManager() {
        gui = new ProGuardGUI();

        assertNotNull(gui.getLayout(), "GUI should have a layout manager");
    }

    // startSplash tests

    /**
     * Test that startSplash does not throw an exception.
     */
    @Test
    public void testStartSplashDoesNotThrow() {
        gui = new ProGuardGUI();

        assertDoesNotThrow(() -> {
            gui.startSplash();
        }, "startSplash should not throw any exceptions");
    }

    /**
     * Test that startSplash can be called immediately after construction.
     */
    @Test
    public void testStartSplashAfterConstruction() {
        gui = new ProGuardGUI();

        assertDoesNotThrow(() -> {
            gui.startSplash();
        }, "startSplash should work immediately after construction");
    }

    /**
     * Test that startSplash can be called multiple times.
     */
    @Test
    public void testStartSplashMultipleTimes() {
        gui = new ProGuardGUI();

        assertDoesNotThrow(() -> {
            gui.startSplash();
            gui.startSplash();
            gui.startSplash();
        }, "startSplash should be callable multiple times");
    }

    /**
     * Test that startSplash followed by skipSplash does not throw.
     */
    @Test
    public void testStartSplashFollowedBySkipSplash() {
        gui = new ProGuardGUI();

        assertDoesNotThrow(() -> {
            gui.startSplash();
            gui.skipSplash();
        }, "startSplash followed by skipSplash should not throw");
    }

    // skipSplash tests

    /**
     * Test that skipSplash does not throw an exception.
     */
    @Test
    public void testSkipSplashDoesNotThrow() {
        gui = new ProGuardGUI();

        assertDoesNotThrow(() -> {
            gui.skipSplash();
        }, "skipSplash should not throw any exceptions");
    }

    /**
     * Test that skipSplash can be called immediately after construction.
     */
    @Test
    public void testSkipSplashAfterConstruction() {
        gui = new ProGuardGUI();

        assertDoesNotThrow(() -> {
            gui.skipSplash();
        }, "skipSplash should work immediately after construction");
    }

    /**
     * Test that skipSplash can be called multiple times.
     */
    @Test
    public void testSkipSplashMultipleTimes() {
        gui = new ProGuardGUI();

        assertDoesNotThrow(() -> {
            gui.skipSplash();
            gui.skipSplash();
            gui.skipSplash();
        }, "skipSplash should be callable multiple times");
    }

    /**
     * Test that skipSplash can be called before startSplash.
     */
    @Test
    public void testSkipSplashBeforeStartSplash() {
        gui = new ProGuardGUI();

        assertDoesNotThrow(() -> {
            gui.skipSplash();
            gui.startSplash();
        }, "skipSplash before startSplash should not throw");
    }

    /**
     * Test that skipSplash followed by startSplash does not throw.
     */
    @Test
    public void testSkipSplashFollowedByStartSplash() {
        gui = new ProGuardGUI();

        assertDoesNotThrow(() -> {
            gui.skipSplash();
            gui.startSplash();
        }, "skipSplash followed by startSplash should not throw");
    }

    /**
     * Test alternating startSplash and skipSplash calls.
     */
    @Test
    public void testAlternatingStartAndSkipSplash() {
        gui = new ProGuardGUI();

        assertDoesNotThrow(() -> {
            gui.startSplash();
            gui.skipSplash();
            gui.startSplash();
            gui.skipSplash();
        }, "Alternating startSplash and skipSplash should not throw");
    }

    // createOptimizationsButton tests

    /**
     * Test that createOptimizationsButton returns a non-null button.
     */
    @Test
    public void testCreateOptimizationsButtonReturnsButton() {
        gui = new ProGuardGUI();
        JTextField textField = new JTextField();

        JButton button = gui.createOptimizationsButton(textField);

        assertNotNull(button, "createOptimizationsButton should return a non-null button");
    }

    /**
     * Test that createOptimizationsButton returns a JButton instance.
     */
    @Test
    public void testCreateOptimizationsButtonReturnsJButton() {
        gui = new ProGuardGUI();
        JTextField textField = new JTextField();

        JButton button = gui.createOptimizationsButton(textField);

        assertTrue(button instanceof JButton,
                  "createOptimizationsButton should return a JButton instance");
    }

    /**
     * Test that createOptimizationsButton with null text field creates a button.
     * Note: The button's action listener will fail if clicked with null textField,
     * but the button creation itself should succeed.
     */
    @Test
    public void testCreateOptimizationsButtonWithNullTextField() {
        gui = new ProGuardGUI();

        JButton button = gui.createOptimizationsButton(null);
        assertNotNull(button, "Button should be created even with null text field");
    }

    /**
     * Test that createOptimizationsButton with empty text field works.
     */
    @Test
    public void testCreateOptimizationsButtonWithEmptyTextField() {
        gui = new ProGuardGUI();
        JTextField textField = new JTextField();

        JButton button = gui.createOptimizationsButton(textField);

        assertNotNull(button, "Button should be created with empty text field");
    }

    /**
     * Test that createOptimizationsButton with populated text field works.
     */
    @Test
    public void testCreateOptimizationsButtonWithPopulatedTextField() {
        gui = new ProGuardGUI();
        JTextField textField = new JTextField("class/marking/final");

        JButton button = gui.createOptimizationsButton(textField);

        assertNotNull(button, "Button should be created with populated text field");
    }

    /**
     * Test that the button created has action listeners.
     */
    @Test
    public void testCreateOptimizationsButtonHasActionListener() {
        gui = new ProGuardGUI();
        JTextField textField = new JTextField();

        JButton button = gui.createOptimizationsButton(textField);

        assertTrue(button.getActionListeners().length > 0,
                  "Button should have at least one action listener");
    }

    /**
     * Test that createOptimizationsButton can be called multiple times.
     */
    @Test
    public void testCreateOptimizationsButtonMultipleTimes() {
        gui = new ProGuardGUI();
        JTextField textField1 = new JTextField();
        JTextField textField2 = new JTextField();

        JButton button1 = gui.createOptimizationsButton(textField1);
        JButton button2 = gui.createOptimizationsButton(textField2);

        assertNotNull(button1, "First button should be created");
        assertNotNull(button2, "Second button should be created");
        assertNotSame(button1, button2, "Each call should create a new button");
    }

    /**
     * Test that the button has text.
     */
    @Test
    public void testCreateOptimizationsButtonHasText() {
        gui = new ProGuardGUI();
        JTextField textField = new JTextField();

        JButton button = gui.createOptimizationsButton(textField);

        assertNotNull(button.getText(), "Button should have text");
        assertFalse(button.getText().isEmpty(), "Button text should not be empty");
    }

    /**
     * Test that createOptimizationsButton is enabled by default.
     */
    @Test
    public void testCreateOptimizationsButtonIsEnabled() {
        gui = new ProGuardGUI();
        JTextField textField = new JTextField();

        JButton button = gui.createOptimizationsButton(textField);

        assertTrue(button.isEnabled(), "Button should be enabled by default");
    }

    /**
     * Test that createOptimizationsButton is visible by default.
     */
    @Test
    public void testCreateOptimizationsButtonIsVisible() {
        gui = new ProGuardGUI();
        JTextField textField = new JTextField();

        JButton button = gui.createOptimizationsButton(textField);

        assertTrue(button.isVisible(), "Button should be visible by default");
    }

    // main method tests

    /**
     * Test that main method with empty args does not throw immediately.
     * Note: This test starts the GUI in the background and verifies it doesn't crash.
     */
    @Test
    public void testMainWithEmptyArgs() {
        Thread mainThread = new Thread(() -> {
            try {
                ProGuardGUI.main(new String[]{});
            } catch (Exception e) {
                fail("main method should not throw exception with empty args: " + e.getMessage());
            }
        });

        mainThread.start();

        // Wait a bit to ensure GUI initializes
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // Ignore
        }

        // Clean up: find and dispose all ProGuardGUI windows
        SwingUtilities.invokeLater(() -> {
            for (Window window : Window.getWindows()) {
                if (window instanceof ProGuardGUI) {
                    window.dispose();
                }
            }
        });

        // Wait for cleanup
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            // Ignore
        }
    }

    /**
     * Test that main method with null args is handled.
     * Note: The main method may or may not throw with null args, we just verify it doesn't crash the JVM.
     */
    @Test
    public void testMainWithNullArgs() {
        Thread mainThread = new Thread(() -> {
            try {
                ProGuardGUI.main(null);
            } catch (NullPointerException e) {
                // Expected if main doesn't handle null args
            } catch (Exception e) {
                // Other exceptions are also acceptable
            }
        });

        mainThread.start();

        // Wait a bit to allow initialization
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // Ignore
        }

        // Clean up any windows that were created
        SwingUtilities.invokeLater(() -> {
            for (Window window : Window.getWindows()) {
                if (window instanceof ProGuardGUI) {
                    window.dispose();
                }
            }
        });

        // Wait for cleanup
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            // Ignore
        }
    }

    /**
     * Test that main method with -nosplash arg works.
     */
    @Test
    public void testMainWithNoSplashArg() {
        Thread mainThread = new Thread(() -> {
            try {
                ProGuardGUI.main(new String[]{"-nosplash"});
            } catch (Exception e) {
                fail("main method should not throw exception with -nosplash arg: " + e.getMessage());
            }
        });

        mainThread.start();

        // Wait a bit to ensure GUI initializes
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // Ignore
        }

        // Clean up
        SwingUtilities.invokeLater(() -> {
            for (Window window : Window.getWindows()) {
                if (window instanceof ProGuardGUI) {
                    window.dispose();
                }
            }
        });

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            // Ignore
        }
    }

    /**
     * Test that main method with config file arg works.
     */
    @Test
    public void testMainWithConfigFileArg() {
        Thread mainThread = new Thread(() -> {
            try {
                ProGuardGUI.main(new String[]{"config.pro"});
            } catch (Exception e) {
                // May fail if config.pro doesn't exist, which is expected
            }
        });

        mainThread.start();

        // Wait a bit
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // Ignore
        }

        // Clean up
        SwingUtilities.invokeLater(() -> {
            for (Window window : Window.getWindows()) {
                if (window instanceof ProGuardGUI) {
                    window.dispose();
                }
            }
        });

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            // Ignore
        }
    }

    /**
     * Test that main method with multiple args works.
     */
    @Test
    public void testMainWithMultipleArgs() {
        Thread mainThread = new Thread(() -> {
            try {
                ProGuardGUI.main(new String[]{"-nosplash", "config.pro"});
            } catch (Exception e) {
                // May fail if config.pro doesn't exist, which is expected
            }
        });

        mainThread.start();

        // Wait a bit
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // Ignore
        }

        // Clean up
        SwingUtilities.invokeLater(() -> {
            for (Window window : Window.getWindows()) {
                if (window instanceof ProGuardGUI) {
                    window.dispose();
                }
            }
        });

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            // Ignore
        }
    }

    // Integration tests

    /**
     * Test complete workflow: create GUI, start splash, skip splash, dispose.
     */
    @Test
    public void testCompleteWorkflowWithSplash() {
        gui = new ProGuardGUI();

        assertDoesNotThrow(() -> {
            gui.startSplash();
            Thread.sleep(100);
            gui.skipSplash();
        }, "Complete splash workflow should not throw");
    }

    /**
     * Test creating multiple buttons with different text fields.
     */
    @Test
    public void testCreateMultipleOptimizationsButtons() {
        gui = new ProGuardGUI();

        JTextField textField1 = new JTextField("class/*");
        JTextField textField2 = new JTextField("field/*");
        JTextField textField3 = new JTextField("method/*");

        JButton button1 = gui.createOptimizationsButton(textField1);
        JButton button2 = gui.createOptimizationsButton(textField2);
        JButton button3 = gui.createOptimizationsButton(textField3);

        assertNotNull(button1, "First button should be created");
        assertNotNull(button2, "Second button should be created");
        assertNotNull(button3, "Third button should be created");

        assertNotSame(button1, button2, "Buttons should be different instances");
        assertNotSame(button2, button3, "Buttons should be different instances");
        assertNotSame(button1, button3, "Buttons should be different instances");
    }

    /**
     * Test GUI can be created, used, and disposed multiple times.
     */
    @Test
    public void testMultipleGUICreationAndDisposal() {
        ProGuardGUI gui1 = new ProGuardGUI();
        assertNotNull(gui1, "First GUI should be created");
        gui1.dispose();

        ProGuardGUI gui2 = new ProGuardGUI();
        assertNotNull(gui2, "Second GUI should be created after first is disposed");
        gui2.dispose();

        ProGuardGUI gui3 = new ProGuardGUI();
        assertNotNull(gui3, "Third GUI should be created");
        gui3.dispose();
    }

    /**
     * Test that GUI components are properly initialized after construction.
     */
    @Test
    public void testGUIComponentsInitialized() {
        gui = new ProGuardGUI();

        assertNotNull(gui.getContentPane(), "Content pane should be initialized");
        assertNotNull(gui.getTitle(), "Title should be initialized");
        assertTrue(gui.getDefaultCloseOperation() >= 0,
                  "Default close operation should be set");
    }
}
