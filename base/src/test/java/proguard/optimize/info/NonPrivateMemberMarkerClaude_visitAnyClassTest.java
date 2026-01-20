package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link NonPrivateMemberMarker#visitAnyClass(Clazz)}.
 *
 * The visitAnyClass method is an empty implementation (no-op) that serves as a default
 * handler in the ClassVisitor pattern for classes that don't have specialized visitor methods.
 * The actual marking of non-private members happens in visitProgramClass.
 */
public class NonPrivateMemberMarkerClaude_visitAnyClassTest {

    private NonPrivateMemberMarker marker;
    private Clazz clazz;

    @BeforeEach
    public void setUp() {
        marker = new NonPrivateMemberMarker();
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
        NonPrivateMemberMarker marker2 = new NonPrivateMemberMarker();

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
        NonPrivateMemberMarker marker1 = new NonPrivateMemberMarker();
        NonPrivateMemberMarker marker2 = new NonPrivateMemberMarker();

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
        NonPrivateMemberMarker marker1 = new NonPrivateMemberMarker();
        NonPrivateMemberMarker marker2 = new NonPrivateMemberMarker();
        NonPrivateMemberMarker marker3 = new NonPrivateMemberMarker();

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
     * Tests that visitAnyClass is distinct from visitProgramClass.
     * The no-op visitAnyClass should not trigger any marking behavior.
     */
    @Test
    public void testVisitAnyClass_distinctFromVisitProgramClass() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act - call the no-op visitAnyClass (should do nothing)
        marker.visitAnyClass(programClass);

        // visitAnyClass is a no-op, so we just verify it doesn't throw
        // The actual marking happens in visitProgramClass, not visitAnyClass
        assertDoesNotThrow(() -> marker.visitAnyClass(programClass),
                "visitAnyClass should not throw as it's a no-op");
    }

    /**
     * Tests that visitAnyClass can be safely called before visitProgramClass
     * without affecting the marking behavior.
     */
    @Test
    public void testVisitAnyClass_beforeVisitProgramClass_noInterference() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act - call visitAnyClass first (no-op)
        marker.visitAnyClass(programClass);

        // Act - now call visitProgramClass (should work normally)
        assertDoesNotThrow(() -> marker.visitProgramClass(programClass),
                "visitProgramClass should work normally after visitAnyClass");
    }

    /**
     * Tests that visitAnyClass can be safely called after visitProgramClass
     * without affecting the marking behavior.
     */
    @Test
    public void testVisitAnyClass_afterVisitProgramClass_noInterference() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act - call visitProgramClass first (performs actual marking)
        assertDoesNotThrow(() -> marker.visitProgramClass(programClass));

        // Act - now call visitAnyClass (no-op, shouldn't affect anything)
        assertDoesNotThrow(() -> marker.visitAnyClass(programClass),
                "visitAnyClass should work after visitProgramClass");
    }

    /**
     * Tests that visitAnyClass doesn't call any ConstantVisitor methods.
     * The no-op should not trigger constant pool processing.
     */
    @Test
    public void testVisitAnyClass_doesNotProcessConstantPool() {
        // Arrange
        ProgramClass programClass = mock(ProgramClass.class);

        // Act
        marker.visitAnyClass(programClass);

        // Assert - verify no constant pool methods were called
        verify(programClass, never()).constantPoolEntriesAccept(any());
    }

    /**
     * Tests that visitAnyClass doesn't call any MemberVisitor methods.
     * The no-op should not process methods or fields.
     */
    @Test
    public void testVisitAnyClass_doesNotProcessMembers() {
        // Arrange
        ProgramClass programClass = mock(ProgramClass.class);

        // Act
        marker.visitAnyClass(programClass);

        // Assert - verify no member processing methods were called
        verify(programClass, never()).methodAccept(anyString(), anyString(), any());
        verify(programClass, never()).methodsAccept(any());
        verify(programClass, never()).fieldsAccept(any());
    }
}
