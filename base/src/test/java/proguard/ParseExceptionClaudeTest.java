package proguard;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ParseException}.
 * Tests both constructors and verifies exception behavior.
 */
public class ParseExceptionClaudeTest {

    /**
     * Tests the no-argument constructor ParseException().
     * Verifies that the exception can be instantiated with null message.
     */
    @Test
    public void testNoArgConstructor() {
        // Act - Create ParseException with no arguments
        ParseException exception = new ParseException();

        // Assert - Verify the instance is not null and message is null
        assertNotNull(exception, "ParseException instance should not be null");
        assertNull(exception.getMessage(), "ParseException message should be null for no-arg constructor");
    }

    /**
     * Tests the String constructor ParseException(String).
     * Verifies that the exception stores the provided message correctly.
     */
    @Test
    public void testStringConstructor() {
        // Arrange
        String expectedMessage = "Test error message";

        // Act - Create ParseException with a message
        ParseException exception = new ParseException(expectedMessage);

        // Assert - Verify the instance is not null and message is correct
        assertNotNull(exception, "ParseException instance should not be null");
        assertEquals(expectedMessage, exception.getMessage(), "ParseException message should match the provided message");
    }

    /**
     * Tests the String constructor with null message.
     * Verifies that passing null to the constructor is handled correctly.
     */
    @Test
    public void testStringConstructorWithNull() {
        // Act - Create ParseException with null message
        ParseException exception = new ParseException(null);

        // Assert - Verify the instance is not null and message is null
        assertNotNull(exception, "ParseException instance should not be null");
        assertNull(exception.getMessage(), "ParseException message should be null when null is passed");
    }

    /**
     * Tests the String constructor with an empty string.
     * Verifies that empty strings are handled correctly.
     */
    @Test
    public void testStringConstructorWithEmptyString() {
        // Arrange
        String emptyMessage = "";

        // Act - Create ParseException with empty message
        ParseException exception = new ParseException(emptyMessage);

        // Assert - Verify the instance is not null and message is empty
        assertNotNull(exception, "ParseException instance should not be null");
        assertEquals(emptyMessage, exception.getMessage(), "ParseException message should be empty string");
        assertEquals("", exception.getMessage(), "ParseException message should be empty");
    }

    /**
     * Tests that ParseException extends Exception.
     * Verifies the inheritance hierarchy.
     */
    @Test
    public void testExceptionInheritance() {
        // Act - Create ParseException
        ParseException exception = new ParseException("test");

        // Assert - Verify it's an instance of Exception
        assertInstanceOf(Exception.class, exception, "ParseException should be an instance of Exception");
        assertInstanceOf(Throwable.class, exception, "ParseException should be an instance of Throwable");
    }

    /**
     * Tests that ParseException can be thrown and caught.
     * Verifies the exception can be used in typical exception handling scenarios.
     */
    @Test
    public void testExceptionCanBeThrown() {
        // Arrange
        String expectedMessage = "Parsing failed";

        // Act & Assert - Verify exception can be thrown and caught
        ParseException exception = assertThrows(ParseException.class, () -> {
            throw new ParseException(expectedMessage);
        }, "ParseException should be throwable");

        assertEquals(expectedMessage, exception.getMessage(), "Caught exception should have the correct message");
    }

    /**
     * Tests that ParseException with no-arg constructor can be thrown and caught.
     * Verifies the no-arg constructor exception can be used in typical exception handling scenarios.
     */
    @Test
    public void testNoArgExceptionCanBeThrown() {
        // Act & Assert - Verify no-arg exception can be thrown and caught
        ParseException exception = assertThrows(ParseException.class, () -> {
            throw new ParseException();
        }, "ParseException with no-arg constructor should be throwable");

        assertNull(exception.getMessage(), "Caught exception should have null message");
    }

    /**
     * Tests the String constructor with a multi-line message.
     * Verifies that complex messages are handled correctly.
     */
    @Test
    public void testStringConstructorWithMultilineMessage() {
        // Arrange
        String multilineMessage = "Error on line 1\nUnexpected token\nExpected '}'";

        // Act - Create ParseException with multiline message
        ParseException exception = new ParseException(multilineMessage);

        // Assert - Verify the message is stored correctly
        assertNotNull(exception, "ParseException instance should not be null");
        assertEquals(multilineMessage, exception.getMessage(), "ParseException should preserve multiline messages");
        assertTrue(exception.getMessage().contains("\n"), "Message should contain newline characters");
    }

    /**
     * Tests the String constructor with special characters.
     * Verifies that messages with special characters are handled correctly.
     */
    @Test
    public void testStringConstructorWithSpecialCharacters() {
        // Arrange
        String specialMessage = "Parse error: unexpected character '@' at position 5";

        // Act - Create ParseException with special characters
        ParseException exception = new ParseException(specialMessage);

        // Assert - Verify the message is stored correctly
        assertNotNull(exception, "ParseException instance should not be null");
        assertEquals(specialMessage, exception.getMessage(), "ParseException should preserve special characters");
        assertTrue(exception.getMessage().contains("@"), "Message should contain special characters");
    }

    /**
     * Tests that ParseException can be caught as a generic Exception.
     * Verifies polymorphic exception handling works correctly.
     */
    @Test
    public void testExceptionCanBeCaughtAsException() {
        // Arrange
        String expectedMessage = "Parse error";

        // Act & Assert - Verify ParseException can be caught as Exception
        Exception exception = assertThrows(Exception.class, () -> {
            throw new ParseException(expectedMessage);
        }, "ParseException should be catchable as Exception");

        assertInstanceOf(ParseException.class, exception, "Caught exception should be ParseException");
        assertEquals(expectedMessage, exception.getMessage(), "Message should be preserved when caught as Exception");
    }

    /**
     * Tests that multiple ParseException instances can be created independently.
     * Verifies that instances are independent and don't share state.
     */
    @Test
    public void testMultipleInstances() {
        // Act - Create multiple instances
        ParseException exception1 = new ParseException("Error 1");
        ParseException exception2 = new ParseException("Error 2");
        ParseException exception3 = new ParseException();

        // Assert - Verify all instances are independent
        assertNotNull(exception1, "First instance should not be null");
        assertNotNull(exception2, "Second instance should not be null");
        assertNotNull(exception3, "Third instance should not be null");

        assertEquals("Error 1", exception1.getMessage(), "First instance should have its own message");
        assertEquals("Error 2", exception2.getMessage(), "Second instance should have its own message");
        assertNull(exception3.getMessage(), "Third instance should have null message");

        assertNotSame(exception1, exception2, "Instances should be different objects");
        assertNotSame(exception2, exception3, "Instances should be different objects");
    }

    /**
     * Tests the String constructor with a very long message.
     * Verifies that long messages are handled correctly.
     */
    @Test
    public void testStringConstructorWithLongMessage() {
        // Arrange
        StringBuilder longMessage = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longMessage.append("Error ").append(i).append(" ");
        }
        String expectedMessage = longMessage.toString();

        // Act - Create ParseException with long message
        ParseException exception = new ParseException(expectedMessage);

        // Assert - Verify the long message is stored correctly
        assertNotNull(exception, "ParseException instance should not be null");
        assertEquals(expectedMessage, exception.getMessage(), "ParseException should preserve long messages");
        assertTrue(exception.getMessage().length() > 1000, "Message should be long");
    }

    /**
     * Tests that ParseException's toString() includes the message.
     * Verifies the exception's string representation.
     */
    @Test
    public void testToStringWithMessage() {
        // Arrange
        String message = "Parse failed at line 42";

        // Act
        ParseException exception = new ParseException(message);
        String result = exception.toString();

        // Assert - Verify toString contains class name and message
        assertNotNull(result, "toString should not return null");
        assertTrue(result.contains("ParseException"), "toString should contain class name");
        assertTrue(result.contains(message), "toString should contain the message");
    }

    /**
     * Tests that ParseException's toString() works with no-arg constructor.
     * Verifies the exception's string representation when message is null.
     */
    @Test
    public void testToStringWithNoMessage() {
        // Act
        ParseException exception = new ParseException();
        String result = exception.toString();

        // Assert - Verify toString contains class name
        assertNotNull(result, "toString should not return null");
        assertTrue(result.contains("ParseException"), "toString should contain class name");
    }
}
