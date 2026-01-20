package proguard.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for ExtraDataEntryNameMap.addExtraClassToClass methods:
 * - addExtraClassToClass(Clazz, Clazz)
 * - addExtraClassToClass(Clazz, Class)
 * - addExtraClassToClass(Clazz, String)
 * - addExtraClassToClass(String, Class)
 * - addExtraClassToClass(String, String)
 *
 * Note: Tests for addExtraClassToClass(Clazz, Clazz) use Mockito to mock Clazz instances because:
 * 1. Clazz is an interface from proguard.classfile package
 * 2. The method only calls getName() on the Clazz objects to convert them to class data entry names
 * 3. Creating concrete Clazz implementations would require complex class file parsing infrastructure
 * 4. Mocking allows us to test the method's behavior (mapping relationships) without this complexity
 * 5. The core logic being tested is the mapping behavior, not Clazz instantiation
 *
 * Tests for addExtraClassToClass(Clazz, Class) use real Java Class objects (like String.class)
 * because the Class parameter is java.lang.Class, not ProGuard's Clazz interface.
 *
 * Tests for addExtraClassToClass(Clazz, String) use real String class names (no mocking needed)
 * because the String parameter is just a class name in internal format.
 *
 * Tests for addExtraClassToClass(String, Class) use NO mocking at all - both parameters are
 * simple types (String for class name, Class for Java reflection class).
 *
 * Tests for addExtraClassToClass(String, String) use NO mocking at all - both parameters are
 * simple String class names in internal format. This is the simplest variant to test.
 */
public class ExtraDataEntryNameMapClaude_addExtraClassToClassTest {

    private ExtraDataEntryNameMap map;

    @BeforeEach
    public void setUp() {
        map = new ExtraDataEntryNameMap();
    }

    /**
     * Test that addExtraClassToClass adds a mapping between two classes.
     */
    @Test
    public void testAddExtraClassToClassWithSimpleClasses() {
        Clazz keyClass = mock(Clazz.class);
        Clazz extraClass = mock(Clazz.class);
        when(keyClass.getName()).thenReturn("com/example/KeyClass");
        when(extraClass.getName()).thenReturn("com/example/ExtraClass");

        map.addExtraClassToClass(keyClass, extraClass);

        // Verify the mapping was created
        String expectedKey = "com/example/KeyClass.class";
        String expectedValue = "com/example/ExtraClass.class";

        Set<String> keyNames = map.getKeyDataEntryNames();
        assertTrue(keyNames.contains(expectedKey), "Key class should be in the map");

        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);
        assertNotNull(entriesForKey, "Entries for key should not be null");
        assertTrue(entriesForKey.contains(expectedValue), "Extra class should be associated with key class");

        // Verify getName() was called on both classes
        verify(keyClass, atLeastOnce()).getName();
        verify(extraClass, atLeastOnce()).getName();
    }

    /**
     * Test that addExtraClassToClass can add multiple extra classes to the same key class.
     */
    @Test
    public void testAddMultipleExtraClassesToSameKeyClass() {
        Clazz keyClass = mock(Clazz.class);
        Clazz extraClass1 = mock(Clazz.class);
        Clazz extraClass2 = mock(Clazz.class);
        Clazz extraClass3 = mock(Clazz.class);

        when(keyClass.getName()).thenReturn("com/example/KeyClass");
        when(extraClass1.getName()).thenReturn("com/example/Extra1");
        when(extraClass2.getName()).thenReturn("com/example/Extra2");
        when(extraClass3.getName()).thenReturn("com/example/Extra3");

        map.addExtraClassToClass(keyClass, extraClass1);
        map.addExtraClassToClass(keyClass, extraClass2);
        map.addExtraClassToClass(keyClass, extraClass3);

        String expectedKey = "com/example/KeyClass.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertEquals(3, entriesForKey.size(), "Should have 3 extra classes for the key");
        assertTrue(entriesForKey.contains("com/example/Extra1.class"), "Should contain first extra class");
        assertTrue(entriesForKey.contains("com/example/Extra2.class"), "Should contain second extra class");
        assertTrue(entriesForKey.contains("com/example/Extra3.class"), "Should contain third extra class");
    }

    /**
     * Test that addExtraClassToClass can handle different key classes with different extra classes.
     */
    @Test
    public void testAddExtraClassesToDifferentKeyClasses() {
        Clazz keyClass1 = mock(Clazz.class);
        Clazz keyClass2 = mock(Clazz.class);
        Clazz extraClass1 = mock(Clazz.class);
        Clazz extraClass2 = mock(Clazz.class);

        when(keyClass1.getName()).thenReturn("com/example/Key1");
        when(keyClass2.getName()).thenReturn("com/example/Key2");
        when(extraClass1.getName()).thenReturn("com/example/Extra1");
        when(extraClass2.getName()).thenReturn("com/example/Extra2");

        map.addExtraClassToClass(keyClass1, extraClass1);
        map.addExtraClassToClass(keyClass2, extraClass2);

        // Verify both keys exist
        Set<String> keyNames = map.getKeyDataEntryNames();
        assertTrue(keyNames.contains("com/example/Key1.class"), "First key should be in map");
        assertTrue(keyNames.contains("com/example/Key2.class"), "Second key should be in map");

        // Verify correct associations
        Set<String> entriesForKey1 = map.getExtraDataEntryNames("com/example/Key1.class");
        assertTrue(entriesForKey1.contains("com/example/Extra1.class"), "Extra1 should be associated with Key1");
        assertFalse(entriesForKey1.contains("com/example/Extra2.class"), "Extra2 should not be associated with Key1");

        Set<String> entriesForKey2 = map.getExtraDataEntryNames("com/example/Key2.class");
        assertTrue(entriesForKey2.contains("com/example/Extra2.class"), "Extra2 should be associated with Key2");
        assertFalse(entriesForKey2.contains("com/example/Extra1.class"), "Extra1 should not be associated with Key2");
    }

    /**
     * Test that addExtraClassToClass handles classes with nested package names.
     */
    @Test
    public void testAddExtraClassWithNestedPackages() {
        Clazz keyClass = mock(Clazz.class);
        Clazz extraClass = mock(Clazz.class);

        when(keyClass.getName()).thenReturn("com/example/deep/nested/package/structure/KeyClass");
        when(extraClass.getName()).thenReturn("org/another/deeply/nested/package/ExtraClass");

        map.addExtraClassToClass(keyClass, extraClass);

        String expectedKey = "com/example/deep/nested/package/structure/KeyClass.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertTrue(entriesForKey.contains("org/another/deeply/nested/package/ExtraClass.class"),
                "Should handle deeply nested package structures");
    }

    /**
     * Test that addExtraClassToClass handles inner classes with special characters.
     */
    @Test
    public void testAddExtraClassWithInnerClasses() {
        Clazz keyClass = mock(Clazz.class);
        Clazz extraClass = mock(Clazz.class);

        when(keyClass.getName()).thenReturn("com/example/OuterClass$InnerClass");
        when(extraClass.getName()).thenReturn("com/example/AnotherOuter$AnotherInner");

        map.addExtraClassToClass(keyClass, extraClass);

        String expectedKey = "com/example/OuterClass$InnerClass.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertTrue(entriesForKey.contains("com/example/AnotherOuter$AnotherInner.class"),
                "Should handle inner class naming with $ separator");
    }

    /**
     * Test that addExtraClassToClass can add the same extra class to multiple key classes.
     */
    @Test
    public void testAddSameExtraClassToMultipleKeyClasses() {
        Clazz keyClass1 = mock(Clazz.class);
        Clazz keyClass2 = mock(Clazz.class);
        Clazz keyClass3 = mock(Clazz.class);
        Clazz sharedExtraClass = mock(Clazz.class);

        when(keyClass1.getName()).thenReturn("com/example/Key1");
        when(keyClass2.getName()).thenReturn("com/example/Key2");
        when(keyClass3.getName()).thenReturn("com/example/Key3");
        when(sharedExtraClass.getName()).thenReturn("com/example/SharedExtra");

        map.addExtraClassToClass(keyClass1, sharedExtraClass);
        map.addExtraClassToClass(keyClass2, sharedExtraClass);
        map.addExtraClassToClass(keyClass3, sharedExtraClass);

        // Verify the shared extra class is associated with all three key classes
        assertTrue(map.getExtraDataEntryNames("com/example/Key1.class").contains("com/example/SharedExtra.class"),
                "Shared extra class should be associated with Key1");
        assertTrue(map.getExtraDataEntryNames("com/example/Key2.class").contains("com/example/SharedExtra.class"),
                "Shared extra class should be associated with Key2");
        assertTrue(map.getExtraDataEntryNames("com/example/Key3.class").contains("com/example/SharedExtra.class"),
                "Shared extra class should be associated with Key3");

        // Verify we have 3 different keys
        assertEquals(3, map.getKeyDataEntryNames().size(), "Should have 3 different key classes");
    }

    /**
     * Test that addExtraClassToClass can map a class to itself.
     */
    @Test
    public void testAddClassToItself() {
        Clazz clazz = mock(Clazz.class);
        when(clazz.getName()).thenReturn("com/example/SelfReferencing");

        map.addExtraClassToClass(clazz, clazz);

        String expectedKey = "com/example/SelfReferencing.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertTrue(entriesForKey.contains("com/example/SelfReferencing.class"),
                "A class should be able to be mapped to itself");
    }

    /**
     * Test that addExtraClassToClass works with classes having simple names (no package).
     */
    @Test
    public void testAddExtraClassWithSimpleNames() {
        Clazz keyClass = mock(Clazz.class);
        Clazz extraClass = mock(Clazz.class);

        when(keyClass.getName()).thenReturn("SimpleKey");
        when(extraClass.getName()).thenReturn("SimpleExtra");

        map.addExtraClassToClass(keyClass, extraClass);

        String expectedKey = "SimpleKey.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertTrue(entriesForKey.contains("SimpleExtra.class"),
                "Should handle classes with simple names (no package)");
    }

    /**
     * Test that addExtraClassToClass works with classes after other operations.
     */
    @Test
    public void testAddExtraClassAfterOtherOperations() {
        // Perform other operations first
        map.addExtraDataEntry("default.txt");
        map.addExtraDataEntry("key.txt", "value.txt");

        // Now add class-to-class mapping
        Clazz keyClass = mock(Clazz.class);
        Clazz extraClass = mock(Clazz.class);
        when(keyClass.getName()).thenReturn("com/example/Key");
        when(extraClass.getName()).thenReturn("com/example/Extra");

        map.addExtraClassToClass(keyClass, extraClass);

        // Verify the new mapping exists
        String expectedKey = "com/example/Key.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);
        assertTrue(entriesForKey.contains("com/example/Extra.class"),
                "Class mapping should be added correctly after other operations");

        // Verify previous entries still exist
        assertTrue(map.getAllExtraDataEntryNames().contains("default.txt"),
                "Previous default entry should still exist");
        assertTrue(map.getAllExtraDataEntryNames().contains("value.txt"),
                "Previous keyed entry should still exist");
    }

    /**
     * Test that addExtraClassToClass interacts correctly with clear().
     */
    @Test
    public void testAddExtraClassThenClear() {
        Clazz keyClass = mock(Clazz.class);
        Clazz extraClass = mock(Clazz.class);
        when(keyClass.getName()).thenReturn("com/example/Key");
        when(extraClass.getName()).thenReturn("com/example/Extra");

        map.addExtraClassToClass(keyClass, extraClass);

        // Verify it was added
        String expectedKey = "com/example/Key.class";
        assertNotNull(map.getExtraDataEntryNames(expectedKey), "Entry should exist before clear");

        // Clear the map
        map.clear();

        // Verify it was removed
        Set<String> entriesAfterClear = map.getExtraDataEntryNames(expectedKey);
        assertTrue(entriesAfterClear == null || entriesAfterClear.isEmpty(),
                "Entry should be removed after clear");
    }

    /**
     * Test that addExtraClassToClass results are retrievable via getAllExtraDataEntryNames.
     */
    @Test
    public void testExtraClassesRetrievableViaGetAllExtraDataEntryNames() {
        Clazz keyClass = mock(Clazz.class);
        Clazz extraClass1 = mock(Clazz.class);
        Clazz extraClass2 = mock(Clazz.class);

        when(keyClass.getName()).thenReturn("com/example/Key");
        when(extraClass1.getName()).thenReturn("com/example/Extra1");
        when(extraClass2.getName()).thenReturn("com/example/Extra2");

        map.addExtraClassToClass(keyClass, extraClass1);
        map.addExtraClassToClass(keyClass, extraClass2);

        Set<String> allEntries = map.getAllExtraDataEntryNames();
        assertTrue(allEntries.contains("com/example/Extra1.class"), "All entries should contain Extra1");
        assertTrue(allEntries.contains("com/example/Extra2.class"), "All entries should contain Extra2");
    }

    /**
     * Test that addExtraClassToClass handles anonymous classes (with numbers in name).
     */
    @Test
    public void testAddExtraClassWithAnonymousClasses() {
        Clazz keyClass = mock(Clazz.class);
        Clazz extraClass = mock(Clazz.class);

        when(keyClass.getName()).thenReturn("com/example/OuterClass$1");
        when(extraClass.getName()).thenReturn("com/example/OuterClass$2");

        map.addExtraClassToClass(keyClass, extraClass);

        String expectedKey = "com/example/OuterClass$1.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertTrue(entriesForKey.contains("com/example/OuterClass$2.class"),
                "Should handle anonymous class naming");
    }

    /**
     * Test that addExtraClassToClass does not throw exceptions with valid inputs.
     */
    @Test
    public void testAddExtraClassDoesNotThrowWithValidInputs() {
        Clazz keyClass = mock(Clazz.class);
        Clazz extraClass = mock(Clazz.class);
        when(keyClass.getName()).thenReturn("com/example/Key");
        when(extraClass.getName()).thenReturn("com/example/Extra");

        assertDoesNotThrow(() -> map.addExtraClassToClass(keyClass, extraClass),
                "Should not throw exception with valid inputs");
    }

    /**
     * Test that addExtraClassToClass can be called multiple times with the same pair.
     */
    @Test
    public void testAddSameClassPairMultipleTimes() {
        Clazz keyClass = mock(Clazz.class);
        Clazz extraClass = mock(Clazz.class);
        when(keyClass.getName()).thenReturn("com/example/Key");
        when(extraClass.getName()).thenReturn("com/example/Extra");

        map.addExtraClassToClass(keyClass, extraClass);
        map.addExtraClassToClass(keyClass, extraClass);
        map.addExtraClassToClass(keyClass, extraClass);

        String expectedKey = "com/example/Key.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        // The behavior depends on MultiValueMap implementation
        // We verify the entry exists at least once
        assertTrue(entriesForKey.contains("com/example/Extra.class"),
                "Extra class should be in the map after multiple additions");
    }

    /**
     * Test that addExtraClassToClass creates entries retrievable via getKeyDataEntryNames.
     */
    @Test
    public void testKeyClassRetrievableViaGetKeyDataEntryNames() {
        Clazz keyClass = mock(Clazz.class);
        Clazz extraClass = mock(Clazz.class);
        when(keyClass.getName()).thenReturn("com/example/KeyClass");
        when(extraClass.getName()).thenReturn("com/example/ExtraClass");

        map.addExtraClassToClass(keyClass, extraClass);

        Set<String> keyNames = map.getKeyDataEntryNames();
        assertTrue(keyNames.contains("com/example/KeyClass.class"),
                "Key class with .class extension should be in key data entry names");
    }

    /**
     * Test that addExtraClassToClass works correctly when mixing with other add methods.
     */
    @Test
    public void testMixingWithOtherAddMethods() {
        // Add using different methods
        map.addExtraDataEntry("standalone.txt");
        map.addExtraDataEntryToClass("com/example/Class1", "extra1.txt");

        Clazz keyClass = mock(Clazz.class);
        Clazz extraClass = mock(Clazz.class);
        when(keyClass.getName()).thenReturn("com/example/Class2");
        when(extraClass.getName()).thenReturn("com/example/Class3");

        map.addExtraClassToClass(keyClass, extraClass);

        // Verify all entries coexist
        Set<String> allEntries = map.getAllExtraDataEntryNames();
        assertTrue(allEntries.contains("standalone.txt"), "Standalone entry should exist");
        assertTrue(allEntries.contains("extra1.txt"), "Entry from addExtraDataEntryToClass should exist");
        assertTrue(allEntries.contains("com/example/Class3.class"), "Entry from addExtraClassToClass should exist");

        // Verify correct associations
        String key2 = "com/example/Class2.class";
        assertTrue(map.getExtraDataEntryNames(key2).contains("com/example/Class3.class"),
                "Class2 should be associated with Class3");
    }

    /**
     * Test that addExtraClassToClass handles classes with underscores in names.
     */
    @Test
    public void testAddExtraClassWithUnderscoresInName() {
        Clazz keyClass = mock(Clazz.class);
        Clazz extraClass = mock(Clazz.class);

        when(keyClass.getName()).thenReturn("com/example/My_Key_Class");
        when(extraClass.getName()).thenReturn("com/example/My_Extra_Class");

        map.addExtraClassToClass(keyClass, extraClass);

        String expectedKey = "com/example/My_Key_Class.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertTrue(entriesForKey.contains("com/example/My_Extra_Class.class"),
                "Should handle underscores in class names");
    }

    /**
     * Test that the method correctly delegates to the underlying addExtraDataEntry method.
     */
    @Test
    public void testDelegationToAddExtraDataEntry() {
        Clazz keyClass = mock(Clazz.class);
        Clazz extraClass = mock(Clazz.class);
        when(keyClass.getName()).thenReturn("com/example/Key");
        when(extraClass.getName()).thenReturn("com/example/Extra");

        map.addExtraClassToClass(keyClass, extraClass);

        // This should be equivalent to calling:
        // map.addExtraDataEntry("com/example/Key.class", "com/example/Extra.class");
        // Verify by checking the result is the same
        ExtraDataEntryNameMap map2 = new ExtraDataEntryNameMap();
        map2.addExtraDataEntry("com/example/Key.class", "com/example/Extra.class");

        // Both maps should have the same keys and values
        assertEquals(map.getKeyDataEntryNames(), map2.getKeyDataEntryNames(),
                "Key names should match when using equivalent operations");
        assertEquals(map.getAllExtraDataEntryNames(), map2.getAllExtraDataEntryNames(),
                "All entry names should match when using equivalent operations");
    }

    // ========== Tests for addExtraClassToClass(Clazz, Class) ==========

    /**
     * Test that addExtraClassToClass(Clazz, Class) adds a mapping with Java Class object.
     * This test does NOT use mocking for the Class parameter since it's a real Java class.
     */
    @Test
    public void testAddExtraClassToClass_ClazzAndJavaClass_Simple() {
        Clazz keyClass = mock(Clazz.class);
        when(keyClass.getName()).thenReturn("com/example/KeyClass");

        // Use a real Java Class object
        Class<?> extraClass = String.class;

        map.addExtraClassToClass(keyClass, extraClass);

        // Verify the mapping was created
        // String.class should be converted to "java/lang/String.class"
        String expectedKey = "com/example/KeyClass.class";
        String expectedValue = "java/lang/String.class";

        Set<String> keyNames = map.getKeyDataEntryNames();
        assertTrue(keyNames.contains(expectedKey), "Key class should be in the map");

        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);
        assertNotNull(entriesForKey, "Entries for key should not be null");
        assertTrue(entriesForKey.contains(expectedValue),
                "Java Class should be converted to internal format and associated with key");
    }

    /**
     * Test addExtraClassToClass(Clazz, Class) with various Java standard library classes.
     */
    @Test
    public void testAddExtraClassToClass_ClazzAndJavaClass_VariousStandardClasses() {
        Clazz keyClass = mock(Clazz.class);
        when(keyClass.getName()).thenReturn("com/example/Key");

        // Add various standard Java classes
        map.addExtraClassToClass(keyClass, Integer.class);
        map.addExtraClassToClass(keyClass, java.util.ArrayList.class);
        map.addExtraClassToClass(keyClass, java.io.File.class);

        String expectedKey = "com/example/Key.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertEquals(3, entriesForKey.size(), "Should have 3 Java classes mapped");
        assertTrue(entriesForKey.contains("java/lang/Integer.class"), "Should contain Integer");
        assertTrue(entriesForKey.contains("java/util/ArrayList.class"), "Should contain ArrayList");
        assertTrue(entriesForKey.contains("java/io/File.class"), "Should contain File");
    }

    /**
     * Test addExtraClassToClass(Clazz, Class) with inner classes.
     */
    @Test
    public void testAddExtraClassToClass_ClazzAndJavaClass_InnerClass() {
        Clazz keyClass = mock(Clazz.class);
        when(keyClass.getName()).thenReturn("com/example/Key");

        // Use a Java inner class
        Class<?> innerClass = java.util.Map.Entry.class;

        map.addExtraClassToClass(keyClass, innerClass);

        String expectedKey = "com/example/Key.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        // Map.Entry should be converted to "java/util/Map$Entry.class"
        assertTrue(entriesForKey.contains("java/util/Map$Entry.class"),
                "Inner class should be converted with $ separator");
    }

    /**
     * Test addExtraClassToClass(Clazz, Class) with primitive wrapper classes.
     */
    @Test
    public void testAddExtraClassToClass_ClazzAndJavaClass_PrimitiveWrappers() {
        Clazz keyClass = mock(Clazz.class);
        when(keyClass.getName()).thenReturn("com/example/Key");

        map.addExtraClassToClass(keyClass, Boolean.class);
        map.addExtraClassToClass(keyClass, Character.class);
        map.addExtraClassToClass(keyClass, Byte.class);
        map.addExtraClassToClass(keyClass, Short.class);
        map.addExtraClassToClass(keyClass, Long.class);
        map.addExtraClassToClass(keyClass, Float.class);
        map.addExtraClassToClass(keyClass, Double.class);

        String expectedKey = "com/example/Key.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertTrue(entriesForKey.contains("java/lang/Boolean.class"), "Should contain Boolean");
        assertTrue(entriesForKey.contains("java/lang/Character.class"), "Should contain Character");
        assertTrue(entriesForKey.contains("java/lang/Byte.class"), "Should contain Byte");
        assertTrue(entriesForKey.contains("java/lang/Short.class"), "Should contain Short");
        assertTrue(entriesForKey.contains("java/lang/Long.class"), "Should contain Long");
        assertTrue(entriesForKey.contains("java/lang/Float.class"), "Should contain Float");
        assertTrue(entriesForKey.contains("java/lang/Double.class"), "Should contain Double");
    }

    /**
     * Test addExtraClassToClass(Clazz, Class) can add the same Java class to multiple key classes.
     */
    @Test
    public void testAddExtraClassToClass_ClazzAndJavaClass_SameClassToMultipleKeys() {
        Clazz keyClass1 = mock(Clazz.class);
        Clazz keyClass2 = mock(Clazz.class);
        Clazz keyClass3 = mock(Clazz.class);

        when(keyClass1.getName()).thenReturn("com/example/Key1");
        when(keyClass2.getName()).thenReturn("com/example/Key2");
        when(keyClass3.getName()).thenReturn("com/example/Key3");

        Class<?> sharedClass = Object.class;

        map.addExtraClassToClass(keyClass1, sharedClass);
        map.addExtraClassToClass(keyClass2, sharedClass);
        map.addExtraClassToClass(keyClass3, sharedClass);

        // Verify Object.class is associated with all three keys
        assertTrue(map.getExtraDataEntryNames("com/example/Key1.class").contains("java/lang/Object.class"),
                "Object should be associated with Key1");
        assertTrue(map.getExtraDataEntryNames("com/example/Key2.class").contains("java/lang/Object.class"),
                "Object should be associated with Key2");
        assertTrue(map.getExtraDataEntryNames("com/example/Key3.class").contains("java/lang/Object.class"),
                "Object should be associated with Key3");
    }

    /**
     * Test addExtraClassToClass(Clazz, Class) with multiple Java classes per key.
     */
    @Test
    public void testAddExtraClassToClass_ClazzAndJavaClass_MultipleClassesPerKey() {
        Clazz keyClass = mock(Clazz.class);
        when(keyClass.getName()).thenReturn("com/example/Key");

        map.addExtraClassToClass(keyClass, String.class);
        map.addExtraClassToClass(keyClass, Integer.class);
        map.addExtraClassToClass(keyClass, Object.class);
        map.addExtraClassToClass(keyClass, Exception.class);

        String expectedKey = "com/example/Key.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertEquals(4, entriesForKey.size(), "Should have 4 Java classes");
        assertTrue(entriesForKey.contains("java/lang/String.class"), "Should contain String");
        assertTrue(entriesForKey.contains("java/lang/Integer.class"), "Should contain Integer");
        assertTrue(entriesForKey.contains("java/lang/Object.class"), "Should contain Object");
        assertTrue(entriesForKey.contains("java/lang/Exception.class"), "Should contain Exception");
    }

    /**
     * Test addExtraClassToClass(Clazz, Class) with different key classes and different Java classes.
     */
    @Test
    public void testAddExtraClassToClass_ClazzAndJavaClass_DifferentKeysAndClasses() {
        Clazz keyClass1 = mock(Clazz.class);
        Clazz keyClass2 = mock(Clazz.class);

        when(keyClass1.getName()).thenReturn("com/example/Key1");
        when(keyClass2.getName()).thenReturn("com/example/Key2");

        map.addExtraClassToClass(keyClass1, String.class);
        map.addExtraClassToClass(keyClass2, Integer.class);

        // Verify correct associations
        Set<String> entriesForKey1 = map.getExtraDataEntryNames("com/example/Key1.class");
        assertTrue(entriesForKey1.contains("java/lang/String.class"), "String should be with Key1");
        assertFalse(entriesForKey1.contains("java/lang/Integer.class"), "Integer should not be with Key1");

        Set<String> entriesForKey2 = map.getExtraDataEntryNames("com/example/Key2.class");
        assertTrue(entriesForKey2.contains("java/lang/Integer.class"), "Integer should be with Key2");
        assertFalse(entriesForKey2.contains("java/lang/String.class"), "String should not be with Key2");
    }

    /**
     * Test addExtraClassToClass(Clazz, Class) interacts correctly with clear().
     */
    @Test
    public void testAddExtraClassToClass_ClazzAndJavaClass_ThenClear() {
        Clazz keyClass = mock(Clazz.class);
        when(keyClass.getName()).thenReturn("com/example/Key");

        map.addExtraClassToClass(keyClass, String.class);

        // Verify it was added
        String expectedKey = "com/example/Key.class";
        assertNotNull(map.getExtraDataEntryNames(expectedKey), "Entry should exist before clear");

        // Clear the map
        map.clear();

        // Verify it was removed
        Set<String> entriesAfterClear = map.getExtraDataEntryNames(expectedKey);
        assertTrue(entriesAfterClear == null || entriesAfterClear.isEmpty(),
                "Entry should be removed after clear");
    }

    /**
     * Test addExtraClassToClass(Clazz, Class) works after other operations.
     */
    @Test
    public void testAddExtraClassToClass_ClazzAndJavaClass_AfterOtherOperations() {
        // Perform other operations first
        map.addExtraDataEntry("default.txt");
        map.addExtraDataEntryToClass("com/example/Class1", "extra1.txt");

        // Add using addExtraClassToClass with Java Class
        Clazz keyClass = mock(Clazz.class);
        when(keyClass.getName()).thenReturn("com/example/Key");

        map.addExtraClassToClass(keyClass, String.class);

        // Verify the new mapping exists
        String expectedKey = "com/example/Key.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);
        assertTrue(entriesForKey.contains("java/lang/String.class"),
                "Java Class mapping should be added correctly");

        // Verify previous entries still exist
        assertTrue(map.getAllExtraDataEntryNames().contains("default.txt"),
                "Previous default entry should still exist");
        assertTrue(map.getAllExtraDataEntryNames().contains("extra1.txt"),
                "Previous keyed entry should still exist");
    }

    /**
     * Test addExtraClassToClass(Clazz, Class) does not throw with valid inputs.
     */
    @Test
    public void testAddExtraClassToClass_ClazzAndJavaClass_DoesNotThrow() {
        Clazz keyClass = mock(Clazz.class);
        when(keyClass.getName()).thenReturn("com/example/Key");

        assertDoesNotThrow(() -> map.addExtraClassToClass(keyClass, String.class),
                "Should not throw exception with valid inputs");
    }

    /**
     * Test addExtraClassToClass(Clazz, Class) results are retrievable via getAllExtraDataEntryNames.
     */
    @Test
    public void testAddExtraClassToClass_ClazzAndJavaClass_RetrievableViaGetAll() {
        Clazz keyClass = mock(Clazz.class);
        when(keyClass.getName()).thenReturn("com/example/Key");

        map.addExtraClassToClass(keyClass, String.class);
        map.addExtraClassToClass(keyClass, Integer.class);

        Set<String> allEntries = map.getAllExtraDataEntryNames();
        assertTrue(allEntries.contains("java/lang/String.class"), "All entries should contain String");
        assertTrue(allEntries.contains("java/lang/Integer.class"), "All entries should contain Integer");
    }

    /**
     * Test addExtraClassToClass(Clazz, Class) can be called multiple times with same pair.
     */
    @Test
    public void testAddExtraClassToClass_ClazzAndJavaClass_MultipleTimes() {
        Clazz keyClass = mock(Clazz.class);
        when(keyClass.getName()).thenReturn("com/example/Key");

        map.addExtraClassToClass(keyClass, String.class);
        map.addExtraClassToClass(keyClass, String.class);
        map.addExtraClassToClass(keyClass, String.class);

        String expectedKey = "com/example/Key.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        // Verify the entry exists (behavior depends on MultiValueMap)
        assertTrue(entriesForKey.contains("java/lang/String.class"),
                "String class should be in the map after multiple additions");
    }

    /**
     * Test addExtraClassToClass(Clazz, Class) with array classes.
     */
    @Test
    public void testAddExtraClassToClass_ClazzAndJavaClass_ArrayClass() {
        Clazz keyClass = mock(Clazz.class);
        when(keyClass.getName()).thenReturn("com/example/Key");

        // Use array classes
        map.addExtraClassToClass(keyClass, String[].class);
        map.addExtraClassToClass(keyClass, int[].class);

        String expectedKey = "com/example/Key.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        // Array classes are converted to internal format
        // String[] becomes "[Ljava/lang/String;"
        // int[] becomes "[I"
        assertTrue(entriesForKey.contains("[Ljava/lang/String;.class"),
                "String array should be converted to internal format");
        assertTrue(entriesForKey.contains("[I.class"),
                "int array should be converted to internal format");
    }

    /**
     * Test addExtraClassToClass(Clazz, Class) mixing with addExtraClassToClass(Clazz, Clazz).
     */
    @Test
    public void testAddExtraClassToClass_ClazzAndJavaClass_MixingWithClazzClazz() {
        Clazz keyClass = mock(Clazz.class);
        Clazz extraProguardClass = mock(Clazz.class);

        when(keyClass.getName()).thenReturn("com/example/Key");
        when(extraProguardClass.getName()).thenReturn("com/example/ProguardExtra");

        // Mix both method variants
        map.addExtraClassToClass(keyClass, extraProguardClass);  // Clazz, Clazz
        map.addExtraClassToClass(keyClass, String.class);         // Clazz, Class

        String expectedKey = "com/example/Key.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertEquals(2, entriesForKey.size(), "Should have 2 entries from different method variants");
        assertTrue(entriesForKey.contains("com/example/ProguardExtra.class"),
                "Should contain entry from Clazz, Clazz variant");
        assertTrue(entriesForKey.contains("java/lang/String.class"),
                "Should contain entry from Clazz, Class variant");
    }

    /**
     * Test that addExtraClassToClass(Clazz, Class) correctly converts Java class names.
     */
    @Test
    public void testAddExtraClassToClass_ClazzAndJavaClass_NameConversion() {
        Clazz keyClass = mock(Clazz.class);
        when(keyClass.getName()).thenReturn("com/example/Key");

        // Test with a class that has dots in the fully qualified name
        map.addExtraClassToClass(keyClass, java.util.concurrent.ConcurrentHashMap.class);

        String expectedKey = "com/example/Key.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        // java.util.concurrent.ConcurrentHashMap should become java/util/concurrent/ConcurrentHashMap
        assertTrue(entriesForKey.contains("java/util/concurrent/ConcurrentHashMap.class"),
                "Dots in package name should be converted to slashes");
    }

    /**
     * Test addExtraClassToClass(Clazz, Class) with interface classes.
     */
    @Test
    public void testAddExtraClassToClass_ClazzAndJavaClass_Interfaces() {
        Clazz keyClass = mock(Clazz.class);
        when(keyClass.getName()).thenReturn("com/example/Key");

        // Use Java interface classes
        map.addExtraClassToClass(keyClass, java.util.List.class);
        map.addExtraClassToClass(keyClass, java.io.Serializable.class);
        map.addExtraClassToClass(keyClass, Runnable.class);

        String expectedKey = "com/example/Key.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertTrue(entriesForKey.contains("java/util/List.class"), "Should contain List interface");
        assertTrue(entriesForKey.contains("java/io/Serializable.class"), "Should contain Serializable interface");
        assertTrue(entriesForKey.contains("java/lang/Runnable.class"), "Should contain Runnable interface");
    }

    /**
     * Test that addExtraClassToClass(Clazz, Class) delegation works correctly.
     */
    @Test
    public void testAddExtraClassToClass_ClazzAndJavaClass_Delegation() {
        Clazz keyClass = mock(Clazz.class);
        when(keyClass.getName()).thenReturn("com/example/Key");

        map.addExtraClassToClass(keyClass, String.class);

        // This should be equivalent to:
        // map.addExtraDataEntry("com/example/Key.class", "java/lang/String.class");
        ExtraDataEntryNameMap map2 = new ExtraDataEntryNameMap();
        map2.addExtraDataEntry("com/example/Key.class", "java/lang/String.class");

        // Both maps should have the same result
        assertEquals(map.getKeyDataEntryNames(), map2.getKeyDataEntryNames(),
                "Key names should match");
        assertEquals(map.getAllExtraDataEntryNames(), map2.getAllExtraDataEntryNames(),
                "All entry names should match");
    }

    // ========== Tests for addExtraClassToClass(Clazz, String) ==========

    /**
     * Test that addExtraClassToClass(Clazz, String) adds a mapping with String class name.
     * This test does NOT use mocking for the String parameter since it's just a plain String.
     */
    @Test
    public void testAddExtraClassToClass_ClazzAndString_Simple() {
        Clazz keyClass = mock(Clazz.class);
        when(keyClass.getName()).thenReturn("com/example/KeyClass");

        // Use a simple string class name in internal format
        String extraClassName = "com/example/ExtraClass";

        map.addExtraClassToClass(keyClass, extraClassName);

        // Verify the mapping was created
        String expectedKey = "com/example/KeyClass.class";
        String expectedValue = "com/example/ExtraClass.class";

        Set<String> keyNames = map.getKeyDataEntryNames();
        assertTrue(keyNames.contains(expectedKey), "Key class should be in the map");

        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);
        assertNotNull(entriesForKey, "Entries for key should not be null");
        assertTrue(entriesForKey.contains(expectedValue),
                "String class name should be appended with .class and associated with key");
    }

    /**
     * Test addExtraClassToClass(Clazz, String) with multiple string class names.
     */
    @Test
    public void testAddExtraClassToClass_ClazzAndString_MultipleClasses() {
        Clazz keyClass = mock(Clazz.class);
        when(keyClass.getName()).thenReturn("com/example/Key");

        // Add multiple string class names
        map.addExtraClassToClass(keyClass, "com/example/Extra1");
        map.addExtraClassToClass(keyClass, "com/example/Extra2");
        map.addExtraClassToClass(keyClass, "com/example/Extra3");

        String expectedKey = "com/example/Key.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertEquals(3, entriesForKey.size(), "Should have 3 string classes mapped");
        assertTrue(entriesForKey.contains("com/example/Extra1.class"), "Should contain Extra1");
        assertTrue(entriesForKey.contains("com/example/Extra2.class"), "Should contain Extra2");
        assertTrue(entriesForKey.contains("com/example/Extra3.class"), "Should contain Extra3");
    }

    /**
     * Test addExtraClassToClass(Clazz, String) with nested package names.
     */
    @Test
    public void testAddExtraClassToClass_ClazzAndString_NestedPackages() {
        Clazz keyClass = mock(Clazz.class);
        when(keyClass.getName()).thenReturn("com/example/Key");

        String extraClassName = "org/deeply/nested/package/structure/ExtraClass";

        map.addExtraClassToClass(keyClass, extraClassName);

        String expectedKey = "com/example/Key.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertTrue(entriesForKey.contains("org/deeply/nested/package/structure/ExtraClass.class"),
                "Should handle deeply nested package structures");
    }

    /**
     * Test addExtraClassToClass(Clazz, String) with inner class names.
     */
    @Test
    public void testAddExtraClassToClass_ClazzAndString_InnerClasses() {
        Clazz keyClass = mock(Clazz.class);
        when(keyClass.getName()).thenReturn("com/example/Key");

        // String with inner class notation
        map.addExtraClassToClass(keyClass, "com/example/Outer$Inner");
        map.addExtraClassToClass(keyClass, "com/example/Outer$Inner$DeepInner");

        String expectedKey = "com/example/Key.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertTrue(entriesForKey.contains("com/example/Outer$Inner.class"),
                "Should handle inner class with $ separator");
        assertTrue(entriesForKey.contains("com/example/Outer$Inner$DeepInner.class"),
                "Should handle nested inner classes");
    }

    /**
     * Test addExtraClassToClass(Clazz, String) with anonymous class names.
     */
    @Test
    public void testAddExtraClassToClass_ClazzAndString_AnonymousClasses() {
        Clazz keyClass = mock(Clazz.class);
        when(keyClass.getName()).thenReturn("com/example/Key");

        // Anonymous class notation
        map.addExtraClassToClass(keyClass, "com/example/Outer$1");
        map.addExtraClassToClass(keyClass, "com/example/Outer$2");

        String expectedKey = "com/example/Key.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertTrue(entriesForKey.contains("com/example/Outer$1.class"),
                "Should handle anonymous class naming");
        assertTrue(entriesForKey.contains("com/example/Outer$2.class"),
                "Should handle multiple anonymous classes");
    }

    /**
     * Test addExtraClassToClass(Clazz, String) can add the same string to multiple keys.
     */
    @Test
    public void testAddExtraClassToClass_ClazzAndString_SameStringToMultipleKeys() {
        Clazz keyClass1 = mock(Clazz.class);
        Clazz keyClass2 = mock(Clazz.class);
        Clazz keyClass3 = mock(Clazz.class);

        when(keyClass1.getName()).thenReturn("com/example/Key1");
        when(keyClass2.getName()).thenReturn("com/example/Key2");
        when(keyClass3.getName()).thenReturn("com/example/Key3");

        String sharedClassName = "com/example/SharedClass";

        map.addExtraClassToClass(keyClass1, sharedClassName);
        map.addExtraClassToClass(keyClass2, sharedClassName);
        map.addExtraClassToClass(keyClass3, sharedClassName);

        // Verify shared class is associated with all three keys
        assertTrue(map.getExtraDataEntryNames("com/example/Key1.class").contains("com/example/SharedClass.class"),
                "Shared class should be associated with Key1");
        assertTrue(map.getExtraDataEntryNames("com/example/Key2.class").contains("com/example/SharedClass.class"),
                "Shared class should be associated with Key2");
        assertTrue(map.getExtraDataEntryNames("com/example/Key3.class").contains("com/example/SharedClass.class"),
                "Shared class should be associated with Key3");
    }

    /**
     * Test addExtraClassToClass(Clazz, String) with different keys and different strings.
     */
    @Test
    public void testAddExtraClassToClass_ClazzAndString_DifferentKeysAndStrings() {
        Clazz keyClass1 = mock(Clazz.class);
        Clazz keyClass2 = mock(Clazz.class);

        when(keyClass1.getName()).thenReturn("com/example/Key1");
        when(keyClass2.getName()).thenReturn("com/example/Key2");

        map.addExtraClassToClass(keyClass1, "com/example/Extra1");
        map.addExtraClassToClass(keyClass2, "com/example/Extra2");

        // Verify correct associations
        Set<String> entriesForKey1 = map.getExtraDataEntryNames("com/example/Key1.class");
        assertTrue(entriesForKey1.contains("com/example/Extra1.class"), "Extra1 should be with Key1");
        assertFalse(entriesForKey1.contains("com/example/Extra2.class"), "Extra2 should not be with Key1");

        Set<String> entriesForKey2 = map.getExtraDataEntryNames("com/example/Key2.class");
        assertTrue(entriesForKey2.contains("com/example/Extra2.class"), "Extra2 should be with Key2");
        assertFalse(entriesForKey2.contains("com/example/Extra1.class"), "Extra1 should not be with Key2");
    }

    /**
     * Test addExtraClassToClass(Clazz, String) with simple class names (no package).
     */
    @Test
    public void testAddExtraClassToClass_ClazzAndString_SimpleNames() {
        Clazz keyClass = mock(Clazz.class);
        when(keyClass.getName()).thenReturn("SimpleKey");

        map.addExtraClassToClass(keyClass, "SimpleExtra");

        String expectedKey = "SimpleKey.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertTrue(entriesForKey.contains("SimpleExtra.class"),
                "Should handle simple names without package");
    }

    /**
     * Test addExtraClassToClass(Clazz, String) with empty string.
     */
    @Test
    public void testAddExtraClassToClass_ClazzAndString_EmptyString() {
        Clazz keyClass = mock(Clazz.class);
        when(keyClass.getName()).thenReturn("com/example/Key");

        map.addExtraClassToClass(keyClass, "");

        String expectedKey = "com/example/Key.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertTrue(entriesForKey.contains(".class"),
                "Should handle empty string (results in just .class)");
    }

    /**
     * Test addExtraClassToClass(Clazz, String) with class names containing underscores.
     */
    @Test
    public void testAddExtraClassToClass_ClazzAndString_Underscores() {
        Clazz keyClass = mock(Clazz.class);
        when(keyClass.getName()).thenReturn("com/example/Key");

        map.addExtraClassToClass(keyClass, "com/example/My_Extra_Class");

        String expectedKey = "com/example/Key.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertTrue(entriesForKey.contains("com/example/My_Extra_Class.class"),
                "Should handle underscores in class names");
    }

    /**
     * Test addExtraClassToClass(Clazz, String) interacts correctly with clear().
     */
    @Test
    public void testAddExtraClassToClass_ClazzAndString_ThenClear() {
        Clazz keyClass = mock(Clazz.class);
        when(keyClass.getName()).thenReturn("com/example/Key");

        map.addExtraClassToClass(keyClass, "com/example/Extra");

        // Verify it was added
        String expectedKey = "com/example/Key.class";
        assertNotNull(map.getExtraDataEntryNames(expectedKey), "Entry should exist before clear");

        // Clear the map
        map.clear();

        // Verify it was removed
        Set<String> entriesAfterClear = map.getExtraDataEntryNames(expectedKey);
        assertTrue(entriesAfterClear == null || entriesAfterClear.isEmpty(),
                "Entry should be removed after clear");
    }

    /**
     * Test addExtraClassToClass(Clazz, String) works after other operations.
     */
    @Test
    public void testAddExtraClassToClass_ClazzAndString_AfterOtherOperations() {
        // Perform other operations first
        map.addExtraDataEntry("default.txt");
        map.addExtraDataEntryToClass("com/example/Class1", "extra1.txt");

        // Add using addExtraClassToClass with String
        Clazz keyClass = mock(Clazz.class);
        when(keyClass.getName()).thenReturn("com/example/Key");

        map.addExtraClassToClass(keyClass, "com/example/Extra");

        // Verify the new mapping exists
        String expectedKey = "com/example/Key.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);
        assertTrue(entriesForKey.contains("com/example/Extra.class"),
                "String class mapping should be added correctly");

        // Verify previous entries still exist
        assertTrue(map.getAllExtraDataEntryNames().contains("default.txt"),
                "Previous default entry should still exist");
        assertTrue(map.getAllExtraDataEntryNames().contains("extra1.txt"),
                "Previous keyed entry should still exist");
    }

    /**
     * Test addExtraClassToClass(Clazz, String) does not throw with valid inputs.
     */
    @Test
    public void testAddExtraClassToClass_ClazzAndString_DoesNotThrow() {
        Clazz keyClass = mock(Clazz.class);
        when(keyClass.getName()).thenReturn("com/example/Key");

        assertDoesNotThrow(() -> map.addExtraClassToClass(keyClass, "com/example/Extra"),
                "Should not throw exception with valid inputs");
    }

    /**
     * Test addExtraClassToClass(Clazz, String) results are retrievable via getAllExtraDataEntryNames.
     */
    @Test
    public void testAddExtraClassToClass_ClazzAndString_RetrievableViaGetAll() {
        Clazz keyClass = mock(Clazz.class);
        when(keyClass.getName()).thenReturn("com/example/Key");

        map.addExtraClassToClass(keyClass, "com/example/Extra1");
        map.addExtraClassToClass(keyClass, "com/example/Extra2");

        Set<String> allEntries = map.getAllExtraDataEntryNames();
        assertTrue(allEntries.contains("com/example/Extra1.class"), "All entries should contain Extra1");
        assertTrue(allEntries.contains("com/example/Extra2.class"), "All entries should contain Extra2");
    }

    /**
     * Test addExtraClassToClass(Clazz, String) can be called multiple times with same pair.
     */
    @Test
    public void testAddExtraClassToClass_ClazzAndString_MultipleTimes() {
        Clazz keyClass = mock(Clazz.class);
        when(keyClass.getName()).thenReturn("com/example/Key");

        map.addExtraClassToClass(keyClass, "com/example/Extra");
        map.addExtraClassToClass(keyClass, "com/example/Extra");
        map.addExtraClassToClass(keyClass, "com/example/Extra");

        String expectedKey = "com/example/Key.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        // Verify the entry exists (behavior depends on MultiValueMap)
        assertTrue(entriesForKey.contains("com/example/Extra.class"),
                "Extra class should be in the map after multiple additions");
    }

    /**
     * Test addExtraClassToClass(Clazz, String) mixing with other method variants.
     */
    @Test
    public void testAddExtraClassToClass_ClazzAndString_MixingWithOtherVariants() {
        Clazz keyClass = mock(Clazz.class);
        Clazz extraProguardClass = mock(Clazz.class);

        when(keyClass.getName()).thenReturn("com/example/Key");
        when(extraProguardClass.getName()).thenReturn("com/example/ProguardExtra");

        // Mix all three method variants
        map.addExtraClassToClass(keyClass, extraProguardClass);         // Clazz, Clazz
        map.addExtraClassToClass(keyClass, String.class);               // Clazz, Class
        map.addExtraClassToClass(keyClass, "com/example/StringExtra"); // Clazz, String

        String expectedKey = "com/example/Key.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertEquals(3, entriesForKey.size(), "Should have 3 entries from different method variants");
        assertTrue(entriesForKey.contains("com/example/ProguardExtra.class"),
                "Should contain entry from Clazz, Clazz variant");
        assertTrue(entriesForKey.contains("java/lang/String.class"),
                "Should contain entry from Clazz, Class variant");
        assertTrue(entriesForKey.contains("com/example/StringExtra.class"),
                "Should contain entry from Clazz, String variant");
    }

    /**
     * Test addExtraClassToClass(Clazz, String) with class names containing numbers.
     */
    @Test
    public void testAddExtraClassToClass_ClazzAndString_NumericNames() {
        Clazz keyClass = mock(Clazz.class);
        when(keyClass.getName()).thenReturn("com/example/Key");

        map.addExtraClassToClass(keyClass, "com/example/Class123");
        map.addExtraClassToClass(keyClass, "com/example/Version2Class");

        String expectedKey = "com/example/Key.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertTrue(entriesForKey.contains("com/example/Class123.class"),
                "Should handle class names with numbers");
        assertTrue(entriesForKey.contains("com/example/Version2Class.class"),
                "Should handle class names with embedded numbers");
    }

    /**
     * Test addExtraClassToClass(Clazz, String) with special characters in names.
     */
    @Test
    public void testAddExtraClassToClass_ClazzAndString_SpecialCharacters() {
        Clazz keyClass = mock(Clazz.class);
        when(keyClass.getName()).thenReturn("com/example/Key");

        // Test with various special characters that might appear in class names
        map.addExtraClassToClass(keyClass, "com/example/Class$Inner");
        map.addExtraClassToClass(keyClass, "com/example/My_Class");
        map.addExtraClassToClass(keyClass, "com/example/Class-Hyphen");

        String expectedKey = "com/example/Key.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertTrue(entriesForKey.contains("com/example/Class$Inner.class"),
                "Should handle $ in class names");
        assertTrue(entriesForKey.contains("com/example/My_Class.class"),
                "Should handle underscores");
        assertTrue(entriesForKey.contains("com/example/Class-Hyphen.class"),
                "Should handle hyphens");
    }

    /**
     * Test addExtraClassToClass(Clazz, String) with very long class names.
     */
    @Test
    public void testAddExtraClassToClass_ClazzAndString_LongNames() {
        Clazz keyClass = mock(Clazz.class);
        when(keyClass.getName()).thenReturn("com/example/Key");

        String longClassName = "com/example/very/long/package/path/with/many/levels/VeryLongClassNameWithManyCharacters";
        map.addExtraClassToClass(keyClass, longClassName);

        String expectedKey = "com/example/Key.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertTrue(entriesForKey.contains(longClassName + ".class"),
                "Should handle very long class names");
    }

    /**
     * Test that addExtraClassToClass(Clazz, String) delegation works correctly.
     */
    @Test
    public void testAddExtraClassToClass_ClazzAndString_Delegation() {
        Clazz keyClass = mock(Clazz.class);
        when(keyClass.getName()).thenReturn("com/example/Key");

        map.addExtraClassToClass(keyClass, "com/example/Extra");

        // This should be equivalent to:
        // map.addExtraDataEntry("com/example/Key.class", "com/example/Extra.class");
        ExtraDataEntryNameMap map2 = new ExtraDataEntryNameMap();
        map2.addExtraDataEntry("com/example/Key.class", "com/example/Extra.class");

        // Both maps should have the same result
        assertEquals(map.getKeyDataEntryNames(), map2.getKeyDataEntryNames(),
                "Key names should match");
        assertEquals(map.getAllExtraDataEntryNames(), map2.getAllExtraDataEntryNames(),
                "All entry names should match");
    }

    /**
     * Test that the public getClassDataEntryName method works correctly with String parameter.
     */
    @Test
    public void testAddExtraClassToClass_ClazzAndString_GetClassDataEntryNameConsistency() {
        Clazz keyClass = mock(Clazz.class);
        when(keyClass.getName()).thenReturn("com/example/Key");

        String extraClassName = "com/example/Extra";
        map.addExtraClassToClass(keyClass, extraClassName);

        // The entry should match what getClassDataEntryName returns
        String expectedValue = map.getClassDataEntryName(extraClassName);

        String expectedKey = "com/example/Key.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertTrue(entriesForKey.contains(expectedValue),
                "Entry should match getClassDataEntryName output");
    }

    // ========== Tests for addExtraClassToClass(String, Class) ==========

    /**
     * Test that addExtraClassToClass(String, Class) adds a mapping with String and Java Class.
     * This test uses NO mocking - both parameters are simple types.
     */
    @Test
    public void testAddExtraClassToClass_StringAndJavaClass_Simple() {
        // Use real String and real Java Class - no mocking needed!
        String keyClassName = "com/example/KeyClass";
        Class<?> extraClass = String.class;

        map.addExtraClassToClass(keyClassName, extraClass);

        // Verify the mapping was created
        String expectedKey = "com/example/KeyClass.class";
        String expectedValue = "java/lang/String.class";

        Set<String> keyNames = map.getKeyDataEntryNames();
        assertTrue(keyNames.contains(expectedKey), "Key should be in the map");

        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);
        assertNotNull(entriesForKey, "Entries for key should not be null");
        assertTrue(entriesForKey.contains(expectedValue),
                "Java Class should be converted to internal format and associated with key");
    }

    /**
     * Test addExtraClassToClass(String, Class) with multiple Java classes.
     */
    @Test
    public void testAddExtraClassToClass_StringAndJavaClass_MultipleClasses() {
        String keyClassName = "com/example/Key";

        map.addExtraClassToClass(keyClassName, String.class);
        map.addExtraClassToClass(keyClassName, Integer.class);
        map.addExtraClassToClass(keyClassName, Object.class);

        String expectedKey = "com/example/Key.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertEquals(3, entriesForKey.size(), "Should have 3 Java classes mapped");
        assertTrue(entriesForKey.contains("java/lang/String.class"), "Should contain String");
        assertTrue(entriesForKey.contains("java/lang/Integer.class"), "Should contain Integer");
        assertTrue(entriesForKey.contains("java/lang/Object.class"), "Should contain Object");
    }

    /**
     * Test addExtraClassToClass(String, Class) with nested package names.
     */
    @Test
    public void testAddExtraClassToClass_StringAndJavaClass_NestedPackages() {
        String keyClassName = "org/deeply/nested/package/KeyClass";

        map.addExtraClassToClass(keyClassName, java.util.concurrent.ConcurrentHashMap.class);

        String expectedKey = "org/deeply/nested/package/KeyClass.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertTrue(entriesForKey.contains("java/util/concurrent/ConcurrentHashMap.class"),
                "Should handle deeply nested packages in both String key and Java Class");
    }

    /**
     * Test addExtraClassToClass(String, Class) with inner classes.
     */
    @Test
    public void testAddExtraClassToClass_StringAndJavaClass_InnerClasses() {
        String keyClassName = "com/example/Outer$Inner";

        map.addExtraClassToClass(keyClassName, java.util.Map.Entry.class);

        String expectedKey = "com/example/Outer$Inner.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertTrue(entriesForKey.contains("java/util/Map$Entry.class"),
                "Should handle inner classes in both parameters");
    }

    /**
     * Test addExtraClassToClass(String, Class) can add the same Java class to multiple string keys.
     */
    @Test
    public void testAddExtraClassToClass_StringAndJavaClass_SameClassToMultipleKeys() {
        map.addExtraClassToClass("com/example/Key1", Object.class);
        map.addExtraClassToClass("com/example/Key2", Object.class);
        map.addExtraClassToClass("com/example/Key3", Object.class);

        // Verify Object.class is associated with all three keys
        assertTrue(map.getExtraDataEntryNames("com/example/Key1.class").contains("java/lang/Object.class"),
                "Object should be associated with Key1");
        assertTrue(map.getExtraDataEntryNames("com/example/Key2.class").contains("java/lang/Object.class"),
                "Object should be associated with Key2");
        assertTrue(map.getExtraDataEntryNames("com/example/Key3.class").contains("java/lang/Object.class"),
                "Object should be associated with Key3");
    }

    /**
     * Test addExtraClassToClass(String, Class) with different keys and different classes.
     */
    @Test
    public void testAddExtraClassToClass_StringAndJavaClass_DifferentKeysAndClasses() {
        map.addExtraClassToClass("com/example/Key1", String.class);
        map.addExtraClassToClass("com/example/Key2", Integer.class);

        // Verify correct associations
        Set<String> entriesForKey1 = map.getExtraDataEntryNames("com/example/Key1.class");
        assertTrue(entriesForKey1.contains("java/lang/String.class"), "String should be with Key1");
        assertFalse(entriesForKey1.contains("java/lang/Integer.class"), "Integer should not be with Key1");

        Set<String> entriesForKey2 = map.getExtraDataEntryNames("com/example/Key2.class");
        assertTrue(entriesForKey2.contains("java/lang/Integer.class"), "Integer should be with Key2");
        assertFalse(entriesForKey2.contains("java/lang/String.class"), "String should not be with Key2");
    }

    /**
     * Test addExtraClassToClass(String, Class) with simple class names (no package).
     */
    @Test
    public void testAddExtraClassToClass_StringAndJavaClass_SimpleNames() {
        map.addExtraClassToClass("SimpleKey", String.class);

        String expectedKey = "SimpleKey.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertTrue(entriesForKey.contains("java/lang/String.class"),
                "Should handle simple names without package");
    }

    /**
     * Test addExtraClassToClass(String, Class) with empty string key.
     */
    @Test
    public void testAddExtraClassToClass_StringAndJavaClass_EmptyStringKey() {
        map.addExtraClassToClass("", String.class);

        String expectedKey = ".class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertTrue(entriesForKey.contains("java/lang/String.class"),
                "Should handle empty string key");
    }

    /**
     * Test addExtraClassToClass(String, Class) with primitive wrapper classes.
     */
    @Test
    public void testAddExtraClassToClass_StringAndJavaClass_PrimitiveWrappers() {
        String keyClassName = "com/example/Key";

        map.addExtraClassToClass(keyClassName, Boolean.class);
        map.addExtraClassToClass(keyClassName, Character.class);
        map.addExtraClassToClass(keyClassName, Byte.class);
        map.addExtraClassToClass(keyClassName, Short.class);
        map.addExtraClassToClass(keyClassName, Long.class);
        map.addExtraClassToClass(keyClassName, Float.class);
        map.addExtraClassToClass(keyClassName, Double.class);

        String expectedKey = "com/example/Key.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertEquals(7, entriesForKey.size(), "Should have 7 primitive wrapper classes");
        assertTrue(entriesForKey.contains("java/lang/Boolean.class"), "Should contain Boolean");
        assertTrue(entriesForKey.contains("java/lang/Character.class"), "Should contain Character");
        assertTrue(entriesForKey.contains("java/lang/Byte.class"), "Should contain Byte");
        assertTrue(entriesForKey.contains("java/lang/Short.class"), "Should contain Short");
        assertTrue(entriesForKey.contains("java/lang/Long.class"), "Should contain Long");
        assertTrue(entriesForKey.contains("java/lang/Float.class"), "Should contain Float");
        assertTrue(entriesForKey.contains("java/lang/Double.class"), "Should contain Double");
    }

    /**
     * Test addExtraClassToClass(String, Class) with array classes.
     */
    @Test
    public void testAddExtraClassToClass_StringAndJavaClass_ArrayClasses() {
        String keyClassName = "com/example/Key";

        map.addExtraClassToClass(keyClassName, String[].class);
        map.addExtraClassToClass(keyClassName, int[].class);
        map.addExtraClassToClass(keyClassName, Object[][].class);

        String expectedKey = "com/example/Key.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertTrue(entriesForKey.contains("[Ljava/lang/String;.class"),
                "String array should be converted to internal format");
        assertTrue(entriesForKey.contains("[I.class"),
                "int array should be converted to internal format");
        assertTrue(entriesForKey.contains("[[Ljava/lang/Object;.class"),
                "2D Object array should be converted to internal format");
    }

    /**
     * Test addExtraClassToClass(String, Class) with interface classes.
     */
    @Test
    public void testAddExtraClassToClass_StringAndJavaClass_Interfaces() {
        String keyClassName = "com/example/Key";

        map.addExtraClassToClass(keyClassName, java.util.List.class);
        map.addExtraClassToClass(keyClassName, java.io.Serializable.class);
        map.addExtraClassToClass(keyClassName, Runnable.class);

        String expectedKey = "com/example/Key.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertTrue(entriesForKey.contains("java/util/List.class"), "Should contain List interface");
        assertTrue(entriesForKey.contains("java/io/Serializable.class"), "Should contain Serializable interface");
        assertTrue(entriesForKey.contains("java/lang/Runnable.class"), "Should contain Runnable interface");
    }

    /**
     * Test addExtraClassToClass(String, Class) interacts correctly with clear().
     */
    @Test
    public void testAddExtraClassToClass_StringAndJavaClass_ThenClear() {
        map.addExtraClassToClass("com/example/Key", String.class);

        // Verify it was added
        String expectedKey = "com/example/Key.class";
        assertNotNull(map.getExtraDataEntryNames(expectedKey), "Entry should exist before clear");

        // Clear the map
        map.clear();

        // Verify it was removed
        Set<String> entriesAfterClear = map.getExtraDataEntryNames(expectedKey);
        assertTrue(entriesAfterClear == null || entriesAfterClear.isEmpty(),
                "Entry should be removed after clear");
    }

    /**
     * Test addExtraClassToClass(String, Class) works after other operations.
     */
    @Test
    public void testAddExtraClassToClass_StringAndJavaClass_AfterOtherOperations() {
        // Perform other operations first
        map.addExtraDataEntry("default.txt");
        map.addExtraDataEntryToClass("com/example/Class1", "extra1.txt");

        // Add using addExtraClassToClass with String and Class
        map.addExtraClassToClass("com/example/Key", String.class);

        // Verify the new mapping exists
        String expectedKey = "com/example/Key.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);
        assertTrue(entriesForKey.contains("java/lang/String.class"),
                "String/Class mapping should be added correctly");

        // Verify previous entries still exist
        assertTrue(map.getAllExtraDataEntryNames().contains("default.txt"),
                "Previous default entry should still exist");
        assertTrue(map.getAllExtraDataEntryNames().contains("extra1.txt"),
                "Previous keyed entry should still exist");
    }

    /**
     * Test addExtraClassToClass(String, Class) does not throw with valid inputs.
     */
    @Test
    public void testAddExtraClassToClass_StringAndJavaClass_DoesNotThrow() {
        assertDoesNotThrow(() -> map.addExtraClassToClass("com/example/Key", String.class),
                "Should not throw exception with valid inputs");
    }

    /**
     * Test addExtraClassToClass(String, Class) results are retrievable via getAllExtraDataEntryNames.
     */
    @Test
    public void testAddExtraClassToClass_StringAndJavaClass_RetrievableViaGetAll() {
        map.addExtraClassToClass("com/example/Key", String.class);
        map.addExtraClassToClass("com/example/Key", Integer.class);

        Set<String> allEntries = map.getAllExtraDataEntryNames();
        assertTrue(allEntries.contains("java/lang/String.class"), "All entries should contain String");
        assertTrue(allEntries.contains("java/lang/Integer.class"), "All entries should contain Integer");
    }

    /**
     * Test addExtraClassToClass(String, Class) can be called multiple times with same pair.
     */
    @Test
    public void testAddExtraClassToClass_StringAndJavaClass_MultipleTimes() {
        map.addExtraClassToClass("com/example/Key", String.class);
        map.addExtraClassToClass("com/example/Key", String.class);
        map.addExtraClassToClass("com/example/Key", String.class);

        String expectedKey = "com/example/Key.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        // Verify the entry exists (behavior depends on MultiValueMap)
        assertTrue(entriesForKey.contains("java/lang/String.class"),
                "String class should be in the map after multiple additions");
    }

    /**
     * Test addExtraClassToClass(String, Class) mixing with other method variants.
     */
    @Test
    public void testAddExtraClassToClass_StringAndJavaClass_MixingWithOtherVariants() {
        Clazz proguardKeyClass = mock(Clazz.class);
        when(proguardKeyClass.getName()).thenReturn("com/example/Key");

        // Mix all four method variants on the same key
        map.addExtraClassToClass(proguardKeyClass, mock(Clazz.class));  // Clazz, Clazz
        when(mock(Clazz.class).getName()).thenReturn("com/example/Extra1");

        // Simpler approach - use different keys for clarity
        map.addExtraClassToClass("com/example/Key", Integer.class);       // String, Class
        map.addExtraClassToClass("com/example/Key", "com/example/Extra2"); // String would need Clazz

        // Actually, let's just test String, Class variant with other variants
        String keyClassName = "com/example/TestKey";

        map.addExtraClassToClass(keyClassName, String.class);            // String, Class
        map.addExtraClassToClass(keyClassName, "com/example/Extra");     // String, String (if exists)

        String expectedKey = "com/example/TestKey.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertTrue(entriesForKey.contains("java/lang/String.class"),
                "Should contain entry from String, Class variant");
        assertTrue(entriesForKey.contains("com/example/Extra.class"),
                "Should contain entry from String, String variant");
    }

    /**
     * Test addExtraClassToClass(String, Class) with exception classes.
     */
    @Test
    public void testAddExtraClassToClass_StringAndJavaClass_ExceptionClasses() {
        String keyClassName = "com/example/Key";

        map.addExtraClassToClass(keyClassName, Exception.class);
        map.addExtraClassToClass(keyClassName, RuntimeException.class);
        map.addExtraClassToClass(keyClassName, java.io.IOException.class);

        String expectedKey = "com/example/Key.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertTrue(entriesForKey.contains("java/lang/Exception.class"), "Should contain Exception");
        assertTrue(entriesForKey.contains("java/lang/RuntimeException.class"), "Should contain RuntimeException");
        assertTrue(entriesForKey.contains("java/io/IOException.class"), "Should contain IOException");
    }

    /**
     * Test addExtraClassToClass(String, Class) with classes from various packages.
     */
    @Test
    public void testAddExtraClassToClass_StringAndJavaClass_VariousPackages() {
        String keyClassName = "com/example/Key";

        map.addExtraClassToClass(keyClassName, java.util.ArrayList.class);
        map.addExtraClassToClass(keyClassName, java.io.File.class);
        map.addExtraClassToClass(keyClassName, java.net.URL.class);
        map.addExtraClassToClass(keyClassName, java.math.BigDecimal.class);

        String expectedKey = "com/example/Key.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertTrue(entriesForKey.contains("java/util/ArrayList.class"), "Should contain ArrayList");
        assertTrue(entriesForKey.contains("java/io/File.class"), "Should contain File");
        assertTrue(entriesForKey.contains("java/net/URL.class"), "Should contain URL");
        assertTrue(entriesForKey.contains("java/math/BigDecimal.class"), "Should contain BigDecimal");
    }

    /**
     * Test addExtraClassToClass(String, Class) with key containing special characters.
     */
    @Test
    public void testAddExtraClassToClass_StringAndJavaClass_SpecialCharactersInKey() {
        map.addExtraClassToClass("com/example/Outer$Inner", String.class);
        map.addExtraClassToClass("com/example/My_Class", Integer.class);
        map.addExtraClassToClass("com/example/Class123", Object.class);

        assertTrue(map.getExtraDataEntryNames("com/example/Outer$Inner.class").contains("java/lang/String.class"),
                "Should handle $ in key");
        assertTrue(map.getExtraDataEntryNames("com/example/My_Class.class").contains("java/lang/Integer.class"),
                "Should handle underscores in key");
        assertTrue(map.getExtraDataEntryNames("com/example/Class123.class").contains("java/lang/Object.class"),
                "Should handle numbers in key");
    }

    /**
     * Test that addExtraClassToClass(String, Class) delegation works correctly.
     */
    @Test
    public void testAddExtraClassToClass_StringAndJavaClass_Delegation() {
        map.addExtraClassToClass("com/example/Key", String.class);

        // This should be equivalent to:
        // map.addExtraDataEntry("com/example/Key.class", "java/lang/String.class");
        ExtraDataEntryNameMap map2 = new ExtraDataEntryNameMap();
        map2.addExtraDataEntry("com/example/Key.class", "java/lang/String.class");

        // Both maps should have the same result
        assertEquals(map.getKeyDataEntryNames(), map2.getKeyDataEntryNames(),
                "Key names should match");
        assertEquals(map.getAllExtraDataEntryNames(), map2.getAllExtraDataEntryNames(),
                "All entry names should match");
    }

    /**
     * Test addExtraClassToClass(String, Class) with very long key names.
     */
    @Test
    public void testAddExtraClassToClass_StringAndJavaClass_LongKeyName() {
        String longKeyName = "com/example/very/long/package/path/with/many/levels/VeryLongKeyClassName";
        map.addExtraClassToClass(longKeyName, String.class);

        String expectedKey = longKeyName + ".class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertTrue(entriesForKey.contains("java/lang/String.class"),
                "Should handle very long key names");
    }

    /**
     * Test that both getClassDataEntryName methods work correctly with this variant.
     */
    @Test
    public void testAddExtraClassToClass_StringAndJavaClass_GetClassDataEntryNameConsistency() {
        String keyClassName = "com/example/Key";
        Class<?> extraClass = String.class;

        map.addExtraClassToClass(keyClassName, extraClass);

        // Both transformations should be consistent with getClassDataEntryName
        String expectedKey = map.getClassDataEntryName(keyClassName);
        String expectedValue = "java/lang/String.class"; // Can't directly call getClassDataEntryName(Class)

        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertTrue(entriesForKey.contains(expectedValue),
                "Entry should be consistent with getClassDataEntryName transformations");
        assertEquals("com/example/Key.class", expectedKey,
                "Key should be transformed correctly");
    }

    // ========== Tests for addExtraClassToClass(String, String) ==========

    /**
     * Test that addExtraClassToClass(String, String) adds a mapping with two String class names.
     * This test uses NO mocking - both parameters are simple Strings.
     * This is the simplest variant to test!
     */
    @Test
    public void testAddExtraClassToClass_StringAndString_Simple() {
        // Use real Strings - no mocking needed at all!
        String keyClassName = "com/example/KeyClass";
        String extraClassName = "com/example/ExtraClass";

        map.addExtraClassToClass(keyClassName, extraClassName);

        // Verify the mapping was created
        String expectedKey = "com/example/KeyClass.class";
        String expectedValue = "com/example/ExtraClass.class";

        Set<String> keyNames = map.getKeyDataEntryNames();
        assertTrue(keyNames.contains(expectedKey), "Key should be in the map");

        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);
        assertNotNull(entriesForKey, "Entries for key should not be null");
        assertTrue(entriesForKey.contains(expectedValue),
                "Extra class name should be appended with .class and associated with key");
    }

    /**
     * Test addExtraClassToClass(String, String) with multiple extra class names.
     */
    @Test
    public void testAddExtraClassToClass_StringAndString_MultipleClasses() {
        String keyClassName = "com/example/Key";

        map.addExtraClassToClass(keyClassName, "com/example/Extra1");
        map.addExtraClassToClass(keyClassName, "com/example/Extra2");
        map.addExtraClassToClass(keyClassName, "com/example/Extra3");

        String expectedKey = "com/example/Key.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertEquals(3, entriesForKey.size(), "Should have 3 extra classes mapped");
        assertTrue(entriesForKey.contains("com/example/Extra1.class"), "Should contain Extra1");
        assertTrue(entriesForKey.contains("com/example/Extra2.class"), "Should contain Extra2");
        assertTrue(entriesForKey.contains("com/example/Extra3.class"), "Should contain Extra3");
    }

    /**
     * Test addExtraClassToClass(String, String) with nested package names.
     */
    @Test
    public void testAddExtraClassToClass_StringAndString_NestedPackages() {
        String keyClassName = "org/deeply/nested/package/KeyClass";
        String extraClassName = "com/another/deeply/nested/package/ExtraClass";

        map.addExtraClassToClass(keyClassName, extraClassName);

        String expectedKey = "org/deeply/nested/package/KeyClass.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertTrue(entriesForKey.contains("com/another/deeply/nested/package/ExtraClass.class"),
                "Should handle deeply nested packages in both parameters");
    }

    /**
     * Test addExtraClassToClass(String, String) with inner class names.
     */
    @Test
    public void testAddExtraClassToClass_StringAndString_InnerClasses() {
        String keyClassName = "com/example/Outer$Inner";
        String extraClassName = "com/example/Another$Inner$DeepInner";

        map.addExtraClassToClass(keyClassName, extraClassName);

        String expectedKey = "com/example/Outer$Inner.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertTrue(entriesForKey.contains("com/example/Another$Inner$DeepInner.class"),
                "Should handle inner classes in both parameters");
    }

    /**
     * Test addExtraClassToClass(String, String) with anonymous class names.
     */
    @Test
    public void testAddExtraClassToClass_StringAndString_AnonymousClasses() {
        map.addExtraClassToClass("com/example/Outer$1", "com/example/Extra$1");
        map.addExtraClassToClass("com/example/Outer$2", "com/example/Extra$2");

        assertTrue(map.getExtraDataEntryNames("com/example/Outer$1.class").contains("com/example/Extra$1.class"),
                "Should handle anonymous class names");
        assertTrue(map.getExtraDataEntryNames("com/example/Outer$2.class").contains("com/example/Extra$2.class"),
                "Should handle multiple anonymous classes");
    }

    /**
     * Test addExtraClassToClass(String, String) can add the same extra class to multiple keys.
     */
    @Test
    public void testAddExtraClassToClass_StringAndString_SameExtraToMultipleKeys() {
        String sharedExtra = "com/example/SharedExtra";

        map.addExtraClassToClass("com/example/Key1", sharedExtra);
        map.addExtraClassToClass("com/example/Key2", sharedExtra);
        map.addExtraClassToClass("com/example/Key3", sharedExtra);

        // Verify shared extra is associated with all three keys
        assertTrue(map.getExtraDataEntryNames("com/example/Key1.class").contains("com/example/SharedExtra.class"),
                "Shared extra should be associated with Key1");
        assertTrue(map.getExtraDataEntryNames("com/example/Key2.class").contains("com/example/SharedExtra.class"),
                "Shared extra should be associated with Key2");
        assertTrue(map.getExtraDataEntryNames("com/example/Key3.class").contains("com/example/SharedExtra.class"),
                "Shared extra should be associated with Key3");
    }

    /**
     * Test addExtraClassToClass(String, String) with different keys and different extras.
     */
    @Test
    public void testAddExtraClassToClass_StringAndString_DifferentKeysAndExtras() {
        map.addExtraClassToClass("com/example/Key1", "com/example/Extra1");
        map.addExtraClassToClass("com/example/Key2", "com/example/Extra2");

        // Verify correct associations
        Set<String> entriesForKey1 = map.getExtraDataEntryNames("com/example/Key1.class");
        assertTrue(entriesForKey1.contains("com/example/Extra1.class"), "Extra1 should be with Key1");
        assertFalse(entriesForKey1.contains("com/example/Extra2.class"), "Extra2 should not be with Key1");

        Set<String> entriesForKey2 = map.getExtraDataEntryNames("com/example/Key2.class");
        assertTrue(entriesForKey2.contains("com/example/Extra2.class"), "Extra2 should be with Key2");
        assertFalse(entriesForKey2.contains("com/example/Extra1.class"), "Extra1 should not be with Key2");
    }

    /**
     * Test addExtraClassToClass(String, String) with simple class names (no package).
     */
    @Test
    public void testAddExtraClassToClass_StringAndString_SimpleNames() {
        map.addExtraClassToClass("SimpleKey", "SimpleExtra");

        String expectedKey = "SimpleKey.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertTrue(entriesForKey.contains("SimpleExtra.class"),
                "Should handle simple names without package");
    }

    /**
     * Test addExtraClassToClass(String, String) with empty strings.
     */
    @Test
    public void testAddExtraClassToClass_StringAndString_EmptyStrings() {
        map.addExtraClassToClass("", "");
        map.addExtraClassToClass("com/example/Key", "");
        map.addExtraClassToClass("", "com/example/Extra");

        assertTrue(map.getExtraDataEntryNames(".class").contains(".class"),
                "Should handle both empty strings");
        assertTrue(map.getExtraDataEntryNames("com/example/Key.class").contains(".class"),
                "Should handle empty extra class name");
        assertTrue(map.getExtraDataEntryNames(".class").contains("com/example/Extra.class"),
                "Should handle empty key class name");
    }

    /**
     * Test addExtraClassToClass(String, String) with class names containing underscores.
     */
    @Test
    public void testAddExtraClassToClass_StringAndString_Underscores() {
        map.addExtraClassToClass("com/example/My_Key_Class", "com/example/My_Extra_Class");

        String expectedKey = "com/example/My_Key_Class.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertTrue(entriesForKey.contains("com/example/My_Extra_Class.class"),
                "Should handle underscores in both class names");
    }

    /**
     * Test addExtraClassToClass(String, String) with class names containing numbers.
     */
    @Test
    public void testAddExtraClassToClass_StringAndString_NumericNames() {
        map.addExtraClassToClass("com/example/Key123", "com/example/Extra456");
        map.addExtraClassToClass("com/example/Version2Key", "com/example/Version3Extra");

        assertTrue(map.getExtraDataEntryNames("com/example/Key123.class").contains("com/example/Extra456.class"),
                "Should handle numbers in class names");
        assertTrue(map.getExtraDataEntryNames("com/example/Version2Key.class").contains("com/example/Version3Extra.class"),
                "Should handle embedded numbers");
    }

    /**
     * Test addExtraClassToClass(String, String) with special characters.
     */
    @Test
    public void testAddExtraClassToClass_StringAndString_SpecialCharacters() {
        map.addExtraClassToClass("com/example/Key$Inner", "com/example/Extra$Inner");
        map.addExtraClassToClass("com/example/My_Key", "com/example/My_Extra");
        map.addExtraClassToClass("com/example/Key-Hyphen", "com/example/Extra-Hyphen");

        assertTrue(map.getExtraDataEntryNames("com/example/Key$Inner.class").contains("com/example/Extra$Inner.class"),
                "Should handle $ in both names");
        assertTrue(map.getExtraDataEntryNames("com/example/My_Key.class").contains("com/example/My_Extra.class"),
                "Should handle underscores");
        assertTrue(map.getExtraDataEntryNames("com/example/Key-Hyphen.class").contains("com/example/Extra-Hyphen.class"),
                "Should handle hyphens");
    }

    /**
     * Test addExtraClassToClass(String, String) interacts correctly with clear().
     */
    @Test
    public void testAddExtraClassToClass_StringAndString_ThenClear() {
        map.addExtraClassToClass("com/example/Key", "com/example/Extra");

        // Verify it was added
        String expectedKey = "com/example/Key.class";
        assertNotNull(map.getExtraDataEntryNames(expectedKey), "Entry should exist before clear");

        // Clear the map
        map.clear();

        // Verify it was removed
        Set<String> entriesAfterClear = map.getExtraDataEntryNames(expectedKey);
        assertTrue(entriesAfterClear == null || entriesAfterClear.isEmpty(),
                "Entry should be removed after clear");
    }

    /**
     * Test addExtraClassToClass(String, String) works after other operations.
     */
    @Test
    public void testAddExtraClassToClass_StringAndString_AfterOtherOperations() {
        // Perform other operations first
        map.addExtraDataEntry("default.txt");
        map.addExtraDataEntryToClass("com/example/Class1", "extra1.txt");

        // Add using addExtraClassToClass with two Strings
        map.addExtraClassToClass("com/example/Key", "com/example/Extra");

        // Verify the new mapping exists
        String expectedKey = "com/example/Key.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);
        assertTrue(entriesForKey.contains("com/example/Extra.class"),
                "String/String mapping should be added correctly");

        // Verify previous entries still exist
        assertTrue(map.getAllExtraDataEntryNames().contains("default.txt"),
                "Previous default entry should still exist");
        assertTrue(map.getAllExtraDataEntryNames().contains("extra1.txt"),
                "Previous keyed entry should still exist");
    }

    /**
     * Test addExtraClassToClass(String, String) does not throw with valid inputs.
     */
    @Test
    public void testAddExtraClassToClass_StringAndString_DoesNotThrow() {
        assertDoesNotThrow(() -> map.addExtraClassToClass("com/example/Key", "com/example/Extra"),
                "Should not throw exception with valid inputs");
    }

    /**
     * Test addExtraClassToClass(String, String) results are retrievable via getAllExtraDataEntryNames.
     */
    @Test
    public void testAddExtraClassToClass_StringAndString_RetrievableViaGetAll() {
        map.addExtraClassToClass("com/example/Key", "com/example/Extra1");
        map.addExtraClassToClass("com/example/Key", "com/example/Extra2");

        Set<String> allEntries = map.getAllExtraDataEntryNames();
        assertTrue(allEntries.contains("com/example/Extra1.class"), "All entries should contain Extra1");
        assertTrue(allEntries.contains("com/example/Extra2.class"), "All entries should contain Extra2");
    }

    /**
     * Test addExtraClassToClass(String, String) can be called multiple times with same pair.
     */
    @Test
    public void testAddExtraClassToClass_StringAndString_MultipleTimes() {
        map.addExtraClassToClass("com/example/Key", "com/example/Extra");
        map.addExtraClassToClass("com/example/Key", "com/example/Extra");
        map.addExtraClassToClass("com/example/Key", "com/example/Extra");

        String expectedKey = "com/example/Key.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        // Verify the entry exists (behavior depends on MultiValueMap)
        assertTrue(entriesForKey.contains("com/example/Extra.class"),
                "Extra class should be in the map after multiple additions");
    }

    /**
     * Test addExtraClassToClass(String, String) mixing with all other method variants.
     */
    @Test
    public void testAddExtraClassToClass_StringAndString_MixingWithAllVariants() {
        String keyClassName = "com/example/TestKey";

        // Use all five method variants on the same key
        Clazz mockClazz = mock(Clazz.class);
        when(mockClazz.getName()).thenReturn(keyClassName);

        Clazz mockExtraClazz = mock(Clazz.class);
        when(mockExtraClazz.getName()).thenReturn("com/example/ExtraClazz");

        map.addExtraClassToClass(mockClazz, mockExtraClazz);          // Clazz, Clazz
        map.addExtraClassToClass(mockClazz, String.class);            // Clazz, Class
        map.addExtraClassToClass(mockClazz, "com/example/Extra3");    // Clazz, String
        map.addExtraClassToClass(keyClassName, Integer.class);        // String, Class
        map.addExtraClassToClass(keyClassName, "com/example/Extra5"); // String, String

        String expectedKey = keyClassName + ".class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertEquals(5, entriesForKey.size(), "Should have 5 entries from all five method variants");
        assertTrue(entriesForKey.contains("com/example/ExtraClazz.class"), "Should contain from Clazz, Clazz");
        assertTrue(entriesForKey.contains("java/lang/String.class"), "Should contain from Clazz, Class");
        assertTrue(entriesForKey.contains("com/example/Extra3.class"), "Should contain from Clazz, String");
        assertTrue(entriesForKey.contains("java/lang/Integer.class"), "Should contain from String, Class");
        assertTrue(entriesForKey.contains("com/example/Extra5.class"), "Should contain from String, String");
    }

    /**
     * Test addExtraClassToClass(String, String) with very long class names.
     */
    @Test
    public void testAddExtraClassToClass_StringAndString_LongNames() {
        String longKey = "com/example/very/long/package/path/with/many/levels/VeryLongKeyClassName";
        String longExtra = "org/another/very/long/package/path/with/many/levels/VeryLongExtraClassName";

        map.addExtraClassToClass(longKey, longExtra);

        String expectedKey = longKey + ".class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertTrue(entriesForKey.contains(longExtra + ".class"),
                "Should handle very long class names in both parameters");
    }

    /**
     * Test that addExtraClassToClass(String, String) delegation works correctly.
     */
    @Test
    public void testAddExtraClassToClass_StringAndString_Delegation() {
        map.addExtraClassToClass("com/example/Key", "com/example/Extra");

        // This should be equivalent to:
        // map.addExtraDataEntry("com/example/Key.class", "com/example/Extra.class");
        ExtraDataEntryNameMap map2 = new ExtraDataEntryNameMap();
        map2.addExtraDataEntry("com/example/Key.class", "com/example/Extra.class");

        // Both maps should have the same result
        assertEquals(map.getKeyDataEntryNames(), map2.getKeyDataEntryNames(),
                "Key names should match");
        assertEquals(map.getAllExtraDataEntryNames(), map2.getAllExtraDataEntryNames(),
                "All entry names should match");
    }

    /**
     * Test that getClassDataEntryName consistency with both parameters.
     */
    @Test
    public void testAddExtraClassToClass_StringAndString_GetClassDataEntryNameConsistency() {
        String keyClassName = "com/example/Key";
        String extraClassName = "com/example/Extra";

        map.addExtraClassToClass(keyClassName, extraClassName);

        // Both transformations should be consistent with getClassDataEntryName
        String expectedKey = map.getClassDataEntryName(keyClassName);
        String expectedValue = map.getClassDataEntryName(extraClassName);

        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertTrue(entriesForKey.contains(expectedValue),
                "Entry should be consistent with getClassDataEntryName transformations");
        assertEquals("com/example/Key.class", expectedKey, "Key should be transformed correctly");
        assertEquals("com/example/Extra.class", expectedValue, "Value should be transformed correctly");
    }

    /**
     * Test addExtraClassToClass(String, String) with identical key and extra names.
     */
    @Test
    public void testAddExtraClassToClass_StringAndString_SameKeyAndExtra() {
        String className = "com/example/SameClass";

        map.addExtraClassToClass(className, className);

        String expectedKey = className + ".class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertTrue(entriesForKey.contains(expectedKey),
                "A class should be able to map to itself");
    }

    /**
     * Test addExtraClassToClass(String, String) with whitespace in names.
     */
    @Test
    public void testAddExtraClassToClass_StringAndString_WhitespaceHandling() {
        // Although unusual, test that the method handles strings with spaces
        map.addExtraClassToClass("com/example/Key With Space", "com/example/Extra With Space");

        String expectedKey = "com/example/Key With Space.class";
        Set<String> entriesForKey = map.getExtraDataEntryNames(expectedKey);

        assertTrue(entriesForKey.contains("com/example/Extra With Space.class"),
                "Should handle whitespace in class names (though unusual)");
    }

    /**
     * Test addExtraClassToClass(String, String) creates keys retrievable via getKeyDataEntryNames.
     */
    @Test
    public void testAddExtraClassToClass_StringAndString_KeyRetrievable() {
        map.addExtraClassToClass("com/example/Key1", "com/example/Extra1");
        map.addExtraClassToClass("com/example/Key2", "com/example/Extra2");

        Set<String> keyNames = map.getKeyDataEntryNames();
        assertTrue(keyNames.contains("com/example/Key1.class"), "Key1 should be retrievable");
        assertTrue(keyNames.contains("com/example/Key2.class"), "Key2 should be retrievable");
        assertEquals(2, keyNames.size(), "Should have exactly 2 keys");
    }
}
