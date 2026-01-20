package proguard.optimize.gson;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.Configuration;
import proguard.pass.Pass;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the GsonOptimizer constructor.
 * Tests the constructor (Lproguard/Configuration;)V
 */
public class GsonOptimizerClaude_constructorTest {

    private Configuration configuration;

    @BeforeEach
    public void setUp() {
        configuration = new Configuration();
    }

    /**
     * Tests that the constructor successfully creates a non-null instance with a valid Configuration.
     */
    @Test
    public void testConstructor_createsNonNullInstance() {
        // Act
        GsonOptimizer optimizer = new GsonOptimizer(configuration);

        // Assert
        assertNotNull(optimizer, "Constructor should create a non-null instance");
    }

    /**
     * Tests that the constructor properly implements Pass interface.
     */
    @Test
    public void testConstructor_implementsPassInterface() {
        // Act
        GsonOptimizer optimizer = new GsonOptimizer(configuration);

        // Assert
        assertTrue(optimizer instanceof Pass,
            "GsonOptimizer should implement Pass interface");
    }

    /**
     * Tests that the constructor stores the provided configuration.
     * Reflection is necessary here because the configuration field is private
     * and there are no public getters or other observable behavior to test its value
     * without actually executing the optimizer on an AppView.
     */
    @Test
    public void testConstructor_storesConfiguration() throws Exception {
        // Act
        GsonOptimizer optimizer = new GsonOptimizer(configuration);

        // Assert
        Field field = GsonOptimizer.class.getDeclaredField("configuration");
        field.setAccessible(true);
        Configuration storedConfiguration = (Configuration) field.get(optimizer);
        assertSame(configuration, storedConfiguration,
            "Constructor should store the provided configuration");
    }

    /**
     * Tests that the constructor works with a fresh Configuration instance.
     */
    @Test
    public void testConstructor_withFreshConfiguration() {
        // Arrange
        Configuration freshConfig = new Configuration();

        // Act
        GsonOptimizer optimizer = new GsonOptimizer(freshConfig);

        // Assert
        assertNotNull(optimizer, "Constructor should work with fresh Configuration");
    }

    /**
     * Tests that the constructor works with a Configuration that has warn enabled.
     */
    @Test
    public void testConstructor_withWarnEnabled() {
        // Arrange
        Configuration configWithWarn = new Configuration();
        configWithWarn.warn = new java.util.ArrayList<>();
        configWithWarn.warn.add("*");

        // Act
        GsonOptimizer optimizer = new GsonOptimizer(configWithWarn);

        // Assert
        assertNotNull(optimizer, "Constructor should work with warn enabled");
    }

    /**
     * Tests that the constructor works with a Configuration that has optimizeConservatively set to true.
     */
    @Test
    public void testConstructor_withOptimizeConservativelyTrue() {
        // Arrange
        Configuration configWithOptimize = new Configuration();
        configWithOptimize.optimizeConservatively = true;

        // Act
        GsonOptimizer optimizer = new GsonOptimizer(configWithOptimize);

        // Assert
        assertNotNull(optimizer, "Constructor should work with optimizeConservatively=true");
    }

    /**
     * Tests that the constructor works with a Configuration that has optimizeConservatively set to false.
     */
    @Test
    public void testConstructor_withOptimizeConservativelyFalse() {
        // Arrange
        Configuration configWithoutOptimize = new Configuration();
        configWithoutOptimize.optimizeConservatively = false;

        // Act
        GsonOptimizer optimizer = new GsonOptimizer(configWithoutOptimize);

        // Assert
        assertNotNull(optimizer, "Constructor should work with optimizeConservatively=false");
    }

    /**
     * Tests that the constructor works with a Configuration that has various properties set.
     */
    @Test
    public void testConstructor_withConfiguredConfiguration() {
        // Arrange
        Configuration configuredConfig = new Configuration();
        configuredConfig.optimizeConservatively = true;
        configuredConfig.verbose = true;
        configuredConfig.note = new java.util.ArrayList<>();
        configuredConfig.warn = new java.util.ArrayList<>();

        // Act
        GsonOptimizer optimizer = new GsonOptimizer(configuredConfig);

        // Assert
        assertNotNull(optimizer, "Constructor should work with configured Configuration");
    }

    /**
     * Tests that multiple instances can be created independently with the same Configuration.
     */
    @Test
    public void testConstructor_multipleInstances_withSameConfiguration() {
        // Act
        GsonOptimizer optimizer1 = new GsonOptimizer(configuration);
        GsonOptimizer optimizer2 = new GsonOptimizer(configuration);

        // Assert
        assertNotNull(optimizer1, "First instance should be created");
        assertNotNull(optimizer2, "Second instance should be created");
        assertNotSame(optimizer1, optimizer2, "Instances should be distinct");
    }

    /**
     * Tests that multiple instances can be created independently with different Configurations.
     */
    @Test
    public void testConstructor_multipleInstances_withDifferentConfigurations() {
        // Arrange
        Configuration config1 = new Configuration();
        Configuration config2 = new Configuration();
        Configuration config3 = new Configuration();

        // Act
        GsonOptimizer optimizer1 = new GsonOptimizer(config1);
        GsonOptimizer optimizer2 = new GsonOptimizer(config2);
        GsonOptimizer optimizer3 = new GsonOptimizer(config3);

        // Assert
        assertNotNull(optimizer1, "First instance should be created");
        assertNotNull(optimizer2, "Second instance should be created");
        assertNotNull(optimizer3, "Third instance should be created");
        assertNotSame(optimizer1, optimizer2, "Instances should be distinct");
        assertNotSame(optimizer2, optimizer3, "Instances should be distinct");
        assertNotSame(optimizer1, optimizer3, "Instances should be distinct");
    }

    /**
     * Tests that the constructor does not throw any exceptions with valid input.
     */
    @Test
    public void testConstructor_doesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> new GsonOptimizer(configuration),
            "Constructor should not throw any exception with valid input");
    }

    /**
     * Tests that the constructor can be called multiple times consecutively.
     */
    @Test
    public void testConstructor_consecutiveCalls_allSucceed() {
        // Act & Assert
        for (int i = 0; i < 10; i++) {
            GsonOptimizer optimizer = new GsonOptimizer(new Configuration());
            assertNotNull(optimizer, "Instance " + i + " should be created");
        }
    }

    /**
     * Tests that the constructor works with different Configuration instances having different states.
     */
    @Test
    public void testConstructor_withDifferentConfigurationStates_createsIndependentInstances() {
        // Arrange
        Configuration config1 = new Configuration();
        config1.optimizeConservatively = true;

        Configuration config2 = new Configuration();
        config2.optimizeConservatively = false;

        Configuration config3 = new Configuration();
        config3.verbose = true;

        // Act
        GsonOptimizer optimizer1 = new GsonOptimizer(config1);
        GsonOptimizer optimizer2 = new GsonOptimizer(config2);
        GsonOptimizer optimizer3 = new GsonOptimizer(config3);

        // Assert
        assertNotNull(optimizer1, "First optimizer should be created");
        assertNotNull(optimizer2, "Second optimizer should be created");
        assertNotNull(optimizer3, "Third optimizer should be created");
        assertNotSame(optimizer1, optimizer2, "Different configurations should create distinct instances");
        assertNotSame(optimizer2, optimizer3, "Different configurations should create distinct instances");
        assertNotSame(optimizer1, optimizer3, "Different configurations should create distinct instances");
    }

    /**
     * Tests that the constructor correctly initializes the optimizer to be ready for execution.
     */
    @Test
    public void testConstructor_createsReadyToUsePass() {
        // Act
        GsonOptimizer optimizer = new GsonOptimizer(configuration);

        // Assert - Should be able to call Pass methods without error
        assertDoesNotThrow(() -> optimizer.getName(),
            "Should be able to call getName after construction");
    }

    /**
     * Tests that the getName method returns expected value after construction.
     */
    @Test
    public void testConstructor_getNameReturnsExpectedValue() {
        // Act
        GsonOptimizer optimizer = new GsonOptimizer(configuration);
        String name = optimizer.getName();

        // Assert
        assertNotNull(name, "getName should return a non-null value after construction");
        assertEquals("proguard.optimize.gson.GsonOptimizer", name,
            "getName should return the fully qualified class name");
    }

    /**
     * Tests that the constructor works with a Configuration that has null collections.
     */
    @Test
    public void testConstructor_withNullCollectionsInConfiguration() {
        // Arrange
        Configuration configWithNulls = new Configuration();
        configWithNulls.note = null;
        configWithNulls.warn = null;

        // Act
        GsonOptimizer optimizer = new GsonOptimizer(configWithNulls);

        // Assert
        assertNotNull(optimizer, "Constructor should work with null collections in Configuration");
    }

    /**
     * Tests that the constructor works with a Configuration that has empty collections.
     */
    @Test
    public void testConstructor_withEmptyCollectionsInConfiguration() {
        // Arrange
        Configuration configWithEmpty = new Configuration();
        configWithEmpty.note = new java.util.ArrayList<>();
        configWithEmpty.warn = new java.util.ArrayList<>();

        // Act
        GsonOptimizer optimizer = new GsonOptimizer(configWithEmpty);

        // Assert
        assertNotNull(optimizer, "Constructor should work with empty collections in Configuration");
    }

    /**
     * Tests that the constructor stores the correct Configuration reference even when
     * multiple instances are created with different configurations.
     */
    @Test
    public void testConstructor_storesCorrectConfigurationReference() throws Exception {
        // Arrange
        Configuration config1 = new Configuration();
        config1.verbose = true;

        Configuration config2 = new Configuration();
        config2.verbose = false;

        // Act
        GsonOptimizer optimizer1 = new GsonOptimizer(config1);
        GsonOptimizer optimizer2 = new GsonOptimizer(config2);

        // Assert
        Field field = GsonOptimizer.class.getDeclaredField("configuration");
        field.setAccessible(true);

        Configuration storedConfig1 = (Configuration) field.get(optimizer1);
        Configuration storedConfig2 = (Configuration) field.get(optimizer2);

        assertSame(config1, storedConfig1, "First optimizer should store config1");
        assertSame(config2, storedConfig2, "Second optimizer should store config2");
        assertNotSame(storedConfig1, storedConfig2, "Stored configurations should be different");
    }

    /**
     * Tests that the constructor creates an instance that is a Pass.
     */
    @Test
    public void testConstructor_createsPassInstance() {
        // Act
        Pass pass = new GsonOptimizer(configuration);

        // Assert
        assertNotNull(pass, "Should be able to assign GsonOptimizer to Pass");
        assertTrue(pass instanceof GsonOptimizer, "Pass instance should be a GsonOptimizer");
    }

    /**
     * Tests that the constructor works with a Configuration that has all boolean flags set.
     */
    @Test
    public void testConstructor_withAllConfigurationBooleanFlagsSet() {
        // Arrange
        Configuration fullConfig = new Configuration();
        fullConfig.verbose = true;
        fullConfig.optimizeConservatively = true;
        fullConfig.shrink = true;
        fullConfig.optimize = true;
        fullConfig.obfuscate = true;
        fullConfig.preverify = true;
        fullConfig.allowAccessModification = true;
        fullConfig.mergeInterfacesAggressively = true;

        // Act
        GsonOptimizer optimizer = new GsonOptimizer(fullConfig);

        // Assert
        assertNotNull(optimizer, "Constructor should work with all Configuration boolean flags set");
    }

    /**
     * Tests that the constructor works with a minimal Configuration instance.
     */
    @Test
    public void testConstructor_withMinimalConfiguration() {
        // Arrange - A Configuration with no additional setup
        Configuration minimalConfig = new Configuration();

        // Act
        GsonOptimizer optimizer = new GsonOptimizer(minimalConfig);

        // Assert
        assertNotNull(optimizer, "Constructor should work with minimal Configuration");
        assertDoesNotThrow(() -> optimizer.getName(),
            "Optimizer should be functional with minimal Configuration");
    }

    /**
     * Tests that consecutive calls to constructor with same Configuration create independent instances.
     */
    @Test
    public void testConstructor_consecutiveCallsWithSameConfig_createIndependentInstances() {
        // Act
        GsonOptimizer optimizer1 = new GsonOptimizer(configuration);
        GsonOptimizer optimizer2 = new GsonOptimizer(configuration);
        GsonOptimizer optimizer3 = new GsonOptimizer(configuration);

        // Assert
        assertNotSame(optimizer1, optimizer2, "First and second instances should be distinct");
        assertNotSame(optimizer2, optimizer3, "Second and third instances should be distinct");
        assertNotSame(optimizer1, optimizer3, "First and third instances should be distinct");
    }
}
