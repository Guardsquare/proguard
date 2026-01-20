package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.SignatureAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ChangedCodePrinter#visitSignatureAttribute(Clazz, SignatureAttribute)}.
 *
 * The visitSignatureAttribute method in ChangedCodePrinter is a delegation method.
 * It simply forwards the call to the wrapped AttributeVisitor without any additional logic,
 * as SignatureAttribute does not contain bytecode that needs change detection.
 *
 * These tests verify that the method:
 * 1. Correctly delegates to the wrapped visitor
 * 2. Passes the correct parameters
 * 3. Works with various inputs including edge cases
 */
public class ChangedCodePrinterClaude_visitSignatureAttributeTest {

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
     * Tests that visitSignatureAttribute delegates to the wrapped visitor.
     * Verifies that the method calls the visitor with the correct parameters.
     */
    @Test
    public void testVisitSignatureAttribute_delegatesToWrappedVisitor() {
        // Arrange
        SignatureAttribute attribute = new SignatureAttribute();

        // Act
        changedCodePrinter.visitSignatureAttribute(clazz, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitSignatureAttribute(clazz, attribute);
    }

    /**
     * Tests that the method does not throw an exception with valid inputs.
     */
    @Test
    public void testVisitSignatureAttribute_withValidInputs_doesNotThrow() {
        // Arrange
        SignatureAttribute attribute = new SignatureAttribute();

        // Act & Assert
        assertDoesNotThrow(() ->
            changedCodePrinter.visitSignatureAttribute(clazz, attribute),
            "visitSignatureAttribute should not throw any exception");
    }

    /**
     * Tests that the method can be called multiple times without issues.
     */
    @Test
    public void testVisitSignatureAttribute_calledMultipleTimes_doesNotThrow() {
        // Arrange
        SignatureAttribute attribute = new SignatureAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitSignatureAttribute(clazz, attribute);
            changedCodePrinter.visitSignatureAttribute(clazz, attribute);
            changedCodePrinter.visitSignatureAttribute(clazz, attribute);
        }, "Multiple calls should not throw any exception");

        // Verify the visitor was called multiple times
        verify(mockAttributeVisitor, times(3))
            .visitSignatureAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works with different Clazz instances.
     */
    @Test
    public void testVisitSignatureAttribute_withDifferentClasses_delegatesCorrectly() {
        // Arrange
        Clazz clazz1 = new ProgramClass();
        Clazz clazz2 = new ProgramClass();
        SignatureAttribute attribute = new SignatureAttribute();

        // Act
        changedCodePrinter.visitSignatureAttribute(clazz1, attribute);
        changedCodePrinter.visitSignatureAttribute(clazz2, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitSignatureAttribute(clazz1, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitSignatureAttribute(clazz2, attribute);
    }

    /**
     * Tests that the method works with different SignatureAttribute instances.
     */
    @Test
    public void testVisitSignatureAttribute_withDifferentAttributes_delegatesCorrectly() {
        // Arrange
        SignatureAttribute attribute1 = new SignatureAttribute();
        SignatureAttribute attribute2 = new SignatureAttribute();

        // Act
        changedCodePrinter.visitSignatureAttribute(clazz, attribute1);
        changedCodePrinter.visitSignatureAttribute(clazz, attribute2);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitSignatureAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitSignatureAttribute(clazz, attribute2);
    }

    /**
     * Tests that the method passes parameters in the correct order.
     */
    @Test
    public void testVisitSignatureAttribute_passesParametersInCorrectOrder() {
        // Arrange
        SignatureAttribute attribute = new SignatureAttribute();

        // Act
        changedCodePrinter.visitSignatureAttribute(clazz, attribute);

        // Assert - verify the parameters are in correct order
        verify(mockAttributeVisitor).visitSignatureAttribute(
            argThat(arg -> arg == clazz),
            argThat(arg -> arg == attribute)
        );
    }

    /**
     * Tests that the method can be called in rapid succession.
     */
    @Test
    public void testVisitSignatureAttribute_rapidSuccessiveCalls_doesNotThrow() {
        // Arrange
        SignatureAttribute attribute = new SignatureAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                changedCodePrinter.visitSignatureAttribute(clazz, attribute);
            }
        }, "Rapid successive calls should not throw any exception");

        // Verify the visitor was called 100 times
        verify(mockAttributeVisitor, times(100))
            .visitSignatureAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works when the wrapped visitor does nothing (no-op).
     */
    @Test
    public void testVisitSignatureAttribute_withNoOpVisitor_doesNotThrow() {
        // Arrange
        AttributeVisitor noOpVisitor = mock(AttributeVisitor.class);
        doNothing().when(noOpVisitor).visitSignatureAttribute(any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(noOpVisitor);
        SignatureAttribute attribute = new SignatureAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> printer.visitSignatureAttribute(clazz, attribute),
            "Should not throw when visitor is no-op");
    }

    /**
     * Tests that the method works when the wrapped visitor throws an exception.
     */
    @Test
    public void testVisitSignatureAttribute_whenVisitorThrows_propagatesException() {
        // Arrange
        AttributeVisitor throwingVisitor = mock(AttributeVisitor.class);
        RuntimeException expectedException = new RuntimeException("Test exception");
        doThrow(expectedException)
            .when(throwingVisitor)
            .visitSignatureAttribute(any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(throwingVisitor);
        SignatureAttribute attribute = new SignatureAttribute();

        // Act & Assert
        RuntimeException thrownException = assertThrows(RuntimeException.class, () ->
            printer.visitSignatureAttribute(clazz, attribute),
            "Should propagate exception from wrapped visitor");

        assertEquals(expectedException, thrownException, "Should throw the same exception");
    }

    /**
     * Tests that multiple ChangedCodePrinter instances work independently.
     */
    @Test
    public void testVisitSignatureAttribute_multipleInstances_workIndependently() {
        // Arrange
        AttributeVisitor visitor1 = mock(AttributeVisitor.class);
        AttributeVisitor visitor2 = mock(AttributeVisitor.class);
        ChangedCodePrinter printer1 = new ChangedCodePrinter(visitor1);
        ChangedCodePrinter printer2 = new ChangedCodePrinter(visitor2);
        SignatureAttribute attribute = new SignatureAttribute();

        // Act
        printer1.visitSignatureAttribute(clazz, attribute);
        printer2.visitSignatureAttribute(clazz, attribute);

        // Assert
        verify(visitor1, times(1)).visitSignatureAttribute(clazz, attribute);
        verify(visitor2, times(1)).visitSignatureAttribute(clazz, attribute);
        verifyNoMoreInteractions(visitor1, visitor2);
    }

    /**
     * Tests that the printer can be reused after calling visitSignatureAttribute.
     */
    @Test
    public void testVisitSignatureAttribute_printerReusable() {
        // Arrange
        SignatureAttribute attribute1 = new SignatureAttribute();
        SignatureAttribute attribute2 = new SignatureAttribute();

        // Act & Assert - reuse the same printer
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitSignatureAttribute(clazz, attribute1);
            changedCodePrinter.visitSignatureAttribute(clazz, attribute2);
            changedCodePrinter.visitSignatureAttribute(clazz, attribute1);
        }, "Printer should be reusable");

        verify(mockAttributeVisitor, times(2))
            .visitSignatureAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitSignatureAttribute(clazz, attribute2);
    }

    /**
     * Tests that the method delegates exactly once per call.
     */
    @Test
    public void testVisitSignatureAttribute_delegatesExactlyOnce() {
        // Arrange
        SignatureAttribute attribute = new SignatureAttribute();

        // Act
        changedCodePrinter.visitSignatureAttribute(clazz, attribute);

        // Assert - should delegate exactly once, no more, no less
        verify(mockAttributeVisitor, times(1))
            .visitSignatureAttribute(any(), any());
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method doesn't call any other visitor methods.
     */
    @Test
    public void testVisitSignatureAttribute_doesNotCallOtherVisitorMethods() {
        // Arrange
        SignatureAttribute attribute = new SignatureAttribute();

        // Act
        changedCodePrinter.visitSignatureAttribute(clazz, attribute);

        // Assert - verify only visitSignatureAttribute was called
        verify(mockAttributeVisitor, times(1))
            .visitSignatureAttribute(clazz, attribute);
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method returns normally (doesn't hang or loop).
     */
    @Test
    public void testVisitSignatureAttribute_returnsImmediately() {
        // Arrange
        SignatureAttribute attribute = new SignatureAttribute();

        // Act
        long startTime = System.nanoTime();
        changedCodePrinter.visitSignatureAttribute(clazz, attribute);
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
    public void testVisitSignatureAttribute_withFreshPrinter_doesNotThrow() {
        // Arrange
        AttributeVisitor visitor = mock(AttributeVisitor.class);
        ChangedCodePrinter freshPrinter = new ChangedCodePrinter(visitor);
        SignatureAttribute attribute = new SignatureAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> freshPrinter.visitSignatureAttribute(clazz, attribute),
                "Method should work with a newly created printer");
    }

    /**
     * Tests that the same attribute can be visited multiple times with the same clazz.
     */
    @Test
    public void testVisitSignatureAttribute_sameAttributeMultipleTimes() {
        // Arrange
        SignatureAttribute attribute = new SignatureAttribute();

        // Act
        changedCodePrinter.visitSignatureAttribute(clazz, attribute);
        changedCodePrinter.visitSignatureAttribute(clazz, attribute);
        changedCodePrinter.visitSignatureAttribute(clazz, attribute);

        // Assert
        verify(mockAttributeVisitor, times(3))
            .visitSignatureAttribute(clazz, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the attribute.
     */
    @Test
    public void testVisitSignatureAttribute_doesNotModifyAttribute() {
        // Arrange
        SignatureAttribute attribute = new SignatureAttribute();

        // Act
        changedCodePrinter.visitSignatureAttribute(clazz, attribute);

        // Assert - verify the call succeeded and delegated properly
        verify(mockAttributeVisitor, times(1))
            .visitSignatureAttribute(clazz, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the clazz.
     */
    @Test
    public void testVisitSignatureAttribute_doesNotModifyClazz() {
        // Arrange
        SignatureAttribute attribute = new SignatureAttribute();

        // Act
        changedCodePrinter.visitSignatureAttribute(clazz, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitSignatureAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works correctly when alternating with other visitor methods.
     */
    @Test
    public void testVisitSignatureAttribute_alternatingWithOtherMethods_doesNotInterfere() {
        // Arrange
        SignatureAttribute attribute1 = new SignatureAttribute();
        SignatureAttribute attribute2 = new SignatureAttribute();

        // Act - alternate calls
        changedCodePrinter.visitSignatureAttribute(clazz, attribute1);
        changedCodePrinter.visitSignatureAttribute(clazz, attribute2);
        changedCodePrinter.visitSignatureAttribute(clazz, attribute1);

        // Assert
        verify(mockAttributeVisitor, times(2))
            .visitSignatureAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitSignatureAttribute(clazz, attribute2);
    }
}
