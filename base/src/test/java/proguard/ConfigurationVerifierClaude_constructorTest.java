package proguard;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ConfigurationVerifier} constructor.
 * Tests ConfigurationVerifier(Configuration) constructor.
 */
public class ConfigurationVerifierClaude_constructorTest {

    /**
     * Tests the constructor ConfigurationVerifier(Configuration) with a valid Configuration object.
     * Verifies that the verifier can be instantiated with a proper configuration.
     */
    @Test
    public void testConstructorWithValidConfiguration() {
        // Arrange - Create a valid configuration
        Configuration configuration = new Configuration();

        // Act - Create verifier with the configuration
        ConfigurationVerifier verifier = new ConfigurationVerifier(configuration);

        // Assert - Verify the verifier was created successfully
        assertNotNull(verifier, "ConfigurationVerifier should be instantiated successfully");
    }

    /**
     * Tests the constructor ConfigurationVerifier(Configuration) with a null Configuration.
     * The constructor accepts null and the verifier object should still be created,
     * though calling check() would likely fail.
     */
    @Test
    public void testConstructorWithNullConfiguration() {
        // Act - Create verifier with null configuration
        ConfigurationVerifier verifier = new ConfigurationVerifier(null);

        // Assert - Verify the verifier was created (constructor doesn't validate null)
        assertNotNull(verifier, "ConfigurationVerifier should be instantiated even with null configuration");
    }

    /**
     * Tests the constructor ConfigurationVerifier(Configuration) with a configuration
     * containing programJars.
     * Verifies that the verifier can handle a configuration with program jars.
     */
    @Test
    public void testConstructorWithConfigurationContainingProgramJars() {
        // Arrange - Create a configuration with program jars
        Configuration configuration = new Configuration();
        configuration.programJars = new ClassPath();
        configuration.programJars.add(new ClassPathEntry(new File("input.jar"), false));

        // Act - Create verifier with the configuration
        ConfigurationVerifier verifier = new ConfigurationVerifier(configuration);

        // Assert - Verify the verifier was created successfully
        assertNotNull(verifier, "ConfigurationVerifier should handle configuration with program jars");
    }

    /**
     * Tests the constructor ConfigurationVerifier(Configuration) with a configuration
     * containing libraryJars.
     * Verifies that the verifier can handle a configuration with library jars.
     */
    @Test
    public void testConstructorWithConfigurationContainingLibraryJars() {
        // Arrange - Create a configuration with library jars
        Configuration configuration = new Configuration();
        configuration.libraryJars = new ClassPath();
        configuration.libraryJars.add(new ClassPathEntry(new File("library.jar"), false));

        // Act - Create verifier with the configuration
        ConfigurationVerifier verifier = new ConfigurationVerifier(configuration);

        // Assert - Verify the verifier was created successfully
        assertNotNull(verifier, "ConfigurationVerifier should handle configuration with library jars");
    }

    /**
     * Tests the constructor ConfigurationVerifier(Configuration) with a configuration
     * containing both programJars and libraryJars.
     * Verifies that the verifier can handle a complex configuration.
     */
    @Test
    public void testConstructorWithConfigurationContainingBothJarTypes() {
        // Arrange - Create a configuration with both program and library jars
        Configuration configuration = new Configuration();
        configuration.programJars = new ClassPath();
        configuration.programJars.add(new ClassPathEntry(new File("input.jar"), false));
        configuration.programJars.add(new ClassPathEntry(new File("output.jar"), true));
        configuration.libraryJars = new ClassPath();
        configuration.libraryJars.add(new ClassPathEntry(new File("library.jar"), false));

        // Act - Create verifier with the configuration
        ConfigurationVerifier verifier = new ConfigurationVerifier(configuration);

        // Assert - Verify the verifier was created successfully
        assertNotNull(verifier, "ConfigurationVerifier should handle configuration with both jar types");
    }

    /**
     * Tests the constructor ConfigurationVerifier(Configuration) with an empty configuration.
     * Verifies that the verifier can be instantiated with a default/empty configuration.
     */
    @Test
    public void testConstructorWithEmptyConfiguration() {
        // Arrange - Create an empty configuration
        Configuration configuration = new Configuration();

        // Act - Create verifier with the empty configuration
        ConfigurationVerifier verifier = new ConfigurationVerifier(configuration);

        // Assert - Verify the verifier was created successfully
        assertNotNull(verifier, "ConfigurationVerifier should handle empty configuration");
    }

    /**
     * Tests the constructor ConfigurationVerifier(Configuration) with a configuration
     * containing various settings.
     * Verifies that the verifier can handle a configuration with multiple settings.
     */
    @Test
    public void testConstructorWithConfigurationContainingVariousSettings() {
        // Arrange - Create a configuration with various settings
        Configuration configuration = new Configuration();
        configuration.verbose = true;
        configuration.obfuscate = true;
        configuration.shrink = true;
        configuration.optimize = true;

        // Act - Create verifier with the configuration
        ConfigurationVerifier verifier = new ConfigurationVerifier(configuration);

        // Assert - Verify the verifier was created successfully
        assertNotNull(verifier, "ConfigurationVerifier should handle configuration with various settings");
    }

    /**
     * Tests that multiple ConfigurationVerifier instances can be created independently.
     * Verifies that each verifier instance is independent.
     */
    @Test
    public void testMultipleVerifierInstances() {
        // Arrange - Create two different configurations
        Configuration configuration1 = new Configuration();
        configuration1.verbose = true;

        Configuration configuration2 = new Configuration();
        configuration2.obfuscate = true;

        // Act - Create two verifier instances
        ConfigurationVerifier verifier1 = new ConfigurationVerifier(configuration1);
        ConfigurationVerifier verifier2 = new ConfigurationVerifier(configuration2);

        // Assert - Verify both verifiers were created successfully
        assertNotNull(verifier1, "First verifier should be created");
        assertNotNull(verifier2, "Second verifier should be created");
        assertNotSame(verifier1, verifier2, "Verifier instances should be different objects");
    }

    /**
     * Tests the constructor ConfigurationVerifier(Configuration) with the same configuration
     * object used to create multiple verifiers.
     * Verifies that the same configuration can be used for multiple verifiers.
     */
    @Test
    public void testMultipleVerifiersWithSameConfiguration() {
        // Arrange - Create a single configuration
        Configuration configuration = new Configuration();
        configuration.programJars = new ClassPath();
        configuration.programJars.add(new ClassPathEntry(new File("input.jar"), false));

        // Act - Create two verifier instances with the same configuration
        ConfigurationVerifier verifier1 = new ConfigurationVerifier(configuration);
        ConfigurationVerifier verifier2 = new ConfigurationVerifier(configuration);

        // Assert - Verify both verifiers were created successfully
        assertNotNull(verifier1, "First verifier should be created");
        assertNotNull(verifier2, "Second verifier should be created");
        assertNotSame(verifier1, verifier2, "Verifier instances should be different objects");
    }

    /**
     * Tests the constructor ConfigurationVerifier(Configuration) with a configuration
     * containing output jars only.
     * Verifies that the verifier can be instantiated even with a configuration
     * that would fail validation later.
     */
    @Test
    public void testConstructorWithConfigurationContainingOnlyOutputJars() {
        // Arrange - Create a configuration with only output jars
        Configuration configuration = new Configuration();
        configuration.programJars = new ClassPath();
        configuration.programJars.add(new ClassPathEntry(new File("output.jar"), true));

        // Act - Create verifier with the configuration
        ConfigurationVerifier verifier = new ConfigurationVerifier(configuration);

        // Assert - Verify the verifier was created successfully
        // Note: The constructor doesn't validate, so this should succeed
        assertNotNull(verifier, "ConfigurationVerifier constructor should succeed even with invalid configuration");
    }

    /**
     * Tests the constructor ConfigurationVerifier(Configuration) with a configuration
     * containing multiple program jars.
     * Verifies that the verifier can handle a configuration with multiple entries.
     */
    @Test
    public void testConstructorWithConfigurationContainingMultipleProgramJars() {
        // Arrange - Create a configuration with multiple program jars
        Configuration configuration = new Configuration();
        configuration.programJars = new ClassPath();
        configuration.programJars.add(new ClassPathEntry(new File("input1.jar"), false));
        configuration.programJars.add(new ClassPathEntry(new File("output1.jar"), true));
        configuration.programJars.add(new ClassPathEntry(new File("input2.jar"), false));
        configuration.programJars.add(new ClassPathEntry(new File("output2.jar"), true));

        // Act - Create verifier with the configuration
        ConfigurationVerifier verifier = new ConfigurationVerifier(configuration);

        // Assert - Verify the verifier was created successfully
        assertNotNull(verifier, "ConfigurationVerifier should handle configuration with multiple program jars");
    }

    /**
     * Tests the constructor ConfigurationVerifier(Configuration) with a configuration
     * containing multiple library jars.
     * Verifies that the verifier can handle a configuration with multiple library entries.
     */
    @Test
    public void testConstructorWithConfigurationContainingMultipleLibraryJars() {
        // Arrange - Create a configuration with multiple library jars
        Configuration configuration = new Configuration();
        configuration.libraryJars = new ClassPath();
        configuration.libraryJars.add(new ClassPathEntry(new File("library1.jar"), false));
        configuration.libraryJars.add(new ClassPathEntry(new File("library2.jar"), false));
        configuration.libraryJars.add(new ClassPathEntry(new File("library3.jar"), false));

        // Act - Create verifier with the configuration
        ConfigurationVerifier verifier = new ConfigurationVerifier(configuration);

        // Assert - Verify the verifier was created successfully
        assertNotNull(verifier, "ConfigurationVerifier should handle configuration with multiple library jars");
    }

    /**
     * Tests the constructor ConfigurationVerifier(Configuration) with a configuration
     * containing null programJars.
     * Verifies that the verifier can be instantiated with null programJars.
     */
    @Test
    public void testConstructorWithConfigurationContainingNullProgramJars() {
        // Arrange - Create a configuration with null programJars
        Configuration configuration = new Configuration();
        configuration.programJars = null;

        // Act - Create verifier with the configuration
        ConfigurationVerifier verifier = new ConfigurationVerifier(configuration);

        // Assert - Verify the verifier was created successfully
        assertNotNull(verifier, "ConfigurationVerifier should handle configuration with null programJars");
    }

    /**
     * Tests the constructor ConfigurationVerifier(Configuration) with a configuration
     * containing empty ClassPaths.
     * Verifies that the verifier can handle a configuration with empty but non-null class paths.
     */
    @Test
    public void testConstructorWithConfigurationContainingEmptyClassPaths() {
        // Arrange - Create a configuration with empty class paths
        Configuration configuration = new Configuration();
        configuration.programJars = new ClassPath();
        configuration.libraryJars = new ClassPath();

        // Act - Create verifier with the configuration
        ConfigurationVerifier verifier = new ConfigurationVerifier(configuration);

        // Assert - Verify the verifier was created successfully
        assertNotNull(verifier, "ConfigurationVerifier should handle configuration with empty class paths");
    }

    /**
     * Tests the constructor ConfigurationVerifier(Configuration) with a configuration
     * containing all major properties set.
     * Verifies that the verifier can handle a fully populated configuration.
     */
    @Test
    public void testConstructorWithFullyPopulatedConfiguration() {
        // Arrange - Create a fully populated configuration
        Configuration configuration = new Configuration();
        configuration.programJars = new ClassPath();
        configuration.programJars.add(new ClassPathEntry(new File("input.jar"), false));
        configuration.programJars.add(new ClassPathEntry(new File("output.jar"), true));
        configuration.libraryJars = new ClassPath();
        configuration.libraryJars.add(new ClassPathEntry(new File("library.jar"), false));
        configuration.verbose = true;
        configuration.obfuscate = true;
        configuration.shrink = true;
        configuration.optimize = true;
        configuration.useMixedCaseClassNames = true;

        // Act - Create verifier with the configuration
        ConfigurationVerifier verifier = new ConfigurationVerifier(configuration);

        // Assert - Verify the verifier was created successfully
        assertNotNull(verifier, "ConfigurationVerifier should handle fully populated configuration");
    }
}
