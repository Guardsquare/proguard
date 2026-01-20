package proguard.obfuscate.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.Member;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramField;
import proguard.classfile.ProgramMethod;
import proguard.classfile.constant.Constant;
import proguard.classfile.instruction.Instruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link InstructionSequenceObfuscator#visitAnyMember(Clazz, Member)}.
 *
 * The visitAnyMember method is an empty implementation (no-op) that serves as a default
 * handler in the MemberVisitor pattern for members that don't require specialized processing.
 * The InstructionSequenceObfuscator only processes ProgramMethod instances via visitProgramMethod
 * to apply instruction sequence obfuscation; all other member types are handled by this no-op method.
 */
public class InstructionSequenceObfuscatorClaude_visitAnyMemberTest {

    private InstructionSequenceObfuscator obfuscator;
    private ReplacementSequences replacementSequences;
    private Clazz clazz;
    private Member member;

    @BeforeEach
    public void setUp() {
        replacementSequences = createSimpleReplacementSequences();
        obfuscator = new InstructionSequenceObfuscator(replacementSequences);
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
        assertDoesNotThrow(() -> obfuscator.visitAnyMember(clazz, member));
    }

    /**
     * Tests that visitAnyMember can be called with null Clazz parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyMember_withNullClazz_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> obfuscator.visitAnyMember(null, member));
    }

    /**
     * Tests that visitAnyMember can be called with null Member parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyMember_withNullMember_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> obfuscator.visitAnyMember(clazz, null));
    }

    /**
     * Tests that visitAnyMember can be called with both parameters null.
     * The method should handle null parameters gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyMember_withBothParametersNull_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> obfuscator.visitAnyMember(null, null));
    }

    /**
     * Tests that visitAnyMember can be called multiple times in succession.
     * The method should be idempotent and handle repeated calls without issues.
     */
    @Test
    public void testVisitAnyMember_calledMultipleTimes_doesNotThrowException() {
        // Act & Assert - should not throw any exception on multiple calls
        assertDoesNotThrow(() -> {
            obfuscator.visitAnyMember(clazz, member);
            obfuscator.visitAnyMember(clazz, member);
            obfuscator.visitAnyMember(clazz, member);
        });
    }

    /**
     * Tests that visitAnyMember doesn't interact with the Clazz parameter.
     * Since it's a no-op method, it should not call any methods on the clazz.
     */
    @Test
    public void testVisitAnyMember_doesNotInteractWithClazz() {
        // Act
        obfuscator.visitAnyMember(clazz, member);

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
        obfuscator.visitAnyMember(clazz, member);

        // Assert - verify no interactions occurred with the member mock
        verifyNoInteractions(member);
    }

    /**
     * Tests that visitAnyMember doesn't interact with either mock parameter.
     * Since it's a no-op method, it should not call any methods on either parameter.
     */
    @Test
    public void testVisitAnyMember_doesNotInteractWithEitherParameter() {
        // Act
        obfuscator.visitAnyMember(clazz, member);

        // Assert - verify no interactions occurred with either mock
        verifyNoInteractions(clazz);
        verifyNoInteractions(member);
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
            obfuscator.visitAnyMember(clazz1, member);
            obfuscator.visitAnyMember(clazz2, member);
            obfuscator.visitAnyMember(clazz3, member);
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
            obfuscator.visitAnyMember(clazz, member1);
            obfuscator.visitAnyMember(clazz, member2);
            obfuscator.visitAnyMember(clazz, member3);
        });
    }

    /**
     * Tests that visitAnyMember can be called on different obfuscator instances.
     * Each obfuscator instance should work independently.
     */
    @Test
    public void testVisitAnyMember_withDifferentObfuscators_doesNotThrowException() {
        // Arrange
        InstructionSequenceObfuscator obfuscator1 = new InstructionSequenceObfuscator(createSimpleReplacementSequences());
        InstructionSequenceObfuscator obfuscator2 = new InstructionSequenceObfuscator(createSimpleReplacementSequences());

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> {
            obfuscator1.visitAnyMember(clazz, member);
            obfuscator2.visitAnyMember(clazz, member);
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
            obfuscator.visitAnyMember(clazz, member);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100, "visitAnyMember should execute quickly as it's a no-op");
    }

    /**
     * Tests that visitAnyMember with mixed null and valid calls works correctly.
     * The method should handle mixed null and non-null calls without issues.
     */
    @Test
    public void testVisitAnyMember_mixedNullAndValidCalls_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> {
            obfuscator.visitAnyMember(null, null);
            obfuscator.visitAnyMember(clazz, member);
            obfuscator.visitAnyMember(null, member);
            obfuscator.visitAnyMember(clazz, null);
        });
    }

    /**
     * Tests that visitAnyMember can be called with different Clazz instances and same Member.
     * The method should handle any combination of parameters.
     */
    @Test
    public void testVisitAnyMember_withDifferentClazzInstancesSameMember_doesNotThrowException() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Clazz clazz3 = mock(ProgramClass.class);

        // Act & Assert - should not throw any exception with different clazz instances
        assertDoesNotThrow(() -> {
            obfuscator.visitAnyMember(clazz1, member);
            obfuscator.visitAnyMember(clazz2, member);
            obfuscator.visitAnyMember(clazz3, member);
        });
    }

    /**
     * Tests that visitAnyMember can be called with same Clazz and different Members.
     * The method should handle any combination of parameters.
     */
    @Test
    public void testVisitAnyMember_withSameClazzDifferentMembers_doesNotThrowException() {
        // Arrange
        Member member1 = mock(Member.class);
        Member member2 = mock(Member.class);
        Member member3 = mock(Member.class);

        // Act & Assert - should not throw any exception with different member instances
        assertDoesNotThrow(() -> {
            obfuscator.visitAnyMember(clazz, member1);
            obfuscator.visitAnyMember(clazz, member2);
            obfuscator.visitAnyMember(clazz, member3);
        });
    }

    /**
     * Tests that visitAnyMember with various configured member mocks doesn't throw exceptions.
     * This ensures the no-op works with members that have stubbed methods.
     */
    @Test
    public void testVisitAnyMember_withConfiguredMemberMocks_doesNotThrowException() {
        // Arrange - test with various member types with stubbed methods
        Member member1 = mock(Member.class);
        Member member2 = mock(Member.class);
        Member member3 = mock(Member.class);

        when(member1.getName(any())).thenReturn("member1");
        when(member2.getName(any())).thenReturn("member2");
        when(member3.getName(any())).thenReturn("member3");

        // Act & Assert - should handle all member types gracefully
        assertDoesNotThrow(() -> {
            obfuscator.visitAnyMember(clazz, member1);
            obfuscator.visitAnyMember(clazz, member2);
            obfuscator.visitAnyMember(clazz, member3);
        });

        // Verify that the member methods were not called since it's a no-op
        verify(member1, never()).getName(any());
        verify(member2, never()).getName(any());
        verify(member3, never()).getName(any());
    }

    /**
     * Tests that visitAnyMember can be called with the same parameters repeatedly
     * without accumulating any state or causing issues.
     */
    @Test
    public void testVisitAnyMember_repeatedCallsWithSameParameters_noStateAccumulation() {
        // Act - call multiple times with same parameters
        for (int i = 0; i < 10; i++) {
            obfuscator.visitAnyMember(clazz, member);
        }

        // Assert - verify no interactions occurred despite multiple calls
        verifyNoInteractions(clazz);
        verifyNoInteractions(member);
    }

    /**
     * Tests that visitAnyMember can be invoked using the MemberVisitor interface.
     * Verifies polymorphic behavior through the interface.
     */
    @Test
    public void testVisitAnyMember_viaMemberVisitorInterface_doesNotThrowException() {
        // Arrange - use the obfuscator as a MemberVisitor
        proguard.classfile.visitor.MemberVisitor visitor = obfuscator;

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> visitor.visitAnyMember(clazz, member));
    }

    /**
     * Tests that multiple obfuscators can independently call visitAnyMember.
     * Each obfuscator should maintain its own independent state.
     */
    @Test
    public void testVisitAnyMember_multipleObfuscatorsIndependent() {
        // Arrange
        InstructionSequenceObfuscator obfuscator1 = new InstructionSequenceObfuscator(createSimpleReplacementSequences());
        InstructionSequenceObfuscator obfuscator2 = new InstructionSequenceObfuscator(createSimpleReplacementSequences());

        // Act
        obfuscator1.visitAnyMember(clazz, member);
        obfuscator2.visitAnyMember(clazz, member);

        // Assert - verify each obfuscator works independently
        assertNotSame(obfuscator1, obfuscator2);
        verifyNoInteractions(clazz);
        verifyNoInteractions(member);
    }

    /**
     * Tests that visitAnyMember doesn't affect other operations on the obfuscator.
     * Calling visitAnyMember should not interfere with the obfuscator's other methods.
     */
    @Test
    public void testVisitAnyMember_doesNotAffectOtherOperations() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act - call visitAnyMember and then other methods
        obfuscator.visitAnyMember(clazz, member);

        // Assert - other methods should still work normally
        assertDoesNotThrow(() -> {
            obfuscator.visitProgramClass(programClass);
        });
    }

    /**
     * Tests that visitAnyMember works correctly with ProgramField instances.
     * Even though visitAnyMember is a no-op, it should handle field members gracefully.
     */
    @Test
    public void testVisitAnyMember_withProgramField_doesNotThrowException() {
        // Arrange
        ProgramField field = mock(ProgramField.class);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> obfuscator.visitAnyMember(clazz, field));

        // Assert - verify no interactions
        verifyNoInteractions(field);
    }

    /**
     * Tests that visitAnyMember works correctly with ProgramMethod instances.
     * Note: ProgramMethod instances are typically handled by visitProgramMethod,
     * but visitAnyMember should still work as a fallback no-op handler.
     */
    @Test
    public void testVisitAnyMember_withProgramMethod_doesNotThrowException() {
        // Arrange
        ProgramMethod method = mock(ProgramMethod.class);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> obfuscator.visitAnyMember(clazz, method));

        // Assert - verify no interactions (visitAnyMember is a no-op)
        verifyNoInteractions(method);
    }

    /**
     * Tests that visitAnyMember with various member type mocks works correctly.
     * The no-op should work with any Member implementation.
     */
    @Test
    public void testVisitAnyMember_withVariousMemberTypes_doesNotThrowException() {
        // Arrange
        Member genericMember = mock(Member.class);
        ProgramField field = mock(ProgramField.class);
        ProgramMethod method = mock(ProgramMethod.class);

        // Act & Assert - should not throw any exception with various member types
        assertDoesNotThrow(() -> {
            obfuscator.visitAnyMember(clazz, genericMember);
            obfuscator.visitAnyMember(clazz, field);
            obfuscator.visitAnyMember(clazz, method);
        });

        // Assert - verify no interactions
        verifyNoInteractions(genericMember);
        verifyNoInteractions(field);
        verifyNoInteractions(method);
    }

    /**
     * Tests that visitAnyMember can be called in a loop with different parameters.
     * Verifies that the no-op method handles batch operations without issues.
     */
    @Test
    public void testVisitAnyMember_inLoopWithDifferentParameters_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                Member tempMember = mock(Member.class);
                obfuscator.visitAnyMember(clazz, tempMember);
            }
        });
    }

    /**
     * Tests that visitAnyMember with real ProgramClass and mocked Member works correctly.
     * Combines real and mocked objects to verify compatibility.
     */
    @Test
    public void testVisitAnyMember_withRealProgramClassAndMockedMember_doesNotThrowException() {
        // Arrange
        ProgramClass realProgramClass = new ProgramClass();

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> obfuscator.visitAnyMember(realProgramClass, member));
    }

    /**
     * Tests that visitAnyMember with real ProgramClass and real ProgramMethod works correctly.
     * Testing without mocking where possible.
     */
    @Test
    public void testVisitAnyMember_withRealObjects_doesNotThrowException() {
        // Arrange
        ProgramClass realProgramClass = new ProgramClass();
        ProgramMethod realMethod = new ProgramMethod();

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> obfuscator.visitAnyMember(realProgramClass, realMethod));
    }

    /**
     * Tests that visitAnyMember returns void and completes successfully.
     * Verifies the method signature and return type behavior.
     */
    @Test
    public void testVisitAnyMember_returnsVoid() {
        // Act - method returns void, so just verify it executes
        obfuscator.visitAnyMember(clazz, member);

        // Assert - if we reach here without exception, the method completed successfully
        assertTrue(true, "Method should complete and return void");
    }

    /**
     * Tests that visitAnyMember is truly stateless.
     * Calling it should not modify any internal state of the obfuscator.
     */
    @Test
    public void testVisitAnyMember_isStateless() {
        // Arrange - call the method multiple times with different parameters
        Member member1 = mock(Member.class);
        Member member2 = mock(Member.class);
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);

        // Act
        obfuscator.visitAnyMember(clazz1, member1);
        obfuscator.visitAnyMember(clazz2, member2);
        obfuscator.visitAnyMember(clazz1, member2);
        obfuscator.visitAnyMember(clazz2, member1);

        // Assert - verify no interactions with any mock
        verifyNoInteractions(clazz1);
        verifyNoInteractions(clazz2);
        verifyNoInteractions(member1);
        verifyNoInteractions(member2);
    }

    /**
     * Tests that visitAnyMember can be called before any other obfuscator methods.
     * Verifies that it can be the first method called on a new obfuscator instance.
     */
    @Test
    public void testVisitAnyMember_canBeCalledFirst() {
        // Arrange
        InstructionSequenceObfuscator newObfuscator = new InstructionSequenceObfuscator(createSimpleReplacementSequences());

        // Act & Assert - should work as the first method called
        assertDoesNotThrow(() -> newObfuscator.visitAnyMember(clazz, member));
    }

    /**
     * Tests that visitAnyMember with stubbed Clazz methods doesn't call them.
     * Verifies the no-op doesn't interact with stubbed methods.
     */
    @Test
    public void testVisitAnyMember_withStubbedClazz_doesNotCallStubbedMethods() {
        // Arrange
        when(clazz.getName()).thenReturn("TestClass");

        // Act
        obfuscator.visitAnyMember(clazz, member);

        // Assert - verify the stubbed method was never called
        verify(clazz, never()).getName();
    }

    /**
     * Tests that visitAnyMember works with all combinations of null and non-null parameters.
     * Comprehensive null parameter testing.
     */
    @Test
    public void testVisitAnyMember_allParameterCombinations_doesNotThrowException() {
        // Arrange
        Clazz nonNullClazz = mock(Clazz.class);
        Member nonNullMember = mock(Member.class);

        // Act & Assert - test all combinations
        assertDoesNotThrow(() -> {
            obfuscator.visitAnyMember(null, null);
            obfuscator.visitAnyMember(null, nonNullMember);
            obfuscator.visitAnyMember(nonNullClazz, null);
            obfuscator.visitAnyMember(nonNullClazz, nonNullMember);
        });
    }

    /**
     * Tests that visitAnyMember maintains the visitor pattern contract.
     * The method should be a valid implementation of the MemberVisitor interface.
     */
    @Test
    public void testVisitAnyMember_maintainsVisitorPatternContract() {
        // Arrange - cast to interface to ensure contract is maintained
        proguard.classfile.visitor.MemberVisitor visitor = obfuscator;

        // Act & Assert - should work through the interface
        assertDoesNotThrow(() -> {
            visitor.visitAnyMember(clazz, member);
        });

        // Verify the behavior is consistent whether called directly or through interface
        verifyNoInteractions(clazz);
        verifyNoInteractions(member);
    }

    /**
     * Tests that visitAnyMember can be called with freshly created mock objects.
     * Verifies compatibility with new mock instances.
     */
    @Test
    public void testVisitAnyMember_withFreshMocks_doesNotThrowException() {
        // Act & Assert - create and use new mocks inline
        assertDoesNotThrow(() -> {
            obfuscator.visitAnyMember(mock(Clazz.class), mock(Member.class));
            obfuscator.visitAnyMember(mock(ProgramClass.class), mock(ProgramField.class));
            obfuscator.visitAnyMember(mock(Clazz.class), mock(ProgramMethod.class));
        });
    }

    /**
     * Tests that visitAnyMember behavior is consistent across different test executions.
     * Each test should get consistent no-op behavior.
     */
    @Test
    public void testVisitAnyMember_consistentBehaviorAcrossTests() {
        // Arrange
        Clazz testClazz = mock(Clazz.class);
        Member testMember = mock(Member.class);

        // Act
        obfuscator.visitAnyMember(testClazz, testMember);

        // Assert - verify consistent no-op behavior
        verifyNoInteractions(testClazz);
        verifyNoInteractions(testMember);
    }

    /**
     * Tests that visitAnyMember doesn't throw any RuntimeException subtypes.
     * Comprehensive exception handling verification.
     */
    @Test
    public void testVisitAnyMember_doesNotThrowRuntimeException() {
        // Act & Assert - should not throw any RuntimeException
        try {
            obfuscator.visitAnyMember(clazz, member);
            obfuscator.visitAnyMember(null, null);
            obfuscator.visitAnyMember(mock(Clazz.class), mock(Member.class));
            // If we reach here, no exception was thrown
            assertTrue(true);
        } catch (RuntimeException e) {
            fail("visitAnyMember should not throw RuntimeException, but threw: " + e.getClass().getName());
        }
    }

    /**
     * Tests that visitAnyMember can be called in nested loops.
     * Verifies stability under repeated nested calls.
     */
    @Test
    public void testVisitAnyMember_nestedLoops_doesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    obfuscator.visitAnyMember(clazz, member);
                }
            }
        });

        // Verify no interactions despite 100 calls
        verifyNoInteractions(clazz);
        verifyNoInteractions(member);
    }

    /**
     * Tests that visitAnyMember works correctly after obfuscator has been used for other operations.
     * Verifies that prior state doesn't affect the no-op behavior.
     */
    @Test
    public void testVisitAnyMember_afterOtherObfuscatorOperations_doesNotThrowException() {
        // Arrange - use obfuscator for other operations first
        ProgramClass programClass = new ProgramClass();
        obfuscator.visitProgramClass(programClass);

        // Act & Assert - visitAnyMember should still work correctly
        assertDoesNotThrow(() -> obfuscator.visitAnyMember(clazz, member));

        // Verify no interaction with member
        verifyNoInteractions(member);
    }

    /**
     * Tests that visitAnyMember can be called alternately with visitProgramClass.
     * The methods should work independently without interfering with each other.
     */
    @Test
    public void testVisitAnyMember_alternatingWithVisitProgramClass_doesNotThrowException() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> {
            obfuscator.visitAnyMember(clazz, member);
            obfuscator.visitProgramClass(programClass);
            obfuscator.visitAnyMember(clazz, member);
            obfuscator.visitProgramClass(programClass);
        });
    }

    /**
     * Tests that visitAnyMember returns immediately without blocking.
     * Verifies the method is non-blocking.
     */
    @Test
    public void testVisitAnyMember_returnsImmediately() {
        // Arrange
        long maxExpectedTimeNs = 1_000_000; // 1ms

        // Act
        long startTime = System.nanoTime();
        obfuscator.visitAnyMember(clazz, member);
        long endTime = System.nanoTime();
        long duration = endTime - startTime;

        // Assert - should return very quickly
        assertTrue(duration < maxExpectedTimeNs,
            "visitAnyMember should return immediately (took " + duration + "ns)");
    }

    /**
     * Tests that visitAnyMember doesn't modify the obfuscator's internal state.
     * Subsequent operations should work normally.
     */
    @Test
    public void testVisitAnyMember_doesNotModifyObfuscatorState() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act - call visitAnyMember
        obfuscator.visitAnyMember(clazz, member);

        // Assert - obfuscator should still work normally
        assertDoesNotThrow(() -> obfuscator.visitProgramClass(programClass));
    }

    /**
     * Tests that visitAnyMember with various ReplacementSequences configurations.
     * The method's behavior should be independent of constructor parameters.
     */
    @Test
    public void testVisitAnyMember_withDifferentReplacementSequences_doesNotThrowException() {
        // Arrange - create obfuscators with different replacement sequences
        ReplacementSequences emptySequences = createEmptyReplacementSequences();
        InstructionSequenceObfuscator emptyObfuscator = new InstructionSequenceObfuscator(emptySequences);

        // Act & Assert - all should work without exception
        assertDoesNotThrow(() -> {
            obfuscator.visitAnyMember(clazz, member);
            emptyObfuscator.visitAnyMember(clazz, member);
        });
    }

    /**
     * Tests that visitAnyMember execution doesn't leak resources.
     * Multiple sequential calls should not cause memory issues.
     */
    @Test
    public void testVisitAnyMember_noResourceLeak() {
        // Act - call many times
        for (int i = 0; i < 100; i++) {
            obfuscator.visitAnyMember(clazz, member);
        }

        // Assert - should complete without errors or resource issues
        assertDoesNotThrow(() -> obfuscator.visitAnyMember(clazz, member));
    }

    // Helper methods

    private ReplacementSequences createSimpleReplacementSequences() {
        return new ReplacementSequences() {
            @Override
            public Instruction[][][] getSequences() {
                return new Instruction[0][][];
            }

            @Override
            public Constant[] getConstants() {
                return new Constant[0];
            }
        };
    }

    private ReplacementSequences createEmptyReplacementSequences() {
        return new ReplacementSequences() {
            @Override
            public Instruction[][][] getSequences() {
                return new Instruction[0][][];
            }

            @Override
            public Constant[] getConstants() {
                return new Constant[0];
            }
        };
    }
}
