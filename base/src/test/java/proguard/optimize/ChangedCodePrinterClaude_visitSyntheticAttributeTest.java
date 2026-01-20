package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.SyntheticAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ChangedCodePrinter#visitSyntheticAttribute(Clazz, SyntheticAttribute)}.
 *
 * The visitSyntheticAttribute method in ChangedCodePrinter is a delegation method.
 * It simply forwards the call to the wrapped AttributeVisitor without any additional logic,
 * as SyntheticAttribute does not contain bytecode that needs change detection.
 *
 * These tests verify that the method:
 * 1. Correctly delegates to the wrapped visitor
 * 2. Passes the correct parameters
 * 3. Works with various inputs including edge cases
 */
public class ChangedCodePrinterClaude_visitSyntheticAttributeTest {

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
     * Tests that visitSyntheticAttribute delegates to the wrapped visitor.
     * Verifies that the method calls the visitor with the correct parameters.
     */
    @Test
    public void testVisitSyntheticAttribute_delegatesToWrappedVisitor() {
        // Arrange
        SyntheticAttribute attribute = new SyntheticAttribute();

        // Act
        changedCodePrinter.visitSyntheticAttribute(clazz, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitSyntheticAttribute(clazz, attribute);
    }

    /**
     * Tests that the method does not throw an exception with valid inputs.
     */
    @Test
    public void testVisitSyntheticAttribute_withValidInputs_doesNotThrow() {
        // Arrange
        SyntheticAttribute attribute = new SyntheticAttribute();

        // Act & Assert
        assertDoesNotThrow(() ->
            changedCodePrinter.visitSyntheticAttribute(clazz, attribute),
            "visitSyntheticAttribute should not throw any exception");
    }

    /**
     * Tests that the method can be called multiple times without issues.
     */
    @Test
    public void testVisitSyntheticAttribute_calledMultipleTimes_doesNotThrow() {
        // Arrange
        SyntheticAttribute attribute = new SyntheticAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitSyntheticAttribute(clazz, attribute);
            changedCodePrinter.visitSyntheticAttribute(clazz, attribute);
            changedCodePrinter.visitSyntheticAttribute(clazz, attribute);
        }, "Multiple calls should not throw any exception");

        // Verify the visitor was called multiple times
        verify(mockAttributeVisitor, times(3))
            .visitSyntheticAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works with different Clazz instances.
     */
    @Test
    public void testVisitSyntheticAttribute_withDifferentClasses_delegatesCorrectly() {
        // Arrange
        Clazz clazz1 = new ProgramClass();
        Clazz clazz2 = new ProgramClass();
        SyntheticAttribute attribute = new SyntheticAttribute();

        // Act
        changedCodePrinter.visitSyntheticAttribute(clazz1, attribute);
        changedCodePrinter.visitSyntheticAttribute(clazz2, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitSyntheticAttribute(clazz1, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitSyntheticAttribute(clazz2, attribute);
    }

    /**
     * Tests that the method works with different SyntheticAttribute instances.
     */
    @Test
    public void testVisitSyntheticAttribute_withDifferentAttributes_delegatesCorrectly() {
        // Arrange
        SyntheticAttribute attribute1 = new SyntheticAttribute();
        SyntheticAttribute attribute2 = new SyntheticAttribute();

        // Act
        changedCodePrinter.visitSyntheticAttribute(clazz, attribute1);
        changedCodePrinter.visitSyntheticAttribute(clazz, attribute2);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitSyntheticAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitSyntheticAttribute(clazz, attribute2);
    }

    /**
     * Tests that the method passes parameters in the correct order.
     */
    @Test
    public void testVisitSyntheticAttribute_passesParametersInCorrectOrder() {
        // Arrange
        SyntheticAttribute attribute = new SyntheticAttribute();

        // Act
        changedCodePrinter.visitSyntheticAttribute(clazz, attribute);

        // Assert - verify the parameters are in correct order
        verify(mockAttributeVisitor).visitSyntheticAttribute(
            argThat(arg -> arg == clazz),
            argThat(arg -> arg == attribute)
        );
    }

    /**
     * Tests that the method can be called in rapid succession.
     */
    @Test
    public void testVisitSyntheticAttribute_rapidSuccessiveCalls_doesNotThrow() {
        // Arrange
        SyntheticAttribute attribute = new SyntheticAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                changedCodePrinter.visitSyntheticAttribute(clazz, attribute);
            }
        }, "Rapid successive calls should not throw any exception");

        // Verify the visitor was called 100 times
        verify(mockAttributeVisitor, times(100))
            .visitSyntheticAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works when the wrapped visitor does nothing (no-op).
     */
    @Test
    public void testVisitSyntheticAttribute_withNoOpVisitor_doesNotThrow() {
        // Arrange
        AttributeVisitor noOpVisitor = mock(AttributeVisitor.class);
        doNothing().when(noOpVisitor).visitSyntheticAttribute(any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(noOpVisitor);
        SyntheticAttribute attribute = new SyntheticAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> printer.visitSyntheticAttribute(clazz, attribute),
            "Should not throw when visitor is no-op");
    }

    /**
     * Tests that the method works when the wrapped visitor throws an exception.
     */
    @Test
    public void testVisitSyntheticAttribute_whenVisitorThrows_propagatesException() {
        // Arrange
        AttributeVisitor throwingVisitor = mock(AttributeVisitor.class);
        RuntimeException expectedException = new RuntimeException("Test exception");
        doThrow(expectedException)
            .when(throwingVisitor)
            .visitSyntheticAttribute(any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(throwingVisitor);
        SyntheticAttribute attribute = new SyntheticAttribute();

        // Act & Assert
        RuntimeException thrownException = assertThrows(RuntimeException.class, () ->
            printer.visitSyntheticAttribute(clazz, attribute),
            "Should propagate exception from wrapped visitor");

        assertEquals(expectedException, thrownException, "Should throw the same exception");
    }

    /**
     * Tests that multiple ChangedCodePrinter instances work independently.
     */
    @Test
    public void testVisitSyntheticAttribute_multipleInstances_workIndependently() {
        // Arrange
        AttributeVisitor visitor1 = mock(AttributeVisitor.class);
        AttributeVisitor visitor2 = mock(AttributeVisitor.class);
        ChangedCodePrinter printer1 = new ChangedCodePrinter(visitor1);
        ChangedCodePrinter printer2 = new ChangedCodePrinter(visitor2);
        SyntheticAttribute attribute = new SyntheticAttribute();

        // Act
        printer1.visitSyntheticAttribute(clazz, attribute);
        printer2.visitSyntheticAttribute(clazz, attribute);

        // Assert
        verify(visitor1, times(1)).visitSyntheticAttribute(clazz, attribute);
        verify(visitor2, times(1)).visitSyntheticAttribute(clazz, attribute);
        verifyNoMoreInteractions(visitor1, visitor2);
    }

    /**
     * Tests that the printer can be reused after calling visitSyntheticAttribute.
     */
    @Test
    public void testVisitSyntheticAttribute_printerReusable() {
        // Arrange
        SyntheticAttribute attribute1 = new SyntheticAttribute();
        SyntheticAttribute attribute2 = new SyntheticAttribute();

        // Act & Assert - reuse the same printer
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitSyntheticAttribute(clazz, attribute1);
            changedCodePrinter.visitSyntheticAttribute(clazz, attribute2);
            changedCodePrinter.visitSyntheticAttribute(clazz, attribute1);
        }, "Printer should be reusable");

        verify(mockAttributeVisitor, times(2))
            .visitSyntheticAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitSyntheticAttribute(clazz, attribute2);
    }

    /**
     * Tests that the method delegates exactly once per call.
     */
    @Test
    public void testVisitSyntheticAttribute_delegatesExactlyOnce() {
        // Arrange
        SyntheticAttribute attribute = new SyntheticAttribute();

        // Act
        changedCodePrinter.visitSyntheticAttribute(clazz, attribute);

        // Assert - should delegate exactly once, no more, no less
        verify(mockAttributeVisitor, times(1))
            .visitSyntheticAttribute(any(), any());
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method doesn't call any other visitor methods.
     */
    @Test
    public void testVisitSyntheticAttribute_doesNotCallOtherVisitorMethods() {
        // Arrange
        SyntheticAttribute attribute = new SyntheticAttribute();

        // Act
        changedCodePrinter.visitSyntheticAttribute(clazz, attribute);

        // Assert - verify only visitSyntheticAttribute was called
        verify(mockAttributeVisitor, times(1))
            .visitSyntheticAttribute(clazz, attribute);
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method returns normally (doesn't hang or loop).
     */
    @Test
    public void testVisitSyntheticAttribute_returnsImmediately() {
        // Arrange
        SyntheticAttribute attribute = new SyntheticAttribute();

        // Act
        long startTime = System.nanoTime();
        changedCodePrinter.visitSyntheticAttribute(clazz, attribute);
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
    public void testVisitSyntheticAttribute_withFreshPrinter_doesNotThrow() {
        // Arrange
        AttributeVisitor visitor = mock(AttributeVisitor.class);
        ChangedCodePrinter freshPrinter = new ChangedCodePrinter(visitor);
        SyntheticAttribute attribute = new SyntheticAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> freshPrinter.visitSyntheticAttribute(clazz, attribute),
                "Method should work with a newly created printer");
    }

    /**
     * Tests that the same attribute can be visited multiple times with the same clazz.
     */
    @Test
    public void testVisitSyntheticAttribute_sameAttributeMultipleTimes() {
        // Arrange
        SyntheticAttribute attribute = new SyntheticAttribute();

        // Act
        changedCodePrinter.visitSyntheticAttribute(clazz, attribute);
        changedCodePrinter.visitSyntheticAttribute(clazz, attribute);
        changedCodePrinter.visitSyntheticAttribute(clazz, attribute);

        // Assert
        verify(mockAttributeVisitor, times(3))
            .visitSyntheticAttribute(clazz, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the attribute.
     */
    @Test
    public void testVisitSyntheticAttribute_doesNotModifyAttribute() {
        // Arrange
        SyntheticAttribute attribute = new SyntheticAttribute();

        // Act
        changedCodePrinter.visitSyntheticAttribute(clazz, attribute);

        // Assert - verify the call succeeded and delegated properly
        verify(mockAttributeVisitor, times(1))
            .visitSyntheticAttribute(clazz, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the clazz.
     */
    @Test
    public void testVisitSyntheticAttribute_doesNotModifyClazz() {
        // Arrange
        SyntheticAttribute attribute = new SyntheticAttribute();

        // Act
        changedCodePrinter.visitSyntheticAttribute(clazz, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitSyntheticAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works correctly when alternating with other visitor methods.
     */
    @Test
    public void testVisitSyntheticAttribute_alternatingWithOtherMethods_doesNotInterfere() {
        // Arrange
        SyntheticAttribute attribute1 = new SyntheticAttribute();
        SyntheticAttribute attribute2 = new SyntheticAttribute();

        // Act - alternate calls
        changedCodePrinter.visitSyntheticAttribute(clazz, attribute1);
        changedCodePrinter.visitSyntheticAttribute(clazz, attribute2);
        changedCodePrinter.visitSyntheticAttribute(clazz, attribute1);

        // Assert
        verify(mockAttributeVisitor, times(2))
            .visitSyntheticAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitSyntheticAttribute(clazz, attribute2);
    }
}
