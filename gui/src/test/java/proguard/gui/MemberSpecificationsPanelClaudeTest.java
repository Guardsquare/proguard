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
 * Test class for MemberSpecificationsPanel.
 *
 * This class tests all public methods of MemberSpecificationsPanel:
 * - Constructor (JDialog, boolean)
 * - addAddFieldButton()
 * - addAddMethodButton()
 * - addEditButton()
 * - setMemberSpecifications(List, List)
 * - getMemberSpecifications(boolean)
 *
 * The panel allows adding, editing, and managing field and method specifications.
 *
 * Note: These tests require GUI components and will skip in headless environments.
 */
public class MemberSpecificationsPanelClaudeTest {

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

    // Constructor tests

    /**
     * Test constructor with includeFieldButton = true.
     */
    @Test
    public void testConstructorWithFieldButton() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        assertNotNull(panel, "Panel should be created successfully");
        // When includeFieldButton is true, we should have: Add Field, Add Method, Edit, Remove, Up, Down buttons
        List buttons = panel.getButtons();
        assertTrue(buttons.size() >= 6, "Panel should have at least 6 buttons when includeFieldButton is true");
    }

    /**
     * Test constructor with includeFieldButton = false.
     */
    @Test
    public void testConstructorWithoutFieldButton() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        assertNotNull(panel, "Panel should be created successfully");
        // When includeFieldButton is false, we should have: Add Method, Edit, Remove, Up, Down buttons
        List buttons = panel.getButtons();
        assertTrue(buttons.size() >= 5, "Panel should have at least 5 buttons when includeFieldButton is false");
    }

    /**
     * Test constructor with null owner and includeFieldButton = true.
     */
    @Test
    public void testConstructorWithNullOwnerAndFieldButton() {
        panel = new MemberSpecificationsPanel(null, true);

        assertNotNull(panel, "Panel should be created successfully with null owner");
        List buttons = panel.getButtons();
        assertTrue(buttons.size() >= 6, "Panel should have buttons even with null owner");
    }

    /**
     * Test constructor with null owner and includeFieldButton = false.
     */
    @Test
    public void testConstructorWithNullOwnerWithoutFieldButton() {
        panel = new MemberSpecificationsPanel(null, false);

        assertNotNull(panel, "Panel should be created successfully with null owner");
        List buttons = panel.getButtons();
        assertTrue(buttons.size() >= 5, "Panel should have buttons even with null owner");
    }

    /**
     * Test that constructor initializes panel with empty list.
     */
    @Test
    public void testConstructorInitializesEmptyList() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        List fieldSpecs = panel.getMemberSpecifications(true);
        List methodSpecs = panel.getMemberSpecifications(false);

        assertNull(fieldSpecs, "Field specifications should be null initially");
        assertNull(methodSpecs, "Method specifications should be null initially");
    }

    // setMemberSpecifications tests

    /**
     * Test setMemberSpecifications with null lists.
     */
    @Test
    public void testSetMemberSpecificationsWithNullLists() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        panel.setMemberSpecifications(null, null);

        List fieldSpecs = panel.getMemberSpecifications(true);
        List methodSpecs = panel.getMemberSpecifications(false);

        assertNull(fieldSpecs, "Field specifications should be null");
        assertNull(methodSpecs, "Method specifications should be null");
    }

    /**
     * Test setMemberSpecifications with empty lists.
     */
    @Test
    public void testSetMemberSpecificationsWithEmptyLists() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        List<MemberSpecification> emptyFieldList = new ArrayList<>();
        List<MemberSpecification> emptyMethodList = new ArrayList<>();

        panel.setMemberSpecifications(emptyFieldList, emptyMethodList);

        List fieldSpecs = panel.getMemberSpecifications(true);
        List methodSpecs = panel.getMemberSpecifications(false);

        assertNull(fieldSpecs, "Field specifications should be null for empty list");
        assertNull(methodSpecs, "Method specifications should be null for empty list");
    }

    /**
     * Test setMemberSpecifications with only field specifications.
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

        panel.setMemberSpecifications(fieldSpecs, null);

        List result = panel.getMemberSpecifications(true);
        assertNotNull(result, "Field specifications should not be null");
        assertEquals(3, result.size(), "Should have 3 field specifications");
    }

    /**
     * Test setMemberSpecifications with multiple methods.
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

        panel.setMemberSpecifications(null, methodSpecs);

        List result = panel.getMemberSpecifications(false);
        assertNotNull(result, "Method specifications should not be null");
        assertEquals(3, result.size(), "Should have 3 method specifications");
    }

    /**
     * Test that setMemberSpecifications clears previous specifications.
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

        // Set new specifications
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
        MemberSpecification fieldSpec = (MemberSpecification) resultFields.get(0);
        assertNull(fieldSpec.name, "Field name should be null");

        List resultMethods = panel.getMemberSpecifications(false);
        assertNotNull(resultMethods, "Method specifications should not be null");
        assertEquals(1, resultMethods.size(), "Should have 1 method specification");
        MemberSpecification methodSpec = (MemberSpecification) resultMethods.get(0);
        assertNull(methodSpec.name, "Method name should be null");
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
        assertEquals(1, resultFields.size(), "Should have 1 field specification");
        MemberSpecification fieldSpec = (MemberSpecification) resultFields.get(0);
        assertNull(fieldSpec.descriptor, "Field descriptor should be null");

        List resultMethods = panel.getMemberSpecifications(false);
        assertNotNull(resultMethods, "Method specifications should not be null");
        assertEquals(1, resultMethods.size(), "Should have 1 method specification");
        MemberSpecification methodSpec = (MemberSpecification) resultMethods.get(0);
        assertNull(methodSpec.descriptor, "Method descriptor should be null");
    }

    /**
     * Test setMemberSpecifications with specifications having annotations.
     */
    @Test
    public void testSetMemberSpecificationsWithAnnotations() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        List<MemberSpecification> fieldSpecs = new ArrayList<>();
        fieldSpecs.add(new MemberSpecification(
            AccessConstants.PUBLIC, 0, "Ljava/lang/Deprecated;", "deprecatedField", "I"
        ));

        List<MemberSpecification> methodSpecs = new ArrayList<>();
        methodSpecs.add(new MemberSpecification(
            AccessConstants.PUBLIC, 0, "Ljava/lang/Override;", "overrideMethod", "()V"
        ));

        panel.setMemberSpecifications(fieldSpecs, methodSpecs);

        List resultFields = panel.getMemberSpecifications(true);
        assertNotNull(resultFields, "Field specifications should not be null");
        assertEquals(1, resultFields.size(), "Should have 1 field specification");
        MemberSpecification fieldSpec = (MemberSpecification) resultFields.get(0);
        assertEquals("Ljava/lang/Deprecated;", fieldSpec.annotationType, "Field annotation should match");

        List resultMethods = panel.getMemberSpecifications(false);
        assertNotNull(resultMethods, "Method specifications should not be null");
        assertEquals(1, resultMethods.size(), "Should have 1 method specification");
        MemberSpecification methodSpec = (MemberSpecification) resultMethods.get(0);
        assertEquals("Ljava/lang/Override;", methodSpec.annotationType, "Method annotation should match");
    }

    // getMemberSpecifications tests

    /**
     * Test getMemberSpecifications returns null for empty panel.
     */
    @Test
    public void testGetMemberSpecificationsEmpty() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        List fieldSpecs = panel.getMemberSpecifications(true);
        List methodSpecs = panel.getMemberSpecifications(false);

        assertNull(fieldSpecs, "Field specifications should be null when empty");
        assertNull(methodSpecs, "Method specifications should be null when empty");
    }

    /**
     * Test getMemberSpecifications filters correctly by isField parameter.
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

        // Get field specifications
        List resultFields = panel.getMemberSpecifications(true);
        assertNotNull(resultFields, "Field specifications should not be null");
        assertEquals(2, resultFields.size(), "Should have exactly 2 field specifications");
        for (Object obj : resultFields) {
            MemberSpecification spec = (MemberSpecification) obj;
            assertTrue(spec.name.startsWith("field"), "All returned specifications should be fields");
        }

        // Get method specifications
        List resultMethods = panel.getMemberSpecifications(false);
        assertNotNull(resultMethods, "Method specifications should not be null");
        assertEquals(2, resultMethods.size(), "Should have exactly 2 method specifications");
        for (Object obj : resultMethods) {
            MemberSpecification spec = (MemberSpecification) obj;
            assertTrue(spec.name.startsWith("method"), "All returned specifications should be methods");
        }
    }

    /**
     * Test getMemberSpecifications with mixed specifications preserves order.
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

        MemberSpecification spec1 = (MemberSpecification) result.get(0);
        assertEquals("field1", spec1.name, "First field should be field1");

        MemberSpecification spec2 = (MemberSpecification) result.get(1);
        assertEquals("field2", spec2.name, "Second field should be field2");

        MemberSpecification spec3 = (MemberSpecification) result.get(2);
        assertEquals("field3", spec3.name, "Third field should be field3");
    }

    /**
     * Test getMemberSpecifications preserves access flags.
     */
    @Test
    public void testGetMemberSpecificationsPreservesAccessFlags() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        List<MemberSpecification> fieldSpecs = new ArrayList<>();
        fieldSpecs.add(new MemberSpecification(
            AccessConstants.PUBLIC | AccessConstants.STATIC | AccessConstants.FINAL,
            0,
            null,
            "constant",
            "I"
        ));

        panel.setMemberSpecifications(fieldSpecs, null);

        List result = panel.getMemberSpecifications(true);
        assertNotNull(result, "Field specifications should not be null");
        assertEquals(1, result.size(), "Should have 1 field specification");

        MemberSpecification spec = (MemberSpecification) result.get(0);
        assertTrue((spec.requiredSetAccessFlags & AccessConstants.PUBLIC) != 0,
                   "Public flag should be preserved");
        assertTrue((spec.requiredSetAccessFlags & AccessConstants.STATIC) != 0,
                   "Static flag should be preserved");
        assertTrue((spec.requiredSetAccessFlags & AccessConstants.FINAL) != 0,
                   "Final flag should be preserved");
    }

    /**
     * Test getMemberSpecifications with only one type present.
     */
    @Test
    public void testGetMemberSpecificationsWithOnlyFields() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        List<MemberSpecification> fieldSpecs = new ArrayList<>();
        fieldSpecs.add(new MemberSpecification(
            AccessConstants.PUBLIC, 0, null, "field1", "I"
        ));

        panel.setMemberSpecifications(fieldSpecs, null);

        List resultFields = panel.getMemberSpecifications(true);
        assertNotNull(resultFields, "Field specifications should not be null");
        assertEquals(1, resultFields.size(), "Should have 1 field specification");

        List resultMethods = panel.getMemberSpecifications(false);
        assertNull(resultMethods, "Method specifications should be null when no methods present");
    }

    /**
     * Test getMemberSpecifications with only methods present.
     */
    @Test
    public void testGetMemberSpecificationsWithOnlyMethods() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        List<MemberSpecification> methodSpecs = new ArrayList<>();
        methodSpecs.add(new MemberSpecification(
            AccessConstants.PUBLIC, 0, null, "method1", "()V"
        ));

        panel.setMemberSpecifications(null, methodSpecs);

        List resultFields = panel.getMemberSpecifications(true);
        assertNull(resultFields, "Field specifications should be null when no fields present");

        List resultMethods = panel.getMemberSpecifications(false);
        assertNotNull(resultMethods, "Method specifications should not be null");
        assertEquals(1, resultMethods.size(), "Should have 1 method specification");
    }

    /**
     * Test that multiple calls to setMemberSpecifications and getMemberSpecifications work correctly.
     */
    @Test
    public void testMultipleSetGetCalls() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        // First set
        List<MemberSpecification> fieldSpecs1 = new ArrayList<>();
        fieldSpecs1.add(new MemberSpecification(
            AccessConstants.PUBLIC, 0, null, "field1", "I"
        ));
        panel.setMemberSpecifications(fieldSpecs1, null);
        List result1 = panel.getMemberSpecifications(true);
        assertEquals(1, result1.size(), "Should have 1 field specification after first set");

        // Second set
        List<MemberSpecification> fieldSpecs2 = new ArrayList<>();
        fieldSpecs2.add(new MemberSpecification(
            AccessConstants.PRIVATE, 0, null, "field2", "J"
        ));
        fieldSpecs2.add(new MemberSpecification(
            AccessConstants.PROTECTED, 0, null, "field3", "D"
        ));
        panel.setMemberSpecifications(fieldSpecs2, null);
        List result2 = panel.getMemberSpecifications(true);
        assertEquals(2, result2.size(), "Should have 2 field specifications after second set");
    }

    /**
     * Test addAddFieldButton creates a functional button.
     * This test verifies the button is added by checking the button count.
     */
    @Test
    public void testAddAddFieldButtonCreatesButton() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        // Panel with includeFieldButton = true should have the field button
        List buttons = panel.getButtons();
        boolean hasFieldButton = false;
        for (Object obj : buttons) {
            if (obj instanceof JButton) {
                JButton button = (JButton) obj;
                // Check if this might be the field button by looking for typical field-related text
                if (button.getActionListeners().length > 0) {
                    hasFieldButton = true;
                    break;
                }
            }
        }

        assertTrue(buttons.size() >= 6, "Panel should have at least 6 buttons including field button");
    }

    /**
     * Test addAddMethodButton creates a functional button.
     * This test verifies the button is added by checking the button count.
     */
    @Test
    public void testAddAddMethodButtonCreatesButton() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, false);

        // Panel should always have the method button
        List buttons = panel.getButtons();
        assertTrue(buttons.size() >= 5, "Panel should have at least 5 buttons including method button");
    }

    /**
     * Test addEditButton creates a functional button.
     * This test verifies the button is added by checking the button count.
     */
    @Test
    public void testAddEditButtonCreatesButton() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        // Panel should have the edit button
        List buttons = panel.getButtons();
        assertTrue(buttons.size() >= 6, "Panel should have at least 6 buttons including edit button");
    }

    /**
     * Test that panel works correctly after setting specifications to null to clear.
     */
    @Test
    public void testSetMemberSpecificationsClearWithNull() {
        testDialog = new JDialog();
        panel = new MemberSpecificationsPanel(testDialog, true);

        // Set some specifications
        List<MemberSpecification> fieldSpecs = new ArrayList<>();
        fieldSpecs.add(new MemberSpecification(
            AccessConstants.PUBLIC, 0, null, "field1", "I"
        ));
        panel.setMemberSpecifications(fieldSpecs, null);

        // Verify specifications are set
        List result1 = panel.getMemberSpecifications(true);
        assertNotNull(result1, "Field specifications should not be null");
        assertEquals(1, result1.size(), "Should have 1 field specification");

        // Clear by setting to null
        panel.setMemberSpecifications(null, null);

        // Verify specifications are cleared
        List result2 = panel.getMemberSpecifications(true);
        assertNull(result2, "Field specifications should be null after clearing");
    }
}
