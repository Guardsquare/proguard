package proguard.backport;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link LambdaExpressionCollector} constructor.
 * Tests LambdaExpressionCollector(Map<Integer, LambdaExpression>) constructor.
 */
public class LambdaExpressionCollectorClaude_constructorTest {

    /**
     * Tests the constructor with an empty HashMap.
     * Verifies that the collector is properly initialized with an empty map.
     */
    @Test
    public void testConstructorWithEmptyHashMap() {
        // Arrange
        Map<Integer, LambdaExpression> lambdaExpressions = new HashMap<>();

        // Act
        LambdaExpressionCollector collector = new LambdaExpressionCollector(lambdaExpressions);

        // Assert
        assertNotNull(collector, "Collector should be instantiated");
    }

    /**
     * Tests the constructor with a pre-populated HashMap.
     * Verifies that the map reference is stored and the collector can use it.
     */
    @Test
    public void testConstructorWithPrePopulatedHashMap() {
        // Arrange
        Map<Integer, LambdaExpression> lambdaExpressions = new HashMap<>();
        LambdaExpression lambda1 = createSampleLambdaExpression();
        LambdaExpression lambda2 = createSampleLambdaExpression();
        lambdaExpressions.put(0, lambda1);
        lambdaExpressions.put(1, lambda2);

        // Act
        LambdaExpressionCollector collector = new LambdaExpressionCollector(lambdaExpressions);

        // Assert
        assertNotNull(collector, "Collector should be instantiated");
        assertEquals(2, lambdaExpressions.size(), "Map should retain its entries");
    }

    /**
     * Tests the constructor with null Map.
     * Verifies that the constructor accepts null.
     */
    @Test
    public void testConstructorWithNullMap() {
        // Arrange
        Map<Integer, LambdaExpression> lambdaExpressions = null;

        // Act
        LambdaExpressionCollector collector = new LambdaExpressionCollector(lambdaExpressions);

        // Assert
        assertNotNull(collector, "Collector should be instantiated with null map");
    }

    /**
     * Tests the constructor with LinkedHashMap.
     * Verifies that different Map implementations are accepted.
     */
    @Test
    public void testConstructorWithLinkedHashMap() {
        // Arrange
        Map<Integer, LambdaExpression> lambdaExpressions = new LinkedHashMap<>();

        // Act
        LambdaExpressionCollector collector = new LambdaExpressionCollector(lambdaExpressions);

        // Assert
        assertNotNull(collector, "Collector should be instantiated with LinkedHashMap");
    }

    /**
     * Tests the constructor with TreeMap.
     * Verifies that different Map implementations are accepted.
     */
    @Test
    public void testConstructorWithTreeMap() {
        // Arrange
        Map<Integer, LambdaExpression> lambdaExpressions = new TreeMap<>();

        // Act
        LambdaExpressionCollector collector = new LambdaExpressionCollector(lambdaExpressions);

        // Assert
        assertNotNull(collector, "Collector should be instantiated with TreeMap");
    }

    /**
     * Tests the constructor with ConcurrentHashMap.
     * Verifies that thread-safe Map implementations are accepted.
     */
    @Test
    public void testConstructorWithConcurrentHashMap() {
        // Arrange
        Map<Integer, LambdaExpression> lambdaExpressions = new ConcurrentHashMap<>();

        // Act
        LambdaExpressionCollector collector = new LambdaExpressionCollector(lambdaExpressions);

        // Assert
        assertNotNull(collector, "Collector should be instantiated with ConcurrentHashMap");
    }

    /**
     * Tests that multiple collectors can be created with different maps.
     * Verifies that each collector maintains its own map reference.
     */
    @Test
    public void testMultipleCollectorsWithDifferentMaps() {
        // Arrange
        Map<Integer, LambdaExpression> map1 = new HashMap<>();
        Map<Integer, LambdaExpression> map2 = new HashMap<>();
        map1.put(0, createSampleLambdaExpression());
        map2.put(1, createSampleLambdaExpression());

        // Act
        LambdaExpressionCollector collector1 = new LambdaExpressionCollector(map1);
        LambdaExpressionCollector collector2 = new LambdaExpressionCollector(map2);

        // Assert
        assertNotNull(collector1, "First collector should be instantiated");
        assertNotNull(collector2, "Second collector should be instantiated");
        assertNotSame(collector1, collector2, "Collectors should be different instances");
    }

    /**
     * Tests the constructor with a map containing multiple entries.
     * Verifies that the map with various entries is accepted.
     */
    @Test
    public void testConstructorWithMultipleMapEntries() {
        // Arrange
        Map<Integer, LambdaExpression> lambdaExpressions = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            lambdaExpressions.put(i, createSampleLambdaExpression());
        }

        // Act
        LambdaExpressionCollector collector = new LambdaExpressionCollector(lambdaExpressions);

        // Assert
        assertNotNull(collector, "Collector should be instantiated with multiple entries");
        assertEquals(10, lambdaExpressions.size(), "Map should retain all entries");
    }

    /**
     * Tests the constructor with map containing null values.
     * Verifies that null LambdaExpression values in the map are accepted.
     */
    @Test
    public void testConstructorWithMapContainingNullValues() {
        // Arrange
        Map<Integer, LambdaExpression> lambdaExpressions = new HashMap<>();
        lambdaExpressions.put(0, null);
        lambdaExpressions.put(1, createSampleLambdaExpression());
        lambdaExpressions.put(2, null);

        // Act
        LambdaExpressionCollector collector = new LambdaExpressionCollector(lambdaExpressions);

        // Assert
        assertNotNull(collector, "Collector should be instantiated with map containing null values");
        assertEquals(3, lambdaExpressions.size(), "Map should retain all entries including nulls");
    }

    /**
     * Tests the constructor with map using negative keys.
     * Verifies that negative integer keys are accepted.
     */
    @Test
    public void testConstructorWithNegativeKeys() {
        // Arrange
        Map<Integer, LambdaExpression> lambdaExpressions = new HashMap<>();
        lambdaExpressions.put(-1, createSampleLambdaExpression());
        lambdaExpressions.put(-10, createSampleLambdaExpression());

        // Act
        LambdaExpressionCollector collector = new LambdaExpressionCollector(lambdaExpressions);

        // Assert
        assertNotNull(collector, "Collector should be instantiated with negative keys");
        assertEquals(2, lambdaExpressions.size(), "Map should retain entries with negative keys");
    }

    /**
     * Tests the constructor with map using large key values.
     * Verifies that large integer keys are accepted.
     */
    @Test
    public void testConstructorWithLargeKeyValues() {
        // Arrange
        Map<Integer, LambdaExpression> lambdaExpressions = new HashMap<>();
        lambdaExpressions.put(Integer.MAX_VALUE, createSampleLambdaExpression());
        lambdaExpressions.put(Integer.MAX_VALUE - 1, createSampleLambdaExpression());

        // Act
        LambdaExpressionCollector collector = new LambdaExpressionCollector(lambdaExpressions);

        // Assert
        assertNotNull(collector, "Collector should be instantiated with large key values");
        assertEquals(2, lambdaExpressions.size(), "Map should retain entries with large keys");
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
