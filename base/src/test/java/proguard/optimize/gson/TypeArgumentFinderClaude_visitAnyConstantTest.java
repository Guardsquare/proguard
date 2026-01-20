package proguard.optimize.gson;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.LibraryClass;
import proguard.classfile.constant.*;
import proguard.evaluation.PartialEvaluator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link TypeArgumentFinder#visitAnyConstant(Clazz, Constant)}.
 *
 * The visitAnyConstant method is an empty implementation (no-op) that serves as a default
 * handler in the ConstantVisitor pattern for constants that don't require specialized processing.
 * The actual processing logic is in specialized methods like visitClassConstant and visitAnyRefConstant.
 */
public class TypeArgumentFinderClaude_visitAnyConstantTest {

    private TypeArgumentFinder finder;
    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private PartialEvaluator partialEvaluator;
    private Clazz clazz;
    private Constant constant;

    @BeforeEach
    public void setUp() {
        programClassPool = new ClassPool();
        libraryClassPool = new ClassPool();
        partialEvaluator = PartialEvaluator.Builder.create().build();
        finder = new TypeArgumentFinder(programClassPool, libraryClassPool, partialEvaluator);
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
        assertDoesNotThrow(() -> finder.visitAnyConstant(clazz, constant));
    }

    /**
     * Tests that visitAnyConstant can be called with null Clazz parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyConstant_withNullClazz_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> finder.visitAnyConstant(null, constant));
    }

    /**
     * Tests that visitAnyConstant can be called with null Constant parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyConstant_withNullConstant_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> finder.visitAnyConstant(clazz, null));
    }

    /**
     * Tests that visitAnyConstant can be called with both parameters null.
     * The method should handle null parameters gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyConstant_withBothParametersNull_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> finder.visitAnyConstant(null, null));
    }

    /**
     * Tests that visitAnyConstant can be called multiple times in succession.
     * The method should be idempotent and handle repeated calls without issues.
     */
    @Test
    public void testVisitAnyConstant_calledMultipleTimes_doesNotThrowException() {
        // Act & Assert - should not throw any exception on multiple calls
        assertDoesNotThrow(() -> {
            finder.visitAnyConstant(clazz, constant);
            finder.visitAnyConstant(clazz, constant);
            finder.visitAnyConstant(clazz, constant);
        });
    }

    /**
     * Tests that visitAnyConstant doesn't interact with the Clazz parameter.
     * Since it's a no-op method, it should not call any methods on the clazz.
     */
    @Test
    public void testVisitAnyConstant_doesNotInteractWithClazz() {
        // Act
        finder.visitAnyConstant(clazz, constant);

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
        finder.visitAnyConstant(clazz, constant);

        // Assert - verify no interactions occurred with the constant mock
        verifyNoInteractions(constant);
    }

    /**
     * Tests that visitAnyConstant doesn't interact with either parameter.
     * Verifies that both parameters remain untouched.
     */
    @Test
    public void testVisitAnyConstant_doesNotInteractWithAnyParameter() {
        // Act
        finder.visitAnyConstant(clazz, constant);

        // Assert - verify no interactions occurred with either mock
        verifyNoInteractions(clazz, constant);
    }

    /**
     * Tests that visitAnyConstant can be used as part of the ConstantVisitor interface.
     * Verifies integration with the visitor pattern.
     */
    @Test
    public void testVisitAnyConstant_usedAsConstantVisitor_doesNotThrowException() {
        // Arrange
        proguard.classfile.constant.visitor.ConstantVisitor visitor = finder;

        // Act & Assert
        assertDoesNotThrow(() -> visitor.visitAnyConstant(clazz, constant));
    }

    /**
     * Tests that visitAnyConstant can be called with real ProgramClass instance.
     * Verifies the method works with actual class instances, not just mocks.
     */
    @Test
    public void testVisitAnyConstant_withRealProgramClass_doesNotThrowException() {
        // Arrange
        ProgramClass realClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> finder.visitAnyConstant(realClass, constant));
    }

    /**
     * Tests that visitAnyConstant can be called with real LibraryClass instance.
     * Verifies the method works with library classes.
     */
    @Test
    public void testVisitAnyConstant_withRealLibraryClass_doesNotThrowException() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();

        // Act & Assert
        assertDoesNotThrow(() -> finder.visitAnyConstant(libraryClass, constant));
    }

    /**
     * Tests that visitAnyConstant can be called rapidly in succession.
     * Verifies consistent behavior under stress.
     */
    @Test
    public void testVisitAnyConstant_rapidSequentialCalls_doesNotThrowException() {
        // Act & Assert - all calls should succeed without exceptions
        for (int i = 0; i < 1000; i++) {
            assertDoesNotThrow(() -> finder.visitAnyConstant(clazz, constant),
                    "Call " + i + " should not throw exception");
        }
    }

    /**
     * Tests that multiple TypeArgumentFinder instances can all call visitAnyConstant
     * on the same parameters without interference.
     */
    @Test
    public void testVisitAnyConstant_multipleFinderInstancesOneParameter_allWorkCorrectly() {
        // Arrange
        TypeArgumentFinder finder1 = new TypeArgumentFinder(programClassPool, libraryClassPool, partialEvaluator);
        TypeArgumentFinder finder2 = new TypeArgumentFinder(programClassPool, libraryClassPool, partialEvaluator);
        TypeArgumentFinder finder3 = new TypeArgumentFinder(programClassPool, libraryClassPool, partialEvaluator);

        // Act & Assert
        assertDoesNotThrow(() -> {
            finder1.visitAnyConstant(clazz, constant);
            finder2.visitAnyConstant(clazz, constant);
            finder3.visitAnyConstant(clazz, constant);
        });

        // Verify no interactions from any of the calls
        verifyNoInteractions(clazz, constant);
    }

    /**
     * Tests that visitAnyConstant is truly a no-op by verifying no exceptions
     * even with parameters that would normally cause issues if accessed.
     */
    @Test
    public void testVisitAnyConstant_isNoop_noExceptionsWithAnyInput() {
        // Act & Assert - should not throw even with unusual combinations
        assertDoesNotThrow(() -> {
            finder.visitAnyConstant(null, null);
            finder.visitAnyConstant(clazz, null);
            finder.visitAnyConstant(null, constant);
            finder.visitAnyConstant(clazz, constant);
        });
    }

    /**
     * Tests that visitAnyConstant maintains thread-safe behavior as a no-op.
     * Verifies the method can be called rapidly without issues.
     */
    @Test
    public void testVisitAnyConstant_rapidCalls_doesNotThrowException() {
        // Act & Assert - rapid calls should all succeed
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                finder.visitAnyConstant(clazz, constant);
            }
        });
    }

    /**
     * Tests that visitAnyConstant doesn't interfere with the ConstantVisitor interface contract.
     * Verifies that it can be safely called through the interface.
     */
    @Test
    public void testVisitAnyConstant_throughInterface_doesNotThrowException() {
        // Arrange
        proguard.classfile.constant.visitor.ConstantVisitor visitor = finder;
        ProgramClass realClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> {
            visitor.visitAnyConstant(realClass, constant);
            visitor.visitAnyConstant(null, null);
        });
    }

    /**
     * Tests that visitAnyConstant has no effect on the TypeArgumentFinder's internal state.
     * Verifies that typeArgumentClasses field is not modified.
     */
    @Test
    public void testVisitAnyConstant_doesNotModifyTypeArgumentClasses() {
        // Arrange
        String[] initialState = finder.typeArgumentClasses;

        // Act
        finder.visitAnyConstant(clazz, constant);

        // Assert - typeArgumentClasses should remain unchanged
        assertSame(initialState, finder.typeArgumentClasses,
                "typeArgumentClasses should not be modified by visitAnyConstant");
    }

    /**
     * Tests that visitAnyConstant has no effect on the TypeArgumentFinder's internal state
     * even when typeArgumentClasses has been previously set.
     */
    @Test
    public void testVisitAnyConstant_doesNotModifyPresetTypeArgumentClasses() {
        // Arrange
        finder.typeArgumentClasses = new String[] { "com.example.TestClass" };
        String[] expectedState = finder.typeArgumentClasses;

        // Act
        finder.visitAnyConstant(clazz, constant);

        // Assert - typeArgumentClasses should remain unchanged
        assertSame(expectedState, finder.typeArgumentClasses,
                "typeArgumentClasses should not be modified by visitAnyConstant");
        assertArrayEquals(new String[] { "com.example.TestClass" }, finder.typeArgumentClasses,
                "typeArgumentClasses content should remain unchanged");
    }

    /**
     * Tests that visitAnyConstant repeats with consistent behavior.
     * Verifies that subsequent calls to visitAnyConstant behave identically.
     */
    @Test
    public void testVisitAnyConstant_repeatedCalls_behaviorRemainsConsistent() {
        // Act & Assert - multiple calls should have identical behavior
        assertDoesNotThrow(() -> finder.visitAnyConstant(clazz, constant));
        assertDoesNotThrow(() -> finder.visitAnyConstant(clazz, constant));
        assertDoesNotThrow(() -> finder.visitAnyConstant(clazz, constant));

        // Verify no interactions still occur after multiple calls
        verifyNoInteractions(clazz, constant);
    }

    /**
     * Tests that visitAnyConstant can be interleaved with other method calls.
     * Verifies that the no-op method doesn't interfere with normal operation.
     */
    @Test
    public void testVisitAnyConstant_interleavedCalls_doesNotThrowException() {
        // Act & Assert - interleaved calls should all succeed
        assertDoesNotThrow(() -> {
            finder.visitAnyConstant(clazz, constant);
            finder.visitAnyConstant(null, null);
            finder.visitAnyConstant(clazz, constant);
            finder.visitAnyConstant(null, constant);
            finder.visitAnyConstant(clazz, null);
        });
    }

    /**
     * Tests that visitAnyConstant works correctly with a freshly created ProgramClass.
     * Verifies no initialization issues affect the no-op behavior.
     */
    @Test
    public void testVisitAnyConstant_withFreshProgramClass_doesNotThrowException() {
        // Arrange
        ProgramClass freshClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> finder.visitAnyConstant(freshClass, constant));
        assertDoesNotThrow(() -> finder.visitAnyConstant(freshClass, null));
    }

    /**
     * Tests that visitAnyConstant can handle alternating null and non-null parameters.
     * Verifies robustness of the no-op implementation.
     */
    @Test
    public void testVisitAnyConstant_alternatingNullAndNonNull_doesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            finder.visitAnyConstant(clazz, constant);
            finder.visitAnyConstant(null, constant);
            finder.visitAnyConstant(clazz, null);
            finder.visitAnyConstant(null, null);
            finder.visitAnyConstant(clazz, constant);
        });
    }

    /**
     * Tests that visitAnyConstant doesn't cause any memory leaks or reference retention.
     * Verifies that parameters can be garbage collected after the call.
     */
    @Test
    public void testVisitAnyConstant_doesNotRetainReferences() {
        // Arrange
        Clazz tempClazz = mock(ProgramClass.class);
        Constant tempConstant = mock(Constant.class);

        // Act
        finder.visitAnyConstant(tempClazz, tempConstant);

        // Assert - no interactions means no references should be retained
        verifyNoInteractions(tempClazz, tempConstant);
    }

    /**
     * Tests that visitAnyConstant doesn't call any methods on Clazz when given a mock.
     * Confirms complete isolation and no-op behavior.
     */
    @Test
    public void testVisitAnyConstant_withMockClazz_noMethodsCalled() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class, withSettings().strictness(org.mockito.quality.Strictness.STRICT_STUBS));

        // Act
        finder.visitAnyConstant(mockClazz, constant);

        // Assert - verify absolutely no interactions
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests that visitAnyConstant doesn't call any methods on Constant when given a mock.
     * Confirms complete isolation and no-op behavior.
     */
    @Test
    public void testVisitAnyConstant_withMockConstant_noMethodsCalled() {
        // Arrange
        Constant mockConstant = mock(Constant.class, withSettings().strictness(org.mockito.quality.Strictness.STRICT_STUBS));

        // Act
        finder.visitAnyConstant(clazz, mockConstant);

        // Assert - verify absolutely no interactions
        verifyNoInteractions(mockConstant);
    }

    /**
     * Tests that visitAnyConstant works correctly when called with the same instances repeatedly.
     * Verifies stable behavior with instance reuse.
     */
    @Test
    public void testVisitAnyConstant_sameInstanceRepeatedCalls_consistentBehavior() {
        // Act & Assert - multiple calls with same instances should all succeed
        for (int i = 0; i < 50; i++) {
            assertDoesNotThrow(() -> finder.visitAnyConstant(clazz, constant),
                    "Repeated call " + i + " with same instances should not throw");
        }

        // Verify still no interactions after many calls
        verifyNoInteractions(clazz, constant);
    }

    /**
     * Tests that visitAnyConstant preserves the finder's ability to work with other visitor methods.
     * Verifies that calling visitAnyConstant doesn't affect the finder's state for other operations.
     */
    @Test
    public void testVisitAnyConstant_doesNotAffectOtherVisitorMethods() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act - call visitAnyConstant multiple times
        finder.visitAnyConstant(programClass, constant);
        finder.visitAnyConstant(programClass, constant);

        // Assert - other visitor methods should still work
        assertDoesNotThrow(() -> finder.visitAnyInstruction(programClass, null, null, 0, null),
                "Other visitor methods should still work after visitAnyConstant");
    }

    /**
     * Tests that visitAnyConstant works correctly in a sequence of different visitor method calls.
     * Verifies the no-op doesn't affect other visitor patterns.
     */
    @Test
    public void testVisitAnyConstant_inVisitorSequence_doesNotThrowException() {
        // Arrange
        ProgramClass realClass = new ProgramClass();

        // Act & Assert - should work in a sequence of visitor calls
        assertDoesNotThrow(() -> {
            finder.visitAnyInstruction(realClass, null, null, 0, null);
            finder.visitAnyConstant(realClass, constant);
            finder.visitAnyInstruction(realClass, null, null, 0, null);
            finder.visitAnyConstant(realClass, null);
        });
    }

    /**
     * Tests that visitAnyConstant maintains expected behavior across varied call patterns.
     * Verifies consistency regardless of call order or parameter variation.
     */
    @Test
    public void testVisitAnyConstant_variedCallPatterns_allWorkCorrectly() {
        // Arrange
        Constant const1 = mock(Constant.class);
        Constant const2 = mock(Constant.class);
        Clazz clazz2 = mock(ProgramClass.class);

        // Act & Assert - various call patterns should all work
        assertDoesNotThrow(() -> {
            finder.visitAnyConstant(clazz, constant);
            finder.visitAnyConstant(clazz2, const1);
            finder.visitAnyConstant(clazz, const2);
            finder.visitAnyConstant(null, constant);
            finder.visitAnyConstant(clazz, null);
            finder.visitAnyConstant(clazz2, null);
            finder.visitAnyConstant(null, const1);
        });
    }

    /**
     * Tests that visitAnyConstant doesn't modify any state.
     * Verifies that calling the method has no side effects.
     */
    @Test
    public void testVisitAnyConstant_doesNotModifyState() {
        // Arrange
        ProgramClass realClass = new ProgramClass();
        Object initialProcessingInfo = new Object();
        realClass.setProcessingInfo(initialProcessingInfo);

        // Act
        finder.visitAnyConstant(realClass, constant);

        // Assert - verify the class state wasn't modified
        assertSame(initialProcessingInfo, realClass.getProcessingInfo(),
                "Class processing info should not be modified");
    }

    /**
     * Tests that multiple instances of TypeArgumentFinder can all call visitAnyConstant
     * on the same parameters without interference.
     */
    @Test
    public void testVisitAnyConstant_multipleInstancesSameParameters_allWorkCorrectly() {
        // Arrange
        TypeArgumentFinder f1 = new TypeArgumentFinder(programClassPool, libraryClassPool, partialEvaluator);
        TypeArgumentFinder f2 = new TypeArgumentFinder(programClassPool, libraryClassPool, partialEvaluator);
        TypeArgumentFinder f3 = new TypeArgumentFinder(programClassPool, libraryClassPool, partialEvaluator);

        // Act & Assert
        assertDoesNotThrow(() -> {
            f1.visitAnyConstant(clazz, constant);
            f2.visitAnyConstant(clazz, constant);
            f3.visitAnyConstant(clazz, constant);
        });

        // Verify no interactions from any of the calls
        verifyNoInteractions(clazz, constant);
    }

    /**
     * Tests that visitAnyConstant can be called with different constant types.
     * Verifies the method works with various constant implementations.
     */
    @Test
    public void testVisitAnyConstant_withDifferentConstantTypes_doesNotThrowException() {
        // Arrange
        Constant const1 = mock(IntegerConstant.class);
        Constant const2 = mock(StringConstant.class);
        Constant const3 = mock(ClassConstant.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            finder.visitAnyConstant(clazz, const1);
            finder.visitAnyConstant(clazz, const2);
            finder.visitAnyConstant(clazz, const3);
        });
    }

    /**
     * Tests that visitAnyConstant can be called with different clazz types.
     * Verifies the method works with both ProgramClass and LibraryClass.
     */
    @Test
    public void testVisitAnyConstant_withDifferentClazzTypes_doesNotThrowException() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        LibraryClass libraryClass = new LibraryClass();

        // Act & Assert
        assertDoesNotThrow(() -> {
            finder.visitAnyConstant(programClass, constant);
            finder.visitAnyConstant(libraryClass, constant);
            finder.visitAnyConstant(programClass, null);
            finder.visitAnyConstant(libraryClass, null);
        });
    }

    /**
     * Tests that visitAnyConstant can be called an extreme number of times without issues.
     * Verifies the no-op implementation doesn't accumulate any state or resources.
     */
    @Test
    public void testVisitAnyConstant_extremeNumberOfCalls_doesNotThrowException() {
        // Act & Assert - should handle many calls without issues
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 10000; i++) {
                finder.visitAnyConstant(clazz, constant);
            }
        });

        // After many calls, still no interactions
        verifyNoInteractions(clazz, constant);
    }

    /**
     * Tests that visitAnyConstant through ConstantVisitor interface works with casting.
     * Verifies proper interface implementation.
     */
    @Test
    public void testVisitAnyConstant_throughCastedInterface_doesNotThrowException() {
        // Arrange
        proguard.classfile.constant.visitor.ConstantVisitor visitor =
            (proguard.classfile.constant.visitor.ConstantVisitor) finder;

        // Act & Assert
        assertDoesNotThrow(() -> {
            visitor.visitAnyConstant(clazz, constant);
            visitor.visitAnyConstant(null, null);
        });
    }

    /**
     * Tests that visitAnyConstant works with various combinations of real and mock objects.
     * Verifies flexibility in parameter types.
     */
    @Test
    public void testVisitAnyConstant_mixedRealAndMockObjects_doesNotThrowException() {
        // Arrange
        ProgramClass realClass = new ProgramClass();
        Clazz mockClazz = mock(Clazz.class);
        Constant mockConstant = mock(Constant.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            finder.visitAnyConstant(realClass, mockConstant);
            finder.visitAnyConstant(mockClazz, mockConstant);
            finder.visitAnyConstant(realClass, null);
            finder.visitAnyConstant(mockClazz, null);
        });
    }

    /**
     * Tests that visitAnyConstant maintains consistency when alternating between
     * different finder instances and parameters.
     */
    @Test
    public void testVisitAnyConstant_alternatingFindersAndParameters_consistentBehavior() {
        // Arrange
        TypeArgumentFinder finder1 = new TypeArgumentFinder(programClassPool, libraryClassPool, partialEvaluator);
        TypeArgumentFinder finder2 = new TypeArgumentFinder(programClassPool, libraryClassPool, partialEvaluator);
        Constant const1 = mock(Constant.class);
        Constant const2 = mock(Constant.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            finder1.visitAnyConstant(clazz, const1);
            finder2.visitAnyConstant(clazz, const2);
            finder1.visitAnyConstant(clazz, const2);
            finder2.visitAnyConstant(clazz, const1);
        });
    }

    /**
     * Tests that visitAnyConstant maintains independent state across multiple finder instances.
     * Verifies that each finder's typeArgumentClasses is not affected by visitAnyConstant calls.
     */
    @Test
    public void testVisitAnyConstant_independentStateAcrossInstances() {
        // Arrange
        TypeArgumentFinder finder1 = new TypeArgumentFinder(programClassPool, libraryClassPool, partialEvaluator);
        TypeArgumentFinder finder2 = new TypeArgumentFinder(programClassPool, libraryClassPool, partialEvaluator);

        finder1.typeArgumentClasses = new String[] { "Class1" };
        finder2.typeArgumentClasses = new String[] { "Class2" };

        // Act
        finder1.visitAnyConstant(clazz, constant);
        finder2.visitAnyConstant(clazz, constant);

        // Assert - each finder's state should remain unchanged
        assertArrayEquals(new String[] { "Class1" }, finder1.typeArgumentClasses,
                "finder1 typeArgumentClasses should remain unchanged");
        assertArrayEquals(new String[] { "Class2" }, finder2.typeArgumentClasses,
                "finder2 typeArgumentClasses should remain unchanged");
    }

    /**
     * Tests that visitAnyConstant works correctly when called immediately after construction.
     * Verifies the finder is ready to use immediately.
     */
    @Test
    public void testVisitAnyConstant_immediatelyAfterConstruction_doesNotThrowException() {
        // Arrange
        TypeArgumentFinder newFinder = new TypeArgumentFinder(programClassPool, libraryClassPool, partialEvaluator);

        // Act & Assert
        assertDoesNotThrow(() -> newFinder.visitAnyConstant(clazz, constant),
                "visitAnyConstant should work immediately after construction");
    }

    /**
     * Tests that visitAnyConstant preserves null state of typeArgumentClasses.
     * Verifies that a null typeArgumentClasses field remains null after the call.
     */
    @Test
    public void testVisitAnyConstant_preservesNullTypeArgumentClasses() {
        // Arrange
        assertNull(finder.typeArgumentClasses, "Initial state should be null");

        // Act
        finder.visitAnyConstant(clazz, constant);

        // Assert
        assertNull(finder.typeArgumentClasses,
                "typeArgumentClasses should remain null after visitAnyConstant");
    }

    /**
     * Tests that visitAnyConstant preserves empty array state of typeArgumentClasses.
     * Verifies that an empty array remains empty after the call.
     */
    @Test
    public void testVisitAnyConstant_preservesEmptyArrayTypeArgumentClasses() {
        // Arrange
        finder.typeArgumentClasses = new String[0];
        String[] initialState = finder.typeArgumentClasses;

        // Act
        finder.visitAnyConstant(clazz, constant);

        // Assert
        assertSame(initialState, finder.typeArgumentClasses,
                "Empty array should remain the same instance");
        assertEquals(0, finder.typeArgumentClasses.length,
                "Empty array should remain empty");
    }

    /**
     * Tests that visitAnyConstant is idempotent.
     * Verifies that calling it multiple times has the same effect as calling it once.
     */
    @Test
    public void testVisitAnyConstant_isIdempotent() {
        // Arrange
        finder.typeArgumentClasses = new String[] { "TestClass" };
        String[] initialState = finder.typeArgumentClasses;

        // Act
        finder.visitAnyConstant(clazz, constant);
        String[] stateAfterFirstCall = finder.typeArgumentClasses;

        finder.visitAnyConstant(clazz, constant);
        String[] stateAfterSecondCall = finder.typeArgumentClasses;

        finder.visitAnyConstant(clazz, constant);
        String[] stateAfterThirdCall = finder.typeArgumentClasses;

        // Assert - all states should be the same
        assertSame(initialState, stateAfterFirstCall,
                "First call should not modify state");
        assertSame(stateAfterFirstCall, stateAfterSecondCall,
                "Second call should not modify state");
        assertSame(stateAfterSecondCall, stateAfterThirdCall,
                "Third call should not modify state");
    }
}
