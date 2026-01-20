package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.DeprecatedAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ChangedCodePrinter#visitDeprecatedAttribute(Clazz, DeprecatedAttribute)}.
 *
 * The visitDeprecatedAttribute method in ChangedCodePrinter is a delegation method.
 * It simply forwards the call to the wrapped AttributeVisitor without any additional logic,
 * as DeprecatedAttribute does not contain bytecode that needs change detection.
 *
 * These tests verify that the method:
 * 1. Correctly delegates to the wrapped visitor
 * 2. Passes the correct parameters
 * 3. Works with various inputs including edge cases
 */
public class ChangedCodePrinterClaude_visitDeprecatedAttributeTest {

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
     * Tests that visitDeprecatedAttribute delegates to the wrapped visitor.
     * Verifies that the method calls the visitor with the correct parameters.
     */
    @Test
    public void testVisitDeprecatedAttribute_delegatesToWrappedVisitor() {
        // Arrange
        DeprecatedAttribute attribute = new DeprecatedAttribute();

        // Act
        changedCodePrinter.visitDeprecatedAttribute(clazz, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitDeprecatedAttribute(clazz, attribute);
    }

    /**
     * Tests that the method does not throw an exception with valid inputs.
     */
    @Test
    public void testVisitDeprecatedAttribute_withValidInputs_doesNotThrow() {
        // Arrange
        DeprecatedAttribute attribute = new DeprecatedAttribute();

        // Act & Assert
        assertDoesNotThrow(() ->
            changedCodePrinter.visitDeprecatedAttribute(clazz, attribute),
            "visitDeprecatedAttribute should not throw any exception");
    }

    /**
     * Tests that the method can be called multiple times without issues.
     */
    @Test
    public void testVisitDeprecatedAttribute_calledMultipleTimes_doesNotThrow() {
        // Arrange
        DeprecatedAttribute attribute = new DeprecatedAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitDeprecatedAttribute(clazz, attribute);
            changedCodePrinter.visitDeprecatedAttribute(clazz, attribute);
            changedCodePrinter.visitDeprecatedAttribute(clazz, attribute);
        }, "Multiple calls should not throw any exception");

        // Verify the visitor was called multiple times
        verify(mockAttributeVisitor, times(3))
            .visitDeprecatedAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works with different Clazz instances.
     */
    @Test
    public void testVisitDeprecatedAttribute_withDifferentClasses_delegatesCorrectly() {
        // Arrange
        Clazz clazz1 = new ProgramClass();
        Clazz clazz2 = new ProgramClass();
        DeprecatedAttribute attribute = new DeprecatedAttribute();

        // Act
        changedCodePrinter.visitDeprecatedAttribute(clazz1, attribute);
        changedCodePrinter.visitDeprecatedAttribute(clazz2, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitDeprecatedAttribute(clazz1, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitDeprecatedAttribute(clazz2, attribute);
    }

    /**
     * Tests that the method works with different DeprecatedAttribute instances.
     */
    @Test
    public void testVisitDeprecatedAttribute_withDifferentAttributes_delegatesCorrectly() {
        // Arrange
        DeprecatedAttribute attribute1 = new DeprecatedAttribute();
        DeprecatedAttribute attribute2 = new DeprecatedAttribute();

        // Act
        changedCodePrinter.visitDeprecatedAttribute(clazz, attribute1);
        changedCodePrinter.visitDeprecatedAttribute(clazz, attribute2);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitDeprecatedAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitDeprecatedAttribute(clazz, attribute2);
    }

    /**
     * Tests that the method passes parameters in the correct order.
     */
    @Test
    public void testVisitDeprecatedAttribute_passesParametersInCorrectOrder() {
        // Arrange
        DeprecatedAttribute attribute = new DeprecatedAttribute();

        // Act
        changedCodePrinter.visitDeprecatedAttribute(clazz, attribute);

        // Assert - verify the parameters are in correct order
        verify(mockAttributeVisitor).visitDeprecatedAttribute(
            argThat(arg -> arg == clazz),
            argThat(arg -> arg == attribute)
        );
    }

    /**
     * Tests that the method can be called in rapid succession.
     */
    @Test
    public void testVisitDeprecatedAttribute_rapidSuccessiveCalls_doesNotThrow() {
        // Arrange
        DeprecatedAttribute attribute = new DeprecatedAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                changedCodePrinter.visitDeprecatedAttribute(clazz, attribute);
            }
        }, "Rapid successive calls should not throw any exception");

        // Verify the visitor was called 100 times
        verify(mockAttributeVisitor, times(100))
            .visitDeprecatedAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works when the wrapped visitor does nothing (no-op).
     */
    @Test
    public void testVisitDeprecatedAttribute_withNoOpVisitor_doesNotThrow() {
        // Arrange
        AttributeVisitor noOpVisitor = mock(AttributeVisitor.class);
        doNothing().when(noOpVisitor).visitDeprecatedAttribute(any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(noOpVisitor);
        DeprecatedAttribute attribute = new DeprecatedAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> printer.visitDeprecatedAttribute(clazz, attribute),
            "Should not throw when visitor is no-op");
    }

    /**
     * Tests that the method works when the wrapped visitor throws an exception.
     */
    @Test
    public void testVisitDeprecatedAttribute_whenVisitorThrows_propagatesException() {
        // Arrange
        AttributeVisitor throwingVisitor = mock(AttributeVisitor.class);
        RuntimeException expectedException = new RuntimeException("Test exception");
        doThrow(expectedException)
            .when(throwingVisitor)
            .visitDeprecatedAttribute(any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(throwingVisitor);
        DeprecatedAttribute attribute = new DeprecatedAttribute();

        // Act & Assert
        RuntimeException thrownException = assertThrows(RuntimeException.class, () ->
            printer.visitDeprecatedAttribute(clazz, attribute),
            "Should propagate exception from wrapped visitor");

        assertEquals(expectedException, thrownException, "Should throw the same exception");
    }

    /**
     * Tests that multiple ChangedCodePrinter instances work independently.
     */
    @Test
    public void testVisitDeprecatedAttribute_multipleInstances_workIndependently() {
        // Arrange
        AttributeVisitor visitor1 = mock(AttributeVisitor.class);
        AttributeVisitor visitor2 = mock(AttributeVisitor.class);
        ChangedCodePrinter printer1 = new ChangedCodePrinter(visitor1);
        ChangedCodePrinter printer2 = new ChangedCodePrinter(visitor2);
        DeprecatedAttribute attribute = new DeprecatedAttribute();

        // Act
        printer1.visitDeprecatedAttribute(clazz, attribute);
        printer2.visitDeprecatedAttribute(clazz, attribute);

        // Assert
        verify(visitor1, times(1)).visitDeprecatedAttribute(clazz, attribute);
        verify(visitor2, times(1)).visitDeprecatedAttribute(clazz, attribute);
        verifyNoMoreInteractions(visitor1, visitor2);
    }

    /**
     * Tests that the printer can be reused after calling visitDeprecatedAttribute.
     */
    @Test
    public void testVisitDeprecatedAttribute_printerReusable() {
        // Arrange
        DeprecatedAttribute attribute1 = new DeprecatedAttribute();
        DeprecatedAttribute attribute2 = new DeprecatedAttribute();

        // Act & Assert - reuse the same printer
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitDeprecatedAttribute(clazz, attribute1);
            changedCodePrinter.visitDeprecatedAttribute(clazz, attribute2);
            changedCodePrinter.visitDeprecatedAttribute(clazz, attribute1);
        }, "Printer should be reusable");

        verify(mockAttributeVisitor, times(2))
            .visitDeprecatedAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitDeprecatedAttribute(clazz, attribute2);
    }

    /**
     * Tests that the method delegates exactly once per call.
     */
    @Test
    public void testVisitDeprecatedAttribute_delegatesExactlyOnce() {
        // Arrange
        DeprecatedAttribute attribute = new DeprecatedAttribute();

        // Act
        changedCodePrinter.visitDeprecatedAttribute(clazz, attribute);

        // Assert - should delegate exactly once, no more, no less
        verify(mockAttributeVisitor, times(1))
            .visitDeprecatedAttribute(any(), any());
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method doesn't call any other visitor methods.
     */
    @Test
    public void testVisitDeprecatedAttribute_doesNotCallOtherVisitorMethods() {
        // Arrange
        DeprecatedAttribute attribute = new DeprecatedAttribute();

        // Act
        changedCodePrinter.visitDeprecatedAttribute(clazz, attribute);

        // Assert - verify only visitDeprecatedAttribute was called
        verify(mockAttributeVisitor, times(1))
            .visitDeprecatedAttribute(clazz, attribute);
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method returns normally (doesn't hang or loop).
     */
    @Test
    public void testVisitDeprecatedAttribute_returnsImmediately() {
        // Arrange
        DeprecatedAttribute attribute = new DeprecatedAttribute();

        // Act
        long startTime = System.nanoTime();
        changedCodePrinter.visitDeprecatedAttribute(clazz, attribute);
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
    public void testVisitDeprecatedAttribute_withFreshPrinter_doesNotThrow() {
        // Arrange
        AttributeVisitor visitor = mock(AttributeVisitor.class);
        ChangedCodePrinter freshPrinter = new ChangedCodePrinter(visitor);
        DeprecatedAttribute attribute = new DeprecatedAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> freshPrinter.visitDeprecatedAttribute(clazz, attribute),
                "Method should work with a newly created printer");
    }

    /**
     * Tests that the same attribute can be visited multiple times with the same clazz.
     */
    @Test
    public void testVisitDeprecatedAttribute_sameAttributeMultipleTimes() {
        // Arrange
        DeprecatedAttribute attribute = new DeprecatedAttribute();

        // Act
        changedCodePrinter.visitDeprecatedAttribute(clazz, attribute);
        changedCodePrinter.visitDeprecatedAttribute(clazz, attribute);
        changedCodePrinter.visitDeprecatedAttribute(clazz, attribute);

        // Assert
        verify(mockAttributeVisitor, times(3))
            .visitDeprecatedAttribute(clazz, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the attribute.
     */
    @Test
    public void testVisitDeprecatedAttribute_doesNotModifyAttribute() {
        // Arrange
        DeprecatedAttribute attribute = new DeprecatedAttribute();

        // Act
        changedCodePrinter.visitDeprecatedAttribute(clazz, attribute);

        // Assert - verify the call succeeded and delegated properly
        verify(mockAttributeVisitor, times(1))
            .visitDeprecatedAttribute(clazz, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the clazz.
     */
    @Test
    public void testVisitDeprecatedAttribute_doesNotModifyClazz() {
        // Arrange
        DeprecatedAttribute attribute = new DeprecatedAttribute();

        // Act
        changedCodePrinter.visitDeprecatedAttribute(clazz, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitDeprecatedAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works correctly when alternating with other visitor methods.
     */
    @Test
    public void testVisitDeprecatedAttribute_alternatingWithOtherMethods_doesNotInterfere() {
        // Arrange
        DeprecatedAttribute attribute1 = new DeprecatedAttribute();
        DeprecatedAttribute attribute2 = new DeprecatedAttribute();

        // Act - alternate calls
        changedCodePrinter.visitDeprecatedAttribute(clazz, attribute1);
        changedCodePrinter.visitDeprecatedAttribute(clazz, attribute2);
        changedCodePrinter.visitDeprecatedAttribute(clazz, attribute1);

        // Assert
        verify(mockAttributeVisitor, times(2))
            .visitDeprecatedAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitDeprecatedAttribute(clazz, attribute2);
    }
}
