package proguard.optimize.peephole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.LibraryClass;
import proguard.classfile.attribute.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link VariableShrinker#visitAnyAttribute(Clazz, Attribute)}.
 *
 * The visitAnyAttribute method is an empty implementation (no-op) that serves as a default
 * handler in the AttributeVisitor pattern. The VariableShrinker only processes
 * CodeAttribute through the specialized visitCodeAttribute method.
 * This method provides the default no-op behavior for all other attribute types.
 *
 * Note: Mocking is used here because visitAnyAttribute is a no-op method with an empty body.
 * There is no meaningful behavior to test without mocking - we can only verify it doesn't throw
 * exceptions and doesn't interact with its parameters.
 */
public class VariableShrinkerClaude_visitAnyAttributeTest {

    private VariableShrinker shrinker;
    private Clazz clazz;
    private Attribute attribute;

    @BeforeEach
    public void setUp() {
        shrinker = new VariableShrinker();
        clazz = mock(ProgramClass.class);
        attribute = mock(Attribute.class);
    }

    /**
     * Tests that visitAnyAttribute can be called with valid mock objects without throwing exceptions.
     * Since this is a no-op method, it should simply do nothing and complete successfully.
     */
    @Test
    public void testVisitAnyAttribute_withValidMocks_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> shrinker.visitAnyAttribute(clazz, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called with null Clazz parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyAttribute_withNullClazz_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> shrinker.visitAnyAttribute(null, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called with null Attribute parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyAttribute_withNullAttribute_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> shrinker.visitAnyAttribute(clazz, null));
    }

    /**
     * Tests that visitAnyAttribute can be called with both parameters null.
     * The method should handle null parameters gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyAttribute_withBothParametersNull_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> shrinker.visitAnyAttribute(null, null));
    }

    /**
     * Tests that visitAnyAttribute can be called multiple times in succession.
     * The method should be idempotent and handle repeated calls without issues.
     */
    @Test
    public void testVisitAnyAttribute_calledMultipleTimes_doesNotThrowException() {
        // Act & Assert - should not throw any exception on multiple calls
        assertDoesNotThrow(() -> {
            shrinker.visitAnyAttribute(clazz, attribute);
            shrinker.visitAnyAttribute(clazz, attribute);
            shrinker.visitAnyAttribute(clazz, attribute);
        });
    }

    /**
     * Tests that visitAnyAttribute doesn't interact with the Clazz parameter.
     * Since it's a no-op method, it should not call any methods on the clazz.
     */
    @Test
    public void testVisitAnyAttribute_doesNotInteractWithClazz() {
        // Act
        shrinker.visitAnyAttribute(clazz, attribute);

        // Assert - verify no interactions occurred with the clazz mock
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitAnyAttribute doesn't interact with the Attribute parameter.
     * Since it's a no-op method, it should not call any methods on the attribute.
     */
    @Test
    public void testVisitAnyAttribute_doesNotInteractWithAttribute() {
        // Act
        shrinker.visitAnyAttribute(clazz, attribute);

        // Assert - verify no interactions occurred with the attribute mock
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute works with different Clazz implementations.
     */
    @Test
    public void testVisitAnyAttribute_withDifferentClazzTypes_doesNotThrowException() {
        // Arrange
        Clazz programClass = mock(ProgramClass.class);
        Clazz libraryClass = mock(LibraryClass.class);

        // Act & Assert - should not throw any exception with different clazz types
        assertDoesNotThrow(() -> {
            shrinker.visitAnyAttribute(programClass, attribute);
            shrinker.visitAnyAttribute(libraryClass, attribute);
        });
    }

    /**
     * Tests that visitAnyAttribute works with different Attribute implementations.
     */
    @Test
    public void testVisitAnyAttribute_withDifferentAttributeTypes_doesNotThrowException() {
        // Arrange - create mocks of various attribute types
        Attribute codeAttr = mock(CodeAttribute.class);
        Attribute sigAttr = mock(SignatureAttribute.class);
        Attribute sourceFileAttr = mock(SourceFileAttribute.class);
        Attribute innerClassesAttr = mock(InnerClassesAttribute.class);
        Attribute deprecatedAttr = mock(DeprecatedAttribute.class);

        // Act & Assert - should not throw any exception with different attribute types
        assertDoesNotThrow(() -> {
            shrinker.visitAnyAttribute(clazz, codeAttr);
            shrinker.visitAnyAttribute(clazz, sigAttr);
            shrinker.visitAnyAttribute(clazz, sourceFileAttr);
            shrinker.visitAnyAttribute(clazz, innerClassesAttr);
            shrinker.visitAnyAttribute(clazz, deprecatedAttr);
        });
    }

    /**
     * Tests that visitAnyAttribute execution completes immediately.
     * Since it's a no-op method, it should have minimal overhead.
     */
    @Test
    public void testVisitAnyAttribute_executesQuickly() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            shrinker.visitAnyAttribute(clazz, attribute);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100, "visitAnyAttribute should execute quickly as it's a no-op");
    }

    /**
     * Tests that visitAnyAttribute doesn't affect subsequent calls.
     * The no-op should not interfere with the shrinker's normal operation.
     */
    @Test
    public void testVisitAnyAttribute_doesNotAffectSubsequentOperations() {
        // Act - call visitAnyAttribute first
        shrinker.visitAnyAttribute(clazz, attribute);

        // Then call visitAnyAttribute again
        shrinker.visitAnyAttribute(clazz, attribute);

        // Assert - verify no side effects occurred
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute can be called with the same parameters repeatedly
     * without accumulating any state or causing issues.
     */
    @Test
    public void testVisitAnyAttribute_repeatedCallsWithSameParameters_noStateAccumulation() {
        // Act - call multiple times with same parameters
        for (int i = 0; i < 10; i++) {
            shrinker.visitAnyAttribute(clazz, attribute);
        }

        // Assert - verify no interactions occurred despite multiple calls
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute is thread-safe when called concurrently.
     * Since it's a no-op with no state changes, it should handle concurrent calls.
     */
    @Test
    public void testVisitAnyAttribute_concurrentCalls_noExceptions() throws InterruptedException {
        // Arrange
        final int threadCount = 10;
        Thread[] threads = new Thread[threadCount];

        // Act - create multiple threads that call visitAnyAttribute
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    shrinker.visitAnyAttribute(clazz, attribute);
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
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that multiple shrinkers can call visitAnyAttribute independently.
     * Each shrinker's no-op should not affect others.
     */
    @Test
    public void testVisitAnyAttribute_withMultipleShrinkers_operateIndependently() {
        // Arrange - create multiple shrinkers
        VariableShrinker shrinker1 = new VariableShrinker();
        VariableShrinker shrinker2 = new VariableShrinker();

        // Act - call visitAnyAttribute on both shrinkers
        shrinker1.visitAnyAttribute(clazz, attribute);
        shrinker2.visitAnyAttribute(clazz, attribute);

        // Assert - verify no interactions occurred
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute works with different Clazz mock instances.
     */
    @Test
    public void testVisitAnyAttribute_withDifferentClazzInstances_doesNotThrowException() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Clazz clazz3 = mock(ProgramClass.class);

        // Act & Assert - should not throw any exception with different instances
        assertDoesNotThrow(() -> {
            shrinker.visitAnyAttribute(clazz1, attribute);
            shrinker.visitAnyAttribute(clazz2, attribute);
            shrinker.visitAnyAttribute(clazz3, attribute);
        });

        // Verify no interactions
        verifyNoInteractions(clazz1);
        verifyNoInteractions(clazz2);
        verifyNoInteractions(clazz3);
    }

    /**
     * Tests that visitAnyAttribute works with different Attribute mock instances.
     */
    @Test
    public void testVisitAnyAttribute_withDifferentAttributeInstances_doesNotThrowException() {
        // Arrange
        Attribute attr1 = mock(Attribute.class);
        Attribute attr2 = mock(Attribute.class);
        Attribute attr3 = mock(Attribute.class);

        // Act & Assert - should not throw any exception with different instances
        assertDoesNotThrow(() -> {
            shrinker.visitAnyAttribute(clazz, attr1);
            shrinker.visitAnyAttribute(clazz, attr2);
            shrinker.visitAnyAttribute(clazz, attr3);
        });

        // Verify no interactions
        verifyNoInteractions(attr1);
        verifyNoInteractions(attr2);
        verifyNoInteractions(attr3);
    }

    /**
     * Tests that visitAnyAttribute completes without blocking or hanging.
     * This ensures the method doesn't have any unexpected wait conditions.
     */
    @Test
    public void testVisitAnyAttribute_completesImmediately() {
        // Arrange
        long timeoutNanos = 1_000_000; // 1 millisecond
        long startTime = System.nanoTime();

        // Act
        shrinker.visitAnyAttribute(clazz, attribute);

        long duration = System.nanoTime() - startTime;

        // Assert - should complete within the timeout
        assertTrue(duration < timeoutNanos,
                "visitAnyAttribute should complete immediately, took " + duration + " nanoseconds");
    }

    /**
     * Tests that visitAnyAttribute called after multiple other operations still behaves as a no-op.
     * This verifies consistent behavior regardless of the shrinker's usage history.
     */
    @Test
    public void testVisitAnyAttribute_afterMultipleOperations_stillNoOp() {
        // Arrange - simulate some prior operations
        for (int i = 0; i < 5; i++) {
            shrinker.visitAnyAttribute(clazz, attribute);
        }

        // Act - call visitAnyAttribute again
        shrinker.visitAnyAttribute(clazz, attribute);

        // Assert - verify it's still a no-op with no interactions
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute doesn't affect the shrinker's internal state.
     * Since it's a no-op, it should not modify any internal fields.
     */
    @Test
    public void testVisitAnyAttribute_doesNotAffectInternalState() {
        // Act - call visitAnyAttribute multiple times
        shrinker.visitAnyAttribute(clazz, attribute);
        shrinker.visitAnyAttribute(clazz, attribute);

        // Assert - verify no interactions occurred, indicating no state changes
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute with varying combinations of parameters
     * all result in the same no-op behavior.
     */
    @Test
    public void testVisitAnyAttribute_varyingParameterCombinations_consistentNoOpBehavior() {
        // Arrange
        Clazz clazz2 = mock(ProgramClass.class);
        Attribute attr2 = mock(CodeAttribute.class);

        // Act - call with various parameter combinations
        shrinker.visitAnyAttribute(clazz, attribute);
        shrinker.visitAnyAttribute(clazz2, attr2);
        shrinker.visitAnyAttribute(clazz, attr2);

        // Assert - verify no interactions occurred with any parameters
        verifyNoInteractions(clazz);
        verifyNoInteractions(clazz2);
        verifyNoInteractions(attribute);
        verifyNoInteractions(attr2);
    }

    /**
     * Tests that the visitAnyAttribute method signature matches the AttributeVisitor interface.
     * This ensures the method properly overrides the interface method.
     */
    @Test
    public void testVisitAnyAttribute_implementsInterfaceCorrectly() {
        // Assert - VariableShrinker should be an AttributeVisitor
        assertTrue(shrinker instanceof proguard.classfile.attribute.visitor.AttributeVisitor,
                "VariableShrinker should implement AttributeVisitor");
    }

    /**
     * Tests that visitAnyAttribute can be called in rapid succession without issues.
     * This verifies there's no timing-dependent behavior.
     */
    @Test
    public void testVisitAnyAttribute_rapidSuccession_noIssues() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 1000; i++) {
                shrinker.visitAnyAttribute(clazz, attribute);
            }
        }, "Rapid successive calls should not cause issues");

        // Verify no interactions occurred
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute with mixed null and non-null parameters works correctly.
     * This ensures the method handles partial null inputs gracefully.
     */
    @Test
    public void testVisitAnyAttribute_withMixedNullParameters_doesNotThrowException() {
        // Act & Assert - test various combinations of null/non-null parameters
        assertDoesNotThrow(() -> shrinker.visitAnyAttribute(null, attribute));
        assertDoesNotThrow(() -> shrinker.visitAnyAttribute(clazz, null));
        assertDoesNotThrow(() -> shrinker.visitAnyAttribute(null, null));
    }

    /**
     * Tests that visitAnyAttribute handles various attribute type mocks.
     * Since it's a no-op, all attribute types should be handled the same way.
     */
    @Test
    public void testVisitAnyAttribute_withVariousAttributeTypeMocks_doesNotThrowException() {
        // Arrange - create mocks of various attribute types
        Attribute[] attributes = {
            mock(CodeAttribute.class),
            mock(SignatureAttribute.class),
            mock(SourceFileAttribute.class),
            mock(InnerClassesAttribute.class),
            mock(DeprecatedAttribute.class),
            mock(SyntheticAttribute.class),
            mock(LineNumberTableAttribute.class),
            mock(LocalVariableTableAttribute.class),
            mock(LocalVariableTypeTableAttribute.class)
        };

        // Act & Assert - should not throw any exception with any attribute type
        for (Attribute attr : attributes) {
            assertDoesNotThrow(() -> shrinker.visitAnyAttribute(clazz, attr),
                    "Should not throw with attribute: " + attr);
        }
    }

    /**
     * Tests that visitAnyAttribute doesn't change shrinker behavior for subsequent attribute processing.
     * The no-op should have no lasting effects.
     */
    @Test
    public void testVisitAnyAttribute_doesNotAffectSubsequentAttributeProcessing() {
        // Act - call visitAnyAttribute multiple times
        for (int i = 0; i < 5; i++) {
            shrinker.visitAnyAttribute(clazz, attribute);
        }

        // Assert - shrinker should still be in initial state with no interactions
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute with the same attribute instance multiple times
     * doesn't accumulate state or cause issues.
     */
    @Test
    public void testVisitAnyAttribute_sameAttributeInstanceMultipleTimes_noAccumulation() {
        // Arrange
        Attribute singleAttribute = mock(CodeAttribute.class);

        // Act - call multiple times with the same attribute
        for (int i = 0; i < 10; i++) {
            shrinker.visitAnyAttribute(clazz, singleAttribute);
        }

        // Assert - verify no state accumulation
        verifyNoInteractions(clazz);
        verifyNoInteractions(singleAttribute);
    }

    /**
     * Tests that visitAnyAttribute returns immediately without performing any operations.
     * This confirms the no-op nature of the method.
     */
    @Test
    public void testVisitAnyAttribute_returnsImmediatelyWithoutOperations() {
        // Arrange
        Attribute trackedAttribute = mock(Attribute.class);

        // Act
        shrinker.visitAnyAttribute(clazz, trackedAttribute);

        // Assert - no method calls should have been made
        verifyNoInteractions(clazz);
        verifyNoInteractions(trackedAttribute);
    }

    /**
     * Tests that multiple independent shrinkers calling visitAnyAttribute don't interfere with each other.
     */
    @Test
    public void testVisitAnyAttribute_multipleShrinkers_noInterference() {
        // Arrange
        VariableShrinker shrinker1 = new VariableShrinker();
        VariableShrinker shrinker2 = new VariableShrinker();
        VariableShrinker shrinker3 = new VariableShrinker();
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Attribute attr1 = mock(Attribute.class);
        Attribute attr2 = mock(Attribute.class);

        // Act - different shrinkers process different attributes
        shrinker1.visitAnyAttribute(clazz1, attr1);
        shrinker2.visitAnyAttribute(clazz2, attr2);
        shrinker3.visitAnyAttribute(clazz1, attr2);

        // Assert - verify no interactions occurred
        verifyNoInteractions(clazz1);
        verifyNoInteractions(clazz2);
        verifyNoInteractions(attr1);
        verifyNoInteractions(attr2);
    }

    /**
     * Tests that visitAnyAttribute works correctly when the VariableShrinker
     * is used polymorphically as an AttributeVisitor.
     */
    @Test
    public void testVisitAnyAttribute_asAttributeVisitorInterface_doesNotThrowException() {
        // Arrange - treat shrinker as AttributeVisitor interface
        proguard.classfile.attribute.visitor.AttributeVisitor visitor = shrinker;

        // Act & Assert - should work the same way through the interface
        assertDoesNotThrow(() -> visitor.visitAnyAttribute(clazz, attribute));
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute called on newly created shrinker instance behaves consistently.
     * Verifies the method works immediately after construction without initialization.
     */
    @Test
    public void testVisitAnyAttribute_onNewInstance_behavesConsistently() {
        // Arrange - create a fresh shrinker
        VariableShrinker freshShrinker = new VariableShrinker();

        // Act - immediately call visitAnyAttribute
        freshShrinker.visitAnyAttribute(clazz, attribute);

        // Assert - should behave as no-op
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute handles edge case of calling with various
     * combinations of different clazz and attribute pairs in sequence.
     */
    @Test
    public void testVisitAnyAttribute_sequentialCallsWithDifferentParameters_noIssues() {
        // Arrange
        Clazz[] clazzes = {
            mock(ProgramClass.class),
            mock(LibraryClass.class),
            mock(ProgramClass.class)
        };
        Attribute[] attributes = {
            mock(CodeAttribute.class),
            mock(SignatureAttribute.class),
            mock(SourceFileAttribute.class)
        };

        // Act - call with different combinations
        for (Clazz c : clazzes) {
            for (Attribute attr : attributes) {
                shrinker.visitAnyAttribute(c, attr);
            }
        }

        // Assert - verify no interactions with any parameter
        for (Clazz c : clazzes) {
            verifyNoInteractions(c);
        }
        for (Attribute attr : attributes) {
            verifyNoInteractions(attr);
        }
    }

    /**
     * Tests that visitAnyAttribute maintains no-op behavior even when
     * alternating between null and non-null parameters.
     */
    @Test
    public void testVisitAnyAttribute_alternatingNullAndNonNull_consistentBehavior() {
        // Act & Assert - alternate between null and non-null
        assertDoesNotThrow(() -> {
            shrinker.visitAnyAttribute(clazz, attribute);
            shrinker.visitAnyAttribute(null, attribute);
            shrinker.visitAnyAttribute(clazz, null);
            shrinker.visitAnyAttribute(null, null);
            shrinker.visitAnyAttribute(clazz, attribute);
        });

        // Verify no interactions with non-null parameters
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute on shrinker with extraVariableMemberVisitor still has no-op behavior.
     * The extra visitor should not affect visitAnyAttribute since it's only used in visitCodeAttribute.
     */
    @Test
    public void testVisitAnyAttribute_withExtraVariableMemberVisitor_stillNoOp() {
        // Arrange - create shrinker with an extra member visitor
        VariableShrinker shrinkerWithExtra = new VariableShrinker(
            mock(proguard.classfile.visitor.MemberVisitor.class)
        );

        // Act
        shrinkerWithExtra.visitAnyAttribute(clazz, attribute);

        // Assert - should still be a no-op
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute maintains consistent behavior across different constructor variations.
     */
    @Test
    public void testVisitAnyAttribute_differentConstructors_consistentBehavior() {
        // Arrange - create shrinkers using different constructors
        VariableShrinker shrinker1 = new VariableShrinker();
        VariableShrinker shrinker2 = new VariableShrinker(null);
        VariableShrinker shrinker3 = new VariableShrinker(
            mock(proguard.classfile.visitor.MemberVisitor.class)
        );

        // Act - call visitAnyAttribute on all shrinkers
        shrinker1.visitAnyAttribute(clazz, attribute);
        shrinker2.visitAnyAttribute(clazz, attribute);
        shrinker3.visitAnyAttribute(clazz, attribute);

        // Assert - all should be no-ops
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
    }
}
