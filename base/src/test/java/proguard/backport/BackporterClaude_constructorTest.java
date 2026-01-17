package proguard.backport;

import org.junit.jupiter.api.Test;
import proguard.Configuration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link Backporter} constructor.
 * Tests Backporter(Configuration) constructor.
 */
public class BackporterClaude_constructorTest {

    /**
     * Tests the constructor Backporter(Configuration) with a valid Configuration.
     * Verifies that the backporter can be instantiated with a proper configuration.
     */
    @Test
    public void testConstructorWithValidConfiguration() {
        // Arrange - Create a valid Configuration
        Configuration configuration = new Configuration();

        // Act - Create Backporter with the configuration
        Backporter backporter = new Backporter(configuration);

        // Assert - Verify the backporter was created successfully
        assertNotNull(backporter, "Backporter should be instantiated successfully");
    }

    /**
     * Tests the constructor Backporter(Configuration) with a null Configuration.
     * Verifies that the backporter accepts null configuration (no NullPointerException in constructor).
     */
    @Test
    public void testConstructorWithNullConfiguration() {
        // Act - Create Backporter with null configuration
        // Note: The constructor doesn't validate null, so this should succeed
        Backporter backporter = new Backporter(null);

        // Assert - Verify the backporter was created (constructor doesn't throw)
        assertNotNull(backporter, "Backporter should be instantiated even with null configuration");
    }

    /**
     * Tests the constructor Backporter(Configuration) with a Configuration having default values.
     * Verifies that the backporter can handle a configuration with default settings.
     */
    @Test
    public void testConstructorWithDefaultConfiguration() {
        // Arrange - Create a Configuration with default values
        Configuration configuration = new Configuration();
        // All fields will have their default values

        // Act - Create Backporter with default configuration
        Backporter backporter = new Backporter(configuration);

        // Assert - Verify the backporter was created successfully
        assertNotNull(backporter, "Backporter should handle default configuration");
    }

    /**
     * Tests the constructor Backporter(Configuration) with a Configuration having targetClassVersion set.
     * Verifies that the backporter can be created with a specific target class version.
     */
    @Test
    public void testConstructorWithTargetClassVersion() {
        // Arrange - Create a Configuration with target class version set
        Configuration configuration = new Configuration();
        configuration.targetClassVersion = 52; // Java 8

        // Act - Create Backporter with the configuration
        Backporter backporter = new Backporter(configuration);

        // Assert - Verify the backporter was created successfully
        assertNotNull(backporter, "Backporter should handle configuration with target class version");
    }

    /**
     * Tests the constructor Backporter(Configuration) with a Configuration having targetClassVersion set to 0.
     * Verifies that the backporter can be created with zero target class version.
     */
    @Test
    public void testConstructorWithZeroTargetClassVersion() {
        // Arrange - Create a Configuration with target class version 0
        Configuration configuration = new Configuration();
        configuration.targetClassVersion = 0;

        // Act - Create Backporter with the configuration
        Backporter backporter = new Backporter(configuration);

        // Assert - Verify the backporter was created successfully
        assertNotNull(backporter, "Backporter should handle configuration with zero target class version");
    }

    /**
     * Tests the constructor Backporter(Configuration) with a Configuration having allowAccessModification enabled.
     * Verifies that the backporter can be created with access modification allowed.
     */
    @Test
    public void testConstructorWithAllowAccessModificationEnabled() {
        // Arrange - Create a Configuration with allowAccessModification enabled
        Configuration configuration = new Configuration();
        configuration.allowAccessModification = true;

        // Act - Create Backporter with the configuration
        Backporter backporter = new Backporter(configuration);

        // Assert - Verify the backporter was created successfully
        assertNotNull(backporter, "Backporter should handle configuration with allow access modification enabled");
    }

    /**
     * Tests the constructor Backporter(Configuration) with a Configuration having allowAccessModification disabled.
     * Verifies that the backporter can be created with access modification disabled.
     */
    @Test
    public void testConstructorWithAllowAccessModificationDisabled() {
        // Arrange - Create a Configuration with allowAccessModification disabled
        Configuration configuration = new Configuration();
        configuration.allowAccessModification = false;

        // Act - Create Backporter with the configuration
        Backporter backporter = new Backporter(configuration);

        // Assert - Verify the backporter was created successfully
        assertNotNull(backporter, "Backporter should handle configuration with allow access modification disabled");
    }

    /**
     * Tests the constructor Backporter(Configuration) with a Configuration having various target class versions.
     * Verifies that the backporter can be created with different Java version targets.
     */
    @Test
    public void testConstructorWithDifferentTargetClassVersions() {
        // Test with Java 7 target
        Configuration config1 = new Configuration();
        config1.targetClassVersion = 51; // Java 7
        Backporter backporter1 = new Backporter(config1);
        assertNotNull(backporter1, "Backporter should handle Java 7 target");

        // Test with Java 8 target
        Configuration config2 = new Configuration();
        config2.targetClassVersion = 52; // Java 8
        Backporter backporter2 = new Backporter(config2);
        assertNotNull(backporter2, "Backporter should handle Java 8 target");

        // Test with Java 9 target
        Configuration config3 = new Configuration();
        config3.targetClassVersion = 53; // Java 9
        Backporter backporter3 = new Backporter(config3);
        assertNotNull(backporter3, "Backporter should handle Java 9 target");
    }

    /**
     * Tests that multiple Backporter instances can be created independently.
     * Verifies that each backporter instance is independent.
     */
    @Test
    public void testMultipleBackporterInstances() {
        // Arrange - Create two different configurations
        Configuration configuration1 = new Configuration();
        configuration1.targetClassVersion = 51; // Java 7

        Configuration configuration2 = new Configuration();
        configuration2.targetClassVersion = 52; // Java 8

        // Act - Create two backporter instances
        Backporter backporter1 = new Backporter(configuration1);
        Backporter backporter2 = new Backporter(configuration2);

        // Assert - Verify both backporters were created successfully
        assertNotNull(backporter1, "First backporter should be created");
        assertNotNull(backporter2, "Second backporter should be created");
        assertNotSame(backporter1, backporter2, "Backporter instances should be different objects");
    }

    /**
     * Tests the constructor Backporter(Configuration) with the same Configuration instance multiple times.
     * Verifies that the same configuration can be used to create multiple backporters.
     */
    @Test
    public void testMultipleBackportersWithSameConfiguration() {
        // Arrange - Create a single configuration
        Configuration configuration = new Configuration();
        configuration.targetClassVersion = 51;

        // Act - Create multiple backporters with the same configuration
        Backporter backporter1 = new Backporter(configuration);
        Backporter backporter2 = new Backporter(configuration);

        // Assert - Verify both backporters were created successfully
        assertNotNull(backporter1, "First backporter should be created");
        assertNotNull(backporter2, "Second backporter should be created");
        assertNotSame(backporter1, backporter2, "Backporter instances should be different objects");
    }

    /**
     * Tests the constructor Backporter(Configuration) with a complex Configuration.
     * Verifies that the backporter can handle a configuration with multiple options set.
     */
    @Test
    public void testConstructorWithComplexConfiguration() {
        // Arrange - Create a Configuration with multiple options
        Configuration configuration = new Configuration();
        configuration.targetClassVersion = 51; // Java 7
        configuration.allowAccessModification = true;
        configuration.shrink = true;
        configuration.optimize = true;
        configuration.obfuscate = false;
        configuration.verbose = true;

        // Act - Create Backporter with complex configuration
        Backporter backporter = new Backporter(configuration);

        // Assert - Verify the backporter was created successfully
        assertNotNull(backporter, "Backporter should handle complex configuration");
    }

    /**
     * Tests the constructor Backporter(Configuration) with a Configuration having high target class version.
     * Verifies that the backporter can be created with modern Java version targets.
     */
    @Test
    public void testConstructorWithHighTargetClassVersion() {
        // Arrange - Create a Configuration with high target class version
        Configuration configuration = new Configuration();
        configuration.targetClassVersion = 61; // Java 17

        // Act - Create Backporter with the configuration
        Backporter backporter = new Backporter(configuration);

        // Assert - Verify the backporter was created successfully
        assertNotNull(backporter, "Backporter should handle configuration with high target class version");
    }

    /**
     * Tests the constructor Backporter(Configuration) with a Configuration having negative target class version.
     * Verifies that the backporter accepts negative target class version (edge case).
     */
    @Test
    public void testConstructorWithNegativeTargetClassVersion() {
        // Arrange - Create a Configuration with negative target class version
        Configuration configuration = new Configuration();
        configuration.targetClassVersion = -1;

        // Act - Create Backporter with the configuration
        Backporter backporter = new Backporter(configuration);

        // Assert - Verify the backporter was created successfully
        assertNotNull(backporter, "Backporter should handle configuration with negative target class version");
    }
}
