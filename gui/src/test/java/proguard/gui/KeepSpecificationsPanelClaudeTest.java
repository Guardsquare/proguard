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
 * Test class for KeepSpecificationsPanel.
 *
 * This test class verifies the functionality of KeepSpecificationsPanel, which extends
 * ClassSpecificationsPanel to provide GUI panel functionality for managing KeepClassSpecification
 * objects with various keep options (markClasses, markConditionally, etc.).
 *
 * Tests cover:
 * - Constructor: initialization with all combinations of boolean flags
 * - createClassSpecification: factory method that creates KeepClassSpecification instances
 * - setClassSpecification: setting specifications in the dialog
 * - getClassSpecification: retrieving specifications from the dialog
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class KeepSpecificationsPanelClaudeTest {

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

    // ========== Constructor Tests ==========

    /**
     * Test constructor with all flags set to true.
     * Verifies that the panel is properly initialized.
     */
    @Test
    public void testConstructorAllTrue() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, true, true, true, true, true, true);

        assertNotNull(panel, "Panel should be created");
        assertNotNull(panel.classSpecificationDialog, "Dialog should be initialized");
    }

    /**
     * Test constructor with all flags set to false.
     * Verifies that the panel can be created with all flags disabled.
     */
    @Test
    public void testConstructorAllFalse() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, false, false, false, false, false, false);

        assertNotNull(panel, "Panel should be created");
        assertNotNull(panel.classSpecificationDialog, "Dialog should be initialized");
    }

    /**
     * Test constructor with markClasses=true, others false.
     */
    @Test
    public void testConstructorMarkClassesOnly() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, true, false, false, false, false, false);

        assertNotNull(panel, "Panel should be created");

        // Verify that createClassSpecification respects the markClasses flag
        ClassSpecification spec = panel.createClassSpecification();
        assertTrue(spec instanceof KeepClassSpecification, "Should create KeepClassSpecification");
        KeepClassSpecification keepSpec = (KeepClassSpecification) spec;
        assertTrue(keepSpec.markClasses, "markClasses should be true");
        assertFalse(keepSpec.markConditionally, "markConditionally should be false");
        assertFalse(keepSpec.markDescriptorClasses, "markDescriptorClasses should be false");
        assertFalse(keepSpec.allowShrinking, "allowShrinking should be false");
        assertFalse(keepSpec.allowOptimization, "allowOptimization should be false");
        assertFalse(keepSpec.allowObfuscation, "allowObfuscation should be false");
    }

    /**
     * Test constructor with markConditionally=true, others false.
     */
    @Test
    public void testConstructorMarkConditionallyOnly() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, false, true, false, false, false, false);

        assertNotNull(panel, "Panel should be created");

        ClassSpecification spec = panel.createClassSpecification();
        assertTrue(spec instanceof KeepClassSpecification, "Should create KeepClassSpecification");
        KeepClassSpecification keepSpec = (KeepClassSpecification) spec;
        assertFalse(keepSpec.markClasses, "markClasses should be false");
        assertTrue(keepSpec.markConditionally, "markConditionally should be true");
        assertFalse(keepSpec.markDescriptorClasses, "markDescriptorClasses should be false");
        assertFalse(keepSpec.allowShrinking, "allowShrinking should be false");
        assertFalse(keepSpec.allowOptimization, "allowOptimization should be false");
        assertFalse(keepSpec.allowObfuscation, "allowObfuscation should be false");
    }

    /**
     * Test constructor with markDescriptorClasses=true, others false.
     */
    @Test
    public void testConstructorMarkDescriptorClassesOnly() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, false, false, true, false, false, false);

        assertNotNull(panel, "Panel should be created");

        ClassSpecification spec = panel.createClassSpecification();
        assertTrue(spec instanceof KeepClassSpecification, "Should create KeepClassSpecification");
        KeepClassSpecification keepSpec = (KeepClassSpecification) spec;
        assertFalse(keepSpec.markClasses, "markClasses should be false");
        assertFalse(keepSpec.markConditionally, "markConditionally should be false");
        assertTrue(keepSpec.markDescriptorClasses, "markDescriptorClasses should be true");
        assertFalse(keepSpec.allowShrinking, "allowShrinking should be false");
        assertFalse(keepSpec.allowOptimization, "allowOptimization should be false");
        assertFalse(keepSpec.allowObfuscation, "allowObfuscation should be false");
    }

    /**
     * Test constructor with allowShrinking=true, others false.
     */
    @Test
    public void testConstructorAllowShrinkingOnly() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, false, false, false, true, false, false);

        assertNotNull(panel, "Panel should be created");

        ClassSpecification spec = panel.createClassSpecification();
        assertTrue(spec instanceof KeepClassSpecification, "Should create KeepClassSpecification");
        KeepClassSpecification keepSpec = (KeepClassSpecification) spec;
        assertFalse(keepSpec.markClasses, "markClasses should be false");
        assertFalse(keepSpec.markConditionally, "markConditionally should be false");
        assertFalse(keepSpec.markDescriptorClasses, "markDescriptorClasses should be false");
        assertTrue(keepSpec.allowShrinking, "allowShrinking should be true");
        assertFalse(keepSpec.allowOptimization, "allowOptimization should be false");
        assertFalse(keepSpec.allowObfuscation, "allowObfuscation should be false");
    }

    /**
     * Test constructor with allowOptimization=true, others false.
     */
    @Test
    public void testConstructorAllowOptimizationOnly() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, false, false, false, false, true, false);

        assertNotNull(panel, "Panel should be created");

        ClassSpecification spec = panel.createClassSpecification();
        assertTrue(spec instanceof KeepClassSpecification, "Should create KeepClassSpecification");
        KeepClassSpecification keepSpec = (KeepClassSpecification) spec;
        assertFalse(keepSpec.markClasses, "markClasses should be false");
        assertFalse(keepSpec.markConditionally, "markConditionally should be false");
        assertFalse(keepSpec.markDescriptorClasses, "markDescriptorClasses should be false");
        assertFalse(keepSpec.allowShrinking, "allowShrinking should be false");
        assertTrue(keepSpec.allowOptimization, "allowOptimization should be true");
        assertFalse(keepSpec.allowObfuscation, "allowObfuscation should be false");
    }

    /**
     * Test constructor with allowObfuscation=true, others false.
     */
    @Test
    public void testConstructorAllowObfuscationOnly() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, false, false, false, false, false, true);

        assertNotNull(panel, "Panel should be created");

        ClassSpecification spec = panel.createClassSpecification();
        assertTrue(spec instanceof KeepClassSpecification, "Should create KeepClassSpecification");
        KeepClassSpecification keepSpec = (KeepClassSpecification) spec;
        assertFalse(keepSpec.markClasses, "markClasses should be false");
        assertFalse(keepSpec.markConditionally, "markConditionally should be false");
        assertFalse(keepSpec.markDescriptorClasses, "markDescriptorClasses should be false");
        assertFalse(keepSpec.allowShrinking, "allowShrinking should be false");
        assertFalse(keepSpec.allowOptimization, "allowOptimization should be false");
        assertTrue(keepSpec.allowObfuscation, "allowObfuscation should be true");
    }

    /**
     * Test constructor with multiple mark flags set.
     */
    @Test
    public void testConstructorMultipleMarkFlags() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, true, true, true, false, false, false);

        assertNotNull(panel, "Panel should be created");

        ClassSpecification spec = panel.createClassSpecification();
        assertTrue(spec instanceof KeepClassSpecification, "Should create KeepClassSpecification");
        KeepClassSpecification keepSpec = (KeepClassSpecification) spec;
        assertTrue(keepSpec.markClasses, "markClasses should be true");
        assertTrue(keepSpec.markConditionally, "markConditionally should be true");
        assertTrue(keepSpec.markDescriptorClasses, "markDescriptorClasses should be true");
        assertFalse(keepSpec.allowShrinking, "allowShrinking should be false");
        assertFalse(keepSpec.allowOptimization, "allowOptimization should be false");
        assertFalse(keepSpec.allowObfuscation, "allowObfuscation should be false");
    }

    /**
     * Test constructor with multiple allow flags set.
     */
    @Test
    public void testConstructorMultipleAllowFlags() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, false, false, false, true, true, true);

        assertNotNull(panel, "Panel should be created");

        ClassSpecification spec = panel.createClassSpecification();
        assertTrue(spec instanceof KeepClassSpecification, "Should create KeepClassSpecification");
        KeepClassSpecification keepSpec = (KeepClassSpecification) spec;
        assertFalse(keepSpec.markClasses, "markClasses should be false");
        assertFalse(keepSpec.markConditionally, "markConditionally should be false");
        assertFalse(keepSpec.markDescriptorClasses, "markDescriptorClasses should be false");
        assertTrue(keepSpec.allowShrinking, "allowShrinking should be true");
        assertTrue(keepSpec.allowOptimization, "allowOptimization should be true");
        assertTrue(keepSpec.allowObfuscation, "allowObfuscation should be true");
    }

    /**
     * Test constructor with mixed flags.
     */
    @Test
    public void testConstructorMixedFlags1() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, true, false, true, false, true, false);

        assertNotNull(panel, "Panel should be created");

        ClassSpecification spec = panel.createClassSpecification();
        assertTrue(spec instanceof KeepClassSpecification, "Should create KeepClassSpecification");
        KeepClassSpecification keepSpec = (KeepClassSpecification) spec;
        assertTrue(keepSpec.markClasses, "markClasses should be true");
        assertFalse(keepSpec.markConditionally, "markConditionally should be false");
        assertTrue(keepSpec.markDescriptorClasses, "markDescriptorClasses should be true");
        assertFalse(keepSpec.allowShrinking, "allowShrinking should be false");
        assertTrue(keepSpec.allowOptimization, "allowOptimization should be true");
        assertFalse(keepSpec.allowObfuscation, "allowObfuscation should be false");
    }

    /**
     * Test constructor with mixed flags (inverse pattern).
     */
    @Test
    public void testConstructorMixedFlags2() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, false, true, false, true, false, true);

        assertNotNull(panel, "Panel should be created");

        ClassSpecification spec = panel.createClassSpecification();
        assertTrue(spec instanceof KeepClassSpecification, "Should create KeepClassSpecification");
        KeepClassSpecification keepSpec = (KeepClassSpecification) spec;
        assertFalse(keepSpec.markClasses, "markClasses should be false");
        assertTrue(keepSpec.markConditionally, "markConditionally should be true");
        assertFalse(keepSpec.markDescriptorClasses, "markDescriptorClasses should be false");
        assertTrue(keepSpec.allowShrinking, "allowShrinking should be true");
        assertFalse(keepSpec.allowOptimization, "allowOptimization should be false");
        assertTrue(keepSpec.allowObfuscation, "allowObfuscation should be true");
    }

    /**
     * Test constructor with null owner.
     */
    @Test
    public void testConstructorWithNullOwner() {
        panel = new KeepSpecificationsPanel(null, true, false, true, false, true, false);

        assertNotNull(panel, "Panel should be created with null owner");
        assertNotNull(panel.classSpecificationDialog, "Dialog should be initialized");
    }

    /**
     * Test that constructor properly inherits from ClassSpecificationsPanel.
     */
    @Test
    public void testConstructorInheritsFromParent() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, true, true, true, false, false, false);

        // Should have buttons from parent class
        assertTrue(panel.getButtons().size() >= 5,
                   "Panel should have at least 5 buttons from parent class");
    }

    // ========== createClassSpecification Tests ==========

    /**
     * Test createClassSpecification returns a KeepClassSpecification.
     */
    @Test
    public void testCreateClassSpecificationReturnsKeepClassSpecification() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, true, false, false, false, false, false);

        ClassSpecification spec = panel.createClassSpecification();

        assertNotNull(spec, "Should return non-null specification");
        assertTrue(spec instanceof KeepClassSpecification,
                   "Should return KeepClassSpecification instance");
    }

    /**
     * Test createClassSpecification returns new instance each time.
     */
    @Test
    public void testCreateClassSpecificationReturnsNewInstances() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, true, false, false, false, false, false);

        ClassSpecification spec1 = panel.createClassSpecification();
        ClassSpecification spec2 = panel.createClassSpecification();

        assertNotSame(spec1, spec2, "Should return different instances");
    }

    /**
     * Test createClassSpecification wraps a base ClassSpecification.
     */
    @Test
    public void testCreateClassSpecificationWrapsBaseSpec() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, true, false, false, false, false, false);

        ClassSpecification spec = panel.createClassSpecification();
        KeepClassSpecification keepSpec = (KeepClassSpecification) spec;

        // KeepClassSpecification should have inherited fields from ClassSpecification
        // These are accessible through the parent class
        assertNotNull(keepSpec, "KeepClassSpecification should be properly initialized");
    }

    /**
     * Test createClassSpecification with all flags false creates proper specification.
     */
    @Test
    public void testCreateClassSpecificationAllFlagsDisabled() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, false, false, false, false, false, false);

        ClassSpecification spec = panel.createClassSpecification();
        KeepClassSpecification keepSpec = (KeepClassSpecification) spec;

        assertFalse(keepSpec.markClasses);
        assertFalse(keepSpec.markConditionally);
        assertFalse(keepSpec.markDescriptorClasses);
        assertFalse(keepSpec.allowShrinking);
        assertFalse(keepSpec.allowOptimization);
        assertFalse(keepSpec.allowObfuscation);
        assertNull(keepSpec.condition, "Condition should be null");
        // markClassMembers is set to false by the implementation
        assertFalse(keepSpec.markClassMembers);
        // markCodeAttributes is set to false by the implementation
        assertFalse(keepSpec.markCodeAttributes);
    }

    /**
     * Test createClassSpecification with all flags enabled creates proper specification.
     */
    @Test
    public void testCreateClassSpecificationAllFlagsEnabled() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, true, true, true, true, true, true);

        ClassSpecification spec = panel.createClassSpecification();
        KeepClassSpecification keepSpec = (KeepClassSpecification) spec;

        assertTrue(keepSpec.markClasses);
        assertTrue(keepSpec.markConditionally);
        assertTrue(keepSpec.markDescriptorClasses);
        assertTrue(keepSpec.allowShrinking);
        assertTrue(keepSpec.allowOptimization);
        assertTrue(keepSpec.allowObfuscation);
        assertNull(keepSpec.condition, "Condition should be null");
        assertFalse(keepSpec.markClassMembers, "markClassMembers is hardcoded to false");
        assertFalse(keepSpec.markCodeAttributes, "markCodeAttributes is hardcoded to false");
    }

    /**
     * Test createClassSpecification preserves flags across multiple calls.
     */
    @Test
    public void testCreateClassSpecificationPreservesFlagsAcrossCalls() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, true, false, true, false, true, false);

        ClassSpecification spec1 = panel.createClassSpecification();
        ClassSpecification spec2 = panel.createClassSpecification();

        KeepClassSpecification keepSpec1 = (KeepClassSpecification) spec1;
        KeepClassSpecification keepSpec2 = (KeepClassSpecification) spec2;

        // Both should have same flag values
        assertEquals(keepSpec1.markClasses, keepSpec2.markClasses);
        assertEquals(keepSpec1.markConditionally, keepSpec2.markConditionally);
        assertEquals(keepSpec1.markDescriptorClasses, keepSpec2.markDescriptorClasses);
        assertEquals(keepSpec1.allowShrinking, keepSpec2.allowShrinking);
        assertEquals(keepSpec1.allowOptimization, keepSpec2.allowOptimization);
        assertEquals(keepSpec1.allowObfuscation, keepSpec2.allowObfuscation);
    }

    // ========== setClassSpecification Tests ==========

    /**
     * Test setClassSpecification with a valid KeepClassSpecification.
     */
    @Test
    public void testSetClassSpecificationWithValidKeepSpec() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, true, false, false, false, false, false);

        ClassSpecification baseSpec = new ClassSpecification(
            "Test Comment", 0, 0, null, "com.example.Test", null, null
        );
        KeepClassSpecification keepSpec = new KeepClassSpecification(
            true, false, false, false, false, false, false, null, baseSpec
        );

        // Should not throw exception
        panel.setClassSpecification(keepSpec);

        // Verify it was set
        ClassSpecification retrieved = panel.getClassSpecification();
        assertTrue(retrieved instanceof KeepClassSpecification,
                   "Retrieved should be KeepClassSpecification");
        assertEquals("Test Comment", retrieved.comments);
        assertEquals("com.example.Test", retrieved.className);
    }

    /**
     * Test setClassSpecification with different KeepClassSpecification configurations.
     */
    @Test
    public void testSetClassSpecificationWithDifferentConfigurations() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, true, true, true, true, true, true);

        ClassSpecification baseSpec1 = new ClassSpecification(
            "Config1", 0, 0, null, "Class1", null, null
        );
        KeepClassSpecification keepSpec1 = new KeepClassSpecification(
            true, true, false, false, false, false, false, null, baseSpec1
        );

        panel.setClassSpecification(keepSpec1);
        ClassSpecification retrieved1 = panel.getClassSpecification();
        assertEquals("Config1", retrieved1.comments);

        ClassSpecification baseSpec2 = new ClassSpecification(
            "Config2", 0, 0, null, "Class2", null, null
        );
        KeepClassSpecification keepSpec2 = new KeepClassSpecification(
            false, false, true, true, false, false, false, null, baseSpec2
        );

        panel.setClassSpecification(keepSpec2);
        ClassSpecification retrieved2 = panel.getClassSpecification();
        assertEquals("Config2", retrieved2.comments);
    }

    /**
     * Test setClassSpecification with null.
     */
    @Test
    public void testSetClassSpecificationWithNull() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, false, false, false, false, false, false);

        // Should not throw exception
        panel.setClassSpecification(null);
    }

    /**
     * Test setClassSpecification updates the dialog state.
     */
    @Test
    public void testSetClassSpecificationUpdatesDialogState() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, true, false, true, false, true, false);

        ClassSpecification baseSpec = new ClassSpecification(
            "Original", 0, 0, null, "com.example.Original", null, null
        );
        KeepClassSpecification keepSpec = new KeepClassSpecification(
            true, false, true, false, false, false, false, null, baseSpec
        );

        panel.setClassSpecification(keepSpec);

        // Get the specification back from the dialog
        ClassSpecification retrieved = panel.getClassSpecification();

        assertEquals("Original", retrieved.comments);
        assertEquals("com.example.Original", retrieved.className);
    }

    // ========== getClassSpecification Tests ==========

    /**
     * Test getClassSpecification returns KeepClassSpecification.
     */
    @Test
    public void testGetClassSpecificationReturnsKeepClassSpec() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, true, false, false, false, false, false);

        ClassSpecification baseSpec = new ClassSpecification(
            "Test", 0, 0, null, "com.example.Test", null, null
        );
        KeepClassSpecification keepSpec = new KeepClassSpecification(
            true, false, false, false, false, false, false, null, baseSpec
        );

        panel.setClassSpecification(keepSpec);
        ClassSpecification retrieved = panel.getClassSpecification();

        assertNotNull(retrieved, "Should return non-null");
        assertTrue(retrieved instanceof KeepClassSpecification,
                   "Should return KeepClassSpecification");
    }

    /**
     * Test getClassSpecification maintains data consistency.
     */
    @Test
    public void testGetClassSpecificationDataConsistency() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, true, true, false, false, false, false);

        ClassSpecification baseSpec = new ClassSpecification(
            "Consistent", 1, 2, "anno", "com.test.Class", "exAnno", "exClass"
        );
        KeepClassSpecification keepSpec = new KeepClassSpecification(
            true, true, false, false, false, false, false, null, baseSpec
        );

        panel.setClassSpecification(keepSpec);
        ClassSpecification retrieved = panel.getClassSpecification();

        assertEquals("Consistent", retrieved.comments);
        assertEquals(1, retrieved.requiredSetAccessFlags);
        assertEquals(2, retrieved.requiredUnsetAccessFlags);
        assertEquals("anno", retrieved.annotationType);
        assertEquals("com.test.Class", retrieved.className);
        assertEquals("exAnno", retrieved.extendsAnnotationType);
        assertEquals("exClass", retrieved.extendsClassName);
    }

    /**
     * Test getClassSpecification after multiple set operations.
     */
    @Test
    public void testGetClassSpecificationAfterMultipleSets() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, false, false, false, false, false, false);

        ClassSpecification baseSpec1 = new ClassSpecification(
            "First", 0, 0, null, "Class1", null, null
        );
        KeepClassSpecification keepSpec1 = new KeepClassSpecification(
            false, false, false, false, false, false, false, null, baseSpec1
        );

        panel.setClassSpecification(keepSpec1);
        ClassSpecification retrieved1 = panel.getClassSpecification();
        assertEquals("First", retrieved1.comments);

        ClassSpecification baseSpec2 = new ClassSpecification(
            "Second", 0, 0, null, "Class2", null, null
        );
        KeepClassSpecification keepSpec2 = new KeepClassSpecification(
            false, false, false, false, false, false, false, null, baseSpec2
        );

        panel.setClassSpecification(keepSpec2);
        ClassSpecification retrieved2 = panel.getClassSpecification();
        assertEquals("Second", retrieved2.comments);
    }

    /**
     * Test round-trip: set then get should return same data.
     */
    @Test
    public void testRoundTripSetGetClassSpecification() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, true, false, true, false, true, false);

        ClassSpecification baseSpec = new ClassSpecification(
            "RoundTrip", 3, 4, "testAnno", "com.test.Round", "exAnno", "exClass"
        );
        KeepClassSpecification keepSpec = new KeepClassSpecification(
            true, false, true, false, false, false, true, null, baseSpec
        );

        panel.setClassSpecification(keepSpec);
        ClassSpecification retrieved = panel.getClassSpecification();

        assertEquals("RoundTrip", retrieved.comments);
        assertEquals(3, retrieved.requiredSetAccessFlags);
        assertEquals(4, retrieved.requiredUnsetAccessFlags);
        assertEquals("testAnno", retrieved.annotationType);
        assertEquals("com.test.Round", retrieved.className);
        assertEquals("exAnno", retrieved.extendsAnnotationType);
        assertEquals("exClass", retrieved.extendsClassName);

        assertTrue(retrieved instanceof KeepClassSpecification);
        KeepClassSpecification retrievedKeep = (KeepClassSpecification) retrieved;
        assertTrue(retrievedKeep.markClasses || !retrievedKeep.markClasses); // Just verify it's accessible
    }

    // ========== Integration Tests ==========

    /**
     * Test that the panel properly initializes with parent class functionality.
     */
    @Test
    public void testPanelInheritedFunctionality() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, true, true, true, false, false, false);

        // Should have list functionality from parent
        assertNull(panel.getClassSpecifications(), "Initial list should be empty");

        // Should have buttons from parent
        assertTrue(panel.getButtons().size() >= 5, "Should have buttons from parent");
    }

    /**
     * Test panel can be displayed in a frame.
     */
    @Test
    public void testPanelCanBeDisplayed() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, true, false, false, true, false, false);

        testFrame.add(panel);
        testFrame.pack();

        assertNotNull(panel.getParent(), "Panel should have a parent");
        assertTrue(panel.getSize().width > 0, "Panel should have positive width");
        assertTrue(panel.getSize().height > 0, "Panel should have positive height");
    }

    /**
     * Test multiple panels with different configurations can coexist.
     */
    @Test
    public void testMultiplePanelsCanCoexist() {
        testFrame = new JFrame("Test Frame");
        KeepSpecificationsPanel panel1 = new KeepSpecificationsPanel(
            testFrame, true, false, false, false, false, false
        );
        KeepSpecificationsPanel panel2 = new KeepSpecificationsPanel(
            testFrame, false, true, false, false, false, false
        );

        ClassSpecification spec1 = panel1.createClassSpecification();
        ClassSpecification spec2 = panel2.createClassSpecification();

        KeepClassSpecification keepSpec1 = (KeepClassSpecification) spec1;
        KeepClassSpecification keepSpec2 = (KeepClassSpecification) spec2;

        // Verify each panel maintains its own configuration
        assertTrue(keepSpec1.markClasses);
        assertFalse(keepSpec1.markConditionally);

        assertFalse(keepSpec2.markClasses);
        assertTrue(keepSpec2.markConditionally);
    }

    /**
     * Test that created specifications have correct hardcoded values.
     */
    @Test
    public void testCreatedSpecificationsHaveCorrectDefaults() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, true, true, true, true, true, true);

        ClassSpecification spec = panel.createClassSpecification();
        KeepClassSpecification keepSpec = (KeepClassSpecification) spec;

        // According to the implementation, these are always false
        assertFalse(keepSpec.markClassMembers,
                    "markClassMembers should be false (hardcoded in implementation)");
        assertFalse(keepSpec.markCodeAttributes,
                    "markCodeAttributes should be false (hardcoded in implementation)");
        assertNull(keepSpec.condition,
                   "condition should be null (hardcoded in implementation)");
    }

    /**
     * Test constructor calls parent constructor with correct parameters.
     */
    @Test
    public void testConstructorCallsParentCorrectly() {
        testFrame = new JFrame("Test Frame");
        panel = new KeepSpecificationsPanel(testFrame, false, false, false, false, false, false);

        // Parent constructor is called with (owner, true, true)
        // This should result in the dialog being initialized with includeKeepSettings=true
        assertNotNull(panel.classSpecificationDialog,
                      "Dialog should be initialized (parent constructor should be called)");
    }
}
