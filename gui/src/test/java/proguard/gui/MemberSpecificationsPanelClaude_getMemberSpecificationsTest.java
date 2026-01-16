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
 * Test class for MemberSpecificationsPanel.getMemberSpecifications(boolean) method.
 *
 * The getMemberSpecifications() method retrieves field or method specifications from the panel.
 * Lines that need coverage:
 * - Line 190: Gets the size of the list model
 * - Line 191: Checks if size is 0
 * - Line 193: Returns null if size is 0
 * - Line 196: Creates a new ArrayList with initial capacity
 * - Line 197: Loops through all items in the list model
 * - Line 199-200: Gets the wrapper from the list model
 * - Line 202: Checks if wrapper.isField matches the parameter
 * - Line 204: Adds the member specification to the result list
 * - Line 208: Returns the list of specifications
 *
 * These tests verify:
 * - Returns null when list is empty (lines 190, 191, 193)
 * - Creates and returns list when items exist (lines 190, 196, 197, 199-200, 202, 204, 208)
 * - Filters correctly by isField parameter (line 202)
 * - Returns only matching specifications
 * - Handles mixed field and method specifications
 * - Preserves order of specifications
 * - Works with various list sizes
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class MemberSpecificationsPanelClaude_getMemberSpecificationsTest {

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
     * Test getMemberSpecifications returns null for empty panel.
     * This covers lines 190, 191 (true branch), 193.
     */
    @Test
    public void testGetMemberSpecificationsReturnsNullWhenEmpty() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        // Line 190: size = 0, Line 191: if (size == 0) is true, Line 193: return null
        List fieldSpecs = panel.getMemberSpecifications(true);
        assertNull(fieldSpecs, "Field specifications should be null when empty");

        List methodSpecs = panel.getMemberSpecifications(false);
        assertNull(methodSpecs, "Method specifications should be null when empty");
    }

    /**
     * Test getMemberSpecifications with only field specifications.
     * This covers lines 190, 191 (false), 196, 197, 199-200, 202 (true), 204, 208.
     */
    @Test
    public void testGetMemberSpecificationsWithOnlyFields() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        List<MemberSpecification> fieldSpecs = new ArrayList<>();
        fieldSpecs.add(new MemberSpecification(
            AccessConstants.PUBLIC, 0, null, "field1", "I"
        ));
        fieldSpecs.add(new MemberSpecification(
            AccessConstants.PRIVATE, 0, null, "field2", "Ljava/lang/String;"
        ));

        panel.setMemberSpecifications(fieldSpecs, null);

        // Line 190: size = 2, Line 191: false, Line 196: create ArrayList,
        // Line 197: loop twice, Line 199-200: get wrapper, Line 202: check isField == true,
        // Line 204: add specification, Line 208: return list
        List result = panel.getMemberSpecifications(true);
        assertNotNull(result, "Field specifications should not be null");
        assertEquals(2, result.size(), "Should have 2 field specifications");

        MemberSpecification spec1 = (MemberSpecification) result.get(0);
        assertEquals("field1", spec1.name, "First field name should match");
        assertEquals("I", spec1.descriptor, "First field descriptor should match");

        MemberSpecification spec2 = (MemberSpecification) result.get(1);
        assertEquals("field2", spec2.name, "Second field name should match");
        assertEquals("Ljava/lang/String;", spec2.descriptor, "Second field descriptor should match");
    }

    /**
     * Test getMemberSpecifications with only method specifications.
     * This covers lines 190, 191 (false), 196, 197, 199-200, 202 (true), 204, 208.
     */
    @Test
    public void testGetMemberSpecificationsWithOnlyMethods() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        List<MemberSpecification> methodSpecs = new ArrayList<>();
        methodSpecs.add(new MemberSpecification(
            AccessConstants.PUBLIC, 0, null, "method1", "()V"
        ));
        methodSpecs.add(new MemberSpecification(
            AccessConstants.PROTECTED, 0, null, "method2", "(I)Ljava/lang/String;"
        ));

        panel.setMemberSpecifications(null, methodSpecs);

        // Line 190: size = 2, Line 191: false, Line 196: create ArrayList,
        // Line 197: loop twice, Line 199-200: get wrapper, Line 202: check isField == false,
        // Line 204: add specification, Line 208: return list
        List result = panel.getMemberSpecifications(false);
        assertNotNull(result, "Method specifications should not be null");
        assertEquals(2, result.size(), "Should have 2 method specifications");

        MemberSpecification spec1 = (MemberSpecification) result.get(0);
        assertEquals("method1", spec1.name, "First method name should match");
        assertEquals("()V", spec1.descriptor, "First method descriptor should match");

        MemberSpecification spec2 = (MemberSpecification) result.get(1);
        assertEquals("method2", spec2.name, "Second method name should match");
        assertEquals("(I)Ljava/lang/String;", spec2.descriptor, "Second method descriptor should match");
    }

    /**
     * Test getMemberSpecifications filters correctly by isField parameter.
     * This covers line 202 with both true and false outcomes.
     */
    @Test
    public void testGetMemberSpecificationsFiltersCorrectly() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        List<MemberSpecification> fieldSpecs = new ArrayList<>();
        fieldSpecs.add(new MemberSpecification(
            AccessConstants.PUBLIC, 0, null, "field1", "I"
        ));
        fieldSpecs.add(new MemberSpecification(
            AccessConstants.PRIVATE, 0, null, "field2", "J"
        ));

        List<MemberSpecification> methodSpecs = new ArrayList<>();
        methodSpecs.add(new MemberSpecification(
            AccessConstants.PUBLIC, 0, null, "method1", "()V"
        ));
        methodSpecs.add(new MemberSpecification(
            AccessConstants.PROTECTED, 0, null, "method2", "(I)I"
        ));

        panel.setMemberSpecifications(fieldSpecs, methodSpecs);

        // Get field specifications - line 202 filters by isField == true
        List resultFields = panel.getMemberSpecifications(true);
        assertNotNull(resultFields, "Field specifications should not be null");
        assertEquals(2, resultFields.size(), "Should have exactly 2 field specifications");
        for (Object obj : resultFields) {
            MemberSpecification spec = (MemberSpecification) obj;
            assertTrue(spec.name.startsWith("field"), "All returned specifications should be fields");
        }

        // Get method specifications - line 202 filters by isField == false
        List resultMethods = panel.getMemberSpecifications(false);
        assertNotNull(resultMethods, "Method specifications should not be null");
        assertEquals(2, resultMethods.size(), "Should have exactly 2 method specifications");
        for (Object obj : resultMethods) {
            MemberSpecification spec = (MemberSpecification) obj;
            assertTrue(spec.name.startsWith("method"), "All returned specifications should be methods");
        }
    }

    /**
     * Test getMemberSpecifications with single field specification.
     * This covers lines 190, 196, 197 (one iteration), 199-200, 202, 204, 208.
     */
    @Test
    public void testGetMemberSpecificationsWithSingleField() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        List<MemberSpecification> fieldSpecs = new ArrayList<>();
        fieldSpecs.add(new MemberSpecification(
            AccessConstants.PUBLIC, 0, null, "singleField", "I"
        ));

        panel.setMemberSpecifications(fieldSpecs, null);

        // Line 190: size = 1, Line 197: loop once
        List result = panel.getMemberSpecifications(true);
        assertNotNull(result, "Field specifications should not be null");
        assertEquals(1, result.size(), "Should have exactly 1 field specification");

        MemberSpecification spec = (MemberSpecification) result.get(0);
        assertEquals("singleField", spec.name, "Field name should match");
    }

    /**
     * Test getMemberSpecifications with single method specification.
     * This covers lines 190, 196, 197 (one iteration), 199-200, 202, 204, 208.
     */
    @Test
    public void testGetMemberSpecificationsWithSingleMethod() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        List<MemberSpecification> methodSpecs = new ArrayList<>();
        methodSpecs.add(new MemberSpecification(
            AccessConstants.PUBLIC, 0, null, "singleMethod", "()V"
        ));

        panel.setMemberSpecifications(null, methodSpecs);

        // Line 190: size = 1, Line 197: loop once
        List result = panel.getMemberSpecifications(false);
        assertNotNull(result, "Method specifications should not be null");
        assertEquals(1, result.size(), "Should have exactly 1 method specification");

        MemberSpecification spec = (MemberSpecification) result.get(0);
        assertEquals("singleMethod", spec.name, "Method name should match");
    }

    /**
     * Test getMemberSpecifications with many specifications.
     * This covers line 197 with many iterations.
     */
    @Test
    public void testGetMemberSpecificationsWithManySpecifications() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        List<MemberSpecification> fieldSpecs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            fieldSpecs.add(new MemberSpecification(
                AccessConstants.PUBLIC, 0, null, "field" + i, "I"
            ));
        }

        List<MemberSpecification> methodSpecs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            methodSpecs.add(new MemberSpecification(
                AccessConstants.PUBLIC, 0, null, "method" + i, "()V"
            ));
        }

        panel.setMemberSpecifications(fieldSpecs, methodSpecs);

        // Line 190: size = 20, Line 197: loop 20 times, Line 202: filter by isField
        List resultFields = panel.getMemberSpecifications(true);
        assertEquals(10, resultFields.size(), "Should have 10 field specifications");

        List resultMethods = panel.getMemberSpecifications(false);
        assertEquals(10, resultMethods.size(), "Should have 10 method specifications");
    }

    /**
     * Test getMemberSpecifications preserves order.
     * This verifies that line 197 iterates in order and line 204 maintains that order.
     */
    @Test
    public void testGetMemberSpecificationsPreservesOrder() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        List<MemberSpecification> fieldSpecs = new ArrayList<>();
        fieldSpecs.add(new MemberSpecification(
            AccessConstants.PUBLIC, 0, null, "field1", "I"
        ));
        fieldSpecs.add(new MemberSpecification(
            AccessConstants.PRIVATE, 0, null, "field2", "J"
        ));
        fieldSpecs.add(new MemberSpecification(
            AccessConstants.PROTECTED, 0, null, "field3", "D"
        ));

        panel.setMemberSpecifications(fieldSpecs, null);

        List result = panel.getMemberSpecifications(true);
        assertEquals(3, result.size(), "Should have 3 field specifications");

        assertEquals("field1", ((MemberSpecification) result.get(0)).name, "First field should be field1");
        assertEquals("field2", ((MemberSpecification) result.get(1)).name, "Second field should be field2");
        assertEquals("field3", ((MemberSpecification) result.get(2)).name, "Third field should be field3");
    }

    /**
     * Test getMemberSpecifications with mixed specifications returns only requested type.
     * This tests line 202 filtering when both types are present.
     */
    @Test
    public void testGetMemberSpecificationsReturnsOnlyRequestedType() {
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

        panel.setMemberSpecifications(fieldSpecs, methodSpecs);

        // Request fields only - line 202 should skip methods
        List resultFields = panel.getMemberSpecifications(true);
        assertEquals(1, resultFields.size(), "Should have only 1 field specification");
        assertEquals("field1", ((MemberSpecification) resultFields.get(0)).name, "Should return field");

        // Request methods only - line 202 should skip fields
        List resultMethods = panel.getMemberSpecifications(false);
        assertEquals(1, resultMethods.size(), "Should have only 1 method specification");
        assertEquals("method1", ((MemberSpecification) resultMethods.get(0)).name, "Should return method");
    }

    /**
     * Test getMemberSpecifications when no matching type exists.
     * This tests line 202 when all items are skipped.
     */
    @Test
    public void testGetMemberSpecificationsWhenNoMatchingType() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        // Only add fields
        List<MemberSpecification> fieldSpecs = new ArrayList<>();
        fieldSpecs.add(new MemberSpecification(
            AccessConstants.PUBLIC, 0, null, "field1", "I"
        ));

        panel.setMemberSpecifications(fieldSpecs, null);

        // Request methods - line 202 should skip all items, but still return empty list not null
        List resultMethods = panel.getMemberSpecifications(false);
        // The list is created (line 196) but no items match (line 202 false), so we get empty list
        // Actually, looking at the implementation, it returns the list even if empty (line 208)
        assertNotNull(resultMethods, "Should return a list even if empty");
        assertEquals(0, resultMethods.size(), "Should have 0 method specifications");
    }

    /**
     * Test getMemberSpecifications after clearing the panel.
     * This tests line 191 after setMemberSpecifications(null, null).
     */
    @Test
    public void testGetMemberSpecificationsAfterClearing() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        // Add some specifications
        List<MemberSpecification> fieldSpecs = new ArrayList<>();
        fieldSpecs.add(new MemberSpecification(
            AccessConstants.PUBLIC, 0, null, "field1", "I"
        ));
        panel.setMemberSpecifications(fieldSpecs, null);

        // Verify they exist
        assertNotNull(panel.getMemberSpecifications(true), "Should have field specifications");

        // Clear the panel
        panel.setMemberSpecifications(null, null);

        // Line 190: size = 0, Line 191: true, Line 193: return null
        List result = panel.getMemberSpecifications(true);
        assertNull(result, "Field specifications should be null after clearing");
    }

    /**
     * Test getMemberSpecifications called multiple times returns consistent results.
     * This verifies all lines execute consistently.
     */
    @Test
    public void testGetMemberSpecificationsConsistentResults() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        List<MemberSpecification> fieldSpecs = new ArrayList<>();
        fieldSpecs.add(new MemberSpecification(
            AccessConstants.PUBLIC, 0, null, "field1", "I"
        ));

        panel.setMemberSpecifications(fieldSpecs, null);

        // Call multiple times
        List result1 = panel.getMemberSpecifications(true);
        List result2 = panel.getMemberSpecifications(true);
        List result3 = panel.getMemberSpecifications(true);

        // All should return consistent results
        assertEquals(1, result1.size(), "First call should return 1 specification");
        assertEquals(1, result2.size(), "Second call should return 1 specification");
        assertEquals(1, result3.size(), "Third call should return 1 specification");

        // Content should be the same
        assertEquals(((MemberSpecification) result1.get(0)).name,
                     ((MemberSpecification) result2.get(0)).name,
                     "Results should be consistent");
    }

    /**
     * Test getMemberSpecifications preserves specification properties.
     * This verifies lines 199-200 and 204 correctly retrieve and add specifications.
     */
    @Test
    public void testGetMemberSpecificationsPreservesProperties() {
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

        // Line 199-200: get wrapper, Line 204: add wrapper.memberSpecification
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
     * Test getMemberSpecifications with specifications having null names.
     */
    @Test
    public void testGetMemberSpecificationsWithNullNames() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        List<MemberSpecification> fieldSpecs = new ArrayList<>();
        fieldSpecs.add(new MemberSpecification(
            AccessConstants.PUBLIC, 0, null, null, "I"
        ));

        panel.setMemberSpecifications(fieldSpecs, null);

        List result = panel.getMemberSpecifications(true);
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Should have 1 specification");
        assertNull(((MemberSpecification) result.get(0)).name, "Name should be null");
    }

    /**
     * Test getMemberSpecifications with specifications having null descriptors.
     */
    @Test
    public void testGetMemberSpecificationsWithNullDescriptors() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        List<MemberSpecification> methodSpecs = new ArrayList<>();
        methodSpecs.add(new MemberSpecification(
            AccessConstants.PUBLIC, 0, null, "method1", null
        ));

        panel.setMemberSpecifications(null, methodSpecs);

        List result = panel.getMemberSpecifications(false);
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Should have 1 specification");
        assertNull(((MemberSpecification) result.get(0)).descriptor, "Descriptor should be null");
    }

    /**
     * Test getMemberSpecifications with both parameters (true and false).
     * This ensures both branches of line 202 are tested.
     */
    @Test
    public void testGetMemberSpecificationsWithBothParameters() {
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

        panel.setMemberSpecifications(fieldSpecs, methodSpecs);

        // Test with isField = true (line 202: wrapper.isField == true)
        List resultFields = panel.getMemberSpecifications(true);
        assertNotNull(resultFields, "Fields should not be null");
        assertEquals(1, resultFields.size(), "Should have 1 field");

        // Test with isField = false (line 202: wrapper.isField == false)
        List resultMethods = panel.getMemberSpecifications(false);
        assertNotNull(resultMethods, "Methods should not be null");
        assertEquals(1, resultMethods.size(), "Should have 1 method");
    }

    /**
     * Test getMemberSpecifications returns different list instances.
     * This verifies line 196 creates a new ArrayList each time.
     */
    @Test
    public void testGetMemberSpecificationsReturnsDifferentInstances() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        List<MemberSpecification> fieldSpecs = new ArrayList<>();
        fieldSpecs.add(new MemberSpecification(
            AccessConstants.PUBLIC, 0, null, "field1", "I"
        ));

        panel.setMemberSpecifications(fieldSpecs, null);

        // Get the list twice - line 196 should create new ArrayList each time
        List result1 = panel.getMemberSpecifications(true);
        List result2 = panel.getMemberSpecifications(true);

        assertNotSame(result1, result2, "Should return different list instances");
        assertEquals(result1.size(), result2.size(), "But should have same content");
    }

    /**
     * Test getMemberSpecifications with interleaved field and method specifications.
     * This tests line 197 and 202 with mixed types in the list.
     */
    @Test
    public void testGetMemberSpecificationsWithInterleavedTypes() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        // Add specifications in mixed order
        List<MemberSpecification> fieldSpecs = new ArrayList<>();
        fieldSpecs.add(new MemberSpecification(AccessConstants.PUBLIC, 0, null, "field1", "I"));
        fieldSpecs.add(new MemberSpecification(AccessConstants.PUBLIC, 0, null, "field2", "J"));

        List<MemberSpecification> methodSpecs = new ArrayList<>();
        methodSpecs.add(new MemberSpecification(AccessConstants.PUBLIC, 0, null, "method1", "()V"));
        methodSpecs.add(new MemberSpecification(AccessConstants.PUBLIC, 0, null, "method2", "()I"));

        panel.setMemberSpecifications(fieldSpecs, methodSpecs);

        // Line 197: loops through all 4 items, Line 202: filters by isField
        List resultFields = panel.getMemberSpecifications(true);
        assertEquals(2, resultFields.size(), "Should filter and return only fields");

        List resultMethods = panel.getMemberSpecifications(false);
        assertEquals(2, resultMethods.size(), "Should filter and return only methods");
    }

    /**
     * Test complete coverage with all lines executed.
     */
    @Test
    public void testGetMemberSpecificationsCompleteCoverage() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        // Test empty case (lines 190, 191, 193)
        List emptyResult = panel.getMemberSpecifications(true);
        assertNull(emptyResult, "Should return null when empty");

        // Add specifications
        List<MemberSpecification> fieldSpecs = new ArrayList<>();
        fieldSpecs.add(new MemberSpecification(AccessConstants.PUBLIC, 0, null, "field1", "I"));
        fieldSpecs.add(new MemberSpecification(AccessConstants.PUBLIC, 0, null, "field2", "J"));

        List<MemberSpecification> methodSpecs = new ArrayList<>();
        methodSpecs.add(new MemberSpecification(AccessConstants.PUBLIC, 0, null, "method1", "()V"));

        panel.setMemberSpecifications(fieldSpecs, methodSpecs);

        // Test non-empty case (lines 190, 191, 196, 197, 199-200, 202, 204, 208)
        List resultFields = panel.getMemberSpecifications(true);
        assertNotNull(resultFields, "Should return list when not empty");
        assertEquals(2, resultFields.size(), "Should have 2 fields");

        List resultMethods = panel.getMemberSpecifications(false);
        assertNotNull(resultMethods, "Should return list when not empty");
        assertEquals(1, resultMethods.size(), "Should have 1 method");
    }

    /**
     * Test getMemberSpecifications doesn't throw exception with any valid state.
     */
    @Test
    public void testGetMemberSpecificationsNoException() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        // Empty panel
        assertDoesNotThrow(() -> {
            panel.getMemberSpecifications(true);
            panel.getMemberSpecifications(false);
        }, "getMemberSpecifications should not throw exception on empty panel");

        // With specifications
        List<MemberSpecification> fieldSpecs = new ArrayList<>();
        fieldSpecs.add(new MemberSpecification(AccessConstants.PUBLIC, 0, null, "field", "I"));
        panel.setMemberSpecifications(fieldSpecs, null);

        assertDoesNotThrow(() -> {
            panel.getMemberSpecifications(true);
            panel.getMemberSpecifications(false);
        }, "getMemberSpecifications should not throw exception with specifications");
    }

    /**
     * Test that line 196 creates ArrayList with correct initial capacity.
     * This is implicitly tested but worth noting explicitly.
     */
    @Test
    public void testGetMemberSpecificationsCreatesAppropriatelySizedList() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        List<MemberSpecification> fieldSpecs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            fieldSpecs.add(new MemberSpecification(AccessConstants.PUBLIC, 0, null, "field" + i, "I"));
        }

        panel.setMemberSpecifications(fieldSpecs, null);

        // Line 196: new ArrayList(size) where size = 5
        List result = panel.getMemberSpecifications(true);
        assertNotNull(result, "Result should not be null");
        assertEquals(5, result.size(), "Should have 5 specifications");
    }
}
