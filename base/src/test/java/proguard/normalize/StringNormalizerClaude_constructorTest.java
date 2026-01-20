package proguard.normalize;

import org.junit.jupiter.api.Test;
import proguard.pass.Pass;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link StringNormalizer} no-argument constructor.
 * Tests the default constructor StringNormalizer().
 */
public class StringNormalizerClaude_constructorTest {

    /**
     * Tests that the default constructor successfully creates a StringNormalizer instance.
     * Verifies that the no-argument constructor can be called without errors.
     */
    @Test
    public void testDefaultConstructor() {
        // Act - Create StringNormalizer using default constructor
        StringNormalizer stringNormalizer = new StringNormalizer();

        // Assert - Verify the instance was created successfully
        assertNotNull(stringNormalizer, "StringNormalizer should be instantiated successfully");
    }

    /**
     * Tests that the created StringNormalizer instance is a valid Pass implementation.
     * Verifies that the constructor creates an instance that implements the Pass interface.
     */
    @Test
    public void testConstructorCreatesPassImplementation() {
        // Act - Create StringNormalizer
        StringNormalizer stringNormalizer = new StringNormalizer();

        // Assert - Verify it implements Pass interface
        assertTrue(stringNormalizer instanceof Pass, "StringNormalizer should be an instance of Pass");
    }

    /**
     * Tests that multiple StringNormalizer instances can be created independently.
     * Verifies that each instance is a separate object.
     */
    @Test
    public void testMultipleInstances() {
        // Act - Create multiple StringNormalizer instances
        StringNormalizer normalizer1 = new StringNormalizer();
        StringNormalizer normalizer2 = new StringNormalizer();

        // Assert - Verify both instances were created and are different objects
        assertNotNull(normalizer1, "First StringNormalizer instance should be created");
        assertNotNull(normalizer2, "Second StringNormalizer instance should be created");
        assertNotSame(normalizer1, normalizer2, "StringNormalizer instances should be different objects");
    }

    /**
     * Tests that the constructor creates a StringNormalizer that can be used immediately.
     * Verifies the instance is in a valid state after construction.
     */
    @Test
    public void testConstructorCreatesUsableInstance() {
        // Act - Create StringNormalizer
        StringNormalizer stringNormalizer = new StringNormalizer();

        // Assert - Verify it's a non-null, usable instance
        assertNotNull(stringNormalizer, "StringNormalizer should be usable after construction");
        assertNotNull(stringNormalizer.toString(), "toString() should work on constructed instance");
    }

    /**
     * Tests that the constructor doesn't throw any exceptions.
     * Verifies that object creation completes without errors.
     */
    @Test
    public void testConstructorDoesNotThrow() {
        // Act & Assert - Constructor should not throw any exception
        assertDoesNotThrow(() -> new StringNormalizer(),
                "Constructor should not throw any exception");
    }

    /**
     * Tests that the created instance has the correct class type.
     * Verifies type identity after construction.
     */
    @Test
    public void testConstructorCreatesCorrectType() {
        // Act - Create StringNormalizer
        StringNormalizer stringNormalizer = new StringNormalizer();

        // Assert - Verify the exact type
        assertEquals(StringNormalizer.class, stringNormalizer.getClass(),
                "Instance should be of type StringNormalizer");
    }

    /**
     * Tests that newly created instances are independent and have no shared state.
     * Verifies that the constructor creates truly independent instances.
     */
    @Test
    public void testInstancesAreIndependent() {
        // Act - Create two instances
        StringNormalizer normalizer1 = new StringNormalizer();
        StringNormalizer normalizer2 = new StringNormalizer();

        // Assert - Verify they are independent instances
        assertNotSame(normalizer1, normalizer2,
                "Multiple constructor calls should create independent instances");
        assertFalse(normalizer1.equals(normalizer2) && normalizer1 == normalizer2,
                "Instances should be independent objects");
    }
}
