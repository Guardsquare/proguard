package proguard.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ExtraDataEntryNameMap.getExtraDataEntryNames(String) method.
 */
public class ExtraDataEntryNameMapClaude_getExtraDataEntryNamesTest {

    private ExtraDataEntryNameMap map;

    @BeforeEach
    public void setUp() {
        map = new ExtraDataEntryNameMap();
    }

    /**
     * Test that getExtraDataEntryNames returns null for a non-existent key.
     */
    @Test
    public void testGetExtraDataEntryNamesWithNonExistentKey() {
        Set<String> entries = map.getExtraDataEntryNames("nonexistent.txt");

        assertNull(entries, "Should return null for non-existent key");
    }

    /**
     * Test that getExtraDataEntryNames returns null on empty map.
     */
    @Test
    public void testGetExtraDataEntryNamesOnEmptyMap() {
        Set<String> entries = map.getExtraDataEntryNames("any.txt");

        assertNull(entries, "Should return null for any key on empty map");
    }

    /**
     * Test that getExtraDataEntryNames returns entries for an existing key.
     */
    @Test
    public void testGetExtraDataEntryNamesWithExistingKey() {
        String key = "key.txt";
        String value = "value.txt";
        map.addExtraDataEntry(key, value);

        Set<String> entries = map.getExtraDataEntryNames(key);

        assertNotNull(entries, "Should return non-null for existing key");
        assertEquals(1, entries.size(), "Should have exactly 1 entry");
        assertTrue(entries.contains(value), "Should contain the added value");
    }

    /**
     * Test that getExtraDataEntryNames returns multiple entries for a key.
     */
    @Test
    public void testGetExtraDataEntryNamesWithMultipleEntries() {
        String key = "key.class";
        map.addExtraDataEntry(key, "value1.txt");
        map.addExtraDataEntry(key, "value2.xml");
        map.addExtraDataEntry(key, "value3.json");

        Set<String> entries = map.getExtraDataEntryNames(key);

        assertNotNull(entries, "Should return non-null for existing key");
        assertEquals(3, entries.size(), "Should have 3 entries");
        assertTrue(entries.contains("value1.txt"), "Should contain value1.txt");
        assertTrue(entries.contains("value2.xml"), "Should contain value2.xml");
        assertTrue(entries.contains("value3.json"), "Should contain value3.json");
    }

    /**
     * Test that getExtraDataEntryNames only returns entries for the specified key.
     */
    @Test
    public void testGetExtraDataEntryNamesReturnsOnlySpecificKeyEntries() {
        map.addExtraDataEntry("key1.txt", "value1.txt");
        map.addExtraDataEntry("key2.txt", "value2.txt");
        map.addExtraDataEntry("key3.txt", "value3.txt");

        Set<String> entriesForKey1 = map.getExtraDataEntryNames("key1.txt");

        assertNotNull(entriesForKey1, "Should return entries for key1");
        assertEquals(1, entriesForKey1.size(), "Should have exactly 1 entry for key1");
        assertTrue(entriesForKey1.contains("value1.txt"), "Should contain value1.txt");
        assertFalse(entriesForKey1.contains("value2.txt"), "Should not contain value2.txt");
        assertFalse(entriesForKey1.contains("value3.txt"), "Should not contain value3.txt");
    }

    /**
     * Test that getExtraDataEntryNames works with class data entries.
     */
    @Test
    public void testGetExtraDataEntryNamesWithClassDataEntry() {
        String className = "com/example/TestClass";
        String classKey = className + ".class";
        String extraEntry = "extra.xml";

        map.addExtraDataEntryToClass(className, extraEntry);

        Set<String> entries = map.getExtraDataEntryNames(classKey);

        assertNotNull(entries, "Should return entries for class key");
        assertTrue(entries.contains(extraEntry), "Should contain the extra entry");
    }

    /**
     * Test that getExtraDataEntryNames does not return default entries (null key).
     */
    @Test
    public void testGetExtraDataEntryNamesDoesNotReturnDefaultEntries() {
        map.addExtraDataEntry("default.txt");
        map.addExtraDataEntry("key.txt", "value.txt");

        Set<String> entriesForKey = map.getExtraDataEntryNames("key.txt");

        assertNotNull(entriesForKey, "Should return entries for key");
        assertFalse(entriesForKey.contains("default.txt"), "Should not contain default entry");
        assertTrue(entriesForKey.contains("value.txt"), "Should contain keyed entry");
    }

    /**
     * Test that getExtraDataEntryNames returns null after key entries are cleared.
     */
    @Test
    public void testGetExtraDataEntryNamesAfterClear() {
        String key = "key.txt";
        map.addExtraDataEntry(key, "value.txt");

        // Verify entry exists
        assertNotNull(map.getExtraDataEntryNames(key), "Should have entries before clear");

        map.clear();

        Set<String> entriesAfterClear = map.getExtraDataEntryNames(key);
        assertTrue(entriesAfterClear == null || entriesAfterClear.isEmpty(),
                "Should return null or empty after clear");
    }

    /**
     * Test that getExtraDataEntryNames handles null key parameter.
     */
    @Test
    public void testGetExtraDataEntryNamesWithNullKey() {
        map.addExtraDataEntry("default.txt");

        Set<String> entries = map.getExtraDataEntryNames(null);

        // This should return the same as getDefaultExtraDataEntryNames
        assertNotNull(entries, "Should return default entries for null key");
        assertTrue(entries.contains("default.txt"), "Should contain default entry");
    }

    /**
     * Test that getExtraDataEntryNames handles empty string key.
     */
    @Test
    public void testGetExtraDataEntryNamesWithEmptyStringKey() {
        map.addExtraDataEntry("", "value.txt");

        Set<String> entries = map.getExtraDataEntryNames("");

        assertNotNull(entries, "Should return entries for empty string key");
        assertTrue(entries.contains("value.txt"), "Should contain the value");
    }

    /**
     * Test that getExtraDataEntryNames handles keys with special characters.
     */
    @Test
    public void testGetExtraDataEntryNamesWithSpecialCharactersInKey() {
        String key = "com/example/Test$Inner_Class-123.class";
        String value = "extra.txt";
        map.addExtraDataEntry(key, value);

        Set<String> entries = map.getExtraDataEntryNames(key);

        assertNotNull(entries, "Should return entries for key with special characters");
        assertTrue(entries.contains(value), "Should contain the value");
    }

    /**
     * Test that getExtraDataEntryNames handles keys with paths.
     */
    @Test
    public void testGetExtraDataEntryNamesWithPathInKey() {
        String key = "path/to/deep/nested/file.txt";
        String value = "extra.xml";
        map.addExtraDataEntry(key, value);

        Set<String> entries = map.getExtraDataEntryNames(key);

        assertNotNull(entries, "Should return entries for key with path");
        assertTrue(entries.contains(value), "Should contain the value");
    }

    /**
     * Test that getExtraDataEntryNames is case-sensitive.
     */
    @Test
    public void testGetExtraDataEntryNamesIsCaseSensitive() {
        map.addExtraDataEntry("Key.txt", "value1.txt");
        map.addExtraDataEntry("key.txt", "value2.txt");
        map.addExtraDataEntry("KEY.txt", "value3.txt");

        Set<String> entriesForKey = map.getExtraDataEntryNames("Key.txt");
        Set<String> entriesForkey = map.getExtraDataEntryNames("key.txt");
        Set<String> entriesForKEY = map.getExtraDataEntryNames("KEY.txt");

        assertNotNull(entriesForKey, "Should have entries for 'Key.txt'");
        assertNotNull(entriesForkey, "Should have entries for 'key.txt'");
        assertNotNull(entriesForKEY, "Should have entries for 'KEY.txt'");

        assertTrue(entriesForKey.contains("value1.txt"), "'Key.txt' should map to value1.txt");
        assertTrue(entriesForkey.contains("value2.txt"), "'key.txt' should map to value2.txt");
        assertTrue(entriesForKEY.contains("value3.txt"), "'KEY.txt' should map to value3.txt");
    }

    /**
     * Test that getExtraDataEntryNames can be called multiple times.
     */
    @Test
    public void testGetExtraDataEntryNamesMultipleCalls() {
        String key = "key.txt";
        String value = "value.txt";
        map.addExtraDataEntry(key, value);

        Set<String> entries1 = map.getExtraDataEntryNames(key);
        Set<String> entries2 = map.getExtraDataEntryNames(key);

        assertNotNull(entries1, "First call should return non-null");
        assertNotNull(entries2, "Second call should return non-null");
        assertEquals(entries1.size(), entries2.size(), "Both calls should return same size");
        assertTrue(entries1.contains(value), "First call should contain value");
        assertTrue(entries2.contains(value), "Second call should contain value");
    }

    /**
     * Test that getExtraDataEntryNames returns updated results after adding more entries.
     */
    @Test
    public void testGetExtraDataEntryNamesReflectsNewEntries() {
        String key = "key.class";
        map.addExtraDataEntry(key, "value1.txt");

        Set<String> entries1 = map.getExtraDataEntryNames(key);
        assertEquals(1, entries1.size(), "Should have 1 entry initially");

        map.addExtraDataEntry(key, "value2.txt");

        Set<String> entries2 = map.getExtraDataEntryNames(key);
        assertEquals(2, entries2.size(), "Should have 2 entries after adding another");
        assertTrue(entries2.contains("value1.txt"), "Should contain first value");
        assertTrue(entries2.contains("value2.txt"), "Should contain second value");
    }

    /**
     * Test that getExtraDataEntryNames handles duplicate values.
     */
    @Test
    public void testGetExtraDataEntryNamesWithDuplicateValues() {
        String key = "key.txt";
        String value = "duplicate.txt";
        map.addExtraDataEntry(key, value);
        map.addExtraDataEntry(key, value);
        map.addExtraDataEntry(key, value);

        Set<String> entries = map.getExtraDataEntryNames(key);

        assertNotNull(entries, "Should return entries for key");
        // Note: Behavior depends on MultiValueMap implementation (Set vs List)
        // We verify the entry exists
        assertTrue(entries.contains(value), "Should contain the duplicate value");
    }

    /**
     * Test that getExtraDataEntryNames handles keys associated with class entries.
     */
    @Test
    public void testGetExtraDataEntryNamesWithClassToClassMapping() {
        String keyClass = "com/example/KeyClass";
        String extraClass = "com/example/ExtraClass";

        map.addExtraClassToClass(keyClass, extraClass);

        String keyDataEntry = keyClass + ".class";
        Set<String> entries = map.getExtraDataEntryNames(keyDataEntry);

        assertNotNull(entries, "Should return entries for class key");
        assertTrue(entries.contains(extraClass + ".class"), "Should contain extra class with .class extension");
    }

    /**
     * Test that getExtraDataEntryNames does not throw exceptions with valid keys.
     */
    @Test
    public void testGetExtraDataEntryNamesDoesNotThrow() {
        assertDoesNotThrow(() -> map.getExtraDataEntryNames("any.txt"),
                "Should not throw with non-existent key");

        map.addExtraDataEntry("key.txt", "value.txt");
        assertDoesNotThrow(() -> map.getExtraDataEntryNames("key.txt"),
                "Should not throw with existing key");

        assertDoesNotThrow(() -> map.getExtraDataEntryNames(null),
                "Should not throw with null key");

        assertDoesNotThrow(() -> map.getExtraDataEntryNames(""),
                "Should not throw with empty key");
    }

    /**
     * Test that getExtraDataEntryNames returns a Set.
     */
    @Test
    public void testGetExtraDataEntryNamesReturnsSet() {
        String key = "key.txt";
        map.addExtraDataEntry(key, "value.txt");

        Set<String> entries = map.getExtraDataEntryNames(key);

        assertNotNull(entries, "Should return non-null");
        assertTrue(entries instanceof Set, "Should return a Set instance");
    }

    /**
     * Test that getExtraDataEntryNames handles very long key names.
     */
    @Test
    public void testGetExtraDataEntryNamesWithLongKey() {
        StringBuilder longKey = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longKey.append("a");
        }
        longKey.append(".txt");

        String value = "value.txt";
        map.addExtraDataEntry(longKey.toString(), value);

        Set<String> entries = map.getExtraDataEntryNames(longKey.toString());

        assertNotNull(entries, "Should return entries for long key");
        assertTrue(entries.contains(value), "Should contain the value");
    }

    /**
     * Test that getExtraDataEntryNames handles keys with various file extensions.
     */
    @Test
    public void testGetExtraDataEntryNamesWithVariousFileExtensions() {
        map.addExtraDataEntry("file.class", "extra1.txt");
        map.addExtraDataEntry("file.txt", "extra2.xml");
        map.addExtraDataEntry("file.xml", "extra3.json");
        map.addExtraDataEntry("file.json", "extra4.properties");

        Set<String> entriesForClass = map.getExtraDataEntryNames("file.class");
        Set<String> entriesForTxt = map.getExtraDataEntryNames("file.txt");
        Set<String> entriesForXml = map.getExtraDataEntryNames("file.xml");
        Set<String> entriesForJson = map.getExtraDataEntryNames("file.json");

        assertNotNull(entriesForClass, "Should have entries for .class file");
        assertNotNull(entriesForTxt, "Should have entries for .txt file");
        assertNotNull(entriesForXml, "Should have entries for .xml file");
        assertNotNull(entriesForJson, "Should have entries for .json file");

        assertTrue(entriesForClass.contains("extra1.txt"), "Should contain correct entry for .class");
        assertTrue(entriesForTxt.contains("extra2.xml"), "Should contain correct entry for .txt");
        assertTrue(entriesForXml.contains("extra3.json"), "Should contain correct entry for .xml");
        assertTrue(entriesForJson.contains("extra4.properties"), "Should contain correct entry for .json");
    }

    /**
     * Test that getExtraDataEntryNames works after clearExtraClasses.
     */
    @Test
    public void testGetExtraDataEntryNamesAfterClearExtraClasses() {
        String key = "com/example/TestClass.class";
        map.addExtraDataEntry(key, "extra.class");
        map.addExtraDataEntry(key, "extra.txt");

        Set<String> entriesBefore = map.getExtraDataEntryNames(key);
        assertEquals(2, entriesBefore.size(), "Should have 2 entries before clearExtraClasses");

        map.clearExtraClasses();

        Set<String> entriesAfter = map.getExtraDataEntryNames(key);
        // clearExtraClasses removes entries that end with .class extension
        if (entriesAfter != null) {
            assertFalse(entriesAfter.contains("extra.class"), "Should not contain .class entry after clearExtraClasses");
            // The non-class entry should still be there
            assertTrue(entriesAfter.contains("extra.txt") || entriesAfter.isEmpty(),
                    "Non-class entry might still exist");
        }
    }

    /**
     * Test that getExtraDataEntryNames handles mixed keyed and default entries correctly.
     */
    @Test
    public void testGetExtraDataEntryNamesWithMixedEntries() {
        map.addExtraDataEntry("default1.txt");
        map.addExtraDataEntry("default2.txt");
        map.addExtraDataEntry("key1.txt", "value1.txt");
        map.addExtraDataEntry("key2.txt", "value2.txt");

        Set<String> entriesForKey1 = map.getExtraDataEntryNames("key1.txt");
        Set<String> entriesForKey2 = map.getExtraDataEntryNames("key2.txt");
        Set<String> entriesForNonExistent = map.getExtraDataEntryNames("key3.txt");

        assertNotNull(entriesForKey1, "Should have entries for key1");
        assertNotNull(entriesForKey2, "Should have entries for key2");
        assertNull(entriesForNonExistent, "Should be null for non-existent key");

        assertEquals(1, entriesForKey1.size(), "Should have 1 entry for key1");
        assertEquals(1, entriesForKey2.size(), "Should have 1 entry for key2");

        assertTrue(entriesForKey1.contains("value1.txt"), "key1 should map to value1.txt");
        assertTrue(entriesForKey2.contains("value2.txt"), "key2 should map to value2.txt");
    }

    /**
     * Test that getExtraDataEntryNames handles a large number of entries for one key.
     */
    @Test
    public void testGetExtraDataEntryNamesWithManyEntriesForOneKey() {
        String key = "key.class";
        int entryCount = 1000;

        for (int i = 0; i < entryCount; i++) {
            map.addExtraDataEntry(key, "value" + i + ".txt");
        }

        Set<String> entries = map.getExtraDataEntryNames(key);

        assertNotNull(entries, "Should return entries");
        assertTrue(entries.size() > 0, "Should have entries");
        assertTrue(entries.size() <= entryCount, "Should not exceed number of added entries");
        assertTrue(entries.contains("value0.txt"), "Should contain first entry");
        assertTrue(entries.contains("value999.txt"), "Should contain last entry");
    }

    /**
     * Test that getExtraDataEntryNames works correctly with getClassDataEntryName.
     */
    @Test
    public void testGetExtraDataEntryNamesWithGetClassDataEntryName() {
        String className = "com/example/TestClass";
        String extraEntry = "extra.xml";

        map.addExtraDataEntryToClass(className, extraEntry);

        // Use getClassDataEntryName to get the proper key
        String key = map.getClassDataEntryName(className);
        Set<String> entries = map.getExtraDataEntryNames(key);

        assertNotNull(entries, "Should have entries for class data entry name");
        assertTrue(entries.contains(extraEntry), "Should contain the extra entry");
    }

    /**
     * Test that getExtraDataEntryNames returns independent sets for different keys.
     */
    @Test
    public void testGetExtraDataEntryNamesReturnsIndependentSets() {
        map.addExtraDataEntry("key1.txt", "value1.txt");
        map.addExtraDataEntry("key2.txt", "value2.txt");

        Set<String> entries1 = map.getExtraDataEntryNames("key1.txt");
        Set<String> entries2 = map.getExtraDataEntryNames("key2.txt");

        assertNotEquals(entries1, entries2, "Sets for different keys should be different");
        assertTrue(entries1.contains("value1.txt"), "First set should contain value1");
        assertFalse(entries1.contains("value2.txt"), "First set should not contain value2");
        assertTrue(entries2.contains("value2.txt"), "Second set should contain value2");
        assertFalse(entries2.contains("value1.txt"), "Second set should not contain value1");
    }
}
