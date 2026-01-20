package proguard.optimize.evaluation;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.evaluation.PartialEvaluator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link InstructionUsageMarker#tracedInstructionFilter(InstructionVisitor)} method.
 * Tests the tracedInstructionFilter method which returns a filtering version of the given
 * instruction visitor that only visits traced instructions.
 */
public class InstructionUsageMarkerClaude_tracedInstructionFilterTest {

    /**
     * Tests that tracedInstructionFilter returns a non-null result with a non-null visitor.
     * Verifies that the method returns a valid InstructionVisitor.
     */
    @Test
    public void testTracedInstructionFilterReturnsNonNull() {
        // Arrange - Create InstructionUsageMarker and a visitor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Get filtered visitor
        InstructionVisitor filtered = marker.tracedInstructionFilter(visitor);

        // Assert - Verify non-null result
        assertNotNull(filtered, "tracedInstructionFilter should return a non-null InstructionVisitor");
    }

    /**
     * Tests that tracedInstructionFilter works with null visitor.
     * Verifies that the method handles null input gracefully.
     */
    @Test
    public void testTracedInstructionFilterWithNullVisitor() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Verify method can be called with null
        assertDoesNotThrow(() -> marker.tracedInstructionFilter(null),
                "tracedInstructionFilter should handle null visitor");
    }

    /**
     * Tests that tracedInstructionFilter returns an InstructionVisitor.
     * Verifies that the result is of the correct type.
     */
    @Test
    public void testTracedInstructionFilterReturnsInstructionVisitor() {
        // Arrange - Create InstructionUsageMarker and visitor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Get filtered visitor
        InstructionVisitor filtered = marker.tracedInstructionFilter(visitor);

        // Assert - Verify it's an InstructionVisitor
        assertInstanceOf(InstructionVisitor.class, filtered,
                "tracedInstructionFilter should return an InstructionVisitor");
    }

    /**
     * Tests that tracedInstructionFilter can be called multiple times with same visitor.
     * Verifies that the method is stable and can be called repeatedly.
     */
    @Test
    public void testTracedInstructionFilterMultipleCallsSameVisitor() {
        // Arrange - Create InstructionUsageMarker and visitor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Call multiple times with same visitor
        InstructionVisitor filtered1 = marker.tracedInstructionFilter(visitor);
        InstructionVisitor filtered2 = marker.tracedInstructionFilter(visitor);
        InstructionVisitor filtered3 = marker.tracedInstructionFilter(visitor);

        // Assert - Verify all results are non-null
        assertNotNull(filtered1, "First filtered visitor should not be null");
        assertNotNull(filtered2, "Second filtered visitor should not be null");
        assertNotNull(filtered3, "Third filtered visitor should not be null");
    }

    /**
     * Tests that tracedInstructionFilter works with different visitor implementations.
     * Verifies that the method handles various visitor types.
     */
    @Test
    public void testTracedInstructionFilterWithDifferentVisitors() {
        // Arrange - Create InstructionUsageMarker and different visitors
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        InstructionVisitor visitor1 = new TestInstructionVisitor();
        InstructionVisitor visitor2 = new CountingInstructionVisitor();

        // Act - Get filtered visitors
        InstructionVisitor filtered1 = marker.tracedInstructionFilter(visitor1);
        InstructionVisitor filtered2 = marker.tracedInstructionFilter(visitor2);

        // Assert - Verify both work
        assertNotNull(filtered1, "Filtered visitor 1 should not be null");
        assertNotNull(filtered2, "Filtered visitor 2 should not be null");
    }

    /**
     * Tests tracedInstructionFilter with InstructionUsageMarker created using boolean constructor.
     * Verifies that the method works with the single-parameter constructor.
     */
    @Test
    public void testTracedInstructionFilterWithBooleanConstructor() {
        // Arrange - Create InstructionUsageMarker with boolean constructor
        InstructionUsageMarker marker = new InstructionUsageMarker(true);
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Get filtered visitor
        InstructionVisitor filtered = marker.tracedInstructionFilter(visitor);

        // Assert - Verify result is non-null
        assertNotNull(filtered, "tracedInstructionFilter should work with boolean constructor");
    }

    /**
     * Tests tracedInstructionFilter with InstructionUsageMarker created using 3-parameter constructor.
     * Verifies that the method works with the three-parameter constructor.
     */
    @Test
    public void testTracedInstructionFilterWithThreeParameterConstructor() {
        // Arrange - Create InstructionUsageMarker with 3-parameter constructor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true);
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Get filtered visitor
        InstructionVisitor filtered = marker.tracedInstructionFilter(visitor);

        // Assert - Verify result is non-null
        assertNotNull(filtered, "tracedInstructionFilter should work with three-parameter constructor");
    }

    /**
     * Tests tracedInstructionFilter with InstructionUsageMarker created using 4-parameter constructor.
     * Verifies that the method works with the four-parameter constructor.
     */
    @Test
    public void testTracedInstructionFilterWithFourParameterConstructor() {
        // Arrange - Create InstructionUsageMarker with 4-parameter constructor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Get filtered visitor
        InstructionVisitor filtered = marker.tracedInstructionFilter(visitor);

        // Assert - Verify result is non-null
        assertNotNull(filtered, "tracedInstructionFilter should work with four-parameter constructor");
    }

    /**
     * Tests tracedInstructionFilter with different PartialEvaluator configurations.
     * Verifies that the method works with various evaluator settings.
     */
    @Test
    public void testTracedInstructionFilterWithDifferentEvaluatorConfigurations() {
        // Arrange - Create InstructionUsageMarkers with different configurations
        PartialEvaluator evaluator1 = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker1 = new InstructionUsageMarker(evaluator1, true, true, true);

        PartialEvaluator evaluator2 = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker2 = new InstructionUsageMarker(evaluator2, false, false, false);

        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Get filtered visitors
        InstructionVisitor filtered1 = marker1.tracedInstructionFilter(visitor);
        InstructionVisitor filtered2 = marker2.tracedInstructionFilter(visitor);

        // Assert - Verify both work
        assertNotNull(filtered1, "tracedInstructionFilter should work with first configuration");
        assertNotNull(filtered2, "tracedInstructionFilter should work with second configuration");
    }

    /**
     * Tests that tracedInstructionFilter works on multiple InstructionUsageMarker instances.
     * Verifies that the method works independently for different marker instances.
     */
    @Test
    public void testTracedInstructionFilterOnMultipleInstances() {
        // Arrange - Create multiple InstructionUsageMarker instances
        InstructionUsageMarker marker1 = new InstructionUsageMarker(true);
        InstructionUsageMarker marker2 = new InstructionUsageMarker(false);
        InstructionUsageMarker marker3 = new InstructionUsageMarker(true);
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Get filtered visitors from all instances
        InstructionVisitor filtered1 = marker1.tracedInstructionFilter(visitor);
        InstructionVisitor filtered2 = marker2.tracedInstructionFilter(visitor);
        InstructionVisitor filtered3 = marker3.tracedInstructionFilter(visitor);

        // Assert - Verify all instances work independently
        assertNotNull(filtered1, "tracedInstructionFilter should work on first marker instance");
        assertNotNull(filtered2, "tracedInstructionFilter should work on second marker instance");
        assertNotNull(filtered3, "tracedInstructionFilter should work on third marker instance");
    }

    /**
     * Tests tracedInstructionFilter with runPartialEvaluator set to true.
     * Verifies that the method works when partial evaluator is configured to run.
     */
    @Test
    public void testTracedInstructionFilterWithRunPartialEvaluatorTrue() {
        // Arrange - Create InstructionUsageMarker with runPartialEvaluator=true
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Get filtered visitor
        InstructionVisitor filtered = marker.tracedInstructionFilter(visitor);

        // Assert - Verify result is non-null
        assertNotNull(filtered, "tracedInstructionFilter should work when runPartialEvaluator is true");
    }

    /**
     * Tests tracedInstructionFilter with runPartialEvaluator set to false.
     * Verifies that the method works when partial evaluator is configured not to run.
     */
    @Test
    public void testTracedInstructionFilterWithRunPartialEvaluatorFalse() {
        // Arrange - Create InstructionUsageMarker with runPartialEvaluator=false
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, false, true, true);
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Get filtered visitor
        InstructionVisitor filtered = marker.tracedInstructionFilter(visitor);

        // Assert - Verify result is non-null
        assertNotNull(filtered, "tracedInstructionFilter should work when runPartialEvaluator is false");
    }

    /**
     * Tests tracedInstructionFilter with ensureSafetyForVerifier set to true.
     * Verifies that the method works with verifier safety enabled.
     */
    @Test
    public void testTracedInstructionFilterWithEnsureSafetyForVerifierTrue() {
        // Arrange - Create InstructionUsageMarker with ensureSafetyForVerifier=true
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Get filtered visitor
        InstructionVisitor filtered = marker.tracedInstructionFilter(visitor);

        // Assert - Verify result is non-null
        assertNotNull(filtered, "tracedInstructionFilter should work when ensureSafetyForVerifier is true");
    }

    /**
     * Tests tracedInstructionFilter with ensureSafetyForVerifier set to false.
     * Verifies that the method works with verifier safety disabled.
     */
    @Test
    public void testTracedInstructionFilterWithEnsureSafetyForVerifierFalse() {
        // Arrange - Create InstructionUsageMarker with ensureSafetyForVerifier=false
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, false, true);
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Get filtered visitor
        InstructionVisitor filtered = marker.tracedInstructionFilter(visitor);

        // Assert - Verify result is non-null
        assertNotNull(filtered, "tracedInstructionFilter should work when ensureSafetyForVerifier is false");
    }

    /**
     * Tests tracedInstructionFilter with markExternalSideEffects set to true.
     * Verifies that the method works with external side effects marking enabled.
     */
    @Test
    public void testTracedInstructionFilterWithMarkExternalSideEffectsTrue() {
        // Arrange - Create InstructionUsageMarker with markExternalSideEffects=true
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Get filtered visitor
        InstructionVisitor filtered = marker.tracedInstructionFilter(visitor);

        // Assert - Verify result is non-null
        assertNotNull(filtered, "tracedInstructionFilter should work when markExternalSideEffects is true");
    }

    /**
     * Tests tracedInstructionFilter with markExternalSideEffects set to false.
     * Verifies that the method works with external side effects marking disabled.
     */
    @Test
    public void testTracedInstructionFilterWithMarkExternalSideEffectsFalse() {
        // Arrange - Create InstructionUsageMarker with markExternalSideEffects=false
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, false);
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Get filtered visitor
        InstructionVisitor filtered = marker.tracedInstructionFilter(visitor);

        // Assert - Verify result is non-null
        assertNotNull(filtered, "tracedInstructionFilter should work when markExternalSideEffects is false");
    }

    /**
     * Tests tracedInstructionFilter called multiple times in rapid succession.
     * Verifies that the method is stable for rapid sequential calls.
     */
    @Test
    public void testTracedInstructionFilterRapidSuccessiveCalls() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Call many times rapidly
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                InstructionVisitor visitor = new TestInstructionVisitor();
                InstructionVisitor filtered = marker.tracedInstructionFilter(visitor);
                assertNotNull(filtered, "Filtered visitor " + i + " should not be null");
            }
        }, "tracedInstructionFilter should handle rapid successive calls");
    }

    /**
     * Tests tracedInstructionFilter with all boolean flag combinations.
     * Verifies that the method works with all possible constructor flag combinations.
     */
    @Test
    public void testTracedInstructionFilterWithAllFlagCombinations() {
        // Arrange - Create visitor
        InstructionVisitor visitor = new TestInstructionVisitor();
        PartialEvaluator pe = PartialEvaluator.Builder.create().build();

        // Act & Assert - Test all 8 combinations
        InstructionUsageMarker marker1 = new InstructionUsageMarker(pe, true, true, true);
        assertNotNull(marker1.tracedInstructionFilter(visitor), "Should work with (true, true, true)");

        InstructionUsageMarker marker2 = new InstructionUsageMarker(pe, true, true, false);
        assertNotNull(marker2.tracedInstructionFilter(visitor), "Should work with (true, true, false)");

        InstructionUsageMarker marker3 = new InstructionUsageMarker(pe, true, false, true);
        assertNotNull(marker3.tracedInstructionFilter(visitor), "Should work with (true, false, true)");

        InstructionUsageMarker marker4 = new InstructionUsageMarker(pe, true, false, false);
        assertNotNull(marker4.tracedInstructionFilter(visitor), "Should work with (true, false, false)");

        InstructionUsageMarker marker5 = new InstructionUsageMarker(pe, false, true, true);
        assertNotNull(marker5.tracedInstructionFilter(visitor), "Should work with (false, true, true)");

        InstructionUsageMarker marker6 = new InstructionUsageMarker(pe, false, true, false);
        assertNotNull(marker6.tracedInstructionFilter(visitor), "Should work with (false, true, false)");

        InstructionUsageMarker marker7 = new InstructionUsageMarker(pe, false, false, true);
        assertNotNull(marker7.tracedInstructionFilter(visitor), "Should work with (false, false, true)");

        InstructionUsageMarker marker8 = new InstructionUsageMarker(pe, false, false, false);
        assertNotNull(marker8.tracedInstructionFilter(visitor), "Should work with (false, false, false)");
    }

    /**
     * Tests that tracedInstructionFilter can handle the same visitor being filtered multiple times.
     * Verifies that a visitor can be wrapped multiple times.
     */
    @Test
    public void testTracedInstructionFilterSameVisitorMultipleTimes() {
        // Arrange - Create InstructionUsageMarker and visitor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Filter the same visitor multiple times
        InstructionVisitor filtered1 = marker.tracedInstructionFilter(visitor);
        InstructionVisitor filtered2 = marker.tracedInstructionFilter(visitor);
        InstructionVisitor filtered3 = marker.tracedInstructionFilter(visitor);

        // Assert - All results should be non-null (they may or may not be the same instance)
        assertNotNull(filtered1, "First filtered visitor should not be null");
        assertNotNull(filtered2, "Second filtered visitor should not be null");
        assertNotNull(filtered3, "Third filtered visitor should not be null");
    }

    /**
     * Tests that tracedInstructionFilter can wrap already-filtered visitors.
     * Verifies that filtering can be chained.
     */
    @Test
    public void testTracedInstructionFilterChaining() {
        // Arrange - Create InstructionUsageMarker and visitor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Chain filtering
        InstructionVisitor filtered1 = marker.tracedInstructionFilter(visitor);
        InstructionVisitor filtered2 = marker.tracedInstructionFilter(filtered1);

        // Assert - Both should be non-null
        assertNotNull(filtered1, "First filtered visitor should not be null");
        assertNotNull(filtered2, "Second (chained) filtered visitor should not be null");
    }

    /**
     * Tests tracedInstructionFilter with a visitor that extends InstructionVisitor.
     * Verifies that the method works with visitor subclasses.
     */
    @Test
    public void testTracedInstructionFilterWithVisitorSubclass() {
        // Arrange - Create InstructionUsageMarker and visitor subclass
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        InstructionVisitor visitor = new CountingInstructionVisitor();

        // Act - Get filtered visitor
        InstructionVisitor filtered = marker.tracedInstructionFilter(visitor);

        // Assert - Verify result is non-null
        assertNotNull(filtered, "tracedInstructionFilter should work with visitor subclass");
    }

    /**
     * Tests that tracedInstructionFilter does not throw exceptions with valid parameters.
     * Verifies exception-free operation.
     */
    @Test
    public void testTracedInstructionFilterDoesNotThrowException() {
        // Arrange - Create InstructionUsageMarker and visitor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act & Assert - Verify no exception is thrown
        assertDoesNotThrow(() -> marker.tracedInstructionFilter(visitor),
                "tracedInstructionFilter should not throw exception with valid parameters");
    }

    // ========== Tests for tracedInstructionFilter(boolean, InstructionVisitor) method ==========

    /**
     * Tests the two-parameter tracedInstructionFilter with traced=true.
     * Verifies that the method returns a non-null result when filtering for traced instructions.
     */
    @Test
    public void testTwoParameterTracedInstructionFilterWithTracedTrue() {
        // Arrange - Create InstructionUsageMarker and visitor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Get filtered visitor for traced instructions
        InstructionVisitor filtered = marker.tracedInstructionFilter(true, visitor);

        // Assert - Verify non-null result
        assertNotNull(filtered, "tracedInstructionFilter(true, visitor) should return a non-null InstructionVisitor");
    }

    /**
     * Tests the two-parameter tracedInstructionFilter with traced=false.
     * Verifies that the method returns a non-null result when filtering for untraced instructions.
     */
    @Test
    public void testTwoParameterTracedInstructionFilterWithTracedFalse() {
        // Arrange - Create InstructionUsageMarker and visitor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Get filtered visitor for untraced instructions
        InstructionVisitor filtered = marker.tracedInstructionFilter(false, visitor);

        // Assert - Verify non-null result
        assertNotNull(filtered, "tracedInstructionFilter(false, visitor) should return a non-null InstructionVisitor");
    }

    /**
     * Tests two-parameter tracedInstructionFilter with null visitor and traced=true.
     * Verifies that the method handles null input gracefully.
     */
    @Test
    public void testTwoParameterTracedInstructionFilterWithNullVisitorTracedTrue() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Verify method can be called with null
        assertDoesNotThrow(() -> marker.tracedInstructionFilter(true, null),
                "tracedInstructionFilter(true, null) should handle null visitor");
    }

    /**
     * Tests two-parameter tracedInstructionFilter with null visitor and traced=false.
     * Verifies that the method handles null input gracefully for untraced filter.
     */
    @Test
    public void testTwoParameterTracedInstructionFilterWithNullVisitorTracedFalse() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Verify method can be called with null
        assertDoesNotThrow(() -> marker.tracedInstructionFilter(false, null),
                "tracedInstructionFilter(false, null) should handle null visitor");
    }

    /**
     * Tests that two-parameter tracedInstructionFilter returns an InstructionVisitor.
     * Verifies that the result is of the correct type.
     */
    @Test
    public void testTwoParameterTracedInstructionFilterReturnsInstructionVisitor() {
        // Arrange - Create InstructionUsageMarker and visitor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Get filtered visitors for both traced values
        InstructionVisitor filteredTrue = marker.tracedInstructionFilter(true, visitor);
        InstructionVisitor filteredFalse = marker.tracedInstructionFilter(false, visitor);

        // Assert - Verify both are InstructionVisitors
        assertInstanceOf(InstructionVisitor.class, filteredTrue,
                "tracedInstructionFilter(true, visitor) should return an InstructionVisitor");
        assertInstanceOf(InstructionVisitor.class, filteredFalse,
                "tracedInstructionFilter(false, visitor) should return an InstructionVisitor");
    }

    /**
     * Tests two-parameter tracedInstructionFilter can be called multiple times with same parameters.
     * Verifies that the method is stable and can be called repeatedly.
     */
    @Test
    public void testTwoParameterTracedInstructionFilterMultipleCallsSameParameters() {
        // Arrange - Create InstructionUsageMarker and visitor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Call multiple times with same parameters
        InstructionVisitor filtered1 = marker.tracedInstructionFilter(true, visitor);
        InstructionVisitor filtered2 = marker.tracedInstructionFilter(true, visitor);
        InstructionVisitor filtered3 = marker.tracedInstructionFilter(true, visitor);

        // Assert - Verify all results are non-null
        assertNotNull(filtered1, "First filtered visitor should not be null");
        assertNotNull(filtered2, "Second filtered visitor should not be null");
        assertNotNull(filtered3, "Third filtered visitor should not be null");
    }

    /**
     * Tests two-parameter tracedInstructionFilter with different traced flag values.
     * Verifies that the method handles both true and false traced flags.
     */
    @Test
    public void testTwoParameterTracedInstructionFilterWithDifferentTracedFlags() {
        // Arrange - Create InstructionUsageMarker and visitor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Get filtered visitors for both traced values
        InstructionVisitor filteredTraced = marker.tracedInstructionFilter(true, visitor);
        InstructionVisitor filteredUntraced = marker.tracedInstructionFilter(false, visitor);

        // Assert - Verify both work
        assertNotNull(filteredTraced, "Filtered visitor for traced instructions should not be null");
        assertNotNull(filteredUntraced, "Filtered visitor for untraced instructions should not be null");
    }

    /**
     * Tests two-parameter tracedInstructionFilter with different visitor implementations.
     * Verifies that the method handles various visitor types.
     */
    @Test
    public void testTwoParameterTracedInstructionFilterWithDifferentVisitors() {
        // Arrange - Create InstructionUsageMarker and different visitors
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        InstructionVisitor visitor1 = new TestInstructionVisitor();
        InstructionVisitor visitor2 = new CountingInstructionVisitor();

        // Act - Get filtered visitors
        InstructionVisitor filtered1 = marker.tracedInstructionFilter(true, visitor1);
        InstructionVisitor filtered2 = marker.tracedInstructionFilter(false, visitor2);

        // Assert - Verify both work
        assertNotNull(filtered1, "Filtered visitor 1 should not be null");
        assertNotNull(filtered2, "Filtered visitor 2 should not be null");
    }

    /**
     * Tests two-parameter tracedInstructionFilter with InstructionUsageMarker created using boolean constructor.
     * Verifies that the method works with the single-parameter constructor.
     */
    @Test
    public void testTwoParameterTracedInstructionFilterWithBooleanConstructor() {
        // Arrange - Create InstructionUsageMarker with boolean constructor
        InstructionUsageMarker marker = new InstructionUsageMarker(true);
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Get filtered visitors
        InstructionVisitor filteredTrue = marker.tracedInstructionFilter(true, visitor);
        InstructionVisitor filteredFalse = marker.tracedInstructionFilter(false, visitor);

        // Assert - Verify both work
        assertNotNull(filteredTrue, "tracedInstructionFilter(true) should work with boolean constructor");
        assertNotNull(filteredFalse, "tracedInstructionFilter(false) should work with boolean constructor");
    }

    /**
     * Tests two-parameter tracedInstructionFilter with InstructionUsageMarker created using 3-parameter constructor.
     * Verifies that the method works with the three-parameter constructor.
     */
    @Test
    public void testTwoParameterTracedInstructionFilterWithThreeParameterConstructor() {
        // Arrange - Create InstructionUsageMarker with 3-parameter constructor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true);
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Get filtered visitors
        InstructionVisitor filteredTrue = marker.tracedInstructionFilter(true, visitor);
        InstructionVisitor filteredFalse = marker.tracedInstructionFilter(false, visitor);

        // Assert - Verify both work
        assertNotNull(filteredTrue, "tracedInstructionFilter(true) should work with three-parameter constructor");
        assertNotNull(filteredFalse, "tracedInstructionFilter(false) should work with three-parameter constructor");
    }

    /**
     * Tests two-parameter tracedInstructionFilter with InstructionUsageMarker created using 4-parameter constructor.
     * Verifies that the method works with the four-parameter constructor.
     */
    @Test
    public void testTwoParameterTracedInstructionFilterWithFourParameterConstructor() {
        // Arrange - Create InstructionUsageMarker with 4-parameter constructor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Get filtered visitors
        InstructionVisitor filteredTrue = marker.tracedInstructionFilter(true, visitor);
        InstructionVisitor filteredFalse = marker.tracedInstructionFilter(false, visitor);

        // Assert - Verify both work
        assertNotNull(filteredTrue, "tracedInstructionFilter(true) should work with four-parameter constructor");
        assertNotNull(filteredFalse, "tracedInstructionFilter(false) should work with four-parameter constructor");
    }

    /**
     * Tests two-parameter tracedInstructionFilter with different PartialEvaluator configurations.
     * Verifies that the method works with various evaluator settings.
     */
    @Test
    public void testTwoParameterTracedInstructionFilterWithDifferentEvaluatorConfigurations() {
        // Arrange - Create InstructionUsageMarkers with different configurations
        PartialEvaluator evaluator1 = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker1 = new InstructionUsageMarker(evaluator1, true, true, true);

        PartialEvaluator evaluator2 = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker2 = new InstructionUsageMarker(evaluator2, false, false, false);

        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Get filtered visitors
        InstructionVisitor filtered1 = marker1.tracedInstructionFilter(true, visitor);
        InstructionVisitor filtered2 = marker2.tracedInstructionFilter(false, visitor);

        // Assert - Verify both work
        assertNotNull(filtered1, "tracedInstructionFilter should work with first configuration");
        assertNotNull(filtered2, "tracedInstructionFilter should work with second configuration");
    }

    /**
     * Tests that two-parameter tracedInstructionFilter works on multiple InstructionUsageMarker instances.
     * Verifies that the method works independently for different marker instances.
     */
    @Test
    public void testTwoParameterTracedInstructionFilterOnMultipleInstances() {
        // Arrange - Create multiple InstructionUsageMarker instances
        InstructionUsageMarker marker1 = new InstructionUsageMarker(true);
        InstructionUsageMarker marker2 = new InstructionUsageMarker(false);
        InstructionUsageMarker marker3 = new InstructionUsageMarker(true);
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Get filtered visitors from all instances
        InstructionVisitor filtered1 = marker1.tracedInstructionFilter(true, visitor);
        InstructionVisitor filtered2 = marker2.tracedInstructionFilter(false, visitor);
        InstructionVisitor filtered3 = marker3.tracedInstructionFilter(true, visitor);

        // Assert - Verify all instances work independently
        assertNotNull(filtered1, "tracedInstructionFilter should work on first marker instance");
        assertNotNull(filtered2, "tracedInstructionFilter should work on second marker instance");
        assertNotNull(filtered3, "tracedInstructionFilter should work on third marker instance");
    }

    /**
     * Tests two-parameter tracedInstructionFilter with runPartialEvaluator set to true.
     * Verifies that the method works when partial evaluator is configured to run.
     */
    @Test
    public void testTwoParameterTracedInstructionFilterWithRunPartialEvaluatorTrue() {
        // Arrange - Create InstructionUsageMarker with runPartialEvaluator=true
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Get filtered visitors
        InstructionVisitor filteredTrue = marker.tracedInstructionFilter(true, visitor);
        InstructionVisitor filteredFalse = marker.tracedInstructionFilter(false, visitor);

        // Assert - Verify both work
        assertNotNull(filteredTrue, "tracedInstructionFilter(true) should work when runPartialEvaluator is true");
        assertNotNull(filteredFalse, "tracedInstructionFilter(false) should work when runPartialEvaluator is true");
    }

    /**
     * Tests two-parameter tracedInstructionFilter with runPartialEvaluator set to false.
     * Verifies that the method works when partial evaluator is configured not to run.
     */
    @Test
    public void testTwoParameterTracedInstructionFilterWithRunPartialEvaluatorFalse() {
        // Arrange - Create InstructionUsageMarker with runPartialEvaluator=false
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, false, true, true);
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Get filtered visitors
        InstructionVisitor filteredTrue = marker.tracedInstructionFilter(true, visitor);
        InstructionVisitor filteredFalse = marker.tracedInstructionFilter(false, visitor);

        // Assert - Verify both work
        assertNotNull(filteredTrue, "tracedInstructionFilter(true) should work when runPartialEvaluator is false");
        assertNotNull(filteredFalse, "tracedInstructionFilter(false) should work when runPartialEvaluator is false");
    }

    /**
     * Tests two-parameter tracedInstructionFilter with ensureSafetyForVerifier set to true.
     * Verifies that the method works with verifier safety enabled.
     */
    @Test
    public void testTwoParameterTracedInstructionFilterWithEnsureSafetyForVerifierTrue() {
        // Arrange - Create InstructionUsageMarker with ensureSafetyForVerifier=true
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Get filtered visitors
        InstructionVisitor filteredTrue = marker.tracedInstructionFilter(true, visitor);
        InstructionVisitor filteredFalse = marker.tracedInstructionFilter(false, visitor);

        // Assert - Verify both work
        assertNotNull(filteredTrue, "tracedInstructionFilter(true) should work when ensureSafetyForVerifier is true");
        assertNotNull(filteredFalse, "tracedInstructionFilter(false) should work when ensureSafetyForVerifier is true");
    }

    /**
     * Tests two-parameter tracedInstructionFilter with ensureSafetyForVerifier set to false.
     * Verifies that the method works with verifier safety disabled.
     */
    @Test
    public void testTwoParameterTracedInstructionFilterWithEnsureSafetyForVerifierFalse() {
        // Arrange - Create InstructionUsageMarker with ensureSafetyForVerifier=false
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, false, true);
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Get filtered visitors
        InstructionVisitor filteredTrue = marker.tracedInstructionFilter(true, visitor);
        InstructionVisitor filteredFalse = marker.tracedInstructionFilter(false, visitor);

        // Assert - Verify both work
        assertNotNull(filteredTrue, "tracedInstructionFilter(true) should work when ensureSafetyForVerifier is false");
        assertNotNull(filteredFalse, "tracedInstructionFilter(false) should work when ensureSafetyForVerifier is false");
    }

    /**
     * Tests two-parameter tracedInstructionFilter with markExternalSideEffects set to true.
     * Verifies that the method works with external side effects marking enabled.
     */
    @Test
    public void testTwoParameterTracedInstructionFilterWithMarkExternalSideEffectsTrue() {
        // Arrange - Create InstructionUsageMarker with markExternalSideEffects=true
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Get filtered visitors
        InstructionVisitor filteredTrue = marker.tracedInstructionFilter(true, visitor);
        InstructionVisitor filteredFalse = marker.tracedInstructionFilter(false, visitor);

        // Assert - Verify both work
        assertNotNull(filteredTrue, "tracedInstructionFilter(true) should work when markExternalSideEffects is true");
        assertNotNull(filteredFalse, "tracedInstructionFilter(false) should work when markExternalSideEffects is true");
    }

    /**
     * Tests two-parameter tracedInstructionFilter with markExternalSideEffects set to false.
     * Verifies that the method works with external side effects marking disabled.
     */
    @Test
    public void testTwoParameterTracedInstructionFilterWithMarkExternalSideEffectsFalse() {
        // Arrange - Create InstructionUsageMarker with markExternalSideEffects=false
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, false);
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Get filtered visitors
        InstructionVisitor filteredTrue = marker.tracedInstructionFilter(true, visitor);
        InstructionVisitor filteredFalse = marker.tracedInstructionFilter(false, visitor);

        // Assert - Verify both work
        assertNotNull(filteredTrue, "tracedInstructionFilter(true) should work when markExternalSideEffects is false");
        assertNotNull(filteredFalse, "tracedInstructionFilter(false) should work when markExternalSideEffects is false");
    }

    /**
     * Tests two-parameter tracedInstructionFilter called multiple times in rapid succession.
     * Verifies that the method is stable for rapid sequential calls.
     */
    @Test
    public void testTwoParameterTracedInstructionFilterRapidSuccessiveCalls() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert - Call many times rapidly with alternating traced flag
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                InstructionVisitor visitor = new TestInstructionVisitor();
                boolean traced = (i % 2 == 0);
                InstructionVisitor filtered = marker.tracedInstructionFilter(traced, visitor);
                assertNotNull(filtered, "Filtered visitor " + i + " should not be null");
            }
        }, "tracedInstructionFilter should handle rapid successive calls");
    }

    /**
     * Tests two-parameter tracedInstructionFilter with all boolean flag combinations.
     * Verifies that the method works with all possible constructor flag combinations.
     */
    @Test
    public void testTwoParameterTracedInstructionFilterWithAllFlagCombinations() {
        // Arrange - Create visitor
        InstructionVisitor visitor = new TestInstructionVisitor();
        PartialEvaluator pe = PartialEvaluator.Builder.create().build();

        // Act & Assert - Test all 8 combinations with both traced values
        InstructionUsageMarker marker1 = new InstructionUsageMarker(pe, true, true, true);
        assertNotNull(marker1.tracedInstructionFilter(true, visitor), "Should work with (true, true, true) and traced=true");
        assertNotNull(marker1.tracedInstructionFilter(false, visitor), "Should work with (true, true, true) and traced=false");

        InstructionUsageMarker marker2 = new InstructionUsageMarker(pe, true, true, false);
        assertNotNull(marker2.tracedInstructionFilter(true, visitor), "Should work with (true, true, false) and traced=true");
        assertNotNull(marker2.tracedInstructionFilter(false, visitor), "Should work with (true, true, false) and traced=false");

        InstructionUsageMarker marker3 = new InstructionUsageMarker(pe, true, false, true);
        assertNotNull(marker3.tracedInstructionFilter(true, visitor), "Should work with (true, false, true) and traced=true");
        assertNotNull(marker3.tracedInstructionFilter(false, visitor), "Should work with (true, false, true) and traced=false");

        InstructionUsageMarker marker4 = new InstructionUsageMarker(pe, true, false, false);
        assertNotNull(marker4.tracedInstructionFilter(true, visitor), "Should work with (true, false, false) and traced=true");
        assertNotNull(marker4.tracedInstructionFilter(false, visitor), "Should work with (true, false, false) and traced=false");

        InstructionUsageMarker marker5 = new InstructionUsageMarker(pe, false, true, true);
        assertNotNull(marker5.tracedInstructionFilter(true, visitor), "Should work with (false, true, true) and traced=true");
        assertNotNull(marker5.tracedInstructionFilter(false, visitor), "Should work with (false, true, true) and traced=false");

        InstructionUsageMarker marker6 = new InstructionUsageMarker(pe, false, true, false);
        assertNotNull(marker6.tracedInstructionFilter(true, visitor), "Should work with (false, true, false) and traced=true");
        assertNotNull(marker6.tracedInstructionFilter(false, visitor), "Should work with (false, true, false) and traced=false");

        InstructionUsageMarker marker7 = new InstructionUsageMarker(pe, false, false, true);
        assertNotNull(marker7.tracedInstructionFilter(true, visitor), "Should work with (false, false, true) and traced=true");
        assertNotNull(marker7.tracedInstructionFilter(false, visitor), "Should work with (false, false, true) and traced=false");

        InstructionUsageMarker marker8 = new InstructionUsageMarker(pe, false, false, false);
        assertNotNull(marker8.tracedInstructionFilter(true, visitor), "Should work with (false, false, false) and traced=true");
        assertNotNull(marker8.tracedInstructionFilter(false, visitor), "Should work with (false, false, false) and traced=false");
    }

    /**
     * Tests that two-parameter tracedInstructionFilter can handle the same visitor being filtered multiple times.
     * Verifies that a visitor can be wrapped multiple times with different traced flags.
     */
    @Test
    public void testTwoParameterTracedInstructionFilterSameVisitorMultipleTimes() {
        // Arrange - Create InstructionUsageMarker and visitor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Filter the same visitor multiple times with different traced flags
        InstructionVisitor filtered1 = marker.tracedInstructionFilter(true, visitor);
        InstructionVisitor filtered2 = marker.tracedInstructionFilter(false, visitor);
        InstructionVisitor filtered3 = marker.tracedInstructionFilter(true, visitor);

        // Assert - All results should be non-null
        assertNotNull(filtered1, "First filtered visitor should not be null");
        assertNotNull(filtered2, "Second filtered visitor should not be null");
        assertNotNull(filtered3, "Third filtered visitor should not be null");
    }

    /**
     * Tests that two-parameter tracedInstructionFilter can wrap already-filtered visitors.
     * Verifies that filtering can be chained with different traced flags.
     */
    @Test
    public void testTwoParameterTracedInstructionFilterChaining() {
        // Arrange - Create InstructionUsageMarker and visitor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Chain filtering with different traced flags
        InstructionVisitor filtered1 = marker.tracedInstructionFilter(true, visitor);
        InstructionVisitor filtered2 = marker.tracedInstructionFilter(false, filtered1);
        InstructionVisitor filtered3 = marker.tracedInstructionFilter(true, filtered2);

        // Assert - All should be non-null
        assertNotNull(filtered1, "First filtered visitor should not be null");
        assertNotNull(filtered2, "Second (chained) filtered visitor should not be null");
        assertNotNull(filtered3, "Third (chained) filtered visitor should not be null");
    }

    /**
     * Tests two-parameter tracedInstructionFilter with a visitor that extends InstructionVisitor.
     * Verifies that the method works with visitor subclasses for both traced flags.
     */
    @Test
    public void testTwoParameterTracedInstructionFilterWithVisitorSubclass() {
        // Arrange - Create InstructionUsageMarker and visitor subclass
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        InstructionVisitor visitor = new CountingInstructionVisitor();

        // Act - Get filtered visitors
        InstructionVisitor filteredTrue = marker.tracedInstructionFilter(true, visitor);
        InstructionVisitor filteredFalse = marker.tracedInstructionFilter(false, visitor);

        // Assert - Verify both work
        assertNotNull(filteredTrue, "tracedInstructionFilter(true) should work with visitor subclass");
        assertNotNull(filteredFalse, "tracedInstructionFilter(false) should work with visitor subclass");
    }

    /**
     * Tests that two-parameter tracedInstructionFilter does not throw exceptions with valid parameters.
     * Verifies exception-free operation for both traced flag values.
     */
    @Test
    public void testTwoParameterTracedInstructionFilterDoesNotThrowException() {
        // Arrange - Create InstructionUsageMarker and visitor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act & Assert - Verify no exception is thrown for both traced values
        assertDoesNotThrow(() -> marker.tracedInstructionFilter(true, visitor),
                "tracedInstructionFilter(true, visitor) should not throw exception");
        assertDoesNotThrow(() -> marker.tracedInstructionFilter(false, visitor),
                "tracedInstructionFilter(false, visitor) should not throw exception");
    }

    /**
     * Tests two-parameter tracedInstructionFilter alternating between traced and untraced.
     * Verifies that the method handles alternating traced flags correctly.
     */
    @Test
    public void testTwoParameterTracedInstructionFilterAlternatingTracedFlags() {
        // Arrange - Create InstructionUsageMarker and visitor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act & Assert - Alternate between true and false
        for (int i = 0; i < 10; i++) {
            boolean traced = (i % 2 == 0);
            InstructionVisitor filtered = marker.tracedInstructionFilter(traced, visitor);
            assertNotNull(filtered, "Filtered visitor for iteration " + i + " (traced=" + traced + ") should not be null");
        }
    }

    // ========== Helper Classes ==========

    /**
     * Simple test InstructionVisitor implementation for testing purposes.
     */
    private static class TestInstructionVisitor implements InstructionVisitor {
        @Override
        public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute,
                                       int offset, Instruction instruction) {
            // No-op for testing
        }
    }

    /**
     * InstructionVisitor implementation that counts visits.
     */
    private static class CountingInstructionVisitor implements InstructionVisitor {
        int visitCount = 0;

        @Override
        public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute,
                                       int offset, Instruction instruction) {
            visitCount++;
        }
    }
}
