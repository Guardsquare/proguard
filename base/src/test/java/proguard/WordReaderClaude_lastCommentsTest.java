package proguard;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link WordReader#lastComments()}.
 * Tests the lastComments method behavior for collecting and returning comments.
 */
public class WordReaderClaude_lastCommentsTest {

    /**
     * Tests lastComments returns null when no comments have been encountered.
     */
    @Test
    public void testLastCommentsWithNoComments() throws IOException {
        // Arrange
        ArgumentWordReader reader = new ArgumentWordReader(new String[]{"word1", "word2"}, null);

        // Act
        reader.nextWord(false, false);
        String comments = reader.lastComments();

        // Assert
        assertNull(comments, "Should return null when no comments have been encountered");

        reader.close();
    }

    /**
     * Tests lastComments returns a single comment line.
     */
    @Test
    public void testLastCommentsWithSingleComment() throws IOException {
        // Arrange
        ArgumentWordReader reader = new ArgumentWordReader(
            new String[]{"# This is a comment", "word1"}, null);

        // Act
        reader.nextWord(false, false);
        String comments = reader.lastComments();

        // Assert
        assertEquals(" This is a comment", comments,
            "Should return the comment without the '#' character");

        reader.close();
    }

    /**
     * Tests lastComments returns multiple comment lines concatenated with newlines.
     */
    @Test
    public void testLastCommentsWithMultipleComments() throws IOException {
        // Arrange
        ArgumentWordReader reader = new ArgumentWordReader(
            new String[]{"# Comment 1", "# Comment 2", "# Comment 3", "word1"}, null);

        // Act
        reader.nextWord(false, false);
        String comments = reader.lastComments();

        // Assert
        assertEquals(" Comment 1\n Comment 2\n Comment 3", comments,
            "Should return all comments concatenated with newlines");

        reader.close();
    }

    /**
     * Tests that lastComments clears the comments after returning them.
     */
    @Test
    public void testLastCommentsClearsComments() throws IOException {
        // Arrange
        ArgumentWordReader reader = new ArgumentWordReader(
            new String[]{"# Comment", "word1", "word2"}, null);

        // Act
        reader.nextWord(false, false);
        String firstCall = reader.lastComments();
        String secondCall = reader.lastComments();

        // Assert
        assertEquals(" Comment", firstCall, "First call should return the comment");
        assertNull(secondCall, "Second call should return null after comments are cleared");

        reader.close();
    }

    /**
     * Tests lastComments accumulates comments across multiple calls to nextWord.
     */
    @Test
    public void testLastCommentsAccumulatesBeforeNextWord() throws IOException {
        // Arrange
        ArgumentWordReader reader = new ArgumentWordReader(
            new String[]{"# Comment 1", "# Comment 2", "word1", "word2"}, null);

        // Act
        reader.nextWord(false, false);
        String comments = reader.lastComments();
        reader.nextWord(false, false);
        String noComments = reader.lastComments();

        // Assert
        assertEquals(" Comment 1\n Comment 2", comments,
            "Should accumulate all comments before the word");
        assertNull(noComments, "Should return null when no new comments");

        reader.close();
    }

    /**
     * Tests lastComments with inline comments (comments after words on the same line).
     */
    @Test
    public void testLastCommentsWithInlineComments() throws IOException {
        // Arrange
        ArgumentWordReader reader = new ArgumentWordReader(
            new String[]{"word1 # inline comment", "word2"}, null);

        // Act
        String word1 = reader.nextWord(false, false);
        String commentsAfterWord1 = reader.lastComments();
        String word2 = reader.nextWord(false, false);
        String commentsAfterWord2 = reader.lastComments();

        // Assert
        assertEquals("word1", word1, "Should read the word before the comment");
        assertNull(commentsAfterWord1,
            "Inline comments on the same line as a word are not captured");
        assertEquals("word2", word2);
        assertNull(commentsAfterWord2);

        reader.close();
    }

    /**
     * Tests lastComments when there are empty comment lines.
     */
    @Test
    public void testLastCommentsWithEmptyComment() throws IOException {
        // Arrange
        ArgumentWordReader reader = new ArgumentWordReader(
            new String[]{"#", "word1"}, null);

        // Act
        reader.nextWord(false, false);
        String comments = reader.lastComments();

        // Assert
        assertEquals("", comments, "Should return empty string for empty comment line");

        reader.close();
    }

    /**
     * Tests lastComments with comments containing special characters.
     */
    @Test
    public void testLastCommentsWithSpecialCharacters() throws IOException {
        // Arrange
        ArgumentWordReader reader = new ArgumentWordReader(
            new String[]{"# Comment with @special {chars} (and) 'quotes'", "word1"}, null);

        // Act
        reader.nextWord(false, false);
        String comments = reader.lastComments();

        // Assert
        assertEquals(" Comment with @special {chars} (and) 'quotes'", comments,
            "Should preserve special characters in comments");

        reader.close();
    }

    /**
     * Tests lastComments delegates to included reader when one exists.
     */
    @Test
    public void testLastCommentsWithIncludedReader() throws IOException {
        // Arrange
        ArgumentWordReader mainReader = new ArgumentWordReader(
            new String[]{"# Main comment", "mainWord"}, null);
        ArgumentWordReader includedReader = new ArgumentWordReader(
            new String[]{"# Included comment", "includedWord"}, null);

        mainReader.includeWordReader(includedReader);

        // Act
        // First word comes from included reader
        String word1 = mainReader.nextWord(false, false);
        String comments1 = mainReader.lastComments();

        // Assert
        assertEquals("includedWord", word1, "Should read from included reader");
        assertEquals(" Included comment", comments1,
            "Should return comments from included reader");

        mainReader.close();
    }

    /**
     * Tests lastComments returns to main reader after included reader is exhausted.
     */
    @Test
    public void testLastCommentsAfterExhaustingIncludedReader() throws IOException {
        // Arrange
        ArgumentWordReader mainReader = new ArgumentWordReader(
            new String[]{"# Main comment", "mainWord"}, null);
        ArgumentWordReader includedReader = new ArgumentWordReader(
            new String[]{"includedWord"}, null);

        mainReader.includeWordReader(includedReader);

        // Act
        // Read from included reader (no comments)
        String word1 = mainReader.nextWord(false, false);
        String comments1 = mainReader.lastComments();

        // Read from main reader (has comments)
        String word2 = mainReader.nextWord(false, false);
        String comments2 = mainReader.lastComments();

        // Assert
        assertEquals("includedWord", word1);
        assertNull(comments1, "Included reader had no comments");
        assertEquals("mainWord", word2);
        assertEquals(" Main comment", comments2, "Should return main reader's comments");

        mainReader.close();
    }

    /**
     * Tests lastComments with chained included readers.
     */
    @Test
    public void testLastCommentsWithChainedIncludedReaders() throws IOException {
        // Arrange
        ArgumentWordReader reader1 = new ArgumentWordReader(
            new String[]{"# Comment 1", "word1"}, null);
        ArgumentWordReader reader2 = new ArgumentWordReader(
            new String[]{"# Comment 2", "word2"}, null);
        ArgumentWordReader reader3 = new ArgumentWordReader(
            new String[]{"# Comment 3", "word3"}, null);

        reader1.includeWordReader(reader2);
        reader2.includeWordReader(reader3);

        // Act
        String word3 = reader1.nextWord(false, false);
        String comments3 = reader1.lastComments();

        String word2 = reader1.nextWord(false, false);
        String comments2 = reader1.lastComments();

        String word1 = reader1.nextWord(false, false);
        String comments1 = reader1.lastComments();

        // Assert
        assertEquals("word3", word3);
        assertEquals(" Comment 3", comments3, "Should get comments from deepest reader");
        assertEquals("word2", word2);
        assertEquals(" Comment 2", comments2, "Should get comments from middle reader");
        assertEquals("word1", word1);
        assertEquals(" Comment 1", comments1, "Should get comments from main reader");

        reader1.close();
    }

    /**
     * Tests lastComments can be called multiple times after exhausting the reader.
     */
    @Test
    public void testLastCommentsAfterExhausted() throws IOException {
        // Arrange
        ArgumentWordReader reader = new ArgumentWordReader(
            new String[]{"# Comment", "word1"}, null);

        // Act
        reader.nextWord(false, false);
        reader.lastComments(); // Clear the comments
        assertNull(reader.nextWord(false, false)); // Exhaust the reader

        String comments1 = reader.lastComments();
        String comments2 = reader.lastComments();

        // Assert
        assertNull(comments1, "Should return null after reader is exhausted");
        assertNull(comments2, "Should consistently return null");

        reader.close();
    }

    /**
     * Tests lastComments with whitespace before comments.
     */
    @Test
    public void testLastCommentsWithLeadingWhitespace() throws IOException {
        // Arrange
        ArgumentWordReader reader = new ArgumentWordReader(
            new String[]{"   # Comment with leading spaces", "word1"}, null);

        // Act
        reader.nextWord(false, false);
        String comments = reader.lastComments();

        // Assert
        assertEquals(" Comment with leading spaces", comments,
            "Should handle leading whitespace before comment character");

        reader.close();
    }

    /**
     * Tests lastComments is independent for separate reader instances.
     */
    @Test
    public void testLastCommentsIndependentReaders() throws IOException {
        // Arrange
        ArgumentWordReader reader1 = new ArgumentWordReader(
            new String[]{"# Comment 1", "word1"}, null);
        ArgumentWordReader reader2 = new ArgumentWordReader(
            new String[]{"# Comment 2", "word2"}, null);

        // Act
        reader1.nextWord(false, false);
        reader2.nextWord(false, false);
        String comments1 = reader1.lastComments();
        String comments2 = reader2.lastComments();

        // Assert
        assertEquals(" Comment 1", comments1, "Reader 1 should have its own comments");
        assertEquals(" Comment 2", comments2, "Reader 2 should have its own comments");

        reader1.close();
        reader2.close();
    }

    /**
     * Tests lastComments with mixed comment and non-comment lines.
     */
    @Test
    public void testLastCommentsWithMixedLines() throws IOException {
        // Arrange
        ArgumentWordReader reader = new ArgumentWordReader(
            new String[]{"# Comment 1", "word1", "# Comment 2", "word2", "word3"}, null);

        // Act & Assert
        String word1 = reader.nextWord(false, false);
        assertEquals("word1", word1);
        String comments1 = reader.lastComments();
        assertEquals(" Comment 1", comments1);

        String word2 = reader.nextWord(false, false);
        assertEquals("word2", word2);
        String comments2 = reader.lastComments();
        assertEquals(" Comment 2", comments2);

        String word3 = reader.nextWord(false, false);
        assertEquals("word3", word3);
        String comments3 = reader.lastComments();
        assertNull(comments3, "No comments before word3");

        reader.close();
    }

    /**
     * Tests lastComments before calling nextWord for the first time.
     */
    @Test
    public void testLastCommentsBeforeFirstNextWord() throws IOException {
        // Arrange
        ArgumentWordReader reader = new ArgumentWordReader(
            new String[]{"# Comment", "word1"}, null);

        // Act
        String comments = reader.lastComments();

        // Assert
        assertNull(comments, "Should return null before any words are read");

        reader.close();
    }

    /**
     * Tests lastComments with consecutive comment lines without blank lines.
     */
    @Test
    public void testLastCommentsWithConsecutiveComments() throws IOException {
        // Arrange
        ArgumentWordReader reader = new ArgumentWordReader(
            new String[]{
                "# First comment",
                "# Second comment",
                "# Third comment",
                "# Fourth comment",
                "word1"
            }, null);

        // Act
        reader.nextWord(false, false);
        String comments = reader.lastComments();

        // Assert
        assertEquals(" First comment\n Second comment\n Third comment\n Fourth comment", comments,
            "Should concatenate all consecutive comments");

        reader.close();
    }
}
