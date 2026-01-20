package proguard.optimize.gson;

import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link _OptimizedJsonWriterImpl}.
 * Tests all methods including constructor, b (name method), and c (value method).
 *
 * The _OptimizedJsonWriterImpl class is a template for an optimized JSON writer implementation.
 * It contains a static String array that maps internal indices to JSON field names/values.
 * The static array is initialized to null by the static initializer method, and is expected
 * to be populated via bytecode injection at runtime.
 */
public class _OptimizedJsonWriterImplClaudeTest {

    // =========================================================================
    // Tests for constructor: <init>.()V
    // =========================================================================

    /**
     * Tests that the constructor creates a non-null instance.
     */
    @Test
    public void testConstructor_createsNonNullInstance() {
        // Act
        _OptimizedJsonWriterImpl instance = new _OptimizedJsonWriterImpl();

        // Assert
        assertNotNull(instance, "Constructor should create a non-null instance");
    }

    /**
     * Tests that the constructor creates an instance implementing the _OptimizedJsonWriter interface.
     */
    @Test
    public void testConstructor_createsInstanceOfCorrectType() {
        // Act
        _OptimizedJsonWriterImpl instance = new _OptimizedJsonWriterImpl();

        // Assert
        assertTrue(instance instanceof _OptimizedJsonWriter,
            "Instance should implement _OptimizedJsonWriter interface");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_multipleInstances_areIndependent() {
        // Act
        _OptimizedJsonWriterImpl instance1 = new _OptimizedJsonWriterImpl();
        _OptimizedJsonWriterImpl instance2 = new _OptimizedJsonWriterImpl();

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
            _OptimizedJsonWriterImpl instance = new _OptimizedJsonWriterImpl();
            assertNotNull(instance, "Instance " + i + " should not be null");
        }
    }

    /**
     * Tests that the constructor can be called repeatedly without side effects.
     */
    @Test
    public void testConstructor_repeatedCalls_noSideEffects() {
        // Act
        _OptimizedJsonWriterImpl instance1 = new _OptimizedJsonWriterImpl();
        _OptimizedJsonWriterImpl instance2 = new _OptimizedJsonWriterImpl();
        _OptimizedJsonWriterImpl instance3 = new _OptimizedJsonWriterImpl();

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
        _OptimizedJsonWriterImpl instance = new _OptimizedJsonWriterImpl();

        // Assert
        assertEquals("_OptimizedJsonWriterImpl", instance.getClass().getSimpleName(),
            "Instance should have correct class name");
    }

    /**
     * Tests that the created instance is of the exact class type.
     */
    @Test
    public void testConstructor_createdInstanceIsExactType() {
        // Act
        _OptimizedJsonWriterImpl instance = new _OptimizedJsonWriterImpl();

        // Assert
        assertEquals(_OptimizedJsonWriterImpl.class, instance.getClass(),
            "Instance should be of exact _OptimizedJsonWriterImpl type");
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
        final _OptimizedJsonWriterImpl[] instances = new _OptimizedJsonWriterImpl[threadCount];

        // Act
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                instances[index] = new _OptimizedJsonWriterImpl();
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
        _OptimizedJsonWriterImpl instance1 = new _OptimizedJsonWriterImpl();
        _OptimizedJsonWriterImpl instance2 = new _OptimizedJsonWriterImpl();

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
        Object instance = _OptimizedJsonWriterImpl.class.getDeclaredConstructor().newInstance();

        // Assert
        assertNotNull(instance, "Instance created via reflection should not be null");
        assertTrue(instance instanceof _OptimizedJsonWriterImpl,
            "Instance should be of type _OptimizedJsonWriterImpl");
        assertTrue(instance instanceof _OptimizedJsonWriter,
            "Instance should implement _OptimizedJsonWriter interface");
    }

    // =========================================================================
    // Tests for method b: b.(Lcom/google/gson/stream/JsonWriter;I)V
    // =========================================================================

    /**
     * Tests that method b throws NullPointerException when the static array is null.
     * The static array 'a' is initialized to null by default, so accessing it will cause NPE.
     */
    @Test
    public void testMethodB_withNullStaticArray_throwsNullPointerException() {
        // Arrange
        _OptimizedJsonWriterImpl instance = new _OptimizedJsonWriterImpl();
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            instance.b(jsonWriter, 0);
        }, "Should throw NullPointerException when static array is null");
    }

    /**
     * Tests that method b throws NullPointerException with null JsonWriter parameter.
     */
    @Test
    public void testMethodB_withNullJsonWriter_throwsNullPointerException() {
        // Arrange
        _OptimizedJsonWriterImpl instance = new _OptimizedJsonWriterImpl();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            instance.b(null, 0);
        }, "Should throw NullPointerException when jsonWriter is null");
    }

    /**
     * Tests that method b attempts to access the static array at the given index.
     * Since the array is null, it will throw NullPointerException regardless of index value.
     */
    @Test
    public void testMethodB_withPositiveIndex_throwsNullPointerException() {
        // Arrange
        _OptimizedJsonWriterImpl instance = new _OptimizedJsonWriterImpl();
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            instance.b(jsonWriter, 5);
        }, "Should throw NullPointerException when accessing null array");
    }

    /**
     * Tests that method b with negative index also results in NullPointerException.
     * The NPE occurs before any array bounds checking because the array itself is null.
     */
    @Test
    public void testMethodB_withNegativeIndex_throwsNullPointerException() {
        // Arrange
        _OptimizedJsonWriterImpl instance = new _OptimizedJsonWriterImpl();
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            instance.b(jsonWriter, -1);
        }, "Should throw NullPointerException when accessing null array");
    }

    /**
     * Tests that method b with zero index throws NullPointerException.
     */
    @Test
    public void testMethodB_withZeroIndex_throwsNullPointerException() {
        // Arrange
        _OptimizedJsonWriterImpl instance = new _OptimizedJsonWriterImpl();
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            instance.b(jsonWriter, 0);
        }, "Should throw NullPointerException when accessing null array at index 0");
    }

    /**
     * Tests that method b with large index value throws NullPointerException.
     */
    @Test
    public void testMethodB_withLargeIndex_throwsNullPointerException() {
        // Arrange
        _OptimizedJsonWriterImpl instance = new _OptimizedJsonWriterImpl();
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            instance.b(jsonWriter, Integer.MAX_VALUE);
        }, "Should throw NullPointerException when accessing null array with large index");
    }

    /**
     * Tests that method b can be called from multiple instances with same parameters.
     * All calls should throw NullPointerException due to null static array.
     */
    @Test
    public void testMethodB_multipleInstances_allThrowNullPointerException() {
        // Arrange
        _OptimizedJsonWriterImpl instance1 = new _OptimizedJsonWriterImpl();
        _OptimizedJsonWriterImpl instance2 = new _OptimizedJsonWriterImpl();
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            instance1.b(jsonWriter, 0);
        }, "First instance should throw NullPointerException");

        assertThrows(NullPointerException.class, () -> {
            instance2.b(jsonWriter, 0);
        }, "Second instance should throw NullPointerException");
    }

    /**
     * Tests that method b fails before writing anything when array is null.
     */
    @Test
    public void testMethodB_withNullArray_doesNotWriteToJsonWriter() {
        // Arrange
        _OptimizedJsonWriterImpl instance = new _OptimizedJsonWriterImpl();
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        // Act
        try {
            instance.b(jsonWriter, 0);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException | IOException e) {
            // Expected
        }

        // Assert
        assertEquals("", stringWriter.toString(),
            "No output should be written when NullPointerException occurs");
    }

    /**
     * Tests that method b with different instances and different indices all throw NPE.
     */
    @Test
    public void testMethodB_variousIndices_allThrowNullPointerException() {
        // Arrange
        _OptimizedJsonWriterImpl instance = new _OptimizedJsonWriterImpl();
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        int[] testIndices = {0, 1, 10, 100, -1, -10, Integer.MIN_VALUE, Integer.MAX_VALUE};

        // Act & Assert
        for (int index : testIndices) {
            assertThrows(NullPointerException.class, () -> {
                instance.b(jsonWriter, index);
            }, "Should throw NullPointerException for index: " + index);
        }
    }

    /**
     * Tests that method b from the _OptimizedJsonWriter interface can be called.
     */
    @Test
    public void testMethodB_viaInterface_throwsNullPointerException() {
        // Arrange
        _OptimizedJsonWriter instance = new _OptimizedJsonWriterImpl();
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            instance.b(jsonWriter, 0);
        }, "Should throw NullPointerException when called via interface");
    }

    // =========================================================================
    // Tests for method c: c.(Lcom/google/gson/stream/JsonWriter;I)V
    // =========================================================================

    /**
     * Tests that method c throws NullPointerException when the static array is null.
     * The static array 'a' is initialized to null by default, so accessing it will cause NPE.
     */
    @Test
    public void testMethodC_withNullStaticArray_throwsNullPointerException() {
        // Arrange
        _OptimizedJsonWriterImpl instance = new _OptimizedJsonWriterImpl();
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            instance.c(jsonWriter, 0);
        }, "Should throw NullPointerException when static array is null");
    }

    /**
     * Tests that method c throws NullPointerException with null JsonWriter parameter.
     */
    @Test
    public void testMethodC_withNullJsonWriter_throwsNullPointerException() {
        // Arrange
        _OptimizedJsonWriterImpl instance = new _OptimizedJsonWriterImpl();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            instance.c(null, 0);
        }, "Should throw NullPointerException when jsonWriter is null");
    }

    /**
     * Tests that method c attempts to access the static array at the given index.
     * Since the array is null, it will throw NullPointerException regardless of index value.
     */
    @Test
    public void testMethodC_withPositiveIndex_throwsNullPointerException() {
        // Arrange
        _OptimizedJsonWriterImpl instance = new _OptimizedJsonWriterImpl();
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            instance.c(jsonWriter, 5);
        }, "Should throw NullPointerException when accessing null array");
    }

    /**
     * Tests that method c with negative index also results in NullPointerException.
     * The NPE occurs before any array bounds checking because the array itself is null.
     */
    @Test
    public void testMethodC_withNegativeIndex_throwsNullPointerException() {
        // Arrange
        _OptimizedJsonWriterImpl instance = new _OptimizedJsonWriterImpl();
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            instance.c(jsonWriter, -1);
        }, "Should throw NullPointerException when accessing null array");
    }

    /**
     * Tests that method c with zero index throws NullPointerException.
     */
    @Test
    public void testMethodC_withZeroIndex_throwsNullPointerException() {
        // Arrange
        _OptimizedJsonWriterImpl instance = new _OptimizedJsonWriterImpl();
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            instance.c(jsonWriter, 0);
        }, "Should throw NullPointerException when accessing null array at index 0");
    }

    /**
     * Tests that method c with large index value throws NullPointerException.
     */
    @Test
    public void testMethodC_withLargeIndex_throwsNullPointerException() {
        // Arrange
        _OptimizedJsonWriterImpl instance = new _OptimizedJsonWriterImpl();
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            instance.c(jsonWriter, Integer.MAX_VALUE);
        }, "Should throw NullPointerException when accessing null array with large index");
    }

    /**
     * Tests that method c can be called from multiple instances with same parameters.
     * All calls should throw NullPointerException due to null static array.
     */
    @Test
    public void testMethodC_multipleInstances_allThrowNullPointerException() {
        // Arrange
        _OptimizedJsonWriterImpl instance1 = new _OptimizedJsonWriterImpl();
        _OptimizedJsonWriterImpl instance2 = new _OptimizedJsonWriterImpl();
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            instance1.c(jsonWriter, 0);
        }, "First instance should throw NullPointerException");

        assertThrows(NullPointerException.class, () -> {
            instance2.c(jsonWriter, 0);
        }, "Second instance should throw NullPointerException");
    }

    /**
     * Tests that method c fails before writing anything when array is null.
     */
    @Test
    public void testMethodC_withNullArray_doesNotWriteToJsonWriter() {
        // Arrange
        _OptimizedJsonWriterImpl instance = new _OptimizedJsonWriterImpl();
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        // Act
        try {
            instance.c(jsonWriter, 0);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException | IOException e) {
            // Expected
        }

        // Assert
        assertEquals("", stringWriter.toString(),
            "No output should be written when NullPointerException occurs");
    }

    /**
     * Tests that method c with different instances and different indices all throw NPE.
     */
    @Test
    public void testMethodC_variousIndices_allThrowNullPointerException() {
        // Arrange
        _OptimizedJsonWriterImpl instance = new _OptimizedJsonWriterImpl();
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        int[] testIndices = {0, 1, 10, 100, -1, -10, Integer.MIN_VALUE, Integer.MAX_VALUE};

        // Act & Assert
        for (int index : testIndices) {
            assertThrows(NullPointerException.class, () -> {
                instance.c(jsonWriter, index);
            }, "Should throw NullPointerException for index: " + index);
        }
    }

    /**
     * Tests that method c from the _OptimizedJsonWriter interface can be called.
     */
    @Test
    public void testMethodC_viaInterface_throwsNullPointerException() {
        // Arrange
        _OptimizedJsonWriter instance = new _OptimizedJsonWriterImpl();
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            instance.c(jsonWriter, 0);
        }, "Should throw NullPointerException when called via interface");
    }

    /**
     * Tests that methods b and c can be called sequentially.
     * Both should throw NullPointerException due to null static array.
     */
    @Test
    public void testMethodBAndC_calledSequentially_bothThrowNullPointerException() {
        // Arrange
        _OptimizedJsonWriterImpl instance = new _OptimizedJsonWriterImpl();
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            instance.b(jsonWriter, 0);
        }, "Method b should throw NullPointerException");

        assertThrows(NullPointerException.class, () -> {
            instance.c(jsonWriter, 0);
        }, "Method c should throw NullPointerException");
    }

    /**
     * Tests that different instances share the same static array state.
     * Since the array is static and null, all instances will throw NPE.
     */
    @Test
    public void testStaticArraySharing_multipleInstances_shareNullState() {
        // Arrange
        _OptimizedJsonWriterImpl instance1 = new _OptimizedJsonWriterImpl();
        _OptimizedJsonWriterImpl instance2 = new _OptimizedJsonWriterImpl();
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            instance1.b(jsonWriter, 0);
        }, "First instance b should throw NullPointerException");

        assertThrows(NullPointerException.class, () -> {
            instance2.b(jsonWriter, 0);
        }, "Second instance b should throw NullPointerException");

        assertThrows(NullPointerException.class, () -> {
            instance1.c(jsonWriter, 0);
        }, "First instance c should throw NullPointerException");

        assertThrows(NullPointerException.class, () -> {
            instance2.c(jsonWriter, 0);
        }, "Second instance c should throw NullPointerException");
    }
}
