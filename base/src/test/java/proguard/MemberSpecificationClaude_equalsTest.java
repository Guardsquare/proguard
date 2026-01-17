package proguard;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link MemberSpecification#equals(Object)} method.
 */
public class MemberSpecificationClaude_equalsTest {

    /**
     * Tests that equals returns true when comparing an object with itself.
     */
    @Test
    public void testEqualsWithSameObject() {
        // Arrange
        MemberSpecification spec = new MemberSpecification(1, 2, "Anno", "name", "desc");

        // Act & Assert
        assertTrue(spec.equals(spec));
    }

    /**
     * Tests that equals returns false when comparing with null.
     */
    @Test
    public void testEqualsWithNull() {
        // Arrange
        MemberSpecification spec = new MemberSpecification(1, 2, "Anno", "name", "desc");

        // Act & Assert
        assertFalse(spec.equals(null));
    }

    /**
     * Tests that equals returns false when comparing with an object of different class.
     */
    @Test
    public void testEqualsWithDifferentClass() {
        // Arrange
        MemberSpecification spec = new MemberSpecification(1, 2, "Anno", "name", "desc");
        String differentClass = "not a MemberSpecification";

        // Act & Assert
        assertFalse(spec.equals(differentClass));
    }

    /**
     * Tests that equals returns true for two instances with identical parameters.
     */
    @Test
    public void testEqualsWithIdenticalParameters() {
        // Arrange
        MemberSpecification spec1 = new MemberSpecification(1, 2, "Anno", "name", "desc");
        MemberSpecification spec2 = new MemberSpecification(1, 2, "Anno", "name", "desc");

        // Act & Assert
        assertTrue(spec1.equals(spec2));
        assertTrue(spec2.equals(spec1)); // Test symmetry
    }

    /**
     * Tests that equals returns true for two instances with all null string fields.
     */
    @Test
    public void testEqualsWithAllNullStrings() {
        // Arrange
        MemberSpecification spec1 = new MemberSpecification(0, 0, null, null, null);
        MemberSpecification spec2 = new MemberSpecification(0, 0, null, null, null);

        // Act & Assert
        assertTrue(spec1.equals(spec2));
    }

    /**
     * Tests that equals returns false when requiredSetAccessFlags differ.
     */
    @Test
    public void testEqualsWithDifferentRequiredSetAccessFlags() {
        // Arrange
        MemberSpecification spec1 = new MemberSpecification(1, 0, null, null, null);
        MemberSpecification spec2 = new MemberSpecification(2, 0, null, null, null);

        // Act & Assert
        assertFalse(spec1.equals(spec2));
    }

    /**
     * Tests that equals returns false when requiredUnsetAccessFlags differ.
     */
    @Test
    public void testEqualsWithDifferentRequiredUnsetAccessFlags() {
        // Arrange
        MemberSpecification spec1 = new MemberSpecification(0, 1, null, null, null);
        MemberSpecification spec2 = new MemberSpecification(0, 2, null, null, null);

        // Act & Assert
        assertFalse(spec1.equals(spec2));
    }

    /**
     * Tests that equals returns false when annotationType differs (one null, one not).
     */
    @Test
    public void testEqualsWithDifferentAnnotationType_OneNull() {
        // Arrange
        MemberSpecification spec1 = new MemberSpecification(0, 0, "Anno", null, null);
        MemberSpecification spec2 = new MemberSpecification(0, 0, null, null, null);

        // Act & Assert
        assertFalse(spec1.equals(spec2));
        assertFalse(spec2.equals(spec1)); // Test symmetry
    }

    /**
     * Tests that equals returns false when annotationType differs (both non-null).
     */
    @Test
    public void testEqualsWithDifferentAnnotationType_BothNonNull() {
        // Arrange
        MemberSpecification spec1 = new MemberSpecification(0, 0, "Anno1", null, null);
        MemberSpecification spec2 = new MemberSpecification(0, 0, "Anno2", null, null);

        // Act & Assert
        assertFalse(spec1.equals(spec2));
    }

    /**
     * Tests that equals returns true when both annotationType are null.
     */
    @Test
    public void testEqualsWithBothAnnotationTypeNull() {
        // Arrange
        MemberSpecification spec1 = new MemberSpecification(1, 2, null, "name", "desc");
        MemberSpecification spec2 = new MemberSpecification(1, 2, null, "name", "desc");

        // Act & Assert
        assertTrue(spec1.equals(spec2));
    }

    /**
     * Tests that equals returns false when name differs (one null, one not).
     */
    @Test
    public void testEqualsWithDifferentName_OneNull() {
        // Arrange
        MemberSpecification spec1 = new MemberSpecification(0, 0, null, "name", null);
        MemberSpecification spec2 = new MemberSpecification(0, 0, null, null, null);

        // Act & Assert
        assertFalse(spec1.equals(spec2));
        assertFalse(spec2.equals(spec1)); // Test symmetry
    }

    /**
     * Tests that equals returns false when name differs (both non-null).
     */
    @Test
    public void testEqualsWithDifferentName_BothNonNull() {
        // Arrange
        MemberSpecification spec1 = new MemberSpecification(0, 0, null, "name1", null);
        MemberSpecification spec2 = new MemberSpecification(0, 0, null, "name2", null);

        // Act & Assert
        assertFalse(spec1.equals(spec2));
    }

    /**
     * Tests that equals returns true when both name are null.
     */
    @Test
    public void testEqualsWithBothNameNull() {
        // Arrange
        MemberSpecification spec1 = new MemberSpecification(1, 2, "Anno", null, "desc");
        MemberSpecification spec2 = new MemberSpecification(1, 2, "Anno", null, "desc");

        // Act & Assert
        assertTrue(spec1.equals(spec2));
    }

    /**
     * Tests that equals returns false when descriptor differs (one null, one not).
     */
    @Test
    public void testEqualsWithDifferentDescriptor_OneNull() {
        // Arrange
        MemberSpecification spec1 = new MemberSpecification(0, 0, null, null, "desc");
        MemberSpecification spec2 = new MemberSpecification(0, 0, null, null, null);

        // Act & Assert
        assertFalse(spec1.equals(spec2));
        assertFalse(spec2.equals(spec1)); // Test symmetry
    }

    /**
     * Tests that equals returns false when descriptor differs (both non-null).
     */
    @Test
    public void testEqualsWithDifferentDescriptor_BothNonNull() {
        // Arrange
        MemberSpecification spec1 = new MemberSpecification(0, 0, null, null, "desc1");
        MemberSpecification spec2 = new MemberSpecification(0, 0, null, null, "desc2");

        // Act & Assert
        assertFalse(spec1.equals(spec2));
    }

    /**
     * Tests that equals returns true when both descriptor are null.
     */
    @Test
    public void testEqualsWithBothDescriptorNull() {
        // Arrange
        MemberSpecification spec1 = new MemberSpecification(1, 2, "Anno", "name", null);
        MemberSpecification spec2 = new MemberSpecification(1, 2, "Anno", "name", null);

        // Act & Assert
        assertTrue(spec1.equals(spec2));
    }

    /**
     * Tests that equals is transitive (if a=b and b=c, then a=c).
     */
    @Test
    public void testEqualsTransitivity() {
        // Arrange
        MemberSpecification spec1 = new MemberSpecification(1, 2, "Anno", "name", "desc");
        MemberSpecification spec2 = new MemberSpecification(1, 2, "Anno", "name", "desc");
        MemberSpecification spec3 = new MemberSpecification(1, 2, "Anno", "name", "desc");

        // Act & Assert
        assertTrue(spec1.equals(spec2));
        assertTrue(spec2.equals(spec3));
        assertTrue(spec1.equals(spec3)); // Transitivity
    }

    /**
     * Tests that equals returns false when only requiredSetAccessFlags match.
     */
    @Test
    public void testEqualsWithOnlySetAccessFlagsMatching() {
        // Arrange
        MemberSpecification spec1 = new MemberSpecification(1, 2, "Anno", "name", "desc");
        MemberSpecification spec2 = new MemberSpecification(1, 3, "Different", "different", "different");

        // Act & Assert
        assertFalse(spec1.equals(spec2));
    }

    /**
     * Tests equals with empty strings vs null strings.
     */
    @Test
    public void testEqualsWithEmptyStringVsNull() {
        // Arrange
        MemberSpecification spec1 = new MemberSpecification(0, 0, "", "", "");
        MemberSpecification spec2 = new MemberSpecification(0, 0, null, null, null);

        // Act & Assert
        assertFalse(spec1.equals(spec2));
    }

    /**
     * Tests equals with empty strings in both objects.
     */
    @Test
    public void testEqualsWithBothEmptyStrings() {
        // Arrange
        MemberSpecification spec1 = new MemberSpecification(0, 0, "", "", "");
        MemberSpecification spec2 = new MemberSpecification(0, 0, "", "", "");

        // Act & Assert
        assertTrue(spec1.equals(spec2));
    }

    /**
     * Tests equals with complex annotation type strings.
     */
    @Test
    public void testEqualsWithComplexAnnotationType() {
        // Arrange
        MemberSpecification spec1 = new MemberSpecification(0, 0, "Lcom/example/MyAnnotation;", null, null);
        MemberSpecification spec2 = new MemberSpecification(0, 0, "Lcom/example/MyAnnotation;", null, null);

        // Act & Assert
        assertTrue(spec1.equals(spec2));
    }

    /**
     * Tests equals with wildcard names.
     */
    @Test
    public void testEqualsWithWildcardNames() {
        // Arrange
        MemberSpecification spec1 = new MemberSpecification(0, 0, null, "get*", null);
        MemberSpecification spec2 = new MemberSpecification(0, 0, null, "get*", null);

        // Act & Assert
        assertTrue(spec1.equals(spec2));
    }

    /**
     * Tests equals with different wildcard patterns in names.
     */
    @Test
    public void testEqualsWithDifferentWildcardNames() {
        // Arrange
        MemberSpecification spec1 = new MemberSpecification(0, 0, null, "get*", null);
        MemberSpecification spec2 = new MemberSpecification(0, 0, null, "set*", null);

        // Act & Assert
        assertFalse(spec1.equals(spec2));
    }

    /**
     * Tests equals with complex descriptors.
     */
    @Test
    public void testEqualsWithComplexDescriptors() {
        // Arrange
        MemberSpecification spec1 = new MemberSpecification(0, 0, null, null, "(Ljava/lang/String;I)V");
        MemberSpecification spec2 = new MemberSpecification(0, 0, null, null, "(Ljava/lang/String;I)V");

        // Act & Assert
        assertTrue(spec1.equals(spec2));
    }

    /**
     * Tests equals with wildcard descriptors.
     */
    @Test
    public void testEqualsWithWildcardDescriptors() {
        // Arrange
        MemberSpecification spec1 = new MemberSpecification(0, 0, null, null, "(**)*");
        MemberSpecification spec2 = new MemberSpecification(0, 0, null, null, "(**)*");

        // Act & Assert
        assertTrue(spec1.equals(spec2));
    }

    /**
     * Tests equals with negative access flags.
     */
    @Test
    public void testEqualsWithNegativeAccessFlags() {
        // Arrange
        MemberSpecification spec1 = new MemberSpecification(-1, -2, null, null, null);
        MemberSpecification spec2 = new MemberSpecification(-1, -2, null, null, null);

        // Act & Assert
        assertTrue(spec1.equals(spec2));
    }

    /**
     * Tests equals with maximum integer values for access flags.
     */
    @Test
    public void testEqualsWithMaxIntAccessFlags() {
        // Arrange
        MemberSpecification spec1 = new MemberSpecification(Integer.MAX_VALUE, Integer.MAX_VALUE, null, null, null);
        MemberSpecification spec2 = new MemberSpecification(Integer.MAX_VALUE, Integer.MAX_VALUE, null, null, null);

        // Act & Assert
        assertTrue(spec1.equals(spec2));
    }

    /**
     * Tests equals with minimum integer values for access flags.
     */
    @Test
    public void testEqualsWithMinIntAccessFlags() {
        // Arrange
        MemberSpecification spec1 = new MemberSpecification(Integer.MIN_VALUE, Integer.MIN_VALUE, null, null, null);
        MemberSpecification spec2 = new MemberSpecification(Integer.MIN_VALUE, Integer.MIN_VALUE, null, null, null);

        // Act & Assert
        assertTrue(spec1.equals(spec2));
    }

    /**
     * Tests that modifying non-final fields affects equality.
     */
    @Test
    public void testEqualsAfterModifyingNonFinalFields() {
        // Arrange
        MemberSpecification spec1 = new MemberSpecification(1, 2, "Anno", "name", "desc");
        MemberSpecification spec2 = new MemberSpecification(1, 2, "Anno", "name", "desc");

        // Initially equal
        assertTrue(spec1.equals(spec2));

        // Modify non-final field
        spec1.requiredSetAccessFlags = 10;

        // Act & Assert - should no longer be equal
        assertFalse(spec1.equals(spec2));
    }

    /**
     * Tests that default constructor instances are equal to each other.
     */
    @Test
    public void testEqualsWithDefaultConstructors() {
        // Arrange
        MemberSpecification spec1 = new MemberSpecification();
        MemberSpecification spec2 = new MemberSpecification();

        // Act & Assert
        assertTrue(spec1.equals(spec2));
    }

    /**
     * Tests that default constructor instance equals parameterized constructor with zeros and nulls.
     */
    @Test
    public void testEqualsDefaultVsParameterizedWithZerosAndNulls() {
        // Arrange
        MemberSpecification spec1 = new MemberSpecification();
        MemberSpecification spec2 = new MemberSpecification(0, 0, null, null, null);

        // Act & Assert
        assertTrue(spec1.equals(spec2));
    }

    /**
     * Tests equals consistency - multiple invocations should return same result.
     */
    @Test
    public void testEqualsConsistency() {
        // Arrange
        MemberSpecification spec1 = new MemberSpecification(1, 2, "Anno", "name", "desc");
        MemberSpecification spec2 = new MemberSpecification(1, 2, "Anno", "name", "desc");

        // Act & Assert - multiple calls should return same result
        assertTrue(spec1.equals(spec2));
        assertTrue(spec1.equals(spec2));
        assertTrue(spec1.equals(spec2));
    }

    /**
     * Tests equals with case-sensitive string comparisons.
     */
    @Test
    public void testEqualsWithCaseSensitiveStrings() {
        // Arrange
        MemberSpecification spec1 = new MemberSpecification(0, 0, "Anno", "Name", "Desc");
        MemberSpecification spec2 = new MemberSpecification(0, 0, "anno", "name", "desc");

        // Act & Assert - strings should be case-sensitive
        assertFalse(spec1.equals(spec2));
    }

    /**
     * Tests equals with whitespace differences in strings.
     */
    @Test
    public void testEqualsWithWhitespaceDifferences() {
        // Arrange
        MemberSpecification spec1 = new MemberSpecification(0, 0, "Anno", "name", "desc");
        MemberSpecification spec2 = new MemberSpecification(0, 0, "Anno ", "name", "desc");

        // Act & Assert - whitespace should matter
        assertFalse(spec1.equals(spec2));
    }

    /**
     * Tests equals when all fields differ.
     */
    @Test
    public void testEqualsWithAllFieldsDifferent() {
        // Arrange
        MemberSpecification spec1 = new MemberSpecification(1, 2, "Anno1", "name1", "desc1");
        MemberSpecification spec2 = new MemberSpecification(3, 4, "Anno2", "name2", "desc2");

        // Act & Assert
        assertFalse(spec1.equals(spec2));
    }

    /**
     * Tests equals when only string fields match but access flags differ.
     */
    @Test
    public void testEqualsWithMatchingStringsButDifferentFlags() {
        // Arrange
        MemberSpecification spec1 = new MemberSpecification(1, 2, "Anno", "name", "desc");
        MemberSpecification spec2 = new MemberSpecification(3, 4, "Anno", "name", "desc");

        // Act & Assert
        assertFalse(spec1.equals(spec2));
    }
}
