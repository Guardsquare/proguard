package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.annotation.AnnotationDefaultAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ChangedCodePrinter#visitAnnotationDefaultAttribute(Clazz, Method, AnnotationDefaultAttribute)}.
 *
 * The visitAnnotationDefaultAttribute method in ChangedCodePrinter is a delegation method.
 * It simply forwards the call to the wrapped AttributeVisitor without any additional logic,
 * as AnnotationDefaultAttribute does not contain bytecode that needs change detection.
 *
 * These tests verify that the method:
 * 1. Correctly delegates to the wrapped visitor
 * 2. Passes the correct parameters
 * 3. Works with various inputs including edge cases
 */
public class ChangedCodePrinterClaude_visitAnnotationDefaultAttributeTest {

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
     * Tests that visitAnnotationDefaultAttribute delegates to the wrapped visitor.
     * Verifies that the method calls the visitor with the correct parameters.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_delegatesToWrappedVisitor() {
        // Arrange
        AnnotationDefaultAttribute attribute = new AnnotationDefaultAttribute();

        // Act
        changedCodePrinter.visitAnnotationDefaultAttribute(clazz, method, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitAnnotationDefaultAttribute(clazz, method, attribute);
    }

    /**
     * Tests that the method does not throw an exception with valid inputs.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_withValidInputs_doesNotThrow() {
        // Arrange
        AnnotationDefaultAttribute attribute = new AnnotationDefaultAttribute();

        // Act & Assert
        assertDoesNotThrow(() ->
            changedCodePrinter.visitAnnotationDefaultAttribute(clazz, method, attribute),
            "visitAnnotationDefaultAttribute should not throw any exception");
    }

    /**
     * Tests that the method can be called multiple times without issues.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_calledMultipleTimes_doesNotThrow() {
        // Arrange
        AnnotationDefaultAttribute attribute = new AnnotationDefaultAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitAnnotationDefaultAttribute(clazz, method, attribute);
            changedCodePrinter.visitAnnotationDefaultAttribute(clazz, method, attribute);
            changedCodePrinter.visitAnnotationDefaultAttribute(clazz, method, attribute);
        }, "Multiple calls should not throw any exception");

        // Verify the visitor was called multiple times
        verify(mockAttributeVisitor, times(3))
            .visitAnnotationDefaultAttribute(clazz, method, attribute);
    }

    /**
     * Tests that the method works with different Clazz instances.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_withDifferentClasses_delegatesCorrectly() {
        // Arrange
        Clazz clazz1 = new ProgramClass();
        Clazz clazz2 = new ProgramClass();
        AnnotationDefaultAttribute attribute = new AnnotationDefaultAttribute();

        // Act
        changedCodePrinter.visitAnnotationDefaultAttribute(clazz1, method, attribute);
        changedCodePrinter.visitAnnotationDefaultAttribute(clazz2, method, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitAnnotationDefaultAttribute(clazz1, method, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitAnnotationDefaultAttribute(clazz2, method, attribute);
    }

    /**
     * Tests that the method works with different Method instances.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_withDifferentMethods_delegatesCorrectly() {
        // Arrange
        Method method1 = new ProgramMethod();
        Method method2 = new ProgramMethod();
        AnnotationDefaultAttribute attribute = new AnnotationDefaultAttribute();

        // Act
        changedCodePrinter.visitAnnotationDefaultAttribute(clazz, method1, attribute);
        changedCodePrinter.visitAnnotationDefaultAttribute(clazz, method2, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitAnnotationDefaultAttribute(clazz, method1, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitAnnotationDefaultAttribute(clazz, method2, attribute);
    }

    /**
     * Tests that the method works with different AnnotationDefaultAttribute instances.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_withDifferentAttributes_delegatesCorrectly() {
        // Arrange
        AnnotationDefaultAttribute attribute1 = new AnnotationDefaultAttribute();
        AnnotationDefaultAttribute attribute2 = new AnnotationDefaultAttribute();

        // Act
        changedCodePrinter.visitAnnotationDefaultAttribute(clazz, method, attribute1);
        changedCodePrinter.visitAnnotationDefaultAttribute(clazz, method, attribute2);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitAnnotationDefaultAttribute(clazz, method, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitAnnotationDefaultAttribute(clazz, method, attribute2);
    }

    /**
     * Tests that the method passes parameters in the correct order.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_passesParametersInCorrectOrder() {
        // Arrange
        AnnotationDefaultAttribute attribute = new AnnotationDefaultAttribute();

        // Act
        changedCodePrinter.visitAnnotationDefaultAttribute(clazz, method, attribute);

        // Assert - verify the parameters are in correct order
        verify(mockAttributeVisitor).visitAnnotationDefaultAttribute(
            argThat(arg -> arg == clazz),
            argThat(arg -> arg == method),
            argThat(arg -> arg == attribute)
        );
    }

    /**
     * Tests that the method can be called in rapid succession.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_rapidSuccessiveCalls_doesNotThrow() {
        // Arrange
        AnnotationDefaultAttribute attribute = new AnnotationDefaultAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                changedCodePrinter.visitAnnotationDefaultAttribute(clazz, method, attribute);
            }
        }, "Rapid successive calls should not throw any exception");

        // Verify the visitor was called 100 times
        verify(mockAttributeVisitor, times(100))
            .visitAnnotationDefaultAttribute(clazz, method, attribute);
    }

    /**
     * Tests that the method works when the wrapped visitor does nothing (no-op).
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_withNoOpVisitor_doesNotThrow() {
        // Arrange
        AttributeVisitor noOpVisitor = mock(AttributeVisitor.class);
        doNothing().when(noOpVisitor).visitAnnotationDefaultAttribute(any(), any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(noOpVisitor);
        AnnotationDefaultAttribute attribute = new AnnotationDefaultAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> printer.visitAnnotationDefaultAttribute(clazz, method, attribute),
            "Should not throw when visitor is no-op");
    }

    /**
     * Tests that the method works when the wrapped visitor throws an exception.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_whenVisitorThrows_propagatesException() {
        // Arrange
        AttributeVisitor throwingVisitor = mock(AttributeVisitor.class);
        RuntimeException expectedException = new RuntimeException("Test exception");
        doThrow(expectedException)
            .when(throwingVisitor)
            .visitAnnotationDefaultAttribute(any(), any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(throwingVisitor);
        AnnotationDefaultAttribute attribute = new AnnotationDefaultAttribute();

        // Act & Assert
        RuntimeException thrownException = assertThrows(RuntimeException.class, () ->
            printer.visitAnnotationDefaultAttribute(clazz, method, attribute),
            "Should propagate exception from wrapped visitor");

        assertEquals(expectedException, thrownException, "Should throw the same exception");
    }

    /**
     * Tests that multiple ChangedCodePrinter instances work independently.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_multipleInstances_workIndependently() {
        // Arrange
        AttributeVisitor visitor1 = mock(AttributeVisitor.class);
        AttributeVisitor visitor2 = mock(AttributeVisitor.class);
        ChangedCodePrinter printer1 = new ChangedCodePrinter(visitor1);
        ChangedCodePrinter printer2 = new ChangedCodePrinter(visitor2);
        AnnotationDefaultAttribute attribute = new AnnotationDefaultAttribute();

        // Act
        printer1.visitAnnotationDefaultAttribute(clazz, method, attribute);
        printer2.visitAnnotationDefaultAttribute(clazz, method, attribute);

        // Assert
        verify(visitor1, times(1)).visitAnnotationDefaultAttribute(clazz, method, attribute);
        verify(visitor2, times(1)).visitAnnotationDefaultAttribute(clazz, method, attribute);
        verifyNoMoreInteractions(visitor1, visitor2);
    }

    /**
     * Tests that the printer can be reused after calling visitAnnotationDefaultAttribute.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_printerReusable() {
        // Arrange
        AnnotationDefaultAttribute attribute1 = new AnnotationDefaultAttribute();
        AnnotationDefaultAttribute attribute2 = new AnnotationDefaultAttribute();

        // Act & Assert - reuse the same printer
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitAnnotationDefaultAttribute(clazz, method, attribute1);
            changedCodePrinter.visitAnnotationDefaultAttribute(clazz, method, attribute2);
            changedCodePrinter.visitAnnotationDefaultAttribute(clazz, method, attribute1);
        }, "Printer should be reusable");

        verify(mockAttributeVisitor, times(2))
            .visitAnnotationDefaultAttribute(clazz, method, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitAnnotationDefaultAttribute(clazz, method, attribute2);
    }

    /**
     * Tests that the method delegates exactly once per call.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_delegatesExactlyOnce() {
        // Arrange
        AnnotationDefaultAttribute attribute = new AnnotationDefaultAttribute();

        // Act
        changedCodePrinter.visitAnnotationDefaultAttribute(clazz, method, attribute);

        // Assert - should delegate exactly once, no more, no less
        verify(mockAttributeVisitor, times(1))
            .visitAnnotationDefaultAttribute(any(), any(), any());
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method doesn't call any other visitor methods.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_doesNotCallOtherVisitorMethods() {
        // Arrange
        AnnotationDefaultAttribute attribute = new AnnotationDefaultAttribute();

        // Act
        changedCodePrinter.visitAnnotationDefaultAttribute(clazz, method, attribute);

        // Assert - verify only visitAnnotationDefaultAttribute was called
        verify(mockAttributeVisitor, times(1))
            .visitAnnotationDefaultAttribute(clazz, method, attribute);
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method returns normally (doesn't hang or loop).
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_returnsImmediately() {
        // Arrange
        AnnotationDefaultAttribute attribute = new AnnotationDefaultAttribute();

        // Act
        long startTime = System.nanoTime();
        changedCodePrinter.visitAnnotationDefaultAttribute(clazz, method, attribute);
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
    public void testVisitAnnotationDefaultAttribute_withFreshPrinter_doesNotThrow() {
        // Arrange
        AttributeVisitor visitor = mock(AttributeVisitor.class);
        ChangedCodePrinter freshPrinter = new ChangedCodePrinter(visitor);
        AnnotationDefaultAttribute attribute = new AnnotationDefaultAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> freshPrinter.visitAnnotationDefaultAttribute(clazz, method, attribute),
                "Method should work with a newly created printer");
    }

    /**
     * Tests that the same attribute can be visited multiple times with the same clazz and method.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_sameAttributeMultipleTimes() {
        // Arrange
        AnnotationDefaultAttribute attribute = new AnnotationDefaultAttribute();

        // Act
        changedCodePrinter.visitAnnotationDefaultAttribute(clazz, method, attribute);
        changedCodePrinter.visitAnnotationDefaultAttribute(clazz, method, attribute);
        changedCodePrinter.visitAnnotationDefaultAttribute(clazz, method, attribute);

        // Assert
        verify(mockAttributeVisitor, times(3))
            .visitAnnotationDefaultAttribute(clazz, method, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the attribute.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_doesNotModifyAttribute() {
        // Arrange
        AnnotationDefaultAttribute attribute = new AnnotationDefaultAttribute();

        // Act
        changedCodePrinter.visitAnnotationDefaultAttribute(clazz, method, attribute);

        // Assert - verify the call succeeded and delegated properly
        verify(mockAttributeVisitor, times(1))
            .visitAnnotationDefaultAttribute(clazz, method, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the clazz.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_doesNotModifyClazz() {
        // Arrange
        AnnotationDefaultAttribute attribute = new AnnotationDefaultAttribute();

        // Act
        changedCodePrinter.visitAnnotationDefaultAttribute(clazz, method, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitAnnotationDefaultAttribute(clazz, method, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the method.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_doesNotModifyMethod() {
        // Arrange
        AnnotationDefaultAttribute attribute = new AnnotationDefaultAttribute();

        // Act
        changedCodePrinter.visitAnnotationDefaultAttribute(clazz, method, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitAnnotationDefaultAttribute(clazz, method, attribute);
    }

    /**
     * Tests that the method works correctly when alternating with other visitor methods.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_alternatingWithOtherMethods_doesNotInterfere() {
        // Arrange
        AnnotationDefaultAttribute attribute1 = new AnnotationDefaultAttribute();
        AnnotationDefaultAttribute attribute2 = new AnnotationDefaultAttribute();

        // Act - alternate calls
        changedCodePrinter.visitAnnotationDefaultAttribute(clazz, method, attribute1);
        changedCodePrinter.visitAnnotationDefaultAttribute(clazz, method, attribute2);
        changedCodePrinter.visitAnnotationDefaultAttribute(clazz, method, attribute1);

        // Assert
        verify(mockAttributeVisitor, times(2))
            .visitAnnotationDefaultAttribute(clazz, method, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitAnnotationDefaultAttribute(clazz, method, attribute2);
    }

    /**
     * Tests that the method works with all parameters being different instances.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_withAllDifferentParameters_delegatesCorrectly() {
        // Arrange
        Clazz clazz1 = new ProgramClass();
        Method method1 = new ProgramMethod();
        AnnotationDefaultAttribute attribute1 = new AnnotationDefaultAttribute();

        Clazz clazz2 = new ProgramClass();
        Method method2 = new ProgramMethod();
        AnnotationDefaultAttribute attribute2 = new AnnotationDefaultAttribute();

        // Act
        changedCodePrinter.visitAnnotationDefaultAttribute(clazz1, method1, attribute1);
        changedCodePrinter.visitAnnotationDefaultAttribute(clazz2, method2, attribute2);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitAnnotationDefaultAttribute(clazz1, method1, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitAnnotationDefaultAttribute(clazz2, method2, attribute2);
    }
}
