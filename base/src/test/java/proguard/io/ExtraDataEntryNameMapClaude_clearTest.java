package proguard.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ExtraDataEntryNameMap.clear() method.
 */
public class ExtraDataEntryNameMapClaude_clearTest {

    private ExtraDataEntryNameMap map;

    @BeforeEach
    public void setUp() {
        map = new ExtraDataEntryNameMap();
    }

    /**
     * Test that clear() works on an empty map without throwing exceptions.
     */
    @Test
    public void testClearOnEmptyMap() {
        // Clear an empty map
        assertDoesNotThrow(() -> map.clear(), "clear() should work on an empty map");

        // Verify the map is still empty
        assertTrue(map.getKeyDataEntryNames().isEmpty(), "Map should remain empty after clearing empty map");
        assertTrue(map.getAllExtraDataEntryNames().isEmpty(), "All extra data entries should be empty");
    }

    /**
     * Test that clear() removes all default extra data entries (entries not linked to any key).
     */
    @Test
    public void testClearRemovesDefaultExtraDataEntries() {
        // Add some default extra data entries (not linked to any key)
        map.addExtraDataEntry("default1.txt");
        map.addExtraDataEntry("default2.txt");
        map.addExtraDataEntry("default3.txt");

        // Verify they were added
        Set<String> defaultEntries = map.getDefaultExtraDataEntryNames();
        assertNotNull(defaultEntries, "Default entries should exist before clear");
        assertEquals(3, defaultEntries.size(), "Should have 3 default entries before clear");

        // Clear the map
        map.clear();

        // Verify all default entries are removed
        Set<String> defaultEntriesAfterClear = map.getDefaultExtraDataEntryNames();
        assertTrue(defaultEntriesAfterClear == null || defaultEntriesAfterClear.isEmpty(),
                   "Default entries should be null or empty after clear");
        assertTrue(map.getAllExtraDataEntryNames().isEmpty(), "All extra data entries should be empty");
    }

    /**
     * Test that clear() removes all keyed extra data entries.
     */
    @Test
    public void testClearRemovesKeyedExtraDataEntries() {
        // Add some keyed extra data entries
        map.addExtraDataEntry("key1.txt", "value1.txt");
        map.addExtraDataEntry("key2.txt", "value2.txt");
        map.addExtraDataEntry("key3.txt", "value3.txt");

        // Verify they were added
        assertFalse(map.getKeyDataEntryNames().isEmpty(), "Should have keys before clear");
        assertEquals(3, map.getKeyDataEntryNames().size(), "Should have 3 keys before clear");

        // Clear the map
        map.clear();

        // Verify all keyed entries are removed
        assertTrue(map.getKeyDataEntryNames().isEmpty(), "Key data entry names should be empty after clear");
        assertTrue(map.getAllExtraDataEntryNames().isEmpty(), "All extra data entries should be empty");
    }

    /**
     * Test that clear() removes both default and keyed extra data entries.
     */
    @Test
    public void testClearRemovesBothDefaultAndKeyedEntries() {
        // Add both default and keyed entries
        map.addExtraDataEntry("default.txt");
        map.addExtraDataEntry("key1.txt", "value1.txt");
        map.addExtraDataEntry("key2.txt", "value2.txt");

        // Verify they were added
        assertFalse(map.getKeyDataEntryNames().isEmpty(), "Should have keys before clear");
        assertFalse(map.getAllExtraDataEntryNames().isEmpty(), "Should have entries before clear");

        // Clear the map
        map.clear();

        // Verify everything is removed
        assertTrue(map.getKeyDataEntryNames().isEmpty(), "Key data entry names should be empty after clear");
        assertTrue(map.getAllExtraDataEntryNames().isEmpty(), "All extra data entries should be empty after clear");
        Set<String> defaultEntries = map.getDefaultExtraDataEntryNames();
        assertTrue(defaultEntries == null || defaultEntries.isEmpty(),
                   "Default entries should be null or empty after clear");
    }

    /**
     * Test that clear() removes extra class entries.
     */
    @Test
    public void testClearRemovesExtraClassEntries() {
        // Add extra class entries
        map.addExtraClass("com/example/Class1");
        map.addExtraClass("com/example/Class2");
        map.addExtraClassToClass("com/example/Key", "com/example/Value");

        // Verify they were added
        assertFalse(map.getAllExtraDataEntryNames().isEmpty(), "Should have entries before clear");

        // Clear the map
        map.clear();

        // Verify all entries are removed
        assertTrue(map.getAllExtraDataEntryNames().isEmpty(), "All class entries should be removed after clear");
        assertTrue(map.getKeyDataEntryNames().isEmpty(), "All keys should be removed after clear");
    }

    /**
     * Test that the map can be reused after clear() by adding new entries.
     */
    @Test
    public void testMapCanBeReusedAfterClear() {
        // Add some entries
        map.addExtraDataEntry("initial.txt");
        map.addExtraDataEntry("key.txt", "value.txt");

        // Clear the map
        map.clear();

        // Add new entries after clear
        map.addExtraDataEntry("new.txt");
        map.addExtraDataEntry("newKey.txt", "newValue.txt");

        // Verify the new entries are present
        assertFalse(map.getAllExtraDataEntryNames().isEmpty(), "Should have new entries after clear and re-add");
        assertTrue(map.getAllExtraDataEntryNames().contains("new.txt"), "Should contain new default entry");
        assertTrue(map.getAllExtraDataEntryNames().contains("newValue.txt"), "Should contain new keyed entry");

        // Verify old entries are not present
        assertFalse(map.getAllExtraDataEntryNames().contains("initial.txt"), "Should not contain old default entry");
        assertFalse(map.getAllExtraDataEntryNames().contains("value.txt"), "Should not contain old keyed entry");
    }

    /**
     * Test that clear() can be called multiple times in succession.
     */
    @Test
    public void testMultipleClearCalls() {
        // Add entries
        map.addExtraDataEntry("entry.txt");

        // Clear multiple times
        map.clear();
        map.clear();
        map.clear();

        // Verify the map is still empty and functional
        assertTrue(map.getAllExtraDataEntryNames().isEmpty(), "Map should be empty after multiple clears");

        // Verify the map still works
        map.addExtraDataEntry("test.txt");
        assertTrue(map.getAllExtraDataEntryNames().contains("test.txt"), "Map should still be functional");
    }

    /**
     * Test that clear() removes entries with multiple values mapped to the same key.
     */
    @Test
    public void testClearRemovesMultipleValuesPerKey() {
        // Add multiple values to the same key
        map.addExtraDataEntry("key.txt", "value1.txt");
        map.addExtraDataEntry("key.txt", "value2.txt");
        map.addExtraDataEntry("key.txt", "value3.txt");

        // Verify they were added
        Set<String> values = map.getExtraDataEntryNames("key.txt");
        assertNotNull(values, "Values should exist before clear");
        assertEquals(3, values.size(), "Should have 3 values for the key before clear");

        // Clear the map
        map.clear();

        // Verify all values are removed
        Set<String> valuesAfterClear = map.getExtraDataEntryNames("key.txt");
        assertTrue(valuesAfterClear == null || valuesAfterClear.isEmpty(),
                   "Values should be null or empty after clear");
        assertTrue(map.getAllExtraDataEntryNames().isEmpty(), "All entries should be removed");
    }

    /**
     * Test that clear() removes all keys when there are many different keys.
     */
    @Test
    public void testClearRemovesManyKeys() {
        // Add many different keys
        for (int i = 0; i < 100; i++) {
            map.addExtraDataEntry("key" + i + ".txt", "value" + i + ".txt");
        }

        // Verify they were added
        assertEquals(100, map.getKeyDataEntryNames().size(), "Should have 100 keys before clear");

        // Clear the map
        map.clear();

        // Verify all are removed
        assertTrue(map.getKeyDataEntryNames().isEmpty(), "All keys should be removed after clear");
        assertTrue(map.getAllExtraDataEntryNames().isEmpty(), "All entries should be removed after clear");
    }

    /**
     * Test that clear() properly resets the map state so getters return empty/null.
     */
    @Test
    public void testClearResetsMapState() {
        // Add various types of entries
        map.addExtraDataEntry("default.txt");
        map.addExtraDataEntry("key.txt", "value.txt");
        map.addExtraClass("com/example/TestClass");

        // Clear the map
        map.clear();

        // Verify all getters return empty or null
        assertTrue(map.getKeyDataEntryNames().isEmpty(), "getKeyDataEntryNames() should return empty set");
        assertTrue(map.getAllExtraDataEntryNames().isEmpty(), "getAllExtraDataEntryNames() should return empty set");

        Set<String> defaultEntries = map.getDefaultExtraDataEntryNames();
        assertTrue(defaultEntries == null || defaultEntries.isEmpty(),
                   "getDefaultExtraDataEntryNames() should return null or empty");

        Set<String> specificEntries = map.getExtraDataEntryNames("key.txt");
        assertTrue(specificEntries == null || specificEntries.isEmpty(),
                   "getExtraDataEntryNames() should return null or empty for any key");
    }
}
