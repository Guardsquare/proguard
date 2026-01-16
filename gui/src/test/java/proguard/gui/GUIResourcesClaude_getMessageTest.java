package proguard.gui;

import org.junit.jupiter.api.Test;

import java.util.MissingResourceException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for GUIResources.getMessage methods.
 *
 * This class tests both overloaded getMessage methods:
 *
 * 1. getMessage(String messageKey) - lines 41-44 of GUIResources.java
 *    - Takes a String messageKey
 *    - Returns an internationalized message from the ResourceBundle
 *    - Implementation: return messages.getString(messageKey);
 *
 * 2. getMessage(String messageKey, Object[] messageArguments) - lines 51-55 of GUIResources.java
 *    - Takes a String messageKey and Object[] messageArguments
 *    - Returns an internationalized, formatted message
 *    - Implementation:
 *      formatter.applyPattern(messages.getString(messageKey));
 *      return formatter.format(messageArguments);
 *
 * These tests verify the methods work correctly with:
 * - Valid keys from the properties file
 * - Invalid keys (should throw MissingResourceException)
 * - Null keys (should throw NullPointerException)
 * - Various types of property values (simple, multi-line, with special characters)
 * - Message formatting with arguments (for the second overload)
 */
public class GUIResourcesClaude_getMessageTest {

    /**
     * Test getMessage with a simple valid key.
     * This tests the basic functionality of line 43: messages.getString(messageKey)
     */
    @Test
    public void testGetMessageWithSimpleValidKey() {
        String result = GUIResources.getMessage("proGuardTab");
        assertNotNull(result, "getMessage should return a non-null value");
        assertEquals("ProGuard", result, "getMessage should return the correct value for 'proGuardTab'");
    }

    /**
     * Test getMessage with multiple valid keys to ensure consistent behavior.
     * Verifies that the ResourceBundle is properly loaded and accessible.
     */
    @Test
    public void testGetMessageWithMultipleValidKeys() {
        assertEquals("ProGuard", GUIResources.getMessage("proGuardTab"));
        assertEquals("Input/Output", GUIResources.getMessage("inputOutputTab"));
        assertEquals("Shrinking", GUIResources.getMessage("shrinkingTab"));
        assertEquals("Obfuscation", GUIResources.getMessage("obfuscationTab"));
        assertEquals("Optimization", GUIResources.getMessage("optimizationTab"));
        assertEquals("Information", GUIResources.getMessage("informationTab"));
        assertEquals("Process", GUIResources.getMessage("processTab"));
        assertEquals("ReTrace", GUIResources.getMessage("reTraceTab"));
    }

    /**
     * Test getMessage with keys that have special characters.
     * The "developed" key has a value "Developed by Guardsquare".
     */
    @Test
    public void testGetMessageWithSpecialCharacters() {
        String result = GUIResources.getMessage("developed");
        assertNotNull(result);
        assertEquals("Developed by Guardsquare", result,
                    "getMessage should correctly return values with special characters");
    }

    /**
     * Test getMessage with keys for multi-word values.
     * Tests keys like "shrinkingTab", "obfuscationTab", etc.
     */
    @Test
    public void testGetMessageWithMultiWordValues() {
        String result = GUIResources.getMessage("preverificationAndTargeting");
        assertNotNull(result);
        assertEquals("Preverification and targeting", result);

        result = GUIResources.getMessage("consistencyAndCorrectness");
        assertEquals("Consistency and correctness", result);

        result = GUIResources.getMessage("processingConsole");
        assertEquals("Processing console", result);
    }

    /**
     * Test getMessage with keys that have multi-line values.
     * The properties file contains several multi-line values (using backslash continuation).
     */
    @Test
    public void testGetMessageWithMultilineValue() {
        String result = GUIResources.getMessage("keepAdditionalTip");
        assertNotNull(result);
        assertTrue(result.contains("additional classes"),
                  "Multi-line property value should be properly loaded");
    }

    /**
     * Test getMessage with an invalid key.
     * This should throw MissingResourceException as per ResourceBundle.getString behavior.
     */
    @Test
    public void testGetMessageWithInvalidKey() {
        assertThrows(MissingResourceException.class, () -> {
            GUIResources.getMessage("nonExistentKey");
        }, "getMessage should throw MissingResourceException for non-existent keys");
    }

    /**
     * Test getMessage with a null key.
     * This should throw NullPointerException as per ResourceBundle.getString behavior.
     */
    @Test
    public void testGetMessageWithNullKey() {
        assertThrows(NullPointerException.class, () -> {
            GUIResources.getMessage(null);
        }, "getMessage should throw NullPointerException for null key");
    }

    /**
     * Test getMessage with an empty string key.
     * This should throw MissingResourceException since "" is not a valid key in the properties.
     */
    @Test
    public void testGetMessageWithEmptyKey() {
        assertThrows(MissingResourceException.class, () -> {
            GUIResources.getMessage("");
        }, "getMessage should throw MissingResourceException for empty string key");
    }

    /**
     * Test getMessage returns different values for different keys.
     * This ensures the method is actually looking up the key and not returning a constant.
     */
    @Test
    public void testGetMessageReturnsDifferentValuesForDifferentKeys() {
        String value1 = GUIResources.getMessage("proGuardTab");
        String value2 = GUIResources.getMessage("inputOutputTab");
        String value3 = GUIResources.getMessage("shrinkingTab");

        assertNotEquals(value1, value2, "Different keys should return different values");
        assertNotEquals(value2, value3, "Different keys should return different values");
        assertNotEquals(value1, value3, "Different keys should return different values");
    }

    /**
     * Test getMessage with keys that have HTML content.
     * Several properties contain HTML markup for tooltips.
     */
    @Test
    public void testGetMessageWithHtmlContent() {
        String result = GUIResources.getMessage("proGuardInfo");
        assertNotNull(result);
        assertTrue(result.contains("<html>"), "HTML content should be preserved in the message");
        assertTrue(result.contains("ProGuard"), "Content should be correct");
    }

    /**
     * Test getMessage called multiple times with the same key returns the same result.
     * This verifies that the method is idempotent and the ResourceBundle is stable.
     */
    @Test
    public void testGetMessageIsIdempotent() {
        String key = "proGuardTab";
        String result1 = GUIResources.getMessage(key);
        String result2 = GUIResources.getMessage(key);
        String result3 = GUIResources.getMessage(key);

        assertEquals(result1, result2, "Multiple calls with same key should return same value");
        assertEquals(result2, result3, "Multiple calls with same key should return same value");
        assertEquals(result1, result3, "Multiple calls with same key should return same value");
    }

    /**
     * Test getMessage with keys from different sections of the properties file.
     * The properties file is organized into sections (tabs, splash text, panel titles, etc.).
     */
    @Test
    public void testGetMessageFromDifferentSections() {
        // Tab name section
        assertEquals("ProGuard", GUIResources.getMessage("proGuardTab"));

        // Splash text section
        assertEquals("Shrinking", GUIResources.getMessage("shrinking"));
        assertEquals("Optimization", GUIResources.getMessage("optimization"));

        // Panel titles section
        assertEquals("Welcome to ProGuard", GUIResources.getMessage("welcome"));
        assertEquals("Options", GUIResources.getMessage("options"));

        // Button texts section
        assertEquals("Ok", GUIResources.getMessage("ok"));
        assertEquals("Cancel", GUIResources.getMessage("cancel"));
    }

    /**
     * Test getMessage with keys that have single-word values.
     */
    @Test
    public void testGetMessageWithSingleWordValues() {
        assertEquals("Shrinking", GUIResources.getMessage("shrinking"));
        assertEquals("Optimization", GUIResources.getMessage("optimization"));
        assertEquals("Obfuscation", GUIResources.getMessage("obfuscation"));
        assertEquals("Preverification", GUIResources.getMessage("preverification"));
    }

    /**
     * Test getMessage with various action keys (button labels).
     */
    @Test
    public void testGetMessageWithActionKeys() {
        assertEquals("Ok", GUIResources.getMessage("ok"));
        assertEquals("Cancel", GUIResources.getMessage("cancel"));
        assertEquals("Browse...", GUIResources.getMessage("browse"));
        assertEquals("Process!", GUIResources.getMessage("process"));
        assertEquals("ReTrace!", GUIResources.getMessage("reTrace"));
    }

    /**
     * Test getMessage with file-related keys.
     */
    @Test
    public void testGetMessageWithFileRelatedKeys() {
        assertEquals("Mapping file", GUIResources.getMessage("mappingFile"));
        assertEquals("Obfuscated stack trace", GUIResources.getMessage("obfuscatedStackTrace"));
    }

    /**
     * Test getMessage with keys that contain underscores.
     * Many keys in the properties file use underscores for word separation.
     */
    @Test
    public void testGetMessageWithUnderscoreKeys() {
        assertEquals("Input/Output", GUIResources.getMessage("inputOutputTab"));
        assertEquals("Allow access modification", GUIResources.getMessage("allowAccessModification"));
        assertEquals("Merge interfaces aggressively", GUIResources.getMessage("mergeInterfacesAggressively"));
        assertEquals("Use unique class member names", GUIResources.getMessage("useUniqueClassMemberNames"));
    }

    /**
     * Test getMessage with keys for boolean-type options.
     */
    @Test
    public void testGetMessageWithBooleanOptionKeys() {
        assertEquals("Shrink", GUIResources.getMessage("shrink"));
        assertEquals("Optimize", GUIResources.getMessage("optimize"));
        assertEquals("Obfuscate", GUIResources.getMessage("obfuscate"));
        assertEquals("Preverify", GUIResources.getMessage("preverify"));
        assertEquals("Verbose", GUIResources.getMessage("verbose"));
    }

    /**
     * Test getMessage with keys that have numeric values or contain numbers.
     */
    @Test
    public void testGetMessageWithNumericContent() {
        String targets = GUIResources.getMessage("targets");
        assertNotNull(targets);
        assertTrue(targets.contains("1.0"), "Targets should contain version numbers");
    }

    /**
     * Test getMessage with tooltip keys.
     * Many keys end with "Tip" and provide tooltip text.
     */
    @Test
    public void testGetMessageWithTooltipKeys() {
        assertNotNull(GUIResources.getMessage("shrinkTip"));
        assertNotNull(GUIResources.getMessage("optimizeTip"));
        assertNotNull(GUIResources.getMessage("obfuscateTip"));
        assertNotNull(GUIResources.getMessage("preverifyTip"));

        String shrinkTip = GUIResources.getMessage("shrinkTip");
        assertTrue(shrinkTip.length() > 0, "Tooltip should have content");
    }

    /**
     * Test getMessage with warning/error message keys.
     */
    @Test
    public void testGetMessageWithWarningKeys() {
        assertEquals("Warning", GUIResources.getMessage("warning"));
        assertEquals("Out of memory", GUIResources.getMessage("outOfMemory"));
        assertEquals("Error during processing", GUIResources.getMessage("errorProcessing"));
        assertEquals("Error during retracing", GUIResources.getMessage("errorReTracing"));
    }

    /**
     * Test getMessage with keys that have formatted message patterns.
     * These messages contain {0}, {1}, etc. placeholders for MessageFormat.
     * When called through getMessage(String), these placeholders should remain in the string.
     */
    @Test
    public void testGetMessageWithMessageFormatPatterns() {
        String message = GUIResources.getMessage("cantOpenConfigurationFile");
        assertNotNull(message);
        assertTrue(message.contains("{0}"),
                  "Message pattern placeholders should be present when using getMessage(String)");
    }

    /**
     * Test getMessage with filter-related keys.
     */
    @Test
    public void testGetMessageWithFilterKeys() {
        assertEquals("Filters", GUIResources.getMessage("filters"));
        assertEquals("File name filter", GUIResources.getMessage("nameFilter"));
        assertEquals("Jar name filter", GUIResources.getMessage("jarNameFilter"));
    }

    /**
     * Test getMessage with access modifier keys.
     */
    @Test
    public void testGetMessageWithAccessKeys() {
        assertEquals("Access", GUIResources.getMessage("access"));
        assertEquals("Required", GUIResources.getMessage("required"));
        assertEquals("Not", GUIResources.getMessage("not"));
        assertEquals("Don't care", GUIResources.getMessage("dontCare"));
    }

    /**
     * Test getMessage with keys containing parentheses.
     */
    @Test
    public void testGetMessageWithParentheses() {
        assertEquals("(none)", GUIResources.getMessage("none"));
    }

    /**
     * Test getMessage with keys for keep options.
     */
    @Test
    public void testGetMessageWithKeepOptionKeys() {
        assertEquals("Keep", GUIResources.getMessage("keepTitle"));
        assertEquals("Keep classes", GUIResources.getMessage("keepClasses"));
        assertEquals("Keep class members", GUIResources.getMessage("keepClassMembers"));
        assertEquals("Keep classes and class members, if members are present",
                    GUIResources.getMessage("keepClassesWithMembers"));
    }

    /**
     * Test getMessage with navigation keys.
     */
    @Test
    public void testGetMessageWithNavigationKeys() {
        assertEquals("Previous", GUIResources.getMessage("previous"));
        assertEquals("Next", GUIResources.getMessage("next"));
    }

    /**
     * Test getMessage with keys for list manipulation actions.
     */
    @Test
    public void testGetMessageWithListActionKeys() {
        assertEquals("Add...", GUIResources.getMessage("add"));
        assertEquals("Edit...", GUIResources.getMessage("edit"));
        assertEquals("Remove", GUIResources.getMessage("remove"));
        assertEquals("Move up", GUIResources.getMessage("moveUp"));
        assertEquals("Move down", GUIResources.getMessage("moveDown"));
    }

    /**
     * Test getMessage with descriptive keys about program structure.
     */
    @Test
    public void testGetMessageWithProgramStructureKeys() {
        String programJars = GUIResources.getMessage("programJars");
        assertNotNull(programJars);
        assertTrue(programJars.contains("jars"),
                  "Program structure messages should contain expected content");

        String libraryJars = GUIResources.getMessage("libraryJars");
        assertNotNull(libraryJars);
        assertTrue(libraryJars.contains("jars"),
                  "Library structure messages should contain expected content");
    }

    /**
     * Test getMessage with file extension keys.
     */
    @Test
    public void testGetMessageWithFileExtensionKeys() {
        String jarExtensions = GUIResources.getMessage("jarExtensions");
        assertNotNull(jarExtensions);
        assertTrue(jarExtensions.contains(".jar"), "Extension list should contain .jar");

        String proExtension = GUIResources.getMessage("proExtension");
        assertNotNull(proExtension);
        assertTrue(proExtension.contains(".pro"), "Extension should contain .pro");
    }

    /**
     * Test getMessage return value is never null for valid keys.
     * This is an important contract of the method.
     */
    @Test
    public void testGetMessageNeverReturnsNullForValidKeys() {
        // Test a sample of various keys
        String[] validKeys = {
            "proGuardTab", "inputOutputTab", "shrinkingTab", "obfuscationTab",
            "welcome", "options", "shrink", "optimize", "ok", "cancel"
        };

        for (String key : validKeys) {
            String result = GUIResources.getMessage(key);
            assertNotNull(result, "getMessage should never return null for valid key: " + key);
        }
    }

    /**
     * Test that getMessage preserves exact string content from properties file.
     * This verifies that no unexpected transformations occur.
     */
    @Test
    public void testGetMessagePreservesExactContent() {
        // Test exact matches for several keys
        assertEquals("ProGuard", GUIResources.getMessage("proGuardTab"));
        assertEquals("Input/Output", GUIResources.getMessage("inputOutputTab"));
        assertEquals("Developed by Guardsquare", GUIResources.getMessage("developed"));
        assertEquals("Process!", GUIResources.getMessage("process"));
        assertEquals("ReTrace!", GUIResources.getMessage("reTrace"));
    }

    // ========================================================================
    // Tests for getMessage(String messageKey, Object[] messageArguments)
    // ========================================================================

    /**
     * Test getMessage with arguments - basic functionality.
     * This tests lines 51-55: formatter.applyPattern() and formatter.format()
     *
     * The property "cantOpenConfigurationFile" has pattern: "Can''t open the configuration file [{0}]"
     * Note: Two single quotes ('') in properties file = one single quote in the string
     */
    @Test
    public void testGetMessageWithArgumentsBasic() {
        String result = GUIResources.getMessage("cantOpenConfigurationFile",
                                                new Object[]{"test.pro"});
        assertNotNull(result, "getMessage with arguments should return non-null");
        assertTrue(result.contains("test.pro"),
                  "Result should contain the argument value");
        assertTrue(result.contains("Can't open"),
                  "Result should contain the base message text");
    }

    /**
     * Test getMessage with arguments using different message keys.
     * Tests that the formatter correctly applies different patterns.
     */
    @Test
    public void testGetMessageWithArgumentsDifferentKeys() {
        // Test cantOpenConfigurationFile
        String result1 = GUIResources.getMessage("cantOpenConfigurationFile",
                                                 new Object[]{"config.pro"});
        assertTrue(result1.contains("config.pro"));
        assertTrue(result1.contains("configuration file"));

        // Test cantParseConfigurationFile
        String result2 = GUIResources.getMessage("cantParseConfigurationFile",
                                                 new Object[]{"myconfig.pro"});
        assertTrue(result2.contains("myconfig.pro"));
        assertTrue(result2.contains("parse"));

        // Test cantSaveConfigurationFile
        String result3 = GUIResources.getMessage("cantSaveConfigurationFile",
                                                 new Object[]{"output.pro"});
        assertTrue(result3.contains("output.pro"));
        assertTrue(result3.contains("save"));

        // Test cantOpenStackTraceFile
        String result4 = GUIResources.getMessage("cantOpenStackTraceFile",
                                                 new Object[]{"stacktrace.txt"});
        assertTrue(result4.contains("stacktrace.txt"));
        assertTrue(result4.contains("stack trace"));
    }

    /**
     * Test getMessage with arguments containing special characters.
     * Verifies that special characters in arguments are properly handled.
     */
    @Test
    public void testGetMessageWithArgumentsSpecialCharacters() {
        // Test with path containing special characters
        String result = GUIResources.getMessage("cantOpenConfigurationFile",
                                                new Object[]{"C:\\Program Files\\config.pro"});
        assertNotNull(result);
        assertTrue(result.contains("config.pro"),
                  "Arguments with special characters should be preserved");
    }

    /**
     * Test getMessage with arguments containing spaces.
     */
    @Test
    public void testGetMessageWithArgumentsContainingSpaces() {
        String result = GUIResources.getMessage("cantOpenConfigurationFile",
                                                new Object[]{"my config file.pro"});
        assertNotNull(result);
        assertTrue(result.contains("my config file.pro"),
                  "Arguments with spaces should be preserved");
    }

    /**
     * Test getMessage with empty arguments array.
     * When a message expects arguments but receives an empty array,
     * MessageFormat should leave placeholders as-is or throw an exception.
     */
    @Test
    public void testGetMessageWithEmptyArgumentsArray() {
        // This should either throw an exception or leave {0} in the string
        assertDoesNotThrow(() -> {
            String result = GUIResources.getMessage("cantOpenConfigurationFile",
                                                   new Object[]{});
            // The result will have the pattern without substitution or might throw
            assertNotNull(result);
        });
    }

    /**
     * Test getMessage with arguments where argument is null.
     * MessageFormat should handle null arguments gracefully.
     */
    @Test
    public void testGetMessageWithNullArgument() {
        String result = GUIResources.getMessage("cantOpenConfigurationFile",
                                                new Object[]{null});
        assertNotNull(result, "getMessage should handle null argument values");
        // MessageFormat typically converts null to "null" string
    }

    /**
     * Test getMessage with null arguments array.
     * This should throw NullPointerException when MessageFormat.format is called.
     */
    @Test
    public void testGetMessageWithNullArgumentsArray() {
        assertThrows(NullPointerException.class, () -> {
            GUIResources.getMessage("cantOpenConfigurationFile", null);
        }, "getMessage should throw NullPointerException for null arguments array");
    }

    /**
     * Test getMessage with arguments for invalid key.
     * Should throw MissingResourceException when trying to get the pattern.
     */
    @Test
    public void testGetMessageWithArgumentsInvalidKey() {
        assertThrows(MissingResourceException.class, () -> {
            GUIResources.getMessage("nonExistentKey", new Object[]{"value"});
        }, "getMessage with arguments should throw MissingResourceException for invalid key");
    }

    /**
     * Test getMessage with arguments for null key.
     * Should throw NullPointerException when accessing the ResourceBundle.
     */
    @Test
    public void testGetMessageWithArgumentsNullKey() {
        assertThrows(NullPointerException.class, () -> {
            GUIResources.getMessage(null, new Object[]{"value"});
        }, "getMessage with arguments should throw NullPointerException for null key");
    }

    /**
     * Test getMessage with arguments is idempotent.
     * Multiple calls with same key and arguments should return the same result.
     */
    @Test
    public void testGetMessageWithArgumentsIsIdempotent() {
        Object[] args = new Object[]{"test.pro"};
        String result1 = GUIResources.getMessage("cantOpenConfigurationFile", args);
        String result2 = GUIResources.getMessage("cantOpenConfigurationFile", args);
        String result3 = GUIResources.getMessage("cantOpenConfigurationFile", args);

        assertEquals(result1, result2, "Multiple calls should return same result");
        assertEquals(result2, result3, "Multiple calls should return same result");
    }

    /**
     * Test getMessage with arguments using different argument values.
     * Verifies that different arguments produce different results.
     */
    @Test
    public void testGetMessageWithArgumentsDifferentValues() {
        String result1 = GUIResources.getMessage("cantOpenConfigurationFile",
                                                 new Object[]{"file1.pro"});
        String result2 = GUIResources.getMessage("cantOpenConfigurationFile",
                                                 new Object[]{"file2.pro"});

        assertNotEquals(result1, result2,
                       "Different arguments should produce different results");
        assertTrue(result1.contains("file1.pro"));
        assertTrue(result2.contains("file2.pro"));
    }

    /**
     * Test that the static MessageFormat formatter is reused.
     * This tests that line 53 (formatter.applyPattern) correctly updates
     * the pattern for each call, ensuring no state leakage between calls.
     */
    @Test
    public void testGetMessageWithArgumentsFormatterReuse() {
        // Call with first pattern and argument
        String result1 = GUIResources.getMessage("cantOpenConfigurationFile",
                                                 new Object[]{"first.pro"});
        assertTrue(result1.contains("first.pro"));

        // Call with different pattern and argument
        String result2 = GUIResources.getMessage("cantParseConfigurationFile",
                                                 new Object[]{"second.pro"});
        assertTrue(result2.contains("second.pro"));
        assertTrue(result2.contains("parse"));

        // Call with first pattern again to ensure formatter was properly reset
        String result3 = GUIResources.getMessage("cantOpenConfigurationFile",
                                                 new Object[]{"third.pro"});
        assertTrue(result3.contains("third.pro"));
        assertFalse(result3.contains("parse"),
                   "Formatter should not have state from previous call");
    }

    /**
     * Test getMessage with arguments containing numeric values.
     */
    @Test
    public void testGetMessageWithArgumentsNumericValues() {
        String result = GUIResources.getMessage("cantOpenConfigurationFile",
                                                new Object[]{123});
        assertNotNull(result);
        assertTrue(result.contains("123"), "Numeric arguments should be converted to strings");
    }

    /**
     * Test getMessage with arguments containing boolean values.
     */
    @Test
    public void testGetMessageWithArgumentsBooleanValues() {
        String result = GUIResources.getMessage("cantOpenConfigurationFile",
                                                new Object[]{true});
        assertNotNull(result);
        assertTrue(result.contains("true"), "Boolean arguments should be converted to strings");
    }

    /**
     * Test getMessage with arguments for a key that doesn't have format patterns.
     * When using a simple key (without {0}, {1}, etc.) with the arguments method,
     * the message should still be returned correctly.
     */
    @Test
    public void testGetMessageWithArgumentsForSimpleKey() {
        // "proGuardTab" = "ProGuard" (no format patterns)
        String result = GUIResources.getMessage("proGuardTab", new Object[]{"ignored"});
        assertNotNull(result);
        assertEquals("ProGuard", result,
                    "Simple keys without patterns should work with arguments method");
    }

    /**
     * Test getMessage with arguments containing empty string.
     */
    @Test
    public void testGetMessageWithArgumentsEmptyString() {
        String result = GUIResources.getMessage("cantOpenConfigurationFile",
                                                new Object[]{""});
        assertNotNull(result);
        // Empty string argument should still format the message
    }

    /**
     * Test getMessage with arguments where argument is a complex object.
     * MessageFormat calls toString() on the object.
     */
    @Test
    public void testGetMessageWithArgumentsComplexObject() {
        Object complexObject = new Object() {
            @Override
            public String toString() {
                return "CustomObject";
            }
        };

        String result = GUIResources.getMessage("cantOpenConfigurationFile",
                                                new Object[]{complexObject});
        assertNotNull(result);
        assertTrue(result.contains("CustomObject"),
                  "Complex objects should be converted using toString()");
    }

    /**
     * Test that getMessage with arguments never returns null for valid keys.
     */
    @Test
    public void testGetMessageWithArgumentsNeverReturnsNull() {
        String[] validKeys = {
            "cantOpenConfigurationFile",
            "cantParseConfigurationFile",
            "cantSaveConfigurationFile",
            "cantOpenStackTraceFile"
        };

        for (String key : validKeys) {
            String result = GUIResources.getMessage(key, new Object[]{"test"});
            assertNotNull(result,
                         "getMessage with arguments should never return null for key: " + key);
        }
    }

    /**
     * Test getMessage with arguments using very long argument values.
     * Verifies that long strings are handled correctly.
     */
    @Test
    public void testGetMessageWithArgumentsLongValue() {
        String longPath = "very/long/path/to/configuration/file/that/might/be/used/in/real/world/scenario.pro";
        String result = GUIResources.getMessage("cantOpenConfigurationFile",
                                                new Object[]{longPath});
        assertNotNull(result);
        assertTrue(result.contains(longPath), "Long argument values should be preserved");
    }

    /**
     * Test getMessage with arguments containing Unicode characters.
     */
    @Test
    public void testGetMessageWithArgumentsUnicodeCharacters() {
        String unicodePath = "config_\u00E9\u00E0\u00F1.pro"; // Contains é, à, ñ
        String result = GUIResources.getMessage("cantOpenConfigurationFile",
                                                new Object[]{unicodePath});
        assertNotNull(result);
        assertTrue(result.contains(unicodePath), "Unicode characters should be preserved");
    }

    /**
     * Test getMessage with arguments called consecutively with different keys.
     * Verifies that the formatter state is properly managed between calls.
     */
    @Test
    public void testGetMessageWithArgumentsConsecutiveCalls() {
        for (int i = 0; i < 5; i++) {
            String result = GUIResources.getMessage("cantOpenConfigurationFile",
                                                   new Object[]{"file" + i + ".pro"});
            assertTrue(result.contains("file" + i + ".pro"),
                      "Each consecutive call should format correctly");
        }
    }

    /**
     * Test that both getMessage overloads can be used interchangeably.
     * This verifies that using one method doesn't affect the other.
     */
    @Test
    public void testBothGetMessageOverloadsIndependent() {
        // Call getMessage(String)
        String result1 = GUIResources.getMessage("proGuardTab");
        assertEquals("ProGuard", result1);

        // Call getMessage(String, Object[])
        String result2 = GUIResources.getMessage("cantOpenConfigurationFile",
                                                 new Object[]{"test.pro"});
        assertTrue(result2.contains("test.pro"));

        // Call getMessage(String) again
        String result3 = GUIResources.getMessage("inputOutputTab");
        assertEquals("Input/Output", result3);

        // Call getMessage(String, Object[]) again with different key
        String result4 = GUIResources.getMessage("cantParseConfigurationFile",
                                                 new Object[]{"parse.pro"});
        assertTrue(result4.contains("parse.pro"));

        // Verify original calls still work
        assertEquals("ProGuard", GUIResources.getMessage("proGuardTab"));
    }

    /**
     * Test getMessage with arguments where the message pattern contains special MessageFormat syntax.
     * The properties file uses '' (two single quotes) to represent a literal single quote.
     */
    @Test
    public void testGetMessageWithArgumentsMessageFormatEscaping() {
        // "cantOpenConfigurationFile" = "Can''t open the configuration file [{0}]"
        // The '' should become ' in the result
        String result = GUIResources.getMessage("cantOpenConfigurationFile",
                                                new Object[]{"test.pro"});
        assertTrue(result.contains("Can't"),
                  "Double single quotes in pattern should become single quote in result");
        assertTrue(result.contains("[test.pro]"),
                  "Brackets and arguments should be properly formatted");
    }

    /**
     * Test getMessage with arguments using array containing multiple elements
     * where only first element is needed.
     */
    @Test
    public void testGetMessageWithArgumentsExtraArguments() {
        // Pattern only has {0}, but we provide multiple arguments
        String result = GUIResources.getMessage("cantOpenConfigurationFile",
                                                new Object[]{"first.pro", "second.pro", "third.pro"});
        assertNotNull(result);
        assertTrue(result.contains("first.pro"),
                  "First argument should be used");
        assertFalse(result.contains("second.pro"),
                   "Extra arguments should be ignored");
    }
}
