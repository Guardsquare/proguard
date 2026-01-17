package proguard;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link WordReader#close()} method.
 * Tests the base close() behavior, particularly around includeWordReader management.
 */
public class WordReaderClaude_closeTest {

    /**
     * Simple concrete implementation of WordReader for testing purposes.
     * Tracks whether close() has been called.
     */
    private static class TestWordReader extends WordReader {
        private boolean closed = false;
        private final String[] lines;
        private int lineIndex = 0;

        public TestWordReader(File baseDir, String... lines) {
            super(baseDir);
            this.lines = lines;
        }

        @Override
        protected String nextLine() throws IOException {
            if (lineIndex < lines.length) {
                return lines[lineIndex++];
            }
            return null;
        }

        @Override
        protected String lineLocationDescription() {
            return "test line " + lineIndex;
        }

        @Override
        public void close() throws IOException {
            super.close();
            closed = true;
        }

        public boolean isClosed() {
            return closed;
        }
    }

    /**
     * Tests that close() can be called on a WordReader without any included readers.
     * Verifies that the method completes without exception.
     */
    @Test
    public void testCloseWithoutIncludedReader() throws IOException {
        // Arrange
        TestWordReader reader = new TestWordReader(null, "word1 word2");

        // Act & Assert
        assertDoesNotThrow(() -> reader.close(), "close() should not throw exception");
        assertTrue(reader.isClosed(), "Reader should be marked as closed");
    }

    /**
     * Tests that close() properly closes an included WordReader.
     * Verifies that the includeWordReader is closed and set to null.
     */
    @Test
    public void testCloseWithSingleIncludedReader() throws IOException {
        // Arrange
        TestWordReader mainReader = new TestWordReader(null, "main1 main2");
        TestWordReader includedReader = new TestWordReader(null, "included1 included2");

        mainReader.includeWordReader(includedReader);

        // Act
        mainReader.close();

        // Assert
        assertTrue(mainReader.isClosed(), "Main reader should be closed");
        assertTrue(includedReader.isClosed(), "Included reader should be closed");
    }

    /**
     * Tests that close() properly closes a chain of included WordReaders.
     * Verifies that all readers in the chain are closed.
     */
    @Test
    public void testCloseWithMultipleIncludedReaders() throws IOException {
        // Arrange
        TestWordReader mainReader = new TestWordReader(null, "main");
        TestWordReader included1 = new TestWordReader(null, "included1");
        TestWordReader included2 = new TestWordReader(null, "included2");
        TestWordReader included3 = new TestWordReader(null, "included3");

        mainReader.includeWordReader(included1);
        mainReader.includeWordReader(included2);
        mainReader.includeWordReader(included3);

        // Act
        mainReader.close();

        // Assert
        assertTrue(mainReader.isClosed(), "Main reader should be closed");
        assertTrue(included1.isClosed(), "First included reader should be closed");
        assertTrue(included2.isClosed(), "Second included reader should be closed");
        assertTrue(included3.isClosed(), "Third included reader should be closed");
    }

    /**
     * Tests that close() can be called multiple times without error.
     * Verifies idempotent behavior of close().
     */
    @Test
    public void testCloseMultipleTimes() throws IOException {
        // Arrange
        TestWordReader reader = new TestWordReader(null, "word1 word2");

        // Act & Assert
        assertDoesNotThrow(() -> {
            reader.close();
            reader.close();
            reader.close();
        }, "Multiple close() calls should not throw exception");
    }

    /**
     * Tests that close() works correctly after reading words from included reader.
     * Verifies that close() can be called while an included reader is active.
     */
    @Test
    public void testCloseAfterReadingFromIncludedReader() throws IOException {
        // Arrange
        TestWordReader mainReader = new TestWordReader(null, "main1 main2");
        TestWordReader includedReader = new TestWordReader(null, "included1 included2");

        mainReader.includeWordReader(includedReader);

        // Read one word (should come from included reader)
        String word = mainReader.nextWord(false, false);
        assertNotNull(word, "Should be able to read a word");

        // Act
        mainReader.close();

        // Assert
        assertTrue(mainReader.isClosed(), "Main reader should be closed");
        assertTrue(includedReader.isClosed(), "Included reader should be closed");
    }

    /**
     * Tests that close() works correctly when included reader has been exhausted.
     * Verifies that close() handles the case where includeWordReader might already be null.
     */
    @Test
    public void testCloseAfterExhaustingIncludedReader() throws IOException {
        // Arrange
        TestWordReader mainReader = new TestWordReader(null, "main1");
        TestWordReader includedReader = new TestWordReader(null, "inc1");

        mainReader.includeWordReader(includedReader);

        // Read all words from included reader and one from main
        String word1 = mainReader.nextWord(false, false);  // from included
        String word2 = mainReader.nextWord(false, false);  // from main (included exhausted)
        assertNotNull(word1);
        assertNotNull(word2);

        // At this point, the included reader should have been closed and set to null
        // during the nextWord() call when it was exhausted

        // Act
        mainReader.close();

        // Assert
        assertTrue(mainReader.isClosed(), "Main reader should be closed");
        assertTrue(includedReader.isClosed(), "Included reader should be closed");
    }

    /**
     * Tests close() behavior with ArgumentWordReader, which doesn't override close().
     * This directly tests the base WordReader.close() implementation.
     */
    @Test
    public void testCloseWithArgumentWordReader() throws IOException {
        // Arrange
        String[] args = {"arg1", "arg2", "arg3"};
        ArgumentWordReader reader = new ArgumentWordReader(args, null);

        // Act & Assert
        assertDoesNotThrow(() -> reader.close(), "close() should not throw exception");
    }

    /**
     * Tests close() with ArgumentWordReader that has an included reader.
     * Verifies the base close() implementation handles included readers correctly.
     */
    @Test
    public void testCloseWithArgumentWordReaderAndIncludedReader() throws IOException {
        // Arrange
        String[] args1 = {"arg1", "arg2"};
        String[] args2 = {"arg3", "arg4"};
        ArgumentWordReader mainReader = new ArgumentWordReader(args1, null);
        ArgumentWordReader includedReader = new ArgumentWordReader(args2, null);

        mainReader.includeWordReader(includedReader);

        // Act & Assert
        assertDoesNotThrow(() -> mainReader.close(), "close() should not throw exception");
    }

    /**
     * Tests that close() can be called on a reader that hasn't read any words.
     * Verifies that close() works in pristine state.
     */
    @Test
    public void testCloseBeforeReadingAnyWords() throws IOException {
        // Arrange
        TestWordReader reader = new TestWordReader(null, "word1 word2 word3");

        // Act & Assert (close immediately without reading)
        assertDoesNotThrow(() -> reader.close(), "close() should work before reading any words");
        assertTrue(reader.isClosed(), "Reader should be closed");
    }

    /**
     * Tests that close() properly handles a reader with empty content.
     * Verifies that close() works when there are no lines to read.
     */
    @Test
    public void testCloseWithEmptyReader() throws IOException {
        // Arrange - Create reader with no lines
        TestWordReader reader = new TestWordReader(null);

        // Act & Assert
        assertDoesNotThrow(() -> reader.close(), "close() should work with empty reader");
        assertTrue(reader.isClosed(), "Empty reader should be closed");
    }
}
