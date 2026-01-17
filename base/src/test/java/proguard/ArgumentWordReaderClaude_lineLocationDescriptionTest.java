package proguard;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ArgumentWordReader#lineLocationDescription()} method.
 * Tests the line location description functionality which reports the current argument index.
 */
public class ArgumentWordReaderClaude_lineLocationDescriptionTest {

    /**
     * Tests that lineLocationDescription returns the correct format at initialization.
     * The initial index should be 0, so it should return "argument number 0".
     */
    @Test
    public void testLineLocationDescriptionAtInitialization() throws IOException {
        // Arrange
        String[] arguments = {"arg1", "arg2", "arg3"};
        ArgumentWordReader reader = new ArgumentWordReader(arguments, null);

        // Act
        String location = reader.locationDescription();

        // Assert
        // At initialization, before reading any arguments, index is 0
        assertTrue(location.contains("argument number 0"),
                   "Initial location should contain 'argument number 0', but was: " + location);

        reader.close();
    }

    /**
     * Tests that lineLocationDescription updates after reading the first argument.
     * After reading one argument, index should be 1.
     */
    @Test
    public void testLineLocationDescriptionAfterReadingFirstArgument() throws IOException {
        // Arrange
        String[] arguments = {"arg1", "arg2", "arg3"};
        ArgumentWordReader reader = new ArgumentWordReader(arguments, null);

        // Act
        reader.nextWord(false, false); // Read first argument
        String location = reader.locationDescription();

        // Assert
        // After reading one argument, index should be 1
        assertTrue(location.contains("argument number 1"),
                   "Location after reading first arg should contain 'argument number 1', but was: " + location);

        reader.close();
    }

    /**
     * Tests that lineLocationDescription updates correctly after reading multiple arguments.
     */
    @Test
    public void testLineLocationDescriptionAfterReadingMultipleArguments() throws IOException {
        // Arrange
        String[] arguments = {"arg1", "arg2", "arg3"};
        ArgumentWordReader reader = new ArgumentWordReader(arguments, null);

        // Act & Assert
        reader.nextWord(false, false); // Read arg1, index becomes 1
        assertTrue(reader.locationDescription().contains("argument number 1"));

        reader.nextWord(false, false); // Read arg2, index becomes 2
        assertTrue(reader.locationDescription().contains("argument number 2"));

        reader.nextWord(false, false); // Read arg3, index becomes 3
        assertTrue(reader.locationDescription().contains("argument number 3"));

        reader.close();
    }

    /**
     * Tests that lineLocationDescription works correctly after reading all arguments.
     * Even when there are no more arguments to read, the index reflects the position.
     */
    @Test
    public void testLineLocationDescriptionAfterExhaustingArguments() throws IOException {
        // Arrange
        String[] arguments = {"arg1", "arg2"};
        ArgumentWordReader reader = new ArgumentWordReader(arguments, null);

        // Act
        reader.nextWord(false, false); // Read arg1
        reader.nextWord(false, false); // Read arg2
        String word = reader.nextWord(false, false); // Try to read beyond

        // Assert
        assertNull(word, "Should return null when no more arguments");
        String location = reader.locationDescription();
        assertTrue(location.contains("argument number 2"),
                   "Location should still show last index position, but was: " + location);

        reader.close();
    }

    /**
     * Tests lineLocationDescription with an empty arguments array.
     * Even with no arguments, the location description should work.
     */
    @Test
    public void testLineLocationDescriptionWithEmptyArray() throws IOException {
        // Arrange
        String[] arguments = {};
        ArgumentWordReader reader = new ArgumentWordReader(arguments, null);

        // Act
        String location = reader.locationDescription();

        // Assert
        assertTrue(location.contains("argument number 0"),
                   "Empty array should show argument number 0, but was: " + location);

        reader.close();
    }

    /**
     * Tests lineLocationDescription with a single argument.
     * Verifies the index progression with a minimal array.
     */
    @Test
    public void testLineLocationDescriptionWithSingleArgument() throws IOException {
        // Arrange
        String[] arguments = {"single"};
        ArgumentWordReader reader = new ArgumentWordReader(arguments, null);

        // Act & Assert
        assertTrue(reader.locationDescription().contains("argument number 0"),
                   "Before reading, should be at argument number 0");

        reader.nextWord(false, false); // Read the single argument
        assertTrue(reader.locationDescription().contains("argument number 1"),
                   "After reading single argument, should be at argument number 1");

        reader.close();
    }

    /**
     * Tests that lineLocationDescription is included in error messages.
     * When an error occurs (e.g., missing closing quote), the location should be in the error message.
     */
    @Test
    public void testLineLocationDescriptionInErrorMessages() {
        // Arrange
        String[] arguments = {"\"unclosed quote"};
        ArgumentWordReader reader = new ArgumentWordReader(arguments, null);

        // Act & Assert
        IOException exception = assertThrows(IOException.class, () -> {
            reader.nextWord(false, false);
        }, "Should throw IOException for unclosed quote");

        String errorMessage = exception.getMessage();
        assertTrue(errorMessage.contains("argument number"),
                   "Error message should contain location description with 'argument number', but was: " + errorMessage);

        try {
            reader.close();
        } catch (IOException e) {
            // Ignore
        }
    }

    /**
     * Tests lineLocationDescription when reading multi-word arguments.
     * An argument with spaces will be split into multiple words, but the index
     * should only increment when moving to the next argument.
     */
    @Test
    public void testLineLocationDescriptionWithMultiWordArgument() throws IOException {
        // Arrange
        String[] arguments = {"word1 word2", "arg2"};
        ArgumentWordReader reader = new ArgumentWordReader(arguments, null);

        // Act & Assert
        // Initially at index 0
        assertTrue(reader.locationDescription().contains("argument number 0"));

        // Read first word from first argument
        String word1 = reader.nextWord(false, false);
        assertEquals("word1", word1);
        // Still reading from first argument, but index has moved to 1 after nextLine() was called
        assertTrue(reader.locationDescription().contains("argument number 1"));

        // Read second word from same argument (no index change as we're still on same line)
        String word2 = reader.nextWord(false, false);
        assertEquals("word2", word2);
        assertTrue(reader.locationDescription().contains("argument number 1"));

        // Read from second argument
        String word3 = reader.nextWord(false, false);
        assertEquals("arg2", word3);
        // Now we've moved to second argument, index is 2
        assertTrue(reader.locationDescription().contains("argument number 2"));

        reader.close();
    }

    /**
     * Tests lineLocationDescription format and content.
     * Verifies the exact format matches the expected pattern.
     */
    @Test
    public void testLineLocationDescriptionFormat() throws IOException {
        // Arrange
        String[] arguments = {"test"};
        ArgumentWordReader reader = new ArgumentWordReader(arguments, null);

        // Act
        String location = reader.locationDescription();

        // Assert
        // The format should be "argument number X" where X is the index
        assertTrue(location.matches(".*argument number \\d+.*"),
                   "Location should match pattern 'argument number <number>', but was: " + location);

        reader.close();
    }

    /**
     * Tests that lineLocationDescription works consistently across multiple reads and resets.
     */
    @Test
    public void testLineLocationDescriptionConsistency() throws IOException {
        // Arrange
        String[] arguments = {"a", "b", "c", "d", "e"};
        ArgumentWordReader reader = new ArgumentWordReader(arguments, null);

        // Act & Assert - verify index increments predictably
        for (int i = 0; i < arguments.length; i++) {
            String word = reader.nextWord(false, false);
            assertNotNull(word, "Should read word at index " + i);

            String location = reader.locationDescription();
            int expectedIndex = i + 1; // Index increments after reading
            assertTrue(location.contains("argument number " + expectedIndex),
                       "After reading " + (i + 1) + " argument(s), location should show index " + expectedIndex);
        }

        reader.close();
    }

    /**
     * Tests lineLocationDescription with arguments containing special characters.
     * The location description should work regardless of argument content.
     */
    @Test
    public void testLineLocationDescriptionWithSpecialCharacters() throws IOException {
        // Arrange
        String[] arguments = {"{}", "()", "@@", "##"};
        ArgumentWordReader reader = new ArgumentWordReader(arguments, null);

        // Act - read a few words (special chars may be split into multiple words)
        reader.nextWord(false, false);
        String location = reader.locationDescription();

        // Assert
        assertTrue(location.contains("argument number"),
                   "Location description should work with special characters, but was: " + location);

        reader.close();
    }

    /**
     * Tests that lineLocationDescription is visible through the public locationDescription() method.
     * This is the primary way users interact with the location information.
     */
    @Test
    public void testLineLocationDescriptionAccessibleViaLocationDescription() throws IOException {
        // Arrange
        String[] arguments = {"test1", "test2"};
        ArgumentWordReader reader = new ArgumentWordReader(arguments, null);

        // Act
        reader.nextWord(false, false); // Read first argument
        String publicLocation = reader.locationDescription();

        // Assert
        // The public locationDescription() method should include the lineLocationDescription() output
        assertNotNull(publicLocation, "locationDescription() should not return null");
        assertTrue(publicLocation.contains("argument number"),
                   "Public locationDescription() should contain lineLocationDescription() content");

        reader.close();
    }

    /**
     * Tests lineLocationDescription when reading with fileName mode enabled.
     * The index behavior should be the same regardless of reading mode.
     */
    @Test
    public void testLineLocationDescriptionWithFileNameMode() throws IOException {
        // Arrange
        String[] arguments = {"file.txt", "-option", "value"};
        ArgumentWordReader reader = new ArgumentWordReader(arguments, null);

        // Act
        reader.nextWord(true, true); // Read as filename
        String location1 = reader.locationDescription();

        reader.nextWord(false, false); // Read as option
        String location2 = reader.locationDescription();

        // Assert
        assertTrue(location1.contains("argument number 1"),
                   "After reading filename, location should show index 1");
        assertTrue(location2.contains("argument number 2"),
                   "After reading option, location should show index 2");

        reader.close();
    }

    /**
     * Tests that lineLocationDescription reflects the correct state during error conditions.
     */
    @Test
    public void testLineLocationDescriptionDuringErrorRecovery() throws IOException {
        // Arrange
        String[] arguments = {"valid", "\"unclosed", "another"};
        ArgumentWordReader reader = new ArgumentWordReader(arguments, null);

        // Act
        reader.nextWord(false, false); // Read "valid"
        String locationBeforeError = reader.locationDescription();

        // Assert
        assertTrue(locationBeforeError.contains("argument number 1"),
                   "Before error, should be at argument number 1");

        // Try to read unclosed quote - this will throw an exception
        try {
            reader.nextWord(false, false);
            fail("Should have thrown IOException for unclosed quote");
        } catch (IOException e) {
            // Expected - verify the location is in the error message
            assertTrue(e.getMessage().contains("argument number"),
                       "Error message should include location description");
        }

        try {
            reader.close();
        } catch (IOException e) {
            // Ignore
        }
    }
}
