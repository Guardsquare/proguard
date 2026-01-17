package proguard;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link Initializer} constructor.
 * Tests Initializer(Configuration) constructor.
 */
public class InitializerClaude_constructorTest {

    /**
     * Tests the constructor Initializer(Configuration) with a valid Configuration.
     * Verifies that the initializer can be instantiated with a proper configuration.
     */
    @Test
    public void testConstructorWithValidConfiguration() {
        // Arrange - Create a valid Configuration
        Configuration configuration = new Configuration();

        // Act - Create Initializer with the configuration
        Initializer initializer = new Initializer(configuration);

        // Assert - Verify the initializer was created successfully
        assertNotNull(initializer, "Initializer should be instantiated successfully");
    }

    /**
     * Tests the constructor Initializer(Configuration) with a null Configuration.
     * Verifies that the initializer accepts null configuration (no NullPointerException in constructor).
     */
    @Test
    public void testConstructorWithNullConfiguration() {
        // Act - Create Initializer with null configuration
        // Note: The constructor doesn't validate null, so this should succeed
        Initializer initializer = new Initializer(null);

        // Assert - Verify the initializer was created (constructor doesn't throw)
        assertNotNull(initializer, "Initializer should be instantiated even with null configuration");
    }

    /**
     * Tests the constructor Initializer(Configuration) with a Configuration having default values.
     * Verifies that the initializer can handle a configuration with default settings.
     */
    @Test
    public void testConstructorWithDefaultConfiguration() {
        // Arrange - Create a Configuration with default values
        Configuration configuration = new Configuration();
        // All fields will have their default values

        // Act - Create Initializer with default configuration
        Initializer initializer = new Initializer(configuration);

        // Assert - Verify the initializer was created successfully
        assertNotNull(initializer, "Initializer should handle default configuration");
    }

    /**
     * Tests the constructor Initializer(Configuration) with a Configuration having shrink enabled.
     * Verifies that the initializer can be created with shrink configuration.
     */
    @Test
    public void testConstructorWithShrinkEnabled() {
        // Arrange - Create a Configuration with shrink enabled
        Configuration configuration = new Configuration();
        configuration.shrink = true;

        // Act - Create Initializer with the configuration
        Initializer initializer = new Initializer(configuration);

        // Assert - Verify the initializer was created successfully
        assertNotNull(initializer, "Initializer should handle configuration with shrink enabled");
    }

    /**
     * Tests the constructor Initializer(Configuration) with a Configuration having optimize enabled.
     * Verifies that the initializer can be created with optimize configuration.
     */
    @Test
    public void testConstructorWithOptimizeEnabled() {
        // Arrange - Create a Configuration with optimize enabled
        Configuration configuration = new Configuration();
        configuration.optimize = true;

        // Act - Create Initializer with the configuration
        Initializer initializer = new Initializer(configuration);

        // Assert - Verify the initializer was created successfully
        assertNotNull(initializer, "Initializer should handle configuration with optimize enabled");
    }

    /**
     * Tests the constructor Initializer(Configuration) with a Configuration having obfuscate enabled.
     * Verifies that the initializer can be created with obfuscate configuration.
     */
    @Test
    public void testConstructorWithObfuscateEnabled() {
        // Arrange - Create a Configuration with obfuscate enabled
        Configuration configuration = new Configuration();
        configuration.obfuscate = true;

        // Act - Create Initializer with the configuration
        Initializer initializer = new Initializer(configuration);

        // Assert - Verify the initializer was created successfully
        assertNotNull(initializer, "Initializer should handle configuration with obfuscate enabled");
    }

    /**
     * Tests the constructor Initializer(Configuration) with all processing options enabled.
     * Verifies that the initializer can handle a fully enabled configuration.
     */
    @Test
    public void testConstructorWithAllProcessingEnabled() {
        // Arrange - Create a Configuration with all processing options enabled
        Configuration configuration = new Configuration();
        configuration.shrink = true;
        configuration.optimize = true;
        configuration.obfuscate = true;
        configuration.preverify = true;

        // Act - Create Initializer with the configuration
        Initializer initializer = new Initializer(configuration);

        // Assert - Verify the initializer was created successfully
        assertNotNull(initializer, "Initializer should handle configuration with all processing enabled");
    }

    /**
     * Tests the constructor Initializer(Configuration) with a Configuration having useUniqueClassMemberNames enabled.
     * Verifies that the initializer can be created with this specific configuration option.
     */
    @Test
    public void testConstructorWithUniqueClassMemberNames() {
        // Arrange - Create a Configuration with useUniqueClassMemberNames
        Configuration configuration = new Configuration();
        configuration.useUniqueClassMemberNames = true;

        // Act - Create Initializer with the configuration
        Initializer initializer = new Initializer(configuration);

        // Assert - Verify the initializer was created successfully
        assertNotNull(initializer, "Initializer should handle configuration with unique class member names");
    }

    /**
     * Tests the constructor Initializer(Configuration) with a Configuration having keepKotlinMetadata enabled.
     * Verifies that the initializer can be created with Kotlin metadata preservation.
     */
    @Test
    public void testConstructorWithKeepKotlinMetadata() {
        // Arrange - Create a Configuration with keepKotlinMetadata enabled
        Configuration configuration = new Configuration();
        configuration.keepKotlinMetadata = true;

        // Act - Create Initializer with the configuration
        Initializer initializer = new Initializer(configuration);

        // Assert - Verify the initializer was created successfully
        assertNotNull(initializer, "Initializer should handle configuration with keep Kotlin metadata");
    }

    /**
     * Tests the constructor Initializer(Configuration) with a Configuration having ignoreWarnings enabled.
     * Verifies that the initializer can be created with warning ignore configuration.
     */
    @Test
    public void testConstructorWithIgnoreWarnings() {
        // Arrange - Create a Configuration with ignoreWarnings enabled
        Configuration configuration = new Configuration();
        configuration.ignoreWarnings = true;

        // Act - Create Initializer with the configuration
        Initializer initializer = new Initializer(configuration);

        // Assert - Verify the initializer was created successfully
        assertNotNull(initializer, "Initializer should handle configuration with ignore warnings");
    }

    /**
     * Tests the constructor Initializer(Configuration) with a Configuration having skipNonPublicLibraryClasses enabled.
     * Verifies that the initializer can be created with this library processing option.
     */
    @Test
    public void testConstructorWithSkipNonPublicLibraryClasses() {
        // Arrange - Create a Configuration with skipNonPublicLibraryClasses enabled
        Configuration configuration = new Configuration();
        configuration.skipNonPublicLibraryClasses = true;

        // Act - Create Initializer with the configuration
        Initializer initializer = new Initializer(configuration);

        // Assert - Verify the initializer was created successfully
        assertNotNull(initializer, "Initializer should handle configuration with skip non-public library classes");
    }

    /**
     * Tests the constructor Initializer(Configuration) with a Configuration having skipNonPublicLibraryClassMembers disabled.
     * Verifies that the initializer can be created with this configuration change.
     */
    @Test
    public void testConstructorWithSkipNonPublicLibraryClassMembersDisabled() {
        // Arrange - Create a Configuration with skipNonPublicLibraryClassMembers disabled
        Configuration configuration = new Configuration();
        configuration.skipNonPublicLibraryClassMembers = false;

        // Act - Create Initializer with the configuration
        Initializer initializer = new Initializer(configuration);

        // Assert - Verify the initializer was created successfully
        assertNotNull(initializer, "Initializer should handle configuration with skip non-public library class members disabled");
    }

    /**
     * Tests that multiple Initializer instances can be created independently.
     * Verifies that each initializer instance is independent.
     */
    @Test
    public void testMultipleInitializerInstances() {
        // Arrange - Create two different configurations
        Configuration configuration1 = new Configuration();
        configuration1.shrink = true;

        Configuration configuration2 = new Configuration();
        configuration2.optimize = true;

        // Act - Create two initializer instances
        Initializer initializer1 = new Initializer(configuration1);
        Initializer initializer2 = new Initializer(configuration2);

        // Assert - Verify both initializers were created successfully
        assertNotNull(initializer1, "First initializer should be created");
        assertNotNull(initializer2, "Second initializer should be created");
        assertNotSame(initializer1, initializer2, "Initializer instances should be different objects");
    }

    /**
     * Tests the constructor Initializer(Configuration) with the same Configuration instance multiple times.
     * Verifies that the same configuration can be used to create multiple initializers.
     */
    @Test
    public void testMultipleInitializersWithSameConfiguration() {
        // Arrange - Create a single configuration
        Configuration configuration = new Configuration();
        configuration.verbose = true;

        // Act - Create multiple initializers with the same configuration
        Initializer initializer1 = new Initializer(configuration);
        Initializer initializer2 = new Initializer(configuration);

        // Assert - Verify both initializers were created successfully
        assertNotNull(initializer1, "First initializer should be created");
        assertNotNull(initializer2, "Second initializer should be created");
        assertNotSame(initializer1, initializer2, "Initializer instances should be different objects");
    }

    /**
     * Tests the constructor Initializer(Configuration) with a Configuration having verbose enabled.
     * Verifies that the initializer can be created with verbose configuration.
     */
    @Test
    public void testConstructorWithVerboseEnabled() {
        // Arrange - Create a Configuration with verbose enabled
        Configuration configuration = new Configuration();
        configuration.verbose = true;

        // Act - Create Initializer with the configuration
        Initializer initializer = new Initializer(configuration);

        // Assert - Verify the initializer was created successfully
        assertNotNull(initializer, "Initializer should handle configuration with verbose enabled");
    }

    /**
     * Tests the constructor Initializer(Configuration) with a complex Configuration.
     * Verifies that the initializer can handle a configuration with multiple options set.
     */
    @Test
    public void testConstructorWithComplexConfiguration() {
        // Arrange - Create a Configuration with multiple options
        Configuration configuration = new Configuration();
        configuration.shrink = true;
        configuration.optimize = true;
        configuration.obfuscate = false;
        configuration.verbose = true;
        configuration.ignoreWarnings = false;
        configuration.useUniqueClassMemberNames = false;
        configuration.keepKotlinMetadata = true;
        configuration.skipNonPublicLibraryClasses = false;
        configuration.skipNonPublicLibraryClassMembers = true;

        // Act - Create Initializer with complex configuration
        Initializer initializer = new Initializer(configuration);

        // Assert - Verify the initializer was created successfully
        assertNotNull(initializer, "Initializer should handle complex configuration");
    }
}
