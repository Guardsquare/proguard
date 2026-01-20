package proguard.optimize.evaluation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.LibraryClass;
import proguard.classfile.constant.MethodTypeConstant;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link SimpleEnumUseChecker#visitMethodTypeConstant(Clazz, MethodTypeConstant)}.
 *
 * The visitMethodTypeConstant method is responsible for unmarking simple enum classes
 * that are referenced in a method type constant. This is done by calling
 * methodTypeConstant.referencedClassesAccept(referencedComplexEnumMarker), which visits
 * all referenced classes with the complexEnumMarker to mark them as complex enums
 * (not eligible for simple enum optimizations).
 */
public class SimpleEnumUseCheckerClaude_visitMethodTypeConstantTest {

    private SimpleEnumUseChecker checker;
    private Clazz clazz;
    private MethodTypeConstant methodTypeConstant;

    @BeforeEach
    public void setUp() {
        checker = new SimpleEnumUseChecker();
        clazz = mock(ProgramClass.class);
        methodTypeConstant = mock(MethodTypeConstant.class);
    }

    /**
     * Tests that visitMethodTypeConstant calls referencedClassesAccept on the MethodTypeConstant.
     * This is the core behavior - the method should delegate to the method type constant
     * to visit all referenced classes with the referencedComplexEnumMarker.
     */
    @Test
    public void testVisitMethodTypeConstant_callsReferencedClassesAccept() {
        // Act
        checker.visitMethodTypeConstant(clazz, methodTypeConstant);

        // Assert - verify that referencedClassesAccept was called with a ClassVisitor
        verify(methodTypeConstant, times(1)).referencedClassesAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitMethodTypeConstant works with valid mock objects without throwing exceptions.
     */
    @Test
    public void testVisitMethodTypeConstant_withValidMocks_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.visitMethodTypeConstant(clazz, methodTypeConstant));
    }

    /**
     * Tests that visitMethodTypeConstant can be called with null Clazz parameter.
     * The clazz parameter is not used in the method implementation, so null should be handled gracefully.
     */
    @Test
    public void testVisitMethodTypeConstant_withNullClazz_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.visitMethodTypeConstant(null, methodTypeConstant));

        // Verify the method still calls referencedClassesAccept
        verify(methodTypeConstant, times(1)).referencedClassesAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitMethodTypeConstant with null MethodTypeConstant throws NullPointerException.
     * This should result in a NullPointerException since the method calls a method on methodTypeConstant.
     */
    @Test
    public void testVisitMethodTypeConstant_withNullMethodTypeConstant_throwsNullPointerException() {
        // Act & Assert - should throw NullPointerException
        assertThrows(NullPointerException.class,
            () -> checker.visitMethodTypeConstant(clazz, null));
    }

    /**
     * Tests that visitMethodTypeConstant can be called multiple times in succession.
     * Each call should invoke referencedClassesAccept.
     */
    @Test
    public void testVisitMethodTypeConstant_calledMultipleTimes_invokesReferencedClassesAcceptEachTime() {
        // Act
        checker.visitMethodTypeConstant(clazz, methodTypeConstant);
        checker.visitMethodTypeConstant(clazz, methodTypeConstant);
        checker.visitMethodTypeConstant(clazz, methodTypeConstant);

        // Assert - verify referencedClassesAccept was called three times
        verify(methodTypeConstant, times(3)).referencedClassesAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitMethodTypeConstant doesn't directly interact with the Clazz parameter.
     * The clazz is passed as a context parameter but not used in this method.
     */
    @Test
    public void testVisitMethodTypeConstant_doesNotInteractWithClazz() {
        // Act
        checker.visitMethodTypeConstant(clazz, methodTypeConstant);

        // Assert - verify no interactions occurred with the clazz mock
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitMethodTypeConstant can be used as part of the ConstantVisitor interface.
     * Verifies integration with the visitor pattern.
     */
    @Test
    public void testVisitMethodTypeConstant_usedAsConstantVisitor_worksCorrectly() {
        // Arrange
        proguard.classfile.constant.visitor.ConstantVisitor visitor = checker;

        // Act & Assert
        assertDoesNotThrow(() -> visitor.visitMethodTypeConstant(clazz, methodTypeConstant));
        verify(methodTypeConstant, times(1)).referencedClassesAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitMethodTypeConstant can be called with real ProgramClass instance.
     * Verifies the method works with actual class instances, not just mocks.
     */
    @Test
    public void testVisitMethodTypeConstant_withRealProgramClass_doesNotThrowException() {
        // Arrange
        ProgramClass realClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitMethodTypeConstant(realClass, methodTypeConstant));
        verify(methodTypeConstant, times(1)).referencedClassesAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitMethodTypeConstant can be called with real LibraryClass instance.
     * Verifies the method works with library classes.
     */
    @Test
    public void testVisitMethodTypeConstant_withRealLibraryClass_doesNotThrowException() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitMethodTypeConstant(libraryClass, methodTypeConstant));
        verify(methodTypeConstant, times(1)).referencedClassesAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitMethodTypeConstant can be called rapidly in succession.
     * Verifies consistent behavior under repeated calls.
     */
    @Test
    public void testVisitMethodTypeConstant_rapidSequentialCalls_worksCorrectly() {
        // Act
        for (int i = 0; i < 100; i++) {
            assertDoesNotThrow(() -> checker.visitMethodTypeConstant(clazz, methodTypeConstant),
                    "Call " + i + " should not throw exception");
        }

        // Assert - verify referencedClassesAccept was called 100 times
        verify(methodTypeConstant, times(100)).referencedClassesAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that multiple SimpleEnumUseChecker instances can all call visitMethodTypeConstant
     * on the same MethodTypeConstant without interference.
     */
    @Test
    public void testVisitMethodTypeConstant_multipleCheckers_allWorkCorrectly() {
        // Arrange
        SimpleEnumUseChecker checker1 = new SimpleEnumUseChecker();
        SimpleEnumUseChecker checker2 = new SimpleEnumUseChecker();
        SimpleEnumUseChecker checker3 = new SimpleEnumUseChecker();

        // Act
        checker1.visitMethodTypeConstant(clazz, methodTypeConstant);
        checker2.visitMethodTypeConstant(clazz, methodTypeConstant);
        checker3.visitMethodTypeConstant(clazz, methodTypeConstant);

        // Assert - verify referencedClassesAccept was called three times
        verify(methodTypeConstant, times(3)).referencedClassesAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitMethodTypeConstant works with different MethodTypeConstant instances.
     * Verifies the method can handle multiple different method type constants.
     */
    @Test
    public void testVisitMethodTypeConstant_withDifferentMethodTypeConstants_worksCorrectly() {
        // Arrange
        MethodTypeConstant mtc1 = mock(MethodTypeConstant.class);
        MethodTypeConstant mtc2 = mock(MethodTypeConstant.class);
        MethodTypeConstant mtc3 = mock(MethodTypeConstant.class);

        // Act
        checker.visitMethodTypeConstant(clazz, mtc1);
        checker.visitMethodTypeConstant(clazz, mtc2);
        checker.visitMethodTypeConstant(clazz, mtc3);

        // Assert - verify each constant had referencedClassesAccept called
        verify(mtc1, times(1)).referencedClassesAccept(any(ClassVisitor.class));
        verify(mtc2, times(1)).referencedClassesAccept(any(ClassVisitor.class));
        verify(mtc3, times(1)).referencedClassesAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitMethodTypeConstant works with different Clazz instances.
     * Verifies the method can handle multiple different clazz contexts.
     */
    @Test
    public void testVisitMethodTypeConstant_withDifferentClazzes_worksCorrectly() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(LibraryClass.class);
        ProgramClass clazz3 = new ProgramClass();

        // Act
        checker.visitMethodTypeConstant(clazz1, methodTypeConstant);
        checker.visitMethodTypeConstant(clazz2, methodTypeConstant);
        checker.visitMethodTypeConstant(clazz3, methodTypeConstant);

        // Assert - verify referencedClassesAccept was called three times
        verify(methodTypeConstant, times(3)).referencedClassesAccept(any(ClassVisitor.class));

        // Verify no interactions with clazz parameters
        verifyNoInteractions(clazz1, clazz2);
    }

    /**
     * Tests that visitMethodTypeConstant maintains consistent behavior across multiple calls
     * with the same parameters.
     */
    @Test
    public void testVisitMethodTypeConstant_repeatedCallsSameParameters_consistentBehavior() {
        // Act & Assert - multiple calls should have identical behavior
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> checker.visitMethodTypeConstant(clazz, methodTypeConstant));
        }

        // Verify referencedClassesAccept was called 10 times
        verify(methodTypeConstant, times(10)).referencedClassesAccept(any(ClassVisitor.class));

        // Verify no interactions with clazz
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitMethodTypeConstant can be called through the ConstantVisitor interface
     * with various parameter combinations.
     */
    @Test
    public void testVisitMethodTypeConstant_throughInterface_worksWithVariousParameters() {
        // Arrange
        proguard.classfile.constant.visitor.ConstantVisitor visitor = checker;
        ProgramClass programClass = new ProgramClass();
        LibraryClass libraryClass = new LibraryClass();

        // Act & Assert
        assertDoesNotThrow(() -> {
            visitor.visitMethodTypeConstant(programClass, methodTypeConstant);
            visitor.visitMethodTypeConstant(libraryClass, methodTypeConstant);
            visitor.visitMethodTypeConstant(null, methodTypeConstant);
        });

        // Verify referencedClassesAccept was called three times
        verify(methodTypeConstant, times(3)).referencedClassesAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitMethodTypeConstant works correctly when using a custom PartialEvaluator.
     * Verifies that the method behavior is consistent across different checker configurations.
     */
    @Test
    public void testVisitMethodTypeConstant_withCustomPartialEvaluator_worksCorrectly() {
        // Arrange
        proguard.evaluation.PartialEvaluator evaluator =
            proguard.evaluation.PartialEvaluator.Builder.create().build();
        SimpleEnumUseChecker customChecker = new SimpleEnumUseChecker(evaluator);

        // Act
        customChecker.visitMethodTypeConstant(clazz, methodTypeConstant);

        // Assert
        verify(methodTypeConstant, times(1)).referencedClassesAccept(any(ClassVisitor.class));
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitMethodTypeConstant can handle interleaved calls with different parameters.
     * Verifies robustness with varied call patterns.
     */
    @Test
    public void testVisitMethodTypeConstant_interleavedCalls_worksCorrectly() {
        // Arrange
        MethodTypeConstant mtc1 = mock(MethodTypeConstant.class);
        MethodTypeConstant mtc2 = mock(MethodTypeConstant.class);
        Clazz clazz2 = mock(ProgramClass.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            checker.visitMethodTypeConstant(clazz, mtc1);
            checker.visitMethodTypeConstant(clazz2, mtc2);
            checker.visitMethodTypeConstant(clazz, mtc2);
            checker.visitMethodTypeConstant(clazz2, mtc1);
        });

        // Verify each method type constant was called twice
        verify(mtc1, times(2)).referencedClassesAccept(any(ClassVisitor.class));
        verify(mtc2, times(2)).referencedClassesAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitMethodTypeConstant passes a non-null ClassVisitor to referencedClassesAccept.
     * The visitor should be the referencedComplexEnumMarker.
     */
    @Test
    public void testVisitMethodTypeConstant_passesNonNullClassVisitor() {
        // Act
        checker.visitMethodTypeConstant(clazz, methodTypeConstant);

        // Assert - verify that referencedClassesAccept was called with a non-null ClassVisitor
        verify(methodTypeConstant).referencedClassesAccept(argThat(visitor -> visitor != null));
    }

    /**
     * Tests that visitMethodTypeConstant maintains correct behavior when called in a sequence
     * with other visitor methods.
     */
    @Test
    public void testVisitMethodTypeConstant_inVisitorSequence_worksCorrectly() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - should work in a sequence of visitor calls
        assertDoesNotThrow(() -> {
            checker.visitAnyClass(programClass);
            checker.visitMethodTypeConstant(programClass, methodTypeConstant);
            checker.visitAnyClass(programClass);
            checker.visitMethodTypeConstant(programClass, methodTypeConstant);
        });

        // Verify referencedClassesAccept was called twice
        verify(methodTypeConstant, times(2)).referencedClassesAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitMethodTypeConstant doesn't modify the state of the clazz parameter.
     */
    @Test
    public void testVisitMethodTypeConstant_doesNotModifyClazzState() {
        // Arrange
        ProgramClass realClass = new ProgramClass();
        Object initialProcessingInfo = new Object();
        realClass.setProcessingInfo(initialProcessingInfo);

        // Act
        checker.visitMethodTypeConstant(realClass, methodTypeConstant);

        // Assert - verify the class state wasn't modified
        assertSame(initialProcessingInfo, realClass.getProcessingInfo(),
                "Class processing info should not be modified");
    }

    /**
     * Tests that visitMethodTypeConstant can handle extreme numbers of calls without issues.
     * Verifies the implementation doesn't accumulate state or resources.
     */
    @Test
    public void testVisitMethodTypeConstant_extremeNumberOfCalls_worksCorrectly() {
        // Act
        for (int i = 0; i < 1000; i++) {
            assertDoesNotThrow(() -> checker.visitMethodTypeConstant(clazz, methodTypeConstant));
        }

        // Assert - verify referencedClassesAccept was called 1000 times
        verify(methodTypeConstant, times(1000)).referencedClassesAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitMethodTypeConstant works correctly with mixed real and mock objects.
     */
    @Test
    public void testVisitMethodTypeConstant_mixedRealAndMockObjects_worksCorrectly() {
        // Arrange
        ProgramClass realProgramClass = new ProgramClass();
        LibraryClass realLibraryClass = new LibraryClass();
        Clazz mockClazz = mock(Clazz.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            checker.visitMethodTypeConstant(realProgramClass, methodTypeConstant);
            checker.visitMethodTypeConstant(realLibraryClass, methodTypeConstant);
            checker.visitMethodTypeConstant(mockClazz, methodTypeConstant);
        });

        // Verify referencedClassesAccept was called three times
        verify(methodTypeConstant, times(3)).referencedClassesAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that multiple checker instances can call visitMethodTypeConstant independently
     * without interfering with each other.
     */
    @Test
    public void testVisitMethodTypeConstant_multipleInstancesIndependent_worksCorrectly() {
        // Arrange
        SimpleEnumUseChecker checker1 = new SimpleEnumUseChecker();
        SimpleEnumUseChecker checker2 = new SimpleEnumUseChecker();
        MethodTypeConstant mtc1 = mock(MethodTypeConstant.class);
        MethodTypeConstant mtc2 = mock(MethodTypeConstant.class);

        // Act
        checker1.visitMethodTypeConstant(clazz, mtc1);
        checker2.visitMethodTypeConstant(clazz, mtc2);

        // Assert - verify each checker called its respective method type constant
        verify(mtc1, times(1)).referencedClassesAccept(any(ClassVisitor.class));
        verify(mtc2, times(1)).referencedClassesAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitMethodTypeConstant maintains the visitor pattern contract.
     * The method should always call referencedClassesAccept regardless of the clazz parameter.
     */
    @Test
    public void testVisitMethodTypeConstant_visitorPatternContract_alwaysCallsReferencedClassesAccept() {
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
            assertDoesNotThrow(() -> checker.visitMethodTypeConstant(c, methodTypeConstant));
        }

        // Assert - verify referencedClassesAccept was called for each variation
        verify(methodTypeConstant, times(differentClazzes.length))
            .referencedClassesAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitMethodTypeConstant behavior remains consistent after being called
     * with null clazz parameters.
     */
    @Test
    public void testVisitMethodTypeConstant_afterNullClazz_behaviorRemainsConsistent() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            checker.visitMethodTypeConstant(null, methodTypeConstant);
            checker.visitMethodTypeConstant(clazz, methodTypeConstant);
            checker.visitMethodTypeConstant(null, methodTypeConstant);
        });

        // Verify referencedClassesAccept was called three times
        verify(methodTypeConstant, times(3)).referencedClassesAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitMethodTypeConstant can be called as part of a complex visitor workflow.
     */
    @Test
    public void testVisitMethodTypeConstant_inComplexWorkflow_worksCorrectly() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        MethodTypeConstant mtc1 = mock(MethodTypeConstant.class);
        MethodTypeConstant mtc2 = mock(MethodTypeConstant.class);

        // Act & Assert - simulate a complex visitor workflow
        assertDoesNotThrow(() -> {
            checker.visitAnyClass(programClass);
            checker.visitMethodTypeConstant(programClass, mtc1);
            checker.visitAnyConstant(programClass, mtc1);
            checker.visitMethodTypeConstant(programClass, mtc2);
            checker.visitAnyClass(programClass);
        });

        // Verify both method type constants had referencedClassesAccept called
        verify(mtc1, times(1)).referencedClassesAccept(any(ClassVisitor.class));
        verify(mtc2, times(1)).referencedClassesAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitMethodTypeConstant works correctly when alternating between
     * different checker instances and parameters.
     */
    @Test
    public void testVisitMethodTypeConstant_alternatingCheckersAndParameters_consistentBehavior() {
        // Arrange
        SimpleEnumUseChecker checker1 = new SimpleEnumUseChecker();
        SimpleEnumUseChecker checker2 = new SimpleEnumUseChecker();
        MethodTypeConstant mtc1 = mock(MethodTypeConstant.class);
        MethodTypeConstant mtc2 = mock(MethodTypeConstant.class);
        Clazz clazz2 = mock(ProgramClass.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            checker1.visitMethodTypeConstant(clazz, mtc1);
            checker2.visitMethodTypeConstant(clazz2, mtc2);
            checker1.visitMethodTypeConstant(clazz2, mtc2);
            checker2.visitMethodTypeConstant(clazz, mtc1);
        });

        // Verify each constant was called twice
        verify(mtc1, times(2)).referencedClassesAccept(any(ClassVisitor.class));
        verify(mtc2, times(2)).referencedClassesAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitMethodTypeConstant works correctly in combination with other constant visitor methods.
     */
    @Test
    public void testVisitMethodTypeConstant_withOtherConstantVisitorMethods_worksCorrectly() {
        // Arrange
        proguard.classfile.constant.StringConstant stringConstant =
            mock(proguard.classfile.constant.StringConstant.class);
        proguard.classfile.constant.MethodHandleConstant methodHandleConstant =
            mock(proguard.classfile.constant.MethodHandleConstant.class);

        // Act & Assert - should work with other constant visitor methods
        assertDoesNotThrow(() -> {
            checker.visitMethodTypeConstant(clazz, methodTypeConstant);
            checker.visitStringConstant(clazz, stringConstant);
            checker.visitMethodHandleConstant(clazz, methodHandleConstant);
            checker.visitMethodTypeConstant(clazz, methodTypeConstant);
        });

        // Verify all constants were visited
        verify(methodTypeConstant, times(2)).referencedClassesAccept(any(ClassVisitor.class));
        verify(stringConstant, times(1)).referencedClassAccept(any());
        verify(methodHandleConstant, times(1)).referenceAccept(any(), any());
    }

    /**
     * Tests that visitMethodTypeConstant passes a ClassVisitor instance to referencedClassesAccept.
     */
    @Test
    public void testVisitMethodTypeConstant_passesClassVisitorToReferencedClassesAccept() {
        // Act
        checker.visitMethodTypeConstant(clazz, methodTypeConstant);

        // Assert - verify a ClassVisitor was passed (the referencedComplexEnumMarker)
        verify(methodTypeConstant).referencedClassesAccept(
            argThat(visitor -> visitor instanceof ClassVisitor)
        );
    }

    /**
     * Tests that visitMethodTypeConstant can be called with both parameters as various types
     * without side effects on the clazz parameter.
     */
    @Test
    public void testVisitMethodTypeConstant_variousParameterTypes_noSideEffectsOnClazz() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        LibraryClass libraryClass = new LibraryClass();
        Clazz mockClazz = mock(Clazz.class);

        // Act
        checker.visitMethodTypeConstant(programClass, methodTypeConstant);
        checker.visitMethodTypeConstant(libraryClass, methodTypeConstant);
        checker.visitMethodTypeConstant(mockClazz, methodTypeConstant);
        checker.visitMethodTypeConstant(null, methodTypeConstant);

        // Assert - verify no interactions with any clazz parameter
        verifyNoInteractions(mockClazz);
        // programClass and libraryClass state shouldn't change
        assertNull(programClass.getProcessingInfo());
        assertNull(libraryClass.getProcessingInfo());
    }

    /**
     * Tests that visitMethodTypeConstant handles the case where referencedClassesAccept
     * is called but there are no referenced classes (does nothing special).
     */
    @Test
    public void testVisitMethodTypeConstant_withNoReferencedClasses_worksCorrectly() {
        // Arrange - doNothing is the default behavior for mocks, but we make it explicit
        doNothing().when(methodTypeConstant).referencedClassesAccept(any(ClassVisitor.class));

        // Act & Assert - should complete without issues
        assertDoesNotThrow(() -> checker.visitMethodTypeConstant(clazz, methodTypeConstant));

        // Verify the call was made
        verify(methodTypeConstant, times(1)).referencedClassesAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitMethodTypeConstant works correctly when used in a chain of method calls.
     */
    @Test
    public void testVisitMethodTypeConstant_inMethodChain_worksCorrectly() {
        // Arrange
        MethodTypeConstant mtc1 = mock(MethodTypeConstant.class);
        MethodTypeConstant mtc2 = mock(MethodTypeConstant.class);
        MethodTypeConstant mtc3 = mock(MethodTypeConstant.class);

        // Act & Assert - chain multiple calls
        assertDoesNotThrow(() -> {
            checker.visitMethodTypeConstant(clazz, mtc1);
            checker.visitMethodTypeConstant(clazz, mtc2);
            checker.visitMethodTypeConstant(clazz, mtc3);
        });

        // Verify all were called
        verify(mtc1, times(1)).referencedClassesAccept(any(ClassVisitor.class));
        verify(mtc2, times(1)).referencedClassesAccept(any(ClassVisitor.class));
        verify(mtc3, times(1)).referencedClassesAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitMethodTypeConstant doesn't retain references to parameters.
     * Important for memory management.
     */
    @Test
    public void testVisitMethodTypeConstant_doesNotRetainReferences() {
        // Arrange
        Clazz tempClazz = mock(ProgramClass.class);
        MethodTypeConstant tempMethodTypeConstant = mock(MethodTypeConstant.class);

        // Act
        checker.visitMethodTypeConstant(tempClazz, tempMethodTypeConstant);

        // Assert - the method should have called referencedClassesAccept and not retained refs
        verify(tempMethodTypeConstant, times(1)).referencedClassesAccept(any(ClassVisitor.class));
        verifyNoInteractions(tempClazz);
    }
}
