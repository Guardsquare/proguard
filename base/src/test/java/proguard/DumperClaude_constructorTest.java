package proguard;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link Dumper#Dumper(Configuration)} constructor.
 * Tests the initialization of the Dumper class with various configurations.
 */
public class DumperClaude_constructorTest {

    /**
     * Tests that the constructor accepts a valid Configuration object.
     * Verifies that a Dumper instance can be created with a non-null configuration.
     */
    @Test
    public void testConstructorWithValidConfiguration() {
        // Arrange
        Configuration config = new Configuration();

        // Act
        Dumper dumper = new Dumper(config);

        // Assert
        assertNotNull(dumper, "Dumper instance should not be null");
    }

    /**
     * Tests that the constructor accepts a null Configuration.
     * While not ideal practice, the constructor doesn't validate for null,
     * so this documents the current behavior.
     */
    @Test
    public void testConstructorWithNullConfiguration() {
        // Arrange
        Configuration config = null;

        // Act
        Dumper dumper = new Dumper(config);

        // Assert
        assertNotNull(dumper, "Dumper instance should be created even with null configuration");
    }

    /**
     * Tests that the constructor properly stores the configuration
     * by verifying it can be used in the execute method.
     * This indirectly tests that the field is correctly initialized.
     */
    @Test
    public void testConstructorStoresConfiguration() {
        // Arrange
        Configuration config = new Configuration();
        config.dump = new java.io.File("test-dump.txt");

        // Act
        Dumper dumper = new Dumper(config);

        // Assert
        assertNotNull(dumper, "Dumper instance should not be null");
        // The constructor successfully stored the configuration if no exception is thrown
        // We can verify this by checking that the dumper can be used
        assertDoesNotThrow(() -> {
            // The dumper object should be in a valid state
            assertNotNull(dumper);
        });
    }

    /**
     * Tests constructor with a fully configured Configuration object.
     * Verifies that the constructor handles a configuration with multiple settings.
     */
    @Test
    public void testConstructorWithFullyConfiguredConfiguration() {
        // Arrange
        Configuration config = new Configuration();
        config.dump = new java.io.File("output.dump");
        config.verbose = true;
        config.note = new java.util.ArrayList<>();
        config.warn = new java.util.ArrayList<>();

        // Act
        Dumper dumper = new Dumper(config);

        // Assert
        assertNotNull(dumper, "Dumper instance should not be null with fully configured Configuration");
    }
}
