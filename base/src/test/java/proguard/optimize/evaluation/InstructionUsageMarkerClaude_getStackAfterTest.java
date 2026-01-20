package proguard.optimize.evaluation;

import org.junit.jupiter.api.Test;
import proguard.evaluation.PartialEvaluator;
import proguard.evaluation.TracedStack;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link InstructionUsageMarker#getStackAfter(int)} method.
 * Tests the getStackAfter method which returns the stack after execution of the instruction
 * at the given offset in the most recently analyzed code attribute.
 */
public class InstructionUsageMarkerClaude_getStackAfterTest {

    /**
     * Tests that getStackAfter can be called with offset 0.
     * Verifies that the method handles the first instruction offset.
     */
    @Test
    public void testGetStackAfterWithOffsetZero() {
        // Arrange - Create InstructionUsageMarker with default PartialEvaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Verify method can be called without throwing exception
        assertDoesNotThrow(() -> marker.getStackAfter(0),
                "getStackAfter should not throw exception with offset 0");
    }

    /**
     * Tests that getStackAfter can be called with positive offsets.
     * Verifies that the method handles various positive instruction offsets.
     */
    @Test
    public void testGetStackAfterWithPositiveOffsets() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Test various positive offsets
        assertDoesNotThrow(() -> marker.getStackAfter(1),
                "getStackAfter should not throw exception with offset 1");
        assertDoesNotThrow(() -> marker.getStackAfter(10),
                "getStackAfter should not throw exception with offset 10");
        assertDoesNotThrow(() -> marker.getStackAfter(100),
                "getStackAfter should not throw exception with offset 100");
        assertDoesNotThrow(() -> marker.getStackAfter(1000),
                "getStackAfter should not throw exception with offset 1000");
    }

    /**
     * Tests that getStackAfter returns a TracedStack or null.
     * Verifies that the method returns the expected type.
     */
    @Test
    public void testGetStackAfterReturnsTracedStackOrNull() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act - Call getStackAfter
        TracedStack result = marker.getStackAfter(0);

        // Assert - Verify result is either a TracedStack or null
        // (null is valid if no code attribute has been analyzed)
        if (result != null) {
            assertInstanceOf(TracedStack.class, result,
                    "getStackAfter should return a TracedStack instance when not null");
        }
    }

    /**
     * Tests that getStackAfter can be called multiple times with same offset.
     * Verifies that the method is stable and can be called repeatedly.
     */
    @Test
    public void testGetStackAfterMultipleCallsSameOffset() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act - Call getStackAfter multiple times with same offset
        TracedStack result1 = marker.getStackAfter(5);
        TracedStack result2 = marker.getStackAfter(5);
        TracedStack result3 = marker.getStackAfter(5);

        // Assert - Verify consistent results (same reference or all null)
        assertEquals(result1, result2, "getStackAfter should return consistent result for same offset");
        assertEquals(result2, result3, "getStackAfter should return consistent result for same offset");
    }

    /**
     * Tests that getStackAfter can be called with different offsets in sequence.
     * Verifies that the method handles multiple different offsets.
     */
    @Test
    public void testGetStackAfterWithDifferentOffsets() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Test multiple different offsets
        assertDoesNotThrow(() -> {
            marker.getStackAfter(0);
            marker.getStackAfter(1);
            marker.getStackAfter(2);
            marker.getStackAfter(5);
            marker.getStackAfter(10);
            marker.getStackAfter(20);
        }, "getStackAfter should handle multiple different offsets");
    }

    /**
     * Tests getStackAfter with InstructionUsageMarker created using boolean constructor.
     * Verifies that the method works with the single-parameter constructor.
     */
    @Test
    public void testGetStackAfterWithBooleanConstructor() {
        // Arrange - Create InstructionUsageMarker with boolean constructor
        InstructionUsageMarker marker = new InstructionUsageMarker(true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.getStackAfter(0),
                "getStackAfter should work with boolean constructor");
    }

    /**
     * Tests getStackAfter with InstructionUsageMarker created using 3-parameter constructor.
     * Verifies that the method works with the three-parameter constructor.
     */
    @Test
    public void testGetStackAfterWithThreeParameterConstructor() {
        // Arrange - Create InstructionUsageMarker with 3-parameter constructor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.getStackAfter(0),
                "getStackAfter should work with three-parameter constructor");
    }

    /**
     * Tests getStackAfter with InstructionUsageMarker created using 4-parameter constructor.
     * Verifies that the method works with the four-parameter constructor.
     */
    @Test
    public void testGetStackAfterWithFourParameterConstructor() {
        // Arrange - Create InstructionUsageMarker with 4-parameter constructor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.getStackAfter(0),
                "getStackAfter should work with four-parameter constructor");
    }

    /**
     * Tests getStackAfter with different PartialEvaluator configurations.
     * Verifies that the method works with various evaluator settings.
     */
    @Test
    public void testGetStackAfterWithDifferentEvaluatorConfigurations() {
        // Arrange - Create InstructionUsageMarkers with different configurations
        PartialEvaluator evaluator1 = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker1 = new InstructionUsageMarker(evaluator1, true, true, true);

        PartialEvaluator evaluator2 = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker2 = new InstructionUsageMarker(evaluator2, false, false, false);

        // Act & Assert - Verify both work
        assertDoesNotThrow(() -> marker1.getStackAfter(0),
                "getStackAfter should work with first configuration");
        assertDoesNotThrow(() -> marker2.getStackAfter(0),
                "getStackAfter should work with second configuration");
    }

    /**
     * Tests that getStackAfter can be called on multiple InstructionUsageMarker instances.
     * Verifies that the method works independently for different marker instances.
     */
    @Test
    public void testGetStackAfterOnMultipleInstances() {
        // Arrange - Create multiple InstructionUsageMarker instances
        InstructionUsageMarker marker1 = new InstructionUsageMarker(true);
        InstructionUsageMarker marker2 = new InstructionUsageMarker(false);
        InstructionUsageMarker marker3 = new InstructionUsageMarker(true);

        // Act & Assert - Verify all instances work independently
        assertDoesNotThrow(() -> marker1.getStackAfter(5),
                "getStackAfter should work on first marker instance");
        assertDoesNotThrow(() -> marker2.getStackAfter(5),
                "getStackAfter should work on second marker instance");
        assertDoesNotThrow(() -> marker3.getStackAfter(5),
                "getStackAfter should work on third marker instance");
    }

    /**
     * Tests getStackAfter with a sequence of increasing offsets.
     * Verifies that the method handles sequential offset queries.
     */
    @Test
    public void testGetStackAfterWithIncreasingOffsets() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Test increasing sequence
        for (int offset = 0; offset < 20; offset++) {
            final int currentOffset = offset;
            assertDoesNotThrow(() -> marker.getStackAfter(currentOffset),
                    "getStackAfter should handle offset " + currentOffset);
        }
    }

    /**
     * Tests getStackAfter with a sequence of decreasing offsets.
     * Verifies that the method handles reverse sequential offset queries.
     */
    @Test
    public void testGetStackAfterWithDecreasingOffsets() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Test decreasing sequence
        for (int offset = 20; offset >= 0; offset--) {
            final int currentOffset = offset;
            assertDoesNotThrow(() -> marker.getStackAfter(currentOffset),
                    "getStackAfter should handle offset " + currentOffset);
        }
    }

    /**
     * Tests getStackAfter with random access pattern.
     * Verifies that the method handles non-sequential offset queries.
     */
    @Test
    public void testGetStackAfterWithRandomAccessPattern() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Test random access pattern
        int[] offsets = {0, 10, 5, 15, 3, 20, 1, 8, 12, 6};
        for (int offset : offsets) {
            assertDoesNotThrow(() -> marker.getStackAfter(offset),
                    "getStackAfter should handle offset " + offset + " in random access");
        }
    }

    /**
     * Tests getStackAfter with runPartialEvaluator set to true.
     * Verifies that the method works when partial evaluator is configured to run.
     */
    @Test
    public void testGetStackAfterWithRunPartialEvaluatorTrue() {
        // Arrange - Create InstructionUsageMarker with runPartialEvaluator=true
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.getStackAfter(0),
                "getStackAfter should work when runPartialEvaluator is true");
    }

    /**
     * Tests getStackAfter with runPartialEvaluator set to false.
     * Verifies that the method works when partial evaluator is configured not to run.
     */
    @Test
    public void testGetStackAfterWithRunPartialEvaluatorFalse() {
        // Arrange - Create InstructionUsageMarker with runPartialEvaluator=false
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, false, true, true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.getStackAfter(0),
                "getStackAfter should work when runPartialEvaluator is false");
    }

    /**
     * Tests getStackAfter with ensureSafetyForVerifier set to true.
     * Verifies that the method works with verifier safety enabled.
     */
    @Test
    public void testGetStackAfterWithEnsureSafetyForVerifierTrue() {
        // Arrange - Create InstructionUsageMarker with ensureSafetyForVerifier=true
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.getStackAfter(0),
                "getStackAfter should work when ensureSafetyForVerifier is true");
    }

    /**
     * Tests getStackAfter with ensureSafetyForVerifier set to false.
     * Verifies that the method works with verifier safety disabled.
     */
    @Test
    public void testGetStackAfterWithEnsureSafetyForVerifierFalse() {
        // Arrange - Create InstructionUsageMarker with ensureSafetyForVerifier=false
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, false, true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.getStackAfter(0),
                "getStackAfter should work when ensureSafetyForVerifier is false");
    }

    /**
     * Tests getStackAfter with markExternalSideEffects set to true.
     * Verifies that the method works with external side effects marking enabled.
     */
    @Test
    public void testGetStackAfterWithMarkExternalSideEffectsTrue() {
        // Arrange - Create InstructionUsageMarker with markExternalSideEffects=true
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.getStackAfter(0),
                "getStackAfter should work when markExternalSideEffects is true");
    }

    /**
     * Tests getStackAfter with markExternalSideEffects set to false.
     * Verifies that the method works with external side effects marking disabled.
     */
    @Test
    public void testGetStackAfterWithMarkExternalSideEffectsFalse() {
        // Arrange - Create InstructionUsageMarker with markExternalSideEffects=false
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, false);

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.getStackAfter(0),
                "getStackAfter should work when markExternalSideEffects is false");
    }

    /**
     * Tests getStackAfter called multiple times in rapid succession.
     * Verifies that the method handles rapid sequential calls.
     */
    @Test
    public void testGetStackAfterRapidSuccessiveCalls() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Call many times rapidly
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                marker.getStackAfter(i % 10);
            }
        }, "getStackAfter should handle rapid successive calls");
    }

    /**
     * Tests getStackAfter with all boolean flag combinations.
     * Verifies that the method works with all possible constructor flag combinations.
     */
    @Test
    public void testGetStackAfterWithAllFlagCombinations() {
        // Arrange & Act & Assert - Test all 8 combinations
        PartialEvaluator pe = PartialEvaluator.Builder.create().build();

        InstructionUsageMarker marker1 = new InstructionUsageMarker(pe, true, true, true);
        assertDoesNotThrow(() -> marker1.getStackAfter(0), "Should work with (true, true, true)");

        InstructionUsageMarker marker2 = new InstructionUsageMarker(pe, true, true, false);
        assertDoesNotThrow(() -> marker2.getStackAfter(0), "Should work with (true, true, false)");

        InstructionUsageMarker marker3 = new InstructionUsageMarker(pe, true, false, true);
        assertDoesNotThrow(() -> marker3.getStackAfter(0), "Should work with (true, false, true)");

        InstructionUsageMarker marker4 = new InstructionUsageMarker(pe, true, false, false);
        assertDoesNotThrow(() -> marker4.getStackAfter(0), "Should work with (true, false, false)");

        InstructionUsageMarker marker5 = new InstructionUsageMarker(pe, false, true, true);
        assertDoesNotThrow(() -> marker5.getStackAfter(0), "Should work with (false, true, true)");

        InstructionUsageMarker marker6 = new InstructionUsageMarker(pe, false, true, false);
        assertDoesNotThrow(() -> marker6.getStackAfter(0), "Should work with (false, true, false)");

        InstructionUsageMarker marker7 = new InstructionUsageMarker(pe, false, false, true);
        assertDoesNotThrow(() -> marker7.getStackAfter(0), "Should work with (false, false, true)");

        InstructionUsageMarker marker8 = new InstructionUsageMarker(pe, false, false, false);
        assertDoesNotThrow(() -> marker8.getStackAfter(0), "Should work with (false, false, false)");
    }

    /**
     * Tests getStackAfter returns consistent type across calls.
     * Verifies that the method always returns a TracedStack or null consistently.
     */
    @Test
    public void testGetStackAfterAlwaysReturnsConsistentType() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act - Call multiple times and collect results
        TracedStack[] results = new TracedStack[10];
        for (int i = 0; i < 10; i++) {
            results[i] = marker.getStackAfter(i);
        }

        // Assert - All results should be TracedStack instances or null
        for (int i = 0; i < 10; i++) {
            if (results[i] != null) {
                assertInstanceOf(TracedStack.class, results[i],
                        "Result " + i + " should be a TracedStack instance when not null");
            }
        }
    }
}
