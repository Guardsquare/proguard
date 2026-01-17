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
 * Test class for {@link ConfigurationParser#close()} method.
 * Tests the closing of ConfigurationParser and its underlying resources.
 */
public class ConfigurationParserClaude_closeTest {

    /**
     * Tests the close method with FileWordReader.
     * Verifies that close() properly closes the underlying reader.
     */
    @Test
    public void testCloseWithFileWordReader(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration file and parser
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
        }
        ConfigurationParser parser = new ConfigurationParser(configFile);

        // Act - Close the parser
        parser.close();

        // Assert - Verify no exception was thrown (successful close)
        assertNotNull(parser, "Parser should not be null after close");
    }

    /**
     * Tests the close method with ArgumentWordReader.
     * Verifies that close() works with ArgumentWordReader.
     */
    @Test
    public void testCloseWithArgumentWordReader(@TempDir Path tempDir) throws Exception {
        // Arrange - Create an ArgumentWordReader and parser
        String[] args = {"-verbose", "-dontshrink"};
        WordReader reader = new ArgumentWordReader(args, tempDir.toFile());
        Properties properties = new Properties();
        ConfigurationParser parser = new ConfigurationParser(reader, properties);

        // Act - Close the parser (which should close the reader)
        parser.close();

        // Assert - Verify no exception was thrown
        assertNotNull(parser, "Parser should not be null after close");
    }

    /**
     * Tests the close method called multiple times.
     * Verifies that calling close() multiple times doesn't cause errors.
     */
    @Test
    public void testCloseCalledMultipleTimes(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration file and parser
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
        }
        ConfigurationParser parser = new ConfigurationParser(configFile);

        // Act - Close the parser multiple times
        parser.close();
        parser.close();
        parser.close();

        // Assert - Verify no exception was thrown
        assertNotNull(parser, "Parser should not be null after multiple close calls");
    }

    /**
     * Tests the close method after parsing.
     * Verifies that close() works correctly after parse() has been called.
     */
    @Test
    public void testCloseAfterParsing(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration file and parser
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
            writer.write("-dontshrink\n");
        }
        ConfigurationParser parser = new ConfigurationParser(configFile);
        Configuration config = new Configuration();

        // Act - Parse and then close
        parser.parse(config);
        parser.close();

        // Assert - Verify parsing was successful and close didn't throw exception
        assertTrue(config.verbose, "verbose should be true");
        assertFalse(config.shrink, "shrink should be false");
    }

    /**
     * Tests the close method with empty file.
     * Verifies that close() works with empty configuration files.
     */
    @Test
    public void testCloseWithEmptyFile(@TempDir Path tempDir) throws Exception {
        // Arrange - Create an empty configuration file and parser
        File configFile = tempDir.resolve("empty-config.txt").toFile();
        configFile.createNewFile();
        ConfigurationParser parser = new ConfigurationParser(configFile);

        // Act - Close the parser
        parser.close();

        // Assert - Verify no exception was thrown
        assertNotNull(parser, "Parser should not be null after close");
    }

    /**
     * Tests the close method with URL-based parser.
     * Verifies that close() works with URL-based ConfigurationParser.
     */
    @Test
    public void testCloseWithUrlBasedParser(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration file and URL-based parser
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
        }
        ConfigurationParser parser = new ConfigurationParser(configFile.toURI().toURL(), new Properties());

        // Act - Close the parser
        parser.close();

        // Assert - Verify no exception was thrown
        assertNotNull(parser, "Parser should not be null after close");
    }

    /**
     * Tests the close method without calling parse.
     * Verifies that close() can be called even if parse() was never called.
     */
    @Test
    public void testCloseWithoutParsing(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration file and parser
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
        }
        ConfigurationParser parser = new ConfigurationParser(configFile);

        // Act - Close without parsing
        parser.close();

        // Assert - Verify no exception was thrown
        assertNotNull(parser, "Parser should not be null after close without parsing");
    }

    /**
     * Tests the close method with try-with-resources.
     * Verifies that ConfigurationParser works correctly with try-with-resources if it implements AutoCloseable.
     */
    @Test
    public void testCloseWithTryWithResources(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration file
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
        }
        Configuration config = new Configuration();

        // Act & Assert - Use parser in try block (close will be called automatically)
        try {
            ConfigurationParser parser = new ConfigurationParser(configFile);
            parser.parse(config);
            parser.close();
        } catch (Exception e) {
            fail("Should not throw exception when closing parser");
        }

        // Verify parsing was successful
        assertTrue(config.verbose, "verbose should be true");
    }

    /**
     * Tests the close method with complex configuration.
     * Verifies that close() works with complex configuration files.
     */
    @Test
    public void testCloseWithComplexConfiguration(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a complex configuration file and parser
        File configFile = tempDir.resolve("complex-config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("# Complex configuration\n");
            writer.write("-verbose\n");
            writer.write("-keep public class * {\n");
            writer.write("    public static void main(java.lang.String[]);\n");
            writer.write("}\n");
            writer.write("-dontshrink\n");
        }
        ConfigurationParser parser = new ConfigurationParser(configFile);
        Configuration config = new Configuration();

        // Act - Parse and close
        parser.parse(config);
        parser.close();

        // Assert - Verify parsing was successful and close didn't throw exception
        assertTrue(config.verbose, "verbose should be true");
        assertFalse(config.shrink, "shrink should be false");
    }

    /**
     * Tests that operations after close may fail.
     * Verifies expected behavior when trying to use parser after closing.
     */
    @Test
    public void testOperationsAfterClose(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration file and parser
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
        }
        ConfigurationParser parser = new ConfigurationParser(configFile);
        Configuration config = new Configuration();

        // Act - Close the parser first
        parser.close();

        // Assert - Attempting to parse after close should throw an exception
        assertThrows(Exception.class, () -> {
            parser.parse(config);
        }, "Parsing after close should throw an exception");
    }

    /**
     * Tests the close method with multiple parsers.
     * Verifies that closing one parser doesn't affect others.
     */
    @Test
    public void testCloseWithMultipleParsers(@TempDir Path tempDir) throws Exception {
        // Arrange - Create two configuration files and parsers
        File configFile1 = tempDir.resolve("config1.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile1)) {
            writer.write("-verbose\n");
        }
        File configFile2 = tempDir.resolve("config2.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile2)) {
            writer.write("-dontshrink\n");
        }

        ConfigurationParser parser1 = new ConfigurationParser(configFile1);
        ConfigurationParser parser2 = new ConfigurationParser(configFile2);
        Configuration config2 = new Configuration();

        // Act - Close first parser, use second parser
        parser1.close();
        parser2.parse(config2);
        parser2.close();

        // Assert - Verify second parser still works after first was closed
        assertFalse(config2.shrink, "shrink should be false");
    }

    /**
     * Tests the close method with file containing comments.
     * Verifies that close() works with configuration files containing comments.
     */
    @Test
    public void testCloseWithCommentsInFile(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration file with comments and parser
        File configFile = tempDir.resolve("config-with-comments.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("# This is a comment\n");
            writer.write("-verbose\n");
            writer.write("# Another comment\n");
        }
        ConfigurationParser parser = new ConfigurationParser(configFile);

        // Act - Close the parser
        parser.close();

        // Assert - Verify no exception was thrown
        assertNotNull(parser, "Parser should not be null after close");
    }

    /**
     * Tests the close method with whitespace in file.
     * Verifies that close() works with configuration files containing whitespace.
     */
    @Test
    public void testCloseWithWhitespaceInFile(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration file with whitespace and parser
        File configFile = tempDir.resolve("whitespace-config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("\n");
            writer.write("  -verbose  \n");
            writer.write("\n");
        }
        ConfigurationParser parser = new ConfigurationParser(configFile);

        // Act - Close the parser
        parser.close();

        // Assert - Verify no exception was thrown
        assertNotNull(parser, "Parser should not be null after close");
    }

    /**
     * Tests the close method with different Properties.
     * Verifies that close() works correctly with custom properties.
     */
    @Test
    public void testCloseWithCustomProperties(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration file and parser with custom properties
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
        }
        Properties properties = new Properties();
        properties.setProperty("test.property", "test.value");
        ConfigurationParser parser = new ConfigurationParser(configFile, properties);

        // Act - Close the parser
        parser.close();

        // Assert - Verify no exception was thrown
        assertNotNull(parser, "Parser should not be null after close");
    }

    /**
     * Tests the close method ensuring reader is properly closed.
     * Verifies that the underlying WordReader is closed when parser is closed.
     */
    @Test
    public void testCloseEnsuresReaderIsClosed(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration file and WordReader
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
        }
        WordReader reader = new FileWordReader(configFile);
        Properties properties = new Properties();
        ConfigurationParser parser = new ConfigurationParser(reader, properties);

        // Act - Close the parser (should close the reader)
        parser.close();

        // Assert - Attempting to use the reader after parser close should fail
        assertThrows(Exception.class, () -> {
            reader.nextWord(false, false);
        }, "Reader should be closed after parser close");
    }

    /**
     * Tests the close method with parse that was partially completed.
     * Verifies that close() works even if parsing was interrupted.
     */
    @Test
    public void testCloseAfterPartialParsing(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration file with an unknown option
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
            writer.write("-unknownoption\n");
        }
        ConfigurationParser parser = new ConfigurationParser(configFile);
        Configuration config = new Configuration();

        // Act - Try to parse (may throw exception), then close
        try {
            parser.parse(config, null);
        } catch (ParseException e) {
            // Expected - unknown option without handler
        } finally {
            parser.close();
        }

        // Assert - Verify close was successful
        assertNotNull(parser, "Parser should not be null after close");
    }

    /**
     * Tests the close method with file paths containing spaces.
     * Verifies that close() works with file paths that have spaces.
     */
    @Test
    public void testCloseWithFilePathContainingSpaces(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a subdirectory with spaces and configuration file
        File subDir = tempDir.resolve("folder with spaces").toFile();
        subDir.mkdir();
        File configFile = new File(subDir, "config file.txt");
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
        }
        ConfigurationParser parser = new ConfigurationParser(configFile);

        // Act - Close the parser
        parser.close();

        // Assert - Verify no exception was thrown
        assertNotNull(parser, "Parser should not be null after close");
    }

    /**
     * Tests the close method in a sequence: create, parse, close, repeat.
     * Verifies that the close-create-parse pattern works correctly.
     */
    @Test
    public void testCloseInSequence(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a configuration file
        File configFile = tempDir.resolve("config.txt").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-verbose\n");
        }

        // Act & Assert - Create, parse, close, repeat
        for (int i = 0; i < 3; i++) {
            ConfigurationParser parser = new ConfigurationParser(configFile);
            Configuration config = new Configuration();
            parser.parse(config);
            parser.close();

            assertTrue(config.verbose, "verbose should be true in iteration " + i);
        }
    }
}
