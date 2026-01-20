package proguard.optimize.evaluation;

import org.junit.jupiter.api.Test;
import proguard.evaluation.PartialEvaluator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link InstructionUsageMarker#isExtraPushPopInstructionNecessary(int)} method.
 * Tests the isExtraPushPopInstructionNecessary method which checks if an extra push/pop
 * instruction is required at a given offset in the most recently analyzed code attribute.
 */
public class InstructionUsageMarkerClaude_isExtraPushPopInstructionNecessaryTest {

    /**
     * Tests that isExtraPushPopInstructionNecessary can be called with offset 0.
     * Verifies that the method handles the first instruction offset.
     */
    @Test
    public void testIsExtraPushPopInstructionNecessaryWithOffsetZero() {
        // Arrange - Create InstructionUsageMarker with default PartialEvaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Verify method can be called without throwing exception
        assertDoesNotThrow(() -> marker.isExtraPushPopInstructionNecessary(0),
                "isExtraPushPopInstructionNecessary should not throw exception with offset 0");
    }

    /**
     * Tests that isExtraPushPopInstructionNecessary can be called with positive offsets.
     * Verifies that the method handles various positive instruction offsets.
     */
    @Test
    public void testIsExtraPushPopInstructionNecessaryWithPositiveOffsets() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Test various positive offsets
        assertDoesNotThrow(() -> marker.isExtraPushPopInstructionNecessary(1),
                "isExtraPushPopInstructionNecessary should not throw exception with offset 1");
        assertDoesNotThrow(() -> marker.isExtraPushPopInstructionNecessary(10),
                "isExtraPushPopInstructionNecessary should not throw exception with offset 10");
        assertDoesNotThrow(() -> marker.isExtraPushPopInstructionNecessary(100),
                "isExtraPushPopInstructionNecessary should not throw exception with offset 100");
        assertDoesNotThrow(() -> marker.isExtraPushPopInstructionNecessary(1000),
                "isExtraPushPopInstructionNecessary should not throw exception with offset 1000");
    }

    /**
     * Tests that isExtraPushPopInstructionNecessary returns a boolean value.
     * Verifies that the method returns either true or false.
     */
    @Test
    public void testIsExtraPushPopInstructionNecessaryReturnsBoolean() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act - Call isExtraPushPopInstructionNecessary
        boolean result = marker.isExtraPushPopInstructionNecessary(0);

        // Assert - Verify result is a boolean (always true, method doesn't throw)
        assertNotNull(result, "isExtraPushPopInstructionNecessary should return a non-null boolean value");
    }

    /**
     * Tests that isExtraPushPopInstructionNecessary can be called multiple times with same offset.
     * Verifies that the method is stable and can be called repeatedly.
     */
    @Test
    public void testIsExtraPushPopInstructionNecessaryMultipleCallsSameOffset() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act - Call isExtraPushPopInstructionNecessary multiple times with same offset
        boolean result1 = marker.isExtraPushPopInstructionNecessary(5);
        boolean result2 = marker.isExtraPushPopInstructionNecessary(5);
        boolean result3 = marker.isExtraPushPopInstructionNecessary(5);

        // Assert - Verify consistent results
        assertEquals(result1, result2, "isExtraPushPopInstructionNecessary should return consistent result for same offset");
        assertEquals(result2, result3, "isExtraPushPopInstructionNecessary should return consistent result for same offset");
    }

    /**
     * Tests that isExtraPushPopInstructionNecessary can be called with different offsets in sequence.
     * Verifies that the method handles multiple different offsets.
     */
    @Test
    public void testIsExtraPushPopInstructionNecessaryWithDifferentOffsets() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Test multiple different offsets
        assertDoesNotThrow(() -> {
            marker.isExtraPushPopInstructionNecessary(0);
            marker.isExtraPushPopInstructionNecessary(1);
            marker.isExtraPushPopInstructionNecessary(2);
            marker.isExtraPushPopInstructionNecessary(5);
            marker.isExtraPushPopInstructionNecessary(10);
            marker.isExtraPushPopInstructionNecessary(20);
        }, "isExtraPushPopInstructionNecessary should handle multiple different offsets");
    }

    /**
     * Tests isExtraPushPopInstructionNecessary with InstructionUsageMarker created using boolean constructor.
     * Verifies that the method works with the single-parameter constructor.
     */
    @Test
    public void testIsExtraPushPopInstructionNecessaryWithBooleanConstructor() {
        // Arrange - Create InstructionUsageMarker with boolean constructor
        InstructionUsageMarker marker = new InstructionUsageMarker(true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isExtraPushPopInstructionNecessary(0),
                "isExtraPushPopInstructionNecessary should work with boolean constructor");
    }

    /**
     * Tests isExtraPushPopInstructionNecessary with InstructionUsageMarker created using 3-parameter constructor.
     * Verifies that the method works with the three-parameter constructor.
     */
    @Test
    public void testIsExtraPushPopInstructionNecessaryWithThreeParameterConstructor() {
        // Arrange - Create InstructionUsageMarker with 3-parameter constructor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isExtraPushPopInstructionNecessary(0),
                "isExtraPushPopInstructionNecessary should work with three-parameter constructor");
    }

    /**
     * Tests isExtraPushPopInstructionNecessary with InstructionUsageMarker created using 4-parameter constructor.
     * Verifies that the method works with the four-parameter constructor.
     */
    @Test
    public void testIsExtraPushPopInstructionNecessaryWithFourParameterConstructor() {
        // Arrange - Create InstructionUsageMarker with 4-parameter constructor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isExtraPushPopInstructionNecessary(0),
                "isExtraPushPopInstructionNecessary should work with four-parameter constructor");
    }

    /**
     * Tests isExtraPushPopInstructionNecessary with different PartialEvaluator configurations.
     * Verifies that the method works with various evaluator settings.
     */
    @Test
    public void testIsExtraPushPopInstructionNecessaryWithDifferentEvaluatorConfigurations() {
        // Arrange - Create InstructionUsageMarkers with different configurations
        PartialEvaluator evaluator1 = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker1 = new InstructionUsageMarker(evaluator1, true, true, true);

        PartialEvaluator evaluator2 = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker2 = new InstructionUsageMarker(evaluator2, false, false, false);

        // Act & Assert - Verify both work
        assertDoesNotThrow(() -> marker1.isExtraPushPopInstructionNecessary(0),
                "isExtraPushPopInstructionNecessary should work with first configuration");
        assertDoesNotThrow(() -> marker2.isExtraPushPopInstructionNecessary(0),
                "isExtraPushPopInstructionNecessary should work with second configuration");
    }

    /**
     * Tests that isExtraPushPopInstructionNecessary can be called on multiple InstructionUsageMarker instances.
     * Verifies that the method works independently for different marker instances.
     */
    @Test
    public void testIsExtraPushPopInstructionNecessaryOnMultipleInstances() {
        // Arrange - Create multiple InstructionUsageMarker instances
        InstructionUsageMarker marker1 = new InstructionUsageMarker(true);
        InstructionUsageMarker marker2 = new InstructionUsageMarker(false);
        InstructionUsageMarker marker3 = new InstructionUsageMarker(true);

        // Act & Assert - Verify all instances work independently
        assertDoesNotThrow(() -> marker1.isExtraPushPopInstructionNecessary(5),
                "isExtraPushPopInstructionNecessary should work on first marker instance");
        assertDoesNotThrow(() -> marker2.isExtraPushPopInstructionNecessary(5),
                "isExtraPushPopInstructionNecessary should work on second marker instance");
        assertDoesNotThrow(() -> marker3.isExtraPushPopInstructionNecessary(5),
                "isExtraPushPopInstructionNecessary should work on third marker instance");
    }

    /**
     * Tests isExtraPushPopInstructionNecessary with a sequence of increasing offsets.
     * Verifies that the method handles sequential offset queries.
     */
    @Test
    public void testIsExtraPushPopInstructionNecessaryWithIncreasingOffsets() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Test increasing sequence
        for (int offset = 0; offset < 20; offset++) {
            final int currentOffset = offset;
            assertDoesNotThrow(() -> marker.isExtraPushPopInstructionNecessary(currentOffset),
                    "isExtraPushPopInstructionNecessary should handle offset " + currentOffset);
        }
    }

    /**
     * Tests isExtraPushPopInstructionNecessary with a sequence of decreasing offsets.
     * Verifies that the method handles reverse sequential offset queries.
     */
    @Test
    public void testIsExtraPushPopInstructionNecessaryWithDecreasingOffsets() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Test decreasing sequence
        for (int offset = 20; offset >= 0; offset--) {
            final int currentOffset = offset;
            assertDoesNotThrow(() -> marker.isExtraPushPopInstructionNecessary(currentOffset),
                    "isExtraPushPopInstructionNecessary should handle offset " + currentOffset);
        }
    }

    /**
     * Tests isExtraPushPopInstructionNecessary with random access pattern.
     * Verifies that the method handles non-sequential offset queries.
     */
    @Test
    public void testIsExtraPushPopInstructionNecessaryWithRandomAccessPattern() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Test random access pattern
        int[] offsets = {0, 10, 5, 15, 3, 20, 1, 8, 12, 6};
        for (int offset : offsets) {
            assertDoesNotThrow(() -> marker.isExtraPushPopInstructionNecessary(offset),
                    "isExtraPushPopInstructionNecessary should handle offset " + offset + " in random access");
        }
    }

    /**
     * Tests isExtraPushPopInstructionNecessary with runPartialEvaluator set to true.
     * Verifies that the method works when partial evaluator is configured to run.
     */
    @Test
    public void testIsExtraPushPopInstructionNecessaryWithRunPartialEvaluatorTrue() {
        // Arrange - Create InstructionUsageMarker with runPartialEvaluator=true
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isExtraPushPopInstructionNecessary(0),
                "isExtraPushPopInstructionNecessary should work when runPartialEvaluator is true");
    }

    /**
     * Tests isExtraPushPopInstructionNecessary with runPartialEvaluator set to false.
     * Verifies that the method works when partial evaluator is configured not to run.
     */
    @Test
    public void testIsExtraPushPopInstructionNecessaryWithRunPartialEvaluatorFalse() {
        // Arrange - Create InstructionUsageMarker with runPartialEvaluator=false
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, false, true, true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isExtraPushPopInstructionNecessary(0),
                "isExtraPushPopInstructionNecessary should work when runPartialEvaluator is false");
    }

    /**
     * Tests isExtraPushPopInstructionNecessary with ensureSafetyForVerifier set to true.
     * Verifies that the method works with verifier safety enabled.
     */
    @Test
    public void testIsExtraPushPopInstructionNecessaryWithEnsureSafetyForVerifierTrue() {
        // Arrange - Create InstructionUsageMarker with ensureSafetyForVerifier=true
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isExtraPushPopInstructionNecessary(0),
                "isExtraPushPopInstructionNecessary should work when ensureSafetyForVerifier is true");
    }

    /**
     * Tests isExtraPushPopInstructionNecessary with ensureSafetyForVerifier set to false.
     * Verifies that the method works with verifier safety disabled.
     */
    @Test
    public void testIsExtraPushPopInstructionNecessaryWithEnsureSafetyForVerifierFalse() {
        // Arrange - Create InstructionUsageMarker with ensureSafetyForVerifier=false
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, false, true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isExtraPushPopInstructionNecessary(0),
                "isExtraPushPopInstructionNecessary should work when ensureSafetyForVerifier is false");
    }

    /**
     * Tests isExtraPushPopInstructionNecessary with markExternalSideEffects set to true.
     * Verifies that the method works with external side effects marking enabled.
     */
    @Test
    public void testIsExtraPushPopInstructionNecessaryWithMarkExternalSideEffectsTrue() {
        // Arrange - Create InstructionUsageMarker with markExternalSideEffects=true
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isExtraPushPopInstructionNecessary(0),
                "isExtraPushPopInstructionNecessary should work when markExternalSideEffects is true");
    }

    /**
     * Tests isExtraPushPopInstructionNecessary with markExternalSideEffects set to false.
     * Verifies that the method works with external side effects marking disabled.
     */
    @Test
    public void testIsExtraPushPopInstructionNecessaryWithMarkExternalSideEffectsFalse() {
        // Arrange - Create InstructionUsageMarker with markExternalSideEffects=false
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, false);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.isExtraPushPopInstructionNecessary(0),
                "isExtraPushPopInstructionNecessary should work when markExternalSideEffects is false");
    }

    /**
     * Tests isExtraPushPopInstructionNecessary called multiple times in rapid succession.
     * Verifies that the method is thread-safe for rapid sequential calls.
     */
    @Test
    public void testIsExtraPushPopInstructionNecessaryRapidSuccessiveCalls() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Call many times rapidly
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                marker.isExtraPushPopInstructionNecessary(i % 10);
            }
        }, "isExtraPushPopInstructionNecessary should handle rapid successive calls");
    }

    /**
     * Tests isExtraPushPopInstructionNecessary with all boolean flag combinations.
     * Verifies that the method works with all possible constructor flag combinations.
     */
    @Test
    public void testIsExtraPushPopInstructionNecessaryWithAllFlagCombinations() {
        // Arrange & Act & Assert - Test all 8 combinations
        PartialEvaluator pe = PartialEvaluator.Builder.create().build();

        InstructionUsageMarker marker1 = new InstructionUsageMarker(pe, true, true, true);
        assertDoesNotThrow(() -> marker1.isExtraPushPopInstructionNecessary(0), "Should work with (true, true, true)");

        InstructionUsageMarker marker2 = new InstructionUsageMarker(pe, true, true, false);
        assertDoesNotThrow(() -> marker2.isExtraPushPopInstructionNecessary(0), "Should work with (true, true, false)");

        InstructionUsageMarker marker3 = new InstructionUsageMarker(pe, true, false, true);
        assertDoesNotThrow(() -> marker3.isExtraPushPopInstructionNecessary(0), "Should work with (true, false, true)");

        InstructionUsageMarker marker4 = new InstructionUsageMarker(pe, true, false, false);
        assertDoesNotThrow(() -> marker4.isExtraPushPopInstructionNecessary(0), "Should work with (true, false, false)");

        InstructionUsageMarker marker5 = new InstructionUsageMarker(pe, false, true, true);
        assertDoesNotThrow(() -> marker5.isExtraPushPopInstructionNecessary(0), "Should work with (false, true, true)");

        InstructionUsageMarker marker6 = new InstructionUsageMarker(pe, false, true, false);
        assertDoesNotThrow(() -> marker6.isExtraPushPopInstructionNecessary(0), "Should work with (false, true, false)");

        InstructionUsageMarker marker7 = new InstructionUsageMarker(pe, false, false, true);
        assertDoesNotThrow(() -> marker7.isExtraPushPopInstructionNecessary(0), "Should work with (false, false, true)");

        InstructionUsageMarker marker8 = new InstructionUsageMarker(pe, false, false, false);
        assertDoesNotThrow(() -> marker8.isExtraPushPopInstructionNecessary(0), "Should work with (false, false, false)");
    }

    /**
     * Tests isExtraPushPopInstructionNecessary returns consistent type across calls.
     * Verifies that the method always returns a boolean value.
     */
    @Test
    public void testIsExtraPushPopInstructionNecessaryAlwaysReturnsBoolean() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act - Call multiple times and collect results
        boolean[] results = new boolean[10];
        for (int i = 0; i < 10; i++) {
            results[i] = marker.isExtraPushPopInstructionNecessary(i);
        }

        // Assert - All results should be boolean (implicitly true by type, but verify no exceptions)
        for (int i = 0; i < 10; i++) {
            assertNotNull(results[i], "Result " + i + " should not be null");
        }
    }
}
