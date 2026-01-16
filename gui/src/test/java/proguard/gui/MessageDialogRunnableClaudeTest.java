package proguard.gui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Test class for MessageDialogRunnable.
 *
 * This class tests all public methods of MessageDialogRunnable:
 * - Static showMessageDialog(Component, Object, String, int)
 * - Constructor MessageDialogRunnable(Component, Object, String, int)
 * - run()
 *
 * The MessageDialogRunnable is a utility class that shows JOptionPane message dialogs
 * on the Swing event dispatch thread.
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class MessageDialogRunnableClaudeTest {

    private JFrame testFrame;

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
     * Test constructor with all parameters set to non-null values.
     * The constructor should store the parameters for later use in run().
     */
    @Test
    public void testConstructorWithValidParameters() {
        testFrame = new JFrame("Test Frame");
        String message = "Test message";
        String title = "Test Title";
        int messageType = JOptionPane.INFORMATION_MESSAGE;

        MessageDialogRunnable runnable = new MessageDialogRunnable(
                testFrame,
                message,
                title,
                messageType);

        assertNotNull(runnable, "MessageDialogRunnable should be created successfully");
    }

    /**
     * Test constructor with null parent component.
     * This should be allowed as JOptionPane.showMessageDialog accepts null parent.
     */
    @Test
    public void testConstructorWithNullParent() {
        String message = "Test message";
        String title = "Test Title";
        int messageType = JOptionPane.ERROR_MESSAGE;

        MessageDialogRunnable runnable = new MessageDialogRunnable(
                null,
                message,
                title,
                messageType);

        assertNotNull(runnable, "MessageDialogRunnable should be created with null parent");
    }

    /**
     * Test constructor with null message.
     * JOptionPane accepts null messages, so this should work.
     */
    @Test
    public void testConstructorWithNullMessage() {
        testFrame = new JFrame("Test Frame");
        String title = "Test Title";
        int messageType = JOptionPane.WARNING_MESSAGE;

        MessageDialogRunnable runnable = new MessageDialogRunnable(
                testFrame,
                null,
                title,
                messageType);

        assertNotNull(runnable, "MessageDialogRunnable should be created with null message");
    }

    /**
     * Test constructor with null title.
     * JOptionPane accepts null titles, so this should work.
     */
    @Test
    public void testConstructorWithNullTitle() {
        testFrame = new JFrame("Test Frame");
        String message = "Test message";
        int messageType = JOptionPane.PLAIN_MESSAGE;

        MessageDialogRunnable runnable = new MessageDialogRunnable(
                testFrame,
                message,
                null,
                messageType);

        assertNotNull(runnable, "MessageDialogRunnable should be created with null title");
    }

    /**
     * Test constructor with different message types.
     */
    @Test
    public void testConstructorWithDifferentMessageTypes() {
        testFrame = new JFrame("Test Frame");
        String message = "Test message";
        String title = "Test Title";

        // Test with ERROR_MESSAGE
        MessageDialogRunnable runnable1 = new MessageDialogRunnable(
                testFrame, message, title, JOptionPane.ERROR_MESSAGE);
        assertNotNull(runnable1);

        // Test with INFORMATION_MESSAGE
        MessageDialogRunnable runnable2 = new MessageDialogRunnable(
                testFrame, message, title, JOptionPane.INFORMATION_MESSAGE);
        assertNotNull(runnable2);

        // Test with WARNING_MESSAGE
        MessageDialogRunnable runnable3 = new MessageDialogRunnable(
                testFrame, message, title, JOptionPane.WARNING_MESSAGE);
        assertNotNull(runnable3);

        // Test with QUESTION_MESSAGE
        MessageDialogRunnable runnable4 = new MessageDialogRunnable(
                testFrame, message, title, JOptionPane.QUESTION_MESSAGE);
        assertNotNull(runnable4);

        // Test with PLAIN_MESSAGE
        MessageDialogRunnable runnable5 = new MessageDialogRunnable(
                testFrame, message, title, JOptionPane.PLAIN_MESSAGE);
        assertNotNull(runnable5);
    }

    /**
     * Test constructor with complex message object.
     * JOptionPane can accept various object types as messages.
     */
    @Test
    public void testConstructorWithComplexMessage() {
        testFrame = new JFrame("Test Frame");
        Object[] complexMessage = {"Line 1", "Line 2", new JLabel("Label")};
        String title = "Complex Test";
        int messageType = JOptionPane.INFORMATION_MESSAGE;

        MessageDialogRunnable runnable = new MessageDialogRunnable(
                testFrame,
                complexMessage,
                title,
                messageType);

        assertNotNull(runnable, "MessageDialogRunnable should handle complex message objects");
    }

    /**
     * Test run() method basic execution.
     * Since run() shows a dialog, we cannot easily test it without user interaction.
     * However, we can verify that calling run() doesn't throw exceptions.
     * We'll use a timer to auto-close the dialog.
     */
    @Test
    public void testRunDoesNotThrowException() {
        testFrame = new JFrame("Test Frame");
        String message = "Test message";
        String title = "Test Title";
        int messageType = JOptionPane.INFORMATION_MESSAGE;

        MessageDialogRunnable runnable = new MessageDialogRunnable(
                testFrame,
                message,
                title,
                messageType);

        // Schedule the dialog to be closed automatically
        Timer timer = new Timer(100, e -> {
            Window[] windows = Window.getWindows();
            for (Window window : windows) {
                if (window instanceof JDialog) {
                    JDialog dialog = (JDialog) window;
                    if (title.equals(dialog.getTitle())) {
                        dialog.dispose();
                    }
                }
            }
        });
        timer.setRepeats(false);
        timer.start();

        // Run should not throw an exception
        assertDoesNotThrow(() -> runnable.run(),
                "run() should not throw an exception");

        // Clean up
        timer.stop();
    }

    /**
     * Test static showMessageDialog method.
     * This method creates a MessageDialogRunnable and invokes it using SwingUtil.
     * We'll verify it doesn't throw exceptions and auto-close the dialog.
     */
    @Test
    public void testShowMessageDialogBasic() {
        testFrame = new JFrame("Test Frame");
        testFrame.setVisible(true);
        String message = "Static method test";
        String title = "Static Test";
        int messageType = JOptionPane.INFORMATION_MESSAGE;

        // Schedule the dialog to be closed automatically
        Timer timer = new Timer(100, e -> {
            Window[] windows = Window.getWindows();
            for (Window window : windows) {
                if (window instanceof JDialog) {
                    JDialog dialog = (JDialog) window;
                    if (title.equals(dialog.getTitle())) {
                        dialog.dispose();
                    }
                }
            }
        });
        timer.setRepeats(false);
        timer.start();

        // Should not throw an exception
        assertDoesNotThrow(() -> MessageDialogRunnable.showMessageDialog(
                testFrame,
                message,
                title,
                messageType),
                "showMessageDialog should not throw an exception");

        // Clean up
        timer.stop();
    }

    /**
     * Test static showMessageDialog with null parent.
     */
    @Test
    public void testShowMessageDialogWithNullParent() {
        String message = "Null parent test";
        String title = "Null Parent Test";
        int messageType = JOptionPane.ERROR_MESSAGE;

        // Schedule the dialog to be closed automatically
        Timer timer = new Timer(100, e -> {
            Window[] windows = Window.getWindows();
            for (Window window : windows) {
                if (window instanceof JDialog) {
                    JDialog dialog = (JDialog) window;
                    if (title.equals(dialog.getTitle())) {
                        dialog.dispose();
                    }
                }
            }
        });
        timer.setRepeats(false);
        timer.start();

        // Should not throw an exception even with null parent
        assertDoesNotThrow(() -> MessageDialogRunnable.showMessageDialog(
                null,
                message,
                title,
                messageType),
                "showMessageDialog should handle null parent");

        // Clean up
        timer.stop();
    }

    /**
     * Test static showMessageDialog with different message types.
     */
    @Test
    public void testShowMessageDialogWithDifferentTypes() {
        testFrame = new JFrame("Test Frame");
        String message = "Type test";

        // Test ERROR_MESSAGE
        Timer timer1 = new Timer(50, e -> closeDialogByTitle("Error Test"));
        timer1.setRepeats(false);
        timer1.start();
        assertDoesNotThrow(() -> MessageDialogRunnable.showMessageDialog(
                testFrame, message, "Error Test", JOptionPane.ERROR_MESSAGE));
        timer1.stop();

        // Test WARNING_MESSAGE
        Timer timer2 = new Timer(50, e -> closeDialogByTitle("Warning Test"));
        timer2.setRepeats(false);
        timer2.start();
        assertDoesNotThrow(() -> MessageDialogRunnable.showMessageDialog(
                testFrame, message, "Warning Test", JOptionPane.WARNING_MESSAGE));
        timer2.stop();

        // Test QUESTION_MESSAGE
        Timer timer3 = new Timer(50, e -> closeDialogByTitle("Question Test"));
        timer3.setRepeats(false);
        timer3.start();
        assertDoesNotThrow(() -> MessageDialogRunnable.showMessageDialog(
                testFrame, message, "Question Test", JOptionPane.QUESTION_MESSAGE));
        timer3.stop();

        // Test PLAIN_MESSAGE
        Timer timer4 = new Timer(50, e -> closeDialogByTitle("Plain Test"));
        timer4.setRepeats(false);
        timer4.start();
        assertDoesNotThrow(() -> MessageDialogRunnable.showMessageDialog(
                testFrame, message, "Plain Test", JOptionPane.PLAIN_MESSAGE));
        timer4.stop();
    }

    /**
     * Test showMessageDialog with null message and null title.
     */
    @Test
    public void testShowMessageDialogWithNullMessageAndTitle() {
        testFrame = new JFrame("Test Frame");

        // Schedule the dialog to be closed automatically - null title means "Message"
        Timer timer = new Timer(100, e -> {
            Window[] windows = Window.getWindows();
            for (Window window : windows) {
                if (window instanceof JDialog) {
                    JDialog dialog = (JDialog) window;
                    dialog.dispose();
                }
            }
        });
        timer.setRepeats(false);
        timer.start();

        assertDoesNotThrow(() -> MessageDialogRunnable.showMessageDialog(
                testFrame,
                null,
                null,
                JOptionPane.INFORMATION_MESSAGE),
                "showMessageDialog should handle null message and title");

        timer.stop();
    }

    /**
     * Helper method to close a dialog by its title.
     */
    private void closeDialogByTitle(String title) {
        Window[] windows = Window.getWindows();
        for (Window window : windows) {
            if (window instanceof JDialog) {
                JDialog dialog = (JDialog) window;
                if (title.equals(dialog.getTitle())) {
                    dialog.dispose();
                }
            }
        }
    }
}
