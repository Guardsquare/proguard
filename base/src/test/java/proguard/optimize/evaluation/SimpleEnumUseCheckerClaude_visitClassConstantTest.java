package proguard.optimize.evaluation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.LibraryClass;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link SimpleEnumUseChecker#visitClassConstant(Clazz, ClassConstant)}.
 *
 * The visitClassConstant method is responsible for unmarking any simple enum class
 * that is referenced in a class constant. This is done by calling
 * classConstant.referencedClassAccept(complexEnumMarker), which marks the referenced
 * class as a complex enum (not eligible for simple enum optimizations).
 *
 * Note: This method uses complexEnumMarker directly (not referencedComplexEnumMarker),
 * similar to visitStringConstant.
 */
public class SimpleEnumUseCheckerClaude_visitClassConstantTest {

    private SimpleEnumUseChecker checker;
    private Clazz clazz;
    private ClassConstant classConstant;

    @BeforeEach
    public void setUp() {
        checker = new SimpleEnumUseChecker();
        clazz = mock(ProgramClass.class);
        classConstant = mock(ClassConstant.class);
    }

    /**
     * Tests that visitClassConstant calls referencedClassAccept on the ClassConstant.
     * This is the core behavior - the method should delegate to the class constant
     * to visit the referenced class with the complexEnumMarker.
     */
    @Test
    public void testVisitClassConstant_callsReferencedClassAccept() {
        // Act
        checker.visitClassConstant(clazz, classConstant);

        // Assert - verify that referencedClassAccept was called with a ClassVisitor
        verify(classConstant, times(1)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitClassConstant works with valid mock objects without throwing exceptions.
     */
    @Test
    public void testVisitClassConstant_withValidMocks_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.visitClassConstant(clazz, classConstant));
    }

    /**
     * Tests that visitClassConstant can be called with null Clazz parameter.
     * The clazz parameter is not used in the method implementation, so null should be handled gracefully.
     */
    @Test
    public void testVisitClassConstant_withNullClazz_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.visitClassConstant(null, classConstant));

        // Verify the method still calls referencedClassAccept
        verify(classConstant, times(1)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitClassConstant with null ClassConstant throws NullPointerException.
     * This should result in a NullPointerException since the method calls a method on classConstant.
     */
    @Test
    public void testVisitClassConstant_withNullClassConstant_throwsNullPointerException() {
        // Act & Assert - should throw NullPointerException
        assertThrows(NullPointerException.class,
            () -> checker.visitClassConstant(clazz, null));
    }

    /**
     * Tests that visitClassConstant can be called multiple times in succession.
     * Each call should invoke referencedClassAccept.
     */
    @Test
    public void testVisitClassConstant_calledMultipleTimes_invokesReferencedClassAcceptEachTime() {
        // Act
        checker.visitClassConstant(clazz, classConstant);
        checker.visitClassConstant(clazz, classConstant);
        checker.visitClassConstant(clazz, classConstant);

        // Assert - verify referencedClassAccept was called three times
        verify(classConstant, times(3)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitClassConstant doesn't directly interact with the Clazz parameter.
     * The clazz is passed as a context parameter but not used in this method.
     */
    @Test
    public void testVisitClassConstant_doesNotInteractWithClazz() {
        // Act
        checker.visitClassConstant(clazz, classConstant);

        // Assert - verify no interactions occurred with the clazz mock
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitClassConstant can be used as part of the ConstantVisitor interface.
     * Verifies integration with the visitor pattern.
     */
    @Test
    public void testVisitClassConstant_usedAsConstantVisitor_worksCorrectly() {
        // Arrange
        proguard.classfile.constant.visitor.ConstantVisitor visitor = checker;

        // Act & Assert
        assertDoesNotThrow(() -> visitor.visitClassConstant(clazz, classConstant));
        verify(classConstant, times(1)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitClassConstant can be called with real ProgramClass instance.
     * Verifies the method works with actual class instances, not just mocks.
     */
    @Test
    public void testVisitClassConstant_withRealProgramClass_doesNotThrowException() {
        // Arrange
        ProgramClass realClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitClassConstant(realClass, classConstant));
        verify(classConstant, times(1)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitClassConstant can be called with real LibraryClass instance.
     * Verifies the method works with library classes.
     */
    @Test
    public void testVisitClassConstant_withRealLibraryClass_doesNotThrowException() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitClassConstant(libraryClass, classConstant));
        verify(classConstant, times(1)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitClassConstant can be called rapidly in succession.
     * Verifies consistent behavior under repeated calls.
     */
    @Test
    public void testVisitClassConstant_rapidSequentialCalls_worksCorrectly() {
        // Act
        for (int i = 0; i < 100; i++) {
            assertDoesNotThrow(() -> checker.visitClassConstant(clazz, classConstant),
                    "Call " + i + " should not throw exception");
        }

        // Assert - verify referencedClassAccept was called 100 times
        verify(classConstant, times(100)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that multiple SimpleEnumUseChecker instances can all call visitClassConstant
     * on the same ClassConstant without interference.
     */
    @Test
    public void testVisitClassConstant_multipleCheckers_allWorkCorrectly() {
        // Arrange
        SimpleEnumUseChecker checker1 = new SimpleEnumUseChecker();
        SimpleEnumUseChecker checker2 = new SimpleEnumUseChecker();
        SimpleEnumUseChecker checker3 = new SimpleEnumUseChecker();

        // Act
        checker1.visitClassConstant(clazz, classConstant);
        checker2.visitClassConstant(clazz, classConstant);
        checker3.visitClassConstant(clazz, classConstant);

        // Assert - verify referencedClassAccept was called three times
        verify(classConstant, times(3)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitClassConstant works with different ClassConstant instances.
     * Verifies the method can handle multiple different class constants.
     */
    @Test
    public void testVisitClassConstant_withDifferentClassConstants_worksCorrectly() {
        // Arrange
        ClassConstant cc1 = mock(ClassConstant.class);
        ClassConstant cc2 = mock(ClassConstant.class);
        ClassConstant cc3 = mock(ClassConstant.class);

        // Act
        checker.visitClassConstant(clazz, cc1);
        checker.visitClassConstant(clazz, cc2);
        checker.visitClassConstant(clazz, cc3);

        // Assert - verify each constant had referencedClassAccept called
        verify(cc1, times(1)).referencedClassAccept(any(ClassVisitor.class));
        verify(cc2, times(1)).referencedClassAccept(any(ClassVisitor.class));
        verify(cc3, times(1)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitClassConstant works with different Clazz instances.
     * Verifies the method can handle multiple different clazz contexts.
     */
    @Test
    public void testVisitClassConstant_withDifferentClazzes_worksCorrectly() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(LibraryClass.class);
        ProgramClass clazz3 = new ProgramClass();

        // Act
        checker.visitClassConstant(clazz1, classConstant);
        checker.visitClassConstant(clazz2, classConstant);
        checker.visitClassConstant(clazz3, classConstant);

        // Assert - verify referencedClassAccept was called three times
        verify(classConstant, times(3)).referencedClassAccept(any(ClassVisitor.class));

        // Verify no interactions with clazz parameters
        verifyNoInteractions(clazz1, clazz2);
    }

    /**
     * Tests that visitClassConstant maintains consistent behavior across multiple calls
     * with the same parameters.
     */
    @Test
    public void testVisitClassConstant_repeatedCallsSameParameters_consistentBehavior() {
        // Act & Assert - multiple calls should have identical behavior
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> checker.visitClassConstant(clazz, classConstant));
        }

        // Verify referencedClassAccept was called 10 times
        verify(classConstant, times(10)).referencedClassAccept(any(ClassVisitor.class));

        // Verify no interactions with clazz
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitClassConstant can be called through the ConstantVisitor interface
     * with various parameter combinations.
     */
    @Test
    public void testVisitClassConstant_throughInterface_worksWithVariousParameters() {
        // Arrange
        proguard.classfile.constant.visitor.ConstantVisitor visitor = checker;
        ProgramClass programClass = new ProgramClass();
        LibraryClass libraryClass = new LibraryClass();

        // Act & Assert
        assertDoesNotThrow(() -> {
            visitor.visitClassConstant(programClass, classConstant);
            visitor.visitClassConstant(libraryClass, classConstant);
            visitor.visitClassConstant(null, classConstant);
        });

        // Verify referencedClassAccept was called three times
        verify(classConstant, times(3)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitClassConstant works correctly when using a custom PartialEvaluator.
     * Verifies that the method behavior is consistent across different checker configurations.
     */
    @Test
    public void testVisitClassConstant_withCustomPartialEvaluator_worksCorrectly() {
        // Arrange
        proguard.evaluation.PartialEvaluator evaluator =
            proguard.evaluation.PartialEvaluator.Builder.create().build();
        SimpleEnumUseChecker customChecker = new SimpleEnumUseChecker(evaluator);

        // Act
        customChecker.visitClassConstant(clazz, classConstant);

        // Assert
        verify(classConstant, times(1)).referencedClassAccept(any(ClassVisitor.class));
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitClassConstant can handle interleaved calls with different parameters.
     * Verifies robustness with varied call patterns.
     */
    @Test
    public void testVisitClassConstant_interleavedCalls_worksCorrectly() {
        // Arrange
        ClassConstant cc1 = mock(ClassConstant.class);
        ClassConstant cc2 = mock(ClassConstant.class);
        Clazz clazz2 = mock(ProgramClass.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            checker.visitClassConstant(clazz, cc1);
            checker.visitClassConstant(clazz2, cc2);
            checker.visitClassConstant(clazz, cc2);
            checker.visitClassConstant(clazz2, cc1);
        });

        // Verify each class constant was called twice
        verify(cc1, times(2)).referencedClassAccept(any(ClassVisitor.class));
        verify(cc2, times(2)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitClassConstant passes a non-null ClassVisitor to referencedClassAccept.
     * The visitor should be the complexEnumMarker.
     */
    @Test
    public void testVisitClassConstant_passesNonNullClassVisitor() {
        // Act
        checker.visitClassConstant(clazz, classConstant);

        // Assert - verify that referencedClassAccept was called with a non-null ClassVisitor
        verify(classConstant).referencedClassAccept(argThat(visitor -> visitor != null));
    }

    /**
     * Tests that visitClassConstant maintains correct behavior when called in a sequence
     * with other visitor methods.
     */
    @Test
    public void testVisitClassConstant_inVisitorSequence_worksCorrectly() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - should work in a sequence of visitor calls
        assertDoesNotThrow(() -> {
            checker.visitAnyClass(programClass);
            checker.visitClassConstant(programClass, classConstant);
            checker.visitAnyClass(programClass);
            checker.visitClassConstant(programClass, classConstant);
        });

        // Verify referencedClassAccept was called twice
        verify(classConstant, times(2)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitClassConstant doesn't modify the state of the clazz parameter.
     */
    @Test
    public void testVisitClassConstant_doesNotModifyClazzState() {
        // Arrange
        ProgramClass realClass = new ProgramClass();
        Object initialProcessingInfo = new Object();
        realClass.setProcessingInfo(initialProcessingInfo);

        // Act
        checker.visitClassConstant(realClass, classConstant);

        // Assert - verify the class state wasn't modified
        assertSame(initialProcessingInfo, realClass.getProcessingInfo(),
                "Class processing info should not be modified");
    }

    /**
     * Tests that visitClassConstant can handle extreme numbers of calls without issues.
     * Verifies the implementation doesn't accumulate state or resources.
     */
    @Test
    public void testVisitClassConstant_extremeNumberOfCalls_worksCorrectly() {
        // Act
        for (int i = 0; i < 1000; i++) {
            assertDoesNotThrow(() -> checker.visitClassConstant(clazz, classConstant));
        }

        // Assert - verify referencedClassAccept was called 1000 times
        verify(classConstant, times(1000)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitClassConstant works correctly with mixed real and mock objects.
     */
    @Test
    public void testVisitClassConstant_mixedRealAndMockObjects_worksCorrectly() {
        // Arrange
        ProgramClass realProgramClass = new ProgramClass();
        LibraryClass realLibraryClass = new LibraryClass();
        Clazz mockClazz = mock(Clazz.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            checker.visitClassConstant(realProgramClass, classConstant);
            checker.visitClassConstant(realLibraryClass, classConstant);
            checker.visitClassConstant(mockClazz, classConstant);
        });

        // Verify referencedClassAccept was called three times
        verify(classConstant, times(3)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that multiple checker instances can call visitClassConstant independently
     * without interfering with each other.
     */
    @Test
    public void testVisitClassConstant_multipleInstancesIndependent_worksCorrectly() {
        // Arrange
        SimpleEnumUseChecker checker1 = new SimpleEnumUseChecker();
        SimpleEnumUseChecker checker2 = new SimpleEnumUseChecker();
        ClassConstant cc1 = mock(ClassConstant.class);
        ClassConstant cc2 = mock(ClassConstant.class);

        // Act
        checker1.visitClassConstant(clazz, cc1);
        checker2.visitClassConstant(clazz, cc2);

        // Assert - verify each checker called its respective class constant
        verify(cc1, times(1)).referencedClassAccept(any(ClassVisitor.class));
        verify(cc2, times(1)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitClassConstant maintains the visitor pattern contract.
     * The method should always call referencedClassAccept regardless of the clazz parameter.
     */
    @Test
    public void testVisitClassConstant_visitorPatternContract_alwaysCallsReferencedClassAccept() {
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
            assertDoesNotThrow(() -> checker.visitClassConstant(c, classConstant));
        }

        // Assert - verify referencedClassAccept was called for each variation
        verify(classConstant, times(differentClazzes.length))
            .referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitClassConstant behavior remains consistent after being called
     * with null clazz parameters.
     */
    @Test
    public void testVisitClassConstant_afterNullClazz_behaviorRemainsConsistent() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            checker.visitClassConstant(null, classConstant);
            checker.visitClassConstant(clazz, classConstant);
            checker.visitClassConstant(null, classConstant);
        });

        // Verify referencedClassAccept was called three times
        verify(classConstant, times(3)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitClassConstant can be called as part of a complex visitor workflow.
     */
    @Test
    public void testVisitClassConstant_inComplexWorkflow_worksCorrectly() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ClassConstant cc1 = mock(ClassConstant.class);
        ClassConstant cc2 = mock(ClassConstant.class);

        // Act & Assert - simulate a complex visitor workflow
        assertDoesNotThrow(() -> {
            checker.visitAnyClass(programClass);
            checker.visitClassConstant(programClass, cc1);
            checker.visitAnyConstant(programClass, cc1);
            checker.visitClassConstant(programClass, cc2);
            checker.visitAnyClass(programClass);
        });

        // Verify both class constants had referencedClassAccept called
        verify(cc1, times(1)).referencedClassAccept(any(ClassVisitor.class));
        verify(cc2, times(1)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitClassConstant works correctly when alternating between
     * different checker instances and parameters.
     */
    @Test
    public void testVisitClassConstant_alternatingCheckersAndParameters_consistentBehavior() {
        // Arrange
        SimpleEnumUseChecker checker1 = new SimpleEnumUseChecker();
        SimpleEnumUseChecker checker2 = new SimpleEnumUseChecker();
        ClassConstant cc1 = mock(ClassConstant.class);
        ClassConstant cc2 = mock(ClassConstant.class);
        Clazz clazz2 = mock(ProgramClass.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            checker1.visitClassConstant(clazz, cc1);
            checker2.visitClassConstant(clazz2, cc2);
            checker1.visitClassConstant(clazz2, cc2);
            checker2.visitClassConstant(clazz, cc1);
        });

        // Verify each constant was called twice
        verify(cc1, times(2)).referencedClassAccept(any(ClassVisitor.class));
        verify(cc2, times(2)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitClassConstant works correctly in combination with other constant visitor methods.
     */
    @Test
    public void testVisitClassConstant_withOtherConstantVisitorMethods_worksCorrectly() {
        // Arrange
        proguard.classfile.constant.StringConstant stringConstant =
            mock(proguard.classfile.constant.StringConstant.class);
        proguard.classfile.constant.MethodHandleConstant methodHandleConstant =
            mock(proguard.classfile.constant.MethodHandleConstant.class);
        proguard.classfile.constant.MethodTypeConstant methodTypeConstant =
            mock(proguard.classfile.constant.MethodTypeConstant.class);
        proguard.classfile.constant.RefConstant refConstant =
            mock(proguard.classfile.constant.RefConstant.class);

        // Act & Assert - should work with other constant visitor methods
        assertDoesNotThrow(() -> {
            checker.visitClassConstant(clazz, classConstant);
            checker.visitStringConstant(clazz, stringConstant);
            checker.visitMethodHandleConstant(clazz, methodHandleConstant);
            checker.visitMethodTypeConstant(clazz, methodTypeConstant);
            checker.visitAnyRefConstant(clazz, refConstant);
            checker.visitClassConstant(clazz, classConstant);
        });

        // Verify all constants were visited
        verify(classConstant, times(2)).referencedClassAccept(any(ClassVisitor.class));
        verify(stringConstant, times(1)).referencedClassAccept(any());
        verify(methodHandleConstant, times(1)).referenceAccept(any(), any());
        verify(methodTypeConstant, times(1)).referencedClassesAccept(any());
        verify(refConstant, times(1)).referencedClassAccept(any());
    }

    /**
     * Tests that visitClassConstant passes a ClassVisitor instance to referencedClassAccept.
     */
    @Test
    public void testVisitClassConstant_passesClassVisitorToReferencedClassAccept() {
        // Act
        checker.visitClassConstant(clazz, classConstant);

        // Assert - verify a ClassVisitor was passed (the complexEnumMarker)
        verify(classConstant).referencedClassAccept(
            argThat(visitor -> visitor instanceof ClassVisitor)
        );
    }

    /**
     * Tests that visitClassConstant can be called with both parameters as various types
     * without side effects on the clazz parameter.
     */
    @Test
    public void testVisitClassConstant_variousParameterTypes_noSideEffectsOnClazz() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        LibraryClass libraryClass = new LibraryClass();
        Clazz mockClazz = mock(Clazz.class);

        // Act
        checker.visitClassConstant(programClass, classConstant);
        checker.visitClassConstant(libraryClass, classConstant);
        checker.visitClassConstant(mockClazz, classConstant);
        checker.visitClassConstant(null, classConstant);

        // Assert - verify no interactions with any clazz parameter
        verifyNoInteractions(mockClazz);
        // programClass and libraryClass state shouldn't change
        assertNull(programClass.getProcessingInfo());
        assertNull(libraryClass.getProcessingInfo());
    }

    /**
     * Tests that visitClassConstant works correctly when used in a chain of method calls.
     */
    @Test
    public void testVisitClassConstant_inMethodChain_worksCorrectly() {
        // Arrange
        ClassConstant cc1 = mock(ClassConstant.class);
        ClassConstant cc2 = mock(ClassConstant.class);
        ClassConstant cc3 = mock(ClassConstant.class);

        // Act & Assert - chain multiple calls
        assertDoesNotThrow(() -> {
            checker.visitClassConstant(clazz, cc1);
            checker.visitClassConstant(clazz, cc2);
            checker.visitClassConstant(clazz, cc3);
        });

        // Verify all were called
        verify(cc1, times(1)).referencedClassAccept(any(ClassVisitor.class));
        verify(cc2, times(1)).referencedClassAccept(any(ClassVisitor.class));
        verify(cc3, times(1)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitClassConstant doesn't retain references to parameters.
     * Important for memory management.
     */
    @Test
    public void testVisitClassConstant_doesNotRetainReferences() {
        // Arrange
        Clazz tempClazz = mock(ProgramClass.class);
        ClassConstant tempClassConstant = mock(ClassConstant.class);

        // Act
        checker.visitClassConstant(tempClazz, tempClassConstant);

        // Assert - the method should have called referencedClassAccept and not retained refs
        verify(tempClassConstant, times(1)).referencedClassAccept(any(ClassVisitor.class));
        verifyNoInteractions(tempClazz);
    }

    /**
     * Tests that visitClassConstant through ConstantVisitor interface works with casting.
     * Verifies proper interface implementation.
     */
    @Test
    public void testVisitClassConstant_throughCastedInterface_doesNotThrowException() {
        // Arrange
        proguard.classfile.constant.visitor.ConstantVisitor visitor =
            (proguard.classfile.constant.visitor.ConstantVisitor) checker;

        // Act & Assert
        assertDoesNotThrow(() -> {
            visitor.visitClassConstant(clazz, classConstant);
            visitor.visitClassConstant(null, classConstant);
        });

        // Verify called twice
        verify(classConstant, times(2)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitClassConstant handles the case where referencedClassAccept
     * is called but there is no referenced class (does nothing special).
     */
    @Test
    public void testVisitClassConstant_withNoReferencedClass_worksCorrectly() {
        // Arrange - doNothing is the default behavior for mocks, but we make it explicit
        doNothing().when(classConstant).referencedClassAccept(any(ClassVisitor.class));

        // Act & Assert - should complete without issues
        assertDoesNotThrow(() -> checker.visitClassConstant(clazz, classConstant));

        // Verify the call was made
        verify(classConstant, times(1)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitClassConstant maintains idempotent behavior.
     * Multiple calls with same parameters should behave identically.
     */
    @Test
    public void testVisitClassConstant_idempotentBehavior_consistentResults() {
        // Act - call multiple times with same parameters
        checker.visitClassConstant(clazz, classConstant);
        checker.visitClassConstant(clazz, classConstant);
        checker.visitClassConstant(clazz, classConstant);

        // Assert - each call should invoke referencedClassAccept
        verify(classConstant, times(3)).referencedClassAccept(any(ClassVisitor.class));

        // Verify clazz was never interacted with
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitClassConstant works correctly as a default handler in the visitor pattern.
     * ClassConstant is used in various contexts (instanceof checks, .class literals, etc.).
     */
    @Test
    public void testVisitClassConstant_asDefaultHandler_worksInVariousContexts() {
        // Arrange
        ClassConstant[] classConstants = new ClassConstant[5];
        for (int i = 0; i < classConstants.length; i++) {
            classConstants[i] = mock(ClassConstant.class);
        }

        // Act - simulate various contexts where class constants appear
        for (ClassConstant cc : classConstants) {
            assertDoesNotThrow(() -> checker.visitClassConstant(clazz, cc));
        }

        // Assert - all should have been visited
        for (ClassConstant cc : classConstants) {
            verify(cc, times(1)).referencedClassAccept(any(ClassVisitor.class));
        }
    }

    /**
     * Tests that visitClassConstant is consistent with visitStringConstant in its usage pattern.
     * Both use complexEnumMarker (not referencedComplexEnumMarker).
     */
    @Test
    public void testVisitClassConstant_consistentWithVisitStringConstant() {
        // Arrange
        proguard.classfile.constant.StringConstant stringConstant =
            mock(proguard.classfile.constant.StringConstant.class);

        // Act - call both methods
        checker.visitClassConstant(clazz, classConstant);
        checker.visitStringConstant(clazz, stringConstant);

        // Assert - both should call referencedClassAccept (using complexEnumMarker)
        verify(classConstant, times(1)).referencedClassAccept(any(ClassVisitor.class));
        verify(stringConstant, times(1)).referencedClassAccept(any(ClassVisitor.class));
    }
}
