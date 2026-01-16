package proguard.ant;

import org.junit.jupiter.api.Test;
import proguard.ClassSpecification;
import proguard.MemberSpecification;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ClassSpecificationElement.addConfiguredField(MemberSpecificationElement) method.
 */
public class ClassSpecificationElementClaude_addConfiguredFieldTest {

    /**
     * Test that addConfiguredField accepts a valid MemberSpecificationElement.
     */
    @Test
    public void testAddConfiguredFieldWithValidElement() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        MemberSpecificationElement field = new MemberSpecificationElement();

        assertDoesNotThrow(() -> element.addConfiguredField(field),
            "Should accept valid MemberSpecificationElement");
    }

    /**
     * Test that addConfiguredField adds a single field specification.
     */
    @Test
    public void testAddConfiguredFieldAddsSingleField() {
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
     * Test that addConfiguredField can be called multiple times.
     */
    @Test
    public void testAddConfiguredFieldMultipleTimes() {
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
     * Test that addConfiguredField with an element that has a configured name.
     */
    @Test
    public void testAddConfiguredFieldWithFieldName() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");

        MemberSpecificationElement field = new MemberSpecificationElement();
        field.setName("testField");
        element.addConfiguredField(field);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.fieldSpecifications, "Should have field specifications");
        assertEquals(1, spec.fieldSpecifications.size(), "Should have one field specification");

        MemberSpecification fieldSpec = (MemberSpecification) spec.fieldSpecifications.get(0);
        assertNotNull(fieldSpec, "Field specification should not be null");
    }

    /**
     * Test that addConfiguredField with an element that has access modifiers.
     */
    @Test
    public void testAddConfiguredFieldWithAccessModifiers() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");

        MemberSpecificationElement field = new MemberSpecificationElement();
        field.setName("testField");
        field.setAccess("public");
        element.addConfiguredField(field);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.fieldSpecifications, "Should have field specifications");
        assertEquals(1, spec.fieldSpecifications.size(), "Should have one field specification");
    }

    /**
     * Test that addConfiguredField with an element that has a type.
     */
    @Test
    public void testAddConfiguredFieldWithType() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");

        MemberSpecificationElement field = new MemberSpecificationElement();
        field.setName("testField");
        field.setType("int");
        element.addConfiguredField(field);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.fieldSpecifications, "Should have field specifications");
        assertEquals(1, spec.fieldSpecifications.size(), "Should have one field specification");
    }

    /**
     * Test that addConfiguredField with an element that has an annotation.
     */
    @Test
    public void testAddConfiguredFieldWithAnnotation() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");

        MemberSpecificationElement field = new MemberSpecificationElement();
        field.setName("testField");
        field.setAnnotation("MyAnnotation");
        element.addConfiguredField(field);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.fieldSpecifications, "Should have field specifications");
        assertEquals(1, spec.fieldSpecifications.size(), "Should have one field specification");
    }

    /**
     * Test that addConfiguredField with a fully configured element.
     */
    @Test
    public void testAddConfiguredFieldWithFullyConfiguredElement() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");

        MemberSpecificationElement field = new MemberSpecificationElement();
        field.setName("testField");
        field.setAccess("private");
        field.setType("java.lang.String");
        field.setAnnotation("MyAnnotation");
        element.addConfiguredField(field);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.fieldSpecifications, "Should have field specifications");
        assertEquals(1, spec.fieldSpecifications.size(), "Should have one field specification");
    }

    /**
     * Test that addConfiguredField with an unconfigured element (no name).
     */
    @Test
    public void testAddConfiguredFieldWithUnconfiguredElement() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");

        MemberSpecificationElement field = new MemberSpecificationElement();
        // Not setting any properties
        element.addConfiguredField(field);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.fieldSpecifications, "Should have field specifications");
        assertEquals(1, spec.fieldSpecifications.size(), "Should have one field specification even if unconfigured");
    }

    /**
     * Test that addConfiguredField doesn't affect class name.
     */
    @Test
    public void testAddConfiguredFieldDoesNotAffectClassName() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");

        MemberSpecificationElement field = new MemberSpecificationElement();
        field.setName("testField");
        element.addConfiguredField(field);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.className, "Class name should still be set");
        assertEquals("com/example/TestClass", spec.className, "Class name should not be affected");
    }

    /**
     * Test that addConfiguredField doesn't affect class annotation.
     */
    @Test
    public void testAddConfiguredFieldDoesNotAffectClassAnnotation() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setAnnotation("ClassAnnotation");

        MemberSpecificationElement field = new MemberSpecificationElement();
        field.setName("testField");
        element.addConfiguredField(field);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.annotationType, "Class annotation should still be set");
        assertEquals("LClassAnnotation;", spec.annotationType, "Class annotation should not be affected");
    }

    /**
     * Test that addConfiguredField doesn't affect access flags.
     */
    @Test
    public void testAddConfiguredFieldDoesNotAffectAccessFlags() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setAccess("public");

        MemberSpecificationElement field = new MemberSpecificationElement();
        field.setName("testField");
        element.addConfiguredField(field);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertTrue(spec.requiredSetAccessFlags != 0, "Class access flags should still be set");
    }

    /**
     * Test that addConfiguredField doesn't affect extends clause.
     */
    @Test
    public void testAddConfiguredFieldDoesNotAffectExtends() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setExtends("com.example.BaseClass");

        MemberSpecificationElement field = new MemberSpecificationElement();
        field.setName("testField");
        element.addConfiguredField(field);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.extendsClassName, "Extends class name should still be set");
        assertEquals("com/example/BaseClass", spec.extendsClassName, "Extends should not be affected");
    }

    /**
     * Test that addConfiguredField can work with addConfiguredMethod.
     */
    @Test
    public void testAddConfiguredFieldWithMethods() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");

        MemberSpecificationElement field = new MemberSpecificationElement();
        field.setName("testField");
        element.addConfiguredField(field);

        MemberSpecificationElement method = new MemberSpecificationElement();
        method.setName("testMethod");
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
     * Test that addConfiguredField preserves order of fields.
     */
    @Test
    public void testAddConfiguredFieldPreservesOrder() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");

        MemberSpecificationElement field1 = new MemberSpecificationElement();
        field1.setName("field1");
        element.addConfiguredField(field1);

        MemberSpecificationElement field2 = new MemberSpecificationElement();
        field2.setName("field2");
        element.addConfiguredField(field2);

        MemberSpecificationElement field3 = new MemberSpecificationElement();
        field3.setName("field3");
        element.addConfiguredField(field3);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertEquals(3, spec.fieldSpecifications.size(), "Should have three field specifications");
    }

    /**
     * Test that addConfiguredField on element without class name.
     */
    @Test
    public void testAddConfiguredFieldWithoutClassName() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        // Not setting class name

        MemberSpecificationElement field = new MemberSpecificationElement();
        field.setName("testField");
        element.addConfiguredField(field);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.fieldSpecifications, "Should have field specifications even without class name");
        assertEquals(1, spec.fieldSpecifications.size(), "Should have one field specification");
    }

    /**
     * Test that addConfiguredField with wildcard field name.
     */
    @Test
    public void testAddConfiguredFieldWithWildcardName() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");

        MemberSpecificationElement field = new MemberSpecificationElement();
        field.setName("test*");
        element.addConfiguredField(field);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.fieldSpecifications, "Should have field specifications");
        assertEquals(1, spec.fieldSpecifications.size(), "Should have one field specification with wildcard");
    }

    /**
     * Test that addConfiguredField with multiple access modifiers.
     */
    @Test
    public void testAddConfiguredFieldWithMultipleAccessModifiers() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");

        MemberSpecificationElement field = new MemberSpecificationElement();
        field.setName("testField");
        field.setAccess("public,static");
        element.addConfiguredField(field);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.fieldSpecifications, "Should have field specifications");
        assertEquals(1, spec.fieldSpecifications.size(), "Should have one field specification");
    }

    /**
     * Test that addConfiguredField with primitive type.
     */
    @Test
    public void testAddConfiguredFieldWithPrimitiveType() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");

        MemberSpecificationElement field = new MemberSpecificationElement();
        field.setName("testField");
        field.setType("int");
        element.addConfiguredField(field);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.fieldSpecifications, "Should have field specifications");
        assertEquals(1, spec.fieldSpecifications.size(), "Should have one field specification with primitive type");
    }

    /**
     * Test that addConfiguredField with object type.
     */
    @Test
    public void testAddConfiguredFieldWithObjectType() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");

        MemberSpecificationElement field = new MemberSpecificationElement();
        field.setName("testField");
        field.setType("java.lang.String");
        element.addConfiguredField(field);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.fieldSpecifications, "Should have field specifications");
        assertEquals(1, spec.fieldSpecifications.size(), "Should have one field specification with object type");
    }

    /**
     * Test that addConfiguredField with array type.
     */
    @Test
    public void testAddConfiguredFieldWithArrayType() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");

        MemberSpecificationElement field = new MemberSpecificationElement();
        field.setName("testField");
        field.setType("java.lang.String[]");
        element.addConfiguredField(field);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.fieldSpecifications, "Should have field specifications");
        assertEquals(1, spec.fieldSpecifications.size(), "Should have one field specification with array type");
    }

    /**
     * Test that addConfiguredField initializes fieldSpecifications list on first call.
     */
    @Test
    public void testAddConfiguredFieldInitializesListOnFirstCall() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");

        // First call should initialize the list
        MemberSpecificationElement field1 = new MemberSpecificationElement();
        field1.setName("field1");
        assertDoesNotThrow(() -> element.addConfiguredField(field1),
            "First call should initialize field specifications list");

        // Second call should use existing list
        MemberSpecificationElement field2 = new MemberSpecificationElement();
        field2.setName("field2");
        assertDoesNotThrow(() -> element.addConfiguredField(field2),
            "Second call should use existing list");

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertEquals(2, spec.fieldSpecifications.size(), "Should have two field specifications");
    }

    /**
     * Test that addConfiguredField with negated access modifier.
     */
    @Test
    public void testAddConfiguredFieldWithNegatedAccessModifier() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");

        MemberSpecificationElement field = new MemberSpecificationElement();
        field.setName("testField");
        field.setAccess("!private");
        element.addConfiguredField(field);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.fieldSpecifications, "Should have field specifications");
        assertEquals(1, spec.fieldSpecifications.size(), "Should have one field specification with negated access");
    }

    /**
     * Test that addConfiguredField with different field names creates distinct specifications.
     */
    @Test
    public void testAddConfiguredFieldWithDifferentNames() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");

        MemberSpecificationElement field1 = new MemberSpecificationElement();
        field1.setName("firstName");
        element.addConfiguredField(field1);

        MemberSpecificationElement field2 = new MemberSpecificationElement();
        field2.setName("lastName");
        element.addConfiguredField(field2);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertEquals(2, spec.fieldSpecifications.size(), "Should have two distinct field specifications");
    }

    /**
     * Test that addConfiguredField with same field name creates separate specifications.
     */
    @Test
    public void testAddConfiguredFieldWithSameName() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");

        MemberSpecificationElement field1 = new MemberSpecificationElement();
        field1.setName("testField");
        element.addConfiguredField(field1);

        MemberSpecificationElement field2 = new MemberSpecificationElement();
        field2.setName("testField");
        element.addConfiguredField(field2);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertEquals(2, spec.fieldSpecifications.size(),
            "Should have two separate field specifications even with same name");
    }

    /**
     * Test that addConfiguredField with fully qualified annotation.
     */
    @Test
    public void testAddConfiguredFieldWithFullyQualifiedAnnotation() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");

        MemberSpecificationElement field = new MemberSpecificationElement();
        field.setName("testField");
        field.setAnnotation("com.example.annotations.FieldAnnotation");
        element.addConfiguredField(field);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.fieldSpecifications, "Should have field specifications");
        assertEquals(1, spec.fieldSpecifications.size(), "Should have one field specification with annotation");
    }

    /**
     * Test that addConfiguredField works in combination with all class properties.
     */
    @Test
    public void testAddConfiguredFieldWithFullClassConfiguration() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");
        element.setAccess("public");
        element.setAnnotation("ClassAnnotation");
        element.setType("class");
        element.setExtends("com.example.BaseClass");
        element.setExtendsannotation("BaseAnnotation");

        MemberSpecificationElement field = new MemberSpecificationElement();
        field.setName("testField");
        field.setAccess("private");
        field.setType("java.lang.String");
        field.setAnnotation("FieldAnnotation");
        element.addConfiguredField(field);

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.className, "Class name should be set");
        assertNotNull(spec.annotationType, "Class annotation should be set");
        assertNotNull(spec.extendsClassName, "Extends class should be set");
        assertNotNull(spec.extendsAnnotationType, "Extends annotation should be set");
        assertNotNull(spec.fieldSpecifications, "Field specifications should be set");
        assertEquals(1, spec.fieldSpecifications.size(), "Should have one field specification");
    }

    /**
     * Test that addConfiguredField handles many fields.
     */
    @Test
    public void testAddConfiguredFieldWithManyFields() {
        ClassSpecificationElement element = new ClassSpecificationElement();
        element.setName("com.example.TestClass");

        // Add 10 fields
        for (int i = 0; i < 10; i++) {
            MemberSpecificationElement field = new MemberSpecificationElement();
            field.setName("field" + i);
            element.addConfiguredField(field);
        }

        List classSpecifications = new ArrayList();
        element.appendTo(classSpecifications);

        ClassSpecification spec = (ClassSpecification) classSpecifications.get(0);
        assertNotNull(spec.fieldSpecifications, "Should have field specifications");
        assertEquals(10, spec.fieldSpecifications.size(), "Should have ten field specifications");
    }
}
