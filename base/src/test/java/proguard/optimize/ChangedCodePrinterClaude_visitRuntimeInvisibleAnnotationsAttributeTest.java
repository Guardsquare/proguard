package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.annotation.RuntimeInvisibleAnnotationsAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ChangedCodePrinter#visitRuntimeInvisibleAnnotationsAttribute(Clazz, RuntimeInvisibleAnnotationsAttribute)}.
 *
 * The visitRuntimeInvisibleAnnotationsAttribute method in ChangedCodePrinter is a delegation method.
 * It simply forwards the call to the wrapped AttributeVisitor without any additional logic,
 * as RuntimeInvisibleAnnotationsAttribute does not contain bytecode that needs change detection.
 *
 * These tests verify that the method:
 * 1. Correctly delegates to the wrapped visitor
 * 2. Passes the correct parameters
 * 3. Works with various inputs including edge cases
 */
public class ChangedCodePrinterClaude_visitRuntimeInvisibleAnnotationsAttributeTest {

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
     * Tests that visitRuntimeInvisibleAnnotationsAttribute delegates to the wrapped visitor.
     * Verifies that the method calls the visitor with the correct parameters.
     */
    @Test
    public void testVisitRuntimeInvisibleAnnotationsAttribute_delegatesToWrappedVisitor() {
        // Arrange
        RuntimeInvisibleAnnotationsAttribute attribute = new RuntimeInvisibleAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeInvisibleAnnotationsAttribute(clazz, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeInvisibleAnnotationsAttribute(clazz, attribute);
    }

    /**
     * Tests that the method does not throw an exception with valid inputs.
     */
    @Test
    public void testVisitRuntimeInvisibleAnnotationsAttribute_withValidInputs_doesNotThrow() {
        // Arrange
        RuntimeInvisibleAnnotationsAttribute attribute = new RuntimeInvisibleAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() ->
            changedCodePrinter.visitRuntimeInvisibleAnnotationsAttribute(clazz, attribute),
            "visitRuntimeInvisibleAnnotationsAttribute should not throw any exception");
    }

    /**
     * Tests that the method can be called multiple times without issues.
     */
    @Test
    public void testVisitRuntimeInvisibleAnnotationsAttribute_calledMultipleTimes_doesNotThrow() {
        // Arrange
        RuntimeInvisibleAnnotationsAttribute attribute = new RuntimeInvisibleAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitRuntimeInvisibleAnnotationsAttribute(clazz, attribute);
            changedCodePrinter.visitRuntimeInvisibleAnnotationsAttribute(clazz, attribute);
            changedCodePrinter.visitRuntimeInvisibleAnnotationsAttribute(clazz, attribute);
        }, "Multiple calls should not throw any exception");

        // Verify the visitor was called multiple times
        verify(mockAttributeVisitor, times(3))
            .visitRuntimeInvisibleAnnotationsAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works with different Clazz instances.
     */
    @Test
    public void testVisitRuntimeInvisibleAnnotationsAttribute_withDifferentClasses_delegatesCorrectly() {
        // Arrange
        Clazz clazz1 = new ProgramClass();
        Clazz clazz2 = new ProgramClass();
        RuntimeInvisibleAnnotationsAttribute attribute = new RuntimeInvisibleAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeInvisibleAnnotationsAttribute(clazz1, attribute);
        changedCodePrinter.visitRuntimeInvisibleAnnotationsAttribute(clazz2, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeInvisibleAnnotationsAttribute(clazz1, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeInvisibleAnnotationsAttribute(clazz2, attribute);
    }

    /**
     * Tests that the method works with different RuntimeInvisibleAnnotationsAttribute instances.
     */
    @Test
    public void testVisitRuntimeInvisibleAnnotationsAttribute_withDifferentAttributes_delegatesCorrectly() {
        // Arrange
        RuntimeInvisibleAnnotationsAttribute attribute1 = new RuntimeInvisibleAnnotationsAttribute();
        RuntimeInvisibleAnnotationsAttribute attribute2 = new RuntimeInvisibleAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeInvisibleAnnotationsAttribute(clazz, attribute1);
        changedCodePrinter.visitRuntimeInvisibleAnnotationsAttribute(clazz, attribute2);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeInvisibleAnnotationsAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeInvisibleAnnotationsAttribute(clazz, attribute2);
    }

    /**
     * Tests that the method passes parameters in the correct order.
     */
    @Test
    public void testVisitRuntimeInvisibleAnnotationsAttribute_passesParametersInCorrectOrder() {
        // Arrange
        RuntimeInvisibleAnnotationsAttribute attribute = new RuntimeInvisibleAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeInvisibleAnnotationsAttribute(clazz, attribute);

        // Assert - verify the parameters are in correct order
        verify(mockAttributeVisitor).visitRuntimeInvisibleAnnotationsAttribute(
            argThat(arg -> arg == clazz),
            argThat(arg -> arg == attribute)
        );
    }

    /**
     * Tests that the method can be called in rapid succession.
     */
    @Test
    public void testVisitRuntimeInvisibleAnnotationsAttribute_rapidSuccessiveCalls_doesNotThrow() {
        // Arrange
        RuntimeInvisibleAnnotationsAttribute attribute = new RuntimeInvisibleAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                changedCodePrinter.visitRuntimeInvisibleAnnotationsAttribute(clazz, attribute);
            }
        }, "Rapid successive calls should not throw any exception");

        // Verify the visitor was called 100 times
        verify(mockAttributeVisitor, times(100))
            .visitRuntimeInvisibleAnnotationsAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works when the wrapped visitor does nothing (no-op).
     */
    @Test
    public void testVisitRuntimeInvisibleAnnotationsAttribute_withNoOpVisitor_doesNotThrow() {
        // Arrange
        AttributeVisitor noOpVisitor = mock(AttributeVisitor.class);
        doNothing().when(noOpVisitor).visitRuntimeInvisibleAnnotationsAttribute(any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(noOpVisitor);
        RuntimeInvisibleAnnotationsAttribute attribute = new RuntimeInvisibleAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> printer.visitRuntimeInvisibleAnnotationsAttribute(clazz, attribute),
            "Should not throw when visitor is no-op");
    }

    /**
     * Tests that the method works when the wrapped visitor throws an exception.
     */
    @Test
    public void testVisitRuntimeInvisibleAnnotationsAttribute_whenVisitorThrows_propagatesException() {
        // Arrange
        AttributeVisitor throwingVisitor = mock(AttributeVisitor.class);
        RuntimeException expectedException = new RuntimeException("Test exception");
        doThrow(expectedException)
            .when(throwingVisitor)
            .visitRuntimeInvisibleAnnotationsAttribute(any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(throwingVisitor);
        RuntimeInvisibleAnnotationsAttribute attribute = new RuntimeInvisibleAnnotationsAttribute();

        // Act & Assert
        RuntimeException thrownException = assertThrows(RuntimeException.class, () ->
            printer.visitRuntimeInvisibleAnnotationsAttribute(clazz, attribute),
            "Should propagate exception from wrapped visitor");

        assertEquals(expectedException, thrownException, "Should throw the same exception");
    }

    /**
     * Tests that multiple ChangedCodePrinter instances work independently.
     */
    @Test
    public void testVisitRuntimeInvisibleAnnotationsAttribute_multipleInstances_workIndependently() {
        // Arrange
        AttributeVisitor visitor1 = mock(AttributeVisitor.class);
        AttributeVisitor visitor2 = mock(AttributeVisitor.class);
        ChangedCodePrinter printer1 = new ChangedCodePrinter(visitor1);
        ChangedCodePrinter printer2 = new ChangedCodePrinter(visitor2);
        RuntimeInvisibleAnnotationsAttribute attribute = new RuntimeInvisibleAnnotationsAttribute();

        // Act
        printer1.visitRuntimeInvisibleAnnotationsAttribute(clazz, attribute);
        printer2.visitRuntimeInvisibleAnnotationsAttribute(clazz, attribute);

        // Assert
        verify(visitor1, times(1)).visitRuntimeInvisibleAnnotationsAttribute(clazz, attribute);
        verify(visitor2, times(1)).visitRuntimeInvisibleAnnotationsAttribute(clazz, attribute);
        verifyNoMoreInteractions(visitor1, visitor2);
    }

    /**
     * Tests that the printer can be reused after calling visitRuntimeInvisibleAnnotationsAttribute.
     */
    @Test
    public void testVisitRuntimeInvisibleAnnotationsAttribute_printerReusable() {
        // Arrange
        RuntimeInvisibleAnnotationsAttribute attribute1 = new RuntimeInvisibleAnnotationsAttribute();
        RuntimeInvisibleAnnotationsAttribute attribute2 = new RuntimeInvisibleAnnotationsAttribute();

        // Act & Assert - reuse the same printer
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitRuntimeInvisibleAnnotationsAttribute(clazz, attribute1);
            changedCodePrinter.visitRuntimeInvisibleAnnotationsAttribute(clazz, attribute2);
            changedCodePrinter.visitRuntimeInvisibleAnnotationsAttribute(clazz, attribute1);
        }, "Printer should be reusable");

        verify(mockAttributeVisitor, times(2))
            .visitRuntimeInvisibleAnnotationsAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeInvisibleAnnotationsAttribute(clazz, attribute2);
    }

    /**
     * Tests that the method delegates exactly once per call.
     */
    @Test
    public void testVisitRuntimeInvisibleAnnotationsAttribute_delegatesExactlyOnce() {
        // Arrange
        RuntimeInvisibleAnnotationsAttribute attribute = new RuntimeInvisibleAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeInvisibleAnnotationsAttribute(clazz, attribute);

        // Assert - should delegate exactly once, no more, no less
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeInvisibleAnnotationsAttribute(any(), any());
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method doesn't call any other visitor methods.
     */
    @Test
    public void testVisitRuntimeInvisibleAnnotationsAttribute_doesNotCallOtherVisitorMethods() {
        // Arrange
        RuntimeInvisibleAnnotationsAttribute attribute = new RuntimeInvisibleAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeInvisibleAnnotationsAttribute(clazz, attribute);

        // Assert - verify only visitRuntimeInvisibleAnnotationsAttribute was called
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeInvisibleAnnotationsAttribute(clazz, attribute);
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method returns normally (doesn't hang or loop).
     */
    @Test
    public void testVisitRuntimeInvisibleAnnotationsAttribute_returnsImmediately() {
        // Arrange
        RuntimeInvisibleAnnotationsAttribute attribute = new RuntimeInvisibleAnnotationsAttribute();

        // Act
        long startTime = System.nanoTime();
        changedCodePrinter.visitRuntimeInvisibleAnnotationsAttribute(clazz, attribute);
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
    public void testVisitRuntimeInvisibleAnnotationsAttribute_withFreshPrinter_doesNotThrow() {
        // Arrange
        AttributeVisitor visitor = mock(AttributeVisitor.class);
        ChangedCodePrinter freshPrinter = new ChangedCodePrinter(visitor);
        RuntimeInvisibleAnnotationsAttribute attribute = new RuntimeInvisibleAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> freshPrinter.visitRuntimeInvisibleAnnotationsAttribute(clazz, attribute),
                "Method should work with a newly created printer");
    }

    /**
     * Tests that the same attribute can be visited multiple times with the same clazz.
     */
    @Test
    public void testVisitRuntimeInvisibleAnnotationsAttribute_sameAttributeMultipleTimes() {
        // Arrange
        RuntimeInvisibleAnnotationsAttribute attribute = new RuntimeInvisibleAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeInvisibleAnnotationsAttribute(clazz, attribute);
        changedCodePrinter.visitRuntimeInvisibleAnnotationsAttribute(clazz, attribute);
        changedCodePrinter.visitRuntimeInvisibleAnnotationsAttribute(clazz, attribute);

        // Assert
        verify(mockAttributeVisitor, times(3))
            .visitRuntimeInvisibleAnnotationsAttribute(clazz, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the attribute.
     */
    @Test
    public void testVisitRuntimeInvisibleAnnotationsAttribute_doesNotModifyAttribute() {
        // Arrange
        RuntimeInvisibleAnnotationsAttribute attribute = new RuntimeInvisibleAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeInvisibleAnnotationsAttribute(clazz, attribute);

        // Assert - verify the call succeeded and delegated properly
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeInvisibleAnnotationsAttribute(clazz, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the clazz.
     */
    @Test
    public void testVisitRuntimeInvisibleAnnotationsAttribute_doesNotModifyClazz() {
        // Arrange
        RuntimeInvisibleAnnotationsAttribute attribute = new RuntimeInvisibleAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeInvisibleAnnotationsAttribute(clazz, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeInvisibleAnnotationsAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works correctly when alternating with other visitor methods.
     */
    @Test
    public void testVisitRuntimeInvisibleAnnotationsAttribute_alternatingWithOtherMethods_doesNotInterfere() {
        // Arrange
        RuntimeInvisibleAnnotationsAttribute attribute1 = new RuntimeInvisibleAnnotationsAttribute();
        RuntimeInvisibleAnnotationsAttribute attribute2 = new RuntimeInvisibleAnnotationsAttribute();

        // Act - alternate calls
        changedCodePrinter.visitRuntimeInvisibleAnnotationsAttribute(clazz, attribute1);
        changedCodePrinter.visitRuntimeInvisibleAnnotationsAttribute(clazz, attribute2);
        changedCodePrinter.visitRuntimeInvisibleAnnotationsAttribute(clazz, attribute1);

        // Assert
        verify(mockAttributeVisitor, times(2))
            .visitRuntimeInvisibleAnnotationsAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeInvisibleAnnotationsAttribute(clazz, attribute2);
    }
}
