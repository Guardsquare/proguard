package proguard.optimize.evaluation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.LibraryClass;
import proguard.classfile.constant.MethodHandleConstant;
import proguard.classfile.constant.visitor.ConstantVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link SimpleEnumUseChecker#visitMethodHandleConstant(Clazz, MethodHandleConstant)}.
 *
 * The visitMethodHandleConstant method is responsible for unmarking simple enum classes
 * that are referenced in a method handle constant (through a reference constant). This is done
 * by calling methodHandleConstant.referenceAccept(clazz, this), which delegates to process
 * the reference constant within the method handle with the SimpleEnumUseChecker as a visitor.
 */
public class SimpleEnumUseCheckerClaude_visitMethodHandleConstantTest {

    private SimpleEnumUseChecker checker;
    private Clazz clazz;
    private MethodHandleConstant methodHandleConstant;

    @BeforeEach
    public void setUp() {
        checker = new SimpleEnumUseChecker();
        clazz = mock(ProgramClass.class);
        methodHandleConstant = mock(MethodHandleConstant.class);
    }

    /**
     * Tests that visitMethodHandleConstant calls referenceAccept on the MethodHandleConstant.
     * This is the core behavior - the method should delegate to the method handle constant
     * to visit the reference constant with both the clazz and the checker as a ConstantVisitor.
     */
    @Test
    public void testVisitMethodHandleConstant_callsReferenceAccept() {
        // Act
        checker.visitMethodHandleConstant(clazz, methodHandleConstant);

        // Assert - verify that referenceAccept was called with clazz and a ConstantVisitor
        verify(methodHandleConstant, times(1)).referenceAccept(eq(clazz), any(ConstantVisitor.class));
    }

    /**
     * Tests that visitMethodHandleConstant passes the correct clazz parameter to referenceAccept.
     */
    @Test
    public void testVisitMethodHandleConstant_passesCorrectClazz() {
        // Act
        checker.visitMethodHandleConstant(clazz, methodHandleConstant);

        // Assert - verify that referenceAccept was called with the exact clazz instance
        verify(methodHandleConstant).referenceAccept(same(clazz), any(ConstantVisitor.class));
    }

    /**
     * Tests that visitMethodHandleConstant passes the checker itself as the ConstantVisitor.
     */
    @Test
    public void testVisitMethodHandleConstant_passesCheckerAsVisitor() {
        // Act
        checker.visitMethodHandleConstant(clazz, methodHandleConstant);

        // Assert - verify that referenceAccept was called with the checker as the visitor
        verify(methodHandleConstant).referenceAccept(any(Clazz.class), same(checker));
    }

    /**
     * Tests that visitMethodHandleConstant works with valid mock objects without throwing exceptions.
     */
    @Test
    public void testVisitMethodHandleConstant_withValidMocks_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.visitMethodHandleConstant(clazz, methodHandleConstant));
    }

    /**
     * Tests that visitMethodHandleConstant can be called with null Clazz parameter.
     * The method should pass the null to referenceAccept.
     */
    @Test
    public void testVisitMethodHandleConstant_withNullClazz_callsReferenceAccept() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.visitMethodHandleConstant(null, methodHandleConstant));

        // Verify the method still calls referenceAccept with null clazz
        verify(methodHandleConstant, times(1)).referenceAccept(isNull(), any(ConstantVisitor.class));
    }

    /**
     * Tests that visitMethodHandleConstant with null MethodHandleConstant throws NullPointerException.
     * This should result in a NullPointerException since the method calls a method on methodHandleConstant.
     */
    @Test
    public void testVisitMethodHandleConstant_withNullMethodHandleConstant_throwsNullPointerException() {
        // Act & Assert - should throw NullPointerException
        assertThrows(NullPointerException.class,
            () -> checker.visitMethodHandleConstant(clazz, null));
    }

    /**
     * Tests that visitMethodHandleConstant can be called multiple times in succession.
     * Each call should invoke referenceAccept.
     */
    @Test
    public void testVisitMethodHandleConstant_calledMultipleTimes_invokesReferenceAcceptEachTime() {
        // Act
        checker.visitMethodHandleConstant(clazz, methodHandleConstant);
        checker.visitMethodHandleConstant(clazz, methodHandleConstant);
        checker.visitMethodHandleConstant(clazz, methodHandleConstant);

        // Assert - verify referenceAccept was called three times
        verify(methodHandleConstant, times(3)).referenceAccept(eq(clazz), any(ConstantVisitor.class));
    }

    /**
     * Tests that visitMethodHandleConstant can be used as part of the ConstantVisitor interface.
     * Verifies integration with the visitor pattern.
     */
    @Test
    public void testVisitMethodHandleConstant_usedAsConstantVisitor_worksCorrectly() {
        // Arrange
        ConstantVisitor visitor = checker;

        // Act & Assert
        assertDoesNotThrow(() -> visitor.visitMethodHandleConstant(clazz, methodHandleConstant));
        verify(methodHandleConstant, times(1)).referenceAccept(eq(clazz), any(ConstantVisitor.class));
    }

    /**
     * Tests that visitMethodHandleConstant can be called with real ProgramClass instance.
     * Verifies the method works with actual class instances, not just mocks.
     */
    @Test
    public void testVisitMethodHandleConstant_withRealProgramClass_doesNotThrowException() {
        // Arrange
        ProgramClass realClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitMethodHandleConstant(realClass, methodHandleConstant));
        verify(methodHandleConstant, times(1)).referenceAccept(eq(realClass), any(ConstantVisitor.class));
    }

    /**
     * Tests that visitMethodHandleConstant can be called with real LibraryClass instance.
     * Verifies the method works with library classes.
     */
    @Test
    public void testVisitMethodHandleConstant_withRealLibraryClass_doesNotThrowException() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitMethodHandleConstant(libraryClass, methodHandleConstant));
        verify(methodHandleConstant, times(1)).referenceAccept(eq(libraryClass), any(ConstantVisitor.class));
    }

    /**
     * Tests that visitMethodHandleConstant can be called rapidly in succession.
     * Verifies consistent behavior under repeated calls.
     */
    @Test
    public void testVisitMethodHandleConstant_rapidSequentialCalls_worksCorrectly() {
        // Act
        for (int i = 0; i < 100; i++) {
            assertDoesNotThrow(() -> checker.visitMethodHandleConstant(clazz, methodHandleConstant),
                    "Call " + i + " should not throw exception");
        }

        // Assert - verify referenceAccept was called 100 times
        verify(methodHandleConstant, times(100)).referenceAccept(eq(clazz), any(ConstantVisitor.class));
    }

    /**
     * Tests that multiple SimpleEnumUseChecker instances can all call visitMethodHandleConstant
     * on the same MethodHandleConstant without interference.
     */
    @Test
    public void testVisitMethodHandleConstant_multipleCheckers_allWorkCorrectly() {
        // Arrange
        SimpleEnumUseChecker checker1 = new SimpleEnumUseChecker();
        SimpleEnumUseChecker checker2 = new SimpleEnumUseChecker();
        SimpleEnumUseChecker checker3 = new SimpleEnumUseChecker();

        // Act
        checker1.visitMethodHandleConstant(clazz, methodHandleConstant);
        checker2.visitMethodHandleConstant(clazz, methodHandleConstant);
        checker3.visitMethodHandleConstant(clazz, methodHandleConstant);

        // Assert - verify referenceAccept was called three times
        verify(methodHandleConstant, times(3)).referenceAccept(eq(clazz), any(ConstantVisitor.class));
    }

    /**
     * Tests that visitMethodHandleConstant works with different MethodHandleConstant instances.
     * Verifies the method can handle multiple different method handle constants.
     */
    @Test
    public void testVisitMethodHandleConstant_withDifferentMethodHandleConstants_worksCorrectly() {
        // Arrange
        MethodHandleConstant mhc1 = mock(MethodHandleConstant.class);
        MethodHandleConstant mhc2 = mock(MethodHandleConstant.class);
        MethodHandleConstant mhc3 = mock(MethodHandleConstant.class);

        // Act
        checker.visitMethodHandleConstant(clazz, mhc1);
        checker.visitMethodHandleConstant(clazz, mhc2);
        checker.visitMethodHandleConstant(clazz, mhc3);

        // Assert - verify each constant had referenceAccept called
        verify(mhc1, times(1)).referenceAccept(eq(clazz), any(ConstantVisitor.class));
        verify(mhc2, times(1)).referenceAccept(eq(clazz), any(ConstantVisitor.class));
        verify(mhc3, times(1)).referenceAccept(eq(clazz), any(ConstantVisitor.class));
    }

    /**
     * Tests that visitMethodHandleConstant works with different Clazz instances.
     * Verifies the method can handle multiple different clazz contexts.
     */
    @Test
    public void testVisitMethodHandleConstant_withDifferentClazzes_worksCorrectly() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(LibraryClass.class);
        ProgramClass clazz3 = new ProgramClass();

        // Act
        checker.visitMethodHandleConstant(clazz1, methodHandleConstant);
        checker.visitMethodHandleConstant(clazz2, methodHandleConstant);
        checker.visitMethodHandleConstant(clazz3, methodHandleConstant);

        // Assert - verify referenceAccept was called three times with each clazz
        verify(methodHandleConstant).referenceAccept(eq(clazz1), any(ConstantVisitor.class));
        verify(methodHandleConstant).referenceAccept(eq(clazz2), any(ConstantVisitor.class));
        verify(methodHandleConstant).referenceAccept(eq(clazz3), any(ConstantVisitor.class));
    }

    /**
     * Tests that visitMethodHandleConstant maintains consistent behavior across multiple calls
     * with the same parameters.
     */
    @Test
    public void testVisitMethodHandleConstant_repeatedCallsSameParameters_consistentBehavior() {
        // Act & Assert - multiple calls should have identical behavior
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> checker.visitMethodHandleConstant(clazz, methodHandleConstant));
        }

        // Verify referenceAccept was called 10 times
        verify(methodHandleConstant, times(10)).referenceAccept(eq(clazz), any(ConstantVisitor.class));
    }

    /**
     * Tests that visitMethodHandleConstant can be called through the ConstantVisitor interface
     * with various parameter combinations.
     */
    @Test
    public void testVisitMethodHandleConstant_throughInterface_worksWithVariousParameters() {
        // Arrange
        ConstantVisitor visitor = checker;
        ProgramClass programClass = new ProgramClass();
        LibraryClass libraryClass = new LibraryClass();

        // Act & Assert
        assertDoesNotThrow(() -> {
            visitor.visitMethodHandleConstant(programClass, methodHandleConstant);
            visitor.visitMethodHandleConstant(libraryClass, methodHandleConstant);
            visitor.visitMethodHandleConstant(null, methodHandleConstant);
        });

        // Verify referenceAccept was called three times
        verify(methodHandleConstant, times(3)).referenceAccept(any(), any(ConstantVisitor.class));
    }

    /**
     * Tests that visitMethodHandleConstant works correctly when using a custom PartialEvaluator.
     * Verifies that the method behavior is consistent across different checker configurations.
     */
    @Test
    public void testVisitMethodHandleConstant_withCustomPartialEvaluator_worksCorrectly() {
        // Arrange
        proguard.evaluation.PartialEvaluator evaluator =
            proguard.evaluation.PartialEvaluator.Builder.create().build();
        SimpleEnumUseChecker customChecker = new SimpleEnumUseChecker(evaluator);

        // Act
        customChecker.visitMethodHandleConstant(clazz, methodHandleConstant);

        // Assert
        verify(methodHandleConstant, times(1)).referenceAccept(eq(clazz), same(customChecker));
    }

    /**
     * Tests that visitMethodHandleConstant can handle interleaved calls with different parameters.
     * Verifies robustness with varied call patterns.
     */
    @Test
    public void testVisitMethodHandleConstant_interleavedCalls_worksCorrectly() {
        // Arrange
        MethodHandleConstant mhc1 = mock(MethodHandleConstant.class);
        MethodHandleConstant mhc2 = mock(MethodHandleConstant.class);
        Clazz clazz2 = mock(ProgramClass.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            checker.visitMethodHandleConstant(clazz, mhc1);
            checker.visitMethodHandleConstant(clazz2, mhc2);
            checker.visitMethodHandleConstant(clazz, mhc2);
            checker.visitMethodHandleConstant(clazz2, mhc1);
        });

        // Verify each method handle constant was called twice
        verify(mhc1, times(2)).referenceAccept(any(Clazz.class), any(ConstantVisitor.class));
        verify(mhc2, times(2)).referenceAccept(any(Clazz.class), any(ConstantVisitor.class));
    }

    /**
     * Tests that visitMethodHandleConstant passes both parameters correctly to referenceAccept.
     */
    @Test
    public void testVisitMethodHandleConstant_passesBothParametersCorrectly() {
        // Arrange
        ProgramClass specificClass = new ProgramClass();

        // Act
        checker.visitMethodHandleConstant(specificClass, methodHandleConstant);

        // Assert - verify that referenceAccept was called with the exact clazz and checker
        verify(methodHandleConstant).referenceAccept(same(specificClass), same(checker));
    }

    /**
     * Tests that visitMethodHandleConstant maintains correct behavior when called in a sequence
     * with other visitor methods.
     */
    @Test
    public void testVisitMethodHandleConstant_inVisitorSequence_worksCorrectly() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - should work in a sequence of visitor calls
        assertDoesNotThrow(() -> {
            checker.visitAnyClass(programClass);
            checker.visitMethodHandleConstant(programClass, methodHandleConstant);
            checker.visitAnyClass(programClass);
            checker.visitMethodHandleConstant(programClass, methodHandleConstant);
        });

        // Verify referenceAccept was called twice
        verify(methodHandleConstant, times(2)).referenceAccept(eq(programClass), any(ConstantVisitor.class));
    }

    /**
     * Tests that visitMethodHandleConstant doesn't modify the state of the clazz parameter.
     */
    @Test
    public void testVisitMethodHandleConstant_doesNotModifyClazzState() {
        // Arrange
        ProgramClass realClass = new ProgramClass();
        Object initialProcessingInfo = new Object();
        realClass.setProcessingInfo(initialProcessingInfo);

        // Act
        checker.visitMethodHandleConstant(realClass, methodHandleConstant);

        // Assert - verify the class state wasn't modified
        assertSame(initialProcessingInfo, realClass.getProcessingInfo(),
                "Class processing info should not be modified");
    }

    /**
     * Tests that visitMethodHandleConstant can handle extreme numbers of calls without issues.
     * Verifies the implementation doesn't accumulate state or resources.
     */
    @Test
    public void testVisitMethodHandleConstant_extremeNumberOfCalls_worksCorrectly() {
        // Act
        for (int i = 0; i < 1000; i++) {
            assertDoesNotThrow(() -> checker.visitMethodHandleConstant(clazz, methodHandleConstant));
        }

        // Assert - verify referenceAccept was called 1000 times
        verify(methodHandleConstant, times(1000)).referenceAccept(eq(clazz), any(ConstantVisitor.class));
    }

    /**
     * Tests that visitMethodHandleConstant works correctly with mixed real and mock objects.
     */
    @Test
    public void testVisitMethodHandleConstant_mixedRealAndMockObjects_worksCorrectly() {
        // Arrange
        ProgramClass realProgramClass = new ProgramClass();
        LibraryClass realLibraryClass = new LibraryClass();
        Clazz mockClazz = mock(Clazz.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            checker.visitMethodHandleConstant(realProgramClass, methodHandleConstant);
            checker.visitMethodHandleConstant(realLibraryClass, methodHandleConstant);
            checker.visitMethodHandleConstant(mockClazz, methodHandleConstant);
        });

        // Verify referenceAccept was called three times
        verify(methodHandleConstant, times(3)).referenceAccept(any(Clazz.class), any(ConstantVisitor.class));
    }

    /**
     * Tests that multiple checker instances can call visitMethodHandleConstant independently
     * without interfering with each other.
     */
    @Test
    public void testVisitMethodHandleConstant_multipleInstancesIndependent_worksCorrectly() {
        // Arrange
        SimpleEnumUseChecker checker1 = new SimpleEnumUseChecker();
        SimpleEnumUseChecker checker2 = new SimpleEnumUseChecker();
        MethodHandleConstant mhc1 = mock(MethodHandleConstant.class);
        MethodHandleConstant mhc2 = mock(MethodHandleConstant.class);

        // Act
        checker1.visitMethodHandleConstant(clazz, mhc1);
        checker2.visitMethodHandleConstant(clazz, mhc2);

        // Assert - verify each checker called its respective method handle constant
        verify(mhc1, times(1)).referenceAccept(eq(clazz), same(checker1));
        verify(mhc2, times(1)).referenceAccept(eq(clazz), same(checker2));
    }

    /**
     * Tests that visitMethodHandleConstant maintains the visitor pattern contract.
     * The method should always call referenceAccept regardless of the clazz parameter value.
     */
    @Test
    public void testVisitMethodHandleConstant_visitorPatternContract_alwaysCallsReferenceAccept() {
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
            assertDoesNotThrow(() -> checker.visitMethodHandleConstant(c, methodHandleConstant));
        }

        // Assert - verify referenceAccept was called for each variation
        verify(methodHandleConstant, times(differentClazzes.length))
            .referenceAccept(any(), any(ConstantVisitor.class));
    }

    /**
     * Tests that visitMethodHandleConstant behavior remains consistent after being called
     * with null clazz parameters.
     */
    @Test
    public void testVisitMethodHandleConstant_afterNullClazz_behaviorRemainsConsistent() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            checker.visitMethodHandleConstant(null, methodHandleConstant);
            checker.visitMethodHandleConstant(clazz, methodHandleConstant);
            checker.visitMethodHandleConstant(null, methodHandleConstant);
        });

        // Verify referenceAccept was called three times
        verify(methodHandleConstant, times(3)).referenceAccept(any(), any(ConstantVisitor.class));
    }

    /**
     * Tests that visitMethodHandleConstant can be called as part of a complex visitor workflow.
     */
    @Test
    public void testVisitMethodHandleConstant_inComplexWorkflow_worksCorrectly() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        MethodHandleConstant mhc1 = mock(MethodHandleConstant.class);
        MethodHandleConstant mhc2 = mock(MethodHandleConstant.class);

        // Act & Assert - simulate a complex visitor workflow
        assertDoesNotThrow(() -> {
            checker.visitAnyClass(programClass);
            checker.visitMethodHandleConstant(programClass, mhc1);
            checker.visitAnyConstant(programClass, mhc1);
            checker.visitMethodHandleConstant(programClass, mhc2);
            checker.visitAnyClass(programClass);
        });

        // Verify both method handle constants had referenceAccept called
        verify(mhc1, times(1)).referenceAccept(eq(programClass), any(ConstantVisitor.class));
        verify(mhc2, times(1)).referenceAccept(eq(programClass), any(ConstantVisitor.class));
    }

    /**
     * Tests that visitMethodHandleConstant correctly passes the checker instance to referenceAccept.
     * This is important because the checker implements ConstantVisitor and will handle
     * the reference constant processing.
     */
    @Test
    public void testVisitMethodHandleConstant_passesCheckerInstance_asConstantVisitor() {
        // Act
        checker.visitMethodHandleConstant(clazz, methodHandleConstant);

        // Assert - verify the checker itself was passed as the ConstantVisitor
        verify(methodHandleConstant).referenceAccept(
            any(Clazz.class),
            argThat(visitor -> visitor instanceof SimpleEnumUseChecker)
        );
    }

    /**
     * Tests that visitMethodHandleConstant with both null clazz is handled correctly.
     */
    @Test
    public void testVisitMethodHandleConstant_withNullClazz_passesNullToReferenceAccept() {
        // Act
        checker.visitMethodHandleConstant(null, methodHandleConstant);

        // Assert - verify null was passed to referenceAccept
        verify(methodHandleConstant).referenceAccept(isNull(), any(ConstantVisitor.class));
    }

    /**
     * Tests that visitMethodHandleConstant works correctly when alternating between
     * different checker instances and parameters.
     */
    @Test
    public void testVisitMethodHandleConstant_alternatingCheckersAndParameters_consistentBehavior() {
        // Arrange
        SimpleEnumUseChecker checker1 = new SimpleEnumUseChecker();
        SimpleEnumUseChecker checker2 = new SimpleEnumUseChecker();
        MethodHandleConstant mhc1 = mock(MethodHandleConstant.class);
        MethodHandleConstant mhc2 = mock(MethodHandleConstant.class);
        Clazz clazz2 = mock(ProgramClass.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            checker1.visitMethodHandleConstant(clazz, mhc1);
            checker2.visitMethodHandleConstant(clazz2, mhc2);
            checker1.visitMethodHandleConstant(clazz2, mhc2);
            checker2.visitMethodHandleConstant(clazz, mhc1);
        });

        // Verify each constant was called twice
        verify(mhc1, times(2)).referenceAccept(any(Clazz.class), any(ConstantVisitor.class));
        verify(mhc2, times(2)).referenceAccept(any(Clazz.class), any(ConstantVisitor.class));
    }

    /**
     * Tests that visitMethodHandleConstant preserves the specific clazz instance when passing to referenceAccept.
     */
    @Test
    public void testVisitMethodHandleConstant_preservesClazzInstance() {
        // Arrange
        ProgramClass specificClass1 = new ProgramClass();
        ProgramClass specificClass2 = new ProgramClass();

        // Act
        checker.visitMethodHandleConstant(specificClass1, methodHandleConstant);
        checker.visitMethodHandleConstant(specificClass2, methodHandleConstant);

        // Assert - verify the correct clazz instances were passed
        verify(methodHandleConstant).referenceAccept(same(specificClass1), any(ConstantVisitor.class));
        verify(methodHandleConstant).referenceAccept(same(specificClass2), any(ConstantVisitor.class));
    }

    /**
     * Tests that visitMethodHandleConstant works correctly in combination with visitStringConstant.
     */
    @Test
    public void testVisitMethodHandleConstant_withOtherConstantVisitorMethods_worksCorrectly() {
        // Arrange
        proguard.classfile.constant.StringConstant stringConstant =
            mock(proguard.classfile.constant.StringConstant.class);

        // Act & Assert - should work with other constant visitor methods
        assertDoesNotThrow(() -> {
            checker.visitMethodHandleConstant(clazz, methodHandleConstant);
            checker.visitStringConstant(clazz, stringConstant);
            checker.visitMethodHandleConstant(clazz, methodHandleConstant);
        });

        // Verify both constants were visited
        verify(methodHandleConstant, times(2)).referenceAccept(eq(clazz), any(ConstantVisitor.class));
        verify(stringConstant, times(1)).referencedClassAccept(any());
    }
}
