package proguard;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ClassSpecification}.
 * Tests all methods to ensure proper functionality of ClassSpecification.
 */
public class ClassSpecificationClaudeTest {

    // ========== Constructor Tests ==========

    /**
     * Tests the no-argument constructor ClassSpecification().
     * Verifies that all fields are initialized to default values.
     */
    @Test
    public void testConstructorNoArgs() {
        // Act
        ClassSpecification spec = new ClassSpecification();

        // Assert
        assertNull(spec.comments, "Comments should be null by default");
        assertNull(spec.memberComments, "Member comments should be null by default");
        assertEquals(0, spec.requiredSetAccessFlags, "Required set access flags should be 0");
        assertEquals(0, spec.requiredUnsetAccessFlags, "Required unset access flags should be 0");
        assertNull(spec.annotationType, "Annotation type should be null by default");
        assertNull(spec.className, "Class name should be null by default");
        assertNull(spec.extendsAnnotationType, "Extends annotation type should be null by default");
        assertNull(spec.extendsClassName, "Extends class name should be null by default");
        assertNull(spec.fieldSpecifications, "Field specifications should be null by default");
        assertNull(spec.methodSpecifications, "Method specifications should be null by default");
    }

    /**
     * Tests the copy constructor ClassSpecification(ClassSpecification).
     * Verifies that all fields are copied from the source specification.
     */
    @Test
    public void testConstructorCopy() {
        // Arrange
        MemberSpecification field = new MemberSpecification(1, 2, "FieldAnnotation", "fieldName", "I");
        MemberSpecification method = new MemberSpecification(4, 8, "MethodAnnotation", "methodName", "()V");
        List<MemberSpecification> fieldSpecs = new ArrayList<>();
        fieldSpecs.add(field);
        List<MemberSpecification> methodSpecs = new ArrayList<>();
        methodSpecs.add(method);

        ClassSpecification original = new ClassSpecification(
            "Original comments",
            16,
            32,
            "AnnotationType",
            "ClassName",
            "ExtendsAnnotationType",
            "ExtendsClassName",
            fieldSpecs,
            methodSpecs
        );

        // Act
        ClassSpecification copy = new ClassSpecification(original);

        // Assert
        assertEquals(original.comments, copy.comments, "Comments should be copied");
        assertEquals(original.requiredSetAccessFlags, copy.requiredSetAccessFlags, "Required set access flags should be copied");
        assertEquals(original.requiredUnsetAccessFlags, copy.requiredUnsetAccessFlags, "Required unset access flags should be copied");
        assertEquals(original.annotationType, copy.annotationType, "Annotation type should be copied");
        assertEquals(original.className, copy.className, "Class name should be copied");
        assertEquals(original.extendsAnnotationType, copy.extendsAnnotationType, "Extends annotation type should be copied");
        assertEquals(original.extendsClassName, copy.extendsClassName, "Extends class name should be copied");
        assertSame(original.fieldSpecifications, copy.fieldSpecifications, "Field specifications should reference the same list");
        assertSame(original.methodSpecifications, copy.methodSpecifications, "Method specifications should reference the same list");
    }

    /**
     * Tests the copy constructor with a specification that has null lists.
     */
    @Test
    public void testConstructorCopyWithNullLists() {
        // Arrange
        ClassSpecification original = new ClassSpecification(
            "Comments",
            1,
            2,
            "Annotation",
            "Class",
            "ExtendsAnnotation",
            "ExtendsClass"
        );

        // Act
        ClassSpecification copy = new ClassSpecification(original);

        // Assert
        assertNull(copy.fieldSpecifications, "Field specifications should be null");
        assertNull(copy.methodSpecifications, "Method specifications should be null");
    }

    /**
     * Tests the seven-argument constructor without member specifications.
     * Verifies that all parameters are set correctly.
     */
    @Test
    public void testConstructorSevenArgs() {
        // Act
        ClassSpecification spec = new ClassSpecification(
            "Test comments",
            64,
            128,
            "TestAnnotation",
            "TestClass",
            "TestExtendsAnnotation",
            "TestExtendsClass"
        );

        // Assert
        assertEquals("Test comments", spec.comments, "Comments should be set");
        assertEquals(64, spec.requiredSetAccessFlags, "Required set access flags should be set");
        assertEquals(128, spec.requiredUnsetAccessFlags, "Required unset access flags should be set");
        assertEquals("TestAnnotation", spec.annotationType, "Annotation type should be set");
        assertEquals("TestClass", spec.className, "Class name should be set");
        assertEquals("TestExtendsAnnotation", spec.extendsAnnotationType, "Extends annotation type should be set");
        assertEquals("TestExtendsClass", spec.extendsClassName, "Extends class name should be set");
        assertNull(spec.fieldSpecifications, "Field specifications should be null");
        assertNull(spec.methodSpecifications, "Method specifications should be null");
    }

    /**
     * Tests the seven-argument constructor with null values.
     */
    @Test
    public void testConstructorSevenArgsWithNulls() {
        // Act
        ClassSpecification spec = new ClassSpecification(
            null,
            0,
            0,
            null,
            null,
            null,
            null
        );

        // Assert
        assertNull(spec.comments, "Comments should be null");
        assertNull(spec.annotationType, "Annotation type should be null");
        assertNull(spec.className, "Class name should be null");
        assertNull(spec.extendsAnnotationType, "Extends annotation type should be null");
        assertNull(spec.extendsClassName, "Extends class name should be null");
    }

    /**
     * Tests the nine-argument constructor with member specifications.
     * Verifies that all parameters including lists are set correctly.
     */
    @Test
    public void testConstructorNineArgs() {
        // Arrange
        MemberSpecification field = new MemberSpecification(1, 2, "FieldAnnotation", "field", "I");
        MemberSpecification method = new MemberSpecification(4, 8, "MethodAnnotation", "method", "()V");
        List<MemberSpecification> fieldSpecs = new ArrayList<>();
        fieldSpecs.add(field);
        List<MemberSpecification> methodSpecs = new ArrayList<>();
        methodSpecs.add(method);

        // Act
        ClassSpecification spec = new ClassSpecification(
            "Full comments",
            256,
            512,
            "FullAnnotation",
            "FullClass",
            "FullExtendsAnnotation",
            "FullExtendsClass",
            fieldSpecs,
            methodSpecs
        );

        // Assert
        assertEquals("Full comments", spec.comments, "Comments should be set");
        assertEquals(256, spec.requiredSetAccessFlags, "Required set access flags should be set");
        assertEquals(512, spec.requiredUnsetAccessFlags, "Required unset access flags should be set");
        assertEquals("FullAnnotation", spec.annotationType, "Annotation type should be set");
        assertEquals("FullClass", spec.className, "Class name should be set");
        assertEquals("FullExtendsAnnotation", spec.extendsAnnotationType, "Extends annotation type should be set");
        assertEquals("FullExtendsClass", spec.extendsClassName, "Extends class name should be set");
        assertSame(fieldSpecs, spec.fieldSpecifications, "Field specifications should be the same instance");
        assertSame(methodSpecs, spec.methodSpecifications, "Method specifications should be the same instance");
        assertEquals(1, spec.fieldSpecifications.size(), "Field specifications should have one element");
        assertEquals(1, spec.methodSpecifications.size(), "Method specifications should have one element");
    }

    /**
     * Tests the nine-argument constructor with empty lists.
     */
    @Test
    public void testConstructorNineArgsWithEmptyLists() {
        // Arrange
        List<MemberSpecification> emptyFieldSpecs = new ArrayList<>();
        List<MemberSpecification> emptyMethodSpecs = new ArrayList<>();

        // Act
        ClassSpecification spec = new ClassSpecification(
            "Comments",
            1,
            2,
            "Annotation",
            "Class",
            "ExtendsAnnotation",
            "ExtendsClass",
            emptyFieldSpecs,
            emptyMethodSpecs
        );

        // Assert
        assertSame(emptyFieldSpecs, spec.fieldSpecifications, "Field specifications should be the same empty list instance");
        assertSame(emptyMethodSpecs, spec.methodSpecifications, "Method specifications should be the same empty list instance");
        assertEquals(0, spec.fieldSpecifications.size(), "Field specifications should be empty");
        assertEquals(0, spec.methodSpecifications.size(), "Method specifications should be empty");
    }

    /**
     * Tests the nine-argument constructor with null lists.
     */
    @Test
    public void testConstructorNineArgsWithNullLists() {
        // Act
        ClassSpecification spec = new ClassSpecification(
            "Comments",
            1,
            2,
            "Annotation",
            "Class",
            "ExtendsAnnotation",
            "ExtendsClass",
            null,
            null
        );

        // Assert
        assertNull(spec.fieldSpecifications, "Field specifications should be null");
        assertNull(spec.methodSpecifications, "Method specifications should be null");
    }

    // ========== addField() Tests ==========

    /**
     * Tests addField() on a specification with null fieldSpecifications.
     * Verifies that a new list is created and the field is added.
     */
    @Test
    public void testAddFieldToNullList() {
        // Arrange
        ClassSpecification spec = new ClassSpecification();
        MemberSpecification field = new MemberSpecification(1, 2, "Annotation", "field", "I");

        // Act
        spec.addField(field);

        // Assert
        assertNotNull(spec.fieldSpecifications, "Field specifications should not be null after adding");
        assertEquals(1, spec.fieldSpecifications.size(), "Field specifications should have one element");
        assertSame(field, spec.fieldSpecifications.get(0), "Added field should be in the list");
    }

    /**
     * Tests addField() on a specification with an existing fieldSpecifications list.
     * Verifies that the field is appended to the existing list.
     */
    @Test
    public void testAddFieldToExistingList() {
        // Arrange
        MemberSpecification field1 = new MemberSpecification(1, 2, "Annotation1", "field1", "I");
        MemberSpecification field2 = new MemberSpecification(4, 8, "Annotation2", "field2", "Z");
        List<MemberSpecification> fieldSpecs = new ArrayList<>();
        fieldSpecs.add(field1);
        ClassSpecification spec = new ClassSpecification(null, 0, 0, null, null, null, null, fieldSpecs, null);

        // Act
        spec.addField(field2);

        // Assert
        assertEquals(2, spec.fieldSpecifications.size(), "Field specifications should have two elements");
        assertSame(field1, spec.fieldSpecifications.get(0), "First field should remain");
        assertSame(field2, spec.fieldSpecifications.get(1), "Second field should be added");
    }

    /**
     * Tests adding multiple fields sequentially.
     */
    @Test
    public void testAddMultipleFields() {
        // Arrange
        ClassSpecification spec = new ClassSpecification();
        MemberSpecification field1 = new MemberSpecification(1, 2, "Ann1", "f1", "I");
        MemberSpecification field2 = new MemberSpecification(4, 8, "Ann2", "f2", "Z");
        MemberSpecification field3 = new MemberSpecification(16, 32, "Ann3", "f3", "Ljava/lang/String;");

        // Act
        spec.addField(field1);
        spec.addField(field2);
        spec.addField(field3);

        // Assert
        assertNotNull(spec.fieldSpecifications, "Field specifications should not be null");
        assertEquals(3, spec.fieldSpecifications.size(), "Field specifications should have three elements");
        assertSame(field1, spec.fieldSpecifications.get(0), "First field should be in position 0");
        assertSame(field2, spec.fieldSpecifications.get(1), "Second field should be in position 1");
        assertSame(field3, spec.fieldSpecifications.get(2), "Third field should be in position 2");
    }

    /**
     * Tests addField() with null value.
     * Should add null to the list (no validation in the method).
     */
    @Test
    public void testAddFieldWithNull() {
        // Arrange
        ClassSpecification spec = new ClassSpecification();

        // Act
        spec.addField(null);

        // Assert
        assertNotNull(spec.fieldSpecifications, "Field specifications should not be null");
        assertEquals(1, spec.fieldSpecifications.size(), "Field specifications should have one element");
        assertNull(spec.fieldSpecifications.get(0), "Added field should be null");
    }

    // ========== addMethod() Tests ==========

    /**
     * Tests addMethod() on a specification with null methodSpecifications.
     * Verifies that a new list is created and the method is added.
     */
    @Test
    public void testAddMethodToNullList() {
        // Arrange
        ClassSpecification spec = new ClassSpecification();
        MemberSpecification method = new MemberSpecification(1, 2, "Annotation", "method", "()V");

        // Act
        spec.addMethod(method);

        // Assert
        assertNotNull(spec.methodSpecifications, "Method specifications should not be null after adding");
        assertEquals(1, spec.methodSpecifications.size(), "Method specifications should have one element");
        assertSame(method, spec.methodSpecifications.get(0), "Added method should be in the list");
    }

    /**
     * Tests addMethod() on a specification with an existing methodSpecifications list.
     * Verifies that the method is appended to the existing list.
     */
    @Test
    public void testAddMethodToExistingList() {
        // Arrange
        MemberSpecification method1 = new MemberSpecification(1, 2, "Annotation1", "method1", "()V");
        MemberSpecification method2 = new MemberSpecification(4, 8, "Annotation2", "method2", "(I)Z");
        List<MemberSpecification> methodSpecs = new ArrayList<>();
        methodSpecs.add(method1);
        ClassSpecification spec = new ClassSpecification(null, 0, 0, null, null, null, null, null, methodSpecs);

        // Act
        spec.addMethod(method2);

        // Assert
        assertEquals(2, spec.methodSpecifications.size(), "Method specifications should have two elements");
        assertSame(method1, spec.methodSpecifications.get(0), "First method should remain");
        assertSame(method2, spec.methodSpecifications.get(1), "Second method should be added");
    }

    /**
     * Tests adding multiple methods sequentially.
     */
    @Test
    public void testAddMultipleMethods() {
        // Arrange
        ClassSpecification spec = new ClassSpecification();
        MemberSpecification method1 = new MemberSpecification(1, 2, "Ann1", "m1", "()V");
        MemberSpecification method2 = new MemberSpecification(4, 8, "Ann2", "m2", "(I)I");
        MemberSpecification method3 = new MemberSpecification(16, 32, "Ann3", "m3", "(Ljava/lang/String;)V");

        // Act
        spec.addMethod(method1);
        spec.addMethod(method2);
        spec.addMethod(method3);

        // Assert
        assertNotNull(spec.methodSpecifications, "Method specifications should not be null");
        assertEquals(3, spec.methodSpecifications.size(), "Method specifications should have three elements");
        assertSame(method1, spec.methodSpecifications.get(0), "First method should be in position 0");
        assertSame(method2, spec.methodSpecifications.get(1), "Second method should be in position 1");
        assertSame(method3, spec.methodSpecifications.get(2), "Third method should be in position 2");
    }

    /**
     * Tests addMethod() with null value.
     * Should add null to the list (no validation in the method).
     */
    @Test
    public void testAddMethodWithNull() {
        // Arrange
        ClassSpecification spec = new ClassSpecification();

        // Act
        spec.addMethod(null);

        // Assert
        assertNotNull(spec.methodSpecifications, "Method specifications should not be null");
        assertEquals(1, spec.methodSpecifications.size(), "Method specifications should have one element");
        assertNull(spec.methodSpecifications.get(0), "Added method should be null");
    }

    /**
     * Tests adding both fields and methods to the same specification.
     */
    @Test
    public void testAddFieldsAndMethods() {
        // Arrange
        ClassSpecification spec = new ClassSpecification();
        MemberSpecification field = new MemberSpecification(1, 2, "FieldAnn", "field", "I");
        MemberSpecification method = new MemberSpecification(4, 8, "MethodAnn", "method", "()V");

        // Act
        spec.addField(field);
        spec.addMethod(method);

        // Assert
        assertNotNull(spec.fieldSpecifications, "Field specifications should not be null");
        assertNotNull(spec.methodSpecifications, "Method specifications should not be null");
        assertEquals(1, spec.fieldSpecifications.size(), "Field specifications should have one element");
        assertEquals(1, spec.methodSpecifications.size(), "Method specifications should have one element");
        assertSame(field, spec.fieldSpecifications.get(0), "Field should be in field specifications");
        assertSame(method, spec.methodSpecifications.get(0), "Method should be in method specifications");
    }

    // ========== equals() Tests ==========

    /**
     * Tests equals() with the same instance.
     * Should return true.
     */
    @Test
    public void testEqualsSameInstance() {
        // Arrange
        ClassSpecification spec = new ClassSpecification("Comment", 1, 2, "Ann", "Class", "ExtAnn", "ExtClass");

        // Act & Assert
        assertEquals(spec, spec, "Specification should equal itself");
    }

    /**
     * Tests equals() with null.
     * Should return false.
     */
    @Test
    public void testEqualsNull() {
        // Arrange
        ClassSpecification spec = new ClassSpecification();

        // Act & Assert
        assertNotEquals(null, spec, "Specification should not equal null");
    }

    /**
     * Tests equals() with an object of different class.
     * Should return false.
     */
    @Test
    public void testEqualsDifferentClass() {
        // Arrange
        ClassSpecification spec = new ClassSpecification();
        String other = "Not a ClassSpecification";

        // Act & Assert
        assertNotEquals(spec, other, "Specification should not equal a String");
    }

    /**
     * Tests equals() with two default specifications.
     * Should return true as all fields match.
     */
    @Test
    public void testEqualsDefaultSpecifications() {
        // Arrange
        ClassSpecification spec1 = new ClassSpecification();
        ClassSpecification spec2 = new ClassSpecification();

        // Act & Assert
        assertEquals(spec1, spec2, "Two default specifications should be equal");
    }

    /**
     * Tests equals() with matching specifications.
     * All fields are identical.
     */
    @Test
    public void testEqualsMatchingSpecifications() {
        // Arrange
        ClassSpecification spec1 = new ClassSpecification("Comment", 1, 2, "Ann", "Class", "ExtAnn", "ExtClass");
        ClassSpecification spec2 = new ClassSpecification("Comment", 1, 2, "Ann", "Class", "ExtAnn", "ExtClass");

        // Act & Assert
        assertEquals(spec1, spec2, "Specifications with matching fields should be equal");
    }

    /**
     * Tests equals() with different requiredSetAccessFlags.
     */
    @Test
    public void testEqualsDifferentSetAccessFlags() {
        // Arrange
        ClassSpecification spec1 = new ClassSpecification("Comment", 1, 2, "Ann", "Class", "ExtAnn", "ExtClass");
        ClassSpecification spec2 = new ClassSpecification("Comment", 4, 2, "Ann", "Class", "ExtAnn", "ExtClass");

        // Act & Assert
        assertNotEquals(spec1, spec2, "Specifications with different set access flags should not be equal");
    }

    /**
     * Tests equals() with different requiredUnsetAccessFlags.
     */
    @Test
    public void testEqualsDifferentUnsetAccessFlags() {
        // Arrange
        ClassSpecification spec1 = new ClassSpecification("Comment", 1, 2, "Ann", "Class", "ExtAnn", "ExtClass");
        ClassSpecification spec2 = new ClassSpecification("Comment", 1, 8, "Ann", "Class", "ExtAnn", "ExtClass");

        // Act & Assert
        assertNotEquals(spec1, spec2, "Specifications with different unset access flags should not be equal");
    }

    /**
     * Tests equals() with different annotationType.
     */
    @Test
    public void testEqualsDifferentAnnotationType() {
        // Arrange
        ClassSpecification spec1 = new ClassSpecification("Comment", 1, 2, "Ann1", "Class", "ExtAnn", "ExtClass");
        ClassSpecification spec2 = new ClassSpecification("Comment", 1, 2, "Ann2", "Class", "ExtAnn", "ExtClass");

        // Act & Assert
        assertNotEquals(spec1, spec2, "Specifications with different annotation types should not be equal");
    }

    /**
     * Tests equals() with one null annotationType and one non-null.
     */
    @Test
    public void testEqualsDifferentAnnotationTypeOneNull() {
        // Arrange
        ClassSpecification spec1 = new ClassSpecification("Comment", 1, 2, null, "Class", "ExtAnn", "ExtClass");
        ClassSpecification spec2 = new ClassSpecification("Comment", 1, 2, "Ann", "Class", "ExtAnn", "ExtClass");

        // Act & Assert
        assertNotEquals(spec1, spec2, "Specifications with one null annotation type should not be equal");
        assertNotEquals(spec2, spec1, "Reverse comparison should also not be equal");
    }

    /**
     * Tests equals() with both null annotationType.
     */
    @Test
    public void testEqualsBothAnnotationTypeNull() {
        // Arrange
        ClassSpecification spec1 = new ClassSpecification("Comment", 1, 2, null, "Class", "ExtAnn", "ExtClass");
        ClassSpecification spec2 = new ClassSpecification("Comment", 1, 2, null, "Class", "ExtAnn", "ExtClass");

        // Act & Assert
        assertEquals(spec1, spec2, "Specifications with both null annotation types should be equal");
    }

    /**
     * Tests equals() with different className.
     */
    @Test
    public void testEqualsDifferentClassName() {
        // Arrange
        ClassSpecification spec1 = new ClassSpecification("Comment", 1, 2, "Ann", "Class1", "ExtAnn", "ExtClass");
        ClassSpecification spec2 = new ClassSpecification("Comment", 1, 2, "Ann", "Class2", "ExtAnn", "ExtClass");

        // Act & Assert
        assertNotEquals(spec1, spec2, "Specifications with different class names should not be equal");
    }

    /**
     * Tests equals() with one null className and one non-null.
     */
    @Test
    public void testEqualsDifferentClassNameOneNull() {
        // Arrange
        ClassSpecification spec1 = new ClassSpecification("Comment", 1, 2, "Ann", null, "ExtAnn", "ExtClass");
        ClassSpecification spec2 = new ClassSpecification("Comment", 1, 2, "Ann", "Class", "ExtAnn", "ExtClass");

        // Act & Assert
        assertNotEquals(spec1, spec2, "Specifications with one null class name should not be equal");
    }

    /**
     * Tests equals() with both null className.
     */
    @Test
    public void testEqualsBothClassNameNull() {
        // Arrange
        ClassSpecification spec1 = new ClassSpecification("Comment", 1, 2, "Ann", null, "ExtAnn", "ExtClass");
        ClassSpecification spec2 = new ClassSpecification("Comment", 1, 2, "Ann", null, "ExtAnn", "ExtClass");

        // Act & Assert
        assertEquals(spec1, spec2, "Specifications with both null class names should be equal");
    }

    /**
     * Tests equals() with different extendsAnnotationType.
     */
    @Test
    public void testEqualsDifferentExtendsAnnotationType() {
        // Arrange
        ClassSpecification spec1 = new ClassSpecification("Comment", 1, 2, "Ann", "Class", "ExtAnn1", "ExtClass");
        ClassSpecification spec2 = new ClassSpecification("Comment", 1, 2, "Ann", "Class", "ExtAnn2", "ExtClass");

        // Act & Assert
        assertNotEquals(spec1, spec2, "Specifications with different extends annotation types should not be equal");
    }

    /**
     * Tests equals() with one null extendsAnnotationType and one non-null.
     */
    @Test
    public void testEqualsDifferentExtendsAnnotationTypeOneNull() {
        // Arrange
        ClassSpecification spec1 = new ClassSpecification("Comment", 1, 2, "Ann", "Class", null, "ExtClass");
        ClassSpecification spec2 = new ClassSpecification("Comment", 1, 2, "Ann", "Class", "ExtAnn", "ExtClass");

        // Act & Assert
        assertNotEquals(spec1, spec2, "Specifications with one null extends annotation type should not be equal");
    }

    /**
     * Tests equals() with both null extendsAnnotationType.
     */
    @Test
    public void testEqualsBothExtendsAnnotationTypeNull() {
        // Arrange
        ClassSpecification spec1 = new ClassSpecification("Comment", 1, 2, "Ann", "Class", null, "ExtClass");
        ClassSpecification spec2 = new ClassSpecification("Comment", 1, 2, "Ann", "Class", null, "ExtClass");

        // Act & Assert
        assertEquals(spec1, spec2, "Specifications with both null extends annotation types should be equal");
    }

    /**
     * Tests equals() with different extendsClassName.
     */
    @Test
    public void testEqualsDifferentExtendsClassName() {
        // Arrange
        ClassSpecification spec1 = new ClassSpecification("Comment", 1, 2, "Ann", "Class", "ExtAnn", "ExtClass1");
        ClassSpecification spec2 = new ClassSpecification("Comment", 1, 2, "Ann", "Class", "ExtAnn", "ExtClass2");

        // Act & Assert
        assertNotEquals(spec1, spec2, "Specifications with different extends class names should not be equal");
    }

    /**
     * Tests equals() with one null extendsClassName and one non-null.
     */
    @Test
    public void testEqualsDifferentExtendsClassNameOneNull() {
        // Arrange
        ClassSpecification spec1 = new ClassSpecification("Comment", 1, 2, "Ann", "Class", "ExtAnn", null);
        ClassSpecification spec2 = new ClassSpecification("Comment", 1, 2, "Ann", "Class", "ExtAnn", "ExtClass");

        // Act & Assert
        assertNotEquals(spec1, spec2, "Specifications with one null extends class name should not be equal");
    }

    /**
     * Tests equals() with both null extendsClassName.
     */
    @Test
    public void testEqualsBothExtendsClassNameNull() {
        // Arrange
        ClassSpecification spec1 = new ClassSpecification("Comment", 1, 2, "Ann", "Class", "ExtAnn", null);
        ClassSpecification spec2 = new ClassSpecification("Comment", 1, 2, "Ann", "Class", "ExtAnn", null);

        // Act & Assert
        assertEquals(spec1, spec2, "Specifications with both null extends class names should be equal");
    }

    /**
     * Tests equals() with matching fieldSpecifications.
     */
    @Test
    public void testEqualsMatchingFieldSpecifications() {
        // Arrange
        MemberSpecification field = new MemberSpecification(1, 2, "Ann", "field", "I");
        List<MemberSpecification> fields1 = new ArrayList<>();
        fields1.add(field);
        List<MemberSpecification> fields2 = new ArrayList<>();
        fields2.add(field);

        ClassSpecification spec1 = new ClassSpecification("C", 1, 2, "A", "C", "EA", "EC", fields1, null);
        ClassSpecification spec2 = new ClassSpecification("C", 1, 2, "A", "C", "EA", "EC", fields2, null);

        // Act & Assert
        assertEquals(spec1, spec2, "Specifications with equal field specifications should be equal");
    }

    /**
     * Tests equals() with different fieldSpecifications.
     */
    @Test
    public void testEqualsDifferentFieldSpecifications() {
        // Arrange
        MemberSpecification field1 = new MemberSpecification(1, 2, "Ann", "field1", "I");
        MemberSpecification field2 = new MemberSpecification(1, 2, "Ann", "field2", "I");
        List<MemberSpecification> fields1 = new ArrayList<>();
        fields1.add(field1);
        List<MemberSpecification> fields2 = new ArrayList<>();
        fields2.add(field2);

        ClassSpecification spec1 = new ClassSpecification("C", 1, 2, "A", "C", "EA", "EC", fields1, null);
        ClassSpecification spec2 = new ClassSpecification("C", 1, 2, "A", "C", "EA", "EC", fields2, null);

        // Act & Assert
        assertNotEquals(spec1, spec2, "Specifications with different field specifications should not be equal");
    }

    /**
     * Tests equals() with one null fieldSpecifications and one non-null.
     */
    @Test
    public void testEqualsDifferentFieldSpecificationsOneNull() {
        // Arrange
        MemberSpecification field = new MemberSpecification(1, 2, "Ann", "field", "I");
        List<MemberSpecification> fields = new ArrayList<>();
        fields.add(field);

        ClassSpecification spec1 = new ClassSpecification("C", 1, 2, "A", "C", "EA", "EC", null, null);
        ClassSpecification spec2 = new ClassSpecification("C", 1, 2, "A", "C", "EA", "EC", fields, null);

        // Act & Assert
        assertNotEquals(spec1, spec2, "Specifications with one null field specifications should not be equal");
    }

    /**
     * Tests equals() with both null fieldSpecifications.
     */
    @Test
    public void testEqualsBothFieldSpecificationsNull() {
        // Arrange
        ClassSpecification spec1 = new ClassSpecification("C", 1, 2, "A", "C", "EA", "EC", null, null);
        ClassSpecification spec2 = new ClassSpecification("C", 1, 2, "A", "C", "EA", "EC", null, null);

        // Act & Assert
        assertEquals(spec1, spec2, "Specifications with both null field specifications should be equal");
    }

    /**
     * Tests equals() with matching methodSpecifications.
     */
    @Test
    public void testEqualsMatchingMethodSpecifications() {
        // Arrange
        MemberSpecification method = new MemberSpecification(1, 2, "Ann", "method", "()V");
        List<MemberSpecification> methods1 = new ArrayList<>();
        methods1.add(method);
        List<MemberSpecification> methods2 = new ArrayList<>();
        methods2.add(method);

        ClassSpecification spec1 = new ClassSpecification("C", 1, 2, "A", "C", "EA", "EC", null, methods1);
        ClassSpecification spec2 = new ClassSpecification("C", 1, 2, "A", "C", "EA", "EC", null, methods2);

        // Act & Assert
        assertEquals(spec1, spec2, "Specifications with equal method specifications should be equal");
    }

    /**
     * Tests equals() with different methodSpecifications.
     */
    @Test
    public void testEqualsDifferentMethodSpecifications() {
        // Arrange
        MemberSpecification method1 = new MemberSpecification(1, 2, "Ann", "method1", "()V");
        MemberSpecification method2 = new MemberSpecification(1, 2, "Ann", "method2", "()V");
        List<MemberSpecification> methods1 = new ArrayList<>();
        methods1.add(method1);
        List<MemberSpecification> methods2 = new ArrayList<>();
        methods2.add(method2);

        ClassSpecification spec1 = new ClassSpecification("C", 1, 2, "A", "C", "EA", "EC", null, methods1);
        ClassSpecification spec2 = new ClassSpecification("C", 1, 2, "A", "C", "EA", "EC", null, methods2);

        // Act & Assert
        assertNotEquals(spec1, spec2, "Specifications with different method specifications should not be equal");
    }

    /**
     * Tests equals() with one null methodSpecifications and one non-null.
     */
    @Test
    public void testEqualsDifferentMethodSpecificationsOneNull() {
        // Arrange
        MemberSpecification method = new MemberSpecification(1, 2, "Ann", "method", "()V");
        List<MemberSpecification> methods = new ArrayList<>();
        methods.add(method);

        ClassSpecification spec1 = new ClassSpecification("C", 1, 2, "A", "C", "EA", "EC", null, null);
        ClassSpecification spec2 = new ClassSpecification("C", 1, 2, "A", "C", "EA", "EC", null, methods);

        // Act & Assert
        assertNotEquals(spec1, spec2, "Specifications with one null method specifications should not be equal");
    }

    /**
     * Tests equals() with both null methodSpecifications.
     */
    @Test
    public void testEqualsBothMethodSpecificationsNull() {
        // Arrange
        ClassSpecification spec1 = new ClassSpecification("C", 1, 2, "A", "C", "EA", "EC", null, null);
        ClassSpecification spec2 = new ClassSpecification("C", 1, 2, "A", "C", "EA", "EC", null, null);

        // Act & Assert
        assertEquals(spec1, spec2, "Specifications with both null method specifications should be equal");
    }

    // ========== hashCode() Tests ==========

    /**
     * Tests hashCode() consistency.
     * Multiple calls should return the same value.
     */
    @Test
    public void testHashCodeConsistency() {
        // Arrange
        ClassSpecification spec = new ClassSpecification("Comment", 1, 2, "Ann", "Class", "ExtAnn", "ExtClass");

        // Act
        int hash1 = spec.hashCode();
        int hash2 = spec.hashCode();

        // Assert
        assertEquals(hash1, hash2, "hashCode() should be consistent across multiple calls");
    }

    /**
     * Tests hashCode() for equal objects.
     * Equal objects must have equal hash codes.
     */
    @Test
    public void testHashCodeEqualObjects() {
        // Arrange
        ClassSpecification spec1 = new ClassSpecification("Comment", 1, 2, "Ann", "Class", "ExtAnn", "ExtClass");
        ClassSpecification spec2 = new ClassSpecification("Comment", 1, 2, "Ann", "Class", "ExtAnn", "ExtClass");

        // Act & Assert
        assertEquals(spec1, spec2, "Objects should be equal");
        assertEquals(spec1.hashCode(), spec2.hashCode(), "Equal objects should have equal hash codes");
    }

    /**
     * Tests hashCode() for default specifications.
     */
    @Test
    public void testHashCodeDefaultSpecifications() {
        // Arrange
        ClassSpecification spec1 = new ClassSpecification();
        ClassSpecification spec2 = new ClassSpecification();

        // Act & Assert
        assertEquals(spec1, spec2, "Default specifications should be equal");
        assertEquals(spec1.hashCode(), spec2.hashCode(), "Default specifications should have equal hash codes");
    }

    /**
     * Tests hashCode() with different requiredSetAccessFlags.
     * Different flags should likely produce different hash codes (not guaranteed but likely).
     */
    @Test
    public void testHashCodeDifferentSetAccessFlags() {
        // Arrange
        ClassSpecification spec1 = new ClassSpecification("C", 1, 2, "A", "C", "EA", "EC");
        ClassSpecification spec2 = new ClassSpecification("C", 4, 2, "A", "C", "EA", "EC");

        // Act
        int hash1 = spec1.hashCode();
        int hash2 = spec2.hashCode();

        // Assert - Different objects, likely different hash codes
        assertNotEquals(spec1, spec2, "Specifications should not be equal");
        // Note: Hash codes are not guaranteed to be different, but we can compute them
        // We won't assert inequality as collisions are allowed, just verify computation works
        assertNotNull(hash1);
        assertNotNull(hash2);
    }

    /**
     * Tests hashCode() with different requiredUnsetAccessFlags.
     */
    @Test
    public void testHashCodeDifferentUnsetAccessFlags() {
        // Arrange
        ClassSpecification spec1 = new ClassSpecification("C", 1, 2, "A", "C", "EA", "EC");
        ClassSpecification spec2 = new ClassSpecification("C", 1, 8, "A", "C", "EA", "EC");

        // Act
        int hash1 = spec1.hashCode();
        int hash2 = spec2.hashCode();

        // Assert
        assertNotEquals(spec1, spec2, "Specifications should not be equal");
    }

    /**
     * Tests hashCode() with null fields.
     */
    @Test
    public void testHashCodeWithNullFields() {
        // Arrange
        ClassSpecification spec = new ClassSpecification(null, 0, 0, null, null, null, null, null, null);

        // Act
        int hash = spec.hashCode();

        // Assert - Should compute without throwing exception
        // Hash of all nulls and zeros is 0
        assertEquals(0, hash, "hashCode() of all nulls should be 0");
    }

    /**
     * Tests hashCode() with fieldSpecifications.
     */
    @Test
    public void testHashCodeWithFieldSpecifications() {
        // Arrange
        MemberSpecification field = new MemberSpecification(1, 2, "Ann", "field", "I");
        List<MemberSpecification> fields = new ArrayList<>();
        fields.add(field);
        ClassSpecification spec = new ClassSpecification("C", 1, 2, "A", "C", "EA", "EC", fields, null);

        // Act
        int hash = spec.hashCode();

        // Assert - Should compute without throwing exception
        assertNotEquals(0, hash, "hashCode() with field specifications should be non-zero");
    }

    /**
     * Tests hashCode() with methodSpecifications.
     */
    @Test
    public void testHashCodeWithMethodSpecifications() {
        // Arrange
        MemberSpecification method = new MemberSpecification(1, 2, "Ann", "method", "()V");
        List<MemberSpecification> methods = new ArrayList<>();
        methods.add(method);
        ClassSpecification spec = new ClassSpecification("C", 1, 2, "A", "C", "EA", "EC", null, methods);

        // Act
        int hash = spec.hashCode();

        // Assert - Should compute without throwing exception
        assertNotEquals(0, hash, "hashCode() with method specifications should be non-zero");
    }

    /**
     * Tests hashCode() with both field and method specifications.
     */
    @Test
    public void testHashCodeWithBothSpecifications() {
        // Arrange
        MemberSpecification field = new MemberSpecification(1, 2, "FAnn", "field", "I");
        MemberSpecification method = new MemberSpecification(4, 8, "MAnn", "method", "()V");
        List<MemberSpecification> fields = new ArrayList<>();
        fields.add(field);
        List<MemberSpecification> methods = new ArrayList<>();
        methods.add(method);
        ClassSpecification spec = new ClassSpecification("C", 1, 2, "A", "C", "EA", "EC", fields, methods);

        // Act
        int hash = spec.hashCode();

        // Assert - Should compute without throwing exception
        assertNotEquals(0, hash, "hashCode() with both specifications should be non-zero");
    }

    // ========== clone() Tests ==========

    /**
     * Tests clone() creates a new object.
     * The clone should not be the same instance as the original.
     */
    @Test
    public void testCloneCreatesNewObject() {
        // Arrange
        ClassSpecification original = new ClassSpecification("Comment", 1, 2, "Ann", "Class", "ExtAnn", "ExtClass");

        // Act
        Object cloned = original.clone();

        // Assert
        assertNotNull(cloned, "Clone should not be null");
        assertNotSame(original, cloned, "Clone should be a different instance");
        assertTrue(cloned instanceof ClassSpecification, "Clone should be a ClassSpecification");
    }

    /**
     * Tests clone() creates an equal object.
     * The clone should be equal to the original according to equals().
     */
    @Test
    public void testCloneCreatesEqualObject() {
        // Arrange
        ClassSpecification original = new ClassSpecification("Comment", 1, 2, "Ann", "Class", "ExtAnn", "ExtClass");

        // Act
        ClassSpecification cloned = (ClassSpecification) original.clone();

        // Assert
        assertEquals(original, cloned, "Clone should be equal to original");
    }

    /**
     * Tests clone() with default specification.
     */
    @Test
    public void testCloneDefaultSpecification() {
        // Arrange
        ClassSpecification original = new ClassSpecification();

        // Act
        ClassSpecification cloned = (ClassSpecification) original.clone();

        // Assert
        assertNotNull(cloned, "Clone should not be null");
        assertNotSame(original, cloned, "Clone should be a different instance");
        assertEquals(original, cloned, "Clone should be equal to original");
        assertNull(cloned.comments, "Cloned comments should be null");
        assertNull(cloned.className, "Cloned class name should be null");
    }

    /**
     * Tests clone() with fieldSpecifications.
     * Note: clone() uses Object.clone() which performs shallow copy.
     */
    @Test
    public void testCloneWithFieldSpecifications() {
        // Arrange
        MemberSpecification field = new MemberSpecification(1, 2, "Ann", "field", "I");
        List<MemberSpecification> fields = new ArrayList<>();
        fields.add(field);
        ClassSpecification original = new ClassSpecification("C", 1, 2, "A", "C", "EA", "EC", fields, null);

        // Act
        ClassSpecification cloned = (ClassSpecification) original.clone();

        // Assert
        assertNotNull(cloned, "Clone should not be null");
        assertNotSame(original, cloned, "Clone should be a different instance");
        assertEquals(original, cloned, "Clone should be equal to original");
        // Shallow copy means the list reference is the same
        assertSame(original.fieldSpecifications, cloned.fieldSpecifications, "Field specifications should be shallow copied");
    }

    /**
     * Tests clone() with methodSpecifications.
     */
    @Test
    public void testCloneWithMethodSpecifications() {
        // Arrange
        MemberSpecification method = new MemberSpecification(1, 2, "Ann", "method", "()V");
        List<MemberSpecification> methods = new ArrayList<>();
        methods.add(method);
        ClassSpecification original = new ClassSpecification("C", 1, 2, "A", "C", "EA", "EC", null, methods);

        // Act
        ClassSpecification cloned = (ClassSpecification) original.clone();

        // Assert
        assertNotNull(cloned, "Clone should not be null");
        assertNotSame(original, cloned, "Clone should be a different instance");
        assertEquals(original, cloned, "Clone should be equal to original");
        // Shallow copy means the list reference is the same
        assertSame(original.methodSpecifications, cloned.methodSpecifications, "Method specifications should be shallow copied");
    }

    /**
     * Tests clone() with all fields populated.
     */
    @Test
    public void testCloneWithAllFields() {
        // Arrange
        MemberSpecification field = new MemberSpecification(1, 2, "FAnn", "field", "I");
        MemberSpecification method = new MemberSpecification(4, 8, "MAnn", "method", "()V");
        List<MemberSpecification> fields = new ArrayList<>();
        fields.add(field);
        List<MemberSpecification> methods = new ArrayList<>();
        methods.add(method);
        ClassSpecification original = new ClassSpecification("Comment", 16, 32, "Ann", "Class", "ExtAnn", "ExtClass", fields, methods);

        // Act
        ClassSpecification cloned = (ClassSpecification) original.clone();

        // Assert
        assertNotNull(cloned, "Clone should not be null");
        assertNotSame(original, cloned, "Clone should be a different instance");
        assertEquals(original, cloned, "Clone should be equal to original");
        assertEquals(original.comments, cloned.comments, "Comments should be equal");
        assertEquals(original.requiredSetAccessFlags, cloned.requiredSetAccessFlags, "Set access flags should be equal");
        assertEquals(original.requiredUnsetAccessFlags, cloned.requiredUnsetAccessFlags, "Unset access flags should be equal");
        assertEquals(original.annotationType, cloned.annotationType, "Annotation type should be equal");
        assertEquals(original.className, cloned.className, "Class name should be equal");
        assertEquals(original.extendsAnnotationType, cloned.extendsAnnotationType, "Extends annotation type should be equal");
        assertEquals(original.extendsClassName, cloned.extendsClassName, "Extends class name should be equal");
        assertSame(original.fieldSpecifications, cloned.fieldSpecifications, "Field specifications should be shallow copied");
        assertSame(original.methodSpecifications, cloned.methodSpecifications, "Method specifications should be shallow copied");
    }

    /**
     * Tests modifying the clone doesn't affect the original.
     * Tests modifiable fields (not final).
     */
    @Test
    public void testCloneIndependenceModifiableFields() {
        // Arrange
        ClassSpecification original = new ClassSpecification("Comment", 1, 2, "Ann", "Class", "ExtAnn", "ExtClass");
        ClassSpecification cloned = (ClassSpecification) original.clone();

        // Act - Modify cloned object's modifiable fields
        cloned.memberComments = "Modified member comments";
        cloned.requiredSetAccessFlags = 99;
        cloned.requiredUnsetAccessFlags = 88;
        cloned.className = "ModifiedClass";

        // Assert - Original should remain unchanged
        assertNull(original.memberComments, "Original member comments should remain null");
        assertEquals(1, original.requiredSetAccessFlags, "Original set access flags should remain 1");
        assertEquals(2, original.requiredUnsetAccessFlags, "Original unset access flags should remain 2");
        assertEquals("Class", original.className, "Original class name should remain unchanged");
    }

    /**
     * Tests clone() hashCode consistency.
     * Clone should have the same hashCode as the original.
     */
    @Test
    public void testCloneHashCodeConsistency() {
        // Arrange
        ClassSpecification original = new ClassSpecification("Comment", 1, 2, "Ann", "Class", "ExtAnn", "ExtClass");

        // Act
        ClassSpecification cloned = (ClassSpecification) original.clone();

        // Assert
        assertEquals(original.hashCode(), cloned.hashCode(), "Clone should have the same hashCode as original");
    }

    // ========== Integration Tests ==========

    /**
     * Integration test: Create specification, add fields and methods, verify all operations.
     */
    @Test
    public void testCompleteWorkflow() {
        // Arrange & Act - Create specification
        ClassSpecification spec = new ClassSpecification(
            "Test class specification",
            1,
            2,
            "TestAnnotation",
            "com.example.TestClass",
            "ExtendsAnnotation",
            "java.lang.Object"
        );

        // Act - Add fields
        MemberSpecification field1 = new MemberSpecification(1, 2, "FieldAnn1", "field1", "I");
        MemberSpecification field2 = new MemberSpecification(4, 8, "FieldAnn2", "field2", "Ljava/lang/String;");
        spec.addField(field1);
        spec.addField(field2);

        // Act - Add methods
        MemberSpecification method1 = new MemberSpecification(1, 2, "MethodAnn1", "method1", "()V");
        MemberSpecification method2 = new MemberSpecification(4, 8, "MethodAnn2", "method2", "(I)I");
        spec.addMethod(method1);
        spec.addMethod(method2);

        // Assert - Verify all fields
        assertEquals("Test class specification", spec.comments);
        assertEquals(1, spec.requiredSetAccessFlags);
        assertEquals(2, spec.requiredUnsetAccessFlags);
        assertEquals("TestAnnotation", spec.annotationType);
        assertEquals("com.example.TestClass", spec.className);
        assertEquals("ExtendsAnnotation", spec.extendsAnnotationType);
        assertEquals("java.lang.Object", spec.extendsClassName);
        assertNotNull(spec.fieldSpecifications);
        assertNotNull(spec.methodSpecifications);
        assertEquals(2, spec.fieldSpecifications.size());
        assertEquals(2, spec.methodSpecifications.size());
        assertSame(field1, spec.fieldSpecifications.get(0));
        assertSame(field2, spec.fieldSpecifications.get(1));
        assertSame(method1, spec.methodSpecifications.get(0));
        assertSame(method2, spec.methodSpecifications.get(1));

        // Act - Clone the specification
        ClassSpecification cloned = (ClassSpecification) spec.clone();

        // Assert - Verify clone
        assertEquals(spec, cloned);
        assertNotSame(spec, cloned);
        assertSame(spec.fieldSpecifications, cloned.fieldSpecifications);
        assertSame(spec.methodSpecifications, cloned.methodSpecifications);
    }

    /**
     * Integration test: Test equals and hashCode contract with complex specifications.
     */
    @Test
    public void testEqualsHashCodeContract() {
        // Arrange - Create three equal specifications
        MemberSpecification field = new MemberSpecification(1, 2, "Ann", "field", "I");
        List<MemberSpecification> fields1 = new ArrayList<>();
        fields1.add(field);
        List<MemberSpecification> fields2 = new ArrayList<>();
        fields2.add(field);
        List<MemberSpecification> fields3 = new ArrayList<>();
        fields3.add(field);

        ClassSpecification spec1 = new ClassSpecification("C", 1, 2, "A", "C", "EA", "EC", fields1, null);
        ClassSpecification spec2 = new ClassSpecification("C", 1, 2, "A", "C", "EA", "EC", fields2, null);
        ClassSpecification spec3 = new ClassSpecification("C", 1, 2, "A", "C", "EA", "EC", fields3, null);

        // Assert - Reflexive
        assertEquals(spec1, spec1, "Specification should equal itself");

        // Assert - Symmetric
        assertEquals(spec1, spec2, "spec1 should equal spec2");
        assertEquals(spec2, spec1, "spec2 should equal spec1");

        // Assert - Transitive
        assertEquals(spec1, spec2, "spec1 should equal spec2");
        assertEquals(spec2, spec3, "spec2 should equal spec3");
        assertEquals(spec1, spec3, "spec1 should equal spec3");

        // Assert - Consistent hashCode
        assertEquals(spec1.hashCode(), spec2.hashCode(), "Equal objects should have equal hash codes");
        assertEquals(spec2.hashCode(), spec3.hashCode(), "Equal objects should have equal hash codes");
        assertEquals(spec1.hashCode(), spec3.hashCode(), "Equal objects should have equal hash codes");

        // Assert - Null comparison
        assertNotEquals(null, spec1, "Specification should not equal null");
    }

    /**
     * Integration test: Test copy constructor with modified original.
     */
    @Test
    public void testCopyConstructorIndependence() {
        // Arrange
        ClassSpecification original = new ClassSpecification("Original", 1, 2, "Ann", "Class", "ExtAnn", "ExtClass");
        original.addField(new MemberSpecification(1, 2, "FAnn", "field", "I"));
        original.addMethod(new MemberSpecification(4, 8, "MAnn", "method", "()V"));

        // Act - Create copy
        ClassSpecification copy = new ClassSpecification(original);

        // Act - Modify original's modifiable fields
        original.memberComments = "New member comments";
        original.requiredSetAccessFlags = 99;
        original.requiredUnsetAccessFlags = 88;
        original.className = "ModifiedClass";

        // Assert - Copy should reflect original values at time of copy
        assertEquals("Original", copy.comments);
        assertEquals(1, copy.requiredSetAccessFlags);
        assertEquals(2, copy.requiredUnsetAccessFlags);
        assertEquals("Class", copy.className);
        assertNull(copy.memberComments);

        // Note: Lists are shared (shallow copy), so adding to original affects copy
        assertSame(original.fieldSpecifications, copy.fieldSpecifications);
        assertSame(original.methodSpecifications, copy.methodSpecifications);
    }
}
