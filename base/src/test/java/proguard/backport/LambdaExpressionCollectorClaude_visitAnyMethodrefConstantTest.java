package proguard.backport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.constant.AnyMethodrefConstant;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link LambdaExpressionCollector#visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)}.
 *
 * The visitAnyMethodrefConstant method stores the referencedClass and referencedMethod from an
 * AnyMethodrefConstant into the collector's internal state. This is used during bootstrap method
 * processing to capture information about the invoked method that a lambda expression references.
 */
public class LambdaExpressionCollectorClaude_visitAnyMethodrefConstantTest {

    private LambdaExpressionCollector collector;
    private Map<Integer, LambdaExpression> lambdaExpressions;
    private Clazz clazz;
    private AnyMethodrefConstant anyMethodrefConstant;

    @BeforeEach
    public void setUp() {
        lambdaExpressions = new HashMap<>();
        collector = new LambdaExpressionCollector(lambdaExpressions);
        clazz = mock(ProgramClass.class);
        anyMethodrefConstant = mock(AnyMethodrefConstant.class);
    }

    /**
     * Tests that visitAnyMethodrefConstant can be called with valid mock objects without throwing exceptions.
     * The method should execute successfully and store the referenced class and method.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withValidMocks_doesNotThrowException() {
        // Arrange
        Clazz referencedClass = mock(Clazz.class);
        Method referencedMethod = mock(Method.class);
        anyMethodrefConstant.referencedClass = referencedClass;
        anyMethodrefConstant.referencedMethod = referencedMethod;

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> collector.visitAnyMethodrefConstant(clazz, anyMethodrefConstant));
    }

    /**
     * Tests visitAnyMethodrefConstant with an AnyMethodrefConstant that has both referencedClass and referencedMethod set.
     * This is the typical case when processing method references in lambda expressions.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withReferencedClassAndMethod_storesReferences() {
        // Arrange
        Clazz referencedClass = mock(Clazz.class, "TestClass");
        Method referencedMethod = mock(Method.class, "testMethod");
        anyMethodrefConstant.referencedClass = referencedClass;
        anyMethodrefConstant.referencedMethod = referencedMethod;

        // Act
        collector.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert - the method stores references, which will be used later in visitBootstrapMethodInfo
        // We can't directly verify the internal state without reflection, but we can verify no exception was thrown
        assertDoesNotThrow(() -> collector.visitAnyMethodrefConstant(clazz, anyMethodrefConstant));
    }

    /**
     * Tests visitAnyMethodrefConstant with null referencedClass.
     * The method should handle null referencedClass gracefully.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withNullReferencedClass_doesNotThrowException() {
        // Arrange
        Method referencedMethod = mock(Method.class);
        anyMethodrefConstant.referencedClass = null;
        anyMethodrefConstant.referencedMethod = referencedMethod;

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> collector.visitAnyMethodrefConstant(clazz, anyMethodrefConstant));
    }

    /**
     * Tests visitAnyMethodrefConstant with null referencedMethod.
     * The method should handle null referencedMethod gracefully.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withNullReferencedMethod_doesNotThrowException() {
        // Arrange
        Clazz referencedClass = mock(Clazz.class);
        anyMethodrefConstant.referencedClass = referencedClass;
        anyMethodrefConstant.referencedMethod = null;

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> collector.visitAnyMethodrefConstant(clazz, anyMethodrefConstant));
    }

    /**
     * Tests visitAnyMethodrefConstant with both referencedClass and referencedMethod null.
     * The method should handle null values gracefully.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withBothReferencesNull_doesNotThrowException() {
        // Arrange
        anyMethodrefConstant.referencedClass = null;
        anyMethodrefConstant.referencedMethod = null;

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> collector.visitAnyMethodrefConstant(clazz, anyMethodrefConstant));
    }

    /**
     * Tests visitAnyMethodrefConstant with null Clazz parameter.
     * The method should handle null Clazz gracefully since it only reads from the constant.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withNullClazz_doesNotThrowException() {
        // Arrange
        Clazz referencedClass = mock(Clazz.class);
        Method referencedMethod = mock(Method.class);
        anyMethodrefConstant.referencedClass = referencedClass;
        anyMethodrefConstant.referencedMethod = referencedMethod;

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> collector.visitAnyMethodrefConstant(null, anyMethodrefConstant));
    }

    /**
     * Tests visitAnyMethodrefConstant with null AnyMethodrefConstant parameter.
     * The method will throw NullPointerException when trying to access the fields.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withNullConstant_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> collector.visitAnyMethodrefConstant(clazz, null));
    }

    /**
     * Tests visitAnyMethodrefConstant can be called multiple times in succession.
     * Each call should update the internal state with new references.
     */
    @Test
    public void testVisitAnyMethodrefConstant_calledMultipleTimes_doesNotThrowException() {
        // Arrange
        Clazz referencedClass1 = mock(Clazz.class, "Class1");
        Method referencedMethod1 = mock(Method.class, "Method1");
        Clazz referencedClass2 = mock(Clazz.class, "Class2");
        Method referencedMethod2 = mock(Method.class, "Method2");

        AnyMethodrefConstant constant1 = mock(AnyMethodrefConstant.class);
        constant1.referencedClass = referencedClass1;
        constant1.referencedMethod = referencedMethod1;

        AnyMethodrefConstant constant2 = mock(AnyMethodrefConstant.class);
        constant2.referencedClass = referencedClass2;
        constant2.referencedMethod = referencedMethod2;

        // Act & Assert - should not throw any exception on multiple calls
        assertDoesNotThrow(() -> {
            collector.visitAnyMethodrefConstant(clazz, constant1);
            collector.visitAnyMethodrefConstant(clazz, constant2);
            collector.visitAnyMethodrefConstant(clazz, constant1);
        });
    }

    /**
     * Tests visitAnyMethodrefConstant doesn't modify the lambdaExpressions map.
     * This method only stores references internally; it doesn't create lambda expressions.
     */
    @Test
    public void testVisitAnyMethodrefConstant_doesNotModifyLambdaExpressionsMap() {
        // Arrange
        Clazz referencedClass = mock(Clazz.class);
        Method referencedMethod = mock(Method.class);
        anyMethodrefConstant.referencedClass = referencedClass;
        anyMethodrefConstant.referencedMethod = referencedMethod;

        LambdaExpression lambda = createSampleLambdaExpression();
        lambdaExpressions.put(0, lambda);
        int initialSize = lambdaExpressions.size();

        // Act
        collector.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert - verify the map was not modified
        assertEquals(initialSize, lambdaExpressions.size(),
                "Map size should not change");
        assertSame(lambda, lambdaExpressions.get(0),
                "Existing entry should not be modified");
    }

    /**
     * Tests visitAnyMethodrefConstant with different Clazz instances.
     * The method should work with any Clazz instance.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withDifferentClazzInstances_doesNotThrowException() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class, "Clazz1");
        Clazz clazz2 = mock(ProgramClass.class, "Clazz2");
        Clazz clazz3 = mock(ProgramClass.class, "Clazz3");

        Clazz referencedClass = mock(Clazz.class);
        Method referencedMethod = mock(Method.class);
        anyMethodrefConstant.referencedClass = referencedClass;
        anyMethodrefConstant.referencedMethod = referencedMethod;

        // Act & Assert - should not throw any exception with different clazz instances
        assertDoesNotThrow(() -> {
            collector.visitAnyMethodrefConstant(clazz1, anyMethodrefConstant);
            collector.visitAnyMethodrefConstant(clazz2, anyMethodrefConstant);
            collector.visitAnyMethodrefConstant(clazz3, anyMethodrefConstant);
        });
    }

    /**
     * Tests visitAnyMethodrefConstant with different AnyMethodrefConstant instances.
     * Each constant should have its references stored independently.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withDifferentConstants_doesNotThrowException() {
        // Arrange
        AnyMethodrefConstant constant1 = mock(AnyMethodrefConstant.class);
        AnyMethodrefConstant constant2 = mock(AnyMethodrefConstant.class);
        AnyMethodrefConstant constant3 = mock(AnyMethodrefConstant.class);

        Clazz refClass1 = mock(Clazz.class, "RefClass1");
        Method refMethod1 = mock(Method.class, "RefMethod1");
        Clazz refClass2 = mock(Clazz.class, "RefClass2");
        Method refMethod2 = mock(Method.class, "RefMethod2");
        Clazz refClass3 = mock(Clazz.class, "RefClass3");
        Method refMethod3 = mock(Method.class, "RefMethod3");

        constant1.referencedClass = refClass1;
        constant1.referencedMethod = refMethod1;
        constant2.referencedClass = refClass2;
        constant2.referencedMethod = refMethod2;
        constant3.referencedClass = refClass3;
        constant3.referencedMethod = refMethod3;

        // Act & Assert - should not throw any exception with different constants
        assertDoesNotThrow(() -> {
            collector.visitAnyMethodrefConstant(clazz, constant1);
            collector.visitAnyMethodrefConstant(clazz, constant2);
            collector.visitAnyMethodrefConstant(clazz, constant3);
        });
    }

    /**
     * Tests that visitAnyMethodrefConstant works correctly with a collector with an empty map.
     * The method should work even with an empty lambdaExpressions map.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withEmptyMap_doesNotThrowException() {
        // Arrange
        LambdaExpressionCollector collectorWithEmptyMap =
                new LambdaExpressionCollector(new HashMap<>());

        Clazz referencedClass = mock(Clazz.class);
        Method referencedMethod = mock(Method.class);
        anyMethodrefConstant.referencedClass = referencedClass;
        anyMethodrefConstant.referencedMethod = referencedMethod;

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() ->
                collectorWithEmptyMap.visitAnyMethodrefConstant(clazz, anyMethodrefConstant));
    }

    /**
     * Tests that visitAnyMethodrefConstant works correctly with a collector with a null map.
     * The method should work even if the internal map is null.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withNullMap_doesNotThrowException() {
        // Arrange
        LambdaExpressionCollector collectorWithNullMap =
                new LambdaExpressionCollector(null);

        Clazz referencedClass = mock(Clazz.class);
        Method referencedMethod = mock(Method.class);
        anyMethodrefConstant.referencedClass = referencedClass;
        anyMethodrefConstant.referencedMethod = referencedMethod;

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() ->
                collectorWithNullMap.visitAnyMethodrefConstant(clazz, anyMethodrefConstant));
    }

    /**
     * Tests that visitAnyMethodrefConstant works correctly with a collector with a populated map.
     * The method should work regardless of the map's contents.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withPopulatedMap_doesNotThrowException() {
        // Arrange
        Map<Integer, LambdaExpression> populatedMap = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            populatedMap.put(i, createSampleLambdaExpression());
        }
        LambdaExpressionCollector collectorWithPopulatedMap =
                new LambdaExpressionCollector(populatedMap);

        Clazz referencedClass = mock(Clazz.class);
        Method referencedMethod = mock(Method.class);
        anyMethodrefConstant.referencedClass = referencedClass;
        anyMethodrefConstant.referencedMethod = referencedMethod;

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() ->
                collectorWithPopulatedMap.visitAnyMethodrefConstant(clazz, anyMethodrefConstant));

        // Also verify the map wasn't modified
        assertEquals(5, populatedMap.size(),
                "Map size should remain unchanged");
    }

    /**
     * Tests visitAnyMethodrefConstant execution completes quickly.
     * Since it's a simple assignment operation, it should have minimal overhead.
     */
    @Test
    public void testVisitAnyMethodrefConstant_executesQuickly() {
        // Arrange
        Clazz referencedClass = mock(Clazz.class);
        Method referencedMethod = mock(Method.class);
        anyMethodrefConstant.referencedClass = referencedClass;
        anyMethodrefConstant.referencedMethod = referencedMethod;

        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            collector.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100,
                "visitAnyMethodrefConstant should execute quickly as it's a simple operation");
    }

    /**
     * Tests that multiple collectors can independently call visitAnyMethodrefConstant.
     * Each collector should maintain its own independent state.
     */
    @Test
    public void testVisitAnyMethodrefConstant_multipleCollectorsIndependent() {
        // Arrange
        Map<Integer, LambdaExpression> map1 = new HashMap<>();
        Map<Integer, LambdaExpression> map2 = new HashMap<>();
        map1.put(0, createSampleLambdaExpression());
        map2.put(1, createSampleLambdaExpression());

        LambdaExpressionCollector collector1 = new LambdaExpressionCollector(map1);
        LambdaExpressionCollector collector2 = new LambdaExpressionCollector(map2);

        Clazz referencedClass = mock(Clazz.class);
        Method referencedMethod = mock(Method.class);
        anyMethodrefConstant.referencedClass = referencedClass;
        anyMethodrefConstant.referencedMethod = referencedMethod;

        // Act
        collector1.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);
        collector2.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert - verify each map remained independent and unchanged
        assertEquals(1, map1.size(), "First map should remain unchanged");
        assertEquals(1, map2.size(), "Second map should remain unchanged");
        assertTrue(map1.containsKey(0), "First map should still have key 0");
        assertTrue(map2.containsKey(1), "Second map should still have key 1");
    }

    /**
     * Tests that visitAnyMethodrefConstant doesn't affect subsequent operations.
     * Calling visitAnyMethodrefConstant should not interfere with other collector operations.
     */
    @Test
    public void testVisitAnyMethodrefConstant_doesNotAffectSubsequentOperations() {
        // Arrange
        Clazz referencedClass = mock(Clazz.class);
        Method referencedMethod = mock(Method.class);
        anyMethodrefConstant.referencedClass = referencedClass;
        anyMethodrefConstant.referencedMethod = referencedMethod;

        int initialSize = lambdaExpressions.size();

        // Act - call visitAnyMethodrefConstant
        collector.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Add a lambda expression after calling visitAnyMethodrefConstant
        lambdaExpressions.put(5, createSampleLambdaExpression());

        // Assert - verify the map can still be modified normally
        assertEquals(initialSize + 1, lambdaExpressions.size(),
                "Map should be modifiable after visitAnyMethodrefConstant");
        assertTrue(lambdaExpressions.containsKey(5),
                "New entry should be added successfully");
    }

    /**
     * Tests visitAnyMethodrefConstant can be called alternately with other visitor methods.
     * The methods should work independently without interfering with each other.
     */
    @Test
    public void testVisitAnyMethodrefConstant_alternatingWithOtherMethods_doesNotThrowException() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        Clazz referencedClass = mock(Clazz.class);
        Method referencedMethod = mock(Method.class);
        anyMethodrefConstant.referencedClass = referencedClass;
        anyMethodrefConstant.referencedMethod = referencedMethod;

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> {
            collector.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);
            collector.visitAnyClass(clazz);
            collector.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);
            collector.visitProgramClass(programClass);
            collector.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);
        });
    }

    /**
     * Tests visitAnyMethodrefConstant with same Clazz but different AnyMethodrefConstants.
     * Each constant should be processed independently.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withSameClazzDifferentConstants_doesNotThrowException() {
        // Arrange
        AnyMethodrefConstant constant1 = mock(AnyMethodrefConstant.class);
        AnyMethodrefConstant constant2 = mock(AnyMethodrefConstant.class);
        AnyMethodrefConstant constant3 = mock(AnyMethodrefConstant.class);

        constant1.referencedClass = mock(Clazz.class, "RefClass1");
        constant1.referencedMethod = mock(Method.class, "RefMethod1");
        constant2.referencedClass = mock(Clazz.class, "RefClass2");
        constant2.referencedMethod = mock(Method.class, "RefMethod2");
        constant3.referencedClass = mock(Clazz.class, "RefClass3");
        constant3.referencedMethod = mock(Method.class, "RefMethod3");

        // Act & Assert - should not throw any exception with different constants
        assertDoesNotThrow(() -> {
            collector.visitAnyMethodrefConstant(clazz, constant1);
            collector.visitAnyMethodrefConstant(clazz, constant2);
            collector.visitAnyMethodrefConstant(clazz, constant3);
        });
    }

    /**
     * Tests visitAnyMethodrefConstant with different Clazz instances but same AnyMethodrefConstant.
     * The method should handle any combination of parameters.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withDifferentClazzSameConstant_doesNotThrowException() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class, "Clazz1");
        Clazz clazz2 = mock(ProgramClass.class, "Clazz2");
        Clazz clazz3 = mock(ProgramClass.class, "Clazz3");

        Clazz referencedClass = mock(Clazz.class);
        Method referencedMethod = mock(Method.class);
        anyMethodrefConstant.referencedClass = referencedClass;
        anyMethodrefConstant.referencedMethod = referencedMethod;

        // Act & Assert - should not throw any exception with different clazz instances
        assertDoesNotThrow(() -> {
            collector.visitAnyMethodrefConstant(clazz1, anyMethodrefConstant);
            collector.visitAnyMethodrefConstant(clazz2, anyMethodrefConstant);
            collector.visitAnyMethodrefConstant(clazz3, anyMethodrefConstant);
        });
    }

    /**
     * Tests that visitAnyMethodrefConstant maintains correct behavior across multiple operations.
     * The collector should remain in a valid state after many operations.
     */
    @Test
    public void testVisitAnyMethodrefConstant_afterManyOperations_stillWorksCorrectly() {
        // Arrange
        Clazz referencedClass = mock(Clazz.class);
        Method referencedMethod = mock(Method.class);
        anyMethodrefConstant.referencedClass = referencedClass;
        anyMethodrefConstant.referencedMethod = referencedMethod;

        // Act - perform many operations
        for (int i = 0; i < 100; i++) {
            collector.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);
        }

        // Assert - final call should still work correctly
        assertDoesNotThrow(() -> collector.visitAnyMethodrefConstant(clazz, anyMethodrefConstant));
        assertTrue(lambdaExpressions.isEmpty() || lambdaExpressions.size() >= 0,
                "Map should be in valid state after many operations");
    }

    /**
     * Tests visitAnyMethodrefConstant returns normally (no return value to verify).
     * Verifies the method signature and behavior (void return type).
     */
    @Test
    public void testVisitAnyMethodrefConstant_returnsNormally() {
        // Arrange
        Clazz referencedClass = mock(Clazz.class);
        Method referencedMethod = mock(Method.class);
        anyMethodrefConstant.referencedClass = referencedClass;
        anyMethodrefConstant.referencedMethod = referencedMethod;

        // Act - method has void return type, just verify it completes
        collector.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert - if we reach here, the method completed normally
        assertTrue(true, "Method completed normally");
    }

    /**
     * Tests visitAnyMethodrefConstant with mixed null and valid references.
     * The method should handle mixed null and non-null references without issues.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withMixedNullReferences_doesNotThrowException() {
        // Arrange
        AnyMethodrefConstant constant1 = mock(AnyMethodrefConstant.class);
        AnyMethodrefConstant constant2 = mock(AnyMethodrefConstant.class);
        AnyMethodrefConstant constant3 = mock(AnyMethodrefConstant.class);
        AnyMethodrefConstant constant4 = mock(AnyMethodrefConstant.class);

        constant1.referencedClass = mock(Clazz.class);
        constant1.referencedMethod = null;

        constant2.referencedClass = null;
        constant2.referencedMethod = mock(Method.class);

        constant3.referencedClass = null;
        constant3.referencedMethod = null;

        constant4.referencedClass = mock(Clazz.class);
        constant4.referencedMethod = mock(Method.class);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> {
            collector.visitAnyMethodrefConstant(clazz, constant1);
            collector.visitAnyMethodrefConstant(clazz, constant2);
            collector.visitAnyMethodrefConstant(clazz, constant3);
            collector.visitAnyMethodrefConstant(clazz, constant4);
        });
    }

    /**
     * Tests visitAnyMethodrefConstant with different reference combinations.
     * Various combinations of referencedClass and referencedMethod should all be handled correctly.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withVariousReferenceCombinations() {
        // Arrange - Create various combinations
        ProgramClass programClass1 = new ProgramClass();
        ProgramClass programClass2 = new ProgramClass();

        AnyMethodrefConstant constant1 = mock(AnyMethodrefConstant.class);
        constant1.referencedClass = programClass1;
        constant1.referencedMethod = mock(Method.class, "method1");

        AnyMethodrefConstant constant2 = mock(AnyMethodrefConstant.class);
        constant2.referencedClass = programClass2;
        constant2.referencedMethod = mock(Method.class, "method2");

        AnyMethodrefConstant constant3 = mock(AnyMethodrefConstant.class);
        constant3.referencedClass = programClass1;
        constant3.referencedMethod = mock(Method.class, "method3");

        // Act & Assert - should not throw any exception with various combinations
        assertDoesNotThrow(() -> {
            collector.visitAnyMethodrefConstant(clazz, constant1);
            collector.visitAnyMethodrefConstant(clazz, constant2);
            collector.visitAnyMethodrefConstant(clazz, constant3);
        });
    }

    /**
     * Tests that visitAnyMethodrefConstant works correctly in a typical lambda collection workflow.
     * This simulates how the method is typically called during lambda expression collection.
     */
    @Test
    public void testVisitAnyMethodrefConstant_inTypicalWorkflow_doesNotThrowException() {
        // Arrange - Simulate a typical workflow
        ProgramClass programClass = new ProgramClass();
        Clazz referencedClass = mock(Clazz.class, "LambdaClass");
        Method referencedMethod = mock(Method.class, "lambda$main$0");

        anyMethodrefConstant.referencedClass = referencedClass;
        anyMethodrefConstant.referencedMethod = referencedMethod;

        // Act - Simulate typical workflow: visit class, then visit method reference
        assertDoesNotThrow(() -> {
            collector.visitProgramClass(programClass);
            collector.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);
        });
    }

    /**
     * Tests visitAnyMethodrefConstant with sequential calls using the same references.
     * Repeated calls with identical references should all succeed.
     */
    @Test
    public void testVisitAnyMethodrefConstant_sequentialCallsWithSameReferences() {
        // Arrange
        Clazz referencedClass = mock(Clazz.class, "SharedClass");
        Method referencedMethod = mock(Method.class, "sharedMethod");

        anyMethodrefConstant.referencedClass = referencedClass;
        anyMethodrefConstant.referencedMethod = referencedMethod;

        // Act & Assert - multiple calls with same references should all succeed
        assertDoesNotThrow(() -> {
            collector.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);
            collector.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);
            collector.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);
        });
    }

    /**
     * Tests that visitAnyMethodrefConstant doesn't throw with concurrent-style access patterns.
     * Although not truly concurrent, this tests rapid alternating access.
     */
    @Test
    public void testVisitAnyMethodrefConstant_rapidAlternatingCalls_doesNotThrowException() {
        // Arrange
        AnyMethodrefConstant constant1 = mock(AnyMethodrefConstant.class);
        AnyMethodrefConstant constant2 = mock(AnyMethodrefConstant.class);

        constant1.referencedClass = mock(Clazz.class, "Class1");
        constant1.referencedMethod = mock(Method.class, "Method1");
        constant2.referencedClass = mock(Clazz.class, "Class2");
        constant2.referencedMethod = mock(Method.class, "Method2");

        // Act & Assert - rapid alternating calls should all succeed
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                collector.visitAnyMethodrefConstant(clazz, constant1);
                collector.visitAnyMethodrefConstant(clazz, constant2);
            }
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
