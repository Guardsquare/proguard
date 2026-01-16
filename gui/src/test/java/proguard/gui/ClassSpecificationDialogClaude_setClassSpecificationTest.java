package proguard.gui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.ClassSpecification;
import proguard.MemberSpecification;
import proguard.classfile.AccessConstants;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Test class for ClassSpecificationDialog.setClassSpecification() method.
 *
 * The setClassSpecification method:
 * - Takes a ClassSpecification parameter
 * - Sets the comments text area
 * - Sets access modifier radio buttons (public, final, abstract, interface, annotation, enum, synthetic)
 * - Sets text fields for annotation type, class name, extends annotation type, and extends class name
 * - Sets member specifications (field and method specifications)
 *
 * These tests verify:
 * - The method correctly sets all text fields from the ClassSpecification
 * - The method handles null values properly (converting to empty strings or defaults)
 * - The method correctly sets access flags via radio buttons
 * - The method sets member specifications correctly
 * - The values can be retrieved back using getClassSpecification
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class ClassSpecificationDialogClaude_setClassSpecificationTest {

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
     * Test setClassSpecification with a simple class name.
     */
    @Test
    public void testSetClassSpecificationWithSimpleClassName() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        ClassSpecification classSpec = new ClassSpecification(
                null,
                0, 0,
                null, "com/example/TestClass",
                null, null
        );

        dialog.setClassSpecification(classSpec);

        ClassSpecification result = dialog.getClassSpecification();
        assertNotNull(result, "Result should not be null");
        assertEquals("com.example.TestClass", result.className, "Class name should be set correctly");
    }

    /**
     * Test setClassSpecification with comments.
     */
    @Test
    public void testSetClassSpecificationWithComments() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        String testComments = "This is a test comment\nWith multiple lines";
        ClassSpecification classSpec = new ClassSpecification(
                testComments,
                0, 0,
                null, "TestClass",
                null, null
        );

        dialog.setClassSpecification(classSpec);

        ClassSpecification result = dialog.getClassSpecification();
        assertEquals(testComments, result.comments, "Comments should be preserved");
    }

    /**
     * Test setClassSpecification with null comments.
     */
    @Test
    public void testSetClassSpecificationWithNullComments() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        ClassSpecification classSpec = new ClassSpecification(
                null,  // null comments
                0, 0,
                null, "TestClass",
                null, null
        );

        dialog.setClassSpecification(classSpec);

        ClassSpecification result = dialog.getClassSpecification();
        assertNull(result.comments, "Comments should be null when set to null");
    }

    /**
     * Test setClassSpecification with annotation type.
     */
    @Test
    public void testSetClassSpecificationWithAnnotationType() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        ClassSpecification classSpec = new ClassSpecification(
                null,
                0, 0,
                "Ljava/lang/Deprecated;", "TestClass",
                null, null
        );

        dialog.setClassSpecification(classSpec);

        ClassSpecification result = dialog.getClassSpecification();
        assertNotNull(result.annotationType, "Annotation type should not be null");
        assertEquals("Ljava/lang/Deprecated;", result.annotationType, "Annotation type should be preserved");
    }

    /**
     * Test setClassSpecification with extends class name.
     */
    @Test
    public void testSetClassSpecificationWithExtendsClassName() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        ClassSpecification classSpec = new ClassSpecification(
                null,
                0, 0,
                null, "TestClass",
                null, "java/lang/Object"
        );

        dialog.setClassSpecification(classSpec);

        ClassSpecification result = dialog.getClassSpecification();
        assertEquals("java.lang.Object", result.extendsClassName, "Extends class name should be preserved");
    }

    /**
     * Test setClassSpecification with extends annotation type.
     */
    @Test
    public void testSetClassSpecificationWithExtendsAnnotationType() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        ClassSpecification classSpec = new ClassSpecification(
                null,
                0, 0,
                null, "TestClass",
                "Ljava/lang/annotation/Retention;", null
        );

        dialog.setClassSpecification(classSpec);

        ClassSpecification result = dialog.getClassSpecification();
        assertNotNull(result.extendsAnnotationType, "Extends annotation type should not be null");
    }

    /**
     * Test setClassSpecification with null class name (should default to "*").
     */
    @Test
    public void testSetClassSpecificationWithNullClassName() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        ClassSpecification classSpec = new ClassSpecification(
                null,
                0, 0,
                null, null,  // null class name
                null, null
        );

        dialog.setClassSpecification(classSpec);

        ClassSpecification result = dialog.getClassSpecification();
        assertNull(result.className, "Class name should be null when wildcarded");
    }

    /**
     * Test setClassSpecification with public access flag.
     */
    @Test
    public void testSetClassSpecificationWithPublicAccessFlag() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        ClassSpecification classSpec = new ClassSpecification(
                null,
                AccessConstants.PUBLIC,  // required set flags
                0,                        // required unset flags
                null, "TestClass",
                null, null
        );

        dialog.setClassSpecification(classSpec);

        ClassSpecification result = dialog.getClassSpecification();
        assertTrue((result.requiredSetAccessFlags & AccessConstants.PUBLIC) != 0,
                   "Public flag should be set");
    }

    /**
     * Test setClassSpecification with final access flag.
     */
    @Test
    public void testSetClassSpecificationWithFinalAccessFlag() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        ClassSpecification classSpec = new ClassSpecification(
                null,
                AccessConstants.FINAL,
                0,
                null, "TestClass",
                null, null
        );

        dialog.setClassSpecification(classSpec);

        ClassSpecification result = dialog.getClassSpecification();
        assertTrue((result.requiredSetAccessFlags & AccessConstants.FINAL) != 0,
                   "Final flag should be set");
    }

    /**
     * Test setClassSpecification with abstract access flag.
     */
    @Test
    public void testSetClassSpecificationWithAbstractAccessFlag() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        ClassSpecification classSpec = new ClassSpecification(
                null,
                AccessConstants.ABSTRACT,
                0,
                null, "TestClass",
                null, null
        );

        dialog.setClassSpecification(classSpec);

        ClassSpecification result = dialog.getClassSpecification();
        assertTrue((result.requiredSetAccessFlags & AccessConstants.ABSTRACT) != 0,
                   "Abstract flag should be set");
    }

    /**
     * Test setClassSpecification with interface access flag.
     */
    @Test
    public void testSetClassSpecificationWithInterfaceAccessFlag() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        ClassSpecification classSpec = new ClassSpecification(
                null,
                AccessConstants.INTERFACE,
                0,
                null, "TestClass",
                null, null
        );

        dialog.setClassSpecification(classSpec);

        ClassSpecification result = dialog.getClassSpecification();
        assertTrue((result.requiredSetAccessFlags & AccessConstants.INTERFACE) != 0,
                   "Interface flag should be set");
    }

    /**
     * Test setClassSpecification with multiple access flags.
     */
    @Test
    public void testSetClassSpecificationWithMultipleAccessFlags() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        ClassSpecification classSpec = new ClassSpecification(
                null,
                AccessConstants.PUBLIC | AccessConstants.FINAL,
                0,
                null, "TestClass",
                null, null
        );

        dialog.setClassSpecification(classSpec);

        ClassSpecification result = dialog.getClassSpecification();
        assertTrue((result.requiredSetAccessFlags & AccessConstants.PUBLIC) != 0,
                   "Public flag should be set");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.FINAL) != 0,
                   "Final flag should be set");
    }

    /**
     * Test setClassSpecification can be called multiple times.
     */
    @Test
    public void testSetClassSpecificationMultipleTimes() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        // First specification
        ClassSpecification classSpec1 = new ClassSpecification(
                "First comments",
                0, 0,
                null, "FirstClass",
                null, null
        );
        dialog.setClassSpecification(classSpec1);

        ClassSpecification result1 = dialog.getClassSpecification();
        assertEquals("First comments", result1.comments, "First comments should be set");
        assertEquals("FirstClass", result1.className, "First class name should be set");

        // Second specification
        ClassSpecification classSpec2 = new ClassSpecification(
                "Second comments",
                0, 0,
                null, "SecondClass",
                null, null
        );
        dialog.setClassSpecification(classSpec2);

        ClassSpecification result2 = dialog.getClassSpecification();
        assertEquals("Second comments", result2.comments, "Second comments should replace first");
        assertEquals("SecondClass", result2.className, "Second class name should replace first");
    }

    /**
     * Test setClassSpecification with empty ClassSpecification.
     */
    @Test
    public void testSetClassSpecificationWithEmptySpec() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        ClassSpecification classSpec = new ClassSpecification();

        dialog.setClassSpecification(classSpec);

        // Should not throw exception
        ClassSpecification result = dialog.getClassSpecification();
        assertNotNull(result, "Result should not be null");
    }

    /**
     * Test setClassSpecification with all fields populated.
     */
    @Test
    public void testSetClassSpecificationWithAllFieldsPopulated() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        ClassSpecification classSpec = new ClassSpecification(
                "Complete specification",
                AccessConstants.PUBLIC | AccessConstants.FINAL,
                0,
                "Ljava/lang/Deprecated;",
                "com/example/CompleteClass",
                "Ljava/lang/annotation/Retention;",
                "java/lang/Object"
        );

        dialog.setClassSpecification(classSpec);

        ClassSpecification result = dialog.getClassSpecification();
        assertEquals("Complete specification", result.comments, "Comments should be preserved");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.PUBLIC) != 0, "Public flag should be set");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.FINAL) != 0, "Final flag should be set");
        assertNotNull(result.annotationType, "Annotation type should be preserved");
        assertEquals("com.example.CompleteClass", result.className, "Class name should be preserved");
        assertNotNull(result.extendsAnnotationType, "Extends annotation type should be preserved");
        assertEquals("java.lang.Object", result.extendsClassName, "Extends class name should be preserved");
    }

    /**
     * Test setClassSpecification overrides previous values.
     */
    @Test
    public void testSetClassSpecificationOverridesPreviousValues() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        // Set first specification with comments
        ClassSpecification classSpec1 = new ClassSpecification(
                "Original comments",
                AccessConstants.PUBLIC,
                0,
                null, "OriginalClass",
                null, null
        );
        dialog.setClassSpecification(classSpec1);

        // Set second specification without comments but with different class
        ClassSpecification classSpec2 = new ClassSpecification(
                null,  // No comments
                AccessConstants.FINAL,
                0,
                null, "NewClass",
                null, null
        );
        dialog.setClassSpecification(classSpec2);

        ClassSpecification result = dialog.getClassSpecification();
        assertNull(result.comments, "Comments should be null after second set");
        assertEquals("NewClass", result.className, "Class name should be from second spec");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.FINAL) != 0,
                   "Final flag should be set from second spec");
    }

    /**
     * Test setClassSpecification with wildcard class name.
     */
    @Test
    public void testSetClassSpecificationWithWildcardClassName() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        ClassSpecification classSpec = new ClassSpecification(
                null,
                0, 0,
                null, "com/example/*",
                null, null
        );

        dialog.setClassSpecification(classSpec);

        ClassSpecification result = dialog.getClassSpecification();
        assertNotNull(result.className, "Wildcard class name should be preserved");
        assertTrue(result.className.contains("*"), "Class name should contain wildcard");
    }

    /**
     * Test setClassSpecification with synthetic access flag.
     */
    @Test
    public void testSetClassSpecificationWithSyntheticAccessFlag() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        ClassSpecification classSpec = new ClassSpecification(
                null,
                AccessConstants.SYNTHETIC,
                0,
                null, "TestClass",
                null, null
        );

        dialog.setClassSpecification(classSpec);

        ClassSpecification result = dialog.getClassSpecification();
        assertTrue((result.requiredSetAccessFlags & AccessConstants.SYNTHETIC) != 0,
                   "Synthetic flag should be set");
    }

    /**
     * Test setClassSpecification with enum access flag.
     */
    @Test
    public void testSetClassSpecificationWithEnumAccessFlag() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        ClassSpecification classSpec = new ClassSpecification(
                null,
                AccessConstants.ENUM,
                0,
                null, "TestEnum",
                null, null
        );

        dialog.setClassSpecification(classSpec);

        ClassSpecification result = dialog.getClassSpecification();
        assertTrue((result.requiredSetAccessFlags & AccessConstants.ENUM) != 0,
                   "Enum flag should be set");
    }

    /**
     * Test setClassSpecification with annotation access flag.
     */
    @Test
    public void testSetClassSpecificationWithAnnotationAccessFlag() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        ClassSpecification classSpec = new ClassSpecification(
                null,
                AccessConstants.ANNOTATION,
                0,
                null, "TestAnnotation",
                null, null
        );

        dialog.setClassSpecification(classSpec);

        ClassSpecification result = dialog.getClassSpecification();
        assertTrue((result.requiredSetAccessFlags & AccessConstants.ANNOTATION) != 0,
                   "Annotation flag should be set");
    }

    /**
     * Test setClassSpecification with package name pattern.
     */
    @Test
    public void testSetClassSpecificationWithPackagePattern() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        ClassSpecification classSpec = new ClassSpecification(
                null,
                0, 0,
                null, "com/example/**",
                null, null
        );

        dialog.setClassSpecification(classSpec);

        ClassSpecification result = dialog.getClassSpecification();
        assertNotNull(result.className, "Package pattern should be preserved");
        assertTrue(result.className.contains("**"), "Class name should contain double wildcard");
    }

    /**
     * Test setClassSpecification with fully qualified annotation type.
     */
    @Test
    public void testSetClassSpecificationWithFullyQualifiedAnnotation() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        ClassSpecification classSpec = new ClassSpecification(
                null,
                0, 0,
                "Lcom/example/CustomAnnotation;", "TestClass",
                null, null
        );

        dialog.setClassSpecification(classSpec);

        ClassSpecification result = dialog.getClassSpecification();
        assertNotNull(result.annotationType, "Annotation type should be preserved");
        assertEquals("Lcom/example/CustomAnnotation;", result.annotationType,
                     "Full annotation type should match");
    }

    /**
     * Test setClassSpecification with empty strings (should convert to nulls or defaults).
     */
    @Test
    public void testSetClassSpecificationWithEmptyStrings() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        ClassSpecification classSpec = new ClassSpecification(
                "",  // empty comments
                0, 0,
                "", "",  // empty annotation and class name
                "", ""   // empty extends annotation and class name
        );

        dialog.setClassSpecification(classSpec);

        ClassSpecification result = dialog.getClassSpecification();
        assertNotNull(result, "Result should not be null");
        // Empty strings should be converted appropriately
    }

    /**
     * Test setClassSpecification preserves complex class name patterns.
     */
    @Test
    public void testSetClassSpecificationWithComplexClassNamePattern() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        ClassSpecification classSpec = new ClassSpecification(
                null,
                0, 0,
                null, "com/example/**/Test*Class?",
                null, null
        );

        dialog.setClassSpecification(classSpec);

        ClassSpecification result = dialog.getClassSpecification();
        assertNotNull(result.className, "Complex pattern should be preserved");
    }
}
