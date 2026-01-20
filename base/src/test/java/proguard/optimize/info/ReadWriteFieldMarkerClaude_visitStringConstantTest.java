package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.LibraryClass;
import proguard.classfile.constant.StringConstant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ReadWriteFieldMarker#visitStringConstant(Clazz, StringConstant)}.
 *
 * The visitStringConstant method calls referencedMemberAccept on the StringConstant,
 * which allows the marker to process any field references contained in the string constant.
 * This is important for tracking field access through mechanisms like reflection where
 * field names are passed as strings.
 *
 * The method does NOT directly mark fields - instead it delegates to referencedMemberAccept,
 * which will eventually call visitProgramField if the string references a field.
 */
public class ReadWriteFieldMarkerClaude_visitStringConstantTest {

    private ReadWriteFieldMarker marker;
    private MutableBoolean repeatTrigger;
    private Clazz clazz;
    private StringConstant stringConstant;

    @BeforeEach
    public void setUp() {
        repeatTrigger = new MutableBoolean();
        marker = new ReadWriteFieldMarker(repeatTrigger);
        clazz = mock(ProgramClass.class);
        stringConstant = mock(StringConstant.class);
    }

    /**
     * Tests that visitStringConstant calls referencedMemberAccept on the StringConstant.
     * This is the core behavior - delegating to the string constant to visit any referenced field.
     */
    @Test
    public void testVisitStringConstant_callsReferencedMemberAccept() {
        // Act
        marker.visitStringConstant(clazz, stringConstant);

        // Assert
        verify(stringConstant, times(1)).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitStringConstant passes the marker itself as the MemberVisitor.
     */
    @Test
    public void testVisitStringConstant_passesMarkerAsVisitor() {
        // Act
        marker.visitStringConstant(clazz, stringConstant);

        // Assert - verify the marker itself is passed as the visitor
        verify(stringConstant).referencedMemberAccept(same(marker));
    }

    /**
     * Tests that visitStringConstant works with valid mock objects without throwing exceptions.
     */
    @Test
    public void testVisitStringConstant_withValidMocks_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> marker.visitStringConstant(clazz, stringConstant));
    }

    /**
     * Tests that visitStringConstant can be called with null Clazz parameter.
     * The method should handle null clazz gracefully since it doesn't directly use it.
     */
    @Test
    public void testVisitStringConstant_withNullClazz_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> marker.visitStringConstant(null, stringConstant));

        // Verify the method still calls referencedMemberAccept
        verify(stringConstant, times(1)).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitStringConstant with null StringConstant throws NullPointerException.
     * This should result in NPE since the method calls a method on stringConstant.
     */
    @Test
    public void testVisitStringConstant_withNullStringConstant_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> marker.visitStringConstant(clazz, null));
    }

    /**
     * Tests that visitStringConstant can be called multiple times in succession.
     * Each call should invoke referencedMemberAccept.
     */
    @Test
    public void testVisitStringConstant_calledMultipleTimes_invokesAcceptMethodEachTime() {
        // Act
        marker.visitStringConstant(clazz, stringConstant);
        marker.visitStringConstant(clazz, stringConstant);
        marker.visitStringConstant(clazz, stringConstant);

        // Assert
        verify(stringConstant, times(3)).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitStringConstant doesn't directly interact with the Clazz parameter.
     * The clazz is passed as a context parameter but not used in this method.
     */
    @Test
    public void testVisitStringConstant_doesNotInteractWithClazz() {
        // Act
        marker.visitStringConstant(clazz, stringConstant);

        // Assert
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitStringConstant works with different Clazz implementations.
     */
    @Test
    public void testVisitStringConstant_withDifferentClazzTypes_doesNotThrowException() {
        // Arrange
        Clazz programClass = mock(ProgramClass.class);
        Clazz libraryClass = mock(LibraryClass.class);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitStringConstant(programClass, stringConstant));
        assertDoesNotThrow(() -> marker.visitStringConstant(libraryClass, stringConstant));

        // Verify both calls invoked referencedMemberAccept
        verify(stringConstant, times(2)).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitStringConstant works with different StringConstant mock instances.
     */
    @Test
    public void testVisitStringConstant_withDifferentStringConstants_callsEachOne() {
        // Arrange
        StringConstant const1 = mock(StringConstant.class);
        StringConstant const2 = mock(StringConstant.class);
        StringConstant const3 = mock(StringConstant.class);

        // Act
        marker.visitStringConstant(clazz, const1);
        marker.visitStringConstant(clazz, const2);
        marker.visitStringConstant(clazz, const3);

        // Assert - verify each constant's referencedMemberAccept was called
        verify(const1, times(1)).referencedMemberAccept(eq(marker));
        verify(const2, times(1)).referencedMemberAccept(eq(marker));
        verify(const3, times(1)).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitStringConstant doesn't modify the repeatTrigger directly.
     * The trigger would only be set if a field is actually marked, which happens
     * in visitProgramField, not in this method.
     */
    @Test
    public void testVisitStringConstant_doesNotDirectlyModifyRepeatTrigger() {
        // Arrange
        repeatTrigger.reset(); // Ensure it's false

        // Act
        marker.visitStringConstant(clazz, stringConstant);

        // Assert - trigger should remain unset since this method just delegates
        assertFalse(repeatTrigger.isSet(),
                "visitStringConstant should not directly modify the repeat trigger");
    }

    /**
     * Tests that visitStringConstant works with markers created using different constructors.
     */
    @Test
    public void testVisitStringConstant_withDifferentConstructors_callsReferencedMemberAccept() {
        // Arrange
        MutableBoolean trigger1 = new MutableBoolean();
        MutableBoolean trigger2 = new MutableBoolean();
        MutableBoolean trigger3 = new MutableBoolean();

        ReadWriteFieldMarker marker1 = new ReadWriteFieldMarker(trigger1);
        ReadWriteFieldMarker marker2 = new ReadWriteFieldMarker(trigger2, true, true);
        ReadWriteFieldMarker marker3 = new ReadWriteFieldMarker(trigger3, false, false);

        // Act
        marker1.visitStringConstant(clazz, stringConstant);
        marker2.visitStringConstant(clazz, stringConstant);
        marker3.visitStringConstant(clazz, stringConstant);

        // Assert - all should call referencedMemberAccept
        verify(stringConstant, times(3)).referencedMemberAccept(any(ReadWriteFieldMarker.class));
    }

    /**
     * Tests that visitStringConstant with a read-only marker still calls referencedMemberAccept.
     * The marker configuration doesn't affect this delegation.
     */
    @Test
    public void testVisitStringConstant_withReadOnlyMarker_stillCallsAcceptMethod() {
        // Arrange
        MutableBoolean trigger = new MutableBoolean();
        ReadWriteFieldMarker readOnlyMarker = new ReadWriteFieldMarker(trigger, true, false);

        // Act
        readOnlyMarker.visitStringConstant(clazz, stringConstant);

        // Assert
        verify(stringConstant, times(1)).referencedMemberAccept(eq(readOnlyMarker));
    }

    /**
     * Tests that visitStringConstant with a write-only marker still calls referencedMemberAccept.
     * The marker configuration doesn't affect this delegation.
     */
    @Test
    public void testVisitStringConstant_withWriteOnlyMarker_stillCallsAcceptMethod() {
        // Arrange
        MutableBoolean trigger = new MutableBoolean();
        ReadWriteFieldMarker writeOnlyMarker = new ReadWriteFieldMarker(trigger, false, true);

        // Act
        writeOnlyMarker.visitStringConstant(clazz, stringConstant);

        // Assert
        verify(stringConstant, times(1)).referencedMemberAccept(eq(writeOnlyMarker));
    }

    /**
     * Tests that visitStringConstant with both flags disabled still calls referencedMemberAccept.
     * The marker configuration doesn't affect this delegation.
     */
    @Test
    public void testVisitStringConstant_withBothFlagsDisabled_stillCallsAcceptMethod() {
        // Arrange
        MutableBoolean trigger = new MutableBoolean();
        ReadWriteFieldMarker disabledMarker = new ReadWriteFieldMarker(trigger, false, false);

        // Act
        disabledMarker.visitStringConstant(clazz, stringConstant);

        // Assert
        verify(stringConstant, times(1)).referencedMemberAccept(eq(disabledMarker));
    }

    /**
     * Tests that visitStringConstant executes quickly with many calls.
     */
    @Test
    public void testVisitStringConstant_executesQuickly() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - call many times
        for (int i = 0; i < 1000; i++) {
            marker.visitStringConstant(clazz, stringConstant);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100, "visitStringConstant should execute quickly");

        // Verify it was called the expected number of times
        verify(stringConstant, times(1000)).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitStringConstant is thread-safe when called concurrently.
     */
    @Test
    public void testVisitStringConstant_concurrentCalls_doesNotCrash() throws InterruptedException {
        // Arrange
        final int threadCount = 10;
        Thread[] threads = new Thread[threadCount];

        // Act - create threads that call visitStringConstant
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    marker.visitStringConstant(clazz, stringConstant);
                }
            });
            threads[i].start();
        }

        // Wait for completion
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert - verify referencedMemberAccept was called (should be 1000 times)
        verify(stringConstant, atLeast(1000)).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that multiple markers can call visitStringConstant independently.
     */
    @Test
    public void testVisitStringConstant_withMultipleMarkers_operateIndependently() {
        // Arrange
        MutableBoolean trigger1 = new MutableBoolean();
        MutableBoolean trigger2 = new MutableBoolean();
        ReadWriteFieldMarker marker1 = new ReadWriteFieldMarker(trigger1);
        ReadWriteFieldMarker marker2 = new ReadWriteFieldMarker(trigger2);

        // Act
        marker1.visitStringConstant(clazz, stringConstant);
        marker2.visitStringConstant(clazz, stringConstant);

        // Assert
        verify(stringConstant, times(2)).referencedMemberAccept(any(ReadWriteFieldMarker.class));
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitStringConstant can be called in rapid succession without issues.
     */
    @Test
    public void testVisitStringConstant_rapidSuccession_noIssues() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 1000; i++) {
                marker.visitStringConstant(clazz, stringConstant);
            }
        });

        // Verify it was called many times
        verify(stringConstant, times(1000)).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitStringConstant with mixed null and non-null clazz parameters works correctly.
     */
    @Test
    public void testVisitStringConstant_withMixedNullClazz_doesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> marker.visitStringConstant(null, stringConstant));
        assertDoesNotThrow(() -> marker.visitStringConstant(clazz, stringConstant));
        assertDoesNotThrow(() -> marker.visitStringConstant(null, stringConstant));

        // Verify referencedMemberAccept was called three times
        verify(stringConstant, times(3)).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitStringConstant doesn't call referencedClassAccept.
     * Unlike some other markers, ReadWriteFieldMarker only cares about member references.
     */
    @Test
    public void testVisitStringConstant_doesNotCallReferencedClassAccept() {
        // Act
        marker.visitStringConstant(clazz, stringConstant);

        // Assert - verify only referencedMemberAccept is called, not referencedClassAccept
        verify(stringConstant, times(1)).referencedMemberAccept(eq(marker));
        verify(stringConstant, never()).referencedClassAccept(any());
    }

    /**
     * Tests that visitStringConstant with different Clazz instances for same StringConstant works.
     */
    @Test
    public void testVisitStringConstant_withDifferentClazzInstances_callsSameStringConstant() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Clazz clazz3 = mock(ProgramClass.class);

        // Act
        marker.visitStringConstant(clazz1, stringConstant);
        marker.visitStringConstant(clazz2, stringConstant);
        marker.visitStringConstant(clazz3, stringConstant);

        // Assert
        verify(stringConstant, times(3)).referencedMemberAccept(eq(marker));
        verifyNoInteractions(clazz1);
        verifyNoInteractions(clazz2);
        verifyNoInteractions(clazz3);
    }

    /**
     * Tests that visitStringConstant completes without blocking or hanging.
     */
    @Test
    public void testVisitStringConstant_completesImmediately() {
        // Arrange
        long timeoutNanos = 1_000_000; // 1 millisecond
        long startTime = System.nanoTime();

        // Act
        marker.visitStringConstant(clazz, stringConstant);

        long duration = System.nanoTime() - startTime;

        // Assert
        assertTrue(duration < timeoutNanos,
                "visitStringConstant should complete immediately, took " + duration + " nanoseconds");
    }

    /**
     * Tests that visitStringConstant called after other operations still works correctly.
     */
    @Test
    public void testVisitStringConstant_afterOtherOperations_stillWorks() {
        // Arrange - call other visitor methods first
        marker.visitAnyInstruction(null, null, null, 0, null);
        marker.visitAnyConstant(null, null);

        // Act
        marker.visitStringConstant(clazz, stringConstant);

        // Assert
        verify(stringConstant, times(1)).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitStringConstant doesn't affect subsequent calls to itself.
     */
    @Test
    public void testVisitStringConstant_doesNotAffectSubsequentCalls() {
        // Act
        marker.visitStringConstant(clazz, stringConstant);
        marker.visitStringConstant(clazz, stringConstant);

        // Assert - each call should be independent
        verify(stringConstant, times(2)).referencedMemberAccept(eq(marker));
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitStringConstant with the same StringConstant multiple times
     * calls referencedMemberAccept each time.
     */
    @Test
    public void testVisitStringConstant_sameConstantMultipleTimes_callsAcceptEachTime() {
        // Act - call multiple times with the same constant
        for (int i = 0; i < 10; i++) {
            marker.visitStringConstant(clazz, stringConstant);
        }

        // Assert
        verify(stringConstant, times(10)).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitStringConstant handles sequential calls with different constants correctly.
     */
    @Test
    public void testVisitStringConstant_sequentialCallsWithDifferentConstants_callsEachCorrectly() {
        // Arrange
        StringConstant const1 = mock(StringConstant.class);
        StringConstant const2 = mock(StringConstant.class);

        // Act
        marker.visitStringConstant(clazz, const1);
        marker.visitStringConstant(clazz, const1);
        marker.visitStringConstant(clazz, const2);
        marker.visitStringConstant(clazz, const2);
        marker.visitStringConstant(clazz, const2);

        // Assert
        verify(const1, times(2)).referencedMemberAccept(eq(marker));
        verify(const2, times(3)).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that the method signature matches the ConstantVisitor interface.
     */
    @Test
    public void testVisitStringConstant_implementsInterfaceCorrectly() {
        // Assert
        assertTrue(marker instanceof proguard.classfile.constant.visitor.ConstantVisitor,
                "ReadWriteFieldMarker should implement ConstantVisitor");
    }

    /**
     * Tests that visitStringConstant with both null and non-null parameters in sequence.
     */
    @Test
    public void testVisitStringConstant_mixedNullAndNonNullSequence_handlesCorrectly() {
        // Arrange
        StringConstant const1 = mock(StringConstant.class);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitStringConstant(clazz, const1));
        assertDoesNotThrow(() -> marker.visitStringConstant(null, const1));
        assertDoesNotThrow(() -> marker.visitStringConstant(clazz, const1));

        // Verify all valid calls worked
        verify(const1, times(3)).referencedMemberAccept(eq(marker));
    }

    /**
     * Tests that visitStringConstant doesn't accumulate state between calls.
     */
    @Test
    public void testVisitStringConstant_noStateAccumulation() {
        // Arrange
        StringConstant const1 = mock(StringConstant.class);

        // Act - call multiple times
        for (int i = 0; i < 5; i++) {
            marker.visitStringConstant(clazz, const1);
        }

        // Assert - each call should be independent, no state accumulation
        verify(const1, times(5)).referencedMemberAccept(eq(marker));
        assertFalse(repeatTrigger.isSet(), "No state should accumulate in the marker");
    }

    /**
     * Tests that visitStringConstant returns immediately without performing complex operations.
     * It just delegates to the StringConstant.
     */
    @Test
    public void testVisitStringConstant_justDelegates_noComplexLogic() {
        // Act
        marker.visitStringConstant(clazz, stringConstant);

        // Assert - only one interaction with stringConstant
        verify(stringConstant, times(1)).referencedMemberAccept(eq(marker));
        verifyNoMoreInteractions(stringConstant);
    }
}
