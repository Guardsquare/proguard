package proguard.obfuscate;

import org.junit.jupiter.api.Test;
import proguard.Configuration;
import proguard.pass.Pass;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ResourceFileNameAdapter} constructor.
 * Tests the constructor:
 * <init>.(Lproguard/Configuration;)V
 */
public class ResourceFileNameAdapterClaude_constructorTest {

    /**
     * Tests the constructor with a valid Configuration object.
     * Verifies that a ResourceFileNameAdapter can be instantiated with a valid configuration.
     */
    @Test
    public void testConstructorWithValidConfiguration() {
        // Arrange
        Configuration configuration = new Configuration();

        // Act
        ResourceFileNameAdapter adapter = new ResourceFileNameAdapter(configuration);

        // Assert
        assertNotNull(adapter, "ResourceFileNameAdapter should be created successfully");
    }

    /**
     * Tests the constructor with a null Configuration.
     * Verifies that the constructor accepts null configuration (may be needed for certain use cases).
     */
    @Test
    public void testConstructorWithNullConfiguration() {
        // Act
        ResourceFileNameAdapter adapter = new ResourceFileNameAdapter(null);

        // Assert
        assertNotNull(adapter, "ResourceFileNameAdapter should be created with null configuration");
    }

    /**
     * Tests that the adapter implements the Pass interface.
     * Verifies that it can be used as a Pass.
     */
    @Test
    public void testAdapterImplementsPass() {
        // Arrange
        Configuration configuration = new Configuration();

        // Act
        ResourceFileNameAdapter adapter = new ResourceFileNameAdapter(configuration);

        // Assert
        assertTrue(adapter instanceof Pass,
                   "ResourceFileNameAdapter should implement Pass interface");
    }

    /**
     * Tests that multiple adapters can be created with different configurations.
     * Verifies that each instance is independent.
     */
    @Test
    public void testMultipleAdapterInstances() {
        // Arrange
        Configuration config1 = new Configuration();
        Configuration config2 = new Configuration();

        // Act
        ResourceFileNameAdapter adapter1 = new ResourceFileNameAdapter(config1);
        ResourceFileNameAdapter adapter2 = new ResourceFileNameAdapter(config2);

        // Assert
        assertNotNull(adapter1, "First adapter should be created");
        assertNotNull(adapter2, "Second adapter should be created");
        assertNotSame(adapter1, adapter2, "Adapters should be different instances");
    }

    /**
     * Tests that the constructor with the same configuration creates different instances.
     * Verifies that each constructor call creates a new instance.
     */
    @Test
    public void testConstructorCreatesDifferentInstances() {
        // Arrange
        Configuration configuration = new Configuration();

        // Act
        ResourceFileNameAdapter adapter1 = new ResourceFileNameAdapter(configuration);
        ResourceFileNameAdapter adapter2 = new ResourceFileNameAdapter(configuration);

        // Assert
        assertNotSame(adapter1, adapter2, "Each constructor call should create a new instance");
    }

    /**
     * Tests that adapter can be assigned to Pass reference.
     * Verifies interface implementation.
     */
    @Test
    public void testAdapterAsPass() {
        // Arrange
        Configuration configuration = new Configuration();

        // Act
        Pass adapter = new ResourceFileNameAdapter(configuration);

        // Assert
        assertNotNull(adapter, "ResourceFileNameAdapter should be assignable to Pass");
    }

    /**
     * Tests that the constructor completes quickly and efficiently.
     * Verifies that the constructor doesn't perform heavy operations.
     */
    @Test
    public void testConstructorIsEfficient() {
        // Arrange
        Configuration configuration = new Configuration();
        long startTime = System.nanoTime();

        // Act
        ResourceFileNameAdapter adapter = new ResourceFileNameAdapter(configuration);

        // Assert
        long duration = System.nanoTime() - startTime;
        assertNotNull(adapter, "Adapter should be created");
        // Constructor should complete in less than 10 milliseconds
        assertTrue(duration < 10_000_000L,
            "Constructor should complete quickly (took " + duration + " ns)");
    }

    /**
     * Tests creating multiple adapters with the same configuration object.
     * Verifies that multiple instances can share the same configuration.
     */
    @Test
    public void testMultipleAdaptersWithSameConfiguration() {
        // Arrange
        Configuration sharedConfiguration = new Configuration();

        // Act
        ResourceFileNameAdapter adapter1 = new ResourceFileNameAdapter(sharedConfiguration);
        ResourceFileNameAdapter adapter2 = new ResourceFileNameAdapter(sharedConfiguration);

        // Assert
        assertNotNull(adapter1, "First adapter should be created");
        assertNotNull(adapter2, "Second adapter should be created");
        assertNotSame(adapter1, adapter2, "Adapter instances should be different");
    }

    /**
     * Tests that the constructor works with a configuration that has default values.
     * Verifies that a freshly created Configuration is acceptable.
     */
    @Test
    public void testConstructorWithDefaultConfiguration() {
        // Arrange
        Configuration configuration = new Configuration();
        // Configuration has default values

        // Act
        ResourceFileNameAdapter adapter = new ResourceFileNameAdapter(configuration);

        // Assert
        assertNotNull(adapter, "ResourceFileNameAdapter should be created with default configuration");
    }

    /**
     * Tests creating many adapters to verify no resource leaks.
     * Verifies that the constructor can be called many times without issues.
     */
    @Test
    public void testConstructorCanBeCalledMultipleTimes() {
        // Arrange
        Configuration configuration = new Configuration();

        // Act & Assert
        for (int i = 0; i < 100; i++) {
            ResourceFileNameAdapter adapter = new ResourceFileNameAdapter(configuration);
            assertNotNull(adapter, "Adapter " + i + " should be created successfully");
        }
    }

    /**
     * Tests that the adapter is a concrete class and can be instantiated.
     * Verifies that ResourceFileNameAdapter is not abstract.
     */
    @Test
    public void testAdapterIsConcreteClass() {
        // Arrange
        Configuration configuration = new Configuration();

        // Act
        ResourceFileNameAdapter adapter = new ResourceFileNameAdapter(configuration);

        // Assert
        assertNotNull(adapter, "ResourceFileNameAdapter should be a concrete class");
        assertEquals("proguard.obfuscate.ResourceFileNameAdapter",
                     adapter.getClass().getName(),
                     "Class name should match");
    }
}
