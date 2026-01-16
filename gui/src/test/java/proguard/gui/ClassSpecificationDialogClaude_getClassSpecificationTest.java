package proguard.gui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.ClassSpecification;
import proguard.classfile.AccessConstants;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Test class for ClassSpecificationDialog.getClassSpecification() method.
 *
 * The getClassSpecification method:
 * - Reads text from various UI text fields (comments, annotation type, class name, etc.)
 * - Converts empty strings to null values
 * - Converts from external format to internal format (using ClassUtil)
 * - Reads access modifier settings from radio buttons
 * - Gets member specifications (fields and methods) from the member specifications panel
 * - Creates and returns a new ClassSpecification object
 *
 * These tests verify:
 * - The method returns a non-null ClassSpecification
 * - Text fields are correctly read and converted
 * - Empty strings are converted to null appropriately
 * - Access flags are correctly retrieved from radio buttons
 * - Values set via setClassSpecification can be retrieved via getClassSpecification
 * - The method handles various input formats correctly
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class ClassSpecificationDialogClaude_getClassSpecificationTest {

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
     * Test that getClassSpecification returns a non-null result.
     */
    @Test
    public void testGetClassSpecificationReturnsNonNull() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        ClassSpecification result = dialog.getClassSpecification();

        assertNotNull(result, "getClassSpecification should never return null");
    }

    /**
     * Test getClassSpecification with default values from a new dialog.
     */
    @Test
    public void testGetClassSpecificationDefaultValues() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        ClassSpecification result = dialog.getClassSpecification();

        assertNotNull(result, "Result should not be null");
        assertNull(result.comments, "Default comments should be null");
        assertNull(result.className, "Default class name should be null (wildcarded)");
    }

    /**
     * Test getClassSpecification retrieves simple class name correctly.
     */
    @Test
    public void testGetClassSpecificationWithSimpleClassName() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        ClassSpecification classSpec = new ClassSpecification(
                null, 0, 0, null, "com/example/TestClass", null, null
        );

        dialog.setClassSpecification(classSpec);
        ClassSpecification result = dialog.getClassSpecification();

        assertEquals("com.example.TestClass", result.className, "Class name should be retrieved correctly");
    }

    /**
     * Test getClassSpecification retrieves comments correctly.
     */
    @Test
    public void testGetClassSpecificationWithComments() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        String testComments = "Test comment line 1\nTest comment line 2";
        ClassSpecification classSpec = new ClassSpecification(
                testComments, 0, 0, null, "TestClass", null, null
        );

        dialog.setClassSpecification(classSpec);
        ClassSpecification result = dialog.getClassSpecification();

        assertEquals(testComments, result.comments, "Comments should be preserved");
    }

    /**
     * Test getClassSpecification with null comments returns null.
     */
    @Test
    public void testGetClassSpecificationWithNullComments() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        ClassSpecification classSpec = new ClassSpecification(
                null, 0, 0, null, "TestClass", null, null
        );

        dialog.setClassSpecification(classSpec);
        ClassSpecification result = dialog.getClassSpecification();

        assertNull(result.comments, "Null comments should remain null");
    }

    /**
     * Test getClassSpecification retrieves annotation type correctly.
     */
    @Test
    public void testGetClassSpecificationWithAnnotationType() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        ClassSpecification classSpec = new ClassSpecification(
                null, 0, 0,
                "Ljava/lang/Deprecated;", "TestClass",
                null, null
        );

        dialog.setClassSpecification(classSpec);
        ClassSpecification result = dialog.getClassSpecification();

        assertEquals("Ljava/lang/Deprecated;", result.annotationType,
                     "Annotation type should be preserved");
    }

    /**
     * Test getClassSpecification retrieves extends class name correctly.
     */
    @Test
    public void testGetClassSpecificationWithExtendsClassName() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        ClassSpecification classSpec = new ClassSpecification(
                null, 0, 0, null, "TestClass", null, "java/lang/Object"
        );

        dialog.setClassSpecification(classSpec);
        ClassSpecification result = dialog.getClassSpecification();

        assertEquals("java.lang.Object", result.extendsClassName,
                     "Extends class name should be preserved");
    }

    /**
     * Test getClassSpecification retrieves extends annotation type correctly.
     */
    @Test
    public void testGetClassSpecificationWithExtendsAnnotationType() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        ClassSpecification classSpec = new ClassSpecification(
                null, 0, 0, null, "TestClass",
                "Ljava/lang/annotation/Retention;", null
        );

        dialog.setClassSpecification(classSpec);
        ClassSpecification result = dialog.getClassSpecification();

        assertEquals("Ljava/lang/annotation/Retention;", result.extendsAnnotationType,
                     "Extends annotation type should be preserved");
    }

    /**
     * Test getClassSpecification with null class name (wildcard).
     */
    @Test
    public void testGetClassSpecificationWithNullClassName() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        ClassSpecification classSpec = new ClassSpecification(
                null, 0, 0, null, null, null, null
        );

        dialog.setClassSpecification(classSpec);
        ClassSpecification result = dialog.getClassSpecification();

        assertNull(result.className, "Null class name should remain null (wildcard)");
    }

    /**
     * Test getClassSpecification retrieves public access flag correctly.
     */
    @Test
    public void testGetClassSpecificationWithPublicAccessFlag() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        ClassSpecification classSpec = new ClassSpecification(
                null,
                AccessConstants.PUBLIC,
                0,
                null, "TestClass",
                null, null
        );

        dialog.setClassSpecification(classSpec);
        ClassSpecification result = dialog.getClassSpecification();

        assertTrue((result.requiredSetAccessFlags & AccessConstants.PUBLIC) != 0,
                   "Public flag should be set");
    }

    /**
     * Test getClassSpecification retrieves final access flag correctly.
     */
    @Test
    public void testGetClassSpecificationWithFinalAccessFlag() {
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
     * Test getClassSpecification retrieves abstract access flag correctly.
     */
    @Test
    public void testGetClassSpecificationWithAbstractAccessFlag() {
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
     * Test getClassSpecification retrieves interface access flag correctly.
     */
    @Test
    public void testGetClassSpecificationWithInterfaceAccessFlag() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        ClassSpecification classSpec = new ClassSpecification(
                null,
                AccessConstants.INTERFACE,
                0,
                null, "TestInterface",
                null, null
        );

        dialog.setClassSpecification(classSpec);
        ClassSpecification result = dialog.getClassSpecification();

        assertTrue((result.requiredSetAccessFlags & AccessConstants.INTERFACE) != 0,
                   "Interface flag should be set");
    }

    /**
     * Test getClassSpecification retrieves enum access flag correctly.
     */
    @Test
    public void testGetClassSpecificationWithEnumAccessFlag() {
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
     * Test getClassSpecification retrieves annotation access flag correctly.
     */
    @Test
    public void testGetClassSpecificationWithAnnotationAccessFlag() {
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
     * Test getClassSpecification retrieves synthetic access flag correctly.
     */
    @Test
    public void testGetClassSpecificationWithSyntheticAccessFlag() {
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
     * Test getClassSpecification retrieves multiple access flags correctly.
     */
    @Test
    public void testGetClassSpecificationWithMultipleAccessFlags() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        ClassSpecification classSpec = new ClassSpecification(
                null,
                AccessConstants.PUBLIC | AccessConstants.FINAL | AccessConstants.ABSTRACT,
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
        assertTrue((result.requiredSetAccessFlags & AccessConstants.ABSTRACT) != 0,
                   "Abstract flag should be set");
    }

    /**
     * Test getClassSpecification can be called multiple times and returns consistent results.
     */
    @Test
    public void testGetClassSpecificationMultipleCalls() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        ClassSpecification classSpec = new ClassSpecification(
                "Test comments", 0, 0, null, "TestClass", null, null
        );

        dialog.setClassSpecification(classSpec);

        ClassSpecification result1 = dialog.getClassSpecification();
        ClassSpecification result2 = dialog.getClassSpecification();

        assertEquals(result1.comments, result2.comments, "Comments should be consistent");
        assertEquals(result1.className, result2.className, "Class name should be consistent");
    }

    /**
     * Test getClassSpecification returns a new instance each time.
     */
    @Test
    public void testGetClassSpecificationReturnsNewInstance() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        ClassSpecification result1 = dialog.getClassSpecification();
        ClassSpecification result2 = dialog.getClassSpecification();

        assertNotSame(result1, result2, "Each call should return a new instance");
    }

    /**
     * Test getClassSpecification with wildcard class name pattern.
     */
    @Test
    public void testGetClassSpecificationWithWildcardClassName() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        ClassSpecification classSpec = new ClassSpecification(
                null, 0, 0, null, "com/example/*", null, null
        );

        dialog.setClassSpecification(classSpec);
        ClassSpecification result = dialog.getClassSpecification();

        assertNotNull(result.className, "Wildcard class name should be preserved");
        assertTrue(result.className.contains("*"), "Wildcard should be in class name");
    }

    /**
     * Test getClassSpecification with package-level wildcard.
     */
    @Test
    public void testGetClassSpecificationWithPackageWildcard() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        ClassSpecification classSpec = new ClassSpecification(
                null, 0, 0, null, "com/example/**", null, null
        );

        dialog.setClassSpecification(classSpec);
        ClassSpecification result = dialog.getClassSpecification();

        assertNotNull(result.className, "Package wildcard should be preserved");
        assertTrue(result.className.contains("**"), "Double wildcard should be in class name");
    }

    /**
     * Test getClassSpecification with all fields populated.
     */
    @Test
    public void testGetClassSpecificationWithAllFieldsPopulated() {
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
        assertEquals("com.example.CompleteClass", result.className, "Class name should be preserved");
        assertEquals("Ljava/lang/Deprecated;", result.annotationType, "Annotation type should be preserved");
        assertEquals("java.lang.Object", result.extendsClassName, "Extends class name should be preserved");
        assertEquals("Ljava/lang/annotation/Retention;", result.extendsAnnotationType,
                     "Extends annotation type should be preserved");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.PUBLIC) != 0,
                   "Public flag should be preserved");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.FINAL) != 0,
                   "Final flag should be preserved");
    }

    /**
     * Test getClassSpecification after setting values multiple times.
     */
    @Test
    public void testGetClassSpecificationAfterMultipleSets() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        // First set
        ClassSpecification classSpec1 = new ClassSpecification(
                "First", 0, 0, null, "FirstClass", null, null
        );
        dialog.setClassSpecification(classSpec1);

        // Second set
        ClassSpecification classSpec2 = new ClassSpecification(
                "Second", 0, 0, null, "SecondClass", null, null
        );
        dialog.setClassSpecification(classSpec2);

        ClassSpecification result = dialog.getClassSpecification();

        assertEquals("Second", result.comments, "Should reflect second set");
        assertEquals("SecondClass", result.className, "Should reflect second set");
    }

    /**
     * Test getClassSpecification with empty ClassSpecification.
     */
    @Test
    public void testGetClassSpecificationWithEmptySpec() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        ClassSpecification classSpec = new ClassSpecification();

        dialog.setClassSpecification(classSpec);
        ClassSpecification result = dialog.getClassSpecification();

        assertNotNull(result, "Result should not be null");
    }

    /**
     * Test getClassSpecification with complex wildcard pattern.
     */
    @Test
    public void testGetClassSpecificationWithComplexWildcardPattern() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        ClassSpecification classSpec = new ClassSpecification(
                null, 0, 0, null, "com/example/**/Test*Class?", null, null
        );

        dialog.setClassSpecification(classSpec);
        ClassSpecification result = dialog.getClassSpecification();

        assertNotNull(result.className, "Complex pattern should be preserved");
    }

    /**
     * Test getClassSpecification with only access flags set.
     */
    @Test
    public void testGetClassSpecificationWithOnlyAccessFlags() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        ClassSpecification classSpec = new ClassSpecification(
                null,
                AccessConstants.PUBLIC,
                0,
                null, null,
                null, null
        );

        dialog.setClassSpecification(classSpec);
        ClassSpecification result = dialog.getClassSpecification();

        assertTrue((result.requiredSetAccessFlags & AccessConstants.PUBLIC) != 0,
                   "Public flag should be set even without class name");
    }

    /**
     * Test getClassSpecification with fully qualified class name.
     */
    @Test
    public void testGetClassSpecificationWithFullyQualifiedClassName() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        ClassSpecification classSpec = new ClassSpecification(
                null, 0, 0, null, "com/example/subpackage/MyClass", null, null
        );

        dialog.setClassSpecification(classSpec);
        ClassSpecification result = dialog.getClassSpecification();

        assertEquals("com.example.subpackage.MyClass", result.className,
                     "Fully qualified class name should be preserved");
    }

    /**
     * Test getClassSpecification preserves inner class notation.
     */
    @Test
    public void testGetClassSpecificationWithInnerClass() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        ClassSpecification classSpec = new ClassSpecification(
                null, 0, 0, null, "com/example/OuterClass$InnerClass", null, null
        );

        dialog.setClassSpecification(classSpec);
        ClassSpecification result = dialog.getClassSpecification();

        assertNotNull(result.className, "Inner class notation should be preserved");
        assertTrue(result.className.contains("$"), "Inner class $ notation should be preserved");
    }

    /**
     * Test getClassSpecification with custom annotation.
     */
    @Test
    public void testGetClassSpecificationWithCustomAnnotation() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        ClassSpecification classSpec = new ClassSpecification(
                null, 0, 0,
                "Lcom/example/MyCustomAnnotation;", "TestClass",
                null, null
        );

        dialog.setClassSpecification(classSpec);
        ClassSpecification result = dialog.getClassSpecification();

        assertEquals("Lcom/example/MyCustomAnnotation;", result.annotationType,
                     "Custom annotation should be preserved");
    }

    /**
     * Test getClassSpecification with interface hierarchy.
     */
    @Test
    public void testGetClassSpecificationWithInterfaceHierarchy() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        ClassSpecification classSpec = new ClassSpecification(
                null, 0, 0, null, "MyInterface", null, "java/io/Serializable"
        );

        dialog.setClassSpecification(classSpec);
        ClassSpecification result = dialog.getClassSpecification();

        assertEquals("java.io.Serializable", result.extendsClassName,
                     "Interface hierarchy should be preserved");
    }

    /**
     * Test getClassSpecification after dialog reuse.
     */
    @Test
    public void testGetClassSpecificationAfterDialogReuse() {
        testFrame = new JFrame("Test Frame");
        dialog = new ClassSpecificationDialog(testFrame, false, true);

        // Use dialog first time
        ClassSpecification classSpec1 = new ClassSpecification(
                "First use", AccessConstants.PUBLIC, 0, null, "FirstClass", null, null
        );
        dialog.setClassSpecification(classSpec1);
        ClassSpecification result1 = dialog.getClassSpecification();

        // Reuse dialog
        ClassSpecification classSpec2 = new ClassSpecification(
                "Second use", AccessConstants.FINAL, 0, null, "SecondClass", null, null
        );
        dialog.setClassSpecification(classSpec2);
        ClassSpecification result2 = dialog.getClassSpecification();

        assertEquals("Second use", result2.comments, "Dialog should be reusable");
        assertEquals("SecondClass", result2.className, "Dialog should be reusable");
        assertFalse((result2.requiredSetAccessFlags & AccessConstants.PUBLIC) != 0,
                    "Previous flags should be cleared");
        assertTrue((result2.requiredSetAccessFlags & AccessConstants.FINAL) != 0,
                   "New flags should be set");
    }
}
