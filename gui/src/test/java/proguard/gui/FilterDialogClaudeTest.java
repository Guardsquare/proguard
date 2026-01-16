package proguard.gui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Test class for FilterDialog.
 *
 * This class tests all public methods of FilterDialog including:
 * - Constructor
 * - All filter getters and setters (filter, apkFilter, jarFilter, aarFilter, warFilter, earFilter, jmodFilter, zipFilter)
 * - showDialog method
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class FilterDialogClaudeTest {

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

    // Constructor tests

    /**
     * Test that the constructor creates a dialog with valid parameters.
     */
    @Test
    public void testConstructorWithValidParameters() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        assertNotNull(dialog, "Dialog should be created successfully");
        assertTrue(dialog.isModal(), "Dialog should be modal");
        assertTrue(dialog.isResizable(), "Dialog should be resizable");
        assertEquals(testFrame, dialog.getOwner(), "Dialog owner should be the test frame");
    }

    /**
     * Test that the constructor creates a dialog with null owner.
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
     */
    @Test
    public void testConstructorWithEmptyExplanation() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "");

        assertNotNull(dialog, "Dialog should be created successfully with empty explanation");
        assertTrue(dialog.isModal(), "Dialog should be modal");
    }

    /**
     * Test that the constructor creates a dialog with long explanation.
     */
    @Test
    public void testConstructorWithLongExplanation() {
        testFrame = new JFrame("Test Frame");
        String longExplanation = "This is a very long explanation that should wrap across multiple lines. " +
                                "It contains enough text to verify that the text area properly handles " +
                                "long explanations without any issues. The text should wrap nicely.";
        dialog = new FilterDialog(testFrame, longExplanation);

        assertNotNull(dialog, "Dialog should be created successfully with long explanation");
        assertTrue(dialog.getContentPane().getComponentCount() > 0,
                   "Dialog content pane should contain components");
    }

    /**
     * Test that the dialog is initially not visible after construction.
     */
    @Test
    public void testConstructorDialogNotInitiallyVisible() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        assertFalse(dialog.isVisible(), "Dialog should not be visible immediately after construction");
    }

    /**
     * Test that the dialog has a content pane after construction.
     */
    @Test
    public void testConstructorInitializesContentPane() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        assertNotNull(dialog.getContentPane(), "Dialog should have a content pane");
        assertTrue(dialog.getContentPane().getComponentCount() > 0,
                   "Dialog content pane should contain components");
    }

    // Filter tests (general filter)

    /**
     * Test setFilter with null value.
     */
    @Test
    public void testSetFilterWithNull() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        dialog.setFilter(null);
        // No exception should be thrown
    }

    /**
     * Test setFilter with empty list.
     */
    @Test
    public void testSetFilterWithEmptyList() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        dialog.setFilter(Collections.emptyList());
        // No exception should be thrown
    }

    /**
     * Test setFilter with single element list.
     */
    @Test
    public void testSetFilterWithSingleElement() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        List<String> filter = Collections.singletonList("*.class");
        dialog.setFilter(filter);
        // No exception should be thrown
    }

    /**
     * Test setFilter with multiple elements.
     */
    @Test
    public void testSetFilterWithMultipleElements() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        List<String> filter = Arrays.asList("*.class", "*.java", "*.txt");
        dialog.setFilter(filter);
        // No exception should be thrown
    }

    /**
     * Test getFilter returns null for default filter.
     */
    @Test
    public void testGetFilterReturnsNullForDefault() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        dialog.setFilter(null);
        List result = dialog.getFilter();
        assertNull(result, "getFilter should return null for default filter");
    }

    /**
     * Test getFilter returns list after setting filter.
     */
    @Test
    public void testGetFilterAfterSetFilter() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        List<String> filter = Arrays.asList("*.class", "*.java");
        dialog.setFilter(filter);
        List result = dialog.getFilter();

        assertNotNull(result, "getFilter should return non-null list");
    }

    // APK Filter tests

    /**
     * Test setApkFilter with null value.
     */
    @Test
    public void testSetApkFilterWithNull() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        dialog.setApkFilter(null);
        // No exception should be thrown
    }

    /**
     * Test setApkFilter with empty list.
     */
    @Test
    public void testSetApkFilterWithEmptyList() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        dialog.setApkFilter(Collections.emptyList());
        // No exception should be thrown
    }

    /**
     * Test setApkFilter with single element.
     */
    @Test
    public void testSetApkFilterWithSingleElement() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        List<String> filter = Collections.singletonList("*.apk");
        dialog.setApkFilter(filter);
        // No exception should be thrown
    }

    /**
     * Test setApkFilter with multiple elements.
     */
    @Test
    public void testSetApkFilterWithMultipleElements() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        List<String> filter = Arrays.asList("app.apk", "test.apk");
        dialog.setApkFilter(filter);
        // No exception should be thrown
    }

    /**
     * Test getApkFilter returns null for default filter.
     */
    @Test
    public void testGetApkFilterReturnsNullForDefault() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        dialog.setApkFilter(null);
        List result = dialog.getApkFilter();
        assertNull(result, "getApkFilter should return null for default filter");
    }

    /**
     * Test getApkFilter returns list after setting filter.
     */
    @Test
    public void testGetApkFilterAfterSetApkFilter() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        List<String> filter = Arrays.asList("app.apk", "test.apk");
        dialog.setApkFilter(filter);
        List result = dialog.getApkFilter();

        assertNotNull(result, "getApkFilter should return non-null list");
    }

    // JAR Filter tests

    /**
     * Test setJarFilter with null value.
     */
    @Test
    public void testSetJarFilterWithNull() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        dialog.setJarFilter(null);
        // No exception should be thrown
    }

    /**
     * Test setJarFilter with empty list.
     */
    @Test
    public void testSetJarFilterWithEmptyList() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        dialog.setJarFilter(Collections.emptyList());
        // No exception should be thrown
    }

    /**
     * Test setJarFilter with single element.
     */
    @Test
    public void testSetJarFilterWithSingleElement() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        List<String> filter = Collections.singletonList("*.jar");
        dialog.setJarFilter(filter);
        // No exception should be thrown
    }

    /**
     * Test setJarFilter with multiple elements.
     */
    @Test
    public void testSetJarFilterWithMultipleElements() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        List<String> filter = Arrays.asList("app.jar", "lib.jar");
        dialog.setJarFilter(filter);
        // No exception should be thrown
    }

    /**
     * Test getJarFilter returns null for default filter.
     */
    @Test
    public void testGetJarFilterReturnsNullForDefault() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        dialog.setJarFilter(null);
        List result = dialog.getJarFilter();
        assertNull(result, "getJarFilter should return null for default filter");
    }

    /**
     * Test getJarFilter returns list after setting filter.
     */
    @Test
    public void testGetJarFilterAfterSetJarFilter() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        List<String> filter = Arrays.asList("app.jar", "lib.jar");
        dialog.setJarFilter(filter);
        List result = dialog.getJarFilter();

        assertNotNull(result, "getJarFilter should return non-null list");
    }

    // AAR Filter tests

    /**
     * Test setAarFilter with null value.
     */
    @Test
    public void testSetAarFilterWithNull() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        dialog.setAarFilter(null);
        // No exception should be thrown
    }

    /**
     * Test setAarFilter with empty list.
     */
    @Test
    public void testSetAarFilterWithEmptyList() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        dialog.setAarFilter(Collections.emptyList());
        // No exception should be thrown
    }

    /**
     * Test setAarFilter with single element.
     */
    @Test
    public void testSetAarFilterWithSingleElement() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        List<String> filter = Collections.singletonList("*.aar");
        dialog.setAarFilter(filter);
        // No exception should be thrown
    }

    /**
     * Test setAarFilter with multiple elements.
     */
    @Test
    public void testSetAarFilterWithMultipleElements() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        List<String> filter = Arrays.asList("lib.aar", "support.aar");
        dialog.setAarFilter(filter);
        // No exception should be thrown
    }

    /**
     * Test getAarFilter returns null for default filter.
     */
    @Test
    public void testGetAarFilterReturnsNullForDefault() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        dialog.setAarFilter(null);
        List result = dialog.getAarFilter();
        assertNull(result, "getAarFilter should return null for default filter");
    }

    /**
     * Test getAarFilter returns list after setting filter.
     */
    @Test
    public void testGetAarFilterAfterSetAarFilter() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        List<String> filter = Arrays.asList("lib.aar", "support.aar");
        dialog.setAarFilter(filter);
        List result = dialog.getAarFilter();

        assertNotNull(result, "getAarFilter should return non-null list");
    }

    // WAR Filter tests

    /**
     * Test setWarFilter with null value.
     */
    @Test
    public void testSetWarFilterWithNull() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        dialog.setWarFilter(null);
        // No exception should be thrown
    }

    /**
     * Test setWarFilter with empty list.
     */
    @Test
    public void testSetWarFilterWithEmptyList() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        dialog.setWarFilter(Collections.emptyList());
        // No exception should be thrown
    }

    /**
     * Test setWarFilter with single element.
     */
    @Test
    public void testSetWarFilterWithSingleElement() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        List<String> filter = Collections.singletonList("*.war");
        dialog.setWarFilter(filter);
        // No exception should be thrown
    }

    /**
     * Test setWarFilter with multiple elements.
     */
    @Test
    public void testSetWarFilterWithMultipleElements() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        List<String> filter = Arrays.asList("app.war", "webapp.war");
        dialog.setWarFilter(filter);
        // No exception should be thrown
    }

    /**
     * Test getWarFilter returns null for default filter.
     */
    @Test
    public void testGetWarFilterReturnsNullForDefault() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        dialog.setWarFilter(null);
        List result = dialog.getWarFilter();
        assertNull(result, "getWarFilter should return null for default filter");
    }

    /**
     * Test getWarFilter returns list after setting filter.
     */
    @Test
    public void testGetWarFilterAfterSetWarFilter() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        List<String> filter = Arrays.asList("app.war", "webapp.war");
        dialog.setWarFilter(filter);
        List result = dialog.getWarFilter();

        assertNotNull(result, "getWarFilter should return non-null list");
    }

    // EAR Filter tests

    /**
     * Test setEarFilter with null value.
     */
    @Test
    public void testSetEarFilterWithNull() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        dialog.setEarFilter(null);
        // No exception should be thrown
    }

    /**
     * Test setEarFilter with empty list.
     */
    @Test
    public void testSetEarFilterWithEmptyList() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        dialog.setEarFilter(Collections.emptyList());
        // No exception should be thrown
    }

    /**
     * Test setEarFilter with single element.
     */
    @Test
    public void testSetEarFilterWithSingleElement() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        List<String> filter = Collections.singletonList("*.ear");
        dialog.setEarFilter(filter);
        // No exception should be thrown
    }

    /**
     * Test setEarFilter with multiple elements.
     */
    @Test
    public void testSetEarFilterWithMultipleElements() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        List<String> filter = Arrays.asList("app.ear", "enterprise.ear");
        dialog.setEarFilter(filter);
        // No exception should be thrown
    }

    /**
     * Test getEarFilter returns null for default filter.
     */
    @Test
    public void testGetEarFilterReturnsNullForDefault() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        dialog.setEarFilter(null);
        List result = dialog.getEarFilter();
        assertNull(result, "getEarFilter should return null for default filter");
    }

    /**
     * Test getEarFilter returns list after setting filter.
     */
    @Test
    public void testGetEarFilterAfterSetEarFilter() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        List<String> filter = Arrays.asList("app.ear", "enterprise.ear");
        dialog.setEarFilter(filter);
        List result = dialog.getEarFilter();

        assertNotNull(result, "getEarFilter should return non-null list");
    }

    // JMOD Filter tests

    /**
     * Test setJmodFilter with null value.
     */
    @Test
    public void testSetJmodFilterWithNull() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        dialog.setJmodFilter(null);
        // No exception should be thrown
    }

    /**
     * Test setJmodFilter with empty list.
     */
    @Test
    public void testSetJmodFilterWithEmptyList() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        dialog.setJmodFilter(Collections.emptyList());
        // No exception should be thrown
    }

    /**
     * Test setJmodFilter with single element.
     */
    @Test
    public void testSetJmodFilterWithSingleElement() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        List<String> filter = Collections.singletonList("*.jmod");
        dialog.setJmodFilter(filter);
        // No exception should be thrown
    }

    /**
     * Test setJmodFilter with multiple elements.
     */
    @Test
    public void testSetJmodFilterWithMultipleElements() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        List<String> filter = Arrays.asList("module.jmod", "base.jmod");
        dialog.setJmodFilter(filter);
        // No exception should be thrown
    }

    /**
     * Test getJmodFilter returns null for default filter.
     */
    @Test
    public void testGetJmodFilterReturnsNullForDefault() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        dialog.setJmodFilter(null);
        List result = dialog.getJmodFilter();
        assertNull(result, "getJmodFilter should return null for default filter");
    }

    /**
     * Test getJmodFilter returns list after setting filter.
     */
    @Test
    public void testGetJmodFilterAfterSetJmodFilter() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        List<String> filter = Arrays.asList("module.jmod", "base.jmod");
        dialog.setJmodFilter(filter);
        List result = dialog.getJmodFilter();

        assertNotNull(result, "getJmodFilter should return non-null list");
    }

    // ZIP Filter tests

    /**
     * Test setZipFilter with null value.
     */
    @Test
    public void testSetZipFilterWithNull() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        dialog.setZipFilter(null);
        // No exception should be thrown
    }

    /**
     * Test setZipFilter with empty list.
     */
    @Test
    public void testSetZipFilterWithEmptyList() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        dialog.setZipFilter(Collections.emptyList());
        // No exception should be thrown
    }

    /**
     * Test setZipFilter with single element.
     */
    @Test
    public void testSetZipFilterWithSingleElement() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        List<String> filter = Collections.singletonList("*.zip");
        dialog.setZipFilter(filter);
        // No exception should be thrown
    }

    /**
     * Test setZipFilter with multiple elements.
     */
    @Test
    public void testSetZipFilterWithMultipleElements() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        List<String> filter = Arrays.asList("archive.zip", "backup.zip");
        dialog.setZipFilter(filter);
        // No exception should be thrown
    }

    /**
     * Test getZipFilter returns null for default filter.
     */
    @Test
    public void testGetZipFilterReturnsNullForDefault() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        dialog.setZipFilter(null);
        List result = dialog.getZipFilter();
        assertNull(result, "getZipFilter should return null for default filter");
    }

    /**
     * Test getZipFilter returns list after setting filter.
     */
    @Test
    public void testGetZipFilterAfterSetZipFilter() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        List<String> filter = Arrays.asList("archive.zip", "backup.zip");
        dialog.setZipFilter(filter);
        List result = dialog.getZipFilter();

        assertNotNull(result, "getZipFilter should return non-null list");
    }

    // showDialog tests

    /**
     * Test showDialog returns CANCEL_OPTION by default when dialog is closed.
     * Note: This test programmatically closes the dialog to avoid blocking.
     */
    @Test
    public void testShowDialogReturnsCancelOption() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        // Use a separate thread to close the dialog after a short delay
        Thread closer = new Thread(() -> {
            try {
                Thread.sleep(100);
                SwingUtilities.invokeLater(() -> {
                    if (dialog != null && dialog.isVisible()) {
                        dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
                    }
                });
            } catch (InterruptedException e) {
                // Ignore
            }
        });
        closer.start();

        int result = dialog.showDialog();

        assertEquals(FilterDialog.CANCEL_OPTION, result,
                    "showDialog should return CANCEL_OPTION when dialog is closed");
    }

    /**
     * Test that showDialog makes the dialog visible.
     * Note: This test programmatically closes the dialog to avoid blocking.
     */
    @Test
    public void testShowDialogMakesDialogVisible() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        assertFalse(dialog.isVisible(), "Dialog should not be visible before showDialog");

        // Use a separate thread to verify visibility and close the dialog
        Thread checker = new Thread(() -> {
            try {
                Thread.sleep(100);
                SwingUtilities.invokeLater(() -> {
                    if (dialog != null) {
                        assertTrue(dialog.isVisible(), "Dialog should be visible during showDialog");
                        dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
                    }
                });
            } catch (InterruptedException e) {
                // Ignore
            }
        });
        checker.start();

        dialog.showDialog();
    }

    /**
     * Test that APPROVE_OPTION constant has expected value.
     */
    @Test
    public void testApproveOptionConstant() {
        assertEquals(0, FilterDialog.APPROVE_OPTION,
                    "APPROVE_OPTION should have value 0");
    }

    /**
     * Test that CANCEL_OPTION constant has expected value.
     */
    @Test
    public void testCancelOptionConstant() {
        assertEquals(1, FilterDialog.CANCEL_OPTION,
                    "CANCEL_OPTION should have value 1");
    }

    /**
     * Test that multiple calls to set/get methods work correctly.
     */
    @Test
    public void testMultipleSetGetCalls() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        // Set and get multiple times
        dialog.setFilter(Arrays.asList("*.class"));
        dialog.setJarFilter(Arrays.asList("*.jar"));
        dialog.setFilter(Arrays.asList("*.java"));

        List result = dialog.getFilter();
        assertNotNull(result, "Filter should be non-null after multiple sets");
    }

    /**
     * Test that all filters can be set and retrieved independently.
     */
    @Test
    public void testAllFiltersIndependently() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        // Set all filters with different values
        dialog.setFilter(Arrays.asList("filter1"));
        dialog.setApkFilter(Arrays.asList("apk1"));
        dialog.setJarFilter(Arrays.asList("jar1"));
        dialog.setAarFilter(Arrays.asList("aar1"));
        dialog.setWarFilter(Arrays.asList("war1"));
        dialog.setEarFilter(Arrays.asList("ear1"));
        dialog.setJmodFilter(Arrays.asList("jmod1"));
        dialog.setZipFilter(Arrays.asList("zip1"));

        // Verify all filters return non-null values
        assertNotNull(dialog.getFilter(), "Filter should be non-null");
        assertNotNull(dialog.getApkFilter(), "ApkFilter should be non-null");
        assertNotNull(dialog.getJarFilter(), "JarFilter should be non-null");
        assertNotNull(dialog.getAarFilter(), "AarFilter should be non-null");
        assertNotNull(dialog.getWarFilter(), "WarFilter should be non-null");
        assertNotNull(dialog.getEarFilter(), "EarFilter should be non-null");
        assertNotNull(dialog.getJmodFilter(), "JmodFilter should be non-null");
        assertNotNull(dialog.getZipFilter(), "ZipFilter should be non-null");
    }

    /**
     * Test that dialog can be disposed and recreated.
     */
    @Test
    public void testDialogDisposeAndRecreate() {
        testFrame = new JFrame("Test Frame");
        dialog = new FilterDialog(testFrame, "Test explanation");

        dialog.setFilter(Arrays.asList("*.class"));
        dialog.dispose();

        // Create a new dialog
        dialog = new FilterDialog(testFrame, "Test explanation 2");
        assertNotNull(dialog, "New dialog should be created after disposing previous one");

        // Set filter on new dialog should work
        dialog.setFilter(Arrays.asList("*.java"));
    }
}
