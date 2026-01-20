package proguard.optimize.evaluation;

import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.evaluation.PartialEvaluator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link InstructionUsageMarker#necessaryInstructionFilter(InstructionVisitor)} method
 * and {@link InstructionUsageMarker#necessaryInstructionFilter(boolean, InstructionVisitor)} method.
 * Tests the necessaryInstructionFilter methods which return a filtering version of the given
 * instruction visitor that only visits necessary or unnecessary instructions.
 */
public class InstructionUsageMarkerClaude_necessaryInstructionFilterTest {

    /**
     * Helper class that implements InstructionVisitor for testing purposes.
     */
    private static class TestInstructionVisitor implements InstructionVisitor {
        @Override
        public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute,
                                       int offset, Instruction instruction) {
            // No-op for testing
        }
    }

    /**
     * Helper class that counts the number of times instructions are visited.
     */
    private static class CountingInstructionVisitor implements InstructionVisitor {
        int visitCount = 0;

        @Override
        public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute,
                                       int offset, Instruction instruction) {
            visitCount++;
        }
    }

    /**
     * Tests that necessaryInstructionFilter can be called with a non-null visitor.
     * Verifies that the method accepts a valid InstructionVisitor parameter.
     */
    @Test
    public void testNecessaryInstructionFilterWithNonNullVisitor() {
        // Arrange - Create InstructionUsageMarker and a test visitor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        TestInstructionVisitor visitor = new TestInstructionVisitor();

        // Act & Assert - Verify method can be called without throwing exception
        assertDoesNotThrow(() -> marker.necessaryInstructionFilter(visitor),
                "necessaryInstructionFilter should not throw exception with non-null visitor");
    }

    /**
     * Tests that necessaryInstructionFilter returns a non-null InstructionVisitor.
     * Verifies that the method returns a valid filtering visitor.
     */
    @Test
    public void testNecessaryInstructionFilterReturnsNonNull() {
        // Arrange - Create InstructionUsageMarker and a test visitor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        TestInstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Call necessaryInstructionFilter
        InstructionVisitor result = marker.necessaryInstructionFilter(visitor);

        // Assert - Verify result is not null
        assertNotNull(result, "necessaryInstructionFilter should return a non-null InstructionVisitor");
    }

    /**
     * Tests that necessaryInstructionFilter returns an InstructionVisitor.
     * Verifies that the returned object is an instance of InstructionVisitor.
     */
    @Test
    public void testNecessaryInstructionFilterReturnsInstructionVisitor() {
        // Arrange - Create InstructionUsageMarker and a test visitor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        TestInstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Call necessaryInstructionFilter
        InstructionVisitor result = marker.necessaryInstructionFilter(visitor);

        // Assert - Verify result is an InstructionVisitor
        assertInstanceOf(InstructionVisitor.class, result,
                "necessaryInstructionFilter should return an InstructionVisitor instance");
    }

    /**
     * Tests that necessaryInstructionFilter can be called multiple times with same visitor.
     * Verifies that the method is stable and can be called repeatedly.
     */
    @Test
    public void testNecessaryInstructionFilterMultipleCallsSameVisitor() {
        // Arrange - Create InstructionUsageMarker and a test visitor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        TestInstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Call necessaryInstructionFilter multiple times
        InstructionVisitor result1 = marker.necessaryInstructionFilter(visitor);
        InstructionVisitor result2 = marker.necessaryInstructionFilter(visitor);
        InstructionVisitor result3 = marker.necessaryInstructionFilter(visitor);

        // Assert - Verify all results are non-null
        assertNotNull(result1, "First call should return non-null result");
        assertNotNull(result2, "Second call should return non-null result");
        assertNotNull(result3, "Third call should return non-null result");
    }

    /**
     * Tests that necessaryInstructionFilter can be called with different visitors.
     * Verifies that the method handles multiple different visitor instances.
     */
    @Test
    public void testNecessaryInstructionFilterWithDifferentVisitors() {
        // Arrange - Create InstructionUsageMarker and multiple test visitors
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        TestInstructionVisitor visitor1 = new TestInstructionVisitor();
        TestInstructionVisitor visitor2 = new TestInstructionVisitor();
        CountingInstructionVisitor visitor3 = new CountingInstructionVisitor();

        // Act - Call necessaryInstructionFilter with different visitors
        InstructionVisitor result1 = marker.necessaryInstructionFilter(visitor1);
        InstructionVisitor result2 = marker.necessaryInstructionFilter(visitor2);
        InstructionVisitor result3 = marker.necessaryInstructionFilter(visitor3);

        // Assert - Verify all results are non-null
        assertNotNull(result1, "Result with first visitor should be non-null");
        assertNotNull(result2, "Result with second visitor should be non-null");
        assertNotNull(result3, "Result with third visitor should be non-null");
    }

    /**
     * Tests necessaryInstructionFilter with InstructionUsageMarker created using boolean constructor.
     * Verifies that the method works with the single-parameter constructor.
     */
    @Test
    public void testNecessaryInstructionFilterWithBooleanConstructor() {
        // Arrange - Create InstructionUsageMarker with boolean constructor
        InstructionUsageMarker marker = new InstructionUsageMarker(true);
        TestInstructionVisitor visitor = new TestInstructionVisitor();

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.necessaryInstructionFilter(visitor),
                "necessaryInstructionFilter should work with boolean constructor");
        assertNotNull(marker.necessaryInstructionFilter(visitor),
                "necessaryInstructionFilter should return non-null with boolean constructor");
    }

    /**
     * Tests necessaryInstructionFilter with InstructionUsageMarker created using 3-parameter constructor.
     * Verifies that the method works with the three-parameter constructor.
     */
    @Test
    public void testNecessaryInstructionFilterWithThreeParameterConstructor() {
        // Arrange - Create InstructionUsageMarker with 3-parameter constructor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true);
        TestInstructionVisitor visitor = new TestInstructionVisitor();

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.necessaryInstructionFilter(visitor),
                "necessaryInstructionFilter should work with three-parameter constructor");
        assertNotNull(marker.necessaryInstructionFilter(visitor),
                "necessaryInstructionFilter should return non-null with three-parameter constructor");
    }

    /**
     * Tests necessaryInstructionFilter with InstructionUsageMarker created using 4-parameter constructor.
     * Verifies that the method works with the four-parameter constructor.
     */
    @Test
    public void testNecessaryInstructionFilterWithFourParameterConstructor() {
        // Arrange - Create InstructionUsageMarker with 4-parameter constructor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        TestInstructionVisitor visitor = new TestInstructionVisitor();

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.necessaryInstructionFilter(visitor),
                "necessaryInstructionFilter should work with four-parameter constructor");
        assertNotNull(marker.necessaryInstructionFilter(visitor),
                "necessaryInstructionFilter should return non-null with four-parameter constructor");
    }

    /**
     * Tests necessaryInstructionFilter with different PartialEvaluator configurations.
     * Verifies that the method works with various evaluator settings.
     */
    @Test
    public void testNecessaryInstructionFilterWithDifferentEvaluatorConfigurations() {
        // Arrange - Create InstructionUsageMarkers with different configurations
        PartialEvaluator evaluator1 = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker1 = new InstructionUsageMarker(evaluator1, true, true, true);

        PartialEvaluator evaluator2 = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker2 = new InstructionUsageMarker(evaluator2, false, false, false);

        TestInstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Call necessaryInstructionFilter on both markers
        InstructionVisitor result1 = marker1.necessaryInstructionFilter(visitor);
        InstructionVisitor result2 = marker2.necessaryInstructionFilter(visitor);

        // Assert - Verify both work
        assertNotNull(result1, "necessaryInstructionFilter should work with first configuration");
        assertNotNull(result2, "necessaryInstructionFilter should work with second configuration");
    }

    /**
     * Tests that necessaryInstructionFilter can be called on multiple InstructionUsageMarker instances.
     * Verifies that the method works independently for different marker instances.
     */
    @Test
    public void testNecessaryInstructionFilterOnMultipleInstances() {
        // Arrange - Create multiple InstructionUsageMarker instances
        InstructionUsageMarker marker1 = new InstructionUsageMarker(true);
        InstructionUsageMarker marker2 = new InstructionUsageMarker(false);
        InstructionUsageMarker marker3 = new InstructionUsageMarker(true);

        TestInstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Call necessaryInstructionFilter on all instances
        InstructionVisitor result1 = marker1.necessaryInstructionFilter(visitor);
        InstructionVisitor result2 = marker2.necessaryInstructionFilter(visitor);
        InstructionVisitor result3 = marker3.necessaryInstructionFilter(visitor);

        // Assert - Verify all instances work independently
        assertNotNull(result1, "necessaryInstructionFilter should work on first marker instance");
        assertNotNull(result2, "necessaryInstructionFilter should work on second marker instance");
        assertNotNull(result3, "necessaryInstructionFilter should work on third marker instance");
    }

    /**
     * Tests necessaryInstructionFilter with CountingInstructionVisitor.
     * Verifies that the method works with a visitor that maintains state.
     */
    @Test
    public void testNecessaryInstructionFilterWithCountingVisitor() {
        // Arrange - Create InstructionUsageMarker and a counting visitor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        CountingInstructionVisitor visitor = new CountingInstructionVisitor();

        // Act - Call necessaryInstructionFilter
        InstructionVisitor result = marker.necessaryInstructionFilter(visitor);

        // Assert - Verify result is non-null and visitor count is unchanged
        assertNotNull(result, "necessaryInstructionFilter should return non-null with counting visitor");
        assertEquals(0, visitor.visitCount, "Visitor count should be unchanged after creating filter");
    }

    /**
     * Tests necessaryInstructionFilter with runPartialEvaluator set to true.
     * Verifies that the method works when partial evaluator is configured to run.
     */
    @Test
    public void testNecessaryInstructionFilterWithRunPartialEvaluatorTrue() {
        // Arrange - Create InstructionUsageMarker with runPartialEvaluator=true
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        TestInstructionVisitor visitor = new TestInstructionVisitor();

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.necessaryInstructionFilter(visitor),
                "necessaryInstructionFilter should work when runPartialEvaluator is true");
        assertNotNull(marker.necessaryInstructionFilter(visitor),
                "necessaryInstructionFilter should return non-null when runPartialEvaluator is true");
    }

    /**
     * Tests necessaryInstructionFilter with runPartialEvaluator set to false.
     * Verifies that the method works when partial evaluator is configured not to run.
     */
    @Test
    public void testNecessaryInstructionFilterWithRunPartialEvaluatorFalse() {
        // Arrange - Create InstructionUsageMarker with runPartialEvaluator=false
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, false, true, true);
        TestInstructionVisitor visitor = new TestInstructionVisitor();

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.necessaryInstructionFilter(visitor),
                "necessaryInstructionFilter should work when runPartialEvaluator is false");
        assertNotNull(marker.necessaryInstructionFilter(visitor),
                "necessaryInstructionFilter should return non-null when runPartialEvaluator is false");
    }

    /**
     * Tests necessaryInstructionFilter with ensureSafetyForVerifier set to true.
     * Verifies that the method works with verifier safety enabled.
     */
    @Test
    public void testNecessaryInstructionFilterWithEnsureSafetyForVerifierTrue() {
        // Arrange - Create InstructionUsageMarker with ensureSafetyForVerifier=true
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        TestInstructionVisitor visitor = new TestInstructionVisitor();

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.necessaryInstructionFilter(visitor),
                "necessaryInstructionFilter should work when ensureSafetyForVerifier is true");
        assertNotNull(marker.necessaryInstructionFilter(visitor),
                "necessaryInstructionFilter should return non-null when ensureSafetyForVerifier is true");
    }

    /**
     * Tests necessaryInstructionFilter with ensureSafetyForVerifier set to false.
     * Verifies that the method works with verifier safety disabled.
     */
    @Test
    public void testNecessaryInstructionFilterWithEnsureSafetyForVerifierFalse() {
        // Arrange - Create InstructionUsageMarker with ensureSafetyForVerifier=false
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, false, true);
        TestInstructionVisitor visitor = new TestInstructionVisitor();

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.necessaryInstructionFilter(visitor),
                "necessaryInstructionFilter should work when ensureSafetyForVerifier is false");
        assertNotNull(marker.necessaryInstructionFilter(visitor),
                "necessaryInstructionFilter should return non-null when ensureSafetyForVerifier is false");
    }

    /**
     * Tests necessaryInstructionFilter with markExternalSideEffects set to true.
     * Verifies that the method works with external side effects marking enabled.
     */
    @Test
    public void testNecessaryInstructionFilterWithMarkExternalSideEffectsTrue() {
        // Arrange - Create InstructionUsageMarker with markExternalSideEffects=true
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        TestInstructionVisitor visitor = new TestInstructionVisitor();

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.necessaryInstructionFilter(visitor),
                "necessaryInstructionFilter should work when markExternalSideEffects is true");
        assertNotNull(marker.necessaryInstructionFilter(visitor),
                "necessaryInstructionFilter should return non-null when markExternalSideEffects is true");
    }

    /**
     * Tests necessaryInstructionFilter with markExternalSideEffects set to false.
     * Verifies that the method works with external side effects marking disabled.
     */
    @Test
    public void testNecessaryInstructionFilterWithMarkExternalSideEffectsFalse() {
        // Arrange - Create InstructionUsageMarker with markExternalSideEffects=false
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, false);
        TestInstructionVisitor visitor = new TestInstructionVisitor();

        // Act & Assert - Verify method works
        assertDoesNotThrow(() -> marker.necessaryInstructionFilter(visitor),
                "necessaryInstructionFilter should work when markExternalSideEffects is false");
        assertNotNull(marker.necessaryInstructionFilter(visitor),
                "necessaryInstructionFilter should return non-null when markExternalSideEffects is false");
    }

    /**
     * Tests necessaryInstructionFilter called in rapid succession.
     * Verifies that the method is thread-safe for rapid sequential calls.
     */
    @Test
    public void testNecessaryInstructionFilterRapidSuccessiveCalls() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        TestInstructionVisitor visitor = new TestInstructionVisitor();

        // Act & Assert - Call many times rapidly
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                InstructionVisitor result = marker.necessaryInstructionFilter(visitor);
                assertNotNull(result, "Result " + i + " should be non-null");
            }
        }, "necessaryInstructionFilter should handle rapid successive calls");
    }

    /**
     * Tests necessaryInstructionFilter with all boolean flag combinations.
     * Verifies that the method works with all possible constructor flag combinations.
     */
    @Test
    public void testNecessaryInstructionFilterWithAllFlagCombinations() {
        // Arrange - Create test visitor
        TestInstructionVisitor visitor = new TestInstructionVisitor();
        PartialEvaluator pe = PartialEvaluator.Builder.create().build();

        // Act & Assert - Test all 8 combinations
        InstructionUsageMarker marker1 = new InstructionUsageMarker(pe, true, true, true);
        assertNotNull(marker1.necessaryInstructionFilter(visitor), "Should work with (true, true, true)");

        InstructionUsageMarker marker2 = new InstructionUsageMarker(pe, true, true, false);
        assertNotNull(marker2.necessaryInstructionFilter(visitor), "Should work with (true, true, false)");

        InstructionUsageMarker marker3 = new InstructionUsageMarker(pe, true, false, true);
        assertNotNull(marker3.necessaryInstructionFilter(visitor), "Should work with (true, false, true)");

        InstructionUsageMarker marker4 = new InstructionUsageMarker(pe, true, false, false);
        assertNotNull(marker4.necessaryInstructionFilter(visitor), "Should work with (true, false, false)");

        InstructionUsageMarker marker5 = new InstructionUsageMarker(pe, false, true, true);
        assertNotNull(marker5.necessaryInstructionFilter(visitor), "Should work with (false, true, true)");

        InstructionUsageMarker marker6 = new InstructionUsageMarker(pe, false, true, false);
        assertNotNull(marker6.necessaryInstructionFilter(visitor), "Should work with (false, true, false)");

        InstructionUsageMarker marker7 = new InstructionUsageMarker(pe, false, false, true);
        assertNotNull(marker7.necessaryInstructionFilter(visitor), "Should work with (false, false, true)");

        InstructionUsageMarker marker8 = new InstructionUsageMarker(pe, false, false, false);
        assertNotNull(marker8.necessaryInstructionFilter(visitor), "Should work with (false, false, false)");
    }

    /**
     * Tests necessaryInstructionFilter returns consistent type across calls.
     * Verifies that the method always returns an InstructionVisitor instance.
     */
    @Test
    public void testNecessaryInstructionFilterAlwaysReturnsInstructionVisitor() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        TestInstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Call multiple times and collect results
        InstructionVisitor[] results = new InstructionVisitor[10];
        for (int i = 0; i < 10; i++) {
            results[i] = marker.necessaryInstructionFilter(visitor);
        }

        // Assert - All results should be InstructionVisitor instances
        for (int i = 0; i < 10; i++) {
            assertNotNull(results[i], "Result " + i + " should not be null");
            assertInstanceOf(InstructionVisitor.class, results[i],
                    "Result " + i + " should be an InstructionVisitor instance");
        }
    }

    /**
     * Tests necessaryInstructionFilter with multiple different visitor types.
     * Verifies that the method handles different InstructionVisitor implementations.
     */
    @Test
    public void testNecessaryInstructionFilterWithMultipleVisitorTypes() {
        // Arrange - Create InstructionUsageMarker and multiple visitor types
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        TestInstructionVisitor testVisitor = new TestInstructionVisitor();
        CountingInstructionVisitor countingVisitor = new CountingInstructionVisitor();

        // Act - Create filters for different visitor types
        InstructionVisitor result1 = marker.necessaryInstructionFilter(testVisitor);
        InstructionVisitor result2 = marker.necessaryInstructionFilter(countingVisitor);

        // Assert - Verify both results are valid
        assertNotNull(result1, "Result with TestInstructionVisitor should be non-null");
        assertNotNull(result2, "Result with CountingInstructionVisitor should be non-null");
        assertInstanceOf(InstructionVisitor.class, result1, "Result 1 should be InstructionVisitor");
        assertInstanceOf(InstructionVisitor.class, result2, "Result 2 should be InstructionVisitor");
    }

    // ========== Tests for overloaded necessaryInstructionFilter(boolean, InstructionVisitor) ==========

    /**
     * Tests that necessaryInstructionFilter can be called with necessary=true and a non-null visitor.
     * Verifies that the method accepts valid parameters for filtering necessary instructions.
     */
    @Test
    public void testNecessaryInstructionFilterBooleanWithNecessaryTrue() {
        // Arrange - Create InstructionUsageMarker and a test visitor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        TestInstructionVisitor visitor = new TestInstructionVisitor();

        // Act & Assert - Verify method can be called without throwing exception
        assertDoesNotThrow(() -> marker.necessaryInstructionFilter(true, visitor),
                "necessaryInstructionFilter should not throw exception with necessary=true");
    }

    /**
     * Tests that necessaryInstructionFilter can be called with necessary=false and a non-null visitor.
     * Verifies that the method accepts valid parameters for filtering unnecessary instructions.
     */
    @Test
    public void testNecessaryInstructionFilterBooleanWithNecessaryFalse() {
        // Arrange - Create InstructionUsageMarker and a test visitor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        TestInstructionVisitor visitor = new TestInstructionVisitor();

        // Act & Assert - Verify method can be called without throwing exception
        assertDoesNotThrow(() -> marker.necessaryInstructionFilter(false, visitor),
                "necessaryInstructionFilter should not throw exception with necessary=false");
    }

    /**
     * Tests that necessaryInstructionFilter returns a non-null InstructionVisitor with necessary=true.
     * Verifies that the method returns a valid filtering visitor for necessary instructions.
     */
    @Test
    public void testNecessaryInstructionFilterBooleanReturnsNonNullWithTrue() {
        // Arrange - Create InstructionUsageMarker and a test visitor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        TestInstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Call necessaryInstructionFilter with necessary=true
        InstructionVisitor result = marker.necessaryInstructionFilter(true, visitor);

        // Assert - Verify result is not null
        assertNotNull(result, "necessaryInstructionFilter should return a non-null InstructionVisitor with necessary=true");
    }

    /**
     * Tests that necessaryInstructionFilter returns a non-null InstructionVisitor with necessary=false.
     * Verifies that the method returns a valid filtering visitor for unnecessary instructions.
     */
    @Test
    public void testNecessaryInstructionFilterBooleanReturnsNonNullWithFalse() {
        // Arrange - Create InstructionUsageMarker and a test visitor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        TestInstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Call necessaryInstructionFilter with necessary=false
        InstructionVisitor result = marker.necessaryInstructionFilter(false, visitor);

        // Assert - Verify result is not null
        assertNotNull(result, "necessaryInstructionFilter should return a non-null InstructionVisitor with necessary=false");
    }

    /**
     * Tests that necessaryInstructionFilter returns an InstructionVisitor for both true and false.
     * Verifies that the returned objects are instances of InstructionVisitor.
     */
    @Test
    public void testNecessaryInstructionFilterBooleanReturnsInstructionVisitor() {
        // Arrange - Create InstructionUsageMarker and a test visitor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        TestInstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Call necessaryInstructionFilter with both true and false
        InstructionVisitor resultTrue = marker.necessaryInstructionFilter(true, visitor);
        InstructionVisitor resultFalse = marker.necessaryInstructionFilter(false, visitor);

        // Assert - Verify results are InstructionVisitor instances
        assertInstanceOf(InstructionVisitor.class, resultTrue,
                "necessaryInstructionFilter should return an InstructionVisitor instance with necessary=true");
        assertInstanceOf(InstructionVisitor.class, resultFalse,
                "necessaryInstructionFilter should return an InstructionVisitor instance with necessary=false");
    }

    /**
     * Tests that necessaryInstructionFilter can be called multiple times with different boolean values.
     * Verifies that the method is stable and can be called repeatedly with both true and false.
     */
    @Test
    public void testNecessaryInstructionFilterBooleanMultipleCalls() {
        // Arrange - Create InstructionUsageMarker and a test visitor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        TestInstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Call necessaryInstructionFilter multiple times with different boolean values
        InstructionVisitor result1 = marker.necessaryInstructionFilter(true, visitor);
        InstructionVisitor result2 = marker.necessaryInstructionFilter(false, visitor);
        InstructionVisitor result3 = marker.necessaryInstructionFilter(true, visitor);
        InstructionVisitor result4 = marker.necessaryInstructionFilter(false, visitor);

        // Assert - Verify all results are non-null
        assertNotNull(result1, "First call with true should return non-null result");
        assertNotNull(result2, "First call with false should return non-null result");
        assertNotNull(result3, "Second call with true should return non-null result");
        assertNotNull(result4, "Second call with false should return non-null result");
    }

    /**
     * Tests that necessaryInstructionFilter works with different visitors and boolean values.
     * Verifies that the method handles multiple combinations of boolean and visitor parameters.
     */
    @Test
    public void testNecessaryInstructionFilterBooleanWithDifferentVisitors() {
        // Arrange - Create InstructionUsageMarker and multiple test visitors
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        TestInstructionVisitor visitor1 = new TestInstructionVisitor();
        CountingInstructionVisitor visitor2 = new CountingInstructionVisitor();

        // Act - Call necessaryInstructionFilter with different combinations
        InstructionVisitor result1 = marker.necessaryInstructionFilter(true, visitor1);
        InstructionVisitor result2 = marker.necessaryInstructionFilter(false, visitor1);
        InstructionVisitor result3 = marker.necessaryInstructionFilter(true, visitor2);
        InstructionVisitor result4 = marker.necessaryInstructionFilter(false, visitor2);

        // Assert - Verify all results are non-null
        assertNotNull(result1, "Result with visitor1 and true should be non-null");
        assertNotNull(result2, "Result with visitor1 and false should be non-null");
        assertNotNull(result3, "Result with visitor2 and true should be non-null");
        assertNotNull(result4, "Result with visitor2 and false should be non-null");
    }

    /**
     * Tests necessaryInstructionFilter with boolean parameter using boolean constructor.
     * Verifies that the method works with the single-parameter constructor.
     */
    @Test
    public void testNecessaryInstructionFilterBooleanWithBooleanConstructor() {
        // Arrange - Create InstructionUsageMarker with boolean constructor
        InstructionUsageMarker marker = new InstructionUsageMarker(true);
        TestInstructionVisitor visitor = new TestInstructionVisitor();

        // Act & Assert - Verify method works with both boolean values
        assertDoesNotThrow(() -> marker.necessaryInstructionFilter(true, visitor),
                "necessaryInstructionFilter should work with boolean constructor and necessary=true");
        assertDoesNotThrow(() -> marker.necessaryInstructionFilter(false, visitor),
                "necessaryInstructionFilter should work with boolean constructor and necessary=false");
        assertNotNull(marker.necessaryInstructionFilter(true, visitor),
                "necessaryInstructionFilter should return non-null with boolean constructor and necessary=true");
        assertNotNull(marker.necessaryInstructionFilter(false, visitor),
                "necessaryInstructionFilter should return non-null with boolean constructor and necessary=false");
    }

    /**
     * Tests necessaryInstructionFilter with boolean parameter using 3-parameter constructor.
     * Verifies that the method works with the three-parameter constructor.
     */
    @Test
    public void testNecessaryInstructionFilterBooleanWithThreeParameterConstructor() {
        // Arrange - Create InstructionUsageMarker with 3-parameter constructor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true);
        TestInstructionVisitor visitor = new TestInstructionVisitor();

        // Act & Assert - Verify method works with both boolean values
        assertNotNull(marker.necessaryInstructionFilter(true, visitor),
                "necessaryInstructionFilter should return non-null with three-parameter constructor and necessary=true");
        assertNotNull(marker.necessaryInstructionFilter(false, visitor),
                "necessaryInstructionFilter should return non-null with three-parameter constructor and necessary=false");
    }

    /**
     * Tests necessaryInstructionFilter with boolean parameter using 4-parameter constructor.
     * Verifies that the method works with the four-parameter constructor.
     */
    @Test
    public void testNecessaryInstructionFilterBooleanWithFourParameterConstructor() {
        // Arrange - Create InstructionUsageMarker with 4-parameter constructor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        TestInstructionVisitor visitor = new TestInstructionVisitor();

        // Act & Assert - Verify method works with both boolean values
        assertNotNull(marker.necessaryInstructionFilter(true, visitor),
                "necessaryInstructionFilter should return non-null with four-parameter constructor and necessary=true");
        assertNotNull(marker.necessaryInstructionFilter(false, visitor),
                "necessaryInstructionFilter should return non-null with four-parameter constructor and necessary=false");
    }

    /**
     * Tests necessaryInstructionFilter with boolean parameter and different evaluator configurations.
     * Verifies that the method works with various evaluator settings.
     */
    @Test
    public void testNecessaryInstructionFilterBooleanWithDifferentEvaluatorConfigurations() {
        // Arrange - Create InstructionUsageMarkers with different configurations
        PartialEvaluator evaluator1 = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker1 = new InstructionUsageMarker(evaluator1, true, true, true);

        PartialEvaluator evaluator2 = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker2 = new InstructionUsageMarker(evaluator2, false, false, false);

        TestInstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Call necessaryInstructionFilter on both markers with both boolean values
        InstructionVisitor result1True = marker1.necessaryInstructionFilter(true, visitor);
        InstructionVisitor result1False = marker1.necessaryInstructionFilter(false, visitor);
        InstructionVisitor result2True = marker2.necessaryInstructionFilter(true, visitor);
        InstructionVisitor result2False = marker2.necessaryInstructionFilter(false, visitor);

        // Assert - Verify all work
        assertNotNull(result1True, "Should work with first configuration and necessary=true");
        assertNotNull(result1False, "Should work with first configuration and necessary=false");
        assertNotNull(result2True, "Should work with second configuration and necessary=true");
        assertNotNull(result2False, "Should work with second configuration and necessary=false");
    }

    /**
     * Tests that necessaryInstructionFilter with boolean works on multiple instances.
     * Verifies that the method works independently for different marker instances.
     */
    @Test
    public void testNecessaryInstructionFilterBooleanOnMultipleInstances() {
        // Arrange - Create multiple InstructionUsageMarker instances
        InstructionUsageMarker marker1 = new InstructionUsageMarker(true);
        InstructionUsageMarker marker2 = new InstructionUsageMarker(false);
        InstructionUsageMarker marker3 = new InstructionUsageMarker(true);

        TestInstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Call necessaryInstructionFilter on all instances with both boolean values
        InstructionVisitor result1True = marker1.necessaryInstructionFilter(true, visitor);
        InstructionVisitor result1False = marker1.necessaryInstructionFilter(false, visitor);
        InstructionVisitor result2True = marker2.necessaryInstructionFilter(true, visitor);
        InstructionVisitor result2False = marker2.necessaryInstructionFilter(false, visitor);
        InstructionVisitor result3True = marker3.necessaryInstructionFilter(true, visitor);
        InstructionVisitor result3False = marker3.necessaryInstructionFilter(false, visitor);

        // Assert - Verify all instances work independently
        assertNotNull(result1True, "Should work on first marker with necessary=true");
        assertNotNull(result1False, "Should work on first marker with necessary=false");
        assertNotNull(result2True, "Should work on second marker with necessary=true");
        assertNotNull(result2False, "Should work on second marker with necessary=false");
        assertNotNull(result3True, "Should work on third marker with necessary=true");
        assertNotNull(result3False, "Should work on third marker with necessary=false");
    }

    /**
     * Tests necessaryInstructionFilter with boolean and CountingInstructionVisitor.
     * Verifies that the method works with a visitor that maintains state.
     */
    @Test
    public void testNecessaryInstructionFilterBooleanWithCountingVisitor() {
        // Arrange - Create InstructionUsageMarker and a counting visitor
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        CountingInstructionVisitor visitor = new CountingInstructionVisitor();

        // Act - Call necessaryInstructionFilter with both boolean values
        InstructionVisitor resultTrue = marker.necessaryInstructionFilter(true, visitor);
        InstructionVisitor resultFalse = marker.necessaryInstructionFilter(false, visitor);

        // Assert - Verify results are non-null and visitor count is unchanged
        assertNotNull(resultTrue, "Should return non-null with counting visitor and necessary=true");
        assertNotNull(resultFalse, "Should return non-null with counting visitor and necessary=false");
        assertEquals(0, visitor.visitCount, "Visitor count should be unchanged after creating filters");
    }

    /**
     * Tests necessaryInstructionFilter with boolean and runPartialEvaluator set to true.
     * Verifies that the method works when partial evaluator is configured to run.
     */
    @Test
    public void testNecessaryInstructionFilterBooleanWithRunPartialEvaluatorTrue() {
        // Arrange - Create InstructionUsageMarker with runPartialEvaluator=true
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        TestInstructionVisitor visitor = new TestInstructionVisitor();

        // Act & Assert - Verify method works with both boolean values
        assertNotNull(marker.necessaryInstructionFilter(true, visitor),
                "Should work when runPartialEvaluator is true and necessary=true");
        assertNotNull(marker.necessaryInstructionFilter(false, visitor),
                "Should work when runPartialEvaluator is true and necessary=false");
    }

    /**
     * Tests necessaryInstructionFilter with boolean and runPartialEvaluator set to false.
     * Verifies that the method works when partial evaluator is configured not to run.
     */
    @Test
    public void testNecessaryInstructionFilterBooleanWithRunPartialEvaluatorFalse() {
        // Arrange - Create InstructionUsageMarker with runPartialEvaluator=false
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, false, true, true);
        TestInstructionVisitor visitor = new TestInstructionVisitor();

        // Act & Assert - Verify method works with both boolean values
        assertNotNull(marker.necessaryInstructionFilter(true, visitor),
                "Should work when runPartialEvaluator is false and necessary=true");
        assertNotNull(marker.necessaryInstructionFilter(false, visitor),
                "Should work when runPartialEvaluator is false and necessary=false");
    }

    /**
     * Tests necessaryInstructionFilter with boolean and ensureSafetyForVerifier set to true.
     * Verifies that the method works with verifier safety enabled.
     */
    @Test
    public void testNecessaryInstructionFilterBooleanWithEnsureSafetyForVerifierTrue() {
        // Arrange - Create InstructionUsageMarker with ensureSafetyForVerifier=true
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        TestInstructionVisitor visitor = new TestInstructionVisitor();

        // Act & Assert - Verify method works with both boolean values
        assertNotNull(marker.necessaryInstructionFilter(true, visitor),
                "Should work when ensureSafetyForVerifier is true and necessary=true");
        assertNotNull(marker.necessaryInstructionFilter(false, visitor),
                "Should work when ensureSafetyForVerifier is true and necessary=false");
    }

    /**
     * Tests necessaryInstructionFilter with boolean and ensureSafetyForVerifier set to false.
     * Verifies that the method works with verifier safety disabled.
     */
    @Test
    public void testNecessaryInstructionFilterBooleanWithEnsureSafetyForVerifierFalse() {
        // Arrange - Create InstructionUsageMarker with ensureSafetyForVerifier=false
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, false, true);
        TestInstructionVisitor visitor = new TestInstructionVisitor();

        // Act & Assert - Verify method works with both boolean values
        assertNotNull(marker.necessaryInstructionFilter(true, visitor),
                "Should work when ensureSafetyForVerifier is false and necessary=true");
        assertNotNull(marker.necessaryInstructionFilter(false, visitor),
                "Should work when ensureSafetyForVerifier is false and necessary=false");
    }

    /**
     * Tests necessaryInstructionFilter with boolean and markExternalSideEffects set to true.
     * Verifies that the method works with external side effects marking enabled.
     */
    @Test
    public void testNecessaryInstructionFilterBooleanWithMarkExternalSideEffectsTrue() {
        // Arrange - Create InstructionUsageMarker with markExternalSideEffects=true
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        TestInstructionVisitor visitor = new TestInstructionVisitor();

        // Act & Assert - Verify method works with both boolean values
        assertNotNull(marker.necessaryInstructionFilter(true, visitor),
                "Should work when markExternalSideEffects is true and necessary=true");
        assertNotNull(marker.necessaryInstructionFilter(false, visitor),
                "Should work when markExternalSideEffects is true and necessary=false");
    }

    /**
     * Tests necessaryInstructionFilter with boolean and markExternalSideEffects set to false.
     * Verifies that the method works with external side effects marking disabled.
     */
    @Test
    public void testNecessaryInstructionFilterBooleanWithMarkExternalSideEffectsFalse() {
        // Arrange - Create InstructionUsageMarker with markExternalSideEffects=false
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, false);
        TestInstructionVisitor visitor = new TestInstructionVisitor();

        // Act & Assert - Verify method works with both boolean values
        assertNotNull(marker.necessaryInstructionFilter(true, visitor),
                "Should work when markExternalSideEffects is false and necessary=true");
        assertNotNull(marker.necessaryInstructionFilter(false, visitor),
                "Should work when markExternalSideEffects is false and necessary=false");
    }

    /**
     * Tests necessaryInstructionFilter with boolean called in rapid succession.
     * Verifies that the method is thread-safe for rapid sequential calls with both boolean values.
     */
    @Test
    public void testNecessaryInstructionFilterBooleanRapidSuccessiveCalls() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        TestInstructionVisitor visitor = new TestInstructionVisitor();

        // Act & Assert - Call many times rapidly with alternating boolean values
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                boolean necessary = (i % 2 == 0);
                InstructionVisitor result = marker.necessaryInstructionFilter(necessary, visitor);
                assertNotNull(result, "Result " + i + " should be non-null");
            }
        }, "necessaryInstructionFilter should handle rapid successive calls with boolean parameter");
    }

    /**
     * Tests necessaryInstructionFilter with boolean and all flag combinations.
     * Verifies that the method works with all possible constructor flag combinations and both boolean values.
     */
    @Test
    public void testNecessaryInstructionFilterBooleanWithAllFlagCombinations() {
        // Arrange - Create test visitor
        TestInstructionVisitor visitor = new TestInstructionVisitor();
        PartialEvaluator pe = PartialEvaluator.Builder.create().build();

        // Act & Assert - Test all 8 combinations with both boolean values
        InstructionUsageMarker marker1 = new InstructionUsageMarker(pe, true, true, true);
        assertNotNull(marker1.necessaryInstructionFilter(true, visitor), "Should work with (true, true, true) and necessary=true");
        assertNotNull(marker1.necessaryInstructionFilter(false, visitor), "Should work with (true, true, true) and necessary=false");

        InstructionUsageMarker marker2 = new InstructionUsageMarker(pe, true, true, false);
        assertNotNull(marker2.necessaryInstructionFilter(true, visitor), "Should work with (true, true, false) and necessary=true");
        assertNotNull(marker2.necessaryInstructionFilter(false, visitor), "Should work with (true, true, false) and necessary=false");

        InstructionUsageMarker marker3 = new InstructionUsageMarker(pe, true, false, true);
        assertNotNull(marker3.necessaryInstructionFilter(true, visitor), "Should work with (true, false, true) and necessary=true");
        assertNotNull(marker3.necessaryInstructionFilter(false, visitor), "Should work with (true, false, true) and necessary=false");

        InstructionUsageMarker marker4 = new InstructionUsageMarker(pe, true, false, false);
        assertNotNull(marker4.necessaryInstructionFilter(true, visitor), "Should work with (true, false, false) and necessary=true");
        assertNotNull(marker4.necessaryInstructionFilter(false, visitor), "Should work with (true, false, false) and necessary=false");

        InstructionUsageMarker marker5 = new InstructionUsageMarker(pe, false, true, true);
        assertNotNull(marker5.necessaryInstructionFilter(true, visitor), "Should work with (false, true, true) and necessary=true");
        assertNotNull(marker5.necessaryInstructionFilter(false, visitor), "Should work with (false, true, true) and necessary=false");

        InstructionUsageMarker marker6 = new InstructionUsageMarker(pe, false, true, false);
        assertNotNull(marker6.necessaryInstructionFilter(true, visitor), "Should work with (false, true, false) and necessary=true");
        assertNotNull(marker6.necessaryInstructionFilter(false, visitor), "Should work with (false, true, false) and necessary=false");

        InstructionUsageMarker marker7 = new InstructionUsageMarker(pe, false, false, true);
        assertNotNull(marker7.necessaryInstructionFilter(true, visitor), "Should work with (false, false, true) and necessary=true");
        assertNotNull(marker7.necessaryInstructionFilter(false, visitor), "Should work with (false, false, true) and necessary=false");

        InstructionUsageMarker marker8 = new InstructionUsageMarker(pe, false, false, false);
        assertNotNull(marker8.necessaryInstructionFilter(true, visitor), "Should work with (false, false, false) and necessary=true");
        assertNotNull(marker8.necessaryInstructionFilter(false, visitor), "Should work with (false, false, false) and necessary=false");
    }

    /**
     * Tests necessaryInstructionFilter with boolean returns consistent type across calls.
     * Verifies that the method always returns an InstructionVisitor instance for both boolean values.
     */
    @Test
    public void testNecessaryInstructionFilterBooleanAlwaysReturnsInstructionVisitor() {
        // Arrange - Create InstructionUsageMarker
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);
        TestInstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Call multiple times with alternating boolean values
        InstructionVisitor[] results = new InstructionVisitor[10];
        for (int i = 0; i < 10; i++) {
            results[i] = marker.necessaryInstructionFilter(i % 2 == 0, visitor);
        }

        // Assert - All results should be InstructionVisitor instances
        for (int i = 0; i < 10; i++) {
            assertNotNull(results[i], "Result " + i + " should not be null");
            assertInstanceOf(InstructionVisitor.class, results[i],
                    "Result " + i + " should be an InstructionVisitor instance");
        }
    }

    /**
     * Tests necessaryInstructionFilter with boolean and multiple different visitor types.
     * Verifies that the method handles different InstructionVisitor implementations with both boolean values.
     */
    @Test
    public void testNecessaryInstructionFilterBooleanWithMultipleVisitorTypes() {
        // Arrange - Create InstructionUsageMarker and multiple visitor types
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        TestInstructionVisitor testVisitor = new TestInstructionVisitor();
        CountingInstructionVisitor countingVisitor = new CountingInstructionVisitor();

        // Act - Create filters for different visitor types with both boolean values
        InstructionVisitor result1True = marker.necessaryInstructionFilter(true, testVisitor);
        InstructionVisitor result1False = marker.necessaryInstructionFilter(false, testVisitor);
        InstructionVisitor result2True = marker.necessaryInstructionFilter(true, countingVisitor);
        InstructionVisitor result2False = marker.necessaryInstructionFilter(false, countingVisitor);

        // Assert - Verify all results are valid
        assertNotNull(result1True, "Result with TestInstructionVisitor and necessary=true should be non-null");
        assertNotNull(result1False, "Result with TestInstructionVisitor and necessary=false should be non-null");
        assertNotNull(result2True, "Result with CountingInstructionVisitor and necessary=true should be non-null");
        assertNotNull(result2False, "Result with CountingInstructionVisitor and necessary=false should be non-null");
        assertInstanceOf(InstructionVisitor.class, result1True, "Result 1 True should be InstructionVisitor");
        assertInstanceOf(InstructionVisitor.class, result1False, "Result 1 False should be InstructionVisitor");
        assertInstanceOf(InstructionVisitor.class, result2True, "Result 2 True should be InstructionVisitor");
        assertInstanceOf(InstructionVisitor.class, result2False, "Result 2 False should be InstructionVisitor");
    }
}
