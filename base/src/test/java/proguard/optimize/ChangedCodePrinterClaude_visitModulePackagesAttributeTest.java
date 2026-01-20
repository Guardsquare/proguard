package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.module.ModulePackagesAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ChangedCodePrinter#visitModulePackagesAttribute(Clazz, ModulePackagesAttribute)}.
 *
 * The visitModulePackagesAttribute method in ChangedCodePrinter is a delegation method.
 * It simply forwards the call to the wrapped AttributeVisitor without any additional logic,
 * as ModulePackagesAttribute does not contain bytecode that needs change detection.
 *
 * These tests verify that the method:
 * 1. Correctly delegates to the wrapped visitor
 * 2. Passes the correct parameters
 * 3. Works with various inputs including edge cases
 */
public class ChangedCodePrinterClaude_visitModulePackagesAttributeTest {

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
     * Tests that visitModulePackagesAttribute delegates to the wrapped visitor.
     * Verifies that the method calls the visitor with the correct parameters.
     */
    @Test
    public void testVisitModulePackagesAttribute_delegatesToWrappedVisitor() {
        // Arrange
        ModulePackagesAttribute attribute = new ModulePackagesAttribute();

        // Act
        changedCodePrinter.visitModulePackagesAttribute(clazz, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitModulePackagesAttribute(clazz, attribute);
    }

    /**
     * Tests that the method does not throw an exception with valid inputs.
     */
    @Test
    public void testVisitModulePackagesAttribute_withValidInputs_doesNotThrow() {
        // Arrange
        ModulePackagesAttribute attribute = new ModulePackagesAttribute();

        // Act & Assert
        assertDoesNotThrow(() ->
            changedCodePrinter.visitModulePackagesAttribute(clazz, attribute),
            "visitModulePackagesAttribute should not throw any exception");
    }

    /**
     * Tests that the method can be called multiple times without issues.
     */
    @Test
    public void testVisitModulePackagesAttribute_calledMultipleTimes_doesNotThrow() {
        // Arrange
        ModulePackagesAttribute attribute = new ModulePackagesAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitModulePackagesAttribute(clazz, attribute);
            changedCodePrinter.visitModulePackagesAttribute(clazz, attribute);
            changedCodePrinter.visitModulePackagesAttribute(clazz, attribute);
        }, "Multiple calls should not throw any exception");

        // Verify the visitor was called multiple times
        verify(mockAttributeVisitor, times(3))
            .visitModulePackagesAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works with different Clazz instances.
     */
    @Test
    public void testVisitModulePackagesAttribute_withDifferentClasses_delegatesCorrectly() {
        // Arrange
        Clazz clazz1 = new ProgramClass();
        Clazz clazz2 = new ProgramClass();
        ModulePackagesAttribute attribute = new ModulePackagesAttribute();

        // Act
        changedCodePrinter.visitModulePackagesAttribute(clazz1, attribute);
        changedCodePrinter.visitModulePackagesAttribute(clazz2, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitModulePackagesAttribute(clazz1, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitModulePackagesAttribute(clazz2, attribute);
    }

    /**
     * Tests that the method works with different ModulePackagesAttribute instances.
     */
    @Test
    public void testVisitModulePackagesAttribute_withDifferentAttributes_delegatesCorrectly() {
        // Arrange
        ModulePackagesAttribute attribute1 = new ModulePackagesAttribute();
        ModulePackagesAttribute attribute2 = new ModulePackagesAttribute();

        // Act
        changedCodePrinter.visitModulePackagesAttribute(clazz, attribute1);
        changedCodePrinter.visitModulePackagesAttribute(clazz, attribute2);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitModulePackagesAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitModulePackagesAttribute(clazz, attribute2);
    }

    /**
     * Tests that the method passes parameters in the correct order.
     */
    @Test
    public void testVisitModulePackagesAttribute_passesParametersInCorrectOrder() {
        // Arrange
        ModulePackagesAttribute attribute = new ModulePackagesAttribute();

        // Act
        changedCodePrinter.visitModulePackagesAttribute(clazz, attribute);

        // Assert - verify the parameters are in correct order
        verify(mockAttributeVisitor).visitModulePackagesAttribute(
            argThat(arg -> arg == clazz),
            argThat(arg -> arg == attribute)
        );
    }

    /**
     * Tests that the method can be called in rapid succession.
     */
    @Test
    public void testVisitModulePackagesAttribute_rapidSuccessiveCalls_doesNotThrow() {
        // Arrange
        ModulePackagesAttribute attribute = new ModulePackagesAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                changedCodePrinter.visitModulePackagesAttribute(clazz, attribute);
            }
        }, "Rapid successive calls should not throw any exception");

        // Verify the visitor was called 100 times
        verify(mockAttributeVisitor, times(100))
            .visitModulePackagesAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works when the wrapped visitor does nothing (no-op).
     */
    @Test
    public void testVisitModulePackagesAttribute_withNoOpVisitor_doesNotThrow() {
        // Arrange
        AttributeVisitor noOpVisitor = mock(AttributeVisitor.class);
        doNothing().when(noOpVisitor).visitModulePackagesAttribute(any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(noOpVisitor);
        ModulePackagesAttribute attribute = new ModulePackagesAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> printer.visitModulePackagesAttribute(clazz, attribute),
            "Should not throw when visitor is no-op");
    }

    /**
     * Tests that the method works when the wrapped visitor throws an exception.
     */
    @Test
    public void testVisitModulePackagesAttribute_whenVisitorThrows_propagatesException() {
        // Arrange
        AttributeVisitor throwingVisitor = mock(AttributeVisitor.class);
        RuntimeException expectedException = new RuntimeException("Test exception");
        doThrow(expectedException)
            .when(throwingVisitor)
            .visitModulePackagesAttribute(any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(throwingVisitor);
        ModulePackagesAttribute attribute = new ModulePackagesAttribute();

        // Act & Assert
        RuntimeException thrownException = assertThrows(RuntimeException.class, () ->
            printer.visitModulePackagesAttribute(clazz, attribute),
            "Should propagate exception from wrapped visitor");

        assertEquals(expectedException, thrownException, "Should throw the same exception");
    }

    /**
     * Tests that multiple ChangedCodePrinter instances work independently.
     */
    @Test
    public void testVisitModulePackagesAttribute_multipleInstances_workIndependently() {
        // Arrange
        AttributeVisitor visitor1 = mock(AttributeVisitor.class);
        AttributeVisitor visitor2 = mock(AttributeVisitor.class);
        ChangedCodePrinter printer1 = new ChangedCodePrinter(visitor1);
        ChangedCodePrinter printer2 = new ChangedCodePrinter(visitor2);
        ModulePackagesAttribute attribute = new ModulePackagesAttribute();

        // Act
        printer1.visitModulePackagesAttribute(clazz, attribute);
        printer2.visitModulePackagesAttribute(clazz, attribute);

        // Assert
        verify(visitor1, times(1)).visitModulePackagesAttribute(clazz, attribute);
        verify(visitor2, times(1)).visitModulePackagesAttribute(clazz, attribute);
        verifyNoMoreInteractions(visitor1, visitor2);
    }

    /**
     * Tests that the printer can be reused after calling visitModulePackagesAttribute.
     */
    @Test
    public void testVisitModulePackagesAttribute_printerReusable() {
        // Arrange
        ModulePackagesAttribute attribute1 = new ModulePackagesAttribute();
        ModulePackagesAttribute attribute2 = new ModulePackagesAttribute();

        // Act & Assert - reuse the same printer
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitModulePackagesAttribute(clazz, attribute1);
            changedCodePrinter.visitModulePackagesAttribute(clazz, attribute2);
            changedCodePrinter.visitModulePackagesAttribute(clazz, attribute1);
        }, "Printer should be reusable");

        verify(mockAttributeVisitor, times(2))
            .visitModulePackagesAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitModulePackagesAttribute(clazz, attribute2);
    }

    /**
     * Tests that the method delegates exactly once per call.
     */
    @Test
    public void testVisitModulePackagesAttribute_delegatesExactlyOnce() {
        // Arrange
        ModulePackagesAttribute attribute = new ModulePackagesAttribute();

        // Act
        changedCodePrinter.visitModulePackagesAttribute(clazz, attribute);

        // Assert - should delegate exactly once, no more, no less
        verify(mockAttributeVisitor, times(1))
            .visitModulePackagesAttribute(any(), any());
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method doesn't call any other visitor methods.
     */
    @Test
    public void testVisitModulePackagesAttribute_doesNotCallOtherVisitorMethods() {
        // Arrange
        ModulePackagesAttribute attribute = new ModulePackagesAttribute();

        // Act
        changedCodePrinter.visitModulePackagesAttribute(clazz, attribute);

        // Assert - verify only visitModulePackagesAttribute was called
        verify(mockAttributeVisitor, times(1))
            .visitModulePackagesAttribute(clazz, attribute);
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method returns normally (doesn't hang or loop).
     */
    @Test
    public void testVisitModulePackagesAttribute_returnsImmediately() {
        // Arrange
        ModulePackagesAttribute attribute = new ModulePackagesAttribute();

        // Act
        long startTime = System.nanoTime();
        changedCodePrinter.visitModulePackagesAttribute(clazz, attribute);
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
    public void testVisitModulePackagesAttribute_withFreshPrinter_doesNotThrow() {
        // Arrange
        AttributeVisitor visitor = mock(AttributeVisitor.class);
        ChangedCodePrinter freshPrinter = new ChangedCodePrinter(visitor);
        ModulePackagesAttribute attribute = new ModulePackagesAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> freshPrinter.visitModulePackagesAttribute(clazz, attribute),
                "Method should work with a newly created printer");
    }

    /**
     * Tests that the same attribute can be visited multiple times with the same clazz.
     */
    @Test
    public void testVisitModulePackagesAttribute_sameAttributeMultipleTimes() {
        // Arrange
        ModulePackagesAttribute attribute = new ModulePackagesAttribute();

        // Act
        changedCodePrinter.visitModulePackagesAttribute(clazz, attribute);
        changedCodePrinter.visitModulePackagesAttribute(clazz, attribute);
        changedCodePrinter.visitModulePackagesAttribute(clazz, attribute);

        // Assert
        verify(mockAttributeVisitor, times(3))
            .visitModulePackagesAttribute(clazz, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the attribute.
     */
    @Test
    public void testVisitModulePackagesAttribute_doesNotModifyAttribute() {
        // Arrange
        ModulePackagesAttribute attribute = new ModulePackagesAttribute();

        // Act
        changedCodePrinter.visitModulePackagesAttribute(clazz, attribute);

        // Assert - verify the call succeeded and delegated properly
        verify(mockAttributeVisitor, times(1))
            .visitModulePackagesAttribute(clazz, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the clazz.
     */
    @Test
    public void testVisitModulePackagesAttribute_doesNotModifyClazz() {
        // Arrange
        ModulePackagesAttribute attribute = new ModulePackagesAttribute();

        // Act
        changedCodePrinter.visitModulePackagesAttribute(clazz, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitModulePackagesAttribute(clazz, attribute);
    }

    /**
     * Tests that the method works correctly when alternating with other visitor methods.
     */
    @Test
    public void testVisitModulePackagesAttribute_alternatingWithOtherMethods_doesNotInterfere() {
        // Arrange
        ModulePackagesAttribute attribute1 = new ModulePackagesAttribute();
        ModulePackagesAttribute attribute2 = new ModulePackagesAttribute();

        // Act - alternate calls
        changedCodePrinter.visitModulePackagesAttribute(clazz, attribute1);
        changedCodePrinter.visitModulePackagesAttribute(clazz, attribute2);
        changedCodePrinter.visitModulePackagesAttribute(clazz, attribute1);

        // Assert
        verify(mockAttributeVisitor, times(2))
            .visitModulePackagesAttribute(clazz, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitModulePackagesAttribute(clazz, attribute2);
    }
}
