package proguard.ant;

import org.junit.jupiter.api.Test;
import proguard.ClassSpecification;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ClassSpecificationElement.setAnnotation(String) method.
 */
public class ClassSpecificationElementClaude_setAnnotationTest {

    /**
     * Test that setAnnotation accepts a simple annotation name.
     */
    @Test
    public void testSetAnnotationWithSimpleName() {
        ClassSpecificationElement element = new ClassSpecificationElement();

        assertDoesNotThrow(() -> element.setAnnotation("MyAnnotation"),
            "Should accept simple annotation name");
    }

    /**
     * Test that setAnnotation accepts a fully qualified annotation name.
     */
    @Test
    public void testSetAnnotationWithFullyQualifiedName() {
        ClassSpecificationElement element = new ClassSpecificationElement();

        assertDoesNotThrow(() -> element.setAnnotation("com.example.MyAnnotation"),
            "Should accept fully qualified annotation name");
    }

    /**
     * Test that setAnnotation accepts null value.
     */
    @Test
    public void testSetAnnotationWithNull() {
        ClassSpecificationElement element = new ClassSpecificationElement();

        assertDoesNotThrow(() -> element.setAnnotation(null),
            "Should accept null annotation");
    }

    /**
     * Test that setAnnotation accepts an empty string.
     */
    @Test
    public void testSetAnnotationWithEmptyString() {
        ClassSpecificationElement element = new ClassSpecificationElement();

        assertDoesNotThrow(() -> element.setAnnotation(""),
            "Should accept empty string annotation");
    }

    /**
     * Test that setAnnotation stores the annotation value correctly by checking
     * the resulting ClassSpecification.
     */
    @Test
    public void testSetAnnotationStoresValue() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setAnnotation("MyAnnotation");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.annotationType, "Annotation type should be stored in ClassSpecification");
        assertEquals("LMyAnnotation;", spec.annotationType,
            "Annotation should be converted to internal type format");
    }

    /**
     * Test that setAnnotation with fully qualified name stores the value correctly.
     */
    @Test
    public void testSetAnnotationWithFullyQualifiedNameStoresValue() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setAnnotation("com.example.annotations.MyAnnotation");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.annotationType, "Annotation type should be stored in ClassSpecification");
        assertEquals("Lcom/example/annotations/MyAnnotation;", spec.annotationType,
            "Fully qualified annotation should be converted to internal type format");
    }

    /**
     * Test that setAnnotation with null results in null annotation type.
     */
    @Test
    public void testSetAnnotationWithNullResultsInNullType() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setAnnotation(null);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNull(spec.annotationType, "Null annotation should result in null annotation type");
    }

    /**
     * Test that setAnnotation can be called multiple times (last value wins).
     */
    @Test
    public void testSetAnnotationMultipleTimes() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setAnnotation("FirstAnnotation");
        element.setAnnotation("SecondAnnotation");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.annotationType, "Annotation type should be stored");
        assertEquals("LSecondAnnotation;", spec.annotationType,
            "Last set annotation should be used");
    }

    /**
     * Test that setAnnotation can override with null.
     */
    @Test
    public void testSetAnnotationOverrideWithNull() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setAnnotation("MyAnnotation");
        element.setAnnotation(null);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNull(spec.annotationType, "Setting null should override previous annotation");
    }

    /**
     * Test that setAnnotation works independently of other element configuration.
     */
    @Test
    public void testSetAnnotationIndependentOfOtherConfiguration() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setAccess("public");
        element.setType("class");
        element.setExtends("com.example.BaseClass");
        element.setAnnotation("MyAnnotation");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.annotationType, "Annotation should be set");
        assertEquals("LMyAnnotation;", spec.annotationType,
            "Annotation should be correctly stored alongside other configuration");
    }

    /**
     * Test that setAnnotation with nested class annotation name.
     */
    @Test
    public void testSetAnnotationWithNestedClassName() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setAnnotation("com.example.OuterClass$InnerAnnotation");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.annotationType, "Nested class annotation should be stored");
        assertEquals("Lcom/example/OuterClass$InnerAnnotation;", spec.annotationType,
            "Nested class annotation should be converted correctly");
    }

    /**
     * Test that setAnnotation with wildcard pattern.
     */
    @Test
    public void testSetAnnotationWithWildcard() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setAnnotation("com.example.*Annotation");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.annotationType, "Wildcard annotation should be stored");
        assertTrue(spec.annotationType.contains("*"), "Wildcard should be preserved");
    }

    /**
     * Test that setAnnotation with double wildcard pattern.
     */
    @Test
    public void testSetAnnotationWithDoubleWildcard() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setAnnotation("com.example.**");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.annotationType, "Double wildcard annotation should be stored");
        assertTrue(spec.annotationType.contains("**"), "Double wildcard should be preserved");
    }

    /**
     * Test that setAnnotation doesn't interfere with extendsannotation.
     */
    @Test
    public void testSetAnnotationDoesNotAffectExtendsAnnotation() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setAnnotation("ClassAnnotation");
        element.setExtendsannotation("ExtendsAnnotation");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.annotationType, "Class annotation should be set");
        assertEquals("LClassAnnotation;", spec.annotationType,
            "Class annotation should be correct");
        assertNotNull(spec.extendsAnnotationType, "Extends annotation should be set");
        assertEquals("LExtendsAnnotation;", spec.extendsAnnotationType,
            "Extends annotation should be correct");
    }

    /**
     * Test that setAnnotation with single character name.
     */
    @Test
    public void testSetAnnotationWithSingleCharacter() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setAnnotation("A");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.annotationType, "Single character annotation should be stored");
        assertEquals("LA;", spec.annotationType,
            "Single character annotation should be converted correctly");
    }

    /**
     * Test that setAnnotation with package-less annotation name.
     */
    @Test
    public void testSetAnnotationWithoutPackage() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setAnnotation("SimpleAnnotation");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.annotationType, "Package-less annotation should be stored");
        assertEquals("LSimpleAnnotation;", spec.annotationType,
            "Package-less annotation should be converted correctly");
    }

    /**
     * Test that setAnnotation is called successfully on an element without name.
     */
    @Test
    public void testSetAnnotationOnElementWithoutName() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setAnnotation("MyAnnotation");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.annotationType, "Annotation should be set even without class name");
        assertEquals("LMyAnnotation;", spec.annotationType,
            "Annotation should be correctly stored");
    }

    /**
     * Test that empty string annotation results in empty internal type.
     */
    @Test
    public void testSetAnnotationWithEmptyStringStoresValue() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setAnnotation("");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.annotationType, "Empty string annotation should result in non-null type");
        assertEquals("L;", spec.annotationType,
            "Empty string annotation should be converted to L;");
    }

    /**
     * Test that setAnnotation with spaces in name.
     */
    @Test
    public void testSetAnnotationWithSpaces() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setAnnotation("My Annotation");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.annotationType, "Annotation with spaces should be stored");
        // The ClassUtil.internalType will convert dots to slashes but spaces remain
        assertTrue(spec.annotationType.contains(" "), "Spaces should be preserved");
    }

    /**
     * Test that setAnnotation doesn't affect access flags.
     */
    @Test
    public void testSetAnnotationDoesNotAffectAccessFlags() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setAccess("public,final");
        element.setAnnotation("MyAnnotation");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.annotationType, "Annotation should be set");
        assertTrue(spec.requiredSetAccessFlags != 0, "Access flags should still be set");
    }

    /**
     * Test that setAnnotation doesn't affect class type.
     */
    @Test
    public void testSetAnnotationDoesNotAffectType() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setType("interface");
        element.setAnnotation("MyAnnotation");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.annotationType, "Annotation should be set");
        assertTrue(spec.requiredSetAccessFlags != 0 || spec.requiredUnsetAccessFlags != 0,
            "Type flags should still be set");
    }

    /**
     * Test that setAnnotation doesn't affect extends clause.
     */
    @Test
    public void testSetAnnotationDoesNotAffectExtends() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtends("com.example.BaseClass");
        element.setAnnotation("MyAnnotation");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.annotationType, "Annotation should be set");
        assertNotNull(spec.extendsClassName, "Extends class name should still be set");
        assertEquals("com/example/BaseClass", spec.extendsClassName,
            "Extends class name should not be affected");
    }
}
