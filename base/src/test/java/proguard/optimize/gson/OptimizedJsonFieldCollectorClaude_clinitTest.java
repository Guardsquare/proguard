package proguard.optimize.gson;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link OptimizedJsonFieldCollector.Mode} enum static initialization.
 * Tests the <clinit> method to ensure the Mode enum is properly initialized.
 *
 * The <clinit> method is the static initializer for the Mode enum, which creates
 * the enum constants serialize and deserialize.
 */
public class OptimizedJsonFieldCollectorClaude_clinitTest {

    // =========================================================================
    // Tests for Mode.<clinit>.()V (static initialization)
    // =========================================================================

    /**
     * Tests that accessing Mode.serialize triggers static initialization and returns a non-null value.
     * This covers the enum declaration and the serialize constant initialization.
     */
    @Test
    public void testModeStaticInit_accessSerialize_returnsNonNull() {
        // Act
        OptimizedJsonFieldCollector.Mode serialize = OptimizedJsonFieldCollector.Mode.serialize;

        // Assert
        assertNotNull(serialize, "Mode.serialize should be initialized and non-null");
    }

    /**
     * Tests that accessing Mode.deserialize triggers static initialization and returns a non-null value.
     * This covers the deserialize constant initialization.
     */
    @Test
    public void testModeStaticInit_accessDeserialize_returnsNonNull() {
        // Act
        OptimizedJsonFieldCollector.Mode deserialize = OptimizedJsonFieldCollector.Mode.deserialize;

        // Assert
        assertNotNull(deserialize, "Mode.deserialize should be initialized and non-null");
    }

    /**
     * Tests that both enum constants are properly initialized and distinct.
     * This verifies that the static initializer created both constants correctly.
     */
    @Test
    public void testModeStaticInit_bothConstantsInitialized_areDistinct() {
        // Act
        OptimizedJsonFieldCollector.Mode serialize = OptimizedJsonFieldCollector.Mode.serialize;
        OptimizedJsonFieldCollector.Mode deserialize = OptimizedJsonFieldCollector.Mode.deserialize;

        // Assert
        assertNotNull(serialize, "serialize should be initialized");
        assertNotNull(deserialize, "deserialize should be initialized");
        assertNotSame(serialize, deserialize, "serialize and deserialize should be distinct instances");
    }

    /**
     * Tests that the enum constants have the correct names after static initialization.
     */
    @Test
    public void testModeStaticInit_enumConstants_haveCorrectNames() {
        // Act
        OptimizedJsonFieldCollector.Mode serialize = OptimizedJsonFieldCollector.Mode.serialize;
        OptimizedJsonFieldCollector.Mode deserialize = OptimizedJsonFieldCollector.Mode.deserialize;

        // Assert
        assertEquals("serialize", serialize.name(), "serialize constant should have name 'serialize'");
        assertEquals("deserialize", deserialize.name(), "deserialize constant should have name 'deserialize'");
    }

    /**
     * Tests that the enum constants are singletons after static initialization.
     * Repeated access should return the same instance.
     */
    @Test
    public void testModeStaticInit_enumConstants_areSingletons() {
        // Act
        OptimizedJsonFieldCollector.Mode serialize1 = OptimizedJsonFieldCollector.Mode.serialize;
        OptimizedJsonFieldCollector.Mode serialize2 = OptimizedJsonFieldCollector.Mode.serialize;
        OptimizedJsonFieldCollector.Mode deserialize1 = OptimizedJsonFieldCollector.Mode.deserialize;
        OptimizedJsonFieldCollector.Mode deserialize2 = OptimizedJsonFieldCollector.Mode.deserialize;

        // Assert
        assertSame(serialize1, serialize2, "Multiple accesses to serialize should return the same instance");
        assertSame(deserialize1, deserialize2, "Multiple accesses to deserialize should return the same instance");
    }

    /**
     * Tests that Mode.values() works after static initialization.
     * This verifies that the synthetic values array is properly created by <clinit>.
     */
    @Test
    public void testModeStaticInit_valuesMethod_returnsInitializedArray() {
        // Act
        OptimizedJsonFieldCollector.Mode[] modes = OptimizedJsonFieldCollector.Mode.values();

        // Assert
        assertNotNull(modes, "values() should return a non-null array after static initialization");
        assertEquals(2, modes.length, "values() should return array with 2 elements");
        assertSame(OptimizedJsonFieldCollector.Mode.serialize, modes[0], "First element should be serialize");
        assertSame(OptimizedJsonFieldCollector.Mode.deserialize, modes[1], "Second element should be deserialize");
    }

    /**
     * Tests that enum constants have correct ordinal values after static initialization.
     */
    @Test
    public void testModeStaticInit_enumConstants_haveCorrectOrdinals() {
        // Act
        OptimizedJsonFieldCollector.Mode serialize = OptimizedJsonFieldCollector.Mode.serialize;
        OptimizedJsonFieldCollector.Mode deserialize = OptimizedJsonFieldCollector.Mode.deserialize;

        // Assert
        assertEquals(0, serialize.ordinal(), "serialize should have ordinal 0");
        assertEquals(1, deserialize.ordinal(), "deserialize should have ordinal 1");
    }

    /**
     * Tests that enum constants can be used in comparisons after static initialization.
     */
    @Test
    public void testModeStaticInit_enumConstants_canBeCompared() {
        // Act
        OptimizedJsonFieldCollector.Mode serialize = OptimizedJsonFieldCollector.Mode.serialize;
        OptimizedJsonFieldCollector.Mode deserialize = OptimizedJsonFieldCollector.Mode.deserialize;

        // Assert
        assertTrue(serialize.compareTo(deserialize) < 0, "serialize should compare less than deserialize");
        assertTrue(deserialize.compareTo(serialize) > 0, "deserialize should compare greater than serialize");
        assertEquals(0, serialize.compareTo(serialize), "serialize should equal itself in comparison");
    }

    /**
     * Tests that enum constants can be used in equality checks after static initialization.
     */
    @Test
    public void testModeStaticInit_enumConstants_supportEquality() {
        // Act
        OptimizedJsonFieldCollector.Mode serialize = OptimizedJsonFieldCollector.Mode.serialize;
        OptimizedJsonFieldCollector.Mode deserialize = OptimizedJsonFieldCollector.Mode.deserialize;

        // Assert
        assertEquals(serialize, serialize, "serialize should equal itself");
        assertEquals(deserialize, deserialize, "deserialize should equal itself");
        assertNotEquals(serialize, deserialize, "serialize should not equal deserialize");
        assertNotEquals(deserialize, serialize, "deserialize should not equal serialize");
    }

    /**
     * Tests that toString() works correctly after static initialization.
     */
    @Test
    public void testModeStaticInit_enumConstants_toStringWorks() {
        // Act
        String serializeString = OptimizedJsonFieldCollector.Mode.serialize.toString();
        String deserializeString = OptimizedJsonFieldCollector.Mode.deserialize.toString();

        // Assert
        assertEquals("serialize", serializeString, "serialize.toString() should return 'serialize'");
        assertEquals("deserialize", deserializeString, "deserialize.toString() should return 'deserialize'");
    }

    /**
     * Tests that the enum can be used in switch statements after static initialization.
     * This verifies the enum is fully functional.
     */
    @Test
    public void testModeStaticInit_enumConstants_canBeUsedInSwitch() {
        // Act
        String serializeResult = getSwitchResult(OptimizedJsonFieldCollector.Mode.serialize);
        String deserializeResult = getSwitchResult(OptimizedJsonFieldCollector.Mode.deserialize);

        // Assert
        assertEquals("SERIALIZE", serializeResult, "serialize should work in switch statement");
        assertEquals("DESERIALIZE", deserializeResult, "deserialize should work in switch statement");
    }

    /**
     * Tests that Mode.valueOf() works correctly after static initialization.
     */
    @Test
    public void testModeStaticInit_valueOfMethod_worksCorrectly() {
        // Act
        OptimizedJsonFieldCollector.Mode serializeFromValueOf =
            OptimizedJsonFieldCollector.Mode.valueOf("serialize");
        OptimizedJsonFieldCollector.Mode deserializeFromValueOf =
            OptimizedJsonFieldCollector.Mode.valueOf("deserialize");

        // Assert
        assertSame(OptimizedJsonFieldCollector.Mode.serialize, serializeFromValueOf,
                "valueOf('serialize') should return the serialize constant");
        assertSame(OptimizedJsonFieldCollector.Mode.deserialize, deserializeFromValueOf,
                "valueOf('deserialize') should return the deserialize constant");
    }

    /**
     * Tests that the enum class itself is accessible after static initialization.
     */
    @Test
    public void testModeStaticInit_enumClass_isAccessible() {
        // Act
        Class<OptimizedJsonFieldCollector.Mode> modeClass = OptimizedJsonFieldCollector.Mode.class;

        // Assert
        assertNotNull(modeClass, "Mode.class should be accessible");
        assertTrue(modeClass.isEnum(), "Mode class should be an enum");
        assertEquals("Mode", modeClass.getSimpleName(), "Enum should have correct simple name");
    }

    /**
     * Tests that enum constants can be retrieved via reflection after static initialization.
     */
    @Test
    public void testModeStaticInit_enumConstants_accessibleViaReflection() {
        // Act
        OptimizedJsonFieldCollector.Mode[] constants =
            OptimizedJsonFieldCollector.Mode.class.getEnumConstants();

        // Assert
        assertNotNull(constants, "getEnumConstants() should return non-null array");
        assertEquals(2, constants.length, "Should have 2 enum constants");
        assertSame(OptimizedJsonFieldCollector.Mode.serialize, constants[0]);
        assertSame(OptimizedJsonFieldCollector.Mode.deserialize, constants[1]);
    }

    /**
     * Tests that the enum can be used as a constructor parameter after static initialization.
     * This verifies integration with OptimizedJsonFieldCollector.
     */
    @Test
    public void testModeStaticInit_enumConstants_canBeUsedInConstructor() {
        // Arrange
        OptimizedJsonInfo info = new OptimizedJsonInfo();

        // Act & Assert
        assertDoesNotThrow(() -> {
            new OptimizedJsonFieldCollector(info, OptimizedJsonFieldCollector.Mode.serialize);
            new OptimizedJsonFieldCollector(info, OptimizedJsonFieldCollector.Mode.deserialize);
        }, "Enum constants should be usable in OptimizedJsonFieldCollector constructor");
    }

    // =========================================================================
    // Helper methods
    // =========================================================================

    /**
     * Helper method to test enum in switch statements.
     */
    private String getSwitchResult(OptimizedJsonFieldCollector.Mode mode) {
        switch (mode) {
            case serialize:
                return "SERIALIZE";
            case deserialize:
                return "DESERIALIZE";
            default:
                return "UNKNOWN";
        }
    }
}
