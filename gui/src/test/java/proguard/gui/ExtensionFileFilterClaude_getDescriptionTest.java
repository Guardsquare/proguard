package proguard.gui;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ExtensionFileFilter.getDescription() method.
 *
 * The getDescription() method (line 53-56) performs the following operation:
 * - Line 55: Returns the description field that was set in the constructor
 *
 * These tests verify:
 * - The method returns the exact description string passed to the constructor
 * - The method handles null descriptions correctly
 * - The method handles empty descriptions correctly
 * - The method returns descriptions with special characters
 * - The method returns descriptions with Unicode characters
 * - The description is immutable (multiple calls return the same value)
 */
public class ExtensionFileFilterClaude_getDescriptionTest {

    /**
     * Test getDescription() with a simple description.
     * This verifies line 55: returns the description field.
     */
    @Test
    public void testGetDescriptionWithSimpleDescription() {
        String description = "Java Files";
        String[] extensions = {".java"};

        ExtensionFileFilter filter = new ExtensionFileFilter(description, extensions);

        assertEquals(description, filter.getDescription(),
                     "getDescription() should return the description passed to constructor");
    }

    /**
     * Test getDescription() with null description.
     * Verifies the method handles and returns null correctly.
     */
    @Test
    public void testGetDescriptionWithNullDescription() {
        String[] extensions = {".txt"};

        ExtensionFileFilter filter = new ExtensionFileFilter(null, extensions);

        assertNull(filter.getDescription(),
                   "getDescription() should return null when null was passed to constructor");
    }

    /**
     * Test getDescription() with empty string description.
     * Verifies the method returns empty string correctly.
     */
    @Test
    public void testGetDescriptionWithEmptyDescription() {
        String description = "";
        String[] extensions = {".txt"};

        ExtensionFileFilter filter = new ExtensionFileFilter(description, extensions);

        assertEquals("", filter.getDescription(),
                     "getDescription() should return empty string when empty string was passed");
    }

    /**
     * Test getDescription() with long description.
     * Verifies the method handles lengthy descriptions correctly.
     */
    @Test
    public void testGetDescriptionWithLongDescription() {
        String description = "This is a very long description for a file filter that " +
                           "accepts multiple types of files with various extensions " +
                           "and is used to demonstrate that the getDescription method " +
                           "properly returns long strings without truncation or errors";
        String[] extensions = {".txt"};

        ExtensionFileFilter filter = new ExtensionFileFilter(description, extensions);

        assertEquals(description, filter.getDescription(),
                     "getDescription() should return full long description");
    }

    /**
     * Test getDescription() with special characters.
     * Verifies the method handles special characters correctly.
     */
    @Test
    public void testGetDescriptionWithSpecialCharacters() {
        String description = "Files (*.txt, *.doc) & Others - [Special]";
        String[] extensions = {".txt", ".doc"};

        ExtensionFileFilter filter = new ExtensionFileFilter(description, extensions);

        assertEquals(description, filter.getDescription(),
                     "getDescription() should return description with special characters");
    }

    /**
     * Test getDescription() with Unicode characters.
     * Verifies the method handles Unicode correctly.
     */
    @Test
    public void testGetDescriptionWithUnicodeCharacters() {
        String description = "Fichiers texte (文本文件) - テキストファイル";
        String[] extensions = {".txt"};

        ExtensionFileFilter filter = new ExtensionFileFilter(description, extensions);

        assertEquals(description, filter.getDescription(),
                     "getDescription() should return description with Unicode characters");
    }

    /**
     * Test getDescription() with whitespace.
     * Verifies the method handles descriptions with leading/trailing whitespace.
     */
    @Test
    public void testGetDescriptionWithWhitespace() {
        String description = "  Text Files  ";
        String[] extensions = {".txt"};

        ExtensionFileFilter filter = new ExtensionFileFilter(description, extensions);

        assertEquals(description, filter.getDescription(),
                     "getDescription() should return description preserving whitespace");
    }

    /**
     * Test getDescription() with newlines and tabs.
     * Verifies the method handles multi-line descriptions.
     */
    @Test
    public void testGetDescriptionWithNewlinesAndTabs() {
        String description = "Text Files\n\tSupported formats";
        String[] extensions = {".txt"};

        ExtensionFileFilter filter = new ExtensionFileFilter(description, extensions);

        assertEquals(description, filter.getDescription(),
                     "getDescription() should return description with newlines and tabs");
    }

    /**
     * Test getDescription() multiple times.
     * Verifies that multiple calls return the same value (immutability).
     */
    @Test
    public void testGetDescriptionMultipleCalls() {
        String description = "Archive Files";
        String[] extensions = {".zip"};

        ExtensionFileFilter filter = new ExtensionFileFilter(description, extensions);

        String firstCall = filter.getDescription();
        String secondCall = filter.getDescription();
        String thirdCall = filter.getDescription();

        assertEquals(description, firstCall,
                     "First call should return correct description");
        assertEquals(description, secondCall,
                     "Second call should return correct description");
        assertEquals(description, thirdCall,
                     "Third call should return correct description");
        assertSame(firstCall, secondCall,
                   "Multiple calls should return the same object");
        assertSame(secondCall, thirdCall,
                   "Multiple calls should return the same object");
    }

    /**
     * Test getDescription() with mixed case.
     * Verifies the method preserves case.
     */
    @Test
    public void testGetDescriptionWithMixedCase() {
        String description = "TeXt FiLeS";
        String[] extensions = {".txt"};

        ExtensionFileFilter filter = new ExtensionFileFilter(description, extensions);

        assertEquals(description, filter.getDescription(),
                     "getDescription() should preserve case");
    }

    /**
     * Test getDescription() with numeric characters.
     * Verifies the method handles numeric characters in description.
     */
    @Test
    public void testGetDescriptionWithNumericCharacters() {
        String description = "Version 2.0 Files (2023)";
        String[] extensions = {".v2"};

        ExtensionFileFilter filter = new ExtensionFileFilter(description, extensions);

        assertEquals(description, filter.getDescription(),
                     "getDescription() should return description with numbers");
    }

    /**
     * Test getDescription() with single character.
     * Verifies the method handles single-character descriptions.
     */
    @Test
    public void testGetDescriptionWithSingleCharacter() {
        String description = "X";
        String[] extensions = {".x"};

        ExtensionFileFilter filter = new ExtensionFileFilter(description, extensions);

        assertEquals(description, filter.getDescription(),
                     "getDescription() should return single-character description");
    }

    /**
     * Test getDescription() is independent across instances.
     * Verifies that each filter instance has its own description.
     */
    @Test
    public void testGetDescriptionIndependentInstances() {
        String desc1 = "Description 1";
        String desc2 = "Description 2";
        String[] ext1 = {".txt"};
        String[] ext2 = {".java"};

        ExtensionFileFilter filter1 = new ExtensionFileFilter(desc1, ext1);
        ExtensionFileFilter filter2 = new ExtensionFileFilter(desc2, ext2);

        assertEquals(desc1, filter1.getDescription(),
                     "Filter 1 should return its own description");
        assertEquals(desc2, filter2.getDescription(),
                     "Filter 2 should return its own description");
        assertNotEquals(filter1.getDescription(), filter2.getDescription(),
                        "Different filters should have different descriptions");
    }

    /**
     * Test getDescription() with only spaces.
     * Verifies the method handles space-only descriptions.
     */
    @Test
    public void testGetDescriptionWithOnlySpaces() {
        String description = "     ";
        String[] extensions = {".txt"};

        ExtensionFileFilter filter = new ExtensionFileFilter(description, extensions);

        assertEquals(description, filter.getDescription(),
                     "getDescription() should return space-only description");
    }

    /**
     * Test getDescription() with quotes.
     * Verifies the method handles quoted strings.
     */
    @Test
    public void testGetDescriptionWithQuotes() {
        String description = "\"Quoted\" Files";
        String[] extensions = {".txt"};

        ExtensionFileFilter filter = new ExtensionFileFilter(description, extensions);

        assertEquals(description, filter.getDescription(),
                     "getDescription() should return description with quotes");
    }

    /**
     * Test getDescription() with backslashes.
     * Verifies the method handles backslashes correctly.
     */
    @Test
    public void testGetDescriptionWithBackslashes() {
        String description = "Path\\To\\Files";
        String[] extensions = {".txt"};

        ExtensionFileFilter filter = new ExtensionFileFilter(description, extensions);

        assertEquals(description, filter.getDescription(),
                     "getDescription() should return description with backslashes");
    }

    /**
     * Test getDescription() with HTML-like content.
     * Verifies the method handles HTML tags (not interpreted, just stored).
     */
    @Test
    public void testGetDescriptionWithHtmlLikeContent() {
        String description = "<b>Bold</b> Files";
        String[] extensions = {".html"};

        ExtensionFileFilter filter = new ExtensionFileFilter(description, extensions);

        assertEquals(description, filter.getDescription(),
                     "getDescription() should return description with HTML-like content");
    }

    /**
     * Test getDescription() with emoji characters.
     * Verifies the method handles emoji correctly.
     */
    @Test
    public void testGetDescriptionWithEmoji() {
        String description = "📄 Document Files 📁";
        String[] extensions = {".doc"};

        ExtensionFileFilter filter = new ExtensionFileFilter(description, extensions);

        assertEquals(description, filter.getDescription(),
                     "getDescription() should return description with emoji");
    }

    /**
     * Test getDescription() with very long single word.
     * Verifies the method handles long unbroken strings.
     */
    @Test
    public void testGetDescriptionWithVeryLongWord() {
        String description = "Averylongdescriptionwithoutanyspacesorbreakstotestthatthemethodcanhandlelongunbrokenstrings";
        String[] extensions = {".txt"};

        ExtensionFileFilter filter = new ExtensionFileFilter(description, extensions);

        assertEquals(description, filter.getDescription(),
                     "getDescription() should return very long single-word description");
    }

    /**
     * Test getDescription() with equals sign.
     * Verifies the method handles equals signs in description.
     */
    @Test
    public void testGetDescriptionWithEqualsSign() {
        String description = "Config Files (key=value)";
        String[] extensions = {".cfg"};

        ExtensionFileFilter filter = new ExtensionFileFilter(description, extensions);

        assertEquals(description, filter.getDescription(),
                     "getDescription() should return description with equals sign");
    }

    /**
     * Test getDescription() doesn't modify the returned value.
     * Verifies that the description is stored correctly and not computed.
     */
    @Test
    public void testGetDescriptionNotModified() {
        String description = "Original Description";
        String[] extensions = {".txt"};

        ExtensionFileFilter filter = new ExtensionFileFilter(description, extensions);

        String retrieved = filter.getDescription();
        assertEquals(description, retrieved,
                     "Retrieved description should match original");
        assertEquals(description, filter.getDescription(),
                     "Second retrieval should still match original");
    }

    /**
     * Test getDescription() with description containing file extensions.
     * Verifies that description can contain text that looks like extensions.
     */
    @Test
    public void testGetDescriptionContainingExtensions() {
        String description = "Text files (.txt and .text)";
        String[] extensions = {".txt", ".text"};

        ExtensionFileFilter filter = new ExtensionFileFilter(description, extensions);

        assertEquals(description, filter.getDescription(),
                     "getDescription() should return description containing extension-like text");
    }

    /**
     * Test getDescription() after multiple accept() calls.
     * Verifies that calling other methods doesn't affect getDescription().
     */
    @Test
    public void testGetDescriptionAfterOtherMethodCalls() {
        String description = "Test Files";
        String[] extensions = {".test"};

        ExtensionFileFilter filter = new ExtensionFileFilter(description, extensions);

        // Call accept() multiple times (shouldn't affect description)
        java.io.File file = new java.io.File("test.test");
        filter.accept(file);
        filter.accept(file);
        filter.accept(file);

        assertEquals(description, filter.getDescription(),
                     "getDescription() should return same value after accept() calls");
    }

    /**
     * Test getDescription() with description matching common file filter patterns.
     * Verifies realistic use cases.
     */
    @Test
    public void testGetDescriptionWithRealisticDescriptions() {
        String[] descriptions = {
            "All Files (*.*)",
            "Java Source Files (*.java)",
            "Archive Files (*.jar, *.zip, *.war)",
            "Image Files",
            "Configuration Files"
        };

        for (String desc : descriptions) {
            String[] extensions = {".test"};
            ExtensionFileFilter filter = new ExtensionFileFilter(desc, extensions);

            assertEquals(desc, filter.getDescription(),
                         "getDescription() should return realistic description: " + desc);
        }
    }

    /**
     * Test getDescription() with description containing parentheses.
     * Verifies handling of parentheses which are common in file filter descriptions.
     */
    @Test
    public void testGetDescriptionWithParentheses() {
        String description = "Text Files (Plain Text)";
        String[] extensions = {".txt"};

        ExtensionFileFilter filter = new ExtensionFileFilter(description, extensions);

        assertEquals(description, filter.getDescription(),
                     "getDescription() should return description with parentheses");
    }
}
