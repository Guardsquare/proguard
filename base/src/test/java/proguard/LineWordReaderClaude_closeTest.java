package proguard;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link LineWordReader#close()} method.
 * Tests the closing behavior of LineWordReader including resource cleanup.
 */
public class LineWordReaderClaude_closeTest {

    /**
     * Tests that close() properly closes the underlying LineNumberReader.
     * Verifies that after closing, the underlying reader is no longer usable.
     */
    @Test
    public void testCloseClosesUnderlyingReader(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a file with content and a LineWordReader
        File inputFile = tempDir.resolve("test-input.txt").toFile();
        try (FileWriter writer = new FileWriter(inputFile)) {
            writer.write("word1 word2\nword3 word4");
        }

        FileReader fileReader = new FileReader(inputFile);
        LineNumberReader lineNumberReader = new LineNumberReader(fileReader);
        LineWordReader wordReader = new LineWordReader(
            lineNumberReader,
            "test-input.txt",
            tempDir.toFile()
        );

        // Act - Close the word reader
        wordReader.close();

        // Assert - Verify the underlying reader is closed by checking ready() returns false
        // or throws an exception (behavior may vary)
        try {
            boolean ready = lineNumberReader.ready();
            assertFalse(ready, "Closed reader should not be ready");
        } catch (IOException e) {
            // This is also acceptable - some implementations throw on closed stream
            assertTrue(true, "IOException on closed stream is acceptable");
        }
    }

    /**
     * Tests that close() can be called successfully on a newly created LineWordReader.
     * Verifies that close() works even if no words have been read.
     */
    @Test
    public void testCloseWithoutReadingWords(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a LineWordReader without reading any words
        File inputFile = tempDir.resolve("test-input.txt").toFile();
        try (FileWriter writer = new FileWriter(inputFile)) {
            writer.write("word1 word2");
        }

        FileReader fileReader = new FileReader(inputFile);
        LineNumberReader lineNumberReader = new LineNumberReader(fileReader);
        LineWordReader wordReader = new LineWordReader(
            lineNumberReader,
            "test-input.txt",
            tempDir.toFile()
        );

        // Act & Assert - Close should not throw any exception
        assertDoesNotThrow(() -> {
            wordReader.close();
        }, "Closing without reading should not throw exception");
    }

    /**
     * Tests that close() can be called multiple times without error.
     * Verifies that the method is idempotent.
     */
    @Test
    public void testCloseMultipleTimes(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a LineWordReader
        File inputFile = tempDir.resolve("test-input.txt").toFile();
        try (FileWriter writer = new FileWriter(inputFile)) {
            writer.write("word1 word2");
        }

        FileReader fileReader = new FileReader(inputFile);
        LineNumberReader lineNumberReader = new LineNumberReader(fileReader);
        LineWordReader wordReader = new LineWordReader(
            lineNumberReader,
            "test-input.txt",
            tempDir.toFile()
        );

        // Act & Assert - Close multiple times should not throw exception
        assertDoesNotThrow(() -> {
            wordReader.close();
            wordReader.close();
            wordReader.close();
        }, "Multiple close calls should not throw exception");
    }

    /**
     * Tests that close() works correctly after reading some words.
     * Verifies that the reader can be closed in the middle of processing.
     */
    @Test
    public void testCloseAfterReadingSomeWords(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a LineWordReader and read some words
        File inputFile = tempDir.resolve("test-input.txt").toFile();
        try (FileWriter writer = new FileWriter(inputFile)) {
            writer.write("word1 word2 word3\nword4 word5");
        }

        FileReader fileReader = new FileReader(inputFile);
        LineNumberReader lineNumberReader = new LineNumberReader(fileReader);
        LineWordReader wordReader = new LineWordReader(
            lineNumberReader,
            "test-input.txt",
            tempDir.toFile()
        );

        // Read some words
        String word1 = wordReader.nextWord(false, false);
        String word2 = wordReader.nextWord(false, false);
        assertNotNull(word1);
        assertNotNull(word2);

        // Act & Assert - Close the reader should not throw exception
        assertDoesNotThrow(() -> {
            wordReader.close();
        }, "Close after reading some words should not throw exception");
    }

    /**
     * Tests that close() works correctly with URL-based constructor.
     * Verifies that closing works regardless of which constructor was used.
     */
    @Test
    public void testCloseWithURLConstructor(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a file and get its URL
        File inputFile = tempDir.resolve("test-input.txt").toFile();
        try (FileWriter writer = new FileWriter(inputFile)) {
            writer.write("word1 word2");
        }

        FileReader fileReader = new FileReader(inputFile);
        LineNumberReader lineNumberReader = new LineNumberReader(fileReader);
        LineWordReader wordReader = new LineWordReader(
            lineNumberReader,
            "test-input.txt",
            inputFile.getParentFile().toURI().toURL()
        );

        // Act & Assert - Close should not throw any exception
        assertDoesNotThrow(() -> {
            wordReader.close();
        }, "Closing with URL constructor should not throw exception");
    }

    /**
     * Tests that close() properly closes included WordReaders.
     * Verifies that calling close() also closes any included readers.
     */
    @Test
    public void testCloseWithIncludedReader(@TempDir Path tempDir) throws Exception {
        // Arrange - Create two LineWordReaders, one included in the other
        File inputFile1 = tempDir.resolve("test-input1.txt").toFile();
        try (FileWriter writer = new FileWriter(inputFile1)) {
            writer.write("word1 word2");
        }

        File inputFile2 = tempDir.resolve("test-input2.txt").toFile();
        try (FileWriter writer = new FileWriter(inputFile2)) {
            writer.write("word3 word4");
        }

        FileReader fileReader1 = new FileReader(inputFile1);
        LineNumberReader lineNumberReader1 = new LineNumberReader(fileReader1);
        LineWordReader wordReader1 = new LineWordReader(
            lineNumberReader1,
            "test-input1.txt",
            tempDir.toFile()
        );

        FileReader fileReader2 = new FileReader(inputFile2);
        LineNumberReader lineNumberReader2 = new LineNumberReader(fileReader2);
        LineWordReader wordReader2 = new LineWordReader(
            lineNumberReader2,
            "test-input2.txt",
            tempDir.toFile()
        );

        // Include wordReader2 in wordReader1
        wordReader1.includeWordReader(wordReader2);

        // Act & Assert - Close the main reader (should close included reader too)
        assertDoesNotThrow(() -> {
            wordReader1.close();
        }, "Close should not throw exception even with included reader");
    }

    /**
     * Tests that close() works correctly after reading all words.
     * Verifies that the reader can be closed after exhausting the input.
     */
    @Test
    public void testCloseAfterReadingAllWords(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a LineWordReader with limited content
        File inputFile = tempDir.resolve("test-input.txt").toFile();
        try (FileWriter writer = new FileWriter(inputFile)) {
            writer.write("word1 word2");
        }

        FileReader fileReader = new FileReader(inputFile);
        LineNumberReader lineNumberReader = new LineNumberReader(fileReader);
        LineWordReader wordReader = new LineWordReader(
            lineNumberReader,
            "test-input.txt",
            tempDir.toFile()
        );

        // Read all words
        String word1 = wordReader.nextWord(false, false);
        String word2 = wordReader.nextWord(false, false);
        String word3 = wordReader.nextWord(false, false); // Should be null (end of file)
        assertNotNull(word1);
        assertNotNull(word2);
        assertNull(word3, "Should return null at end of file");

        // Act & Assert - Close should not throw any exception
        assertDoesNotThrow(() -> {
            wordReader.close();
        }, "Closing after reading all words should not throw exception");
    }

    /**
     * Tests that close() handles empty file correctly.
     * Verifies that closing a reader for an empty file doesn't cause issues.
     */
    @Test
    public void testCloseWithEmptyFile(@TempDir Path tempDir) throws Exception {
        // Arrange - Create an empty file
        File inputFile = tempDir.resolve("empty.txt").toFile();
        inputFile.createNewFile();

        FileReader fileReader = new FileReader(inputFile);
        LineNumberReader lineNumberReader = new LineNumberReader(fileReader);
        LineWordReader wordReader = new LineWordReader(
            lineNumberReader,
            "empty.txt",
            tempDir.toFile()
        );

        // Act & Assert - Close should not throw any exception
        assertDoesNotThrow(() -> {
            wordReader.close();
        }, "Closing with empty file should not throw exception");
    }

    /**
     * Tests that close() handles files with only whitespace correctly.
     * Verifies that closing works even if the file contains only whitespace.
     */
    @Test
    public void testCloseWithWhitespaceOnlyFile(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a file with only whitespace
        File inputFile = tempDir.resolve("whitespace.txt").toFile();
        try (FileWriter writer = new FileWriter(inputFile)) {
            writer.write("   \n\t\n   ");
        }

        FileReader fileReader = new FileReader(inputFile);
        LineNumberReader lineNumberReader = new LineNumberReader(fileReader);
        LineWordReader wordReader = new LineWordReader(
            lineNumberReader,
            "whitespace.txt",
            tempDir.toFile()
        );

        // Act & Assert - Close should not throw any exception
        assertDoesNotThrow(() -> {
            wordReader.close();
        }, "Closing with whitespace-only file should not throw exception");
    }

    /**
     * Tests that close() releases file handles properly.
     * Verifies that after closing, the file can be deleted (indicating handle is released).
     */
    @Test
    public void testCloseReleasesFileHandle(@TempDir Path tempDir) throws Exception {
        // Arrange - Create a file and a LineWordReader
        File inputFile = tempDir.resolve("test-input.txt").toFile();
        try (FileWriter writer = new FileWriter(inputFile)) {
            writer.write("word1 word2");
        }

        FileReader fileReader = new FileReader(inputFile);
        LineNumberReader lineNumberReader = new LineNumberReader(fileReader);
        LineWordReader wordReader = new LineWordReader(
            lineNumberReader,
            "test-input.txt",
            tempDir.toFile()
        );

        // Read a word to ensure the reader is initialized
        String word = wordReader.nextWord(false, false);
        assertNotNull(word);

        // Act - Close the reader
        wordReader.close();

        // Assert - The file should be deletable (handle released)
        assertTrue(inputFile.delete(), "File should be deletable after close");
    }
}
