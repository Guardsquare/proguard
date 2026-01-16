package proguard.gui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.MemberSpecification;
import proguard.classfile.AccessConstants;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Test class for MemberSpecificationDialog.setMemberSpecification() method.
 *
 * The setMemberSpecification method (lines 326-363):
 * - Extracts annotationType, name, and descriptor from the MemberSpecification (lines 328-330)
 * - Sets the annotation type text field (line 333)
 * - Calls setMemberSpecificationRadioButtons for all access modifiers (lines 336-349)
 * - Sets the name text field (line 352)
 * - For fields (isField=true): Sets the type text field (line 356)
 * - For methods (isField=false): Sets the return type and arguments text fields (lines 360-361)
 *
 * These tests verify:
 * - The method correctly extracts and sets all fields from MemberSpecification
 * - Annotation type handling (null and non-null values)
 * - Name handling (null and non-null values)
 * - Descriptor handling for both fields and methods
 * - All access modifiers are properly set via radio buttons
 * - Field-specific branch (isField=true) is executed
 * - Method-specific branch (isField=false) is executed
 * - External type conversion is applied correctly
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class MemberSpecificationDialogClaude_setMemberSpecificationTest {

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

    /**
     * Test setMemberSpecification with simple field specification.
     * This covers lines 328-330, 333, 336-349, 352, 354-356.
     */
    @Test
    public void testSetMemberSpecificationWithSimpleField() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                0, 0, null, "testField", "I"
        );

        // This should execute all lines of setMemberSpecification for field dialog
        memberDialog.setMemberSpecification(fieldSpec);

        // Verify by getting the specification back
        MemberSpecification result = memberDialog.getMemberSpecification();
        assertEquals("testField", result.name, "Field name should be set correctly");
        assertEquals("I", result.descriptor, "Field descriptor should be set correctly");
    }

    /**
     * Test setMemberSpecification with simple method specification.
     * This covers lines 328-330, 333, 336-349, 352, 360-361.
     */
    @Test
    public void testSetMemberSpecificationWithSimpleMethod() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                0, 0, null, "testMethod", "()V"
        );

        // This should execute all lines of setMemberSpecification for method dialog
        memberDialog.setMemberSpecification(methodSpec);

        // Verify by getting the specification back
        MemberSpecification result = memberDialog.getMemberSpecification();
        assertEquals("testMethod", result.name, "Method name should be set correctly");
        assertEquals("()V", result.descriptor, "Method descriptor should be set correctly");
    }

    /**
     * Test setMemberSpecification with null annotation type.
     * This covers line 333 with annotationType == null.
     */
    @Test
    public void testSetMemberSpecificationWithNullAnnotationType() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                0, 0, null, "field", "I"
        );

        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();
        assertNull(result.annotationType, "Annotation type should be null");
    }

    /**
     * Test setMemberSpecification with non-null annotation type.
     * This covers line 333 with annotationType != null.
     */
    @Test
    public void testSetMemberSpecificationWithAnnotationType() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                0, 0, "Ljava/lang/Deprecated;", "deprecatedField", "I"
        );

        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();
        assertEquals("Ljava/lang/Deprecated;", result.annotationType,
                     "Annotation type should be set correctly");
    }

    /**
     * Test setMemberSpecification with null name.
     * This covers line 352 with name == null.
     */
    @Test
    public void testSetMemberSpecificationWithNullName() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                0, 0, null, null, "I"
        );

        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();
        assertNull(result.name, "Name should be null for wildcard");
    }

    /**
     * Test setMemberSpecification with non-null name.
     * This covers line 352 with name != null.
     */
    @Test
    public void testSetMemberSpecificationWithName() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                0, 0, null, "myField", "I"
        );

        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();
        assertEquals("myField", result.name, "Name should be set correctly");
    }

    /**
     * Test setMemberSpecification with null descriptor for field.
     * This covers line 356 with descriptor == null in field branch.
     */
    @Test
    public void testSetMemberSpecificationWithNullDescriptorForField() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                0, 0, null, "anyField", null
        );

        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();
        assertNull(result.descriptor, "Descriptor should be null for wildcard");
    }

    /**
     * Test setMemberSpecification with non-null descriptor for field.
     * This covers line 356 with descriptor != null in field branch.
     */
    @Test
    public void testSetMemberSpecificationWithDescriptorForField() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                0, 0, null, "stringField", "Ljava/lang/String;"
        );

        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();
        assertEquals("Ljava/lang/String;", result.descriptor,
                     "Field descriptor should be set correctly");
    }

    /**
     * Test setMemberSpecification with null descriptor for method.
     * This covers lines 360-361 with descriptor == null in method branch.
     */
    @Test
    public void testSetMemberSpecificationWithNullDescriptorForMethod() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                0, 0, null, "anyMethod", null
        );

        memberDialog.setMemberSpecification(methodSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();
        assertNull(result.descriptor, "Descriptor should be null for wildcard");
    }

    /**
     * Test setMemberSpecification with non-null descriptor for method.
     * This covers lines 360-361 with descriptor != null in method branch.
     */
    @Test
    public void testSetMemberSpecificationWithDescriptorForMethod() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                0, 0, null, "getString", "()Ljava/lang/String;"
        );

        memberDialog.setMemberSpecification(methodSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();
        assertEquals("()Ljava/lang/String;", result.descriptor,
                     "Method descriptor should be set correctly");
    }

    /**
     * Test setMemberSpecification with public access modifier.
     * This covers line 336 where setMemberSpecificationRadioButtons is called for PUBLIC.
     */
    @Test
    public void testSetMemberSpecificationWithPublicModifier() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                AccessConstants.PUBLIC, 0, null, "publicField", "I"
        );

        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();
        assertTrue((result.requiredSetAccessFlags & AccessConstants.PUBLIC) != 0,
                   "Public flag should be set");
    }

    /**
     * Test setMemberSpecification with private access modifier.
     * This covers line 337 where setMemberSpecificationRadioButtons is called for PRIVATE.
     */
    @Test
    public void testSetMemberSpecificationWithPrivateModifier() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                AccessConstants.PRIVATE, 0, null, "privateField", "I"
        );

        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();
        assertTrue((result.requiredSetAccessFlags & AccessConstants.PRIVATE) != 0,
                   "Private flag should be set");
    }

    /**
     * Test setMemberSpecification with protected access modifier.
     * This covers line 338 where setMemberSpecificationRadioButtons is called for PROTECTED.
     */
    @Test
    public void testSetMemberSpecificationWithProtectedModifier() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                AccessConstants.PROTECTED, 0, null, "protectedField", "I"
        );

        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();
        assertTrue((result.requiredSetAccessFlags & AccessConstants.PROTECTED) != 0,
                   "Protected flag should be set");
    }

    /**
     * Test setMemberSpecification with static modifier.
     * This covers line 339 where setMemberSpecificationRadioButtons is called for STATIC.
     */
    @Test
    public void testSetMemberSpecificationWithStaticModifier() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                AccessConstants.STATIC, 0, null, "staticField", "I"
        );

        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();
        assertTrue((result.requiredSetAccessFlags & AccessConstants.STATIC) != 0,
                   "Static flag should be set");
    }

    /**
     * Test setMemberSpecification with final modifier.
     * This covers line 340 where setMemberSpecificationRadioButtons is called for FINAL.
     */
    @Test
    public void testSetMemberSpecificationWithFinalModifier() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                AccessConstants.FINAL, 0, null, "finalField", "I"
        );

        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();
        assertTrue((result.requiredSetAccessFlags & AccessConstants.FINAL) != 0,
                   "Final flag should be set");
    }

    /**
     * Test setMemberSpecification with synthetic modifier.
     * This covers line 341 where setMemberSpecificationRadioButtons is called for SYNTHETIC.
     */
    @Test
    public void testSetMemberSpecificationWithSyntheticModifier() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                AccessConstants.SYNTHETIC, 0, null, "syntheticField", "I"
        );

        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();
        assertTrue((result.requiredSetAccessFlags & AccessConstants.SYNTHETIC) != 0,
                   "Synthetic flag should be set");
    }

    /**
     * Test setMemberSpecification with volatile modifier (field-specific).
     * This covers line 342 where setMemberSpecificationRadioButtons is called for VOLATILE.
     */
    @Test
    public void testSetMemberSpecificationWithVolatileModifier() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                AccessConstants.VOLATILE, 0, null, "volatileField", "I"
        );

        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();
        assertTrue((result.requiredSetAccessFlags & AccessConstants.VOLATILE) != 0,
                   "Volatile flag should be set");
    }

    /**
     * Test setMemberSpecification with transient modifier (field-specific).
     * This covers line 343 where setMemberSpecificationRadioButtons is called for TRANSIENT.
     */
    @Test
    public void testSetMemberSpecificationWithTransientModifier() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                AccessConstants.TRANSIENT, 0, null, "transientField", "I"
        );

        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();
        assertTrue((result.requiredSetAccessFlags & AccessConstants.TRANSIENT) != 0,
                   "Transient flag should be set");
    }

    /**
     * Test setMemberSpecification with synchronized modifier (method-specific).
     * This covers line 344 where setMemberSpecificationRadioButtons is called for SYNCHRONIZED.
     */
    @Test
    public void testSetMemberSpecificationWithSynchronizedModifier() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                AccessConstants.SYNCHRONIZED, 0, null, "syncMethod", "()V"
        );

        memberDialog.setMemberSpecification(methodSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();
        assertTrue((result.requiredSetAccessFlags & AccessConstants.SYNCHRONIZED) != 0,
                   "Synchronized flag should be set");
    }

    /**
     * Test setMemberSpecification with native modifier (method-specific).
     * This covers line 345 where setMemberSpecificationRadioButtons is called for NATIVE.
     */
    @Test
    public void testSetMemberSpecificationWithNativeModifier() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                AccessConstants.NATIVE, 0, null, "nativeMethod", "()V"
        );

        memberDialog.setMemberSpecification(methodSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();
        assertTrue((result.requiredSetAccessFlags & AccessConstants.NATIVE) != 0,
                   "Native flag should be set");
    }

    /**
     * Test setMemberSpecification with abstract modifier (method-specific).
     * This covers line 346 where setMemberSpecificationRadioButtons is called for ABSTRACT.
     */
    @Test
    public void testSetMemberSpecificationWithAbstractModifier() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                AccessConstants.ABSTRACT, 0, null, "abstractMethod", "()V"
        );

        memberDialog.setMemberSpecification(methodSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();
        assertTrue((result.requiredSetAccessFlags & AccessConstants.ABSTRACT) != 0,
                   "Abstract flag should be set");
    }

    /**
     * Test setMemberSpecification with strict modifier (method-specific).
     * This covers line 347 where setMemberSpecificationRadioButtons is called for STRICT.
     */
    @Test
    public void testSetMemberSpecificationWithStrictModifier() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                AccessConstants.STRICT, 0, null, "strictMethod", "()V"
        );

        memberDialog.setMemberSpecification(methodSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();
        assertTrue((result.requiredSetAccessFlags & AccessConstants.STRICT) != 0,
                   "Strict flag should be set");
    }

    /**
     * Test setMemberSpecification with bridge modifier (method-specific).
     * This covers line 348 where setMemberSpecificationRadioButtons is called for BRIDGE.
     */
    @Test
    public void testSetMemberSpecificationWithBridgeModifier() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                AccessConstants.BRIDGE, 0, null, "bridgeMethod", "()V"
        );

        memberDialog.setMemberSpecification(methodSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();
        assertTrue((result.requiredSetAccessFlags & AccessConstants.BRIDGE) != 0,
                   "Bridge flag should be set");
    }

    /**
     * Test setMemberSpecification with varargs modifier (method-specific).
     * This covers line 349 where setMemberSpecificationRadioButtons is called for VARARGS.
     */
    @Test
    public void testSetMemberSpecificationWithVarargsModifier() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                AccessConstants.VARARGS, 0, null, "varargsMethod", "([Ljava/lang/String;)V"
        );

        memberDialog.setMemberSpecification(methodSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();
        assertTrue((result.requiredSetAccessFlags & AccessConstants.VARARGS) != 0,
                   "Varargs flag should be set");
    }

    /**
     * Test setMemberSpecification with combined access modifiers.
     * This tests multiple lines 336-349 simultaneously.
     */
    @Test
    public void testSetMemberSpecificationWithCombinedModifiers() {
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
        assertTrue((result.requiredSetAccessFlags & AccessConstants.PUBLIC) != 0,
                   "Public flag should be set");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.STATIC) != 0,
                   "Static flag should be set");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.FINAL) != 0,
                   "Final flag should be set");
    }

    /**
     * Test setMemberSpecification executes field branch (isField=true).
     * This specifically tests lines 354-356 in the field branch.
     */
    @Test
    public void testSetMemberSpecificationExecutesFieldBranch() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                0, 0, null, "testField", "Ljava/lang/Object;"
        );

        // This should execute the field branch (line 354-356)
        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();
        assertEquals("Ljava/lang/Object;", result.descriptor,
                     "Field descriptor should be set via field branch");
    }

    /**
     * Test setMemberSpecification executes method branch (isField=false).
     * This specifically tests lines 360-361 in the method branch.
     */
    @Test
    public void testSetMemberSpecificationExecutesMethodBranch() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                0, 0, null, "testMethod", "(ILjava/lang/String;)V"
        );

        // This should execute the method branch (lines 360-361)
        memberDialog.setMemberSpecification(methodSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();
        assertEquals("(ILjava/lang/String;)V", result.descriptor,
                     "Method descriptor should be set via method branch");
    }

    /**
     * Test setMemberSpecification with method having complex parameters.
     * This tests line 361 with complex argument parsing.
     */
    @Test
    public void testSetMemberSpecificationWithComplexMethodParameters() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                0, 0, null, "complexMethod",
                "(ILjava/lang/String;[Ljava/lang/Object;)Ljava/util/List;"
        );

        memberDialog.setMemberSpecification(methodSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();
        assertEquals("(ILjava/lang/String;[Ljava/lang/Object;)Ljava/util/List;",
                     result.descriptor,
                     "Complex method descriptor should be set correctly");
    }

    /**
     * Test setMemberSpecification can be called multiple times with different values.
     * This ensures the method properly updates all fields on subsequent calls.
     */
    @Test
    public void testSetMemberSpecificationMultipleTimes() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // First call
        MemberSpecification fieldSpec1 = new MemberSpecification(
                AccessConstants.PUBLIC, 0, null, "field1", "I"
        );
        memberDialog.setMemberSpecification(fieldSpec1);
        MemberSpecification result1 = memberDialog.getMemberSpecification();
        assertEquals("field1", result1.name, "First name should be set");

        // Second call
        MemberSpecification fieldSpec2 = new MemberSpecification(
                AccessConstants.PRIVATE, 0, null, "field2", "J"
        );
        memberDialog.setMemberSpecification(fieldSpec2);
        MemberSpecification result2 = memberDialog.getMemberSpecification();
        assertEquals("field2", result2.name, "Second name should be set");
        assertTrue((result2.requiredSetAccessFlags & AccessConstants.PRIVATE) != 0,
                   "Private flag should be set");
    }

    /**
     * Test setMemberSpecification with all fields null.
     * This tests the null handling in lines 328-330, 333, 352, 356.
     */
    @Test
    public void testSetMemberSpecificationWithAllNullFields() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                0, 0, null, null, null
        );

        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();
        assertNull(result.annotationType, "Annotation type should be null");
        assertNull(result.name, "Name should be null");
        assertNull(result.descriptor, "Descriptor should be null");
    }

    /**
     * Test setMemberSpecification with all fields set.
     * This tests the non-null handling in lines 333, 352, 356.
     */
    @Test
    public void testSetMemberSpecificationWithAllFieldsSet() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                AccessConstants.PUBLIC | AccessConstants.STATIC | AccessConstants.FINAL,
                0,
                "Ljava/lang/Deprecated;",
                "CONSTANT_VALUE",
                "Ljava/lang/String;"
        );

        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();
        assertEquals("Ljava/lang/Deprecated;", result.annotationType,
                     "Annotation type should be set");
        assertEquals("CONSTANT_VALUE", result.name, "Name should be set");
        assertEquals("Ljava/lang/String;", result.descriptor, "Descriptor should be set");
    }

    /**
     * Test setMemberSpecification with primitive field types.
     * This tests line 356 with various primitive descriptors.
     */
    @Test
    public void testSetMemberSpecificationWithPrimitiveFieldTypes() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Test with int
        MemberSpecification intField = new MemberSpecification(0, 0, null, "intField", "I");
        memberDialog.setMemberSpecification(intField);
        assertEquals("I", memberDialog.getMemberSpecification().descriptor);

        // Test with long
        MemberSpecification longField = new MemberSpecification(0, 0, null, "longField", "J");
        memberDialog.setMemberSpecification(longField);
        assertEquals("J", memberDialog.getMemberSpecification().descriptor);

        // Test with double
        MemberSpecification doubleField = new MemberSpecification(0, 0, null, "doubleField", "D");
        memberDialog.setMemberSpecification(doubleField);
        assertEquals("D", memberDialog.getMemberSpecification().descriptor);

        // Test with boolean
        MemberSpecification boolField = new MemberSpecification(0, 0, null, "boolField", "Z");
        memberDialog.setMemberSpecification(boolField);
        assertEquals("Z", memberDialog.getMemberSpecification().descriptor);
    }

    /**
     * Test setMemberSpecification with array field types.
     * This tests line 356 with array descriptors.
     */
    @Test
    public void testSetMemberSpecificationWithArrayFieldTypes() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification arrayField = new MemberSpecification(
                0, 0, null, "arrayField", "[Ljava/lang/String;"
        );

        memberDialog.setMemberSpecification(arrayField);

        MemberSpecification result = memberDialog.getMemberSpecification();
        assertEquals("[Ljava/lang/String;", result.descriptor,
                     "Array descriptor should be set correctly");
    }

    /**
     * Test setMemberSpecification with method returning void.
     * This tests lines 360-361 with void return type.
     */
    @Test
    public void testSetMemberSpecificationWithVoidReturnType() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                0, 0, null, "voidMethod", "()V"
        );

        memberDialog.setMemberSpecification(methodSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();
        assertEquals("()V", result.descriptor, "Void method descriptor should be set correctly");
    }

    /**
     * Test setMemberSpecification with method returning primitive.
     * This tests lines 360-361 with primitive return type.
     */
    @Test
    public void testSetMemberSpecificationWithPrimitiveReturnType() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                0, 0, null, "getInt", "()I"
        );

        memberDialog.setMemberSpecification(methodSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();
        assertEquals("()I", result.descriptor,
                     "Primitive return type descriptor should be set correctly");
    }

    /**
     * Test setMemberSpecification with method having no parameters.
     * This tests line 361 with empty parameter list.
     */
    @Test
    public void testSetMemberSpecificationWithNoParameters() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                0, 0, null, "noParams", "()Ljava/lang/String;"
        );

        memberDialog.setMemberSpecification(methodSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();
        assertEquals("()Ljava/lang/String;", result.descriptor,
                     "No-parameter method descriptor should be set correctly");
    }

    /**
     * Test setMemberSpecification with method having single parameter.
     * This tests line 361 with single parameter.
     */
    @Test
    public void testSetMemberSpecificationWithSingleParameter() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                0, 0, null, "oneParam", "(I)V"
        );

        memberDialog.setMemberSpecification(methodSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();
        assertEquals("(I)V", result.descriptor,
                     "Single parameter method descriptor should be set correctly");
    }

    /**
     * Test setMemberSpecification with method having multiple parameters.
     * This tests line 361 with multiple parameters.
     */
    @Test
    public void testSetMemberSpecificationWithMultipleParameters() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                0, 0, null, "multipleParams", "(ILjava/lang/String;Z)V"
        );

        memberDialog.setMemberSpecification(methodSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();
        assertEquals("(ILjava/lang/String;Z)V", result.descriptor,
                     "Multiple parameter method descriptor should be set correctly");
    }

    /**
     * Test setMemberSpecification with all annotation, name, and descriptor combinations.
     * This is a comprehensive test covering all major code paths.
     */
    @Test
    public void testSetMemberSpecificationWithAllCombinations() {
        testDialog = new JDialog();

        // Test field dialog with all values
        MemberSpecificationDialog fieldDialog = new MemberSpecificationDialog(testDialog, true);
        MemberSpecification fullFieldSpec = new MemberSpecification(
                AccessConstants.PUBLIC | AccessConstants.STATIC | AccessConstants.FINAL |
                AccessConstants.VOLATILE | AccessConstants.TRANSIENT | AccessConstants.SYNTHETIC,
                0,
                "Ljava/lang/Deprecated;",
                "fullField",
                "Ljava/lang/String;"
        );
        fieldDialog.setMemberSpecification(fullFieldSpec);
        MemberSpecification fieldResult = fieldDialog.getMemberSpecification();
        assertNotNull(fieldResult, "Field result should not be null");
        fieldDialog.dispose();

        // Test method dialog with all values
        memberDialog = new MemberSpecificationDialog(testDialog, false);
        MemberSpecification fullMethodSpec = new MemberSpecification(
                AccessConstants.PUBLIC | AccessConstants.STATIC | AccessConstants.FINAL |
                AccessConstants.SYNCHRONIZED | AccessConstants.NATIVE | AccessConstants.ABSTRACT |
                AccessConstants.STRICT | AccessConstants.BRIDGE | AccessConstants.VARARGS |
                AccessConstants.SYNTHETIC,
                0,
                "Ljava/lang/Override;",
                "fullMethod",
                "(ILjava/lang/String;[Ljava/lang/Object;)Ljava/util/List;"
        );
        memberDialog.setMemberSpecification(fullMethodSpec);
        MemberSpecification methodResult = memberDialog.getMemberSpecification();
        assertNotNull(methodResult, "Method result should not be null");
    }
}
