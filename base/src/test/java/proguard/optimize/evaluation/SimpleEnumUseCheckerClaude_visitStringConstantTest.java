package proguard.optimize.evaluation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.LibraryClass;
import proguard.classfile.constant.StringConstant;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link SimpleEnumUseChecker#visitStringConstant(Clazz, StringConstant)}.
 *
 * The visitStringConstant method is responsible for unmarking any simple enum class
 * that is referenced in a string constant. This is done by calling
 * stringConstant.referencedClassAccept(complexEnumMarker), which marks the referenced
 * class as a complex enum (not eligible for simple enum optimizations).
 */
public class SimpleEnumUseCheckerClaude_visitStringConstantTest {

    private SimpleEnumUseChecker checker;
    private Clazz clazz;
    private StringConstant stringConstant;

    @BeforeEach
    public void setUp() {
        checker = new SimpleEnumUseChecker();
        clazz = mock(ProgramClass.class);
        stringConstant = mock(StringConstant.class);
    }

    /**
     * Tests that visitStringConstant calls referencedClassAccept on the StringConstant.
     * This is the core behavior - the method should delegate to the string constant
     * to visit any referenced class with the complexEnumMarker.
     */
    @Test
    public void testVisitStringConstant_callsReferencedClassAccept() {
        // Act
        checker.visitStringConstant(clazz, stringConstant);

        // Assert - verify that referencedClassAccept was called with a ClassVisitor
        verify(stringConstant, times(1)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitStringConstant works with valid mock objects without throwing exceptions.
     */
    @Test
    public void testVisitStringConstant_withValidMocks_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.visitStringConstant(clazz, stringConstant));
    }

    /**
     * Tests that visitStringConstant can be called with null Clazz parameter.
     * The method should handle null clazz gracefully since it doesn't directly use it.
     */
    @Test
    public void testVisitStringConstant_withNullClazz_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.visitStringConstant(null, stringConstant));

        // Verify the method still calls referencedClassAccept
        verify(stringConstant, times(1)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitStringConstant can be called with null StringConstant parameter.
     * This should result in a NullPointerException since the method calls a method on stringConstant.
     */
    @Test
    public void testVisitStringConstant_withNullStringConstant_throwsNullPointerException() {
        // Act & Assert - should throw NullPointerException
        assertThrows(NullPointerException.class,
            () -> checker.visitStringConstant(clazz, null));
    }

    /**
     * Tests that visitStringConstant can be called multiple times in succession.
     * Each call should invoke referencedClassAccept.
     */
    @Test
    public void testVisitStringConstant_calledMultipleTimes_invokesReferencedClassAcceptEachTime() {
        // Act
        checker.visitStringConstant(clazz, stringConstant);
        checker.visitStringConstant(clazz, stringConstant);
        checker.visitStringConstant(clazz, stringConstant);

        // Assert - verify referencedClassAccept was called three times
        verify(stringConstant, times(3)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitStringConstant doesn't directly interact with the Clazz parameter.
     * The clazz is passed as a context parameter but not used in this method.
     */
    @Test
    public void testVisitStringConstant_doesNotInteractWithClazz() {
        // Act
        checker.visitStringConstant(clazz, stringConstant);

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
        proguard.classfile.constant.visitor.ConstantVisitor visitor = checker;

        // Act & Assert
        assertDoesNotThrow(() -> visitor.visitStringConstant(clazz, stringConstant));
        verify(stringConstant, times(1)).referencedClassAccept(any(ClassVisitor.class));
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
        assertDoesNotThrow(() -> checker.visitStringConstant(realClass, stringConstant));
        verify(stringConstant, times(1)).referencedClassAccept(any(ClassVisitor.class));
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
        assertDoesNotThrow(() -> checker.visitStringConstant(libraryClass, stringConstant));
        verify(stringConstant, times(1)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitStringConstant can be called rapidly in succession.
     * Verifies consistent behavior under repeated calls.
     */
    @Test
    public void testVisitStringConstant_rapidSequentialCalls_worksCorrectly() {
        // Act
        for (int i = 0; i < 100; i++) {
            assertDoesNotThrow(() -> checker.visitStringConstant(clazz, stringConstant),
                    "Call " + i + " should not throw exception");
        }

        // Assert - verify referencedClassAccept was called 100 times
        verify(stringConstant, times(100)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that multiple SimpleEnumUseChecker instances can all call visitStringConstant
     * on the same StringConstant without interference.
     */
    @Test
    public void testVisitStringConstant_multipleCheckers_allWorkCorrectly() {
        // Arrange
        SimpleEnumUseChecker checker1 = new SimpleEnumUseChecker();
        SimpleEnumUseChecker checker2 = new SimpleEnumUseChecker();
        SimpleEnumUseChecker checker3 = new SimpleEnumUseChecker();

        // Act
        checker1.visitStringConstant(clazz, stringConstant);
        checker2.visitStringConstant(clazz, stringConstant);
        checker3.visitStringConstant(clazz, stringConstant);

        // Assert - verify referencedClassAccept was called three times
        verify(stringConstant, times(3)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitStringConstant works with different StringConstant instances.
     * Verifies the method can handle multiple different string constants.
     */
    @Test
    public void testVisitStringConstant_withDifferentStringConstants_worksCorrectly() {
        // Arrange
        StringConstant stringConstant1 = mock(StringConstant.class);
        StringConstant stringConstant2 = mock(StringConstant.class);
        StringConstant stringConstant3 = mock(StringConstant.class);

        // Act
        checker.visitStringConstant(clazz, stringConstant1);
        checker.visitStringConstant(clazz, stringConstant2);
        checker.visitStringConstant(clazz, stringConstant3);

        // Assert - verify each constant had referencedClassAccept called
        verify(stringConstant1, times(1)).referencedClassAccept(any(ClassVisitor.class));
        verify(stringConstant2, times(1)).referencedClassAccept(any(ClassVisitor.class));
        verify(stringConstant3, times(1)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitStringConstant works with different Clazz instances.
     * Verifies the method can handle multiple different clazz contexts.
     */
    @Test
    public void testVisitStringConstant_withDifferentClazzes_worksCorrectly() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(LibraryClass.class);
        ProgramClass clazz3 = new ProgramClass();

        // Act
        checker.visitStringConstant(clazz1, stringConstant);
        checker.visitStringConstant(clazz2, stringConstant);
        checker.visitStringConstant(clazz3, stringConstant);

        // Assert - verify referencedClassAccept was called three times
        verify(stringConstant, times(3)).referencedClassAccept(any(ClassVisitor.class));

        // Verify no interactions with clazz parameters
        verifyNoInteractions(clazz1, clazz2);
    }

    /**
     * Tests that visitStringConstant maintains consistent behavior across multiple calls
     * with the same parameters.
     */
    @Test
    public void testVisitStringConstant_repeatedCallsSameParameters_consistentBehavior() {
        // Act & Assert - multiple calls should have identical behavior
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> checker.visitStringConstant(clazz, stringConstant));
        }

        // Verify referencedClassAccept was called 10 times
        verify(stringConstant, times(10)).referencedClassAccept(any(ClassVisitor.class));

        // Verify no interactions with clazz
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitStringConstant can be called through the ConstantVisitor interface
     * with various parameter combinations.
     */
    @Test
    public void testVisitStringConstant_throughInterface_worksWithVariousParameters() {
        // Arrange
        proguard.classfile.constant.visitor.ConstantVisitor visitor = checker;
        ProgramClass programClass = new ProgramClass();
        LibraryClass libraryClass = new LibraryClass();

        // Act & Assert
        assertDoesNotThrow(() -> {
            visitor.visitStringConstant(programClass, stringConstant);
            visitor.visitStringConstant(libraryClass, stringConstant);
            visitor.visitStringConstant(null, stringConstant);
        });

        // Verify referencedClassAccept was called three times
        verify(stringConstant, times(3)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitStringConstant works correctly when using a custom PartialEvaluator.
     * Verifies that the method behavior is consistent across different checker configurations.
     */
    @Test
    public void testVisitStringConstant_withCustomPartialEvaluator_worksCorrectly() {
        // Arrange
        proguard.evaluation.PartialEvaluator evaluator =
            proguard.evaluation.PartialEvaluator.Builder.create().build();
        SimpleEnumUseChecker customChecker = new SimpleEnumUseChecker(evaluator);

        // Act
        customChecker.visitStringConstant(clazz, stringConstant);

        // Assert
        verify(stringConstant, times(1)).referencedClassAccept(any(ClassVisitor.class));
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitStringConstant can handle interleaved calls with different parameters.
     * Verifies robustness with varied call patterns.
     */
    @Test
    public void testVisitStringConstant_interleavedCalls_worksCorrectly() {
        // Arrange
        StringConstant sc1 = mock(StringConstant.class);
        StringConstant sc2 = mock(StringConstant.class);
        Clazz clazz2 = mock(ProgramClass.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            checker.visitStringConstant(clazz, sc1);
            checker.visitStringConstant(clazz2, sc2);
            checker.visitStringConstant(clazz, sc2);
            checker.visitStringConstant(clazz2, sc1);
        });

        // Verify each string constant was called twice
        verify(sc1, times(2)).referencedClassAccept(any(ClassVisitor.class));
        verify(sc2, times(2)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitStringConstant passes the correct ClassVisitor to referencedClassAccept.
     * The visitor should be the complexEnumMarker which marks classes as complex enums.
     */
    @Test
    public void testVisitStringConstant_passesClassVisitorToReferencedClassAccept() {
        // Act
        checker.visitStringConstant(clazz, stringConstant);

        // Assert - verify that referencedClassAccept was called with a non-null ClassVisitor
        verify(stringConstant).referencedClassAccept(argThat(visitor -> visitor != null));
    }

    /**
     * Tests that visitStringConstant maintains correct behavior when called in a sequence
     * with other visitor methods.
     */
    @Test
    public void testVisitStringConstant_inVisitorSequence_worksCorrectly() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - should work in a sequence of visitor calls
        assertDoesNotThrow(() -> {
            checker.visitAnyClass(programClass);
            checker.visitStringConstant(programClass, stringConstant);
            checker.visitAnyClass(programClass);
            checker.visitStringConstant(programClass, stringConstant);
        });

        // Verify referencedClassAccept was called twice
        verify(stringConstant, times(2)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitStringConstant doesn't modify the state of the clazz parameter.
     */
    @Test
    public void testVisitStringConstant_doesNotModifyClazzState() {
        // Arrange
        ProgramClass realClass = new ProgramClass();
        Object initialProcessingInfo = new Object();
        realClass.setProcessingInfo(initialProcessingInfo);

        // Act
        checker.visitStringConstant(realClass, stringConstant);

        // Assert - verify the class state wasn't modified
        assertSame(initialProcessingInfo, realClass.getProcessingInfo(),
                "Class processing info should not be modified");
    }

    /**
     * Tests that visitStringConstant can handle extreme numbers of calls without issues.
     * Verifies the implementation doesn't accumulate state or resources.
     */
    @Test
    public void testVisitStringConstant_extremeNumberOfCalls_worksCorrectly() {
        // Act
        for (int i = 0; i < 1000; i++) {
            assertDoesNotThrow(() -> checker.visitStringConstant(clazz, stringConstant));
        }

        // Assert - verify referencedClassAccept was called 1000 times
        verify(stringConstant, times(1000)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitStringConstant works correctly with mixed real and mock objects.
     */
    @Test
    public void testVisitStringConstant_mixedRealAndMockObjects_worksCorrectly() {
        // Arrange
        ProgramClass realProgramClass = new ProgramClass();
        LibraryClass realLibraryClass = new LibraryClass();
        Clazz mockClazz = mock(Clazz.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            checker.visitStringConstant(realProgramClass, stringConstant);
            checker.visitStringConstant(realLibraryClass, stringConstant);
            checker.visitStringConstant(mockClazz, stringConstant);
        });

        // Verify referencedClassAccept was called three times
        verify(stringConstant, times(3)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that multiple checker instances can call visitStringConstant independently
     * without interfering with each other.
     */
    @Test
    public void testVisitStringConstant_multipleInstancesIndependent_worksCorrectly() {
        // Arrange
        SimpleEnumUseChecker checker1 = new SimpleEnumUseChecker();
        SimpleEnumUseChecker checker2 = new SimpleEnumUseChecker();
        StringConstant sc1 = mock(StringConstant.class);
        StringConstant sc2 = mock(StringConstant.class);

        // Act
        checker1.visitStringConstant(clazz, sc1);
        checker2.visitStringConstant(clazz, sc2);

        // Assert - verify each checker called its respective string constant
        verify(sc1, times(1)).referencedClassAccept(any(ClassVisitor.class));
        verify(sc2, times(1)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitStringConstant maintains the visitor pattern contract.
     * The method should always call referencedClassAccept regardless of the clazz parameter.
     */
    @Test
    public void testVisitStringConstant_visitorPatternContract_alwaysCallsReferencedClassAccept() {
        // Arrange
        Clazz[] differentClazzes = {
            mock(ProgramClass.class),
            mock(LibraryClass.class),
            new ProgramClass(),
            new LibraryClass(),
            null
        };

        // Act - call with different clazz types
        for (Clazz c : differentClazzes) {
            assertDoesNotThrow(() -> checker.visitStringConstant(c, stringConstant));
        }

        // Assert - verify referencedClassAccept was called for each variation
        verify(stringConstant, times(differentClazzes.length))
            .referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitStringConstant behavior remains consistent after being called
     * with null clazz parameters.
     */
    @Test
    public void testVisitStringConstant_afterNullClazz_behaviorRemainsConsistent() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            checker.visitStringConstant(null, stringConstant);
            checker.visitStringConstant(clazz, stringConstant);
            checker.visitStringConstant(null, stringConstant);
        });

        // Verify referencedClassAccept was called three times
        verify(stringConstant, times(3)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitStringConstant can be called as part of a complex visitor workflow.
     */
    @Test
    public void testVisitStringConstant_inComplexWorkflow_worksCorrectly() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        StringConstant sc1 = mock(StringConstant.class);
        StringConstant sc2 = mock(StringConstant.class);

        // Act & Assert - simulate a complex visitor workflow
        assertDoesNotThrow(() -> {
            checker.visitAnyClass(programClass);
            checker.visitStringConstant(programClass, sc1);
            checker.visitAnyConstant(programClass, sc1);
            checker.visitStringConstant(programClass, sc2);
            checker.visitAnyClass(programClass);
        });

        // Verify both string constants had referencedClassAccept called
        verify(sc1, times(1)).referencedClassAccept(any(ClassVisitor.class));
        verify(sc2, times(1)).referencedClassAccept(any(ClassVisitor.class));
    }
}
