package proguard.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ExtraDataEntryNameMap.addExtraDataEntryToClass(String, String) method.
 */
public class ExtraDataEntryNameMapClaude_addExtraDataEntryToClassTest {

    private ExtraDataEntryNameMap map;

    @BeforeEach
    public void setUp() {
        map = new ExtraDataEntryNameMap();
    }

    /**
     * Test that addExtraDataEntryToClass adds an entry with a simple class name and data entry.
     */
    @Test
    public void testAddExtraDataEntryToClassWithSimpleNames() {
        String keyClassName = "com/example/MyClass";
        String extraDataEntry = "extra.txt";

        map.addExtraDataEntryToClass(keyClassName, extraDataEntry);

        // Verify the entry was added by checking if it appears in the map
        Set<String> allEntries = map.getAllExtraDataEntryNames();
        assertTrue(allEntries.contains(extraDataEntry),
                "Extra data entry should be added to the map");

        // Verify the key was created with .class extension
        String expectedKey = keyClassName + ".class";
        Set<String> keyNames = map.getKeyDataEntryNames();
        assertTrue(keyNames.contains(expectedKey),
                "Key should be created with .class extension appended");

        // Verify the entry is associated with the correct key
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);
        assertNotNull(entriesForKey, "Entries for key should not be null");
        assertTrue(entriesForKey.contains(extraDataEntry),
                "Extra data entry should be associated with the correct key");
    }

    /**
     * Test that addExtraDataEntryToClass can add multiple entries to the same class.
     */
    @Test
    public void testAddMultipleEntriesToSameClass() {
        String keyClassName = "com/example/TestClass";
        String entry1 = "extra1.txt";
        String entry2 = "extra2.txt";
        String entry3 = "extra3.txt";

        map.addExtraDataEntryToClass(keyClassName, entry1);
        map.addExtraDataEntryToClass(keyClassName, entry2);
        map.addExtraDataEntryToClass(keyClassName, entry3);

        // Verify all entries are in the map
        Set<String> allEntries = map.getAllExtraDataEntryNames();
        assertTrue(allEntries.contains(entry1), "First entry should be in map");
        assertTrue(allEntries.contains(entry2), "Second entry should be in map");
        assertTrue(allEntries.contains(entry3), "Third entry should be in map");

        // Verify all entries are associated with the key
        String expectedKey = keyClassName + ".class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);
        assertEquals(3, entriesForKey.size(), "Should have 3 entries for the key");
        assertTrue(entriesForKey.contains(entry1), "Should contain first entry");
        assertTrue(entriesForKey.contains(entry2), "Should contain second entry");
        assertTrue(entriesForKey.contains(entry3), "Should contain third entry");
    }

    /**
     * Test that addExtraDataEntryToClass can handle different classes with different entries.
     */
    @Test
    public void testAddEntriesForDifferentClasses() {
        String class1 = "com/example/Class1";
        String class2 = "com/example/Class2";
        String entry1 = "extra1.txt";
        String entry2 = "extra2.txt";

        map.addExtraDataEntryToClass(class1, entry1);
        map.addExtraDataEntryToClass(class2, entry2);

        // Verify both keys are in the map
        Set<String> keyNames = map.getKeyDataEntryNames();
        assertTrue(keyNames.contains(class1 + ".class"), "First class key should be in map");
        assertTrue(keyNames.contains(class2 + ".class"), "Second class key should be in map");

        // Verify correct entries are associated with correct keys
        Set<String> entriesForClass1 = map.getExtraDataEntryNames(class1 + ".class");
        assertTrue(entriesForClass1.contains(entry1), "Entry1 should be associated with Class1");
        assertFalse(entriesForClass1.contains(entry2), "Entry2 should not be associated with Class1");

        Set<String> entriesForClass2 = map.getExtraDataEntryNames(class2 + ".class");
        assertTrue(entriesForClass2.contains(entry2), "Entry2 should be associated with Class2");
        assertFalse(entriesForClass2.contains(entry1), "Entry1 should not be associated with Class2");
    }

    /**
     * Test that addExtraDataEntryToClass handles class names with nested packages.
     */
    @Test
    public void testAddExtraDataEntryWithNestedPackages() {
        String keyClassName = "com/example/deep/nested/package/MyClass";
        String extraDataEntry = "nested.txt";

        map.addExtraDataEntryToClass(keyClassName, extraDataEntry);

        String expectedKey = keyClassName + ".class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);
        assertNotNull(entriesForKey, "Entries should exist for nested package class");
        assertTrue(entriesForKey.contains(extraDataEntry),
                "Entry should be associated with nested package class");
    }

    /**
     * Test that addExtraDataEntryToClass handles class names with special characters.
     */
    @Test
    public void testAddExtraDataEntryWithSpecialCharactersInClassName() {
        String keyClassName = "com/example/My$Inner_Class";
        String extraDataEntry = "special.txt";

        map.addExtraDataEntryToClass(keyClassName, extraDataEntry);

        String expectedKey = keyClassName + ".class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);
        assertNotNull(entriesForKey, "Entries should exist for class with special characters");
        assertTrue(entriesForKey.contains(extraDataEntry),
                "Entry should be associated with class name containing special characters");
    }

    /**
     * Test that addExtraDataEntryToClass handles various data entry formats.
     */
    @Test
    public void testAddVariousDataEntryFormats() {
        String keyClassName = "com/example/TestClass";

        // Test with different file extensions
        map.addExtraDataEntryToClass(keyClassName, "data.xml");
        map.addExtraDataEntryToClass(keyClassName, "data.json");
        map.addExtraDataEntryToClass(keyClassName, "data.properties");
        map.addExtraDataEntryToClass(keyClassName, "no_extension");

        String expectedKey = keyClassName + ".class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);
        assertEquals(4, entriesForKey.size(), "Should have 4 entries with different formats");
        assertTrue(entriesForKey.contains("data.xml"), "Should contain XML file");
        assertTrue(entriesForKey.contains("data.json"), "Should contain JSON file");
        assertTrue(entriesForKey.contains("data.properties"), "Should contain properties file");
        assertTrue(entriesForKey.contains("no_extension"), "Should contain file without extension");
    }

    /**
     * Test that addExtraDataEntryToClass can add the same entry to multiple classes.
     */
    @Test
    public void testAddSameEntryToMultipleClasses() {
        String class1 = "com/example/Class1";
        String class2 = "com/example/Class2";
        String class3 = "com/example/Class3";
        String sharedEntry = "shared.txt";

        map.addExtraDataEntryToClass(class1, sharedEntry);
        map.addExtraDataEntryToClass(class2, sharedEntry);
        map.addExtraDataEntryToClass(class3, sharedEntry);

        // Verify the shared entry is associated with all three classes
        assertTrue(map.getExtraDataEntryNames(class1 + ".class").contains(sharedEntry),
                "Shared entry should be associated with Class1");
        assertTrue(map.getExtraDataEntryNames(class2 + ".class").contains(sharedEntry),
                "Shared entry should be associated with Class2");
        assertTrue(map.getExtraDataEntryNames(class3 + ".class").contains(sharedEntry),
                "Shared entry should be associated with Class3");

        // Verify that we have 3 keys
        Set<String> keyNames = map.getKeyDataEntryNames();
        assertEquals(3, keyNames.size(), "Should have 3 different class keys");
    }

    /**
     * Test that addExtraDataEntryToClass works with empty string class name.
     */
    @Test
    public void testAddExtraDataEntryWithEmptyClassName() {
        String emptyClassName = "";
        String extraDataEntry = "empty.txt";

        map.addExtraDataEntryToClass(emptyClassName, extraDataEntry);

        String expectedKey = ".class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);
        assertNotNull(entriesForKey, "Should be able to add entry with empty class name");
        assertTrue(entriesForKey.contains(extraDataEntry),
                "Entry should be associated with empty class name");
    }

    /**
     * Test that addExtraDataEntryToClass handles data entry names with paths.
     */
    @Test
    public void testAddExtraDataEntryWithPaths() {
        String keyClassName = "com/example/MyClass";
        String entryWithPath = "resources/data/extra.txt";

        map.addExtraDataEntryToClass(keyClassName, entryWithPath);

        String expectedKey = keyClassName + ".class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);
        assertTrue(entriesForKey.contains(entryWithPath),
                "Entry with path should be associated correctly");
    }

    /**
     * Test that addExtraDataEntryToClass can be used after other operations.
     */
    @Test
    public void testAddExtraDataEntryAfterOtherOperations() {
        // Perform other operations first
        map.addExtraDataEntry("default.txt");
        map.addExtraDataEntry("key1.txt", "value1.txt");
        map.addExtraClass("com/example/ExtraClass");

        // Now add using addExtraDataEntryToClass
        String keyClassName = "com/example/MyClass";
        String extraDataEntry = "extra.txt";
        map.addExtraDataEntryToClass(keyClassName, extraDataEntry);

        // Verify the new entry is added correctly
        String expectedKey = keyClassName + ".class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);
        assertTrue(entriesForKey.contains(extraDataEntry),
                "Entry should be added correctly after other operations");

        // Verify previous entries are still there
        assertTrue(map.getAllExtraDataEntryNames().contains("default.txt"),
                "Previous default entry should still exist");
        assertTrue(map.getAllExtraDataEntryNames().contains("value1.txt"),
                "Previous keyed entry should still exist");
    }

    /**
     * Test that addExtraDataEntryToClass can add duplicate entries to the same class.
     */
    @Test
    public void testAddDuplicateEntryToSameClass() {
        String keyClassName = "com/example/TestClass";
        String duplicateEntry = "duplicate.txt";

        map.addExtraDataEntryToClass(keyClassName, duplicateEntry);
        map.addExtraDataEntryToClass(keyClassName, duplicateEntry);
        map.addExtraDataEntryToClass(keyClassName, duplicateEntry);

        String expectedKey = keyClassName + ".class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        // Note: The behavior depends on the MultiValueMap implementation.
        // If it allows duplicates, size could be > 1; if it uses a Set, size will be 1.
        // We verify that the entry exists at least once.
        assertTrue(entriesForKey.contains(duplicateEntry),
                "Duplicate entry should be in the map");
    }

    /**
     * Test that addExtraDataEntryToClass interacts correctly with clear().
     */
    @Test
    public void testAddExtraDataEntryThenClear() {
        String keyClassName = "com/example/MyClass";
        String extraDataEntry = "extra.txt";

        map.addExtraDataEntryToClass(keyClassName, extraDataEntry);

        // Verify it was added
        String expectedKey = keyClassName + ".class";
        assertNotNull(map.getExtraDataEntryNames(expectedKey),
                "Entry should exist before clear");

        // Clear the map
        map.clear();

        // Verify it was removed
        Set<String> entriesAfterClear = map.getExtraDataEntryNames(expectedKey);
        assertTrue(entriesAfterClear == null || entriesAfterClear.isEmpty(),
                "Entry should be removed after clear");
    }

    /**
     * Test that addExtraDataEntryToClass works with class names containing numbers.
     */
    @Test
    public void testAddExtraDataEntryWithNumericClassName() {
        String keyClassName = "com/example/Class123";
        String extraDataEntry = "numeric.txt";

        map.addExtraDataEntryToClass(keyClassName, extraDataEntry);

        String expectedKey = keyClassName + ".class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);
        assertTrue(entriesForKey.contains(extraDataEntry),
                "Entry should be associated with numeric class name");
    }

    /**
     * Test that addExtraDataEntryToClass handles long class names and entry names.
     */
    @Test
    public void testAddExtraDataEntryWithLongNames() {
        String longClassName = "com/example/very/long/package/structure/with/many/levels/VeryLongClassNameWithManyCharacters";
        String longEntryName = "very/long/path/to/resource/file/with/many/levels/and/a/very/long/filename.txt";

        map.addExtraDataEntryToClass(longClassName, longEntryName);

        String expectedKey = longClassName + ".class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);
        assertTrue(entriesForKey.contains(longEntryName),
                "Entry with long name should be associated with long class name");
    }

    /**
     * Test that addExtraDataEntryToClass method does not throw exceptions with valid inputs.
     */
    @Test
    public void testAddExtraDataEntryDoesNotThrowWithValidInputs() {
        assertDoesNotThrow(() -> map.addExtraDataEntryToClass("com/example/Test", "test.txt"),
                "Should not throw exception with valid inputs");
    }

    /**
     * Test that entries added via addExtraDataEntryToClass are retrievable via getAllExtraDataEntryNames.
     */
    @Test
    public void testEntriesRetrievableViaGetAllExtraDataEntryNames() {
        String keyClassName = "com/example/MyClass";
        String entry1 = "extra1.txt";
        String entry2 = "extra2.txt";

        map.addExtraDataEntryToClass(keyClassName, entry1);
        map.addExtraDataEntryToClass(keyClassName, entry2);

        Set<String> allEntries = map.getAllExtraDataEntryNames();
        assertTrue(allEntries.contains(entry1), "All entries should contain entry1");
        assertTrue(allEntries.contains(entry2), "All entries should contain entry2");
    }

    /**
     * Test that the key generated by addExtraDataEntryToClass matches the pattern used by getClassDataEntryName.
     */
    @Test
    public void testKeyFormatMatchesGetClassDataEntryName() {
        String keyClassName = "com/example/MyClass";
        String extraDataEntry = "extra.txt";

        map.addExtraDataEntryToClass(keyClassName, extraDataEntry);

        // The key should be created using getClassDataEntryName, which we can also call directly
        String expectedKey = map.getClassDataEntryName(keyClassName);

        // Verify this key exists in the map
        Set<String> keyNames = map.getKeyDataEntryNames();
        assertTrue(keyNames.contains(expectedKey),
                "Key should match the format from getClassDataEntryName");

        // Verify the entry is associated with this key
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);
        assertTrue(entriesForKey.contains(extraDataEntry),
                "Entry should be associated with key from getClassDataEntryName");
    }
}
