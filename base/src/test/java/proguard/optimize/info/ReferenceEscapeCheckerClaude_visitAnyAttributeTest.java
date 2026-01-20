package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.LibraryClass;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.LineNumberTableAttribute;
import proguard.classfile.attribute.SourceFileAttribute;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ReferenceEscapeChecker#visitAnyAttribute(Clazz, Attribute)}.
 *
 * The visitAnyAttribute method is an empty implementation (no-op) that serves as a default
 * handler in the AttributeVisitor pattern. The ReferenceEscapeChecker only processes
 * specific attribute types through visitCodeAttribute. This method provides the default no-op
 * behavior for all other attribute types (LineNumberTable, SourceFile, etc.).
 *
 * Method signature: public void visitAnyAttribute(Clazz clazz, Attribute attribute)
 * Implementation: {} (empty body)
 *
 * Note: Mocking is used here because visitAnyAttribute is a no-op method with an empty body.
 * There is no meaningful behavior to test without mocking - we can only verify it doesn't throw
 * exceptions and doesn't interact with its parameters.
 */
public class ReferenceEscapeCheckerClaude_visitAnyAttributeTest {

    private ReferenceEscapeChecker checker;
    private Clazz clazz;
    private Attribute attribute;

    @BeforeEach
    public void setUp() {
        checker = new ReferenceEscapeChecker();
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
        assertDoesNotThrow(() -> checker.visitAnyAttribute(clazz, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called with null Clazz parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyAttribute_withNullClazz_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.visitAnyAttribute(null, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called with null Attribute parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyAttribute_withNullAttribute_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.visitAnyAttribute(clazz, null));
    }

    /**
     * Tests that visitAnyAttribute can be called with both parameters null.
     * The method should handle null parameters gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyAttribute_withBothParametersNull_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.visitAnyAttribute(null, null));
    }

    /**
     * Tests that visitAnyAttribute can be called multiple times in succession.
     * The method should be idempotent and handle repeated calls without issues.
     */
    @Test
    public void testVisitAnyAttribute_calledMultipleTimes_doesNotThrowException() {
        // Act & Assert - should not throw any exception on multiple calls
        assertDoesNotThrow(() -> {
            checker.visitAnyAttribute(clazz, attribute);
            checker.visitAnyAttribute(clazz, attribute);
            checker.visitAnyAttribute(clazz, attribute);
        });
    }

    /**
     * Tests that visitAnyAttribute doesn't interact with the Clazz parameter.
     * Since it's a no-op method, it should not call any methods on the clazz.
     */
    @Test
    public void testVisitAnyAttribute_doesNotInteractWithClazz() {
        // Act
        checker.visitAnyAttribute(clazz, attribute);

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
        checker.visitAnyAttribute(clazz, attribute);

        // Assert - verify no interactions occurred with the attribute mock
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute works with different Clazz implementations.
     */
    @Test
    public void testVisitAnyAttribute_withDifferentClazzTypes_doesNotThrowException() {
        // Arrange
        Clazz programClass = mock(ProgramClass.class);
        Clazz libraryClass = mock(LibraryClass.class);

        // Act & Assert - should not throw any exception with different clazz types
        assertDoesNotThrow(() -> {
            checker.visitAnyAttribute(programClass, attribute);
            checker.visitAnyAttribute(libraryClass, attribute);
        });
    }

    /**
     * Tests that visitAnyAttribute works with different Attribute implementations.
     */
    @Test
    public void testVisitAnyAttribute_withDifferentAttributeTypes_doesNotThrowException() {
        // Arrange - create mocks of various attribute types
        Attribute codeAttribute = mock(CodeAttribute.class);
        Attribute lineNumberTableAttribute = mock(LineNumberTableAttribute.class);
        Attribute sourceFileAttribute = mock(SourceFileAttribute.class);
        Attribute genericAttribute = mock(Attribute.class);

        // Act & Assert - should not throw any exception with different attribute types
        assertDoesNotThrow(() -> {
            checker.visitAnyAttribute(clazz, codeAttribute);
            checker.visitAnyAttribute(clazz, lineNumberTableAttribute);
            checker.visitAnyAttribute(clazz, sourceFileAttribute);
            checker.visitAnyAttribute(clazz, genericAttribute);
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
            checker.visitAnyAttribute(clazz, attribute);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100, "visitAnyAttribute should execute quickly as it's a no-op");
    }

    /**
     * Tests that visitAnyAttribute doesn't affect subsequent calls.
     * The no-op should not interfere with the checker's normal operation.
     */
    @Test
    public void testVisitAnyAttribute_doesNotAffectSubsequentOperations() {
        // Act - call visitAnyAttribute first
        checker.visitAnyAttribute(clazz, attribute);

        // Then call visitAnyAttribute again
        checker.visitAnyAttribute(clazz, attribute);

        // Assert - verify no side effects occurred
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
            checker.visitAnyAttribute(clazz, attribute);
        }

        // Assert - verify no interactions occurred despite multiple calls
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute is thread-safe when called concurrently.
     * Since it's a no-op with no state changes, it should handle concurrent calls.
     */
    @Test
    public void testVisitAnyAttribute_concurrentCalls_noExceptions() throws InterruptedException {
        // Arrange
        final int threadCount = 10;
        Thread[] threads = new Thread[threadCount];

        // Act - create multiple threads that call visitAnyAttribute
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    checker.visitAnyAttribute(clazz, attribute);
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
     * Tests that visitAnyAttribute can be called alternating with different parameters.
     */
    @Test
    public void testVisitAnyAttribute_alternatingParameters_noExceptions() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(LibraryClass.class);
        Attribute attr1 = mock(CodeAttribute.class);
        Attribute attr2 = mock(LineNumberTableAttribute.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            checker.visitAnyAttribute(clazz1, attr1);
            checker.visitAnyAttribute(clazz2, attr2);
            checker.visitAnyAttribute(clazz1, attr2);
            checker.visitAnyAttribute(clazz2, attr1);
        });

        // Verify no interactions occurred with any mock
        verifyNoInteractions(clazz1);
        verifyNoInteractions(clazz2);
        verifyNoInteractions(attr1);
        verifyNoInteractions(attr2);
    }

    /**
     * Tests that visitAnyAttribute doesn't modify any internal state of the checker.
     * We can verify this by checking that isInstanceExternal still returns false after calling it.
     */
    @Test
    public void testVisitAnyAttribute_doesNotModifyCheckerState() {
        // Arrange - check initial state
        assertFalse(checker.isInstanceExternal(0), "Initial state should be false");

        // Act - call visitAnyAttribute
        checker.visitAnyAttribute(clazz, attribute);

        // Assert - state should remain unchanged
        assertFalse(checker.isInstanceExternal(0), "State should remain unchanged after visitAnyAttribute");
        assertFalse(checker.isInstanceEscaping(0), "State should remain unchanged");
        assertFalse(checker.isInstanceReturned(0), "State should remain unchanged");
        assertFalse(checker.isInstanceModified(0), "State should remain unchanged");
    }

    /**
     * Tests that visitAnyAttribute can be called from multiple checker instances independently.
     */
    @Test
    public void testVisitAnyAttribute_multipleCheckerInstances_independent() {
        // Arrange
        ReferenceEscapeChecker checker1 = new ReferenceEscapeChecker();
        ReferenceEscapeChecker checker2 = new ReferenceEscapeChecker();
        ReferenceEscapeChecker checker3 = new ReferenceEscapeChecker();

        // Act & Assert - should work independently without interference
        assertDoesNotThrow(() -> {
            checker1.visitAnyAttribute(clazz, attribute);
            checker2.visitAnyAttribute(clazz, attribute);
            checker3.visitAnyAttribute(clazz, attribute);
        });
    }

    /**
     * Tests that visitAnyAttribute can be called in a loop with varying parameters.
     */
    @Test
    public void testVisitAnyAttribute_loopWithVaryingParameters_noExceptions() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 50; i++) {
                Clazz tempClazz = (i % 2 == 0) ? mock(ProgramClass.class) : mock(LibraryClass.class);
                Attribute tempAttr = mock(Attribute.class);
                checker.visitAnyAttribute(tempClazz, tempAttr);
            }
        });
    }

    /**
     * Tests that visitAnyAttribute returns void (no return value).
     */
    @Test
    public void testVisitAnyAttribute_returnsVoid() {
        // This test verifies the method signature at compile time
        // If this compiles, the method returns void
        checker.visitAnyAttribute(clazz, attribute);
        // No assertion needed - compilation success is the test
    }

    /**
     * Tests that visitAnyAttribute can be invoked through the AttributeVisitor interface.
     */
    @Test
    public void testVisitAnyAttribute_throughAttributeVisitorInterface_works() {
        // Arrange
        proguard.classfile.attribute.visitor.AttributeVisitor visitor = checker;

        // Act & Assert
        assertDoesNotThrow(() -> visitor.visitAnyAttribute(clazz, attribute));
    }

    /**
     * Tests that visitAnyAttribute doesn't throw any runtime exceptions even with unusual inputs.
     */
    @Test
    public void testVisitAnyAttribute_withUnusualInputs_noExceptions() {
        // Act & Assert - test various edge cases
        assertDoesNotThrow(() -> {
            // Null inputs
            checker.visitAnyAttribute(null, null);

            // Null clazz, valid attribute
            checker.visitAnyAttribute(null, attribute);

            // Valid clazz, null attribute
            checker.visitAnyAttribute(clazz, null);

            // Normal case
            checker.visitAnyAttribute(clazz, attribute);
        });
    }

    /**
     * Tests that calling visitAnyAttribute many times doesn't cause memory issues.
     */
    @Test
    public void testVisitAnyAttribute_manyInvocations_noMemoryIssues() {
        // Act & Assert - call 10000 times
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 10000; i++) {
                checker.visitAnyAttribute(clazz, attribute);
            }
        });
    }

    /**
     * Tests that visitAnyAttribute works correctly after the checker has been used for other operations.
     */
    @Test
    public void testVisitAnyAttribute_afterOtherOperations_stillWorks() {
        // Arrange - perform other operations first
        checker.isInstanceExternal(0);
        checker.isInstanceEscaping(0);
        checker.isInstanceReturned(0);
        checker.isInstanceModified(0);

        // Act & Assert - visitAnyAttribute should still work
        assertDoesNotThrow(() -> checker.visitAnyAttribute(clazz, attribute));

        // Verify no interactions
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute has no observable side effects.
     */
    @Test
    public void testVisitAnyAttribute_noObservableSideEffects() {
        // Arrange - record state before
        boolean externalBefore = checker.isInstanceExternal(0);
        boolean escapingBefore = checker.isInstanceEscaping(0);
        boolean returnedBefore = checker.isInstanceReturned(0);
        boolean modifiedBefore = checker.isInstanceModified(0);

        // Act
        checker.visitAnyAttribute(clazz, attribute);

        // Assert - state should be unchanged
        assertEquals(externalBefore, checker.isInstanceExternal(0), "isInstanceExternal should be unchanged");
        assertEquals(escapingBefore, checker.isInstanceEscaping(0), "isInstanceEscaping should be unchanged");
        assertEquals(returnedBefore, checker.isInstanceReturned(0), "isInstanceReturned should be unchanged");
        assertEquals(modifiedBefore, checker.isInstanceModified(0), "isInstanceModified should be unchanged");
    }

    /**
     * Tests that visitAnyAttribute completes instantly even when called in rapid succession.
     */
    @Test
    public void testVisitAnyAttribute_rapidSuccession_completesInstantly() {
        // Act - call in very rapid succession
        long startTime = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            checker.visitAnyAttribute(clazz, attribute);
        }
        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should be extremely fast for 10000 no-op calls
        assertTrue(durationMs < 500, "10000 calls should complete in less than 500ms");
    }
}
