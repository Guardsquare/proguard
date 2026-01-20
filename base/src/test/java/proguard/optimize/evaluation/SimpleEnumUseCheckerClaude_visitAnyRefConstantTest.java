package proguard.optimize.evaluation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.LibraryClass;
import proguard.classfile.constant.RefConstant;
import proguard.classfile.constant.FieldrefConstant;
import proguard.classfile.constant.MethodrefConstant;
import proguard.classfile.constant.InterfaceMethodrefConstant;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link SimpleEnumUseChecker#visitAnyRefConstant(Clazz, RefConstant)}.
 *
 * The visitAnyRefConstant method is responsible for unmarking simple enum classes
 * that are referenced in a reference constant (RefConstant). RefConstant is the base
 * class for field references, method references, and interface method references.
 * The method calls refConstant.referencedClassAccept(referencedComplexEnumMarker),
 * which marks the referenced class as a complex enum (not eligible for simple enum optimizations).
 */
public class SimpleEnumUseCheckerClaude_visitAnyRefConstantTest {

    private SimpleEnumUseChecker checker;
    private Clazz clazz;
    private RefConstant refConstant;

    @BeforeEach
    public void setUp() {
        checker = new SimpleEnumUseChecker();
        clazz = mock(ProgramClass.class);
        refConstant = mock(RefConstant.class);
    }

    /**
     * Tests that visitAnyRefConstant calls referencedClassAccept on the RefConstant.
     * This is the core behavior - the method should delegate to the ref constant
     * to visit the referenced class with the referencedComplexEnumMarker.
     */
    @Test
    public void testVisitAnyRefConstant_callsReferencedClassAccept() {
        // Act
        checker.visitAnyRefConstant(clazz, refConstant);

        // Assert - verify that referencedClassAccept was called with a ClassVisitor
        verify(refConstant, times(1)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitAnyRefConstant works with valid mock objects without throwing exceptions.
     */
    @Test
    public void testVisitAnyRefConstant_withValidMocks_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.visitAnyRefConstant(clazz, refConstant));
    }

    /**
     * Tests that visitAnyRefConstant can be called with null Clazz parameter.
     * The clazz parameter is not used in the method implementation, so null should be handled gracefully.
     */
    @Test
    public void testVisitAnyRefConstant_withNullClazz_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.visitAnyRefConstant(null, refConstant));

        // Verify the method still calls referencedClassAccept
        verify(refConstant, times(1)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitAnyRefConstant with null RefConstant throws NullPointerException.
     * This should result in a NullPointerException since the method calls a method on refConstant.
     */
    @Test
    public void testVisitAnyRefConstant_withNullRefConstant_throwsNullPointerException() {
        // Act & Assert - should throw NullPointerException
        assertThrows(NullPointerException.class,
            () -> checker.visitAnyRefConstant(clazz, null));
    }

    /**
     * Tests that visitAnyRefConstant can be called multiple times in succession.
     * Each call should invoke referencedClassAccept.
     */
    @Test
    public void testVisitAnyRefConstant_calledMultipleTimes_invokesReferencedClassAcceptEachTime() {
        // Act
        checker.visitAnyRefConstant(clazz, refConstant);
        checker.visitAnyRefConstant(clazz, refConstant);
        checker.visitAnyRefConstant(clazz, refConstant);

        // Assert - verify referencedClassAccept was called three times
        verify(refConstant, times(3)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitAnyRefConstant doesn't directly interact with the Clazz parameter.
     * The clazz is passed as a context parameter but not used in this method.
     */
    @Test
    public void testVisitAnyRefConstant_doesNotInteractWithClazz() {
        // Act
        checker.visitAnyRefConstant(clazz, refConstant);

        // Assert - verify no interactions occurred with the clazz mock
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitAnyRefConstant can be used as part of the ConstantVisitor interface.
     * Verifies integration with the visitor pattern.
     */
    @Test
    public void testVisitAnyRefConstant_usedAsConstantVisitor_worksCorrectly() {
        // Arrange
        proguard.classfile.constant.visitor.ConstantVisitor visitor = checker;

        // Act & Assert
        assertDoesNotThrow(() -> visitor.visitAnyRefConstant(clazz, refConstant));
        verify(refConstant, times(1)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitAnyRefConstant can be called with real ProgramClass instance.
     * Verifies the method works with actual class instances, not just mocks.
     */
    @Test
    public void testVisitAnyRefConstant_withRealProgramClass_doesNotThrowException() {
        // Arrange
        ProgramClass realClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitAnyRefConstant(realClass, refConstant));
        verify(refConstant, times(1)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitAnyRefConstant can be called with real LibraryClass instance.
     * Verifies the method works with library classes.
     */
    @Test
    public void testVisitAnyRefConstant_withRealLibraryClass_doesNotThrowException() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitAnyRefConstant(libraryClass, refConstant));
        verify(refConstant, times(1)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitAnyRefConstant can be called rapidly in succession.
     * Verifies consistent behavior under repeated calls.
     */
    @Test
    public void testVisitAnyRefConstant_rapidSequentialCalls_worksCorrectly() {
        // Act
        for (int i = 0; i < 100; i++) {
            assertDoesNotThrow(() -> checker.visitAnyRefConstant(clazz, refConstant),
                    "Call " + i + " should not throw exception");
        }

        // Assert - verify referencedClassAccept was called 100 times
        verify(refConstant, times(100)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that multiple SimpleEnumUseChecker instances can all call visitAnyRefConstant
     * on the same RefConstant without interference.
     */
    @Test
    public void testVisitAnyRefConstant_multipleCheckers_allWorkCorrectly() {
        // Arrange
        SimpleEnumUseChecker checker1 = new SimpleEnumUseChecker();
        SimpleEnumUseChecker checker2 = new SimpleEnumUseChecker();
        SimpleEnumUseChecker checker3 = new SimpleEnumUseChecker();

        // Act
        checker1.visitAnyRefConstant(clazz, refConstant);
        checker2.visitAnyRefConstant(clazz, refConstant);
        checker3.visitAnyRefConstant(clazz, refConstant);

        // Assert - verify referencedClassAccept was called three times
        verify(refConstant, times(3)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitAnyRefConstant works with different RefConstant instances.
     * Verifies the method can handle multiple different reference constants.
     */
    @Test
    public void testVisitAnyRefConstant_withDifferentRefConstants_worksCorrectly() {
        // Arrange
        RefConstant rc1 = mock(RefConstant.class);
        RefConstant rc2 = mock(RefConstant.class);
        RefConstant rc3 = mock(RefConstant.class);

        // Act
        checker.visitAnyRefConstant(clazz, rc1);
        checker.visitAnyRefConstant(clazz, rc2);
        checker.visitAnyRefConstant(clazz, rc3);

        // Assert - verify each constant had referencedClassAccept called
        verify(rc1, times(1)).referencedClassAccept(any(ClassVisitor.class));
        verify(rc2, times(1)).referencedClassAccept(any(ClassVisitor.class));
        verify(rc3, times(1)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitAnyRefConstant works with different Clazz instances.
     * Verifies the method can handle multiple different clazz contexts.
     */
    @Test
    public void testVisitAnyRefConstant_withDifferentClazzes_worksCorrectly() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(LibraryClass.class);
        ProgramClass clazz3 = new ProgramClass();

        // Act
        checker.visitAnyRefConstant(clazz1, refConstant);
        checker.visitAnyRefConstant(clazz2, refConstant);
        checker.visitAnyRefConstant(clazz3, refConstant);

        // Assert - verify referencedClassAccept was called three times
        verify(refConstant, times(3)).referencedClassAccept(any(ClassVisitor.class));

        // Verify no interactions with clazz parameters
        verifyNoInteractions(clazz1, clazz2);
    }

    /**
     * Tests that visitAnyRefConstant maintains consistent behavior across multiple calls
     * with the same parameters.
     */
    @Test
    public void testVisitAnyRefConstant_repeatedCallsSameParameters_consistentBehavior() {
        // Act & Assert - multiple calls should have identical behavior
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> checker.visitAnyRefConstant(clazz, refConstant));
        }

        // Verify referencedClassAccept was called 10 times
        verify(refConstant, times(10)).referencedClassAccept(any(ClassVisitor.class));

        // Verify no interactions with clazz
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitAnyRefConstant can be called through the ConstantVisitor interface
     * with various parameter combinations.
     */
    @Test
    public void testVisitAnyRefConstant_throughInterface_worksWithVariousParameters() {
        // Arrange
        proguard.classfile.constant.visitor.ConstantVisitor visitor = checker;
        ProgramClass programClass = new ProgramClass();
        LibraryClass libraryClass = new LibraryClass();

        // Act & Assert
        assertDoesNotThrow(() -> {
            visitor.visitAnyRefConstant(programClass, refConstant);
            visitor.visitAnyRefConstant(libraryClass, refConstant);
            visitor.visitAnyRefConstant(null, refConstant);
        });

        // Verify referencedClassAccept was called three times
        verify(refConstant, times(3)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitAnyRefConstant works correctly when using a custom PartialEvaluator.
     * Verifies that the method behavior is consistent across different checker configurations.
     */
    @Test
    public void testVisitAnyRefConstant_withCustomPartialEvaluator_worksCorrectly() {
        // Arrange
        proguard.evaluation.PartialEvaluator evaluator =
            proguard.evaluation.PartialEvaluator.Builder.create().build();
        SimpleEnumUseChecker customChecker = new SimpleEnumUseChecker(evaluator);

        // Act
        customChecker.visitAnyRefConstant(clazz, refConstant);

        // Assert
        verify(refConstant, times(1)).referencedClassAccept(any(ClassVisitor.class));
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitAnyRefConstant can handle interleaved calls with different parameters.
     * Verifies robustness with varied call patterns.
     */
    @Test
    public void testVisitAnyRefConstant_interleavedCalls_worksCorrectly() {
        // Arrange
        RefConstant rc1 = mock(RefConstant.class);
        RefConstant rc2 = mock(RefConstant.class);
        Clazz clazz2 = mock(ProgramClass.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            checker.visitAnyRefConstant(clazz, rc1);
            checker.visitAnyRefConstant(clazz2, rc2);
            checker.visitAnyRefConstant(clazz, rc2);
            checker.visitAnyRefConstant(clazz2, rc1);
        });

        // Verify each ref constant was called twice
        verify(rc1, times(2)).referencedClassAccept(any(ClassVisitor.class));
        verify(rc2, times(2)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitAnyRefConstant passes a non-null ClassVisitor to referencedClassAccept.
     * The visitor should be the referencedComplexEnumMarker.
     */
    @Test
    public void testVisitAnyRefConstant_passesNonNullClassVisitor() {
        // Act
        checker.visitAnyRefConstant(clazz, refConstant);

        // Assert - verify that referencedClassAccept was called with a non-null ClassVisitor
        verify(refConstant).referencedClassAccept(argThat(visitor -> visitor != null));
    }

    /**
     * Tests that visitAnyRefConstant maintains correct behavior when called in a sequence
     * with other visitor methods.
     */
    @Test
    public void testVisitAnyRefConstant_inVisitorSequence_worksCorrectly() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - should work in a sequence of visitor calls
        assertDoesNotThrow(() -> {
            checker.visitAnyClass(programClass);
            checker.visitAnyRefConstant(programClass, refConstant);
            checker.visitAnyClass(programClass);
            checker.visitAnyRefConstant(programClass, refConstant);
        });

        // Verify referencedClassAccept was called twice
        verify(refConstant, times(2)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitAnyRefConstant doesn't modify the state of the clazz parameter.
     */
    @Test
    public void testVisitAnyRefConstant_doesNotModifyClazzState() {
        // Arrange
        ProgramClass realClass = new ProgramClass();
        Object initialProcessingInfo = new Object();
        realClass.setProcessingInfo(initialProcessingInfo);

        // Act
        checker.visitAnyRefConstant(realClass, refConstant);

        // Assert - verify the class state wasn't modified
        assertSame(initialProcessingInfo, realClass.getProcessingInfo(),
                "Class processing info should not be modified");
    }

    /**
     * Tests that visitAnyRefConstant can handle extreme numbers of calls without issues.
     * Verifies the implementation doesn't accumulate state or resources.
     */
    @Test
    public void testVisitAnyRefConstant_extremeNumberOfCalls_worksCorrectly() {
        // Act
        for (int i = 0; i < 1000; i++) {
            assertDoesNotThrow(() -> checker.visitAnyRefConstant(clazz, refConstant));
        }

        // Assert - verify referencedClassAccept was called 1000 times
        verify(refConstant, times(1000)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitAnyRefConstant works correctly with mixed real and mock objects.
     */
    @Test
    public void testVisitAnyRefConstant_mixedRealAndMockObjects_worksCorrectly() {
        // Arrange
        ProgramClass realProgramClass = new ProgramClass();
        LibraryClass realLibraryClass = new LibraryClass();
        Clazz mockClazz = mock(Clazz.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            checker.visitAnyRefConstant(realProgramClass, refConstant);
            checker.visitAnyRefConstant(realLibraryClass, refConstant);
            checker.visitAnyRefConstant(mockClazz, refConstant);
        });

        // Verify referencedClassAccept was called three times
        verify(refConstant, times(3)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that multiple checker instances can call visitAnyRefConstant independently
     * without interfering with each other.
     */
    @Test
    public void testVisitAnyRefConstant_multipleInstancesIndependent_worksCorrectly() {
        // Arrange
        SimpleEnumUseChecker checker1 = new SimpleEnumUseChecker();
        SimpleEnumUseChecker checker2 = new SimpleEnumUseChecker();
        RefConstant rc1 = mock(RefConstant.class);
        RefConstant rc2 = mock(RefConstant.class);

        // Act
        checker1.visitAnyRefConstant(clazz, rc1);
        checker2.visitAnyRefConstant(clazz, rc2);

        // Assert - verify each checker called its respective ref constant
        verify(rc1, times(1)).referencedClassAccept(any(ClassVisitor.class));
        verify(rc2, times(1)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitAnyRefConstant maintains the visitor pattern contract.
     * The method should always call referencedClassAccept regardless of the clazz parameter.
     */
    @Test
    public void testVisitAnyRefConstant_visitorPatternContract_alwaysCallsReferencedClassAccept() {
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
            assertDoesNotThrow(() -> checker.visitAnyRefConstant(c, refConstant));
        }

        // Assert - verify referencedClassAccept was called for each variation
        verify(refConstant, times(differentClazzes.length))
            .referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitAnyRefConstant behavior remains consistent after being called
     * with null clazz parameters.
     */
    @Test
    public void testVisitAnyRefConstant_afterNullClazz_behaviorRemainsConsistent() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            checker.visitAnyRefConstant(null, refConstant);
            checker.visitAnyRefConstant(clazz, refConstant);
            checker.visitAnyRefConstant(null, refConstant);
        });

        // Verify referencedClassAccept was called three times
        verify(refConstant, times(3)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitAnyRefConstant can be called as part of a complex visitor workflow.
     */
    @Test
    public void testVisitAnyRefConstant_inComplexWorkflow_worksCorrectly() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        RefConstant rc1 = mock(RefConstant.class);
        RefConstant rc2 = mock(RefConstant.class);

        // Act & Assert - simulate a complex visitor workflow
        assertDoesNotThrow(() -> {
            checker.visitAnyClass(programClass);
            checker.visitAnyRefConstant(programClass, rc1);
            checker.visitAnyConstant(programClass, rc1);
            checker.visitAnyRefConstant(programClass, rc2);
            checker.visitAnyClass(programClass);
        });

        // Verify both ref constants had referencedClassAccept called
        verify(rc1, times(1)).referencedClassAccept(any(ClassVisitor.class));
        verify(rc2, times(1)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitAnyRefConstant works with FieldrefConstant, a subclass of RefConstant.
     */
    @Test
    public void testVisitAnyRefConstant_withFieldrefConstant_worksCorrectly() {
        // Arrange
        FieldrefConstant fieldrefConstant = mock(FieldrefConstant.class);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitAnyRefConstant(clazz, fieldrefConstant));

        // Verify referencedClassAccept was called
        verify(fieldrefConstant, times(1)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitAnyRefConstant works with MethodrefConstant, a subclass of RefConstant.
     */
    @Test
    public void testVisitAnyRefConstant_withMethodrefConstant_worksCorrectly() {
        // Arrange
        MethodrefConstant methodrefConstant = mock(MethodrefConstant.class);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitAnyRefConstant(clazz, methodrefConstant));

        // Verify referencedClassAccept was called
        verify(methodrefConstant, times(1)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitAnyRefConstant works with InterfaceMethodrefConstant, a subclass of RefConstant.
     */
    @Test
    public void testVisitAnyRefConstant_withInterfaceMethodrefConstant_worksCorrectly() {
        // Arrange
        InterfaceMethodrefConstant interfaceMethodrefConstant = mock(InterfaceMethodrefConstant.class);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitAnyRefConstant(clazz, interfaceMethodrefConstant));

        // Verify referencedClassAccept was called
        verify(interfaceMethodrefConstant, times(1)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitAnyRefConstant works with all three RefConstant subtypes in sequence.
     */
    @Test
    public void testVisitAnyRefConstant_withAllRefConstantSubtypes_allWorkCorrectly() {
        // Arrange
        FieldrefConstant fieldrefConstant = mock(FieldrefConstant.class);
        MethodrefConstant methodrefConstant = mock(MethodrefConstant.class);
        InterfaceMethodrefConstant interfaceMethodrefConstant = mock(InterfaceMethodrefConstant.class);

        // Act
        checker.visitAnyRefConstant(clazz, fieldrefConstant);
        checker.visitAnyRefConstant(clazz, methodrefConstant);
        checker.visitAnyRefConstant(clazz, interfaceMethodrefConstant);

        // Assert - verify each had referencedClassAccept called
        verify(fieldrefConstant, times(1)).referencedClassAccept(any(ClassVisitor.class));
        verify(methodrefConstant, times(1)).referencedClassAccept(any(ClassVisitor.class));
        verify(interfaceMethodrefConstant, times(1)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitAnyRefConstant works correctly when alternating between
     * different checker instances and parameters.
     */
    @Test
    public void testVisitAnyRefConstant_alternatingCheckersAndParameters_consistentBehavior() {
        // Arrange
        SimpleEnumUseChecker checker1 = new SimpleEnumUseChecker();
        SimpleEnumUseChecker checker2 = new SimpleEnumUseChecker();
        RefConstant rc1 = mock(RefConstant.class);
        RefConstant rc2 = mock(RefConstant.class);
        Clazz clazz2 = mock(ProgramClass.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            checker1.visitAnyRefConstant(clazz, rc1);
            checker2.visitAnyRefConstant(clazz2, rc2);
            checker1.visitAnyRefConstant(clazz2, rc2);
            checker2.visitAnyRefConstant(clazz, rc1);
        });

        // Verify each constant was called twice
        verify(rc1, times(2)).referencedClassAccept(any(ClassVisitor.class));
        verify(rc2, times(2)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitAnyRefConstant works correctly in combination with other constant visitor methods.
     */
    @Test
    public void testVisitAnyRefConstant_withOtherConstantVisitorMethods_worksCorrectly() {
        // Arrange
        proguard.classfile.constant.StringConstant stringConstant =
            mock(proguard.classfile.constant.StringConstant.class);
        proguard.classfile.constant.MethodHandleConstant methodHandleConstant =
            mock(proguard.classfile.constant.MethodHandleConstant.class);
        proguard.classfile.constant.MethodTypeConstant methodTypeConstant =
            mock(proguard.classfile.constant.MethodTypeConstant.class);

        // Act & Assert - should work with other constant visitor methods
        assertDoesNotThrow(() -> {
            checker.visitAnyRefConstant(clazz, refConstant);
            checker.visitStringConstant(clazz, stringConstant);
            checker.visitMethodHandleConstant(clazz, methodHandleConstant);
            checker.visitMethodTypeConstant(clazz, methodTypeConstant);
            checker.visitAnyRefConstant(clazz, refConstant);
        });

        // Verify all constants were visited
        verify(refConstant, times(2)).referencedClassAccept(any(ClassVisitor.class));
        verify(stringConstant, times(1)).referencedClassAccept(any());
        verify(methodHandleConstant, times(1)).referenceAccept(any(), any());
        verify(methodTypeConstant, times(1)).referencedClassesAccept(any());
    }

    /**
     * Tests that visitAnyRefConstant passes a ClassVisitor instance to referencedClassAccept.
     */
    @Test
    public void testVisitAnyRefConstant_passesClassVisitorToReferencedClassAccept() {
        // Act
        checker.visitAnyRefConstant(clazz, refConstant);

        // Assert - verify a ClassVisitor was passed (the referencedComplexEnumMarker)
        verify(refConstant).referencedClassAccept(
            argThat(visitor -> visitor instanceof ClassVisitor)
        );
    }

    /**
     * Tests that visitAnyRefConstant can be called with both parameters as various types
     * without side effects on the clazz parameter.
     */
    @Test
    public void testVisitAnyRefConstant_variousParameterTypes_noSideEffectsOnClazz() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        LibraryClass libraryClass = new LibraryClass();
        Clazz mockClazz = mock(Clazz.class);

        // Act
        checker.visitAnyRefConstant(programClass, refConstant);
        checker.visitAnyRefConstant(libraryClass, refConstant);
        checker.visitAnyRefConstant(mockClazz, refConstant);
        checker.visitAnyRefConstant(null, refConstant);

        // Assert - verify no interactions with any clazz parameter
        verifyNoInteractions(mockClazz);
        // programClass and libraryClass state shouldn't change
        assertNull(programClass.getProcessingInfo());
        assertNull(libraryClass.getProcessingInfo());
    }

    /**
     * Tests that visitAnyRefConstant works correctly when used in a chain of method calls.
     */
    @Test
    public void testVisitAnyRefConstant_inMethodChain_worksCorrectly() {
        // Arrange
        RefConstant rc1 = mock(RefConstant.class);
        RefConstant rc2 = mock(RefConstant.class);
        RefConstant rc3 = mock(RefConstant.class);

        // Act & Assert - chain multiple calls
        assertDoesNotThrow(() -> {
            checker.visitAnyRefConstant(clazz, rc1);
            checker.visitAnyRefConstant(clazz, rc2);
            checker.visitAnyRefConstant(clazz, rc3);
        });

        // Verify all were called
        verify(rc1, times(1)).referencedClassAccept(any(ClassVisitor.class));
        verify(rc2, times(1)).referencedClassAccept(any(ClassVisitor.class));
        verify(rc3, times(1)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitAnyRefConstant doesn't retain references to parameters.
     * Important for memory management.
     */
    @Test
    public void testVisitAnyRefConstant_doesNotRetainReferences() {
        // Arrange
        Clazz tempClazz = mock(ProgramClass.class);
        RefConstant tempRefConstant = mock(RefConstant.class);

        // Act
        checker.visitAnyRefConstant(tempClazz, tempRefConstant);

        // Assert - the method should have called referencedClassAccept and not retained refs
        verify(tempRefConstant, times(1)).referencedClassAccept(any(ClassVisitor.class));
        verifyNoInteractions(tempClazz);
    }

    /**
     * Tests that visitAnyRefConstant through ConstantVisitor interface works with casting.
     * Verifies proper interface implementation.
     */
    @Test
    public void testVisitAnyRefConstant_throughCastedInterface_doesNotThrowException() {
        // Arrange
        proguard.classfile.constant.visitor.ConstantVisitor visitor =
            (proguard.classfile.constant.visitor.ConstantVisitor) checker;

        // Act & Assert
        assertDoesNotThrow(() -> {
            visitor.visitAnyRefConstant(clazz, refConstant);
            visitor.visitAnyRefConstant(null, refConstant);
        });

        // Verify called twice
        verify(refConstant, times(2)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitAnyRefConstant handles the polymorphic nature of RefConstant.
     * RefConstant can be Fieldref, Methodref, or InterfaceMethodref.
     */
    @Test
    public void testVisitAnyRefConstant_polymorphicRefConstant_handlesAllTypes() {
        // Arrange
        RefConstant[] refConstants = {
            mock(FieldrefConstant.class),
            mock(MethodrefConstant.class),
            mock(InterfaceMethodrefConstant.class),
            mock(RefConstant.class)
        };

        // Act - call with each type
        for (RefConstant rc : refConstants) {
            assertDoesNotThrow(() -> checker.visitAnyRefConstant(clazz, rc));
        }

        // Assert - verify each was visited
        for (RefConstant rc : refConstants) {
            verify(rc, times(1)).referencedClassAccept(any(ClassVisitor.class));
        }
    }
}
