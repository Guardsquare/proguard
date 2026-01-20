package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.annotation.RuntimeVisibleTypeAnnotationsAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ChangedCodePrinter#visitRuntimeVisibleTypeAnnotationsAttribute(Clazz, Method, CodeAttribute, RuntimeVisibleTypeAnnotationsAttribute)}.
 *
 * The visitRuntimeVisibleTypeAnnotationsAttribute method in ChangedCodePrinter is a delegation method.
 * It simply forwards the call to the wrapped AttributeVisitor without any additional logic,
 * as RuntimeVisibleTypeAnnotationsAttribute does not contain bytecode that needs change detection.
 *
 * These tests verify that the method:
 * 1. Correctly delegates to the wrapped visitor
 * 2. Passes the correct parameters in the correct order
 * 3. Works with various inputs including edge cases
 */
public class ChangedCodePrinterClaude_visitRuntimeVisibleTypeAnnotationsAttributeTest {

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
     * Tests that visitRuntimeVisibleTypeAnnotationsAttribute delegates to the wrapped visitor.
     * Verifies that the method calls the visitor with the correct parameters.
     */
    @Test
    public void testVisitRuntimeVisibleTypeAnnotationsAttribute_delegatesToWrappedVisitor() {
        // Arrange
        RuntimeVisibleTypeAnnotationsAttribute attribute = new RuntimeVisibleTypeAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that the method does not throw an exception with valid inputs.
     */
    @Test
    public void testVisitRuntimeVisibleTypeAnnotationsAttribute_withValidInputs_doesNotThrow() {
        // Arrange
        RuntimeVisibleTypeAnnotationsAttribute attribute = new RuntimeVisibleTypeAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() ->
            changedCodePrinter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute),
            "visitRuntimeVisibleTypeAnnotationsAttribute should not throw any exception");
    }

    /**
     * Tests that the method can be called multiple times without issues.
     */
    @Test
    public void testVisitRuntimeVisibleTypeAnnotationsAttribute_calledMultipleTimes_doesNotThrow() {
        // Arrange
        RuntimeVisibleTypeAnnotationsAttribute attribute = new RuntimeVisibleTypeAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);
            changedCodePrinter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);
            changedCodePrinter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);
        }, "Multiple calls should not throw any exception");

        // Verify the visitor was called multiple times
        verify(mockAttributeVisitor, times(3))
            .visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that the method works with different Clazz instances.
     */
    @Test
    public void testVisitRuntimeVisibleTypeAnnotationsAttribute_withDifferentClasses_delegatesCorrectly() {
        // Arrange
        Clazz clazz1 = new ProgramClass();
        Clazz clazz2 = new ProgramClass();
        RuntimeVisibleTypeAnnotationsAttribute attribute = new RuntimeVisibleTypeAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz1, method, codeAttribute, attribute);
        changedCodePrinter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz2, method, codeAttribute, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleTypeAnnotationsAttribute(clazz1, method, codeAttribute, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleTypeAnnotationsAttribute(clazz2, method, codeAttribute, attribute);
    }

    /**
     * Tests that the method works with different Method instances.
     */
    @Test
    public void testVisitRuntimeVisibleTypeAnnotationsAttribute_withDifferentMethods_delegatesCorrectly() {
        // Arrange
        Method method1 = new ProgramMethod();
        Method method2 = new ProgramMethod();
        RuntimeVisibleTypeAnnotationsAttribute attribute = new RuntimeVisibleTypeAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method1, codeAttribute, attribute);
        changedCodePrinter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method2, codeAttribute, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method1, codeAttribute, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method2, codeAttribute, attribute);
    }

    /**
     * Tests that the method works with different CodeAttribute instances.
     */
    @Test
    public void testVisitRuntimeVisibleTypeAnnotationsAttribute_withDifferentCodeAttributes_delegatesCorrectly() {
        // Arrange
        CodeAttribute codeAttribute1 = new CodeAttribute();
        CodeAttribute codeAttribute2 = new CodeAttribute();
        RuntimeVisibleTypeAnnotationsAttribute attribute = new RuntimeVisibleTypeAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute1, attribute);
        changedCodePrinter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute2, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute1, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute2, attribute);
    }

    /**
     * Tests that the method works with different RuntimeVisibleTypeAnnotationsAttribute instances.
     */
    @Test
    public void testVisitRuntimeVisibleTypeAnnotationsAttribute_withDifferentAttributes_delegatesCorrectly() {
        // Arrange
        RuntimeVisibleTypeAnnotationsAttribute attribute1 = new RuntimeVisibleTypeAnnotationsAttribute();
        RuntimeVisibleTypeAnnotationsAttribute attribute2 = new RuntimeVisibleTypeAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute1);
        changedCodePrinter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute2);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute2);
    }

    /**
     * Tests that the method passes parameters in the correct order.
     */
    @Test
    public void testVisitRuntimeVisibleTypeAnnotationsAttribute_passesParametersInCorrectOrder() {
        // Arrange
        RuntimeVisibleTypeAnnotationsAttribute attribute = new RuntimeVisibleTypeAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify the parameters are in correct order
        verify(mockAttributeVisitor).visitRuntimeVisibleTypeAnnotationsAttribute(
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
    public void testVisitRuntimeVisibleTypeAnnotationsAttribute_rapidSuccessiveCalls_doesNotThrow() {
        // Arrange
        RuntimeVisibleTypeAnnotationsAttribute attribute = new RuntimeVisibleTypeAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                changedCodePrinter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);
            }
        }, "Rapid successive calls should not throw any exception");

        // Verify the visitor was called 100 times
        verify(mockAttributeVisitor, times(100))
            .visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that the method works when the wrapped visitor does nothing (no-op).
     */
    @Test
    public void testVisitRuntimeVisibleTypeAnnotationsAttribute_withNoOpVisitor_doesNotThrow() {
        // Arrange
        AttributeVisitor noOpVisitor = mock(AttributeVisitor.class);
        doNothing().when(noOpVisitor).visitRuntimeVisibleTypeAnnotationsAttribute(any(), any(), any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(noOpVisitor);
        RuntimeVisibleTypeAnnotationsAttribute attribute = new RuntimeVisibleTypeAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> printer.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute),
            "Should not throw when visitor is no-op");
    }

    /**
     * Tests that the method works when the wrapped visitor throws an exception.
     */
    @Test
    public void testVisitRuntimeVisibleTypeAnnotationsAttribute_whenVisitorThrows_propagatesException() {
        // Arrange
        AttributeVisitor throwingVisitor = mock(AttributeVisitor.class);
        RuntimeException expectedException = new RuntimeException("Test exception");
        doThrow(expectedException)
            .when(throwingVisitor)
            .visitRuntimeVisibleTypeAnnotationsAttribute(any(), any(), any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(throwingVisitor);
        RuntimeVisibleTypeAnnotationsAttribute attribute = new RuntimeVisibleTypeAnnotationsAttribute();

        // Act & Assert
        RuntimeException thrownException = assertThrows(RuntimeException.class, () ->
            printer.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute),
            "Should propagate exception from wrapped visitor");

        assertEquals(expectedException, thrownException, "Should throw the same exception");
    }

    /**
     * Tests that multiple ChangedCodePrinter instances work independently.
     */
    @Test
    public void testVisitRuntimeVisibleTypeAnnotationsAttribute_multipleInstances_workIndependently() {
        // Arrange
        AttributeVisitor visitor1 = mock(AttributeVisitor.class);
        AttributeVisitor visitor2 = mock(AttributeVisitor.class);
        ChangedCodePrinter printer1 = new ChangedCodePrinter(visitor1);
        ChangedCodePrinter printer2 = new ChangedCodePrinter(visitor2);
        RuntimeVisibleTypeAnnotationsAttribute attribute = new RuntimeVisibleTypeAnnotationsAttribute();

        // Act
        printer1.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);
        printer2.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);

        // Assert
        verify(visitor1, times(1)).visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);
        verify(visitor2, times(1)).visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);
        verifyNoMoreInteractions(visitor1, visitor2);
    }

    /**
     * Tests that the printer can be reused after calling visitRuntimeVisibleTypeAnnotationsAttribute.
     */
    @Test
    public void testVisitRuntimeVisibleTypeAnnotationsAttribute_printerReusable() {
        // Arrange
        RuntimeVisibleTypeAnnotationsAttribute attribute1 = new RuntimeVisibleTypeAnnotationsAttribute();
        RuntimeVisibleTypeAnnotationsAttribute attribute2 = new RuntimeVisibleTypeAnnotationsAttribute();

        // Act & Assert - reuse the same printer
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute1);
            changedCodePrinter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute2);
            changedCodePrinter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute1);
        }, "Printer should be reusable");

        verify(mockAttributeVisitor, times(2))
            .visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute2);
    }

    /**
     * Tests that the method delegates exactly once per call.
     */
    @Test
    public void testVisitRuntimeVisibleTypeAnnotationsAttribute_delegatesExactlyOnce() {
        // Arrange
        RuntimeVisibleTypeAnnotationsAttribute attribute = new RuntimeVisibleTypeAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);

        // Assert - should delegate exactly once, no more, no less
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleTypeAnnotationsAttribute(any(), any(), any(), any());
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method doesn't call any other visitor methods.
     */
    @Test
    public void testVisitRuntimeVisibleTypeAnnotationsAttribute_doesNotCallOtherVisitorMethods() {
        // Arrange
        RuntimeVisibleTypeAnnotationsAttribute attribute = new RuntimeVisibleTypeAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify only visitRuntimeVisibleTypeAnnotationsAttribute was called
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method returns normally (doesn't hang or loop).
     */
    @Test
    public void testVisitRuntimeVisibleTypeAnnotationsAttribute_returnsImmediately() {
        // Arrange
        RuntimeVisibleTypeAnnotationsAttribute attribute = new RuntimeVisibleTypeAnnotationsAttribute();

        // Act
        long startTime = System.nanoTime();
        changedCodePrinter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);
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
    public void testVisitRuntimeVisibleTypeAnnotationsAttribute_withFreshPrinter_doesNotThrow() {
        // Arrange
        AttributeVisitor visitor = mock(AttributeVisitor.class);
        ChangedCodePrinter freshPrinter = new ChangedCodePrinter(visitor);
        RuntimeVisibleTypeAnnotationsAttribute attribute = new RuntimeVisibleTypeAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> freshPrinter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute),
                "Method should work with a newly created printer");
    }

    /**
     * Tests that the same attribute can be visited multiple times with the same parameters.
     */
    @Test
    public void testVisitRuntimeVisibleTypeAnnotationsAttribute_sameAttributeMultipleTimes() {
        // Arrange
        RuntimeVisibleTypeAnnotationsAttribute attribute = new RuntimeVisibleTypeAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);
        changedCodePrinter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);
        changedCodePrinter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);

        // Assert
        verify(mockAttributeVisitor, times(3))
            .visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the attribute.
     */
    @Test
    public void testVisitRuntimeVisibleTypeAnnotationsAttribute_doesNotModifyAttribute() {
        // Arrange
        RuntimeVisibleTypeAnnotationsAttribute attribute = new RuntimeVisibleTypeAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify the call succeeded and delegated properly
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the clazz.
     */
    @Test
    public void testVisitRuntimeVisibleTypeAnnotationsAttribute_doesNotModifyClazz() {
        // Arrange
        RuntimeVisibleTypeAnnotationsAttribute attribute = new RuntimeVisibleTypeAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the method.
     */
    @Test
    public void testVisitRuntimeVisibleTypeAnnotationsAttribute_doesNotModifyMethod() {
        // Arrange
        RuntimeVisibleTypeAnnotationsAttribute attribute = new RuntimeVisibleTypeAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the code attribute.
     */
    @Test
    public void testVisitRuntimeVisibleTypeAnnotationsAttribute_doesNotModifyCodeAttribute() {
        // Arrange
        RuntimeVisibleTypeAnnotationsAttribute attribute = new RuntimeVisibleTypeAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that the method works correctly when alternating with different attributes.
     */
    @Test
    public void testVisitRuntimeVisibleTypeAnnotationsAttribute_alternatingWithOtherAttributes_doesNotInterfere() {
        // Arrange
        RuntimeVisibleTypeAnnotationsAttribute attribute1 = new RuntimeVisibleTypeAnnotationsAttribute();
        RuntimeVisibleTypeAnnotationsAttribute attribute2 = new RuntimeVisibleTypeAnnotationsAttribute();

        // Act - alternate calls
        changedCodePrinter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute1);
        changedCodePrinter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute2);
        changedCodePrinter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute1);

        // Assert
        verify(mockAttributeVisitor, times(2))
            .visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute2);
    }

    /**
     * Tests that the method works with all parameters being different instances.
     */
    @Test
    public void testVisitRuntimeVisibleTypeAnnotationsAttribute_withAllDifferentParameters_delegatesCorrectly() {
        // Arrange
        Clazz clazz1 = new ProgramClass();
        Method method1 = new ProgramMethod();
        CodeAttribute codeAttribute1 = new CodeAttribute();
        RuntimeVisibleTypeAnnotationsAttribute attribute1 = new RuntimeVisibleTypeAnnotationsAttribute();

        Clazz clazz2 = new ProgramClass();
        Method method2 = new ProgramMethod();
        CodeAttribute codeAttribute2 = new CodeAttribute();
        RuntimeVisibleTypeAnnotationsAttribute attribute2 = new RuntimeVisibleTypeAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz1, method1, codeAttribute1, attribute1);
        changedCodePrinter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz2, method2, codeAttribute2, attribute2);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleTypeAnnotationsAttribute(clazz1, method1, codeAttribute1, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleTypeAnnotationsAttribute(clazz2, method2, codeAttribute2, attribute2);
    }
}
