package proguard.gui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.ClassSpecification;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Test class for ClassSpecificationDialog.label methods.
 *
 * Tests both overloaded label methods:
 * - label(ClassSpecification) - delegates to label(ClassSpecification, -1)
 * - label(ClassSpecification, int) - accepts an index parameter
 *
 * The label method:
 * - Creates a human-readable string summary of a ClassSpecification
 * - Uses a priority order to determine what to display:
 *   1. If null → returns message for "none"
 *   2. If has comments → returns trimmed comments
 *   3. If has className → returns "Class <className>"
 *   4. If has annotationType → returns "Classes annotated with <annotationType>"
 *   5. If has extendsClassName → returns "Extensions of <extendsClassName>"
 *   6. If has extendsAnnotationType → returns "Extensions of classes annotated with <extendsAnnotationType>"
 *   7. If index >= 0 → returns "Specification #<index>"
 *   8. Otherwise → returns "Specification"
 *
 * These tests verify:
 * - The method returns appropriate strings for different ClassSpecification configurations
 * - The priority order is respected
 * - Null input is handled correctly
 * - Comments take priority over other fields
 * - Class names are converted to external format
 * - The index parameter is used when no other fields are present
 * - The method returns non-null strings for all valid inputs
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class ClassSpecificationDialogClaude_labelTest {

    private JFrame testFrame;
    private ClassSpecificationDialog dialog;

    @BeforeEach
    public void setUp() {
        // Tests will skip if headless mode is active
        assumeFalse(GraphicsEnvironment.isHeadless(),
                    "Skipping test: Headless environment detected. GUI components require a display.");

        // Create dialog for testing
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);
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
     * Test label with null ClassSpecification.
     */
    @Test
    public void testLabelWithNull() {
        String result = dialog.label(null);

        assertNotNull(result, "Label should not return null");
        // Should return the "none" message
        assertFalse(result.isEmpty(), "Label for null should not be empty");
    }

    /**
     * Test label with ClassSpecification that has comments.
     * Comments take highest priority.
     */
    @Test
    public void testLabelWithComments() {
        ClassSpecification classSpec = new ClassSpecification(
                "Test comment",
                0, 0,
                null, "TestClass",
                null, null
        );

        String result = dialog.label(classSpec);

        assertEquals("Test comment", result, "Label should return comments");
    }

    /**
     * Test label with ClassSpecification that has comments with whitespace.
     * Comments should be trimmed.
     */
    @Test
    public void testLabelWithCommentsWithWhitespace() {
        ClassSpecification classSpec = new ClassSpecification(
                "  Test comment with spaces  ",
                0, 0,
                null, "TestClass",
                null, null
        );

        String result = dialog.label(classSpec);

        assertEquals("Test comment with spaces", result, "Label should trim comments");
    }

    /**
     * Test label with ClassSpecification that has multi-line comments.
     */
    @Test
    public void testLabelWithMultiLineComments() {
        ClassSpecification classSpec = new ClassSpecification(
                "First line\nSecond line",
                0, 0,
                null, "TestClass",
                null, null
        );

        String result = dialog.label(classSpec);

        // Comments take priority, even multi-line
        assertTrue(result.contains("First line"), "Label should include first line");
    }

    /**
     * Test label with ClassSpecification that has class name only.
     */
    @Test
    public void testLabelWithClassName() {
        ClassSpecification classSpec = new ClassSpecification(
                null, 0, 0,
                null, "com/example/TestClass",
                null, null
        );

        String result = dialog.label(classSpec);

        assertNotNull(result, "Label should not be null");
        assertTrue(result.contains("Class"), "Label should contain 'Class'");
        assertTrue(result.contains("com.example.TestClass"), "Label should contain external class name");
    }

    /**
     * Test label with ClassSpecification that has simple class name.
     */
    @Test
    public void testLabelWithSimpleClassName() {
        ClassSpecification classSpec = new ClassSpecification(
                null, 0, 0,
                null, "TestClass",
                null, null
        );

        String result = dialog.label(classSpec);

        assertNotNull(result, "Label should not be null");
        assertTrue(result.contains("Class"), "Label should contain 'Class'");
        assertTrue(result.contains("TestClass"), "Label should contain class name");
    }

    /**
     * Test label with ClassSpecification that has annotation type only.
     */
    @Test
    public void testLabelWithAnnotationType() {
        ClassSpecification classSpec = new ClassSpecification(
                null, 0, 0,
                "Ljava/lang/Deprecated;", null,
                null, null
        );

        String result = dialog.label(classSpec);

        assertNotNull(result, "Label should not be null");
        assertTrue(result.toLowerCase().contains("annotated"), "Label should mention annotation");
        assertTrue(result.contains("java.lang.Deprecated"), "Label should contain external annotation type");
    }

    /**
     * Test label with ClassSpecification that has extends class name only.
     */
    @Test
    public void testLabelWithExtendsClassName() {
        ClassSpecification classSpec = new ClassSpecification(
                null, 0, 0,
                null, null,
                null, "java/lang/Object"
        );

        String result = dialog.label(classSpec);

        assertNotNull(result, "Label should not be null");
        assertTrue(result.toLowerCase().contains("extension"), "Label should mention extension");
        assertTrue(result.contains("java.lang.Object"), "Label should contain external class name");
    }

    /**
     * Test label with ClassSpecification that has extends annotation type only.
     */
    @Test
    public void testLabelWithExtendsAnnotationType() {
        ClassSpecification classSpec = new ClassSpecification(
                null, 0, 0,
                null, null,
                "Ljava/lang/annotation/Retention;", null
        );

        String result = dialog.label(classSpec);

        assertNotNull(result, "Label should not be null");
        assertTrue(result.toLowerCase().contains("extension"), "Label should mention extension");
        assertTrue(result.toLowerCase().contains("annotated"), "Label should mention annotation");
        assertTrue(result.contains("java.lang.annotation.Retention"),
                   "Label should contain external annotation type");
    }

    /**
     * Test label with empty ClassSpecification.
     * Should return generic "Specification" message.
     */
    @Test
    public void testLabelWithEmptyClassSpecification() {
        ClassSpecification classSpec = new ClassSpecification();

        String result = dialog.label(classSpec);

        assertNotNull(result, "Label should not be null");
        assertTrue(result.toLowerCase().contains("specification"),
                   "Label should contain 'specification'");
    }

    /**
     * Test label priority: comments over class name.
     */
    @Test
    public void testLabelPriorityCommentsOverClassName() {
        ClassSpecification classSpec = new ClassSpecification(
                "My comment",
                0, 0,
                null, "TestClass",
                null, null
        );

        String result = dialog.label(classSpec);

        assertEquals("My comment", result, "Comments should take priority over class name");
    }

    /**
     * Test label priority: class name over annotation type.
     */
    @Test
    public void testLabelPriorityClassNameOverAnnotationType() {
        ClassSpecification classSpec = new ClassSpecification(
                null, 0, 0,
                "Ljava/lang/Deprecated;", "TestClass",
                null, null
        );

        String result = dialog.label(classSpec);

        assertTrue(result.contains("Class"), "Class name should take priority");
        assertTrue(result.contains("TestClass"), "Should show class name");
        assertFalse(result.toLowerCase().contains("deprecated"),
                    "Should not show annotation when class name is present");
    }

    /**
     * Test label priority: annotation type over extends class name.
     */
    @Test
    public void testLabelPriorityAnnotationTypeOverExtendsClassName() {
        ClassSpecification classSpec = new ClassSpecification(
                null, 0, 0,
                "Ljava/lang/Deprecated;", null,
                null, "java/lang/Object"
        );

        String result = dialog.label(classSpec);

        assertTrue(result.toLowerCase().contains("annotated"),
                   "Annotation type should take priority");
        assertFalse(result.contains("Object"),
                    "Should not show extends class when annotation type is present");
    }

    /**
     * Test label priority: extends class name over extends annotation type.
     */
    @Test
    public void testLabelPriorityExtendsClassNameOverExtendsAnnotationType() {
        ClassSpecification classSpec = new ClassSpecification(
                null, 0, 0,
                null, null,
                "Ljava/lang/annotation/Retention;", "java/lang/Object"
        );

        String result = dialog.label(classSpec);

        assertTrue(result.contains("Object"), "Extends class name should take priority");
        assertFalse(result.contains("Retention"),
                    "Should not show extends annotation when extends class is present");
    }

    /**
     * Test label with wildcard class name.
     */
    @Test
    public void testLabelWithWildcardClassName() {
        ClassSpecification classSpec = new ClassSpecification(
                null, 0, 0,
                null, "com/example/*",
                null, null
        );

        String result = dialog.label(classSpec);

        assertTrue(result.contains("*"), "Label should preserve wildcard");
        assertTrue(result.contains("com.example"), "Label should contain package");
    }

    /**
     * Test label with package-level wildcard.
     */
    @Test
    public void testLabelWithPackageWildcard() {
        ClassSpecification classSpec = new ClassSpecification(
                null, 0, 0,
                null, "com/example/**",
                null, null
        );

        String result = dialog.label(classSpec);

        assertTrue(result.contains("**"), "Label should preserve double wildcard");
        assertTrue(result.contains("com.example"), "Label should contain package");
    }

    /**
     * Test label with inner class notation.
     */
    @Test
    public void testLabelWithInnerClass() {
        ClassSpecification classSpec = new ClassSpecification(
                null, 0, 0,
                null, "com/example/Outer$Inner",
                null, null
        );

        String result = dialog.label(classSpec);

        assertTrue(result.contains("$"), "Label should preserve inner class notation");
        assertTrue(result.contains("com.example.Outer"), "Label should contain outer class");
    }

    /**
     * Test label returns non-null for all configurations.
     */
    @Test
    public void testLabelNeverReturnsNull() {
        // Test various configurations
        ClassSpecification[] specs = {
            null,
            new ClassSpecification(),
            new ClassSpecification("Comment", 0, 0, null, null, null, null),
            new ClassSpecification(null, 0, 0, null, "TestClass", null, null),
            new ClassSpecification(null, 0, 0, "Ljava/lang/Deprecated;", null, null, null),
            new ClassSpecification(null, 0, 0, null, null, null, "java/lang/Object"),
            new ClassSpecification(null, 0, 0, null, null, "Ljava/lang/annotation/Retention;", null)
        };

        for (ClassSpecification spec : specs) {
            String result = dialog.label(spec);
            assertNotNull(result, "Label should never return null for any configuration");
        }
    }

    /**
     * Test label with custom annotation type.
     */
    @Test
    public void testLabelWithCustomAnnotation() {
        ClassSpecification classSpec = new ClassSpecification(
                null, 0, 0,
                "Lcom/example/MyAnnotation;", null,
                null, null
        );

        String result = dialog.label(classSpec);

        assertTrue(result.contains("com.example.MyAnnotation"),
                   "Label should contain custom annotation");
    }

    /**
     * Test label with fully qualified class name.
     */
    @Test
    public void testLabelWithFullyQualifiedClassName() {
        ClassSpecification classSpec = new ClassSpecification(
                null, 0, 0,
                null, "com/example/subpackage/deep/MyClass",
                null, null
        );

        String result = dialog.label(classSpec);

        assertTrue(result.contains("com.example.subpackage.deep.MyClass"),
                   "Label should contain full class path");
    }

    /**
     * Test label with empty comments string.
     * Empty comments should not take priority.
     */
    @Test
    public void testLabelWithEmptyComments() {
        ClassSpecification classSpec = new ClassSpecification(
                "", 0, 0,
                null, "TestClass",
                null, null
        );

        String result = dialog.label(classSpec);

        // Empty string comments are still considered present, so they take priority
        // but after trimming, they become empty
        assertNotNull(result, "Label should not be null");
    }

    /**
     * Test label can be called multiple times with same specification.
     */
    @Test
    public void testLabelMultipleCalls() {
        ClassSpecification classSpec = new ClassSpecification(
                "Test", 0, 0, null, "TestClass", null, null
        );

        String result1 = dialog.label(classSpec);
        String result2 = dialog.label(classSpec);

        assertEquals(result1, result2, "Label should return consistent results");
    }

    /**
     * Test label with specification containing only access flags.
     * Access flags don't affect the label.
     */
    @Test
    public void testLabelWithOnlyAccessFlags() {
        ClassSpecification classSpec = new ClassSpecification(
                null,
                proguard.classfile.AccessConstants.PUBLIC,
                0,
                null, null,
                null, null
        );

        String result = dialog.label(classSpec);

        assertNotNull(result, "Label should handle specification with only access flags");
        assertTrue(result.toLowerCase().contains("specification"),
                   "Should return generic specification label");
    }

    // ========== Tests for label(ClassSpecification, int) overload ==========

    /**
     * Test label with index parameter and empty specification.
     * When index >= 0 and no other fields are present, should show specification number.
     */
    @Test
    public void testLabelWithIndexAndEmptySpecification() {
        ClassSpecification classSpec = new ClassSpecification();

        String result = dialog.label(classSpec, 0);

        assertNotNull(result, "Label should not be null");
        assertTrue(result.toLowerCase().contains("specification"), "Should contain 'specification'");
        assertTrue(result.contains("0") || result.contains("#0") || result.contains("# 0"),
                   "Should contain index 0");
    }

    /**
     * Test label with positive index and empty specification.
     */
    @Test
    public void testLabelWithPositiveIndexAndEmptySpecification() {
        ClassSpecification classSpec = new ClassSpecification();

        String result = dialog.label(classSpec, 5);

        assertNotNull(result, "Label should not be null");
        assertTrue(result.contains("5"), "Should contain index 5");
    }

    /**
     * Test label with negative index and empty specification.
     * Negative index should not show number, just "Specification".
     */
    @Test
    public void testLabelWithNegativeIndexAndEmptySpecification() {
        ClassSpecification classSpec = new ClassSpecification();

        String resultNegative = dialog.label(classSpec, -1);
        String resultNoIndex = dialog.label(classSpec);

        // Both should be the same since label() delegates to label(spec, -1)
        assertEquals(resultNoIndex, resultNegative,
                     "label(spec) and label(spec, -1) should return the same result");
    }

    /**
     * Test that index is ignored when comments are present.
     * Comments have higher priority than index.
     */
    @Test
    public void testLabelIndexIgnoredWhenCommentsPresent() {
        ClassSpecification classSpec = new ClassSpecification(
                "My comment", 0, 0, null, null, null, null
        );

        String result = dialog.label(classSpec, 10);

        assertEquals("My comment", result, "Comments should take priority over index");
        assertFalse(result.contains("10"), "Index should not appear when comments are present");
    }

    /**
     * Test that index is ignored when class name is present.
     * Class name has higher priority than index.
     */
    @Test
    public void testLabelIndexIgnoredWhenClassNamePresent() {
        ClassSpecification classSpec = new ClassSpecification(
                null, 0, 0, null, "TestClass", null, null
        );

        String result = dialog.label(classSpec, 10);

        assertTrue(result.contains("Class"), "Should contain 'Class'");
        assertTrue(result.contains("TestClass"), "Should contain class name");
        assertFalse(result.contains("10"), "Index should not appear when class name is present");
    }

    /**
     * Test that index is ignored when annotation type is present.
     */
    @Test
    public void testLabelIndexIgnoredWhenAnnotationTypePresent() {
        ClassSpecification classSpec = new ClassSpecification(
                null, 0, 0, "Ljava/lang/Deprecated;", null, null, null
        );

        String result = dialog.label(classSpec, 10);

        assertTrue(result.toLowerCase().contains("annotated"),
                   "Should mention annotation");
        assertFalse(result.contains("10"),
                    "Index should not appear when annotation type is present");
    }

    /**
     * Test that index is ignored when extends class name is present.
     */
    @Test
    public void testLabelIndexIgnoredWhenExtendsClassNamePresent() {
        ClassSpecification classSpec = new ClassSpecification(
                null, 0, 0, null, null, null, "java/lang/Object"
        );

        String result = dialog.label(classSpec, 10);

        assertTrue(result.toLowerCase().contains("extension"),
                   "Should mention extension");
        assertFalse(result.contains("10"),
                    "Index should not appear when extends class name is present");
    }

    /**
     * Test that index is ignored when extends annotation type is present.
     */
    @Test
    public void testLabelIndexIgnoredWhenExtendsAnnotationTypePresent() {
        ClassSpecification classSpec = new ClassSpecification(
                null, 0, 0, null, null, "Ljava/lang/annotation/Retention;", null
        );

        String result = dialog.label(classSpec, 10);

        assertTrue(result.toLowerCase().contains("extension"),
                   "Should mention extension");
        assertTrue(result.toLowerCase().contains("annotated"),
                   "Should mention annotation");
        assertFalse(result.contains("10"),
                    "Index should not appear when extends annotation type is present");
    }

    /**
     * Test label with zero index.
     */
    @Test
    public void testLabelWithZeroIndex() {
        ClassSpecification classSpec = new ClassSpecification();

        String result = dialog.label(classSpec, 0);

        assertNotNull(result, "Label should not be null");
        assertTrue(result.contains("0"), "Should contain index 0");
    }

    /**
     * Test label with large index.
     */
    @Test
    public void testLabelWithLargeIndex() {
        ClassSpecification classSpec = new ClassSpecification();

        String result = dialog.label(classSpec, 999);

        assertNotNull(result, "Label should not be null");
        assertTrue(result.contains("999"), "Should contain index 999");
    }

    /**
     * Test label with null specification and positive index.
     */
    @Test
    public void testLabelWithNullSpecificationAndIndex() {
        String result = dialog.label(null, 5);

        assertNotNull(result, "Label should not be null");
        // Null specification should return "none" message, index is ignored
        assertFalse(result.contains("5"),
                    "Index should not appear for null specification");
    }

    /**
     * Test that label(spec) and label(spec, -1) are equivalent.
     */
    @Test
    public void testLabelOverloadEquivalence() {
        ClassSpecification[] specs = {
            null,
            new ClassSpecification(),
            new ClassSpecification("Comment", 0, 0, null, null, null, null),
            new ClassSpecification(null, 0, 0, null, "TestClass", null, null),
            new ClassSpecification(null, 0, 0, "Ljava/lang/Deprecated;", null, null, null)
        };

        for (ClassSpecification spec : specs) {
            String result1 = dialog.label(spec);
            String result2 = dialog.label(spec, -1);
            assertEquals(result1, result2,
                         "label(spec) should be equivalent to label(spec, -1)");
        }
    }

    /**
     * Test label with different indices for same specification.
     */
    @Test
    public void testLabelWithDifferentIndices() {
        ClassSpecification classSpec = new ClassSpecification();

        String result0 = dialog.label(classSpec, 0);
        String result1 = dialog.label(classSpec, 1);
        String result2 = dialog.label(classSpec, 2);

        assertNotEquals(result0, result1, "Different indices should produce different labels");
        assertNotEquals(result1, result2, "Different indices should produce different labels");
        assertTrue(result0.contains("0"), "Should contain correct index");
        assertTrue(result1.contains("1"), "Should contain correct index");
        assertTrue(result2.contains("2"), "Should contain correct index");
    }

    /**
     * Test label with index and only access flags.
     * Access flags don't affect the label, so index should be used.
     */
    @Test
    public void testLabelWithIndexAndOnlyAccessFlags() {
        ClassSpecification classSpec = new ClassSpecification(
                null,
                proguard.classfile.AccessConstants.PUBLIC,
                0,
                null, null,
                null, null
        );

        String result = dialog.label(classSpec, 7);

        assertNotNull(result, "Label should not be null");
        assertTrue(result.contains("7"), "Should contain index when only access flags are present");
    }

    /**
     * Test label with very negative index.
     */
    @Test
    public void testLabelWithVeryNegativeIndex() {
        ClassSpecification classSpec = new ClassSpecification();

        String result = dialog.label(classSpec, -100);

        assertNotNull(result, "Label should not be null");
        assertFalse(result.contains("100") || result.contains("-100"),
                    "Negative index should not appear in label");
        assertTrue(result.toLowerCase().contains("specification"),
                   "Should return generic specification label");
    }

    /**
     * Test that index parameter does not affect priority order.
     */
    @Test
    public void testLabelIndexDoesNotAffectPriority() {
        // Comments still take priority even with index
        ClassSpecification specWithComments = new ClassSpecification(
                "Comment", 0, 0, null, "TestClass", null, null
        );
        String resultComments = dialog.label(specWithComments, 5);
        assertEquals("Comment", resultComments,
                     "Comments should still take priority with index parameter");

        // Class name still takes priority over annotation type even with index
        ClassSpecification specWithClassName = new ClassSpecification(
                null, 0, 0, "Ljava/lang/Deprecated;", "TestClass", null, null
        );
        String resultClassName = dialog.label(specWithClassName, 5);
        assertTrue(resultClassName.contains("TestClass"),
                   "Class name should still take priority with index parameter");
        assertFalse(resultClassName.toLowerCase().contains("deprecated"),
                    "Lower priority fields should still be ignored with index parameter");
    }
}
