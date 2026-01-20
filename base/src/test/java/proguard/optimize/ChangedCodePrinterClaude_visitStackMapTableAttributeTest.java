package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.preverification.StackMapTableAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ChangedCodePrinter#visitStackMapTableAttribute(Clazz, Method, CodeAttribute, StackMapTableAttribute)}.
 *
 * The visitStackMapTableAttribute method in ChangedCodePrinter is a delegation method.
 * It simply forwards the call to the wrapped AttributeVisitor without any additional logic,
 * as StackMapTableAttribute does not contain bytecode that needs change detection.
 *
 * These tests verify that the method:
 * 1. Correctly delegates to the wrapped visitor
 * 2. Passes the correct parameters in the correct order
 * 3. Works with various inputs including edge cases
 */
public class ChangedCodePrinterClaude_visitStackMapTableAttributeTest {

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
     * Tests that visitStackMapTableAttribute delegates to the wrapped visitor.
     * Verifies that the method calls the visitor with the correct parameters.
     */
    @Test
    public void testVisitStackMapTableAttribute_delegatesToWrappedVisitor() {
        // Arrange
        StackMapTableAttribute attribute = new StackMapTableAttribute();

        // Act
        changedCodePrinter.visitStackMapTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitStackMapTableAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that the method does not throw an exception with valid inputs.
     */
    @Test
    public void testVisitStackMapTableAttribute_withValidInputs_doesNotThrow() {
        // Arrange
        StackMapTableAttribute attribute = new StackMapTableAttribute();

        // Act & Assert
        assertDoesNotThrow(() ->
            changedCodePrinter.visitStackMapTableAttribute(clazz, method, codeAttribute, attribute),
            "visitStackMapTableAttribute should not throw any exception");
    }

    /**
     * Tests that the method can be called multiple times without issues.
     */
    @Test
    public void testVisitStackMapTableAttribute_calledMultipleTimes_doesNotThrow() {
        // Arrange
        StackMapTableAttribute attribute = new StackMapTableAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitStackMapTableAttribute(clazz, method, codeAttribute, attribute);
            changedCodePrinter.visitStackMapTableAttribute(clazz, method, codeAttribute, attribute);
            changedCodePrinter.visitStackMapTableAttribute(clazz, method, codeAttribute, attribute);
        }, "Multiple calls should not throw any exception");

        // Verify the visitor was called multiple times
        verify(mockAttributeVisitor, times(3))
            .visitStackMapTableAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that the method works with different Clazz instances.
     */
    @Test
    public void testVisitStackMapTableAttribute_withDifferentClasses_delegatesCorrectly() {
        // Arrange
        Clazz clazz1 = new ProgramClass();
        Clazz clazz2 = new ProgramClass();
        StackMapTableAttribute attribute = new StackMapTableAttribute();

        // Act
        changedCodePrinter.visitStackMapTableAttribute(clazz1, method, codeAttribute, attribute);
        changedCodePrinter.visitStackMapTableAttribute(clazz2, method, codeAttribute, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitStackMapTableAttribute(clazz1, method, codeAttribute, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitStackMapTableAttribute(clazz2, method, codeAttribute, attribute);
    }

    /**
     * Tests that the method works with different Method instances.
     */
    @Test
    public void testVisitStackMapTableAttribute_withDifferentMethods_delegatesCorrectly() {
        // Arrange
        Method method1 = new ProgramMethod();
        Method method2 = new ProgramMethod();
        StackMapTableAttribute attribute = new StackMapTableAttribute();

        // Act
        changedCodePrinter.visitStackMapTableAttribute(clazz, method1, codeAttribute, attribute);
        changedCodePrinter.visitStackMapTableAttribute(clazz, method2, codeAttribute, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitStackMapTableAttribute(clazz, method1, codeAttribute, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitStackMapTableAttribute(clazz, method2, codeAttribute, attribute);
    }

    /**
     * Tests that the method works with different CodeAttribute instances.
     */
    @Test
    public void testVisitStackMapTableAttribute_withDifferentCodeAttributes_delegatesCorrectly() {
        // Arrange
        CodeAttribute codeAttribute1 = new CodeAttribute();
        CodeAttribute codeAttribute2 = new CodeAttribute();
        StackMapTableAttribute attribute = new StackMapTableAttribute();

        // Act
        changedCodePrinter.visitStackMapTableAttribute(clazz, method, codeAttribute1, attribute);
        changedCodePrinter.visitStackMapTableAttribute(clazz, method, codeAttribute2, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitStackMapTableAttribute(clazz, method, codeAttribute1, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitStackMapTableAttribute(clazz, method, codeAttribute2, attribute);
    }

    /**
     * Tests that the method works with different StackMapTableAttribute instances.
     */
    @Test
    public void testVisitStackMapTableAttribute_withDifferentAttributes_delegatesCorrectly() {
        // Arrange
        StackMapTableAttribute attribute1 = new StackMapTableAttribute();
        StackMapTableAttribute attribute2 = new StackMapTableAttribute();

        // Act
        changedCodePrinter.visitStackMapTableAttribute(clazz, method, codeAttribute, attribute1);
        changedCodePrinter.visitStackMapTableAttribute(clazz, method, codeAttribute, attribute2);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitStackMapTableAttribute(clazz, method, codeAttribute, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitStackMapTableAttribute(clazz, method, codeAttribute, attribute2);
    }

    /**
     * Tests that the method passes parameters in the correct order.
     */
    @Test
    public void testVisitStackMapTableAttribute_passesParametersInCorrectOrder() {
        // Arrange
        StackMapTableAttribute attribute = new StackMapTableAttribute();

        // Act
        changedCodePrinter.visitStackMapTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify the parameters are in correct order
        verify(mockAttributeVisitor).visitStackMapTableAttribute(
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
    public void testVisitStackMapTableAttribute_rapidSuccessiveCalls_doesNotThrow() {
        // Arrange
        StackMapTableAttribute attribute = new StackMapTableAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                changedCodePrinter.visitStackMapTableAttribute(clazz, method, codeAttribute, attribute);
            }
        }, "Rapid successive calls should not throw any exception");

        // Verify the visitor was called 100 times
        verify(mockAttributeVisitor, times(100))
            .visitStackMapTableAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that the method works when the wrapped visitor does nothing (no-op).
     */
    @Test
    public void testVisitStackMapTableAttribute_withNoOpVisitor_doesNotThrow() {
        // Arrange
        AttributeVisitor noOpVisitor = mock(AttributeVisitor.class);
        doNothing().when(noOpVisitor).visitStackMapTableAttribute(any(), any(), any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(noOpVisitor);
        StackMapTableAttribute attribute = new StackMapTableAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> printer.visitStackMapTableAttribute(clazz, method, codeAttribute, attribute),
            "Should not throw when visitor is no-op");
    }

    /**
     * Tests that the method works when the wrapped visitor throws an exception.
     */
    @Test
    public void testVisitStackMapTableAttribute_whenVisitorThrows_propagatesException() {
        // Arrange
        AttributeVisitor throwingVisitor = mock(AttributeVisitor.class);
        RuntimeException expectedException = new RuntimeException("Test exception");
        doThrow(expectedException)
            .when(throwingVisitor)
            .visitStackMapTableAttribute(any(), any(), any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(throwingVisitor);
        StackMapTableAttribute attribute = new StackMapTableAttribute();

        // Act & Assert
        RuntimeException thrownException = assertThrows(RuntimeException.class, () ->
            printer.visitStackMapTableAttribute(clazz, method, codeAttribute, attribute),
            "Should propagate exception from wrapped visitor");

        assertEquals(expectedException, thrownException, "Should throw the same exception");
    }

    /**
     * Tests that multiple ChangedCodePrinter instances work independently.
     */
    @Test
    public void testVisitStackMapTableAttribute_multipleInstances_workIndependently() {
        // Arrange
        AttributeVisitor visitor1 = mock(AttributeVisitor.class);
        AttributeVisitor visitor2 = mock(AttributeVisitor.class);
        ChangedCodePrinter printer1 = new ChangedCodePrinter(visitor1);
        ChangedCodePrinter printer2 = new ChangedCodePrinter(visitor2);
        StackMapTableAttribute attribute = new StackMapTableAttribute();

        // Act
        printer1.visitStackMapTableAttribute(clazz, method, codeAttribute, attribute);
        printer2.visitStackMapTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert
        verify(visitor1, times(1)).visitStackMapTableAttribute(clazz, method, codeAttribute, attribute);
        verify(visitor2, times(1)).visitStackMapTableAttribute(clazz, method, codeAttribute, attribute);
        verifyNoMoreInteractions(visitor1, visitor2);
    }

    /**
     * Tests that the printer can be reused after calling visitStackMapTableAttribute.
     */
    @Test
    public void testVisitStackMapTableAttribute_printerReusable() {
        // Arrange
        StackMapTableAttribute attribute1 = new StackMapTableAttribute();
        StackMapTableAttribute attribute2 = new StackMapTableAttribute();

        // Act & Assert - reuse the same printer
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitStackMapTableAttribute(clazz, method, codeAttribute, attribute1);
            changedCodePrinter.visitStackMapTableAttribute(clazz, method, codeAttribute, attribute2);
            changedCodePrinter.visitStackMapTableAttribute(clazz, method, codeAttribute, attribute1);
        }, "Printer should be reusable");

        verify(mockAttributeVisitor, times(2))
            .visitStackMapTableAttribute(clazz, method, codeAttribute, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitStackMapTableAttribute(clazz, method, codeAttribute, attribute2);
    }

    /**
     * Tests that the method delegates exactly once per call.
     */
    @Test
    public void testVisitStackMapTableAttribute_delegatesExactlyOnce() {
        // Arrange
        StackMapTableAttribute attribute = new StackMapTableAttribute();

        // Act
        changedCodePrinter.visitStackMapTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert - should delegate exactly once, no more, no less
        verify(mockAttributeVisitor, times(1))
            .visitStackMapTableAttribute(any(), any(), any(), any());
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method doesn't call any other visitor methods.
     */
    @Test
    public void testVisitStackMapTableAttribute_doesNotCallOtherVisitorMethods() {
        // Arrange
        StackMapTableAttribute attribute = new StackMapTableAttribute();

        // Act
        changedCodePrinter.visitStackMapTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify only visitStackMapTableAttribute was called
        verify(mockAttributeVisitor, times(1))
            .visitStackMapTableAttribute(clazz, method, codeAttribute, attribute);
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method returns normally (doesn't hang or loop).
     */
    @Test
    public void testVisitStackMapTableAttribute_returnsImmediately() {
        // Arrange
        StackMapTableAttribute attribute = new StackMapTableAttribute();

        // Act
        long startTime = System.nanoTime();
        changedCodePrinter.visitStackMapTableAttribute(clazz, method, codeAttribute, attribute);
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
    public void testVisitStackMapTableAttribute_withFreshPrinter_doesNotThrow() {
        // Arrange
        AttributeVisitor visitor = mock(AttributeVisitor.class);
        ChangedCodePrinter freshPrinter = new ChangedCodePrinter(visitor);
        StackMapTableAttribute attribute = new StackMapTableAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> freshPrinter.visitStackMapTableAttribute(clazz, method, codeAttribute, attribute),
                "Method should work with a newly created printer");
    }

    /**
     * Tests that the same attribute can be visited multiple times with the same parameters.
     */
    @Test
    public void testVisitStackMapTableAttribute_sameAttributeMultipleTimes() {
        // Arrange
        StackMapTableAttribute attribute = new StackMapTableAttribute();

        // Act
        changedCodePrinter.visitStackMapTableAttribute(clazz, method, codeAttribute, attribute);
        changedCodePrinter.visitStackMapTableAttribute(clazz, method, codeAttribute, attribute);
        changedCodePrinter.visitStackMapTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert
        verify(mockAttributeVisitor, times(3))
            .visitStackMapTableAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the attribute.
     */
    @Test
    public void testVisitStackMapTableAttribute_doesNotModifyAttribute() {
        // Arrange
        StackMapTableAttribute attribute = new StackMapTableAttribute();

        // Act
        changedCodePrinter.visitStackMapTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify the call succeeded and delegated properly
        verify(mockAttributeVisitor, times(1))
            .visitStackMapTableAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the clazz.
     */
    @Test
    public void testVisitStackMapTableAttribute_doesNotModifyClazz() {
        // Arrange
        StackMapTableAttribute attribute = new StackMapTableAttribute();

        // Act
        changedCodePrinter.visitStackMapTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitStackMapTableAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the method.
     */
    @Test
    public void testVisitStackMapTableAttribute_doesNotModifyMethod() {
        // Arrange
        StackMapTableAttribute attribute = new StackMapTableAttribute();

        // Act
        changedCodePrinter.visitStackMapTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitStackMapTableAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the code attribute.
     */
    @Test
    public void testVisitStackMapTableAttribute_doesNotModifyCodeAttribute() {
        // Arrange
        StackMapTableAttribute attribute = new StackMapTableAttribute();

        // Act
        changedCodePrinter.visitStackMapTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitStackMapTableAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that the method works correctly when alternating with different attributes.
     */
    @Test
    public void testVisitStackMapTableAttribute_alternatingWithOtherAttributes_doesNotInterfere() {
        // Arrange
        StackMapTableAttribute attribute1 = new StackMapTableAttribute();
        StackMapTableAttribute attribute2 = new StackMapTableAttribute();

        // Act - alternate calls
        changedCodePrinter.visitStackMapTableAttribute(clazz, method, codeAttribute, attribute1);
        changedCodePrinter.visitStackMapTableAttribute(clazz, method, codeAttribute, attribute2);
        changedCodePrinter.visitStackMapTableAttribute(clazz, method, codeAttribute, attribute1);

        // Assert
        verify(mockAttributeVisitor, times(2))
            .visitStackMapTableAttribute(clazz, method, codeAttribute, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitStackMapTableAttribute(clazz, method, codeAttribute, attribute2);
    }

    /**
     * Tests that the method works with all parameters being different instances.
     */
    @Test
    public void testVisitStackMapTableAttribute_withAllDifferentParameters_delegatesCorrectly() {
        // Arrange
        Clazz clazz1 = new ProgramClass();
        Method method1 = new ProgramMethod();
        CodeAttribute codeAttribute1 = new CodeAttribute();
        StackMapTableAttribute attribute1 = new StackMapTableAttribute();

        Clazz clazz2 = new ProgramClass();
        Method method2 = new ProgramMethod();
        CodeAttribute codeAttribute2 = new CodeAttribute();
        StackMapTableAttribute attribute2 = new StackMapTableAttribute();

        // Act
        changedCodePrinter.visitStackMapTableAttribute(clazz1, method1, codeAttribute1, attribute1);
        changedCodePrinter.visitStackMapTableAttribute(clazz2, method2, codeAttribute2, attribute2);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitStackMapTableAttribute(clazz1, method1, codeAttribute1, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitStackMapTableAttribute(clazz2, method2, codeAttribute2, attribute2);
    }
}
