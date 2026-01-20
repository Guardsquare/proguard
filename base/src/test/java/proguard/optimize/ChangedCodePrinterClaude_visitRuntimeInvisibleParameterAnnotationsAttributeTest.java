package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.annotation.RuntimeInvisibleParameterAnnotationsAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ChangedCodePrinter#visitRuntimeInvisibleParameterAnnotationsAttribute(Clazz, Method, RuntimeInvisibleParameterAnnotationsAttribute)}.
 *
 * The visitRuntimeInvisibleParameterAnnotationsAttribute method in ChangedCodePrinter is a delegation method.
 * It simply forwards the call to the wrapped AttributeVisitor without any additional logic,
 * as RuntimeInvisibleParameterAnnotationsAttribute does not contain bytecode that needs change detection.
 *
 * These tests verify that the method:
 * 1. Correctly delegates to the wrapped visitor
 * 2. Passes the correct parameters
 * 3. Works with various inputs including edge cases
 */
public class ChangedCodePrinterClaude_visitRuntimeInvisibleParameterAnnotationsAttributeTest {

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
     * Tests that visitRuntimeInvisibleParameterAnnotationsAttribute delegates to the wrapped visitor.
     * Verifies that the method calls the visitor with the correct parameters.
     */
    @Test
    public void testVisitRuntimeInvisibleParameterAnnotationsAttribute_delegatesToWrappedVisitor() {
        // Arrange
        RuntimeInvisibleParameterAnnotationsAttribute attribute = new RuntimeInvisibleParameterAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute);
    }

    /**
     * Tests that the method does not throw an exception with valid inputs.
     */
    @Test
    public void testVisitRuntimeInvisibleParameterAnnotationsAttribute_withValidInputs_doesNotThrow() {
        // Arrange
        RuntimeInvisibleParameterAnnotationsAttribute attribute = new RuntimeInvisibleParameterAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() ->
            changedCodePrinter.visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute),
            "visitRuntimeInvisibleParameterAnnotationsAttribute should not throw any exception");
    }

    /**
     * Tests that the method can be called multiple times without issues.
     */
    @Test
    public void testVisitRuntimeInvisibleParameterAnnotationsAttribute_calledMultipleTimes_doesNotThrow() {
        // Arrange
        RuntimeInvisibleParameterAnnotationsAttribute attribute = new RuntimeInvisibleParameterAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute);
            changedCodePrinter.visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute);
            changedCodePrinter.visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute);
        }, "Multiple calls should not throw any exception");

        // Verify the visitor was called multiple times
        verify(mockAttributeVisitor, times(3))
            .visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute);
    }

    /**
     * Tests that the method works with different Clazz instances.
     */
    @Test
    public void testVisitRuntimeInvisibleParameterAnnotationsAttribute_withDifferentClasses_delegatesCorrectly() {
        // Arrange
        Clazz clazz1 = new ProgramClass();
        Clazz clazz2 = new ProgramClass();
        RuntimeInvisibleParameterAnnotationsAttribute attribute = new RuntimeInvisibleParameterAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeInvisibleParameterAnnotationsAttribute(clazz1, method, attribute);
        changedCodePrinter.visitRuntimeInvisibleParameterAnnotationsAttribute(clazz2, method, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeInvisibleParameterAnnotationsAttribute(clazz1, method, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeInvisibleParameterAnnotationsAttribute(clazz2, method, attribute);
    }

    /**
     * Tests that the method works with different Method instances.
     */
    @Test
    public void testVisitRuntimeInvisibleParameterAnnotationsAttribute_withDifferentMethods_delegatesCorrectly() {
        // Arrange
        Method method1 = new ProgramMethod();
        Method method2 = new ProgramMethod();
        RuntimeInvisibleParameterAnnotationsAttribute attribute = new RuntimeInvisibleParameterAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method1, attribute);
        changedCodePrinter.visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method2, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method1, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method2, attribute);
    }

    /**
     * Tests that the method works with different RuntimeInvisibleParameterAnnotationsAttribute instances.
     */
    @Test
    public void testVisitRuntimeInvisibleParameterAnnotationsAttribute_withDifferentAttributes_delegatesCorrectly() {
        // Arrange
        RuntimeInvisibleParameterAnnotationsAttribute attribute1 = new RuntimeInvisibleParameterAnnotationsAttribute();
        RuntimeInvisibleParameterAnnotationsAttribute attribute2 = new RuntimeInvisibleParameterAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute1);
        changedCodePrinter.visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute2);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute2);
    }

    /**
     * Tests that the method passes parameters in the correct order.
     */
    @Test
    public void testVisitRuntimeInvisibleParameterAnnotationsAttribute_passesParametersInCorrectOrder() {
        // Arrange
        RuntimeInvisibleParameterAnnotationsAttribute attribute = new RuntimeInvisibleParameterAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute);

        // Assert - verify the parameters are in correct order
        verify(mockAttributeVisitor).visitRuntimeInvisibleParameterAnnotationsAttribute(
            argThat(arg -> arg == clazz),
            argThat(arg -> arg == method),
            argThat(arg -> arg == attribute)
        );
    }

    /**
     * Tests that the method can be called in rapid succession.
     */
    @Test
    public void testVisitRuntimeInvisibleParameterAnnotationsAttribute_rapidSuccessiveCalls_doesNotThrow() {
        // Arrange
        RuntimeInvisibleParameterAnnotationsAttribute attribute = new RuntimeInvisibleParameterAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                changedCodePrinter.visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute);
            }
        }, "Rapid successive calls should not throw any exception");

        // Verify the visitor was called 100 times
        verify(mockAttributeVisitor, times(100))
            .visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute);
    }

    /**
     * Tests that the method works when the wrapped visitor does nothing (no-op).
     */
    @Test
    public void testVisitRuntimeInvisibleParameterAnnotationsAttribute_withNoOpVisitor_doesNotThrow() {
        // Arrange
        AttributeVisitor noOpVisitor = mock(AttributeVisitor.class);
        doNothing().when(noOpVisitor).visitRuntimeInvisibleParameterAnnotationsAttribute(any(), any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(noOpVisitor);
        RuntimeInvisibleParameterAnnotationsAttribute attribute = new RuntimeInvisibleParameterAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> printer.visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute),
            "Should not throw when visitor is no-op");
    }

    /**
     * Tests that the method works when the wrapped visitor throws an exception.
     */
    @Test
    public void testVisitRuntimeInvisibleParameterAnnotationsAttribute_whenVisitorThrows_propagatesException() {
        // Arrange
        AttributeVisitor throwingVisitor = mock(AttributeVisitor.class);
        RuntimeException expectedException = new RuntimeException("Test exception");
        doThrow(expectedException)
            .when(throwingVisitor)
            .visitRuntimeInvisibleParameterAnnotationsAttribute(any(), any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(throwingVisitor);
        RuntimeInvisibleParameterAnnotationsAttribute attribute = new RuntimeInvisibleParameterAnnotationsAttribute();

        // Act & Assert
        RuntimeException thrownException = assertThrows(RuntimeException.class, () ->
            printer.visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute),
            "Should propagate exception from wrapped visitor");

        assertEquals(expectedException, thrownException, "Should throw the same exception");
    }

    /**
     * Tests that multiple ChangedCodePrinter instances work independently.
     */
    @Test
    public void testVisitRuntimeInvisibleParameterAnnotationsAttribute_multipleInstances_workIndependently() {
        // Arrange
        AttributeVisitor visitor1 = mock(AttributeVisitor.class);
        AttributeVisitor visitor2 = mock(AttributeVisitor.class);
        ChangedCodePrinter printer1 = new ChangedCodePrinter(visitor1);
        ChangedCodePrinter printer2 = new ChangedCodePrinter(visitor2);
        RuntimeInvisibleParameterAnnotationsAttribute attribute = new RuntimeInvisibleParameterAnnotationsAttribute();

        // Act
        printer1.visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute);
        printer2.visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute);

        // Assert
        verify(visitor1, times(1)).visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute);
        verify(visitor2, times(1)).visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute);
        verifyNoMoreInteractions(visitor1, visitor2);
    }

    /**
     * Tests that the printer can be reused after calling visitRuntimeInvisibleParameterAnnotationsAttribute.
     */
    @Test
    public void testVisitRuntimeInvisibleParameterAnnotationsAttribute_printerReusable() {
        // Arrange
        RuntimeInvisibleParameterAnnotationsAttribute attribute1 = new RuntimeInvisibleParameterAnnotationsAttribute();
        RuntimeInvisibleParameterAnnotationsAttribute attribute2 = new RuntimeInvisibleParameterAnnotationsAttribute();

        // Act & Assert - reuse the same printer
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute1);
            changedCodePrinter.visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute2);
            changedCodePrinter.visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute1);
        }, "Printer should be reusable");

        verify(mockAttributeVisitor, times(2))
            .visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute2);
    }

    /**
     * Tests that the method delegates exactly once per call.
     */
    @Test
    public void testVisitRuntimeInvisibleParameterAnnotationsAttribute_delegatesExactlyOnce() {
        // Arrange
        RuntimeInvisibleParameterAnnotationsAttribute attribute = new RuntimeInvisibleParameterAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute);

        // Assert - should delegate exactly once, no more, no less
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeInvisibleParameterAnnotationsAttribute(any(), any(), any());
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method doesn't call any other visitor methods.
     */
    @Test
    public void testVisitRuntimeInvisibleParameterAnnotationsAttribute_doesNotCallOtherVisitorMethods() {
        // Arrange
        RuntimeInvisibleParameterAnnotationsAttribute attribute = new RuntimeInvisibleParameterAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute);

        // Assert - verify only visitRuntimeInvisibleParameterAnnotationsAttribute was called
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute);
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method returns normally (doesn't hang or loop).
     */
    @Test
    public void testVisitRuntimeInvisibleParameterAnnotationsAttribute_returnsImmediately() {
        // Arrange
        RuntimeInvisibleParameterAnnotationsAttribute attribute = new RuntimeInvisibleParameterAnnotationsAttribute();

        // Act
        long startTime = System.nanoTime();
        changedCodePrinter.visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute);
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
    public void testVisitRuntimeInvisibleParameterAnnotationsAttribute_withFreshPrinter_doesNotThrow() {
        // Arrange
        AttributeVisitor visitor = mock(AttributeVisitor.class);
        ChangedCodePrinter freshPrinter = new ChangedCodePrinter(visitor);
        RuntimeInvisibleParameterAnnotationsAttribute attribute = new RuntimeInvisibleParameterAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> freshPrinter.visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute),
                "Method should work with a newly created printer");
    }

    /**
     * Tests that the same attribute can be visited multiple times with the same clazz and method.
     */
    @Test
    public void testVisitRuntimeInvisibleParameterAnnotationsAttribute_sameAttributeMultipleTimes() {
        // Arrange
        RuntimeInvisibleParameterAnnotationsAttribute attribute = new RuntimeInvisibleParameterAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute);
        changedCodePrinter.visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute);
        changedCodePrinter.visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute);

        // Assert
        verify(mockAttributeVisitor, times(3))
            .visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the attribute.
     */
    @Test
    public void testVisitRuntimeInvisibleParameterAnnotationsAttribute_doesNotModifyAttribute() {
        // Arrange
        RuntimeInvisibleParameterAnnotationsAttribute attribute = new RuntimeInvisibleParameterAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute);

        // Assert - verify the call succeeded and delegated properly
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the clazz.
     */
    @Test
    public void testVisitRuntimeInvisibleParameterAnnotationsAttribute_doesNotModifyClazz() {
        // Arrange
        RuntimeInvisibleParameterAnnotationsAttribute attribute = new RuntimeInvisibleParameterAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the method.
     */
    @Test
    public void testVisitRuntimeInvisibleParameterAnnotationsAttribute_doesNotModifyMethod() {
        // Arrange
        RuntimeInvisibleParameterAnnotationsAttribute attribute = new RuntimeInvisibleParameterAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute);
    }

    /**
     * Tests that the method works correctly when alternating with other visitor methods.
     */
    @Test
    public void testVisitRuntimeInvisibleParameterAnnotationsAttribute_alternatingWithOtherMethods_doesNotInterfere() {
        // Arrange
        RuntimeInvisibleParameterAnnotationsAttribute attribute1 = new RuntimeInvisibleParameterAnnotationsAttribute();
        RuntimeInvisibleParameterAnnotationsAttribute attribute2 = new RuntimeInvisibleParameterAnnotationsAttribute();

        // Act - alternate calls
        changedCodePrinter.visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute1);
        changedCodePrinter.visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute2);
        changedCodePrinter.visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute1);

        // Assert
        verify(mockAttributeVisitor, times(2))
            .visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, attribute2);
    }
}
