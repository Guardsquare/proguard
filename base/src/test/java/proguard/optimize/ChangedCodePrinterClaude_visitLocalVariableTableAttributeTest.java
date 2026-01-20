package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.LocalVariableTableAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ChangedCodePrinter#visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute)}.
 *
 * The visitLocalVariableTableAttribute method in ChangedCodePrinter is a delegation method.
 * It simply forwards the call to the wrapped AttributeVisitor without any additional logic,
 * as LocalVariableTableAttribute does not contain bytecode that needs change detection.
 *
 * These tests verify that the method:
 * 1. Correctly delegates to the wrapped visitor
 * 2. Passes the correct parameters in the correct order
 * 3. Works with various inputs including edge cases
 */
public class ChangedCodePrinterClaude_visitLocalVariableTableAttributeTest {

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
     * Tests that visitLocalVariableTableAttribute delegates to the wrapped visitor.
     * Verifies that the method calls the visitor with the correct parameters.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_delegatesToWrappedVisitor() {
        // Arrange
        LocalVariableTableAttribute attribute = new LocalVariableTableAttribute();

        // Act
        changedCodePrinter.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that the method does not throw an exception with valid inputs.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_withValidInputs_doesNotThrow() {
        // Arrange
        LocalVariableTableAttribute attribute = new LocalVariableTableAttribute();

        // Act & Assert
        assertDoesNotThrow(() ->
            changedCodePrinter.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute),
            "visitLocalVariableTableAttribute should not throw any exception");
    }

    /**
     * Tests that the method can be called multiple times without issues.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_calledMultipleTimes_doesNotThrow() {
        // Arrange
        LocalVariableTableAttribute attribute = new LocalVariableTableAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);
            changedCodePrinter.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);
            changedCodePrinter.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);
        }, "Multiple calls should not throw any exception");

        // Verify the visitor was called multiple times
        verify(mockAttributeVisitor, times(3))
            .visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that the method works with different Clazz instances.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_withDifferentClasses_delegatesCorrectly() {
        // Arrange
        Clazz clazz1 = new ProgramClass();
        Clazz clazz2 = new ProgramClass();
        LocalVariableTableAttribute attribute = new LocalVariableTableAttribute();

        // Act
        changedCodePrinter.visitLocalVariableTableAttribute(clazz1, method, codeAttribute, attribute);
        changedCodePrinter.visitLocalVariableTableAttribute(clazz2, method, codeAttribute, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitLocalVariableTableAttribute(clazz1, method, codeAttribute, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitLocalVariableTableAttribute(clazz2, method, codeAttribute, attribute);
    }

    /**
     * Tests that the method works with different Method instances.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_withDifferentMethods_delegatesCorrectly() {
        // Arrange
        Method method1 = new ProgramMethod();
        Method method2 = new ProgramMethod();
        LocalVariableTableAttribute attribute = new LocalVariableTableAttribute();

        // Act
        changedCodePrinter.visitLocalVariableTableAttribute(clazz, method1, codeAttribute, attribute);
        changedCodePrinter.visitLocalVariableTableAttribute(clazz, method2, codeAttribute, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitLocalVariableTableAttribute(clazz, method1, codeAttribute, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitLocalVariableTableAttribute(clazz, method2, codeAttribute, attribute);
    }

    /**
     * Tests that the method works with different CodeAttribute instances.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_withDifferentCodeAttributes_delegatesCorrectly() {
        // Arrange
        CodeAttribute codeAttribute1 = new CodeAttribute();
        CodeAttribute codeAttribute2 = new CodeAttribute();
        LocalVariableTableAttribute attribute = new LocalVariableTableAttribute();

        // Act
        changedCodePrinter.visitLocalVariableTableAttribute(clazz, method, codeAttribute1, attribute);
        changedCodePrinter.visitLocalVariableTableAttribute(clazz, method, codeAttribute2, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitLocalVariableTableAttribute(clazz, method, codeAttribute1, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitLocalVariableTableAttribute(clazz, method, codeAttribute2, attribute);
    }

    /**
     * Tests that the method works with different LocalVariableTableAttribute instances.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_withDifferentAttributes_delegatesCorrectly() {
        // Arrange
        LocalVariableTableAttribute attribute1 = new LocalVariableTableAttribute();
        LocalVariableTableAttribute attribute2 = new LocalVariableTableAttribute();

        // Act
        changedCodePrinter.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute1);
        changedCodePrinter.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute2);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute2);
    }

    /**
     * Tests that the method passes parameters in the correct order.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_passesParametersInCorrectOrder() {
        // Arrange
        LocalVariableTableAttribute attribute = new LocalVariableTableAttribute();

        // Act
        changedCodePrinter.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify the parameters are in correct order
        verify(mockAttributeVisitor).visitLocalVariableTableAttribute(
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
    public void testVisitLocalVariableTableAttribute_rapidSuccessiveCalls_doesNotThrow() {
        // Arrange
        LocalVariableTableAttribute attribute = new LocalVariableTableAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                changedCodePrinter.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);
            }
        }, "Rapid successive calls should not throw any exception");

        // Verify the visitor was called 100 times
        verify(mockAttributeVisitor, times(100))
            .visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that the method works when the wrapped visitor does nothing (no-op).
     */
    @Test
    public void testVisitLocalVariableTableAttribute_withNoOpVisitor_doesNotThrow() {
        // Arrange
        AttributeVisitor noOpVisitor = mock(AttributeVisitor.class);
        doNothing().when(noOpVisitor).visitLocalVariableTableAttribute(any(), any(), any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(noOpVisitor);
        LocalVariableTableAttribute attribute = new LocalVariableTableAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> printer.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute),
            "Should not throw when visitor is no-op");
    }

    /**
     * Tests that the method works when the wrapped visitor throws an exception.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_whenVisitorThrows_propagatesException() {
        // Arrange
        AttributeVisitor throwingVisitor = mock(AttributeVisitor.class);
        RuntimeException expectedException = new RuntimeException("Test exception");
        doThrow(expectedException)
            .when(throwingVisitor)
            .visitLocalVariableTableAttribute(any(), any(), any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(throwingVisitor);
        LocalVariableTableAttribute attribute = new LocalVariableTableAttribute();

        // Act & Assert
        RuntimeException thrownException = assertThrows(RuntimeException.class, () ->
            printer.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute),
            "Should propagate exception from wrapped visitor");

        assertEquals(expectedException, thrownException, "Should throw the same exception");
    }

    /**
     * Tests that multiple ChangedCodePrinter instances work independently.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_multipleInstances_workIndependently() {
        // Arrange
        AttributeVisitor visitor1 = mock(AttributeVisitor.class);
        AttributeVisitor visitor2 = mock(AttributeVisitor.class);
        ChangedCodePrinter printer1 = new ChangedCodePrinter(visitor1);
        ChangedCodePrinter printer2 = new ChangedCodePrinter(visitor2);
        LocalVariableTableAttribute attribute = new LocalVariableTableAttribute();

        // Act
        printer1.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);
        printer2.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert
        verify(visitor1, times(1)).visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);
        verify(visitor2, times(1)).visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);
        verifyNoMoreInteractions(visitor1, visitor2);
    }

    /**
     * Tests that the printer can be reused after calling visitLocalVariableTableAttribute.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_printerReusable() {
        // Arrange
        LocalVariableTableAttribute attribute1 = new LocalVariableTableAttribute();
        LocalVariableTableAttribute attribute2 = new LocalVariableTableAttribute();

        // Act & Assert - reuse the same printer
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute1);
            changedCodePrinter.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute2);
            changedCodePrinter.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute1);
        }, "Printer should be reusable");

        verify(mockAttributeVisitor, times(2))
            .visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute2);
    }

    /**
     * Tests that the method delegates exactly once per call.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_delegatesExactlyOnce() {
        // Arrange
        LocalVariableTableAttribute attribute = new LocalVariableTableAttribute();

        // Act
        changedCodePrinter.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert - should delegate exactly once, no more, no less
        verify(mockAttributeVisitor, times(1))
            .visitLocalVariableTableAttribute(any(), any(), any(), any());
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method doesn't call any other visitor methods.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_doesNotCallOtherVisitorMethods() {
        // Arrange
        LocalVariableTableAttribute attribute = new LocalVariableTableAttribute();

        // Act
        changedCodePrinter.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify only visitLocalVariableTableAttribute was called
        verify(mockAttributeVisitor, times(1))
            .visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method returns normally (doesn't hang or loop).
     */
    @Test
    public void testVisitLocalVariableTableAttribute_returnsImmediately() {
        // Arrange
        LocalVariableTableAttribute attribute = new LocalVariableTableAttribute();

        // Act
        long startTime = System.nanoTime();
        changedCodePrinter.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);
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
    public void testVisitLocalVariableTableAttribute_withFreshPrinter_doesNotThrow() {
        // Arrange
        AttributeVisitor visitor = mock(AttributeVisitor.class);
        ChangedCodePrinter freshPrinter = new ChangedCodePrinter(visitor);
        LocalVariableTableAttribute attribute = new LocalVariableTableAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> freshPrinter.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute),
                "Method should work with a newly created printer");
    }

    /**
     * Tests that the same attribute can be visited multiple times with the same parameters.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_sameAttributeMultipleTimes() {
        // Arrange
        LocalVariableTableAttribute attribute = new LocalVariableTableAttribute();

        // Act
        changedCodePrinter.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);
        changedCodePrinter.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);
        changedCodePrinter.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert
        verify(mockAttributeVisitor, times(3))
            .visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the attribute.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_doesNotModifyAttribute() {
        // Arrange
        LocalVariableTableAttribute attribute = new LocalVariableTableAttribute();

        // Act
        changedCodePrinter.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify the call succeeded and delegated properly
        verify(mockAttributeVisitor, times(1))
            .visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the clazz.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_doesNotModifyClazz() {
        // Arrange
        LocalVariableTableAttribute attribute = new LocalVariableTableAttribute();

        // Act
        changedCodePrinter.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the method.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_doesNotModifyMethod() {
        // Arrange
        LocalVariableTableAttribute attribute = new LocalVariableTableAttribute();

        // Act
        changedCodePrinter.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the code attribute.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_doesNotModifyCodeAttribute() {
        // Arrange
        LocalVariableTableAttribute attribute = new LocalVariableTableAttribute();

        // Act
        changedCodePrinter.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that the method works correctly when alternating with different attributes.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_alternatingWithOtherAttributes_doesNotInterfere() {
        // Arrange
        LocalVariableTableAttribute attribute1 = new LocalVariableTableAttribute();
        LocalVariableTableAttribute attribute2 = new LocalVariableTableAttribute();

        // Act - alternate calls
        changedCodePrinter.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute1);
        changedCodePrinter.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute2);
        changedCodePrinter.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute1);

        // Assert
        verify(mockAttributeVisitor, times(2))
            .visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute2);
    }

    /**
     * Tests that the method works with all parameters being different instances.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_withAllDifferentParameters_delegatesCorrectly() {
        // Arrange
        Clazz clazz1 = new ProgramClass();
        Method method1 = new ProgramMethod();
        CodeAttribute codeAttribute1 = new CodeAttribute();
        LocalVariableTableAttribute attribute1 = new LocalVariableTableAttribute();

        Clazz clazz2 = new ProgramClass();
        Method method2 = new ProgramMethod();
        CodeAttribute codeAttribute2 = new CodeAttribute();
        LocalVariableTableAttribute attribute2 = new LocalVariableTableAttribute();

        // Act
        changedCodePrinter.visitLocalVariableTableAttribute(clazz1, method1, codeAttribute1, attribute1);
        changedCodePrinter.visitLocalVariableTableAttribute(clazz2, method2, codeAttribute2, attribute2);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitLocalVariableTableAttribute(clazz1, method1, codeAttribute1, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitLocalVariableTableAttribute(clazz2, method2, codeAttribute2, attribute2);
    }
}
