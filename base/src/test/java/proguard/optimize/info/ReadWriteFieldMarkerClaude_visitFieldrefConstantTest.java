package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.LibraryClass;
import proguard.classfile.constant.FieldrefConstant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ReadWriteFieldMarker#visitFieldrefConstant(Clazz, FieldrefConstant)}.
 *
 * The visitFieldrefConstant method calls referencedFieldAccept on the FieldrefConstant,
 * which allows the marker to process the field that this constant references. This is
 * a key method for tracking field read/write access, as field references in the constant
 * pool are visited through this method.
 *
 * The method delegates to referencedFieldAccept, which will eventually call visitProgramField
 * where the actual marking of fields as read or written occurs based on the internal state
 * (reading/writing flags) set by visitConstantInstruction.
 */
public class ReadWriteFieldMarkerClaude_visitFieldrefConstantTest {

    private ReadWriteFieldMarker marker;
    private MutableBoolean repeatTrigger;
    private Clazz clazz;
    private FieldrefConstant fieldrefConstant;

    @BeforeEach
    public void setUp() {
        repeatTrigger = new MutableBoolean();
        marker = new ReadWriteFieldMarker(repeatTrigger);
        clazz = mock(ProgramClass.class);
        fieldrefConstant = mock(FieldrefConstant.class);
    }

    /**
     * Tests that visitFieldrefConstant calls referencedFieldAccept on the FieldrefConstant.
     * This is the core behavior - delegating to the field reference to visit the actual field.
     */
    @Test
    public void testVisitFieldrefConstant_callsReferencedFieldAccept() {
        // Act
        marker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert
        verify(fieldrefConstant, times(1)).referencedFieldAccept(eq(marker));
    }

    /**
     * Tests that visitFieldrefConstant passes the marker itself as the MemberVisitor.
     */
    @Test
    public void testVisitFieldrefConstant_passesMarkerAsVisitor() {
        // Act
        marker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert - verify the marker itself is passed as the visitor
        verify(fieldrefConstant).referencedFieldAccept(same(marker));
    }

    /**
     * Tests that visitFieldrefConstant works with valid mock objects without throwing exceptions.
     */
    @Test
    public void testVisitFieldrefConstant_withValidMocks_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> marker.visitFieldrefConstant(clazz, fieldrefConstant));
    }

    /**
     * Tests that visitFieldrefConstant can be called with null Clazz parameter.
     * The method should handle null clazz gracefully since it doesn't directly use it.
     */
    @Test
    public void testVisitFieldrefConstant_withNullClazz_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> marker.visitFieldrefConstant(null, fieldrefConstant));

        // Verify the method still calls referencedFieldAccept
        verify(fieldrefConstant, times(1)).referencedFieldAccept(eq(marker));
    }

    /**
     * Tests that visitFieldrefConstant with null FieldrefConstant throws NullPointerException.
     * This should result in NPE since the method calls a method on fieldrefConstant.
     */
    @Test
    public void testVisitFieldrefConstant_withNullFieldrefConstant_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> marker.visitFieldrefConstant(clazz, null));
    }

    /**
     * Tests that visitFieldrefConstant can be called multiple times in succession.
     * Each call should invoke referencedFieldAccept.
     */
    @Test
    public void testVisitFieldrefConstant_calledMultipleTimes_invokesAcceptMethodEachTime() {
        // Act
        marker.visitFieldrefConstant(clazz, fieldrefConstant);
        marker.visitFieldrefConstant(clazz, fieldrefConstant);
        marker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert
        verify(fieldrefConstant, times(3)).referencedFieldAccept(eq(marker));
    }

    /**
     * Tests that visitFieldrefConstant doesn't directly interact with the Clazz parameter.
     * The clazz is passed as a context parameter but not used in this method.
     */
    @Test
    public void testVisitFieldrefConstant_doesNotInteractWithClazz() {
        // Act
        marker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitFieldrefConstant works with different Clazz implementations.
     */
    @Test
    public void testVisitFieldrefConstant_withDifferentClazzTypes_doesNotThrowException() {
        // Arrange
        Clazz programClass = mock(ProgramClass.class);
        Clazz libraryClass = mock(LibraryClass.class);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitFieldrefConstant(programClass, fieldrefConstant));
        assertDoesNotThrow(() -> marker.visitFieldrefConstant(libraryClass, fieldrefConstant));

        // Verify both calls invoked referencedFieldAccept
        verify(fieldrefConstant, times(2)).referencedFieldAccept(eq(marker));
    }

    /**
     * Tests that visitFieldrefConstant works with different FieldrefConstant mock instances.
     */
    @Test
    public void testVisitFieldrefConstant_withDifferentFieldrefConstants_callsEachOne() {
        // Arrange
        FieldrefConstant const1 = mock(FieldrefConstant.class);
        FieldrefConstant const2 = mock(FieldrefConstant.class);
        FieldrefConstant const3 = mock(FieldrefConstant.class);

        // Act
        marker.visitFieldrefConstant(clazz, const1);
        marker.visitFieldrefConstant(clazz, const2);
        marker.visitFieldrefConstant(clazz, const3);

        // Assert - verify each constant's referencedFieldAccept was called
        verify(const1, times(1)).referencedFieldAccept(eq(marker));
        verify(const2, times(1)).referencedFieldAccept(eq(marker));
        verify(const3, times(1)).referencedFieldAccept(eq(marker));
    }

    /**
     * Tests that visitFieldrefConstant doesn't modify the repeatTrigger directly.
     * The trigger would only be set if a field is actually marked, which happens
     * in visitProgramField, not in this method.
     */
    @Test
    public void testVisitFieldrefConstant_doesNotDirectlyModifyRepeatTrigger() {
        // Arrange
        repeatTrigger.reset(); // Ensure it's false

        // Act
        marker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert - trigger should remain unset since this method just delegates
        assertFalse(repeatTrigger.isSet(),
                "visitFieldrefConstant should not directly modify the repeat trigger");
    }

    /**
     * Tests that visitFieldrefConstant works with markers created using different constructors.
     */
    @Test
    public void testVisitFieldrefConstant_withDifferentConstructors_callsReferencedFieldAccept() {
        // Arrange
        MutableBoolean trigger1 = new MutableBoolean();
        MutableBoolean trigger2 = new MutableBoolean();
        MutableBoolean trigger3 = new MutableBoolean();

        ReadWriteFieldMarker marker1 = new ReadWriteFieldMarker(trigger1);
        ReadWriteFieldMarker marker2 = new ReadWriteFieldMarker(trigger2, true, true);
        ReadWriteFieldMarker marker3 = new ReadWriteFieldMarker(trigger3, false, false);

        // Act
        marker1.visitFieldrefConstant(clazz, fieldrefConstant);
        marker2.visitFieldrefConstant(clazz, fieldrefConstant);
        marker3.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert - all should call referencedFieldAccept
        verify(fieldrefConstant, times(3)).referencedFieldAccept(any(ReadWriteFieldMarker.class));
    }

    /**
     * Tests that visitFieldrefConstant with a read-only marker still calls referencedFieldAccept.
     * The marker configuration doesn't affect this delegation.
     */
    @Test
    public void testVisitFieldrefConstant_withReadOnlyMarker_stillCallsAcceptMethod() {
        // Arrange
        MutableBoolean trigger = new MutableBoolean();
        ReadWriteFieldMarker readOnlyMarker = new ReadWriteFieldMarker(trigger, true, false);

        // Act
        readOnlyMarker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert
        verify(fieldrefConstant, times(1)).referencedFieldAccept(eq(readOnlyMarker));
    }

    /**
     * Tests that visitFieldrefConstant with a write-only marker still calls referencedFieldAccept.
     * The marker configuration doesn't affect this delegation.
     */
    @Test
    public void testVisitFieldrefConstant_withWriteOnlyMarker_stillCallsAcceptMethod() {
        // Arrange
        MutableBoolean trigger = new MutableBoolean();
        ReadWriteFieldMarker writeOnlyMarker = new ReadWriteFieldMarker(trigger, false, true);

        // Act
        writeOnlyMarker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert
        verify(fieldrefConstant, times(1)).referencedFieldAccept(eq(writeOnlyMarker));
    }

    /**
     * Tests that visitFieldrefConstant with both flags disabled still calls referencedFieldAccept.
     * The marker configuration doesn't affect this delegation.
     */
    @Test
    public void testVisitFieldrefConstant_withBothFlagsDisabled_stillCallsAcceptMethod() {
        // Arrange
        MutableBoolean trigger = new MutableBoolean();
        ReadWriteFieldMarker disabledMarker = new ReadWriteFieldMarker(trigger, false, false);

        // Act
        disabledMarker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert
        verify(fieldrefConstant, times(1)).referencedFieldAccept(eq(disabledMarker));
    }

    /**
     * Tests that visitFieldrefConstant executes quickly with many calls.
     */
    @Test
    public void testVisitFieldrefConstant_executesQuickly() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - call many times
        for (int i = 0; i < 1000; i++) {
            marker.visitFieldrefConstant(clazz, fieldrefConstant);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100, "visitFieldrefConstant should execute quickly");

        // Verify it was called the expected number of times
        verify(fieldrefConstant, times(1000)).referencedFieldAccept(eq(marker));
    }

    /**
     * Tests that visitFieldrefConstant is thread-safe when called concurrently.
     */
    @Test
    public void testVisitFieldrefConstant_concurrentCalls_doesNotCrash() throws InterruptedException {
        // Arrange
        final int threadCount = 10;
        Thread[] threads = new Thread[threadCount];

        // Act - create threads that call visitFieldrefConstant
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    marker.visitFieldrefConstant(clazz, fieldrefConstant);
                }
            });
            threads[i].start();
        }

        // Wait for completion
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert - verify referencedFieldAccept was called (should be 1000 times)
        verify(fieldrefConstant, atLeast(1000)).referencedFieldAccept(eq(marker));
    }

    /**
     * Tests that multiple markers can call visitFieldrefConstant independently.
     */
    @Test
    public void testVisitFieldrefConstant_withMultipleMarkers_operateIndependently() {
        // Arrange
        MutableBoolean trigger1 = new MutableBoolean();
        MutableBoolean trigger2 = new MutableBoolean();
        ReadWriteFieldMarker marker1 = new ReadWriteFieldMarker(trigger1);
        ReadWriteFieldMarker marker2 = new ReadWriteFieldMarker(trigger2);

        // Act
        marker1.visitFieldrefConstant(clazz, fieldrefConstant);
        marker2.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert
        verify(fieldrefConstant, times(2)).referencedFieldAccept(any(ReadWriteFieldMarker.class));
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitFieldrefConstant can be called in rapid succession without issues.
     */
    @Test
    public void testVisitFieldrefConstant_rapidSuccession_noIssues() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 1000; i++) {
                marker.visitFieldrefConstant(clazz, fieldrefConstant);
            }
        });

        // Verify it was called many times
        verify(fieldrefConstant, times(1000)).referencedFieldAccept(eq(marker));
    }

    /**
     * Tests that visitFieldrefConstant with mixed null and non-null clazz parameters works correctly.
     */
    @Test
    public void testVisitFieldrefConstant_withMixedNullClazz_doesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> marker.visitFieldrefConstant(null, fieldrefConstant));
        assertDoesNotThrow(() -> marker.visitFieldrefConstant(clazz, fieldrefConstant));
        assertDoesNotThrow(() -> marker.visitFieldrefConstant(null, fieldrefConstant));

        // Verify referencedFieldAccept was called three times
        verify(fieldrefConstant, times(3)).referencedFieldAccept(eq(marker));
    }

    /**
     * Tests that visitFieldrefConstant with different Clazz instances for same FieldrefConstant works.
     */
    @Test
    public void testVisitFieldrefConstant_withDifferentClazzInstances_callsSameFieldrefConstant() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Clazz clazz3 = mock(ProgramClass.class);

        // Act
        marker.visitFieldrefConstant(clazz1, fieldrefConstant);
        marker.visitFieldrefConstant(clazz2, fieldrefConstant);
        marker.visitFieldrefConstant(clazz3, fieldrefConstant);

        // Assert
        verify(fieldrefConstant, times(3)).referencedFieldAccept(eq(marker));
        verifyNoInteractions(clazz1);
        verifyNoInteractions(clazz2);
        verifyNoInteractions(clazz3);
    }

    /**
     * Tests that visitFieldrefConstant completes without blocking or hanging.
     */
    @Test
    public void testVisitFieldrefConstant_completesImmediately() {
        // Arrange
        long timeoutNanos = 1_000_000; // 1 millisecond
        long startTime = System.nanoTime();

        // Act
        marker.visitFieldrefConstant(clazz, fieldrefConstant);

        long duration = System.nanoTime() - startTime;

        // Assert
        assertTrue(duration < timeoutNanos,
                "visitFieldrefConstant should complete immediately, took " + duration + " nanoseconds");
    }

    /**
     * Tests that visitFieldrefConstant called after other operations still works correctly.
     */
    @Test
    public void testVisitFieldrefConstant_afterOtherOperations_stillWorks() {
        // Arrange - call other visitor methods first
        marker.visitAnyInstruction(null, null, null, 0, null);
        marker.visitAnyConstant(null, null);

        // Act
        marker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert
        verify(fieldrefConstant, times(1)).referencedFieldAccept(eq(marker));
    }

    /**
     * Tests that visitFieldrefConstant doesn't affect subsequent calls to itself.
     */
    @Test
    public void testVisitFieldrefConstant_doesNotAffectSubsequentCalls() {
        // Act
        marker.visitFieldrefConstant(clazz, fieldrefConstant);
        marker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert - each call should be independent
        verify(fieldrefConstant, times(2)).referencedFieldAccept(eq(marker));
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitFieldrefConstant with the same FieldrefConstant multiple times
     * calls referencedFieldAccept each time.
     */
    @Test
    public void testVisitFieldrefConstant_sameConstantMultipleTimes_callsAcceptEachTime() {
        // Act - call multiple times with the same constant
        for (int i = 0; i < 10; i++) {
            marker.visitFieldrefConstant(clazz, fieldrefConstant);
        }

        // Assert
        verify(fieldrefConstant, times(10)).referencedFieldAccept(eq(marker));
    }

    /**
     * Tests that visitFieldrefConstant handles sequential calls with different constants correctly.
     */
    @Test
    public void testVisitFieldrefConstant_sequentialCallsWithDifferentConstants_callsEachCorrectly() {
        // Arrange
        FieldrefConstant const1 = mock(FieldrefConstant.class);
        FieldrefConstant const2 = mock(FieldrefConstant.class);

        // Act
        marker.visitFieldrefConstant(clazz, const1);
        marker.visitFieldrefConstant(clazz, const1);
        marker.visitFieldrefConstant(clazz, const2);
        marker.visitFieldrefConstant(clazz, const2);
        marker.visitFieldrefConstant(clazz, const2);

        // Assert
        verify(const1, times(2)).referencedFieldAccept(eq(marker));
        verify(const2, times(3)).referencedFieldAccept(eq(marker));
    }

    /**
     * Tests that the method signature matches the ConstantVisitor interface.
     */
    @Test
    public void testVisitFieldrefConstant_implementsInterfaceCorrectly() {
        // Assert
        assertTrue(marker instanceof proguard.classfile.constant.visitor.ConstantVisitor,
                "ReadWriteFieldMarker should implement ConstantVisitor");
    }

    /**
     * Tests that visitFieldrefConstant with both null and non-null parameters in sequence.
     */
    @Test
    public void testVisitFieldrefConstant_mixedNullAndNonNullSequence_handlesCorrectly() {
        // Arrange
        FieldrefConstant const1 = mock(FieldrefConstant.class);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitFieldrefConstant(clazz, const1));
        assertDoesNotThrow(() -> marker.visitFieldrefConstant(null, const1));
        assertDoesNotThrow(() -> marker.visitFieldrefConstant(clazz, const1));

        // Verify all valid calls worked
        verify(const1, times(3)).referencedFieldAccept(eq(marker));
    }

    /**
     * Tests that visitFieldrefConstant doesn't accumulate state between calls.
     */
    @Test
    public void testVisitFieldrefConstant_noStateAccumulation() {
        // Arrange
        FieldrefConstant const1 = mock(FieldrefConstant.class);

        // Act - call multiple times
        for (int i = 0; i < 5; i++) {
            marker.visitFieldrefConstant(clazz, const1);
        }

        // Assert - each call should be independent, no state accumulation
        verify(const1, times(5)).referencedFieldAccept(eq(marker));
        assertFalse(repeatTrigger.isSet(), "No state should accumulate in the marker");
    }

    /**
     * Tests that visitFieldrefConstant returns immediately without performing complex operations.
     * It just delegates to the FieldrefConstant.
     */
    @Test
    public void testVisitFieldrefConstant_justDelegates_noComplexLogic() {
        // Act
        marker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert - only one interaction with fieldrefConstant
        verify(fieldrefConstant, times(1)).referencedFieldAccept(eq(marker));
        verifyNoMoreInteractions(fieldrefConstant);
    }

    /**
     * Tests that visitFieldrefConstant is called as part of the constant visitor chain.
     * This method is a key part of how field references are tracked.
     */
    @Test
    public void testVisitFieldrefConstant_partOfVisitorChain() {
        // Arrange
        FieldrefConstant fieldref1 = mock(FieldrefConstant.class);
        FieldrefConstant fieldref2 = mock(FieldrefConstant.class);

        // Act - simulate visiting multiple field references
        marker.visitFieldrefConstant(clazz, fieldref1);
        marker.visitFieldrefConstant(clazz, fieldref2);

        // Assert - both field references should be visited
        verify(fieldref1, times(1)).referencedFieldAccept(eq(marker));
        verify(fieldref2, times(1)).referencedFieldAccept(eq(marker));
    }

    /**
     * Tests that visitFieldrefConstant works correctly when called alternately with other visitor methods.
     */
    @Test
    public void testVisitFieldrefConstant_alternatingWithOtherMethods_worksCorrectly() {
        // Arrange
        FieldrefConstant const1 = mock(FieldrefConstant.class);

        // Act - alternate between different visitor methods
        marker.visitFieldrefConstant(clazz, const1);
        marker.visitAnyConstant(null, null);
        marker.visitFieldrefConstant(clazz, const1);

        // Assert
        verify(const1, times(2)).referencedFieldAccept(eq(marker));
    }

    /**
     * Tests that visitFieldrefConstant doesn't modify any marker internal state directly.
     * State changes only occur in visitProgramField based on reading/writing flags.
     */
    @Test
    public void testVisitFieldrefConstant_doesNotModifyInternalState() {
        // Arrange
        repeatTrigger.reset();

        // Act
        marker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert - internal state should remain unchanged
        assertFalse(repeatTrigger.isSet(), "Internal state should not change in this method");
        verifyNoInteractions(clazz);
    }
}
