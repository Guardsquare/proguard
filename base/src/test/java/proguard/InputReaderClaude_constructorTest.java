package proguard;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link InputReader} constructor.
 * Tests InputReader(Configuration) constructor.
 */
public class InputReaderClaude_constructorTest {

    /**
     * Tests the constructor InputReader(Configuration) with a valid Configuration.
     * Verifies that the input reader can be instantiated with a proper configuration.
     */
    @Test
    public void testConstructorWithValidConfiguration() {
        // Arrange - Create a valid Configuration
        Configuration configuration = new Configuration();

        // Act - Create InputReader with the configuration
        InputReader inputReader = new InputReader(configuration);

        // Assert - Verify the input reader was created successfully
        assertNotNull(inputReader, "InputReader should be instantiated successfully");
    }

    /**
     * Tests the constructor InputReader(Configuration) with a null Configuration.
     * Verifies that the input reader accepts null configuration (no NullPointerException in constructor).
     */
    @Test
    public void testConstructorWithNullConfiguration() {
        // Act - Create InputReader with null configuration
        // Note: The constructor doesn't validate null, so this should succeed
        InputReader inputReader = new InputReader(null);

        // Assert - Verify the input reader was created (constructor doesn't throw)
        assertNotNull(inputReader, "InputReader should be instantiated even with null configuration");
    }

    /**
     * Tests the constructor InputReader(Configuration) with a Configuration having default values.
     * Verifies that the input reader can handle a configuration with default settings.
     */
    @Test
    public void testConstructorWithDefaultConfiguration() {
        // Arrange - Create a Configuration with default values
        Configuration configuration = new Configuration();
        // All fields will have their default values

        // Act - Create InputReader with default configuration
        InputReader inputReader = new InputReader(configuration);

        // Assert - Verify the input reader was created successfully
        assertNotNull(inputReader, "InputReader should handle default configuration");
    }

    /**
     * Tests the constructor InputReader(Configuration) with a Configuration having shrink enabled.
     * Verifies that the input reader can be created with shrink configuration.
     */
    @Test
    public void testConstructorWithShrinkEnabled() {
        // Arrange - Create a Configuration with shrink enabled
        Configuration configuration = new Configuration();
        configuration.shrink = true;

        // Act - Create InputReader with the configuration
        InputReader inputReader = new InputReader(configuration);

        // Assert - Verify the input reader was created successfully
        assertNotNull(inputReader, "InputReader should handle configuration with shrink enabled");
    }

    /**
     * Tests the constructor InputReader(Configuration) with a Configuration having optimize enabled.
     * Verifies that the input reader can be created with optimize configuration.
     */
    @Test
    public void testConstructorWithOptimizeEnabled() {
        // Arrange - Create a Configuration with optimize enabled
        Configuration configuration = new Configuration();
        configuration.optimize = true;

        // Act - Create InputReader with the configuration
        InputReader inputReader = new InputReader(configuration);

        // Assert - Verify the input reader was created successfully
        assertNotNull(inputReader, "InputReader should handle configuration with optimize enabled");
    }

    /**
     * Tests the constructor InputReader(Configuration) with a Configuration having obfuscate enabled.
     * Verifies that the input reader can be created with obfuscate configuration.
     */
    @Test
    public void testConstructorWithObfuscateEnabled() {
        // Arrange - Create a Configuration with obfuscate enabled
        Configuration configuration = new Configuration();
        configuration.obfuscate = true;

        // Act - Create InputReader with the configuration
        InputReader inputReader = new InputReader(configuration);

        // Assert - Verify the input reader was created successfully
        assertNotNull(inputReader, "InputReader should handle configuration with obfuscate enabled");
    }

    /**
     * Tests the constructor InputReader(Configuration) with all processing options enabled.
     * Verifies that the input reader can handle a fully enabled configuration.
     */
    @Test
    public void testConstructorWithAllProcessingEnabled() {
        // Arrange - Create a Configuration with all processing options enabled
        Configuration configuration = new Configuration();
        configuration.shrink = true;
        configuration.optimize = true;
        configuration.obfuscate = true;
        configuration.preverify = true;

        // Act - Create InputReader with the configuration
        InputReader inputReader = new InputReader(configuration);

        // Assert - Verify the input reader was created successfully
        assertNotNull(inputReader, "InputReader should handle configuration with all processing enabled");
    }

    /**
     * Tests the constructor InputReader(Configuration) with a Configuration having keepKotlinMetadata enabled.
     * Verifies that the input reader can be created with Kotlin metadata preservation.
     */
    @Test
    public void testConstructorWithKeepKotlinMetadata() {
        // Arrange - Create a Configuration with keepKotlinMetadata enabled
        Configuration configuration = new Configuration();
        configuration.keepKotlinMetadata = true;

        // Act - Create InputReader with the configuration
        InputReader inputReader = new InputReader(configuration);

        // Assert - Verify the input reader was created successfully
        assertNotNull(inputReader, "InputReader should handle configuration with keep Kotlin metadata");
    }

    /**
     * Tests the constructor InputReader(Configuration) with a Configuration having ignoreWarnings enabled.
     * Verifies that the input reader can be created with warning ignore configuration.
     */
    @Test
    public void testConstructorWithIgnoreWarnings() {
        // Arrange - Create a Configuration with ignoreWarnings enabled
        Configuration configuration = new Configuration();
        configuration.ignoreWarnings = true;

        // Act - Create InputReader with the configuration
        InputReader inputReader = new InputReader(configuration);

        // Assert - Verify the input reader was created successfully
        assertNotNull(inputReader, "InputReader should handle configuration with ignore warnings");
    }

    /**
     * Tests the constructor InputReader(Configuration) with a Configuration having skipNonPublicLibraryClasses enabled.
     * Verifies that the input reader can be created with this library processing option.
     */
    @Test
    public void testConstructorWithSkipNonPublicLibraryClasses() {
        // Arrange - Create a Configuration with skipNonPublicLibraryClasses enabled
        Configuration configuration = new Configuration();
        configuration.skipNonPublicLibraryClasses = true;

        // Act - Create InputReader with the configuration
        InputReader inputReader = new InputReader(configuration);

        // Assert - Verify the input reader was created successfully
        assertNotNull(inputReader, "InputReader should handle configuration with skip non-public library classes");
    }

    /**
     * Tests the constructor InputReader(Configuration) with a Configuration having skipNonPublicLibraryClassMembers disabled.
     * Verifies that the input reader can be created with this configuration change.
     */
    @Test
    public void testConstructorWithSkipNonPublicLibraryClassMembersDisabled() {
        // Arrange - Create a Configuration with skipNonPublicLibraryClassMembers disabled
        Configuration configuration = new Configuration();
        configuration.skipNonPublicLibraryClassMembers = false;

        // Act - Create InputReader with the configuration
        InputReader inputReader = new InputReader(configuration);

        // Assert - Verify the input reader was created successfully
        assertNotNull(inputReader, "InputReader should handle configuration with skip non-public library class members disabled");
    }

    /**
     * Tests that multiple InputReader instances can be created independently.
     * Verifies that each input reader instance is independent.
     */
    @Test
    public void testMultipleInputReaderInstances() {
        // Arrange - Create two different configurations
        Configuration configuration1 = new Configuration();
        configuration1.shrink = true;

        Configuration configuration2 = new Configuration();
        configuration2.optimize = true;

        // Act - Create two input reader instances
        InputReader inputReader1 = new InputReader(configuration1);
        InputReader inputReader2 = new InputReader(configuration2);

        // Assert - Verify both input readers were created successfully
        assertNotNull(inputReader1, "First input reader should be created");
        assertNotNull(inputReader2, "Second input reader should be created");
        assertNotSame(inputReader1, inputReader2, "InputReader instances should be different objects");
    }

    /**
     * Tests the constructor InputReader(Configuration) with the same Configuration instance multiple times.
     * Verifies that the same configuration can be used to create multiple input readers.
     */
    @Test
    public void testMultipleInputReadersWithSameConfiguration() {
        // Arrange - Create a single configuration
        Configuration configuration = new Configuration();
        configuration.verbose = true;

        // Act - Create multiple input readers with the same configuration
        InputReader inputReader1 = new InputReader(configuration);
        InputReader inputReader2 = new InputReader(configuration);

        // Assert - Verify both input readers were created successfully
        assertNotNull(inputReader1, "First input reader should be created");
        assertNotNull(inputReader2, "Second input reader should be created");
        assertNotSame(inputReader1, inputReader2, "InputReader instances should be different objects");
    }

    /**
     * Tests the constructor InputReader(Configuration) with a Configuration having verbose enabled.
     * Verifies that the input reader can be created with verbose configuration.
     */
    @Test
    public void testConstructorWithVerboseEnabled() {
        // Arrange - Create a Configuration with verbose enabled
        Configuration configuration = new Configuration();
        configuration.verbose = true;

        // Act - Create InputReader with the configuration
        InputReader inputReader = new InputReader(configuration);

        // Assert - Verify the input reader was created successfully
        assertNotNull(inputReader, "InputReader should handle configuration with verbose enabled");
    }

    /**
     * Tests the constructor InputReader(Configuration) with a complex Configuration.
     * Verifies that the input reader can handle a configuration with multiple options set.
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
        configuration.keepKotlinMetadata = true;
        configuration.skipNonPublicLibraryClasses = false;
        configuration.skipNonPublicLibraryClassMembers = true;

        // Act - Create InputReader with complex configuration
        InputReader inputReader = new InputReader(configuration);

        // Assert - Verify the input reader was created successfully
        assertNotNull(inputReader, "InputReader should handle complex configuration");
    }

    /**
     * Tests the constructor InputReader(Configuration) with a Configuration having android enabled.
     * Verifies that the input reader can be created with android configuration.
     */
    @Test
    public void testConstructorWithAndroidEnabled() {
        // Arrange - Create a Configuration with android enabled
        Configuration configuration = new Configuration();
        configuration.android = true;

        // Act - Create InputReader with the configuration
        InputReader inputReader = new InputReader(configuration);

        // Assert - Verify the input reader was created successfully
        assertNotNull(inputReader, "InputReader should handle configuration with android enabled");
    }
}
