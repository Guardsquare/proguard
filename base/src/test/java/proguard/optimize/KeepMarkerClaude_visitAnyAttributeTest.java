package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.Attribute;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link KeepMarker#visitAnyAttribute(Clazz, Attribute)}.
 *
 * The visitAnyAttribute method is an empty implementation (no-op) that serves as a default
 * handler in the AttributeVisitor pattern for attributes that don't have specialized visitor methods.
 * Since visitCodeAttribute has a specific implementation, visitAnyAttribute handles all other
 * attribute types by doing nothing.
 *
 * These tests verify that:
 * 1. The method can be called without throwing exceptions
 * 2. The method handles null parameters gracefully
 * 3. The method doesn't interact with any parameters (true no-op)
 * 4. The method can be called multiple times safely
 */
public class KeepMarkerClaude_visitAnyAttributeTest {

    private KeepMarker keepMarker;
    private Clazz clazz;
    private Attribute attribute;

    @BeforeEach
    public void setUp() {
        keepMarker = new KeepMarker();
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
        assertDoesNotThrow(() -> keepMarker.visitAnyAttribute(clazz, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called with null Clazz parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyAttribute_withNullClazz_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> keepMarker.visitAnyAttribute(null, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called with null Attribute parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyAttribute_withNullAttribute_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> keepMarker.visitAnyAttribute(clazz, null));
    }

    /**
     * Tests that visitAnyAttribute can be called with both parameters null.
     * The method should handle null parameters gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyAttribute_withBothParametersNull_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> keepMarker.visitAnyAttribute(null, null));
    }

    /**
     * Tests that visitAnyAttribute can be called multiple times in succession.
     * The method should be idempotent and handle repeated calls without issues.
     */
    @Test
    public void testVisitAnyAttribute_calledMultipleTimes_doesNotThrowException() {
        // Act & Assert - should not throw any exception on multiple calls
        assertDoesNotThrow(() -> {
            keepMarker.visitAnyAttribute(clazz, attribute);
            keepMarker.visitAnyAttribute(clazz, attribute);
            keepMarker.visitAnyAttribute(clazz, attribute);
        });
    }

    /**
     * Tests that visitAnyAttribute doesn't interact with the Clazz parameter.
     * Since it's a no-op method, it should not call any methods on the clazz.
     */
    @Test
    public void testVisitAnyAttribute_doesNotInteractWithClazz() {
        // Act
        keepMarker.visitAnyAttribute(clazz, attribute);

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
        keepMarker.visitAnyAttribute(clazz, attribute);

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
        keepMarker.visitAnyAttribute(clazz, attribute);

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
        proguard.classfile.attribute.visitor.AttributeVisitor visitor = keepMarker;

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
        assertDoesNotThrow(() -> keepMarker.visitAnyAttribute(realClass, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called rapidly in succession.
     * Verifies consistent behavior under stress.
     */
    @Test
    public void testVisitAnyAttribute_rapidSequentialCalls_doesNotThrowException() {
        // Act & Assert - all calls should succeed without exceptions
        for (int i = 0; i < 1000; i++) {
            assertDoesNotThrow(() -> keepMarker.visitAnyAttribute(clazz, attribute),
                    "Call " + i + " should not throw exception");
        }
    }

    /**
     * Tests that visitAnyAttribute works with different KeepMarker instances.
     * Verifies that multiple marker instances behave consistently.
     */
    @Test
    public void testVisitAnyAttribute_multipleKeepMarkerInstances_allWorkCorrectly() {
        // Arrange
        KeepMarker marker1 = new KeepMarker();
        KeepMarker marker2 = new KeepMarker();
        KeepMarker marker3 = new KeepMarker();

        // Act & Assert - all should work without exceptions
        assertDoesNotThrow(() -> {
            marker1.visitAnyAttribute(clazz, attribute);
            marker2.visitAnyAttribute(clazz, attribute);
            marker3.visitAnyAttribute(clazz, attribute);
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
            keepMarker.visitAnyAttribute(clazz, attr1);
            keepMarker.visitAnyAttribute(clazz, attr2);
            keepMarker.visitAnyAttribute(clazz, attr3);
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
            keepMarker.visitAnyAttribute(clazz1, attribute);
            keepMarker.visitAnyAttribute(clazz2, attribute);
            keepMarker.visitAnyAttribute(clazz3, attribute);
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
        keepMarker.visitAnyAttribute(realClass, attribute);

        // Assert - verify the class state wasn't modified
        assertSame(initialProcessingInfo, realClass.getProcessingInfo(),
                "Class processing info should not be modified");
    }

    /**
     * Tests that visitAnyAttribute can be called after other visitor methods.
     * Verifies consistent behavior across different visitor method calls.
     */
    @Test
    public void testVisitAnyAttribute_afterOtherVisitorMethods_stillWorksCorrectly() {
        // Arrange
        ProgramClass testClass = new ProgramClass();
        proguard.classfile.ProgramField programField = new proguard.classfile.ProgramField();

        // Act - call other visitor methods first
        keepMarker.visitAnyClass(testClass);
        keepMarker.visitProgramField(testClass, programField);

        // Act & Assert - visitAnyAttribute should still work
        assertDoesNotThrow(() -> keepMarker.visitAnyAttribute(testClass, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called before other visitor methods.
     * Verifies that the no-op doesn't interfere with subsequent visitor calls.
     */
    @Test
    public void testVisitAnyAttribute_beforeOtherVisitorMethods_doesNotInterfere() {
        // Arrange
        ProgramClass testClass = new ProgramClass();
        proguard.classfile.ProgramField programField = new proguard.classfile.ProgramField();

        // Act - call visitAnyAttribute first
        keepMarker.visitAnyAttribute(testClass, attribute);

        // Act & Assert - other visitor methods should still work
        assertDoesNotThrow(() -> {
            keepMarker.visitAnyClass(testClass);
            keepMarker.visitProgramField(testClass, programField);
        });
    }

    /**
     * Tests that visitAnyAttribute is truly a no-op by verifying no exceptions
     * even with parameters that would normally cause issues if accessed.
     */
    @Test
    public void testVisitAnyAttribute_isNoop_noExceptionsWithAnyInput() {
        // Act & Assert - should not throw even with unusual combinations
        assertDoesNotThrow(() -> {
            keepMarker.visitAnyAttribute(null, null);
            keepMarker.visitAnyAttribute(clazz, null);
            keepMarker.visitAnyAttribute(null, attribute);
            keepMarker.visitAnyAttribute(clazz, attribute);
        });
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
            keepMarker.visitAnyAttribute(clazz, attribute);
            keepMarker.visitCodeAttribute(clazz, method, codeAttribute);
            keepMarker.visitAnyAttribute(clazz, attribute);
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
                keepMarker.visitAnyAttribute(clazz, attribute);
            }
        });
    }
}
