package proguard.obfuscate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link NumericNameFactory#reset()} method.
 */
public class NumericNameFactoryClaude_resetTest {

    /**
     * Tests that reset returns the factory to its initial state.
     * Verifies that after reset, the factory starts generating from "1" again.
     */
    @Test
    public void testResetReturnsToInitialState() {
        // Arrange - Create factory and advance it
        NumericNameFactory factory = new NumericNameFactory();
        factory.nextName(); // "1"
        factory.nextName(); // "2"
        factory.nextName(); // "3"

        // Act - Reset the factory
        factory.reset();

        // Assert - Should start from "1" again
        assertEquals("1", factory.nextName(),
                "After reset, factory should start generating from '1'");
        assertEquals("2", factory.nextName(),
                "After reset, second name should be '2'");
    }

    /**
     * Tests that reset works correctly after generating many names.
     * Verifies that reset works regardless of how far the sequence has advanced.
     */
    @Test
    public void testResetAfterManyNames() {
        // Arrange - Create factory and advance it significantly
        NumericNameFactory factory = new NumericNameFactory();
        for (int i = 1; i <= 100; i++) {
            factory.nextName();
        }
        String lastNameBeforeReset = factory.nextName(); // "101"

        // Act - Reset the factory
        factory.reset();

        // Assert - Should start from "1" again
        assertEquals("1", factory.nextName(),
                "After generating 101 names and resetting, should start from '1'");
        assertNotEquals(lastNameBeforeReset, factory.nextName(),
                "After reset, should not continue from previous sequence");
    }

    /**
     * Tests that reset can be called multiple times consecutively.
     * Verifies that multiple consecutive resets don't cause any issues.
     */
    @Test
    public void testMultipleConsecutiveResets() {
        // Arrange - Create factory and advance it
        NumericNameFactory factory = new NumericNameFactory();
        factory.nextName(); // "1"
        factory.nextName(); // "2"

        // Act - Call reset multiple times
        factory.reset();
        factory.reset();
        factory.reset();

        // Assert - Should still work correctly
        assertEquals("1", factory.nextName(),
                "After multiple consecutive resets, should start from '1'");
        assertEquals("2", factory.nextName(),
                "After multiple consecutive resets, second name should be '2'");
    }

    /**
     * Tests that reset works without consuming any names first.
     * Verifies that reset can be called on a newly constructed factory.
     */
    @Test
    public void testResetWithoutConsuming() {
        // Arrange - Create a fresh factory
        NumericNameFactory factory = new NumericNameFactory();

        // Act - Reset immediately without calling nextName
        factory.reset();

        // Assert - Should work normally
        assertEquals("1", factory.nextName(),
                "After reset on new factory, should generate '1'");
        assertEquals("2", factory.nextName(),
                "After reset on new factory, second name should be '2'");
    }

    /**
     * Tests that reset at different positions always returns to the same initial state.
     * Verifies that reset behavior is consistent regardless of current position.
     */
    @Test
    public void testResetAtDifferentPositions() {
        NumericNameFactory factory = new NumericNameFactory();

        // Reset at position 1
        assertEquals("1", factory.nextName());
        factory.reset();
        String afterReset1 = factory.nextName();

        // Reset at position 5
        factory.nextName(); // "2"
        factory.nextName(); // "3"
        factory.nextName(); // "4"
        factory.nextName(); // "5"
        factory.reset();
        String afterReset5 = factory.nextName();

        // Reset at position 50
        for (int i = 0; i < 49; i++) {
            factory.nextName();
        }
        factory.reset();
        String afterReset50 = factory.nextName();

        // Assert - All resets should return to "1"
        assertEquals("1", afterReset1, "Reset at position 1 should return to '1'");
        assertEquals("1", afterReset5, "Reset at position 5 should return to '1'");
        assertEquals("1", afterReset50, "Reset at position 50 should return to '1'");
        assertEquals(afterReset1, afterReset5, "All resets should produce same result");
        assertEquals(afterReset1, afterReset50, "All resets should produce same result");
    }

    /**
     * Tests that reset preserves the sequence pattern.
     * Verifies that after reset, the numeric sequence is identical to the original.
     */
    @Test
    public void testResetPreservesSequencePattern() {
        NumericNameFactory factory = new NumericNameFactory();

        // First iteration - collect sequence
        String[] firstIteration = new String[5];
        for (int i = 0; i < 5; i++) {
            firstIteration[i] = factory.nextName();
        }

        // Reset
        factory.reset();

        // Second iteration - should be identical
        String[] secondIteration = new String[5];
        for (int i = 0; i < 5; i++) {
            secondIteration[i] = factory.nextName();
        }

        // Assert - Both iterations should be identical
        assertArrayEquals(firstIteration, secondIteration,
                "Sequence after reset should be identical to original sequence");
    }

    /**
     * Tests that reset works correctly with multiple factory instances.
     * Verifies that resetting one instance doesn't affect another.
     */
    @Test
    public void testResetDoesNotAffectOtherInstances() {
        // Arrange - Create two independent factories
        NumericNameFactory factory1 = new NumericNameFactory();
        NumericNameFactory factory2 = new NumericNameFactory();

        // Advance both factories
        factory1.nextName(); // "1"
        factory1.nextName(); // "2"
        factory2.nextName(); // "1"
        factory2.nextName(); // "2"
        factory2.nextName(); // "3"

        // Act - Reset only factory1
        factory1.reset();

        // Assert - factory1 should be reset, factory2 should be unaffected
        assertEquals("1", factory1.nextName(), "Factory1 should be reset to '1'");
        assertEquals("4", factory2.nextName(), "Factory2 should continue at '4'");
    }

    /**
     * Tests that reset can be called after each name generation.
     * Verifies that reset works correctly in rapid succession with consumption.
     */
    @Test
    public void testResetAfterEachGeneration() {
        NumericNameFactory factory = new NumericNameFactory();

        // Call nextName and reset multiple times
        for (int i = 0; i < 5; i++) {
            String name = factory.nextName();
            assertEquals("1", name, "Each generation after reset should be '1'");
            factory.reset();
        }

        // Final check
        assertEquals("1", factory.nextName(), "Final generation should still be '1'");
    }

    /**
     * Tests reset behavior in a realistic usage pattern.
     * Verifies that reset works correctly when used to generate the same sequence multiple times.
     */
    @Test
    public void testResetInRealisticPattern() {
        NumericNameFactory factory = new NumericNameFactory();

        // Simulate processing multiple classes, resetting between each
        for (int classNum = 0; classNum < 3; classNum++) {
            // Generate some names for this class
            assertEquals("1", factory.nextName());
            assertEquals("2", factory.nextName());
            assertEquals("3", factory.nextName());

            // Reset for next class
            factory.reset();
        }

        // Verify factory still works correctly
        assertEquals("1", factory.nextName());
    }

    /**
     * Tests that reset doesn't throw any exceptions.
     * Verifies that reset is a safe operation that never fails.
     */
    @Test
    public void testResetDoesNotThrow() {
        NumericNameFactory factory = new NumericNameFactory();

        // Test reset in various states
        assertDoesNotThrow(() -> factory.reset(),
                "Reset on new factory should not throw");

        factory.nextName();
        assertDoesNotThrow(() -> factory.reset(),
                "Reset after one name should not throw");

        for (int i = 0; i < 100; i++) {
            factory.nextName();
        }
        assertDoesNotThrow(() -> factory.reset(),
                "Reset after many names should not throw");
    }

    /**
     * Tests that reset followed by multiple name generations works correctly.
     * Verifies the complete sequence after reset matches expected values.
     */
    @Test
    public void testResetFollowedByLongSequence() {
        NumericNameFactory factory = new NumericNameFactory();

        // Advance and reset
        for (int i = 0; i < 50; i++) {
            factory.nextName();
        }
        factory.reset();

        // Generate a longer sequence
        for (int i = 1; i <= 20; i++) {
            assertEquals(String.valueOf(i), factory.nextName(),
                    "Name " + i + " should match expected value");
        }
    }

    /**
     * Tests reset with edge case of very large numbers.
     * Verifies that reset works even after generating very large numeric names.
     */
    @Test
    public void testResetAfterLargeNumbers() {
        NumericNameFactory factory = new NumericNameFactory();

        // Generate up to 10000
        for (int i = 1; i <= 10000; i++) {
            factory.nextName();
        }

        String lastBefore = factory.nextName(); // "10001"
        assertEquals("10001", lastBefore);

        // Act - Reset
        factory.reset();

        // Assert - Should return to "1"
        assertEquals("1", factory.nextName(),
                "After generating 10001 names, reset should return to '1'");
    }

    /**
     * Tests that reset is idempotent when called multiple times without name generation.
     * Verifies that reset can be called repeatedly without changing the result.
     */
    @Test
    public void testResetIdempotency() {
        NumericNameFactory factory = new NumericNameFactory();
        factory.nextName(); // "1"

        // Call reset many times
        for (int i = 0; i < 10; i++) {
            factory.reset();
        }

        // Should still produce "1" as first name
        assertEquals("1", factory.nextName(),
                "Multiple resets should be idempotent");
    }

    /**
     * Tests reset with interleaved name generation.
     * Verifies that reset can be called at any point in the sequence.
     */
    @Test
    public void testResetWithInterleavedGeneration() {
        NumericNameFactory factory = new NumericNameFactory();

        assertEquals("1", factory.nextName());
        factory.reset();

        assertEquals("1", factory.nextName());
        assertEquals("2", factory.nextName());
        factory.reset();

        assertEquals("1", factory.nextName());
        assertEquals("2", factory.nextName());
        assertEquals("3", factory.nextName());
        factory.reset();

        assertEquals("1", factory.nextName());
    }

    /**
     * Tests that the NameFactory interface reset method is properly implemented.
     * Verifies that reset works when called through the interface reference.
     */
    @Test
    public void testResetThroughInterfaceReference() {
        // Use interface reference instead of concrete class
        NameFactory factory = new NumericNameFactory();

        factory.nextName(); // "1"
        factory.nextName(); // "2"
        factory.reset();

        assertEquals("1", factory.nextName(),
                "Reset through interface reference should work correctly");
    }

    /**
     * Tests reset consistency across many iterations.
     * Verifies that reset behavior is stable over many cycles.
     */
    @Test
    public void testResetConsistencyAcrossManyIterations() {
        NumericNameFactory factory = new NumericNameFactory();

        // Perform many reset cycles
        for (int iteration = 0; iteration < 100; iteration++) {
            assertEquals("1", factory.nextName(),
                    "Iteration " + iteration + " should start with '1' after reset");
            assertEquals("2", factory.nextName(),
                    "Iteration " + iteration + " should have '2' as second name");
            assertEquals("3", factory.nextName(),
                    "Iteration " + iteration + " should have '3' as third name");
            factory.reset();
        }

        // Final verification
        assertEquals("1", factory.nextName(),
                "After 100 iterations, reset should still work correctly");
    }

    /**
     * Tests that reset doesn't affect the type of names generated.
     * Verifies that names after reset are still numeric strings.
     */
    @Test
    public void testResetPreservesNameType() {
        NumericNameFactory factory = new NumericNameFactory();

        // Generate some names
        factory.nextName();
        factory.nextName();

        // Reset
        factory.reset();

        // Verify names are still valid numeric strings
        String name1 = factory.nextName();
        String name2 = factory.nextName();

        assertDoesNotThrow(() -> Integer.parseInt(name1),
                "Name after reset should be a valid integer");
        assertDoesNotThrow(() -> Integer.parseInt(name2),
                "Name after reset should be a valid integer");
        assertEquals(1, Integer.parseInt(name1),
                "First name after reset should be numeric value 1");
        assertEquals(2, Integer.parseInt(name2),
                "Second name after reset should be numeric value 2");
    }

    /**
     * Tests reset with single name generation between resets.
     * Verifies that reset works correctly with minimal name generation.
     */
    @Test
    public void testResetWithSingleNameGeneration() {
        NumericNameFactory factory = new NumericNameFactory();

        // Generate one name, reset, repeat
        assertEquals("1", factory.nextName());
        factory.reset();

        assertEquals("1", factory.nextName());
        factory.reset();

        assertEquals("1", factory.nextName());
        factory.reset();

        assertEquals("1", factory.nextName());
    }

    /**
     * Tests that reset creates a clean state equivalent to a new instance.
     * Verifies that a reset factory behaves identically to a newly constructed one.
     */
    @Test
    public void testResetCreatesCleanState() {
        NumericNameFactory oldFactory = new NumericNameFactory();

        // Use the old factory extensively
        for (int i = 0; i < 1000; i++) {
            oldFactory.nextName();
        }

        // Reset it
        oldFactory.reset();

        // Create a new factory
        NumericNameFactory newFactory = new NumericNameFactory();

        // Compare next 10 names
        for (int i = 0; i < 10; i++) {
            assertEquals(newFactory.nextName(), oldFactory.nextName(),
                    "Reset factory should produce same names as new factory");
        }
    }
}
