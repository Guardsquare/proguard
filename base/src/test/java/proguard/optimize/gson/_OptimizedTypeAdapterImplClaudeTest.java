package proguard.optimize.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link _OptimizedTypeAdapterImpl}.
 * Tests all methods including constructor, write, and read.
 *
 * The _OptimizedTypeAdapterImpl class is a template for an optimized GSON type adapter.
 * The implementation of the write() and read() methods are expected to be replaced
 * with injected bytecode that invokes generated toJson$xxx() and fromJson$xxx() methods
 * on the appropriate domain class. In their template form, write() does nothing and
 * read() returns null.
 */
public class _OptimizedTypeAdapterImplClaudeTest {

    // =========================================================================
    // Tests for constructor: <init>.(Lcom/google/gson/Gson;Lproguard/optimize/gson/_OptimizedJsonReader;Lproguard/optimize/gson/_OptimizedJsonWriter;)V
    // =========================================================================

    /**
     * Tests that the constructor creates a non-null instance with valid parameters.
     */
    @Test
    public void testConstructor_withValidParameters_createsNonNullInstance() {
        // Arrange
        Gson gson = new Gson();
        _OptimizedJsonReader reader = new _OptimizedJsonReaderImpl();
        _OptimizedJsonWriter writer = new _OptimizedJsonWriterImpl();

        // Act
        _OptimizedTypeAdapterImpl instance = new _OptimizedTypeAdapterImpl(gson, reader, writer);

        // Assert
        assertNotNull(instance, "Constructor should create a non-null instance");
    }

    /**
     * Tests that the constructor creates an instance of the correct type.
     */
    @Test
    public void testConstructor_createsInstanceOfCorrectType() {
        // Arrange
        Gson gson = new Gson();
        _OptimizedJsonReader reader = new _OptimizedJsonReaderImpl();
        _OptimizedJsonWriter writer = new _OptimizedJsonWriterImpl();

        // Act
        _OptimizedTypeAdapterImpl instance = new _OptimizedTypeAdapterImpl(gson, reader, writer);

        // Assert
        assertTrue(instance instanceof TypeAdapter,
            "Instance should extend TypeAdapter");
        assertTrue(instance instanceof _OptimizedTypeAdapter,
            "Instance should implement _OptimizedTypeAdapter interface");
    }

    /**
     * Tests that the constructor accepts null Gson parameter.
     */
    @Test
    public void testConstructor_withNullGson_succeeds() {
        // Arrange
        _OptimizedJsonReader reader = new _OptimizedJsonReaderImpl();
        _OptimizedJsonWriter writer = new _OptimizedJsonWriterImpl();

        // Act
        _OptimizedTypeAdapterImpl instance = new _OptimizedTypeAdapterImpl(null, reader, writer);

        // Assert
        assertNotNull(instance, "Constructor should create instance even with null Gson");
    }

    /**
     * Tests that the constructor accepts null _OptimizedJsonReader parameter.
     */
    @Test
    public void testConstructor_withNullReader_succeeds() {
        // Arrange
        Gson gson = new Gson();
        _OptimizedJsonWriter writer = new _OptimizedJsonWriterImpl();

        // Act
        _OptimizedTypeAdapterImpl instance = new _OptimizedTypeAdapterImpl(gson, null, writer);

        // Assert
        assertNotNull(instance, "Constructor should create instance even with null reader");
    }

    /**
     * Tests that the constructor accepts null _OptimizedJsonWriter parameter.
     */
    @Test
    public void testConstructor_withNullWriter_succeeds() {
        // Arrange
        Gson gson = new Gson();
        _OptimizedJsonReader reader = new _OptimizedJsonReaderImpl();

        // Act
        _OptimizedTypeAdapterImpl instance = new _OptimizedTypeAdapterImpl(gson, reader, null);

        // Assert
        assertNotNull(instance, "Constructor should create instance even with null writer");
    }

    /**
     * Tests that the constructor accepts all null parameters.
     */
    @Test
    public void testConstructor_withAllNullParameters_succeeds() {
        // Act
        _OptimizedTypeAdapterImpl instance = new _OptimizedTypeAdapterImpl(null, null, null);

        // Assert
        assertNotNull(instance, "Constructor should create instance even with all null parameters");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_multipleInstances_areIndependent() {
        // Arrange
        Gson gson = new Gson();
        _OptimizedJsonReader reader = new _OptimizedJsonReaderImpl();
        _OptimizedJsonWriter writer = new _OptimizedJsonWriterImpl();

        // Act
        _OptimizedTypeAdapterImpl instance1 = new _OptimizedTypeAdapterImpl(gson, reader, writer);
        _OptimizedTypeAdapterImpl instance2 = new _OptimizedTypeAdapterImpl(gson, reader, writer);

        // Assert
        assertNotNull(instance1, "First instance should not be null");
        assertNotNull(instance2, "Second instance should not be null");
        assertNotSame(instance1, instance2, "Instances should be different objects");
    }

    /**
     * Tests that instances created with different Gson instances are distinct.
     */
    @Test
    public void testConstructor_withDifferentGsonInstances_createsDistinctInstances() {
        // Arrange
        Gson gson1 = new Gson();
        Gson gson2 = new Gson();
        _OptimizedJsonReader reader = new _OptimizedJsonReaderImpl();
        _OptimizedJsonWriter writer = new _OptimizedJsonWriterImpl();

        // Act
        _OptimizedTypeAdapterImpl instance1 = new _OptimizedTypeAdapterImpl(gson1, reader, writer);
        _OptimizedTypeAdapterImpl instance2 = new _OptimizedTypeAdapterImpl(gson2, reader, writer);

        // Assert
        assertNotSame(instance1, instance2, "Instances with different Gson objects should be distinct");
    }

    /**
     * Tests that instances created with different reader instances are distinct.
     */
    @Test
    public void testConstructor_withDifferentReaders_createsDistinctInstances() {
        // Arrange
        Gson gson = new Gson();
        _OptimizedJsonReader reader1 = new _OptimizedJsonReaderImpl();
        _OptimizedJsonReader reader2 = new _OptimizedJsonReaderImpl();
        _OptimizedJsonWriter writer = new _OptimizedJsonWriterImpl();

        // Act
        _OptimizedTypeAdapterImpl instance1 = new _OptimizedTypeAdapterImpl(gson, reader1, writer);
        _OptimizedTypeAdapterImpl instance2 = new _OptimizedTypeAdapterImpl(gson, reader2, writer);

        // Assert
        assertNotSame(instance1, instance2, "Instances with different readers should be distinct");
    }

    /**
     * Tests that instances created with different writer instances are distinct.
     */
    @Test
    public void testConstructor_withDifferentWriters_createsDistinctInstances() {
        // Arrange
        Gson gson = new Gson();
        _OptimizedJsonReader reader = new _OptimizedJsonReaderImpl();
        _OptimizedJsonWriter writer1 = new _OptimizedJsonWriterImpl();
        _OptimizedJsonWriter writer2 = new _OptimizedJsonWriterImpl();

        // Act
        _OptimizedTypeAdapterImpl instance1 = new _OptimizedTypeAdapterImpl(gson, reader, writer1);
        _OptimizedTypeAdapterImpl instance2 = new _OptimizedTypeAdapterImpl(gson, reader, writer2);

        // Assert
        assertNotSame(instance1, instance2, "Instances with different writers should be distinct");
    }

    /**
     * Tests that the constructor can be called repeatedly without side effects.
     */
    @Test
    public void testConstructor_repeatedCalls_noSideEffects() {
        // Arrange
        Gson gson = new Gson();
        _OptimizedJsonReader reader = new _OptimizedJsonReaderImpl();
        _OptimizedJsonWriter writer = new _OptimizedJsonWriterImpl();

        // Act
        _OptimizedTypeAdapterImpl instance1 = new _OptimizedTypeAdapterImpl(gson, reader, writer);
        _OptimizedTypeAdapterImpl instance2 = new _OptimizedTypeAdapterImpl(gson, reader, writer);
        _OptimizedTypeAdapterImpl instance3 = new _OptimizedTypeAdapterImpl(gson, reader, writer);

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
        // Arrange
        Gson gson = new Gson();
        _OptimizedJsonReader reader = new _OptimizedJsonReaderImpl();
        _OptimizedJsonWriter writer = new _OptimizedJsonWriterImpl();

        // Act
        _OptimizedTypeAdapterImpl instance = new _OptimizedTypeAdapterImpl(gson, reader, writer);

        // Assert
        assertEquals("_OptimizedTypeAdapterImpl", instance.getClass().getSimpleName(),
            "Instance should have correct class name");
    }

    /**
     * Tests that the created instance is of the exact class type.
     */
    @Test
    public void testConstructor_createdInstanceIsExactType() {
        // Arrange
        Gson gson = new Gson();
        _OptimizedJsonReader reader = new _OptimizedJsonReaderImpl();
        _OptimizedJsonWriter writer = new _OptimizedJsonWriterImpl();

        // Act
        _OptimizedTypeAdapterImpl instance = new _OptimizedTypeAdapterImpl(gson, reader, writer);

        // Assert
        assertEquals(_OptimizedTypeAdapterImpl.class, instance.getClass(),
            "Instance should be of exact _OptimizedTypeAdapterImpl type");
    }

    /**
     * Tests that the constructor works in a multi-threaded context.
     */
    @Test
    public void testConstructor_multiThreaded_succeeds() throws InterruptedException {
        // Arrange
        final Gson gson = new Gson();
        final _OptimizedJsonReader reader = new _OptimizedJsonReaderImpl();
        final _OptimizedJsonWriter writer = new _OptimizedJsonWriterImpl();
        final int threadCount = 10;
        Thread[] threads = new Thread[threadCount];
        final _OptimizedTypeAdapterImpl[] instances = new _OptimizedTypeAdapterImpl[threadCount];

        // Act
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                instances[index] = new _OptimizedTypeAdapterImpl(gson, reader, writer);
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
     * Tests that the constructor works correctly when called via reflection.
     */
    @Test
    public void testConstructor_viaReflection_succeeds() throws Exception {
        // Arrange
        Gson gson = new Gson();
        _OptimizedJsonReader reader = new _OptimizedJsonReaderImpl();
        _OptimizedJsonWriter writer = new _OptimizedJsonWriterImpl();

        // Act
        Object instance = _OptimizedTypeAdapterImpl.class
            .getDeclaredConstructor(Gson.class, _OptimizedJsonReader.class, _OptimizedJsonWriter.class)
            .newInstance(gson, reader, writer);

        // Assert
        assertNotNull(instance, "Instance created via reflection should not be null");
        assertTrue(instance instanceof _OptimizedTypeAdapterImpl,
            "Instance should be of type _OptimizedTypeAdapterImpl");
        assertTrue(instance instanceof TypeAdapter,
            "Instance should extend TypeAdapter");
        assertTrue(instance instanceof _OptimizedTypeAdapter,
            "Instance should implement _OptimizedTypeAdapter interface");
    }

    /**
     * Tests that instances are unique objects.
     */
    @Test
    public void testConstructor_eachInstanceIsUnique() {
        // Arrange
        Gson gson = new Gson();
        _OptimizedJsonReader reader = new _OptimizedJsonReaderImpl();
        _OptimizedJsonWriter writer = new _OptimizedJsonWriterImpl();

        // Act
        _OptimizedTypeAdapterImpl instance1 = new _OptimizedTypeAdapterImpl(gson, reader, writer);
        _OptimizedTypeAdapterImpl instance2 = new _OptimizedTypeAdapterImpl(gson, reader, writer);

        // Assert
        assertNotEquals(System.identityHashCode(instance1), System.identityHashCode(instance2),
            "Different instances should have different identity hash codes");
    }

    // =========================================================================
    // Tests for method write: write.(Lcom/google/gson/stream/JsonWriter;Ljava/lang/Object;)V
    // =========================================================================

    /**
     * Tests that write method does nothing with valid parameters.
     * The template implementation has an empty body.
     */
    @Test
    public void testWrite_withValidParameters_doesNothing() throws IOException {
        // Arrange
        Gson gson = new Gson();
        _OptimizedJsonReader reader = new _OptimizedJsonReaderImpl();
        _OptimizedJsonWriter writer = new _OptimizedJsonWriterImpl();
        _OptimizedTypeAdapterImpl adapter = new _OptimizedTypeAdapterImpl(gson, reader, writer);

        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        Object value = new Object();

        // Act
        adapter.write(jsonWriter, value);

        // Assert - no exception thrown and no output written
        assertEquals("", stringWriter.toString(), "Write method should not write anything");
    }

    /**
     * Tests that write method accepts null JsonWriter.
     * The empty implementation should not throw any exception.
     */
    @Test
    public void testWrite_withNullJsonWriter_doesNothing() throws IOException {
        // Arrange
        Gson gson = new Gson();
        _OptimizedJsonReader reader = new _OptimizedJsonReaderImpl();
        _OptimizedJsonWriter writer = new _OptimizedJsonWriterImpl();
        _OptimizedTypeAdapterImpl adapter = new _OptimizedTypeAdapterImpl(gson, reader, writer);

        // Act & Assert - should not throw exception
        adapter.write(null, new Object());
    }

    /**
     * Tests that write method accepts null value.
     * The empty implementation should not throw any exception.
     */
    @Test
    public void testWrite_withNullValue_doesNothing() throws IOException {
        // Arrange
        Gson gson = new Gson();
        _OptimizedJsonReader reader = new _OptimizedJsonReaderImpl();
        _OptimizedJsonWriter writer = new _OptimizedJsonWriterImpl();
        _OptimizedTypeAdapterImpl adapter = new _OptimizedTypeAdapterImpl(gson, reader, writer);

        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        // Act
        adapter.write(jsonWriter, null);

        // Assert - no exception thrown
        assertEquals("", stringWriter.toString(), "Write method should not write anything");
    }

    /**
     * Tests that write method accepts both null parameters.
     */
    @Test
    public void testWrite_withBothNullParameters_doesNothing() throws IOException {
        // Arrange
        Gson gson = new Gson();
        _OptimizedJsonReader reader = new _OptimizedJsonReaderImpl();
        _OptimizedJsonWriter writer = new _OptimizedJsonWriterImpl();
        _OptimizedTypeAdapterImpl adapter = new _OptimizedTypeAdapterImpl(gson, reader, writer);

        // Act & Assert - should not throw exception
        adapter.write(null, null);
    }

    /**
     * Tests that write method can be called multiple times without side effects.
     */
    @Test
    public void testWrite_calledMultipleTimes_noSideEffects() throws IOException {
        // Arrange
        Gson gson = new Gson();
        _OptimizedJsonReader reader = new _OptimizedJsonReaderImpl();
        _OptimizedJsonWriter writer = new _OptimizedJsonWriterImpl();
        _OptimizedTypeAdapterImpl adapter = new _OptimizedTypeAdapterImpl(gson, reader, writer);

        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        Object value = "test";

        // Act
        adapter.write(jsonWriter, value);
        adapter.write(jsonWriter, value);
        adapter.write(jsonWriter, value);

        // Assert
        assertEquals("", stringWriter.toString(), "Multiple write calls should not write anything");
    }

    /**
     * Tests that write method works with different types of values.
     */
    @Test
    public void testWrite_withDifferentValueTypes_doesNothing() throws IOException {
        // Arrange
        Gson gson = new Gson();
        _OptimizedJsonReader reader = new _OptimizedJsonReaderImpl();
        _OptimizedJsonWriter writer = new _OptimizedJsonWriterImpl();
        _OptimizedTypeAdapterImpl adapter = new _OptimizedTypeAdapterImpl(gson, reader, writer);

        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        // Act
        adapter.write(jsonWriter, "string");
        adapter.write(jsonWriter, 42);
        adapter.write(jsonWriter, true);
        adapter.write(jsonWriter, 3.14);
        adapter.write(jsonWriter, new Object());

        // Assert
        assertEquals("", stringWriter.toString(), "Write method should not write anything for any type");
    }

    /**
     * Tests that write method can be called via TypeAdapter interface.
     */
    @Test
    public void testWrite_viaTypeAdapterInterface_doesNothing() throws IOException {
        // Arrange
        Gson gson = new Gson();
        _OptimizedJsonReader reader = new _OptimizedJsonReaderImpl();
        _OptimizedJsonWriter writer = new _OptimizedJsonWriterImpl();
        TypeAdapter adapter = new _OptimizedTypeAdapterImpl(gson, reader, writer);

        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        // Act
        adapter.write(jsonWriter, "test");

        // Assert
        assertEquals("", stringWriter.toString(), "Write via interface should not write anything");
    }

    /**
     * Tests that write method from multiple instances behaves the same way.
     */
    @Test
    public void testWrite_multipleInstances_behaveSimilarly() throws IOException {
        // Arrange
        Gson gson = new Gson();
        _OptimizedJsonReader reader = new _OptimizedJsonReaderImpl();
        _OptimizedJsonWriter writer = new _OptimizedJsonWriterImpl();
        _OptimizedTypeAdapterImpl adapter1 = new _OptimizedTypeAdapterImpl(gson, reader, writer);
        _OptimizedTypeAdapterImpl adapter2 = new _OptimizedTypeAdapterImpl(gson, reader, writer);

        StringWriter stringWriter1 = new StringWriter();
        JsonWriter jsonWriter1 = new JsonWriter(stringWriter1);
        StringWriter stringWriter2 = new StringWriter();
        JsonWriter jsonWriter2 = new JsonWriter(stringWriter2);

        // Act
        adapter1.write(jsonWriter1, "test");
        adapter2.write(jsonWriter2, "test");

        // Assert
        assertEquals(stringWriter1.toString(), stringWriter2.toString(),
            "Different instances should behave the same way");
    }

    /**
     * Tests that write method can be called in multi-threaded context.
     */
    @Test
    public void testWrite_multiThreaded_succeeds() throws InterruptedException {
        // Arrange
        final Gson gson = new Gson();
        final _OptimizedJsonReader reader = new _OptimizedJsonReaderImpl();
        final _OptimizedJsonWriter writer = new _OptimizedJsonWriterImpl();
        final _OptimizedTypeAdapterImpl adapter = new _OptimizedTypeAdapterImpl(gson, reader, writer);
        final int threadCount = 10;
        Thread[] threads = new Thread[threadCount];
        final boolean[] success = new boolean[threadCount];

        // Act
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                try {
                    StringWriter stringWriter = new StringWriter();
                    JsonWriter jsonWriter = new JsonWriter(stringWriter);
                    adapter.write(jsonWriter, "test");
                    success[index] = true;
                } catch (IOException e) {
                    success[index] = false;
                }
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert
        for (int i = 0; i < threadCount; i++) {
            assertTrue(success[i], "Thread " + i + " should complete successfully");
        }
    }

    /**
     * Tests that write method does not modify the JsonWriter state.
     */
    @Test
    public void testWrite_doesNotModifyJsonWriterState() throws IOException {
        // Arrange
        Gson gson = new Gson();
        _OptimizedJsonReader reader = new _OptimizedJsonReaderImpl();
        _OptimizedJsonWriter writer = new _OptimizedJsonWriterImpl();
        _OptimizedTypeAdapterImpl adapter = new _OptimizedTypeAdapterImpl(gson, reader, writer);

        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        jsonWriter.beginObject();
        String beforeWrite = stringWriter.toString();

        // Act
        adapter.write(jsonWriter, "test");
        String afterWrite = stringWriter.toString();

        // Assert
        assertEquals(beforeWrite, afterWrite, "Write method should not modify JsonWriter state");
    }

    // =========================================================================
    // Tests for method read: read.(Lcom/google/gson/stream/JsonReader;)Ljava/lang/Object;
    // =========================================================================

    /**
     * Tests that read method returns null with valid JsonReader.
     * The template implementation always returns null.
     */
    @Test
    public void testRead_withValidJsonReader_returnsNull() throws IOException {
        // Arrange
        Gson gson = new Gson();
        _OptimizedJsonReader reader = new _OptimizedJsonReaderImpl();
        _OptimizedJsonWriter writer = new _OptimizedJsonWriterImpl();
        _OptimizedTypeAdapterImpl adapter = new _OptimizedTypeAdapterImpl(gson, reader, writer);

        StringReader stringReader = new StringReader("{}");
        JsonReader jsonReader = new JsonReader(stringReader);

        // Act
        Object result = adapter.read(jsonReader);

        // Assert
        assertNull(result, "Template read method should return null");
    }

    /**
     * Tests that read method returns null with null JsonReader.
     */
    @Test
    public void testRead_withNullJsonReader_returnsNull() throws IOException {
        // Arrange
        Gson gson = new Gson();
        _OptimizedJsonReader reader = new _OptimizedJsonReaderImpl();
        _OptimizedJsonWriter writer = new _OptimizedJsonWriterImpl();
        _OptimizedTypeAdapterImpl adapter = new _OptimizedTypeAdapterImpl(gson, reader, writer);

        // Act
        Object result = adapter.read(null);

        // Assert
        assertNull(result, "Read method should return null even with null JsonReader");
    }

    /**
     * Tests that read method can be called multiple times and always returns null.
     */
    @Test
    public void testRead_calledMultipleTimes_alwaysReturnsNull() throws IOException {
        // Arrange
        Gson gson = new Gson();
        _OptimizedJsonReader reader = new _OptimizedJsonReaderImpl();
        _OptimizedJsonWriter writer = new _OptimizedJsonWriterImpl();
        _OptimizedTypeAdapterImpl adapter = new _OptimizedTypeAdapterImpl(gson, reader, writer);

        StringReader stringReader = new StringReader("{}");
        JsonReader jsonReader = new JsonReader(stringReader);

        // Act & Assert
        for (int i = 0; i < 5; i++) {
            assertNull(adapter.read(jsonReader), "Call " + i + " should return null");
        }
    }

    /**
     * Tests that read method works with different JSON inputs.
     */
    @Test
    public void testRead_withDifferentJsonInputs_alwaysReturnsNull() throws IOException {
        // Arrange
        Gson gson = new Gson();
        _OptimizedJsonReader reader = new _OptimizedJsonReaderImpl();
        _OptimizedJsonWriter writer = new _OptimizedJsonWriterImpl();
        _OptimizedTypeAdapterImpl adapter = new _OptimizedTypeAdapterImpl(gson, reader, writer);

        // Act & Assert
        assertNull(adapter.read(new JsonReader(new StringReader("{}"))),
            "Should return null for empty object");
        assertNull(adapter.read(new JsonReader(new StringReader("[]"))),
            "Should return null for empty array");
        assertNull(adapter.read(new JsonReader(new StringReader("\"string\""))),
            "Should return null for string");
        assertNull(adapter.read(new JsonReader(new StringReader("42"))),
            "Should return null for number");
        assertNull(adapter.read(new JsonReader(new StringReader("true"))),
            "Should return null for boolean");
        assertNull(adapter.read(new JsonReader(new StringReader("null"))),
            "Should return null for null");
    }

    /**
     * Tests that read method can be called via TypeAdapter interface.
     */
    @Test
    public void testRead_viaTypeAdapterInterface_returnsNull() throws IOException {
        // Arrange
        Gson gson = new Gson();
        _OptimizedJsonReader reader = new _OptimizedJsonReaderImpl();
        _OptimizedJsonWriter writer = new _OptimizedJsonWriterImpl();
        TypeAdapter adapter = new _OptimizedTypeAdapterImpl(gson, reader, writer);

        StringReader stringReader = new StringReader("{}");
        JsonReader jsonReader = new JsonReader(stringReader);

        // Act
        Object result = adapter.read(jsonReader);

        // Assert
        assertNull(result, "Read via interface should return null");
    }

    /**
     * Tests that read method from multiple instances behaves the same way.
     */
    @Test
    public void testRead_multipleInstances_allReturnNull() throws IOException {
        // Arrange
        Gson gson = new Gson();
        _OptimizedJsonReader reader = new _OptimizedJsonReaderImpl();
        _OptimizedJsonWriter writer = new _OptimizedJsonWriterImpl();
        _OptimizedTypeAdapterImpl adapter1 = new _OptimizedTypeAdapterImpl(gson, reader, writer);
        _OptimizedTypeAdapterImpl adapter2 = new _OptimizedTypeAdapterImpl(gson, reader, writer);

        JsonReader jsonReader1 = new JsonReader(new StringReader("{}"));
        JsonReader jsonReader2 = new JsonReader(new StringReader("{}"));

        // Act
        Object result1 = adapter1.read(jsonReader1);
        Object result2 = adapter2.read(jsonReader2);

        // Assert
        assertNull(result1, "First instance should return null");
        assertNull(result2, "Second instance should return null");
    }

    /**
     * Tests that read method can be called in multi-threaded context.
     */
    @Test
    public void testRead_multiThreaded_alwaysReturnsNull() throws InterruptedException {
        // Arrange
        final Gson gson = new Gson();
        final _OptimizedJsonReader reader = new _OptimizedJsonReaderImpl();
        final _OptimizedJsonWriter writer = new _OptimizedJsonWriterImpl();
        final _OptimizedTypeAdapterImpl adapter = new _OptimizedTypeAdapterImpl(gson, reader, writer);
        final int threadCount = 10;
        Thread[] threads = new Thread[threadCount];
        final Object[] results = new Object[threadCount];

        // Act
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                try {
                    JsonReader jsonReader = new JsonReader(new StringReader("{}"));
                    results[index] = adapter.read(jsonReader);
                } catch (IOException e) {
                    results[index] = e;
                }
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
     * Tests that read method does not consume input from JsonReader.
     */
    @Test
    public void testRead_doesNotConsumeInput() throws IOException {
        // Arrange
        Gson gson = new Gson();
        _OptimizedJsonReader reader = new _OptimizedJsonReaderImpl();
        _OptimizedJsonWriter writer = new _OptimizedJsonWriterImpl();
        _OptimizedTypeAdapterImpl adapter = new _OptimizedTypeAdapterImpl(gson, reader, writer);

        String json = "{\"key\":\"value\"}";
        JsonReader jsonReader = new JsonReader(new StringReader(json));

        // Act
        Object result = adapter.read(jsonReader);

        // Assert
        assertNull(result, "Should return null");
        // The JsonReader should still be at the start since read() doesn't consume anything
    }

    /**
     * Tests that read method works with complex JSON structures.
     */
    @Test
    public void testRead_withComplexJson_returnsNull() throws IOException {
        // Arrange
        Gson gson = new Gson();
        _OptimizedJsonReader reader = new _OptimizedJsonReaderImpl();
        _OptimizedJsonWriter writer = new _OptimizedJsonWriterImpl();
        _OptimizedTypeAdapterImpl adapter = new _OptimizedTypeAdapterImpl(gson, reader, writer);

        String complexJson = "{\"name\":\"test\",\"nested\":{\"value\":42},\"array\":[1,2,3]}";
        JsonReader jsonReader = new JsonReader(new StringReader(complexJson));

        // Act
        Object result = adapter.read(jsonReader);

        // Assert
        assertNull(result, "Should return null even for complex JSON");
    }

    // =========================================================================
    // Combined tests for write and read methods
    // =========================================================================

    /**
     * Tests that write and read can be called sequentially.
     */
    @Test
    public void testWriteAndRead_calledSequentially_bothWork() throws IOException {
        // Arrange
        Gson gson = new Gson();
        _OptimizedJsonReader reader = new _OptimizedJsonReaderImpl();
        _OptimizedJsonWriter writer = new _OptimizedJsonWriterImpl();
        _OptimizedTypeAdapterImpl adapter = new _OptimizedTypeAdapterImpl(gson, reader, writer);

        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        JsonReader jsonReader = new JsonReader(new StringReader("{}"));

        // Act
        adapter.write(jsonWriter, "test");
        Object result = adapter.read(jsonReader);

        // Assert
        assertEquals("", stringWriter.toString(), "Write should not write anything");
        assertNull(result, "Read should return null");
    }

    /**
     * Tests that the adapter can be used as a TypeAdapter in standard GSON workflow.
     */
    @Test
    public void testAdapter_usedAsTypeAdapter_behavesCorrectly() throws IOException {
        // Arrange
        Gson gson = new Gson();
        _OptimizedJsonReader reader = new _OptimizedJsonReaderImpl();
        _OptimizedJsonWriter writer = new _OptimizedJsonWriterImpl();
        TypeAdapter adapter = new _OptimizedTypeAdapterImpl(gson, reader, writer);

        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        JsonReader jsonReader = new JsonReader(new StringReader("{}"));

        // Act
        adapter.write(jsonWriter, "test");
        Object readResult = adapter.read(jsonReader);

        // Assert
        assertEquals("", stringWriter.toString(), "Write should not write anything");
        assertNull(readResult, "Read should return null");
    }

    /**
     * Tests that instance created with all null parameters can still call write and read.
     */
    @Test
    public void testWriteAndRead_withNullConstructorParameters_work() throws IOException {
        // Arrange
        _OptimizedTypeAdapterImpl adapter = new _OptimizedTypeAdapterImpl(null, null, null);

        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        JsonReader jsonReader = new JsonReader(new StringReader("{}"));

        // Act
        adapter.write(jsonWriter, "test");
        Object result = adapter.read(jsonReader);

        // Assert
        assertEquals("", stringWriter.toString(), "Write should not write anything");
        assertNull(result, "Read should return null");
    }
}
