package proguard.backport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link LambdaExpressionCollector#visitAnyClass(Clazz)}.
 *
 * The visitAnyClass method is an empty implementation (no-op) that serves as a default
 * handler in the ClassVisitor pattern for classes that don't require specialized processing.
 */
public class LambdaExpressionCollectorClaude_visitAnyClassTest {

    private LambdaExpressionCollector collector;
    private Map<Integer, LambdaExpression> lambdaExpressions;
    private Clazz clazz;

    @BeforeEach
    public void setUp() {
        lambdaExpressions = new HashMap<>();
        collector = new LambdaExpressionCollector(lambdaExpressions);
        clazz = mock(ProgramClass.class);
    }

    /**
     * Tests that visitAnyClass can be called with a valid mock object without throwing exceptions.
     * Since this is a no-op method, it should simply do nothing and complete successfully.
     */
    @Test
    public void testVisitAnyClass_withValidMock_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> collector.visitAnyClass(clazz));
    }

    /**
     * Tests that visitAnyClass can be called with null Clazz parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyClass_withNullClazz_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> collector.visitAnyClass(null));
    }

    /**
     * Tests that visitAnyClass can be called multiple times in succession.
     * The method should be idempotent and handle repeated calls without issues.
     */
    @Test
    public void testVisitAnyClass_calledMultipleTimes_doesNotThrowException() {
        // Act & Assert - should not throw any exception on multiple calls
        assertDoesNotThrow(() -> {
            collector.visitAnyClass(clazz);
            collector.visitAnyClass(clazz);
            collector.visitAnyClass(clazz);
        });
    }

    /**
     * Tests that visitAnyClass doesn't interact with the Clazz parameter.
     * Since it's a no-op method, it should not call any methods on the clazz.
     */
    @Test
    public void testVisitAnyClass_doesNotInteractWithClazz() {
        // Act
        collector.visitAnyClass(clazz);

        // Assert - verify no interactions occurred with the clazz mock
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitAnyClass doesn't modify the lambdaExpressions map.
     * Since it's a no-op method, it should not add, remove, or modify entries in the map.
     */
    @Test
    public void testVisitAnyClass_doesNotModifyLambdaExpressionsMap() {
        // Arrange
        LambdaExpression lambda = createSampleLambdaExpression();
        lambdaExpressions.put(0, lambda);
        int initialSize = lambdaExpressions.size();

        // Act
        collector.visitAnyClass(clazz);

        // Assert - verify the map was not modified
        assertEquals(initialSize, lambdaExpressions.size(), "Map size should not change");
        assertSame(lambda, lambdaExpressions.get(0), "Existing entry should not be modified");
    }

    /**
     * Tests that visitAnyClass works with different Clazz mock instances.
     * The method should handle any Clazz implementation without issues.
     */
    @Test
    public void testVisitAnyClass_withDifferentClazzInstances_doesNotThrowException() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Clazz clazz3 = mock(ProgramClass.class);

        // Act & Assert - should not throw any exception with different instances
        assertDoesNotThrow(() -> {
            collector.visitAnyClass(clazz1);
            collector.visitAnyClass(clazz2);
            collector.visitAnyClass(clazz3);
        });
    }

    /**
     * Tests that visitAnyClass doesn't affect the collector's internal state.
     * Calling the method should not change any fields or trigger any side effects.
     */
    @Test
    public void testVisitAnyClass_doesNotModifyCollectorState() {
        // Arrange
        lambdaExpressions.put(1, createSampleLambdaExpression());
        Map<Integer, LambdaExpression> snapshotBefore = new HashMap<>(lambdaExpressions);

        // Act
        collector.visitAnyClass(clazz);

        // Assert - verify the map state hasn't changed
        assertEquals(snapshotBefore.size(), lambdaExpressions.size(), "Map size should remain the same");
        assertEquals(snapshotBefore, lambdaExpressions, "Map contents should remain the same");
    }

    /**
     * Tests that visitAnyClass can be called on a collector with an empty map.
     * The method should work even with an empty lambdaExpressions map.
     */
    @Test
    public void testVisitAnyClass_withEmptyMap_doesNotThrowException() {
        // Arrange
        LambdaExpressionCollector collectorWithEmptyMap = new LambdaExpressionCollector(new HashMap<>());

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> collectorWithEmptyMap.visitAnyClass(clazz));
    }

    /**
     * Tests that visitAnyClass can be called on a collector with a null map.
     * The method should work even if the internal map is null.
     */
    @Test
    public void testVisitAnyClass_withNullMap_doesNotThrowException() {
        // Arrange
        LambdaExpressionCollector collectorWithNullMap = new LambdaExpressionCollector(null);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> collectorWithNullMap.visitAnyClass(clazz));
    }

    /**
     * Tests that visitAnyClass can be called on a collector with a populated map.
     * The method should work regardless of the map's contents.
     */
    @Test
    public void testVisitAnyClass_withPopulatedMap_doesNotThrowException() {
        // Arrange
        Map<Integer, LambdaExpression> populatedMap = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            populatedMap.put(i, createSampleLambdaExpression());
        }
        LambdaExpressionCollector collectorWithPopulatedMap = new LambdaExpressionCollector(populatedMap);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> collectorWithPopulatedMap.visitAnyClass(clazz));

        // Also verify the map wasn't modified
        assertEquals(5, populatedMap.size(), "Map size should remain unchanged");
    }

    /**
     * Tests that visitAnyClass execution completes immediately.
     * Since it's a no-op method, it should have minimal overhead.
     */
    @Test
    public void testVisitAnyClass_executesQuickly() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            collector.visitAnyClass(clazz);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100, "visitAnyClass should execute quickly as it's a no-op");
    }

    /**
     * Tests that multiple collectors can independently call visitAnyClass.
     * Each collector should maintain its own independent state.
     */
    @Test
    public void testVisitAnyClass_multipleCollectorsIndependent() {
        // Arrange
        Map<Integer, LambdaExpression> map1 = new HashMap<>();
        Map<Integer, LambdaExpression> map2 = new HashMap<>();
        map1.put(0, createSampleLambdaExpression());
        map2.put(1, createSampleLambdaExpression());

        LambdaExpressionCollector collector1 = new LambdaExpressionCollector(map1);
        LambdaExpressionCollector collector2 = new LambdaExpressionCollector(map2);

        // Act
        collector1.visitAnyClass(clazz);
        collector2.visitAnyClass(clazz);

        // Assert - verify each map remained independent and unchanged
        assertEquals(1, map1.size(), "First map should remain unchanged");
        assertEquals(1, map2.size(), "Second map should remain unchanged");
        assertTrue(map1.containsKey(0), "First map should still have key 0");
        assertTrue(map2.containsKey(1), "Second map should still have key 1");
    }

    /**
     * Tests that visitAnyClass with null followed by valid clazz works correctly.
     * The method should handle mixed null and non-null calls without issues.
     */
    @Test
    public void testVisitAnyClass_mixedNullAndValidCalls_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> {
            collector.visitAnyClass(null);
            collector.visitAnyClass(clazz);
            collector.visitAnyClass(null);
            collector.visitAnyClass(clazz);
        });
    }

    /**
     * Tests that visitAnyClass doesn't affect subsequent operations.
     * Calling visitAnyClass should not interfere with other collector methods.
     */
    @Test
    public void testVisitAnyClass_doesNotAffectSubsequentOperations() {
        // Arrange
        int initialSize = lambdaExpressions.size();

        // Act - call visitAnyClass
        collector.visitAnyClass(clazz);

        // Add a lambda expression after calling visitAnyClass
        lambdaExpressions.put(5, createSampleLambdaExpression());

        // Assert - verify the map can still be modified normally
        assertEquals(initialSize + 1, lambdaExpressions.size(), "Map should be modifiable after visitAnyClass");
        assertTrue(lambdaExpressions.containsKey(5), "New entry should be added successfully");
    }

    /**
     * Tests that visitAnyClass can be called alternately with visitProgramClass.
     * The methods should work independently without interfering with each other.
     */
    @Test
    public void testVisitAnyClass_alternatingWithVisitProgramClass_doesNotThrowException() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> {
            collector.visitAnyClass(clazz);
            collector.visitProgramClass(programClass);
            collector.visitAnyClass(clazz);
        });
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
