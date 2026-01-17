package proguard;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link FileWordReader}.
 * Tests all constructors to ensure proper initialization and functionality.
 */
public class FileWordReaderClaudeTest {

    // ========== Constructor with File Tests ==========

    /**
     * Tests the File constructor with a valid file containing simple words.
     * Verifies that the FileWordReader can be instantiated and reads words correctly.
     */
    @Test
    public void testConstructorWithValidFile(@TempDir Path tempDir) throws IOException {
        // Arrange
        File testFile = tempDir.resolve("test.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("hello world test");
        }

        // Act
        FileWordReader reader = new FileWordReader(testFile);

        // Assert
        assertNotNull(reader, "FileWordReader should not be null");
        assertEquals("hello", reader.nextWord(false, false));
        assertEquals("world", reader.nextWord(false, false));
        assertEquals("test", reader.nextWord(false, false));
        assertNull(reader.nextWord(false, false));

        reader.close();
    }

    /**
     * Tests the File constructor with a file containing UTF-8 characters.
     * Verifies that UTF-8 encoding is properly handled.
     */
    @Test
    public void testConstructorWithUTF8File(@TempDir Path tempDir) throws IOException {
        // Arrange
        File testFile = tempDir.resolve("utf8.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("café naïve résumé");
        }

        // Act
        FileWordReader reader = new FileWordReader(testFile);

        // Assert
        assertNotNull(reader, "FileWordReader should not be null");
        assertEquals("café", reader.nextWord(false, false));
        assertEquals("naïve", reader.nextWord(false, false));
        assertEquals("résumé", reader.nextWord(false, false));

        reader.close();
    }

    /**
     * Tests the File constructor with an empty file.
     * Verifies that reading from empty file returns null immediately.
     */
    @Test
    public void testConstructorWithEmptyFile(@TempDir Path tempDir) throws IOException {
        // Arrange
        File testFile = tempDir.resolve("empty.txt").toFile();
        testFile.createNewFile();

        // Act
        FileWordReader reader = new FileWordReader(testFile);

        // Assert
        assertNull(reader.nextWord(false, false), "Empty file should return null");

        reader.close();
    }

    /**
     * Tests the File constructor with a file containing only whitespace.
     * Verifies that whitespace-only files are treated as empty.
     */
    @Test
    public void testConstructorWithWhitespaceOnlyFile(@TempDir Path tempDir) throws IOException {
        // Arrange
        File testFile = tempDir.resolve("whitespace.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("   \n\t\n   ");
        }

        // Act
        FileWordReader reader = new FileWordReader(testFile);

        // Assert
        assertNull(reader.nextWord(false, false), "Whitespace-only file should return null");

        reader.close();
    }

    /**
     * Tests the File constructor with a file containing comments.
     * Verifies that comments are properly handled.
     */
    @Test
    public void testConstructorWithCommentsInFile(@TempDir Path tempDir) throws IOException {
        // Arrange
        File testFile = tempDir.resolve("comments.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("word1\n# This is a comment\nword2");
        }

        // Act
        FileWordReader reader = new FileWordReader(testFile);

        // Assert
        assertEquals("word1", reader.nextWord(false, false));
        assertEquals("word2", reader.nextWord(false, false));
        assertNull(reader.nextWord(false, false));

        reader.close();
    }

    /**
     * Tests the File constructor with a file containing multiple lines.
     * Verifies that multi-line files are read correctly.
     */
    @Test
    public void testConstructorWithMultilineFile(@TempDir Path tempDir) throws IOException {
        // Arrange
        File testFile = tempDir.resolve("multiline.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("line1word1 line1word2\nline2word1 line2word2\nline3word1");
        }

        // Act
        FileWordReader reader = new FileWordReader(testFile);

        // Assert
        assertEquals("line1word1", reader.nextWord(false, false));
        assertEquals("line1word2", reader.nextWord(false, false));
        assertEquals("line2word1", reader.nextWord(false, false));
        assertEquals("line2word2", reader.nextWord(false, false));
        assertEquals("line3word1", reader.nextWord(false, false));
        assertNull(reader.nextWord(false, false));

        reader.close();
    }

    /**
     * Tests the File constructor with a non-existent file.
     * Verifies that IOException is thrown for missing files.
     */
    @Test
    public void testConstructorWithNonExistentFile(@TempDir Path tempDir) {
        // Arrange
        File nonExistentFile = tempDir.resolve("nonexistent.txt").toFile();

        // Act & Assert
        assertThrows(IOException.class, () -> {
            new FileWordReader(nonExistentFile);
        }, "Should throw IOException for non-existent file");
    }

    /**
     * Tests the File constructor with a directory instead of a file.
     * Verifies that IOException is thrown when trying to read a directory.
     */
    @Test
    public void testConstructorWithDirectory(@TempDir Path tempDir) {
        // Arrange
        File directory = tempDir.toFile();

        // Act & Assert
        assertThrows(IOException.class, () -> {
            new FileWordReader(directory);
        }, "Should throw IOException when trying to read a directory");
    }

    /**
     * Tests that getBaseDir() returns the correct parent directory.
     * Verifies that the baseDir is properly set to the file's parent directory.
     */
    @Test
    public void testConstructorSetsBaseDirCorrectly(@TempDir Path tempDir) throws IOException {
        // Arrange
        File testFile = tempDir.resolve("test.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("test");
        }

        // Act
        FileWordReader reader = new FileWordReader(testFile);

        // Assert
        assertEquals(testFile.getParentFile(), reader.getBaseDir(),
            "BaseDir should be set to the file's parent directory");

        reader.close();
    }

    /**
     * Tests the File constructor with a file in a nested directory.
     * Verifies that baseDir is correctly set to the immediate parent.
     */
    @Test
    public void testConstructorWithNestedDirectory(@TempDir Path tempDir) throws IOException {
        // Arrange
        Path nestedDir = tempDir.resolve("nested").resolve("dir");
        nestedDir.toFile().mkdirs();
        File testFile = nestedDir.resolve("test.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("nested");
        }

        // Act
        FileWordReader reader = new FileWordReader(testFile);

        // Assert
        assertEquals(testFile.getParentFile(), reader.getBaseDir(),
            "BaseDir should be the immediate parent directory");
        assertEquals("nested", reader.nextWord(false, false));

        reader.close();
    }

    /**
     * Tests that close() properly closes the reader.
     * Verifies that the reader can be closed without errors.
     */
    @Test
    public void testCloseAfterFileConstruction(@TempDir Path tempDir) throws IOException {
        // Arrange
        File testFile = tempDir.resolve("test.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("test");
        }
        FileWordReader reader = new FileWordReader(testFile);

        // Act & Assert
        assertDoesNotThrow(() -> reader.close(), "Close should not throw exception");
    }

    // ========== Constructor with URL Tests ==========

    /**
     * Tests the URL constructor with a valid file URL.
     * Verifies that the FileWordReader can be instantiated from a URL.
     */
    @Test
    public void testConstructorWithValidURL(@TempDir Path tempDir) throws IOException {
        // Arrange
        File testFile = tempDir.resolve("url_test.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("url word test");
        }
        URL url = testFile.toURI().toURL();

        // Act
        FileWordReader reader = new FileWordReader(url);

        // Assert
        assertNotNull(reader, "FileWordReader should not be null");
        assertEquals("url", reader.nextWord(false, false));
        assertEquals("word", reader.nextWord(false, false));
        assertEquals("test", reader.nextWord(false, false));
        assertNull(reader.nextWord(false, false));

        reader.close();
    }

    /**
     * Tests the URL constructor with a file URL containing UTF-8 characters.
     * Verifies that UTF-8 encoding works correctly with URL input.
     */
    @Test
    public void testConstructorWithUTF8URL(@TempDir Path tempDir) throws IOException {
        // Arrange
        File testFile = tempDir.resolve("utf8_url.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("München Zürich São");
        }
        URL url = testFile.toURI().toURL();

        // Act
        FileWordReader reader = new FileWordReader(url);

        // Assert
        assertNotNull(reader, "FileWordReader should not be null");
        assertEquals("München", reader.nextWord(false, false));
        assertEquals("Zürich", reader.nextWord(false, false));
        assertEquals("São", reader.nextWord(false, false));

        reader.close();
    }

    /**
     * Tests the URL constructor with an empty file URL.
     * Verifies that empty files via URL return null immediately.
     */
    @Test
    public void testConstructorWithEmptyFileURL(@TempDir Path tempDir) throws IOException {
        // Arrange
        File testFile = tempDir.resolve("empty_url.txt").toFile();
        testFile.createNewFile();
        URL url = testFile.toURI().toURL();

        // Act
        FileWordReader reader = new FileWordReader(url);

        // Assert
        assertNull(reader.nextWord(false, false), "Empty file URL should return null");

        reader.close();
    }

    /**
     * Tests the URL constructor with a URL containing comments.
     * Verifies that comments are properly ignored when reading from URL.
     */
    @Test
    public void testConstructorWithCommentsInURL(@TempDir Path tempDir) throws IOException {
        // Arrange
        File testFile = tempDir.resolve("comments_url.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("before\n# comment line\nafter");
        }
        URL url = testFile.toURI().toURL();

        // Act
        FileWordReader reader = new FileWordReader(url);

        // Assert
        assertEquals("before", reader.nextWord(false, false));
        assertEquals("after", reader.nextWord(false, false));
        assertNull(reader.nextWord(false, false));

        reader.close();
    }

    /**
     * Tests the URL constructor with multiline content.
     * Verifies that multiline files are read correctly via URL.
     */
    @Test
    public void testConstructorWithMultilineURL(@TempDir Path tempDir) throws IOException {
        // Arrange
        File testFile = tempDir.resolve("multiline_url.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("first second\nthird fourth\nfifth");
        }
        URL url = testFile.toURI().toURL();

        // Act
        FileWordReader reader = new FileWordReader(url);

        // Assert
        assertEquals("first", reader.nextWord(false, false));
        assertEquals("second", reader.nextWord(false, false));
        assertEquals("third", reader.nextWord(false, false));
        assertEquals("fourth", reader.nextWord(false, false));
        assertEquals("fifth", reader.nextWord(false, false));
        assertNull(reader.nextWord(false, false));

        reader.close();
    }

    /**
     * Tests the URL constructor with an invalid URL.
     * Verifies that IOException is thrown for invalid URLs.
     */
    @Test
    public void testConstructorWithInvalidURL() throws MalformedURLException {
        // Arrange
        URL invalidUrl = new URL("file:///nonexistent/path/to/file.txt");

        // Act & Assert
        assertThrows(IOException.class, () -> {
            new FileWordReader(invalidUrl);
        }, "Should throw IOException for invalid URL");
    }

    /**
     * Tests that getBaseURL() returns the correct URL.
     * Verifies that the baseURL is properly set.
     */
    @Test
    public void testConstructorSetsBaseURLCorrectly(@TempDir Path tempDir) throws IOException {
        // Arrange
        File testFile = tempDir.resolve("baseurl_test.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("test");
        }
        URL url = testFile.toURI().toURL();

        // Act
        FileWordReader reader = new FileWordReader(url);

        // Assert
        assertEquals(url, reader.getBaseURL(), "BaseURL should be set correctly");

        reader.close();
    }

    /**
     * Tests that close() properly closes the reader created from URL.
     * Verifies that the URL-based reader can be closed without errors.
     */
    @Test
    public void testCloseAfterURLConstruction(@TempDir Path tempDir) throws IOException {
        // Arrange
        File testFile = tempDir.resolve("close_test.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("test");
        }
        URL url = testFile.toURI().toURL();
        FileWordReader reader = new FileWordReader(url);

        // Act & Assert
        assertDoesNotThrow(() -> reader.close(), "Close should not throw exception");
    }

    /**
     * Tests that multiple reads work correctly with URL constructor.
     * Verifies sequential reading functionality.
     */
    @Test
    public void testMultipleReadsWithURL(@TempDir Path tempDir) throws IOException {
        // Arrange
        File testFile = tempDir.resolve("multiple_reads.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("one two three four five");
        }
        URL url = testFile.toURI().toURL();

        // Act
        FileWordReader reader = new FileWordReader(url);

        // Assert
        assertEquals("one", reader.nextWord(false, false));
        assertEquals("two", reader.nextWord(false, false));
        assertEquals("three", reader.nextWord(false, false));
        assertEquals("four", reader.nextWord(false, false));
        assertEquals("five", reader.nextWord(false, false));
        assertNull(reader.nextWord(false, false), "Should return null after all words read");

        reader.close();
    }

    // ========== Integration Tests ==========

    /**
     * Integration test: Verifies that File and URL constructors produce equivalent behavior.
     * Tests that the same file accessed via File and URL produces the same results.
     */
    @Test
    public void testFileAndURLConstructorEquivalence(@TempDir Path tempDir) throws IOException {
        // Arrange
        File testFile = tempDir.resolve("equivalence.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("alpha beta gamma delta");
        }

        // Act - Read via File constructor
        FileWordReader fileReader = new FileWordReader(testFile);
        String word1FromFile = fileReader.nextWord(false, false);
        String word2FromFile = fileReader.nextWord(false, false);
        fileReader.close();

        // Act - Read via URL constructor
        URL url = testFile.toURI().toURL();
        FileWordReader urlReader = new FileWordReader(url);
        String word1FromURL = urlReader.nextWord(false, false);
        String word2FromURL = urlReader.nextWord(false, false);
        urlReader.close();

        // Assert
        assertEquals(word1FromFile, word1FromURL, "First word should be the same");
        assertEquals(word2FromFile, word2FromURL, "Second word should be the same");
    }

    /**
     * Tests that lineLocationDescription() includes the file path for File constructor.
     * Verifies that location descriptions are meaningful.
     */
    @Test
    public void testLocationDescriptionWithFile(@TempDir Path tempDir) throws IOException {
        // Arrange
        File testFile = tempDir.resolve("location.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("test");
        }

        // Act
        FileWordReader reader = new FileWordReader(testFile);
        reader.nextWord(false, false); // Read a word to position the reader
        String location = reader.locationDescription();

        // Assert
        assertNotNull(location, "Location description should not be null");
        assertTrue(location.contains("location.txt"),
            "Location should contain the file name");

        reader.close();
    }

    /**
     * Tests that lineLocationDescription() includes the URL for URL constructor.
     * Verifies that location descriptions work with URLs.
     */
    @Test
    public void testLocationDescriptionWithURL(@TempDir Path tempDir) throws IOException {
        // Arrange
        File testFile = tempDir.resolve("url_location.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("test");
        }
        URL url = testFile.toURI().toURL();

        // Act
        FileWordReader reader = new FileWordReader(url);
        reader.nextWord(false, false); // Read a word to position the reader
        String location = reader.locationDescription();

        // Assert
        assertNotNull(location, "Location description should not be null");
        assertTrue(location.contains("file"),
            "Location should reference the URL");

        reader.close();
    }

    /**
     * Tests reading quoted strings with File constructor.
     * Verifies that quoted words are handled correctly.
     */
    @Test
    public void testQuotedStringsWithFile(@TempDir Path tempDir) throws IOException {
        // Arrange
        File testFile = tempDir.resolve("quoted.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("'quoted string' \"double quoted\" normal");
        }

        // Act
        FileWordReader reader = new FileWordReader(testFile);

        // Assert
        assertEquals("quoted string", reader.nextWord(false, false));
        assertEquals("double quoted", reader.nextWord(false, false));
        assertEquals("normal", reader.nextWord(false, false));

        reader.close();
    }

    /**
     * Tests reading quoted strings with URL constructor.
     * Verifies that quoted words work with URL-based readers.
     */
    @Test
    public void testQuotedStringsWithURL(@TempDir Path tempDir) throws IOException {
        // Arrange
        File testFile = tempDir.resolve("quoted_url.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("'single' \"double\" plain");
        }
        URL url = testFile.toURI().toURL();

        // Act
        FileWordReader reader = new FileWordReader(url);

        // Assert
        assertEquals("single", reader.nextWord(false, false));
        assertEquals("double", reader.nextWord(false, false));
        assertEquals("plain", reader.nextWord(false, false));

        reader.close();
    }
}
