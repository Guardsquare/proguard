package proguard.optimize.evaluation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.LocalVariableTableAttribute;
import proguard.classfile.attribute.visitor.LocalVariableInfoVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link SimpleEnumDescriptorSimplifier#visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute)}.
 *
 * The visitLocalVariableTableAttribute method delegates to the localVariablesAccept method
 * of the LocalVariableTableAttribute, which processes each local variable in the table
 * by calling back to the simplifier's visitLocalVariableInfo method.
 *
 * This delegation enables the simplifier to process and simplify enum descriptors in
 * local variable declarations within the local variable table.
 */
public class SimpleEnumDescriptorSimplifierClaude_visitLocalVariableTableAttributeTest {

    private SimpleEnumDescriptorSimplifier simplifier;
    private Clazz clazz;
    private Method method;
    private CodeAttribute codeAttribute;
    private LocalVariableTableAttribute localVariableTableAttribute;

    @BeforeEach
    public void setUp() {
        simplifier = new SimpleEnumDescriptorSimplifier();
        clazz = new ProgramClass();
        method = new ProgramMethod();
        codeAttribute = new CodeAttribute();
        localVariableTableAttribute = mock(LocalVariableTableAttribute.class);
    }

    /**
     * Tests that visitLocalVariableTableAttribute correctly delegates to localVariablesAccept.
     * This verifies the core functionality of the method - delegation to process local variables.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_delegatesToLocalVariablesAccept() {
        // Act
        simplifier.visitLocalVariableTableAttribute(clazz, method, codeAttribute, localVariableTableAttribute);

        // Assert - verify that localVariablesAccept was called with correct parameters
        verify(localVariableTableAttribute).localVariablesAccept(clazz, method, codeAttribute, simplifier);
    }

    /**
     * Tests that visitLocalVariableTableAttribute can be called with valid mocks without exceptions.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_withValidMocks_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() ->
            simplifier.visitLocalVariableTableAttribute(clazz, method, codeAttribute, localVariableTableAttribute)
        );
    }

    /**
     * Tests that visitLocalVariableTableAttribute can be called multiple times.
     * Each call should independently delegate to localVariablesAccept.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_calledMultipleTimes_delegatesEachTime() {
        // Act
        simplifier.visitLocalVariableTableAttribute(clazz, method, codeAttribute, localVariableTableAttribute);
        simplifier.visitLocalVariableTableAttribute(clazz, method, codeAttribute, localVariableTableAttribute);
        simplifier.visitLocalVariableTableAttribute(clazz, method, codeAttribute, localVariableTableAttribute);

        // Assert - verify localVariablesAccept was called exactly 3 times
        verify(localVariableTableAttribute, times(3))
            .localVariablesAccept(clazz, method, codeAttribute, simplifier);
    }

    /**
     * Tests that visitLocalVariableTableAttribute works with different attribute instances.
     * Each attribute instance should have its localVariablesAccept method called.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_withDifferentAttributes_delegatesToEach() {
        // Arrange
        LocalVariableTableAttribute attr1 = mock(LocalVariableTableAttribute.class);
        LocalVariableTableAttribute attr2 = mock(LocalVariableTableAttribute.class);
        LocalVariableTableAttribute attr3 = mock(LocalVariableTableAttribute.class);

        // Act
        simplifier.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attr1);
        simplifier.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attr2);
        simplifier.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attr3);

        // Assert - verify each attribute's localVariablesAccept was called once
        verify(attr1).localVariablesAccept(clazz, method, codeAttribute, simplifier);
        verify(attr2).localVariablesAccept(clazz, method, codeAttribute, simplifier);
        verify(attr3).localVariablesAccept(clazz, method, codeAttribute, simplifier);
    }

    /**
     * Tests that visitLocalVariableTableAttribute passes the simplifier itself as the visitor.
     * This is crucial because the simplifier implements LocalVariableInfoVisitor.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_passesSimplifierAsVisitor() {
        // Act
        simplifier.visitLocalVariableTableAttribute(clazz, method, codeAttribute, localVariableTableAttribute);

        // Assert - verify that the simplifier itself is passed as the visitor parameter
        verify(localVariableTableAttribute).localVariablesAccept(
            eq(clazz),
            eq(method),
            eq(codeAttribute),
            same(simplifier)  // The simplifier itself should be passed as visitor
        );
    }

    /**
     * Tests that visitLocalVariableTableAttribute works with different clazz instances.
     * Each clazz should be correctly passed through to localVariablesAccept.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_withDifferentClazz_passesCorrectClazz() {
        // Arrange
        Clazz clazz1 = new ProgramClass();
        Clazz clazz2 = new ProgramClass();

        // Act
        simplifier.visitLocalVariableTableAttribute(clazz1, method, codeAttribute, localVariableTableAttribute);
        simplifier.visitLocalVariableTableAttribute(clazz2, method, codeAttribute, localVariableTableAttribute);

        // Assert - verify the correct clazz was passed in each call
        verify(localVariableTableAttribute).localVariablesAccept(clazz1, method, codeAttribute, simplifier);
        verify(localVariableTableAttribute).localVariablesAccept(clazz2, method, codeAttribute, simplifier);
    }

    /**
     * Tests that visitLocalVariableTableAttribute works with different method instances.
     * Each method should be correctly passed through to localVariablesAccept.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_withDifferentMethod_passesCorrectMethod() {
        // Arrange
        Method method1 = new ProgramMethod();
        Method method2 = new ProgramMethod();

        // Act
        simplifier.visitLocalVariableTableAttribute(clazz, method1, codeAttribute, localVariableTableAttribute);
        simplifier.visitLocalVariableTableAttribute(clazz, method2, codeAttribute, localVariableTableAttribute);

        // Assert - verify the correct method was passed in each call
        verify(localVariableTableAttribute).localVariablesAccept(clazz, method1, codeAttribute, simplifier);
        verify(localVariableTableAttribute).localVariablesAccept(clazz, method2, codeAttribute, simplifier);
    }

    /**
     * Tests that visitLocalVariableTableAttribute works with different code attribute instances.
     * Each code attribute should be correctly passed through to localVariablesAccept.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_withDifferentCodeAttribute_passesCorrectCodeAttribute() {
        // Arrange
        CodeAttribute codeAttr1 = new CodeAttribute();
        CodeAttribute codeAttr2 = new CodeAttribute();

        // Act
        simplifier.visitLocalVariableTableAttribute(clazz, method, codeAttr1, localVariableTableAttribute);
        simplifier.visitLocalVariableTableAttribute(clazz, method, codeAttr2, localVariableTableAttribute);

        // Assert - verify the correct code attribute was passed in each call
        verify(localVariableTableAttribute).localVariablesAccept(clazz, method, codeAttr1, simplifier);
        verify(localVariableTableAttribute).localVariablesAccept(clazz, method, codeAttr2, simplifier);
    }

    /**
     * Tests that visitLocalVariableTableAttribute doesn't interact with parameters beyond delegation.
     * The method should only delegate and not directly interact with clazz, method, or codeAttribute.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_doesNotDirectlyInteractWithParameters() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        Method mockMethod = mock(Method.class);
        CodeAttribute mockCodeAttribute = mock(CodeAttribute.class);

        // Act
        simplifier.visitLocalVariableTableAttribute(mockClazz, mockMethod, mockCodeAttribute, localVariableTableAttribute);

        // Assert - verify no direct interactions with parameters (they're only passed through)
        verifyNoInteractions(mockClazz);
        verifyNoInteractions(mockMethod);
        verifyNoInteractions(mockCodeAttribute);
        // localVariableTableAttribute should have been called via delegation
        verify(localVariableTableAttribute, times(1))
            .localVariablesAccept(any(), any(), any(), any(LocalVariableInfoVisitor.class));
    }

    /**
     * Tests that visitLocalVariableTableAttribute maintains correct order when called with multiple attributes.
     * This ensures that sequential calls maintain independence and proper delegation.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_sequentialCalls_maintainIndependence() {
        // Arrange
        LocalVariableTableAttribute attr1 = mock(LocalVariableTableAttribute.class);
        LocalVariableTableAttribute attr2 = mock(LocalVariableTableAttribute.class);

        // Act - call with first attribute
        simplifier.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attr1);
        verify(attr1).localVariablesAccept(clazz, method, codeAttribute, simplifier);

        // Act - call with second attribute
        simplifier.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attr2);
        verify(attr2).localVariablesAccept(clazz, method, codeAttribute, simplifier);

        // Assert - first attribute should not have been called again
        verify(attr1, times(1)).localVariablesAccept(any(), any(), any(), any(LocalVariableInfoVisitor.class));
    }

    /**
     * Tests that visitLocalVariableTableAttribute integrates correctly with the visitor pattern.
     * The simplifier implements LocalVariableInfoVisitor, so it should be a valid visitor.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_simplifierIsValidVisitor() {
        // Arrange & Assert - verify the simplifier is an instance of LocalVariableInfoVisitor
        assertTrue(simplifier instanceof LocalVariableInfoVisitor,
            "Simplifier should implement LocalVariableInfoVisitor to be used as a visitor");

        // Act
        simplifier.visitLocalVariableTableAttribute(clazz, method, codeAttribute, localVariableTableAttribute);

        // Assert - verify it's passed as a LocalVariableInfoVisitor
        verify(localVariableTableAttribute).localVariablesAccept(
            any(Clazz.class),
            any(Method.class),
            any(CodeAttribute.class),
            any(LocalVariableInfoVisitor.class)
        );
    }

    /**
     * Tests that visitLocalVariableTableAttribute can be called in rapid succession without issues.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_rapidSuccessiveCalls_doesNotThrow() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                simplifier.visitLocalVariableTableAttribute(clazz, method, codeAttribute, localVariableTableAttribute);
            }
        }, "Rapid successive calls should not throw any exception");

        // Verify the delegation occurred 100 times
        verify(localVariableTableAttribute, times(100))
            .localVariablesAccept(clazz, method, codeAttribute, simplifier);
    }

    /**
     * Tests that visitLocalVariableTableAttribute delegates exactly once per call.
     * Ensures no duplicate or missing delegations occur.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_delegatesExactlyOnce() {
        // Act
        simplifier.visitLocalVariableTableAttribute(clazz, method, codeAttribute, localVariableTableAttribute);

        // Assert - should delegate exactly once, no more, no less
        verify(localVariableTableAttribute, times(1))
            .localVariablesAccept(any(), any(), any(), any(LocalVariableInfoVisitor.class));
        verifyNoMoreInteractions(localVariableTableAttribute);
    }

    /**
     * Tests that visitLocalVariableTableAttribute works correctly when alternating between different attributes.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_alternatingAttributes_delegatesCorrectly() {
        // Arrange
        LocalVariableTableAttribute attr1 = mock(LocalVariableTableAttribute.class);
        LocalVariableTableAttribute attr2 = mock(LocalVariableTableAttribute.class);

        // Act - alternate calls
        simplifier.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attr1);
        simplifier.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attr2);
        simplifier.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attr1);

        // Assert
        verify(attr1, times(2))
            .localVariablesAccept(clazz, method, codeAttribute, simplifier);
        verify(attr2, times(1))
            .localVariablesAccept(clazz, method, codeAttribute, simplifier);
    }

    /**
     * Tests that visitLocalVariableTableAttribute works with all parameters being different instances.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_withAllDifferentParameters_delegatesCorrectly() {
        // Arrange
        Clazz clazz1 = new ProgramClass();
        Method method1 = new ProgramMethod();
        CodeAttribute codeAttribute1 = new CodeAttribute();
        LocalVariableTableAttribute attribute1 = mock(LocalVariableTableAttribute.class);

        Clazz clazz2 = new ProgramClass();
        Method method2 = new ProgramMethod();
        CodeAttribute codeAttribute2 = new CodeAttribute();
        LocalVariableTableAttribute attribute2 = mock(LocalVariableTableAttribute.class);

        // Act
        simplifier.visitLocalVariableTableAttribute(clazz1, method1, codeAttribute1, attribute1);
        simplifier.visitLocalVariableTableAttribute(clazz2, method2, codeAttribute2, attribute2);

        // Assert
        verify(attribute1, times(1))
            .localVariablesAccept(clazz1, method1, codeAttribute1, simplifier);
        verify(attribute2, times(1))
            .localVariablesAccept(clazz2, method2, codeAttribute2, simplifier);
    }

    /**
     * Tests that visitLocalVariableTableAttribute returns immediately without hanging.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_returnsImmediately() {
        // Act
        long startTime = System.nanoTime();
        simplifier.visitLocalVariableTableAttribute(clazz, method, codeAttribute, localVariableTableAttribute);
        long endTime = System.nanoTime();

        // Assert - should complete very quickly (within 1 second)
        long durationNanos = endTime - startTime;
        long oneSecondInNanos = 1_000_000_000L;
        assertTrue(durationNanos < oneSecondInNanos,
                "Method should return immediately, took " + durationNanos + " nanoseconds");
    }

    /**
     * Tests that visitLocalVariableTableAttribute works with a freshly created simplifier.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_withFreshSimplifier_doesNotThrow() {
        // Arrange
        SimpleEnumDescriptorSimplifier freshSimplifier = new SimpleEnumDescriptorSimplifier();

        // Act & Assert
        assertDoesNotThrow(() ->
            freshSimplifier.visitLocalVariableTableAttribute(clazz, method, codeAttribute, localVariableTableAttribute),
            "Method should work with a newly created simplifier");
    }

    /**
     * Tests that the same attribute can be visited multiple times with the same parameters.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_sameAttributeMultipleTimes() {
        // Act
        simplifier.visitLocalVariableTableAttribute(clazz, method, codeAttribute, localVariableTableAttribute);
        simplifier.visitLocalVariableTableAttribute(clazz, method, codeAttribute, localVariableTableAttribute);
        simplifier.visitLocalVariableTableAttribute(clazz, method, codeAttribute, localVariableTableAttribute);

        // Assert
        verify(localVariableTableAttribute, times(3))
            .localVariablesAccept(clazz, method, codeAttribute, simplifier);
    }

    /**
     * Tests that visitLocalVariableTableAttribute passes parameters in the correct order.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_passesParametersInCorrectOrder() {
        // Act
        simplifier.visitLocalVariableTableAttribute(clazz, method, codeAttribute, localVariableTableAttribute);

        // Assert - verify the parameters are in correct order
        verify(localVariableTableAttribute).localVariablesAccept(
            argThat(arg -> arg == clazz),
            argThat(arg -> arg == method),
            argThat(arg -> arg == codeAttribute),
            argThat(arg -> arg == simplifier)
        );
    }

    /**
     * Tests that multiple simplifier instances work independently.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_multipleInstances_workIndependently() {
        // Arrange
        SimpleEnumDescriptorSimplifier simplifier1 = new SimpleEnumDescriptorSimplifier();
        SimpleEnumDescriptorSimplifier simplifier2 = new SimpleEnumDescriptorSimplifier();
        LocalVariableTableAttribute attr1 = mock(LocalVariableTableAttribute.class);
        LocalVariableTableAttribute attr2 = mock(LocalVariableTableAttribute.class);

        // Act
        simplifier1.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attr1);
        simplifier2.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attr2);

        // Assert
        verify(attr1, times(1)).localVariablesAccept(clazz, method, codeAttribute, simplifier1);
        verify(attr2, times(1)).localVariablesAccept(clazz, method, codeAttribute, simplifier2);
        verifyNoMoreInteractions(attr1, attr2);
    }

    /**
     * Tests that the simplifier can be reused after calling visitLocalVariableTableAttribute.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_simplifierReusable() {
        // Arrange
        LocalVariableTableAttribute attribute1 = mock(LocalVariableTableAttribute.class);
        LocalVariableTableAttribute attribute2 = mock(LocalVariableTableAttribute.class);

        // Act & Assert - reuse the same simplifier
        assertDoesNotThrow(() -> {
            simplifier.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute1);
            simplifier.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute2);
            simplifier.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attribute1);
        }, "Simplifier should be reusable");

        verify(attribute1, times(2))
            .localVariablesAccept(clazz, method, codeAttribute, simplifier);
        verify(attribute2, times(1))
            .localVariablesAccept(clazz, method, codeAttribute, simplifier);
    }

    /**
     * Tests that visitLocalVariableTableAttribute integrates properly with the AttributeVisitor interface.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_asAttributeVisitor_delegatesCorrectly() {
        // Arrange
        proguard.classfile.attribute.visitor.AttributeVisitor visitor = simplifier;

        // Act
        visitor.visitLocalVariableTableAttribute(clazz, method, codeAttribute, localVariableTableAttribute);

        // Assert
        verify(localVariableTableAttribute, times(1))
            .localVariablesAccept(clazz, method, codeAttribute, simplifier);
    }

    /**
     * Tests that visitLocalVariableTableAttribute works when called through the CodeAttribute visitor chain.
     * This simulates the real usage scenario where visitCodeAttribute calls this method.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_throughVisitorChain_delegatesCorrectly() {
        // Arrange
        CodeAttribute realCodeAttribute = new CodeAttribute();
        LocalVariableTableAttribute realAttribute = mock(LocalVariableTableAttribute.class);

        // Act - simulate what visitCodeAttribute does
        simplifier.visitLocalVariableTableAttribute(clazz, method, realCodeAttribute, realAttribute);

        // Assert
        verify(realAttribute, times(1))
            .localVariablesAccept(clazz, method, realCodeAttribute, simplifier);
    }

    /**
     * Tests that visitLocalVariableTableAttribute doesn't throw when the attribute's localVariablesAccept does nothing.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_withNoOpAttribute_doesNotThrow() {
        // Arrange
        LocalVariableTableAttribute noOpAttribute = mock(LocalVariableTableAttribute.class);
        doNothing().when(noOpAttribute).localVariablesAccept(any(), any(), any(), any());

        // Act & Assert
        assertDoesNotThrow(() ->
            simplifier.visitLocalVariableTableAttribute(clazz, method, codeAttribute, noOpAttribute),
            "Should not throw when attribute's localVariablesAccept is no-op");
    }

    /**
     * Tests that visitLocalVariableTableAttribute propagates exceptions from the attribute's localVariablesAccept.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_whenAttributeThrows_propagatesException() {
        // Arrange
        LocalVariableTableAttribute throwingAttribute = mock(LocalVariableTableAttribute.class);
        RuntimeException expectedException = new RuntimeException("Test exception");
        doThrow(expectedException)
            .when(throwingAttribute)
            .localVariablesAccept(any(), any(), any(), any());

        // Act & Assert
        RuntimeException thrownException = assertThrows(RuntimeException.class, () ->
            simplifier.visitLocalVariableTableAttribute(clazz, method, codeAttribute, throwingAttribute),
            "Should propagate exception from attribute's localVariablesAccept");

        assertEquals(expectedException, thrownException, "Should throw the same exception");
    }

    /**
     * Tests that visitLocalVariableTableAttribute doesn't call any other visitor methods.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_doesNotCallOtherVisitorMethods() {
        // Act
        simplifier.visitLocalVariableTableAttribute(clazz, method, codeAttribute, localVariableTableAttribute);

        // Assert - verify only localVariablesAccept was called
        verify(localVariableTableAttribute, times(1))
            .localVariablesAccept(clazz, method, codeAttribute, simplifier);
        verifyNoMoreInteractions(localVariableTableAttribute);
    }

    /**
     * Tests that visitLocalVariableTableAttribute works correctly in a complex visitor chain scenario.
     * Simulates processing multiple methods with multiple local variable tables.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_inComplexVisitorChain_delegatesCorrectly() {
        // Arrange
        Method method1 = new ProgramMethod();
        Method method2 = new ProgramMethod();
        LocalVariableTableAttribute attr1 = mock(LocalVariableTableAttribute.class);
        LocalVariableTableAttribute attr2 = mock(LocalVariableTableAttribute.class);
        LocalVariableTableAttribute attr3 = mock(LocalVariableTableAttribute.class);

        // Act - simulate visiting multiple methods and their local variable tables
        simplifier.visitLocalVariableTableAttribute(clazz, method1, codeAttribute, attr1);
        simplifier.visitLocalVariableTableAttribute(clazz, method1, codeAttribute, attr2);
        simplifier.visitLocalVariableTableAttribute(clazz, method2, codeAttribute, attr3);

        // Assert - verify each delegation occurred correctly
        verify(attr1, times(1)).localVariablesAccept(clazz, method1, codeAttribute, simplifier);
        verify(attr2, times(1)).localVariablesAccept(clazz, method1, codeAttribute, simplifier);
        verify(attr3, times(1)).localVariablesAccept(clazz, method2, codeAttribute, simplifier);
    }
}
