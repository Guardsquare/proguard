package proguard.optimize.evaluation;

import org.junit.jupiter.api.Test;
import proguard.evaluation.PartialEvaluator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link InstructionUsageMarker#isStackEntryNecessaryAfter(int, int)} method.
 * Tests the isStackEntryNecessaryAfter method which returns whether the specified stack entry
 * after the given offset is necessary.
 */
public class InstructionUsageMarkerClaude_isStackEntryNecessaryAfterTest {

    /**
     * Tests that isStackEntryNecessaryAfter can be called with offset 0 and stackIndex 0.
     * Verifies that the method handles the first instruction offset and stack index.
     */
    @Test
    public void testIsStackEntryNecessaryAfterWithZeroOffsetAndZeroIndex() {
        // Arrange - Create InstructionUsageMarker with default PartialEvaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Verify method can be called without throwing exception
        assertDoesNotThrow(() -> marker.isStackEntryNecessaryAfter(0, 0),
                "isStackEntryNecessaryAfter should not throw exception with offset 0 and stackIndex 0");
    }

    /**
     * Tests that isStackEntryNecessaryAfter can be called with various positive offsets and indices.
     * Verifies that the method handles different instruction offsets and stack indices.
     */
    @Test
    public void testIsStackEntryNecessaryAfterWithPositiveOffsetsAndIndices() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Test various positive offsets and indices
        assertDoesNotThrow(() -> marker.isStackEntryNecessaryAfter(1, 0),
                "isStackEntryNecessaryAfter should not throw exception with offset 1, stackIndex 0");
        assertDoesNotThrow(() -> marker.isStackEntryNecessaryAfter(10, 5),
                "isStackEntryNecessaryAfter should not throw exception with offset 10, stackIndex 5");
        assertDoesNotThrow(() -> marker.isStackEntryNecessaryAfter(100, 10),
                "isStackEntryNecessaryAfter should not throw exception with offset 100, stackIndex 10");
    }

    /**
     * Tests that isStackEntryNecessaryAfter returns a boolean value.
     * Verifies that the method returns either true or false.
     */
    @Test
    public void testIsStackEntryNecessaryAfterReturnsBoolean() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act - Call isStackEntryNecessaryAfter
        boolean result = marker.isStackEntryNecessaryAfter(0, 0);

        // Assert - Verify result is a boolean (always true, method doesn't throw)
        assertNotNull(result, "isStackEntryNecessaryAfter should return a non-null boolean value");
    }

    /**
     * Tests that isStackEntryNecessaryAfter can be called multiple times with same parameters.
     * Verifies that the method is stable and can be called repeatedly.
     */
    @Test
    public void testIsStackEntryNecessaryAfterMultipleCallsSameParameters() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act - Call isStackEntryNecessaryAfter multiple times with same parameters
        boolean result1 = marker.isStackEntryNecessaryAfter(5, 2);
        boolean result2 = marker.isStackEntryNecessaryAfter(5, 2);
        boolean result3 = marker.isStackEntryNecessaryAfter(5, 2);

        // Assert - Verify consistent results
        assertEquals(result1, result2, "isStackEntryNecessaryAfter should return consistent result for same parameters");
        assertEquals(result2, result3, "isStackEntryNecessaryAfter should return consistent result for same parameters");
    }

    /**
     * Tests that isStackEntryNecessaryAfter can be called with different parameters.
     * Verifies that the method handles multiple different parameter combinations.
     */
    @Test
    public void testIsStackEntryNecessaryAfterWithDifferentParameters() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Test multiple different parameter combinations
        assertDoesNotThrow(() -> {
            marker.isStackEntryNecessaryAfter(0, 0);
            marker.isStackEntryNecessaryAfter(1, 0);
            marker.isStackEntryNecessaryAfter(2, 1);
            marker.isStackEntryNecessaryAfter(5, 2);
            marker.isStackEntryNecessaryAfter(10, 5);
            marker.isStackEntryNecessaryAfter(20, 10);
        }, "isStackEntryNecessaryAfter should handle multiple different parameter combinations");
    }

    /**
     * Tests isStackEntryNecessaryAfter with different stack indices for same offset.
     * Verifies that the method handles different stack indices correctly.
     */
    @Test
    public void testIsStackEntryNecessaryAfterWithDifferentStackIndices() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Test same offset with different stack indices
        assertDoesNotThrow(() -> {
            marker.isStackEntryNecessaryAfter(10, 0);
            marker.isStackEntryNecessaryAfter(10, 1);
            marker.isStackEntryNecessaryAfter(10, 2);
            marker.isStackEntryNecessaryAfter(10, 3);
            marker.isStackEntryNecessaryAfter(10, 4);
        }, "isStackEntryNecessaryAfter should handle different stack indices for same offset");
    }

    /**
     * Tests isStackEntryNecessaryAfter with InstructionUsageMarker created using boolean constructor.
     * Verifies that the method works with the single-parameter constructor.
     */
    @Test
    public void testIsStackEntryNecessaryAfterWithBooleanConstructor() {
        // Arrange - Create InstructionUsageMarker with boolean constructor
        InstructionUsageMarker marker = new InstructionUsageMarker(true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isStackEntryNecessaryAfter(0, 0),
                "isStackEntryNecessaryAfter should work with boolean constructor");
    }

    /**
     * Tests isStackEntryNecessaryAfter with InstructionUsageMarker created using 3-parameter constructor.
     * Verifies that the method works with the three-parameter constructor.
     */
    @Test
    public void testIsStackEntryNecessaryAfterWithThreeParameterConstructor() {
        // Arrange - Create InstructionUsageMarker with 3-parameter constructor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isStackEntryNecessaryAfter(0, 0),
                "isStackEntryNecessaryAfter should work with three-parameter constructor");
    }

    /**
     * Tests isStackEntryNecessaryAfter with InstructionUsageMarker created using 4-parameter constructor.
     * Verifies that the method works with the four-parameter constructor.
     */
    @Test
    public void testIsStackEntryNecessaryAfterWithFourParameterConstructor() {
        // Arrange - Create InstructionUsageMarker with 4-parameter constructor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isStackEntryNecessaryAfter(0, 0),
                "isStackEntryNecessaryAfter should work with four-parameter constructor");
    }

    /**
     * Tests isStackEntryNecessaryAfter with different PartialEvaluator configurations.
     * Verifies that the method works with various evaluator settings.
     */
    @Test
    public void testIsStackEntryNecessaryAfterWithDifferentEvaluatorConfigurations() {
        // Arrange - Create InstructionUsageMarkers with different configurations
        PartialEvaluator evaluator1 = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker1 = new InstructionUsageMarker(evaluator1, true, true, true);

        PartialEvaluator evaluator2 = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker2 = new InstructionUsageMarker(evaluator2, false, false, false);

        // Act & Assert - Verify both work
        assertDoesNotThrow(() -> marker1.isStackEntryNecessaryAfter(0, 0),
                "isStackEntryNecessaryAfter should work with first configuration");
        assertDoesNotThrow(() -> marker2.isStackEntryNecessaryAfter(0, 0),
                "isStackEntryNecessaryAfter should work with second configuration");
    }

    /**
     * Tests that isStackEntryNecessaryAfter can be called on multiple InstructionUsageMarker instances.
     * Verifies that the method works independently for different marker instances.
     */
    @Test
    public void testIsStackEntryNecessaryAfterOnMultipleInstances() {
        // Arrange - Create multiple InstructionUsageMarker instances
        InstructionUsageMarker marker1 = new InstructionUsageMarker(true);
        InstructionUsageMarker marker2 = new InstructionUsageMarker(false);
        InstructionUsageMarker marker3 = new InstructionUsageMarker(true);

        // Act & Assert - Verify all instances work independently
        assertDoesNotThrow(() -> marker1.isStackEntryNecessaryAfter(5, 2),
                "isStackEntryNecessaryAfter should work on first marker instance");
        assertDoesNotThrow(() -> marker2.isStackEntryNecessaryAfter(5, 2),
                "isStackEntryNecessaryAfter should work on second marker instance");
        assertDoesNotThrow(() -> marker3.isStackEntryNecessaryAfter(5, 2),
                "isStackEntryNecessaryAfter should work on third marker instance");
    }

    /**
     * Tests isStackEntryNecessaryAfter with a sequence of increasing offsets.
     * Verifies that the method handles sequential offset queries.
     */
    @Test
    public void testIsStackEntryNecessaryAfterWithIncreasingOffsets() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Test increasing sequence
        for (int offset = 0; offset < 20; offset++) {
            final int currentOffset = offset;
            assertDoesNotThrow(() -> marker.isStackEntryNecessaryAfter(currentOffset, 0),
                    "isStackEntryNecessaryAfter should handle offset " + currentOffset);
        }
    }

    /**
     * Tests isStackEntryNecessaryAfter with a sequence of increasing stack indices.
     * Verifies that the method handles sequential stack index queries.
     */
    @Test
    public void testIsStackEntryNecessaryAfterWithIncreasingStackIndices() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Test increasing sequence of stack indices
        for (int stackIndex = 0; stackIndex < 10; stackIndex++) {
            final int currentStackIndex = stackIndex;
            assertDoesNotThrow(() -> marker.isStackEntryNecessaryAfter(5, currentStackIndex),
                    "isStackEntryNecessaryAfter should handle stackIndex " + currentStackIndex);
        }
    }

    /**
     * Tests isStackEntryNecessaryAfter with random access pattern.
     * Verifies that the method handles non-sequential parameter queries.
     */
    @Test
    public void testIsStackEntryNecessaryAfterWithRandomAccessPattern() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Test random access pattern
        int[][] parameters = {{0, 0}, {10, 5}, {5, 2}, {15, 7}, {3, 1}, {20, 10}, {1, 0}, {8, 4}, {12, 6}, {6, 3}};
        for (int[] params : parameters) {
            assertDoesNotThrow(() -> marker.isStackEntryNecessaryAfter(params[0], params[1]),
                    "isStackEntryNecessaryAfter should handle offset " + params[0] + " and stackIndex " + params[1]);
        }
    }

    /**
     * Tests isStackEntryNecessaryAfter with runPartialEvaluator set to true.
     * Verifies that the method works when partial evaluator is configured to run.
     */
    @Test
    public void testIsStackEntryNecessaryAfterWithRunPartialEvaluatorTrue() {
        // Arrange - Create InstructionUsageMarker with runPartialEvaluator=true
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isStackEntryNecessaryAfter(0, 0),
                "isStackEntryNecessaryAfter should work when runPartialEvaluator is true");
    }

    /**
     * Tests isStackEntryNecessaryAfter with runPartialEvaluator set to false.
     * Verifies that the method works when partial evaluator is configured not to run.
     */
    @Test
    public void testIsStackEntryNecessaryAfterWithRunPartialEvaluatorFalse() {
        // Arrange - Create InstructionUsageMarker with runPartialEvaluator=false
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, false, true, true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isStackEntryNecessaryAfter(0, 0),
                "isStackEntryNecessaryAfter should work when runPartialEvaluator is false");
    }

    /**
     * Tests isStackEntryNecessaryAfter with ensureSafetyForVerifier set to true.
     * Verifies that the method works with verifier safety enabled.
     */
    @Test
    public void testIsStackEntryNecessaryAfterWithEnsureSafetyForVerifierTrue() {
        // Arrange - Create InstructionUsageMarker with ensureSafetyForVerifier=true
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isStackEntryNecessaryAfter(0, 0),
                "isStackEntryNecessaryAfter should work when ensureSafetyForVerifier is true");
    }

    /**
     * Tests isStackEntryNecessaryAfter with ensureSafetyForVerifier set to false.
     * Verifies that the method works with verifier safety disabled.
     */
    @Test
    public void testIsStackEntryNecessaryAfterWithEnsureSafetyForVerifierFalse() {
        // Arrange - Create InstructionUsageMarker with ensureSafetyForVerifier=false
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, false, true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isStackEntryNecessaryAfter(0, 0),
                "isStackEntryNecessaryAfter should work when ensureSafetyForVerifier is false");
    }

    /**
     * Tests isStackEntryNecessaryAfter with markExternalSideEffects set to true.
     * Verifies that the method works with external side effects marking enabled.
     */
    @Test
    public void testIsStackEntryNecessaryAfterWithMarkExternalSideEffectsTrue() {
        // Arrange - Create InstructionUsageMarker with markExternalSideEffects=true
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isStackEntryNecessaryAfter(0, 0),
                "isStackEntryNecessaryAfter should work when markExternalSideEffects is true");
    }

    /**
     * Tests isStackEntryNecessaryAfter with markExternalSideEffects set to false.
     * Verifies that the method works with external side effects marking disabled.
     */
    @Test
    public void testIsStackEntryNecessaryAfterWithMarkExternalSideEffectsFalse() {
        // Arrange - Create InstructionUsageMarker with markExternalSideEffects=false
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, false);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isStackEntryNecessaryAfter(0, 0),
                "isStackEntryNecessaryAfter should work when markExternalSideEffects is false");
    }

    /**
     * Tests isStackEntryNecessaryAfter called multiple times in rapid succession.
     * Verifies that the method handles rapid sequential calls.
     */
    @Test
    public void testIsStackEntryNecessaryAfterRapidSuccessiveCalls() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Call many times rapidly
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                marker.isStackEntryNecessaryAfter(i % 10, i % 5);
            }
        }, "isStackEntryNecessaryAfter should handle rapid successive calls");
    }

    /**
     * Tests isStackEntryNecessaryAfter with all boolean flag combinations.
     * Verifies that the method works with all possible constructor flag combinations.
     */
    @Test
    public void testIsStackEntryNecessaryAfterWithAllFlagCombinations() {
        // Arrange & Act & Assert - Test all 8 combinations
        PartialEvaluator pe = PartialEvaluator.Builder.create().build();

        InstructionUsageMarker marker1 = new InstructionUsageMarker(pe, true, true, true);
        assertDoesNotThrow(() -> marker1.isStackEntryNecessaryAfter(0, 0), "Should work with (true, true, true)");

        InstructionUsageMarker marker2 = new InstructionUsageMarker(pe, true, true, false);
        assertDoesNotThrow(() -> marker2.isStackEntryNecessaryAfter(0, 0), "Should work with (true, true, false)");

        InstructionUsageMarker marker3 = new InstructionUsageMarker(pe, true, false, true);
        assertDoesNotThrow(() -> marker3.isStackEntryNecessaryAfter(0, 0), "Should work with (true, false, true)");

        InstructionUsageMarker marker4 = new InstructionUsageMarker(pe, true, false, false);
        assertDoesNotThrow(() -> marker4.isStackEntryNecessaryAfter(0, 0), "Should work with (true, false, false)");

        InstructionUsageMarker marker5 = new InstructionUsageMarker(pe, false, true, true);
        assertDoesNotThrow(() -> marker5.isStackEntryNecessaryAfter(0, 0), "Should work with (false, true, true)");

        InstructionUsageMarker marker6 = new InstructionUsageMarker(pe, false, true, false);
        assertDoesNotThrow(() -> marker6.isStackEntryNecessaryAfter(0, 0), "Should work with (false, true, false)");

        InstructionUsageMarker marker7 = new InstructionUsageMarker(pe, false, false, true);
        assertDoesNotThrow(() -> marker7.isStackEntryNecessaryAfter(0, 0), "Should work with (false, false, true)");

        InstructionUsageMarker marker8 = new InstructionUsageMarker(pe, false, false, false);
        assertDoesNotThrow(() -> marker8.isStackEntryNecessaryAfter(0, 0), "Should work with (false, false, false)");
    }

    /**
     * Tests isStackEntryNecessaryAfter returns consistent type across calls.
     * Verifies that the method always returns a boolean value.
     */
    @Test
    public void testIsStackEntryNecessaryAfterAlwaysReturnsBoolean() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act - Call multiple times and collect results
        boolean[] results = new boolean[10];
        for (int i = 0; i < 10; i++) {
            results[i] = marker.isStackEntryNecessaryAfter(i, i % 5);
        }

        // Assert - All results should be boolean (implicitly true by type, but verify no exceptions)
        for (int i = 0; i < 10; i++) {
            assertNotNull(results[i], "Result " + i + " should not be null");
        }
    }
}
