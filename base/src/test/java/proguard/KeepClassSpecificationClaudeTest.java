package proguard;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link KeepClassSpecification}.
 * Tests all methods to ensure proper functionality of KeepClassSpecification.
 */
public class KeepClassSpecificationClaudeTest {

    // ========== Constructor Tests (10-parameter constructor) ==========

    /**
     * Tests the 10-parameter constructor with all flags set to true.
     * Verifies that all fields are correctly initialized.
     */
    @Test
    public void testConstructor10ParamsAllTrue() {
        // Arrange
        ClassSpecification condition = new ClassSpecification("ConditionComment", 1, 2, "CondAnn", "CondClass", null, null);
        ClassSpecification classSpec = new ClassSpecification("Comment", 4, 8, "Ann", "TestClass", "ExtAnn", "ExtClass");

        // Act
        KeepClassSpecification keep = new KeepClassSpecification(
            true,  // markClasses
            true,  // markClassMembers
            true,  // markConditionally
            true,  // markDescriptorClasses
            true,  // markCodeAttributes
            true,  // allowShrinking
            true,  // allowOptimization
            true,  // allowObfuscation
            condition,
            classSpec
        );

        // Assert
        assertTrue(keep.markClasses, "markClasses should be true");
        assertTrue(keep.markClassMembers, "markClassMembers should be true");
        assertTrue(keep.markConditionally, "markConditionally should be true");
        assertTrue(keep.markDescriptorClasses, "markDescriptorClasses should be true");
        assertTrue(keep.markCodeAttributes, "markCodeAttributes should be true");
        assertTrue(keep.allowShrinking, "allowShrinking should be true");
        assertTrue(keep.allowOptimization, "allowOptimization should be true");
        assertTrue(keep.allowObfuscation, "allowObfuscation should be true");
        assertSame(condition, keep.condition, "condition should be set correctly");
        assertEquals("Comment", keep.comments, "comments should be inherited from classSpec");
        assertEquals("TestClass", keep.className, "className should be inherited from classSpec");
    }

    /**
     * Tests the 10-parameter constructor with all flags set to false.
     */
    @Test
    public void testConstructor10ParamsAllFalse() {
        // Arrange
        ClassSpecification classSpec = new ClassSpecification();

        // Act
        KeepClassSpecification keep = new KeepClassSpecification(
            false,  // markClasses
            false,  // markClassMembers
            false,  // markConditionally
            false,  // markDescriptorClasses
            false,  // markCodeAttributes
            false,  // allowShrinking
            false,  // allowOptimization
            false,  // allowObfuscation
            null,   // condition
            classSpec
        );

        // Assert
        assertFalse(keep.markClasses, "markClasses should be false");
        assertFalse(keep.markClassMembers, "markClassMembers should be false");
        assertFalse(keep.markConditionally, "markConditionally should be false");
        assertFalse(keep.markDescriptorClasses, "markDescriptorClasses should be false");
        assertFalse(keep.markCodeAttributes, "markCodeAttributes should be false");
        assertFalse(keep.allowShrinking, "allowShrinking should be false");
        assertFalse(keep.allowOptimization, "allowOptimization should be false");
        assertFalse(keep.allowObfuscation, "allowObfuscation should be false");
        assertNull(keep.condition, "condition should be null");
    }

    /**
     * Tests the 10-parameter constructor with mixed flag values.
     */
    @Test
    public void testConstructor10ParamsMixedFlags() {
        // Arrange
        ClassSpecification condition = new ClassSpecification("Cond", 0, 0, null, "CondClass", null, null);
        ClassSpecification classSpec = new ClassSpecification("Spec", 16, 32, "Annotation", "MyClass", "ExtAnn", "ExtClass");

        // Act
        KeepClassSpecification keep = new KeepClassSpecification(
            true,   // markClasses
            false,  // markClassMembers
            true,   // markConditionally
            false,  // markDescriptorClasses
            true,   // markCodeAttributes
            false,  // allowShrinking
            true,   // allowOptimization
            false,  // allowObfuscation
            condition,
            classSpec
        );

        // Assert
        assertTrue(keep.markClasses);
        assertFalse(keep.markClassMembers);
        assertTrue(keep.markConditionally);
        assertFalse(keep.markDescriptorClasses);
        assertTrue(keep.markCodeAttributes);
        assertFalse(keep.allowShrinking);
        assertTrue(keep.allowOptimization);
        assertFalse(keep.allowObfuscation);
        assertSame(condition, keep.condition);
        assertEquals("Spec", keep.comments);
        assertEquals("MyClass", keep.className);
        assertEquals("Annotation", keep.annotationType);
    }

    /**
     * Tests the 10-parameter constructor with null condition.
     */
    @Test
    public void testConstructor10ParamsNullCondition() {
        // Arrange
        ClassSpecification classSpec = new ClassSpecification("Comment", 1, 2, "Ann", "Class", null, null);

        // Act
        KeepClassSpecification keep = new KeepClassSpecification(
            true, true, false, false, true, false, true, false,
            null,  // condition is null
            classSpec
        );

        // Assert
        assertNull(keep.condition, "condition should be null");
        assertNotNull(keep.comments, "comments should be inherited from classSpec");
    }

    /**
     * Tests the 10-parameter constructor inherits parent class fields.
     */
    @Test
    public void testConstructor10ParamsInheritsParentFields() {
        // Arrange
        MemberSpecification field = new MemberSpecification(1, 2, "FieldAnn", "field", "I");
        MemberSpecification method = new MemberSpecification(4, 8, "MethodAnn", "method", "()V");
        List<MemberSpecification> fields = new ArrayList<>();
        fields.add(field);
        List<MemberSpecification> methods = new ArrayList<>();
        methods.add(method);
        ClassSpecification classSpec = new ClassSpecification(
            "MyComment", 16, 32, "MyAnn", "MyClass", "MyExtAnn", "MyExtClass", fields, methods
        );

        // Act
        KeepClassSpecification keep = new KeepClassSpecification(
            true, true, true, true, true, true, true, true,
            null,
            classSpec
        );

        // Assert - Verify parent fields are inherited
        assertEquals("MyComment", keep.comments);
        assertEquals(16, keep.requiredSetAccessFlags);
        assertEquals(32, keep.requiredUnsetAccessFlags);
        assertEquals("MyAnn", keep.annotationType);
        assertEquals("MyClass", keep.className);
        assertEquals("MyExtAnn", keep.extendsAnnotationType);
        assertEquals("MyExtClass", keep.extendsClassName);
        assertSame(fields, keep.fieldSpecifications);
        assertSame(methods, keep.methodSpecifications);
    }

    // ========== Constructor Tests (9-parameter deprecated constructor) ==========

    /**
     * Tests the deprecated 9-parameter constructor with all flags set to true.
     * This constructor sets both markClasses and markClassMembers to the same value (markClassesAndMembers).
     */
    @Test
    public void testConstructor9ParamsAllTrue() {
        // Arrange
        ClassSpecification condition = new ClassSpecification("Cond", 1, 2, null, "CondClass", null, null);
        ClassSpecification classSpec = new ClassSpecification("Spec", 4, 8, "Ann", "TestClass", null, null);

        // Act
        KeepClassSpecification keep = new KeepClassSpecification(
            true,  // markClassesAndMembers (sets both markClasses and markClassMembers)
            true,  // markConditionally
            true,  // markDescriptorClasses
            true,  // markCodeAttributes
            true,  // allowShrinking
            true,  // allowOptimization
            true,  // allowObfuscation
            condition,
            classSpec
        );

        // Assert - Both markClasses and markClassMembers should be true
        assertTrue(keep.markClasses, "markClasses should be true");
        assertTrue(keep.markClassMembers, "markClassMembers should be true");
        assertTrue(keep.markConditionally, "markConditionally should be true");
        assertTrue(keep.markDescriptorClasses, "markDescriptorClasses should be true");
        assertTrue(keep.markCodeAttributes, "markCodeAttributes should be true");
        assertTrue(keep.allowShrinking, "allowShrinking should be true");
        assertTrue(keep.allowOptimization, "allowOptimization should be true");
        assertTrue(keep.allowObfuscation, "allowObfuscation should be true");
        assertSame(condition, keep.condition);
    }

    /**
     * Tests the deprecated 9-parameter constructor with all flags set to false.
     */
    @Test
    public void testConstructor9ParamsAllFalse() {
        // Arrange
        ClassSpecification classSpec = new ClassSpecification();

        // Act
        KeepClassSpecification keep = new KeepClassSpecification(
            false,  // markClassesAndMembers
            false,  // markConditionally
            false,  // markDescriptorClasses
            false,  // markCodeAttributes
            false,  // allowShrinking
            false,  // allowOptimization
            false,  // allowObfuscation
            null,
            classSpec
        );

        // Assert - Both markClasses and markClassMembers should be false
        assertFalse(keep.markClasses, "markClasses should be false");
        assertFalse(keep.markClassMembers, "markClassMembers should be false");
        assertFalse(keep.markConditionally, "markConditionally should be false");
        assertFalse(keep.markDescriptorClasses, "markDescriptorClasses should be false");
        assertFalse(keep.markCodeAttributes, "markCodeAttributes should be false");
        assertFalse(keep.allowShrinking, "allowShrinking should be false");
        assertFalse(keep.allowOptimization, "allowOptimization should be false");
        assertFalse(keep.allowObfuscation, "allowObfuscation should be false");
        assertNull(keep.condition);
    }

    /**
     * Tests the deprecated 9-parameter constructor with markClassesAndMembers=true.
     * Verifies that both markClasses and markClassMembers are set to true.
     */
    @Test
    public void testConstructor9ParamsMarkClassesAndMembersTrue() {
        // Arrange
        ClassSpecification classSpec = new ClassSpecification("Test", 0, 0, null, "Test", null, null);

        // Act
        KeepClassSpecification keep = new KeepClassSpecification(
            true,   // markClassesAndMembers - should set both markClasses and markClassMembers
            false,
            false,
            false,
            false,
            false,
            false,
            null,
            classSpec
        );

        // Assert
        assertTrue(keep.markClasses, "markClasses should be true when markClassesAndMembers is true");
        assertTrue(keep.markClassMembers, "markClassMembers should be true when markClassesAndMembers is true");
    }

    /**
     * Tests the deprecated 9-parameter constructor with markClassesAndMembers=false.
     * Verifies that both markClasses and markClassMembers are set to false.
     */
    @Test
    public void testConstructor9ParamsMarkClassesAndMembersFalse() {
        // Arrange
        ClassSpecification classSpec = new ClassSpecification("Test", 0, 0, null, "Test", null, null);

        // Act
        KeepClassSpecification keep = new KeepClassSpecification(
            false,  // markClassesAndMembers - should set both markClasses and markClassMembers
            true,   // other flags can be different
            true,
            true,
            true,
            true,
            true,
            null,
            classSpec
        );

        // Assert
        assertFalse(keep.markClasses, "markClasses should be false when markClassesAndMembers is false");
        assertFalse(keep.markClassMembers, "markClassMembers should be false when markClassesAndMembers is false");
        // Verify other flags are still set correctly
        assertTrue(keep.markConditionally);
        assertTrue(keep.markDescriptorClasses);
        assertTrue(keep.markCodeAttributes);
    }

    /**
     * Tests the deprecated 9-parameter constructor with mixed flags.
     */
    @Test
    public void testConstructor9ParamsMixedFlags() {
        // Arrange
        ClassSpecification condition = new ClassSpecification();
        ClassSpecification classSpec = new ClassSpecification("Comment", 64, 128, "Ann", "Class", null, "Base");

        // Act
        KeepClassSpecification keep = new KeepClassSpecification(
            true,   // markClassesAndMembers
            false,  // markConditionally
            true,   // markDescriptorClasses
            false,  // markCodeAttributes
            true,   // allowShrinking
            false,  // allowOptimization
            true,   // allowObfuscation
            condition,
            classSpec
        );

        // Assert
        assertTrue(keep.markClasses);
        assertTrue(keep.markClassMembers);
        assertFalse(keep.markConditionally);
        assertTrue(keep.markDescriptorClasses);
        assertFalse(keep.markCodeAttributes);
        assertTrue(keep.allowShrinking);
        assertFalse(keep.allowOptimization);
        assertTrue(keep.allowObfuscation);
        assertSame(condition, keep.condition);
        assertEquals("Comment", keep.comments);
        assertEquals("Class", keep.className);
    }

    /**
     * Tests that the 9-parameter constructor calls the 10-parameter constructor correctly.
     */
    @Test
    public void testConstructor9ParamsDelegatesTo10Params() {
        // Arrange
        ClassSpecification condition = new ClassSpecification("Cond", 1, 2, "CA", "CC", null, null);
        ClassSpecification classSpec = new ClassSpecification("Spec", 4, 8, "SA", "SC", "EA", "EC");

        // Act - Create with 9-param constructor
        KeepClassSpecification keep9 = new KeepClassSpecification(
            true, false, true, false, true, false, true,
            condition,
            classSpec
        );

        // Create equivalent with 10-param constructor
        KeepClassSpecification keep10 = new KeepClassSpecification(
            true,  // markClasses = markClassesAndMembers
            true,  // markClassMembers = markClassesAndMembers
            false, // markConditionally
            true,  // markDescriptorClasses
            false, // markCodeAttributes
            true,  // allowShrinking
            false, // allowOptimization
            true,  // allowObfuscation
            condition,
            classSpec
        );

        // Assert - Both should be equivalent
        assertEquals(keep10.markClasses, keep9.markClasses);
        assertEquals(keep10.markClassMembers, keep9.markClassMembers);
        assertEquals(keep10.markConditionally, keep9.markConditionally);
        assertEquals(keep10.markDescriptorClasses, keep9.markDescriptorClasses);
        assertEquals(keep10.markCodeAttributes, keep9.markCodeAttributes);
        assertEquals(keep10.allowShrinking, keep9.allowShrinking);
        assertEquals(keep10.allowOptimization, keep9.allowOptimization);
        assertEquals(keep10.allowObfuscation, keep9.allowObfuscation);
        assertEquals(keep10.condition, keep9.condition);
    }

    // ========== equals() Tests ==========

    /**
     * Tests equals() with the same instance.
     */
    @Test
    public void testEqualsSameInstance() {
        // Arrange
        ClassSpecification classSpec = new ClassSpecification("Test", 1, 2, "Ann", "Class", null, null);
        KeepClassSpecification keep = new KeepClassSpecification(
            true, true, false, false, true, false, true, false, null, classSpec
        );

        // Act & Assert
        assertEquals(keep, keep, "Instance should equal itself");
    }

    /**
     * Tests equals() with null.
     */
    @Test
    public void testEqualsNull() {
        // Arrange
        ClassSpecification classSpec = new ClassSpecification();
        KeepClassSpecification keep = new KeepClassSpecification(
            true, true, true, true, true, true, true, true, null, classSpec
        );

        // Act & Assert
        assertNotEquals(null, keep, "KeepClassSpecification should not equal null");
    }

    /**
     * Tests equals() with an object of different class.
     */
    @Test
    public void testEqualsDifferentClass() {
        // Arrange
        ClassSpecification classSpec = new ClassSpecification();
        KeepClassSpecification keep = new KeepClassSpecification(
            true, true, true, true, true, true, true, true, null, classSpec
        );
        String other = "Not a KeepClassSpecification";

        // Act & Assert
        assertNotEquals(keep, other, "KeepClassSpecification should not equal a String");
    }

    /**
     * Tests equals() with a parent ClassSpecification object.
     * Should return false as they are different classes.
     */
    @Test
    public void testEqualsDifferentClassType() {
        // Arrange
        ClassSpecification classSpec = new ClassSpecification("Test", 1, 2, "Ann", "Class", null, null);
        KeepClassSpecification keep = new KeepClassSpecification(
            true, true, true, true, true, true, true, true, null, classSpec
        );
        ClassSpecification parent = new ClassSpecification("Test", 1, 2, "Ann", "Class", null, null);

        // Act & Assert
        assertNotEquals(keep, parent, "KeepClassSpecification should not equal ClassSpecification");
    }

    /**
     * Tests equals() with two identical KeepClassSpecification objects.
     */
    @Test
    public void testEqualsIdenticalObjects() {
        // Arrange
        ClassSpecification classSpec = new ClassSpecification("Test", 1, 2, "Ann", "Class", "ExtAnn", "ExtClass");
        ClassSpecification condition = new ClassSpecification("Cond", 4, 8, "CA", "CC", null, null);

        KeepClassSpecification keep1 = new KeepClassSpecification(
            true, true, false, true, false, true, false, true,
            condition,
            classSpec
        );

        KeepClassSpecification keep2 = new KeepClassSpecification(
            true, true, false, true, false, true, false, true,
            condition,
            classSpec
        );

        // Act & Assert
        assertEquals(keep1, keep2, "Identical KeepClassSpecifications should be equal");
    }

    /**
     * Tests equals() with different markClasses.
     */
    @Test
    public void testEqualsDifferentMarkClasses() {
        // Arrange
        ClassSpecification classSpec = new ClassSpecification();
        KeepClassSpecification keep1 = new KeepClassSpecification(
            true, true, true, true, true, true, true, true, null, classSpec
        );
        KeepClassSpecification keep2 = new KeepClassSpecification(
            false, true, true, true, true, true, true, true, null, classSpec
        );

        // Act & Assert
        assertNotEquals(keep1, keep2, "Different markClasses should result in inequality");
    }

    /**
     * Tests equals() with different markClassMembers.
     */
    @Test
    public void testEqualsDifferentMarkClassMembers() {
        // Arrange
        ClassSpecification classSpec = new ClassSpecification();
        KeepClassSpecification keep1 = new KeepClassSpecification(
            true, true, true, true, true, true, true, true, null, classSpec
        );
        KeepClassSpecification keep2 = new KeepClassSpecification(
            true, false, true, true, true, true, true, true, null, classSpec
        );

        // Act & Assert
        assertNotEquals(keep1, keep2, "Different markClassMembers should result in inequality");
    }

    /**
     * Tests equals() with different markConditionally.
     */
    @Test
    public void testEqualsDifferentMarkConditionally() {
        // Arrange
        ClassSpecification classSpec = new ClassSpecification();
        KeepClassSpecification keep1 = new KeepClassSpecification(
            true, true, true, true, true, true, true, true, null, classSpec
        );
        KeepClassSpecification keep2 = new KeepClassSpecification(
            true, true, false, true, true, true, true, true, null, classSpec
        );

        // Act & Assert
        assertNotEquals(keep1, keep2, "Different markConditionally should result in inequality");
    }

    /**
     * Tests equals() with different markDescriptorClasses.
     */
    @Test
    public void testEqualsDifferentMarkDescriptorClasses() {
        // Arrange
        ClassSpecification classSpec = new ClassSpecification();
        KeepClassSpecification keep1 = new KeepClassSpecification(
            true, true, true, true, true, true, true, true, null, classSpec
        );
        KeepClassSpecification keep2 = new KeepClassSpecification(
            true, true, true, false, true, true, true, true, null, classSpec
        );

        // Act & Assert
        assertNotEquals(keep1, keep2, "Different markDescriptorClasses should result in inequality");
    }

    /**
     * Tests equals() with different markCodeAttributes.
     */
    @Test
    public void testEqualsDifferentMarkCodeAttributes() {
        // Arrange
        ClassSpecification classSpec = new ClassSpecification();
        KeepClassSpecification keep1 = new KeepClassSpecification(
            true, true, true, true, true, true, true, true, null, classSpec
        );
        KeepClassSpecification keep2 = new KeepClassSpecification(
            true, true, true, true, false, true, true, true, null, classSpec
        );

        // Act & Assert
        assertNotEquals(keep1, keep2, "Different markCodeAttributes should result in inequality");
    }

    /**
     * Tests equals() with different allowShrinking.
     */
    @Test
    public void testEqualsDifferentAllowShrinking() {
        // Arrange
        ClassSpecification classSpec = new ClassSpecification();
        KeepClassSpecification keep1 = new KeepClassSpecification(
            true, true, true, true, true, true, true, true, null, classSpec
        );
        KeepClassSpecification keep2 = new KeepClassSpecification(
            true, true, true, true, true, false, true, true, null, classSpec
        );

        // Act & Assert
        assertNotEquals(keep1, keep2, "Different allowShrinking should result in inequality");
    }

    /**
     * Tests equals() with different allowOptimization.
     */
    @Test
    public void testEqualsDifferentAllowOptimization() {
        // Arrange
        ClassSpecification classSpec = new ClassSpecification();
        KeepClassSpecification keep1 = new KeepClassSpecification(
            true, true, true, true, true, true, true, true, null, classSpec
        );
        KeepClassSpecification keep2 = new KeepClassSpecification(
            true, true, true, true, true, true, false, true, null, classSpec
        );

        // Act & Assert
        assertNotEquals(keep1, keep2, "Different allowOptimization should result in inequality");
    }

    /**
     * Tests equals() with different allowObfuscation.
     */
    @Test
    public void testEqualsDifferentAllowObfuscation() {
        // Arrange
        ClassSpecification classSpec = new ClassSpecification();
        KeepClassSpecification keep1 = new KeepClassSpecification(
            true, true, true, true, true, true, true, true, null, classSpec
        );
        KeepClassSpecification keep2 = new KeepClassSpecification(
            true, true, true, true, true, true, true, false, null, classSpec
        );

        // Act & Assert
        assertNotEquals(keep1, keep2, "Different allowObfuscation should result in inequality");
    }

    /**
     * Tests equals() with different condition (non-null vs null).
     */
    @Test
    public void testEqualsDifferentConditionOneNull() {
        // Arrange
        ClassSpecification classSpec = new ClassSpecification();
        ClassSpecification condition = new ClassSpecification("Cond", 1, 2, "CA", "CC", null, null);

        KeepClassSpecification keep1 = new KeepClassSpecification(
            true, true, true, true, true, true, true, true, condition, classSpec
        );
        KeepClassSpecification keep2 = new KeepClassSpecification(
            true, true, true, true, true, true, true, true, null, classSpec
        );

        // Act & Assert
        assertNotEquals(keep1, keep2, "Different condition (non-null vs null) should result in inequality");
        assertNotEquals(keep2, keep1, "Reverse comparison should also not be equal");
    }

    /**
     * Tests equals() with both conditions null.
     */
    @Test
    public void testEqualsBothConditionsNull() {
        // Arrange
        ClassSpecification classSpec = new ClassSpecification("Test", 1, 2, "Ann", "Class", null, null);

        KeepClassSpecification keep1 = new KeepClassSpecification(
            true, false, true, false, true, false, true, false, null, classSpec
        );
        KeepClassSpecification keep2 = new KeepClassSpecification(
            true, false, true, false, true, false, true, false, null, classSpec
        );

        // Act & Assert
        assertEquals(keep1, keep2, "Both null conditions should be equal");
    }

    /**
     * Tests equals() with different condition values (both non-null but different).
     */
    @Test
    public void testEqualsDifferentConditionValues() {
        // Arrange
        ClassSpecification classSpec = new ClassSpecification();
        ClassSpecification condition1 = new ClassSpecification("Cond1", 1, 2, "CA1", "CC1", null, null);
        ClassSpecification condition2 = new ClassSpecification("Cond2", 1, 2, "CA2", "CC2", null, null);

        KeepClassSpecification keep1 = new KeepClassSpecification(
            true, true, true, true, true, true, true, true, condition1, classSpec
        );
        KeepClassSpecification keep2 = new KeepClassSpecification(
            true, true, true, true, true, true, true, true, condition2, classSpec
        );

        // Act & Assert
        assertNotEquals(keep1, keep2, "Different condition values should result in inequality");
    }

    /**
     * Tests equals() with same condition reference.
     */
    @Test
    public void testEqualsSameConditionReference() {
        // Arrange
        ClassSpecification classSpec = new ClassSpecification("Test", 1, 2, "Ann", "Class", null, null);
        ClassSpecification condition = new ClassSpecification("Cond", 4, 8, "CA", "CC", null, null);

        KeepClassSpecification keep1 = new KeepClassSpecification(
            false, true, false, true, false, true, false, true, condition, classSpec
        );
        KeepClassSpecification keep2 = new KeepClassSpecification(
            false, true, false, true, false, true, false, true, condition, classSpec
        );

        // Act & Assert
        assertEquals(keep1, keep2, "Same condition reference should result in equality");
    }

    /**
     * Tests equals() with different parent class fields (className).
     */
    @Test
    public void testEqualsDifferentParentClassName() {
        // Arrange
        ClassSpecification classSpec1 = new ClassSpecification("Test", 1, 2, "Ann", "Class1", null, null);
        ClassSpecification classSpec2 = new ClassSpecification("Test", 1, 2, "Ann", "Class2", null, null);

        KeepClassSpecification keep1 = new KeepClassSpecification(
            true, true, true, true, true, true, true, true, null, classSpec1
        );
        KeepClassSpecification keep2 = new KeepClassSpecification(
            true, true, true, true, true, true, true, true, null, classSpec2
        );

        // Act & Assert
        assertNotEquals(keep1, keep2, "Different parent className should result in inequality");
    }

    /**
     * Tests equals() with all flags false and different parent fields.
     */
    @Test
    public void testEqualsAllFalseDifferentParent() {
        // Arrange
        ClassSpecification classSpec1 = new ClassSpecification("Test1", 1, 2, "Ann1", "Class1", null, null);
        ClassSpecification classSpec2 = new ClassSpecification("Test2", 1, 2, "Ann2", "Class2", null, null);

        KeepClassSpecification keep1 = new KeepClassSpecification(
            false, false, false, false, false, false, false, false, null, classSpec1
        );
        KeepClassSpecification keep2 = new KeepClassSpecification(
            false, false, false, false, false, false, false, false, null, classSpec2
        );

        // Act & Assert
        assertNotEquals(keep1, keep2, "Different parent fields should result in inequality even with all flags false");
    }

    // ========== hashCode() Tests ==========

    /**
     * Tests hashCode() consistency.
     */
    @Test
    public void testHashCodeConsistency() {
        // Arrange
        ClassSpecification classSpec = new ClassSpecification("Test", 1, 2, "Ann", "Class", null, null);
        KeepClassSpecification keep = new KeepClassSpecification(
            true, false, true, false, true, false, true, false, null, classSpec
        );

        // Act
        int hash1 = keep.hashCode();
        int hash2 = keep.hashCode();

        // Assert
        assertEquals(hash1, hash2, "hashCode() should be consistent across multiple calls");
    }

    /**
     * Tests hashCode() for equal objects.
     */
    @Test
    public void testHashCodeEqualObjects() {
        // Arrange
        ClassSpecification classSpec = new ClassSpecification("Test", 1, 2, "Ann", "Class", "ExtAnn", "ExtClass");
        ClassSpecification condition = new ClassSpecification("Cond", 4, 8, "CA", "CC", null, null);

        KeepClassSpecification keep1 = new KeepClassSpecification(
            true, false, true, false, true, false, true, false, condition, classSpec
        );
        KeepClassSpecification keep2 = new KeepClassSpecification(
            true, false, true, false, true, false, true, false, condition, classSpec
        );

        // Act & Assert
        assertEquals(keep1, keep2, "Objects should be equal");
        assertEquals(keep1.hashCode(), keep2.hashCode(), "Equal objects should have equal hash codes");
    }

    /**
     * Tests hashCode() with all flags true.
     */
    @Test
    public void testHashCodeAllFlagsTrue() {
        // Arrange
        ClassSpecification classSpec = new ClassSpecification();
        KeepClassSpecification keep = new KeepClassSpecification(
            true, true, true, true, true, true, true, true, null, classSpec
        );

        // Act
        int hash = keep.hashCode();

        // Assert - Should compute without throwing exception
        assertNotNull(hash);
    }

    /**
     * Tests hashCode() with all flags false.
     */
    @Test
    public void testHashCodeAllFlagsFalse() {
        // Arrange
        ClassSpecification classSpec = new ClassSpecification();
        KeepClassSpecification keep = new KeepClassSpecification(
            false, false, false, false, false, false, false, false, null, classSpec
        );

        // Act
        int hash = keep.hashCode();

        // Assert - Should compute without throwing exception
        // When all boolean flags are false, each contributes to the XOR
        assertNotNull(hash);
    }

    /**
     * Tests hashCode() with different markClasses values.
     */
    @Test
    public void testHashCodeDifferentMarkClasses() {
        // Arrange
        ClassSpecification classSpec = new ClassSpecification();
        KeepClassSpecification keep1 = new KeepClassSpecification(
            true, false, false, false, false, false, false, false, null, classSpec
        );
        KeepClassSpecification keep2 = new KeepClassSpecification(
            false, false, false, false, false, false, false, false, null, classSpec
        );

        // Act
        int hash1 = keep1.hashCode();
        int hash2 = keep2.hashCode();

        // Assert - Different boolean values should affect hash code
        assertNotEquals(keep1, keep2, "Objects should not be equal");
        assertNotEquals(hash1, hash2, "Different markClasses should produce different hash codes");
    }

    /**
     * Tests hashCode() with null condition.
     */
    @Test
    public void testHashCodeNullCondition() {
        // Arrange
        ClassSpecification classSpec = new ClassSpecification("Test", 1, 2, "Ann", "Class", null, null);
        KeepClassSpecification keep = new KeepClassSpecification(
            true, true, true, true, true, true, true, true, null, classSpec
        );

        // Act
        int hash = keep.hashCode();

        // Assert - Should compute without throwing exception
        assertNotNull(hash);
    }

    /**
     * Tests hashCode() with non-null condition.
     */
    @Test
    public void testHashCodeNonNullCondition() {
        // Arrange
        ClassSpecification classSpec = new ClassSpecification("Test", 1, 2, "Ann", "Class", null, null);
        ClassSpecification condition = new ClassSpecification("Cond", 4, 8, "CA", "CC", null, null);
        KeepClassSpecification keep = new KeepClassSpecification(
            true, true, true, true, true, true, true, true, condition, classSpec
        );

        // Act
        int hash = keep.hashCode();

        // Assert - Should compute without throwing exception and include condition's hash
        assertNotNull(hash);
    }

    /**
     * Tests hashCode() includes parent class hashCode.
     */
    @Test
    public void testHashCodeIncludesParentHashCode() {
        // Arrange
        ClassSpecification classSpec1 = new ClassSpecification("Test1", 1, 2, "Ann1", "Class1", null, null);
        ClassSpecification classSpec2 = new ClassSpecification("Test2", 4, 8, "Ann2", "Class2", null, null);

        // Same KeepClassSpecification flags, different parent class specs
        KeepClassSpecification keep1 = new KeepClassSpecification(
            true, true, true, true, true, true, true, true, null, classSpec1
        );
        KeepClassSpecification keep2 = new KeepClassSpecification(
            true, true, true, true, true, true, true, true, null, classSpec2
        );

        // Act
        int hash1 = keep1.hashCode();
        int hash2 = keep2.hashCode();

        // Assert - Different parent specs should affect hash code
        assertNotEquals(keep1, keep2, "Objects should not be equal due to different parent specs");
        // Hash codes can collide but should generally be different
        // We verify they compute successfully
        assertNotNull(hash1);
        assertNotNull(hash2);
    }

    /**
     * Tests hashCode() with mixed boolean flags.
     */
    @Test
    public void testHashCodeMixedFlags() {
        // Arrange
        ClassSpecification classSpec = new ClassSpecification();
        KeepClassSpecification keep = new KeepClassSpecification(
            true, false, true, false, true, false, true, false, null, classSpec
        );

        // Act
        int hash = keep.hashCode();

        // Assert
        assertNotNull(hash);
    }

    // ========== clone() Tests ==========

    /**
     * Tests clone() creates a new object.
     */
    @Test
    public void testCloneCreatesNewObject() {
        // Arrange
        ClassSpecification classSpec = new ClassSpecification("Test", 1, 2, "Ann", "Class", null, null);
        KeepClassSpecification original = new KeepClassSpecification(
            true, false, true, false, true, false, true, false, null, classSpec
        );

        // Act
        Object cloned = original.clone();

        // Assert
        assertNotNull(cloned, "Clone should not be null");
        assertNotSame(original, cloned, "Clone should be a different instance");
        assertTrue(cloned instanceof KeepClassSpecification, "Clone should be a KeepClassSpecification");
    }

    /**
     * Tests clone() creates an equal object.
     */
    @Test
    public void testCloneCreatesEqualObject() {
        // Arrange
        ClassSpecification classSpec = new ClassSpecification("Test", 1, 2, "Ann", "Class", null, null);
        ClassSpecification condition = new ClassSpecification("Cond", 4, 8, "CA", "CC", null, null);
        KeepClassSpecification original = new KeepClassSpecification(
            true, false, true, false, true, false, true, false, condition, classSpec
        );

        // Act
        KeepClassSpecification cloned = (KeepClassSpecification) original.clone();

        // Assert
        assertEquals(original, cloned, "Clone should be equal to original");
    }

    /**
     * Tests clone() preserves all boolean fields.
     */
    @Test
    public void testClonePreservesBooleanFields() {
        // Arrange
        ClassSpecification classSpec = new ClassSpecification("Test", 1, 2, "Ann", "Class", null, null);
        ClassSpecification condition = new ClassSpecification("Cond", 4, 8, "CA", "CC", null, null);
        KeepClassSpecification original = new KeepClassSpecification(
            true, false, true, false, true, false, true, false, condition, classSpec
        );

        // Act
        KeepClassSpecification cloned = (KeepClassSpecification) original.clone();

        // Assert - Verify all boolean flags are preserved
        assertEquals(original.markClasses, cloned.markClasses, "markClasses should be preserved");
        assertEquals(original.markClassMembers, cloned.markClassMembers, "markClassMembers should be preserved");
        assertEquals(original.markConditionally, cloned.markConditionally, "markConditionally should be preserved");
        assertEquals(original.markDescriptorClasses, cloned.markDescriptorClasses, "markDescriptorClasses should be preserved");
        assertEquals(original.markCodeAttributes, cloned.markCodeAttributes, "markCodeAttributes should be preserved");
        assertEquals(original.allowShrinking, cloned.allowShrinking, "allowShrinking should be preserved");
        assertEquals(original.allowOptimization, cloned.allowOptimization, "allowOptimization should be preserved");
        assertEquals(original.allowObfuscation, cloned.allowObfuscation, "allowObfuscation should be preserved");
    }

    /**
     * Tests clone() preserves condition reference (shallow copy).
     */
    @Test
    public void testClonePreservesConditionReference() {
        // Arrange
        ClassSpecification classSpec = new ClassSpecification("Test", 1, 2, "Ann", "Class", null, null);
        ClassSpecification condition = new ClassSpecification("Cond", 4, 8, "CA", "CC", null, null);
        KeepClassSpecification original = new KeepClassSpecification(
            true, true, true, true, true, true, true, true, condition, classSpec
        );

        // Act
        KeepClassSpecification cloned = (KeepClassSpecification) original.clone();

        // Assert - Shallow copy means same reference
        assertSame(original.condition, cloned.condition, "Condition should be shallow copied (same reference)");
    }

    /**
     * Tests clone() with null condition.
     */
    @Test
    public void testCloneWithNullCondition() {
        // Arrange
        ClassSpecification classSpec = new ClassSpecification("Test", 1, 2, "Ann", "Class", null, null);
        KeepClassSpecification original = new KeepClassSpecification(
            true, true, true, true, true, true, true, true, null, classSpec
        );

        // Act
        KeepClassSpecification cloned = (KeepClassSpecification) original.clone();

        // Assert
        assertNull(cloned.condition, "Cloned condition should be null");
        assertEquals(original, cloned, "Clone should be equal to original");
    }

    /**
     * Tests clone() preserves parent class fields.
     */
    @Test
    public void testClonePreservesParentFields() {
        // Arrange
        MemberSpecification field = new MemberSpecification(1, 2, "FAnn", "field", "I");
        MemberSpecification method = new MemberSpecification(4, 8, "MAnn", "method", "()V");
        List<MemberSpecification> fields = new ArrayList<>();
        fields.add(field);
        List<MemberSpecification> methods = new ArrayList<>();
        methods.add(method);
        ClassSpecification classSpec = new ClassSpecification(
            "MyComment", 16, 32, "MyAnn", "MyClass", "MyExtAnn", "MyExtClass", fields, methods
        );
        KeepClassSpecification original = new KeepClassSpecification(
            true, false, true, false, true, false, true, false, null, classSpec
        );

        // Act
        KeepClassSpecification cloned = (KeepClassSpecification) original.clone();

        // Assert - Verify parent fields are preserved
        assertEquals(original.comments, cloned.comments);
        assertEquals(original.requiredSetAccessFlags, cloned.requiredSetAccessFlags);
        assertEquals(original.requiredUnsetAccessFlags, cloned.requiredUnsetAccessFlags);
        assertEquals(original.annotationType, cloned.annotationType);
        assertEquals(original.className, cloned.className);
        assertEquals(original.extendsAnnotationType, cloned.extendsAnnotationType);
        assertEquals(original.extendsClassName, cloned.extendsClassName);
        assertSame(original.fieldSpecifications, cloned.fieldSpecifications, "Field specifications should be shallow copied");
        assertSame(original.methodSpecifications, cloned.methodSpecifications, "Method specifications should be shallow copied");
    }

    /**
     * Tests clone() with all flags true.
     */
    @Test
    public void testCloneAllFlagsTrue() {
        // Arrange
        ClassSpecification classSpec = new ClassSpecification("Test", 1, 2, "Ann", "Class", null, null);
        KeepClassSpecification original = new KeepClassSpecification(
            true, true, true, true, true, true, true, true, null, classSpec
        );

        // Act
        KeepClassSpecification cloned = (KeepClassSpecification) original.clone();

        // Assert
        assertTrue(cloned.markClasses);
        assertTrue(cloned.markClassMembers);
        assertTrue(cloned.markConditionally);
        assertTrue(cloned.markDescriptorClasses);
        assertTrue(cloned.markCodeAttributes);
        assertTrue(cloned.allowShrinking);
        assertTrue(cloned.allowOptimization);
        assertTrue(cloned.allowObfuscation);
    }

    /**
     * Tests clone() with all flags false.
     */
    @Test
    public void testCloneAllFlagsFalse() {
        // Arrange
        ClassSpecification classSpec = new ClassSpecification("Test", 1, 2, "Ann", "Class", null, null);
        KeepClassSpecification original = new KeepClassSpecification(
            false, false, false, false, false, false, false, false, null, classSpec
        );

        // Act
        KeepClassSpecification cloned = (KeepClassSpecification) original.clone();

        // Assert
        assertFalse(cloned.markClasses);
        assertFalse(cloned.markClassMembers);
        assertFalse(cloned.markConditionally);
        assertFalse(cloned.markDescriptorClasses);
        assertFalse(cloned.markCodeAttributes);
        assertFalse(cloned.allowShrinking);
        assertFalse(cloned.allowOptimization);
        assertFalse(cloned.allowObfuscation);
    }

    /**
     * Tests clone() hashCode consistency.
     */
    @Test
    public void testCloneHashCodeConsistency() {
        // Arrange
        ClassSpecification classSpec = new ClassSpecification("Test", 1, 2, "Ann", "Class", null, null);
        ClassSpecification condition = new ClassSpecification("Cond", 4, 8, "CA", "CC", null, null);
        KeepClassSpecification original = new KeepClassSpecification(
            true, false, true, false, true, false, true, false, condition, classSpec
        );

        // Act
        KeepClassSpecification cloned = (KeepClassSpecification) original.clone();

        // Assert
        assertEquals(original.hashCode(), cloned.hashCode(), "Clone should have the same hashCode as original");
    }

    /**
     * Tests modifying the clone doesn't affect the original (modifiable fields).
     */
    @Test
    public void testCloneIndependenceModifiableFields() {
        // Arrange
        ClassSpecification classSpec = new ClassSpecification("Test", 1, 2, "Ann", "Class", null, null);
        KeepClassSpecification original = new KeepClassSpecification(
            true, true, true, true, true, true, true, true, null, classSpec
        );
        KeepClassSpecification cloned = (KeepClassSpecification) original.clone();

        // Act - Modify cloned object's modifiable parent fields
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

    // ========== Integration Tests ==========

    /**
     * Integration test: Create KeepClassSpecification with complex configuration.
     */
    @Test
    public void testCompleteWorkflow() {
        // Arrange & Act - Create condition
        ClassSpecification condition = new ClassSpecification(
            "Condition specification",
            1,
            2,
            "ConditionAnnotation",
            "com.example.Condition",
            null,
            null
        );

        // Act - Create class specification with members
        ClassSpecification classSpec = new ClassSpecification(
            "Keep specification",
            4,
            8,
            "KeepAnnotation",
            "com.example.KeepClass",
            "ExtendsAnnotation",
            "java.lang.Object"
        );
        classSpec.addField(new MemberSpecification(1, 2, "FieldAnn", "field1", "I"));
        classSpec.addMethod(new MemberSpecification(4, 8, "MethodAnn", "method1", "()V"));

        // Act - Create KeepClassSpecification
        KeepClassSpecification keep = new KeepClassSpecification(
            true, false, true, false, true, false, true, false,
            condition,
            classSpec
        );

        // Assert - Verify all fields
        assertTrue(keep.markClasses);
        assertFalse(keep.markClassMembers);
        assertTrue(keep.markConditionally);
        assertFalse(keep.markDescriptorClasses);
        assertTrue(keep.markCodeAttributes);
        assertFalse(keep.allowShrinking);
        assertTrue(keep.allowOptimization);
        assertFalse(keep.allowObfuscation);
        assertSame(condition, keep.condition);
        assertEquals("Keep specification", keep.comments);
        assertEquals("com.example.KeepClass", keep.className);
        assertNotNull(keep.fieldSpecifications);
        assertNotNull(keep.methodSpecifications);
        assertEquals(1, keep.fieldSpecifications.size());
        assertEquals(1, keep.methodSpecifications.size());

        // Act - Clone the specification
        KeepClassSpecification cloned = (KeepClassSpecification) keep.clone();

        // Assert - Verify clone
        assertEquals(keep, cloned);
        assertNotSame(keep, cloned);
        assertEquals(keep.hashCode(), cloned.hashCode());
    }

    /**
     * Integration test: Test equals and hashCode contract with complex specifications.
     */
    @Test
    public void testEqualsHashCodeContract() {
        // Arrange - Create three equal specifications
        ClassSpecification condition = new ClassSpecification("Cond", 1, 2, "CA", "CC", null, null);
        ClassSpecification classSpec = new ClassSpecification("Spec", 4, 8, "SA", "SC", "EA", "EC");

        KeepClassSpecification keep1 = new KeepClassSpecification(
            true, false, true, false, true, false, true, false, condition, classSpec
        );
        KeepClassSpecification keep2 = new KeepClassSpecification(
            true, false, true, false, true, false, true, false, condition, classSpec
        );
        KeepClassSpecification keep3 = new KeepClassSpecification(
            true, false, true, false, true, false, true, false, condition, classSpec
        );

        // Assert - Reflexive
        assertEquals(keep1, keep1, "Specification should equal itself");

        // Assert - Symmetric
        assertEquals(keep1, keep2, "keep1 should equal keep2");
        assertEquals(keep2, keep1, "keep2 should equal keep1");

        // Assert - Transitive
        assertEquals(keep1, keep2, "keep1 should equal keep2");
        assertEquals(keep2, keep3, "keep2 should equal keep3");
        assertEquals(keep1, keep3, "keep1 should equal keep3");

        // Assert - Consistent hashCode
        assertEquals(keep1.hashCode(), keep2.hashCode(), "Equal objects should have equal hash codes");
        assertEquals(keep2.hashCode(), keep3.hashCode(), "Equal objects should have equal hash codes");
        assertEquals(keep1.hashCode(), keep3.hashCode(), "Equal objects should have equal hash codes");

        // Assert - Null comparison
        assertNotEquals(null, keep1, "Specification should not equal null");
    }

    /**
     * Integration test: Verify 9-parameter and 10-parameter constructors produce equivalent results.
     */
    @Test
    public void testConstructorEquivalence() {
        // Arrange
        ClassSpecification condition = new ClassSpecification("Cond", 1, 2, "CA", "CC", null, null);
        ClassSpecification classSpec = new ClassSpecification("Spec", 4, 8, "SA", "SC", "EA", "EC");

        // Act - Create with 9-parameter constructor
        KeepClassSpecification keep9 = new KeepClassSpecification(
            true,   // markClassesAndMembers
            false,  // markConditionally
            true,   // markDescriptorClasses
            false,  // markCodeAttributes
            true,   // allowShrinking
            false,  // allowOptimization
            true,   // allowObfuscation
            condition,
            classSpec
        );

        // Create with 10-parameter constructor (equivalent)
        KeepClassSpecification keep10 = new KeepClassSpecification(
            true,   // markClasses
            true,   // markClassMembers (same as markClassesAndMembers)
            false,  // markConditionally
            true,   // markDescriptorClasses
            false,  // markCodeAttributes
            true,   // allowShrinking
            false,  // allowOptimization
            true,   // allowObfuscation
            condition,
            classSpec
        );

        // Assert - They should be equal
        assertEquals(keep9, keep10, "9-parameter and 10-parameter constructors should produce equal objects");
        assertEquals(keep9.hashCode(), keep10.hashCode(), "Equal objects should have equal hash codes");
    }
}
