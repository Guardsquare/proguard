package proguard.optimize.peephole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.LibraryClass;
import proguard.classfile.constant.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link TargetClassChanger#visitAnyConstant(Clazz, Constant)}.
 *
 * The visitAnyConstant method is an empty implementation (no-op) that serves as a default
 * handler in the ConstantVisitor pattern. The TargetClassChanger only processes
 * specific constant types through specialized methods like visitStringConstant,
 * visitClassConstant, visitFieldrefConstant, etc.
 * This method provides the default no-op behavior for all other constant types.
 *
 * Note: Mocking is used here because visitAnyConstant is a no-op method with an empty body.
 * There is no meaningful behavior to test without mocking - we can only verify it doesn't throw
 * exceptions and doesn't interact with its parameters.
 */
public class TargetClassChangerClaude_visitAnyConstantTest {

    private TargetClassChanger changer;
    private Clazz clazz;
    private Constant constant;

    @BeforeEach
    public void setUp() {
        changer = new TargetClassChanger();
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
        assertDoesNotThrow(() -> changer.visitAnyConstant(clazz, constant));
    }

    /**
     * Tests that visitAnyConstant can be called with null Clazz parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyConstant_withNullClazz_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> changer.visitAnyConstant(null, constant));
    }

    /**
     * Tests that visitAnyConstant can be called with null Constant parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyConstant_withNullConstant_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> changer.visitAnyConstant(clazz, null));
    }

    /**
     * Tests that visitAnyConstant can be called with both parameters null.
     * The method should handle null parameters gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyConstant_withBothParametersNull_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> changer.visitAnyConstant(null, null));
    }

    /**
     * Tests that visitAnyConstant can be called multiple times in succession.
     * The method should be idempotent and handle repeated calls without issues.
     */
    @Test
    public void testVisitAnyConstant_calledMultipleTimes_doesNotThrowException() {
        // Act & Assert - should not throw any exception on multiple calls
        assertDoesNotThrow(() -> {
            changer.visitAnyConstant(clazz, constant);
            changer.visitAnyConstant(clazz, constant);
            changer.visitAnyConstant(clazz, constant);
        });
    }

    /**
     * Tests that visitAnyConstant doesn't interact with the Clazz parameter.
     * Since it's a no-op method, it should not call any methods on the clazz.
     */
    @Test
    public void testVisitAnyConstant_doesNotInteractWithClazz() {
        // Act
        changer.visitAnyConstant(clazz, constant);

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
        changer.visitAnyConstant(clazz, constant);

        // Assert - verify no interactions occurred with the constant mock
        verifyNoInteractions(constant);
    }

    /**
     * Tests that visitAnyConstant works with different Clazz implementations.
     */
    @Test
    public void testVisitAnyConstant_withDifferentClazzTypes_doesNotThrowException() {
        // Arrange
        Clazz programClass = mock(ProgramClass.class);
        Clazz libraryClass = mock(LibraryClass.class);

        // Act & Assert - should not throw any exception with different clazz types
        assertDoesNotThrow(() -> {
            changer.visitAnyConstant(programClass, constant);
            changer.visitAnyConstant(libraryClass, constant);
        });
    }

    /**
     * Tests that visitAnyConstant works with different Constant implementations.
     */
    @Test
    public void testVisitAnyConstant_withDifferentConstantTypes_doesNotThrowException() {
        // Arrange - create mocks of various constant types
        Constant integerConstant = mock(IntegerConstant.class);
        Constant longConstant = mock(LongConstant.class);
        Constant floatConstant = mock(FloatConstant.class);
        Constant doubleConstant = mock(DoubleConstant.class);
        Constant utf8Constant = mock(Utf8Constant.class);

        // Act & Assert - should not throw any exception with different constant types
        assertDoesNotThrow(() -> {
            changer.visitAnyConstant(clazz, integerConstant);
            changer.visitAnyConstant(clazz, longConstant);
            changer.visitAnyConstant(clazz, floatConstant);
            changer.visitAnyConstant(clazz, doubleConstant);
            changer.visitAnyConstant(clazz, utf8Constant);
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
            changer.visitAnyConstant(clazz, constant);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100, "visitAnyConstant should execute quickly as it's a no-op");
    }

    /**
     * Tests that visitAnyConstant doesn't affect subsequent calls.
     * The no-op should not interfere with the changer's normal operation.
     */
    @Test
    public void testVisitAnyConstant_doesNotAffectSubsequentOperations() {
        // Act - call visitAnyConstant first
        changer.visitAnyConstant(clazz, constant);

        // Then call visitAnyConstant again
        changer.visitAnyConstant(clazz, constant);

        // Assert - verify no side effects occurred
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
            changer.visitAnyConstant(clazz, constant);
        }

        // Assert - verify no interactions occurred despite multiple calls
        verifyNoInteractions(clazz);
        verifyNoInteractions(constant);
    }

    /**
     * Tests that visitAnyConstant is thread-safe when called concurrently.
     * Since it's a no-op with no state changes, it should handle concurrent calls.
     */
    @Test
    public void testVisitAnyConstant_concurrentCalls_noExceptions() throws InterruptedException {
        // Arrange
        final int threadCount = 10;
        Thread[] threads = new Thread[threadCount];

        // Act - create multiple threads that call visitAnyConstant
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    changer.visitAnyConstant(clazz, constant);
                }
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert - verify no interactions occurred
        verifyNoInteractions(clazz);
        verifyNoInteractions(constant);
    }

    /**
     * Tests that multiple changers can call visitAnyConstant independently.
     * Each changer's no-op should not affect others.
     */
    @Test
    public void testVisitAnyConstant_withMultipleChangers_operateIndependently() {
        // Arrange - create multiple changers
        TargetClassChanger changer1 = new TargetClassChanger();
        TargetClassChanger changer2 = new TargetClassChanger();

        // Act - call visitAnyConstant on both changers
        changer1.visitAnyConstant(clazz, constant);
        changer2.visitAnyConstant(clazz, constant);

        // Assert - verify no interactions occurred
        verifyNoInteractions(clazz);
        verifyNoInteractions(constant);
    }

    /**
     * Tests that visitAnyConstant works with different Clazz mock instances.
     */
    @Test
    public void testVisitAnyConstant_withDifferentClazzInstances_doesNotThrowException() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Clazz clazz3 = mock(ProgramClass.class);

        // Act & Assert - should not throw any exception with different instances
        assertDoesNotThrow(() -> {
            changer.visitAnyConstant(clazz1, constant);
            changer.visitAnyConstant(clazz2, constant);
            changer.visitAnyConstant(clazz3, constant);
        });

        // Verify no interactions
        verifyNoInteractions(clazz1);
        verifyNoInteractions(clazz2);
        verifyNoInteractions(clazz3);
    }

    /**
     * Tests that visitAnyConstant works with different Constant mock instances.
     */
    @Test
    public void testVisitAnyConstant_withDifferentConstantInstances_doesNotThrowException() {
        // Arrange
        Constant const1 = mock(Constant.class);
        Constant const2 = mock(Constant.class);
        Constant const3 = mock(Constant.class);

        // Act & Assert - should not throw any exception with different instances
        assertDoesNotThrow(() -> {
            changer.visitAnyConstant(clazz, const1);
            changer.visitAnyConstant(clazz, const2);
            changer.visitAnyConstant(clazz, const3);
        });

        // Verify no interactions
        verifyNoInteractions(const1);
        verifyNoInteractions(const2);
        verifyNoInteractions(const3);
    }

    /**
     * Tests that visitAnyConstant completes without blocking or hanging.
     * This ensures the method doesn't have any unexpected wait conditions.
     */
    @Test
    public void testVisitAnyConstant_completesImmediately() {
        // Arrange
        long timeoutNanos = 1_000_000; // 1 millisecond
        long startTime = System.nanoTime();

        // Act
        changer.visitAnyConstant(clazz, constant);

        long duration = System.nanoTime() - startTime;

        // Assert - should complete within the timeout
        assertTrue(duration < timeoutNanos,
                "visitAnyConstant should complete immediately, took " + duration + " nanoseconds");
    }

    /**
     * Tests that visitAnyConstant called after multiple other operations still behaves as a no-op.
     * This verifies consistent behavior regardless of the changer's usage history.
     */
    @Test
    public void testVisitAnyConstant_afterMultipleOperations_stillNoOp() {
        // Arrange - simulate some prior operations
        for (int i = 0; i < 5; i++) {
            changer.visitAnyConstant(clazz, constant);
        }

        // Act - call visitAnyConstant again
        changer.visitAnyConstant(clazz, constant);

        // Assert - verify it's still a no-op with no interactions
        verifyNoInteractions(clazz);
        verifyNoInteractions(constant);
    }

    /**
     * Tests that visitAnyConstant doesn't affect the changer's internal state.
     * Since it's a no-op, it should not modify any internal fields.
     */
    @Test
    public void testVisitAnyConstant_doesNotAffectInternalState() {
        // Act - call visitAnyConstant multiple times
        changer.visitAnyConstant(clazz, constant);
        changer.visitAnyConstant(clazz, constant);

        // Assert - verify no interactions occurred, indicating no state changes
        verifyNoInteractions(clazz);
        verifyNoInteractions(constant);
    }

    /**
     * Tests that visitAnyConstant with varying combinations of parameters
     * all result in the same no-op behavior.
     */
    @Test
    public void testVisitAnyConstant_varyingParameterCombinations_consistentNoOpBehavior() {
        // Arrange
        Clazz clazz2 = mock(ProgramClass.class);
        Constant const2 = mock(IntegerConstant.class);

        // Act - call with various parameter combinations
        changer.visitAnyConstant(clazz, constant);
        changer.visitAnyConstant(clazz2, const2);
        changer.visitAnyConstant(clazz, const2);

        // Assert - verify no interactions occurred with any parameters
        verifyNoInteractions(clazz);
        verifyNoInteractions(clazz2);
        verifyNoInteractions(constant);
        verifyNoInteractions(const2);
    }

    /**
     * Tests that the visitAnyConstant method signature matches the ConstantVisitor interface.
     * This ensures the method properly overrides the interface method.
     */
    @Test
    public void testVisitAnyConstant_implementsInterfaceCorrectly() {
        // Assert - TargetClassChanger should be a ConstantVisitor
        assertTrue(changer instanceof proguard.classfile.constant.visitor.ConstantVisitor,
                "TargetClassChanger should implement ConstantVisitor");
    }

    /**
     * Tests that visitAnyConstant can be called in rapid succession without issues.
     * This verifies there's no timing-dependent behavior.
     */
    @Test
    public void testVisitAnyConstant_rapidSuccession_noIssues() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 1000; i++) {
                changer.visitAnyConstant(clazz, constant);
            }
        }, "Rapid successive calls should not cause issues");

        // Verify no interactions occurred
        verifyNoInteractions(clazz);
        verifyNoInteractions(constant);
    }

    /**
     * Tests that visitAnyConstant with mixed null and non-null parameters works correctly.
     * This ensures the method handles partial null inputs gracefully.
     */
    @Test
    public void testVisitAnyConstant_withMixedNullParameters_doesNotThrowException() {
        // Act & Assert - test various combinations of null/non-null parameters
        assertDoesNotThrow(() -> changer.visitAnyConstant(null, constant));
        assertDoesNotThrow(() -> changer.visitAnyConstant(clazz, null));
        assertDoesNotThrow(() -> changer.visitAnyConstant(null, null));
    }

    /**
     * Tests that visitAnyConstant handles various constant type mocks.
     * Since it's a no-op, all constant types should be handled the same way.
     */
    @Test
    public void testVisitAnyConstant_withVariousConstantTypeMocks_doesNotThrowException() {
        // Arrange - create mocks of various constant types
        Constant[] constants = {
            mock(IntegerConstant.class),
            mock(LongConstant.class),
            mock(FloatConstant.class),
            mock(DoubleConstant.class),
            mock(Utf8Constant.class),
            mock(StringConstant.class),
            mock(ClassConstant.class),
            mock(MethodrefConstant.class),
            mock(FieldrefConstant.class)
        };

        // Act & Assert - should not throw any exception with any constant type
        for (Constant const_ : constants) {
            assertDoesNotThrow(() -> changer.visitAnyConstant(clazz, const_),
                    "Should not throw with constant: " + const_);
        }
    }

    /**
     * Tests that visitAnyConstant doesn't change changer behavior for subsequent constant processing.
     * The no-op should have no lasting effects.
     */
    @Test
    public void testVisitAnyConstant_doesNotAffectSubsequentConstantProcessing() {
        // Act - call visitAnyConstant multiple times
        for (int i = 0; i < 5; i++) {
            changer.visitAnyConstant(clazz, constant);
        }

        // Assert - changer should still be in initial state with no interactions
        verifyNoInteractions(clazz);
        verifyNoInteractions(constant);
    }

    /**
     * Tests that visitAnyConstant with the same constant instance multiple times
     * doesn't accumulate state or cause issues.
     */
    @Test
    public void testVisitAnyConstant_sameConstantInstanceMultipleTimes_noAccumulation() {
        // Arrange
        Constant singleConstant = mock(IntegerConstant.class);

        // Act - call multiple times with the same constant
        for (int i = 0; i < 10; i++) {
            changer.visitAnyConstant(clazz, singleConstant);
        }

        // Assert - verify no state accumulation
        verifyNoInteractions(clazz);
        verifyNoInteractions(singleConstant);
    }

    /**
     * Tests that visitAnyConstant returns immediately without performing any operations.
     * This confirms the no-op nature of the method.
     */
    @Test
    public void testVisitAnyConstant_returnsImmediatelyWithoutOperations() {
        // Arrange
        Constant trackedConstant = mock(Constant.class);

        // Act
        changer.visitAnyConstant(clazz, trackedConstant);

        // Assert - no method calls should have been made
        verifyNoInteractions(clazz);
        verifyNoInteractions(trackedConstant);
    }

    /**
     * Tests that multiple independent changers calling visitAnyConstant don't interfere with each other.
     */
    @Test
    public void testVisitAnyConstant_multipleChangersNoInterference() {
        // Arrange
        TargetClassChanger changer1 = new TargetClassChanger();
        TargetClassChanger changer2 = new TargetClassChanger();
        TargetClassChanger changer3 = new TargetClassChanger();
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Constant const1 = mock(Constant.class);
        Constant const2 = mock(Constant.class);

        // Act - different changers process different constants
        changer1.visitAnyConstant(clazz1, const1);
        changer2.visitAnyConstant(clazz2, const2);
        changer3.visitAnyConstant(clazz1, const2);

        // Assert - verify no interactions occurred
        verifyNoInteractions(clazz1);
        verifyNoInteractions(clazz2);
        verifyNoInteractions(const1);
        verifyNoInteractions(const2);
    }

    /**
     * Tests that visitAnyConstant works correctly when the TargetClassChanger
     * is used polymorphically as a ConstantVisitor.
     */
    @Test
    public void testVisitAnyConstant_asConstantVisitorInterface_doesNotThrowException() {
        // Arrange - treat changer as ConstantVisitor interface
        proguard.classfile.constant.visitor.ConstantVisitor visitor = changer;

        // Act & Assert - should work the same way through the interface
        assertDoesNotThrow(() -> visitor.visitAnyConstant(clazz, constant));
        verifyNoInteractions(clazz);
        verifyNoInteractions(constant);
    }

    /**
     * Tests that visitAnyConstant called on newly created changer instance behaves consistently.
     * Verifies the method works immediately after construction without initialization.
     */
    @Test
    public void testVisitAnyConstant_onNewInstance_behavesConsistently() {
        // Arrange - create a fresh changer
        TargetClassChanger freshChanger = new TargetClassChanger();

        // Act - immediately call visitAnyConstant
        freshChanger.visitAnyConstant(clazz, constant);

        // Assert - should behave as no-op
        verifyNoInteractions(clazz);
        verifyNoInteractions(constant);
    }

    /**
     * Tests that visitAnyConstant handles edge case of calling with various
     * combinations of different clazz and constant pairs in sequence.
     */
    @Test
    public void testVisitAnyConstant_sequentialCallsWithDifferentParameters_noIssues() {
        // Arrange
        Clazz[] clazzes = {
            mock(ProgramClass.class),
            mock(LibraryClass.class),
            mock(ProgramClass.class)
        };
        Constant[] constants = {
            mock(IntegerConstant.class),
            mock(StringConstant.class),
            mock(ClassConstant.class)
        };

        // Act - call with different combinations
        for (Clazz c : clazzes) {
            for (Constant const_ : constants) {
                changer.visitAnyConstant(c, const_);
            }
        }

        // Assert - verify no interactions with any parameter
        for (Clazz c : clazzes) {
            verifyNoInteractions(c);
        }
        for (Constant const_ : constants) {
            verifyNoInteractions(const_);
        }
    }

    /**
     * Tests that visitAnyConstant maintains no-op behavior even when
     * alternating between null and non-null parameters.
     */
    @Test
    public void testVisitAnyConstant_alternatingNullAndNonNull_consistentBehavior() {
        // Act & Assert - alternate between null and non-null
        assertDoesNotThrow(() -> {
            changer.visitAnyConstant(clazz, constant);
            changer.visitAnyConstant(null, constant);
            changer.visitAnyConstant(clazz, null);
            changer.visitAnyConstant(null, null);
            changer.visitAnyConstant(clazz, constant);
        });

        // Verify no interactions with non-null parameters
        verifyNoInteractions(clazz);
        verifyNoInteractions(constant);
    }
}
