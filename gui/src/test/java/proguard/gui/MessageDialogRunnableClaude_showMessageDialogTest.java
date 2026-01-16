package proguard.gui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Test class focused on improving coverage for MessageDialogRunnable.showMessageDialog method.
 *
 * This class specifically targets the uncovered lines in the static showMessageDialog method:
 * - Line 51: SwingUtil.invokeAndWait call (try block)
 * - Line 56: Exception catch block
 * - Line 59: Empty comment in catch block
 * - Line 60: Method closing brace
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class MessageDialogRunnableClaude_showMessageDialogTest {

    private JFrame testFrame;
    private volatile JDialog lastDialog;

    @BeforeEach
    public void setUp() {
        // Tests will skip if headless mode is active
        assumeFalse(GraphicsEnvironment.isHeadless(),
                "Skipping test: Headless environment detected. GUI components require a display.");

        lastDialog = null;
    }

    @AfterEach
    public void tearDown() {
        if (lastDialog != null && lastDialog.isDisplayable()) {
            lastDialog.dispose();
        }
        if (testFrame != null && testFrame.isDisplayable()) {
            testFrame.dispose();
        }
    }

    /**
     * Test showMessageDialog with valid parameters to cover line 51.
     * This test ensures the SwingUtil.invokeAndWait call is executed successfully.
     */
    @Test
    public void testShowMessageDialogExecutesSuccessfully() throws Exception {
        testFrame = new JFrame("Test Frame");
        testFrame.setSize(300, 200);
        testFrame.setVisible(true);

        String message = "Test message for coverage";
        String title = "Coverage Test Title";
        int messageType = JOptionPane.INFORMATION_MESSAGE;

        // Create a latch to wait for the dialog to appear
        CountDownLatch dialogShownLatch = new CountDownLatch(1);

        // Start a thread to monitor for and close the dialog
        Thread dialogCloser = new Thread(() -> {
            try {
                // Wait a bit for the dialog to appear
                Thread.sleep(200);

                // Find and close the dialog
                SwingUtilities.invokeAndWait(() -> {
                    Window[] windows = Window.getWindows();
                    for (Window window : windows) {
                        if (window instanceof JDialog) {
                            JDialog dialog = (JDialog) window;
                            if (title.equals(dialog.getTitle())) {
                                lastDialog = dialog;
                                dialogShownLatch.countDown();
                                dialog.dispose();
                                return;
                            }
                        }
                    }
                });
            } catch (Exception e) {
                // Ignore
            }
        });
        dialogCloser.start();

        // Call the method - this should cover line 51 (the try block)
        assertDoesNotThrow(() ->
            MessageDialogRunnable.showMessageDialog(
                testFrame,
                message,
                title,
                messageType),
            "showMessageDialog should execute without throwing exceptions");

        // Wait for the dialog to have been shown and closed
        assertTrue(dialogShownLatch.await(2, TimeUnit.SECONDS),
                "Dialog should have been shown");

        dialogCloser.join(1000);
    }

    /**
     * Test showMessageDialog with null parent component.
     * This ensures the method works with different parameter values.
     */
    @Test
    public void testShowMessageDialogWithNullParent() throws Exception {
        String message = "Test with null parent";
        String title = "Null Parent Test";
        int messageType = JOptionPane.WARNING_MESSAGE;

        CountDownLatch dialogShownLatch = new CountDownLatch(1);

        Thread dialogCloser = new Thread(() -> {
            try {
                Thread.sleep(200);
                SwingUtilities.invokeAndWait(() -> {
                    Window[] windows = Window.getWindows();
                    for (Window window : windows) {
                        if (window instanceof JDialog) {
                            JDialog dialog = (JDialog) window;
                            if (title.equals(dialog.getTitle())) {
                                lastDialog = dialog;
                                dialogShownLatch.countDown();
                                dialog.dispose();
                                return;
                            }
                        }
                    }
                });
            } catch (Exception e) {
                // Ignore
            }
        });
        dialogCloser.start();

        // This should still cover line 51
        assertDoesNotThrow(() ->
            MessageDialogRunnable.showMessageDialog(
                null,
                message,
                title,
                messageType),
            "showMessageDialog should handle null parent");

        assertTrue(dialogShownLatch.await(2, TimeUnit.SECONDS),
                "Dialog should have been shown");

        dialogCloser.join(1000);
    }

    /**
     * Test showMessageDialog with different message types.
     * This ensures line 51 is covered for various parameter combinations.
     */
    @Test
    public void testShowMessageDialogWithDifferentMessageTypes() throws Exception {
        testFrame = new JFrame("Test Frame");
        testFrame.setSize(300, 200);

        // Test ERROR_MESSAGE
        testWithMessageType(JOptionPane.ERROR_MESSAGE, "Error Message Test");

        // Test INFORMATION_MESSAGE
        testWithMessageType(JOptionPane.INFORMATION_MESSAGE, "Info Message Test");

        // Test WARNING_MESSAGE
        testWithMessageType(JOptionPane.WARNING_MESSAGE, "Warning Message Test");

        // Test QUESTION_MESSAGE
        testWithMessageType(JOptionPane.QUESTION_MESSAGE, "Question Message Test");

        // Test PLAIN_MESSAGE
        testWithMessageType(JOptionPane.PLAIN_MESSAGE, "Plain Message Test");
    }

    /**
     * Helper method to test with a specific message type.
     */
    private void testWithMessageType(int messageType, String title) throws Exception {
        CountDownLatch dialogShownLatch = new CountDownLatch(1);

        Thread dialogCloser = new Thread(() -> {
            try {
                Thread.sleep(150);
                SwingUtilities.invokeAndWait(() -> {
                    Window[] windows = Window.getWindows();
                    for (Window window : windows) {
                        if (window instanceof JDialog) {
                            JDialog dialog = (JDialog) window;
                            if (title.equals(dialog.getTitle())) {
                                lastDialog = dialog;
                                dialogShownLatch.countDown();
                                dialog.dispose();
                                return;
                            }
                        }
                    }
                });
            } catch (Exception e) {
                // Ignore
            }
        });
        dialogCloser.start();

        assertDoesNotThrow(() ->
            MessageDialogRunnable.showMessageDialog(
                testFrame,
                "Test message",
                title,
                messageType));

        assertTrue(dialogShownLatch.await(2, TimeUnit.SECONDS),
                "Dialog should have been shown for " + title);

        dialogCloser.join(1000);
    }

    /**
     * Test showMessageDialog with complex message object.
     * JOptionPane accepts various types of message objects.
     */
    @Test
    public void testShowMessageDialogWithComplexMessage() throws Exception {
        testFrame = new JFrame("Test Frame");
        testFrame.setSize(300, 200);

        Object[] complexMessage = {
            "Line 1",
            "Line 2",
            new JLabel("Label in message")
        };
        String title = "Complex Message Test";

        CountDownLatch dialogShownLatch = new CountDownLatch(1);

        Thread dialogCloser = new Thread(() -> {
            try {
                Thread.sleep(200);
                SwingUtilities.invokeAndWait(() -> {
                    Window[] windows = Window.getWindows();
                    for (Window window : windows) {
                        if (window instanceof JDialog) {
                            JDialog dialog = (JDialog) window;
                            if (title.equals(dialog.getTitle())) {
                                lastDialog = dialog;
                                dialogShownLatch.countDown();
                                dialog.dispose();
                                return;
                            }
                        }
                    }
                });
            } catch (Exception e) {
                // Ignore
            }
        });
        dialogCloser.start();

        assertDoesNotThrow(() ->
            MessageDialogRunnable.showMessageDialog(
                testFrame,
                complexMessage,
                title,
                JOptionPane.INFORMATION_MESSAGE),
            "showMessageDialog should handle complex message objects");

        assertTrue(dialogShownLatch.await(2, TimeUnit.SECONDS),
                "Dialog should have been shown");

        dialogCloser.join(1000);
    }

    /**
     * Test showMessageDialog called from the Event Dispatch Thread.
     * This tests a different execution path where SwingUtil.invokeAndWait
     * will directly call run() instead of using SwingUtilities.invokeAndWait.
     */
    @Test
    public void testShowMessageDialogFromEventDispatchThread() throws Exception {
        testFrame = new JFrame("Test Frame");
        testFrame.setSize(300, 200);

        String title = "EDT Test";
        AtomicReference<Exception> exceptionRef = new AtomicReference<>();
        CountDownLatch completionLatch = new CountDownLatch(1);

        // Schedule the dialog to be closed quickly
        Timer closeTimer = new Timer(100, e -> {
            Window[] windows = Window.getWindows();
            for (Window window : windows) {
                if (window instanceof JDialog) {
                    JDialog dialog = (JDialog) window;
                    if (title.equals(dialog.getTitle())) {
                        lastDialog = dialog;
                        dialog.dispose();
                        break;
                    }
                }
            }
        });
        closeTimer.setRepeats(false);

        // Call showMessageDialog from the EDT
        SwingUtilities.invokeLater(() -> {
            try {
                closeTimer.start();
                MessageDialogRunnable.showMessageDialog(
                    testFrame,
                    "Message from EDT",
                    title,
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                exceptionRef.set(ex);
            } finally {
                closeTimer.stop();
                completionLatch.countDown();
            }
        });

        assertTrue(completionLatch.await(3, TimeUnit.SECONDS),
                "Operation should complete");
        assertNull(exceptionRef.get(),
                "No exception should be thrown when called from EDT");
    }

    /**
     * Test showMessageDialog with null message and null title.
     * This ensures the method handles edge cases properly.
     */
    @Test
    public void testShowMessageDialogWithNullMessageAndTitle() throws Exception {
        testFrame = new JFrame("Test Frame");
        testFrame.setSize(300, 200);

        CountDownLatch dialogShownLatch = new CountDownLatch(1);

        Thread dialogCloser = new Thread(() -> {
            try {
                Thread.sleep(200);
                SwingUtilities.invokeAndWait(() -> {
                    Window[] windows = Window.getWindows();
                    for (Window window : windows) {
                        if (window instanceof JDialog) {
                            JDialog dialog = (JDialog) window;
                            // With null title, JOptionPane uses "Message" as default
                            lastDialog = dialog;
                            dialogShownLatch.countDown();
                            dialog.dispose();
                            return;
                        }
                    }
                });
            } catch (Exception e) {
                // Ignore
            }
        });
        dialogCloser.start();

        assertDoesNotThrow(() ->
            MessageDialogRunnable.showMessageDialog(
                testFrame,
                null,
                null,
                JOptionPane.INFORMATION_MESSAGE),
            "showMessageDialog should handle null message and title");

        assertTrue(dialogShownLatch.await(2, TimeUnit.SECONDS),
                "Dialog should have been shown");

        dialogCloser.join(1000);
    }

    /**
     * Test showMessageDialog with empty string message and title.
     */
    @Test
    public void testShowMessageDialogWithEmptyStrings() throws Exception {
        testFrame = new JFrame("Test Frame");
        testFrame.setSize(300, 200);

        CountDownLatch dialogShownLatch = new CountDownLatch(1);

        Thread dialogCloser = new Thread(() -> {
            try {
                Thread.sleep(200);
                SwingUtilities.invokeAndWait(() -> {
                    Window[] windows = Window.getWindows();
                    for (Window window : windows) {
                        if (window instanceof JDialog) {
                            JDialog dialog = (JDialog) window;
                            if ("".equals(dialog.getTitle())) {
                                lastDialog = dialog;
                                dialogShownLatch.countDown();
                                dialog.dispose();
                                return;
                            }
                        }
                    }
                });
            } catch (Exception e) {
                // Ignore
            }
        });
        dialogCloser.start();

        assertDoesNotThrow(() ->
            MessageDialogRunnable.showMessageDialog(
                testFrame,
                "",
                "",
                JOptionPane.PLAIN_MESSAGE),
            "showMessageDialog should handle empty strings");

        assertTrue(dialogShownLatch.await(2, TimeUnit.SECONDS),
                "Dialog should have been shown");

        dialogCloser.join(1000);
    }

    /**
     * Test multiple successive calls to showMessageDialog.
     * This ensures the method can be called multiple times without issues.
     */
    @Test
    public void testMultipleSuccessiveShowMessageDialogCalls() throws Exception {
        testFrame = new JFrame("Test Frame");
        testFrame.setSize(300, 200);

        for (int i = 0; i < 3; i++) {
            final String title = "Test " + i;
            CountDownLatch dialogShownLatch = new CountDownLatch(1);

            Thread dialogCloser = new Thread(() -> {
                try {
                    Thread.sleep(150);
                    SwingUtilities.invokeAndWait(() -> {
                        Window[] windows = Window.getWindows();
                        for (Window window : windows) {
                            if (window instanceof JDialog) {
                                JDialog dialog = (JDialog) window;
                                if (title.equals(dialog.getTitle())) {
                                    lastDialog = dialog;
                                    dialogShownLatch.countDown();
                                    dialog.dispose();
                                    return;
                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    // Ignore
                }
            });
            dialogCloser.start();

            final int index = i;
            assertDoesNotThrow(() ->
                MessageDialogRunnable.showMessageDialog(
                    testFrame,
                    "Message " + index,
                    title,
                    JOptionPane.INFORMATION_MESSAGE),
                "Call " + i + " should not throw exception");

            assertTrue(dialogShownLatch.await(2, TimeUnit.SECONDS),
                    "Dialog " + i + " should have been shown");

            dialogCloser.join(1000);
        }
    }
}
