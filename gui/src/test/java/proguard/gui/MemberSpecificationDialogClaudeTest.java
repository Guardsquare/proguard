package proguard.gui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.MemberSpecification;
import proguard.classfile.AccessConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Test class for MemberSpecificationDialog.
 *
 * This class tests all public methods of MemberSpecificationDialog:
 * - Constructor (JDialog, boolean)
 * - setMemberSpecification(MemberSpecification)
 * - getMemberSpecification()
 * - showDialog()
 *
 * The dialog handles both field and method specifications based on the isField parameter.
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class MemberSpecificationDialogClaudeTest {

    private JDialog testDialog;
    private MemberSpecificationDialog memberDialog;

    @BeforeEach
    public void setUp() {
        // Tests will skip if headless mode is active
        assumeFalse(GraphicsEnvironment.isHeadless(),
                    "Skipping test: Headless environment detected. GUI components require a display.");
    }

    @AfterEach
    public void tearDown() {
        if (memberDialog != null) {
            memberDialog.dispose();
        }
        if (testDialog != null) {
            testDialog.dispose();
        }
    }

    // Constructor tests

    /**
     * Test constructor with valid parameters for field dialog.
     */
    @Test
    public void testConstructorForField() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        assertNotNull(memberDialog, "Dialog should be created successfully");
        assertTrue(memberDialog.isModal(), "Dialog should be modal");
        assertTrue(memberDialog.isResizable(), "Dialog should be resizable");
        assertEquals(testDialog, memberDialog.getOwner(), "Dialog owner should be the test dialog");
    }

    /**
     * Test constructor with valid parameters for method dialog.
     */
    @Test
    public void testConstructorForMethod() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        assertNotNull(memberDialog, "Dialog should be created successfully");
        assertTrue(memberDialog.isModal(), "Dialog should be modal");
        assertTrue(memberDialog.isResizable(), "Dialog should be resizable");
        assertEquals(testDialog, memberDialog.getOwner(), "Dialog owner should be the test dialog");
    }

    /**
     * Test constructor with null owner for field dialog.
     */
    @Test
    public void testConstructorWithNullOwnerForField() {
        memberDialog = new MemberSpecificationDialog(null, true);

        assertNotNull(memberDialog, "Dialog should be created successfully with null owner");
        assertTrue(memberDialog.isModal(), "Dialog should be modal");
        assertTrue(memberDialog.isResizable(), "Dialog should be resizable");
        assertNull(memberDialog.getOwner(), "Dialog owner should be null");
    }

    /**
     * Test constructor with null owner for method dialog.
     */
    @Test
    public void testConstructorWithNullOwnerForMethod() {
        memberDialog = new MemberSpecificationDialog(null, false);

        assertNotNull(memberDialog, "Dialog should be created successfully with null owner");
        assertTrue(memberDialog.isModal(), "Dialog should be modal");
        assertTrue(memberDialog.isResizable(), "Dialog should be resizable");
        assertNull(memberDialog.getOwner(), "Dialog owner should be null");
    }

    /**
     * Test that the dialog is not initially visible after construction.
     */
    @Test
    public void testConstructorDialogNotInitiallyVisible() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        assertFalse(memberDialog.isVisible(), "Dialog should not be visible immediately after construction");
    }

    /**
     * Test that the dialog has a content pane after construction.
     */
    @Test
    public void testConstructorInitializesContentPane() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        assertNotNull(memberDialog.getContentPane(), "Dialog should have a content pane");
        assertTrue(memberDialog.getContentPane().getComponentCount() > 0,
                   "Dialog content pane should contain components");
    }

    // getMemberSpecification tests - default values

    /**
     * Test getMemberSpecification returns non-null for field dialog.
     */
    @Test
    public void testGetMemberSpecificationReturnsNonNullForField() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "getMemberSpecification should never return null");
    }

    /**
     * Test getMemberSpecification returns non-null for method dialog.
     */
    @Test
    public void testGetMemberSpecificationReturnsNonNullForMethod() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "getMemberSpecification should never return null");
    }

    /**
     * Test getMemberSpecification with default values for field dialog.
     */
    @Test
    public void testGetMemberSpecificationDefaultValuesForField() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        // Default name and descriptor should be null (wildcarded)
        assertNull(result.name, "Default name should be null");
        assertNull(result.descriptor, "Default descriptor should be null");
        assertEquals(0, result.requiredSetAccessFlags, "Default required set flags should be 0");
        assertEquals(0, result.requiredUnsetAccessFlags, "Default required unset flags should be 0");
    }

    /**
     * Test getMemberSpecification with default values for method dialog.
     */
    @Test
    public void testGetMemberSpecificationDefaultValuesForMethod() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        // Default name and descriptor should be null (wildcarded)
        assertNull(result.name, "Default name should be null");
        assertNull(result.descriptor, "Default descriptor should be null");
        assertEquals(0, result.requiredSetAccessFlags, "Default required set flags should be 0");
        assertEquals(0, result.requiredUnsetAccessFlags, "Default required unset flags should be 0");
    }

    // setMemberSpecification and getMemberSpecification tests - field specifications

    /**
     * Test setMemberSpecification and getMemberSpecification with simple field specification.
     */
    @Test
    public void testSetAndGetMemberSpecificationSimpleField() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                0, 0, null, "testField", "I"
        );

        memberDialog.setMemberSpecification(fieldSpec);
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        assertEquals("testField", result.name, "Field name should match");
        assertEquals("I", result.descriptor, "Field descriptor should match");
    }

    /**
     * Test setMemberSpecification and getMemberSpecification with null field name.
     */
    @Test
    public void testSetAndGetMemberSpecificationNullFieldName() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                0, 0, null, null, "Ljava/lang/String;"
        );

        memberDialog.setMemberSpecification(fieldSpec);
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        assertNull(result.name, "Field name should be null");
    }

    /**
     * Test setMemberSpecification and getMemberSpecification with null field descriptor.
     */
    @Test
    public void testSetAndGetMemberSpecificationNullFieldDescriptor() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                0, 0, null, "myField", null
        );

        memberDialog.setMemberSpecification(fieldSpec);
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        assertEquals("myField", result.name, "Field name should match");
        assertNull(result.descriptor, "Field descriptor should be null");
    }

    /**
     * Test setMemberSpecification and getMemberSpecification with public field.
     */
    @Test
    public void testSetAndGetMemberSpecificationPublicField() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                AccessConstants.PUBLIC, 0, null, "publicField", "I"
        );

        memberDialog.setMemberSpecification(fieldSpec);
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.PUBLIC) != 0,
                   "Public flag should be set");
    }

    /**
     * Test setMemberSpecification and getMemberSpecification with private field.
     */
    @Test
    public void testSetAndGetMemberSpecificationPrivateField() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                AccessConstants.PRIVATE, 0, null, "privateField", "J"
        );

        memberDialog.setMemberSpecification(fieldSpec);
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.PRIVATE) != 0,
                   "Private flag should be set");
    }

    /**
     * Test setMemberSpecification and getMemberSpecification with protected field.
     */
    @Test
    public void testSetAndGetMemberSpecificationProtectedField() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                AccessConstants.PROTECTED, 0, null, "protectedField", "D"
        );

        memberDialog.setMemberSpecification(fieldSpec);
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.PROTECTED) != 0,
                   "Protected flag should be set");
    }

    /**
     * Test setMemberSpecification and getMemberSpecification with static field.
     */
    @Test
    public void testSetAndGetMemberSpecificationStaticField() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                AccessConstants.STATIC, 0, null, "staticField", "I"
        );

        memberDialog.setMemberSpecification(fieldSpec);
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.STATIC) != 0,
                   "Static flag should be set");
    }

    /**
     * Test setMemberSpecification and getMemberSpecification with final field.
     */
    @Test
    public void testSetAndGetMemberSpecificationFinalField() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                AccessConstants.FINAL, 0, null, "finalField", "I"
        );

        memberDialog.setMemberSpecification(fieldSpec);
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.FINAL) != 0,
                   "Final flag should be set");
    }

    /**
     * Test setMemberSpecification and getMemberSpecification with volatile field.
     */
    @Test
    public void testSetAndGetMemberSpecificationVolatileField() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                AccessConstants.VOLATILE, 0, null, "volatileField", "I"
        );

        memberDialog.setMemberSpecification(fieldSpec);
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.VOLATILE) != 0,
                   "Volatile flag should be set");
    }

    /**
     * Test setMemberSpecification and getMemberSpecification with transient field.
     */
    @Test
    public void testSetAndGetMemberSpecificationTransientField() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                AccessConstants.TRANSIENT, 0, null, "transientField", "Ljava/lang/Object;"
        );

        memberDialog.setMemberSpecification(fieldSpec);
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.TRANSIENT) != 0,
                   "Transient flag should be set");
    }

    /**
     * Test setMemberSpecification and getMemberSpecification with synthetic field.
     */
    @Test
    public void testSetAndGetMemberSpecificationSyntheticField() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                AccessConstants.SYNTHETIC, 0, null, "syntheticField", "I"
        );

        memberDialog.setMemberSpecification(fieldSpec);
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.SYNTHETIC) != 0,
                   "Synthetic flag should be set");
    }

    /**
     * Test setMemberSpecification and getMemberSpecification with non-public field (required unset).
     */
    @Test
    public void testSetAndGetMemberSpecificationNonPublicField() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                0, AccessConstants.PUBLIC, null, "nonPublicField", "I"
        );

        memberDialog.setMemberSpecification(fieldSpec);
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        assertTrue((result.requiredUnsetAccessFlags & AccessConstants.PUBLIC) != 0,
                   "Public flag should be unset");
    }

    /**
     * Test setMemberSpecification and getMemberSpecification with non-static field (required unset).
     */
    @Test
    public void testSetAndGetMemberSpecificationNonStaticField() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                0, AccessConstants.STATIC, null, "nonStaticField", "I"
        );

        memberDialog.setMemberSpecification(fieldSpec);
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        assertTrue((result.requiredUnsetAccessFlags & AccessConstants.STATIC) != 0,
                   "Static flag should be unset");
    }

    /**
     * Test setMemberSpecification and getMemberSpecification with combined flags for field.
     */
    @Test
    public void testSetAndGetMemberSpecificationCombinedFlagsField() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                AccessConstants.PUBLIC | AccessConstants.STATIC | AccessConstants.FINAL,
                0,
                null,
                "constantField",
                "I"
        );

        memberDialog.setMemberSpecification(fieldSpec);
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.PUBLIC) != 0,
                   "Public flag should be set");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.STATIC) != 0,
                   "Static flag should be set");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.FINAL) != 0,
                   "Final flag should be set");
    }

    /**
     * Test setMemberSpecification and getMemberSpecification with annotation type for field.
     */
    @Test
    public void testSetAndGetMemberSpecificationFieldWithAnnotation() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                0, 0, "Ljava/lang/Deprecated;", "deprecatedField", "I"
        );

        memberDialog.setMemberSpecification(fieldSpec);
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        assertEquals("Ljava/lang/Deprecated;", result.annotationType,
                     "Annotation type should match");
    }

    // setMemberSpecification and getMemberSpecification tests - method specifications

    /**
     * Test setMemberSpecification and getMemberSpecification with simple method specification.
     */
    @Test
    public void testSetAndGetMemberSpecificationSimpleMethod() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                0, 0, null, "testMethod", "()V"
        );

        memberDialog.setMemberSpecification(methodSpec);
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        assertEquals("testMethod", result.name, "Method name should match");
        assertEquals("()V", result.descriptor, "Method descriptor should match");
    }

    /**
     * Test setMemberSpecification and getMemberSpecification with method with parameters.
     */
    @Test
    public void testSetAndGetMemberSpecificationMethodWithParameters() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                0, 0, null, "calculate", "(II)I"
        );

        memberDialog.setMemberSpecification(methodSpec);
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        assertEquals("calculate", result.name, "Method name should match");
        assertEquals("(II)I", result.descriptor, "Method descriptor should match");
    }

    /**
     * Test setMemberSpecification and getMemberSpecification with method returning object.
     */
    @Test
    public void testSetAndGetMemberSpecificationMethodReturningObject() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                0, 0, null, "toString", "()Ljava/lang/String;"
        );

        memberDialog.setMemberSpecification(methodSpec);
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        assertEquals("toString", result.name, "Method name should match");
        assertEquals("()Ljava/lang/String;", result.descriptor, "Method descriptor should match");
    }

    /**
     * Test setMemberSpecification and getMemberSpecification with null method name.
     */
    @Test
    public void testSetAndGetMemberSpecificationNullMethodName() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                0, 0, null, null, "()V"
        );

        memberDialog.setMemberSpecification(methodSpec);
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        assertNull(result.name, "Method name should be null");
    }

    /**
     * Test setMemberSpecification and getMemberSpecification with null method descriptor.
     */
    @Test
    public void testSetAndGetMemberSpecificationNullMethodDescriptor() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                0, 0, null, "anyMethod", null
        );

        memberDialog.setMemberSpecification(methodSpec);
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        assertEquals("anyMethod", result.name, "Method name should match");
        assertNull(result.descriptor, "Method descriptor should be null");
    }

    /**
     * Test setMemberSpecification and getMemberSpecification with public method.
     */
    @Test
    public void testSetAndGetMemberSpecificationPublicMethod() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                AccessConstants.PUBLIC, 0, null, "publicMethod", "()V"
        );

        memberDialog.setMemberSpecification(methodSpec);
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.PUBLIC) != 0,
                   "Public flag should be set");
    }

    /**
     * Test setMemberSpecification and getMemberSpecification with private method.
     */
    @Test
    public void testSetAndGetMemberSpecificationPrivateMethod() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                AccessConstants.PRIVATE, 0, null, "privateMethod", "()V"
        );

        memberDialog.setMemberSpecification(methodSpec);
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.PRIVATE) != 0,
                   "Private flag should be set");
    }

    /**
     * Test setMemberSpecification and getMemberSpecification with protected method.
     */
    @Test
    public void testSetAndGetMemberSpecificationProtectedMethod() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                AccessConstants.PROTECTED, 0, null, "protectedMethod", "()V"
        );

        memberDialog.setMemberSpecification(methodSpec);
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.PROTECTED) != 0,
                   "Protected flag should be set");
    }

    /**
     * Test setMemberSpecification and getMemberSpecification with static method.
     */
    @Test
    public void testSetAndGetMemberSpecificationStaticMethod() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                AccessConstants.STATIC, 0, null, "staticMethod", "()V"
        );

        memberDialog.setMemberSpecification(methodSpec);
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.STATIC) != 0,
                   "Static flag should be set");
    }

    /**
     * Test setMemberSpecification and getMemberSpecification with final method.
     */
    @Test
    public void testSetAndGetMemberSpecificationFinalMethod() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                AccessConstants.FINAL, 0, null, "finalMethod", "()V"
        );

        memberDialog.setMemberSpecification(methodSpec);
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.FINAL) != 0,
                   "Final flag should be set");
    }

    /**
     * Test setMemberSpecification and getMemberSpecification with synchronized method.
     */
    @Test
    public void testSetAndGetMemberSpecificationSynchronizedMethod() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                AccessConstants.SYNCHRONIZED, 0, null, "syncMethod", "()V"
        );

        memberDialog.setMemberSpecification(methodSpec);
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.SYNCHRONIZED) != 0,
                   "Synchronized flag should be set");
    }

    /**
     * Test setMemberSpecification and getMemberSpecification with native method.
     */
    @Test
    public void testSetAndGetMemberSpecificationNativeMethod() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                AccessConstants.NATIVE, 0, null, "nativeMethod", "()V"
        );

        memberDialog.setMemberSpecification(methodSpec);
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.NATIVE) != 0,
                   "Native flag should be set");
    }

    /**
     * Test setMemberSpecification and getMemberSpecification with abstract method.
     */
    @Test
    public void testSetAndGetMemberSpecificationAbstractMethod() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                AccessConstants.ABSTRACT, 0, null, "abstractMethod", "()V"
        );

        memberDialog.setMemberSpecification(methodSpec);
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.ABSTRACT) != 0,
                   "Abstract flag should be set");
    }

    /**
     * Test setMemberSpecification and getMemberSpecification with strict method.
     */
    @Test
    public void testSetAndGetMemberSpecificationStrictMethod() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                AccessConstants.STRICT, 0, null, "strictMethod", "()V"
        );

        memberDialog.setMemberSpecification(methodSpec);
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.STRICT) != 0,
                   "Strict flag should be set");
    }

    /**
     * Test setMemberSpecification and getMemberSpecification with bridge method.
     */
    @Test
    public void testSetAndGetMemberSpecificationBridgeMethod() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                AccessConstants.BRIDGE, 0, null, "bridgeMethod", "()V"
        );

        memberDialog.setMemberSpecification(methodSpec);
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.BRIDGE) != 0,
                   "Bridge flag should be set");
    }

    /**
     * Test setMemberSpecification and getMemberSpecification with varargs method.
     */
    @Test
    public void testSetAndGetMemberSpecificationVarargsMethod() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                AccessConstants.VARARGS, 0, null, "varargsMethod", "([Ljava/lang/String;)V"
        );

        memberDialog.setMemberSpecification(methodSpec);
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.VARARGS) != 0,
                   "Varargs flag should be set");
    }

    /**
     * Test setMemberSpecification and getMemberSpecification with synthetic method.
     */
    @Test
    public void testSetAndGetMemberSpecificationSyntheticMethod() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                AccessConstants.SYNTHETIC, 0, null, "syntheticMethod", "()V"
        );

        memberDialog.setMemberSpecification(methodSpec);
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.SYNTHETIC) != 0,
                   "Synthetic flag should be set");
    }

    /**
     * Test setMemberSpecification and getMemberSpecification with non-public method (required unset).
     */
    @Test
    public void testSetAndGetMemberSpecificationNonPublicMethod() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                0, AccessConstants.PUBLIC, null, "nonPublicMethod", "()V"
        );

        memberDialog.setMemberSpecification(methodSpec);
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        assertTrue((result.requiredUnsetAccessFlags & AccessConstants.PUBLIC) != 0,
                   "Public flag should be unset");
    }

    /**
     * Test setMemberSpecification and getMemberSpecification with non-abstract method (required unset).
     */
    @Test
    public void testSetAndGetMemberSpecificationNonAbstractMethod() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                0, AccessConstants.ABSTRACT, null, "concreteMethod", "()V"
        );

        memberDialog.setMemberSpecification(methodSpec);
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        assertTrue((result.requiredUnsetAccessFlags & AccessConstants.ABSTRACT) != 0,
                   "Abstract flag should be unset");
    }

    /**
     * Test setMemberSpecification and getMemberSpecification with combined flags for method.
     */
    @Test
    public void testSetAndGetMemberSpecificationCombinedFlagsMethod() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                AccessConstants.PUBLIC | AccessConstants.STATIC | AccessConstants.FINAL,
                0,
                null,
                "utilityMethod",
                "()V"
        );

        memberDialog.setMemberSpecification(methodSpec);
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.PUBLIC) != 0,
                   "Public flag should be set");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.STATIC) != 0,
                   "Static flag should be set");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.FINAL) != 0,
                   "Final flag should be set");
    }

    /**
     * Test setMemberSpecification and getMemberSpecification with annotation type for method.
     */
    @Test
    public void testSetAndGetMemberSpecificationMethodWithAnnotation() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                0, 0, "Ljava/lang/Override;", "toString", "()Ljava/lang/String;"
        );

        memberDialog.setMemberSpecification(methodSpec);
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        assertEquals("Ljava/lang/Override;", result.annotationType,
                     "Annotation type should match");
    }

    // showDialog tests

    /**
     * Test that showDialog returns CANCEL_OPTION when dialog is closed.
     */
    @Test
    public void testShowDialogReturnsCancelOption() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Use a separate thread to close the dialog after a short delay
        Thread closer = new Thread(() -> {
            try {
                Thread.sleep(100);
                SwingUtilities.invokeLater(() -> {
                    if (memberDialog != null && memberDialog.isVisible()) {
                        memberDialog.dispatchEvent(new WindowEvent(memberDialog, WindowEvent.WINDOW_CLOSING));
                    }
                });
            } catch (InterruptedException e) {
                // Ignore
            }
        });
        closer.start();

        int result = memberDialog.showDialog();

        assertEquals(MemberSpecificationDialog.CANCEL_OPTION, result,
                    "showDialog should return CANCEL_OPTION when dialog is closed");
    }

    /**
     * Test that showDialog makes the dialog visible.
     */
    @Test
    public void testShowDialogMakesDialogVisible() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        assertFalse(memberDialog.isVisible(), "Dialog should not be visible before showDialog");

        // Use a separate thread to verify visibility and close the dialog
        Thread checker = new Thread(() -> {
            try {
                Thread.sleep(100);
                SwingUtilities.invokeLater(() -> {
                    if (memberDialog != null) {
                        assertTrue(memberDialog.isVisible(), "Dialog should be visible during showDialog");
                        memberDialog.dispatchEvent(new WindowEvent(memberDialog, WindowEvent.WINDOW_CLOSING));
                    }
                });
            } catch (InterruptedException e) {
                // Ignore
            }
        });
        checker.start();

        memberDialog.showDialog();
    }

    /**
     * Test that APPROVE_OPTION constant has expected value.
     */
    @Test
    public void testApproveOptionConstant() {
        assertEquals(0, MemberSpecificationDialog.APPROVE_OPTION,
                    "APPROVE_OPTION should have value 0");
    }

    /**
     * Test that CANCEL_OPTION constant has expected value.
     */
    @Test
    public void testCancelOptionConstant() {
        assertEquals(1, MemberSpecificationDialog.CANCEL_OPTION,
                    "CANCEL_OPTION should have value 1");
    }

    // Additional edge case tests

    /**
     * Test that multiple calls to setMemberSpecification and getMemberSpecification work correctly.
     */
    @Test
    public void testMultipleSetGetCalls() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // First specification
        MemberSpecification fieldSpec1 = new MemberSpecification(
                AccessConstants.PUBLIC, 0, null, "field1", "I"
        );
        memberDialog.setMemberSpecification(fieldSpec1);
        MemberSpecification result1 = memberDialog.getMemberSpecification();
        assertEquals("field1", result1.name, "First field name should match");

        // Second specification
        MemberSpecification fieldSpec2 = new MemberSpecification(
                AccessConstants.PRIVATE, 0, null, "field2", "Ljava/lang/String;"
        );
        memberDialog.setMemberSpecification(fieldSpec2);
        MemberSpecification result2 = memberDialog.getMemberSpecification();
        assertEquals("field2", result2.name, "Second field name should match");
    }

    /**
     * Test that dialog can be disposed and recreated.
     */
    @Test
    public void testDialogDisposeAndRecreate() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                0, 0, null, "field", "I"
        );
        memberDialog.setMemberSpecification(fieldSpec);
        memberDialog.dispose();

        // Create a new dialog
        memberDialog = new MemberSpecificationDialog(testDialog, true);
        assertNotNull(memberDialog, "New dialog should be created after disposing previous one");

        // Set specification on new dialog should work
        memberDialog.setMemberSpecification(fieldSpec);
        MemberSpecification result = memberDialog.getMemberSpecification();
        assertNotNull(result, "Result should not be null");
    }

    /**
     * Test setMemberSpecification with empty MemberSpecification.
     */
    @Test
    public void testSetMemberSpecificationEmpty() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification emptySpec = new MemberSpecification();

        memberDialog.setMemberSpecification(emptySpec);
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
    }

    /**
     * Test that field dialog and method dialog have different behavior for descriptors.
     */
    @Test
    public void testFieldVsMethodDescriptorHandling() {
        testDialog = new JDialog();

        // Field dialog
        MemberSpecificationDialog fieldDialog = new MemberSpecificationDialog(testDialog, true);
        MemberSpecification fieldSpec = new MemberSpecification(
                0, 0, null, "member", "I"
        );
        fieldDialog.setMemberSpecification(fieldSpec);
        MemberSpecification fieldResult = fieldDialog.getMemberSpecification();
        assertNotNull(fieldResult, "Field result should not be null");
        fieldDialog.dispose();

        // Method dialog
        MemberSpecificationDialog methodDialog = new MemberSpecificationDialog(testDialog, false);
        MemberSpecification methodSpec = new MemberSpecification(
                0, 0, null, "member", "()I"
        );
        methodDialog.setMemberSpecification(methodSpec);
        MemberSpecification methodResult = methodDialog.getMemberSpecification();
        assertNotNull(methodResult, "Method result should not be null");
        methodDialog.dispose();
    }

    /**
     * Test setMemberSpecification with complex method descriptor with multiple parameters.
     */
    @Test
    public void testSetAndGetMemberSpecificationComplexMethodDescriptor() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                0, 0, null, "complexMethod",
                "(ILjava/lang/String;[Ljava/lang/Object;)Ljava/util/List;"
        );

        memberDialog.setMemberSpecification(methodSpec);
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        assertEquals("complexMethod", result.name, "Method name should match");
        assertNotNull(result.descriptor, "Method descriptor should not be null");
    }

    /**
     * Test that access flags are properly preserved when only unset flags are specified.
     */
    @Test
    public void testSetAndGetMemberSpecificationOnlyUnsetFlags() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                0,
                AccessConstants.STATIC | AccessConstants.FINAL,
                null,
                "instanceField",
                "I"
        );

        memberDialog.setMemberSpecification(fieldSpec);
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        assertEquals(0, result.requiredSetAccessFlags, "Required set flags should be 0");
        assertTrue((result.requiredUnsetAccessFlags & AccessConstants.STATIC) != 0,
                   "Static flag should be unset");
        assertTrue((result.requiredUnsetAccessFlags & AccessConstants.FINAL) != 0,
                   "Final flag should be unset");
    }

    /**
     * Test that access flags are properly preserved with both set and unset flags.
     */
    @Test
    public void testSetAndGetMemberSpecificationSetAndUnsetFlags() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                AccessConstants.PUBLIC,
                AccessConstants.STATIC,
                null,
                "instancePublicField",
                "I"
        );

        memberDialog.setMemberSpecification(fieldSpec);
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.PUBLIC) != 0,
                   "Public flag should be set");
        assertTrue((result.requiredUnsetAccessFlags & AccessConstants.STATIC) != 0,
                   "Static flag should be unset");
    }
}
