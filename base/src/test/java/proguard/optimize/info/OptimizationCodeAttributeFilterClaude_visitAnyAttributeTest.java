package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link OptimizationCodeAttributeFilter#visitAnyAttribute(Clazz, Attribute)}.
 *
 * The visitAnyAttribute method is an empty implementation (no-op) that serves as a default
 * handler in the AttributeVisitor pattern for attributes that don't have specialized visitor methods.
 * According to the class documentation, any attribute other than CodeAttribute will NOT be delegated.
 */
public class OptimizationCodeAttributeFilterClaude_visitAnyAttributeTest {

    private OptimizationCodeAttributeFilter filter;
    private AttributeVisitor attributeVisitor;
    private AttributeVisitor otherAttributeVisitor;
    private Clazz clazz;
    private Attribute attribute;

    @BeforeEach
    public void setUp() {
        attributeVisitor = mock(AttributeVisitor.class);
        otherAttributeVisitor = mock(AttributeVisitor.class);
        filter = new OptimizationCodeAttributeFilter(attributeVisitor, otherAttributeVisitor);
        clazz = mock(ProgramClass.class);
        attribute = mock(Attribute.class);
    }

    /**
     * Tests that visitAnyAttribute can be called with valid mock objects without throwing exceptions.
     * Since this is a no-op method, it should simply do nothing and complete successfully.
     */
    @Test
    public void testVisitAnyAttribute_withValidMocks_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> filter.visitAnyAttribute(clazz, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called with null Clazz parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyAttribute_withNullClazz_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> filter.visitAnyAttribute(null, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called with null Attribute parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyAttribute_withNullAttribute_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> filter.visitAnyAttribute(clazz, null));
    }

    /**
     * Tests that visitAnyAttribute can be called with both parameters null.
     * The method should handle null parameters gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyAttribute_withBothParametersNull_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> filter.visitAnyAttribute(null, null));
    }

    /**
     * Tests that visitAnyAttribute can be called multiple times in succession.
     * The method should be idempotent and handle repeated calls without issues.
     */
    @Test
    public void testVisitAnyAttribute_calledMultipleTimes_doesNotThrowException() {
        // Act & Assert - should not throw any exception on multiple calls
        assertDoesNotThrow(() -> {
            filter.visitAnyAttribute(clazz, attribute);
            filter.visitAnyAttribute(clazz, attribute);
            filter.visitAnyAttribute(clazz, attribute);
        });
    }

    /**
     * Tests that visitAnyAttribute doesn't interact with the Clazz parameter.
     * Since it's a no-op method, it should not call any methods on the clazz.
     */
    @Test
    public void testVisitAnyAttribute_doesNotInteractWithClazz() {
        // Act
        filter.visitAnyAttribute(clazz, attribute);

        // Assert - verify no interactions occurred with the clazz mock
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitAnyAttribute doesn't interact with the Attribute parameter.
     * Since it's a no-op method, it should not call any methods on the attribute.
     */
    @Test
    public void testVisitAnyAttribute_doesNotInteractWithAttribute() {
        // Act
        filter.visitAnyAttribute(clazz, attribute);

        // Assert - verify no interactions occurred with the attribute mock
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute doesn't delegate to the attributeVisitor.
     * Since it's a no-op method, it should not call any delegated visitors.
     */
    @Test
    public void testVisitAnyAttribute_doesNotDelegateToAttributeVisitor() {
        // Act
        filter.visitAnyAttribute(clazz, attribute);

        // Assert - verify no interactions occurred with the attribute visitor
        verifyNoInteractions(attributeVisitor);
    }

    /**
     * Tests that visitAnyAttribute doesn't delegate to the otherAttributeVisitor.
     * Since it's a no-op method, it should not call any delegated visitors.
     */
    @Test
    public void testVisitAnyAttribute_doesNotDelegateToOtherAttributeVisitor() {
        // Act
        filter.visitAnyAttribute(clazz, attribute);

        // Assert - verify no interactions occurred with the other attribute visitor
        verifyNoInteractions(otherAttributeVisitor);
    }

    /**
     * Tests that visitAnyAttribute doesn't affect the filter's internal state.
     * Calling the method should not change any fields or trigger any side effects.
     */
    @Test
    public void testVisitAnyAttribute_doesNotModifyFilterState() {
        // Arrange - create another filter with the same configuration
        OptimizationCodeAttributeFilter filter2 = new OptimizationCodeAttributeFilter(
                attributeVisitor, otherAttributeVisitor);

        // Act - call visitAnyAttribute on the first filter
        filter.visitAnyAttribute(clazz, attribute);

        // Assert - both filters should be functionally equivalent
        // Since visitAnyAttribute is a no-op, we verify no visitors were invoked
        verifyNoInteractions(attributeVisitor);
        verifyNoInteractions(otherAttributeVisitor);
    }

    /**
     * Tests that visitAnyAttribute works with different Clazz mock instances.
     * The method should handle any Clazz implementation without issues.
     */
    @Test
    public void testVisitAnyAttribute_withDifferentClazzInstances_doesNotThrowException() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Clazz clazz3 = mock(ProgramClass.class);

        // Act & Assert - should not throw any exception with different instances
        assertDoesNotThrow(() -> {
            filter.visitAnyAttribute(clazz1, attribute);
            filter.visitAnyAttribute(clazz2, attribute);
            filter.visitAnyAttribute(clazz3, attribute);
        });
    }

    /**
     * Tests that visitAnyAttribute works with different Attribute mock instances.
     * The method should handle any Attribute implementation without issues.
     */
    @Test
    public void testVisitAnyAttribute_withDifferentAttributeInstances_doesNotThrowException() {
        // Arrange
        Attribute attr1 = mock(Attribute.class);
        Attribute attr2 = mock(Attribute.class);
        Attribute attr3 = mock(Attribute.class);

        // Act & Assert - should not throw any exception with different instances
        assertDoesNotThrow(() -> {
            filter.visitAnyAttribute(clazz, attr1);
            filter.visitAnyAttribute(clazz, attr2);
            filter.visitAnyAttribute(clazz, attr3);
        });
    }

    /**
     * Tests that visitAnyAttribute execution completes immediately.
     * Since it's a no-op method, it should have minimal overhead.
     */
    @Test
    public void testVisitAnyAttribute_executesQuickly() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            filter.visitAnyAttribute(clazz, attribute);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100, "visitAnyAttribute should execute quickly as it's a no-op");
    }

    /**
     * Tests that visitAnyAttribute doesn't affect subsequent calls to other methods.
     * The no-op should not interfere with the filter's normal operation.
     */
    @Test
    public void testVisitAnyAttribute_doesNotAffectSubsequentOperations() {
        // Act - call visitAnyAttribute first
        filter.visitAnyAttribute(clazz, attribute);

        // Then call visitAnyAttribute again
        filter.visitAnyAttribute(clazz, attribute);

        // Assert - verify no side effects occurred
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
        verifyNoInteractions(attributeVisitor);
        verifyNoInteractions(otherAttributeVisitor);
    }

    /**
     * Tests that visitAnyAttribute with real Attribute implementations doesn't throw exceptions.
     * This ensures the no-op works with concrete attribute types.
     */
    @Test
    public void testVisitAnyAttribute_withVariousAttributeTypes_doesNotThrowException() {
        // Arrange - test with various attribute types
        Attribute attr1 = mock(Attribute.class);
        Attribute attr2 = mock(Attribute.class);
        Attribute attr3 = mock(Attribute.class);

        when(attr1.getAttributeName(any())).thenReturn("CustomAttribute1");
        when(attr2.getAttributeName(any())).thenReturn("CustomAttribute2");
        when(attr3.getAttributeName(any())).thenReturn("CustomAttribute3");

        // Act & Assert - should handle all attribute types gracefully
        assertDoesNotThrow(() -> {
            filter.visitAnyAttribute(clazz, attr1);
            filter.visitAnyAttribute(clazz, attr2);
            filter.visitAnyAttribute(clazz, attr3);
        });

        // Verify that the attribute methods were not called since it's a no-op
        verify(attr1, never()).getAttributeName(any());
        verify(attr2, never()).getAttributeName(any());
        verify(attr3, never()).getAttributeName(any());
    }

    /**
     * Tests that multiple filters can call visitAnyAttribute independently.
     * Each filter's no-op should not affect others.
     */
    @Test
    public void testVisitAnyAttribute_withMultipleFilters_operateIndependently() {
        // Arrange - create multiple filters with different visitors
        AttributeVisitor visitor1 = mock(AttributeVisitor.class);
        AttributeVisitor visitor2 = mock(AttributeVisitor.class);
        AttributeVisitor otherVisitor1 = mock(AttributeVisitor.class);
        AttributeVisitor otherVisitor2 = mock(AttributeVisitor.class);

        OptimizationCodeAttributeFilter filter1 = new OptimizationCodeAttributeFilter(visitor1, otherVisitor1);
        OptimizationCodeAttributeFilter filter2 = new OptimizationCodeAttributeFilter(visitor2, otherVisitor2);

        // Act - call visitAnyAttribute on both filters
        filter1.visitAnyAttribute(clazz, attribute);
        filter2.visitAnyAttribute(clazz, attribute);

        // Assert - verify no interactions occurred on any visitor
        verifyNoInteractions(visitor1);
        verifyNoInteractions(visitor2);
        verifyNoInteractions(otherVisitor1);
        verifyNoInteractions(otherVisitor2);
    }

    /**
     * Tests that visitAnyAttribute can be called with the same parameters repeatedly
     * without accumulating any state or causing issues.
     */
    @Test
    public void testVisitAnyAttribute_repeatedCallsWithSameParameters_noStateAccumulation() {
        // Act - call multiple times with same parameters
        for (int i = 0; i < 10; i++) {
            filter.visitAnyAttribute(clazz, attribute);
        }

        // Assert - verify no interactions occurred despite multiple calls
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
        verifyNoInteractions(attributeVisitor);
        verifyNoInteractions(otherAttributeVisitor);
    }

    /**
     * Tests that visitAnyAttribute is thread-safe and can be called concurrently.
     * Multiple threads should be able to call this no-op method without issues.
     */
    @Test
    public void testVisitAnyAttribute_threadSafe() throws InterruptedException {
        // Arrange
        final int threadCount = 10;
        final int callsPerThread = 100;
        Thread[] threads = new Thread[threadCount];

        // Act - create instances in multiple threads
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < callsPerThread; j++) {
                    filter.visitAnyAttribute(clazz, attribute);
                }
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert - verify no side effects occurred
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
        verifyNoInteractions(attributeVisitor);
        verifyNoInteractions(otherAttributeVisitor);
    }

    /**
     * Tests that visitAnyAttribute returns immediately (is truly a no-op).
     * The method should not perform any computation or I/O.
     */
    @Test
    public void testVisitAnyAttribute_returnsImmediately() {
        // Arrange - measure execution time for a single call
        long startTime = System.nanoTime();

        // Act
        filter.visitAnyAttribute(clazz, attribute);

        long endTime = System.nanoTime();
        long durationNanos = endTime - startTime;

        // Assert - should complete in less than 1 millisecond (1,000,000 nanoseconds)
        // This is a very generous threshold for a no-op method
        assertTrue(durationNanos < 1_000_000,
                "visitAnyAttribute should return immediately for a no-op method");
    }

    /**
     * Tests that visitAnyAttribute can be called after filter has been used for other operations.
     * The no-op should work regardless of the filter's usage history.
     */
    @Test
    public void testVisitAnyAttribute_afterOtherOperations_doesNotThrowException() {
        // Arrange - call visitAnyAttribute first
        filter.visitAnyAttribute(clazz, attribute);

        // Act & Assert - should work fine after previous calls
        assertDoesNotThrow(() -> filter.visitAnyAttribute(clazz, attribute));
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
        verifyNoInteractions(attributeVisitor);
        verifyNoInteractions(otherAttributeVisitor);
    }

    /**
     * Tests that visitAnyAttribute behavior is consistent across multiple filter instances.
     * All filters should have identical no-op behavior.
     */
    @Test
    public void testVisitAnyAttribute_consistentAcrossInstances() {
        // Arrange
        OptimizationCodeAttributeFilter filter1 = new OptimizationCodeAttributeFilter(attributeVisitor, otherAttributeVisitor);
        OptimizationCodeAttributeFilter filter2 = new OptimizationCodeAttributeFilter(attributeVisitor, otherAttributeVisitor);
        OptimizationCodeAttributeFilter filter3 = new OptimizationCodeAttributeFilter(attributeVisitor, otherAttributeVisitor);

        // Act - call on all instances
        filter1.visitAnyAttribute(clazz, attribute);
        filter2.visitAnyAttribute(clazz, attribute);
        filter3.visitAnyAttribute(clazz, attribute);

        // Assert - all should have no interactions
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
        verifyNoInteractions(attributeVisitor);
        verifyNoInteractions(otherAttributeVisitor);
    }

    /**
     * Tests that visitAnyAttribute can be called on a filter with null visitors.
     * The no-op should work regardless of constructor parameters.
     */
    @Test
    public void testVisitAnyAttribute_withNullVisitors_doesNotThrowException() {
        // Arrange - create filter with null visitors
        OptimizationCodeAttributeFilter filterWithNullVisitors =
                new OptimizationCodeAttributeFilter(null, null);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> filterWithNullVisitors.visitAnyAttribute(clazz, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called on a filter with only first visitor set.
     * The no-op should work regardless of which visitors are null.
     */
    @Test
    public void testVisitAnyAttribute_withOnlyFirstVisitor_doesNotThrowException() {
        // Arrange - create filter with only first visitor
        OptimizationCodeAttributeFilter filterWithFirstVisitor =
                new OptimizationCodeAttributeFilter(attributeVisitor, null);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> filterWithFirstVisitor.visitAnyAttribute(clazz, attribute));

        // Verify first visitor was not called
        verifyNoInteractions(attributeVisitor);
    }

    /**
     * Tests that visitAnyAttribute can be called on a filter with only second visitor set.
     * The no-op should work regardless of which visitors are null.
     */
    @Test
    public void testVisitAnyAttribute_withOnlySecondVisitor_doesNotThrowException() {
        // Arrange - create filter with only second visitor
        OptimizationCodeAttributeFilter filterWithSecondVisitor =
                new OptimizationCodeAttributeFilter(null, otherAttributeVisitor);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> filterWithSecondVisitor.visitAnyAttribute(clazz, attribute));

        // Verify second visitor was not called
        verifyNoInteractions(otherAttributeVisitor);
    }

    /**
     * Tests that visitAnyAttribute is truly a no-op by verifying it doesn't delegate
     * to any visitors even when they are provided.
     */
    @Test
    public void testVisitAnyAttribute_neverDelegates_regardlessOfVisitorConfiguration() {
        // Arrange - create filters with various visitor configurations
        OptimizationCodeAttributeFilter filter1 = new OptimizationCodeAttributeFilter(attributeVisitor);
        OptimizationCodeAttributeFilter filter2 = new OptimizationCodeAttributeFilter(attributeVisitor, otherAttributeVisitor);
        OptimizationCodeAttributeFilter filter3 = new OptimizationCodeAttributeFilter(null, otherAttributeVisitor);

        // Act - call visitAnyAttribute on all filters
        filter1.visitAnyAttribute(clazz, attribute);
        filter2.visitAnyAttribute(clazz, attribute);
        filter3.visitAnyAttribute(clazz, attribute);

        // Assert - verify no visitor was ever called
        verifyNoInteractions(attributeVisitor);
        verifyNoInteractions(otherAttributeVisitor);
    }
}
