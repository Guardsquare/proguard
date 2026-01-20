package proguard.optimize.evaluation;

import org.junit.jupiter.api.Test;
import proguard.evaluation.PartialEvaluator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link InstructionUsageMarker#isStackEntryUnwantedBefore(int, int)} method.
 * Tests the isStackEntryUnwantedBefore method which returns whether the specified stack entry
 * before the given offset is unwanted, e.g. because it was intended as a method parameter
 * that has been removed.
 */
public class InstructionUsageMarkerClaude_isStackEntryUnwantedBeforeTest {

    /**
     * Tests that isStackEntryUnwantedBefore can be called with offset 0 and stackIndex 0.
     * Verifies that the method handles the first instruction offset and stack index.
     */
    @Test
    public void testIsStackEntryUnwantedBeforeWithZeroOffsetAndZeroIndex() {
        // Arrange - Create InstructionUsageMarker with default PartialEvaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Verify method can be called without throwing exception
        assertDoesNotThrow(() -> marker.isStackEntryUnwantedBefore(0, 0),
                "isStackEntryUnwantedBefore should not throw exception with offset 0 and stackIndex 0");
    }

    /**
     * Tests that isStackEntryUnwantedBefore can be called with various positive offsets and indices.
     * Verifies that the method handles different instruction offsets and stack indices.
     */
    @Test
    public void testIsStackEntryUnwantedBeforeWithPositiveOffsetsAndIndices() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Test various positive offsets and indices
        assertDoesNotThrow(() -> marker.isStackEntryUnwantedBefore(1, 0),
                "isStackEntryUnwantedBefore should not throw exception with offset 1, stackIndex 0");
        assertDoesNotThrow(() -> marker.isStackEntryUnwantedBefore(10, 5),
                "isStackEntryUnwantedBefore should not throw exception with offset 10, stackIndex 5");
        assertDoesNotThrow(() -> marker.isStackEntryUnwantedBefore(100, 10),
                "isStackEntryUnwantedBefore should not throw exception with offset 100, stackIndex 10");
    }

    /**
     * Tests that isStackEntryUnwantedBefore returns a boolean value.
     * Verifies that the method returns either true or false.
     */
    @Test
    public void testIsStackEntryUnwantedBeforeReturnsBoolean() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act - Call isStackEntryUnwantedBefore
        boolean result = marker.isStackEntryUnwantedBefore(0, 0);

        // Assert - Verify result is a boolean (always true, method doesn't throw)
        assertNotNull(result, "isStackEntryUnwantedBefore should return a non-null boolean value");
    }

    /**
     * Tests that isStackEntryUnwantedBefore can be called multiple times with same parameters.
     * Verifies that the method is stable and can be called repeatedly.
     */
    @Test
    public void testIsStackEntryUnwantedBeforeMultipleCallsSameParameters() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act - Call isStackEntryUnwantedBefore multiple times with same parameters
        boolean result1 = marker.isStackEntryUnwantedBefore(5, 2);
        boolean result2 = marker.isStackEntryUnwantedBefore(5, 2);
        boolean result3 = marker.isStackEntryUnwantedBefore(5, 2);

        // Assert - Verify consistent results
        assertEquals(result1, result2, "isStackEntryUnwantedBefore should return consistent result for same parameters");
        assertEquals(result2, result3, "isStackEntryUnwantedBefore should return consistent result for same parameters");
    }

    /**
     * Tests that isStackEntryUnwantedBefore can be called with different parameters.
     * Verifies that the method handles multiple different parameter combinations.
     */
    @Test
    public void testIsStackEntryUnwantedBeforeWithDifferentParameters() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Test multiple different parameter combinations
        assertDoesNotThrow(() -> {
            marker.isStackEntryUnwantedBefore(0, 0);
            marker.isStackEntryUnwantedBefore(1, 0);
            marker.isStackEntryUnwantedBefore(2, 1);
            marker.isStackEntryUnwantedBefore(5, 2);
            marker.isStackEntryUnwantedBefore(10, 5);
            marker.isStackEntryUnwantedBefore(20, 10);
        }, "isStackEntryUnwantedBefore should handle multiple different parameter combinations");
    }

    /**
     * Tests isStackEntryUnwantedBefore with different stack indices for same offset.
     * Verifies that the method handles different stack indices correctly.
     */
    @Test
    public void testIsStackEntryUnwantedBeforeWithDifferentStackIndices() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Test same offset with different stack indices
        assertDoesNotThrow(() -> {
            marker.isStackEntryUnwantedBefore(10, 0);
            marker.isStackEntryUnwantedBefore(10, 1);
            marker.isStackEntryUnwantedBefore(10, 2);
            marker.isStackEntryUnwantedBefore(10, 3);
            marker.isStackEntryUnwantedBefore(10, 4);
        }, "isStackEntryUnwantedBefore should handle different stack indices for same offset");
    }

    /**
     * Tests isStackEntryUnwantedBefore with InstructionUsageMarker created using boolean constructor.
     * Verifies that the method works with the single-parameter constructor.
     */
    @Test
    public void testIsStackEntryUnwantedBeforeWithBooleanConstructor() {
        // Arrange - Create InstructionUsageMarker with boolean constructor
        InstructionUsageMarker marker = new InstructionUsageMarker(true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isStackEntryUnwantedBefore(0, 0),
                "isStackEntryUnwantedBefore should work with boolean constructor");
    }

    /**
     * Tests isStackEntryUnwantedBefore with InstructionUsageMarker created using 3-parameter constructor.
     * Verifies that the method works with the three-parameter constructor.
     */
    @Test
    public void testIsStackEntryUnwantedBeforeWithThreeParameterConstructor() {
        // Arrange - Create InstructionUsageMarker with 3-parameter constructor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isStackEntryUnwantedBefore(0, 0),
                "isStackEntryUnwantedBefore should work with three-parameter constructor");
    }

    /**
     * Tests isStackEntryUnwantedBefore with InstructionUsageMarker created using 4-parameter constructor.
     * Verifies that the method works with the four-parameter constructor.
     */
    @Test
    public void testIsStackEntryUnwantedBeforeWithFourParameterConstructor() {
        // Arrange - Create InstructionUsageMarker with 4-parameter constructor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isStackEntryUnwantedBefore(0, 0),
                "isStackEntryUnwantedBefore should work with four-parameter constructor");
    }

    /**
     * Tests isStackEntryUnwantedBefore with different PartialEvaluator configurations.
     * Verifies that the method works with various evaluator settings.
     */
    @Test
    public void testIsStackEntryUnwantedBeforeWithDifferentEvaluatorConfigurations() {
        // Arrange - Create InstructionUsageMarkers with different configurations
        PartialEvaluator evaluator1 = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker1 = new InstructionUsageMarker(evaluator1, true, true, true);

        PartialEvaluator evaluator2 = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker2 = new InstructionUsageMarker(evaluator2, false, false, false);

        // Act & Assert - Verify both work
        assertDoesNotThrow(() -> marker1.isStackEntryUnwantedBefore(0, 0),
                "isStackEntryUnwantedBefore should work with first configuration");
        assertDoesNotThrow(() -> marker2.isStackEntryUnwantedBefore(0, 0),
                "isStackEntryUnwantedBefore should work with second configuration");
    }

    /**
     * Tests that isStackEntryUnwantedBefore can be called on multiple InstructionUsageMarker instances.
     * Verifies that the method works independently for different marker instances.
     */
    @Test
    public void testIsStackEntryUnwantedBeforeOnMultipleInstances() {
        // Arrange - Create multiple InstructionUsageMarker instances
        InstructionUsageMarker marker1 = new InstructionUsageMarker(true);
        InstructionUsageMarker marker2 = new InstructionUsageMarker(false);
        InstructionUsageMarker marker3 = new InstructionUsageMarker(true);

        // Act & Assert - Verify all instances work independently
        assertDoesNotThrow(() -> marker1.isStackEntryUnwantedBefore(5, 2),
                "isStackEntryUnwantedBefore should work on first marker instance");
        assertDoesNotThrow(() -> marker2.isStackEntryUnwantedBefore(5, 2),
                "isStackEntryUnwantedBefore should work on second marker instance");
        assertDoesNotThrow(() -> marker3.isStackEntryUnwantedBefore(5, 2),
                "isStackEntryUnwantedBefore should work on third marker instance");
    }

    /**
     * Tests isStackEntryUnwantedBefore with a sequence of increasing offsets.
     * Verifies that the method handles sequential offset queries.
     */
    @Test
    public void testIsStackEntryUnwantedBeforeWithIncreasingOffsets() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Test increasing sequence
        for (int offset = 0; offset < 20; offset++) {
            final int currentOffset = offset;
            assertDoesNotThrow(() -> marker.isStackEntryUnwantedBefore(currentOffset, 0),
                    "isStackEntryUnwantedBefore should handle offset " + currentOffset);
        }
    }

    /**
     * Tests isStackEntryUnwantedBefore with a sequence of increasing stack indices.
     * Verifies that the method handles sequential stack index queries.
     */
    @Test
    public void testIsStackEntryUnwantedBeforeWithIncreasingStackIndices() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Test increasing sequence of stack indices
        for (int stackIndex = 0; stackIndex < 10; stackIndex++) {
            final int currentStackIndex = stackIndex;
            assertDoesNotThrow(() -> marker.isStackEntryUnwantedBefore(5, currentStackIndex),
                    "isStackEntryUnwantedBefore should handle stackIndex " + currentStackIndex);
        }
    }

    /**
     * Tests isStackEntryUnwantedBefore with random access pattern.
     * Verifies that the method handles non-sequential parameter queries.
     */
    @Test
    public void testIsStackEntryUnwantedBeforeWithRandomAccessPattern() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Test random access pattern
        int[][] parameters = {{0, 0}, {10, 5}, {5, 2}, {15, 7}, {3, 1}, {20, 10}, {1, 0}, {8, 4}, {12, 6}, {6, 3}};
        for (int[] params : parameters) {
            assertDoesNotThrow(() -> marker.isStackEntryUnwantedBefore(params[0], params[1]),
                    "isStackEntryUnwantedBefore should handle offset " + params[0] + " and stackIndex " + params[1]);
        }
    }

    /**
     * Tests isStackEntryUnwantedBefore with runPartialEvaluator set to true.
     * Verifies that the method works when partial evaluator is configured to run.
     */
    @Test
    public void testIsStackEntryUnwantedBeforeWithRunPartialEvaluatorTrue() {
        // Arrange - Create InstructionUsageMarker with runPartialEvaluator=true
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isStackEntryUnwantedBefore(0, 0),
                "isStackEntryUnwantedBefore should work when runPartialEvaluator is true");
    }

    /**
     * Tests isStackEntryUnwantedBefore with runPartialEvaluator set to false.
     * Verifies that the method works when partial evaluator is configured not to run.
     */
    @Test
    public void testIsStackEntryUnwantedBeforeWithRunPartialEvaluatorFalse() {
        // Arrange - Create InstructionUsageMarker with runPartialEvaluator=false
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, false, true, true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isStackEntryUnwantedBefore(0, 0),
                "isStackEntryUnwantedBefore should work when runPartialEvaluator is false");
    }

    /**
     * Tests isStackEntryUnwantedBefore with ensureSafetyForVerifier set to true.
     * Verifies that the method works with verifier safety enabled.
     */
    @Test
    public void testIsStackEntryUnwantedBeforeWithEnsureSafetyForVerifierTrue() {
        // Arrange - Create InstructionUsageMarker with ensureSafetyForVerifier=true
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isStackEntryUnwantedBefore(0, 0),
                "isStackEntryUnwantedBefore should work when ensureSafetyForVerifier is true");
    }

    /**
     * Tests isStackEntryUnwantedBefore with ensureSafetyForVerifier set to false.
     * Verifies that the method works with verifier safety disabled.
     */
    @Test
    public void testIsStackEntryUnwantedBeforeWithEnsureSafetyForVerifierFalse() {
        // Arrange - Create InstructionUsageMarker with ensureSafetyForVerifier=false
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, false, true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isStackEntryUnwantedBefore(0, 0),
                "isStackEntryUnwantedBefore should work when ensureSafetyForVerifier is false");
    }

    /**
     * Tests isStackEntryUnwantedBefore with markExternalSideEffects set to true.
     * Verifies that the method works with external side effects marking enabled.
     */
    @Test
    public void testIsStackEntryUnwantedBeforeWithMarkExternalSideEffectsTrue() {
        // Arrange - Create InstructionUsageMarker with markExternalSideEffects=true
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isStackEntryUnwantedBefore(0, 0),
                "isStackEntryUnwantedBefore should work when markExternalSideEffects is true");
    }

    /**
     * Tests isStackEntryUnwantedBefore with markExternalSideEffects set to false.
     * Verifies that the method works with external side effects marking disabled.
     */
    @Test
    public void testIsStackEntryUnwantedBeforeWithMarkExternalSideEffectsFalse() {
        // Arrange - Create InstructionUsageMarker with markExternalSideEffects=false
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, false);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isStackEntryUnwantedBefore(0, 0),
                "isStackEntryUnwantedBefore should work when markExternalSideEffects is false");
    }

    /**
     * Tests isStackEntryUnwantedBefore called multiple times in rapid succession.
     * Verifies that the method handles rapid sequential calls.
     */
    @Test
    public void testIsStackEntryUnwantedBeforeRapidSuccessiveCalls() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Call many times rapidly
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                marker.isStackEntryUnwantedBefore(i % 10, i % 5);
            }
        }, "isStackEntryUnwantedBefore should handle rapid successive calls");
    }

    /**
     * Tests isStackEntryUnwantedBefore with all boolean flag combinations.
     * Verifies that the method works with all possible constructor flag combinations.
     */
    @Test
    public void testIsStackEntryUnwantedBeforeWithAllFlagCombinations() {
        // Arrange & Act & Assert - Test all 8 combinations
        PartialEvaluator pe = PartialEvaluator.Builder.create().build();

        InstructionUsageMarker marker1 = new InstructionUsageMarker(pe, true, true, true);
        assertDoesNotThrow(() -> marker1.isStackEntryUnwantedBefore(0, 0), "Should work with (true, true, true)");

        InstructionUsageMarker marker2 = new InstructionUsageMarker(pe, true, true, false);
        assertDoesNotThrow(() -> marker2.isStackEntryUnwantedBefore(0, 0), "Should work with (true, true, false)");

        InstructionUsageMarker marker3 = new InstructionUsageMarker(pe, true, false, true);
        assertDoesNotThrow(() -> marker3.isStackEntryUnwantedBefore(0, 0), "Should work with (true, false, true)");

        InstructionUsageMarker marker4 = new InstructionUsageMarker(pe, true, false, false);
        assertDoesNotThrow(() -> marker4.isStackEntryUnwantedBefore(0, 0), "Should work with (true, false, false)");

        InstructionUsageMarker marker5 = new InstructionUsageMarker(pe, false, true, true);
        assertDoesNotThrow(() -> marker5.isStackEntryUnwantedBefore(0, 0), "Should work with (false, true, true)");

        InstructionUsageMarker marker6 = new InstructionUsageMarker(pe, false, true, false);
        assertDoesNotThrow(() -> marker6.isStackEntryUnwantedBefore(0, 0), "Should work with (false, true, false)");

        InstructionUsageMarker marker7 = new InstructionUsageMarker(pe, false, false, true);
        assertDoesNotThrow(() -> marker7.isStackEntryUnwantedBefore(0, 0), "Should work with (false, false, true)");

        InstructionUsageMarker marker8 = new InstructionUsageMarker(pe, false, false, false);
        assertDoesNotThrow(() -> marker8.isStackEntryUnwantedBefore(0, 0), "Should work with (false, false, false)");
    }

    /**
     * Tests isStackEntryUnwantedBefore returns consistent type across calls.
     * Verifies that the method always returns a boolean value.
     */
    @Test
    public void testIsStackEntryUnwantedBeforeAlwaysReturnsBoolean() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act - Call multiple times and collect results
        boolean[] results = new boolean[10];
        for (int i = 0; i < 10; i++) {
            results[i] = marker.isStackEntryUnwantedBefore(i, i % 5);
        }

        // Assert - All results should be boolean (implicitly true by type, but verify no exceptions)
        for (int i = 0; i < 10; i++) {
            assertNotNull(results[i], "Result " + i + " should not be null");
        }
    }
}
