package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.EnclosingMethodAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ChangedCodePrinter#visitEnclosingMethodAttribute(Clazz, EnclosingMethodAttribute)}.
 *
 * The visitEnclosingMethodAttribute method in ChangedCodePrinter is a delegation method.
 * It simply forwards the call to the wrapped AttributeVisitor without any additional logic,
 * as EnclosingMethodAttribute does not contain bytecode that needs change detection.
 *
 * These tests verify that the method:
 * 1. Correctly delegates to the wrapped visitor
 * 2. Passes the correct parameters
 * 3. Works with various inputs including edge cases
 */
public class ChangedCodePrinterClaude_visitEnclosingMethodAttributeTest {

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
     * Tests that visitEnclosingMethodAttribute delegates to the wrapped visitor.
     * Verifies that the method calls the visitor with the correct parameters.
     */
    @Test
    public void testVisitEnclosingMethodAttribute_delegatesToWrappedVisitor() {
        // Arrange
        EnclosingMethodAttribute attribute = new EnclosingMethodAttribute();

        // Act
        changedCodePrinter.visitEnclosingMethodAttribute(clazz, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitEnclosingMethodAttribute(clazz, attribute);
    }

    /**
     * Tests that the method does not throw an exception with valid inputs.
     */
    @Test
    public void testVisitEnclosingMethodAttribute_withValidInputs_doesNotThrow() {
        // Arrange
        EnclosingMethodAttribute attribute = new EnclosingMethodAttribute();

        // Act & Assert
        assertDoesNotThrow(() ->
            changedCodePrinter.visitEnclosingMethodAttribute(clazz, attribute),
            "visitEnclosingMethodAttribute should not throw any exception");
    }

    /**
     * Tests that the method can be called multiple times without issues.
     */
    @Test
    public void testVisitEnclosingMethodAttribute_calledMultipleTimes_doesNotThrow() {
        // Arrange
        EnclosingMethodAttribute attribute = new EnclosingMethodAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitEnclosingMethodAttribute(clazz, attribute);
            changedCodePrinter.visitEnclosingMethodAttribute(clazz, attribute);
            changedCodePrinter.visitEnclosingMethodAttribute(clazz, attribute);
        }, "Multiple calls should not throw any exception");

        // Verify the visitor was called multiple times
        verify(mockAttributeVisitor, times(3))
            .visitEnclosingMethodAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works with different Clazz instances.
     */
    @Test
    public void testVisitEnclosingMethodAttribute_withDifferentClasses_delegatesCorrectly() {
        // Arrange
        Clazz clazz1 = new ProgramClass();
        Clazz clazz2 = new ProgramClass();
        EnclosingMethodAttribute attribute = new EnclosingMethodAttribute();

        // Act
        changedCodePrinter.visitEnclosingMethodAttribute(clazz1, attribute);
        changedCodePrinter.visitEnclosingMethodAttribute(clazz2, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitEnclosingMethodAttribute(clazz1, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitEnclosingMethodAttribute(clazz2, attribute);
    }

    /**
     * Tests that the method works with different EnclosingMethodAttribute instances.
     */
    @Test
    public void testVisitEnclosingMethodAttribute_withDifferentAttributes_delegatesCorrectly() {
        // Arrange
        EnclosingMethodAttribute attribute1 = new EnclosingMethodAttribute();
        EnclosingMethodAttribute attribute2 = new EnclosingMethodAttribute();

        // Act
        changedCodePrinter.visitEnclosingMethodAttribute(clazz, attribute1);
        changedCodePrinter.visitEnclosingMethodAttribute(clazz, attribute2);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitEnclosingMethodAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitEnclosingMethodAttribute(clazz, attribute2);
    }

    /**
     * Tests that the method passes parameters in the correct order.
     */
    @Test
    public void testVisitEnclosingMethodAttribute_passesParametersInCorrectOrder() {
        // Arrange
        EnclosingMethodAttribute attribute = new EnclosingMethodAttribute();

        // Act
        changedCodePrinter.visitEnclosingMethodAttribute(clazz, attribute);

        // Assert - verify the parameters are in correct order
        verify(mockAttributeVisitor).visitEnclosingMethodAttribute(
            argThat(arg -> arg == clazz),
            argThat(arg -> arg == attribute)
        );
    }

    /**
     * Tests that the method can be called in rapid succession.
     */
    @Test
    public void testVisitEnclosingMethodAttribute_rapidSuccessiveCalls_doesNotThrow() {
        // Arrange
        EnclosingMethodAttribute attribute = new EnclosingMethodAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                changedCodePrinter.visitEnclosingMethodAttribute(clazz, attribute);
            }
        }, "Rapid successive calls should not throw any exception");

        // Verify the visitor was called 100 times
        verify(mockAttributeVisitor, times(100))
            .visitEnclosingMethodAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works when the wrapped visitor does nothing (no-op).
     */
    @Test
    public void testVisitEnclosingMethodAttribute_withNoOpVisitor_doesNotThrow() {
        // Arrange
        AttributeVisitor noOpVisitor = mock(AttributeVisitor.class);
        doNothing().when(noOpVisitor).visitEnclosingMethodAttribute(any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(noOpVisitor);
        EnclosingMethodAttribute attribute = new EnclosingMethodAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> printer.visitEnclosingMethodAttribute(clazz, attribute),
            "Should not throw when visitor is no-op");
    }

    /**
     * Tests that the method works when the wrapped visitor throws an exception.
     */
    @Test
    public void testVisitEnclosingMethodAttribute_whenVisitorThrows_propagatesException() {
        // Arrange
        AttributeVisitor throwingVisitor = mock(AttributeVisitor.class);
        RuntimeException expectedException = new RuntimeException("Test exception");
        doThrow(expectedException)
            .when(throwingVisitor)
            .visitEnclosingMethodAttribute(any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(throwingVisitor);
        EnclosingMethodAttribute attribute = new EnclosingMethodAttribute();

        // Act & Assert
        RuntimeException thrownException = assertThrows(RuntimeException.class, () ->
            printer.visitEnclosingMethodAttribute(clazz, attribute),
            "Should propagate exception from wrapped visitor");

        assertEquals(expectedException, thrownException, "Should throw the same exception");
    }

    /**
     * Tests that multiple ChangedCodePrinter instances work independently.
     */
    @Test
    public void testVisitEnclosingMethodAttribute_multipleInstances_workIndependently() {
        // Arrange
        AttributeVisitor visitor1 = mock(AttributeVisitor.class);
        AttributeVisitor visitor2 = mock(AttributeVisitor.class);
        ChangedCodePrinter printer1 = new ChangedCodePrinter(visitor1);
        ChangedCodePrinter printer2 = new ChangedCodePrinter(visitor2);
        EnclosingMethodAttribute attribute = new EnclosingMethodAttribute();

        // Act
        printer1.visitEnclosingMethodAttribute(clazz, attribute);
        printer2.visitEnclosingMethodAttribute(clazz, attribute);

        // Assert
        verify(visitor1, times(1)).visitEnclosingMethodAttribute(clazz, attribute);
        verify(visitor2, times(1)).visitEnclosingMethodAttribute(clazz, attribute);
        verifyNoMoreInteractions(visitor1, visitor2);
    }

    /**
     * Tests that the printer can be reused after calling visitEnclosingMethodAttribute.
     */
    @Test
    public void testVisitEnclosingMethodAttribute_printerReusable() {
        // Arrange
        EnclosingMethodAttribute attribute1 = new EnclosingMethodAttribute();
        EnclosingMethodAttribute attribute2 = new EnclosingMethodAttribute();

        // Act & Assert - reuse the same printer
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitEnclosingMethodAttribute(clazz, attribute1);
            changedCodePrinter.visitEnclosingMethodAttribute(clazz, attribute2);
            changedCodePrinter.visitEnclosingMethodAttribute(clazz, attribute1);
        }, "Printer should be reusable");

        verify(mockAttributeVisitor, times(2))
            .visitEnclosingMethodAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitEnclosingMethodAttribute(clazz, attribute2);
    }

    /**
     * Tests that the method delegates exactly once per call.
     */
    @Test
    public void testVisitEnclosingMethodAttribute_delegatesExactlyOnce() {
        // Arrange
        EnclosingMethodAttribute attribute = new EnclosingMethodAttribute();

        // Act
        changedCodePrinter.visitEnclosingMethodAttribute(clazz, attribute);

        // Assert - should delegate exactly once, no more, no less
        verify(mockAttributeVisitor, times(1))
            .visitEnclosingMethodAttribute(any(), any());
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method doesn't call any other visitor methods.
     */
    @Test
    public void testVisitEnclosingMethodAttribute_doesNotCallOtherVisitorMethods() {
        // Arrange
        EnclosingMethodAttribute attribute = new EnclosingMethodAttribute();

        // Act
        changedCodePrinter.visitEnclosingMethodAttribute(clazz, attribute);

        // Assert - verify only visitEnclosingMethodAttribute was called
        verify(mockAttributeVisitor, times(1))
            .visitEnclosingMethodAttribute(clazz, attribute);
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method returns normally (doesn't hang or loop).
     */
    @Test
    public void testVisitEnclosingMethodAttribute_returnsImmediately() {
        // Arrange
        EnclosingMethodAttribute attribute = new EnclosingMethodAttribute();

        // Act
        long startTime = System.nanoTime();
        changedCodePrinter.visitEnclosingMethodAttribute(clazz, attribute);
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
    public void testVisitEnclosingMethodAttribute_withFreshPrinter_doesNotThrow() {
        // Arrange
        AttributeVisitor visitor = mock(AttributeVisitor.class);
        ChangedCodePrinter freshPrinter = new ChangedCodePrinter(visitor);
        EnclosingMethodAttribute attribute = new EnclosingMethodAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> freshPrinter.visitEnclosingMethodAttribute(clazz, attribute),
                "Method should work with a newly created printer");
    }

    /**
     * Tests that the same attribute can be visited multiple times with the same clazz.
     */
    @Test
    public void testVisitEnclosingMethodAttribute_sameAttributeMultipleTimes() {
        // Arrange
        EnclosingMethodAttribute attribute = new EnclosingMethodAttribute();

        // Act
        changedCodePrinter.visitEnclosingMethodAttribute(clazz, attribute);
        changedCodePrinter.visitEnclosingMethodAttribute(clazz, attribute);
        changedCodePrinter.visitEnclosingMethodAttribute(clazz, attribute);

        // Assert
        verify(mockAttributeVisitor, times(3))
            .visitEnclosingMethodAttribute(clazz, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the attribute.
     */
    @Test
    public void testVisitEnclosingMethodAttribute_doesNotModifyAttribute() {
        // Arrange
        EnclosingMethodAttribute attribute = new EnclosingMethodAttribute();

        // Act
        changedCodePrinter.visitEnclosingMethodAttribute(clazz, attribute);

        // Assert - verify the call succeeded and delegated properly
        verify(mockAttributeVisitor, times(1))
            .visitEnclosingMethodAttribute(clazz, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the clazz.
     */
    @Test
    public void testVisitEnclosingMethodAttribute_doesNotModifyClazz() {
        // Arrange
        EnclosingMethodAttribute attribute = new EnclosingMethodAttribute();

        // Act
        changedCodePrinter.visitEnclosingMethodAttribute(clazz, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitEnclosingMethodAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works correctly when alternating with other visitor methods.
     */
    @Test
    public void testVisitEnclosingMethodAttribute_alternatingWithOtherMethods_doesNotInterfere() {
        // Arrange
        EnclosingMethodAttribute attribute1 = new EnclosingMethodAttribute();
        EnclosingMethodAttribute attribute2 = new EnclosingMethodAttribute();

        // Act - alternate calls
        changedCodePrinter.visitEnclosingMethodAttribute(clazz, attribute1);
        changedCodePrinter.visitEnclosingMethodAttribute(clazz, attribute2);
        changedCodePrinter.visitEnclosingMethodAttribute(clazz, attribute1);

        // Assert
        verify(mockAttributeVisitor, times(2))
            .visitEnclosingMethodAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitEnclosingMethodAttribute(clazz, attribute2);
    }
}
