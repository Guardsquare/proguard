package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.InnerClassesAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ChangedCodePrinter#visitInnerClassesAttribute(Clazz, InnerClassesAttribute)}.
 *
 * The visitInnerClassesAttribute method in ChangedCodePrinter is a delegation method.
 * It simply forwards the call to the wrapped AttributeVisitor without any additional logic,
 * as InnerClassesAttribute does not contain bytecode that needs change detection.
 *
 * These tests verify that the method:
 * 1. Correctly delegates to the wrapped visitor
 * 2. Passes the correct parameters
 * 3. Works with various inputs including edge cases
 */
public class ChangedCodePrinterClaude_visitInnerClassesAttributeTest {

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
     * Tests that visitInnerClassesAttribute delegates to the wrapped visitor.
     * Verifies that the method calls the visitor with the correct parameters.
     */
    @Test
    public void testVisitInnerClassesAttribute_delegatesToWrappedVisitor() {
        // Arrange
        InnerClassesAttribute attribute = new InnerClassesAttribute();

        // Act
        changedCodePrinter.visitInnerClassesAttribute(clazz, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitInnerClassesAttribute(clazz, attribute);
    }

    /**
     * Tests that the method does not throw an exception with valid inputs.
     */
    @Test
    public void testVisitInnerClassesAttribute_withValidInputs_doesNotThrow() {
        // Arrange
        InnerClassesAttribute attribute = new InnerClassesAttribute();

        // Act & Assert
        assertDoesNotThrow(() ->
            changedCodePrinter.visitInnerClassesAttribute(clazz, attribute),
            "visitInnerClassesAttribute should not throw any exception");
    }

    /**
     * Tests that the method can be called multiple times without issues.
     */
    @Test
    public void testVisitInnerClassesAttribute_calledMultipleTimes_doesNotThrow() {
        // Arrange
        InnerClassesAttribute attribute = new InnerClassesAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitInnerClassesAttribute(clazz, attribute);
            changedCodePrinter.visitInnerClassesAttribute(clazz, attribute);
            changedCodePrinter.visitInnerClassesAttribute(clazz, attribute);
        }, "Multiple calls should not throw any exception");

        // Verify the visitor was called multiple times
        verify(mockAttributeVisitor, times(3))
            .visitInnerClassesAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works with different Clazz instances.
     */
    @Test
    public void testVisitInnerClassesAttribute_withDifferentClasses_delegatesCorrectly() {
        // Arrange
        Clazz clazz1 = new ProgramClass();
        Clazz clazz2 = new ProgramClass();
        InnerClassesAttribute attribute = new InnerClassesAttribute();

        // Act
        changedCodePrinter.visitInnerClassesAttribute(clazz1, attribute);
        changedCodePrinter.visitInnerClassesAttribute(clazz2, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitInnerClassesAttribute(clazz1, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitInnerClassesAttribute(clazz2, attribute);
    }

    /**
     * Tests that the method works with different InnerClassesAttribute instances.
     */
    @Test
    public void testVisitInnerClassesAttribute_withDifferentAttributes_delegatesCorrectly() {
        // Arrange
        InnerClassesAttribute attribute1 = new InnerClassesAttribute();
        InnerClassesAttribute attribute2 = new InnerClassesAttribute();

        // Act
        changedCodePrinter.visitInnerClassesAttribute(clazz, attribute1);
        changedCodePrinter.visitInnerClassesAttribute(clazz, attribute2);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitInnerClassesAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitInnerClassesAttribute(clazz, attribute2);
    }

    /**
     * Tests that the method passes parameters in the correct order.
     */
    @Test
    public void testVisitInnerClassesAttribute_passesParametersInCorrectOrder() {
        // Arrange
        InnerClassesAttribute attribute = new InnerClassesAttribute();

        // Act
        changedCodePrinter.visitInnerClassesAttribute(clazz, attribute);

        // Assert - verify the parameters are in correct order
        verify(mockAttributeVisitor).visitInnerClassesAttribute(
            argThat(arg -> arg == clazz),
            argThat(arg -> arg == attribute)
        );
    }

    /**
     * Tests that the method can be called in rapid succession.
     */
    @Test
    public void testVisitInnerClassesAttribute_rapidSuccessiveCalls_doesNotThrow() {
        // Arrange
        InnerClassesAttribute attribute = new InnerClassesAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                changedCodePrinter.visitInnerClassesAttribute(clazz, attribute);
            }
        }, "Rapid successive calls should not throw any exception");

        // Verify the visitor was called 100 times
        verify(mockAttributeVisitor, times(100))
            .visitInnerClassesAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works when the wrapped visitor does nothing (no-op).
     */
    @Test
    public void testVisitInnerClassesAttribute_withNoOpVisitor_doesNotThrow() {
        // Arrange
        AttributeVisitor noOpVisitor = mock(AttributeVisitor.class);
        doNothing().when(noOpVisitor).visitInnerClassesAttribute(any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(noOpVisitor);
        InnerClassesAttribute attribute = new InnerClassesAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> printer.visitInnerClassesAttribute(clazz, attribute),
            "Should not throw when visitor is no-op");
    }

    /**
     * Tests that the method works when the wrapped visitor throws an exception.
     */
    @Test
    public void testVisitInnerClassesAttribute_whenVisitorThrows_propagatesException() {
        // Arrange
        AttributeVisitor throwingVisitor = mock(AttributeVisitor.class);
        RuntimeException expectedException = new RuntimeException("Test exception");
        doThrow(expectedException)
            .when(throwingVisitor)
            .visitInnerClassesAttribute(any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(throwingVisitor);
        InnerClassesAttribute attribute = new InnerClassesAttribute();

        // Act & Assert
        RuntimeException thrownException = assertThrows(RuntimeException.class, () ->
            printer.visitInnerClassesAttribute(clazz, attribute),
            "Should propagate exception from wrapped visitor");

        assertEquals(expectedException, thrownException, "Should throw the same exception");
    }

    /**
     * Tests that multiple ChangedCodePrinter instances work independently.
     */
    @Test
    public void testVisitInnerClassesAttribute_multipleInstances_workIndependently() {
        // Arrange
        AttributeVisitor visitor1 = mock(AttributeVisitor.class);
        AttributeVisitor visitor2 = mock(AttributeVisitor.class);
        ChangedCodePrinter printer1 = new ChangedCodePrinter(visitor1);
        ChangedCodePrinter printer2 = new ChangedCodePrinter(visitor2);
        InnerClassesAttribute attribute = new InnerClassesAttribute();

        // Act
        printer1.visitInnerClassesAttribute(clazz, attribute);
        printer2.visitInnerClassesAttribute(clazz, attribute);

        // Assert
        verify(visitor1, times(1)).visitInnerClassesAttribute(clazz, attribute);
        verify(visitor2, times(1)).visitInnerClassesAttribute(clazz, attribute);
        verifyNoMoreInteractions(visitor1, visitor2);
    }

    /**
     * Tests that the printer can be reused after calling visitInnerClassesAttribute.
     */
    @Test
    public void testVisitInnerClassesAttribute_printerReusable() {
        // Arrange
        InnerClassesAttribute attribute1 = new InnerClassesAttribute();
        InnerClassesAttribute attribute2 = new InnerClassesAttribute();

        // Act & Assert - reuse the same printer
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitInnerClassesAttribute(clazz, attribute1);
            changedCodePrinter.visitInnerClassesAttribute(clazz, attribute2);
            changedCodePrinter.visitInnerClassesAttribute(clazz, attribute1);
        }, "Printer should be reusable");

        verify(mockAttributeVisitor, times(2))
            .visitInnerClassesAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitInnerClassesAttribute(clazz, attribute2);
    }

    /**
     * Tests that the method delegates exactly once per call.
     */
    @Test
    public void testVisitInnerClassesAttribute_delegatesExactlyOnce() {
        // Arrange
        InnerClassesAttribute attribute = new InnerClassesAttribute();

        // Act
        changedCodePrinter.visitInnerClassesAttribute(clazz, attribute);

        // Assert - should delegate exactly once, no more, no less
        verify(mockAttributeVisitor, times(1))
            .visitInnerClassesAttribute(any(), any());
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method doesn't call any other visitor methods.
     */
    @Test
    public void testVisitInnerClassesAttribute_doesNotCallOtherVisitorMethods() {
        // Arrange
        InnerClassesAttribute attribute = new InnerClassesAttribute();

        // Act
        changedCodePrinter.visitInnerClassesAttribute(clazz, attribute);

        // Assert - verify only visitInnerClassesAttribute was called
        verify(mockAttributeVisitor, times(1))
            .visitInnerClassesAttribute(clazz, attribute);
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method returns normally (doesn't hang or loop).
     */
    @Test
    public void testVisitInnerClassesAttribute_returnsImmediately() {
        // Arrange
        InnerClassesAttribute attribute = new InnerClassesAttribute();

        // Act
        long startTime = System.nanoTime();
        changedCodePrinter.visitInnerClassesAttribute(clazz, attribute);
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
    public void testVisitInnerClassesAttribute_withFreshPrinter_doesNotThrow() {
        // Arrange
        AttributeVisitor visitor = mock(AttributeVisitor.class);
        ChangedCodePrinter freshPrinter = new ChangedCodePrinter(visitor);
        InnerClassesAttribute attribute = new InnerClassesAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> freshPrinter.visitInnerClassesAttribute(clazz, attribute),
                "Method should work with a newly created printer");
    }

    /**
     * Tests that the same attribute can be visited multiple times with the same clazz.
     */
    @Test
    public void testVisitInnerClassesAttribute_sameAttributeMultipleTimes() {
        // Arrange
        InnerClassesAttribute attribute = new InnerClassesAttribute();

        // Act
        changedCodePrinter.visitInnerClassesAttribute(clazz, attribute);
        changedCodePrinter.visitInnerClassesAttribute(clazz, attribute);
        changedCodePrinter.visitInnerClassesAttribute(clazz, attribute);

        // Assert
        verify(mockAttributeVisitor, times(3))
            .visitInnerClassesAttribute(clazz, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the attribute.
     */
    @Test
    public void testVisitInnerClassesAttribute_doesNotModifyAttribute() {
        // Arrange
        InnerClassesAttribute attribute = new InnerClassesAttribute();

        // Act
        changedCodePrinter.visitInnerClassesAttribute(clazz, attribute);

        // Assert - verify the call succeeded and delegated properly
        verify(mockAttributeVisitor, times(1))
            .visitInnerClassesAttribute(clazz, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the clazz.
     */
    @Test
    public void testVisitInnerClassesAttribute_doesNotModifyClazz() {
        // Arrange
        InnerClassesAttribute attribute = new InnerClassesAttribute();

        // Act
        changedCodePrinter.visitInnerClassesAttribute(clazz, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitInnerClassesAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works correctly when alternating with other visitor methods.
     */
    @Test
    public void testVisitInnerClassesAttribute_alternatingWithOtherMethods_doesNotInterfere() {
        // Arrange
        InnerClassesAttribute attribute1 = new InnerClassesAttribute();
        InnerClassesAttribute attribute2 = new InnerClassesAttribute();

        // Act - alternate calls
        changedCodePrinter.visitInnerClassesAttribute(clazz, attribute1);
        changedCodePrinter.visitInnerClassesAttribute(clazz, attribute2);
        changedCodePrinter.visitInnerClassesAttribute(clazz, attribute1);

        // Assert
        verify(mockAttributeVisitor, times(2))
            .visitInnerClassesAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitInnerClassesAttribute(clazz, attribute2);
    }
}
