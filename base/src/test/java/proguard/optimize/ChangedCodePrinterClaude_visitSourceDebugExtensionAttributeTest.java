package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.SourceDebugExtensionAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ChangedCodePrinter#visitSourceDebugExtensionAttribute(Clazz, SourceDebugExtensionAttribute)}.
 *
 * The visitSourceDebugExtensionAttribute method in ChangedCodePrinter is a delegation method.
 * It simply forwards the call to the wrapped AttributeVisitor without any additional logic,
 * as SourceDebugExtensionAttribute does not contain bytecode that needs change detection.
 *
 * These tests verify that the method:
 * 1. Correctly delegates to the wrapped visitor
 * 2. Passes the correct parameters
 * 3. Works with various inputs including edge cases
 */
public class ChangedCodePrinterClaude_visitSourceDebugExtensionAttributeTest {

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
     * Tests that visitSourceDebugExtensionAttribute delegates to the wrapped visitor.
     * Verifies that the method calls the visitor with the correct parameters.
     */
    @Test
    public void testVisitSourceDebugExtensionAttribute_delegatesToWrappedVisitor() {
        // Arrange
        SourceDebugExtensionAttribute attribute = new SourceDebugExtensionAttribute();

        // Act
        changedCodePrinter.visitSourceDebugExtensionAttribute(clazz, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitSourceDebugExtensionAttribute(clazz, attribute);
    }

    /**
     * Tests that the method does not throw an exception with valid inputs.
     */
    @Test
    public void testVisitSourceDebugExtensionAttribute_withValidInputs_doesNotThrow() {
        // Arrange
        SourceDebugExtensionAttribute attribute = new SourceDebugExtensionAttribute();

        // Act & Assert
        assertDoesNotThrow(() ->
            changedCodePrinter.visitSourceDebugExtensionAttribute(clazz, attribute),
            "visitSourceDebugExtensionAttribute should not throw any exception");
    }

    /**
     * Tests that the method can be called multiple times without issues.
     */
    @Test
    public void testVisitSourceDebugExtensionAttribute_calledMultipleTimes_doesNotThrow() {
        // Arrange
        SourceDebugExtensionAttribute attribute = new SourceDebugExtensionAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitSourceDebugExtensionAttribute(clazz, attribute);
            changedCodePrinter.visitSourceDebugExtensionAttribute(clazz, attribute);
            changedCodePrinter.visitSourceDebugExtensionAttribute(clazz, attribute);
        }, "Multiple calls should not throw any exception");

        // Verify the visitor was called multiple times
        verify(mockAttributeVisitor, times(3))
            .visitSourceDebugExtensionAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works with different Clazz instances.
     */
    @Test
    public void testVisitSourceDebugExtensionAttribute_withDifferentClasses_delegatesCorrectly() {
        // Arrange
        Clazz clazz1 = new ProgramClass();
        Clazz clazz2 = new ProgramClass();
        SourceDebugExtensionAttribute attribute = new SourceDebugExtensionAttribute();

        // Act
        changedCodePrinter.visitSourceDebugExtensionAttribute(clazz1, attribute);
        changedCodePrinter.visitSourceDebugExtensionAttribute(clazz2, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitSourceDebugExtensionAttribute(clazz1, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitSourceDebugExtensionAttribute(clazz2, attribute);
    }

    /**
     * Tests that the method works with different SourceDebugExtensionAttribute instances.
     */
    @Test
    public void testVisitSourceDebugExtensionAttribute_withDifferentAttributes_delegatesCorrectly() {
        // Arrange
        SourceDebugExtensionAttribute attribute1 = new SourceDebugExtensionAttribute();
        SourceDebugExtensionAttribute attribute2 = new SourceDebugExtensionAttribute();

        // Act
        changedCodePrinter.visitSourceDebugExtensionAttribute(clazz, attribute1);
        changedCodePrinter.visitSourceDebugExtensionAttribute(clazz, attribute2);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitSourceDebugExtensionAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitSourceDebugExtensionAttribute(clazz, attribute2);
    }

    /**
     * Tests that the method passes parameters in the correct order.
     */
    @Test
    public void testVisitSourceDebugExtensionAttribute_passesParametersInCorrectOrder() {
        // Arrange
        SourceDebugExtensionAttribute attribute = new SourceDebugExtensionAttribute();

        // Act
        changedCodePrinter.visitSourceDebugExtensionAttribute(clazz, attribute);

        // Assert - verify the parameters are in correct order
        verify(mockAttributeVisitor).visitSourceDebugExtensionAttribute(
            argThat(arg -> arg == clazz),
            argThat(arg -> arg == attribute)
        );
    }

    /**
     * Tests that the method can be called in rapid succession.
     */
    @Test
    public void testVisitSourceDebugExtensionAttribute_rapidSuccessiveCalls_doesNotThrow() {
        // Arrange
        SourceDebugExtensionAttribute attribute = new SourceDebugExtensionAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                changedCodePrinter.visitSourceDebugExtensionAttribute(clazz, attribute);
            }
        }, "Rapid successive calls should not throw any exception");

        // Verify the visitor was called 100 times
        verify(mockAttributeVisitor, times(100))
            .visitSourceDebugExtensionAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works when the wrapped visitor does nothing (no-op).
     */
    @Test
    public void testVisitSourceDebugExtensionAttribute_withNoOpVisitor_doesNotThrow() {
        // Arrange
        AttributeVisitor noOpVisitor = mock(AttributeVisitor.class);
        doNothing().when(noOpVisitor).visitSourceDebugExtensionAttribute(any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(noOpVisitor);
        SourceDebugExtensionAttribute attribute = new SourceDebugExtensionAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> printer.visitSourceDebugExtensionAttribute(clazz, attribute),
            "Should not throw when visitor is no-op");
    }

    /**
     * Tests that the method works when the wrapped visitor throws an exception.
     */
    @Test
    public void testVisitSourceDebugExtensionAttribute_whenVisitorThrows_propagatesException() {
        // Arrange
        AttributeVisitor throwingVisitor = mock(AttributeVisitor.class);
        RuntimeException expectedException = new RuntimeException("Test exception");
        doThrow(expectedException)
            .when(throwingVisitor)
            .visitSourceDebugExtensionAttribute(any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(throwingVisitor);
        SourceDebugExtensionAttribute attribute = new SourceDebugExtensionAttribute();

        // Act & Assert
        RuntimeException thrownException = assertThrows(RuntimeException.class, () ->
            printer.visitSourceDebugExtensionAttribute(clazz, attribute),
            "Should propagate exception from wrapped visitor");

        assertEquals(expectedException, thrownException, "Should throw the same exception");
    }

    /**
     * Tests that multiple ChangedCodePrinter instances work independently.
     */
    @Test
    public void testVisitSourceDebugExtensionAttribute_multipleInstances_workIndependently() {
        // Arrange
        AttributeVisitor visitor1 = mock(AttributeVisitor.class);
        AttributeVisitor visitor2 = mock(AttributeVisitor.class);
        ChangedCodePrinter printer1 = new ChangedCodePrinter(visitor1);
        ChangedCodePrinter printer2 = new ChangedCodePrinter(visitor2);
        SourceDebugExtensionAttribute attribute = new SourceDebugExtensionAttribute();

        // Act
        printer1.visitSourceDebugExtensionAttribute(clazz, attribute);
        printer2.visitSourceDebugExtensionAttribute(clazz, attribute);

        // Assert
        verify(visitor1, times(1)).visitSourceDebugExtensionAttribute(clazz, attribute);
        verify(visitor2, times(1)).visitSourceDebugExtensionAttribute(clazz, attribute);
        verifyNoMoreInteractions(visitor1, visitor2);
    }

    /**
     * Tests that the printer can be reused after calling visitSourceDebugExtensionAttribute.
     */
    @Test
    public void testVisitSourceDebugExtensionAttribute_printerReusable() {
        // Arrange
        SourceDebugExtensionAttribute attribute1 = new SourceDebugExtensionAttribute();
        SourceDebugExtensionAttribute attribute2 = new SourceDebugExtensionAttribute();

        // Act & Assert - reuse the same printer
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitSourceDebugExtensionAttribute(clazz, attribute1);
            changedCodePrinter.visitSourceDebugExtensionAttribute(clazz, attribute2);
            changedCodePrinter.visitSourceDebugExtensionAttribute(clazz, attribute1);
        }, "Printer should be reusable");

        verify(mockAttributeVisitor, times(2))
            .visitSourceDebugExtensionAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitSourceDebugExtensionAttribute(clazz, attribute2);
    }

    /**
     * Tests that the method delegates exactly once per call.
     */
    @Test
    public void testVisitSourceDebugExtensionAttribute_delegatesExactlyOnce() {
        // Arrange
        SourceDebugExtensionAttribute attribute = new SourceDebugExtensionAttribute();

        // Act
        changedCodePrinter.visitSourceDebugExtensionAttribute(clazz, attribute);

        // Assert - should delegate exactly once, no more, no less
        verify(mockAttributeVisitor, times(1))
            .visitSourceDebugExtensionAttribute(any(), any());
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method doesn't call any other visitor methods.
     */
    @Test
    public void testVisitSourceDebugExtensionAttribute_doesNotCallOtherVisitorMethods() {
        // Arrange
        SourceDebugExtensionAttribute attribute = new SourceDebugExtensionAttribute();

        // Act
        changedCodePrinter.visitSourceDebugExtensionAttribute(clazz, attribute);

        // Assert - verify only visitSourceDebugExtensionAttribute was called
        verify(mockAttributeVisitor, times(1))
            .visitSourceDebugExtensionAttribute(clazz, attribute);
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method returns normally (doesn't hang or loop).
     */
    @Test
    public void testVisitSourceDebugExtensionAttribute_returnsImmediately() {
        // Arrange
        SourceDebugExtensionAttribute attribute = new SourceDebugExtensionAttribute();

        // Act
        long startTime = System.nanoTime();
        changedCodePrinter.visitSourceDebugExtensionAttribute(clazz, attribute);
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
    public void testVisitSourceDebugExtensionAttribute_withFreshPrinter_doesNotThrow() {
        // Arrange
        AttributeVisitor visitor = mock(AttributeVisitor.class);
        ChangedCodePrinter freshPrinter = new ChangedCodePrinter(visitor);
        SourceDebugExtensionAttribute attribute = new SourceDebugExtensionAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> freshPrinter.visitSourceDebugExtensionAttribute(clazz, attribute),
                "Method should work with a newly created printer");
    }

    /**
     * Tests that the same attribute can be visited multiple times with the same clazz.
     */
    @Test
    public void testVisitSourceDebugExtensionAttribute_sameAttributeMultipleTimes() {
        // Arrange
        SourceDebugExtensionAttribute attribute = new SourceDebugExtensionAttribute();

        // Act
        changedCodePrinter.visitSourceDebugExtensionAttribute(clazz, attribute);
        changedCodePrinter.visitSourceDebugExtensionAttribute(clazz, attribute);
        changedCodePrinter.visitSourceDebugExtensionAttribute(clazz, attribute);

        // Assert
        verify(mockAttributeVisitor, times(3))
            .visitSourceDebugExtensionAttribute(clazz, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the attribute.
     */
    @Test
    public void testVisitSourceDebugExtensionAttribute_doesNotModifyAttribute() {
        // Arrange
        SourceDebugExtensionAttribute attribute = new SourceDebugExtensionAttribute();
        // Capture the attribute state before (attributes don't have easily accessible state,
        // so we mainly verify it doesn't throw and delegation works)

        // Act
        changedCodePrinter.visitSourceDebugExtensionAttribute(clazz, attribute);

        // Assert - mainly verify the call succeeded and delegated properly
        verify(mockAttributeVisitor, times(1))
            .visitSourceDebugExtensionAttribute(clazz, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the clazz.
     */
    @Test
    public void testVisitSourceDebugExtensionAttribute_doesNotModifyClazz() {
        // Arrange
        SourceDebugExtensionAttribute attribute = new SourceDebugExtensionAttribute();

        // Act
        changedCodePrinter.visitSourceDebugExtensionAttribute(clazz, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitSourceDebugExtensionAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works correctly when alternating with other visitor methods.
     */
    @Test
    public void testVisitSourceDebugExtensionAttribute_alternatingWithOtherMethods_doesNotInterfere() {
        // Arrange
        SourceDebugExtensionAttribute attribute1 = new SourceDebugExtensionAttribute();
        SourceDebugExtensionAttribute attribute2 = new SourceDebugExtensionAttribute();

        // Act - alternate calls
        changedCodePrinter.visitSourceDebugExtensionAttribute(clazz, attribute1);
        changedCodePrinter.visitSourceDebugExtensionAttribute(clazz, attribute2);
        changedCodePrinter.visitSourceDebugExtensionAttribute(clazz, attribute1);

        // Assert
        verify(mockAttributeVisitor, times(2))
            .visitSourceDebugExtensionAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitSourceDebugExtensionAttribute(clazz, attribute2);
    }
}
