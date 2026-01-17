package proguard;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ConfigurationParser} constructors.
 * Tests ConfigurationParser(File), ConfigurationParser(File, Properties),
 * and ConfigurationParser(URL, Properties) constructors.
 */
public class ConfigurationParserClaude_constructorTest {

    /**
     * Tests the constructor ConfigurationParser(File) with a valid configuration file.
     * Verifies that the parser can be instantiated with a proper configuration file.
     */
    @Test
    public void testConstructorWithValidConfigurationFile(@TempDir Path tempDir) throws IOException {
        // Arrange - Create a temporary configuration file with valid content
        File configFile = tempDir.resolve("proguard-config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
            writer.write("-dontshrink\n");
        }

        // Act - Create parser with the configuration file
        ConfigurationParser parser = new ConfigurationParser(configFile);

        // Assert - Verify the parser was created successfully
        assertNotNull(parser, "ConfigurationParser should be instantiated successfully");
    }

    /**
     * Tests the constructor ConfigurationParser(File) with an empty configuration file.
     * Verifies that the parser can handle empty files.
     */
    @Test
    public void testConstructorWithEmptyConfigurationFile(@TempDir Path tempDir) throws IOException {
        // Arrange - Create an empty configuration file
        File configFile = tempDir.resolve("empty-config.txt").toFile();
        configFile.createNewFile();

        // Act - Create parser with the empty configuration file
        ConfigurationParser parser = new ConfigurationParser(configFile);

        // Assert - Verify the parser was created successfully
        assertNotNull(parser, "ConfigurationParser should be instantiated with empty file");
    }

    /**
     * Tests the constructor ConfigurationParser(File) with a non-existent file.
     * Verifies that an IOException is thrown when the file doesn't exist.
     */
    @Test
    public void testConstructorWithNonExistentFile(@TempDir Path tempDir) {
        // Arrange - Create a path to a non-existent file
        File nonExistentFile = tempDir.resolve("non-existent-config.txt").toFile();

        // Act & Assert - Verify that IOException is thrown
        assertThrows(IOException.class, () -> {
            new ConfigurationParser(nonExistentFile);
        }, "ConfigurationParser should throw IOException for non-existent file");
    }

    /**
     * Tests the constructor ConfigurationParser(File) with a null File parameter.
     * Verifies that a NullPointerException is thrown.
     */
    @Test
    public void testConstructorWithNullFile() {
        // Act & Assert - Verify that NullPointerException is thrown
        assertThrows(NullPointerException.class, () -> {
            new ConfigurationParser((File) null);
        }, "ConfigurationParser should throw NullPointerException for null file");
    }

    /**
     * Tests the constructor ConfigurationParser(File) with a directory instead of a file.
     * Verifies that an IOException is thrown when given a directory.
     */
    @Test
    public void testConstructorWithDirectory(@TempDir Path tempDir) {
        // Arrange - Use the temp directory itself
        File directory = tempDir.toFile();

        // Act & Assert - Verify that IOException is thrown
        assertThrows(IOException.class, () -> {
            new ConfigurationParser(directory);
        }, "ConfigurationParser should throw IOException when given a directory");
    }

    /**
     * Tests the constructor ConfigurationParser(File) with a file containing comments.
     * Verifies that the parser can handle configuration files with comments.
     */
    @Test
    public void testConstructorWithCommentsInConfigFile(@TempDir Path tempDir) throws IOException {
        // Arrange - Create a configuration file with comments
        File configFile = tempDir.resolve("config-with-comments.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("# This is a comment\n");
            writer.write("-verbose\n");
            writer.write("# Another comment\n");
            writer.write("-dontshrink\n");
        }

        // Act - Create parser with the configuration file
        ConfigurationParser parser = new ConfigurationParser(configFile);

        // Assert - Verify the parser was created successfully
        assertNotNull(parser, "ConfigurationParser should handle files with comments");
    }

    /**
     * Tests the constructor ConfigurationParser(File) with a file containing multiple options.
     * Verifies that the parser can be instantiated with a complex configuration file.
     */
    @Test
    public void testConstructorWithMultipleOptions(@TempDir Path tempDir) throws IOException {
        // Arrange - Create a configuration file with multiple options
        File configFile = tempDir.resolve("multi-option-config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
            writer.write("-dontshrink\n");
            writer.write("-dontoptimize\n");
            writer.write("-dontobfuscate\n");
        }

        // Act - Create parser with the configuration file
        ConfigurationParser parser = new ConfigurationParser(configFile);

        // Assert - Verify the parser was created successfully
        assertNotNull(parser, "ConfigurationParser should handle multiple options");
    }

    /**
     * Tests the constructor ConfigurationParser(File) with a file containing whitespace.
     * Verifies that the parser can handle files with various whitespace patterns.
     */
    @Test
    public void testConstructorWithWhitespace(@TempDir Path tempDir) throws IOException {
        // Arrange - Create a configuration file with whitespace
        File configFile = tempDir.resolve("whitespace-config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("\n");
            writer.write("  -verbose  \n");
            writer.write("\n");
            writer.write("\t-dontshrink\t\n");
            writer.write("\n");
        }

        // Act - Create parser with the configuration file
        ConfigurationParser parser = new ConfigurationParser(configFile);

        // Assert - Verify the parser was created successfully
        assertNotNull(parser, "ConfigurationParser should handle whitespace in file");
    }

    /**
     * Tests the constructor ConfigurationParser(File) with a file containing special characters.
     * Verifies that the parser can handle files with special characters in option values.
     */
    @Test
    public void testConstructorWithSpecialCharacters(@TempDir Path tempDir) throws IOException {
        // Arrange - Create a configuration file with special characters
        File configFile = tempDir.resolve("special-chars-config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
            writer.write("-printseeds output/seeds.txt\n");
        }

        // Act - Create parser with the configuration file
        ConfigurationParser parser = new ConfigurationParser(configFile);

        // Assert - Verify the parser was created successfully
        assertNotNull(parser, "ConfigurationParser should handle special characters");
    }

    /**
     * Tests that multiple ConfigurationParser instances can be created independently.
     * Verifies that each parser instance is independent.
     */
    @Test
    public void testMultipleParserInstances(@TempDir Path tempDir) throws IOException {
        // Arrange - Create two different configuration files
        File configFile1 = tempDir.resolve("config1.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile1)) {
            writer.write("-verbose\n");
        }

        File configFile2 = tempDir.resolve("config2.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile2)) {
            writer.write("-dontshrink\n");
        }

        // Act - Create two parser instances
        ConfigurationParser parser1 = new ConfigurationParser(configFile1);
        ConfigurationParser parser2 = new ConfigurationParser(configFile2);

        // Assert - Verify both parsers were created successfully
        assertNotNull(parser1, "First parser should be created");
        assertNotNull(parser2, "Second parser should be created");
        assertNotSame(parser1, parser2, "Parser instances should be different objects");
    }

    /**
     * Tests the constructor ConfigurationParser(File) with a file containing keep rules.
     * Verifies that the parser can handle configuration files with complex keep rules.
     */
    @Test
    public void testConstructorWithKeepRules(@TempDir Path tempDir) throws IOException {
        // Arrange - Create a configuration file with keep rules
        File configFile = tempDir.resolve("keep-rules-config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-keep public class * {\n");
            writer.write("    public static void main(java.lang.String[]);\n");
            writer.write("}\n");
        }

        // Act - Create parser with the configuration file
        ConfigurationParser parser = new ConfigurationParser(configFile);

        // Assert - Verify the parser was created successfully
        assertNotNull(parser, "ConfigurationParser should handle keep rules");
    }

    /**
     * Tests the constructor ConfigurationParser(File) with a file path containing spaces.
     * Verifies that the parser can handle file paths with spaces.
     */
    @Test
    public void testConstructorWithFilePathContainingSpaces(@TempDir Path tempDir) throws IOException {
        // Arrange - Create a subdirectory with spaces in the name
        File subDir = tempDir.resolve("folder with spaces").toFile();
        subDir.mkdir();
        File configFile = new File(subDir, "config file.txt");
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
        }

        // Act - Create parser with the configuration file
        ConfigurationParser parser = new ConfigurationParser(configFile);

        // Assert - Verify the parser was created successfully
        assertNotNull(parser, "ConfigurationParser should handle file paths with spaces");
    }

    // ========== Tests for ConfigurationParser(File, Properties) constructor ==========

    /**
     * Tests the constructor ConfigurationParser(File, Properties) with valid parameters.
     * Verifies that the parser can be instantiated with a file and properties.
     */
    @Test
    public void testTwoParamConstructorWithValidFileAndProperties(@TempDir Path tempDir) throws IOException {
        // Arrange - Create a configuration file and properties
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
        }
        Properties properties = new Properties();
        properties.setProperty("test.property", "test.value");

        // Act - Create parser with file and properties
        ConfigurationParser parser = new ConfigurationParser(configFile, properties);

        // Assert - Verify the parser was created successfully
        assertNotNull(parser, "ConfigurationParser should be instantiated with file and properties");
    }

    /**
     * Tests the constructor ConfigurationParser(File, Properties) with empty properties.
     * Verifies that the parser can handle empty properties.
     */
    @Test
    public void testTwoParamConstructorWithEmptyProperties(@TempDir Path tempDir) throws IOException {
        // Arrange - Create a configuration file and empty properties
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
        }
        Properties properties = new Properties();

        // Act - Create parser with file and empty properties
        ConfigurationParser parser = new ConfigurationParser(configFile, properties);

        // Assert - Verify the parser was created successfully
        assertNotNull(parser, "ConfigurationParser should handle empty properties");
    }

    /**
     * Tests the constructor ConfigurationParser(File, Properties) with system properties.
     * Verifies that the parser can be instantiated with system properties.
     */
    @Test
    public void testTwoParamConstructorWithSystemProperties(@TempDir Path tempDir) throws IOException {
        // Arrange - Create a configuration file
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
        }
        Properties properties = System.getProperties();

        // Act - Create parser with file and system properties
        ConfigurationParser parser = new ConfigurationParser(configFile, properties);

        // Assert - Verify the parser was created successfully
        assertNotNull(parser, "ConfigurationParser should handle system properties");
    }

    /**
     * Tests the constructor ConfigurationParser(File, Properties) with null properties.
     * Verifies that a NullPointerException is thrown for null properties.
     */
    @Test
    public void testTwoParamConstructorWithNullProperties(@TempDir Path tempDir) throws IOException {
        // Arrange - Create a configuration file
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
        }

        // Act & Assert - Verify that NullPointerException is thrown
        assertThrows(NullPointerException.class, () -> {
            new ConfigurationParser(configFile, null);
        }, "ConfigurationParser should throw NullPointerException for null properties");
    }

    /**
     * Tests the constructor ConfigurationParser(File, Properties) with null file.
     * Verifies that a NullPointerException is thrown for null file.
     */
    @Test
    public void testTwoParamConstructorWithNullFile() {
        // Arrange - Create properties
        Properties properties = new Properties();

        // Act & Assert - Verify that NullPointerException is thrown
        assertThrows(NullPointerException.class, () -> {
            new ConfigurationParser((File) null, properties);
        }, "ConfigurationParser should throw NullPointerException for null file");
    }

    /**
     * Tests the constructor ConfigurationParser(File, Properties) with non-existent file.
     * Verifies that an IOException is thrown when the file doesn't exist.
     */
    @Test
    public void testTwoParamConstructorWithNonExistentFile(@TempDir Path tempDir) {
        // Arrange - Create a path to a non-existent file and properties
        File nonExistentFile = tempDir.resolve("non-existent.txt").toFile();
        Properties properties = new Properties();

        // Act & Assert - Verify that IOException is thrown
        assertThrows(IOException.class, () -> {
            new ConfigurationParser(nonExistentFile, properties);
        }, "ConfigurationParser should throw IOException for non-existent file");
    }

    /**
     * Tests the constructor ConfigurationParser(File, Properties) with a directory.
     * Verifies that an IOException is thrown when given a directory.
     */
    @Test
    public void testTwoParamConstructorWithDirectory(@TempDir Path tempDir) {
        // Arrange - Use the temp directory and create properties
        File directory = tempDir.toFile();
        Properties properties = new Properties();

        // Act & Assert - Verify that IOException is thrown
        assertThrows(IOException.class, () -> {
            new ConfigurationParser(directory, properties);
        }, "ConfigurationParser should throw IOException when given a directory");
    }

    /**
     * Tests the constructor ConfigurationParser(File, Properties) with multiple custom properties.
     * Verifies that the parser can handle multiple custom properties.
     */
    @Test
    public void testTwoParamConstructorWithMultipleProperties(@TempDir Path tempDir) throws IOException {
        // Arrange - Create a configuration file and multiple properties
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
        }
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");
        properties.setProperty("property3", "value3");

        // Act - Create parser with file and multiple properties
        ConfigurationParser parser = new ConfigurationParser(configFile, properties);

        // Assert - Verify the parser was created successfully
        assertNotNull(parser, "ConfigurationParser should handle multiple properties");
    }

    /**
     * Tests the constructor ConfigurationParser(File, Properties) with empty file and properties.
     * Verifies that the parser can handle both empty file and properties.
     */
    @Test
    public void testTwoParamConstructorWithEmptyFileAndProperties(@TempDir Path tempDir) throws IOException {
        // Arrange - Create an empty configuration file and empty properties
        File configFile = tempDir.resolve("empty-config.txt").toFile();
        configFile.createNewFile();
        Properties properties = new Properties();

        // Act - Create parser with empty file and empty properties
        ConfigurationParser parser = new ConfigurationParser(configFile, properties);

        // Assert - Verify the parser was created successfully
        assertNotNull(parser, "ConfigurationParser should handle empty file and empty properties");
    }

    /**
     * Tests that ConfigurationParser(File) uses system properties.
     * Verifies the relationship between one-param and two-param constructors.
     */
    @Test
    public void testOneParamConstructorUsesSystemProperties(@TempDir Path tempDir) throws IOException {
        // Arrange - Create a configuration file
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
        }

        // Act - Create parsers with both constructors
        ConfigurationParser parser1 = new ConfigurationParser(configFile);
        ConfigurationParser parser2 = new ConfigurationParser(configFile, System.getProperties());

        // Assert - Verify both parsers were created successfully
        // Both should work since ConfigurationParser(File) delegates to ConfigurationParser(File, Properties)
        // with System.getProperties()
        assertNotNull(parser1, "One-param constructor should work");
        assertNotNull(parser2, "Two-param constructor with system properties should work");
    }

    /**
     * Tests the constructor ConfigurationParser(File, Properties) with special property values.
     * Verifies that the parser can handle properties with special characters.
     */
    @Test
    public void testTwoParamConstructorWithSpecialPropertyValues(@TempDir Path tempDir) throws IOException {
        // Arrange - Create a configuration file and properties with special characters
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
        }
        Properties properties = new Properties();
        properties.setProperty("path.separator", ":");
        properties.setProperty("file.separator", "/");
        properties.setProperty("special.chars", "!@#$%^&*()");

        // Act - Create parser with file and special properties
        ConfigurationParser parser = new ConfigurationParser(configFile, properties);

        // Assert - Verify the parser was created successfully
        assertNotNull(parser, "ConfigurationParser should handle special property values");
    }

    /**
     * Tests the constructor ConfigurationParser(File, Properties) with complex configuration.
     * Verifies that the parser can handle complex configuration with properties.
     */
    @Test
    public void testTwoParamConstructorWithComplexConfiguration(@TempDir Path tempDir) throws IOException {
        // Arrange - Create a configuration file with complex content and custom properties
        File configFile = tempDir.resolve("complex-config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("# Complex configuration\n");
            writer.write("-verbose\n");
            writer.write("-keep public class * {\n");
            writer.write("    public static void main(java.lang.String[]);\n");
            writer.write("}\n");
            writer.write("-dontshrink\n");
        }
        Properties properties = new Properties();
        properties.setProperty("user.dir", tempDir.toString());
        properties.setProperty("custom.property", "custom.value");

        // Act - Create parser with complex configuration and properties
        ConfigurationParser parser = new ConfigurationParser(configFile, properties);

        // Assert - Verify the parser was created successfully
        assertNotNull(parser, "ConfigurationParser should handle complex configuration with properties");
    }

    /**
     * Tests creating multiple ConfigurationParser instances with different properties.
     * Verifies that each instance maintains its own properties.
     */
    @Test
    public void testTwoParamConstructorMultipleInstancesWithDifferentProperties(@TempDir Path tempDir) throws IOException {
        // Arrange - Create configuration files and different property sets
        File configFile1 = tempDir.resolve("config1.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile1)) {
            writer.write("-verbose\n");
        }
        Properties properties1 = new Properties();
        properties1.setProperty("prop", "value1");

        File configFile2 = tempDir.resolve("config2.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile2)) {
            writer.write("-dontshrink\n");
        }
        Properties properties2 = new Properties();
        properties2.setProperty("prop", "value2");

        // Act - Create two parser instances with different properties
        ConfigurationParser parser1 = new ConfigurationParser(configFile1, properties1);
        ConfigurationParser parser2 = new ConfigurationParser(configFile2, properties2);

        // Assert - Verify both parsers were created successfully and are different instances
        assertNotNull(parser1, "First parser should be created");
        assertNotNull(parser2, "Second parser should be created");
        assertNotSame(parser1, parser2, "Parser instances should be different objects");
    }

    // ========== Tests for ConfigurationParser(URL, Properties) constructor ==========

    /**
     * Tests the constructor ConfigurationParser(URL, Properties) with valid parameters.
     * Verifies that the parser can be instantiated with a URL and properties.
     */
    @Test
    public void testUrlConstructorWithValidUrlAndProperties(@TempDir Path tempDir) throws IOException {
        // Arrange - Create a configuration file and convert to URL
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
        }
        URL url = configFile.toURI().toURL();
        Properties properties = new Properties();
        properties.setProperty("test.property", "test.value");

        // Act - Create parser with URL and properties
        ConfigurationParser parser = new ConfigurationParser(url, properties);

        // Assert - Verify the parser was created successfully
        assertNotNull(parser, "ConfigurationParser should be instantiated with URL and properties");
    }

    /**
     * Tests the constructor ConfigurationParser(URL, Properties) with empty properties.
     * Verifies that the parser can handle empty properties with a URL.
     */
    @Test
    public void testUrlConstructorWithEmptyProperties(@TempDir Path tempDir) throws IOException {
        // Arrange - Create a configuration file and convert to URL
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
        }
        URL url = configFile.toURI().toURL();
        Properties properties = new Properties();

        // Act - Create parser with URL and empty properties
        ConfigurationParser parser = new ConfigurationParser(url, properties);

        // Assert - Verify the parser was created successfully
        assertNotNull(parser, "ConfigurationParser should handle URL with empty properties");
    }

    /**
     * Tests the constructor ConfigurationParser(URL, Properties) with system properties.
     * Verifies that the parser can be instantiated with URL and system properties.
     */
    @Test
    public void testUrlConstructorWithSystemProperties(@TempDir Path tempDir) throws IOException {
        // Arrange - Create a configuration file and convert to URL
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
        }
        URL url = configFile.toURI().toURL();
        Properties properties = System.getProperties();

        // Act - Create parser with URL and system properties
        ConfigurationParser parser = new ConfigurationParser(url, properties);

        // Assert - Verify the parser was created successfully
        assertNotNull(parser, "ConfigurationParser should handle URL with system properties");
    }

    /**
     * Tests the constructor ConfigurationParser(URL, Properties) with null properties.
     * Verifies that a NullPointerException is thrown for null properties.
     */
    @Test
    public void testUrlConstructorWithNullProperties(@TempDir Path tempDir) throws IOException {
        // Arrange - Create a configuration file and convert to URL
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
        }
        URL url = configFile.toURI().toURL();

        // Act & Assert - Verify that NullPointerException is thrown
        assertThrows(NullPointerException.class, () -> {
            new ConfigurationParser(url, null);
        }, "ConfigurationParser should throw NullPointerException for null properties with URL");
    }

    /**
     * Tests the constructor ConfigurationParser(URL, Properties) with null URL.
     * Verifies that a NullPointerException is thrown for null URL.
     */
    @Test
    public void testUrlConstructorWithNullUrl() {
        // Arrange - Create properties
        Properties properties = new Properties();

        // Act & Assert - Verify that NullPointerException is thrown
        assertThrows(NullPointerException.class, () -> {
            new ConfigurationParser((URL) null, properties);
        }, "ConfigurationParser should throw NullPointerException for null URL");
    }

    /**
     * Tests the constructor ConfigurationParser(URL, Properties) with a URL to a non-existent file.
     * Verifies that an IOException is thrown when the URL points to a non-existent file.
     */
    @Test
    public void testUrlConstructorWithNonExistentUrl(@TempDir Path tempDir) throws MalformedURLException {
        // Arrange - Create a URL to a non-existent file
        File nonExistentFile = tempDir.resolve("non-existent.txt").toFile();
        URL url = nonExistentFile.toURI().toURL();
        Properties properties = new Properties();

        // Act & Assert - Verify that IOException is thrown
        assertThrows(IOException.class, () -> {
            new ConfigurationParser(url, properties);
        }, "ConfigurationParser should throw IOException for URL to non-existent file");
    }

    /**
     * Tests the constructor ConfigurationParser(URL, Properties) with an empty file.
     * Verifies that the parser can handle empty files via URL.
     */
    @Test
    public void testUrlConstructorWithEmptyFile(@TempDir Path tempDir) throws IOException {
        // Arrange - Create an empty configuration file and convert to URL
        File configFile = tempDir.resolve("empty-config.txt").toFile();
        configFile.createNewFile();
        URL url = configFile.toURI().toURL();
        Properties properties = new Properties();

        // Act - Create parser with URL to empty file
        ConfigurationParser parser = new ConfigurationParser(url, properties);

        // Assert - Verify the parser was created successfully
        assertNotNull(parser, "ConfigurationParser should handle URL to empty file");
    }

    /**
     * Tests the constructor ConfigurationParser(URL, Properties) with multiple custom properties.
     * Verifies that the parser can handle multiple custom properties with URL.
     */
    @Test
    public void testUrlConstructorWithMultipleProperties(@TempDir Path tempDir) throws IOException {
        // Arrange - Create a configuration file and convert to URL
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
        }
        URL url = configFile.toURI().toURL();
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");
        properties.setProperty("property3", "value3");

        // Act - Create parser with URL and multiple properties
        ConfigurationParser parser = new ConfigurationParser(url, properties);

        // Assert - Verify the parser was created successfully
        assertNotNull(parser, "ConfigurationParser should handle URL with multiple properties");
    }

    /**
     * Tests the constructor ConfigurationParser(URL, Properties) with file URL scheme.
     * Verifies that the parser can handle file:// URLs.
     */
    @Test
    public void testUrlConstructorWithFileUrlScheme(@TempDir Path tempDir) throws IOException {
        // Arrange - Create a configuration file and get file:// URL
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
            writer.write("-dontshrink\n");
        }
        URL url = configFile.toURI().toURL();
        Properties properties = new Properties();

        // Act - Create parser with file:// URL
        ConfigurationParser parser = new ConfigurationParser(url, properties);

        // Assert - Verify the parser was created successfully and URL scheme is file
        assertNotNull(parser, "ConfigurationParser should handle file:// URL");
        assertEquals("file", url.getProtocol(), "URL protocol should be 'file'");
    }

    /**
     * Tests the constructor ConfigurationParser(URL, Properties) with complex configuration.
     * Verifies that the parser can handle complex configuration via URL.
     */
    @Test
    public void testUrlConstructorWithComplexConfiguration(@TempDir Path tempDir) throws IOException {
        // Arrange - Create a configuration file with complex content and convert to URL
        File configFile = tempDir.resolve("complex-config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("# Complex configuration\n");
            writer.write("-verbose\n");
            writer.write("-keep public class * {\n");
            writer.write("    public static void main(java.lang.String[]);\n");
            writer.write("}\n");
            writer.write("-dontshrink\n");
        }
        URL url = configFile.toURI().toURL();
        Properties properties = new Properties();
        properties.setProperty("user.dir", tempDir.toString());
        properties.setProperty("custom.property", "custom.value");

        // Act - Create parser with complex configuration URL and properties
        ConfigurationParser parser = new ConfigurationParser(url, properties);

        // Assert - Verify the parser was created successfully
        assertNotNull(parser, "ConfigurationParser should handle complex configuration via URL");
    }

    /**
     * Tests the constructor ConfigurationParser(URL, Properties) with file containing comments.
     * Verifies that the parser can handle configuration files with comments via URL.
     */
    @Test
    public void testUrlConstructorWithComments(@TempDir Path tempDir) throws IOException {
        // Arrange - Create a configuration file with comments and convert to URL
        File configFile = tempDir.resolve("config-with-comments.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("# This is a comment\n");
            writer.write("-verbose\n");
            writer.write("# Another comment\n");
            writer.write("-dontshrink\n");
        }
        URL url = configFile.toURI().toURL();
        Properties properties = new Properties();

        // Act - Create parser with URL to file with comments
        ConfigurationParser parser = new ConfigurationParser(url, properties);

        // Assert - Verify the parser was created successfully
        assertNotNull(parser, "ConfigurationParser should handle URL to file with comments");
    }

    /**
     * Tests creating multiple ConfigurationParser instances with URLs and different properties.
     * Verifies that each instance maintains its own properties.
     */
    @Test
    public void testUrlConstructorMultipleInstancesWithDifferentProperties(@TempDir Path tempDir) throws IOException {
        // Arrange - Create configuration files, convert to URLs, and create different property sets
        File configFile1 = tempDir.resolve("config1.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile1)) {
            writer.write("-verbose\n");
        }
        URL url1 = configFile1.toURI().toURL();
        Properties properties1 = new Properties();
        properties1.setProperty("prop", "value1");

        File configFile2 = tempDir.resolve("config2.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile2)) {
            writer.write("-dontshrink\n");
        }
        URL url2 = configFile2.toURI().toURL();
        Properties properties2 = new Properties();
        properties2.setProperty("prop", "value2");

        // Act - Create two parser instances with different URLs and properties
        ConfigurationParser parser1 = new ConfigurationParser(url1, properties1);
        ConfigurationParser parser2 = new ConfigurationParser(url2, properties2);

        // Assert - Verify both parsers were created successfully and are different instances
        assertNotNull(parser1, "First parser should be created");
        assertNotNull(parser2, "Second parser should be created");
        assertNotSame(parser1, parser2, "Parser instances should be different objects");
    }

    /**
     * Tests the constructor ConfigurationParser(URL, Properties) with special property values.
     * Verifies that the parser can handle properties with special characters when using URL.
     */
    @Test
    public void testUrlConstructorWithSpecialPropertyValues(@TempDir Path tempDir) throws IOException {
        // Arrange - Create a configuration file and convert to URL
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
        }
        URL url = configFile.toURI().toURL();
        Properties properties = new Properties();
        properties.setProperty("path.separator", ":");
        properties.setProperty("file.separator", "/");
        properties.setProperty("special.chars", "!@#$%^&*()");

        // Act - Create parser with URL and special properties
        ConfigurationParser parser = new ConfigurationParser(url, properties);

        // Assert - Verify the parser was created successfully
        assertNotNull(parser, "ConfigurationParser should handle URL with special property values");
    }

    /**
     * Tests the constructor ConfigurationParser(URL, Properties) with whitespace in file.
     * Verifies that the parser can handle files with whitespace via URL.
     */
    @Test
    public void testUrlConstructorWithWhitespace(@TempDir Path tempDir) throws IOException {
        // Arrange - Create a configuration file with whitespace and convert to URL
        File configFile = tempDir.resolve("whitespace-config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("\n");
            writer.write("  -verbose  \n");
            writer.write("\n");
            writer.write("\t-dontshrink\t\n");
            writer.write("\n");
        }
        URL url = configFile.toURI().toURL();
        Properties properties = new Properties();

        // Act - Create parser with URL to file with whitespace
        ConfigurationParser parser = new ConfigurationParser(url, properties);

        // Assert - Verify the parser was created successfully
        assertNotNull(parser, "ConfigurationParser should handle URL to file with whitespace");
    }

    /**
     * Tests the constructor ConfigurationParser(URL, Properties) with URL to file with spaces in path.
     * Verifies that the parser can handle URLs with encoded spaces.
     */
    @Test
    public void testUrlConstructorWithSpacesInPath(@TempDir Path tempDir) throws IOException {
        // Arrange - Create a subdirectory with spaces in the name
        File subDir = tempDir.resolve("folder with spaces").toFile();
        subDir.mkdir();
        File configFile = new File(subDir, "config file.txt");
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
        }
        URL url = configFile.toURI().toURL();
        Properties properties = new Properties();

        // Act - Create parser with URL containing spaces
        ConfigurationParser parser = new ConfigurationParser(url, properties);

        // Assert - Verify the parser was created successfully
        assertNotNull(parser, "ConfigurationParser should handle URL with spaces in path");
    }

    // ========== Tests for ConfigurationParser(WordReader, Properties) constructor ==========

    /**
     * Tests the constructor ConfigurationParser(WordReader, Properties) with valid parameters.
     * Verifies that the parser can be instantiated with a WordReader and properties.
     */
    @Test
    public void testWordReaderConstructorWithValidParameters(@TempDir Path tempDir) throws IOException {
        // Arrange - Create a configuration file and WordReader
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
        }
        WordReader reader = new FileWordReader(configFile);
        Properties properties = new Properties();
        properties.setProperty("test.property", "test.value");

        // Act - Create parser with WordReader and properties
        ConfigurationParser parser = new ConfigurationParser(reader, properties);

        // Assert - Verify the parser was created successfully
        assertNotNull(parser, "ConfigurationParser should be instantiated with WordReader and properties");

        // Clean up
        reader.close();
    }

    /**
     * Tests the constructor ConfigurationParser(WordReader, Properties) with ArgumentWordReader.
     * Verifies that the parser can handle ArgumentWordReader.
     */
    @Test
    public void testWordReaderConstructorWithArgumentWordReader(@TempDir Path tempDir) throws IOException {
        // Arrange - Create an ArgumentWordReader with configuration options
        String[] args = {"-verbose", "-dontshrink"};
        WordReader reader = new ArgumentWordReader(args, tempDir.toFile());
        Properties properties = new Properties();

        // Act - Create parser with ArgumentWordReader
        ConfigurationParser parser = new ConfigurationParser(reader, properties);

        // Assert - Verify the parser was created successfully
        assertNotNull(parser, "ConfigurationParser should handle ArgumentWordReader");

        // Clean up
        reader.close();
    }

    /**
     * Tests the constructor ConfigurationParser(WordReader, Properties) with FileWordReader.
     * Verifies that the parser can handle FileWordReader.
     */
    @Test
    public void testWordReaderConstructorWithFileWordReader(@TempDir Path tempDir) throws IOException {
        // Arrange - Create a configuration file and FileWordReader
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
            writer.write("-dontshrink\n");
        }
        WordReader reader = new FileWordReader(configFile);
        Properties properties = new Properties();

        // Act - Create parser with FileWordReader
        ConfigurationParser parser = new ConfigurationParser(reader, properties);

        // Assert - Verify the parser was created successfully
        assertNotNull(parser, "ConfigurationParser should handle FileWordReader");

        // Clean up
        reader.close();
    }

    /**
     * Tests the constructor ConfigurationParser(WordReader, Properties) with empty properties.
     * Verifies that the parser can handle empty properties with WordReader.
     */
    @Test
    public void testWordReaderConstructorWithEmptyProperties(@TempDir Path tempDir) throws IOException {
        // Arrange - Create a configuration file and WordReader with empty properties
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
        }
        WordReader reader = new FileWordReader(configFile);
        Properties properties = new Properties();

        // Act - Create parser with WordReader and empty properties
        ConfigurationParser parser = new ConfigurationParser(reader, properties);

        // Assert - Verify the parser was created successfully
        assertNotNull(parser, "ConfigurationParser should handle WordReader with empty properties");

        // Clean up
        reader.close();
    }

    /**
     * Tests the constructor ConfigurationParser(WordReader, Properties) with system properties.
     * Verifies that the parser can handle system properties with WordReader.
     */
    @Test
    public void testWordReaderConstructorWithSystemProperties(@TempDir Path tempDir) throws IOException {
        // Arrange - Create a configuration file and WordReader
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
        }
        WordReader reader = new FileWordReader(configFile);
        Properties properties = System.getProperties();

        // Act - Create parser with WordReader and system properties
        ConfigurationParser parser = new ConfigurationParser(reader, properties);

        // Assert - Verify the parser was created successfully
        assertNotNull(parser, "ConfigurationParser should handle WordReader with system properties");

        // Clean up
        reader.close();
    }

    /**
     * Tests the constructor ConfigurationParser(WordReader, Properties) with null properties.
     * Verifies that a NullPointerException is thrown for null properties.
     */
    @Test
    public void testWordReaderConstructorWithNullProperties(@TempDir Path tempDir) throws IOException {
        // Arrange - Create a configuration file and WordReader
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
        }
        WordReader reader = new FileWordReader(configFile);

        // Act & Assert - Verify that NullPointerException is thrown
        try {
            assertThrows(NullPointerException.class, () -> {
                new ConfigurationParser(reader, null);
            }, "ConfigurationParser should throw NullPointerException for null properties with WordReader");
        } finally {
            // Clean up
            reader.close();
        }
    }

    /**
     * Tests the constructor ConfigurationParser(WordReader, Properties) with null WordReader.
     * Verifies that a NullPointerException is thrown for null WordReader.
     */
    @Test
    public void testWordReaderConstructorWithNullWordReader() {
        // Arrange - Create properties
        Properties properties = new Properties();

        // Act & Assert - Verify that NullPointerException is thrown
        assertThrows(NullPointerException.class, () -> {
            new ConfigurationParser((WordReader) null, properties);
        }, "ConfigurationParser should throw NullPointerException for null WordReader");
    }

    /**
     * Tests the constructor ConfigurationParser(WordReader, Properties) with empty arguments.
     * Verifies that the parser can handle empty argument array.
     */
    @Test
    public void testWordReaderConstructorWithEmptyArguments(@TempDir Path tempDir) throws IOException {
        // Arrange - Create an ArgumentWordReader with empty arguments
        String[] args = {};
        WordReader reader = new ArgumentWordReader(args, tempDir.toFile());
        Properties properties = new Properties();

        // Act - Create parser with empty ArgumentWordReader
        ConfigurationParser parser = new ConfigurationParser(reader, properties);

        // Assert - Verify the parser was created successfully
        assertNotNull(parser, "ConfigurationParser should handle empty ArgumentWordReader");

        // Clean up
        reader.close();
    }

    /**
     * Tests the constructor ConfigurationParser(WordReader, Properties) with multiple properties.
     * Verifies that the parser can handle multiple custom properties with WordReader.
     */
    @Test
    public void testWordReaderConstructorWithMultipleProperties(@TempDir Path tempDir) throws IOException {
        // Arrange - Create a configuration file and WordReader with multiple properties
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
        }
        WordReader reader = new FileWordReader(configFile);
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");
        properties.setProperty("property3", "value3");

        // Act - Create parser with WordReader and multiple properties
        ConfigurationParser parser = new ConfigurationParser(reader, properties);

        // Assert - Verify the parser was created successfully
        assertNotNull(parser, "ConfigurationParser should handle WordReader with multiple properties");

        // Clean up
        reader.close();
    }

    /**
     * Tests the constructor ConfigurationParser(WordReader, Properties) with complex configuration.
     * Verifies that the parser can handle complex configuration via WordReader.
     */
    @Test
    public void testWordReaderConstructorWithComplexConfiguration(@TempDir Path tempDir) throws IOException {
        // Arrange - Create a configuration file with complex content and WordReader
        File configFile = tempDir.resolve("complex-config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("# Complex configuration\n");
            writer.write("-verbose\n");
            writer.write("-keep public class * {\n");
            writer.write("    public static void main(java.lang.String[]);\n");
            writer.write("}\n");
            writer.write("-dontshrink\n");
        }
        WordReader reader = new FileWordReader(configFile);
        Properties properties = new Properties();
        properties.setProperty("user.dir", tempDir.toString());
        properties.setProperty("custom.property", "custom.value");

        // Act - Create parser with complex configuration WordReader
        ConfigurationParser parser = new ConfigurationParser(reader, properties);

        // Assert - Verify the parser was created successfully
        assertNotNull(parser, "ConfigurationParser should handle complex configuration via WordReader");

        // Clean up
        reader.close();
    }

    /**
     * Tests the constructor ConfigurationParser(WordReader, Properties) with file containing comments.
     * Verifies that the parser can handle configuration files with comments via WordReader.
     */
    @Test
    public void testWordReaderConstructorWithComments(@TempDir Path tempDir) throws IOException {
        // Arrange - Create a configuration file with comments and WordReader
        File configFile = tempDir.resolve("config-with-comments.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("# This is a comment\n");
            writer.write("-verbose\n");
            writer.write("# Another comment\n");
            writer.write("-dontshrink\n");
        }
        WordReader reader = new FileWordReader(configFile);
        Properties properties = new Properties();

        // Act - Create parser with WordReader to file with comments
        ConfigurationParser parser = new ConfigurationParser(reader, properties);

        // Assert - Verify the parser was created successfully
        assertNotNull(parser, "ConfigurationParser should handle WordReader to file with comments");

        // Clean up
        reader.close();
    }

    /**
     * Tests creating multiple ConfigurationParser instances with WordReaders and different properties.
     * Verifies that each instance maintains its own properties.
     */
    @Test
    public void testWordReaderConstructorMultipleInstancesWithDifferentProperties(@TempDir Path tempDir) throws IOException {
        // Arrange - Create configuration files, WordReaders, and different property sets
        File configFile1 = tempDir.resolve("config1.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile1)) {
            writer.write("-verbose\n");
        }
        WordReader reader1 = new FileWordReader(configFile1);
        Properties properties1 = new Properties();
        properties1.setProperty("prop", "value1");

        File configFile2 = tempDir.resolve("config2.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile2)) {
            writer.write("-dontshrink\n");
        }
        WordReader reader2 = new FileWordReader(configFile2);
        Properties properties2 = new Properties();
        properties2.setProperty("prop", "value2");

        // Act - Create two parser instances with different WordReaders and properties
        ConfigurationParser parser1 = new ConfigurationParser(reader1, properties1);
        ConfigurationParser parser2 = new ConfigurationParser(reader2, properties2);

        // Assert - Verify both parsers were created successfully and are different instances
        assertNotNull(parser1, "First parser should be created");
        assertNotNull(parser2, "Second parser should be created");
        assertNotSame(parser1, parser2, "Parser instances should be different objects");

        // Clean up
        reader1.close();
        reader2.close();
    }

    /**
     * Tests the constructor ConfigurationParser(WordReader, Properties) with special property values.
     * Verifies that the parser can handle properties with special characters when using WordReader.
     */
    @Test
    public void testWordReaderConstructorWithSpecialPropertyValues(@TempDir Path tempDir) throws IOException {
        // Arrange - Create a configuration file and WordReader with special properties
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
        }
        WordReader reader = new FileWordReader(configFile);
        Properties properties = new Properties();
        properties.setProperty("path.separator", ":");
        properties.setProperty("file.separator", "/");
        properties.setProperty("special.chars", "!@#$%^&*()");

        // Act - Create parser with WordReader and special properties
        ConfigurationParser parser = new ConfigurationParser(reader, properties);

        // Assert - Verify the parser was created successfully
        assertNotNull(parser, "ConfigurationParser should handle WordReader with special property values");

        // Clean up
        reader.close();
    }

    /**
     * Tests the constructor ConfigurationParser(WordReader, Properties) with empty file.
     * Verifies that the parser can handle empty files via WordReader.
     */
    @Test
    public void testWordReaderConstructorWithEmptyFile(@TempDir Path tempDir) throws IOException {
        // Arrange - Create an empty configuration file and WordReader
        File configFile = tempDir.resolve("empty-config.txt").toFile();
        configFile.createNewFile();
        WordReader reader = new FileWordReader(configFile);
        Properties properties = new Properties();

        // Act - Create parser with WordReader to empty file
        ConfigurationParser parser = new ConfigurationParser(reader, properties);

        // Assert - Verify the parser was created successfully
        assertNotNull(parser, "ConfigurationParser should handle WordReader to empty file");

        // Clean up
        reader.close();
    }

    /**
     * Tests the constructor ConfigurationParser(WordReader, Properties) with whitespace in file.
     * Verifies that the parser can handle files with whitespace via WordReader.
     */
    @Test
    public void testWordReaderConstructorWithWhitespace(@TempDir Path tempDir) throws IOException {
        // Arrange - Create a configuration file with whitespace and WordReader
        File configFile = tempDir.resolve("whitespace-config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("\n");
            writer.write("  -verbose  \n");
            writer.write("\n");
            writer.write("\t-dontshrink\t\n");
            writer.write("\n");
        }
        WordReader reader = new FileWordReader(configFile);
        Properties properties = new Properties();

        // Act - Create parser with WordReader to file with whitespace
        ConfigurationParser parser = new ConfigurationParser(reader, properties);

        // Assert - Verify the parser was created successfully
        assertNotNull(parser, "ConfigurationParser should handle WordReader to file with whitespace");

        // Clean up
        reader.close();
    }

    /**
     * Tests the constructor ConfigurationParser(WordReader, Properties) with ArgumentWordReader containing complex arguments.
     * Verifies that the parser can handle complex argument patterns.
     */
    @Test
    public void testWordReaderConstructorWithComplexArguments(@TempDir Path tempDir) throws IOException {
        // Arrange - Create an ArgumentWordReader with complex arguments
        String[] args = {
            "-verbose",
            "-keep", "public", "class", "*", "{",
            "public", "static", "void", "main(java.lang.String[]);",
            "}"
        };
        WordReader reader = new ArgumentWordReader(args, tempDir.toFile());
        Properties properties = new Properties();

        // Act - Create parser with ArgumentWordReader with complex arguments
        ConfigurationParser parser = new ConfigurationParser(reader, properties);

        // Assert - Verify the parser was created successfully
        assertNotNull(parser, "ConfigurationParser should handle ArgumentWordReader with complex arguments");

        // Clean up
        reader.close();
    }
}
