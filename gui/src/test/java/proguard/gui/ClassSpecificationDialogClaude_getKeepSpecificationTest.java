package proguard.gui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.ClassSpecification;
import proguard.KeepClassSpecification;
import proguard.classfile.AccessConstants;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Test class for ClassSpecificationDialog.getKeepSpecification() method.
 *
 * The getKeepSpecification method:
 * - Reads the state of various keep-related checkboxes
 * - Gets the condition ClassSpecification from a nested dialog
 * - Gets the ClassSpecification via getClassSpecification()
 * - Creates and returns a new KeepClassSpecification with all the collected data
 *
 * These tests verify:
 * - The method correctly retrieves all boolean flags from checkboxes
 * - The method retrieves the condition correctly
 * - The method retrieves the ClassSpecification data correctly
 * - The method returns non-null KeepClassSpecification
 * - Values set via setKeepSpecification can be retrieved via getKeepSpecification
 *
 * Note: These tests require GUI components and will skip in headless environments.
 * Note: The dialog must be created with includeKeepSettings=true to have the keep-related checkboxes.
 */
public class ClassSpecificationDialogClaude_getKeepSpecificationTest {

    private JFrame testFrame;
    private ClassSpecificationDialog dialog;

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

    /**
     * Test that getKeepSpecification returns a non-null result.
     */
    @Test
    public void testGetKeepSpecificationReturnsNonNull() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        KeepClassSpecification result = dialog.getKeepSpecification();

        assertNotNull(result, "getKeepSpecification should never return null");
    }

    /**
     * Test that getKeepSpecification retrieves default values from a newly created dialog.
     */
    @Test
    public void testGetKeepSpecificationDefaultValues() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        KeepClassSpecification result = dialog.getKeepSpecification();

        assertNotNull(result, "Result should not be null");
        // Default values should be false for all flags
        assertFalse(result.markClasses, "Default markClasses should be false");
        assertFalse(result.markClassMembers, "Default markClassMembers should be false");
        assertFalse(result.markConditionally, "Default markConditionally should be false");
        assertFalse(result.markDescriptorClasses, "Default markDescriptorClasses should be false");
        assertFalse(result.markCodeAttributes, "Default markCodeAttributes should be false");
        assertFalse(result.allowShrinking, "Default allowShrinking should be false");
        assertFalse(result.allowOptimization, "Default allowOptimization should be false");
        assertFalse(result.allowObfuscation, "Default allowObfuscation should be false");
    }

    /**
     * Test getKeepSpecification after setting all flags to true.
     */
    @Test
    public void testGetKeepSpecificationAfterSettingAllTrue() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        ClassSpecification classSpec = new ClassSpecification(
                null, 0, 0, null, "TestClass", null, null
        );

        KeepClassSpecification keepSpec = new KeepClassSpecification(
                true, true, true, true, true, true, true, true,
                null, classSpec
        );

        dialog.setKeepSpecification(keepSpec);
        KeepClassSpecification result = dialog.getKeepSpecification();

        assertTrue(result.markClasses, "markClasses should be true");
        assertTrue(result.markClassMembers, "markClassMembers should be true");
        assertTrue(result.markConditionally, "markConditionally should be true");
        assertTrue(result.markDescriptorClasses, "markDescriptorClasses should be true");
        assertTrue(result.markCodeAttributes, "markCodeAttributes should be true");
        assertTrue(result.allowShrinking, "allowShrinking should be true");
        assertTrue(result.allowOptimization, "allowOptimization should be true");
        assertTrue(result.allowObfuscation, "allowObfuscation should be true");
    }

    /**
     * Test getKeepSpecification after setting all flags to false.
     */
    @Test
    public void testGetKeepSpecificationAfterSettingAllFalse() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        ClassSpecification classSpec = new ClassSpecification(
                null, 0, 0, null, "TestClass", null, null
        );

        KeepClassSpecification keepSpec = new KeepClassSpecification(
                false, false, false, false, false, false, false, false,
                null, classSpec
        );

        dialog.setKeepSpecification(keepSpec);
        KeepClassSpecification result = dialog.getKeepSpecification();

        assertFalse(result.markClasses, "markClasses should be false");
        assertFalse(result.markClassMembers, "markClassMembers should be false");
        assertFalse(result.markConditionally, "markConditionally should be false");
        assertFalse(result.markDescriptorClasses, "markDescriptorClasses should be false");
        assertFalse(result.markCodeAttributes, "markCodeAttributes should be false");
        assertFalse(result.allowShrinking, "allowShrinking should be false");
        assertFalse(result.allowOptimization, "allowOptimization should be false");
        assertFalse(result.allowObfuscation, "allowObfuscation should be false");
    }

    /**
     * Test getKeepSpecification retrieves mixed boolean values correctly.
     */
    @Test
    public void testGetKeepSpecificationWithMixedBooleans() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        ClassSpecification classSpec = new ClassSpecification(
                null, 0, 0, null, "TestClass", null, null
        );

        KeepClassSpecification keepSpec = new KeepClassSpecification(
                true, false, true, false, true, false, true, false,
                null, classSpec
        );

        dialog.setKeepSpecification(keepSpec);
        KeepClassSpecification result = dialog.getKeepSpecification();

        assertTrue(result.markClasses, "markClasses should be true");
        assertFalse(result.markClassMembers, "markClassMembers should be false");
        assertTrue(result.markConditionally, "markConditionally should be true");
        assertFalse(result.markDescriptorClasses, "markDescriptorClasses should be false");
        assertTrue(result.markCodeAttributes, "markCodeAttributes should be true");
        assertFalse(result.allowShrinking, "allowShrinking should be false");
        assertTrue(result.allowOptimization, "allowOptimization should be true");
        assertFalse(result.allowObfuscation, "allowObfuscation should be false");
    }

    /**
     * Test getKeepSpecification retrieves ClassSpecification data correctly.
     */
    @Test
    public void testGetKeepSpecificationRetrievesClassSpecification() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        String testComments = "Test comments";
        String testClassName = "com/example/TestClass";

        ClassSpecification classSpec = new ClassSpecification(
                testComments, 0, 0, null, testClassName, null, null
        );

        KeepClassSpecification keepSpec = new KeepClassSpecification(
                true, true, false, false, false, false, false, false,
                null, classSpec
        );

        dialog.setKeepSpecification(keepSpec);
        KeepClassSpecification result = dialog.getKeepSpecification();

        assertEquals(testComments, result.comments, "Comments should be preserved");
        assertEquals("com.example.TestClass", result.className, "Class name should be preserved");
    }

    /**
     * Test getKeepSpecification with a non-null condition.
     */
    @Test
    public void testGetKeepSpecificationWithCondition() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        ClassSpecification classSpec = new ClassSpecification(
                null, 0, 0, null, "TestClass", null, null
        );

        ClassSpecification condition = new ClassSpecification(
                null, 0, 0, null, "ConditionClass", null, null
        );

        KeepClassSpecification keepSpec = new KeepClassSpecification(
                true, false, false, false, false, false, false, false,
                condition, classSpec
        );

        dialog.setKeepSpecification(keepSpec);
        KeepClassSpecification result = dialog.getKeepSpecification();

        assertNotNull(result.condition, "Condition should not be null");
    }

    /**
     * Test getKeepSpecification with null condition.
     */
    @Test
    public void testGetKeepSpecificationWithNullCondition() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        ClassSpecification classSpec = new ClassSpecification(
                null, 0, 0, null, "TestClass", null, null
        );

        KeepClassSpecification keepSpec = new KeepClassSpecification(
                true, false, false, false, false, false, false, false,
                null, classSpec
        );

        dialog.setKeepSpecification(keepSpec);
        KeepClassSpecification result = dialog.getKeepSpecification();

        // Condition should be null when not set
        assertNull(result.condition, "Condition should be null when not set");
    }

    /**
     * Test getKeepSpecification retrieves access flags correctly.
     */
    @Test
    public void testGetKeepSpecificationRetrievesAccessFlags() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        ClassSpecification classSpec = new ClassSpecification(
                null,
                AccessConstants.PUBLIC | AccessConstants.FINAL,
                0,
                null, "TestClass",
                null, null
        );

        KeepClassSpecification keepSpec = new KeepClassSpecification(
                true, false, false, false, false, false, false, false,
                null, classSpec
        );

        dialog.setKeepSpecification(keepSpec);
        KeepClassSpecification result = dialog.getKeepSpecification();

        assertTrue((result.requiredSetAccessFlags & AccessConstants.PUBLIC) != 0,
                   "Public flag should be set");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.FINAL) != 0,
                   "Final flag should be set");
    }

    /**
     * Test getKeepSpecification can be called multiple times and returns consistent results.
     */
    @Test
    public void testGetKeepSpecificationMultipleCalls() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        ClassSpecification classSpec = new ClassSpecification(
                "Test", 0, 0, null, "TestClass", null, null
        );

        KeepClassSpecification keepSpec = new KeepClassSpecification(
                true, true, false, false, false, false, false, false,
                null, classSpec
        );

        dialog.setKeepSpecification(keepSpec);

        // Call getKeepSpecification multiple times
        KeepClassSpecification result1 = dialog.getKeepSpecification();
        KeepClassSpecification result2 = dialog.getKeepSpecification();

        // Results should have the same values (though they are different objects)
        assertEquals(result1.markClasses, result2.markClasses, "markClasses should be consistent");
        assertEquals(result1.markClassMembers, result2.markClassMembers, "markClassMembers should be consistent");
        assertEquals(result1.comments, result2.comments, "Comments should be consistent");
        assertEquals(result1.className, result2.className, "Class name should be consistent");
    }

    /**
     * Test getKeepSpecification with only markClasses set.
     */
    @Test
    public void testGetKeepSpecificationWithOnlyMarkClasses() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        ClassSpecification classSpec = new ClassSpecification(
                null, 0, 0, null, "*", null, null
        );

        KeepClassSpecification keepSpec = new KeepClassSpecification(
                true, false, false, false, false, false, false, false,
                null, classSpec
        );

        dialog.setKeepSpecification(keepSpec);
        KeepClassSpecification result = dialog.getKeepSpecification();

        assertTrue(result.markClasses, "Only markClasses should be true");
        assertFalse(result.markClassMembers, "markClassMembers should be false");
        assertFalse(result.markConditionally, "markConditionally should be false");
        assertFalse(result.markDescriptorClasses, "markDescriptorClasses should be false");
        assertFalse(result.markCodeAttributes, "markCodeAttributes should be false");
        assertFalse(result.allowShrinking, "allowShrinking should be false");
        assertFalse(result.allowOptimization, "allowOptimization should be false");
        assertFalse(result.allowObfuscation, "allowObfuscation should be false");
    }

    /**
     * Test getKeepSpecification with only allowObfuscation set.
     */
    @Test
    public void testGetKeepSpecificationWithOnlyAllowObfuscation() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        ClassSpecification classSpec = new ClassSpecification(
                null, 0, 0, null, "*", null, null
        );

        KeepClassSpecification keepSpec = new KeepClassSpecification(
                false, false, false, false, false, false, false, true,
                null, classSpec
        );

        dialog.setKeepSpecification(keepSpec);
        KeepClassSpecification result = dialog.getKeepSpecification();

        assertFalse(result.markClasses, "markClasses should be false");
        assertFalse(result.markClassMembers, "markClassMembers should be false");
        assertFalse(result.markConditionally, "markConditionally should be false");
        assertFalse(result.markDescriptorClasses, "markDescriptorClasses should be false");
        assertFalse(result.markCodeAttributes, "markCodeAttributes should be false");
        assertFalse(result.allowShrinking, "allowShrinking should be false");
        assertFalse(result.allowOptimization, "allowOptimization should be false");
        assertTrue(result.allowObfuscation, "Only allowObfuscation should be true");
    }

    /**
     * Test getKeepSpecification with annotation type in ClassSpecification.
     */
    @Test
    public void testGetKeepSpecificationWithAnnotationType() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        ClassSpecification classSpec = new ClassSpecification(
                null, 0, 0,
                "Ljava/lang/Deprecated;", "TestClass",
                null, null
        );

        KeepClassSpecification keepSpec = new KeepClassSpecification(
                true, false, false, false, false, false, false, false,
                null, classSpec
        );

        dialog.setKeepSpecification(keepSpec);
        KeepClassSpecification result = dialog.getKeepSpecification();

        assertNotNull(result.annotationType, "Annotation type should be preserved");
    }

    /**
     * Test getKeepSpecification with extends clause in ClassSpecification.
     */
    @Test
    public void testGetKeepSpecificationWithExtendsClause() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        ClassSpecification classSpec = new ClassSpecification(
                null, 0, 0,
                null, "TestClass",
                null, "java/lang/Object"
        );

        KeepClassSpecification keepSpec = new KeepClassSpecification(
                true, false, false, false, false, false, false, false,
                null, classSpec
        );

        dialog.setKeepSpecification(keepSpec);
        KeepClassSpecification result = dialog.getKeepSpecification();

        assertEquals("java.lang.Object", result.extendsClassName,
                     "Extends class name should be preserved");
    }

    /**
     * Test getKeepSpecification returns a new instance each time (not the same object).
     */
    @Test
    public void testGetKeepSpecificationReturnsNewInstance() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        KeepClassSpecification result1 = dialog.getKeepSpecification();
        KeepClassSpecification result2 = dialog.getKeepSpecification();

        assertNotSame(result1, result2, "Each call should return a new instance");
    }

    /**
     * Test getKeepSpecification after setting values multiple times.
     */
    @Test
    public void testGetKeepSpecificationAfterMultipleSets() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        // First set
        ClassSpecification classSpec1 = new ClassSpecification(
                "First", 0, 0, null, "FirstClass", null, null
        );
        KeepClassSpecification keepSpec1 = new KeepClassSpecification(
                true, true, true, true, true, true, true, true,
                null, classSpec1
        );
        dialog.setKeepSpecification(keepSpec1);

        // Second set
        ClassSpecification classSpec2 = new ClassSpecification(
                "Second", 0, 0, null, "SecondClass", null, null
        );
        KeepClassSpecification keepSpec2 = new KeepClassSpecification(
                false, false, false, false, false, false, false, false,
                null, classSpec2
        );
        dialog.setKeepSpecification(keepSpec2);

        // Get the final state
        KeepClassSpecification result = dialog.getKeepSpecification();

        // Should reflect the second set values
        assertFalse(result.markClasses, "Should reflect second set (false)");
        assertFalse(result.markClassMembers, "Should reflect second set (false)");
        assertEquals("Second", result.comments, "Should reflect second set comments");
        assertEquals("SecondClass", result.className, "Should reflect second set class name");
    }

    /**
     * Test getKeepSpecification with empty ClassSpecification.
     */
    @Test
    public void testGetKeepSpecificationWithEmptyClassSpec() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        ClassSpecification classSpec = new ClassSpecification();

        KeepClassSpecification keepSpec = new KeepClassSpecification(
                true, false, false, false, false, false, false, false,
                null, classSpec
        );

        dialog.setKeepSpecification(keepSpec);
        KeepClassSpecification result = dialog.getKeepSpecification();

        assertNotNull(result, "Result should not be null even with empty ClassSpecification");
        assertTrue(result.markClasses, "markClasses flag should still be retrievable");
    }

    /**
     * Test getKeepSpecification with all allow flags set.
     */
    @Test
    public void testGetKeepSpecificationWithAllAllowFlags() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        ClassSpecification classSpec = new ClassSpecification(
                null, 0, 0, null, "TestClass", null, null
        );

        KeepClassSpecification keepSpec = new KeepClassSpecification(
                false, false, false, false, false,
                true, true, true,  // All allow flags
                null, classSpec
        );

        dialog.setKeepSpecification(keepSpec);
        KeepClassSpecification result = dialog.getKeepSpecification();

        assertTrue(result.allowShrinking, "allowShrinking should be true");
        assertTrue(result.allowOptimization, "allowOptimization should be true");
        assertTrue(result.allowObfuscation, "allowObfuscation should be true");
    }

    /**
     * Test getKeepSpecification with all mark flags set.
     */
    @Test
    public void testGetKeepSpecificationWithAllMarkFlags() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        ClassSpecification classSpec = new ClassSpecification(
                null, 0, 0, null, "TestClass", null, null
        );

        KeepClassSpecification keepSpec = new KeepClassSpecification(
                true, true, true, true, true,  // All mark flags
                false, false, false,
                null, classSpec
        );

        dialog.setKeepSpecification(keepSpec);
        KeepClassSpecification result = dialog.getKeepSpecification();

        assertTrue(result.markClasses, "markClasses should be true");
        assertTrue(result.markClassMembers, "markClassMembers should be true");
        assertTrue(result.markConditionally, "markConditionally should be true");
        assertTrue(result.markDescriptorClasses, "markDescriptorClasses should be true");
        assertTrue(result.markCodeAttributes, "markCodeAttributes should be true");
    }

    /**
     * Test getKeepSpecification with complex ClassSpecification including access flags.
     */
    @Test
    public void testGetKeepSpecificationWithComplexClassSpec() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        ClassSpecification classSpec = new ClassSpecification(
                "Complex class specification",
                AccessConstants.PUBLIC | AccessConstants.ABSTRACT,
                0,
                "Ljava/lang/annotation/Documented;",
                "com/example/ComplexClass",
                "Ljava/lang/annotation/Retention;",
                "java/lang/Object"
        );

        KeepClassSpecification keepSpec = new KeepClassSpecification(
                true, true, false, false, false, false, false, false,
                null, classSpec
        );

        dialog.setKeepSpecification(keepSpec);
        KeepClassSpecification result = dialog.getKeepSpecification();

        assertNotNull(result, "Result should not be null");
        assertEquals("Complex class specification", result.comments, "Comments should be preserved");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.PUBLIC) != 0,
                   "Public flag should be preserved");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.ABSTRACT) != 0,
                   "Abstract flag should be preserved");
        assertNotNull(result.annotationType, "Annotation type should be preserved");
        assertEquals("com.example.ComplexClass", result.className, "Class name should be preserved");
        assertNotNull(result.extendsAnnotationType, "Extends annotation type should be preserved");
        assertEquals("java.lang.Object", result.extendsClassName, "Extends class name should be preserved");
    }
}
