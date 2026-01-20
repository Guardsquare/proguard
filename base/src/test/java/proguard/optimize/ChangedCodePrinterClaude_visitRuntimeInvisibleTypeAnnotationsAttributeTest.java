package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.annotation.RuntimeInvisibleTypeAnnotationsAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ChangedCodePrinter#visitRuntimeInvisibleTypeAnnotationsAttribute(Clazz, Method, CodeAttribute, RuntimeInvisibleTypeAnnotationsAttribute)}.
 *
 * The visitRuntimeInvisibleTypeAnnotationsAttribute method in ChangedCodePrinter is a delegation method
 * for the 4-parameter version that includes Method and CodeAttribute parameters.
 * It simply forwards the call to the wrapped AttributeVisitor without any additional logic,
 * as RuntimeInvisibleTypeAnnotationsAttribute does not contain bytecode that needs change detection.
 *
 * These tests verify that the method:
 * 1. Correctly delegates to the wrapped visitor
 * 2. Passes all parameters in the correct order
 * 3. Works with various inputs including edge cases
 */
public class ChangedCodePrinterClaude_visitRuntimeInvisibleTypeAnnotationsAttributeTest {

    private AttributeVisitor mockAttributeVisitor;
    private ChangedCodePrinter changedCodePrinter;
    private Clazz clazz;
    private Method method;
    private CodeAttribute codeAttribute;

    @BeforeEach
    public void setUp() {
        mockAttributeVisitor = mock(AttributeVisitor.class);
        changedCodePrinter = new ChangedCodePrinter(mockAttributeVisitor);
        clazz = new ProgramClass();
        method = new ProgramMethod();
        codeAttribute = new CodeAttribute();
    }

    /**
     * Tests that visitRuntimeInvisibleTypeAnnotationsAttribute delegates to the wrapped visitor.
     * Verifies that the method calls the visitor with the correct parameters.
     */
    @Test
    public void testVisitRuntimeInvisibleTypeAnnotationsAttribute_delegatesToWrappedVisitor() {
        // Arrange
        RuntimeInvisibleTypeAnnotationsAttribute attribute = new RuntimeInvisibleTypeAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that the method does not throw an exception with valid inputs.
     */
    @Test
    public void testVisitRuntimeInvisibleTypeAnnotationsAttribute_withValidInputs_doesNotThrow() {
        // Arrange
        RuntimeInvisibleTypeAnnotationsAttribute attribute = new RuntimeInvisibleTypeAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() ->
            changedCodePrinter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute),
            "visitRuntimeInvisibleTypeAnnotationsAttribute should not throw any exception");
    }

    /**
     * Tests that the method can be called multiple times without issues.
     */
    @Test
    public void testVisitRuntimeInvisibleTypeAnnotationsAttribute_calledMultipleTimes_doesNotThrow() {
        // Arrange
        RuntimeInvisibleTypeAnnotationsAttribute attribute = new RuntimeInvisibleTypeAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);
            changedCodePrinter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);
            changedCodePrinter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);
        }, "Multiple calls should not throw any exception");

        // Verify the visitor was called multiple times
        verify(mockAttributeVisitor, times(3))
            .visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that the method works with different Clazz instances.
     */
    @Test
    public void testVisitRuntimeInvisibleTypeAnnotationsAttribute_withDifferentClasses_delegatesCorrectly() {
        // Arrange
        Clazz clazz1 = new ProgramClass();
        Clazz clazz2 = new ProgramClass();
        RuntimeInvisibleTypeAnnotationsAttribute attribute = new RuntimeInvisibleTypeAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz1, method, codeAttribute, attribute);
        changedCodePrinter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz2, method, codeAttribute, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeInvisibleTypeAnnotationsAttribute(clazz1, method, codeAttribute, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeInvisibleTypeAnnotationsAttribute(clazz2, method, codeAttribute, attribute);
    }

    /**
     * Tests that the method works with different Method instances.
     */
    @Test
    public void testVisitRuntimeInvisibleTypeAnnotationsAttribute_withDifferentMethods_delegatesCorrectly() {
        // Arrange
        Method method1 = new ProgramMethod();
        Method method2 = new ProgramMethod();
        RuntimeInvisibleTypeAnnotationsAttribute attribute = new RuntimeInvisibleTypeAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method1, codeAttribute, attribute);
        changedCodePrinter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method2, codeAttribute, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method1, codeAttribute, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method2, codeAttribute, attribute);
    }

    /**
     * Tests that the method works with different CodeAttribute instances.
     */
    @Test
    public void testVisitRuntimeInvisibleTypeAnnotationsAttribute_withDifferentCodeAttributes_delegatesCorrectly() {
        // Arrange
        CodeAttribute codeAttribute1 = new CodeAttribute();
        CodeAttribute codeAttribute2 = new CodeAttribute();
        RuntimeInvisibleTypeAnnotationsAttribute attribute = new RuntimeInvisibleTypeAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute1, attribute);
        changedCodePrinter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute2, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute1, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute2, attribute);
    }

    /**
     * Tests that the method works with different RuntimeInvisibleTypeAnnotationsAttribute instances.
     */
    @Test
    public void testVisitRuntimeInvisibleTypeAnnotationsAttribute_withDifferentAttributes_delegatesCorrectly() {
        // Arrange
        RuntimeInvisibleTypeAnnotationsAttribute attribute1 = new RuntimeInvisibleTypeAnnotationsAttribute();
        RuntimeInvisibleTypeAnnotationsAttribute attribute2 = new RuntimeInvisibleTypeAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute1);
        changedCodePrinter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute2);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute2);
    }

    /**
     * Tests that the method passes parameters in the correct order.
     */
    @Test
    public void testVisitRuntimeInvisibleTypeAnnotationsAttribute_passesParametersInCorrectOrder() {
        // Arrange
        RuntimeInvisibleTypeAnnotationsAttribute attribute = new RuntimeInvisibleTypeAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify the parameters are in correct order
        verify(mockAttributeVisitor).visitRuntimeInvisibleTypeAnnotationsAttribute(
            argThat(arg -> arg == clazz),
            argThat(arg -> arg == method),
            argThat(arg -> arg == codeAttribute),
            argThat(arg -> arg == attribute)
        );
    }

    /**
     * Tests that the method can be called in rapid succession.
     */
    @Test
    public void testVisitRuntimeInvisibleTypeAnnotationsAttribute_rapidSuccessiveCalls_doesNotThrow() {
        // Arrange
        RuntimeInvisibleTypeAnnotationsAttribute attribute = new RuntimeInvisibleTypeAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                changedCodePrinter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);
            }
        }, "Rapid successive calls should not throw any exception");

        // Verify the visitor was called 100 times
        verify(mockAttributeVisitor, times(100))
            .visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that the method works when the wrapped visitor does nothing (no-op).
     */
    @Test
    public void testVisitRuntimeInvisibleTypeAnnotationsAttribute_withNoOpVisitor_doesNotThrow() {
        // Arrange
        AttributeVisitor noOpVisitor = mock(AttributeVisitor.class);
        doNothing().when(noOpVisitor).visitRuntimeInvisibleTypeAnnotationsAttribute(any(), any(), any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(noOpVisitor);
        RuntimeInvisibleTypeAnnotationsAttribute attribute = new RuntimeInvisibleTypeAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> printer.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute),
            "Should not throw when visitor is no-op");
    }

    /**
     * Tests that the method works when the wrapped visitor throws an exception.
     */
    @Test
    public void testVisitRuntimeInvisibleTypeAnnotationsAttribute_whenVisitorThrows_propagatesException() {
        // Arrange
        AttributeVisitor throwingVisitor = mock(AttributeVisitor.class);
        RuntimeException expectedException = new RuntimeException("Test exception");
        doThrow(expectedException)
            .when(throwingVisitor)
            .visitRuntimeInvisibleTypeAnnotationsAttribute(any(), any(), any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(throwingVisitor);
        RuntimeInvisibleTypeAnnotationsAttribute attribute = new RuntimeInvisibleTypeAnnotationsAttribute();

        // Act & Assert
        RuntimeException thrownException = assertThrows(RuntimeException.class, () ->
            printer.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute),
            "Should propagate exception from wrapped visitor");

        assertEquals(expectedException, thrownException, "Should throw the same exception");
    }

    /**
     * Tests that multiple ChangedCodePrinter instances work independently.
     */
    @Test
    public void testVisitRuntimeInvisibleTypeAnnotationsAttribute_multipleInstances_workIndependently() {
        // Arrange
        AttributeVisitor visitor1 = mock(AttributeVisitor.class);
        AttributeVisitor visitor2 = mock(AttributeVisitor.class);
        ChangedCodePrinter printer1 = new ChangedCodePrinter(visitor1);
        ChangedCodePrinter printer2 = new ChangedCodePrinter(visitor2);
        RuntimeInvisibleTypeAnnotationsAttribute attribute = new RuntimeInvisibleTypeAnnotationsAttribute();

        // Act
        printer1.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);
        printer2.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);

        // Assert
        verify(visitor1, times(1)).visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);
        verify(visitor2, times(1)).visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);
        verifyNoMoreInteractions(visitor1, visitor2);
    }

    /**
     * Tests that the printer can be reused after calling visitRuntimeInvisibleTypeAnnotationsAttribute.
     */
    @Test
    public void testVisitRuntimeInvisibleTypeAnnotationsAttribute_printerReusable() {
        // Arrange
        RuntimeInvisibleTypeAnnotationsAttribute attribute1 = new RuntimeInvisibleTypeAnnotationsAttribute();
        RuntimeInvisibleTypeAnnotationsAttribute attribute2 = new RuntimeInvisibleTypeAnnotationsAttribute();

        // Act & Assert - reuse the same printer
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute1);
            changedCodePrinter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute2);
            changedCodePrinter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute1);
        }, "Printer should be reusable");

        verify(mockAttributeVisitor, times(2))
            .visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute2);
    }

    /**
     * Tests that the method delegates exactly once per call.
     */
    @Test
    public void testVisitRuntimeInvisibleTypeAnnotationsAttribute_delegatesExactlyOnce() {
        // Arrange
        RuntimeInvisibleTypeAnnotationsAttribute attribute = new RuntimeInvisibleTypeAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);

        // Assert - should delegate exactly once, no more, no less
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeInvisibleTypeAnnotationsAttribute(any(), any(), any(), any());
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method doesn't call any other visitor methods.
     */
    @Test
    public void testVisitRuntimeInvisibleTypeAnnotationsAttribute_doesNotCallOtherVisitorMethods() {
        // Arrange
        RuntimeInvisibleTypeAnnotationsAttribute attribute = new RuntimeInvisibleTypeAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify only visitRuntimeInvisibleTypeAnnotationsAttribute was called
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method returns normally (doesn't hang or loop).
     */
    @Test
    public void testVisitRuntimeInvisibleTypeAnnotationsAttribute_returnsImmediately() {
        // Arrange
        RuntimeInvisibleTypeAnnotationsAttribute attribute = new RuntimeInvisibleTypeAnnotationsAttribute();

        // Act
        long startTime = System.nanoTime();
        changedCodePrinter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);
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
    public void testVisitRuntimeInvisibleTypeAnnotationsAttribute_withFreshPrinter_doesNotThrow() {
        // Arrange
        AttributeVisitor visitor = mock(AttributeVisitor.class);
        ChangedCodePrinter freshPrinter = new ChangedCodePrinter(visitor);
        RuntimeInvisibleTypeAnnotationsAttribute attribute = new RuntimeInvisibleTypeAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> freshPrinter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute),
                "Method should work with a newly created printer");
    }

    /**
     * Tests that the same attribute can be visited multiple times with the same parameters.
     */
    @Test
    public void testVisitRuntimeInvisibleTypeAnnotationsAttribute_sameAttributeMultipleTimes() {
        // Arrange
        RuntimeInvisibleTypeAnnotationsAttribute attribute = new RuntimeInvisibleTypeAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);
        changedCodePrinter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);
        changedCodePrinter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);

        // Assert
        verify(mockAttributeVisitor, times(3))
            .visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the attribute.
     */
    @Test
    public void testVisitRuntimeInvisibleTypeAnnotationsAttribute_doesNotModifyAttribute() {
        // Arrange
        RuntimeInvisibleTypeAnnotationsAttribute attribute = new RuntimeInvisibleTypeAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify the call succeeded and delegated properly
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the clazz.
     */
    @Test
    public void testVisitRuntimeInvisibleTypeAnnotationsAttribute_doesNotModifyClazz() {
        // Arrange
        RuntimeInvisibleTypeAnnotationsAttribute attribute = new RuntimeInvisibleTypeAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the method parameter.
     */
    @Test
    public void testVisitRuntimeInvisibleTypeAnnotationsAttribute_doesNotModifyMethod() {
        // Arrange
        RuntimeInvisibleTypeAnnotationsAttribute attribute = new RuntimeInvisibleTypeAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the codeAttribute.
     */
    @Test
    public void testVisitRuntimeInvisibleTypeAnnotationsAttribute_doesNotModifyCodeAttribute() {
        // Arrange
        RuntimeInvisibleTypeAnnotationsAttribute attribute = new RuntimeInvisibleTypeAnnotationsAttribute();

        // Act
        changedCodePrinter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that the method works correctly when alternating with other visitor methods.
     */
    @Test
    public void testVisitRuntimeInvisibleTypeAnnotationsAttribute_alternatingWithOtherMethods_doesNotInterfere() {
        // Arrange
        RuntimeInvisibleTypeAnnotationsAttribute attribute1 = new RuntimeInvisibleTypeAnnotationsAttribute();
        RuntimeInvisibleTypeAnnotationsAttribute attribute2 = new RuntimeInvisibleTypeAnnotationsAttribute();

        // Act - alternate calls
        changedCodePrinter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute1);
        changedCodePrinter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute2);
        changedCodePrinter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute1);

        // Assert
        verify(mockAttributeVisitor, times(2))
            .visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, codeAttribute, attribute2);
    }
}
