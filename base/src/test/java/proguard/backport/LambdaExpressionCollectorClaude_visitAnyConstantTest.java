package proguard.backport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.constant.*;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link LambdaExpressionCollector#visitAnyConstant(Clazz, Constant)}.
 *
 * The visitAnyConstant method is an empty implementation (no-op) that serves as a default
 * handler in the ConstantVisitor pattern for constants that don't require specialized processing.
 */
public class LambdaExpressionCollectorClaude_visitAnyConstantTest {

    private LambdaExpressionCollector collector;
    private Map<Integer, LambdaExpression> lambdaExpressions;
    private Clazz clazz;
    private Constant constant;

    @BeforeEach
    public void setUp() {
        lambdaExpressions = new HashMap<>();
        collector = new LambdaExpressionCollector(lambdaExpressions);
        clazz = mock(ProgramClass.class);
        constant = mock(Constant.class);
    }

    /**
     * Tests that visitAnyConstant can be called with valid mock objects without throwing exceptions.
     * Since this is a no-op method, it should simply do nothing and complete successfully.
     */
    @Test
    public void testVisitAnyConstant_withValidMocks_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> collector.visitAnyConstant(clazz, constant));
    }

    /**
     * Tests that visitAnyConstant can be called with null Clazz parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyConstant_withNullClazz_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> collector.visitAnyConstant(null, constant));
    }

    /**
     * Tests that visitAnyConstant can be called with null Constant parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyConstant_withNullConstant_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> collector.visitAnyConstant(clazz, null));
    }

    /**
     * Tests that visitAnyConstant can be called with both parameters null.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyConstant_withBothParametersNull_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> collector.visitAnyConstant(null, null));
    }

    /**
     * Tests that visitAnyConstant can be called multiple times in succession.
     * The method should be idempotent and handle repeated calls without issues.
     */
    @Test
    public void testVisitAnyConstant_calledMultipleTimes_doesNotThrowException() {
        // Act & Assert - should not throw any exception on multiple calls
        assertDoesNotThrow(() -> {
            collector.visitAnyConstant(clazz, constant);
            collector.visitAnyConstant(clazz, constant);
            collector.visitAnyConstant(clazz, constant);
        });
    }

    /**
     * Tests that visitAnyConstant doesn't interact with the Clazz parameter.
     * Since it's a no-op method, it should not call any methods on the clazz.
     */
    @Test
    public void testVisitAnyConstant_doesNotInteractWithClazz() {
        // Act
        collector.visitAnyConstant(clazz, constant);

        // Assert - verify no interactions occurred with the clazz mock
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitAnyConstant doesn't interact with the Constant parameter.
     * Since it's a no-op method, it should not call any methods on the constant.
     */
    @Test
    public void testVisitAnyConstant_doesNotInteractWithConstant() {
        // Act
        collector.visitAnyConstant(clazz, constant);

        // Assert - verify no interactions occurred with the constant mock
        verifyNoInteractions(constant);
    }

    /**
     * Tests that visitAnyConstant doesn't modify the lambdaExpressions map.
     * Since it's a no-op method, it should not add, remove, or modify entries in the map.
     */
    @Test
    public void testVisitAnyConstant_doesNotModifyLambdaExpressionsMap() {
        // Arrange
        LambdaExpression lambda = createSampleLambdaExpression();
        lambdaExpressions.put(0, lambda);
        int initialSize = lambdaExpressions.size();

        // Act
        collector.visitAnyConstant(clazz, constant);

        // Assert - verify the map was not modified
        assertEquals(initialSize, lambdaExpressions.size(), "Map size should not change");
        assertSame(lambda, lambdaExpressions.get(0), "Existing entry should not be modified");
    }

    /**
     * Tests that visitAnyConstant works with different Constant type implementations.
     * The method should handle any Constant implementation without issues.
     */
    @Test
    public void testVisitAnyConstant_withDifferentConstantTypes_doesNotThrowException() {
        // Arrange
        IntegerConstant intConstant = mock(IntegerConstant.class);
        StringConstant stringConstant = mock(StringConstant.class);
        ClassConstant classConstant = mock(ClassConstant.class);
        Utf8Constant utf8Constant = mock(Utf8Constant.class);

        // Act & Assert - should not throw any exception with different constant types
        assertDoesNotThrow(() -> {
            collector.visitAnyConstant(clazz, intConstant);
            collector.visitAnyConstant(clazz, stringConstant);
            collector.visitAnyConstant(clazz, classConstant);
            collector.visitAnyConstant(clazz, utf8Constant);
        });
    }

    /**
     * Tests that visitAnyConstant doesn't affect the collector's internal state.
     * Calling the method should not change any fields or trigger any side effects.
     */
    @Test
    public void testVisitAnyConstant_doesNotModifyCollectorState() {
        // Arrange
        lambdaExpressions.put(1, createSampleLambdaExpression());
        Map<Integer, LambdaExpression> snapshotBefore = new HashMap<>(lambdaExpressions);

        // Act
        collector.visitAnyConstant(clazz, constant);

        // Assert - verify the map state hasn't changed
        assertEquals(snapshotBefore.size(), lambdaExpressions.size(), "Map size should remain the same");
        assertEquals(snapshotBefore, lambdaExpressions, "Map contents should remain the same");
    }

    /**
     * Tests that visitAnyConstant can be called on a collector with an empty map.
     * The method should work even with an empty lambdaExpressions map.
     */
    @Test
    public void testVisitAnyConstant_withEmptyMap_doesNotThrowException() {
        // Arrange
        LambdaExpressionCollector collectorWithEmptyMap = new LambdaExpressionCollector(new HashMap<>());

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> collectorWithEmptyMap.visitAnyConstant(clazz, constant));
    }

    /**
     * Tests that visitAnyConstant can be called on a collector with a null map.
     * The method should work even if the internal map is null.
     */
    @Test
    public void testVisitAnyConstant_withNullMap_doesNotThrowException() {
        // Arrange
        LambdaExpressionCollector collectorWithNullMap = new LambdaExpressionCollector(null);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> collectorWithNullMap.visitAnyConstant(clazz, constant));
    }

    /**
     * Tests that visitAnyConstant can be called on a collector with a populated map.
     * The method should work regardless of the map's contents.
     */
    @Test
    public void testVisitAnyConstant_withPopulatedMap_doesNotThrowException() {
        // Arrange
        Map<Integer, LambdaExpression> populatedMap = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            populatedMap.put(i, createSampleLambdaExpression());
        }
        LambdaExpressionCollector collectorWithPopulatedMap = new LambdaExpressionCollector(populatedMap);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> collectorWithPopulatedMap.visitAnyConstant(clazz, constant));

        // Also verify the map wasn't modified
        assertEquals(5, populatedMap.size(), "Map size should remain unchanged");
    }

    /**
     * Tests that visitAnyConstant execution completes immediately.
     * Since it's a no-op method, it should have minimal overhead.
     */
    @Test
    public void testVisitAnyConstant_executesQuickly() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            collector.visitAnyConstant(clazz, constant);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100, "visitAnyConstant should execute quickly as it's a no-op");
    }

    /**
     * Tests that multiple collectors can independently call visitAnyConstant.
     * Each collector should maintain its own independent state.
     */
    @Test
    public void testVisitAnyConstant_multipleCollectorsIndependent() {
        // Arrange
        Map<Integer, LambdaExpression> map1 = new HashMap<>();
        Map<Integer, LambdaExpression> map2 = new HashMap<>();
        map1.put(0, createSampleLambdaExpression());
        map2.put(1, createSampleLambdaExpression());

        LambdaExpressionCollector collector1 = new LambdaExpressionCollector(map1);
        LambdaExpressionCollector collector2 = new LambdaExpressionCollector(map2);

        // Act
        collector1.visitAnyConstant(clazz, constant);
        collector2.visitAnyConstant(clazz, constant);

        // Assert - verify each map remained independent and unchanged
        assertEquals(1, map1.size(), "First map should remain unchanged");
        assertEquals(1, map2.size(), "Second map should remain unchanged");
        assertTrue(map1.containsKey(0), "First map should still have key 0");
        assertTrue(map2.containsKey(1), "Second map should still have key 1");
    }

    /**
     * Tests that visitAnyConstant with mixed null and valid calls works correctly.
     * The method should handle mixed null and non-null calls without issues.
     */
    @Test
    public void testVisitAnyConstant_mixedNullAndValidCalls_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> {
            collector.visitAnyConstant(null, null);
            collector.visitAnyConstant(clazz, constant);
            collector.visitAnyConstant(null, constant);
            collector.visitAnyConstant(clazz, null);
        });
    }

    /**
     * Tests that visitAnyConstant doesn't affect subsequent operations.
     * Calling visitAnyConstant should not interfere with other collector methods.
     */
    @Test
    public void testVisitAnyConstant_doesNotAffectSubsequentOperations() {
        // Arrange
        int initialSize = lambdaExpressions.size();

        // Act - call visitAnyConstant
        collector.visitAnyConstant(clazz, constant);

        // Add a lambda expression after calling visitAnyConstant
        lambdaExpressions.put(5, createSampleLambdaExpression());

        // Assert - verify the map can still be modified normally
        assertEquals(initialSize + 1, lambdaExpressions.size(), "Map should be modifiable after visitAnyConstant");
        assertTrue(lambdaExpressions.containsKey(5), "New entry should be added successfully");
    }

    /**
     * Tests that visitAnyConstant can be called with different Clazz instances and same Constant.
     * The method should handle any combination of parameters.
     */
    @Test
    public void testVisitAnyConstant_withDifferentClazzInstancesSameConstant_doesNotThrowException() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Clazz clazz3 = mock(ProgramClass.class);

        // Act & Assert - should not throw any exception with different clazz instances
        assertDoesNotThrow(() -> {
            collector.visitAnyConstant(clazz1, constant);
            collector.visitAnyConstant(clazz2, constant);
            collector.visitAnyConstant(clazz3, constant);
        });
    }

    /**
     * Tests that visitAnyConstant can be called with same Clazz and different Constants.
     * The method should handle any combination of parameters.
     */
    @Test
    public void testVisitAnyConstant_withSameClazzDifferentConstants_doesNotThrowException() {
        // Arrange
        Constant constant1 = mock(IntegerConstant.class);
        Constant constant2 = mock(StringConstant.class);
        Constant constant3 = mock(ClassConstant.class);

        // Act & Assert - should not throw any exception with different constant instances
        assertDoesNotThrow(() -> {
            collector.visitAnyConstant(clazz, constant1);
            collector.visitAnyConstant(clazz, constant2);
            collector.visitAnyConstant(clazz, constant3);
        });
    }

    /**
     * Tests that visitAnyConstant can be called alternately with other visitor methods.
     * The methods should work independently without interfering with each other.
     */
    @Test
    public void testVisitAnyConstant_alternatingWithOtherMethods_doesNotThrowException() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> {
            collector.visitAnyConstant(clazz, constant);
            collector.visitAnyClass(clazz);
            collector.visitAnyConstant(clazz, constant);
            collector.visitProgramClass(programClass);
            collector.visitAnyConstant(clazz, constant);
        });
    }

    /**
     * Tests that visitAnyConstant doesn't interact with either mock parameter.
     * Since it's a no-op method, it should not call any methods on either parameter.
     */
    @Test
    public void testVisitAnyConstant_doesNotInteractWithEitherParameter() {
        // Act
        collector.visitAnyConstant(clazz, constant);

        // Assert - verify no interactions occurred with either mock
        verifyNoInteractions(clazz);
        verifyNoInteractions(constant);
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
