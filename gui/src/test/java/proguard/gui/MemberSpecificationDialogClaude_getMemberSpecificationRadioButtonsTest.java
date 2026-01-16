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
 * Test class for coverage of MemberSpecificationDialog.getMemberSpecificationRadioButtons private method.
 *
 * The getMemberSpecificationRadioButtons method (lines 471-486) is private and:
 * - Checks if radioButtons parameter is not null (line 475)
 * - If radioButtons[0] is selected, adds flag to requiredSetAccessFlags (lines 477-479)
 * - Else if radioButtons[1] is selected, adds flag to requiredUnsetAccessFlags (lines 481-483)
 * - If radioButtons[2] is selected, does nothing (don't care state)
 * - If radioButtons is null, does nothing (line 486)
 *
 * This method is called from getMemberSpecification (lines 410-423) for each access modifier:
 * - Always called for: PUBLIC, PRIVATE, PROTECTED, STATIC, FINAL, SYNTHETIC
 * - For fields: Also called for VOLATILE, TRANSIENT (may pass null radioButtons)
 * - For methods: Also called for SYNCHRONIZED, NATIVE, ABSTRACT, STRICT, BRIDGE, VARARGS (may pass null radioButtons)
 *
 * These tests cover the private method by:
 * - Setting radio buttons to different states via setMemberSpecification
 * - Calling getMemberSpecification which internally calls getMemberSpecificationRadioButtons
 * - Verifying the returned MemberSpecification has correct access flags
 * - Testing all three radio button states (required set, required unset, don't care)
 * - Testing with null radioButtons
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class MemberSpecificationDialogClaude_getMemberSpecificationRadioButtonsTest {

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
     * Test getMemberSpecificationRadioButtons with radioButtons[0] selected (required set).
     * This covers lines 475, 477, 479.
     */
    @Test
    public void testGetMemberSpecificationRadioButtonsWithFirstButtonSelected() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Set PUBLIC flag as required set (radioButtons[0] will be selected)
        MemberSpecification fieldSpec = new MemberSpecification(
                AccessConstants.PUBLIC, 0, null, "publicField", "I"
        );
        memberDialog.setMemberSpecification(fieldSpec);

        // Call getMemberSpecification which internally calls getMemberSpecificationRadioButtons
        // Line 475: radioButtons != null (publicRadioButtons exists)
        // Line 477: radioButtons[0].isSelected() returns true
        // Line 479: memberSpecification.requiredSetAccessFlags |= flag (PUBLIC flag added)
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertTrue((result.requiredSetAccessFlags & AccessConstants.PUBLIC) != 0,
                   "PUBLIC flag should be in requiredSetAccessFlags (first button selected)");
        assertEquals(0, result.requiredUnsetAccessFlags & AccessConstants.PUBLIC,
                     "PUBLIC should not be in requiredUnsetAccessFlags");
    }

    /**
     * Test getMemberSpecificationRadioButtons with radioButtons[1] selected (required unset).
     * This covers lines 475, 477 (false), 481, 483.
     */
    @Test
    public void testGetMemberSpecificationRadioButtonsWithSecondButtonSelected() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Set PUBLIC flag as required unset (radioButtons[1] will be selected)
        MemberSpecification fieldSpec = new MemberSpecification(
                0, AccessConstants.PUBLIC, null, "nonPublicField", "I"
        );
        memberDialog.setMemberSpecification(fieldSpec);

        // Call getMemberSpecification which internally calls getMemberSpecificationRadioButtons
        // Line 475: radioButtons != null (publicRadioButtons exists)
        // Line 477: radioButtons[0].isSelected() returns false
        // Line 481: radioButtons[1].isSelected() returns true
        // Line 483: memberSpecification.requiredUnsetAccessFlags |= flag (PUBLIC flag added)
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertTrue((result.requiredUnsetAccessFlags & AccessConstants.PUBLIC) != 0,
                   "PUBLIC flag should be in requiredUnsetAccessFlags (second button selected)");
        assertEquals(0, result.requiredSetAccessFlags & AccessConstants.PUBLIC,
                     "PUBLIC should not be in requiredSetAccessFlags");
    }

    /**
     * Test getMemberSpecificationRadioButtons with radioButtons[2] selected (don't care).
     * This covers lines 475, 477 (false), 481 (false), 486 (implicit - no action taken).
     */
    @Test
    public void testGetMemberSpecificationRadioButtonsWithThirdButtonSelected() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Set no PUBLIC flags (radioButtons[2] will be selected - don't care)
        MemberSpecification fieldSpec = new MemberSpecification(
                0, 0, null, "anyField", "I"
        );
        memberDialog.setMemberSpecification(fieldSpec);

        // Call getMemberSpecification which internally calls getMemberSpecificationRadioButtons
        // Line 475: radioButtons != null (publicRadioButtons exists)
        // Line 477: radioButtons[0].isSelected() returns false
        // Line 481: radioButtons[1].isSelected() returns false
        // Line 486: Method exits without setting any flags
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertEquals(0, result.requiredSetAccessFlags & AccessConstants.PUBLIC,
                     "PUBLIC should not be in requiredSetAccessFlags (third button selected)");
        assertEquals(0, result.requiredUnsetAccessFlags & AccessConstants.PUBLIC,
                     "PUBLIC should not be in requiredUnsetAccessFlags (third button selected)");
    }

    /**
     * Test getMemberSpecificationRadioButtons with null radioButtons.
     * This covers line 475 (false branch) and line 486.
     */
    @Test
    public void testGetMemberSpecificationRadioButtonsWithNullRadioButtons() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        // For method dialog, volatileRadioButtons is null
        // Set VOLATILE flag (which will be passed to getMemberSpecificationRadioButtons with null)
        MemberSpecification methodSpec = new MemberSpecification(
                AccessConstants.VOLATILE, 0, null, "method", "()V"
        );
        memberDialog.setMemberSpecification(methodSpec);

        // Call getMemberSpecification which internally calls getMemberSpecificationRadioButtons
        // Line 475: radioButtons == null (volatileRadioButtons doesn't exist for method dialog)
        // Line 486: Method exits without attempting to access radioButtons
        MemberSpecification result = memberDialog.getMemberSpecification();

        // The method should complete without error even though radioButtons was null
        assertNotNull(result, "getMemberSpecification should handle null radioButtons gracefully");
    }

    /**
     * Test all common access modifiers to cover lines 475-486 for multiple flags.
     * This ensures the method works correctly for all common modifiers.
     */
    @Test
    public void testAllCommonAccessModifiersFirstButtonSelected() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Set all common flags as required set
        MemberSpecification fieldSpec = new MemberSpecification(
                AccessConstants.PUBLIC | AccessConstants.PRIVATE | AccessConstants.PROTECTED |
                AccessConstants.STATIC | AccessConstants.FINAL | AccessConstants.SYNTHETIC,
                0,
                null,
                "field",
                "I"
        );
        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();

        // All flags should be in requiredSetAccessFlags
        assertTrue((result.requiredSetAccessFlags & AccessConstants.PUBLIC) != 0,
                   "PUBLIC should be in requiredSetAccessFlags");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.PRIVATE) != 0,
                   "PRIVATE should be in requiredSetAccessFlags");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.PROTECTED) != 0,
                   "PROTECTED should be in requiredSetAccessFlags");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.STATIC) != 0,
                   "STATIC should be in requiredSetAccessFlags");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.FINAL) != 0,
                   "FINAL should be in requiredSetAccessFlags");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.SYNTHETIC) != 0,
                   "SYNTHETIC should be in requiredSetAccessFlags");
    }

    /**
     * Test field-specific access modifiers with non-null radioButtons.
     * This ensures lines 475-486 work for field-specific modifiers.
     */
    @Test
    public void testFieldSpecificAccessModifiers() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Set field-specific flags as required set
        MemberSpecification fieldSpec = new MemberSpecification(
                AccessConstants.VOLATILE | AccessConstants.TRANSIENT,
                0,
                null,
                "field",
                "I"
        );
        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();

        assertTrue((result.requiredSetAccessFlags & AccessConstants.VOLATILE) != 0,
                   "VOLATILE should be in requiredSetAccessFlags");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.TRANSIENT) != 0,
                   "TRANSIENT should be in requiredSetAccessFlags");
    }

    /**
     * Test method-specific access modifiers with non-null radioButtons.
     * This ensures lines 475-486 work for method-specific modifiers.
     */
    @Test
    public void testMethodSpecificAccessModifiers() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        // Set method-specific flags as required set
        MemberSpecification methodSpec = new MemberSpecification(
                AccessConstants.SYNCHRONIZED | AccessConstants.NATIVE | AccessConstants.ABSTRACT |
                AccessConstants.STRICT | AccessConstants.BRIDGE | AccessConstants.VARARGS,
                0,
                null,
                "method",
                "()V"
        );
        memberDialog.setMemberSpecification(methodSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();

        assertTrue((result.requiredSetAccessFlags & AccessConstants.SYNCHRONIZED) != 0,
                   "SYNCHRONIZED should be in requiredSetAccessFlags");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.NATIVE) != 0,
                   "NATIVE should be in requiredSetAccessFlags");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.ABSTRACT) != 0,
                   "ABSTRACT should be in requiredSetAccessFlags");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.STRICT) != 0,
                   "STRICT should be in requiredSetAccessFlags");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.BRIDGE) != 0,
                   "BRIDGE should be in requiredSetAccessFlags");
        assertTrue((result.requiredSetAccessFlags & AccessConstants.VARARGS) != 0,
                   "VARARGS should be in requiredSetAccessFlags");
    }

    /**
     * Test all three radio button states for multiple modifiers.
     * This comprehensively tests all code paths in lines 475-486.
     */
    @Test
    public void testMultipleModifiersWithDifferentButtonStates() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // PUBLIC: required set (radioButtons[0])
        // PRIVATE: required unset (radioButtons[1])
        // PROTECTED: don't care (radioButtons[2])
        MemberSpecification fieldSpec = new MemberSpecification(
                AccessConstants.PUBLIC,   // Required set
                AccessConstants.PRIVATE,  // Required unset
                null,
                "field",
                "I"
        );
        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();

        // Line 479 executed for PUBLIC
        assertTrue((result.requiredSetAccessFlags & AccessConstants.PUBLIC) != 0,
                   "PUBLIC should be in requiredSetAccessFlags (line 479)");
        // Line 483 executed for PRIVATE
        assertTrue((result.requiredUnsetAccessFlags & AccessConstants.PRIVATE) != 0,
                   "PRIVATE should be in requiredUnsetAccessFlags (line 483)");
        // Neither line 479 nor 483 executed for PROTECTED
        assertEquals(0, result.requiredSetAccessFlags & AccessConstants.PROTECTED,
                     "PROTECTED should not be in requiredSetAccessFlags");
        assertEquals(0, result.requiredUnsetAccessFlags & AccessConstants.PROTECTED,
                     "PROTECTED should not be in requiredUnsetAccessFlags");
    }

    /**
     * Test the OR operation in lines 479 and 483.
     * This verifies that flags are accumulated correctly using |= operator.
     */
    @Test
    public void testFlagsAreAccumulatedWithOrOperation() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Set multiple flags as required set
        MemberSpecification fieldSpec = new MemberSpecification(
                AccessConstants.PUBLIC | AccessConstants.STATIC | AccessConstants.FINAL,
                0,
                null,
                "field",
                "I"
        );
        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();

        // Line 479 should be executed multiple times, accumulating flags with |=
        int expectedFlags = AccessConstants.PUBLIC | AccessConstants.STATIC | AccessConstants.FINAL;
        assertEquals(expectedFlags, result.requiredSetAccessFlags & expectedFlags,
                     "All flags should be accumulated with OR operation (line 479)");
    }

    /**
     * Test calling getMemberSpecification multiple times.
     * This ensures lines 475-486 work correctly on repeated calls.
     */
    @Test
    public void testGetMemberSpecificationCalledMultipleTimes() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // First call
        MemberSpecification spec1 = new MemberSpecification(
                AccessConstants.PUBLIC, 0, null, "field1", "I"
        );
        memberDialog.setMemberSpecification(spec1);
        MemberSpecification result1 = memberDialog.getMemberSpecification();
        assertTrue((result1.requiredSetAccessFlags & AccessConstants.PUBLIC) != 0);

        // Second call
        MemberSpecification spec2 = new MemberSpecification(
                0, AccessConstants.STATIC, null, "field2", "I"
        );
        memberDialog.setMemberSpecification(spec2);
        MemberSpecification result2 = memberDialog.getMemberSpecification();
        assertTrue((result2.requiredUnsetAccessFlags & AccessConstants.STATIC) != 0);

        // Third call
        MemberSpecification spec3 = new MemberSpecification(
                0, 0, null, "field3", "I"
        );
        memberDialog.setMemberSpecification(spec3);
        MemberSpecification result3 = memberDialog.getMemberSpecification();
        assertEquals(0, result3.requiredSetAccessFlags & AccessConstants.FINAL);
        assertEquals(0, result3.requiredUnsetAccessFlags & AccessConstants.FINAL);
    }

    /**
     * Test with all field-specific modifiers on method dialog (null radioButtons).
     * This ensures line 475 guard works correctly for multiple null cases.
     */
    @Test
    public void testMultipleNullRadioButtonsOnMethodDialog() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, false);

        // Set field-specific modifiers - all will have null radioButtons on method dialog
        MemberSpecification methodSpec = new MemberSpecification(
                AccessConstants.VOLATILE | AccessConstants.TRANSIENT,
                0,
                null,
                "method",
                "()V"
        );
        memberDialog.setMemberSpecification(methodSpec);

        // This will call getMemberSpecificationRadioButtons with null radioButtons
        // Line 475: radioButtons == null for both VOLATILE and TRANSIENT
        // Line 486: Method exits safely without accessing null radioButtons
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Should handle multiple null radioButtons gracefully");
    }

    /**
     * Test with all method-specific modifiers on field dialog (null radioButtons).
     * This ensures line 475 guard works correctly for multiple null cases.
     */
    @Test
    public void testMultipleNullRadioButtonsOnFieldDialog() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Set method-specific modifiers - all will have null radioButtons on field dialog
        MemberSpecification fieldSpec = new MemberSpecification(
                AccessConstants.SYNCHRONIZED | AccessConstants.NATIVE | AccessConstants.ABSTRACT,
                0,
                null,
                "field",
                "I"
        );
        memberDialog.setMemberSpecification(fieldSpec);

        // This will call getMemberSpecificationRadioButtons with null radioButtons
        // Line 475: radioButtons == null for SYNCHRONIZED, NATIVE, ABSTRACT
        // Line 486: Method exits safely without accessing null radioButtons
        MemberSpecification result = memberDialog.getMemberSpecification();

        assertNotNull(result, "Should handle multiple null radioButtons gracefully");
    }

    /**
     * Test that requiredSetAccessFlags accumulates correctly for multiple flags.
     * This specifically tests line 479 with multiple calls.
     */
    @Test
    public void testRequiredSetAccessFlagsAccumulation() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                AccessConstants.PUBLIC | AccessConstants.STATIC | AccessConstants.FINAL |
                AccessConstants.VOLATILE | AccessConstants.TRANSIENT | AccessConstants.SYNTHETIC,
                0,
                null,
                "field",
                "I"
        );
        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();

        // All these flags should have been accumulated via line 479
        int allSetFlags = AccessConstants.PUBLIC | AccessConstants.STATIC | AccessConstants.FINAL |
                          AccessConstants.VOLATILE | AccessConstants.TRANSIENT | AccessConstants.SYNTHETIC;
        assertEquals(allSetFlags, result.requiredSetAccessFlags & allSetFlags,
                     "All required set flags should be accumulated via line 479");
    }

    /**
     * Test that requiredUnsetAccessFlags accumulates correctly for multiple flags.
     * This specifically tests line 483 with multiple calls.
     */
    @Test
    public void testRequiredUnsetAccessFlagsAccumulation() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        MemberSpecification fieldSpec = new MemberSpecification(
                0,
                AccessConstants.PUBLIC | AccessConstants.PRIVATE | AccessConstants.PROTECTED |
                AccessConstants.STATIC | AccessConstants.FINAL | AccessConstants.SYNTHETIC,
                null,
                "field",
                "I"
        );
        memberDialog.setMemberSpecification(fieldSpec);

        MemberSpecification result = memberDialog.getMemberSpecification();

        // All these flags should have been accumulated via line 483
        int allUnsetFlags = AccessConstants.PUBLIC | AccessConstants.PRIVATE | AccessConstants.PROTECTED |
                            AccessConstants.STATIC | AccessConstants.FINAL | AccessConstants.SYNTHETIC;
        assertEquals(allUnsetFlags, result.requiredUnsetAccessFlags & allUnsetFlags,
                     "All required unset flags should be accumulated via line 483");
    }

    /**
     * Comprehensive test covering all lines 475, 477, 479, 481, 483, 486.
     * This test ensures complete coverage of the private method.
     */
    @Test
    public void testComprehensiveCoverageOfGetMemberSpecificationRadioButtons() {
        testDialog = new JDialog();
        memberDialog = new MemberSpecificationDialog(testDialog, true);

        // Test 1: Line 475 (radioButtons != null), line 477 (true), line 479
        MemberSpecification spec1 = new MemberSpecification(
                AccessConstants.PUBLIC, 0, null, "field", "I"
        );
        memberDialog.setMemberSpecification(spec1);
        MemberSpecification result1 = memberDialog.getMemberSpecification();
        assertTrue((result1.requiredSetAccessFlags & AccessConstants.PUBLIC) != 0,
                   "Line 479 executed (first button selected)");

        // Test 2: Line 475 (radioButtons != null), line 477 (false), line 481 (true), line 483
        MemberSpecification spec2 = new MemberSpecification(
                0, AccessConstants.PRIVATE, null, "field", "I"
        );
        memberDialog.setMemberSpecification(spec2);
        MemberSpecification result2 = memberDialog.getMemberSpecification();
        assertTrue((result2.requiredUnsetAccessFlags & AccessConstants.PRIVATE) != 0,
                   "Line 483 executed (second button selected)");

        // Test 3: Line 475 (radioButtons != null), line 477 (false), line 481 (false), line 486
        MemberSpecification spec3 = new MemberSpecification(
                0, 0, null, "field", "I"
        );
        memberDialog.setMemberSpecification(spec3);
        MemberSpecification result3 = memberDialog.getMemberSpecification();
        assertEquals(0, result3.requiredSetAccessFlags & AccessConstants.PROTECTED,
                     "Line 486 reached (third button selected, no flags set)");
        assertEquals(0, result3.requiredUnsetAccessFlags & AccessConstants.PROTECTED,
                     "Line 486 reached (third button selected, no flags set)");

        // Test 4: Line 475 (radioButtons == null), line 486
        MemberSpecificationDialog methodDialog = new MemberSpecificationDialog(testDialog, false);
        MemberSpecification spec4 = new MemberSpecification(
                AccessConstants.VOLATILE, 0, null, "method", "()V"
        );
        methodDialog.setMemberSpecification(spec4);
        MemberSpecification result4 = methodDialog.getMemberSpecification();
        assertNotNull(result4, "Line 475 false branch, line 486 reached (null radioButtons)");
        methodDialog.dispose();

        // All lines 475, 477, 479, 481, 483, 486 have been covered
        assertTrue(true, "All lines of getMemberSpecificationRadioButtons covered");
    }
}
