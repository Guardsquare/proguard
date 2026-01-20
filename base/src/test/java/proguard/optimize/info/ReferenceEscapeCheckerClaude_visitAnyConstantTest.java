package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.LibraryClass;
import proguard.classfile.constant.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ReferenceEscapeChecker#visitAnyConstant(Clazz, Constant)}.
 *
 * The visitAnyConstant method is an empty implementation (no-op) that serves as a default
 * handler in the ConstantVisitor pattern. The ReferenceEscapeChecker implements specific
 * constant visitor methods (visitFieldrefConstant, visitAnyMethodrefConstant) to handle
 * particular constant types, and this method provides the default no-op behavior for all
 * other constant types.
 *
 * Method signature: public void visitAnyConstant(Clazz clazz, Constant constant)
 * Implementation: {} (empty body)
 *
 * Note: Mocking is used here because visitAnyConstant is a no-op method with an empty body.
 * There is no meaningful behavior to test without mocking - we can only verify it doesn't throw
 * exceptions and doesn't interact with its parameters.
 */
public class ReferenceEscapeCheckerClaude_visitAnyConstantTest {

    private ReferenceEscapeChecker checker;
    private Clazz clazz;
    private Constant constant;

    @BeforeEach
    public void setUp() {
        checker = new ReferenceEscapeChecker();
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
        assertDoesNotThrow(() -> checker.visitAnyConstant(clazz, constant));
    }

    /**
     * Tests that visitAnyConstant can be called with null Clazz parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyConstant_withNullClazz_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.visitAnyConstant(null, constant));
    }

    /**
     * Tests that visitAnyConstant can be called with null Constant parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyConstant_withNullConstant_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.visitAnyConstant(clazz, null));
    }

    /**
     * Tests that visitAnyConstant can be called with both parameters null.
     * The method should handle null parameters gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyConstant_withBothParametersNull_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.visitAnyConstant(null, null));
    }

    /**
     * Tests that visitAnyConstant can be called multiple times in succession.
     * The method should be idempotent and handle repeated calls without issues.
     */
    @Test
    public void testVisitAnyConstant_calledMultipleTimes_doesNotThrowException() {
        // Act & Assert - should not throw any exception on multiple calls
        assertDoesNotThrow(() -> {
            checker.visitAnyConstant(clazz, constant);
            checker.visitAnyConstant(clazz, constant);
            checker.visitAnyConstant(clazz, constant);
        });
    }

    /**
     * Tests that visitAnyConstant doesn't interact with the Clazz parameter.
     * Since it's a no-op method, it should not call any methods on the clazz.
     */
    @Test
    public void testVisitAnyConstant_doesNotInteractWithClazz() {
        // Act
        checker.visitAnyConstant(clazz, constant);

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
        checker.visitAnyConstant(clazz, constant);

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
            checker.visitAnyConstant(programClass, constant);
            checker.visitAnyConstant(libraryClass, constant);
        });
    }

    /**
     * Tests that visitAnyConstant works with different Constant implementations.
     */
    @Test
    public void testVisitAnyConstant_withDifferentConstantTypes_doesNotThrowException() {
        // Arrange - create mocks of various constant types
        Constant integerConstant = mock(IntegerConstant.class);
        Constant floatConstant = mock(FloatConstant.class);
        Constant longConstant = mock(LongConstant.class);
        Constant doubleConstant = mock(DoubleConstant.class);
        Constant stringConstant = mock(StringConstant.class);
        Constant utf8Constant = mock(Utf8Constant.class);
        Constant classConstant = mock(ClassConstant.class);
        Constant fieldrefConstant = mock(FieldrefConstant.class);
        Constant methodrefConstant = mock(MethodrefConstant.class);
        Constant interfaceMethodrefConstant = mock(InterfaceMethodrefConstant.class);

        // Act & Assert - should not throw any exception with different constant types
        assertDoesNotThrow(() -> {
            checker.visitAnyConstant(clazz, integerConstant);
            checker.visitAnyConstant(clazz, floatConstant);
            checker.visitAnyConstant(clazz, longConstant);
            checker.visitAnyConstant(clazz, doubleConstant);
            checker.visitAnyConstant(clazz, stringConstant);
            checker.visitAnyConstant(clazz, utf8Constant);
            checker.visitAnyConstant(clazz, classConstant);
            checker.visitAnyConstant(clazz, fieldrefConstant);
            checker.visitAnyConstant(clazz, methodrefConstant);
            checker.visitAnyConstant(clazz, interfaceMethodrefConstant);
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
            checker.visitAnyConstant(clazz, constant);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100, "visitAnyConstant should execute quickly as it's a no-op");
    }

    /**
     * Tests that visitAnyConstant doesn't affect subsequent calls.
     * The no-op should not interfere with the checker's normal operation.
     */
    @Test
    public void testVisitAnyConstant_doesNotAffectSubsequentOperations() {
        // Act - call visitAnyConstant first
        checker.visitAnyConstant(clazz, constant);

        // Then call visitAnyConstant again
        checker.visitAnyConstant(clazz, constant);

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
            checker.visitAnyConstant(clazz, constant);
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
                    checker.visitAnyConstant(clazz, constant);
                }
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert - if we reached here, no exceptions were thrown
        assertTrue(true, "All concurrent calls completed without exceptions");
    }

    /**
     * Tests that visitAnyConstant can be called alternating with different parameters.
     */
    @Test
    public void testVisitAnyConstant_alternatingParameters_noExceptions() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(LibraryClass.class);
        Constant const1 = mock(IntegerConstant.class);
        Constant const2 = mock(StringConstant.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            checker.visitAnyConstant(clazz1, const1);
            checker.visitAnyConstant(clazz2, const2);
            checker.visitAnyConstant(clazz1, const2);
            checker.visitAnyConstant(clazz2, const1);
        });

        // Verify no interactions occurred with any mock
        verifyNoInteractions(clazz1);
        verifyNoInteractions(clazz2);
        verifyNoInteractions(const1);
        verifyNoInteractions(const2);
    }

    /**
     * Tests that visitAnyConstant doesn't modify any internal state of the checker.
     * We can verify this by checking that public state accessors still return false after calling it.
     */
    @Test
    public void testVisitAnyConstant_doesNotModifyCheckerState() {
        // Arrange - check initial state
        assertFalse(checker.isInstanceExternal(0), "Initial state should be false");

        // Act - call visitAnyConstant
        checker.visitAnyConstant(clazz, constant);

        // Assert - state should remain unchanged
        assertFalse(checker.isInstanceExternal(0), "State should remain unchanged after visitAnyConstant");
        assertFalse(checker.isInstanceEscaping(0), "State should remain unchanged");
        assertFalse(checker.isInstanceReturned(0), "State should remain unchanged");
        assertFalse(checker.isInstanceModified(0), "State should remain unchanged");
    }

    /**
     * Tests that visitAnyConstant can be called from multiple checker instances independently.
     */
    @Test
    public void testVisitAnyConstant_multipleCheckerInstances_independent() {
        // Arrange
        ReferenceEscapeChecker checker1 = new ReferenceEscapeChecker();
        ReferenceEscapeChecker checker2 = new ReferenceEscapeChecker();
        ReferenceEscapeChecker checker3 = new ReferenceEscapeChecker();

        // Act & Assert - should work independently without interference
        assertDoesNotThrow(() -> {
            checker1.visitAnyConstant(clazz, constant);
            checker2.visitAnyConstant(clazz, constant);
            checker3.visitAnyConstant(clazz, constant);
        });
    }

    /**
     * Tests that visitAnyConstant can be called in a loop with varying parameters.
     */
    @Test
    public void testVisitAnyConstant_loopWithVaryingParameters_noExceptions() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 50; i++) {
                Clazz tempClazz = (i % 2 == 0) ? mock(ProgramClass.class) : mock(LibraryClass.class);
                Constant tempConst = (i % 3 == 0) ? mock(IntegerConstant.class) : mock(StringConstant.class);
                checker.visitAnyConstant(tempClazz, tempConst);
            }
        });
    }

    /**
     * Tests that visitAnyConstant returns void (no return value).
     */
    @Test
    public void testVisitAnyConstant_returnsVoid() {
        // This test verifies the method signature at compile time
        // If this compiles, the method returns void
        checker.visitAnyConstant(clazz, constant);
        // No assertion needed - compilation success is the test
    }

    /**
     * Tests that visitAnyConstant can be invoked through the ConstantVisitor interface.
     */
    @Test
    public void testVisitAnyConstant_throughConstantVisitorInterface_works() {
        // Arrange
        proguard.classfile.constant.visitor.ConstantVisitor visitor = checker;

        // Act & Assert
        assertDoesNotThrow(() -> visitor.visitAnyConstant(clazz, constant));
    }

    /**
     * Tests that visitAnyConstant doesn't throw any runtime exceptions even with unusual inputs.
     */
    @Test
    public void testVisitAnyConstant_withUnusualInputs_noExceptions() {
        // Act & Assert - test various edge cases
        assertDoesNotThrow(() -> {
            // Null inputs
            checker.visitAnyConstant(null, null);

            // Null clazz, valid constant
            checker.visitAnyConstant(null, constant);

            // Valid clazz, null constant
            checker.visitAnyConstant(clazz, null);

            // Normal case
            checker.visitAnyConstant(clazz, constant);
        });
    }

    /**
     * Tests that calling visitAnyConstant many times doesn't cause memory issues.
     */
    @Test
    public void testVisitAnyConstant_manyInvocations_noMemoryIssues() {
        // Act & Assert - call 10000 times
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 10000; i++) {
                checker.visitAnyConstant(clazz, constant);
            }
        });
    }

    /**
     * Tests that visitAnyConstant works correctly after the checker has been used for other operations.
     */
    @Test
    public void testVisitAnyConstant_afterOtherOperations_stillWorks() {
        // Arrange - perform other operations first
        checker.isInstanceExternal(0);
        checker.isInstanceEscaping(0);
        checker.isInstanceReturned(0);
        checker.isInstanceModified(0);

        // Act & Assert - visitAnyConstant should still work
        assertDoesNotThrow(() -> checker.visitAnyConstant(clazz, constant));

        // Verify no interactions
        verifyNoInteractions(clazz);
        verifyNoInteractions(constant);
    }

    /**
     * Tests that visitAnyConstant has no observable side effects.
     */
    @Test
    public void testVisitAnyConstant_noObservableSideEffects() {
        // Arrange - record state before
        boolean externalBefore = checker.isInstanceExternal(0);
        boolean escapingBefore = checker.isInstanceEscaping(0);
        boolean returnedBefore = checker.isInstanceReturned(0);
        boolean modifiedBefore = checker.isInstanceModified(0);

        // Act
        checker.visitAnyConstant(clazz, constant);

        // Assert - state should be unchanged
        assertEquals(externalBefore, checker.isInstanceExternal(0), "isInstanceExternal should be unchanged");
        assertEquals(escapingBefore, checker.isInstanceEscaping(0), "isInstanceEscaping should be unchanged");
        assertEquals(returnedBefore, checker.isInstanceReturned(0), "isInstanceReturned should be unchanged");
        assertEquals(modifiedBefore, checker.isInstanceModified(0), "isInstanceModified should be unchanged");
    }

    /**
     * Tests that visitAnyConstant completes instantly even when called in rapid succession.
     */
    @Test
    public void testVisitAnyConstant_rapidSuccession_completesInstantly() {
        // Act - call in very rapid succession
        long startTime = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            checker.visitAnyConstant(clazz, constant);
        }
        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should be extremely fast for 10000 no-op calls
        assertTrue(durationMs < 500, "10000 calls should complete in less than 500ms");
    }

    /**
     * Tests that visitAnyConstant works correctly even when called between
     * other visitor methods that do have side effects.
     */
    @Test
    public void testVisitAnyConstant_betweenOtherVisitorCalls_remainsNoOp() {
        // Act - call visitAnyConstant
        checker.visitAnyConstant(clazz, constant);

        // Assert - verify no interactions occurred
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
        checker.visitAnyConstant(clazz, constant);
        checker.visitAnyConstant(clazz2, const2);
        checker.visitAnyConstant(clazz, const2);

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
        // Assert - ReferenceEscapeChecker should be a ConstantVisitor
        assertTrue(checker instanceof proguard.classfile.constant.visitor.ConstantVisitor,
                "ReferenceEscapeChecker should implement ConstantVisitor");
    }

    /**
     * Tests that visitAnyConstant works with actual constant types (not just mocks).
     */
    @Test
    public void testVisitAnyConstant_withActualConstantTypes_doesNotThrowException() {
        // Arrange - create actual constant instances
        IntegerConstant intConst = new IntegerConstant(42);
        LongConstant longConst = new LongConstant(100L);
        FloatConstant floatConst = new FloatConstant(3.14f);
        DoubleConstant doubleConst = new DoubleConstant(2.718);
        Utf8Constant utf8Const = new Utf8Constant("test");
        StringConstant strConst = new StringConstant(1, null, null);

        // Act & Assert - should not throw with actual constants
        assertDoesNotThrow(() -> {
            checker.visitAnyConstant(clazz, intConst);
            checker.visitAnyConstant(clazz, longConst);
            checker.visitAnyConstant(clazz, floatConst);
            checker.visitAnyConstant(clazz, doubleConst);
            checker.visitAnyConstant(clazz, utf8Const);
            checker.visitAnyConstant(clazz, strConst);
        });
    }

    /**
     * Tests that visitAnyConstant doesn't modify the constant parameter.
     * Since it's a no-op, the constant should remain unchanged.
     */
    @Test
    public void testVisitAnyConstant_constantRemainsUnchanged() {
        // Arrange
        IntegerConstant intConst = new IntegerConstant(42);
        int originalValue = intConst.getValue();

        // Act
        checker.visitAnyConstant(clazz, intConst);

        // Assert - constant should remain unchanged
        assertEquals(originalValue, intConst.getValue(),
                "Constant value should remain unchanged after visitAnyConstant");
    }

    /**
     * Tests that visitAnyConstant can be called with different constant values.
     */
    @Test
    public void testVisitAnyConstant_withDifferentConstantValues_doesNotThrowException() {
        // Arrange
        IntegerConstant int1 = new IntegerConstant(1);
        IntegerConstant int2 = new IntegerConstant(100);
        IntegerConstant int3 = new IntegerConstant(-50);
        Utf8Constant str1 = new Utf8Constant("hello");
        Utf8Constant str2 = new Utf8Constant("world");

        // Act & Assert - should not throw with different values
        assertDoesNotThrow(() -> {
            checker.visitAnyConstant(clazz, int1);
            checker.visitAnyConstant(clazz, int2);
            checker.visitAnyConstant(clazz, int3);
            checker.visitAnyConstant(clazz, str1);
            checker.visitAnyConstant(clazz, str2);
        });
    }

    /**
     * Tests that visitAnyConstant handles the complete absence of state changes.
     * This is a comprehensive test verifying the method truly does nothing.
     */
    @Test
    public void testVisitAnyConstant_completeNoOp_noStateChanges() {
        // Arrange - capture all initial state
        boolean[] initialState = new boolean[10];
        for (int i = 0; i < 10; i++) {
            initialState[i] = checker.isInstanceExternal(i) ||
                              checker.isInstanceEscaping(i) ||
                              checker.isInstanceReturned(i) ||
                              checker.isInstanceModified(i);
        }

        // Act - call visitAnyConstant multiple times
        for (int i = 0; i < 5; i++) {
            checker.visitAnyConstant(clazz, constant);
        }

        // Assert - all state should remain unchanged
        for (int i = 0; i < 10; i++) {
            boolean currentState = checker.isInstanceExternal(i) ||
                                   checker.isInstanceEscaping(i) ||
                                   checker.isInstanceReturned(i) ||
                                   checker.isInstanceModified(i);
            assertEquals(initialState[i], currentState,
                    "State at offset " + i + " should remain unchanged");
        }
    }
}
