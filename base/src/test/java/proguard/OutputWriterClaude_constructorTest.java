package proguard;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link OutputWriter#OutputWriter(Configuration)} constructor.
 * Tests the initialization of the OutputWriter class with various configurations.
 */
public class OutputWriterClaude_constructorTest {

    /**
     * Tests that the constructor accepts a valid Configuration object.
     * Verifies that an OutputWriter instance can be created with a non-null configuration.
     */
    @Test
    public void testConstructorWithValidConfiguration() {
        // Arrange
        Configuration config = new Configuration();

        // Act
        OutputWriter outputWriter = new OutputWriter(config);

        // Assert
        assertNotNull(outputWriter, "OutputWriter instance should not be null");
    }

    /**
     * Tests that the constructor accepts a null Configuration.
     * The constructor doesn't validate for null, so this documents the current behavior.
     */
    @Test
    public void testConstructorWithNullConfiguration() {
        // Arrange
        Configuration config = null;

        // Act
        OutputWriter outputWriter = new OutputWriter(config);

        // Assert
        assertNotNull(outputWriter, "OutputWriter instance should be created even with null configuration");
    }

    /**
     * Tests constructor with a Configuration that has programJars set.
     * Verifies that the constructor handles configurations with jar inputs/outputs.
     */
    @Test
    public void testConstructorWithProgramJars() {
        // Arrange
        Configuration config = new Configuration();
        config.programJars = new ClassPath();

        // Act
        OutputWriter outputWriter = new OutputWriter(config);

        // Assert
        assertNotNull(outputWriter, "OutputWriter instance should not be null with programJars set");
    }

    /**
     * Tests constructor with a Configuration that has dontCompress list.
     * Verifies that the constructor handles compression configurations.
     */
    @Test
    public void testConstructorWithDontCompress() {
        // Arrange
        Configuration config = new Configuration();
        config.dontCompress = new java.util.ArrayList<>();
        config.dontCompress.add("*.png");
        config.dontCompress.add("*.jpg");

        // Act
        OutputWriter outputWriter = new OutputWriter(config);

        // Assert
        assertNotNull(outputWriter, "OutputWriter instance should not be null with dontCompress set");
    }

    /**
     * Tests constructor with a Configuration that has zipAlign set.
     * Verifies that the constructor handles alignment configurations.
     */
    @Test
    public void testConstructorWithZipAlign() {
        // Arrange
        Configuration config = new Configuration();
        config.zipAlign = 4;

        // Act
        OutputWriter outputWriter = new OutputWriter(config);

        // Assert
        assertNotNull(outputWriter, "OutputWriter instance should not be null with zipAlign set");
    }

    /**
     * Tests constructor with a Configuration that has android flag enabled.
     * Verifies that the constructor handles Android-specific configurations.
     */
    @Test
    public void testConstructorWithAndroidEnabled() {
        // Arrange
        Configuration config = new Configuration();
        config.android = true;

        // Act
        OutputWriter outputWriter = new OutputWriter(config);

        // Assert
        assertNotNull(outputWriter, "OutputWriter instance should not be null with android enabled");
    }

    /**
     * Tests constructor with a Configuration that has obfuscate enabled.
     * Verifies that the constructor handles obfuscation configurations.
     */
    @Test
    public void testConstructorWithObfuscateEnabled() {
        // Arrange
        Configuration config = new Configuration();
        config.obfuscate = true;

        // Act
        OutputWriter outputWriter = new OutputWriter(config);

        // Assert
        assertNotNull(outputWriter, "OutputWriter instance should not be null with obfuscate enabled");
    }

    /**
     * Tests constructor with a Configuration that has obfuscate disabled.
     * Verifies that the constructor handles non-obfuscation mode.
     */
    @Test
    public void testConstructorWithObfuscateDisabled() {
        // Arrange
        Configuration config = new Configuration();
        config.obfuscate = false;

        // Act
        OutputWriter outputWriter = new OutputWriter(config);

        // Assert
        assertNotNull(outputWriter, "OutputWriter instance should not be null with obfuscate disabled");
    }

    /**
     * Tests constructor with a Configuration that has extraJar set.
     * Verifies that the constructor handles configurations with extra jar output.
     */
    @Test
    public void testConstructorWithExtraJar() {
        // Arrange
        Configuration config = new Configuration();
        config.extraJar = new java.io.File("extra.jar");

        // Act
        OutputWriter outputWriter = new OutputWriter(config);

        // Assert
        assertNotNull(outputWriter, "OutputWriter instance should not be null with extraJar set");
    }

    /**
     * Tests constructor with a Configuration that has keyStore settings.
     * Verifies that the constructor handles signing configurations.
     */
    @Test
    public void testConstructorWithKeyStoreSettings() {
        // Arrange
        Configuration config = new Configuration();
        config.keyStores = new java.util.ArrayList<>();
        config.keyStorePasswords = new java.util.ArrayList<>();
        config.keyAliases = new java.util.ArrayList<>();
        config.keyPasswords = new java.util.ArrayList<>();

        // Act
        OutputWriter outputWriter = new OutputWriter(config);

        // Assert
        assertNotNull(outputWriter, "OutputWriter instance should not be null with keyStore settings");
    }

    /**
     * Tests constructor with a Configuration that has adaptResourceFileNames set.
     * Verifies that the constructor handles resource adaptation configurations.
     */
    @Test
    public void testConstructorWithAdaptResourceFileNames() {
        // Arrange
        Configuration config = new Configuration();
        config.adaptResourceFileNames = new java.util.ArrayList<>();
        config.adaptResourceFileNames.add("**.properties");

        // Act
        OutputWriter outputWriter = new OutputWriter(config);

        // Assert
        assertNotNull(outputWriter, "OutputWriter instance should not be null with adaptResourceFileNames set");
    }

    /**
     * Tests constructor with a Configuration that has adaptResourceFileContents set.
     * Verifies that the constructor handles resource content adaptation configurations.
     */
    @Test
    public void testConstructorWithAdaptResourceFileContents() {
        // Arrange
        Configuration config = new Configuration();
        config.adaptResourceFileContents = new java.util.ArrayList<>();
        config.adaptResourceFileContents.add("**.xml");

        // Act
        OutputWriter outputWriter = new OutputWriter(config);

        // Assert
        assertNotNull(outputWriter, "OutputWriter instance should not be null with adaptResourceFileContents set");
    }

    /**
     * Tests constructor with a Configuration that has keepKotlinMetadata enabled.
     * Verifies that the constructor handles Kotlin metadata preservation.
     */
    @Test
    public void testConstructorWithKeepKotlinMetadata() {
        // Arrange
        Configuration config = new Configuration();
        config.keepKotlinMetadata = true;

        // Act
        OutputWriter outputWriter = new OutputWriter(config);

        // Assert
        assertNotNull(outputWriter, "OutputWriter instance should not be null with keepKotlinMetadata enabled");
    }

    /**
     * Tests constructor with a Configuration that has shrink enabled.
     * Verifies that the constructor handles shrinking configurations.
     */
    @Test
    public void testConstructorWithShrinkEnabled() {
        // Arrange
        Configuration config = new Configuration();
        config.shrink = true;

        // Act
        OutputWriter outputWriter = new OutputWriter(config);

        // Assert
        assertNotNull(outputWriter, "OutputWriter instance should not be null with shrink enabled");
    }

    /**
     * Tests constructor with a Configuration that has optimize enabled.
     * Verifies that the constructor handles optimization configurations.
     */
    @Test
    public void testConstructorWithOptimizeEnabled() {
        // Arrange
        Configuration config = new Configuration();
        config.optimize = true;

        // Act
        OutputWriter outputWriter = new OutputWriter(config);

        // Assert
        assertNotNull(outputWriter, "OutputWriter instance should not be null with optimize enabled");
    }

    /**
     * Tests constructor with a Configuration that has addConfigurationDebugging enabled.
     * Verifies that the constructor handles debugging configuration.
     */
    @Test
    public void testConstructorWithAddConfigurationDebugging() {
        // Arrange
        Configuration config = new Configuration();
        config.addConfigurationDebugging = true;

        // Act
        OutputWriter outputWriter = new OutputWriter(config);

        // Assert
        assertNotNull(outputWriter, "OutputWriter instance should not be null with addConfigurationDebugging enabled");
    }

    /**
     * Tests constructor with a Configuration that has keepDirectories set.
     * Verifies that the constructor handles directory preservation configurations.
     */
    @Test
    public void testConstructorWithKeepDirectories() {
        // Arrange
        Configuration config = new Configuration();
        config.keepDirectories = new java.util.ArrayList<>();

        // Act
        OutputWriter outputWriter = new OutputWriter(config);

        // Assert
        assertNotNull(outputWriter, "OutputWriter instance should not be null with keepDirectories set");
    }

    /**
     * Tests constructor with a fully configured Configuration object.
     * Verifies that the constructor handles a configuration with multiple settings.
     */
    @Test
    public void testConstructorWithFullyConfiguredConfiguration() {
        // Arrange
        Configuration config = new Configuration();
        config.programJars = new ClassPath();
        config.obfuscate = true;
        config.shrink = true;
        config.optimize = true;
        config.android = true;
        config.zipAlign = 4;
        config.keepKotlinMetadata = true;
        config.dontCompress = new java.util.ArrayList<>();
        config.adaptResourceFileNames = new java.util.ArrayList<>();
        config.adaptResourceFileContents = new java.util.ArrayList<>();

        // Act
        OutputWriter outputWriter = new OutputWriter(config);

        // Assert
        assertNotNull(outputWriter, "OutputWriter instance should not be null with fully configured Configuration");
    }

    /**
     * Tests that multiple OutputWriter instances can be created independently.
     * Verifies that each OutputWriter instance is independent.
     */
    @Test
    public void testMultipleOutputWriterInstances() {
        // Arrange
        Configuration config1 = new Configuration();
        config1.obfuscate = true;

        Configuration config2 = new Configuration();
        config2.obfuscate = false;

        // Act
        OutputWriter outputWriter1 = new OutputWriter(config1);
        OutputWriter outputWriter2 = new OutputWriter(config2);

        // Assert
        assertNotNull(outputWriter1, "First OutputWriter instance should not be null");
        assertNotNull(outputWriter2, "Second OutputWriter instance should not be null");
        assertNotSame(outputWriter1, outputWriter2, "OutputWriter instances should be different objects");
    }

    /**
     * Tests the constructor with the same Configuration instance multiple times.
     * Verifies that the same configuration can be used to create multiple OutputWriters.
     */
    @Test
    public void testMultipleOutputWritersWithSameConfiguration() {
        // Arrange
        Configuration config = new Configuration();
        config.verbose = true;

        // Act
        OutputWriter outputWriter1 = new OutputWriter(config);
        OutputWriter outputWriter2 = new OutputWriter(config);

        // Assert
        assertNotNull(outputWriter1, "First OutputWriter instance should not be null");
        assertNotNull(outputWriter2, "Second OutputWriter instance should not be null");
        assertNotSame(outputWriter1, outputWriter2, "OutputWriter instances should be different objects");
    }

    /**
     * Tests constructor with a Configuration that has verbose enabled.
     * Verifies that the constructor handles verbose configurations.
     */
    @Test
    public void testConstructorWithVerboseEnabled() {
        // Arrange
        Configuration config = new Configuration();
        config.verbose = true;

        // Act
        OutputWriter outputWriter = new OutputWriter(config);

        // Assert
        assertNotNull(outputWriter, "OutputWriter instance should not be null with verbose enabled");
    }
}
