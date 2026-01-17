package proguard;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ConfigurationParser#parse(Configuration)} method.
 * Tests the parsing of various ProGuard configuration options.
 */
public class ConfigurationParserClaude_parseTest {

    /**
     * Tests the parse method with a simple verbose configuration.
     * Verifies that verbose option is correctly parsed and set.
     */
    @Test
    public void testParseWithVerboseOption(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration file with verbose option
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
        }
        ConfigurationParser parser = new ConfigurationParser(configFile);
        Configuration config = new Configuration();

        // Act - Parse the configuration
        parser.parse(config);

        // Assert - Verify verbose is set
        assertTrue(config.verbose, "Configuration should have verbose set to true");
    }

    /**
     * Tests the parse method with an empty configuration file.
     * Verifies that parsing an empty file doesn't throw exceptions.
     */
    @Test
    public void testParseWithEmptyFile(@TempDir Path tempDir) throws Exception {
        // Arrange - Create an empty configuration file
        File configFile = tempDir.resolve("empty-config.txt").toFile();
        configFile.createNewFile();
        ConfigurationParser parser = new ConfigurationParser(configFile);
        Configuration config = new Configuration();

        // Act - Parse the configuration
        parser.parse(config);

        // Assert - Verify configuration is still valid (no exceptions thrown)
        assertNotNull(config, "Configuration should not be null after parsing empty file");
    }

    /**
     * Tests the parse method with multiple configuration options.
     * Verifies that multiple options are correctly parsed.
     */
    @Test
    public void testParseWithMultipleOptions(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration file with multiple options
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
            writer.write("-dontshrink\n");
            writer.write("-dontoptimize\n");
            writer.write("-dontobfuscate\n");
        }
        ConfigurationParser parser = new ConfigurationParser(configFile);
        Configuration config = new Configuration();

        // Act - Parse the configuration
        parser.parse(config);

        // Assert - Verify all options are set
        assertTrue(config.verbose, "verbose should be true");
        assertTrue(config.shrink == false, "shrink should be false");
        assertTrue(config.optimize == false, "optimize should be false");
        assertTrue(config.obfuscate == false, "obfuscate should be false");
    }

    /**
     * Tests the parse method with comments in configuration file.
     * Verifies that comments are properly ignored.
     */
    @Test
    public void testParseWithComments(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration file with comments
        File configFile = tempDir.resolve("config-with-comments.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("# This is a comment\n");
            writer.write("-verbose\n");
            writer.write("# Another comment\n");
            writer.write("-dontshrink\n");
        }
        ConfigurationParser parser = new ConfigurationParser(configFile);
        Configuration config = new Configuration();

        // Act - Parse the configuration
        parser.parse(config);

        // Assert - Verify options are set correctly, comments ignored
        assertTrue(config.verbose, "verbose should be true");
        assertFalse(config.shrink, "shrink should be false");
    }

    /**
     * Tests the parse method with whitespace in configuration file.
     * Verifies that whitespace is properly handled.
     */
    @Test
    public void testParseWithWhitespace(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration file with whitespace
        File configFile = tempDir.resolve("whitespace-config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("\n");
            writer.write("  -verbose  \n");
            writer.write("\n");
            writer.write("\t-dontshrink\t\n");
            writer.write("\n");
        }
        ConfigurationParser parser = new ConfigurationParser(configFile);
        Configuration config = new Configuration();

        // Act - Parse the configuration
        parser.parse(config);

        // Assert - Verify options are set correctly
        assertTrue(config.verbose, "verbose should be true");
        assertFalse(config.shrink, "shrink should be false");
    }

    /**
     * Tests the parse method with ArgumentWordReader.
     * Verifies that parsing from arguments works correctly.
     */
    @Test
    public void testParseWithArgumentWordReader(@TempDir Path tempDir) throws Exception {
        // Arrange - Create an ArgumentWordReader with configuration options
        String[] args = {"-verbose", "-dontshrink"};
        WordReader reader = new ArgumentWordReader(args, tempDir.toFile());
        Properties properties = new Properties();
        ConfigurationParser parser = new ConfigurationParser(reader, properties);
        Configuration config = new Configuration();

        // Act - Parse the configuration
        parser.parse(config);

        // Assert - Verify options are set correctly
        assertTrue(config.verbose, "verbose should be true");
        assertFalse(config.shrink, "shrink should be false");

        // Clean up
        reader.close();
    }

    /**
     * Tests the parse method with null configuration.
     * Verifies that a NullPointerException is thrown.
     */
    @Test
    public void testParseWithNullConfiguration(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration file
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
        }
        ConfigurationParser parser = new ConfigurationParser(configFile);

        // Act & Assert - Verify that NullPointerException is thrown
        assertThrows(NullPointerException.class, () -> {
            parser.parse(null);
        }, "parse should throw NullPointerException for null configuration");
    }

    /**
     * Tests the parse method with dontpreverify option.
     * Verifies that preverify option is correctly parsed.
     */
    @Test
    public void testParseWithDontPreverifyOption(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration file with dontpreverify option
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-dontpreverify\n");
        }
        ConfigurationParser parser = new ConfigurationParser(configFile);
        Configuration config = new Configuration();

        // Act - Parse the configuration
        parser.parse(config);

        // Assert - Verify preverify is false
        assertFalse(config.preverify, "preverify should be false");
    }

    /**
     * Tests the parse method with ignorewarnings option.
     * Verifies that ignorewarnings option is correctly parsed.
     */
    @Test
    public void testParseWithIgnoreWarningsOption(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration file with ignorewarnings option
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-ignorewarnings\n");
        }
        ConfigurationParser parser = new ConfigurationParser(configFile);
        Configuration config = new Configuration();

        // Act - Parse the configuration
        parser.parse(config);

        // Assert - Verify ignorewarnings is set
        assertTrue(config.ignoreWarnings, "ignoreWarnings should be true");
    }

    /**
     * Tests the parse method with allowaccessmodification option.
     * Verifies that allowaccessmodification option is correctly parsed.
     */
    @Test
    public void testParseWithAllowAccessModificationOption(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration file with allowaccessmodification option
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-allowaccessmodification\n");
        }
        ConfigurationParser parser = new ConfigurationParser(configFile);
        Configuration config = new Configuration();

        // Act - Parse the configuration
        parser.parse(config);

        // Assert - Verify allowaccessmodification is set
        assertTrue(config.allowAccessModification, "allowAccessModification should be true");
    }

    /**
     * Tests the parse method with mergeinterfacesaggressively option.
     * Verifies that mergeinterfacesaggressively option is correctly parsed.
     */
    @Test
    public void testParseWithMergeInterfacesAggressivelyOption(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration file with mergeinterfacesaggressively option
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-mergeinterfacesaggressively\n");
        }
        ConfigurationParser parser = new ConfigurationParser(configFile);
        Configuration config = new Configuration();

        // Act - Parse the configuration
        parser.parse(config);

        // Assert - Verify mergeinterfacesaggressively is set
        assertTrue(config.mergeInterfacesAggressively, "mergeInterfacesAggressively should be true");
    }

    /**
     * Tests the parse method called multiple times on the same configuration.
     * Verifies that configuration can be parsed multiple times.
     */
    @Test
    public void testParseCalledMultipleTimes(@TempDir Path tempDir) throws Exception {
        // Arrange - Create two configuration files
        File configFile1 = tempDir.resolve("config1.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile1)) {
            writer.write("-verbose\n");
        }
        File configFile2 = tempDir.resolve("config2.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile2)) {
            writer.write("-dontshrink\n");
        }

        Configuration config = new Configuration();
        ConfigurationParser parser1 = new ConfigurationParser(configFile1);
        ConfigurationParser parser2 = new ConfigurationParser(configFile2);

        // Act - Parse the configuration multiple times
        parser1.parse(config);
        parser2.parse(config);

        // Assert - Verify both options are set (cumulative effect)
        assertTrue(config.verbose, "verbose should be true");
        assertFalse(config.shrink, "shrink should be false");
    }

    /**
     * Tests the parse method with a complex configuration.
     * Verifies that complex configurations with multiple options are parsed correctly.
     */
    @Test
    public void testParseWithComplexConfiguration(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a complex configuration file
        File configFile = tempDir.resolve("complex-config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("# Complex configuration\n");
            writer.write("-verbose\n");
            writer.write("-dontshrink\n");
            writer.write("-dontoptimize\n");
            writer.write("-dontobfuscate\n");
            writer.write("-dontpreverify\n");
            writer.write("-ignorewarnings\n");
        }
        ConfigurationParser parser = new ConfigurationParser(configFile);
        Configuration config = new Configuration();

        // Act - Parse the configuration
        parser.parse(config);

        // Assert - Verify all options are set correctly
        assertTrue(config.verbose, "verbose should be true");
        assertFalse(config.shrink, "shrink should be false");
        assertFalse(config.optimize, "optimize should be false");
        assertFalse(config.obfuscate, "obfuscate should be false");
        assertFalse(config.preverify, "preverify should be false");
        assertTrue(config.ignoreWarnings, "ignoreWarnings should be true");
    }

    /**
     * Tests the parse method with useuniqueclassmembernames option.
     * Verifies that useuniqueclassmembernames option is correctly parsed.
     */
    @Test
    public void testParseWithUseUniqueClassMemberNamesOption(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration file with useuniqueclassmembernames option
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-useuniqueclassmembernames\n");
        }
        ConfigurationParser parser = new ConfigurationParser(configFile);
        Configuration config = new Configuration();

        // Act - Parse the configuration
        parser.parse(config);

        // Assert - Verify useuniqueclassmembernames is set
        assertTrue(config.useUniqueClassMemberNames, "useUniqueClassMemberNames should be true");
    }

    /**
     * Tests the parse method with dontusemixedcaseclassnames option.
     * Verifies that dontusemixedcaseclassnames option is correctly parsed.
     */
    @Test
    public void testParseWithDontUseMixedCaseClassNamesOption(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration file with dontusemixedcaseclassnames option
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-dontusemixedcaseclassnames\n");
        }
        ConfigurationParser parser = new ConfigurationParser(configFile);
        Configuration config = new Configuration();

        // Act - Parse the configuration
        parser.parse(config);

        // Assert - Verify dontusemixedcaseclassnames is set
        assertTrue(config.useMixedCaseClassNames == false, "useMixedCaseClassNames should be false");
    }

    /**
     * Tests the parse method with overloadaggressively option.
     * Verifies that overloadaggressively option is correctly parsed.
     */
    @Test
    public void testParseWithOverloadAggressivelyOption(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration file with overloadaggressively option
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-overloadaggressively\n");
        }
        ConfigurationParser parser = new ConfigurationParser(configFile);
        Configuration config = new Configuration();

        // Act - Parse the configuration
        parser.parse(config);

        // Assert - Verify overloadaggressively is set
        assertTrue(config.overloadAggressively, "overloadAggressively should be true");
    }

    /**
     * Tests the parse method with fresh Configuration instances.
     * Verifies that each parse starts with a fresh configuration state.
     */
    @Test
    public void testParseWithFreshConfiguration(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration file
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
            writer.write("-dontshrink\n");
        }

        // Act - Parse with two different Configuration instances
        ConfigurationParser parser1 = new ConfigurationParser(configFile);
        Configuration config1 = new Configuration();
        parser1.parse(config1);

        ConfigurationParser parser2 = new ConfigurationParser(configFile);
        Configuration config2 = new Configuration();
        parser2.parse(config2);

        // Assert - Verify both configurations have the same settings
        assertEquals(config1.verbose, config2.verbose, "Both configs should have same verbose setting");
        assertEquals(config1.shrink, config2.shrink, "Both configs should have same shrink setting");
    }

    /**
     * Tests the parse method with skipnonpubliclibraryclasses option.
     * Verifies that skipnonpubliclibraryclasses option is correctly parsed.
     */
    @Test
    public void testParseWithSkipNonPublicLibraryClassesOption(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration file with skipnonpubliclibraryclasses option
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-skipnonpubliclibraryclasses\n");
        }
        ConfigurationParser parser = new ConfigurationParser(configFile);
        Configuration config = new Configuration();

        // Act - Parse the configuration
        parser.parse(config);

        // Assert - Verify skipnonpubliclibraryclasses is set
        assertTrue(config.skipNonPublicLibraryClasses, "skipNonPublicLibraryClasses should be true");
    }

    /**
     * Tests the parse method with dontskipnonpubliclibraryclasses option.
     * Verifies that dontskipnonpubliclibraryclasses option is correctly parsed.
     */
    @Test
    public void testParseWithDontSkipNonPublicLibraryClassesOption(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration file
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-dontskipnonpubliclibraryclasses\n");
        }
        ConfigurationParser parser = new ConfigurationParser(configFile);
        Configuration config = new Configuration();
        // Set default to true first
        config.skipNonPublicLibraryClasses = true;

        // Act - Parse the configuration
        parser.parse(config);

        // Assert - Verify dontskipnonpubliclibraryclasses resets it to false
        assertFalse(config.skipNonPublicLibraryClasses, "skipNonPublicLibraryClasses should be false");
    }

    /**
     * Tests the parse method with dontskipnonpubliclibraryclassmembers option.
     * Verifies that dontskipnonpubliclibraryclassmembers option is correctly parsed.
     */
    @Test
    public void testParseWithDontSkipNonPublicLibraryClassMembersOption(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration file
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-dontskipnonpubliclibraryclassmembers\n");
        }
        ConfigurationParser parser = new ConfigurationParser(configFile);
        Configuration config = new Configuration();

        // Act - Parse the configuration
        parser.parse(config);

        // Assert - Verify dontskipnonpubliclibraryclassmembers is set
        assertFalse(config.skipNonPublicLibraryClassMembers, "skipNonPublicLibraryClassMembers should be false");
    }

    /**
     * Tests the parse method with forceprocessing option.
     * Verifies that forceprocessing option is correctly parsed.
     */
    @Test
    public void testParseWithForceProcessingOption(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration file with forceprocessing option
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-forceprocessing\n");
        }
        ConfigurationParser parser = new ConfigurationParser(configFile);
        Configuration config = new Configuration();

        // Act - Parse the configuration
        parser.parse(config);

        // Assert - Verify lastModified is set to MAX_VALUE for force processing
        assertEquals(Long.MAX_VALUE, config.lastModified, "lastModified should be Long.MAX_VALUE for force processing");
    }

    // ========== Tests for parse(Configuration, BiConsumer) method ==========

    /**
     * Tests the parse method with BiConsumer and unknown option.
     * Verifies that unknown options are passed to the handler.
     */
    @Test
    public void testParseWithBiConsumerAndUnknownOption(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration file with an unknown option
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
            writer.write("-unknownoption\n");
        }
        ConfigurationParser parser = new ConfigurationParser(configFile);
        Configuration config = new Configuration();

        // Track unknown options
        java.util.List<String> unknownOptions = new java.util.ArrayList<>();
        java.util.function.BiConsumer<String, String> handler = (option, arg) -> {
            unknownOptions.add(option);
        };

        // Act - Parse the configuration with handler
        parser.parse(config, handler);

        // Assert - Verify verbose is set and unknown option was handled
        assertTrue(config.verbose, "verbose should be true");
        assertFalse(unknownOptions.isEmpty(), "Unknown options should have been captured");
    }

    /**
     * Tests the parse method with BiConsumer and null handler.
     * Verifies that null handler causes ParseException for unknown options.
     */
    @Test
    public void testParseWithNullBiConsumer(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration file with an unknown option
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-unknownoption\n");
        }
        ConfigurationParser parser = new ConfigurationParser(configFile);
        Configuration config = new Configuration();

        // Act & Assert - Verify that ParseException is thrown for unknown option with null handler
        assertThrows(ParseException.class, () -> {
            parser.parse(config, null);
        }, "parse should throw ParseException for unknown option with null handler");
    }

    /**
     * Tests the parse method with BiConsumer and only known options.
     * Verifies that handler is not called when all options are known.
     */
    @Test
    public void testParseWithBiConsumerAndOnlyKnownOptions(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration file with only known options
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
            writer.write("-dontshrink\n");
        }
        ConfigurationParser parser = new ConfigurationParser(configFile);
        Configuration config = new Configuration();

        // Track unknown options
        java.util.List<String> unknownOptions = new java.util.ArrayList<>();
        java.util.function.BiConsumer<String, String> handler = (option, arg) -> {
            unknownOptions.add(option);
        };

        // Act - Parse the configuration with handler
        parser.parse(config, handler);

        // Assert - Verify options are set and handler was not called
        assertTrue(config.verbose, "verbose should be true");
        assertFalse(config.shrink, "shrink should be false");
        assertTrue(unknownOptions.isEmpty(), "Handler should not have been called for known options");
    }

    /**
     * Tests the parse method with BiConsumer that captures option arguments.
     * Verifies that both option and its argument are passed to the handler.
     */
    @Test
    public void testParseWithBiConsumerCapturingArguments(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration file with unknown option with argument
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
            writer.write("-unknownoption someargument\n");
        }
        ConfigurationParser parser = new ConfigurationParser(configFile);
        Configuration config = new Configuration();

        // Track unknown options and their arguments
        java.util.Map<String, String> unknownOptionsMap = new java.util.HashMap<>();
        java.util.function.BiConsumer<String, String> handler = (option, arg) -> {
            unknownOptionsMap.put(option, arg);
        };

        // Act - Parse the configuration with handler
        parser.parse(config, handler);

        // Assert - Verify verbose is set and unknown option with argument was captured
        assertTrue(config.verbose, "verbose should be true");
        assertFalse(unknownOptionsMap.isEmpty(), "Unknown options should have been captured");
    }

    /**
     * Tests the parse method with BiConsumer and multiple unknown options.
     * Verifies that all unknown options are passed to the handler.
     */
    @Test
    public void testParseWithBiConsumerAndMultipleUnknownOptions(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration file with multiple unknown options
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
            writer.write("-unknownoption1\n");
            writer.write("-unknownoption2\n");
            writer.write("-dontshrink\n");
        }
        ConfigurationParser parser = new ConfigurationParser(configFile);
        Configuration config = new Configuration();

        // Track unknown options
        java.util.List<String> unknownOptions = new java.util.ArrayList<>();
        java.util.function.BiConsumer<String, String> handler = (option, arg) -> {
            unknownOptions.add(option);
        };

        // Act - Parse the configuration with handler
        parser.parse(config, handler);

        // Assert - Verify known options are set and multiple unknown options were captured
        assertTrue(config.verbose, "verbose should be true");
        assertFalse(config.shrink, "shrink should be false");
        assertTrue(unknownOptions.size() >= 1, "At least one unknown option should have been captured");
    }

    /**
     * Tests the parse method with BiConsumer and null configuration.
     * Verifies that a NullPointerException is thrown.
     */
    @Test
    public void testParseWithBiConsumerAndNullConfiguration(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration file
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
        }
        ConfigurationParser parser = new ConfigurationParser(configFile);

        java.util.function.BiConsumer<String, String> handler = (option, arg) -> {};

        // Act & Assert - Verify that NullPointerException is thrown
        assertThrows(NullPointerException.class, () -> {
            parser.parse(null, handler);
        }, "parse should throw NullPointerException for null configuration");
    }

    /**
     * Tests the parse method with BiConsumer that throws exception.
     * Verifies that exceptions from the handler propagate correctly.
     */
    @Test
    public void testParseWithBiConsumerThatThrowsException(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration file with unknown option
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-unknownoption\n");
        }
        ConfigurationParser parser = new ConfigurationParser(configFile);
        Configuration config = new Configuration();

        // Handler that throws exception
        java.util.function.BiConsumer<String, String> handler = (option, arg) -> {
            throw new RuntimeException("Handler exception");
        };

        // Act & Assert - Verify that the exception from handler propagates
        assertThrows(RuntimeException.class, () -> {
            parser.parse(config, handler);
        }, "parse should propagate exceptions from handler");
    }

    /**
     * Tests the parse method with BiConsumer and empty file.
     * Verifies that parsing empty file with handler works correctly.
     */
    @Test
    public void testParseWithBiConsumerAndEmptyFile(@TempDir Path tempDir) throws Exception {
        // Arrange - Create an empty configuration file
        File configFile = tempDir.resolve("empty-config.txt").toFile();
        configFile.createNewFile();
        ConfigurationParser parser = new ConfigurationParser(configFile);
        Configuration config = new Configuration();

        // Track unknown options
        java.util.List<String> unknownOptions = new java.util.ArrayList<>();
        java.util.function.BiConsumer<String, String> handler = (option, arg) -> {
            unknownOptions.add(option);
        };

        // Act - Parse the configuration with handler
        parser.parse(config, handler);

        // Assert - Verify configuration is valid and handler was not called
        assertNotNull(config, "Configuration should not be null");
        assertTrue(unknownOptions.isEmpty(), "Handler should not be called for empty file");
    }

    /**
     * Tests the parse method with BiConsumer and complex configuration.
     * Verifies that complex configurations with mix of known and unknown options work correctly.
     */
    @Test
    public void testParseWithBiConsumerAndComplexConfiguration(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a complex configuration file
        File configFile = tempDir.resolve("complex-config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("# Complex configuration\n");
            writer.write("-verbose\n");
            writer.write("-dontshrink\n");
            writer.write("-unknownoption1\n");
            writer.write("-dontoptimize\n");
            writer.write("-unknownoption2 arg\n");
        }
        ConfigurationParser parser = new ConfigurationParser(configFile);
        Configuration config = new Configuration();

        // Track unknown options
        java.util.List<String> unknownOptions = new java.util.ArrayList<>();
        java.util.function.BiConsumer<String, String> handler = (option, arg) -> {
            unknownOptions.add(option);
        };

        // Act - Parse the configuration with handler
        parser.parse(config, handler);

        // Assert - Verify known options are set and unknown options were captured
        assertTrue(config.verbose, "verbose should be true");
        assertFalse(config.shrink, "shrink should be false");
        assertFalse(config.optimize, "optimize should be false");
        assertTrue(unknownOptions.size() >= 1, "Unknown options should have been captured");
    }

    /**
     * Tests the parse method with BiConsumer using ArgumentWordReader.
     * Verifies that parsing from arguments with handler works correctly.
     */
    @Test
    public void testParseWithBiConsumerAndArgumentWordReader(@TempDir Path tempDir) throws Exception {
        // Arrange - Create an ArgumentWordReader with unknown option
        String[] args = {"-verbose", "-unknownoption", "-dontshrink"};
        WordReader reader = new ArgumentWordReader(args, tempDir.toFile());
        Properties properties = new Properties();
        ConfigurationParser parser = new ConfigurationParser(reader, properties);
        Configuration config = new Configuration();

        // Track unknown options
        java.util.List<String> unknownOptions = new java.util.ArrayList<>();
        java.util.function.BiConsumer<String, String> handler = (option, arg) -> {
            unknownOptions.add(option);
        };

        // Act - Parse the configuration with handler
        parser.parse(config, handler);

        // Assert - Verify options are set and unknown option was captured
        assertTrue(config.verbose, "verbose should be true");
        assertFalse(config.shrink, "shrink should be false");
        assertFalse(unknownOptions.isEmpty(), "Unknown option should have been captured");

        // Clean up
        reader.close();
    }

    /**
     * Tests the parse method called multiple times with same BiConsumer.
     * Verifies that the handler can be reused across multiple parse calls.
     */
    @Test
    public void testParseWithBiConsumerCalledMultipleTimes(@TempDir Path tempDir) throws Exception {
        // Arrange - Create two configuration files
        File configFile1 = tempDir.resolve("config1.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile1)) {
            writer.write("-verbose\n");
            writer.write("-unknownoption1\n");
        }
        File configFile2 = tempDir.resolve("config2.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile2)) {
            writer.write("-dontshrink\n");
            writer.write("-unknownoption2\n");
        }

        Configuration config = new Configuration();
        ConfigurationParser parser1 = new ConfigurationParser(configFile1);
        ConfigurationParser parser2 = new ConfigurationParser(configFile2);

        // Track unknown options
        java.util.List<String> unknownOptions = new java.util.ArrayList<>();
        java.util.function.BiConsumer<String, String> handler = (option, arg) -> {
            unknownOptions.add(option);
        };

        // Act - Parse both configurations with same handler
        parser1.parse(config, handler);
        parser2.parse(config, handler);

        // Assert - Verify options from both files are set and handler captured unknown options from both
        assertTrue(config.verbose, "verbose should be true");
        assertFalse(config.shrink, "shrink should be false");
        assertTrue(unknownOptions.size() >= 1, "Unknown options from both files should have been captured");
    }

    /**
     * Tests the parse method with BiConsumer and handler that logs options.
     * Verifies that handler receives correct option information.
     */
    @Test
    public void testParseWithBiConsumerLoggingHandler(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration file with unknown option
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
            writer.write("-customoption\n");
        }
        ConfigurationParser parser = new ConfigurationParser(configFile);
        Configuration config = new Configuration();

        // Handler that logs to a list
        java.util.List<String> log = new java.util.ArrayList<>();
        java.util.function.BiConsumer<String, String> handler = (option, arg) -> {
            log.add("Option: " + option + ", Arg: " + arg);
        };

        // Act - Parse the configuration with logging handler
        parser.parse(config, handler);

        // Assert - Verify verbose is set and handler logged the unknown option
        assertTrue(config.verbose, "verbose should be true");
        assertFalse(log.isEmpty(), "Handler should have logged unknown option");
    }
}
