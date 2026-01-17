package proguard.backport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.BootstrapMethodsAttribute;
import proguard.classfile.attribute.visitor.BootstrapMethodInfoVisitor;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link LambdaExpressionCollector#visitBootstrapMethodsAttribute(Clazz, BootstrapMethodsAttribute)}.
 *
 * The visitBootstrapMethodsAttribute method delegates to the BootstrapMethodsAttribute to accept
 * a specific bootstrap method entry at the referencedBootstrapMethodIndex. This is a key step in
 * collecting lambda expression information from the bytecode.
 */
public class LambdaExpressionCollectorClaude_visitBootstrapMethodsAttributeTest {

    private LambdaExpressionCollector collector;
    private Map<Integer, LambdaExpression> lambdaExpressions;
    private Clazz clazz;
    private BootstrapMethodsAttribute bootstrapMethodsAttribute;

    @BeforeEach
    public void setUp() {
        lambdaExpressions = new HashMap<>();
        collector = new LambdaExpressionCollector(lambdaExpressions);
        clazz = mock(ProgramClass.class);
        bootstrapMethodsAttribute = mock(BootstrapMethodsAttribute.class);
    }

    /**
     * Tests that visitBootstrapMethodsAttribute calls bootstrapMethodEntryAccept on the attribute.
     * This is the primary behavior of this method - delegating to the attribute.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_callsBootstrapMethodEntryAccept() {
        // Arrange
        // The referencedBootstrapMethodIndex is set by visitInvokeDynamicConstant, but we can't
        // access it directly. We'll test that the method calls bootstrapMethodEntryAccept.

        // Act
        collector.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);

        // Assert - verify that bootstrapMethodEntryAccept was called
        // Note: The method should be called with the internal referencedBootstrapMethodIndex
        verify(bootstrapMethodsAttribute, times(1)).bootstrapMethodEntryAccept(
                eq(clazz),
                anyInt(),
                eq(collector)
        );
    }

    /**
     * Tests that visitBootstrapMethodsAttribute can be called with valid mock objects without throwing exceptions.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_withValidMocks_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> collector.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute));
    }

    /**
     * Tests that visitBootstrapMethodsAttribute can be called with null Clazz parameter.
     * The attribute's bootstrapMethodEntryAccept will receive the null, which may or may not be valid.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_withNullClazz_callsAttribute() {
        // Act
        collector.visitBootstrapMethodsAttribute(null, bootstrapMethodsAttribute);

        // Assert - verify that bootstrapMethodEntryAccept was called even with null clazz
        verify(bootstrapMethodsAttribute, times(1)).bootstrapMethodEntryAccept(
                isNull(),
                anyInt(),
                eq(collector)
        );
    }

    /**
     * Tests that visitBootstrapMethodsAttribute with null attribute throws NullPointerException.
     * Since the method calls a method on the attribute, null will cause an exception.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_withNullAttribute_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> collector.visitBootstrapMethodsAttribute(clazz, null));
    }

    /**
     * Tests that visitBootstrapMethodsAttribute can be called multiple times in succession.
     * Each call should delegate to the attribute's bootstrapMethodEntryAccept method.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_calledMultipleTimes_delegatesEachTime() {
        // Act
        collector.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);
        collector.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);
        collector.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);

        // Assert - verify that bootstrapMethodEntryAccept was called three times
        verify(bootstrapMethodsAttribute, times(3)).bootstrapMethodEntryAccept(
                eq(clazz),
                anyInt(),
                eq(collector)
        );
    }

    /**
     * Tests that visitBootstrapMethodsAttribute passes the collector as the visitor.
     * The collector implements BootstrapMethodInfoVisitor, so it should be passed as the visitor.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_passesCollectorAsVisitor() {
        // Act
        collector.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);

        // Assert - verify that the collector itself is passed as the visitor
        verify(bootstrapMethodsAttribute).bootstrapMethodEntryAccept(
                any(Clazz.class),
                anyInt(),
                argThat(visitor -> visitor == collector)
        );
    }

    /**
     * Tests that visitBootstrapMethodsAttribute works with different Clazz instances.
     * The method should work with any Clazz implementation.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_withDifferentClazzInstances_delegatesCorrectly() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Clazz clazz3 = mock(ProgramClass.class);

        // Act
        collector.visitBootstrapMethodsAttribute(clazz1, bootstrapMethodsAttribute);
        collector.visitBootstrapMethodsAttribute(clazz2, bootstrapMethodsAttribute);
        collector.visitBootstrapMethodsAttribute(clazz3, bootstrapMethodsAttribute);

        // Assert - verify that bootstrapMethodEntryAccept was called three times
        verify(bootstrapMethodsAttribute, times(3)).bootstrapMethodEntryAccept(
                any(Clazz.class),
                anyInt(),
                eq(collector)
        );
    }

    /**
     * Tests that visitBootstrapMethodsAttribute works with different BootstrapMethodsAttribute instances.
     * Each attribute should have its bootstrapMethodEntryAccept method called.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_withDifferentAttributes_delegatesCorrectly() {
        // Arrange
        BootstrapMethodsAttribute attr1 = mock(BootstrapMethodsAttribute.class);
        BootstrapMethodsAttribute attr2 = mock(BootstrapMethodsAttribute.class);
        BootstrapMethodsAttribute attr3 = mock(BootstrapMethodsAttribute.class);

        // Act
        collector.visitBootstrapMethodsAttribute(clazz, attr1);
        collector.visitBootstrapMethodsAttribute(clazz, attr2);
        collector.visitBootstrapMethodsAttribute(clazz, attr3);

        // Assert - verify each attribute had its method called exactly once
        verify(attr1, times(1)).bootstrapMethodEntryAccept(eq(clazz), anyInt(), eq(collector));
        verify(attr2, times(1)).bootstrapMethodEntryAccept(eq(clazz), anyInt(), eq(collector));
        verify(attr3, times(1)).bootstrapMethodEntryAccept(eq(clazz), anyInt(), eq(collector));
    }

    /**
     * Tests that visitBootstrapMethodsAttribute doesn't modify the lambdaExpressions map directly.
     * The method only delegates; any map modification happens in visitBootstrapMethodInfo.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_doesNotDirectlyModifyMap() {
        // Arrange
        LambdaExpression lambda = createSampleLambdaExpression();
        lambdaExpressions.put(0, lambda);
        int initialSize = lambdaExpressions.size();

        // Act
        collector.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);

        // Assert - verify the map was not directly modified
        // (Note: In real usage, the map may be modified via the callback to visitBootstrapMethodInfo,
        // but this test verifies that visitBootstrapMethodsAttribute itself doesn't modify it)
        assertEquals(initialSize, lambdaExpressions.size(), "Map size should not change directly");
        assertSame(lambda, lambdaExpressions.get(0), "Existing entry should not be modified");
    }

    /**
     * Tests that visitBootstrapMethodsAttribute works correctly with an empty map.
     * The method should work regardless of the map's contents.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_withEmptyMap_doesNotThrowException() {
        // Arrange
        LambdaExpressionCollector collectorWithEmptyMap = new LambdaExpressionCollector(new HashMap<>());

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> collectorWithEmptyMap.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute));
    }

    /**
     * Tests that visitBootstrapMethodsAttribute works correctly with a null map.
     * The method should work even if the internal map is null.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_withNullMap_doesNotThrowException() {
        // Arrange
        LambdaExpressionCollector collectorWithNullMap = new LambdaExpressionCollector(null);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> collectorWithNullMap.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute));
    }

    /**
     * Tests that visitBootstrapMethodsAttribute works correctly with a populated map.
     * The method should work regardless of the map's contents.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_withPopulatedMap_doesNotThrowException() {
        // Arrange
        Map<Integer, LambdaExpression> populatedMap = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            populatedMap.put(i, createSampleLambdaExpression());
        }
        LambdaExpressionCollector collectorWithPopulatedMap = new LambdaExpressionCollector(populatedMap);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() ->
                collectorWithPopulatedMap.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute));

        // Also verify the map wasn't directly modified
        assertEquals(5, populatedMap.size(), "Map size should remain unchanged");
    }

    /**
     * Tests that visitBootstrapMethodsAttribute execution completes quickly.
     * Since it's a simple delegation method, it should have minimal overhead.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_executesQuickly() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            collector.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100,
                "visitBootstrapMethodsAttribute should execute quickly as it's a simple delegation");
    }

    /**
     * Tests that multiple collectors can independently call visitBootstrapMethodsAttribute.
     * Each collector should maintain its own independent state.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_multipleCollectorsIndependent() {
        // Arrange
        Map<Integer, LambdaExpression> map1 = new HashMap<>();
        Map<Integer, LambdaExpression> map2 = new HashMap<>();
        map1.put(0, createSampleLambdaExpression());
        map2.put(1, createSampleLambdaExpression());

        LambdaExpressionCollector collector1 = new LambdaExpressionCollector(map1);
        LambdaExpressionCollector collector2 = new LambdaExpressionCollector(map2);

        BootstrapMethodsAttribute attr1 = mock(BootstrapMethodsAttribute.class);
        BootstrapMethodsAttribute attr2 = mock(BootstrapMethodsAttribute.class);

        // Act
        collector1.visitBootstrapMethodsAttribute(clazz, attr1);
        collector2.visitBootstrapMethodsAttribute(clazz, attr2);

        // Assert - verify each collector called its respective attribute
        verify(attr1, times(1)).bootstrapMethodEntryAccept(eq(clazz), anyInt(), eq(collector1));
        verify(attr2, times(1)).bootstrapMethodEntryAccept(eq(clazz), anyInt(), eq(collector2));

        // Verify maps remained independent
        assertEquals(1, map1.size(), "First map should remain unchanged");
        assertEquals(1, map2.size(), "Second map should remain unchanged");
    }

    /**
     * Tests that visitBootstrapMethodsAttribute doesn't affect subsequent operations.
     * Calling visitBootstrapMethodsAttribute should not interfere with other collector operations.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_doesNotAffectSubsequentOperations() {
        // Arrange
        int initialSize = lambdaExpressions.size();

        // Act - call visitBootstrapMethodsAttribute
        collector.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);

        // Add a lambda expression after calling visitBootstrapMethodsAttribute
        lambdaExpressions.put(5, createSampleLambdaExpression());

        // Assert - verify the map can still be modified normally
        assertEquals(initialSize + 1, lambdaExpressions.size(),
                "Map should be modifiable after visitBootstrapMethodsAttribute");
        assertTrue(lambdaExpressions.containsKey(5), "New entry should be added successfully");
    }

    /**
     * Tests that visitBootstrapMethodsAttribute can be called alternately with other visitor methods.
     * The methods should work independently without interfering with each other.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_alternatingWithOtherMethods_doesNotThrowException() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> {
            collector.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);
            collector.visitAnyClass(clazz);
            collector.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);
            collector.visitProgramClass(programClass);
            collector.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);
        });
    }

    /**
     * Tests that visitBootstrapMethodsAttribute passes the correct Clazz to bootstrapMethodEntryAccept.
     * The exact Clazz instance provided should be passed through.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_passesCorrectClazz() {
        // Arrange
        ProgramClass specificClass = mock(ProgramClass.class);

        // Act
        collector.visitBootstrapMethodsAttribute(specificClass, bootstrapMethodsAttribute);

        // Assert - verify the specific clazz was passed through
        verify(bootstrapMethodsAttribute).bootstrapMethodEntryAccept(
                same(specificClass),
                anyInt(),
                eq(collector)
        );
    }

    /**
     * Tests that visitBootstrapMethodsAttribute with same parameters repeatedly calls the attribute.
     * Each call should result in a delegation to the attribute.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_repeatedCallsWithSameParameters_delegatesEachTime() {
        // Act - call multiple times with same parameters
        for (int i = 0; i < 10; i++) {
            collector.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);
        }

        // Assert - verify bootstrapMethodEntryAccept was called 10 times
        verify(bootstrapMethodsAttribute, times(10)).bootstrapMethodEntryAccept(
                eq(clazz),
                anyInt(),
                eq(collector)
        );
    }

    /**
     * Tests that visitBootstrapMethodsAttribute works correctly in a typical lambda collection workflow.
     * This simulates how the method is typically called during lambda expression collection.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_inTypicalWorkflow_delegatesCorrectly() {
        // Arrange - Simulate a typical workflow
        ProgramClass programClass = new ProgramClass();

        // Act - Simulate typical workflow: visit class, then visit bootstrap methods attribute
        assertDoesNotThrow(() -> {
            collector.visitProgramClass(programClass);
            collector.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);
        });

        // Assert - verify the attribute method was called
        verify(bootstrapMethodsAttribute, times(1)).bootstrapMethodEntryAccept(
                eq(clazz),
                anyInt(),
                eq(collector)
        );
    }

    /**
     * Tests that visitBootstrapMethodsAttribute passes the collector (which implements BootstrapMethodInfoVisitor).
     * The collector should be passed as an instance of BootstrapMethodInfoVisitor.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_passesCollectorAsBootstrapMethodInfoVisitor() {
        // Act
        collector.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);

        // Assert - verify that a BootstrapMethodInfoVisitor was passed (which is the collector)
        verify(bootstrapMethodsAttribute).bootstrapMethodEntryAccept(
                any(Clazz.class),
                anyInt(),
                any(BootstrapMethodInfoVisitor.class)
        );
    }

    /**
     * Tests that visitBootstrapMethodsAttribute with different combinations of parameters works correctly.
     * Various combinations should all result in proper delegation.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_withVariousParameterCombinations() {
        // Arrange
        ProgramClass programClass1 = mock(ProgramClass.class);
        ProgramClass programClass2 = mock(ProgramClass.class);
        BootstrapMethodsAttribute attr1 = mock(BootstrapMethodsAttribute.class);
        BootstrapMethodsAttribute attr2 = mock(BootstrapMethodsAttribute.class);

        // Act & Assert - should not throw any exception with various combinations
        assertDoesNotThrow(() -> {
            collector.visitBootstrapMethodsAttribute(programClass1, attr1);
            collector.visitBootstrapMethodsAttribute(programClass2, attr1);
            collector.visitBootstrapMethodsAttribute(programClass1, attr2);
            collector.visitBootstrapMethodsAttribute(programClass2, attr2);
        });

        // Verify all calls were made
        verify(attr1, times(2)).bootstrapMethodEntryAccept(any(Clazz.class), anyInt(), eq(collector));
        verify(attr2, times(2)).bootstrapMethodEntryAccept(any(Clazz.class), anyInt(), eq(collector));
    }

    /**
     * Tests that visitBootstrapMethodsAttribute maintains correct behavior across many operations.
     * The collector should remain in a valid state after many operations.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_afterManyOperations_stillWorksCorrectly() {
        // Act - perform many operations
        for (int i = 0; i < 100; i++) {
            collector.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);
        }

        // Assert - final call should still work correctly
        assertDoesNotThrow(() -> collector.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute));

        // Verify the total number of calls
        verify(bootstrapMethodsAttribute, times(101)).bootstrapMethodEntryAccept(
                eq(clazz),
                anyInt(),
                eq(collector)
        );
    }

    /**
     * Tests visitBootstrapMethodsAttribute returns normally (no return value to verify).
     * Verifies the method signature and behavior (void return type).
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_returnsNormally() {
        // Act - method has void return type, just verify it completes
        collector.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);

        // Assert - if we reach here, the method completed normally
        verify(bootstrapMethodsAttribute, times(1)).bootstrapMethodEntryAccept(
                any(Clazz.class),
                anyInt(),
                eq(collector)
        );
    }

    /**
     * Tests that visitBootstrapMethodsAttribute with sequential calls works correctly.
     * Sequential calls should all succeed and delegate properly.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_sequentialCalls() {
        // Act - make sequential calls
        collector.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);
        collector.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);
        collector.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);

        // Assert - all calls should have been delegated
        verify(bootstrapMethodsAttribute, times(3)).bootstrapMethodEntryAccept(
                eq(clazz),
                anyInt(),
                eq(collector)
        );
    }

    /**
     * Tests that visitBootstrapMethodsAttribute works correctly when the attribute throws an exception.
     * The exception should propagate through without being caught.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_whenAttributeThrowsException_propagatesException() {
        // Arrange
        doThrow(new RuntimeException("Test exception"))
                .when(bootstrapMethodsAttribute)
                .bootstrapMethodEntryAccept(any(), anyInt(), any());

        // Act & Assert - exception should propagate
        assertThrows(RuntimeException.class,
                () -> collector.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute));
    }

    /**
     * Tests that visitBootstrapMethodsAttribute uses the referencedBootstrapMethodIndex.
     * Although we can't directly verify the index value, we can verify it's passed to the attribute.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_usesReferencedBootstrapMethodIndex() {
        // Act
        collector.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);

        // Assert - verify that an integer index was passed (we can't verify the exact value
        // without reflection, but we can verify the method was called with the correct parameters)
        verify(bootstrapMethodsAttribute).bootstrapMethodEntryAccept(
                eq(clazz),
                anyInt(), // This is the referencedBootstrapMethodIndex
                eq(collector)
        );
    }

    /**
     * Tests that visitBootstrapMethodsAttribute can handle rapid alternating calls.
     * Rapid calls with different attributes should all work correctly.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_rapidAlternatingCalls() {
        // Arrange
        BootstrapMethodsAttribute attr1 = mock(BootstrapMethodsAttribute.class);
        BootstrapMethodsAttribute attr2 = mock(BootstrapMethodsAttribute.class);

        // Act - rapid alternating calls
        for (int i = 0; i < 50; i++) {
            collector.visitBootstrapMethodsAttribute(clazz, attr1);
            collector.visitBootstrapMethodsAttribute(clazz, attr2);
        }

        // Assert - verify each attribute was called 50 times
        verify(attr1, times(50)).bootstrapMethodEntryAccept(eq(clazz), anyInt(), eq(collector));
        verify(attr2, times(50)).bootstrapMethodEntryAccept(eq(clazz), anyInt(), eq(collector));
    }

    /**
     * Helper method to create a sample LambdaExpression for testing.
     */
    private LambdaExpression createSampleLambdaExpression() {
        return new LambdaExpression(
                null, // referencedClass
                0,    // bootstrapMethodIndex
                null, // bootstrapMethodInfo
                "()Ljava/util/function/Supplier;", // factoryMethodDescriptor
                new String[]{"java/util/function/Supplier"}, // interfaces
                new String[0], // bridgeMethodDescriptors
                "get", // interfaceMethod
                "()Ljava/lang/Object;", // interfaceMethodDescriptor
                6,     // invokedReferenceKind
                "TestClass", // invokedClassName
                "lambda$main$0", // invokedMethodName
                "()Ljava/lang/String;", // invokedMethodDesc
                null,  // referencedInvokedClass
                null   // referencedInvokedMethod
        );
    }
}
