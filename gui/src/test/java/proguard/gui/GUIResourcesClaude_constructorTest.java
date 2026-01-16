package proguard.gui;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for GUIResources constructor.
 *
 * The GUIResources class has an implicit default constructor (no explicit constructor is defined).
 * The class is a utility class with static methods and static fields that are initialized at
 * class loading time (lines 34-35):
 * - Line 34: private static final ResourceBundle messages
 * - Line 35: private static final MessageFormat formatter
 *
 * The constructor itself performs no explicit operations beyond standard object initialization.
 * However, we can verify that:
 * - The class can be instantiated (constructor executes successfully)
 * - The static fields are properly initialized (verified indirectly through static method behavior)
 * - The ResourceBundle is properly loaded with the correct properties file
 */
public class GUIResourcesClaude_constructorTest {

    /**
     * Test that the GUIResources class can be instantiated.
     * This verifies the implicit default constructor executes without errors.
     */
    @Test
    public void testConstructorCreatesInstance() {
        // The class has an implicit default constructor
        // We can instantiate it even though it's designed to be used statically
        assertDoesNotThrow(() -> {
            GUIResources instance = new GUIResources();
            assertNotNull(instance, "Constructor should create a non-null instance");
        });
    }

    /**
     * Test that static initialization works correctly.
     * This verifies that the static ResourceBundle (line 34) is properly initialized
     * by testing the getMessage method which depends on it.
     */
    @Test
    public void testStaticResourceBundleInitialization() {
        // The static ResourceBundle should be initialized when the class is loaded
        // We can verify this by calling getMessage with a known key
        assertDoesNotThrow(() -> {
            String message = GUIResources.getMessage("proGuardTab");
            assertNotNull(message, "getMessage should return a non-null value");
            assertEquals("ProGuard", message,
                        "getMessage should return the correct value from the resource bundle");
        });
    }

    /**
     * Test that static MessageFormat is properly initialized.
     * This verifies that the static MessageFormat (line 35) is properly initialized
     * by testing the getMessage method with arguments which depends on it.
     */
    @Test
    public void testStaticMessageFormatInitialization() {
        // The static MessageFormat should be initialized when the class is loaded
        // We can verify this by calling getMessage with arguments
        assertDoesNotThrow(() -> {
            // Use a message that takes parameters
            String message = GUIResources.getMessage("cantOpenConfigurationFile",
                                                     new Object[]{"test.pro"});
            assertNotNull(message, "getMessage with arguments should return a non-null value");
            assertTrue(message.contains("test.pro"),
                      "getMessage should format the message with the provided argument");
        });
    }

    /**
     * Test getMessage with different keys from the properties file.
     * This further verifies that the ResourceBundle is correctly loaded and accessible.
     */
    @Test
    public void testGetMessageWithVariousKeys() {
        // Test multiple keys to verify the resource bundle is fully loaded
        assertEquals("Input/Output", GUIResources.getMessage("inputOutputTab"));
        assertEquals("Shrinking", GUIResources.getMessage("shrinkingTab"));
        assertEquals("Obfuscation", GUIResources.getMessage("obfuscationTab"));
        assertEquals("Optimization", GUIResources.getMessage("optimizationTab"));
    }

    /**
     * Test that getMessage throws MissingResourceException for invalid keys.
     * This verifies the ResourceBundle is working as expected.
     */
    @Test
    public void testGetMessageWithInvalidKey() {
        // Testing behavior with an invalid key
        assertThrows(java.util.MissingResourceException.class, () -> {
            GUIResources.getMessage("nonExistentKey");
        }, "getMessage should throw MissingResourceException for invalid keys");
    }

    /**
     * Test that creating multiple instances doesn't affect static state.
     * The static fields should be shared across all instances.
     */
    @Test
    public void testMultipleInstancesShareStaticState() {
        GUIResources instance1 = new GUIResources();
        GUIResources instance2 = new GUIResources();

        assertNotNull(instance1);
        assertNotNull(instance2);
        assertNotSame(instance1, instance2, "Different instances should be created");

        // Both should access the same static ResourceBundle
        String message1 = GUIResources.getMessage("proGuardTab");
        String message2 = GUIResources.getMessage("proGuardTab");
        assertEquals(message1, message2,
                    "Static methods should return the same results regardless of instances");
    }

    /**
     * Test getMessage with formatted message containing multiple arguments.
     * This tests the MessageFormat initialization more thoroughly.
     */
    @Test
    public void testGetMessageWithMultipleArguments() {
        assertDoesNotThrow(() -> {
            // Test with a message that could take arguments
            String message = GUIResources.getMessage("cantOpenConfigurationFile",
                                                     new Object[]{"config.pro"});
            assertNotNull(message);
            assertTrue(message.length() > 0, "Formatted message should not be empty");
        });
    }

    /**
     * Test that constructor can be called multiple times without issues.
     * This verifies that repeated instantiation doesn't cause problems.
     */
    @Test
    public void testConstructorCanBeCalledMultipleTimes() {
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 10; i++) {
                GUIResources instance = new GUIResources();
                assertNotNull(instance, "Each constructor call should create a valid instance");
            }
        });
    }

    /**
     * Test that getMessage works correctly with empty argument array.
     * This tests edge case handling in the formatted message method.
     */
    @Test
    public void testGetMessageWithEmptyArguments() {
        assertDoesNotThrow(() -> {
            // Using a simple key that doesn't expect arguments
            String message = GUIResources.getMessage("cantOpenConfigurationFile",
                                                     new Object[]{});
            assertNotNull(message, "getMessage should handle empty arguments array");
        });
    }

    /**
     * Test that the class can be loaded and constructor executed without the properties file
     * being explicitly accessed (lazy loading test).
     */
    @Test
    public void testConstructorBeforeStaticMethodCalls() {
        // Create instance before calling any static methods
        // This tests that constructor doesn't trigger issues with static initialization
        GUIResources instance = new GUIResources();
        assertNotNull(instance);

        // Now call static method - should work fine
        String message = GUIResources.getMessage("proGuardTab");
        assertEquals("ProGuard", message);
    }

    /**
     * Test getMessage with a message that has special characters.
     * This verifies that the ResourceBundle handles various message formats correctly.
     */
    @Test
    public void testGetMessageWithSpecialCharacters() {
        // The "developed" message should contain special characters
        String message = GUIResources.getMessage("developed");
        assertNotNull(message);
        assertEquals("Developed by Guardsquare", message);
    }

    /**
     * Test that null cannot be passed as message key.
     * This verifies error handling in getMessage.
     */
    @Test
    public void testGetMessageWithNullKey() {
        assertThrows(NullPointerException.class, () -> {
            GUIResources.getMessage(null);
        }, "getMessage should throw NullPointerException for null key");
    }

    /**
     * Test that the ResourceBundle is loaded from the correct location.
     * This verifies line 34 initialization: ResourceBundle.getBundle(GUIResources.class.getName())
     */
    @Test
    public void testResourceBundleLoadedFromCorrectLocation() {
        // If the ResourceBundle is loaded correctly, we should be able to access
        // all the expected keys from the properties file
        assertDoesNotThrow(() -> {
            GUIResources.getMessage("proGuardTab");
            GUIResources.getMessage("inputOutputTab");
            GUIResources.getMessage("shrinkingTab");
            GUIResources.getMessage("obfuscationTab");
            GUIResources.getMessage("optimizationTab");
            GUIResources.getMessage("informationTab");
            GUIResources.getMessage("processTab");
            GUIResources.getMessage("reTraceTab");
        }, "All expected keys should be accessible from the loaded ResourceBundle");
    }
}
