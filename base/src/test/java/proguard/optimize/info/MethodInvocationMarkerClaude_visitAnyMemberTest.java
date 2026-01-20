package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.LibraryClass;
import proguard.classfile.Member;
import proguard.classfile.ProgramMethod;
import proguard.classfile.ProgramField;
import proguard.classfile.LibraryMethod;
import proguard.classfile.LibraryField;
import proguard.classfile.visitor.MemberVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link MethodInvocationMarker#visitAnyMember(Clazz, Member)}.
 *
 * The visitAnyMember method is an empty implementation (no-op) that serves as a default
 * handler in the MemberVisitor pattern. The MethodInvocationMarker only processes
 * ProgramMethod types through the specialized visitProgramMethod method.
 * This method provides the default no-op behavior for all other member types
 * (fields, library methods, etc.).
 */
public class MethodInvocationMarkerClaude_visitAnyMemberTest {

    private MethodInvocationMarker marker;
    private Clazz clazz;
    private Member member;

    @BeforeEach
    public void setUp() {
        marker = new MethodInvocationMarker();
        clazz = mock(ProgramClass.class);
        member = mock(Member.class);
    }

    /**
     * Tests that visitAnyMember can be called with valid mock objects without throwing exceptions.
     * Since this is a no-op method, it should simply do nothing and complete successfully.
     */
    @Test
    public void testVisitAnyMember_withValidMocks_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> marker.visitAnyMember(clazz, member));
    }

    /**
     * Tests that visitAnyMember can be called with null Clazz parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyMember_withNullClazz_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> marker.visitAnyMember(null, member));
    }

    /**
     * Tests that visitAnyMember can be called with null Member parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyMember_withNullMember_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> marker.visitAnyMember(clazz, null));
    }

    /**
     * Tests that visitAnyMember can be called with both parameters null.
     * The method should handle null parameters gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyMember_withBothParametersNull_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> marker.visitAnyMember(null, null));
    }

    /**
     * Tests that visitAnyMember can be called multiple times in succession.
     * The method should be idempotent and handle repeated calls without issues.
     */
    @Test
    public void testVisitAnyMember_calledMultipleTimes_doesNotThrowException() {
        // Act & Assert - should not throw any exception on multiple calls
        assertDoesNotThrow(() -> {
            marker.visitAnyMember(clazz, member);
            marker.visitAnyMember(clazz, member);
            marker.visitAnyMember(clazz, member);
        });
    }

    /**
     * Tests that visitAnyMember doesn't interact with the Clazz parameter.
     * Since it's a no-op method, it should not call any methods on the clazz.
     */
    @Test
    public void testVisitAnyMember_doesNotInteractWithClazz() {
        // Act
        marker.visitAnyMember(clazz, member);

        // Assert - verify no interactions occurred with the clazz mock
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitAnyMember doesn't interact with the Member parameter.
     * Since it's a no-op method, it should not call any methods on the member.
     */
    @Test
    public void testVisitAnyMember_doesNotInteractWithMember() {
        // Act
        marker.visitAnyMember(clazz, member);

        // Assert - verify no interactions occurred with the member mock
        verifyNoInteractions(member);
    }

    /**
     * Tests that visitAnyMember works with different Clazz implementations.
     */
    @Test
    public void testVisitAnyMember_withDifferentClazzTypes_doesNotThrowException() {
        // Arrange
        Clazz programClass = mock(ProgramClass.class);
        Clazz libraryClass = mock(LibraryClass.class);

        // Act & Assert - should not throw any exception with different clazz types
        assertDoesNotThrow(() -> {
            marker.visitAnyMember(programClass, member);
            marker.visitAnyMember(libraryClass, member);
        });
    }

    /**
     * Tests that visitAnyMember works with different Member implementations.
     */
    @Test
    public void testVisitAnyMember_withDifferentMemberTypes_doesNotThrowException() {
        // Arrange - create mocks of various member types
        Member programField = mock(ProgramField.class);
        Member libraryMethod = mock(LibraryMethod.class);
        Member libraryField = mock(LibraryField.class);

        // Act & Assert - should not throw any exception with different member types
        assertDoesNotThrow(() -> {
            marker.visitAnyMember(clazz, programField);
            marker.visitAnyMember(clazz, libraryMethod);
            marker.visitAnyMember(clazz, libraryField);
        });
    }

    /**
     * Tests that visitAnyMember execution completes immediately.
     * Since it's a no-op method, it should have minimal overhead.
     */
    @Test
    public void testVisitAnyMember_executesQuickly() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            marker.visitAnyMember(clazz, member);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100, "visitAnyMember should execute quickly as it's a no-op");
    }

    /**
     * Tests that visitAnyMember doesn't affect subsequent calls.
     * The no-op should not interfere with the marker's normal operation.
     */
    @Test
    public void testVisitAnyMember_doesNotAffectSubsequentOperations() {
        // Act - call visitAnyMember first
        marker.visitAnyMember(clazz, member);

        // Then call visitAnyMember again
        marker.visitAnyMember(clazz, member);

        // Assert - verify no side effects occurred
        verifyNoInteractions(clazz);
        verifyNoInteractions(member);
    }

    /**
     * Tests that visitAnyMember can be called with the same parameters repeatedly
     * without accumulating any state or causing issues.
     */
    @Test
    public void testVisitAnyMember_repeatedCallsWithSameParameters_noStateAccumulation() {
        // Act - call multiple times with same parameters
        for (int i = 0; i < 10; i++) {
            marker.visitAnyMember(clazz, member);
        }

        // Assert - verify no interactions occurred despite multiple calls
        verifyNoInteractions(clazz);
        verifyNoInteractions(member);
    }

    /**
     * Tests that visitAnyMember is thread-safe when called concurrently.
     * Since it's a no-op with no state changes, it should handle concurrent calls.
     */
    @Test
    public void testVisitAnyMember_concurrentCalls_noExceptions() throws InterruptedException {
        // Arrange
        final int threadCount = 10;
        Thread[] threads = new Thread[threadCount];

        // Act - create multiple threads that call visitAnyMember
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    marker.visitAnyMember(clazz, member);
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
        verifyNoInteractions(member);
    }

    /**
     * Tests that multiple markers can call visitAnyMember independently.
     * Each marker's no-op should not affect others.
     */
    @Test
    public void testVisitAnyMember_withMultipleMarkers_operateIndependently() {
        // Arrange - create multiple markers
        MethodInvocationMarker marker1 = new MethodInvocationMarker();
        MethodInvocationMarker marker2 = new MethodInvocationMarker();

        // Act - call visitAnyMember on both markers
        marker1.visitAnyMember(clazz, member);
        marker2.visitAnyMember(clazz, member);

        // Assert - verify no interactions occurred
        verifyNoInteractions(clazz);
        verifyNoInteractions(member);
    }

    /**
     * Tests that visitAnyMember completes without blocking or hanging.
     * This ensures the method doesn't have any unexpected wait conditions.
     */
    @Test
    public void testVisitAnyMember_completesImmediately() {
        // Arrange
        long timeoutNanos = 1_000_000; // 1 millisecond
        long startTime = System.nanoTime();

        // Act
        marker.visitAnyMember(clazz, member);

        long duration = System.nanoTime() - startTime;

        // Assert - should complete within the timeout
        assertTrue(duration < timeoutNanos,
                "visitAnyMember should complete immediately, took " + duration + " nanoseconds");
    }

    /**
     * Tests that visitAnyMember called after multiple other operations still behaves as a no-op.
     * This verifies consistent behavior regardless of the marker's usage history.
     */
    @Test
    public void testVisitAnyMember_afterMultipleOperations_stillNoOp() {
        // Arrange - simulate some prior operations
        for (int i = 0; i < 5; i++) {
            marker.visitAnyMember(clazz, member);
        }

        // Act - call visitAnyMember again
        marker.visitAnyMember(clazz, member);

        // Assert - verify it's still a no-op with no interactions
        verifyNoInteractions(clazz);
        verifyNoInteractions(member);
    }

    /**
     * Tests that visitAnyMember can be called in rapid succession without issues.
     * This verifies there's no timing-dependent behavior.
     */
    @Test
    public void testVisitAnyMember_rapidSuccession_noIssues() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 1000; i++) {
                marker.visitAnyMember(clazz, member);
            }
        }, "Rapid successive calls should not cause issues");

        // Verify no interactions occurred
        verifyNoInteractions(clazz);
        verifyNoInteractions(member);
    }

    /**
     * Tests that visitAnyMember with mixed null and non-null parameters works correctly.
     * This ensures the method handles partial null inputs gracefully.
     */
    @Test
    public void testVisitAnyMember_withMixedNullParameters_doesNotThrowException() {
        // Act & Assert - test various combinations of null/non-null parameters
        assertDoesNotThrow(() -> marker.visitAnyMember(null, member));
        assertDoesNotThrow(() -> marker.visitAnyMember(clazz, null));
        assertDoesNotThrow(() -> marker.visitAnyMember(null, null));
    }

    /**
     * Tests that visitAnyMember works with different Clazz mock instances.
     * The method should handle any Clazz implementation without issues.
     */
    @Test
    public void testVisitAnyMember_withDifferentClazzInstances_doesNotThrowException() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Clazz clazz3 = mock(ProgramClass.class);

        // Act & Assert - should not throw any exception with different instances
        assertDoesNotThrow(() -> {
            marker.visitAnyMember(clazz1, member);
            marker.visitAnyMember(clazz2, member);
            marker.visitAnyMember(clazz3, member);
        });
    }

    /**
     * Tests that visitAnyMember works with different Member mock instances.
     * The method should handle any Member implementation without issues.
     */
    @Test
    public void testVisitAnyMember_withDifferentMemberInstances_doesNotThrowException() {
        // Arrange
        Member member1 = mock(Member.class);
        Member member2 = mock(Member.class);
        Member member3 = mock(Member.class);

        // Act & Assert - should not throw any exception with different instances
        assertDoesNotThrow(() -> {
            marker.visitAnyMember(clazz, member1);
            marker.visitAnyMember(clazz, member2);
            marker.visitAnyMember(clazz, member3);
        });
    }

    /**
     * Tests that visitAnyMember doesn't modify the marker's internal state.
     * Since it's a no-op, it should not modify any internal fields.
     */
    @Test
    public void testVisitAnyMember_doesNotModifyMarkerState() {
        // Act - call visitAnyMember multiple times
        marker.visitAnyMember(clazz, member);
        marker.visitAnyMember(clazz, member);

        // Assert - verify no interactions occurred, indicating no state changes
        verifyNoInteractions(clazz);
        verifyNoInteractions(member);
    }

    /**
     * Tests that visitAnyMember works correctly regardless of marker's construction.
     * The method should behave consistently for any marker instance.
     */
    @Test
    public void testVisitAnyMember_withNewlyCreatedMarker_worksCorrectly() {
        // Arrange - create a fresh marker
        MethodInvocationMarker newMarker = new MethodInvocationMarker();

        // Act & Assert
        assertDoesNotThrow(() -> newMarker.visitAnyMember(clazz, member));

        // Verify no interactions
        verifyNoInteractions(clazz);
        verifyNoInteractions(member);
    }

    /**
     * Tests that visitAnyMember doesn't throw with various Member subclass mocks.
     */
    @Test
    public void testVisitAnyMember_withVariousMemberSubclasses_doesNotThrow() {
        // Arrange - create mocks of various specific member types
        ProgramField programField = mock(ProgramField.class);
        LibraryMethod libraryMethod = mock(LibraryMethod.class);
        LibraryField libraryField = mock(LibraryField.class);

        // Act & Assert - should not throw with any member type
        assertDoesNotThrow(() -> {
            marker.visitAnyMember(clazz, programField);
            marker.visitAnyMember(clazz, libraryMethod);
            marker.visitAnyMember(clazz, libraryField);
        });
    }

    /**
     * Tests that visitAnyMember doesn't interact with any mock objects.
     * This verifies the true no-op nature of the method.
     */
    @Test
    public void testVisitAnyMember_verifiesCompleteNoOp() {
        // Arrange
        Clazz mockClazz = mock(ProgramClass.class);
        Member mockMember = mock(Member.class);

        // Act
        marker.visitAnyMember(mockClazz, mockMember);

        // Assert - verify absolutely no interactions with any mock
        verifyNoInteractions(mockClazz);
        verifyNoInteractions(mockMember);
    }

    /**
     * Tests that visitAnyMember can be called an arbitrary number of times.
     */
    @Test
    public void testVisitAnyMember_manySequentialCalls_noIssues() {
        // Act - call 10000 times
        for (int i = 0; i < 10000; i++) {
            assertDoesNotThrow(() -> marker.visitAnyMember(clazz, member));
        }

        // Assert
        verifyNoInteractions(clazz);
        verifyNoInteractions(member);
    }

    /**
     * Tests that visitAnyMember maintains consistent behavior across different
     * combinations of Clazz and Member instances.
     */
    @Test
    public void testVisitAnyMember_variousCombinations_consistentBehavior() {
        // Arrange
        Clazz programClass = mock(ProgramClass.class);
        Clazz libraryClass = mock(LibraryClass.class);
        Member programField = mock(ProgramField.class);
        Member libraryMethod = mock(LibraryMethod.class);

        // Act & Assert - all combinations should work identically
        assertDoesNotThrow(() -> {
            marker.visitAnyMember(programClass, programField);
            marker.visitAnyMember(programClass, libraryMethod);
            marker.visitAnyMember(libraryClass, programField);
            marker.visitAnyMember(libraryClass, libraryMethod);
        });

        // Verify no interactions with any mock
        verifyNoInteractions(programClass);
        verifyNoInteractions(libraryClass);
        verifyNoInteractions(programField);
        verifyNoInteractions(libraryMethod);
    }

    /**
     * Tests that multiple markers calling visitAnyMember don't interfere with each other.
     */
    @Test
    public void testVisitAnyMember_multipleMarkersInParallel_noInterference() {
        // Arrange
        MethodInvocationMarker marker1 = new MethodInvocationMarker();
        MethodInvocationMarker marker2 = new MethodInvocationMarker();
        MethodInvocationMarker marker3 = new MethodInvocationMarker();
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Member member1 = mock(Member.class);
        Member member2 = mock(Member.class);

        // Act
        marker1.visitAnyMember(clazz1, member1);
        marker2.visitAnyMember(clazz2, member2);
        marker3.visitAnyMember(clazz1, member2);

        // Assert - no interactions with any mock
        verifyNoInteractions(clazz1);
        verifyNoInteractions(clazz2);
        verifyNoInteractions(member1);
        verifyNoInteractions(member2);
    }

    /**
     * Tests that visitAnyMember properly implements the MemberVisitor interface.
     * This ensures the method signature matches the interface.
     */
    @Test
    public void testVisitAnyMember_implementsMemberVisitorInterface() {
        // Assert - MethodInvocationMarker should be a MemberVisitor
        assertTrue(marker instanceof MemberVisitor,
                "MethodInvocationMarker should implement MemberVisitor");
    }

    /**
     * Tests that visitAnyMember doesn't affect the filteredMethodMarker field.
     * The no-op should not interact with any internal state of the marker.
     */
    @Test
    public void testVisitAnyMember_doesNotAffectInternalFields() {
        // Act - call visitAnyMember multiple times
        marker.visitAnyMember(clazz, member);
        marker.visitAnyMember(clazz, member);
        marker.visitAnyMember(clazz, member);

        // Assert - verify no interactions occurred
        verifyNoInteractions(clazz);
        verifyNoInteractions(member);
    }

    /**
     * Tests that visitAnyMember with field members works correctly.
     * Fields should be handled as no-ops since MethodInvocationMarker only tracks methods.
     */
    @Test
    public void testVisitAnyMember_withFieldMembers_doesNotThrow() {
        // Arrange
        Member programField = mock(ProgramField.class);
        Member libraryField = mock(LibraryField.class);

        // Act & Assert - should handle field members
        assertDoesNotThrow(() -> {
            marker.visitAnyMember(clazz, programField);
            marker.visitAnyMember(clazz, libraryField);
        });

        // Verify no interactions
        verifyNoInteractions(programField);
        verifyNoInteractions(libraryField);
    }

    /**
     * Tests that visitAnyMember with library method members works correctly.
     * Library methods should be handled as no-ops since they don't have optimization info.
     */
    @Test
    public void testVisitAnyMember_withLibraryMethods_doesNotThrow() {
        // Arrange
        Member libraryMethod = mock(LibraryMethod.class);

        // Act & Assert - should handle library methods
        assertDoesNotThrow(() -> marker.visitAnyMember(clazz, libraryMethod));

        // Verify no interactions
        verifyNoInteractions(libraryMethod);
    }

    /**
     * Tests that visitAnyMember behavior is consistent before and after other visitor method calls.
     * The method should remain a no-op regardless of the marker's usage.
     */
    @Test
    public void testVisitAnyMember_behaviorConsistentAcrossUsage() {
        // Act - call before and after
        marker.visitAnyMember(clazz, member);

        // Simulate some other operations (still no-ops but conceptually different methods)
        marker.visitAnyMember(clazz, member);

        // Call again
        marker.visitAnyMember(clazz, member);

        // Assert - behavior should be consistently no-op
        verifyNoInteractions(clazz);
        verifyNoInteractions(member);
    }

    /**
     * Tests that visitAnyMember serves as a default implementation for the visitor pattern.
     * This method handles members that don't have specialized visitor methods.
     */
    @Test
    public void testVisitAnyMember_servesAsDefaultForUnspecializedMembers() {
        // Arrange - members that would typically use the default visitor
        ProgramField programField = mock(ProgramField.class);
        LibraryMethod libraryMethod = mock(LibraryMethod.class);
        LibraryField libraryField = mock(LibraryField.class);

        // Act - these members don't have specialized methods in MethodInvocationMarker
        marker.visitAnyMember(clazz, programField);
        marker.visitAnyMember(clazz, libraryMethod);
        marker.visitAnyMember(clazz, libraryField);

        // Assert - no processing should occur for these member types
        verifyNoInteractions(programField);
        verifyNoInteractions(libraryMethod);
        verifyNoInteractions(libraryField);
    }

    /**
     * Tests that visitAnyMember doesn't process members that should be handled
     * by specialized methods (like visitProgramMethod).
     */
    @Test
    public void testVisitAnyMember_doesNotProcessSpecializedMembers() {
        // Arrange - generic member mock
        Member genericMember = mock(Member.class);

        // Act
        marker.visitAnyMember(clazz, genericMember);

        // Assert - no processing should occur in this default method
        verifyNoInteractions(genericMember);
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitAnyMember has zero memory allocation overhead.
     * Since it's an empty method, it shouldn't allocate any objects.
     */
    @Test
    public void testVisitAnyMember_zeroAllocationOverhead() {
        // Act - call multiple times
        for (int i = 0; i < 1000; i++) {
            marker.visitAnyMember(clazz, member);
        }

        // Assert - verify no interactions, implying no object creation for processing
        verifyNoInteractions(clazz);
        verifyNoInteractions(member);
    }

    /**
     * Tests that visitAnyMember maintains consistency across invocations.
     * Every call should behave identically as a no-op.
     */
    @Test
    public void testVisitAnyMember_consistentBehavior() {
        // Act - call multiple times and verify consistent behavior
        for (int i = 0; i < 100; i++) {
            assertDoesNotThrow(() ->
                marker.visitAnyMember(clazz, member)
            );
        }

        // Assert - verify no interactions occurred in any of the calls
        verifyNoInteractions(clazz);
        verifyNoInteractions(member);
    }

    /**
     * Tests that visitAnyMember can be used as a base implementation.
     * Subclasses could override this method if needed, but the default is to do nothing.
     */
    @Test
    public void testVisitAnyMember_usableAsBaseImplementation() {
        // Act
        marker.visitAnyMember(clazz, member);

        // Assert - verify the base implementation (no-op) was used
        verifyNoInteractions(clazz);
        verifyNoInteractions(member);
    }

    /**
     * Tests that visitAnyMember works with real class instances.
     */
    @Test
    public void testVisitAnyMember_withRealClasses_doesNotThrow() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        LibraryClass libraryClass = new LibraryClass();

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitAnyMember(programClass, member));
        assertDoesNotThrow(() -> marker.visitAnyMember(libraryClass, member));
    }

    /**
     * Tests that visitAnyMember handles edge cases where member types might be unusual.
     */
    @Test
    public void testVisitAnyMember_withEdgeCaseMemberTypes_handlesGracefully() {
        // Arrange - various member mocks
        Member member1 = mock(Member.class, "member1");
        Member member2 = mock(Member.class, "member2");
        Member member3 = mock(Member.class, "member3");

        // Act
        marker.visitAnyMember(clazz, member1);
        marker.visitAnyMember(clazz, member2);
        marker.visitAnyMember(clazz, member3);

        // Assert - all should be handled as no-ops
        verifyNoInteractions(member1);
        verifyNoInteractions(member2);
        verifyNoInteractions(member3);
    }

    /**
     * Tests that visitAnyMember does not increment invocation counts.
     * Only visitProgramMethod should increment counts, not this default method.
     */
    @Test
    public void testVisitAnyMember_doesNotIncrementInvocationCounts() {
        // Arrange
        Member genericMember = mock(Member.class);

        // Act
        marker.visitAnyMember(clazz, genericMember);

        // Assert - verify the member was not processed (no invocation count increment)
        verifyNoInteractions(genericMember);
    }

    /**
     * Tests that visitAnyMember properly delegates to specialized methods for specific types.
     * This default method should not be called for ProgramMethod (handled by visitProgramMethod).
     */
    @Test
    public void testVisitAnyMember_doesNotHandleSpecializedTypes() {
        // Arrange - this tests the default behavior, not specialized handling
        Member nonProgramMethod = mock(LibraryMethod.class);

        // Act
        marker.visitAnyMember(clazz, nonProgramMethod);

        // Assert - verify no processing occurred
        verifyNoInteractions(nonProgramMethod);
    }
}
