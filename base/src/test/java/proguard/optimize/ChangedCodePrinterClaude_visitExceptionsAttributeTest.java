package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.ExceptionsAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ChangedCodePrinter#visitExceptionsAttribute(Clazz, Method, ExceptionsAttribute)}.
 *
 * The visitExceptionsAttribute method in ChangedCodePrinter is a delegation method.
 * It simply forwards the call to the wrapped AttributeVisitor without any additional logic,
 * as ExceptionsAttribute does not contain bytecode that needs change detection.
 *
 * These tests verify that the method:
 * 1. Correctly delegates to the wrapped visitor
 * 2. Passes the correct parameters
 * 3. Works with various inputs including edge cases
 */
public class ChangedCodePrinterClaude_visitExceptionsAttributeTest {

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
     * Tests that visitExceptionsAttribute delegates to the wrapped visitor.
     * Verifies that the method calls the visitor with the correct parameters.
     */
    @Test
    public void testVisitExceptionsAttribute_delegatesToWrappedVisitor() {
        // Arrange
        ExceptionsAttribute attribute = new ExceptionsAttribute();

        // Act
        changedCodePrinter.visitExceptionsAttribute(clazz, method, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitExceptionsAttribute(clazz, method, attribute);
    }

    /**
     * Tests that the method does not throw an exception with valid inputs.
     */
    @Test
    public void testVisitExceptionsAttribute_withValidInputs_doesNotThrow() {
        // Arrange
        ExceptionsAttribute attribute = new ExceptionsAttribute();

        // Act & Assert
        assertDoesNotThrow(() ->
            changedCodePrinter.visitExceptionsAttribute(clazz, method, attribute),
            "visitExceptionsAttribute should not throw any exception");
    }

    /**
     * Tests that the method can be called multiple times without issues.
     */
    @Test
    public void testVisitExceptionsAttribute_calledMultipleTimes_doesNotThrow() {
        // Arrange
        ExceptionsAttribute attribute = new ExceptionsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitExceptionsAttribute(clazz, method, attribute);
            changedCodePrinter.visitExceptionsAttribute(clazz, method, attribute);
            changedCodePrinter.visitExceptionsAttribute(clazz, method, attribute);
        }, "Multiple calls should not throw any exception");

        // Verify the visitor was called multiple times
        verify(mockAttributeVisitor, times(3))
            .visitExceptionsAttribute(clazz, method, attribute);
    }

    /**
     * Tests that the method works with different Clazz instances.
     */
    @Test
    public void testVisitExceptionsAttribute_withDifferentClasses_delegatesCorrectly() {
        // Arrange
        Clazz clazz1 = new ProgramClass();
        Clazz clazz2 = new ProgramClass();
        ExceptionsAttribute attribute = new ExceptionsAttribute();

        // Act
        changedCodePrinter.visitExceptionsAttribute(clazz1, method, attribute);
        changedCodePrinter.visitExceptionsAttribute(clazz2, method, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitExceptionsAttribute(clazz1, method, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitExceptionsAttribute(clazz2, method, attribute);
    }

    /**
     * Tests that the method works with different Method instances.
     */
    @Test
    public void testVisitExceptionsAttribute_withDifferentMethods_delegatesCorrectly() {
        // Arrange
        Method method1 = new ProgramMethod();
        Method method2 = new ProgramMethod();
        ExceptionsAttribute attribute = new ExceptionsAttribute();

        // Act
        changedCodePrinter.visitExceptionsAttribute(clazz, method1, attribute);
        changedCodePrinter.visitExceptionsAttribute(clazz, method2, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitExceptionsAttribute(clazz, method1, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitExceptionsAttribute(clazz, method2, attribute);
    }

    /**
     * Tests that the method works with different ExceptionsAttribute instances.
     */
    @Test
    public void testVisitExceptionsAttribute_withDifferentAttributes_delegatesCorrectly() {
        // Arrange
        ExceptionsAttribute attribute1 = new ExceptionsAttribute();
        ExceptionsAttribute attribute2 = new ExceptionsAttribute();

        // Act
        changedCodePrinter.visitExceptionsAttribute(clazz, method, attribute1);
        changedCodePrinter.visitExceptionsAttribute(clazz, method, attribute2);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitExceptionsAttribute(clazz, method, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitExceptionsAttribute(clazz, method, attribute2);
    }

    /**
     * Tests that the method passes parameters in the correct order.
     */
    @Test
    public void testVisitExceptionsAttribute_passesParametersInCorrectOrder() {
        // Arrange
        ExceptionsAttribute attribute = new ExceptionsAttribute();

        // Act
        changedCodePrinter.visitExceptionsAttribute(clazz, method, attribute);

        // Assert - verify the parameters are in correct order
        verify(mockAttributeVisitor).visitExceptionsAttribute(
            argThat(arg -> arg == clazz),
            argThat(arg -> arg == method),
            argThat(arg -> arg == attribute)
        );
    }

    /**
     * Tests that the method can be called in rapid succession.
     */
    @Test
    public void testVisitExceptionsAttribute_rapidSuccessiveCalls_doesNotThrow() {
        // Arrange
        ExceptionsAttribute attribute = new ExceptionsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                changedCodePrinter.visitExceptionsAttribute(clazz, method, attribute);
            }
        }, "Rapid successive calls should not throw any exception");

        // Verify the visitor was called 100 times
        verify(mockAttributeVisitor, times(100))
            .visitExceptionsAttribute(clazz, method, attribute);
    }

    /**
     * Tests that the method works when the wrapped visitor does nothing (no-op).
     */
    @Test
    public void testVisitExceptionsAttribute_withNoOpVisitor_doesNotThrow() {
        // Arrange
        AttributeVisitor noOpVisitor = mock(AttributeVisitor.class);
        doNothing().when(noOpVisitor).visitExceptionsAttribute(any(), any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(noOpVisitor);
        ExceptionsAttribute attribute = new ExceptionsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> printer.visitExceptionsAttribute(clazz, method, attribute),
            "Should not throw when visitor is no-op");
    }

    /**
     * Tests that the method works when the wrapped visitor throws an exception.
     */
    @Test
    public void testVisitExceptionsAttribute_whenVisitorThrows_propagatesException() {
        // Arrange
        AttributeVisitor throwingVisitor = mock(AttributeVisitor.class);
        RuntimeException expectedException = new RuntimeException("Test exception");
        doThrow(expectedException)
            .when(throwingVisitor)
            .visitExceptionsAttribute(any(), any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(throwingVisitor);
        ExceptionsAttribute attribute = new ExceptionsAttribute();

        // Act & Assert
        RuntimeException thrownException = assertThrows(RuntimeException.class, () ->
            printer.visitExceptionsAttribute(clazz, method, attribute),
            "Should propagate exception from wrapped visitor");

        assertEquals(expectedException, thrownException, "Should throw the same exception");
    }

    /**
     * Tests that multiple ChangedCodePrinter instances work independently.
     */
    @Test
    public void testVisitExceptionsAttribute_multipleInstances_workIndependently() {
        // Arrange
        AttributeVisitor visitor1 = mock(AttributeVisitor.class);
        AttributeVisitor visitor2 = mock(AttributeVisitor.class);
        ChangedCodePrinter printer1 = new ChangedCodePrinter(visitor1);
        ChangedCodePrinter printer2 = new ChangedCodePrinter(visitor2);
        ExceptionsAttribute attribute = new ExceptionsAttribute();

        // Act
        printer1.visitExceptionsAttribute(clazz, method, attribute);
        printer2.visitExceptionsAttribute(clazz, method, attribute);

        // Assert
        verify(visitor1, times(1)).visitExceptionsAttribute(clazz, method, attribute);
        verify(visitor2, times(1)).visitExceptionsAttribute(clazz, method, attribute);
        verifyNoMoreInteractions(visitor1, visitor2);
    }

    /**
     * Tests that the printer can be reused after calling visitExceptionsAttribute.
     */
    @Test
    public void testVisitExceptionsAttribute_printerReusable() {
        // Arrange
        ExceptionsAttribute attribute1 = new ExceptionsAttribute();
        ExceptionsAttribute attribute2 = new ExceptionsAttribute();

        // Act & Assert - reuse the same printer
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitExceptionsAttribute(clazz, method, attribute1);
            changedCodePrinter.visitExceptionsAttribute(clazz, method, attribute2);
            changedCodePrinter.visitExceptionsAttribute(clazz, method, attribute1);
        }, "Printer should be reusable");

        verify(mockAttributeVisitor, times(2))
            .visitExceptionsAttribute(clazz, method, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitExceptionsAttribute(clazz, method, attribute2);
    }

    /**
     * Tests that the method delegates exactly once per call.
     */
    @Test
    public void testVisitExceptionsAttribute_delegatesExactlyOnce() {
        // Arrange
        ExceptionsAttribute attribute = new ExceptionsAttribute();

        // Act
        changedCodePrinter.visitExceptionsAttribute(clazz, method, attribute);

        // Assert - should delegate exactly once, no more, no less
        verify(mockAttributeVisitor, times(1))
            .visitExceptionsAttribute(any(), any(), any());
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method doesn't call any other visitor methods.
     */
    @Test
    public void testVisitExceptionsAttribute_doesNotCallOtherVisitorMethods() {
        // Arrange
        ExceptionsAttribute attribute = new ExceptionsAttribute();

        // Act
        changedCodePrinter.visitExceptionsAttribute(clazz, method, attribute);

        // Assert - verify only visitExceptionsAttribute was called
        verify(mockAttributeVisitor, times(1))
            .visitExceptionsAttribute(clazz, method, attribute);
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method returns normally (doesn't hang or loop).
     */
    @Test
    public void testVisitExceptionsAttribute_returnsImmediately() {
        // Arrange
        ExceptionsAttribute attribute = new ExceptionsAttribute();

        // Act
        long startTime = System.nanoTime();
        changedCodePrinter.visitExceptionsAttribute(clazz, method, attribute);
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
    public void testVisitExceptionsAttribute_withFreshPrinter_doesNotThrow() {
        // Arrange
        AttributeVisitor visitor = mock(AttributeVisitor.class);
        ChangedCodePrinter freshPrinter = new ChangedCodePrinter(visitor);
        ExceptionsAttribute attribute = new ExceptionsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> freshPrinter.visitExceptionsAttribute(clazz, method, attribute),
                "Method should work with a newly created printer");
    }

    /**
     * Tests that the same attribute can be visited multiple times with the same clazz and method.
     */
    @Test
    public void testVisitExceptionsAttribute_sameAttributeMultipleTimes() {
        // Arrange
        ExceptionsAttribute attribute = new ExceptionsAttribute();

        // Act
        changedCodePrinter.visitExceptionsAttribute(clazz, method, attribute);
        changedCodePrinter.visitExceptionsAttribute(clazz, method, attribute);
        changedCodePrinter.visitExceptionsAttribute(clazz, method, attribute);

        // Assert
        verify(mockAttributeVisitor, times(3))
            .visitExceptionsAttribute(clazz, method, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the attribute.
     */
    @Test
    public void testVisitExceptionsAttribute_doesNotModifyAttribute() {
        // Arrange
        ExceptionsAttribute attribute = new ExceptionsAttribute();

        // Act
        changedCodePrinter.visitExceptionsAttribute(clazz, method, attribute);

        // Assert - verify the call succeeded and delegated properly
        verify(mockAttributeVisitor, times(1))
            .visitExceptionsAttribute(clazz, method, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the clazz.
     */
    @Test
    public void testVisitExceptionsAttribute_doesNotModifyClazz() {
        // Arrange
        ExceptionsAttribute attribute = new ExceptionsAttribute();

        // Act
        changedCodePrinter.visitExceptionsAttribute(clazz, method, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitExceptionsAttribute(clazz, method, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the method.
     */
    @Test
    public void testVisitExceptionsAttribute_doesNotModifyMethod() {
        // Arrange
        ExceptionsAttribute attribute = new ExceptionsAttribute();

        // Act
        changedCodePrinter.visitExceptionsAttribute(clazz, method, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitExceptionsAttribute(clazz, method, attribute);
    }

    /**
     * Tests that the method works correctly when alternating with other visitor methods.
     */
    @Test
    public void testVisitExceptionsAttribute_alternatingWithOtherMethods_doesNotInterfere() {
        // Arrange
        ExceptionsAttribute attribute1 = new ExceptionsAttribute();
        ExceptionsAttribute attribute2 = new ExceptionsAttribute();

        // Act - alternate calls
        changedCodePrinter.visitExceptionsAttribute(clazz, method, attribute1);
        changedCodePrinter.visitExceptionsAttribute(clazz, method, attribute2);
        changedCodePrinter.visitExceptionsAttribute(clazz, method, attribute1);

        // Assert
        verify(mockAttributeVisitor, times(2))
            .visitExceptionsAttribute(clazz, method, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitExceptionsAttribute(clazz, method, attribute2);
    }
}
