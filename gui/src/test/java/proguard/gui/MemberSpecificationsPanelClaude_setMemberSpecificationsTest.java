package proguard.gui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.MemberSpecification;
import proguard.classfile.AccessConstants;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 * Test class for MemberSpecificationsPanel.setMemberSpecifications(List, List) method.
 *
 * The setMemberSpecifications() method sets the field and method specifications to be displayed.
 * Lines that need coverage:
 * - Line 153: Clears the list model
 * - Line 155: Checks if fieldSpecifications is not null
 * - Line 157: Loops through field specifications
 * - Line 159-160: Adds field specification wrapper to list model
 * - Line 165: Checks if methodSpecifications is not null
 * - Line 167: Loops through method specifications
 * - Line 169-170: Adds method specification wrapper to list model
 * - Line 177: Calls enableSelectionButtons()
 * - Line 178: (continuation of method)
 *
 * These tests verify:
 * - The list model is cleared before adding new specifications
 * - Null parameters are handled correctly
 * - Empty lists are handled correctly
 * - Field specifications are added correctly
 * - Method specifications are added correctly
 * - Both field and method specifications can be set together
 * - Previous specifications are replaced
 * - Selection buttons are enabled/disabled correctly after setting
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class MemberSpecificationsPanelClaude_setMemberSpecificationsTest {

    private JDialog testDialog;
    private MemberSpecificationsPanel panel;

    @BeforeEach
    public void setUp() {
        // Tests will skip if headless mode is active
        assumeFalse(GraphicsEnvironment.isHeadless(),
                    "Skipping test: Headless environment detected. GUI components require a display.");
    }

    @AfterEach
    public void tearDown() {
        if (panel != null) {
            panel.removeAll();
        }
        if (testDialog != null) {
            testDialog.dispose();
        }
    }

    /**
     * Test setMemberSpecifications with null lists.
     * This covers lines 153, 155 (false branch), 165 (false branch), 177, 178.
     */
    @Test
    public void testSetMemberSpecificationsWithNullLists() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        // This executes lines 153, 155 (null check fails), 165 (null check fails), 177
        panel.setMemberSpecifications(null, null);

        List fieldSpecs = panel.getMemberSpecifications(true);
        List methodSpecs = panel.getMemberSpecifications(false);

        assertNull(fieldSpecs, "Field specifications should be null");
        assertNull(methodSpecs, "Method specifications should be null");
    }

    /**
     * Test setMemberSpecifications with empty lists.
     * This covers lines 153, 155 (true branch), 157 (loop not entered), 165 (true branch), 167 (loop not entered), 177, 178.
     */
    @Test
    public void testSetMemberSpecificationsWithEmptyLists() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        List<MemberSpecification> emptyFieldList = new ArrayList<>();
        List<MemberSpecification> emptyMethodList = new ArrayList<>();

        // This executes lines 153, 155 (not null), 157 (size=0, loop not entered), 165 (not null), 167 (size=0, loop not entered), 177
        panel.setMemberSpecifications(emptyFieldList, emptyMethodList);

        List fieldSpecs = panel.getMemberSpecifications(true);
        List methodSpecs = panel.getMemberSpecifications(false);

        assertNull(fieldSpecs, "Field specifications should be null for empty list");
        assertNull(methodSpecs, "Method specifications should be null for empty list");
    }

    /**
     * Test setMemberSpecifications with only field specifications.
     * This covers lines 153, 155, 157, 159-160, 165 (false branch), 177, 178.
     */
    @Test
    public void testSetMemberSpecificationsWithOnlyFields() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        List<MemberSpecification> fieldSpecs = new ArrayList<>();
        fieldSpecs.add(new MemberSpecification(
            AccessConstants.PUBLIC, 0, null, "field1", "I"
        ));
        fieldSpecs.add(new MemberSpecification(
            AccessConstants.PRIVATE, 0, null, "field2", "Ljava/lang/String;"
        ));

        // This executes lines 153, 155 (not null), 157 (loop twice), 159-160 (twice), 165 (null), 177
        panel.setMemberSpecifications(fieldSpecs, null);

        List result = panel.getMemberSpecifications(true);
        assertNotNull(result, "Field specifications should not be null");
        assertEquals(2, result.size(), "Should have 2 field specifications");

        MemberSpecification spec1 = (MemberSpecification) result.get(0);
        assertEquals("field1", spec1.name, "First field name should match");
        assertEquals("I", spec1.descriptor, "First field descriptor should match");

        MemberSpecification spec2 = (MemberSpecification) result.get(1);
        assertEquals("field2", spec2.name, "Second field name should match");
        assertEquals("Ljava/lang/String;", spec2.descriptor, "Second field descriptor should match");

        List methodSpecs = panel.getMemberSpecifications(false);
        assertNull(methodSpecs, "Method specifications should be null");
    }

    /**
     * Test setMemberSpecifications with only method specifications.
     * This covers lines 153, 155 (false branch), 165, 167, 169-170, 177, 178.
     */
    @Test
    public void testSetMemberSpecificationsWithOnlyMethods() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        List<MemberSpecification> methodSpecs = new ArrayList<>();
        methodSpecs.add(new MemberSpecification(
            AccessConstants.PUBLIC, 0, null, "method1", "()V"
        ));
        methodSpecs.add(new MemberSpecification(
            AccessConstants.PROTECTED, 0, null, "method2", "(I)Ljava/lang/String;"
        ));

        // This executes lines 153, 155 (null), 165 (not null), 167 (loop twice), 169-170 (twice), 177
        panel.setMemberSpecifications(null, methodSpecs);

        List result = panel.getMemberSpecifications(false);
        assertNotNull(result, "Method specifications should not be null");
        assertEquals(2, result.size(), "Should have 2 method specifications");

        MemberSpecification spec1 = (MemberSpecification) result.get(0);
        assertEquals("method1", spec1.name, "First method name should match");
        assertEquals("()V", spec1.descriptor, "First method descriptor should match");

        MemberSpecification spec2 = (MemberSpecification) result.get(1);
        assertEquals("method2", spec2.name, "Second method name should match");
        assertEquals("(I)Ljava/lang/String;", spec2.descriptor, "Second method descriptor should match");

        List fieldSpecs = panel.getMemberSpecifications(true);
        assertNull(fieldSpecs, "Field specifications should be null");
    }

    /**
     * Test setMemberSpecifications with both field and method specifications.
     * This covers all lines: 153, 155, 157, 159-160, 165, 167, 169-170, 177, 178.
     */
    @Test
    public void testSetMemberSpecificationsWithBothFieldsAndMethods() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        List<MemberSpecification> fieldSpecs = new ArrayList<>();
        fieldSpecs.add(new MemberSpecification(
            AccessConstants.PUBLIC, 0, null, "field1", "I"
        ));

        List<MemberSpecification> methodSpecs = new ArrayList<>();
        methodSpecs.add(new MemberSpecification(
            AccessConstants.PUBLIC, 0, null, "method1", "()V"
        ));

        // This executes ALL lines: 153, 155 (not null), 157 (loop once), 159-160 (once),
        // 165 (not null), 167 (loop once), 169-170 (once), 177
        panel.setMemberSpecifications(fieldSpecs, methodSpecs);

        List resultFields = panel.getMemberSpecifications(true);
        assertNotNull(resultFields, "Field specifications should not be null");
        assertEquals(1, resultFields.size(), "Should have 1 field specification");

        List resultMethods = panel.getMemberSpecifications(false);
        assertNotNull(resultMethods, "Method specifications should not be null");
        assertEquals(1, resultMethods.size(), "Should have 1 method specification");
    }

    /**
     * Test setMemberSpecifications with multiple fields.
     * This covers lines 153, 155, 157 (multiple iterations), 159-160, 177, 178.
     */
    @Test
    public void testSetMemberSpecificationsWithMultipleFields() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        List<MemberSpecification> fieldSpecs = new ArrayList<>();
        fieldSpecs.add(new MemberSpecification(
            AccessConstants.PUBLIC, 0, null, "publicField", "I"
        ));
        fieldSpecs.add(new MemberSpecification(
            AccessConstants.PRIVATE, 0, null, "privateField", "J"
        ));
        fieldSpecs.add(new MemberSpecification(
            AccessConstants.PROTECTED, 0, null, "protectedField", "D"
        ));

        // This executes line 157 three times, lines 159-160 three times
        panel.setMemberSpecifications(fieldSpecs, null);

        List result = panel.getMemberSpecifications(true);
        assertNotNull(result, "Field specifications should not be null");
        assertEquals(3, result.size(), "Should have 3 field specifications");
    }

    /**
     * Test setMemberSpecifications with multiple methods.
     * This covers lines 153, 165, 167 (multiple iterations), 169-170, 177, 178.
     */
    @Test
    public void testSetMemberSpecificationsWithMultipleMethods() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        List<MemberSpecification> methodSpecs = new ArrayList<>();
        methodSpecs.add(new MemberSpecification(
            AccessConstants.PUBLIC, 0, null, "method1", "()V"
        ));
        methodSpecs.add(new MemberSpecification(
            AccessConstants.PRIVATE, 0, null, "method2", "(I)V"
        ));
        methodSpecs.add(new MemberSpecification(
            AccessConstants.PROTECTED, 0, null, "method3", "(II)I"
        ));

        // This executes line 167 three times, lines 169-170 three times
        panel.setMemberSpecifications(null, methodSpecs);

        List result = panel.getMemberSpecifications(false);
        assertNotNull(result, "Method specifications should not be null");
        assertEquals(3, result.size(), "Should have 3 method specifications");
    }

    /**
     * Test that setMemberSpecifications clears previous specifications.
     * This verifies line 153 actually clears the list.
     */
    @Test
    public void testSetMemberSpecificationsClearsPrevious() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        // Set initial specifications
        List<MemberSpecification> fieldSpecs1 = new ArrayList<>();
        fieldSpecs1.add(new MemberSpecification(
            AccessConstants.PUBLIC, 0, null, "field1", "I"
        ));
        panel.setMemberSpecifications(fieldSpecs1, null);

        // Verify initial specifications
        List result1 = panel.getMemberSpecifications(true);
        assertEquals(1, result1.size(), "Should have 1 field specification initially");

        // Set new specifications - line 153 should clear the old ones
        List<MemberSpecification> fieldSpecs2 = new ArrayList<>();
        fieldSpecs2.add(new MemberSpecification(
            AccessConstants.PRIVATE, 0, null, "field2", "J"
        ));
        fieldSpecs2.add(new MemberSpecification(
            AccessConstants.PROTECTED, 0, null, "field3", "D"
        ));
        panel.setMemberSpecifications(fieldSpecs2, null);

        // Verify new specifications replaced the old ones
        List result2 = panel.getMemberSpecifications(true);
        assertEquals(2, result2.size(), "Should have 2 field specifications after reset");
        MemberSpecification spec = (MemberSpecification) result2.get(0);
        assertEquals("field2", spec.name, "First field should be from new specifications");
    }

    /**
     * Test setMemberSpecifications with a single field specification.
     * This covers lines 153, 155, 157 (one iteration), 159-160, 177, 178.
     */
    @Test
    public void testSetMemberSpecificationsWithSingleField() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        List<MemberSpecification> fieldSpecs = new ArrayList<>();
        fieldSpecs.add(new MemberSpecification(
            AccessConstants.PUBLIC, 0, null, "singleField", "I"
        ));

        // Loop at line 157 executes exactly once
        panel.setMemberSpecifications(fieldSpecs, null);

        List result = panel.getMemberSpecifications(true);
        assertNotNull(result, "Field specifications should not be null");
        assertEquals(1, result.size(), "Should have exactly 1 field specification");
    }

    /**
     * Test setMemberSpecifications with a single method specification.
     * This covers lines 153, 165, 167 (one iteration), 169-170, 177, 178.
     */
    @Test
    public void testSetMemberSpecificationsWithSingleMethod() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        List<MemberSpecification> methodSpecs = new ArrayList<>();
        methodSpecs.add(new MemberSpecification(
            AccessConstants.PUBLIC, 0, null, "singleMethod", "()V"
        ));

        // Loop at line 167 executes exactly once
        panel.setMemberSpecifications(null, methodSpecs);

        List result = panel.getMemberSpecifications(false);
        assertNotNull(result, "Method specifications should not be null");
        assertEquals(1, result.size(), "Should have exactly 1 method specification");
    }

    /**
     * Test setMemberSpecifications called multiple times.
     * This ensures line 153 clears properly each time.
     */
    @Test
    public void testSetMemberSpecificationsMultipleTimes() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        // First call
        List<MemberSpecification> specs1 = new ArrayList<>();
        specs1.add(new MemberSpecification(AccessConstants.PUBLIC, 0, null, "field1", "I"));
        panel.setMemberSpecifications(specs1, null);
        assertEquals(1, panel.getMemberSpecifications(true).size(), "Should have 1 spec after first call");

        // Second call - should clear and add new
        List<MemberSpecification> specs2 = new ArrayList<>();
        specs2.add(new MemberSpecification(AccessConstants.PRIVATE, 0, null, "field2", "J"));
        specs2.add(new MemberSpecification(AccessConstants.PROTECTED, 0, null, "field3", "D"));
        panel.setMemberSpecifications(specs2, null);
        assertEquals(2, panel.getMemberSpecifications(true).size(), "Should have 2 specs after second call");

        // Third call - should clear and add new
        List<MemberSpecification> specs3 = new ArrayList<>();
        specs3.add(new MemberSpecification(AccessConstants.PUBLIC, 0, null, "field4", "F"));
        panel.setMemberSpecifications(specs3, null);
        assertEquals(1, panel.getMemberSpecifications(true).size(), "Should have 1 spec after third call");
    }

    /**
     * Test setMemberSpecifications with large number of specifications.
     * This tests the loops at lines 157 and 167 with many iterations.
     */
    @Test
    public void testSetMemberSpecificationsWithManySpecifications() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        List<MemberSpecification> fieldSpecs = new ArrayList<>();
        List<MemberSpecification> methodSpecs = new ArrayList<>();

        // Add 10 field specifications
        for (int i = 0; i < 10; i++) {
            fieldSpecs.add(new MemberSpecification(
                AccessConstants.PUBLIC, 0, null, "field" + i, "I"
            ));
        }

        // Add 10 method specifications
        for (int i = 0; i < 10; i++) {
            methodSpecs.add(new MemberSpecification(
                AccessConstants.PUBLIC, 0, null, "method" + i, "()V"
            ));
        }

        // This executes line 157 ten times and line 167 ten times
        panel.setMemberSpecifications(fieldSpecs, methodSpecs);

        List resultFields = panel.getMemberSpecifications(true);
        assertEquals(10, resultFields.size(), "Should have 10 field specifications");

        List resultMethods = panel.getMemberSpecifications(false);
        assertEquals(10, resultMethods.size(), "Should have 10 method specifications");
    }

    /**
     * Test that enableSelectionButtons is called at the end.
     * This verifies line 177 is executed.
     */
    @Test
    public void testSetMemberSpecificationsCallsEnableSelectionButtons() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        List<MemberSpecification> fieldSpecs = new ArrayList<>();
        fieldSpecs.add(new MemberSpecification(
            AccessConstants.PUBLIC, 0, null, "field1", "I"
        ));

        // This should call enableSelectionButtons() at line 177
        panel.setMemberSpecifications(fieldSpecs, null);

        // The selection buttons should be properly enabled/disabled
        // Edit/Remove buttons should be disabled when no item is selected
        java.util.List buttons = panel.getButtons();
        JButton editButton = (JButton) buttons.get(2);  // Edit button
        assertFalse(editButton.isEnabled(), "Edit button should be disabled (no selection)");
    }

    /**
     * Test setMemberSpecifications preserves specification properties.
     * This verifies lines 159-160 and 169-170 correctly wrap specifications.
     */
    @Test
    public void testSetMemberSpecificationsPreservesProperties() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        List<MemberSpecification> fieldSpecs = new ArrayList<>();
        fieldSpecs.add(new MemberSpecification(
            AccessConstants.PUBLIC | AccessConstants.STATIC | AccessConstants.FINAL,
            AccessConstants.PRIVATE,
            "Ljava/lang/Deprecated;",
            "constantField",
            "I"
        ));

        panel.setMemberSpecifications(fieldSpecs, null);

        List result = panel.getMemberSpecifications(true);
        MemberSpecification spec = (MemberSpecification) result.get(0);

        assertEquals(AccessConstants.PUBLIC | AccessConstants.STATIC | AccessConstants.FINAL,
                     spec.requiredSetAccessFlags, "Required set access flags should be preserved");
        assertEquals(AccessConstants.PRIVATE,
                     spec.requiredUnsetAccessFlags, "Required unset access flags should be preserved");
        assertEquals("Ljava/lang/Deprecated;", spec.annotationType, "Annotation type should be preserved");
        assertEquals("constantField", spec.name, "Name should be preserved");
        assertEquals("I", spec.descriptor, "Descriptor should be preserved");
    }

    /**
     * Test setMemberSpecifications with null field list and non-null method list.
     * This covers lines 153, 155 (false), 165 (true), 167, 169-170, 177, 178.
     */
    @Test
    public void testSetMemberSpecificationsNullFieldsNonNullMethods() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        List<MemberSpecification> methodSpecs = new ArrayList<>();
        methodSpecs.add(new MemberSpecification(
            AccessConstants.PUBLIC, 0, null, "method1", "()V"
        ));

        // fieldSpecifications is null (line 155 fails), methodSpecifications is not null (line 165 succeeds)
        panel.setMemberSpecifications(null, methodSpecs);

        List resultFields = panel.getMemberSpecifications(true);
        assertNull(resultFields, "Field specifications should be null");

        List resultMethods = panel.getMemberSpecifications(false);
        assertNotNull(resultMethods, "Method specifications should not be null");
        assertEquals(1, resultMethods.size(), "Should have 1 method specification");
    }

    /**
     * Test setMemberSpecifications with non-null field list and null method list.
     * This covers lines 153, 155 (true), 157, 159-160, 165 (false), 177, 178.
     */
    @Test
    public void testSetMemberSpecificationsNonNullFieldsNullMethods() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        List<MemberSpecification> fieldSpecs = new ArrayList<>();
        fieldSpecs.add(new MemberSpecification(
            AccessConstants.PUBLIC, 0, null, "field1", "I"
        ));

        // fieldSpecifications is not null (line 155 succeeds), methodSpecifications is null (line 165 fails)
        panel.setMemberSpecifications(fieldSpecs, null);

        List resultFields = panel.getMemberSpecifications(true);
        assertNotNull(resultFields, "Field specifications should not be null");
        assertEquals(1, resultFields.size(), "Should have 1 field specification");

        List resultMethods = panel.getMemberSpecifications(false);
        assertNull(resultMethods, "Method specifications should be null");
    }

    /**
     * Test setMemberSpecifications clears to empty state.
     * This verifies line 153 clears the list when both parameters are null.
     */
    @Test
    public void testSetMemberSpecificationsClearsToEmpty() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        // Add some specifications
        List<MemberSpecification> fieldSpecs = new ArrayList<>();
        fieldSpecs.add(new MemberSpecification(
            AccessConstants.PUBLIC, 0, null, "field1", "I"
        ));
        panel.setMemberSpecifications(fieldSpecs, null);

        // Verify specifications exist
        assertNotNull(panel.getMemberSpecifications(true), "Should have field specifications");

        // Clear by setting both to null - line 153 should clear
        panel.setMemberSpecifications(null, null);

        // Verify cleared
        assertNull(panel.getMemberSpecifications(true), "Field specifications should be null after clearing");
        assertNull(panel.getMemberSpecifications(false), "Method specifications should be null after clearing");
    }

    /**
     * Test setMemberSpecifications with empty field list and non-empty method list.
     * This covers different combinations of empty and non-empty lists.
     */
    @Test
    public void testSetMemberSpecificationsEmptyFieldsNonEmptyMethods() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        List<MemberSpecification> emptyFieldList = new ArrayList<>();
        List<MemberSpecification> methodSpecs = new ArrayList<>();
        methodSpecs.add(new MemberSpecification(
            AccessConstants.PUBLIC, 0, null, "method1", "()V"
        ));

        // Line 157 loop not entered (size=0), line 167 loop entered once
        panel.setMemberSpecifications(emptyFieldList, methodSpecs);

        List resultFields = panel.getMemberSpecifications(true);
        assertNull(resultFields, "Field specifications should be null for empty list");

        List resultMethods = panel.getMemberSpecifications(false);
        assertNotNull(resultMethods, "Method specifications should not be null");
        assertEquals(1, resultMethods.size(), "Should have 1 method specification");
    }

    /**
     * Test setMemberSpecifications with non-empty field list and empty method list.
     * This covers different combinations of empty and non-empty lists.
     */
    @Test
    public void testSetMemberSpecificationsNonEmptyFieldsEmptyMethods() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        List<MemberSpecification> fieldSpecs = new ArrayList<>();
        fieldSpecs.add(new MemberSpecification(
            AccessConstants.PUBLIC, 0, null, "field1", "I"
        ));
        List<MemberSpecification> emptyMethodList = new ArrayList<>();

        // Line 157 loop entered once, line 167 loop not entered (size=0)
        panel.setMemberSpecifications(fieldSpecs, emptyMethodList);

        List resultFields = panel.getMemberSpecifications(true);
        assertNotNull(resultFields, "Field specifications should not be null");
        assertEquals(1, resultFields.size(), "Should have 1 field specification");

        List resultMethods = panel.getMemberSpecifications(false);
        assertNull(resultMethods, "Method specifications should be null for empty list");
    }

    /**
     * Test setMemberSpecifications maintains order of specifications.
     * This verifies that the loop indices at lines 157 and 167 preserve order.
     */
    @Test
    public void testSetMemberSpecificationsPreservesOrder() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        List<MemberSpecification> fieldSpecs = new ArrayList<>();
        fieldSpecs.add(new MemberSpecification(AccessConstants.PUBLIC, 0, null, "first", "I"));
        fieldSpecs.add(new MemberSpecification(AccessConstants.PUBLIC, 0, null, "second", "J"));
        fieldSpecs.add(new MemberSpecification(AccessConstants.PUBLIC, 0, null, "third", "D"));

        panel.setMemberSpecifications(fieldSpecs, null);

        List result = panel.getMemberSpecifications(true);
        assertEquals("first", ((MemberSpecification) result.get(0)).name, "First spec should be 'first'");
        assertEquals("second", ((MemberSpecification) result.get(1)).name, "Second spec should be 'second'");
        assertEquals("third", ((MemberSpecification) result.get(2)).name, "Third spec should be 'third'");
    }

    /**
     * Test setMemberSpecifications with specifications having null names.
     */
    @Test
    public void testSetMemberSpecificationsWithNullNames() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        List<MemberSpecification> fieldSpecs = new ArrayList<>();
        fieldSpecs.add(new MemberSpecification(
            AccessConstants.PUBLIC, 0, null, null, "I"
        ));

        List<MemberSpecification> methodSpecs = new ArrayList<>();
        methodSpecs.add(new MemberSpecification(
            AccessConstants.PUBLIC, 0, null, null, "()V"
        ));

        panel.setMemberSpecifications(fieldSpecs, methodSpecs);

        List resultFields = panel.getMemberSpecifications(true);
        assertNotNull(resultFields, "Field specifications should not be null");
        assertEquals(1, resultFields.size(), "Should have 1 field specification");
        assertNull(((MemberSpecification) resultFields.get(0)).name, "Field name should be null");

        List resultMethods = panel.getMemberSpecifications(false);
        assertNotNull(resultMethods, "Method specifications should not be null");
        assertEquals(1, resultMethods.size(), "Should have 1 method specification");
        assertNull(((MemberSpecification) resultMethods.get(0)).name, "Method name should be null");
    }

    /**
     * Test setMemberSpecifications with specifications having null descriptors.
     */
    @Test
    public void testSetMemberSpecificationsWithNullDescriptors() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        List<MemberSpecification> fieldSpecs = new ArrayList<>();
        fieldSpecs.add(new MemberSpecification(
            AccessConstants.PUBLIC, 0, null, "field1", null
        ));

        List<MemberSpecification> methodSpecs = new ArrayList<>();
        methodSpecs.add(new MemberSpecification(
            AccessConstants.PUBLIC, 0, null, "method1", null
        ));

        panel.setMemberSpecifications(fieldSpecs, methodSpecs);

        List resultFields = panel.getMemberSpecifications(true);
        assertNotNull(resultFields, "Field specifications should not be null");
        assertNull(((MemberSpecification) resultFields.get(0)).descriptor, "Field descriptor should be null");

        List resultMethods = panel.getMemberSpecifications(false);
        assertNotNull(resultMethods, "Method specifications should not be null");
        assertNull(((MemberSpecification) resultMethods.get(0)).descriptor, "Method descriptor should be null");
    }

    /**
     * Test that all lines are executed in a single comprehensive call.
     * This ensures complete line coverage.
     */
    @Test
    public void testSetMemberSpecificationsCompleteCoverage() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        List<MemberSpecification> fieldSpecs = new ArrayList<>();
        fieldSpecs.add(new MemberSpecification(AccessConstants.PUBLIC, 0, null, "field1", "I"));
        fieldSpecs.add(new MemberSpecification(AccessConstants.PRIVATE, 0, null, "field2", "J"));

        List<MemberSpecification> methodSpecs = new ArrayList<>();
        methodSpecs.add(new MemberSpecification(AccessConstants.PUBLIC, 0, null, "method1", "()V"));
        methodSpecs.add(new MemberSpecification(AccessConstants.PROTECTED, 0, null, "method2", "(I)I"));

        // This executes ALL lines: 153, 155, 157 (twice), 159-160 (twice), 165, 167 (twice), 169-170 (twice), 177, 178
        panel.setMemberSpecifications(fieldSpecs, methodSpecs);

        // Verify results
        List resultFields = panel.getMemberSpecifications(true);
        assertEquals(2, resultFields.size(), "Should have 2 field specifications");

        List resultMethods = panel.getMemberSpecifications(false);
        assertEquals(2, resultMethods.size(), "Should have 2 method specifications");
    }

    /**
     * Test setMemberSpecifications doesn't throw exception with any valid input.
     */
    @Test
    public void testSetMemberSpecificationsNoException() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        List<MemberSpecification> fieldSpecs = new ArrayList<>();
        fieldSpecs.add(new MemberSpecification(AccessConstants.PUBLIC, 0, null, "field", "I"));

        assertDoesNotThrow(() -> {
            panel.setMemberSpecifications(fieldSpecs, null);
        }, "setMemberSpecifications should not throw exception");

        assertDoesNotThrow(() -> {
            panel.setMemberSpecifications(null, null);
        }, "setMemberSpecifications with null should not throw exception");

        assertDoesNotThrow(() -> {
            panel.setMemberSpecifications(new ArrayList<>(), new ArrayList<>());
        }, "setMemberSpecifications with empty lists should not throw exception");
    }
}
