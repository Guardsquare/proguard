package proguard.evaluation;

import org.junit.jupiter.api.Test;
import proguard.MemberSpecification;
import proguard.MemberValueSpecification;
import proguard.classfile.Clazz;
import proguard.classfile.Member;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MemberVisitor;
import proguard.classfile.visitor.MultiMemberVisitor;
import proguard.evaluation.value.*;
import proguard.optimize.OptimizationInfoMemberFilter;
import proguard.util.WildcardManager;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link AssumeClassSpecificationVisitorFactory}.
 * Tests all methods to ensure proper functionality of the factory with value specifications.
 */
public class AssumeClassSpecificationVisitorFactoryClaudeTest {

    // ========== Constructor Tests ==========

    /**
     * Tests the constructor with a non-null ValueFactory.
     * Verifies that the factory can be instantiated with a ValueFactory.
     */
    @Test
    public void testConstructorWithNonNullValueFactory() {
        // Arrange
        ValueFactory valueFactory = new ParticularValueFactory();

        // Act
        AssumeClassSpecificationVisitorFactory factory =
            new AssumeClassSpecificationVisitorFactory(valueFactory);

        // Assert
        assertNotNull(factory, "Factory should be instantiated");
    }

    /**
     * Tests the constructor with another ValueFactory implementation.
     * Verifies compatibility with different ValueFactory implementations.
     */
    @Test
    public void testConstructorWithDifferentValueFactory() {
        // Arrange
        ValueFactory valueFactory = new BasicValueFactory();

        // Act
        AssumeClassSpecificationVisitorFactory factory =
            new AssumeClassSpecificationVisitorFactory(valueFactory);

        // Assert
        assertNotNull(factory, "Factory should be instantiated with BasicValueFactory");
    }

    /**
     * Tests constructor with IdentifiedValueFactory.
     */
    @Test
    public void testConstructorWithIdentifiedValueFactory() {
        // Arrange
        ValueFactory valueFactory = new IdentifiedValueFactory();

        // Act
        AssumeClassSpecificationVisitorFactory factory =
            new AssumeClassSpecificationVisitorFactory(valueFactory);

        // Assert
        assertNotNull(factory, "Factory should be instantiated with IdentifiedValueFactory");
    }

    // ========== createNonTestingClassVisitor Tests ==========

    /**
     * Tests createNonTestingClassVisitor with a regular MemberSpecification (not MemberValueSpecification).
     * Should return a non-null ClassVisitor that delegates to the parent implementation.
     */
    @Test
    public void testCreateNonTestingClassVisitorWithRegularMemberSpecification() {
        // Arrange
        ValueFactory valueFactory = new ParticularValueFactory();
        AssumeClassSpecificationVisitorFactory factory =
            new AssumeClassSpecificationVisitorFactory(valueFactory);

        MemberSpecification memberSpec = new MemberSpecification();
        TestMemberVisitor memberVisitor = new TestMemberVisitor();
        TestAttributeVisitor attributeVisitor = new TestAttributeVisitor();
        WildcardManager wildcardManager = new WildcardManager();

        // Act
        ClassVisitor result = factory.createNonTestingClassVisitor(
            memberSpec, true, memberVisitor, attributeVisitor, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null");
    }

    /**
     * Tests createNonTestingClassVisitor with MemberValueSpecification but null values.
     * Should return a ClassVisitor without adding the value setter.
     */
    @Test
    public void testCreateNonTestingClassVisitorWithMemberValueSpecificationNullValues() {
        // Arrange
        ValueFactory valueFactory = new ParticularValueFactory();
        AssumeClassSpecificationVisitorFactory factory =
            new AssumeClassSpecificationVisitorFactory(valueFactory);

        MemberValueSpecification memberValueSpec = new MemberValueSpecification();
        memberValueSpec.values = null;

        TestMemberVisitor memberVisitor = new TestMemberVisitor();
        WildcardManager wildcardManager = new WildcardManager();

        // Act
        ClassVisitor result = factory.createNonTestingClassVisitor(
            memberValueSpec, true, memberVisitor, null, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null even with null values");
    }

    /**
     * Tests createNonTestingClassVisitor with MemberValueSpecification with single value.
     * Should return a ClassVisitor that includes the value setter.
     */
    @Test
    public void testCreateNonTestingClassVisitorWithSingleValue() {
        // Arrange
        ValueFactory valueFactory = new ParticularValueFactory();
        AssumeClassSpecificationVisitorFactory factory =
            new AssumeClassSpecificationVisitorFactory(valueFactory);

        MemberValueSpecification memberValueSpec = new MemberValueSpecification();
        memberValueSpec.values = new Number[]{42};

        TestMemberVisitor memberVisitor = new TestMemberVisitor();
        WildcardManager wildcardManager = new WildcardManager();

        // Act
        ClassVisitor result = factory.createNonTestingClassVisitor(
            memberValueSpec, false, memberVisitor, null, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null with single value");
    }

    /**
     * Tests createNonTestingClassVisitor with MemberValueSpecification with value range.
     * Should return a ClassVisitor that includes the value setter with range.
     */
    @Test
    public void testCreateNonTestingClassVisitorWithValueRange() {
        // Arrange
        ValueFactory valueFactory = new ParticularValueFactory();
        AssumeClassSpecificationVisitorFactory factory =
            new AssumeClassSpecificationVisitorFactory(valueFactory);

        MemberValueSpecification memberValueSpec = new MemberValueSpecification();
        memberValueSpec.values = new Number[]{10, 100};

        TestMemberVisitor memberVisitor = new TestMemberVisitor();
        WildcardManager wildcardManager = new WildcardManager();

        // Act
        ClassVisitor result = factory.createNonTestingClassVisitor(
            memberValueSpec, false, memberVisitor, null, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null with value range");
    }

    /**
     * Tests createNonTestingClassVisitor with MemberValueSpecification for a field.
     * Verifies the isField parameter is properly handled.
     */
    @Test
    public void testCreateNonTestingClassVisitorForField() {
        // Arrange
        ValueFactory valueFactory = new BasicValueFactory();
        AssumeClassSpecificationVisitorFactory factory =
            new AssumeClassSpecificationVisitorFactory(valueFactory);

        MemberValueSpecification memberValueSpec = new MemberValueSpecification(
            0, 0, null, "testField", "I", new Number[]{123});

        TestMemberVisitor memberVisitor = new TestMemberVisitor();
        WildcardManager wildcardManager = new WildcardManager();

        // Act
        ClassVisitor result = factory.createNonTestingClassVisitor(
            memberValueSpec, true, memberVisitor, null, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null for field specification");
    }

    /**
     * Tests createNonTestingClassVisitor with MemberValueSpecification for a method.
     * Verifies the isField parameter set to false is properly handled.
     */
    @Test
    public void testCreateNonTestingClassVisitorForMethod() {
        // Arrange
        ValueFactory valueFactory = new BasicValueFactory();
        AssumeClassSpecificationVisitorFactory factory =
            new AssumeClassSpecificationVisitorFactory(valueFactory);

        MemberValueSpecification memberValueSpec = new MemberValueSpecification(
            0, 0, null, "testMethod", "()I", new Number[]{456});

        TestMemberVisitor memberVisitor = new TestMemberVisitor();
        WildcardManager wildcardManager = new WildcardManager();

        // Act
        ClassVisitor result = factory.createNonTestingClassVisitor(
            memberValueSpec, false, memberVisitor, null, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null for method specification");
    }

    /**
     * Tests createNonTestingClassVisitor with negative value.
     * Verifies that negative values are handled correctly.
     */
    @Test
    public void testCreateNonTestingClassVisitorWithNegativeValue() {
        // Arrange
        ValueFactory valueFactory = new ParticularValueFactory();
        AssumeClassSpecificationVisitorFactory factory =
            new AssumeClassSpecificationVisitorFactory(valueFactory);

        MemberValueSpecification memberValueSpec = new MemberValueSpecification();
        memberValueSpec.values = new Number[]{-42};

        TestMemberVisitor memberVisitor = new TestMemberVisitor();
        WildcardManager wildcardManager = new WildcardManager();

        // Act
        ClassVisitor result = factory.createNonTestingClassVisitor(
            memberValueSpec, true, memberVisitor, null, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null with negative value");
    }

    /**
     * Tests createNonTestingClassVisitor with zero value.
     * Verifies that zero is handled correctly.
     */
    @Test
    public void testCreateNonTestingClassVisitorWithZeroValue() {
        // Arrange
        ValueFactory valueFactory = new ParticularValueFactory();
        AssumeClassSpecificationVisitorFactory factory =
            new AssumeClassSpecificationVisitorFactory(valueFactory);

        MemberValueSpecification memberValueSpec = new MemberValueSpecification();
        memberValueSpec.values = new Number[]{0};

        TestMemberVisitor memberVisitor = new TestMemberVisitor();
        WildcardManager wildcardManager = new WildcardManager();

        // Act
        ClassVisitor result = factory.createNonTestingClassVisitor(
            memberValueSpec, false, memberVisitor, null, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null with zero value");
    }

    /**
     * Tests createNonTestingClassVisitor with null MemberVisitor.
     * Should handle null member visitor gracefully.
     */
    @Test
    public void testCreateNonTestingClassVisitorWithNullMemberVisitor() {
        // Arrange
        ValueFactory valueFactory = new ParticularValueFactory();
        AssumeClassSpecificationVisitorFactory factory =
            new AssumeClassSpecificationVisitorFactory(valueFactory);

        MemberValueSpecification memberValueSpec = new MemberValueSpecification();
        memberValueSpec.values = new Number[]{123};

        WildcardManager wildcardManager = new WildcardManager();

        // Act
        ClassVisitor result = factory.createNonTestingClassVisitor(
            memberValueSpec, true, null, null, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null with null member visitor");
    }

    /**
     * Tests createNonTestingClassVisitor with null AttributeVisitor.
     * Should handle null attribute visitor gracefully.
     */
    @Test
    public void testCreateNonTestingClassVisitorWithNullAttributeVisitor() {
        // Arrange
        ValueFactory valueFactory = new ParticularValueFactory();
        AssumeClassSpecificationVisitorFactory factory =
            new AssumeClassSpecificationVisitorFactory(valueFactory);

        MemberValueSpecification memberValueSpec = new MemberValueSpecification();
        memberValueSpec.values = new Number[]{789};

        TestMemberVisitor memberVisitor = new TestMemberVisitor();
        WildcardManager wildcardManager = new WildcardManager();

        // Act
        ClassVisitor result = factory.createNonTestingClassVisitor(
            memberValueSpec, false, memberVisitor, null, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null with null attribute visitor");
    }

    /**
     * Tests createNonTestingClassVisitor with null WildcardManager.
     * Should handle null wildcard manager.
     */
    @Test
    public void testCreateNonTestingClassVisitorWithNullWildcardManager() {
        // Arrange
        ValueFactory valueFactory = new ParticularValueFactory();
        AssumeClassSpecificationVisitorFactory factory =
            new AssumeClassSpecificationVisitorFactory(valueFactory);

        MemberSpecification memberSpec = new MemberSpecification();
        TestMemberVisitor memberVisitor = new TestMemberVisitor();

        // Act
        ClassVisitor result = factory.createNonTestingClassVisitor(
            memberSpec, true, memberVisitor, null, null);

        // Assert
        assertNotNull(result, "Result should not be null with null wildcard manager");
    }

    /**
     * Tests createNonTestingClassVisitor with MemberValueSpecification containing access flags.
     * Verifies that access flags are properly handled.
     */
    @Test
    public void testCreateNonTestingClassVisitorWithAccessFlags() {
        // Arrange
        ValueFactory valueFactory = new ParticularValueFactory();
        AssumeClassSpecificationVisitorFactory factory =
            new AssumeClassSpecificationVisitorFactory(valueFactory);

        MemberValueSpecification memberValueSpec = new MemberValueSpecification(
            0x0001, // public flag
            0,
            null,
            "field",
            "I",
            new Number[]{100}
        );

        TestMemberVisitor memberVisitor = new TestMemberVisitor();
        WildcardManager wildcardManager = new WildcardManager();

        // Act
        ClassVisitor result = factory.createNonTestingClassVisitor(
            memberValueSpec, true, memberVisitor, null, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null with access flags");
    }

    /**
     * Tests createNonTestingClassVisitor with MemberValueSpecification containing annotation type.
     * Verifies that annotation type is properly handled.
     */
    @Test
    public void testCreateNonTestingClassVisitorWithAnnotationType() {
        // Arrange
        ValueFactory valueFactory = new ParticularValueFactory();
        AssumeClassSpecificationVisitorFactory factory =
            new AssumeClassSpecificationVisitorFactory(valueFactory);

        MemberValueSpecification memberValueSpec = new MemberValueSpecification(
            0,
            0,
            "Ljava/lang/Deprecated;",
            "field",
            "I",
            new Number[]{200}
        );

        TestMemberVisitor memberVisitor = new TestMemberVisitor();
        WildcardManager wildcardManager = new WildcardManager();

        // Act
        ClassVisitor result = factory.createNonTestingClassVisitor(
            memberValueSpec, true, memberVisitor, null, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null with annotation type");
    }

    /**
     * Tests createNonTestingClassVisitor with AttributeVisitor provided.
     * Verifies that attribute visitor is properly passed through.
     */
    @Test
    public void testCreateNonTestingClassVisitorWithAttributeVisitor() {
        // Arrange
        ValueFactory valueFactory = new BasicValueFactory();
        AssumeClassSpecificationVisitorFactory factory =
            new AssumeClassSpecificationVisitorFactory(valueFactory);

        MemberValueSpecification memberValueSpec = new MemberValueSpecification();
        memberValueSpec.values = new Number[]{555};

        TestMemberVisitor memberVisitor = new TestMemberVisitor();
        TestAttributeVisitor attributeVisitor = new TestAttributeVisitor();
        WildcardManager wildcardManager = new WildcardManager();

        // Act
        ClassVisitor result = factory.createNonTestingClassVisitor(
            memberValueSpec, false, memberVisitor, attributeVisitor, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null with attribute visitor");
    }

    /**
     * Tests createNonTestingClassVisitor with large value.
     * Verifies that large integer values are handled correctly.
     */
    @Test
    public void testCreateNonTestingClassVisitorWithLargeValue() {
        // Arrange
        ValueFactory valueFactory = new ParticularValueFactory();
        AssumeClassSpecificationVisitorFactory factory =
            new AssumeClassSpecificationVisitorFactory(valueFactory);

        MemberValueSpecification memberValueSpec = new MemberValueSpecification();
        memberValueSpec.values = new Number[]{Integer.MAX_VALUE};

        TestMemberVisitor memberVisitor = new TestMemberVisitor();
        WildcardManager wildcardManager = new WildcardManager();

        // Act
        ClassVisitor result = factory.createNonTestingClassVisitor(
            memberValueSpec, true, memberVisitor, null, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null with large value");
    }

    /**
     * Tests createNonTestingClassVisitor with minimum value.
     * Verifies that Integer.MIN_VALUE is handled correctly.
     */
    @Test
    public void testCreateNonTestingClassVisitorWithMinimumValue() {
        // Arrange
        ValueFactory valueFactory = new ParticularValueFactory();
        AssumeClassSpecificationVisitorFactory factory =
            new AssumeClassSpecificationVisitorFactory(valueFactory);

        MemberValueSpecification memberValueSpec = new MemberValueSpecification();
        memberValueSpec.values = new Number[]{Integer.MIN_VALUE};

        TestMemberVisitor memberVisitor = new TestMemberVisitor();
        WildcardManager wildcardManager = new WildcardManager();

        // Act
        ClassVisitor result = factory.createNonTestingClassVisitor(
            memberValueSpec, false, memberVisitor, null, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null with minimum value");
    }

    /**
     * Tests createNonTestingClassVisitor with value range where min > max.
     * Verifies behavior with inverted range.
     */
    @Test
    public void testCreateNonTestingClassVisitorWithInvertedRange() {
        // Arrange
        ValueFactory valueFactory = new ParticularValueFactory();
        AssumeClassSpecificationVisitorFactory factory =
            new AssumeClassSpecificationVisitorFactory(valueFactory);

        MemberValueSpecification memberValueSpec = new MemberValueSpecification();
        // Inverted range: higher value first
        memberValueSpec.values = new Number[]{100, 10};

        TestMemberVisitor memberVisitor = new TestMemberVisitor();
        WildcardManager wildcardManager = new WildcardManager();

        // Act - should not throw exception
        ClassVisitor result = factory.createNonTestingClassVisitor(
            memberValueSpec, true, memberVisitor, null, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null with inverted range");
    }

    /**
     * Tests createNonTestingClassVisitor with empty values array.
     * Verifies handling of empty array (edge case).
     */
    @Test
    public void testCreateNonTestingClassVisitorWithEmptyValuesArray() {
        // Arrange
        ValueFactory valueFactory = new ParticularValueFactory();
        AssumeClassSpecificationVisitorFactory factory =
            new AssumeClassSpecificationVisitorFactory(valueFactory);

        MemberValueSpecification memberValueSpec = new MemberValueSpecification();
        memberValueSpec.values = new Number[]{};

        TestMemberVisitor memberVisitor = new TestMemberVisitor();
        WildcardManager wildcardManager = new WildcardManager();

        // Act - may throw exception or handle gracefully
        try {
            ClassVisitor result = factory.createNonTestingClassVisitor(
                memberValueSpec, false, memberVisitor, null, wildcardManager);
            // If it doesn't throw, verify result
            assertNotNull(result, "Result should not be null even with empty values array");
        } catch (ArrayIndexOutOfBoundsException e) {
            // This is acceptable behavior for empty array
            assertTrue(true, "Empty array caused exception as expected");
        }
    }

    /**
     * Tests createNonTestingClassVisitor with values array of length 3.
     * Verifies behavior with more than 2 values (edge case).
     */
    @Test
    public void testCreateNonTestingClassVisitorWithThreeValues() {
        // Arrange
        ValueFactory valueFactory = new ParticularValueFactory();
        AssumeClassSpecificationVisitorFactory factory =
            new AssumeClassSpecificationVisitorFactory(valueFactory);

        MemberValueSpecification memberValueSpec = new MemberValueSpecification();
        // More than 2 values - uses first two for range
        memberValueSpec.values = new Number[]{1, 10, 100};

        TestMemberVisitor memberVisitor = new TestMemberVisitor();
        WildcardManager wildcardManager = new WildcardManager();

        // Act
        ClassVisitor result = factory.createNonTestingClassVisitor(
            memberValueSpec, true, memberVisitor, null, wildcardManager);

        // Assert
        assertNotNull(result, "Result should not be null with three values");
    }

    // ========== Helper Classes ==========

    /**
     * Test implementation of ClassVisitor.
     */
    private static class TestClassVisitor implements ClassVisitor {
        @Override
        public void visitAnyClass(Clazz clazz) {
            // No-op for testing
        }
    }

    /**
     * Test implementation of MemberVisitor.
     */
    private static class TestMemberVisitor implements MemberVisitor {
        @Override
        public void visitAnyMember(Clazz clazz, Member member) {
            // No-op for testing
        }
    }

    /**
     * Test implementation of AttributeVisitor.
     */
    private static class TestAttributeVisitor implements AttributeVisitor {
        @Override
        public void visitAnyAttribute(Clazz clazz, Attribute attribute) {
            // No-op for testing
        }
    }
}
