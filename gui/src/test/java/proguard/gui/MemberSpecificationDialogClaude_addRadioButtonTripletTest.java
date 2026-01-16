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
 * Test class for coverage of MemberSpecificationDialog.addRadioButtonTriplet private method.
 *
 * The addRadioButtonTriplet method is private and:
 * - Creates GridBagConstraints for label, buttons, and glue (lines 285-294)
 * - Creates three JRadioButton instances (lines 297-299)
 * - Creates a ButtonGroup and adds the radio buttons to it (lines 302-305)
 * - Adds a label, the three radio buttons, and glue to the panel (lines 308-312)
 * - Returns the three radio buttons in an array (lines 314-319)
 *
 * This method is called from the constructor for each access modifier:
 * - Always called for: Public, Private, Protected, Static, Final, Synthetic (6 times)
 * - For fields (isField=true): Also called for Volatile, Transient (2 more times = 8 total)
 * - For methods (isField=false): Also called for Synchronized, Native, Abstract, Strict, Bridge, Varargs (6 more times = 12 total)
 *
 * These tests cover the private method by:
 * - Creating MemberSpecificationDialog instances (which calls addRadioButtonTriplet)
 * - Using setMemberSpecification and getMemberSpecification to verify radio buttons work
 * - Testing that access flags are properly set and retrieved through the radio buttons
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class MemberSpecificationDialogClaude_addRadioButtonTripletTest {

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
     * Test that creating a field dialog calls addRadioButtonTriplet for all field-specific modifiers.
     * This covers lines 285-319 via the constructor calling addRadioButtonTriplet 8 times.
     */
    @Test
    public void testFieldDialogCreationCallsAddRadioButtonTriplet() {
        testDialog = new JDialog();
        // Creating a field dialog calls addRadioButtonTriplet for:
        // Public, Private, Protected, Static, Final, Synthetic, Volatile, Transient (8 times)
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        assertNotNull(memberDialog, "Field dialog should be created successfully");
        assertTrue(memberDialog.isModal(), "Dialog should be modal");
    }

    /**
     * Test that creating a method dialog calls addRadioButtonTriplet for all method-specific modifiers.
     * This covers lines 285-319 via the constructor calling addRadioButtonTriplet 12 times.
     */
    @Test
    public void testMethodDialogCreationCallsAddRadioButtonTriplet() {
        testDialog = new JDialog();
        // Creating a method dialog calls addRadioButtonTriplet for:
        // Public, Private, Protected, Static, Final, Synthetic, Synchronized, Native, Abstract, Strict, Bridge, Varargs (12 times)
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        assertNotNull(memberDialog, "Method dialog should be created successfully");
        assertTrue(memberDialog.isModal(), "Dialog should be modal");
    }

    /**
     * Test that Public radio buttons work correctly (verifies addRadioButtonTriplet created them).
     * This indirectly verifies that addRadioButtonTriplet was called and created functional radio buttons.
     */
    @Test
    public void testPublicRadioButtonsWorkCorrectly() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Set a public field specification
        MemberSpecification fieldSpec = new MemberSpecification(
                AccessConstants.PUBLIC, 0, null, "publicField", "I"
        );
        memberDialog.setMemberSpecification(fieldSpec);

        // Get it back and verify the radio buttons worked
        MemberSpecification result = memberDialog.getMemberSpecification();
        assertTrue((result.requiredSetAccessFlags & AccessConstants.PUBLIC) != 0,
                   "Public flag should be set, indicating radio buttons work");
    }

    /**
     * Test that Private radio buttons work correctly.
     * This verifies addRadioButtonTriplet created functional radio buttons for Private.
     */
    @Test
    public void testPrivateRadioButtonsWorkCorrectly() {
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
     * Test that Protected radio buttons work correctly.
     * This verifies addRadioButtonTriplet created functional radio buttons for Protected.
     */
    @Test
    public void testProtectedRadioButtonsWorkCorrectly() {
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
     * Test that Static radio buttons work correctly.
     * This verifies addRadioButtonTriplet created functional radio buttons for Static.
     */
    @Test
    public void testStaticRadioButtonsWorkCorrectly() {
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
     * Test that Final radio buttons work correctly.
     * This verifies addRadioButtonTriplet created functional radio buttons for Final.
     */
    @Test
    public void testFinalRadioButtonsWorkCorrectly() {
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
     * Test that Synthetic radio buttons work correctly.
     * This verifies addRadioButtonTriplet created functional radio buttons for Synthetic.
     */
    @Test
    public void testSyntheticRadioButtonsWorkCorrectly() {
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
     * Test that Volatile radio buttons work correctly (field-specific).
     * This verifies addRadioButtonTriplet created functional radio buttons for Volatile.
     */
    @Test
    public void testVolatileRadioButtonsWorkCorrectly() {
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
     * Test that Transient radio buttons work correctly (field-specific).
     * This verifies addRadioButtonTriplet created functional radio buttons for Transient.
     */
    @Test
    public void testTransientRadioButtonsWorkCorrectly() {
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
     * Test that Synchronized radio buttons work correctly (method-specific).
     * This verifies addRadioButtonTriplet created functional radio buttons for Synchronized.
     */
    @Test
    public void testSynchronizedRadioButtonsWorkCorrectly() {
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
     * Test that Native radio buttons work correctly (method-specific).
     * This verifies addRadioButtonTriplet created functional radio buttons for Native.
     */
    @Test
    public void testNativeRadioButtonsWorkCorrectly() {
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
     * Test that Abstract radio buttons work correctly (method-specific).
     * This verifies addRadioButtonTriplet created functional radio buttons for Abstract.
     */
    @Test
    public void testAbstractRadioButtonsWorkCorrectly() {
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
     * Test that Strict radio buttons work correctly (method-specific).
     * This verifies addRadioButtonTriplet created functional radio buttons for Strict.
     */
    @Test
    public void testStrictRadioButtonsWorkCorrectly() {
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
     * Test that Bridge radio buttons work correctly (method-specific).
     * This verifies addRadioButtonTriplet created functional radio buttons for Bridge.
     */
    @Test
    public void testBridgeRadioButtonsWorkCorrectly() {
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
     * Test that Varargs radio buttons work correctly (method-specific).
     * This verifies addRadioButtonTriplet created functional radio buttons for Varargs.
     */
    @Test
    public void testVarargsRadioButtonsWorkCorrectly() {
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
     * Test that radio buttons for "not set" (unset flags) work correctly.
     * This verifies the second radio button in each triplet works.
     */
    @Test
    public void testUnsetFlagRadioButtonsWork() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Set a specification where PUBLIC is required to be unset
        MemberSpecification fieldSpec = new MemberSpecification(
                0, AccessConstants.PUBLIC, null, "nonPublicField", "I"
        );
        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();
        assertTrue((result.requiredUnsetAccessFlags & AccessConstants.PUBLIC) != 0,
                   "Public flag should be in unset flags, indicating second radio button works");
    }

    /**
     * Test that radio buttons can be set to "don't care" state.
     * This verifies the third radio button in each triplet works.
     */
    @Test
    public void testDontCareRadioButtonsWork() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Set a specification with no access flags (don't care state)
        MemberSpecification fieldSpec = new MemberSpecification(
                0, 0, null, "anyField", "I"
        );
        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();
        // When flags are 0, the "don't care" radio button should be selected
        assertEquals(0, result.requiredSetAccessFlags & AccessConstants.PUBLIC,
                     "Public should be in don't care state");
        assertEquals(0, result.requiredUnsetAccessFlags & AccessConstants.PUBLIC,
                     "Public should not be in unset flags");
    }

    /**
     * Test multiple radio button triplets work simultaneously in field dialog.
     * This verifies all triplets created by addRadioButtonTriplet work independently.
     */
    @Test
    public void testMultipleRadioButtonTripletsWorkSimultaneouslyForField() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Set multiple flags at once
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
                   "Public triplet should work");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.STATIC) != 0,
                   "Static triplet should work");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.FINAL) != 0,
                   "Final triplet should work");
    }

    /**
     * Test multiple radio button triplets work simultaneously in method dialog.
     * This verifies all triplets created by addRadioButtonTriplet work independently.
     */
    @Test
    public void testMultipleRadioButtonTripletsWorkSimultaneouslyForMethod() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        // Set multiple flags at once
        MemberSpecification methodSpec = new MemberSpecification(
                AccessConstants.PUBLIC | AccessConstants.SYNCHRONIZED | AccessConstants.FINAL,
                0,
                null,
                "syncMethod",
                "()V"
        );
        memberDialog.setMemberSpecification(methodSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();
        assertTrue((result.requiredSetAccessFlags & AccessConstants.PUBLIC) != 0,
                   "Public triplet should work");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.SYNCHRONIZED) != 0,
                   "Synchronized triplet should work");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.FINAL) != 0,
                   "Final triplet should work");
    }

    /**
     * Test that creating multiple field dialogs calls addRadioButtonTriplet correctly each time.
     * This ensures the method works correctly on repeated calls.
     */
    @Test
    public void testMultipleFieldDialogCreationsCallAddRadioButtonTriplet() {
        testDialog = new JDialog();

        // Create first field dialog
        MemberSpecificationDialog dialog1 = new MemberSpecificationDialog(testDialog, true);
        assertNotNull(dialog1, "First dialog should be created");
        dialog1.dispose();

        // Create second field dialog
        MemberSpecificationDialog dialog2 = new MemberSpecificationDialog(testDialog, true);
        assertNotNull(dialog2, "Second dialog should be created");
        dialog2.dispose();

        // Create third field dialog
        memberDialog = new MemberSpecificationDialog(testDialog, true);
        assertNotNull(memberDialog, "Third dialog should be created");
    }

    /**
     * Test that creating multiple method dialogs calls addRadioButtonTriplet correctly each time.
     * This ensures the method works correctly on repeated calls.
     */
    @Test
    public void testMultipleMethodDialogCreationsCallAddRadioButtonTriplet() {
        testDialog = new JDialog();

        // Create first method dialog
        MemberSpecificationDialog dialog1 = new MemberSpecificationDialog(testDialog, false);
        assertNotNull(dialog1, "First dialog should be created");
        dialog1.dispose();

        // Create second method dialog
        MemberSpecificationDialog dialog2 = new MemberSpecificationDialog(testDialog, false);
        assertNotNull(dialog2, "Second dialog should be created");
        dialog2.dispose();

        // Create third method dialog
        memberDialog = new MemberSpecificationDialog(testDialog, false);
        assertNotNull(memberDialog, "Third dialog should be created");
    }

    /**
     * Test field dialog with all field-specific access modifiers set.
     * This exercises all radio button triplets created for field dialogs.
     */
    @Test
    public void testFieldDialogWithAllFieldModifiers() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Set all possible field modifiers
        MemberSpecification fieldSpec = new MemberSpecification(
                AccessConstants.PUBLIC | AccessConstants.STATIC | AccessConstants.FINAL |
                AccessConstants.VOLATILE | AccessConstants.TRANSIENT | AccessConstants.SYNTHETIC,
                0,
                null,
                "complexField",
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
        assertTrue((result.requiredSetAccessFlags & AccessConstants.VOLATILE) != 0,
                   "Volatile should be set");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.TRANSIENT) != 0,
                   "Transient should be set");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.SYNTHETIC) != 0,
                   "Synthetic should be set");
    }

    /**
     * Test method dialog with all method-specific access modifiers set.
     * This exercises all radio button triplets created for method dialogs.
     */
    @Test
    public void testMethodDialogWithAllMethodModifiers() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        // Set all possible method modifiers
        MemberSpecification methodSpec = new MemberSpecification(
                AccessConstants.PUBLIC | AccessConstants.STATIC | AccessConstants.FINAL |
                AccessConstants.SYNCHRONIZED | AccessConstants.NATIVE | AccessConstants.ABSTRACT |
                AccessConstants.STRICT | AccessConstants.BRIDGE | AccessConstants.VARARGS |
                AccessConstants.SYNTHETIC,
                0,
                null,
                "complexMethod",
                "()V"
        );
        memberDialog.setMemberSpecification(methodSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();
        assertTrue((result.requiredSetAccessFlags & AccessConstants.PUBLIC) != 0,
                   "Public should be set");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.STATIC) != 0,
                   "Static should be set");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.FINAL) != 0,
                   "Final should be set");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.SYNCHRONIZED) != 0,
                   "Synchronized should be set");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.NATIVE) != 0,
                   "Native should be set");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.ABSTRACT) != 0,
                   "Abstract should be set");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.STRICT) != 0,
                   "Strict should be set");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.BRIDGE) != 0,
                   "Bridge should be set");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.VARARGS) != 0,
                   "Varargs should be set");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.SYNTHETIC) != 0,
                   "Synthetic should be set");
    }

    /**
     * Test that alternating between field and method dialogs calls addRadioButtonTriplet correctly.
     * This ensures the method works correctly when called with different configurations.
     */
    @Test
    public void testAlternatingFieldAndMethodDialogsCallAddRadioButtonTriplet() {
        testDialog = new JDialog();

        // Create field dialog (8 calls to addRadioButtonTriplet)
        MemberSpecificationDialog fieldDialog = new MemberSpecificationDialog(testDialog, true);
        assertNotNull(fieldDialog, "Field dialog should be created");
        fieldDialog.dispose();

        // Create method dialog (12 calls to addRadioButtonTriplet)
        MemberSpecificationDialog methodDialog = new MemberSpecificationDialog(testDialog, false);
        assertNotNull(methodDialog, "Method dialog should be created");
        methodDialog.dispose();

        // Create another field dialog (8 calls to addRadioButtonTriplet)
        memberDialog = new MemberSpecificationDialog(testDialog, true);
        assertNotNull(memberDialog, "Another field dialog should be created");
    }

    /**
     * Test that radio button state persists correctly after setting and getting.
     * This verifies that the ButtonGroup created in addRadioButtonTriplet works correctly.
     */
    @Test
    public void testRadioButtonStatePersistence() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Set public field
        MemberSpecification fieldSpec1 = new MemberSpecification(
                AccessConstants.PUBLIC, 0, null, "field1", "I"
        );
        memberDialog.setMemberSpecification(fieldSpec1);
        MemberSpecification result1 = memberDialog.getMemberSpecification();
        assertTrue((result1.requiredSetAccessFlags & AccessConstants.PUBLIC) != 0,
                   "First setting should work");

        // Change to private field
        MemberSpecification fieldSpec2 = new MemberSpecification(
                AccessConstants.PRIVATE, 0, null, "field2", "I"
        );
        memberDialog.setMemberSpecification(fieldSpec2);
        MemberSpecification result2 = memberDialog.getMemberSpecification();
        assertTrue((result2.requiredSetAccessFlags & AccessConstants.PRIVATE) != 0,
                   "Second setting should work");
        assertEquals(0, result2.requiredSetAccessFlags & AccessConstants.PUBLIC,
                     "Previous setting should be cleared");
    }

    /**
     * Test radio buttons with mixed set and unset flags.
     * This verifies all three radio buttons in triplets work (required, not required, don't care).
     */
    @Test
    public void testRadioButtonsWithMixedSetAndUnsetFlags() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Set some flags as required, some as required unset, and leave others as don't care
        MemberSpecification fieldSpec = new MemberSpecification(
                AccessConstants.PUBLIC | AccessConstants.FINAL,  // Required to be set
                AccessConstants.STATIC,                           // Required to be unset
                null,
                "mixedField",
                "I"
        );
        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();
        assertTrue((result.requiredSetAccessFlags & AccessConstants.PUBLIC) != 0,
                   "Public should be required");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.FINAL) != 0,
                   "Final should be required");
        assertTrue((result.requiredUnsetAccessFlags & AccessConstants.STATIC) != 0,
                   "Static should be required unset");
        // Other flags should be in don't care state (not in either set)
    }

    /**
     * Test that all radio button triplets are functionally independent.
     * This verifies that ButtonGroups created in addRadioButtonTriplet don't interfere with each other.
     */
    @Test
    public void testRadioButtonTripletsAreIndependent() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Set different states for different triplets
        MemberSpecification fieldSpec = new MemberSpecification(
                AccessConstants.PUBLIC,      // Public: required set (button 0)
                AccessConstants.PRIVATE,     // Private: required unset (button 1)
                null,                        // Protected: don't care (button 2)
                "independentField",
                "I"
        );
        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();
        assertTrue((result.requiredSetAccessFlags & AccessConstants.PUBLIC) != 0,
                   "Public triplet should be in state 0 (required)");
        assertTrue((result.requiredUnsetAccessFlags & AccessConstants.PRIVATE) != 0,
                   "Private triplet should be in state 1 (not required)");
        assertEquals(0, result.requiredSetAccessFlags & AccessConstants.PROTECTED,
                     "Protected triplet should be in state 2 (don't care)");
        assertEquals(0, result.requiredUnsetAccessFlags & AccessConstants.PROTECTED,
                     "Protected triplet should be in state 2 (don't care)");
    }
}
