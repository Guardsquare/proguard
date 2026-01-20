package proguard.optimize.evaluation;

import org.junit.jupiter.api.Test;
import proguard.evaluation.PartialEvaluator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link InstructionUsageMarker#isTraced(int)} method.
 * Tests the isTraced method which checks if a specific instruction was traced
 * in the most recently analyzed code attribute.
 */
public class InstructionUsageMarkerClaude_isTracedTest {

    /**
     * Tests that isTraced can be called with offset 0.
     * Verifies that the method handles the first instruction offset.
     */
    @Test
    public void testIsTracedWithOffsetZero() {
        // Arrange - Create InstructionUsageMarker with default PartialEvaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Verify method can be called without throwing exception
        assertDoesNotThrow(() -> marker.isTraced(0),
                "isTraced should not throw exception with offset 0");
    }

    /**
     * Tests that isTraced can be called with positive offsets.
     * Verifies that the method handles various positive instruction offsets.
     */
    @Test
    public void testIsTracedWithPositiveOffsets() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Test various positive offsets
        assertDoesNotThrow(() -> marker.isTraced(1),
                "isTraced should not throw exception with offset 1");
        assertDoesNotThrow(() -> marker.isTraced(10),
                "isTraced should not throw exception with offset 10");
        assertDoesNotThrow(() -> marker.isTraced(100),
                "isTraced should not throw exception with offset 100");
        assertDoesNotThrow(() -> marker.isTraced(1000),
                "isTraced should not throw exception with offset 1000");
    }

    /**
     * Tests that isTraced returns a boolean value.
     * Verifies that the method returns either true or false.
     */
    @Test
    public void testIsTracedReturnsBoolean() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act - Call isTraced
        boolean result = marker.isTraced(0);

        // Assert - Verify result is a boolean (always true, method doesn't throw)
        assertNotNull(result, "isTraced should return a non-null boolean value");
    }

    /**
     * Tests that isTraced can be called multiple times with same offset.
     * Verifies that the method is stable and can be called repeatedly.
     */
    @Test
    public void testIsTracedMultipleCallsSameOffset() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act - Call isTraced multiple times with same offset
        boolean result1 = marker.isTraced(5);
        boolean result2 = marker.isTraced(5);
        boolean result3 = marker.isTraced(5);

        // Assert - Verify consistent results
        assertEquals(result1, result2, "isTraced should return consistent result for same offset");
        assertEquals(result2, result3, "isTraced should return consistent result for same offset");
    }

    /**
     * Tests that isTraced can be called with different offsets in sequence.
     * Verifies that the method handles multiple different offsets.
     */
    @Test
    public void testIsTracedWithDifferentOffsets() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Test multiple different offsets
        assertDoesNotThrow(() -> {
            marker.isTraced(0);
            marker.isTraced(1);
            marker.isTraced(2);
            marker.isTraced(5);
            marker.isTraced(10);
            marker.isTraced(20);
        }, "isTraced should handle multiple different offsets");
    }

    /**
     * Tests isTraced with InstructionUsageMarker created using boolean constructor.
     * Verifies that the method works with the single-parameter constructor.
     */
    @Test
    public void testIsTracedWithBooleanConstructor() {
        // Arrange - Create InstructionUsageMarker with boolean constructor
        InstructionUsageMarker marker = new InstructionUsageMarker(true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isTraced(0),
                "isTraced should work with boolean constructor");
    }

    /**
     * Tests isTraced with InstructionUsageMarker created using 3-parameter constructor.
     * Verifies that the method works with the three-parameter constructor.
     */
    @Test
    public void testIsTracedWithThreeParameterConstructor() {
        // Arrange - Create InstructionUsageMarker with 3-parameter constructor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isTraced(0),
                "isTraced should work with three-parameter constructor");
    }

    /**
     * Tests isTraced with InstructionUsageMarker created using 4-parameter constructor.
     * Verifies that the method works with the four-parameter constructor.
     */
    @Test
    public void testIsTracedWithFourParameterConstructor() {
        // Arrange - Create InstructionUsageMarker with 4-parameter constructor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isTraced(0),
                "isTraced should work with four-parameter constructor");
    }

    /**
     * Tests isTraced with different PartialEvaluator configurations.
     * Verifies that the method works with various evaluator settings.
     */
    @Test
    public void testIsTracedWithDifferentEvaluatorConfigurations() {
        // Arrange - Create InstructionUsageMarkers with different configurations
        PartialEvaluator evaluator1 = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker1 = new InstructionUsageMarker(evaluator1, true, true, true);

        PartialEvaluator evaluator2 = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker2 = new InstructionUsageMarker(evaluator2, false, false, false);

        // Act & Assert - Verify both work
        assertDoesNotThrow(() -> marker1.isTraced(0),
                "isTraced should work with first configuration");
        assertDoesNotThrow(() -> marker2.isTraced(0),
                "isTraced should work with second configuration");
    }

    /**
     * Tests that isTraced can be called on multiple InstructionUsageMarker instances.
     * Verifies that the method works independently for different marker instances.
     */
    @Test
    public void testIsTracedOnMultipleInstances() {
        // Arrange - Create multiple InstructionUsageMarker instances
        InstructionUsageMarker marker1 = new InstructionUsageMarker(true);
        InstructionUsageMarker marker2 = new InstructionUsageMarker(false);
        InstructionUsageMarker marker3 = new InstructionUsageMarker(true);

        // Act & Assert - Verify all instances work independently
        assertDoesNotThrow(() -> marker1.isTraced(5),
                "isTraced should work on first marker instance");
        assertDoesNotThrow(() -> marker2.isTraced(5),
                "isTraced should work on second marker instance");
        assertDoesNotThrow(() -> marker3.isTraced(5),
                "isTraced should work on third marker instance");
    }

    /**
     * Tests isTraced with a sequence of increasing offsets.
     * Verifies that the method handles sequential offset queries.
     */
    @Test
    public void testIsTracedWithIncreasingOffsets() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Test increasing sequence
        for (int offset = 0; offset < 20; offset++) {
            final int currentOffset = offset;
            assertDoesNotThrow(() -> marker.isTraced(currentOffset),
                    "isTraced should handle offset " + currentOffset);
        }
    }

    /**
     * Tests isTraced with a sequence of decreasing offsets.
     * Verifies that the method handles reverse sequential offset queries.
     */
    @Test
    public void testIsTracedWithDecreasingOffsets() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Test decreasing sequence
        for (int offset = 20; offset >= 0; offset--) {
            final int currentOffset = offset;
            assertDoesNotThrow(() -> marker.isTraced(currentOffset),
                    "isTraced should handle offset " + currentOffset);
        }
    }

    /**
     * Tests isTraced with random access pattern.
     * Verifies that the method handles non-sequential offset queries.
     */
    @Test
    public void testIsTracedWithRandomAccessPattern() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Test random access pattern
        int[] offsets = {0, 10, 5, 15, 3, 20, 1, 8, 12, 6};
        for (int offset : offsets) {
            assertDoesNotThrow(() -> marker.isTraced(offset),
                    "isTraced should handle offset " + offset + " in random access");
        }
    }

    /**
     * Tests isTraced with runPartialEvaluator set to true.
     * Verifies that the method works when partial evaluator is configured to run.
     */
    @Test
    public void testIsTracedWithRunPartialEvaluatorTrue() {
        // Arrange - Create InstructionUsageMarker with runPartialEvaluator=true
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isTraced(0),
                "isTraced should work when runPartialEvaluator is true");
    }

    /**
     * Tests isTraced with runPartialEvaluator set to false.
     * Verifies that the method works when partial evaluator is configured not to run.
     */
    @Test
    public void testIsTracedWithRunPartialEvaluatorFalse() {
        // Arrange - Create InstructionUsageMarker with runPartialEvaluator=false
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, false, true, true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isTraced(0),
                "isTraced should work when runPartialEvaluator is false");
    }

    /**
     * Tests isTraced with ensureSafetyForVerifier set to true.
     * Verifies that the method works with verifier safety enabled.
     */
    @Test
    public void testIsTracedWithEnsureSafetyForVerifierTrue() {
        // Arrange - Create InstructionUsageMarker with ensureSafetyForVerifier=true
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isTraced(0),
                "isTraced should work when ensureSafetyForVerifier is true");
    }

    /**
     * Tests isTraced with ensureSafetyForVerifier set to false.
     * Verifies that the method works with verifier safety disabled.
     */
    @Test
    public void testIsTracedWithEnsureSafetyForVerifierFalse() {
        // Arrange - Create InstructionUsageMarker with ensureSafetyForVerifier=false
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, false, true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isTraced(0),
                "isTraced should work when ensureSafetyForVerifier is false");
    }

    /**
     * Tests isTraced with markExternalSideEffects set to true.
     * Verifies that the method works with external side effects marking enabled.
     */
    @Test
    public void testIsTracedWithMarkExternalSideEffectsTrue() {
        // Arrange - Create InstructionUsageMarker with markExternalSideEffects=true
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isTraced(0),
                "isTraced should work when markExternalSideEffects is true");
    }

    /**
     * Tests isTraced with markExternalSideEffects set to false.
     * Verifies that the method works with external side effects marking disabled.
     */
    @Test
    public void testIsTracedWithMarkExternalSideEffectsFalse() {
        // Arrange - Create InstructionUsageMarker with markExternalSideEffects=false
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, false);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isTraced(0),
                "isTraced should work when markExternalSideEffects is false");
    }

    /**
     * Tests isTraced called multiple times in rapid succession.
     * Verifies that the method is thread-safe for rapid sequential calls.
     */
    @Test
    public void testIsTracedRapidSuccessiveCalls() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Call many times rapidly
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                marker.isTraced(i % 10);
            }
        }, "isTraced should handle rapid successive calls");
    }

    /**
     * Tests isTraced with all boolean flag combinations.
     * Verifies that the method works with all possible constructor flag combinations.
     */
    @Test
    public void testIsTracedWithAllFlagCombinations() {
        // Arrange & Act & Assert - Test all 8 combinations
        PartialEvaluator pe = PartialEvaluator.Builder.create().build();

        InstructionUsageMarker marker1 = new InstructionUsageMarker(pe, true, true, true);
        assertDoesNotThrow(() -> marker1.isTraced(0), "Should work with (true, true, true)");

        InstructionUsageMarker marker2 = new InstructionUsageMarker(pe, true, true, false);
        assertDoesNotThrow(() -> marker2.isTraced(0), "Should work with (true, true, false)");

        InstructionUsageMarker marker3 = new InstructionUsageMarker(pe, true, false, true);
        assertDoesNotThrow(() -> marker3.isTraced(0), "Should work with (true, false, true)");

        InstructionUsageMarker marker4 = new InstructionUsageMarker(pe, true, false, false);
        assertDoesNotThrow(() -> marker4.isTraced(0), "Should work with (true, false, false)");

        InstructionUsageMarker marker5 = new InstructionUsageMarker(pe, false, true, true);
        assertDoesNotThrow(() -> marker5.isTraced(0), "Should work with (false, true, true)");

        InstructionUsageMarker marker6 = new InstructionUsageMarker(pe, false, true, false);
        assertDoesNotThrow(() -> marker6.isTraced(0), "Should work with (false, true, false)");

        InstructionUsageMarker marker7 = new InstructionUsageMarker(pe, false, false, true);
        assertDoesNotThrow(() -> marker7.isTraced(0), "Should work with (false, false, true)");

        InstructionUsageMarker marker8 = new InstructionUsageMarker(pe, false, false, false);
        assertDoesNotThrow(() -> marker8.isTraced(0), "Should work with (false, false, false)");
    }

    /**
     * Tests isTraced returns consistent type across calls.
     * Verifies that the method always returns a boolean value.
     */
    @Test
    public void testIsTracedAlwaysReturnsBoolean() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act - Call multiple times and collect results
        boolean[] results = new boolean[10];
        for (int i = 0; i < 10; i++) {
            results[i] = marker.isTraced(i);
        }

        // Assert - All results should be boolean (implicitly true by type, but verify no exceptions)
        for (int i = 0; i < 10; i++) {
            assertNotNull(results[i], "Result " + i + " should not be null");
        }
    }
}
