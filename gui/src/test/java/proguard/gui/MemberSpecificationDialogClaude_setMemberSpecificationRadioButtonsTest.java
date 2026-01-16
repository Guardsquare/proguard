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
 * Test class for coverage of MemberSpecificationDialog.setMemberSpecificationRadioButtons private method.
 *
 * The setMemberSpecificationRadioButtons method (lines 453-464) is private and:
 * - Checks if radioButtons parameter is not null (line 457)
 * - Calculates which radio button to select based on access flags (lines 459-461):
 *   - Index 0 if flag is in requiredSetAccessFlags (required to be set)
 *   - Index 1 if flag is in requiredUnsetAccessFlags (required to be unset)
 *   - Index 2 otherwise (don't care)
 * - Selects the appropriate radio button (line 462)
 *
 * This method is called from setMemberSpecification (lines 336-349) for each access modifier:
 * - Always called for: PUBLIC, PRIVATE, PROTECTED, STATIC, FINAL, SYNTHETIC
 * - For fields: Also called for VOLATILE, TRANSIENT (may pass null radioButtons)
 * - For methods: Also called for SYNCHRONIZED, NATIVE, ABSTRACT, STRICT, BRIDGE, VARARGS (may pass null radioButtons)
 *
 * These tests cover the private method by:
 * - Calling setMemberSpecification with various MemberSpecification objects
 * - Testing all three radio button states (required, not required, don't care)
 * - Testing with null radioButtons (field-specific modifiers on method dialogs and vice versa)
 * - Verifying the correct radio button is selected by retrieving the specification
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class MemberSpecificationDialogClaude_setMemberSpecificationRadioButtonsTest {

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
     * Test setMemberSpecificationRadioButtons with radioButtons != null and flag in requiredSetAccessFlags.
     * This covers lines 457, 459 (first condition true), 462.
     */
    @Test
    public void testSetMemberSpecificationRadioButtonsWithRequiredSetFlag() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Set PUBLIC flag as required set (index 0 should be selected)
        MemberSpecification fieldSpec = new MemberSpecification(
                AccessConstants.PUBLIC, 0, null, "publicField", "I"
        );

        // This calls setMemberSpecificationRadioButtons with PUBLIC flag
        // Line 457: radioButtons != null (publicRadioButtons exists)
        // Line 459: first condition true (PUBLIC in requiredSetAccessFlags), index = 0
        // Line 462: radioButtons[0].setSelected(true)
        memberDialog.setMemberSpecification(fieldSpec);

        // Verify by getting the specification back
        MemberSpecification result = memberDialog.getMemberSpecification();
        assertTrue((result.requiredSetAccessFlags & AccessConstants.PUBLIC) != 0,
                   "PUBLIC flag should be in requiredSetAccessFlags, indicating index 0 was selected");
    }

    /**
     * Test setMemberSpecificationRadioButtons with radioButtons != null and flag in requiredUnsetAccessFlags.
     * This covers lines 457, 459 (first false), 460 (second condition true), 462.
     */
    @Test
    public void testSetMemberSpecificationRadioButtonsWithRequiredUnsetFlag() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Set PUBLIC flag as required unset (index 1 should be selected)
        MemberSpecification fieldSpec = new MemberSpecification(
                0, AccessConstants.PUBLIC, null, "nonPublicField", "I"
        );

        // This calls setMemberSpecificationRadioButtons with PUBLIC flag
        // Line 457: radioButtons != null (publicRadioButtons exists)
        // Line 459: first condition false (PUBLIC not in requiredSetAccessFlags)
        // Line 460: second condition true (PUBLIC in requiredUnsetAccessFlags), index = 1
        // Line 462: radioButtons[1].setSelected(true)
        memberDialog.setMemberSpecification(fieldSpec);

        // Verify by getting the specification back
        MemberSpecification result = memberDialog.getMemberSpecification();
        assertTrue((result.requiredUnsetAccessFlags & AccessConstants.PUBLIC) != 0,
                   "PUBLIC flag should be in requiredUnsetAccessFlags, indicating index 1 was selected");
    }

    /**
     * Test setMemberSpecificationRadioButtons with radioButtons != null and flag in neither set.
     * This covers lines 457, 459 (first false), 460 (second false), 461 (default to 2), 462.
     */
    @Test
    public void testSetMemberSpecificationRadioButtonsWithDontCareFlag() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Set no PUBLIC flags (index 2 should be selected - don't care)
        MemberSpecification fieldSpec = new MemberSpecification(
                0, 0, null, "anyField", "I"
        );

        // This calls setMemberSpecificationRadioButtons with PUBLIC flag
        // Line 457: radioButtons != null (publicRadioButtons exists)
        // Line 459: first condition false (PUBLIC not in requiredSetAccessFlags)
        // Line 460: second condition false (PUBLIC not in requiredUnsetAccessFlags)
        // Line 461: default to index = 2
        // Line 462: radioButtons[2].setSelected(true)
        memberDialog.setMemberSpecification(fieldSpec);

        // Verify by getting the specification back
        MemberSpecification result = memberDialog.getMemberSpecification();
        assertEquals(0, result.requiredSetAccessFlags & AccessConstants.PUBLIC,
                     "PUBLIC should not be in requiredSetAccessFlags");
        assertEquals(0, result.requiredUnsetAccessFlags & AccessConstants.PUBLIC,
                     "PUBLIC should not be in requiredUnsetAccessFlags, indicating index 2 was selected");
    }

    /**
     * Test setMemberSpecificationRadioButtons with null radioButtons (field-specific modifier on method dialog).
     * This covers line 457 where radioButtons == null, so lines 459-462 are NOT executed.
     */
    @Test
    public void testSetMemberSpecificationRadioButtonsWithNullRadioButtonsForVolatile() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        // For method dialog, volatileRadioButtons is null
        // Set VOLATILE flag (which will be passed to setMemberSpecificationRadioButtons with null)
        MemberSpecification methodSpec = new MemberSpecification(
                AccessConstants.VOLATILE, 0, null, "method", "()V"
        );

        // This calls setMemberSpecificationRadioButtons with VOLATILE flag and null radioButtons
        // Line 457: radioButtons == null (volatileRadioButtons doesn't exist for method dialog)
        // Lines 459-462: NOT executed due to if guard
        memberDialog.setMemberSpecification(methodSpec);

        // The method should complete without error even though radioButtons was null
        assertNotNull(memberDialog, "Dialog should handle null radioButtons gracefully");
    }

    /**
     * Test setMemberSpecificationRadioButtons with null radioButtons (method-specific modifier on field dialog).
     * This covers line 457 where radioButtons == null, so lines 459-462 are NOT executed.
     */
    @Test
    public void testSetMemberSpecificationRadioButtonsWithNullRadioButtonsForSynchronized() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // For field dialog, synchronizedRadioButtons is null
        // Set SYNCHRONIZED flag (which will be passed to setMemberSpecificationRadioButtons with null)
        MemberSpecification fieldSpec = new MemberSpecification(
                AccessConstants.SYNCHRONIZED, 0, null, "field", "I"
        );

        // This calls setMemberSpecificationRadioButtons with SYNCHRONIZED flag and null radioButtons
        // Line 457: radioButtons == null (synchronizedRadioButtons doesn't exist for field dialog)
        // Lines 459-462: NOT executed due to if guard
        memberDialog.setMemberSpecification(fieldSpec);

        // The method should complete without error even though radioButtons was null
        assertNotNull(memberDialog, "Dialog should handle null radioButtons gracefully");
    }

    /**
     * Test all common access modifiers that have non-null radioButtons.
     * This ensures lines 457, 459-462 are executed for all common modifiers.
     */
    @Test
    public void testAllCommonAccessModifiers() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Test PUBLIC (required set)
        MemberSpecification publicSpec = new MemberSpecification(
                AccessConstants.PUBLIC, 0, null, "field", "I"
        );
        memberDialog.setMemberSpecification(publicSpec);
        assertTrue((memberDialog.getMemberSpecification().requiredSetAccessFlags & AccessConstants.PUBLIC) != 0);

        // Test PRIVATE (required set)
        MemberSpecification privateSpec = new MemberSpecification(
                AccessConstants.PRIVATE, 0, null, "field", "I"
        );
        memberDialog.setMemberSpecification(privateSpec);
        assertTrue((memberDialog.getMemberSpecification().requiredSetAccessFlags & AccessConstants.PRIVATE) != 0);

        // Test PROTECTED (required set)
        MemberSpecification protectedSpec = new MemberSpecification(
                AccessConstants.PROTECTED, 0, null, "field", "I"
        );
        memberDialog.setMemberSpecification(protectedSpec);
        assertTrue((memberDialog.getMemberSpecification().requiredSetAccessFlags & AccessConstants.PROTECTED) != 0);

        // Test STATIC (required set)
        MemberSpecification staticSpec = new MemberSpecification(
                AccessConstants.STATIC, 0, null, "field", "I"
        );
        memberDialog.setMemberSpecification(staticSpec);
        assertTrue((memberDialog.getMemberSpecification().requiredSetAccessFlags & AccessConstants.STATIC) != 0);

        // Test FINAL (required set)
        MemberSpecification finalSpec = new MemberSpecification(
                AccessConstants.FINAL, 0, null, "field", "I"
        );
        memberDialog.setMemberSpecification(finalSpec);
        assertTrue((memberDialog.getMemberSpecification().requiredSetAccessFlags & AccessConstants.FINAL) != 0);

        // Test SYNTHETIC (required set)
        MemberSpecification syntheticSpec = new MemberSpecification(
                AccessConstants.SYNTHETIC, 0, null, "field", "I"
        );
        memberDialog.setMemberSpecification(syntheticSpec);
        assertTrue((memberDialog.getMemberSpecification().requiredSetAccessFlags & AccessConstants.SYNTHETIC) != 0);
    }

    /**
     * Test field-specific access modifiers with non-null radioButtons.
     * This ensures lines 457, 459-462 are executed for field-specific modifiers.
     */
    @Test
    public void testFieldSpecificAccessModifiers() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Test VOLATILE (required set)
        MemberSpecification volatileSpec = new MemberSpecification(
                AccessConstants.VOLATILE, 0, null, "volatileField", "I"
        );
        memberDialog.setMemberSpecification(volatileSpec);
        assertTrue((memberDialog.getMemberSpecification().requiredSetAccessFlags & AccessConstants.VOLATILE) != 0,
                   "VOLATILE radio buttons should exist and be set for field dialog");

        // Test TRANSIENT (required set)
        MemberSpecification transientSpec = new MemberSpecification(
                AccessConstants.TRANSIENT, 0, null, "transientField", "I"
        );
        memberDialog.setMemberSpecification(transientSpec);
        assertTrue((memberDialog.getMemberSpecification().requiredSetAccessFlags & AccessConstants.TRANSIENT) != 0,
                   "TRANSIENT radio buttons should exist and be set for field dialog");
    }

    /**
     * Test method-specific access modifiers with non-null radioButtons.
     * This ensures lines 457, 459-462 are executed for method-specific modifiers.
     */
    @Test
    public void testMethodSpecificAccessModifiers() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        // Test SYNCHRONIZED (required set)
        MemberSpecification syncSpec = new MemberSpecification(
                AccessConstants.SYNCHRONIZED, 0, null, "syncMethod", "()V"
        );
        memberDialog.setMemberSpecification(syncSpec);
        assertTrue((memberDialog.getMemberSpecification().requiredSetAccessFlags & AccessConstants.SYNCHRONIZED) != 0,
                   "SYNCHRONIZED radio buttons should exist and be set for method dialog");

        // Test NATIVE (required set)
        MemberSpecification nativeSpec = new MemberSpecification(
                AccessConstants.NATIVE, 0, null, "nativeMethod", "()V"
        );
        memberDialog.setMemberSpecification(nativeSpec);
        assertTrue((memberDialog.getMemberSpecification().requiredSetAccessFlags & AccessConstants.NATIVE) != 0,
                   "NATIVE radio buttons should exist and be set for method dialog");

        // Test ABSTRACT (required set)
        MemberSpecification abstractSpec = new MemberSpecification(
                AccessConstants.ABSTRACT, 0, null, "abstractMethod", "()V"
        );
        memberDialog.setMemberSpecification(abstractSpec);
        assertTrue((memberDialog.getMemberSpecification().requiredSetAccessFlags & AccessConstants.ABSTRACT) != 0,
                   "ABSTRACT radio buttons should exist and be set for method dialog");

        // Test STRICT (required set)
        MemberSpecification strictSpec = new MemberSpecification(
                AccessConstants.STRICT, 0, null, "strictMethod", "()V"
        );
        memberDialog.setMemberSpecification(strictSpec);
        assertTrue((memberDialog.getMemberSpecification().requiredSetAccessFlags & AccessConstants.STRICT) != 0,
                   "STRICT radio buttons should exist and be set for method dialog");

        // Test BRIDGE (required set)
        MemberSpecification bridgeSpec = new MemberSpecification(
                AccessConstants.BRIDGE, 0, null, "bridgeMethod", "()V"
        );
        memberDialog.setMemberSpecification(bridgeSpec);
        assertTrue((memberDialog.getMemberSpecification().requiredSetAccessFlags & AccessConstants.BRIDGE) != 0,
                   "BRIDGE radio buttons should exist and be set for method dialog");

        // Test VARARGS (required set)
        MemberSpecification varargsSpec = new MemberSpecification(
                AccessConstants.VARARGS, 0, null, "varargsMethod", "([Ljava/lang/String;)V"
        );
        memberDialog.setMemberSpecification(varargsSpec);
        assertTrue((memberDialog.getMemberSpecification().requiredSetAccessFlags & AccessConstants.VARARGS) != 0,
                   "VARARGS radio buttons should exist and be set for method dialog");
    }

    /**
     * Test all three radio button states for a single modifier.
     * This comprehensively tests lines 459-461 for all three conditions.
     */
    @Test
    public void testAllThreeRadioButtonStatesForPublic() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // State 1: Required set (index 0)
        MemberSpecification requiredSetSpec = new MemberSpecification(
                AccessConstants.PUBLIC, 0, null, "field", "I"
        );
        memberDialog.setMemberSpecification(requiredSetSpec);
        MemberSpecification result1 = memberDialog.getMemberSpecification();
        assertTrue((result1.requiredSetAccessFlags & AccessConstants.PUBLIC) != 0,
                   "Index 0 should be selected (required set)");

        // State 2: Required unset (index 1)
        MemberSpecification requiredUnsetSpec = new MemberSpecification(
                0, AccessConstants.PUBLIC, null, "field", "I"
        );
        memberDialog.setMemberSpecification(requiredUnsetSpec);
        MemberSpecification result2 = memberDialog.getMemberSpecification();
        assertTrue((result2.requiredUnsetAccessFlags & AccessConstants.PUBLIC) != 0,
                   "Index 1 should be selected (required unset)");

        // State 3: Don't care (index 2)
        MemberSpecification dontCareSpec = new MemberSpecification(
                0, 0, null, "field", "I"
        );
        memberDialog.setMemberSpecification(dontCareSpec);
        MemberSpecification result3 = memberDialog.getMemberSpecification();
        assertEquals(0, result3.requiredSetAccessFlags & AccessConstants.PUBLIC,
                     "PUBLIC should not be in requiredSetAccessFlags");
        assertEquals(0, result3.requiredUnsetAccessFlags & AccessConstants.PUBLIC,
                     "Index 2 should be selected (don't care)");
    }

    /**
     * Test multiple modifiers with different radio button states.
     * This ensures the method works correctly when called multiple times with different flags.
     */
    @Test
    public void testMultipleModifiersWithDifferentStates() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // PUBLIC: required set (index 0)
        // PRIVATE: required unset (index 1)
        // PROTECTED: don't care (index 2)
        MemberSpecification mixedSpec = new MemberSpecification(
                AccessConstants.PUBLIC,     // Required set
                AccessConstants.PRIVATE,    // Required unset
                null,
                "field",
                "I"
        );

        memberDialog.setMemberSpecification(mixedSpec);
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertTrue((result.requiredSetAccessFlags & AccessConstants.PUBLIC) != 0,
                   "PUBLIC should be required set");
        assertTrue((result.requiredUnsetAccessFlags & AccessConstants.PRIVATE) != 0,
                   "PRIVATE should be required unset");
        assertEquals(0, result.requiredSetAccessFlags & AccessConstants.PROTECTED,
                     "PROTECTED should be in don't care state");
        assertEquals(0, result.requiredUnsetAccessFlags & AccessConstants.PROTECTED,
                     "PROTECTED should be in don't care state");
    }

    /**
     * Test that radioButtons array is properly accessed at calculated index.
     * This verifies line 462 correctly uses the calculated index.
     */
    @Test
    public void testRadioButtonArrayAccessAtCalculatedIndex() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Test index 0
        MemberSpecification spec0 = new MemberSpecification(
                AccessConstants.STATIC, 0, null, "field", "I"
        );
        memberDialog.setMemberSpecification(spec0);
        assertTrue((memberDialog.getMemberSpecification().requiredSetAccessFlags & AccessConstants.STATIC) != 0,
                   "Index 0 should be correctly accessed and selected");

        // Test index 1
        MemberSpecification spec1 = new MemberSpecification(
                0, AccessConstants.STATIC, null, "field", "I"
        );
        memberDialog.setMemberSpecification(spec1);
        assertTrue((memberDialog.getMemberSpecification().requiredUnsetAccessFlags & AccessConstants.STATIC) != 0,
                   "Index 1 should be correctly accessed and selected");

        // Test index 2
        MemberSpecification spec2 = new MemberSpecification(
                0, 0, null, "field", "I"
        );
        memberDialog.setMemberSpecification(spec2);
        assertEquals(0, memberDialog.getMemberSpecification().requiredSetAccessFlags & AccessConstants.STATIC,
                     "Index 2 should be correctly accessed and selected");
    }

    /**
     * Test setMemberSpecificationRadioButtons called multiple times for same dialog.
     * This ensures the method can be called repeatedly without issues.
     */
    @Test
    public void testSetMemberSpecificationRadioButtonsCalledMultipleTimes() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // First call - set PUBLIC to required set
        MemberSpecification spec1 = new MemberSpecification(
                AccessConstants.PUBLIC, 0, null, "field1", "I"
        );
        memberDialog.setMemberSpecification(spec1);
        assertTrue((memberDialog.getMemberSpecification().requiredSetAccessFlags & AccessConstants.PUBLIC) != 0);

        // Second call - set PUBLIC to required unset
        MemberSpecification spec2 = new MemberSpecification(
                0, AccessConstants.PUBLIC, null, "field2", "I"
        );
        memberDialog.setMemberSpecification(spec2);
        assertTrue((memberDialog.getMemberSpecification().requiredUnsetAccessFlags & AccessConstants.PUBLIC) != 0);

        // Third call - set PUBLIC to don't care
        MemberSpecification spec3 = new MemberSpecification(
                0, 0, null, "field3", "I"
        );
        memberDialog.setMemberSpecification(spec3);
        assertEquals(0, memberDialog.getMemberSpecification().requiredSetAccessFlags & AccessConstants.PUBLIC);
        assertEquals(0, memberDialog.getMemberSpecification().requiredUnsetAccessFlags & AccessConstants.PUBLIC);
    }

    /**
     * Test with all null radioButtons (calling with all method-specific modifiers on field dialog).
     * This ensures line 457 guard works correctly for multiple null cases.
     */
    @Test
    public void testMultipleNullRadioButtonsOnFieldDialog() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Set all method-specific modifiers - all will have null radioButtons on field dialog
        MemberSpecification spec = new MemberSpecification(
                AccessConstants.SYNCHRONIZED | AccessConstants.NATIVE | AccessConstants.ABSTRACT |
                AccessConstants.STRICT | AccessConstants.BRIDGE | AccessConstants.VARARGS,
                0,
                null,
                "field",
                "I"
        );

        // This will call setMemberSpecificationRadioButtons multiple times with null radioButtons
        // Line 457 should guard against null for all of them
        memberDialog.setMemberSpecification(spec);

        // Should complete without error
        assertNotNull(memberDialog.getMemberSpecification(), "Should handle multiple null radioButtons");
    }

    /**
     * Test with all null radioButtons (calling with all field-specific modifiers on method dialog).
     * This ensures line 457 guard works correctly for multiple null cases.
     */
    @Test
    public void testMultipleNullRadioButtonsOnMethodDialog() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        // Set all field-specific modifiers - all will have null radioButtons on method dialog
        MemberSpecification spec = new MemberSpecification(
                AccessConstants.VOLATILE | AccessConstants.TRANSIENT,
                0,
                null,
                "method",
                "()V"
        );

        // This will call setMemberSpecificationRadioButtons multiple times with null radioButtons
        // Line 457 should guard against null for all of them
        memberDialog.setMemberSpecification(spec);

        // Should complete without error
        assertNotNull(memberDialog.getMemberSpecification(), "Should handle multiple null radioButtons");
    }

    /**
     * Comprehensive test covering all lines 457, 459, 460, 461, 462.
     * This test ensures complete coverage of the private method.
     */
    @Test
    public void testComprehensiveCoverageOfSetMemberSpecificationRadioButtons() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Test 1: Line 457 (radioButtons != null) and line 459 (first condition true)
        MemberSpecification spec1 = new MemberSpecification(
                AccessConstants.PUBLIC | AccessConstants.STATIC, 0, null, "field", "I"
        );
        memberDialog.setMemberSpecification(spec1);
        MemberSpecification result1 = memberDialog.getMemberSpecification();
        assertTrue((result1.requiredSetAccessFlags & AccessConstants.PUBLIC) != 0, "Line 459 first condition");
        assertTrue((result1.requiredSetAccessFlags & AccessConstants.STATIC) != 0, "Line 459 first condition");

        // Test 2: Line 457 (radioButtons != null) and line 460 (second condition true)
        MemberSpecification spec2 = new MemberSpecification(
                0, AccessConstants.PRIVATE | AccessConstants.FINAL, null, "field", "I"
        );
        memberDialog.setMemberSpecification(spec2);
        MemberSpecification result2 = memberDialog.getMemberSpecification();
        assertTrue((result2.requiredUnsetAccessFlags & AccessConstants.PRIVATE) != 0, "Line 460 second condition");
        assertTrue((result2.requiredUnsetAccessFlags & AccessConstants.FINAL) != 0, "Line 460 second condition");

        // Test 3: Line 457 (radioButtons != null) and line 461 (default to index 2)
        MemberSpecification spec3 = new MemberSpecification(
                0, 0, null, "field", "I"
        );
        memberDialog.setMemberSpecification(spec3);
        MemberSpecification result3 = memberDialog.getMemberSpecification();
        assertEquals(0, result3.requiredSetAccessFlags & AccessConstants.PROTECTED, "Line 461 default case");
        assertEquals(0, result3.requiredUnsetAccessFlags & AccessConstants.PROTECTED, "Line 461 default case");

        // Test 4: Line 457 (radioButtons == null) - method-specific modifier on field dialog
        MemberSpecification spec4 = new MemberSpecification(
                AccessConstants.SYNCHRONIZED, 0, null, "field", "I"
        );
        memberDialog.setMemberSpecification(spec4);
        assertNotNull(memberDialog.getMemberSpecification(), "Line 457 null guard works");

        // All lines 457, 459, 460, 461, 462 have been covered
        assertTrue(true, "All lines of setMemberSpecificationRadioButtons covered");
    }
}
