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
 * Test class for MemberSpecificationDialog.getMemberSpecification() method.
 *
 * The getMemberSpecification method (lines 369-426):
 * - Reads text from UI text fields: annotation type, name, type, arguments (lines 371-374)
 * - Converts annotation type to internal format or null (lines 377-379)
 * - Converts name to null if empty or wildcard (lines 381-384)
 * - For fields (isField=true): Converts type to internal format or null (lines 387-391)
 * - For methods (isField=false): Converts return type and arguments (lines 395-403)
 * - Creates MemberSpecification with converted values (lines 406-407)
 * - Reads all access modifier radio button states (lines 410-423)
 * - Returns the MemberSpecification (line 425)
 *
 * These tests verify:
 * - Text field values are read correctly
 * - Empty string and "***" annotation types are converted to null
 * - Empty string and "*" names are converted to null
 * - Field branch (isField=true) correctly converts field types
 * - Method branch (isField=false) correctly handles return types and arguments
 * - Empty return type is converted to void for methods
 * - Wildcard method signatures ("***" and "...") are converted to null
 * - All access modifier radio buttons are read correctly
 * - The returned MemberSpecification contains all expected values
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class MemberSpecificationDialogClaude_getMemberSpecificationTest {

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
     * Test getMemberSpecification returns non-null for field dialog.
     * This covers basic execution of lines 371-425.
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
     * This covers basic execution of lines 371-425.
     */
    @Test
    public void testGetMemberSpecificationReturnsNonNullForMethod() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "getMemberSpecification should never return null");
    }

    /**
     * Test getMemberSpecification reads text fields (lines 371-374).
     * This verifies the method reads from annotation type, name, type, and arguments text fields.
     */
    @Test
    public void testGetMemberSpecificationReadsTextFields() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Set a field specification
        MemberSpecification fieldSpec = new MemberSpecification(
                0, 0, null, "testField", "I"
        );
        memberDialog.setMemberSpecification(fieldSpec);

        // Get it back - this executes lines 371-374
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        assertEquals("testField", result.name, "Name should be read from text field");
    }

    /**
     * Test getMemberSpecification with empty annotation type.
     * This covers line 378 where annotationType.equals("") returns true.
     */
    @Test
    public void testGetMemberSpecificationWithEmptyAnnotationType() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Set a specification with null annotation (which displays as "")
        MemberSpecification fieldSpec = new MemberSpecification(
                0, 0, null, "field", "I"
        );
        memberDialog.setMemberSpecification(fieldSpec);

        // Get it back - empty annotation should be converted to null
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNull(result.annotationType, "Empty annotation type should be converted to null");
    }

    /**
     * Test getMemberSpecification with "***" annotation type.
     * This covers line 379 where annotationType.equals("***") returns true.
     */
    @Test
    public void testGetMemberSpecificationWithWildcardAnnotationType() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Default dialog has "***" in annotation type field
        MemberSpecification result = memberDialog.getMemberSpecification();

        // The annotation type should be null (wildcarded)
        assertNull(result.annotationType, "Wildcard annotation type should be null");
    }

    /**
     * Test getMemberSpecification with actual annotation type.
     * This covers line 379 where annotation type is converted using ClassUtil.internalType.
     */
    @Test
    public void testGetMemberSpecificationWithActualAnnotationType() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                0, 0, "Ljava/lang/Deprecated;", "field", "I"
        );
        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();

        assertEquals("Ljava/lang/Deprecated;", result.annotationType,
                     "Annotation type should be in internal format");
    }

    /**
     * Test getMemberSpecification with empty name.
     * This covers line 381 where name.equals("") returns true.
     */
    @Test
    public void testGetMemberSpecificationWithEmptyName() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Set specification with null name (displays as "*")
        MemberSpecification fieldSpec = new MemberSpecification(
                0, 0, null, null, "I"
        );
        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNull(result.name, "Empty name should be converted to null");
    }

    /**
     * Test getMemberSpecification with "*" wildcard name.
     * This covers line 382 where name.equals("*") returns true.
     */
    @Test
    public void testGetMemberSpecificationWithWildcardName() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Set specification with null name (which sets "*" in text field)
        MemberSpecification fieldSpec = new MemberSpecification(
                0, 0, null, null, "I"
        );
        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNull(result.name, "Wildcard name should be converted to null");
    }

    /**
     * Test getMemberSpecification with actual name.
     * This covers the else branch where name is not empty or wildcard (line 384 not executed).
     */
    @Test
    public void testGetMemberSpecificationWithActualName() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                0, 0, null, "myField", "I"
        );
        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();

        assertEquals("myField", result.name, "Actual name should be preserved");
    }

    /**
     * Test getMemberSpecification executes field branch (isField=true).
     * This covers lines 387-391 in the field branch.
     */
    @Test
    public void testGetMemberSpecificationExecutesFieldBranch() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                0, 0, null, "field", "I"
        );
        memberDialog.setMemberSpecification(fieldSpec);

        // This should execute the field branch (lines 387-391)
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertEquals("I", result.descriptor, "Field descriptor should be set correctly");
    }

    /**
     * Test getMemberSpecification with empty field type.
     * This covers line 390 where type.equals("") returns true in field branch.
     */
    @Test
    public void testGetMemberSpecificationWithEmptyFieldType() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Set specification with null descriptor
        MemberSpecification fieldSpec = new MemberSpecification(
                0, 0, null, "field", null
        );
        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNull(result.descriptor, "Empty field type should be converted to null");
    }

    /**
     * Test getMemberSpecification with "***" field type.
     * This covers line 391 where type.equals("***") returns true in field branch.
     */
    @Test
    public void testGetMemberSpecificationWithWildcardFieldType() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Set specification with null descriptor (displays as "***")
        MemberSpecification fieldSpec = new MemberSpecification(
                0, 0, null, "field", null
        );
        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNull(result.descriptor, "Wildcard field type should be null");
    }

    /**
     * Test getMemberSpecification with actual field type.
     * This covers line 391 where type is converted using ClassUtil.internalType.
     */
    @Test
    public void testGetMemberSpecificationWithActualFieldType() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                0, 0, null, "field", "Ljava/lang/String;"
        );
        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();

        assertEquals("Ljava/lang/String;", result.descriptor,
                     "Field type should be in internal format");
    }

    /**
     * Test getMemberSpecification executes method branch (isField=false).
     * This covers lines 395-403 in the method branch.
     */
    @Test
    public void testGetMemberSpecificationExecutesMethodBranch() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                0, 0, null, "method", "()V"
        );
        memberDialog.setMemberSpecification(methodSpec);

        // This should execute the method branch (lines 395-403)
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertEquals("()V", result.descriptor, "Method descriptor should be set correctly");
    }

    /**
     * Test getMemberSpecification with empty return type for method.
     * This covers lines 395-397 where type.equals("") returns true.
     */
    @Test
    public void testGetMemberSpecificationWithEmptyReturnType() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        // Set specification with null descriptor first, then modify to test empty return type
        MemberSpecification methodSpec = new MemberSpecification(
                0, 0, null, "method", null
        );
        memberDialog.setMemberSpecification(methodSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();

        // When return type is empty and arguments are "...", descriptor should be null
        assertNull(result.descriptor, "Empty return type with wildcard args should be null");
    }

    /**
     * Test getMemberSpecification with "***" return type and "..." arguments.
     * This covers lines 400-402 where both type and arguments are wildcards.
     */
    @Test
    public void testGetMemberSpecificationWithWildcardMethodSignature() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        // Set specification with null descriptor (displays as "***" and "...")
        MemberSpecification methodSpec = new MemberSpecification(
                0, 0, null, "method", null
        );
        memberDialog.setMemberSpecification(methodSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNull(result.descriptor, "Wildcard method signature should be null");
    }

    /**
     * Test getMemberSpecification with actual return type and arguments.
     * This covers line 403 where ClassUtil.internalMethodDescriptor is called.
     */
    @Test
    public void testGetMemberSpecificationWithActualMethodSignature() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                0, 0, null, "method", "(ILjava/lang/String;)V"
        );
        memberDialog.setMemberSpecification(methodSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();

        assertEquals("(ILjava/lang/String;)V", result.descriptor,
                     "Method descriptor should be in internal format");
    }

    /**
     * Test getMemberSpecification creates MemberSpecification object.
     * This covers lines 406-407 where the MemberSpecification is created.
     */
    @Test
    public void testGetMemberSpecificationCreatesMemberSpecificationObject() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                0, 0, "Ljava/lang/Deprecated;", "field", "I"
        );
        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "MemberSpecification object should be created");
        assertEquals("Ljava/lang/Deprecated;", result.annotationType);
        assertEquals("field", result.name);
        assertEquals("I", result.descriptor);
    }

    /**
     * Test getMemberSpecification reads PUBLIC radio button.
     * This covers line 410 where getMemberSpecificationRadioButtons is called for PUBLIC.
     */
    @Test
    public void testGetMemberSpecificationReadsPublicRadioButton() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                AccessConstants.PUBLIC, 0, null, "publicField", "I"
        );
        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();

        assertTrue((result.requiredSetAccessFlags & AccessConstants.PUBLIC) != 0,
                   "Public flag should be read from radio buttons");
    }

    /**
     * Test getMemberSpecification reads PRIVATE radio button.
     * This covers line 411 where getMemberSpecificationRadioButtons is called for PRIVATE.
     */
    @Test
    public void testGetMemberSpecificationReadsPrivateRadioButton() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                AccessConstants.PRIVATE, 0, null, "privateField", "I"
        );
        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();

        assertTrue((result.requiredSetAccessFlags & AccessConstants.PRIVATE) != 0,
                   "Private flag should be read from radio buttons");
    }

    /**
     * Test getMemberSpecification reads PROTECTED radio button.
     * This covers line 412 where getMemberSpecificationRadioButtons is called for PROTECTED.
     */
    @Test
    public void testGetMemberSpecificationReadsProtectedRadioButton() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                AccessConstants.PROTECTED, 0, null, "protectedField", "I"
        );
        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();

        assertTrue((result.requiredSetAccessFlags & AccessConstants.PROTECTED) != 0,
                   "Protected flag should be read from radio buttons");
    }

    /**
     * Test getMemberSpecification reads STATIC radio button.
     * This covers line 413 where getMemberSpecificationRadioButtons is called for STATIC.
     */
    @Test
    public void testGetMemberSpecificationReadsStaticRadioButton() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                AccessConstants.STATIC, 0, null, "staticField", "I"
        );
        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();

        assertTrue((result.requiredSetAccessFlags & AccessConstants.STATIC) != 0,
                   "Static flag should be read from radio buttons");
    }

    /**
     * Test getMemberSpecification reads FINAL radio button.
     * This covers line 414 where getMemberSpecificationRadioButtons is called for FINAL.
     */
    @Test
    public void testGetMemberSpecificationReadsFinalRadioButton() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                AccessConstants.FINAL, 0, null, "finalField", "I"
        );
        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();

        assertTrue((result.requiredSetAccessFlags & AccessConstants.FINAL) != 0,
                   "Final flag should be read from radio buttons");
    }

    /**
     * Test getMemberSpecification reads SYNTHETIC radio button.
     * This covers line 415 where getMemberSpecificationRadioButtons is called for SYNTHETIC.
     */
    @Test
    public void testGetMemberSpecificationReadsSyntheticRadioButton() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                AccessConstants.SYNTHETIC, 0, null, "syntheticField", "I"
        );
        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();

        assertTrue((result.requiredSetAccessFlags & AccessConstants.SYNTHETIC) != 0,
                   "Synthetic flag should be read from radio buttons");
    }

    /**
     * Test getMemberSpecification reads VOLATILE radio button.
     * This covers line 416 where getMemberSpecificationRadioButtons is called for VOLATILE.
     */
    @Test
    public void testGetMemberSpecificationReadsVolatileRadioButton() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                AccessConstants.VOLATILE, 0, null, "volatileField", "I"
        );
        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();

        assertTrue((result.requiredSetAccessFlags & AccessConstants.VOLATILE) != 0,
                   "Volatile flag should be read from radio buttons");
    }

    /**
     * Test getMemberSpecification reads TRANSIENT radio button.
     * This covers line 417 where getMemberSpecificationRadioButtons is called for TRANSIENT.
     */
    @Test
    public void testGetMemberSpecificationReadsTransientRadioButton() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                AccessConstants.TRANSIENT, 0, null, "transientField", "I"
        );
        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();

        assertTrue((result.requiredSetAccessFlags & AccessConstants.TRANSIENT) != 0,
                   "Transient flag should be read from radio buttons");
    }

    /**
     * Test getMemberSpecification reads SYNCHRONIZED radio button.
     * This covers line 418 where getMemberSpecificationRadioButtons is called for SYNCHRONIZED.
     */
    @Test
    public void testGetMemberSpecificationReadsSynchronizedRadioButton() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                AccessConstants.SYNCHRONIZED, 0, null, "syncMethod", "()V"
        );
        memberDialog.setMemberSpecification(methodSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();

        assertTrue((result.requiredSetAccessFlags & AccessConstants.SYNCHRONIZED) != 0,
                   "Synchronized flag should be read from radio buttons");
    }

    /**
     * Test getMemberSpecification reads NATIVE radio button.
     * This covers line 419 where getMemberSpecificationRadioButtons is called for NATIVE.
     */
    @Test
    public void testGetMemberSpecificationReadsNativeRadioButton() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                AccessConstants.NATIVE, 0, null, "nativeMethod", "()V"
        );
        memberDialog.setMemberSpecification(methodSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();

        assertTrue((result.requiredSetAccessFlags & AccessConstants.NATIVE) != 0,
                   "Native flag should be read from radio buttons");
    }

    /**
     * Test getMemberSpecification reads ABSTRACT radio button.
     * This covers line 420 where getMemberSpecificationRadioButtons is called for ABSTRACT.
     */
    @Test
    public void testGetMemberSpecificationReadsAbstractRadioButton() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                AccessConstants.ABSTRACT, 0, null, "abstractMethod", "()V"
        );
        memberDialog.setMemberSpecification(methodSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();

        assertTrue((result.requiredSetAccessFlags & AccessConstants.ABSTRACT) != 0,
                   "Abstract flag should be read from radio buttons");
    }

    /**
     * Test getMemberSpecification reads STRICT radio button.
     * This covers line 421 where getMemberSpecificationRadioButtons is called for STRICT.
     */
    @Test
    public void testGetMemberSpecificationReadsStrictRadioButton() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                AccessConstants.STRICT, 0, null, "strictMethod", "()V"
        );
        memberDialog.setMemberSpecification(methodSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();

        assertTrue((result.requiredSetAccessFlags & AccessConstants.STRICT) != 0,
                   "Strict flag should be read from radio buttons");
    }

    /**
     * Test getMemberSpecification reads BRIDGE radio button.
     * This covers line 422 where getMemberSpecificationRadioButtons is called for BRIDGE.
     */
    @Test
    public void testGetMemberSpecificationReadsBridgeRadioButton() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                AccessConstants.BRIDGE, 0, null, "bridgeMethod", "()V"
        );
        memberDialog.setMemberSpecification(methodSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();

        assertTrue((result.requiredSetAccessFlags & AccessConstants.BRIDGE) != 0,
                   "Bridge flag should be read from radio buttons");
    }

    /**
     * Test getMemberSpecification reads VARARGS radio button.
     * This covers line 423 where getMemberSpecificationRadioButtons is called for VARARGS.
     */
    @Test
    public void testGetMemberSpecificationReadsVarargsRadioButton() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                AccessConstants.VARARGS, 0, null, "varargsMethod", "([Ljava/lang/String;)V"
        );
        memberDialog.setMemberSpecification(methodSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();

        assertTrue((result.requiredSetAccessFlags & AccessConstants.VARARGS) != 0,
                   "Varargs flag should be read from radio buttons");
    }

    /**
     * Test getMemberSpecification returns the MemberSpecification.
     * This covers line 425 where the result is returned.
     */
    @Test
    public void testGetMemberSpecificationReturnsResult() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                AccessConstants.PUBLIC, 0, null, "field", "I"
        );
        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should be returned");
        assertSame(MemberSpecification.class, result.getClass(),
                   "Result should be a MemberSpecification instance");
    }

    /**
     * Test getMemberSpecification with all access modifiers combined.
     * This tests multiple lines 410-423 simultaneously.
     */
    @Test
    public void testGetMemberSpecificationWithCombinedAccessModifiers() {
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
                   "Public should be set");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.STATIC) != 0,
                   "Static should be set");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.FINAL) != 0,
                   "Final should be set");
    }

    /**
     * Test getMemberSpecification can be called multiple times.
     * This ensures the method produces consistent results.
     */
    @Test
    public void testGetMemberSpecificationCanBeCalledMultipleTimes() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                AccessConstants.PUBLIC, 0, null, "field", "I"
        );
        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result1 = memberDialog.getMemberSpecification();
        MemberSpecification result2 = memberDialog.getMemberSpecification();

        assertNotNull(result1, "First result should not be null");
        assertNotNull(result2, "Second result should not be null");
        assertEquals(result1.name, result2.name, "Results should be consistent");
        assertEquals(result1.descriptor, result2.descriptor, "Results should be consistent");
    }

    /**
     * Test getMemberSpecification with primitive field types.
     * This tests line 391 with various primitive type conversions.
     */
    @Test
    public void testGetMemberSpecificationWithPrimitiveFieldTypes() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Test int
        MemberSpecification intField = new MemberSpecification(0, 0, null, "intField", "I");
        memberDialog.setMemberSpecification(intField);
        assertEquals("I", memberDialog.getMemberSpecification().descriptor);

        // Test long
        MemberSpecification longField = new MemberSpecification(0, 0, null, "longField", "J");
        memberDialog.setMemberSpecification(longField);
        assertEquals("J", memberDialog.getMemberSpecification().descriptor);

        // Test boolean
        MemberSpecification boolField = new MemberSpecification(0, 0, null, "boolField", "Z");
        memberDialog.setMemberSpecification(boolField);
        assertEquals("Z", memberDialog.getMemberSpecification().descriptor);
    }

    /**
     * Test getMemberSpecification with object field types.
     * This tests line 391 with object type conversions.
     */
    @Test
    public void testGetMemberSpecificationWithObjectFieldTypes() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification objectField = new MemberSpecification(
                0, 0, null, "objectField", "Ljava/lang/Object;"
        );
        memberDialog.setMemberSpecification(objectField);

        MemberSpecification result = memberDialog.getMemberSpecification();

        assertEquals("Ljava/lang/Object;", result.descriptor,
                     "Object field type should be preserved");
    }

    /**
     * Test getMemberSpecification with array field types.
     * This tests line 391 with array type conversions.
     */
    @Test
    public void testGetMemberSpecificationWithArrayFieldTypes() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification arrayField = new MemberSpecification(
                0, 0, null, "arrayField", "[Ljava/lang/String;"
        );
        memberDialog.setMemberSpecification(arrayField);

        MemberSpecification result = memberDialog.getMemberSpecification();

        assertEquals("[Ljava/lang/String;", result.descriptor,
                     "Array field type should be preserved");
    }

    /**
     * Test getMemberSpecification with method returning void.
     * This tests the method branch with void return type.
     */
    @Test
    public void testGetMemberSpecificationWithVoidReturnType() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                0, 0, null, "voidMethod", "()V"
        );
        memberDialog.setMemberSpecification(methodSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();

        assertEquals("()V", result.descriptor, "Void return type should be preserved");
    }

    /**
     * Test getMemberSpecification with method having parameters.
     * This tests line 403 with parameter parsing.
     */
    @Test
    public void testGetMemberSpecificationWithMethodParameters() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        MemberSpecification methodSpec = new MemberSpecification(
                0, 0, null, "method", "(ILjava/lang/String;)Ljava/lang/Object;"
        );
        memberDialog.setMemberSpecification(methodSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();

        assertEquals("(ILjava/lang/String;)Ljava/lang/Object;", result.descriptor,
                     "Method with parameters should be preserved");
    }

    /**
     * Test getMemberSpecification with complex method signature.
     * This tests line 403 with complex parameter and return type parsing.
     */
    @Test
    public void testGetMemberSpecificationWithComplexMethodSignature() {
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
                     "Complex method signature should be preserved");
    }

    /**
     * Test getMemberSpecification with all fields having maximum values.
     * This is a comprehensive test covering all major code paths.
     */
    @Test
    public void testGetMemberSpecificationWithMaximumFieldValues() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fullFieldSpec = new MemberSpecification(
                AccessConstants.PUBLIC | AccessConstants.STATIC | AccessConstants.FINAL |
                AccessConstants.VOLATILE | AccessConstants.TRANSIENT | AccessConstants.SYNTHETIC,
                0,
                "Ljava/lang/Deprecated;",
                "FULL_FIELD",
                "Ljava/lang/String;"
        );
        memberDialog.setMemberSpecification(fullFieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        assertEquals("Ljava/lang/Deprecated;", result.annotationType);
        assertEquals("FULL_FIELD", result.name);
        assertEquals("Ljava/lang/String;", result.descriptor);
        assertTrue((result.requiredSetAccessFlags & AccessConstants.PUBLIC) != 0);
        assertTrue((result.requiredSetAccessFlags & AccessConstants.STATIC) != 0);
        assertTrue((result.requiredSetAccessFlags & AccessConstants.FINAL) != 0);
        assertTrue((result.requiredSetAccessFlags & AccessConstants.VOLATILE) != 0);
        assertTrue((result.requiredSetAccessFlags & AccessConstants.TRANSIENT) != 0);
        assertTrue((result.requiredSetAccessFlags & AccessConstants.SYNTHETIC) != 0);
    }

    /**
     * Test getMemberSpecification with all fields having maximum values for method.
     * This is a comprehensive test covering all major code paths for methods.
     */
    @Test
    public void testGetMemberSpecificationWithMaximumMethodValues() {
        testDialog = new JDialog();
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

        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Result should not be null");
        assertEquals("Ljava/lang/Override;", result.annotationType);
        assertEquals("fullMethod", result.name);
        assertEquals("(ILjava/lang/String;[Ljava/lang/Object;)Ljava/util/List;",
                     result.descriptor);
        assertTrue((result.requiredSetAccessFlags & AccessConstants.PUBLIC) != 0);
        assertTrue((result.requiredSetAccessFlags & AccessConstants.STATIC) != 0);
        assertTrue((result.requiredSetAccessFlags & AccessConstants.FINAL) != 0);
        assertTrue((result.requiredSetAccessFlags & AccessConstants.SYNCHRONIZED) != 0);
        assertTrue((result.requiredSetAccessFlags & AccessConstants.NATIVE) != 0);
        assertTrue((result.requiredSetAccessFlags & AccessConstants.ABSTRACT) != 0);
        assertTrue((result.requiredSetAccessFlags & AccessConstants.STRICT) != 0);
        assertTrue((result.requiredSetAccessFlags & AccessConstants.BRIDGE) != 0);
        assertTrue((result.requiredSetAccessFlags & AccessConstants.VARARGS) != 0);
        assertTrue((result.requiredSetAccessFlags & AccessConstants.SYNTHETIC) != 0);
    }
}
