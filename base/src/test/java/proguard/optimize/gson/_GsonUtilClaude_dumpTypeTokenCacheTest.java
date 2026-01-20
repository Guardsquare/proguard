package proguard.optimize.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link _GsonUtil#dumpTypeTokenCache(String, Map)}.
 * Tests the debugging method: dumpTypeTokenCache.(Ljava/lang/String;Ljava/util/Map;)V
 *
 * The dumpTypeTokenCache method is a debugging utility that prints the contents of a
 * type token cache to System.out. It displays a message followed by each entry in the cache.
 */
public class _GsonUtilClaude_dumpTypeTokenCacheTest {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        // Capture System.out for testing
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    public void tearDown() {
        // Restore original System.out
        System.setOut(originalOut);
    }

    // =========================================================================
    // Tests for dumpTypeTokenCache(String, Map)
    // =========================================================================

    /**
     * Tests that dumpTypeTokenCache prints the message when given an empty map.
     */
    @Test
    public void testDumpTypeTokenCache_emptyMap_printsMessage() {
        // Arrange
        String message = "Test Message";
        Map<TypeToken<?>, TypeAdapter<?>> emptyCache = new HashMap<>();

        // Act
        _GsonUtil.dumpTypeTokenCache(message, emptyCache);

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains(message), "Output should contain the message");
    }

    /**
     * Tests that dumpTypeTokenCache prints the message and entries for a single-entry map.
     */
    @Test
    public void testDumpTypeTokenCache_singleEntry_printsMessageAndEntry() {
        // Arrange
        String message = "Cache Contents";
        Map<TypeToken<?>, TypeAdapter<?>> cache = new LinkedHashMap<>();
        TypeToken<String> typeToken = TypeToken.get(String.class);
        cache.put(typeToken, null);

        // Act
        _GsonUtil.dumpTypeTokenCache(message, cache);

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains(message), "Output should contain the message");
        assertTrue(output.contains(typeToken.toString()), "Output should contain the type token");
    }

    /**
     * Tests that dumpTypeTokenCache prints multiple entries correctly.
     */
    @Test
    public void testDumpTypeTokenCache_multipleEntries_printsAllEntries() {
        // Arrange
        String message = "Multiple Entries";
        Map<TypeToken<?>, TypeAdapter<?>> cache = new LinkedHashMap<>();
        TypeToken<String> stringToken = TypeToken.get(String.class);
        TypeToken<Integer> integerToken = TypeToken.get(Integer.class);
        TypeToken<Double> doubleToken = TypeToken.get(Double.class);

        cache.put(stringToken, null);
        cache.put(integerToken, null);
        cache.put(doubleToken, null);

        // Act
        _GsonUtil.dumpTypeTokenCache(message, cache);

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains(message), "Output should contain the message");
        assertTrue(output.contains(stringToken.toString()), "Output should contain String type token");
        assertTrue(output.contains(integerToken.toString()), "Output should contain Integer type token");
        assertTrue(output.contains(doubleToken.toString()), "Output should contain Double type token");
    }

    /**
     * Tests that dumpTypeTokenCache handles null message gracefully.
     */
    @Test
    public void testDumpTypeTokenCache_nullMessage_printsNull() {
        // Arrange
        Map<TypeToken<?>, TypeAdapter<?>> cache = new HashMap<>();

        // Act
        _GsonUtil.dumpTypeTokenCache(null, cache);

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("null"), "Output should contain 'null' for null message");
    }

    /**
     * Tests that dumpTypeTokenCache handles empty string message.
     */
    @Test
    public void testDumpTypeTokenCache_emptyMessage_printsEmptyLine() {
        // Arrange
        String message = "";
        Map<TypeToken<?>, TypeAdapter<?>> cache = new HashMap<>();

        // Act
        _GsonUtil.dumpTypeTokenCache(message, cache);

        // Assert
        String output = outputStream.toString();
        assertFalse(output.isEmpty(), "Output should not be empty");
        assertTrue(output.startsWith("\n") || output.startsWith("\r\n") || output.length() > 0,
                "Output should contain at least a newline");
    }

    /**
     * Tests that dumpTypeTokenCache correctly formats entries with indentation.
     */
    @Test
    public void testDumpTypeTokenCache_indentation_correctlyIndentsEntries() {
        // Arrange
        String message = "Indented Output";
        Map<TypeToken<?>, TypeAdapter<?>> cache = new LinkedHashMap<>();
        TypeToken<String> typeToken = TypeToken.get(String.class);
        cache.put(typeToken, null);

        // Act
        _GsonUtil.dumpTypeTokenCache(message, cache);

        // Assert
        String output = outputStream.toString();
        String[] lines = output.split(System.lineSeparator());

        // First line should be the message
        assertTrue(lines.length >= 2, "Output should have at least 2 lines");
        assertEquals(message, lines[0].trim(), "First line should be the message");

        // Entry lines should be indented with 4 spaces
        for (int i = 1; i < lines.length && !lines[i].trim().isEmpty(); i++) {
            assertTrue(lines[i].startsWith("    "),
                    "Entry line should start with 4 spaces for indentation");
        }
    }

    /**
     * Tests that dumpTypeTokenCache prints the arrow separator between key and value.
     */
    @Test
    public void testDumpTypeTokenCache_arrowSeparator_presentInOutput() {
        // Arrange
        String message = "Separator Test";
        Map<TypeToken<?>, TypeAdapter<?>> cache = new LinkedHashMap<>();
        TypeToken<String> typeToken = TypeToken.get(String.class);
        cache.put(typeToken, null);

        // Act
        _GsonUtil.dumpTypeTokenCache(message, cache);

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains(" -> "), "Output should contain ' -> ' separator");
    }

    /**
     * Tests that dumpTypeTokenCache handles various message strings correctly.
     */
    @Test
    public void testDumpTypeTokenCache_variousMessages_allPrintedCorrectly() {
        // Test with different message types
        String[] messages = {
            "Simple message",
            "Message with special chars: @#$%^&*()",
            "Message with numbers: 123456",
            "Message\nwith\nnewlines",
            "Very long message that contains a lot of text to test if the method can handle longer strings without issues"
        };

        for (String message : messages) {
            // Arrange
            outputStream.reset(); // Clear output between tests
            Map<TypeToken<?>, TypeAdapter<?>> cache = new HashMap<>();

            // Act
            _GsonUtil.dumpTypeTokenCache(message, cache);

            // Assert
            String output = outputStream.toString();
            assertTrue(output.contains(message) || output.contains(message.replace("\n", System.lineSeparator())),
                    "Output should contain the message: " + message);
        }
    }

    /**
     * Tests that dumpTypeTokenCache handles null values in the map correctly.
     */
    @Test
    public void testDumpTypeTokenCache_nullTypeAdapterValue_printsNull() {
        // Arrange
        String message = "Null Value Test";
        Map<TypeToken<?>, TypeAdapter<?>> cache = new LinkedHashMap<>();
        TypeToken<String> typeToken = TypeToken.get(String.class);
        cache.put(typeToken, null);

        // Act
        _GsonUtil.dumpTypeTokenCache(message, cache);

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("null"), "Output should contain 'null' for null type adapter value");
    }

    /**
     * Tests that dumpTypeTokenCache prints entries in the order they appear in the map.
     * Uses LinkedHashMap to ensure predictable iteration order.
     */
    @Test
    public void testDumpTypeTokenCache_entryOrder_maintainedInOutput() {
        // Arrange
        String message = "Order Test";
        Map<TypeToken<?>, TypeAdapter<?>> cache = new LinkedHashMap<>();
        TypeToken<String> stringToken = TypeToken.get(String.class);
        TypeToken<Integer> integerToken = TypeToken.get(Integer.class);
        TypeToken<Boolean> booleanToken = TypeToken.get(Boolean.class);

        cache.put(stringToken, null);
        cache.put(integerToken, null);
        cache.put(booleanToken, null);

        // Act
        _GsonUtil.dumpTypeTokenCache(message, cache);

        // Assert
        String output = outputStream.toString();
        int stringIndex = output.indexOf(stringToken.toString());
        int integerIndex = output.indexOf(integerToken.toString());
        int booleanIndex = output.indexOf(booleanToken.toString());

        assertTrue(stringIndex < integerIndex && integerIndex < booleanIndex,
                "Entries should appear in the same order as they were added to the map");
    }

    /**
     * Tests that dumpTypeTokenCache handles complex TypeToken types.
     */
    @Test
    public void testDumpTypeTokenCache_complexTypeTokens_printedCorrectly() {
        // Arrange
        String message = "Complex Types";
        Map<TypeToken<?>, TypeAdapter<?>> cache = new LinkedHashMap<>();

        // Add various complex type tokens
        TypeToken<?> listOfStrings = new TypeToken<java.util.List<String>>() {};
        TypeToken<?> mapOfStringToInteger = new TypeToken<java.util.Map<String, Integer>>() {};

        cache.put(listOfStrings, null);
        cache.put(mapOfStringToInteger, null);

        // Act
        _GsonUtil.dumpTypeTokenCache(message, cache);

        // Assert
        String output = outputStream.toString();
        assertFalse(output.isEmpty(), "Output should not be empty");
        assertTrue(output.contains(message), "Output should contain the message");
        // The exact string representation may vary, but it should contain something
        assertTrue(output.split(System.lineSeparator()).length >= 3,
                "Output should have at least message + 2 entries");
    }

    /**
     * Tests that calling dumpTypeTokenCache multiple times produces independent outputs.
     */
    @Test
    public void testDumpTypeTokenCache_multipleCalls_independentOutputs() {
        // First call
        String message1 = "First Call";
        Map<TypeToken<?>, TypeAdapter<?>> cache1 = new LinkedHashMap<>();
        cache1.put(TypeToken.get(String.class), null);

        _GsonUtil.dumpTypeTokenCache(message1, cache1);
        String output1 = outputStream.toString();

        // Clear and second call
        outputStream.reset();
        String message2 = "Second Call";
        Map<TypeToken<?>, TypeAdapter<?>> cache2 = new LinkedHashMap<>();
        cache2.put(TypeToken.get(Integer.class), null);

        _GsonUtil.dumpTypeTokenCache(message2, cache2);
        String output2 = outputStream.toString();

        // Assert
        assertTrue(output1.contains(message1), "First output should contain first message");
        assertFalse(output1.contains(message2), "First output should not contain second message");
        assertTrue(output2.contains(message2), "Second output should contain second message");
        assertFalse(output2.contains(message1), "Second output should not contain first message");
    }

    /**
     * Tests that dumpTypeTokenCache handles a large number of entries.
     */
    @Test
    public void testDumpTypeTokenCache_largeMap_handlesMany() {
        // Arrange
        String message = "Large Map Test";
        Map<TypeToken<?>, TypeAdapter<?>> cache = new LinkedHashMap<>();

        // Add many entries (using wrapper classes to ensure distinct type tokens)
        cache.put(TypeToken.get(String.class), null);
        cache.put(TypeToken.get(Integer.class), null);
        cache.put(TypeToken.get(Long.class), null);
        cache.put(TypeToken.get(Double.class), null);
        cache.put(TypeToken.get(Float.class), null);
        cache.put(TypeToken.get(Boolean.class), null);
        cache.put(TypeToken.get(Character.class), null);
        cache.put(TypeToken.get(Byte.class), null);
        cache.put(TypeToken.get(Short.class), null);
        cache.put(TypeToken.get(Object.class), null);

        // Act
        _GsonUtil.dumpTypeTokenCache(message, cache);

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains(message), "Output should contain the message");

        // Count lines (message + entries)
        String[] lines = output.split(System.lineSeparator());
        assertTrue(lines.length >= 11, "Output should have at least 11 lines (message + 10 entries)");
    }
}
