package proguard.ant;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.DataType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for KeepSpecificationElement default constructor.
 */
public class KeepSpecificationElementClaude_constructorTest {

    /**
     * Test that the default constructor successfully creates a KeepSpecificationElement instance.
     */
    @Test
    public void testDefaultConstructorCreatesInstance() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        assertNotNull(element, "KeepSpecificationElement should be created successfully");
    }

    /**
     * Test that the default constructor creates a valid DataType.
     * KeepSpecificationElement extends ClassSpecificationElement which extends DataType.
     */
    @Test
    public void testDefaultConstructorCreatesValidDataType() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        // Verify it behaves as a DataType by checking we can use DataType methods
        assertFalse(element.isReference(), "Newly created element should not be a reference");
        assertInstanceOf(DataType.class, element, "KeepSpecificationElement should be a DataType");
    }

    /**
     * Test that the default constructor creates a valid ClassSpecificationElement.
     */
    @Test
    public void testDefaultConstructorCreatesValidClassSpecificationElement() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        assertInstanceOf(ClassSpecificationElement.class, element,
            "KeepSpecificationElement should be a ClassSpecificationElement");
    }

    /**
     * Test that the constructor initializes the element to work with Ant projects.
     */
    @Test
    public void testDefaultConstructorWorksWithAntProject() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        Project project = new Project();
        project.init();

        // DataType provides setProject method
        element.setProject(project);

        assertSame(project, element.getProject(), "Project should be settable on the element");
    }

    /**
     * Test that multiple instances can be created independently.
     */
    @Test
    public void testDefaultConstructorCreatesIndependentInstances() {
        KeepSpecificationElement element1 = new KeepSpecificationElement();
        KeepSpecificationElement element2 = new KeepSpecificationElement();

        assertNotNull(element1, "First element should be created");
        assertNotNull(element2, "Second element should be created");
        assertNotSame(element1, element2, "Elements should be different instances");
    }

    /**
     * Test that the constructor allows setting includedescriptorclasses.
     * This verifies that the internal state is properly initialized.
     */
    @Test
    public void testDefaultConstructorAllowsSettingIncludeDescriptorClasses() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        // Should be able to set includedescriptorclasses without errors
        assertDoesNotThrow(() -> element.setIncludedescriptorclasses(true),
            "Should be able to set includedescriptorclasses");
        assertDoesNotThrow(() -> element.setIncludedescriptorclasses(false),
            "Should be able to set includedescriptorclasses to false");
    }

    /**
     * Test that the constructor allows setting includecode.
     * This verifies that the internal state is properly initialized.
     */
    @Test
    public void testDefaultConstructorAllowsSettingIncludeCode() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        // Should be able to set includecode without errors
        assertDoesNotThrow(() -> element.setIncludecode(true),
            "Should be able to set includecode");
        assertDoesNotThrow(() -> element.setIncludecode(false),
            "Should be able to set includecode to false");
    }

    /**
     * Test that the constructor allows setting allowshrinking.
     * This verifies that the internal state is properly initialized.
     */
    @Test
    public void testDefaultConstructorAllowsSettingAllowShrinking() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        // Should be able to set allowshrinking without errors
        assertDoesNotThrow(() -> element.setAllowshrinking(true),
            "Should be able to set allowshrinking");
        assertDoesNotThrow(() -> element.setAllowshrinking(false),
            "Should be able to set allowshrinking to false");
    }

    /**
     * Test that the constructor allows setting allowoptimization.
     * This verifies that the internal state is properly initialized.
     */
    @Test
    public void testDefaultConstructorAllowsSettingAllowOptimization() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        // Should be able to set allowoptimization without errors
        assertDoesNotThrow(() -> element.setAllowoptimization(true),
            "Should be able to set allowoptimization");
        assertDoesNotThrow(() -> element.setAllowoptimization(false),
            "Should be able to set allowoptimization to false");
    }

    /**
     * Test that the constructor allows setting allowobfuscation.
     * This verifies that the internal state is properly initialized.
     */
    @Test
    public void testDefaultConstructorAllowsSettingAllowObfuscation() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        // Should be able to set allowobfuscation without errors
        assertDoesNotThrow(() -> element.setAllowobfuscation(true),
            "Should be able to set allowobfuscation");
        assertDoesNotThrow(() -> element.setAllowobfuscation(false),
            "Should be able to set allowobfuscation to false");
    }

    /**
     * Test that a newly constructed element can be used with the appendTo method.
     */
    @Test
    public void testDefaultConstructorAllowsAppendTo() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");

        List keepSpecifications = new ArrayList();

        // Should be able to call appendTo without errors
        assertDoesNotThrow(() -> element.appendTo(keepSpecifications, true, true, false),
            "Should be able to call appendTo");

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should be populated after appendTo");
    }

    /**
     * Test that the constructor initializes the element to work with references.
     */
    @Test
    public void testDefaultConstructorAllowsReferences() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        Project project = new Project();
        project.init();
        element.setProject(project);

        // Should be able to set a reference without errors
        assertDoesNotThrow(() -> element.setRefid(new org.apache.tools.ant.types.Reference(project, "test-ref")),
            "Should be able to set reference");
    }

    /**
     * Test that a newly constructed element can be configured with multiple setters
     * and used with appendTo.
     */
    @Test
    public void testDefaultConstructorAllowsMultipleSetters() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        // Should be able to call all setters without errors
        assertDoesNotThrow(() -> {
            element.setIncludedescriptorclasses(true);
            element.setIncludecode(true);
            element.setAllowshrinking(false);
            element.setAllowoptimization(false);
            element.setAllowobfuscation(false);
            element.setName("com.example.**");
        }, "Should be able to call multiple setters");

        List keepSpecifications = new ArrayList();
        assertDoesNotThrow(() -> element.appendTo(keepSpecifications, true, true, false),
            "Should be able to call appendTo after setting multiple properties");

        assertFalse(keepSpecifications.isEmpty(), "Keep specifications list should be populated");
    }

    /**
     * Test that multiple KeepSpecificationElement instances can be created and configured independently.
     */
    @Test
    public void testDefaultConstructorCreatesIndependentlyConfigurableInstances() {
        KeepSpecificationElement element1 = new KeepSpecificationElement();
        KeepSpecificationElement element2 = new KeepSpecificationElement();

        element1.setName("com.example.package1.**");
        element1.setAllowshrinking(true);

        element2.setName("com.example.package2.**");
        element2.setAllowobfuscation(true);

        List list1 = new ArrayList();
        List list2 = new ArrayList();

        element1.appendTo(list1, true, true, false);
        element2.appendTo(list2, true, true, false);

        // Verify that the elements are independent
        assertFalse(list1.isEmpty(), "First list should be populated");
        assertFalse(list2.isEmpty(), "Second list should be populated");
        assertNotEquals(list1, list2, "Different elements should produce different specifications");
    }

    /**
     * Test that a newly constructed element without a project can still be used.
     */
    @Test
    public void testDefaultConstructorWorksWithoutProject() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        // Don't set a project
        assertNull(element.getProject(), "Project should be null by default");

        element.setName("com.example.**");
        List keepSpecifications = new ArrayList();

        // Should still work without a project
        assertDoesNotThrow(() -> element.appendTo(keepSpecifications, true, true, false),
            "Should work without a project set");
    }

    /**
     * Test that the constructor creates an element that can be checked for reference status.
     */
    @Test
    public void testDefaultConstructorIsNotReference() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        assertFalse(element.isReference(), "Newly constructed element should not be a reference");
    }

    /**
     * Test that the constructor creates an element that can be used immediately
     * after construction without any configuration (with minimal setup).
     */
    @Test
    public void testDefaultConstructorAllowsImmediateUse() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        List keepSpecifications = new ArrayList();

        // Should be able to use element immediately after construction
        assertDoesNotThrow(() -> element.appendTo(keepSpecifications, true, true, false),
            "Should be able to use element immediately after construction");
    }

    /**
     * Test that the constructor allows the element to inherit from ClassSpecificationElement
     * and use parent class methods.
     */
    @Test
    public void testDefaultConstructorInheritsParentMethods() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        // Should be able to use methods from ClassSpecificationElement
        assertDoesNotThrow(() -> element.setAccess("public"),
            "Should be able to set access modifier");
        assertDoesNotThrow(() -> element.setType("class"),
            "Should be able to set type");
        assertDoesNotThrow(() -> element.setName("com.example.MyClass"),
            "Should be able to set name");
    }

    /**
     * Test that a newly constructed element can be used with different appendTo parameters.
     */
    @Test
    public void testDefaultConstructorHandlesDifferentAppendToParameters() {
        KeepSpecificationElement element = new KeepSpecificationElement();
        element.setName("com.example.**");

        List list1 = new ArrayList();
        List list2 = new ArrayList();
        List list3 = new ArrayList();

        // Test with different parameter combinations
        assertDoesNotThrow(() -> {
            element.appendTo(list1, true, true, true);
            element.appendTo(list2, false, false, false);
            element.appendTo(list3, true, false, true);
        }, "Should handle different appendTo parameter combinations");

        assertFalse(list1.isEmpty(), "List with all true should be populated");
        assertFalse(list2.isEmpty(), "List with all false should be populated");
        assertFalse(list3.isEmpty(), "List with mixed values should be populated");
    }

    /**
     * Test that the constructor creates an element that can be configured
     * with both parent and child class setters.
     */
    @Test
    public void testDefaultConstructorAllowsParentAndChildSetters() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        // Child class setters
        assertDoesNotThrow(() -> {
            element.setIncludedescriptorclasses(true);
            element.setIncludecode(true);
            element.setAllowshrinking(true);
            element.setAllowoptimization(true);
            element.setAllowobfuscation(true);
        }, "Should be able to use child class setters");

        // Parent class setters
        assertDoesNotThrow(() -> {
            element.setAccess("public");
            element.setAnnotation("com.example.MyAnnotation");
            element.setType("class");
            element.setName("com.example.MyClass");
            element.setExtends("com.example.BaseClass");
        }, "Should be able to use parent class setters");
    }

    /**
     * Test that the constructor creates an element that works correctly
     * with the setImplements method (inherited from parent).
     */
    @Test
    public void testDefaultConstructorAllowsSetImplements() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        assertDoesNotThrow(() -> element.setImplements("com.example.MyInterface"),
            "Should be able to call setImplements");
    }

    /**
     * Test that the constructor creates an element that works correctly
     * with the setExtendsannotation method (inherited from parent).
     */
    @Test
    public void testDefaultConstructorAllowsSetExtendsAnnotation() {
        KeepSpecificationElement element = new KeepSpecificationElement();

        assertDoesNotThrow(() -> element.setExtendsannotation("com.example.MyAnnotation"),
            "Should be able to call setExtendsannotation");
    }
}
