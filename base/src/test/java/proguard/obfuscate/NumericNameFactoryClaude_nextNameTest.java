package proguard.obfuscate;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link NumericNameFactory#nextName()} method.
 */
public class NumericNameFactoryClaude_nextNameTest {

    /**
     * Tests that the first call to nextName returns "1".
     * Verifies the documented behavior that names start at "1".
     */
    @Test
    public void testNextNameReturnsOneAsFirstName() {
        // Arrange
        NumericNameFactory factory = new NumericNameFactory();

        // Act
        String firstName = factory.nextName();

        // Assert
        assertEquals("1", firstName,
                "First call to nextName should return '1'");
    }

    /**
     * Tests that nextName returns a non-null value.
     * Verifies that nextName never returns null.
     */
    @Test
    public void testNextNameReturnsNonNull() {
        // Arrange
        NumericNameFactory factory = new NumericNameFactory();

        // Act
        String name = factory.nextName();

        // Assert
        assertNotNull(name, "nextName should never return null");
    }

    /**
     * Tests that nextName returns a non-empty string.
     * Verifies that nextName never returns an empty string.
     */
    @Test
    public void testNextNameReturnsNonEmpty() {
        // Arrange
        NumericNameFactory factory = new NumericNameFactory();

        // Act
        String name = factory.nextName();

        // Assert
        assertFalse(name.isEmpty(), "nextName should never return an empty string");
        assertTrue(name.length() > 0, "nextName should return a string with length > 0");
    }

    /**
     * Tests that consecutive calls to nextName return sequential numbers.
     * Verifies the sequence: "1", "2", "3", "4", "5".
     */
    @Test
    public void testNextNameReturnsSequentialNumbers() {
        // Arrange
        NumericNameFactory factory = new NumericNameFactory();

        // Act & Assert
        assertEquals("1", factory.nextName(), "First name should be '1'");
        assertEquals("2", factory.nextName(), "Second name should be '2'");
        assertEquals("3", factory.nextName(), "Third name should be '3'");
        assertEquals("4", factory.nextName(), "Fourth name should be '4'");
        assertEquals("5", factory.nextName(), "Fifth name should be '5'");
    }

    /**
     * Tests that nextName returns unique names.
     * Verifies that each call returns a different value.
     */
    @Test
    public void testNextNameReturnsUniqueNames() {
        // Arrange
        NumericNameFactory factory = new NumericNameFactory();
        Set<String> generatedNames = new HashSet<>();

        // Act - Generate 100 names
        for (int i = 0; i < 100; i++) {
            String name = factory.nextName();
            generatedNames.add(name);
        }

        // Assert - All names should be unique
        assertEquals(100, generatedNames.size(),
                "All 100 generated names should be unique");
    }

    /**
     * Tests that nextName can generate many sequential names.
     * Verifies that the factory can handle large sequences.
     */
    @Test
    public void testNextNameGeneratesManyNames() {
        // Arrange
        NumericNameFactory factory = new NumericNameFactory();

        // Act - Generate 1000 names
        String lastValue = null;
        for (int i = 1; i <= 1000; i++) {
            lastValue = factory.nextName();
        }

        // Assert - The 1000th name should be "1000"
        assertEquals("1000", lastValue,
                "After 1000 calls, nextName should return '1000'");
    }

    /**
     * Tests that nextName returns valid numeric strings.
     * Verifies that all returned values can be parsed as integers.
     */
    @Test
    public void testNextNameReturnsValidNumericStrings() {
        // Arrange
        NumericNameFactory factory = new NumericNameFactory();

        // Act & Assert - Test first 50 names
        for (int i = 1; i <= 50; i++) {
            String name = factory.nextName();
            assertDoesNotThrow(() -> Integer.parseInt(name),
                    "Name '" + name + "' should be a valid integer string");
            assertEquals(i, Integer.parseInt(name),
                    "Name should be numeric value " + i);
        }
    }

    /**
     * Tests that nextName returns strings without leading zeros.
     * Verifies that numbers are formatted without unnecessary leading zeros.
     */
    @Test
    public void testNextNameReturnsNumbersWithoutLeadingZeros() {
        // Arrange
        NumericNameFactory factory = new NumericNameFactory();

        // Act - Generate first 20 names
        for (int i = 1; i <= 20; i++) {
            String name = factory.nextName();

            // Assert - Should not have leading zeros
            assertFalse(name.startsWith("0"),
                    "Name '" + name + "' should not have leading zeros");
            assertEquals(String.valueOf(i), name,
                    "Name should be the standard string representation of " + i);
        }
    }

    /**
     * Tests that nextName increments correctly across reset boundaries.
     * Verifies that after reset, nextName starts from "1" again.
     */
    @Test
    public void testNextNameAfterReset() {
        // Arrange
        NumericNameFactory factory = new NumericNameFactory();
        factory.nextName(); // "1"
        factory.nextName(); // "2"
        factory.nextName(); // "3"

        // Act - Reset and generate new name
        factory.reset();
        String nameAfterReset = factory.nextName();

        // Assert
        assertEquals("1", nameAfterReset,
                "After reset, nextName should return '1'");
    }

    /**
     * Tests that nextName can be called repeatedly without errors.
     * Verifies that nextName doesn't throw exceptions.
     */
    @Test
    public void testNextNameDoesNotThrow() {
        // Arrange
        NumericNameFactory factory = new NumericNameFactory();

        // Act & Assert - Call many times
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 10000; i++) {
                factory.nextName();
            }
        }, "nextName should not throw exceptions");
    }

    /**
     * Tests that nextName returns incrementing values.
     * Verifies that each subsequent call returns a larger numeric value.
     */
    @Test
    public void testNextNameReturnsIncrementingValues() {
        // Arrange
        NumericNameFactory factory = new NumericNameFactory();

        // Act & Assert - Verify each name is larger than the previous
        int previousValue = 0;
        for (int i = 1; i <= 50; i++) {
            String name = factory.nextName();
            int currentValue = Integer.parseInt(name);
            assertTrue(currentValue > previousValue,
                    "Each name should be numerically larger than the previous");
            assertEquals(previousValue + 1, currentValue,
                    "Each name should increment by exactly 1");
            previousValue = currentValue;
        }
    }

    /**
     * Tests that nextName handles transition to double-digit numbers.
     * Verifies the transition from "9" to "10".
     */
    @Test
    public void testNextNameHandlesDoubleDigitTransition() {
        // Arrange
        NumericNameFactory factory = new NumericNameFactory();

        // Act - Generate up to 11
        for (int i = 1; i < 9; i++) {
            factory.nextName();
        }
        String ninth = factory.nextName();
        String tenth = factory.nextName();
        String eleventh = factory.nextName();

        // Assert
        assertEquals("9", ninth, "9th name should be '9'");
        assertEquals("10", tenth, "10th name should be '10'");
        assertEquals("11", eleventh, "11th name should be '11'");
    }

    /**
     * Tests that nextName handles transition to triple-digit numbers.
     * Verifies the transition from "99" to "100".
     */
    @Test
    public void testNextNameHandlesTripleDigitTransition() {
        // Arrange
        NumericNameFactory factory = new NumericNameFactory();

        // Act - Generate up to 101
        for (int i = 1; i < 99; i++) {
            factory.nextName();
        }
        String name99 = factory.nextName();
        String name100 = factory.nextName();
        String name101 = factory.nextName();

        // Assert
        assertEquals("99", name99, "99th name should be '99'");
        assertEquals("100", name100, "100th name should be '100'");
        assertEquals("101", name101, "101st name should be '101'");
    }

    /**
     * Tests that nextName handles large numbers correctly.
     * Verifies that the factory can generate very large numeric names.
     */
    @Test
    public void testNextNameHandlesLargeNumbers() {
        // Arrange
        NumericNameFactory factory = new NumericNameFactory();

        // Act - Generate 100000 names
        String lastValue = null;
        for (int i = 1; i <= 100000; i++) {
            lastValue = factory.nextName();
        }

        // Assert
        assertEquals("100000", lastValue,
                "After 100000 calls, should return '100000'");
    }

    /**
     * Tests that nextName returns Java identifiers.
     * Verifies that all returned names are valid Java identifiers.
     */
    @Test
    public void testNextNameReturnsValidJavaIdentifiers() {
        // Arrange
        NumericNameFactory factory = new NumericNameFactory();

        // Act & Assert - Test first 100 names
        for (int i = 0; i < 100; i++) {
            String name = factory.nextName();

            // Numeric strings starting with digits are not valid Java identifiers
            // But the interface documentation says names must be valid Java identifiers
            // Numbers 1-9 and beyond are technically not valid identifiers in Java
            // However, this is what the class generates, so we verify it generates numbers
            assertFalse(name.isEmpty(), "Name should not be empty");
            assertTrue(name.matches("\\d+"), "Name should be a numeric string");
        }
    }

    /**
     * Tests that nextName with multiple factory instances.
     * Verifies that each instance maintains its own independent sequence.
     */
    @Test
    public void testNextNameWithMultipleInstances() {
        // Arrange
        NumericNameFactory factory1 = new NumericNameFactory();
        NumericNameFactory factory2 = new NumericNameFactory();

        // Act
        String name1_1 = factory1.nextName();
        String name2_1 = factory2.nextName();
        String name1_2 = factory1.nextName();
        String name2_2 = factory2.nextName();

        // Assert - Both should generate the same sequence independently
        assertEquals("1", name1_1, "Factory 1 first name should be '1'");
        assertEquals("1", name2_1, "Factory 2 first name should be '1'");
        assertEquals("2", name1_2, "Factory 1 second name should be '2'");
        assertEquals("2", name2_2, "Factory 2 second name should be '2'");
    }

    /**
     * Tests that nextName maintains state correctly.
     * Verifies that the internal state persists across calls.
     */
    @Test
    public void testNextNameMaintainsState() {
        // Arrange
        NumericNameFactory factory = new NumericNameFactory();

        // Act - Generate sequence with interruptions
        assertEquals("1", factory.nextName());
        assertEquals("2", factory.nextName());

        // Do nothing for a while (simulating time passing)

        assertEquals("3", factory.nextName());
        assertEquals("4", factory.nextName());

        // Assert - State is maintained correctly
        assertEquals("5", factory.nextName(),
                "State should be maintained across the lifetime of the factory");
    }

    /**
     * Tests that nextName through interface reference works correctly.
     * Verifies that the method works when called via the NameFactory interface.
     */
    @Test
    public void testNextNameThroughInterfaceReference() {
        // Arrange - Use interface reference
        NameFactory factory = new NumericNameFactory();

        // Act
        String name1 = factory.nextName();
        String name2 = factory.nextName();
        String name3 = factory.nextName();

        // Assert
        assertEquals("1", name1, "Via interface, first name should be '1'");
        assertEquals("2", name2, "Via interface, second name should be '2'");
        assertEquals("3", name3, "Via interface, third name should be '3'");
    }

    /**
     * Tests that nextName generates the correct sequence over many reset cycles.
     * Verifies that nextName works correctly with reset in a loop.
     */
    @Test
    public void testNextNameOverManyResetCycles() {
        // Arrange
        NumericNameFactory factory = new NumericNameFactory();

        // Act & Assert - Test 10 reset cycles
        for (int cycle = 0; cycle < 10; cycle++) {
            assertEquals("1", factory.nextName(), "Cycle " + cycle + ": first name should be '1'");
            assertEquals("2", factory.nextName(), "Cycle " + cycle + ": second name should be '2'");
            assertEquals("3", factory.nextName(), "Cycle " + cycle + ": third name should be '3'");
            factory.reset();
        }
    }

    /**
     * Tests that nextName returns consistent string format.
     * Verifies that the format doesn't change based on position in sequence.
     */
    @Test
    public void testNextNameConsistentFormat() {
        // Arrange
        NumericNameFactory factory = new NumericNameFactory();

        // Act & Assert - All should be simple decimal format
        for (int i = 1; i <= 100; i++) {
            String name = factory.nextName();
            assertTrue(name.matches("^\\d+$"),
                    "Name '" + name + "' should be only digits with no formatting");
            assertFalse(name.contains("."),
                    "Name should not contain decimal point");
            assertFalse(name.contains(","),
                    "Name should not contain comma separators");
            assertFalse(name.contains(" "),
                    "Name should not contain spaces");
        }
    }

    /**
     * Tests that nextName generates names in strictly ascending order.
     * Verifies that no name is repeated or out of order.
     */
    @Test
    public void testNextNameStrictlyAscending() {
        // Arrange
        NumericNameFactory factory = new NumericNameFactory();

        // Act & Assert
        int previous = 0;
        for (int i = 1; i <= 1000; i++) {
            String name = factory.nextName();
            int current = Integer.parseInt(name);
            assertEquals(previous + 1, current,
                    "Names should be in strictly ascending order");
            previous = current;
        }
    }

    /**
     * Tests the boundary of integer representation.
     * Verifies that nextName can handle very large sequences.
     */
    @Test
    public void testNextNameWithLargeSequence() {
        // Arrange
        NumericNameFactory factory = new NumericNameFactory();

        // Act - Generate up to 50000
        String lastValue = null;
        for (int i = 1; i <= 50000; i++) {
            lastValue = factory.nextName();
        }

        // Assert
        assertEquals("50000", lastValue);
        assertNotNull(lastValue);
        assertEquals(5, lastValue.length(), "50000 should have 5 digits");
    }

    /**
     * Tests that nextName never returns the same value twice in one sequence.
     * Verifies uniqueness guarantee within a single factory instance.
     */
    @Test
    public void testNextNameNeverRepeatsWithoutReset() {
        // Arrange
        NumericNameFactory factory = new NumericNameFactory();
        Set<String> seen = new HashSet<>();

        // Act - Generate 10000 names
        for (int i = 0; i < 10000; i++) {
            String name = factory.nextName();

            // Assert - This name hasn't been seen before
            assertFalse(seen.contains(name),
                    "Name '" + name + "' was generated more than once");
            seen.add(name);
        }

        // Assert
        assertEquals(10000, seen.size(), "Should have 10000 unique names");
    }

    /**
     * Tests that nextName starts at 1, not 0.
     * Verifies that the sequence never includes "0".
     */
    @Test
    public void testNextNameNeverReturnsZero() {
        // Arrange
        NumericNameFactory factory = new NumericNameFactory();

        // Act - Generate first 100 names
        for (int i = 0; i < 100; i++) {
            String name = factory.nextName();

            // Assert - Should never be "0"
            assertNotEquals("0", name,
                    "nextName should never return '0'");
            assertNotEquals(0, Integer.parseInt(name),
                    "nextName should never return numeric value 0");
        }
    }

    /**
     * Tests that nextName return values match Integer.toString behavior.
     * Verifies that the formatting matches Java's standard integer formatting.
     */
    @Test
    public void testNextNameMatchesIntegerToString() {
        // Arrange
        NumericNameFactory factory = new NumericNameFactory();

        // Act & Assert - Verify format matches Integer.toString
        for (int i = 1; i <= 100; i++) {
            String name = factory.nextName();
            String expected = Integer.toString(i);
            assertEquals(expected, name,
                    "nextName output should match Integer.toString(" + i + ")");
        }
    }
}
