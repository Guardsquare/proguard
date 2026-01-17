package proguard;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ArgumentWordReader#ArgumentWordReader(String[], File)} constructor.
 * Tests the constructor's behavior with various argument and baseDir combinations.
 */
public class ArgumentWordReaderClaude_constructorTest {

    /**
     * Tests the constructor with valid non-null arguments and null baseDir.
     * Verifies that the reader is properly initialized and can read the arguments.
     */
    @Test
    public void testConstructorWithValidArgumentsAndNullBaseDir() throws IOException {
        // Arrange
        String[] arguments = {"arg1", "arg2", "arg3"};

        // Act
        ArgumentWordReader reader = new ArgumentWordReader(arguments, null);

        // Assert - verify the reader can read the arguments
        String word1 = reader.nextWord(false, false);
        assertEquals("arg1", word1, "First argument should be 'arg1'");

        String word2 = reader.nextWord(false, false);
        assertEquals("arg2", word2, "Second argument should be 'arg2'");

        String word3 = reader.nextWord(false, false);
        assertEquals("arg3", word3, "Third argument should be 'arg3'");

        // After reading all arguments, nextWord should return null
        String noMoreWords = reader.nextWord(false, false);
        assertNull(noMoreWords, "Should return null when no more arguments");

        reader.close();
    }

    /**
     * Tests the constructor with valid non-null arguments and a valid baseDir.
     * Verifies that both the arguments array and baseDir are properly stored.
     */
    @Test
    public void testConstructorWithValidArgumentsAndValidBaseDir() throws IOException {
        // Arrange
        String[] arguments = {"option1", "option2"};
        File baseDir = new File("/tmp");

        // Act
        ArgumentWordReader reader = new ArgumentWordReader(arguments, baseDir);

        // Assert - verify the reader can read arguments
        String word1 = reader.nextWord(false, false);
        assertEquals("option1", word1, "First argument should be 'option1'");

        // Verify baseDir is stored correctly
        File storedBaseDir = reader.getBaseDir();
        assertEquals(baseDir, storedBaseDir, "BaseDir should be stored correctly");

        reader.close();
    }

    /**
     * Tests the constructor with an empty arguments array.
     * Verifies that the reader handles empty arrays gracefully.
     */
    @Test
    public void testConstructorWithEmptyArgumentsArray() throws IOException {
        // Arrange
        String[] arguments = {};

        // Act
        ArgumentWordReader reader = new ArgumentWordReader(arguments, null);

        // Assert - should return null immediately when reading
        String word = reader.nextWord(false, false);
        assertNull(word, "Should return null for empty arguments array");

        reader.close();
    }

    /**
     * Tests the constructor with a single argument.
     * Verifies that single-element arrays work correctly.
     */
    @Test
    public void testConstructorWithSingleArgument() throws IOException {
        // Arrange
        String[] arguments = {"single"};

        // Act
        ArgumentWordReader reader = new ArgumentWordReader(arguments, null);

        // Assert
        String word = reader.nextWord(false, false);
        assertEquals("single", word, "Should read the single argument");

        String noMore = reader.nextWord(false, false);
        assertNull(noMore, "Should return null after single argument");

        reader.close();
    }

    /**
     * Tests the constructor with arguments containing spaces.
     * Verifies that arguments with spaces are handled correctly (they should be split).
     */
    @Test
    public void testConstructorWithArgumentsContainingSpaces() throws IOException {
        // Arrange
        String[] arguments = {"arg with spaces"};

        // Act
        ArgumentWordReader reader = new ArgumentWordReader(arguments, null);

        // Assert - the argument will be parsed, and spaces will cause it to be split
        String word1 = reader.nextWord(false, false);
        assertEquals("arg", word1, "First word should be 'arg'");

        String word2 = reader.nextWord(false, false);
        assertEquals("with", word2, "Second word should be 'with'");

        String word3 = reader.nextWord(false, false);
        assertEquals("spaces", word3, "Third word should be 'spaces'");

        reader.close();
    }

    /**
     * Tests the constructor with arguments containing quoted strings.
     * Verifies that quoted strings preserve spaces.
     */
    @Test
    public void testConstructorWithQuotedArguments() throws IOException {
        // Arrange
        String[] arguments = {"\"quoted string\"", "normal"};

        // Act
        ArgumentWordReader reader = new ArgumentWordReader(arguments, null);

        // Assert - quoted strings should preserve spaces
        String word1 = reader.nextWord(false, false);
        assertEquals("quoted string", word1, "Quoted string should preserve spaces");

        String word2 = reader.nextWord(false, false);
        assertEquals("normal", word2, "Normal argument should be read correctly");

        reader.close();
    }

    /**
     * Tests the constructor with arguments containing special delimiters.
     * Verifies that delimiters are handled correctly by the WordReader.
     */
    @Test
    public void testConstructorWithDelimiterCharacters() throws IOException {
        // Arrange
        String[] arguments = {"arg1,arg2", "arg3;arg4"};

        // Act
        ArgumentWordReader reader = new ArgumentWordReader(arguments, null);

        // Assert - delimiters should split the arguments
        String word1 = reader.nextWord(false, false);
        assertEquals("arg1", word1);

        String word2 = reader.nextWord(false, false);
        assertEquals(",", word2); // comma is a delimiter

        String word3 = reader.nextWord(false, false);
        assertEquals("arg2", word3);

        String word4 = reader.nextWord(false, false);
        assertEquals("arg3", word4);

        String word5 = reader.nextWord(false, false);
        assertEquals(";", word5); // semicolon is a delimiter

        String word6 = reader.nextWord(false, false);
        assertEquals("arg4", word6);

        reader.close();
    }

    /**
     * Tests the constructor with arguments containing option flags (starting with '-').
     * Verifies that option flags are recognized correctly.
     */
    @Test
    public void testConstructorWithOptionFlags() throws IOException {
        // Arrange
        String[] arguments = {"-option", "value", "-flag"};

        // Act
        ArgumentWordReader reader = new ArgumentWordReader(arguments, null);

        // Assert
        String word1 = reader.nextWord(false, false);
        assertEquals("-option", word1, "Option flag should be read correctly");

        String word2 = reader.nextWord(false, false);
        assertEquals("value", word2, "Value should be read correctly");

        String word3 = reader.nextWord(false, false);
        assertEquals("-flag", word3, "Flag should be read correctly");

        reader.close();
    }

    /**
     * Tests the constructor with a non-existent baseDir.
     * Verifies that the constructor accepts any File object regardless of existence.
     */
    @Test
    public void testConstructorWithNonExistentBaseDir() throws IOException {
        // Arrange
        String[] arguments = {"test"};
        File nonExistentDir = new File("/non/existent/path");

        // Act
        ArgumentWordReader reader = new ArgumentWordReader(arguments, nonExistentDir);

        // Assert - constructor should not validate baseDir existence
        assertNotNull(reader, "Reader should be created successfully");
        assertEquals(nonExistentDir, reader.getBaseDir(), "BaseDir should be stored even if non-existent");

        String word = reader.nextWord(false, false);
        assertEquals("test", word, "Should still read arguments correctly");

        reader.close();
    }

    /**
     * Tests the constructor with arguments array containing null elements.
     * Verifies behavior when array elements are null.
     */
    @Test
    public void testConstructorWithNullElementsInArray() throws IOException {
        // Arrange
        String[] arguments = {"arg1", null, "arg3"};

        // Act
        ArgumentWordReader reader = new ArgumentWordReader(arguments, null);

        // Assert
        String word1 = reader.nextWord(false, false);
        assertEquals("arg1", word1, "First argument should be read correctly");

        // The null element will cause nextLine() to return null, ending the reading
        String word2 = reader.nextWord(false, false);
        assertNull(word2, "Null element should end reading");

        reader.close();
    }

    /**
     * Tests the constructor with arguments containing comments.
     * Verifies that comments (lines starting with '#') are handled correctly.
     */
    @Test
    public void testConstructorWithCommentsInArguments() throws IOException {
        // Arrange
        String[] arguments = {"arg1", "# this is a comment", "arg2"};

        // Act
        ArgumentWordReader reader = new ArgumentWordReader(arguments, null);

        // Assert
        String word1 = reader.nextWord(false, false);
        assertEquals("arg1", word1, "First argument should be read");

        // The comment line should be skipped
        String word2 = reader.nextWord(false, false);
        assertEquals("arg2", word2, "Comment should be skipped, next arg read");

        reader.close();
    }

    /**
     * Tests that the constructor properly initializes so that lineLocationDescription works.
     * Verifies that the location description functionality is available after construction.
     */
    @Test
    public void testConstructorInitializesLocationDescription() throws IOException {
        // Arrange
        String[] arguments = {"test"};

        // Act
        ArgumentWordReader reader = new ArgumentWordReader(arguments, null);

        // Assert - location description should work after construction
        String location = reader.locationDescription();
        assertNotNull(location, "Location description should be available");

        reader.close();
    }

    /**
     * Tests the constructor with arguments containing special characters.
     * Verifies that special characters like braces and parentheses are handled.
     */
    @Test
    public void testConstructorWithSpecialCharacters() throws IOException {
        // Arrange
        String[] arguments = {"{", "}", "(", ")"};

        // Act
        ArgumentWordReader reader = new ArgumentWordReader(arguments, null);

        // Assert - these are delimiters and should be read as individual tokens
        String word1 = reader.nextWord(false, false);
        assertEquals("{", word1, "Opening brace should be read");

        String word2 = reader.nextWord(false, false);
        assertEquals("}", word2, "Closing brace should be read");

        String word3 = reader.nextWord(false, false);
        assertEquals("(", word3, "Opening parenthesis should be read");

        String word4 = reader.nextWord(false, false);
        assertEquals(")", word4, "Closing parenthesis should be read");

        reader.close();
    }

    /**
     * Tests that multiple readers can be constructed independently.
     * Verifies that each instance maintains its own state.
     */
    @Test
    public void testMultipleIndependentReaders() throws IOException {
        // Arrange
        String[] arguments1 = {"reader1", "data"};
        String[] arguments2 = {"reader2", "info"};

        // Act
        ArgumentWordReader reader1 = new ArgumentWordReader(arguments1, null);
        ArgumentWordReader reader2 = new ArgumentWordReader(arguments2, null);

        // Assert - each reader should maintain independent state
        String word1_1 = reader1.nextWord(false, false);
        String word2_1 = reader2.nextWord(false, false);

        assertEquals("reader1", word1_1, "Reader1 should read its first argument");
        assertEquals("reader2", word2_1, "Reader2 should read its first argument");

        String word1_2 = reader1.nextWord(false, false);
        String word2_2 = reader2.nextWord(false, false);

        assertEquals("data", word1_2, "Reader1 should read its second argument");
        assertEquals("info", word2_2, "Reader2 should read its second argument");

        reader1.close();
        reader2.close();
    }

    /**
     * Tests the constructor with arguments containing the @ character.
     * The @ character is a start delimiter in WordReader.
     */
    @Test
    public void testConstructorWithAtSymbol() throws IOException {
        // Arrange
        String[] arguments = {"@file", "normal"};

        // Act
        ArgumentWordReader reader = new ArgumentWordReader(arguments, null);

        // Assert - @ is a start delimiter
        String word1 = reader.nextWord(false, false);
        assertEquals("@", word1, "@ should be read as a delimiter");

        String word2 = reader.nextWord(false, false);
        assertEquals("file", word2, "Next word should be 'file'");

        String word3 = reader.nextWord(false, false);
        assertEquals("normal", word3, "Last word should be 'normal'");

        reader.close();
    }

    /**
     * Tests reading file names with the isFileName parameter.
     * Verifies that the constructor properly supports file name reading mode.
     */
    @Test
    public void testConstructorSupportsFileNameReading() throws IOException {
        // Arrange
        String[] arguments = {"file.txt", "-option"};

        // Act
        ArgumentWordReader reader = new ArgumentWordReader(arguments, null);

        // Assert - when isFileName=true, should read entire non-option as filename
        String fileName = reader.nextWord(true, true);
        assertEquals("file.txt", fileName, "Should read file name");

        String option = reader.nextWord(false, false);
        assertEquals("-option", option, "Should read option");

        reader.close();
    }

    /**
     * Tests that the constructor properly stores the arguments array reference.
     * Verifies that modifications to the original array don't affect already-read values
     * but do affect unread values.
     */
    @Test
    public void testConstructorStoresArgumentsArrayReference() throws IOException {
        // Arrange
        String[] arguments = {"arg1", "arg2", "arg3"};

        // Act
        ArgumentWordReader reader = new ArgumentWordReader(arguments, null);

        String word1 = reader.nextWord(false, false);
        assertEquals("arg1", word1, "First word should be read correctly");

        // Modify the original array
        arguments[1] = "modified";
        arguments[2] = "changed";

        // Assert - modifications should affect unread values
        String word2 = reader.nextWord(false, false);
        assertEquals("modified", word2, "Modified value should be read");

        String word3 = reader.nextWord(false, false);
        assertEquals("changed", word3, "Changed value should be read");

        reader.close();
    }
}
