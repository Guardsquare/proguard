package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.visitor.MemberVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link DuplicateInitializerFixer#visitAnyAttribute(Clazz, Attribute)}.
 *
 * The visitAnyAttribute method is an empty implementation (no-op) that serves as a default
 * handler in the AttributeVisitor pattern for attributes that don't have specialized visitor methods.
 */
public class DuplicateInitializerFixerClaude_visitAnyAttributeTest {

    private DuplicateInitializerFixer fixer;
    private MemberVisitor extraFixedInitializerVisitor;
    private Clazz clazz;
    private Attribute attribute;

    @BeforeEach
    public void setUp() {
        extraFixedInitializerVisitor = mock(MemberVisitor.class);
        fixer = new DuplicateInitializerFixer(extraFixedInitializerVisitor);
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
        assertDoesNotThrow(() -> fixer.visitAnyAttribute(clazz, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called with null Clazz parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyAttribute_withNullClazz_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> fixer.visitAnyAttribute(null, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called with null Attribute parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyAttribute_withNullAttribute_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> fixer.visitAnyAttribute(clazz, null));
    }

    /**
     * Tests that visitAnyAttribute can be called with both parameters null.
     * The method should handle null parameters gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyAttribute_withBothParametersNull_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> fixer.visitAnyAttribute(null, null));
    }

    /**
     * Tests that visitAnyAttribute can be called multiple times in succession.
     * The method should be idempotent and handle repeated calls without issues.
     */
    @Test
    public void testVisitAnyAttribute_calledMultipleTimes_doesNotThrowException() {
        // Act & Assert - should not throw any exception on multiple calls
        assertDoesNotThrow(() -> {
            fixer.visitAnyAttribute(clazz, attribute);
            fixer.visitAnyAttribute(clazz, attribute);
            fixer.visitAnyAttribute(clazz, attribute);
        });
    }

    /**
     * Tests that visitAnyAttribute doesn't interact with the Clazz parameter.
     * Since it's a no-op method, it should not call any methods on the clazz.
     */
    @Test
    public void testVisitAnyAttribute_doesNotInteractWithClazz() {
        // Act
        fixer.visitAnyAttribute(clazz, attribute);

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
        fixer.visitAnyAttribute(clazz, attribute);

        // Assert - verify no interactions occurred with the attribute mock
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute doesn't affect the fixer's internal state.
     * Calling the method should not change any fields or trigger any side effects.
     */
    @Test
    public void testVisitAnyAttribute_doesNotModifyFixerState() {
        // Arrange - create another fixer with the same configuration
        DuplicateInitializerFixer fixer2 = new DuplicateInitializerFixer(extraFixedInitializerVisitor);

        // Act - call visitAnyAttribute on the first fixer
        fixer.visitAnyAttribute(clazz, attribute);

        // Assert - both fixers should be functionally equivalent
        // Since visitAnyAttribute is a no-op, we verify no visitors were invoked
        verifyNoInteractions(extraFixedInitializerVisitor);
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
            fixer.visitAnyAttribute(clazz1, attribute);
            fixer.visitAnyAttribute(clazz2, attribute);
            fixer.visitAnyAttribute(clazz3, attribute);
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
            fixer.visitAnyAttribute(clazz, attr1);
            fixer.visitAnyAttribute(clazz, attr2);
            fixer.visitAnyAttribute(clazz, attr3);
        });
    }

    /**
     * Tests that visitAnyAttribute doesn't trigger the extra fixed initializer visitor.
     * Since it's a no-op method, it should not affect the visitor.
     */
    @Test
    public void testVisitAnyAttribute_doesNotTriggerExtraVisitor() {
        // Act
        fixer.visitAnyAttribute(clazz, attribute);

        // Assert - verify the extra visitor was not invoked
        verifyNoInteractions(extraFixedInitializerVisitor);
    }

    /**
     * Tests that visitAnyAttribute can be called on a fixer with null extra visitor.
     * The method should work even if the optional visitor is null.
     */
    @Test
    public void testVisitAnyAttribute_withNullExtraVisitor_doesNotThrowException() {
        // Arrange - create fixer with null extra visitor (using no-arg constructor)
        DuplicateInitializerFixer fixerWithNullVisitor = new DuplicateInitializerFixer();

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> fixerWithNullVisitor.visitAnyAttribute(clazz, attribute));
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
            fixer.visitAnyAttribute(clazz, attribute);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100, "visitAnyAttribute should execute quickly as it's a no-op");
    }

    /**
     * Tests that visitAnyAttribute doesn't affect subsequent calls to other methods.
     * The no-op should not interfere with the fixer's normal operation.
     */
    @Test
    public void testVisitAnyAttribute_doesNotAffectSubsequentOperations() {
        // Act - call visitAnyAttribute first
        fixer.visitAnyAttribute(clazz, attribute);

        // Then call visitAnyAttribute again
        fixer.visitAnyAttribute(clazz, attribute);

        // Assert - verify no side effects occurred
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
        verifyNoInteractions(extraFixedInitializerVisitor);
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
            fixer.visitAnyAttribute(clazz, attr1);
            fixer.visitAnyAttribute(clazz, attr2);
            fixer.visitAnyAttribute(clazz, attr3);
        });

        // Verify that the attribute methods were not called since it's a no-op
        verify(attr1, never()).getAttributeName(any());
        verify(attr2, never()).getAttributeName(any());
        verify(attr3, never()).getAttributeName(any());
    }

    /**
     * Tests that multiple fixers can call visitAnyAttribute independently.
     * Each fixer's no-op should not affect others.
     */
    @Test
    public void testVisitAnyAttribute_withMultipleFixers_operateIndependently() {
        // Arrange - create multiple fixers
        MemberVisitor visitor1 = mock(MemberVisitor.class);
        MemberVisitor visitor2 = mock(MemberVisitor.class);

        DuplicateInitializerFixer fixer1 = new DuplicateInitializerFixer(visitor1);
        DuplicateInitializerFixer fixer2 = new DuplicateInitializerFixer(visitor2);

        // Act - call visitAnyAttribute on both fixers
        fixer1.visitAnyAttribute(clazz, attribute);
        fixer2.visitAnyAttribute(clazz, attribute);

        // Assert - verify no interactions occurred on any visitor
        verifyNoInteractions(visitor1);
        verifyNoInteractions(visitor2);
    }

    /**
     * Tests that visitAnyAttribute can be called with the same parameters repeatedly
     * without accumulating any state or causing issues.
     */
    @Test
    public void testVisitAnyAttribute_repeatedCallsWithSameParameters_noStateAccumulation() {
        // Act - call multiple times with same parameters
        for (int i = 0; i < 10; i++) {
            fixer.visitAnyAttribute(clazz, attribute);
        }

        // Assert - verify no interactions occurred despite multiple calls
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
        verifyNoInteractions(extraFixedInitializerVisitor);
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
                    fixer.visitAnyAttribute(clazz, attribute);
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
        verifyNoInteractions(attribute);
        verifyNoInteractions(extraFixedInitializerVisitor);
    }
}
