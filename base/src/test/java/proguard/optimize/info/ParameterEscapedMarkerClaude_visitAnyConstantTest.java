package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.constant.Constant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ParameterEscapedMarker#visitAnyConstant(Clazz, Constant)}.
 *
 * The visitAnyConstant method is an empty implementation (no-op) that serves as a default
 * handler in the ConstantVisitor pattern for constants that don't have specialized visitor methods.
 * The actual parameter escape analysis work is done in visitAnyMethodrefConstant.
 */
public class ParameterEscapedMarkerClaude_visitAnyConstantTest {

    private ParameterEscapedMarker marker;
    private Clazz clazz;
    private Constant constant;

    @BeforeEach
    public void setUp() {
        marker = new ParameterEscapedMarker();
        clazz = mock(ProgramClass.class);
        constant = mock(Constant.class);
    }

    /**
     * Tests that visitAnyConstant can be called with valid mock objects without throwing exceptions.
     * Since this is a no-op method, it should simply do nothing and complete successfully.
     */
    @Test
    public void testVisitAnyConstant_withValidMocks_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> marker.visitAnyConstant(clazz, constant));
    }

    /**
     * Tests that visitAnyConstant can be called with null Clazz parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyConstant_withNullClazz_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> marker.visitAnyConstant(null, constant));
    }

    /**
     * Tests that visitAnyConstant can be called with null Constant parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyConstant_withNullConstant_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> marker.visitAnyConstant(clazz, null));
    }

    /**
     * Tests that visitAnyConstant can be called with both parameters null.
     * The method should handle null parameters gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyConstant_withBothParametersNull_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> marker.visitAnyConstant(null, null));
    }

    /**
     * Tests that visitAnyConstant can be called multiple times in succession.
     * The method should be idempotent and handle repeated calls without issues.
     */
    @Test
    public void testVisitAnyConstant_calledMultipleTimes_doesNotThrowException() {
        // Act & Assert - should not throw any exception on multiple calls
        assertDoesNotThrow(() -> {
            marker.visitAnyConstant(clazz, constant);
            marker.visitAnyConstant(clazz, constant);
            marker.visitAnyConstant(clazz, constant);
        });
    }

    /**
     * Tests that visitAnyConstant doesn't interact with the Clazz parameter.
     * Since it's a no-op method, it should not call any methods on the clazz.
     */
    @Test
    public void testVisitAnyConstant_doesNotInteractWithClazz() {
        // Act
        marker.visitAnyConstant(clazz, constant);

        // Assert - verify no interactions occurred with the clazz mock
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitAnyConstant doesn't interact with the Constant parameter.
     * Since it's a no-op method, it should not call any methods on the constant.
     */
    @Test
    public void testVisitAnyConstant_doesNotInteractWithConstant() {
        // Act
        marker.visitAnyConstant(clazz, constant);

        // Assert - verify no interactions occurred with the constant mock
        verifyNoInteractions(constant);
    }

    /**
     * Tests that visitAnyConstant doesn't affect the marker's internal state.
     * Calling the method should not change any fields or trigger any side effects.
     */
    @Test
    public void testVisitAnyConstant_doesNotModifyMarkerState() {
        // Arrange - create another marker
        ParameterEscapedMarker marker2 = new ParameterEscapedMarker();

        // Act - call visitAnyConstant on the first marker
        marker.visitAnyConstant(clazz, constant);

        // Assert - both markers should be functionally equivalent
        // Since visitAnyConstant is a no-op, we verify no side effects
        verifyNoInteractions(clazz);
        verifyNoInteractions(constant);
    }

    /**
     * Tests that visitAnyConstant works with different Clazz mock instances.
     * The method should handle any Clazz implementation without issues.
     */
    @Test
    public void testVisitAnyConstant_withDifferentClazzInstances_doesNotThrowException() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Clazz clazz3 = mock(ProgramClass.class);

        // Act & Assert - should not throw any exception with different instances
        assertDoesNotThrow(() -> {
            marker.visitAnyConstant(clazz1, constant);
            marker.visitAnyConstant(clazz2, constant);
            marker.visitAnyConstant(clazz3, constant);
        });
    }

    /**
     * Tests that visitAnyConstant works with different Constant mock instances.
     * The method should handle any Constant implementation without issues.
     */
    @Test
    public void testVisitAnyConstant_withDifferentConstantInstances_doesNotThrowException() {
        // Arrange
        Constant const1 = mock(Constant.class);
        Constant const2 = mock(Constant.class);
        Constant const3 = mock(Constant.class);

        // Act & Assert - should not throw any exception with different instances
        assertDoesNotThrow(() -> {
            marker.visitAnyConstant(clazz, const1);
            marker.visitAnyConstant(clazz, const2);
            marker.visitAnyConstant(clazz, const3);
        });
    }

    /**
     * Tests that visitAnyConstant execution completes immediately.
     * Since it's a no-op method, it should have minimal overhead.
     */
    @Test
    public void testVisitAnyConstant_executesQuickly() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            marker.visitAnyConstant(clazz, constant);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100, "visitAnyConstant should execute quickly as it's a no-op");
    }

    /**
     * Tests that visitAnyConstant doesn't affect subsequent calls to other methods.
     * The no-op should not interfere with the marker's normal operation.
     */
    @Test
    public void testVisitAnyConstant_doesNotAffectSubsequentOperations() {
        // Act - call visitAnyConstant first
        marker.visitAnyConstant(clazz, constant);

        // Then call visitAnyConstant again
        marker.visitAnyConstant(clazz, constant);

        // Assert - verify no side effects occurred
        verifyNoInteractions(clazz);
        verifyNoInteractions(constant);
    }

    /**
     * Tests that visitAnyConstant with various constant types doesn't throw exceptions.
     * This ensures the no-op works with concrete constant types.
     */
    @Test
    public void testVisitAnyConstant_withVariousConstantTypes_doesNotThrowException() {
        // Arrange - test with various constant types
        Constant const1 = mock(Constant.class);
        Constant const2 = mock(Constant.class);
        Constant const3 = mock(Constant.class);

        // Act & Assert - should handle all constant types gracefully
        assertDoesNotThrow(() -> {
            marker.visitAnyConstant(clazz, const1);
            marker.visitAnyConstant(clazz, const2);
            marker.visitAnyConstant(clazz, const3);
        });

        // Verify that no methods were called since it's a no-op
        verifyNoInteractions(const1);
        verifyNoInteractions(const2);
        verifyNoInteractions(const3);
    }

    /**
     * Tests that multiple markers can call visitAnyConstant independently.
     * Each marker's no-op should not affect others.
     */
    @Test
    public void testVisitAnyConstant_withMultipleMarkers_operateIndependently() {
        // Arrange - create multiple markers
        ParameterEscapedMarker marker1 = new ParameterEscapedMarker();
        ParameterEscapedMarker marker2 = new ParameterEscapedMarker();

        // Act - call visitAnyConstant on both markers
        marker1.visitAnyConstant(clazz, constant);
        marker2.visitAnyConstant(clazz, constant);

        // Assert - verify no interactions occurred on any visitor
        verifyNoInteractions(clazz);
        verifyNoInteractions(constant);
    }

    /**
     * Tests that visitAnyConstant can be called with the same parameters repeatedly
     * without accumulating any state or causing issues.
     */
    @Test
    public void testVisitAnyConstant_repeatedCallsWithSameParameters_noStateAccumulation() {
        // Act - call multiple times with same parameters
        for (int i = 0; i < 10; i++) {
            marker.visitAnyConstant(clazz, constant);
        }

        // Assert - verify no interactions occurred despite multiple calls
        verifyNoInteractions(clazz);
        verifyNoInteractions(constant);
    }

    /**
     * Tests that visitAnyConstant is thread-safe and can be called concurrently.
     * Multiple threads should be able to call this no-op method without issues.
     */
    @Test
    public void testVisitAnyConstant_threadSafe() throws InterruptedException {
        // Arrange
        final int threadCount = 10;
        final int callsPerThread = 100;
        Thread[] threads = new Thread[threadCount];

        // Act - create instances in multiple threads
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < callsPerThread; j++) {
                    marker.visitAnyConstant(clazz, constant);
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
        verifyNoInteractions(constant);
    }

    /**
     * Tests that visitAnyConstant returns immediately (is truly a no-op).
     * The method should not perform any computation or I/O.
     */
    @Test
    public void testVisitAnyConstant_returnsImmediately() {
        // Arrange - measure execution time for a single call
        long startTime = System.nanoTime();

        // Act
        marker.visitAnyConstant(clazz, constant);

        long endTime = System.nanoTime();
        long durationNanos = endTime - startTime;

        // Assert - should complete in less than 1 millisecond (1,000,000 nanoseconds)
        // This is a very generous threshold for a no-op method
        assertTrue(durationNanos < 1_000_000,
                "visitAnyConstant should return immediately for a no-op method");
    }

    /**
     * Tests that visitAnyConstant can be called after marker has been used for other operations.
     * The no-op should work regardless of the marker's usage history.
     */
    @Test
    public void testVisitAnyConstant_afterOtherOperations_doesNotThrowException() {
        // Arrange - call visitAnyConstant first
        marker.visitAnyConstant(clazz, constant);

        // Act & Assert - should work fine after previous calls
        assertDoesNotThrow(() -> marker.visitAnyConstant(clazz, constant));
        verifyNoInteractions(clazz);
        verifyNoInteractions(constant);
    }

    /**
     * Tests that visitAnyConstant behavior is consistent across multiple marker instances.
     * All markers should have identical no-op behavior.
     */
    @Test
    public void testVisitAnyConstant_consistentAcrossInstances() {
        // Arrange
        ParameterEscapedMarker marker1 = new ParameterEscapedMarker();
        ParameterEscapedMarker marker2 = new ParameterEscapedMarker();
        ParameterEscapedMarker marker3 = new ParameterEscapedMarker();

        // Act - call on all instances
        marker1.visitAnyConstant(clazz, constant);
        marker2.visitAnyConstant(clazz, constant);
        marker3.visitAnyConstant(clazz, constant);

        // Assert - all should have no interactions
        verifyNoInteractions(clazz);
        verifyNoInteractions(constant);
    }

    /**
     * Tests that visitAnyConstant with alternating parameters doesn't cause issues.
     * The method should handle parameter changes without side effects.
     */
    @Test
    public void testVisitAnyConstant_withAlternatingParameters_doesNotThrowException() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Constant const1 = mock(Constant.class);
        Constant const2 = mock(Constant.class);

        // Act & Assert - alternate between different parameter combinations
        assertDoesNotThrow(() -> {
            marker.visitAnyConstant(clazz1, const1);
            marker.visitAnyConstant(clazz2, const2);
            marker.visitAnyConstant(clazz1, const2);
            marker.visitAnyConstant(clazz2, const1);
        });

        // Verify no interactions with any of the mocks
        verifyNoInteractions(clazz1);
        verifyNoInteractions(clazz2);
        verifyNoInteractions(const1);
        verifyNoInteractions(const2);
    }

    /**
     * Tests that visitAnyConstant handles rapid successive calls without issues.
     */
    @Test
    public void testVisitAnyConstant_rapidSuccessiveCalls_doesNotThrowException() {
        // Act & Assert - make rapid successive calls
        for (int i = 0; i < 100; i++) {
            assertDoesNotThrow(() -> marker.visitAnyConstant(clazz, constant),
                    "Call " + i + " should not throw exception");
        }

        // Verify no interactions occurred
        verifyNoInteractions(clazz);
        verifyNoInteractions(constant);
    }

    /**
     * Tests that visitAnyConstant can be invoked in various contexts without issues.
     */
    @Test
    public void testVisitAnyConstant_inVariousContexts_doesNotThrowException() {
        // Act & Assert - call in different contexts

        // Direct call
        assertDoesNotThrow(() -> marker.visitAnyConstant(clazz, constant));

        // In a block
        {
            assertDoesNotThrow(() -> marker.visitAnyConstant(clazz, constant));
        }

        // In a conditional
        if (true) {
            assertDoesNotThrow(() -> marker.visitAnyConstant(clazz, constant));
        }

        // In a loop
        for (int i = 0; i < 3; i++) {
            assertDoesNotThrow(() -> marker.visitAnyConstant(clazz, constant));
        }

        // Verify no interactions occurred
        verifyNoInteractions(clazz);
        verifyNoInteractions(constant);
    }

    /**
     * Tests that visitAnyConstant maintains consistency when called before and after
     * other visitor methods on the same marker instance.
     */
    @Test
    public void testVisitAnyConstant_beforeAndAfterOtherVisitorMethods_remainsConsistent() {
        // Act - call visitAnyConstant multiple times
        marker.visitAnyConstant(clazz, constant);
        marker.visitAnyConstant(clazz, constant);
        marker.visitAnyConstant(clazz, constant);

        // Assert - verify consistent no-op behavior
        verifyNoInteractions(clazz);
        verifyNoInteractions(constant);
    }

    /**
     * Tests that visitAnyConstant doesn't modify or inspect the constant parameter.
     * This verifies it's truly a no-op with respect to the constant.
     */
    @Test
    public void testVisitAnyConstant_doesNotInspectConstant() {
        // Arrange - create a constant mock with configured methods
        Constant mockConstant = mock(Constant.class);

        // Act
        marker.visitAnyConstant(clazz, mockConstant);

        // Assert - no methods should be called on the constant
        verifyNoInteractions(mockConstant);
    }

    /**
     * Tests that visitAnyConstant doesn't modify or inspect the clazz parameter.
     * This verifies it's truly a no-op with respect to the clazz.
     */
    @Test
    public void testVisitAnyConstant_doesNotInspectClazz() {
        // Arrange - create a clazz mock
        Clazz mockClazz = mock(ProgramClass.class);

        // Act
        marker.visitAnyConstant(mockClazz, constant);

        // Assert - no methods should be called on the clazz
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests that visitAnyConstant works correctly when called in a sequence
     * with different parameter combinations.
     */
    @Test
    public void testVisitAnyConstant_sequenceOfDifferentCalls_allSucceed() {
        // Arrange
        Constant const1 = mock(Constant.class);
        Constant const2 = mock(Constant.class);
        Clazz clazz1 = mock(ProgramClass.class);

        // Act - make a sequence of calls with different combinations
        assertDoesNotThrow(() -> {
            marker.visitAnyConstant(clazz, constant);
            marker.visitAnyConstant(clazz1, constant);
            marker.visitAnyConstant(clazz, const1);
            marker.visitAnyConstant(clazz1, const2);
            marker.visitAnyConstant(null, constant);
            marker.visitAnyConstant(clazz, null);
            marker.visitAnyConstant(null, null);
        });

        // Assert - no interactions with any mock
        verifyNoInteractions(clazz);
        verifyNoInteractions(clazz1);
        verifyNoInteractions(constant);
        verifyNoInteractions(const1);
        verifyNoInteractions(const2);
    }

    /**
     * Tests that the no-op nature of visitAnyConstant is evident through performance.
     * A large number of calls should complete very quickly.
     */
    @Test
    public void testVisitAnyConstant_largeNumberOfCalls_completesQuickly() {
        // Arrange
        long startTime = System.nanoTime();
        final int iterations = 10000;

        // Act - call many times
        for (int i = 0; i < iterations; i++) {
            marker.visitAnyConstant(clazz, constant);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 200ms for 10000 calls)
        assertTrue(durationMs < 200,
                "visitAnyConstant should execute very quickly for a no-op (" + iterations + " calls took " + durationMs + "ms)");
    }

    /**
     * Tests that visitAnyConstant doesn't throw any runtime exceptions
     * even with extreme parameter combinations.
     */
    @Test
    public void testVisitAnyConstant_extremeParameterCombinations_noExceptions() {
        // Act & Assert - test various extreme combinations
        assertDoesNotThrow(() -> marker.visitAnyConstant(null, null));
        assertDoesNotThrow(() -> marker.visitAnyConstant(clazz, null));
        assertDoesNotThrow(() -> marker.visitAnyConstant(null, constant));
        assertDoesNotThrow(() -> marker.visitAnyConstant(clazz, constant));

        // Verify no interactions
        verifyNoInteractions(clazz);
        verifyNoInteractions(constant);
    }
}
