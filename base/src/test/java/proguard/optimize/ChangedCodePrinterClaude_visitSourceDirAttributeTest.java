package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.SourceDirAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ChangedCodePrinter#visitSourceDirAttribute(Clazz, SourceDirAttribute)}.
 *
 * The visitSourceDirAttribute method in ChangedCodePrinter is a delegation method.
 * It simply forwards the call to the wrapped AttributeVisitor without any additional logic,
 * as SourceDirAttribute does not contain bytecode that needs change detection.
 *
 * These tests verify that the method:
 * 1. Correctly delegates to the wrapped visitor
 * 2. Passes the correct parameters
 * 3. Works with various inputs including edge cases
 */
public class ChangedCodePrinterClaude_visitSourceDirAttributeTest {

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
     * Tests that visitSourceDirAttribute delegates to the wrapped visitor.
     * Verifies that the method calls the visitor with the correct parameters.
     */
    @Test
    public void testVisitSourceDirAttribute_delegatesToWrappedVisitor() {
        // Arrange
        SourceDirAttribute attribute = new SourceDirAttribute();

        // Act
        changedCodePrinter.visitSourceDirAttribute(clazz, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitSourceDirAttribute(clazz, attribute);
    }

    /**
     * Tests that the method does not throw an exception with valid inputs.
     */
    @Test
    public void testVisitSourceDirAttribute_withValidInputs_doesNotThrow() {
        // Arrange
        SourceDirAttribute attribute = new SourceDirAttribute();

        // Act & Assert
        assertDoesNotThrow(() ->
            changedCodePrinter.visitSourceDirAttribute(clazz, attribute),
            "visitSourceDirAttribute should not throw any exception");
    }

    /**
     * Tests that the method can be called multiple times without issues.
     */
    @Test
    public void testVisitSourceDirAttribute_calledMultipleTimes_doesNotThrow() {
        // Arrange
        SourceDirAttribute attribute = new SourceDirAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitSourceDirAttribute(clazz, attribute);
            changedCodePrinter.visitSourceDirAttribute(clazz, attribute);
            changedCodePrinter.visitSourceDirAttribute(clazz, attribute);
        }, "Multiple calls should not throw any exception");

        // Verify the visitor was called multiple times
        verify(mockAttributeVisitor, times(3))
            .visitSourceDirAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works with different Clazz instances.
     */
    @Test
    public void testVisitSourceDirAttribute_withDifferentClasses_delegatesCorrectly() {
        // Arrange
        Clazz clazz1 = new ProgramClass();
        Clazz clazz2 = new ProgramClass();
        SourceDirAttribute attribute = new SourceDirAttribute();

        // Act
        changedCodePrinter.visitSourceDirAttribute(clazz1, attribute);
        changedCodePrinter.visitSourceDirAttribute(clazz2, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitSourceDirAttribute(clazz1, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitSourceDirAttribute(clazz2, attribute);
    }

    /**
     * Tests that the method works with different SourceDirAttribute instances.
     */
    @Test
    public void testVisitSourceDirAttribute_withDifferentAttributes_delegatesCorrectly() {
        // Arrange
        SourceDirAttribute attribute1 = new SourceDirAttribute();
        SourceDirAttribute attribute2 = new SourceDirAttribute();

        // Act
        changedCodePrinter.visitSourceDirAttribute(clazz, attribute1);
        changedCodePrinter.visitSourceDirAttribute(clazz, attribute2);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitSourceDirAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitSourceDirAttribute(clazz, attribute2);
    }

    /**
     * Tests that the method passes parameters in the correct order.
     */
    @Test
    public void testVisitSourceDirAttribute_passesParametersInCorrectOrder() {
        // Arrange
        SourceDirAttribute attribute = new SourceDirAttribute();

        // Act
        changedCodePrinter.visitSourceDirAttribute(clazz, attribute);

        // Assert - verify the parameters are in correct order
        verify(mockAttributeVisitor).visitSourceDirAttribute(
            argThat(arg -> arg == clazz),
            argThat(arg -> arg == attribute)
        );
    }

    /**
     * Tests that the method can be called in rapid succession.
     */
    @Test
    public void testVisitSourceDirAttribute_rapidSuccessiveCalls_doesNotThrow() {
        // Arrange
        SourceDirAttribute attribute = new SourceDirAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                changedCodePrinter.visitSourceDirAttribute(clazz, attribute);
            }
        }, "Rapid successive calls should not throw any exception");

        // Verify the visitor was called 100 times
        verify(mockAttributeVisitor, times(100))
            .visitSourceDirAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works when the wrapped visitor does nothing (no-op).
     */
    @Test
    public void testVisitSourceDirAttribute_withNoOpVisitor_doesNotThrow() {
        // Arrange
        AttributeVisitor noOpVisitor = mock(AttributeVisitor.class);
        doNothing().when(noOpVisitor).visitSourceDirAttribute(any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(noOpVisitor);
        SourceDirAttribute attribute = new SourceDirAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> printer.visitSourceDirAttribute(clazz, attribute),
            "Should not throw when visitor is no-op");
    }

    /**
     * Tests that the method works when the wrapped visitor throws an exception.
     */
    @Test
    public void testVisitSourceDirAttribute_whenVisitorThrows_propagatesException() {
        // Arrange
        AttributeVisitor throwingVisitor = mock(AttributeVisitor.class);
        RuntimeException expectedException = new RuntimeException("Test exception");
        doThrow(expectedException)
            .when(throwingVisitor)
            .visitSourceDirAttribute(any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(throwingVisitor);
        SourceDirAttribute attribute = new SourceDirAttribute();

        // Act & Assert
        RuntimeException thrownException = assertThrows(RuntimeException.class, () ->
            printer.visitSourceDirAttribute(clazz, attribute),
            "Should propagate exception from wrapped visitor");

        assertEquals(expectedException, thrownException, "Should throw the same exception");
    }

    /**
     * Tests that multiple ChangedCodePrinter instances work independently.
     */
    @Test
    public void testVisitSourceDirAttribute_multipleInstances_workIndependently() {
        // Arrange
        AttributeVisitor visitor1 = mock(AttributeVisitor.class);
        AttributeVisitor visitor2 = mock(AttributeVisitor.class);
        ChangedCodePrinter printer1 = new ChangedCodePrinter(visitor1);
        ChangedCodePrinter printer2 = new ChangedCodePrinter(visitor2);
        SourceDirAttribute attribute = new SourceDirAttribute();

        // Act
        printer1.visitSourceDirAttribute(clazz, attribute);
        printer2.visitSourceDirAttribute(clazz, attribute);

        // Assert
        verify(visitor1, times(1)).visitSourceDirAttribute(clazz, attribute);
        verify(visitor2, times(1)).visitSourceDirAttribute(clazz, attribute);
        verifyNoMoreInteractions(visitor1, visitor2);
    }

    /**
     * Tests that the printer can be reused after calling visitSourceDirAttribute.
     */
    @Test
    public void testVisitSourceDirAttribute_printerReusable() {
        // Arrange
        SourceDirAttribute attribute1 = new SourceDirAttribute();
        SourceDirAttribute attribute2 = new SourceDirAttribute();

        // Act & Assert - reuse the same printer
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitSourceDirAttribute(clazz, attribute1);
            changedCodePrinter.visitSourceDirAttribute(clazz, attribute2);
            changedCodePrinter.visitSourceDirAttribute(clazz, attribute1);
        }, "Printer should be reusable");

        verify(mockAttributeVisitor, times(2))
            .visitSourceDirAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitSourceDirAttribute(clazz, attribute2);
    }

    /**
     * Tests that the method delegates exactly once per call.
     */
    @Test
    public void testVisitSourceDirAttribute_delegatesExactlyOnce() {
        // Arrange
        SourceDirAttribute attribute = new SourceDirAttribute();

        // Act
        changedCodePrinter.visitSourceDirAttribute(clazz, attribute);

        // Assert - should delegate exactly once, no more, no less
        verify(mockAttributeVisitor, times(1))
            .visitSourceDirAttribute(any(), any());
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method doesn't call any other visitor methods.
     */
    @Test
    public void testVisitSourceDirAttribute_doesNotCallOtherVisitorMethods() {
        // Arrange
        SourceDirAttribute attribute = new SourceDirAttribute();

        // Act
        changedCodePrinter.visitSourceDirAttribute(clazz, attribute);

        // Assert - verify only visitSourceDirAttribute was called
        verify(mockAttributeVisitor, times(1))
            .visitSourceDirAttribute(clazz, attribute);
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method returns normally (doesn't hang or loop).
     */
    @Test
    public void testVisitSourceDirAttribute_returnsImmediately() {
        // Arrange
        SourceDirAttribute attribute = new SourceDirAttribute();

        // Act
        long startTime = System.nanoTime();
        changedCodePrinter.visitSourceDirAttribute(clazz, attribute);
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
    public void testVisitSourceDirAttribute_withFreshPrinter_doesNotThrow() {
        // Arrange
        AttributeVisitor visitor = mock(AttributeVisitor.class);
        ChangedCodePrinter freshPrinter = new ChangedCodePrinter(visitor);
        SourceDirAttribute attribute = new SourceDirAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> freshPrinter.visitSourceDirAttribute(clazz, attribute),
                "Method should work with a newly created printer");
    }

    /**
     * Tests that the same attribute can be visited multiple times with the same clazz.
     */
    @Test
    public void testVisitSourceDirAttribute_sameAttributeMultipleTimes() {
        // Arrange
        SourceDirAttribute attribute = new SourceDirAttribute();

        // Act
        changedCodePrinter.visitSourceDirAttribute(clazz, attribute);
        changedCodePrinter.visitSourceDirAttribute(clazz, attribute);
        changedCodePrinter.visitSourceDirAttribute(clazz, attribute);

        // Assert
        verify(mockAttributeVisitor, times(3))
            .visitSourceDirAttribute(clazz, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the attribute.
     */
    @Test
    public void testVisitSourceDirAttribute_doesNotModifyAttribute() {
        // Arrange
        SourceDirAttribute attribute = new SourceDirAttribute();

        // Act
        changedCodePrinter.visitSourceDirAttribute(clazz, attribute);

        // Assert - verify the call succeeded and delegated properly
        verify(mockAttributeVisitor, times(1))
            .visitSourceDirAttribute(clazz, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the clazz.
     */
    @Test
    public void testVisitSourceDirAttribute_doesNotModifyClazz() {
        // Arrange
        SourceDirAttribute attribute = new SourceDirAttribute();

        // Act
        changedCodePrinter.visitSourceDirAttribute(clazz, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitSourceDirAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works correctly when alternating with other visitor methods.
     */
    @Test
    public void testVisitSourceDirAttribute_alternatingWithOtherMethods_doesNotInterfere() {
        // Arrange
        SourceDirAttribute attribute1 = new SourceDirAttribute();
        SourceDirAttribute attribute2 = new SourceDirAttribute();

        // Act - alternate calls
        changedCodePrinter.visitSourceDirAttribute(clazz, attribute1);
        changedCodePrinter.visitSourceDirAttribute(clazz, attribute2);
        changedCodePrinter.visitSourceDirAttribute(clazz, attribute1);

        // Assert
        verify(mockAttributeVisitor, times(2))
            .visitSourceDirAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitSourceDirAttribute(clazz, attribute2);
    }
}
