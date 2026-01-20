package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.SourceFileAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ChangedCodePrinter#visitSourceFileAttribute(Clazz, SourceFileAttribute)}.
 *
 * The visitSourceFileAttribute method in ChangedCodePrinter is a delegation method.
 * It simply forwards the call to the wrapped AttributeVisitor without any additional logic,
 * as SourceFileAttribute does not contain bytecode that needs change detection.
 *
 * These tests verify that the method:
 * 1. Correctly delegates to the wrapped visitor
 * 2. Passes the correct parameters
 * 3. Works with various inputs including edge cases
 */
public class ChangedCodePrinterClaude_visitSourceFileAttributeTest {

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
     * Tests that visitSourceFileAttribute delegates to the wrapped visitor.
     * Verifies that the method calls the visitor with the correct parameters.
     */
    @Test
    public void testVisitSourceFileAttribute_delegatesToWrappedVisitor() {
        // Arrange
        SourceFileAttribute attribute = new SourceFileAttribute();

        // Act
        changedCodePrinter.visitSourceFileAttribute(clazz, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitSourceFileAttribute(clazz, attribute);
    }

    /**
     * Tests that the method does not throw an exception with valid inputs.
     */
    @Test
    public void testVisitSourceFileAttribute_withValidInputs_doesNotThrow() {
        // Arrange
        SourceFileAttribute attribute = new SourceFileAttribute();

        // Act & Assert
        assertDoesNotThrow(() ->
            changedCodePrinter.visitSourceFileAttribute(clazz, attribute),
            "visitSourceFileAttribute should not throw any exception");
    }

    /**
     * Tests that the method can be called multiple times without issues.
     */
    @Test
    public void testVisitSourceFileAttribute_calledMultipleTimes_doesNotThrow() {
        // Arrange
        SourceFileAttribute attribute = new SourceFileAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitSourceFileAttribute(clazz, attribute);
            changedCodePrinter.visitSourceFileAttribute(clazz, attribute);
            changedCodePrinter.visitSourceFileAttribute(clazz, attribute);
        }, "Multiple calls should not throw any exception");

        // Verify the visitor was called multiple times
        verify(mockAttributeVisitor, times(3))
            .visitSourceFileAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works with different Clazz instances.
     */
    @Test
    public void testVisitSourceFileAttribute_withDifferentClasses_delegatesCorrectly() {
        // Arrange
        Clazz clazz1 = new ProgramClass();
        Clazz clazz2 = new ProgramClass();
        SourceFileAttribute attribute = new SourceFileAttribute();

        // Act
        changedCodePrinter.visitSourceFileAttribute(clazz1, attribute);
        changedCodePrinter.visitSourceFileAttribute(clazz2, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitSourceFileAttribute(clazz1, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitSourceFileAttribute(clazz2, attribute);
    }

    /**
     * Tests that the method works with different SourceFileAttribute instances.
     */
    @Test
    public void testVisitSourceFileAttribute_withDifferentAttributes_delegatesCorrectly() {
        // Arrange
        SourceFileAttribute attribute1 = new SourceFileAttribute();
        SourceFileAttribute attribute2 = new SourceFileAttribute();

        // Act
        changedCodePrinter.visitSourceFileAttribute(clazz, attribute1);
        changedCodePrinter.visitSourceFileAttribute(clazz, attribute2);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitSourceFileAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitSourceFileAttribute(clazz, attribute2);
    }

    /**
     * Tests that the method passes parameters in the correct order.
     */
    @Test
    public void testVisitSourceFileAttribute_passesParametersInCorrectOrder() {
        // Arrange
        SourceFileAttribute attribute = new SourceFileAttribute();

        // Act
        changedCodePrinter.visitSourceFileAttribute(clazz, attribute);

        // Assert - verify the parameters are in correct order
        verify(mockAttributeVisitor).visitSourceFileAttribute(
            argThat(arg -> arg == clazz),
            argThat(arg -> arg == attribute)
        );
    }

    /**
     * Tests that the method can be called in rapid succession.
     */
    @Test
    public void testVisitSourceFileAttribute_rapidSuccessiveCalls_doesNotThrow() {
        // Arrange
        SourceFileAttribute attribute = new SourceFileAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                changedCodePrinter.visitSourceFileAttribute(clazz, attribute);
            }
        }, "Rapid successive calls should not throw any exception");

        // Verify the visitor was called 100 times
        verify(mockAttributeVisitor, times(100))
            .visitSourceFileAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works when the wrapped visitor does nothing (no-op).
     */
    @Test
    public void testVisitSourceFileAttribute_withNoOpVisitor_doesNotThrow() {
        // Arrange
        AttributeVisitor noOpVisitor = mock(AttributeVisitor.class);
        doNothing().when(noOpVisitor).visitSourceFileAttribute(any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(noOpVisitor);
        SourceFileAttribute attribute = new SourceFileAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> printer.visitSourceFileAttribute(clazz, attribute),
            "Should not throw when visitor is no-op");
    }

    /**
     * Tests that the method works when the wrapped visitor throws an exception.
     */
    @Test
    public void testVisitSourceFileAttribute_whenVisitorThrows_propagatesException() {
        // Arrange
        AttributeVisitor throwingVisitor = mock(AttributeVisitor.class);
        RuntimeException expectedException = new RuntimeException("Test exception");
        doThrow(expectedException)
            .when(throwingVisitor)
            .visitSourceFileAttribute(any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(throwingVisitor);
        SourceFileAttribute attribute = new SourceFileAttribute();

        // Act & Assert
        RuntimeException thrownException = assertThrows(RuntimeException.class, () ->
            printer.visitSourceFileAttribute(clazz, attribute),
            "Should propagate exception from wrapped visitor");

        assertEquals(expectedException, thrownException, "Should throw the same exception");
    }

    /**
     * Tests that multiple ChangedCodePrinter instances work independently.
     */
    @Test
    public void testVisitSourceFileAttribute_multipleInstances_workIndependently() {
        // Arrange
        AttributeVisitor visitor1 = mock(AttributeVisitor.class);
        AttributeVisitor visitor2 = mock(AttributeVisitor.class);
        ChangedCodePrinter printer1 = new ChangedCodePrinter(visitor1);
        ChangedCodePrinter printer2 = new ChangedCodePrinter(visitor2);
        SourceFileAttribute attribute = new SourceFileAttribute();

        // Act
        printer1.visitSourceFileAttribute(clazz, attribute);
        printer2.visitSourceFileAttribute(clazz, attribute);

        // Assert
        verify(visitor1, times(1)).visitSourceFileAttribute(clazz, attribute);
        verify(visitor2, times(1)).visitSourceFileAttribute(clazz, attribute);
        verifyNoMoreInteractions(visitor1, visitor2);
    }

    /**
     * Tests that the printer can be reused after calling visitSourceFileAttribute.
     */
    @Test
    public void testVisitSourceFileAttribute_printerReusable() {
        // Arrange
        SourceFileAttribute attribute1 = new SourceFileAttribute();
        SourceFileAttribute attribute2 = new SourceFileAttribute();

        // Act & Assert - reuse the same printer
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitSourceFileAttribute(clazz, attribute1);
            changedCodePrinter.visitSourceFileAttribute(clazz, attribute2);
            changedCodePrinter.visitSourceFileAttribute(clazz, attribute1);
        }, "Printer should be reusable");

        verify(mockAttributeVisitor, times(2))
            .visitSourceFileAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitSourceFileAttribute(clazz, attribute2);
    }

    /**
     * Tests that the method delegates exactly once per call.
     */
    @Test
    public void testVisitSourceFileAttribute_delegatesExactlyOnce() {
        // Arrange
        SourceFileAttribute attribute = new SourceFileAttribute();

        // Act
        changedCodePrinter.visitSourceFileAttribute(clazz, attribute);

        // Assert - should delegate exactly once, no more, no less
        verify(mockAttributeVisitor, times(1))
            .visitSourceFileAttribute(any(), any());
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method doesn't call any other visitor methods.
     */
    @Test
    public void testVisitSourceFileAttribute_doesNotCallOtherVisitorMethods() {
        // Arrange
        SourceFileAttribute attribute = new SourceFileAttribute();

        // Act
        changedCodePrinter.visitSourceFileAttribute(clazz, attribute);

        // Assert - verify only visitSourceFileAttribute was called
        verify(mockAttributeVisitor, times(1))
            .visitSourceFileAttribute(clazz, attribute);
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method returns normally (doesn't hang or loop).
     */
    @Test
    public void testVisitSourceFileAttribute_returnsImmediately() {
        // Arrange
        SourceFileAttribute attribute = new SourceFileAttribute();

        // Act
        long startTime = System.nanoTime();
        changedCodePrinter.visitSourceFileAttribute(clazz, attribute);
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
    public void testVisitSourceFileAttribute_withFreshPrinter_doesNotThrow() {
        // Arrange
        AttributeVisitor visitor = mock(AttributeVisitor.class);
        ChangedCodePrinter freshPrinter = new ChangedCodePrinter(visitor);
        SourceFileAttribute attribute = new SourceFileAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> freshPrinter.visitSourceFileAttribute(clazz, attribute),
                "Method should work with a newly created printer");
    }

    /**
     * Tests that the same attribute can be visited multiple times with the same clazz.
     */
    @Test
    public void testVisitSourceFileAttribute_sameAttributeMultipleTimes() {
        // Arrange
        SourceFileAttribute attribute = new SourceFileAttribute();

        // Act
        changedCodePrinter.visitSourceFileAttribute(clazz, attribute);
        changedCodePrinter.visitSourceFileAttribute(clazz, attribute);
        changedCodePrinter.visitSourceFileAttribute(clazz, attribute);

        // Assert
        verify(mockAttributeVisitor, times(3))
            .visitSourceFileAttribute(clazz, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the attribute.
     */
    @Test
    public void testVisitSourceFileAttribute_doesNotModifyAttribute() {
        // Arrange
        SourceFileAttribute attribute = new SourceFileAttribute();

        // Act
        changedCodePrinter.visitSourceFileAttribute(clazz, attribute);

        // Assert - verify the call succeeded and delegated properly
        verify(mockAttributeVisitor, times(1))
            .visitSourceFileAttribute(clazz, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the clazz.
     */
    @Test
    public void testVisitSourceFileAttribute_doesNotModifyClazz() {
        // Arrange
        SourceFileAttribute attribute = new SourceFileAttribute();

        // Act
        changedCodePrinter.visitSourceFileAttribute(clazz, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitSourceFileAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works correctly when alternating with other visitor methods.
     */
    @Test
    public void testVisitSourceFileAttribute_alternatingWithOtherMethods_doesNotInterfere() {
        // Arrange
        SourceFileAttribute attribute1 = new SourceFileAttribute();
        SourceFileAttribute attribute2 = new SourceFileAttribute();

        // Act - alternate calls
        changedCodePrinter.visitSourceFileAttribute(clazz, attribute1);
        changedCodePrinter.visitSourceFileAttribute(clazz, attribute2);
        changedCodePrinter.visitSourceFileAttribute(clazz, attribute1);

        // Assert
        verify(mockAttributeVisitor, times(2))
            .visitSourceFileAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitSourceFileAttribute(clazz, attribute2);
    }
}
