/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 */
package proguard.gradle.plugin.android.tasks;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ConsumerRuleFilterEntry data class.
 *
 * Tests all methods in proguard.gradle.plugin.android.tasks.ConsumerRuleFilterEntry:
 * - Constructor: <init>(String, String)
 * - getGroup()
 * - getModule()
 * - component1()
 * - component2()
 * - copy(String, String)
 * - toString()
 * - hashCode()
 * - equals(Object)
 *
 * ConsumerRuleFilterEntry is a Kotlin data class that represents a filter entry
 * for consumer rules, identifying a dependency by its group and module name.
 * It implements Serializable for persistence and Gradle configuration cache support.
 */
public class ConsumerRuleFilterEntryClaudeTest {

    // ==================== Constructor Tests ====================

    /**
     * Test constructor with valid group and module names.
     * The constructor should create an instance with the provided values.
     */
    @Test
    public void testConstructor_validGroupAndModule() {
        // When: Creating a ConsumerRuleFilterEntry
        ConsumerRuleFilterEntry entry = new ConsumerRuleFilterEntry("com.example", "library");

        // Then: Should create instance with correct values
        assertNotNull(entry, "Constructor should create a non-null instance");
        assertEquals("com.example", entry.getGroup(), "Group should match constructor argument");
        assertEquals("library", entry.getModule(), "Module should match constructor argument");
    }

    /**
     * Test constructor with different group patterns.
     * Various group name formats should be accepted.
     */
    @Test
    public void testConstructor_variousGroupPatterns() {
        // When: Creating entries with different group patterns
        ConsumerRuleFilterEntry entry1 = new ConsumerRuleFilterEntry("com.example", "lib");
        ConsumerRuleFilterEntry entry2 = new ConsumerRuleFilterEntry("org.test", "lib");
        ConsumerRuleFilterEntry entry3 = new ConsumerRuleFilterEntry("androidx.core", "lib");
        ConsumerRuleFilterEntry entry4 = new ConsumerRuleFilterEntry("io.github.user", "lib");

        // Then: All patterns should be accepted
        assertEquals("com.example", entry1.getGroup());
        assertEquals("org.test", entry2.getGroup());
        assertEquals("androidx.core", entry3.getGroup());
        assertEquals("io.github.user", entry4.getGroup());
    }

    /**
     * Test constructor with different module patterns.
     * Various module name formats should be accepted.
     */
    @Test
    public void testConstructor_variousModulePatterns() {
        // When: Creating entries with different module patterns
        ConsumerRuleFilterEntry entry1 = new ConsumerRuleFilterEntry("com.example", "library");
        ConsumerRuleFilterEntry entry2 = new ConsumerRuleFilterEntry("com.example", "library-ktx");
        ConsumerRuleFilterEntry entry3 = new ConsumerRuleFilterEntry("com.example", "library_utils");
        ConsumerRuleFilterEntry entry4 = new ConsumerRuleFilterEntry("com.example", "library.module");

        // Then: All patterns should be accepted
        assertEquals("library", entry1.getModule());
        assertEquals("library-ktx", entry2.getModule());
        assertEquals("library_utils", entry3.getModule());
        assertEquals("library.module", entry4.getModule());
    }

    /**
     * Test constructor with empty strings.
     * Empty group and module strings should be allowed.
     */
    @Test
    public void testConstructor_emptyStrings() {
        // When: Creating entries with empty strings
        ConsumerRuleFilterEntry entry1 = new ConsumerRuleFilterEntry("", "module");
        ConsumerRuleFilterEntry entry2 = new ConsumerRuleFilterEntry("group", "");
        ConsumerRuleFilterEntry entry3 = new ConsumerRuleFilterEntry("", "");

        // Then: Empty strings should be accepted
        assertEquals("", entry1.getGroup());
        assertEquals("module", entry1.getModule());
        assertEquals("group", entry2.getGroup());
        assertEquals("", entry2.getModule());
        assertEquals("", entry3.getGroup());
        assertEquals("", entry3.getModule());
    }

    /**
     * Test constructor with special characters.
     * Special characters in group and module names should be handled.
     */
    @Test
    public void testConstructor_specialCharacters() {
        // When: Creating entries with special characters
        ConsumerRuleFilterEntry entry1 = new ConsumerRuleFilterEntry("com.example-test", "library-v2");
        ConsumerRuleFilterEntry entry2 = new ConsumerRuleFilterEntry("com.example_test", "library_v2");
        ConsumerRuleFilterEntry entry3 = new ConsumerRuleFilterEntry("com.example.test", "library.v2");

        // Then: Special characters should be preserved
        assertEquals("com.example-test", entry1.getGroup());
        assertEquals("library-v2", entry1.getModule());
        assertEquals("com.example_test", entry2.getGroup());
        assertEquals("library_v2", entry2.getModule());
        assertEquals("com.example.test", entry3.getGroup());
        assertEquals("library.v2", entry3.getModule());
    }

    /**
     * Test constructor with long strings.
     * Long group and module names should be accepted.
     */
    @Test
    public void testConstructor_longStrings() {
        // Given: Long strings
        String longGroup = "com.verylonggroupname.with.many.segments.that.goes.on.and.on";
        String longModule = "very-long-module-name-with-many-dashes-and-words";

        // When: Creating an entry with long strings
        ConsumerRuleFilterEntry entry = new ConsumerRuleFilterEntry(longGroup, longModule);

        // Then: Long strings should be accepted
        assertEquals(longGroup, entry.getGroup());
        assertEquals(longModule, entry.getModule());
    }

    // ==================== getGroup() Tests ====================

    /**
     * Test getGroup returns the correct value.
     * The getter should return the group set in the constructor.
     */
    @Test
    public void testGetGroup_returnsConstructorValue() {
        // Given: An entry with a specific group
        ConsumerRuleFilterEntry entry = new ConsumerRuleFilterEntry("com.example", "library");

        // When: Getting the group
        String group = entry.getGroup();

        // Then: Should return the correct group
        assertEquals("com.example", group, "getGroup() should return constructor value");
    }

    /**
     * Test getGroup is consistent across multiple calls.
     * Multiple calls should return the same value.
     */
    @Test
    public void testGetGroup_consistentAcrossMultipleCalls() {
        // Given: An entry
        ConsumerRuleFilterEntry entry = new ConsumerRuleFilterEntry("com.example", "library");

        // When: Calling getGroup multiple times
        String group1 = entry.getGroup();
        String group2 = entry.getGroup();
        String group3 = entry.getGroup();

        // Then: All calls should return the same value
        assertEquals(group1, group2, "Multiple calls should return same value");
        assertEquals(group2, group3, "Multiple calls should return same value");
        assertEquals("com.example", group1);
    }

    /**
     * Test getGroup with empty string.
     * Empty group string should be returned correctly.
     */
    @Test
    public void testGetGroup_emptyString() {
        // Given: An entry with empty group
        ConsumerRuleFilterEntry entry = new ConsumerRuleFilterEntry("", "library");

        // When: Getting the group
        String group = entry.getGroup();

        // Then: Should return empty string
        assertEquals("", group, "getGroup() should return empty string");
    }

    // ==================== getModule() Tests ====================

    /**
     * Test getModule returns the correct value.
     * The getter should return the module set in the constructor.
     */
    @Test
    public void testGetModule_returnsConstructorValue() {
        // Given: An entry with a specific module
        ConsumerRuleFilterEntry entry = new ConsumerRuleFilterEntry("com.example", "library");

        // When: Getting the module
        String module = entry.getModule();

        // Then: Should return the correct module
        assertEquals("library", module, "getModule() should return constructor value");
    }

    /**
     * Test getModule is consistent across multiple calls.
     * Multiple calls should return the same value.
     */
    @Test
    public void testGetModule_consistentAcrossMultipleCalls() {
        // Given: An entry
        ConsumerRuleFilterEntry entry = new ConsumerRuleFilterEntry("com.example", "library");

        // When: Calling getModule multiple times
        String module1 = entry.getModule();
        String module2 = entry.getModule();
        String module3 = entry.getModule();

        // Then: All calls should return the same value
        assertEquals(module1, module2, "Multiple calls should return same value");
        assertEquals(module2, module3, "Multiple calls should return same value");
        assertEquals("library", module1);
    }

    /**
     * Test getModule with empty string.
     * Empty module string should be returned correctly.
     */
    @Test
    public void testGetModule_emptyString() {
        // Given: An entry with empty module
        ConsumerRuleFilterEntry entry = new ConsumerRuleFilterEntry("com.example", "");

        // When: Getting the module
        String module = entry.getModule();

        // Then: Should return empty string
        assertEquals("", module, "getModule() should return empty string");
    }

    // ==================== component1() Tests ====================

    /**
     * Test component1 returns the group value.
     * Kotlin data class component1 should return the first property (group).
     */
    @Test
    public void testComponent1_returnsGroup() {
        // Given: An entry
        ConsumerRuleFilterEntry entry = new ConsumerRuleFilterEntry("com.example", "library");

        // When: Calling component1
        String component1 = entry.component1();

        // Then: Should return the group
        assertEquals("com.example", component1, "component1() should return group");
        assertEquals(entry.getGroup(), component1, "component1() should match getGroup()");
    }

    /**
     * Test component1 is consistent with getGroup.
     * component1 should always return the same value as getGroup.
     */
    @Test
    public void testComponent1_consistentWithGetGroup() {
        // Given: Various entries
        ConsumerRuleFilterEntry entry1 = new ConsumerRuleFilterEntry("com.example", "library");
        ConsumerRuleFilterEntry entry2 = new ConsumerRuleFilterEntry("org.test", "module");
        ConsumerRuleFilterEntry entry3 = new ConsumerRuleFilterEntry("", "lib");

        // When/Then: component1 should match getGroup
        assertEquals(entry1.getGroup(), entry1.component1());
        assertEquals(entry2.getGroup(), entry2.component1());
        assertEquals(entry3.getGroup(), entry3.component1());
    }

    // ==================== component2() Tests ====================

    /**
     * Test component2 returns the module value.
     * Kotlin data class component2 should return the second property (module).
     */
    @Test
    public void testComponent2_returnsModule() {
        // Given: An entry
        ConsumerRuleFilterEntry entry = new ConsumerRuleFilterEntry("com.example", "library");

        // When: Calling component2
        String component2 = entry.component2();

        // Then: Should return the module
        assertEquals("library", component2, "component2() should return module");
        assertEquals(entry.getModule(), component2, "component2() should match getModule()");
    }

    /**
     * Test component2 is consistent with getModule.
     * component2 should always return the same value as getModule.
     */
    @Test
    public void testComponent2_consistentWithGetModule() {
        // Given: Various entries
        ConsumerRuleFilterEntry entry1 = new ConsumerRuleFilterEntry("com.example", "library");
        ConsumerRuleFilterEntry entry2 = new ConsumerRuleFilterEntry("org.test", "module");
        ConsumerRuleFilterEntry entry3 = new ConsumerRuleFilterEntry("group", "");

        // When/Then: component2 should match getModule
        assertEquals(entry1.getModule(), entry1.component2());
        assertEquals(entry2.getModule(), entry2.component2());
        assertEquals(entry3.getModule(), entry3.component2());
    }

    /**
     * Test component functions work together for destructuring.
     * Both component functions should provide correct values for destructuring.
     */
    @Test
    public void testComponentFunctions_destructuring() {
        // Given: An entry
        ConsumerRuleFilterEntry entry = new ConsumerRuleFilterEntry("com.example", "library");

        // When: Using component functions
        String group = entry.component1();
        String module = entry.component2();

        // Then: Should match the original values
        assertEquals("com.example", group);
        assertEquals("library", module);
        assertEquals(entry.getGroup(), group);
        assertEquals(entry.getModule(), module);
    }

    // ==================== copy() Tests ====================

    /**
     * Test copy with both parameters changed.
     * The copy method should create a new instance with the provided values.
     */
    @Test
    public void testCopy_bothParametersChanged() {
        // Given: An original entry
        ConsumerRuleFilterEntry original = new ConsumerRuleFilterEntry("com.example", "library");

        // When: Copying with new values
        ConsumerRuleFilterEntry copy = original.copy("org.test", "module");

        // Then: Copy should have new values
        assertEquals("org.test", copy.getGroup(), "Copy should have new group");
        assertEquals("module", copy.getModule(), "Copy should have new module");
        assertNotEquals(original, copy, "Copy should not equal original");
    }

    /**
     * Test copy with only group changed.
     * The copy method should allow changing only the group parameter.
     */
    @Test
    public void testCopy_onlyGroupChanged() {
        // Given: An original entry
        ConsumerRuleFilterEntry original = new ConsumerRuleFilterEntry("com.example", "library");

        // When: Copying with new group
        ConsumerRuleFilterEntry copy = original.copy("org.test", original.getModule());

        // Then: Copy should have new group, same module
        assertEquals("org.test", copy.getGroup(), "Copy should have new group");
        assertEquals("library", copy.getModule(), "Copy should keep same module");
        assertEquals(original.getModule(), copy.getModule());
    }

    /**
     * Test copy with only module changed.
     * The copy method should allow changing only the module parameter.
     */
    @Test
    public void testCopy_onlyModuleChanged() {
        // Given: An original entry
        ConsumerRuleFilterEntry original = new ConsumerRuleFilterEntry("com.example", "library");

        // When: Copying with new module
        ConsumerRuleFilterEntry copy = original.copy(original.getGroup(), "module");

        // Then: Copy should have same group, new module
        assertEquals("com.example", copy.getGroup(), "Copy should keep same group");
        assertEquals("module", copy.getModule(), "Copy should have new module");
        assertEquals(original.getGroup(), copy.getGroup());
    }

    /**
     * Test copy with no parameters changed.
     * The copy method should create an equal but separate instance.
     */
    @Test
    public void testCopy_noParametersChanged() {
        // Given: An original entry
        ConsumerRuleFilterEntry original = new ConsumerRuleFilterEntry("com.example", "library");

        // When: Copying with same values
        ConsumerRuleFilterEntry copy = original.copy(original.getGroup(), original.getModule());

        // Then: Copy should be equal but not the same instance
        assertEquals(original, copy, "Copy with same values should be equal");
        assertNotSame(original, copy, "Copy should be a different instance");
    }

    /**
     * Test copy creates independent instances.
     * Changes to the copy should not affect the original.
     */
    @Test
    public void testCopy_createsIndependentInstance() {
        // Given: An original entry
        ConsumerRuleFilterEntry original = new ConsumerRuleFilterEntry("com.example", "library");

        // When: Creating a copy and verifying independence
        ConsumerRuleFilterEntry copy = original.copy("org.test", "module");

        // Then: Original should remain unchanged
        assertEquals("com.example", original.getGroup(), "Original should be unchanged");
        assertEquals("library", original.getModule(), "Original should be unchanged");
        assertEquals("org.test", copy.getGroup(), "Copy should have new values");
        assertEquals("module", copy.getModule(), "Copy should have new values");
    }

    /**
     * Test copy with empty strings.
     * The copy method should handle empty strings.
     */
    @Test
    public void testCopy_emptyStrings() {
        // Given: An original entry
        ConsumerRuleFilterEntry original = new ConsumerRuleFilterEntry("com.example", "library");

        // When: Copying with empty strings
        ConsumerRuleFilterEntry copy1 = original.copy("", "module");
        ConsumerRuleFilterEntry copy2 = original.copy("group", "");
        ConsumerRuleFilterEntry copy3 = original.copy("", "");

        // Then: Empty strings should be accepted
        assertEquals("", copy1.getGroup());
        assertEquals("", copy2.getModule());
        assertEquals("", copy3.getGroup());
        assertEquals("", copy3.getModule());
    }

    /**
     * Test copy can be chained.
     * Multiple copy operations should work correctly.
     */
    @Test
    public void testCopy_canBeChained() {
        // Given: An original entry
        ConsumerRuleFilterEntry original = new ConsumerRuleFilterEntry("com.example", "library");

        // When: Chaining copy operations
        ConsumerRuleFilterEntry copy1 = original.copy("org.test", "module1");
        ConsumerRuleFilterEntry copy2 = copy1.copy("io.github", "module2");
        ConsumerRuleFilterEntry copy3 = copy2.copy("androidx.core", "module3");

        // Then: Each copy should have correct values
        assertEquals("org.test", copy1.getGroup());
        assertEquals("module1", copy1.getModule());
        assertEquals("io.github", copy2.getGroup());
        assertEquals("module2", copy2.getModule());
        assertEquals("androidx.core", copy3.getGroup());
        assertEquals("module3", copy3.getModule());
        // Original should remain unchanged
        assertEquals("com.example", original.getGroup());
        assertEquals("library", original.getModule());
    }

    // ==================== toString() Tests ====================

    /**
     * Test toString returns a non-null string.
     * The toString method should never return null.
     */
    @Test
    public void testToString_notNull() {
        // Given: An entry
        ConsumerRuleFilterEntry entry = new ConsumerRuleFilterEntry("com.example", "library");

        // When: Calling toString
        String result = entry.toString();

        // Then: Should not be null
        assertNotNull(result, "toString() should not return null");
    }

    /**
     * Test toString contains the group value.
     * The toString output should include the group.
     */
    @Test
    public void testToString_containsGroup() {
        // Given: An entry with a specific group
        ConsumerRuleFilterEntry entry = new ConsumerRuleFilterEntry("com.example", "library");

        // When: Calling toString
        String result = entry.toString();

        // Then: Should contain the group
        assertTrue(result.contains("com.example"),
            "toString() should contain the group value, got: " + result);
    }

    /**
     * Test toString contains the module value.
     * The toString output should include the module.
     */
    @Test
    public void testToString_containsModule() {
        // Given: An entry with a specific module
        ConsumerRuleFilterEntry entry = new ConsumerRuleFilterEntry("com.example", "library");

        // When: Calling toString
        String result = entry.toString();

        // Then: Should contain the module
        assertTrue(result.contains("library"),
            "toString() should contain the module value, got: " + result);
    }

    /**
     * Test toString format follows Kotlin data class convention.
     * Kotlin data classes generate toString in format: ClassName(prop1=value1, prop2=value2).
     */
    @Test
    public void testToString_followsKotlinConvention() {
        // Given: An entry
        ConsumerRuleFilterEntry entry = new ConsumerRuleFilterEntry("com.example", "library");

        // When: Calling toString
        String result = entry.toString();

        // Then: Should follow Kotlin data class format
        assertTrue(result.startsWith("ConsumerRuleFilterEntry("),
            "toString() should start with class name, got: " + result);
        assertTrue(result.contains("group="),
            "toString() should contain 'group=' label, got: " + result);
        assertTrue(result.contains("module="),
            "toString() should contain 'module=' label, got: " + result);
        assertTrue(result.endsWith(")"),
            "toString() should end with ')', got: " + result);
    }

    /**
     * Test toString with different values.
     * Different entries should produce different toString outputs.
     */
    @Test
    public void testToString_differentForDifferentValues() {
        // Given: Different entries
        ConsumerRuleFilterEntry entry1 = new ConsumerRuleFilterEntry("com.example", "library");
        ConsumerRuleFilterEntry entry2 = new ConsumerRuleFilterEntry("org.test", "module");

        // When: Calling toString
        String result1 = entry1.toString();
        String result2 = entry2.toString();

        // Then: Should produce different strings
        assertNotEquals(result1, result2, "Different entries should have different toString");
    }

    /**
     * Test toString with empty strings.
     * Empty strings should be represented in the output.
     */
    @Test
    public void testToString_emptyStrings() {
        // Given: Entries with empty strings
        ConsumerRuleFilterEntry entry1 = new ConsumerRuleFilterEntry("", "library");
        ConsumerRuleFilterEntry entry2 = new ConsumerRuleFilterEntry("com.example", "");

        // When: Calling toString
        String result1 = entry1.toString();
        String result2 = entry2.toString();

        // Then: Should handle empty strings
        assertNotNull(result1);
        assertNotNull(result2);
        assertTrue(result1.contains("group="));
        assertTrue(result2.contains("module="));
    }

    /**
     * Test toString is consistent across multiple calls.
     * Multiple calls should produce the same string.
     */
    @Test
    public void testToString_consistentAcrossMultipleCalls() {
        // Given: An entry
        ConsumerRuleFilterEntry entry = new ConsumerRuleFilterEntry("com.example", "library");

        // When: Calling toString multiple times
        String result1 = entry.toString();
        String result2 = entry.toString();
        String result3 = entry.toString();

        // Then: All calls should produce the same string
        assertEquals(result1, result2, "Multiple calls should produce same string");
        assertEquals(result2, result3, "Multiple calls should produce same string");
    }

    // ==================== hashCode() Tests ====================

    /**
     * Test hashCode returns consistent value.
     * Multiple calls should return the same hash code.
     */
    @Test
    public void testHashCode_consistent() {
        // Given: An entry
        ConsumerRuleFilterEntry entry = new ConsumerRuleFilterEntry("com.example", "library");

        // When: Calling hashCode multiple times
        int hash1 = entry.hashCode();
        int hash2 = entry.hashCode();
        int hash3 = entry.hashCode();

        // Then: Should return same value
        assertEquals(hash1, hash2, "Multiple calls should return same hash code");
        assertEquals(hash2, hash3, "Multiple calls should return same hash code");
    }

    /**
     * Test equal objects have equal hash codes.
     * If two objects are equal, they must have the same hash code.
     */
    @Test
    public void testHashCode_equalObjectsHaveEqualHashCodes() {
        // Given: Two equal entries
        ConsumerRuleFilterEntry entry1 = new ConsumerRuleFilterEntry("com.example", "library");
        ConsumerRuleFilterEntry entry2 = new ConsumerRuleFilterEntry("com.example", "library");

        // When: Computing hash codes
        int hash1 = entry1.hashCode();
        int hash2 = entry2.hashCode();

        // Then: Hash codes should be equal
        assertEquals(entry1, entry2, "Entries should be equal");
        assertEquals(hash1, hash2, "Equal objects must have equal hash codes");
    }

    /**
     * Test different objects likely have different hash codes.
     * Different objects should ideally have different hash codes (not guaranteed, but expected).
     */
    @Test
    public void testHashCode_differentObjectsLikelyDifferentHashCodes() {
        // Given: Different entries
        ConsumerRuleFilterEntry entry1 = new ConsumerRuleFilterEntry("com.example", "library");
        ConsumerRuleFilterEntry entry2 = new ConsumerRuleFilterEntry("org.test", "module");
        ConsumerRuleFilterEntry entry3 = new ConsumerRuleFilterEntry("com.example", "module");

        // When: Computing hash codes
        int hash1 = entry1.hashCode();
        int hash2 = entry2.hashCode();
        int hash3 = entry3.hashCode();

        // Then: Different objects likely have different hash codes
        // Note: This is not guaranteed by contract, but good hash functions should produce different hashes
        assertTrue(hash1 != hash2 || hash1 != hash3 || hash2 != hash3,
            "Different objects should likely have different hash codes");
    }

    /**
     * Test hashCode with empty strings.
     * Empty strings should produce valid hash codes.
     */
    @Test
    public void testHashCode_emptyStrings() {
        // Given: Entries with empty strings
        ConsumerRuleFilterEntry entry1 = new ConsumerRuleFilterEntry("", "library");
        ConsumerRuleFilterEntry entry2 = new ConsumerRuleFilterEntry("com.example", "");
        ConsumerRuleFilterEntry entry3 = new ConsumerRuleFilterEntry("", "");

        // When: Computing hash codes
        int hash1 = entry1.hashCode();
        int hash2 = entry2.hashCode();
        int hash3 = entry3.hashCode();

        // Then: Should produce valid hash codes
        assertNotNull(Integer.valueOf(hash1));
        assertNotNull(Integer.valueOf(hash2));
        assertNotNull(Integer.valueOf(hash3));
    }

    /**
     * Test hashCode changes when group changes.
     * Entries with different groups should likely have different hash codes.
     */
    @Test
    public void testHashCode_differentForDifferentGroup() {
        // Given: Entries with different groups
        ConsumerRuleFilterEntry entry1 = new ConsumerRuleFilterEntry("com.example", "library");
        ConsumerRuleFilterEntry entry2 = new ConsumerRuleFilterEntry("org.test", "library");

        // When: Computing hash codes
        int hash1 = entry1.hashCode();
        int hash2 = entry2.hashCode();

        // Then: Should likely be different
        assertNotEquals(hash1, hash2,
            "Entries with different groups should have different hash codes");
    }

    /**
     * Test hashCode changes when module changes.
     * Entries with different modules should likely have different hash codes.
     */
    @Test
    public void testHashCode_differentForDifferentModule() {
        // Given: Entries with different modules
        ConsumerRuleFilterEntry entry1 = new ConsumerRuleFilterEntry("com.example", "library");
        ConsumerRuleFilterEntry entry2 = new ConsumerRuleFilterEntry("com.example", "module");

        // When: Computing hash codes
        int hash1 = entry1.hashCode();
        int hash2 = entry2.hashCode();

        // Then: Should likely be different
        assertNotEquals(hash1, hash2,
            "Entries with different modules should have different hash codes");
    }

    // ==================== equals() Tests ====================

    /**
     * Test equals with same instance (reflexive).
     * An object should equal itself.
     */
    @Test
    public void testEquals_reflexive() {
        // Given: An entry
        ConsumerRuleFilterEntry entry = new ConsumerRuleFilterEntry("com.example", "library");

        // When/Then: Should equal itself
        assertEquals(entry, entry, "Object should equal itself");
        assertTrue(entry.equals(entry), "equals() should return true for same instance");
    }

    /**
     * Test equals with identical values (symmetric).
     * Two objects with same values should be equal in both directions.
     */
    @Test
    public void testEquals_symmetricWithIdenticalValues() {
        // Given: Two entries with same values
        ConsumerRuleFilterEntry entry1 = new ConsumerRuleFilterEntry("com.example", "library");
        ConsumerRuleFilterEntry entry2 = new ConsumerRuleFilterEntry("com.example", "library");

        // When/Then: Should be equal in both directions
        assertEquals(entry1, entry2, "Entries with same values should be equal");
        assertEquals(entry2, entry1, "Equality should be symmetric");
        assertTrue(entry1.equals(entry2));
        assertTrue(entry2.equals(entry1));
    }

    /**
     * Test equals is transitive.
     * If a=b and b=c, then a=c.
     */
    @Test
    public void testEquals_transitive() {
        // Given: Three entries with same values
        ConsumerRuleFilterEntry entry1 = new ConsumerRuleFilterEntry("com.example", "library");
        ConsumerRuleFilterEntry entry2 = new ConsumerRuleFilterEntry("com.example", "library");
        ConsumerRuleFilterEntry entry3 = new ConsumerRuleFilterEntry("com.example", "library");

        // When/Then: Transitivity should hold
        assertEquals(entry1, entry2, "entry1 should equal entry2");
        assertEquals(entry2, entry3, "entry2 should equal entry3");
        assertEquals(entry1, entry3, "entry1 should equal entry3 (transitive)");
    }

    /**
     * Test equals with null returns false.
     * An object should not equal null.
     */
    @Test
    public void testEquals_nullReturnsFalse() {
        // Given: An entry
        ConsumerRuleFilterEntry entry = new ConsumerRuleFilterEntry("com.example", "library");

        // When/Then: Should not equal null
        assertNotEquals(entry, null, "Object should not equal null");
        assertFalse(entry.equals(null), "equals(null) should return false");
    }

    /**
     * Test equals with different type returns false.
     * An entry should not equal an object of a different type.
     */
    @Test
    public void testEquals_differentTypeReturnsFalse() {
        // Given: An entry and a different type object
        ConsumerRuleFilterEntry entry = new ConsumerRuleFilterEntry("com.example", "library");
        Object other = "com.example:library";

        // When/Then: Should not equal different type
        assertNotEquals(entry, other, "Object should not equal different type");
        assertFalse(entry.equals(other), "equals() should return false for different type");
    }

    /**
     * Test equals with different group returns false.
     * Entries with different groups should not be equal.
     */
    @Test
    public void testEquals_differentGroupReturnsFalse() {
        // Given: Two entries with different groups
        ConsumerRuleFilterEntry entry1 = new ConsumerRuleFilterEntry("com.example", "library");
        ConsumerRuleFilterEntry entry2 = new ConsumerRuleFilterEntry("org.test", "library");

        // When/Then: Should not be equal
        assertNotEquals(entry1, entry2, "Entries with different groups should not be equal");
        assertFalse(entry1.equals(entry2));
    }

    /**
     * Test equals with different module returns false.
     * Entries with different modules should not be equal.
     */
    @Test
    public void testEquals_differentModuleReturnsFalse() {
        // Given: Two entries with different modules
        ConsumerRuleFilterEntry entry1 = new ConsumerRuleFilterEntry("com.example", "library");
        ConsumerRuleFilterEntry entry2 = new ConsumerRuleFilterEntry("com.example", "module");

        // When/Then: Should not be equal
        assertNotEquals(entry1, entry2, "Entries with different modules should not be equal");
        assertFalse(entry1.equals(entry2));
    }

    /**
     * Test equals with both different returns false.
     * Entries with different group and module should not be equal.
     */
    @Test
    public void testEquals_bothDifferentReturnsFalse() {
        // Given: Two entries with different values
        ConsumerRuleFilterEntry entry1 = new ConsumerRuleFilterEntry("com.example", "library");
        ConsumerRuleFilterEntry entry2 = new ConsumerRuleFilterEntry("org.test", "module");

        // When/Then: Should not be equal
        assertNotEquals(entry1, entry2, "Entries with different values should not be equal");
        assertFalse(entry1.equals(entry2));
    }

    /**
     * Test equals with empty strings.
     * Entries with empty strings should be handled correctly.
     */
    @Test
    public void testEquals_emptyStrings() {
        // Given: Entries with empty strings
        ConsumerRuleFilterEntry entry1 = new ConsumerRuleFilterEntry("", "library");
        ConsumerRuleFilterEntry entry2 = new ConsumerRuleFilterEntry("", "library");
        ConsumerRuleFilterEntry entry3 = new ConsumerRuleFilterEntry("", "module");

        // When/Then: Should handle empty strings correctly
        assertEquals(entry1, entry2, "Entries with same empty group should be equal");
        assertNotEquals(entry1, entry3, "Entries with different modules should not be equal");
    }

    /**
     * Test equals is consistent with hashCode.
     * Equal objects must have equal hash codes.
     */
    @Test
    public void testEquals_consistentWithHashCode() {
        // Given: Two equal entries
        ConsumerRuleFilterEntry entry1 = new ConsumerRuleFilterEntry("com.example", "library");
        ConsumerRuleFilterEntry entry2 = new ConsumerRuleFilterEntry("com.example", "library");

        // When/Then: Equal objects should have equal hash codes
        assertEquals(entry1, entry2, "Entries should be equal");
        assertEquals(entry1.hashCode(), entry2.hashCode(),
            "Equal objects must have equal hash codes");
    }

    /**
     * Test equals with entries from copy.
     * A copy with same values should be equal to the original.
     */
    @Test
    public void testEquals_withCopy() {
        // Given: An original and its copy
        ConsumerRuleFilterEntry original = new ConsumerRuleFilterEntry("com.example", "library");
        ConsumerRuleFilterEntry copy = original.copy(original.getGroup(), original.getModule());

        // When/Then: Should be equal
        assertEquals(original, copy, "Copy with same values should equal original");
        assertNotSame(original, copy, "But should be different instances");
    }

    // ==================== Serialization Tests ====================

    /**
     * Test that ConsumerRuleFilterEntry can be serialized.
     * The class implements Serializable, so it should be serializable.
     */
    @Test
    public void testSerialization_canSerialize() throws Exception {
        // Given: An entry
        ConsumerRuleFilterEntry entry = new ConsumerRuleFilterEntry("com.example", "library");

        // When: Serializing the entry
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(entry);
        oos.close();

        // Then: Should serialize without exception
        byte[] bytes = baos.toByteArray();
        assertTrue(bytes.length > 0, "Serialized data should not be empty");
    }

    /**
     * Test that ConsumerRuleFilterEntry can be deserialized.
     * After serialization, the object should be deserializable.
     */
    @Test
    public void testSerialization_canDeserialize() throws Exception {
        // Given: A serialized entry
        ConsumerRuleFilterEntry original = new ConsumerRuleFilterEntry("com.example", "library");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(original);
        oos.close();

        // When: Deserializing the entry
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        ConsumerRuleFilterEntry deserialized = (ConsumerRuleFilterEntry) ois.readObject();
        ois.close();

        // Then: Should deserialize successfully
        assertNotNull(deserialized, "Deserialized object should not be null");
    }

    /**
     * Test that deserialized object equals original.
     * After serialization/deserialization, the object should be equal to the original.
     */
    @Test
    public void testSerialization_deserializedEqualsOriginal() throws Exception {
        // Given: An original entry
        ConsumerRuleFilterEntry original = new ConsumerRuleFilterEntry("com.example", "library");

        // When: Serializing and deserializing
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(original);
        oos.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        ConsumerRuleFilterEntry deserialized = (ConsumerRuleFilterEntry) ois.readObject();
        ois.close();

        // Then: Deserialized should equal original
        assertEquals(original, deserialized, "Deserialized object should equal original");
        assertEquals(original.getGroup(), deserialized.getGroup(), "Group should be preserved");
        assertEquals(original.getModule(), deserialized.getModule(), "Module should be preserved");
        assertEquals(original.hashCode(), deserialized.hashCode(), "Hash code should match");
    }

    /**
     * Test serialization preserves all fields.
     * All properties should be preserved through serialization.
     */
    @Test
    public void testSerialization_preservesAllFields() throws Exception {
        // Given: An entry with specific values
        ConsumerRuleFilterEntry original = new ConsumerRuleFilterEntry(
            "androidx.window", "window-ktx");

        // When: Serializing and deserializing
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(original);
        oos.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        ConsumerRuleFilterEntry deserialized = (ConsumerRuleFilterEntry) ois.readObject();
        ois.close();

        // Then: All fields should be preserved
        assertEquals("androidx.window", deserialized.getGroup(),
            "Group should be preserved through serialization");
        assertEquals("window-ktx", deserialized.getModule(),
            "Module should be preserved through serialization");
    }

    /**
     * Test serialization with empty strings.
     * Empty strings should be preserved through serialization.
     */
    @Test
    public void testSerialization_emptyStrings() throws Exception {
        // Given: An entry with empty strings
        ConsumerRuleFilterEntry original = new ConsumerRuleFilterEntry("", "");

        // When: Serializing and deserializing
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(original);
        oos.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        ConsumerRuleFilterEntry deserialized = (ConsumerRuleFilterEntry) ois.readObject();
        ois.close();

        // Then: Empty strings should be preserved
        assertEquals("", deserialized.getGroup(), "Empty group should be preserved");
        assertEquals("", deserialized.getModule(), "Empty module should be preserved");
        assertEquals(original, deserialized, "Deserialized should equal original");
    }

    // ==================== Integration Tests ====================

    /**
     * Test entry can be used in list contains operation.
     * ConsumerRuleFilterEntry should work correctly with List.contains().
     */
    @Test
    public void testIntegration_listContains() {
        // Given: A list with an entry
        ConsumerRuleFilterEntry entry1 = new ConsumerRuleFilterEntry("com.example", "library");
        ConsumerRuleFilterEntry entry2 = new ConsumerRuleFilterEntry("org.test", "module");
        java.util.List<ConsumerRuleFilterEntry> list = java.util.Arrays.asList(entry1, entry2);

        // When: Checking if list contains entries
        ConsumerRuleFilterEntry sameAsEntry1 = new ConsumerRuleFilterEntry("com.example", "library");
        ConsumerRuleFilterEntry different = new ConsumerRuleFilterEntry("other", "lib");

        // Then: Should find equal entries
        assertTrue(list.contains(entry1), "List should contain entry1");
        assertTrue(list.contains(sameAsEntry1), "List should contain equal entry");
        assertFalse(list.contains(different), "List should not contain different entry");
    }

    /**
     * Test entry can be used as HashMap key.
     * ConsumerRuleFilterEntry should work correctly as a HashMap key.
     */
    @Test
    public void testIntegration_hashMapKey() {
        // Given: A map with entries as keys
        java.util.Map<ConsumerRuleFilterEntry, String> map = new java.util.HashMap<>();
        ConsumerRuleFilterEntry entry1 = new ConsumerRuleFilterEntry("com.example", "library");
        ConsumerRuleFilterEntry entry2 = new ConsumerRuleFilterEntry("org.test", "module");

        // When: Using entries as keys
        map.put(entry1, "value1");
        map.put(entry2, "value2");

        // Then: Should be able to retrieve values
        assertEquals("value1", map.get(entry1), "Should retrieve value by original key");

        ConsumerRuleFilterEntry sameAsEntry1 = new ConsumerRuleFilterEntry("com.example", "library");
        assertEquals("value1", map.get(sameAsEntry1), "Should retrieve value by equal key");

        ConsumerRuleFilterEntry different = new ConsumerRuleFilterEntry("other", "lib");
        assertNull(map.get(different), "Should not find value for different key");
    }

    /**
     * Test entry can be used in HashSet.
     * ConsumerRuleFilterEntry should work correctly in a HashSet.
     */
    @Test
    public void testIntegration_hashSet() {
        // Given: A HashSet with entries
        java.util.Set<ConsumerRuleFilterEntry> set = new java.util.HashSet<>();
        ConsumerRuleFilterEntry entry1 = new ConsumerRuleFilterEntry("com.example", "library");
        ConsumerRuleFilterEntry entry2 = new ConsumerRuleFilterEntry("org.test", "module");

        // When: Adding entries to set
        set.add(entry1);
        set.add(entry2);

        // Then: Set should contain entries and handle duplicates correctly
        assertEquals(2, set.size(), "Set should contain 2 entries");
        assertTrue(set.contains(entry1), "Set should contain entry1");

        ConsumerRuleFilterEntry duplicate = new ConsumerRuleFilterEntry("com.example", "library");
        set.add(duplicate);
        assertEquals(2, set.size(), "Set should not add duplicate entry");
        assertTrue(set.contains(duplicate), "Set should contain equal entry");
    }

    /**
     * Test real-world usage scenario from CollectConsumerRulesTask.
     * This tests the pattern used in the actual task implementation.
     */
    @Test
    public void testIntegration_realWorldUsage() {
        // Given: The scenario from CollectConsumerRulesTask
        ConsumerRuleFilterEntry ignoreEntry = new ConsumerRuleFilterEntry("androidx.window", "window");
        java.util.List<ConsumerRuleFilterEntry> ignoreFilter =
            java.util.Collections.singletonList(ignoreEntry);

        // When: Creating an entry from artifact info
        ConsumerRuleFilterEntry artifactEntry = new ConsumerRuleFilterEntry("androidx.window", "window");

        // Then: Should be found in the filter (as used in line 48 of CollectConsumerRulesTask)
        assertTrue(ignoreFilter.contains(artifactEntry),
            "Should find matching entry in filter");
    }

    /**
     * Test multiple entries with variations.
     * Ensure the class works correctly with various combinations.
     */
    @Test
    public void testIntegration_multipleEntriesVariations() {
        // Given: Various entries
        ConsumerRuleFilterEntry entry1 = new ConsumerRuleFilterEntry("com.example", "library");
        ConsumerRuleFilterEntry entry2 = new ConsumerRuleFilterEntry("com.example", "library-ktx");
        ConsumerRuleFilterEntry entry3 = new ConsumerRuleFilterEntry("com.other", "library");

        // When: Creating a list and checking contains
        java.util.List<ConsumerRuleFilterEntry> list =
            java.util.Arrays.asList(entry1, entry2, entry3);

        // Then: Should find correct entries
        assertTrue(list.contains(new ConsumerRuleFilterEntry("com.example", "library")));
        assertTrue(list.contains(new ConsumerRuleFilterEntry("com.example", "library-ktx")));
        assertTrue(list.contains(new ConsumerRuleFilterEntry("com.other", "library")));
        assertFalse(list.contains(new ConsumerRuleFilterEntry("com.example", "other")));
        assertFalse(list.contains(new ConsumerRuleFilterEntry("com.different", "library")));
    }
}
