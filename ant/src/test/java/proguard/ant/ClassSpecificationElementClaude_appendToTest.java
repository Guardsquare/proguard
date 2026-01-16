package proguard.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Reference;
import org.junit.jupiter.api.Test;
import proguard.ClassSpecification;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ClassSpecificationElement.appendTo(List) method.
 */
public class ClassSpecificationElementClaude_appendToTest {

    /**
     * Test that appendTo adds one ClassSpecification to an empty list.
     */
    @Test
    public void testAppendToEmptyList() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.MyClass");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        assertEquals(1, classSpecifications.size(), "Should add exactly one specification");
        assertNotNull(classSpecifications.get(0), "The specification should not be null");
        assertTrue(classSpecifications.get(0) instanceof ClassSpecification,
            "Should add a ClassSpecification instance");
    }

    /**
     * Test that appendTo adds to an existing non-empty list.
     */
    @Test
    public void testAppendToNonEmptyList() {
        ClassSpecificationElement element1 = new ClassSpecificationElement();
        element1.setName("com.example.FirstClass");

        ClassSpecificationElement element2 = new ClassSpecificationElement();
        element2.setName("com.example.SecondClass");

        List classSpecifications = new ArrayList();
        element1.appendTo(classSpecifications);
        element2.appendTo(classSpecifications);

        assertEquals(2, classSpecifications.size(), "Should have two specifications");
        assertNotSame(classSpecifications.get(0), classSpecifications.get(1),
            "Should add different specification instances");
    }

    /**
     * Test that appendTo works with an element that has no configuration.
     */
    @Test
    public void testAppendToWithUnconfiguredElement() {
        ClassSpecificationElement element = new ClassSpecificationElement();

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        assertEquals(1, classSpecifications.size(), "Should add specification even without configuration");
        assertNotNull(classSpecifications.get(0), "The specification should not be null");
    }

    /**
     * Test that appendTo creates a ClassSpecification with the configured name.
     */
    @Test
    public void testAppendToWithName() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.className, "ClassSpecification should have a class name");
        assertEquals("com/example/TestClass", spec.className,
            "Class name should be converted to internal format");
    }

    /**
     * Test that appendTo handles wildcard class names (backward compatibility).
     */
    @Test
    public void testAppendToWithWildcardName() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("*");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNull(spec.className, "Wildcard '*' should be converted to null for any class matching");
    }

    /**
     * Test that appendTo creates a ClassSpecification with access flags.
     */
    @Test
    public void testAppendToWithAccessFlags() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setAccess("public");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertTrue(spec.requiredSetAccessFlags != 0 || spec.requiredUnsetAccessFlags != 0,
            "Should set access flags");
    }

    /**
     * Test that appendTo creates a ClassSpecification with multiple access flags.
     */
    @Test
    public void testAppendToWithMultipleAccessFlags() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setAccess("public,final");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertTrue(spec.requiredSetAccessFlags != 0, "Should have set access flags");
    }

    /**
     * Test that appendTo creates a ClassSpecification with annotation type.
     */
    @Test
    public void testAppendToWithAnnotation() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setAnnotation("MyAnnotation");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.annotationType, "Should have annotation type");
        assertEquals("LMyAnnotation;", spec.annotationType,
            "Annotation should be converted to internal type format");
    }

    /**
     * Test that appendTo creates a ClassSpecification with class type.
     */
    @Test
    public void testAppendToWithClassType() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setType("class");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec, "Should create specification with class type");
    }

    /**
     * Test that appendTo creates a ClassSpecification with interface type.
     */
    @Test
    public void testAppendToWithInterfaceType() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestInterface");
        element.setType("interface");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertTrue(spec.requiredSetAccessFlags != 0 || spec.requiredUnsetAccessFlags != 0,
            "Should set interface access flags");
    }

    /**
     * Test that appendTo creates a ClassSpecification with enum type.
     */
    @Test
    public void testAppendToWithEnumType() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestEnum");
        element.setType("enum");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertTrue(spec.requiredSetAccessFlags != 0 || spec.requiredUnsetAccessFlags != 0,
            "Should set enum access flags");
    }

    /**
     * Test that appendTo creates a ClassSpecification with extends clause.
     */
    @Test
    public void testAppendToWithExtends() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtends("com.example.BaseClass");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Should have extends class name");
        assertEquals("com/example/BaseClass", spec.extendsClassName,
            "Extends class name should be converted to internal format");
    }

    /**
     * Test that appendTo creates a ClassSpecification with implements clause.
     */
    @Test
    public void testAppendToWithImplements() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setImplements("java.io.Serializable");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Should have implements class name");
        assertEquals("java/io/Serializable", spec.extendsClassName,
            "Implements class name should be converted to internal format");
    }

    /**
     * Test that appendTo creates a ClassSpecification with extends annotation.
     */
    @Test
    public void testAppendToWithExtendsAnnotation() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtendsannotation("MyAnnotation");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsAnnotationType, "Should have extends annotation type");
        assertEquals("LMyAnnotation;", spec.extendsAnnotationType,
            "Extends annotation should be converted to internal type format");
    }

    /**
     * Test that appendTo creates a ClassSpecification with field specifications.
     */
    @Test
    public void testAppendToWithFieldSpecifications() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");

        MemberSpecificationElement field = new MemberSpecificationElement();
        field.setName("myField");
        element.addConfiguredField(field);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.fieldSpecifications, "Should have field specifications");
        assertEquals(1, spec.fieldSpecifications.size(), "Should have one field specification");
    }

    /**
     * Test that appendTo creates a ClassSpecification with multiple field specifications.
     */
    @Test
    public void testAppendToWithMultipleFieldSpecifications() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");

        MemberSpecificationElement field1 = new MemberSpecificationElement();
        field1.setName("field1");
        element.addConfiguredField(field1);

        MemberSpecificationElement field2 = new MemberSpecificationElement();
        field2.setName("field2");
        element.addConfiguredField(field2);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.fieldSpecifications, "Should have field specifications");
        assertEquals(2, spec.fieldSpecifications.size(), "Should have two field specifications");
    }

    /**
     * Test that appendTo creates a ClassSpecification with method specifications.
     */
    @Test
    public void testAppendToWithMethodSpecifications() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");

        MemberSpecificationElement method = new MemberSpecificationElement();
        method.setName("myMethod");
        element.addConfiguredMethod(method);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.methodSpecifications, "Should have method specifications");
        assertEquals(1, spec.methodSpecifications.size(), "Should have one method specification");
    }

    /**
     * Test that appendTo creates a ClassSpecification with constructor specifications.
     */
    @Test
    public void testAppendToWithConstructorSpecifications() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");

        MemberSpecificationElement constructor = new MemberSpecificationElement();
        element.addConfiguredConstructor(constructor);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.methodSpecifications, "Should have method specifications (constructors are methods)");
        assertEquals(1, spec.methodSpecifications.size(), "Should have one constructor specification");
    }

    /**
     * Test that appendTo creates a ClassSpecification with both fields and methods.
     */
    @Test
    public void testAppendToWithFieldsAndMethods() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");

        MemberSpecificationElement field = new MemberSpecificationElement();
        field.setName("myField");
        element.addConfiguredField(field);

        MemberSpecificationElement method = new MemberSpecificationElement();
        method.setName("myMethod");
        element.addConfiguredMethod(method);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.fieldSpecifications, "Should have field specifications");
        assertEquals(1, spec.fieldSpecifications.size(), "Should have one field specification");
        assertNotNull(spec.methodSpecifications, "Should have method specifications");
        assertEquals(1, spec.methodSpecifications.size(), "Should have one method specification");
    }

    /**
     * Test that appendTo creates a fully configured ClassSpecification.
     */
    @Test
    public void testAppendToWithFullConfiguration() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setAccess("public,final");
        element.setAnnotation("MyAnnotation");
        element.setType("class");
        element.setExtends("com.example.BaseClass");
        element.setExtendsannotation("BaseAnnotation");

        MemberSpecificationElement field = new MemberSpecificationElement();
        field.setName("myField");
        element.addConfiguredField(field);

        MemberSpecificationElement method = new MemberSpecificationElement();
        method.setName("myMethod");
        element.addConfiguredMethod(method);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.className, "Should have class name");
        assertNotNull(spec.annotationType, "Should have annotation type");
        assertNotNull(spec.extendsClassName, "Should have extends class name");
        assertNotNull(spec.extendsAnnotationType, "Should have extends annotation type");
        assertNotNull(spec.fieldSpecifications, "Should have field specifications");
        assertNotNull(spec.methodSpecifications, "Should have method specifications");
    }

    /**
     * Test that appendTo throws exception for invalid access modifier.
     */
    @Test
    public void testAppendToWithInvalidAccessModifier() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setAccess("invalid");

        List classSpecifications = new ArrayList();

        assertThrows(BuildException.class, () -> element.appendTo(classSpecifications),
            "Should throw BuildException for invalid access modifier");
    }

    /**
     * Test that appendTo throws exception for invalid type.
     */
    @Test
    public void testAppendToWithInvalidType() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setType("invalid");

        List classSpecifications = new ArrayList();

        assertThrows(BuildException.class, () -> element.appendTo(classSpecifications),
            "Should throw BuildException for invalid type");
    }

    /**
     * Test that appendTo can be called multiple times on the same element.
     */
    @Test
    public void testAppendToCalledMultipleTimes() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);
        element.appendTo(classSpecifications);

        assertEquals(2, classSpecifications.size(), "Should add specification twice");
    }

    /**
     * Test that appendTo with a reference resolves the reference before appending.
     */
    @Test
    public void testAppendToWithReference() {
        Project project = new Project();
        project.init();

        // Create the referenced element
        ClassSpecificationElement referencedElement = new ClassSpecificationElement();
        referencedElement.setProject(project);
        referencedElement.setName("com.example.ReferencedClass");
        project.addReference("myref", referencedElement);

        // Create an element that references it
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setProject(project);
        element.setRefid(new Reference(project, "myref"));

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        assertEquals(1, classSpecifications.size(), "Should add one specification");
        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.className, "Should have class name from referenced element");
        assertEquals("com/example/ReferencedClass", spec.className,
            "Should use configuration from referenced element");
    }

    /**
     * Test that appendTo preserves the original list contents.
     */
    @Test
    public void testAppendToPreservesExistingContents() {
        List classSpecifications = new ArrayList();
        ClassSpecification existingSpec = new ClassSpecification();
        classSpecifications.add(existingSpec);

        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.appendTo(classSpecifications);

        assertEquals(2, classSpecifications.size(), "Should have two specifications");
        assertSame(existingSpec, classSpecifications.get(0), "Should preserve existing specification");
    }

    /**
     * Test that appendTo works with package patterns.
     */
    @Test
    public void testAppendToWithPackagePattern() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.**");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.className, "Should have class name pattern");
        assertTrue(spec.className.contains("**"), "Should preserve wildcard pattern");
    }

    /**
     * Test that appendTo works with class name wildcards.
     */
    @Test
    public void testAppendToWithClassNameWildcard() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.*Class");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.className, "Should have class name pattern");
        assertTrue(spec.className.contains("*"), "Should preserve wildcard in class name");
    }

    /**
     * Test that appendTo handles negated access flags.
     */
    @Test
    public void testAppendToWithNegatedAccessFlags() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setAccess("!public");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertTrue(spec.requiredUnsetAccessFlags != 0 || spec.requiredSetAccessFlags != 0,
            "Should handle negated access flags");
    }

    /**
     * Test that appendTo handles negated type.
     */
    @Test
    public void testAppendToWithNegatedType() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setType("!interface");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec, "Should handle negated type");
    }
}
