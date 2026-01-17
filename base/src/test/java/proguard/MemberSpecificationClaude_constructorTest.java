package proguard;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link MemberSpecification} constructors.
 * Tests MemberSpecification() and MemberSpecification(int, int, String, String, String) constructors.
 */
public class MemberSpecificationClaude_constructorTest {

    /**
     * Tests the default constructor MemberSpecification().
     * Verifies that all fields are initialized to their expected default values.
     */
    @Test
    public void testDefaultConstructor() {
        // Act - Create MemberSpecification with default constructor
        MemberSpecification memberSpec = new MemberSpecification();

        // Assert - Verify all fields are initialized correctly
        assertNotNull(memberSpec, "MemberSpecification should be instantiated successfully");
        assertEquals(0, memberSpec.requiredSetAccessFlags,
                     "requiredSetAccessFlags should be initialized to 0");
        assertEquals(0, memberSpec.requiredUnsetAccessFlags,
                     "requiredUnsetAccessFlags should be initialized to 0");
        assertNull(memberSpec.annotationType,
                   "annotationType should be initialized to null");
        assertNull(memberSpec.name,
                   "name should be initialized to null");
        assertNull(memberSpec.descriptor,
                   "descriptor should be initialized to null");
        assertNull(memberSpec.attributeNames,
                   "attributeNames should be null");
    }

    /**
     * Tests that multiple MemberSpecification instances created with default constructor
     * are independent objects.
     */
    @Test
    public void testMultipleDefaultConstructorInstances() {
        // Act - Create two MemberSpecification instances
        MemberSpecification memberSpec1 = new MemberSpecification();
        MemberSpecification memberSpec2 = new MemberSpecification();

        // Assert - Verify they are different objects
        assertNotNull(memberSpec1, "First MemberSpecification should be created");
        assertNotNull(memberSpec2, "Second MemberSpecification should be created");
        assertNotSame(memberSpec1, memberSpec2,
                      "MemberSpecification instances should be different objects");
    }

    /**
     * Tests that MemberSpecification created with default constructor
     * has the same field values as one created with explicit zero/null parameters.
     */
    @Test
    public void testDefaultConstructorEquivalentToExplicitParameters() {
        // Act - Create one with default constructor and one with explicit parameters
        MemberSpecification defaultSpec = new MemberSpecification();
        MemberSpecification explicitSpec = new MemberSpecification(0, 0, null, null, null);

        // Assert - Verify they are equivalent
        assertEquals(explicitSpec.requiredSetAccessFlags, defaultSpec.requiredSetAccessFlags,
                     "requiredSetAccessFlags should match");
        assertEquals(explicitSpec.requiredUnsetAccessFlags, defaultSpec.requiredUnsetAccessFlags,
                     "requiredUnsetAccessFlags should match");
        assertEquals(explicitSpec.annotationType, defaultSpec.annotationType,
                     "annotationType should match");
        assertEquals(explicitSpec.name, defaultSpec.name,
                     "name should match");
        assertEquals(explicitSpec.descriptor, defaultSpec.descriptor,
                     "descriptor should match");
    }

    /**
     * Tests that MemberSpecification created with default constructor
     * equals another one created with default constructor.
     */
    @Test
    public void testDefaultConstructorEqualsAnotherDefault() {
        // Act - Create two MemberSpecification instances with default constructor
        MemberSpecification memberSpec1 = new MemberSpecification();
        MemberSpecification memberSpec2 = new MemberSpecification();

        // Assert - Verify they are equal (same field values)
        assertEquals(memberSpec1, memberSpec2,
                     "Two default MemberSpecifications should be equal");
        assertEquals(memberSpec1.hashCode(), memberSpec2.hashCode(),
                     "Two default MemberSpecifications should have same hash code");
    }

    /**
     * Tests that requiredSetAccessFlags can be modified after construction.
     */
    @Test
    public void testDefaultConstructorAllowsModifyingRequiredSetAccessFlags() {
        // Arrange
        MemberSpecification memberSpec = new MemberSpecification();

        // Act - Modify requiredSetAccessFlags
        memberSpec.requiredSetAccessFlags = 1;

        // Assert - Verify the field was modified
        assertEquals(1, memberSpec.requiredSetAccessFlags,
                     "requiredSetAccessFlags should be modifiable");
    }

    /**
     * Tests that requiredUnsetAccessFlags can be modified after construction.
     */
    @Test
    public void testDefaultConstructorAllowsModifyingRequiredUnsetAccessFlags() {
        // Arrange
        MemberSpecification memberSpec = new MemberSpecification();

        // Act - Modify requiredUnsetAccessFlags
        memberSpec.requiredUnsetAccessFlags = 2;

        // Assert - Verify the field was modified
        assertEquals(2, memberSpec.requiredUnsetAccessFlags,
                     "requiredUnsetAccessFlags should be modifiable");
    }

    /**
     * Tests that a MemberSpecification created with default constructor
     * can be used in equality comparisons with non-default MemberSpecifications.
     */
    @Test
    public void testDefaultConstructorNotEqualToNonDefault() {
        // Act - Create one default and one with parameters
        MemberSpecification defaultSpec = new MemberSpecification();
        MemberSpecification nonDefaultSpec = new MemberSpecification(1, 0, null, null, null);

        // Assert - Verify they are not equal
        assertNotEquals(defaultSpec, nonDefaultSpec,
                        "Default MemberSpecification should not equal one with different flags");
    }

    /**
     * Tests that a MemberSpecification created with default constructor
     * produces a consistent hash code.
     */
    @Test
    public void testDefaultConstructorHashCodeConsistency() {
        // Act - Create a MemberSpecification and get hash code twice
        MemberSpecification memberSpec = new MemberSpecification();
        int hashCode1 = memberSpec.hashCode();
        int hashCode2 = memberSpec.hashCode();

        // Assert - Verify hash code is consistent
        assertEquals(hashCode1, hashCode2,
                     "Hash code should be consistent across multiple calls");
    }

    /**
     * Tests that a MemberSpecification created with default constructor
     * is not equal to null.
     */
    @Test
    public void testDefaultConstructorNotEqualToNull() {
        // Act - Create a MemberSpecification
        MemberSpecification memberSpec = new MemberSpecification();

        // Assert - Verify it's not equal to null
        assertNotEquals(memberSpec, null,
                        "MemberSpecification should not equal null");
    }

    /**
     * Tests that a MemberSpecification created with default constructor
     * is equal to itself.
     */
    @Test
    public void testDefaultConstructorEqualToItself() {
        // Act - Create a MemberSpecification
        MemberSpecification memberSpec = new MemberSpecification();

        // Assert - Verify it's equal to itself
        assertEquals(memberSpec, memberSpec,
                     "MemberSpecification should equal itself");
    }

    /**
     * Tests that a MemberSpecification created with default constructor
     * can be used as a template for all class members (as per the comment in the code).
     */
    @Test
    public void testDefaultConstructorCreatesTemplateForAllMembers() {
        // Act - Create a MemberSpecification with default constructor
        MemberSpecification memberSpec = new MemberSpecification();

        // Assert - Verify it represents "all possible class members" (no restrictions)
        // No access flags are required to be set
        assertEquals(0, memberSpec.requiredSetAccessFlags,
                     "No access flags required to be set for matching all members");
        // No access flags are required to be unset
        assertEquals(0, memberSpec.requiredUnsetAccessFlags,
                     "No access flags required to be unset for matching all members");
        // No annotation type restriction
        assertNull(memberSpec.annotationType,
                   "No annotation restriction for matching all members");
        // No name restriction (any name matches)
        assertNull(memberSpec.name,
                   "No name restriction for matching all members");
        // No descriptor restriction (any descriptor matches)
        assertNull(memberSpec.descriptor,
                   "No descriptor restriction for matching all members");
    }

    // Tests for parameterized constructor MemberSpecification(int, int, String, String, String)

    /**
     * Tests the parameterized constructor with all null string parameters.
     */
    @Test
    public void testParameterizedConstructorWithAllNulls() {
        // Act
        MemberSpecification memberSpec = new MemberSpecification(0, 0, null, null, null);

        // Assert
        assertEquals(0, memberSpec.requiredSetAccessFlags);
        assertEquals(0, memberSpec.requiredUnsetAccessFlags);
        assertNull(memberSpec.annotationType);
        assertNull(memberSpec.name);
        assertNull(memberSpec.descriptor);
    }

    /**
     * Tests the parameterized constructor with non-zero access flags.
     */
    @Test
    public void testParameterizedConstructorWithAccessFlags() {
        // Act
        MemberSpecification memberSpec = new MemberSpecification(1, 2, null, null, null);

        // Assert
        assertEquals(1, memberSpec.requiredSetAccessFlags);
        assertEquals(2, memberSpec.requiredUnsetAccessFlags);
        assertNull(memberSpec.annotationType);
        assertNull(memberSpec.name);
        assertNull(memberSpec.descriptor);
    }

    /**
     * Tests the parameterized constructor with annotation type.
     */
    @Test
    public void testParameterizedConstructorWithAnnotationType() {
        // Act
        MemberSpecification memberSpec = new MemberSpecification(0, 0, "Lcom/example/Annotation;", null, null);

        // Assert
        assertEquals(0, memberSpec.requiredSetAccessFlags);
        assertEquals(0, memberSpec.requiredUnsetAccessFlags);
        assertEquals("Lcom/example/Annotation;", memberSpec.annotationType);
        assertNull(memberSpec.name);
        assertNull(memberSpec.descriptor);
    }

    /**
     * Tests the parameterized constructor with name.
     */
    @Test
    public void testParameterizedConstructorWithName() {
        // Act
        MemberSpecification memberSpec = new MemberSpecification(0, 0, null, "myMethod", null);

        // Assert
        assertEquals(0, memberSpec.requiredSetAccessFlags);
        assertEquals(0, memberSpec.requiredUnsetAccessFlags);
        assertNull(memberSpec.annotationType);
        assertEquals("myMethod", memberSpec.name);
        assertNull(memberSpec.descriptor);
    }

    /**
     * Tests the parameterized constructor with descriptor.
     */
    @Test
    public void testParameterizedConstructorWithDescriptor() {
        // Act
        MemberSpecification memberSpec = new MemberSpecification(0, 0, null, null, "()V");

        // Assert
        assertEquals(0, memberSpec.requiredSetAccessFlags);
        assertEquals(0, memberSpec.requiredUnsetAccessFlags);
        assertNull(memberSpec.annotationType);
        assertNull(memberSpec.name);
        assertEquals("()V", memberSpec.descriptor);
    }

    /**
     * Tests the parameterized constructor with all parameters specified.
     */
    @Test
    public void testParameterizedConstructorWithAllParameters() {
        // Act
        MemberSpecification memberSpec = new MemberSpecification(
            4, 8, "Lcom/example/MyAnnotation;", "myField", "Ljava/lang/String;");

        // Assert
        assertEquals(4, memberSpec.requiredSetAccessFlags);
        assertEquals(8, memberSpec.requiredUnsetAccessFlags);
        assertEquals("Lcom/example/MyAnnotation;", memberSpec.annotationType);
        assertEquals("myField", memberSpec.name);
        assertEquals("Ljava/lang/String;", memberSpec.descriptor);
    }

    /**
     * Tests the parameterized constructor with wildcard name.
     */
    @Test
    public void testParameterizedConstructorWithWildcardName() {
        // Act
        MemberSpecification memberSpec = new MemberSpecification(0, 0, null, "get*", null);

        // Assert
        assertEquals("get*", memberSpec.name);
    }

    /**
     * Tests the parameterized constructor with question mark wildcard in name.
     */
    @Test
    public void testParameterizedConstructorWithQuestionMarkInName() {
        // Act
        MemberSpecification memberSpec = new MemberSpecification(0, 0, null, "get?", null);

        // Assert
        assertEquals("get?", memberSpec.name);
    }

    /**
     * Tests the parameterized constructor with wildcards in descriptor.
     */
    @Test
    public void testParameterizedConstructorWithWildcardDescriptor() {
        // Act
        MemberSpecification memberSpec = new MemberSpecification(0, 0, null, null, "(**)*");

        // Assert
        assertEquals("(**)*", memberSpec.descriptor);
    }

    /**
     * Tests the parameterized constructor with empty strings.
     */
    @Test
    public void testParameterizedConstructorWithEmptyStrings() {
        // Act
        MemberSpecification memberSpec = new MemberSpecification(0, 0, "", "", "");

        // Assert
        assertEquals("", memberSpec.annotationType);
        assertEquals("", memberSpec.name);
        assertEquals("", memberSpec.descriptor);
    }

    /**
     * Tests the parameterized constructor with negative access flags.
     */
    @Test
    public void testParameterizedConstructorWithNegativeAccessFlags() {
        // Act
        MemberSpecification memberSpec = new MemberSpecification(-1, -2, null, null, null);

        // Assert
        assertEquals(-1, memberSpec.requiredSetAccessFlags);
        assertEquals(-2, memberSpec.requiredUnsetAccessFlags);
    }

    /**
     * Tests the parameterized constructor with maximum int values for access flags.
     */
    @Test
    public void testParameterizedConstructorWithMaxIntAccessFlags() {
        // Act
        MemberSpecification memberSpec = new MemberSpecification(
            Integer.MAX_VALUE, Integer.MAX_VALUE, null, null, null);

        // Assert
        assertEquals(Integer.MAX_VALUE, memberSpec.requiredSetAccessFlags);
        assertEquals(Integer.MAX_VALUE, memberSpec.requiredUnsetAccessFlags);
    }

    /**
     * Tests that two MemberSpecification instances with same parameters are equal.
     */
    @Test
    public void testParameterizedConstructorEquality() {
        // Act
        MemberSpecification spec1 = new MemberSpecification(1, 2, "Anno", "name", "desc");
        MemberSpecification spec2 = new MemberSpecification(1, 2, "Anno", "name", "desc");

        // Assert
        assertEquals(spec1, spec2);
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }

    /**
     * Tests that two MemberSpecification instances with different requiredSetAccessFlags are not equal.
     */
    @Test
    public void testParameterizedConstructorInequalitySetFlags() {
        // Act
        MemberSpecification spec1 = new MemberSpecification(1, 0, null, null, null);
        MemberSpecification spec2 = new MemberSpecification(2, 0, null, null, null);

        // Assert
        assertNotEquals(spec1, spec2);
    }

    /**
     * Tests that two MemberSpecification instances with different requiredUnsetAccessFlags are not equal.
     */
    @Test
    public void testParameterizedConstructorInequalityUnsetFlags() {
        // Act
        MemberSpecification spec1 = new MemberSpecification(0, 1, null, null, null);
        MemberSpecification spec2 = new MemberSpecification(0, 2, null, null, null);

        // Assert
        assertNotEquals(spec1, spec2);
    }

    /**
     * Tests that two MemberSpecification instances with different annotationType are not equal.
     */
    @Test
    public void testParameterizedConstructorInequalityAnnotationType() {
        // Act
        MemberSpecification spec1 = new MemberSpecification(0, 0, "Anno1", null, null);
        MemberSpecification spec2 = new MemberSpecification(0, 0, "Anno2", null, null);

        // Assert
        assertNotEquals(spec1, spec2);
    }

    /**
     * Tests that two MemberSpecification instances with different name are not equal.
     */
    @Test
    public void testParameterizedConstructorInequalityName() {
        // Act
        MemberSpecification spec1 = new MemberSpecification(0, 0, null, "name1", null);
        MemberSpecification spec2 = new MemberSpecification(0, 0, null, "name2", null);

        // Assert
        assertNotEquals(spec1, spec2);
    }

    /**
     * Tests that two MemberSpecification instances with different descriptor are not equal.
     */
    @Test
    public void testParameterizedConstructorInequalityDescriptor() {
        // Act
        MemberSpecification spec1 = new MemberSpecification(0, 0, null, null, "desc1");
        MemberSpecification spec2 = new MemberSpecification(0, 0, null, null, "desc2");

        // Assert
        assertNotEquals(spec1, spec2);
    }

    /**
     * Tests that a MemberSpecification with null annotationType equals another with null.
     */
    @Test
    public void testParameterizedConstructorEqualityWithNullAnnotationType() {
        // Act
        MemberSpecification spec1 = new MemberSpecification(1, 2, null, "name", "desc");
        MemberSpecification spec2 = new MemberSpecification(1, 2, null, "name", "desc");

        // Assert
        assertEquals(spec1, spec2);
    }

    /**
     * Tests that a MemberSpecification with null name equals another with null.
     */
    @Test
    public void testParameterizedConstructorEqualityWithNullName() {
        // Act
        MemberSpecification spec1 = new MemberSpecification(1, 2, "Anno", null, "desc");
        MemberSpecification spec2 = new MemberSpecification(1, 2, "Anno", null, "desc");

        // Assert
        assertEquals(spec1, spec2);
    }

    /**
     * Tests that a MemberSpecification with null descriptor equals another with null.
     */
    @Test
    public void testParameterizedConstructorEqualityWithNullDescriptor() {
        // Act
        MemberSpecification spec1 = new MemberSpecification(1, 2, "Anno", "name", null);
        MemberSpecification spec2 = new MemberSpecification(1, 2, "Anno", "name", null);

        // Assert
        assertEquals(spec1, spec2);
    }

    /**
     * Tests that MemberSpecification is not equal to null.
     */
    @Test
    public void testParameterizedConstructorNotEqualToNull() {
        // Act
        MemberSpecification spec = new MemberSpecification(1, 2, "Anno", "name", "desc");

        // Assert
        assertNotEquals(spec, null);
    }

    /**
     * Tests that MemberSpecification is equal to itself.
     */
    @Test
    public void testParameterizedConstructorEqualToItself() {
        // Act
        MemberSpecification spec = new MemberSpecification(1, 2, "Anno", "name", "desc");

        // Assert
        assertEquals(spec, spec);
    }

    /**
     * Tests that MemberSpecification is not equal to an object of a different class.
     */
    @Test
    public void testParameterizedConstructorNotEqualToDifferentClass() {
        // Act
        MemberSpecification spec = new MemberSpecification(1, 2, "Anno", "name", "desc");
        String differentClass = "string";

        // Assert
        assertNotEquals(spec, differentClass);
    }

    /**
     * Tests that hash code is consistent across multiple calls.
     */
    @Test
    public void testParameterizedConstructorHashCodeConsistency() {
        // Act
        MemberSpecification spec = new MemberSpecification(1, 2, "Anno", "name", "desc");
        int hash1 = spec.hashCode();
        int hash2 = spec.hashCode();

        // Assert
        assertEquals(hash1, hash2);
    }

    /**
     * Tests that fields are final where appropriate (annotationType, name, descriptor).
     */
    @Test
    public void testParameterizedConstructorFinalFields() {
        // Act
        MemberSpecification spec = new MemberSpecification(1, 2, "Anno", "name", "desc");

        // Assert - These fields should not be modifiable (they are final)
        // We can only verify they are set correctly
        assertEquals("Anno", spec.annotationType);
        assertEquals("name", spec.name);
        assertEquals("desc", spec.descriptor);
    }

    /**
     * Tests that non-final fields can be modified after construction.
     */
    @Test
    public void testParameterizedConstructorNonFinalFieldsModifiable() {
        // Act
        MemberSpecification spec = new MemberSpecification(1, 2, "Anno", "name", "desc");
        spec.requiredSetAccessFlags = 10;
        spec.requiredUnsetAccessFlags = 20;

        // Assert
        assertEquals(10, spec.requiredSetAccessFlags);
        assertEquals(20, spec.requiredUnsetAccessFlags);
    }

    /**
     * Tests the parameterized constructor with typical method descriptor.
     */
    @Test
    public void testParameterizedConstructorWithMethodDescriptor() {
        // Act
        MemberSpecification spec = new MemberSpecification(
            0, 0, null, "toString", "()Ljava/lang/String;");

        // Assert
        assertEquals("toString", spec.name);
        assertEquals("()Ljava/lang/String;", spec.descriptor);
    }

    /**
     * Tests the parameterized constructor with typical field descriptor.
     */
    @Test
    public void testParameterizedConstructorWithFieldDescriptor() {
        // Act
        MemberSpecification spec = new MemberSpecification(
            0, 0, null, "value", "I");

        // Assert
        assertEquals("value", spec.name);
        assertEquals("I", spec.descriptor);
    }

    /**
     * Tests attributeNames is always null regardless of constructor parameters.
     */
    @Test
    public void testParameterizedConstructorAttributeNamesAlwaysNull() {
        // Act
        MemberSpecification spec = new MemberSpecification(1, 2, "Anno", "name", "desc");

        // Assert
        assertNull(spec.attributeNames);
    }
}
