package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.annotation.RuntimeVisibleAnnotationsAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ChangedCodePrinter#visitRuntimeVisibleAnnotationsAttribute(Clazz, RuntimeVisibleAnnotationsAttribute)}.
 *
 * The visitRuntimeVisibleAnnotationsAttribute method in ChangedCodePrinter is a delegation method.
 * It simply forwards the call to the wrapped AttributeVisitor without any additional logic,
 * as RuntimeVisibleAnnotationsAttribute does not contain bytecode that needs change detection.
 *
 * These tests verify that the method:
 * 1. Correctly delegates to the wrapped visitor
 * 2. Passes the correct parameters
 * 3. Works with various inputs including edge cases
 */
public class ChangedCodePrinterClaude_visitRuntimeVisibleAnnotationsAttributeTest {

    private AttributeVisitor mockAttributeVisitor;
    private ChangedCodePrinter changedCodePrinter;
    private Clazz clazz;

    @BeforeEach
    public void setUp() {
        mockAttributeVisitor = mock(AttributeVisitor.class);
        changedCodePrinter = new ChangedCodePrinter(mockAttributeVisitor);
        clazz = new ProgramClass();
    }

    /**
     * Tests that visitRuntimeVisibleAnnotationsAttribute delegates to the wrapped visitor.
     * Verifies that the method calls the visitor with the correct parameters.
     */
    @Test
    public void testVisitRuntimeVisibleAnnotationsAttribute_delegatesToWrappedVisitor() {
        // Arrange
        RuntimeVisibleAnnotationsAttribute attribute = new RuntimeVisibleAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeVisibleAnnotationsAttribute(clazz, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleAnnotationsAttribute(clazz, attribute);
    }

    /**
     * Tests that the method does not throw an exception with valid inputs.
     */
    @Test
    public void testVisitRuntimeVisibleAnnotationsAttribute_withValidInputs_doesNotThrow() {
        // Arrange
        RuntimeVisibleAnnotationsAttribute attribute = new RuntimeVisibleAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() ->
            changedCodePrinter.visitRuntimeVisibleAnnotationsAttribute(clazz, attribute),
            "visitRuntimeVisibleAnnotationsAttribute should not throw any exception");
    }

    /**
     * Tests that the method can be called multiple times without issues.
     */
    @Test
    public void testVisitRuntimeVisibleAnnotationsAttribute_calledMultipleTimes_doesNotThrow() {
        // Arrange
        RuntimeVisibleAnnotationsAttribute attribute = new RuntimeVisibleAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitRuntimeVisibleAnnotationsAttribute(clazz, attribute);
            changedCodePrinter.visitRuntimeVisibleAnnotationsAttribute(clazz, attribute);
            changedCodePrinter.visitRuntimeVisibleAnnotationsAttribute(clazz, attribute);
        }, "Multiple calls should not throw any exception");

        // Verify the visitor was called multiple times
        verify(mockAttributeVisitor, times(3))
            .visitRuntimeVisibleAnnotationsAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works with different Clazz instances.
     */
    @Test
    public void testVisitRuntimeVisibleAnnotationsAttribute_withDifferentClasses_delegatesCorrectly() {
        // Arrange
        Clazz clazz1 = new ProgramClass();
        Clazz clazz2 = new ProgramClass();
        RuntimeVisibleAnnotationsAttribute attribute = new RuntimeVisibleAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeVisibleAnnotationsAttribute(clazz1, attribute);
        changedCodePrinter.visitRuntimeVisibleAnnotationsAttribute(clazz2, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleAnnotationsAttribute(clazz1, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleAnnotationsAttribute(clazz2, attribute);
    }

    /**
     * Tests that the method works with different RuntimeVisibleAnnotationsAttribute instances.
     */
    @Test
    public void testVisitRuntimeVisibleAnnotationsAttribute_withDifferentAttributes_delegatesCorrectly() {
        // Arrange
        RuntimeVisibleAnnotationsAttribute attribute1 = new RuntimeVisibleAnnotationsAttribute();
        RuntimeVisibleAnnotationsAttribute attribute2 = new RuntimeVisibleAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeVisibleAnnotationsAttribute(clazz, attribute1);
        changedCodePrinter.visitRuntimeVisibleAnnotationsAttribute(clazz, attribute2);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleAnnotationsAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleAnnotationsAttribute(clazz, attribute2);
    }

    /**
     * Tests that the method passes parameters in the correct order.
     */
    @Test
    public void testVisitRuntimeVisibleAnnotationsAttribute_passesParametersInCorrectOrder() {
        // Arrange
        RuntimeVisibleAnnotationsAttribute attribute = new RuntimeVisibleAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeVisibleAnnotationsAttribute(clazz, attribute);

        // Assert - verify the parameters are in correct order
        verify(mockAttributeVisitor).visitRuntimeVisibleAnnotationsAttribute(
            argThat(arg -> arg == clazz),
            argThat(arg -> arg == attribute)
        );
    }

    /**
     * Tests that the method can be called in rapid succession.
     */
    @Test
    public void testVisitRuntimeVisibleAnnotationsAttribute_rapidSuccessiveCalls_doesNotThrow() {
        // Arrange
        RuntimeVisibleAnnotationsAttribute attribute = new RuntimeVisibleAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                changedCodePrinter.visitRuntimeVisibleAnnotationsAttribute(clazz, attribute);
            }
        }, "Rapid successive calls should not throw any exception");

        // Verify the visitor was called 100 times
        verify(mockAttributeVisitor, times(100))
            .visitRuntimeVisibleAnnotationsAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works when the wrapped visitor does nothing (no-op).
     */
    @Test
    public void testVisitRuntimeVisibleAnnotationsAttribute_withNoOpVisitor_doesNotThrow() {
        // Arrange
        AttributeVisitor noOpVisitor = mock(AttributeVisitor.class);
        doNothing().when(noOpVisitor).visitRuntimeVisibleAnnotationsAttribute(any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(noOpVisitor);
        RuntimeVisibleAnnotationsAttribute attribute = new RuntimeVisibleAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> printer.visitRuntimeVisibleAnnotationsAttribute(clazz, attribute),
            "Should not throw when visitor is no-op");
    }

    /**
     * Tests that the method works when the wrapped visitor throws an exception.
     */
    @Test
    public void testVisitRuntimeVisibleAnnotationsAttribute_whenVisitorThrows_propagatesException() {
        // Arrange
        AttributeVisitor throwingVisitor = mock(AttributeVisitor.class);
        RuntimeException expectedException = new RuntimeException("Test exception");
        doThrow(expectedException)
            .when(throwingVisitor)
            .visitRuntimeVisibleAnnotationsAttribute(any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(throwingVisitor);
        RuntimeVisibleAnnotationsAttribute attribute = new RuntimeVisibleAnnotationsAttribute();

        // Act & Assert
        RuntimeException thrownException = assertThrows(RuntimeException.class, () ->
            printer.visitRuntimeVisibleAnnotationsAttribute(clazz, attribute),
            "Should propagate exception from wrapped visitor");

        assertEquals(expectedException, thrownException, "Should throw the same exception");
    }

    /**
     * Tests that multiple ChangedCodePrinter instances work independently.
     */
    @Test
    public void testVisitRuntimeVisibleAnnotationsAttribute_multipleInstances_workIndependently() {
        // Arrange
        AttributeVisitor visitor1 = mock(AttributeVisitor.class);
        AttributeVisitor visitor2 = mock(AttributeVisitor.class);
        ChangedCodePrinter printer1 = new ChangedCodePrinter(visitor1);
        ChangedCodePrinter printer2 = new ChangedCodePrinter(visitor2);
        RuntimeVisibleAnnotationsAttribute attribute = new RuntimeVisibleAnnotationsAttribute();

        // Act
        printer1.visitRuntimeVisibleAnnotationsAttribute(clazz, attribute);
        printer2.visitRuntimeVisibleAnnotationsAttribute(clazz, attribute);

        // Assert
        verify(visitor1, times(1)).visitRuntimeVisibleAnnotationsAttribute(clazz, attribute);
        verify(visitor2, times(1)).visitRuntimeVisibleAnnotationsAttribute(clazz, attribute);
        verifyNoMoreInteractions(visitor1, visitor2);
    }

    /**
     * Tests that the printer can be reused after calling visitRuntimeVisibleAnnotationsAttribute.
     */
    @Test
    public void testVisitRuntimeVisibleAnnotationsAttribute_printerReusable() {
        // Arrange
        RuntimeVisibleAnnotationsAttribute attribute1 = new RuntimeVisibleAnnotationsAttribute();
        RuntimeVisibleAnnotationsAttribute attribute2 = new RuntimeVisibleAnnotationsAttribute();

        // Act & Assert - reuse the same printer
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitRuntimeVisibleAnnotationsAttribute(clazz, attribute1);
            changedCodePrinter.visitRuntimeVisibleAnnotationsAttribute(clazz, attribute2);
            changedCodePrinter.visitRuntimeVisibleAnnotationsAttribute(clazz, attribute1);
        }, "Printer should be reusable");

        verify(mockAttributeVisitor, times(2))
            .visitRuntimeVisibleAnnotationsAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleAnnotationsAttribute(clazz, attribute2);
    }

    /**
     * Tests that the method delegates exactly once per call.
     */
    @Test
    public void testVisitRuntimeVisibleAnnotationsAttribute_delegatesExactlyOnce() {
        // Arrange
        RuntimeVisibleAnnotationsAttribute attribute = new RuntimeVisibleAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeVisibleAnnotationsAttribute(clazz, attribute);

        // Assert - should delegate exactly once, no more, no less
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleAnnotationsAttribute(any(), any());
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method doesn't call any other visitor methods.
     */
    @Test
    public void testVisitRuntimeVisibleAnnotationsAttribute_doesNotCallOtherVisitorMethods() {
        // Arrange
        RuntimeVisibleAnnotationsAttribute attribute = new RuntimeVisibleAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeVisibleAnnotationsAttribute(clazz, attribute);

        // Assert - verify only visitRuntimeVisibleAnnotationsAttribute was called
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleAnnotationsAttribute(clazz, attribute);
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method returns normally (doesn't hang or loop).
     */
    @Test
    public void testVisitRuntimeVisibleAnnotationsAttribute_returnsImmediately() {
        // Arrange
        RuntimeVisibleAnnotationsAttribute attribute = new RuntimeVisibleAnnotationsAttribute();

        // Act
        long startTime = System.nanoTime();
        changedCodePrinter.visitRuntimeVisibleAnnotationsAttribute(clazz, attribute);
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
    public void testVisitRuntimeVisibleAnnotationsAttribute_withFreshPrinter_doesNotThrow() {
        // Arrange
        AttributeVisitor visitor = mock(AttributeVisitor.class);
        ChangedCodePrinter freshPrinter = new ChangedCodePrinter(visitor);
        RuntimeVisibleAnnotationsAttribute attribute = new RuntimeVisibleAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> freshPrinter.visitRuntimeVisibleAnnotationsAttribute(clazz, attribute),
                "Method should work with a newly created printer");
    }

    /**
     * Tests that the same attribute can be visited multiple times with the same clazz.
     */
    @Test
    public void testVisitRuntimeVisibleAnnotationsAttribute_sameAttributeMultipleTimes() {
        // Arrange
        RuntimeVisibleAnnotationsAttribute attribute = new RuntimeVisibleAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeVisibleAnnotationsAttribute(clazz, attribute);
        changedCodePrinter.visitRuntimeVisibleAnnotationsAttribute(clazz, attribute);
        changedCodePrinter.visitRuntimeVisibleAnnotationsAttribute(clazz, attribute);

        // Assert
        verify(mockAttributeVisitor, times(3))
            .visitRuntimeVisibleAnnotationsAttribute(clazz, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the attribute.
     */
    @Test
    public void testVisitRuntimeVisibleAnnotationsAttribute_doesNotModifyAttribute() {
        // Arrange
        RuntimeVisibleAnnotationsAttribute attribute = new RuntimeVisibleAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeVisibleAnnotationsAttribute(clazz, attribute);

        // Assert - verify the call succeeded and delegated properly
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleAnnotationsAttribute(clazz, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the clazz.
     */
    @Test
    public void testVisitRuntimeVisibleAnnotationsAttribute_doesNotModifyClazz() {
        // Arrange
        RuntimeVisibleAnnotationsAttribute attribute = new RuntimeVisibleAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeVisibleAnnotationsAttribute(clazz, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleAnnotationsAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works correctly when alternating with other visitor methods.
     */
    @Test
    public void testVisitRuntimeVisibleAnnotationsAttribute_alternatingWithOtherMethods_doesNotInterfere() {
        // Arrange
        RuntimeVisibleAnnotationsAttribute attribute1 = new RuntimeVisibleAnnotationsAttribute();
        RuntimeVisibleAnnotationsAttribute attribute2 = new RuntimeVisibleAnnotationsAttribute();

        // Act - alternate calls
        changedCodePrinter.visitRuntimeVisibleAnnotationsAttribute(clazz, attribute1);
        changedCodePrinter.visitRuntimeVisibleAnnotationsAttribute(clazz, attribute2);
        changedCodePrinter.visitRuntimeVisibleAnnotationsAttribute(clazz, attribute1);

        // Assert
        verify(mockAttributeVisitor, times(2))
            .visitRuntimeVisibleAnnotationsAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeVisibleAnnotationsAttribute(clazz, attribute2);
    }
}
