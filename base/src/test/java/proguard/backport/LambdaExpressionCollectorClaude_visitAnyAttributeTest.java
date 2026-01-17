package proguard.backport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.Attribute;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link LambdaExpressionCollector#visitAnyAttribute(Clazz, Attribute)}.
 *
 * The visitAnyAttribute method is an empty implementation (no-op) that serves as a default
 * handler in the AttributeVisitor pattern for attributes that don't require specialized processing.
 */
public class LambdaExpressionCollectorClaude_visitAnyAttributeTest {

    private LambdaExpressionCollector collector;
    private Map<Integer, LambdaExpression> lambdaExpressions;
    private Clazz clazz;
    private Attribute attribute;

    @BeforeEach
    public void setUp() {
        lambdaExpressions = new HashMap<>();
        collector = new LambdaExpressionCollector(lambdaExpressions);
        clazz = mock(ProgramClass.class);
        attribute = mock(Attribute.class);
    }

    /**
     * Tests that visitAnyAttribute can be called with valid mock objects without throwing exceptions.
     * Since this is a no-op method, it should simply do nothing and complete successfully.
     */
    @Test
    public void testVisitAnyAttribute_withValidMocks_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> collector.visitAnyAttribute(clazz, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called with null Clazz parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyAttribute_withNullClazz_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> collector.visitAnyAttribute(null, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called with null Attribute parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyAttribute_withNullAttribute_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> collector.visitAnyAttribute(clazz, null));
    }

    /**
     * Tests that visitAnyAttribute can be called with both parameters null.
     * The method should handle null parameters gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyAttribute_withBothParametersNull_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> collector.visitAnyAttribute(null, null));
    }

    /**
     * Tests that visitAnyAttribute can be called multiple times in succession.
     * The method should be idempotent and handle repeated calls without issues.
     */
    @Test
    public void testVisitAnyAttribute_calledMultipleTimes_doesNotThrowException() {
        // Act & Assert - should not throw any exception on multiple calls
        assertDoesNotThrow(() -> {
            collector.visitAnyAttribute(clazz, attribute);
            collector.visitAnyAttribute(clazz, attribute);
            collector.visitAnyAttribute(clazz, attribute);
        });
    }

    /**
     * Tests that visitAnyAttribute doesn't interact with the Clazz parameter.
     * Since it's a no-op method, it should not call any methods on the clazz.
     */
    @Test
    public void testVisitAnyAttribute_doesNotInteractWithClazz() {
        // Act
        collector.visitAnyAttribute(clazz, attribute);

        // Assert - verify no interactions occurred with the clazz mock
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitAnyAttribute doesn't interact with the Attribute parameter.
     * Since it's a no-op method, it should not call any methods on the attribute.
     */
    @Test
    public void testVisitAnyAttribute_doesNotInteractWithAttribute() {
        // Act
        collector.visitAnyAttribute(clazz, attribute);

        // Assert - verify no interactions occurred with the attribute mock
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute doesn't modify the lambdaExpressions map.
     * Since it's a no-op method, it should not add, remove, or modify entries in the map.
     */
    @Test
    public void testVisitAnyAttribute_doesNotModifyLambdaExpressionsMap() {
        // Arrange
        LambdaExpression lambda = createSampleLambdaExpression();
        lambdaExpressions.put(0, lambda);
        int initialSize = lambdaExpressions.size();

        // Act
        collector.visitAnyAttribute(clazz, attribute);

        // Assert - verify the map was not modified
        assertEquals(initialSize, lambdaExpressions.size(), "Map size should not change");
        assertSame(lambda, lambdaExpressions.get(0), "Existing entry should not be modified");
    }

    /**
     * Tests that visitAnyAttribute works with different Clazz mock instances.
     * The method should handle any Clazz implementation without issues.
     */
    @Test
    public void testVisitAnyAttribute_withDifferentClazzInstances_doesNotThrowException() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Clazz clazz3 = mock(ProgramClass.class);

        // Act & Assert - should not throw any exception with different instances
        assertDoesNotThrow(() -> {
            collector.visitAnyAttribute(clazz1, attribute);
            collector.visitAnyAttribute(clazz2, attribute);
            collector.visitAnyAttribute(clazz3, attribute);
        });
    }

    /**
     * Tests that visitAnyAttribute works with different Attribute mock instances.
     * The method should handle any Attribute implementation without issues.
     */
    @Test
    public void testVisitAnyAttribute_withDifferentAttributeInstances_doesNotThrowException() {
        // Arrange
        Attribute attr1 = mock(Attribute.class);
        Attribute attr2 = mock(Attribute.class);
        Attribute attr3 = mock(Attribute.class);

        // Act & Assert - should not throw any exception with different instances
        assertDoesNotThrow(() -> {
            collector.visitAnyAttribute(clazz, attr1);
            collector.visitAnyAttribute(clazz, attr2);
            collector.visitAnyAttribute(clazz, attr3);
        });
    }

    /**
     * Tests that visitAnyAttribute doesn't affect the collector's internal state.
     * Calling the method should not change any fields or trigger any side effects.
     */
    @Test
    public void testVisitAnyAttribute_doesNotModifyCollectorState() {
        // Arrange
        lambdaExpressions.put(1, createSampleLambdaExpression());
        Map<Integer, LambdaExpression> snapshotBefore = new HashMap<>(lambdaExpressions);

        // Act
        collector.visitAnyAttribute(clazz, attribute);

        // Assert - verify the map state hasn't changed
        assertEquals(snapshotBefore.size(), lambdaExpressions.size(), "Map size should remain the same");
        assertEquals(snapshotBefore, lambdaExpressions, "Map contents should remain the same");
    }

    /**
     * Tests that visitAnyAttribute can be called on a collector with an empty map.
     * The method should work even with an empty lambdaExpressions map.
     */
    @Test
    public void testVisitAnyAttribute_withEmptyMap_doesNotThrowException() {
        // Arrange
        LambdaExpressionCollector collectorWithEmptyMap = new LambdaExpressionCollector(new HashMap<>());

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> collectorWithEmptyMap.visitAnyAttribute(clazz, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called on a collector with a null map.
     * The method should work even if the internal map is null.
     */
    @Test
    public void testVisitAnyAttribute_withNullMap_doesNotThrowException() {
        // Arrange
        LambdaExpressionCollector collectorWithNullMap = new LambdaExpressionCollector(null);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> collectorWithNullMap.visitAnyAttribute(clazz, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called on a collector with a populated map.
     * The method should work regardless of the map's contents.
     */
    @Test
    public void testVisitAnyAttribute_withPopulatedMap_doesNotThrowException() {
        // Arrange
        Map<Integer, LambdaExpression> populatedMap = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            populatedMap.put(i, createSampleLambdaExpression());
        }
        LambdaExpressionCollector collectorWithPopulatedMap = new LambdaExpressionCollector(populatedMap);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> collectorWithPopulatedMap.visitAnyAttribute(clazz, attribute));

        // Also verify the map wasn't modified
        assertEquals(5, populatedMap.size(), "Map size should remain unchanged");
    }

    /**
     * Tests that visitAnyAttribute execution completes immediately.
     * Since it's a no-op method, it should have minimal overhead.
     */
    @Test
    public void testVisitAnyAttribute_executesQuickly() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            collector.visitAnyAttribute(clazz, attribute);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100, "visitAnyAttribute should execute quickly as it's a no-op");
    }

    /**
     * Tests that multiple collectors can independently call visitAnyAttribute.
     * Each collector should maintain its own independent state.
     */
    @Test
    public void testVisitAnyAttribute_multipleCollectorsIndependent() {
        // Arrange
        Map<Integer, LambdaExpression> map1 = new HashMap<>();
        Map<Integer, LambdaExpression> map2 = new HashMap<>();
        map1.put(0, createSampleLambdaExpression());
        map2.put(1, createSampleLambdaExpression());

        LambdaExpressionCollector collector1 = new LambdaExpressionCollector(map1);
        LambdaExpressionCollector collector2 = new LambdaExpressionCollector(map2);

        // Act
        collector1.visitAnyAttribute(clazz, attribute);
        collector2.visitAnyAttribute(clazz, attribute);

        // Assert - verify each map remained independent and unchanged
        assertEquals(1, map1.size(), "First map should remain unchanged");
        assertEquals(1, map2.size(), "Second map should remain unchanged");
        assertTrue(map1.containsKey(0), "First map should still have key 0");
        assertTrue(map2.containsKey(1), "Second map should still have key 1");
    }

    /**
     * Tests that visitAnyAttribute with mixed null and valid calls works correctly.
     * The method should handle mixed null and non-null calls without issues.
     */
    @Test
    public void testVisitAnyAttribute_mixedNullAndValidCalls_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> {
            collector.visitAnyAttribute(null, null);
            collector.visitAnyAttribute(clazz, attribute);
            collector.visitAnyAttribute(null, attribute);
            collector.visitAnyAttribute(clazz, null);
        });
    }

    /**
     * Tests that visitAnyAttribute doesn't affect subsequent operations.
     * Calling visitAnyAttribute should not interfere with other collector methods.
     */
    @Test
    public void testVisitAnyAttribute_doesNotAffectSubsequentOperations() {
        // Arrange
        int initialSize = lambdaExpressions.size();

        // Act - call visitAnyAttribute
        collector.visitAnyAttribute(clazz, attribute);

        // Add a lambda expression after calling visitAnyAttribute
        lambdaExpressions.put(5, createSampleLambdaExpression());

        // Assert - verify the map can still be modified normally
        assertEquals(initialSize + 1, lambdaExpressions.size(), "Map should be modifiable after visitAnyAttribute");
        assertTrue(lambdaExpressions.containsKey(5), "New entry should be added successfully");
    }

    /**
     * Tests that visitAnyAttribute can be called with different Clazz instances and same Attribute.
     * The method should handle any combination of parameters.
     */
    @Test
    public void testVisitAnyAttribute_withDifferentClazzInstancesSameAttribute_doesNotThrowException() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Clazz clazz3 = mock(ProgramClass.class);

        // Act & Assert - should not throw any exception with different clazz instances
        assertDoesNotThrow(() -> {
            collector.visitAnyAttribute(clazz1, attribute);
            collector.visitAnyAttribute(clazz2, attribute);
            collector.visitAnyAttribute(clazz3, attribute);
        });
    }

    /**
     * Tests that visitAnyAttribute can be called with same Clazz and different Attributes.
     * The method should handle any combination of parameters.
     */
    @Test
    public void testVisitAnyAttribute_withSameClazzDifferentAttributes_doesNotThrowException() {
        // Arrange
        Attribute attr1 = mock(Attribute.class);
        Attribute attr2 = mock(Attribute.class);
        Attribute attr3 = mock(Attribute.class);

        // Act & Assert - should not throw any exception with different attribute instances
        assertDoesNotThrow(() -> {
            collector.visitAnyAttribute(clazz, attr1);
            collector.visitAnyAttribute(clazz, attr2);
            collector.visitAnyAttribute(clazz, attr3);
        });
    }

    /**
     * Tests that visitAnyAttribute can be called alternately with other visitor methods.
     * The methods should work independently without interfering with each other.
     */
    @Test
    public void testVisitAnyAttribute_alternatingWithOtherMethods_doesNotThrowException() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> {
            collector.visitAnyAttribute(clazz, attribute);
            collector.visitAnyClass(clazz);
            collector.visitAnyAttribute(clazz, attribute);
            collector.visitProgramClass(programClass);
            collector.visitAnyAttribute(clazz, attribute);
        });
    }

    /**
     * Tests that visitAnyAttribute doesn't interact with either mock parameter.
     * Since it's a no-op method, it should not call any methods on either parameter.
     */
    @Test
    public void testVisitAnyAttribute_doesNotInteractWithEitherParameter() {
        // Act
        collector.visitAnyAttribute(clazz, attribute);

        // Assert - verify no interactions occurred with either mock
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute with various configured attribute mocks doesn't throw exceptions.
     * This ensures the no-op works with attributes that have stubbed methods.
     */
    @Test
    public void testVisitAnyAttribute_withConfiguredAttributeMocks_doesNotThrowException() {
        // Arrange - test with various attribute types with stubbed methods
        Attribute attr1 = mock(Attribute.class);
        Attribute attr2 = mock(Attribute.class);
        Attribute attr3 = mock(Attribute.class);

        when(attr1.getAttributeName(any())).thenReturn("CustomAttribute1");
        when(attr2.getAttributeName(any())).thenReturn("CustomAttribute2");
        when(attr3.getAttributeName(any())).thenReturn("CustomAttribute3");

        // Act & Assert - should handle all attribute types gracefully
        assertDoesNotThrow(() -> {
            collector.visitAnyAttribute(clazz, attr1);
            collector.visitAnyAttribute(clazz, attr2);
            collector.visitAnyAttribute(clazz, attr3);
        });

        // Verify that the attribute methods were not called since it's a no-op
        verify(attr1, never()).getAttributeName(any());
        verify(attr2, never()).getAttributeName(any());
        verify(attr3, never()).getAttributeName(any());
    }

    /**
     * Tests that visitAnyAttribute can be called with the same parameters repeatedly
     * without accumulating any state or causing issues.
     */
    @Test
    public void testVisitAnyAttribute_repeatedCallsWithSameParameters_noStateAccumulation() {
        // Act - call multiple times with same parameters
        for (int i = 0; i < 10; i++) {
            collector.visitAnyAttribute(clazz, attribute);
        }

        // Assert - verify no interactions occurred despite multiple calls
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
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
