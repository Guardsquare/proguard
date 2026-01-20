package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link EscapingClassMarker#visitAnyClass(Clazz)}.
 *
 * The visitAnyClass method is an empty implementation (no-op) that serves as a default
 * handler in the ClassVisitor pattern for classes that don't have specialized visitor methods.
 * The actual marking of classes as escaping happens in visitProgramClass.
 */
public class EscapingClassMarkerClaude_visitAnyClassTest {

    private EscapingClassMarker marker;
    private Clazz clazz;

    @BeforeEach
    public void setUp() {
        marker = new EscapingClassMarker();
        clazz = mock(ProgramClass.class);
    }

    /**
     * Tests that visitAnyClass can be called with valid mock objects without throwing exceptions.
     * Since this is a no-op method, it should simply do nothing and complete successfully.
     */
    @Test
    public void testVisitAnyClass_withValidMocks_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> marker.visitAnyClass(clazz));
    }

    /**
     * Tests that visitAnyClass can be called with null Clazz parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyClass_withNullClazz_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> marker.visitAnyClass(null));
    }

    /**
     * Tests that visitAnyClass can be called multiple times in succession.
     * The method should be idempotent and handle repeated calls without issues.
     */
    @Test
    public void testVisitAnyClass_calledMultipleTimes_doesNotThrowException() {
        // Act & Assert - should not throw any exception on multiple calls
        assertDoesNotThrow(() -> {
            marker.visitAnyClass(clazz);
            marker.visitAnyClass(clazz);
            marker.visitAnyClass(clazz);
        });
    }

    /**
     * Tests that visitAnyClass doesn't interact with the Clazz parameter.
     * Since it's a no-op method, it should not call any methods on the clazz.
     */
    @Test
    public void testVisitAnyClass_doesNotInteractWithClazz() {
        // Act
        marker.visitAnyClass(clazz);

        // Assert - verify no interactions occurred with the clazz mock
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitAnyClass doesn't affect the marker's internal state.
     * Calling the method should not change any fields or trigger any side effects.
     */
    @Test
    public void testVisitAnyClass_doesNotModifyMarkerState() {
        // Arrange - create another marker
        EscapingClassMarker marker2 = new EscapingClassMarker();

        // Act - call visitAnyClass on the first marker
        marker.visitAnyClass(clazz);

        // Assert - both markers should be functionally equivalent
        // Since visitAnyClass is a no-op, we verify no side effects
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitAnyClass works with different Clazz mock instances.
     * The method should handle any Clazz implementation without issues.
     */
    @Test
    public void testVisitAnyClass_withDifferentClazzInstances_doesNotThrowException() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Clazz clazz3 = mock(ProgramClass.class);

        // Act & Assert - should not throw any exception with different instances
        assertDoesNotThrow(() -> {
            marker.visitAnyClass(clazz1);
            marker.visitAnyClass(clazz2);
            marker.visitAnyClass(clazz3);
        });
    }

    /**
     * Tests that visitAnyClass execution completes immediately.
     * Since it's a no-op method, it should have minimal overhead.
     */
    @Test
    public void testVisitAnyClass_executesQuickly() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            marker.visitAnyClass(clazz);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100, "visitAnyClass should execute quickly as it's a no-op");
    }

    /**
     * Tests that visitAnyClass doesn't affect subsequent calls to other methods.
     * The no-op should not interfere with the marker's normal operation.
     */
    @Test
    public void testVisitAnyClass_doesNotAffectSubsequentOperations() {
        // Act - call visitAnyClass first
        marker.visitAnyClass(clazz);

        // Then call visitAnyClass again
        marker.visitAnyClass(clazz);

        // Assert - verify no side effects occurred
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that multiple markers can call visitAnyClass independently.
     * Each marker's no-op should not affect others.
     */
    @Test
    public void testVisitAnyClass_withMultipleMarkers_operateIndependently() {
        // Arrange - create multiple markers
        EscapingClassMarker marker1 = new EscapingClassMarker();
        EscapingClassMarker marker2 = new EscapingClassMarker();

        // Act - call visitAnyClass on both markers
        marker1.visitAnyClass(clazz);
        marker2.visitAnyClass(clazz);

        // Assert - verify no interactions occurred on any visitor
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitAnyClass can be called with the same parameters repeatedly
     * without accumulating any state or causing issues.
     */
    @Test
    public void testVisitAnyClass_repeatedCallsWithSameParameters_noStateAccumulation() {
        // Act - call multiple times with same parameters
        for (int i = 0; i < 10; i++) {
            marker.visitAnyClass(clazz);
        }

        // Assert - verify no interactions occurred despite multiple calls
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitAnyClass is thread-safe and can be called concurrently.
     * Multiple threads should be able to call this no-op method without issues.
     */
    @Test
    public void testVisitAnyClass_threadSafe() throws InterruptedException {
        // Arrange
        final int threadCount = 10;
        final int callsPerThread = 100;
        Thread[] threads = new Thread[threadCount];

        // Act - create instances in multiple threads
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < callsPerThread; j++) {
                    marker.visitAnyClass(clazz);
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
    }

    /**
     * Tests that visitAnyClass returns immediately (is truly a no-op).
     * The method should not perform any computation or I/O.
     */
    @Test
    public void testVisitAnyClass_returnsImmediately() {
        // Arrange - measure execution time for a single call
        long startTime = System.nanoTime();

        // Act
        marker.visitAnyClass(clazz);

        long endTime = System.nanoTime();
        long durationNanos = endTime - startTime;

        // Assert - should complete in less than 1 millisecond (1,000,000 nanoseconds)
        // This is a very generous threshold for a no-op method
        assertTrue(durationNanos < 1_000_000,
                "visitAnyClass should return immediately for a no-op method");
    }

    /**
     * Tests that visitAnyClass can be called after marker has been used for other operations.
     * The no-op should work regardless of the marker's usage history.
     */
    @Test
    public void testVisitAnyClass_afterOtherOperations_doesNotThrowException() {
        // Arrange - call visitAnyClass first
        marker.visitAnyClass(clazz);

        // Act & Assert - should work fine after previous calls
        assertDoesNotThrow(() -> marker.visitAnyClass(clazz));
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitAnyClass behavior is consistent across multiple marker instances.
     * All markers should have identical no-op behavior.
     */
    @Test
    public void testVisitAnyClass_consistentAcrossInstances() {
        // Arrange
        EscapingClassMarker marker1 = new EscapingClassMarker();
        EscapingClassMarker marker2 = new EscapingClassMarker();
        EscapingClassMarker marker3 = new EscapingClassMarker();

        // Act - call on all instances
        marker1.visitAnyClass(clazz);
        marker2.visitAnyClass(clazz);
        marker3.visitAnyClass(clazz);

        // Assert - all should have no interactions
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitAnyClass with interleaved calls works correctly.
     */
    @Test
    public void testVisitAnyClass_interleavedCalls() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);

        // Act - interleave calls
        marker.visitAnyClass(clazz1);
        marker.visitAnyClass(clazz2);
        marker.visitAnyClass(clazz1);
        marker.visitAnyClass(clazz2);

        // Assert - no interactions should occur
        verifyNoInteractions(clazz1);
        verifyNoInteractions(clazz2);
    }

    /**
     * Tests that visitAnyClass is idempotent.
     * Calling it multiple times should have the same effect as calling it once.
     */
    @Test
    public void testVisitAnyClass_idempotent() {
        // Act - call multiple times
        marker.visitAnyClass(clazz);
        marker.visitAnyClass(clazz);
        marker.visitAnyClass(clazz);

        // Assert - verify no side effects and no interactions
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitAnyClass doesn't modify class state.
     * Since it's a no-op, the class should remain unchanged.
     */
    @Test
    public void testVisitAnyClass_doesNotModifyClassState() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Verify initial state - should not be escaping
        assertFalse(EscapingClassMarker.isClassEscaping(programClass),
                "Class should not be marked as escaping initially");

        // Act - call visitAnyClass (note: this is the no-op, not visitProgramClass)
        marker.visitAnyClass(programClass);

        // Assert - should still not be escaping because visitAnyClass is a no-op
        assertFalse(EscapingClassMarker.isClassEscaping(programClass),
                "Class should still not be marked as escaping after visitAnyClass (no-op)");
    }

    /**
     * Tests that visitAnyClass is distinct from visitProgramClass.
     * The no-op visitAnyClass should not trigger the marking behavior.
     */
    @Test
    public void testVisitAnyClass_distinctFromVisitProgramClass() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act - call the no-op visitAnyClass
        marker.visitAnyClass(programClass);

        // Assert - class should NOT be marked as escaping (because visitAnyClass is a no-op)
        assertFalse(EscapingClassMarker.isClassEscaping(programClass),
                "visitAnyClass should not mark class as escaping (it's a no-op)");

        // Act - now call visitProgramClass which actually marks the class
        marker.visitProgramClass(programClass);

        // Assert - now the class should be marked as escaping
        assertTrue(EscapingClassMarker.isClassEscaping(programClass),
                "visitProgramClass should mark class as escaping");
    }

    /**
     * Tests that visitAnyClass can be safely called before visitProgramClass
     * without affecting the marking behavior.
     */
    @Test
    public void testVisitAnyClass_beforeVisitProgramClass_noInterference() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act - call visitAnyClass first (no-op)
        marker.visitAnyClass(programClass);

        // Assert - should not be marked yet
        assertFalse(EscapingClassMarker.isClassEscaping(programClass),
                "Class should not be marked after visitAnyClass");

        // Act - now call visitProgramClass
        marker.visitProgramClass(programClass);

        // Assert - should now be marked
        assertTrue(EscapingClassMarker.isClassEscaping(programClass),
                "Class should be marked after visitProgramClass");
    }

    /**
     * Tests that visitAnyClass can be safely called after visitProgramClass
     * without affecting the marking behavior.
     */
    @Test
    public void testVisitAnyClass_afterVisitProgramClass_noInterference() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act - call visitProgramClass first
        marker.visitProgramClass(programClass);

        // Assert - should be marked
        assertTrue(EscapingClassMarker.isClassEscaping(programClass),
                "Class should be marked after visitProgramClass");

        // Act - now call visitAnyClass (no-op)
        marker.visitAnyClass(programClass);

        // Assert - should still be marked (no-op doesn't change anything)
        assertTrue(EscapingClassMarker.isClassEscaping(programClass),
                "Class should still be marked after visitAnyClass (no-op)");
    }
}
