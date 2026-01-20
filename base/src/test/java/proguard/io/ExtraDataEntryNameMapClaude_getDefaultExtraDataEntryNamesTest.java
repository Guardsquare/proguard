package proguard.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ExtraDataEntryNameMap.getDefaultExtraDataEntryNames() method.
 */
public class ExtraDataEntryNameMapClaude_getDefaultExtraDataEntryNamesTest {

    private ExtraDataEntryNameMap map;

    @BeforeEach
    public void setUp() {
        map = new ExtraDataEntryNameMap();
    }

    /**
     * Test that getDefaultExtraDataEntryNames returns null for an empty map.
     */
    @Test
    public void testGetDefaultExtraDataEntryNamesOnEmptyMap() {
        Set<String> defaultEntries = map.getDefaultExtraDataEntryNames();

        assertNull(defaultEntries, "Default extra data entry names should be null for empty map");
    }

    /**
     * Test that getDefaultExtraDataEntryNames returns a single entry after adding one default entry.
     */
    @Test
    public void testGetDefaultExtraDataEntryNamesWithSingleEntry() {
        String entryName = "default.txt";
        map.addExtraDataEntry(entryName);

        Set<String> defaultEntries = map.getDefaultExtraDataEntryNames();

        assertNotNull(defaultEntries, "Default entries should not be null");
        assertEquals(1, defaultEntries.size(), "Should have exactly 1 default entry");
        assertTrue(defaultEntries.contains(entryName), "Should contain the added entry");
    }

    /**
     * Test that getDefaultExtraDataEntryNames returns multiple entries after adding several.
     */
    @Test
    public void testGetDefaultExtraDataEntryNamesWithMultipleEntries() {
        map.addExtraDataEntry("entry1.txt");
        map.addExtraDataEntry("entry2.xml");
        map.addExtraDataEntry("entry3.json");

        Set<String> defaultEntries = map.getDefaultExtraDataEntryNames();

        assertNotNull(defaultEntries, "Default entries should not be null");
        assertEquals(3, defaultEntries.size(), "Should have 3 default entries");
        assertTrue(defaultEntries.contains("entry1.txt"), "Should contain entry1.txt");
        assertTrue(defaultEntries.contains("entry2.xml"), "Should contain entry2.xml");
        assertTrue(defaultEntries.contains("entry3.json"), "Should contain entry3.json");
    }

    /**
     * Test that getDefaultExtraDataEntryNames only returns default entries, not keyed entries.
     */
    @Test
    public void testGetDefaultExtraDataEntryNamesExcludesKeyedEntries() {
        map.addExtraDataEntry("default.txt");
        map.addExtraDataEntry("key1.class", "keyed1.txt");
        map.addExtraDataEntry("key2.class", "keyed2.txt");

        Set<String> defaultEntries = map.getDefaultExtraDataEntryNames();

        assertNotNull(defaultEntries, "Default entries should not be null");
        assertEquals(1, defaultEntries.size(), "Should have exactly 1 default entry");
        assertTrue(defaultEntries.contains("default.txt"), "Should contain default entry");
        assertFalse(defaultEntries.contains("keyed1.txt"), "Should not contain keyed entry");
        assertFalse(defaultEntries.contains("keyed2.txt"), "Should not contain keyed entry");
    }

    /**
     * Test that getDefaultExtraDataEntryNames includes class entries added via addExtraClass.
     */
    @Test
    public void testGetDefaultExtraDataEntryNamesWithExtraClasses() {
        map.addExtraClass("com/example/Class1");
        map.addExtraClass("com/example/Class2");

        Set<String> defaultEntries = map.getDefaultExtraDataEntryNames();

        assertNotNull(defaultEntries, "Default entries should not be null");
        assertEquals(2, defaultEntries.size(), "Should have 2 default entries");
        assertTrue(defaultEntries.contains("com/example/Class1.class"),
                "Should contain Class1 with .class extension");
        assertTrue(defaultEntries.contains("com/example/Class2.class"),
                "Should contain Class2 with .class extension");
    }

    /**
     * Test that getDefaultExtraDataEntryNames includes both regular and class entries.
     */
    @Test
    public void testGetDefaultExtraDataEntryNamesWithMixedEntries() {
        map.addExtraDataEntry("config.xml");
        map.addExtraClass("com/example/TestClass");
        map.addExtraDataEntry("data.json");

        Set<String> defaultEntries = map.getDefaultExtraDataEntryNames();

        assertNotNull(defaultEntries, "Default entries should not be null");
        assertEquals(3, defaultEntries.size(), "Should have 3 default entries");
        assertTrue(defaultEntries.contains("config.xml"), "Should contain config.xml");
        assertTrue(defaultEntries.contains("com/example/TestClass.class"), "Should contain TestClass");
        assertTrue(defaultEntries.contains("data.json"), "Should contain data.json");
    }

    /**
     * Test that getDefaultExtraDataEntryNames returns null after clear() is called.
     */
    @Test
    public void testGetDefaultExtraDataEntryNamesAfterClear() {
        map.addExtraDataEntry("entry1.txt");
        map.addExtraDataEntry("entry2.txt");

        // Verify entries exist
        assertNotNull(map.getDefaultExtraDataEntryNames(), "Should have entries before clear");

        map.clear();

        Set<String> defaultEntriesAfterClear = map.getDefaultExtraDataEntryNames();
        assertTrue(defaultEntriesAfterClear == null || defaultEntriesAfterClear.isEmpty(),
                "Default entries should be null or empty after clear");
    }

    /**
     * Test that getDefaultExtraDataEntryNames returns null when only keyed entries exist.
     */
    @Test
    public void testGetDefaultExtraDataEntryNamesWithOnlyKeyedEntries() {
        map.addExtraDataEntry("key1.class", "value1.txt");
        map.addExtraDataEntry("key2.class", "value2.txt");
        map.addExtraClassToClass("com/example/Key", "com/example/Value");

        Set<String> defaultEntries = map.getDefaultExtraDataEntryNames();

        assertNull(defaultEntries, "Default entries should be null when only keyed entries exist");
    }

    /**
     * Test that getDefaultExtraDataEntryNames handles adding duplicate entries.
     */
    @Test
    public void testGetDefaultExtraDataEntryNamesWithDuplicates() {
        map.addExtraDataEntry("duplicate.txt");
        map.addExtraDataEntry("duplicate.txt");
        map.addExtraDataEntry("duplicate.txt");

        Set<String> defaultEntries = map.getDefaultExtraDataEntryNames();

        assertNotNull(defaultEntries, "Default entries should not be null");
        // Note: Behavior depends on MultiValueMap implementation (Set vs List)
        // We verify the entry exists, regardless of count
        assertTrue(defaultEntries.contains("duplicate.txt"), "Should contain the duplicate entry");
    }

    /**
     * Test that getDefaultExtraDataEntryNames handles entries with various file extensions.
     */
    @Test
    public void testGetDefaultExtraDataEntryNamesWithVariousExtensions() {
        map.addExtraDataEntry("file.txt");
        map.addExtraDataEntry("file.xml");
        map.addExtraDataEntry("file.json");
        map.addExtraDataEntry("file.properties");
        map.addExtraDataEntry("file.yaml");
        map.addExtraDataEntry("no_extension");

        Set<String> defaultEntries = map.getDefaultExtraDataEntryNames();

        assertNotNull(defaultEntries, "Default entries should not be null");
        assertTrue(defaultEntries.contains("file.txt"), "Should contain .txt file");
        assertTrue(defaultEntries.contains("file.xml"), "Should contain .xml file");
        assertTrue(defaultEntries.contains("file.json"), "Should contain .json file");
        assertTrue(defaultEntries.contains("file.properties"), "Should contain .properties file");
        assertTrue(defaultEntries.contains("file.yaml"), "Should contain .yaml file");
        assertTrue(defaultEntries.contains("no_extension"), "Should contain file without extension");
    }

    /**
     * Test that getDefaultExtraDataEntryNames handles entries with paths.
     */
    @Test
    public void testGetDefaultExtraDataEntryNamesWithPaths() {
        map.addExtraDataEntry("resources/config.xml");
        map.addExtraDataEntry("data/nested/deep/file.txt");
        map.addExtraDataEntry("/absolute/path/file.json");

        Set<String> defaultEntries = map.getDefaultExtraDataEntryNames();

        assertNotNull(defaultEntries, "Default entries should not be null");
        assertEquals(3, defaultEntries.size(), "Should have 3 entries with paths");
        assertTrue(defaultEntries.contains("resources/config.xml"), "Should contain relative path entry");
        assertTrue(defaultEntries.contains("data/nested/deep/file.txt"), "Should contain nested path entry");
        assertTrue(defaultEntries.contains("/absolute/path/file.json"), "Should contain absolute path entry");
    }

    /**
     * Test that getDefaultExtraDataEntryNames handles empty string entry.
     */
    @Test
    public void testGetDefaultExtraDataEntryNamesWithEmptyString() {
        map.addExtraDataEntry("");

        Set<String> defaultEntries = map.getDefaultExtraDataEntryNames();

        assertNotNull(defaultEntries, "Default entries should not be null");
        assertEquals(1, defaultEntries.size(), "Should have 1 entry");
        assertTrue(defaultEntries.contains(""), "Should contain empty string entry");
    }

    /**
     * Test that getDefaultExtraDataEntryNames handles entries with special characters.
     */
    @Test
    public void testGetDefaultExtraDataEntryNamesWithSpecialCharacters() {
        map.addExtraDataEntry("file-with-dashes.txt");
        map.addExtraDataEntry("file_with_underscores.txt");
        map.addExtraDataEntry("file with spaces.txt");
        map.addExtraDataEntry("file$with$dollars.txt");

        Set<String> defaultEntries = map.getDefaultExtraDataEntryNames();

        assertNotNull(defaultEntries, "Default entries should not be null");
        assertEquals(4, defaultEntries.size(), "Should have 4 entries");
        assertTrue(defaultEntries.contains("file-with-dashes.txt"), "Should contain entry with dashes");
        assertTrue(defaultEntries.contains("file_with_underscores.txt"), "Should contain entry with underscores");
        assertTrue(defaultEntries.contains("file with spaces.txt"), "Should contain entry with spaces");
        assertTrue(defaultEntries.contains("file$with$dollars.txt"), "Should contain entry with dollar signs");
    }

    /**
     * Test that getDefaultExtraDataEntryNames does not throw exceptions.
     */
    @Test
    public void testGetDefaultExtraDataEntryNamesDoesNotThrow() {
        assertDoesNotThrow(() -> map.getDefaultExtraDataEntryNames(),
                "getDefaultExtraDataEntryNames should not throw on empty map");

        map.addExtraDataEntry("test.txt");
        assertDoesNotThrow(() -> map.getDefaultExtraDataEntryNames(),
                "getDefaultExtraDataEntryNames should not throw with entries");
    }

    /**
     * Test that getDefaultExtraDataEntryNames can be called multiple times.
     */
    @Test
    public void testGetDefaultExtraDataEntryNamesMultipleCalls() {
        map.addExtraDataEntry("entry.txt");

        Set<String> result1 = map.getDefaultExtraDataEntryNames();
        Set<String> result2 = map.getDefaultExtraDataEntryNames();

        assertNotNull(result1, "First call should return non-null");
        assertNotNull(result2, "Second call should return non-null");
        assertEquals(result1.size(), result2.size(), "Multiple calls should return same size");
        assertTrue(result1.contains("entry.txt"), "First call should contain entry");
        assertTrue(result2.contains("entry.txt"), "Second call should contain entry");
    }

    /**
     * Test that getDefaultExtraDataEntryNames is updated when new entries are added.
     */
    @Test
    public void testGetDefaultExtraDataEntryNamesUpdatesWithNewEntries() {
        map.addExtraDataEntry("entry1.txt");

        Set<String> defaultEntries1 = map.getDefaultExtraDataEntryNames();
        assertEquals(1, defaultEntries1.size(), "Should have 1 entry initially");

        map.addExtraDataEntry("entry2.txt");

        Set<String> defaultEntries2 = map.getDefaultExtraDataEntryNames();
        assertEquals(2, defaultEntries2.size(), "Should have 2 entries after adding another");
        assertTrue(defaultEntries2.contains("entry1.txt"), "Should contain first entry");
        assertTrue(defaultEntries2.contains("entry2.txt"), "Should contain second entry");
    }

    /**
     * Test that getDefaultExtraDataEntryNames is affected by clearExtraClasses.
     */
    @Test
    public void testGetDefaultExtraDataEntryNamesAfterClearExtraClasses() {
        map.addExtraClass("com/example/Class1");
        map.addExtraDataEntry("config.txt");

        Set<String> defaultEntriesBefore = map.getDefaultExtraDataEntryNames();
        assertEquals(2, defaultEntriesBefore.size(), "Should have 2 entries before clearExtraClasses");

        map.clearExtraClasses();

        Set<String> defaultEntriesAfter = map.getDefaultExtraDataEntryNames();
        assertNotNull(defaultEntriesAfter, "Should still have entries after clearExtraClasses");
        assertEquals(1, defaultEntriesAfter.size(), "Should have 1 entry after clearExtraClasses");
        assertTrue(defaultEntriesAfter.contains("config.txt"), "Should contain non-class entry");
        assertFalse(defaultEntriesAfter.contains("com/example/Class1.class"),
                "Should not contain class entry after clearExtraClasses");
    }

    /**
     * Test that getDefaultExtraDataEntryNames handles a large number of entries.
     */
    @Test
    public void testGetDefaultExtraDataEntryNamesWithManyEntries() {
        int entryCount = 1000;
        for (int i = 0; i < entryCount; i++) {
            map.addExtraDataEntry("entry" + i + ".txt");
        }

        Set<String> defaultEntries = map.getDefaultExtraDataEntryNames();

        assertNotNull(defaultEntries, "Default entries should not be null");
        // Size may vary if MultiValueMap allows duplicates, but should be at least 1
        assertTrue(defaultEntries.size() > 0, "Should have entries");
        assertTrue(defaultEntries.size() <= entryCount, "Should not exceed the number of added entries");
        assertTrue(defaultEntries.contains("entry0.txt"), "Should contain first entry");
        assertTrue(defaultEntries.contains("entry999.txt"), "Should contain last entry");
    }

    /**
     * Test that getDefaultExtraDataEntryNames returns entries independent of key order.
     */
    @Test
    public void testGetDefaultExtraDataEntryNamesIndependentOfKeyOrder() {
        // Add entries in different orders with different keys
        map.addExtraDataEntry("key1.class", "value1.txt");
        map.addExtraDataEntry("default1.txt");
        map.addExtraDataEntry("key2.class", "value2.txt");
        map.addExtraDataEntry("default2.txt");

        Set<String> defaultEntries = map.getDefaultExtraDataEntryNames();

        assertNotNull(defaultEntries, "Default entries should not be null");
        assertEquals(2, defaultEntries.size(), "Should have exactly 2 default entries");
        assertTrue(defaultEntries.contains("default1.txt"), "Should contain default1.txt");
        assertTrue(defaultEntries.contains("default2.txt"), "Should contain default2.txt");
        assertFalse(defaultEntries.contains("value1.txt"), "Should not contain keyed value1.txt");
        assertFalse(defaultEntries.contains("value2.txt"), "Should not contain keyed value2.txt");
    }

    /**
     * Test that getDefaultExtraDataEntryNames handles entries with very long names.
     */
    @Test
    public void testGetDefaultExtraDataEntryNamesWithLongNames() {
        StringBuilder longName = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longName.append("a");
        }
        longName.append(".txt");

        map.addExtraDataEntry(longName.toString());

        Set<String> defaultEntries = map.getDefaultExtraDataEntryNames();

        assertNotNull(defaultEntries, "Default entries should not be null");
        assertEquals(1, defaultEntries.size(), "Should have 1 entry");
        assertTrue(defaultEntries.contains(longName.toString()), "Should contain long name entry");
    }

    /**
     * Test that getDefaultExtraDataEntryNames returns a Set that contains the expected entries.
     */
    @Test
    public void testGetDefaultExtraDataEntryNamesReturnsSet() {
        map.addExtraDataEntry("entry1.txt");
        map.addExtraDataEntry("entry2.txt");

        Set<String> defaultEntries = map.getDefaultExtraDataEntryNames();

        assertNotNull(defaultEntries, "Should return a Set");
        assertTrue(defaultEntries instanceof Set, "Return type should be a Set");
    }
}
