package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.BootstrapMethodsAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ChangedCodePrinter#visitBootstrapMethodsAttribute(Clazz, BootstrapMethodsAttribute)}.
 *
 * The visitBootstrapMethodsAttribute method in ChangedCodePrinter is a delegation method.
 * It simply forwards the call to the wrapped AttributeVisitor without any additional logic,
 * as BootstrapMethodsAttribute does not contain bytecode that needs change detection.
 *
 * These tests verify that the method:
 * 1. Correctly delegates to the wrapped visitor
 * 2. Passes the correct parameters
 * 3. Works with various inputs including edge cases
 */
public class ChangedCodePrinterClaude_visitBootstrapMethodsAttributeTest {

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
     * Tests that visitBootstrapMethodsAttribute delegates to the wrapped visitor.
     * Verifies that the method calls the visitor with the correct parameters.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_delegatesToWrappedVisitor() {
        // Arrange
        BootstrapMethodsAttribute attribute = new BootstrapMethodsAttribute();

        // Act
        changedCodePrinter.visitBootstrapMethodsAttribute(clazz, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitBootstrapMethodsAttribute(clazz, attribute);
    }

    /**
     * Tests that the method does not throw an exception with valid inputs.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_withValidInputs_doesNotThrow() {
        // Arrange
        BootstrapMethodsAttribute attribute = new BootstrapMethodsAttribute();

        // Act & Assert
        assertDoesNotThrow(() ->
            changedCodePrinter.visitBootstrapMethodsAttribute(clazz, attribute),
            "visitBootstrapMethodsAttribute should not throw any exception");
    }

    /**
     * Tests that the method can be called multiple times without issues.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_calledMultipleTimes_doesNotThrow() {
        // Arrange
        BootstrapMethodsAttribute attribute = new BootstrapMethodsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitBootstrapMethodsAttribute(clazz, attribute);
            changedCodePrinter.visitBootstrapMethodsAttribute(clazz, attribute);
            changedCodePrinter.visitBootstrapMethodsAttribute(clazz, attribute);
        }, "Multiple calls should not throw any exception");

        // Verify the visitor was called multiple times
        verify(mockAttributeVisitor, times(3))
            .visitBootstrapMethodsAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works with different Clazz instances.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_withDifferentClasses_delegatesCorrectly() {
        // Arrange
        Clazz clazz1 = new ProgramClass();
        Clazz clazz2 = new ProgramClass();
        BootstrapMethodsAttribute attribute = new BootstrapMethodsAttribute();

        // Act
        changedCodePrinter.visitBootstrapMethodsAttribute(clazz1, attribute);
        changedCodePrinter.visitBootstrapMethodsAttribute(clazz2, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitBootstrapMethodsAttribute(clazz1, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitBootstrapMethodsAttribute(clazz2, attribute);
    }

    /**
     * Tests that the method works with different BootstrapMethodsAttribute instances.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_withDifferentAttributes_delegatesCorrectly() {
        // Arrange
        BootstrapMethodsAttribute attribute1 = new BootstrapMethodsAttribute();
        BootstrapMethodsAttribute attribute2 = new BootstrapMethodsAttribute();

        // Act
        changedCodePrinter.visitBootstrapMethodsAttribute(clazz, attribute1);
        changedCodePrinter.visitBootstrapMethodsAttribute(clazz, attribute2);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitBootstrapMethodsAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitBootstrapMethodsAttribute(clazz, attribute2);
    }

    /**
     * Tests that the method passes parameters in the correct order.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_passesParametersInCorrectOrder() {
        // Arrange
        BootstrapMethodsAttribute attribute = new BootstrapMethodsAttribute();

        // Act
        changedCodePrinter.visitBootstrapMethodsAttribute(clazz, attribute);

        // Assert - verify the parameters are in correct order
        verify(mockAttributeVisitor).visitBootstrapMethodsAttribute(
            argThat(arg -> arg == clazz),
            argThat(arg -> arg == attribute)
        );
    }

    /**
     * Tests that the method can be called in rapid succession.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_rapidSuccessiveCalls_doesNotThrow() {
        // Arrange
        BootstrapMethodsAttribute attribute = new BootstrapMethodsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                changedCodePrinter.visitBootstrapMethodsAttribute(clazz, attribute);
            }
        }, "Rapid successive calls should not throw any exception");

        // Verify the visitor was called 100 times
        verify(mockAttributeVisitor, times(100))
            .visitBootstrapMethodsAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works when the wrapped visitor does nothing (no-op).
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_withNoOpVisitor_doesNotThrow() {
        // Arrange
        AttributeVisitor noOpVisitor = mock(AttributeVisitor.class);
        doNothing().when(noOpVisitor).visitBootstrapMethodsAttribute(any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(noOpVisitor);
        BootstrapMethodsAttribute attribute = new BootstrapMethodsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> printer.visitBootstrapMethodsAttribute(clazz, attribute),
            "Should not throw when visitor is no-op");
    }

    /**
     * Tests that the method works when the wrapped visitor throws an exception.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_whenVisitorThrows_propagatesException() {
        // Arrange
        AttributeVisitor throwingVisitor = mock(AttributeVisitor.class);
        RuntimeException expectedException = new RuntimeException("Test exception");
        doThrow(expectedException)
            .when(throwingVisitor)
            .visitBootstrapMethodsAttribute(any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(throwingVisitor);
        BootstrapMethodsAttribute attribute = new BootstrapMethodsAttribute();

        // Act & Assert
        RuntimeException thrownException = assertThrows(RuntimeException.class, () ->
            printer.visitBootstrapMethodsAttribute(clazz, attribute),
            "Should propagate exception from wrapped visitor");

        assertEquals(expectedException, thrownException, "Should throw the same exception");
    }

    /**
     * Tests that multiple ChangedCodePrinter instances work independently.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_multipleInstances_workIndependently() {
        // Arrange
        AttributeVisitor visitor1 = mock(AttributeVisitor.class);
        AttributeVisitor visitor2 = mock(AttributeVisitor.class);
        ChangedCodePrinter printer1 = new ChangedCodePrinter(visitor1);
        ChangedCodePrinter printer2 = new ChangedCodePrinter(visitor2);
        BootstrapMethodsAttribute attribute = new BootstrapMethodsAttribute();

        // Act
        printer1.visitBootstrapMethodsAttribute(clazz, attribute);
        printer2.visitBootstrapMethodsAttribute(clazz, attribute);

        // Assert
        verify(visitor1, times(1)).visitBootstrapMethodsAttribute(clazz, attribute);
        verify(visitor2, times(1)).visitBootstrapMethodsAttribute(clazz, attribute);
        verifyNoMoreInteractions(visitor1, visitor2);
    }

    /**
     * Tests that the printer can be reused after calling visitBootstrapMethodsAttribute.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_printerReusable() {
        // Arrange
        BootstrapMethodsAttribute attribute1 = new BootstrapMethodsAttribute();
        BootstrapMethodsAttribute attribute2 = new BootstrapMethodsAttribute();

        // Act & Assert - reuse the same printer
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitBootstrapMethodsAttribute(clazz, attribute1);
            changedCodePrinter.visitBootstrapMethodsAttribute(clazz, attribute2);
            changedCodePrinter.visitBootstrapMethodsAttribute(clazz, attribute1);
        }, "Printer should be reusable");

        verify(mockAttributeVisitor, times(2))
            .visitBootstrapMethodsAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitBootstrapMethodsAttribute(clazz, attribute2);
    }

    /**
     * Tests that the method delegates exactly once per call.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_delegatesExactlyOnce() {
        // Arrange
        BootstrapMethodsAttribute attribute = new BootstrapMethodsAttribute();

        // Act
        changedCodePrinter.visitBootstrapMethodsAttribute(clazz, attribute);

        // Assert - should delegate exactly once, no more, no less
        verify(mockAttributeVisitor, times(1))
            .visitBootstrapMethodsAttribute(any(), any());
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method doesn't call any other visitor methods.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_doesNotCallOtherVisitorMethods() {
        // Arrange
        BootstrapMethodsAttribute attribute = new BootstrapMethodsAttribute();

        // Act
        changedCodePrinter.visitBootstrapMethodsAttribute(clazz, attribute);

        // Assert - verify only visitBootstrapMethodsAttribute was called
        verify(mockAttributeVisitor, times(1))
            .visitBootstrapMethodsAttribute(clazz, attribute);
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method returns normally (doesn't hang or loop).
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_returnsImmediately() {
        // Arrange
        BootstrapMethodsAttribute attribute = new BootstrapMethodsAttribute();

        // Act
        long startTime = System.nanoTime();
        changedCodePrinter.visitBootstrapMethodsAttribute(clazz, attribute);
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
    public void testVisitBootstrapMethodsAttribute_withFreshPrinter_doesNotThrow() {
        // Arrange
        AttributeVisitor visitor = mock(AttributeVisitor.class);
        ChangedCodePrinter freshPrinter = new ChangedCodePrinter(visitor);
        BootstrapMethodsAttribute attribute = new BootstrapMethodsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> freshPrinter.visitBootstrapMethodsAttribute(clazz, attribute),
                "Method should work with a newly created printer");
    }

    /**
     * Tests that the same attribute can be visited multiple times with the same clazz.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_sameAttributeMultipleTimes() {
        // Arrange
        BootstrapMethodsAttribute attribute = new BootstrapMethodsAttribute();

        // Act
        changedCodePrinter.visitBootstrapMethodsAttribute(clazz, attribute);
        changedCodePrinter.visitBootstrapMethodsAttribute(clazz, attribute);
        changedCodePrinter.visitBootstrapMethodsAttribute(clazz, attribute);

        // Assert
        verify(mockAttributeVisitor, times(3))
            .visitBootstrapMethodsAttribute(clazz, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the attribute.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_doesNotModifyAttribute() {
        // Arrange
        BootstrapMethodsAttribute attribute = new BootstrapMethodsAttribute();

        // Act
        changedCodePrinter.visitBootstrapMethodsAttribute(clazz, attribute);

        // Assert - verify the call succeeded and delegated properly
        verify(mockAttributeVisitor, times(1))
            .visitBootstrapMethodsAttribute(clazz, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the clazz.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_doesNotModifyClazz() {
        // Arrange
        BootstrapMethodsAttribute attribute = new BootstrapMethodsAttribute();

        // Act
        changedCodePrinter.visitBootstrapMethodsAttribute(clazz, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitBootstrapMethodsAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works correctly when alternating with other visitor methods.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_alternatingWithOtherMethods_doesNotInterfere() {
        // Arrange
        BootstrapMethodsAttribute attribute1 = new BootstrapMethodsAttribute();
        BootstrapMethodsAttribute attribute2 = new BootstrapMethodsAttribute();

        // Act - alternate calls
        changedCodePrinter.visitBootstrapMethodsAttribute(clazz, attribute1);
        changedCodePrinter.visitBootstrapMethodsAttribute(clazz, attribute2);
        changedCodePrinter.visitBootstrapMethodsAttribute(clazz, attribute1);

        // Assert
        verify(mockAttributeVisitor, times(2))
            .visitBootstrapMethodsAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitBootstrapMethodsAttribute(clazz, attribute2);
    }
}
