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
 * Test class for ClassSpecificationDialog.setKeepSpecification() method.
 *
 * The setKeepSpecification method:
 * - Takes a KeepClassSpecification parameter
 * - Sets various UI checkboxes based on the specification's boolean fields
 * - Sets the condition field and nested condition dialog
 * - Delegates to setClassSpecification for the ClassSpecification portion
 *
 * These tests verify:
 * - The method correctly sets all boolean flags (markClasses, markClassMembers, etc.)
 * - The method handles null conditions properly
 * - The method handles non-null conditions correctly
 * - The method sets the underlying ClassSpecification fields correctly
 * - The values can be retrieved back using getKeepSpecification
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class ClassSpecificationDialogClaude_setKeepSpecificationTest {

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
     * Test setKeepSpecification with all boolean flags set to true.
     */
    @Test
    public void testSetKeepSpecificationAllTrue() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        // Create a ClassSpecification
        ClassSpecification classSpec = new ClassSpecification(
                "Test comments",
                0, 0,
                null, "TestClass",
                null, null
        );

        // Create a KeepClassSpecification with all flags true
        KeepClassSpecification keepSpec = new KeepClassSpecification(
                true,  // markClasses
                true,  // markClassMembers
                true,  // markConditionally
                true,  // markDescriptorClasses
                true,  // markCodeAttributes
                true,  // allowShrinking
                true,  // allowOptimization
                true,  // allowObfuscation
                null,  // condition
                classSpec
        );

        // Set the specification
        dialog.setKeepSpecification(keepSpec);

        // Retrieve and verify
        KeepClassSpecification result = dialog.getKeepSpecification();
        assertNotNull(result, "Result should not be null");
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
     * Test setKeepSpecification with all boolean flags set to false.
     */
    @Test
    public void testSetKeepSpecificationAllFalse() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        // Create a ClassSpecification
        ClassSpecification classSpec = new ClassSpecification(
                null,
                0, 0,
                null, "TestClass",
                null, null
        );

        // Create a KeepClassSpecification with all flags false
        KeepClassSpecification keepSpec = new KeepClassSpecification(
                false, // markClasses
                false, // markClassMembers
                false, // markConditionally
                false, // markDescriptorClasses
                false, // markCodeAttributes
                false, // allowShrinking
                false, // allowOptimization
                false, // allowObfuscation
                null,  // condition
                classSpec
        );

        // Set the specification
        dialog.setKeepSpecification(keepSpec);

        // Retrieve and verify
        KeepClassSpecification result = dialog.getKeepSpecification();
        assertNotNull(result, "Result should not be null");
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
     * Test setKeepSpecification with mixed boolean flag values.
     */
    @Test
    public void testSetKeepSpecificationMixedFlags() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        ClassSpecification classSpec = new ClassSpecification(
                "Comments",
                0, 0,
                null, "MyClass",
                null, null
        );

        // Create specification with alternating true/false values
        KeepClassSpecification keepSpec = new KeepClassSpecification(
                true,  // markClasses
                false, // markClassMembers
                true,  // markConditionally
                false, // markDescriptorClasses
                true,  // markCodeAttributes
                false, // allowShrinking
                true,  // allowOptimization
                false, // allowObfuscation
                null,
                classSpec
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
     * Test setKeepSpecification with a non-null condition.
     */
    @Test
    public void testSetKeepSpecificationWithCondition() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        ClassSpecification classSpec = new ClassSpecification(
                null,
                0, 0,
                null, "TestClass",
                null, null
        );

        // Create a condition
        ClassSpecification condition = new ClassSpecification(
                null,
                0, 0,
                null, "ConditionClass",
                null, null
        );

        KeepClassSpecification keepSpec = new KeepClassSpecification(
                true, true, false, false, false,
                false, false, false,
                condition,  // non-null condition
                classSpec
        );

        dialog.setKeepSpecification(keepSpec);

        KeepClassSpecification result = dialog.getKeepSpecification();
        assertNotNull(result, "Result should not be null");
        assertNotNull(result.condition, "Condition should not be null");
    }

    /**
     * Test setKeepSpecification preserves the ClassSpecification data.
     */
    @Test
    public void testSetKeepSpecificationPreservesClassSpecification() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        String testComments = "Test comments for class";
        String testClassName = "com.example.TestClass";

        ClassSpecification classSpec = new ClassSpecification(
                testComments,
                0, 0,
                null, testClassName,
                null, null
        );

        KeepClassSpecification keepSpec = new KeepClassSpecification(
                true, true, false, false, false,
                false, false, false,
                null,
                classSpec
        );

        dialog.setKeepSpecification(keepSpec);

        KeepClassSpecification result = dialog.getKeepSpecification();
        assertNotNull(result, "Result should not be null");
        assertEquals(testComments, result.comments, "Comments should be preserved");
        assertEquals(testClassName, result.className, "Class name should be preserved");
    }

    /**
     * Test setKeepSpecification can be called multiple times with different specifications.
     */
    @Test
    public void testSetKeepSpecificationMultipleTimes() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        // First specification - all true
        ClassSpecification classSpec1 = new ClassSpecification(
                "First",
                0, 0,
                null, "Class1",
                null, null
        );
        KeepClassSpecification keepSpec1 = new KeepClassSpecification(
                true, true, true, true, true, true, true, true,
                null, classSpec1
        );
        dialog.setKeepSpecification(keepSpec1);

        KeepClassSpecification result1 = dialog.getKeepSpecification();
        assertTrue(result1.markClasses, "First call: markClasses should be true");
        assertTrue(result1.allowShrinking, "First call: allowShrinking should be true");

        // Second specification - all false
        ClassSpecification classSpec2 = new ClassSpecification(
                "Second",
                0, 0,
                null, "Class2",
                null, null
        );
        KeepClassSpecification keepSpec2 = new KeepClassSpecification(
                false, false, false, false, false, false, false, false,
                null, classSpec2
        );
        dialog.setKeepSpecification(keepSpec2);

        KeepClassSpecification result2 = dialog.getKeepSpecification();
        assertFalse(result2.markClasses, "Second call: markClasses should be false");
        assertFalse(result2.allowShrinking, "Second call: allowShrinking should be false");
    }

    /**
     * Test setKeepSpecification with empty ClassSpecification.
     */
    @Test
    public void testSetKeepSpecificationWithEmptyClassSpec() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        // Create empty ClassSpecification
        ClassSpecification classSpec = new ClassSpecification();

        KeepClassSpecification keepSpec = new KeepClassSpecification(
                true, false, true, false, true,
                false, true, false,
                null,
                classSpec
        );

        dialog.setKeepSpecification(keepSpec);

        KeepClassSpecification result = dialog.getKeepSpecification();
        assertNotNull(result, "Result should not be null");
        assertTrue(result.markClasses, "markClasses should be true");
        assertFalse(result.markClassMembers, "markClassMembers should be false");
    }

    /**
     * Test setKeepSpecification handles null condition properly.
     */
    @Test
    public void testSetKeepSpecificationWithNullCondition() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        ClassSpecification classSpec = new ClassSpecification(
                null,
                0, 0,
                null, "TestClass",
                null, null
        );

        KeepClassSpecification keepSpec = new KeepClassSpecification(
                true, true, false, false, false,
                false, false, false,
                null,  // null condition
                classSpec
        );

        dialog.setKeepSpecification(keepSpec);

        // Should not throw exception
        KeepClassSpecification result = dialog.getKeepSpecification();
        assertNotNull(result, "Result should not be null");
    }

    /**
     * Test that setKeepSpecification updates override previous values completely.
     */
    @Test
    public void testSetKeepSpecificationOverridesPreviousValues() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        // Set first specification with specific pattern
        ClassSpecification classSpec1 = new ClassSpecification(
                "Original comments",
                0, 0,
                null, "OriginalClass",
                null, null
        );
        KeepClassSpecification keepSpec1 = new KeepClassSpecification(
                true, false, true, false, true,
                false, true, false,
                null, classSpec1
        );
        dialog.setKeepSpecification(keepSpec1);

        // Set second specification with opposite pattern
        ClassSpecification classSpec2 = new ClassSpecification(
                "New comments",
                0, 0,
                null, "NewClass",
                null, null
        );
        KeepClassSpecification keepSpec2 = new KeepClassSpecification(
                false, true, false, true, false,
                true, false, true,
                null, classSpec2
        );
        dialog.setKeepSpecification(keepSpec2);

        // Verify the second specification completely replaced the first
        KeepClassSpecification result = dialog.getKeepSpecification();
        assertFalse(result.markClasses, "markClasses should match second spec");
        assertTrue(result.markClassMembers, "markClassMembers should match second spec");
        assertFalse(result.markConditionally, "markConditionally should match second spec");
        assertTrue(result.markDescriptorClasses, "markDescriptorClasses should match second spec");
        assertFalse(result.markCodeAttributes, "markCodeAttributes should match second spec");
        assertTrue(result.allowShrinking, "allowShrinking should match second spec");
        assertFalse(result.allowOptimization, "allowOptimization should match second spec");
        assertTrue(result.allowObfuscation, "allowObfuscation should match second spec");
        assertEquals("New comments", result.comments, "Comments should match second spec");
    }

    /**
     * Test setKeepSpecification with ClassSpecification containing extends clause.
     */
    @Test
    public void testSetKeepSpecificationWithExtendsClause() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        ClassSpecification classSpec = new ClassSpecification(
                null,
                0, 0,
                null, "MyClass",
                null, "ParentClass"  // extends clause
        );

        KeepClassSpecification keepSpec = new KeepClassSpecification(
                true, true, false, false, false,
                false, false, false,
                null,
                classSpec
        );

        dialog.setKeepSpecification(keepSpec);

        KeepClassSpecification result = dialog.getKeepSpecification();
        assertNotNull(result, "Result should not be null");
        assertEquals("ParentClass", result.extendsClassName, "Extends class name should be preserved");
    }

    /**
     * Test setKeepSpecification with ClassSpecification containing annotation type.
     */
    @Test
    public void testSetKeepSpecificationWithAnnotationType() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        ClassSpecification classSpec = new ClassSpecification(
                null,
                0, 0,
                "Lannotation/MyAnnotation;", "MyClass",  // annotation type
                null, null
        );

        KeepClassSpecification keepSpec = new KeepClassSpecification(
                true, true, false, false, false,
                false, false, false,
                null,
                classSpec
        );

        dialog.setKeepSpecification(keepSpec);

        KeepClassSpecification result = dialog.getKeepSpecification();
        assertNotNull(result, "Result should not be null");
        assertNotNull(result.annotationType, "Annotation type should be preserved");
    }

    /**
     * Test setKeepSpecification with only markClasses flag set.
     */
    @Test
    public void testSetKeepSpecificationOnlyMarkClasses() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        ClassSpecification classSpec = new ClassSpecification(
                null, 0, 0, null, "*", null, null
        );

        KeepClassSpecification keepSpec = new KeepClassSpecification(
                true,  // Only this is true
                false, false, false, false, false, false, false,
                null, classSpec
        );

        dialog.setKeepSpecification(keepSpec);

        KeepClassSpecification result = dialog.getKeepSpecification();
        assertTrue(result.markClasses, "markClasses should be true");
        assertFalse(result.markClassMembers, "markClassMembers should be false");
        assertFalse(result.markConditionally, "markConditionally should be false");
        assertFalse(result.markDescriptorClasses, "markDescriptorClasses should be false");
        assertFalse(result.markCodeAttributes, "markCodeAttributes should be false");
        assertFalse(result.allowShrinking, "allowShrinking should be false");
        assertFalse(result.allowOptimization, "allowOptimization should be false");
        assertFalse(result.allowObfuscation, "allowObfuscation should be false");
    }

    /**
     * Test setKeepSpecification with only allowObfuscation flag set.
     */
    @Test
    public void testSetKeepSpecificationOnlyAllowObfuscation() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, true, true);

        ClassSpecification classSpec = new ClassSpecification(
                null, 0, 0, null, "*", null, null
        );

        KeepClassSpecification keepSpec = new KeepClassSpecification(
                false, false, false, false, false, false, false,
                true,  // Only this is true
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
        assertTrue(result.allowObfuscation, "allowObfuscation should be true");
    }
}
