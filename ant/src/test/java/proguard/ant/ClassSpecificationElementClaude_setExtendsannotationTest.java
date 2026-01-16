package proguard.ant;

import org.junit.jupiter.api.Test;
import proguard.ClassSpecification;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ClassSpecificationElement.setExtendsannotation(String) method.
 */
public class ClassSpecificationElementClaude_setExtendsannotationTest {

    /**
     * Test that setExtendsannotation accepts a simple annotation name.
     */
    @Test
    public void testSetExtendsannotationWithSimpleName() {
        ClassSpecificationElement element = new ClassSpecificationElement();

        assertDoesNotThrow(() -> element.setExtendsannotation("MyAnnotation"),
            "Should accept simple annotation name");
    }

    /**
     * Test that setExtendsannotation accepts a fully qualified annotation name.
     */
    @Test
    public void testSetExtendsannotationWithFullyQualifiedName() {
        ClassSpecificationElement element = new ClassSpecificationElement();

        assertDoesNotThrow(() -> element.setExtendsannotation("com.example.MyAnnotation"),
            "Should accept fully qualified annotation name");
    }

    /**
     * Test that setExtendsannotation accepts null value.
     */
    @Test
    public void testSetExtendsannotationWithNull() {
        ClassSpecificationElement element = new ClassSpecificationElement();

        assertDoesNotThrow(() -> element.setExtendsannotation(null),
            "Should accept null extends annotation");
    }

    /**
     * Test that setExtendsannotation accepts an empty string.
     */
    @Test
    public void testSetExtendsannotationWithEmptyString() {
        ClassSpecificationElement element = new ClassSpecificationElement();

        assertDoesNotThrow(() -> element.setExtendsannotation(""),
            "Should accept empty string extends annotation");
    }

    /**
     * Test that setExtendsannotation stores the annotation value correctly.
     */
    @Test
    public void testSetExtendsannotationStoresValue() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtendsannotation("MyAnnotation");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsAnnotationType, "Extends annotation type should be stored");
        assertEquals("LMyAnnotation;", spec.extendsAnnotationType,
            "Extends annotation should be converted to internal type format");
    }

    /**
     * Test that setExtendsannotation with fully qualified name stores the value correctly.
     */
    @Test
    public void testSetExtendsannotationWithFullyQualifiedNameStoresValue() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtendsannotation("com.example.annotations.BaseAnnotation");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsAnnotationType, "Extends annotation type should be stored");
        assertEquals("Lcom/example/annotations/BaseAnnotation;", spec.extendsAnnotationType,
            "Fully qualified extends annotation should be converted to internal type format");
    }

    /**
     * Test that setExtendsannotation with null results in null annotation type.
     */
    @Test
    public void testSetExtendsannotationWithNullResultsInNullType() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtendsannotation(null);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNull(spec.extendsAnnotationType, "Null extends annotation should result in null type");
    }

    /**
     * Test that setExtendsannotation can be called multiple times (last value wins).
     */
    @Test
    public void testSetExtendsannotationMultipleTimes() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtendsannotation("FirstAnnotation");
        element.setExtendsannotation("SecondAnnotation");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsAnnotationType, "Extends annotation type should be stored");
        assertEquals("LSecondAnnotation;", spec.extendsAnnotationType,
            "Last set extends annotation should be used");
    }

    /**
     * Test that setExtendsannotation can override with null.
     */
    @Test
    public void testSetExtendsannotationOverrideWithNull() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtendsannotation("MyAnnotation");
        element.setExtendsannotation(null);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNull(spec.extendsAnnotationType, "Setting null should override previous extends annotation");
    }

    /**
     * Test that setExtendsannotation works independently of other element configuration.
     */
    @Test
    public void testSetExtendsannotationIndependentOfOtherConfiguration() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setAccess("public");
        element.setType("class");
        element.setAnnotation("ClassAnnotation");
        element.setExtends("com.example.BaseClass");
        element.setExtendsannotation("BaseAnnotation");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsAnnotationType, "Extends annotation should be set");
        assertEquals("LBaseAnnotation;", spec.extendsAnnotationType,
            "Extends annotation should be correctly stored alongside other configuration");
    }

    /**
     * Test that setExtendsannotation doesn't affect the class annotation.
     */
    @Test
    public void testSetExtendsannotationDoesNotAffectClassAnnotation() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setAnnotation("ClassAnnotation");
        element.setExtendsannotation("ExtendsAnnotation");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.annotationType, "Class annotation should be set");
        assertEquals("LClassAnnotation;", spec.annotationType, "Class annotation should be correct");
        assertNotNull(spec.extendsAnnotationType, "Extends annotation should be set");
        assertEquals("LExtendsAnnotation;", spec.extendsAnnotationType,
            "Extends annotation should be correct");
    }

    /**
     * Test that setExtendsannotation doesn't affect extends class name.
     */
    @Test
    public void testSetExtendsannotationDoesNotAffectExtendsClassName() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtends("com.example.BaseClass");
        element.setExtendsannotation("BaseAnnotation");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Extends class name should be set");
        assertEquals("com/example/BaseClass", spec.extendsClassName,
            "Extends class name should be correct");
        assertNotNull(spec.extendsAnnotationType, "Extends annotation should be set");
        assertEquals("LBaseAnnotation;", spec.extendsAnnotationType,
            "Extends annotation should be correct");
    }

    /**
     * Test that setExtendsannotation doesn't affect access flags.
     */
    @Test
    public void testSetExtendsannotationDoesNotAffectAccessFlags() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setAccess("public,final");
        element.setExtendsannotation("MyAnnotation");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsAnnotationType, "Extends annotation should be set");
        assertTrue(spec.requiredSetAccessFlags != 0, "Access flags should still be set");
    }

    /**
     * Test that setExtendsannotation doesn't affect class type.
     */
    @Test
    public void testSetExtendsannotationDoesNotAffectType() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setType("interface");
        element.setExtendsannotation("MyAnnotation");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsAnnotationType, "Extends annotation should be set");
        assertTrue(spec.requiredSetAccessFlags != 0 || spec.requiredUnsetAccessFlags != 0,
            "Type flags should still be set");
    }

    /**
     * Test that setExtendsannotation with nested class annotation name.
     */
    @Test
    public void testSetExtendsannotationWithNestedClassName() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtendsannotation("com.example.OuterClass$InnerAnnotation");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsAnnotationType, "Nested class annotation should be stored");
        assertEquals("Lcom/example/OuterClass$InnerAnnotation;", spec.extendsAnnotationType,
            "Nested class annotation should be converted correctly");
    }

    /**
     * Test that setExtendsannotation with wildcard pattern.
     */
    @Test
    public void testSetExtendsannotationWithWildcard() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtendsannotation("com.example.*Annotation");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsAnnotationType, "Wildcard annotation should be stored");
        assertTrue(spec.extendsAnnotationType.contains("*"), "Wildcard should be preserved");
    }

    /**
     * Test that setExtendsannotation with double wildcard pattern.
     */
    @Test
    public void testSetExtendsannotationWithDoubleWildcard() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtendsannotation("com.example.**");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsAnnotationType, "Double wildcard annotation should be stored");
        assertTrue(spec.extendsAnnotationType.contains("**"), "Double wildcard should be preserved");
    }

    /**
     * Test that setExtendsannotation with single character name.
     */
    @Test
    public void testSetExtendsannotationWithSingleCharacter() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtendsannotation("A");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsAnnotationType, "Single character annotation should be stored");
        assertEquals("LA;", spec.extendsAnnotationType,
            "Single character annotation should be converted correctly");
    }

    /**
     * Test that setExtendsannotation with package-less annotation name.
     */
    @Test
    public void testSetExtendsannotationWithoutPackage() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtendsannotation("SimpleAnnotation");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsAnnotationType, "Package-less annotation should be stored");
        assertEquals("LSimpleAnnotation;", spec.extendsAnnotationType,
            "Package-less annotation should be converted correctly");
    }

    /**
     * Test that setExtendsannotation is called successfully on an element without name.
     */
    @Test
    public void testSetExtendsannotationOnElementWithoutName() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setExtendsannotation("MyAnnotation");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsAnnotationType, "Extends annotation should be set even without class name");
        assertEquals("LMyAnnotation;", spec.extendsAnnotationType,
            "Extends annotation should be correctly stored");
    }

    /**
     * Test that empty string annotation results in empty internal type.
     */
    @Test
    public void testSetExtendsannotationWithEmptyStringStoresValue() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtendsannotation("");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsAnnotationType, "Empty string annotation should result in non-null type");
        assertEquals("L;", spec.extendsAnnotationType,
            "Empty string annotation should be converted to L;");
    }

    /**
     * Test that setExtendsannotation with spaces in name.
     */
    @Test
    public void testSetExtendsannotationWithSpaces() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtendsannotation("My Annotation");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsAnnotationType, "Annotation with spaces should be stored");
        assertTrue(spec.extendsAnnotationType.contains(" "), "Spaces should be preserved");
    }

    /**
     * Test that setExtendsannotation doesn't affect class name.
     */
    @Test
    public void testSetExtendsannotationDoesNotAffectClassName() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtendsannotation("MyAnnotation");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.className, "Class name should be set");
        assertEquals("com/example/TestClass", spec.className, "Class name should not be affected");
        assertNotNull(spec.extendsAnnotationType, "Extends annotation should be set");
    }

    /**
     * Test that setExtendsannotation doesn't interfere with field specifications.
     */
    @Test
    public void testSetExtendsannotationDoesNotAffectFieldSpecifications() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtendsannotation("MyAnnotation");

        MemberSpecificationElement field = new MemberSpecificationElement();
        field.setName("myField");
        element.addConfiguredField(field);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsAnnotationType, "Extends annotation should be set");
        assertNotNull(spec.fieldSpecifications, "Field specifications should be set");
        assertEquals(1, spec.fieldSpecifications.size(), "Should have one field specification");
    }

    /**
     * Test that setExtendsannotation doesn't interfere with method specifications.
     */
    @Test
    public void testSetExtendsannotationDoesNotAffectMethodSpecifications() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtendsannotation("MyAnnotation");

        MemberSpecificationElement method = new MemberSpecificationElement();
        method.setName("myMethod");
        element.addConfiguredMethod(method);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsAnnotationType, "Extends annotation should be set");
        assertNotNull(spec.methodSpecifications, "Method specifications should be set");
        assertEquals(1, spec.methodSpecifications.size(), "Should have one method specification");
    }

    /**
     * Test that setExtendsannotation with numeric characters.
     */
    @Test
    public void testSetExtendsannotationWithNumericCharacters() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtendsannotation("com.example.MyAnnotation123");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsAnnotationType, "Annotation with numbers should be stored");
        assertEquals("Lcom/example/MyAnnotation123;", spec.extendsAnnotationType,
            "Annotation with numbers should be converted correctly");
    }

    /**
     * Test that setExtendsannotation with underscores.
     */
    @Test
    public void testSetExtendsannotationWithUnderscores() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtendsannotation("com.example.My_Annotation");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsAnnotationType, "Annotation with underscores should be stored");
        assertEquals("Lcom/example/My_Annotation;", spec.extendsAnnotationType,
            "Annotation with underscores should be converted correctly");
    }

    /**
     * Test that setExtendsannotation works when extends class is also set.
     */
    @Test
    public void testSetExtendsannotationWorksWithExtendsClass() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtends("com.example.BaseClass");
        element.setExtendsannotation("BaseAnnotation");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Extends class name should be set");
        assertEquals("com/example/BaseClass", spec.extendsClassName, "Extends class name should be correct");
        assertNotNull(spec.extendsAnnotationType, "Extends annotation should be set");
        assertEquals("LBaseAnnotation;", spec.extendsAnnotationType, "Extends annotation should be correct");
    }

    /**
     * Test that setExtendsannotation works when implements is used (uses extends internally).
     */
    @Test
    public void testSetExtendsannotationWorksWithImplements() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setImplements("java.io.Serializable");
        element.setExtendsannotation("SerializableAnnotation");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Implements (extends) class name should be set");
        assertEquals("java/io/Serializable", spec.extendsClassName, "Implements class name should be correct");
        assertNotNull(spec.extendsAnnotationType, "Extends annotation should be set");
        assertEquals("LSerializableAnnotation;", spec.extendsAnnotationType,
            "Extends annotation should be correct");
    }

    /**
     * Test that setExtendsannotation with very long qualified name.
     */
    @Test
    public void testSetExtendsannotationWithVeryLongQualifiedName() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtendsannotation("com.example.very.long.package.name.hierarchy.MyAnnotation");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsAnnotationType, "Long qualified annotation should be stored");
        assertEquals("Lcom/example/very/long/package/name/hierarchy/MyAnnotation;",
            spec.extendsAnnotationType,
            "Long qualified annotation should be converted correctly");
    }

    /**
     * Test that setExtendsannotation can be used without setting extends class.
     */
    @Test
    public void testSetExtendsannotationWithoutExtendsClass() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtendsannotation("MyAnnotation");
        // Note: not setting extends class

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNull(spec.extendsClassName, "Extends class name should be null");
        assertNotNull(spec.extendsAnnotationType, "Extends annotation should still be set");
        assertEquals("LMyAnnotation;", spec.extendsAnnotationType,
            "Extends annotation should be correct even without extends class");
    }
}
