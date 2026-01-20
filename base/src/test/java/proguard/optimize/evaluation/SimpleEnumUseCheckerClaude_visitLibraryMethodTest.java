package proguard.optimize.evaluation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryMethod;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link SimpleEnumUseChecker#visitLibraryMethod(LibraryClass, LibraryMethod)}.
 *
 * The visitLibraryMethod method is an empty implementation (no-op) that serves as a default
 * handler in the MemberVisitor pattern for library methods that don't require specialized processing.
 * The actual processing logic is in the visitProgramMethod method which handles program methods
 * for simple enum checking.
 */
public class SimpleEnumUseCheckerClaude_visitLibraryMethodTest {

    private SimpleEnumUseChecker checker;
    private LibraryClass libraryClass;
    private LibraryMethod libraryMethod;

    @BeforeEach
    public void setUp() {
        checker = new SimpleEnumUseChecker();
        libraryClass = mock(LibraryClass.class);
        libraryMethod = mock(LibraryMethod.class);
    }

    /**
     * Tests that visitLibraryMethod can be called with valid mock objects without throwing exceptions.
     * Since this is a no-op method, it should simply do nothing and complete successfully.
     */
    @Test
    public void testVisitLibraryMethod_withValidMocks_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.visitLibraryMethod(libraryClass, libraryMethod));
    }

    /**
     * Tests that visitLibraryMethod can be called with null LibraryClass parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitLibraryMethod_withNullLibraryClass_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.visitLibraryMethod(null, libraryMethod));
    }

    /**
     * Tests that visitLibraryMethod can be called with null LibraryMethod parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitLibraryMethod_withNullLibraryMethod_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.visitLibraryMethod(libraryClass, null));
    }

    /**
     * Tests that visitLibraryMethod can be called with both parameters null.
     * The method should handle all nulls gracefully since it's a no-op.
     */
    @Test
    public void testVisitLibraryMethod_withBothParametersNull_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.visitLibraryMethod(null, null));
    }

    /**
     * Tests that visitLibraryMethod can be called multiple times in succession.
     * The method should be idempotent and handle repeated calls without issues.
     */
    @Test
    public void testVisitLibraryMethod_calledMultipleTimes_doesNotThrowException() {
        // Act & Assert - should not throw any exception on multiple calls
        assertDoesNotThrow(() -> {
            checker.visitLibraryMethod(libraryClass, libraryMethod);
            checker.visitLibraryMethod(libraryClass, libraryMethod);
            checker.visitLibraryMethod(libraryClass, libraryMethod);
        });
    }

    /**
     * Tests that visitLibraryMethod doesn't interact with any of the parameters.
     * Since it's a no-op method, it should not call any methods on the parameters.
     */
    @Test
    public void testVisitLibraryMethod_doesNotInteractWithParameters() {
        // Act
        checker.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert - verify no interactions occurred with any of the mocks
        verifyNoInteractions(libraryClass);
        verifyNoInteractions(libraryMethod);
    }

    /**
     * Tests that visitLibraryMethod can be called rapidly in succession.
     * Verifies consistent behavior under stress.
     */
    @Test
    public void testVisitLibraryMethod_rapidSequentialCalls_doesNotThrowException() {
        // Act & Assert - all calls should succeed without exceptions
        for (int i = 0; i < 1000; i++) {
            assertDoesNotThrow(() -> checker.visitLibraryMethod(libraryClass, libraryMethod),
                    "Call " + i + " should not throw exception");
        }
    }

    /**
     * Tests that multiple SimpleEnumUseChecker instances can all call visitLibraryMethod
     * on the same parameters without interference.
     */
    @Test
    public void testVisitLibraryMethod_multipleCheckersOneParameter_allWorkCorrectly() {
        // Arrange
        SimpleEnumUseChecker checker1 = new SimpleEnumUseChecker();
        SimpleEnumUseChecker checker2 = new SimpleEnumUseChecker();
        SimpleEnumUseChecker checker3 = new SimpleEnumUseChecker();

        // Act & Assert
        assertDoesNotThrow(() -> {
            checker1.visitLibraryMethod(libraryClass, libraryMethod);
            checker2.visitLibraryMethod(libraryClass, libraryMethod);
            checker3.visitLibraryMethod(libraryClass, libraryMethod);
        });

        // Verify no interactions from any of the calls
        verifyNoInteractions(libraryClass, libraryMethod);
    }

    /**
     * Tests that visitLibraryMethod works with real LibraryClass and LibraryMethod instances.
     * Verifies the method works with actual instances, not just mocks.
     */
    @Test
    public void testVisitLibraryMethod_withRealInstances_doesNotThrowException() {
        // Arrange
        LibraryClass realLibraryClass = new LibraryClass();
        LibraryMethod realLibraryMethod = new LibraryMethod();

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitLibraryMethod(realLibraryClass, realLibraryMethod));
    }

    /**
     * Tests that visitLibraryMethod is truly a no-op by verifying no exceptions
     * even with parameters that would normally cause issues if accessed.
     */
    @Test
    public void testVisitLibraryMethod_isNoop_noExceptionsWithAnyInput() {
        // Act & Assert - should not throw even with unusual combinations
        assertDoesNotThrow(() -> {
            checker.visitLibraryMethod(null, null);
            checker.visitLibraryMethod(libraryClass, libraryMethod);
            checker.visitLibraryMethod(libraryClass, null);
            checker.visitLibraryMethod(null, libraryMethod);
        });
    }

    /**
     * Tests that visitLibraryMethod can be used as part of the MemberVisitor interface.
     * Verifies integration with the visitor pattern.
     */
    @Test
    public void testVisitLibraryMethod_usedAsMemberVisitor_worksCorrectly() {
        // Arrange
        proguard.classfile.visitor.MemberVisitor visitor = checker;

        // Act & Assert
        assertDoesNotThrow(() -> visitor.visitLibraryMethod(libraryClass, libraryMethod));
        verifyNoInteractions(libraryClass, libraryMethod);
    }

    /**
     * Tests that visitLibraryMethod maintains thread-safe behavior as a no-op.
     * Verifies the method can be called rapidly without issues.
     */
    @Test
    public void testVisitLibraryMethod_rapidCalls_doesNotThrowException() {
        // Act & Assert - rapid calls should all succeed
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                checker.visitLibraryMethod(libraryClass, libraryMethod);
            }
        });
    }

    /**
     * Tests that visitLibraryMethod has no effect on the SimpleEnumUseChecker's internal state.
     * Verifies that subsequent calls to visitLibraryMethod behave identically.
     */
    @Test
    public void testVisitLibraryMethod_repeatedCalls_behaviorRemainsConsistent() {
        // Act & Assert - multiple calls should have identical behavior
        assertDoesNotThrow(() -> checker.visitLibraryMethod(libraryClass, libraryMethod));
        assertDoesNotThrow(() -> checker.visitLibraryMethod(libraryClass, libraryMethod));
        assertDoesNotThrow(() -> checker.visitLibraryMethod(libraryClass, libraryMethod));

        // Verify no interactions still occur after multiple calls
        verifyNoInteractions(libraryClass, libraryMethod);
    }

    /**
     * Tests that visitLibraryMethod can be interleaved with null and non-null parameters.
     * Verifies that the no-op method doesn't interfere with normal operation.
     */
    @Test
    public void testVisitLibraryMethod_interleavedCalls_doesNotThrowException() {
        // Act & Assert - interleaved calls should all succeed
        assertDoesNotThrow(() -> {
            checker.visitLibraryMethod(libraryClass, libraryMethod);
            checker.visitLibraryMethod(null, null);
            checker.visitLibraryMethod(libraryClass, libraryMethod);
            checker.visitLibraryMethod(null, libraryMethod);
        });
    }

    /**
     * Tests that visitLibraryMethod works correctly when called with the same parameters repeatedly.
     * Verifies stable behavior with parameter reuse.
     */
    @Test
    public void testVisitLibraryMethod_sameParametersRepeatedCalls_consistentBehavior() {
        // Act & Assert - multiple calls with same parameters should all succeed
        for (int i = 0; i < 50; i++) {
            assertDoesNotThrow(() -> checker.visitLibraryMethod(libraryClass, libraryMethod),
                    "Repeated call " + i + " with same parameters should not throw");
        }

        // Verify still no interactions after many calls
        verifyNoInteractions(libraryClass, libraryMethod);
    }

    /**
     * Tests that visitLibraryMethod preserves the checker's ability to work with other visitor methods.
     * Verifies that calling visitLibraryMethod doesn't affect the checker's state for other operations.
     */
    @Test
    public void testVisitLibraryMethod_doesNotAffectOtherVisitorMethods() {
        // Arrange
        proguard.classfile.ProgramClass programClass = new proguard.classfile.ProgramClass();

        // Act - call visitLibraryMethod multiple times
        checker.visitLibraryMethod(libraryClass, libraryMethod);
        checker.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert - other visitor methods should still work
        assertDoesNotThrow(() -> checker.visitAnyClass(programClass),
                "Other visitor methods should still work after visitLibraryMethod");
        assertDoesNotThrow(() -> checker.visitLibraryMethod(libraryClass, libraryMethod),
                "visitLibraryMethod should still work after multiple calls");
    }

    /**
     * Tests that visitLibraryMethod works correctly in a sequence of different visitor method calls.
     * Verifies the no-op doesn't affect other visitor patterns.
     */
    @Test
    public void testVisitLibraryMethod_inVisitorSequence_doesNotThrowException() {
        // Arrange
        proguard.classfile.ProgramClass realClass = new proguard.classfile.ProgramClass();

        // Act & Assert - should work in a sequence of visitor calls
        assertDoesNotThrow(() -> {
            checker.visitAnyClass(realClass);
            checker.visitLibraryMethod(libraryClass, libraryMethod);
            checker.visitAnyClass(realClass);
            checker.visitLibraryMethod(libraryClass, libraryMethod);
        });
    }

    /**
     * Tests that visitLibraryMethod maintains expected behavior across varied call patterns.
     * Verifies consistency regardless of call order or parameter variation.
     */
    @Test
    public void testVisitLibraryMethod_variedCallPatterns_allWorkCorrectly() {
        // Arrange
        LibraryClass libraryClass2 = mock(LibraryClass.class);
        LibraryMethod libraryMethod2 = mock(LibraryMethod.class);
        LibraryClass realLibraryClass = new LibraryClass();
        LibraryMethod realLibraryMethod = new LibraryMethod();

        // Act & Assert - various call patterns should all work
        assertDoesNotThrow(() -> {
            checker.visitLibraryMethod(libraryClass, libraryMethod);
            checker.visitLibraryMethod(libraryClass2, libraryMethod2);
            checker.visitLibraryMethod(realLibraryClass, realLibraryMethod);
            checker.visitLibraryMethod(null, null);
            checker.visitLibraryMethod(libraryClass, libraryMethod);
        });
    }

    /**
     * Tests that visitLibraryMethod doesn't retain references to any parameters.
     * Verifies no memory leaks or reference retention.
     */
    @Test
    public void testVisitLibraryMethod_doesNotRetainReferences() {
        // Arrange
        LibraryMethod tempMethod = mock(LibraryMethod.class);

        // Act
        checker.visitLibraryMethod(libraryClass, tempMethod);

        // Assert - no interactions means no references should be retained
        verifyNoInteractions(tempMethod);
    }

    /**
     * Tests that visitLibraryMethod doesn't call any methods on any parameter when given mocks.
     * Confirms complete isolation and no-op behavior.
     */
    @Test
    public void testVisitLibraryMethod_withStrictMocks_noMethodsCalled() {
        // Arrange - create strict mocks
        LibraryClass mockLibraryClass = mock(LibraryClass.class,
            withSettings().strictness(org.mockito.quality.Strictness.STRICT_STUBS));
        LibraryMethod mockLibraryMethod = mock(LibraryMethod.class,
            withSettings().strictness(org.mockito.quality.Strictness.STRICT_STUBS));

        // Act
        checker.visitLibraryMethod(mockLibraryClass, mockLibraryMethod);

        // Assert - verify absolutely no interactions
        verifyNoInteractions(mockLibraryClass, mockLibraryMethod);
    }

    /**
     * Tests that the checker created with a custom PartialEvaluator also has a no-op visitLibraryMethod.
     * Verifies consistency across different constructor configurations.
     */
    @Test
    public void testVisitLibraryMethod_withCustomPartialEvaluator_doesNotThrowException() {
        // Arrange
        proguard.evaluation.PartialEvaluator evaluator =
            proguard.evaluation.PartialEvaluator.Builder.create().build();
        SimpleEnumUseChecker customChecker = new SimpleEnumUseChecker(evaluator);

        // Act & Assert
        assertDoesNotThrow(() -> {
            customChecker.visitLibraryMethod(libraryClass, libraryMethod);
            customChecker.visitLibraryMethod(null, null);
        });

        verifyNoInteractions(libraryClass, libraryMethod);
    }

    /**
     * Tests that visitLibraryMethod can be called an extreme number of times without issues.
     * Verifies the no-op implementation doesn't accumulate any state or resources.
     */
    @Test
    public void testVisitLibraryMethod_extremeNumberOfCalls_doesNotThrowException() {
        // Act & Assert - should handle many calls without issues
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 10000; i++) {
                checker.visitLibraryMethod(libraryClass, libraryMethod);
            }
        });

        // After many calls, still no interactions
        verifyNoInteractions(libraryClass, libraryMethod);
    }

    /**
     * Tests that visitLibraryMethod through MemberVisitor interface works with casting.
     * Verifies proper interface implementation.
     */
    @Test
    public void testVisitLibraryMethod_throughCastedInterface_doesNotThrowException() {
        // Arrange
        proguard.classfile.visitor.MemberVisitor visitor =
            (proguard.classfile.visitor.MemberVisitor) checker;

        // Act & Assert
        assertDoesNotThrow(() -> {
            visitor.visitLibraryMethod(libraryClass, libraryMethod);
            visitor.visitLibraryMethod(null, null);
        });
    }

    /**
     * Tests that visitLibraryMethod works with different combinations of null and non-null parameters.
     * Verifies robust handling of various parameter states.
     */
    @Test
    public void testVisitLibraryMethod_mixedNullAndNonNull_doesNotThrowException() {
        // Act & Assert - should handle all combinations gracefully
        assertDoesNotThrow(() -> {
            checker.visitLibraryMethod(libraryClass, null);
            checker.visitLibraryMethod(null, libraryMethod);
            checker.visitLibraryMethod(null, null);
            checker.visitLibraryMethod(libraryClass, libraryMethod);
        });
    }

    /**
     * Tests that multiple checker instances can call visitLibraryMethod independently
     * without interfering with each other.
     */
    @Test
    public void testVisitLibraryMethod_multipleInstancesIndependent_worksCorrectly() {
        // Arrange
        SimpleEnumUseChecker checker1 = new SimpleEnumUseChecker();
        SimpleEnumUseChecker checker2 = new SimpleEnumUseChecker();
        LibraryMethod method1 = mock(LibraryMethod.class);
        LibraryMethod method2 = mock(LibraryMethod.class);

        // Act
        checker1.visitLibraryMethod(libraryClass, method1);
        checker2.visitLibraryMethod(libraryClass, method2);

        // Assert - verify each checker called with respective methods
        verifyNoInteractions(method1, method2);
    }

    /**
     * Tests that visitLibraryMethod maintains the visitor pattern contract.
     * The method should always complete successfully regardless of parameters.
     */
    @Test
    public void testVisitLibraryMethod_visitorPatternContract_alwaysSucceeds() {
        // Arrange
        Object[][] parameterVariations = {
            {libraryClass, libraryMethod},
            {null, null},
            {libraryClass, null},
            {null, libraryMethod},
            {new LibraryClass(), new LibraryMethod()},
        };

        // Act - call with different parameter combinations
        for (Object[] params : parameterVariations) {
            assertDoesNotThrow(() -> checker.visitLibraryMethod(
                (LibraryClass) params[0],
                (LibraryMethod) params[1]
            ));
        }
    }

    /**
     * Tests that visitLibraryMethod behavior remains consistent after being called
     * with null parameters.
     */
    @Test
    public void testVisitLibraryMethod_afterNullParameters_behaviorRemainsConsistent() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            checker.visitLibraryMethod(null, null);
            checker.visitLibraryMethod(libraryClass, libraryMethod);
            checker.visitLibraryMethod(null, null);
        });

        // Verify no interactions occurred
        verifyNoInteractions(libraryClass, libraryMethod);
    }

    /**
     * Tests that visitLibraryMethod can be called as part of a complex visitor workflow.
     */
    @Test
    public void testVisitLibraryMethod_inComplexWorkflow_worksCorrectly() {
        // Arrange
        proguard.classfile.ProgramClass programClass = new proguard.classfile.ProgramClass();
        LibraryMethod method1 = mock(LibraryMethod.class);
        LibraryMethod method2 = mock(LibraryMethod.class);

        // Act & Assert - simulate a complex visitor workflow
        assertDoesNotThrow(() -> {
            checker.visitAnyClass(programClass);
            checker.visitLibraryMethod(libraryClass, method1);
            checker.visitAnyClass(programClass);
            checker.visitLibraryMethod(libraryClass, method2);
            checker.visitAnyClass(programClass);
        });

        // Verify no interactions with methods
        verifyNoInteractions(method1, method2);
    }

    /**
     * Tests that visitLibraryMethod works with different LibraryClass instances.
     * Verifies the method can handle multiple different library classes.
     */
    @Test
    public void testVisitLibraryMethod_withDifferentLibraryClasses_worksCorrectly() {
        // Arrange
        LibraryClass libraryClass1 = mock(LibraryClass.class);
        LibraryClass libraryClass2 = mock(LibraryClass.class);
        LibraryClass libraryClass3 = new LibraryClass();

        // Act
        checker.visitLibraryMethod(libraryClass1, libraryMethod);
        checker.visitLibraryMethod(libraryClass2, libraryMethod);
        checker.visitLibraryMethod(libraryClass3, libraryMethod);

        // Assert - verify no interactions with any library class
        verifyNoInteractions(libraryClass1, libraryClass2);
    }

    /**
     * Tests that visitLibraryMethod works with different LibraryMethod instances.
     * Verifies the method can handle multiple different library methods.
     */
    @Test
    public void testVisitLibraryMethod_withDifferentLibraryMethods_worksCorrectly() {
        // Arrange
        LibraryMethod method1 = mock(LibraryMethod.class);
        LibraryMethod method2 = mock(LibraryMethod.class);
        LibraryMethod method3 = new LibraryMethod();

        // Act
        checker.visitLibraryMethod(libraryClass, method1);
        checker.visitLibraryMethod(libraryClass, method2);
        checker.visitLibraryMethod(libraryClass, method3);

        // Assert - verify no interactions with any method
        verifyNoInteractions(method1, method2);
    }

    /**
     * Tests that visitLibraryMethod is a proper no-op that doesn't access any properties.
     * Even uninitialized or null parameters should work fine.
     */
    @Test
    public void testVisitLibraryMethod_asProperNoop_worksWithAnyParameters() {
        // Act & Assert - should work as a proper no-op
        assertDoesNotThrow(() -> {
            checker.visitLibraryMethod(null, null);
            checker.visitLibraryMethod(libraryClass, libraryMethod);
            checker.visitLibraryMethod(libraryClass, null);
            checker.visitLibraryMethod(null, libraryMethod);
            checker.visitLibraryMethod(new LibraryClass(), new LibraryMethod());
        });
    }

    /**
     * Tests that visitLibraryMethod doesn't modify the state of the LibraryClass parameter.
     */
    @Test
    public void testVisitLibraryMethod_doesNotModifyLibraryClassState() {
        // Arrange
        LibraryClass realClass = new LibraryClass();
        Object initialProcessingInfo = new Object();
        realClass.setProcessingInfo(initialProcessingInfo);

        // Act
        checker.visitLibraryMethod(realClass, libraryMethod);

        // Assert - verify the class state wasn't modified
        assertSame(initialProcessingInfo, realClass.getProcessingInfo(),
                "LibraryClass processing info should not be modified");
    }

    /**
     * Tests that visitLibraryMethod doesn't modify the state of the LibraryMethod parameter.
     */
    @Test
    public void testVisitLibraryMethod_doesNotModifyLibraryMethodState() {
        // Arrange
        LibraryMethod realMethod = new LibraryMethod();
        Object initialProcessingInfo = new Object();
        realMethod.setProcessingInfo(initialProcessingInfo);

        // Act
        checker.visitLibraryMethod(libraryClass, realMethod);

        // Assert - verify the method state wasn't modified
        assertSame(initialProcessingInfo, realMethod.getProcessingInfo(),
                "LibraryMethod processing info should not be modified");
    }

    /**
     * Tests that visitLibraryMethod works correctly with freshly created instances.
     * Verifies no initialization issues affect the no-op behavior.
     */
    @Test
    public void testVisitLibraryMethod_withFreshInstances_doesNotThrowException() {
        // Arrange
        LibraryClass freshClass = new LibraryClass();
        LibraryMethod freshMethod = new LibraryMethod();

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitLibraryMethod(freshClass, freshMethod));
        assertDoesNotThrow(() -> checker.visitLibraryMethod(freshClass, freshMethod));
    }

    /**
     * Tests that visitLibraryMethod can handle alternating null and non-null parameters.
     * Verifies robustness of the no-op implementation.
     */
    @Test
    public void testVisitLibraryMethod_alternatingNullAndNonNull_doesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            checker.visitLibraryMethod(libraryClass, libraryMethod);
            checker.visitLibraryMethod(null, null);
            checker.visitLibraryMethod(libraryClass, libraryMethod);
            checker.visitLibraryMethod(null, null);
            checker.visitLibraryMethod(libraryClass, libraryMethod);
        });
    }

    /**
     * Tests that visitLibraryMethod works correctly when called with mixed real and mock objects.
     */
    @Test
    public void testVisitLibraryMethod_mixedRealAndMockObjects_worksCorrectly() {
        // Arrange
        LibraryClass realLibraryClass = new LibraryClass();
        LibraryMethod realLibraryMethod = new LibraryMethod();
        LibraryClass mockLibraryClass = mock(LibraryClass.class);
        LibraryMethod mockLibraryMethod = mock(LibraryMethod.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            checker.visitLibraryMethod(realLibraryClass, realLibraryMethod);
            checker.visitLibraryMethod(mockLibraryClass, mockLibraryMethod);
            checker.visitLibraryMethod(realLibraryClass, mockLibraryMethod);
            checker.visitLibraryMethod(mockLibraryClass, realLibraryMethod);
        });

        // Verify no interactions with mocks
        verifyNoInteractions(mockLibraryClass, mockLibraryMethod);
    }

    /**
     * Tests that visitLibraryMethod maintains consistent behavior across multiple calls
     * with the same parameters.
     */
    @Test
    public void testVisitLibraryMethod_repeatedCallsSameParameters_consistentBehavior() {
        // Act & Assert - multiple calls should have identical behavior
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> checker.visitLibraryMethod(libraryClass, libraryMethod));
        }

        // Verify no interactions still occur
        verifyNoInteractions(libraryClass, libraryMethod);
    }

    /**
     * Tests that visitLibraryMethod can be called through the MemberVisitor interface
     * with various parameter combinations.
     */
    @Test
    public void testVisitLibraryMethod_throughInterface_worksWithVariousParameters() {
        // Arrange
        proguard.classfile.visitor.MemberVisitor visitor = checker;
        LibraryClass realLibraryClass = new LibraryClass();
        LibraryMethod realLibraryMethod = new LibraryMethod();

        // Act & Assert
        assertDoesNotThrow(() -> {
            visitor.visitLibraryMethod(realLibraryClass, realLibraryMethod);
            visitor.visitLibraryMethod(libraryClass, libraryMethod);
            visitor.visitLibraryMethod(null, null);
        });

        // Verify no interactions with mocks
        verifyNoInteractions(libraryClass, libraryMethod);
    }

    /**
     * Tests that visitLibraryMethod can handle interleaved calls with different parameters.
     * Verifies robustness with varied call patterns.
     */
    @Test
    public void testVisitLibraryMethod_interleavedCallsDifferentParameters_worksCorrectly() {
        // Arrange
        LibraryClass class1 = mock(LibraryClass.class);
        LibraryClass class2 = mock(LibraryClass.class);
        LibraryMethod method1 = mock(LibraryMethod.class);
        LibraryMethod method2 = mock(LibraryMethod.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            checker.visitLibraryMethod(class1, method1);
            checker.visitLibraryMethod(class2, method2);
            checker.visitLibraryMethod(class1, method2);
            checker.visitLibraryMethod(class2, method1);
        });

        // Verify no interactions
        verifyNoInteractions(class1, class2, method1, method2);
    }
}
