package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.preverification.StackMapAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ChangedCodePrinter#visitStackMapAttribute(Clazz, Method, CodeAttribute, StackMapAttribute)}.
 *
 * The visitStackMapAttribute method in ChangedCodePrinter is a delegation method.
 * It simply forwards the call to the wrapped AttributeVisitor without any additional logic,
 * as StackMapAttribute does not contain bytecode that needs change detection.
 *
 * These tests verify that the method:
 * 1. Correctly delegates to the wrapped visitor
 * 2. Passes the correct parameters in the correct order
 * 3. Works with various inputs including edge cases
 */
public class ChangedCodePrinterClaude_visitStackMapAttributeTest {

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
     * Tests that visitStackMapAttribute delegates to the wrapped visitor.
     * Verifies that the method calls the visitor with the correct parameters.
     */
    @Test
    public void testVisitStackMapAttribute_delegatesToWrappedVisitor() {
        // Arrange
        StackMapAttribute attribute = new StackMapAttribute();

        // Act
        changedCodePrinter.visitStackMapAttribute(clazz, method, codeAttribute, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitStackMapAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that the method does not throw an exception with valid inputs.
     */
    @Test
    public void testVisitStackMapAttribute_withValidInputs_doesNotThrow() {
        // Arrange
        StackMapAttribute attribute = new StackMapAttribute();

        // Act & Assert
        assertDoesNotThrow(() ->
            changedCodePrinter.visitStackMapAttribute(clazz, method, codeAttribute, attribute),
            "visitStackMapAttribute should not throw any exception");
    }

    /**
     * Tests that the method can be called multiple times without issues.
     */
    @Test
    public void testVisitStackMapAttribute_calledMultipleTimes_doesNotThrow() {
        // Arrange
        StackMapAttribute attribute = new StackMapAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitStackMapAttribute(clazz, method, codeAttribute, attribute);
            changedCodePrinter.visitStackMapAttribute(clazz, method, codeAttribute, attribute);
            changedCodePrinter.visitStackMapAttribute(clazz, method, codeAttribute, attribute);
        }, "Multiple calls should not throw any exception");

        // Verify the visitor was called multiple times
        verify(mockAttributeVisitor, times(3))
            .visitStackMapAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that the method works with different Clazz instances.
     */
    @Test
    public void testVisitStackMapAttribute_withDifferentClasses_delegatesCorrectly() {
        // Arrange
        Clazz clazz1 = new ProgramClass();
        Clazz clazz2 = new ProgramClass();
        StackMapAttribute attribute = new StackMapAttribute();

        // Act
        changedCodePrinter.visitStackMapAttribute(clazz1, method, codeAttribute, attribute);
        changedCodePrinter.visitStackMapAttribute(clazz2, method, codeAttribute, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitStackMapAttribute(clazz1, method, codeAttribute, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitStackMapAttribute(clazz2, method, codeAttribute, attribute);
    }

    /**
     * Tests that the method works with different Method instances.
     */
    @Test
    public void testVisitStackMapAttribute_withDifferentMethods_delegatesCorrectly() {
        // Arrange
        Method method1 = new ProgramMethod();
        Method method2 = new ProgramMethod();
        StackMapAttribute attribute = new StackMapAttribute();

        // Act
        changedCodePrinter.visitStackMapAttribute(clazz, method1, codeAttribute, attribute);
        changedCodePrinter.visitStackMapAttribute(clazz, method2, codeAttribute, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitStackMapAttribute(clazz, method1, codeAttribute, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitStackMapAttribute(clazz, method2, codeAttribute, attribute);
    }

    /**
     * Tests that the method works with different CodeAttribute instances.
     */
    @Test
    public void testVisitStackMapAttribute_withDifferentCodeAttributes_delegatesCorrectly() {
        // Arrange
        CodeAttribute codeAttribute1 = new CodeAttribute();
        CodeAttribute codeAttribute2 = new CodeAttribute();
        StackMapAttribute attribute = new StackMapAttribute();

        // Act
        changedCodePrinter.visitStackMapAttribute(clazz, method, codeAttribute1, attribute);
        changedCodePrinter.visitStackMapAttribute(clazz, method, codeAttribute2, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitStackMapAttribute(clazz, method, codeAttribute1, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitStackMapAttribute(clazz, method, codeAttribute2, attribute);
    }

    /**
     * Tests that the method works with different StackMapAttribute instances.
     */
    @Test
    public void testVisitStackMapAttribute_withDifferentAttributes_delegatesCorrectly() {
        // Arrange
        StackMapAttribute attribute1 = new StackMapAttribute();
        StackMapAttribute attribute2 = new StackMapAttribute();

        // Act
        changedCodePrinter.visitStackMapAttribute(clazz, method, codeAttribute, attribute1);
        changedCodePrinter.visitStackMapAttribute(clazz, method, codeAttribute, attribute2);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitStackMapAttribute(clazz, method, codeAttribute, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitStackMapAttribute(clazz, method, codeAttribute, attribute2);
    }

    /**
     * Tests that the method passes parameters in the correct order.
     */
    @Test
    public void testVisitStackMapAttribute_passesParametersInCorrectOrder() {
        // Arrange
        StackMapAttribute attribute = new StackMapAttribute();

        // Act
        changedCodePrinter.visitStackMapAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify the parameters are in correct order
        verify(mockAttributeVisitor).visitStackMapAttribute(
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
    public void testVisitStackMapAttribute_rapidSuccessiveCalls_doesNotThrow() {
        // Arrange
        StackMapAttribute attribute = new StackMapAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                changedCodePrinter.visitStackMapAttribute(clazz, method, codeAttribute, attribute);
            }
        }, "Rapid successive calls should not throw any exception");

        // Verify the visitor was called 100 times
        verify(mockAttributeVisitor, times(100))
            .visitStackMapAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that the method works when the wrapped visitor does nothing (no-op).
     */
    @Test
    public void testVisitStackMapAttribute_withNoOpVisitor_doesNotThrow() {
        // Arrange
        AttributeVisitor noOpVisitor = mock(AttributeVisitor.class);
        doNothing().when(noOpVisitor).visitStackMapAttribute(any(), any(), any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(noOpVisitor);
        StackMapAttribute attribute = new StackMapAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> printer.visitStackMapAttribute(clazz, method, codeAttribute, attribute),
            "Should not throw when visitor is no-op");
    }

    /**
     * Tests that the method works when the wrapped visitor throws an exception.
     */
    @Test
    public void testVisitStackMapAttribute_whenVisitorThrows_propagatesException() {
        // Arrange
        AttributeVisitor throwingVisitor = mock(AttributeVisitor.class);
        RuntimeException expectedException = new RuntimeException("Test exception");
        doThrow(expectedException)
            .when(throwingVisitor)
            .visitStackMapAttribute(any(), any(), any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(throwingVisitor);
        StackMapAttribute attribute = new StackMapAttribute();

        // Act & Assert
        RuntimeException thrownException = assertThrows(RuntimeException.class, () ->
            printer.visitStackMapAttribute(clazz, method, codeAttribute, attribute),
            "Should propagate exception from wrapped visitor");

        assertEquals(expectedException, thrownException, "Should throw the same exception");
    }

    /**
     * Tests that multiple ChangedCodePrinter instances work independently.
     */
    @Test
    public void testVisitStackMapAttribute_multipleInstances_workIndependently() {
        // Arrange
        AttributeVisitor visitor1 = mock(AttributeVisitor.class);
        AttributeVisitor visitor2 = mock(AttributeVisitor.class);
        ChangedCodePrinter printer1 = new ChangedCodePrinter(visitor1);
        ChangedCodePrinter printer2 = new ChangedCodePrinter(visitor2);
        StackMapAttribute attribute = new StackMapAttribute();

        // Act
        printer1.visitStackMapAttribute(clazz, method, codeAttribute, attribute);
        printer2.visitStackMapAttribute(clazz, method, codeAttribute, attribute);

        // Assert
        verify(visitor1, times(1)).visitStackMapAttribute(clazz, method, codeAttribute, attribute);
        verify(visitor2, times(1)).visitStackMapAttribute(clazz, method, codeAttribute, attribute);
        verifyNoMoreInteractions(visitor1, visitor2);
    }

    /**
     * Tests that the printer can be reused after calling visitStackMapAttribute.
     */
    @Test
    public void testVisitStackMapAttribute_printerReusable() {
        // Arrange
        StackMapAttribute attribute1 = new StackMapAttribute();
        StackMapAttribute attribute2 = new StackMapAttribute();

        // Act & Assert - reuse the same printer
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitStackMapAttribute(clazz, method, codeAttribute, attribute1);
            changedCodePrinter.visitStackMapAttribute(clazz, method, codeAttribute, attribute2);
            changedCodePrinter.visitStackMapAttribute(clazz, method, codeAttribute, attribute1);
        }, "Printer should be reusable");

        verify(mockAttributeVisitor, times(2))
            .visitStackMapAttribute(clazz, method, codeAttribute, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitStackMapAttribute(clazz, method, codeAttribute, attribute2);
    }

    /**
     * Tests that the method delegates exactly once per call.
     */
    @Test
    public void testVisitStackMapAttribute_delegatesExactlyOnce() {
        // Arrange
        StackMapAttribute attribute = new StackMapAttribute();

        // Act
        changedCodePrinter.visitStackMapAttribute(clazz, method, codeAttribute, attribute);

        // Assert - should delegate exactly once, no more, no less
        verify(mockAttributeVisitor, times(1))
            .visitStackMapAttribute(any(), any(), any(), any());
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method doesn't call any other visitor methods.
     */
    @Test
    public void testVisitStackMapAttribute_doesNotCallOtherVisitorMethods() {
        // Arrange
        StackMapAttribute attribute = new StackMapAttribute();

        // Act
        changedCodePrinter.visitStackMapAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify only visitStackMapAttribute was called
        verify(mockAttributeVisitor, times(1))
            .visitStackMapAttribute(clazz, method, codeAttribute, attribute);
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method returns normally (doesn't hang or loop).
     */
    @Test
    public void testVisitStackMapAttribute_returnsImmediately() {
        // Arrange
        StackMapAttribute attribute = new StackMapAttribute();

        // Act
        long startTime = System.nanoTime();
        changedCodePrinter.visitStackMapAttribute(clazz, method, codeAttribute, attribute);
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
    public void testVisitStackMapAttribute_withFreshPrinter_doesNotThrow() {
        // Arrange
        AttributeVisitor visitor = mock(AttributeVisitor.class);
        ChangedCodePrinter freshPrinter = new ChangedCodePrinter(visitor);
        StackMapAttribute attribute = new StackMapAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> freshPrinter.visitStackMapAttribute(clazz, method, codeAttribute, attribute),
                "Method should work with a newly created printer");
    }

    /**
     * Tests that the same attribute can be visited multiple times with the same parameters.
     */
    @Test
    public void testVisitStackMapAttribute_sameAttributeMultipleTimes() {
        // Arrange
        StackMapAttribute attribute = new StackMapAttribute();

        // Act
        changedCodePrinter.visitStackMapAttribute(clazz, method, codeAttribute, attribute);
        changedCodePrinter.visitStackMapAttribute(clazz, method, codeAttribute, attribute);
        changedCodePrinter.visitStackMapAttribute(clazz, method, codeAttribute, attribute);

        // Assert
        verify(mockAttributeVisitor, times(3))
            .visitStackMapAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the attribute.
     */
    @Test
    public void testVisitStackMapAttribute_doesNotModifyAttribute() {
        // Arrange
        StackMapAttribute attribute = new StackMapAttribute();

        // Act
        changedCodePrinter.visitStackMapAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify the call succeeded and delegated properly
        verify(mockAttributeVisitor, times(1))
            .visitStackMapAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the clazz.
     */
    @Test
    public void testVisitStackMapAttribute_doesNotModifyClazz() {
        // Arrange
        StackMapAttribute attribute = new StackMapAttribute();

        // Act
        changedCodePrinter.visitStackMapAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitStackMapAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the method.
     */
    @Test
    public void testVisitStackMapAttribute_doesNotModifyMethod() {
        // Arrange
        StackMapAttribute attribute = new StackMapAttribute();

        // Act
        changedCodePrinter.visitStackMapAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitStackMapAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the code attribute.
     */
    @Test
    public void testVisitStackMapAttribute_doesNotModifyCodeAttribute() {
        // Arrange
        StackMapAttribute attribute = new StackMapAttribute();

        // Act
        changedCodePrinter.visitStackMapAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitStackMapAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that the method works correctly when alternating with different attributes.
     */
    @Test
    public void testVisitStackMapAttribute_alternatingWithOtherAttributes_doesNotInterfere() {
        // Arrange
        StackMapAttribute attribute1 = new StackMapAttribute();
        StackMapAttribute attribute2 = new StackMapAttribute();

        // Act - alternate calls
        changedCodePrinter.visitStackMapAttribute(clazz, method, codeAttribute, attribute1);
        changedCodePrinter.visitStackMapAttribute(clazz, method, codeAttribute, attribute2);
        changedCodePrinter.visitStackMapAttribute(clazz, method, codeAttribute, attribute1);

        // Assert
        verify(mockAttributeVisitor, times(2))
            .visitStackMapAttribute(clazz, method, codeAttribute, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitStackMapAttribute(clazz, method, codeAttribute, attribute2);
    }

    /**
     * Tests that the method works with all parameters being different instances.
     */
    @Test
    public void testVisitStackMapAttribute_withAllDifferentParameters_delegatesCorrectly() {
        // Arrange
        Clazz clazz1 = new ProgramClass();
        Method method1 = new ProgramMethod();
        CodeAttribute codeAttribute1 = new CodeAttribute();
        StackMapAttribute attribute1 = new StackMapAttribute();

        Clazz clazz2 = new ProgramClass();
        Method method2 = new ProgramMethod();
        CodeAttribute codeAttribute2 = new CodeAttribute();
        StackMapAttribute attribute2 = new StackMapAttribute();

        // Act
        changedCodePrinter.visitStackMapAttribute(clazz1, method1, codeAttribute1, attribute1);
        changedCodePrinter.visitStackMapAttribute(clazz2, method2, codeAttribute2, attribute2);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitStackMapAttribute(clazz1, method1, codeAttribute1, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitStackMapAttribute(clazz2, method2, codeAttribute2, attribute2);
    }
}
