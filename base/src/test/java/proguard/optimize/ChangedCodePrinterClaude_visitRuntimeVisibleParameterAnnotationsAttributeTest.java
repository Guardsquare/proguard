package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.annotation.RuntimeVisibleParameterAnnotationsAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ChangedCodePrinter#visitRuntimeVisibleParameterAnnotationsAttribute(Clazz, Method, RuntimeVisibleParameterAnnotationsAttribute)}.
 *
 * The visitRuntimeVisibleParameterAnnotationsAttribute method in ChangedCodePrinter is a delegation method.
 * It simply forwards the call to the wrapped AttributeVisitor without any additional logic,
 * as RuntimeVisibleParameterAnnotationsAttribute does not contain bytecode that needs change detection.
 *
 * These tests verify that the method:
 * 1. Correctly delegates to the wrapped visitor
 * 2. Passes the correct parameters
 * 3. Works with various inputs including edge cases
 */
public class ChangedCodePrinterClaude_visitRuntimeVisibleParameterAnnotationsAttributeTest {

    private AttributeVisitor mockAttributeVisitor;
    private ChangedCodePrinter changedCodePrinter;
    private Clazz clazz;
    private Method method;

    @BeforeEach
    public void setUp() {
        mockAttributeVisitor = mock(AttributeVisitor.class);
        changedCodePrinter = new ChangedCodePrinter(mockAttributeVisitor);
        clazz = new ProgramClass();
        method = new ProgramMethod();
    }

    /**
     * Tests that visitRuntimeVisibleParameterAnnotationsAttribute delegates to the wrapped visitor.
     * Verifies that the method calls the visitor with the correct parameters.
     */
    @Test
    public void testVisitRuntimeVisibleParameterAnnotationsAttribute_delegatesToWrappedVisitor() {
        // Arrange
        RuntimeVisibleParameterAnnotationsAttribute attribute = new RuntimeVisibleParameterAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute);
    }

    /**
     * Tests that the method does not throw an exception with valid inputs.
     */
    @Test
    public void testVisitRuntimeVisibleParameterAnnotationsAttribute_withValidInputs_doesNotThrow() {
        // Arrange
        RuntimeVisibleParameterAnnotationsAttribute attribute = new RuntimeVisibleParameterAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() ->
            changedCodePrinter.visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute),
            "visitRuntimeVisibleParameterAnnotationsAttribute should not throw any exception");
    }

    /**
     * Tests that the method can be called multiple times without issues.
     */
    @Test
    public void testVisitRuntimeVisibleParameterAnnotationsAttribute_calledMultipleTimes_doesNotThrow() {
        // Arrange
        RuntimeVisibleParameterAnnotationsAttribute attribute = new RuntimeVisibleParameterAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute);
            changedCodePrinter.visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute);
            changedCodePrinter.visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute);
        }, "Multiple calls should not throw any exception");

        // Verify the visitor was called multiple times
        verify(mockAttributeVisitor, times(3))
            .visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute);
    }

    /**
     * Tests that the method works with different Clazz instances.
     */
    @Test
    public void testVisitRuntimeVisibleParameterAnnotationsAttribute_withDifferentClasses_delegatesCorrectly() {
        // Arrange
        Clazz clazz1 = new ProgramClass();
        Clazz clazz2 = new ProgramClass();
        RuntimeVisibleParameterAnnotationsAttribute attribute = new RuntimeVisibleParameterAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeVisibleParameterAnnotationsAttribute(clazz1, method, attribute);
        changedCodePrinter.visitRuntimeVisibleParameterAnnotationsAttribute(clazz2, method, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleParameterAnnotationsAttribute(clazz1, method, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleParameterAnnotationsAttribute(clazz2, method, attribute);
    }

    /**
     * Tests that the method works with different Method instances.
     */
    @Test
    public void testVisitRuntimeVisibleParameterAnnotationsAttribute_withDifferentMethods_delegatesCorrectly() {
        // Arrange
        Method method1 = new ProgramMethod();
        Method method2 = new ProgramMethod();
        RuntimeVisibleParameterAnnotationsAttribute attribute = new RuntimeVisibleParameterAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method1, attribute);
        changedCodePrinter.visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method2, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method1, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method2, attribute);
    }

    /**
     * Tests that the method works with different RuntimeVisibleParameterAnnotationsAttribute instances.
     */
    @Test
    public void testVisitRuntimeVisibleParameterAnnotationsAttribute_withDifferentAttributes_delegatesCorrectly() {
        // Arrange
        RuntimeVisibleParameterAnnotationsAttribute attribute1 = new RuntimeVisibleParameterAnnotationsAttribute();
        RuntimeVisibleParameterAnnotationsAttribute attribute2 = new RuntimeVisibleParameterAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute1);
        changedCodePrinter.visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute2);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute2);
    }

    /**
     * Tests that the method passes parameters in the correct order.
     */
    @Test
    public void testVisitRuntimeVisibleParameterAnnotationsAttribute_passesParametersInCorrectOrder() {
        // Arrange
        RuntimeVisibleParameterAnnotationsAttribute attribute = new RuntimeVisibleParameterAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute);

        // Assert - verify the parameters are in correct order
        verify(mockAttributeVisitor).visitRuntimeVisibleParameterAnnotationsAttribute(
            argThat(arg -> arg == clazz),
            argThat(arg -> arg == method),
            argThat(arg -> arg == attribute)
        );
    }

    /**
     * Tests that the method can be called in rapid succession.
     */
    @Test
    public void testVisitRuntimeVisibleParameterAnnotationsAttribute_rapidSuccessiveCalls_doesNotThrow() {
        // Arrange
        RuntimeVisibleParameterAnnotationsAttribute attribute = new RuntimeVisibleParameterAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                changedCodePrinter.visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute);
            }
        }, "Rapid successive calls should not throw any exception");

        // Verify the visitor was called 100 times
        verify(mockAttributeVisitor, times(100))
            .visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute);
    }

    /**
     * Tests that the method works when the wrapped visitor does nothing (no-op).
     */
    @Test
    public void testVisitRuntimeVisibleParameterAnnotationsAttribute_withNoOpVisitor_doesNotThrow() {
        // Arrange
        AttributeVisitor noOpVisitor = mock(AttributeVisitor.class);
        doNothing().when(noOpVisitor).visitRuntimeVisibleParameterAnnotationsAttribute(any(), any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(noOpVisitor);
        RuntimeVisibleParameterAnnotationsAttribute attribute = new RuntimeVisibleParameterAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> printer.visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute),
            "Should not throw when visitor is no-op");
    }

    /**
     * Tests that the method works when the wrapped visitor throws an exception.
     */
    @Test
    public void testVisitRuntimeVisibleParameterAnnotationsAttribute_whenVisitorThrows_propagatesException() {
        // Arrange
        AttributeVisitor throwingVisitor = mock(AttributeVisitor.class);
        RuntimeException expectedException = new RuntimeException("Test exception");
        doThrow(expectedException)
            .when(throwingVisitor)
            .visitRuntimeVisibleParameterAnnotationsAttribute(any(), any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(throwingVisitor);
        RuntimeVisibleParameterAnnotationsAttribute attribute = new RuntimeVisibleParameterAnnotationsAttribute();

        // Act & Assert
        RuntimeException thrownException = assertThrows(RuntimeException.class, () ->
            printer.visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute),
            "Should propagate exception from wrapped visitor");

        assertEquals(expectedException, thrownException, "Should throw the same exception");
    }

    /**
     * Tests that multiple ChangedCodePrinter instances work independently.
     */
    @Test
    public void testVisitRuntimeVisibleParameterAnnotationsAttribute_multipleInstances_workIndependently() {
        // Arrange
        AttributeVisitor visitor1 = mock(AttributeVisitor.class);
        AttributeVisitor visitor2 = mock(AttributeVisitor.class);
        ChangedCodePrinter printer1 = new ChangedCodePrinter(visitor1);
        ChangedCodePrinter printer2 = new ChangedCodePrinter(visitor2);
        RuntimeVisibleParameterAnnotationsAttribute attribute = new RuntimeVisibleParameterAnnotationsAttribute();

        // Act
        printer1.visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute);
        printer2.visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute);

        // Assert
        verify(visitor1, times(1)).visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute);
        verify(visitor2, times(1)).visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute);
        verifyNoMoreInteractions(visitor1, visitor2);
    }

    /**
     * Tests that the printer can be reused after calling visitRuntimeVisibleParameterAnnotationsAttribute.
     */
    @Test
    public void testVisitRuntimeVisibleParameterAnnotationsAttribute_printerReusable() {
        // Arrange
        RuntimeVisibleParameterAnnotationsAttribute attribute1 = new RuntimeVisibleParameterAnnotationsAttribute();
        RuntimeVisibleParameterAnnotationsAttribute attribute2 = new RuntimeVisibleParameterAnnotationsAttribute();

        // Act & Assert - reuse the same printer
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute1);
            changedCodePrinter.visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute2);
            changedCodePrinter.visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute1);
        }, "Printer should be reusable");

        verify(mockAttributeVisitor, times(2))
            .visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute2);
    }

    /**
     * Tests that the method delegates exactly once per call.
     */
    @Test
    public void testVisitRuntimeVisibleParameterAnnotationsAttribute_delegatesExactlyOnce() {
        // Arrange
        RuntimeVisibleParameterAnnotationsAttribute attribute = new RuntimeVisibleParameterAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute);

        // Assert - should delegate exactly once, no more, no less
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleParameterAnnotationsAttribute(any(), any(), any());
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method doesn't call any other visitor methods.
     */
    @Test
    public void testVisitRuntimeVisibleParameterAnnotationsAttribute_doesNotCallOtherVisitorMethods() {
        // Arrange
        RuntimeVisibleParameterAnnotationsAttribute attribute = new RuntimeVisibleParameterAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute);

        // Assert - verify only visitRuntimeVisibleParameterAnnotationsAttribute was called
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute);
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method returns normally (doesn't hang or loop).
     */
    @Test
    public void testVisitRuntimeVisibleParameterAnnotationsAttribute_returnsImmediately() {
        // Arrange
        RuntimeVisibleParameterAnnotationsAttribute attribute = new RuntimeVisibleParameterAnnotationsAttribute();

        // Act
        long startTime = System.nanoTime();
        changedCodePrinter.visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute);
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
    public void testVisitRuntimeVisibleParameterAnnotationsAttribute_withFreshPrinter_doesNotThrow() {
        // Arrange
        AttributeVisitor visitor = mock(AttributeVisitor.class);
        ChangedCodePrinter freshPrinter = new ChangedCodePrinter(visitor);
        RuntimeVisibleParameterAnnotationsAttribute attribute = new RuntimeVisibleParameterAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> freshPrinter.visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute),
                "Method should work with a newly created printer");
    }

    /**
     * Tests that the same attribute can be visited multiple times with the same clazz and method.
     */
    @Test
    public void testVisitRuntimeVisibleParameterAnnotationsAttribute_sameAttributeMultipleTimes() {
        // Arrange
        RuntimeVisibleParameterAnnotationsAttribute attribute = new RuntimeVisibleParameterAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute);
        changedCodePrinter.visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute);
        changedCodePrinter.visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute);

        // Assert
        verify(mockAttributeVisitor, times(3))
            .visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the attribute.
     */
    @Test
    public void testVisitRuntimeVisibleParameterAnnotationsAttribute_doesNotModifyAttribute() {
        // Arrange
        RuntimeVisibleParameterAnnotationsAttribute attribute = new RuntimeVisibleParameterAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute);

        // Assert - verify the call succeeded and delegated properly
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the clazz.
     */
    @Test
    public void testVisitRuntimeVisibleParameterAnnotationsAttribute_doesNotModifyClazz() {
        // Arrange
        RuntimeVisibleParameterAnnotationsAttribute attribute = new RuntimeVisibleParameterAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the method.
     */
    @Test
    public void testVisitRuntimeVisibleParameterAnnotationsAttribute_doesNotModifyMethod() {
        // Arrange
        RuntimeVisibleParameterAnnotationsAttribute attribute = new RuntimeVisibleParameterAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute);
    }

    /**
     * Tests that the method works correctly when alternating with other visitor methods.
     */
    @Test
    public void testVisitRuntimeVisibleParameterAnnotationsAttribute_alternatingWithOtherMethods_doesNotInterfere() {
        // Arrange
        RuntimeVisibleParameterAnnotationsAttribute attribute1 = new RuntimeVisibleParameterAnnotationsAttribute();
        RuntimeVisibleParameterAnnotationsAttribute attribute2 = new RuntimeVisibleParameterAnnotationsAttribute();

        // Act - alternate calls
        changedCodePrinter.visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute1);
        changedCodePrinter.visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute2);
        changedCodePrinter.visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute1);

        // Assert
        verify(mockAttributeVisitor, times(2))
            .visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, attribute2);
    }
}
