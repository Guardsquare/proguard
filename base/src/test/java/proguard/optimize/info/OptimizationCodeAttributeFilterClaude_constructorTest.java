package proguard.optimize.info;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link OptimizationCodeAttributeFilter} constructor.
 * Tests OptimizationCodeAttributeFilter(AttributeVisitor, AttributeVisitor) constructor.
 */
public class OptimizationCodeAttributeFilterClaude_constructorTest {

    /**
     * Tests the constructor with two valid AttributeVisitors.
     * Verifies that the OptimizationCodeAttributeFilter instance can be instantiated with proper visitors.
     */
    @Test
    public void testConstructorWithTwoValidAttributeVisitors() {
        // Arrange - Create valid AttributeVisitors
        AttributeVisitor visitor1 = new TestAttributeVisitor();
        AttributeVisitor visitor2 = new TestAttributeVisitor();

        // Act - Create OptimizationCodeAttributeFilter with both visitors
        OptimizationCodeAttributeFilter filter = new OptimizationCodeAttributeFilter(visitor1, visitor2);

        // Assert - Verify the OptimizationCodeAttributeFilter instance was created successfully
        assertNotNull(filter, "OptimizationCodeAttributeFilter should be instantiated successfully");
    }

    /**
     * Tests the constructor with first visitor as null and second visitor as valid.
     * Verifies that the constructor accepts null for the first visitor.
     */
    @Test
    public void testConstructorWithNullFirstVisitor() {
        // Arrange - Create a valid second visitor
        AttributeVisitor visitor2 = new TestAttributeVisitor();

        // Act - Create OptimizationCodeAttributeFilter with null first visitor
        OptimizationCodeAttributeFilter filter = new OptimizationCodeAttributeFilter(null, visitor2);

        // Assert - Verify the OptimizationCodeAttributeFilter instance was created
        assertNotNull(filter, "OptimizationCodeAttributeFilter should be instantiated with null first visitor");
    }

    /**
     * Tests the constructor with first visitor as valid and second visitor as null.
     * Verifies that the constructor accepts null for the second visitor.
     */
    @Test
    public void testConstructorWithNullSecondVisitor() {
        // Arrange - Create a valid first visitor
        AttributeVisitor visitor1 = new TestAttributeVisitor();

        // Act - Create OptimizationCodeAttributeFilter with null second visitor
        OptimizationCodeAttributeFilter filter = new OptimizationCodeAttributeFilter(visitor1, null);

        // Assert - Verify the OptimizationCodeAttributeFilter instance was created
        assertNotNull(filter, "OptimizationCodeAttributeFilter should be instantiated with null second visitor");
    }

    /**
     * Tests the constructor with both visitors as null.
     * Verifies that the constructor accepts null for both visitors.
     */
    @Test
    public void testConstructorWithBothVisitorsNull() {
        // Act - Create OptimizationCodeAttributeFilter with both visitors null
        OptimizationCodeAttributeFilter filter = new OptimizationCodeAttributeFilter(null, null);

        // Assert - Verify the OptimizationCodeAttributeFilter instance was created
        assertNotNull(filter, "OptimizationCodeAttributeFilter should be instantiated with both null visitors");
    }

    /**
     * Tests the constructor multiple times with different visitors.
     * Verifies that multiple OptimizationCodeAttributeFilter instances can be created independently.
     */
    @Test
    public void testMultipleOptimizationCodeAttributeFilterInstances() {
        // Arrange - Create different AttributeVisitors
        AttributeVisitor visitor1 = new TestAttributeVisitor();
        AttributeVisitor visitor2 = new TestAttributeVisitor();
        AttributeVisitor visitor3 = new TestAttributeVisitor();
        AttributeVisitor visitor4 = new TestAttributeVisitor();

        // Act - Create two OptimizationCodeAttributeFilter instances
        OptimizationCodeAttributeFilter filter1 = new OptimizationCodeAttributeFilter(visitor1, visitor2);
        OptimizationCodeAttributeFilter filter2 = new OptimizationCodeAttributeFilter(visitor3, visitor4);

        // Assert - Verify both OptimizationCodeAttributeFilter instances were created successfully
        assertNotNull(filter1, "First OptimizationCodeAttributeFilter instance should be created");
        assertNotNull(filter2, "Second OptimizationCodeAttributeFilter instance should be created");
        assertNotSame(filter1, filter2, "OptimizationCodeAttributeFilter instances should be different objects");
    }

    /**
     * Tests the constructor with the same AttributeVisitor instances multiple times.
     * Verifies that the same visitors can be used to create multiple OptimizationCodeAttributeFilter instances.
     */
    @Test
    public void testMultipleOptimizationCodeAttributeFilterInstancesWithSameVisitors() {
        // Arrange - Create AttributeVisitors
        AttributeVisitor visitor1 = new TestAttributeVisitor();
        AttributeVisitor visitor2 = new TestAttributeVisitor();

        // Act - Create multiple OptimizationCodeAttributeFilter instances with the same visitors
        OptimizationCodeAttributeFilter filter1 = new OptimizationCodeAttributeFilter(visitor1, visitor2);
        OptimizationCodeAttributeFilter filter2 = new OptimizationCodeAttributeFilter(visitor1, visitor2);

        // Assert - Verify both OptimizationCodeAttributeFilter instances were created successfully
        assertNotNull(filter1, "First OptimizationCodeAttributeFilter instance should be created");
        assertNotNull(filter2, "Second OptimizationCodeAttributeFilter instance should be created");
        assertNotSame(filter1, filter2, "OptimizationCodeAttributeFilter instances should be different objects");
    }

    /**
     * Tests that the constructor properly stores the first visitor for non-kept code attributes.
     * Verifies that the stored first visitor is called correctly when the code attribute is not kept.
     */
    @Test
    public void testConstructorStoresFirstVisitorCorrectlyForNonKeptAttribute() {
        // Arrange - Create tracking visitors
        TrackingAttributeVisitor trackingVisitor1 = new TrackingAttributeVisitor();
        TrackingAttributeVisitor trackingVisitor2 = new TrackingAttributeVisitor();
        OptimizationCodeAttributeFilter filter = new OptimizationCodeAttributeFilter(trackingVisitor1, trackingVisitor2);

        // Create a CodeAttribute without KeepMarker (not kept)
        CodeAttribute codeAttribute = new CodeAttribute();
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();

        // Act - Visit the code attribute
        filter.visitCodeAttribute(programClass, programMethod, codeAttribute);

        // Assert - Verify only the first visitor was called
        assertTrue(trackingVisitor1.visited, "First visitor should have been called for non-kept code attribute");
        assertFalse(trackingVisitor2.visited, "Second visitor should NOT have been called for non-kept code attribute");
    }

    /**
     * Tests that the constructor properly stores the second visitor for kept code attributes.
     * Verifies that the stored second visitor is called correctly when the code attribute is kept.
     */
    @Test
    public void testConstructorStoresSecondVisitorCorrectlyForKeptAttribute() {
        // Arrange - Create tracking visitors
        TrackingAttributeVisitor trackingVisitor1 = new TrackingAttributeVisitor();
        TrackingAttributeVisitor trackingVisitor2 = new TrackingAttributeVisitor();
        OptimizationCodeAttributeFilter filter = new OptimizationCodeAttributeFilter(trackingVisitor1, trackingVisitor2);

        // Create a CodeAttribute with KeepMarker (kept)
        CodeAttribute codeAttribute = new CodeAttribute();
        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(codeAttribute);
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();

        // Act - Visit the code attribute
        filter.visitCodeAttribute(programClass, programMethod, codeAttribute);

        // Assert - Verify only the second visitor was called
        assertFalse(trackingVisitor1.visited, "First visitor should NOT have been called for kept code attribute");
        assertTrue(trackingVisitor2.visited, "Second visitor should have been called for kept code attribute");
    }

    /**
     * Tests that the constructor properly handles null first visitor when used with non-kept attribute.
     * Verifies that OptimizationCodeAttributeFilter can handle null first visitor gracefully during operation.
     */
    @Test
    public void testConstructorWithNullFirstVisitorDuringOperation() {
        // Arrange - Create OptimizationCodeAttributeFilter with null first visitor
        TrackingAttributeVisitor trackingVisitor2 = new TrackingAttributeVisitor();
        OptimizationCodeAttributeFilter filter = new OptimizationCodeAttributeFilter(null, trackingVisitor2);

        // Create a CodeAttribute without KeepMarker (not kept)
        CodeAttribute codeAttribute = new CodeAttribute();
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();

        // Act - Visit the code attribute (should not throw exception)
        filter.visitCodeAttribute(programClass, programMethod, codeAttribute);

        // Assert - Verify the second visitor was NOT called
        assertFalse(trackingVisitor2.visited, "Second visitor should NOT have been called for non-kept code attribute");
    }

    /**
     * Tests that the constructor properly handles null second visitor when used with kept attribute.
     * Verifies that OptimizationCodeAttributeFilter can handle null second visitor gracefully during operation.
     */
    @Test
    public void testConstructorWithNullSecondVisitorDuringOperation() {
        // Arrange - Create OptimizationCodeAttributeFilter with null second visitor
        TrackingAttributeVisitor trackingVisitor1 = new TrackingAttributeVisitor();
        OptimizationCodeAttributeFilter filter = new OptimizationCodeAttributeFilter(trackingVisitor1, null);

        // Create a CodeAttribute with KeepMarker (kept)
        CodeAttribute codeAttribute = new CodeAttribute();
        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(codeAttribute);
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();

        // Act - Visit the code attribute (should not throw exception)
        filter.visitCodeAttribute(programClass, programMethod, codeAttribute);

        // Assert - Verify the first visitor was NOT called
        assertFalse(trackingVisitor1.visited, "First visitor should NOT have been called for kept code attribute");
    }

    /**
     * Tests that the constructor accepts different AttributeVisitor implementations.
     * Verifies that OptimizationCodeAttributeFilter works with various AttributeVisitor implementations.
     */
    @Test
    public void testConstructorWithDifferentAttributeVisitorImplementations() {
        // Arrange & Act - Create OptimizationCodeAttributeFilter with different visitor types
        OptimizationCodeAttributeFilter filter1 = new OptimizationCodeAttributeFilter(
                new TestAttributeVisitor(), new TrackingAttributeVisitor());
        OptimizationCodeAttributeFilter filter2 = new OptimizationCodeAttributeFilter(
                new TrackingAttributeVisitor(), new TestAttributeVisitor());
        OptimizationCodeAttributeFilter filter3 = new OptimizationCodeAttributeFilter(
                new AnotherTestAttributeVisitor(), new AnotherTestAttributeVisitor());

        // Assert - Verify all OptimizationCodeAttributeFilter instances were created successfully
        assertNotNull(filter1, "OptimizationCodeAttributeFilter should work with mixed visitor types");
        assertNotNull(filter2, "OptimizationCodeAttributeFilter should work with reversed visitor types");
        assertNotNull(filter3, "OptimizationCodeAttributeFilter should work with same visitor types");
    }

    /**
     * Tests that the filter properly delegates based on keep status with multiple calls.
     * Verifies that the constructor stores the visitors correctly for repeated filtering operations.
     */
    @Test
    public void testConstructorStoresVisitorsCorrectlyForMultipleCalls() {
        // Arrange - Create counting visitors
        CountingAttributeVisitor countingVisitor1 = new CountingAttributeVisitor();
        CountingAttributeVisitor countingVisitor2 = new CountingAttributeVisitor();
        OptimizationCodeAttributeFilter filter = new OptimizationCodeAttributeFilter(countingVisitor1, countingVisitor2);

        // Create kept and non-kept code attributes
        CodeAttribute nonKeptAttribute1 = new CodeAttribute();
        CodeAttribute nonKeptAttribute2 = new CodeAttribute();

        CodeAttribute keptAttribute1 = new CodeAttribute();
        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(keptAttribute1);

        CodeAttribute keptAttribute2 = new CodeAttribute();
        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(keptAttribute2);

        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();

        // Act - Visit multiple code attributes
        filter.visitCodeAttribute(programClass, programMethod, nonKeptAttribute1);
        filter.visitCodeAttribute(programClass, programMethod, keptAttribute1);
        filter.visitCodeAttribute(programClass, programMethod, nonKeptAttribute2);
        filter.visitCodeAttribute(programClass, programMethod, keptAttribute2);

        // Assert - Verify the visitors were called the correct number of times
        assertEquals(2, countingVisitor1.visitCount, "First visitor should have been called twice for non-kept attributes");
        assertEquals(2, countingVisitor2.visitCount, "Second visitor should have been called twice for kept attributes");
    }

    /**
     * Tests that visitAnyAttribute does nothing regardless of constructor parameters.
     * Verifies that the filter only processes CodeAttribute and ignores other attributes.
     */
    @Test
    public void testConstructorWithVisitorsButVisitAnyAttributeDoesNothing() {
        // Arrange - Create counting visitors
        CountingAttributeVisitor countingVisitor1 = new CountingAttributeVisitor();
        CountingAttributeVisitor countingVisitor2 = new CountingAttributeVisitor();
        OptimizationCodeAttributeFilter filter = new OptimizationCodeAttributeFilter(countingVisitor1, countingVisitor2);

        // Create a non-CodeAttribute
        Attribute attribute = new Attribute() {};
        ProgramClass programClass = new ProgramClass();

        // Act - Visit a non-code attribute
        filter.visitAnyAttribute(programClass, attribute);

        // Assert - Verify neither visitor was called
        assertEquals(0, countingVisitor1.visitCount, "First visitor should not be called for non-code attributes");
        assertEquals(0, countingVisitor2.visitCount, "Second visitor should not be called for non-code attributes");
    }

    /**
     * Tests constructor behavior with same visitor instance for both parameters.
     * Verifies that the same visitor can be used for both kept and non-kept attributes.
     */
    @Test
    public void testConstructorWithSameVisitorForBothParameters() {
        // Arrange - Create a single tracking visitor
        CountingAttributeVisitor countingVisitor = new CountingAttributeVisitor();
        OptimizationCodeAttributeFilter filter = new OptimizationCodeAttributeFilter(countingVisitor, countingVisitor);

        // Create kept and non-kept code attributes
        CodeAttribute nonKeptAttribute = new CodeAttribute();
        CodeAttribute keptAttribute = new CodeAttribute();
        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(keptAttribute);

        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();

        // Act - Visit both types of attributes
        filter.visitCodeAttribute(programClass, programMethod, nonKeptAttribute);
        filter.visitCodeAttribute(programClass, programMethod, keptAttribute);

        // Assert - Verify the same visitor was called twice
        assertEquals(2, countingVisitor.visitCount, "Same visitor should be called for both kept and non-kept attributes");
    }

    /**
     * Simple test AttributeVisitor implementation for testing purposes.
     */
    private static class TestAttributeVisitor implements AttributeVisitor {
        @Override
        public void visitAnyAttribute(Clazz clazz, Attribute attribute) {
            // No-op for testing
        }

        @Override
        public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute) {
            // No-op for testing
        }
    }

    /**
     * AttributeVisitor implementation that tracks whether it was called.
     */
    private static class TrackingAttributeVisitor implements AttributeVisitor {
        boolean visited = false;

        @Override
        public void visitAnyAttribute(Clazz clazz, Attribute attribute) {
            visited = true;
        }

        @Override
        public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute) {
            visited = true;
        }
    }

    /**
     * AttributeVisitor implementation that counts how many times it was called.
     */
    private static class CountingAttributeVisitor implements AttributeVisitor {
        int visitCount = 0;

        @Override
        public void visitAnyAttribute(Clazz clazz, Attribute attribute) {
            visitCount++;
        }

        @Override
        public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute) {
            visitCount++;
        }
    }

    /**
     * Another test AttributeVisitor implementation for testing purposes.
     */
    private static class AnotherTestAttributeVisitor implements AttributeVisitor {
        @Override
        public void visitAnyAttribute(Clazz clazz, Attribute attribute) {
            // No-op for testing
        }

        @Override
        public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute) {
            // No-op for testing
        }
    }
}
