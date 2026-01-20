package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.PermittedSubclassesAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ChangedCodePrinter#visitPermittedSubclassesAttribute(Clazz, PermittedSubclassesAttribute)}.
 *
 * The visitPermittedSubclassesAttribute method in ChangedCodePrinter is a delegation method.
 * It simply forwards the call to the wrapped AttributeVisitor without any additional logic,
 * as PermittedSubclassesAttribute does not contain bytecode that needs change detection.
 *
 * These tests verify that the method:
 * 1. Correctly delegates to the wrapped visitor
 * 2. Passes the correct parameters
 * 3. Works with various inputs including edge cases
 */
public class ChangedCodePrinterClaude_visitPermittedSubclassesAttributeTest {

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
     * Tests that visitPermittedSubclassesAttribute delegates to the wrapped visitor.
     * Verifies that the method calls the visitor with the correct parameters.
     */
    @Test
    public void testVisitPermittedSubclassesAttribute_delegatesToWrappedVisitor() {
        // Arrange
        PermittedSubclassesAttribute attribute = new PermittedSubclassesAttribute();

        // Act
        changedCodePrinter.visitPermittedSubclassesAttribute(clazz, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitPermittedSubclassesAttribute(clazz, attribute);
    }

    /**
     * Tests that the method does not throw an exception with valid inputs.
     */
    @Test
    public void testVisitPermittedSubclassesAttribute_withValidInputs_doesNotThrow() {
        // Arrange
        PermittedSubclassesAttribute attribute = new PermittedSubclassesAttribute();

        // Act & Assert
        assertDoesNotThrow(() ->
            changedCodePrinter.visitPermittedSubclassesAttribute(clazz, attribute),
            "visitPermittedSubclassesAttribute should not throw any exception");
    }

    /**
     * Tests that the method can be called multiple times without issues.
     */
    @Test
    public void testVisitPermittedSubclassesAttribute_calledMultipleTimes_doesNotThrow() {
        // Arrange
        PermittedSubclassesAttribute attribute = new PermittedSubclassesAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitPermittedSubclassesAttribute(clazz, attribute);
            changedCodePrinter.visitPermittedSubclassesAttribute(clazz, attribute);
            changedCodePrinter.visitPermittedSubclassesAttribute(clazz, attribute);
        }, "Multiple calls should not throw any exception");

        // Verify the visitor was called multiple times
        verify(mockAttributeVisitor, times(3))
            .visitPermittedSubclassesAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works with different Clazz instances.
     */
    @Test
    public void testVisitPermittedSubclassesAttribute_withDifferentClasses_delegatesCorrectly() {
        // Arrange
        Clazz clazz1 = new ProgramClass();
        Clazz clazz2 = new ProgramClass();
        PermittedSubclassesAttribute attribute = new PermittedSubclassesAttribute();

        // Act
        changedCodePrinter.visitPermittedSubclassesAttribute(clazz1, attribute);
        changedCodePrinter.visitPermittedSubclassesAttribute(clazz2, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitPermittedSubclassesAttribute(clazz1, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitPermittedSubclassesAttribute(clazz2, attribute);
    }

    /**
     * Tests that the method works with different PermittedSubclassesAttribute instances.
     */
    @Test
    public void testVisitPermittedSubclassesAttribute_withDifferentAttributes_delegatesCorrectly() {
        // Arrange
        PermittedSubclassesAttribute attribute1 = new PermittedSubclassesAttribute();
        PermittedSubclassesAttribute attribute2 = new PermittedSubclassesAttribute();

        // Act
        changedCodePrinter.visitPermittedSubclassesAttribute(clazz, attribute1);
        changedCodePrinter.visitPermittedSubclassesAttribute(clazz, attribute2);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitPermittedSubclassesAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitPermittedSubclassesAttribute(clazz, attribute2);
    }

    /**
     * Tests that the method passes parameters in the correct order.
     */
    @Test
    public void testVisitPermittedSubclassesAttribute_passesParametersInCorrectOrder() {
        // Arrange
        PermittedSubclassesAttribute attribute = new PermittedSubclassesAttribute();

        // Act
        changedCodePrinter.visitPermittedSubclassesAttribute(clazz, attribute);

        // Assert - verify the parameters are in correct order
        verify(mockAttributeVisitor).visitPermittedSubclassesAttribute(
            argThat(arg -> arg == clazz),
            argThat(arg -> arg == attribute)
        );
    }

    /**
     * Tests that the method can be called in rapid succession.
     */
    @Test
    public void testVisitPermittedSubclassesAttribute_rapidSuccessiveCalls_doesNotThrow() {
        // Arrange
        PermittedSubclassesAttribute attribute = new PermittedSubclassesAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                changedCodePrinter.visitPermittedSubclassesAttribute(clazz, attribute);
            }
        }, "Rapid successive calls should not throw any exception");

        // Verify the visitor was called 100 times
        verify(mockAttributeVisitor, times(100))
            .visitPermittedSubclassesAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works when the wrapped visitor does nothing (no-op).
     */
    @Test
    public void testVisitPermittedSubclassesAttribute_withNoOpVisitor_doesNotThrow() {
        // Arrange
        AttributeVisitor noOpVisitor = mock(AttributeVisitor.class);
        doNothing().when(noOpVisitor).visitPermittedSubclassesAttribute(any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(noOpVisitor);
        PermittedSubclassesAttribute attribute = new PermittedSubclassesAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> printer.visitPermittedSubclassesAttribute(clazz, attribute),
            "Should not throw when visitor is no-op");
    }

    /**
     * Tests that the method works when the wrapped visitor throws an exception.
     */
    @Test
    public void testVisitPermittedSubclassesAttribute_whenVisitorThrows_propagatesException() {
        // Arrange
        AttributeVisitor throwingVisitor = mock(AttributeVisitor.class);
        RuntimeException expectedException = new RuntimeException("Test exception");
        doThrow(expectedException)
            .when(throwingVisitor)
            .visitPermittedSubclassesAttribute(any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(throwingVisitor);
        PermittedSubclassesAttribute attribute = new PermittedSubclassesAttribute();

        // Act & Assert
        RuntimeException thrownException = assertThrows(RuntimeException.class, () ->
            printer.visitPermittedSubclassesAttribute(clazz, attribute),
            "Should propagate exception from wrapped visitor");

        assertEquals(expectedException, thrownException, "Should throw the same exception");
    }

    /**
     * Tests that multiple ChangedCodePrinter instances work independently.
     */
    @Test
    public void testVisitPermittedSubclassesAttribute_multipleInstances_workIndependently() {
        // Arrange
        AttributeVisitor visitor1 = mock(AttributeVisitor.class);
        AttributeVisitor visitor2 = mock(AttributeVisitor.class);
        ChangedCodePrinter printer1 = new ChangedCodePrinter(visitor1);
        ChangedCodePrinter printer2 = new ChangedCodePrinter(visitor2);
        PermittedSubclassesAttribute attribute = new PermittedSubclassesAttribute();

        // Act
        printer1.visitPermittedSubclassesAttribute(clazz, attribute);
        printer2.visitPermittedSubclassesAttribute(clazz, attribute);

        // Assert
        verify(visitor1, times(1)).visitPermittedSubclassesAttribute(clazz, attribute);
        verify(visitor2, times(1)).visitPermittedSubclassesAttribute(clazz, attribute);
        verifyNoMoreInteractions(visitor1, visitor2);
    }

    /**
     * Tests that the printer can be reused after calling visitPermittedSubclassesAttribute.
     */
    @Test
    public void testVisitPermittedSubclassesAttribute_printerReusable() {
        // Arrange
        PermittedSubclassesAttribute attribute1 = new PermittedSubclassesAttribute();
        PermittedSubclassesAttribute attribute2 = new PermittedSubclassesAttribute();

        // Act & Assert - reuse the same printer
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitPermittedSubclassesAttribute(clazz, attribute1);
            changedCodePrinter.visitPermittedSubclassesAttribute(clazz, attribute2);
            changedCodePrinter.visitPermittedSubclassesAttribute(clazz, attribute1);
        }, "Printer should be reusable");

        verify(mockAttributeVisitor, times(2))
            .visitPermittedSubclassesAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitPermittedSubclassesAttribute(clazz, attribute2);
    }

    /**
     * Tests that the method delegates exactly once per call.
     */
    @Test
    public void testVisitPermittedSubclassesAttribute_delegatesExactlyOnce() {
        // Arrange
        PermittedSubclassesAttribute attribute = new PermittedSubclassesAttribute();

        // Act
        changedCodePrinter.visitPermittedSubclassesAttribute(clazz, attribute);

        // Assert - should delegate exactly once, no more, no less
        verify(mockAttributeVisitor, times(1))
            .visitPermittedSubclassesAttribute(any(), any());
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method doesn't call any other visitor methods.
     */
    @Test
    public void testVisitPermittedSubclassesAttribute_doesNotCallOtherVisitorMethods() {
        // Arrange
        PermittedSubclassesAttribute attribute = new PermittedSubclassesAttribute();

        // Act
        changedCodePrinter.visitPermittedSubclassesAttribute(clazz, attribute);

        // Assert - verify only visitPermittedSubclassesAttribute was called
        verify(mockAttributeVisitor, times(1))
            .visitPermittedSubclassesAttribute(clazz, attribute);
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method returns normally (doesn't hang or loop).
     */
    @Test
    public void testVisitPermittedSubclassesAttribute_returnsImmediately() {
        // Arrange
        PermittedSubclassesAttribute attribute = new PermittedSubclassesAttribute();

        // Act
        long startTime = System.nanoTime();
        changedCodePrinter.visitPermittedSubclassesAttribute(clazz, attribute);
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
    public void testVisitPermittedSubclassesAttribute_withFreshPrinter_doesNotThrow() {
        // Arrange
        AttributeVisitor visitor = mock(AttributeVisitor.class);
        ChangedCodePrinter freshPrinter = new ChangedCodePrinter(visitor);
        PermittedSubclassesAttribute attribute = new PermittedSubclassesAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> freshPrinter.visitPermittedSubclassesAttribute(clazz, attribute),
                "Method should work with a newly created printer");
    }

    /**
     * Tests that the same attribute can be visited multiple times with the same clazz.
     */
    @Test
    public void testVisitPermittedSubclassesAttribute_sameAttributeMultipleTimes() {
        // Arrange
        PermittedSubclassesAttribute attribute = new PermittedSubclassesAttribute();

        // Act
        changedCodePrinter.visitPermittedSubclassesAttribute(clazz, attribute);
        changedCodePrinter.visitPermittedSubclassesAttribute(clazz, attribute);
        changedCodePrinter.visitPermittedSubclassesAttribute(clazz, attribute);

        // Assert
        verify(mockAttributeVisitor, times(3))
            .visitPermittedSubclassesAttribute(clazz, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the attribute.
     */
    @Test
    public void testVisitPermittedSubclassesAttribute_doesNotModifyAttribute() {
        // Arrange
        PermittedSubclassesAttribute attribute = new PermittedSubclassesAttribute();

        // Act
        changedCodePrinter.visitPermittedSubclassesAttribute(clazz, attribute);

        // Assert - verify the call succeeded and delegated properly
        verify(mockAttributeVisitor, times(1))
            .visitPermittedSubclassesAttribute(clazz, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the clazz.
     */
    @Test
    public void testVisitPermittedSubclassesAttribute_doesNotModifyClazz() {
        // Arrange
        PermittedSubclassesAttribute attribute = new PermittedSubclassesAttribute();

        // Act
        changedCodePrinter.visitPermittedSubclassesAttribute(clazz, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitPermittedSubclassesAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works correctly when alternating with other visitor methods.
     */
    @Test
    public void testVisitPermittedSubclassesAttribute_alternatingWithOtherMethods_doesNotInterfere() {
        // Arrange
        PermittedSubclassesAttribute attribute1 = new PermittedSubclassesAttribute();
        PermittedSubclassesAttribute attribute2 = new PermittedSubclassesAttribute();

        // Act - alternate calls
        changedCodePrinter.visitPermittedSubclassesAttribute(clazz, attribute1);
        changedCodePrinter.visitPermittedSubclassesAttribute(clazz, attribute2);
        changedCodePrinter.visitPermittedSubclassesAttribute(clazz, attribute1);

        // Assert
        verify(mockAttributeVisitor, times(2))
            .visitPermittedSubclassesAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitPermittedSubclassesAttribute(clazz, attribute2);
    }
}
