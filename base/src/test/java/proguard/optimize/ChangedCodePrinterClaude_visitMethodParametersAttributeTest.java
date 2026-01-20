package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.MethodParametersAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ChangedCodePrinter#visitMethodParametersAttribute(Clazz, Method, MethodParametersAttribute)}.
 *
 * The visitMethodParametersAttribute method in ChangedCodePrinter is a delegation method.
 * It simply forwards the call to the wrapped AttributeVisitor without any additional logic,
 * as MethodParametersAttribute does not contain bytecode that needs change detection.
 *
 * These tests verify that the method:
 * 1. Correctly delegates to the wrapped visitor
 * 2. Passes the correct parameters
 * 3. Works with various inputs including edge cases
 */
public class ChangedCodePrinterClaude_visitMethodParametersAttributeTest {

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
     * Tests that visitMethodParametersAttribute delegates to the wrapped visitor.
     * Verifies that the method calls the visitor with the correct parameters.
     */
    @Test
    public void testVisitMethodParametersAttribute_delegatesToWrappedVisitor() {
        // Arrange
        MethodParametersAttribute attribute = new MethodParametersAttribute();

        // Act
        changedCodePrinter.visitMethodParametersAttribute(clazz, method, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitMethodParametersAttribute(clazz, method, attribute);
    }

    /**
     * Tests that the method does not throw an exception with valid inputs.
     */
    @Test
    public void testVisitMethodParametersAttribute_withValidInputs_doesNotThrow() {
        // Arrange
        MethodParametersAttribute attribute = new MethodParametersAttribute();

        // Act & Assert
        assertDoesNotThrow(() ->
            changedCodePrinter.visitMethodParametersAttribute(clazz, method, attribute),
            "visitMethodParametersAttribute should not throw any exception");
    }

    /**
     * Tests that the method can be called multiple times without issues.
     */
    @Test
    public void testVisitMethodParametersAttribute_calledMultipleTimes_doesNotThrow() {
        // Arrange
        MethodParametersAttribute attribute = new MethodParametersAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitMethodParametersAttribute(clazz, method, attribute);
            changedCodePrinter.visitMethodParametersAttribute(clazz, method, attribute);
            changedCodePrinter.visitMethodParametersAttribute(clazz, method, attribute);
        }, "Multiple calls should not throw any exception");

        // Verify the visitor was called multiple times
        verify(mockAttributeVisitor, times(3))
            .visitMethodParametersAttribute(clazz, method, attribute);
    }

    /**
     * Tests that the method works with different Clazz instances.
     */
    @Test
    public void testVisitMethodParametersAttribute_withDifferentClasses_delegatesCorrectly() {
        // Arrange
        Clazz clazz1 = new ProgramClass();
        Clazz clazz2 = new ProgramClass();
        MethodParametersAttribute attribute = new MethodParametersAttribute();

        // Act
        changedCodePrinter.visitMethodParametersAttribute(clazz1, method, attribute);
        changedCodePrinter.visitMethodParametersAttribute(clazz2, method, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitMethodParametersAttribute(clazz1, method, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitMethodParametersAttribute(clazz2, method, attribute);
    }

    /**
     * Tests that the method works with different Method instances.
     */
    @Test
    public void testVisitMethodParametersAttribute_withDifferentMethods_delegatesCorrectly() {
        // Arrange
        Method method1 = new ProgramMethod();
        Method method2 = new ProgramMethod();
        MethodParametersAttribute attribute = new MethodParametersAttribute();

        // Act
        changedCodePrinter.visitMethodParametersAttribute(clazz, method1, attribute);
        changedCodePrinter.visitMethodParametersAttribute(clazz, method2, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitMethodParametersAttribute(clazz, method1, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitMethodParametersAttribute(clazz, method2, attribute);
    }

    /**
     * Tests that the method works with different MethodParametersAttribute instances.
     */
    @Test
    public void testVisitMethodParametersAttribute_withDifferentAttributes_delegatesCorrectly() {
        // Arrange
        MethodParametersAttribute attribute1 = new MethodParametersAttribute();
        MethodParametersAttribute attribute2 = new MethodParametersAttribute();

        // Act
        changedCodePrinter.visitMethodParametersAttribute(clazz, method, attribute1);
        changedCodePrinter.visitMethodParametersAttribute(clazz, method, attribute2);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitMethodParametersAttribute(clazz, method, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitMethodParametersAttribute(clazz, method, attribute2);
    }

    /**
     * Tests that the method passes parameters in the correct order.
     */
    @Test
    public void testVisitMethodParametersAttribute_passesParametersInCorrectOrder() {
        // Arrange
        MethodParametersAttribute attribute = new MethodParametersAttribute();

        // Act
        changedCodePrinter.visitMethodParametersAttribute(clazz, method, attribute);

        // Assert - verify the parameters are in correct order
        verify(mockAttributeVisitor).visitMethodParametersAttribute(
            argThat(arg -> arg == clazz),
            argThat(arg -> arg == method),
            argThat(arg -> arg == attribute)
        );
    }

    /**
     * Tests that the method can be called in rapid succession.
     */
    @Test
    public void testVisitMethodParametersAttribute_rapidSuccessiveCalls_doesNotThrow() {
        // Arrange
        MethodParametersAttribute attribute = new MethodParametersAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                changedCodePrinter.visitMethodParametersAttribute(clazz, method, attribute);
            }
        }, "Rapid successive calls should not throw any exception");

        // Verify the visitor was called 100 times
        verify(mockAttributeVisitor, times(100))
            .visitMethodParametersAttribute(clazz, method, attribute);
    }

    /**
     * Tests that the method works when the wrapped visitor does nothing (no-op).
     */
    @Test
    public void testVisitMethodParametersAttribute_withNoOpVisitor_doesNotThrow() {
        // Arrange
        AttributeVisitor noOpVisitor = mock(AttributeVisitor.class);
        doNothing().when(noOpVisitor).visitMethodParametersAttribute(any(), any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(noOpVisitor);
        MethodParametersAttribute attribute = new MethodParametersAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> printer.visitMethodParametersAttribute(clazz, method, attribute),
            "Should not throw when visitor is no-op");
    }

    /**
     * Tests that the method works when the wrapped visitor throws an exception.
     */
    @Test
    public void testVisitMethodParametersAttribute_whenVisitorThrows_propagatesException() {
        // Arrange
        AttributeVisitor throwingVisitor = mock(AttributeVisitor.class);
        RuntimeException expectedException = new RuntimeException("Test exception");
        doThrow(expectedException)
            .when(throwingVisitor)
            .visitMethodParametersAttribute(any(), any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(throwingVisitor);
        MethodParametersAttribute attribute = new MethodParametersAttribute();

        // Act & Assert
        RuntimeException thrownException = assertThrows(RuntimeException.class, () ->
            printer.visitMethodParametersAttribute(clazz, method, attribute),
            "Should propagate exception from wrapped visitor");

        assertEquals(expectedException, thrownException, "Should throw the same exception");
    }

    /**
     * Tests that multiple ChangedCodePrinter instances work independently.
     */
    @Test
    public void testVisitMethodParametersAttribute_multipleInstances_workIndependently() {
        // Arrange
        AttributeVisitor visitor1 = mock(AttributeVisitor.class);
        AttributeVisitor visitor2 = mock(AttributeVisitor.class);
        ChangedCodePrinter printer1 = new ChangedCodePrinter(visitor1);
        ChangedCodePrinter printer2 = new ChangedCodePrinter(visitor2);
        MethodParametersAttribute attribute = new MethodParametersAttribute();

        // Act
        printer1.visitMethodParametersAttribute(clazz, method, attribute);
        printer2.visitMethodParametersAttribute(clazz, method, attribute);

        // Assert
        verify(visitor1, times(1)).visitMethodParametersAttribute(clazz, method, attribute);
        verify(visitor2, times(1)).visitMethodParametersAttribute(clazz, method, attribute);
        verifyNoMoreInteractions(visitor1, visitor2);
    }

    /**
     * Tests that the printer can be reused after calling visitMethodParametersAttribute.
     */
    @Test
    public void testVisitMethodParametersAttribute_printerReusable() {
        // Arrange
        MethodParametersAttribute attribute1 = new MethodParametersAttribute();
        MethodParametersAttribute attribute2 = new MethodParametersAttribute();

        // Act & Assert - reuse the same printer
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitMethodParametersAttribute(clazz, method, attribute1);
            changedCodePrinter.visitMethodParametersAttribute(clazz, method, attribute2);
            changedCodePrinter.visitMethodParametersAttribute(clazz, method, attribute1);
        }, "Printer should be reusable");

        verify(mockAttributeVisitor, times(2))
            .visitMethodParametersAttribute(clazz, method, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitMethodParametersAttribute(clazz, method, attribute2);
    }

    /**
     * Tests that the method delegates exactly once per call.
     */
    @Test
    public void testVisitMethodParametersAttribute_delegatesExactlyOnce() {
        // Arrange
        MethodParametersAttribute attribute = new MethodParametersAttribute();

        // Act
        changedCodePrinter.visitMethodParametersAttribute(clazz, method, attribute);

        // Assert - should delegate exactly once, no more, no less
        verify(mockAttributeVisitor, times(1))
            .visitMethodParametersAttribute(any(), any(), any());
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method doesn't call any other visitor methods.
     */
    @Test
    public void testVisitMethodParametersAttribute_doesNotCallOtherVisitorMethods() {
        // Arrange
        MethodParametersAttribute attribute = new MethodParametersAttribute();

        // Act
        changedCodePrinter.visitMethodParametersAttribute(clazz, method, attribute);

        // Assert - verify only visitMethodParametersAttribute was called
        verify(mockAttributeVisitor, times(1))
            .visitMethodParametersAttribute(clazz, method, attribute);
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method returns normally (doesn't hang or loop).
     */
    @Test
    public void testVisitMethodParametersAttribute_returnsImmediately() {
        // Arrange
        MethodParametersAttribute attribute = new MethodParametersAttribute();

        // Act
        long startTime = System.nanoTime();
        changedCodePrinter.visitMethodParametersAttribute(clazz, method, attribute);
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
    public void testVisitMethodParametersAttribute_withFreshPrinter_doesNotThrow() {
        // Arrange
        AttributeVisitor visitor = mock(AttributeVisitor.class);
        ChangedCodePrinter freshPrinter = new ChangedCodePrinter(visitor);
        MethodParametersAttribute attribute = new MethodParametersAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> freshPrinter.visitMethodParametersAttribute(clazz, method, attribute),
                "Method should work with a newly created printer");
    }

    /**
     * Tests that the same attribute can be visited multiple times with the same clazz and method.
     */
    @Test
    public void testVisitMethodParametersAttribute_sameAttributeMultipleTimes() {
        // Arrange
        MethodParametersAttribute attribute = new MethodParametersAttribute();

        // Act
        changedCodePrinter.visitMethodParametersAttribute(clazz, method, attribute);
        changedCodePrinter.visitMethodParametersAttribute(clazz, method, attribute);
        changedCodePrinter.visitMethodParametersAttribute(clazz, method, attribute);

        // Assert
        verify(mockAttributeVisitor, times(3))
            .visitMethodParametersAttribute(clazz, method, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the attribute.
     */
    @Test
    public void testVisitMethodParametersAttribute_doesNotModifyAttribute() {
        // Arrange
        MethodParametersAttribute attribute = new MethodParametersAttribute();

        // Act
        changedCodePrinter.visitMethodParametersAttribute(clazz, method, attribute);

        // Assert - verify the call succeeded and delegated properly
        verify(mockAttributeVisitor, times(1))
            .visitMethodParametersAttribute(clazz, method, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the clazz.
     */
    @Test
    public void testVisitMethodParametersAttribute_doesNotModifyClazz() {
        // Arrange
        MethodParametersAttribute attribute = new MethodParametersAttribute();

        // Act
        changedCodePrinter.visitMethodParametersAttribute(clazz, method, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitMethodParametersAttribute(clazz, method, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the method.
     */
    @Test
    public void testVisitMethodParametersAttribute_doesNotModifyMethod() {
        // Arrange
        MethodParametersAttribute attribute = new MethodParametersAttribute();

        // Act
        changedCodePrinter.visitMethodParametersAttribute(clazz, method, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitMethodParametersAttribute(clazz, method, attribute);
    }

    /**
     * Tests that the method works correctly when alternating with other visitor methods.
     */
    @Test
    public void testVisitMethodParametersAttribute_alternatingWithOtherMethods_doesNotInterfere() {
        // Arrange
        MethodParametersAttribute attribute1 = new MethodParametersAttribute();
        MethodParametersAttribute attribute2 = new MethodParametersAttribute();

        // Act - alternate calls
        changedCodePrinter.visitMethodParametersAttribute(clazz, method, attribute1);
        changedCodePrinter.visitMethodParametersAttribute(clazz, method, attribute2);
        changedCodePrinter.visitMethodParametersAttribute(clazz, method, attribute1);

        // Assert
        verify(mockAttributeVisitor, times(2))
            .visitMethodParametersAttribute(clazz, method, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitMethodParametersAttribute(clazz, method, attribute2);
    }
}
