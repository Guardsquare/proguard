package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.LocalVariableTypeTableAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ChangedCodePrinter#visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTypeTableAttribute)}.
 *
 * The visitLocalVariableTypeTableAttribute method in ChangedCodePrinter is a delegation method.
 * It simply forwards the call to the wrapped AttributeVisitor without any additional logic,
 * as LocalVariableTypeTableAttribute does not contain bytecode that needs change detection.
 *
 * These tests verify that the method:
 * 1. Correctly delegates to the wrapped visitor
 * 2. Passes the correct parameters in the correct order
 * 3. Works with various inputs including edge cases
 */
public class ChangedCodePrinterClaude_visitLocalVariableTypeTableAttributeTest {

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
     * Tests that visitLocalVariableTypeTableAttribute delegates to the wrapped visitor.
     * Verifies that the method calls the visitor with the correct parameters.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_delegatesToWrappedVisitor() {
        // Arrange
        LocalVariableTypeTableAttribute attribute = new LocalVariableTypeTableAttribute();

        // Act
        changedCodePrinter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that the method does not throw an exception with valid inputs.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withValidInputs_doesNotThrow() {
        // Arrange
        LocalVariableTypeTableAttribute attribute = new LocalVariableTypeTableAttribute();

        // Act & Assert
        assertDoesNotThrow(() ->
            changedCodePrinter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute),
            "visitLocalVariableTypeTableAttribute should not throw any exception");
    }

    /**
     * Tests that the method can be called multiple times without issues.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_calledMultipleTimes_doesNotThrow() {
        // Arrange
        LocalVariableTypeTableAttribute attribute = new LocalVariableTypeTableAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);
            changedCodePrinter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);
            changedCodePrinter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);
        }, "Multiple calls should not throw any exception");

        // Verify the visitor was called multiple times
        verify(mockAttributeVisitor, times(3))
            .visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that the method works with different Clazz instances.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withDifferentClasses_delegatesCorrectly() {
        // Arrange
        Clazz clazz1 = new ProgramClass();
        Clazz clazz2 = new ProgramClass();
        LocalVariableTypeTableAttribute attribute = new LocalVariableTypeTableAttribute();

        // Act
        changedCodePrinter.visitLocalVariableTypeTableAttribute(clazz1, method, codeAttribute, attribute);
        changedCodePrinter.visitLocalVariableTypeTableAttribute(clazz2, method, codeAttribute, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitLocalVariableTypeTableAttribute(clazz1, method, codeAttribute, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitLocalVariableTypeTableAttribute(clazz2, method, codeAttribute, attribute);
    }

    /**
     * Tests that the method works with different Method instances.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withDifferentMethods_delegatesCorrectly() {
        // Arrange
        Method method1 = new ProgramMethod();
        Method method2 = new ProgramMethod();
        LocalVariableTypeTableAttribute attribute = new LocalVariableTypeTableAttribute();

        // Act
        changedCodePrinter.visitLocalVariableTypeTableAttribute(clazz, method1, codeAttribute, attribute);
        changedCodePrinter.visitLocalVariableTypeTableAttribute(clazz, method2, codeAttribute, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitLocalVariableTypeTableAttribute(clazz, method1, codeAttribute, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitLocalVariableTypeTableAttribute(clazz, method2, codeAttribute, attribute);
    }

    /**
     * Tests that the method works with different CodeAttribute instances.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withDifferentCodeAttributes_delegatesCorrectly() {
        // Arrange
        CodeAttribute codeAttribute1 = new CodeAttribute();
        CodeAttribute codeAttribute2 = new CodeAttribute();
        LocalVariableTypeTableAttribute attribute = new LocalVariableTypeTableAttribute();

        // Act
        changedCodePrinter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute1, attribute);
        changedCodePrinter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute2, attribute);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute1, attribute);
        verify(mockAttributeVisitor, times(1))
            .visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute2, attribute);
    }

    /**
     * Tests that the method works with different LocalVariableTypeTableAttribute instances.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withDifferentAttributes_delegatesCorrectly() {
        // Arrange
        LocalVariableTypeTableAttribute attribute1 = new LocalVariableTypeTableAttribute();
        LocalVariableTypeTableAttribute attribute2 = new LocalVariableTypeTableAttribute();

        // Act
        changedCodePrinter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute1);
        changedCodePrinter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute2);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute2);
    }

    /**
     * Tests that the method passes parameters in the correct order.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_passesParametersInCorrectOrder() {
        // Arrange
        LocalVariableTypeTableAttribute attribute = new LocalVariableTypeTableAttribute();

        // Act
        changedCodePrinter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify the parameters are in correct order
        verify(mockAttributeVisitor).visitLocalVariableTypeTableAttribute(
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
    public void testVisitLocalVariableTypeTableAttribute_rapidSuccessiveCalls_doesNotThrow() {
        // Arrange
        LocalVariableTypeTableAttribute attribute = new LocalVariableTypeTableAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                changedCodePrinter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);
            }
        }, "Rapid successive calls should not throw any exception");

        // Verify the visitor was called 100 times
        verify(mockAttributeVisitor, times(100))
            .visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that the method works when the wrapped visitor does nothing (no-op).
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withNoOpVisitor_doesNotThrow() {
        // Arrange
        AttributeVisitor noOpVisitor = mock(AttributeVisitor.class);
        doNothing().when(noOpVisitor).visitLocalVariableTypeTableAttribute(any(), any(), any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(noOpVisitor);
        LocalVariableTypeTableAttribute attribute = new LocalVariableTypeTableAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> printer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute),
            "Should not throw when visitor is no-op");
    }

    /**
     * Tests that the method works when the wrapped visitor throws an exception.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_whenVisitorThrows_propagatesException() {
        // Arrange
        AttributeVisitor throwingVisitor = mock(AttributeVisitor.class);
        RuntimeException expectedException = new RuntimeException("Test exception");
        doThrow(expectedException)
            .when(throwingVisitor)
            .visitLocalVariableTypeTableAttribute(any(), any(), any(), any());
        ChangedCodePrinter printer = new ChangedCodePrinter(throwingVisitor);
        LocalVariableTypeTableAttribute attribute = new LocalVariableTypeTableAttribute();

        // Act & Assert
        RuntimeException thrownException = assertThrows(RuntimeException.class, () ->
            printer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute),
            "Should propagate exception from wrapped visitor");

        assertEquals(expectedException, thrownException, "Should throw the same exception");
    }

    /**
     * Tests that multiple ChangedCodePrinter instances work independently.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_multipleInstances_workIndependently() {
        // Arrange
        AttributeVisitor visitor1 = mock(AttributeVisitor.class);
        AttributeVisitor visitor2 = mock(AttributeVisitor.class);
        ChangedCodePrinter printer1 = new ChangedCodePrinter(visitor1);
        ChangedCodePrinter printer2 = new ChangedCodePrinter(visitor2);
        LocalVariableTypeTableAttribute attribute = new LocalVariableTypeTableAttribute();

        // Act
        printer1.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);
        printer2.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert
        verify(visitor1, times(1)).visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);
        verify(visitor2, times(1)).visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);
        verifyNoMoreInteractions(visitor1, visitor2);
    }

    /**
     * Tests that the printer can be reused after calling visitLocalVariableTypeTableAttribute.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_printerReusable() {
        // Arrange
        LocalVariableTypeTableAttribute attribute1 = new LocalVariableTypeTableAttribute();
        LocalVariableTypeTableAttribute attribute2 = new LocalVariableTypeTableAttribute();

        // Act & Assert - reuse the same printer
        assertDoesNotThrow(() -> {
            changedCodePrinter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute1);
            changedCodePrinter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute2);
            changedCodePrinter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute1);
        }, "Printer should be reusable");

        verify(mockAttributeVisitor, times(2))
            .visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute2);
    }

    /**
     * Tests that the method delegates exactly once per call.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_delegatesExactlyOnce() {
        // Arrange
        LocalVariableTypeTableAttribute attribute = new LocalVariableTypeTableAttribute();

        // Act
        changedCodePrinter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert - should delegate exactly once, no more, no less
        verify(mockAttributeVisitor, times(1))
            .visitLocalVariableTypeTableAttribute(any(), any(), any(), any());
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method doesn't call any other visitor methods.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_doesNotCallOtherVisitorMethods() {
        // Arrange
        LocalVariableTypeTableAttribute attribute = new LocalVariableTypeTableAttribute();

        // Act
        changedCodePrinter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify only visitLocalVariableTypeTableAttribute was called
        verify(mockAttributeVisitor, times(1))
            .visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);
        verifyNoMoreInteractions(mockAttributeVisitor);
    }

    /**
     * Tests that the method returns normally (doesn't hang or loop).
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_returnsImmediately() {
        // Arrange
        LocalVariableTypeTableAttribute attribute = new LocalVariableTypeTableAttribute();

        // Act
        long startTime = System.nanoTime();
        changedCodePrinter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);
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
    public void testVisitLocalVariableTypeTableAttribute_withFreshPrinter_doesNotThrow() {
        // Arrange
        AttributeVisitor visitor = mock(AttributeVisitor.class);
        ChangedCodePrinter freshPrinter = new ChangedCodePrinter(visitor);
        LocalVariableTypeTableAttribute attribute = new LocalVariableTypeTableAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> freshPrinter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute),
                "Method should work with a newly created printer");
    }

    /**
     * Tests that the same attribute can be visited multiple times with the same parameters.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_sameAttributeMultipleTimes() {
        // Arrange
        LocalVariableTypeTableAttribute attribute = new LocalVariableTypeTableAttribute();

        // Act
        changedCodePrinter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);
        changedCodePrinter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);
        changedCodePrinter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert
        verify(mockAttributeVisitor, times(3))
            .visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the attribute.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_doesNotModifyAttribute() {
        // Arrange
        LocalVariableTypeTableAttribute attribute = new LocalVariableTypeTableAttribute();

        // Act
        changedCodePrinter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify the call succeeded and delegated properly
        verify(mockAttributeVisitor, times(1))
            .visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the clazz.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_doesNotModifyClazz() {
        // Arrange
        LocalVariableTypeTableAttribute attribute = new LocalVariableTypeTableAttribute();

        // Act
        changedCodePrinter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the method.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_doesNotModifyMethod() {
        // Arrange
        LocalVariableTypeTableAttribute attribute = new LocalVariableTypeTableAttribute();

        // Act
        changedCodePrinter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that calling the method doesn't modify the code attribute.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_doesNotModifyCodeAttribute() {
        // Arrange
        LocalVariableTypeTableAttribute attribute = new LocalVariableTypeTableAttribute();

        // Act
        changedCodePrinter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);

        // Assert - verify the call succeeded
        verify(mockAttributeVisitor, times(1))
            .visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute);
    }

    /**
     * Tests that the method works correctly when alternating with different attributes.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_alternatingWithOtherAttributes_doesNotInterfere() {
        // Arrange
        LocalVariableTypeTableAttribute attribute1 = new LocalVariableTypeTableAttribute();
        LocalVariableTypeTableAttribute attribute2 = new LocalVariableTypeTableAttribute();

        // Act - alternate calls
        changedCodePrinter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute1);
        changedCodePrinter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute2);
        changedCodePrinter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute1);

        // Assert
        verify(mockAttributeVisitor, times(2))
            .visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute2);
    }

    /**
     * Tests that the method works with all parameters being different instances.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withAllDifferentParameters_delegatesCorrectly() {
        // Arrange
        Clazz clazz1 = new ProgramClass();
        Method method1 = new ProgramMethod();
        CodeAttribute codeAttribute1 = new CodeAttribute();
        LocalVariableTypeTableAttribute attribute1 = new LocalVariableTypeTableAttribute();

        Clazz clazz2 = new ProgramClass();
        Method method2 = new ProgramMethod();
        CodeAttribute codeAttribute2 = new CodeAttribute();
        LocalVariableTypeTableAttribute attribute2 = new LocalVariableTypeTableAttribute();

        // Act
        changedCodePrinter.visitLocalVariableTypeTableAttribute(clazz1, method1, codeAttribute1, attribute1);
        changedCodePrinter.visitLocalVariableTypeTableAttribute(clazz2, method2, codeAttribute2, attribute2);

        // Assert
        verify(mockAttributeVisitor, times(1))
            .visitLocalVariableTypeTableAttribute(clazz1, method1, codeAttribute1, attribute1);
        verify(mockAttributeVisitor, times(1))
            .visitLocalVariableTypeTableAttribute(clazz2, method2, codeAttribute2, attribute2);
    }
}
