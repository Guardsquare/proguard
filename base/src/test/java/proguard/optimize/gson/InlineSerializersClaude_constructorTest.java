package proguard.optimize.gson;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for InlineSerializers constructors.
 * Tests:
 * - InlineSerializers$InlineBooleanSerializer.<init>.()V
 * - InlineSerializers$InlinePrimitiveBooleanSerializer.<init>.()V
 * - InlineSerializers$InlinePrimitiveIntegerSerializer.<init>.()V
 * - InlineSerializers$InlineStringSerializer.<init>.()V
 */
public class InlineSerializersClaude_constructorTest {

    /**
     * Tests that the default constructor creates a non-null InlineBooleanSerializer instance.
     */
    @Test
    public void testConstructorCreatesNonNullInstance() {
        // Act
        InlineSerializers.InlineBooleanSerializer serializer =
            new InlineSerializers.InlineBooleanSerializer();

        // Assert
        assertNotNull(serializer, "InlineBooleanSerializer instance should not be null");
    }

    /**
     * Tests that multiple InlineBooleanSerializer instances can be created independently.
     */
    @Test
    public void testMultipleInstancesCreation() {
        // Act
        InlineSerializers.InlineBooleanSerializer serializer1 =
            new InlineSerializers.InlineBooleanSerializer();
        InlineSerializers.InlineBooleanSerializer serializer2 =
            new InlineSerializers.InlineBooleanSerializer();

        // Assert
        assertNotNull(serializer1, "First InlineBooleanSerializer instance should not be null");
        assertNotNull(serializer2, "Second InlineBooleanSerializer instance should not be null");
        assertNotSame(serializer1, serializer2, "Each constructor call should create a distinct instance");
    }

    /**
     * Tests that a newly constructed InlineBooleanSerializer is a valid InlineSerializer.
     * This verifies that the object implements the expected interface.
     */
    @Test
    public void testConstructorCreatesInlineSerializerInstance() {
        // Act
        InlineSerializers.InlineBooleanSerializer serializer =
            new InlineSerializers.InlineBooleanSerializer();

        // Assert
        assertTrue(serializer instanceof InlineSerializer,
            "InlineBooleanSerializer should implement InlineSerializer interface");
    }

    /**
     * Tests that a newly constructed InlineBooleanSerializer has its methods accessible.
     * This verifies the object is properly initialized and ready for subsequent operations.
     */
    @Test
    public void testConstructorCreatesAccessibleMethods() {
        // Act
        InlineSerializers.InlineBooleanSerializer serializer =
            new InlineSerializers.InlineBooleanSerializer();

        // Assert - Verify we can access the interface methods without any exceptions
        assertDoesNotThrow(() -> {
            // Just verify methods exist and can be referenced
            serializer.getClass().getMethod("canSerialize",
                proguard.classfile.ClassPool.class,
                GsonRuntimeSettings.class);
            serializer.getClass().getMethod("serialize",
                proguard.classfile.ProgramClass.class,
                proguard.classfile.ProgramField.class,
                proguard.classfile.editor.CompactCodeAttributeComposer.class,
                GsonRuntimeSettings.class);
        }, "Should be able to access all interface methods after construction");
    }

    /**
     * Tests that the constructor creates objects of the expected class type.
     */
    @Test
    public void testConstructorCreatesCorrectType() {
        // Act
        InlineSerializers.InlineBooleanSerializer serializer =
            new InlineSerializers.InlineBooleanSerializer();

        // Assert
        assertEquals("proguard.optimize.gson.InlineSerializers$InlineBooleanSerializer",
            serializer.getClass().getName(),
            "Class name should match expected value");
    }

    /**
     * Tests that the no-arg constructor is consistent with itself across multiple invocations.
     * Verifies that the constructor behaves predictably.
     */
    @Test
    public void testConstructorConsistency() {
        // Act & Assert - Create instances in a loop to verify consistent behavior
        for (int i = 0; i < 10; i++) {
            InlineSerializers.InlineBooleanSerializer serializer =
                new InlineSerializers.InlineBooleanSerializer();
            assertNotNull(serializer, "Instance " + i + " should not be null");
            assertTrue(serializer instanceof InlineSerializer,
                "Instance " + i + " should implement InlineSerializer");
        }
    }

    /**
     * Tests that the constructor works correctly in a multithreaded context by
     * creating multiple instances concurrently.
     */
    @Test
    public void testConstructorConcurrentCreation() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            Thread[] threads = new Thread[5];
            for (int i = 0; i < threads.length; i++) {
                threads[i] = new Thread(() -> {
                    for (int j = 0; j < 5; j++) {
                        InlineSerializers.InlineBooleanSerializer serializer =
                            new InlineSerializers.InlineBooleanSerializer();
                        assertNotNull(serializer, "Serializer should not be null");
                    }
                });
                threads[i].start();
            }
            for (Thread thread : threads) {
                thread.join();
            }
        }, "Constructor should work correctly in concurrent context");
    }

    /**
     * Tests that the newly created InlineBooleanSerializer is a distinct object
     * and can be used independently.
     */
    @Test
    public void testConstructorCreatesDistinctInstances() {
        // Act
        InlineSerializers.InlineBooleanSerializer serializer1 =
            new InlineSerializers.InlineBooleanSerializer();
        InlineSerializers.InlineBooleanSerializer serializer2 =
            new InlineSerializers.InlineBooleanSerializer();
        InlineSerializers.InlineBooleanSerializer serializer3 =
            new InlineSerializers.InlineBooleanSerializer();

        // Assert
        assertNotSame(serializer1, serializer2, "Instances 1 and 2 should be distinct");
        assertNotSame(serializer1, serializer3, "Instances 1 and 3 should be distinct");
        assertNotSame(serializer2, serializer3, "Instances 2 and 3 should be distinct");
    }

    // ==================== Tests for InlinePrimitiveBooleanSerializer ====================

    /**
     * Tests that the default constructor creates a non-null InlinePrimitiveBooleanSerializer instance.
     */
    @Test
    public void testPrimitiveBooleanConstructorCreatesNonNullInstance() {
        // Act
        InlineSerializers.InlinePrimitiveBooleanSerializer serializer =
            new InlineSerializers.InlinePrimitiveBooleanSerializer();

        // Assert
        assertNotNull(serializer, "InlinePrimitiveBooleanSerializer instance should not be null");
    }

    /**
     * Tests that multiple InlinePrimitiveBooleanSerializer instances can be created independently.
     */
    @Test
    public void testPrimitiveBooleanMultipleInstancesCreation() {
        // Act
        InlineSerializers.InlinePrimitiveBooleanSerializer serializer1 =
            new InlineSerializers.InlinePrimitiveBooleanSerializer();
        InlineSerializers.InlinePrimitiveBooleanSerializer serializer2 =
            new InlineSerializers.InlinePrimitiveBooleanSerializer();

        // Assert
        assertNotNull(serializer1, "First InlinePrimitiveBooleanSerializer instance should not be null");
        assertNotNull(serializer2, "Second InlinePrimitiveBooleanSerializer instance should not be null");
        assertNotSame(serializer1, serializer2, "Each constructor call should create a distinct instance");
    }

    /**
     * Tests that a newly constructed InlinePrimitiveBooleanSerializer is a valid InlineSerializer.
     * This verifies that the object implements the expected interface.
     */
    @Test
    public void testPrimitiveBooleanConstructorCreatesInlineSerializerInstance() {
        // Act
        InlineSerializers.InlinePrimitiveBooleanSerializer serializer =
            new InlineSerializers.InlinePrimitiveBooleanSerializer();

        // Assert
        assertTrue(serializer instanceof InlineSerializer,
            "InlinePrimitiveBooleanSerializer should implement InlineSerializer interface");
    }

    /**
     * Tests that a newly constructed InlinePrimitiveBooleanSerializer has its methods accessible.
     * This verifies the object is properly initialized and ready for subsequent operations.
     */
    @Test
    public void testPrimitiveBooleanConstructorCreatesAccessibleMethods() {
        // Act
        InlineSerializers.InlinePrimitiveBooleanSerializer serializer =
            new InlineSerializers.InlinePrimitiveBooleanSerializer();

        // Assert - Verify we can access the interface methods without any exceptions
        assertDoesNotThrow(() -> {
            // Just verify methods exist and can be referenced
            serializer.getClass().getMethod("canSerialize",
                proguard.classfile.ClassPool.class,
                GsonRuntimeSettings.class);
            serializer.getClass().getMethod("serialize",
                proguard.classfile.ProgramClass.class,
                proguard.classfile.ProgramField.class,
                proguard.classfile.editor.CompactCodeAttributeComposer.class,
                GsonRuntimeSettings.class);
        }, "Should be able to access all interface methods after construction");
    }

    /**
     * Tests that the constructor creates objects of the expected class type.
     */
    @Test
    public void testPrimitiveBooleanConstructorCreatesCorrectType() {
        // Act
        InlineSerializers.InlinePrimitiveBooleanSerializer serializer =
            new InlineSerializers.InlinePrimitiveBooleanSerializer();

        // Assert
        assertEquals("proguard.optimize.gson.InlineSerializers$InlinePrimitiveBooleanSerializer",
            serializer.getClass().getName(),
            "Class name should match expected value");
    }

    /**
     * Tests that the no-arg constructor is consistent with itself across multiple invocations.
     * Verifies that the constructor behaves predictably.
     */
    @Test
    public void testPrimitiveBooleanConstructorConsistency() {
        // Act & Assert - Create instances in a loop to verify consistent behavior
        for (int i = 0; i < 10; i++) {
            InlineSerializers.InlinePrimitiveBooleanSerializer serializer =
                new InlineSerializers.InlinePrimitiveBooleanSerializer();
            assertNotNull(serializer, "Instance " + i + " should not be null");
            assertTrue(serializer instanceof InlineSerializer,
                "Instance " + i + " should implement InlineSerializer");
        }
    }

    /**
     * Tests that the constructor works correctly in a multithreaded context by
     * creating multiple instances concurrently.
     */
    @Test
    public void testPrimitiveBooleanConstructorConcurrentCreation() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            Thread[] threads = new Thread[5];
            for (int i = 0; i < threads.length; i++) {
                threads[i] = new Thread(() -> {
                    for (int j = 0; j < 5; j++) {
                        InlineSerializers.InlinePrimitiveBooleanSerializer serializer =
                            new InlineSerializers.InlinePrimitiveBooleanSerializer();
                        assertNotNull(serializer, "Serializer should not be null");
                    }
                });
                threads[i].start();
            }
            for (Thread thread : threads) {
                thread.join();
            }
        }, "Constructor should work correctly in concurrent context");
    }

    /**
     * Tests that the newly created InlinePrimitiveBooleanSerializer is a distinct object
     * and can be used independently.
     */
    @Test
    public void testPrimitiveBooleanConstructorCreatesDistinctInstances() {
        // Act
        InlineSerializers.InlinePrimitiveBooleanSerializer serializer1 =
            new InlineSerializers.InlinePrimitiveBooleanSerializer();
        InlineSerializers.InlinePrimitiveBooleanSerializer serializer2 =
            new InlineSerializers.InlinePrimitiveBooleanSerializer();
        InlineSerializers.InlinePrimitiveBooleanSerializer serializer3 =
            new InlineSerializers.InlinePrimitiveBooleanSerializer();

        // Assert
        assertNotSame(serializer1, serializer2, "Instances 1 and 2 should be distinct");
        assertNotSame(serializer1, serializer3, "Instances 1 and 3 should be distinct");
        assertNotSame(serializer2, serializer3, "Instances 2 and 3 should be distinct");
    }

    // ==================== Tests for InlinePrimitiveIntegerSerializer ====================

    /**
     * Tests that the default constructor creates a non-null InlinePrimitiveIntegerSerializer instance.
     */
    @Test
    public void testPrimitiveIntegerConstructorCreatesNonNullInstance() {
        // Act
        InlineSerializers.InlinePrimitiveIntegerSerializer serializer =
            new InlineSerializers.InlinePrimitiveIntegerSerializer();

        // Assert
        assertNotNull(serializer, "InlinePrimitiveIntegerSerializer instance should not be null");
    }

    /**
     * Tests that multiple InlinePrimitiveIntegerSerializer instances can be created independently.
     */
    @Test
    public void testPrimitiveIntegerMultipleInstancesCreation() {
        // Act
        InlineSerializers.InlinePrimitiveIntegerSerializer serializer1 =
            new InlineSerializers.InlinePrimitiveIntegerSerializer();
        InlineSerializers.InlinePrimitiveIntegerSerializer serializer2 =
            new InlineSerializers.InlinePrimitiveIntegerSerializer();

        // Assert
        assertNotNull(serializer1, "First InlinePrimitiveIntegerSerializer instance should not be null");
        assertNotNull(serializer2, "Second InlinePrimitiveIntegerSerializer instance should not be null");
        assertNotSame(serializer1, serializer2, "Each constructor call should create a distinct instance");
    }

    /**
     * Tests that a newly constructed InlinePrimitiveIntegerSerializer is a valid InlineSerializer.
     * This verifies that the object implements the expected interface.
     */
    @Test
    public void testPrimitiveIntegerConstructorCreatesInlineSerializerInstance() {
        // Act
        InlineSerializers.InlinePrimitiveIntegerSerializer serializer =
            new InlineSerializers.InlinePrimitiveIntegerSerializer();

        // Assert
        assertTrue(serializer instanceof InlineSerializer,
            "InlinePrimitiveIntegerSerializer should implement InlineSerializer interface");
    }

    /**
     * Tests that a newly constructed InlinePrimitiveIntegerSerializer has its methods accessible.
     * This verifies the object is properly initialized and ready for subsequent operations.
     */
    @Test
    public void testPrimitiveIntegerConstructorCreatesAccessibleMethods() {
        // Act
        InlineSerializers.InlinePrimitiveIntegerSerializer serializer =
            new InlineSerializers.InlinePrimitiveIntegerSerializer();

        // Assert - Verify we can access the interface methods without any exceptions
        assertDoesNotThrow(() -> {
            // Just verify methods exist and can be referenced
            serializer.getClass().getMethod("canSerialize",
                proguard.classfile.ClassPool.class,
                GsonRuntimeSettings.class);
            serializer.getClass().getMethod("serialize",
                proguard.classfile.ProgramClass.class,
                proguard.classfile.ProgramField.class,
                proguard.classfile.editor.CompactCodeAttributeComposer.class,
                GsonRuntimeSettings.class);
        }, "Should be able to access all interface methods after construction");
    }

    /**
     * Tests that the constructor creates objects of the expected class type.
     */
    @Test
    public void testPrimitiveIntegerConstructorCreatesCorrectType() {
        // Act
        InlineSerializers.InlinePrimitiveIntegerSerializer serializer =
            new InlineSerializers.InlinePrimitiveIntegerSerializer();

        // Assert
        assertEquals("proguard.optimize.gson.InlineSerializers$InlinePrimitiveIntegerSerializer",
            serializer.getClass().getName(),
            "Class name should match expected value");
    }

    /**
     * Tests that the no-arg constructor is consistent with itself across multiple invocations.
     * Verifies that the constructor behaves predictably.
     */
    @Test
    public void testPrimitiveIntegerConstructorConsistency() {
        // Act & Assert - Create instances in a loop to verify consistent behavior
        for (int i = 0; i < 10; i++) {
            InlineSerializers.InlinePrimitiveIntegerSerializer serializer =
                new InlineSerializers.InlinePrimitiveIntegerSerializer();
            assertNotNull(serializer, "Instance " + i + " should not be null");
            assertTrue(serializer instanceof InlineSerializer,
                "Instance " + i + " should implement InlineSerializer");
        }
    }

    /**
     * Tests that the constructor works correctly in a multithreaded context by
     * creating multiple instances concurrently.
     */
    @Test
    public void testPrimitiveIntegerConstructorConcurrentCreation() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            Thread[] threads = new Thread[5];
            for (int i = 0; i < threads.length; i++) {
                threads[i] = new Thread(() -> {
                    for (int j = 0; j < 5; j++) {
                        InlineSerializers.InlinePrimitiveIntegerSerializer serializer =
                            new InlineSerializers.InlinePrimitiveIntegerSerializer();
                        assertNotNull(serializer, "Serializer should not be null");
                    }
                });
                threads[i].start();
            }
            for (Thread thread : threads) {
                thread.join();
            }
        }, "Constructor should work correctly in concurrent context");
    }

    /**
     * Tests that the newly created InlinePrimitiveIntegerSerializer is a distinct object
     * and can be used independently.
     */
    @Test
    public void testPrimitiveIntegerConstructorCreatesDistinctInstances() {
        // Act
        InlineSerializers.InlinePrimitiveIntegerSerializer serializer1 =
            new InlineSerializers.InlinePrimitiveIntegerSerializer();
        InlineSerializers.InlinePrimitiveIntegerSerializer serializer2 =
            new InlineSerializers.InlinePrimitiveIntegerSerializer();
        InlineSerializers.InlinePrimitiveIntegerSerializer serializer3 =
            new InlineSerializers.InlinePrimitiveIntegerSerializer();

        // Assert
        assertNotSame(serializer1, serializer2, "Instances 1 and 2 should be distinct");
        assertNotSame(serializer1, serializer3, "Instances 1 and 3 should be distinct");
        assertNotSame(serializer2, serializer3, "Instances 2 and 3 should be distinct");
    }

    // ==================== Tests for InlineStringSerializer ====================

    /**
     * Tests that the default constructor creates a non-null InlineStringSerializer instance.
     */
    @Test
    public void testStringConstructorCreatesNonNullInstance() {
        // Act
        InlineSerializers.InlineStringSerializer serializer =
            new InlineSerializers.InlineStringSerializer();

        // Assert
        assertNotNull(serializer, "InlineStringSerializer instance should not be null");
    }

    /**
     * Tests that multiple InlineStringSerializer instances can be created independently.
     */
    @Test
    public void testStringMultipleInstancesCreation() {
        // Act
        InlineSerializers.InlineStringSerializer serializer1 =
            new InlineSerializers.InlineStringSerializer();
        InlineSerializers.InlineStringSerializer serializer2 =
            new InlineSerializers.InlineStringSerializer();

        // Assert
        assertNotNull(serializer1, "First InlineStringSerializer instance should not be null");
        assertNotNull(serializer2, "Second InlineStringSerializer instance should not be null");
        assertNotSame(serializer1, serializer2, "Each constructor call should create a distinct instance");
    }

    /**
     * Tests that a newly constructed InlineStringSerializer is a valid InlineSerializer.
     * This verifies that the object implements the expected interface.
     */
    @Test
    public void testStringConstructorCreatesInlineSerializerInstance() {
        // Act
        InlineSerializers.InlineStringSerializer serializer =
            new InlineSerializers.InlineStringSerializer();

        // Assert
        assertTrue(serializer instanceof InlineSerializer,
            "InlineStringSerializer should implement InlineSerializer interface");
    }

    /**
     * Tests that a newly constructed InlineStringSerializer has its methods accessible.
     * This verifies the object is properly initialized and ready for subsequent operations.
     */
    @Test
    public void testStringConstructorCreatesAccessibleMethods() {
        // Act
        InlineSerializers.InlineStringSerializer serializer =
            new InlineSerializers.InlineStringSerializer();

        // Assert - Verify we can access the interface methods without any exceptions
        assertDoesNotThrow(() -> {
            // Just verify methods exist and can be referenced
            serializer.getClass().getMethod("canSerialize",
                proguard.classfile.ClassPool.class,
                GsonRuntimeSettings.class);
            serializer.getClass().getMethod("serialize",
                proguard.classfile.ProgramClass.class,
                proguard.classfile.ProgramField.class,
                proguard.classfile.editor.CompactCodeAttributeComposer.class,
                GsonRuntimeSettings.class);
        }, "Should be able to access all interface methods after construction");
    }

    /**
     * Tests that the constructor creates objects of the expected class type.
     */
    @Test
    public void testStringConstructorCreatesCorrectType() {
        // Act
        InlineSerializers.InlineStringSerializer serializer =
            new InlineSerializers.InlineStringSerializer();

        // Assert
        assertEquals("proguard.optimize.gson.InlineSerializers$InlineStringSerializer",
            serializer.getClass().getName(),
            "Class name should match expected value");
    }

    /**
     * Tests that the no-arg constructor is consistent with itself across multiple invocations.
     * Verifies that the constructor behaves predictably.
     */
    @Test
    public void testStringConstructorConsistency() {
        // Act & Assert - Create instances in a loop to verify consistent behavior
        for (int i = 0; i < 10; i++) {
            InlineSerializers.InlineStringSerializer serializer =
                new InlineSerializers.InlineStringSerializer();
            assertNotNull(serializer, "Instance " + i + " should not be null");
            assertTrue(serializer instanceof InlineSerializer,
                "Instance " + i + " should implement InlineSerializer");
        }
    }

    /**
     * Tests that the constructor works correctly in a multithreaded context by
     * creating multiple instances concurrently.
     */
    @Test
    public void testStringConstructorConcurrentCreation() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            Thread[] threads = new Thread[5];
            for (int i = 0; i < threads.length; i++) {
                threads[i] = new Thread(() -> {
                    for (int j = 0; j < 5; j++) {
                        InlineSerializers.InlineStringSerializer serializer =
                            new InlineSerializers.InlineStringSerializer();
                        assertNotNull(serializer, "Serializer should not be null");
                    }
                });
                threads[i].start();
            }
            for (Thread thread : threads) {
                thread.join();
            }
        }, "Constructor should work correctly in concurrent context");
    }

    /**
     * Tests that the newly created InlineStringSerializer is a distinct object
     * and can be used independently.
     */
    @Test
    public void testStringConstructorCreatesDistinctInstances() {
        // Act
        InlineSerializers.InlineStringSerializer serializer1 =
            new InlineSerializers.InlineStringSerializer();
        InlineSerializers.InlineStringSerializer serializer2 =
            new InlineSerializers.InlineStringSerializer();
        InlineSerializers.InlineStringSerializer serializer3 =
            new InlineSerializers.InlineStringSerializer();

        // Assert
        assertNotSame(serializer1, serializer2, "Instances 1 and 2 should be distinct");
        assertNotSame(serializer1, serializer3, "Instances 1 and 3 should be distinct");
        assertNotSame(serializer2, serializer3, "Instances 2 and 3 should be distinct");
    }
}
