package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.Attribute;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ParameterUsageMarker#visitAnyAttribute(Clazz, Attribute)}.
 *
 * The visitAnyAttribute method is an empty implementation (no-op) that serves as a default
 * handler in the AttributeVisitor pattern for attributes that don't have specialized visitor methods.
 * The actual parameter usage analysis work is done in visitCodeAttribute.
 */
public class ParameterUsageMarkerClaude_visitAnyAttributeTest {

    private ParameterUsageMarker marker;
    private Clazz clazz;
    private Attribute attribute;

    @BeforeEach
    public void setUp() {
        marker = new ParameterUsageMarker();
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
        assertDoesNotThrow(() -> marker.visitAnyAttribute(clazz, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called with null Clazz parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyAttribute_withNullClazz_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> marker.visitAnyAttribute(null, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called with null Attribute parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyAttribute_withNullAttribute_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> marker.visitAnyAttribute(clazz, null));
    }

    /**
     * Tests that visitAnyAttribute can be called with both parameters null.
     * The method should handle null parameters gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyAttribute_withBothParametersNull_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> marker.visitAnyAttribute(null, null));
    }

    /**
     * Tests that visitAnyAttribute can be called multiple times in succession.
     * The method should be idempotent and handle repeated calls without issues.
     */
    @Test
    public void testVisitAnyAttribute_calledMultipleTimes_doesNotThrowException() {
        // Act & Assert - should not throw any exception on multiple calls
        assertDoesNotThrow(() -> {
            marker.visitAnyAttribute(clazz, attribute);
            marker.visitAnyAttribute(clazz, attribute);
            marker.visitAnyAttribute(clazz, attribute);
        });
    }

    /**
     * Tests that visitAnyAttribute doesn't interact with the Clazz parameter.
     * Since it's a no-op method, it should not call any methods on the clazz.
     */
    @Test
    public void testVisitAnyAttribute_doesNotInteractWithClazz() {
        // Act
        marker.visitAnyAttribute(clazz, attribute);

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
        marker.visitAnyAttribute(clazz, attribute);

        // Assert - verify no interactions occurred with the attribute mock
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute doesn't affect the marker's internal state.
     * Calling the method should not change any fields or trigger any side effects.
     */
    @Test
    public void testVisitAnyAttribute_doesNotModifyMarkerState() {
        // Arrange - create another marker
        ParameterUsageMarker marker2 = new ParameterUsageMarker();

        // Act - call visitAnyAttribute on the first marker
        marker.visitAnyAttribute(clazz, attribute);

        // Assert - both markers should be functionally equivalent
        // Since visitAnyAttribute is a no-op, we verify no side effects
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
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
            marker.visitAnyAttribute(clazz1, attribute);
            marker.visitAnyAttribute(clazz2, attribute);
            marker.visitAnyAttribute(clazz3, attribute);
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
            marker.visitAnyAttribute(clazz, attr1);
            marker.visitAnyAttribute(clazz, attr2);
            marker.visitAnyAttribute(clazz, attr3);
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
            marker.visitAnyAttribute(clazz, attribute);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100, "visitAnyAttribute should execute quickly as it's a no-op");
    }

    /**
     * Tests that visitAnyAttribute doesn't affect subsequent calls to other methods.
     * The no-op should not interfere with the marker's normal operation.
     */
    @Test
    public void testVisitAnyAttribute_doesNotAffectSubsequentOperations() {
        // Act - call visitAnyAttribute first
        marker.visitAnyAttribute(clazz, attribute);

        // Then call visitAnyAttribute again
        marker.visitAnyAttribute(clazz, attribute);

        // Assert - verify no side effects occurred
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
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
            marker.visitAnyAttribute(clazz, attr1);
            marker.visitAnyAttribute(clazz, attr2);
            marker.visitAnyAttribute(clazz, attr3);
        });

        // Verify that the attribute methods were not called since it's a no-op
        verify(attr1, never()).getAttributeName(any());
        verify(attr2, never()).getAttributeName(any());
        verify(attr3, never()).getAttributeName(any());
    }

    /**
     * Tests that multiple markers can call visitAnyAttribute independently.
     * Each marker's no-op should not affect others.
     */
    @Test
    public void testVisitAnyAttribute_withMultipleMarkers_operateIndependently() {
        // Arrange - create multiple markers
        ParameterUsageMarker marker1 = new ParameterUsageMarker();
        ParameterUsageMarker marker2 = new ParameterUsageMarker();

        // Act - call visitAnyAttribute on both markers
        marker1.visitAnyAttribute(clazz, attribute);
        marker2.visitAnyAttribute(clazz, attribute);

        // Assert - verify no interactions occurred on any visitor
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute can be called with the same parameters repeatedly
     * without accumulating any state or causing issues.
     */
    @Test
    public void testVisitAnyAttribute_repeatedCallsWithSameParameters_noStateAccumulation() {
        // Act - call multiple times with same parameters
        for (int i = 0; i < 10; i++) {
            marker.visitAnyAttribute(clazz, attribute);
        }

        // Assert - verify no interactions occurred despite multiple calls
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
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
                    marker.visitAnyAttribute(clazz, attribute);
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
        marker.visitAnyAttribute(clazz, attribute);

        long endTime = System.nanoTime();
        long durationNanos = endTime - startTime;

        // Assert - should complete in less than 1 millisecond (1,000,000 nanoseconds)
        // This is a very generous threshold for a no-op method
        assertTrue(durationNanos < 1_000_000,
                "visitAnyAttribute should return immediately for a no-op method");
    }

    /**
     * Tests that visitAnyAttribute can be called after marker has been used for other operations.
     * The no-op should work regardless of the marker's usage history.
     */
    @Test
    public void testVisitAnyAttribute_afterOtherOperations_doesNotThrowException() {
        // Arrange - call visitAnyAttribute first
        marker.visitAnyAttribute(clazz, attribute);

        // Act & Assert - should work fine after previous calls
        assertDoesNotThrow(() -> marker.visitAnyAttribute(clazz, attribute));
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute behavior is consistent across multiple marker instances.
     * All markers should have identical no-op behavior.
     */
    @Test
    public void testVisitAnyAttribute_consistentAcrossInstances() {
        // Arrange
        ParameterUsageMarker marker1 = new ParameterUsageMarker();
        ParameterUsageMarker marker2 = new ParameterUsageMarker();
        ParameterUsageMarker marker3 = new ParameterUsageMarker();

        // Act - call on all instances
        marker1.visitAnyAttribute(clazz, attribute);
        marker2.visitAnyAttribute(clazz, attribute);
        marker3.visitAnyAttribute(clazz, attribute);

        // Assert - all should have no interactions
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute works correctly with markers created with different constructor parameters.
     * The no-op behavior should be consistent regardless of how the marker is configured.
     */
    @Test
    public void testVisitAnyAttribute_withDifferentConstructorParameters_doesNotThrowException() {
        // Arrange - create markers with different constructor parameters
        ParameterUsageMarker marker1 = new ParameterUsageMarker();
        ParameterUsageMarker marker2 = new ParameterUsageMarker(true, false);
        ParameterUsageMarker marker3 = new ParameterUsageMarker(false, true);
        ParameterUsageMarker marker4 = new ParameterUsageMarker(true, true);
        ParameterUsageMarker marker5 = new ParameterUsageMarker(false, false, false);

        // Act & Assert - all should handle visitAnyAttribute without throwing
        assertDoesNotThrow(() -> {
            marker1.visitAnyAttribute(clazz, attribute);
            marker2.visitAnyAttribute(clazz, attribute);
            marker3.visitAnyAttribute(clazz, attribute);
            marker4.visitAnyAttribute(clazz, attribute);
            marker5.visitAnyAttribute(clazz, attribute);
        });

        // Verify no interactions occurred with any marker
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute doesn't modify or access any marker configuration.
     * The method should be completely independent of constructor parameters.
     */
    @Test
    public void testVisitAnyAttribute_doesNotDependOnMarkerConfiguration() {
        // Arrange - create markers with all possible configurations
        ParameterUsageMarker markerDefault = new ParameterUsageMarker();
        ParameterUsageMarker markerMarkThis = new ParameterUsageMarker(true, false);
        ParameterUsageMarker markerMarkAll = new ParameterUsageMarker(false, true);
        ParameterUsageMarker markerMarkBoth = new ParameterUsageMarker(true, true);
        ParameterUsageMarker markerNoAnalyze = new ParameterUsageMarker(false, false, false);

        // Act - call visitAnyAttribute on all markers
        markerDefault.visitAnyAttribute(clazz, attribute);
        markerMarkThis.visitAnyAttribute(clazz, attribute);
        markerMarkAll.visitAnyAttribute(clazz, attribute);
        markerMarkBoth.visitAnyAttribute(clazz, attribute);
        markerNoAnalyze.visitAnyAttribute(clazz, attribute);

        // Assert - verify no interactions regardless of configuration
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
    }
}
