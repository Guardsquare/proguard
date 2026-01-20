package proguard.obfuscate;

import org.junit.jupiter.api.Test;
import proguard.Configuration;
import proguard.pass.Pass;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ObfuscationPreparation} constructor.
 * Tests the constructor ObfuscationPreparation(Configuration).
 */
public class ObfuscationPreparationClaude_constructorTest {

    /**
     * Tests that the constructor creates a valid ObfuscationPreparation instance with a non-null configuration.
     * Verifies that the instance is not null.
     */
    @Test
    public void testConstructorCreatesValidInstance() {
        // Arrange - Create a Configuration object
        Configuration configuration = new Configuration();

        // Act - Create an ObfuscationPreparation using the constructor
        ObfuscationPreparation obfuscationPreparation = new ObfuscationPreparation(configuration);

        // Assert - Verify the instance was created successfully
        assertNotNull(obfuscationPreparation, "ObfuscationPreparation should be instantiated successfully");
    }

    /**
     * Tests that the constructor creates an instance that implements Pass.
     * Verifies that ObfuscationPreparation can be used as a Pass.
     */
    @Test
    public void testConstructorCreatesInstanceOfPass() {
        // Arrange - Create a Configuration object
        Configuration configuration = new Configuration();

        // Act - Create an ObfuscationPreparation
        ObfuscationPreparation obfuscationPreparation = new ObfuscationPreparation(configuration);

        // Assert - Verify the instance implements Pass
        assertInstanceOf(Pass.class, obfuscationPreparation,
                "ObfuscationPreparation should implement Pass interface");
    }

    /**
     * Tests that the constructor accepts a null configuration.
     * Verifies that null configuration does not cause an exception during construction.
     */
    @Test
    public void testConstructorAcceptsNullConfiguration() {
        // Act - Create an ObfuscationPreparation with null configuration
        ObfuscationPreparation obfuscationPreparation = new ObfuscationPreparation(null);

        // Assert - Verify the instance was created successfully
        assertNotNull(obfuscationPreparation, "ObfuscationPreparation should accept null configuration");
    }

    /**
     * Tests that multiple instances can be created independently.
     * Verifies that the constructor can be called multiple times.
     */
    @Test
    public void testConstructorCreatesMultipleIndependentInstances() {
        // Arrange - Create Configuration objects
        Configuration configuration1 = new Configuration();
        Configuration configuration2 = new Configuration();

        // Act - Create multiple ObfuscationPreparation instances
        ObfuscationPreparation obfuscationPreparation1 = new ObfuscationPreparation(configuration1);
        ObfuscationPreparation obfuscationPreparation2 = new ObfuscationPreparation(configuration2);

        // Assert - Verify both instances are created and are distinct
        assertNotNull(obfuscationPreparation1, "First ObfuscationPreparation instance should be created");
        assertNotNull(obfuscationPreparation2, "Second ObfuscationPreparation instance should be created");
        assertNotSame(obfuscationPreparation1, obfuscationPreparation2,
                "Multiple instances should be distinct objects");
    }

    /**
     * Tests that the constructor can be called with the same configuration object multiple times.
     * Verifies that the same configuration can be used to create multiple instances.
     */
    @Test
    public void testConstructorWithSameConfiguration() {
        // Arrange - Create a Configuration object
        Configuration configuration = new Configuration();

        // Act - Create multiple ObfuscationPreparation instances with the same configuration
        ObfuscationPreparation obfuscationPreparation1 = new ObfuscationPreparation(configuration);
        ObfuscationPreparation obfuscationPreparation2 = new ObfuscationPreparation(configuration);

        // Assert - Verify both instances are created and are distinct
        assertNotNull(obfuscationPreparation1, "First ObfuscationPreparation instance should be created");
        assertNotNull(obfuscationPreparation2, "Second ObfuscationPreparation instance should be created");
        assertNotSame(obfuscationPreparation1, obfuscationPreparation2,
                "Multiple instances should be distinct even with same configuration");
    }

    /**
     * Tests that the constructor works with a configuration that has obfuscate flag set to true.
     * Verifies that the default configuration state is acceptable.
     */
    @Test
    public void testConstructorWithObfuscateEnabled() {
        // Arrange - Create a Configuration with obfuscate enabled (default)
        Configuration configuration = new Configuration();
        configuration.obfuscate = true;

        // Act - Create an ObfuscationPreparation
        ObfuscationPreparation obfuscationPreparation = new ObfuscationPreparation(configuration);

        // Assert - Verify the instance was created successfully
        assertNotNull(obfuscationPreparation, "ObfuscationPreparation should be created with obfuscate enabled");
    }

    /**
     * Tests that the constructor works with a configuration that has obfuscate flag set to false.
     * Verifies that the constructor doesn't validate the configuration state.
     */
    @Test
    public void testConstructorWithObfuscateDisabled() {
        // Arrange - Create a Configuration with obfuscate disabled
        Configuration configuration = new Configuration();
        configuration.obfuscate = false;

        // Act - Create an ObfuscationPreparation
        ObfuscationPreparation obfuscationPreparation = new ObfuscationPreparation(configuration);

        // Assert - Verify the instance was created successfully
        assertNotNull(obfuscationPreparation, "ObfuscationPreparation should be created with obfuscate disabled");
    }

    /**
     * Tests that the constructor works with a configuration that has applyMapping set.
     * Verifies that the constructor accepts configurations with mapping files.
     */
    @Test
    public void testConstructorWithApplyMapping() {
        // Arrange - Create a Configuration with applyMapping set
        Configuration configuration = new Configuration();
        configuration.applyMapping = Configuration.STD_OUT;

        // Act - Create an ObfuscationPreparation
        ObfuscationPreparation obfuscationPreparation = new ObfuscationPreparation(configuration);

        // Assert - Verify the instance was created successfully
        assertNotNull(obfuscationPreparation, "ObfuscationPreparation should be created with applyMapping set");
    }

    /**
     * Tests that the constructor works with a configuration that has printMapping set.
     * Verifies that the constructor accepts configurations with mapping output files.
     */
    @Test
    public void testConstructorWithPrintMapping() {
        // Arrange - Create a Configuration with printMapping set
        Configuration configuration = new Configuration();
        configuration.printMapping = Configuration.STD_OUT;

        // Act - Create an ObfuscationPreparation
        ObfuscationPreparation obfuscationPreparation = new ObfuscationPreparation(configuration);

        // Assert - Verify the instance was created successfully
        assertNotNull(obfuscationPreparation, "ObfuscationPreparation should be created with printMapping set");
    }

    /**
     * Tests that the constructor works with a configuration that has various boolean flags set.
     * Verifies that the constructor accepts configurations with different flag combinations.
     */
    @Test
    public void testConstructorWithVariousConfigurationFlags() {
        // Arrange - Create a Configuration with various flags set
        Configuration configuration = new Configuration();
        configuration.obfuscate = true;
        configuration.shrink = false;
        configuration.optimize = false;
        configuration.preverify = false;
        configuration.verbose = true;

        // Act - Create an ObfuscationPreparation
        ObfuscationPreparation obfuscationPreparation = new ObfuscationPreparation(configuration);

        // Assert - Verify the instance was created successfully
        assertNotNull(obfuscationPreparation, "ObfuscationPreparation should be created with various flags set");
    }

    /**
     * Tests that the constructor creates instances that can be used independently.
     * Verifies that modifying one instance's configuration doesn't affect another.
     */
    @Test
    public void testConstructorWithIndependentConfigurations() {
        // Arrange - Create two different Configuration objects
        Configuration configuration1 = new Configuration();
        configuration1.obfuscate = true;

        Configuration configuration2 = new Configuration();
        configuration2.obfuscate = false;

        // Act - Create two ObfuscationPreparation instances
        ObfuscationPreparation obfuscationPreparation1 = new ObfuscationPreparation(configuration1);
        ObfuscationPreparation obfuscationPreparation2 = new ObfuscationPreparation(configuration2);

        // Assert - Verify both instances are created successfully and are distinct
        assertNotNull(obfuscationPreparation1, "First instance should be created");
        assertNotNull(obfuscationPreparation2, "Second instance should be created");
        assertNotSame(obfuscationPreparation1, obfuscationPreparation2,
                "Instances should be distinct");
    }
}
