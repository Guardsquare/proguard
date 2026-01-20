package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.NestMembersAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ChangedCodePrinter#visitNestMembersAttribute(Clazz, NestMembersAttribute)}.
 *
 * The visitNestMembersAttribute method in ChangedCodePrinter is a delegation method.
 * It simply forwards the call to the wrapped AttributeVisitor without any additional logic,
 * as NestMembersAttribute does not contain bytecode that needs change detection.
 *
 * These tests verify that the method:
 * 1. Correctly delegates to the wrapped visitor
 * 2. Passes the correct parameters
 * 3. Works with various inputs including edge cases
 */
public class ChangedCodePrinterClaude_visitNestMembersAttributeTest {

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
     * Tests that visitNestMembersAttribute delegates to the wrapped visitor.
     * Verifies that the method calls the visitor with the correct parameters.
     */
    @Test
    public void testVisitNestMembersAttribute_delegatesToWrappedVisitor() {
        // Arrange
        NestMembersAttribute attribute = new NestMembersAttribute();

        // Act
        changedCodePrinter.visitNestMembersAttribute(clazz, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitNestMembersAttribute(clazz, attribute);
    }

    /**
     * Tests that the method does not throw an exception with valid inputs.
     */
    @Test
    public void testVisitNestMembersAttribute_withValidInputs_doesNotThrow() {
        // Arrange
        NestMembersAttribute attribute = new NestMembersAttribute();

        // Act & Assert
        assertDoesNotThrow(() ->
            changedCodePrinter.visitNestMembersAttribute(clazz, attribute),
            "visitNestMembersAttribute should not throw any exception");
    }

    /**
     * Tests that the method can be called multiple times without issues.
     */
    @Test
    public void testVisitNestMembersAttribute_calledMultipleTimes_doesNotThrow() {
        // Arrange
        NestMembersAttribute attribute = new NestMembersAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitNestMembersAttribute(clazz, attribute);
            changedCodePrinter.visitNestMembersAttribute(clazz, attribute);
            changedCodePrinter.visitNestMembersAttribute(clazz, attribute);
        }, "Multiple calls should not throw any exception");

        // Verify the visitor was called multiple times
        verify(mockAttributeVisitor, times(3))
            .visitNestMembersAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works with different Clazz instances.
     */
    @Test
    public void testVisitNestMembersAttribute_withDifferentClasses_delegatesCorrectly() {
        // Arrange
        Clazz clazz1 = new ProgramClass();
        Clazz clazz2 = new ProgramClass();
        NestMembersAttribute attribute = new NestMembersAttribute();

        // Act
        changedCodePrinter.visitNestMembersAttribute(clazz1, attribute);
        changedCodePrinter.visitNestMembersAttribute(clazz2, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitNestMembersAttribute(clazz1, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitNestMembersAttribute(clazz2, attribute);
    }

    /**
     * Tests that the method works with different NestMembersAttribute instances.
     */
    @Test
    public void testVisitNestMembersAttribute_withDifferentAttributes_delegatesCorrectly() {
        // Arrange
        NestMembersAttribute attribute1 = new NestMembersAttribute();
        NestMembersAttribute attribute2 = new NestMembersAttribute();

        // Act
        changedCodePrinter.visitNestMembersAttribute(clazz, attribute1);
        changedCodePrinter.visitNestMembersAttribute(clazz, attribute2);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitNestMembersAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitNestMembersAttribute(clazz, attribute2);
    }

    /**
     * Tests that the method passes parameters in the correct order.
     */
    @Test
    public void testVisitNestMembersAttribute_passesParametersInCorrectOrder() {
        // Arrange
        NestMembersAttribute attribute = new NestMembersAttribute();

        // Act
        changedCodePrinter.visitNestMembersAttribute(clazz, attribute);

        // Assert - verify the parameters are in correct order
        verify(mockAttributeVisitor).visitNestMembersAttribute(
            argThat(arg -> arg == clazz),
            argThat(arg -> arg == attribute)
        );
    }

    /**
     * Tests that the method can be called in rapid succession.
     */
    @Test
    public void testVisitNestMembersAttribute_rapidSuccessiveCalls_doesNotThrow() {
        // Arrange
        NestMembersAttribute attribute = new NestMembersAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                changedCodePrinter.visitNestMembersAttribute(clazz, attribute);
            }
        }, "Rapid successive calls should not throw any exception");

        // Verify the visitor was called 100 times
        verify(mockAttributeVisitor, times(100))
            .visitNestMembersAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works when the wrapped visitor does nothing (no-op).
     */
    @Test
    public void testVisitNestMembersAttribute_withNoOpVisitor_doesNotThrow() {
        // Arrange
        AttributeVisitor noOpVisitor = mock(AttributeVisitor.class);
        doNothing().when(noOpVisitor).visitNestMembersAttribute(any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(noOpVisitor);
        NestMembersAttribute attribute = new NestMembersAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> printer.visitNestMembersAttribute(clazz, attribute),
            "Should not throw when visitor is no-op");
    }

    /**
     * Tests that the method works when the wrapped visitor throws an exception.
     */
    @Test
    public void testVisitNestMembersAttribute_whenVisitorThrows_propagatesException() {
        // Arrange
        AttributeVisitor throwingVisitor = mock(AttributeVisitor.class);
        RuntimeException expectedException = new RuntimeException("Test exception");
        doThrow(expectedException)
            .when(throwingVisitor)
            .visitNestMembersAttribute(any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(throwingVisitor);
        NestMembersAttribute attribute = new NestMembersAttribute();

        // Act & Assert
        RuntimeException thrownException = assertThrows(RuntimeException.class, () ->
            printer.visitNestMembersAttribute(clazz, attribute),
            "Should propagate exception from wrapped visitor");

        assertEquals(expectedException, thrownException, "Should throw the same exception");
    }

    /**
     * Tests that multiple ChangedCodePrinter instances work independently.
     */
    @Test
    public void testVisitNestMembersAttribute_multipleInstances_workIndependently() {
        // Arrange
        AttributeVisitor visitor1 = mock(AttributeVisitor.class);
        AttributeVisitor visitor2 = mock(AttributeVisitor.class);
        ChangedCodePrinter printer1 = new ChangedCodePrinter(visitor1);
        ChangedCodePrinter printer2 = new ChangedCodePrinter(visitor2);
        NestMembersAttribute attribute = new NestMembersAttribute();

        // Act
        printer1.visitNestMembersAttribute(clazz, attribute);
        printer2.visitNestMembersAttribute(clazz, attribute);

        // Assert
        verify(visitor1, times(1)).visitNestMembersAttribute(clazz, attribute);
        verify(visitor2, times(1)).visitNestMembersAttribute(clazz, attribute);
        verifyNoMoreInteractions(visitor1, visitor2);
    }

    /**
     * Tests that the printer can be reused after calling visitNestMembersAttribute.
     */
    @Test
    public void testVisitNestMembersAttribute_printerReusable() {
        // Arrange
        NestMembersAttribute attribute1 = new NestMembersAttribute();
        NestMembersAttribute attribute2 = new NestMembersAttribute();

        // Act & Assert - reuse the same printer
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitNestMembersAttribute(clazz, attribute1);
            changedCodePrinter.visitNestMembersAttribute(clazz, attribute2);
            changedCodePrinter.visitNestMembersAttribute(clazz, attribute1);
        }, "Printer should be reusable");

        verify(mockAttributeVisitor, times(2))
            .visitNestMembersAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitNestMembersAttribute(clazz, attribute2);
    }

    /**
     * Tests that the method delegates exactly once per call.
     */
    @Test
    public void testVisitNestMembersAttribute_delegatesExactlyOnce() {
        // Arrange
        NestMembersAttribute attribute = new NestMembersAttribute();

        // Act
        changedCodePrinter.visitNestMembersAttribute(clazz, attribute);

        // Assert - should delegate exactly once, no more, no less
        verify(mockAttributeVisitor, times(1))
            .visitNestMembersAttribute(any(), any());
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method doesn't call any other visitor methods.
     */
    @Test
    public void testVisitNestMembersAttribute_doesNotCallOtherVisitorMethods() {
        // Arrange
        NestMembersAttribute attribute = new NestMembersAttribute();

        // Act
        changedCodePrinter.visitNestMembersAttribute(clazz, attribute);

        // Assert - verify only visitNestMembersAttribute was called
        verify(mockAttributeVisitor, times(1))
            .visitNestMembersAttribute(clazz, attribute);
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method returns normally (doesn't hang or loop).
     */
    @Test
    public void testVisitNestMembersAttribute_returnsImmediately() {
        // Arrange
        NestMembersAttribute attribute = new NestMembersAttribute();

        // Act
        long startTime = System.nanoTime();
        changedCodePrinter.visitNestMembersAttribute(clazz, attribute);
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
    public void testVisitNestMembersAttribute_withFreshPrinter_doesNotThrow() {
        // Arrange
        AttributeVisitor visitor = mock(AttributeVisitor.class);
        ChangedCodePrinter freshPrinter = new ChangedCodePrinter(visitor);
        NestMembersAttribute attribute = new NestMembersAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> freshPrinter.visitNestMembersAttribute(clazz, attribute),
                "Method should work with a newly created printer");
    }

    /**
     * Tests that the same attribute can be visited multiple times with the same clazz.
     */
    @Test
    public void testVisitNestMembersAttribute_sameAttributeMultipleTimes() {
        // Arrange
        NestMembersAttribute attribute = new NestMembersAttribute();

        // Act
        changedCodePrinter.visitNestMembersAttribute(clazz, attribute);
        changedCodePrinter.visitNestMembersAttribute(clazz, attribute);
        changedCodePrinter.visitNestMembersAttribute(clazz, attribute);

        // Assert
        verify(mockAttributeVisitor, times(3))
            .visitNestMembersAttribute(clazz, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the attribute.
     */
    @Test
    public void testVisitNestMembersAttribute_doesNotModifyAttribute() {
        // Arrange
        NestMembersAttribute attribute = new NestMembersAttribute();

        // Act
        changedCodePrinter.visitNestMembersAttribute(clazz, attribute);

        // Assert - verify the call succeeded and delegated properly
        verify(mockAttributeVisitor, times(1))
            .visitNestMembersAttribute(clazz, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the clazz.
     */
    @Test
    public void testVisitNestMembersAttribute_doesNotModifyClazz() {
        // Arrange
        NestMembersAttribute attribute = new NestMembersAttribute();

        // Act
        changedCodePrinter.visitNestMembersAttribute(clazz, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitNestMembersAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works correctly when alternating with other visitor methods.
     */
    @Test
    public void testVisitNestMembersAttribute_alternatingWithOtherMethods_doesNotInterfere() {
        // Arrange
        NestMembersAttribute attribute1 = new NestMembersAttribute();
        NestMembersAttribute attribute2 = new NestMembersAttribute();

        // Act - alternate calls
        changedCodePrinter.visitNestMembersAttribute(clazz, attribute1);
        changedCodePrinter.visitNestMembersAttribute(clazz, attribute2);
        changedCodePrinter.visitNestMembersAttribute(clazz, attribute1);

        // Assert
        verify(mockAttributeVisitor, times(2))
            .visitNestMembersAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitNestMembersAttribute(clazz, attribute2);
    }
}
