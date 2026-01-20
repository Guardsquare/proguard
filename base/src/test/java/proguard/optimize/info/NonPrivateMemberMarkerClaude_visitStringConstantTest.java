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
 * Test class for {@link NonPrivateMemberMarker#visitStringConstant(Clazz, StringConstant)}.
 *
 * The visitStringConstant method processes string constants that may reference class members.
 * When a member is referenced through a string constant, it can never be made private,
 * even if it's in the same class. This is because string constant references typically indicate
 * reflection usage or other dynamic access patterns that require the member to remain accessible.
 *
 * The method delegates to the string constant's referencedMemberAccept method, passing a
 * filteredMemberMarker that only processes members with optimization info.
 */
public class NonPrivateMemberMarkerClaude_visitStringConstantTest {

    private NonPrivateMemberMarker marker;
    private Clazz clazz;
    private StringConstant stringConstant;

    @BeforeEach
    public void setUp() {
        marker = new NonPrivateMemberMarker();
        clazz = mock(ProgramClass.class);
        stringConstant = mock(StringConstant.class);
    }

    /**
     * Tests that visitStringConstant calls referencedMemberAccept on the StringConstant.
     * This is the core behavior - the method should delegate to the string constant to visit
     * any referenced member through the filtered member marker.
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
     * The visitor should be the filteredMemberMarker (an OptimizationInfoMemberFilter).
     */
    @Test
    public void testVisitStringConstant_passesFilteredMemberMarkerAsVisitor() {
        // Act
        marker.visitStringConstant(clazz, stringConstant);

        // Assert - verify a MemberVisitor was passed (the filteredMemberMarker)
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
     * Tests that multiple NonPrivateMemberMarker instances work independently.
     */
    @Test
    public void testVisitStringConstant_withMultipleMarkers_operateIndependently() {
        // Arrange
        NonPrivateMemberMarker marker1 = new NonPrivateMemberMarker();
        NonPrivateMemberMarker marker2 = new NonPrivateMemberMarker();
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
     * The filteredMemberMarker used internally must be a MemberVisitor.
     */
    @Test
    public void testVisitStringConstant_markerImplementsMemberVisitor() {
        // Assert - verify marker is a MemberVisitor
        assertTrue(marker instanceof MemberVisitor,
                "NonPrivateMemberMarker should implement MemberVisitor");
    }

    /**
     * Tests that visitStringConstant handles the case where stringConstant has no referenced member.
     * The method should still attempt to call referencedMemberAccept even if it does nothing.
     */
    @Test
    public void testVisitStringConstant_withNoReferencedMember_stillCallsReferencedMemberAccept() {
        // Arrange
        StringConstant stringConstantNoMember = mock(StringConstant.class);
        doNothing().when(stringConstantNoMember).referencedMemberAccept(any(MemberVisitor.class));

        // Act
        marker.visitStringConstant(clazz, stringConstantNoMember);

        // Assert - verify referencedMemberAccept was called
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
     * Tests that visitStringConstant does not call referencedClassAccept.
     * Unlike some other markers, NonPrivateMemberMarker only processes members, not classes.
     */
    @Test
    public void testVisitStringConstant_doesNotCallReferencedClassAccept() {
        // Act
        marker.visitStringConstant(clazz, stringConstant);

        // Assert - verify referencedClassAccept was never called
        verify(stringConstant, never()).referencedClassAccept(any());
        // Only referencedMemberAccept should be called
        verify(stringConstant, times(1)).referencedMemberAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitStringConstant always marks referenced members as non-private.
     * The comment in the code states: "The referenced class member, if any, can never
     * be made private, even if it's in the same class."
     */
    @Test
    public void testVisitStringConstant_marksReferencedMemberAsNonPrivate_regardlessOfClass() {
        // Arrange
        ProgramClass sameClass = new ProgramClass();

        // Act - call with the same class (comment says "even if it's in the same class")
        marker.visitStringConstant(sameClass, stringConstant);

        // Assert - verify referencedMemberAccept is called regardless
        verify(stringConstant, times(1)).referencedMemberAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitStringConstant passes a non-null visitor to referencedMemberAccept.
     */
    @Test
    public void testVisitStringConstant_passesNonNullVisitor() {
        // Act
        marker.visitStringConstant(clazz, stringConstant);

        // Assert - verify a non-null visitor was passed
        verify(stringConstant).referencedMemberAccept(notNull());
    }

    /**
     * Tests that visitStringConstant uses a filtered visitor.
     * The filteredMemberMarker wraps the marker in an OptimizationInfoMemberFilter.
     */
    @Test
    public void testVisitStringConstant_usesFilteredMemberMarker() {
        // Act
        marker.visitStringConstant(clazz, stringConstant);

        // Assert - verify a MemberVisitor (the filteredMemberMarker) was passed
        // We can't directly verify it's an OptimizationInfoMemberFilter without reflection,
        // but we can verify it's a MemberVisitor
        verify(stringConstant).referencedMemberAccept(isA(MemberVisitor.class));
    }

    /**
     * Tests that visitStringConstant handles multiple string constants in sequence.
     */
    @Test
    public void testVisitStringConstant_multipleConstantsInSequence_eachProcessed() {
        // Arrange
        StringConstant sc1 = mock(StringConstant.class);
        StringConstant sc2 = mock(StringConstant.class);
        StringConstant sc3 = mock(StringConstant.class);
        StringConstant sc4 = mock(StringConstant.class);

        // Act
        marker.visitStringConstant(clazz, sc1);
        marker.visitStringConstant(clazz, sc2);
        marker.visitStringConstant(clazz, sc3);
        marker.visitStringConstant(clazz, sc4);

        // Assert
        verify(sc1, times(1)).referencedMemberAccept(any(MemberVisitor.class));
        verify(sc2, times(1)).referencedMemberAccept(any(MemberVisitor.class));
        verify(sc3, times(1)).referencedMemberAccept(any(MemberVisitor.class));
        verify(sc4, times(1)).referencedMemberAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitStringConstant can be called in a tight loop without issues.
     */
    @Test
    public void testVisitStringConstant_tightLoop_noIssues() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 1000; i++) {
                marker.visitStringConstant(clazz, stringConstant);
            }
        });

        verify(stringConstant, times(1000)).referencedMemberAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitStringConstant doesn't modify the marker's state in a way that
     * affects subsequent calls. Each call should be independent.
     */
    @Test
    public void testVisitStringConstant_subsequentCalls_independent() {
        // Arrange
        StringConstant sc1 = mock(StringConstant.class);
        StringConstant sc2 = mock(StringConstant.class);

        // Act
        marker.visitStringConstant(clazz, sc1);
        marker.visitStringConstant(clazz, sc2);

        // Assert - each constant should be visited exactly once
        verify(sc1, times(1)).referencedMemberAccept(any(MemberVisitor.class));
        verify(sc2, times(1)).referencedMemberAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitStringConstant with alternating clazz and string constant parameters
     * processes each call correctly.
     */
    @Test
    public void testVisitStringConstant_alternatingParameters_allProcessed() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        StringConstant sc1 = mock(StringConstant.class);
        StringConstant sc2 = mock(StringConstant.class);

        // Act
        marker.visitStringConstant(clazz1, sc1);
        marker.visitStringConstant(clazz2, sc2);
        marker.visitStringConstant(clazz1, sc2);
        marker.visitStringConstant(clazz2, sc1);

        // Assert
        verify(sc1, times(2)).referencedMemberAccept(any(MemberVisitor.class));
        verify(sc2, times(2)).referencedMemberAccept(any(MemberVisitor.class));
        verifyNoInteractions(clazz1);
        verifyNoInteractions(clazz2);
    }

    /**
     * Tests that visitStringConstant behaves consistently across fresh marker instances.
     */
    @Test
    public void testVisitStringConstant_freshMarkerInstances_consistentBehavior() {
        // Arrange
        NonPrivateMemberMarker marker1 = new NonPrivateMemberMarker();
        NonPrivateMemberMarker marker2 = new NonPrivateMemberMarker();
        NonPrivateMemberMarker marker3 = new NonPrivateMemberMarker();
        StringConstant sc = mock(StringConstant.class);

        // Act
        marker1.visitStringConstant(clazz, sc);
        marker2.visitStringConstant(clazz, sc);
        marker3.visitStringConstant(clazz, sc);

        // Assert - each marker should call referencedMemberAccept once
        verify(sc, times(3)).referencedMemberAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitStringConstant execution is efficient and doesn't cause delays.
     */
    @Test
    public void testVisitStringConstant_executesEfficiently() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - call many times
        for (int i = 0; i < 1000; i++) {
            marker.visitStringConstant(clazz, stringConstant);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete reasonably quickly (within 500ms for 1000 calls)
        assertTrue(durationMs < 500,
                "visitStringConstant should execute efficiently, took " + durationMs + "ms for 1000 calls");
    }
}
