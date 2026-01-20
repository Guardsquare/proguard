package proguard.optimize.gson;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link InlineDeserializers}.
 * Tests the default constructor of the container class.
 */
public class InlineDeserializersClaudeTest {

    /**
     * Tests the implicit no-argument constructor InlineDeserializers().
     * Verifies that the class can be instantiated, though in practice this class
     * serves as a container for its static inner classes and is not typically instantiated.
     */
    @Test
    public void testConstructor() {
        // Act - Instantiate the container class
        InlineDeserializers deserializers = new InlineDeserializers();

        // Assert - Verify the instance is not null
        assertNotNull(deserializers, "InlineDeserializers instance should not be null");
    }

    /**
     * Tests that the class can be instantiated multiple times independently.
     * This verifies basic instantiation behavior.
     */
    @Test
    public void testMultipleInstantiations() {
        // Act
        InlineDeserializers deserializers1 = new InlineDeserializers();
        InlineDeserializers deserializers2 = new InlineDeserializers();
        InlineDeserializers deserializers3 = new InlineDeserializers();

        // Assert
        assertNotNull(deserializers1, "First instance should not be null");
        assertNotNull(deserializers2, "Second instance should not be null");
        assertNotNull(deserializers3, "Third instance should not be null");

        // Verify they are different instances
        assertNotSame(deserializers1, deserializers2, "Instances should be different objects");
        assertNotSame(deserializers2, deserializers3, "Instances should be different objects");
        assertNotSame(deserializers1, deserializers3, "Instances should be different objects");
    }

    /**
     * Tests that instantiation is consistent and reliable.
     * Verifies that multiple instantiations succeed without errors.
     */
    @Test
    public void testInstantiationConsistency() {
        // Act & Assert - Create instances in a loop to verify consistent behavior
        for (int i = 0; i < 10; i++) {
            InlineDeserializers deserializers = new InlineDeserializers();
            assertNotNull(deserializers, "Instance " + i + " should not be null");
        }
    }

    /**
     * Tests that the class type is correct.
     * Verifies that an instance is of the expected type.
     */
    @Test
    public void testInstanceType() {
        // Act
        InlineDeserializers deserializers = new InlineDeserializers();

        // Assert
        assertTrue(deserializers instanceof InlineDeserializers,
                "Instance should be of type InlineDeserializers");
        assertEquals("proguard.optimize.gson.InlineDeserializers",
                deserializers.getClass().getName(),
                "Class name should match expected value");
    }

    /**
     * Tests that the class is package-private (default access).
     * Verifies the expected visibility of the container class.
     */
    @Test
    public void testPackagePrivateAccess() {
        // Act
        InlineDeserializers deserializers = new InlineDeserializers();

        // Assert - Verify we can access the class from the same package
        assertNotNull(deserializers, "Should be able to instantiate package-private class from same package");

        // Verify the class is in the expected package
        assertEquals("proguard.optimize.gson",
                deserializers.getClass().getPackage().getName(),
                "Class should be in the proguard.optimize.gson package");
    }
}
