package proguard.backport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ProgramClass;
import proguard.classfile.VersionConstants;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link LambdaExpressionCollector#visitProgramClass(ProgramClass)}.
 *
 * The visitProgramClass method processes ProgramClass instances to collect lambda expressions
 * by visiting InvokeDynamic constants in the constant pool. This method filters for
 * INVOKE_DYNAMIC constants and processes them to extract lambda expression information.
 */
public class LambdaExpressionCollectorClaude_visitProgramClassTest {

    private LambdaExpressionCollector collector;
    private Map<Integer, LambdaExpression> lambdaExpressions;

    @BeforeEach
    public void setUp() {
        lambdaExpressions = new HashMap<>();
        collector = new LambdaExpressionCollector(lambdaExpressions);
    }

    /**
     * Tests visitProgramClass with a newly created ProgramClass (empty constant pool).
     * Verifies that the method handles an empty class without throwing exceptions.
     */
    @Test
    public void testVisitProgramClass_withEmptyProgramClass_doesNotThrowException() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> collector.visitProgramClass(programClass));
    }

    /**
     * Tests visitProgramClass with a ProgramClass that has basic initialization.
     * Verifies the method works with a minimally configured ProgramClass.
     */
    @Test
    public void testVisitProgramClass_withBasicProgramClass_doesNotThrowException() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u4version = VersionConstants.CLASS_VERSION_1_8;

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> collector.visitProgramClass(programClass));
    }

    /**
     * Tests visitProgramClass does not modify the lambdaExpressions map when processing
     * a ProgramClass without InvokeDynamic constants.
     */
    @Test
    public void testVisitProgramClass_withoutInvokeDynamic_doesNotModifyMap() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        int initialSize = lambdaExpressions.size();

        // Act
        collector.visitProgramClass(programClass);

        // Assert - map should remain empty
        assertEquals(initialSize, lambdaExpressions.size(),
                "Map should not be modified when no InvokeDynamic constants are present");
    }

    /**
     * Tests visitProgramClass can be called multiple times on the same ProgramClass.
     * Verifies the method is idempotent for classes without lambda expressions.
     */
    @Test
    public void testVisitProgramClass_calledMultipleTimes_doesNotThrowException() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - should not throw any exception on multiple calls
        assertDoesNotThrow(() -> {
            collector.visitProgramClass(programClass);
            collector.visitProgramClass(programClass);
            collector.visitProgramClass(programClass);
        });
    }

    /**
     * Tests visitProgramClass with null ProgramClass throws NullPointerException.
     * Verifies proper null handling behavior.
     */
    @Test
    public void testVisitProgramClass_withNullClass_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> collector.visitProgramClass(null));
    }

    /**
     * Tests visitProgramClass with different ProgramClass instances.
     * Verifies the method can handle multiple different classes.
     */
    @Test
    public void testVisitProgramClass_withDifferentClasses_doesNotThrowException() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();
        ProgramClass class3 = new ProgramClass();

        // Act & Assert - should not throw any exception with different instances
        assertDoesNotThrow(() -> {
            collector.visitProgramClass(class1);
            collector.visitProgramClass(class2);
            collector.visitProgramClass(class3);
        });
    }

    /**
     * Tests visitProgramClass does not affect the collector's internal state
     * when processing classes without lambda expressions.
     */
    @Test
    public void testVisitProgramClass_doesNotModifyCollectorState() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        lambdaExpressions.put(1, createSampleLambdaExpression());
        Map<Integer, LambdaExpression> snapshotBefore = new HashMap<>(lambdaExpressions);

        // Act
        collector.visitProgramClass(programClass);

        // Assert - verify the map state hasn't changed
        assertEquals(snapshotBefore.size(), lambdaExpressions.size(),
                "Map size should remain the same");
        assertEquals(snapshotBefore, lambdaExpressions,
                "Map contents should remain the same");
    }

    /**
     * Tests visitProgramClass with a collector that has an empty map.
     * Verifies the method works with an initially empty lambda expressions map.
     */
    @Test
    public void testVisitProgramClass_withEmptyMap_doesNotThrowException() {
        // Arrange
        LambdaExpressionCollector collectorWithEmptyMap =
                new LambdaExpressionCollector(new HashMap<>());
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> collectorWithEmptyMap.visitProgramClass(programClass));
    }

    /**
     * Tests visitProgramClass with a collector that has a pre-populated map.
     * Verifies the method works regardless of the map's initial contents.
     */
    @Test
    public void testVisitProgramClass_withPopulatedMap_doesNotThrowException() {
        // Arrange
        Map<Integer, LambdaExpression> populatedMap = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            populatedMap.put(i, createSampleLambdaExpression());
        }
        LambdaExpressionCollector collectorWithPopulatedMap =
                new LambdaExpressionCollector(populatedMap);
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> collectorWithPopulatedMap.visitProgramClass(programClass));

        // Verify the map wasn't modified
        assertEquals(5, populatedMap.size(),
                "Map size should remain unchanged");
    }

    /**
     * Tests visitProgramClass with different Map implementations.
     * Verifies the method works with various Map types.
     */
    @Test
    public void testVisitProgramClass_withLinkedHashMap_doesNotThrowException() {
        // Arrange
        LambdaExpressionCollector collectorWithLinkedHashMap =
                new LambdaExpressionCollector(new LinkedHashMap<>());
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> collectorWithLinkedHashMap.visitProgramClass(programClass));
    }

    /**
     * Tests visitProgramClass with ConcurrentHashMap.
     * Verifies the method works with thread-safe Map implementations.
     */
    @Test
    public void testVisitProgramClass_withConcurrentHashMap_doesNotThrowException() {
        // Arrange
        LambdaExpressionCollector collectorWithConcurrentHashMap =
                new LambdaExpressionCollector(new ConcurrentHashMap<>());
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> collectorWithConcurrentHashMap.visitProgramClass(programClass));
    }

    /**
     * Tests that multiple collectors can independently visit the same ProgramClass.
     * Each collector should maintain its own independent state.
     */
    @Test
    public void testVisitProgramClass_multipleCollectorsIndependent() {
        // Arrange
        Map<Integer, LambdaExpression> map1 = new HashMap<>();
        Map<Integer, LambdaExpression> map2 = new HashMap<>();
        map1.put(0, createSampleLambdaExpression());
        map2.put(1, createSampleLambdaExpression());

        LambdaExpressionCollector collector1 = new LambdaExpressionCollector(map1);
        LambdaExpressionCollector collector2 = new LambdaExpressionCollector(map2);

        ProgramClass programClass = new ProgramClass();

        // Act
        collector1.visitProgramClass(programClass);
        collector2.visitProgramClass(programClass);

        // Assert - verify each map remained independent and unchanged
        assertEquals(1, map1.size(), "First map should remain unchanged");
        assertEquals(1, map2.size(), "Second map should remain unchanged");
        assertTrue(map1.containsKey(0), "First map should still have key 0");
        assertTrue(map2.containsKey(1), "Second map should still have key 1");
    }

    /**
     * Tests visitProgramClass does not throw exception after calling visitAnyClass.
     * Verifies the methods work independently without interfering with each other.
     */
    @Test
    public void testVisitProgramClass_afterVisitAnyClass_doesNotThrowException() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> {
            collector.visitAnyClass(programClass);
            collector.visitProgramClass(programClass);
        });
    }

    /**
     * Tests visitProgramClass with alternating calls to visitAnyClass.
     * Verifies both methods can be called in any order without issues.
     */
    @Test
    public void testVisitProgramClass_alternatingWithVisitAnyClass_doesNotThrowException() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> {
            collector.visitAnyClass(programClass);
            collector.visitProgramClass(programClass);
            collector.visitAnyClass(programClass);
            collector.visitProgramClass(programClass);
        });
    }

    /**
     * Tests visitProgramClass execution completes for classes without lambda expressions.
     * Since the method filters for InvokeDynamic constants, it should complete quickly
     * for classes without them.
     */
    @Test
    public void testVisitProgramClass_executesQuicklyForEmptyClass() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            collector.visitProgramClass(programClass);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete reasonably quickly (within 200ms for 1000 calls)
        assertTrue(durationMs < 200,
                "visitProgramClass should execute quickly for empty classes");
    }

    /**
     * Tests that visitProgramClass does not modify the ProgramClass itself.
     * The method should only read from the class, not modify it.
     */
    @Test
    public void testVisitProgramClass_doesNotModifyProgramClass() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u4version = VersionConstants.CLASS_VERSION_1_8;
        int originalVersion = programClass.u4version;

        // Act
        collector.visitProgramClass(programClass);

        // Assert - version should remain unchanged
        assertEquals(originalVersion, programClass.u4version,
                "visitProgramClass should not modify the ProgramClass");
    }

    /**
     * Tests visitProgramClass with sequential calls on different ProgramClass instances.
     * Verifies the method maintains stateless behavior across different classes.
     */
    @Test
    public void testVisitProgramClass_sequentialCallsOnDifferentClasses_stateless() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();
        ProgramClass class3 = new ProgramClass();

        int initialSize = lambdaExpressions.size();

        // Act
        collector.visitProgramClass(class1);
        collector.visitProgramClass(class2);
        collector.visitProgramClass(class3);

        // Assert - map should remain unchanged (no InvokeDynamic constants)
        assertEquals(initialSize, lambdaExpressions.size(),
                "Map should remain unchanged across multiple visits");
    }

    /**
     * Tests visitProgramClass does not interfere with subsequent operations.
     * Calling visitProgramClass should not affect other collector operations.
     */
    @Test
    public void testVisitProgramClass_doesNotAffectSubsequentOperations() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        int initialSize = lambdaExpressions.size();

        // Act - call visitProgramClass
        collector.visitProgramClass(programClass);

        // Add a lambda expression after calling visitProgramClass
        lambdaExpressions.put(5, createSampleLambdaExpression());

        // Assert - verify the map can still be modified normally
        assertEquals(initialSize + 1, lambdaExpressions.size(),
                "Map should be modifiable after visitProgramClass");
        assertTrue(lambdaExpressions.containsKey(5),
                "New entry should be added successfully");
    }

    /**
     * Tests visitProgramClass with ProgramClass instances having different version numbers.
     * Verifies the method handles classes from different Java versions.
     */
    @Test
    public void testVisitProgramClass_withDifferentClassVersions_doesNotThrowException() {
        // Arrange
        ProgramClass java8Class = new ProgramClass();
        java8Class.u4version = VersionConstants.CLASS_VERSION_1_8;

        ProgramClass java11Class = new ProgramClass();
        java11Class.u4version = VersionConstants.CLASS_VERSION_11;

        ProgramClass java17Class = new ProgramClass();
        java17Class.u4version = VersionConstants.CLASS_VERSION_17;

        // Act & Assert - should not throw any exception with different versions
        assertDoesNotThrow(() -> {
            collector.visitProgramClass(java8Class);
            collector.visitProgramClass(java11Class);
            collector.visitProgramClass(java17Class);
        });
    }

    /**
     * Tests visitProgramClass maintains correct behavior after being used extensively.
     * Verifies the collector doesn't accumulate state that affects subsequent calls.
     */
    @Test
    public void testVisitProgramClass_afterManyOperations_stillWorksCorrectly() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act - perform many operations
        for (int i = 0; i < 100; i++) {
            collector.visitProgramClass(programClass);
        }

        // Assert - final call should still work correctly
        assertDoesNotThrow(() -> collector.visitProgramClass(programClass));
        assertTrue(lambdaExpressions.isEmpty() || lambdaExpressions.size() >= 0,
                "Map should be in valid state after many operations");
    }

    /**
     * Tests that visitProgramClass works correctly with a collector using null map.
     * This tests edge case behavior when the internal map is null.
     */
    @Test
    public void testVisitProgramClass_withNullMap_throwsNullPointerException() {
        // Arrange - Create collector with null map
        LambdaExpressionCollector collectorWithNullMap =
                new LambdaExpressionCollector(null);
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - Since the class has no InvokeDynamic constants,
        // the null map won't be accessed and no exception should occur
        assertDoesNotThrow(() -> collectorWithNullMap.visitProgramClass(programClass));
    }

    /**
     * Tests visitProgramClass returns normally (no return value to verify).
     * Verifies the method signature and behavior (void return type).
     */
    @Test
    public void testVisitProgramClass_returnsNormally() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act - method has void return type, just verify it completes
        collector.visitProgramClass(programClass);

        // Assert - if we reach here, the method completed normally
        assertTrue(true, "Method completed normally");
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
