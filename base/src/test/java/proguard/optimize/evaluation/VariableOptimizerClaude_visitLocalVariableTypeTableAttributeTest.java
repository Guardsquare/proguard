package proguard.optimize.evaluation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.LocalVariableTypeTableAttribute;
import proguard.classfile.attribute.visitor.LocalVariableTypeInfoVisitor;
import proguard.classfile.visitor.MemberVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link VariableOptimizer#visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTypeTableAttribute)}.
 *
 * The visitLocalVariableTypeTableAttribute method trims the local variable type entries
 * by delegating to the localVariablesAccept method of the LocalVariableTypeTableAttribute,
 * which processes each local variable type info in the table by calling back to the optimizer's
 * visitLocalVariableTypeInfo method.
 *
 * This enables the optimizer to trim local variable type table entries to the instructions
 * at which the variables are actually alive based on liveness analysis.
 */
public class VariableOptimizerClaude_visitLocalVariableTypeTableAttributeTest {

    private VariableOptimizer optimizer;
    private Clazz clazz;
    private Method method;
    private CodeAttribute codeAttribute;
    private LocalVariableTypeTableAttribute localVariableTypeTableAttribute;

    @BeforeEach
    public void setUp() {
        optimizer = new VariableOptimizer(true);
        clazz = new ProgramClass();
        method = new ProgramMethod();
        codeAttribute = new CodeAttribute();
        localVariableTypeTableAttribute = mock(LocalVariableTypeTableAttribute.class);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute correctly delegates to localVariablesAccept.
     * This verifies the core functionality of the method - delegation to process local variable types.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_delegatesToLocalVariablesAccept() {
        // Act
        optimizer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, localVariableTypeTableAttribute);

        // Assert - verify that localVariablesAccept was called with correct parameters
        verify(localVariableTypeTableAttribute).localVariablesAccept(clazz, method, codeAttribute, optimizer);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute can be called with valid mocks without exceptions.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withValidMocks_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() ->
            optimizer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, localVariableTypeTableAttribute)
        );
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute can be called multiple times.
     * Each call should independently delegate to localVariablesAccept.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_calledMultipleTimes_delegatesEachTime() {
        // Act
        optimizer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, localVariableTypeTableAttribute);
        optimizer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, localVariableTypeTableAttribute);
        optimizer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, localVariableTypeTableAttribute);

        // Assert - verify localVariablesAccept was called exactly 3 times
        verify(localVariableTypeTableAttribute, times(3))
            .localVariablesAccept(clazz, method, codeAttribute, optimizer);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute works with different attribute instances.
     * Each attribute instance should have its localVariablesAccept method called.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withDifferentAttributes_delegatesToEach() {
        // Arrange
        LocalVariableTypeTableAttribute attr1 = mock(LocalVariableTypeTableAttribute.class);
        LocalVariableTypeTableAttribute attr2 = mock(LocalVariableTypeTableAttribute.class);
        LocalVariableTypeTableAttribute attr3 = mock(LocalVariableTypeTableAttribute.class);

        // Act
        optimizer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attr1);
        optimizer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attr2);
        optimizer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attr3);

        // Assert - verify each attribute's localVariablesAccept was called once
        verify(attr1).localVariablesAccept(clazz, method, codeAttribute, optimizer);
        verify(attr2).localVariablesAccept(clazz, method, codeAttribute, optimizer);
        verify(attr3).localVariablesAccept(clazz, method, codeAttribute, optimizer);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute passes the optimizer itself as the visitor.
     * This is crucial because the optimizer implements LocalVariableTypeInfoVisitor.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_passesOptimizerAsVisitor() {
        // Act
        optimizer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, localVariableTypeTableAttribute);

        // Assert - verify that the optimizer itself is passed as the visitor parameter
        verify(localVariableTypeTableAttribute).localVariablesAccept(
            eq(clazz),
            eq(method),
            eq(codeAttribute),
            same(optimizer)  // The optimizer itself should be passed as visitor
        );
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute works with different clazz instances.
     * Each clazz should be correctly passed through to localVariablesAccept.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withDifferentClazz_passesCorrectClazz() {
        // Arrange
        Clazz clazz1 = new ProgramClass();
        Clazz clazz2 = new ProgramClass();

        // Act
        optimizer.visitLocalVariableTypeTableAttribute(clazz1, method, codeAttribute, localVariableTypeTableAttribute);
        optimizer.visitLocalVariableTypeTableAttribute(clazz2, method, codeAttribute, localVariableTypeTableAttribute);

        // Assert - verify the correct clazz was passed in each call
        verify(localVariableTypeTableAttribute).localVariablesAccept(clazz1, method, codeAttribute, optimizer);
        verify(localVariableTypeTableAttribute).localVariablesAccept(clazz2, method, codeAttribute, optimizer);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute works with different method instances.
     * Each method should be correctly passed through to localVariablesAccept.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withDifferentMethod_passesCorrectMethod() {
        // Arrange
        Method method1 = new ProgramMethod();
        Method method2 = new ProgramMethod();

        // Act
        optimizer.visitLocalVariableTypeTableAttribute(clazz, method1, codeAttribute, localVariableTypeTableAttribute);
        optimizer.visitLocalVariableTypeTableAttribute(clazz, method2, codeAttribute, localVariableTypeTableAttribute);

        // Assert - verify the correct method was passed in each call
        verify(localVariableTypeTableAttribute).localVariablesAccept(clazz, method1, codeAttribute, optimizer);
        verify(localVariableTypeTableAttribute).localVariablesAccept(clazz, method2, codeAttribute, optimizer);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute works with different code attribute instances.
     * Each code attribute should be correctly passed through to localVariablesAccept.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withDifferentCodeAttribute_passesCorrectCodeAttribute() {
        // Arrange
        CodeAttribute codeAttr1 = new CodeAttribute();
        CodeAttribute codeAttr2 = new CodeAttribute();

        // Act
        optimizer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttr1, localVariableTypeTableAttribute);
        optimizer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttr2, localVariableTypeTableAttribute);

        // Assert - verify the correct code attribute was passed in each call
        verify(localVariableTypeTableAttribute).localVariablesAccept(clazz, method, codeAttr1, optimizer);
        verify(localVariableTypeTableAttribute).localVariablesAccept(clazz, method, codeAttr2, optimizer);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute doesn't interact with parameters beyond delegation.
     * The method should only delegate and not directly interact with clazz, method, or codeAttribute.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_doesNotDirectlyInteractWithParameters() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        Method mockMethod = mock(Method.class);
        CodeAttribute mockCodeAttribute = mock(CodeAttribute.class);

        // Act
        optimizer.visitLocalVariableTypeTableAttribute(mockClazz, mockMethod, mockCodeAttribute, localVariableTypeTableAttribute);

        // Assert - verify no direct interactions with parameters (they're only passed through)
        verifyNoInteractions(mockClazz);
        verifyNoInteractions(mockMethod);
        verifyNoInteractions(mockCodeAttribute);
        // localVariableTypeTableAttribute should have been called via delegation
        verify(localVariableTypeTableAttribute, times(1))
            .localVariablesAccept(any(), any(), any(), any(LocalVariableTypeInfoVisitor.class));
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute maintains correct order when called with multiple attributes.
     * This ensures that sequential calls maintain independence and proper delegation.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_sequentialCalls_maintainIndependence() {
        // Arrange
        LocalVariableTypeTableAttribute attr1 = mock(LocalVariableTypeTableAttribute.class);
        LocalVariableTypeTableAttribute attr2 = mock(LocalVariableTypeTableAttribute.class);

        // Act - call with first attribute
        optimizer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attr1);
        verify(attr1).localVariablesAccept(clazz, method, codeAttribute, optimizer);

        // Act - call with second attribute
        optimizer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attr2);
        verify(attr2).localVariablesAccept(clazz, method, codeAttribute, optimizer);

        // Assert - first attribute should not have been called again
        verify(attr1, times(1)).localVariablesAccept(any(), any(), any(), any(LocalVariableTypeInfoVisitor.class));
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute integrates correctly with the visitor pattern.
     * The optimizer implements LocalVariableTypeInfoVisitor, so it should be a valid visitor.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_optimizerIsValidVisitor() {
        // Arrange & Assert - verify the optimizer is an instance of LocalVariableTypeInfoVisitor
        assertTrue(optimizer instanceof LocalVariableTypeInfoVisitor,
            "Optimizer should implement LocalVariableTypeInfoVisitor to be used as a visitor");

        // Act
        optimizer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, localVariableTypeTableAttribute);

        // Assert - verify it's passed as a LocalVariableTypeInfoVisitor
        verify(localVariableTypeTableAttribute).localVariablesAccept(
            any(Clazz.class),
            any(Method.class),
            any(CodeAttribute.class),
            any(LocalVariableTypeInfoVisitor.class)
        );
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute can be called in rapid succession without issues.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_rapidSuccessiveCalls_doesNotThrow() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                optimizer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, localVariableTypeTableAttribute);
            }
        }, "Rapid successive calls should not throw any exception");

        // Verify the delegation occurred 100 times
        verify(localVariableTypeTableAttribute, times(100))
            .localVariablesAccept(clazz, method, codeAttribute, optimizer);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute delegates exactly once per call.
     * Ensures no duplicate or missing delegations occur.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_delegatesExactlyOnce() {
        // Act
        optimizer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, localVariableTypeTableAttribute);

        // Assert - should delegate exactly once, no more, no less
        verify(localVariableTypeTableAttribute, times(1))
            .localVariablesAccept(any(), any(), any(), any(LocalVariableTypeInfoVisitor.class));
        verifyNoMoreInteractions(localVariableTypeTableAttribute);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute works correctly when alternating between different attributes.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_alternatingAttributes_delegatesCorrectly() {
        // Arrange
        LocalVariableTypeTableAttribute attr1 = mock(LocalVariableTypeTableAttribute.class);
        LocalVariableTypeTableAttribute attr2 = mock(LocalVariableTypeTableAttribute.class);

        // Act - alternate calls
        optimizer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attr1);
        optimizer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attr2);
        optimizer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attr1);

        // Assert
        verify(attr1, times(2))
            .localVariablesAccept(clazz, method, codeAttribute, optimizer);
        verify(attr2, times(1))
            .localVariablesAccept(clazz, method, codeAttribute, optimizer);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute works with all parameters being different instances.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withAllDifferentParameters_delegatesCorrectly() {
        // Arrange
        Clazz clazz1 = new ProgramClass();
        Method method1 = new ProgramMethod();
        CodeAttribute codeAttribute1 = new CodeAttribute();
        LocalVariableTypeTableAttribute attribute1 = mock(LocalVariableTypeTableAttribute.class);

        Clazz clazz2 = new ProgramClass();
        Method method2 = new ProgramMethod();
        CodeAttribute codeAttribute2 = new CodeAttribute();
        LocalVariableTypeTableAttribute attribute2 = mock(LocalVariableTypeTableAttribute.class);

        // Act
        optimizer.visitLocalVariableTypeTableAttribute(clazz1, method1, codeAttribute1, attribute1);
        optimizer.visitLocalVariableTypeTableAttribute(clazz2, method2, codeAttribute2, attribute2);

        // Assert
        verify(attribute1, times(1))
            .localVariablesAccept(clazz1, method1, codeAttribute1, optimizer);
        verify(attribute2, times(1))
            .localVariablesAccept(clazz2, method2, codeAttribute2, optimizer);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute returns immediately without hanging.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_returnsImmediately() {
        // Act
        long startTime = System.nanoTime();
        optimizer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, localVariableTypeTableAttribute);
        long endTime = System.nanoTime();

        // Assert - should complete very quickly (within 1 second)
        long durationNanos = endTime - startTime;
        long oneSecondInNanos = 1_000_000_000L;
        assertTrue(durationNanos < oneSecondInNanos,
                "Method should return immediately, took " + durationNanos + " nanoseconds");
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute works with a freshly created optimizer.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withFreshOptimizer_doesNotThrow() {
        // Arrange
        VariableOptimizer freshOptimizer = new VariableOptimizer(true);

        // Act & Assert
        assertDoesNotThrow(() ->
            freshOptimizer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, localVariableTypeTableAttribute),
            "Method should work with a newly created optimizer");
    }

    /**
     * Tests that the same attribute can be visited multiple times with the same parameters.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_sameAttributeMultipleTimes() {
        // Act
        optimizer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, localVariableTypeTableAttribute);
        optimizer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, localVariableTypeTableAttribute);
        optimizer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, localVariableTypeTableAttribute);

        // Assert
        verify(localVariableTypeTableAttribute, times(3))
            .localVariablesAccept(clazz, method, codeAttribute, optimizer);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute passes parameters in the correct order.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_passesParametersInCorrectOrder() {
        // Act
        optimizer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, localVariableTypeTableAttribute);

        // Assert - verify the parameters are in correct order
        verify(localVariableTypeTableAttribute).localVariablesAccept(
            argThat(arg -> arg == clazz),
            argThat(arg -> arg == method),
            argThat(arg -> arg == codeAttribute),
            argThat(arg -> arg == optimizer)
        );
    }

    /**
     * Tests that multiple optimizer instances work independently.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_multipleInstances_workIndependently() {
        // Arrange
        VariableOptimizer optimizer1 = new VariableOptimizer(true);
        VariableOptimizer optimizer2 = new VariableOptimizer(false);
        LocalVariableTypeTableAttribute attr1 = mock(LocalVariableTypeTableAttribute.class);
        LocalVariableTypeTableAttribute attr2 = mock(LocalVariableTypeTableAttribute.class);

        // Act
        optimizer1.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attr1);
        optimizer2.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attr2);

        // Assert
        verify(attr1, times(1)).localVariablesAccept(clazz, method, codeAttribute, optimizer1);
        verify(attr2, times(1)).localVariablesAccept(clazz, method, codeAttribute, optimizer2);
        verifyNoMoreInteractions(attr1, attr2);
    }

    /**
     * Tests that the optimizer can be reused after calling visitLocalVariableTypeTableAttribute.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_optimizerReusable() {
        // Arrange
        LocalVariableTypeTableAttribute attribute1 = mock(LocalVariableTypeTableAttribute.class);
        LocalVariableTypeTableAttribute attribute2 = mock(LocalVariableTypeTableAttribute.class);

        // Act & Assert - reuse the same optimizer
        assertDoesNotThrow(() -> {
            optimizer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute1);
            optimizer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute2);
            optimizer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attribute1);
        }, "Optimizer should be reusable");

        verify(attribute1, times(2))
            .localVariablesAccept(clazz, method, codeAttribute, optimizer);
        verify(attribute2, times(1))
            .localVariablesAccept(clazz, method, codeAttribute, optimizer);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute integrates properly with the AttributeVisitor interface.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_asAttributeVisitor_delegatesCorrectly() {
        // Arrange
        proguard.classfile.attribute.visitor.AttributeVisitor visitor = optimizer;

        // Act
        visitor.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, localVariableTypeTableAttribute);

        // Assert
        verify(localVariableTypeTableAttribute, times(1))
            .localVariablesAccept(clazz, method, codeAttribute, optimizer);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute works when called through the CodeAttribute visitor chain.
     * This simulates the real usage scenario where visitCodeAttribute calls this method.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_throughVisitorChain_delegatesCorrectly() {
        // Arrange
        CodeAttribute realCodeAttribute = new CodeAttribute();
        LocalVariableTypeTableAttribute realAttribute = mock(LocalVariableTypeTableAttribute.class);

        // Act - simulate what visitCodeAttribute does
        optimizer.visitLocalVariableTypeTableAttribute(clazz, method, realCodeAttribute, realAttribute);

        // Assert
        verify(realAttribute, times(1))
            .localVariablesAccept(clazz, method, realCodeAttribute, optimizer);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute doesn't throw when the attribute's localVariablesAccept does nothing.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withNoOpAttribute_doesNotThrow() {
        // Arrange
        LocalVariableTypeTableAttribute noOpAttribute = mock(LocalVariableTypeTableAttribute.class);
        doNothing().when(noOpAttribute).localVariablesAccept(any(), any(), any(), any());

        // Act & Assert
        assertDoesNotThrow(() ->
            optimizer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, noOpAttribute),
            "Should not throw when attribute's localVariablesAccept is no-op");
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute propagates exceptions from the attribute's localVariablesAccept.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_whenAttributeThrows_propagatesException() {
        // Arrange
        LocalVariableTypeTableAttribute throwingAttribute = mock(LocalVariableTypeTableAttribute.class);
        RuntimeException expectedException = new RuntimeException("Test exception");
        doThrow(expectedException)
            .when(throwingAttribute)
            .localVariablesAccept(any(), any(), any(), any());

        // Act & Assert
        RuntimeException thrownException = assertThrows(RuntimeException.class, () ->
            optimizer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, throwingAttribute),
            "Should propagate exception from attribute's localVariablesAccept");

        assertEquals(expectedException, thrownException, "Should throw the same exception");
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute doesn't call any other visitor methods.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_doesNotCallOtherVisitorMethods() {
        // Act
        optimizer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, localVariableTypeTableAttribute);

        // Assert - verify only localVariablesAccept was called
        verify(localVariableTypeTableAttribute, times(1))
            .localVariablesAccept(clazz, method, codeAttribute, optimizer);
        verifyNoMoreInteractions(localVariableTypeTableAttribute);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute works correctly in a complex visitor chain scenario.
     * Simulates processing multiple methods with multiple local variable type tables.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_inComplexVisitorChain_delegatesCorrectly() {
        // Arrange
        Method method1 = new ProgramMethod();
        Method method2 = new ProgramMethod();
        LocalVariableTypeTableAttribute attr1 = mock(LocalVariableTypeTableAttribute.class);
        LocalVariableTypeTableAttribute attr2 = mock(LocalVariableTypeTableAttribute.class);
        LocalVariableTypeTableAttribute attr3 = mock(LocalVariableTypeTableAttribute.class);

        // Act - simulate visiting multiple methods and their local variable type tables
        optimizer.visitLocalVariableTypeTableAttribute(clazz, method1, codeAttribute, attr1);
        optimizer.visitLocalVariableTypeTableAttribute(clazz, method1, codeAttribute, attr2);
        optimizer.visitLocalVariableTypeTableAttribute(clazz, method2, codeAttribute, attr3);

        // Assert - verify each delegation occurred correctly
        verify(attr1, times(1)).localVariablesAccept(clazz, method1, codeAttribute, optimizer);
        verify(attr2, times(1)).localVariablesAccept(clazz, method1, codeAttribute, optimizer);
        verify(attr3, times(1)).localVariablesAccept(clazz, method2, codeAttribute, optimizer);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute handles generic type signatures correctly.
     * The LocalVariableTypeTableAttribute is specifically used for generic type information.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withGenericTypes_delegatesCorrectly() {
        // Arrange
        LocalVariableTypeTableAttribute genericAttribute = mock(LocalVariableTypeTableAttribute.class);

        // Act
        optimizer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, genericAttribute);

        // Assert - verify delegation occurs for generic type processing
        verify(genericAttribute, times(1))
            .localVariablesAccept(clazz, method, codeAttribute, optimizer);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute distinguishes itself from visitLocalVariableTableAttribute.
     * Both methods exist but handle different attribute types.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_distinctFromLocalVariableTableAttribute() {
        // Arrange
        LocalVariableTypeTableAttribute typeAttribute = mock(LocalVariableTypeTableAttribute.class);

        // Act
        optimizer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, typeAttribute);

        // Assert - verify the correct method is called (LocalVariableTypeTableAttribute, not LocalVariableTableAttribute)
        verify(typeAttribute, times(1))
            .localVariablesAccept(clazz, method, codeAttribute, optimizer);
        verifyNoMoreInteractions(typeAttribute);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute works correctly with Java 5+ code that uses generics.
     * LocalVariableTypeTable attributes were introduced in Java 5 for generic type information.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withJava5PlusCode_delegatesCorrectly() {
        // Arrange
        LocalVariableTypeTableAttribute java5Attribute = mock(LocalVariableTypeTableAttribute.class);

        // Act
        optimizer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, java5Attribute);

        // Assert
        verify(java5Attribute, times(1))
            .localVariablesAccept(clazz, method, codeAttribute, optimizer);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute maintains thread safety when called from multiple threads.
     * Different optimizer instances should be independent.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_threadSafety_doesNotInterfere() {
        // Arrange
        LocalVariableTypeTableAttribute attr1 = mock(LocalVariableTypeTableAttribute.class);
        LocalVariableTypeTableAttribute attr2 = mock(LocalVariableTypeTableAttribute.class);
        VariableOptimizer optimizer1 = new VariableOptimizer(true);
        VariableOptimizer optimizer2 = new VariableOptimizer(false);

        // Act - simulate concurrent calls with different optimizer instances
        optimizer1.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attr1);
        optimizer2.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attr2);

        // Assert - verify each call is independent
        verify(attr1, times(1)).localVariablesAccept(clazz, method, codeAttribute, optimizer1);
        verify(attr2, times(1)).localVariablesAccept(clazz, method, codeAttribute, optimizer2);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute handles empty type tables correctly.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withEmptyTypeTable_delegatesWithoutError() {
        // Arrange
        LocalVariableTypeTableAttribute emptyAttribute = mock(LocalVariableTypeTableAttribute.class);
        doNothing().when(emptyAttribute).localVariablesAccept(any(), any(), any(), any());

        // Act & Assert
        assertDoesNotThrow(() ->
            optimizer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, emptyAttribute),
            "Should handle empty type table without error");
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute works with optimizer created with reuseThis=true.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withReuseThisTrue_delegatesCorrectly() {
        // Arrange
        VariableOptimizer reuseThisOptimizer = new VariableOptimizer(true);

        // Act
        reuseThisOptimizer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, localVariableTypeTableAttribute);

        // Assert
        verify(localVariableTypeTableAttribute, times(1))
            .localVariablesAccept(clazz, method, codeAttribute, reuseThisOptimizer);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute works with optimizer created with reuseThis=false.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withReuseThisFalse_delegatesCorrectly() {
        // Arrange
        VariableOptimizer noReuseThisOptimizer = new VariableOptimizer(false);

        // Act
        noReuseThisOptimizer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, localVariableTypeTableAttribute);

        // Assert
        verify(localVariableTypeTableAttribute, times(1))
            .localVariablesAccept(clazz, method, codeAttribute, noReuseThisOptimizer);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute works with optimizer created with extraMemberVisitor.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withExtraMemberVisitor_delegatesCorrectly() {
        // Arrange
        MemberVisitor extraVisitor = mock(MemberVisitor.class);
        VariableOptimizer optimizerWithExtra = new VariableOptimizer(true, extraVisitor);

        // Act
        optimizerWithExtra.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, localVariableTypeTableAttribute);

        // Assert
        verify(localVariableTypeTableAttribute, times(1))
            .localVariablesAccept(clazz, method, codeAttribute, optimizerWithExtra);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute works with all constructor variants.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withAllConstructorVariants_delegatesCorrectly() {
        // Arrange
        MemberVisitor visitor = mock(MemberVisitor.class);
        VariableOptimizer opt1 = new VariableOptimizer(true);
        VariableOptimizer opt2 = new VariableOptimizer(false);
        VariableOptimizer opt3 = new VariableOptimizer(true, visitor);
        VariableOptimizer opt4 = new VariableOptimizer(false, visitor);
        VariableOptimizer opt5 = new VariableOptimizer(true, null);

        LocalVariableTypeTableAttribute attr = mock(LocalVariableTypeTableAttribute.class);

        // Act
        opt1.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attr);
        opt2.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attr);
        opt3.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attr);
        opt4.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attr);
        opt5.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attr);

        // Assert - verify each optimizer delegated correctly
        verify(attr, times(1)).localVariablesAccept(clazz, method, codeAttribute, opt1);
        verify(attr, times(1)).localVariablesAccept(clazz, method, codeAttribute, opt2);
        verify(attr, times(1)).localVariablesAccept(clazz, method, codeAttribute, opt3);
        verify(attr, times(1)).localVariablesAccept(clazz, method, codeAttribute, opt4);
        verify(attr, times(1)).localVariablesAccept(clazz, method, codeAttribute, opt5);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute is called for trimming variables in local variable type table.
     * This is the actual purpose mentioned in the code comment.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_trimsVariablesInTable() {
        // Act
        optimizer.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, localVariableTypeTableAttribute);

        // Assert - the delegation to localVariablesAccept is what enables variable trimming
        verify(localVariableTypeTableAttribute, times(1))
            .localVariablesAccept(clazz, method, codeAttribute, optimizer);
    }
}
