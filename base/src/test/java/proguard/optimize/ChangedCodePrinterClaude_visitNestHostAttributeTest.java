package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.NestHostAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ChangedCodePrinter#visitNestHostAttribute(Clazz, NestHostAttribute)}.
 *
 * The visitNestHostAttribute method in ChangedCodePrinter is a delegation method.
 * It simply forwards the call to the wrapped AttributeVisitor without any additional logic,
 * as NestHostAttribute does not contain bytecode that needs change detection.
 *
 * These tests verify that the method:
 * 1. Correctly delegates to the wrapped visitor
 * 2. Passes the correct parameters
 * 3. Works with various inputs including edge cases
 */
public class ChangedCodePrinterClaude_visitNestHostAttributeTest {

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
     * Tests that visitNestHostAttribute delegates to the wrapped visitor.
     * Verifies that the method calls the visitor with the correct parameters.
     */
    @Test
    public void testVisitNestHostAttribute_delegatesToWrappedVisitor() {
        // Arrange
        NestHostAttribute attribute = new NestHostAttribute();

        // Act
        changedCodePrinter.visitNestHostAttribute(clazz, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitNestHostAttribute(clazz, attribute);
    }

    /**
     * Tests that the method does not throw an exception with valid inputs.
     */
    @Test
    public void testVisitNestHostAttribute_withValidInputs_doesNotThrow() {
        // Arrange
        NestHostAttribute attribute = new NestHostAttribute();

        // Act & Assert
        assertDoesNotThrow(() ->
            changedCodePrinter.visitNestHostAttribute(clazz, attribute),
            "visitNestHostAttribute should not throw any exception");
    }

    /**
     * Tests that the method can be called multiple times without issues.
     */
    @Test
    public void testVisitNestHostAttribute_calledMultipleTimes_doesNotThrow() {
        // Arrange
        NestHostAttribute attribute = new NestHostAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitNestHostAttribute(clazz, attribute);
            changedCodePrinter.visitNestHostAttribute(clazz, attribute);
            changedCodePrinter.visitNestHostAttribute(clazz, attribute);
        }, "Multiple calls should not throw any exception");

        // Verify the visitor was called multiple times
        verify(mockAttributeVisitor, times(3))
            .visitNestHostAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works with different Clazz instances.
     */
    @Test
    public void testVisitNestHostAttribute_withDifferentClasses_delegatesCorrectly() {
        // Arrange
        Clazz clazz1 = new ProgramClass();
        Clazz clazz2 = new ProgramClass();
        NestHostAttribute attribute = new NestHostAttribute();

        // Act
        changedCodePrinter.visitNestHostAttribute(clazz1, attribute);
        changedCodePrinter.visitNestHostAttribute(clazz2, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitNestHostAttribute(clazz1, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitNestHostAttribute(clazz2, attribute);
    }

    /**
     * Tests that the method works with different NestHostAttribute instances.
     */
    @Test
    public void testVisitNestHostAttribute_withDifferentAttributes_delegatesCorrectly() {
        // Arrange
        NestHostAttribute attribute1 = new NestHostAttribute();
        NestHostAttribute attribute2 = new NestHostAttribute();

        // Act
        changedCodePrinter.visitNestHostAttribute(clazz, attribute1);
        changedCodePrinter.visitNestHostAttribute(clazz, attribute2);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitNestHostAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitNestHostAttribute(clazz, attribute2);
    }

    /**
     * Tests that the method passes parameters in the correct order.
     */
    @Test
    public void testVisitNestHostAttribute_passesParametersInCorrectOrder() {
        // Arrange
        NestHostAttribute attribute = new NestHostAttribute();

        // Act
        changedCodePrinter.visitNestHostAttribute(clazz, attribute);

        // Assert - verify the parameters are in correct order
        verify(mockAttributeVisitor).visitNestHostAttribute(
            argThat(arg -> arg == clazz),
            argThat(arg -> arg == attribute)
        );
    }

    /**
     * Tests that the method can be called in rapid succession.
     */
    @Test
    public void testVisitNestHostAttribute_rapidSuccessiveCalls_doesNotThrow() {
        // Arrange
        NestHostAttribute attribute = new NestHostAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                changedCodePrinter.visitNestHostAttribute(clazz, attribute);
            }
        }, "Rapid successive calls should not throw any exception");

        // Verify the visitor was called 100 times
        verify(mockAttributeVisitor, times(100))
            .visitNestHostAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works when the wrapped visitor does nothing (no-op).
     */
    @Test
    public void testVisitNestHostAttribute_withNoOpVisitor_doesNotThrow() {
        // Arrange
        AttributeVisitor noOpVisitor = mock(AttributeVisitor.class);
        doNothing().when(noOpVisitor).visitNestHostAttribute(any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(noOpVisitor);
        NestHostAttribute attribute = new NestHostAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> printer.visitNestHostAttribute(clazz, attribute),
            "Should not throw when visitor is no-op");
    }

    /**
     * Tests that the method works when the wrapped visitor throws an exception.
     */
    @Test
    public void testVisitNestHostAttribute_whenVisitorThrows_propagatesException() {
        // Arrange
        AttributeVisitor throwingVisitor = mock(AttributeVisitor.class);
        RuntimeException expectedException = new RuntimeException("Test exception");
        doThrow(expectedException)
            .when(throwingVisitor)
            .visitNestHostAttribute(any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(throwingVisitor);
        NestHostAttribute attribute = new NestHostAttribute();

        // Act & Assert
        RuntimeException thrownException = assertThrows(RuntimeException.class, () ->
            printer.visitNestHostAttribute(clazz, attribute),
            "Should propagate exception from wrapped visitor");

        assertEquals(expectedException, thrownException, "Should throw the same exception");
    }

    /**
     * Tests that multiple ChangedCodePrinter instances work independently.
     */
    @Test
    public void testVisitNestHostAttribute_multipleInstances_workIndependently() {
        // Arrange
        AttributeVisitor visitor1 = mock(AttributeVisitor.class);
        AttributeVisitor visitor2 = mock(AttributeVisitor.class);
        ChangedCodePrinter printer1 = new ChangedCodePrinter(visitor1);
        ChangedCodePrinter printer2 = new ChangedCodePrinter(visitor2);
        NestHostAttribute attribute = new NestHostAttribute();

        // Act
        printer1.visitNestHostAttribute(clazz, attribute);
        printer2.visitNestHostAttribute(clazz, attribute);

        // Assert
        verify(visitor1, times(1)).visitNestHostAttribute(clazz, attribute);
        verify(visitor2, times(1)).visitNestHostAttribute(clazz, attribute);
        verifyNoMoreInteractions(visitor1, visitor2);
    }

    /**
     * Tests that the printer can be reused after calling visitNestHostAttribute.
     */
    @Test
    public void testVisitNestHostAttribute_printerReusable() {
        // Arrange
        NestHostAttribute attribute1 = new NestHostAttribute();
        NestHostAttribute attribute2 = new NestHostAttribute();

        // Act & Assert - reuse the same printer
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitNestHostAttribute(clazz, attribute1);
            changedCodePrinter.visitNestHostAttribute(clazz, attribute2);
            changedCodePrinter.visitNestHostAttribute(clazz, attribute1);
        }, "Printer should be reusable");

        verify(mockAttributeVisitor, times(2))
            .visitNestHostAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitNestHostAttribute(clazz, attribute2);
    }

    /**
     * Tests that the method delegates exactly once per call.
     */
    @Test
    public void testVisitNestHostAttribute_delegatesExactlyOnce() {
        // Arrange
        NestHostAttribute attribute = new NestHostAttribute();

        // Act
        changedCodePrinter.visitNestHostAttribute(clazz, attribute);

        // Assert - should delegate exactly once, no more, no less
        verify(mockAttributeVisitor, times(1))
            .visitNestHostAttribute(any(), any());
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method doesn't call any other visitor methods.
     */
    @Test
    public void testVisitNestHostAttribute_doesNotCallOtherVisitorMethods() {
        // Arrange
        NestHostAttribute attribute = new NestHostAttribute();

        // Act
        changedCodePrinter.visitNestHostAttribute(clazz, attribute);

        // Assert - verify only visitNestHostAttribute was called
        verify(mockAttributeVisitor, times(1))
            .visitNestHostAttribute(clazz, attribute);
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method returns normally (doesn't hang or loop).
     */
    @Test
    public void testVisitNestHostAttribute_returnsImmediately() {
        // Arrange
        NestHostAttribute attribute = new NestHostAttribute();

        // Act
        long startTime = System.nanoTime();
        changedCodePrinter.visitNestHostAttribute(clazz, attribute);
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
    public void testVisitNestHostAttribute_withFreshPrinter_doesNotThrow() {
        // Arrange
        AttributeVisitor visitor = mock(AttributeVisitor.class);
        ChangedCodePrinter freshPrinter = new ChangedCodePrinter(visitor);
        NestHostAttribute attribute = new NestHostAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> freshPrinter.visitNestHostAttribute(clazz, attribute),
                "Method should work with a newly created printer");
    }

    /**
     * Tests that the same attribute can be visited multiple times with the same clazz.
     */
    @Test
    public void testVisitNestHostAttribute_sameAttributeMultipleTimes() {
        // Arrange
        NestHostAttribute attribute = new NestHostAttribute();

        // Act
        changedCodePrinter.visitNestHostAttribute(clazz, attribute);
        changedCodePrinter.visitNestHostAttribute(clazz, attribute);
        changedCodePrinter.visitNestHostAttribute(clazz, attribute);

        // Assert
        verify(mockAttributeVisitor, times(3))
            .visitNestHostAttribute(clazz, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the attribute.
     */
    @Test
    public void testVisitNestHostAttribute_doesNotModifyAttribute() {
        // Arrange
        NestHostAttribute attribute = new NestHostAttribute();

        // Act
        changedCodePrinter.visitNestHostAttribute(clazz, attribute);

        // Assert - verify the call succeeded and delegated properly
        verify(mockAttributeVisitor, times(1))
            .visitNestHostAttribute(clazz, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the clazz.
     */
    @Test
    public void testVisitNestHostAttribute_doesNotModifyClazz() {
        // Arrange
        NestHostAttribute attribute = new NestHostAttribute();

        // Act
        changedCodePrinter.visitNestHostAttribute(clazz, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitNestHostAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works correctly when alternating with other visitor methods.
     */
    @Test
    public void testVisitNestHostAttribute_alternatingWithOtherMethods_doesNotInterfere() {
        // Arrange
        NestHostAttribute attribute1 = new NestHostAttribute();
        NestHostAttribute attribute2 = new NestHostAttribute();

        // Act - alternate calls
        changedCodePrinter.visitNestHostAttribute(clazz, attribute1);
        changedCodePrinter.visitNestHostAttribute(clazz, attribute2);
        changedCodePrinter.visitNestHostAttribute(clazz, attribute1);

        // Assert
        verify(mockAttributeVisitor, times(2))
            .visitNestHostAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitNestHostAttribute(clazz, attribute2);
    }
}
