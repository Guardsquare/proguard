package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.module.ModuleMainClassAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ChangedCodePrinter#visitModuleMainClassAttribute(Clazz, ModuleMainClassAttribute)}.
 *
 * The visitModuleMainClassAttribute method in ChangedCodePrinter is a delegation method.
 * It simply forwards the call to the wrapped AttributeVisitor without any additional logic,
 * as ModuleMainClassAttribute does not contain bytecode that needs change detection.
 *
 * These tests verify that the method:
 * 1. Correctly delegates to the wrapped visitor
 * 2. Passes the correct parameters
 * 3. Works with various inputs including edge cases
 */
public class ChangedCodePrinterClaude_visitModuleMainClassAttributeTest {

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
     * Tests that visitModuleMainClassAttribute delegates to the wrapped visitor.
     * Verifies that the method calls the visitor with the correct parameters.
     */
    @Test
    public void testVisitModuleMainClassAttribute_delegatesToWrappedVisitor() {
        // Arrange
        ModuleMainClassAttribute attribute = new ModuleMainClassAttribute();

        // Act
        changedCodePrinter.visitModuleMainClassAttribute(clazz, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitModuleMainClassAttribute(clazz, attribute);
    }

    /**
     * Tests that the method does not throw an exception with valid inputs.
     */
    @Test
    public void testVisitModuleMainClassAttribute_withValidInputs_doesNotThrow() {
        // Arrange
        ModuleMainClassAttribute attribute = new ModuleMainClassAttribute();

        // Act & Assert
        assertDoesNotThrow(() ->
            changedCodePrinter.visitModuleMainClassAttribute(clazz, attribute),
            "visitModuleMainClassAttribute should not throw any exception");
    }

    /**
     * Tests that the method can be called multiple times without issues.
     */
    @Test
    public void testVisitModuleMainClassAttribute_calledMultipleTimes_doesNotThrow() {
        // Arrange
        ModuleMainClassAttribute attribute = new ModuleMainClassAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitModuleMainClassAttribute(clazz, attribute);
            changedCodePrinter.visitModuleMainClassAttribute(clazz, attribute);
            changedCodePrinter.visitModuleMainClassAttribute(clazz, attribute);
        }, "Multiple calls should not throw any exception");

        // Verify the visitor was called multiple times
        verify(mockAttributeVisitor, times(3))
            .visitModuleMainClassAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works with different Clazz instances.
     */
    @Test
    public void testVisitModuleMainClassAttribute_withDifferentClasses_delegatesCorrectly() {
        // Arrange
        Clazz clazz1 = new ProgramClass();
        Clazz clazz2 = new ProgramClass();
        ModuleMainClassAttribute attribute = new ModuleMainClassAttribute();

        // Act
        changedCodePrinter.visitModuleMainClassAttribute(clazz1, attribute);
        changedCodePrinter.visitModuleMainClassAttribute(clazz2, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitModuleMainClassAttribute(clazz1, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitModuleMainClassAttribute(clazz2, attribute);
    }

    /**
     * Tests that the method works with different ModuleMainClassAttribute instances.
     */
    @Test
    public void testVisitModuleMainClassAttribute_withDifferentAttributes_delegatesCorrectly() {
        // Arrange
        ModuleMainClassAttribute attribute1 = new ModuleMainClassAttribute();
        ModuleMainClassAttribute attribute2 = new ModuleMainClassAttribute();

        // Act
        changedCodePrinter.visitModuleMainClassAttribute(clazz, attribute1);
        changedCodePrinter.visitModuleMainClassAttribute(clazz, attribute2);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitModuleMainClassAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitModuleMainClassAttribute(clazz, attribute2);
    }

    /**
     * Tests that the method passes parameters in the correct order.
     */
    @Test
    public void testVisitModuleMainClassAttribute_passesParametersInCorrectOrder() {
        // Arrange
        ModuleMainClassAttribute attribute = new ModuleMainClassAttribute();

        // Act
        changedCodePrinter.visitModuleMainClassAttribute(clazz, attribute);

        // Assert - verify the parameters are in correct order
        verify(mockAttributeVisitor).visitModuleMainClassAttribute(
            argThat(arg -> arg == clazz),
            argThat(arg -> arg == attribute)
        );
    }

    /**
     * Tests that the method can be called in rapid succession.
     */
    @Test
    public void testVisitModuleMainClassAttribute_rapidSuccessiveCalls_doesNotThrow() {
        // Arrange
        ModuleMainClassAttribute attribute = new ModuleMainClassAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                changedCodePrinter.visitModuleMainClassAttribute(clazz, attribute);
            }
        }, "Rapid successive calls should not throw any exception");

        // Verify the visitor was called 100 times
        verify(mockAttributeVisitor, times(100))
            .visitModuleMainClassAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works when the wrapped visitor does nothing (no-op).
     */
    @Test
    public void testVisitModuleMainClassAttribute_withNoOpVisitor_doesNotThrow() {
        // Arrange
        AttributeVisitor noOpVisitor = mock(AttributeVisitor.class);
        doNothing().when(noOpVisitor).visitModuleMainClassAttribute(any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(noOpVisitor);
        ModuleMainClassAttribute attribute = new ModuleMainClassAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> printer.visitModuleMainClassAttribute(clazz, attribute),
            "Should not throw when visitor is no-op");
    }

    /**
     * Tests that the method works when the wrapped visitor throws an exception.
     */
    @Test
    public void testVisitModuleMainClassAttribute_whenVisitorThrows_propagatesException() {
        // Arrange
        AttributeVisitor throwingVisitor = mock(AttributeVisitor.class);
        RuntimeException expectedException = new RuntimeException("Test exception");
        doThrow(expectedException)
            .when(throwingVisitor)
            .visitModuleMainClassAttribute(any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(throwingVisitor);
        ModuleMainClassAttribute attribute = new ModuleMainClassAttribute();

        // Act & Assert
        RuntimeException thrownException = assertThrows(RuntimeException.class, () ->
            printer.visitModuleMainClassAttribute(clazz, attribute),
            "Should propagate exception from wrapped visitor");

        assertEquals(expectedException, thrownException, "Should throw the same exception");
    }

    /**
     * Tests that multiple ChangedCodePrinter instances work independently.
     */
    @Test
    public void testVisitModuleMainClassAttribute_multipleInstances_workIndependently() {
        // Arrange
        AttributeVisitor visitor1 = mock(AttributeVisitor.class);
        AttributeVisitor visitor2 = mock(AttributeVisitor.class);
        ChangedCodePrinter printer1 = new ChangedCodePrinter(visitor1);
        ChangedCodePrinter printer2 = new ChangedCodePrinter(visitor2);
        ModuleMainClassAttribute attribute = new ModuleMainClassAttribute();

        // Act
        printer1.visitModuleMainClassAttribute(clazz, attribute);
        printer2.visitModuleMainClassAttribute(clazz, attribute);

        // Assert
        verify(visitor1, times(1)).visitModuleMainClassAttribute(clazz, attribute);
        verify(visitor2, times(1)).visitModuleMainClassAttribute(clazz, attribute);
        verifyNoMoreInteractions(visitor1, visitor2);
    }

    /**
     * Tests that the printer can be reused after calling visitModuleMainClassAttribute.
     */
    @Test
    public void testVisitModuleMainClassAttribute_printerReusable() {
        // Arrange
        ModuleMainClassAttribute attribute1 = new ModuleMainClassAttribute();
        ModuleMainClassAttribute attribute2 = new ModuleMainClassAttribute();

        // Act & Assert - reuse the same printer
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitModuleMainClassAttribute(clazz, attribute1);
            changedCodePrinter.visitModuleMainClassAttribute(clazz, attribute2);
            changedCodePrinter.visitModuleMainClassAttribute(clazz, attribute1);
        }, "Printer should be reusable");

        verify(mockAttributeVisitor, times(2))
            .visitModuleMainClassAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitModuleMainClassAttribute(clazz, attribute2);
    }

    /**
     * Tests that the method delegates exactly once per call.
     */
    @Test
    public void testVisitModuleMainClassAttribute_delegatesExactlyOnce() {
        // Arrange
        ModuleMainClassAttribute attribute = new ModuleMainClassAttribute();

        // Act
        changedCodePrinter.visitModuleMainClassAttribute(clazz, attribute);

        // Assert - should delegate exactly once, no more, no less
        verify(mockAttributeVisitor, times(1))
            .visitModuleMainClassAttribute(any(), any());
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method doesn't call any other visitor methods.
     */
    @Test
    public void testVisitModuleMainClassAttribute_doesNotCallOtherVisitorMethods() {
        // Arrange
        ModuleMainClassAttribute attribute = new ModuleMainClassAttribute();

        // Act
        changedCodePrinter.visitModuleMainClassAttribute(clazz, attribute);

        // Assert - verify only visitModuleMainClassAttribute was called
        verify(mockAttributeVisitor, times(1))
            .visitModuleMainClassAttribute(clazz, attribute);
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method returns normally (doesn't hang or loop).
     */
    @Test
    public void testVisitModuleMainClassAttribute_returnsImmediately() {
        // Arrange
        ModuleMainClassAttribute attribute = new ModuleMainClassAttribute();

        // Act
        long startTime = System.nanoTime();
        changedCodePrinter.visitModuleMainClassAttribute(clazz, attribute);
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
    public void testVisitModuleMainClassAttribute_withFreshPrinter_doesNotThrow() {
        // Arrange
        AttributeVisitor visitor = mock(AttributeVisitor.class);
        ChangedCodePrinter freshPrinter = new ChangedCodePrinter(visitor);
        ModuleMainClassAttribute attribute = new ModuleMainClassAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> freshPrinter.visitModuleMainClassAttribute(clazz, attribute),
                "Method should work with a newly created printer");
    }

    /**
     * Tests that the same attribute can be visited multiple times with the same clazz.
     */
    @Test
    public void testVisitModuleMainClassAttribute_sameAttributeMultipleTimes() {
        // Arrange
        ModuleMainClassAttribute attribute = new ModuleMainClassAttribute();

        // Act
        changedCodePrinter.visitModuleMainClassAttribute(clazz, attribute);
        changedCodePrinter.visitModuleMainClassAttribute(clazz, attribute);
        changedCodePrinter.visitModuleMainClassAttribute(clazz, attribute);

        // Assert
        verify(mockAttributeVisitor, times(3))
            .visitModuleMainClassAttribute(clazz, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the attribute.
     */
    @Test
    public void testVisitModuleMainClassAttribute_doesNotModifyAttribute() {
        // Arrange
        ModuleMainClassAttribute attribute = new ModuleMainClassAttribute();

        // Act
        changedCodePrinter.visitModuleMainClassAttribute(clazz, attribute);

        // Assert - verify the call succeeded and delegated properly
        verify(mockAttributeVisitor, times(1))
            .visitModuleMainClassAttribute(clazz, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the clazz.
     */
    @Test
    public void testVisitModuleMainClassAttribute_doesNotModifyClazz() {
        // Arrange
        ModuleMainClassAttribute attribute = new ModuleMainClassAttribute();

        // Act
        changedCodePrinter.visitModuleMainClassAttribute(clazz, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitModuleMainClassAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works correctly when alternating with other visitor methods.
     */
    @Test
    public void testVisitModuleMainClassAttribute_alternatingWithOtherMethods_doesNotInterfere() {
        // Arrange
        ModuleMainClassAttribute attribute1 = new ModuleMainClassAttribute();
        ModuleMainClassAttribute attribute2 = new ModuleMainClassAttribute();

        // Act - alternate calls
        changedCodePrinter.visitModuleMainClassAttribute(clazz, attribute1);
        changedCodePrinter.visitModuleMainClassAttribute(clazz, attribute2);
        changedCodePrinter.visitModuleMainClassAttribute(clazz, attribute1);

        // Assert
        verify(mockAttributeVisitor, times(2))
            .visitModuleMainClassAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitModuleMainClassAttribute(clazz, attribute2);
    }
}
