package proguard;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ProGuard} constructor.
 * Tests ProGuard(Configuration) constructor.
 */
public class ProGuardClaude_constructorTest {

    /**
     * Tests the constructor ProGuard(Configuration) with a valid Configuration.
     * Verifies that the ProGuard instance can be instantiated with a proper configuration.
     */
    @Test
    public void testConstructorWithValidConfiguration() {
        // Arrange - Create a valid Configuration
        Configuration configuration = new Configuration();

        // Act - Create ProGuard with the configuration
        ProGuard proGuard = new ProGuard(configuration);

        // Assert - Verify the ProGuard instance was created successfully
        assertNotNull(proGuard, "ProGuard should be instantiated successfully");
    }

    /**
     * Tests the constructor ProGuard(Configuration) with a null Configuration.
     * Verifies that the ProGuard constructor accepts null configuration (no NullPointerException in constructor).
     */
    @Test
    public void testConstructorWithNullConfiguration() {
        // Act - Create ProGuard with null configuration
        // Note: The constructor doesn't validate null, so this should succeed
        ProGuard proGuard = new ProGuard(null);

        // Assert - Verify the ProGuard instance was created (constructor doesn't throw)
        assertNotNull(proGuard, "ProGuard should be instantiated even with null configuration");
    }

    /**
     * Tests the constructor ProGuard(Configuration) with a Configuration having default values.
     * Verifies that ProGuard can handle a configuration with default settings.
     */
    @Test
    public void testConstructorWithDefaultConfiguration() {
        // Arrange - Create a Configuration with default values
        Configuration configuration = new Configuration();
        // All fields will have their default values

        // Act - Create ProGuard with default configuration
        ProGuard proGuard = new ProGuard(configuration);

        // Assert - Verify the ProGuard instance was created successfully
        assertNotNull(proGuard, "ProGuard should handle default configuration");
    }

    /**
     * Tests the constructor ProGuard(Configuration) with a Configuration having shrink enabled.
     * Verifies that ProGuard can be created with shrink configuration.
     */
    @Test
    public void testConstructorWithShrinkEnabled() {
        // Arrange - Create a Configuration with shrink enabled
        Configuration configuration = new Configuration();
        configuration.shrink = true;

        // Act - Create ProGuard with the configuration
        ProGuard proGuard = new ProGuard(configuration);

        // Assert - Verify the ProGuard instance was created successfully
        assertNotNull(proGuard, "ProGuard should handle configuration with shrink enabled");
    }

    /**
     * Tests the constructor ProGuard(Configuration) with a Configuration having optimize enabled.
     * Verifies that ProGuard can be created with optimize configuration.
     */
    @Test
    public void testConstructorWithOptimizeEnabled() {
        // Arrange - Create a Configuration with optimize enabled
        Configuration configuration = new Configuration();
        configuration.optimize = true;

        // Act - Create ProGuard with the configuration
        ProGuard proGuard = new ProGuard(configuration);

        // Assert - Verify the ProGuard instance was created successfully
        assertNotNull(proGuard, "ProGuard should handle configuration with optimize enabled");
    }

    /**
     * Tests the constructor ProGuard(Configuration) with a Configuration having obfuscate enabled.
     * Verifies that ProGuard can be created with obfuscate configuration.
     */
    @Test
    public void testConstructorWithObfuscateEnabled() {
        // Arrange - Create a Configuration with obfuscate enabled
        Configuration configuration = new Configuration();
        configuration.obfuscate = true;

        // Act - Create ProGuard with the configuration
        ProGuard proGuard = new ProGuard(configuration);

        // Assert - Verify the ProGuard instance was created successfully
        assertNotNull(proGuard, "ProGuard should handle configuration with obfuscate enabled");
    }

    /**
     * Tests the constructor ProGuard(Configuration) with all processing options enabled.
     * Verifies that ProGuard can handle a fully enabled configuration.
     */
    @Test
    public void testConstructorWithAllProcessingEnabled() {
        // Arrange - Create a Configuration with all processing options enabled
        Configuration configuration = new Configuration();
        configuration.shrink = true;
        configuration.optimize = true;
        configuration.obfuscate = true;
        configuration.preverify = true;

        // Act - Create ProGuard with the configuration
        ProGuard proGuard = new ProGuard(configuration);

        // Assert - Verify the ProGuard instance was created successfully
        assertNotNull(proGuard, "ProGuard should handle configuration with all processing enabled");
    }

    /**
     * Tests the constructor ProGuard(Configuration) with a Configuration having preverify enabled.
     * Verifies that ProGuard can be created with preverify configuration.
     */
    @Test
    public void testConstructorWithPreverifyEnabled() {
        // Arrange - Create a Configuration with preverify enabled
        Configuration configuration = new Configuration();
        configuration.preverify = true;

        // Act - Create ProGuard with the configuration
        ProGuard proGuard = new ProGuard(configuration);

        // Assert - Verify the ProGuard instance was created successfully
        assertNotNull(proGuard, "ProGuard should handle configuration with preverify enabled");
    }

    /**
     * Tests the constructor ProGuard(Configuration) with a Configuration having backport enabled.
     * Verifies that ProGuard can be created with backport configuration.
     */
    @Test
    public void testConstructorWithBackportEnabled() {
        // Arrange - Create a Configuration with backport enabled
        Configuration configuration = new Configuration();
        configuration.backport = true;

        // Act - Create ProGuard with the configuration
        ProGuard proGuard = new ProGuard(configuration);

        // Assert - Verify the ProGuard instance was created successfully
        assertNotNull(proGuard, "ProGuard should handle configuration with backport enabled");
    }

    /**
     * Tests the constructor ProGuard(Configuration) with a Configuration having keepKotlinMetadata enabled.
     * Verifies that ProGuard can be created with Kotlin metadata preservation.
     */
    @Test
    public void testConstructorWithKeepKotlinMetadata() {
        // Arrange - Create a Configuration with keepKotlinMetadata enabled
        Configuration configuration = new Configuration();
        configuration.keepKotlinMetadata = true;

        // Act - Create ProGuard with the configuration
        ProGuard proGuard = new ProGuard(configuration);

        // Assert - Verify the ProGuard instance was created successfully
        assertNotNull(proGuard, "ProGuard should handle configuration with keep Kotlin metadata");
    }

    /**
     * Tests the constructor ProGuard(Configuration) with a Configuration having verbose enabled.
     * Verifies that ProGuard can be created with verbose configuration.
     */
    @Test
    public void testConstructorWithVerboseEnabled() {
        // Arrange - Create a Configuration with verbose enabled
        Configuration configuration = new Configuration();
        configuration.verbose = true;

        // Act - Create ProGuard with the configuration
        ProGuard proGuard = new ProGuard(configuration);

        // Assert - Verify the ProGuard instance was created successfully
        assertNotNull(proGuard, "ProGuard should handle configuration with verbose enabled");
    }

    /**
     * Tests the constructor ProGuard(Configuration) with a Configuration having ignoreWarnings enabled.
     * Verifies that ProGuard can be created with warning ignore configuration.
     */
    @Test
    public void testConstructorWithIgnoreWarnings() {
        // Arrange - Create a Configuration with ignoreWarnings enabled
        Configuration configuration = new Configuration();
        configuration.ignoreWarnings = true;

        // Act - Create ProGuard with the configuration
        ProGuard proGuard = new ProGuard(configuration);

        // Assert - Verify the ProGuard instance was created successfully
        assertNotNull(proGuard, "ProGuard should handle configuration with ignore warnings");
    }

    /**
     * Tests the constructor ProGuard(Configuration) with a Configuration having addConfigurationDebugging enabled.
     * Verifies that ProGuard can be created with configuration debugging option.
     */
    @Test
    public void testConstructorWithAddConfigurationDebugging() {
        // Arrange - Create a Configuration with addConfigurationDebugging enabled
        Configuration configuration = new Configuration();
        configuration.addConfigurationDebugging = true;

        // Act - Create ProGuard with the configuration
        ProGuard proGuard = new ProGuard(configuration);

        // Assert - Verify the ProGuard instance was created successfully
        assertNotNull(proGuard, "ProGuard should handle configuration with add configuration debugging");
    }

    /**
     * Tests the constructor ProGuard(Configuration) with a Configuration having android enabled.
     * Verifies that ProGuard can be created with Android configuration.
     */
    @Test
    public void testConstructorWithAndroidEnabled() {
        // Arrange - Create a Configuration with android enabled
        Configuration configuration = new Configuration();
        configuration.android = true;

        // Act - Create ProGuard with the configuration
        ProGuard proGuard = new ProGuard(configuration);

        // Assert - Verify the ProGuard instance was created successfully
        assertNotNull(proGuard, "ProGuard should handle configuration with android enabled");
    }

    /**
     * Tests the constructor ProGuard(Configuration) with a Configuration having targetClassVersion set.
     * Verifies that ProGuard can be created with a specific target class version.
     */
    @Test
    public void testConstructorWithTargetClassVersion() {
        // Arrange - Create a Configuration with targetClassVersion set
        Configuration configuration = new Configuration();
        configuration.targetClassVersion = 52; // Java 8

        // Act - Create ProGuard with the configuration
        ProGuard proGuard = new ProGuard(configuration);

        // Assert - Verify the ProGuard instance was created successfully
        assertNotNull(proGuard, "ProGuard should handle configuration with target class version");
    }

    /**
     * Tests that multiple ProGuard instances can be created independently.
     * Verifies that each ProGuard instance is independent.
     */
    @Test
    public void testMultipleProGuardInstances() {
        // Arrange - Create two different configurations
        Configuration configuration1 = new Configuration();
        configuration1.shrink = true;

        Configuration configuration2 = new Configuration();
        configuration2.optimize = true;

        // Act - Create two ProGuard instances
        ProGuard proGuard1 = new ProGuard(configuration1);
        ProGuard proGuard2 = new ProGuard(configuration2);

        // Assert - Verify both ProGuard instances were created successfully
        assertNotNull(proGuard1, "First ProGuard instance should be created");
        assertNotNull(proGuard2, "Second ProGuard instance should be created");
        assertNotSame(proGuard1, proGuard2, "ProGuard instances should be different objects");
    }

    /**
     * Tests the constructor ProGuard(Configuration) with the same Configuration instance multiple times.
     * Verifies that the same configuration can be used to create multiple ProGuard instances.
     */
    @Test
    public void testMultipleProGuardInstancesWithSameConfiguration() {
        // Arrange - Create a single configuration
        Configuration configuration = new Configuration();
        configuration.verbose = true;

        // Act - Create multiple ProGuard instances with the same configuration
        ProGuard proGuard1 = new ProGuard(configuration);
        ProGuard proGuard2 = new ProGuard(configuration);

        // Assert - Verify both ProGuard instances were created successfully
        assertNotNull(proGuard1, "First ProGuard instance should be created");
        assertNotNull(proGuard2, "Second ProGuard instance should be created");
        assertNotSame(proGuard1, proGuard2, "ProGuard instances should be different objects");
    }

    /**
     * Tests the constructor ProGuard(Configuration) with a complex Configuration.
     * Verifies that ProGuard can handle a configuration with multiple options set.
     */
    @Test
    public void testConstructorWithComplexConfiguration() {
        // Arrange - Create a Configuration with multiple options
        Configuration configuration = new Configuration();
        configuration.shrink = true;
        configuration.optimize = true;
        configuration.obfuscate = false;
        configuration.preverify = false;
        configuration.verbose = true;
        configuration.ignoreWarnings = false;
        configuration.keepKotlinMetadata = true;
        configuration.backport = false;
        configuration.android = false;
        configuration.targetClassVersion = 52;

        // Act - Create ProGuard with complex configuration
        ProGuard proGuard = new ProGuard(configuration);

        // Assert - Verify the ProGuard instance was created successfully
        assertNotNull(proGuard, "ProGuard should handle complex configuration");
    }

    /**
     * Tests the constructor ProGuard(Configuration) with a Configuration having all processing disabled.
     * Verifies that ProGuard can be created with minimal configuration.
     */
    @Test
    public void testConstructorWithAllProcessingDisabled() {
        // Arrange - Create a Configuration with all processing disabled
        Configuration configuration = new Configuration();
        configuration.shrink = false;
        configuration.optimize = false;
        configuration.obfuscate = false;
        configuration.preverify = false;

        // Act - Create ProGuard with the configuration
        ProGuard proGuard = new ProGuard(configuration);

        // Assert - Verify the ProGuard instance was created successfully
        assertNotNull(proGuard, "ProGuard should handle configuration with all processing disabled");
    }

    /**
     * Tests the constructor ProGuard(Configuration) with a Configuration having optimizationPasses set.
     * Verifies that ProGuard can be created with specific optimization passes.
     */
    @Test
    public void testConstructorWithOptimizationPasses() {
        // Arrange - Create a Configuration with optimizationPasses set
        Configuration configuration = new Configuration();
        configuration.optimize = true;
        configuration.optimizationPasses = 5;

        // Act - Create ProGuard with the configuration
        ProGuard proGuard = new ProGuard(configuration);

        // Assert - Verify the ProGuard instance was created successfully
        assertNotNull(proGuard, "ProGuard should handle configuration with optimization passes");
    }

    /**
     * Tests the constructor ProGuard(Configuration) with a Configuration having dontProcessKotlinMetadata enabled.
     * Verifies that ProGuard can be created with Kotlin metadata processing disabled.
     */
    @Test
    public void testConstructorWithDontProcessKotlinMetadata() {
        // Arrange - Create a Configuration with dontProcessKotlinMetadata enabled
        Configuration configuration = new Configuration();
        configuration.dontProcessKotlinMetadata = true;

        // Act - Create ProGuard with the configuration
        ProGuard proGuard = new ProGuard(configuration);

        // Assert - Verify the ProGuard instance was created successfully
        assertNotNull(proGuard, "ProGuard should handle configuration with dont process Kotlin metadata");
    }
}
