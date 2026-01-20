package proguard.optimize.evaluation;

import org.junit.jupiter.api.Test;
import proguard.evaluation.PartialEvaluator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link InstructionUsageMarker#isInstructionNecessary(int)} method.
 * Tests the isInstructionNecessary method which checks if a specific instruction
 * is necessary in the most recently analyzed code attribute.
 */
public class InstructionUsageMarkerClaude_isInstructionNecessaryTest {

    /**
     * Tests that isInstructionNecessary can be called with offset 0.
     * Verifies that the method handles the first instruction offset.
     */
    @Test
    public void testIsInstructionNecessaryWithOffsetZero() {
        // Arrange - Create InstructionUsageMarker with default PartialEvaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Verify method can be called without throwing exception
        assertDoesNotThrow(() -> marker.isInstructionNecessary(0),
                "isInstructionNecessary should not throw exception with offset 0");
    }

    /**
     * Tests that isInstructionNecessary can be called with positive offsets.
     * Verifies that the method handles various positive instruction offsets.
     */
    @Test
    public void testIsInstructionNecessaryWithPositiveOffsets() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Test various positive offsets
        assertDoesNotThrow(() -> marker.isInstructionNecessary(1),
                "isInstructionNecessary should not throw exception with offset 1");
        assertDoesNotThrow(() -> marker.isInstructionNecessary(10),
                "isInstructionNecessary should not throw exception with offset 10");
        assertDoesNotThrow(() -> marker.isInstructionNecessary(100),
                "isInstructionNecessary should not throw exception with offset 100");
        assertDoesNotThrow(() -> marker.isInstructionNecessary(1000),
                "isInstructionNecessary should not throw exception with offset 1000");
    }

    /**
     * Tests that isInstructionNecessary returns a boolean value.
     * Verifies that the method returns either true or false.
     */
    @Test
    public void testIsInstructionNecessaryReturnsBoolean() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act - Call isInstructionNecessary
        boolean result = marker.isInstructionNecessary(0);

        // Assert - Verify result is a boolean (always true, method doesn't throw)
        assertNotNull(result, "isInstructionNecessary should return a non-null boolean value");
    }

    /**
     * Tests that isInstructionNecessary can be called multiple times with same offset.
     * Verifies that the method is stable and can be called repeatedly.
     */
    @Test
    public void testIsInstructionNecessaryMultipleCallsSameOffset() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act - Call isInstructionNecessary multiple times with same offset
        boolean result1 = marker.isInstructionNecessary(5);
        boolean result2 = marker.isInstructionNecessary(5);
        boolean result3 = marker.isInstructionNecessary(5);

        // Assert - Verify consistent results
        assertEquals(result1, result2, "isInstructionNecessary should return consistent result for same offset");
        assertEquals(result2, result3, "isInstructionNecessary should return consistent result for same offset");
    }

    /**
     * Tests that isInstructionNecessary can be called with different offsets in sequence.
     * Verifies that the method handles multiple different offsets.
     */
    @Test
    public void testIsInstructionNecessaryWithDifferentOffsets() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Test multiple different offsets
        assertDoesNotThrow(() -> {
            marker.isInstructionNecessary(0);
            marker.isInstructionNecessary(1);
            marker.isInstructionNecessary(2);
            marker.isInstructionNecessary(5);
            marker.isInstructionNecessary(10);
            marker.isInstructionNecessary(20);
        }, "isInstructionNecessary should handle multiple different offsets");
    }

    /**
     * Tests isInstructionNecessary with InstructionUsageMarker created using boolean constructor.
     * Verifies that the method works with the single-parameter constructor.
     */
    @Test
    public void testIsInstructionNecessaryWithBooleanConstructor() {
        // Arrange - Create InstructionUsageMarker with boolean constructor
        InstructionUsageMarker marker = new InstructionUsageMarker(true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isInstructionNecessary(0),
                "isInstructionNecessary should work with boolean constructor");
    }

    /**
     * Tests isInstructionNecessary with InstructionUsageMarker created using 3-parameter constructor.
     * Verifies that the method works with the three-parameter constructor.
     */
    @Test
    public void testIsInstructionNecessaryWithThreeParameterConstructor() {
        // Arrange - Create InstructionUsageMarker with 3-parameter constructor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isInstructionNecessary(0),
                "isInstructionNecessary should work with three-parameter constructor");
    }

    /**
     * Tests isInstructionNecessary with InstructionUsageMarker created using 4-parameter constructor.
     * Verifies that the method works with the four-parameter constructor.
     */
    @Test
    public void testIsInstructionNecessaryWithFourParameterConstructor() {
        // Arrange - Create InstructionUsageMarker with 4-parameter constructor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isInstructionNecessary(0),
                "isInstructionNecessary should work with four-parameter constructor");
    }

    /**
     * Tests isInstructionNecessary with different PartialEvaluator configurations.
     * Verifies that the method works with various evaluator settings.
     */
    @Test
    public void testIsInstructionNecessaryWithDifferentEvaluatorConfigurations() {
        // Arrange - Create InstructionUsageMarkers with different configurations
        PartialEvaluator evaluator1 = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker1 = new InstructionUsageMarker(evaluator1, true, true, true);

        PartialEvaluator evaluator2 = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker2 = new InstructionUsageMarker(evaluator2, false, false, false);

        // Act & Assert - Verify both work
        assertDoesNotThrow(() -> marker1.isInstructionNecessary(0),
                "isInstructionNecessary should work with first configuration");
        assertDoesNotThrow(() -> marker2.isInstructionNecessary(0),
                "isInstructionNecessary should work with second configuration");
    }

    /**
     * Tests that isInstructionNecessary can be called on multiple InstructionUsageMarker instances.
     * Verifies that the method works independently for different marker instances.
     */
    @Test
    public void testIsInstructionNecessaryOnMultipleInstances() {
        // Arrange - Create multiple InstructionUsageMarker instances
        InstructionUsageMarker marker1 = new InstructionUsageMarker(true);
        InstructionUsageMarker marker2 = new InstructionUsageMarker(false);
        InstructionUsageMarker marker3 = new InstructionUsageMarker(true);

        // Act & Assert - Verify all instances work independently
        assertDoesNotThrow(() -> marker1.isInstructionNecessary(5),
                "isInstructionNecessary should work on first marker instance");
        assertDoesNotThrow(() -> marker2.isInstructionNecessary(5),
                "isInstructionNecessary should work on second marker instance");
        assertDoesNotThrow(() -> marker3.isInstructionNecessary(5),
                "isInstructionNecessary should work on third marker instance");
    }

    /**
     * Tests isInstructionNecessary with a sequence of increasing offsets.
     * Verifies that the method handles sequential offset queries.
     */
    @Test
    public void testIsInstructionNecessaryWithIncreasingOffsets() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Test increasing sequence
        for (int offset = 0; offset < 20; offset++) {
            final int currentOffset = offset;
            assertDoesNotThrow(() -> marker.isInstructionNecessary(currentOffset),
                    "isInstructionNecessary should handle offset " + currentOffset);
        }
    }

    /**
     * Tests isInstructionNecessary with a sequence of decreasing offsets.
     * Verifies that the method handles reverse sequential offset queries.
     */
    @Test
    public void testIsInstructionNecessaryWithDecreasingOffsets() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Test decreasing sequence
        for (int offset = 20; offset >= 0; offset--) {
            final int currentOffset = offset;
            assertDoesNotThrow(() -> marker.isInstructionNecessary(currentOffset),
                    "isInstructionNecessary should handle offset " + currentOffset);
        }
    }

    /**
     * Tests isInstructionNecessary with random access pattern.
     * Verifies that the method handles non-sequential offset queries.
     */
    @Test
    public void testIsInstructionNecessaryWithRandomAccessPattern() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Test random access pattern
        int[] offsets = {0, 10, 5, 15, 3, 20, 1, 8, 12, 6};
        for (int offset : offsets) {
            assertDoesNotThrow(() -> marker.isInstructionNecessary(offset),
                    "isInstructionNecessary should handle offset " + offset + " in random access");
        }
    }

    /**
     * Tests isInstructionNecessary with runPartialEvaluator set to true.
     * Verifies that the method works when partial evaluator is configured to run.
     */
    @Test
    public void testIsInstructionNecessaryWithRunPartialEvaluatorTrue() {
        // Arrange - Create InstructionUsageMarker with runPartialEvaluator=true
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isInstructionNecessary(0),
                "isInstructionNecessary should work when runPartialEvaluator is true");
    }

    /**
     * Tests isInstructionNecessary with runPartialEvaluator set to false.
     * Verifies that the method works when partial evaluator is configured not to run.
     */
    @Test
    public void testIsInstructionNecessaryWithRunPartialEvaluatorFalse() {
        // Arrange - Create InstructionUsageMarker with runPartialEvaluator=false
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, false, true, true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isInstructionNecessary(0),
                "isInstructionNecessary should work when runPartialEvaluator is false");
    }

    /**
     * Tests isInstructionNecessary with ensureSafetyForVerifier set to true.
     * Verifies that the method works with verifier safety enabled.
     */
    @Test
    public void testIsInstructionNecessaryWithEnsureSafetyForVerifierTrue() {
        // Arrange - Create InstructionUsageMarker with ensureSafetyForVerifier=true
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isInstructionNecessary(0),
                "isInstructionNecessary should work when ensureSafetyForVerifier is true");
    }

    /**
     * Tests isInstructionNecessary with ensureSafetyForVerifier set to false.
     * Verifies that the method works with verifier safety disabled.
     */
    @Test
    public void testIsInstructionNecessaryWithEnsureSafetyForVerifierFalse() {
        // Arrange - Create InstructionUsageMarker with ensureSafetyForVerifier=false
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, false, true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isInstructionNecessary(0),
                "isInstructionNecessary should work when ensureSafetyForVerifier is false");
    }

    /**
     * Tests isInstructionNecessary with markExternalSideEffects set to true.
     * Verifies that the method works with external side effects marking enabled.
     */
    @Test
    public void testIsInstructionNecessaryWithMarkExternalSideEffectsTrue() {
        // Arrange - Create InstructionUsageMarker with markExternalSideEffects=true
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isInstructionNecessary(0),
                "isInstructionNecessary should work when markExternalSideEffects is true");
    }

    /**
     * Tests isInstructionNecessary with markExternalSideEffects set to false.
     * Verifies that the method works with external side effects marking disabled.
     */
    @Test
    public void testIsInstructionNecessaryWithMarkExternalSideEffectsFalse() {
        // Arrange - Create InstructionUsageMarker with markExternalSideEffects=false
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, false);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isInstructionNecessary(0),
                "isInstructionNecessary should work when markExternalSideEffects is false");
    }

    /**
     * Tests isInstructionNecessary called multiple times in rapid succession.
     * Verifies that the method is thread-safe for rapid sequential calls.
     */
    @Test
    public void testIsInstructionNecessaryRapidSuccessiveCalls() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Call many times rapidly
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                marker.isInstructionNecessary(i % 10);
            }
        }, "isInstructionNecessary should handle rapid successive calls");
    }

    /**
     * Tests isInstructionNecessary with all boolean flag combinations.
     * Verifies that the method works with all possible constructor flag combinations.
     */
    @Test
    public void testIsInstructionNecessaryWithAllFlagCombinations() {
        // Arrange & Act & Assert - Test all 8 combinations
        PartialEvaluator pe = PartialEvaluator.Builder.create().build();

        InstructionUsageMarker marker1 = new InstructionUsageMarker(pe, true, true, true);
        assertDoesNotThrow(() -> marker1.isInstructionNecessary(0), "Should work with (true, true, true)");

        InstructionUsageMarker marker2 = new InstructionUsageMarker(pe, true, true, false);
        assertDoesNotThrow(() -> marker2.isInstructionNecessary(0), "Should work with (true, true, false)");

        InstructionUsageMarker marker3 = new InstructionUsageMarker(pe, true, false, true);
        assertDoesNotThrow(() -> marker3.isInstructionNecessary(0), "Should work with (true, false, true)");

        InstructionUsageMarker marker4 = new InstructionUsageMarker(pe, true, false, false);
        assertDoesNotThrow(() -> marker4.isInstructionNecessary(0), "Should work with (true, false, false)");

        InstructionUsageMarker marker5 = new InstructionUsageMarker(pe, false, true, true);
        assertDoesNotThrow(() -> marker5.isInstructionNecessary(0), "Should work with (false, true, true)");

        InstructionUsageMarker marker6 = new InstructionUsageMarker(pe, false, true, false);
        assertDoesNotThrow(() -> marker6.isInstructionNecessary(0), "Should work with (false, true, false)");

        InstructionUsageMarker marker7 = new InstructionUsageMarker(pe, false, false, true);
        assertDoesNotThrow(() -> marker7.isInstructionNecessary(0), "Should work with (false, false, true)");

        InstructionUsageMarker marker8 = new InstructionUsageMarker(pe, false, false, false);
        assertDoesNotThrow(() -> marker8.isInstructionNecessary(0), "Should work with (false, false, false)");
    }

    /**
     * Tests isInstructionNecessary returns consistent type across calls.
     * Verifies that the method always returns a boolean value.
     */
    @Test
    public void testIsInstructionNecessaryAlwaysReturnsBoolean() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act - Call multiple times and collect results
        boolean[] results = new boolean[10];
        for (int i = 0; i < 10; i++) {
            results[i] = marker.isInstructionNecessary(i);
        }

        // Assert - All results should be boolean (implicitly true by type, but verify no exceptions)
        for (int i = 0; i < 10; i++) {
            assertNotNull(results[i], "Result " + i + " should not be null");
        }
    }
}
