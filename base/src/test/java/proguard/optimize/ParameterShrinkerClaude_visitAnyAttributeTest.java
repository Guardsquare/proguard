package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.visitor.MemberVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ParameterShrinker#visitAnyAttribute(Clazz, Attribute)}.
 *
 * The visitAnyAttribute method is an empty implementation (no-op) that serves as a default
 * handler in the AttributeVisitor pattern for attributes that don't have specialized visitor methods.
 * Since ParameterShrinker only has a specific implementation for visitCodeAttribute,
 * visitAnyAttribute handles all other attribute types by doing nothing.
 *
 * These tests verify that:
 * 1. The method can be called without throwing exceptions
 * 2. The method handles null parameters gracefully
 * 3. The method doesn't interact with any parameters (true no-op)
 * 4. The method can be called multiple times safely
 */
public class ParameterShrinkerClaude_visitAnyAttributeTest {

    private ParameterShrinker shrinker;
    private Clazz clazz;
    private Attribute attribute;

    @BeforeEach
    public void setUp() {
        shrinker = new ParameterShrinker();
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
     * Tests that visitAnyAttribute doesn't interact with either parameter.
     * Verifies that both parameters remain untouched.
     */
    @Test
    public void testVisitAnyAttribute_doesNotInteractWithAnyParameter() {
        // Act
        shrinker.visitAnyAttribute(clazz, attribute);

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
        proguard.classfile.attribute.visitor.AttributeVisitor visitor = shrinker;

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
        assertDoesNotThrow(() -> shrinker.visitAnyAttribute(realClass, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called rapidly in succession.
     * Verifies consistent behavior under stress.
     */
    @Test
    public void testVisitAnyAttribute_rapidSequentialCalls_doesNotThrowException() {
        // Act & Assert - all calls should succeed without exceptions
        for (int i = 0; i < 1000; i++) {
            assertDoesNotThrow(() -> shrinker.visitAnyAttribute(clazz, attribute),
                    "Call " + i + " should not throw exception");
        }
    }

    /**
     * Tests that visitAnyAttribute works with different ParameterShrinker instances.
     * Verifies that multiple shrinker instances behave consistently.
     */
    @Test
    public void testVisitAnyAttribute_multipleShrinkerInstances_allWorkCorrectly() {
        // Arrange
        ParameterShrinker shrinker1 = new ParameterShrinker();
        ParameterShrinker shrinker2 = new ParameterShrinker();
        ParameterShrinker shrinker3 = new ParameterShrinker();

        // Act & Assert - all should work without exceptions
        assertDoesNotThrow(() -> {
            shrinker1.visitAnyAttribute(clazz, attribute);
            shrinker2.visitAnyAttribute(clazz, attribute);
            shrinker3.visitAnyAttribute(clazz, attribute);
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
            shrinker.visitAnyAttribute(clazz, attr1);
            shrinker.visitAnyAttribute(clazz, attr2);
            shrinker.visitAnyAttribute(clazz, attr3);
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
            shrinker.visitAnyAttribute(clazz1, attribute);
            shrinker.visitAnyAttribute(clazz2, attribute);
            shrinker.visitAnyAttribute(clazz3, attribute);
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
        shrinker.visitAnyAttribute(realClass, attribute);

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
            shrinker.visitAnyAttribute(null, null);
            shrinker.visitAnyAttribute(clazz, null);
            shrinker.visitAnyAttribute(null, attribute);
            shrinker.visitAnyAttribute(clazz, attribute);
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
                shrinker.visitAnyAttribute(clazz, attribute);
            }
        });
    }

    /**
     * Tests that visitAnyAttribute works correctly with shrinker created with extraMemberVisitor.
     * Verifies that the no-op behavior is consistent regardless of constructor variant used.
     */
    @Test
    public void testVisitAnyAttribute_withExtraMemberVisitor_stillNoOp() {
        // Arrange
        MemberVisitor extraVisitor = mock(MemberVisitor.class);
        ParameterShrinker shrinkerWithExtra = new ParameterShrinker(extraVisitor);

        // Act
        shrinkerWithExtra.visitAnyAttribute(clazz, attribute);

        // Assert - verify no interactions
        verifyNoInteractions(clazz, attribute, extraVisitor);
    }

    /**
     * Tests that visitAnyAttribute can be interleaved with visitCodeAttribute calls.
     * Verifies that the no-op doesn't interfere with the specialized attribute visitor.
     */
    @Test
    public void testVisitAnyAttribute_interleavedWithVisitCodeAttribute_worksCorrectly() {
        // Arrange
        proguard.classfile.attribute.CodeAttribute codeAttribute =
                mock(proguard.classfile.attribute.CodeAttribute.class);
        proguard.classfile.Method method = mock(proguard.classfile.Method.class);

        // Act & Assert - both methods should work when called in sequence
        assertDoesNotThrow(() -> {
            shrinker.visitAnyAttribute(clazz, attribute);
            shrinker.visitAnyAttribute(clazz, attribute);
        });
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
            shrinker.visitAnyAttribute(clazz, signatureAttr);
            shrinker.visitAnyAttribute(clazz, exceptionsAttr);
            shrinker.visitAnyAttribute(clazz, lineNumberAttr);
        });
    }

    /**
     * Tests that visitAnyAttribute on one instance doesn't affect other instances.
     * Verifies proper instance isolation.
     */
    @Test
    public void testVisitAnyAttribute_instanceIsolation_noInterference() {
        // Arrange
        ParameterShrinker shrinker1 = new ParameterShrinker();
        ParameterShrinker shrinker2 = new ParameterShrinker();

        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Attribute attr1 = mock(Attribute.class);
        Attribute attr2 = mock(Attribute.class);

        // Act
        shrinker1.visitAnyAttribute(clazz1, attr1);
        shrinker2.visitAnyAttribute(clazz2, attr2);

        // Assert - verify each set of mocks was only used by its own shrinker
        verifyNoInteractions(clazz1, attr1, clazz2, attr2);
    }

    /**
     * Tests that visitAnyAttribute with no-arg constructor works correctly.
     * Verifies that the no-op behavior is consistent with no-arg constructor.
     */
    @Test
    public void testVisitAnyAttribute_withNoArgConstructor_stillNoOp() {
        // Arrange
        ParameterShrinker noArgShrinker = new ParameterShrinker();

        // Act
        noArgShrinker.visitAnyAttribute(clazz, attribute);

        // Assert - verify no interactions
        verifyNoInteractions(clazz, attribute);
    }

    /**
     * Tests that visitAnyAttribute can be called before and after visitCodeAttribute.
     * Verifies that the no-op doesn't affect the state needed by visitCodeAttribute.
     */
    @Test
    public void testVisitAnyAttribute_beforeAndAfterVisitCodeAttribute_worksCorrectly() {
        // Act & Assert - should work in any order
        assertDoesNotThrow(() -> {
            shrinker.visitAnyAttribute(clazz, attribute);
            shrinker.visitAnyAttribute(clazz, attribute);
        });
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
        shrinker.visitAnyAttribute(realClass, attribute);

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
                    shrinker.visitAnyAttribute(clazz, attribute);
                } else {
                    shrinker.visitAnyAttribute(null, null);
                }
            }
        });
    }

    /**
     * Tests that multiple ParameterShrinker instances with different configurations
     * all have the same no-op behavior for visitAnyAttribute.
     */
    @Test
    public void testVisitAnyAttribute_differentShrinkerConfigurations_sameNoOpBehavior() {
        // Arrange
        MemberVisitor visitor1 = mock(MemberVisitor.class);
        MemberVisitor visitor2 = mock(MemberVisitor.class);

        ParameterShrinker shrinker1 = new ParameterShrinker();
        ParameterShrinker shrinker2 = new ParameterShrinker(visitor1);
        ParameterShrinker shrinker3 = new ParameterShrinker(visitor2);
        ParameterShrinker shrinker4 = new ParameterShrinker(null);

        // Act
        shrinker1.visitAnyAttribute(clazz, attribute);
        shrinker2.visitAnyAttribute(clazz, attribute);
        shrinker3.visitAnyAttribute(clazz, attribute);
        shrinker4.visitAnyAttribute(clazz, attribute);

        // Assert - all should have no interactions
        verifyNoInteractions(clazz, attribute, visitor1, visitor2);
    }
}
