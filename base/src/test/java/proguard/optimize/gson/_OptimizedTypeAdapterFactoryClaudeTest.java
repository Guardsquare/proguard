package proguard.optimize.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link _OptimizedTypeAdapterFactory}.
 * Tests all methods including constructor and create method.
 *
 * The _OptimizedTypeAdapterFactory class is a template for an optimized TypeAdapterFactory
 * that implements the TypeAdapterFactory interface. The create() method implementation is
 * expected to be replaced with injected bytecode that handles specific domain classes and
 * returns appropriate optimized type adapters.
 *
 * In its template form, the create() method always returns null, as noted in the class javadoc.
 */
public class _OptimizedTypeAdapterFactoryClaudeTest {

    // =========================================================================
    // Tests for constructor: <init>.()V
    // =========================================================================

    /**
     * Tests that the constructor creates a non-null instance.
     */
    @Test
    public void testConstructor_createsNonNullInstance() {
        // Act
        _OptimizedTypeAdapterFactory instance = new _OptimizedTypeAdapterFactory();

        // Assert
        assertNotNull(instance, "Constructor should create a non-null instance");
    }

    /**
     * Tests that the constructor creates an instance implementing TypeAdapterFactory.
     */
    @Test
    public void testConstructor_createsInstanceOfCorrectType() {
        // Act
        _OptimizedTypeAdapterFactory instance = new _OptimizedTypeAdapterFactory();

        // Assert
        assertTrue(instance instanceof com.google.gson.TypeAdapterFactory,
            "Instance should implement TypeAdapterFactory interface");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_multipleInstances_areIndependent() {
        // Act
        _OptimizedTypeAdapterFactory instance1 = new _OptimizedTypeAdapterFactory();
        _OptimizedTypeAdapterFactory instance2 = new _OptimizedTypeAdapterFactory();

        // Assert
        assertNotNull(instance1, "First instance should not be null");
        assertNotNull(instance2, "Second instance should not be null");
        assertNotSame(instance1, instance2, "Instances should be different objects");
    }

    /**
     * Tests that multiple instances can be created in sequence.
     */
    @Test
    public void testConstructor_sequentialCreation_succeeds() {
        // Act & Assert
        for (int i = 0; i < 10; i++) {
            _OptimizedTypeAdapterFactory instance = new _OptimizedTypeAdapterFactory();
            assertNotNull(instance, "Instance " + i + " should not be null");
        }
    }

    /**
     * Tests that the constructor can be called repeatedly without side effects.
     */
    @Test
    public void testConstructor_repeatedCalls_noSideEffects() {
        // Act
        _OptimizedTypeAdapterFactory instance1 = new _OptimizedTypeAdapterFactory();
        _OptimizedTypeAdapterFactory instance2 = new _OptimizedTypeAdapterFactory();
        _OptimizedTypeAdapterFactory instance3 = new _OptimizedTypeAdapterFactory();

        // Assert
        assertNotNull(instance1, "First instance should not be null");
        assertNotNull(instance2, "Second instance should not be null");
        assertNotNull(instance3, "Third instance should not be null");
    }

    /**
     * Tests that the created instance has the expected class name.
     */
    @Test
    public void testConstructor_createdInstanceHasCorrectClassName() {
        // Act
        _OptimizedTypeAdapterFactory instance = new _OptimizedTypeAdapterFactory();

        // Assert
        assertEquals("_OptimizedTypeAdapterFactory", instance.getClass().getSimpleName(),
            "Instance should have correct class name");
    }

    /**
     * Tests that the created instance is of the exact class type.
     */
    @Test
    public void testConstructor_createdInstanceIsExactType() {
        // Act
        _OptimizedTypeAdapterFactory instance = new _OptimizedTypeAdapterFactory();

        // Assert
        assertEquals(_OptimizedTypeAdapterFactory.class, instance.getClass(),
            "Instance should be of exact _OptimizedTypeAdapterFactory type");
    }

    /**
     * Tests that the constructor works in a multi-threaded context.
     * Creates instances from multiple threads concurrently.
     */
    @Test
    public void testConstructor_multiThreaded_succeeds() throws InterruptedException {
        // Arrange
        final int threadCount = 10;
        Thread[] threads = new Thread[threadCount];
        final _OptimizedTypeAdapterFactory[] instances = new _OptimizedTypeAdapterFactory[threadCount];

        // Act
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                instances[index] = new _OptimizedTypeAdapterFactory();
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert
        for (int i = 0; i < threadCount; i++) {
            assertNotNull(instances[i], "Instance " + i + " should not be null");
        }

        // Verify all instances are different
        for (int i = 0; i < threadCount; i++) {
            for (int j = i + 1; j < threadCount; j++) {
                assertNotSame(instances[i], instances[j],
                    "Instance " + i + " and " + j + " should be different");
            }
        }
    }

    /**
     * Tests that instances created by the constructor are unique objects.
     */
    @Test
    public void testConstructor_eachInstanceIsUnique() {
        // Act
        _OptimizedTypeAdapterFactory instance1 = new _OptimizedTypeAdapterFactory();
        _OptimizedTypeAdapterFactory instance2 = new _OptimizedTypeAdapterFactory();

        // Assert
        assertFalse(instance1.equals(instance2), "Different instances should not be equal");
        assertNotEquals(System.identityHashCode(instance1), System.identityHashCode(instance2),
            "Different instances should have different identity hash codes");
    }

    /**
     * Tests that the constructor works correctly when called via reflection.
     */
    @Test
    public void testConstructor_viaReflection_succeeds() throws Exception {
        // Act
        Object instance = _OptimizedTypeAdapterFactory.class.getDeclaredConstructor().newInstance();

        // Assert
        assertNotNull(instance, "Instance created via reflection should not be null");
        assertTrue(instance instanceof _OptimizedTypeAdapterFactory,
            "Instance should be of type _OptimizedTypeAdapterFactory");
        assertTrue(instance instanceof com.google.gson.TypeAdapterFactory,
            "Instance should implement TypeAdapterFactory interface");
    }

    // =========================================================================
    // Tests for method create: create.(Lcom/google/gson/Gson;Lcom/google/gson/reflect/TypeToken;)Lcom/google/gson/TypeAdapter;
    // =========================================================================

    /**
     * Tests that the create method returns null with valid Gson and TypeToken.
     * The template implementation always returns null as documented.
     */
    @Test
    public void testCreate_withValidGsonAndTypeToken_returnsNull() {
        // Arrange
        _OptimizedTypeAdapterFactory factory = new _OptimizedTypeAdapterFactory();
        Gson gson = new Gson();
        TypeToken<String> typeToken = TypeToken.get(String.class);

        // Act
        TypeAdapter<String> result = factory.create(gson, typeToken);

        // Assert
        assertNull(result, "Template create method should return null");
    }

    /**
     * Tests that the create method returns null for different types.
     */
    @Test
    public void testCreate_withDifferentTypes_returnsNull() {
        // Arrange
        _OptimizedTypeAdapterFactory factory = new _OptimizedTypeAdapterFactory();
        Gson gson = new Gson();

        // Act & Assert
        assertNull(factory.create(gson, TypeToken.get(String.class)), "Should return null for String");
        assertNull(factory.create(gson, TypeToken.get(Integer.class)), "Should return null for Integer");
        assertNull(factory.create(gson, TypeToken.get(Object.class)), "Should return null for Object");
        assertNull(factory.create(gson, TypeToken.get(Boolean.class)), "Should return null for Boolean");
    }

    /**
     * Tests that the create method can be called multiple times and always returns null.
     */
    @Test
    public void testCreate_calledMultipleTimes_alwaysReturnsNull() {
        // Arrange
        _OptimizedTypeAdapterFactory factory = new _OptimizedTypeAdapterFactory();
        Gson gson = new Gson();
        TypeToken<String> typeToken = TypeToken.get(String.class);

        // Act & Assert
        for (int i = 0; i < 5; i++) {
            assertNull(factory.create(gson, typeToken), "Call " + i + " should return null");
        }
    }

    /**
     * Tests that the create method works with null Gson parameter.
     * The template implementation doesn't validate parameters and just returns null.
     */
    @Test
    public void testCreate_withNullGson_returnsNull() {
        // Arrange
        _OptimizedTypeAdapterFactory factory = new _OptimizedTypeAdapterFactory();
        TypeToken<String> typeToken = TypeToken.get(String.class);

        // Act
        TypeAdapter<String> result = factory.create(null, typeToken);

        // Assert
        assertNull(result, "Should return null even with null Gson");
    }

    /**
     * Tests that the create method works with null TypeToken parameter.
     * The template implementation doesn't validate parameters and just returns null.
     */
    @Test
    public void testCreate_withNullTypeToken_returnsNull() {
        // Arrange
        _OptimizedTypeAdapterFactory factory = new _OptimizedTypeAdapterFactory();
        Gson gson = new Gson();

        // Act
        TypeAdapter result = factory.create(gson, null);

        // Assert
        assertNull(result, "Should return null even with null TypeToken");
    }

    /**
     * Tests that the create method works with both null parameters.
     * The template implementation doesn't validate parameters and just returns null.
     */
    @Test
    public void testCreate_withBothParametersNull_returnsNull() {
        // Arrange
        _OptimizedTypeAdapterFactory factory = new _OptimizedTypeAdapterFactory();

        // Act
        TypeAdapter result = factory.create(null, null);

        // Assert
        assertNull(result, "Should return null even with both parameters null");
    }

    /**
     * Tests that the create method can be called from multiple instances.
     */
    @Test
    public void testCreate_multipleInstances_allReturnNull() {
        // Arrange
        _OptimizedTypeAdapterFactory factory1 = new _OptimizedTypeAdapterFactory();
        _OptimizedTypeAdapterFactory factory2 = new _OptimizedTypeAdapterFactory();
        Gson gson = new Gson();
        TypeToken<String> typeToken = TypeToken.get(String.class);

        // Act
        TypeAdapter<String> result1 = factory1.create(gson, typeToken);
        TypeAdapter<String> result2 = factory2.create(gson, typeToken);

        // Assert
        assertNull(result1, "First instance should return null");
        assertNull(result2, "Second instance should return null");
    }

    /**
     * Tests that the create method works via the TypeAdapterFactory interface.
     */
    @Test
    public void testCreate_viaInterface_returnsNull() {
        // Arrange
        com.google.gson.TypeAdapterFactory factory = new _OptimizedTypeAdapterFactory();
        Gson gson = new Gson();
        TypeToken<String> typeToken = TypeToken.get(String.class);

        // Act
        TypeAdapter<String> result = factory.create(gson, typeToken);

        // Assert
        assertNull(result, "Should return null when called via interface");
    }

    /**
     * Tests that the create method works with custom types.
     */
    @Test
    public void testCreate_withCustomType_returnsNull() {
        // Arrange
        _OptimizedTypeAdapterFactory factory = new _OptimizedTypeAdapterFactory();
        Gson gson = new Gson();
        TypeToken<CustomTestClass> typeToken = TypeToken.get(CustomTestClass.class);

        // Act
        TypeAdapter<CustomTestClass> result = factory.create(gson, typeToken);

        // Assert
        assertNull(result, "Should return null for custom type");
    }

    /**
     * Tests that the create method works with generic types.
     */
    @Test
    public void testCreate_withGenericType_returnsNull() {
        // Arrange
        _OptimizedTypeAdapterFactory factory = new _OptimizedTypeAdapterFactory();
        Gson gson = new Gson();
        TypeToken<java.util.List<String>> typeToken = new TypeToken<java.util.List<String>>() {};

        // Act
        TypeAdapter<java.util.List<String>> result = factory.create(gson, typeToken);

        // Assert
        assertNull(result, "Should return null for generic type");
    }

    /**
     * Tests that the create method works with array types.
     */
    @Test
    public void testCreate_withArrayType_returnsNull() {
        // Arrange
        _OptimizedTypeAdapterFactory factory = new _OptimizedTypeAdapterFactory();
        Gson gson = new Gson();
        TypeToken<String[]> typeToken = TypeToken.get(String[].class);

        // Act
        TypeAdapter<String[]> result = factory.create(gson, typeToken);

        // Assert
        assertNull(result, "Should return null for array type");
    }

    /**
     * Tests that the create method works with primitive types.
     */
    @Test
    public void testCreate_withPrimitiveTypes_returnsNull() {
        // Arrange
        _OptimizedTypeAdapterFactory factory = new _OptimizedTypeAdapterFactory();
        Gson gson = new Gson();

        // Act & Assert
        assertNull(factory.create(gson, TypeToken.get(int.class)), "Should return null for int");
        assertNull(factory.create(gson, TypeToken.get(long.class)), "Should return null for long");
        assertNull(factory.create(gson, TypeToken.get(boolean.class)), "Should return null for boolean");
        assertNull(factory.create(gson, TypeToken.get(double.class)), "Should return null for double");
    }

    /**
     * Tests that the create method can be called in multi-threaded context.
     */
    @Test
    public void testCreate_multiThreaded_alwaysReturnsNull() throws InterruptedException {
        // Arrange
        final _OptimizedTypeAdapterFactory factory = new _OptimizedTypeAdapterFactory();
        final Gson gson = new Gson();
        final TypeToken<String> typeToken = TypeToken.get(String.class);
        final int threadCount = 10;
        Thread[] threads = new Thread[threadCount];
        final TypeAdapter<String>[] results = new TypeAdapter[threadCount];

        // Act
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                results[index] = factory.create(gson, typeToken);
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert
        for (int i = 0; i < threadCount; i++) {
            assertNull(results[i], "Result " + i + " should be null");
        }
    }

    /**
     * Tests that the factory can be used with a GsonBuilder even though it returns null.
     */
    @Test
    public void testCreate_withGsonBuilder_returnsNull() {
        // Arrange
        _OptimizedTypeAdapterFactory factory = new _OptimizedTypeAdapterFactory();
        Gson gson = new com.google.gson.GsonBuilder()
            .registerTypeAdapterFactory(factory)
            .create();
        TypeToken<String> typeToken = TypeToken.get(String.class);

        // Act
        TypeAdapter<String> result = factory.create(gson, typeToken);

        // Assert
        assertNull(result, "Should return null when registered with GsonBuilder");
    }

    /**
     * Tests that multiple calls with different Gson instances all return null.
     */
    @Test
    public void testCreate_withDifferentGsonInstances_returnsNull() {
        // Arrange
        _OptimizedTypeAdapterFactory factory = new _OptimizedTypeAdapterFactory();
        Gson gson1 = new Gson();
        Gson gson2 = new com.google.gson.GsonBuilder().create();
        TypeToken<String> typeToken = TypeToken.get(String.class);

        // Act
        TypeAdapter<String> result1 = factory.create(gson1, typeToken);
        TypeAdapter<String> result2 = factory.create(gson2, typeToken);

        // Assert
        assertNull(result1, "Should return null for first Gson instance");
        assertNull(result2, "Should return null for second Gson instance");
    }

    // Helper class for testing custom types
    private static class CustomTestClass {
        private String field;

        public CustomTestClass() {}

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }
    }
}
