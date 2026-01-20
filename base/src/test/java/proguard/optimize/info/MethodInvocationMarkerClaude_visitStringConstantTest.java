package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.LibraryClass;
import proguard.classfile.constant.StringConstant;
import proguard.classfile.visitor.MemberVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link MethodInvocationMarker#visitStringConstant(Clazz, StringConstant)}.
 *
 * The visitStringConstant method processes string constants that may reference methods.
 * It delegates to the string constant's referencedMemberAccept method, passing a filtered
 * method marker that only processes methods with optimization info. This allows the marker
 * to track method invocations that occur through string constant references (e.g., for reflection
 * or method handles).
 */
public class MethodInvocationMarkerClaude_visitStringConstantTest {

    private MethodInvocationMarker marker;
    private Clazz clazz;
    private StringConstant stringConstant;

    @BeforeEach
    public void setUp() {
        marker = new MethodInvocationMarker();
        clazz = mock(ProgramClass.class);
        stringConstant = mock(StringConstant.class);
    }

    /**
     * Tests that visitStringConstant calls referencedMemberAccept on the StringConstant.
     * This is the core behavior - the method should delegate to the string constant to visit
     * any referenced member (method) through the filtered method marker.
     */
    @Test
    public void testVisitStringConstant_callsReferencedMemberAccept() {
        // Act
        marker.visitStringConstant(clazz, stringConstant);

        // Assert - verify that referencedMemberAccept was called with a MemberVisitor
        verify(stringConstant, times(1)).referencedMemberAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitStringConstant passes a MemberVisitor to referencedMemberAccept.
     * The visitor should be the filteredMethodMarker (an OptimizationInfoMemberFilter wrapping the marker).
     */
    @Test
    public void testVisitStringConstant_passesFilteredMethodMarkerAsVisitor() {
        // Act
        marker.visitStringConstant(clazz, stringConstant);

        // Assert - verify a MemberVisitor was passed (the filteredMethodMarker)
        verify(stringConstant).referencedMemberAccept(isA(MemberVisitor.class));
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
        verify(stringConstant, times(1)).referencedMemberAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitStringConstant with null StringConstant throws NullPointerException.
     * This should result in a NullPointerException since the method calls methods on stringConstant.
     */
    @Test
    public void testVisitStringConstant_withNullStringConstant_throwsNullPointerException() {
        // Act & Assert - should throw NullPointerException
        assertThrows(NullPointerException.class,
            () -> marker.visitStringConstant(clazz, null));
    }

    /**
     * Tests that visitStringConstant can be called multiple times in succession.
     * Each call should invoke referencedMemberAccept.
     */
    @Test
    public void testVisitStringConstant_calledMultipleTimes_invokesReferencedMemberAcceptEachTime() {
        // Act
        marker.visitStringConstant(clazz, stringConstant);
        marker.visitStringConstant(clazz, stringConstant);
        marker.visitStringConstant(clazz, stringConstant);

        // Assert - verify referencedMemberAccept was called three times
        verify(stringConstant, times(3)).referencedMemberAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitStringConstant doesn't directly interact with the Clazz parameter.
     * The clazz is passed as a context parameter but not used in this method.
     */
    @Test
    public void testVisitStringConstant_doesNotInteractWithClazz() {
        // Act
        marker.visitStringConstant(clazz, stringConstant);

        // Assert - verify no interactions occurred with the clazz mock
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitStringConstant can be used as part of the ConstantVisitor interface.
     * Verifies integration with the visitor pattern.
     */
    @Test
    public void testVisitStringConstant_usedAsConstantVisitor_worksCorrectly() {
        // Arrange
        proguard.classfile.constant.visitor.ConstantVisitor visitor = marker;

        // Act & Assert
        assertDoesNotThrow(() -> visitor.visitStringConstant(clazz, stringConstant));
        verify(stringConstant, times(1)).referencedMemberAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitStringConstant can be called with real ProgramClass instance.
     * Verifies the method works with actual class instances, not just mocks.
     */
    @Test
    public void testVisitStringConstant_withRealProgramClass_doesNotThrowException() {
        // Arrange
        ProgramClass realClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitStringConstant(realClass, stringConstant));
        verify(stringConstant, times(1)).referencedMemberAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitStringConstant can be called with real LibraryClass instance.
     * Verifies the method works with library classes.
     */
    @Test
    public void testVisitStringConstant_withRealLibraryClass_doesNotThrowException() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitStringConstant(libraryClass, stringConstant));
        verify(stringConstant, times(1)).referencedMemberAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitStringConstant works with different StringConstant instances.
     */
    @Test
    public void testVisitStringConstant_withDifferentStringConstants_callsAcceptOnEach() {
        // Arrange
        StringConstant stringConstant1 = mock(StringConstant.class);
        StringConstant stringConstant2 = mock(StringConstant.class);
        StringConstant stringConstant3 = mock(StringConstant.class);

        // Act
        marker.visitStringConstant(clazz, stringConstant1);
        marker.visitStringConstant(clazz, stringConstant2);
        marker.visitStringConstant(clazz, stringConstant3);

        // Assert - verify each string constant had referencedMemberAccept called
        verify(stringConstant1).referencedMemberAccept(any(MemberVisitor.class));
        verify(stringConstant2).referencedMemberAccept(any(MemberVisitor.class));
        verify(stringConstant3).referencedMemberAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that multiple MethodInvocationMarker instances work independently.
     */
    @Test
    public void testVisitStringConstant_withMultipleMarkers_operateIndependently() {
        // Arrange
        MethodInvocationMarker marker1 = new MethodInvocationMarker();
        MethodInvocationMarker marker2 = new MethodInvocationMarker();
        StringConstant stringConstant1 = mock(StringConstant.class);
        StringConstant stringConstant2 = mock(StringConstant.class);

        // Act
        marker1.visitStringConstant(clazz, stringConstant1);
        marker2.visitStringConstant(clazz, stringConstant2);

        // Assert - verify each marker processed its respective constant
        verify(stringConstant1).referencedMemberAccept(any(MemberVisitor.class));
        verify(stringConstant2).referencedMemberAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitStringConstant verifies marker implements MemberVisitor.
     */
    @Test
    public void testVisitStringConstant_markerImplementsMemberVisitor() {
        // Assert - verify marker is a MemberVisitor
        assertTrue(marker instanceof MemberVisitor,
                "MethodInvocationMarker should implement MemberVisitor");
    }

    /**
     * Tests that visitStringConstant handles the case where stringConstant has no referenced member.
     * The method should still attempt to call referencedMemberAccept.
     */
    @Test
    public void testVisitStringConstant_withNoReferencedMember_stillCallsReferencedMemberAccept() {
        // Arrange
        StringConstant stringConstantNoMember = mock(StringConstant.class);
        doNothing().when(stringConstantNoMember).referencedMemberAccept(any(MemberVisitor.class));

        // Act
        marker.visitStringConstant(clazz, stringConstantNoMember);

        // Assert - verify the method was called
        verify(stringConstantNoMember).referencedMemberAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitStringConstant can handle rapid successive calls.
     */
    @Test
    public void testVisitStringConstant_rapidSuccessiveCalls_allProcessed() {
        // Act
        for (int i = 0; i < 100; i++) {
            marker.visitStringConstant(clazz, stringConstant);
        }

        // Assert
        verify(stringConstant, times(100)).referencedMemberAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitStringConstant works correctly with different clazz instances.
     */
    @Test
    public void testVisitStringConstant_withDifferentClazzInstances_worksCorrectly() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(LibraryClass.class);

        // Act
        marker.visitStringConstant(clazz1, stringConstant);
        marker.visitStringConstant(clazz2, stringConstant);

        // Assert - verify both calls processed the string constant
        verify(stringConstant, times(2)).referencedMemberAccept(any(MemberVisitor.class));

        // Verify neither clazz was interacted with
        verifyNoInteractions(clazz1);
        verifyNoInteractions(clazz2);
    }

    /**
     * Tests that visitStringConstant only interacts with the StringConstant parameter.
     */
    @Test
    public void testVisitStringConstant_onlyInteractsWithStringConstant() {
        // Act
        marker.visitStringConstant(clazz, stringConstant);

        // Assert - verify only stringConstant was interacted with
        verify(stringConstant).referencedMemberAccept(any(MemberVisitor.class));
        verifyNoMoreInteractions(stringConstant);
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitStringConstant can be invoked through the ConstantVisitor interface.
     */
    @Test
    public void testVisitStringConstant_throughConstantVisitorInterface_worksCorrectly() {
        // Arrange - use marker as ConstantVisitor
        proguard.classfile.constant.visitor.ConstantVisitor constantVisitor = marker;

        // Act
        constantVisitor.visitStringConstant(clazz, stringConstant);

        // Assert
        verify(stringConstant).referencedMemberAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitStringConstant with both null clazz and different string constants works.
     */
    @Test
    public void testVisitStringConstant_withNullClazzAndDifferentStringConstants_worksCorrectly() {
        // Arrange
        StringConstant sc1 = mock(StringConstant.class);
        StringConstant sc2 = mock(StringConstant.class);

        // Act
        marker.visitStringConstant(null, sc1);
        marker.visitStringConstant(null, sc2);

        // Assert
        verify(sc1).referencedMemberAccept(any(MemberVisitor.class));
        verify(sc2).referencedMemberAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitStringConstant preserves the marker's visitor implementation.
     * This ensures the marker can properly visit referenced members through the filtered visitor.
     */
    @Test
    public void testVisitStringConstant_preservesVisitorImplementation() {
        // Act
        marker.visitStringConstant(clazz, stringConstant);

        // Assert - verify the filtered marker was passed as MemberVisitor
        verify(stringConstant).referencedMemberAccept(isA(MemberVisitor.class));
    }

    /**
     * Tests that visitStringConstant works correctly when called on the same
     * string constant multiple times with different clazz instances.
     */
    @Test
    public void testVisitStringConstant_sameStringConstantDifferentClazz_allProcessed() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Clazz clazz3 = mock(LibraryClass.class);

        // Act
        marker.visitStringConstant(clazz1, stringConstant);
        marker.visitStringConstant(clazz2, stringConstant);
        marker.visitStringConstant(clazz3, stringConstant);

        // Assert
        verify(stringConstant, times(3)).referencedMemberAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitStringConstant correctly delegates to the string constant
     * without attempting to perform the member visitation itself.
     */
    @Test
    public void testVisitStringConstant_delegatesToStringConstant_doesNotVisitDirectly() {
        // Act
        marker.visitStringConstant(clazz, stringConstant);

        // Assert - only the string constant should be interacted with
        verify(stringConstant).referencedMemberAccept(any(MemberVisitor.class));

        // The clazz parameter should not be touched
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitStringConstant does NOT call referencedClassAccept.
     * Unlike AccessMethodMarker, MethodInvocationMarker only cares about referenced members (methods).
     */
    @Test
    public void testVisitStringConstant_doesNotCallReferencedClassAccept() {
        // Act
        marker.visitStringConstant(clazz, stringConstant);

        // Assert - verify referencedClassAccept was NOT called
        verify(stringConstant, never()).referencedClassAccept(any());
        // But referencedMemberAccept should be called
        verify(stringConstant).referencedMemberAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitStringConstant uses the filteredMethodMarker, not the marker itself.
     * This is important because the filtered marker only processes methods with optimization info.
     */
    @Test
    public void testVisitStringConstant_usesFilteredMethodMarker_notMarkerDirectly() {
        // Act
        marker.visitStringConstant(clazz, stringConstant);

        // Assert - verify a MemberVisitor was passed (which is the filteredMethodMarker)
        // We can't directly verify it's the filteredMethodMarker, but we can verify it's a MemberVisitor
        verify(stringConstant).referencedMemberAccept(isA(MemberVisitor.class));
    }

    /**
     * Tests that visitStringConstant is stateless and can be called repeatedly
     * without affecting subsequent calls.
     */
    @Test
    public void testVisitStringConstant_isStateless_repeatedCallsIndependent() {
        // Arrange
        StringConstant sc1 = mock(StringConstant.class);
        StringConstant sc2 = mock(StringConstant.class);

        // Act - call multiple times
        marker.visitStringConstant(clazz, sc1);
        marker.visitStringConstant(clazz, sc2);
        marker.visitStringConstant(clazz, sc1);

        // Assert - verify each call was independent
        verify(sc1, times(2)).referencedMemberAccept(any(MemberVisitor.class));
        verify(sc2, times(1)).referencedMemberAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitStringConstant works correctly with empty string constants
     * (constants that don't reference any methods).
     */
    @Test
    public void testVisitStringConstant_withEmptyStringConstant_stillCallsAccept() {
        // Arrange - string constant with no referenced member
        StringConstant emptyStringConstant = mock(StringConstant.class);
        doNothing().when(emptyStringConstant).referencedMemberAccept(any(MemberVisitor.class));

        // Act
        marker.visitStringConstant(clazz, emptyStringConstant);

        // Assert - method should still be called even if there's no referenced member
        verify(emptyStringConstant).referencedMemberAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitStringConstant executes quickly as it's a simple delegation.
     */
    @Test
    public void testVisitStringConstant_executesQuickly() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            marker.visitStringConstant(clazz, stringConstant);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100, "visitStringConstant should execute quickly");
    }

    /**
     * Tests that visitStringConstant is thread-safe when called concurrently.
     * Since the method uses an instance field (filteredMethodMarker), it should still be thread-safe.
     */
    @Test
    public void testVisitStringConstant_concurrentCalls_noExceptions() throws InterruptedException {
        // Arrange
        final int threadCount = 10;
        Thread[] threads = new Thread[threadCount];

        // Act - create multiple threads that call visitStringConstant
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    marker.visitStringConstant(clazz, stringConstant);
                }
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert - verify referencedMemberAccept was called the expected number of times
        verify(stringConstant, times(1000)).referencedMemberAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitStringConstant handles string constants that reference methods.
     * This is the primary use case for this visitor method.
     */
    @Test
    public void testVisitStringConstant_withMethodReference_callsReferencedMemberAccept() {
        // Arrange - string constant that references a method
        StringConstant methodRefStringConstant = mock(StringConstant.class);

        // Act
        marker.visitStringConstant(clazz, methodRefStringConstant);

        // Assert - verify the method reference was processed
        verify(methodRefStringConstant).referencedMemberAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitStringConstant processes method references for invocation counting.
     * This verifies the method's purpose in the context of MethodInvocationMarker.
     */
    @Test
    public void testVisitStringConstant_processesMethodReferencesForInvocationCounting() {
        // Arrange
        StringConstant stringConstWithMethodRef = mock(StringConstant.class);

        // Act - this should trigger processing of the referenced method
        marker.visitStringConstant(clazz, stringConstWithMethodRef);

        // Assert - verify the string constant was asked to accept a member visitor
        verify(stringConstWithMethodRef).referencedMemberAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitStringConstant can handle string constants from different sources.
     */
    @Test
    public void testVisitStringConstant_withStringConstantsFromDifferentSources_allProcessed() {
        // Arrange
        StringConstant stringConst1 = mock(StringConstant.class, "stringConst1");
        StringConstant stringConst2 = mock(StringConstant.class, "stringConst2");
        StringConstant stringConst3 = mock(StringConstant.class, "stringConst3");

        // Act
        marker.visitStringConstant(clazz, stringConst1);
        marker.visitStringConstant(clazz, stringConst2);
        marker.visitStringConstant(clazz, stringConst3);

        // Assert - verify all were processed
        verify(stringConst1).referencedMemberAccept(any(MemberVisitor.class));
        verify(stringConst2).referencedMemberAccept(any(MemberVisitor.class));
        verify(stringConst3).referencedMemberAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitStringConstant maintains correct behavior across different marker instances.
     */
    @Test
    public void testVisitStringConstant_acrossDifferentMarkerInstances_consistentBehavior() {
        // Arrange
        MethodInvocationMarker marker1 = new MethodInvocationMarker();
        MethodInvocationMarker marker2 = new MethodInvocationMarker();
        StringConstant sc = mock(StringConstant.class);

        // Act
        marker1.visitStringConstant(clazz, sc);
        marker2.visitStringConstant(clazz, sc);

        // Assert - both should have called referencedMemberAccept
        verify(sc, times(2)).referencedMemberAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitStringConstant does not modify the StringConstant.
     * The method should only read/visit, not modify.
     */
    @Test
    public void testVisitStringConstant_doesNotModifyStringConstant() {
        // Act
        marker.visitStringConstant(clazz, stringConstant);

        // Assert - verify only the read operation (referencedMemberAccept) was called
        verify(stringConstant, times(1)).referencedMemberAccept(any(MemberVisitor.class));
        verifyNoMoreInteractions(stringConstant);
    }

    /**
     * Tests that visitStringConstant handles the case where the string constant's
     * referencedMemberAccept is called but does nothing (no referenced member).
     */
    @Test
    public void testVisitStringConstant_withNoOpReferencedMemberAccept_completesSuccessfully() {
        // Arrange - setup the mock to do nothing when referencedMemberAccept is called
        StringConstant noOpStringConstant = mock(StringConstant.class);
        doNothing().when(noOpStringConstant).referencedMemberAccept(any(MemberVisitor.class));

        // Act & Assert - should complete without error
        assertDoesNotThrow(() -> marker.visitStringConstant(clazz, noOpStringConstant));
        verify(noOpStringConstant).referencedMemberAccept(any(MemberVisitor.class));
    }
}
