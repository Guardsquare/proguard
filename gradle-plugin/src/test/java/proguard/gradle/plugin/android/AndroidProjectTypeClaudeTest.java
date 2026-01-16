/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 */
package proguard.gradle.plugin.android;

import kotlin.enums.EnumEntries;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for proguard.gradle.plugin.android.AndroidProjectType enum.
 *
 * Tests the following methods:
 * - values()[Lproguard/gradle/plugin/android/AndroidProjectType; - Returns an array containing all enum values
 * - valueOf(Ljava/lang/String;)Lproguard/gradle/plugin/android/AndroidProjectType; - Returns enum constant by name
 * - getEntries()Lkotlin/enums/EnumEntries; - Returns EnumEntries (Kotlin 1.9+ feature)
 *
 * This enum represents the type of Android project: application or library.
 * It's used by AndroidPlugin to determine how to apply ProGuard transformations.
 */
public class AndroidProjectTypeClaudeTest {

    // ==================== Tests for values() method ====================

    /**
     * Test that values() returns a non-null array.
     * The values() method should always return an array, never null.
     */
    @Test
    public void testValues_returnsNonNull() {
        // When: Getting all enum values
        AndroidProjectType[] values = AndroidProjectType.values();

        // Then: Should return a non-null array
        assertNotNull(values, "values() should not return null");
    }

    /**
     * Test that values() returns an array with exactly 2 elements.
     * AndroidProjectType has exactly two enum constants: ANDROID_APPLICATION and ANDROID_LIBRARY.
     */
    @Test
    public void testValues_returnsCorrectLength() {
        // When: Getting all enum values
        AndroidProjectType[] values = AndroidProjectType.values();

        // Then: Should return an array with 2 elements
        assertNotNull(values, "values() should not return null");
        assertEquals(2, values.length, "AndroidProjectType should have exactly 2 enum constants");
    }

    /**
     * Test that values() contains ANDROID_APPLICATION.
     * Verifies that the first enum constant exists in the array.
     */
    @Test
    public void testValues_containsAndroidApplication() {
        // When: Getting all enum values
        AndroidProjectType[] values = AndroidProjectType.values();

        // Then: Should contain ANDROID_APPLICATION
        assertNotNull(values, "values() should not return null");
        boolean found = false;
        for (AndroidProjectType type : values) {
            if (type == AndroidProjectType.ANDROID_APPLICATION) {
                found = true;
                break;
            }
        }
        assertTrue(found, "values() should contain ANDROID_APPLICATION");
    }

    /**
     * Test that values() contains ANDROID_LIBRARY.
     * Verifies that the second enum constant exists in the array.
     */
    @Test
    public void testValues_containsAndroidLibrary() {
        // When: Getting all enum values
        AndroidProjectType[] values = AndroidProjectType.values();

        // Then: Should contain ANDROID_LIBRARY
        assertNotNull(values, "values() should not return null");
        boolean found = false;
        for (AndroidProjectType type : values) {
            if (type == AndroidProjectType.ANDROID_LIBRARY) {
                found = true;
                break;
            }
        }
        assertTrue(found, "values() should contain ANDROID_LIBRARY");
    }

    /**
     * Test that values() returns the enum constants in declaration order.
     * Enum values() should maintain the order defined in the source code.
     */
    @Test
    public void testValues_returnsInCorrectOrder() {
        // When: Getting all enum values
        AndroidProjectType[] values = AndroidProjectType.values();

        // Then: Should return ANDROID_APPLICATION first, then ANDROID_LIBRARY
        assertNotNull(values, "values() should not return null");
        assertEquals(2, values.length, "Should have 2 elements");
        assertEquals(AndroidProjectType.ANDROID_APPLICATION, values[0],
            "First element should be ANDROID_APPLICATION");
        assertEquals(AndroidProjectType.ANDROID_LIBRARY, values[1],
            "Second element should be ANDROID_LIBRARY");
    }

    /**
     * Test that values() returns a new array each time (defensive copy).
     * Modifying the returned array should not affect subsequent calls.
     */
    @Test
    public void testValues_returnsDefensiveCopy() {
        // When: Getting values and modifying the array
        AndroidProjectType[] values1 = AndroidProjectType.values();
        AndroidProjectType original = values1[0];
        values1[0] = null; // Modify the array

        AndroidProjectType[] values2 = AndroidProjectType.values();

        // Then: Second call should not be affected by modification
        assertNotNull(values2, "Second values() call should not return null");
        assertNotNull(values2[0], "Second array should not have null at index 0");
        assertEquals(original, values2[0], "Second array should have the original value");
    }

    /**
     * Test that each element in values() is non-null.
     * All enum constants should be non-null.
     */
    @Test
    public void testValues_allElementsNonNull() {
        // When: Getting all enum values
        AndroidProjectType[] values = AndroidProjectType.values();

        // Then: All elements should be non-null
        assertNotNull(values, "values() should not return null");
        for (int i = 0; i < values.length; i++) {
            assertNotNull(values[i], "Element at index " + i + " should not be null");
        }
    }

    // ==================== Tests for valueOf(String) method ====================

    /**
     * Test that valueOf() returns ANDROID_APPLICATION for "ANDROID_APPLICATION".
     * Tests the basic functionality of valueOf with the first enum constant.
     */
    @Test
    public void testValueOf_androidApplication() {
        // When: Getting enum by name
        AndroidProjectType type = AndroidProjectType.valueOf("ANDROID_APPLICATION");

        // Then: Should return ANDROID_APPLICATION
        assertNotNull(type, "valueOf should not return null");
        assertEquals(AndroidProjectType.ANDROID_APPLICATION, type,
            "valueOf('ANDROID_APPLICATION') should return ANDROID_APPLICATION");
    }

    /**
     * Test that valueOf() returns ANDROID_LIBRARY for "ANDROID_LIBRARY".
     * Tests the basic functionality of valueOf with the second enum constant.
     */
    @Test
    public void testValueOf_androidLibrary() {
        // When: Getting enum by name
        AndroidProjectType type = AndroidProjectType.valueOf("ANDROID_LIBRARY");

        // Then: Should return ANDROID_LIBRARY
        assertNotNull(type, "valueOf should not return null");
        assertEquals(AndroidProjectType.ANDROID_LIBRARY, type,
            "valueOf('ANDROID_LIBRARY') should return ANDROID_LIBRARY");
    }

    /**
     * Test that valueOf() throws IllegalArgumentException for invalid name.
     * valueOf should fail with appropriate exception for non-existent enum constant.
     */
    @Test
    public void testValueOf_throwsExceptionForInvalidName() {
        // When/Then: valueOf with invalid name should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            AndroidProjectType.valueOf("INVALID_NAME");
        }, "valueOf should throw IllegalArgumentException for invalid name");
    }

    /**
     * Test that valueOf() throws IllegalArgumentException for lowercase name.
     * Enum names are case-sensitive.
     */
    @Test
    public void testValueOf_throwsExceptionForLowercaseName() {
        // When/Then: valueOf with lowercase name should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            AndroidProjectType.valueOf("android_application");
        }, "valueOf should throw IllegalArgumentException for lowercase name");
    }

    /**
     * Test that valueOf() throws NullPointerException for null name.
     * valueOf should not accept null input.
     */
    @Test
    public void testValueOf_throwsExceptionForNull() {
        // When/Then: valueOf with null should throw NullPointerException
        assertThrows(NullPointerException.class, () -> {
            AndroidProjectType.valueOf(null);
        }, "valueOf should throw NullPointerException for null");
    }

    /**
     * Test that valueOf() throws IllegalArgumentException for empty string.
     * valueOf should not accept empty string.
     */
    @Test
    public void testValueOf_throwsExceptionForEmptyString() {
        // When/Then: valueOf with empty string should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            AndroidProjectType.valueOf("");
        }, "valueOf should throw IllegalArgumentException for empty string");
    }

    /**
     * Test that valueOf() returns the same instance as values() array.
     * valueOf should return the same enum constant instance.
     */
    @Test
    public void testValueOf_returnsSameInstanceAsValues() {
        // When: Getting enum by valueOf and values
        AndroidProjectType fromValueOf = AndroidProjectType.valueOf("ANDROID_APPLICATION");
        AndroidProjectType fromValues = AndroidProjectType.values()[0];

        // Then: Should be the same instance
        assertNotNull(fromValueOf, "valueOf result should not be null");
        assertNotNull(fromValues, "values result should not be null");
        assertSame(fromValueOf, fromValues,
            "valueOf should return the same instance as in values() array");
    }

    /**
     * Test that valueOf() is consistent across multiple calls.
     * Multiple calls with the same name should return the same instance.
     */
    @Test
    public void testValueOf_consistentAcrossCalls() {
        // When: Calling valueOf multiple times with the same name
        AndroidProjectType type1 = AndroidProjectType.valueOf("ANDROID_APPLICATION");
        AndroidProjectType type2 = AndroidProjectType.valueOf("ANDROID_APPLICATION");

        // Then: Should return the same instance
        assertNotNull(type1, "First valueOf result should not be null");
        assertNotNull(type2, "Second valueOf result should not be null");
        assertSame(type1, type2, "Multiple valueOf calls should return the same instance");
    }

    /**
     * Test that valueOf() works for all values returned by values().
     * Every enum constant name from values() should work with valueOf.
     */
    @Test
    public void testValueOf_worksForAllValuesNames() {
        // When: Getting all enum values and using valueOf with their names
        AndroidProjectType[] values = AndroidProjectType.values();

        // Then: valueOf should work for each name
        assertNotNull(values, "values() should not return null");
        for (AndroidProjectType value : values) {
            AndroidProjectType fromValueOf = AndroidProjectType.valueOf(value.name());
            assertNotNull(fromValueOf, "valueOf should return non-null for " + value.name());
            assertSame(value, fromValueOf,
                "valueOf should return the same instance as the original enum constant");
        }
    }

    // ==================== Tests for getEntries() method ====================

    /**
     * Test that getEntries() returns a non-null EnumEntries object.
     * The getEntries() method is a Kotlin 1.9+ feature that returns EnumEntries.
     */
    @Test
    public void testGetEntries_returnsNonNull() {
        // When: Getting enum entries
        EnumEntries<AndroidProjectType> entries = AndroidProjectType.getEntries();

        // Then: Should return a non-null EnumEntries object
        assertNotNull(entries, "getEntries() should not return null");
    }

    /**
     * Test that getEntries() returns a list with exactly 2 elements.
     * EnumEntries should contain all enum constants.
     */
    @Test
    public void testGetEntries_returnsCorrectSize() {
        // When: Getting enum entries
        EnumEntries<AndroidProjectType> entries = AndroidProjectType.getEntries();

        // Then: Should have size 2
        assertNotNull(entries, "getEntries() should not return null");
        assertEquals(2, entries.size(), "EnumEntries should have size 2");
    }

    /**
     * Test that getEntries() contains ANDROID_APPLICATION.
     * Verifies that the first enum constant exists in the entries.
     */
    @Test
    public void testGetEntries_containsAndroidApplication() {
        // When: Getting enum entries
        EnumEntries<AndroidProjectType> entries = AndroidProjectType.getEntries();

        // Then: Should contain ANDROID_APPLICATION
        assertNotNull(entries, "getEntries() should not return null");
        assertTrue(entries.contains(AndroidProjectType.ANDROID_APPLICATION),
            "EnumEntries should contain ANDROID_APPLICATION");
    }

    /**
     * Test that getEntries() contains ANDROID_LIBRARY.
     * Verifies that the second enum constant exists in the entries.
     */
    @Test
    public void testGetEntries_containsAndroidLibrary() {
        // When: Getting enum entries
        EnumEntries<AndroidProjectType> entries = AndroidProjectType.getEntries();

        // Then: Should contain ANDROID_LIBRARY
        assertNotNull(entries, "getEntries() should not return null");
        assertTrue(entries.contains(AndroidProjectType.ANDROID_LIBRARY),
            "EnumEntries should contain ANDROID_LIBRARY");
    }

    /**
     * Test that getEntries() returns entries in declaration order.
     * EnumEntries should maintain the order defined in the source code.
     */
    @Test
    public void testGetEntries_returnsInCorrectOrder() {
        // When: Getting enum entries
        EnumEntries<AndroidProjectType> entries = AndroidProjectType.getEntries();

        // Then: Should return ANDROID_APPLICATION first, then ANDROID_LIBRARY
        assertNotNull(entries, "getEntries() should not return null");
        assertEquals(2, entries.size(), "Should have 2 entries");
        assertEquals(AndroidProjectType.ANDROID_APPLICATION, entries.get(0),
            "First entry should be ANDROID_APPLICATION");
        assertEquals(AndroidProjectType.ANDROID_LIBRARY, entries.get(1),
            "Second entry should be ANDROID_LIBRARY");
    }

    /**
     * Test that getEntries() returns an immutable list.
     * EnumEntries should not allow modification.
     */
    @Test
    public void testGetEntries_isImmutable() {
        // When: Getting enum entries and attempting to modify
        EnumEntries<AndroidProjectType> entries = AndroidProjectType.getEntries();

        // Then: Should throw UnsupportedOperationException on modification
        assertNotNull(entries, "getEntries() should not return null");
        assertThrows(UnsupportedOperationException.class, () -> {
            entries.add(AndroidProjectType.ANDROID_APPLICATION);
        }, "EnumEntries should be immutable and throw UnsupportedOperationException on add");
    }

    /**
     * Test that getEntries() returns the same instance on multiple calls.
     * EnumEntries should be cached and return the same instance.
     */
    @Test
    public void testGetEntries_returnsSameInstance() {
        // When: Calling getEntries multiple times
        EnumEntries<AndroidProjectType> entries1 = AndroidProjectType.getEntries();
        EnumEntries<AndroidProjectType> entries2 = AndroidProjectType.getEntries();

        // Then: Should return the same instance
        assertNotNull(entries1, "First getEntries() result should not be null");
        assertNotNull(entries2, "Second getEntries() result should not be null");
        assertSame(entries1, entries2,
            "Multiple getEntries() calls should return the same instance");
    }

    /**
     * Test that getEntries() supports iteration.
     * EnumEntries should be iterable.
     */
    @Test
    public void testGetEntries_supportsIteration() {
        // When: Getting enum entries and iterating
        EnumEntries<AndroidProjectType> entries = AndroidProjectType.getEntries();

        // Then: Should be iterable
        assertNotNull(entries, "getEntries() should not return null");
        int count = 0;
        for (AndroidProjectType type : entries) {
            assertNotNull(type, "Iterated element should not be null");
            count++;
        }
        assertEquals(2, count, "Should iterate over 2 elements");
    }

    /**
     * Test that getEntries() indexOf() method works correctly.
     * EnumEntries should support indexOf operations.
     */
    @Test
    public void testGetEntries_indexOfWorks() {
        // When: Getting enum entries and using indexOf
        EnumEntries<AndroidProjectType> entries = AndroidProjectType.getEntries();

        // Then: indexOf should work correctly
        assertNotNull(entries, "getEntries() should not return null");
        assertEquals(0, entries.indexOf(AndroidProjectType.ANDROID_APPLICATION),
            "ANDROID_APPLICATION should be at index 0");
        assertEquals(1, entries.indexOf(AndroidProjectType.ANDROID_LIBRARY),
            "ANDROID_LIBRARY should be at index 1");
    }

    /**
     * Test that getEntries() isEmpty() returns false.
     * EnumEntries should not be empty as the enum has constants.
     */
    @Test
    public void testGetEntries_isNotEmpty() {
        // When: Getting enum entries
        EnumEntries<AndroidProjectType> entries = AndroidProjectType.getEntries();

        // Then: Should not be empty
        assertNotNull(entries, "getEntries() should not return null");
        assertFalse(entries.isEmpty(), "EnumEntries should not be empty");
    }

    /**
     * Test that getEntries() contains() returns false for null.
     * EnumEntries should not contain null.
     */
    @Test
    public void testGetEntries_doesNotContainNull() {
        // When: Getting enum entries and checking for null
        EnumEntries<AndroidProjectType> entries = AndroidProjectType.getEntries();

        // Then: Should not contain null
        assertNotNull(entries, "getEntries() should not return null");
        assertFalse(entries.contains(null), "EnumEntries should not contain null");
    }

    // ==================== Tests for enum constant properties ====================

    /**
     * Test that enum constants have correct name() values.
     * Each enum constant should return its name as defined in the source.
     */
    @Test
    public void testEnumConstants_haveCorrectNames() {
        // When: Getting name of each enum constant
        String appName = AndroidProjectType.ANDROID_APPLICATION.name();
        String libName = AndroidProjectType.ANDROID_LIBRARY.name();

        // Then: Names should match the constant declarations
        assertEquals("ANDROID_APPLICATION", appName,
            "ANDROID_APPLICATION.name() should return 'ANDROID_APPLICATION'");
        assertEquals("ANDROID_LIBRARY", libName,
            "ANDROID_LIBRARY.name() should return 'ANDROID_LIBRARY'");
    }

    /**
     * Test that enum constants have correct ordinal values.
     * Ordinals should be assigned based on declaration order (starting from 0).
     */
    @Test
    public void testEnumConstants_haveCorrectOrdinals() {
        // When: Getting ordinal of each enum constant
        int appOrdinal = AndroidProjectType.ANDROID_APPLICATION.ordinal();
        int libOrdinal = AndroidProjectType.ANDROID_LIBRARY.ordinal();

        // Then: Ordinals should be 0 and 1 respectively
        assertEquals(0, appOrdinal,
            "ANDROID_APPLICATION should have ordinal 0");
        assertEquals(1, libOrdinal,
            "ANDROID_LIBRARY should have ordinal 1");
    }

    /**
     * Test that enum constants toString() returns the name.
     * By default, enum toString() returns the name.
     */
    @Test
    public void testEnumConstants_toStringReturnsName() {
        // When: Converting enum constants to string
        String appString = AndroidProjectType.ANDROID_APPLICATION.toString();
        String libString = AndroidProjectType.ANDROID_LIBRARY.toString();

        // Then: Should return the enum constant names
        assertEquals("ANDROID_APPLICATION", appString,
            "ANDROID_APPLICATION.toString() should return 'ANDROID_APPLICATION'");
        assertEquals("ANDROID_LIBRARY", libString,
            "ANDROID_LIBRARY.toString() should return 'ANDROID_LIBRARY'");
    }

    /**
     * Test that enum constants are singletons.
     * Each enum constant should have only one instance.
     */
    @Test
    public void testEnumConstants_areSingletons() {
        // When: Getting enum constants through different methods
        AndroidProjectType app1 = AndroidProjectType.ANDROID_APPLICATION;
        AndroidProjectType app2 = AndroidProjectType.valueOf("ANDROID_APPLICATION");
        AndroidProjectType app3 = AndroidProjectType.values()[0];

        // Then: All should be the same instance
        assertSame(app1, app2, "Enum constants should be singletons (app1 == app2)");
        assertSame(app1, app3, "Enum constants should be singletons (app1 == app3)");
        assertSame(app2, app3, "Enum constants should be singletons (app2 == app3)");
    }

    /**
     * Test that enum constants can be compared using ==.
     * Enum instances can be compared using reference equality.
     */
    @Test
    public void testEnumConstants_canBeComparedWithEquals() {
        // When: Comparing enum constants
        AndroidProjectType app = AndroidProjectType.ANDROID_APPLICATION;
        AndroidProjectType lib = AndroidProjectType.ANDROID_LIBRARY;

        // Then: == comparison should work correctly
        assertTrue(app == AndroidProjectType.ANDROID_APPLICATION,
            "Should be able to compare with ==");
        assertFalse(app == lib,
            "Different enum constants should not be equal");
    }

    /**
     * Test that enum constants can be used in switch statements.
     * Enums are commonly used in switch statements.
     */
    @Test
    public void testEnumConstants_canBeUsedInSwitch() {
        // When: Using enum in switch statement
        AndroidProjectType type = AndroidProjectType.ANDROID_APPLICATION;
        String result;

        switch (type) {
            case ANDROID_APPLICATION:
                result = "application";
                break;
            case ANDROID_LIBRARY:
                result = "library";
                break;
            default:
                result = "unknown";
        }

        // Then: Switch should work correctly
        assertEquals("application", result,
            "Switch statement should handle ANDROID_APPLICATION correctly");
    }

    /**
     * Test enum constant usage with ANDROID_LIBRARY in switch.
     * Tests the second enum constant in a switch statement.
     */
    @Test
    public void testEnumConstants_libraryInSwitch() {
        // When: Using ANDROID_LIBRARY in switch statement
        AndroidProjectType type = AndroidProjectType.ANDROID_LIBRARY;
        String result;

        switch (type) {
            case ANDROID_APPLICATION:
                result = "application";
                break;
            case ANDROID_LIBRARY:
                result = "library";
                break;
            default:
                result = "unknown";
        }

        // Then: Switch should work correctly
        assertEquals("library", result,
            "Switch statement should handle ANDROID_LIBRARY correctly");
    }

    /**
     * Test that enum compareTo works correctly.
     * Enum compareTo should compare based on ordinal values.
     */
    @Test
    public void testEnumConstants_compareToWorks() {
        // When: Comparing enum constants
        AndroidProjectType app = AndroidProjectType.ANDROID_APPLICATION;
        AndroidProjectType lib = AndroidProjectType.ANDROID_LIBRARY;

        // Then: compareTo should work based on ordinal
        assertTrue(app.compareTo(lib) < 0,
            "ANDROID_APPLICATION should be < ANDROID_LIBRARY (based on ordinal)");
        assertTrue(lib.compareTo(app) > 0,
            "ANDROID_LIBRARY should be > ANDROID_APPLICATION (based on ordinal)");
        assertEquals(0, app.compareTo(app),
            "Enum constant should be equal to itself (compareTo == 0)");
    }
}
