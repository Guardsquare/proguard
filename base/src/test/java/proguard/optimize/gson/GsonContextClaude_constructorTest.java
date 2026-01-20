package proguard.optimize.gson;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the GsonContext default constructor.
 * Tests the implicit no-argument constructor <init>.()V
 */
public class GsonContextClaude_constructorTest {

    /**
     * Tests that the default constructor creates a non-null GsonContext instance.
     */
    @Test
    public void testConstructorCreatesNonNullInstance() {
        // Act
        GsonContext context = new GsonContext();

        // Assert
        assertNotNull(context, "GsonContext instance should not be null");
    }

    /**
     * Tests that the default constructor initializes gsonDomainClassPool to null.
     * The field is initialized to null and will be set later by setupFor().
     */
    @Test
    public void testConstructorInitializesGsonDomainClassPoolToNull() {
        // Act
        GsonContext context = new GsonContext();

        // Assert
        assertNull(context.gsonDomainClassPool, "gsonDomainClassPool should be null after construction");
    }

    /**
     * Tests that the default constructor initializes gsonRuntimeSettings to null.
     * The field is initialized to null and will be set later by setupFor().
     */
    @Test
    public void testConstructorInitializesGsonRuntimeSettingsToNull() {
        // Act
        GsonContext context = new GsonContext();

        // Assert
        assertNull(context.gsonRuntimeSettings, "gsonRuntimeSettings should be null after construction");
    }

    /**
     * Tests that multiple GsonContext instances can be created independently.
     */
    @Test
    public void testMultipleInstancesCreation() {
        // Act
        GsonContext context1 = new GsonContext();
        GsonContext context2 = new GsonContext();

        // Assert
        assertNotNull(context1, "First GsonContext instance should not be null");
        assertNotNull(context2, "Second GsonContext instance should not be null");
        assertNotSame(context1, context2, "Each constructor call should create a distinct instance");
    }

    /**
     * Tests that a newly constructed GsonContext has its fields accessible.
     * This verifies the object is properly initialized and ready for subsequent operations.
     */
    @Test
    public void testConstructorCreatesAccessibleFields() {
        // Act
        GsonContext context = new GsonContext();

        // Assert - Verify we can access both public fields without any exceptions
        assertDoesNotThrow(() -> {
            Object domainPool = context.gsonDomainClassPool;
            Object runtimeSettings = context.gsonRuntimeSettings;
        }, "Should be able to access all public fields after construction");
    }
}
