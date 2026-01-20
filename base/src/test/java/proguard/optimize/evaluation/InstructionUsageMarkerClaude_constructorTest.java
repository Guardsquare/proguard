package proguard.optimize.evaluation;

import org.junit.jupiter.api.Test;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.evaluation.PartialEvaluator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link InstructionUsageMarker} constructors.
 * Tests the InstructionUsageMarker(boolean) constructor,
 * the InstructionUsageMarker(PartialEvaluator, boolean, boolean) constructor,
 * and the InstructionUsageMarker(PartialEvaluator, boolean, boolean, boolean) constructor.
 */
public class InstructionUsageMarkerClaude_constructorTest {

    /**
     * Tests the constructor with markExternalSideEffects set to true.
     * Verifies that the InstructionUsageMarker instance can be instantiated with external side effects marking enabled.
     */
    @Test
    public void testConstructorWithMarkExternalSideEffectsTrue() {
        // Act - Create InstructionUsageMarker with markExternalSideEffects=true
        InstructionUsageMarker marker = new InstructionUsageMarker(true);

        // Assert - Verify the InstructionUsageMarker instance was created successfully
        assertNotNull(marker, "InstructionUsageMarker should be instantiated successfully with markExternalSideEffects=true");
    }

    /**
     * Tests the constructor with markExternalSideEffects set to false.
     * Verifies that the InstructionUsageMarker instance can be instantiated with external side effects marking disabled.
     */
    @Test
    public void testConstructorWithMarkExternalSideEffectsFalse() {
        // Act - Create InstructionUsageMarker with markExternalSideEffects=false
        InstructionUsageMarker marker = new InstructionUsageMarker(false);

        // Assert - Verify the InstructionUsageMarker instance was created successfully
        assertNotNull(marker, "InstructionUsageMarker should be instantiated successfully with markExternalSideEffects=false");
    }

    /**
     * Tests that the created InstructionUsageMarker is a valid AttributeVisitor.
     * Verifies that InstructionUsageMarker implements the AttributeVisitor interface.
     */
    @Test
    public void testConstructorCreatesValidAttributeVisitor() {
        // Act - Create InstructionUsageMarker
        InstructionUsageMarker marker = new InstructionUsageMarker(true);

        // Assert - Verify it implements AttributeVisitor
        assertInstanceOf(AttributeVisitor.class, marker,
                "InstructionUsageMarker should implement AttributeVisitor");
    }

    /**
     * Tests that multiple InstructionUsageMarker instances can be created independently.
     * Verifies that multiple instances are distinct objects.
     */
    @Test
    public void testMultipleInstructionUsageMarkerInstances() {
        // Act - Create two InstructionUsageMarker instances
        InstructionUsageMarker marker1 = new InstructionUsageMarker(true);
        InstructionUsageMarker marker2 = new InstructionUsageMarker(false);

        // Assert - Verify both instances were created and are different
        assertNotNull(marker1, "First InstructionUsageMarker should be created");
        assertNotNull(marker2, "Second InstructionUsageMarker should be created");
        assertNotSame(marker1, marker2, "InstructionUsageMarker instances should be different objects");
    }

    /**
     * Tests that the same boolean parameter value can be used to create multiple instances.
     * Verifies that creating multiple markers with the same parameter works correctly.
     */
    @Test
    public void testMultipleInstancesWithSameParameter() {
        // Act - Create two InstructionUsageMarker instances with the same parameter
        InstructionUsageMarker marker1 = new InstructionUsageMarker(true);
        InstructionUsageMarker marker2 = new InstructionUsageMarker(true);

        // Assert - Verify both instances were created and are different objects
        assertNotNull(marker1, "First InstructionUsageMarker should be created");
        assertNotNull(marker2, "Second InstructionUsageMarker should be created");
        assertNotSame(marker1, marker2, "InstructionUsageMarker instances should be different objects even with same parameter");
    }

    /**
     * Tests the constructor with markExternalSideEffects true creates an AttributeVisitor.
     * Verifies interface implementation when external side effects marking is enabled.
     */
    @Test
    public void testConstructorWithTrueCreatesAttributeVisitor() {
        // Act - Create InstructionUsageMarker with markExternalSideEffects=true
        InstructionUsageMarker marker = new InstructionUsageMarker(true);

        // Assert - Verify it implements AttributeVisitor
        assertInstanceOf(AttributeVisitor.class, marker,
                "InstructionUsageMarker with markExternalSideEffects=true should implement AttributeVisitor");
    }

    /**
     * Tests the constructor with markExternalSideEffects false creates an AttributeVisitor.
     * Verifies interface implementation when external side effects marking is disabled.
     */
    @Test
    public void testConstructorWithFalseCreatesAttributeVisitor() {
        // Act - Create InstructionUsageMarker with markExternalSideEffects=false
        InstructionUsageMarker marker = new InstructionUsageMarker(false);

        // Assert - Verify it implements AttributeVisitor
        assertInstanceOf(AttributeVisitor.class, marker,
                "InstructionUsageMarker with markExternalSideEffects=false should implement AttributeVisitor");
    }

    /**
     * Tests that the constructor can be called multiple times in sequence.
     * Verifies stability of the constructor when called repeatedly.
     */
    @Test
    public void testConstructorRepeatedInvocation() {
        // Act & Assert - Create multiple markers in sequence
        for (int i = 0; i < 5; i++) {
            boolean markExternalSideEffects = (i % 2 == 0);
            InstructionUsageMarker marker = new InstructionUsageMarker(markExternalSideEffects);
            assertNotNull(marker, "InstructionUsageMarker should be created on iteration " + i);
            assertInstanceOf(AttributeVisitor.class, marker,
                    "InstructionUsageMarker should implement AttributeVisitor on iteration " + i);
        }
    }

    /**
     * Tests that the constructor works correctly with both boolean values in alternating order.
     * Verifies that the constructor handles both parameter values correctly when alternated.
     */
    @Test
    public void testConstructorWithAlternatingBooleanValues() {
        // Act - Create markers with alternating boolean values
        InstructionUsageMarker marker1 = new InstructionUsageMarker(true);
        InstructionUsageMarker marker2 = new InstructionUsageMarker(false);
        InstructionUsageMarker marker3 = new InstructionUsageMarker(true);
        InstructionUsageMarker marker4 = new InstructionUsageMarker(false);

        // Assert - Verify all instances were created successfully
        assertNotNull(marker1, "InstructionUsageMarker with true should be created");
        assertNotNull(marker2, "InstructionUsageMarker with false should be created");
        assertNotNull(marker3, "InstructionUsageMarker with true (second) should be created");
        assertNotNull(marker4, "InstructionUsageMarker with false (second) should be created");

        // Verify they are all different instances
        assertNotSame(marker1, marker2, "marker1 and marker2 should be different");
        assertNotSame(marker1, marker3, "marker1 and marker3 should be different");
        assertNotSame(marker1, marker4, "marker1 and marker4 should be different");
        assertNotSame(marker2, marker3, "marker2 and marker3 should be different");
        assertNotSame(marker2, marker4, "marker2 and marker4 should be different");
        assertNotSame(marker3, marker4, "marker3 and marker4 should be different");
    }

    /**
     * Tests that the constructor does not throw any exceptions with markExternalSideEffects true.
     * Verifies exception-free construction with external side effects enabled.
     */
    @Test
    public void testConstructorDoesNotThrowExceptionWithTrue() {
        // Act & Assert - Verify no exception is thrown
        assertDoesNotThrow(() -> new InstructionUsageMarker(true),
                "Constructor should not throw exception with markExternalSideEffects=true");
    }

    /**
     * Tests that the constructor does not throw any exceptions with markExternalSideEffects false.
     * Verifies exception-free construction with external side effects disabled.
     */
    @Test
    public void testConstructorDoesNotThrowExceptionWithFalse() {
        // Act & Assert - Verify no exception is thrown
        assertDoesNotThrow(() -> new InstructionUsageMarker(false),
                "Constructor should not throw exception with markExternalSideEffects=false");
    }

    // ========== Tests for InstructionUsageMarker(PartialEvaluator, boolean, boolean) constructor ==========

    /**
     * Tests the 3-parameter constructor with all boolean parameters set to true.
     * Verifies that the InstructionUsageMarker can be instantiated with a PartialEvaluator and both flags enabled.
     */
    @Test
    public void testThreeParameterConstructorWithAllTrue() {
        // Arrange - Create PartialEvaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act - Create InstructionUsageMarker with all true
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true);

        // Assert - Verify the InstructionUsageMarker instance was created successfully
        assertNotNull(marker, "InstructionUsageMarker should be instantiated successfully with all parameters true");
    }

    /**
     * Tests the 3-parameter constructor with all boolean parameters set to false.
     * Verifies that the InstructionUsageMarker can be instantiated with both flags disabled.
     */
    @Test
    public void testThreeParameterConstructorWithAllFalse() {
        // Arrange - Create PartialEvaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act - Create InstructionUsageMarker with all false
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, false, false);

        // Assert - Verify the InstructionUsageMarker instance was created successfully
        assertNotNull(marker, "InstructionUsageMarker should be instantiated successfully with all parameters false");
    }

    /**
     * Tests the 3-parameter constructor with runPartialEvaluator true and markExternalSideEffects false.
     * Verifies that the constructor works with mixed boolean values.
     */
    @Test
    public void testThreeParameterConstructorWithRunTrueMarkFalse() {
        // Arrange - Create PartialEvaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act - Create InstructionUsageMarker with runPartialEvaluator=true, markExternalSideEffects=false
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, false);

        // Assert - Verify the InstructionUsageMarker instance was created successfully
        assertNotNull(marker, "InstructionUsageMarker should be created with runPartialEvaluator=true, markExternalSideEffects=false");
    }

    /**
     * Tests the 3-parameter constructor with runPartialEvaluator false and markExternalSideEffects true.
     * Verifies that the constructor works with mixed boolean values (opposite configuration).
     */
    @Test
    public void testThreeParameterConstructorWithRunFalseMarkTrue() {
        // Arrange - Create PartialEvaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act - Create InstructionUsageMarker with runPartialEvaluator=false, markExternalSideEffects=true
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, false, true);

        // Assert - Verify the InstructionUsageMarker instance was created successfully
        assertNotNull(marker, "InstructionUsageMarker should be created with runPartialEvaluator=false, markExternalSideEffects=true");
    }

    /**
     * Tests the 3-parameter constructor with all four possible boolean combinations.
     * Verifies that all combinations of boolean parameters work correctly.
     */
    @Test
    public void testThreeParameterConstructorWithAllBooleanCombinations() {
        // Arrange - Create PartialEvaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act & Assert - Test all 4 combinations of boolean flags
        InstructionUsageMarker marker1 = new InstructionUsageMarker(partialEvaluator, true, true);
        assertNotNull(marker1, "InstructionUsageMarker should work with (true, true)");

        InstructionUsageMarker marker2 = new InstructionUsageMarker(partialEvaluator, true, false);
        assertNotNull(marker2, "InstructionUsageMarker should work with (true, false)");

        InstructionUsageMarker marker3 = new InstructionUsageMarker(partialEvaluator, false, true);
        assertNotNull(marker3, "InstructionUsageMarker should work with (false, true)");

        InstructionUsageMarker marker4 = new InstructionUsageMarker(partialEvaluator, false, false);
        assertNotNull(marker4, "InstructionUsageMarker should work with (false, false)");
    }

    /**
     * Tests that the 3-parameter constructor creates a valid AttributeVisitor.
     * Verifies that the created instance implements the AttributeVisitor interface.
     */
    @Test
    public void testThreeParameterConstructorCreatesAttributeVisitor() {
        // Arrange - Create PartialEvaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act - Create InstructionUsageMarker
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true);

        // Assert - Verify it implements AttributeVisitor
        assertInstanceOf(AttributeVisitor.class, marker,
                "InstructionUsageMarker should implement AttributeVisitor");
    }

    /**
     * Tests that multiple InstructionUsageMarker instances can be created with the 3-parameter constructor.
     * Verifies that multiple instances are distinct objects.
     */
    @Test
    public void testThreeParameterConstructorMultipleInstances() {
        // Arrange - Create PartialEvaluators
        PartialEvaluator partialEvaluator1 = PartialEvaluator.Builder.create().build();
        PartialEvaluator partialEvaluator2 = PartialEvaluator.Builder.create().build();

        // Act - Create two InstructionUsageMarker instances
        InstructionUsageMarker marker1 = new InstructionUsageMarker(partialEvaluator1, true, true);
        InstructionUsageMarker marker2 = new InstructionUsageMarker(partialEvaluator2, false, false);

        // Assert - Verify both instances were created and are different
        assertNotNull(marker1, "First InstructionUsageMarker should be created");
        assertNotNull(marker2, "Second InstructionUsageMarker should be created");
        assertNotSame(marker1, marker2, "InstructionUsageMarker instances should be different objects");
    }

    /**
     * Tests that the same PartialEvaluator can be used for multiple InstructionUsageMarker instances.
     * Verifies that multiple markers can share the same evaluator.
     */
    @Test
    public void testThreeParameterConstructorWithSharedPartialEvaluator() {
        // Arrange - Create a single PartialEvaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act - Create multiple InstructionUsageMarker instances with the same evaluator
        InstructionUsageMarker marker1 = new InstructionUsageMarker(partialEvaluator, true, true);
        InstructionUsageMarker marker2 = new InstructionUsageMarker(partialEvaluator, false, false);

        // Assert - Verify both instances were created successfully
        assertNotNull(marker1, "First InstructionUsageMarker should be created");
        assertNotNull(marker2, "Second InstructionUsageMarker should be created");
        assertNotSame(marker1, marker2, "Different InstructionUsageMarker instances should be different objects");
    }

    /**
     * Tests that the 3-parameter constructor works with different PartialEvaluator configurations.
     * Verifies compatibility with various PartialEvaluator instances.
     */
    @Test
    public void testThreeParameterConstructorWithDifferentPartialEvaluators() {
        // Arrange & Act - Create InstructionUsageMarkers with different PartialEvaluator configurations
        PartialEvaluator evaluator1 = PartialEvaluator.Builder.create().build();
        PartialEvaluator evaluator2 = PartialEvaluator.Builder.create().build();

        InstructionUsageMarker marker1 = new InstructionUsageMarker(evaluator1, true, false);
        InstructionUsageMarker marker2 = new InstructionUsageMarker(evaluator2, false, true);

        // Assert - Verify both instances were created successfully
        assertNotNull(marker1, "InstructionUsageMarker should work with first PartialEvaluator");
        assertNotNull(marker2, "InstructionUsageMarker should work with second PartialEvaluator");
    }

    /**
     * Tests the 3-parameter constructor with runPartialEvaluator set to true.
     * Verifies that the constructor works correctly when partial evaluation is enabled.
     */
    @Test
    public void testThreeParameterConstructorWithRunPartialEvaluatorTrue() {
        // Arrange - Create PartialEvaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act - Create InstructionUsageMarker with runPartialEvaluator=true
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, false);

        // Assert - Verify the InstructionUsageMarker instance was created successfully
        assertNotNull(marker, "InstructionUsageMarker should be created with runPartialEvaluator=true");
    }

    /**
     * Tests the 3-parameter constructor with runPartialEvaluator set to false.
     * Verifies that the constructor works correctly when partial evaluation is disabled.
     */
    @Test
    public void testThreeParameterConstructorWithRunPartialEvaluatorFalse() {
        // Arrange - Create PartialEvaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act - Create InstructionUsageMarker with runPartialEvaluator=false
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, false, true);

        // Assert - Verify the InstructionUsageMarker instance was created successfully
        assertNotNull(marker, "InstructionUsageMarker should be created with runPartialEvaluator=false");
    }

    /**
     * Tests the 3-parameter constructor with markExternalSideEffects set to true.
     * Verifies that the constructor works correctly when external side effects marking is enabled.
     */
    @Test
    public void testThreeParameterConstructorWithMarkExternalSideEffectsTrue() {
        // Arrange - Create PartialEvaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act - Create InstructionUsageMarker with markExternalSideEffects=true
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, false, true);

        // Assert - Verify the InstructionUsageMarker instance was created successfully
        assertNotNull(marker, "InstructionUsageMarker should be created with markExternalSideEffects=true");
    }

    /**
     * Tests the 3-parameter constructor with markExternalSideEffects set to false.
     * Verifies that the constructor works correctly when external side effects marking is disabled.
     */
    @Test
    public void testThreeParameterConstructorWithMarkExternalSideEffectsFalse() {
        // Arrange - Create PartialEvaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act - Create InstructionUsageMarker with markExternalSideEffects=false
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, false);

        // Assert - Verify the InstructionUsageMarker instance was created successfully
        assertNotNull(marker, "InstructionUsageMarker should be created with markExternalSideEffects=false");
    }

    /**
     * Tests that the 3-parameter constructor does not throw exceptions with valid parameters.
     * Verifies exception-free construction.
     */
    @Test
    public void testThreeParameterConstructorDoesNotThrowException() {
        // Arrange - Create PartialEvaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act & Assert - Verify no exception is thrown
        assertDoesNotThrow(() -> new InstructionUsageMarker(partialEvaluator, true, true),
                "Constructor should not throw exception with valid parameters");
    }

    /**
     * Tests that the 3-parameter constructor can be called multiple times in sequence.
     * Verifies stability of the constructor when called repeatedly.
     */
    @Test
    public void testThreeParameterConstructorRepeatedInvocation() {
        // Arrange - Create PartialEvaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act & Assert - Create multiple markers in sequence
        for (int i = 0; i < 5; i++) {
            boolean runPartialEvaluator = (i % 2 == 0);
            boolean markExternalSideEffects = (i % 3 == 0);
            InstructionUsageMarker marker = new InstructionUsageMarker(
                    partialEvaluator, runPartialEvaluator, markExternalSideEffects);
            assertNotNull(marker, "InstructionUsageMarker should be created on iteration " + i);
            assertInstanceOf(AttributeVisitor.class, marker,
                    "InstructionUsageMarker should implement AttributeVisitor on iteration " + i);
        }
    }

    /**
     * Tests the 3-parameter constructor with multiple instances using same parameter combinations.
     * Verifies that instances with same parameters are still distinct objects.
     */
    @Test
    public void testThreeParameterConstructorMultipleInstancesSameParameters() {
        // Arrange - Create PartialEvaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act - Create two InstructionUsageMarker instances with same parameters
        InstructionUsageMarker marker1 = new InstructionUsageMarker(partialEvaluator, true, true);
        InstructionUsageMarker marker2 = new InstructionUsageMarker(partialEvaluator, true, true);

        // Assert - Verify both instances were created and are different objects
        assertNotNull(marker1, "First InstructionUsageMarker should be created");
        assertNotNull(marker2, "Second InstructionUsageMarker should be created");
        assertNotSame(marker1, marker2, "InstructionUsageMarker instances should be different objects even with same parameters");
    }

    /**
     * Tests that the 3-parameter constructor creates instances implementing AttributeVisitor with all combinations.
     * Verifies interface implementation across all parameter combinations.
     */
    @Test
    public void testThreeParameterConstructorAlwaysCreatesAttributeVisitor() {
        // Arrange - Create PartialEvaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act & Assert - Test all 4 combinations
        InstructionUsageMarker marker1 = new InstructionUsageMarker(partialEvaluator, true, true);
        assertInstanceOf(AttributeVisitor.class, marker1, "Marker with (true, true) should implement AttributeVisitor");

        InstructionUsageMarker marker2 = new InstructionUsageMarker(partialEvaluator, true, false);
        assertInstanceOf(AttributeVisitor.class, marker2, "Marker with (true, false) should implement AttributeVisitor");

        InstructionUsageMarker marker3 = new InstructionUsageMarker(partialEvaluator, false, true);
        assertInstanceOf(AttributeVisitor.class, marker3, "Marker with (false, true) should implement AttributeVisitor");

        InstructionUsageMarker marker4 = new InstructionUsageMarker(partialEvaluator, false, false);
        assertInstanceOf(AttributeVisitor.class, marker4, "Marker with (false, false) should implement AttributeVisitor");
    }

    // ========== Tests for InstructionUsageMarker(PartialEvaluator, boolean, boolean, boolean) constructor ==========

    /**
     * Tests the 4-parameter constructor with all boolean parameters set to true.
     * Verifies that the InstructionUsageMarker can be instantiated with all flags enabled.
     */
    @Test
    public void testFourParameterConstructorWithAllTrue() {
        // Arrange - Create PartialEvaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act - Create InstructionUsageMarker with all true
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Assert - Verify the InstructionUsageMarker instance was created successfully
        assertNotNull(marker, "InstructionUsageMarker should be instantiated successfully with all parameters true");
    }

    /**
     * Tests the 4-parameter constructor with all boolean parameters set to false.
     * Verifies that the InstructionUsageMarker can be instantiated with all flags disabled.
     */
    @Test
    public void testFourParameterConstructorWithAllFalse() {
        // Arrange - Create PartialEvaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act - Create InstructionUsageMarker with all false
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, false, false, false);

        // Assert - Verify the InstructionUsageMarker instance was created successfully
        assertNotNull(marker, "InstructionUsageMarker should be instantiated successfully with all parameters false");
    }

    /**
     * Tests the 4-parameter constructor with all 8 possible boolean combinations.
     * Verifies that all combinations of boolean parameters work correctly.
     */
    @Test
    public void testFourParameterConstructorWithAllBooleanCombinations() {
        // Arrange - Create PartialEvaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act & Assert - Test all 8 combinations of boolean flags
        InstructionUsageMarker marker1 = new InstructionUsageMarker(partialEvaluator, true, true, true);
        assertNotNull(marker1, "InstructionUsageMarker should work with (true, true, true)");

        InstructionUsageMarker marker2 = new InstructionUsageMarker(partialEvaluator, true, true, false);
        assertNotNull(marker2, "InstructionUsageMarker should work with (true, true, false)");

        InstructionUsageMarker marker3 = new InstructionUsageMarker(partialEvaluator, true, false, true);
        assertNotNull(marker3, "InstructionUsageMarker should work with (true, false, true)");

        InstructionUsageMarker marker4 = new InstructionUsageMarker(partialEvaluator, true, false, false);
        assertNotNull(marker4, "InstructionUsageMarker should work with (true, false, false)");

        InstructionUsageMarker marker5 = new InstructionUsageMarker(partialEvaluator, false, true, true);
        assertNotNull(marker5, "InstructionUsageMarker should work with (false, true, true)");

        InstructionUsageMarker marker6 = new InstructionUsageMarker(partialEvaluator, false, true, false);
        assertNotNull(marker6, "InstructionUsageMarker should work with (false, true, false)");

        InstructionUsageMarker marker7 = new InstructionUsageMarker(partialEvaluator, false, false, true);
        assertNotNull(marker7, "InstructionUsageMarker should work with (false, false, true)");

        InstructionUsageMarker marker8 = new InstructionUsageMarker(partialEvaluator, false, false, false);
        assertNotNull(marker8, "InstructionUsageMarker should work with (false, false, false)");
    }

    /**
     * Tests that the 4-parameter constructor creates a valid AttributeVisitor.
     * Verifies that the created instance implements the AttributeVisitor interface.
     */
    @Test
    public void testFourParameterConstructorCreatesAttributeVisitor() {
        // Arrange - Create PartialEvaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act - Create InstructionUsageMarker
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Assert - Verify it implements AttributeVisitor
        assertInstanceOf(AttributeVisitor.class, marker,
                "InstructionUsageMarker should implement AttributeVisitor");
    }

    /**
     * Tests the 4-parameter constructor with runPartialEvaluator set to true.
     * Verifies that the constructor works correctly when partial evaluation is enabled.
     */
    @Test
    public void testFourParameterConstructorWithRunPartialEvaluatorTrue() {
        // Arrange - Create PartialEvaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act - Create InstructionUsageMarker with runPartialEvaluator=true
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, false, false);

        // Assert - Verify the InstructionUsageMarker instance was created successfully
        assertNotNull(marker, "InstructionUsageMarker should be created with runPartialEvaluator=true");
    }

    /**
     * Tests the 4-parameter constructor with runPartialEvaluator set to false.
     * Verifies that the constructor works correctly when partial evaluation is disabled.
     */
    @Test
    public void testFourParameterConstructorWithRunPartialEvaluatorFalse() {
        // Arrange - Create PartialEvaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act - Create InstructionUsageMarker with runPartialEvaluator=false
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, false, true, true);

        // Assert - Verify the InstructionUsageMarker instance was created successfully
        assertNotNull(marker, "InstructionUsageMarker should be created with runPartialEvaluator=false");
    }

    /**
     * Tests the 4-parameter constructor with ensureSafetyForVerifier set to true.
     * Verifies that the constructor works correctly when verifier safety is enabled.
     */
    @Test
    public void testFourParameterConstructorWithEnsureSafetyForVerifierTrue() {
        // Arrange - Create PartialEvaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act - Create InstructionUsageMarker with ensureSafetyForVerifier=true
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, false, true, false);

        // Assert - Verify the InstructionUsageMarker instance was created successfully
        assertNotNull(marker, "InstructionUsageMarker should be created with ensureSafetyForVerifier=true");
    }

    /**
     * Tests the 4-parameter constructor with ensureSafetyForVerifier set to false.
     * Verifies that the constructor works correctly when verifier safety is disabled.
     */
    @Test
    public void testFourParameterConstructorWithEnsureSafetyForVerifierFalse() {
        // Arrange - Create PartialEvaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act - Create InstructionUsageMarker with ensureSafetyForVerifier=false
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, false, true);

        // Assert - Verify the InstructionUsageMarker instance was created successfully
        assertNotNull(marker, "InstructionUsageMarker should be created with ensureSafetyForVerifier=false");
    }

    /**
     * Tests the 4-parameter constructor with markExternalSideEffects set to true.
     * Verifies that the constructor works correctly when external side effects marking is enabled.
     */
    @Test
    public void testFourParameterConstructorWithMarkExternalSideEffectsTrue() {
        // Arrange - Create PartialEvaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act - Create InstructionUsageMarker with markExternalSideEffects=true
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, false, false, true);

        // Assert - Verify the InstructionUsageMarker instance was created successfully
        assertNotNull(marker, "InstructionUsageMarker should be created with markExternalSideEffects=true");
    }

    /**
     * Tests the 4-parameter constructor with markExternalSideEffects set to false.
     * Verifies that the constructor works correctly when external side effects marking is disabled.
     */
    @Test
    public void testFourParameterConstructorWithMarkExternalSideEffectsFalse() {
        // Arrange - Create PartialEvaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act - Create InstructionUsageMarker with markExternalSideEffects=false
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, false);

        // Assert - Verify the InstructionUsageMarker instance was created successfully
        assertNotNull(marker, "InstructionUsageMarker should be created with markExternalSideEffects=false");
    }

    /**
     * Tests that multiple InstructionUsageMarker instances can be created with the 4-parameter constructor.
     * Verifies that multiple instances are distinct objects.
     */
    @Test
    public void testFourParameterConstructorMultipleInstances() {
        // Arrange - Create PartialEvaluators
        PartialEvaluator partialEvaluator1 = PartialEvaluator.Builder.create().build();
        PartialEvaluator partialEvaluator2 = PartialEvaluator.Builder.create().build();

        // Act - Create two InstructionUsageMarker instances
        InstructionUsageMarker marker1 = new InstructionUsageMarker(partialEvaluator1, true, true, true);
        InstructionUsageMarker marker2 = new InstructionUsageMarker(partialEvaluator2, false, false, false);

        // Assert - Verify both instances were created and are different
        assertNotNull(marker1, "First InstructionUsageMarker should be created");
        assertNotNull(marker2, "Second InstructionUsageMarker should be created");
        assertNotSame(marker1, marker2, "InstructionUsageMarker instances should be different objects");
    }

    /**
     * Tests that the same PartialEvaluator can be used for multiple InstructionUsageMarker instances.
     * Verifies that multiple markers can share the same evaluator.
     */
    @Test
    public void testFourParameterConstructorWithSharedPartialEvaluator() {
        // Arrange - Create a single PartialEvaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act - Create multiple InstructionUsageMarker instances with the same evaluator
        InstructionUsageMarker marker1 = new InstructionUsageMarker(partialEvaluator, true, true, true);
        InstructionUsageMarker marker2 = new InstructionUsageMarker(partialEvaluator, false, false, false);

        // Assert - Verify both instances were created successfully
        assertNotNull(marker1, "First InstructionUsageMarker should be created");
        assertNotNull(marker2, "Second InstructionUsageMarker should be created");
        assertNotSame(marker1, marker2, "Different InstructionUsageMarker instances should be different objects");
    }

    /**
     * Tests that the 4-parameter constructor works with different PartialEvaluator configurations.
     * Verifies compatibility with various PartialEvaluator instances.
     */
    @Test
    public void testFourParameterConstructorWithDifferentPartialEvaluators() {
        // Arrange & Act - Create InstructionUsageMarkers with different PartialEvaluator configurations
        PartialEvaluator evaluator1 = PartialEvaluator.Builder.create().build();
        PartialEvaluator evaluator2 = PartialEvaluator.Builder.create().build();

        InstructionUsageMarker marker1 = new InstructionUsageMarker(evaluator1, true, false, true);
        InstructionUsageMarker marker2 = new InstructionUsageMarker(evaluator2, false, true, false);

        // Assert - Verify both instances were created successfully
        assertNotNull(marker1, "InstructionUsageMarker should work with first PartialEvaluator");
        assertNotNull(marker2, "InstructionUsageMarker should work with second PartialEvaluator");
    }

    /**
     * Tests that the 4-parameter constructor does not throw exceptions with valid parameters.
     * Verifies exception-free construction.
     */
    @Test
    public void testFourParameterConstructorDoesNotThrowException() {
        // Arrange - Create PartialEvaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act & Assert - Verify no exception is thrown
        assertDoesNotThrow(() -> new InstructionUsageMarker(partialEvaluator, true, true, true),
                "Constructor should not throw exception with valid parameters");
    }

    /**
     * Tests that the 4-parameter constructor can be called multiple times in sequence.
     * Verifies stability of the constructor when called repeatedly.
     */
    @Test
    public void testFourParameterConstructorRepeatedInvocation() {
        // Arrange - Create PartialEvaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act & Assert - Create multiple markers in sequence
        for (int i = 0; i < 8; i++) {
            boolean runPartialEvaluator = (i % 2 == 0);
            boolean ensureSafetyForVerifier = (i % 3 == 0);
            boolean markExternalSideEffects = (i % 4 == 0);
            InstructionUsageMarker marker = new InstructionUsageMarker(
                    partialEvaluator, runPartialEvaluator, ensureSafetyForVerifier, markExternalSideEffects);
            assertNotNull(marker, "InstructionUsageMarker should be created on iteration " + i);
            assertInstanceOf(AttributeVisitor.class, marker,
                    "InstructionUsageMarker should implement AttributeVisitor on iteration " + i);
        }
    }

    /**
     * Tests the 4-parameter constructor with multiple instances using same parameter combinations.
     * Verifies that instances with same parameters are still distinct objects.
     */
    @Test
    public void testFourParameterConstructorMultipleInstancesSameParameters() {
        // Arrange - Create PartialEvaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act - Create two InstructionUsageMarker instances with same parameters
        InstructionUsageMarker marker1 = new InstructionUsageMarker(partialEvaluator, true, true, true);
        InstructionUsageMarker marker2 = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Assert - Verify both instances were created and are different objects
        assertNotNull(marker1, "First InstructionUsageMarker should be created");
        assertNotNull(marker2, "Second InstructionUsageMarker should be created");
        assertNotSame(marker1, marker2, "InstructionUsageMarker instances should be different objects even with same parameters");
    }

    /**
     * Tests that the 4-parameter constructor creates instances implementing AttributeVisitor with all combinations.
     * Verifies interface implementation across all parameter combinations.
     */
    @Test
    public void testFourParameterConstructorAlwaysCreatesAttributeVisitor() {
        // Arrange - Create PartialEvaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act & Assert - Test all 8 combinations
        InstructionUsageMarker marker1 = new InstructionUsageMarker(partialEvaluator, true, true, true);
        assertInstanceOf(AttributeVisitor.class, marker1, "Marker with (true, true, true) should implement AttributeVisitor");

        InstructionUsageMarker marker2 = new InstructionUsageMarker(partialEvaluator, true, true, false);
        assertInstanceOf(AttributeVisitor.class, marker2, "Marker with (true, true, false) should implement AttributeVisitor");

        InstructionUsageMarker marker3 = new InstructionUsageMarker(partialEvaluator, true, false, true);
        assertInstanceOf(AttributeVisitor.class, marker3, "Marker with (true, false, true) should implement AttributeVisitor");

        InstructionUsageMarker marker4 = new InstructionUsageMarker(partialEvaluator, true, false, false);
        assertInstanceOf(AttributeVisitor.class, marker4, "Marker with (true, false, false) should implement AttributeVisitor");

        InstructionUsageMarker marker5 = new InstructionUsageMarker(partialEvaluator, false, true, true);
        assertInstanceOf(AttributeVisitor.class, marker5, "Marker with (false, true, true) should implement AttributeVisitor");

        InstructionUsageMarker marker6 = new InstructionUsageMarker(partialEvaluator, false, true, false);
        assertInstanceOf(AttributeVisitor.class, marker6, "Marker with (false, true, false) should implement AttributeVisitor");

        InstructionUsageMarker marker7 = new InstructionUsageMarker(partialEvaluator, false, false, true);
        assertInstanceOf(AttributeVisitor.class, marker7, "Marker with (false, false, true) should implement AttributeVisitor");

        InstructionUsageMarker marker8 = new InstructionUsageMarker(partialEvaluator, false, false, false);
        assertInstanceOf(AttributeVisitor.class, marker8, "Marker with (false, false, false) should implement AttributeVisitor");
    }

    /**
     * Tests the 4-parameter constructor with various combinations of first two parameters.
     * Verifies that runPartialEvaluator and ensureSafetyForVerifier work together correctly.
     */
    @Test
    public void testFourParameterConstructorWithRunAndSafetyCombinations() {
        // Arrange - Create PartialEvaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act & Assert - Test combinations of runPartialEvaluator and ensureSafetyForVerifier
        InstructionUsageMarker marker1 = new InstructionUsageMarker(partialEvaluator, true, true, false);
        assertNotNull(marker1, "Should work with runPartialEvaluator=true, ensureSafetyForVerifier=true");

        InstructionUsageMarker marker2 = new InstructionUsageMarker(partialEvaluator, true, false, false);
        assertNotNull(marker2, "Should work with runPartialEvaluator=true, ensureSafetyForVerifier=false");

        InstructionUsageMarker marker3 = new InstructionUsageMarker(partialEvaluator, false, true, false);
        assertNotNull(marker3, "Should work with runPartialEvaluator=false, ensureSafetyForVerifier=true");

        InstructionUsageMarker marker4 = new InstructionUsageMarker(partialEvaluator, false, false, false);
        assertNotNull(marker4, "Should work with runPartialEvaluator=false, ensureSafetyForVerifier=false");
    }

    /**
     * Tests the 4-parameter constructor with various combinations of last two parameters.
     * Verifies that ensureSafetyForVerifier and markExternalSideEffects work together correctly.
     */
    @Test
    public void testFourParameterConstructorWithSafetyAndMarkCombinations() {
        // Arrange - Create PartialEvaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act & Assert - Test combinations of ensureSafetyForVerifier and markExternalSideEffects
        InstructionUsageMarker marker1 = new InstructionUsageMarker(partialEvaluator, false, true, true);
        assertNotNull(marker1, "Should work with ensureSafetyForVerifier=true, markExternalSideEffects=true");

        InstructionUsageMarker marker2 = new InstructionUsageMarker(partialEvaluator, false, true, false);
        assertNotNull(marker2, "Should work with ensureSafetyForVerifier=true, markExternalSideEffects=false");

        InstructionUsageMarker marker3 = new InstructionUsageMarker(partialEvaluator, false, false, true);
        assertNotNull(marker3, "Should work with ensureSafetyForVerifier=false, markExternalSideEffects=true");

        InstructionUsageMarker marker4 = new InstructionUsageMarker(partialEvaluator, false, false, false);
        assertNotNull(marker4, "Should work with ensureSafetyForVerifier=false, markExternalSideEffects=false");
    }

    /**
     * Tests the 4-parameter constructor with various combinations of outer parameters.
     * Verifies that runPartialEvaluator and markExternalSideEffects work together correctly.
     */
    @Test
    public void testFourParameterConstructorWithRunAndMarkCombinations() {
        // Arrange - Create PartialEvaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act & Assert - Test combinations of runPartialEvaluator and markExternalSideEffects
        InstructionUsageMarker marker1 = new InstructionUsageMarker(partialEvaluator, true, false, true);
        assertNotNull(marker1, "Should work with runPartialEvaluator=true, markExternalSideEffects=true");

        InstructionUsageMarker marker2 = new InstructionUsageMarker(partialEvaluator, true, false, false);
        assertNotNull(marker2, "Should work with runPartialEvaluator=true, markExternalSideEffects=false");

        InstructionUsageMarker marker3 = new InstructionUsageMarker(partialEvaluator, false, false, true);
        assertNotNull(marker3, "Should work with runPartialEvaluator=false, markExternalSideEffects=true");

        InstructionUsageMarker marker4 = new InstructionUsageMarker(partialEvaluator, false, false, false);
        assertNotNull(marker4, "Should work with runPartialEvaluator=false, markExternalSideEffects=false");
    }
}
