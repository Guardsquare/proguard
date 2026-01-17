package proguard;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link Targeter} constructor.
 * Tests Targeter(Configuration) constructor.
 */
public class TargeterClaude_constructorTest {

    /**
     * Tests the constructor Targeter(Configuration) with a valid Configuration.
     * Verifies that the targeter can be instantiated with a proper configuration.
     */
    @Test
    public void testConstructorWithValidConfiguration() {
        // Arrange - Create a valid Configuration
        Configuration configuration = new Configuration();

        // Act - Create Targeter with the configuration
        Targeter targeter = new Targeter(configuration);

        // Assert - Verify the targeter was created successfully
        assertNotNull(targeter, "Targeter should be instantiated successfully");
    }

    /**
     * Tests the constructor Targeter(Configuration) with a null Configuration.
     * Verifies that the targeter accepts null configuration (no NullPointerException in constructor).
     */
    @Test
    public void testConstructorWithNullConfiguration() {
        // Act - Create Targeter with null configuration
        // Note: The constructor doesn't validate null, so this should succeed
        Targeter targeter = new Targeter(null);

        // Assert - Verify the targeter was created (constructor doesn't throw)
        assertNotNull(targeter, "Targeter should be instantiated even with null configuration");
    }

    /**
     * Tests the constructor Targeter(Configuration) with a Configuration having default values.
     * Verifies that the targeter can handle a configuration with default settings.
     */
    @Test
    public void testConstructorWithDefaultConfiguration() {
        // Arrange - Create a Configuration with default values
        Configuration configuration = new Configuration();
        // All fields will have their default values

        // Act - Create Targeter with default configuration
        Targeter targeter = new Targeter(configuration);

        // Assert - Verify the targeter was created successfully
        assertNotNull(targeter, "Targeter should handle default configuration");
    }

    /**
     * Tests the constructor Targeter(Configuration) with a Configuration having a specific target class version.
     * Verifies that the targeter can be created with a specific target version.
     */
    @Test
    public void testConstructorWithTargetClassVersion() {
        // Arrange - Create a Configuration with a target class version
        Configuration configuration = new Configuration();
        configuration.targetClassVersion = 50; // Java 6

        // Act - Create Targeter with the configuration
        Targeter targeter = new Targeter(configuration);

        // Assert - Verify the targeter was created successfully
        assertNotNull(targeter, "Targeter should handle configuration with target class version");
    }

    /**
     * Tests the constructor Targeter(Configuration) with a Configuration having warn enabled.
     * Verifies that the targeter can be created with warning configuration.
     */
    @Test
    public void testConstructorWithWarnEnabled() {
        // Arrange - Create a Configuration with warn enabled
        Configuration configuration = new Configuration();
        configuration.warn = new java.util.ArrayList<>();

        // Act - Create Targeter with the configuration
        Targeter targeter = new Targeter(configuration);

        // Assert - Verify the targeter was created successfully
        assertNotNull(targeter, "Targeter should handle configuration with warn enabled");
    }

    /**
     * Tests the constructor Targeter(Configuration) with a Configuration having ignoreWarnings enabled.
     * Verifies that the targeter can be created with ignore warnings configuration.
     */
    @Test
    public void testConstructorWithIgnoreWarnings() {
        // Arrange - Create a Configuration with ignoreWarnings enabled
        Configuration configuration = new Configuration();
        configuration.ignoreWarnings = true;

        // Act - Create Targeter with the configuration
        Targeter targeter = new Targeter(configuration);

        // Assert - Verify the targeter was created successfully
        assertNotNull(targeter, "Targeter should handle configuration with ignore warnings");
    }

    /**
     * Tests the constructor Targeter(Configuration) with a Configuration having both target version and ignore warnings.
     * Verifies that the targeter can handle multiple relevant configuration options.
     */
    @Test
    public void testConstructorWithTargetVersionAndIgnoreWarnings() {
        // Arrange - Create a Configuration with both options
        Configuration configuration = new Configuration();
        configuration.targetClassVersion = 52; // Java 8
        configuration.ignoreWarnings = true;

        // Act - Create Targeter with the configuration
        Targeter targeter = new Targeter(configuration);

        // Assert - Verify the targeter was created successfully
        assertNotNull(targeter, "Targeter should handle configuration with target version and ignore warnings");
    }

    /**
     * Tests the constructor Targeter(Configuration) with a Configuration having target class version set to zero.
     * Verifies that the targeter can handle zero target class version (no target version specified).
     */
    @Test
    public void testConstructorWithZeroTargetClassVersion() {
        // Arrange - Create a Configuration with zero target class version
        Configuration configuration = new Configuration();
        configuration.targetClassVersion = 0;

        // Act - Create Targeter with the configuration
        Targeter targeter = new Targeter(configuration);

        // Assert - Verify the targeter was created successfully
        assertNotNull(targeter, "Targeter should handle configuration with zero target class version");
    }

    /**
     * Tests the constructor Targeter(Configuration) with a Configuration having a high target class version.
     * Verifies that the targeter can handle modern Java versions.
     */
    @Test
    public void testConstructorWithHighTargetClassVersion() {
        // Arrange - Create a Configuration with a high target class version
        Configuration configuration = new Configuration();
        configuration.targetClassVersion = 65; // Java 21

        // Act - Create Targeter with the configuration
        Targeter targeter = new Targeter(configuration);

        // Assert - Verify the targeter was created successfully
        assertNotNull(targeter, "Targeter should handle configuration with high target class version");
    }

    /**
     * Tests that multiple Targeter instances can be created independently.
     * Verifies that each targeter instance is independent.
     */
    @Test
    public void testMultipleTargeterInstances() {
        // Arrange - Create two different configurations
        Configuration configuration1 = new Configuration();
        configuration1.targetClassVersion = 50;

        Configuration configuration2 = new Configuration();
        configuration2.targetClassVersion = 52;

        // Act - Create two targeter instances
        Targeter targeter1 = new Targeter(configuration1);
        Targeter targeter2 = new Targeter(configuration2);

        // Assert - Verify both targeters were created successfully
        assertNotNull(targeter1, "First targeter should be created");
        assertNotNull(targeter2, "Second targeter should be created");
        assertNotSame(targeter1, targeter2, "Targeter instances should be different objects");
    }

    /**
     * Tests the constructor Targeter(Configuration) with the same Configuration instance multiple times.
     * Verifies that the same configuration can be used to create multiple targeters.
     */
    @Test
    public void testMultipleTargetersWithSameConfiguration() {
        // Arrange - Create a single configuration
        Configuration configuration = new Configuration();
        configuration.targetClassVersion = 51;

        // Act - Create multiple targeters with the same configuration
        Targeter targeter1 = new Targeter(configuration);
        Targeter targeter2 = new Targeter(configuration);

        // Assert - Verify both targeters were created successfully
        assertNotNull(targeter1, "First targeter should be created");
        assertNotNull(targeter2, "Second targeter should be created");
        assertNotSame(targeter1, targeter2, "Targeter instances should be different objects");
    }

    /**
     * Tests the constructor Targeter(Configuration) with a Configuration having verbose enabled.
     * Verifies that the targeter can be created with verbose configuration.
     */
    @Test
    public void testConstructorWithVerboseEnabled() {
        // Arrange - Create a Configuration with verbose enabled
        Configuration configuration = new Configuration();
        configuration.verbose = true;

        // Act - Create Targeter with the configuration
        Targeter targeter = new Targeter(configuration);

        // Assert - Verify the targeter was created successfully
        assertNotNull(targeter, "Targeter should handle configuration with verbose enabled");
    }

    /**
     * Tests the constructor Targeter(Configuration) with a complex Configuration.
     * Verifies that the targeter can handle a configuration with multiple options set.
     */
    @Test
    public void testConstructorWithComplexConfiguration() {
        // Arrange - Create a Configuration with multiple options
        Configuration configuration = new Configuration();
        configuration.targetClassVersion = 52;
        configuration.ignoreWarnings = false;
        configuration.verbose = true;
        configuration.warn = new java.util.ArrayList<>();

        // Act - Create Targeter with complex configuration
        Targeter targeter = new Targeter(configuration);

        // Assert - Verify the targeter was created successfully
        assertNotNull(targeter, "Targeter should handle complex configuration");
    }

    /**
     * Tests the constructor Targeter(Configuration) with a Configuration having all relevant fields set.
     * Verifies complete initialization scenario.
     */
    @Test
    public void testConstructorWithAllRelevantFields() {
        // Arrange - Create a Configuration with all Targeter-relevant fields
        Configuration configuration = new Configuration();
        configuration.targetClassVersion = 55; // Java 11
        configuration.warn = null; // warnings disabled
        configuration.ignoreWarnings = true;
        configuration.verbose = true;

        // Act - Create Targeter with the configuration
        Targeter targeter = new Targeter(configuration);

        // Assert - Verify the targeter was created successfully
        assertNotNull(targeter, "Targeter should handle configuration with all relevant fields");
    }

    /**
     * Tests the constructor Targeter(Configuration) with a negative target class version.
     * Verifies that the targeter can handle invalid/unusual target version values.
     */
    @Test
    public void testConstructorWithNegativeTargetClassVersion() {
        // Arrange - Create a Configuration with negative target class version
        Configuration configuration = new Configuration();
        configuration.targetClassVersion = -1;

        // Act - Create Targeter with the configuration
        Targeter targeter = new Targeter(configuration);

        // Assert - Verify the targeter was created successfully
        // Constructor doesn't validate the value, so it should succeed
        assertNotNull(targeter, "Targeter should be created even with negative target class version");
    }

    /**
     * Tests the constructor Targeter(Configuration) with a very old target class version.
     * Verifies that the targeter can handle legacy Java versions.
     */
    @Test
    public void testConstructorWithOldTargetClassVersion() {
        // Arrange - Create a Configuration with old target class version
        Configuration configuration = new Configuration();
        configuration.targetClassVersion = 45; // Java 1.1

        // Act - Create Targeter with the configuration
        Targeter targeter = new Targeter(configuration);

        // Assert - Verify the targeter was created successfully
        assertNotNull(targeter, "Targeter should handle configuration with old target class version");
    }
}
