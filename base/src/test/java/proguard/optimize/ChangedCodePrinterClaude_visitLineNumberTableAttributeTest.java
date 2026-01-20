package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.LineNumberTableAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ChangedCodePrinter#visitLineNumberTableAttribute(Clazz, Method, CodeAttribute, LineNumberTableAttribute)}.
 *
 * The visitLineNumberTableAttribute method in ChangedCodePrinter is a delegation method.
 * It simply forwards the call to the wrapped AttributeVisitor without any additional logic,
 * as LineNumberTableAttribute does not contain bytecode that needs change detection.
 *
 * These tests verify that the method:
 * 1. Correctly delegates to the wrapped visitor
 * 2. Passes the correct parameters in the correct order
 * 3. Works with various inputs including edge cases
 */
public class ChangedCodePrinterClaude_visitLineNumberTableAttributeTest {

    private AttributeVisitor mockAttributeVisitor;
    private ChangedCodePrinter changedCodePrinter;
    private Clazz clazz;
    private Method method;
    private CodeAttribute codeAttribute;

    @BeforeEach
    public void setUp() {
        mockAttributeVisitor = mock(AttributeVisitor.class);
        changedCodePrinter = new ChangedCodePrinter(mockAttributeVisitor);
        clazz = new ProgramClass();
        method = new ProgramMethod();
        codeAttribute = new CodeAttribute();
    }

    /**
     * Tests that visitLineNumberTableAttribute delegates to the wrapped visitor.
     * Verifies that the method calls the visitor with the correct parameters.
     */
    @Test
    public void testVisitLineNumberTableAttribute_delegatesToWrappedVisitor() {
        // Arrange
        LineNumberTableAttribute attribute = new LineNumberTableAttribute();

        // Act
        changedCodePrinter.visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that the method does not throw an exception with valid inputs.
     */
    @Test
    public void testVisitLineNumberTableAttribute_withValidInputs_doesNotThrow() {
        // Arrange
        LineNumberTableAttribute attribute = new LineNumberTableAttribute();

        // Act & Assert
        assertDoesNotThrow(() ->
            changedCodePrinter.visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute),
            "visitLineNumberTableAttribute should not throw any exception");
    }

    /**
     * Tests that the method can be called multiple times without issues.
     */
    @Test
    public void testVisitLineNumberTableAttribute_calledMultipleTimes_doesNotThrow() {
        // Arrange
        LineNumberTableAttribute attribute = new LineNumberTableAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute);
            changedCodePrinter.visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute);
            changedCodePrinter.visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute);
        }, "Multiple calls should not throw any exception");

        // Verify the visitor was called multiple times
        verify(mockAttributeVisitor, times(3))
            .visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that the method works with different Clazz instances.
     */
    @Test
    public void testVisitLineNumberTableAttribute_withDifferentClasses_delegatesCorrectly() {
        // Arrange
        Clazz clazz1 = new ProgramClass();
        Clazz clazz2 = new ProgramClass();
        LineNumberTableAttribute attribute = new LineNumberTableAttribute();

        // Act
        changedCodePrinter.visitLineNumberTableAttribute(clazz1, method, codeAttribute, attribute);
        changedCodePrinter.visitLineNumberTableAttribute(clazz2, method, codeAttribute, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitLineNumberTableAttribute(clazz1, method, codeAttribute, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitLineNumberTableAttribute(clazz2, method, codeAttribute, attribute);
    }

    /**
     * Tests that the method works with different Method instances.
     */
    @Test
    public void testVisitLineNumberTableAttribute_withDifferentMethods_delegatesCorrectly() {
        // Arrange
        Method method1 = new ProgramMethod();
        Method method2 = new ProgramMethod();
        LineNumberTableAttribute attribute = new LineNumberTableAttribute();

        // Act
        changedCodePrinter.visitLineNumberTableAttribute(clazz, method1, codeAttribute, attribute);
        changedCodePrinter.visitLineNumberTableAttribute(clazz, method2, codeAttribute, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitLineNumberTableAttribute(clazz, method1, codeAttribute, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitLineNumberTableAttribute(clazz, method2, codeAttribute, attribute);
    }

    /**
     * Tests that the method works with different CodeAttribute instances.
     */
    @Test
    public void testVisitLineNumberTableAttribute_withDifferentCodeAttributes_delegatesCorrectly() {
        // Arrange
        CodeAttribute codeAttribute1 = new CodeAttribute();
        CodeAttribute codeAttribute2 = new CodeAttribute();
        LineNumberTableAttribute attribute = new LineNumberTableAttribute();

        // Act
        changedCodePrinter.visitLineNumberTableAttribute(clazz, method, codeAttribute1, attribute);
        changedCodePrinter.visitLineNumberTableAttribute(clazz, method, codeAttribute2, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitLineNumberTableAttribute(clazz, method, codeAttribute1, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitLineNumberTableAttribute(clazz, method, codeAttribute2, attribute);
    }

    /**
     * Tests that the method works with different LineNumberTableAttribute instances.
     */
    @Test
    public void testVisitLineNumberTableAttribute_withDifferentAttributes_delegatesCorrectly() {
        // Arrange
        LineNumberTableAttribute attribute1 = new LineNumberTableAttribute();
        LineNumberTableAttribute attribute2 = new LineNumberTableAttribute();

        // Act
        changedCodePrinter.visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute1);
        changedCodePrinter.visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute2);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute2);
    }

    /**
     * Tests that the method passes parameters in the correct order.
     */
    @Test
    public void testVisitLineNumberTableAttribute_passesParametersInCorrectOrder() {
        // Arrange
        LineNumberTableAttribute attribute = new LineNumberTableAttribute();

        // Act
        changedCodePrinter.visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify the parameters are in correct order
        verify(mockAttributeVisitor).visitLineNumberTableAttribute(
            argThat(arg -> arg == clazz),
            argThat(arg -> arg == method),
            argThat(arg -> arg == codeAttribute),
            argThat(arg -> arg == attribute)
        );
    }

    /**
     * Tests that the method can be called in rapid succession.
     */
    @Test
    public void testVisitLineNumberTableAttribute_rapidSuccessiveCalls_doesNotThrow() {
        // Arrange
        LineNumberTableAttribute attribute = new LineNumberTableAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                changedCodePrinter.visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute);
            }
        }, "Rapid successive calls should not throw any exception");

        // Verify the visitor was called 100 times
        verify(mockAttributeVisitor, times(100))
            .visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that the method works when the wrapped visitor does nothing (no-op).
     */
    @Test
    public void testVisitLineNumberTableAttribute_withNoOpVisitor_doesNotThrow() {
        // Arrange
        AttributeVisitor noOpVisitor = mock(AttributeVisitor.class);
        doNothing().when(noOpVisitor).visitLineNumberTableAttribute(any(), any(), any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(noOpVisitor);
        LineNumberTableAttribute attribute = new LineNumberTableAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> printer.visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute),
            "Should not throw when visitor is no-op");
    }

    /**
     * Tests that the method works when the wrapped visitor throws an exception.
     */
    @Test
    public void testVisitLineNumberTableAttribute_whenVisitorThrows_propagatesException() {
        // Arrange
        AttributeVisitor throwingVisitor = mock(AttributeVisitor.class);
        RuntimeException expectedException = new RuntimeException("Test exception");
        doThrow(expectedException)
            .when(throwingVisitor)
            .visitLineNumberTableAttribute(any(), any(), any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(throwingVisitor);
        LineNumberTableAttribute attribute = new LineNumberTableAttribute();

        // Act & Assert
        RuntimeException thrownException = assertThrows(RuntimeException.class, () ->
            printer.visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute),
            "Should propagate exception from wrapped visitor");

        assertEquals(expectedException, thrownException, "Should throw the same exception");
    }

    /**
     * Tests that multiple ChangedCodePrinter instances work independently.
     */
    @Test
    public void testVisitLineNumberTableAttribute_multipleInstances_workIndependently() {
        // Arrange
        AttributeVisitor visitor1 = mock(AttributeVisitor.class);
        AttributeVisitor visitor2 = mock(AttributeVisitor.class);
        ChangedCodePrinter printer1 = new ChangedCodePrinter(visitor1);
        ChangedCodePrinter printer2 = new ChangedCodePrinter(visitor2);
        LineNumberTableAttribute attribute = new LineNumberTableAttribute();

        // Act
        printer1.visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute);
        printer2.visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert
        verify(visitor1, times(1)).visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute);
        verify(visitor2, times(1)).visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute);
        verifyNoMoreInteractions(visitor1, visitor2);
    }

    /**
     * Tests that the printer can be reused after calling visitLineNumberTableAttribute.
     */
    @Test
    public void testVisitLineNumberTableAttribute_printerReusable() {
        // Arrange
        LineNumberTableAttribute attribute1 = new LineNumberTableAttribute();
        LineNumberTableAttribute attribute2 = new LineNumberTableAttribute();

        // Act & Assert - reuse the same printer
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute1);
            changedCodePrinter.visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute2);
            changedCodePrinter.visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute1);
        }, "Printer should be reusable");

        verify(mockAttributeVisitor, times(2))
            .visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute2);
    }

    /**
     * Tests that the method delegates exactly once per call.
     */
    @Test
    public void testVisitLineNumberTableAttribute_delegatesExactlyOnce() {
        // Arrange
        LineNumberTableAttribute attribute = new LineNumberTableAttribute();

        // Act
        changedCodePrinter.visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert - should delegate exactly once, no more, no less
        verify(mockAttributeVisitor, times(1))
            .visitLineNumberTableAttribute(any(), any(), any(), any());
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method doesn't call any other visitor methods.
     */
    @Test
    public void testVisitLineNumberTableAttribute_doesNotCallOtherVisitorMethods() {
        // Arrange
        LineNumberTableAttribute attribute = new LineNumberTableAttribute();

        // Act
        changedCodePrinter.visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify only visitLineNumberTableAttribute was called
        verify(mockAttributeVisitor, times(1))
            .visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute);
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method returns normally (doesn't hang or loop).
     */
    @Test
    public void testVisitLineNumberTableAttribute_returnsImmediately() {
        // Arrange
        LineNumberTableAttribute attribute = new LineNumberTableAttribute();

        // Act
        long startTime = System.nanoTime();
        changedCodePrinter.visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute);
        long endTime = System.nanoTime();

        // Assert - should complete very quickly (within 1 second)
        long durationNanos = endTime - startTime;
        long oneSecondInNanos = 1_000_000_000L;
        assertTrue(durationNanos < oneSecondInNanos,
                "Method should return immediately, took " + durationNanos + " nanoseconds");
    }

    /**
     * Tests that the method works with newly created ChangedCodePrinter.
     */
    @Test
    public void testVisitLineNumberTableAttribute_withFreshPrinter_doesNotThrow() {
        // Arrange
        AttributeVisitor visitor = mock(AttributeVisitor.class);
        ChangedCodePrinter freshPrinter = new ChangedCodePrinter(visitor);
        LineNumberTableAttribute attribute = new LineNumberTableAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> freshPrinter.visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute),
                "Method should work with a newly created printer");
    }

    /**
     * Tests that the same attribute can be visited multiple times with the same parameters.
     */
    @Test
    public void testVisitLineNumberTableAttribute_sameAttributeMultipleTimes() {
        // Arrange
        LineNumberTableAttribute attribute = new LineNumberTableAttribute();

        // Act
        changedCodePrinter.visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute);
        changedCodePrinter.visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute);
        changedCodePrinter.visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert
        verify(mockAttributeVisitor, times(3))
            .visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the attribute.
     */
    @Test
    public void testVisitLineNumberTableAttribute_doesNotModifyAttribute() {
        // Arrange
        LineNumberTableAttribute attribute = new LineNumberTableAttribute();

        // Act
        changedCodePrinter.visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify the call succeeded and delegated properly
        verify(mockAttributeVisitor, times(1))
            .visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the clazz.
     */
    @Test
    public void testVisitLineNumberTableAttribute_doesNotModifyClazz() {
        // Arrange
        LineNumberTableAttribute attribute = new LineNumberTableAttribute();

        // Act
        changedCodePrinter.visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the method.
     */
    @Test
    public void testVisitLineNumberTableAttribute_doesNotModifyMethod() {
        // Arrange
        LineNumberTableAttribute attribute = new LineNumberTableAttribute();

        // Act
        changedCodePrinter.visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the code attribute.
     */
    @Test
    public void testVisitLineNumberTableAttribute_doesNotModifyCodeAttribute() {
        // Arrange
        LineNumberTableAttribute attribute = new LineNumberTableAttribute();

        // Act
        changedCodePrinter.visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that the method works correctly when alternating with different attributes.
     */
    @Test
    public void testVisitLineNumberTableAttribute_alternatingWithOtherAttributes_doesNotInterfere() {
        // Arrange
        LineNumberTableAttribute attribute1 = new LineNumberTableAttribute();
        LineNumberTableAttribute attribute2 = new LineNumberTableAttribute();

        // Act - alternate calls
        changedCodePrinter.visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute1);
        changedCodePrinter.visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute2);
        changedCodePrinter.visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute1);

        // Assert
        verify(mockAttributeVisitor, times(2))
            .visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitLineNumberTableAttribute(clazz, method, codeAttribute, attribute2);
    }

    /**
     * Tests that the method works with all parameters being different instances.
     */
    @Test
    public void testVisitLineNumberTableAttribute_withAllDifferentParameters_delegatesCorrectly() {
        // Arrange
        Clazz clazz1 = new ProgramClass();
        Method method1 = new ProgramMethod();
        CodeAttribute codeAttribute1 = new CodeAttribute();
        LineNumberTableAttribute attribute1 = new LineNumberTableAttribute();

        Clazz clazz2 = new ProgramClass();
        Method method2 = new ProgramMethod();
        CodeAttribute codeAttribute2 = new CodeAttribute();
        LineNumberTableAttribute attribute2 = new LineNumberTableAttribute();

        // Act
        changedCodePrinter.visitLineNumberTableAttribute(clazz1, method1, codeAttribute1, attribute1);
        changedCodePrinter.visitLineNumberTableAttribute(clazz2, method2, codeAttribute2, attribute2);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitLineNumberTableAttribute(clazz1, method1, codeAttribute1, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitLineNumberTableAttribute(clazz2, method2, codeAttribute2, attribute2);
    }
}
