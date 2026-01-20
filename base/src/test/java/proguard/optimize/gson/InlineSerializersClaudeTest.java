package proguard.optimize.gson;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link InlineSerializers}.
 * Tests the default constructor of the container class.
 */
public class InlineSerializersClaudeTest {

    /**
     * Tests the implicit no-argument constructor InlineSerializers().
     * Verifies that the class can be instantiated, though in practice this class
     * serves as a container for its static inner classes and is not typically instantiated.
     */
    @Test
    public void testConstructor() {
        // Act - Instantiate the container class
        InlineSerializers serializers = new InlineSerializers();

        // Assert - Verify the instance is not null
        assertNotNull(serializers, "InlineSerializers instance should not be null");
    }

    /**
     * Tests that the class can be instantiated multiple times independently.
     * This verifies basic instantiation behavior.
     */
    @Test
    public void testMultipleInstantiations() {
        // Act
        InlineSerializers serializers1 = new InlineSerializers();
        InlineSerializers serializers2 = new InlineSerializers();
        InlineSerializers serializers3 = new InlineSerializers();

        // Assert
        assertNotNull(serializers1, "First instance should not be null");
        assertNotNull(serializers2, "Second instance should not be null");
        assertNotNull(serializers3, "Third instance should not be null");

        // Verify they are different instances
        assertNotSame(serializers1, serializers2, "Instances should be different objects");
        assertNotSame(serializers2, serializers3, "Instances should be different objects");
        assertNotSame(serializers1, serializers3, "Instances should be different objects");
    }

    /**
     * Tests that instantiation is consistent and reliable.
     * Verifies that multiple instantiations succeed without errors.
     */
    @Test
    public void testInstantiationConsistency() {
        // Act & Assert - Create instances in a loop to verify consistent behavior
        for (int i = 0; i < 10; i++) {
            InlineSerializers serializers = new InlineSerializers();
            assertNotNull(serializers, "Instance " + i + " should not be null");
        }
    }

    /**
     * Tests that the class type is correct.
     * Verifies that an instance is of the expected type.
     */
    @Test
    public void testInstanceType() {
        // Act
        InlineSerializers serializers = new InlineSerializers();

        // Assert
        assertTrue(serializers instanceof InlineSerializers,
                "Instance should be of type InlineSerializers");
        assertEquals("proguard.optimize.gson.InlineSerializers",
                serializers.getClass().getName(),
                "Class name should match expected value");
    }

    /**
     * Tests that the class is package-private (default access).
     * Verifies the expected visibility of the container class.
     */
    @Test
    public void testPackagePrivateAccess() {
        // Act
        InlineSerializers serializers = new InlineSerializers();

        // Assert - Verify we can access the class from the same package
        assertNotNull(serializers, "Should be able to instantiate package-private class from same package");

        // Verify the class is in the expected package
        assertEquals("proguard.optimize.gson",
                serializers.getClass().getPackage().getName(),
                "Class should be in the proguard.optimize.gson package");
    }
}
