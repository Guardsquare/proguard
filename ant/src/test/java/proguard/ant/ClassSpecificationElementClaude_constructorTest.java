package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ClassSpecificationElement default constructor.
 */
public class ClassSpecificationElementClaude_constructorTest {

    /**
     * Test that the default constructor successfully creates a ClassSpecificationElement.
     */
    @Test
    public void testDefaultConstructorCreatesInstance() {
        ClassSpecificationElement element = new ClassSpecificationElement();

        assertNotNull(element, "ClassSpecificationElement should be created successfully");
    }

    /**
     * Test that the default constructor creates a valid DataType.
     * ClassSpecificationElement extends DataType, so it should behave as one.
     */
    @Test
    public void testDefaultConstructorCreatesValidDataType() {
        ClassSpecificationElement element = new ClassSpecificationElement();

        // Verify it behaves as a DataType by checking we can use DataType methods
        assertFalse(element.isReference(), "Newly created element should not be a reference");
    }

    /**
     * Test that the constructor initializes the element to work with Ant projects.
     */
    @Test
    public void testDefaultConstructorWorksWithAntProject() {
        ClassSpecificationElement element = new ClassSpecificationElement();
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
        ClassSpecificationElement element1 = new ClassSpecificationElement();
        ClassSpecificationElement element2 = new ClassSpecificationElement();

        assertNotNull(element1, "First element should be created");
        assertNotNull(element2, "Second element should be created");
        assertNotSame(element1, element2, "Elements should be different instances");
    }

    /**
     * Test that the constructor initializes the element to accept access modifiers.
     */
    @Test
    public void testDefaultConstructorAllowsAccessModifierConfiguration() {
        ClassSpecificationElement element = new ClassSpecificationElement();

        // Should be able to set access modifiers without errors
        assertDoesNotThrow(() -> element.setAccess("public"), "Should be able to set access");
        assertDoesNotThrow(() -> element.setAccess("public,final"), "Should be able to set multiple access flags");
    }

    /**
     * Test that the constructor initializes the element to accept annotations.
     */
    @Test
    public void testDefaultConstructorAllowsAnnotationConfiguration() {
        ClassSpecificationElement element = new ClassSpecificationElement();

        // Should be able to set annotations without errors
        assertDoesNotThrow(() -> element.setAnnotation("MyAnnotation"), "Should be able to set annotation");
    }

    /**
     * Test that the constructor initializes the element to accept type specifications.
     */
    @Test
    public void testDefaultConstructorAllowsTypeConfiguration() {
        ClassSpecificationElement element = new ClassSpecificationElement();

        // Should be able to set type without errors
        assertDoesNotThrow(() -> element.setType("class"), "Should be able to set type to class");
        assertDoesNotThrow(() -> element.setType("interface"), "Should be able to set type to interface");
        assertDoesNotThrow(() -> element.setType("enum"), "Should be able to set type to enum");
    }

    /**
     * Test that the constructor initializes the element to accept name specifications.
     */
    @Test
    public void testDefaultConstructorAllowsNameConfiguration() {
        ClassSpecificationElement element = new ClassSpecificationElement();

        // Should be able to set name without errors
        assertDoesNotThrow(() -> element.setName("com.example.MyClass"), "Should be able to set name");
        assertDoesNotThrow(() -> element.setName("*"), "Should be able to set wildcard name");
    }

    /**
     * Test that the constructor initializes the element to accept extends configurations.
     */
    @Test
    public void testDefaultConstructorAllowsExtendsConfiguration() {
        ClassSpecificationElement element = new ClassSpecificationElement();

        // Should be able to set extends without errors
        assertDoesNotThrow(() -> element.setExtends("java.lang.Object"), "Should be able to set extends");
        assertDoesNotThrow(() -> element.setExtendsannotation("MyAnnotation"), "Should be able to set extends annotation");
    }

    /**
     * Test that the constructor initializes the element to accept implements configurations.
     */
    @Test
    public void testDefaultConstructorAllowsImplementsConfiguration() {
        ClassSpecificationElement element = new ClassSpecificationElement();

        // Should be able to set implements without errors
        assertDoesNotThrow(() -> element.setImplements("java.io.Serializable"), "Should be able to set implements");
    }

    /**
     * Test that the constructor initializes the element to work with field specifications.
     */
    @Test
    public void testDefaultConstructorAllowsFieldSpecifications() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        MemberSpecificationElement fieldSpec = new MemberSpecificationElement();

        // Should be able to add field specifications without errors
        assertDoesNotThrow(() -> element.addConfiguredField(fieldSpec), "Should be able to add field specification");
    }

    /**
     * Test that the constructor initializes the element to work with method specifications.
     */
    @Test
    public void testDefaultConstructorAllowsMethodSpecifications() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        MemberSpecificationElement methodSpec = new MemberSpecificationElement();

        // Should be able to add method specifications without errors
        assertDoesNotThrow(() -> element.addConfiguredMethod(methodSpec), "Should be able to add method specification");
    }

    /**
     * Test that the constructor initializes the element to work with constructor specifications.
     */
    @Test
    public void testDefaultConstructorAllowsConstructorSpecifications() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        MemberSpecificationElement constructorSpec = new MemberSpecificationElement();

        // Should be able to add constructor specifications without errors
        assertDoesNotThrow(() -> element.addConfiguredConstructor(constructorSpec), "Should be able to add constructor specification");
    }

    /**
     * Test that a newly constructed element can append to a list of class specifications.
     */
    @Test
    public void testDefaultConstructorAllowsAppendToList() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.MyClass");

        List classSpecifications = new ArrayList();

        // Should be able to append without errors
        assertDoesNotThrow(() -> element.appendTo(classSpecifications), "Should be able to append to list");
        assertEquals(1, classSpecifications.size(), "List should contain one specification after append");
    }

    /**
     * Test that the constructor creates an element that generates valid ClassSpecification objects.
     */
    @Test
    public void testDefaultConstructorCreatesValidSpecification() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.MyClass");
        element.setAccess("public");
        element.setType("class");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        assertEquals(1, classSpecifications.size(), "Should have created one specification");
        assertNotNull(classSpecifications.get(0), "The specification should not be null");
    }

    /**
     * Test that the constructor creates an element that can be configured fully.
     */
    @Test
    public void testDefaultConstructorAllowsFullConfiguration() {
        ClassSpecificationElement element = new ClassSpecificationElement();

        // Configure all properties
        assertDoesNotThrow(() -> {
            element.setAccess("public,final");
            element.setAnnotation("MyAnnotation");
            element.setType("class");
            element.setName("com.example.MyClass");
            element.setExtendsannotation("MyExtendsAnnotation");
            element.setExtends("com.example.BaseClass");

            MemberSpecificationElement field = new MemberSpecificationElement();
            element.addConfiguredField(field);

            MemberSpecificationElement method = new MemberSpecificationElement();
            element.addConfiguredMethod(method);

            MemberSpecificationElement constructor = new MemberSpecificationElement();
            element.addConfiguredConstructor(constructor);
        }, "Should be able to configure all properties");
    }

    /**
     * Test that constructor creates element that can handle wildcard class names.
     */
    @Test
    public void testDefaultConstructorHandlesWildcardClassName() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("*");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        assertEquals(1, classSpecifications.size(), "Should handle wildcard class name");
    }

    /**
     * Test that constructor creates element that can work without any configuration.
     */
    @Test
    public void testDefaultConstructorWorksWithoutConfiguration() {
        ClassSpecificationElement element = new ClassSpecificationElement();

        List classSpecifications = new ArrayList();

        // Should be able to append even without any configuration
        assertDoesNotThrow(() -> element.appendTo(classSpecifications), "Should work without configuration");
        assertEquals(1, classSpecifications.size(), "Should create a specification even without configuration");
    }
}
