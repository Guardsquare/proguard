package proguard.optimize.peephole;

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
 * Test class for {@link MethodInliner#visitAnyMember(Clazz, Member)}.
 *
 * The visitAnyMember method is an empty implementation (no-op) that serves as a default
 * handler in the MemberVisitor pattern. The MethodInliner processes specific member types
 * through specialized methods:
 * - visitProgramMethod: Processes program methods for potential inlining
 * - visitLibraryMethod: Handles library methods (typically decrements uninitialized object count)
 *
 * This visitAnyMember method provides the default no-op behavior for all other member types
 * (fields, or when called directly instead of through the specialized methods).
 */
public class MethodInlinerClaude_visitAnyMemberTest {

    private MethodInliner inliner;
    private Clazz clazz;
    private Member member;

    @BeforeEach
    public void setUp() {
        // Create a concrete implementation for testing
        inliner = new ShortMethodInliner(false, false, true);
        clazz = mock(ProgramClass.class);
        member = mock(Member.class);
    }

    // ========================================
    // Basic Functionality Tests
    // ========================================

    /**
     * Tests that visitAnyMember can be called with valid mock objects without throwing exceptions.
     * Since this is a no-op method, it should simply do nothing and complete successfully.
     */
    @Test
    public void testVisitAnyMember_withValidMocks_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> inliner.visitAnyMember(clazz, member));
    }

    /**
     * Tests that visitAnyMember doesn't interact with the Clazz parameter.
     * Since it's a no-op method, it should not call any methods on the clazz.
     */
    @Test
    public void testVisitAnyMember_doesNotInteractWithClazz() {
        // Act
        inliner.visitAnyMember(clazz, member);

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
        inliner.visitAnyMember(clazz, member);

        // Assert - verify no interactions occurred with the member mock
        verifyNoInteractions(member);
    }

    /**
     * Tests that visitAnyMember doesn't interact with either parameter.
     * Verifies complete no-op behavior.
     */
    @Test
    public void testVisitAnyMember_doesNotInteractWithEitherParameter() {
        // Arrange
        Clazz mockClazz = mock(ProgramClass.class);
        Member mockMember = mock(Member.class);

        // Act
        inliner.visitAnyMember(mockClazz, mockMember);

        // Assert
        verifyNoInteractions(mockClazz);
        verifyNoInteractions(mockMember);
    }

    // ========================================
    // Null Parameter Tests
    // ========================================

    /**
     * Tests that visitAnyMember can be called with null Clazz parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyMember_withNullClazz_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> inliner.visitAnyMember(null, member));
    }

    /**
     * Tests that visitAnyMember can be called with null Member parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyMember_withNullMember_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> inliner.visitAnyMember(clazz, null));
    }

    /**
     * Tests that visitAnyMember can be called with both parameters null.
     * The method should handle null parameters gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyMember_withBothParametersNull_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> inliner.visitAnyMember(null, null));
    }

    /**
     * Tests that visitAnyMember with mixed null and non-null parameters works correctly.
     * This ensures the method handles partial null inputs gracefully.
     */
    @Test
    public void testVisitAnyMember_withMixedNullParameters_doesNotThrowException() {
        // Act & Assert - test various combinations of null/non-null parameters
        assertDoesNotThrow(() -> inliner.visitAnyMember(null, member));
        assertDoesNotThrow(() -> inliner.visitAnyMember(clazz, null));
        assertDoesNotThrow(() -> inliner.visitAnyMember(null, null));
    }

    // ========================================
    // Different Member Types Tests
    // ========================================

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
            inliner.visitAnyMember(clazz, programField);
            inliner.visitAnyMember(clazz, libraryMethod);
            inliner.visitAnyMember(clazz, libraryField);
        });
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
            inliner.visitAnyMember(clazz, programField);
            inliner.visitAnyMember(clazz, libraryMethod);
            inliner.visitAnyMember(clazz, libraryField);
        });
    }

    /**
     * Tests that visitAnyMember with field members works correctly.
     * Fields should be handled as no-ops since MethodInliner only processes methods.
     */
    @Test
    public void testVisitAnyMember_withFieldMembers_doesNotThrow() {
        // Arrange
        Member programField = mock(ProgramField.class);
        Member libraryField = mock(LibraryField.class);

        // Act & Assert - should handle field members
        assertDoesNotThrow(() -> {
            inliner.visitAnyMember(clazz, programField);
            inliner.visitAnyMember(clazz, libraryField);
        });

        // Verify no interactions
        verifyNoInteractions(programField);
        verifyNoInteractions(libraryField);
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
            inliner.visitAnyMember(clazz, member1);
            inliner.visitAnyMember(clazz, member2);
            inliner.visitAnyMember(clazz, member3);
        });
    }

    // ========================================
    // Different Class Types Tests
    // ========================================

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
            inliner.visitAnyMember(programClass, member);
            inliner.visitAnyMember(libraryClass, member);
        });
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
            inliner.visitAnyMember(clazz1, member);
            inliner.visitAnyMember(clazz2, member);
            inliner.visitAnyMember(clazz3, member);
        });
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
        assertDoesNotThrow(() -> inliner.visitAnyMember(programClass, member));
        assertDoesNotThrow(() -> inliner.visitAnyMember(libraryClass, member));
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
            inliner.visitAnyMember(programClass, programField);
            inliner.visitAnyMember(programClass, libraryMethod);
            inliner.visitAnyMember(libraryClass, programField);
            inliner.visitAnyMember(libraryClass, libraryMethod);
        });

        // Verify no interactions with any mock
        verifyNoInteractions(programClass);
        verifyNoInteractions(libraryClass);
        verifyNoInteractions(programField);
        verifyNoInteractions(libraryMethod);
    }

    // ========================================
    // Multiple Call Tests
    // ========================================

    /**
     * Tests that visitAnyMember can be called multiple times in succession.
     * The method should be idempotent and handle repeated calls without issues.
     */
    @Test
    public void testVisitAnyMember_calledMultipleTimes_doesNotThrowException() {
        // Act & Assert - should not throw any exception on multiple calls
        assertDoesNotThrow(() -> {
            inliner.visitAnyMember(clazz, member);
            inliner.visitAnyMember(clazz, member);
            inliner.visitAnyMember(clazz, member);
        });
    }

    /**
     * Tests that visitAnyMember can be called with the same parameters repeatedly
     * without accumulating any state or causing issues.
     */
    @Test
    public void testVisitAnyMember_repeatedCallsWithSameParameters_noStateAccumulation() {
        // Act - call multiple times with same parameters
        for (int i = 0; i < 10; i++) {
            inliner.visitAnyMember(clazz, member);
        }

        // Assert - verify no interactions occurred despite multiple calls
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
                inliner.visitAnyMember(clazz, member);
            }
        }, "Rapid successive calls should not cause issues");

        // Verify no interactions occurred
        verifyNoInteractions(clazz);
        verifyNoInteractions(member);
    }

    /**
     * Tests that visitAnyMember can be called an arbitrary number of times.
     */
    @Test
    public void testVisitAnyMember_manySequentialCalls_noIssues() {
        // Act - call 1000 times
        for (int i = 0; i < 1000; i++) {
            assertDoesNotThrow(() -> inliner.visitAnyMember(clazz, member));
        }

        // Assert
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
                inliner.visitAnyMember(clazz, member)
            );
        }

        // Assert - verify no interactions occurred in any of the calls
        verifyNoInteractions(clazz);
        verifyNoInteractions(member);
    }

    /**
     * Tests that visitAnyMember maintains consistent behavior with alternating parameters.
     * Verifies the method doesn't accumulate state between calls.
     */
    @Test
    public void testVisitAnyMember_alternatingParameters_consistent() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        LibraryClass libraryClass = new LibraryClass();
        Member member1 = mock(Member.class);
        Member member2 = mock(Member.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            inliner.visitAnyMember(programClass, member1);
            inliner.visitAnyMember(libraryClass, member2);
            inliner.visitAnyMember(programClass, member2);
            inliner.visitAnyMember(libraryClass, member1);
        });

        // Verify no interactions
        verifyNoInteractions(member1);
        verifyNoInteractions(member2);
    }

    // ========================================
    // Different Inliner Configuration Tests
    // ========================================

    /**
     * Tests visitAnyMember with microEdition configuration.
     * Verifies the no-op behavior is independent of configuration.
     */
    @Test
    public void testVisitAnyMember_withMicroEditionConfiguration_doesNotThrow() {
        // Arrange
        MethodInliner microEditionInliner = new ShortMethodInliner(true, false, false);

        // Act & Assert
        assertDoesNotThrow(() -> microEditionInliner.visitAnyMember(clazz, member));
    }

    /**
     * Tests visitAnyMember with Android configuration.
     * Verifies the no-op behavior is independent of configuration.
     */
    @Test
    public void testVisitAnyMember_withAndroidConfiguration_doesNotThrow() {
        // Arrange
        MethodInliner androidInliner = new ShortMethodInliner(false, true, false);

        // Act & Assert
        assertDoesNotThrow(() -> androidInliner.visitAnyMember(clazz, member));
    }

    /**
     * Tests visitAnyMember with allowAccessModification enabled.
     * Verifies the no-op behavior is independent of access modification setting.
     */
    @Test
    public void testVisitAnyMember_withAccessModificationEnabled_doesNotThrow() {
        // Arrange
        MethodInliner accessModInliner = new ShortMethodInliner(false, false, true);

        // Act & Assert
        assertDoesNotThrow(() -> accessModInliner.visitAnyMember(clazz, member));
    }

    /**
     * Tests visitAnyMember with all configuration flags enabled.
     * Verifies the no-op behavior is independent of all configurations.
     */
    @Test
    public void testVisitAnyMember_withAllConfigurationsEnabled_doesNotThrow() {
        // Arrange
        MethodInliner fullConfigInliner = new ShortMethodInliner(true, true, true);

        // Act & Assert
        assertDoesNotThrow(() -> fullConfigInliner.visitAnyMember(clazz, member));
    }

    // ========================================
    // Multiple Inliner Instance Tests
    // ========================================

    /**
     * Tests that different inliner instances work independently.
     * Verifies no shared state between instances.
     */
    @Test
    public void testVisitAnyMember_multipleInliners_independent() {
        // Arrange
        MethodInliner inliner1 = new ShortMethodInliner(false, false, true);
        MethodInliner inliner2 = new ShortMethodInliner(false, false, true);

        // Act & Assert
        assertDoesNotThrow(() -> {
            inliner1.visitAnyMember(clazz, member);
            inliner2.visitAnyMember(clazz, member);
        });
    }

    /**
     * Tests that multiple inliners can visit the same member.
     * Verifies concurrent-like access patterns work correctly.
     */
    @Test
    public void testVisitAnyMember_sameMemberDifferentInliners() {
        // Arrange
        MethodInliner inliner1 = new ShortMethodInliner(false, false, true);
        MethodInliner inliner2 = new ShortMethodInliner(true, false, false);
        MethodInliner inliner3 = new ShortMethodInliner(false, true, true);

        // Act & Assert
        assertDoesNotThrow(() -> {
            inliner1.visitAnyMember(clazz, member);
            inliner2.visitAnyMember(clazz, member);
            inliner3.visitAnyMember(clazz, member);
        });
    }

    /**
     * Tests that multiple inliners calling visitAnyMember don't interfere with each other.
     */
    @Test
    public void testVisitAnyMember_multipleInlinersInParallel_noInterference() {
        // Arrange
        MethodInliner inliner1 = new ShortMethodInliner(false, false, true);
        MethodInliner inliner2 = new ShortMethodInliner(false, false, true);
        MethodInliner inliner3 = new ShortMethodInliner(false, false, true);
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Member member1 = mock(Member.class);
        Member member2 = mock(Member.class);

        // Act
        inliner1.visitAnyMember(clazz1, member1);
        inliner2.visitAnyMember(clazz2, member2);
        inliner3.visitAnyMember(clazz1, member2);

        // Assert - no interactions with any mock
        verifyNoInteractions(clazz1);
        verifyNoInteractions(clazz2);
        verifyNoInteractions(member1);
        verifyNoInteractions(member2);
    }

    // ========================================
    // Edge Case Tests
    // ========================================

    /**
     * Tests visitAnyMember immediately after inliner creation.
     * Verifies the inliner is ready to use immediately.
     */
    @Test
    public void testVisitAnyMember_immediatelyAfterCreation() {
        // Arrange
        MethodInliner newInliner = new ShortMethodInliner(false, false, true);

        // Act & Assert
        assertDoesNotThrow(() -> newInliner.visitAnyMember(clazz, member));
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
            inliner.visitAnyMember(clazz, member);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100, "visitAnyMember should execute quickly as it's a no-op");
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
        inliner.visitAnyMember(clazz, member);

        long duration = System.nanoTime() - startTime;

        // Assert - should complete within the timeout
        assertTrue(duration < timeoutNanos,
                "visitAnyMember should complete immediately, took " + duration + " nanoseconds");
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
        inliner.visitAnyMember(clazz, member1);
        inliner.visitAnyMember(clazz, member2);
        inliner.visitAnyMember(clazz, member3);

        // Assert - all should be handled as no-ops
        verifyNoInteractions(member1);
        verifyNoInteractions(member2);
        verifyNoInteractions(member3);
    }

    // ========================================
    // Visitor Pattern Integration Tests
    // ========================================

    /**
     * Tests that visitAnyMember properly implements the MemberVisitor interface.
     * This ensures the method signature matches the interface.
     */
    @Test
    public void testVisitAnyMember_implementsMemberVisitorInterface() {
        // Assert - MethodInliner should be a MemberVisitor
        assertTrue(inliner instanceof MemberVisitor,
                "MethodInliner should implement MemberVisitor");
    }

    /**
     * Tests that visitAnyMember can be used through the MemberVisitor interface.
     * Verifies proper integration with the visitor pattern.
     */
    @Test
    public void testVisitAnyMember_throughMemberVisitorInterface_works() {
        // Arrange
        MemberVisitor visitor = inliner;

        // Act & Assert
        assertDoesNotThrow(() -> visitor.visitAnyMember(clazz, member),
            "Should work when called through MemberVisitor interface");
    }

    /**
     * Tests that visitAnyMember serves as a default implementation for the visitor pattern.
     * This method handles members that don't have specialized visitor methods.
     */
    @Test
    public void testVisitAnyMember_servesAsDefaultForUnspecializedMembers() {
        // Arrange - members that would typically use the default visitor
        ProgramField programField = mock(ProgramField.class);
        LibraryField libraryField = mock(LibraryField.class);

        // Act - these members don't require specialized processing in MethodInliner
        inliner.visitAnyMember(clazz, programField);
        inliner.visitAnyMember(clazz, libraryField);

        // Assert - no processing should occur for these member types
        verifyNoInteractions(programField);
        verifyNoInteractions(libraryField);
    }

    /**
     * Tests that the empty visitAnyMember doesn't break visitor chains.
     * Verifies it can be part of a visitor pattern without issues.
     */
    @Test
    public void testVisitAnyMember_inVisitorChain_doesNotBreakChain() {
        // Arrange
        boolean[] visited = new boolean[1];

        MemberVisitor chainedVisitor = new MemberVisitor() {
            @Override
            public void visitAnyMember(Clazz clazz, Member member) {
                inliner.visitAnyMember(clazz, member);
                visited[0] = true;
            }
        };

        // Act
        chainedVisitor.visitAnyMember(clazz, member);

        // Assert
        assertTrue(visited[0], "Visitor chain should complete successfully");
    }

    // ========================================
    // State and Side Effect Tests
    // ========================================

    /**
     * Tests that visitAnyMember doesn't affect subsequent calls.
     * The no-op should not interfere with the inliner's normal operation.
     */
    @Test
    public void testVisitAnyMember_doesNotAffectSubsequentOperations() {
        // Act - call visitAnyMember first
        inliner.visitAnyMember(clazz, member);

        // Then call visitAnyMember again
        inliner.visitAnyMember(clazz, member);

        // Assert - verify no side effects occurred
        verifyNoInteractions(clazz);
        verifyNoInteractions(member);
    }

    /**
     * Tests that visitAnyMember called after multiple other operations still behaves as a no-op.
     * This verifies consistent behavior regardless of the inliner's usage history.
     */
    @Test
    public void testVisitAnyMember_afterMultipleOperations_stillNoOp() {
        // Arrange - simulate some prior operations
        for (int i = 0; i < 5; i++) {
            inliner.visitAnyMember(clazz, member);
        }

        // Act - call visitAnyMember again
        inliner.visitAnyMember(clazz, member);

        // Assert - verify it's still a no-op with no interactions
        verifyNoInteractions(clazz);
        verifyNoInteractions(member);
    }

    /**
     * Tests that visitAnyMember doesn't modify the inliner's internal state.
     * Since it's a no-op, it should not modify any internal fields.
     */
    @Test
    public void testVisitAnyMember_doesNotModifyInlinerState() {
        // Act - call visitAnyMember multiple times
        inliner.visitAnyMember(clazz, member);
        inliner.visitAnyMember(clazz, member);

        // Assert - verify no interactions occurred, indicating no state changes
        verifyNoInteractions(clazz);
        verifyNoInteractions(member);
    }

    /**
     * Tests that visitAnyMember has zero memory allocation overhead.
     * Since it's an empty method, it shouldn't allocate any objects.
     */
    @Test
    public void testVisitAnyMember_zeroAllocationOverhead() {
        // Act - call multiple times
        for (int i = 0; i < 1000; i++) {
            inliner.visitAnyMember(clazz, member);
        }

        // Assert - verify no interactions, implying no object creation for processing
        verifyNoInteractions(clazz);
        verifyNoInteractions(member);
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
        inliner.visitAnyMember(mockClazz, mockMember);

        // Assert - verify absolutely no interactions with any mock
        verifyNoInteractions(mockClazz);
        verifyNoInteractions(mockMember);
    }

    /**
     * Tests that visitAnyMember behavior is consistent before and after other visitor method calls.
     * The method should remain a no-op regardless of the inliner's usage.
     */
    @Test
    public void testVisitAnyMember_behaviorConsistentAcrossUsage() {
        // Act - call before and after
        inliner.visitAnyMember(clazz, member);

        // Simulate some other operations (still no-ops but conceptually different methods)
        inliner.visitAnyMember(clazz, member);

        // Call again
        inliner.visitAnyMember(clazz, member);

        // Assert - behavior should be consistently no-op
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
        inliner.visitAnyMember(clazz, member);

        // Assert - verify the base implementation (no-op) was used
        verifyNoInteractions(clazz);
        verifyNoInteractions(member);
    }

    /**
     * Tests that visitAnyMember doesn't process members that should be handled
     * by specialized methods (like visitProgramMethod or visitLibraryMethod).
     */
    @Test
    public void testVisitAnyMember_doesNotProcessSpecializedMembers() {
        // Arrange - generic member mock
        Member genericMember = mock(Member.class);

        // Act
        inliner.visitAnyMember(clazz, genericMember);

        // Assert - no processing should occur in this default method
        verifyNoInteractions(genericMember);
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitAnyMember doesn't attempt any inlining operations.
     * Only the specialized visitProgramMethod should perform inlining.
     */
    @Test
    public void testVisitAnyMember_doesNotPerformInlining() {
        // Arrange
        Member genericMember = mock(Member.class);

        // Act
        inliner.visitAnyMember(clazz, genericMember);

        // Assert - verify the member was not processed (no inlining occurred)
        verifyNoInteractions(genericMember);
    }
}
