package proguard.optimize.gson;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for InlineDeserializers constructors.
 * Tests:
 * - InlineDeserializers$InlinePrimitiveIntegerDeserializer.<init>.()V
 * - InlineDeserializers$InlinePrimitiveIntegerDeserializer.<init>.(Ljava/lang/Class;)V
 * - InlineDeserializers$InlineStringDeserializer.<init>.()V
 */
public class InlineDeserializersClaude_constructorTest {

    /**
     * Tests that the default constructor creates a non-null InlinePrimitiveIntegerDeserializer instance.
     */
    @Test
    public void testConstructorCreatesNonNullInstance() {
        // Act
        InlineDeserializers.InlinePrimitiveIntegerDeserializer deserializer =
            new InlineDeserializers.InlinePrimitiveIntegerDeserializer();

        // Assert
        assertNotNull(deserializer, "InlinePrimitiveIntegerDeserializer instance should not be null");
    }

    /**
     * Tests that multiple InlinePrimitiveIntegerDeserializer instances can be created independently.
     */
    @Test
    public void testMultipleInstancesCreation() {
        // Act
        InlineDeserializers.InlinePrimitiveIntegerDeserializer deserializer1 =
            new InlineDeserializers.InlinePrimitiveIntegerDeserializer();
        InlineDeserializers.InlinePrimitiveIntegerDeserializer deserializer2 =
            new InlineDeserializers.InlinePrimitiveIntegerDeserializer();

        // Assert
        assertNotNull(deserializer1, "First InlinePrimitiveIntegerDeserializer instance should not be null");
        assertNotNull(deserializer2, "Second InlinePrimitiveIntegerDeserializer instance should not be null");
        assertNotSame(deserializer1, deserializer2, "Each constructor call should create a distinct instance");
    }

    /**
     * Tests that a newly constructed InlinePrimitiveIntegerDeserializer is a valid InlineDeserializer.
     * This verifies that the object implements the expected interface.
     */
    @Test
    public void testConstructorCreatesInlineDeserializerInstance() {
        // Act
        InlineDeserializers.InlinePrimitiveIntegerDeserializer deserializer =
            new InlineDeserializers.InlinePrimitiveIntegerDeserializer();

        // Assert
        assertTrue(deserializer instanceof InlineDeserializer,
            "InlinePrimitiveIntegerDeserializer should implement InlineDeserializer interface");
    }

    /**
     * Tests that a newly constructed InlinePrimitiveIntegerDeserializer has its methods accessible.
     * This verifies the object is properly initialized and ready for subsequent operations.
     */
    @Test
    public void testConstructorCreatesAccessibleMethods() {
        // Act
        InlineDeserializers.InlinePrimitiveIntegerDeserializer deserializer =
            new InlineDeserializers.InlinePrimitiveIntegerDeserializer();

        // Assert - Verify we can access the interface methods without any exceptions
        assertDoesNotThrow(() -> {
            // Just verify methods exist and can be referenced
            deserializer.getClass().getMethod("canDeserialize", GsonRuntimeSettings.class);
            deserializer.getClass().getMethod("deserialize",
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
        InlineDeserializers.InlinePrimitiveIntegerDeserializer deserializer =
            new InlineDeserializers.InlinePrimitiveIntegerDeserializer();

        // Assert
        assertEquals("proguard.optimize.gson.InlineDeserializers$InlinePrimitiveIntegerDeserializer",
            deserializer.getClass().getName(),
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
            InlineDeserializers.InlinePrimitiveIntegerDeserializer deserializer =
                new InlineDeserializers.InlinePrimitiveIntegerDeserializer();
            assertNotNull(deserializer, "Instance " + i + " should not be null");
            assertTrue(deserializer instanceof InlineDeserializer,
                "Instance " + i + " should implement InlineDeserializer");
        }
    }

    // ============================================================================
    // Tests for parameterized constructor: InlinePrimitiveIntegerDeserializer(Class)
    // ============================================================================

    /**
     * Tests that the parameterized constructor creates a non-null instance with a null Class parameter.
     */
    @Test
    public void testParameterizedConstructorCreatesNonNullInstanceWithNullClass() {
        // Act
        InlineDeserializers.InlinePrimitiveIntegerDeserializer deserializer =
            new InlineDeserializers.InlinePrimitiveIntegerDeserializer(null);

        // Assert
        assertNotNull(deserializer, "InlinePrimitiveIntegerDeserializer instance should not be null");
    }

    /**
     * Tests that the parameterized constructor creates a non-null instance with Integer.class.
     */
    @Test
    public void testParameterizedConstructorCreatesNonNullInstanceWithIntegerClass() {
        // Act
        InlineDeserializers.InlinePrimitiveIntegerDeserializer deserializer =
            new InlineDeserializers.InlinePrimitiveIntegerDeserializer(Integer.class);

        // Assert
        assertNotNull(deserializer, "InlinePrimitiveIntegerDeserializer instance should not be null");
    }

    /**
     * Tests that the parameterized constructor creates a non-null instance with int.class (primitive).
     */
    @Test
    public void testParameterizedConstructorCreatesNonNullInstanceWithPrimitiveIntClass() {
        // Act
        InlineDeserializers.InlinePrimitiveIntegerDeserializer deserializer =
            new InlineDeserializers.InlinePrimitiveIntegerDeserializer(int.class);

        // Assert
        assertNotNull(deserializer, "InlinePrimitiveIntegerDeserializer instance should not be null");
    }

    /**
     * Tests that the parameterized constructor creates a non-null instance with Short.class.
     */
    @Test
    public void testParameterizedConstructorCreatesNonNullInstanceWithShortClass() {
        // Act
        InlineDeserializers.InlinePrimitiveIntegerDeserializer deserializer =
            new InlineDeserializers.InlinePrimitiveIntegerDeserializer(Short.class);

        // Assert
        assertNotNull(deserializer, "InlinePrimitiveIntegerDeserializer instance should not be null");
    }

    /**
     * Tests that the parameterized constructor creates a non-null instance with short.class (primitive).
     */
    @Test
    public void testParameterizedConstructorCreatesNonNullInstanceWithPrimitiveShortClass() {
        // Act
        InlineDeserializers.InlinePrimitiveIntegerDeserializer deserializer =
            new InlineDeserializers.InlinePrimitiveIntegerDeserializer(short.class);

        // Assert
        assertNotNull(deserializer, "InlinePrimitiveIntegerDeserializer instance should not be null");
    }

    /**
     * Tests that the parameterized constructor creates a non-null instance with Byte.class.
     */
    @Test
    public void testParameterizedConstructorCreatesNonNullInstanceWithByteClass() {
        // Act
        InlineDeserializers.InlinePrimitiveIntegerDeserializer deserializer =
            new InlineDeserializers.InlinePrimitiveIntegerDeserializer(Byte.class);

        // Assert
        assertNotNull(deserializer, "InlinePrimitiveIntegerDeserializer instance should not be null");
    }

    /**
     * Tests that the parameterized constructor creates a non-null instance with byte.class (primitive).
     */
    @Test
    public void testParameterizedConstructorCreatesNonNullInstanceWithPrimitiveByteClass() {
        // Act
        InlineDeserializers.InlinePrimitiveIntegerDeserializer deserializer =
            new InlineDeserializers.InlinePrimitiveIntegerDeserializer(byte.class);

        // Assert
        assertNotNull(deserializer, "InlinePrimitiveIntegerDeserializer instance should not be null");
    }

    /**
     * Tests that the parameterized constructor with null creates a valid InlineDeserializer.
     */
    @Test
    public void testParameterizedConstructorWithNullCreatesInlineDeserializerInstance() {
        // Act
        InlineDeserializers.InlinePrimitiveIntegerDeserializer deserializer =
            new InlineDeserializers.InlinePrimitiveIntegerDeserializer(null);

        // Assert
        assertTrue(deserializer instanceof InlineDeserializer,
            "InlinePrimitiveIntegerDeserializer should implement InlineDeserializer interface");
    }

    /**
     * Tests that the parameterized constructor with Integer.class creates a valid InlineDeserializer.
     */
    @Test
    public void testParameterizedConstructorWithIntegerClassCreatesInlineDeserializerInstance() {
        // Act
        InlineDeserializers.InlinePrimitiveIntegerDeserializer deserializer =
            new InlineDeserializers.InlinePrimitiveIntegerDeserializer(Integer.class);

        // Assert
        assertTrue(deserializer instanceof InlineDeserializer,
            "InlinePrimitiveIntegerDeserializer should implement InlineDeserializer interface");
    }

    /**
     * Tests that multiple instances created with the parameterized constructor are independent.
     */
    @Test
    public void testParameterizedConstructorMultipleInstancesCreation() {
        // Act
        InlineDeserializers.InlinePrimitiveIntegerDeserializer deserializer1 =
            new InlineDeserializers.InlinePrimitiveIntegerDeserializer(Integer.class);
        InlineDeserializers.InlinePrimitiveIntegerDeserializer deserializer2 =
            new InlineDeserializers.InlinePrimitiveIntegerDeserializer(Short.class);

        // Assert
        assertNotNull(deserializer1, "First InlinePrimitiveIntegerDeserializer instance should not be null");
        assertNotNull(deserializer2, "Second InlinePrimitiveIntegerDeserializer instance should not be null");
        assertNotSame(deserializer1, deserializer2, "Each constructor call should create a distinct instance");
    }

    /**
     * Tests that the parameterized constructor with various Class types creates objects of the correct type.
     */
    @Test
    public void testParameterizedConstructorCreatesCorrectType() {
        // Act
        InlineDeserializers.InlinePrimitiveIntegerDeserializer deserializer1 =
            new InlineDeserializers.InlinePrimitiveIntegerDeserializer(null);
        InlineDeserializers.InlinePrimitiveIntegerDeserializer deserializer2 =
            new InlineDeserializers.InlinePrimitiveIntegerDeserializer(Integer.class);

        // Assert
        assertEquals("proguard.optimize.gson.InlineDeserializers$InlinePrimitiveIntegerDeserializer",
            deserializer1.getClass().getName(),
            "Class name should match expected value");
        assertEquals("proguard.optimize.gson.InlineDeserializers$InlinePrimitiveIntegerDeserializer",
            deserializer2.getClass().getName(),
            "Class name should match expected value");
    }

    /**
     * Tests that the parameterized constructor has accessible methods.
     */
    @Test
    public void testParameterizedConstructorCreatesAccessibleMethods() {
        // Act
        InlineDeserializers.InlinePrimitiveIntegerDeserializer deserializer =
            new InlineDeserializers.InlinePrimitiveIntegerDeserializer(Integer.class);

        // Assert - Verify we can access the interface methods without any exceptions
        assertDoesNotThrow(() -> {
            deserializer.getClass().getMethod("canDeserialize", GsonRuntimeSettings.class);
            deserializer.getClass().getMethod("deserialize",
                proguard.classfile.ProgramClass.class,
                proguard.classfile.ProgramField.class,
                proguard.classfile.editor.CompactCodeAttributeComposer.class,
                GsonRuntimeSettings.class);
        }, "Should be able to access all interface methods after construction");
    }

    /**
     * Tests that the parameterized constructor is consistent across multiple invocations with the same parameter.
     */
    @Test
    public void testParameterizedConstructorConsistency() {
        // Act & Assert - Create instances in a loop to verify consistent behavior
        for (int i = 0; i < 5; i++) {
            InlineDeserializers.InlinePrimitiveIntegerDeserializer deserializer =
                new InlineDeserializers.InlinePrimitiveIntegerDeserializer(Integer.class);
            assertNotNull(deserializer, "Instance " + i + " should not be null");
            assertTrue(deserializer instanceof InlineDeserializer,
                "Instance " + i + " should implement InlineDeserializer");
        }
    }

    /**
     * Tests that the parameterized constructor works with different primitive wrapper and primitive classes.
     */
    @Test
    public void testParameterizedConstructorWithVariousIntegerTypes() {
        // Act
        InlineDeserializers.InlinePrimitiveIntegerDeserializer intDeserializer =
            new InlineDeserializers.InlinePrimitiveIntegerDeserializer(int.class);
        InlineDeserializers.InlinePrimitiveIntegerDeserializer shortDeserializer =
            new InlineDeserializers.InlinePrimitiveIntegerDeserializer(short.class);
        InlineDeserializers.InlinePrimitiveIntegerDeserializer byteDeserializer =
            new InlineDeserializers.InlinePrimitiveIntegerDeserializer(byte.class);
        InlineDeserializers.InlinePrimitiveIntegerDeserializer integerDeserializer =
            new InlineDeserializers.InlinePrimitiveIntegerDeserializer(Integer.class);
        InlineDeserializers.InlinePrimitiveIntegerDeserializer shortWrapperDeserializer =
            new InlineDeserializers.InlinePrimitiveIntegerDeserializer(Short.class);
        InlineDeserializers.InlinePrimitiveIntegerDeserializer byteWrapperDeserializer =
            new InlineDeserializers.InlinePrimitiveIntegerDeserializer(Byte.class);

        // Assert - All instances should be properly created
        assertNotNull(intDeserializer, "int.class deserializer should not be null");
        assertNotNull(shortDeserializer, "short.class deserializer should not be null");
        assertNotNull(byteDeserializer, "byte.class deserializer should not be null");
        assertNotNull(integerDeserializer, "Integer.class deserializer should not be null");
        assertNotNull(shortWrapperDeserializer, "Short.class deserializer should not be null");
        assertNotNull(byteWrapperDeserializer, "Byte.class deserializer should not be null");
    }

    /**
     * Tests that the parameterized constructor works with unrelated Class types.
     * The constructor accepts any Class parameter, not just integer-related types.
     */
    @Test
    public void testParameterizedConstructorWithUnrelatedClassTypes() {
        // Act
        InlineDeserializers.InlinePrimitiveIntegerDeserializer stringDeserializer =
            new InlineDeserializers.InlinePrimitiveIntegerDeserializer(String.class);
        InlineDeserializers.InlinePrimitiveIntegerDeserializer objectDeserializer =
            new InlineDeserializers.InlinePrimitiveIntegerDeserializer(Object.class);

        // Assert
        assertNotNull(stringDeserializer, "String.class deserializer should not be null");
        assertNotNull(objectDeserializer, "Object.class deserializer should not be null");
        assertTrue(stringDeserializer instanceof InlineDeserializer,
            "Deserializer should implement InlineDeserializer interface");
        assertTrue(objectDeserializer instanceof InlineDeserializer,
            "Deserializer should implement InlineDeserializer interface");
    }

    // ============================================================================
    // Tests for InlineStringDeserializer constructor: InlineDeserializers$InlineStringDeserializer.<init>.()V
    // ============================================================================

    /**
     * Tests that the default constructor creates a non-null InlineStringDeserializer instance.
     */
    @Test
    public void testStringDeserializerConstructorCreatesNonNullInstance() {
        // Act
        InlineDeserializers.InlineStringDeserializer deserializer =
            new InlineDeserializers.InlineStringDeserializer();

        // Assert
        assertNotNull(deserializer, "InlineStringDeserializer instance should not be null");
    }

    /**
     * Tests that multiple InlineStringDeserializer instances can be created independently.
     */
    @Test
    public void testStringDeserializerMultipleInstancesCreation() {
        // Act
        InlineDeserializers.InlineStringDeserializer deserializer1 =
            new InlineDeserializers.InlineStringDeserializer();
        InlineDeserializers.InlineStringDeserializer deserializer2 =
            new InlineDeserializers.InlineStringDeserializer();

        // Assert
        assertNotNull(deserializer1, "First InlineStringDeserializer instance should not be null");
        assertNotNull(deserializer2, "Second InlineStringDeserializer instance should not be null");
        assertNotSame(deserializer1, deserializer2, "Each constructor call should create a distinct instance");
    }

    /**
     * Tests that a newly constructed InlineStringDeserializer is a valid InlineDeserializer.
     * This verifies that the object implements the expected interface.
     */
    @Test
    public void testStringDeserializerConstructorCreatesInlineDeserializerInstance() {
        // Act
        InlineDeserializers.InlineStringDeserializer deserializer =
            new InlineDeserializers.InlineStringDeserializer();

        // Assert
        assertTrue(deserializer instanceof InlineDeserializer,
            "InlineStringDeserializer should implement InlineDeserializer interface");
    }

    /**
     * Tests that a newly constructed InlineStringDeserializer has its methods accessible.
     * This verifies the object is properly initialized and ready for subsequent operations.
     */
    @Test
    public void testStringDeserializerConstructorCreatesAccessibleMethods() {
        // Act
        InlineDeserializers.InlineStringDeserializer deserializer =
            new InlineDeserializers.InlineStringDeserializer();

        // Assert - Verify we can access the interface methods without any exceptions
        assertDoesNotThrow(() -> {
            // Just verify methods exist and can be referenced
            deserializer.getClass().getMethod("canDeserialize", GsonRuntimeSettings.class);
            deserializer.getClass().getMethod("deserialize",
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
    public void testStringDeserializerConstructorCreatesCorrectType() {
        // Act
        InlineDeserializers.InlineStringDeserializer deserializer =
            new InlineDeserializers.InlineStringDeserializer();

        // Assert
        assertEquals("proguard.optimize.gson.InlineDeserializers$InlineStringDeserializer",
            deserializer.getClass().getName(),
            "Class name should match expected value");
    }

    /**
     * Tests that the no-arg constructor is consistent with itself across multiple invocations.
     * Verifies that the constructor behaves predictably.
     */
    @Test
    public void testStringDeserializerConstructorConsistency() {
        // Act & Assert - Create instances in a loop to verify consistent behavior
        for (int i = 0; i < 10; i++) {
            InlineDeserializers.InlineStringDeserializer deserializer =
                new InlineDeserializers.InlineStringDeserializer();
            assertNotNull(deserializer, "Instance " + i + " should not be null");
            assertTrue(deserializer instanceof InlineDeserializer,
                "Instance " + i + " should implement InlineDeserializer");
        }
    }

    /**
     * Tests that the constructor works correctly in a multithreaded context by
     * creating multiple instances concurrently.
     */
    @Test
    public void testStringDeserializerConstructorConcurrentCreation() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            Thread[] threads = new Thread[5];
            for (int i = 0; i < threads.length; i++) {
                threads[i] = new Thread(() -> {
                    for (int j = 0; j < 5; j++) {
                        InlineDeserializers.InlineStringDeserializer deserializer =
                            new InlineDeserializers.InlineStringDeserializer();
                        assertNotNull(deserializer, "Deserializer should not be null");
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
     * Tests that the newly created InlineStringDeserializer is a distinct object
     * and can be used independently.
     */
    @Test
    public void testStringDeserializerConstructorCreatesDistinctInstances() {
        // Act
        InlineDeserializers.InlineStringDeserializer deserializer1 =
            new InlineDeserializers.InlineStringDeserializer();
        InlineDeserializers.InlineStringDeserializer deserializer2 =
            new InlineDeserializers.InlineStringDeserializer();
        InlineDeserializers.InlineStringDeserializer deserializer3 =
            new InlineDeserializers.InlineStringDeserializer();

        // Assert
        assertNotSame(deserializer1, deserializer2, "Instances 1 and 2 should be distinct");
        assertNotSame(deserializer1, deserializer3, "Instances 1 and 3 should be distinct");
        assertNotSame(deserializer2, deserializer3, "Instances 2 and 3 should be distinct");
    }
}
