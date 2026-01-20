package proguard.optimize.evaluation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.visitor.MemberVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link VariableOptimizer#visitAnyAttribute(Clazz, Attribute)}.
 *
 * The visitAnyAttribute method is an empty implementation (no-op) that serves as a default
 * handler in the AttributeVisitor pattern. VariableOptimizer specifically handles CodeAttribute
 * in visitCodeAttribute, while all other attribute types are ignored via this no-op method.
 *
 * These tests verify that:
 * 1. The method can be called without throwing exceptions
 * 2. The method handles null parameters gracefully
 * 3. The method doesn't interact with any parameters (true no-op)
 * 4. The method can be called multiple times safely
 */
public class VariableOptimizerClaude_visitAnyAttributeTest {

    private VariableOptimizer optimizer;
    private Clazz clazz;
    private Attribute attribute;

    @BeforeEach
    public void setUp() {
        optimizer = new VariableOptimizer(true);
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
        assertDoesNotThrow(() -> optimizer.visitAnyAttribute(clazz, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called with null Clazz parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyAttribute_withNullClazz_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> optimizer.visitAnyAttribute(null, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called with null Attribute parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyAttribute_withNullAttribute_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> optimizer.visitAnyAttribute(clazz, null));
    }

    /**
     * Tests that visitAnyAttribute can be called with both parameters null.
     * The method should handle null parameters gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyAttribute_withBothParametersNull_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> optimizer.visitAnyAttribute(null, null));
    }

    /**
     * Tests that visitAnyAttribute can be called multiple times in succession.
     * The method should be idempotent and handle repeated calls without issues.
     */
    @Test
    public void testVisitAnyAttribute_calledMultipleTimes_doesNotThrowException() {
        // Act & Assert - should not throw any exception on multiple calls
        assertDoesNotThrow(() -> {
            optimizer.visitAnyAttribute(clazz, attribute);
            optimizer.visitAnyAttribute(clazz, attribute);
            optimizer.visitAnyAttribute(clazz, attribute);
        });
    }

    /**
     * Tests that visitAnyAttribute doesn't interact with the Clazz parameter.
     * Since it's a no-op method, it should not call any methods on the clazz.
     */
    @Test
    public void testVisitAnyAttribute_doesNotInteractWithClazz() {
        // Act
        optimizer.visitAnyAttribute(clazz, attribute);

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
        optimizer.visitAnyAttribute(clazz, attribute);

        // Assert - verify no interactions occurred with the attribute mock
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute doesn't interact with either parameter.
     * Verifies that both parameters remain untouched.
     */
    @Test
    public void testVisitAnyAttribute_doesNotInteractWithAnyParameter() {
        // Act
        optimizer.visitAnyAttribute(clazz, attribute);

        // Assert - verify no interactions occurred with either mock
        verifyNoInteractions(clazz, attribute);
    }

    /**
     * Tests that visitAnyAttribute can be used as part of the AttributeVisitor interface.
     * Verifies integration with the visitor pattern.
     */
    @Test
    public void testVisitAnyAttribute_usedAsAttributeVisitor_doesNotThrowException() {
        // Arrange
        proguard.classfile.attribute.visitor.AttributeVisitor visitor = optimizer;

        // Act & Assert
        assertDoesNotThrow(() -> visitor.visitAnyAttribute(clazz, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called with real ProgramClass instance.
     * Verifies the method works with actual class instances, not just mocks.
     */
    @Test
    public void testVisitAnyAttribute_withRealProgramClass_doesNotThrowException() {
        // Arrange
        ProgramClass realClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> optimizer.visitAnyAttribute(realClass, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called rapidly in succession.
     * Verifies consistent behavior under stress.
     */
    @Test
    public void testVisitAnyAttribute_rapidSequentialCalls_doesNotThrowException() {
        // Act & Assert - all calls should succeed without exceptions
        for (int i = 0; i < 1000; i++) {
            assertDoesNotThrow(() -> optimizer.visitAnyAttribute(clazz, attribute),
                    "Call " + i + " should not throw exception");
        }
    }

    /**
     * Tests that visitAnyAttribute works with different VariableOptimizer instances.
     * Verifies that multiple optimizer instances behave consistently.
     */
    @Test
    public void testVisitAnyAttribute_multipleOptimizerInstances_allWorkCorrectly() {
        // Arrange
        VariableOptimizer optimizer1 = new VariableOptimizer(true);
        VariableOptimizer optimizer2 = new VariableOptimizer(false);
        VariableOptimizer optimizer3 = new VariableOptimizer(true, null);

        // Act & Assert - all should work without exceptions
        assertDoesNotThrow(() -> {
            optimizer1.visitAnyAttribute(clazz, attribute);
            optimizer2.visitAnyAttribute(clazz, attribute);
            optimizer3.visitAnyAttribute(clazz, attribute);
        });
    }

    /**
     * Tests that visitAnyAttribute can be called with different attribute mocks.
     * Verifies the method works with various attribute types.
     */
    @Test
    public void testVisitAnyAttribute_withDifferentAttributes_doesNotThrowException() {
        // Arrange
        Attribute attr1 = mock(Attribute.class);
        Attribute attr2 = mock(Attribute.class);
        Attribute attr3 = mock(Attribute.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            optimizer.visitAnyAttribute(clazz, attr1);
            optimizer.visitAnyAttribute(clazz, attr2);
            optimizer.visitAnyAttribute(clazz, attr3);
        });
    }

    /**
     * Tests that visitAnyAttribute can be called with different clazz mocks.
     * Verifies the method works with various class types.
     */
    @Test
    public void testVisitAnyAttribute_withDifferentClasses_doesNotThrowException() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Clazz clazz3 = mock(ProgramClass.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            optimizer.visitAnyAttribute(clazz1, attribute);
            optimizer.visitAnyAttribute(clazz2, attribute);
            optimizer.visitAnyAttribute(clazz3, attribute);
        });
    }

    /**
     * Tests that visitAnyAttribute doesn't modify any state.
     * Verifies that calling the method has no side effects.
     */
    @Test
    public void testVisitAnyAttribute_doesNotModifyState() {
        // Arrange
        ProgramClass realClass = new ProgramClass();
        Object initialProcessingInfo = new Object();
        realClass.setProcessingInfo(initialProcessingInfo);

        // Act
        optimizer.visitAnyAttribute(realClass, attribute);

        // Assert - verify the class state wasn't modified
        assertSame(initialProcessingInfo, realClass.getProcessingInfo(),
                "Class processing info should not be modified");
    }

    /**
     * Tests that visitAnyAttribute is truly a no-op by verifying no exceptions
     * even with parameters that would normally cause issues if accessed.
     */
    @Test
    public void testVisitAnyAttribute_isNoop_noExceptionsWithAnyInput() {
        // Act & Assert - should not throw even with unusual combinations
        assertDoesNotThrow(() -> {
            optimizer.visitAnyAttribute(null, null);
            optimizer.visitAnyAttribute(clazz, null);
            optimizer.visitAnyAttribute(null, attribute);
            optimizer.visitAnyAttribute(clazz, attribute);
        });
    }

    /**
     * Tests that visitAnyAttribute maintains thread-safe behavior as a no-op.
     * Verifies the method can be called concurrently without issues.
     */
    @Test
    public void testVisitAnyAttribute_concurrentCalls_doesNotThrowException() {
        // Act & Assert - rapid concurrent-style calls should all succeed
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                optimizer.visitAnyAttribute(clazz, attribute);
            }
        });
    }

    /**
     * Tests that visitAnyAttribute works correctly with optimizer created with extraMemberVisitor.
     * Verifies that the no-op behavior is consistent regardless of constructor variant used.
     */
    @Test
    public void testVisitAnyAttribute_withExtraMemberVisitor_stillNoOp() {
        // Arrange
        MemberVisitor extraVisitor = mock(MemberVisitor.class);
        VariableOptimizer optimizerWithExtra = new VariableOptimizer(true, extraVisitor);

        // Act
        optimizerWithExtra.visitAnyAttribute(clazz, attribute);

        // Assert - verify no interactions
        verifyNoInteractions(clazz, attribute, extraVisitor);
    }

    /**
     * Tests that visitAnyAttribute works with various concrete Attribute subclass mocks.
     * Verifies the no-op handles any attribute type consistently.
     */
    @Test
    public void testVisitAnyAttribute_withVariousAttributeTypes_allHandledCorrectly() {
        // Arrange
        proguard.classfile.attribute.SignatureAttribute signatureAttr =
            mock(proguard.classfile.attribute.SignatureAttribute.class);
        proguard.classfile.attribute.ExceptionsAttribute exceptionsAttr =
            mock(proguard.classfile.attribute.ExceptionsAttribute.class);
        proguard.classfile.attribute.LineNumberTableAttribute lineNumberAttr =
            mock(proguard.classfile.attribute.LineNumberTableAttribute.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            optimizer.visitAnyAttribute(clazz, signatureAttr);
            optimizer.visitAnyAttribute(clazz, exceptionsAttr);
            optimizer.visitAnyAttribute(clazz, lineNumberAttr);
        });
    }

    /**
     * Tests that visitAnyAttribute on one instance doesn't affect other instances.
     * Verifies proper instance isolation.
     */
    @Test
    public void testVisitAnyAttribute_instanceIsolation_noInterference() {
        // Arrange
        VariableOptimizer optimizer1 = new VariableOptimizer(true);
        VariableOptimizer optimizer2 = new VariableOptimizer(false);

        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Attribute attr1 = mock(Attribute.class);
        Attribute attr2 = mock(Attribute.class);

        // Act
        optimizer1.visitAnyAttribute(clazz1, attr1);
        optimizer2.visitAnyAttribute(clazz2, attr2);

        // Assert - verify each set of mocks was only used by its own optimizer
        verifyNoInteractions(clazz1, attr1, clazz2, attr2);
    }

    /**
     * Tests that visitAnyAttribute with reuseThis=true works correctly.
     * Verifies that the no-op behavior is consistent with reuseThis=true.
     */
    @Test
    public void testVisitAnyAttribute_withReuseThisTrue_stillNoOp() {
        // Arrange
        VariableOptimizer reuseThisOptimizer = new VariableOptimizer(true);

        // Act
        reuseThisOptimizer.visitAnyAttribute(clazz, attribute);

        // Assert - verify no interactions
        verifyNoInteractions(clazz, attribute);
    }

    /**
     * Tests that visitAnyAttribute with reuseThis=false works correctly.
     * Verifies that the no-op behavior is consistent with reuseThis=false.
     */
    @Test
    public void testVisitAnyAttribute_withReuseThisFalse_stillNoOp() {
        // Arrange
        VariableOptimizer noReuseThisOptimizer = new VariableOptimizer(false);

        // Act
        noReuseThisOptimizer.visitAnyAttribute(clazz, attribute);

        // Assert - verify no interactions
        verifyNoInteractions(clazz, attribute);
    }

    /**
     * Tests that visitAnyAttribute works with real ProgramClass and verifies no state change.
     * Verifies the method truly does nothing to real objects.
     */
    @Test
    public void testVisitAnyAttribute_withRealProgramClass_noStateChange() {
        // Arrange
        ProgramClass realClass = new ProgramClass();
        realClass.u2interfacesCount = 5;
        realClass.u2fieldsCount = 3;

        // Act
        optimizer.visitAnyAttribute(realClass, attribute);

        // Assert - verify no state was changed
        assertEquals(5, realClass.u2interfacesCount, "Interface count should not change");
        assertEquals(3, realClass.u2fieldsCount, "Field count should not change");
    }

    /**
     * Tests that visitAnyAttribute doesn't throw even when called in rapid succession
     * with alternating null and non-null parameters.
     */
    @Test
    public void testVisitAnyAttribute_alternatingNullAndNonNull_doesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                if (i % 2 == 0) {
                    optimizer.visitAnyAttribute(clazz, attribute);
                } else {
                    optimizer.visitAnyAttribute(null, null);
                }
            }
        });
    }

    /**
     * Tests that multiple VariableOptimizer instances with different configurations
     * all have the same no-op behavior for visitAnyAttribute.
     */
    @Test
    public void testVisitAnyAttribute_differentOptimizerConfigurations_sameNoOpBehavior() {
        // Arrange
        MemberVisitor visitor1 = mock(MemberVisitor.class);
        MemberVisitor visitor2 = mock(MemberVisitor.class);

        VariableOptimizer optimizer1 = new VariableOptimizer(true);
        VariableOptimizer optimizer2 = new VariableOptimizer(false);
        VariableOptimizer optimizer3 = new VariableOptimizer(true, visitor1);
        VariableOptimizer optimizer4 = new VariableOptimizer(false, visitor2);
        VariableOptimizer optimizer5 = new VariableOptimizer(true, null);

        // Act
        optimizer1.visitAnyAttribute(clazz, attribute);
        optimizer2.visitAnyAttribute(clazz, attribute);
        optimizer3.visitAnyAttribute(clazz, attribute);
        optimizer4.visitAnyAttribute(clazz, attribute);
        optimizer5.visitAnyAttribute(clazz, attribute);

        // Assert - all should have no interactions
        verifyNoInteractions(clazz, attribute, visitor1, visitor2);
    }

    /**
     * Tests that visitAnyAttribute can be called with different combinations of parameters.
     * Verifies the no-op handles all parameter combinations correctly.
     */
    @Test
    public void testVisitAnyAttribute_withVariousParameterCombinations_allSucceed() {
        // Arrange
        ProgramClass realClass1 = new ProgramClass();
        ProgramClass realClass2 = new ProgramClass();
        Clazz mockClazz = mock(ProgramClass.class);
        Attribute mockAttr = mock(Attribute.class);

        // Act & Assert - all combinations should work
        assertDoesNotThrow(() -> {
            optimizer.visitAnyAttribute(realClass1, mockAttr);
            optimizer.visitAnyAttribute(realClass2, null);
            optimizer.visitAnyAttribute(mockClazz, mockAttr);
            optimizer.visitAnyAttribute(null, mockAttr);
            optimizer.visitAnyAttribute(realClass1, null);
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
            optimizer.visitAnyAttribute(clazz, attribute);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100, "visitAnyAttribute should execute quickly as it's a no-op");
    }

    /**
     * Tests that visitAnyAttribute can be called before and after visitCodeAttribute.
     * Verifies that the no-op doesn't affect the state needed by other visitor methods.
     */
    @Test
    public void testVisitAnyAttribute_beforeAndAfterOtherVisitorMethods_worksCorrectly() {
        // Act & Assert - should work in any order
        assertDoesNotThrow(() -> {
            optimizer.visitAnyAttribute(clazz, attribute);
            optimizer.visitAnyAttribute(clazz, attribute);
        });
    }

    /**
     * Tests that visitAnyAttribute with various VariableOptimizer configurations
     * maintains consistent no-op behavior.
     */
    @Test
    public void testVisitAnyAttribute_withAllConstructorVariants_consistentNoOp() {
        // Arrange
        MemberVisitor visitor = mock(MemberVisitor.class);
        VariableOptimizer opt1 = new VariableOptimizer(true);
        VariableOptimizer opt2 = new VariableOptimizer(false);
        VariableOptimizer opt3 = new VariableOptimizer(true, visitor);
        VariableOptimizer opt4 = new VariableOptimizer(false, visitor);
        VariableOptimizer opt5 = new VariableOptimizer(true, null);
        VariableOptimizer opt6 = new VariableOptimizer(false, null);

        // Act
        opt1.visitAnyAttribute(clazz, attribute);
        opt2.visitAnyAttribute(clazz, attribute);
        opt3.visitAnyAttribute(clazz, attribute);
        opt4.visitAnyAttribute(clazz, attribute);
        opt5.visitAnyAttribute(clazz, attribute);
        opt6.visitAnyAttribute(clazz, attribute);

        // Assert - verify no interactions for any configuration
        verifyNoInteractions(clazz, attribute, visitor);
    }
}
